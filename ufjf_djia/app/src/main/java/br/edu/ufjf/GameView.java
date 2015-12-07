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

public class GameView extends View{

	private Board board;
	private Paint paint;
	private int gameState;

	public GameView(Context context) {
		super(context);
		board = new Board();
		paint = new Paint();
		gameState = Board.RESUME;
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
				canvas.drawLine(i*squareSize, j*squareSize, i*squareSize, j*squareSize + squareSize, paint);
			}
		}
		
	}
	
	@SuppressLint("ClickableViewAccessibility") @Override
	public boolean onTouchEvent(MotionEvent event){

		if(gameState==Board.RESUME) {

            try{
                int squareSize = getHeight() / Board.GRID_Y_SIZE;
                int eventX = (int) event.getX()/squareSize;
                int eventY = (int) event.getY()/squareSize;

                if(event.getAction()==MotionEvent.ACTION_DOWN || event.getAction()==MotionEvent.ACTION_MOVE){

                    board.makePlayerMove(eventX, eventY, board.graph);

                }

                gameState = board.getGameState();

                invalidate();
            }
            catch(Exception e){
                Log.d("Exception", e.toString());
            }

            return true;

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
					gameState = Board.RESUME;
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
					gameState = Board.RESUME;
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

}
