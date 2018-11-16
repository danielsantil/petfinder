package com.androidadvanced.petfinder.models;

import android.util.Patterns;

public class Credentials {
    private String email;
    private String password;

    public Credentials(String email, String password) {
        // TODO implement bean validation
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw new IllegalArgumentException("Please enter a valid email address");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password length is below 8 characters");
        }
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
