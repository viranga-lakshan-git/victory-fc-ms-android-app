package com.example.laksh.victoryfc;

import java.sql.Blob;

public class ListMerchandise {
    int itemID;
    Blob image;
    String itemName;
    Float itemPrice;

    public ListMerchandise(int itemID, Blob image, String itemName, Float itemPrice) {
        this.itemID = itemID;
        this.image = image;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public int getItemID() {
        return itemID;
    }

    public Blob getImage() {
        return image;
    }

    public String getItemName() {
        return itemName;
    }

    public Float getItemPrice() {
        return itemPrice;
    }
}