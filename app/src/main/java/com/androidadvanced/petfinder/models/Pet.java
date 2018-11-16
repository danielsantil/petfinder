package com.androidadvanced.petfinder.models;

public class Pet {
    private String name;
    private String photoUrl;
    private String lastSeenAddress;
    private String latLong;

    public Pet() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getLastSeenAddress() {
        return lastSeenAddress;
    }

    public void setLastSeenAddress(String lastSeenAddress) {
        this.lastSeenAddress = lastSeenAddress;
    }

    public String getLatLong() {
        return latLong;
    }

    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }
}
