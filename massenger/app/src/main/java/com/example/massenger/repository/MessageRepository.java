package com.example.massenger.repository;

import android.content.Context;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.massenger.MainActivity;
import com.example.massenger.data.AppDatabase;
import com.example.massenger.data.MessageDao;
import com.example.massenger.model.Message;
import com.example.massenger.model.ApiMessage;
import com.example.massenger.network.RetrofitClient;
import com.example.massenger.network.MessageApiService;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.massenger.R;

public class MessageRepository {
    private final MessageDao messageDao;
    private final MessageApiService apiService;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Integer> newMessageCount = new MutableLiveData<>(0);
    private final Context context;
    private static final String TAG = "MessageRepository";
    private static final String CHANNEL_ID = "new_messages_channel";
    private static final int NOTIFICATION_ID = 1;

    public MessageRepository(Context context) {
        this.context = context;
        AppDatabase db = AppDatabase.getDatabase(context);
        this.messageDao = db.messageDao();
        this.apiService = RetrofitClient.getApiService();
        createNotificationChannel();
    }

    public LiveData<List<Message>> getMessages() {
        return messageDao.getAll();
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Integer> getNewMessageCount() {
        return newMessageCount;
    }

    public void resetNewMessageCount() {
        newMessageCount.postValue(0);
    }

    public void toggleLike(Message message) {
        executor.execute(() -> {
            message.isLiked = !message.isLiked;
            message.likeCount = message.isLiked ?
                    message.likeCount + 1 : Math.max(0, message.likeCount - 1);
            messageDao.updateLike(message.id, message.isLiked, message.likeCount);
        });
    }

    public void generateNewMessages(int count) {
        executor.execute(() -> {
            isLoading.postValue(true);

            try {
                String[] userNames = {"Иван"};
//                String[] userNames = {"Никита"};
                String[] avatars = {
                        "https://i.pravatar.cc/150?img=12",
//                        "https://i.pravatar.cc/150?img=8"
                };

                String[] messageTemplates = {
                        "Привет! Как дела?",
                        "Отличная погода сегодня!",
                        "Посмотрел новый фильм, очень понравилось",
                        "Пойдешь на встречу?",
                        "Есть новости по проекту",
                        "Надо обсудить важный вопрос",
                        "Когда будет следующее собрание?",
                        "Отправил документы по проекту",
                        "Спасибо за помощь!",
                        "Когда сможешь созвониться?",
                        "Проверил задание, все отлично!",
                        "Есть вопрос по лабораторной",
                        "Жду фидбек по дизайну",
                        "Нужна помощь с кодом",
                        "Отличная работа!"
                };

                Random random = new Random();
                Message[] newMessages = new Message[count];

                for (int i = 0; i < count; i++) {
                    String userName = userNames[random.nextInt(userNames.length)];
                    String avatarUrl = avatars[random.nextInt(avatars.length)];
                    String messageText = messageTemplates[random.nextInt(messageTemplates.length)];

                    Message newMessage = new Message(
                            "Новое сообщение",
                            messageText,
                            (long) random.nextInt(1000),
                            userName,
                            avatarUrl
                    );

                    newMessage.likeCount = random.nextInt(20);
                    newMessage.isLiked = random.nextBoolean();
                    newMessages[i] = newMessage;
                }

                messageDao.insertAll(newMessages);

                Integer currentCount = newMessageCount.getValue();
                if (currentCount == null) currentCount = 0;
                newMessageCount.postValue(currentCount + count);

                if (count == 5) {
                    showNotification(count);
                }

            } finally {
                isLoading.postValue(false);
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Новые сообщения";
            String description = "Уведомления о новых сообщениях в ленте";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(int count) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Новые сообщения!")
                .setContentText("Добавлено " + count + " новых сообщений")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

        Log.d(TAG, "Показано уведомление о " + count + " новых сообщениях");
    }
}