package dev.wildtraveling.Domain;

import dev.wildtraveling.Util.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pere on 5/30/17.
 */
public class Note implements Entity {
    private String id;
    private String title;
    private String text;
    private String tripId;
    private int color;
    private List<String> travelerTags;

    public Note(String title, String text, String tripId, int color, List<String> travelerTags) {
        this.title = title;
        this.text = text;
        this.tripId = tripId;
        this.color = color;
        this.travelerTags = travelerTags;
    }

    public Note(){
        this.travelerTags = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public List<String> getTravelerTags() {
        return travelerTags;
    }

    public void setTravelerTags(List<String> travelerTags) {
        this.travelerTags = travelerTags;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }
}
