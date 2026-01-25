package com.example.massenger.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class NetworkMonitor extends LiveData<Boolean> {
    private Context context;
    private ConnectivityManager connectivityManager;
    private MutableLiveData<Boolean> isConnected = new MutableLiveData<>();
    
    public NetworkMonitor(Context context) {
        this.context = context;
        connectivityManager = (ConnectivityManager) 
            context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }
    
    @Override
    protected void onActive() {
        super.onActive();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(networkReceiver, filter);
        updateConnectionStatus();
    }
    
    @Override
    protected void onInactive() {
        super.onInactive();
        context.unregisterReceiver(networkReceiver);
    }
    
    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateConnectionStatus();
        }
    };
    
    private void updateConnectionStatus() {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean connected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        isConnected.setValue(connected);
        if (!connected) {
        } else {
        }
    }
    
    public LiveData<Boolean> getIsConnected() {
        return isConnected;
    }
}