package com.example.mygame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

public class Boss {
    private Bitmap bitmap;
    private int x, y;
    private int speed;
    private int screenX, screenY;
    private boolean isActive;
    private boolean isDefeated;
    private boolean isInPosition; // Indica si el Boss ha llegado a su posición final
    private Rect detectCollision;
    private boolean movingRight; // Dirección del movimiento
    private int health; // Vida del Boss

    public Boss(Context context, int screenX, int screenY) {
        Log.d("Boss", "Constructor ejecutado");
        this.screenX = screenX;
        this.screenY = screenY;
        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bossprueba);
        // Obtener las dimensiones originales
        int originalWidth = originalBitmap.getWidth();
        int originalHeight = originalBitmap.getHeight();

        // Calcular la relación de aspecto
        float aspectRatio = (float) originalWidth / originalHeight;

        // Definir el ancho objetivo (por ejemplo, 200)
        int targetWidth = 200;

        // Calcular el alto objetivo manteniendo la relación de aspecto
        int targetHeight = (int) (targetWidth / aspectRatio);

        // Escalar el bitmap
        bitmap = Bitmap.createScaledBitmap(originalBitmap, targetWidth, targetHeight, false);

        originalBitmap.recycle(); // Liberar la memoria del bitmap original
        x = screenX / 2 - bitmap.getWidth() / 2;
        y = -bitmap.getHeight();
        detectCollision = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
        isActive = false;
        isInPosition = false;
        health = 5;
        isDefeated = false;
        speed = 5; // Inicializar la variable speed
    }

    public void update() {
        Log.d("Boss", "update ejecutado, isActive: " + isActive + ", isInPosition: " + isInPosition);
        if (isActive) {
            // Mover el Boss hacia abajo hasta su posición final
            if (y < screenY / 4) {
                y += speed;
            } else {
                isInPosition = true; // El Boss ha llegado a su posición final
                // Movimiento horizontal
                if (movingRight) {
                    x += speed;
                    if (x + bitmap.getWidth() >= screenX) {
                        movingRight = false;
                    }
                } else {
                    x -= speed;
                    if (x <= 0) {
                        movingRight = true;
                    }
                }
            }
        }
    }

    public void reset() {
        isActive = false;
        isDefeated = false;
        isInPosition = false;
        health = 5; // Reiniciar la vida
        y = -bitmap.getHeight(); // Reiniciar la posición Y
        x = screenX / 2 - bitmap.getWidth() / 2; // Reiniciar la posición X
        movingRight = true; // Reiniciar la dirección del movimiento
        Log.d("Boss", "Boss reiniciado");
    }

    public void activate() {
        reset(); // Reiniciar el estado del Boss antes de activarlo
        isActive = true;
        isInPosition = false; // Cambiar isInPosition a false
        y = -bitmap.getHeight(); // Cambiar y a -bitmap.getHeight()
        Log.d("Boss", "Boss activado, isActive: " + isActive + ", isInPosition: " + isInPosition);
        System.out.println("Boss activado");
        Log.d("Boss", "Boss activado");
    }

    public boolean isActive() {
        return isActive;
    }

    public void setInactive() {
        isActive = false; // Desactiva al Boss
    }

    public Bitmap getBitmap() {
        Log.d("Boss", "getBitmap ejecutado");
        return bitmap;
    }

    public int getX() {
        Log.d("Boss", "getX ejecutado, x: " + x);
        return x;
    }

    public int getY() {
        Log.d("Boss", "getY ejecutado, y: " + y);
        return y;
    }

    public Rect getDetectCollision() {
        return new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
    }

    public void takeDamage() {
        if (!isActive || isDefeated) {
            return; // Si el Boss ya está inactivo o derrotado, no recibe más daño
        }

        if (isInPosition) { // Solo recibir daño si ya llegó a su posición
            if(health > 0){
                health--;
                System.out.println("Boss recibió daño, vida restante: " + health);
            }

            if (health <= 0) {
                setInactive();
                health = 0;
                isDefeated = true;
                System.out.println("Boss ha sido derrotado");
            }
        }
    }

    public boolean isInPosition() {
        return isInPosition;
    }

    public boolean isDefeated() {
        return isDefeated;
    }

    public int getHealth() {
        return health;
    }

}