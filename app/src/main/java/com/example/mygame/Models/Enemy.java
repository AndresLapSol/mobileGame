package com.example.mygame.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.example.mygame.R;

import java.util.Random;

public class Enemy {

    private Bitmap bitmap;
    private int x, y;
    private int speed = 1;
    private int maxX, minX, maxY, minY;
    private Rect detectCollision;
    private boolean isActive; // Nuevo campo para rastrear si el enemigo está activo
    private int collisionMargin = 15;  // Margen para reducir la hitbox uniformemente

    public Enemy(Context context, int screenX, int screenY) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.devil80);
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        Random generator = new Random();
        speed = generator.nextInt(6) + 10;
        x = screenX;
        y = generator.nextInt(maxY) - bitmap.getHeight();

        // Inicializar detectCollision aplicando el margen en todos los lados
        detectCollision = new Rect(
                x + collisionMargin,
                y + collisionMargin,
                x + bitmap.getWidth() - collisionMargin,
                y + bitmap.getHeight() - collisionMargin
        );
        isActive = true; // El enemigo está activo por defecto
    }

    public void update(float speedMultiplier) {
        y += speed * speedMultiplier;

        if (y > maxY) {
            Random generator = new Random();
            speed = generator.nextInt(5) + 3;
            y = minY - bitmap.getHeight();
            x = generator.nextInt(maxX - bitmap.getWidth());
        }

        // Actualizar la hitbox con el margen aplicado en todos los lados
        detectCollision.left = x + collisionMargin;
        detectCollision.top = y + collisionMargin;
        detectCollision.right = x + bitmap.getWidth() - collisionMargin;
        detectCollision.bottom = y + bitmap.getHeight() - collisionMargin;
    }

    public void setX(int x) {
        this.x = x;
    }

    public Rect getDetectCollision() {
        return detectCollision;
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

    public int getSpeed() {
        return speed;
    }

    // Nuevo método para desactivar el enemigo
    public void setInactive() {
        isActive = false;
    }

    // Nuevo método para verificar si el enemigo está activo
    public boolean isActive() {
        return isActive;
    }
}
