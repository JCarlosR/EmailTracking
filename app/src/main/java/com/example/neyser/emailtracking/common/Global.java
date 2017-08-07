package com.example.neyser.emailtracking.common;

import android.content.Context;
import android.content.SharedPreferences;

public class Global {

    public static void saveBooleanPreference(Context context, String key, boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences("global_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void saveIntPreference(Context context, String key, int value) {
        SharedPreferences sharedPref = context.getSharedPreferences("global_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void saveStringPreference(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences("global_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void saveLongPreference(Context context, String key, long value) {
        SharedPreferences sharedPref = context.getSharedPreferences("global_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static boolean getBooleanFromPreferences(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences("global_preferences", Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, false);
    }

    public static int getIntFromPreferences(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences("global_preferences", Context.MODE_PRIVATE);
        return sharedPref.getInt(key, 0);
    }

    public static String getStringFromPreferences(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences("global_preferences", Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    public static Long getLongFromPreferences(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences("global_preferences", Context.MODE_PRIVATE);
        return sharedPref.getLong(key, 0);
    }


}
