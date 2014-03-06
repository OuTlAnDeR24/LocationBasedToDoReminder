package com.example.googlemaps;

import java.util.ArrayList;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Alarms extends Activity implements LocationListener {
	
	public static String locat,Title;
	public int status,ref;
	public static double Range;
	public static ArrayList<LatLng> latLng = new ArrayList<LatLng>();
	public static ArrayList<Double> range = new ArrayList<Double>();
	
	public void allocate(String loc,ArrayList<LatLng> l){
			
			locat = loc;
			latLng = l;
		}
	
	public Alarms(){
		
	}

	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarms);

		TextView location = (TextView) findViewById(R.id.editlocation);
		location.setText(locat);

/*		RelativeLayout layout = (RelativeLayout) findViewById(R.layout.alarms);
		
		layout.setOnTouchListener(new OnTouchListener()
		{
		    
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				 hideKeyboard(arg0);
				return false;
			}
		});*/
		
		}
	
/*	protected void hideKeyboard(View view)
	{
	    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	    in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}*/
	
	
	public void onClickbtnSet(View v){
		
		EditText title = (EditText) findViewById(R.id.edittitle);
		EditText rangeui = (EditText) findViewById(R.id.editrange);
		Title = title.getText().toString();
		MainAct ma = new MainAct();
		if(rangeui.getText().toString().isEmpty()){
			AlertDialog alert = new AlertDialog.Builder(Alarms.this).create();
			alert.setTitle("Alert!!");
			alert.setMessage("Please fill in How many metres before the destination you want the alarm to ring..");
			alert.setButton(alert.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
						
				public void onClick(DialogInterface dialog, int which) {
								
				}
			});
			alert.show();
			
		}
		else{
			if(Title.isEmpty()){
				Title = "No Title.";
			}
	    Range = Double.parseDouble(rangeui.getText().toString());
	    ma.set(Title,Range, locat);
	    Toast mtoast = Toast.makeText(Alarms.this,"Alarm has been set..:)", Toast.LENGTH_LONG);
	    mtoast.show();
	    range.add(Range);
	    findMe();
//	    ma.takethis(Range);
	    
		}
	}


	public void findMe(){
		
		
		   
	       
		   if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
				  
			   Toast mtoast = Toast.makeText(Alarms.this,"Google-Play Services are not available!!" , Toast.LENGTH_LONG);
			   mtoast.show(); 
		   }
	           
	     else{
	 
	       // Getting LocationManager object from System Service LOCATION_SERVICE
	       LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	       
	       // Creating a criteria object to retrieve provider
	       Criteria criteria = new Criteria();
	//       criteria.setAccuracy(Criteria.ACCURACY_FINE);

	       // Getting the name of the best provider
	       String provider = locationManager.getBestProvider(criteria, true);

	       // Getting Current Location
	       Location location = locationManager.getLastKnownLocation(provider);
//       onLocationChanged(location);

		
	       if(location!=null){
	           onLocationChanged(location);
	       }
	       locationManager.requestLocationUpdates(provider, 20000,0, this);      //requesting MyLocation Updates in every 20 seconds.    

	     }

	   }

	@Override
	public void onLocationChanged(Location location) {
		double MyLatitude = location.getLatitude();
		double MyLongitude = location.getLongitude();

		LatLng MyLatlng = new LatLng(MyLatitude,MyLongitude);
		
		int i=0;
		for(i=0;i<latLng.size();i++){
		double dist = distance(latLng.get(i),MyLatlng);
		if(dist<range.get(i)){
			ref=i;			
			range.remove(i);
			latLng.remove(i);
			Intent in = new Intent(Alarms.this, AlarmReceiver.class);
			Alarms.this.startActivity(in);
			finish();
		}
		

		

		}
	}

	public double distance(LatLng StartP, LatLng EndP) {
		double Radius = 6371;
	    double lat1 = StartP.latitude/1E6;
	    double lat2 = EndP.latitude/1E6;
	    double lon1 = StartP.longitude/1E6;
	    double lon2 = EndP.longitude/1E6;
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLon = Math.toRadians(lon2-lon1);
	    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
	            + Math.cos(Math.toRadians(lat1))
	            * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
	            * Math.sin(dLon / 2);
	            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	            double temp = Radius * c;
	            temp=temp*0.621;
	       return temp;
	}
	
	public int give(){					// This method gives the corresponding "INDEX" of the alarm to be rang, to the AlarmReceiver class.
		return ref;
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	
		
}
