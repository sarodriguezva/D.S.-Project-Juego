package com.mygdx.game.DataStructures;

public class BinarySearchTree<T extends Comparable <? super T>> extends BinaryTree<T>{

    public void makeEmpty(){ this.root = null; }

    public boolean isEmpty(){ return this.root == null; }

    public void insert(T data){  this.root = this.insert(data, this.root); }

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
            System.out.println("No está permitida la inserción de valores repetidos.");
            return aux;
        }
        
        //Implementación AVL
        
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

    public boolean contains(T data){ return contains(data, this.root); }

    private boolean contains(T data, BinaryTreeNode<T> aux){
        if (aux == null){
            return false;
        }

        int compareResult = data.compareTo(aux.data);

        if (compareResult < 0) return contains(data, aux.leftSon);
        else if (compareResult > 0) return contains(data, aux.rightSon);
        else return true;
    }

    /*
    public T findMin(){
        if (isEmpty()){
            System.out.println("El árbol está vací­o.");
            return null;
        }

        BinaryTreeNode<T> aux = this.root;
        while(aux.leftSon != null){
            aux = aux.leftSon;
        }

        return aux.data;
    }

    public T findMax(){
        if (isEmpty()){
            System.out.println("El árbol está vací­o.");
            return null;
        }

        BinaryTreeNode<T> aux = this.root;
        while(aux.rightSon != null){
            aux = aux.rightSon;
        }

        return aux.data;
    }
    */

    public T findMin(){ return findMin(this.root); }

    private T findMin(BinaryTreeNode<T> aux){
        if (aux == null){
            System.out.println("El árbol está vací­o");
        }

        if (aux.leftSon == null){
            return aux.data;
        }
        return findMin(aux.leftSon);
    }

    public T findMax(){ return findMax(this.root); }

    private T findMax(BinaryTreeNode<T> aux){
        if (aux == null){
            System.out.println("El árbol está vací­o");
        }

        if (aux.rightSon == null){
            return aux.data;
        }
        return findMax(aux.rightSon);
    }

    public void remove(T data){  this.root = this.remove(data, this.root); }

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
        
        //Implementación AVL.
        
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

    public int height(){ return this.height(this.root); }

    private int height(BinaryTreeNode<T> aux){
        if (aux == null) return -1;
        
        return 1 + Math.max(this.height(aux.leftSon), this.height(aux.rightSon));
    }
    
    //IMPLEMENTACIÓN ROTACIONES AVL
    
    private BinaryTreeNode<T> rightRotation(BinaryTreeNode<T> aux){
        BinaryTreeNode<T> tmp = aux.leftSon;
        aux.leftSon = tmp.rightSon;
        tmp.rightSon = aux;
        aux = tmp;
        
        return aux;
    }
    
    private BinaryTreeNode<T> leftRotation(BinaryTreeNode<T> aux){
        BinaryTreeNode<T> tmp = aux.rightSon;
        aux.rightSon = tmp.leftSon;
        tmp.leftSon = aux;
        aux = tmp;
        
        return aux;
    }
    
    private BinaryTreeNode<T> leftRightRotation(BinaryTreeNode<T> aux){
        aux.leftSon = leftRotation(aux.leftSon);
        aux = rightRotation(aux);
        
        return aux;
    }
    
    private BinaryTreeNode<T> rightLeftRotation(BinaryTreeNode<T> aux){
        aux.rightSon = rightRotation(aux.rightSon);
        aux = leftRotation(aux);
        
        return aux;
    }
}

class Persona implements Comparable<Persona>{
    int age;

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