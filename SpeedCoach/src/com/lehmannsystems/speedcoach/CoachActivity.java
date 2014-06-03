package com.lehmannsystems.speedcoach;


import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.os.Build;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CoachActivity extends ActionBarActivity {

	Boat boatA = new Boat("Entheos", "Ben", 1);
	Boat boatB = new Boat("First Four", "Mike", 1);
	String coxA = "Ready";
	String coxB = "Ready";
	String splitA = "0:00.0";
	String splitB = "0:00.0";
	int metersA = 0;
	int metersB = 0;
	double rateA = 0;
	double rateB = 0;
	String avgSplitA = "0:00.0";
	String avgSplitB = "0:00.0";
	String totalTime = "0:00.0";
	boolean on = false;
	boolean go = true;
	boolean guiOn = false;
	boolean updateOn = false;
	final Runnable updateRunnable = new Runnable() {
		   public void run() {
			   Timer myTimer = new Timer();
			      myTimer.schedule(new TimerTask() {
			         @Override
			         public void run() {	 
			        	 if (updateOn) {
			        	 	 boatA.update(boatA.getCox(), boatA.getTeamId());
			        	 	 boatB.update(boatB.getCox(), boatB.getTeamId());
				        	 coxA = boatA.getCox();
				        	 coxB = boatB.getCox();
				        	 splitA = boatA.formatSplit(boatA.getRawSplit());
				        	 splitB = boatB.formatSplit(boatB.getRawSplit());
				        	 metersA = boatA.getMeters();
				        	 metersB = boatB.getMeters();
				        	 rateA = boatA.getRate();
				        	 rateB = boatB.getRate();
				        	 avgSplitA = boatA.formatSplit(boatA.getRawAvgSplit());
				        	 avgSplitB = boatB.formatSplit(boatB.getRawAvgSplit());
				        	 totalTime = boatA.formatSplit((boatA.getRawTime()));
				        	 boatA.updateTime();
				        	 boatB.updateTime();
			        	 }
			         ;}
			      }, 0, 1000);

						   }
		};
	Thread updateThread = new Thread(updateRunnable);
	Thread guiThread = new Thread() {
		  @Override
		  public void run() {
		    try {
		      Thread.sleep(250);
		      while (!isInterrupted()) {
		        Thread.sleep(1000);
		        runOnUiThread(new Runnable() {
		          @Override
		          public void run() {
		        	  if (guiOn) {
			        	  TextView display = (TextView) findViewById(R.id.coxA);
			         	  display.setText(coxA + " ");
			         	  display = (TextView) findViewById(R.id.coxB);
			         	  display.setText(coxB + " ");
			         	  display = (TextView) findViewById(R.id.splitA);
			         	  display.setText(splitA + " ");
			         	  display = (TextView) findViewById(R.id.splitB);
			         	  display.setText(splitB + " ");
			         	  display = (TextView) findViewById(R.id.metersA);
			         	  display.setText(metersA + " m");
			         	  display = (TextView) findViewById(R.id.metersB);
			         	  display.setText(metersB + " m");
			         	  display = (TextView) findViewById(R.id.rateA);
			         	  display.setText(rateA + " spm");
			         	  display = (TextView) findViewById(R.id.rateB);
			         	  display.setText(rateB + " spm");
			         	  display = (TextView) findViewById(R.id.avgSplitA);
			         	  display.setText(avgSplitA + " ");
			         	  display = (TextView) findViewById(R.id.avgSplitB);
			         	  display.setText(avgSplitB + " ");
			         	  display = (TextView) findViewById(R.id.totalTimeC);
			         	  display.setText("Time: " + totalTime.substring(0, 4));
		        	  }
		          }
		        });
		      }
		    } catch (InterruptedException e) {
		    }
		  }
		};
	
	public void toggle(View v) {
		
		boolean on = ((ToggleButton) v).isChecked();
		if (on) {
			if (go) {
				updateThread.start();
				guiThread.start();
			}
			reset();
			updateOn = true;
			guiOn = true;
			go = false;
		}
		else {
			updateOn = false;
			guiOn = false;
		}
	} 
	public void reset() {
		  boatA.reset();
		  boatB.reset();
		  coxA = "Ready";
		  coxB = "Ready";
	      splitA = "0:00.0";
		  splitB = "0:00.0";
		  metersA = 0;
		  metersB = 0;
		  rateA = 0;
	      rateB = 0;
		  avgSplitA = "0:00.0";
		  avgSplitB = "0:00.0";
		  totalTime = "0:00";
		  TextView display = (TextView) findViewById(R.id.coxA);
     	  display.setText(coxA + " ");
     	  display = (TextView) findViewById(R.id.coxB);
     	  display.setText(coxB + " ");
     	  display = (TextView) findViewById(R.id.splitA);
     	  display.setText(splitA + " ");
     	  display = (TextView) findViewById(R.id.splitB);
     	  display.setText(splitB + " ");
     	  display = (TextView) findViewById(R.id.metersA);
     	  display.setText(metersA + " m");
     	  display = (TextView) findViewById(R.id.metersB);
     	  display.setText(metersB + " m");
     	  display = (TextView) findViewById(R.id.rateA);
     	  display.setText(rateA + " spm");
     	  display = (TextView) findViewById(R.id.rateB);
     	  display.setText(rateB + " spm");
     	  display = (TextView) findViewById(R.id.avgSplitA);
     	  display.setText(avgSplitA + " ");
     	  display = (TextView) findViewById(R.id.avgSplitB);
     	  display.setText(avgSplitB + " ");
     	  display = (TextView) findViewById(R.id.totalTimeC);
     	  display.setText("Time: " + totalTime);
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
}