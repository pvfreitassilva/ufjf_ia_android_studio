package br.edu.granbery.gomap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

public class GoMap extends Activity {
	
	Game game;
	String FILENAME = "gomap_save.ser";
	String FILEPATH;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		if(!getIntent().getExtras().getBoolean("GameContinue")){		
			int index = getIntent().getExtras().getInt("Dificuldade");
			int mode = getIntent().getExtras().getInt("Mode");
			game = new Game(this, index, mode);
		}
		else{
			
			try{
			
				GameInstance gi = returnGameInstance();
				if(gi==null){	
					
					//TODO REMOVE
					Log.d("Testando","Não foi possível restaurar GAME. Criada uma nova instância.");
				}
				else{
					game = new Game(this, gi);
					
					//TODO REMOVE
					Log.d("Testando","Instância de GAME restaurada.");
				}
			}catch(Exception e){
				Toast.makeText(this, "Não foi possível restaurar o estado do jogo",
						Toast.LENGTH_SHORT).show();
				Intent i = new Intent("br.edu.granbery.gomap.MENU");
				startActivity(i);
			}
		}
		
		FILEPATH = getFilesDir()+File.pathSeparator+"gomap_save.ser";
		
		setContentView(game);
	}
	
	@Override
	protected void onResume() {
		super.onResume();		
		setContentView(game);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveGameInstance();
	}
	
	public GameInstance returnGameInstance(){
		try{
			FileInputStream fileIn = openFileInput(FILENAME);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			Object gi = in.readObject();
			in.close();
			fileIn.close();
			if(gi!=null && gi instanceof GameInstance)
				return (GameInstance) gi;
		}catch(Exception e){
			Log.d("Testando",e.toString());
			Log.d("Testando","Falha ao restaurar");
			return null;
		}
		
		return null;
	}
	
	public void saveGameInstance(){
		GameInstance gi = game.getGameInstance();
		try{
			FileOutputStream fos = openFileOutput(FILENAME, MODE_WORLD_READABLE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(gi);
			oos.close();
			fos.close();
		}
		catch(Exception e){
			Log.d("Testando",e.toString());
			Log.d("Testando","Falha ao serializar");
		}
	}
}
