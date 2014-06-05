package com.lehmannsystems.speedcoach;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;
import android.widget.ToggleButton;

public class CoxActivity extends ActionBarActivity implements GPSInterface {
	Context context;
	TextView display;

	Boat myBoat;
	Location loc;

	private Location oldLocation;

	boolean on = false;
	boolean go = true;
	boolean guiOn = false;
	Intent intent;
	//boolean updateOn = false;
	
	URL db;
	BufferedReader in;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cox);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		oldLocation = null;

	}

	protected void onStart() {
		super.onStart();
		myBoat = new Boat("Entheos Tester", "Mike", 1);

		// go = true;
		ToggleButton b = (ToggleButton) findViewById(R.id.updateTogglerCox);
		b.setChecked(false);
		
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, this);

		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Toast.makeText(this, "GPS is Enabled in your device.",
					Toast.LENGTH_SHORT).show();
		} else {
			showGPSDisabledAlertToUser();
		}

	}

	protected void onStop() {
		super.onStop();

		//updateThread.interrupt();
		guiThread.interrupt();
	}

	private void showGPSDisabledAlertToUser() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
				.setMessage(
						"GPS is disabled in your device. Would you like to enable it?")
				.setCancelable(false)
				.setPositiveButton("Goto Settings Page To Enable GPS",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent callGPSSettingIntent = new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivity(callGPSSettingIntent);
							}
						});
		alertDialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}

	private void reset(Location loc) {
		myBoat.reset();
		oldLocation = loc;
		myBoat.setMeters(0);
	}

	public void toggle(View v) {

		boolean on = ((ToggleButton) v).isChecked();
		if (on) {
			if (go) {
				//updateThread.start();
				guiThread.start();
			}
			//updateOn = true;
			guiOn = true;
			go = false;
		} else {
			//updateOn = false;
			guiOn = false;
		}
	}
/*
	Thread updateThread = new Thread() {
		public void run() {
			try {
				URL db;
				BufferedReader in;
				while (!isInterrupted()) {
					if (updateOn) {
						if (loc != null) {
							if (oldLocation == null) {
								oldLocation = loc;
								myBoat.setMeters(0);
							} else {
								myBoat.setMeters(myBoat.getMeters()
										+ (int) loc.distanceTo(oldLocation));
								oldLocation = loc;
							}

							myBoat.setSplitSeconds(500 / loc.getSpeed());
							if (myBoat.getRawSplit()>600)
								myBoat.setSplitSeconds(600);

							try {
								db = new URL(
										"http://www.getgreenrain.com/RowSplit/storeSplit.php?"
												+ "cid=" + myBoat.getCox()
												+ "&tid=" + myBoat.getTeamId()
												+ "&spm=" + myBoat.getRate()
												+ "&mps="
												+ myBoat.getRawSplit() + "&mt="
												+ myBoat.getMeters());
								in = new BufferedReader(new InputStreamReader(
										db.openStream()));
								in.readLine();
							} catch (MalformedURLException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						Thread.sleep(1000);
					}
				}
			} catch (InterruptedException e) {
				return;
			}
		}
	};
*/
	Thread guiThread = new Thread() {
		public void run() {
			try {
				Thread.sleep(250);
				while (!isInterrupted()) {
					Thread.sleep(1000);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (guiOn) {
								display = (TextView) findViewById(R.id.tvMeters);
								display.setText(myBoat.getMeters() + " m");
								display = (TextView) findViewById(R.id.tvSplit);
								display.setText(myBoat.formatSplit(myBoat
										.getRawSplit()) + "");
								display = (TextView) findViewById(R.id.tvTime);
								display.setText(myBoat.formatSplit(myBoat
										.getRawTime()) + "");
								/*display = (TextView) findViewById(R.id.tvAvgSplit);
								display.setText(myBoat.formatSplit(myBoat
										.getRawAvgSplit()) + " ");*/
								display = (TextView) findViewById(R.id.tvRate);
								display.setText(myBoat.getRate() + "spm");
							}
						}
					});
				}
			} catch (InterruptedException e) {
				return;
			}
		}
	};

	public void onLocationChanged(Location loc) {
		if (loc != null) {
			if (oldLocation == null) {
				oldLocation = loc;
				myBoat.setMeters(0);
			} else {
				myBoat.setMeters(myBoat.getMeters()
						+ (int) loc.distanceTo(oldLocation));
				oldLocation = loc;
			}

			myBoat.setSplitSeconds(500 / loc.getSpeed());
			if (myBoat.getRawSplit()>600)
				myBoat.setSplitSeconds(600);

			try {
				db = new URL(
						"http://www.getgreenrain.com/RowSplit/storeSplit.php?"
								+ "cid=" + myBoat.getCox()
								+ "&tid=" + myBoat.getTeamId()
								+ "&spm=" + myBoat.getRate()
								+ "&mps="
								+ myBoat.getRawSplit() + "&mt="
								+ myBoat.getMeters());
				in = new BufferedReader(new InputStreamReader(
						db.openStream()));
				in.readLine();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void onProviderDisabled(String provider) {
		// TODO: do something one day?
	}

	public void onProviderEnabled(String provider) {
		// TODO: do something one day?
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO: do something one day?

	}

	public void onGpsStatusChanged(int event) {
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
		case R.id.change_view:
			intent = new Intent(this, WhoAreYouActivity.class);
			startActivity(intent);
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
