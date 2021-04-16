/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.DataStructures;

import java.util.Objects;

/**
 *
 * @author JUANS
 */

/* NODO QUE SIRVE PARA MUCHAS ESTRUCTURAS DE DATOS
PARA ESE NODO, E ES CUALQUIER TIPO DE DATO, NEXT
ES A DONDE APUNTA EL SIGUIENTE NODO*/

class Node<E> extends Object {
// ESTA CLASE ESTÁ LISTA PARA SER USADA COMO NODO EN UNA LISTA DOBLEMENTE ENCADENADA Y EN UNA LISTA SIMPLE. YA QUE LA LISTA DOBLE NECESITA UN PUNTERO AL PRIMER NODO, 
    //AL ULTIMO, Y CADA NODO DEBE TENER UN REGISTRO DEL NODO SIGUIENTE Y ANTERIOR.
    E data;
    Node<E> next;
    Node<E> previous;
    public Node(E data) {
        this.data = data;
        this.next = null;
        this.previous = null;
    }
}

/* 
Para esta lista count es un contador que
todo el tiempo nos dice el tamaño de la lista,
por temas academicos */
public class MySimpleLinkedList<E> {

    Node<E> first;
    Node<E> last;
    Integer count;

    public MySimpleLinkedList() {
        this.makeEmpty();
    }
     public MySimpleLinkedList(E[] array){
        MySimpleLinkedList<E> arraytolist = new MySimpleLinkedList<>();
        for (int i=0; i<array.length ; i++){
            arraytolist.add(array[i]);
        }
        
    }

    /* PARA  INSERTAR UN NUEVO DATO SE CREA UN NODO
    LUEGO SE BUSCA EL PRIMER NODO PARA PONERLO
    AL INICIO Y PASAR LA REFERENCIA. O(1)
     */
   public void insertBegin(E toInsert) {
        Node<E> newNode = new Node<>(toInsert);
        newNode.next = this.first;
        newNode.previous = null;
        this.first = newNode;
       if (this.count == 0){
           this.last=this.first;
       }
       else{
           newNode.next.previous=newNode;
       }
        this.count++;
    }
    
   public void add(E toInsert){
        if (this.count == 0){
            insertBegin(toInsert);
        }
        else{
        Node<E> newNode = new Node<>(toInsert);
        this.last.next = newNode;
        newNode.previous = this.last;
        this.last=newNode;
        this.count++;
        }
    }

    /// Es similar a insertar el O(1)
   public void deleteBegin() {
        if (this.first == null) {
            System.err.println("Error al intentar eliminar un dato");
        } else {
            this.first = this.first.next;
            this.first.previous=null;
            this.count--;
        }
    }
    
    // TO DO 1 ELIMINAR EN POSICIÓN K: O(N)
   public void kdelete(Integer index){
        if (index < 0 || index >= this.count) {
            System.err.println("Error, no se puede buscar por fuera de la lista");
        } else {
        if (index == 0){
            deleteBegin();
        }
        else{
       
        Node<E> toDelete = getNode(index);
        Node<E> prek  = toDelete.previous;
        Node<E> postk = toDelete.next;
        prek.next=postk;
        postk.previous=prek;
        this.count--;
        }
    }
        
    }
    
   public E pop(){
        Node<E> lastNode = this.last;
        lastNode.previous.next=null;
        this.last=lastNode.previous;
        this.count--;
        return lastNode.data;
        
    }
    
    /* EVIDENTEMENTE TOCA RECORRER TODOS LOS NODOS EN EL PEOR
    DE LOS CASOS , DEBERIA SER O(N), DEVUELVE LOS INDICES DEL DATO A BUSCAR */
  public  MySimpleLinkedList<E> find(E data) {
        if (this.first == null) {
            System.err.println("No se puede buscar en una lista vacia");
            return null;
        } else {
            Node<E> point = this.first;
            Integer cnt = 0;
            MySimpleLinkedList<E> toList = new MySimpleLinkedList<>();
            while (point != null) {
                if (point.data.equals(data)) { // SE USA EL .EQUALS PORQUE ASI SE COMPARAN OBJETOS
                    // DE MANERA CORRECTA. EL == SOLO FUNCIONA PARA PRIMITIVOS
                    toList.add((E) cnt);//aca
                }
                point = point.next;
                cnt++;
            }
            return toList;
        }
    }

    /// O(1)
  public  void makeEmpty() {
        this.count = 0;
        this.first = null;
        this.last = null;
    }

    /* MÉTODO PARA BUSCAR EL NODO EN EL INDICE K, SERIA O(N)*/
 public   Node<E> getNode(Integer index) {
        if (index < 0 || index >= this.count) {
            System.err.println("Error, no se puede buscar por fuera de la lista");
            return null;
        } else {

            Node<E> point = this.first;
            for (int i = 0; i < index; i++) {
                point = point.next;
            }
            return point;
        }
    }
    
  public  E getData(Integer index){//ESTE ES EL METODO READ PERO CON OTRO NOMBRE.
        return getNode(index).data;
    }
    
  public  E getFirstData(){
        if (this.count == 0){
            System.err.println("Error, no se puede buscar por fuera de la lista o en lista vacia");
            return null;
        }
        return this.first.data;
    }
    
   public E getLastData(){
         if (this.count == 0){
            System.err.println("Error, no se puede buscar por fuera de la lista o en lista vacia");
            return null;
        }
        return this.last.data;
    }

    /* Inserta en el indice K, es O(N) en nuestro codigo, el de java es O(1)*/
   public void insert(Integer index, E data) {
        if (index < 0 || index >= this.count + 1) {
            System.err.println("Error, no se puede buscar por fuera de la lista");
        }
        if (index == 0) {
            insertBegin(data);
            return;
        }
        if (Objects.equals(index, this.count)){
            add(data);
            return;
        }
        
        Node<E> point = getNode(index - 1);
        Node<E> aux = new Node<>(data);
        aux.next = point.next;
        aux.previous = point;
        point.next.previous=aux;
        point.next = aux;
        this.count++;
        
    }
    
  public  Integer getSize(){
        return this.count;
    }

 public   Boolean isEmpty(){
        return this.count == 0;
    }
    /* METODO PARA PASAR LA LISTA A UN STRING
    O(N) porque debe construir un nuevo string pasando por cada nodo
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(); // El string builder deja concatenar mas rapido en vez del string con un +.
        sb.append("[");
        if (count == 0){
            return "[]";
        }
        Node<E> point = this.first;
        while (point != null) {
            sb.append(point.data);
            sb.append(", ");
            point = point.next;
        }
        sb.delete(sb.length()-2, sb.length());
        sb.append("]");
        String toReturn = sb.toString();
        return toReturn ;
    }

    /// esto es solo por comodidad, como tal seria o(n) de toString
  public  void printList() {
        System.out.println(toString());
    }
    
    // ESTA FUNCIÓN IMPRIME USANDO LOS PREVIOUS DE LOS NODOS, NO ES NECESARIA PERO ES UTIL PARA HACER DEBUG DE QUE TODO FUNCIONE BIEN AL REVÉS
   public String printListInverted() {
        StringBuilder sb = new StringBuilder(); // El string builder deja concatenar mas rapido en vez del string con un +.
        sb.append("[");
        if (count == 0){
            return "[]";
        }
        Node<E> point = this.last;
        while (point != null) {
            sb.append(point.data);
            sb.append(", ");
            point = point.previous;
        }
        sb.delete(sb.length()-2, sb.length());
        sb.append("]");
        String toReturn = sb.toString();
        return toReturn;
    }
    
}
