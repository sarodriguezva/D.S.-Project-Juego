package com.mygdx.game.DataStructures;

/**
 *
 * @author Santiago
 * @param <T>
 */
public class MyGraph<T>{
    MyDoubleLinkedList<MyDoubleLinkedList<GraphNode<T>>> adj_list = new MyDoubleLinkedList<>();
    private int vertexCount, edgesCount;
    
    
    public MyGraph(){
        
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
    
    public int getVertexCount(){
        return vertexCount;
    }
    
    public int getEdgesCount(){
        return edgesCount;
    }
    
    private MyDoubleLinkedList<GraphNode<T>> getVertexList(T data){
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
}

class GraphNode<T>{
    T value;
    int weight;
    
    public GraphNode(T v, int w){
        this.value = v;
        this.weight = w;
    }
}