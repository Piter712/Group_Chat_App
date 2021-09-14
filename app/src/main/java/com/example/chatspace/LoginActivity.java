package com.example.chatspace;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button LoginButton;
    private EditText UserEmail,UserPassword;
    private TextView NeedNewAccountLink;
    private ProgressDialog loadingBar;
    private DatabaseReference UsersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        InitializeFields(); // Method For Initializing all fields inside the login Activity
        NeedNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToRegisterActivity();
            }
        });
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllowUserToLogin();
            }
        });

    }

    private void AllowUserToLogin() {
        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email is empty! Please Enter Email", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Password is empty! Please Enter Password...", Toast.LENGTH_SHORT).show();
        }
        else{

            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String currentUserId = mAuth.getCurrentUser().getUid();
                                String deviceToken = FirebaseInstanceId.getInstance().getToken();
                                UsersRef.child(currentUserId).child("device_token")
                                        .setValue(deviceToken)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){

                                                    // sign in success
                                                    sendUserToMainActivity();
                                                    Toast.makeText(LoginActivity.this, "Congrats! you are logged in", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });

                            }else{
                                // sign in fails
                                String message = task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Ups, Error!: " + message, Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

        }
    }

    private void sendUserToRegisterActivity() {
        Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(registerIntent);
    }

    private void InitializeFields() {
        LoginButton = findViewById(R.id.login_button);
        UserEmail = findViewById(R.id.login_email);
        UserPassword = findViewById(R.id.login_password);
        NeedNewAccountLink = findViewById(R.id.need_new_account_link);
    }

    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}