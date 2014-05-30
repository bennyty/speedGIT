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

public class CoachActivity extends ActionBarActivity {

	Boat boatA = new Boat("Testrel A", "Atlas", 1);
	Boat boatB = new Boat("Testrel B", "P-Body", 1);
	boolean on = false;
	
	public void act () {
		while (on) {
			boatA.update(boatA.cox, boatA.teamId);
			boatB.update(boatB.cox, boatB.teamId);
			TextView display = (TextView) findViewById(R.id.coxA);
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
		}
		boatA.reset();
		boatB.reset();
	}
	
	public void toggle() {
		on = !on;
	} 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coach);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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

}