package com.example.cmpe285healthproject;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;



public class MainActivity extends ActionBarActivity {

	WebView webView;
	
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		webView = (WebView)findViewById(R.id.webView);

	    WebSettings webSettings = webView.getSettings();
	    webSettings.setJavaScriptEnabled(true);
	    webView.getSettings().setLoadsImagesAutomatically(true);

	    webSettings.setDomStorageEnabled(true);

		final MyJavaScriptInterface myJavaScriptInterface
        = new MyJavaScriptInterface(this);
		webView.addJavascriptInterface(myJavaScriptInterface, "AndroidFunction");
      
		webView.getSettings().setJavaScriptEnabled(true); 
		webView.loadUrl("file:///android_asset/index.html");

		
		
		//new DoInBackground().execute();
		
			 //rvowyysewd.database.windows.net:1433;database=HealthCare;user=team14@rvowyysewd;
			 //password={your_password_here};encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;

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
	
	
	public class MyJavaScriptInterface {
		  Context mContext;

		     MyJavaScriptInterface(Context c) {
		         mContext = c;
		     }
		    
		     public void showToast(String toast){
		         Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
		     }
		    
		     public void launchHospital(){
		     Intent i = new Intent(MainActivity.this,
						HospitalActivity.class);
				MainActivity.this.startActivity(i);
		     }
		     public void launchPhysician(){
			     Intent i = new Intent(MainActivity.this,
							PhysicianActivity.class);
					MainActivity.this.startActivity(i);
			     }
		     public void launchAppointment(){
		    	 
			     Intent i = new Intent(MainActivity.this,
							LoginActivity.class);
					MainActivity.this.startActivity(i);
			     }
		     public void launchHelpline(){
			     Intent i = new Intent(MainActivity.this,
							HelplineActivity.class);
					MainActivity.this.startActivity(i);
			     }
		     public void launchAmbulance(){
			     Intent i = new Intent(MainActivity.this,
							AmbulanceActivity.class);
					MainActivity.this.startActivity(i);
			     }
		     public void launchHealthTips(){
			     Intent i = new Intent(MainActivity.this,
							HealthTipsActivity.class);
					MainActivity.this.startActivity(i);
			     }
		     public void launchParking(){
			     Intent i = new Intent(MainActivity.this,
							ParkingActivity.class);
					MainActivity.this.startActivity(i);
			     }
		     public void launchLogin(){
			     Intent i = new Intent(MainActivity.this,
							LoginActivity.class);
					MainActivity.this.startActivity(i);
			     }
		     public void launchSignUp(){
			     Intent i = new Intent(MainActivity.this,
							SignUpActivity.class);
					MainActivity.this.startActivity(i);
			     }
		     
		     
		     
		     public void callPhone(String phone){
		 		final Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
					String strPhone = phone;	//changing into string
					phoneCallIntent.setData(Uri.parse("tel:" + strPhone));
					startActivity(phoneCallIntent); // make a phone call
			     }
		     public void openFacebook(){
		     Intent intent = new Intent("android.intent.category.LAUNCHER");
		     intent.setClassName("com.facebook.katana", "com.facebook.katana.LoginActivity");
		     startActivity(intent);
		     }
		     public void openTwitter(){
		    	 try {
		    		   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + "iamharleen")));
		    		}catch (Exception e) {
		    		   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" + "iamharleen")));
		    		}
		     }
		     
		     public void openAndroidDialog(){
		      AlertDialog.Builder myDialog
		      = new AlertDialog.Builder(MainActivity.this);
		      myDialog.setTitle("DANGER!");
		      myDialog.setMessage("You can do what you want!");
		      myDialog.setPositiveButton("ON", null);
		      myDialog.show();
		     }

		 }
	
	
	private class DoInBackground extends AsyncTask<Void, Void, Void> implements
			DialogInterface.OnCancelListener {
		private ProgressDialog dialog;

		protected Void doInBackground(Void... unused) {
			//String connectionUrl = "jdbc:jtds:sqlserver://rvowyysewd.database.windows.net:1433/HealthCare;encrypt=false;instance=SQLEXPRESS;";
			try {
				
			/*	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				String connectionUrl = "jdbc:sqlserver://rvowyysewd.database.windows.net:1433;"
				+ "database=HealthCare;"
				+ "user=team14@rvowyysewd;"
				+ "password=Cmpe_285;"
				+ "integratedSecurity=true;"
				//+ "ssl=require";
				+ "encrypt=true" + ";" + "hostNameInCertificate=*.database.windows.net" + ";" +
				"trustServerCertificate=false" + ";" + "loginTimeout=30";
				Connection conn = DriverManager.getConnection(connectionUrl);*/
				
				Class.forName("com.mysql.jdbc.Driver");
			      // setup the connection with the DB.
				Connection connect = DriverManager
			          .getConnection("jdbc:mysql://us-cdbr-azure-west-a.cloudapp.net/as_4a21b51c683787a?"
			              + "user=b4b87dac9133e2&password=23a80e41");

			      // statements allow to issue SQL queries to the database
			      Statement statement = connect.createStatement();
			      ResultSet resultSet = null;
			      resultSet = statement
			              .executeQuery("select * from statistics");
			      while (resultSet.next()) {
			          // it is possible to get the columns via name
			          // also possible to get the columns via the column number
			          // which starts at 1
			          // e.g., resultSet.getSTring(2);
			          String user1 = resultSet.getString("vmname");
				      System.out.println("hi " + user1);
			      }
			      System.out.println("hi " + resultSet);
			      
			/*	Class.forName("net.sourceforge.jtds.jdbc.Driver");
				//Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				String connectionUrl ="jdbc:jtds:sqlserver://rvowyysewd.database.windows.net:1433;database=HealthCare;user=team14@rvowyysewd;password=Cmpe_285;ssl=request;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
				//String connectionUrl = "jdbc:sqlserver://rvowyysewd.database.windows.net:1433;database=HealthCare;integratedSecurity=true;";
				Connection conn = DriverManager.getConnection(connectionUrl);
				
				//java.sql.Connection conn = DriverManager.getConnection(connectionUrl,"team14","Cmpe_285");
				
				//String connUrl="jdbc:jtds:sqlserver://rvowyysewd.database.windows.net:1433/HealthCare;user=team14;passward=Cmpe_285;";
				//java.sql.Connection conn=DriverManager.getConnection(connUrl);
			     
				Statement statement=conn.createStatement();*/
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			 
			return null;
		}

		protected void onPostExecute(Void unused) {
			dialog.dismiss();
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			
		}
	}
	
}
