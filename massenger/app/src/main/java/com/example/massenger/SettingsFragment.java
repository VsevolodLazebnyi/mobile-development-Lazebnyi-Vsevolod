package com.example.massenger;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";
    private SettingsViewModel settingsViewModel;
    private Switch themeSwitch;
    private Switch notificationsSwitch;
    private TextView themeStatusText;

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: SettingsFragment создается");
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        loadSavedTheme();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: SettingsFragment создает view");
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        themeSwitch = view.findViewById(R.id.themeSwitch);
        notificationsSwitch = view.findViewById(R.id.notificationsSwitch);
        themeStatusText = view.findViewById(R.id.themeStatusText);

        setupThemeSwitch();
        setupNotificationsSwitch();

        return view;
    }

    private void loadSavedTheme() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE);
        boolean savedTheme = prefs.getBoolean("dark_theme", false);
        settingsViewModel.setIsDarkTheme(savedTheme);
    }

    private void setupThemeSwitch() {
        settingsViewModel.getIsDarkTheme().observe(getViewLifecycleOwner(), isDarkTheme -> {
            if (themeSwitch != null) {
                themeSwitch.setOnCheckedChangeListener(null);
                themeSwitch.setChecked(isDarkTheme);
                updateThemeStatusText(isDarkTheme);
                themeSwitch.setOnCheckedChangeListener(themeChangeListener);
            }
        });
        settingsViewModel.getNotificationsEnabled().observe(getViewLifecycleOwner(), enabled -> {
            if (notificationsSwitch != null) {
                notificationsSwitch.setChecked(enabled);
            }
        });
        themeSwitch.setOnCheckedChangeListener(themeChangeListener);
        notificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.i(TAG, "Уведомления: " + (isChecked ? "включены" : "выключены"));
            settingsViewModel.setNotificationsEnabled(isChecked);
            saveNotificationSetting(isChecked);
        });
    }

    private final CompoundButton.OnCheckedChangeListener themeChangeListener =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String newTheme = isChecked ? "темная" : "светлая";
                    Log.i(TAG, "Пользователь изменил тему на: " + newTheme);
                    settingsViewModel.setIsDarkTheme(isChecked);
                    saveThemeSetting(isChecked);
                    applyThemeImmediately(isChecked);
                }
            };

    private void setupNotificationsSwitch() {
    }

    private void updateThemeStatusText(boolean isDarkTheme) {
        if (themeStatusText != null) {
            String themeText = isDarkTheme ? "Темная тема активна" : "Светлая тема активна";
            themeStatusText.setText(themeText);
        }
    }

    private void saveThemeSetting(boolean isDarkTheme) {
        SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("dark_theme", isDarkTheme);
        editor.apply();
        Log.d(TAG, "Настройка темы сохранена: " + (isDarkTheme ? "темная" : "светлая"));
    }

    private void saveNotificationSetting(boolean enabled) {
        SharedPreferences prefs = requireActivity().getPreferences(android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("notifications_enabled", enabled);
        editor.apply();
        Log.d(TAG, "Настройка уведомлений сохранена: " + (enabled ? "включены" : "выключены"));
    }

    private void applyThemeImmediately(boolean isDarkTheme) {
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        Log.d(TAG, "Тема установлена: " + (isDarkTheme ? "темная" : "светлая"));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: SettingsFragment view создан");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: SettingsFragment становится видимым");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: SettingsFragment возобновляет работу");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: SettingsFragment приостанавливается");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: SettingsFragment останавливается");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: SettingsFragment view уничтожается");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: SettingsFragment уничтожается");
    }
}