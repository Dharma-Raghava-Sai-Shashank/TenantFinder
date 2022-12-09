package com.example.tenantfinder.MainactivityFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenantfinder.DataModel.HouseData;
import com.example.tenantfinder.HomeRecyclerViewAdapter;
import com.example.tenantfinder.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    HomeRecyclerViewAdapter homeRecyclerViewAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= (ViewGroup)inflater.inflate(R.layout.home_fragment,container,false);

        recyclerView=view.findViewById(R.id.recyclerview);

        // Firebase Databse Data :
        FirebaseRecyclerOptions<HouseData>Data= new FirebaseRecyclerOptions.Builder<HouseData>().setQuery(FirebaseDatabase.getInstance().getReference("House Data"),HouseData.class).build();
        homeRecyclerViewAdapter=new HomeRecyclerViewAdapter(Data);
        recyclerView.setAdapter(homeRecyclerViewAdapter);
        return view;
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

