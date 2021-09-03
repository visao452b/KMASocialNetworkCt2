package com.example.kmasocialnetworkct2.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kmasocialnetworkct2.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class SignInActivity extends AppCompatActivity {
    ActivitySignInBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    private static final String TAG = Context.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(SignInActivity.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Login to your account");

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

            }
        });
        binding.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });

        binding.clickSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSignUp();
            }
        });

        keepLogin();

    }

    private void keepLogin() {
        if (auth.getCurrentUser()!= null){
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }


    private void signIn() {
        if (binding.edtEmailSignIn.getText().toString().isEmpty()){
            binding.edtEmailSignIn.setError("Enter your email");
            return;
        }

        if (binding.edtPasswordSignIn.getText().toString().isEmpty()){
            binding.edtPasswordSignIn.setError("Enter your password");
            return;
        }


        if(!Patterns.EMAIL_ADDRESS.matcher(binding.edtEmailSignIn.getText().toString()).matches()){
            binding.edtEmailSignIn.setError("Please enter a valid email!");
            binding.edtEmailSignIn.requestFocus();
            return;
        }

        if (binding.edtPasswordSignIn.getText().toString().length()<6){
            binding.edtPasswordSignIn.setError("Min password length should be 6 characters!");
            binding.edtPasswordSignIn.requestFocus();
            return;
        }

        progressDialog.show();
        auth.signInWithEmailAndPassword(binding.edtEmailSignIn.getText().toString().trim(), binding.edtPasswordSignIn.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            FirebaseMessaging.getInstance().getToken()
                                    .addOnCompleteListener(new OnCompleteListener<String>() {
                                        @Override
                                        public void onComplete(@NonNull Task<String> task) {
                                            if (!task.isSuccessful()) {
                                                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                                return;
                                            }
                                            // Get new FCM registration token
                                            String token = task.getResult();
                                            // Log and toast
                                            Log.e(TAG, token);
                                            database.getReference().child("Token").child(auth.getUid()).setValue(token).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.e("Token", "onSuccess");
                                                }
                                            });
                                        }
                                    });
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void forgotPassword() {
        Intent intent = new Intent(SignInActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
    }

    private void clickSignUp() {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}