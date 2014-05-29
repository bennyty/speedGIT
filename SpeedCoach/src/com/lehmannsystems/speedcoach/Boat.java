package com.lehmannsystems.speedcoach;

public class Boat {
	String name;
	String cox;
	int rate;
	double splitSeconds;
	int time;
	int meters;
	String regatta;
	
	public Boat (String n, String c){
		name = n;
		cox = c;
		time = 0;
		regatta = null;
	}
	
	public Boat (String n, String c, String r){
		name = n;
		cox = c;
		time = 0;
		regatta = r;
	}
	
	public void update() {
		boolean connected = false;
		//connect to database and update rate and splitSeconds
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
		update();
		return rate;
	}
	
	public double getRawSplit() {
		update();
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
