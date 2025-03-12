package com.example.mygame.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mygame.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private AdView adView;
    private Button buttonPlay;
    private Button buttonCredits;
    private Button buttonExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar AdMob
        MobileAds.initialize(this, initializationStatus -> {
            // AdMob se ha inicializado correctamente
        });

        // Cargar el anuncio
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        setupUI();
    }

    private void setupUI() {
        buttonPlay = findViewById(R.id.buttonPlay);
        buttonCredits = findViewById(R.id.buttonCredits);
        buttonExit = findViewById(R.id.buttonExit);

        buttonPlay.setOnClickListener(this);
        buttonCredits.setOnClickListener(this);
        buttonExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId(); // Obtener el ID del view clickeado

        if (id == R.id.buttonPlay) {
            // Abrir GameActivity
            startActivity(new Intent(this, GameActivity.class));
        } else if (id == R.id.buttonCredits) {
            // Abrir CreditsActivity
            startActivity(new Intent(this, CreditsActivity.class));
        } else if (id == R.id.buttonExit) {
            // Cierra todas las actividades de la app
            finishAffinity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (adView != null) {
            adView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adView != null) {
            adView.destroy();
        }
    }
}