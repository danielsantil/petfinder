package com.androidadvanced.petfinder.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.androidadvanced.petfinder.R;
import com.androidadvanced.petfinder.auth.Authenticator;
import com.androidadvanced.petfinder.auth.FirebaseAuthHelper;
import com.androidadvanced.petfinder.auth.TaskListener;
import com.androidadvanced.petfinder.database.DataCommandListener;
import com.androidadvanced.petfinder.database.FirebaseRepository;
import com.androidadvanced.petfinder.database.Repository;
import com.androidadvanced.petfinder.models.Credentials;
import com.androidadvanced.petfinder.models.Profile;
import com.androidadvanced.petfinder.utils.Keys;
import com.androidadvanced.petfinder.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends OptionMenuBackActivity implements TaskListener {

    @BindView(R.id.email_edit_text)
    EditText email;
    @BindView(R.id.password_edit_text)
    EditText password;
    @BindView(R.id.uncover_pwd)
    ImageButton uncoverPwd;

    private Authenticator myAuth;
    private Repository<Profile> repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initMenu();
        ButterKnife.bind(this);
        myAuth = new FirebaseAuthHelper(getFirebaseAuth());
        repository = new FirebaseRepository<>(Profile.class);
        init();
    }

    private void init() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        uncoverPwd.setOnClickListener(v -> LoginActivity.showPassword(password));
    }

    @OnClick(R.id.signup_button)
    void createUser() {
        try {
            Credentials creds = new Credentials(email.getText().toString(), password.getText().toString());
            myAuth.signUp(creds, this);
        } catch (Exception e) {
            Utils.alert(this, e.getMessage());
        }
    }

    void startEditProfile() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.registration);
    }

    @Override
    public void onTaskSuccess() {
        Profile profile = new Profile(myAuth.getCurrentUser());
        Context context = this;
        repository.put(profile, new DataCommandListener() {
            @Override
            public void onCommandSuccess() {
                startEditProfile();
            }

            @Override
            public void onCommandError(String errorMsg) {
                Utils.alert(context, errorMsg);
            }
        });
    }

    @Override
    public void onTaskError(String errorMsg) {
        Utils.alert(this, errorMsg);
    }
}
