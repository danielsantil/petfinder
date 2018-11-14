package com.androidadvanced.petfinder.auth;

import com.androidadvanced.petfinder.models.Credentials;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthHelper implements Authenticator {

    private final FirebaseAuth auth;

    public FirebaseAuthHelper(FirebaseAuth authInstance) {
        this.auth = authInstance;
    }

    @Override
    public void signIn(Credentials creds, AuthListener listener) {
        auth.signInWithEmailAndPassword(creds.getEmail(), creds.getPassword()).addOnCompleteListener
                (task -> {
                    if (task.isSuccessful()) {
                        listener.onAuthSuccess();
                    } else {
                        listener.onAuthError(task.getException().getMessage());
                    }
                });
    }

    @Override
    public void signUp(Credentials creds, AuthListener listener) {
        auth.createUserWithEmailAndPassword(creds.getEmail(), creds.getPassword()).addOnCompleteListener
                (task -> {
                    if (task.isSuccessful()) {
                        listener.onAuthSuccess();
                    } else {
                        listener.onAuthError(task.getException().getMessage());
                    }
                });
    }

    @Override
    public boolean isUserActive() {
        return auth.getCurrentUser() != null;
    }

    @Override
    public void signOut() {
        auth.signOut();
    }

}