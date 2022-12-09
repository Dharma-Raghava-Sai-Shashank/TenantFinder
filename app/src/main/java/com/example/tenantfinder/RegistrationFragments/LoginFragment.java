package com.example.tenantfinder.RegistrationFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tenantfinder.MainActivity;
import com.example.tenantfinder.R;
import com.example.tenantfinder.Registration;
import com.example.tenantfinder.Utills;
import com.example.tenantfinder.databinding.LoginFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    LoginFragmentBinding binding;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // View Binding :
        binding=LoginFragmentBinding.inflate(getLayoutInflater());

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

                binding.LprogressBar.setVisibility(View.VISIBLE);

                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            binding.LprogressBar.setVisibility(View.INVISIBLE);
                            Utills.Toast(getContext(),"Login Successful");
                            startActivity(new Intent(getContext().getApplicationContext(), MainActivity.class));
                        }
                        else {
                            binding.LprogressBar.setVisibility(View.INVISIBLE);
                            Utills.Toast(getContext(),"Error!");
                        }
                    }
                });
            }
        });
        return binding.getRoot();
    }
}
