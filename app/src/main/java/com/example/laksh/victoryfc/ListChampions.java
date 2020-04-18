package com.example.laksh.victoryfc;

public class ListChampions {
    int year;
    String description;

    public ListChampions(int year, String description) {
        this.year = year;
        this.description = description;
    }

    public int getYear() {
        return year;
    }

    public String getDescription() {
        return description;
    }
}