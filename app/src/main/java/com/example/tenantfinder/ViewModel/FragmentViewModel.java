package com.example.tenantfinder.ViewModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.tenantfinder.DataModel.HouseData;
import com.example.tenantfinder.DataModel.MyConnectionData;
import com.example.tenantfinder.DataModel.MyFavouriteData;
import com.example.tenantfinder.DataModel.MyHouseData;
import com.example.tenantfinder.DataModel.MyProfileData;
import com.example.tenantfinder.DataModel.ProfileData;
import com.example.tenantfinder.Database.AppDatabase;
import com.example.tenantfinder.Interface.AppDataDao;
import com.example.tenantfinder.R;
import com.example.tenantfinder.Repository.AppDataRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.List;

public class FragmentViewModel extends AndroidViewModel {

    AppDataRepository appDataRepository;
    Application Application;

    public MutableLiveData<MyProfileData> MyProfileData=new MutableLiveData<>();
    public MutableLiveData<List<MyFavouriteData>>FavouriteData=new MutableLiveData<>();
    public MutableLiveData<List<MyHouseData>>HouseData=new MutableLiveData<>();
    public MutableLiveData<List<MyConnectionData>>ConnectionData=new MutableLiveData<>();
    public MutableLiveData<Boolean>bool=new MutableLiveData<>();

    public FragmentViewModel(@NonNull Application application) {
        super(application);

        Application=application;

        // Room Database :
        AppDatabase Database= Room.databaseBuilder(application, AppDatabase.class, "USER DATA").allowMainThreadQueries().build();
        AppDataDao appDataDao=Database.appDataDao();
        appDataRepository = new AppDataRepository(appDataDao);

    }

    public void GetMyProfileImage(Context context, ImageView imageView)
    {
        appDataRepository.GetMyProfileImage(context,imageView);
    }

    public void GetHouseImage(Context context, ImageView imageView,String uid)
    {
        appDataRepository.GetHouseImage(context,imageView,uid);
    }

    public void GetProfileImage(Context context, ImageView imageView,String uid)
    {
        appDataRepository.GetProfileImage(context,imageView,uid);
    }


    public void GetMyProfileData()
    {
        MyProfileData.setValue(appDataRepository.GetProfileData());
    }

    public void GetProfileName(String uid, TextView Name)
    {
        appDataRepository.GetProfileName(uid,Name);
    }

    public void GetFavpuriteData()
    {
        FavouriteData.setValue(appDataRepository.GetFavouriteData());
    }

    public void GetHouseData()
    {
        HouseData.setValue(appDataRepository.GetHouseData());
    }

    public void GetConnectionData()
    {
        ConnectionData.setValue(appDataRepository.GetConnectionData());
    }

    public void SetProfileData(ProfileData profileData)
    {
        appDataRepository.SetProfileData(profileData);
    }

    public void SetFavouriteData(MyFavouriteData myFavouriteData)
    {
        appDataRepository.SetFavouriteData(myFavouriteData);
    }

    public void SetConnectionData(MyConnectionData myConnectionData)
    {
        appDataRepository.SetConnectionData(myConnectionData);
    }

    public void SetMyProfileData(MyProfileData myProfileData)
    {
        appDataRepository.SetMyProfileData(myProfileData);
    }

    public void SetHouseData(HouseData houseData)
    {
        appDataRepository.SetHouseData(houseData);
    }

    public void SetMyHouseData(MyHouseData myHouseData)
    {
       appDataRepository.SetMyHouseData(myHouseData);
    }

    public void UpdateMyHouseData(MyHouseData myHouseData)
    {
        appDataRepository.UpdateMyHouseData(myHouseData);
    }

    public void UpdateHouseData(HouseData HouseData,String uid)
    {
        appDataRepository.UpdateHouseData(HouseData,uid);
    }

    public void SetProfileImage(Uri uri)
    {
        appDataRepository.SetProfileImage(uri);
    }

     public void SetHouseImage(Uri uri)
    {
        appDataRepository.SetHouseImage(uri);
    }

    public boolean is_hexist(String uid)
    {
        return appDataRepository.is_hexist(uid);
    }

    public boolean is_cexist(String uid)
    {
        return appDataRepository.is_cexist(uid);
    }

    public void DeleteFavouriteData(String uid)
    {
        appDataRepository.DeleteFavouriteData(uid);
    }

    public void DeleteHouseData(String uid)
    {
        appDataRepository.DeleteHouseData(uid);
    }

    public void DeleteConnectionData(String uid)
    {
        appDataRepository.DeleteConnectionData(uid);
    }

    public void GetProfile(String uid,TextView Name,TextView Email,TextView Phone,TextView About)
    {
        appDataRepository.GetProfile(uid,Name,Email,Phone,About);
    }

    public void ProfileFullImage(Context context,String uid)
    {
        // Dialog Box :
        AlertDialog.Builder fullimage=new AlertDialog.Builder(context);
        View view= LayoutInflater.from(context).inflate(R.layout.fullimage_dialog,null);
        ImageView FullHouseImage=view.findViewById(R.id.FullImage);
        fullimage.setView(view);

        appDataRepository.GetProfileImage(view.getContext(),FullHouseImage,uid);

        FullHouseImage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        FullHouseImage.setScaleType(ImageView.ScaleType.FIT_XY);

        final AlertDialog alertDialog=fullimage.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    public void HouseFullImage(Context context,String uid)
    {
        // Dialog Box :
        AlertDialog.Builder fullimage=new AlertDialog.Builder(context);
        View view= LayoutInflater.from(context).inflate(R.layout.fullimage_dialog,null);
        ImageView FullHouseImage=view.findViewById(R.id.FullImage);
        fullimage.setView(view);

        appDataRepository.GetHouseImage(view.getContext(),FullHouseImage,uid);

        FullHouseImage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        FullHouseImage.setScaleType(ImageView.ScaleType.FIT_XY);

        final AlertDialog alertDialog=fullimage.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

}
