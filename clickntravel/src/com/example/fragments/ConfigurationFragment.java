package com.example.fragments;

import com.example.clickntravel.R;

import android.support.v4.app.Fragment;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ConfigurationFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ActionBar actionBar = getActivity().getActionBar();
		
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		actionBar.setCustomView(R.layout.configuration_abs_layout);
		
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(R.string.main_button_configuration);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
		return inflater.inflate(R.layout.configuration_fragment, container, false);
    }

}
