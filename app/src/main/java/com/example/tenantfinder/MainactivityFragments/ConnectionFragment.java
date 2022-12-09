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
import com.example.tenantfinder.DataModel.MyConnectionData;
import com.example.tenantfinder.AppDataDao;
import com.example.tenantfinder.MyConnectionRecyclerViewAdapter;
import com.example.tenantfinder.R;

import java.util.List;

public class ConnectionFragment extends Fragment {

    TextView ConnectionDataNo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =(ViewGroup)inflater.inflate(R.layout.connection_fragment,container,false);
        ConnectionDataNo=view.findViewById(R.id.ConnectionText);
        RecyclerView recyclerView=view.findViewById(R.id.MyConnectionRecyclerView);

        // Room Database:
        AppDatabase Database= Room.databaseBuilder(getActivity().getApplicationContext(), AppDatabase.class,"USER DATA").allowMainThreadQueries().build();
        AppDataDao houseDataDao=Database.houseDataDao();
        List<MyConnectionData> Data =houseDataDao.getAllConnectionData();

        recyclerView.setAdapter(new MyConnectionRecyclerViewAdapter(Data));

        if(Data.size()!=0)
        {
            ConnectionDataNo.setText("");
        }
        return view;
    }
}
