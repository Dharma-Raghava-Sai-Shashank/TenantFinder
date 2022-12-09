package com.example.tenantfinder.MainactivityFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tenantfinder.AppDatabase;
import com.example.tenantfinder.DataModel.MyHouseData;
import com.example.tenantfinder.AppDataDao;
import com.example.tenantfinder.MyHouseRecyclerViewAdapter;
import com.example.tenantfinder.R;

import java.util.List;

public class MyHouseFragment extends Fragment {

    TextView HousesNo;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment:
        View view = inflater.inflate(R.layout.my_houses_fragment, container, false);
        HousesNo = view.findViewById(R.id.HousesText);
        recyclerView = view.findViewById(R.id.MyHouseRecyclerView);
        AppDatabase Database= Room.databaseBuilder(getActivity().getApplicationContext(), AppDatabase.class,"USER DATA").allowMainThreadQueries().build();
        AppDataDao houseDataDao=Database.houseDataDao();
        List<MyHouseData> Data =houseDataDao.getAllHouseData();
        recyclerView.setAdapter(new MyHouseRecyclerViewAdapter(Data));
        if(Data.size()!=0)
        {
            HousesNo.setText("");
        }
        return view;
    }
}