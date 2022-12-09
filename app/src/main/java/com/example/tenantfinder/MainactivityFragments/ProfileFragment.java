package com.example.tenantfinder.MainactivityFragments;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.tenantfinder.AppDataDao;
import com.example.tenantfinder.AppDatabase;
import com.example.tenantfinder.DataModel.MyProfileData;
import com.example.tenantfinder.DataModel.ProfileData;
import com.example.tenantfinder.R;
import com.example.tenantfinder.Registration;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.List;

public class ProfileFragment extends Fragment {

//    private static final int RESULT_OK = 200;
    ImageView ProfilePic,Camera,EditName,EditPhone,EditEmail,EditAbout;
    TextView Name,Phone,Email,About;
    EditText EditN,EditP,EditE,EditA;
    ProgressBar progressBar;
    int a=0;
    Button Logout,Submit;
    Uri uri;
    static Bitmap bitmap;
    int N=0,E=0,P=0,A=0;
    ImageView FullHouseImage;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= (ViewGroup)inflater.inflate(R.layout.profile_fragment,container,false);

        // Initialising Variables:
        ProfilePic=view.findViewById(R.id.ProfilePersonImage);
        Camera=view.findViewById(R.id.ProfileCamera);
        Logout=view.findViewById(R.id.profilelogout);
        EditName=view.findViewById(R.id.EditName);
        EditEmail=view.findViewById(R.id.EditEmail);
        EditPhone=view.findViewById(R.id.EditPhone);
        EditAbout=view.findViewById(R.id.EditAbout);
        EditN=view.findViewById(R.id.EditProfileName);
        EditP=view.findViewById(R.id.EditProfilePhone);
        EditE=view.findViewById(R.id.EditProfileEmail);
        EditA=view.findViewById(R.id.EditProfileAbout);
        Name=view.findViewById(R.id.ProfileName);
        Email=view.findViewById(R.id.ProfileEmail);
        Phone=view.findViewById(R.id.ProfilePhone);
        About=view.findViewById(R.id.ProfileAbout);
        Submit=view.findViewById(R.id.submit);
        progressBar=view.findViewById(R.id.PprogressBar);

        // Intial Profile:
        AppDatabase Database= Room.databaseBuilder(getActivity().getApplicationContext(), AppDatabase.class,"USER DATA").allowMainThreadQueries().build();
        AppDataDao houseDataDao=Database.houseDataDao();
        List<MyProfileData> profileDataList=houseDataDao.getAllProfileData();
        if(profileDataList.size()!=0) {
            Name.setText("Name: "+profileDataList.get(profileDataList.size()-1).Name);
            Email.setText("Email: "+profileDataList.get(profileDataList.size()-1).Email);
            Phone.setText("Phone: "+profileDataList.get(profileDataList.size()-1).Phone);
            About.setText("About: "+profileDataList.get(profileDataList.size()-1).About);
        }

        // Firebase Storage :
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference HouseImages=storage.getReference().child("Profile Image").child(FirebaseAuth.getInstance().getUid());

