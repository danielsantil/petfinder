package com.androidadvanced.petfinder.database;

public interface Repository<T> {

    void put(BaseEntity entity, DataCommandListener listener);

    void get(String uid, DataQueryListener<T> listener);
}
