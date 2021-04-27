package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.DataStructures.*;

public class MainLogic extends ApplicationAdapter {

    boolean debug = false;

    //////// **** VARIABLES **** ////////
    // BOTONES
    // TEXTURA Y OBJETOS PARA BOTONES
    GenericButton buttonRestart = null;
    GenericButton buttonHelp = null;
    GenericButton buttonClose = null;
    private Texture infoTexture;
    boolean info = false;
    GenericButton buttonShooting = null;
    //GANAR O PERDER
    GenericButton buttonWin=null;
    GenericButton buttonLose=null;
    boolean win= false;
    boolean lose=false;
    String mode;
    int Lastfilled;
    

    // TEXTURAS DE FONDO
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private OrthographicCamera camera;
    int currentLevel = 0;

    // MOUSE
    boolean justTouched = false;
    boolean leftPressed;
    Vector3 touchPos = new Vector3();

    // CANNON
    Canon buttonCannon = null;
    boolean Shooting = false;
    float Shootingtime = 0;
    int Shootingindex = 0;

    // PUENTE
    Bridge<Integer> OrderBridge;

    // TABLAS
    Plank currentPick = null;
    private final MyDoubleLinkedList<Plank> listPlank = new MyDoubleLinkedList<>();
    private final MyDoubleLinkedList<Plank> listPlankBridge = new MyDoubleLinkedList<>();
    private final MyDoubleLinkedList<Plank> listPlankCannon = new MyDoubleLinkedList<>();
    private final MyDoubleLinkedList<Plank> listPlankFired = new MyDoubleLinkedList<>();
    MyStack<Integer> plankStack = new MyStack<>();
    private BitmapFont font;

    @Override
    public void create() {

        // ESTA FUNCION ES MUY IMPORTANTE, SE LLAMA AL INICIO DEL JUEGO Y SE ENCARGA DE INICIALIZARLO TODO
        // SE CREA LA CAMARA  Y SE PONE EN ORTOGONAL Y LAS DIMENSIONES DE LA PANTALLA
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        //Se inicia el nivel y se pone la fuente que se va a usar
        initiateLevel(1);
        font = new BitmapFont(Gdx.files.internal("asd.fnt"));

    }

    // Funcion para crear un plank y anadirlo a la lista automaticamente
    public void createPlank(int x, int y, int w, int h, MyDoubleLinkedList list, int num) {
        // Creacion de buckets.
        Plank myPlank = new Plank(x, y, w, h, num);
        list.add(myPlank);
    }

    @Override
    public void render() {

        // ESTA FUNCION MUESTRA TODO EN PANTALLA, TAMBIEN ES UNA FUNCION QUE SE LLAMA REPETIDAMENTE
        // clear es pa borrar todo en pantalla
        ScreenUtils.clear(0, 0, 0.2f, 1);

        // aca se usa lo de batch es un objeto que se encarga de manejar el tema de opengl para renderizar todo de manera eficiente
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // PARTE DE LOGICA
        //justTouched me dice si el mouse ha sido recientemente presionado, leftpressed si esta continuamente presionado
        justTouched = Gdx.input.justTouched();
        leftPressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);

