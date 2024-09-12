package com.example.edominer_project;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageTextAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load data from the Room database
        AppDatabase db = AppDatabase.getDatabase(this);
        new Thread(() -> {
            List<ImageTextEntity> entities = db.imageTextDao().getAllImageTexts(); // Fetch all records
            runOnUiThread(() -> {
                adapter = new ImageTextAdapter(entities);
                recyclerView.setAdapter(adapter);
            });
        }).start();
    }
}