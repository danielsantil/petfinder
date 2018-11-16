package com.androidadvanced.petfinder.auth;

import com.androidadvanced.petfinder.models.Credentials;
import com.androidadvanced.petfinder.models.AuthProfile;

public interface Authenticator {
    void signIn(Credentials creds, TaskListener listener);

    void signUp(Credentials creds, TaskListener listener);

    boolean isUserActive();

    void signOut();

    AuthProfile getCurrentUser();
}