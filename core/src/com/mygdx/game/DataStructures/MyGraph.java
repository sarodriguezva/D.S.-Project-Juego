package com.mygdx.game.DataStructures;

/**
 *
 * @author Santiago
 * @param <T>
 */
public class MyGraph<T>{
    MyDoubleLinkedList<MyDoubleLinkedList<GraphNode<T>>> adj_list = new MyDoubleLinkedList<>();
    private int vertexCount, edgesCount;
    int INFINITY = Integer.MAX_VALUE;
    
    
    public MyGraph(){
        
    }
    
    public void makeEmpty(){
        adj_list = new MyDoubleLinkedList<>();
        vertexCount = 0;
        edgesCount = 0;
    }
    
    public void addVertex(T data){
        this.addVertex(data, 0);
    }
    
    private void addVertex(T data, int weight){
        GraphNode<T> vertex = new GraphNode<>(data, weight);
        MyDoubleLinkedList<GraphNode<T>> vertexList = new MyDoubleLinkedList<>();
        vertexList.add(vertex);
        adj_list.add(vertexList);
        vertexCount++;
    }
    
    public void addEdge(T src, T dst, int cost, boolean bidirectional){
        if (!containsVertex(src)) addVertex(src);
        if (!containsVertex(dst)) addVertex(dst);
        
        getVertexList(src).add(new GraphNode<>(dst, cost));
        
        if (bidirectional) getVertexList(dst).add(new GraphNode<>(src, cost));
        edgesCount++;
    }
    
    public boolean containsVertex(T data){
        if (getVertexList(data) != null) return true; 
        
        System.out.println("No se ha encontrado el nodo.");
        return false;
    }
    
    public boolean containsEdge(T src, T dst){
        if (!containsVertex(src) || !containsVertex(dst)) return false;
        
        Node<GraphNode<T>> srcListTop = getVertexList(src).getFirst();
        
        while (srcListTop != null){
            if (srcListTop.getData().value.equals(dst)) return true;
            srcListTop = srcListTop.getNext();
        }
        
        Node<GraphNode<T>> dstListTop = getVertexList(dst).getFirst();
        while (dstListTop != null){
            if (dstListTop.getData().value.equals(src)) return true;
            dstListTop = dstListTop.getNext();
        }
        
        System.out.println("No existe arista entre estos nodos");
        return false;
    }
    
    public boolean containsEdgeOneWay(T src, T dst){
        if (!containsVertex(src) || !containsVertex(dst)) return false;
        
        Node<GraphNode<T>> srcListTop = getVertexList(src).getFirst();
        
        while (srcListTop != null){
            if (srcListTop.getData().value.equals(dst)) return true;
            srcListTop = srcListTop.getNext();
        }
        
        System.out.println("No hay camino entre estos dos nodos");
        return false;
    }
    
    public int getCost(T src, T dst){
        Node<GraphNode<T>> srcListTop = getVertexList(src).getFirst();
        
        while (srcListTop != null){
            if (srcListTop.getData().value.equals(dst)) 
                return srcListTop.getData().weight;
            srcListTop = srcListTop.getNext();
        }
        
        Node<GraphNode<T>> dstListTop = getVertexList(dst).getFirst();
        while (dstListTop != null){
            if (dstListTop.getData().value.equals(src)) 
                return dstListTop.getData().weight;
            dstListTop = dstListTop.getNext();
        }
        
        System.out.println("No existe arista entre estos nodos");
        return Integer.MIN_VALUE;
    }
    
    public int getCostOneWay(T src, T dst){
        Node<GraphNode<T>> srcListTop = getVertexList(src).getFirst();
        
        while (srcListTop != null){
            if (srcListTop.getData().value.equals(dst)) 
                return srcListTop.getData().weight;
            srcListTop = srcListTop.getNext();
        }
        
        System.out.println("No existe arista entre estos nodos");
        return Integer.MIN_VALUE;
    }
    
    public int getVertexCount(){
        return vertexCount;
    }
    
    public int getEdgesCount(){
        return edgesCount;
    }
    
    public MyDoubleLinkedList<GraphNode<T>> getVertexList(T data){
        Node<MyDoubleLinkedList<GraphNode<T>>> top = adj_list.getFirst();
        
        while (top != null){
            GraphNode<T> vertex = top.getData().getFirst().getData();
            if (vertex.value.equals(data)){
                return top.getData();
            }
            top = top.getNext();
        }
        System.out.println("No se ha encontrado el nodo.");
        return null;
    }
    
