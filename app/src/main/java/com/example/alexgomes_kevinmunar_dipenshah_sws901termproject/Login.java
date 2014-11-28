package com.example.alexgomes_kevinmunar_dipenshah_sws901termproject;

import android.app.Activity;
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
    XMLParser xmlParser;
    String URL = "http://lalaskinessentials.com/system_info/login.php?";

    final String PARENT_NODE = "loginInfo";
    final String CHILD_NODE_LOGINID = "loginID";
    final String CHILD_NODE_PASSWORD= "password";
    final String CHILD_NODE_USERTYPE= "userType";
    final String CHILD_NODE_LOG= "log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        xmlParser = new XMLParser(Login.this);

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
                usertype = "0";
            }
        });
        radioPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioNurse.setChecked(false);
                usertype = "1";
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginID = txtLoginID.getText().toString();
                password = txtPassword.getText().toString();

                URL += "loginID=" + loginID + "&password=" + password + "&usertype=" + usertype;
                xmlParser.execute(URL);
                System.out.println(URL);
                String result = null;
                try {
                    result = xmlParser.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                Document doc = xmlParser.getDomElement(result);
                NodeList nl = doc.getElementsByTagName(PARENT_NODE);
//                HashMap<String, String> map = new HashMap<String, String>();
                LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
                for (int i = 0; i < nl.getLength(); i++) {
                    Element e = (Element) nl.item(i);
                    map.put(CHILD_NODE_LOGINID,xmlParser.getValue(e,CHILD_NODE_LOGINID));
                    map.put(CHILD_NODE_PASSWORD, xmlParser.getValue(e, CHILD_NODE_PASSWORD));
                    map.put(CHILD_NODE_USERTYPE, xmlParser.getValue(e, CHILD_NODE_USERTYPE));
                    map.put(CHILD_NODE_LOG, xmlParser.getValue(e, CHILD_NODE_LOG));
                    System.out.println(map);
                }
                Toast.makeText(Login.this,map.toString(),Toast.LENGTH_LONG).show();

                txtLoginID.setText("");
                txtPassword.setText("");
                radioNurse.setChecked(false);
                radioPatient.setChecked(false);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}
