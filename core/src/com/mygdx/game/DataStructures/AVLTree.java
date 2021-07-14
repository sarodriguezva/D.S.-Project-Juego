package com.mygdx.game.DataStructures;
/**
 * Clase Árbol de Búsqueda Binario AVL.
 * Extiende de la clase BinaryTree del archivo TreeTraversal.java.
 * Los nodos que usa son creados a partir de la clase BinaryTreeNode
 * en TreeTraversal.java
 * 
 * @author Santiago Rodríguez Vallejo
 * @param <T> Tipo de objetos que almacenará el árbol. 
 * Debe ser comparable.
 */

public class AVLTree<T extends Comparable <? super T>> extends BinaryTree<T>{

    /**
     * Constructor de AVLTree.
     */
    public AVLTree(){
        
    }
    
    /**
     * Constructor de AVLTree.
     * Crea un árbol AVL a partir de un arreglo.
     * 
     * @param array Arreglo a partir del cual se crea el árbol AVL.
     */
    public AVLTree(T[] array){
        for (T object : array) {
            this.insert(object);
        }
    }
    
    /**
     * Método para vaciar el árbol.
     */
    public void makeEmpty(){ this.root = null; }

    /**
     * Método para verificar si el árbol está vací­o.
     * 
     * @return Valor booleano. Si es verdadero, el árbol está vací­o.
     */
    public boolean isEmpty(){ return this.root == null; }

    /**
     * Método para insertar un nodo con un elemento al árbol.
     * 
     * @param data Elemento a insertar.
     */
    public void insert(T data){  this.root = this.insert(data, this.root); }

    /**
     * Método que inserta un nodo con un elemento al árbol
     * desde una raíz dada.
     * 
     * @param data Elemento a insertar.
     * @param aux Nodo de referencia para insertar. (raíz)
     * @return Nodo raíz que representa el nuevo árbol con el nuevo elemento insertado.
     */
    private BinaryTreeNode<T> insert(T data, BinaryTreeNode<T> aux){
        /*
        * Cuando se llegue a la posición deseada a insertar, aux será nulo, por lo que retornará un nuevo
        * nodo con el dato.
        */
        if (aux == null){
            return new BinaryTreeNode<>(data);
        }
        /*
        * compareTo se usa porque no se garantiza que T sea entero siempre.
        * T no puede ser cualquier dato, debe ser uno que yo pueda comparar.
        * Por ello se pone que la clase sea <T extends Comparable <? super T>>
        */
        int compareResult = data.compareTo(aux.data);
        /*
        * compareResult = -1 -> data < aux.data
        * compareResult = 0 -> data == aux.data
        * compareResult = 1 -> data > aux.data
        */
        if(compareResult < 0) aux.leftSon = insert(data, aux.leftSon);
        else if(compareResult > 0) aux.rightSon = insert(data, aux.rightSon);
        else {
            System.out.println("No estÃ¡ permitida la inserciÃ³n de valores repetidos.");
            return aux;
        }
        
        /*
         * Implementación AVL
         */
        
        aux.height = this.height(aux);  //Actualización de altura.
        
        int balance = this.height(aux.leftSon) - this.height(aux.rightSon);
        /*
        * balance > 1 -> sobrecargado a la izquierda.
        * balance < -1 -> sobrecargado a la derecha.
        */

        if (balance > 1){
            int compareLeft = data.compareTo(aux.leftSon.data);
            /*
            * compareLeft = -1 -> data < aux.leftSon.data
            *   Caso Left-Left. Rotación simple a la derecha.
            * compareLeft = 1 -> data > aux.leftSon.data
            *   Caso Left-Right. Rotación doble izquierda-derecha.
            */

            if (compareLeft < 0) aux = rightRotation(aux);
            else aux = leftRightRotation(aux);
        } else if (balance < -1){
            int compareRight = data.compareTo(aux.rightSon.data);
            /*
            * compareRight = -1 -> data < aux.rightSon.data
            *   Caso Right-Left. Rotación doble derecha-izquierda.
            * compareRight = 1 -> data > aux.rightSon.data
            *   Caso Right-Right. Rotación simple a la izquierda.
            */

            if (compareRight > 0) aux = leftRotation(aux);
            else aux = rightLeftRotation(aux);
        }
        return aux;
    }

    /**
     * Método que verifica si el árbol contiene un nodo con un elemento dado.
     * @param data Elemento a revisar si se encuentra en el árbol.
     * @return Valor Booleano. Si es verdadero, el árbol contiene el elemento.
     */
    public boolean contains(T data){ return contains(data, this.root); }

