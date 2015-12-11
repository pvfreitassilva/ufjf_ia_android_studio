package br.edu.granbery.gomap;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class Splash extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		Thread timer = new Thread(){
			public void run(){
				try {
					sleep(3000);
				} catch (InterruptedException e){
					e.printStackTrace();
				} finally {
					File gameSave = new File(getFilesDir()+File.pathSeparator+"gomap_save.ser");
					if(gameSave.exists()){
						runOnUiThread(new Runnable() {
						    public void run() {
						    	returnGameDialog();
						    }
						  });
					}
					else{
						Intent i = new Intent("br.edu.granbery.gomap.MENU");
						startActivity(i);
					}
				}
			}
		};
		timer.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
	
	public void returnGameDialog() {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setMessage("Deseja continuar último jogo?");
		ab.setCancelable(false);
		ab.setPositiveButton("Sim", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface arg0, int arg1) {
				Intent i = new Intent("br.edu.granbery.gomap.GOMAP");
				i.putExtra("GameContinue", true);
				startActivity(i);
	         }
		});
		
		ab.setNegativeButton("Não", new DialogInterface.OnClickListener() {			
		    public void onClick(DialogInterface dialog, int which) {
		    	Intent i = new Intent("br.edu.granbery.gomap.MENU");
				startActivity(i);
			}
		});
			
		ab.show();
	}

}
