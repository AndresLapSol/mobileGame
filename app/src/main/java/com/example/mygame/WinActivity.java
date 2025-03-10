package com.example.mygame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WinActivity extends AppCompatActivity {

    private TextView enemiesDefeatedTextView;
    private TextView timeSurvivedTextView;
    private Button restartButton;
    private Button menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win); // Asegúrate de tener este layout

        // Obtener los datos del Intent
        Intent intent = getIntent();
        int enemiesDefeated = intent.getIntExtra("enemiesDefeated", 0);
        long timeSurvived = intent.getLongExtra("timeSurvived", 0);

        // Inicializar las vistas
        enemiesDefeatedTextView = findViewById(R.id.enemiesDefeatedTextView);
        timeSurvivedTextView = findViewById(R.id.timeSurvivedTextView);
        restartButton = findViewById(R.id.restartButton);
        menuButton = findViewById(R.id.menuButton);

        // Mostrar los datos
        enemiesDefeatedTextView.setText("Enemigos derrotados: " + enemiesDefeated);
        timeSurvivedTextView.setText("Tiempo sobrevivido: " + formatTime(timeSurvived));

        // Configurar el botón de reiniciar
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent restartIntent = new Intent(WinActivity.this, GameActivity.class);
                startActivity(restartIntent);
                finish(); // Cerrar la actividad actual
            }
        });

        // Configurar el botón de menú
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menuIntent = new Intent(WinActivity.this, MenuActivity.class);
                startActivity(menuIntent);
                finish(); // Cerrar la actividad actual
            }
        });
    }

    // Método para formatear el tiempo en minutos y segundos
    private String formatTime(long timeInSeconds) {
        long minutes = timeInSeconds / 60;
        long seconds = timeInSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
