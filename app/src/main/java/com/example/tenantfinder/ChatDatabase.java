package com.example.tenantfinder;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.tenantfinder.Interface.AppDataDao;

@Database(entities = {MyChatData.class}, version = 1)
public abstract class ChatDatabase extends RoomDatabase {
    public abstract ChatDataDao chatDataDao();
}