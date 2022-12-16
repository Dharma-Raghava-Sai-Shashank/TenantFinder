package com.example.tenantfinder.ViewModel;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.tenantfinder.Activity.MainActivity;
import com.example.tenantfinder.DataModel.ProfileData;
import com.example.tenantfinder.Interface.AppDataDao;
import com.example.tenantfinder.Fragment.AddFragment;
import com.example.tenantfinder.Fragment.ConnectionFragment;
import com.example.tenantfinder.Fragment.FavouriteFragment;
import com.example.tenantfinder.Fragment.HomeFragment;
import com.example.tenantfinder.Fragment.MyHouseFragment;
import com.example.tenantfinder.Fragment.ProfileFragment;
import com.example.tenantfinder.Fragment.SettingsFragment;
import com.example.tenantfinder.R;
import com.example.tenantfinder.Repository.AppDataRepository;
import com.example.tenantfinder.Database.AppDatabase;
import com.example.tenantfinder.DataModel.MyProfileData;
import com.example.tenantfinder.databinding.ActivityMainBinding;

public class MainActivityViewModel extends AndroidViewModel {

    AppDataRepository appDataRepository;
    Application Application;
    int a=0;

    public MutableLiveData<String> ProfileName=new MutableLiveData<>();
    public MutableLiveData<Fragment>Fragment=new MutableLiveData<>();
    public MutableLiveData<MyProfileData>MyProfileData=new MutableLiveData<>();
    public String Name="Username";

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        Application=application;

        // Room Database :
        AppDatabase Database= Room.databaseBuilder(application, AppDatabase.class, "USER DATA").allowMainThreadQueries().build();
        AppDataDao appDataDao=Database.appDataDao();
        appDataRepository = new AppDataRepository(appDataDao);

        // Data :
        MyProfileData ProfileData=appDataRepository.GetProfileData();
        appDataRepository.CheckConnections();

        if(ProfileData!=null)
        {
            MyProfileData.setValue(ProfileData);
            Name=ProfileData.Name;
            ProfileName.setValue(Name);
        }
        ReplaceFragment(R.id.Home);
    }

    public void SetProfileImage(Context context, ImageView imageView)
    {
        appDataRepository.GetMyProfileImage(context,imageView);
    }

    public void ReplaceFragment(int itemId)
    {
        switch (itemId) {
            case R.id.Home: {if(a!=1){
                ProfileName.setValue(Name);
                Fragment.setValue(new HomeFragment());
                a=1;}
                break;
            }
            case R.id.Chat: {if(a!=2){
                ProfileName.setValue("CONNECTIONS");
                Fragment.setValue(new ConnectionFragment());
                a=2;}
                break;
            }
            case R.id.Add: {if(a!=3){
                ProfileName.setValue("ADD HOUSE");
                Fragment.setValue(new AddFragment());
                a=3;}
                break;
            }
            case R.id.Favorite: {if(a!=4){
                ProfileName.setValue("FAVOURITES");
                Fragment.setValue(new FavouriteFragment());
                a=4;}
                break;
            }
            case R.id.Profile: {if(a!=5){
                ProfileName.setValue("PROFILE");
                Fragment.setValue(new ProfileFragment());
                a=5;}
                break;
            }
            case R.id.myhouse: {if(a!=6){
                ProfileName.setValue("MY HOUSES");
                Fragment.setValue(new MyHouseFragment());
                a=6;}
                break;
            }case R.id.settings: {if(a!=7){
                ProfileName.setValue("SETTINGS");
                Fragment.setValue(new SettingsFragment());
                a=7;}
                break;
            }case R.id.logout: {if(a!=8){
                ProfileName.setValue("SETTINGS");
                Fragment.setValue(new SettingsFragment());
                a=8;}
                break;
            }
        }
    }
}
