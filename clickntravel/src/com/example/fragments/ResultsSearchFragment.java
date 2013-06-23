package com.example.fragments;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;

import com.example.api.ApiIntent;
import com.example.api.ApiResultReceiver;
import com.example.api.Callback;
import com.example.clickntravel.R;
import com.example.handlers.FragmentHandler;
import com.example.utils.City;
import com.example.utils.Deal;
import com.example.utils.DealAdapter;
import com.example.utils.FlightsDbAdapter;
import com.example.utils.FragmentKey;

public class ResultsSearchFragment extends Fragment {

	private final String fileName = "dealsStorage";
	// private final String preferencesFileName = "favoritesPreferencesStorage";

	// private Map<String, Deal> dealsMap;

	private List<String> dealPrices = new ArrayList<String>();
	private static List<Deal> dealsList = new ArrayList<Deal>();

	// Esto está hardcodeado a que from sea Buenos Aires TODO
	private String nameFrom;
	private String idFrom = "BUE";
	private String nameTo;
	private String idTo;

	private DealAdapter adapter;
	private Deal currentDeal;

	private ListView mListView;
	private FlightsDbAdapter mDbHelper;
	
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ActionBar actionBar = getActivity().getActionBar();

		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.resultssearch_abs_layout);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		Bundle arguments = getArguments();

		idTo = arguments.getString("cityId");
		nameTo = arguments.getString("cityName");

		createDeals();

		if (view == null)
			view = inflater.inflate(R.layout.deal_list_fragment, container,
					false);

		mListView = (ListView) view.findViewById(R.id.deals_list_view);
		ListView listView = (ListView) view.findViewById(R.id.deals_list_view);
//		mListView = (ListView) this.getActivity().findViewById(R.id.list);
		
//		try {
//			retrieveData();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//
//		adapter = new DealAdapter(getActivity(), dealsList);
//
//		listView.setAdapter(adapter); 
//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
////				currentFlight = flightList.get(arg2);
////				int screenLayout = getActivity().getResources().getConfiguration().screenLayout;
////				if ((screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE){
////					((MainActivity)getActivity()).goToNewFavoriteInfoFragmentLarge(currentFlight);
////				} else {
////					getActivity().getActionBar().selectTab(null);
////					((MainActivity)getActivity()).goToNewFavoriteInfoFragment(currentFlight);
////				}
//			}
//		});
//
		
        setHasOptionsMenu(true);
		
        mDbHelper = new FlightsDbAdapter(this.getActivity());
		mDbHelper.open();

		mDbHelper.deleteAllFlights();
//
//		createCities();

//		return inflater.inflate(R.layout.results_search_fragment, container, false);
		
		return view;
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

//		inflater.inflate(R.menu.searchview_in_menu, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
//		mSearchView = (SearchView) searchItem.getActionView();
//		mSearchView.setIconifiedByDefault(false); // Pone la lupita a la derecha
//													// si se comenta
//		mSearchView.setOnQueryTextListener((OnQueryTextListener) this);
//		mSearchView.setOnCloseListener((OnCloseListener) this);
//		mListView = (ListView) this.getActivity().findViewById(R.id.list);

		// Setea el color del textito de la search view
		String text = "<font color = #DDDDDD>"
				+ getString(R.string.search_view_text) + "</font>";
