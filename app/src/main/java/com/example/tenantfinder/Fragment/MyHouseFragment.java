package com.example.tenantfinder.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tenantfinder.Database.AppDatabase;
import com.example.tenantfinder.DataModel.MyHouseData;
import com.example.tenantfinder.Interface.AppDataDao;
import com.example.tenantfinder.Adapter.MyHouseRecyclerViewAdapter;
import com.example.tenantfinder.R;
import com.example.tenantfinder.ViewModel.FragmentViewModel;
import com.example.tenantfinder.databinding.MyHousesFragmentBinding;

import java.util.List;

public class MyHouseFragment extends Fragment {

    MyHousesFragmentBinding binding;
    FragmentViewModel fragmentViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // View Binding :
        binding=MyHousesFragmentBinding.inflate(getLayoutInflater());

        // View Model :
        fragmentViewModel= new ViewModelProvider(this).get(FragmentViewModel.class);

        // Data ;
        fragmentViewModel.GetHouseData();
        fragmentViewModel.HouseData.observe(getViewLifecycleOwner(), new Observer<List<MyHouseData>>() {
            @Override
            public void onChanged(List<MyHouseData> myHouseData) {
                binding.MyHouseRecyclerView.setAdapter(new MyHouseRecyclerViewAdapter(myHouseData));
                if(myHouseData.size()!=0)
                    binding.HousesText.setText("");
            }
        });
        return binding.getRoot();
    }
}