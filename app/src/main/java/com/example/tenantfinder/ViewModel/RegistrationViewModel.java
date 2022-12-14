package com.example.tenantfinder.ViewModel;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.room.Room;

import com.example.tenantfinder.Interface.AppDataDao;
import com.example.tenantfinder.Repository.AppDataRepository;
import com.example.tenantfinder.Database.AppDatabase;
import com.example.tenantfinder.DataModel.MyProfileData;
import com.example.tenantfinder.DataModel.ProfileData;
import com.example.tenantfinder.Utility.Utills;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationViewModel extends AndroidViewModel {

    AppDataRepository appDataRepository;
    Application Application;
    boolean b;

    public RegistrationViewModel(@NonNull Application application) {
        super(application);

        Application=application;

        // Room Database :
        AppDatabase Database= Room.databaseBuilder(application, AppDatabase.class,"USER DATA").allowMainThreadQueries().build();
        AppDataDao appDataDao=Database.appDataDao();
        appDataRepository = new AppDataRepository(appDataDao);

    }

    public void SetUserSiginupCredentials(ProfileData profileData)
    {
        appDataRepository.SetUserSiginupCredentials(profileData);
    }

    public void SetMyProfileData(MyProfileData myProfileData)
    {
        appDataRepository.SetMyProfileData(myProfileData);
    }

}
