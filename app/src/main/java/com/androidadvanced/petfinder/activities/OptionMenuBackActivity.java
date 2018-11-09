package com.androidadvanced.petfinder.activities;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class OptionMenuBackActivity extends AppCompatActivity {

    protected void onCreateMenu(int activityTitle) {
        initMenu(activityTitle);
    }

    private void initMenu(int activityTitle) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(activityTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
