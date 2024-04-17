package com.example.project_5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserPurchasesActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_purchases);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseHelper db = new DatabaseHelper(this);

        // SharedPreferences
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", 0);

        // get all the purchases of the user
        List<Item> purchases = db.getUserSales(userId);

        // show the purchases in the recycler view
        Adapter adapter = new Adapter(purchases, db, userId, false);
        recyclerView.setAdapter(adapter);
        //logout button
        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // erase the user id from the preferences
                sharedPreferences.edit().remove("user_id").apply();
                // go back to the main activity
                Intent intent = new Intent(UserPurchasesActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}