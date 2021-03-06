/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Clase que permite crear un canion con tamanio y posicion, puede disparar.
 * @author judir
 */
public class Canon extends ApplicationAdapter {
    public Texture cannonTexture;
    public Rectangle cannonCollision;
    public Integer cannonNumber;
    /**
     * 
     * @param x posicion en el eje x
     * @param y posicion en el eje y
     * @param w tamanio en anchura
     * @param h tamanio en altura
     * @param pathTexture cadena de texto que contiene la URL local en assets en la cual esta almacenada la textura
     */
    public Canon(int x, int y, int w, int h, String pathTexture ){
        cannonCollision = new Rectangle();
        cannonCollision.x = x;
        cannonCollision.y = y;
        cannonCollision.width = w;
        cannonCollision.height = h;
        cannonTexture = new Texture(Gdx.files.internal(pathTexture));
    }
    /**
     * Funcion que dispara la tabla de forma parabolica hacia algun objetivo.
     * @param objX posicion en el eje x a la cual se disparara la tabla
     * @param objY posicion en el eje y a la cual se disparara la tabla
     * @param initx posicion inicial en el eje x desde la cual se dispara la tabla
     * @param inity posicion inicial en el eje y desde la cual se dispara la tabla
     * @param Shoottime tiempo que ha pasado desde que se disparo para calcular la velocidad
     * @param proyectile tabla que se va a disparar
     */
    public static void ShootPlankto(int objX, int objY, float initx, float inity, float Shoottime, Plank proyectile){
        float velx,vely,accel;
        velx = (objX - (initx+30))/0.9f;
        vely = ((objY+10)-inity)/0.9f + 120f;
        accel = 129f;
        vely -= accel * Shoottime;
        proyectile.plankCollision.x += velx * Gdx.graphics.getDeltaTime();
        proyectile.plankCollision.y += vely * Gdx.graphics.getDeltaTime();
        
    }
    
    //Se dispara desde la posicion de la hoja, no hay necesidad de tener x inicial (initx) 
    //O y inicial (inity)
    /**
     * Funcion que dispara la hoja de forma parabolica hacia algun objetivo, no necesita posicion inicial.
     * @param objX posicion en el eje x a la cual se disparara la hoja
     * @param objY posicion en el eje y a la cual se disparara la hoja
     * @param Shoottime tiempo que ha pasado desde que se disparo para calcular la velocidad
     * @param proyectile hoja que se va a disparar
     */
    public static void ShootLeafto(int objX, int objY, float Shoottime, Leaf proyectile){
        float velx,vely,accel;
        velx = (objX - (proyectile.leafCollision.x))/0.85f;
        vely = ((objY+10)-proyectile.leafCollision.y)/0.9f + 100f;
        accel = 100f;
        vely -= accel * Shoottime;
        proyectile.leafCollision.x += velx * Gdx.graphics.getDeltaTime();
        proyectile.leafCollision.y += vely * Gdx.graphics.getDeltaTime();
        
    }
    
}
