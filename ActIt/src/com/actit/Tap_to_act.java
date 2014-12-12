package com.actit;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Tap_to_act extends Activity {
	private Button start_new_game;
	private EditText mycounter;
	private CountDownTimer cdTimer;
	private long total = 60000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tap_to_act);
		
		start_new_game = (Button) findViewById(R.id.create_game);
		start_new_game.setOnClickListener(new OnClickListener() {
			public void onClick(View view){
				ActionBar actionBar = getActionBar();
				actionBar.hide();
				if(start_new_game.getText().equals("Ready!") || start_new_game.getText().equals("Continue")){
					start_new_game.setText("Pause");
					cdTimer = new CountDownTimer(total, 1000) {

					     public void onTick(long millisUntilFinished) {
					    	 if(millisUntilFinished<10){
					    		 total = millisUntilFinished;
					    		 mycounter.setText(" 00:0" + millisUntilFinished / 1000);
					    	 }
					    	 else{
					    		 total = millisUntilFinished;
					    		 mycounter.setText(" 00:" + millisUntilFinished / 1000);
					    	 }
					     }

					     public void onFinish() {
					    	 mycounter.setText(" Times Up!");
					     }
					  }.start();
				}
				else if(start_new_game.getText().equals("Pause")){
					start_new_game.setText("Continue");
					cdTimer.cancel();
				}
				
			}
		});
		
		mycounter = (EditText) findViewById(R.id.editText2);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tap_to_act, menu);
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
	  //start_new_game.performClick();
	}
	
	@Override
	protected void onPause() {
	  super.onPause();
	  cdTimer.cancel();
	}

}
