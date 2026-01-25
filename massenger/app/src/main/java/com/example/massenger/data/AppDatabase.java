package com.example.massenger.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.massenger.model.Message;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Message.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MessageDao messageDao();
    
    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "message_database"
                    )
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
                
                Message demoMessage1 = new Message(
                    "101", 
                    "Новостник1",
                    "Бла блабла бла-бла бла блабла бла бла-бла бла блабла бла бла-бла бла блабла бла блабла бла-бла бла блабла бла бла-бла бла блабла бла бла-бла бла блабла бла блабла бла-бла бла блабла бла бла-бла бла блабла бла бла-бла бла блабла бла. И это всё в Саратове.",
                    1L
                );
                
                Message demoMessage2 = new Message(
                    "102",
                    "Новостник 2", 
                    "Произошло невероятное! Преподавателю так понравился дизайн, что он просто упал стоя! Лабораторная работа выполнена на отлично, все требования соблюдены. Мобильное приложение работает как швейцарские часы.",
                    2L
                );
                
                Message demoMessage3 = new Message(
                    "103",
                    "Android Development",
                    "Сегодня мы изучили Room, Retrofit и корутины. Эти технологии позволяют создавать современные приложения с офлайн-доступом и красивым интерфейсом. Неоморфный дизайн - это тренд 2024 года!",
                    3L
                );
                
                dao.insertAll(demoMessage1, demoMessage2, demoMessage3);
            });
        }
    };
}