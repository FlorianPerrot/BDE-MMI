package com.bde.lpsmin.bdemmi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by lheido on 25/09/14.
 */
public class Actu {

    private String title;
    private String content;
    private String imgUri;
    private Date date;

    public Actu(String title, String content, String imgUri, String date) {
        this.title = title;
        this.content = content;
        this.imgUri = imgUri;
        this.setDate(date);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(String date) {
        try {
            this.date = new SimpleDateFormat(Utils.DATE_FORMAT).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getDateFormated(){
        return new SimpleDateFormat(Utils.DATE_FORMAT_DISPLAY).format(date);
    }
    
}
