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
        this.pubDate = new Date();
        this.description = "Where does it come from?\n" +
                "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots " +
                "in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32.";
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
