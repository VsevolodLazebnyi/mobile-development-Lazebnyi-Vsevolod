package com.example.massenger.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.TypeConverters;

import com.example.massenger.model.Message;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Message.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MessageDao messageDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Добавляем новые столбцы
            database.execSQL("ALTER TABLE messages ADD COLUMN userName TEXT");
            database.execSQL("ALTER TABLE messages ADD COLUMN avatarUrl TEXT");
            database.execSQL("ALTER TABLE messages ADD COLUMN isLiked INTEGER DEFAULT 0");
            database.execSQL("ALTER TABLE messages ADD COLUMN likeCount INTEGER DEFAULT 0");
            database.execSQL("ALTER TABLE messages ADD COLUMN messageTime TEXT");
        }
    };

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "message_database"
                            )
                            .addMigrations(MIGRATION_2_3)
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                MessageDao dao = INSTANCE.messageDao();

                // Добавляем только 3 демо-сообщения для первого запуска
                Message demoMessage1 = new Message(
                        UUID.randomUUID().toString(),
                        "Новостник1",
                        "Бла блабла бла-бла бла блабла бла бла-бла бла блабла бла бла-бла бла блабла бла блабла бла-бла бла блабла бла бла-бла бла блабла бла бла-бла бла блабла бла. И это всё в Саратове.",
                        1L
                );
                demoMessage1.userName = "Алексей";
                demoMessage1.avatarUrl = "https://i.pravatar.cc/150?img=1";
                demoMessage1.likeCount = 5;

                Message demoMessage2 = new Message(
                        UUID.randomUUID().toString(),
                        "Новостник 2",
                        "Произошло невероятное! Преподавателю так понравился дизайн, что он просто упал стоя! Лабораторная работа выполнена на отлично, все требования соблюдены.",
                        2L
                );
                demoMessage2.userName = "Мария";
                demoMessage2.avatarUrl = "https://i.pravatar.cc/150?img=2";
                demoMessage2.likeCount = 8;

                Message demoMessage3 = new Message(
                        UUID.randomUUID().toString(),
                        "Android Development",
                        "Сегодня мы изучили Room, Retrofit и корутины. Эти технологии позволяют создавать современные приложения с офлайн-доступом и красивым интерфейсом.",
                        3L
                );
                demoMessage3.userName = "Дмитрий";
                demoMessage3.avatarUrl = "https://i.pravatar.cc/150?img=3";
                demoMessage3.likeCount = 12;
                demoMessage3.isLiked = true;

                dao.insertAll(demoMessage1, demoMessage2, demoMessage3);
            });
        }
    };
}