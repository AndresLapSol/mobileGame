package com.example.mygame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{

    //image button
    private Button buttonPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //señalando el button
        buttonPlay = (Button) findViewById(R.id.buttonPlay);

        //click listener
        buttonPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //Empezar GameActivity
        startActivity(new Intent(this, GameActivity.class));
    }
}