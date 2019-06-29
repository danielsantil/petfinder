package com.androidadvanced.petfinder.database;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseRepository<T> implements Repository<T> {

    private final Class<T> refType;
    private final FirebaseDatabase database;

    public FirebaseRepository(Class<T> refType) {
        this.refType = refType;
        this.database = FirebaseDatabase.getInstance();
    }

    @Override
    public void put(BaseEntity entity, DataCommandListener listener) {
        DatabaseReference ref = this.database.getReference().child(getNodeName(this.refType));

        ref.child(getKey(entity, ref)).setValue(entity)
                .addOnSuccessListener(aVoid -> listener.onCommandSuccess())
                .addOnFailureListener(exc -> listener.onCommandError(exc.getMessage()));
    }

    @Override
    public void get(String uid, DataQueryListener<T> listener) {
        DatabaseReference ref = this.database.getReference().child(getNodeName(this.refType))
                .child(uid);

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

    @Override
    public void getAll(DataQueryListener<List<T>> listener) {
        DatabaseReference ref = this.database.getReference().child(getNodeName(this.refType));

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<T> results = new ArrayList<>();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    results.add(child.getValue(refType));
                }
                listener.onQuerySuccess(results);
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

    private String getKey(BaseEntity entity, DatabaseReference ref) {
        String key = entity.getId() == null ? ref.push().getKey() : entity.getId();
        entity.setId(key);
        return key;
    }
}
