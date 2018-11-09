package com.androidadvanced.petfinder.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;

import com.androidadvanced.petfinder.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends OptionMenuBackActivity {

    @BindView(R.id.signup_username)
    EditText username;
    @BindView(R.id.signup_name)
    EditText fullName;
    @BindView(R.id.signup_password)
    EditText password;
    @BindView(R.id.signup_phone)
    EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        onCreateMenu(R.string.registration);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
}
