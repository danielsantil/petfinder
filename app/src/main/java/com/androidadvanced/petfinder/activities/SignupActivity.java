package com.androidadvanced.petfinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.androidadvanced.petfinder.R;
import com.androidadvanced.petfinder.auth.AuthListener;
import com.androidadvanced.petfinder.auth.Authenticator;
import com.androidadvanced.petfinder.auth.FirebaseAuthHelper;
import com.androidadvanced.petfinder.models.Credentials;
import com.androidadvanced.petfinder.utils.Keys;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends OptionMenuBackActivity implements AuthListener {

    @BindView(R.id.email_edit_text)
    EditText email;
    @BindView(R.id.password_edit_text)
    EditText password;
    @BindView(R.id.uncover_pwd)
    ImageButton uncoverPwd;

    private Authenticator myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initMenu();
        ButterKnife.bind(this);
        myAuth = new FirebaseAuthHelper(getFirebaseAuth());
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
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    void startEditProfile() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra(Keys.SHOW_BACK_MENU, false);
        startActivity(intent);
        finish();
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.registration);
    }

    @Override
    public void onAuthSuccess() {
        startEditProfile();
    }

    @Override
    public void onAuthError(String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }
}
