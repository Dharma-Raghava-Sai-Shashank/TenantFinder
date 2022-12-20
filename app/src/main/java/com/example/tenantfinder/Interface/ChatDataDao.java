package com.example.tenantfinder.Interface;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tenantfinder.DataModel.MyChatData;

import java.util.List;

@Dao
public interface ChatDataDao {
    @Insert
    void insertChatData(MyChatData myChatData);

    @Query("SELECT * FROM MyChatData")
    List<MyChatData> getAllChatData();

    @Query("DELETE FROM MyChatData")
    void deleteAllChatData();

    @Query("SELECT COUNT(*) FROM MyChatData")
    Integer getCount();
}
