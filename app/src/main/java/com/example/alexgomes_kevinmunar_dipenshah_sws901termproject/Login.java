package com.example.alexgomes_kevinmunar_dipenshah_sws901termproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

public class Login extends Activity {

    RadioButton radioNurse, radioPatient;
    TextView txtLoginID, txtPassword;
    String loginID, password, usertype;
    Button btnLogin, btnRegister;
    String LOGIN_URL = "http://lalaskinessentials.com/system_info/login.php?";
    String REGISTER_URL = "http://lalaskinessentials.com/system_info/register.php?";
    XMLParser xmlParser;
    Document doc;
    NodeList nl;
    //Intent intent;
    final String PARENT_NODE = "loginInfo";
    final String CHILD_NODE_LOGINID = "loginID";
    final String CHILD_NODE_PASSWORD= "password";
    final String CHILD_NODE_USERTYPE= "userType";
    final String CHILD_NODE_LOG= "log";

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


        final Intent intent;
        intent = new Intent(Login.this,VitalSigns.class);

        radioNurse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioPatient.setChecked(false);
                usertype = "0";
                intent.putExtra("userType","1");
            }
        });
        radioPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioNurse.setChecked(false);
                usertype = "1";
                intent.putExtra("userType","0");
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                xmlParser = new XMLParser(Login.this);
                loginID = txtLoginID.getText().toString();
                password = txtPassword.getText().toString();

                LOGIN_URL += "loginID=" + loginID + "&password=" + password + "&usertype=" + usertype;
                xmlParser.execute(LOGIN_URL);
                System.out.println(LOGIN_URL);
                String result = null;
                try {
                    result = xmlParser.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                doc = xmlParser.getDomElement(result);
                nl = doc.getElementsByTagName(PARENT_NODE);
                LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
                for (int i = 0; i < nl.getLength(); i++) {
                    Element e = (Element) nl.item(i);
                    map.put(CHILD_NODE_LOGINID,xmlParser.getValue(e,CHILD_NODE_LOGINID));
                    map.put(CHILD_NODE_PASSWORD, xmlParser.getValue(e, CHILD_NODE_PASSWORD));
                    map.put(CHILD_NODE_USERTYPE, xmlParser.getValue(e, CHILD_NODE_USERTYPE));
                    map.put(CHILD_NODE_LOG, xmlParser.getValue(e, CHILD_NODE_LOG));
                    System.out.println(map);
                }
                if(txtLoginID.getText().toString().length() > 0 && txtPassword.getText().toString().length() > 0 && radioNurse.isChecked() || radioPatient.isChecked()){
                    if(loginID.equals(map.get(CHILD_NODE_LOGINID)) && password.equals(map.get(CHILD_NODE_PASSWORD))&& usertype.equals(map.get(CHILD_NODE_USERTYPE))){
                        intent.putExtra("loginID",loginID);
                        startActivity(intent);
                    }else{
                        Toast.makeText(Login.this,map.get(CHILD_NODE_LOG)+"\nPlease register",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(Login.this, "Please input everything", Toast.LENGTH_SHORT).show();
                }

                txtLoginID.setText("");
                txtPassword.setText("");
                radioNurse.setChecked(false);
                radioPatient.setChecked(false);
                LOGIN_URL = "http://lalaskinessentials.com/system_info/login.php?";
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                xmlParser = new XMLParser(Login.this);

                loginID = txtLoginID.getText().toString();
                password = txtPassword.getText().toString();

                REGISTER_URL += "loginID=" + loginID + "&password=" + password + "&usertype=" + usertype;
                xmlParser.execute(REGISTER_URL);
                System.out.println(REGISTER_URL);
                String result = null;
                try {
                    result = xmlParser.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                doc = xmlParser.getDomElement(result);
                nl = doc.getElementsByTagName(PARENT_NODE);
                LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
                for (int i = 0; i < nl.getLength(); i++) {
                    Element e = (Element) nl.item(i);
                    map.put(CHILD_NODE_LOG, xmlParser.getValue(e, CHILD_NODE_LOG));
                    System.out.println(map);
                }
                if(txtLoginID.getText().toString().length() > 0 && txtPassword.getText().toString().length() > 0 && radioNurse.isChecked() || radioPatient.isChecked()){
                    if(loginID.equals(map.get(CHILD_NODE_LOGINID)) && password.equals(map.get(CHILD_NODE_PASSWORD))&& usertype.equals(map.get(CHILD_NODE_USERTYPE))){
                        startActivity(intent);
                    }else{
                        Toast.makeText(Login.this,map.get(CHILD_NODE_LOG),Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(Login.this, "Please input everything", Toast.LENGTH_SHORT).show();
                }

                txtLoginID.setText("");
                txtPassword.setText("");
                radioNurse.setChecked(false);
                radioPatient.setChecked(false);
                REGISTER_URL = "http://lalaskinessentials.com/system_info/register.php?";
            }
        });
    }

}
