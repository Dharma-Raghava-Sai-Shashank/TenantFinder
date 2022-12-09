package com.example.tenantfinder.RegistrationFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.tenantfinder.AppDataDao;
import com.example.tenantfinder.AppDatabase;
import com.example.tenantfinder.DataModel.MyProfileData;
import com.example.tenantfinder.DataModel.ProfileData;
import com.example.tenantfinder.MainActivity;
import com.example.tenantfinder.Utills;
import com.example.tenantfinder.databinding.SignupFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpFragment extends Fragment{

    SignupFragmentBinding binding;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // View Binding :
        binding= SignupFragmentBinding.inflate(getLayoutInflater());

        // SignUp Activity :
        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Intialising Variables:
                String Username=binding.signupUsername.getText().toString();
                String Email=binding.signupEmail.getText().toString().trim();
                String Password=binding.signupPassword.getText().toString().trim();
                String create_password=binding.signupCreatePassword.getText().toString().trim();

                // Checking conditions:
                if (TextUtils.isEmpty(Username)) {
                    binding.signupUsername.setError("Username is required");
                    return;
                }
                if (TextUtils.isEmpty(Email)) {
                    binding.signupEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(Password)) {
                    binding.signupPassword.setError("Password is required");
                    return;
                }
                if (Password.length() < 6) {
                    binding.signupPassword.setError("Password must be >= 6 characters");
                    return;
                }
                if (!Password.equals(create_password)) {
                    binding.signupCreatePassword.setError("Recheck password");
                    return;
                }

                binding.SprogressBar.setVisibility(View.VISIBLE);

                firebaseAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Data:
                            ProfileData profileData =new ProfileData(Username,Email,Password);

                            // Firebase Database:
                            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                            DatabaseReference users =firebaseDatabase.getReference("Users");
                            users.child(firebaseAuth.getUid()).child("Signup Credentials").setValue(profileData);

                            // Room Database :
                            AppDatabase Database= Room.databaseBuilder(getContext(), AppDatabase.class,"Users Data").allowMainThreadQueries().build();
                            AppDataDao houseDataDao=Database.houseDataDao();
                            houseDataDao.insertProfieData(new MyProfileData(firebaseAuth.getUid(),Username,Email,"",""));

                            binding.SprogressBar.setVisibility(View.INVISIBLE);
                            Utills.Toast(getContext(),"Signup Successful");

                            startActivity(new Intent(getContext().getApplicationContext(), MainActivity.class));
                        }
                        else {
                            binding.SprogressBar.setVisibility(View.INVISIBLE);
                            Utills.Toast(getContext(),"Error!");
                        }
                    }
                });
            }
        });
        return binding.getRoot();
    }
}
