package com.example.mygame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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

    public GameView(Context context, int screenX, int screenY, GameActivity gameActivity) {
        super(context);

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
    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    private void update() {
        player.update();

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

        // Actualizar enemigos
        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i].isActive()) {
                enemies[i].update();
            } else {
                // Si el enemigo está inactivo, puedes eliminarlo o reiniciarlo
                enemies[i] = new Enemy(getContext(), getWidth(), getHeight());
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
}