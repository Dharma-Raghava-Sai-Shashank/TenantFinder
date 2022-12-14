package com.example.tenantfinder.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.tenantfinder.DataModel.MyProfileData;
import com.example.tenantfinder.DataModel.ProfileData;
import com.example.tenantfinder.R;
import com.example.tenantfinder.Activity.Registration;
import com.example.tenantfinder.Utility.Utills;
import com.example.tenantfinder.ViewModel.FragmentViewModel;
import com.example.tenantfinder.ViewModel.MainActivityViewModel;
import com.example.tenantfinder.databinding.ProfileFragmentBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

public class ProfileFragment extends Fragment {

    ProfileFragmentBinding binding;
    FragmentViewModel fragmentViewModel;

    int a=0;
    Uri uri;
    static Bitmap bitmap;
    int N=0,E=0,P=0,A=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Setting Content :
        binding=ProfileFragmentBinding.inflate(getLayoutInflater());
        // View Model :
        fragmentViewModel= new ViewModelProvider(this).get(FragmentViewModel.class);
        // Profile :
        fragmentViewModel.GetMyProfileData();
        fragmentViewModel.MyProfileData.observe(getViewLifecycleOwner(), new Observer<MyProfileData>() {
            @Override
            public void onChanged(MyProfileData ProfileData) {
                if(ProfileData!=null) {
                    binding.ProfileName.setText("Name: " + ProfileData.Name);
                    binding.ProfileEmail.setText("Email: " + ProfileData.Email);
                    binding.ProfilePhone.setText("Phone: " + ProfileData.Phone);
                    binding.ProfileAbout.setText("About: " + ProfileData.About);
                }
            }
        });
        fragmentViewModel.GetProfileImage(getContext(),binding.ProfilePersonImage);

        // Full ImageViewc :
        binding.ProfilePersonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentViewModel.ProfileFullImage(v.getContext());
            }
        });

        // Image Selection:
        binding.ProfileCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a=1;
                binding.submit.setVisibility(View.VISIBLE);
                imageChooser();
            }
        });

        // Editing Profile :
        binding.EditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                N=1;
                binding.ProfileName.setVisibility(View.INVISIBLE);
                binding.EditProfileName.setVisibility(View.VISIBLE);
                binding.submit.setVisibility(View.VISIBLE);
            }
        });
        binding.EditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                E=1;
                binding.ProfileEmail.setVisibility(View.INVISIBLE);
                binding.EditProfileEmail.setVisibility(View.VISIBLE);
                binding.submit.setVisibility(View.VISIBLE);
            }
        });
        binding.EditPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                P=1;
                binding.ProfilePhone.setVisibility(View.INVISIBLE);
                binding.EditProfilePhone.setVisibility(View.VISIBLE);
                binding.submit.setVisibility(View.VISIBLE);
            }
        });
        binding.EditAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                A=1;
                binding.ProfileAbout.setVisibility(View.INVISIBLE);
                binding.EditProfileAbout.setVisibility(View.VISIBLE);
                binding.submit.setVisibility(View.VISIBLE);
            }
        });

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sName=binding.ProfileName.getText().toString().substring(6),
                        sEmail=binding.ProfileEmail.getText().toString().substring(7),
                        sPhone=binding.ProfilePhone.getText().toString().substring(7),
                        sAbout=binding.ProfileAbout.getText().toString().substring(7);

                binding.PprogressBar.setVisibility(View.VISIBLE);

                if(N==1){
                    N=0;
                    binding.EditProfileName.setVisibility(View.INVISIBLE);
                    sName=binding.EditProfileName.getText().toString();
                    binding.ProfileName.setVisibility(View.VISIBLE);
                }
                if(E==1) {
                    E=0;
                    binding.EditProfileEmail.setVisibility(View.INVISIBLE);
                    sEmail = binding.EditProfileEmail.getText().toString();
                    binding.ProfileEmail.setVisibility(View.VISIBLE);
                }
                if(P==1) {
                    P=0;
                    binding.EditProfilePhone.setVisibility(View.INVISIBLE);
                    sPhone = binding.EditProfilePhone.getText().toString();
                    binding.ProfilePhone.setVisibility(View.VISIBLE);
                }
                if(A==1) {
                    A=0;
                    binding.EditProfileAbout.setVisibility(View.INVISIBLE);
                    sAbout = binding.EditProfileAbout.getText().toString();
                    binding.ProfileAbout.setVisibility(View.VISIBLE);
                }

                // Firebase Database:
                fragmentViewModel.SetProfileData(new ProfileData(sName,sEmail,sPhone,sAbout));
                // Room Database :
                fragmentViewModel.SetMyProfileData(new MyProfileData("",sName,sEmail,sPhone,sAbout));
                // Uploading Photo to Firebase Storage :
                if(a==1) {
                    fragmentViewModel.SetProfileImage(uri);
                }
                binding.PprogressBar.setVisibility(View.INVISIBLE);
                binding.submit.setVisibility(View.INVISIBLE);
                Utills.Toast(getContext(),"Profile Added Successfully");

                fragmentViewModel.GetMyProfileData();
            }
        });

        // Logout:
        binding.profilelogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(Utills.AlertDialouge(v.getContext(),"Do you want to Logout ?"))
                { FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(ProfileFragment.this.getActivity().getApplicationContext(), Registration.class));
                }

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
                binding.ProfilePersonImage.setImageBitmap(bitmap);
            }
            catch (Exception e)
            {

            }
        }
    }
}
