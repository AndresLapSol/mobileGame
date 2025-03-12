package com.example.mygame;

import android.app.Activity;
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
import android.media.MediaPlayer;

import com.example.mygame.Activity.GameActivity;
import com.example.mygame.Activity.GameOverActivity;
import com.example.mygame.Activity.WinActivity;
import com.example.mygame.Models.Boom;
import com.example.mygame.Models.Boss;
import com.example.mygame.Models.Bullet;
import com.example.mygame.Models.Enemy;
import com.example.mygame.Models.Player;
import com.example.mygame.Models.Star;


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
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private long lastShotTime = 0;
    private static final long SHOT_INTERVAL = 500; // Intervalo de disparo en ms
    private long tiempoInicio;

    // Objeto Explosion
    private Boom boom;

    // Objeto Boss
    private Boss boss;

    // Contadores de enemigos y Boss derrotados
    private int enemiesDefeated = 0;
    private int bossesDefeated = 0;

    private Paint backgroundPaint;

    private ArrayList<Integer> bossActivationTimes; // Tiempos de activación (en segundos)
    private int currentBossActivationIndex = 0;

    // Variables para la transición del fondo
    private int currentBackgroundColor = Color.BLACK;
    private int targetBackgroundColor = Color.BLACK;
    private long transitionStartTime = 0;
    private static final long TRANSITION_DURATION = 1000; // Duración de la transición en ms

    private MediaPlayer bossSound;

    private MediaPlayer backgroundMusic;  // Objeto para la música de fondo


    public GameView(Context context, int screenX, int screenY, GameActivity gameActivity) {
        super(context);

        // Inicializar el MediaPlayer con el archivo de música (asegúrate de tener my_music.mp3 en res/raw)
        backgroundMusic = MediaPlayer.create(context, R.raw.musica_main);
        backgroundMusic.setLooping(true);  // Reproduce en bucle

        // Inicializar el Paint para el fondo
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.BLACK);

        tiempoInicio = System.currentTimeMillis();
        Log.d("GameView", "tiempoInicio: " + tiempoInicio);

        // Inicializar la lista de tiempos de activación del Boss (en segundos)
        bossActivationTimes = new ArrayList<>();
        bossActivationTimes.add(10);  // Primer Boss a los 10 segundos
        bossActivationTimes.add(30);  // Segundo Boss a los 30 segundos
        bossActivationTimes.add(50);  // Tercer Boss a los 50 segundos

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

        // Inicializar Explosion
        boom = new Boom(context);

        // Inicializar Boss
        boss = new Boss(context, screenX, screenY);
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

    // Inicia una transición suave al color objetivo
    private void startBackgroundTransition(int targetColor) {
        currentBackgroundColor = backgroundPaint.getColor(); // Color actual
        targetBackgroundColor = targetColor; // Color al que se quiere llegar
        transitionStartTime = System.currentTimeMillis(); // Inicia la transición
    }

    // Actualiza el color de fondo interpolando entre el color actual y el objetivo
    private void updateBackgroundColor() {
        if (transitionStartTime > 0) {
            long elapsedTime = System.currentTimeMillis() - transitionStartTime;
            float fraction = (float) elapsedTime / TRANSITION_DURATION;

            if (fraction >= 1.0f) {
                // Finaliza la transición
                backgroundPaint.setColor(targetBackgroundColor);
                transitionStartTime = 0;
            } else {
                // Interpolación lineal entre colores
                int red = (int) (Color.red(currentBackgroundColor) + fraction * (Color.red(targetBackgroundColor) - Color.red(currentBackgroundColor)));
                int green = (int) (Color.green(currentBackgroundColor) + fraction * (Color.green(targetBackgroundColor) - Color.green(currentBackgroundColor)));
                int blue = (int) (Color.blue(currentBackgroundColor) + fraction * (Color.blue(targetBackgroundColor) - Color.blue(currentBackgroundColor)));
                backgroundPaint.setColor(Color.rgb(red, green, blue));
            }
        }
    }

    private void update() {
        updateBackgroundColor();

        player.update();

        // Mover la explosión fuera de la pantalla
        boom.setX(-250);
        boom.setY(-250);

        // Disparo automático de balas
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastShotTime >= SHOT_INTERVAL) {
            bullets.add(player.shoot(getContext()));
            lastShotTime = currentTime;
        }

        // Actualizar balas
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (bullet.isActive()) {
                bullet.update();
            } else {
                bullets.remove(i);
                i--;
            }
        }

        float speedMultiplier = getSpeedMultiplier();

        // Actualizar enemigos
        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i].isActive()) {
                enemies[i].update(speedMultiplier);
            } else {
                enemies[i] = new Enemy(getContext(), getWidth(), getHeight());
            }
        }

        // Actualizar Boss y verificar colisiones con balas
        if (boss != null && boss.isActive() && !boss.isDefeated()) {
            boss.update();
            Log.d("GameView", "boss.isActive");

            for (int i = 0; i < bullets.size(); i++) {
                Bullet bullet = bullets.get(i);
                if (bullet.isActive() && Rect.intersects(bullet.getDetectCollision(), boss.getDetectCollision())) {
                    bullet.setInactive();
                    boss.takeDamage();
                    // Posicionar la explosión en la colisión
                    boom.setX(boss.getX());
                    boom.setY(boss.getY());

                    if (boss.getHealth() <= 0) {
                        boss.setInactive();
                        bossesDefeated++;
                        Log.d("GameView", "Boss derrotado");
                        // Si ya se han activado todos los Boss (el tercer Boss) se lanza WinActivity
                        if (currentBossActivationIndex >= bossActivationTimes.size()) {
                            Intent winIntent = new Intent(getContext(), WinActivity.class);
                            winIntent.putExtra("enemiesDefeated", enemiesDefeated);
                            winIntent.putExtra("timeSurvived", (System.currentTimeMillis() - tiempoInicio) / 1000);
                            getContext().startActivity(winIntent);
                            // Finaliza el GameActivity para evitar actualizaciones o colisiones posteriores
                            ((Activity) getContext()).finish();
                            return; // Salir para evitar más actualizaciones
                        }
                    }
                }
            }
        }

        // Verificar colisiones entre jugador y enemigos
        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i].isActive() && Rect.intersects(player.getDetectCollision(), enemies[i].getDetectCollision())) {
                Intent gameOverIntent = new Intent(getContext(), GameOverActivity.class);
                gameOverIntent.putExtra("enemiesDefeated", enemiesDefeated);
                gameOverIntent.putExtra("bossesDefeated", bossesDefeated);
                gameOverIntent.putExtra("timeSurvived", (System.currentTimeMillis() - tiempoInicio) / 1000);
                getContext().startActivity(gameOverIntent);
                return;
            }
        }

        // Verificar colisiones entre balas y enemigos
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (bullet.isActive()) {
                for (int j = 0; j < enemies.length; j++) {
                    if (enemies[j].isActive() && Rect.intersects(bullet.getDetectCollision(), enemies[j].getDetectCollision())) {
                        bullet.setInactive();
                        enemies[j].setInactive();
                        enemiesDefeated++;
                        boom.setX(enemies[j].getX());
                        boom.setY(enemies[j].getY());
                        break;
                    }
                }
            }
        }

        // Comprobación extra de colisiones entre jugador y enemigos
        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i].isActive() && Rect.intersects(player.getDetectCollision(), enemies[i].getDetectCollision())) {
                Intent gameOverIntent = new Intent(getContext(), GameOverActivity.class);
                gameOverIntent.putExtra("enemiesDefeated", enemiesDefeated);
                gameOverIntent.putExtra("bossesDefeated", bossesDefeated);
                gameOverIntent.putExtra("timeSurvived", (System.currentTimeMillis() - tiempoInicio) / 1000);
                getContext().startActivity(gameOverIntent);
                return;
            }
        }

        // Activación del Boss según el tiempo jugado
        long tiempoJugado = (System.currentTimeMillis() - tiempoInicio) / 1000;
        Log.d("GameView", "Tiempo jugado: " + tiempoJugado);

        if (currentBossActivationIndex < bossActivationTimes.size()) {
            int nextActivationTime = bossActivationTimes.get(currentBossActivationIndex);
            if (tiempoJugado >= nextActivationTime && !boss.isActive()) {
                // Si el Boss fue derrotado previamente, se crea una nueva instancia para permitir la reactivación
                if (boss.isDefeated()) {
                    boss = new Boss(getContext(), getWidth(), getHeight());
                }
                switch (currentBossActivationIndex) {
                    case 0: // Primer Boss
                        startBackgroundTransition(Color.BLACK);
                        bossSound = MediaPlayer.create(getContext(), R.raw.boss_sound);
                        boss.activate(5);
                        bossSound.start();
                        break;
                    case 1: // Segundo Boss
                        startBackgroundTransition(Color.parseColor("#4B0082")); // Morado oscuro
                        bossSound = MediaPlayer.create(getContext(), R.raw.boss_sound);
                        boss.activate(10);
                        bossSound.start();
                        break;
                    case 2: // Tercer Boss
                        startBackgroundTransition(Color.parseColor("#8B0000")); // Rojo oscuro
                        bossSound = MediaPlayer.create(getContext(), R.raw.boss_sound);
                        boss.activate(20);
                        bossSound.start();
                        break;
                }
                currentBossActivationIndex++;
                Log.d("GameView", "Boss activado en: " + nextActivationTime + "s");
            }
        }
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundPaint);

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

            // Dibujar explosión
            canvas.drawBitmap(boom.getBitmap(), boom.getX(), boom.getY(), paint);

            // Dibujar Boss si está activo
            if (boss.isActive()) {
                canvas.drawBitmap(boss.getBitmap(), boss.getX(), boss.getY(), paint);
            }

            // Mostrar el tiempo jugado en pantalla
            long tiempoJugado = (System.currentTimeMillis() - tiempoInicio) / 1000;
            long minutos = tiempoJugado / 60;
            long segundos = tiempoJugado % 60;
            paint.setColor(Color.WHITE);
            paint.setTextSize(50);
            canvas.drawText("Tiempo: " + minutos + "m " + segundos + "s", 50, 100, paint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            gameThread.sleep(17); // Aproximadamente 60 FPS
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

        // Pausa la música de fondo si se está reproduciendo
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();

        // Inicia la música de fondo si no se está reproduciendo
        if (backgroundMusic != null && !backgroundMusic.isPlaying()) {
            backgroundMusic.start();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float touchX = motionEvent.getX();
                float screenWidth = getWidth();
                if (touchX < screenWidth / 2) {
                    player.moveLeft();
                } else {
                    player.moveRight();
                }
                break;
            case MotionEvent.ACTION_UP:
                player.stopMoving();
                break;
        }
        return true;
    }

    private float getSpeedMultiplier() {
        long tiempoJugado = (System.currentTimeMillis() - tiempoInicio) / 1000;
        return 1.0f + (tiempoJugado / 60.0f);
    }
}