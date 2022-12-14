package com.example.tenantfinder.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tenantfinder.DataModel.HouseData;
import com.example.tenantfinder.Adapter.HomeRecyclerViewAdapter;
import com.example.tenantfinder.databinding.HomeFragmentBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment {

    HomeFragmentBinding binding;
    HomeRecyclerViewAdapter homeRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // View Binding :
        binding=HomeFragmentBinding.inflate(getLayoutInflater());

        // Firebase Databse Data :
        FirebaseRecyclerOptions<HouseData>Data=new FirebaseRecyclerOptions.Builder<HouseData>().setQuery(FirebaseDatabase.getInstance()
                .getReference("House Data"),HouseData.class).build();
        homeRecyclerViewAdapter=new HomeRecyclerViewAdapter(Data);
        binding.recyclerview.setAdapter(homeRecyclerViewAdapter);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        homeRecyclerViewAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        homeRecyclerViewAdapter.stopListening();
    }
}

