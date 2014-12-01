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

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class GetVitalSignsFragment extends Fragment {

    PatientInfoListView patientInfoListView;
    ListView listPatientInfo;
    Spinner choosePatientSpinner;
    String URL = "http://lalaskinessentials.com/system_info/getVitalSigns.php?patientID=";
    XMLParser xmlParser;
    final String PARENT_NODE = "vitalSign";
    final String CHILDNODE_BT= "bodyTemperature";
    final String CHILDNODE_HR= "heartRate";
    final String CHILDNODE_BP= "bloodPressure";
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

        listPatientInfo = (ListView)getActivity().findViewById(R.id.listPatientInfo);
        choosePatientSpinner = (Spinner)getActivity().findViewById(R.id.choosePatientSpinner);
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
                nl = doc.getElementsByTagName(PARENT_NODE);

                HashMap<String,String> map = new HashMap<String, String>();
                for (int j = 0; j < nl.getLength(); j++) {
                    Element e = (Element)nl.item(j);
                    map.put(CHILDNODE_BP,xmlParser.getValue(e,CHILDNODE_BP));
                    map.put(CHILDNODE_HR,xmlParser.getValue(e,CHILDNODE_HR));
                    map.put(CHILDNODE_BT, xmlParser.getValue(e, CHILDNODE_BT));
                }
                patientInfoListView = new PatientInfoListView(getActivity().getApplicationContext(),map);
                listPatientInfo.setAdapter(patientInfoListView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    
    class PatientInfoListView extends BaseAdapter{

        Context context;
        String bloodPressure,heartRate,bodyTemperature;
        TextView lblBloodPressure,lblHeartRate,lblBodyTemperature;
        HashMap<String,String> map = new HashMap<String, String>();
        public PatientInfoListView(Context c,HashMap<String,String> hashMap){
            context = c;
            map = hashMap;
            bloodPressure = map.get(CHILDNODE_BP);
            heartRate = map.get(CHILDNODE_HR);
            bodyTemperature = map.get(CHILDNODE_BT);
        }
        @Override
        public int getCount() {
            return map.size();
        }

        @Override
        public Object getItem(int i) {
            return map.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_list_getvitalsigns,null);

            lblBloodPressure = (TextView)view.findViewById(R.id.lblBloodPressure);
            lblHeartRate = (TextView)view.findViewById(R.id.lblHeartRate);
            lblBodyTemperature= (TextView)view.findViewById(R.id.lblBodyTemperature);

            lblBloodPressure.setText(bloodPressure);
            lblHeartRate.setText(heartRate);
            lblBodyTemperature.setText(bodyTemperature);
            return view;
        }
    }
}
