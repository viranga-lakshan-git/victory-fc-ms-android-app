package com.example.laksh.victoryfc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Achievements extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achivements);

        getSupportActionBar().hide();
    }

    public void btnRunnersUpOnClick(View view) {
        Intent runnersUp = new Intent(this, RunnersUp.class);
        startActivity(runnersUp);
    }

    public void btnChampionsOnClick(View view) {
        Intent champions = new Intent(this, Champions.class);
        startActivity(champions);
    }
}