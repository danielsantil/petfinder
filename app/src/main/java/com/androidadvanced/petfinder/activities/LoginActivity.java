package com.androidadvanced.petfinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.androidadvanced.petfinder.R;
import com.androidadvanced.petfinder.auth.TaskListener;
import com.androidadvanced.petfinder.auth.Authenticator;
import com.androidadvanced.petfinder.auth.FirebaseAuthHelper;
import com.androidadvanced.petfinder.models.Credentials;
import com.androidadvanced.petfinder.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements TaskListener {

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
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        myAuth = new FirebaseAuthHelper(getFirebaseAuth());
        init();
    }

    void init() {
//        if (!isConnectedToInternet()) {
//            Utils.alert(this, "You need an active internet connection.");
//        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        uncoverPwd.setOnClickListener(v -> showPassword(password));
        if (myAuth.isUserActive()) {
            startNewsFeed();
        }
    }

    static void showPassword(EditText passwordEdit) {
        if (passwordEdit.getInputType() == InputType.TYPE_CLASS_TEXT) {
            passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        passwordEdit.setSelection(passwordEdit.getText().length());
    }

    @OnClick(R.id.register_btn)
    void startSignup() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.login_btn)
    void validateLogin() {
        try {
            Credentials creds = new Credentials(email.getText().toString(), password.getText().toString());
            myAuth.signIn(creds, this);
        } catch (Exception e) {
            Utils.alert(this, e.getMessage());
        }
    }

    void startNewsFeed() {
        Intent intent = new Intent(this, NewsFeedActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }


    @Override
    public void onTaskSuccess() {
        startNewsFeed();
    }

    @Override
    public void onTaskError(String errorMsg) {
        Utils.alert(this, errorMsg);
    }
}
