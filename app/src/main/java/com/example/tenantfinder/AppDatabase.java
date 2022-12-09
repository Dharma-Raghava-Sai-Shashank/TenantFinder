package com.example.tenantfinder;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.tenantfinder.DataModel.MyConnectionData;
import com.example.tenantfinder.DataModel.MyFavouriteData;
import com.example.tenantfinder.DataModel.MyHouseData;
import com.example.tenantfinder.DataModel.MyProfileData;

@Database(entities = {MyHouseData.class, MyProfileData.class, MyFavouriteData.class, MyConnectionData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AppDataDao houseDataDao();
}

