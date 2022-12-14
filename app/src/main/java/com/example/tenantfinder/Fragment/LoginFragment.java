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

import com.example.tenantfinder.Activity.ChatActivity;
import com.example.tenantfinder.Activity.MainActivity;
import com.example.tenantfinder.Utility.Utills;
import com.example.tenantfinder.ViewModel.RegistrationViewModel;
import com.example.tenantfinder.databinding.LoginFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    LoginFragmentBinding binding;
    RegistrationViewModel registrationViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // View Binding :
        binding= LoginFragmentBinding.inflate(getLayoutInflater());

        // View Model :
        registrationViewModel= new ViewModelProvider(this).get(RegistrationViewModel.class);

        // Login Activity :
        binding.login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Intialising Variables:
                String email=binding.loginEmail.getText().toString().trim();
                String password=binding.loginPassword.getText().toString().trim();

                // Checking conditions:
                if (TextUtils.isEmpty(email)) {
                    binding.loginEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    binding.loginPassword.setError("Password is required");
                    return;
                }
                if (password.length() < 6) {
                    binding.loginPassword.setError("Password must be >= 6 characters");
                    return;
                }

                // Login Activity :
                binding.LprogressBar.setVisibility(View.VISIBLE);
                Login(email,password);
            }
        });
        return binding.getRoot();
    }

    // Login Activity :
    public void Login(String Email,String Password)
    {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    Utills.Toast(getContext(),"Login Successful");
                    startActivity(new Intent(getContext(), MainActivity.class));
                }
                else
                    Utills.Toast(getContext(),"Error!");
                binding.LprogressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
