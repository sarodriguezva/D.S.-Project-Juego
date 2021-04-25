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
    //AQUI SE CARGAN LAS TEXTURAS 
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Texture infoTexture;
    
    //AQUI SE CARGAN LAS VARIABLES
    boolean justTouched = false;
    boolean leftPressed;
    Plank currentPick = null;
    GenericButton buttonRestart = null;
    GenericButton buttonHelp = null;
    GenericButton buttonClose= null;
    GenericButton buttonFire = null;
    Texture Cannon;
    Sprite Cannonsprite;
    boolean info = false;
    boolean Shooting = false;
    float Shootingtime = 0;
    int Shootingindex = 0;
   
    Vector3 touchPos = new Vector3();
    private final MyDoubleLinkedList<Plank> listPlank = new MyDoubleLinkedList<>();
    private final MyDoubleLinkedList<Plank> listPlankBridge = new MyDoubleLinkedList<>();
    private final MyDoubleLinkedList<Plank> listPlankCannon = new MyDoubleLinkedList<>();
    private final MyDoubleLinkedList<Plank> listPlankFired = new MyDoubleLinkedList<>();
    int currentLevel=0;
    
    private BitmapFont font;
    // ESTA ES LA CAMARA, es una camara 3d pero que se proyecta ortogonalmente (2d)
    private OrthographicCamera camera;

    //ESTE ES EL OBJETO DE PRUEBA
   // private final MyDoubleLinkedList<Plank> listPlank = new MyDoubleLinkedList<>();
    MyStack<Integer> plankStack = new MyStack<>();
    
    @Override
    public void create() {

        // ESTA FUNCION ES MUY IMPORTANTE, SE LLAMA AL INICIO DEL JUEGO Y SE ENCARGA DE INICIALIZARLO TODO
        // SE CREA LA CAMARA  Y SE PONE EN ORTOGONAL Y LAS DIMENSIONES ED LA PANTALLA
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        this.Cannon = new Texture(Gdx.files.internal("Canon_1.png"));
        this.Cannonsprite = new Sprite(Cannon, 0, 0, 32, 32);
        Cannonsprite.setPosition(60, 30);
        Cannonsprite.setScale(4);
        // el batch es una cosa necesaria para renderizar el sprite
        initiateLevel(1);
        font =  new BitmapFont(Gdx.files.internal("asd.fnt"));

    }
    
    
    // Funci�n para crear un plank y a�adirlo a la lista automaticamente
    public void createPlank(int x, int y, int w, int h,MyDoubleLinkedList list, int num) {
        // Creacion de buckets.
        Plank myPlank = new Plank(x, y, w, h, num);
        list.add(myPlank);
    }

    @Override
    public void render() { 
        
        // ESTA FUNCI�N MUESTRA TODO EN PANTALLA, TAMBI�N ES UNA FUNCI�N QUE SE LLAMA REPETIDAMENTE, creo que a 60fps
        // clear es pa borrar todo en pantalla
        ScreenUtils.clear(0, 0, 0.2f, 1);
        
        // ac� se usa lo de batch de antes, en resumen es un objeto que se encarga de manejar el tema de opengl para renderizar todo de manera eficiente
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        
        
        // PARTE DE LOGICA
        
        
        //justTouched me dice si el mouse ha sido recientemente presionado, leftpressed si est� continuamente presionado
        justTouched = Gdx.input.justTouched();
        leftPressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);

        // Si est� continuamente presionado, guardeme las coordenadas del mouse
        if (leftPressed) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
        }

        // Si ha sido recientemente presionado compruebe si ya est� arrastrando un objeto, si no hay objeto, busca el que el mouse presiona en esas coordenadas.
        if (justTouched && currentPick == null) {
            
            if (info == false ){
            for (int i = 0; i < listPlank.getSize(); i++) {
                if(listPlank.getData(i) != null){
                if (touchPos.x > listPlank.getData(i).plankCollision.x - listPlank.getData(i).plankCollision.width && touchPos.x < listPlank.getData(i).plankCollision.x + listPlank.getData(i).plankCollision.width) {
                    if (touchPos.y > listPlank.getData(i).plankCollision.y - listPlank.getData(i).plankCollision.height && touchPos.y < listPlank.getData(i).plankCollision.y + listPlank.getData(i).plankCollision.height) {
                        currentPick = listPlank.getData(i);

                    }
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
            
            // BOTON HELP ////
            if(touchPos.x > buttonHelp.buttonCollision.x - buttonHelp.buttonCollision.width && touchPos.x < buttonHelp.buttonCollision.x + buttonHelp.buttonCollision.width){
                if (touchPos.y > buttonHelp.buttonCollision.y - buttonHelp.buttonCollision.height && touchPos.y < buttonHelp.buttonCollision.y + buttonHelp.buttonCollision.height){
                    info=true;
                    
                     
                }
            }
            
            //BOTON FUEGO//
            if(touchPos.x > buttonFire.buttonCollision.x - buttonFire.buttonCollision.width && touchPos.x < buttonFire.buttonCollision.x + buttonFire.buttonCollision.width){
                if (touchPos.y > buttonFire.buttonCollision.y - buttonFire.buttonCollision.height && touchPos.y < buttonFire.buttonCollision.y + buttonFire.buttonCollision.height){
                   Shooting = true;
                }
            }
            
            /// BOTON PARA CERRAR EL POPUP DE HELP ///
            if(touchPos.x > buttonClose.buttonCollision.x - buttonClose.buttonCollision.width && touchPos.x < buttonClose.buttonCollision.x + buttonClose.buttonCollision.width){
                if (touchPos.y > buttonClose.buttonCollision.y - buttonClose.buttonCollision.height && touchPos.y < buttonClose.buttonCollision.y + buttonClose.buttonCollision.height){
                    info=false;
                    
                     
                }
            }
            
            
        }
        
        // Si est� siendo un objeto presionado:
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
                if(Math.abs(currentPick.plankCollision.x - Cannonsprite.getX())<20){
                    listPlankCannon.add(currentPick);
                    int[] Pos = listPlank.find(currentPick);
                    if(listPlank.getData(Pos[0]) != null){
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
        
        for (int i = 0; i < listPlankBridge.getSize(); i++) {
            Sprite sprPlankBridge = new Sprite(listPlankBridge.getData(i).plankTexture);
            sprPlankBridge.setPosition(listPlankBridge.getData(i).plankCollision.x, listPlankBridge.getData(i).plankCollision.y);
            sprPlankBridge.setColor(1, 1, 1, 0.4f);
            sprPlankBridge.draw(batch);
            font.draw(batch, Integer.toString(listPlankBridge.getData(i).plankNumber) ,listPlankBridge.getData(i).plankCollision.x+10, listPlankBridge.getData(i).plankCollision.y+70); 
         
        }
        
        
        for (int i = 0; i < listPlank.getSize(); i++) {
            if(listPlank.getData(i) != null){
            batch.draw(listPlank.getData(i).plankTexture, listPlank.getData(i).plankCollision.x, listPlank.getData(i).plankCollision.y);
             font.draw(batch, Integer.toString(listPlank.getData(i).plankNumber) ,listPlank.getData(i).plankCollision.x+10, listPlank.getData(i).plankCollision.y+70); 
         
        }
        }
        
        
        for (int i = 0; i < listPlankFired.getSize(); i++) {
           
            batch.draw(listPlankFired.getData(i).plankTexture, listPlankFired.getData(i).plankCollision.x, listPlankFired.getData(i).plankCollision.y);
             font.draw(batch, Integer.toString(listPlankFired.getData(i).plankNumber) ,listPlankFired.getData(i).plankCollision.x+10, listPlankFired.getData(i).plankCollision.y+70); 
         
        }
        
        
        Cannonsprite.draw(batch);
        batch.draw(buttonRestart.buttonTexture,0,0);
        batch.draw(buttonRestart.buttonTexture,0,0) ;
        batch.draw(buttonHelp.buttonTexture,0,555);
        batch.draw(buttonFire.buttonTexture,750,0);
        
        
        //Aqui render info
        if (info == true){
            // Sprite es un tipo de objeto que deja cambiar algunas caracteristicas ed las texturas x eso se crea un sprite con la textura
            Sprite sprite = new Sprite(infoTexture);
            //Aqu� se le pone un color en RGB,A. osea color y opacidad.
            sprite.setPosition(150,100);
            sprite.setSize(500, 450);
            sprite.setColor(1, 1, 1, 0.8f);
            // Se llama la dibujaci�n
            sprite.draw(batch);
            batch.draw(buttonClose.buttonTexture,600,503);
     
        }
        
        if (Shooting == true){
            if(listPlankCannon.isEmpty()|| listPlankFired.getSize()==listPlankBridge.getSize()){
                Shooting = false;
            } else{
             if(Shootingindex < listPlankCannon.getSize()){
                    if (Math.abs(listPlankBridge.getData(Shootingindex).plankCollision.x - listPlankCannon.getData(Shootingindex).plankCollision.x) > 20 || Math.abs(listPlankBridge.getData(Shootingindex).plankCollision.y - listPlankCannon.getData(Shootingindex).plankCollision.y) > 20){
                        ShootPlankto((int) listPlankBridge.getData(Shootingindex).plankCollision.x, (int) listPlankBridge.getData(Shootingindex).plankCollision.y, Cannonsprite.getX(), Cannonsprite.getY(), Shootingtime, listPlankCannon.getData(Shootingindex));
                        batch.draw(listPlankCannon.getData(Shootingindex).plankTexture, listPlankCannon.getData(Shootingindex).plankCollision.x, listPlankCannon.getData(Shootingindex).plankCollision.y);
                        font.draw(batch, Integer.toString(listPlankCannon.getData(Shootingindex).plankNumber) ,listPlankCannon.getData(Shootingindex).plankCollision.x+10, listPlankCannon.getData(Shootingindex).plankCollision.y+70);
                        Shootingtime += Gdx.graphics.getDeltaTime();
                    } else if(Shootingindex < listPlankBridge.getSize()){
                        listPlankCannon.getData(Shootingindex).plankCollision.x = listPlankBridge.getData(Shootingindex).plankCollision.x;
                        listPlankCannon.getData(Shootingindex).plankCollision.y = listPlankBridge.getData(Shootingindex).plankCollision.y;
                        listPlankFired.add(listPlankCannon.getData(Shootingindex));
                        Shootingindex++;
                        Shootingtime = 0;
                    }
             }
            }
        }
        

        
        
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
        // esta funci�n se llama al cerrar el juego, elimina los objetos manualmente (java lo hace solito, pero es como por seguridad)
        batch.dispose();
        for (int i = 0; i < listPlank.getSize(); i++) {
            listPlank.getData(i).dispose();
        }
        buttonRestart.dispose();
        backgroundTexture.dispose();
       
    }
    
    
    
    public void clearLevel(){
        batch.dispose();
        for (int i = 0; i < listPlank.getSize(); i++) {
            listPlank.getData(i).dispose();
        }
        Shooting = false;
        Shootingtime = 0;
        Shootingindex = 0;
        listPlank.makeEmpty();
        listPlankCannon.makeEmpty();
        listPlankCannon.makeEmpty();
        buttonRestart.dispose();
        backgroundTexture.dispose();
        System.gc();
        
    }

    public void initiateLevel(int level){
    //AC� VA LA INFO DE NIVELES. 0=MENU;
    switch (level){
        case 1:
        batch = new SpriteBatch();
        //createPlank(400,00,64,64);
        //createPlank(0,0,64,64);
        buttonHelp= new GenericButton(0,555,50,50,"buttonHelp.png");
        buttonClose= new GenericButton(600,503,50,50,"buttonClose.png");
        buttonRestart = new GenericButton(0,0, 50,50, "buttonRestart.png");
        buttonFire = new GenericButton(750,0, 50,50, "buttonFire.png");
        infoTexture = new Texture(Gdx.files.internal("Info.png"));
        backgroundTexture = new Texture(Gdx.files.internal("parallax-mountain-bg.png"));
        currentLevel=1;
        Integer[] myArr = {44,2,3,0,9,8,4,3};
        Bridge<Integer> OrderBridge = new Bridge<>(myArr);
        for (int i=0 ; i<OrderBridge.getSize() ; i++){
            createPlank(100+i*45,400,44,117,listPlankBridge,myArr[i]);
        }
        Integer[] myArr2 = {2,44,3,0,9,4,3,8};
        for (int i=0 ; i<myArr2.length ; i++){
            createPlank(300+i*45,0,44,117,listPlank,myArr2[i]);
        }
        break;
    }
    
    }
    
    public void ShootPlankto(int objX, int objY, float initx, float inity, float Shoottime, Plank proyectile){
        float velx,vely,accel;
        velx = (objX - initx)/3;
        vely = (objY-inity)/3 + 120f;
        accel = 73f;
        vely -= accel * Shoottime;
        proyectile.plankCollision.x += velx * Gdx.graphics.getDeltaTime();;
        proyectile.plankCollision.y += vely * Gdx.graphics.getDeltaTime();
        
       
        
    }
}