        HouseImages.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(view).load(uri).placeholder(R.drawable.profile).into(ProfilePic);
            }
        });

        final boolean[] isImageFitToScreen = {false};
        ProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfilePic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Dialog Box :
                        AlertDialog.Builder fullimage=new AlertDialog.Builder(v.getRootView().getContext());
                        View view=LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.fullimage_dialog,null);

                        FullHouseImage=view.findViewById(R.id.FullImage);
                        fullimage.setView(view);

                        HouseImages.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(view).load(uri).placeholder(R.drawable.profile).into(FullHouseImage);
                            }
                        });
                        if(isImageFitToScreen[0]) {
                            isImageFitToScreen[0] =false;
                            FullHouseImage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            FullHouseImage.setAdjustViewBounds(true);
                        }else{
                            isImageFitToScreen[0] =true;
                            FullHouseImage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                            FullHouseImage.setScaleType(ImageView.ScaleType.FIT_XY);
                        }

                        final AlertDialog alertDialog=fullimage.create();
                        alertDialog.setCanceledOnTouchOutside(true);
                        alertDialog.show();
                    }
                });
            }
        });

        // Image Selection:
        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a=1;
                Submit.setVisibility(View.VISIBLE);
                imageChooser();
            }
        });

        // Editing Profile :
        EditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                N=1;
                Name.setVisibility(View.INVISIBLE);
                EditN.setVisibility(View.VISIBLE);
                Submit.setVisibility(View.VISIBLE);
            }
        });
        EditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                E=1;
                Email.setVisibility(View.INVISIBLE);
                EditE.setVisibility(View.VISIBLE);
                Submit.setVisibility(View.VISIBLE);
            }
        });
        EditPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                P=1;
                Phone.setVisibility(View.INVISIBLE);
                EditP.setVisibility(View.VISIBLE);
                Submit.setVisibility(View.VISIBLE);
            }
        });
        EditAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                A=1;
                About.setVisibility(View.INVISIBLE);
                EditA.setVisibility(View.VISIBLE);
                Submit.setVisibility(View.VISIBLE);
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            String sName,sEmail,sPhone,sAbout;
            @Override
            public void onClick(View v) {

                if(a!=1)
                {
                    Toast.makeText(getContext(), "Add Profile Image", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                if(N==1){
                    EditN.setVisibility(View.INVISIBLE);
                    Name.setText("Name: "+EditN.getText().toString());
                    sName=EditN.getText().toString();
                    Name.setVisibility(View.VISIBLE);
                }
                if(E==1) {
                    EditE.setVisibility(View.INVISIBLE);
                    Email.setText("Email: " + EditE.getText().toString());
                    sEmail = EditE.getText().toString();
                    Email.setVisibility(View.VISIBLE);
                }
                if(P==1) {
                    EditP.setVisibility(View.INVISIBLE);
                    Phone.setText("Phone: " + EditP.getText().toString());
                    sPhone = EditP.getText().toString();
                    Phone.setVisibility(View.VISIBLE);
                }
                if(A==1) {
                    EditA.setVisibility(View.INVISIBLE);
                    About.setText("About: " + EditA.getText().toString());
                    sAbout = EditA.getText().toString();
                    About.setVisibility(View.VISIBLE);
                }
                Submit.setVisibility(View.INVISIBLE);
                ProfileData profileData =new ProfileData(Name.getText().toString().substring(6),Email.getText().toString().substring(7),Phone.getText().toString().substring(7),About.getText().toString().substring(7));

                // Firebase Database:
                FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                DatabaseReference users =firebaseDatabase.getReference("Users");
                users.child(firebaseAuth.getUid()).child("Profile").setValue(profileData);

                // Room Database :
                AppDatabase Database= Room.databaseBuilder(getActivity().getApplicationContext(), AppDatabase.class,"USER DATA").allowMainThreadQueries().build();
                AppDataDao houseDataDao=Database.houseDataDao();
                houseDataDao.deleteAllProfileData();
                houseDataDao.insertProfieData(new MyProfileData(firebaseAuth.getUid(),Name.getText().toString().substring(6),Email.getText().toString().substring(7),Phone.getText().toString().substring(7),About.getText().toString().substring(7)));

                // Uploading Photo to Firebase Storage :
                if(a==1)
                {
                    FirebaseStorage storage=FirebaseStorage.getInstance();
                    StorageReference ProfileImage=storage.getReference().child("Profile Image").child(firebaseAuth.getUid());
                    ProfileImage.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getContext(), "Profile Added Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "Profile Added Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Logout:
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileFragment.this.getActivity().getApplicationContext(), Registration.class));
            }
        });
        return view;
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
                ProfilePic.setImageBitmap(bitmap);
            }
            catch (Exception e)
            {

            }
        }
    }

    // Logout :
    public void logout(View view)
    {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(ProfileFragment.this.getActivity().getApplicationContext(), Registration.class));
    }
}
