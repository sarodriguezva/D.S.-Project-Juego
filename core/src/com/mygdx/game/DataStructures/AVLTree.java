package com.mygdx.game.DataStructures;
/**
 * Clase �rbol de B�squeda Binario AVL.
 * Extiende de la clase BinaryTree del archivo TreeTraversal.java.
 * Los nodos que usa son creados a partir de la clase BinaryTreeNode
 * en TreeTraversal.java
 * 
 * @author Santiago Rodr�guez Vallejo
 * @param <T> Tipo de objetos que almacenar� el �rbol. 
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
     * Crea un �rbol AVL a partir de un arreglo.
     * 
     * @param array Arreglo a partir del cual se crea el �rbol AVL.
     */
    public AVLTree(T[] array){
        for (T object : array) {
            this.insert(object);
        }
    }
    
    /**
     * M�todo para vaciar el �rbol.
     */
    public void makeEmpty(){ this.root = null; }

    /**
     * M�todo para verificar si el �rbol est� vac�o.
     * 
     * @return Valor booleano. Si es verdadero, el �rbol est� vac�o.
     */
    public boolean isEmpty(){ return this.root == null; }

    /**
     * M�todo para insertar un nodo con un elemento al �rbol.
     * 
     * @param data Elemento a insertar.
     */
    public void insert(T data){  this.root = this.insert(data, this.root); }

    /**
     * M�todo que inserta un nodo con un elemento al �rbol
     * desde una ra�z dada.
     * 
     * @param data Elemento a insertar.
     * @param aux Nodo de referencia para insertar. (ra�z)
     * @return Nodo ra�z que representa el nuevo �rbol con el nuevo elemento insertado.
     */
    private BinaryTreeNode<T> insert(T data, BinaryTreeNode<T> aux){
        /*
        * Cuando se llegue a la posici�n deseada a insertar, aux ser� nulo, por lo que retornar� un nuevo
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
            System.out.println("No está permitida la inserción de valores repetidos.");
            return aux;
        }
        
        /*
         * Implementaci�n AVL
         */
        
        aux.height = this.height(aux);  //Actualizaci�n de altura.
        
        int balance = this.height(aux.leftSon) - this.height(aux.rightSon);
        /*
        * balance > 1 -> sobrecargado a la izquierda.
        * balance < -1 -> sobrecargado a la derecha.
        */

        if (balance > 1){
            int compareLeft = data.compareTo(aux.leftSon.data);
            /*
            * compareLeft = -1 -> data < aux.leftSon.data
            *   Caso Left-Left. Rotaci�n simple a la derecha.
            * compareLeft = 1 -> data > aux.leftSon.data
            *   Caso Left-Right. Rotaci�n doble izquierda-derecha.
            */

            if (compareLeft < 0) aux = rightRotation(aux);
            else aux = leftRightRotation(aux);
        } else if (balance < -1){
            int compareRight = data.compareTo(aux.rightSon.data);
            /*
            * compareRight = -1 -> data < aux.rightSon.data
            *   Caso Right-Left. Rotaci�n doble derecha-izquierda.
            * compareRight = 1 -> data > aux.rightSon.data
            *   Caso Right-Right. Rotaci�n simple a la izquierda.
            */

            if (compareRight > 0) aux = leftRotation(aux);
            else aux = rightLeftRotation(aux);
        }
        return aux;
    }

    /**
     * M�todo que verifica si el �rbol contiene un nodo con un elemento dado.
     * @param data Elemento a revisar si se encuentra en el �rbol.
     * @return Valor Booleano. Si es verdadero, el �rbol contiene el elemento.
     */
    public boolean contains(T data){ return contains(data, this.root); }

    /**
     * M�todo que realiza la b�squeda de un elemento en el �rbol.
     * @param data Elemento a buscar.
     * @param aux Nodo ra�z del cu�l parte la b�squeda.
     * @return Valor Booleano. Si es verdadero, el elemento fue encontrado en el �rbol.
     */
    private boolean contains(T data, BinaryTreeNode<T> aux){
        if (aux == null){
            /*
            * Si el �rbol est� vac�o o si lleg� al final del �rbol.
            * Retornar Falso.
            */
            return false;
        }

        int compareResult = data.compareTo(aux.data);
        /*
        * compareResult = -1 -> data < aux.data.
        *   Debe realizar la b�squeda por la izquierda.
        * compareResult = 0 -> data == aux.data
        *   Dato encontrado, retornar verdadero.
        * compareResult = 1 -> data > aux.data
        *   Debe realizar la b�squeda por la derecha.
        */

        if (compareResult < 0) return contains(data, aux.leftSon);
        else if (compareResult > 0) return contains(data, aux.rightSon);
        else return true;
    }

    /**
     * M�todo para encontrar el valor m�nimo del �rbol.
     * 
     * @return Elemento con valor m�nimo en el �rbol.
     */
    public T findMin(){ return findMin(this.root); }

    /**
     * M�todo que realiza la b�squeda del valor m�nimo en el �rbol.
     * 
     * @param aux Nodo ra�z del cual parte la b�squeda.
     * @return Elemento con valor m�nimo en el �rbol.
     */
    private T findMin(BinaryTreeNode<T> aux){
        if (aux == null){
            System.out.println("El árbol está vacío");
        }

        /*
        * El valor m�nimo se encuentra en el nodo "m�s a la izquierda".
        */
        if (aux.leftSon == null){
            return aux.data;
        }
        return findMin(aux.leftSon);
    }

    /**
     * Método para encontrar el valor m�ximo del �rbol.
     * 
     * @return Elemento con valor m�ximo en el �rbol.
     */
    public T findMax(){ return findMax(this.root); }

    /**
     * M�todo que realiza la b�squeda del valor m�ximo en el �rbol.
     * 
     * @param aux Nodo ra�z del cual parte la b�squeda.
     * @return Elemento con valor m�ximo en el �rbol.
     */
    private T findMax(BinaryTreeNode<T> aux){
        if (aux == null){
            System.out.println("El �rbol est� vac�o");
        }

        /*
        * El valor m�ximo se encuentra en el nodo "m�s a la derecha".
        */
        if (aux.rightSon == null){
            return aux.data;
        }
        return findMax(aux.rightSon);
    }

    /**
     * M�todo que elimina un nodo del �rbol con un elemento dado.
     * 
     * @param data Elemento que contiene el nodo a eliminar.
     */
    public void remove(T data){  this.root = this.remove(data, this.root); }

    /**
     * M�todo que elimina un nodo con un elemento del �rbol
     * desde una ra�z dada.
     * 
     * @param data Elemento a eliminar.
     * @param aux Nodo de referencia para eliminar. (ra�z)
     * @return Nodo ra�z que representa el nuevo �rbol con el nodo eliminado.
     */
    private BinaryTreeNode<T> remove(T data, BinaryTreeNode<T> aux){
        if (aux == null){
            //No se encontr� el dato.
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
        * Implementaci�n AVL.
        */

        if (aux == null){
            return aux;
        }
        
        aux.height = this.height(aux);  //Actualizaci�n de altura.
        
        int balance = this.height(aux.leftSon) - this.height(aux.rightSon);
        /*
        * balance > 1 -> sobrecargado a la izquierda.
        * balance < -1 -> sobrecargado a la derecha.
        */

        if (balance > 1){
            int balanceLeft = this.height(aux.leftSon.leftSon) - this.height(aux.leftSon.rightSon);
            /*
            * balanceLeft >= 0 -> Hijo izquierdo sobrecargado a la izquierda.
            *   Caso Left-Left. Rotaci�n simple a la derecha.
            * balanceLeft < 0 -> Hijo izquierdo sobrecargado a la derecha.
            *   Caso Left-Right. Rotaci�n doble izquierda-derecha.
            */

            if (balanceLeft >= 0) aux = rightRotation(aux);
            else aux = leftRightRotation(aux);
        } else if (balance < -1){
            int balanceRight = this.height(aux.rightSon.leftSon) - this.height(aux.rightSon.rightSon);
            /*
            * balanceRight > 0 -> Hijo derecho sobrecargado a la izquierda.
            *   Caso Right-Left. Rotaci�n doble derecha-izquierda.
            * balanceRight <= 0 -> Hijo derecho sobrecargado a la derecha.
            *   Caso Right-Right. Rotaci�n simple a la izquierda.
            */

            if (balanceRight <= 0) aux = leftRotation(aux);
            else aux = rightLeftRotation(aux);
        }
        return aux;
    }

    /**
     * M�todo que calcula la altura del �rbol.
     * 
     * @return Altura del �rbol.
     */
    public int height(){ return this.height(this.root); }

    /**
     * M�todo que calcula recursivamente la altura de un nodo.
     * 
     * @param aux Nodo al cual se le desea calcular su altura.
     * @return Altura del nodo.
     */
    private int height(BinaryTreeNode<T> aux){
        if (aux == null) return -1;
        
        return 1 + Math.max(this.height(aux.leftSon), this.height(aux.rightSon));
    }
    
    /*
    *IMPLEMENTACI�N ROTACIONES AVL
    */

    /**
     * M�todo que realiza una rotaci�n de nodos simple hacia la derecha.
     * 
     * @param aux Nodo ra�z a partir del cual se desea realizar la rotaci�n.
     * @return Nuevo nodo ra�z con la rotaci�n implementada.
     */
    private BinaryTreeNode<T> rightRotation(BinaryTreeNode<T> aux){
        BinaryTreeNode<T> tmp = aux.leftSon;
        aux.leftSon = tmp.rightSon;
        tmp.rightSon = aux;
        aux = tmp;
        
        return aux;
    }
    
    /**
     * M�todo que realiza una rotaci�n de nodos simple hacia la izquierda.
     * 
     * @param aux Nodo ra�z a partir del cual se desea realizar la rotaci�n.
     * @return Nuevo nodo ra�z con la rotaci�n implementada.
     */
    private BinaryTreeNode<T> leftRotation(BinaryTreeNode<T> aux){
        BinaryTreeNode<T> tmp = aux.rightSon;
        aux.rightSon = tmp.leftSon;
        tmp.leftSon = aux;
        aux = tmp;
        
        return aux;
    }
    
    /**
     * M�todo que realiza una rotaci�n de nodos doble.
     * Primero realiza una rotaci�n simple hacia la izquierda tomando el hijo izquierdo.
     * Luego realiza una rotaci�n simple hacia la derecha tomando la ra�z.
     * 
     * @param aux Nodo ra�z a partir del cual se desea realizar la rotaci�n.
     * @return Nuevo nodo ra�z con la rotaci�n implementada.
     */
    private BinaryTreeNode<T> leftRightRotation(BinaryTreeNode<T> aux){
        aux.leftSon = leftRotation(aux.leftSon);
        aux = rightRotation(aux);
        
        return aux;
    }
    
    /**
     * M�todo que realiza una rotaci�n de nodos doble.
     * Primero realiza una rotaci�n simple hacia la derecha tomando el hijo derecho.
     * Luego realiza una rotaci�n simple hacia la izquierda tomando la ra�z.
     * 
     * @param aux Nodo ra�z a partir del cual se desea realizar la rotaci�n.
     * @return Nuevo nodo ra�z con la rotaci�n implementada.
     */
    private BinaryTreeNode<T> rightLeftRotation(BinaryTreeNode<T> aux){
        aux.rightSon = rightRotation(aux.rightSon);
        aux = leftRotation(aux);
        
        return aux;
    }
}

/**
 * Clase persona que implementa la interfaz Comparable.
 * Usada como objeto de testeo del �rbol AVL.
 * 
 * @author Santiago Rodr�guez Vallejo
 */
class Persona implements Comparable<Persona>{
    //Su atributo de comparaci�n ser� la edad.
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