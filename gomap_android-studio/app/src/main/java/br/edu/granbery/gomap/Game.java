package br.edu.granbery.gomap;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import br.edu.granbery.ai.AlphaBeta;
import br.edu.granbery.core.Board;
import br.edu.granbery.core.Piece;

public class Game extends View {

	private Board board;
	private final int mode;
	private final int boardSize;
	private Board boardTemp;
	private boolean dragging;
	private boolean clicking;
	private boolean blockTouch;
	private boolean invalidating;
	private Piece lastPiece;
	private byte animationProgress;
	private Handler handler;
	private Thread animation;
	private long lastToast;

	//MediaPlayer mpPresetMove;
	//MediaPlayer mpSetMove;
	//MediaPlayer mpInvalideMove;

	public Game(Context context, int dificuldade, int mode) {
		super(context);

		handler= new Handler();
		dragging=false;
		clicking=false;
		lastPiece=null;
		blockTouch=false;
		lastToast=System.currentTimeMillis();
		animationProgress=100;
		boardTemp = null;
		invalidating=false;
		animation = new Thread(animating);

		//mpPresetMove = MediaPlayer.create(getContext(), R.raw.preset_move_sound);
		//mpSetMove = MediaPlayer.create(getContext(), R.raw.set_move_sound);
		//mpInvalideMove = MediaPlayer.create(getContext(), R.raw.invalide_move_sound);

		//bgTile = (BitmapDrawable) getResources().getDrawable(R.drawable.navy_blue);

		switch (dificuldade) {
		case 0:
			boardSize = Board.SMALL_BOARD;
			break;
		case 1:
			boardSize = Board.MEDIUM_BOARD;
			break;
		case 2:
			boardSize = Board.BIG_BOARD;
			break;
		case 3:
			boardSize = Board.EXTREME_BOARD;
			break;
		default:
			boardSize = 0;
		}

		setKeepScreenOn(true);

		board = new Board(boardSize);
		this.mode = mode;
		if (mode == 1)
			showFirstPlayerDialog();
	}

	public Game(Context context, GameInstance gi){
		super(context);

		handler= new Handler();
		dragging=false;
		clicking=false;
		lastPiece=null;
		blockTouch=false;
		lastToast=System.currentTimeMillis();
		animationProgress=100;
		boardTemp = null;
		invalidating=false;
		animation = new Thread(animating);

		board = gi.board;
		boardSize = gi.boardSize;
		mode = gi.mode;


		if(board!=null){
			board.restoreBoard();
		}

		setKeepScreenOn(true);		
	}

	public GameInstance getGameInstance(){
		board.prepareSerialization();
		return new GameInstance(board, mode, boardSize);
	}

	Runnable animating = new Runnable(){
		public void run(){
			byte sleeps=0;
			animationProgress=0;
			handler.post(runHandler);
			while(animationProgress<100){
				if(sleeps<20)
					sleeps++;
				else{
					animationProgress+=5;
					if(animationProgress>=100)
						boardTemp=null;
					handler.post(runHandler);
				}
				try{
					Thread.sleep(50);
				}catch (InterruptedException e){
					e.printStackTrace();
				}
			}
			while(invalidating);
			boardTemp=null;
		}
	};

	Runnable runHandler = new Runnable()
	{
		public void run()
		{
			invalidate();
		}
	};

	@Override
	protected void onDraw(Canvas canvas) {
		invalidating=true;
		Board boardAux = null;
		if (boardTemp != null && animationProgress>=100) {
			boardAux = board;
			board = boardTemp;
			boardTemp = boardAux;
		}
		super.onDraw(canvas);
		paintBoard(canvas);
		paintPieces(canvas);
		paintScores(canvas);
		paintLines(canvas);
		if (board.isGameOver()&&boardTemp==null) {
			showGameOverDialog();
		}
		if (boardAux != null) {
			boardAux = board;
			board = boardTemp;
			boardTemp = boardAux;
		}
		invalidating=false;
	}

