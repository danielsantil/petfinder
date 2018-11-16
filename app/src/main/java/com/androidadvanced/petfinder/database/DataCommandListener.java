package com.androidadvanced.petfinder.database;

public interface DataCommandListener {
    void onCommandSuccess();

    void onCommandError(String errorMsg);
}
