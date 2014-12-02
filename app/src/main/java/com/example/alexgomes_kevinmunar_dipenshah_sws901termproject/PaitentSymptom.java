package com.example.alexgomes_kevinmunar_dipenshah_sws901termproject;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PaitentSymptom extends Fragment {

    public PaitentSymptom() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_paitent_symptom, container, false);
    }


}
