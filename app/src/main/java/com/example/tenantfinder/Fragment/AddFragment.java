package com.example.tenantfinder.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.example.tenantfinder.Database.AppDatabase;
import com.example.tenantfinder.DataModel.HouseData;
import com.example.tenantfinder.DataModel.MyHouseData;
import com.example.tenantfinder.Interface.AppDataDao;
import com.example.tenantfinder.R;
import com.example.tenantfinder.Utility.Utills;
import com.example.tenantfinder.ViewModel.FragmentViewModel;
import com.example.tenantfinder.databinding.AddFragmentBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.List;

public class AddFragment extends Fragment {

    AddFragmentBinding binding;
    FragmentViewModel fragmentViewModel;

    Uri uri;
    Bitmap bitmap;
    int a=0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding=AddFragmentBinding.inflate(getLayoutInflater());
        fragmentViewModel= new ViewModelProvider(this).get(FragmentViewModel.class);

        // Add:
        binding.AddCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
                a=1;
            }
        });
        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (a != 1) {
                    Utills.Toast(getContext(), "Add Image");
                    return;
                }
                binding.AprogressBar.setVisibility(View.VISIBLE);

                HouseData houseData = new HouseData(binding.AddHouseName.getText().toString(), binding.AddOwnerName.getText().toString()
                        , binding.AddPrice.getText().toString(), binding.AddAddress.getText().toString(), binding.AddDescription.getText().toString());

                // Room Database:
                fragmentViewModel.SetHouseData(houseData);

                // Uploading Photo to Firebase Storage :
                fragmentViewModel.SetHouseImage(uri);

                // Firebase Database :
                fragmentViewModel.SetMyHouseData(new MyHouseData("", binding.AddHouseName.getText().toString(), binding.AddOwnerName.getText().toString(),
                        binding.AddPrice.getText().toString(), binding.AddAddress.getText().toString(), binding.AddDescription.getText().toString()));

                
                binding.AddHouseName.setText("");binding.AddOwnerName.setText("");binding.AddPrice.setText("");binding.AddAddress.setText("");binding.AddDescription.setText("");
//                binding.AddHouseImage.setImageResource(R.drawable.house1);
                binding.AprogressBar.setVisibility(View.INVISIBLE);
                Utills.Toast(getContext(),"Added Successfully");
            }
        });
        return binding.getRoot();
    }

    // Image Selector:
    public void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), 200);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK&&requestCode == 200 ) {
            uri = data.getData();
            try {
                InputStream inputStream=getActivity().getContentResolver().openInputStream(uri);
                bitmap= BitmapFactory.decodeStream(inputStream);
                binding.AddHouseImage.setImageBitmap(bitmap);
            }
            catch (Exception e)
            {

            }
        }
    }
}
