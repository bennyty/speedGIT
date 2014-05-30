package com.lehmannsystems.speedcoach;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import android.widget.Toast;

public class Boat {
	String name;
	String cox;
	int teamId;
	int rate;
	double splitSeconds;
	int time;
	int meters;
	String regatta;
	
	public Boat (String n, String c, int i){
		name = n;
		cox = c;
		teamId = i;
		time = 0;
		regatta = null;
	}
	
	public Boat (String n, String c, int i, String r){
		name = n;
		cox = c;
		teamId = i;
		time = 0;
		regatta = r;
	}
	
	public void update(String coxName, int teamID)  {
		
		//connect to database and update rate and splitSeconds
		URL db = null;
		BufferedReader in = null;
		
		try {
			db = new URL("http://getgreenrain.com/RowSplit/retrieveLatestSplit.php?cox=" + coxName + "&team=" + String.valueOf(teamID));
			in = new BufferedReader(new InputStreamReader(db.openStream()));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String inputLine;

		try {
			inputLine = in.readLine();
		    //System.out.println(inputLine);
		    //System.out.println(inputLine.substring(1, inputLine.length()-1));
		    //Need parsing here
		    JSONObject json = new JSONObject(inputLine.substring(1, inputLine.length()-1));
		    /*
		    System.out.println(json.length());
		    System.out.println(json.getInt("id"));
		    System.out.println(json.getString("cox"));
		    System.out.println(json.getInt("team_id"));
		    System.out.println(json.getString("time_stamp"));
		    System.out.println(json.getInt("meters"));
		    System.out.println(json.getDouble("mtpersec"));
		    System.out.println(json.getInt("rate"));
		    */
		    if (cox == json.getString("cox") || teamId == json.getInt("team_id")) {
		    	rate = json.getInt("rate");
		    	meters = json.getInt("meters");
		    	splitSeconds = 500 / json.getDouble("mtpersec");
		    } else {
		    	System.out.println("Cox or teamId mismatch");
		    }
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		time++; //assumes this method is called once per second, if not will need to be altered
	}
	
	public void setSplit(double s) {
		splitSeconds = s;
	}
	
	public void setRate(int r) {
		rate = r;
	}
	
	public void reset() {
		time = 0;
		meters = 0;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCox() {
		return cox;
	}
	
	public int getRate() {
		update(cox, teamId);
		return rate;
	}
	
	public double getRawSplit() {
		update(cox, teamId);
		return splitSeconds;
	}
	
	public double getRawAvgSplit() {
		return 0.0;
	}
	
	public int getMeters() {
		return meters;
	}
	
	public String formatSplit(double split) {
		String result = "";
		int minutes = ((int) split) / 60;
		int seconds = ((int) split) % 60;
		double ms = (split - (int) split) * 10;
		if (seconds > 9) {
			result = minutes + ":" + seconds + "." + (int) ms;
		}
		else {
			result = minutes + ":0" + seconds + "." + (int) ms;
		}
		return result;
	}
}