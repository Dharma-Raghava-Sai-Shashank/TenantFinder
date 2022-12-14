package com.example.tenantfinder.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.tenantfinder.DataModel.MyConnectionData;
import com.example.tenantfinder.DataModel.MyFavouriteData;
import com.example.tenantfinder.DataModel.MyHouseData;
import com.example.tenantfinder.DataModel.MyProfileData;
import com.example.tenantfinder.Interface.AppDataDao;

@Database(entities = {MyHouseData.class, MyProfileData.class, MyFavouriteData.class, MyConnectionData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AppDataDao appDataDao();
}

