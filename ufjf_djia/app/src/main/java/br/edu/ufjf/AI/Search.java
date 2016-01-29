package br.edu.ufjf.ai;

import android.graphics.Point;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import br.edu.ufjf.core.Graph;
import br.edu.ufjf.core.GraphNode;

public class Search {
	
	public final static int LARGURA = 0;
    public final static int PROFUNDIDADE = 1;
    public final static int ORDENADA = 2;
    public final static int GULOSA = 3;
    public final static int AESTRELA = 4;

    private int searchType;
    public LinkedList<Point> path;
    private Point start;
    private Point end;
    private Graph graph;

    int estadosCriados = 0;
    int estadosExpandidos = 0;
    int tamanhoCaminho = 0;
    long tempo=0;

    public Search(Point start, Point end, Graph graph, int searchType) {

        this.start = start;
        this.end = end;
        this.graph = graph;

        this.searchType = searchType;
        path = new LinkedList<Point>();

    }

    public void doSearch() {

        estadosCriados = 0;
        estadosExpandidos = 0;
        tamanhoCaminho = 0;

        long tempoInicial = System.currentTimeMillis();

        switch (searchType){
            case PROFUNDIDADE : {
                depth_first();
                break;
            }

            case LARGURA : {
                breadth_first();
                break;
            }

            case ORDENADA : {
                ordenada();
                break;
            }

            case GULOSA : {
                greedy();
                break;
            }

            case AESTRELA : {
                a_star();
                break;
            }
        }

        tempo =  System.currentTimeMillis() - tempoInicial;
    }


    private void breadth_first(){
        List<GraphNode> adjacency;
        List<SearchNode> openedList = new ArrayList<SearchNode>();
        List<SearchNode> closedList = new ArrayList<SearchNode>();
        SearchNode dadNode = new SearchNode(graph.getNode(start));
        SearchNode sonNode;
        openedList.add(dadNode);
        estadosCriados++;
        estadosExpandidos++;
        while(!openedList.isEmpty() && (dadNode.point.x!=end.x || dadNode.point.y!=end.y)){
            openedList.remove(0);
            closedList.add(dadNode);
            adjacency = dadNode.adjacency;
            for(GraphNode adjNode : adjacency) {
                sonNode = new SearchNode(adjNode);
                sonNode.dad = dadNode;
                boolean contains = false;
                for (SearchNode n : openedList) {
                    if (sonNode.compareTo(n) == 0) {
                        contains = true;
                    }
                }
                for (SearchNode n : closedList) {
                    if (sonNode.compareTo(n) == 0) {
                        contains = true;
                    }
                }
                if (!contains) {
                    openedList.add(sonNode);
                    estadosCriados++;
                }
            }
            dadNode = openedList.get(0);
            estadosExpandidos++;
        }
        buildPath(dadNode);
    }

    private void depth_first(){
        List<GraphNode> adjacency;
        LinkedList<SearchNode> openedList = new LinkedList<SearchNode>();
        LinkedList<SearchNode> closedList = new LinkedList<SearchNode>();
        SearchNode auxNode = new SearchNode(graph.getNode(start));
        SearchNode newNode;
        openedList.addFirst(auxNode);
        estadosCriados++;
        while(!openedList.isEmpty() && (auxNode.point.x!=end.x || auxNode.point.y!=end.y)){
            openedList.removeLast();
            closedList.add(auxNode);
            adjacency = auxNode.adjacency;
            for(GraphNode adjNode : adjacency) {
                newNode = new SearchNode(adjNode);
                newNode.dad = auxNode;
                boolean contains = false;
                for (SearchNode n : openedList) {
                    if (newNode.compareTo(n) == 0) {
                        contains = true;
                    }
                }
                for (SearchNode n : closedList) {
                    if (newNode.compareTo(n) == 0) {
                        contains = true;
                    }
                }
                if (!contains) {

                    openedList.addLast(newNode);
                    estadosCriados++;
                }
            }
            auxNode = openedList.getLast();
            estadosExpandidos++;
        }
        buildPath(auxNode);
    }

    private void ordenada(){
        List<GraphNode> adjacency = graph.getNode(start).adjacency;
        List<SearchNode> openedList = new ArrayList<SearchNode>();
        List<SearchNode> closedList = new ArrayList<SearchNode>();
        SearchNode dadNode = new SearchNode(graph.getNode(start));
        dadNode.evaluation = 0;
        openedList.add(dadNode);
        SearchNode sonNode;
        estadosCriados++;
        while((dadNode.point.x != end.x || dadNode.point.y!=end.y ) && !openedList.isEmpty() )  {
            for (GraphNode n : adjacency) {
                sonNode = new SearchNode(n);
                Boolean contains = false;
                for (SearchNode o : closedList) {
                    if (o.compareTo(sonNode) == 0)
                        contains = true;
                }
                for (SearchNode o : openedList) {
                    if (o.compareTo(sonNode) == 0)
                        contains = true;
                }
                if (!contains) {
                    sonNode.dad = dadNode;
                    sonNode.cost = dadNode.cost + 1;
                    sonNode.evaluation = sonNode.cost;
                    openedList.add(sonNode);
                    estadosCriados++;
                }
            }
            closedList.add(dadNode);
            openedList.remove(dadNode);
            if(!openedList.isEmpty()) {
                SearchNode next = openedList.get(0);
                for (SearchNode n : openedList) {
                    if (n.evaluation <= next.evaluation)
                        next = n;
                }
                adjacency = next.adjacency;
                dadNode = next;
                estadosExpandidos++;
            }
        }
        buildPath(dadNode);
    }

