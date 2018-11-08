package com.androidadvanced.petfinder.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder> {

    private List<T> items;
    private Context context;
    private ItemViewHolderSetter<T> holderSetter;
    private int layoutType;

    public RecyclerViewAdapter(Context context, List<T> items, int layoutType, ItemViewHolderSetter<T>
            holderSetter) {
        this.context = context;
        this.items = items;
        this.layoutType = layoutType;
        this.holderSetter = holderSetter;
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return this.layoutType;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(viewType, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int pos) {
        holderSetter.setViewItems(holder.view, this.items.get(pos));
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        public View view;

        ItemViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
        }
    }
}
