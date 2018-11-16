package com.androidadvanced.petfinder.storage;

import android.net.Uri;

public interface StoreListener {
    void onStoreSuccess(Uri uri);

    void onStoreError(String errorMsg);
}
