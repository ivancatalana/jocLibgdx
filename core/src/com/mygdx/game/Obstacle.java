package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Timer;

public class Obstacle extends Actor {
    Circle bounds;
    AssetManager manager;
    boolean colision = false;
    private float angle = 0;
    Rectangle carro;
    public int getLevel() {
        return level;
    }

    private int level;
    float explosionX;
    float explosionY;
    boolean hit = false;
    private float alturaY=3;
    Obstacle(int level) {
        this.level = level;
        setSize(16, 16);
        bounds = new Circle();
        carro=new Rectangle();
        carro.setSize(30,30);
        setVisible(false);
    }

    @Override
    public void act(float delta) {
        if (level == 1) {

            // mueve el objeto hacia la izquierda y en el eje Y con la función moveBy
            moveBy(-0.5f, 0);

            // actualiza los límites del objeto
            bounds.set(getX(), getY(), 8);
        } else if (level == 2) {

            // aumenta el ángulo en función de la velocidad
            angle += -0.8f * delta;

            // calcula la posición en el eje Y con una función seno
            float amplitude = 1; // magnitud del movimiento oscilatorio
            float y = getY() + amplitude * MathUtils.sin(angle);

            // mueve el objeto hacia la izquierda y en el eje Y con la función moveBy
            moveBy(-20 * delta, y - getY());

            // actualiza los límites del objeto
            bounds.set(getX(), getY(), 8);

        } else if (level == 3) {


            // mueve el objeto hacia la izquierda y en el eje Y con la función moveBy
            moveBy(-0.8f, alturaY);
            if(alturaY<-0.f){
                alturaY=0;
            }
            else{
                alturaY=alturaY-0.1f;

            }
            // actualiza los límites del objeto
            bounds.set(getX(), getY(), 8);

        }

        // verifica si el objeto está visible y si está fuera de la pantalla para eliminarlo
        if (!isVisible()) {
            setVisible(true);
        }
        if (getX() < -64) {
            // remove();

        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);


        if (!colision) {
            batch.draw(manager.get("bullseye.png", Texture.class), getX(), getY(),60,60);
            // Carro Rojo
            Pixmap pixmap2 = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap2.setColor(Color.RED);
            pixmap2.fill();
            Texture textureRoda = new Texture(pixmap2);
            pixmap2.dispose();
            batch.draw(textureRoda, getX(),160,60,100);
            //Carro Black
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.BLACK);
            pixmap.fill();
            Texture texture = new Texture(pixmap);
            pixmap.dispose();
            batch.draw(texture, getX(),200,60,60);

        }

        else {
            if (hit) {
                explosionX = this.getX();
                explosionY = this.getY();
                hit = false;

            } else
                batch.draw(manager.get("explosion.png", Texture.class), explosionX, explosionY, 60, 60);
        }
    }

    public Circle getBounds() {
        return bounds;
    }

    public void setManager(AssetManager manager) {
        this.manager = manager;
    }

    public void removeObstacle() {
        level += 1;
        hit = true;
         colision=true;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
            }
        }, 3); // establecer canImpulse en true después de 1 segundo
    }


}

