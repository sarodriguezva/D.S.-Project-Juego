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
public class cannon extends ApplicationAdapter {
    public Texture cannonTexture;
    public Rectangle cannonCollision;
    public Integer cannonNumber;
    public cannon(int x, int y, int w, int h, String pathTexture ){
        cannonCollision = new Rectangle();
        cannonCollision.x = x;
        cannonCollision.y = y;
        cannonCollision.width = w;
        cannonCollision.height = h;
        cannonTexture = new Texture(Gdx.files.internal(pathTexture));
    }
    
}
