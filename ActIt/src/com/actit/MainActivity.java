package com.actit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.facebook.AppEventsLogger;
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
	  super.onResume();

	  // Logs 'install' and 'app activate' App Events.
	  String appId = getResources().getString(R.string.facebook_app_id);
	  AppEventsLogger.activateApp(getApplicationContext(), appId);
	}
	
	@Override
	protected void onPause() {
	  super.onPause();

	  // Logs 'app deactivate' App Event.
	  AppEventsLogger.deactivateApp(this);
	}

}
