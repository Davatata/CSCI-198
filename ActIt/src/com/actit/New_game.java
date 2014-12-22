package com.actit;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class New_game extends Activity {
	private Button button_new_game;
	private Button guess_new_game;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_game);
		
		button_new_game = (Button) findViewById(R.id.create_game);
		button_new_game.setOnClickListener(new OnClickListener() {
			public void onClick(View view){
				Intent intent = new Intent(New_game.this, Play_game.class);
		  		startActivity(intent);
			}
		});
		
		guess_new_game = (Button) findViewById(R.id.button2);
		guess_new_game.setOnClickListener(new OnClickListener() {
			public void onClick(View view){
				Intent intent = new Intent(New_game.this, Tap_to_guess.class);
		  		startActivity(intent);
			}
		});
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_game, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	        	Intent intent = new Intent(this, MainActivity.class);
        		startActivity(intent);
	        case android.R.id.home:
	            NavUtils.navigateUpFromSameTask(this);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
