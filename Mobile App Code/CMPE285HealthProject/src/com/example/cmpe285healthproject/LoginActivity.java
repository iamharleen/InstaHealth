package com.example.cmpe285healthproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class LoginActivity extends ActionBarActivity {

	WebView webView;
	WebView webViewFooter;
	EditText Msg;
	Button btnSendMsg;
	ProgressDialog dialog;
	Button getDetailsButton;
	TextView username;
	TextView pwd; 
	TextView error; 
	Button login;

	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		CookieManager.getInstance().setAcceptCookie(true);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		
		webView = (WebView) findViewById(R.id.webView);

		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.getSettings().setLoadsImagesAutomatically(true);

		webSettings.setDomStorageEnabled(true);

		final MyJavaScriptInterface hospital = new MyJavaScriptInterface(
				this);
		webView.addJavascriptInterface(hospital, "AndroidFunction");

		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("file:///android_asset/login.html");

		username = (TextView) findViewById(R.id.username);
		pwd = (TextView) findViewById(R.id.pwd);
		login = (Button) findViewById(R.id.login);
		error = (TextView) findViewById(R.id.error);
		//getDetailsButton.setVisibility(1);
		login.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
	    	//login user
	    	new DoInBackground().execute();

	        }
	    });
	    

		


	}
	private class MyJavaScriptInterface {
		  Context mContext;

		     MyJavaScriptInterface(Context c) {
		         mContext = c;
		     }
		    
		     public void showToast(String toast){
		         Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
		     }
		     public void launchHome(){
			     Intent i = new Intent(LoginActivity.this,
							MainActivity.class);
					LoginActivity.this.startActivity(i);
			     }
		     public void launchHospital(){
		     Intent i = new Intent(LoginActivity.this,
						HospitalActivity.class);
				LoginActivity.this.startActivity(i);
		     }
		     public void launchPhysician(){
			     Intent i = new Intent(LoginActivity.this,
							PhysicianActivity.class);
					LoginActivity.this.startActivity(i);
			     }
		     public void launchAppointment(){
			     Intent i = new Intent(LoginActivity.this,
							LoginActivity.class);
					LoginActivity.this.startActivity(i);
			     }
		     public void launchHelpline(){
			     Intent i = new Intent(LoginActivity.this,
							HelplineActivity.class);
					LoginActivity.this.startActivity(i);
			     }
		     public void launchAmbulance(){
			     Intent i = new Intent(LoginActivity.this,
							AmbulanceActivity.class);
					LoginActivity.this.startActivity(i);
			     }
		     public void launchHealthTips(){
			     Intent i = new Intent(LoginActivity.this,
							HealthTipsActivity.class);
					LoginActivity.this.startActivity(i);
			     }
		     public void launchParking(){
			     Intent i = new Intent(LoginActivity.this,
							ParkingActivity.class);
					LoginActivity.this.startActivity(i);
			     }
		     public void launchLogin(){
			     Intent i = new Intent(LoginActivity.this,
							LoginActivity.class);
					LoginActivity.this.startActivity(i);
			     }
		     public void launchSignUp(){
			     Intent i = new Intent(LoginActivity.this,
							SignUpActivity.class);
					LoginActivity.this.startActivity(i);
			     }
		     
		     
		     
		     public void callPhone(String phone){
		 		final Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
					String strPhone = phone;	//changing into string
					phoneCallIntent.setData(Uri.parse("tel:" + strPhone));
					startActivity(phoneCallIntent); // make a phone call
			     }
		     
		     
		     
		     public void openAndroidDialog(){
		      AlertDialog.Builder myDialog
		      = new AlertDialog.Builder(LoginActivity.this);
		      myDialog.setTitle("DANGER!");
		      myDialog.setMessage("You can do what you want!");
		      myDialog.setPositiveButton("ON", null);
		      myDialog.show();
		     }

		 }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hospital, menu);
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

	public class HospitalJavaScriptInterface {
		Context mContext;

		HospitalJavaScriptInterface(Context c) {
			mContext = c;
		}

		public void showToast(String toast) {
			Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
		}

		public void callPhone(String phone) {
			final Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
			String strPhone = phone; // changing into string
			phoneCallIntent.setData(Uri.parse("tel:" + strPhone));
			startActivity(phoneCallIntent); // make a phone call
		}

		public void openMap(String address, String city) {
			String url = "google.navigation:q=an+" + address + "+" + city;
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse(url));
			startActivity(intent);
		}

	}

	protected class DoInBackground extends AsyncTask<Void, String, String> {

		Connection connect;
		
		String susername = username.getText().toString();
		String spwd = pwd.getText().toString();
		boolean flag = false;
		private Context context;
		protected void onPreExecute() {
			// before executing the async task display the progress dialog
			dialog = new ProgressDialog(LoginActivity.this);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setMessage("Loading…");
			dialog.setCancelable(false);
			// show dialog to user
			dialog.show();
		}

		protected String doInBackground(Void... params) {
			try {			
				Class.forName("com.mysql.jdbc.Driver");
				// setup the connection with the DB.
				connect = DriverManager
						.getConnection("jdbc:mysql://us-cdbr-azure-west-a.cloudapp.net/as_4a21b51c683787a?"
								+ "user=b4b87dac9133e2&password=23a80e41");

				// statements allow to issue SQL queries to the database
				Statement statement = connect.createStatement();
				ResultSet resultSet = null;
				System.out.println("select user_id from users where user_name = \"" + susername + "\" and user_pass = \"" + spwd);
				resultSet = statement.executeQuery("select user_id from users where user_name = \"" + susername + "\" and user_pass = \"" + spwd + "\"");
				
				while (resultSet.next()) {
					flag = true;
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					connect.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return null;
		}

		/**
		 * This function set the values on Activity screen once async task is
		 * done
		 * 
		 * @param String
		 *            result value
		 * 
		 * @return nothing
		 */
		protected void onPostExecute(String result) {
			System.out.println("POST EXECUTE");
			if (dialog.isShowing())
				dialog.dismiss();// dismiss loading dialog
			// set the weather variables on activity screen
			Context context = getApplicationContext();
			if(flag)
			{
				CookieManager.getInstance().setAcceptCookie(true);
				webView.loadUrl("javascript:setCookie(\"" + susername + "\", \"" + spwd + "\");");
				Toast.makeText(context, "Login Successful", Toast.LENGTH_LONG).show();
	
					Intent i = new Intent(LoginActivity.this,
							AppointmentActivity.class);
					LoginActivity.this.startActivity(i);
			}
			else {
				error.setVisibility(View.VISIBLE);
		        error.setText("Invalid username/password");
			}
		}

	}
	
	public static Object getKeyFromValue(Map hm, Object value) {
	    for (Object o : hm.keySet()) {
	      if (hm.get(o).equals(value)) {
	        return o;
	      }
	    }
	    return null;
	  }
}
