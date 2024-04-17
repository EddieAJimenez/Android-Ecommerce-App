package com.example.project_5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import android.os.Bundle;

public class SignUp extends AppCompatActivity {

    private EditText userNameEditText;
    private EditText passwordEditText;
    private EditText realNameEditText;
    private EditText addressEditText;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userNameEditText = findViewById(R.id.userNameTxt);
        passwordEditText = findViewById(R.id.passwordTxt);
        realNameEditText = findViewById(R.id.realNameTxt);
        addressEditText = findViewById(R.id.addressTxt);

        databaseHelper = new DatabaseHelper(this);

        Button signUpButton = findViewById(R.id.SignUpBtn);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String realName = realNameEditText.getText().toString();
                String address = addressEditText.getText().toString();

                if (databaseHelper.getUserId(userName, password) == -1) {
                    databaseHelper.addUser(userName, password, realName, address);
                    Toast.makeText(SignUp.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUp.this, LogIn.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignUp.this, "Username already exists!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}