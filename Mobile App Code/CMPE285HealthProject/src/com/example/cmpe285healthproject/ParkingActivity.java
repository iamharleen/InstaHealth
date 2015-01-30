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

public class ParkingActivity extends ActionBarActivity {

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
	private TextView total_park;
	private TextView disable_park;
	private TextView cost_text;
	private TextView totalText;
	private TextView disableText;
	private TextView costText;

	String[] hospitals = null;
	String[] parking_total = null;
	String[] parking_disbale = null;
	String[] cost = null;
	
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parking);

		webView = (WebView) findViewById(R.id.webView);

		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.getSettings().setLoadsImagesAutomatically(true);

		webSettings.setDomStorageEnabled(true);

		final MyJavaScriptInterface myJavaScriptInterface
        = new MyJavaScriptInterface(this);
		webView.addJavascriptInterface(myJavaScriptInterface, "AndroidFunction");
      
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("file:///android_asset/parking.html");


		hospitalSpinner = (Spinner) findViewById(R.id.hospital_spinner);
		total_park =(TextView) findViewById(R.id.total_park);
		disable_park =(TextView) findViewById(R.id.disable_park);
		cost_text =(TextView) findViewById(R.id.cost);
		
		totalText =(TextView) findViewById(R.id.totalText);
		disableText =(TextView) findViewById(R.id.disableText);
		costText =(TextView) findViewById(R.id.costText);
		
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
		        String selected = parentView.getItemAtPosition(position).toString();
		        int i = hospitalSpinner.getSelectedItemPosition();	
		        totalText.setVisibility(View.VISIBLE);
		        disableText.setVisibility(View.VISIBLE);
		        costText.setVisibility(View.VISIBLE);
		        
		        total_park.setVisibility(View.VISIBLE);
		        disable_park.setVisibility(View.VISIBLE);
		        cost_text.setVisibility(View.VISIBLE);
		        total_park.setText(parking_total[i]);
		        disable_park.setText(parking_disbale[i]);
		        cost_text.setText(cost[i]);
		        
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
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
			     Intent i = new Intent(ParkingActivity.this,
							MainActivity.class);
					ParkingActivity.this.startActivity(i);
			     }
		     public void launchHospital(){
		     Intent i = new Intent(ParkingActivity.this,
						HospitalActivity.class);
				ParkingActivity.this.startActivity(i);
		     }
		     public void launchPhysician(){
			     Intent i = new Intent(ParkingActivity.this,
							PhysicianActivity.class);
					ParkingActivity.this.startActivity(i);
			     }
		     public void launchAppointment(){
			     Intent i = new Intent(ParkingActivity.this,
							LoginActivity.class);
					ParkingActivity.this.startActivity(i);
			     }
		     public void launchHelpline(){
			     Intent i = new Intent(ParkingActivity.this,
							HelplineActivity.class);
					ParkingActivity.this.startActivity(i);
			     }
		     public void launchAmbulance(){
			     Intent i = new Intent(ParkingActivity.this,
							AmbulanceActivity.class);
					ParkingActivity.this.startActivity(i);
			     }
		     public void launchHealthTips(){
			     Intent i = new Intent(ParkingActivity.this,
							HealthTipsActivity.class);
					ParkingActivity.this.startActivity(i);
			     }
		     public void launchParking(){
			     Intent i = new Intent(ParkingActivity.this,
							ParkingActivity.class);
					ParkingActivity.this.startActivity(i);
			     }
		     public void launchLogin(){
			     Intent i = new Intent(ParkingActivity.this,
							ParkingActivity.class);
					ParkingActivity.this.startActivity(i);
			     }
		     public void launchSignUp(){
			     Intent i = new Intent(ParkingActivity.this,
							SignUpActivity.class);
					ParkingActivity.this.startActivity(i);
			     }
		     
		     
		     
		     public void callPhone(String phone){
		 		final Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
					String strPhone = phone;	//changing into string
					phoneCallIntent.setData(Uri.parse("tel:" + strPhone));
					startActivity(phoneCallIntent); // make a phone call
			     }
		     
		     
		     
		     public void openAndroidDialog(){
		      AlertDialog.Builder myDialog
		      = new AlertDialog.Builder(ParkingActivity.this);
		      myDialog.setTitle("DANGER!");
		      myDialog.setMessage("You can do what you want!");
		      myDialog.setPositiveButton("ON", null);
		      myDialog.show();
		     }

		 }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.parking, menu);
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
	
	protected class DoInBackground extends AsyncTask<Void, String, String> {

		
		Connection connect;
		ArrayList<String> cityList = new ArrayList<String>();
		
		protected void onPreExecute() {
			// before executing the async task display the progress dialog
			dialog = new ProgressDialog(ParkingActivity.this);
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
				resultSet = statement.executeQuery("select h.hospital_name,p.parking_total_slots,p.parking_disable_slots,p.parking_cost from parking p, hospital h where p.hospital_id=h.hospital_id");
				int count = 0;

				hospitals = new String[12];
				parking_total = new String[12];
				parking_disbale = new String[12];
				cost = new String[12];				
				
				while (resultSet.next()) {
					hospitals[count] = resultSet.getString("hospital_name");
					parking_total[count] = resultSet.getString("parking_total_slots");
					parking_disbale[count] = resultSet.getString("parking_disable_slots");
					cost[count] = resultSet.getString("parking_cost");
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
					ParkingActivity.this,
					android.R.layout.simple_spinner_item, hospitals);

			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			hospitalSpinner.setAdapter(adapter);

		}

	}


}
