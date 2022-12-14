package com.example.tenantfinder.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tenantfinder.Activity.MainActivity;
import com.example.tenantfinder.DataModel.MyProfileData;
import com.example.tenantfinder.DataModel.ProfileData;
import com.example.tenantfinder.Utility.Utills;
import com.example.tenantfinder.ViewModel.RegistrationViewModel;
import com.example.tenantfinder.databinding.SignupFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpFragment extends Fragment{

    SignupFragmentBinding binding;
    RegistrationViewModel registrationViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // View Binding :
        binding= SignupFragmentBinding.inflate(getLayoutInflater());

        // View Model :
        registrationViewModel= new ViewModelProvider(this).get(RegistrationViewModel.class);

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

                // SignUp Activity :
                binding.SprogressBar.setVisibility(View.VISIBLE);
                SignUp(Username,Email,Password);

            }
        });
        return binding.getRoot();
    }

    // SignUp Activity :
    public void SignUp(String Username,String Email,String Password)
    {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    registrationViewModel.SetUserSiginupCredentials(new ProfileData(Username,Email,Password));
                    registrationViewModel.SetMyProfileData(new MyProfileData("",Username,Email,"",""));

                    Utills.Toast(getContext(),"Signup Successful");
                    startActivity(new Intent(getContext(), MainActivity.class));
                }
                else
                    Utills.Toast(getContext(),"Error!");

                binding.SprogressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
