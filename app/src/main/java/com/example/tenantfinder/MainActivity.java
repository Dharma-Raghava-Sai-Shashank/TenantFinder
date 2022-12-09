package com.example.tenantfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tenantfinder.DataModel.MyProfileData;
import com.example.tenantfinder.MainactivityFragments.AddFragment;
import com.example.tenantfinder.MainactivityFragments.ConnectionFragment;
import com.example.tenantfinder.MainactivityFragments.FavouriteFragment;
import com.example.tenantfinder.MainactivityFragments.HomeFragment;
import com.example.tenantfinder.MainactivityFragments.MyHouseFragment;
import com.example.tenantfinder.MainactivityFragments.ProfileFragment;
import com.example.tenantfinder.MainactivityFragments.SettingsFragment;
import com.example.tenantfinder.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    static int a=0;
    static ImageView ProfilePic,FullImage;
    static String ChatUid=null;
    TextView PersonName;
    String Name;

    // Initialising variables:
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // View Binding :
        binding=ActivityMainBinding.inflate(getLayoutInflater());

        // Setting Layout:
        setContentView(binding.getRoot());

        replacefragment(new HomeFragment());

        // Profile :
        ProfilePic=findViewById(R.id.PersonImage);
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference HouseImages=storage.getReference().child("Profile Image").child(FirebaseAuth.getInstance().getUid());
        HouseImages.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplication()).load(uri).placeholder(R.drawable.profile).into(ProfilePic);
            }
        });

        PersonName=findViewById(R.id.PersonName);
        AppDatabase Database= Room.databaseBuilder(getApplicationContext(), AppDatabase.class,"USER DATA").allowMainThreadQueries().build();
        AppDataDao houseDataDao=Database.houseDataDao();
        List<MyProfileData> profileDataList=houseDataDao.getAllProfileData();
        if(profileDataList.size()!=0) {
            Name=profileDataList.get(profileDataList.size()-1).Name;
            PersonName.setText(Name);
        }

        // Tool Bar (Menu) :
        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.blue));

        // Navigation View:
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.Home:{
                    PersonName.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    PersonName.setText(Name);
                    replacefragment(new HomeFragment());
                    a=1;
                    break;}
                case R.id.Chat:{
                    PersonName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    PersonName.setText("CONNECTIONS");
                    replacefragment(new ConnectionFragment());
                    a=2;
                    break;}
                case R.id.Add:{
                    PersonName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    PersonName.setText("ADD HOUSE");
                    replacefragment(new AddFragment());
                    a=3;
                    break;}
                case R.id.Favorite:{
                    PersonName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    PersonName.setText("FAVOURITES");
                    replacefragment(new FavouriteFragment());
                    a=4;
                    break;}
                case R.id.Profile:{
                    PersonName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    PersonName.setText("PROFILE");
                    replacefragment(new ProfileFragment());
                    a=5;
                    break;}
            }
            return true;
        });
    }

    // Menu :
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.myhouse) {
            a=6;
            PersonName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            PersonName.setText("MY HOUSES");
            replacefragment(new MyHouseFragment());
        }
        else if(item.getItemId()==R.id.settings) {
            a=7;
            PersonName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            PersonName.setText("SETTINGS");
            replacefragment(new SettingsFragment());
        }
        else if(item.getItemId()==R.id.logout)
        {
            a=8;
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this.getApplicationContext(), Registration.class));
        }
        else
        {
            a=9;
        }
        return super.onOptionsItemSelected(item);
    }

    // Changing of Fragments by Selection :
    private void replacefragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();
    }

    // Profilepic Touch :
    public void profilepic(View view)
    {
        PersonName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        PersonName.setText("PROFILE");
        replacefragment(new ProfileFragment());
        a=5;
    }

    //Connect Touch :
    public  void connect(View view)
    {
        PersonName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        PersonName.setText("CONNECTIONS");
        replacefragment(new ConnectionFragment());
        a=2;
    }

    //Chat  :
    public  void chat(View view)
    {
        startActivity(new Intent(MainActivity.this.getApplicationContext(), ChatActivity.class));
    }



//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        float x1 = 0,y1,x2,y2;
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                x1=event.getX();
//                y1=event.getY();
//                break;
//            case MotionEvent.ACTION_UP:
//                x2=event.getX();
//                y2=event.getY();
//                if(x1<x2){
//                    Intent intent=new Intent(MainActivity.this,Chats.class);
//                    startActivity(intent);
//                }
//                break;
//            default:
//                throw new IllegalStateException("Unexpected value: " + event.getAction());
//        }
//        return false;
//    }

    //     On Back Press --> End the App :
    @Override
    public void onBackPressed() {
        if (a != 1) {
            a = 1;
            PersonName.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            PersonName.setText(Name);
            replacefragment(new HomeFragment());
        } else {
            super.onBackPressed();
            ActivityCompat.finishAffinity(MainActivity.this);
        }
    }
}