        // Si esta continuamente presionado, guardeme las coordenadas del mouse
        if (leftPressed) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
        }

        // Si ha sido recientemente presionado compruebe si ya esta arrastrando un objeto, si no hay objeto, busca el que el mouse presiona en esas coordenadas.
        if (justTouched && currentPick == null) {

            if (info == false) {
                for (int i = 0; i < listPlank.getSize(); i++) {
                    if (listPlank.getData(i) != null) {
                        if (touchPos.x > listPlank.getData(i).plankCollision.x - listPlank.getData(i).plankCollision.width && touchPos.x < listPlank.getData(i).plankCollision.x + listPlank.getData(i).plankCollision.width) {
                            if (touchPos.y > listPlank.getData(i).plankCollision.y - listPlank.getData(i).plankCollision.height && touchPos.y < listPlank.getData(i).plankCollision.y + listPlank.getData(i).plankCollision.height) {
                                currentPick = listPlank.getData(i);

                            }
                        }
                    }
                }

                ///AQUI VA SI CLICKEA ALGO MIENTRAS NO ARRASTRA NADA
                if (touchPos.x > buttonRestart.buttonCollision.x - buttonRestart.buttonCollision.width && touchPos.x < buttonRestart.buttonCollision.x + buttonRestart.buttonCollision.width) {
                    if (touchPos.y > buttonRestart.buttonCollision.y - buttonRestart.buttonCollision.height && touchPos.y < buttonRestart.buttonCollision.y + buttonRestart.buttonCollision.height) {
                        clearLevel();
                        initiateLevel(currentLevel);
                    }
                }
            }

            // BOTON HELP ////
            if (touchPos.x > buttonHelp.buttonCollision.x - buttonHelp.buttonCollision.width && touchPos.x < buttonHelp.buttonCollision.x + buttonHelp.buttonCollision.width) {
                if (touchPos.y > buttonHelp.buttonCollision.y - buttonHelp.buttonCollision.height && touchPos.y < buttonHelp.buttonCollision.y + buttonHelp.buttonCollision.height) {
                    info = true;

                }
            }

            //BOTON FUEGO//
            if (touchPos.x > buttonShooting.buttonCollision.x - buttonShooting.buttonCollision.width && touchPos.x < buttonShooting.buttonCollision.x + buttonShooting.buttonCollision.width) {
                if (touchPos.y > buttonShooting.buttonCollision.y - buttonShooting.buttonCollision.height && touchPos.y < buttonShooting.buttonCollision.y + buttonShooting.buttonCollision.height) {
                    Shooting = true;
                }
            }

            /// BOTON PARA CERRAR EL POPUP DE HELP ///
            if (touchPos.x > buttonClose.buttonCollision.x - buttonClose.buttonCollision.width && touchPos.x < buttonClose.buttonCollision.x + buttonClose.buttonCollision.width) {
                if (touchPos.y > buttonClose.buttonCollision.y - buttonClose.buttonCollision.height && touchPos.y < buttonClose.buttonCollision.y + buttonClose.buttonCollision.height) {
                    if (info){
                    info = false;}
                    
                    if (win){
                          clearLevel();
                        initiateLevel(currentLevel+1);
                    }

                    if (lose){
                        clearLevel();
                        initiateLevel(currentLevel);
                        
                    }
                    win = false;
                    lose = false;
                    
                }
            }

        }

        // Si esta siendo un objeto presionado:
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
            if (currentPick.plankCollision.y > 80 - 64) {
                currentPick.plankCollision.y = 80 - 64;

            }
            if (currentPick.plankCollision.y < 0) {
                currentPick.plankCollision.y = 0;
            }

            // Si deja de presionar el mouse, se quita el objeto arrastrable.
            if (!leftPressed) {
                if (Math.abs(currentPick.plankCollision.x - buttonCannon.cannonCollision.x) < 60) {
                    currentPick.plankCollision.x = buttonCannon.cannonCollision.x + 20;
                    currentPick.plankCollision.y = buttonCannon.cannonCollision.y - 20;
                    listPlankCannon.add(currentPick);
                    int[] Pos = listPlank.find(currentPick);
                    if (listPlank.getData(Pos[0]) != null) {
                        listPlank.delete(Pos[0]);
                    }
                }
                currentPick = null;
            }
            //Gdx.app.log("MyTag", "MyMessage"); //ASI SE PRINTEA A CONSOla
        }

        // PARTE DE RENDER
        batch.enableBlending();
        batch.begin();
        batch.draw(backgroundTexture, 0, 0);

        //RENDERIZADO DE TABLAS
        for (int i = 0; i < listPlankBridge.getSize(); i++) {
            Sprite sprPlankBridge = new Sprite(listPlankBridge.getData(i).plankTexture);
            sprPlankBridge.setPosition(listPlankBridge.getData(i).plankCollision.x, listPlankBridge.getData(i).plankCollision.y);
            sprPlankBridge.setColor(1, 1, 1, 0.4f);
            sprPlankBridge.draw(batch);
            font.draw(batch, Integer.toString(listPlankBridge.getData(i).plankNumber), listPlankBridge.getData(i).plankCollision.x + 10, listPlankBridge.getData(i).plankCollision.y + 70);

        }

        for (int i = 0; i < listPlank.getSize(); i++) {
            if (listPlank.getData(i) != null) {
                batch.draw(listPlank.getData(i).plankTexture, listPlank.getData(i).plankCollision.x, listPlank.getData(i).plankCollision.y);
                font.draw(batch, Integer.toString(listPlank.getData(i).plankNumber), listPlank.getData(i).plankCollision.x + 10, listPlank.getData(i).plankCollision.y + 70);

            }
        }

        for (int i = 0; i < listPlankFired.getSize(); i++) {

            batch.draw(listPlankFired.getData(i).plankTexture, listPlankFired.getData(i).plankCollision.x, listPlankFired.getData(i).plankCollision.y);
            font.draw(batch, Integer.toString(listPlankFired.getData(i).plankNumber), listPlankFired.getData(i).plankCollision.x + 10, listPlankFired.getData(i).plankCollision.y + 70);

        }

        // RENDERIZADO DE BOTONES
        batch.draw(buttonRestart.buttonTexture, 0, 0);
        batch.draw(buttonRestart.buttonTexture, 0, 0);
        batch.draw(buttonHelp.buttonTexture, 0, 555);
        batch.draw(buttonCannon.cannonTexture, 90, 70);
        batch.draw(buttonShooting.buttonTexture, 10, 80);

        //RENDERIZADO DE EL POPUP DE INFO
        if (info == true) {
            // Sprite es un tipo de objeto que deja cambiar algunas caracteristicas ed las texturas x eso se crea un sprite con la textura
            Sprite sprite = new Sprite(infoTexture);
            //Aqui se le pone un color en RGB,A. osea color y opacidad.
            sprite.setPosition(150, 100);
            sprite.setSize(500, 450);
            sprite.setColor(1, 1, 1, 0.8f);
            sprite.draw(batch);
            batch.draw(buttonClose.buttonTexture, 600, 503);

        }
        if (win==true){
            batch.draw(buttonWin.buttonTexture,53,249);
            batch.draw(buttonClose.buttonTexture, 600, 503);
            
        }
        if (lose==true){
            batch.draw(buttonLose.buttonTexture, 53, 249);
            batch.draw(buttonClose.buttonTexture, 600, 503);
            
        }

        // Funcion de disparo
        if (Shooting == true) {
            if (Shootingindex >= listPlankCannon.getSize()) {
                Lastfilled = Shootingindex;
                Shooting = false;
                MyDoubleLinkedList<Integer> listNumber = convertPlankToNumber(listPlankFired);
                if(Shootingindex >= listPlankBridge.getSize()){
                //Aca se sabe si el usuario gana o pierde
                if (OrderBridge.commit(listNumber)) {
                    Gdx.app.log("E", "Ganar");
                    win=true;  
                } else {
                    Gdx.app.log("E", "Perder");
                    lose=true; 
                }
                }

            } else {
                if(mode.equals("fifo")){
                if (Shootingindex < listPlankCannon.getSize()) {
                    if (Math.abs(listPlankBridge.getData(Shootingindex).plankCollision.x - listPlankCannon.getData(Shootingindex).plankCollision.x) > 30 || Math.abs(listPlankBridge.getData(Shootingindex).plankCollision.y - listPlankCannon.getData(Shootingindex).plankCollision.y) > 30) {
                        Canon.ShootPlankto((int) listPlankBridge.getData(Shootingindex).plankCollision.x, (int) listPlankBridge.getData(Shootingindex).plankCollision.y, buttonCannon.cannonCollision.x, buttonCannon.cannonCollision.y, Shootingtime, listPlankCannon.getData(Shootingindex));
                        batch.draw(listPlankCannon.getData(Shootingindex).plankTexture, listPlankCannon.getData(Shootingindex).plankCollision.x, listPlankCannon.getData(Shootingindex).plankCollision.y);
                        font.draw(batch, Integer.toString(listPlankCannon.getData(Shootingindex).plankNumber), listPlankCannon.getData(Shootingindex).plankCollision.x + 10, listPlankCannon.getData(Shootingindex).plankCollision.y + 70);
                        Shootingtime += Gdx.graphics.getDeltaTime();
                    } else if (Shootingindex < listPlankBridge.getSize()) {
                        listPlankCannon.getData(Shootingindex).plankCollision.x = listPlankBridge.getData(Shootingindex).plankCollision.x;
                        listPlankCannon.getData(Shootingindex).plankCollision.y = listPlankBridge.getData(Shootingindex).plankCollision.y;
                        listPlankFired.add(listPlankCannon.getData(Shootingindex));
                        Shootingindex++;
                        Shootingtime = 0;
                    }
                }
            } else if(mode.equals("lifo")){

               
               
            if(listPlankBridge.getSize() - Shootingindex >=0){
            if (Math.abs(listPlankBridge.getData(Shootingindex).plankCollision.x - listPlankCannon.getData((listPlankCannon.getSize() + Lastfilled -1) - Shootingindex ).plankCollision.x) > 30 || Math.abs(listPlankBridge.getData(Shootingindex).plankCollision.y - listPlankCannon.getData((listPlankCannon.getSize() + Lastfilled -1) - Shootingindex ).plankCollision.y) > 30) {
                        Canon.ShootPlankto((int) listPlankBridge.getData(Shootingindex).plankCollision.x, (int) listPlankBridge.getData(Shootingindex).plankCollision.y, buttonCannon.cannonCollision.x, buttonCannon.cannonCollision.y, Shootingtime, listPlankCannon.getData((listPlankCannon.getSize() + Lastfilled -1) - Shootingindex ));
                        batch.draw(listPlankCannon.getData((listPlankCannon.getSize() + Lastfilled -1) - Shootingindex ).plankTexture, listPlankCannon.getData((listPlankCannon.getSize() + Lastfilled -1) - Shootingindex ).plankCollision.x, listPlankCannon.getData((listPlankCannon.getSize() + Lastfilled -1) - Shootingindex ).plankCollision.y);
                        font.draw(batch, Integer.toString(listPlankCannon.getData((listPlankCannon.getSize() + Lastfilled -1) - Shootingindex ).plankNumber), listPlankCannon.getData((listPlankCannon.getSize() + Lastfilled -1) - Shootingindex ).plankCollision.x + 10, listPlankCannon.getData((listPlankCannon.getSize() + Lastfilled -1) - Shootingindex ).plankCollision.y + 70);
                        Shootingtime += Gdx.graphics.getDeltaTime();
                       
                    } else if ((listPlankCannon.getSize() + Lastfilled -1) - Shootingindex >= 0) {
                        listPlankCannon.getData((listPlankCannon.getSize() + Lastfilled -1) - Shootingindex).plankCollision.x = listPlankBridge.getData(Shootingindex).plankCollision.x;
                        listPlankCannon.getData((listPlankCannon.getSize() + Lastfilled -1) - Shootingindex).plankCollision.y = listPlankBridge.getData(Shootingindex).plankCollision.y;
                        listPlankFired.add(listPlankCannon.getData((listPlankCannon.getSize() + Lastfilled -1) - Shootingindex));
                        Shootingindex++;
                        Shootingtime = 0;
                    } 
            }
            }
            }
        }

        batch.end();

        if (debug) {
            // PARTE DE DEBUG
            long total = Runtime.getRuntime().totalMemory();
            long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            Gdx.app.log("MEMORY", "total: " + total / 1048576 + "MB used: " + used / 1048576 + "MB");
        }

    }

    @Override
    public void dispose() {
        // esta funci�n se llama al cerrar el juego, elimina los objetos manualmente (java lo hace solito, pero es como por seguridad)
        batch.dispose();
        for (int i = 0; i < listPlank.getSize(); i++) {
            listPlank.getData(i).dispose();
        }
        buttonRestart.dispose();
        backgroundTexture.dispose();

    }

    public void clearLevel() {
        batch.dispose();
        for (int i = 0; i < listPlank.getSize(); i++) {
            listPlank.getData(i).dispose();
        }
        Shooting = false;
        Shootingtime = 0;
        Shootingindex = 0;
        Lastfilled = 0;
        listPlank.makeEmpty();
        listPlankCannon.makeEmpty();
        listPlankBridge.makeEmpty();
        listPlankFired.makeEmpty();
        buttonRestart.dispose();
        backgroundTexture.dispose();
        System.gc();

    }

    public void initiateLevel(int level) {
        Integer[] myArr;
        Integer[] myArr2;
        //AC� VA LA INFO DE NIVELES. 0=MENU;
        if (level!=0){
            
                batch = new SpriteBatch();
                buttonHelp = new GenericButton(0, 555, 50, 50, "buttonHelp.png");
                buttonClose = new GenericButton(600, 503, 50, 50, "buttonClose.png");
                buttonRestart = new GenericButton(0, 0, 50, 50, "buttonRestart.png");
                backgroundTexture = new Texture(Gdx.files.internal("parallax-mountain-bg.png"));
                buttonWin= new GenericButton(100,100,100,100,"Win.png");
                buttonLose=new GenericButton(0,0,50,50,"Lose.png");

                
        }
        switch (level) {
            
            case 1:
                mode = "fifo";
                infoTexture = new Texture(Gdx.files.internal("Info.png"));
                buttonCannon = new Canon(90, 70, 100, 100, "Canon_1.png");
                currentLevel = 1;
                buttonShooting = new GenericButton(10, 80, 50, 50, "shooting.png");
                myArr = strToArr("1,2,3");
                OrderBridge = new Bridge<>(myArr);
                for (int i = 0; i < OrderBridge.getSize(); i++) {
                    createPlank(100 + i * 45, 400, 44, 117, listPlankBridge, myArr[i]);
                }
                myArr2 = strToArr("1,2,3");
                for (int i = 0; i < myArr2.length; i++) {
                    createPlank(300 + i * 45, 0, 44, 117, listPlank, myArr2[i]);
                }
                break;
                
            case 2:
                mode = "lifo";
                infoTexture = new Texture(Gdx.files.internal("Info.png"));
                buttonCannon = new Canon(90, 70, 100, 100, "Canon_2.png");
                currentLevel = 2;
                buttonShooting = new GenericButton(10, 80, 50, 50, "shooting.png");
                myArr = strToArr("3,4,5");
                OrderBridge = new Bridge<>(myArr);
                for (int i = 0; i < OrderBridge.getSize(); i++) {
                    createPlank(100 + i * 45, 400, 44, 117, listPlankBridge, myArr[i]);
                }
                myArr2 = strToArr("3,4,5");
                for (int i = 0; i < myArr2.length; i++) {
                    createPlank(300 + i * 45, 0, 44, 117, listPlank, myArr2[i]);
                }
                break;
            case 3:
                mode = "fifo";
                infoTexture = new Texture(Gdx.files.internal("Info.png"));
                buttonCannon = new Canon(90, 70, 100, 100, "Canon_1.png");
                currentLevel = 3;
                buttonShooting = new GenericButton(10, 80, 50, 50, "shooting.png");
                myArr = strToArr("1,2,3,4,5");
                OrderBridge = new Bridge<>(myArr);
                for (int i = 0; i < OrderBridge.getSize(); i++) {
                    createPlank(100 + i * 45, 400, 44, 117, listPlankBridge, myArr[i]);
                }
                myArr2 = strToArr("4,2,1,3,5");
                for (int i = 0; i < myArr2.length; i++) {
                    createPlank(300 + i * 45, 0, 44, 117, listPlank, myArr2[i]);
                }
                break;
        }

    }

    MyDoubleLinkedList<Integer> convertPlankToNumber(MyDoubleLinkedList<Plank> toConvert) {
        MyDoubleLinkedList<Integer> toReturn = new MyDoubleLinkedList<>();
        for (int i = 0; i < toConvert.getSize(); i++) {
            toReturn.add(toConvert.getData(i).plankNumber);
        }
        return toReturn;
    }
    
    Integer[] strToArr(String str){
        String[] myarr = str.split(",");
        Integer[] toReturn = new Integer[myarr.length];
        
        for (int i=0 ; i<myarr.length;i++){
            toReturn[i] = Integer.parseInt(myarr[i] ) ;
        }
        return toReturn;
    }

}
