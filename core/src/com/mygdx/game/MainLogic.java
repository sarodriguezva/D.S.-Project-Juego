package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.google.cloud.firestore.DocumentReference;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mygdx.game.DataStructures.*;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que contiene toda la logica del juego y su funcionamiento interno
 *
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
    GenericButton buttonNextLevel = null;
    GenericButton buttonRetry = null;
    GenericButton buttonAcceptPlay = null;
    GenericButton buttonPlay = null;
    GenericButton buttonScore = null;
    GenericButton buttonExit = null;
    GenericButton buttonCredits = null;
    GenericButton buttonBack = null;

    // TEXTURA Y OBJETOS PARA BOTONES
    private Texture infoTexture;
    private Texture fondoPause;
    private Texture treeTexture;
    // TEXTURAS DE FONDO
    private SpriteBatch batch;
    Sprite sprbc;
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
    boolean isRegistering;

    // VARIABLES DE TEXTO
    String tema;
    String mode;
    String selectedScore;
    // VARIABLE DE ENTEROS
    int Lastfilled;
    int currentLevel = 0;
    int sec = 0;
    int levelScore = 0;
    int minScore = 0;
    int treeX = 0;
    int treeY = 0;
    int waitTime = 0;
    // MAX LIST LEVEL NOS DICE CUANTOS NIVELES DE LISTA HAY
    int maxListLevel[] = new int[3];
    int maxScoreLevel[];
    BinaryHeap<MyTuple<Integer,String>> scoreHeap = new BinaryHeap<>(false);
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
    //Lista posiciones posibles para las hojas
    private MyDoubleLinkedList<MyTuple<Integer,Integer>> ListaPosicionesHojas = new MyDoubleLinkedList<>();
    private int PrimeraPos = 8;
    // Lista de scores
    private MyDoubleLinkedList<ScoreButton> listButtonScore = new MyDoubleLinkedList<>();
    private MyDoubleLinkedList<MyTuple<String,String>> res =new MyDoubleLinkedList<>();
    hueco objetivoArboles = new hueco(0, 0, 0, 0, 0);
    hueco objetivoBorrar = new hueco(1, 1, 0, 64, 64);
    private BitmapFont font;
    private BitmapFont fontScore;
    String userName = "NoName";
    MyStack<Character> userNameStack = new MyStack<>();
    Firebase fbase;
    DocumentReference docRef;

    // FUNCION PARA GUARDAR TEXTO EN UN ARCHIVO .TXT
    /**
     * Esta funcion guarda una cadena de texto en un archivo txt llamado
     * data.txt
     *
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
     * Esta funcion se llama al iniciar el juego, es la encargada de cargar
     * variables antes de renderizar la pantalla
     */
    public void create() {

        // SE CREA UN NUEVO PROCESADOR DE TECLADO PARA SABER QUE HACER CON LAS TECLAS.
        Gdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean touchDragged(int arg0, int arg1, int arg2) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                return false;
            }

            public boolean scrolled(int arg0) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean mouseMoved(int arg0, int arg1) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean keyUp(int arg0) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean keyTyped(char arg0) {

                if (isRegistering) {
                    int key = Integer.valueOf(arg0);
                    if (key == 127 || key == 8) {
                        userNameStack.pop();
                    } else if (userNameStack.getSize() < 11) {
                        if (key > 31 && key < 123) {
                            userNameStack.add(arg0);
                        }
                    }
                    userName = userNameStack.toStringUnite();
                }
                return false;
            }

            @Override
            public boolean keyDown(int arg0) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean scrolled(float arg0, float arg1) {
                return false;

            }
        });
        // FIN PROCESADOR TECLADO

        // ESTA FUNCION ES MUY IMPORTANTE, SE LLAMA AL INICIO DEL JUEGO Y SE ENCARGA DE INICIALIZARLO TODO
        // SE CREA LA CAMARA  Y SE PONE EN ORTOGONAL Y LAS DIMENSIONES DE LA PANTALLA
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        //Se inicia el nivel y se pone la fuente que se va a usar
        font = new BitmapFont(Gdx.files.internal("segoeprint.fnt"));
        fontScore = new BitmapFont(Gdx.files.internal("test.fnt"));

        // max ListLevel es donde cambia de tema. 0 pilas colas, 1 arboles, 2 grafos
        maxListLevel[0] = 0;
        maxListLevel[1] = 3;
        maxListLevel[2] = 6;
        maxScoreLevel = new int[10];
        
        //Se crean las tuplas para la lista de posiciones para las hojas
        ListaPosicionesHojas.add(new MyTuple<Integer,Integer>(500, 0));
        ListaPosicionesHojas.add(new MyTuple<Integer,Integer>(600, 0));
        ListaPosicionesHojas.add(new MyTuple<Integer,Integer>(700, 0));
        ListaPosicionesHojas.add(new MyTuple<Integer,Integer>(500, 75));
        ListaPosicionesHojas.add(new MyTuple<Integer,Integer>(600, 75));
        ListaPosicionesHojas.add(new MyTuple<Integer,Integer>(700, 75));
        ListaPosicionesHojas.add(new MyTuple<Integer,Integer>(500, 150));
        ListaPosicionesHojas.add(new MyTuple<Integer,Integer>(600, 150));
        ListaPosicionesHojas.add(new MyTuple<Integer,Integer>(700, 150));
        if (debug == false) {
            initiateLevel(-100);
        } // PRUEBAS DE TIEMPOS Y MEMORIA PARA DISTINTAS ESTRUCTURAS
        /// DATA TIME PARA DISTINTAS ESTRUCTURAS DE DATOS
        else {

            try {
                int m = 20000;
                int b = 20000 - 20000;
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
     * Esta funcion se utiliza para crear una tabla con posicion, tamanio y
     * numero ademas lo aniade a una lista de tablas
     *
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
     * Esta funcion es la encargada de la logica del juego y de mostrar todo en
     * pantalla, se llama en un ciclo infinito hasta cerrar el juego
     */
    public void render() {

        // ESTA FUNCION MUESTRA TODO EN PANTALLA, TAMBIEN ES UNA FUNCION QUE SE LLAMA REPETIDAMENTE
        // aca se usa lo de batch es un objeto que se encarga de manejar el tema de opengl para renderizar todo de manera eficiente
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.enableBlending();
        batch.begin();
        sprbc = new Sprite(backgroundTexture);
        sprbc.setPosition(0, 0);
        sprbc.setSize(800, 600);
        sprbc.draw(batch);
        batch.end();

        //justTouched me dice si el mouse ha sido recientemente presionado, leftpressed si esta continuamente presionado
        justTouched = Gdx.input.justTouched();
        leftPressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);

        // Si esta continuamente presionado, guardeme las coordenadas del mouse
        if (leftPressed) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
        }
        if (waitTime > 0) {
            waitTime--;
        }
        if (waitTime == 1) {
            waitTime = 0;
            touchPos.set(-10,-10,-10);
        }

        // Todos los botones de menu tendran una variable wait para no tomar colision hasta que pase ese tiempo
            if (currentLevel == -100) {

                batch.enableBlending();
                batch.begin();

                font.draw(batch, userName, 500, 100);
                font.draw(batch, "Su nombre de usuario: ", 100, 100);

                batch.draw(buttonAcceptPlay.buttonTexture, buttonAcceptPlay.buttonCollision.x, buttonAcceptPlay.buttonCollision.y);
                batch.end();

                // Si le da al boton Jugar, isregistering = false. Queda el userName guardado, posiblemente la edad y lo manda al nivel 0 menu
                if (waitTime == 0) {
                // CLICK BOTON ACCEPT PLAY
                if (touchPos.x > buttonAcceptPlay.buttonCollision.x - buttonAcceptPlay.buttonCollision.width && touchPos.x < buttonAcceptPlay.buttonCollision.x + buttonAcceptPlay.buttonCollision.width) {
                    if (touchPos.y > buttonAcceptPlay.buttonCollision.y - buttonAcceptPlay.buttonCollision.height && touchPos.y < buttonAcceptPlay.buttonCollision.y + buttonAcceptPlay.buttonCollision.height) {
                        clearLevel();
                        initiateLevel(-4);
                        isRegistering = false;
                    }
                }
                }
            }
            // FIN CURRENT LEVEL = -100

            // MENU PRINCIPAL
            if (currentLevel == -4) {
                batch.enableBlending();
                batch.begin();
                batch.draw(buttonPlay.buttonTexture, buttonPlay.buttonCollision.x, buttonPlay.buttonCollision.y);
                batch.draw(buttonScore.buttonTexture, buttonScore.buttonCollision.x, buttonScore.buttonCollision.y);
                batch.draw(buttonExit.buttonTexture, buttonExit.buttonCollision.x, buttonExit.buttonCollision.y);
                batch.draw(buttonCredits.buttonTexture, buttonCredits.buttonCollision.x, buttonCredits.buttonCollision.y);
                batch.end();
                if (waitTime == 0) {
                // CLICK BOTON PLAY
                if (touchPos.x > buttonPlay.buttonCollision.x - buttonPlay.buttonCollision.width && touchPos.x < buttonPlay.buttonCollision.x + buttonPlay.buttonCollision.width) {
                    if (touchPos.y > buttonPlay.buttonCollision.y - buttonPlay.buttonCollision.height && touchPos.y < buttonPlay.buttonCollision.y + buttonPlay.buttonCollision.height) {
                        clearLevel();
                        initiateLevel(0);
                    }
                }

                //CLICK BOTON SCORE
                if (touchPos.x > buttonScore.buttonCollision.x - buttonScore.buttonCollision.width && touchPos.x < buttonScore.buttonCollision.x + buttonScore.buttonCollision.width) {
                    if (touchPos.y > buttonScore.buttonCollision.y - buttonScore.buttonCollision.height && touchPos.y < buttonScore.buttonCollision.y + buttonScore.buttonCollision.height) {
                        clearLevel();
                        initiateLevel(-3);
                    }
                }
                // CLICK BOTON EXIT
                if (touchPos.x > buttonExit.buttonCollision.x - buttonExit.buttonCollision.width && touchPos.x < buttonExit.buttonCollision.x + buttonExit.buttonCollision.width) {
                    if (touchPos.y > buttonExit.buttonCollision.y - buttonExit.buttonCollision.height && touchPos.y < buttonExit.buttonCollision.y + buttonExit.buttonCollision.height) {
                        Gdx.app.exit();
                    }
                }
                // CLICK BOTON CREDITOS
                if (touchPos.x > buttonCredits.buttonCollision.x - buttonCredits.buttonCollision.width && touchPos.x < buttonCredits.buttonCollision.x + buttonCredits.buttonCollision.width) {
                    if (touchPos.y > buttonCredits.buttonCollision.y - buttonCredits.buttonCollision.height && touchPos.y < buttonCredits.buttonCollision.y + buttonCredits.buttonCollision.height) {
                        clearLevel();
                        initiateLevel(-5);
                    }
                }
                }
            }
// FIN CURRENT LEVEL = -4
            // NIVEL PARA MOSTRAR PUNTAJES
            if (currentLevel == -3) {
                batch.enableBlending();
                batch.begin();

                for (int i = 0; i < listButtonScore.getSize(); i++) {
                    ScoreButton bt = listButtonScore.getData(i);
                    batch.draw(bt.buttonTexture, bt.buttonCollision.x, bt.buttonCollision.y);
                    String score = bt.buttonTuple.value;
                    JsonElement root = new JsonParser().parse(res.getData(bt.buttonTuple.key).value);
                    String name = root.getAsJsonObject().get("name").getAsString();
                    fontScore.draw(batch, name, bt.buttonCollision.x, bt.buttonCollision.y);
                    fontScore.draw(batch, score, bt.buttonCollision.x + 200, bt.buttonCollision.y + 50);
                }
                batch.draw(buttonBack.buttonTexture, buttonBack.buttonCollision.x, buttonBack.buttonCollision.y);
                batch.end();
                if (waitTime == 0) {
                    // Al dar click en un boton
                    for (int i = 0; i < listButtonScore.getSize(); i++) {
                        ScoreButton bt = listButtonScore.getData(i);
                        if (touchPos.x > bt.buttonCollision.x - bt.buttonCollision.width && touchPos.x < bt.buttonCollision.x + bt.buttonCollision.width) {
                            if (touchPos.y > bt.buttonCollision.y - bt.buttonCollision.height && touchPos.y < bt.buttonCollision.y + bt.buttonCollision.height) {
                                clearLevel();
                                selectedScore = res.getData(Integer.valueOf(bt.buttonTuple.key)).value;
                                initiateLevel(-6);
                            }
                        }
                    }

                    // CLICK BOTON BACK
                    if (touchPos.x > buttonBack.buttonCollision.x - buttonBack.buttonCollision.width && touchPos.x < buttonBack.buttonCollision.x + buttonBack.buttonCollision.width) {
                        if (touchPos.y > buttonBack.buttonCollision.y - buttonBack.buttonCollision.height && touchPos.y < buttonBack.buttonCollision.y + buttonBack.buttonCollision.height) {
                            clearLevel();
                            initiateLevel(-4);
                        }
                    }
                }
            }
            // FIN CURRENT LEVEL =-3
        // ACA VAN LOS CREDITOS
            if (currentLevel==-5){
                batch.enableBlending();
                batch.begin();
                batch.draw(buttonBack.buttonTexture, buttonBack.buttonCollision.x, buttonBack.buttonCollision.y);
                batch.end();
                // CLICK BOTON BACK
                if (waitTime == 0) {
                if (touchPos.x > buttonBack.buttonCollision.x - buttonBack.buttonCollision.width && touchPos.x < buttonBack.buttonCollision.x + buttonBack.buttonCollision.width) {
                    if (touchPos.y > buttonBack.buttonCollision.y - buttonBack.buttonCollision.height && touchPos.y < buttonBack.buttonCollision.y + buttonBack.buttonCollision.height) {
                        clearLevel();
                        initiateLevel(-4);
                    }
                }
                }
            }

            // ACA VA SI SE CLICKEA ALGUN PUNTAJE

            if (currentLevel == -6) {

                // CLICK BOTON BACK
                if (waitTime == 0) {
                    if (touchPos.x > buttonBack.buttonCollision.x - buttonBack.buttonCollision.width && touchPos.x < buttonBack.buttonCollision.x + buttonBack.buttonCollision.width) {
                        if (touchPos.y > buttonBack.buttonCollision.y - buttonBack.buttonCollision.height && touchPos.y < buttonBack.buttonCollision.y + buttonBack.buttonCollision.height) {
                            clearLevel();
                            initiateLevel(-3);
                        }
                    }
                }
                JsonElement root = new JsonParser().parse(selectedScore);
                String name = root.getAsJsonObject().get("name").getAsString();
                String scoreglobal = root.getAsJsonObject().get("9").getAsString();
                String lvl1 = root.getAsJsonObject().get("0").getAsString();
                String lvl2 = root.getAsJsonObject().get("1").getAsString();
                String lvl3 = root.getAsJsonObject().get("2").getAsString();
                String lvl4 = root.getAsJsonObject().get("3").getAsString();
                String lvl5 = root.getAsJsonObject().get("4").getAsString();
                String lvl6 = root.getAsJsonObject().get("5").getAsString();
                String lvl7 = root.getAsJsonObject().get("6").getAsString();
                String lvl8 = root.getAsJsonObject().get("7").getAsString();
                String lvl9 = root.getAsJsonObject().get("8").getAsString();

                batch.enableBlending();
                batch.begin();
                batch.draw(buttonBack.buttonTexture, buttonBack.buttonCollision.x, buttonBack.buttonCollision.y);
                fontScore.draw(batch, "Nombre: " + name, 200, 600);
                fontScore.draw(batch, "Puntaje global: " + scoreglobal, 200, 550);
                fontScore.draw(batch, "level 1: " + lvl1, 200, 500);
                fontScore.draw(batch, "level 2: " + lvl2, 200, 450);
                fontScore.draw(batch, "level 3: " + lvl3, 200, 400);
                fontScore.draw(batch, "level 4: " + lvl4, 200, 350);
                fontScore.draw(batch, "level 5: " + lvl5, 200, 300);
                fontScore.draw(batch, "level 6: " + lvl6, 200, 250);
                fontScore.draw(batch, "level 7: " + lvl7, 200, 200);
                fontScore.draw(batch, "level 8: " + lvl8, 200, 150);
                fontScore.draw(batch, "level 9: " + lvl9, 200, 100);
                batch.end();
            }

            // ELEGIR TEMATICA DE NIVEES
            if (currentLevel == 0) {
                if (waitTime == 0) {
                    //CLICK BOTON NIVELES LINEALES
                    if (touchPos.x > buttonLevelLinearDS.buttonCollision.x - buttonLevelLinearDS.buttonCollision.width && touchPos.x < buttonLevelLinearDS.buttonCollision.x + buttonLevelLinearDS.buttonCollision.width) {
                        if (touchPos.y > buttonLevelLinearDS.buttonCollision.y - buttonLevelLinearDS.buttonCollision.height && touchPos.y < buttonLevelLinearDS.buttonCollision.y + buttonLevelLinearDS.buttonCollision.height) {
                            clearLevel();
                            tema = "list";
                            initiateLevel(1);

                        }
                    }
                    // CLICK BOTON NIVELES ARBOLES
                    if (touchPos.x > buttonLevelTrees.buttonCollision.x - buttonLevelTrees.buttonCollision.width && touchPos.x < buttonLevelTrees.buttonCollision.x + buttonLevelTrees.buttonCollision.width) {
                        if (touchPos.y > buttonLevelTrees.buttonCollision.y - buttonLevelTrees.buttonCollision.height && touchPos.y < buttonLevelTrees.buttonCollision.y + buttonLevelTrees.buttonCollision.height) {
                            //CUANDO HAYA NIVEL DE ARBOLES (DEBERIA SER EL NIVEL 4)
                            clearLevel();
                            tema = "tree";
                            waitTime = 10;
                            initiateLevel(4);

                        }
                    }
                }
                // DRAW TEXTO PARA ELEGIR TEMATICA
                batch.enableBlending();
                batch.begin();
                batch.draw(buttonLevelLinearDS.buttonTexture, 210, 270);
                font.draw(batch, "Linear Data Structures", 250, 300);
                batch.draw(buttonLevelTrees.buttonTexture, 210, 180);
                font.draw(batch, "Trees and Priority Heaps", 240, 210);
                batch.end();
            }
            // FIN CURRENT LEVEL 0


        // PARTE DE LOGICA
        //justTouched me dice si el mouse ha sido recientemente presionado, leftpressed si esta continuamente presionado
        justTouched = Gdx.input.justTouched();
        leftPressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);

        // Si esta continuamente presionado, guardeme las coordenadas del mouse
        if (leftPressed) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
        }

        if (currentLevel >0) {
            // Si ha sido recientemente presionado compruebe si ya esta arrastrando un objeto, si no hay objeto, busca el que el mouse presiona en esas coordenadas.
            if (justTouched && currentPick == null) {
                ///AQUI VA SI CLICKEA ALGO MIENTRAS NO ARRASTRA NADA

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
                            if (!pause && !win && !lose && !info) {
                                clearLevel();
                                initiateLevel(currentLevel);
                            }
                        }
                    }
                }
                // CLICK BOTON VOLVER AL MENU
                if (touchPos.x > volverMenu.buttonCollision.x - volverMenu.buttonCollision.width && touchPos.x < volverMenu.buttonCollision.x + volverMenu.buttonCollision.width) {
                    if (touchPos.y > volverMenu.buttonCollision.y - volverMenu.buttonCollision.height && touchPos.y < volverMenu.buttonCollision.y + volverMenu.buttonCollision.height) {

                        if (pause || win || lose) {
                            pause = false;
                            menu = true;
                            tema = "";
                            win = false;
                            lose = false;
                            backgroundTexture = new Texture(Gdx.files.internal("Fondo_Principal.jpg"));
                            canUndo = true;
                        }

                    }
                }
                // CLICK BOTON PAUSE
                if (touchPos.x > buttonPause.buttonCollision.x - buttonPause.buttonCollision.width && touchPos.x < buttonPause.buttonCollision.x + buttonPause.buttonCollision.width) {
                    if (touchPos.y > buttonPause.buttonCollision.y - buttonPause.buttonCollision.height && touchPos.y < buttonPause.buttonCollision.y + buttonPause.buttonCollision.height) {
                        if (!info && !win && !lose) {
                            pause = true;
                        }
                        canUndo = false;
                    }
                }
                // CLICK BOTON HELP ////
                if (touchPos.x > buttonHelp.buttonCollision.x - buttonHelp.buttonCollision.width && touchPos.x < buttonHelp.buttonCollision.x + buttonHelp.buttonCollision.width) {
                    if (touchPos.y > buttonHelp.buttonCollision.y - buttonHelp.buttonCollision.height && touchPos.y < buttonHelp.buttonCollision.y + buttonHelp.buttonCollision.height) {
                        if (!pause && !win && !lose) {
                            info = true;
                            canUndo = false;
                        }
                    }
                }

                if ("list".equals(tema)) {
                    //CLICK BOTON FUEGO LISTAS//
                    if (touchPos.x > buttonShooting.buttonCollision.x - buttonShooting.buttonCollision.width && touchPos.x < buttonShooting.buttonCollision.x + buttonShooting.buttonCollision.width) {
                        if (touchPos.y > buttonShooting.buttonCollision.y - buttonShooting.buttonCollision.height && touchPos.y < buttonShooting.buttonCollision.y + buttonShooting.buttonCollision.height) {
                            if (!pause && !info && !lose && !win) {
                                Shooting = true;
                            }
                        }
                    }
                }
                /// CLICK BOTON PARA CERRAR EL POPUP DE HELP ///
                if (touchPos.x > buttonClose.buttonCollision.x - buttonClose.buttonCollision.width && touchPos.x < buttonClose.buttonCollision.x + buttonClose.buttonCollision.width) {
                    if (touchPos.y > buttonClose.buttonCollision.y - buttonClose.buttonCollision.height && touchPos.y < buttonClose.buttonCollision.y + buttonClose.buttonCollision.height) {
                        if (pause) {
                            pause = false;
                            canUndo = true;
                        }
                        if (info) {
                            info = false;
                            canUndo = true;
                        }

                    }
                }

                //CLICK BOTON CTRZ //
                if (canUndo) {
                    if (touchPos.x > buttonCtrz.buttonCollision.x - buttonCtrz.buttonCollision.width && touchPos.x < buttonCtrz.buttonCollision.x + buttonCtrz.buttonCollision.width) {
                        if (touchPos.y > buttonCtrz.buttonCollision.y - buttonCtrz.buttonCollision.height && touchPos.y < buttonCtrz.buttonCollision.y + buttonCtrz.buttonCollision.height) {
                            UndoLast();
                        }
                    }
                }
            }

            // CLICK BOTON SIGUIENTE NIVEL //
            if (touchPos.x > buttonNextLevel.buttonCollision.x - buttonNextLevel.buttonCollision.width && touchPos.x < buttonNextLevel.buttonCollision.x + buttonNextLevel.buttonCollision.width) {
                if (touchPos.y > buttonNextLevel.buttonCollision.y - buttonNextLevel.buttonCollision.height && touchPos.y < buttonNextLevel.buttonCollision.y + buttonNextLevel.buttonCollision.height) {

                    if (win) {

                        if (currentLevel == maxListLevel[1]) {
                            tema = "tree";
                        }
                        if (currentLevel == maxListLevel[2]) {
                            tema = "graphs";
                        }

                        waitTime = 17;
                        clearLevel();
                        initiateLevel(currentLevel + 1);
                        canUndo = true;
                        win = false;
                        lose = false;

                    }
                }
            }
            // CLICK BOTON REPETIR NIVEL (PERDIO NIVEL)//
            if (touchPos.x > buttonRetry.buttonCollision.x - buttonRetry.buttonCollision.width && touchPos.x < buttonRetry.buttonCollision.x + buttonRetry.buttonCollision.width) {
                if (touchPos.y > buttonRetry.buttonCollision.y - buttonRetry.buttonCollision.height && touchPos.y < buttonRetry.buttonCollision.y + buttonRetry.buttonCollision.height) {

                    if (lose) {
                        clearLevel();
                        initiateLevel(currentLevel);
                        canUndo = true;
                        win = false;
                        lose = false;
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
                if (currentPick.plankCollision.x > 800 - 32) {
                    currentPick.plankCollision.x = 800 - 32;

                }
                if (currentPick.plankCollision.x < 32) {
                    currentPick.plankCollision.x = 32;
                }
                // Limites en y
                if (currentPick.plankCollision.y > 64 + 16) {
                    currentPick.plankCollision.y = 64 + 16;

                }
                if (currentPick.plankCollision.y < 54) {
                    currentPick.plankCollision.y = 54;
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
            if (!pause && !info) {
                sec++;
            }
            if (levelScore > minScore && sec >= 60 && !win && !lose) {
                levelScore -= 10;
                sec = 0;
            }

            // PARTE DE RENDER 
            batch.enableBlending();
            batch.begin();
            if ("tree".equals(tema)) {
                Sprite sprtree = new Sprite(treeTexture);
                sprtree.setPosition(treeX, treeY);
                sprtree.setScale(1);
                sprtree.draw(batch);

            }
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
                    batch.draw(listPlank.getData(i).plankTexture, listPlank.getData(i).plankCollision.x - 28, listPlank.getData(i).plankCollision.y - 32);
                    font.draw(batch, Integer.toString(listPlank.getData(i).plankNumber), listPlank.getData(i).plankCollision.x + 10, listPlank.getData(i).plankCollision.y + 70);

                }
            }

            for (int i = 0; i < listPlankFired.getSize(); i++) {

                batch.draw(listPlankFired.getData(i).plankTexture, listPlankFired.getData(i).plankCollision.x, listPlankFired.getData(i).plankCollision.y);
                font.draw(batch, Integer.toString(listPlankFired.getData(i).plankNumber), listPlankFired.getData(i).plankCollision.x + 10, listPlankFired.getData(i).plankCollision.y + 70);

            }

            // RENDERIZADO DE BOTONES
            batch.draw(buttonRestart.buttonTexture, buttonRestart.buttonCollision.x - 32, buttonRestart.buttonCollision.y - 32);
            batch.draw(buttonPause.buttonTexture, buttonPause.buttonCollision.x - 32, buttonPause.buttonCollision.y - 32);
            batch.draw(buttonCtrz.buttonTexture, buttonCtrz.buttonCollision.x - 32, buttonCtrz.buttonCollision.y - 32);
            if ("list".equals(tema)) {
                // Render canon lista 
                Sprite spr = new Sprite(buttonCannon.cannonTexture);
                spr.setPosition(buttonCannon.cannonCollision.x, buttonCannon.cannonCollision.y);
                spr.setSize(buttonCannon.cannonCollision.width, buttonCannon.cannonCollision.height);
                spr.draw(batch);
                batch.draw(buttonShooting.buttonTexture, buttonShooting.buttonCollision.x - 32, buttonShooting.buttonCollision.y - 32);
            } else if ("tree".equals(tema)) {
                batch.draw(buttonDelete.buttonTexture, buttonDelete.buttonCollision.x - 32, buttonDelete.buttonCollision.y - 32);
            }
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
                                    win = true;
                                    canUndo = false;
                                    saveScore();
                                } else {
                                    lose = true;
                                    canUndo = false;

                                }
                                volverMenu = new GenericButton(100, 100, 381, 44, "MainMenuButtons.jpg");
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
                if (waitTime == 0) {
                    // RENDER CANON ROTANDO
                    if (currentLevel != 0 && buttonCannon != null) {
                        Sprite sprite = new Sprite(buttonCannon.cannonTexture);
                        sprite.setPosition(buttonCannon.cannonCollision.x, buttonCannon.cannonCollision.y);
                        sprite.setSize(buttonCannon.cannonCollision.height, buttonCannon.cannonCollision.width);
                        sprite.setOrigin(buttonCannon.cannonCollision.height / 2, buttonCannon.cannonCollision.width / 2);
                        if (canUndo) {
                            sprite.setRotation((float) rotx);
                        }
                        sprite.draw(batch);
                    }

                    // DISPARAR A LAS HOJAS
                    if (justTouched && currentPick == null && !listLeaf.isEmpty() && !Shooting && canUndo) {

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

                            if (Math.abs(listLeafFired.getData(listLeafFired.getSize() + Lastfilled - 1).leafCollision.y - objetivoArboles.ypos) < 25 && (Math.abs(listLeafFired.getData(listLeafFired.getSize() + Lastfilled - 1).leafCollision.x - objetivoArboles.xpos) < 25)) {
                                Shooting = false;
                                Shootingtime = 0;
                                listLeafFired.getData(listLeafFired.getSize() + Lastfilled - 1).leafCollision.y = objetivoArboles.ypos;
                                listLeafFired.getData(listLeafFired.getSize() + Lastfilled - 1).leafCollision.x = objetivoArboles.xpos;

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
                                if (tester.checkTreesAsLists(listLeafTreeOrder, listLeafTreePlayerOrder)) {
                                    win = true;
                                    lose = false;
                                    canUndo = false;
                                    saveScore();
                                } else {
                                    win = false;
                                    lose = true;
                                    canUndo = false;
                                }
                                volverMenu = new GenericButton(100, 100, 381, 44, "MainMenuButtons.jpg");
                            }

                        }
                    }

                    // Renderizado de hojas
                    for (int i = 0; i < listLeaf.getSize(); i++) {
                        if (listLeaf.getData(i) != null) {
                            batch.draw(listLeaf.getData(i).leafTexture, listLeaf.getData(i).leafCollision.x, listLeaf.getData(i).leafCollision.y);
                            font.draw(batch, Integer.toString(listLeaf.getData(i).leafNumber), listLeaf.getData(i).leafCollision.x + 32, listLeaf.getData(i).leafCollision.y + 55);

                        }
                    }
                    for (int i = 0; i < listLeafTree.getSize(); i++) {
                        if (listLeafTree.getData(i) != null) {
                            batch.draw(listLeafTree.getData(i).leafTexture, listLeafTree.getData(i).leafCollision.x, listLeafTree.getData(i).leafCollision.y);
                            font.draw(batch, Integer.toString(listLeafTree.getData(i).leafNumber), listLeafTree.getData(i).leafCollision.x + 32, listLeafTree.getData(i).leafCollision.y + 55);

                        }
                    }
                    //Renderizado de huecos

                    if (!listHueco.isEmpty()) {
                        for (int i = 0; i < listHueco.getSize(); i++) {
                            if (listHueco.getData(i) != null) {
                                Texture huecoTexture = new Texture(Gdx.files.internal("Nodo-faltante.png"));
                                batch.draw(huecoTexture, listHueco.getData(i).xpos, listHueco.getData(i).ypos);
                            }

                        }
                    }
                }
            }
            fontScore.draw(batch, "Puntaje: " + Integer.toString(levelScore), 290, 585);
            batch.draw(buttonHelp.buttonTexture, buttonHelp.buttonCollision.x - 32, buttonHelp.buttonCollision.y - 32);


            if (pause == true) {
                Sprite sprite = new Sprite(fondoPause);
                //Aqui se le pone un color en RGB,A. osea color y opacidad.
                sprite.setPosition(150, 100);
                sprite.setSize(500, 450);
                sprite.setColor(1, 1, 1, 0.8f);
                sprite.draw(batch);

                batch.draw(buttonClose.buttonTexture, 600, 503);
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
                batch.draw(buttonNextLevel.buttonTexture, buttonNextLevel.buttonCollision.x, buttonNextLevel.buttonCollision.y);
                batch.draw(volverMenu.buttonTexture, volverMenu.buttonCollision.x, volverMenu.buttonCollision.y);
            }
            if (lose == true) {
                batch.draw(buttonLose.buttonTexture, 53, 249);
                batch.draw(buttonRetry.buttonTexture, buttonRetry.buttonCollision.x, buttonRetry.buttonCollision.y);
                batch.draw(volverMenu.buttonTexture, volverMenu.buttonCollision.x, volverMenu.buttonCollision.y);
            }
            batch.end();
        }
        if (debug) {
            // PARTE DE DEBUG
            long total = Runtime.getRuntime().totalMemory();
            long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            Gdx.app.log("MEMORY", "total: " + total / 1048576 + "MB used: " + used / 1048576 + "MB");
        }

    }

    void saveScore() {
        maxScoreLevel[maxScoreLevel.length - 1] -= maxScoreLevel[currentLevel];
        maxScoreLevel[currentLevel] = levelScore;
        maxScoreLevel[maxScoreLevel.length - 1] += maxScoreLevel[currentLevel];

        MyHashTable<String> myJson = new MyHashTable<>();
        myJson.insert("name", userName);
        for (int i = 0; i < maxScoreLevel.length; i++) {
            myJson.insert(String.valueOf(i), String.valueOf(maxScoreLevel[i]));
        }
        MyJsonReader toSend = new MyJsonReader(myJson.toString());
        
        if (docRef == null){
        docRef = fbase.insertData("puntajes",toSend);
        }
        else{
            fbase.updateData("puntajes", toSend, docRef);
        }
    }

    /**
     * esta funcion se llama al cerrar el juego, elimina los objetos
     * automaticamente
     */
    @Override
    public void dispose() {
        clearLevel();
    }

    /**
     * Esta funcion borra de la memoria ram todos los objetos que se vean en
     * pantalla
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
        LastDeleted.makeEmpty();
        PrimeraPos = 8;
        if (buttonRestart != null) {
            buttonRestart.dispose();
        }
        backgroundTexture.dispose();
        if (buttonCannon != null) {
            buttonCannon.dispose();
        }
        if (buttonCtrz != null) {
            buttonCtrz.dispose();
        }
        if (buttonDelete != null) {
            buttonDelete.dispose();
        }
        if (buttonBack != null) buttonBack.dispose();
        if ( buttonAcceptPlay != null) buttonAcceptPlay.dispose();

        System.gc();

    }

    /**
     * Esta funcion inicia los niveles segun el numero de nivel enviado, donde
     * cero es el menu.
     *
     * @param level nivel a cargar
     */
    public void initiateLevel(int level) {
        Integer[] myArr;
        Integer[] myArr2;
        //ACA VA LA INFO DE NIVELES. 0=MENU;
        batch = new SpriteBatch();
        backgroundTexture = new Texture(Gdx.files.internal("Fondo_Principal.jpg"));
        fondoPause = new Texture(Gdx.files.internal("fondoPause.png"));
        if ("list".equals(tema)) {
            backgroundTexture = new Texture(Gdx.files.internal("Fondo Pilas-colas.jpg"));
            infoTexture = new Texture(Gdx.files.internal("Info.png"));
            buttonShooting = new GenericButton(47, 77, 32, 32, "shooting.png");
        } else if ("tree".equals(tema)) {
            backgroundTexture = new Texture(Gdx.files.internal("Fondo_Arboles.jpg"));
            infoTexture = new Texture(Gdx.files.internal("Info_dos.png"));

        }
        if (level >0) {
            volverMenu = new GenericButton(280, 400, 381, 44, "MainMenuButtons.jpg");
            buttonHelp = new GenericButton(175, 562, 32, 32, "Help.png");
            buttonPause = new GenericButton(47, 562, 32, 32, "Menu.png");
            buttonCtrz = new GenericButton(200, 77, 32, 32, "Re-Do.png");
            if ("list".equals(tema)) {
                buttonCtrz = new GenericButton(200, 77, 32, 32, "Re-Do.png");
            } else if ("tree".equals(tema)) {
                buttonCtrz = new GenericButton(500, 77, 32, 32, "Re-Do.png");
            }

            buttonDelete = new GenericButton(32, 32, 32, 32, "Basura.png");
            buttonClose = new GenericButton(600, 503, 50, 50, "buttonClose.png");
            buttonRestart = new GenericButton(109, 562, 32, 32, "Re-Try.png");
            buttonWin = new GenericButton(100, 100, 100, 100, "Win.png");
            buttonLose = new GenericButton(0, 0, 50, 50, "Lose.png");
            buttonNextLevel = new GenericButton(250, 250, 128, 64, "buttonNextLevel.jpg");
            buttonRetry = new GenericButton(250, 250, 128, 64, "Re-Try.png");

            levelScore = 1000 * level;
            minScore = 100 * level;
        }
        currentLevel = level;
        switch (level) {
             case -100:
                // ACA EL TEMA DE REGISTRAR USUARIO //
                isRegistering = true;
                buttonAcceptPlay = new GenericButton(300, 400, 64, 64, "Play-button.png");
                waitTime=15;
                break;
                
            case -1:
                //Seleccionar niveles tablas
                waitTime=15;
                break;
            case -2:
                // seleccionar niveles arboles
                waitTime=15;
                break;
            case -3:
                // ACA VA EL TEMA DE TOP PUNTAJSE //
                buttonBack = new GenericButton(300,300,64,64,"Re-Do.png");
                waitTime=15;
                try{
                    scoreHeap = new BinaryHeap<>(false);
                    res = fbase.searchData("puntajes");
                    for (int i=0; i<res.getSize() ; i++) {
                        JsonElement root = new JsonParser().parse(res.getData(i).value);
                        String strScore = root.getAsJsonObject().get("9").getAsString();
                        MyTuple<Integer,String> tp = new MyTuple<>(i, strScore);
                        scoreHeap.insert(tp);

                    }
                    // ACA VA MOSTRAR CADA COSITA DE UN PUNTAJE SI SE LE DA CLICK
                    int topSize = 10;
                    if (scoreHeap.getCurrentSize() < 10) topSize=scoreHeap.getCurrentSize();
                    for (int i = 0; i < topSize; i++){
                        ScoreButton buttonShowScore = new ScoreButton(300,400-100*i,64,64,"MainMenuButtons.jpg",scoreHeap.deleteTop());
                        listButtonScore.add(buttonShowScore);
                    }
                }
                catch (Exception e){
                }

                break;
            case -4:
                // ACA VA EL MENU PRINCIPAL
                waitTime=15;
                buttonPlay = new GenericButton(300,200,64,64,"Play-button.png");
                buttonScore = new GenericButton(300,100,64,64,"Points.png");
                buttonExit = new GenericButton(300,000,64,64,"Exit-button.png");
                buttonCredits = new GenericButton(300,350,64,64,"Credits2.png");
                break;
            case -5:
                // ACA VAN LOS CREDITOS
                waitTime=15;
                buttonBack = new GenericButton(300,300,64,64,"Re-Do.png");
                break;
            case -6:
                // ACA VA SI SE SELECCIONA UN PUNTAJE
                waitTime=15;
                buttonBack = new GenericButton(300,300,64,64,"Re-Do.png");
                break;
            case 0:
                // SELECTOR DE TEMA NIVELES
                waitTime=15;
                buttonLevelLinearDS = new GenericButton(210, 270, 381, 44, "MainMenuButtons.jpg");
                buttonLevelTrees = new GenericButton(210, 180, 381, 44, "MainMenuButtons.jpg");
                break;
            case 1:
                mode = "fifo";
                buttonCannon = new Canon(70, 32, 100, 100, "Canon_1.png");
                myArr = strToArr("1,2,3");
                myArr2 = strToArr("1,2,3");
                OrderBridge = new Bridge<>(myArr);
                for (int i = 0; i < OrderBridge.getSize(); i++) {
                    createPlank(100 + i * 45, 400, 20, 64, listPlankBridge, myArr[i]);
                }
                for (int i = 0; i < myArr2.length; i++) {
                    createPlank(300 + i * 55, 52, 15, 64, listPlank, myArr2[i]);
                }
                break;

            case 2:
                mode = "lifo";
                buttonCannon = new Canon(70, 32, 100, 100, "Canon_2.png");
                myArr = strToArr("4,1,2,3,9");
                myArr2 = strToArr("4,1,2,3,9");
                OrderBridge = new Bridge<>(myArr);
                for (int i = 0; i < OrderBridge.getSize(); i++) {
                    createPlank(100 + i * 45, 400, 20, 64, listPlankBridge, myArr[i]);
                }
                for (int i = 0; i < myArr2.length; i++) {
                    createPlank(300 + i * 55, 52, 15, 64, listPlank, myArr2[i]);
                }
                break;
            case 3:
                mode = "fifo";
                buttonCannon = new Canon(70, 32, 100, 100, "Canon_1.png");
                myArr = strToArr("1,2,3,4,5");
                OrderBridge = new Bridge<>(myArr);
                for (int i = 0; i < OrderBridge.getSize(); i++) {
                    createPlank(100 + i * 55, 400, 44, 117, listPlankBridge, myArr[i]);
                }

                myArr2 = strToArr("4,2,1,3,5");
                for (int i = 0; i < myArr2.length; i++) {
                    createPlank(300 + i * 55, 52, 15, 64, listPlank, myArr2[i]);
                }
                break;
            case 6:
                listLeafTreePlayerOrder = new Integer[6];
                buttonCannon = new Canon(345, 5, 100, 100, "CanonTree.png");
                treeTexture = new Texture(Gdx.files.internal("nivel_uno.png"));
                treeX = 110;
                treeY = 220;
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
                addHueco(114, 233, 3, 96, 96);
                addHueco(306, 232, 4, 96, 96);
                addHueco(597, 233, 5, 96, 96);
                Shootingtime = 0;

                break;
            case 5:
                listLeafTreePlayerOrder = new Integer[6];
                buttonCannon = new Canon(345, 5, 100, 100, "CanonTree.png");
                treeTexture = new Texture(Gdx.files.internal("nivel_uno.png"));
                treeX = 110;
                treeY = 220;
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
                addHueco(113, 233, 3, 96, 96);
                addHueco(305, 233, 4, 96, 96);
                addHueco(595, 233, 5, 96, 96);

                Shootingtime = 0;

                break;
            case 4:
                listLeafTreePlayerOrder = new Integer[11];
                buttonCannon = new Canon(345, 5, 100, 100, "CanonTree.png");
                treeTexture = new Texture(Gdx.files.internal("nivel2.png"));
                treeX = 0;
                treeY = 160;
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

                addHueco(9, 175, 6, 45, 54);
                addHueco(190, 175, 7, 50, 49);
                addHueco(408, 174, 8, 40, 51);
                addHueco(525, 173, 9, 50, 51);
                addHueco(705, 173, 10, 63, 61);

                Shootingtime = 0;

                break;
        }

    }

    /**
     * Esta funcion crea un objeto hueco con posicion, tamanio y numero y lo
     * aniade a una lista
     *
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
     * Esta funcion permite deshacer el ultimo cambio realizado en el juego de
     * tablas o de arboles
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
            int xpos = ListaPosicionesHojas.getData(PrimeraPos).key;
            int ypos = ListaPosicionesHojas.getData(PrimeraPos).value;
            if (!LastDeleted.isEmpty()) {
                Leaf myLastLeaf = LastDeleted.getData(0);
                myLastLeaf.leafCollision.x = xpos;
                myLastLeaf.leafCollision.y = ypos;

                listLeaf.add(myLastLeaf);
                LastDeleted.makeEmpty();
            }
            if (listLeafTree.getSize() != 0) {
                Leaf myLastLeaf = listLeafTree.pop();
                myLastLeaf.leafCollision.x = xpos;
                myLastLeaf.leafCollision.y = ypos;

                listLeaf.add(myLastLeaf);

            }
            if (!listHuecosUsados.isEmpty()) {
                listHueco.add(listHuecosUsados.pop());
            }
            PrimeraPos++;
            PrimeraPos = PrimeraPos%9;
        }
    }

    /**
     * Clase de objeto hueco para colocar en niveles de arboles, esta clase
     * permite interactuar con espacios donde deben ir las hojas
     */
    public class hueco {

        int xpos;
        int ypos;
        int index;
        int collx;
        int colly;

        /**
         * Constructor de la clase
         *
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
     * Esta funcion convierte una lista de clase tablas en una lista de numeros
     * enteros
     *
     * @param toConvert la lista de clase tablas que se desea convertir a
     * enteros
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
     * Esta funcion convierte una cadena de texto con numeros separados por
     * comas en un array de enteros
     *
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
