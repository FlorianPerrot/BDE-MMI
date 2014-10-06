package com.bde.lpsmin.bdemmi;

import java.util.Date;

/**
 * Created by lheido on 25/09/14.
 */
public class Event extends Actu {

    private String lieu;

    public Event(String title, String content, String imgUri, String lieu, Date date) {
        super(title, content, imgUri, date);
        this.lieu = lieu;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }
}
