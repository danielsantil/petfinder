package com.androidadvanced.petfinder.auth;

import com.androidadvanced.petfinder.models.AuthProfile;
import com.androidadvanced.petfinder.models.Credentials;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthHelper implements Authenticator {

    private final FirebaseAuth auth;

    public FirebaseAuthHelper(FirebaseAuth authInstance) {
        this.auth = authInstance;
    }

    @Override
    public void signIn(Credentials creds, AuthListener listener) {
        this.auth.signInWithEmailAndPassword(creds.getEmail(), creds.getPassword())
                .addOnSuccessListener(authResult -> listener.onAuthSuccess())
                .addOnFailureListener(e -> listener.onAuthError(e.getMessage()));
    }

    @Override
    public void signUp(Credentials creds, AuthListener listener) {
        this.auth.createUserWithEmailAndPassword(creds.getEmail(), creds.getPassword())
                .addOnSuccessListener(authResult -> listener.onAuthSuccess())
                .addOnFailureListener(e -> listener.onAuthError(e.getMessage()));
    }

    @Override
    public boolean isUserActive() {
        return this.auth.getCurrentUser() != null;
    }

    @Override
    public void signOut() {
        this.auth.signOut();
    }

    @Override
    public AuthProfile getCurrentUser() {
        FirebaseUser user = this.auth.getCurrentUser();
        AuthProfile profile = new AuthProfile();
        profile.setUid(user.getUid());
        profile.setDisplayName(user.getDisplayName());
        profile.setPhotoUrl(user.getPhotoUrl());
        profile.setCreationTime(user.getMetadata().getCreationTimestamp());
        profile.setEmail(user.getEmail());
        profile.setPhoneNumber(user.getPhoneNumber());
        return profile;
    }

}