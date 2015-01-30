package com.example.cmpe285healthproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class HospitalActivity extends ActionBarActivity {

	WebView webView;
	WebView webViewFooter;
	EditText Msg;
	Button btnSendMsg;
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private Spinner hospitalSpinner;
	private Spinner citySpinner;
	ProgressDialog dialog;
	Button getDetailsButton;
	Button getMapButton;
	Map<String,ArrayList<String>> mapHospital = new HashMap<String,ArrayList<String>>();
	Map<String, String> mapDetails = new HashMap<String, String>();
	TextView hospitalDetails; 
	String selectedHosp = "";

	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hospital);

		webView = (WebView) findViewById(R.id.webView);
		//webViewFooter = (WebView) findViewById(R.id.webViewFooter);

		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.getSettings().setLoadsImagesAutomatically(true);

		//WebSettings webSettingsFooter = webViewFooter.getSettings();
		//webSettingsFooter.setJavaScriptEnabled(true);
		//webViewFooter.getSettings().setLoadsImagesAutomatically(true);

		webSettings.setDomStorageEnabled(true);
		//webSettingsFooter.setDomStorageEnabled(true);

		final MyJavaScriptInterface hospital = new MyJavaScriptInterface(
				this);
		webView.addJavascriptInterface(hospital, "AndroidFunction");
		//webViewFooter.addJavascriptInterface(hospital, "AndroidFunction");

		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("file:///android_asset/hospital.html");

		//webViewFooter.getSettings().setJavaScriptEnabled(true);
		//webViewFooter.loadUrl("file:///android_asset/footer.html");

		hospitalSpinner = (Spinner) findViewById(R.id.hospital_spinner);
		citySpinner = (Spinner) findViewById(R.id.city_spinner);
		hospitalDetails =(TextView) findViewById(R.id.hospital_details);
		
		citySpinner.setOnItemSelectedListener(new OnItemSelectedListener() 
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
		        ArrayList<String> hospital = mapHospital.get(selected);
		        ArrayAdapter<String> adapterHospital = new ArrayAdapter<String>(
		        HospitalActivity.this, android.R.layout.simple_spinner_item,hospital);
		        adapterHospital.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		        hospitalSpinner.setAdapter(adapterHospital);

		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }
		});
		
		
		hospitalSpinner.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
		protected Adapter initializedAdapter=null;
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) 
		    {
		        if(initializedAdapter !=parentView.getAdapter() ) {
		            initializedAdapter = parentView.getAdapter();
		           return;
		        }

		        selectedHosp = parentView.getItemAtPosition(position).toString();	
		        
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }
		});
		
		getMapButton = (Button) findViewById(R.id.get_directions_button);
		//getDetailsButton.setVisibility(1);
		getMapButton.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
	             //when play is clicked show stop button and hide play button
	    	//getDetailsButton.setVisibility(View.GONE);
	    	String hospital = hospitalDetails.getText().toString();
	    	String url = "google.navigation:q=" + hospital;
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse(url));
			startActivity(intent);

	        }
	    });
	    
		getDetailsButton = (Button) findViewById(R.id.get_details);
		//getDetailsButton.setVisibility(1);
		getDetailsButton.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
			
	    	selectedHosp = (String)hospitalSpinner.getSelectedItem();
			hospitalDetails.setVisibility(View.VISIBLE);
	        hospitalDetails.setText(mapDetails.get(selectedHosp));
	        getMapButton.setVisibility(View.VISIBLE);  
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
			     Intent i = new Intent(HospitalActivity.this,
							MainActivity.class);
					HospitalActivity.this.startActivity(i);
			     }
		     public void launchHospital(){
		     Intent i = new Intent(HospitalActivity.this,
						HospitalActivity.class);
				HospitalActivity.this.startActivity(i);
		     }
		     public void launchPhysician(){
			     Intent i = new Intent(HospitalActivity.this,
							PhysicianActivity.class);
					HospitalActivity.this.startActivity(i);
			     }
		     public void launchAppointment(){
			     Intent i = new Intent(HospitalActivity.this,
							LoginActivity.class);
					HospitalActivity.this.startActivity(i);
			     }
		     public void launchHelpline(){
			     Intent i = new Intent(HospitalActivity.this,
							HelplineActivity.class);
					HospitalActivity.this.startActivity(i);
			     }
		     public void launchAmbulance(){
			     Intent i = new Intent(HospitalActivity.this,
							AmbulanceActivity.class);
					HospitalActivity.this.startActivity(i);
			     }
		     public void launchHealthTips(){
			     Intent i = new Intent(HospitalActivity.this,
							HealthTipsActivity.class);
					HospitalActivity.this.startActivity(i);
			     }
		     public void launchParking(){
			     Intent i = new Intent(HospitalActivity.this,
							ParkingActivity.class);
					HospitalActivity.this.startActivity(i);
			     }
		     public void launchLogin(){
			     Intent i = new Intent(HospitalActivity.this,
							HospitalActivity.class);
					HospitalActivity.this.startActivity(i);
			     }
		     public void launchSignUp(){
			     Intent i = new Intent(HospitalActivity.this,
							SignUpActivity.class);
					HospitalActivity.this.startActivity(i);
			     }
		     
		     
		     
		     public void callPhone(String phone){
		 		final Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
					String strPhone = phone;	//changing into string
					phoneCallIntent.setData(Uri.parse("tel:" + strPhone));
					startActivity(phoneCallIntent); // make a phone call
			     }
		     
		     
		     
		     public void openAndroidDialog(){
		      AlertDialog.Builder myDialog
		      = new AlertDialog.Builder(HospitalActivity.this);
		      myDialog.setTitle("DANGER!");
		      myDialog.setMessage("You can do what you want!");
		      myDialog.setPositiveButton("ON", null);
		      myDialog.show();
		     }

		 }
	
	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {

		switch (position) {
		case 0:
			// Whatever you want to happen when the first item gets selected
			break;
		case 1:
			// Whatever you want to happen when the second item gets selected
			break;
		case 2:
			// Whatever you want to happen when the thrid item gets selected
			break;

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

		String[] hospitals = null;
		String[] address = null;
		String[] city = null;
		Connection connect;
		ArrayList<String> cityList = new ArrayList<String>();
		
		protected void onPreExecute() {
			// before executing the async task display the progress dialog
			dialog = new ProgressDialog(HospitalActivity.this);
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
				resultSet = statement.executeQuery("select * from hospital");
				int count = 0;

				hospitals = new String[12];
				address = new String[12];
				city = new String[12];
				
				
				while (resultSet.next()) {
					hospitals[count] = resultSet.getString("hospital_name");
					address[count] = resultSet.getString("hospital_address");
					city[count] = resultSet.getString("hospital_city");								
					count++;
				}

				for (int i = 0; i < 12; i++) {
					ArrayList<String> t = mapHospital.get(city[i]);
					// Map<String,ArrayList<String>> map1 = new
					// HashMap<String,ArrayList<String>>();
					if (mapHospital.get(city[i]) == null) {
						ArrayList<String> hospitalListTemp = new ArrayList<String>();
						for (int j = 0; j < 12; j++) {
							if (city[i] == city[j]) {
								if (!cityList.contains(city[i])) {
									cityList.add(city[i]);
								}
								hospitalListTemp.add(hospitals[j]);
							}
						}
						mapHospital.put(city[i], hospitalListTemp);
						mapDetails.put(hospitals[i], address[i]);
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
					HospitalActivity.this,
					android.R.layout.simple_spinner_item, hospitals);

			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			hospitalSpinner.setAdapter(adapter);
			// hospitalSpinner.setOnItemSelectedListener(this);

			
			
			ArrayAdapter<String> adapterCity = new ArrayAdapter<String>(
			HospitalActivity.this, android.R.layout.simple_spinner_item,cityList);
			adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			citySpinner.setAdapter(adapterCity);

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
