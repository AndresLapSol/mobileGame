package com.example.mygame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;

public class GameView extends SurfaceView implements Runnable {

    volatile boolean playing;
    private Thread gameThread = null;
    private Player player;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private Enemy[] enemies;
    private int enemyCount = 3;
    private ArrayList<Star> stars = new ArrayList<>();
    private ArrayList<Bullet> bullets = new ArrayList<>(); // Lista de balas activas
    private long lastShotTime = 0; // Tiempo del último disparo
    private static final long SHOT_INTERVAL = 500; // Intervalo de disparo en milisegundos (500 ms = 0.5 segundos)
    private long tiempoInicio;

    //Definir el objeto "explosion"
    private Boom boom;

    //Definir el objeto "Boss"
    private Boss boss;



    public GameView(Context context, int screenX, int screenY, GameActivity gameActivity) {
        super(context);

        tiempoInicio = System.currentTimeMillis(); // Guarda el tiempo de inicio
        Log.d("GameView", "tiempoInicio: " + tiempoInicio);

        player = new Player(context, screenX, screenY);
        surfaceHolder = getHolder();
        paint = new Paint();

        // Añadir estrellas
        int starNums = 100;
        for (int i = 0; i < starNums; i++) {
            Star s = new Star(screenX, screenY);
            stars.add(s);
        }

        // Inicializar enemigos
        enemies = new Enemy[enemyCount];
        for (int i = 0; i < enemyCount; i++) {
            enemies[i] = new Enemy(context, screenX, screenY);
        }

        //Inicializar Explosion
        boom = new Boom(context);

        // Inicializar el BOSS
        boss = new Boss(context, screenX, screenY); // Asegúrate de agregar esto aquí
    }

    @Override
    public void run() {
        Log.d("GameView", "run ejecutado");
        while (playing) {
            update();
            draw();
            control();
        }
    }

    private void update() {
        Log.d("GameView", "update ejecutado");
        player.update();

        //Poniendo la explosion fuera de la pantalla
        boom.setX(-250);
        boom.setY(-250);

        // Disparar balas constantemente
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastShotTime >= SHOT_INTERVAL) {
            bullets.add(player.shoot(getContext())); // Disparar una nueva bala
            lastShotTime = currentTime; // Actualizar el tiempo del último disparo
        }

