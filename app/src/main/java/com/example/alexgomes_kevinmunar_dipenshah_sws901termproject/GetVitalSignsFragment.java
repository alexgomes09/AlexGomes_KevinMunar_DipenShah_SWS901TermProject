package com.example.alexgomes_kevinmunar_dipenshah_sws901termproject;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class GetVitalSignsFragment extends Fragment {

    Button btnUpdate;
    TextView txtBodyTemperature,txtHeartRate,txtBloodPressure;
    Spinner choosePatientSpinner;
    String URL = "http://lalaskinessentials.com/system_info/getVitalSigns.php?";
    XMLParser xmlParser;
    final String PARENT_NODE = "vitalSign";
    final String CHILDNODE_BT= "bodyTemperature";
    final String CHILDNODE_HR= "heartRate";
    final String CHILDNODE_BP= "bloodPressure";
    final String CHILDNODE_NID= "nurseID";
    final String PARENT_NODE_PATIENTNAME = "username";
    final String CHILDNODE_LOGINID = "loginID";

    Document doc;
    NodeList nl;

    public GetVitalSignsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get_vital_signs, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        txtBodyTemperature = (TextView) getActivity().findViewById(R.id.txtBodyTemperature);
        txtHeartRate = (TextView) getActivity().findViewById(R.id.txtHeartRate);
        txtBloodPressure = (TextView) getActivity().findViewById(R.id.txtBloodPressure);
        choosePatientSpinner = (Spinner) getActivity().findViewById(R.id.choosePatientSpinner);

        btnUpdate = (Button)getActivity().findViewById(R.id.btnUpdate);

        xmlParser = new XMLParser(getActivity().getApplicationContext());

        String getAllPatient = "http://lalaskinessentials.com/system_info/getAllPatient_Nurse.php?usertype=1";
        xmlParser.execute(getAllPatient);
        String result = null;
        try {
            result = xmlParser.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        doc = xmlParser.getDomElement(result);
        nl = doc.getElementsByTagName(PARENT_NODE_PATIENTNAME);

        final HashMap<String, String> patietnHashMap = new HashMap<String, String>();
        final ArrayAdapter<String> patientNameAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item);
        for (int i = 0; i < nl.getLength(); i++) {
            Element e = (Element)nl.item(i);
            patietnHashMap.put(CHILDNODE_LOGINID,xmlParser.getValue(e,CHILDNODE_LOGINID));
            patientNameAdapter.add(patietnHashMap.get(CHILDNODE_LOGINID));
            choosePatientSpinner.setAdapter(patientNameAdapter);
            patientNameAdapter.notifyDataSetChanged();
        }
//        choosePatientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                txtBodyTemperature.setText(patietnHashMap.get(CHILDNODE_BT));
//                txtHeartRate.setText(patietnHashMap.get(CHILDNODE_HR));
//                txtBloodPressure.setText(patietnHashMap.get(CHILDNODE_BP));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
    }
}
