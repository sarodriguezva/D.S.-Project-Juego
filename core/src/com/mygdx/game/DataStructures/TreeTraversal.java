package com.mygdx.game.DataStructures;
/*
class NthGradeTreeNode<T>{
    T data;
    NthGradeTreeNode<T>[] children;

    //Add Constructor

}
*/

/**
 * Clase Nodo Binario. Adecuado para soportar �rboles AVL.
 * 
 * @author Santiago Rodr�guez Vallejo
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
 * Clase �rbol Binario.
 * Contiene los m�todos de recorrido para un �rbol.
 * 
 * @author Santiago Rodr�guez Vallejo.
 */
class BinaryTree<T>{
    BinaryTreeNode<T> root;

    /**
     * M�todo que recorre el �rbol usando preOrder.
     */
    public void preorder(){
        this.auxPreorder(this.root);
    }

    /**
     * M�todo que recorre el �rbol
     * imprimiendo primero el contenido del nodo padre,
     * luego imprime el contenido del hijo izquierdo.
     * luego imprime el contenido del hijo derecho.
     * @param auxRoot Nodo raíz a partir del cual se recorre el �rbol.
     */
    private void auxPreorder(BinaryTreeNode<T> auxRoot){
        if (auxRoot != null){
            System.out.print(auxRoot.data + " ");
            this.auxPreorder(auxRoot.leftSon);
            this.auxPreorder(auxRoot.rightSon);
        }
    }

    /**
     * M�todo que recorre el �rbol usando posOrder.
     */
    public void posorder(){
        this.auxPosorder(this.root);
    }

    /**
     * M�todo que recorre el �rbol
     * imprimiendo primero el contenido de los nodos hijos
     * y luego imprime el contenido del nodo padre.
     * @param auxRoot Nodo ra�z a partir del cual se recorre el �rbol.
     */
    private void auxPosorder(BinaryTreeNode<T> auxRoot){
        if (auxRoot != null){
            this.auxPosorder(auxRoot.leftSon);
            this.auxPosorder(auxRoot.rightSon);
            System.out.print(auxRoot.data + " ");
        }
    }

    /**
     * M�todo que recorre el �rbol usando inOrder.
     */
    public void inorder(){
        this.auxInorder(this.root);
    }

    /**
     * M�todo que recorre el �rbol
     * imprimiendo primero el contenido del hijo izquierdo,
     * luego imprime el contenido del nodo padre.
     * luego imprime el contenido del hijo derecho.
     * @param auxRoot Nodo ra�z a partir del cual se recorre el �rbol.
     */
    private void auxInorder(BinaryTreeNode<T> auxRoot){
        if (auxRoot != null){
            this.auxInorder(auxRoot.leftSon);
            System.out.print(auxRoot.data + " ");
            this.auxInorder(auxRoot.rightSon);
        }
    }

    /**
     * M�todo que recorre el �rbol usando levelOrder.
     */
    public void levelorder(){
        this.auxLevelorder(this.root);
    }

    /**
     * M�todo que recorre el �rbol
     * imprimiendo todos los nodos en un mismo nivel a la vez.
     * @param auxRoot Nodo ra�z a partir del cual se recorre el �rbol.
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
