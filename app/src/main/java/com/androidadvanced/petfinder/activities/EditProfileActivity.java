package com.androidadvanced.petfinder.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidadvanced.petfinder.R;
import com.androidadvanced.petfinder.auth.Authenticator;
import com.androidadvanced.petfinder.auth.FirebaseAuthHelper;
import com.androidadvanced.petfinder.database.DataCommandListener;
import com.androidadvanced.petfinder.database.DataQueryListener;
import com.androidadvanced.petfinder.database.FirebaseRepository;
import com.androidadvanced.petfinder.database.Repository;
import com.androidadvanced.petfinder.models.Profile;
import com.androidadvanced.petfinder.storage.FileStore;
import com.androidadvanced.petfinder.storage.FirebaseStore;
import com.androidadvanced.petfinder.storage.Folders;
import com.androidadvanced.petfinder.storage.StoreListener;
import com.androidadvanced.petfinder.utils.Keys;
import com.androidadvanced.petfinder.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.myhexaville.smartimagepicker.ImagePicker;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfileActivity extends OptionMenuBackActivity implements DataQueryListener<Profile> {

    @BindView(R.id.profile_picture)
    ImageView profilePicture;
    @BindView(R.id.signup_name)
    EditText displayName;
    @BindView(R.id.active_since)
    TextView activeSince;
    @BindView(R.id.profile_phone_number)
    EditText phoneNumber;
    @BindView(R.id.profile_email)
    TextView email;
    @BindView(R.id.profile_skip_button)
    Button skipButton;
    @BindView(R.id.generic_loader)
    ProgressBar loader;

    private ImagePicker imagePicker;
    private Authenticator auth;
    private Repository<Profile> repository;
    private FileStore storage;
    private final int keyTag = R.id.profile_picture;
    private boolean fromFeed;

    @Override
    protected String getActivityTitle() {
        return getString(R.string.my_profile_title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        this.auth = new FirebaseAuthHelper(getFirebaseAuth());
        this.repository = new FirebaseRepository<>(Profile.class);
        this.storage = new FirebaseStore(Folders.PROFILE_PICTURES);
        this.imagePicker = new ImagePicker(this, null, this::setPicture);
        init();
    }

    private void init() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.my_profile_title);

        if (getIntent().getBooleanExtra(Keys.SHOW_BACK_MENU, false)) {
            this.fromFeed = true;
            this.skipButton.setVisibility(View.GONE);
            initMenu();
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        loaderOn(this.loader);
        this.repository.get(this.auth.getCurrentUser().getUid(), this);
    }

    @Override
    public void onQuerySuccess(Profile result) {
        updateUI(result);
    }

    @Override
    public void onQueryError(String errorMsg) {}

    private void updateUI(Profile profile) {
        if (profile == null) {
            return;
        }

        this.displayName.setTag(profile.getId()); // We set this tag to get it back when saving changes
        Uri tempUri = profile.getPhotoUrl() != null ? Uri.parse(profile.getPhotoUrl()) : null;
        setPicture(tempUri);
        this.displayName.setText(StringUtils.defaultString(profile.getFullName()));
        this.activeSince.setText(profile.getActiveSince());
        this.phoneNumber.setText(StringUtils.defaultString(profile.getContact().getPhoneNumber()));
        this.email.setText(StringUtils.defaultString(profile.getContact().getEmail()));
        loaderOff(this.loader);
    }

    @OnClick(R.id.profile_save_button)
    void saveChanges() {
        loaderOn(this.loader);
        // We recreate the profile entity before saving changes
        Profile profile = new Profile();
        profile.setId(String.valueOf(this.displayName.getTag()));
        profile.setFullName(this.displayName.getText().toString());
        profile.setPhotoUrl(String.valueOf(this.profilePicture.getTag(this.keyTag)));
        profile.setActiveSince(this.activeSince.getText().toString());
        profile.getContact().setPhoneNumber(this.phoneNumber.getText().toString());
        profile.getContact().setEmail(this.email.getText().toString());

        // If there's a picture, upload it before changes
        if (profile.getPhotoUrl() != null) {
            Context context = this;
            this.storage.save(Uri.parse(profile.getPhotoUrl()), profile.getId(), new StoreListener() {
                @Override
                public void onStoreSuccess(Uri uri) {
                    profile.setPhotoUrl(uri.toString());
                    updateProfile(profile, context);
                }

                @Override
                public void onStoreError(String errorMsg) {
                    Utils.alert(context, errorMsg);
                    updateProfile(profile, context);
                }
            });
        } else {
            updateProfile(profile, this);
        }
    }

    void updateProfile(Profile profile, Context context) {
        this.repository.put(profile, new DataCommandListener() {
            @Override
            public void onCommandSuccess() {
                loaderOff(loader);
                if (fromFeed) {
                    finish();
                } else {
                    startNewsFeed();
                }
            }

            @Override
            public void onCommandError(String errorMsg) {
                loaderOff(loader);
                Utils.alert(context, errorMsg);
            }
        });
    }

    @OnClick(R.id.profile_skip_button)
    void startNewsFeed() {
        Intent intent = new Intent(this, NewsFeedActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                .FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.profile_choose_picture, R.id.profile_picture})
    void choosePicture() {
        this.imagePicker.choosePicture(true);
    }

    @OnClick(R.id.profile_delete_picture)
    void deletePicture() {
        setPicture(null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        this.imagePicker.handlePermission(requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.imagePicker.handleActivityResult(resultCode, requestCode, data);
    }

    private void setPicture(Uri capturedImageUri) {
        Glide.with(this).load(capturedImageUri).apply(new RequestOptions()
                .placeholder(R.drawable.ic_person).centerCrop()).into(this.profilePicture);
        this.profilePicture.setTag(this.keyTag, capturedImageUri);
    }
}
