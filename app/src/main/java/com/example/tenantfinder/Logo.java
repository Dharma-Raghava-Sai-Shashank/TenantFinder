package com.example.tenantfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.tenantfinder.databinding.ActivityLogoBinding;

public class Logo extends AppCompatActivity {

    ActivityLogoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Full Screen:
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // View Binding :
        binding= ActivityLogoBinding.inflate(getLayoutInflater());

        // Setting Layout:
        setContentView(binding.getRoot());

        // Getting address of other Activity:
        Intent intent=new Intent(this, Registration.class);

        // Timer:
        new CountDownTimer(1500, 1500) {
            @Override
            public void onTick(long millisUntilFinished) {

                // Animation of Logo:
                binding.Logo.animate().alpha(1).setDuration(1250).scaleX(1.3f).scaleY(1.3f);
            }
            @Override
            public void onFinish() {

                // Starting new Activity:
                startActivity(intent);
            }
        }.start();
    }
}