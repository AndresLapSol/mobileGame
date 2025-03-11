package com.example.mygame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Player {
    private Bitmap bitmap;
    private int x, y;
    private Rect detectCollision;
    private int movementDirection = 0;  // 0 = Sin movimiento, -1 = Izquierda, 1 = Derecha
    private int speed = 10;  // Velocidad del jugador
    private int minX, maxX;  // Límites de movimiento
    private int collisionMargin = 11;  // Margen para reducir la hitbox en todos los lados

    public Player(Context context, int screenX, int screenY) {
        x = screenX / 2;  // Iniciar en el centro horizontal
        y = screenY - 400;  // Posicionar cerca de la parte inferior
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.frente64_aurea3);

        // Definir límites de movimiento
        minX = 0;
        maxX = screenX - bitmap.getWidth();

        // Inicializar el rectángulo de colisión aplicando el margen en cada lado
        detectCollision = new Rect(
                x + collisionMargin,
                y + collisionMargin,
                x + bitmap.getWidth() - collisionMargin,
                y + bitmap.getHeight() - collisionMargin
        );
    }

    // Método para disparar una bala
    public Bullet shoot(Context context) {
        // Crear una bala en la posición del jugador (centrada horizontalmente)
        int bulletX = x + (bitmap.getWidth() / 2);
        int bulletY = y;
        return new Bullet(context, bulletX, bulletY);
    }

    // Actualiza la posición del jugador y la hitbox
    public void update() {
        x += movementDirection * speed;

        // Restringir el movimiento dentro de los límites de la pantalla
        if (x < minX) {
            x = minX;
        } else if (x > maxX) {
            x = maxX;
        }

        // Actualizar la hitbox con el margen aplicado en todos los lados
        detectCollision.left = x + collisionMargin;
        detectCollision.top = y + collisionMargin;
        detectCollision.right = x + bitmap.getWidth() - collisionMargin;
        detectCollision.bottom = y + bitmap.getHeight() - collisionMargin;
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
        movementDirection = -1;
    }

    public void moveRight() {
        movementDirection = 1;
    }

    public void stopMoving() {
        movementDirection = 0;
    }
}
