package com.example.mygame.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.example.mygame.R;

public class Bullet {
    private Bitmap bitmap;
    private int x, y;
    private int speed = 15; // Velocidad de la bala (hacia arriba)
    private boolean isActive;
    private Rect detectCollision;

    public Bullet(Context context, int startX, int startY) {
        x = startX;
        y = startY;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet);
        isActive = true;

        // Inicializar el objeto detectCollision
        detectCollision = new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
    }

    public void update() {
        y -= speed; // Mover la bala hacia arriba (en dirección negativa del eje Y)

        // Desactivar la bala si sale de la pantalla
        if (y < 0) {
            isActive = false;
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

    public boolean isActive() {
        return isActive;
    }

    public Rect getDetectCollision() {
        return detectCollision;
    }

    // Nuevo método para desactivar el enemigo
    public void setInactive() {
        isActive = false;
    }

}