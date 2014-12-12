package com.actit;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
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
	private long total = 60000;
	
	int defaultCameraId;
	Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    PictureCallback rawCallback;
    ShutterCallback shutterCallback;
    PictureCallback jpegCallback;
    private final String tag = "VideoServer";	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tap_to_act);
		ActionBar actionBar = getActionBar();
		actionBar.hide();

	
		
		end_game = (Button) findViewById(R.id.button1);
		end_game.setOnClickListener(new OnClickListener() {
			public void onClick(View view){
				finish();
			}
		});
		
		start_new_game = (Button) findViewById(R.id.create_game);
		start_new_game.setOnClickListener(new OnClickListener() {
			public void onClick(View view){
				
				if(start_new_game.getText().equals("Ready!") || start_new_game.getText().equals("Continue")){
					start_camera();
					start_new_game.setText("Pause");
					cdTimer = new CountDownTimer(total, 1000) {

					     public void onTick(long millisUntilFinished) {
					    	 if(millisUntilFinished/1000 <=10){
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
					    	 mycounter.setText(" Times Up!");
					     }
					  }.start();
				}
				else if(start_new_game.getText().equals("Pause")){
					stop_camera();
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
	  start_camera();
	  //start_new_game.performClick();
	}
	
	@Override
	protected void onPause() {
	  super.onPause();
	  start_new_game.setText("Continue");
	  cdTimer.cancel();
	}

	// When back is pressed from Settings, hide app
	@Override public void onBackPressed(){ 
		moveTaskToBack(true); 
	}
	
	private void start_camera()
    {
		
        try{
            camera = Camera.open(CameraInfo.CAMERA_FACING_FRONT);
        }catch(RuntimeException e){
            Log.e(tag, "init_camera: " + e);
            return;
        }
        
        Camera.Parameters param;
        param = camera.getParameters();
        //modify parameter
        param.setPreviewFrameRate(20);
        param.setPreviewSize(1280, 720);
        camera.setParameters(param);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
            //camera.takePicture(shutter, raw, jpeg)
        } catch (Exception e) {
            Log.e(tag, "init_camera: " + e);
            return;
        }
        camera.setDisplayOrientation(90);
    }
	
	private void stop_camera()
    {
        camera.stopPreview();
        camera.release();
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
    
    // Swap camera but unimplemented
    public void swapCamera(){
    	camera.stopPreview();
        camera.release();
    	int numberOfCameras = camera.getNumberOfCameras();
    	
    	if(numberOfCameras > 1){
    		CameraInfo cameraInfo = new CameraInfo();
    		for (int i = 0; i < numberOfCameras; i++) {
                Camera.getCameraInfo(i, cameraInfo);
                if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                    start_camera();
                }
                else if(cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT)
                	start_camera();
            }
    	}
    	
    }
    
}
