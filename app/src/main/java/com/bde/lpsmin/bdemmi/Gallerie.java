package com.bde.lpsmin.bdemmi;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by lheido on 15/12/14.
 */
public class Gallerie {

    private final ArrayList<Image> images;
    private String title;

    public Gallerie(String title, JsonArray asJsonArray) {
        this.title = title;
        this.images = new ArrayList<Image>();
        for(JsonElement elt : asJsonArray){
            this.images.add(new Image(elt.getAsJsonObject()));
        }
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public String getTitle() {
        return title;
    }

    public static class Image{
        private String title;
        private String url;

        public Image(JsonObject elt) {
            title = elt.get(Utils.JSON_IMAGE_TITLE).getAsString();
            url = elt.get(Utils.JSON_IMAGE_URL).getAsString();
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }
    }
}
