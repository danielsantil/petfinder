package com.androidadvanced.petfinder.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidadvanced.petfinder.R;
import com.androidadvanced.petfinder.database.DataQueryListener;
import com.androidadvanced.petfinder.database.FirebaseRepository;
import com.androidadvanced.petfinder.database.Repository;
import com.androidadvanced.petfinder.models.Post;
import com.androidadvanced.petfinder.models.Profile;
import com.androidadvanced.petfinder.utils.Keys;
import com.androidadvanced.petfinder.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostDetailsActivity extends OptionMenuBackActivity {

    private static final String TEXT_MIME_TYPE = "text/plain";

    @BindView(R.id.pet_name)
    TextView petName;
    @BindView(R.id.pet_pub_date)
    TextView pubDate;
    @BindView(R.id.pet_picture)
    ImageView picture;
    @BindView(R.id.post_helping_count)
    TextView helpCount;
    @BindView(R.id.pet_last_seen)
    TextView lastSeenAddress;
    @BindView(R.id.pet_description)
    TextView description;
    @BindView(R.id.generic_loader)
    ProgressBar loader;

    private Repository<Profile> profileRepository;
    private Post post;
    private Profile petOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        initMenu();
        ButterKnife.bind(this);
        init();
        profileRepository = new FirebaseRepository<>(Profile.class);
    }

    private void init() {
        loaderOn(loader);
        String postId = getIntent().getStringExtra(Keys.POST_DETAIL);
        post = new Gson().fromJson(postId, Post.class);
        petName.setText(post.getPet().getName());
        pubDate.setText(post.getPubDate());
        Glide.with(this).load(Uri.parse(post.getPet().getPhotoUrl()))
                .apply(RequestOptions.centerCropTransform())
                .into(picture);
        helpCount.setText(String.valueOf(post.getHelping().size()));
        lastSeenAddress.setText(post.getPet().getLastSeenAddress());
        description.setText(post.getDescription());
        loaderOff(loader);
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.post_details);
    }

    @OnClick(R.id.contact_post)
    void getContactInfo() {
        if (petOwner != null) {
            showContactDialog(petOwner);
            return;
        }

        loaderOn(loader);
        Context context = this;
        profileRepository.get(post.getUserId(), new DataQueryListener<Profile>() {
            @Override
            public void onQuerySuccess(Profile result) {
                petOwner = result;
                showContactDialog(result);
                loaderOff(loader);
            }

            @Override
            public void onQueryError(String errorMsg) {
                loaderOff(loader);
                Utils.alert(context, errorMsg);
            }
        });
    }

    @SuppressLint("InflateParams")
    void showContactDialog(Profile profile) {
        View view = getLayoutInflater().inflate(R.layout.contact_info_dialog, null);
        TextView name = view.findViewById(R.id.contact_info_name);
        name.setText(profile.getFullName());
        TextView phone = view.findViewById(R.id.contact_info_phone);
        phone.setText(profile.getContact().getPhoneNumber());
        TextView email = view.findViewById(R.id.contact_info_email);
        email.setText(profile.getContact().getEmail());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.contact_information_label)
                .setPositiveButton(R.string.close_dialog_text, ((dialog, which) -> dialog.dismiss()))
                .setView(view);
        builder.create().show();
    }

    @OnClick(R.id.pet_picture)
    void showImage() {
        if (post.getPet().getPhotoUrl() == null) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse(post.getPet().getPhotoUrl()), NewsFeedActivity.IMAGE_MIME_TYPE);
        startActivity(i);
    }

    @SuppressLint("InflateParams")
    @OnClick(R.id.share_post)
    void showShareDialog() {
        View view = getLayoutInflater().inflate(R.layout.share_text_dialog, null);
        EditText editText = view.findViewById(R.id.share_edit_text);
        editText.setText(getGenericShareText());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.share_dialog_label_text)
                .setNegativeButton(R.string.go_back_dialog_text, ((dialog, which) -> dialog.dismiss()))
                .setPositiveButton(R.string.share_post, ((dialog, which) -> sharePost(editText.getText()
                        .toString
                        ())))
                .setView(view);
        builder.create().show();
    }

    void sharePost(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType(TEXT_MIME_TYPE);
        startActivity(Intent.createChooser(intent, null));
    }

    private String getGenericShareText() {
        return String.format(getString(R.string.generic_share_text),
                post.getPet().getName(),
                post.getDescription(),
                post.getPet().getLastSeenAddress(),
                post.getPet().getPhotoUrl());
    }
}
