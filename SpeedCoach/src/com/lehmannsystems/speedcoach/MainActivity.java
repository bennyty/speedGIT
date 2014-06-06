package com.lehmannsystems.speedcoach;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Intent intent;
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		//SharedPreferences.Editor prefEditor = prefs.edit();
		//prefEditor.putBoolean("setup", Boolean.FALSE);
		//prefEditor.commit();
		
	    boolean previouslyStarted = prefs.getBoolean("setup", Boolean.FALSE);
	    Log.v(getLocalClassName(), String.valueOf(previouslyStarted));
	    if(!previouslyStarted){
		    intent = new Intent(this, WhoAreYouActivity.class);
		    startActivity(intent);
		    //Log.d(getLocalClassName(), "This has Not been previously started");
	    } else {
	    	//Log.d(getLocalClassName(), "This has been previously started");
	    	switch (prefs.getInt("type", 0)) {
		    	//0 - Coach
				//1 - Cox
				//2 - Viewer
	    		case (0):
	    			intent = new Intent(this, CoachActivity.class);
					startActivity(intent);
	    		break;
	    		case (1):
	    			intent = new Intent(this, CoxActivity.class);
					startActivity(intent);
				break;
	    	}
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

	/*Not used (static references are too hard)
	public void toastMe(String msg) {
		Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
    	toast.show();
	}*/
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
