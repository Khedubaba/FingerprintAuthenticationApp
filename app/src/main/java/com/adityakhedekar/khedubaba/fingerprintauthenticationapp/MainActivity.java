package com.adityakhedekar.khedubaba.fingerprintauthenticationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextView headLable, paraLable;
    private ImageView fingerprintImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        headLable = findViewById(R.id.headingLabel);
        paraLable = findViewById(R.id.paraLabel);
        fingerprintImage = findViewById(R.id.fingerprintImage);

        // TODO Check 1: Android version should be greater or equal to marshmallow
        // TODO Check 2: Device has fingerprint Scanner or not
        // TODO Check 3: Have permission to use fingerprint scanner for app
        // TODO Check 4: Lock screen is secured with atleast one type of lock
        // TODO Check 4: At least one fingerprint is registered in device

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            
        }
    }
}
