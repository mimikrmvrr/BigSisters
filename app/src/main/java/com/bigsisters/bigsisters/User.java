package com.bigsisters.bigsisters;

import java.util.ArrayList;

/**
 * Created by demouser on 8/7/15.
 */
public class User {

    private String id;
    private String name;
    private String email;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    private String photoUrl;
    private ArrayList<String> pastUnis;
    private ArrayList<String> currentUnis;
    private ArrayList<String> faveUnis;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public ArrayList<String> getFaveUnis() {
        return faveUnis;
    }

    public void setFaveUnis(ArrayList<String> faveUnis) {
        this.faveUnis = faveUnis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getPastUnis() {
        return pastUnis;
    }

    public void setPastUnis(ArrayList<String> pastUnis) {
        this.pastUnis = pastUnis;
    }

    public ArrayList<String> getCurrentUnis() {
        return currentUnis;
    }

    public void setCurrentUnis(ArrayList<String> currentUnis) {
        this.currentUnis = currentUnis;
    }



}
