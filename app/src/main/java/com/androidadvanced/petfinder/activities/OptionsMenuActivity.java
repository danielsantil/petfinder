package com.androidadvanced.petfinder.activities;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.androidadvanced.petfinder.R;
import com.androidadvanced.petfinder.auth.Authenticator;
import com.androidadvanced.petfinder.auth.FirebaseAuthHelper;
import com.androidadvanced.petfinder.utils.Keys;

public abstract class OptionsMenuActivity extends BaseActivity {

    protected void initMenu() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getActivityName());
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
        switch (item.getItemId()) {
            case R.id.menu_signout:
                Authenticator auth = new FirebaseAuthHelper(getFirebaseAuth());
                auth.signOut();
                Intent loginIntent = new Intent(this, LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginIntent);
                finish();
                break;
            case R.id.menu_profile:
                Intent profileIntent = new Intent(this, EditProfileActivity.class);
                profileIntent.putExtra(Keys.SHOW_BACK_MENU, true);
                startActivity(profileIntent);
                break;
            case R.id.menu_new_post:
                startActivity(new Intent(this, NewPostActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
