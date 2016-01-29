package br.edu.ufjf.core;

import android.graphics.Point;

import java.util.List;

import br.edu.ufjf.ai.Search;

public class Enemy extends Entity implements Comparable{

    private Search search;

    public int color;

    public Enemy(Point point, Point objective, Graph graph, int searchType, int color){
        super(point);
        this.color=color;
        search = new Search(point, objective, graph, searchType);
	}

	public void makeMove(){

        search.doSearch();
        Point nextPoint = search.nextMove();
        point.x = nextPoint.x;
        point.y = nextPoint.y;

        imprimeDados();
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


    private void imprimeDados(){
        System.out.println(search.toString());
        //Log.d("teste", search.toString());
    }

    public int getSearchType() {
        return search.getSearchType();
    }
}
