package com.example.mygame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Player {
    private Bitmap bitmap;
    private int x, y;
    private Rect detectCollision;
    private int movementDirection = 0;  // 0 = No moverse, -1 = Izquierda, 1 = Derecha
    private int speed = 10;  // Velocidad del jugador
    private int minX, maxX;  // Límites de movimiento

    public Player(Context context, int screenX, int screenY) {
        x = screenX / 2;  // Iniciar en el centro
        y = screenY - 400;  // Posicionar cerca de la parte inferior
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.frente64);

        // Definir límites de movimiento
        minX = 0;
        maxX = screenX - bitmap.getWidth();

        // Inicializar el objeto detectCollision
        detectCollision = new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
    }

    // Método para disparar una bala
    public Bullet shoot(Context context) {
        // Crear una bala en la posición del jugador (centrada horizontalmente)
        int bulletX = x + (bitmap.getWidth() / 2); // Centrar la bala en el jugador
        int bulletY = y; // La bala sale desde la parte superior del jugador
        return new Bullet(context, bulletX, bulletY);
    }

    // Resto de los métodos (update, getters, setters, etc.)
    public void update() {
        x += movementDirection * speed;

        // Restringir el movimiento dentro de los límites de la pantalla
        if (x < minX) {
            x = minX;
        } else if (x > maxX) {
            x = maxX;
        }

        // Actualizar el rectángulo de colisión
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rect getDetectCollision() {
        return detectCollision;
    }

    public void moveLeft() {
        movementDirection = -1;  // Mover a la izquierda
    }

    public void moveRight() {
        movementDirection = 1;  // Mover a la derecha
    }

    public void stopMoving() {
        movementDirection = 0;  // Detener el movimiento
    }
}