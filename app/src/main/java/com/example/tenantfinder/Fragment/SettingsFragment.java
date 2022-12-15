package com.example.tenantfinder.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tenantfinder.Activity.Registration;
import com.example.tenantfinder.Utility.Utills;
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
                Utills.AlertDialouge(getContext(),"Do you want to Logout ?");
                Utills.b.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean Boolean) {
                        if(Boolean)
                        {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(SettingsFragment.this.getActivity().getApplicationContext(), Registration.class));
                        }
                    }
                });
            }
        });
        return binding.getRoot();
    }
}