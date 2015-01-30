package com.example.cmpe285healthproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class HealthTipsActivity extends ActionBarActivity {



	WebView webView;
	WebView webViewFooter;
	EditText Msg;
	Button btnSendMsg;
	ProgressDialog dialog;
	Button getDetailsButton;
	Button getMapButton;
	Map<String,ArrayList<String>> mapHospital = new HashMap<String,ArrayList<String>>();
	Map<String, String> mapDetails = new HashMap<String, String>();
	private TextView tip_name;
	private TextView tip_desc;
	private TextView tip_link;
	private TextView linkText;
	GridView gridDisplay;

	String[] tipName = null;
	String[] tipDescription = null;
	String[] tipLink = null;
	Context context;
	
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_health_tips);

		webView = (WebView) findViewById(R.id.webView);

		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.getSettings().setLoadsImagesAutomatically(true);

		webSettings.setDomStorageEnabled(true);

		final MyJavaScriptInterface myJavaScriptInterface
        = new MyJavaScriptInterface(this);
		webView.addJavascriptInterface(myJavaScriptInterface, "AndroidFunction");
      
		
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("file:///android_asset/healthtips.html");

		context = this;
		
		tip_name = (TextView) findViewById(R.id.tip_name);
		tip_link = (TextView) findViewById(R.id.tip_link);
	    
		
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
			     Intent i = new Intent(HealthTipsActivity.this,
							MainActivity.class);
					HealthTipsActivity.this.startActivity(i);
			     }
		     public void launchHospital(){
		     Intent i = new Intent(HealthTipsActivity.this,
						HospitalActivity.class);
				HealthTipsActivity.this.startActivity(i);
		     }
		     public void launchPhysician(){
			     Intent i = new Intent(HealthTipsActivity.this,
							PhysicianActivity.class);
					HealthTipsActivity.this.startActivity(i);
			     }
		     public void launchAppointment(){
			     Intent i = new Intent(HealthTipsActivity.this,
							LoginActivity.class);
					HealthTipsActivity.this.startActivity(i);
			     }
		     public void launchHelpline(){
			     Intent i = new Intent(HealthTipsActivity.this,
							HelplineActivity.class);
					HealthTipsActivity.this.startActivity(i);
			     }
		     public void launchAmbulance(){
			     Intent i = new Intent(HealthTipsActivity.this,
							AmbulanceActivity.class);
					HealthTipsActivity.this.startActivity(i);
			     }
		     public void launchHealthTips(){
			     Intent i = new Intent(HealthTipsActivity.this,
							HealthTipsActivity.class);
					HealthTipsActivity.this.startActivity(i);
			     }
		     public void launchParking(){
			     Intent i = new Intent(HealthTipsActivity.this,
							ParkingActivity.class);
					HealthTipsActivity.this.startActivity(i);
			     }
		     public void launchLogin(){
			     Intent i = new Intent(HealthTipsActivity.this,
							HealthTipsActivity.class);
					HealthTipsActivity.this.startActivity(i);
			     }
		     public void launchSignUp(){
			     Intent i = new Intent(HealthTipsActivity.this,
							SignUpActivity.class);
					HealthTipsActivity.this.startActivity(i);
			     }
		     
		     
		     
		     public void callPhone(String phone){
		 		final Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
					String strPhone = phone;	//changing into string
					phoneCallIntent.setData(Uri.parse("tel:" + strPhone));
					startActivity(phoneCallIntent); // make a phone call
			     }
		     
		     
		     
		     public void openAndroidDialog(){
		      AlertDialog.Builder myDialog
		      = new AlertDialog.Builder(HealthTipsActivity.this);
		      myDialog.setTitle("DANGER!");
		      myDialog.setMessage("You can do what you want!");
		      myDialog.setPositiveButton("ON", null);
		      myDialog.show();
		     }

		 }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.health_tips, menu);
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
		String[] healthTips = new String[14];
		String[] healthLink = new String[14];
		
		protected void onPreExecute() {
			// before executing the async task display the progress dialog
			dialog = new ProgressDialog(HealthTipsActivity.this);
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
				resultSet = statement.executeQuery("select tip_name,tip_description,tip_link from health_tips");
				int count = 0;

				tipName = new String[14];
				tipDescription = new String[14];
				tipLink = new String[14];			
				
				while (resultSet.next()) {
					tipName[count] = resultSet.getString("tip_name");
					tipDescription[count] = resultSet.getString("tip_description");
					tipLink[count] = resultSet.getString("tip_link");
					healthTips[count] = tipName[count] + "\n" + tipDescription[count];
					healthLink[count] = "Link : " + tipLink[count];
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
			
			gridDisplay = (GridView) findViewById(R.id.gridViewDisplay);
			gridDisplay.setAdapter(new ListAdapter(context, healthTips, healthLink));

		}
	}

}
