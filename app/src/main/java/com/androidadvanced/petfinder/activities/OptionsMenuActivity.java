package com.androidadvanced.petfinder.activities;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

import com.androidadvanced.petfinder.R;

public class OptionsMenuActivity extends AppCompatActivity {

    protected void onCreateMenu(int activityTitle) {
        initMenu(activityTitle);
    }

    private void initMenu(int activityTitle) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(activityTitle);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
}
