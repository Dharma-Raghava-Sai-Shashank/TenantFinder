package com.example.tenantfinder.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.tenantfinder.Activity.MainActivity;
import com.example.tenantfinder.DataModel.MyChatData;
import com.example.tenantfinder.Database.ChatDatabase;
import com.example.tenantfinder.Interface.ChatDataDao;
import com.example.tenantfinder.Repository.AppDataRepository;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {

    AppDataRepository appDataRepository;
    Application Application;
    public MutableLiveData<List<MyChatData>> Chat=new MutableLiveData<>();
    String uid;


    public ChatViewModel(@NonNull Application application) {
        super(application);
        Application=application;
        uid=MainActivity.ChatUid;

        // Room Database :
        ChatDatabase Database= Room.databaseBuilder(application, ChatDatabase.class, uid).allowMainThreadQueries().build();
        ChatDataDao chatDataDao=Database.chatDataDao();
        appDataRepository = new AppDataRepository(chatDataDao);
    }

    public void SetChatData(MyChatData myChatData)
    {
        appDataRepository.SetChatData(myChatData);
        GetAllChatData();
    }

    public void DeleteAllChatData()
    {
        appDataRepository.DeleteAllChatData();
        GetAllChatData();
    }

    public void GetAllChatData()
    {
        Chat.setValue(appDataRepository.GetAllChatData());
    }

}
