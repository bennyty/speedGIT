package com.lehmannsystems.speedcoach;


import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CoachActivity extends ActionBarActivity {

	Boat boatA = new Boat("Entheos", "Mike", 1);
	Boat boatB = new Boat("First Four", "Mike", 1);
	boolean on = false;
	
	public void act () {
		Timer timer = new Timer();
		timer.schedule(new Render(), 0, 1000);
	}
	
	public void toggle(View v) {
		on = !on;
		TextView display = (TextView) findViewById(R.id.buttonC);
		display.setText("Toggle!");
	} 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coach);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		act();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.coach, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
	        case R.id.add_slot_a:
	            addBoatA();
	            return true;
	        case R.id.add_slot_b:
	            addBoatB();
	            return true;
	        case R.id.action_settings:
	            //openSettings();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
		}
	}
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_coach, container,
					false);
			return rootView;
		}
	}

	private void addBoatA() {
		// TODO Auto-generated method stub
		
	}

	private void addBoatB() {
		// TODO Auto-generated method stub
		
	}
	class Render extends TimerTask {
	    public void run() {
	    	TextView display;
	    	if (on) {
				boatA.update(boatA.cox, boatA.teamId);
				boatB.update(boatB.cox, boatB.teamId);
				display = (TextView) findViewById(R.id.coxA);
				display.setText(boatA.getCox());
				display = (TextView) findViewById(R.id.coxB);
				display.setText(boatB.getCox());
				display = (TextView) findViewById(R.id.splitA);
				display.setText(boatA.formatSplit(boatA.getRawSplit()));
				display = (TextView) findViewById(R.id.splitB);
				display.setText(boatB.formatSplit(boatB.getRawSplit()));
				display = (TextView) findViewById(R.id.metersA);
				display.setText(boatA.getMeters());
				display = (TextView) findViewById(R.id.metersB);
				display.setText(boatB.getMeters());
				display = (TextView) findViewById(R.id.rateA);
				display.setText(boatA.getRate());
				display = (TextView) findViewById(R.id.rateB);
				display.setText(boatB.getRate());
				display = (TextView) findViewById(R.id.avgSplitA);
				display.setText(boatA.formatSplit(boatA.getRawAvgSplit()));
				display = (TextView) findViewById(R.id.avgSplitB);
				display.setText(boatB.formatSplit(boatA.getRawAvgSplit()));
				display = (TextView) findViewById(R.id.totalTimeC);
				display.setText(boatA.formatSplit((double) (boatA.time)));
	    	}
			else {
				boatA.reset();
				boatB.reset();
			}
	    }
	}
}