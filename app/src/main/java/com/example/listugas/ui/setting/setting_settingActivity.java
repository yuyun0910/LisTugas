package com.example.listugas.ui.setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.listugas.R;

public class setting_settingActivity extends AppCompatActivity {

    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_setting_setting);

        ImageButton btn = (ImageButton) findViewById(R.id.back_button);

        radioGroup = findViewById(R.id.radioThemeGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // on radio button check change
                switch (checkedId) {
                    case R.id.radioTheme0Button:
                        // on below line we are checking the radio button with id.
                        // on below line we are setting the text to text view as light mode.
                        // on below line we are changing the theme to light mode.
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        break;
                    case R.id.radioTheme2Button:
                        // this method is called when dark radio button is selected
                        // on below line we are setting dark theme text to our text view.
                        // on below line we are changing the theme to dark mode.
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        break;
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}