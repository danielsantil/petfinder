package com.androidadvanced.petfinder.activities;

import android.view.Menu;
import android.view.MenuItem;

public abstract class OptionMenuBackActivity extends BaseActivity {

    protected void initMenu() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getActivityName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected abstract String getActivityTitle();

    @Override
    protected String getActivityName() {
        return getActivityTitle();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
