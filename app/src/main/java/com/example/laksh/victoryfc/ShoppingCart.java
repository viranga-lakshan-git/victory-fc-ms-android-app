package com.example.laksh.victoryfc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ShoppingCart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart);

        getSupportActionBar().hide();
    }
}
