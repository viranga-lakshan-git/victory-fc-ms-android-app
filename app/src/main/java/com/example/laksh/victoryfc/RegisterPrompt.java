package com.example.laksh.victoryfc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

public class RegisterPrompt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_prompt);

        getSupportActionBar().hide();
    }

    public void btnRegisterFanOnClick(View view) {
        Intent RegisterFan = new Intent(this, RegisterFan.class);
        startActivity(RegisterFan);
    }

    public void btnRegisterPlayerOnClick(View view) {
        Intent RegisterPlayer = new Intent(this, RegisterPlayer.class);
        startActivity(RegisterPlayer);
    }

}

