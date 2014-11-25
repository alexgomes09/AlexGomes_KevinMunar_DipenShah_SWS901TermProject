package com.example.alexgomes_kevinmunar_dipenshah_sws901termproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class Login extends Activity {

    RadioButton radioNurse,radioPatient;
    Button btnLogin, btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        radioNurse  = (RadioButton)findViewById(R.id.radioNurse);
        radioPatient = (RadioButton)findViewById(R.id.radioPatient);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnRegister = (Button)findViewById(R.id.btnRegister);

        radioNurse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioPatient.setChecked(false);
            }
        });
        radioPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioNurse.setChecked(false);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
