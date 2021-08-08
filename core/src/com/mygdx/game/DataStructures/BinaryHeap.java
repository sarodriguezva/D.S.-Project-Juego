package com.mygdx.game.DataStructures;
/**
 * Clase Mont�culo Binario.
 * Implementado con Array.
 * 
 * @author Santiago Rodr�guez Vallejo
 * @param <T> Tipo de objetos que almacenar� el mont�culo.
 * Debe ser comparable.
 */

public class BinaryHeap<T extends Comparable <? super T>> {
    //Capacidad por defecto del array.
    private static final int DEFAULT_CAPACITY = 10;

    //N�mero de elementos que tiene actualmente el mont�culo binario.
    private int currentSize;
    private T[] array;
    
    boolean minHeap = true;

    /**
     * Constructor de BinaryHeap.
     * @param heap Valor booleano. Si es verdadero, se maneja un minHeap.
     */
    public BinaryHeap(boolean heap){
        //Crea un mont�culo binario con la capacidad por defecto.
        this(DEFAULT_CAPACITY, heap);
    }

    /**
     * Constructor para crear un mont�culo binario con una capacidad definida.
     * 
     * @param capacity Tama�o del arreglo que representa al mont�culo binario.
     * @param heap Valor booleano. Si es verdadero, se maneja un minHeap.
     */
    public BinaryHeap(int capacity, boolean heap){
        currentSize = 0;
        minHeap = heap;
        array = (T[]) new Comparable[capacity + 1];
    }

    /**
     * M�todo que verifica si el mont�culo est� vac�o.
     * @return Valor booleano. Si es verdadero, el mont�culo binario est� vac�o.
     */
    public boolean isEmpty(){
        return currentSize == 0;
    }

    /**
     * M�todo que vac�a el mont�culo binario.
     * No realiza una eliminaci�n de sus elementos.
     * En cambio establece el tama�o a 0, para que sus valores se reescriban.
     */
    public void makeEmpty(){
        currentSize = 0;
    }

    /**
     * M�todo que inserta un elemento al mont�culo binario.
     * @param data Elemento a insertar.
     * @param heap Valor booleano. Si es verdadero, se maneja un minHeap.
     */
    public void insert(T data, boolean heap){
        /*
        * Si el arreglo est� lleno, se debe crear uno nuevo con el doble de capacidad.
        * Conservando los elementos actuales.
        */
        if (currentSize == array.length - 1) enlargeArray(array.length * 2);

        //Percolate up
        /*
        * Guarda el dato temporalmente.
        * Empieza a comparar el dato con los nodos desde el fondo del mont�culo.
        * Si el dato es menor que el que contiene el nodo a comparar,
        * significa que tiene mayor prioridad.
        * En este caso mueve el nodo con menor prioridad un nivel m�s abajo.
        * Una vez hecho esto se procede a comparar con un nodo en un nivel m�s arriba.
        * Al encontrar el nivel de prioridad adecuado, se procede a asignar el valor al nodo libre.
        */
        int hole = ++currentSize;
        for (array[0] = data; heap ? (data.compareTo(array[hole/2]) < 0) : (data.compareTo(array[hole/2]) > 0); hole /= 2) {
            array[hole] = array[hole/2];
        }
        array[hole] = data;
    }

    /**
     * M�todo que elimina el elemento con mayor prioridad (menor valor).
     * @param heap Valor booleano. Si es verdadero, se maneja un minHeap.
     * @return Valor del elemento.
     */
    public T deleteTop(boolean heap){
        if (this.isEmpty()) {
            System.out.println("Mont�culo Vac�o.");
            return null;
        }
        //Se obtiene el valor m�nimo
        T top = getTop();
        //Almacena temporalmente el �ltimo elemento guardado en el mont�culo en la cima.
        array[1] = array[currentSize--];
        
        //Percolate Down
        int hole = 1;
        int child;
        //�ltimo elemento guardado en el mont�culo.
        T tmp = array[hole];

        /*
         * El ciclo itera hasta el pen�ltimo nivel del mont�culo.
         * Es decir, cuando no hayan m�s hijos para comparar.
         * Toma como referencia el hijo izquierdo de un nodo en un nivel.
         * El viaje se realiza de arriba hacia abajo, buscando los hijos con menor valor.
         * El viaje se detiene cuando se llegue al fondo del mont�culo o cuando se encuentre
         * un nodo con un valor mayor que el almacenado en tmp.
         * En dicho punto se coloca el valor que almacena tmp.
         */
        for (; hole * 2 <= currentSize; hole = child){
            child = hole*2;
            if (child != currentSize && heap ? (array[child + 1].compareTo(array[child]) < 0) : (array[child + 1].compareTo(array[child]) > 0)) child++;
            if (array[child].compareTo(tmp) < 0) array[hole] = array[child];
            else break;
        }
        array[hole] = tmp;
        return top;
    }

    /**
     * M�todo que busca el nodo con el m�nimo o m�ximo valor en el mont�culo.
     * @return El valor m�nimo del mont�culo.
     */
    public T getTop(){
        if (this.isEmpty()) System.out.println("Mont�culo Vac�o.");
        return array[1];
    }

    /**
     * M�todo que alarga la capacidad del mont�culo.
     * @param newCapacity Nueva capacidad del mont�culo binario.
     */
    private void enlargeArray(int newCapacity){
        //Se almacena el arreglo actual.
        T [] old = array;
        //Se crea un nuevo arreglo con m�s capacidad.
        array = (T[]) new Comparable[newCapacity + 1];

        //Se copian los datos del viejo arreglo al nuevo.
        for (int i = 0; i < old.length; i++){
            array[i] = old[i];
        }
    }
    
    /**
     * M�todo que imprime el mont�culo binario en levelOrder.
     */
    public void print(){
        for (int i = 1; i < currentSize; i++){
            System.out.print(array[i] + " ");
        }
        System.out.println(array[currentSize]);
    }

    public int getCurrentSize(){
        return currentSize;
    }
}
