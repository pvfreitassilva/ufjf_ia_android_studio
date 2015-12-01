package br.edu.ufjf;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.graphics.Point;
import android.util.Log;

public class Enemy {
	
//	public Node node;
    public int x ,y;
    public int searchType;
    public List<Node> path;

    int estadosCriados = 0;
    int estadosExpandidos = 0;
    int tamanhoCaminho = 0;

    public Enemy(int x, int y, int searchType){
		this.x = x;
        this.y = y;
        this.searchType=searchType;
        path = new ArrayList<Node>();
	}
	
	//TODO IMPLEMENTAR IA AQUI
	//public void makeMove(Node objetivo, Board estado, Graph graph){
	public void makeMove(Graph graph, Point player){
		
		/*Random r = new Random();
		
		List<Node> adjacency = graph.getAdjacency(x, y);
		
		Node node = adjacency.get(r.nextInt(adjacency.size()));
		
		x = node.x;
		y = node.y;*/
		
		//ordenada(graph, player);
        //aestrela(graph, player);
        //gulosa(graph, player);
        //largura(graph, player);
        //profundidade(graph, player);

        //path = new Search().buildPath(node, objetivo, searchType);


        switch(searchType){

            case Search.PROFUNDIDADE   :{profundidade(graph, player);break;}
            case Search.LARGURA        :{largura(graph, player);break;}
            case Search.GULOSA         :{gulosa (graph, player);break;}
            case Search.ORDENADA       :{ordenada(graph, player);break;}
            case Search.AESTRELA       :{aestrela(graph, player);break;}

        }

        imprimeDados();

        //node.x = path.get(0).x;
        //node.y = path.get(0).y;
    }
	




    //todo remove

    private void largura(Graph graph, Point player){

        //todo remove

        estadosCriados = estadosExpandidos = tamanhoCaminho = 0;


        List<Node> adjacency;
        List<Node> openedList = new ArrayList<Node>();
        List<Node> closedList = new ArrayList<Node>();

        Node auxNode = graph.nodes[x][y].clone();
        Node newNode;

        //List<Node> path = new ArrayList<Node>();
        path.clear();

        openedList.add(auxNode);

        estadosCriados++;
        estadosExpandidos++;

        while(!openedList.isEmpty() && (auxNode.x!=player.x || auxNode.y!=player.y)){

            openedList.remove(0);

            closedList.add(auxNode);

            adjacency = auxNode.adjacency;

            for(Node adjNode : adjacency) {
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

                    estadosCriados++;

                }
            }
            auxNode = openedList.get(0);

            estadosExpandidos++;
        }

        Node answer = auxNode;

        while(answer.dad.x != x || answer.dad.y != y){

            tamanhoCaminho++;

            path.add(answer);
            //System.out.println("Adcionando");
            answer = answer.dad;
        }

        path.add(answer);

        this.x = answer.x;
        this.y = answer.y;

        //return path;

