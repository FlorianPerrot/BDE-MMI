package com.bde.lpsmin.bdemmi;

/**
 * Created by lheido on 25/09/14.
 */
public class Event extends Actu {

    private String lieu;

    public Event(String title, String content, String imgUri, String lieu, String date) {
        super(title, content, imgUri, date);
        this.lieu = lieu;
    }

    public String getLieu() {
        return lieu;
    }

}
