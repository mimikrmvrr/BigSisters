package com.bigsisters.bigsisters;

/**
 * Created by Stefania on 8/6/15.
 */
public class University {
    int id;
    String name;
    String location;
    String websiteUrl;
    String photoUrl;

    public University() {
        id = 0;
        name = null;
        location = null;
        websiteUrl = null;
        photoUrl = null;
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

    public String toString() {
        return id + " " + name + " " + location + " " + websiteUrl + " " + photoUrl;
    }
}
