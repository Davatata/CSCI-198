package com.actit;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Tap_to_guess extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tap_to_guess);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tap_to_guess, menu);
		return true;
	}

}
