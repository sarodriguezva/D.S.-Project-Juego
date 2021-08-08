package com.mygdx.game.DataStructures;

public class MyTuple<V,E> implements Comparable <MyTuple> {
    public V key;
    public E value;
    public MyTuple(V key, E value){
            this.key = key;
            this.value = value;
        }
    @Override
    public int compareTo(MyTuple o) {
        if ( Integer.valueOf(String.valueOf(this.value)) > Integer.valueOf(String.valueOf(o.value)) ) return 1;
        if ( Integer.valueOf(String.valueOf(this.value)) < Integer.valueOf(String.valueOf(o.value)) ) return -1;
        else return 0;
    }
}