	private void paintPieces(Canvas canvas){
		int squareSize = getSquareSize();
		for (int i = 0; i < Board.GRID_SIZE; i++) {
			for (int j = 0; j < Board.GRID_SIZE; j++) {
				if (board.grid[i][j] != -1) {
					Rect rt = new Rect(i * squareSize, j * squareSize,
							(i * squareSize) + squareSize, (j * squareSize)
							+ squareSize);
					Paint p = getPlayerPaint(board.grid[i][j]);
					if(boardTemp!=null)
						if(boardTemp.grid[i][j]!=board.grid[i][j]){
							if(animationProgress<=95 && animation.isAlive())
								p.setAlpha(140 - animationProgress);
							else
								p.setAlpha(100);
						}
					canvas.drawRect(rt,p);
				}
			}
		}
	}

	private void paintLines(Canvas canvas){
		Board boardAux;
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setAlpha(100);
		int grid[][] = board.getGameGraph().getControlGrid();
		int squareSize = getSquareSize();
		for (int i = 0; i < Board.GRID_SIZE; i++) {
			for (int j = 0; j < Board.GRID_SIZE; j++) {
				if (j + 1 < Board.GRID_SIZE && grid[j + 1][i] != grid[j][i]) {
					canvas.drawLine(squareSize * (j + 1), (squareSize * i), squareSize * (j + 1), squareSize + (squareSize * i), paint);
					if (boardTemp != null && (boardTemp.grid[j][i] != board.grid[j][i] || boardTemp.grid[j+1][i] != board.grid[j+1][i])) {
						if(animationProgress<=95){
							paint.setColor(Color.GREEN);
							paint.setAlpha(190 - animationProgress*2);
						}
						else
							paint.setColor(Color.YELLOW);
						canvas.drawLine(squareSize * (j + 1), (squareSize * i), squareSize * (j + 1), squareSize + (squareSize * i), paint);
						paint.setColor(Color.BLACK);
						paint.setAlpha(100);
					}
				}
				if (i + 1 < Board.GRID_SIZE && grid[j][i + 1] != grid[j][i]) {
					canvas.drawLine((squareSize * j), squareSize * (i + 1), squareSize + (squareSize * j), squareSize * (i + 1), paint);
					if (boardTemp != null && (boardTemp.grid[j][i] != board.grid[j][i] || boardTemp.grid[j][i+1] != board.grid[j][i+1])) {
						paint.setColor(Color.YELLOW);
						if(animationProgress<=95){
							paint.setColor(Color.GREEN);
							paint.setAlpha(190 - animationProgress*2);
						}
						else
							paint.setColor(Color.YELLOW);
						canvas.drawLine((squareSize * j), squareSize * (i + 1), squareSize + (squareSize * j), squareSize * (i + 1), paint);
						paint.setColor(Color.BLACK);
						paint.setAlpha(100);
					}
				}
			}
		}

		paint.setColor(Color.YELLOW);

		if(boardTemp!=null)
			boardAux=boardTemp;
		else
			boardAux=board;

		if(boardAux.getPlayer()==0 && animationProgress>=100){
			canvas.drawLine(10, getWidth() + 10, 70, getWidth() + 10, paint);
			canvas.drawLine(10, getWidth() + 70, 70, getWidth() + 70, paint);
			canvas.drawLine(10, getWidth() + 10, 10, getWidth() + 70, paint);
			canvas.drawLine(70, getWidth() + 10, 70, getWidth() + 70, paint);
		}
		else{
			canvas.drawLine(80, getWidth() + 10, 140, getWidth() + 10, paint);
			canvas.drawLine(80, getWidth() + 70, 140, getWidth() + 70, paint);
			canvas.drawLine(80, getWidth() + 10, 80, getWidth() + 70, paint);
			canvas.drawLine(140, getWidth() + 10, 140, getWidth() + 70, paint);
		}
	}

