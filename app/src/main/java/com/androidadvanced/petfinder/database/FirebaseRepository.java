package com.androidadvanced.petfinder.database;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseRepository<T> implements Repository<T> {

    private final Class<T> refType;

    public FirebaseRepository(Class<T> refType) {
        this.refType = refType;
    }

    @Override
    public void put(BaseEntity entity, DataCommandListener listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child(getNodeName(refType));

        ref.child(entity.getId()).setValue(entity)
                .addOnSuccessListener(aVoid -> listener.onCommandSuccess())
                .addOnFailureListener(exc -> listener.onCommandError(exc.getMessage()));
    }

    @Override
    public void get(String uid, DataQueryListener<T> listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child(getNodeName(refType)).child(uid);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onQuerySuccess(dataSnapshot.getValue(refType));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onQueryError(databaseError.getMessage());
            }
        });
    }

    private String getNodeName(Class<?> type) {
        String nodeName;
        Entity ann = type.getAnnotation(Entity.class);
        if (ann == null || ann.value().isEmpty()) {
            nodeName = type.getSimpleName().toLowerCase();
        } else {
            nodeName = ann.value();
        }
        return nodeName;
    }
}
