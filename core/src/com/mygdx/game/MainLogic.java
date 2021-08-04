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
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.System.currentTimeMillis;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase que contiene toda la logica del juego y su funcionamiento interno
 * @author 3200g
 */
public class MainLogic extends ApplicationAdapter {

    //////// **** VARIABLES **** ////////
    // BOTONES
    GenericButton buttonRestart = null;
    GenericButton buttonCtrz = null;
    GenericButton buttonDelete = null;
    GenericButton buttonHelp = null;
    GenericButton buttonClose = null;
    GenericButton buttonLevelTrees = null;
    GenericButton buttonLevelLinearDS = null;
    GenericButton buttonPause = null;
    GenericButton volverMenu = null;
    GenericButton buttonShooting = null;
    GenericButton buttonWin = null;
    GenericButton buttonLose = null;
    // TEXTURA Y OBJETOS PARA BOTONES
    private Texture infoTexture;
    private Texture fondoPause;
    private Texture treeTexture;
    // TEXTURAS DE FONDO
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private OrthographicCamera camera;
    // BOOLEANOS PARA CONTROLAR LA MAQUINA DE ESTADOS
    boolean info = false;
    boolean pause = false;
    boolean menu = false;
    boolean canUndo = true;
    boolean debug = false;
    boolean win = false;
    boolean lose = false;

    // VARIABLES DE TEXTO
    String tema;
    String mode;
    // VARIABLE DE ENTEROS
    int Lastfilled;
    int currentLevel = 0;
    int sec = 0;
    int levelScore = 0;
    int minScore = 0;
    int treeX=0;
    int treeY=0;
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

    // LISTAS DE TABLAS
    Plank currentPick = null;
    private final MyDoubleLinkedList<Plank> listPlank = new MyDoubleLinkedList<>();
    private final MyDoubleLinkedList<Plank> listPlankBridge = new MyDoubleLinkedList<>();
    private final MyDoubleLinkedList<Plank> listPlankCannon = new MyDoubleLinkedList<>();
    private final MyDoubleLinkedList<Plank> listPlankFired = new MyDoubleLinkedList<>();
    MyStack<Integer> plankStack = new MyStack<>();
    
    // LISTAS DE ARBOLES
    private final MyDoubleLinkedList<Leaf> listLeaf = new MyDoubleLinkedList<>();
    private final MyDoubleLinkedList<Leaf> listLeafFired = new MyDoubleLinkedList<>();
    private final MyDoubleLinkedList<Leaf> listLeafTree = new MyDoubleLinkedList<>();
    private final MyDoubleLinkedList<hueco> listHueco = new MyDoubleLinkedList<>();
    private final MyDoubleLinkedList<hueco> listHuecosUsados = new MyDoubleLinkedList<>();
    //LISTAS PARA VERIFICAR EL ORDEN DE LOS ARBOLES
    private MyDoubleLinkedList<Integer> listLeafTreeOrder = new MyDoubleLinkedList<>();
    private Integer[] listLeafTreePlayerOrder = new Integer[1];
    // LISTA PARA PODER HACER UN-DO
    private MyDoubleLinkedList<Leaf> LastDeleted = new MyDoubleLinkedList<>();
    hueco objetivoArboles = new hueco(0, 0, 0, 0, 0);
    hueco objetivoBorrar = new hueco(1, 150, 0, 89, 92);
    private BitmapFont font;
    private BitmapFont fontScore;
    Firebase fbase;
    // FUNCION PARA GUARDAR TEXTO EN UN ARCHIVO .TXT
    /**
     * Esta funcion guarda una cadena de texto en un archivo txt llamado data.txt
     * @param str La cadena a guardar en el archivo
     * @throws IOException 
     */
    public void appendStringToTxt(String str) throws IOException {
        
        BufferedWriter writer = new BufferedWriter(new FileWriter("data.txt", true));
        writer.append("\n\n\n\n\n");
        writer.append(str);

        writer.close();
        
    }

