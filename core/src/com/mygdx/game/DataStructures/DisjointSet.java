/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.DataStructures;

/**
 * Clase Conjuntos Disjuntos.
 * Implementación usando árboles.
 * 
 * @author Santiago Rodríguez Vallejo
 */
public class DisjointSet {
    int parent[];
    int rank[];
    
    public DisjointSet(int data){
        this.makeSet(data);
    }
    
    public void makeSet(int data){
        parent[data] = data;
        rank[data] = 0;
    }
    
    public int find(int data){
        while (data != parent[data]){
            parent[data] = find(parent[data]);
        }
        return parent[data];
    }
    
    public void union(int i, int j){
        int i_id = find(i);
        int j_id = find(i);
        
        if (i_id == j_id) return;
        
        if (rank[i_id] > rank[j_id]) parent[j_id] = i_id;
        else{
            parent[i_id] = j_id;
            if (rank[j_id] == rank[i_id]) rank[j_id] += 1;
        }
    }
}
