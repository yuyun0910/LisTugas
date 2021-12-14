package com.example.listugas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listugas.ui.add_task.TaskModel;
import com.example.listugas.ui.add_task.add_task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    TextInputEditText etRegEmail;
    TextInputEditText etRegPassword;
    TextInputEditText etRegUsername;
    TextView tvLoginHere;
    Button btnRegister;

    FirebaseDatabase DBFirebase;
    DatabaseReference DBreference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String ActiveUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_signup);

        etRegUsername = findViewById(R.id.signUp_inputName);
        etRegEmail = findViewById(R.id.signUp_inputEmail);
        etRegPassword = findViewById(R.id.signUp_inputPwd);
        tvLoginHere = findViewById(R.id.signUp_buttonLogin);
        btnRegister = findViewById(R.id.signUp_buttonSignUp);

        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(view ->{
            createUser();
        });

        tvLoginHere.setOnClickListener(view ->{
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        });
    }

    private void createUser(){
        String username = etRegUsername.getText().toString();
        String email = etRegEmail.getText().toString();
        String password = etRegPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            etRegEmail.setError("Email cannot be empty");
            etRegEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            etRegPassword.setError("Password cannot be empty");
            etRegPassword.requestFocus();
        }else if (TextUtils.isEmpty(username)){
            etRegPassword.setError("username cannot be empty");
            etRegPassword.requestFocus();
        }else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    mUser = mAuth.getCurrentUser();
                                    ActiveUID = mUser.getUid();
                                    DBFirebase = FirebaseDatabase.getInstance();
                                    DBreference = DBFirebase.getReference().child("users").child(ActiveUID);

                                    UserModel NewModel = new UserModel(username, email, false);
                                    DBreference.setValue(NewModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(SignupActivity.this, "Welcome " + username + "!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(SignupActivity.this, SignupSuccessActivity.class));
                                            } else {
                                                String error = task.getException().toString();
                                                Toast.makeText(SignupActivity.this, "Failed: " + error, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }else{
                                    Toast.makeText(SignupActivity.this, "Log in Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(SignupActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void MoveLogin(View view) {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}