    @Override
    /**
     * Esta funcion se llama al iniciar el juego, es la encargada de cargar variables antes de renderizar la pantalla
     */
    public void create() {

        // ESTA FUNCION ES MUY IMPORTANTE, SE LLAMA AL INICIO DEL JUEGO Y SE ENCARGA DE INICIALIZARLO TODO
        // SE CREA LA CAMARA  Y SE PONE EN ORTOGONAL Y LAS DIMENSIONES DE LA PANTALLA
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        //Se inicia el nivel y se pone la fuente que se va a usar
        font = new BitmapFont(Gdx.files.internal("asd.fnt"));
        fontScore = new BitmapFont(Gdx.files.internal("test.fnt"));
        
        // PRUEBAS DE TIEMPOS Y MEMORIA PARA DISTINTAS ESTRUCTURAS
        if (debug == false) {
            initiateLevel(0);
        } /// DATA TIME PARA DISTINTAS ESTRUCTURAS DE DATOS
        else {

            try {
                int m = 20000;
                int b = 20000-20000;
                String str = "";
                long total = Runtime.getRuntime().totalMemory();
                long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                str += "MEMORY PRE DATA" + "total: " + total / 1048576 + "MB used: " + used / 1048576 + "MB";

                AVLTree<Leaf> plankTry = new AVLTree<>();
                
                for (int i = 0; i < m; i++) {
                    Leaf Leaftry = new Leaf(i, i, i, i, i);
                    plankTry.insert(Leaftry);
                }
                
                long pretm = System.currentTimeMillis();

                for (int i = m; i > b; i--) {
                    Leaf Leaftry = new Leaf(i, i, i, i, i);
                    plankTry.contains(Leaftry);
                }
                
                
                long posttm = System.currentTimeMillis();
                total = Runtime.getRuntime().totalMemory();
                used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                str += "\n MEMORY POST DATA" + "total: " + total / 1048576 + "MB used: " + used / 1048576 + "MB";
                str += "\n Time in miliseconds: " + (posttm - pretm);
                appendStringToTxt(str);

            } catch (IOException ex) {
                Logger.getLogger(MainLogic.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // FIN PRUEBAS
        fbase = new Firebase();
        try {
            fbase.connect();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    // Funcion para crear un plank y anadirlo a la lista automaticamente
    /**
     * Esta funcion se utiliza para crear una tabla con posicion, tamanio y numero ademas lo aniade a una lista de tablas
     * @param x posicion de la tabla en el eje x
     * @param y posicion de la tabla en el eje y
     * @param w tamanio de anchura
     * @param h tamanio de altura
     * @param list lista a la cual aniadirse
     * @param num numero de la tabla
     */
    public void createPlank(int x, int y, int w, int h, MyDoubleLinkedList list, int num) {
        // Creacion de buckets.
        Plank myPlank = new Plank(x, y, w, h, num);
        list.add(myPlank);
    }

    @Override
    /**
     * Esta funcion es la encargada de la logica del juego y de mostrar todo en pantalla, se llama en un ciclo infinito hasta cerrar el juego
     */
    public void render() {

        // ESTA FUNCION MUESTRA TODO EN PANTALLA, TAMBIEN ES UNA FUNCION QUE SE LLAMA REPETIDAMENTE
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

        if (currentLevel != 0) {
            // Si ha sido recientemente presionado compruebe si ya esta arrastrando un objeto, si no hay objeto, busca el que el mouse presiona en esas coordenadas.
            if (justTouched && currentPick == null) {
                ///AQUI VA SI CLICKEA ALGO MIENTRAS NO ARRASTRA NADA
                
                // CLICK BOTON INFO
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

                    // CLICK BOTON RESTART
                    if (touchPos.x > buttonRestart.buttonCollision.x - buttonRestart.buttonCollision.width && touchPos.x < buttonRestart.buttonCollision.x + buttonRestart.buttonCollision.width) {
                        if (touchPos.y > buttonRestart.buttonCollision.y - buttonRestart.buttonCollision.height && touchPos.y < buttonRestart.buttonCollision.y + buttonRestart.buttonCollision.height) {
                            clearLevel();
                            initiateLevel(currentLevel);
                        }
                    }
                }
                // CLICK BOTON VOLVER AL MENU
                if (touchPos.x > volverMenu.buttonCollision.x - volverMenu.buttonCollision.width && touchPos.x < volverMenu.buttonCollision.x + volverMenu.buttonCollision.width) {
                    if (touchPos.y > volverMenu.buttonCollision.y - volverMenu.buttonCollision.height && touchPos.y < volverMenu.buttonCollision.y + volverMenu.buttonCollision.height) {

                        if (pause) {
                            pause = false;
                            menu = true;
                            tema = "";
                            backgroundTexture= new Texture(Gdx.files.internal("Fondo_Principal.jpg"));
                        }

                    }
                }
                // CLICK BOTON PAUSE
                if (touchPos.x > buttonPause.buttonCollision.x - buttonPause.buttonCollision.width && touchPos.x < buttonPause.buttonCollision.x + buttonPause.buttonCollision.width) {
                    if (touchPos.y > buttonPause.buttonCollision.y - buttonPause.buttonCollision.height && touchPos.y < buttonPause.buttonCollision.y + buttonPause.buttonCollision.height) {
                        pause = true;
                        canUndo=false;

                    }
                }
                // CLICK BOTON HELP ////
                if (touchPos.x > buttonHelp.buttonCollision.x - buttonHelp.buttonCollision.width && touchPos.x < buttonHelp.buttonCollision.x + buttonHelp.buttonCollision.width) {
                    if (touchPos.y > buttonHelp.buttonCollision.y - buttonHelp.buttonCollision.height && touchPos.y < buttonHelp.buttonCollision.y + buttonHelp.buttonCollision.height) {
                        if (pause) {
                            pause = false;
                            info = true;
                        }

                    }
                }
                
                if ("list".equals(tema)) {
                    //CLICK BOTON FUEGO LISTAS//
                    if (touchPos.x > buttonShooting.buttonCollision.x - buttonShooting.buttonCollision.width && touchPos.x < buttonShooting.buttonCollision.x + buttonShooting.buttonCollision.width) {
                        if (touchPos.y > buttonShooting.buttonCollision.y - buttonShooting.buttonCollision.height && touchPos.y < buttonShooting.buttonCollision.y + buttonShooting.buttonCollision.height) {
                            Shooting = true;
                        }
                    }
                }
                /// CLICK BOTON PARA CERRAR EL POPUP DE HELP ///
                if (touchPos.x > buttonClose.buttonCollision.x - buttonClose.buttonCollision.width && touchPos.x < buttonClose.buttonCollision.x + buttonClose.buttonCollision.width) {
                    if (touchPos.y > buttonClose.buttonCollision.y - buttonClose.buttonCollision.height && touchPos.y < buttonClose.buttonCollision.y + buttonClose.buttonCollision.height) {
                        if (pause) {
                            pause = false;
                            canUndo=true;
                        }
                        if (info) {
                            info = false;
                            canUndo=true;
                        }

                        if (win) {
                            String userName = "Prueba1";
                            Map<String,String> data = new HashMap<>();
                            data.put(Integer.toString(currentLevel), Integer.toString(levelScore));
                            fbase.insertData(tema, userName, data);
                            clearLevel();
                            initiateLevel(currentLevel + 1);
                            canUndo=true;
                        }

                        if (lose) {
                            clearLevel();
                            initiateLevel(currentLevel);
                            canUndo=true;

                        }
                        win = false;
                        lose = false;

                    }
                }

                //CLICK BOTON CTRZ //
                if (canUndo){
                if (touchPos.x > buttonCtrz.buttonCollision.x - buttonCtrz.buttonCollision.width && touchPos.x < buttonCtrz.buttonCollision.x + buttonCtrz.buttonCollision.width) {
                    if (touchPos.y > buttonCtrz.buttonCollision.y - buttonCtrz.buttonCollision.height && touchPos.y < buttonCtrz.buttonCollision.y + buttonCtrz.buttonCollision.height) {
                        UndoLast();
                    }
                }
                }
            }
            
            // MOVIMIENTO DE TABLAS
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
            }
        // FIN LOGICA PARA NIVEL !=0
        if (!pause && !info) sec++;
        if (levelScore>minScore && sec >=60 &&!win && !lose){
        levelScore-=10;
        sec=0;
        }

        }

        // ELEGIR TEMATICA DE NIVEES
        if (currentLevel == 0) {
            //CLICK BOTON NIVELES LINEALES
            if (touchPos.x > buttonLevelLinearDS.buttonCollision.x - buttonLevelLinearDS.buttonCollision.width && touchPos.x < buttonLevelLinearDS.buttonCollision.x + buttonLevelLinearDS.buttonCollision.width) {
                if (touchPos.y > buttonLevelLinearDS.buttonCollision.y - buttonLevelLinearDS.buttonCollision.height && touchPos.y < buttonLevelLinearDS.buttonCollision.y + buttonLevelLinearDS.buttonCollision.height) {
                    clearLevel();
                    initiateLevel(1);
                    tema = "list";
                    backgroundTexture= new Texture(Gdx.files.internal("Fondo Pilas-colas.jpg"));
                }
            }
            // CLICK BOTON NIVELES ARBOLES
            if (touchPos.x > buttonLevelTrees.buttonCollision.x - buttonLevelTrees.buttonCollision.width && touchPos.x < buttonLevelTrees.buttonCollision.x + buttonLevelTrees.buttonCollision.width) {
                if (touchPos.y > buttonLevelTrees.buttonCollision.y - buttonLevelTrees.buttonCollision.height && touchPos.y < buttonLevelTrees.buttonCollision.y + buttonLevelTrees.buttonCollision.height) {
                    //CUANDO HAYA NIVEL DE ARBOLES (DEBERIA SER EL NIVEL 4)
                    clearLevel();
                    initiateLevel(4);
                    tema = "tree";
                    backgroundTexture= new Texture(Gdx.files.internal("Fondo_Arboles.jpg"));

                }
            }
        }

        // PARTE DE RENDER 
        batch.enableBlending();
        batch.begin();
        
        Sprite sprbc = new Sprite(backgroundTexture);
        sprbc.setPosition(0,0);
        sprbc.setSize(800, 600);
        sprbc.draw(batch);
        
        if ("tree".equals(tema)) {
            Sprite sprtree = new Sprite(treeTexture);
            sprtree.setPosition(treeX,treeY);
            sprtree.setScale(1);
            sprtree.draw(batch);
        }
        //RENDERIZADO DE TABLAS
        if (currentLevel != 0) {
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
        }
        // RENDERIZADO DE BOTONES

        if (currentLevel != 0) {
            batch.draw(buttonRestart.buttonTexture, 0, 0);
            batch.draw(buttonPause.buttonTexture, 0, 555);
            batch.draw(buttonCtrz.buttonTexture, 200, 80);
            if ("list".equals(tema)) {
                // Render canon lista 
                Sprite spr= new Sprite(buttonCannon.cannonTexture);
                spr.setPosition(buttonCannon.cannonCollision.x, buttonCannon.cannonCollision.y);
                spr.setSize(buttonCannon.cannonCollision.width, buttonCannon.cannonCollision.height);
                spr.draw(batch);
                batch.draw(buttonShooting.buttonTexture, 10, 80);
            } else if ("tree".equals(tema)) {
                batch.draw(buttonDelete.buttonTexture, 0, 150);
            }
        } else {
            batch.draw(buttonLevelLinearDS.buttonTexture, 210, 270);
            font.draw(batch, "Linear Data Structures", 250, 300);
            batch.draw(buttonLevelTrees.buttonTexture, 210, 180);
            font.draw(batch, "Trees and Priority Heaps", 240, 210);
        }
        //RENDERIZADO DE EL POPUP DE INFO

        // Funcion de disparo
        if ("list".equals(tema)) {

            if (currentLevel != 0) {
                if (Shooting == true) {
                    if (Shootingindex >= listPlankCannon.getSize()) {
                        Lastfilled = Shootingindex;
                        Shooting = false;
                        MyDoubleLinkedList<Integer> listNumber = convertPlankToNumber(listPlankFired);
                        if (Shootingindex >= listPlankBridge.getSize()) {
                            //Aca se sabe si el usuario gana o pierde
                            if (OrderBridge.commit(listNumber) && currentLevel > 0) {
                                Gdx.app.log("E", "Ganar");
                                win = true;
                                canUndo=false;
                            } else {
                                Gdx.app.log("E", "Perder");
                                lose = true;
                                canUndo=false;
                            }
                        }

                    } else {
                        if (mode.equals("fifo")) {
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
                        } else if (mode.equals("lifo")) {

                            if (listPlankBridge.getSize() - Shootingindex >= 0) {
                                if (Math.abs(listPlankBridge.getData(Shootingindex).plankCollision.x - listPlankCannon.getData((listPlankCannon.getSize() + Lastfilled - 1) - Shootingindex).plankCollision.x) > 30 || Math.abs(listPlankBridge.getData(Shootingindex).plankCollision.y - listPlankCannon.getData((listPlankCannon.getSize() + Lastfilled - 1) - Shootingindex).plankCollision.y) > 30) {
                                    Canon.ShootPlankto((int) listPlankBridge.getData(Shootingindex).plankCollision.x, (int) listPlankBridge.getData(Shootingindex).plankCollision.y, buttonCannon.cannonCollision.x, buttonCannon.cannonCollision.y, Shootingtime, listPlankCannon.getData((listPlankCannon.getSize() + Lastfilled - 1) - Shootingindex));
                                    batch.draw(listPlankCannon.getData((listPlankCannon.getSize() + Lastfilled - 1) - Shootingindex).plankTexture, listPlankCannon.getData((listPlankCannon.getSize() + Lastfilled - 1) - Shootingindex).plankCollision.x, listPlankCannon.getData((listPlankCannon.getSize() + Lastfilled - 1) - Shootingindex).plankCollision.y);
                                    font.draw(batch, Integer.toString(listPlankCannon.getData((listPlankCannon.getSize() + Lastfilled - 1) - Shootingindex).plankNumber), listPlankCannon.getData((listPlankCannon.getSize() + Lastfilled - 1) - Shootingindex).plankCollision.x + 10, listPlankCannon.getData((listPlankCannon.getSize() + Lastfilled - 1) - Shootingindex).plankCollision.y + 70);
                                    Shootingtime += Gdx.graphics.getDeltaTime();

                                } else if ((listPlankCannon.getSize() + Lastfilled - 1) - Shootingindex >= 0) {
                                    listPlankCannon.getData((listPlankCannon.getSize() + Lastfilled - 1) - Shootingindex).plankCollision.x = listPlankBridge.getData(Shootingindex).plankCollision.x;
                                    listPlankCannon.getData((listPlankCannon.getSize() + Lastfilled - 1) - Shootingindex).plankCollision.y = listPlankBridge.getData(Shootingindex).plankCollision.y;
                                    listPlankFired.add(listPlankCannon.getData((listPlankCannon.getSize() + Lastfilled - 1) - Shootingindex));
                                    Shootingindex++;
                                    Shootingtime = 0;
                                }
                            }
                        }
                    }
                }
            }
        }
        if ("tree".equals(tema)) {
            // Rotacion del canon
            double rotx = -(double) Gdx.input.getX() / 4.8;

            // RENDER CANON ROTANDO
            if (currentLevel != 0 && buttonCannon != null) {
                Sprite sprite = new Sprite(buttonCannon.cannonTexture);
                sprite.setPosition(buttonCannon.cannonCollision.x, buttonCannon.cannonCollision.y);
                sprite.setSize(buttonCannon.cannonCollision.height, buttonCannon.cannonCollision.width);
                sprite.setOrigin(buttonCannon.cannonCollision.height/2, buttonCannon.cannonCollision.width/2);
                sprite.setRotation((float) rotx);
                sprite.draw(batch);
            }

            // DISPARAR A LAS HOJAS
            if (justTouched && touchPos.y > 100 && currentPick == null && !listLeaf.isEmpty() && !Shooting) {

                //Colision de huecos y hojas
                for (int i = 0; i < listHueco.getSize(); i++) {
                    hueco auxh = listHueco.getData(i);
                    if (touchPos.x > auxh.xpos && touchPos.x < auxh.xpos + auxh.collx) {
                        if (touchPos.y > auxh.ypos && touchPos.y < auxh.ypos + auxh.colly) {
                            objetivoArboles = auxh;
                            listHuecosUsados.add(listHueco.getData(i));
                            listHueco.delete(i);
                        }
                    }

                }

                if (touchPos.x > objetivoBorrar.xpos && touchPos.x < objetivoBorrar.xpos + objetivoBorrar.collx) {
                    if (touchPos.y > objetivoBorrar.ypos && touchPos.y < objetivoBorrar.ypos + objetivoBorrar.colly) {
                        objetivoArboles = objetivoBorrar;
                    }
                }
                if (objetivoArboles.xpos != 0) {
                    Shooting = true;
                    Leaf proyectile = listLeaf.getData(0);
                    listLeaf.delete(0);
                    proyectile.leafCollision.x = buttonCannon.cannonCollision.x;
                    proyectile.leafCollision.y = buttonCannon.cannonCollision.y;
                    listLeafFired.add(proyectile);

                }
            }
            if (!listLeafFired.isEmpty()) {
                if (Shooting) {
                    Canon.ShootLeafto(objetivoArboles.xpos, objetivoArboles.ypos + 60, Shootingtime, listLeafFired.getData(0));
                    batch.draw(listLeafFired.getData((listLeafFired.getSize() + Lastfilled - 1) - Shootingindex).leafTexture, listLeafFired.getData((listLeafFired.getSize() + Lastfilled - 1) - Shootingindex).leafCollision.x, listLeafFired.getData((listLeafFired.getSize() + Lastfilled - 1) - Shootingindex).leafCollision.y);
                    font.draw(batch, Integer.toString(listLeafFired.getData((listLeafFired.getSize() + Lastfilled - 1) - Shootingindex).leafNumber), listLeafFired.getData((listLeafFired.getSize() + Lastfilled - 1) - Shootingindex).leafCollision.x + 10, listLeafFired.getData((listLeafFired.getSize() + Lastfilled - 1) - Shootingindex).leafCollision.y + 70);
                    Shootingtime += Gdx.graphics.getDeltaTime();

                    if (Math.abs(listLeafFired.getData(listLeafFired.getSize() + Lastfilled - 1).leafCollision.y - objetivoArboles.ypos) < 10 && (Math.abs(listLeafFired.getData(listLeafFired.getSize() + Lastfilled - 1).leafCollision.x - objetivoArboles.xpos) < 20)) {
                        Shooting = false;
                        Shootingtime = 0;
                        if (objetivoArboles != objetivoBorrar) {
                            listLeafTree.add(listLeafFired.pop());
                            listLeafTreePlayerOrder[objetivoArboles.index] = listLeafTree.getLastData().leafNumber;
                        } else {
                            LastDeleted.add(listLeafFired.pop());
                        }
                        objetivoArboles = new hueco(0, 0, 0, 0, 0);
                    }
                    
                    // COMPROBAR SI EL USUARIO PERDIO O GANO EL NIVEL DE ARBOLES
                    if (listLeafTreeOrder.getSize() == listLeafTreeOrder.getSize() && listLeaf.isEmpty() && listLeafFired.isEmpty()) {
                        Test tester = new Test();
                        if (tester.checkTreesAsLists(listLeafTreeOrder, listLeafTreePlayerOrder)){
                        win = true;
                        lose = false;
                        canUndo=false;
                        }
                        else{
                            win = false;
                            lose = true;
                            canUndo=false;
                        }
                    }
                }
            }

            // Renderizado de hojas
            for (int i = 0; i < listLeaf.getSize(); i++) {
                if (listLeaf.getData(i) != null) {
                    batch.draw(listLeaf.getData(i).leafTexture, listLeaf.getData(i).leafCollision.x, listLeaf.getData(i).leafCollision.y);
                    font.draw(batch, Integer.toString(listLeaf.getData(i).leafNumber), listLeaf.getData(i).leafCollision.x + 32, listLeaf.getData(i).leafCollision.y+55);

                }
            }
            for (int i = 0; i < listLeafTree.getSize(); i++) {
                if (listLeafTree.getData(i) != null) {
                    batch.draw(listLeafTree.getData(i).leafTexture, listLeafTree.getData(i).leafCollision.x, listLeafTree.getData(i).leafCollision.y);
                    font.draw(batch, Integer.toString(listLeafTree.getData(i).leafNumber), listLeafTree.getData(i).leafCollision.x + 32, listLeafTree.getData(i).leafCollision.y + 55);

                }
            }
            //Renderizado de huecos
            
            if (!listHueco.isEmpty()){
            for (int i = 0; i < listHueco.getSize(); i++) {
                if (listHueco.getData(i) != null) {
                    Texture huecoTexture = new Texture(Gdx.files.internal("Nodo-faltante.png"));
                    batch.draw(huecoTexture, listHueco.getData(i).xpos, listHueco.getData(i).ypos);
                }
        
            }}
        }
        if (currentLevel!=0){
            fontScore.draw(batch,"Puntaje: " + Integer.toString(levelScore),290,585);
        }
        if (pause == true) {
            Sprite sprite = new Sprite(fondoPause);
            //Aqui se le pone un color en RGB,A. osea color y opacidad.
            sprite.setPosition(150, 100);
            sprite.setSize(500, 450);
            sprite.setColor(1, 1, 1, 0.8f);
            sprite.draw(batch);

            batch.draw(buttonClose.buttonTexture, 600, 503);
            batch.draw(buttonHelp.buttonTexture, 220, 250);
            font.draw(batch, "Info nivel", 310, 290);
            batch.draw(buttonLevelLinearDS.buttonTexture, 220, 370);
            font.draw(batch, "Volver al Menu", 290, 400);
        }

        if (menu == true) {

            currentLevel = 0;
            menu = false;
            pause = false;
            info = false;
            batch.draw(volverMenu.buttonTexture, 280, 400);
        }

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
        if (win == true) {
            batch.draw(buttonWin.buttonTexture, 53, 249);
            batch.draw(buttonClose.buttonTexture, 600, 503);

        }
        if (lose == true) {
            batch.draw(buttonLose.buttonTexture, 53, 249);
            batch.draw(buttonClose.buttonTexture, 600, 503);

        }
        batch.end();

        if (debug) {
            // PARTE DE DEBUG
            long total = Runtime.getRuntime().totalMemory();
            long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            Gdx.app.log("MEMORY", "total: " + total / 1048576 + "MB used: " + used / 1048576 + "MB");
        }

    }

    /**
     *esta funcion se llama al cerrar el juego, elimina los objetos automaticamente
     */
    @Override
    public void dispose() {
      clearLevel();
    }

    /**
     * Esta funcion borra de la memoria ram todos los objetos que se vean en pantalla
     */
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
        listLeaf.makeEmpty();
        listLeafFired.makeEmpty();
        listLeafTree.makeEmpty();
        listHueco.makeEmpty();
        listHuecosUsados.makeEmpty();
        buttonRestart.dispose();
        backgroundTexture.dispose();
        buttonCannon.dispose();
        buttonCtrz.dispose();
        buttonDelete.dispose();

        System.gc();

    }
    /**
     * Esta funcion inicia los niveles segun el numero de nivel enviado, donde cero es el menu.
     * @param level nivel a cargar
     */
    public void initiateLevel(int level) {
        Integer[] myArr;
        Integer[] myArr2;
        //ACA VA LA INFO DE NIVELES. 0=MENU;
        batch = new SpriteBatch();
        backgroundTexture = new Texture(Gdx.files.internal("Fondo_Principal.jpg"));
        volverMenu = new GenericButton(280, 400, 381, 44, "MainMenuButtons.jpg");
        buttonHelp = new GenericButton(220, 250, 381, 44, "Help.png");
        buttonPause = new GenericButton(0, 555, 50, 50, "Menu.png");
        buttonCtrz = new GenericButton(200, 80, 100, 100, "Re-Do.png");
        buttonDelete = new GenericButton(0, 150, 100, 100, "Basura.png");
        buttonClose = new GenericButton(600, 503, 50, 50, "buttonClose.png");
        buttonRestart = new GenericButton(0, 0, 50, 50, "Re-Try.png");
        buttonWin = new GenericButton(100, 100, 100, 100, "Win.png");
        buttonLose = new GenericButton(0, 0, 50, 50, "Lose.png");

        switch (level) {
            case 0:
                buttonLevelLinearDS = new GenericButton(210, 270, 381, 44, "MainMenuButtons.jpg");
                buttonLevelTrees = new GenericButton(210, 180, 381, 44, "MainMenuButtons.jpg");

                currentLevel = 0;
                mode = "fifo";
                fondoPause = new Texture(Gdx.files.internal("fondoPause.png"));
                infoTexture = new Texture(Gdx.files.internal("Info.png"));
                buttonCannon = new Canon(90, 70, 100, 100, "Canon_1.png");
                // buttonShooting = new GenericButton(10, 80, 50, 50, "shooting.png");
                break;
            case 1:
                levelScore=10000;
                minScore = 9000;
                mode = "fifo";
                infoTexture = new Texture(Gdx.files.internal("Info.png"));
                buttonCannon = new Canon(90, 70, 100, 100, "Canon_1.png");
                currentLevel = 1;
                buttonShooting = new GenericButton(10, 80, 50, 50, "shooting.png");
                myArr = strToArr("1,2,3");
                myArr2 = strToArr("1,2,3");

                OrderBridge = new Bridge<>(myArr);
                for (int i = 0; i < OrderBridge.getSize(); i++) {
                    createPlank(100 + i * 45, 400, 44, 117, listPlankBridge, myArr[i]);
                }
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
                myArr = strToArr("4,1,2,3,9");
                myArr2 = strToArr("4,1,2,3,9");
                OrderBridge = new Bridge<>(myArr);
                for (int i = 0; i < OrderBridge.getSize(); i++) {
                    createPlank(100 + i * 45, 400, 44, 117, listPlankBridge, myArr[i]);
                }
                for (int i = 0; i < myArr2.length; i++) {
                    createPlank(300 + i * 45, 0, 44, 117, listPlank, myArr2[i]);
                }
                break;
            case 3:
                mode = "fifo";
                infoTexture = new Texture(Gdx.files.internal("Info_dos.png"));
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
            case 4:
                listLeafTreePlayerOrder = new Integer[6];
                infoTexture = new Texture(Gdx.files.internal("Info_dos.png"));
                buttonCannon = new Canon(345, 5, 100, 100, "CanonTree.png");
                currentLevel = 4;
                treeTexture = new Texture(Gdx.files.internal("nivel_uno.png"));
                treeX=100;
                treeY=150;
                AVLTree<Integer> arbol = new AVLTree<>();
                arbol.insert(50);
                arbol.insert(40);
                arbol.insert(80);
                arbol.insert(35);
                arbol.insert(45);
                arbol.insert(90);
                listLeafTreeOrder = arbol.toArray();
                listLeafTreePlayerOrder[0] = 50;
                listLeafTreePlayerOrder[1] = 40;
                listLeafTreePlayerOrder[2] = 80;
                Leaf lef = new Leaf(500, 0, 10, 10, 35);
                listLeaf.add(lef);
                lef = new Leaf(600, 0, 10, 10, 45);
                listLeaf.add(lef);
                lef = new Leaf(700, 0, 10, 10, 90);
                listLeaf.add(lef);
                addHueco(125, 345, 3, 96, 96);
                addHueco(330, 345, 4, 96, 96);
                addHueco(623, 345, 5, 96, 96);

                Shootingtime = 0;

                break;
            case 5:
                listLeafTreePlayerOrder = new Integer[6];
                infoTexture = new Texture(Gdx.files.internal("Info.png"));
                infoTexture = new Texture(Gdx.files.internal("Info_dos.png"));
                buttonCannon = new Canon(345, 5, 100, 100, "CanonTree.png");
                currentLevel = 5;
                treeTexture = new Texture(Gdx.files.internal("nivel_uno.png"));
                AVLTree<Integer> arbol2 = new AVLTree<>();
                arbol2.insert(50);
                arbol2.insert(40);
                arbol2.insert(80);
                arbol2.insert(15);
                arbol2.insert(45);
                arbol2.insert(100);
                listLeafTreeOrder = arbol2.toArray();
                listLeafTreePlayerOrder[0] = 50;
                listLeafTreePlayerOrder[1] = 40;
                listLeafTreePlayerOrder[2] = 80;
                Leaf lef2 = new Leaf(500, 0, 10, 10, 45);
                listLeaf.add(lef2);
                lef2 = new Leaf(600, 0, 10, 10, 55);
                listLeaf.add(lef2);
                lef2 = new Leaf(700, 0, 10, 10, 73);
                listLeaf.add(lef2);
                lef2 = new Leaf(500, 75, 10, 10, 100);
                listLeaf.add(lef2);
                lef2 = new Leaf(600, 75, 10, 10, 15);
                listLeaf.add(lef2);
                addHueco(125, 345, 3, 128, 128);
                addHueco(330, 345, 4, 128, 128);
                addHueco(623, 345, 5, 128, 128);

                Shootingtime = 0;

                break;
            case 6:
                listLeafTreePlayerOrder = new Integer[11];
                infoTexture = new Texture(Gdx.files.internal("Info.png"));
                infoTexture = new Texture(Gdx.files.internal("Info_dos.png"));
                buttonCannon = new Canon(345, 5, 100, 100, "CanonTree.png");
                currentLevel = 6;
                treeTexture = new Texture(Gdx.files.internal("nivel2.png"));
                BinaryTree<Integer> arbol3 = new BinaryTree<>();
                arbol3.insert(39);
                arbol3.insert(22);
                arbol3.insert(71);
                arbol3.insert(17);
                arbol3.insert(29);
                arbol3.insert(100);
                arbol3.insert(15);
                arbol3.insert(20);
                arbol3.insert(35);
                arbol3.insert(80);
                arbol3.insert(120);
                listLeafTreeOrder = arbol3.toArray();
                listLeafTreePlayerOrder[0] = 39;
                listLeafTreePlayerOrder[1] = 22;
                listLeafTreePlayerOrder[2] = 71;
                listLeafTreePlayerOrder[3] = 17;
                listLeafTreePlayerOrder[4] = 29;
                listLeafTreePlayerOrder[5] = 100;
                Leaf lef3 = new Leaf(500, 0, 10, 10, 35);
                listLeaf.add(lef3);
                lef3 = new Leaf(600, 0, 10, 10, 40);
                listLeaf.add(lef3);
                lef3 = new Leaf(700, 0, 10, 10, 20);
                listLeaf.add(lef3);
                lef3 = new Leaf(500, 75, 10, 10, 15);
                listLeaf.add(lef3);
                lef3 = new Leaf(600, 75, 10, 10, 120);
                listLeaf.add(lef3);
                lef3 = new Leaf(700, 75, 10, 10, 68);
                listLeaf.add(lef3);
                lef3 = new Leaf(500, 150, 10, 10, 80);
                listLeaf.add(lef3);
                lef3 = new Leaf(600, 150, 10, 10, 25);
                listLeaf.add(lef3);

                addHueco(163, 310, 6, 45, 54);
                addHueco(265, 304, 7, 50, 49);
                addHueco(406, 303, 8, 40, 51);
                addHueco(461, 303, 9, 50, 51);
                addHueco(556, 290, 10, 63, 61);

                Shootingtime = 0;

                break;
        }

    }
    /**
     * Esta funcion crea un objeto hueco con posicion, tamanio y numero y lo aniade a una lista
     * @param x posicion en el eje x
     * @param y posicion en el eje y
     * @param i numero de hueco
     * @param cx tamanio del hueco en anchura
     * @param cy tamanio del hueco en altura
     */
    void addHueco(int x, int y, int i, int cx, int cy) {
        hueco auxhueco = new hueco(x, y, i, cx, cy);
        listHueco.add(auxhueco);
    }

    /**
     * Esta funcion permite deshacer el ultimo cambio realizado en el juego de tablas o de arboles
     */
    private void UndoLast() {
        if ("list".equals(tema)) {
            if (listPlankCannon.getSize() != 0) {
                Plank myLastPlank = listPlankCannon.pop();
                myLastPlank.plankCollision.x = 400;
                myLastPlank.plankCollision.y = 0;

                listPlank.add(myLastPlank);
            }
        } else if ("tree".equals(tema)) {
            if (!LastDeleted.isEmpty()) {
                Leaf myLastLeaf = LastDeleted.getData(0);
                myLastLeaf.leafCollision.x = 700;
                myLastLeaf.leafCollision.y = 150;

                listLeaf.add(myLastLeaf);
                LastDeleted.makeEmpty();
            }
            if (listLeafTree.getSize() != 0) {
                Leaf myLastLeaf = listLeafTree.pop();
                myLastLeaf.leafCollision.x = 700;
                myLastLeaf.leafCollision.y = 150;

                listLeaf.add(myLastLeaf);

            }
            if (!listHuecosUsados.isEmpty()) {
                listHueco.add(listHuecosUsados.pop());
            }

        }
    }
/**
 * Clase de objeto hueco para colocar en niveles de arboles, esta clase permite interactuar con espacios donde deben ir las hojas
 */
    public class hueco {
        
        int xpos;
        int ypos;
        int index;
        int collx;
        int colly;

        /**
         * Constructor de la clase
         * @param x posicion en eje x
         * @param y posicion en eje y
         * @param i numero entero
         * @param cx tamanio en anchura
         * @param cy tamanio en altura 
         */
        public hueco(int x, int y, int i, int cx, int cy) {
            xpos = x;
            ypos = y;
            index = i;
            collx = cx;
            colly = cy;
             
            
        }
    }
 // ****** FUNCIONES AUXILIARES PARA CONVERTIR DE UN TIPO DE ESTRUCTURA A OTRO ***** //
    /**
     * Esta funcion convierte una lista de clase tablas en una lista de numeros enteros
     * @param toConvert la lista de clase tablas que se desea convertir a enteros
     * @return devuelve una linkedlist de enteros
     */
    MyDoubleLinkedList<Integer> convertPlankToNumber(MyDoubleLinkedList<Plank> toConvert) {
        MyDoubleLinkedList<Integer> toReturn = new MyDoubleLinkedList<>();
        for (int i = 0; i < toConvert.getSize(); i++) {
            toReturn.add(toConvert.getData(i).plankNumber);
        }
        return toReturn;
    }

    /**
     * Esta funcion convierte una cadena de texto con numeros separados por comas en un array de enteros
     * @param str
     * @return devuelve un array de enteros
     */
    Integer[] strToArr(String str) {
        String[] myarr = str.split(",");
        Integer[] toReturn = new Integer[myarr.length];

        for (int i = 0; i < myarr.length; i++) {
            toReturn[i] = Integer.parseInt(myarr[i]);
        }
        return toReturn;
    }
}


   
