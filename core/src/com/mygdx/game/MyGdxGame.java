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

public class MyGdxGame extends ApplicationAdapter {

    SpriteBatch batch;
    Texture img;
    //AQUI SE CARGAN LAS TEXTURAS 
    private Texture dropImage;
    private Texture bucketImage;
    
    // ESTA ES LA CAMARA, es una camara 3d pero que se proyecta ortogonalmente (2d)
    private OrthographicCamera camera;
    
    // Este es un objeto rectangulo (es una clase de libGdx que tiene un área de colisión)
    private Rectangle bucket;
    
    // un array c:
    private Array<Rectangle> raindrops;
    
    // un número grande
    private long lastDropTime;

    
    //ESTA FUNCIÓN CREA LAS GOTITAS DE AGUA DE MANERA RANDOM EN LUGARES RANDOM EN EL EJE X
    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800 - 64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void create() {
        
        // ESTA FUNCION ES MUY IMPORTANTE, SE LLAMA AL INICIO DEL JUEGO Y SE ENCARGA DE INICIALIZARLO TODO
       
        // load the images for the droplet and the bucket, 64x64 pixels each
        // SE CARGAN LAS TEXTURAS EN UN OBJETO
        dropImage = new Texture(Gdx.files.internal("drop.png"));
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));
        
        // SE CREA LA CAMARA  Y SE PONE EN ORTOGONAL Y LAS DIMENSIONES ED LA PANTALLA
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        
         // el batch es una cosa necesaria para renderizar el sprite
        batch = new SpriteBatch();
        
        // ACÁ SE CREA EL BALDE COMO UN ARÉA RECTANGULAR Y SE LE PONE SU TAMAÑO W,H Y SU POSICION INICIAL .X, .Y
        bucket = new Rectangle();
        bucket.x = 800 / 2 - 64 / 2;
        bucket.y = 20;
        bucket.width = 64;
        bucket.height = 64;
        raindrops = new Array<Rectangle>();
        spawnRaindrop();

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
        batch.draw(bucketImage, bucket.x, bucket.y);
        batch.end();
        
        //este if se encarga de saber si el mouse se oprimió y en qué parte de la pantalla y mueve el balde
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucket.x = touchPos.x - 64 / 2;
        }

        // esta funcion pregunta si la tecla left fue oprimida y mueve el balde
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            bucket.x -= 400 * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            bucket.x += 400 * Gdx.graphics.getDeltaTime();
        }
        
        // Poner limites pa q no se salga el balde
        if (bucket.x < 0) {
            bucket.x = 0;
        }
        if (bucket.x > 800 - 64) {
            bucket.x = 800 - 64;
        }

        // spawn de las gotas en un cierto tiempo
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) {
            spawnRaindrop();
        }

        
        // se itera cada gota, para añadirle velocidad vertical (que caiga) y si se sale de la pantalla o toca el balde, se elimina el objeto
        for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {
            Rectangle raindrop = iter.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if (raindrop.y + 64 < 0) {
                iter.remove();
            }
            if (raindrop.overlaps(bucket)) {
                iter.remove();
            }
        }
        
        // dibujar el balde con sus posiciones
        batch.begin();
        batch.draw(bucketImage, bucket.x, bucket.y);
        
        // para cada gota dibujarla con sus posiciones
        for (Rectangle raindrop : raindrops) {
            batch.draw(dropImage, raindrop.x, raindrop.y);
        }
        batch.end();

    }

    @Override
    public void dispose() {
        // esta función se llama al cerrar el juego, elimina los objetos manualmente (java lo hace solito, pero es como por seguridad)
        batch.dispose();
        img.dispose();
        dropImage.dispose();
        bucketImage.dispose();
        batch.dispose();
    }
}
