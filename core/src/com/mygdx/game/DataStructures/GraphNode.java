/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.DataStructures;

/**
 *
 * @author Raul
 */
public class GraphNode<T>{
    T value;
    int weight;
    private int dist;
    private GraphNode<T> path;
    private boolean known;
    
    public GraphNode(T v, int w){
        this.value = v;
        this.weight = w;
    }
    
    public void setDistance(int distance){
        this.dist = distance;
    }
    
    public int getDistance(){
        return this.dist;
    }
    
    public void setPath(GraphNode<T> path){
        this.path = path;
    }
    
    public GraphNode<T> getPath(){
        return this.path;
    }
    
    public void setKnown(){
        this.known = true;
    }
    
    public void setUnknown(){
        this.known = false;
    }
    
    public boolean isKnown(){
        return this.known;
    }
    
    public T getValue() {
        return this.value;
    }
}