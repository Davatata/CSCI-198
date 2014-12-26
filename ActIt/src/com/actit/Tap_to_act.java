package com.actit;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Video;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
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
import android.widget.Toast;

public class Tap_to_act extends Activity implements SurfaceHolder.Callback{
	private Button start_new_game;
	private Button end_game;
	private EditText mycounter;
	private CountDownTimer cdTimer;
	private long total = 50000;
	boolean isRunning = false;
	int defaultCameraId;
	private String word = "default";
	
	
	File video;
	boolean previewing = false;
	Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    PictureCallback rawCallback;
    ShutterCallback shutterCallback;
    PictureCallback jpegCallback;
    private final String tag = "VideoServer";
    public MediaRecorder mrec;
    private String path = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tap_to_act);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		
		Intent intent = getIntent();
		word = intent.getExtras().getString("chosen_word");
		Log.i(null, "The word is " + word);
	
		// "Done" button when user is ready to send
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
			            alert.setCancelable(false);
			            alert.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// Upload the short video
								stopRecording();
								sendVideo();
							}
			            });
			            alert.setNegativeButton("No",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// Don't upload
								// Reset video
								//Intent intent = getIntent();
								//finish();
								//startActivity(intent);
							}
			            });
			            alert.show();
					}
					else{
						// Upload the video
						stopRecording();
						sendVideo();
					}
				}
				else{
					// Alert the user that no video is recorded
		            Builder alert = new AlertDialog.Builder(Tap_to_act.this);
		            alert.setTitle("Alert");
		            alert.setMessage("No video detected");
		            alert.setCancelable(false);
		            alert.setPositiveButton("OK", null);
		            alert.show();
				}
			}
		});
		
		// "Ready!" button when user wants to start recording
		start_new_game = (Button) findViewById(R.id.create_game);
		start_new_game.setOnClickListener(new OnClickListener() {
			public void onClick(View view){
				try {
		            if(!previewing)
		            {
		                startRecording();
		            }

		        } catch (Exception e) {
		            String message = e.getMessage();
		            Log.i(null, "Problem Start" + message);
		            mrec.release();
		        }
				if(start_new_game.getText().equals("Ready!") || start_new_game.getText().equals("Continue")){
					start_new_game.setText("Pause");
						// Hide "Ready!" button after game begins
					start_new_game.setText(word);
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
					            alert2.setMessage("Times Up!");
					            alert2.setCancelable(false);
					            alert2.setPositiveButton("OK",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										// Upload the video
										sendVideo();
										
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
		
		Log.i(null, "Video starting");
		camera = Camera.open(1); // attempt to get a Camera instance
		
		
		
		
		
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
	  //surfaceView.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected void onPause() {
	  super.onPause();
	  //surfaceView.setVisibility(View.GONE);
	  //start_new_game.setText("Continue");
	  cdTimer.cancel();
	}

	// When back is pressed
	@Override public void onBackPressed(){ 
		// Do nothing
	}
	
	protected void startRecording() throws IOException {
		File dir = File.createTempFile("act_it_+" + word + "+", ".mp4", Environment.getExternalStorageDirectory());
		
		
		//File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		path = dir.getAbsolutePath();
		Log.i(null, path);
		
		
		mrec = new MediaRecorder(); // Works well
		camera.unlock();

	    mrec.setCamera(camera);

	    mrec.setVideoSource(MediaRecorder.VideoSource.CAMERA);
	    mrec.setAudioSource(MediaRecorder.AudioSource.MIC);

	    mrec.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
	    //mrec.setOutputFile("/storage/emulated/0/DCIM/Camera/myvideo.mp4");
	    mrec.setOutputFile(dir.getAbsolutePath());
	    mrec.setPreviewDisplay(surfaceHolder.getSurface());
	    
	    mrec.prepare();
	    mrec.start();
	    previewing = true;
	}

	protected void stopRecording() {
	    mrec.stop();
	    mrec.reset();
	    mrec.release();
	    mrec = null;
	    camera.release();
	}
	
	
	
	
	
	
	
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    	if (camera != null) {
    		camera.setDisplayOrientation(90);
            Camera.Parameters params = camera.getParameters();
            //params.setPreviewSize(surfaceView.getHeight(), surfaceView.getWidth());
            camera.setParameters(params);
            
        } else {
            Toast.makeText(getApplicationContext(), "Camera not available!",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    	//camera.stopPreview();
    	//camera.release();
    }
    
    // Send video to YouTube
    public void sendVideo(){
    	Builder alert3 = new AlertDialog.Builder(Tap_to_act.this);
        alert3.setTitle("Alert");
        alert3.setMessage("Sending Video!");
        alert3.setCancelable(false);
        alert3.setPositiveButton("OK",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// Upload the video
				uploadYouTube();
				finish();
			}
        });
        alert3.show();
    }
    
    // Currently unused
    public void quitGame(){
    	Builder alert4 = new AlertDialog.Builder(Tap_to_act.this);
        alert4.setTitle("Alert");
        alert4.setMessage("Quit game?");
        alert4.setCancelable(false);
        alert4.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// Upload the video
				stopRecording();
				finish();
			}
        });
        alert4.setNegativeButton("No",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// Reset activity
				//stopRecording();
				//Intent intent = getIntent();
				//finish();
				//startActivity(intent);
			}
        });
        alert4.show();
    }
    
    public void uploadYouTube(){
    	Log.i(null, "Here we upload");
    }
    
}
