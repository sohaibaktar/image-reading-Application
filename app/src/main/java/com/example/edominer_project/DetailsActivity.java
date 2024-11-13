package com.example.edominer_project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailsActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details);


        imageView = findViewById(R.id.detail_image_view);
        textView = findViewById(R.id.detail_text_view);

        // Get the data from the Intent
        Intent intent = getIntent();
        String imagePath = intent.getStringExtra("imagePath");
        String text = intent.getStringExtra("text");

        // Set the data to the views
        if (imagePath != null) {
            imageView.setImageURI(Uri.parse(imagePath));
        }
        textView.setText(text);
    }
}