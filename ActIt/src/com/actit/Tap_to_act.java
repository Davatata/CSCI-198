package com.actit;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Tap_to_act extends Activity implements SurfaceHolder.Callback{
	private Button start_new_game;
	private Button end_game;
	private EditText mycounter;
	private CountDownTimer cdTimer;
	private long total = 50000;
	boolean isRunning = false;
	
	int defaultCameraId;
	Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    PictureCallback rawCallback;
    ShutterCallback shutterCallback;
    PictureCallback jpegCallback;
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tap_to_act);
		ActionBar actionBar = getActionBar();
		actionBar.hide();

	
		
		end_game = (Button) findViewById(R.id.button1);
		end_game.setOnClickListener(new OnClickListener() {
			public void onClick(View view){
				// Check if user is recording
				if(isRunning == true){
					// Check if video is under 15 seconds
					if(60 - (total / 1000) < 15){
						Builder alert = new AlertDialog.Builder(Tap_to_act.this);
			            alert.setTitle("Alert");
			            alert.setMessage("Video under 15 seconds. Send?");
			            alert.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// Upload the short video
								sendVideo();
							}
			            });
			            alert.setNegativeButton("No",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// Don't upload
								// Reset video
								Intent intent = getIntent();
								finish();
								startActivity(intent);
							}
			            });
			            alert.show();
					}
					else{
						// Upload the video
						sendVideo();
					}
				}
				else{
					// Alert the user that no video is recorded
		            Builder alert = new AlertDialog.Builder(Tap_to_act.this);
		            alert.setTitle("Alert");
		            alert.setMessage("No video detected");
		            alert.setPositiveButton("OK", null);
		            alert.show();
				}
			}
		});
		
		start_new_game = (Button) findViewById(R.id.create_game);
		start_new_game.setOnClickListener(new OnClickListener() {
			public void onClick(View view){
				
				if(start_new_game.getText().equals("Ready!") || start_new_game.getText().equals("Continue")){
					start_new_game.setText("Pause");
						// Hide "Ready!" button after game begins
					start_new_game.setVisibility(View.INVISIBLE);
					cdTimer = new CountDownTimer(total, 1000) {

					     public void onTick(long millisUntilFinished) {
					    	 isRunning = true;
					    	 if(millisUntilFinished/1000 <10){
					    		 total = millisUntilFinished;
					    		 mycounter.setBackgroundColor(Color.RED);
					    		 mycounter.setText(" 00:0" + total / 1000);
					    	 }
					    	 else{
					    		 total = millisUntilFinished;
					    		 mycounter.setText(" 00:" + total / 1000);
					    	 }
					     }

					     public void onFinish() {
					    	 isRunning = false;
					    	 mycounter.setText(" Times Up!");
					    	 Builder alert2 = new AlertDialog.Builder(Tap_to_act.this);
					            alert2.setTitle("Alert");
					            alert2.setMessage("Times Up! Video Sent.");
					            alert2.setPositiveButton("OK",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										// Upload the video
										finish();
									}
					            });
					            alert2.show();
					     }
					  }.start();
				}
				else if(start_new_game.getText().equals("Pause")){
					start_new_game.setText("Continue");
					cdTimer.cancel();
				}
				
			}
		});
		
		surfaceView = (SurfaceView)findViewById(R.id.surfaceView1);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        rawCallback = new PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.d("Log", "onPictureTaken - raw");
            }
        };
        
        
		
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
	  start_new_game.setText("Continue");
	  cdTimer.cancel();
	}

	// When back is pressed, quit game or restart activity
	@Override public void onBackPressed(){ 
		
        super.onBackPressed();
        quitGame();
	}
	
	
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    }
    
    public void sendVideo(){
    	Builder alert3 = new AlertDialog.Builder(Tap_to_act.this);
        alert3.setTitle("Alert");
        alert3.setMessage("Sending Video!");
        alert3.setPositiveButton("OK",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// Upload the video
				finish();
			}
        });
        alert3.show();
    }
    
    public void quitGame(){
    	Builder alert4 = new AlertDialog.Builder(Tap_to_act.this);
        alert4.setTitle("Alert");
        alert4.setMessage("Quit game?");
        alert4.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// Upload the video
				finish();
			}
        });
        alert4.setNegativeButton("No",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// Reset activity
				Intent intent = getIntent();
				finish();
				startActivity(intent);
			}
        });
        alert4.show();
    }
    
}