        //e.x = answer.x;
        //e.y = answer.y;
    }

    private void profundidade(Graph graph, Point destino){

        //todo remove
        estadosCriados = estadosExpandidos = tamanhoCaminho = 0;

        List<Node> adjacency;
        LinkedList<Node> openedList = new LinkedList<Node>();
        LinkedList<Node> closedList = new LinkedList<Node>();

        Node auxNode = graph.nodes[x][y].clone();
        Node newNode;

        //List<Node> path = new ArrayList<Node>();
        path.clear();

        openedList.addFirst(auxNode);

        estadosCriados++;

        while(!openedList.isEmpty() && (auxNode.x!=destino.x || auxNode.y!=destino.y)){

            openedList.removeLast();

            closedList.add(auxNode);

            adjacency = auxNode.adjacency;

            for(Node adjNode : adjacency) {

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
                    estadosCriados++;

                }
            }
            auxNode = openedList.getLast();
            estadosExpandidos++;
        }

        Node answer = auxNode;

        while(answer.dad.x != x || answer.dad.y != y){
            path.add(answer);
            answer = answer.dad;
            tamanhoCaminho++;
        }

        path.add(answer);

        this.x = answer.x;
        this.y = answer.y;

        //return path;

        //TODO REMOVER COMENTÁRIOS
        //x = answer.x;
        //y = answer.y;

    }

    private void ordenada(Graph graph, Point destino){

        //todo remove
        estadosCriados = estadosExpandidos = tamanhoCaminho = 0;

        List<Node> adjacency = graph.nodes[x][y].adjacency;
        List<Node> openedList = new ArrayList<Node>();
        List<Node> closedList = new ArrayList<Node>();
        Node auxNode = graph.nodes[x][y].clone();
        auxNode.evaluation = 0;
        openedList.add(auxNode);
        Node newNode = null;

        //List<Node> path = new ArrayList<Node>();
        path.clear();

        //int j = 0;

        estadosCriados++;

        while((auxNode.x != destino.x || auxNode.y!=destino.y ) && !openedList.isEmpty() )  {

            for (Node n : adjacency) {

                newNode = n.clone();

                Boolean contains = false;

                for (Node o : closedList) {

                    if (o.compareTo(newNode) == 0)
                        contains = true;
                }

                for (Node o : openedList) {

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

                Node next = openedList.get(0);

                for (Node n : openedList) {
                    if (n.evaluation < next.evaluation)
                        next = n;
                }

                adjacency = next.adjacency;

                auxNode = next;

                estadosExpandidos++;
            }
        }

        Node answer = auxNode;

        while(answer.dad.x != x || answer.dad.y != y){
            path.add(answer);
            answer = answer.dad;
            tamanhoCaminho++;
        }

        path.add(answer);

        this.x = answer.x;
        this.y = answer.y;

        //return path;

        //TODO REMOVER COMENTÁRIOS
        //x = answer.x;
        //y = answer.y;


    }

    private void aestrela(Graph graph, Point destino){

        //todo remove
        estadosCriados = estadosExpandidos = tamanhoCaminho = 0;

        List<Node> adjacency = graph.nodes[x][y].adjacency;
        List<Node> openedList = new ArrayList<Node>();
        List<Node> closedList = new ArrayList<Node>();
        Node auxNode = graph.nodes[x][y].clone();
        auxNode.evaluation = f(auxNode, destino);
        openedList.add(auxNode);
        Node newNode = null;

        //List<Node> path = new ArrayList<Node>();
        path.clear();

        //int j = 0;

        estadosCriados++;

        while((auxNode.x != destino.x || auxNode.y!=destino.y ) && !openedList.isEmpty() )  {

            for (Node n : adjacency) {

                newNode = n.clone();

                Boolean contains = false;

                for (Node o : closedList) {

                    if (o.compareTo(newNode) == 0)
                        contains = true;
                }

                for (Node o : openedList) {

                    if (o.compareTo(newNode) == 0)
                        contains = true;
                }

                if (!contains) {

                    newNode.dad = auxNode;
                    newNode.cost = auxNode.cost + 1;
                    newNode.evaluation = f(newNode, destino) + newNode.cost;
                    openedList.add(newNode);
                    estadosCriados++;
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

                estadosExpandidos++;
            }
        }

        Node answer = auxNode;

        while(answer.dad.x != x || answer.dad.y != y){
            path.add(answer);
            answer = answer.dad;
            tamanhoCaminho++;
        }

        path.add(answer);

        this.x = answer.x;
        this.y = answer.y;

        //return path;

        //TODO REMOVER COMENTÁRIOS
        //x = answer.x;
        //y = answer.y;

    }

    private void gulosa(Graph graph, Point destino){

        //todo remove
        estadosCriados = estadosExpandidos = tamanhoCaminho = 0;

        List<Node> adjacency = graph.nodes[x][y].adjacency;
        List<Node> openedList = new ArrayList<Node>();
        List<Node> closedList = new ArrayList<Node>();
        Node auxNode = graph.nodes[x][y].clone();
        auxNode.evaluation = f(auxNode, destino);
        openedList.add(auxNode);
        Node newNode = null;

        //List<Node> path = new ArrayList<Node>();
        path.clear();

        int j = 0;

        estadosCriados++;

        while((auxNode.x != destino.x || auxNode.y!=destino.y ) && !openedList.isEmpty() )  {

            for (Node n : adjacency) {

                newNode = n.clone();

                Boolean contains = false;

                for (Node o : closedList) {

                    if (o.compareTo(newNode) == 0)
                        contains = true;
                }

                for (Node o : openedList) {

                    if (o.compareTo(newNode) == 0)
                        contains = true;
                }

                if (!contains) {

                    newNode.dad = auxNode;
                    newNode.evaluation = f(newNode, destino);
                    openedList.add(newNode);
                    estadosCriados++;
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
                estadosExpandidos++;
            }
        }

        Node answer = auxNode;

        while(answer.dad.x != x || answer.dad.y != y){
            path.add(answer);
            answer = answer.dad;
            tamanhoCaminho++;
        }

        path.add(answer);

        this.x = answer.x;
        this.y = answer.y;

        //return path;

        //TODO REMOVER COMENTARIOS
        //x = answer.x;
        //y = answer.y;
    }

    private int f(Node origem, Point objetivo){
        return Math.abs(objetivo.x - origem.x) + Math.abs(objetivo.y - origem.y);
    }

    private void imprimeDados(){

        Log.d("Teste", "Inimigo: "+ x + ", "+y);
        Log.d("Teste", "Busca: "+ searchType);
        Log.d("Teste", "Estados criados: "+ estadosCriados);
        Log.d("Teste", "Estados expandidos: "+ estadosExpandidos);
        Log.d("Teste", "Tamanho do caminho: "+ tamanhoCaminho);


    }

}
