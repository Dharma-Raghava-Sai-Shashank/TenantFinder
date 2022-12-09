package com.example.tenantfinder.MainactivityFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.tenantfinder.AppDatabase;
import com.example.tenantfinder.DataModel.MyFavouriteData;
import com.example.tenantfinder.FavouriteRecyclerViewAdapter;
import com.example.tenantfinder.AppDataDao;
import com.example.tenantfinder.R;

import java.util.List;

public class FavouriteFragment extends Fragment {

    TextView favoutitesNo;
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= (ViewGroup)inflater.inflate(R.layout.favorite_fragment,container,false);

        favoutitesNo=view.findViewById(R.id.FavouritesText);
        recyclerView=view.findViewById(R.id.FavoriteRecyclerView);

        // Room Database :
        AppDatabase Database= Room.databaseBuilder(view.getContext(), AppDatabase.class,"USER DATA").allowMainThreadQueries().build();
        AppDataDao houseDataDao=Database.houseDataDao();
        List<MyFavouriteData> Data =houseDataDao.getAllFavouriteData();

        recyclerView.setAdapter(new FavouriteRecyclerViewAdapter(Data));

        if(Data.size()!=0){
            favoutitesNo.setText("");
        }
        return view;
    }
}
