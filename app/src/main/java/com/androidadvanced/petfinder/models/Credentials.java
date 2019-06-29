package com.androidadvanced.petfinder.models;

import android.content.Context;
import android.util.Patterns;

import com.androidadvanced.petfinder.R;

public class Credentials {
    private String email;
    private String password;

    public Credentials(String email, String password, Context context) {
        // TODO implement bean validation
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw new IllegalArgumentException(context.getString(R.string.invalid_email_address));
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException(context.getString(R.string.invalid_password_length));
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
