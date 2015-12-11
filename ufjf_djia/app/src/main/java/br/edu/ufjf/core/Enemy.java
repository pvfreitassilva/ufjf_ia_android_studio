package br.edu.ufjf.core;

import android.graphics.Point;

import java.util.List;

import br.edu.ufjf.ai.Search;

public class Enemy extends Entity implements Comparable{

    private Search search;

    public Enemy(Point point, Point objective, Graph graph, int searchType){
        super(point);
        search = new Search(point, objective, graph, searchType);
	}

	public void makeMove(){

        search.doSearch();
        Point nextPoint = search.nextMove();
        point.x = nextPoint.x;
        point.y = nextPoint.y;

        //TODO fazer proximo passo
    }

    public List<Point> getPath(){
        return search.getPath();
    }

    @Override
    public int compareTo(Object another) {
        Enemy e = (Enemy) another;

        if(e.point.equals( this.point ))
            return 0;
        return -1;
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
