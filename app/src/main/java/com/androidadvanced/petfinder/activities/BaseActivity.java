package com.androidadvanced.petfinder.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public abstract class BaseActivity extends AppCompatActivity {

    protected FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    protected abstract String getActivityName();

    protected boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (cm != null) {
            networkInfo = cm.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    protected void loaderOn(ProgressBar loader) {
        loader.setVisibility(View.VISIBLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager
                .LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    protected void loaderOff(ProgressBar loader) {
        loader.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
