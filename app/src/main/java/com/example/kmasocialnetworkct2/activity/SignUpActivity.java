package com.example.kmasocialnetworkct2.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.kmasocialnetworkct2.databinding.ActivitySignUpBinding;
import com.example.kmasocialnetworkct2.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World!");

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are creating your account");

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void signUp() {
        if (binding.edtUsernameSignUp.getText().toString().isEmpty()){
            binding.edtUsernameSignUp.setError("Enter your name");
            return;
        }
        if (binding.edtEmailSignUp.getText().toString().isEmpty()){
            binding.edtEmailSignUp.setError("Enter your email");
            return;
        }
        if (binding.edtPasswordSignUp.getText().toString().isEmpty()){
            binding.edtPasswordSignUp.setError("Enter your password");
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(binding.edtEmailSignUp.getText().toString()).matches()){
            binding.edtEmailSignUp.setError("Please enter a valid email!");
            binding.edtEmailSignUp.requestFocus();
            return;
        }

        if (binding.edtPasswordSignUp.getText().toString().length()<6){
            binding.edtPasswordSignUp.setError("Min password length should be 6 characters!");
            binding.edtPasswordSignUp.requestFocus();
            return;
        }
        progressDialog.show();
        auth.createUserWithEmailAndPassword(binding.edtEmailSignUp.getText().toString().trim(), binding.edtPasswordSignUp.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            String id = task.getResult().getUser().getUid();
                            Users users = new Users(binding.edtUsernameSignUp.getText().toString().trim(), binding.edtEmailSignUp.getText().toString().trim(), id);
                            database.getReference().child("Users").child(id).setValue(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                    startActivity(intent);
                                }
                            });

                        } else {
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}