package com.example.massenger;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.massenger.model.Message;
import com.example.massenger.repository.MessageRepository;
import java.util.List;

public class FeedViewModel extends ViewModel {
    private MessageRepository repository;
    private LiveData<List<Message>> messages;
    private LiveData<Boolean> isLoading;
    private LiveData<Integer> newMessageCount;

    public void initRepository(android.content.Context context) {
        if (repository == null) {
            repository = new MessageRepository(context.getApplicationContext());
            messages = repository.getMessages();
            isLoading = repository.getIsLoading();
            newMessageCount = repository.getNewMessageCount();
        }
    }

    public LiveData<List<Message>> getMessages() {
        return messages;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Integer> getNewMessageCount() {
        return newMessageCount;
    }

    public void resetNewMessageCount() {
        if (repository != null) {
            repository.resetNewMessageCount();
        }
    }

    public void toggleLike(Message message) {
        if (repository != null) {
            repository.toggleLike(message);
        }
    }

    public void generateNewMessages(int count) {
        if (repository != null && count > 0) {
            repository.generateNewMessages(count);
        }
    }
}