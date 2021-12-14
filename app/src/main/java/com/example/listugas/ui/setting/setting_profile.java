package com.example.listugas.ui.setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listugas.R;
import com.example.listugas.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class setting_profile extends AppCompatActivity {

    FirebaseDatabase DBFirebase;
    DatabaseReference DBreference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String ActiveUID;
    String username, email, isPremiumSTR;
    Boolean isPremium;

    TextView btnChangeName;
    TextView usernameView, emailView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_account_profile);

        ImageButton btn = (ImageButton) findViewById(R.id.back_button);
        btnChangeName = (TextView) findViewById(R.id.profile_btnChangeName);

        usernameView = (TextView) findViewById(R.id.account_username);
        emailView = (TextView) findViewById(R.id.account_email);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        ActiveUID = mUser.getUid();
        DBFirebase = FirebaseDatabase.getInstance();
        DBreference = DBFirebase.getReference().child("users").child(ActiveUID);

        DBreference.child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username = (String) snapshot.getValue();

                usernameView.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DBreference.child("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email = (String) snapshot.getValue();

                emailView.setText(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DBreference.child("isPremium").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                isPremiumSTR = (String) snapshot.getValue();
                if (isPremiumSTR == "true"){
                    isPremium = true;
                } else {
                    isPremium = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeUsername();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void changeUsername(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View view = inflater.inflate(R.layout.change_username, null);
        myDialog.setView(view);

        AlertDialog dialog = myDialog.create();

        EditText mUsername = view.findViewById(R.id.chUsername_inputUsername);
        mUsername.setText(username);

        Button btnChange = view.findViewById(R.id.chUsername_btnChange);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_username = mUsername.getText().toString().trim();

                UserModel UpdateUser = new UserModel(new_username, email, isPremium);
                DBreference.setValue(UpdateUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(setting_profile.this, "Changed Successfully.", Toast.LENGTH_SHORT).show();
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                        } else {
                            String error = task.getException().toString();
                            Toast.makeText(setting_profile.this, "Failed: " + error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        dialog.show();
    }
}