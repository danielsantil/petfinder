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
        auth = new FirebaseAuthHelper(getFirebaseAuth());
        repository = new FirebaseRepository<>(Profile.class);
        storage = new FirebaseStore(Folders.PROFILE_PICTURES);
        imagePicker = new ImagePicker(this, null, this::setPicture);
        init();
    }

    private void init() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.my_profile_title);

        if (getIntent().getBooleanExtra(Keys.SHOW_BACK_MENU, false)) {
            fromFeed = true;
            skipButton.setVisibility(View.GONE);
            initMenu();
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        repository.get(auth.getCurrentUser().getUid(), this);
    }

    @Override
    public void onQuerySuccess(Profile result) {
        updateUI(result);
    }

    @Override
    public void onQueryError(String errorMsg) {
    }

    private void updateUI(Profile profile) {
        if (profile == null) {
            return;
        }

        displayName.setTag(profile.getId()); // We set this tag to get it back when saving changes
        Uri tempUri = profile.getPhotoUrl() != null ? Uri.parse(profile.getPhotoUrl()) : null;
        setPicture(tempUri);
        displayName.setText(StringUtils.defaultString(profile.getFullName()));
        activeSince.setText(profile.getActiveSince());
        phoneNumber.setText(StringUtils.defaultString(profile.getContact().getPhoneNumber()));
        email.setText(StringUtils.defaultString(profile.getContact().getEmail()));
    }

    @OnClick(R.id.profile_save_button)
    void saveChanges() {
        // We recreate the profile entity before saving changes
        Profile profile = new Profile();
        profile.setId(String.valueOf(displayName.getTag()));
        profile.setFullName(displayName.getText().toString());
        profile.setPhotoUrl(String.valueOf(profilePicture.getTag(keyTag)));
        profile.setActiveSince(activeSince.getText().toString());
        profile.getContact().setPhoneNumber(phoneNumber.getText().toString());
        profile.getContact().setEmail(email.getText().toString());

        // If there's a picture, upload it before changes
        if (profile.getPhotoUrl() != null) {
            Context context = this;
            storage.save(Uri.parse(profile.getPhotoUrl()), profile.getId(), new StoreListener() {
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
        repository.put(profile, new DataCommandListener() {
            @Override
            public void onCommandSuccess() {
                if (fromFeed) {
                    finish();
                } else {
                    startNewsFeed();
                }
            }

            @Override
            public void onCommandError(String errorMsg) {
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

    @OnClick(R.id.profile_choose_picture)
    void choosePicture() {
        imagePicker.choosePicture(true);
    }

    @OnClick(R.id.profile_delete_picture)
    void deletePicture() {
        setPicture(null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.handlePermission(requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.handleActivityResult(resultCode, requestCode, data);
    }

    private void setPicture(Uri capturedImageUri) {
        Glide.with(this).load(capturedImageUri).apply(new RequestOptions()
                .placeholder(R.drawable.ic_person).centerCrop()).into(profilePicture);
        profilePicture.setTag(keyTag, capturedImageUri);
    }
}
