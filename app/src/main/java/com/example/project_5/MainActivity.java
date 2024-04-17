package com.example.project_5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupButtonListeners();
        databaseHelper = new DatabaseHelper(this);
        databaseHelper.getWritableDatabase(); // Open the database
        // create objetc
        Item item = new Item("T-shirt size S", 26.00);
        Item item2 = new Item("T-shirt size M", 31.00);
        Item item3 = new Item("T-shirt size X", 36.00);
        Item item4 = new Item("T-shirt size XL", 41.00);

        // create instance and add to database
        DatabaseHelper db = new DatabaseHelper(this);
        db.addItem(item);
        db.addItem(item2);
        db.addItem(item3);
        db.addItem(item4);

    }

    private void setupButtonListeners() {
        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LogIn.class);
                startActivity(intent);
            }
        });

        Button buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });


    }
}