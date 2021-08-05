/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.DataStructures;

/**
* Clase Lista reformada. Con métodos más claros, menos redundantes
* y mas óptimos.
* Es una lista ordenada y doblemente enlazada.
* 
* @author Sebastián
* @author Santiago
* @param <E> Tipo de objetos que almacenará la lista.
*/

public class MyDoubleLinkedList<E> {

    protected Node<E> first;
    protected Node<E> last;
    protected int count;
    
    /**
     * Constructor de MyDoubleLinkedList.
     */
    public MyDoubleLinkedList() {
        this.makeEmpty();
    }
    
    /**
     * Constructor para crear una lista ordenada a partir
     * de un arreglo.
     * 
     * @param array El constructor puede recibir
     * un array de cualquier tipo y crear una lista 
     * ordenada a partir de sus elementos.
     */
    public MyDoubleLinkedList(E[] array){
        this.makeEmpty();
        for (int i = 0; i < array.length; i++){
            this.insert(i, array[i]);
        }
    }
    
    /**
     * Método para vaciar la lista.
     */
    public void makeEmpty() {
        this.count = 0;
        this.first = null;
        this.last = null;
    }
    
    /**
     * Método que busca y obtiene un nodo de la lista.
     * 
     * @param k Posición o índice del nodo.
     * @return Nodo de la lista.
     */
    private Node<E> getNode(int k){
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
    
    /**
     * Método que retorna el elemento guardado en un nodo de la lista.
     * 
     * @param index Posición o índice del nodo.
     * @return Elemento guardado en el nodo.
     */
    public E getData(int index){
        Node<E> node = getNode(index);
        
        if (node == null) return null;
        else return node.getData();
    }
    
    /**
     * Método que retorna la lista de posiciones de los nodos
     * que guardan un elemento específico.
     * 
     * @param data Elemento a buscar en la lista.
     * @return Arreglo de posiciones o índices 
     * de los nodos en los que se encuentra el elemento.
     */
    public int[] find(E data){
        int[] idxArray = new int[count];
        if (count > 0){
            Node<E> head = this.first;
            int idx = 0;
            int pos = 0;
            while (head != null){
                if (data.equals(head.getData())){
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
    
    /**
     * Método que inserta un nuevo elemento en la lista
     * en una posición dada.
     * 
     * @param k Posición o índice de la lista en la que se insertará
     * un nodo con el elemento.
     * @param data El elemento a insertar en la lista.
     */
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
    
    /**
     * Método para insertar un elemento al inicio de la lista.
     * 
     * @param data Elemento a insertar.
     */
    public void insertBegin(E data) {
        this.insert(0, data);
    }
    
    /**
     * Método para insertar un elemento al de la lista.
     * 
     * @param data Elemento a insertar. 
     */
    public void add(E data){
        this.insert(this.count, data);
    }
    
    /**
     * Método para eliminar un elemento de la lista
     * en una posición dada.
     * 
     * @param k Posición o índice del nodo a eliminar.
     * @return Elemento eliminado.
     */
    public E delete(int k){
        if (k < 0 || k >= this.count){
            System.out.println("Error al eliminar");
            return null;
        }else if (this.count == 0){
            System.out.println("Error al eliminar, lista vacía");
            return null;
        }

        if (k == 0){
            E data = this.first.getData();
            this.first.previous = null;
            this.first = this.first.next;
            this.count--;
            return data;
        } 
        
        if (k == this.count-1){
            E data = this.last.getData();
            this.last = this.last.previous;
            this.last.next = null;
            this.count--;
            return data;
        }

        Node<E> aux = this.getNode(k);
        E data = aux.getData();
        aux.previous.next = aux.next;
        aux.next.previous = aux.previous;
        this.count--;
        return data;
    }
    
    /**
     * Método para eliminar el primer elemento de la lista.
     * 
     * @return Elemento eliminado.
     */
    public E deleteBegin() {
         return this.delete(0);
    }
    
    /**
     * Método para eliminar el último elemento de la lista.
     * @return Elemento eliminado.
     */
    public E pop(){
         return delete(this.count-1);
    }
    
    /**
     * Método que retorna el primer nodo de la lista.
     * 
     * @return Primer nodo de la lista.
     */
    public Node<E> getFirst(){
        return this.first;
    }
    
    /**
     * Método que retorna el último nodo de la lista.
     * 
     * @return Último nodo de la lista.
     */
    public Node<E> getLast(){
        return this.last;
    }
    /**
     * Método para obtener el primer elemento en la lista.
     * 
     * @return Primer elemento en la lista.
     */
    public E getFirstData(){
        return getData(0);
    }
    
    /**
     * Método para obtener el último elemento en la lista.
     * 
     * @return Último elemento en la lista.
     */
    public E getLastData(){
        return getData(this.count-1);
    }
    
    /**
     * Método para obtener el tamaño de la lista.
     * 
     * @return Tamaño de la lista.
     */
    public int getSize(){
        return this.count;
    }
    
    /**
     * Método que determina si la lista está vacía.
     * 
     * @return Valor booleano. Si es verdadero, la lista está vacía.
     */
    public boolean isEmpty(){
        return this.count == 0;
    }
    
    /**
     * Método para obtener un formato en cadena de texto de todos
     * los elementos en la lista.
     * 
     * @param reverse Valor booleano. Si es verdadero, el formato
     * obtenido será el de las lista comenzando en su último elemento
     * y terminando en el primero. Si es falso, el formato obtenido
     * es el tradicional.
     * @return La cadena de texto que representa los elementos en
     * la lista.
     */
    
    private String toString(boolean reverse) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<E> aux = reverse ? this.last : this.first;
        while (aux != null){
            sb.append(aux.getData());
            sb.append(", ");
            aux = reverse ? aux.previous : aux.next;
        }
        String toReturn = sb.toString();
        toReturn =  toReturn.length() > 1 ? toReturn.substring(0, sb.length() - 2) : toReturn;
        return toReturn+"]";
    }
    
    public String toStringArr(){
        return toString(false);
    }
    /**
     * Método que imprime visualmente la lista.
     * @param reverse Valor booleano. Si es verdadero, imprimirá la lista
     * comenzando por el último elemento y terminando por el primero.
     * Si es falso, imprimirá la lista de forma tradicional.
     */
    public void print(boolean reverse) {
        System.out.println(this.toString(reverse));
    }
    public String toStringUnite(){
                StringBuilder sb = new StringBuilder();
        Node<E> aux = this.first;
        while (aux != null){
            sb.append(aux.getData());
            aux=aux.next;
        }
        String toReturn = sb.toString();
        return toReturn;
    }
}
