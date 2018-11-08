package com.androidadvanced.petfinder.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvanced.petfinder.R;
import com.androidadvanced.petfinder.adapters.RecyclerViewAdapter;
import com.androidadvanced.petfinder.adapters.RecyclerAdapterSetter;
import com.androidadvanced.petfinder.models.Post;
import com.androidadvanced.petfinder.utils.Dummy;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFeedActivity extends AppCompatActivity
        implements RecyclerAdapterSetter<RecyclerViewAdapter<Post>, Post> {

    @BindView(R.id.news_feed_recycler)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.my_news_feed);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
        recyclerView.setAdapter(createAdapter(Dummy.getPosts()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
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

            if (item.getPet().getPicture() != null)
                Glide.with(this).load(item.getPet().getPicture()).into(picture);


            details.setOnClickListener(v -> Toast.makeText(this, "You clicked on "
                    + item.getPet().getName(), Toast.LENGTH_SHORT).show());
        });
    }
}