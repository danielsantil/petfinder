package com.androidadvanced.petfinder.adapters;

import java.util.List;

public interface RecyclerAdapterSetter<T, A> {
    T createAdapter(List<A> items);
}
