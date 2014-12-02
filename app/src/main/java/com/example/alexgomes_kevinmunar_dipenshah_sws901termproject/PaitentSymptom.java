package com.example.alexgomes_kevinmunar_dipenshah_sws901termproject;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PaitentSymptom extends Fragment {

    TextView lblMedicalCondition;
    Button btnCheckCondition;
    String symptom1, symptom2;
    Spinner spinnerSymptom1,spinnerSymptom2;
    String URL= "http://lalaskinessentials.com/system_info/getAllSymptom.php";
    String medicalConditionURL = "http://lalaskinessentials.com/system_info/getDiseaseSymptoms.php?";
    XMLParser xmlParser;
    String PARENT_NODE = "symptomName";
    String CHILD_NODE = "symptom";
    String PARENT_NODE_MEDICALCONDITION = "condition";
    String CHILD_NODE_MEDICALCONDITION = "medicalConditionName";
    Document doc;
    NodeList nl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_paitent_symptom, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        xmlParser = new XMLParser(getActivity().getApplicationContext());
        spinnerSymptom1 = (Spinner)getActivity().findViewById(R.id.spinnerSymptom1);
        spinnerSymptom2 = (Spinner)getActivity().findViewById(R.id.spinnerSymptom2);
        lblMedicalCondition = (TextView)getActivity().findViewById(R.id.lblMedicalCondition);
        btnCheckCondition = (Button) getActivity().findViewById(R.id.btnCheckCondition);

        xmlParser.execute(URL);
        String result = null;
        try{
            result = xmlParser.get();
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }

        doc = xmlParser.getDomElement(result);
        nl = doc.getElementsByTagName(PARENT_NODE);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item);

        for (int i = 0; i < nl.getLength(); i++) {
            Element e = (Element)nl.item(i);
            spinnerAdapter.add(xmlParser.getValue(e,CHILD_NODE));
        }
        spinnerAdapter.notifyDataSetChanged();
        spinnerSymptom1.setAdapter(spinnerAdapter);
        spinnerSymptom2.setAdapter(spinnerAdapter);

        spinnerSymptom1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                symptom1 = adapterView.getSelectedItem().toString();
                symptom1 = symptom1.replace(" ","%20");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerSymptom2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                symptom2 = adapterView.getSelectedItem().toString();
                symptom2 = symptom2.replace(" ", "%20");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnCheckCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                xmlParser = new XMLParser(getActivity().getApplicationContext());
                medicalConditionURL += "symptom1="+symptom1+"&symptom2="+symptom2;
                xmlParser.execute(medicalConditionURL);
                String result = null;

                try{
                    result = xmlParser.get();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }catch (ExecutionException e){
                    e.printStackTrace();
                }

                doc = xmlParser.getDomElement(result);
                nl = doc.getElementsByTagName(PARENT_NODE_MEDICALCONDITION);

                ArrayList<String> medList = new ArrayList<String>();

                for (int j = 0; j < nl.getLength(); j++) {
                    Element e = (Element)nl.item(j);
                    medList.add(xmlParser.getValue(e, CHILD_NODE_MEDICALCONDITION));
                }
                if(medList.size() <= 1){
                    lblMedicalCondition.setText(medList.get(0));
                }else{
                    lblMedicalCondition.setText(medList.get(0)+" or "+medList.get(1));
                }

                medicalConditionURL = "http://lalaskinessentials.com/system_info/getDiseaseSymptoms.php?";
            }
        });


    }



}
