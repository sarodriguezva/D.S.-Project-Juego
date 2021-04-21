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
        this.makeEmpty();
    }
    
    /**
     * Constructor. Crea y llena una pila a partir de datos
     * tomados de un arreglo.
     * 
     * @param array Arreglo de cualquier tipo.
     */
    public MyStack(T[] array){
        super(array);
        this.top = super.last;
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
     * M�todo que retorna y elimina el elemento en la cima de la pila.
     * @return Elemento actualmente en la cima de la pila.
     */
    @Override
    public T pop(){
        T toReturn = super.pop();
        this.top = super.last;
        return toReturn;
    }
    
    /**
     * M�todo para revisar el elemento almacenado en la cima de la pila.
     * @return Elemento que almacena el nodo top.
     */
    public T peek(){
        return this.top == null ? null: this.top.getData();
    }
    
    /**
     * M�todo que retorna el tama�o de la pila.
     * @return Entero que representa el n�mero de elementos en la pila.
     */
    @Override
    public int getSize(){
        return super.getSize();
    }
    
    /**
     * M�todo que determina si la pila est� vac�a.
     * @return Valor booleano. Si es verdadero, la pila est� vac�a.
     */
    @Override
    public boolean isEmpty(){
        return super.isEmpty();
    }
    
    /**
     * M�todo que imprime visualmente los elementos en la pila.
     * Comenzando desde la cima.
     */
    public void print(){
        super.print(true);
    }
}
