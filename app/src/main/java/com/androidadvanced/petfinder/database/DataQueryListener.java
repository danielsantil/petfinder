package com.androidadvanced.petfinder.database;

public interface DataQueryListener<T> {
    void onQuerySuccess(T result);

    void onQueryError(String errorMsg);
}
