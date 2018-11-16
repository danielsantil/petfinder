package com.androidadvanced.petfinder.database;

public class BaseEntity {
    protected String id;

    protected BaseEntity() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
