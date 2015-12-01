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
    public final static int CIMA = 0;
    public final static int BAIXO = 1;
    public final static int DIREITA = 3;
    public final static int ESQUERDA = 4;

    /*
    public List<Node>  buildPath(Node origem, Node destino, int searchType){

        switch(searchType){

            case PROFUNDIDADE   :{return profundidade(origem, destino);}
            case LARGURA        :{return largura(origem, destino);}
            case GULOSA         :{return gulosa(origem, destino);}
            case ORDENADA       :{return ordenada(origem, destino);}
            case AESTRELA       :{return aestrela(origem, destino);}

        }

        return null;
    }

    private List<Node> largura(Node origem, Node destino){

        Node[] adjacency;
        List<Node> openedList = new ArrayList<Node>();
        List<Node> closedList = new ArrayList<Node>();

        Node auxNode = origem.clone();
        Node newNode;

        List<Node> path = new ArrayList<Node>();

        openedList.add(auxNode);

        while(!openedList.isEmpty() && (auxNode.x!=destino.x || auxNode.y!=destino.y)){

            openedList.remove(0);

            closedList.add(auxNode);

            adjacency = auxNode.adjacency;

            for(Node adjNode : adjacency) {
                if (adjNode != null) {
                    newNode = adjNode.clone();

                    newNode.dad = auxNode;

                    boolean contains = false;

                    for (Node n : openedList) {
                        if (newNode.compareTo(n) == 0) {
                            contains = true;
                        }
                    }

                    for (Node n : closedList) {
                        if (newNode.compareTo(n) == 0) {
                            contains = true;
                        }
                    }

                    if (!contains) {

                        openedList.add(newNode);

                    }
                }
                auxNode = openedList.get(0);
            }
        }

        Node answer = auxNode;

        while(answer.dad.x != origem.x || answer.dad.y != origem.y){
            path.add(answer);
            System.out.println("Adcionando");
            answer = answer.dad;
        }

        path.add(answer);

        return path;

        //e.x = answer.x;
        //e.y = answer.y;
    }

    private List<Node> profundidade(Node origem, Node destino){

        Node[] adjacency;
        LinkedList<Node> openedList = new LinkedList<Node>();
        LinkedList<Node> closedList = new LinkedList<Node>();

        Node auxNode = origem.clone();
        Node newNode;

        List<Node> path = new ArrayList<Node>();

        openedList.addFirst(auxNode);

        while(!openedList.isEmpty() && (auxNode.x!=destino.x || auxNode.y!=destino.y)){

            openedList.removeLast();

            closedList.add(auxNode);

            adjacency = auxNode.adjacency;

            for(Node adjNode : adjacency) {

                if (adjNode != null) {

                    newNode = adjNode.clone();

                    newNode.dad = auxNode;

                    boolean contains = false;

                    for (Node n : openedList) {
                        if (newNode.compareTo(n) == 0) {
                            contains = true;
                        }
                    }

                    for (Node n : closedList) {
                        if (newNode.compareTo(n) == 0) {
                            contains = true;
                        }
                    }

                    if (!contains) {

                        openedList.addLast(newNode);

                    }
                }
            }
            auxNode = openedList.getLast();
        }

        Node answer = auxNode;

        while(answer.dad.x != origem.x || answer.dad.y != origem.y){
            path.add(answer);
            answer = answer.dad;
        }

        path.add(answer);

        return path;

        //TODO REMOVER COMENTÁRIOS
        //x = answer.x;
        //y = answer.y;

    }

    private List<Node> ordenada(Node origem, Node destino){

        Node[] adjacency = origem.adjacency;
        List<Node> openedList = new ArrayList<Node>();
        List<Node> closedList = new ArrayList<Node>();
        Node auxNode = origem.clone();
        auxNode.evaluation = 0;
        openedList.add(auxNode);
        Node newNode = null;

        List<Node> path = new ArrayList<Node>();

        //int j = 0;

        while((auxNode.x != destino.x || auxNode.y!=destino.y ) && !openedList.isEmpty() )  {

            for (Node n : adjacency) {

                if (n != null) {

                    newNode = n.clone();

                    Boolean contains = false;

                    for (Node o : closedList) {

                        if (o.compareTo(newNode) == 0)
                            contains = true;
                    }

                    if (!contains) {

                        newNode.dad = auxNode;
                        newNode.cost = auxNode.cost + 1;
                        newNode.evaluation = newNode.cost;
                        openedList.add(newNode);
                    }
                }
            }

            closedList.add(auxNode);

            openedList.remove(auxNode);

            if(!openedList.isEmpty()) {

                Node next = openedList.get(0);

                for (Node n : openedList) {
                    if (n.evaluation < next.evaluation)
                        next = n;
                }

                adjacency = next.adjacency;

                auxNode = next;
            }
        }

        Node answer = auxNode;

        while(answer.dad.x != origem.x || answer.dad.y != origem.y){
            path.add(answer);
            answer = answer.dad;
        }

        path.add(answer);

        return path;

        //TODO REMOVER COMENTÁRIOS
        //x = answer.x;
        //y = answer.y;


    }

    private List<Node> aestrela(Node origem, Node destino){

        Node[] adjacency = origem.adjacency;
        List<Node> openedList = new ArrayList<Node>();
        List<Node> closedList = new ArrayList<Node>();
        Node auxNode = origem.clone();
        auxNode.evaluation = f(auxNode, destino);
        openedList.add(auxNode);
        Node newNode = null;

        List<Node> path = new ArrayList<Node>();

        //int j = 0;

        while((auxNode.x != destino.x || auxNode.y!=destino.y ) && !openedList.isEmpty() )  {

            for (Node n : adjacency) {

                if(n!=null) {
                    newNode = n.clone();

                    Boolean contains = false;

                    for (Node o : closedList) {

                        if (o.compareTo(newNode) == 0)
                            contains = true;
                    }

                    if (!contains) {

                        newNode.dad = auxNode;
                        newNode.cost = auxNode.cost + 1;
                        newNode.evaluation = f(newNode, destino) + newNode.cost;
                        openedList.add(newNode);
                    }
                }
            }

            closedList.add(auxNode);

            openedList.remove(auxNode);

            if(!openedList.isEmpty()) {

                Node next = openedList.get(0);

                for (Node n : openedList) {
                    if (n.evaluation < next.evaluation)
                        next = n;
                }

                adjacency = next.adjacency;

                auxNode = next;
            }
        }

        Node answer = auxNode;

        while(answer.dad.x != origem.x || answer.dad.y != origem.y){
            path.add(answer);
            answer = answer.dad;
        }

        path.add(answer);

        return path;

        //TODO REMOVER COMENTÁRIOS
        //x = answer.x;
        //y = answer.y;

    }

    private List<Node> gulosa(Node origem, Node destino){

        Node[] adjacency = origem.adjacency;
        List<Node> openedList = new ArrayList<Node>();
        List<Node> closedList = new ArrayList<Node>();
        Node auxNode = origem.clone();
        auxNode.evaluation = f(auxNode, destino);
        openedList.add(auxNode);
        Node newNode = null;

        List<Node> path = new ArrayList<Node>();

        int j = 0;

        while((auxNode.x != destino.x || auxNode.y!=destino.y ) && !openedList.isEmpty() )  {

            for (Node n : adjacency) {

                if(n!=null) {
                    newNode = n.clone();

                    Boolean contains = false;

                    for (Node o : closedList) {

                        if (o.compareTo(newNode) == 0)
                            contains = true;
                    }

                    if (!contains) {

                        newNode.dad = auxNode;
                        newNode.evaluation = f(newNode, destino);
                        openedList.add(newNode);
                    }
                }
            }

            closedList.add(auxNode);

            openedList.remove(auxNode);

            if(!openedList.isEmpty()) {

                Node next = openedList.get(0);

                for (Node n : openedList) {
                    if (n.evaluation < next.evaluation)
                        next = n;
                }

                adjacency = next.adjacency;

                auxNode = next;
            }
        }

        Node answer = auxNode;

        while(answer.dad.x != origem.x || answer.dad.y != origem.y){
            path.add(answer);
            answer = answer.dad;
        }

        path.add(answer);

        return path;

        //TODO REMOVER COMENTARIOS
        //x = answer.x;
        //y = answer.y;
    }

    private int f(Node origem, Node objetivo){
        return Math.abs(objetivo.x - origem.x) + Math.abs(objetivo.y - origem.y);
    }

*/
}
