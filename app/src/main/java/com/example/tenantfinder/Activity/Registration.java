package com.example.tenantfinder.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;

import com.example.tenantfinder.Adapter.ViewPagerAdapter;
import com.example.tenantfinder.databinding.RegistrationBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity {

    RegistrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // View Binding :
        binding=RegistrationBinding.inflate(getLayoutInflater());

        // Setting Layout :
        setContentView(binding.getRoot());

        // If Current User :
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        // setting Fragments by Adapters :
        final ViewPagerAdapter registrationAdapter=new ViewPagerAdapter(getSupportFragmentManager(),this,binding.TabLayout.getTabCount());
        binding.ViewPager.setAdapter(registrationAdapter);

        // setting Fragments when Page changed :
        binding.ViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.TabLayout));

        // setting Fragments when Tab item clicked :
        binding.TabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.ViewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        //Animating ViewPager :
        binding.ViewPager.setTranslationY(1000);
        binding.ViewPager.setAlpha(0);
        binding.ViewPager.animate().translationYBy(-1000).alpha(1).setDuration(750).start();

        // Animating TabLayout :
        binding.TabLayout.setAlpha(0);
        binding.TabLayout.animate().alpha(1).setDuration(1000).start();

        // Animating Images :
        binding.twitter.setTranslationX(-500);
        binding.facebook.setTranslationX(500);
        binding.google.setTranslationY(500);
        binding.twitter.animate().translationXBy(500).setDuration(700).setStartDelay(500).start();
        binding.facebook.animate().translationXBy(-500).setDuration(700).setStartDelay(500).start();
        binding.google.animate().translationYBy(-500).setDuration(700).setStartDelay(350);
    }

    // On Back Press --> End the App :
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAffinity(Registration.this);
    }
}