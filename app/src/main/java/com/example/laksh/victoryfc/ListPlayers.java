package com.example.laksh.victoryfc;

import java.sql.Blob;

public class ListPlayers {
    public Blob image;
    public String name;
    public String dob;
    public String position;
    public Float height;
    public Float weight;
    public int matches;
    public int goals;
    public int assists;
    public int red;
    public int yellow;

    public ListPlayers(Blob image, String name, String dob, String position, Float height, Float weight, int matches, int goals, int assists, int red, int yellow) {
        this.image=image;
        this.name = name;
        this.dob = dob;
        this.position = position;
        this.height = height;
        this.weight = weight;
        this.matches = matches;
        this.goals = goals;
        this.assists = assists;
        this.red = red;
        this.yellow = yellow;
    }

    public Blob getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getDob() {
        return dob;
    }

    public String getPosition() {
        return position;
    }

    public Float getHeight() {
        return height;
    }

    public Float getWeight() {
        return weight;
    }

    public int getMatches() {
        return matches;
    }

    public int getGoals() {
        return goals;
    }

    public int getAssists() {
        return assists;
    }

    public int getRed() {
        return red;
    }

    public int getYellow() {
        return yellow;
    }
}