	private void paintScores(Canvas canvas){
		Paint paint = new Paint();

		if(boardTemp!=null && animationProgress>=100){
			paint.setColor(Color.BLUE);
			if(boardTemp.getPlayer()==1)
				paint.setAlpha(60);
			else
				paint.setAlpha(150);
			canvas.drawRect(10, getWidth() + 10, 70, getWidth() + 70, paint);

			paint.setColor(Color.RED);
			if(boardTemp.getPlayer()==0)
				paint.setAlpha(60);
			else
				paint.setAlpha(150);
			canvas.drawRect(80, getWidth() + 10, 140, getWidth() + 70,paint);
		}else{
			paint.setColor(Color.BLUE);
			if(board.getPlayer()==1)
				paint.setAlpha(60);
			else
				paint.setAlpha(150);
			canvas.drawRect(10, getWidth() + 10, 70, getWidth() + 70, paint);

			paint.setColor(Color.RED);
			if(board.getPlayer()==0)
				paint.setAlpha(60);
			else
				paint.setAlpha(150);
			canvas.drawRect(80, getWidth() + 10, 140, getWidth() + 70,paint);
		}

		paint.setColor(Color.WHITE);
		paint.setTextSize(40);
		if(board.getScore()[0]<10)
			canvas.drawText(""+board.getScore()[0], 28, getWidth() + 55,paint);
		else if(board.getScore()[0]<100)
			canvas.drawText(""+board.getScore()[0], 18, getWidth() + 55,paint);
		else{
			paint.setTextSize(30);
			canvas.drawText(""+board.getScore()[0], 13, getWidth() + 52,paint);
		}

		paint.setTextSize(40);
		if(board.getScore()[1]<10)
			canvas.drawText("" + board.getScore()[1], 98, getWidth() + 55, paint);
		else if(board.getScore()[1]<100)
			canvas.drawText("" + board.getScore()[1], 88, getWidth() + 55, paint);
		else{
			paint.setTextSize(30);
			canvas.drawText("" + board.getScore()[1], 83, getWidth() + 52, paint);
		}

		paint.setTextSize(30);
		canvas.drawText("Jogada: " + board.getMove(), 5, getWidth() + 100,
				paint);
	}

	private void paintBoard(Canvas canvas) {
		Paint paint = new Paint();

		setBackgroundResource(R.drawable.navy_background);

		paint.setColor(Color.WHITE);
		paint.setAlpha(70);
		canvas.drawRect(0, 0, getWidth(), getWidth(), paint);
		canvas.drawRect(10, getWidth() + 10, 70, getWidth() + 70, paint);
		canvas.drawRect(80, getWidth() + 10, 140, getWidth() + 70, paint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!blockTouch){
			blockTouch=true;
			if(animationProgress<100||animation.isAlive()){
				animationProgress=100;
				while(animation.isAlive());
				invalidate();
			}

			if (!board.isGameOver()||boardTemp!=null) {
				int x = (int) Math.floor((double) ((int) event.getX() / getSquareSize()));
				int y = (int) Math.floor((double) ((int) event.getY() / getSquareSize()));
				if(x < Board.GRID_SIZE && y < Board.GRID_SIZE){
					Piece piece = board.getGameGraph().getPiece(x, y);
					if(piece!=null){
						if(!(piece.equals(lastPiece))){
							if(event.getAction()==MotionEvent.ACTION_MOVE){
								dragging=true;
								clicking=true;							
								preSetPlayerMove(piece,x,y);
								invalidate();
								dragging=false;
							}
							else if(event.getAction()==MotionEvent.ACTION_DOWN){
								clicking=true;
								preSetPlayerMove(piece,x,y);
								invalidate();			
							}
						}else if(event.getAction()==MotionEvent.ACTION_UP){
							if(!clicking){								
								setPlayerMove(piece,x,y);
								invalidate();
							}
							clicking=false;
						}
					}
				}
			} else {
				if(event.getAction()==MotionEvent.ACTION_DOWN)
					showGameOverDialog();
			}

			blockTouch=false;
		}
		return true;
	}

	public boolean onInterceptTouchEvent(MotionEvent evt){  
		return true;  
	}

