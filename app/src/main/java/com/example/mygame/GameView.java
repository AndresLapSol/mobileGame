package com.example.mygame;

import android.content.Context;
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
    private ArrayList<Star> stars = new ArrayList<Star>();

    // Referencia a GameActivity
    private GameActivity gameActivity;

    public GameView(Context context, int screenX, int screenY, GameActivity gameActivity) {
        super(context);
        this.gameActivity = gameActivity; // Inicializar la referencia a GameActivity

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

        // Actualizar la posición de los enemigos
        for (int i = 0; i < enemyCount; i++) {
            enemies[i].update();

            // Verificar colisión entre el jugador y un enemigo
            if (Rect.intersects(player.getDetectCollision(), enemies[i].getDetectCollision())) {
                // Detener el juego
                playing = false;

                // Iniciar la actividad de Game Over
                gameActivity.gameOver();
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

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            gameThread.sleep(17);
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