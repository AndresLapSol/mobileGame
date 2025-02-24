package com.example.mygame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Boss {
    private Bitmap bitmap;
    private int x, y;
    private int speed;
    private int screenX, screenY;
    private boolean isActive;

    public Boss(Context context, int screenX, int screenY) {
        this.screenX = screenX;
        this.screenY = screenY;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bossprueba); // Imagen del boss
        x = (screenX - bitmap.getWidth()) / 2; // Posición inicial centrada
        y = -bitmap.getHeight(); // Inicialmente fuera de la pantalla
        speed = 5; // Velocidad de bajada
        isActive = false; // No aparece hasta que pase un minuto
    }

    public void update() {
        if (isActive) {
            y += speed; // Mueve el boss hacia abajo lentamente
            if (y > screenY / 4) { // Se detiene al 25% de la pantalla
                y = screenY / 4;
            }
        }
    }

    public void activate() {
        isActive = true;
    }

    public boolean isActive() {
        return isActive;
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
        return new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
    }
}
