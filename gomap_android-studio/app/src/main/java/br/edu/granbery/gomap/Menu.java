package br.edu.granbery.gomap;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class Menu extends Activity {
	
	protected void onCreate(Bundle savedInstanceState){		
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		
		Button btnStart = (Button) findViewById(R.id.btnStart);
		
		btnStart.setOnClickListener ( 
				new View.OnClickListener() {
					public void onClick(View v) {
					RadioGroup rgDificulty = (RadioGroup) findViewById(R.id.radioGroupDificulty);
					int idDificulty = rgDificulty.getCheckedRadioButtonId();
					View radioButtonDificulty = rgDificulty.findViewById(idDificulty);
					int idxDificulty = rgDificulty.indexOfChild(radioButtonDificulty);
					
					RadioGroup rgOpponent = (RadioGroup) findViewById(R.id.radioGroupOpponent);
					int idOpponent = rgOpponent.getCheckedRadioButtonId();
					View radioButtonOpponent = rgOpponent.findViewById(idOpponent);
					int idxOpponent = rgOpponent.indexOfChild(radioButtonOpponent);
	
					Intent intent = new Intent(getApplicationContext(), GoMap.class);
					intent.putExtra("GameContinue", false);
					intent.putExtra("Dificuldade", idxDificulty);
					intent.putExtra("Mode", idxOpponent);
					startActivity(intent);
			}
		});				
	}
}
