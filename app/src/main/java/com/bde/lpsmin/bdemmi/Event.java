package com.bde.lpsmin.bdemmi;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;

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
    static public ArrayList<Event> getActuListFromJson(boolean typeDEvent,Context context) {
    	
    	final ArrayList<Event> eventArray = new ArrayList<Event>();
    	
    	if(typeDEvent){
			Ion.with(context)
			.load("http://localhost/bde/json.php?json=1")
			.asJsonArray()
			.setCallback(new FutureCallback<JsonArray>(){
				public void onCompleted(Exception e, JsonArray result) {
			    	
					for(int i=0; i<result.size() ;i++){
						eventArray.add(new Event(
								result.get(i).getAsJsonObject().get("title").getAsString(),
								result.get(i).getAsJsonObject().get("conent").getAsString(),
								result.get(i).getAsJsonObject().get("image_url").getAsString(),
								result.get(i).getAsJsonObject().get("date").getAsString(),
								result.get(i).getAsJsonObject().get("lieu").getAsString()
						));
					}
				}
			});
    	}
    	else{
			Ion.with(context)
			.load("http://localhost/bde/json.php?json=0")
			.asJsonArray()
			.setCallback(new FutureCallback<JsonArray>(){
				public void onCompleted(Exception e, JsonArray result) {
			    	
					for(int i=0; i<result.size() ;i++){
						eventArray.add(new Event(
								result.get(i).getAsJsonObject().get("title").getAsString(),
								result.get(i).getAsJsonObject().get("conent").getAsString(),
								result.get(i).getAsJsonObject().get("image_url").getAsString(),
								result.get(i).getAsJsonObject().get("date").getAsString(),
								result.get(i).getAsJsonObject().get("lieu").getAsString()
						));
					}
				}
			});  		
    	}
		
		return eventArray;
    }
}
