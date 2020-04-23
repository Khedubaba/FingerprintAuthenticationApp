package com.adityakhedekar.khedubaba.fingerprintauthenticationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    TextView textView;
    Button goBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        textView = findViewById(R.id.textView);
        goBackBtn = findViewById(R.id.goBackBtn);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        textView.setText(name);
    }

    public void goBack(View view){
        finish();
    }
}
