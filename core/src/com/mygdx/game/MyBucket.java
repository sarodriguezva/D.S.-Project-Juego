package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class MyBucket extends ApplicationAdapter {
    public Texture bucketImage;
    public Rectangle bucket;
    
    public MyBucket(int x, int y, int w, int h){
        bucket = new Rectangle();
        bucket.x = x;
        bucket.y = y;
        bucket.width = w;
        bucket.height = h;
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));
    }

    
}
