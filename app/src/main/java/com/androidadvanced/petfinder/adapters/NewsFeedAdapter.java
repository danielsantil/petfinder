package com.androidadvanced.petfinder.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidadvanced.petfinder.R;
import com.androidadvanced.petfinder.models.Post;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.ItemViewHolder> {

    private List<Post> posts;
    private Context context;

    public NewsFeedAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public int getItemCount() {
        return this.posts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.post_item;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(viewType, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Post post = this.posts.get(position);
        holder.setValues(post, this.context);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.post_picture)
        ImageView picture;
        @BindView(R.id.pet_name)
        TextView name;
        @BindView(R.id.last_seen_address)
        TextView lastSeen;
        @BindView(R.id.seen_count)
        TextView seen;
        @BindView(R.id.helping_count)
        TextView helping;

        ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setPicture(byte[] bytes, Context context) {
            if (bytes != null) Glide.with(context).load(bytes).into(picture);
        }

        void setValues(Post post, Context context) {
            this.name.setText(post.getPet().getName());
            this.lastSeen.setText(post.getPet().getLastSeenAddress());
            setPicture(post.getPet().getPicture(), context);
            this.seen.setText(String.valueOf(post.getStats().getSeen()));
            this.helping.setText(String.valueOf(post.getStats().getHelping()));
        }
    }
}
