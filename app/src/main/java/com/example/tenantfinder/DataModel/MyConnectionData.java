package com.example.tenantfinder.DataModel;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MyConnectionData {

    @PrimaryKey()
    @NonNull
    public String uid;

    public MyConnectionData(String uid) {
        this.uid = uid;
    }

    public MyConnectionData() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
