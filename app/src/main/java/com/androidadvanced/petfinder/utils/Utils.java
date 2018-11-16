package com.androidadvanced.petfinder.utils;

import android.content.Context;
import android.widget.Toast;

public class Utils {
    public static void alert(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
