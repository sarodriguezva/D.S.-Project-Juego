package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Plank extends ApplicationAdapter {
    public Texture plankTexture;
    public Rectangle plankCollision;
    public Plank(int x, int y, int w, int h){
        plankCollision = new Rectangle();
        plankCollision.x = x;
        plankCollision.y = y;
        plankCollision.width = w;
        plankCollision.height = h;
        plankTexture = new Texture(Gdx.files.internal("bucket.png"));
    }

    
}