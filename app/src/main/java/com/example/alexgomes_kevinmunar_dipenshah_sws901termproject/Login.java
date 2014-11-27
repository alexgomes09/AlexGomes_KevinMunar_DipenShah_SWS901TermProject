package com.example.alexgomes_kevinmunar_dipenshah_sws901termproject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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


                new LoginTask().execute(URL);
//                new AccessWebServiceTask().execute("Hello");
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }



    protected class LoginTask extends AsyncTask<String, Void, String> {

        public String getXmlFromUrl(String url) {
            String xml = null;

            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                xml = EntityUtils.toString(httpEntity);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // return XML
            return xml;
        }

        public Document getDomElement(String xml){
            Document doc = null;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            try {

                DocumentBuilder db = dbf.newDocumentBuilder();

                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(xml));
                doc = db.parse(is);

            } catch (ParserConfigurationException e) {
                Log.e("Error: ", e.getMessage());
                return null;
            } catch (SAXException e) {
                Log.e("Error: ", e.getMessage());
                return null;
            } catch (IOException e) {
                Log.e("Error: ", e.getMessage());
                return null;
            }

            return doc;
        }

        public final String getElementValue( Node elem ) {
            Node child;
            if( elem != null){
                if (elem.hasChildNodes()){
                    for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
                        if( child.getNodeType() == Node.TEXT_NODE  ){
                            return child.getNodeValue();
                        }
                    }
                }
            }
            return "";
        }

        public String getValue(Element item, String str) {
            NodeList n = item.getElementsByTagName(str);
            return this.getElementValue(n.item(0));
        }

        protected String doInBackground(String... urls) {
            return getXmlFromUrl(urls[0]);
        }

        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), result,Toast.LENGTH_LONG).show();
            System.out.println("Result= "+result);
        }
    }

}
