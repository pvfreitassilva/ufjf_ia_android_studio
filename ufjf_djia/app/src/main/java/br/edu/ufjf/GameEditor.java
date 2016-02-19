package br.edu.ufjf;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class GameEditor extends Activity {

    GameEditorView gve;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //GameView gameView = new GameView(this);

        setContentView(R.layout.editmap);

        final Button zoomin = (Button) findViewById(R.id.btnzoomin);
        final Button zoomout = (Button) findViewById(R.id.btnzoomout);
        final Button addAlt = (Button) findViewById(R.id.btnAddAlt);
        final Button finish = (Button) findViewById(R.id.btnFinish);
        final Button editBarriers = (Button) findViewById(R.id.btnEditBarriers);
        final GameEditorView gve = (GameEditorView) findViewById(R.id.cstEditorView);
        this.gve = gve;

        zoomin.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        gve.zoomIn();
                    }
                });

        zoomout.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        gve.zoomOut();
                    }
                });

        editBarriers.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        if(gve.getEditState()==GameEditorView.TRANSLATING) {
                            editBarriers.setText("Concluir barreiras");
                            gve.setEditState(GameEditorView.EDITING_BARRIERS);
                            finish.setEnabled(false);
                            addAlt.setEnabled(false);
                        }
                        else {
                            editBarriers.setText("Editar barreiras");
                            gve.setEditState(GameEditorView.TRANSLATING);
                            finish.setEnabled(true);
                            addAlt.setEnabled(true);
                            Toast.makeText(getApplicationContext(), "Edição do mapa concluída", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        addAlt.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        showAddAltDialog();
                    }
                });
    }

    private void showAddAltDialog() {
        final String items[] = {"Novo inimigo", "Mudar jogador", "Mudar objetivo", "Remover inimigos", "Cancelar"};
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface d, int choice) {

                switch (choice) {

                    case 0: {
                        gve.setEditState(GameEditorView.ADDING_NEW_ENEMY);
                        Toast.makeText(getApplicationContext(), "Clique na posição que deseja colocar o inimigo", Toast.LENGTH_SHORT).show();
                        //showNewEnemyDialog();
                        break;
                    }
                    case 1: {
                        gve.setEditState(GameEditorView.CHANGING_PLAYER);
                        Toast.makeText(getApplicationContext(), "Clique na posição que deseja colocar o jogador", Toast.LENGTH_SHORT).show();
                        //board.player.point.set();
                        break;
                    }
                    case 2: {
                        gve.setEditState(GameEditorView.CHANGING_GOAL);
                        Toast.makeText(getApplicationContext(), "Clique na posição que deseja colocar o objetivo", Toast.LENGTH_SHORT).show();
                        //board.changeGoal();
                        break;
                    }
                    case 3: {
                        gve.removeEnemies();
                        Toast.makeText(getApplicationContext(), "Inimigos removidos", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                //invalidate();
            }
        });
        ab.setCancelable(true);
        ab.show();
    }
}
