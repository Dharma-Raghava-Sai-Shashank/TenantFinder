package com.example.tenantfinder.DataModel;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MyHouseData {
    @PrimaryKey()
    @NonNull public String uid;
    @ColumnInfo(name = "HouseName")
    public String HouseName;
    @ColumnInfo(name = "OwnerName")
    public String OwnerName;
    @ColumnInfo(name = "Price")
    public String Price;
    @ColumnInfo(name = "Address")
    public String Address;
    @ColumnInfo(name = "Description")
    public String Description;

    public MyHouseData(String uid, String houseName, String ownerName, String price, String address, String description) {
        this.uid = uid;
        HouseName = houseName;
        OwnerName = ownerName;
        Price = price;
        Address = address;
        Description = description;
    }

    public MyHouseData() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHouseName() {
        return HouseName;
    }

    public void setHouseName(String houseName) {
        HouseName = houseName;
    }

    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String ownerName) {
        OwnerName = ownerName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}

