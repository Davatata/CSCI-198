package com.actit;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity {
	private Button button_new_game;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		button_new_game = (Button) findViewById(R.id.create_game);
		button_new_game.setOnClickListener(new OnClickListener() {
			public void onClick(View view){
				Intent intent = new Intent(Main.this, New_game.class);
		  		startActivity(intent);
			}
		});
		
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
	        	Intent intent = new Intent(this, MainActivity.class);
        		startActivity(intent);
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
