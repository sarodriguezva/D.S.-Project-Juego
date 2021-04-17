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
    boolean debug = true;
    //AQUI SE CARGAN LAS TEXTURAS 
    private SpriteBatch batch;
    private Texture backgroundTexture;
    
    //AQUI SE CARGAN LAS VARIABLES
    boolean justTouched = false;
    boolean leftPressed;
    Plank currentPick = null;
    GenericButton buttonRestart = null;
    Vector3 touchPos = new Vector3();
    private final MySimpleLinkedList<Plank> plankList = new MySimpleLinkedList<>();
    int currentLevel=0;
    
    // ESTA ES LA CAMARA, es una camara 3d pero que se proyecta ortogonalmente (2d)
    private OrthographicCamera camera;
    
    @Override
    public void create() {

        // ESTA FUNCION ES MUY IMPORTANTE, SE LLAMA AL INICIO DEL JUEGO Y SE ENCARGA DE INICIALIZARLO TODO
        // SE CREA LA CAMARA  Y SE PONE EN ORTOGONAL Y LAS DIMENSIONES ED LA PANTALLA
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        // el batch es una cosa necesaria para renderizar el sprite
        initiateLevel(1);

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
        
        
        // PARTE DE LOGICA
        
        
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
            
            ///AQUI VA SI CLICKEA ALGO MIENTRAS NO ARRASTRA NADA
            if(touchPos.x > buttonRestart.buttonCollision.x - buttonRestart.buttonCollision.width && touchPos.x < buttonRestart.buttonCollision.x + buttonRestart.buttonCollision.width){
                if (touchPos.y > buttonRestart.buttonCollision.y - buttonRestart.buttonCollision.height && touchPos.y < buttonRestart.buttonCollision.y + buttonRestart.buttonCollision.height){
                    clearLevel();
                    initiateLevel(currentLevel);
                     
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
            //Gdx.app.log("MyTag", "MyMessage"); //ASI SE PRINTEA A CONSOla
        }
    
    
        // PARTE DE RENDER
        batch.begin();
        batch.draw(backgroundTexture, 0, 0);
        for (int i = 0; i < plankList.getSize(); i++) {
            batch.draw(plankList.getData(i).plankTexture, plankList.getData(i).plankCollision.x, plankList.getData(i).plankCollision.y);
        }
        batch.draw(buttonRestart.buttonTexture,0,0);
        batch.end();
        
        if (debug){
        // PARTE DE DEBUG
        long total = Runtime.getRuntime().totalMemory();
        long used  = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        Gdx.app.log("MEMORY", "total: "+total/1048576 +"MB used: "+used/1048576+"MB" );
        }
        
    }

    @Override
    public void dispose() {
        // esta función se llama al cerrar el juego, elimina los objetos manualmente (java lo hace solito, pero es como por seguridad)
        batch.dispose();
        for (int i = 0; i < plankList.getSize(); i++) {
            plankList.getData(i).dispose();
        }
        buttonRestart.dispose();
        backgroundTexture.dispose();
       
    }
    
    public void clearLevel(){
        batch.dispose();
        for (int i = 0; i < plankList.getSize(); i++) {
            plankList.getData(i).dispose();
        }
        plankList.makeEmpty();
        buttonRestart.dispose();
        backgroundTexture.dispose();
        System.gc();
        
    }

    public void initiateLevel(int level){
    //ACÁ VA LA INFO DE NIVELES. 0=MENU;
    switch (level){
        case 1:
        batch = new SpriteBatch();
        createPlank(400,200,64,64);
        createPlank(0,0,64,64);
        buttonRestart = new GenericButton(0, 0, 50, 50, "buttonRestart.png");
        backgroundTexture = new Texture(Gdx.files.internal("parallax-mountain-bg.png"));
        currentLevel=1;
        break;
    }
    
    }
}
