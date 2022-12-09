package com.example.tenantfinder.DataModel;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

public class ProfileData {

String Username,Email,Password,Phone,About;

    public ProfileData(String username, String email, String phone, String about) {
        Username = username;
        Email = email;
        Phone = phone;
        About = about;
    }

    public ProfileData(String username, String email, String password) {
        Username = username;
        Email = email;
        Password = password;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) { Email = email; }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
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
