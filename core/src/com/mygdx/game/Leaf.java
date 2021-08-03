package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
/**
 * Clase hoja que puede ser utilizada en niveles de arboles, puede ser lanzada con un click y comparable con otras hojas.
 * @author 3200g
 */
public class Leaf extends ApplicationAdapter implements Comparable<Leaf>{
    public Texture leafTexture;
    public Rectangle leafCollision;
    public Integer leafNumber;
    /**
     * Esta funcion crea una hoja que puede ser utilizada en los niveles de arboles, con posicion tamanio y numero entero
     * @param x posicion en el eje x
     * @param y posicion en el eje y
     * @param w tamanio en anchura
     * @param h tamanio en altura
     * @param num numero que contiene la hoja
     */
    public Leaf(int x, int y, int w, int h, int num){
        leafCollision = new Rectangle();
        leafCollision.x = x;
        leafCollision.y = y;
        leafCollision.width = w;
        leafCollision.height = h;
        leafNumber=num;
        leafTexture = new Texture(Gdx.files.internal("Nodo.png"));
    }
    /**
     * Esta funcion compara la hoja con otra hoja tomando en cuenta el numero que contiene cada una de ellas
     * @param otherElement
     * @return -1 si es menor, 0 si son iguales, 1 si es mayor
     */
    @Override
        public int compareTo(Leaf otherElement){
        if (this.leafNumber < otherElement.leafNumber) return -1;
        else if (this.leafNumber == otherElement.leafNumber) return 0;
        else return 1;
    }
}


