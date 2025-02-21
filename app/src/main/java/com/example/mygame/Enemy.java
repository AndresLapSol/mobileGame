package com.example.mygame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

/**
 * Creado por Belal el 15/06/2016.
 */
public class Enemy {

    // Bitmap para el enemigo
    // Ya hemos puesto el bitmap en la carpeta drawable
    private Bitmap bitmap;

    // Coordenadas x e y
    private int x;
    private int y;

    // Creando un objeto Rect
    private Rect detectCollision;

    // Velocidad del enemigo
    private int speed = 1;

    // Coordenadas mínimas y máximas para mantener al enemigo dentro de la pantalla
    private int maxX;
    private int minX;

    private int maxY;
    private int minY;


    public Enemy(Context context, int screenX, int screenY) {
        // Obteniendo el bitmap desde el recurso drawable
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.devil80);

        // Inicializando las coordenadas mínimas y máximas
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        // Generando una coordenada aleatoria para agregar el enemigo
        Random generator = new Random();
        speed = generator.nextInt(6) + 10;
        x = screenX;
        y = generator.nextInt(maxY) - bitmap.getHeight();

        detectCollision = new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());

    }

    public void update() {
        // Aumenta la coordenada "y" para que el enemigo caiga de arriba hacia abajo
        y += speed;

        // Si el enemigo llega al borde inferior
        if (y > maxY) {
            // Se reposiciona el enemigo en la parte superior con velocidad aleatoria y posición horizontal aleatoria
            Random generator = new Random();
            speed = generator.nextInt(5) + 3;
            y = minY - bitmap.getHeight();  // Aparece justo por encima de la pantalla
            x = generator.nextInt(maxX - bitmap.getWidth());
        }

        // Agregando la parte superior, izquierda, inferior y derecha al objeto rect
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();


    }

    // Añadiendo un setter para la coordenada x para poder cambiarla después de una colisión
    public void setX(int x){
        this.x = x;
    }

    // Un getter más para obtener el objeto rect
    public Rect getDetectCollision() {
        return detectCollision;
    }


    // Getters
    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }

}
