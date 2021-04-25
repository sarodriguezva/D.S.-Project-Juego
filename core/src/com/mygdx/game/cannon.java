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
 *
 * @author judir
 */
public class Cannon extends ApplicationAdapter {
    public Texture cannonTexture;
    public Rectangle cannonCollision;
    public Integer cannonNumber;
    public Cannon(int x, int y, int w, int h, String pathTexture ){
        cannonCollision = new Rectangle();
        cannonCollision.x = x;
        cannonCollision.y = y;
        cannonCollision.width = w;
        cannonCollision.height = h;
        cannonTexture = new Texture(Gdx.files.internal(pathTexture));
    }
    
    public static void ShootPlankto(int objX, int objY, float initx, float inity, float Shoottime, Plank proyectile){
        float velx,vely,accel;
        velx = (objX - (initx+30))/3;
        vely = ((objY+10)-inity)/3 + 120f;
        accel = 73f;
        vely -= accel * Shoottime;
        proyectile.plankCollision.x += velx * Gdx.graphics.getDeltaTime();
        proyectile.plankCollision.y += vely * Gdx.graphics.getDeltaTime();
        
    }
    
}
