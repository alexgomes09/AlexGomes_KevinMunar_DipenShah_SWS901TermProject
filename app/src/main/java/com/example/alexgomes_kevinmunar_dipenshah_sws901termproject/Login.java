package com.example.alexgomes_kevinmunar_dipenshah_sws901termproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class Login extends Activity {

    RadioButton radioNurse, radioPatient;
    TextView txtLoginID, txtPassword;
    String loginID, password, usertype;
    Button btnLogin, btnRegister;
    String URL = "http://lalaskinessentials.com/system_info/login.php?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        radioNurse = (RadioButton) findViewById(R.id.radioNurse);
        radioPatient = (RadioButton) findViewById(R.id.radioPatient);
        txtLoginID = (TextView) findViewById(R.id.txtUsername);
        txtPassword = (TextView) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        radioNurse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioPatient.setChecked(false);
                usertype = "nurse";
            }
        });
        radioPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioNurse.setChecked(false);
                usertype = "patient";
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginID = txtLoginID.getText().toString();
                password = txtPassword.getText().toString();

                URL += "loginID=" + loginID + "&password=" + password + "&usertype=" + usertype;

                XMLParser xmlParser = new XMLParser(Login.this);
                xmlParser.execute(URL);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}
