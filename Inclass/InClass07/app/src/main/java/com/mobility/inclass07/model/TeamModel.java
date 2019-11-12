package com.mobility.inclass07.model;

import com.google.firebase.database.Exclude;

import java.util.Map;

public class TeamModel {

    private String name;
    private String id;
    private Map<String, Double> ratings;
    private Double avgRatings;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Double> getRatings() {
        return ratings;
    }

    public void setRatings(Map<String, Double> ratings) {
        this.ratings = ratings;
    }

    @Exclude
    public Double getAvgRatings() {
        return avgRatings;
    }

    public void setAvgRatings(Double avgRatings) {
        this.avgRatings = avgRatings;
    }
}
