package com.example.alexgomes_kevinmunar_dipenshah_sws901termproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

public class VitalSigns extends Activity {

    TextView txtBodyTemperature,txtHeartBeat,txtBloodPressure;
    String bodyTemperature,heartRate,bloodPressure,patientID,nurseID;
    Spinner patientSpinner;
    Button btnAddVitalSigns,btnCancel;
    String URL = "http://lalaskinessentials.com/system_info/enterVitalSigns.php?";
    XMLParser xmlParser;
    final String PARENT_NODE = "vitalSign";
    final String CHILD_NODE_LOG= "log";
    final String PARENT_NODE_GETALLPATIENTNAME = "patientName";
    final String CHILD_NODE_LOGINID = "loginID";
    Document doc;
    NodeList nl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vital_signs);

        txtBodyTemperature = (TextView) findViewById(R.id.bodyTemperature);
        txtHeartBeat = (TextView) findViewById(R.id.heartBeat);
        txtBloodPressure = (TextView) findViewById(R.id.bloodPressure);
        patientSpinner = (Spinner) findViewById(R.id.patientSpinner);
        btnAddVitalSigns = (Button)findViewById(R.id.btnAddVitalSigns);

        //get all the patient and add them in spinner
        xmlParser = new XMLParser(VitalSigns.this);
        String getAllPatientURL = "http://lalaskinessentials.com/system_info/getAllPatient.php";
        xmlParser.execute(getAllPatientURL);
        String result = null;
        try {
            result = xmlParser.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        doc = xmlParser.getDomElement(result);
        nl = doc.getElementsByTagName(PARENT_NODE_GETALLPATIENTNAME);
        LinkedHashMap<String,String> patientLinkedHashMap = new LinkedHashMap<String, String>();
        ArrayAdapter<String> patientNameAdapter = new ArrayAdapter<String>(VitalSigns.this,android.R.layout.simple_spinner_item);
        for (int i = 0; i < nl.getLength(); i++) {
            Element e = (Element)nl.item(i);
            patientLinkedHashMap.put(CHILD_NODE_LOGINID, xmlParser.getValue(e, CHILD_NODE_LOGINID));
            patientNameAdapter.add(patientLinkedHashMap.get(CHILD_NODE_LOGINID));
            patientSpinner.setAdapter(patientNameAdapter);
            patientNameAdapter.notifyDataSetChanged();
        }
        patientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                patientID = adapterView.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //get patient name end here

        Intent intent = getIntent();
        nurseID = intent.getStringExtra("nurseID");

        btnAddVitalSigns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtBloodPressure.getText().toString().length() > 0 && txtHeartBeat.getText().toString().length() >0 && txtBodyTemperature.getText().toString().length() > 0){
                    xmlParser = new XMLParser(VitalSigns.this);

                    bodyTemperature = txtBodyTemperature.getText().toString();
                    heartRate = txtHeartBeat.getText().toString();
                    bloodPressure = txtBloodPressure.getText().toString();

                    URL += "bodyTemperature="+bodyTemperature+"&heartRate="+heartRate+"&bloodPressure="+bloodPressure+"&patientID="+patientID+"&nurseID="+nurseID;
                    xmlParser.execute(URL);
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
                        map.put(CHILD_NODE_LOG, xmlParser.getValue(e, CHILD_NODE_LOG));
                    }
                    Toast.makeText(VitalSigns.this,map.get(CHILD_NODE_LOG),Toast.LENGTH_SHORT).show();
                    txtBodyTemperature.setText("");
                    txtHeartBeat.setText("");
                    txtBloodPressure.setText("");
                }else{
                    Toast.makeText(VitalSigns.this,"Please enter all the values",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
