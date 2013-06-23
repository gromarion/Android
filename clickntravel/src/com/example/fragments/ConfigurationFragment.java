package com.example.fragments;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.clickntravel.MainActivity;
import com.example.clickntravel.R;

public class ConfigurationFragment extends PreferenceFragment {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
        addPreferencesFromResource(R.xml.settings);
    }
    
}