    /**
     * Método que realiza la búsqueda de un elemento en el árbol.
     * @param data Elemento a buscar.
     * @param aux Nodo raíz del cuál parte la búsqueda.
     * @return Valor Booleano. Si es verdadero, el elemento fue encontrado en el árbol.
     */
    private boolean contains(T data, BinaryTreeNode<T> aux){
        if (aux == null){
            /*
            * Si el árbol está vací­o o si llegó al final del árbol.
            * Retornar Falso.
            */
            return false;
        }

        int compareResult = data.compareTo(aux.data);
        /*
        * compareResult = -1 -> data < aux.data.
        *   Debe realizar la búsqueda por la izquierda.
        * compareResult = 0 -> data == aux.data
        *   Dato encontrado, retornar verdadero.
        * compareResult = 1 -> data > aux.data
        *   Debe realizar la búsqueda por la derecha.
        */

        if (compareResult < 0) return contains(data, aux.leftSon);
        else if (compareResult > 0) return contains(data, aux.rightSon);
        else return true;
    }

    /**
     * Método para encontrar el valor mínimo del árbol.
     * 
     * @return Elemento con valor mínimo en el árbol.
     */
    public T findMin(){ return findMin(this.root); }

    /**
     * Método que realiza la búsqueda del valor mínimo en el árbol.
     * 
     * @param aux Nodo raíz del cual parte la búsqueda.
     * @return Elemento con valor mí­nimo en el árbol.
     */
    private T findMin(BinaryTreeNode<T> aux){
        if (aux == null){
            System.out.println("El Ã¡rbol estÃ¡ vacÃ­o");
        }

        /*
        * El valor mínimo se encuentra en el nodo "más a la izquierda".
        */
        if (aux.leftSon == null){
            return aux.data;
        }
        return findMin(aux.leftSon);
    }

    /**
     * MÃ©todo para encontrar el valor máximo del árbol.
     * 
     * @return Elemento con valor máximo en el árbol.
     */
    public T findMax(){ return findMax(this.root); }

    /**
     * Método que realiza la búsqueda del valor máximo en el árbol.
     * 
     * @param aux Nodo raíz del cual parte la búsqueda.
     * @return Elemento con valor máximo en el árbol.
     */
    private T findMax(BinaryTreeNode<T> aux){
        if (aux == null){
            System.out.println("El árbol está vací­o");
        }

        /*
        * El valor má­ximo se encuentra en el nodo "más a la derecha".
        */
        if (aux.rightSon == null){
            return aux.data;
        }
        return findMax(aux.rightSon);
    }

    /**
     * Método que elimina un nodo del árbol con un elemento dado.
     * 
     * @param data Elemento que contiene el nodo a eliminar.
     */
    public void remove(T data){  this.root = this.remove(data, this.root); }

    /**
     * Método que elimina un nodo con un elemento del árbol
     * desde una raíz dada.
     * 
     * @param data Elemento a eliminar.
     * @param aux Nodo de referencia para eliminar. (raíz)
     * @return Nodo raíz que representa el nuevo árbol con el nodo eliminado.
     */
    private BinaryTreeNode<T> remove(T data, BinaryTreeNode<T> aux){
        if (aux == null){
            //No se encontró el dato.
            return null;
        }

        int compareResult = data.compareTo(aux.data);

        if (compareResult < 0) aux.leftSon = this.remove(data, aux.leftSon);
        else if (compareResult > 0) aux.rightSon = this.remove(data, aux.rightSon);
        else if (aux.leftSon != null && aux.rightSon != null){
            //Estoy en un nodo con 2 hijos y lo quiero eliminar.
            //Busco el sucesor del nodo y lo reemplazo.
            T sucesor = this.findMin(aux.rightSon);
            aux.data = sucesor;

            //Elimino el nodo que contiene al sucesor.
            aux.rightSon = this.remove(sucesor, aux.rightSon);
        } else {
            //Si mi nodo tiene uno o cero hijos.
            aux = (aux.leftSon != null) ? aux.leftSon : aux.rightSon;
        }
        
        /*
        * Implementación AVL.
        */

        if (aux == null){
            return aux;
        }
        
        aux.height = this.height(aux);  //Actualización de altura.
        
        int balance = this.height(aux.leftSon) - this.height(aux.rightSon);
        /*
        * balance > 1 -> sobrecargado a la izquierda.
        * balance < -1 -> sobrecargado a la derecha.
        */

        if (balance > 1){
            int balanceLeft = this.height(aux.leftSon.leftSon) - this.height(aux.leftSon.rightSon);
            /*
            * balanceLeft >= 0 -> Hijo izquierdo sobrecargado a la izquierda.
            *   Caso Left-Left. Rotación simple a la derecha.
            * balanceLeft < 0 -> Hijo izquierdo sobrecargado a la derecha.
            *   Caso Left-Right. Rotación doble izquierda-derecha.
            */

            if (balanceLeft >= 0) aux = rightRotation(aux);
            else aux = leftRightRotation(aux);
        } else if (balance < -1){
            int balanceRight = this.height(aux.rightSon.leftSon) - this.height(aux.rightSon.rightSon);
            /*
            * balanceRight > 0 -> Hijo derecho sobrecargado a la izquierda.
            *   Caso Right-Left. Rotación doble derecha-izquierda.
            * balanceRight <= 0 -> Hijo derecho sobrecargado a la derecha.
            *   Caso Right-Right. Rotación simple a la izquierda.
            */

            if (balanceRight <= 0) aux = leftRotation(aux);
            else aux = rightLeftRotation(aux);
        }
        return aux;
    }

