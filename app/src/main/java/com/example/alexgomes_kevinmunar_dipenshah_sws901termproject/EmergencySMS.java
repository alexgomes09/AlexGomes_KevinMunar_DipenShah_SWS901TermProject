package com.example.alexgomes_kevinmunar_dipenshah_sws901termproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class EmergencySMS extends Activity {
    private final int PICK_CONTACT = 1;
    public String name = "";
    public String stringNumber = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_sms);
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
        TextView sItems = (TextView) findViewById(R.id.textView);
        sItems.setText("Send Emergency Response SMS to " + name + stringNumber);

        final EditText texttoSend = (EditText) findViewById(R.id.textToSend);
        Button sendBtn = (Button) findViewById(R.id.btnSendEmergencyResponse);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendSMSMessage(texttoSend.getText().toString());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.emergency_sm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c =  getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToNext()) {
                        name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        int columnIndex_ID = c.getColumnIndex(ContactsContract.Contacts._ID);
                        String contactID = c.getString(columnIndex_ID);
                        int columnIndex_HASPHONENUMBER = c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                        String stringHasPhoneNumber = c.getString(columnIndex_HASPHONENUMBER);

                        if(stringHasPhoneNumber.equalsIgnoreCase("1")){
                            Cursor cursorNum = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactID,
                                    null,
                                    null);

                            //Get the first phone number
                            if(cursorNum.moveToNext()){
                                int columnIndex_number = cursorNum.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                                stringNumber = cursorNum.getString(columnIndex_number);
                                TextView sItems = (TextView) findViewById(R.id.textView);
                                sItems.setText("Send Emergency Response SMS to " + name + " - (" + stringNumber + ")");
                            }

                        }else{
                            Toast.makeText(getApplicationContext(), "No number found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }

    public void sendSMSMessage(String message) {
        Log.i("Send SMS", "");



        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(stringNumber, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS failed, please try again.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
