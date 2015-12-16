//TODO tempo de execução
//TODO

package br.edu.ufjf;

import android.annotation.SuppressLint;
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

import br.edu.ufjf.ai.Search;
import br.edu.ufjf.core.Board;
import br.edu.ufjf.core.Enemy;

public class GameView extends View{

	private Board board;
	private Paint paint;
	private int gameState;

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
		int squareSize = getHeight() / Board.GRID_Y_SIZE;
		paint.setColor(Color.WHITE);
		drawBoard(canvas);
		drawLines(canvas);		
		
		paint.setColor(Color.GREEN);
		canvas.drawCircle(board.player.point.x * squareSize + squareSize/2, board.player.point.y*squareSize + squareSize/2, squareSize/2, paint);

		for(Enemy enemy : board.enemies){

			paint.setColor(Color.MAGENTA);
			for(Point p : enemy.getPath()) {
				canvas.drawCircle(p.x * squareSize + squareSize / 2 - squareSize / 4, p.y * squareSize + squareSize / 2 + squareSize / 4, squareSize / 4, paint);
			}

            if(enemy.getPath()==null || enemy.getPath().isEmpty()) paint.setColor(Color.BLACK);
			else paint.setColor(Color.RED);

			canvas.drawCircle(enemy.point.x * squareSize + squareSize/2, enemy.point.y*squareSize + squareSize/2, squareSize/2, paint);

		}
		
		if(gameState == Board.GAMEOVER){
			showGameOverDialog();
		}
		else if(gameState == Board.PLAYERWIN){
			showPlayerWinDialog();
		}
		
	}
	
	private void drawBoard(Canvas canvas){
		
		int squareSize = getHeight() / Board.GRID_Y_SIZE;
		
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
				canvas.drawRect(i * squareSize, j * squareSize, i * squareSize + squareSize, j * squareSize + squareSize, paint);
			}
		}
	}
	
	private void drawLines(Canvas canvas){
		
		int squareSize = getHeight() / Board.GRID_Y_SIZE;
		
		paint.setColor(Color.BLACK);
		for(int i = 0; i < Board.GRID_X_SIZE; i++){
			for(int j = 0; j < Board.GRID_Y_SIZE; j++){ 
				canvas.drawLine(i*squareSize, j*squareSize, i*squareSize + squareSize, j*squareSize, paint);
				canvas.drawLine(i * squareSize, j * squareSize, i * squareSize, j * squareSize + squareSize, paint);
			}
		}
		
	}
	
	@SuppressLint("ClickableViewAccessibility") @Override
	public boolean onTouchEvent(MotionEvent event){

        int squareSize = getHeight() / Board.GRID_Y_SIZE;
        int eventX = (int) event.getX()/squareSize;
        int eventY = (int) event.getY()/squareSize;

        switch (gameState){
            case Board.BUILDING_BOARD : {
                if(board.isValidPosition(eventX, eventY))
                    showBuildingMapDialog(eventX, eventY);
                //break;
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
	
	public void showGameOverDialog() {
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
	
	public void showPlayerWinDialog() {
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

    public void showNewEnemyDialog(final int x, final int y) {
        final String items[] = { "Largura", "Profundidade", "Gulosa", "Ordenada", "A*", "Cancelar" };
        AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
        ab.setTitle("Selecione o tipo de busca");
        ab.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface d, int choice) {

                switch (choice){

                    case 0 :{
                        board.addEnemy(x, y, Search.LARGURA);
                        break;
                    }
                    case 1 :{
                        board.addEnemy(x, y, Search.PROFUNDIDADE);
                        break;
                    }
                    case 2 :{
                        board.addEnemy(x, y, Search.GULOSA);
                        break;
                    }
                    case 3 :{
                        board.addEnemy(x, y, Search.ORDENADA);
                        break;
                    }
                    case 4 :{
                        board.addEnemy(x, y, Search.AESTRELA);
                        break;
                    }
                }

                invalidate();
            }
        });
        ab.setCancelable(true);
        ab.show();
    }

    public void showBuildingMapDialog(final int x, final int y) {
        final String items[] = { "Novo inimigo", "Jogador", "Objetivo", "Alterar mapa", "Começar jogo", "Cancelar" };
        AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
        ab.setTitle("Selecione:");
        ab.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface d, int choice) {

                switch (choice){

                    case 0 :{
                        showNewEnemyDialog(x,y);
                        break;
                    }
                    case 1 :{
                        board.player.point.set(x,y);
                        break;
                    }
                    case 2 :{
                        board.changeGoal(x,y);
                        break;
                    }
                    case 3 :{
                        gameState = Board.BUILDING_MAP;
                        Toast.makeText(getContext(), "Clique no jogador para concluir", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 4 :{
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
