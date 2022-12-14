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

import com.example.tenantfinder.Activity.MainActivity;
import com.example.tenantfinder.Adapter.FavouriteRecyclerViewAdapter;
import com.example.tenantfinder.DataModel.MyFavouriteData;
import com.example.tenantfinder.R;
import com.example.tenantfinder.ViewModel.FragmentViewModel;
import com.example.tenantfinder.ViewModel.MainActivityViewModel;
import com.example.tenantfinder.databinding.FavoriteFragmentBinding;

import java.util.List;

public class FavouriteFragment extends Fragment {

    FavoriteFragmentBinding binding;
    FragmentViewModel fragmentViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // View Binding :
        binding=FavoriteFragmentBinding.inflate(getLayoutInflater());

        // View Model :
        fragmentViewModel= new ViewModelProvider(this).get(FragmentViewModel.class);

        // Room Database :
        fragmentViewModel.GetFavpuriteData();
        fragmentViewModel.FavouriteData.observe(getViewLifecycleOwner(), new Observer<List<MyFavouriteData>>() {
            @Override
            public void onChanged(List<MyFavouriteData> data) {
                binding.FavoriteRecyclerView.setAdapter(new FavouriteRecyclerViewAdapter(data));
                if(data.size()!=0)
                    binding.FavouritesText.setText("");
            }
        });
        return binding.getRoot();
    }
}
