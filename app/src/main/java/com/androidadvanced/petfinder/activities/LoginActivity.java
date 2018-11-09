package com.androidadvanced.petfinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.WindowManager;
import android.widget.EditText;

import com.androidadvanced.petfinder.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.username_edit_text)
    EditText username;
    @BindView(R.id.password_edit_text)
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
    }

    void init() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @OnClick(R.id.uncover_pwd)
    void showPassword() {
        if (password.getInputType() == InputType.TYPE_CLASS_TEXT) {
            password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            password.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        password.setSelection(password.getText().length());
    }

    @OnClick(R.id.register_btn)
    void startSignup() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.login_btn)
    void startNewsFeed() {
        Intent intent = new Intent(this, NewsFeedActivity.class);
        startActivity(intent);
        finish();
    }

}
