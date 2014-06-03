package com.lehmannsystems.speedcoach;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CoxActivity extends ActionBarActivity implements GPSInterface {
	Context context;
	TextView display;

	Boat myBoat;
	Location loc;

	private double oldLatitude;
	private double oldLongitude;
	private double newLatitude;
	private double newLongitude;
	private float[] locResults;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cox);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}

		myBoat = new Boat("Entheos Tester", "Mike", 1);
		locResults = new float[1];
		oldLatitude = 200;
		oldLongitude = 200;

		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

		updateThread.start();
		guiThread.start();
	}

	private void reset(Location loc) {
		myBoat.reset();
		oldLatitude = loc.getLatitude();
		oldLongitude = loc.getLongitude();
		myBoat.setMeters(0);
	}

	Thread updateThread = new Thread() {
		public void run() {
			while (!isInterrupted()) {
				if (loc!=null) {
					if (oldLatitude == 200 || oldLongitude == 200) {
						oldLatitude = loc.getLatitude();
						oldLongitude = loc.getLongitude();
						myBoat.setMeters(0);
					} else {
						newLatitude = loc.getLatitude();
						newLongitude = loc.getLongitude();
						Location.distanceBetween(oldLatitude, oldLongitude, newLatitude, newLongitude, locResults);
						myBoat.setMeters((int) locResults[0]);
						oldLatitude = newLatitude;
						oldLongitude = newLongitude;
					}

					myBoat.setSplitSeconds(500 / loc.getSpeed());
				}
			}
		}
	};

	Thread guiThread = new Thread() {
		public void run() {
			while (!isInterrupted()) {
				runOnUiThread(new Runnable() {
					public void run() {
						TextView display = (TextView) findViewById(R.id.tvMeters);
						display.setText(myBoat.getMeters() + " m");
						display = (TextView) findViewById(R.id.tvSplit);
						display.setText(myBoat.formatSplit(myBoat.getRawSplit()) + "");
						display = (TextView) findViewById(R.id.tvTime);
						display.setText(myBoat.formatSplit(myBoat.getRawTime()) + "");
						display = (TextView) findViewById(R.id.tvAvgSplit);
						display.setText(myBoat.formatSplit(myBoat.getRawAvgSplit()) + " ");
						display = (TextView) findViewById(R.id.tvRate);
						display.setText(myBoat.getRate() + "spm");
					}
				});
			}
		}
	};

	public void onLocationChanged(Location loc)
	{
		if (loc != null)
		{
			this.loc = loc;
		}
	}

	public void onProviderDisabled(String provider)
	{
		// TODO: do something one day?
	}

	public void onProviderEnabled(String provider)
	{
		// TODO: do something one day?
	}

	public void onStatusChanged(String provider, int status, Bundle extras)
	{
		// TODO: do something one day?

	}

	public void onGpsStatusChanged(int event)
	{
		// TODO: do something one day?
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cox, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_reset:
			reset(loc);
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
			View rootView = inflater.inflate(R.layout.fragment_cox, container,
					false);
			return rootView;
		}
	}

}
