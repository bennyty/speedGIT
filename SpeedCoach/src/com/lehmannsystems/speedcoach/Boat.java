package com.lehmannsystems.speedcoach;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import org.json.JSONObject;

import android.util.Log;

public class Boat {
	private String name;
	private String cox;
	private int teamId;
	private double rate;
	private double splitSeconds;
	private int time;
	private int meters;
	@SuppressWarnings("unused")
	private String regatta;
	private double totalSplit;
	
	public Boat (String n, String c, int i) {
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
		    JSONObject json = new JSONObject(inputLine.substring(1, inputLine.length()-1));

		    if (cox == json.getString("cox") || teamId == json.getInt("team_id")) {
		    	rate = json.getDouble("rate");
		    	meters = json.getInt("meters");
		    	splitSeconds = 500 / json.getDouble("mtpersec");
		    	if (splitSeconds>600)
		    		splitSeconds = 600;
		    	totalSplit += splitSeconds;
		    } else {
		    	System.out.println("Cox/Team ID Mismatch");
		    }
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void reset() {
		time = 0;
		meters = 0;
		rate = 0;
		totalSplit = 0;
		
	}
	
	public String getName() {
		return name;
	}
	
	public String getCox() {
		return cox;
	}
	
	public double getRate() {
		update(cox, teamId);
		//Random r = new Random();
		//rate = r.nextInt(40);
		return rate;
	}
	
	public double getRawSplit() {
		//update(cox, teamId);
		return splitSeconds;
	}
	
	public double getRawAvgSplit() {
		double a = totalSplit/time;
		return a;
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
	public int getTeamId() {
		return teamId;
	}
	
	public int getRawTime() {
		return time;
	}
	public void updateTime(){
		time++;
	}

	/**
	 * @param splitSeconds the splitSeconds to set
	 */
	public void setSplitSeconds(double splitSeconds) {
		this.splitSeconds = splitSeconds;
	}
	
	/**
	 * @param time the time to set
	 */
	public void setRawTime(int time) {
		this.time = time;
	}

	/**
	 * @param rate the rate to set
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}

	/**
	 * @param meters the meters to set
	 */
	public void setMeters(int meters) {
		this.meters = meters;
	}

	public double getTotalSplit() {
		return totalSplit;
	}

	public void setTotalSplit(double totalSplit) {
		this.totalSplit = totalSplit;
	}
}