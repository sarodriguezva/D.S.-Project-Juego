package com.mygdx.game.DataStructures;
/*
class NthGradeTreeNode<T>{
    T data;
    NthGradeTreeNode<T>[] children;

    //Add Constructor

}
*/

/**
 * Clase Nodo Binario. Adecuado para soportar Árboles AVL.
 * 
 * @author Santiago Rodrí­guez Vallejo
 */
class BinaryTreeNode<T>{
    T data;
    BinaryTreeNode<T> leftSon;
    BinaryTreeNode<T> rightSon;
    int height;

    public BinaryTreeNode(T data){
        this.data = data;
        this.leftSon = null;
        this.rightSon = null;
    }
}

/**
 * Clase Árbol Binario.
 * Contiene los métodos de recorrido para un árbol.
 * 
 * @author Santiago Rodrí­guez Vallejo.
 */
class BinaryTree<T>{
    BinaryTreeNode<T> root;

    /**
     * Método que recorre el árbol usando preOrder.
     */
    public void preorder(){
        this.auxPreorder(this.root);
    }

    /**
     * Método que recorre el árbol
     * imprimiendo primero el contenido del nodo padre,
     * luego imprime el contenido del hijo izquierdo.
     * luego imprime el contenido del hijo derecho.
     * @param auxRoot Nodo raÃ­z a partir del cual se recorre el árbol.
     */
    private void auxPreorder(BinaryTreeNode<T> auxRoot){
        if (auxRoot != null){
            System.out.print(auxRoot.data + " ");
            this.auxPreorder(auxRoot.leftSon);
            this.auxPreorder(auxRoot.rightSon);
        }
    }

    /**
     * Método que recorre el árbol usando posOrder.
     */
    public void posorder(){
        this.auxPosorder(this.root);
    }

    /**
     * Método que recorre el árbol
     * imprimiendo primero el contenido de los nodos hijos
     * y luego imprime el contenido del nodo padre.
     * @param auxRoot Nodo raí­z a partir del cual se recorre el árbol.
     */
    private void auxPosorder(BinaryTreeNode<T> auxRoot){
        if (auxRoot != null){
            this.auxPosorder(auxRoot.leftSon);
            this.auxPosorder(auxRoot.rightSon);
            System.out.print(auxRoot.data + " ");
        }
    }

    /**
     * Método que recorre el árbol usando inOrder.
     */
    public void inorder(){
        this.auxInorder(this.root);
    }

    /**
     * Método que recorre el árbol
     * imprimiendo primero el contenido del hijo izquierdo,
     * luego imprime el contenido del nodo padre.
     * luego imprime el contenido del hijo derecho.
     * @param auxRoot Nodo raí­z a partir del cual se recorre el árbol.
     */
    private void auxInorder(BinaryTreeNode<T> auxRoot){
        if (auxRoot != null){
            this.auxInorder(auxRoot.leftSon);
            System.out.print(auxRoot.data + " ");
            this.auxInorder(auxRoot.rightSon);
        }
    }

    /**
     * Método que recorre el árbol usando levelOrder.
     */
    public void levelorder(){
        this.auxLevelorder(this.root);
    }

    /**
     * Método que recorre el árbol
     * imprimiendo todos los nodos en un mismo nivel a la vez.
     * @param auxRoot Nodo raíz a partir del cual se recorre el árbol.
     */
    private void auxLevelorder(BinaryTreeNode<T> auxRoot){
        MyQueue<BinaryTreeNode<T>> Q = new MyQueue<>();
        Q.enqueue(auxRoot);
        while (!Q.isEmpty()){
            BinaryTreeNode<T> aux = Q.dequeue();
            if (aux == null) continue;
            System.out.print(aux.data + " ");
            Q.enqueue(aux.leftSon);
            Q.enqueue(aux.rightSon);
        }
    }
}
