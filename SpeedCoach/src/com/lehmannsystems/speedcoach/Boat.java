package com.lehmannsystems.speedcoach;

public class Boat {
	String name;
	String cox;
	int rate;
	double splitSeconds;
	int time;
	int meters;
	static int NEWCOMMIT;
	
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
		return  ((int) getRawSplit()) / 60 + ":" + getRawSplit() % 60;
	}
}
