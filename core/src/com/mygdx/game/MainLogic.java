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

public class MainLogic extends ApplicationAdapter {

    //AQUI SE CARGAN LAS TEXTURAS 
    private SpriteBatch batch;
    private Texture backgroundTexture;
    boolean justTouched = false;
    boolean leftPressed;
    Plank currentPick = null;
    Vector3 touchPos = new Vector3();

    // ESTA ES LA CAMARA, es una camara 3d pero que se proyecta ortogonalmente (2d)
    private OrthographicCamera camera;

    //ESTE ES EL OBJETO DE PRUEBA
    private final MyDoubleLinkedList<Plank> plankList = new MyDoubleLinkedList<>();

    @Override
    public void create() {

        // ESTA FUNCION ES MUY IMPORTANTE, SE LLAMA AL INICIO DEL JUEGO Y SE ENCARGA DE INICIALIZARLO TODO
        // SE CREA LA CAMARA  Y SE PONE EN ORTOGONAL Y LAS DIMENSIONES ED LA PANTALLA
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        backgroundTexture = new Texture(Gdx.files.internal("parallax-mountain-bg.png"));

        // el batch es una cosa necesaria para renderizar el sprite
        batch = new SpriteBatch();
            
        createPlank(400,200,64,64);
        createPlank(0,0,64,64);
    }
    
    // Función para crear un plank y añadirlo a la lista automaticamente
    public void createPlank(int x, int y, int w, int h) {
        // Creacion de buckets.
        Plank myPlank = new Plank(x, y, w, h);
        plankList.add(myPlank);
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
        batch.draw(backgroundTexture, 0, 0);
        for (int i = 0; i < plankList.getSize(); i++) {
            batch.draw(plankList.getData(i).plankTexture, plankList.getData(i).plankCollision.x, plankList.getData(i).plankCollision.y);
        }
        batch.end();

        //justTouched me dice si el mouse ha sido recientemente presionado, leftpressed si está continuamente presionado
        justTouched = Gdx.input.justTouched();
        leftPressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);

        // Si está continuamente presionado, guardeme las coordenadas del mouse
        if (leftPressed) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
        }

        // Si ha sido recientemente presionado compruebe si ya está arrastrando un objeto, si no hay objeto, busca el que el mouse presiona en esas coordenadas.
        if (justTouched && currentPick == null) {
            for (int i = 0; i < plankList.getSize(); i++) {
                if (touchPos.x > plankList.getData(i).plankCollision.x - plankList.getData(i).plankCollision.width && touchPos.x < plankList.getData(i).plankCollision.x + plankList.getData(i).plankCollision.width) {
                    if (touchPos.y > plankList.getData(i).plankCollision.y - plankList.getData(i).plankCollision.height && touchPos.y < plankList.getData(i).plankCollision.y + plankList.getData(i).plankCollision.height) {
                        currentPick = plankList.getData(i);

                    }
                }
            }
        }

        // Si está siendo un objeto presionado:
        if (currentPick != null) {
            // Movement

            currentPick.plankCollision.x = touchPos.x - 64 / 2;
            currentPick.plankCollision.y = touchPos.y - 64 / 2;

            // Poner limites pa q no se salga el balde
            if (currentPick.plankCollision.x > 800 - 64) {
                currentPick.plankCollision.x = 800 - 64;

            }
            if (currentPick.plankCollision.x < 0) {
                currentPick.plankCollision.x = 0;
            }
            // Limites en y
            if (currentPick.plankCollision.y > 500 - 64) {
                currentPick.plankCollision.y = 500 - 64;

            }
            if (currentPick.plankCollision.y < 0) {
                currentPick.plankCollision.y = 0;
            }
            

            // Si deja de presionar el mouse, se quita el objeto arrastrable.
            if (!leftPressed) {
                currentPick = null;
            }
            //Gdx.app.log("MyTag", "MyMessage"); //ASI SE PRINTEA A CONSOLA
        }
    }

    @Override
    public void dispose() {
        // esta función se llama al cerrar el juego, elimina los objetos manualmente (java lo hace solito, pero es como por seguridad)
        batch.dispose();
        for (int i = 0; i < plankList.getSize(); i++) {
            plankList.getData(i).dispose();
        }
    }
}
///7 56asd4fa56sd4f
