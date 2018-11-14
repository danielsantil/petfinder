package com.androidadvanced.petfinder.activities;

import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public abstract class BaseActivity extends AppCompatActivity {

    protected FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    protected abstract String getActivityName();
}
