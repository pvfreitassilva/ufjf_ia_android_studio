package br.edu.ufjf;

import android.graphics.Point;

import java.util.List;

public class Enemy extends Entity{

    private Search search;

    public Enemy(Point point, Point objective, Graph graph, int searchType){
        super(point);
        search = new Search(point, objective, graph, searchType);
	}

	public void makeMove(){

        search.doSearch();

        //TODO fazer proximo passo
    }

    public List<Point> getPath(){
        return search.getPath();
    }

/*
    private void imprimeDados(){

        Log.d("Teste", "Inimigo: "+ x + ", "+y);
        Log.d("Teste", "Busca: "+ searchType);
        Log.d("Teste", "Estados criados: "+ estadosCriados);
        Log.d("Teste", "Estados expandidos: "+ estadosExpandidos);
        Log.d("Teste", "Tamanho do caminho: "+ tamanhoCaminho);


    }
*/
}
