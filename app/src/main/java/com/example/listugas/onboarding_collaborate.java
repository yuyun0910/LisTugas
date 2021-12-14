package com.example.listugas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class onboarding_collaborate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_onboarding_collaborate);
    }

    public void MoveLogin(View view) {
        Intent intent = new Intent(onboarding_collaborate.this, LoginActivity.class);
        startActivity(intent);
    }
}