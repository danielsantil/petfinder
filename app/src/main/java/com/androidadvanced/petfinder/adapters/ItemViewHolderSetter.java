package com.androidadvanced.petfinder.adapters;

import android.view.View;

public interface ItemViewHolderSetter<T> {
    void setViewItems(View view, T item);
}