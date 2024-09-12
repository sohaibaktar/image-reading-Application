package com.example.edominer_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button uploadimage_btn, save_btn,goToHistory_btn;
    private ImageView imageView;
    private TextView tv1;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //initilization
        initialization();
        //image upload
        uploadimage_btn.setOnClickListener(v -> ImagePicker.with(MainActivity.this)
                .crop()	    			// Crop image (Optional)
                .compress(1024)			// Compress image (Optional)
                .maxResultSize(1080, 1080)	// Max result size (Optional)
                .start(101)
        );

        //  save data to db
        save_btn.setOnClickListener(v -> {
            if (uri != null && !tv1.getText().toString().isEmpty()) {
                saveToDatabase(uri.toString(), tv1.getText().toString());
                Toast.makeText(MainActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "No image or text to save", Toast.LENGTH_SHORT).show();
            }
        });

        // go to next activity to view all History
        goToHistory_btn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK) {
            uri = data.getData();
            if (uri != null) {
                try {
                    Bitmap bitmap;
                    // Check for Android version to use the appropriate method
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), uri);
                        bitmap = ImageDecoder.decodeBitmap(source);
                    } else {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    }
                    imageView.setImageBitmap(bitmap);
                    processImage(bitmap); // Pass URI as a string
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void processImage(Bitmap bitmap) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);

        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                .process(image)
                .addOnSuccessListener(text -> {
                    String recognizedText = text.getText();
                    tv1.setText(recognizedText);

                    // Save to database
                    //saveToDatabase(uri.toString(), recognizedText);
                })
                .addOnFailureListener(e -> {
                    Log.e("TextRecognition", "Text recognition failed", e);
                    Toast.makeText(MainActivity.this, "Text recognition failed", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveToDatabase(String imagePath, String text) {
        AppDatabase db = AppDatabase.getDatabase(this);
        new Thread(() -> {
            ImageTextEntity entity = new ImageTextEntity();
            entity.imagePath = imagePath;
            entity.text = text;
            db.imageTextDao().insert(entity);
        }).start();
    }

    private void initialization() {
        imageView = (ImageView) findViewById(R.id.image_view);
        uploadimage_btn = (Button) findViewById(R.id.btn_upload_image);
        tv1 = (TextView) findViewById(R.id.text_view);
        goToHistory_btn = (Button) findViewById(R.id.btn_go_to_history);
        save_btn = (Button)findViewById(R.id.btn_save);
    }
}