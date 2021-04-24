/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.DataStructures;

/**
 *
 * @author JUANS
 * @param <E>
 */

public class MyDynamicArray<E> {

    Object[] arr;
    int size;
    int capacity;

    public MyDynamicArray() {
        this.arr = new Object[2];
        this.size = 0;
        this.capacity = 2;

    }

    public void pushBack(E x) {
        if (size == capacity) {

            Object aux[] = new Object[capacity * 2];

            System.arraycopy(arr, 0, aux, 0, capacity);
            arr = aux;
            capacity *= 2;
        }
        arr[size++] = x;

    }

    public void remove(int index) {

    }

    // void get, set, size, capacity
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(); // El string builder deja concatenar mas rapido en vez del string con un +.
        sb.append("[");
        if (size == 0) {
            return "[]";
        }
        for (int i = 0; i < size; i++) {
            sb.append(arr[i]);
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        String toReturn = sb.toString();
        return toReturn;
    }

    /// esto es solo por comodidad, como tal seria o(n) de toString
    public void printArray() {
        System.out.println(toString());
    }
}