    public GraphNode<T> getVertex(T data) {
        return getVertexList(data).getFirst().getData();
    }
    
    public void printShortestPath(T src, T dst){
        shortestPath(src);
        String shortestPath = getPath(dst);
        
        System.out.println(shortestPath);
    }
    
    private void shortestPath(T s){
        if (!containsVertex(s)) return;
        
        MyQueue<MyDoubleLinkedList<GraphNode<T>>> q = new MyQueue<>();
        
        Node<MyDoubleLinkedList<GraphNode<T>>> top = adj_list.getFirst();
        
        while (top != null){
            GraphNode<T> vertex = top.getData().getFirst().getData();
            vertex.setDistance(INFINITY);
            
            if (vertex.value.equals(s)){
                vertex.setDistance(0);
                q.enqueue(top.getData());
            }
            
            top = top.getNext();
        }
        
        while (!q.isEmpty()){
            Node<GraphNode<T>> vertexNode = q.dequeue().getFirst();
            Node<GraphNode<T>> adjVertexNode = vertexNode.getNext();
            
            GraphNode<T> vertex = vertexNode.getData();
            
            while(adjVertexNode != null){
                GraphNode<T> adjVertex = adjVertexNode.getData();
                
                
                if (adjVertex.getDistance() == INFINITY) {
                    adjVertex.setDistance(vertex.getDistance() + 1);
                    adjVertex.setPath(vertex);
                    
                    MyDoubleLinkedList<GraphNode<T>> adjVertexList;
                    adjVertexList = getVertexList(adjVertex.value);
                    q.enqueue(adjVertexList);
                }
                
                adjVertexNode = adjVertexNode.getNext();
            }
        }
    }
    
    public void printCheapestPath(T src, T dst){
        cheapestPath(src);
        String cheapestPath = getPath(dst);
        
        System.out.println(cheapestPath);
    }
    
    private String getPath(T v){
        StringBuilder sb = new StringBuilder();
        GraphNode<T> dstVertex = getVertex(v);
        GraphNode<T> path = dstVertex.getPath();
        while (!dstVertex.value.equals(path.value)){
            sb.append(dstVertex.value);
            sb.append(" < ");
            dstVertex = getVertex(path.value);
            path = dstVertex.getPath();
        }
        
        String finalPath = sb.toString();
        finalPath = finalPath.substring(0, finalPath.length() - 3);
        
        return finalPath;
    }
    
    private void cheapestPath(T s){
        if (!containsVertex(s)) return;
        Node<MyDoubleLinkedList<GraphNode<T>>> top = adj_list.getFirst();
        
        while (top != null){
            GraphNode<T> vertex = top.getData().getFirst().getData();
            vertex.setDistance(INFINITY);
            vertex.setUnknown();
            
            if (vertex.value.equals(s)) vertex.setDistance(0);
            
            top = top.getNext();
        }
        
        top = adj_list.getFirst();
        
        while (top != null){
            Node<GraphNode<T>> minVertexNode = findMinDistanceVertex();
            GraphNode<T> minVertex = minVertexNode.getData();
            minVertex.setKnown();
            
            
            Node<GraphNode<T>> ptr = minVertexNode;
            
            while (ptr != null){
                GraphNode<T> adjVertex = ptr.getData();
                boolean known = adjVertex.isKnown();
                
                if (!known){
                    int minVertexDistance = minVertex.getDistance();
                    int adjVertexCost = adjVertex.weight;
                    int newDist = minVertexDistance + adjVertexCost;
                    
                    if (newDist < adjVertex.getDistance()){
                        adjVertex.setDistance(newDist);
                        adjVertex.setPath(minVertex);
                    }
                }
            }
        }
    }
    
    private Node<GraphNode<T>> findMinDistanceVertex(){
        int minDistance = INFINITY;
        Node<GraphNode<T>> minVertex = null;
        
        Node<MyDoubleLinkedList<GraphNode<T>>> top = adj_list.getFirst();
        
        while (top != null){
            Node<GraphNode<T>> currentVertexNode = top.getData().getFirst();
            GraphNode<T> currentVertex = currentVertexNode.getData();
            boolean known = currentVertex.isKnown();
            int currentDistance = currentVertex.getDistance();
            if (!known && (minDistance == -1 || currentDistance < minDistance)){
                minVertex = currentVertexNode;
                minDistance = currentDistance;
            }
            
            top = top.getNext();
        }
        
        return minVertex;
    }
}

