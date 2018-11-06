package com.androidadvanced.petfinder.models;

public class Pet {
    private String name;
    private byte[] picture;
    private String lastSeenAddress;

    public Pet() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getLastSeenAddress() {
        return lastSeenAddress;
    }

    public void setLastSeenAddress(String lastSeenAddress) {
        this.lastSeenAddress = lastSeenAddress;
    }
}
