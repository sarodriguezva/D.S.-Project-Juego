/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.DataStructures;

/**
 * Implementaci�n de una pila a partir de la Lista Doblemente Enlazada.
 * 
 * @author Santiago 
 * @param <T> Tipo de objetos que almacenar� la pila.
 */
public class MyStack<T> extends MyDoubleLinkedList<T>{
    private Node<T> top;
    
    /**
     * Constructor. Crea una pila vac�a.
     */
    public MyStack(){
        super();
    }
    
    /**
     * Constructor. Crea y llena una pila a partir de datos
     * tomados de un arreglo.
     * 
     * @param array Arreglo de cualquier tipo.
     */
    public MyStack(T[] array){
        super(array);
    }
    
    /**
     * M�todo para vaciar la pila.
     */
    @Override
    public void makeEmpty(){
        super.makeEmpty();
        this.top = super.last;
    }
    
    /**
     * M�todo para insertar un elemento en la cima de la pila.
     * 
     * @param data Elemento a insertar.
     */
    public void push(T data){
        super.add(data);
        this.top = super.last;
    }
    
    /**
     * M�todo para revisar el elemento almacenado en la cima de la pila.
     * @return Elemento que almacena el nodo top.
     */
    public T peek(){
        return this.top == null ? null : this.top.getData();
    }
}
