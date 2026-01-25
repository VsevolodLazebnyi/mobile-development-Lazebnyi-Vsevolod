package com.example.massenger.workers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.example.massenger.R;
import com.example.massenger.data.AppDatabase;
import com.example.massenger.data.MessageDao;
import com.example.massenger.model.Message;
import java.util.Random;
import java.util.UUID;

public class SyncWorker extends Worker {

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            generateRandomMessage();
            showNotification();

            return Result.success();
        } catch (Exception e) {
            return Result.failure();
        }
    }

    private void generateRandomMessage() {
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        MessageDao messageDao = db.messageDao();

        Random random = new Random();
        String[] userNames = {"Система", "Бот", "Сервер", "Админ", "Ассистент"};
        String[] messages = {
                "Новые сообщения загружены",
                "Фоновая синхронизация завершена",
                "Получены обновления",
                "Данные обновлены в фоне",
                "Загружены новые уведомления"
        };

        for (int i = 0; i < 5; i++) {
            Message newMessage = new Message(
                    messages[random.nextInt(messages.length)],
                    "Автосообщение " +
                            new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date()), // body
                    (long) random.nextInt(100),
                    userNames[random.nextInt(userNames.length)],
                    "https://i.pravatar.cc/150?img=" + (random.nextInt(10) + 1) // avatarUrl
            );

            newMessage.likeCount = random.nextInt(10);
            newMessage.isLiked = random.nextBoolean();

            // Сохраняем в базу данных
            messageDao.insert(newMessage);
        }
    }

    private void showNotification() {
        String channelId = "sync_channel";
        String channelName = "Синхронизация";

        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext()
                        .getSystemService(Context.NOTIFICATION_SERVICE);

        // Создаем канал для Android Oreo и выше
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), channelId)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle("Новые данные получены")
                        .setContentText("Список сообщений обновлен")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
        }
    }
}