package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Leaf extends ApplicationAdapter {
    public Texture leafTexture;
    public Rectangle leafCollision;
    public Integer leafNumber;
    public Leaf(int x, int y, int w, int h, int num){
        leafCollision = new Rectangle();
        leafCollision.x = x;
        leafCollision.y = y;
        leafCollision.width = w;
        leafCollision.height = h;
        leafNumber=num;
        leafTexture = new Texture(Gdx.files.internal("leaf.png"));
    }
}


