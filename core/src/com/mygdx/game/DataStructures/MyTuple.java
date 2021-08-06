package com.mygdx.game.DataStructures;

import com.mygdx.game.Leaf;

public class MyTuple<V,E> implements Comparable <MyTuple> {
    public V key;
    public E value;

    public MyTuple(V key, E value){
            this.key = key;
            this.value = value;
        }
    @Override
    public int compareTo(MyTuple o) {
        if (this.value.equals(o.value)) return 0;
        if (this.value.toString().compareTo(o.value.toString()) >1 ) return 1;
        else return -1;
    }
}
