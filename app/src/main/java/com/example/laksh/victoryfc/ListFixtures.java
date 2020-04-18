package com.example.laksh.victoryfc;

public class ListFixtures {
    String date;
    String time;
    String venue;
    String versus;
    String ground;

    public ListFixtures(String date, String time, String venue, String versus, String ground) {
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.versus = versus;
        this.ground = ground;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getVenue() {
        return venue;
    }

    public String getVersus() {
        return versus;
    }

    public String getGround() {
        return ground;
    }
}
