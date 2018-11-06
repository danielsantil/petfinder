package com.androidadvanced.petfinder.models;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class Post {
    private Long id;
    private Long userId;
    private String description;
    private Date pubDate;
    private Pet pet;
    private Stats stats;
    private Contact contact;

    public Post() {
    }

    public Post(String name, String lastSeen, byte[] bytes, int seen, int helping) {
        this.pet = new Pet();
        pet.setName(name);
        pet.setLastSeenAddress(StringUtils.substring(lastSeen, 0, 90));
        pet.setPicture(bytes);
        this.stats = new Stats();
        stats.setSeen(seen);
        stats.setHelping(helping);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
