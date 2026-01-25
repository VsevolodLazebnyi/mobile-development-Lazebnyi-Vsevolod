package com.example.massenger.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.massenger.data.AppDatabase;
import com.example.massenger.data.MessageDao;
import com.example.massenger.model.Message;
import com.example.massenger.model.ApiMessage;
import com.example.massenger.network.RetrofitClient;
import com.example.massenger.network.MessageApiService;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.massenger.model.Message;

public class MessageRepository {
    private final MessageDao messageDao;
    private final MessageApiService apiService;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ConnectivityManager connectivityManager;

    public MessageRepository(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        this.messageDao = db.messageDao();
        this.apiService = RetrofitClient.getApiService();
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    private boolean isNetworkAvailable() {
        if (connectivityManager != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                   capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
        }
        return false;
    }

    public LiveData<List<Message>> getMessages() {
        MutableLiveData<List<Message>> liveData = new MutableLiveData<>();
        executor.execute(() -> {
            if (isNetworkAvailable()) {
                apiService.getMessages().enqueue(new Callback<List<ApiMessage>>() {
                    @Override
                    public void onResponse(Call<List<ApiMessage>> call, Response<List<ApiMessage>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            executor.execute(() -> {
                                List<Message> messages = convertApiMessages(response.body());
                                messageDao.deleteAll();
                                messageDao.insertAll(messages.toArray(new Message[0]));
                                List<Message> savedMessages = messageDao.getAllSync();
                                liveData.postValue(savedMessages);
                            });
                        } else {
                            loadFromDatabase(liveData);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<ApiMessage>> call, Throwable t) {
                        loadFromDatabase(liveData);
                    }
                });
            } else {
                loadFromDatabase(liveData);
            }
        });
        return liveData;
    }

    private void loadFromDatabase(MutableLiveData<List<Message>> liveData) {
        executor.execute(() -> {
            List<Message> messages = messageDao.getAllSync();
            liveData.postValue(messages);
        });
    }

    public LiveData<List<Message>> refreshMessages() {
        return getMessages();
    }

    private List<Message> convertApiMessages(List<ApiMessage> apiMessages) {
        List<Message> result = new java.util.ArrayList<>();
        for (ApiMessage apiMsg : apiMessages) {
            result.add(apiMsg.toMessage());
        }
        return result;
    }
}