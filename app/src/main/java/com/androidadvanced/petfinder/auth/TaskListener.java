package com.androidadvanced.petfinder.auth;

public interface TaskListener {
    void onTaskSuccess();

    void onTaskError(String errorMsg);
}
