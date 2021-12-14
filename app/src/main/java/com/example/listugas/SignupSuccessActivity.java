package com.example.listugas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class SignupSuccessActivity extends AppCompatActivity {

    private int loading_time=2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_signup_success);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent home=new Intent(SignupSuccessActivity.this, NavActivity.class);
                startActivity(home);
                finish();
            }
        },loading_time);
    }
}