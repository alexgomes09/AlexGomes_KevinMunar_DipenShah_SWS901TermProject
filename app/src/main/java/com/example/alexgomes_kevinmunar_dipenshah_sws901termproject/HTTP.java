package com.example.alexgomes_kevinmunar_dipenshah_sws901termproject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Alex on 11/27/2014.
 */
public class HTTP {

    Context context;

    public  HTTP(){
        new DownloadTextTask()
                .execute("http://www.webservicex.net/CurrencyConvertor.asmx/ConversionRate?FromCurrency=EUR&ToCurrency=USD");
    }


    private String DownloadText(String URL) {
        int BUFFER_SIZE = 2000;
        InputStream in = null;
        try {
            in = OpenHttpGETConnection(URL);
            //in = OpenHttpPOSTConnection(URL);
        } catch (Exception e) {
            Log.d("Networking", e.getLocalizedMessage());
            return "";
        }

        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
        String str = "";
        char[] inputBuffer = new char[BUFFER_SIZE];
        try {
            while ((charRead = isr.read(inputBuffer)) > 0) {
                // ---convert the chars to a String---
                String readString = String
                        .copyValueOf(inputBuffer, 0, charRead);
                str += readString;
                inputBuffer = new char[BUFFER_SIZE];
            }
            in.close();
        } catch (IOException e) {
            Log.d("Networking", e.getLocalizedMessage());
            return "";
        }
        return str;
    }

    // ---Connects using HTTP GET---
    public static InputStream OpenHttpGETConnection(String url) {
        InputStream inputStream = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            inputStream = httpResponse.getEntity().getContent();
        } catch (Exception e) {
            Log.d("", e.getLocalizedMessage());
        }
        return inputStream;
    }

    private class DownloadTextTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            return DownloadText(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            Log.d("DownloadTextTask", result);
        }
    }
}
