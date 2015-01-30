package com.example.cmpe285healthproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AmbulanceActivity extends ActionBarActivity {

	WebView webView;
	WebView webViewFooter;
	EditText Msg;
	Button btnSendMsg;
	private Spinner providerSpinner;
	private Spinner locationSpinner;
	ProgressDialog dialog;
	Button getDetailsButton;
	Button getMapButton;
	Map<String,ArrayList<String>> mapProvider = new HashMap<String,ArrayList<String>>();
	Map<String, String> mapAddress = new HashMap<String, String>();
	Map<String, String> mapPhone = new HashMap<String, String>();
	TextView ambulance_text;
	TextView ambulance_name;
	TextView provider_text;
	TextView provider_name;
	TextView ambulance_no_text;
	TextView ambulance_no;
	String selectedProv = "";

	
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ambulance);

		webView = (WebView) findViewById(R.id.webView);

		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.getSettings().setLoadsImagesAutomatically(true);

		webSettings.setDomStorageEnabled(true);

		final MyJavaScriptInterface hospital = new MyJavaScriptInterface(
				this);
		webView.addJavascriptInterface(hospital, "AndroidFunction");

		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("file:///android_asset/ambulance.html");

		
		providerSpinner = (Spinner) findViewById(R.id.provider_spinner);
		locationSpinner = (Spinner) findViewById(R.id.location_spinner);
		ambulance_text =(TextView) findViewById(R.id.ambulance_text);
		ambulance_name =(TextView) findViewById(R.id.ambulance_name);
		provider_text =(TextView) findViewById(R.id.provider_text);
		provider_name =(TextView) findViewById(R.id.provider_name);
		ambulance_no_text =(TextView) findViewById(R.id.ambulance_no_text);
		ambulance_no =(TextView) findViewById(R.id.ambulance_no);
		
		locationSpinner.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
		protected Adapter initializedAdapter=null;
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) 
		    {
		        if(initializedAdapter !=parentView.getAdapter() ) {
		            initializedAdapter = parentView.getAdapter();
		           return;
		        }

		        String selected = parentView.getItemAtPosition(position).toString();
		        ArrayList<String> provider = mapProvider.get(selected);
		        ArrayAdapter<String> adapterHospital = new ArrayAdapter<String>(
		        AmbulanceActivity.this, android.R.layout.simple_spinner_item,provider);
		        adapterHospital.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		        providerSpinner.setAdapter(adapterHospital);

		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }
		});
		
		
		providerSpinner.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
		protected Adapter initializedAdapter=null;
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) 
		    {
		        if(initializedAdapter !=parentView.getAdapter() ) {
		            initializedAdapter = parentView.getAdapter();
		           return;
		        }

		        selectedProv = parentView.getItemAtPosition(position).toString();		        	        
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }
		});
		
	    
		getDetailsButton = (Button) findViewById(R.id.get_details);
		//getDetailsButton.setVisibility(1);
		getDetailsButton.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
			
			selectedProv = (String)providerSpinner.getSelectedItem();
			ambulance_text.setVisibility(View.VISIBLE);
	    	ambulance_name.setVisibility(View.VISIBLE);
	    	provider_text.setVisibility(View.VISIBLE);
	    	provider_name.setVisibility(View.VISIBLE);
	    	ambulance_no_text.setVisibility(View.VISIBLE);
	    	ambulance_no.setVisibility(View.VISIBLE);
	    	
			
			ambulance_name.setText(selectedProv);
			provider_name.setText(mapPhone.get(selectedProv));
			ambulance_no.setText(mapAddress.get(selectedProv));	  
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
			     Intent i = new Intent(AmbulanceActivity.this,
							MainActivity.class);
					AmbulanceActivity.this.startActivity(i);
			     }
		     public void launchHospital(){
		     Intent i = new Intent(AmbulanceActivity.this,
						HospitalActivity.class);
				AmbulanceActivity.this.startActivity(i);
		     }
		     public void launchPhysician(){
			     Intent i = new Intent(AmbulanceActivity.this,
							PhysicianActivity.class);
					AmbulanceActivity.this.startActivity(i);
			     }
		     public void launchAppointment(){
			     Intent i = new Intent(AmbulanceActivity.this,
							LoginActivity.class);
					AmbulanceActivity.this.startActivity(i);
			     }
		     public void launchHelpline(){
			     Intent i = new Intent(AmbulanceActivity.this,
							HelplineActivity.class);
					AmbulanceActivity.this.startActivity(i);
			     }
		     public void launchAmbulance(){
			     Intent i = new Intent(AmbulanceActivity.this,
							AmbulanceActivity.class);
					AmbulanceActivity.this.startActivity(i);
			     }
		     public void launchHealthTips(){
			     Intent i = new Intent(AmbulanceActivity.this,
							HealthTipsActivity.class);
					AmbulanceActivity.this.startActivity(i);
			     }
		     public void launchParking(){
			     Intent i = new Intent(AmbulanceActivity.this,
							ParkingActivity.class);
					AmbulanceActivity.this.startActivity(i);
			     }
		     public void launchLogin(){
			     Intent i = new Intent(AmbulanceActivity.this,
							AmbulanceActivity.class);
					AmbulanceActivity.this.startActivity(i);
			     }
		     public void launchSignUp(){
			     Intent i = new Intent(AmbulanceActivity.this,
							SignUpActivity.class);
					AmbulanceActivity.this.startActivity(i);
			     }
		     
		     
		     
		     public void callPhone(String phone){
		 		final Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
					String strPhone = phone;	//changing into string
					phoneCallIntent.setData(Uri.parse("tel:" + strPhone));
					startActivity(phoneCallIntent); // make a phone call
			     }
		     
		     
		     
		     public void openAndroidDialog(){
		      AlertDialog.Builder myDialog
		      = new AlertDialog.Builder(AmbulanceActivity.this);
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

		String[] provider = null;
		String[] address = null;
		String[] phone = null;
		String[] city = null;
		Connection connect;
		ArrayList<String> cityList = new ArrayList<String>();
		
		protected void onPreExecute() {
			// before executing the async task display the progress dialog
			dialog = new ProgressDialog(AmbulanceActivity.this);
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
				resultSet = statement.executeQuery("select * from ambulance_provider");
				int count = 0;

				provider = new String[11];
				address = new String[11];
				phone = new String[11];
				city = new String[11];
				
				
				while (resultSet.next()) {
					provider[count] = resultSet.getString("provider_name");
					address[count] = resultSet.getString("provider_address");
					phone[count] = resultSet.getString("provider_phone");
					city[count] = resultSet.getString("provider_city");		
					count++;
				}

				for (int i = 0; i < 11; i++) {
					ArrayList<String> t = mapProvider.get(city[i]);
					if (mapProvider.get(city[i]) == null) {
						ArrayList<String> hospitalListTemp = new ArrayList<String>();
						for (int j = 0; j < 11; j++) {
							if (city[i] == city[j]) {
								if (!cityList.contains(city[i])) {
									cityList.add(city[i]);
								}
								hospitalListTemp.add(provider[j]);
							}
						}
						mapProvider.put(city[i], hospitalListTemp);
						mapAddress.put(provider[i], address[i]);
						mapPhone.put(provider[i], phone[i]);
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

			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					AmbulanceActivity.this,
					android.R.layout.simple_spinner_item, provider);

			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			providerSpinner.setAdapter(adapter);
			// locationSpinner.setOnItemSelectedListener(this);

			
			
			ArrayAdapter<String> adapterCity = new ArrayAdapter<String>(
			AmbulanceActivity.this, android.R.layout.simple_spinner_item,cityList);
			adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			locationSpinner.setAdapter(adapterCity);

		}

	}
	
}
