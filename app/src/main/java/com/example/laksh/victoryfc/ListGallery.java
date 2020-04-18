package com.example.laksh.victoryfc;

import java.sql.Blob;

public class ListGallery {
    String description;
    Blob image;

    public ListGallery(String description,Blob image){
        this.description=description;
        this.image=image;
    }

    public String getDescription() {
        return description;
    }

    public Blob getImage() {
        return image;
    }
}
