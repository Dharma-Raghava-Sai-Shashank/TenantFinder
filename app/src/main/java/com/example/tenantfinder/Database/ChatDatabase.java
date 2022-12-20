package com.example.tenantfinder.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.tenantfinder.Interface.ChatDataDao;
import com.example.tenantfinder.DataModel.MyChatData;

@Database(entities = {MyChatData.class}, version = 1)
public abstract class ChatDatabase extends RoomDatabase {
    public abstract ChatDataDao chatDataDao();
}