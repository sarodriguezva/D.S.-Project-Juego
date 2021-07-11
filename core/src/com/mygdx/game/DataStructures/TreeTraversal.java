package com.mygdx.game.DataStructures;

/*
class NthGradeTreeNode<T>{
    T data;
    NthGradeTreeNode<T>[] children;

    //Add Constructor

}
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

class BinaryTree<T>{
    BinaryTreeNode<T> root;
    //Atributos opcionales.

    void preorder(){
        this.auxPreorder(this.root);
    }

    private void auxPreorder(BinaryTreeNode<T> auxRoot){
        if (auxRoot != null){
            System.out.print(auxRoot.data + " ");
            this.auxPreorder(auxRoot.leftSon);
            this.auxPreorder(auxRoot.rightSon);
        }
    }

    public void posorder(){
        this.auxPosorder(this.root);
    }

    private void auxPosorder(BinaryTreeNode<T> auxRoot){
        if (auxRoot != null){
            this.auxPosorder(auxRoot.leftSon);
            this.auxPosorder(auxRoot.rightSon);
            System.out.print(auxRoot.data + " ");
        }
    }

    public void inorder(){
        this.auxInorder(this.root);
    }

    private void auxInorder(BinaryTreeNode<T> auxRoot){
        if (auxRoot != null){
            this.auxInorder(auxRoot.leftSon);
            System.out.print(auxRoot.data + " ");
            this.auxInorder(auxRoot.rightSon);
        }
    }

    public void levelorder(){
        this.auxLevelorder(this.root);
    }

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
