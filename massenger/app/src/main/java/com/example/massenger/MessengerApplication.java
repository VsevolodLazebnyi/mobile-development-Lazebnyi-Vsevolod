package com.example.massenger;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

public class MessengerApplication extends Application {

    private static final String TAG = "MessengerApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Приложение запущено");
        loadSavedTheme();
    }

    private void loadSavedTheme() {
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean isDarkTheme = prefs.getBoolean("dark_theme", false);

        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Log.d(TAG, "Загружена темная тема");
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Log.d(TAG, "Загружена светлая тема");
        }
    }
}