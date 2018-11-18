package com.androidadvanced.petfinder.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidadvanced.petfinder.R;
import com.androidadvanced.petfinder.adapters.RecyclerAdapterSetter;
import com.androidadvanced.petfinder.adapters.RecyclerViewAdapter;
import com.androidadvanced.petfinder.auth.Authenticator;
import com.androidadvanced.petfinder.auth.FirebaseAuthHelper;
import com.androidadvanced.petfinder.database.DataQueryListener;
import com.androidadvanced.petfinder.database.EmptyDataCommandListener;
import com.androidadvanced.petfinder.database.FirebaseRepository;
import com.androidadvanced.petfinder.database.Repository;
import com.androidadvanced.petfinder.models.Post;
import com.androidadvanced.petfinder.utils.Keys;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsFeedActivity extends OptionsMenuActivity
        implements RecyclerAdapterSetter<RecyclerViewAdapter<Post>, Post> {

    public static final String IMAGE_MIME_TYPE = "image/jpeg";

    @BindView(R.id.news_feed_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.generic_loader)
    ProgressBar loader;

    private Repository<Post> repository;
    private Authenticator authenticator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        initMenu();
        ButterKnife.bind(this);
        repository = new FirebaseRepository<>(Post.class);
        authenticator = new FirebaseAuthHelper(getFirebaseAuth());
        init();
    }

    private void init() {
        loaderOn(loader);
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
        loaderOff(loader);
    }

    @Override
    public RecyclerViewAdapter<Post> createAdapter(List<Post> posts) {
        return new RecyclerViewAdapter<>(this, posts, R.layout.post_item, (view, item) -> {
            ImageView picture = view.findViewById(R.id.post_picture);
            TextView name = view.findViewById(R.id.pet_name);
            TextView lastSeen = view.findViewById(R.id.last_seen_address);
            TextView helping = view.findViewById(R.id.helping_count);
            LinearLayout infoContainer = view.findViewById(R.id.post_info_container);
            Button helpButton = view.findViewById(R.id.help_button);

            name.setText(item.getPet().getName());
            lastSeen.setText(item.getPet().getLastSeenAddress());
            helping.setText(String.valueOf(item.getHelping().size()));
            Glide.with(this).load(Uri.parse(item.getPet().getPhotoUrl()))
                    .apply(RequestOptions.centerCropTransform())
                    .into(picture);
            infoContainer.setOnClickListener(v -> showDetails(item));
            helpButton.setOnClickListener(v -> showNewHelperDialog(item));

            // If current user is the owner of the post, don't show help button
            if (item.getUserId().equals(authenticator.getCurrentUser().getUid())) {
                helpButton.setVisibility(View.GONE);
            } else if (isUserHelping(item)) {
                helpButton.setText(R.string.already_helping_text);
                helpButton.setOnClickListener(v -> showOldHelperDialog(item));

            }
        });
    }

    private void showNewHelperDialog(Post post) {
        AlertDialog myDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.want_to_help_dialog_msg)
                .setPositiveButton(R.string.sure_dialog_text, (dialog, which) ->
                        saveToHelpingList(post))
                .setNegativeButton(R.string.go_back_dialog_text, (dialog, which) -> dialog.dismiss())
                .create();
        myDialog.show();
        myDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources()
                .getColor(R.color.colorPrimaryDark));
    }

    private void showOldHelperDialog(Post post) {
        AlertDialog myDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.dont_want_to_help_dialog_msg)
                .setPositiveButton(R.string.sign_off_dialog_text, (dialog, which) ->
                        unsaveFromHelpingList(post))
                .setNegativeButton(R.string.go_back_dialog_text, (dialog, which) -> dialog.dismiss())
                .create();
        myDialog.show();
        myDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources()
                .getColor(R.color.red));
    }

    private void unsaveFromHelpingList(Post post) {
        post.getHelping().remove(authenticator.getCurrentUser().getUid());
        repository.put(post, new EmptyDataCommandListener());
    }

    private void saveToHelpingList(Post post) {
        post.getHelping().add(authenticator.getCurrentUser().getUid());
        repository.put(post, new EmptyDataCommandListener());
    }

    private boolean isUserHelping(Post post) {
        for (String user : post.getHelping()) {
            if (user.equals(authenticator.getCurrentUser().getUid())) {
                return true;
            }
        }
        return false;
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