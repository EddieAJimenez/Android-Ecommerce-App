package com.example.project_5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LogIn extends AppCompatActivity {

    private EditText userIdEditText;
    private EditText passwordEditText;

    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userIdEditText = findViewById(R.id.userIdTxt);
        passwordEditText = findViewById(R.id.passwordTxt);

        databaseHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        Button loginButton = findViewById(R.id.LogInBtn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userIdEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // get the id of the user
                int userId = databaseHelper.getUserId(userName, password);

                // password and username validation
                if (userId != -1) {
                    Toast.makeText(LogIn.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    // save id in preferences
                    sharedPreferences.edit().putInt("user_id", userId).apply();
                    Intent intent = new Intent(LogIn.this, ShopActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LogIn.this, "Invalid username or password!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void displayUserId() {
        int userId = sharedPreferences.getInt("user_id", 0);
        Toast.makeText(this, "User ID: " + userId, Toast.LENGTH_LONG).show();
    }
}