//		mSearchView.setQueryHint(Html.fromHtml(text));
	}

	public void createDeals() {

		Callback callback = new Callback() {

			public void handleResponse(JSONObject response) {

				try {

					JSONArray dealArray = response.getJSONArray("deals");

					for (int i = 0; i < dealArray.length(); i++) {

						String price = dealArray.getJSONObject(i).optString(
								"price");
						String id = dealArray.getJSONObject(i).optString(
								"cityId");

						getDealPrices(id, price);
					}

					getFlights();

				} catch (JSONException e) {
				}
			}

			private void getDealPrices(String id, String price) {

				if (id.equals(idTo)) {

					dealPrices.add(price);
				}
			}

			private void getFlights() {

				Callback callback = new Callback() {

					public void handleResponse(JSONObject response) {

						try {

							JSONArray dealArray = response
									.getJSONArray("flights");

							String minPrice = null;
							int indexMinPrice = 0;

							for (int i = 0; i < dealArray.length(); i++) {

								String price = dealArray.getJSONObject(i)
										.getJSONObject("price")
										.getJSONObject("total")
										.optString("total");

								if (dealPrices.contains(price)) {

									addDeal(indexMinPrice, minPrice, dealArray);

								} else if (minPrice == null
										|| Double.valueOf(minPrice) > Double
												.valueOf(price)) {

									minPrice = price;
									indexMinPrice = i;
								}
							}

							if (dealsList.isEmpty()) {

								addDeal(indexMinPrice, minPrice, dealArray);
							}

						} catch (JSONException e) {
						}
						
						showResults(nameTo + "*");
					}

					private void addDeal(int index, String price,
							JSONArray dealArray) {

						try {

							JSONObject obj = dealArray.getJSONObject(index);
							JSONObject segments = obj
									.getJSONArray("outboundRoutes")
									.getJSONObject(0).getJSONArray("segments")
									.getJSONObject(0);

							String airlineId = segments.optString("airlineId");
							String flightId = segments.optString("flightId");
							String flightNumber = segments
									.optString("flightNumber");
							String depTime = segments
									.getJSONObject("departure").optString(
											"date");
							String arrivalTime = segments.getJSONObject(
									"arrival").optString("date");
							;

							nameFrom = segments.getJSONObject("departure")
									.optString("cityName");

							Deal curr = new Deal(idFrom, nameFrom, idTo,
									nameTo, price, airlineId, flightId,
									flightNumber, depTime, arrivalTime);

							dealsList.add(curr);

							mDbHelper.createFlights("", "", nameTo, "", "");
							Log.d("deal", curr.toString());

						} catch (JSONException e) {
						}
					}
				};

				ApiResultReceiver receiver = new ApiResultReceiver(
						new Handler(), callback);
				ApiIntent intent = new ApiIntent("GetOneWayFlights", "Booking",
						receiver, getActivity());

				List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

				nameValuePair.add(new BasicNameValuePair("from", idFrom));
				nameValuePair.add(new BasicNameValuePair("to", idTo));
				nameValuePair
						.add(new BasicNameValuePair("dep_date", getDate()));
				nameValuePair.add(new BasicNameValuePair("adults", "1"));
				nameValuePair.add(new BasicNameValuePair("children", "0"));
				nameValuePair.add(new BasicNameValuePair("infants", "0"));

				intent.setParams(nameValuePair);

				getActivity().startService(intent);
			}

			private String getDate() {

				Date date = new Date();

				Format formatter = new SimpleDateFormat("yyyy-MM-dddd");
				SimpleDateFormat simpleFormatter = new SimpleDateFormat(
						"yyyy-MM-dd");

				String dateString = formatter.format(date);
				Calendar calendar = Calendar.getInstance();

				try {
					calendar.setTime(simpleFormatter.parse(dateString));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				calendar.add(Calendar.DATE, 2); // number of days to add
				dateString = simpleFormatter.format(calendar.getTime());

				return dateString;
			}
		};

		ApiResultReceiver receiver = new ApiResultReceiver(new Handler(),
				callback);
		ApiIntent intent = new ApiIntent("GetFlightDeals2", "Booking",
				receiver, this.getActivity());

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

		nameValuePair.add(new BasicNameValuePair("from", idFrom));

		intent.setParams(nameValuePair);

		this.getActivity().startService(intent);
	}
	
	public boolean onQueryTextChange(String newText) {

		showResults(newText + "*");

		return false;
	}

	public boolean onQueryTextSubmit(String query) {

		showResults(query + "*");

		return false;
	}

	private void showResults(String query) {

		query = "a*";
		
		Log.d("probando", query);
		
		if (query == null) {

			return;
		}

		Cursor cursor = mDbHelper.searchFlights(query);
		
		if (cursor != null) {
		
			Log.d("cursor", String.valueOf(cursor.getCount()));
			
			// Specify the columns we want to display in the result
			String[] from = new String[] { FlightsDbAdapter.KEY_TO };

			// Specify the Corresponding layout elements where we want the
			// columns to go
			int[] to = new int[] { R.id.scity };

			// Create a simple cursor adapter for the definitions and apply them
			// to the ListView
			@SuppressWarnings("deprecation")
			SimpleCursorAdapter Flights = new SimpleCursorAdapter(this.getActivity(),
					R.layout.flightresult, cursor, from, to);
			mListView.setAdapter(Flights);

			Log.d("listview", mListView.toString());
			// Define the on-click listener for the list items
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
					// Get the cursor, positioned to the corresponding row in
					// the result set
					Cursor cursor = (Cursor) mListView
							.getItemAtPosition(position);

					// Get the city from this row in the database
					String name = cursor.getString(cursor
							.getColumnIndexOrThrow("city"));
					
//					Deal deal = citiesMap.get(name);
//
//					Bundle resultSearchBundle = new Bundle();
//					resultSearchBundle.putString("cityId", city.getId());
//					resultSearchBundle.putString("cityName", city.getName());
//
					getActivity();
					
					FragmentHandler fragmentHandler = new FragmentHandler(getFragmentManager());
					
//					fragmentHandler.setFragment(FragmentKey.SEARCH_DEALS_LIST,
//							resultSearchBundle);
				}
			});
		}
	}
	
	private String getElementString(int elementId) {
		
		return ((TextView) getActivity().findViewById(elementId)).getText()
				.toString();
	}

	private void storeOnSharedPreferences(JSONObject favorite, String uniqueKey) {

		SharedPreferences prefs = getActivity().getSharedPreferences(fileName,
				Context.MODE_PRIVATE);

		Editor editor = prefs.edit();

		editor.putString(uniqueKey, favorite.toString()).commit();
	}

	@SuppressWarnings("unchecked")
	private void retrieveData() throws JSONException {

		ResultsSearchFragment.dealsList = new ArrayList<Deal>();

		SharedPreferences prefs = getActivity().getSharedPreferences(fileName,
				Context.MODE_PRIVATE);

		Map<String, String> map = (Map<String, String>) prefs.getAll();

		for (String s : map.values()) {

			dealsList.add(new Deal(new JSONObject(s)));
		}

		// prefs = getActivity().getSharedPreferences(preferencesFileName,
		// Context.MODE_PRIVATE);
		// map = (Map<String,String>)prefs.getAll();
	}

	@Override
	public void onDestroyView() {

		if (view != null) {

			((ViewGroup) view.getParent()).removeAllViews();
		}

		ListView lv = (ListView) getActivity().findViewById(
				R.id.deals_list_view);

		if (lv != null) {

			((ViewGroup) lv.getParent()).removeView(lv);
			lv.removeAllViews();
		}

		super.onDestroyView();
	}

	private void eraseField(int fieldId) {

		TextView tv = (TextView) getActivity().findViewById(fieldId);

		tv.setText("");
	}

	public Deal getCurrentFlight() {

		return currentDeal;
	}
}
