package com.example.googlemaps;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.Overlay;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.googlemaps.Alarms;

public class MainAct extends FragmentActivity implements LocationListener {
	
	private GoogleMap map;
	public String loc;
	public static ArrayList<LatLng> latLng = new ArrayList<LatLng>();
	int status,flag=0,set=0,ref=0;
	public static ArrayList<Double> range = new ArrayList<Double>();


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
        status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        // Getting reference to the SupportMapFragment of activity_main.xml
        
       SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

	
       if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
			  
           int requestCode = 10;
           Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
           dialog.show();

       }else { // Google Play Services are available

           // Getting GoogleMap object from the fragment
          map = fm.getMap();
 
       } 	 
	}

	public void OnClickFind(View v){

	 EditText address = (EditText) findViewById(R.id.AddBar);
	  
	 if(address.getText().toString().isEmpty()){
			  Toast mtoast = Toast.makeText(MainAct.this,"Please enter some detail in text box and try again.." , Toast.LENGTH_LONG);
			  mtoast.show(); 
			
		        }
			  
	 else if(!(address.getText().toString().isEmpty())){
		 flag=1;
		 findthis();
	 }
	}
	
	public void findthis(){	 
		 
		EditText address = (EditText) findViewById(R.id.AddBar);
		address.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
	 
		
			 
/*			 if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
			  
	            int requestCode = 10;
	            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
	            dialog.show();
	            	 
	        }*/
//			 else{
				 
						new FindPlace().execute(address.getText().toString());      
						
				 
/*		Geocoder gc = new Geocoder(MainAct.this,Locale.getDefault());
		 if(gc.isPresent()){
				 
	   try{ 
    	List<Address> add = gc.getFromLocationName(address.getText().toString(), 1);
	       
	    latLng = new LatLng(add.get(0).getLatitude(),add.get(0).getLongitude());
	     // Showing the current location in Google Map
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
 
        // Zoom in the Google Map
        map.animateCamera(CameraUpdateFactory.zoomTo(6));
        map.addMarker(new MarkerOptions().position(latLng).title(add.get(0).getAdminArea()));
        
        String display="";
        	
        	if (add.size()>0){
        		for(int i=0;i<add.get(0).getMaxAddressLineIndex();i++){
        			display += add.get(0).getAddressLine(i) + "\n";
        		}
        	    Toast mtoast = Toast.makeText(MainAct.this, display, Toast.LENGTH_SHORT);
        		mtoast.show();
        		address.setText(display);
        	}
        
	 }  catch (IOException e) {
		// TODO Auto-generated catch block
			AlertDialog alert = new AlertDialog.Builder(MainAct.this).create();
			alert.setTitle("Server Error!!");
			alert.setMessage("Please try after some time!!..or Check if you have your data services are activated.");
			alert.setButton(alert.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
						
				public void onClick(DialogInterface dialog, int which) {
								
				}
			});
			alert.show();			
		
	}
	   finally {
		   
	   }
	   
	 }
		 else{
		        
			    Toast mtoast = Toast.makeText(MainAct.this,"Cannot connect to server..Please try after some time..", Toast.LENGTH_LONG);
				mtoast.show();
		 }*/
				 
		

	 }
		
	 private class FindPlace extends AsyncTask<String,Void, LatLng> {
		 
		 ProgressDialog pdLoading = new ProgressDialog(MainAct.this);
	 	     String add=null;


		    @Override
		    protected void onPreExecute() {
		        super.onPreExecute();

		        //this method will be running on UI thread
		        pdLoading.setMessage("\tLoading...");
		        pdLoading.show();
		    }
		    
	     protected LatLng doInBackground(String... url) {
	    	String uri = "http://maps.google.com/maps/api/geocode/json?address=" + url[0] + "&sensor=false";
	    	HttpGet httpGet = new HttpGet(uri);
	 	    HttpClient client = new DefaultHttpClient();
	 	    HttpResponse response;
	 	    StringBuilder stringBuilder = new StringBuilder();
	 	    try {
	 	        response = client.execute(httpGet);
	 	        HttpEntity entity = response.getEntity();
	 	        InputStream stream = entity.getContent();
	 	        int b;
	 	        

	 	        while ((b = stream.read()) != -1) {
	 	            stringBuilder.append((char) b);
	 	        }
	 	    } catch (ClientProtocolException e) {
	 	        e.printStackTrace();
	 	    } catch (IOException e) {
	 	        e.printStackTrace();
	 	    }

	 	    JSONObject jsonObject = new JSONObject();
	 	    try {
	 	        jsonObject = new JSONObject(stringBuilder.toString());

	 	       double lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
	 	            .getJSONObject("geometry").getJSONObject("location")
	 	            .getDouble("lng");

	 	       double lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
	 	            .getJSONObject("geometry").getJSONObject("location")
	 	            .getDouble("lat");
	 	       
	 	       add = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getString("formatted_address");	       
	 	       LatLng ll = new LatLng (lat,lng);
	 	//        add = url[0];
	 	       return ll;

	 	    } catch (JSONException e) {

	 			return null;
	 	    }
			
	 	       
	     }

	   

	     protected void onPostExecute(LatLng result) {
	         pdLoading.dismiss();
	         latLng.add(result);
	         if(result !=null)
	         {
	         if(add ==null ){
	        	 Toast mtoast = Toast.makeText(MainAct.this, "No Location Found..!!..Try with some other name..", Toast.LENGTH_SHORT);
	        	 mtoast.show();
	         }
	         else{
	        	loc = add;
	        	EditText addr = (EditText) findViewById(R.id.AddBar);
	        	addr.setText(add);
	//        	AlarmService as = new AlarmService(latLng);
	        	  // Showing the current location in Google Map
		        map.moveCamera(CameraUpdateFactory.newLatLng(result));
		 
		        // Zoom in the Google Map
		        map.animateCamera(CameraUpdateFactory.zoomTo(6));
	        	Toast mtoast = Toast.makeText(MainAct.this, add, Toast.LENGTH_SHORT);
	        	 mtoast.show();
	    	
	         }

	     }
	         else {
	        	 Toast mtoast = Toast.makeText(MainAct.this, "Something is happening!!..", Toast.LENGTH_SHORT);
	     		 mtoast.show();
	         }
	     }
	     
	     
	    	 
	     }
	 
	
	public void OnClickSetAlarm(View v){
			
		 EditText address = (EditText) findViewById(R.id.AddBar);
		 loc = address.getText().toString();

		 if(loc.isEmpty()){
				  Toast mtoast = Toast.makeText(MainAct.this,"Please enter some detail in text box and try again.." , Toast.LENGTH_LONG);
				  mtoast.show(); 
				
			        }
		 else{
		if(flag==0){
			  Toast mtoast = Toast.makeText(MainAct.this,"First Click 'Find' and then 'Set Alarm'." , Toast.LENGTH_LONG);
			  mtoast.show(); 
		}
		else if(flag==1){
			
			Alarms al = new Alarms();
			al.allocate(loc,latLng);
			Intent i = new Intent(MainAct.this, Alarms.class);
			MainAct.this.startActivity(i);
			flag=0;
			
			AlarmsList als = new AlarmsList();
			int temp = als.check();
			
//			findMe();
			
			if(temp==1){							// If AlarmService has not started yet.
			Intent service = new Intent(MainAct.this, AlarmService.class);
			MainAct.this.startService(service);
			}
			finish();
		
			}

	}
	}
	
	public void set(String title, double range, String loc){
		AlarmsList alarmlist = new AlarmsList(loc,title);
		AlarmService alarmservice = new AlarmService(range,loc,title);
		

	}
	
	public void takethis(double ra){
		range.add(ra);
	}
	
	public void findMe(){
		
		
		   
	       
		   if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
				  
			   Toast mtoast = Toast.makeText(MainAct.this,"Google-Play Services are not available!!" , Toast.LENGTH_LONG);
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
//          onLocationChanged(location);

		
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
			
			Intent in = new Intent(MainAct.this, AlarmReceiver.class);
			MainAct.this.startActivity(in);
			
		}
		break;

		}
	}
	
	public int give(){					// This method gives the corresponding "INDEX" of the alarm to be rang, to the AlarmReceiver class.
		return ref;
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
/*	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}*/

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
	

