package com.example.tenantfinder.Interface;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tenantfinder.DataModel.MyConnectionData;
import com.example.tenantfinder.DataModel.MyFavouriteData;
import com.example.tenantfinder.DataModel.MyHouseData;
import com.example.tenantfinder.DataModel.MyProfileData;

import java.util.List;

@Dao
public interface AppDataDao {
    @Insert
    void insertHouseData(MyHouseData myHouseData);

    @Insert
    void insertProfieData(MyProfileData myProfileData);

    @Insert
    void insertFavouriteData(MyFavouriteData myFavouriteData);

    @Insert
    void insertConnectionData(MyConnectionData myConnectionData);

    @Update
    void updateMyHouseData(MyHouseData myHouseData);

    @Update
    void updateMyPrifileData(MyProfileData myProfileData);

    @Query("SELECT * FROM MyHouseData WHERE uid= :hid")
    MyHouseData FetchMyHouseDatabyID(String hid);

    @Query("SELECT * FROM MyProfileData WHERE uid= :pid")
    MyProfileData FetchMyProfileDatabyID(String pid);

    @Query("DELETE FROM MyFavouriteData WHERE uid= :favhouseuid")
    void deleteFavouriteDatabyID(String favhouseuid);

    @Query("DELETE FROM MyProfileData WHERE uid= :profuid")
    void deleteProfileDataDatabyID(String profuid);

    @Query("DELETE FROM MyHouseData WHERE uid= :houseuid")
    void deleteHouseDatabyID(String houseuid);

    @Query("DELETE FROM MyConnectionData WHERE uid= :pid")
    void deleteConnectionDatabyID(String pid);

    @Query("SELECT * FROM MyHouseData")
    List<MyHouseData>getAllHouseData();

    @Query("SELECT * FROM MyProfileData")
    List<MyProfileData> getAllProfileData();

    @Query("SELECT * FROM MyFavouriteData")
    List<MyFavouriteData> getAllFavouriteData();

    @Query("SELECT * FROM MyConnectionData")
    List<MyConnectionData> getAllConnectionData();

    @Query("DELETE FROM MyProfileData")
    void deleteAllProfileData();

    @Query("SELECT EXISTS(SELECT * FROM MyFavouriteData WHERE uid= :huid)")
    Boolean is_hexist(String huid);

    @Query("SELECT EXISTS(SELECT * FROM MyProfileData WHERE uid= :puid)")
    Boolean is_pexist(String puid);

    @Query("SELECT EXISTS(SELECT * FROM MyConnectionData WHERE uid= :cuid)")
    Boolean is_cexist(String cuid);
}

