package com.adityakhedekar.khedubaba.fingerprintauthenticationapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;

    FingerprintHandler(Context context){
        this.context = context;
    }
}
