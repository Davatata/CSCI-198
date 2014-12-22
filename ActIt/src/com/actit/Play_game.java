package com.actit;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Play_game extends Activity {

	private Button first_word;
	private Button second_word;
	private Button third_word;
	private String chosen_word = "default";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_game);
		
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		
		first_word = (Button) findViewById(R.id.button1);
		first_word.setOnClickListener(new OnClickListener() {
			public void onClick(View view){
				chosen_word = (String) first_word.getText();
				create_game(chosen_word);
			}
		});
		
		second_word = (Button) findViewById(R.id.button3);
		second_word.setOnClickListener(new OnClickListener() {
			public void onClick(View view){
				chosen_word = (String) second_word.getText();
				create_game(chosen_word);
			}
		});
		
		third_word = (Button) findViewById(R.id.button4);
		third_word.setOnClickListener(new OnClickListener() {
			public void onClick(View view){
				chosen_word = (String) third_word.getText();
				create_game(chosen_word);
			}
		});
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		
	}
	
	public void create_game(String chosen){
		
		Intent intent = new Intent(Play_game.this, Tap_to_act.class);
		intent.putExtra("chosen_word", chosen);
  		startActivity(intent);
  		finish();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play_game, menu);
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
	
	@Override
	protected void onResume() {
	  super.onResume();
	  
	}
	
	@Override
	protected void onPause() {
	  super.onPause();
	}

	// When back is pressed
	@Override public void onBackPressed(){ 
		// Do nothing
	}

}
