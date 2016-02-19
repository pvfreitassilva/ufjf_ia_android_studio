//TODO tempo de execução

package br.edu.ufjf;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import br.edu.ufjf.ai.Search;
import br.edu.ufjf.core.Board;
import br.edu.ufjf.core.Enemy;

public class GameEditorView extends View{

	private Board board;
	private Paint paint;
	private int editState;
    private int mapTranslationX;
    private int mapTranslationY;
    private int panTranslationX;
    private int panTranslationY;
    private int squareSize;
    private float zoom = 1;

    public static final int TRANSLATING = 0;
    public static final int EDITING_BARRIERS = 1;
    public static final int ADDING_NEW_ENEMY = 2;
    public static final int CHANGING_PLAYER = 3;
    public static final int CHANGING_GOAL = 4;


    private int lastX, lastY;
    private int lastPanX, lastPanY;

	public GameEditorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		board = GameInstanceManager.board;
        if(board==null){
            board = new Board();
            System.out.println("Board nulo!");
        }
		paint = new Paint();
        editState = TRANSLATING;
        lastX=lastY=-1;
        panTranslationX=panTranslationY=0;
        lastPanX=lastPanY=0;
	}
	
	@Override
	public void onDraw(Canvas canvas){
        //todo trocar para função de desenho única
		super.onDraw(canvas);
        squareSize = Board.GRID_Y_SIZE > Board.GRID_X_SIZE ? getHeight() / Board.GRID_Y_SIZE : getWidth() / Board.GRID_X_SIZE ;
        squareSize*=zoom;
        mapTranslationX = ( getWidth()  - squareSize * Board.GRID_X_SIZE)/2 + panTranslationX;
        mapTranslationY = ( getHeight() - squareSize * Board.GRID_Y_SIZE)/2 + panTranslationY;
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
                canvas.drawCircle(  p.x * squareSize + squareSize / 2 + enemiesTranslations[pathTranslation][0],
                                    p.y * squareSize + squareSize / 2 + enemiesTranslations[pathTranslation][1],
                                    squareSize/6, paint);
			}
			paint.setColor(enemy.color);
			canvas.drawCircle(  enemy.point.x * squareSize + squareSize/2 + enemiesTranslations[pathTranslation][0],
                                enemy.point.y * squareSize + squareSize/2 + enemiesTranslations[pathTranslation][1],
                                squareSize/4, paint);
            pathTranslation = (pathTranslation + 1)%5;
		}
	}
	
	private void drawBoard(Canvas canvas){
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

        switch (editState){
            case TRANSLATING : {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN : {
                        lastPanX = (int) event.getX();
                        lastPanY = (int) event.getY();
                        return true;
                    }
                    case MotionEvent.ACTION_MOVE : {
                        panTranslationX+= (event.getX() - lastPanX);
                        panTranslationY+= (event.getY() - lastPanY);
                        doPanCorrection();
                        lastPanX = (int) event.getX();
                        lastPanY = (int) event.getY();
                        invalidate();
                        return true;
                    }
                    case MotionEvent.ACTION_UP : {
                        lastPanX = 0;
                        lastPanY = 0;
                        break;
                    }
                }
                return false;
            }
            case EDITING_BARRIERS : {
                if(lastX!=eventX || lastY!=eventY) {
                    board.changeGrid(eventX, eventY);
                    invalidate();
                    lastX = eventX;
                    lastY = eventY;
                }
                if(event.getAction() == MotionEvent.ACTION_UP)
                    lastX=lastY=-1;
                return true;
            }
            case ADDING_NEW_ENEMY : {
                if(board.isValidPosition(eventX, eventY))
                    showNewEnemyDialog(eventX, eventY);
                else
                    Toast.makeText(getContext(), "Posição inválida! Ação cancelada", Toast.LENGTH_SHORT).show();
                editState = TRANSLATING;
                return false;
            }
            case CHANGING_PLAYER : {
                if(board.isValidPosition(eventX, eventY)) {
                    board.player.point.set(eventX, eventY);
                    Toast.makeText(getContext(), "Posição do jogador alterada ("+eventX +", "+ eventY+")", Toast.LENGTH_SHORT).show();
                    invalidate();
                }
                else
                    Toast.makeText(getContext(), "Posição inválida! Ação cancelada", Toast.LENGTH_SHORT).show();
                editState = TRANSLATING;
                return false;
            }
            case CHANGING_GOAL: {
                if(board.isValidPosition(eventX, eventY)) {
                    board.changeGoal(eventX, eventY);
                    Toast.makeText(getContext(), "Posição do objetivo alterada ("+eventX +", "+ eventY+")", Toast.LENGTH_SHORT).show();
                    invalidate();
                }
                else
                    Toast.makeText(getContext(), "Posição inválida! Ação cancelada", Toast.LENGTH_SHORT).show();
                editState = TRANSLATING;
                return false;
            }
        }
		return false;
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
                        showEnemyColorDialog(x, y, Search.LARGURA);
                        break;
                    }
                    case 1 :{
                        showEnemyColorDialog(x, y, Search.PROFUNDIDADE);
                        break;
                    }
                    case 2 :{
                        showEnemyColorDialog(x, y, Search.ORDENADA);
                        break;
                    }
                    case 3 :{
                        showEnemyColorDialog(x, y, Search.GULOSA);
                        break;
                    }
                    case 4 :{
                        showEnemyColorDialog(x, y, Search.AESTRELA);
                        break;
                    }
                }
            }
        });
        ab.setCancelable(true);
        ab.show();
    }

    public void zoomIn() {
        if(zoom<10) {
            zoom += 0.5;
            doPanCorrection();
            invalidate();
        }
    }

    public void zoomOut() {
        if(zoom>1) {
            zoom -= 0.5;
            doPanCorrection();
            invalidate();
        }
    }

    public void setEditState(int editState) {
        this.editState = editState;
        board.graph.rebuildGraph();
        lastX=lastY=-1;
        invalidate();
    }

    public void removeEnemies() {
        board.removeEnemis();
        invalidate();
    }

    public int getEditState() {
        return editState;
    }

    private void doPanCorrection(){

        squareSize = Board.GRID_Y_SIZE > Board.GRID_X_SIZE ? getHeight() / Board.GRID_Y_SIZE : getWidth() / Board.GRID_X_SIZE ;
        squareSize*=zoom;

        int maxPanXSize = (board.GRID_X_SIZE*squareSize - getWidth())/2 + 10;
        if(maxPanXSize<=0)
            panTranslationX=0;
        else if(Math.abs(panTranslationX) > maxPanXSize)
            panTranslationX = panTranslationX > 0 ? maxPanXSize : - maxPanXSize;

        int maxPanYSize = (board.GRID_Y_SIZE*squareSize - getHeight())/2 + 10;
        if(maxPanYSize<=0)
            panTranslationY=0;
        else if(Math.abs(panTranslationY) > maxPanYSize)
            panTranslationY = panTranslationY > 0 ? maxPanYSize: - maxPanYSize;

    }
}
