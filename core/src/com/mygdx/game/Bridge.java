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
 */
public class Bridge<T> extends MyDoubleLinkedList {
    
    Bridge (T[] array){
        super(array);
    }
    
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
