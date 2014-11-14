package com.actit;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.facebook.Session;

import android.support.v4.app.Fragment;

import com.fragments.MainFragment;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends FragmentActivity  {
	private MainFragment mainFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
	    if (savedInstanceState == null) {
	        // Add the fragment on initial activity setup
	        mainFragment = new MainFragment();
	        getSupportFragmentManager()
	        .beginTransaction()
	        .add(android.R.id.content, mainFragment)
	        .commit();
	    } else {
	        // Or set the fragment from restored state info
	        mainFragment = (MainFragment) getSupportFragmentManager()
	        .findFragmentById(android.R.id.content);
	    }
	}
		
	private void openSettings() {
		//  Auto-generated method stub
		//  Just stay on page
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
			getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	            openSettings();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	protected void onResume() {
	  super.onResume();

	  // Logs 'install' and 'app activate' App Events.
	  String appId = getResources().getString(R.string.facebook_app_id);
	  AppEventsLogger.activateApp(getApplicationContext(), appId);
	  /*
	  Session session = Session.getActiveSession();
  	  if (session != null && session.isOpened()){
  		  Intent intent = new Intent(this, Main.class);
  		  startActivity(intent);
  	  }*/
	}
	
	@Override
	protected void onPause() {
	  super.onPause();

	  // Logs 'app deactivate' App Event.
	  AppEventsLogger.deactivateApp(this);
	}

}