    private void a_star(){
        List<GraphNode> adjacency = graph.getNode(start).adjacency;
        List<SearchNode> openedList = new ArrayList<SearchNode>();
        List<SearchNode> closedList = new ArrayList<SearchNode>();
        SearchNode dadNode = new SearchNode(graph.getNode(start));
        dadNode.evaluation = h(dadNode.point, end);
        openedList.add(dadNode);
        SearchNode sonNode;
        path.clear();
        estadosCriados++;
        while((dadNode.point.x != end.x || dadNode.point.y!=end.y ) && !openedList.isEmpty() )  {
            for (GraphNode n : adjacency) {
                sonNode = new SearchNode(n);
                Boolean contains = false;
                for (SearchNode o : closedList) {
                    if (o.compareTo(sonNode) == 0)
                        contains = true;
                }
                for (SearchNode o : openedList) {

                    if (o.compareTo(sonNode) == 0)
                        contains = true;
                }
                if (!contains) {
                    sonNode.dad = dadNode;
                    sonNode.cost = dadNode.cost + 1;
                    sonNode.evaluation = h(sonNode.point, end) + sonNode.cost;
                    openedList.add(sonNode);
                    estadosCriados++;
                }
            }
            closedList.add(dadNode);
            openedList.remove(dadNode);
            if(!openedList.isEmpty()) {
                SearchNode next = openedList.get(0);
                for (SearchNode n : openedList) {
                    if (n.evaluation <= next.evaluation)
                        next = n;
                }
                adjacency = next.adjacency;
                dadNode = next;
                estadosExpandidos++;
            }
        }
        buildPath(dadNode);
    }

    private void greedy(){
        List<GraphNode> adjacency = graph.getNode(start).adjacency;
        List<SearchNode> openedList = new ArrayList<SearchNode>();
        List<SearchNode> closedList = new ArrayList<SearchNode>();
        SearchNode dadNode = new SearchNode(graph.getNode(start));
        dadNode.evaluation = h(dadNode.point, end);
        openedList.add(dadNode);
        SearchNode sonNode;
        estadosCriados++;
        while((dadNode.point.x != end.x || dadNode.point.y!=end.y ) && !openedList.isEmpty() )  {
            for (GraphNode n : adjacency) {
                sonNode = new SearchNode(n);
                Boolean contains = false;
                for (SearchNode o : closedList) {
                    if (o.compareTo(sonNode) == 0)
                        contains = true;
                }
                for (SearchNode o : openedList) {
                    if (o.compareTo(sonNode) == 0)
                        contains = true;
                }
                if (!contains) {
                    sonNode.dad = dadNode;
                    sonNode.evaluation = h(sonNode.point, end);
                    openedList.add(sonNode);
                    estadosCriados++;
                }
            }
            closedList.add(dadNode);
            openedList.remove(dadNode);
            if(!openedList.isEmpty()) {
                SearchNode next = openedList.get(0);
                for (SearchNode n : openedList) {
                    if (n.evaluation <= next.evaluation)
                        next = n;
                }
                adjacency = next.adjacency;
                dadNode = next;
                estadosExpandidos++;
            }
        }
        buildPath(dadNode);
    }

    private static int h(Point p1, Point p2){
        return Math.abs(p2.x - p1.x) + Math.abs(p2.y - p1.y);
    }

    private void buildPath(SearchNode endOfPath){
        path.clear();
        if((endOfPath.point.x==end.x && endOfPath.point.y==end.y)) {
            SearchNode answer = endOfPath;
            while (answer.dad.point.x != start.x || answer.dad.point.y != start.y) {
                tamanhoCaminho++;
                path.addFirst(answer.point);
                answer = answer.dad;
            }
            path.addFirst(answer.point);
        }
    }

    public Point nextMove(){
        return path.removeFirst();
    }

    public List<Point> getPath() {
        return path;
    }

    public int getSearchType() {
        return searchType;
    }

    private class SearchNode extends GraphNode implements Comparable{
        public SearchNode dad;
        public int evaluation;
        public int cost;
        public SearchNode(GraphNode graphNode) {
            super(graphNode.point, graphNode.type);
            adjacency = graphNode.adjacency;
            cost = 0;
            evaluation = 0;
        }

        @Override
        public int compareTo(Object another) {

            SearchNode n = (SearchNode) another;

            if(n.point.x == point.x && n.point.y == point.y)
                return 0;
            return -1;
        }

        @Override
        public String toString(){

            return "x: "+point.x+" , y: "+point.y;
        }
    }

    @Override
    public String toString() {

        String type="";

        switch (searchType){
            case 0 : {
                type = "Largura:     ";
                break;
            }
            case 1 : {
                type = "Profundidade:";
                break;
            }
            case 2 : {
                type = "Ordenada:    ";
                break;
            }
            case 3 : {
                type = "Gulosa:      ";
                break;
            }
            case 4 : {
                type = "A*:          ";
                break;
            }

        }

        return  type +
                "\tGerados = " + estadosCriados +
                ",\tExpandidos = " + estadosExpandidos +
                ",\tCaminho = " + tamanhoCaminho +
                ",\tTempo = " + tempo + " ms";
    }
}


