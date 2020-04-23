package com.adityakhedekar.khedubaba.fingerprintauthenticationapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;

    FingerprintHandler(Context context){
        this.context = context;
    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){

        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        this.update("There was an Auth Error " + errString, false);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("Authentication Failed", false);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        this.update("Error:" + helpString, false);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("Authentication Successful", true);
    }

    private void update(String s, boolean b) {
        TextView paraLable = ((Activity)context).findViewById(R.id.paraLabel);
        ImageView imageView = ((Activity)context).findViewById(R.id.fingerprintImage);

        paraLable.setText(s);

        if (!b){
            paraLable.setTextColor(ContextCompat.getColor(context, R.color.error));
            imageView.setImageResource(R.mipmap.fingerprint_error_round);
        }
        else {
            paraLable.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            imageView.setImageResource(R.mipmap.action_done);
        }
    }
}
