package com.example.massenger;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {
    
    private static final String TAG = "ProfileViewModel";
    
    private MutableLiveData<String> userName = new MutableLiveData<>("Всеволод Лазебный");
    private MutableLiveData<String> userStatus = new MutableLiveData<>("🟢 Онлайн");
    private MutableLiveData<String> userEmail = new MutableLiveData<>("vllazebnyi@example.com");
    
    public ProfileViewModel() {
        Log.d(TAG, "ProfileViewModel создан");
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "ProfileViewModel уничтожен");
    }
    public LiveData<String> getUserName() {
        return userName;
    }
    public LiveData<String> getUserStatus() {
        return userStatus;
    }
    public LiveData<String> getUserEmail() {
        return userEmail;
    }
    public void setUserName(String name) {
        Log.d(TAG, "Установлено имя: " + name);
        userName.setValue(name);
    }
    public void setUserStatus(String status) {
        Log.d(TAG, "Установлен статус: " + status);
        userStatus.setValue(status);
    }
    public void setUserEmail(String email) {
        Log.d(TAG, "Установлен email: " + email);
        userEmail.setValue(email);
    }
}