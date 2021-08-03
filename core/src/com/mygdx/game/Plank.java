package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
/**
 * Clase tabla que puede ser utilizada en un nivel de tematica de listas. Es interactuable y arrastrable con el mouse
 * @author 3200g
 */
public class Plank extends ApplicationAdapter {
    public Texture plankTexture;
    public Rectangle plankCollision;
    public Integer plankNumber;
    /**
     * Esta funcion crea una tabla que puede ser utilizada en los niveles de listas, con posicion tamanio y numero entero
     * @param x posicion en el eje x
     * @param y posicion en el eje y
     * @param w tamanio en anchura
     * @param h tamanio en altura
     * @param num numero que contiene la hoja
     */
    public Plank(int x, int y, int w, int h, int num){
        plankCollision = new Rectangle();
        plankCollision.x = x;
        plankCollision.y = y;
        plankCollision.width = w;
        plankCollision.height = h;
        plankNumber=num;
        plankTexture = new Texture(Gdx.files.internal("Tabla1.png"));
    }
}


