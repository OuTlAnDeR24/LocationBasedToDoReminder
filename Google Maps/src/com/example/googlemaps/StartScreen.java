package com.example.googlemaps;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

public class StartScreen extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startscreen);
	
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cool_menu, menu);
		return true;
	}
	

	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		
		case R.id.setalarm:
			Intent i2 = new Intent(StartScreen.this, MainAct.class);
			StartScreen.this.startActivity(i2);
			
			break;
			
		case R.id.rateandreview:
			Intent ir = new Intent(StartScreen.this, About.class);
			StartScreen.this.startActivity(ir);
			
			break;
		case R.id.exit:
		 	finish();
			break;

			
		}
		return false;
	}

	
	public void onClickAdd(View v){
		
		Intent i = new Intent(StartScreen.this, MainAct.class);
		StartScreen.this.startActivity(i);
		
	}
	
	public void onClickAlarmService(View v){
		
		AlarmsList al  = new AlarmsList();
		
		if(al.alarmstitleloc.isEmpty()){
			
				AlertDialog alert = new AlertDialog.Builder(StartScreen.this).create();
				alert.setTitle("No Alarms Found!!");
				alert.setMessage("First Click the Plus Button above to set an Alarm.");
				alert.setButton(alert.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
							
					public void onClick(DialogInterface dialog, int which) {
		
					}
				});
				alert.show();
			
			
		}
		
		else{
			
		Intent i3 = new Intent(StartScreen.this, AlarmsList.class);
		StartScreen.this.startActivity(i3);
		}
		
	}



	
	

}
