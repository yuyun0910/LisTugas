package com.example.listugas.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.listugas.LoginActivity;
import com.example.listugas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingFragment extends Fragment {

    FirebaseDatabase DBFirebase;
    DatabaseReference DBreference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String ActiveUID;
    String username;

    TextView usernameView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        Button ButtonDetail = (Button) view.findViewById(R.id.account_clickDetail);
        Button ButtonSetting = (Button) view.findViewById(R.id.setting_optionSetting);
        Button ButtonAbout = (Button) view.findViewById(R.id.setting_optionAbout);
        Button ButtonLogout = (Button) view.findViewById(R.id.setting_optionLogout);

        usernameView = (TextView) view.findViewById(R.id.setting_username);

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



        ButtonDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), setting_profile.class);
                startActivity(intent);
            }
        });

        ButtonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), setting_settingActivity.class);
                startActivity(intent);
            }
        });

        ButtonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), setting_aboutActivity.class);
                startActivity(intent);
            }
        });

        ButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(getActivity(), "Logout Success.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}