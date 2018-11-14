package com.androidadvanced.petfinder.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidadvanced.petfinder.R;
import com.androidadvanced.petfinder.models.Post;
import com.androidadvanced.petfinder.utils.Keys;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostDetailsActivity extends OptionMenuBackActivity {

    @BindView(R.id.pet_name)
    TextView petName;
    @BindView(R.id.pet_pub_date)
    TextView pubDate;
    @BindView(R.id.pet_picture)
    ImageView picture;
    @BindView(R.id.post_seen_count)
    TextView seenCount;
    @BindView(R.id.post_helping_count)
    TextView helpCount;
    @BindView(R.id.pet_last_seen)
    TextView lastSeenAddress;
    @BindView(R.id.pet_description)
    TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details_activity);
        initMenu();
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        String postStr = getIntent().getStringExtra(Keys.POST_DETAIL);
        Post post = new Gson().fromJson(postStr, Post.class);
        petName.setText(post.getPet().getName());
        pubDate.setText(post.getPubDate().toString());
        Glide.with(this).load(post.getPet().getPicture())
                .apply(new RequestOptions().centerCrop()).into(picture);
        seenCount.setText(String.valueOf(post.getStats().getSeen()));
        helpCount.setText(String.valueOf(post.getStats().getHelping()));
        lastSeenAddress.setText(post.getPet().getLastSeenAddress());
        description.setText(post.getDescription());
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.post_details);
    }
}
