package com.androidadvanced.petfinder.storage;

import android.net.Uri;

import java.io.File;

public interface FileStore {
    void save(File file, String fileName, StoreListener listener);
    void save(Uri fileUri, String fileName, StoreListener listener);
}
