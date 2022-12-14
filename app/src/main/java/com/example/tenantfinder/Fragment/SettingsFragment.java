package com.example.tenantfinder.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tenantfinder.Activity.Registration;
import com.example.tenantfinder.databinding.SettingsFragmentBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    SettingsFragmentBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // View Binding :
        binding=SettingsFragmentBinding.inflate(getLayoutInflater());

        // Logout :
        binding.settingslogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SettingsFragment.this.getActivity().getApplicationContext(), Registration.class));
            }
        });
        return binding.getRoot();
    }
}