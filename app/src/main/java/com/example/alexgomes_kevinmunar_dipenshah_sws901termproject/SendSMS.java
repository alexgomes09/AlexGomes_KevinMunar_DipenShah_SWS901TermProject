package com.example.alexgomes_kevinmunar_dipenshah_sws901termproject;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.view.Menu;

public class SendSMS extends Fragment {

    SmsManager smsManager;
    Button sendbtn;
    EditText phoneno;
    EditText smsmsg;


    @Override
    public void onStart() {
        super.onStart();
        sendbtn = (Button) getView().findViewById(R.id.sendbtn);
        phoneno = (EditText) getView().findViewById(R.id.phoneet);
        smsmsg = (EditText) getView().findViewById(R.id.smset);

        sendbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                sendSMSMessage();
            }
        });
    }

    public void sendSMSMessage(){
        String phonenum = phoneno.getText().toString();
        String message = smsmsg.getText().toString();

        try{
            smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phonenum,null,message,null,null);
            Toast.makeText(getActivity(),"SMS Sent", Toast.LENGTH_LONG).show();
        }catch(Exception e){
            Toast.makeText(getActivity(),"Sms failed to send, try again later", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_sm, container, false);
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
