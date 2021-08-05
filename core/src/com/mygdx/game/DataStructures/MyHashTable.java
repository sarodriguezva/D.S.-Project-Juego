package com.mygdx.game.DataStructures;

/**
 * Tabla Hash que cumple con ser <String, T> donde T es un
 * dato de cualquier tipo.
 * 
 * @author Santiago
 * @param <T>
 */
public class MyHashTable<T> {
    private MyDoubleLinkedList<HashTuple<T>>[] listsArray;
    private static final int DEFAULT_ARRAY_CAPACITY = 101;
    private static int currentArrayCapacity;
    private int currentSize;
    
    public MyHashTable(){
        this(DEFAULT_ARRAY_CAPACITY);
    }
    
    public MyHashTable(int size){
        listsArray = new MyDoubleLinkedList[nextPrime(size)];
        for (int i = 0; i < listsArray.length; i++) 
            listsArray[i] = new MyDoubleLinkedList<>();
        currentArrayCapacity = size;
    }
    
    public void makeEmpty(){
        for (MyDoubleLinkedList list : listsArray){
            list.makeEmpty();
        }
        this.currentSize = 0;
    }
    
    private static int myhash(String k, int size){
        int hashVal = 0;
        
        for (int i = 0; i < k.length(); i++){
            hashVal *= 37;
            hashVal += k.charAt(i);
        }
        
        hashVal %= size;
        if (hashVal < 0) hashVal += size;
        
        return hashVal;
    }
    
    private void rehash(){
        MyDoubleLinkedList<HashTuple<T>>[] old = listsArray;
        
        listsArray = new MyDoubleLinkedList[nextPrime(2*listsArray.length)];
        for (int i = 0; i < listsArray.length; i++) 
            listsArray[i] = new MyDoubleLinkedList<>();
        currentSize = 0;
        currentArrayCapacity = listsArray.length;
        
        for (MyDoubleLinkedList<HashTuple<T>> list : old){
            Node<HashTuple<T>> top = list.getFirst();
            
            while (top != null){
                HashTuple<T> tuple = top.getData();
                String key = tuple.key;
                T value = tuple.value;
                this.insert(key, value);
                
                top = top.getNext();
            }
        }
            
    }
    
    public void insert(String key, T x){
        MyDoubleLinkedList<HashTuple<T>> destinationList;
        destinationList = listsArray[myhash(key, currentArrayCapacity)];
        
        Node<HashTuple<T>> top = destinationList.getFirst();
        
        boolean contains = false;
        while (top != null){
            if (top.getData().key.equals(key)){
                contains = true;
                break;
            }
            
            top = top.getNext();
        }
        
        if (!contains){
            HashTuple<T> tuple = new HashTuple<>(key, x);
            destinationList.add(tuple);
            if (++currentSize > currentArrayCapacity) rehash();
        }
    }
    
    public void remove(String key){
        MyDoubleLinkedList<HashTuple<T>> keyList;
        keyList = listsArray[myhash(key, currentArrayCapacity)];
        
        Node<HashTuple<T>> top = keyList.getFirst();
        int index = 0;
        boolean contains = false;
        while (top != null){
            if (top.getData().key.equals(key)){
                contains = true;
                break;
            }
            index++;
            top = top.getNext();
        }
        
        if (contains){
            keyList.delete(index);
            currentSize--;
        }
    }
    
    public boolean contains(String key){
        MyDoubleLinkedList<HashTuple<T>> keyList;
        keyList = listsArray[myhash(key, currentArrayCapacity)];
        
        Node<HashTuple<T>> top = keyList.getFirst();
        boolean contains = false;
        while (top != null){
            if (top.getData().key.equals(key)){
                contains = true;
                break;
            }
            
            top = top.getNext();
        }
        
        return contains;
    }
    
    
    public T getValue(String key){
        MyDoubleLinkedList<HashTuple<T>> keyList;
        keyList = listsArray[myhash(key, currentArrayCapacity)];
        
        Node<HashTuple<T>> top = keyList.getFirst();
        while (top != null){
            if (top.getData().key.equals(key)){
                return top.getData().value;
            }
            
            top = top.getNext();
        }
        
        System.out.println("La llave no existe.");
        return null;
    }
    
    public int getSize(){
        return currentSize;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        
        sb.append("{");
        for (MyDoubleLinkedList<HashTuple<T>> list : listsArray){
            Node<HashTuple<T>> top = list.getFirst();
            
            while (top != null){
                sb.append("\"");
                sb.append(top.getData().key);
                sb.append("\" : \"");
                String value = (String) top.getData().value;
                sb.append(value);
                sb.append("\",\n");
                
                top = top.getNext();
            }
        }
        String toReturn;
        toReturn = sb.substring(0, sb.length() -2);
        return toReturn + "\n}";
    }
    
    private static int nextPrime(int n){
        if (n%2 == 0) n++;
        
        while (!isPrime(n)) n+=2;
        return n;
    }
    
    private static boolean isPrime(int n){
        if (n == 2 || n == 3) return true;
        if (n == 1 || n%2 == 0) return false;
        
        int i = 3;
        
        while (i*i <= n) {
            if (n%i == 0) return false;
            i += 2;
        }
        return true;
    }
}

class HashTuple<V> {
    public String key;
    public V value;
    
    public HashTuple(String key, V value){
        this.key = key;
        this.value = value;
    }
}
