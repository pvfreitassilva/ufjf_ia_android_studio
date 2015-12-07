package br.edu.ufjf;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Search {
	
	public final static int PROFUNDIDADE = 0;
    public final static int LARGURA = 1;
    public final static int GULOSA = 2;
    public final static int ORDENADA = 3;
    public final static int AESTRELA = 4;

    private int searchType;
    public List<Point> path;
    private Point start;
    private Point end;
    private Graph graph;

    int estadosCriados = 0;
    int estadosExpandidos = 0;
    int tamanhoCaminho = 0;

    public Search(Point start, Point end, Graph graph, int searchType) {

        this.start = start;
        this.end = end;
        this.graph = graph;

        this.searchType = searchType;
        path = new ArrayList<Point>();

    }

    public void doSearch() {

        estadosCriados = 0;
        estadosExpandidos = 0;
        tamanhoCaminho = 0;

        switch (searchType){
            case PROFUNDIDADE : {
                profundidade();
                break;
            }

            case LARGURA : {
                largura();
                break;
            }

            case ORDENADA : {
                ordenada();
                break;
            }

            case GULOSA : {
                gulosa();
                break;
            }

            case AESTRELA : {
                aestrela();
                break;
            }
        }
    }


    private void largura(){

        List<GraphNode> adjacency;
        List<SearchNode> openedList = new ArrayList<SearchNode>();
        List<SearchNode> closedList = new ArrayList<SearchNode>();

        SearchNode auxNode = new SearchNode(graph.getNode(start));
        SearchNode newNode;

        openedList.add(auxNode);

        estadosCriados++;
        estadosExpandidos++;

        while(!openedList.isEmpty() && (auxNode.point.x!=end.x || auxNode.point.y!=end.y)){

            openedList.remove(0);

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

                    openedList.add(newNode);

                    estadosCriados++;

                }
            }
            auxNode = openedList.get(0);

            estadosExpandidos++;
        }

        buildPath(auxNode);

    }

    private void profundidade(){

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
        SearchNode auxNode = new SearchNode(graph.getNode(start));
        auxNode.evaluation = 0;
        openedList.add(auxNode);
        SearchNode newNode = null;

        estadosCriados++;

        while((auxNode.point.x != end.x || auxNode.point.y!=end.y ) && !openedList.isEmpty() )  {

            for (GraphNode n : adjacency) {

                newNode = new SearchNode(n);

                Boolean contains = false;

                for (SearchNode o : closedList) {

                    if (o.compareTo(newNode) == 0)
                        contains = true;
                }

                for (SearchNode o : openedList) {

                    if (o.compareTo(newNode) == 0)
                        contains = true;
                }

                if (!contains) {
                    newNode.dad = auxNode;
                    newNode.cost = auxNode.cost + 1;
                    newNode.evaluation = newNode.cost;
                    openedList.add(newNode);
                    estadosCriados++;
                }
            }

            closedList.add(auxNode);

            openedList.remove(auxNode);

            if(!openedList.isEmpty()) {

                SearchNode next = openedList.get(0);

                for (SearchNode n : openedList) {
                    if (n.evaluation < next.evaluation)
                        next = n;
                }

                adjacency = next.adjacency;

                auxNode = next;

                estadosExpandidos++;
            }
        }

        buildPath(auxNode);
    }

    private void aestrela(){

        List<GraphNode> adjacency = graph.getNode(start).adjacency;
        List<SearchNode> openedList = new ArrayList<SearchNode>();
        List<SearchNode> closedList = new ArrayList<SearchNode>();
        SearchNode auxNode = new SearchNode(graph.getNode(start));
        auxNode.evaluation = f(auxNode.point, end);
        openedList.add(auxNode);
        SearchNode newNode = null;

        //List<Node> path = new ArrayList<Node>();
        path.clear();

        //int j = 0;

        estadosCriados++;

        while((auxNode.point.x != end.x || auxNode.point.y!=end.y ) && !openedList.isEmpty() )  {

            for (GraphNode n : adjacency) {

                newNode = new SearchNode(n);

                Boolean contains = false;

                for (SearchNode o : closedList) {

                    if (o.compareTo(newNode) == 0)
                        contains = true;
                }

                for (SearchNode o : openedList) {

                    if (o.compareTo(newNode) == 0)
                        contains = true;
                }

                if (!contains) {

                    newNode.dad = auxNode;
                    newNode.cost = auxNode.cost + 1;
                    newNode.evaluation = f(newNode.point, end) + newNode.cost;
                    openedList.add(newNode);
                    estadosCriados++;
                }
            }

            closedList.add(auxNode);

            openedList.remove(auxNode);

            if(!openedList.isEmpty()) {

                SearchNode next = openedList.get(0);

                for (SearchNode n : openedList) {
                    if (n.evaluation < next.evaluation)
                        next = n;
                }

                adjacency = next.adjacency;

                auxNode = next;

                estadosExpandidos++;
            }
        }

        buildPath(auxNode);

    }

    private void gulosa(){

        List<GraphNode> adjacency = graph.getNode(start).adjacency;
        List<SearchNode> openedList = new ArrayList<SearchNode>();
        List<SearchNode> closedList = new ArrayList<SearchNode>();
        SearchNode auxNode = new SearchNode(graph.getNode(start));
        auxNode.evaluation = f(auxNode.point, end);
        openedList.add(auxNode);
        SearchNode newNode = null;

        estadosCriados++;

        while((auxNode.point.x != end.x || auxNode.point.y!=end.y ) && !openedList.isEmpty() )  {

            for (GraphNode n : adjacency) {

                newNode = new SearchNode(n);

                Boolean contains = false;

                for (SearchNode o : closedList) {

                    if (o.compareTo(newNode) == 0)
                        contains = true;
                }

                for (SearchNode o : openedList) {

                    if (o.compareTo(newNode) == 0)
                        contains = true;
                }

                if (!contains) {

                    newNode.dad = auxNode;
                    newNode.evaluation = f(newNode.point, end);
                    openedList.add(newNode);
                    estadosCriados++;
                }
            }

            closedList.add(auxNode);

            openedList.remove(auxNode);

            if(!openedList.isEmpty()) {

                SearchNode next = openedList.get(0);

                for (SearchNode n : openedList) {
                    if (n.evaluation < next.evaluation)
                        next = n;
                }

                adjacency = next.adjacency;

                auxNode = next;
                estadosExpandidos++;
            }
        }

       buildPath(auxNode);
    }

    private static int f(Point p1, Point p2){
        return Math.abs(p2.x - p1.x) + Math.abs(p2.y - p1.y);
    }

    private void buildPath(SearchNode endOfPath){

        path.clear();

        SearchNode answer = endOfPath;

        while(answer.dad.point.x != start.x || answer.dad.point.y != start.y){
            tamanhoCaminho++;
            path.add(answer.point);
            answer = answer.dad;
        }

        path.add(answer.point);

        //TODO refazer atualizacao do ponto
        start.x = answer.point.x;
        start.y = answer.point.y;

    }

    public List<Point> getPath() {
        return path;
    }

    private class SearchNode extends GraphNode implements Comparable{

        public SearchNode dad;
        public int evaluation;
        public int cost;

        public SearchNode (Point point, int type){
            super(point, type);
            cost = 0;
            evaluation = 0;
        }

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
}


