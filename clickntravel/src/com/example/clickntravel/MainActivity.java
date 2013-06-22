package com.example.clickntravel;

import android.R.bool;
import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.fragments.ConfigurationFragment;
import com.example.fragments.FlightInfoFragment;
import com.example.fragments.MyFlightsFragment;
import com.example.handlers.FragmentHandler;
import com.example.utils.AddedFlight;
import com.example.utils.FragmentKey;

public class MainActivity extends FragmentActivity {

	private ConfigurationFragment mPrefsFragment = null;
	private FragmentHandler fragmentHandler;

	MenuItem remove;
	MenuItem comment;
	MenuItem seeComments;
	MenuItem configuration;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ColorDrawable actionBarBackground = new ColorDrawable(Color.rgb(12,
				129, 199));
		ActionBar actionBar = getActionBar();

		setContentView(R.layout.activity_main);
		this.fragmentHandler = new FragmentHandler(getSupportFragmentManager());
		this.fragmentHandler.setFragment(FragmentKey.MAIN);
		actionBar.setBackgroundDrawable(actionBarBackground);
		actionBar.setIcon(R.drawable.back);
	}

	public void onClickMyFlights(View view) {
		this.fragmentHandler.setFragment(FragmentKey.MY_FLIGHTS);
	}

	public void onClickAboutUs(View view) {
		this.fragmentHandler.setFragment(FragmentKey.ABOUT_US);
	}

	public void onClickMyDeals(View view) {
		this.fragmentHandler.setFragment(FragmentKey.MY_DEALS);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mPrefsFragment != null) {
			// mPrefsFragment.getPreferenceScreen().removeAll();
			// mPrefsFragment = null;
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}

	public void goToNewFavoriteInfoFragment(AddedFlight f) {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, new FlightInfoFragment(f))
				.addToBackStack(null).commit();
	}

	public void goToNewFavoriteInfoFragmentLarge(AddedFlight f) {
		// checkear que esto este bien, el container
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, new FlightInfoFragment(f))
				.addToBackStack(null).commit();
	}

	public void addFlight(View view) {
		((MyFlightsFragment) fragmentHandler
				.getFragment(FragmentKey.MY_FLIGHTS)).addFlight(view);
	}

	public FragmentHandler getFragmentHandler() {
		return fragmentHandler;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		createMenu(menu);
		return true;
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return menuSelection(item);
	};

	private boolean menuSelection(MenuItem item) {
		switch (item.getItemId()) {
			case 0:
				Toast.makeText(this, "removido mami", Toast.LENGTH_SHORT).show();
				return true;
			case 1:
				Toast.makeText(this, "comentan2", Toast.LENGTH_SHORT).show();
				return true;
			case 2:
				Toast.makeText(this, "v13nd0 c0m3nt4r10z", Toast.LENGTH_SHORT)
						.show();
				return true;
			case 3:
				mPrefsFragment = new ConfigurationFragment();
				FragmentManager mFragmentManager = getFragmentManager();
				FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
				mFragmentTransaction.replace(android.R.id.content, mPrefsFragment);
				mFragmentTransaction.addToBackStack(null).commit();
				this.fragmentHandler.setFragment(FragmentKey.BASE);
				return true;
			case android.R.id.home:
				Intent intent = new Intent(this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void createMenu(Menu menu) {
		remove = menu.add(0, 0, 0, "remove");
		comment = menu.add(0, 1, 1, "comment");
		seeComments = menu.add(0, 2, 2, "see comments");
		configuration = menu.add(0, 3, 3, R.string.main_button_configuration);

		remove.setIcon(R.drawable.remove);
		comment.setIcon(R.drawable.add_comment);
		seeComments.setIcon(R.drawable.comments);
		
		remove.setVisible(false);
		comment.setVisible(false);
		seeComments.setVisible(false);
		configuration.setVisible(true);

		remove.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		comment.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		seeComments.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		configuration.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
	}
	
	public void showDetailOptions(){
		remove.setVisible(true);
		comment.setVisible(true);
		seeComments.setVisible(true);
		configuration.setVisible(true);
	}

}
