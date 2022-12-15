package com.example.tenantfinder.Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.tenantfinder.Fragment.ProfileFragment;
import com.example.tenantfinder.R;
import com.example.tenantfinder.Utility.Utills;
import com.example.tenantfinder.ViewModel.MainActivityViewModel;
import com.example.tenantfinder.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    MainActivityViewModel mainActivityViewModel;

    public static ImageView FullImage;
    public static String ChatUid=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // View Binding :
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        // Setting Layout:
        setContentView(binding.getRoot());
        // View Model :
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        // Profile :
        mainActivityViewModel.ProfileName.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String ProfileName) {
                if(ProfileName!=null) {
                    binding.PersonName.setText(ProfileName);
                }
            }
        });
        mainActivityViewModel.SetProfileImage(getApplicationContext(),binding.PersonImage);

        // Fragments :
        mainActivityViewModel.Fragment.observe(this, new Observer<Fragment>() {
            @Override
            public void onChanged(Fragment fragment) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, fragment);
                fragmentTransaction.commit();
            }
        });
        // Tool Bar (Menu) :
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.blue));
        // Navigation View:
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
                mainActivityViewModel.ReplaceFragment(item.getItemId());
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
        mainActivityViewModel.ReplaceFragment(item.getItemId());
        return super.onOptionsItemSelected(item);
    }
    // Profilepic Touch :
    public void profilepic(View view)
    {
        mainActivityViewModel.ReplaceFragment(R.id.Profile);
    }
    //Connect Touch :
    public  void connect(View view)
    {
        mainActivityViewModel.ReplaceFragment(R.id.Chat);
    }
    //Chat  :
    public  void chat(View view) {startActivity(new Intent(MainActivity.this.getApplicationContext(), ChatActivity.class));}
    //     On Back Press --> End the App :
    @Override
    public void onBackPressed() {
        if (binding.PersonName.getText()!=mainActivityViewModel.Name) {
            mainActivityViewModel.ReplaceFragment(R.id.Home);
        } else {
            super.onBackPressed();
            ActivityCompat.finishAffinity(MainActivity.this);
        }
    }
}