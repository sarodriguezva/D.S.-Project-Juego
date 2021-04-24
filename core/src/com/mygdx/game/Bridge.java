/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;
import com.mygdx.game.DataStructures.*;

/**
 * Clase Puente.
 * Funciona como una lista doblemente encadenada.
 * 
 * @author Santiago
 * @param <T> Tipo de elementos que guardan los nodos (tablas) del puente.
 */
public class Bridge<T> extends MyDoubleLinkedList {
    /**
     * Constructor para crear el puente a partir
     * de un arreglo.
     * 
     * @param array Arreglo que contiene el orden en el que el usuario debe
     * acomodar las tablas.
     */
    Bridge (T[] array){
        super(array);
    }
    
    /**
     * M�todo que verifica si la lista enviada por el usuario
     * a trav�s del ca��n concuerda con el orden requerido del puente.
     * 
     * @param userList Lista que env�a el usuario.
     * @return Valor booleano. Si es falso, la lista enviada por el usuario
     * no concuerda con la lista requerida por el puente.
     */
    public boolean commit(MyDoubleLinkedList<T> userList){
        Node<T> userListPointer = userList.getFirst();
        Node<T> bridgePointer = this.getFirst();
        
        if (userList.getSize() != this.getSize()) return false;
        
        while(userListPointer != null){
            if(userListPointer.getData() != bridgePointer.getData()){
                return false;
            }
            userListPointer = userListPointer.getNext();
            bridgePointer = bridgePointer.getNext();
        }
        return true;
    }
    
}
