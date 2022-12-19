package com.example.tenantfinder;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChatDataDao {
    @Insert
    void insertChatData(MyChatData myChatData);

    @Query("SELECT * FROM MyChatData")
    LiveData<List<MyChatData>> getAllChatData();
}
