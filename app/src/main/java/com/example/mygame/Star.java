package com.example.mygame;

import java.util.Random;

public class Star {
    private int x;
    private int y;
    private int speed;

    private int maxX;
    private int maxY;
    private int minX;
    private int minY;

    public Star(int screenX, int screenY) {
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;
        Random generator = new Random();
        speed = generator.nextInt(10);

        // Generando una coordenada aleatoria
        // pero manteniendo la coordenada dentro del tamaño de la pantalla
        x = generator.nextInt(maxX);
        y = generator.nextInt(maxY);
    }

    public void update(int playerSpeed) {
        // Animando la estrella hacia el lado izquierdo horizontalmente
        // disminuyendo la coordenada x con la velocidad del jugador
        x -= playerSpeed;
        x -= speed;
        // Si la estrella llega al borde izquierdo de la pantalla
        if (x < 0) {
            // Volver a colocar la estrella en el borde derecho
            // Esto dará el efecto de fondo en movimiento infinito
            x = maxX;
            Random generator = new Random();
            y = generator.nextInt(maxY);
            speed = generator.nextInt(15);
        }
    }

    public float getStarWidth() {
        // Haciendo el ancho de la estrella aleatorio para que
        // tenga un aspecto más realista
        float minX = 1.0f;
        float maxX = 4.0f;
        Random rand = new Random();
        float finalX = rand.nextFloat() * (maxX - minX) + minX;
        return finalX;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
