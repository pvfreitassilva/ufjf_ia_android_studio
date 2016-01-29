//TODO tempo de execução

package br.edu.ufjf;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import br.edu.ufjf.ai.Search;
import br.edu.ufjf.core.Board;
import br.edu.ufjf.core.Enemy;

public class GameView extends View{

	private Board board;
	private Paint paint;
	private int gameState;
    private int mapTranslationX;
    private int mapTranslationY;
    private int squareSize;

    private int lastX, lastY;

	public GameView(Context context) {
		super(context);
		board = new Board();
		paint = new Paint();
		gameState = Board.BUILDING_BOARD;
        lastX=lastY=-1;
	}
	
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
        squareSize = Board.GRID_Y_SIZE > Board.GRID_X_SIZE ? getHeight() / Board.GRID_Y_SIZE : getWidth() / Board.GRID_X_SIZE ;
        mapTranslationX = ( getWidth()  - squareSize * Board.GRID_X_SIZE)/2;
        mapTranslationY = ( getHeight() - squareSize * Board.GRID_Y_SIZE)/2;
        canvas.translate(mapTranslationX, mapTranslationY);
		paint.setColor(Color.WHITE);
		drawBoard(canvas);
		drawLines(canvas);
        int enemiesTranslations[][] = { {- squareSize / 4, + squareSize / 4 },
                                        {+ squareSize / 4, + squareSize / 4 },
                                        {+ squareSize / 4, - squareSize / 4 },
                                        {- squareSize / 4, - squareSize / 4 },
                                        {0, 0 },};
		
		paint.setColor(Color.GREEN);
		canvas.drawCircle(  board.player.point.x * squareSize + squareSize/2,
                            board.player.point.y * squareSize + squareSize/2,
                            squareSize/2, paint);

        int pathTranslation = 0;

		for(Enemy enemy : board.enemies){

			paint.setColor(enemy.color);
			for(Point p : enemy.getPath()) {
				//canvas.drawCircle(p.x * squareSize + squareSize / 2 - squareSize / 4, p.y * squareSize + squareSize / 2 + squareSize / 4, squareSize / 4, paint);
                canvas.drawCircle(  p.x * squareSize + squareSize / 2 + enemiesTranslations[pathTranslation][0],
                                    p.y * squareSize + squareSize / 2 + enemiesTranslations[pathTranslation][1],
                                    squareSize/6, paint);
			}

            //if(enemy.getPath()==null || enemy.getPath().isEmpty()) paint.setColor(Color.BLACK);
			//else
			paint.setColor(enemy.color);

			canvas.drawCircle(  enemy.point.x * squareSize + squareSize/2 + enemiesTranslations[pathTranslation][0],
                                enemy.point.y * squareSize + squareSize/2 + enemiesTranslations[pathTranslation][1],
                                squareSize/4, paint);

            pathTranslation = (pathTranslation + 1)%5;

		}
		
