package com.example.massenger;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {
    
    private static final String TAG = "SettingsFragment";
    private Switch themeSwitch;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: SettingsFragment создает view");
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        
        themeSwitch = view.findViewById(R.id.themeSwitch);
        setupThemeSwitch();
        
        return view;
    }

    private void setupThemeSwitch() {
        int nightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        boolean isDarkTheme = nightMode == Configuration.UI_MODE_NIGHT_YES;
        themeSwitch.setChecked(isDarkTheme);
        
        Log.d(TAG, "setupThemeSwitch: Текущая тема - " + (isDarkTheme ? "темная" : "светлая"));
        
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String newTheme = isChecked ? "темная" : "светлая";
                Log.i(TAG, "Пользователь изменил тему на: " + newTheme);
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    Log.d(TAG, "Установлена темная тема");
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    Log.d(TAG, "Установлена светлая тема");
                }
                requireActivity().recreate();
            }
        });
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