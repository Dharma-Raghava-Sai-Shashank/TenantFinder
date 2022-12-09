package com.example.tenantfinder.DataModel;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MyProfileData {
    @PrimaryKey()
    @NonNull
    public String uid;
    @ColumnInfo(name = "Name")
    public String Name;
    @ColumnInfo(name = "Email")
    public String Email;
    @ColumnInfo(name = "Phone")
    public String Phone;
    @ColumnInfo(name = "About")
    public String About;

    public MyProfileData(@NonNull String uid, String name, String email, String phone, String about) {
        this.uid = uid;
        Name = name;
        Email = email;
        Phone = phone;
        About = about;
    }

    public MyProfileData() {
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    public String getHouseName() {
        return Name;
    }

    public void setHouseName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAbout() {
        return About;
    }

    public void setAbout(String about) {
        About = about;
    }
}
