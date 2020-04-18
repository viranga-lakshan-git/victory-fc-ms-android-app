package com.example.laksh.victoryfc;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        getSupportActionBar().hide();
    }

    public void btnManagerOnClick(View view) {
        Intent manager = new Intent(this, Manager.class);
        startActivity(manager);
    }

    public void btnPlayerOnClick(View view) {
        Intent players = new Intent(this, Players.class);
        startActivity(players);
    }

    public void btnAchievementOnClick(View view) {
        Intent achievements = new Intent(this, Achievements.class);
        startActivity(achievements);
    }

    public void btnGalleryOnClick(View view) {
        Intent gallery = new Intent(this, Gallery.class);
        startActivity(gallery);
    }

    public void btnFixturesOnClick(View view) {
        Intent fixtures = new Intent(this, Fixtures.class);
        startActivity(fixtures);
    }

    public void btnMerchandiseOnClick(View view) {
        Intent merchandise = new Intent(this, Merchandise.class);
        startActivity(merchandise);
    }

    public void btnLogoutOnClick(View view) {
        Intent login = new Intent(this, Login.class);
        startActivity(login);
    }

    public void btnExitOnClick(View view) {
        System.exit(1);
    }
}