    /**
     * Método que calcula la altura del árbol.
     * 
     * @return Altura del árbol.
     */
    public int height(){ return this.height(this.root); }

    /**
     * Método que calcula recursivamente la altura de un nodo.
     * 
     * @param aux Nodo al cual se le desea calcular su altura.
     * @return Altura del nodo.
     */
    private int height(BinaryTreeNode<T> aux){
        if (aux == null) return -1;
        
        return 1 + Math.max(this.height(aux.leftSon), this.height(aux.rightSon));
    }
    
    /*
    *IMPLEMENTACIÓN ROTACIONES AVL
    */

    /**
     * Método que realiza una rotación de nodos simple hacia la derecha.
     * 
     * @param aux Nodo raíz a partir del cual se desea realizar la rotación.
     * @return Nuevo nodo raíz con la rotación implementada.
     */
    private BinaryTreeNode<T> rightRotation(BinaryTreeNode<T> aux){
        BinaryTreeNode<T> tmp = aux.leftSon;
        aux.leftSon = tmp.rightSon;
        tmp.rightSon = aux;
        aux = tmp;
        
        return aux;
    }
    
    /**
     * Método que realiza una rotación de nodos simple hacia la izquierda.
     * 
     * @param aux Nodo raíz a partir del cual se desea realizar la rotación.
     * @return Nuevo nodo raíz con la rotación implementada.
     */
    private BinaryTreeNode<T> leftRotation(BinaryTreeNode<T> aux){
        BinaryTreeNode<T> tmp = aux.rightSon;
        aux.rightSon = tmp.leftSon;
        tmp.leftSon = aux;
        aux = tmp;
        
        return aux;
    }
    
    /**
     * Método que realiza una rotación de nodos doble.
     * Primero realiza una rotación simple hacia la izquierda tomando el hijo izquierdo.
     * Luego realiza una rotación simple hacia la derecha tomando la raíz.
     * 
     * @param aux Nodo raíz a partir del cual se desea realizar la rotación.
     * @return Nuevo nodo raíz con la rotación implementada.
     */
    private BinaryTreeNode<T> leftRightRotation(BinaryTreeNode<T> aux){
        aux.leftSon = leftRotation(aux.leftSon);
        aux = rightRotation(aux);
        
        return aux;
    }
    
    /**
     * Método que realiza una rotación de nodos doble.
     * Primero realiza una rotación simple hacia la derecha tomando el hijo derecho.
     * Luego realiza una rotación simple hacia la izquierda tomando la raíz.
     * 
     * @param aux Nodo raíz a partir del cual se desea realizar la rotación.
     * @return Nuevo nodo raíz con la rotación implementada.
     */
    private BinaryTreeNode<T> rightLeftRotation(BinaryTreeNode<T> aux){
        aux.rightSon = rightRotation(aux.rightSon);
        aux = leftRotation(aux);
        
        return aux;
    }
}

/**
 * Clase persona que implementa la interfaz Comparable.
 * Usada como objeto de testeo del árbol AVL.
 * 
 * @author Santiago Rodrí­guez Vallejo
 */
class Persona implements Comparable<Persona>{
    //Su atributo de comparación será la edad.
    int age;

    /**
     * Constructor de Persona.
     * @param age Edad de la persona.
     */
    public Persona(int age){
        this.age = age;
    }
    
    @Override
    public int compareTo(Persona otherElement){
        if (this.age < otherElement.age) return -1;
        else if (this.age == otherElement.age) return 0;
        else return 1;
    }

    @Override
    public String toString(){
        return String.valueOf(this.age);
    }
}