        // Actualizar balas
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (bullet.isActive()) {
                bullet.update();
            } else {
                bullets.remove(i); // Eliminar balas inactivas
                i--;
            }
        }

        float speedMultiplier = getSpeedMultiplier();
        // Actualizar enemigos
        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i].isActive()) {
                enemies[i].update(speedMultiplier);
            } else {
                // Si el enemigo está inactivo, puedes eliminarlo o reiniciarlo
                enemies[i] = new Enemy(getContext(), getWidth(), getHeight());
            }
        }

        // **Actualizar Boss**
        if (boss != null && boss.isActive() && !boss.isDefeated()) { // Comprobar si el boss esta derrotado
            boss.update();
            Log.d("GameView", "boss.isActive");

            // Verificar colisiones entre balas y el Boss
            for (int i = 0; i < bullets.size(); i++) {
                Bullet bullet = bullets.get(i);
                if (bullet.isActive() && Rect.intersects(bullet.getDetectCollision(), boss.getDetectCollision())) {
                    bullet.setInactive(); // Desactivar la bala
                    boss.takeDamage(); // Reducir la vida del Boss

                    // Mover la explosión al sitio de colisión
                    boom.setX(boss.getX());
                    boom.setY(boss.getY());

                    if (boss.getHealth() <= 0) {
                        // El Boss ha sido derrotado
                        boss.setInactive(); // Desactivar al Boss
                        // Aquí puedes agregar una animación o lógica adicional
                    }
                    break; // Salir del bucle de balas para esta colisión
                }
            }
        }

        // Verificar colisión entre el jugador y un enemigo
        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i].isActive() && Rect.intersects(player.getDetectCollision(), enemies[i].getDetectCollision())) {
                // Iniciar la actividad de Game Over
                Intent gameOverIntent = new Intent(getContext(), GameOverActivity.class);
                getContext().startActivity(gameOverIntent);
                return; // Salir del método update para evitar más actualizaciones
            }
        }




        // Verificar colisiones entre balas y enemigos
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (bullet.isActive()) {
                for (int j = 0; j < enemies.length; j++) {
                    if (enemies[j].isActive() && Rect.intersects(bullet.getDetectCollision(), enemies[j].getDetectCollision())) {
                        // Colisión detectada: desactivar la bala y el enemigo
                        bullet.setInactive();
                        enemies[j].setInactive();

                        //Mover la explosion al sitio de colision
                        boom.setX(enemies[j].getX());
                        boom.setY(enemies[j].getY());

                        break; // Salir del bucle de enemigos para esta bala
                    }
                }
            }
        }

        // Verificar colisión entre el jugador y un enemigo
        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i].isActive() && Rect.intersects(player.getDetectCollision(), enemies[i].getDetectCollision())) {
                // Iniciar la actividad de Game Over
                Intent gameOverIntent = new Intent(getContext(), GameOverActivity.class);
                getContext().startActivity(gameOverIntent);
                return; // Salir del método update para evitar más actualizaciones
            }
        }

        long tiempoJugado = (System.currentTimeMillis() - tiempoInicio) / 1000; // Convierte a segundos
        Log.d("GameView", "Tiempo jugado: " + tiempoJugado);
        if (tiempoJugado >= 60 && !boss.isActive()) {
            boss.activate();
        }

    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);

            // Dibujar estrellas
            paint.setColor(Color.WHITE);
            for (Star s : stars) {
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(), s.getY(), paint);
            }

            // Dibujar jugador
            canvas.drawBitmap(player.getBitmap(), player.getX(), player.getY(), paint);

            // Dibujar enemigos
            for (int i = 0; i < enemyCount; i++) {
                canvas.drawBitmap(enemies[i].getBitmap(), enemies[i].getX(), enemies[i].getY(), paint);
            }

            // Dibujar balas
            for (Bullet bullet : bullets) {
                if (bullet.isActive()) {
                    canvas.drawBitmap(bullet.getBitmap(), bullet.getX(), bullet.getY(), paint);
                }
            }

            //Dibujar Explosion
            canvas.drawBitmap(
                    boom.getBitmap(),
                    boom.getX(),
                    boom.getY(),
                    paint
            );

            if (boss.isActive()) {
                canvas.drawBitmap(boss.getBitmap(), boss.getX(), boss.getY(), paint);
            }

            // Calcular el tiempo jugado en segundos
            long tiempoJugado = (System.currentTimeMillis() - tiempoInicio) / 1000;
            long minutos = tiempoJugado / 60;
            long segundos = tiempoJugado % 60;

            // Dibujar el tiempo en pantalla
            paint.setColor(Color.WHITE);
            paint.setTextSize(50);
            canvas.drawText("Tiempo: " + minutos + "m " + segundos + "s", 50, 100, paint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }


    private void control() {
        try {
            gameThread.sleep(17); // ~60 FPS
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float touchX = motionEvent.getX();
                float screenWidth = getWidth();

                if (touchX < screenWidth / 2) {
                    player.moveLeft(); // Mover a la izquierda
                } else {
                    player.moveRight(); // Mover a la derecha
                }
                break;

            case MotionEvent.ACTION_UP:
                player.stopMoving(); // Detener el movimiento cuando se suelta la pantalla
                break;
        }
        return true;
    }

    private float getSpeedMultiplier() {
        long tiempoJugado = (System.currentTimeMillis() - tiempoInicio) / 1000; // Tiempo en segundos
        return 1.0f + (tiempoJugado / 60.0f); // Aumenta la velocidad cada 30 segundos
    }
}