	public void showFirstPlayerDialog() {
		final String items[] = { "Jogador", "Android" };
		AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
		ab.setTitle("Selecione quem começa o jogo:");
		ab.setCancelable(false);

		ab.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				board.setPlayer(1);
				invalidate();
			}
		});

		ab.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface d, int choice) {
				if (choice == 1) {
					setAndroidMove();
				} else {
					board.setPlayer(1);
					invalidate();
				}
			}
		});
		ab.show();
	}

	public void showGameOverDialog() {
		final String items[] = { "Nova Partida", "Retornar ao menu", "Cancelar" };
		AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
		ab.setTitle(board.getWinner());
		ab.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface d, int choice) {
				if (choice == 0) {
					board = new Board(boardSize);
					invalidate();
					if (mode == 1)
						showFirstPlayerDialog();
				} else if (choice == 1) {
					Intent i = new Intent(getContext(), Menu.class);
					getContext().startActivity(i);
				}
			}
		});
		ab.show();
	}

	private int getSquareSize() {
		return getWidth() / Board.GRID_SIZE;
	}

	private Paint getPlayerPaint(int player) {
		Paint p = new Paint();
		if (player == 0)
			p.setColor(Color.BLUE);
		else if (player == 1)
			p.setColor(Color.RED);
		p.setStyle(Style.FILL);
		p.setAlpha(50);
		return p;

	}

	private void setAndroidMove() {
		AndroidMove androidMove = new AndroidMove(2, getContext());
		androidMove.execute("");
	}

	private void preSetPlayerMove(Piece piece, int x, int y){

		if (board.grid[x][y] == -1) {
			lastPiece=piece;
			boardTemp = board.clone();
			boardTemp.makeMove(piece);

		} else {
			boardTemp=board;
			lastPiece=piece;
			if(!dragging && lastToast+2500<System.currentTimeMillis()){
				lastToast=System.currentTimeMillis();
				Toast.makeText(getContext(), "Jogada inválida!",
						Toast.LENGTH_SHORT).show();

				//mpInvalideMove.seekTo(0);
				//mpInvalideMove.start();
			}
			clicking=false;
		}
	}

	private void setPlayerMove(Piece piece, int x, int y){

		if (board.grid[x][y] == -1) {
			lastPiece=null;
			if(boardTemp!=null){
				board = boardTemp;
				boardTemp = null;
			}
			else
				board.makeMove(piece);
			invalidate();

			Vibrator vib = (Vibrator) getContext().getSystemService(
					getContext().VIBRATOR_SERVICE);
			vib.vibrate(50);

			//mpSetMove.seekTo(0);
			//mpSetMove.start();

			if (mode == 1)
				setAndroidMove();

		} else {
			if(!dragging && lastToast+3000<System.currentTimeMillis()){
				lastToast=System.currentTimeMillis();
				Toast.makeText(getContext(), "Jogada inválida!",
						Toast.LENGTH_SHORT).show();

				//mpInvalideMove.seekTo(0);
				//mpInvalideMove.start();
			}
			clicking=false;
		}
	}

	private class AndroidMove extends AsyncTask<String, Integer, String> {

		private AlphaBeta alphaBeta;
		private ProgressDialog dialog;
		private Piece bestMove = null;

		public AndroidMove(int depth, Context context) {
			alphaBeta = new AlphaBeta(depth);
			this.dialog = new ProgressDialog(context);
			this.dialog.setCanceledOnTouchOutside(false);
			this.dialog
			.setOnCancelListener(new DialogInterface.OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					cancel(true);
					Intent i = new Intent(getContext(), Menu.class);
					getContext().startActivity(i);
				}
			});

		}

		@Override
		protected String doInBackground(String... params) {
			bestMove = alphaBeta.getBestMove(board);
			return "Success";
		}

		@Override
		protected void onPostExecute(String result) {

			if (bestMove != null) {

				boardTemp=board.clone();				
				board.makeMove(bestMove);

				Vibrator vib = (Vibrator) getContext().getSystemService(
						getContext().VIBRATOR_SERVICE);
				vib.vibrate(50);

				animation = new Thread(animating);
				animation.start();

				//mpPresetMove.seekTo(0);
				//mpPresetMove.start();
			}
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
		}

		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("Calculando jogada...");
			this.dialog.show();
		}
	}

}
