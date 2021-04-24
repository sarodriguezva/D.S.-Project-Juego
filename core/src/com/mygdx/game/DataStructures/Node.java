/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.DataStructures;

/**
 * Clase que crea un nodo que almacena un elemento u objeto.
 * Contiene una referencia a un nodo siguiente y a un nodo anterior.
 * @author Santiago
 * @param <E> Tipo de objeto que almacena el nodo.
 */
public class Node<E> {
    
    private E data;
    Node<E> next;
    Node<E> previous;
    
    /**
     * Constuctor. Instancia el nodo y almacena un objeto.
     * 
     * @param data Objeto que almacena el nodo. 
     */
    public Node(E data) {
        this.data = data;
        this.next = null;
        this.previous = null;
    }
    
    /**
     * Método que retorna el objeto almacenado en el nodo.
     * 
     * @return Objeto que almacena el nodo.
     */
    public E getData(){
        return this.data;
    }
    
    /**
     * Método que retorna el nodo siguiente al nodo actual.
     * 
     * @return Nodo siguiente al actual.
     */
    public Node<E> getNext(){
        return this.next;
    }
    
    /**
     * Método que retorna el nodo anterior al nodo actual.
     * 
     * @return Nodo anterior al actual.
     */
    public Node<E> getPrevious(){
        return this.previous;
    }
}
