package com.example.alexgomes_kevinmunar_dipenshah_sws901termproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

/**
 * Created by irock4u79 on 12/1/2014.
 */
public class AutoStart extends BroadcastReceiver {


    @Override
    public void onReceive(final Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, MyLocation.class);
        context.startService(startServiceIntent);
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(context);
        final String loginID = shared.getString("loginID", "");


        MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                XMLParser xmlParser = new XMLParser(context);
                String latitude = String.valueOf(location.getLatitude());
                String longitude = String.valueOf(location.getLongitude());
                String URL = "http://lalaskinessentials.com/system_info/enterPatientLocaltion.php?";
                URL += "latitude=" + latitude + "&longitude=" + longitude + "&loginID=" + loginID;
                xmlParser.execute(URL);
            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(context, locationResult);

    }
}
