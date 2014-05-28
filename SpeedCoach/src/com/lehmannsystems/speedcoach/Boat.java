package com.lehmannsystems.speedcoach;

import android.annotation.SuppressLint;

public class Boat {
	String name;
	String cox;
	int rate;
	double splitSeconds;
	int time;
	int meters;
	
	public Boat (String n, String c){
		name = n;
		cox = c;
		time = 0;
	}
	
	public boolean update() {
		boolean connected = false;
		//connect to database and update rate and splitSeconds
		time++; //assumes this method is called once per second, if not will need to be altered
		return connected;
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
		update();
		return rate;
	}
	
	public double getRawSplit() {
		update();
		return splitSeconds;
	}
	
	public int getMeters() {
		return meters;
	}
	
	public String getFormattedSplit() {
		String result = "";
		double split = getRawSplit();
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
