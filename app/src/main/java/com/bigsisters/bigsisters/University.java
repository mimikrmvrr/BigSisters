package com.bigsisters.bigsisters;

/**
 * Created by Stefania on 8/6/15.
 */
public class University {

    final int nrOfAttributes = 4;

    int id;
    String name;
    String location;
    String websiteUrl;
    String photoUrl;
    String info;
    double[] ratings;
    int[] votes;

    public University() {
        id = 0;
        name = null;
        location = null;
        websiteUrl = null;
        photoUrl = null;
        info = null;
        ratings = new double[] {0, 0, 0, 0};
        votes = new int[] {0, 0, 0, 0};
    }

    // get and set methods
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getLocation() {
        return location;
    }
    public String getWebsiteUrl() {
        return websiteUrl;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }
    public String getInfo() { return info; }
    public double[] getRatings() {return ratings;}

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    public void setInfo(String info) { this.info = info;}
    public void setRatings(double[] ratings) {
        if (ratings.length != nrOfAttributes) return;
        for (int i = 0; i < nrOfAttributes; i++) this.ratings[i] = ratings[i];
    }

    public String toString() {
        return id + " " + name + " " + location + " " + websiteUrl + " " + photoUrl;
    }
}
