package com.example.edominer_project;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ImageTextDao {
    @Insert
    void insert(ImageTextEntity imageText);

    @Query("SELECT * FROM image_texts")
    List<ImageTextEntity> getAllImageTexts();
}