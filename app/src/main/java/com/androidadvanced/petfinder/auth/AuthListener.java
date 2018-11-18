package com.androidadvanced.petfinder.auth;

public interface AuthListener {
    void onAuthSuccess();

    void onAuthError(String errorMsg);
}
