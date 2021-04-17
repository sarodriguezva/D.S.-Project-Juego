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
 * @author JUANS
 */
public class GenericButton extends ApplicationAdapter {

    public Texture buttonTexture;
    public Rectangle buttonCollision;
    public GenericButton(int x, int y, int w, int h, String pathTexture){
        buttonCollision = new Rectangle();
        buttonCollision.x = x;
        buttonCollision.y = y;
        buttonCollision.width = w;
        buttonCollision.height = h;
        buttonTexture = new Texture(Gdx.files.internal(pathTexture));
    }       
}


