package com.epiccrown.me.note.noteme.Helpers;

import android.content.Context;
import android.preference.PreferenceManager;

public class PreferencesNoteme {
    private final static String RecyclerStyle = "recstyle";

    public static boolean getRecyclerStyle(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(RecyclerStyle, false);
    }

    public static void setRecyclerStyle(Context context, boolean straggle) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(RecyclerStyle, straggle)
                .apply();
    }
}
