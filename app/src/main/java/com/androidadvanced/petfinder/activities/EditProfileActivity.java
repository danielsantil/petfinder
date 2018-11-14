package com.androidadvanced.petfinder.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.EditText;

import com.androidadvanced.petfinder.R;
import com.androidadvanced.petfinder.utils.Keys;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditProfileActivity extends OptionMenuBackActivity {

    @BindView(R.id.profile_aboutme)
    EditText aboutEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.my_profile_title);

        if (getIntent().getBooleanExtra(Keys.SHOW_BACK_MENU, true)) {
            initMenu();
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        aboutEditText.setOnTouchListener((v, event) -> {
            if (aboutEditText.hasFocus()) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_SCROLL:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                }
            }
            return false;
        });
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.my_profile_title);
    }
}
