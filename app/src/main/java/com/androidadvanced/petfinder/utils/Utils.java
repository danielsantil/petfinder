package com.androidadvanced.petfinder.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static void alert(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static String formatDate(long timestamp) {
        Date date = new Date(timestamp);
        return formatDate(date);
    }

    public static String formatDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);
        return df.format(date);
    }
}
