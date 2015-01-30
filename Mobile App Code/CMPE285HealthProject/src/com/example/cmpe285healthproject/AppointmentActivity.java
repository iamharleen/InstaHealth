package com.example.cmpe285healthproject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.cmpe285healthproject.SignUpActivity.DoInBackground;
import com.mysql.jdbc.PreparedStatement;

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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;

public class AppointmentActivity extends ActionBarActivity {

	WebView webView;
	WebView webViewFooter;
	EditText Msg;
	Button btnSendMsg;
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private Spinner physicianSpinner;
	ProgressDialog dialog;
	Button getDetailsButton;
	Button getMapButton;
	Map<String,ArrayList<String>> mapPhysician = new HashMap<String,ArrayList<String>>();
	Map<String, String> mapHospital = new HashMap<String, String>();
	Map<String, String> mapDesc = new HashMap<String, String>();
	Map<String, String> mapRating = new HashMap<String, String>();
	Map<String, String> mapSpec = new HashMap<String, String>();

	EditText dateInput;
	EditText time_name;
	EditText email_name;
	EditText contact_name;
	Button sendEmail;
	String[] physician = null;
	String selected = "";
	String func = "first";
	CalendarView cal;
	@SuppressLint("JavascriptInterface")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment);

		webView = (WebView) findViewById(R.id.webView);

		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.getSettings().setLoadsImagesAutomatically(true);

		webSettings.setDomStorageEnabled(true);

		final MyJavaScriptInterface hospital = new MyJavaScriptInterface(this);
		webView.addJavascriptInterface(hospital, "AndroidFunction");

		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("file:///android_asset/appointment.html");

		physicianSpinner = (Spinner) findViewById(R.id.physician_spinner);
		dateInput = (EditText) findViewById(R.id.date);
		time_name = (EditText) findViewById(R.id.time_name);
		email_name = (EditText) findViewById(R.id.email_name);
		contact_name = (EditText) findViewById(R.id.contact_name);

		
		
		physicianSpinner.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
		protected Adapter initializedAdapter=null;
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) 
		    {
		        if(initializedAdapter !=parentView.getAdapter() ) {
		            initializedAdapter = parentView.getAdapter();
		           return;
		        }

		        selected = parentView.getItemAtPosition(position).toString();
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }
		});

		

		//curDate = sdf.format(date.getTime());
	/*	cal = (CalendarView) findViewById(R.id.calendarView1);
        
        cal.setOnDateChangeListener(new OnDateChangeListener() {
			
		@Override
		public void onSelectedDayChange(CalendarView view, int year, int month,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			
			String dateString = year + "-" + month + "-" + dayOfMonth + "-";
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			try {
				Date date = format.parse(dateString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(dateString); 
			
			
			Toast.makeText(getBaseContext(),"Selected Date is\n\n"
				+dayOfMonth+" : "+month+" : "+year , 
				Toast.LENGTH_LONG).show();
		}
	});*/
        
		sendEmail = (Button) findViewById(R.id.send);		
		sendEmail.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {

			String[] TO = {"cmpe285team14@gmail.com"};
			String[] CC = {email_name.getText().toString()};
	    	Intent emailIntent = new Intent(Intent.ACTION_SEND);
	    	emailIntent.setData(Uri.parse("mailto:"));
	    	emailIntent.setType("text/plain");
	    	emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
	        emailIntent.putExtra(Intent.EXTRA_CC, CC);
	        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Appointment");
	        String message = "Dear, \n "+ selected +"\n Request you for an Appointment for date : " + dateInput.getText().toString() + " and at time: " + time_name.getText().toString() + "\nContact: " + contact_name.getText().toString();
	        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

	        try {
	            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	            finish();
	         } catch (android.content.ActivityNotFoundException ex) {
	            Toast.makeText(AppointmentActivity.this, 
	            "There is no email client installed.", Toast.LENGTH_SHORT).show();
	         }
			//startActivity(emailIntent);

	        }
	    });
        new DoInBackground().execute();
		
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
			     Intent i = new Intent(AppointmentActivity.this,
							MainActivity.class);
					AppointmentActivity.this.startActivity(i);
			     }
		     public void launchHospital(){
		     Intent i = new Intent(AppointmentActivity.this,
						HospitalActivity.class);
				AppointmentActivity.this.startActivity(i);
		     }
		     public void launchPhysician(){
			     Intent i = new Intent(AppointmentActivity.this,
							PhysicianActivity.class);
					AppointmentActivity.this.startActivity(i);
			     }
		     public void launchAppointment(){
			     Intent i = new Intent(AppointmentActivity.this,
							LoginActivity.class);
					AppointmentActivity.this.startActivity(i);
			     }
		     public void launchHelpline(){
			     Intent i = new Intent(AppointmentActivity.this,
							HelplineActivity.class);
					AppointmentActivity.this.startActivity(i);
			     }
		     public void launchAmbulance(){
			     Intent i = new Intent(AppointmentActivity.this,
							AmbulanceActivity.class);
					AppointmentActivity.this.startActivity(i);
			     }
		     public void launchHealthTips(){
			     Intent i = new Intent(AppointmentActivity.this,
							HealthTipsActivity.class);
					AppointmentActivity.this.startActivity(i);
			     }
		     public void launchParking(){
			     Intent i = new Intent(AppointmentActivity.this,
							ParkingActivity.class);
					AppointmentActivity.this.startActivity(i);
			     }
		     public void launchLogin(){
			     Intent i = new Intent(AppointmentActivity.this,
							AppointmentActivity.class);
					AppointmentActivity.this.startActivity(i);
			     }
		     public void launchSignUp(){
			     Intent i = new Intent(AppointmentActivity.this,
							SignUpActivity.class);
					AppointmentActivity.this.startActivity(i);
			     }
		     
		     
		     
		     public void callPhone(String phone){
		 		final Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
					String strPhone = phone;	//changing into string
					phoneCallIntent.setData(Uri.parse("tel:" + strPhone));
					startActivity(phoneCallIntent); // make a phone call
			     }
		     
		    // Intent emailIntent = new Intent(Intent.ACTION_SEND);
		     
		     public void openAndroidDialog(){
		      AlertDialog.Builder myDialog
		      = new AlertDialog.Builder(AppointmentActivity.this);
		      myDialog.setTitle("DANGER!");
		      myDialog.setMessage("You can do what you want!");
		      myDialog.setPositiveButton("ON", null);
		      myDialog.show();
		     }

		 }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.appointment, menu);
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
	
	
	private class JavaScriptInterface {
		Context mContext;

		JavaScriptInterface(Context c) {
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
		ArrayList<String> cityList = new ArrayList<String>();
		
		protected void onPreExecute() {
			// before executing the async task display the progress dialog
			dialog = new ProgressDialog(AppointmentActivity.this);
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
				resultSet = statement.executeQuery("select specialist_name from specialist");
				int count = 0;

				physician = new String[14];
				
				
				while (resultSet.next()) {
					physician[count] = resultSet.getString("specialist_name");
					count++;
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

			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					AppointmentActivity.this,
					android.R.layout.simple_spinner_item, physician);

			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			physicianSpinner.setAdapter(adapter);
			// hospitalSpinner.setOnItemSelectedListener(this);


		}

	}
	
}
