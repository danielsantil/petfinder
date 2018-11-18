package com.androidadvanced.petfinder.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidadvanced.petfinder.R;
import com.androidadvanced.petfinder.activities.fragments.NewPostDetailsFragment;
import com.androidadvanced.petfinder.activities.fragments.NewPostLocationFragment;
import com.androidadvanced.petfinder.activities.fragments.NewPostPictureFragment;
import com.androidadvanced.petfinder.auth.Authenticator;
import com.androidadvanced.petfinder.auth.FirebaseAuthHelper;
import com.androidadvanced.petfinder.database.DataCommandListener;
import com.androidadvanced.petfinder.database.FirebaseRepository;
import com.androidadvanced.petfinder.database.Repository;
import com.androidadvanced.petfinder.models.Post;
import com.androidadvanced.petfinder.storage.FileStore;
import com.androidadvanced.petfinder.storage.FirebaseStore;
import com.androidadvanced.petfinder.storage.Folders;
import com.androidadvanced.petfinder.storage.StoreListener;
import com.androidadvanced.petfinder.utils.Utils;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewPostActivity extends OptionMenuBackActivity implements NewPostPictureFragment
        .FragmentListener, NewPostDetailsFragment.FragmentListener,
        NewPostLocationFragment.FragmentListener {

    @BindView(R.id.new_post_continue_btn)
    Button continueButton;
    @BindView(R.id.new_post_back_btn)
    Button backButton;
    @BindView(R.id.new_post_header)
    TextView headerText;
    @BindView(R.id.generic_loader)
    ProgressBar loader;

    private Post newPost;
    private Authenticator authenticator;
    private Repository<Post> repository;
    private FileStore storage;

    @Override
    protected String getActivityTitle() {
        return getString(R.string.new_post);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        ButterKnife.bind(this);
        initMenu();
        init();
        authenticator = new FirebaseAuthHelper(getFirebaseAuth());
        repository = new FirebaseRepository<>(Post.class);
        storage = new FirebaseStore(Folders.PETS);
    }

    private void init() {
        newPost = new Post();
        showStep(1);
    }

    void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void onPictureInteraction(Post post) {
        newPost = post;
        setEnabledContinueButton(true);
    }

    @Override
    public void onDetailsInteraction(Post post) {
        newPost = post;
        setEnabledContinueButton(false);
    }

    @Override
    public void onDetailsUnlock() {
        setEnabledContinueButton(true);
    }

    @Override
    public void onLocationInteraction(Post post) {
        newPost = post;
        setEnabledContinueButton(true);
    }

    private void setFrame(int headingText, int backVisible, boolean isLastStep, View.OnClickListener backListener, View.OnClickListener continueListener) {
        headerText.setText(headingText);
        setEnabledContinueButton(false);
        continueButton.setOnClickListener(continueListener);
        backButton.setVisibility(backVisible);
        backButton.setOnClickListener(backListener);

        if (isLastStep) {
            continueButton.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getDrawable(R.drawable.ic_new_post), null);
            continueButton.setText(R.string.new_post_last_step);
        } else {
            continueButton.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getDrawable(R.drawable.ic_right_arrow_next), null);
            continueButton.setText(R.string.new_post_next_step);
        }
    }

    void setEnabledContinueButton(boolean enabled) {
        continueButton.setEnabled(enabled);
        int color = enabled ? R.color.colorPrimary : android.R.color.darker_gray;
        continueButton.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(),
                color));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment f : getSupportFragmentManager().getFragments()) {
            if (f instanceof NewPostPictureFragment) {
                f.onActivityResult(requestCode, resultCode, data);
            }

        }
    }

    private void showStep(int step) {
        switch (step) {
            case 1:
                showFragment(NewPostPictureFragment.newInstance(newPost));
                setFrame(R.string.picture_tab_text, View.GONE, false, b -> {},
                        c -> showStep(2));
                break;
            case 2:
                showFragment(NewPostDetailsFragment.newInstance(newPost));
                setFrame(R.string.details_tab_text, View.VISIBLE, false, b -> showStep(1),
                        c -> showStep(3));
                break;
            case 3:
                showFragment(NewPostLocationFragment.newInstance(newPost));
                setFrame(R.string.location_tab_text, View.VISIBLE, true, b -> showStep(2),
                        c -> savePost());
                break;
        }
    }

    private void savePost() {
        loaderOn(loader);
        // Setting metadata to post entity
        newPost.setUserId(authenticator.getCurrentUser().getUid());
        newPost.setPubDate(Utils.formatDate(new Date()));
        newPost.setHelping(new ArrayList<>());
        newPost.getHelping().add(newPost.getUserId());

        Context context = this;
        storage.save(Uri.parse(newPost.getPet().getPhotoUrl()), newPost.getUserId(), new StoreListener() {
            @Override
            public void onStoreSuccess(Uri uri) {
                newPost.getPet().setPhotoUrl(uri.toString());
                repository.put(newPost, new DataCommandListener() {
                    @Override
                    public void onCommandSuccess() {
                        loaderOff(loader);
                        Utils.alert(context, getString(R.string.post_created_text));
                        finish();
                    }

                    @Override
                    public void onCommandError(String errorMsg) {
                        loaderOff(loader);
                        Utils.alert(context, errorMsg);
                    }
                });
            }

            @Override
            public void onStoreError(String errorMsg) {
                loaderOff(loader);
                Utils.alert(context, errorMsg);
            }
        });
    }
}
