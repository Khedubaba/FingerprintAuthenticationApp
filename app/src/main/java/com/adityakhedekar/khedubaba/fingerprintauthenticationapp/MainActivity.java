package com.adityakhedekar.khedubaba.fingerprintauthenticationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextView headLable, paraLable;
    private ImageView fingerprintImage;

    private FingerprintManager mFingerprintManager;
    private KeyguardManager mKeyguardManager;

    private FingerprintHandler fingerprintHandler;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED){
                    Log.i(TAG, "Permission Granted to access fingerprint");
                    if (mKeyguardManager.isKeyguardSecure()){
                        paraLable.setText(R.string.add_atleast_one_type_lock);
                    }
                    else if (mFingerprintManager.hasEnrolledFingerprints()){
                        paraLable.setText(R.string.add_atleast_one_fingerprint);
                    }
                    else {
                        paraLable.setText(R.string.place_your_finge_on_the_scanner_to_proceed);
                        setFingerprintHandler();
                    }
                }
            }
        }

    }

    protected void setFingerprintHandler(){
         fingerprintHandler = new FingerprintHandler(this);
         fingerprintHandler.startAuth(mFingerprintManager, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        headLable = findViewById(R.id.headingLabel);
        paraLable = findViewById(R.id.paraLabel);
        fingerprintImage = findViewById(R.id.fingerprintImage);

        // Check 1: Android version should be greater or equal to marshmallow
        // Check 2: Device has fingerprint Scanner or not
        // Check 3: Have permission to use fingerprint scanner for app
        // Check 4: Lock screen is secured with atleast one type of lock
        // Check 5: At least one fingerprint is registered in device

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            mFingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            mKeyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

            if (!mFingerprintManager.isHardwareDetected()){
                paraLable.setText(R.string.fingerprint_scanner_not_detected);
            }
            else if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED){
                paraLable.setText(R.string.permission_fingerprint_scanner_not_granted);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_FINGERPRINT}, 1);
            }
            else if (!mKeyguardManager.isKeyguardSecure()){
                paraLable.setText(R.string.add_atleast_one_type_lock);
            }
            else if (!mFingerprintManager.hasEnrolledFingerprints()){
                paraLable.setText(R.string.add_atleast_one_fingerprint);
            }
            else {
                paraLable.setText(R.string.place_your_finge_on_the_scanner_to_proceed);
                setFingerprintHandler();
            }

        }
    }
}
