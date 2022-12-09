package com.example.tenantfinder.MainactivityFragments;

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
import androidx.room.Room;

import com.example.tenantfinder.AppDatabase;
import com.example.tenantfinder.DataModel.HouseData;
import com.example.tenantfinder.DataModel.MyHouseData;
import com.example.tenantfinder.AppDataDao;
import com.example.tenantfinder.R;
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

    ImageView HouseIamge,Addcamera;
    Button Submit;
    EditText HouseName,OwnerName,Price,Address,Description;
    ProgressBar progressBar;
    Uri uri;
    static Bitmap bitmap;
    int a=0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=(ViewGroup)inflater.inflate(R.layout.add_fragment,container,false);
        Addcamera=view.findViewById(R.id.AddCamera);
        HouseIamge=view.findViewById(R.id.AddHouseImage);
        Submit=view.findViewById(R.id.submit);
        HouseName=view.findViewById(R.id.AddHouseName);
        OwnerName=view.findViewById(R.id.AddOwnerName);
        Price=view.findViewById(R.id.AddPrice);
        Address=view.findViewById(R.id.AddAddress);
        Description=view.findViewById(R.id.AddDescription);
        progressBar=view.findViewById(R.id.AprogressBar);

        // First Nonempty:
        HouseName.setText("");
        OwnerName.setText("");
        Price.setText("");
        Address.setText("");
        Description.setText("");

        // Add:
        Addcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
                a=1;
            }
        });
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a!=1)
                {
                    Toast.makeText(getContext(), "Add Image", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                HouseData houseData =new HouseData(HouseName.getText().toString(),OwnerName.getText().toString(),Price.getText().toString(),Address.getText().toString(),Description.getText().toString());

                // Room Database:
                AppDatabase Database= Room.databaseBuilder(getActivity().getApplicationContext(), AppDatabase.class,"USER DATA").allowMainThreadQueries().build();
                AppDataDao houseDataDao=Database.houseDataDao();
                List<MyHouseData>Data =houseDataDao.getAllHouseData();

                // Firebase Database :
                FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                DatabaseReference users =firebaseDatabase.getReference("Users");
                users.child(firebaseAuth.getUid()).child("House").child(firebaseAuth.getUid()+Data.size()).setValue(houseData);
                DatabaseReference Houses=firebaseDatabase.getReference("House Data");

                HouseData newhouseData =new HouseData(users.child(firebaseAuth.getUid()).child("House").child(firebaseAuth.getUid()+Data.size()).getKey(),HouseName.getText().toString(),OwnerName.getText().toString(),Price.getText().toString(),Address.getText().toString(),Description.getText().toString());
                houseDataDao.insertHouseData(new MyHouseData(users.child(firebaseAuth.getUid()).child("House").child(firebaseAuth.getUid()+Data.size()).getKey(),HouseName.getText().toString(),OwnerName.getText().toString(),Price.getText().toString(),Address.getText().toString(),Description.getText().toString()));
                Houses.child(firebaseAuth.getUid()+Data.size()).setValue(newhouseData);


                // Uploading Photo to Firebase Storage :
                FirebaseStorage storage=FirebaseStorage.getInstance();
                StorageReference HouseImages=storage.getReference().child("House Image").child(firebaseAuth.getUid()+Data.size());
                HouseImages.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        HouseName.setText("");
                        OwnerName.setText("");
                        Price.setText("");
                        Address.setText("");
                        Description.setText("");
                        HouseIamge.setImageResource(R.drawable.house1);
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
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
                HouseIamge.setImageBitmap(bitmap);
            }
            catch (Exception e)
            {

            }
        }
    }
}
