package com.bde.lpsmin.bdemmi;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by lheido on 25/09/14.
 */
public class Event extends Actu {

    public static final boolean HISTORIQUE = false;
    public static final boolean PROCHAINEMENT = true;
	
    private String lieu;

    public Event(String title, String content, String imgUri, String lieu, String date) {
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
