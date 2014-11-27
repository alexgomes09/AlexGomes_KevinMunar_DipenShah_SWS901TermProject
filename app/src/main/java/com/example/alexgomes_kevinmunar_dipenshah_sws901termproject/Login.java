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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Login extends Activity {

    RadioButton radioNurse,radioPatient;
    TextView txtLoginID,txtPassword;
    String loginID,password,usertype;
    Button btnLogin, btnRegister;
    String URL = "http://lalaskinessentials.com/system_info/login.php?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        radioNurse  = (RadioButton)findViewById(R.id.radioNurse);
        radioPatient = (RadioButton)findViewById(R.id.radioPatient);
        txtLoginID = (TextView)findViewById(R.id.txtUsername);
        txtPassword = (TextView)findViewById(R.id.txtPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnRegister = (Button)findViewById(R.id.btnRegister);

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

                URL +="loginID="+loginID+"&password="+password+"&usertype="+usertype;

                System.out.println(loginID);
                System.out.println(password);
                System.out.println(usertype);
                System.out.println(URL);
                new AccessWebServiceTask().execute(URL);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    // ---Connects using HTTP GET---
    public static InputStream OpenHttpGETConnection(String url) {
        InputStream inputStream = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            inputStream = httpResponse.getEntity().getContent();
        } catch (Exception e) {
            Log.d("", e.getLocalizedMessage());
        }
        return inputStream;
    }

    private String WordDefinition(String word) {
        InputStream in = null;
        String strDefinition = "";
        try {
            in = OpenHttpGETConnection(
                    "http://services.aonaware.com/DictService/" +
                            "DictService.asmx/Define?word=" + word);
            Document doc = null;
            DocumentBuilderFactory dbf =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder db;
            try {
                db = dbf.newDocumentBuilder();
                doc = db.parse(in);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            doc.getDocumentElement().normalize();

            //---retrieve all the <Definition> elements---
            NodeList definitionElements =
                    doc.getElementsByTagName("Definition");

            //---iterate through each <Definition> elements---
            for (int i = 0; i < definitionElements.getLength(); i++) {
                Node itemNode = definitionElements.item(i);
                if (itemNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    //---convert the Definition node into an Element---
                    Element definitionElement = (Element) itemNode;

                    //---get all the <WordDefinition> elements under
                    // the <Definition> element---
                    NodeList wordDefinitionElements =
                            definitionElement.
                                    getElementsByTagName("WordDefinition");

                    strDefinition = "";
                    //---iterate through each <WordDefinition>
                    // elements---
                    for (int j = 0; j <
                            wordDefinitionElements.getLength(); j++) {
                        //---get all the child nodes under the
                        // <WordDefinition> element---
                        NodeList textNodes =
                                (wordDefinitionElements.item(j)).
                                        getChildNodes();
                        strDefinition +=
                                ((Node)
                                        textNodes.item(0)).getNodeValue() +
                                        ". \n";
                    }
                }
            }
        } catch (Exception e) {
            Log.d("NetworkingActivity", e.getLocalizedMessage());
        }
        //---return the definitions of the word---
        return strDefinition;
    }

    private class AccessWebServiceTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            return WordDefinition(urls[0]);
        }

        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        }
    }

}
