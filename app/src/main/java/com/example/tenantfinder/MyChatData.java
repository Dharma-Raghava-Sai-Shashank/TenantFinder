package com.example.tenantfinder;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MyChatData {
        @PrimaryKey()
        @NonNull
        public String uid;
        @ColumnInfo(name = "Chat")
        public String Chat;

    public MyChatData(@NonNull String uid, String chat) {
        this.uid = uid;
        Chat = chat;
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    public String getChat() {
        return Chat;
    }

    public void setChat(String chat) {
        Chat = chat;
    }
}
