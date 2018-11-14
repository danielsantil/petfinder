package com.androidadvanced.petfinder.activities;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.androidadvanced.petfinder.R;
import com.androidadvanced.petfinder.auth.Authenticator;
import com.androidadvanced.petfinder.auth.FirebaseAuthHelper;

public abstract class OptionsMenuActivity extends BaseActivity {

    protected void initMenu() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getActivityTitle());
        }
    }

    protected abstract String getActivityTitle();

    @Override
    protected String getActivityName() {
        return getActivityTitle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_signout) {
            Authenticator auth = new FirebaseAuthHelper(getFirebaseAuth());
            auth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
