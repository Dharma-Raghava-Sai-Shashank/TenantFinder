package com.example.tenantfinder.DataModel;

import android.graphics.Bitmap;

public class HouseData {
    String Uid,HouseName,OwnerName,Price,Address,Description;

    public HouseData(String houseName, String ownerName, String price, String address, String description) {
        HouseName = houseName;
        OwnerName = ownerName;
        Price = price;
        Address = address;
        Description = description;
    }

    public HouseData(String uid, String houseName, String ownerName, String price, String address, String description) {
        Uid=uid;
        HouseName = houseName;
        OwnerName = ownerName;
        Price = price;
        Address = address;
        Description = description;
    }

    public HouseData() {
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
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
