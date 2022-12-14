package com.example.tenantfinder.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.tenantfinder.Database.AppDatabase;
import com.example.tenantfinder.DataModel.MyConnectionData;
import com.example.tenantfinder.Interface.AppDataDao;
import com.example.tenantfinder.Adapter.MyConnectionRecyclerViewAdapter;
import com.example.tenantfinder.R;
import com.example.tenantfinder.ViewModel.FragmentViewModel;
import com.example.tenantfinder.databinding.ConnectionFragmentBinding;

import java.util.List;

public class ConnectionFragment extends Fragment {

    ConnectionFragmentBinding binding;
    FragmentViewModel fragmentViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // View Binding :
        binding=ConnectionFragmentBinding.inflate(getLayoutInflater());

        // View Model :
        fragmentViewModel=new ViewModelProvider(this).get(FragmentViewModel.class);

        // Data :
        fragmentViewModel.GetConnectionData();
        fragmentViewModel.ConnectionData.observe(getViewLifecycleOwner(), new Observer<List<MyConnectionData>>() {
            @Override
            public void onChanged(List<MyConnectionData> myConnectionData) {
                binding.MyConnectionRecyclerView.setAdapter(new MyConnectionRecyclerViewAdapter(myConnectionData));
                if(myConnectionData.size()!=0)
                    binding.ConnectionText.setText("");
            }
        });
        return binding.getRoot();
    }
}
