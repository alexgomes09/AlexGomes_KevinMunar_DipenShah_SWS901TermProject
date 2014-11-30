package com.example.alexgomes_kevinmunar_dipenshah_sws901termproject;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class EnterVitalSignsFragment extends Fragment {

    TextView txtBodyTemperature,txtHeartBeat,txtBloodPressure;
    String bodyTemperature,heartRate,bloodPressure,nurseID,patientID,userType;
    Spinner patientOrNurseSpinner;
    Button btnAddVitalSigns,btnCancel;
    String URL = "http://lalaskinessentials.com/system_info/enterVitalSigns.php?";
    XMLParser xmlParser;
    final String PARENT_NODE = "vitalSign";
    final String CHILD_NODE_LOG= "log";
    final String PARENT_NODE_GETALLPATIENTORNURSE = "username";
    final String CHILD_NODE_LOGINID = "loginID";
    Document doc;
    NodeList nl;
    Intent intent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vital_signs,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();

        txtBodyTemperature = (TextView) getView().findViewById(R.id.bodyTemperature);
        txtHeartBeat = (TextView) getView().findViewById(R.id.heartRate);
        txtBloodPressure = (TextView) getView().findViewById(R.id.bloodPressure);
        patientOrNurseSpinner = (Spinner) getView().findViewById(R.id.patientOrNurseSpinner);
        btnAddVitalSigns = (Button)getView().findViewById(R.id.btnAddVitalSigns);

        intent = getActivity().getIntent();
        userType  = intent.getStringExtra("userType");

        //get all the patient or nurse and add them in spinner
        xmlParser = new XMLParser(getActivity().getApplicationContext());
        String getAllPatientOrNurse = "http://lalaskinessentials.com/system_info/getAllPatient_Nurse.php?usertype="+userType;
        xmlParser.execute(getAllPatientOrNurse);
        String result = null;
        try {
            result = xmlParser.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        doc = xmlParser.getDomElement(result);
        nl = doc.getElementsByTagName(PARENT_NODE_GETALLPATIENTORNURSE);
        
        LinkedHashMap<String,String> patientLinkedHashMap = new LinkedHashMap<String, String>();
        ArrayAdapter<String> patientNameAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item);
        
        for (int i = 0; i < nl.getLength(); i++) {
            Element e = (Element)nl.item(i);
            patientLinkedHashMap.put(CHILD_NODE_LOGINID, xmlParser.getValue(e, CHILD_NODE_LOGINID));
            patientNameAdapter.add(patientLinkedHashMap.get(CHILD_NODE_LOGINID));
            patientOrNurseSpinner.setAdapter(patientNameAdapter);
            patientNameAdapter.notifyDataSetChanged();
        }
        patientOrNurseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (userType.equals("1")) {
                    patientID = adapterView.getSelectedItem().toString(); // careful with the logic happening here. had to spent enough time fixing this part.
                    nurseID =  intent.getStringExtra("loginID");            // its getting opposite of what intent is sending. and setting them in patient iD and nurseID
                }else if(userType.equals("0")){
                    patientID = intent.getStringExtra("loginID");
                    nurseID = adapterView.getSelectedItem().toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //get patient name end here


        btnAddVitalSigns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtBloodPressure.getText().toString().length() > 0 && txtHeartBeat.getText().toString().length() >0 && txtBodyTemperature.getText().toString().length() > 0){
                    xmlParser = new XMLParser(getActivity().getApplicationContext());

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
                    Toast.makeText(getActivity().getApplicationContext(),map.get(CHILD_NODE_LOG),Toast.LENGTH_SHORT).show();
                    txtBodyTemperature.setText("");
                    txtHeartBeat.setText("");
                    txtBloodPressure.setText("");
                }else{
                    Toast.makeText(getActivity(),"Please enter all the values",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
