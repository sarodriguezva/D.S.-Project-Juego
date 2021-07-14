/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;
import com.mygdx.game.DataStructures.*;

/**
 * Clase Test.Se colocan m�todos adicionales para implementar en el proyecto.
 * 
 * @author Santiago
 * @param <T> Tipo de dato
 */
class Test<T extends Comparable <? super T>> {
    
    /**
     * M�todo que compara si dos �rboles son iguales.
     * @param original Primer �rbol
     * @param user Segundo �rbol
     * @return Valor booleano, si es verdadero ambos �rboles son iguales.
     */
    public boolean checkTrees(BinaryTree<T> original, BinaryTree<T> user ){
        //Se obtienen la representaci�n en arreglos de ambos �rboles.
        MyDoubleLinkedList<T> originalArray = original.toArray();
        MyDoubleLinkedList<T> userArray = user.toArray();
        
        //Si tienen distinto tama�o, no pueden ser iguales
        if (originalArray.getSize() != userArray.getSize()) return false;
        
        //Se comparan uno a uno sus elementos.
        int cnt = 0;
        while (originalArray.getData(cnt).equals(userArray.getData(cnt))){
            cnt++;
        }
        
        return true;
    }
}
