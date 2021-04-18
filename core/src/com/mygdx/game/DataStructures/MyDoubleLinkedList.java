/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.DataStructures;

/*
* Clase Lista reformada. Con métodos más claros, menos redundantes
* y más óptimos.
*/

class Node<E> {
    
    E data;
    Node<E> next;
    Node<E> previous;
    
    public Node(E data) {
        this.data = data;
        this.next = null;
        this.previous = null;
    }
}

public class MyDoubleLinkedList<E> {

    Node<E> first;
    Node<E> last;
    int count;

    public MyDoubleLinkedList() {
        this.makeEmpty();
    }
    
    public MyDoubleLinkedList(E[] array){
        this.makeEmpty();
        for (int i = 0; i < array.length; i++){
            this.insert(i, array[i]);
        }
    }
    
    public void makeEmpty() {
        this.count = 0;
        this.first = null;
        this.last = null;
    }
    
    private Node<E> getNode(int k){
        //Forma un poco optimizada de búsqueda de nodo.
        if (k < 0 || k >= this.count){
            System.out.println("Error al leer");
            return null;
        }

        //Determinar la menor distancia.
        boolean mode = (count - k-1 >= k);
        //mode = true : búsqueda desde first.
        //mode = false : búsqueda desde last.
        int len = mode ? k : count-k-1;
        Node<E> aux = mode ? this.first : this.last;
        for (int i = 0; i < len; i++){
            aux = mode ? aux.next : aux.previous;
        }
        return aux;
    }
    
    public E getData(Integer index){
        return getNode(index).data;
    }
    
    public int[] find(E data){
        int[] idxArray = new int[count];
        if (count > 0){
            Node<E> head = this.first;
            int idx = 0;
            int pos = 0;
            while (head != null){
                if (data.equals(head.data)){
                    idxArray[pos] = idx;
                    pos++;
                }
                idx++;
                head = head.next;
            }
        }else{
            System.out.println("Error al encontrar el elemento.");
        }
        return idxArray;
    }
    
    public void insert(int k, E data){
        if (k < 0 || k > this.count){
            System.out.println("Error al insertar");
            return;
        }
        
        Node<E> newNode = new Node<>(data);
        if (this.count == 0){
            this.first = this.last = newNode;
            this.first.previous = this.last.next = null;
            this.count++;
            return;
        }

        if (k == 0){
            newNode.next = this.first;
            this.first.previous = newNode;
            this.first = newNode;
            this.count++;
            return;
        }
        
        if (k == this.count){
            this.last.next = newNode;
            newNode.previous = this.last;
            this.last = newNode;
            this.count++;
            return;
        }
        
        
        Node<E> aux = this.getNode(k-1);
        aux.next.previous = newNode;
        newNode.next = aux.next;
        aux.next = newNode;
        newNode.previous = aux;
        this.count++;
    }
    
    public void insertBegin(E data) {
         insert(0, data);
    }
    
    public void add(E data){
        insert(this.count, data);
    }
    
    public E delete(int k){
        if (k < 0 || k >= this.count){
            System.out.println("Error al eliminar");
            return null;
        }else if (this.count == 0){
            System.out.println("Error al eliminar, lista vacía");
            return null;
        }

        if (k == 0){
            E data = this.first.data;
            this.first = this.first.next;
            this.first.previous = null;
            this.count--;
            return data;
        } 
        
        if (k == this.count-1){
            E data = this.last.data;
            this.last = this.last.previous;
            this.last.next = null;
            this.count--;
            return data;
        }

        Node<E> aux = this.getNode(k);
        E data = aux.data;
        aux.previous.next = aux.next;
        aux.next.previous = aux.previous;
        return data;
    }
    
    public void deleteBegin() {
         this.delete(0);
    }
    
    public E pop(){
         return delete(this.count-1);
    }
    
    public E getFirstData(){
        if (this.first != null) return this.first.data;
        else return null;
    }
    
    public E getLastData(){
        if (this.last != null) return this.last.data;
        else return null;
    }
    
    public int getSize(){
        return this.count;
    }

    public boolean isEmpty(){
        return this.count == 0;
    }
    
    public String toString(boolean reverse) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<E> aux = reverse ? this.last : this.first;
        while (aux != null){
            sb.append(aux.data);
            sb.append(", ");
            aux = reverse ? aux.previous : aux.next;
        }
        String toReturn = sb.toString();
        toReturn =  toReturn.length() > 1 ? toReturn.substring(0, sb.length() - 2) : toReturn;
        return toReturn+"]";
    }
    
    public void printList() {
        System.out.println(this.toString(false));
    }
}
