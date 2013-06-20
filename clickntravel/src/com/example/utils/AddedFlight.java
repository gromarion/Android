package com.example.utils;


import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class AddedFlight{

	private Destination departure;
	private Destination arrival;
	private Airline airline;
	private int flightId;
	private int flightNumber;
	private String status;
	
//	private NotificationConfiguration config;

	public AddedFlight(JSONObject status) throws JSONException {

		this.flightId = status.getInt("flightId"); 
		this.flightNumber = status.getInt("number");
		this.status = status.getString("status");
		this.departure = getDestination(status.getJSONObject("departure"));
		this.arrival = getDestination(status.getJSONObject("arrival"));
		this.airline = getAirline(status.getJSONObject("airline"));
//		setConfig(new NotificationConfiguration());
		
	}
	
	public AddedFlight(Destination de, Destination ar, Airline ai) {
		departure= de;
		arrival= ar;
		airline= ai;
		flightId= 123;
		flightNumber= flightId;
		status= "puto";
	}

	private Destination getDestination(JSONObject destiny) throws JSONException {
		
		JSONObject airport = destiny.getJSONObject("airport");
		JSONObject city = destiny.getJSONObject("city");
		JSONObject country = destiny.getJSONObject("country");

		return new Destination(airport.getString("id"),
						   airport.getString("description"),
						   airport.getString("terminal"), 
						   airport.getString("gate"),
						   city.getString("name"), 
						   country.getString("name"),
						   destiny.getString("scheduledTime"),
						   destiny.getString("scheduledGateTime"),
						   destiny.getString("actualGateTime"),
						   destiny.getString("estimateRunwayTime"),
						   destiny.getString("actualRunwayTime"));
	}
	
	private Airline getAirline(JSONObject airline) throws JSONException{
		return new Airline(airline.getString("id"),
						   airline.getString("name"),
						   airline.getString("logo")
				);
	}
	
	public Destination getDeparture() {
		return departure;
	}
	
	public Destination getArrival() {
		return arrival;
	}
	
	public Airline getAirline() {
		return airline;
	}
	
	public int getFlightId() {
		return flightId;
	}
	
	public int getFlightNumber() {
		return flightNumber;
	}
	
	public String getFlightStatus() {
		return status;
	}
	
//	public void setNotificationConfiguration(JSONObject jsonPrefs) throws JSONException {
//		config.setNotifyOnStatusChanged(jsonPrefs.getBoolean("status"));
//		config.setNotifyOnTerminalChanged(jsonPrefs.getBoolean("terminal"));
//		config.setNotifyOnGateChanged(jsonPrefs.getBoolean("gate"));
//		config.setNotifyOnScheduledTimeChanged(jsonPrefs.getBoolean("time"));
//	}
//	
//	public JSONObject generateJsonPreferences() throws JSONException {
//		JSONObject jsonPrefs = new JSONObject();
//		jsonPrefs.put("status", String.valueOf(config.isNotifyOnStatusChanged()));
//		jsonPrefs.put("gate", String.valueOf(config.isNotifyOnGateChanged()));
//		jsonPrefs.put("terminal", String.valueOf(config.isNotifyOnTerminalChanged()));
//		jsonPrefs.put("time", String.valueOf(config.isNotifyOnScheduledTimeChanged()));
//		return jsonPrefs;
//	}
	
	
	@Override
	public boolean equals(Object o) {
		if (o.getClass() != AddedFlight.class)
			return false;
		return ((AddedFlight)o).flightId == this.flightId;
	}
	
	public String getKey() {
		return String.valueOf(flightId) + airline.getId();
	}

//	public NotificationConfiguration getConfig() {
//		return config;
//	}
//
//	public void setConfig(NotificationConfiguration config) {
//		this.config = config;
//	}
//	
//	public String getNotificationString(AddedFlight f) {
//		StringBuffer ans = new StringBuffer("");
//		if (config.isNotifyOnStatusChanged() && status.equals(f.status))
//			ans.append("Status changed from " + status + " to " + f.status + "\n");
//		if (config.isNotifyOnTerminalChanged() && !departure.getAirportTerminal().equals(f.departure.getAirportTerminal()))
//			ans.append("Departure terminal changed from " + departure.getAirportTerminal() + " to " + f.departure.getAirportTerminal() + "\n");
//		if (config.isNotifyOnGateChanged() && !departure.getAirportGate().equals(f.departure.getAirportGate()))
//			ans.append("Departure gate changed from " + departure.getAirportGate() + " to " + f.departure.getAirportGate() + "\n");
//		if (config.isNotifyOnScheduledTimeChanged() && !departure.getScheduledTime().equals(f.departure.getScheduledGateTime()))
//				ans.append("Scheduled departure tate changed from " + departure.getScheduledTime() + " to " + f.departure.getScheduledTime() + "\n");
//		return ans.toString();
//	}
	
	public List<NameValuePair> getParams() {
		List<NameValuePair> params = new LinkedList<NameValuePair> ();
		params.add(new BasicNameValuePair("airline_id", this.airline.getId()));
		params.add(new BasicNameValuePair("flight_num", String.valueOf(this.flightNumber)));
		return params;
	}
	
}