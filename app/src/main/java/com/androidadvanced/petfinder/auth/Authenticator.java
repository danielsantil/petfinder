package com.androidadvanced.petfinder.auth;

import com.androidadvanced.petfinder.models.Credentials;

public interface Authenticator {
    void signIn(Credentials creds, AuthListener listener);

    void signUp(Credentials creds, AuthListener listener);

    boolean isUserActive();

    void signOut();
}