package com.example.massenger;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {

    private static final String TAG = "SettingsViewModel";

    private MutableLiveData<Boolean> isDarkTheme = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> notificationsEnabled = new MutableLiveData<>(true);

    public SettingsViewModel() {
        Log.d(TAG, "SettingsViewModel создан");
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "SettingsViewModel уничтожен");
    }
    public LiveData<Boolean> getIsDarkTheme() {
        return isDarkTheme;
    }
    public LiveData<Boolean> getNotificationsEnabled() {
        return notificationsEnabled;
    }
    public void setIsDarkTheme(boolean darkTheme) {
        Log.d(TAG, "Тема изменена на: " + (darkTheme ? "темная" : "светлая"));
        isDarkTheme.setValue(darkTheme);
    }
    public void setNotificationsEnabled(boolean enabled) {
        Log.d(TAG, "Уведомления: " + (enabled ? "включены" : "выключены"));
        notificationsEnabled.setValue(enabled);
    }
    public void toggleTheme() {
        Boolean current = isDarkTheme.getValue();
        if (current != null) {
            setIsDarkTheme(!current);
        }
    }
}