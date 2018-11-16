package com.androidadvanced.petfinder.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.androidadvanced.petfinder.R;

public class NewPostActivity extends OptionMenuBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        initMenu();
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.new_post);
    }
}
