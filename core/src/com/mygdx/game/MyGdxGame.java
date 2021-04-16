package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.Iterator;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.DataStructures.*;

public class MyGdxGame extends ApplicationAdapter {

    
    //AQUI SE CARGAN LAS TEXTURAS 
    private SpriteBatch batch;
    private Texture bucketImage;
    
    // ESTA ES LA CAMARA, es una camara 3d pero que se proyecta ortogonalmente (2d)
    private OrthographicCamera camera;

    //ESTE ES EL OBJETO DE PRUEBA
    private MyBucket bucket;
    private MyBucket bucket2;
    private MySimpleLinkedList<MyBucket> list;

    @Override
    public void create() {
        
        // ESTA FUNCION ES MUY IMPORTANTE, SE LLAMA AL INICIO DEL JUEGO Y SE ENCARGA DE INICIALIZARLO TODO

        // SE CREA LA CAMARA  Y SE PONE EN ORTOGONAL Y LAS DIMENSIONES ED LA PANTALLA
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        
         // el batch es una cosa necesaria para renderizar el sprite
        batch = new SpriteBatch();
        
        // ACa SE CREA EL BALDE COMO UN AReA RECTANGULAR Y SE LE PONE SU TAMAÑO W,H Y SU POSICION INICIAL .X, .Y
      /*  bucket = new Rectangle();
        bucket.x = 800 / 2 - 64 / 2;
        bucket.y = 20;
        bucket.width = 64;
        bucket.height = 64;
      */  
      bucket = new MyBucket(800/2-64/2,20,64,64);
      bucket2 = new MyBucket(100, 100, 64, 64);
      list = new MySimpleLinkedList<>();
      list.add(bucket);
      list.add(bucket2);
       
        


    }

    @Override
    public void render() {
        
        // ESTA FUNCIÓN MUESTRA TODO EN PANTALLA, TAMBIÉN ES UNA FUNCIÓN QUE SE LLAMA REPETIDAMENTE, creo que a 60fps
        // clear es pa borrar todo en pantalla
        ScreenUtils.clear(0, 0, 0.2f, 1);
        
        // acá se usa lo de batch de antes, en resumen es un objeto que se encarga de manejar el tema de opengl para renderizar todo de manera eficiente
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (int i=0 ; i<list.getSize(); i++){
        batch.draw(list.getData(i).bucketImage, list.getData(i).bucket.x, list.getData(i).bucket.y);
        }
        batch.end();
        
        //este if se encarga de saber si el mouse se oprimió y en qué parte de la pantalla y mueve el balde
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            
            for (int i=0 ; i<list.getSize(); i++){
            if (touchPos.x > list.getData(i).bucket.x - list.getData(i).bucket.width  && touchPos.x < list.getData(i).bucket.x + list.getData(i).bucket.width){
                if (touchPos.y > list.getData(i).bucket.y - list.getData(i).bucket.height  && touchPos.y < list.getData(i).bucket.y + list.getData(i).bucket.height){                  
                    list.getData(i).bucket.x = touchPos.x - 64 / 2;
                    list.getData(i).bucket.y = touchPos.y - 64 / 2;
                    
                    // Poner limites pa q no se salga el balde
                    if (list.getData(i).bucket.x > 800-64){
                        list.getData(i).bucket.x = 800-64;
                        
                    }
                    if (list.getData(i).bucket.x < 0){
                        list.getData(i).bucket.x = 0;
                        
                    }
                    
                     //Gdx.app.log("MyTag", "MyMessage"); //ASI SE PRINTEA A CONSOLA
                    
                }
            }
            }
        }
    }

    @Override
    public void dispose() {
        // esta función se llama al cerrar el juego, elimina los objetos manualmente (java lo hace solito, pero es como por seguridad)
        batch.dispose();
        bucketImage.dispose();
    }
}
