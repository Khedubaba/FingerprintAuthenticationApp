# FingerprintAuthenticationApp
Android figerprint authentication in an app.

This app makes uses of Android's FingerprintManager so you can secure your app with fingerprint authentication.

CryptoObject used is optional you can pass the value of cryptoobject null to FingerprintHandler and all will work fine, though it's recommended
to use it as it completely manage new fingeorint entry on device and unique key generation and storing of key in Android KeyStore.

Note: 
1. You need API Level 23 or greater to use FingerprintManager

2. Since API 28 FingerprintManage is depricated and is replaced by BiometricPrompt 

#Links:

1. BiometricPrompt:

https://developer.android.com/reference/android/hardware/biometrics/BiometricPrompt

https://medium.com/mindorks/fingerprint-authentication-using-biometricprompt-compat-1466365b4795

2. CryptoObject:

https://stackoverflow.com/questions/39470098/why-crypto-object-is-needed-for-android-fingerprint-authentication

https://medium.com/@manuelvicnt/android-fingerprint-authentication-f8c7c76c50f8
