package com.adityakhedekar.khedubaba.fingerprintauthenticationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextView headLable, paraLable;
    private ImageView fingerprintImage;

    private FingerprintManager mFingerprintManager;
    private KeyguardManager mKeyguardManager;

    private FingerprintHandler fingerprintHandler;
    FingerprintManager.CryptoObject mCryptoObject;

    //keystore variables
    private KeyStore mKeyStore; //stores unique key each time you generate a key for authenticate
    private Cipher mCipher;
    private String KEY_NAME = "AndroidKey";


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
        generateKey();
        if (cipherInit()){
            mCryptoObject = new FingerprintManager.CryptoObject(mCipher);
            fingerprintHandler = new FingerprintHandler(this);
            fingerprintHandler.startAuth(mFingerprintManager, mCryptoObject);
        }

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

    @TargetApi(Build.VERSION_CODES.M)
    private void generateKey() {

        try {
            mKeyStore = KeyStore.getInstance("AndroidKeyStore");
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            mKeyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();

        } catch (KeyStoreException | IOException | CertificateException
                | NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | NoSuchProviderException e) {

            e.printStackTrace();
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            mCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            mKeyStore.load(null);
            SecretKey key = (SecretKey) mKeyStore.getKey(KEY_NAME, null);
            mCipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        }
        catch (KeyPermanentlyInvalidatedException e) {
            return false;
        }
        catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }

    }


}
