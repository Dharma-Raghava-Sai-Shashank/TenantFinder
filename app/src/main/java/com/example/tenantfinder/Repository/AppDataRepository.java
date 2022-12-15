package com.example.tenantfinder.Repository;

import android.content.Context;
import android.net.Uri;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.example.tenantfinder.DataModel.HouseData;
import com.example.tenantfinder.DataModel.MyConnectionData;
import com.example.tenantfinder.DataModel.MyFavouriteData;
import com.example.tenantfinder.DataModel.MyHouseData;
import com.example.tenantfinder.Interface.AppDataDao;
import com.example.tenantfinder.DataModel.MyProfileData;
import com.example.tenantfinder.DataModel.ProfileData;
import com.example.tenantfinder.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

public class AppDataRepository {

    AppDataDao appDataDao;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    DatabaseReference database;
    StorageReference storage;

    public AppDataRepository(AppDataDao appDataDao)
    {
        this.appDataDao=appDataDao;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        database=firebaseDatabase.getReference();
        firebaseStorage=FirebaseStorage.getInstance();
        storage=firebaseStorage.getReference();
    }

    // ---> Accessing Databases <---

    // Firebase Database :
    public void GetMyProfileImage(Context context, ImageView imageView)
    {
        storage.child("Profile Image").child(firebaseAuth.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).placeholder(R.drawable.profile).into(imageView);
            }
        });
    }

    public void GetHouseImage(Context context, ImageView imageView,String uid)
    {
        storage.child("House Image").child(uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).placeholder(R.drawable.house1).into(imageView);
            }
        });
    }

    public void GetProfile(String uid,TextView Name,TextView Email,TextView Phone,TextView About)
    {
        database.child("Users").child(uid).child("Profile").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot snapshot) {
                Name.setText("Name: "+(snapshot.child("username").getValue()));Email.setText("Email: "+(snapshot.child("email").getValue()));
                Phone.setText("Phone: "+(snapshot.child("phone").getValue()));About.setText("About: "+(snapshot.child("about").getValue()));
            }
        });
    }

    public void GetProfileName (String uid, TextView Name)
    {
        database.child("Users").child(uid).child("Profile").child("username").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Name.setText(String.valueOf(dataSnapshot.getValue()));
            }
        });
    }

    public void GetProfileImage(Context context, ImageView imageView,String uid)
    {
        storage.child("Profile Image").child(uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).placeholder(R.drawable.profile).into(imageView);
            }
        });
    }

    public void SetUserSiginupCredentials(ProfileData profileData)
    {
        database.child("Users").child(firebaseAuth.getUid()).child("Signup Credentials").setValue(profileData);
    }

    public void SetProfileData(ProfileData profileData)
    {
        database.child("Users").child(firebaseAuth.getUid()).child("Profile").setValue(profileData);
    }

    public void SetHouseData(HouseData houseData)
    {
        String s=firebaseAuth.getUid()+appDataDao.getAllHouseData().size();
        database.child("Users").child(firebaseAuth.getUid()).child("House").child(s).setValue(houseData);
        houseData.setUid(s);
        database.child("House Data").child(s).setValue(houseData);
    }

    public void UpdateHouseData(HouseData houseData,String uid)
    {
        database.child("Users").child(firebaseAuth.getUid()).child("House").child(uid).setValue(houseData);
        houseData.setUid(uid);
        database.child("House Data").child(uid).setValue(houseData);
    }

    public void SetProfileImage(Uri uri)
    {
        storage.child("Profile Image").child(firebaseAuth.getUid()).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {}
        });
    }

    public void SetHouseImage(Uri uri)
    {
        storage.child("House Image").child(firebaseAuth.getUid()+appDataDao.getAllHouseData().size()).
                putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {}
        });
    }

    public void DeleteHouseData(String uid)
    {
        appDataDao.deleteHouseDatabyID(uid);
        storage.child("House Image").child(uid).delete();
        database.child("House Data").child(uid).removeValue();
        database.child("Users").child(firebaseAuth.getUid()).child("House").child(uid).removeValue();
    }

    // Room Database :
    public MyProfileData GetProfileData() {return appDataDao.FetchMyProfileDatabyID(firebaseAuth.getUid());}

    public List<MyFavouriteData> GetFavouriteData()
    {
        return appDataDao.getAllFavouriteData();
    }

    public List<MyHouseData> GetHouseData()
    {
        return appDataDao.getAllHouseData();
    }

    public List<MyConnectionData> GetConnectionData()
    {
        return appDataDao.getAllConnectionData();
    }

    public boolean is_hexist(String uid)
    {
        return appDataDao.is_hexist(uid);
    }

    public boolean is_cexist(String uid) {return appDataDao.is_cexist(uid);}

    public void DeleteFavouriteData(String uid)
    {
        appDataDao.deleteFavouriteDatabyID(uid);
    }

    public void DeleteConnectionData(String uid)
    {
        appDataDao.deleteHouseDatabyID(uid);
    }

    public void SetMyProfileData(MyProfileData myProfileData)
    {
        myProfileData.setUid(firebaseAuth.getUid());
        if(appDataDao.is_pexist(firebaseAuth.getUid()))
            appDataDao.updateMyPrifileData(myProfileData);
        else
            appDataDao.insertProfieData(myProfileData);
    }

    public void SetFavouriteData(MyFavouriteData myFavouriteData)
    {
        appDataDao.insertFavouriteData(myFavouriteData);
    }

    public void SetConnectionData(MyConnectionData myConnectionData)
    {
        appDataDao.insertConnectionData(myConnectionData);
    }

    public void SetMyHouseData(MyHouseData myHouseData)
    {
        String s=firebaseAuth.getUid()+appDataDao.getAllHouseData().size();
        myHouseData.setUid(s);
        appDataDao.insertHouseData(myHouseData);
    }

    public void UpdateMyHouseData(MyHouseData myHouseData)
    {
        appDataDao.updateMyHouseData(myHouseData);
    }

}
