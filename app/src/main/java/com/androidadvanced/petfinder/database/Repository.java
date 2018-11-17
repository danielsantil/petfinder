package com.androidadvanced.petfinder.database;

import java.util.List;

public interface Repository<T> {

    void put(BaseEntity entity, DataCommandListener listener);

    void get(String uid, DataQueryListener<T> listener);

    void getAll(DataQueryListener<List<T>> listener);
}
