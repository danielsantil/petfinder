package com.androidadvanced.petfinder.models;

import com.androidadvanced.petfinder.database.BaseEntity;
import com.androidadvanced.petfinder.database.Entity;

import java.util.List;

@Entity("posts")
public class Post extends BaseEntity {
    private String userId;
    private String description;
    private String pubDate;
    private Pet pet;
    private List<String> helping;

    public Post() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public List<String> getHelping() {
        return helping;
    }

    public void setHelping(List<String> helping) {
        this.helping = helping;
    }
}