		if(gameState == Board.GAMEOVER){
			showGameOverDialog();
		}
		else if(gameState == Board.PLAYERWIN){
			showPlayerWinDialog();
		}
		
	}
	
	private void drawBoard(Canvas canvas){

        //int squareSize = Board.GRID_Y_SIZE > Board.GRID_X_SIZE ? getHeight() / Board.GRID_Y_SIZE : getWidth() / Board.GRID_X_SIZE ;
		
		for(int i = 0; i< Board.GRID_X_SIZE; i++){
			for(int j = 0; j < Board.GRID_Y_SIZE; j++){ 
				if(Board.grid[i][j]==Board.EMPTY){
					paint.setColor(Color.WHITE);
				}
				else if(Board.grid[i][j]==Board.BARRIER){
					paint.setColor(Color.BLACK);					
				} else if(Board.grid[i][j]==Board.GOAL){
					paint.setColor(Color.YELLOW);
				}
				canvas.drawRect(i * squareSize,
                                j * squareSize,
                                i * squareSize + squareSize,
                                j * squareSize + squareSize,
                                paint);
			}
		}
	}
	
	private void drawLines(Canvas canvas){

        //int squareSize = Board.GRID_Y_SIZE > Board.GRID_X_SIZE ? getHeight() / Board.GRID_Y_SIZE : getWidth() / Board.GRID_X_SIZE ;

		paint.setColor(Color.BLACK);

        for(int i = 0; i <= Board.GRID_X_SIZE; i++){
            canvas.drawLine(i*squareSize,
                            0,
                            i*squareSize,
                            Board.GRID_Y_SIZE*squareSize,
                            paint);
        }

        for(int j = 0; j <= Board.GRID_Y_SIZE; j++){
            canvas.drawLine(0,
                            j*squareSize,
                            Board.GRID_X_SIZE*squareSize,
                            j*squareSize,
                            paint);
        }


		
	}
	
    @Override
	public boolean onTouchEvent(MotionEvent event){

        int eventX =  (int) (event.getX() - mapTranslationX)/squareSize;
        int eventY =  (int) (event.getY() - mapTranslationY)/squareSize;

        switch (gameState){
            case Board.BUILDING_BOARD : {
                showBuildingMapDialog(eventX, eventY);
                return false;
            }
            case Board.BUILDING_MAP : {

                if(lastX!=eventX || lastY!=eventY) {
                    if(!board.changeGrid(eventX, eventY)) {
                        gameState = Board.BUILDING_BOARD;
                        Toast.makeText(getContext(), "Edição do mapa concluída", Toast.LENGTH_SHORT).show();
                        lastX=lastY=-1;
                        board.graph.rebuildGraph();
                        return false;
                    }
                    invalidate();
                    lastX = eventX;
                    lastY = eventY;
                }

                if(event.getAction() == MotionEvent.ACTION_UP)
                    lastX=lastY=-1;
                return true;
            }
            case Board.ADDING_NEW_ENEMY : {
                if(board.isValidPosition(eventX, eventY))
                    showNewEnemyDialog(eventX, eventY);
                else
                    Toast.makeText(getContext(), "Posição inválida! Ação cancelada", Toast.LENGTH_SHORT).show();
                gameState = Board.BUILDING_BOARD;
                return false;
            }
            case Board.CHANGING_PLAYER_POSITION : {
                if(board.isValidPosition(eventX, eventY)) {
                    board.player.point.set(eventX, eventY);
                    Toast.makeText(getContext(), "Posição do jogador alterada ("+eventX +", "+ eventY+")", Toast.LENGTH_SHORT).show();
                    invalidate();
                }
                else
                    Toast.makeText(getContext(), "Posição inválida! Ação cancelada", Toast.LENGTH_SHORT).show();
                gameState = Board.BUILDING_BOARD;
                return false;
            }
            case Board.CHANGING_GOAL_POSITION : {
                if(board.isValidPosition(eventX, eventY)) {
                    board.changeGoal(eventX, eventY);
                    Toast.makeText(getContext(), "Posição do objetivo alterada ("+eventX +", "+ eventY+")", Toast.LENGTH_SHORT).show();
                    invalidate();
                }
                else
                    Toast.makeText(getContext(), "Posição inválida! Ação cancelada", Toast.LENGTH_SHORT).show();
                gameState = Board.BUILDING_BOARD;
                return false;
            }
            case Board.RESUME : {
                try{
                    if(event.getAction()==MotionEvent.ACTION_DOWN || event.getAction()==MotionEvent.ACTION_MOVE){
                        board.makePlayerMove(eventX, eventY);
                    }
                    gameState = board.getGameState();
                    invalidate();
                }
                catch(Exception e){
                    Log.d("Exception", e.toString());
                }
                return true;
            }
        }
		return false;
	}

    private void showGameOverDialog() {
		final String items[] = { "Nova Partida", "Retornar ao menu", "Cancelar" };
		AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
		ab.setTitle("Você perdeu :(");
		ab.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface d, int choice) {
				if (choice == 0) {
					board = new Board();
					gameState = Board.BUILDING_BOARD;
					invalidate();
				} else if (choice == 1) {
					//Intent i = new Intent(getContext(), Menu.class);
					//getContext().startActivity(i);
				}
			}
		});
		ab.setCancelable(false);
		ab.show();
	}

    private void showPlayerWinDialog() {
		final String items[] = { "Próxima fase", "Retornar ao menu", "Cancelar" };
		AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
		ab.setTitle("Você gaanhou :D");
		ab.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface d, int choice) {
				if (choice == 0) {
					board = new Board();
					gameState = Board.BUILDING_BOARD;
					invalidate();
				} else if (choice == 1) {
					//Intent i = new Intent(getContext(), Menu.class);
					//getContext().startActivity(i);
				}
			}
		});
		ab.setCancelable(false);
		ab.show();
	}

    private void showEnemyColorDialog(final int x, final int y, final int type) {
        final String items[] = { "Vermelho", "Azul", "Rosa", "Cinza", "Preto", "Amarelo", "Ciano", "Cancelar" };
        AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
        ab.setTitle("Selecione a cor do inimigo");
        ab.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface d, int choice) {
                switch (choice){
                    case 0 :{
                        board.addEnemy(x, y, type, Color.RED);
                        Toast.makeText(getContext(), "Inimigo adicionado (" + x + ", "+y+")", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 1 :{
                        board.addEnemy(x, y, type, Color.BLUE);
                        Toast.makeText(getContext(), "Inimigo adicionado (" + x + ", "+y+")", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 2 :{
                        board.addEnemy(x, y, type, Color.MAGENTA);
                        Toast.makeText(getContext(), "Inimigo adicionado (" + x + ", "+y+")", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 3 :{
                        board.addEnemy(x, y, type, Color.GRAY);
                        Toast.makeText(getContext(), "Inimigo adicionado (" + x + ", "+y+")", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 4 :{
                        board.addEnemy(x, y, type, Color.BLACK);
                        Toast.makeText(getContext(), "Inimigo adicionado (" + x + ", "+y+")", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 5 :{
                        board.addEnemy(x, y, type, Color.YELLOW);
                        Toast.makeText(getContext(), "Inimigo adicionado (" + x + ", "+y+")", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 6 :{
                        board.addEnemy(x, y, type, Color.CYAN);
                        Toast.makeText(getContext(), "Inimigo adicionado (" + x + ", "+y+")", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                invalidate();
            }
        });
        ab.setCancelable(true);
        ab.show();
    }


    private void showNewEnemyDialog(final int x, final int y) {
        final String items[] = { "Largura", "Profundidade", "Ordenada", "Gulosa", "A*", "Cancelar" };
        AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
        ab.setTitle("Selecione o tipo de busca");
        ab.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface d, int choice) {

                switch (choice){

                    case 0 :{
                        showEnemyColorDialog(x,y,Search.LARGURA);
                        break;
                    }
                    case 1 :{
                        showEnemyColorDialog(x,y,Search.PROFUNDIDADE);
                        break;
                    }
                    case 2 :{
                        showEnemyColorDialog(x,y,Search.ORDENADA);
                        break;
                    }
                    case 3 :{
                        showEnemyColorDialog(x,y,Search.GULOSA);
                        break;
                    }
                    case 4 :{
                        showEnemyColorDialog(x,y,Search.AESTRELA);
                        break;
                    }
                }
            }
        });
        ab.setCancelable(true);
        ab.show();
    }

    private void showLoadDialog(){
        final List<Integer> ocupatadedSlots = GameInstanceManager.getOcupatedSlots(getContext());
        if(ocupatadedSlots.size()==0) {
            Toast.makeText(getContext(), "Não há mapas salvos", Toast.LENGTH_SHORT).show();
            return;
        }
            final String[] items = new String[ocupatadedSlots.size()+1];
        int i=0;
        for(int slot : ocupatadedSlots){
            items[i] = slot+"";
            i++;
        }
        items[ocupatadedSlots.size()] = "Cancelar";
        AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
        ab.setTitle("Selecione o slot para carregar:");
        ab.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface d, int choice) {
                if(choice< ocupatadedSlots.size()) {
                    if ((board = GameInstanceManager.loadBoard(Integer.parseInt(items[choice]), getContext())) == null) {
                        Toast.makeText(getContext(), "Nao foi possível carregar o slot " + items[choice], Toast.LENGTH_SHORT).show();
                        board = new Board();
                    } else
                        Toast.makeText(getContext(), "Mapa do slot " + items[choice] +" carregado", Toast.LENGTH_SHORT).show();
                }
                invalidate();
            }
        });
        ab.setCancelable(true);
        ab.show();
    }

    private void showDeleteDialog(){
        final List<Integer> ocupatadedSlots = GameInstanceManager.getOcupatedSlots(getContext());
        if(ocupatadedSlots.size()==0) {
            Toast.makeText(getContext(), "Não há slots para apagar", Toast.LENGTH_SHORT).show();
            return;
        }
        final String[] items = new String[ocupatadedSlots.size()+1];
        int i=0;
        for(int slot : ocupatadedSlots){
            items[i] = slot+"";
            i++;
        }
        items[ocupatadedSlots.size()] = "Cancelar";
        AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
        ab.setTitle("Selecione o slot para apagar:");
        ab.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface d, int choice) {
                if(choice< ocupatadedSlots.size()) {
                    GameInstanceManager.deleteSlot(Integer.parseInt(items[choice]), getContext());
                    Toast.makeText(getContext(), "O slot " + items[choice] + " foi apagado", Toast.LENGTH_SHORT).show();
                }
                invalidate();
            }
        });
        ab.setCancelable(true);
        ab.show();
    }

    private void showSaveDialog(){
        final String[] items = new String[GameInstanceManager.NUMBER_OF_SLOTS+1];
        for(int i = 0; i< GameInstanceManager.NUMBER_OF_SLOTS; i++){
            items[i] = i+" ("+ GameInstanceManager.getSlotStateDescription(i, getContext())+")";
        }
        items[GameInstanceManager.NUMBER_OF_SLOTS] = "Cancelar";
        AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
        ab.setTitle("Selecione o slot para salvar:");
        ab.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface d, int choice) {
                if(choice< GameInstanceManager.NUMBER_OF_SLOTS) {
                    if(GameInstanceManager.saveBoard(board, choice, getContext()))
                        Toast.makeText(getContext(), "Mapa salvo no slot "+choice, Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getContext(), "Nao foi possível salvar no slot "+choice, Toast.LENGTH_SHORT).show();
                }
                invalidate();
            }
        });
        ab.setCancelable(true);
        ab.show();
    }

    private void showChangeMapDialog(){
        final String items[] = {"Novo inimigo","Mudar jogador","Mudar objetivo","Editar barreiras", "Remover inimigos", "Cancelar" };
        AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
        ab.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface d, int choice) {

                switch (choice){

                    case 0 :{
                        gameState = Board.ADDING_NEW_ENEMY;
                        Toast.makeText(getContext(), "Clique na posição que deseja colocar o inimigo", Toast.LENGTH_SHORT).show();
                        //showNewEnemyDialog();
                        break;
                    }
                    case 1 :{
                        gameState = Board.CHANGING_PLAYER_POSITION;
                        Toast.makeText(getContext(), "Clique na posição que deseja colocar o jogador", Toast.LENGTH_SHORT).show();
                        //board.player.point.set();
                        break;
                    }
                    case 2 :{
                        gameState = Board.CHANGING_GOAL_POSITION;
                        Toast.makeText(getContext(), "Clique na posição que deseja colocar o objetivo", Toast.LENGTH_SHORT).show();
                        //board.changeGoal();
                        break;
                    }
                    case 3 :{
                        gameState = Board.BUILDING_MAP;
                        Toast.makeText(getContext(), "Clique no jogador para concluir", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 4: {
                        board.removeEnemis();
                        Toast.makeText(getContext(), "Inimigos removidos", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                invalidate();
            }
        });
        ab.setCancelable(true);
        ab.show();
    }

    private void showBuildingMapDialog(final int x, final int y) {
        final String items[] = {"Alterar mapa", "Salvar mapa", "Carregar mapa", "Apagar slot", "Limpar mapa", "Começar jogo", "Cancelar" };
        AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
        ab.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface d, int choice) {

                switch (choice){

                    case 0 :{
                        showChangeMapDialog();
                        break;
                    }
                    case 1:{
                        showSaveDialog();
                        break;
                    }
                    case 2 :{
                        showLoadDialog();
                        break;
                    }
                    case 3 :{
                        showDeleteDialog();
                        break;
                    }
                    case 4 :{
                        board = new Board();
                        break;
                    }
                    case 5 :{
                        gameState = Board.RESUME;
                        break;
                    }
                }
                invalidate();
            }
        });
        ab.setCancelable(true);
        ab.show();
    }
}
