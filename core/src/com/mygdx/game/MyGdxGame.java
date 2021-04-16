package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.DataStructures.*;

public class MyGdxGame extends ApplicationAdapter {

    //AQUI SE CARGAN LAS TEXTURAS 
    private SpriteBatch batch;
    private Texture background;
    boolean justTouched = false;
    boolean leftPressed;
    MyBucket currentPick = null;
    Vector3 touchPos = new Vector3();

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
        background = new Texture(Gdx.files.internal("parallax-mountain-bg.png"));

        // el batch es una cosa necesaria para renderizar el sprite
        batch = new SpriteBatch();

        // Creacion de buckets.
        bucket = new MyBucket(800 / 2 - 64 / 2, 20, 64, 64);
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
        batch.draw(background, 0, 0);
        for (int i = 0; i < list.getSize(); i++) {
            batch.draw(list.getData(i).bucketImage, list.getData(i).bucket.x, list.getData(i).bucket.y);
        }
        batch.end();

        //este if se encarga de saber si el mouse se oprimió y en qué parte de la pantalla y mueve el balde
        justTouched = Gdx.input.justTouched();
        leftPressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);

        if (leftPressed) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
        }

        if (justTouched && currentPick == null) {
            for (int i = 0; i < list.getSize(); i++) {
                if (touchPos.x > list.getData(i).bucket.x - list.getData(i).bucket.width && touchPos.x < list.getData(i).bucket.x + list.getData(i).bucket.width) {
                    if (touchPos.y > list.getData(i).bucket.y - list.getData(i).bucket.height && touchPos.y < list.getData(i).bucket.y + list.getData(i).bucket.height) {
                        currentPick = list.getData(i);

                    }
                }
            }
        }

        if (currentPick != null) {
            // Movement

            currentPick.bucket.x = touchPos.x - 64 / 2;
            currentPick.bucket.y = touchPos.y - 64 / 2;
            // Gdx.app.log("Movemente", touchPos.x + " "+touchPos.y);

            // Poner limites pa q no se salga el balde
            if (currentPick.bucket.x > 800 - 64) {
                currentPick.bucket.x = 800 - 64;

            }
            if (currentPick.bucket.x < 0) {
                currentPick.bucket.x = 0;

            }
            
            if (!leftPressed){
                currentPick=null;
            }
            //Gdx.app.log("MyTag", "MyMessage"); //ASI SE PRINTEA A CONSOLA
        }
    }

    @Override
    public void dispose() {
        // esta función se llama al cerrar el juego, elimina los objetos manualmente (java lo hace solito, pero es como por seguridad)
        batch.dispose();
        for (int i = 0; i < list.getSize(); i++) {
            list.getData(i).dispose();
        }
    }
}
