package com.example.edominer_project;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "image_texts")
public class ImageTextEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String imagePath;
    public String text;
}