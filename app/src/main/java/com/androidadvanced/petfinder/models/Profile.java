package com.androidadvanced.petfinder.models;

import com.androidadvanced.petfinder.database.BaseEntity;
import com.androidadvanced.petfinder.database.Entity;

import java.text.SimpleDateFormat;
import java.util.Locale;

@Entity(value = "users")
public class Profile extends BaseEntity {
    private String fullName;
    private String photoUrl;
    private String activeSince;
    private Contact contact;

    public Profile() {
        this.contact = new Contact();
    }

    public Profile(AuthProfile auth) {
        this.id = auth.getUid();
        this.fullName = auth.getDisplayName();
        if (auth.getPhotoUrl() != null)
            this.photoUrl = auth.getPhotoUrl().toString();

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);
        this.activeSince = df.format(auth.getCreationTime());
        this.contact = new Contact();
        this.contact.setPhoneNumber(auth.getPhoneNumber());
        this.contact.setEmail(auth.getEmail());
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = "null".equals(photoUrl) ? null : photoUrl;
    }

    public String getActiveSince() {
        return this.activeSince;
    }

    public void setActiveSince(String activeSince) {
        this.activeSince = activeSince;
    }
}
