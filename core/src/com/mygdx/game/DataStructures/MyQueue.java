/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.DataStructures;

/**
 * Implementación de una cola a partir de la Lista Doblemente Enlazada.
 * @author Santiago
 * @param <T> Tipo de objetos que almacenará la cola.
 */
public class MyQueue<T> extends MyDoubleLinkedList<T> {
    private Node<T> front;
    private Node<T> back;
    
    /**
     * Constructor. Crea una cola vacía.
     */
    public MyQueue(){
        this.makeEmpty();
    }
    
    /**
     * Constructor. Crea y llena una cola a partir de datos
     * tomados de un arreglo.
     * 
     * @param array Arreglo de cualquier tipo.
     */
    public MyQueue(T[] array){
        super(array);
        this.front = super.last;
        this.back = super.first;
    }
    
    /**
     * Método para vaciar la cola.
     */
    @Override
    public void makeEmpty(){
        super.makeEmpty();
        this.front = super.last;
        this.back = super.first;
    }
    
    /**
     * Método para insertar un elemento al final de la cola.
     * 
     * @param data Elemento a insertar.
     */
    public void enqueue(T data){
        super.add(data);
        this.back = super.last;
    }
    
    /**
     * Método que retorna y elimina el elemento en el frente de la cola.
     * @return Elemento actualmente en el frente de la cola.
     */
    public T dequeue(){
        T toReturn = super.deleteBegin();
        this.front = super.first;
        return toReturn;
    }
    
    /**
     * Método para revisar el elemento almacenado en el frente de la cola.
     * @return Elemento que almacena el nodo front.
     */
    public T peek(){
        return this.front == null ? null: this.front.getData();
    }
    
    /**
     * Método que retorna el tamaño de la cola.
     * @return Entero que representa el número de elementos en la cola.
     */
    @Override
    public int getSize(){
        return super.getSize();
    }
    
    /**
     * Método que determina si la cola está vacía.
     * @return Valor booleano. Si es verdadero, la cola está vacía.
     */
    @Override
    public boolean isEmpty(){
        return super.isEmpty();
    }
    
    /**
     * Método que imprime visualmente los elementos en la cola, comenzando
     * desde el frente.
     */
    public void print(){
        super.print(false);
    }
}
