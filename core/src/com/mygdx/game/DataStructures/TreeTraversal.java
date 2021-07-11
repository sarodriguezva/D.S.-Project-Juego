/*
class NthGradeTreeNode<T>{
    T data;
    NthGradeTreeNode<T>[] children;

    //Add Constructor

}
*/

package com.mygdx.game.DataStructures;

class BinaryTreeNode<T>{
    T data;
    int height;
    BinaryTreeNode<T> leftSon;
    BinaryTreeNode<T> rightSon;

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

public class TreeTraversal {
    public static void main(String[] args){
        BinaryTree<String> stringTree = new BinaryTree<>();

        //Creación machete del árbol.
        stringTree.root = new BinaryTreeNode<>("A");
        stringTree.root.leftSon = new BinaryTreeNode<>("B");
        stringTree.root.rightSon = new BinaryTreeNode<>("C");
        stringTree.root.leftSon.leftSon = new BinaryTreeNode<>("D");
        stringTree.root.leftSon.rightSon = new BinaryTreeNode<>("E");
        stringTree.root.rightSon.leftSon = new BinaryTreeNode<>("F");
        stringTree.root.rightSon.rightSon = new BinaryTreeNode<>("G");
        stringTree.root.leftSon.rightSon.leftSon = new BinaryTreeNode<>("H");
        stringTree.root.rightSon.leftSon.rightSon = new BinaryTreeNode<>("I");
        //Termina la creación machete del árbol.
        System.out.print("Preorder: ");
        stringTree.preorder();
        System.out.println();
        System.out.print("Posorder: ");
        stringTree.posorder();
        System.out.println();
        System.out.print("Posorder: ");
        stringTree.inorder();
        System.out.println();
        System.out.print("Level order: ");
        stringTree.levelorder();
    }
}
