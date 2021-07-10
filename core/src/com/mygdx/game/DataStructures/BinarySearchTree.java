public class BinarySearchTree<T extends Comparable <? super T>> extends BinaryTree<T>{

    public void makeEmpty(){ this.root = null; }

    public boolean isEmpty(){ return this.root == null; }

    public void insert(T data){ 
        this.root = this.insert(data, this.root);
        if (Math.abs(this.height(this.root.leftSon) - this.height(this.root.rightSon)) > 1) {
            //Balancear el árbol (AVL)
            this.balancear();   //O(1)
        }
    }

    private void balancear(){
        //COMPLETAR Y TOMAR DECISIONES.
    }

    private void leftRotation(BinaryTreeNode<T> aux){
        BinaryTreeNode<T> tmp;
        tmp = aux.leftSon;
        aux.leftSon = tmp.rightSon;
        tmp.rightSon = aux;
    }

    private void rightRotation(BinaryTreeNode<T> aux){
        BinaryTreeNode<T> tmp;
        tmp = aux.rightSon;
        aux.rightSon = tmp.leftSon;
        tmp.leftSon = aux;
    }

    private void LR_Rotation(BinaryTreeNode<T> aux){
        this.rightRotation(aux.leftSon);
        this.leftRotation(aux);
    }

    private void RL_Rotation(BinaryTreeNode<T> aux){
        this.leftRotation(aux.rightSon);
        this.rightRotation(aux);
    }

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
        else System.out.println("No está permitida la inserción de valores repetidos.");
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
            System.out.println("El árbol está vacío.");
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
            System.out.println("El árbol está vacío.");
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
            System.out.println("El árbol está vacío");
        }

        if (aux.leftSon == null){
            return aux.data;
        }
        return findMin(aux.leftSon);
    }

    public T findMax(){ return findMax(this.root); }

    private T findMax(BinaryTreeNode<T> aux){
        if (aux == null){
            System.out.println("El árbol está vacío");
        }

        if (aux.rightSon == null){
            return aux.data;
        }
        return findMax(aux.rightSon);
    }

    public void remove(T data){ 
        this.root = this.remove(data, this.root);
        
        if (Math.abs(this.height(this.root.leftSon) - this.height(this.root.rightSon)) > 1) {
            //Balancear el árbol (AVL)
            this.balancear();
        }
    }

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

        return aux;
    }

    public int height(){ return this.height(this.root); }

    private int height(BinaryTreeNode<T> aux){
        if (aux == null) return -1;

        return 1 + Math.max(this.height(aux.leftSon), this.height(aux.rightSon));
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
}