package com.example.mygame.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mygame.GameView;


public class GameActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener las dimensiones de la pantalla
        int screenX = getResources().getDisplayMetrics().widthPixels;
        int screenY = getResources().getDisplayMetrics().heightPixels;

        // Crear una instancia de GameView y pasar el contexto y la referencia de GameActivity
        gameView = new GameView(this, screenX, screenY, this);

        // Establecer GameView como la vista principal de la actividad
        setContentView(gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pausar el juego cuando la actividad esté en pausa
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reanudar el juego cuando la actividad se reanude
        gameView.resume();
    }

    // Método para iniciar GameOverActivity
    public void gameOver() {
        Intent gameOverIntent = new Intent(this, GameOverActivity.class);
        startActivity(gameOverIntent);
        finish(); // Cerrar GameActivity
    }
}