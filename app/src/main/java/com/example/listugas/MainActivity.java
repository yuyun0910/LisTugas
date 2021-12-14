package com.example.listugas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.view.Window;


public class MainActivity extends AppCompatActivity {

    private int loading_time=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent home=new Intent(MainActivity.this, NavActivity.class);
                startActivity(home);
                finish();
            }
        },loading_time);
    }
}
