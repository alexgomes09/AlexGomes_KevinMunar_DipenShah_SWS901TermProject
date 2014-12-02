package com.example.alexgomes_kevinmunar_dipenshah_sws901termproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class SplashScreen extends Activity {

    Button btnSplashScreenLogin, btnEmergency;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        btnSplashScreenLogin = (Button)findViewById(R.id.btnSplashScreenLogin);
        btnEmergency = (Button)findViewById((R.id.emergencybtn));

        btnSplashScreenLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashScreen.this,Login.class);
                startActivity(intent);
            }
        });

        btnEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashScreen.this,EmergencySMS.class);
                startActivity(intent);
            }
        });
    }
}
