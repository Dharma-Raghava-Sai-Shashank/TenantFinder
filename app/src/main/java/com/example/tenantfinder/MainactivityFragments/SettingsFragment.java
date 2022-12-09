package com.example.tenantfinder.MainactivityFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tenantfinder.R;
import com.example.tenantfinder.Registration;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    Button Logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.settings_fragment, container, false);

        // Logout :
        Logout=view.findViewById(R.id.settingslogout);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SettingsFragment.this.getActivity().getApplicationContext(), Registration.class));
            }
        });
        return view;
    }
}