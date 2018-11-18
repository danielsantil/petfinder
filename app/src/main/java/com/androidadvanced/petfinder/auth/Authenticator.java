package com.androidadvanced.petfinder.auth;

import com.androidadvanced.petfinder.models.Credentials;
import com.androidadvanced.petfinder.models.AuthProfile;

public interface Authenticator {
    void signIn(Credentials creds, AuthListener listener);

    void signUp(Credentials creds, AuthListener listener);

    boolean isUserActive();

    void signOut();

    AuthProfile getCurrentUser();
}