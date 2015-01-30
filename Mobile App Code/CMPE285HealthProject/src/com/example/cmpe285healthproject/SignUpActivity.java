package com.example.cmpe285healthproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends ActionBarActivity {

	WebView webView;
	WebView webViewFooter;
	EditText Msg;
	Button btnSendMsg;
	ProgressDialog dialog;
	EditText firstname;
	EditText lastname;
	EditText email;
	EditText pwd;
	EditText address;
	EditText city;
	EditText state;
	EditText zip;
	EditText mobile;
	EditText homeph;
	EditText error;
	
	Button submit;
	Button reset;
    		
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		webView = (WebView) findViewById(R.id.webView);

		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.getSettings().setLoadsImagesAutomatically(true);

		webSettings.setDomStorageEnabled(true);

		final MyJavaScriptInterface hospital = new MyJavaScriptInterface(
				this);
		webView.addJavascriptInterface(hospital, "AndroidFunction");

		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("file:///android_asset/signUp.html");

		firstname = (EditText) findViewById(R.id.firstname);
		lastname = (EditText) findViewById(R.id.lastname);
		email = (EditText) findViewById(R.id.email);
		pwd = (EditText) findViewById(R.id.pwd);
		address = (EditText) findViewById(R.id.address);
		city = (EditText) findViewById(R.id.city);
		state = (EditText) findViewById(R.id.state);
		zip = (EditText) findViewById(R.id.zip);
		mobile = (EditText) findViewById(R.id.mobile);
		homeph = (EditText) findViewById(R.id.homeph);
		
		submit = (Button) findViewById(R.id.submit);
		reset = (Button) findViewById(R.id.reset);
		
		submit.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
	    	//login user
	    	new DoInBackground().execute();

	        }
	    });
	    
		reset.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	//reset all
		    	firstname.setText("");
				lastname.setText("");
				email.setText("");
				pwd.setText("");
				address.setText("");
				city.setText("");
				state.setText("");
				zip.setText("");
				mobile.setText("");
				homeph.setText("");

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
			     Intent i = new Intent(SignUpActivity.this,
							MainActivity.class);
					SignUpActivity.this.startActivity(i);
			     }
		     public void launchHospital(){
		     Intent i = new Intent(SignUpActivity.this,
						HospitalActivity.class);
				SignUpActivity.this.startActivity(i);
		     }
		     public void launchPhysician(){
			     Intent i = new Intent(SignUpActivity.this,
							PhysicianActivity.class);
					SignUpActivity.this.startActivity(i);
			     }
		     public void launchAppointment(){
			     Intent i = new Intent(SignUpActivity.this,
							LoginActivity.class);
					SignUpActivity.this.startActivity(i);
			     }
		     public void launchHelpline(){
			     Intent i = new Intent(SignUpActivity.this,
							HelplineActivity.class);
					SignUpActivity.this.startActivity(i);
			     }
		     public void launchAmbulance(){
			     Intent i = new Intent(SignUpActivity.this,
							AmbulanceActivity.class);
					SignUpActivity.this.startActivity(i);
			     }
		     public void launchHealthTips(){
			     Intent i = new Intent(SignUpActivity.this,
							HealthTipsActivity.class);
					SignUpActivity.this.startActivity(i);
			     }
		     public void launchParking(){
			     Intent i = new Intent(SignUpActivity.this,
							ParkingActivity.class);
					SignUpActivity.this.startActivity(i);
			     }
		     public void launchLogin(){
			     Intent i = new Intent(SignUpActivity.this,
							SignUpActivity.class);
					SignUpActivity.this.startActivity(i);
			     }
		     public void launchSignUp(){
			     Intent i = new Intent(SignUpActivity.this,
							SignUpActivity.class);
					SignUpActivity.this.startActivity(i);
			     }
		     
		     
		     
		     public void callPhone(String phone){
		 		final Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
					String strPhone = phone;	//changing into string
					phoneCallIntent.setData(Uri.parse("tel:" + strPhone));
					startActivity(phoneCallIntent); // make a phone call
			     }
		     
		     
		     
		     public void openAndroidDialog(){
		      AlertDialog.Builder myDialog
		      = new AlertDialog.Builder(SignUpActivity.this);
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
		boolean flag = false;
		private Context context;
		
		String sfirstname = firstname.getText().toString();
		String slastname = lastname.getText().toString();
		String semail = email.getText().toString();
		String spwd = pwd.getText().toString();
		String saddress = address.getText().toString();
		String scity = city.getText().toString();
		String sstate = state.getText().toString();
		String szip = zip.getText().toString();
		String smobile = mobile.getText().toString();
		String shomeph = homeph.getText().toString();
		
		protected void onPreExecute() {
			// before executing the async task display the progress dialog
			dialog = new ProgressDialog(SignUpActivity.this);
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
				//resultSet = statement.executeQuery("select user_id from hospital where user_id = \"" + username + " and user_pass = \"" + pwd);
				String sql1 = "insert into users (user_name, user_pass, user_role) values (\"" + semail + "\", \"" + spwd + "\", \"" + "client" + "\")";
				String sql2 = "insert into user_information (user_mail, user_first_name, user_last_name, user_address, user_city, user_state, user_zip, user_mobile, user_home_phone) values (\""
				+ semail + "\", \"" + sfirstname + "\", \"" + slastname + "\", \"" + saddress + "\", \"" + scity + "\", \"" + sstate + "\", \"" + szip + "\", \"" + smobile + "\", \"" + shomeph + "\")";
				
				if (statement.executeUpdate(sql1) > 0) { // new user account creation
					if(statement.executeUpdate(sql1) > 0)
					{
					flag = true;
				}
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
					Toast.makeText(context, "Successfully account created", Toast.LENGTH_LONG).show();
					Intent i = new Intent(SignUpActivity.this,
							AppointmentActivity.class);
					SignUpActivity.this.startActivity(i);
			}
			else {
				error.setVisibility(View.VISIBLE);
		        error.setText("An error occurred please try again");
		        Toast.makeText(context, "An error occured. Please try again!", Toast.LENGTH_LONG).show();
		        firstname.setText("");
				lastname.setText("");
				email.setText("");
				pwd.setText("");
				address.setText("");
				city.setText("");
				state.setText("");
				zip.setText("");
				mobile.setText("");
				homeph.setText("");

				
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
