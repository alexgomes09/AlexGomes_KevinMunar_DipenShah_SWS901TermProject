package com.example.alexgomes_kevinmunar_dipenshah_sws901termproject;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class GetPatientLocation extends Fragment {

    PatientLocationAdapter patientLocationAdapter;
    Spinner choosePatientSpinner;
    ListView listOfPatientLocation;
    XMLParser xmlParser;
    Document doc;
    NodeList nl;
    String URL = "http://lalaskinessentials.com/system_info/getPatientLocation.php?patientID=";
    String PARENT_NODE_PATIENTLOCATION = "patientLocation";
    String CHILD_NODE_LATITUDE = "latitude";
    String CHILD_NODE_LONGITUDE = "longitude";
    String CHILD_NODE_CURRENTTIME = "currenttime";
    final String PARENT_NODE_PATIENTNAME = "username";
    final String CHILDNODE_LOGINID = "loginID";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get_patient_location, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        listOfPatientLocation = (ListView) getActivity().findViewById(R.id.listOfPatientLocation);
        choosePatientSpinner = (Spinner) getActivity().findViewById(R.id.choosePatientSpinner);
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
        final ArrayAdapter<String> patientNameAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item);
        for (int i = 0; i < nl.getLength(); i++) {
            Element e = (Element) nl.item(i);
            patietnHashMap.put(CHILDNODE_LOGINID, xmlParser.getValue(e, CHILDNODE_LOGINID));
            patientNameAdapter.add(patietnHashMap.get(CHILDNODE_LOGINID));
            patientNameAdapter.notifyDataSetChanged();
        }
        choosePatientSpinner.setAdapter(patientNameAdapter);

        choosePatientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                xmlParser = new XMLParser(getActivity().getApplicationContext());
                URL +=adapterView.getSelectedItem().toString();
                xmlParser.execute(URL);
                String result = null;

                try{
                    result = xmlParser.get();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }catch (ExecutionException e){
                    e.printStackTrace();
                }

                doc = xmlParser.getDomElement(result);
                nl = doc.getElementsByTagName(PARENT_NODE_PATIENTLOCATION);

                patientLocationAdapter = new PatientLocationAdapter();
                ArrayList<PatientLocationAdapter> patientLocationAdapters = new ArrayList<PatientLocationAdapter>();

                for (int j = 0; j < nl.getLength(); j++) {
                    Element e2 = (Element)nl.item(j);
                    patientLocationAdapter.setLatitude(xmlParser.getValue(e2,CHILD_NODE_LATITUDE));
                    patientLocationAdapter.setLongitude(xmlParser.getValue(e2, CHILD_NODE_LONGITUDE));
                    patientLocationAdapter.setCurrentTime(xmlParser.getValue(e2, CHILD_NODE_CURRENTTIME));

                    patientLocationAdapters.add(patientLocationAdapter);

                }
                patientLocationAdapter = new PatientLocationAdapter(patientLocationAdapters);
                System.out.println(patientLocationAdapter.currentTime);
                listOfPatientLocation.setAdapter(patientLocationAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    class PatientLocationAdapter extends BaseAdapter {

        TextView lblLocationName,lblLocationTime;

        ArrayList<PatientLocationAdapter> patientLocationArrayList;
        private String latitude;
        private String currentTime;
        private String longitude;

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public void setCurrentTime(String currentTime) {
            this.currentTime = currentTime;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public PatientLocationAdapter(String latitude, String longitude, String currentTime) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.currentTime = currentTime;
        }
        public PatientLocationAdapter(ArrayList<PatientLocationAdapter> arrayList){
            patientLocationArrayList = arrayList;
        }

        public PatientLocationAdapter(){

        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater =  (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fragment_get_patient_location,null);

            lblLocationName = (TextView)getActivity().findViewById(R.id.locationName);
            lblLocationTime = (TextView)getActivity().findViewById(R.id.locationTime);

            lblLocationTime.setText(patientLocationArrayList.get(i).currentTime.toString());

            return view;
        }
    }
}
