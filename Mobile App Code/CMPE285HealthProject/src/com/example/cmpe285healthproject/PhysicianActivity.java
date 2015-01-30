package com.example.cmpe285healthproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.cmpe285healthproject.PhysicianActivity.DoInBackground;
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

public class PhysicianActivity extends ActionBarActivity {


	WebView webView;
	WebView webViewFooter;
	EditText Msg;
	Button btnSendMsg;
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private Spinner physicianSpinner;
	private Spinner specSpinner;
	ProgressDialog dialog;
	Map<String,ArrayList<String>> mapPhysician = new HashMap<String,ArrayList<String>>();
	Map<String, String> mapHospital = new HashMap<String, String>();
	Map<String, String> mapDesc = new HashMap<String, String>();
	Map<String, String> mapRating = new HashMap<String, String>();
	Map<String, String> mapSpec = new HashMap<String, String>();
	TextView physTV;
	TextView specTV;
	TextView hospTV;
	TextView descTV;
	TextView ratingTV;
	TextView physText;
	TextView specText;
	TextView hospText;
	TextView descText;
	TextView ratingText;
	Button getDetailsButton;
	String selectedPhys = "";
	
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_physician);

		webView = (WebView) findViewById(R.id.webView);

		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.getSettings().setLoadsImagesAutomatically(true);

		webSettings.setDomStorageEnabled(true);

		final MyJavaScriptInterface hospital = new MyJavaScriptInterface(this);
		webView.addJavascriptInterface(hospital, "AndroidFunction");
		//webViewFooter.addJavascriptInterface(hospital, "AndroidFunction");

		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("file:///android_asset/physician.html");

		physicianSpinner = (Spinner) findViewById(R.id.physician_spinner);
		specSpinner = (Spinner) findViewById(R.id.spec_spinner);
		physTV = (TextView) findViewById(R.id.physician_name);
		specTV = (TextView) findViewById(R.id.spec_name);
		hospTV = (TextView) findViewById(R.id.hospital_name);
		descTV = (TextView) findViewById(R.id.desc_name);
		ratingTV = (TextView) findViewById(R.id.rating_name);

		physText = (TextView) findViewById(R.id.physician_text);
		specText = (TextView) findViewById(R.id.spec_text);
		hospText = (TextView) findViewById(R.id.hospital_text);
		descText = (TextView) findViewById(R.id.desc_text);
		ratingText = (TextView) findViewById(R.id.rating_text);		
		
		selectedPhys = (String)physicianSpinner.getSelectedItem();
		
		specSpinner.setOnItemSelectedListener(new OnItemSelectedListener() 
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
		        ArrayList<String> hospital = mapPhysician.get(selected);
		        ArrayAdapter<String> adapterHospital = new ArrayAdapter<String>(
		        PhysicianActivity.this, android.R.layout.simple_spinner_item,hospital);
		        adapterHospital.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		        physicianSpinner.setAdapter(adapterHospital);

		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }
		});
		
		
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
		        selectedPhys = parentView.getItemAtPosition(position).toString();		        
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
	    	physTV.setVisibility(View.VISIBLE);
			specTV.setVisibility(View.VISIBLE);
			hospTV.setVisibility(View.VISIBLE);
			descTV.setVisibility(View.VISIBLE);
			ratingTV.setVisibility(View.VISIBLE);
			
			physText.setVisibility(View.VISIBLE);
			specText.setVisibility(View.VISIBLE);
			hospText.setVisibility(View.VISIBLE);
			descText.setVisibility(View.VISIBLE);
			ratingText.setVisibility(View.VISIBLE);
			
			selectedPhys = (String)physicianSpinner.getSelectedItem();
			String str = (String)physicianSpinner.getSelectedItem();
			String t = mapHospital.get(selectedPhys);
			
			physTV.setText(str);
			specTV.setText(mapHospital.get(selectedPhys));
			hospTV.setText(mapHospital.get(selectedPhys));
			descTV.setText(mapDesc.get(selectedPhys));
			ratingTV.setText(mapRating.get(selectedPhys));
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
			     Intent i = new Intent(PhysicianActivity.this,
							MainActivity.class);
					PhysicianActivity.this.startActivity(i);
			     }
		     public void launchHospital(){
		     Intent i = new Intent(PhysicianActivity.this,
						HospitalActivity.class);
				PhysicianActivity.this.startActivity(i);
		     }
		     public void launchPhysician(){
			     Intent i = new Intent(PhysicianActivity.this,
							PhysicianActivity.class);
					PhysicianActivity.this.startActivity(i);
			     }
		     public void launchAppointment(){
			     Intent i = new Intent(PhysicianActivity.this,
							LoginActivity.class);
					PhysicianActivity.this.startActivity(i);
			     }
		     public void launchHelpline(){
			     Intent i = new Intent(PhysicianActivity.this,
							HelplineActivity.class);
					PhysicianActivity.this.startActivity(i);
			     }
		     public void launchAmbulance(){
			     Intent i = new Intent(PhysicianActivity.this,
							AmbulanceActivity.class);
					PhysicianActivity.this.startActivity(i);
			     }
		     public void launchHealthTips(){
			     Intent i = new Intent(PhysicianActivity.this,
							HealthTipsActivity.class);
					PhysicianActivity.this.startActivity(i);
			     }
		     public void launchParking(){
			     Intent i = new Intent(PhysicianActivity.this,
							ParkingActivity.class);
					PhysicianActivity.this.startActivity(i);
			     }
		     public void launchLogin(){
			     Intent i = new Intent(PhysicianActivity.this,
							PhysicianActivity.class);
					PhysicianActivity.this.startActivity(i);
			     }
		     public void launchSignUp(){
			     Intent i = new Intent(PhysicianActivity.this,
							SignUpActivity.class);
					PhysicianActivity.this.startActivity(i);
			     }
		     
		     
		     
		     public void callPhone(String phone){
		 		final Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
					String strPhone = phone;	//changing into string
					phoneCallIntent.setData(Uri.parse("tel:" + strPhone));
					startActivity(phoneCallIntent); // make a phone call
			     }
		     
		     
		     
		     public void openAndroidDialog(){
		      AlertDialog.Builder myDialog
		      = new AlertDialog.Builder(PhysicianActivity.this);
		      myDialog.setTitle("DANGER!");
		      myDialog.setMessage("You can do what you want!");
		      myDialog.setPositiveButton("ON", null);
		      myDialog.show();
		     }

		 }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.physician, menu);
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

		String[] physician = null;
		String[] spec = null;
		String[] hospital = null;
		String[] rating = null;
		String[] description = null;
		
		Connection connect;
		ArrayList<String> cityList = new ArrayList<String>();
		
		protected void onPreExecute() {
			// before executing the async task display the progress dialog
			dialog = new ProgressDialog(PhysicianActivity.this);
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
				resultSet = statement.executeQuery("select sp.specialization_name, s.specialist_name, s.specialist_rating, s.specialist_description, h.hospital_name from specialist s, hospital h, specialization sp where s.specialization_id = sp.specialization_id and s.hospital_id = h.hospital_id");
				int count = 0;

				physician = new String[14];
				spec = new String[14];
				hospital = new String[14];
				rating = new String[14];
				description = new String[14];
				
				
				while (resultSet.next()) {
					physician[count] = resultSet.getString("specialist_name");
					spec[count] = resultSet.getString("specialization_name");
					hospital[count] = resultSet.getString("hospital_name");
					rating[count] = resultSet.getString("specialist_rating");
					description[count] = resultSet.getString("specialist_description");
					count++;
				}

				for (int i = 0; i < 12; i++) {
					ArrayList<String> t = mapPhysician.get(spec[i]);
					// Map<String,ArrayList<String>> map1 = new
					// HashMap<String,ArrayList<String>>();
					if (mapPhysician.get(spec[i]) == null) {
						ArrayList<String> physicianListTemp = new ArrayList<String>();
						for (int j = 0; j < 12; j++) {
							if (spec[i] == spec[j]) {
								if (!cityList.contains(spec)) {
									cityList.add(spec[i]);
								}
								physicianListTemp.add(physician[j]);
							}
						}
						
						mapPhysician.put(spec[i], physicianListTemp);
						mapHospital.put(physician[i], hospital[i]);
						mapDesc.put(physician[i], description[i]);
						mapRating.put(physician[i], rating[i]);
						mapSpec.put(physician[i], spec[i]);
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
					PhysicianActivity.this,
					android.R.layout.simple_spinner_item, physician);

			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			physicianSpinner.setAdapter(adapter);
			// hospitalSpinner.setOnItemSelectedListener(this);

			
			
			ArrayAdapter<String> adapterCity = new ArrayAdapter<String>(
			PhysicianActivity.this, android.R.layout.simple_spinner_item, spec);
			adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			specSpinner.setAdapter(adapterCity);

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
