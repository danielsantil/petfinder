package com.androidadvanced.petfinder.storage;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class FirebaseStore implements FileStore {

    private final String folderName;
    private final FirebaseStorage storage;

    public FirebaseStore(String folderName) {
        this.folderName = folderName;
        this.storage = FirebaseStorage.getInstance();
    }

    @Override
    public void save(File file, String fileName, StoreListener listener) {
        Uri fileUri = Uri.fromFile(file);
        save(fileUri, fileName, listener);
    }

    @Override
    public void save(Uri fileUri, String fileName, StoreListener listener) {
        StorageReference fileRef = this.storage.getReference(this.folderName + "/"
                + fileName);

        fileRef.putFile(fileUri)
                .continueWithTask(task -> fileRef.getDownloadUrl())
                .addOnSuccessListener(listener::onStoreSuccess)
                .addOnFailureListener(e -> listener.onStoreError(e.getMessage()));
    }
}
