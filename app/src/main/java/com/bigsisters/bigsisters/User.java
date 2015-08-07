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
    private ArrayList<University> pastUnis;
    private ArrayList<University> currentUnis;
    private ArrayList<University> faveUnis;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public ArrayList<University> getFaveUnis() {
        return faveUnis;
    }

    public void setFaveUnis(ArrayList<University> faveUnis) {
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

    public ArrayList<University> getPastUnis() {
        return pastUnis;
    }

    public void setPastUnis(ArrayList<University> pastUnis) {
        this.pastUnis = pastUnis;
    }

    public ArrayList<University> getCurrentUnis() {
        return currentUnis;
    }

    public void setCurrentUnis(ArrayList<University> currentUnis) {
        this.currentUnis = currentUnis;
    }



}
