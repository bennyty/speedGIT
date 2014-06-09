package com.lehmannsystems.speedcoach;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class CoxSignupActivity extends Activity {
	
	String[] teamNames;
	HashMap<String, Integer> teamNamesWithId;

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "I DONT KNOW WHAT THIS IS";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;
	private getTeamNames mGetTeamTask = null;

	// Values for email and password at the time of the login attempt.
	private String mName;
	private String mTeam;
	
	private String cTeamId;
	
/*	private boolean isLoaded = false;
	private boolean onCreateLooper = false;*/

	// UI references.
	private EditText mNameView;
	private AutoCompleteTextView mTeamView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	
	private Intent intent;
	Context context;
	
	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_cox_signup);
		setupActionBar();
		
		teamNamesWithId = new HashMap<String, Integer>();

		// Set up the login form.
		mName = getIntent().getStringExtra(EXTRA_EMAIL);
		mNameView = (EditText) findViewById(R.id.etCoxName);
		mNameView.setText(mName);
		
		context = getBaseContext();

		/*while (!onCreateLooper) {
			if (isLoaded) {
				adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, teamNames);
			} else {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}*/
		
		
		mTeamView = (AutoCompleteTextView) findViewById(R.id.etCoxTeam);
		mTeamView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line));
		/*mTeamView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.etCoxName || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});*/
		
		mGetTeamTask = new getTeamNames();
		mGetTeamTask.execute((Void) null);

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			// TODO: If Settings has multiple levels, Up should navigate up
			// that hierarchy.
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.cox_signup, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mNameView.setError(null);
		mTeamView.setError(null);

		// Store values at the time of the login attempt.
		mName = mNameView.getText().toString();
		mTeam = mTeamView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid team.
		if (TextUtils.isEmpty(mTeam)) {
			mTeamView.setError(getString(R.string.error_field_required));
			focusView = mTeamView;
			cancel = true;
		} /*else if (mPassword.length() < 4) {
			mTeamView.setError(getString(R.string.error_invalid_password));
			focusView = mTeamView;
			cancel = true;
		}*/

		// Check for a valid name.
		if (TextUtils.isEmpty(mName)) {
			mNameView.setError(getString(R.string.error_field_required));
			focusView = mNameView;
			cancel = true;
		} /*else if (!mName.contains("@")) {
			mNameView.setError(getString(R.string.error_invalid_email));
			focusView = mNameView;
			cancel = true;
		}*/

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class getTeamNames extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				URL db = new URL("http://getgreenrain.com/RowSplit/getTeamList.php");
				BufferedReader in = new BufferedReader(new InputStreamReader(db.openStream()));
				String inputLine = in.readLine();
				JSONArray json = new JSONArray(inputLine); //.substring(1, inputLine.length()-1)
				teamNames = new String[json.length()];
				for (int i = 0; i < json.length(); i++) {
					JSONObject jo = json.getJSONObject(i);
					String n = jo.getString("name");
					String joId = jo.getString("id");
					
					teamNamesWithId.put(n, Integer.valueOf(joId));
					teamNames[i] = n;
					//adapter.add(n);
				}
			} /*catch (Exception e) {
				e.printStackTrace();
			}*/
			
			 catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			
			//isLoaded = true;
			return true;
		}
		
		@Override
		protected void onPostExecute(final Boolean success) {
			mGetTeamTask = null;
			//showProgress(false);

			if (success) {
				mTeamView.setAdapter(new ArrayAdapter<String>(CoxSignupActivity.this, android.R.layout.simple_dropdown_item_1line, teamNames));
				finish();
			} else {
				//mTeamView.setError(getString(R.string.error_field_required));
				mTeamView.setError("Error connecting to the database.");
				//TODO May be incorrect ^^
				mTeamView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mGetTeamTask = null;
			//showProgress(false);
		}
		
	}
	
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			for (String key : teamNamesWithId.keySet()) {
				if(mTeam.equals(key))
				{
					cTeamId = teamNamesWithId.get(key).toString();
				}
			}
			
			URL db;
			BufferedReader in;
			try {
				// Network access
				db = new URL("http://getgreenrain.com/RowSplit/insertCoxin.php?tid=" + cTeamId + "&name=" + mName);
				in = new BufferedReader(new InputStreamReader(
						db.openStream()));
				in.readLine();
				in.close();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return false;
			}
			
			
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				//intent = new Intent(context, CoxActivity.class);
				//startActivity(intent);
				finish();
			} else {
				//mTeamView.setError(getString(R.string.error_field_required));
				mTeamView.setError("Error connecting to the database.");
				//TODO May be incorrect ^^
				mTeamView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
