package br.edu.ufjf;

import android.content.Context;
import android.graphics.Point;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.edu.ufjf.core.Board;
import br.edu.ufjf.core.Enemy;

/**
 * Created by Paulo VItor on 26/01/2016.
 */
public final class GameInstanceManager {

    public static final int NUMBER_OF_SLOTS = 10;
    private static final String[] FILENAME = {  "board0.map", "board1.map", "board2.map", "board3.map", "board4.map",
                                                "board5.map", "board6.map", "board7.map", "board8.map", "board9.map"};

    public static boolean saveBoard(Board board, int slot, Context context){
        Instance instance = new Instance(board);

        try{
            FileOutputStream fos = context.openFileOutput(FILENAME[slot], context.MODE_WORLD_READABLE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(instance);
            oos.close();
            fos.close();
            System.out.println("Estado salvo com sucesso!");
            return true;
        }
        catch(Exception e){
            System.err.println("Erro ao salvar arquivo: "+e);
        }
        return false;

    }

    public static Board loadBoard(int slot, Context context){
        try{
            FileInputStream fileIn = context.openFileInput(FILENAME[slot]);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Object instance = in.readObject();
            in.close();
            fileIn.close();
            if(instance!=null && instance instanceof Instance)
                return ((Instance) instance).recoverBoard();
        }catch(Exception e){
            System.err.println("Falha ao recuperar estado do jogo: "+e);
        }
        return null;
    }

    public static String getSlotStateDescription(int slot, Context context){
        return isSlotOcupated(slot, context) ? "Ocupado" : "Vazio";
    }

    public static boolean isSlotOcupated(int slot, Context context){
        try {
            FileInputStream fileIn = context.openFileInput(FILENAME[slot]);
            fileIn.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Integer> getOcupatedSlots(Context context){

        List<Integer> slotsOcupataded = new ArrayList<Integer>();

        for(int i = 0; i<NUMBER_OF_SLOTS; i++){
            if(isSlotOcupated(i, context)){
                slotsOcupataded.add(i);
            }
        }
        return slotsOcupataded;
    }

    public static void deleteSlot(int slot, Context context){
        context.deleteFile(FILENAME[slot]);
    }

    private static class Instance implements Serializable {
        public int grid_x, grid_y;
        public Integer grid[][];
        public int playerX, playerY;
        public EnemyInstance enemies[];
        public Instance(Board board) {
            this.grid = board.getGrid();
            this.playerX = board.player.point.x;
            this.playerY = board.player.point.y;
            enemies = new EnemyInstance[board.enemies.size()];
            int i = 0;
            for(Enemy e : board.enemies){
                enemies[i] = new EnemyInstance(e);
                i++;
            }
            this.grid_x = board.GRID_X_SIZE;
            this.grid_y=board.GRID_Y_SIZE;
        }
        public Board recoverBoard(){
            Board board = new Board(grid, new Point(playerX, playerY), grid_x, grid_y);
            for(EnemyInstance ei : enemies){
                board.addEnemy(ei.x, ei.y, ei.searchType, ei.color);
            }
            return board;
        }
    }

    private static class EnemyInstance implements Serializable{
        public int x, y;
        public int searchType;
        public int color;

        EnemyInstance(Enemy enemy){
            x = enemy.point.x;
            y = enemy.point.y;
            searchType = enemy.getSearchType();
            color = enemy.color;
        }
    }
}
