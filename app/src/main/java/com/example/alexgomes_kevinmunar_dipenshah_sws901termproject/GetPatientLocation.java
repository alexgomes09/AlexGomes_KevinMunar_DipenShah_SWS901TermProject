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


                ArrayList<String> patientLocationAdapters = new ArrayList<String>();
                ArrayList<String> patientLocationAdapters2 = new ArrayList<String>();
                ArrayList<String> patientLocationAdapters3 = new ArrayList<String>();
                for (int j = 0; j < nl.getLength(); j++) {
                    Element e2 = (Element)nl.item(j);

                    String lat = (xmlParser.getValue(e2,CHILD_NODE_LATITUDE));
                    String log = (xmlParser.getValue(e2, CHILD_NODE_LONGITUDE));
                    String curTime = (xmlParser.getValue(e2, CHILD_NODE_CURRENTTIME));

                    patientLocationAdapters.add(lat);
                    patientLocationAdapters2.add(log);
                    patientLocationAdapters3.add(curTime);
                }

                patientLocationAdapter = new PatientLocationAdapter(patientLocationAdapters,patientLocationAdapters2,patientLocationAdapters3);
                listOfPatientLocation.setAdapter(patientLocationAdapter);

                URL = "http://lalaskinessentials.com/system_info/getPatientLocation.php?patientID=";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    class PatientLocationAdapter extends BaseAdapter {

        TextView lblLocationName,lblLocationTime;
        ArrayList<String> patientLocationArrayList1,patientLocationArrayList2, patientLocationArrayList3;

        public PatientLocationAdapter(ArrayList<String> arrayList1,ArrayList<String> arrayList2, ArrayList<String> arrayList3){
            patientLocationArrayList1 = arrayList1;
            patientLocationArrayList2 = arrayList2;
            patientLocationArrayList3 = arrayList3;
        }

        @Override
        public int getCount() {
            return patientLocationArrayList1.size();
        }

        @Override
        public Object getItem(int i) {
            return patientLocationArrayList1.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater =  (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_list_patient_single_location,viewGroup,false);

            lblLocationName = (TextView)view.findViewById(R.id.locationName);
            lblLocationTime = (TextView)view.findViewById(R.id.locationTime);

            patientLocationAdapter.getItem(i);
            System.out.println(patientLocationArrayList1.get(i));
            System.out.println(patientLocationArrayList2.get(i));
            System.out.println(patientLocationArrayList3.get(i));

            lblLocationTime.setText(patientLocationArrayList3.get(i));

            return view;
        }
    }
}
