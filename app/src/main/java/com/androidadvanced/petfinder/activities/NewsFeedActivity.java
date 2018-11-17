package com.androidadvanced.petfinder.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidadvanced.petfinder.R;
import com.androidadvanced.petfinder.adapters.RecyclerAdapterSetter;
import com.androidadvanced.petfinder.adapters.RecyclerViewAdapter;
import com.androidadvanced.petfinder.database.DataQueryListener;
import com.androidadvanced.petfinder.database.FirebaseRepository;
import com.androidadvanced.petfinder.database.Repository;
import com.androidadvanced.petfinder.models.Post;
import com.androidadvanced.petfinder.utils.Keys;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsFeedActivity extends OptionsMenuActivity
        implements RecyclerAdapterSetter<RecyclerViewAdapter<Post>, Post> {

    @BindView(R.id.news_feed_recycler)
    RecyclerView recyclerView;

    private Repository<Post> repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        initMenu();
        ButterKnife.bind(this);
        repository = new FirebaseRepository<>(Post.class);
        init();
    }

    private void init() {
        repository.getAll(new DataQueryListener<List<Post>>() {
            @Override
            public void onQuerySuccess(List<Post> result) {
                updateUI(result);
            }

            @Override
            public void onQueryError(String errorMsg) {
                Log.d(getActivityTitle(), errorMsg);
                updateUI(null);
            }
        });
    }

    private void updateUI(List<Post> posts) {
        if (posts == null || posts.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                    false));
            recyclerView.setAdapter(createAdapter(posts));
        }
    }

    @Override
    public RecyclerViewAdapter<Post> createAdapter(List<Post> posts) {
        return new RecyclerViewAdapter<>(this, posts, R.layout.post_item, (view, item) -> {
            ImageView picture = view.findViewById(R.id.post_picture);
            TextView name = view.findViewById(R.id.pet_name);
            TextView lastSeen = view.findViewById(R.id.last_seen_address);
            TextView seen = view.findViewById(R.id.seen_count);
            TextView helping = view.findViewById(R.id.helping_count);
            ImageButton details = view.findViewById(R.id.post_details);

            name.setText(item.getPet().getName());
            lastSeen.setText(item.getPet().getLastSeenAddress());
            seen.setText(String.valueOf(item.getStats().getSeen()));
            helping.setText(String.valueOf(item.getStats().getHelping()));
            Glide.with(this).load(Uri.parse(item.getPet().getPhotoUrl())).into(picture);

            details.setOnClickListener(v -> showDetails(item));
        });
    }

    private void showDetails(Post post) {
        Intent intent = new Intent(this, PostDetailsActivity.class);
        intent.putExtra(Keys.POST_DETAIL, new Gson().toJson(post));
        startActivity(intent);
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.my_news_feed);
    }

    @OnClick(R.id.new_post_background_btn)
    void startNewPost() {
        startActivity(new Intent(this, NewPostActivity.class));
    }

}