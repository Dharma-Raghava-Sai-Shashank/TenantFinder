package com.example.tenantfinder;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.room.Room;

import com.example.tenantfinder.Database.AppDatabase;
import com.example.tenantfinder.Interface.AppDataDao;
import com.example.tenantfinder.Repository.AppDataRepository;

public class ChatViewModel extends AndroidViewModel {

    AppDataRepository appDataRepository;
    Application Application;

    public ChatViewModel(@NonNull Application application,String uid) {
        super(application);
        Application=application;

        // Room Database :
        ChatDatabase Database= Room.databaseBuilder(application, ChatDatabase.class, uid).allowMainThreadQueries().build();
        ChatDataDao chatDataDao=Database.chatDataDao();
        appDataRepository = new AppDataRepository(chatDataDao);
    }
}
