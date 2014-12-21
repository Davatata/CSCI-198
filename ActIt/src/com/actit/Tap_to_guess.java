package com.actit;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class Tap_to_guess extends Activity {

	private Button start_new_game;
	private long total = 15000;
	private EditText mycounter;
	private CountDownTimer cdTimer;
	boolean isRunning = false;
	boolean quitting = false;
	String currentWord = "Dog";
	private ImageButton recordButton;
	protected static final int RESULT_SPEECH = 1;
	private boolean winner = false;
	
	private SpeechRecognizer mSpeechRecognizer;
	private Intent mSpeechRecognizerIntent; 
	private boolean mIslistening; 
	
	protected class SpeechRecognitionListener implements RecognitionListener
	{

	    @Override
	    public void onBeginningOfSpeech()
	    {               
	        //Log.d(TAG, "onBeginingOfSpeech"); 
	    }

	    @Override
	    public void onBufferReceived(byte[] buffer)
	    {

	    }

	    @Override
	    public void onEndOfSpeech()
	    {
	        //Log.d(TAG, "onEndOfSpeech");
	     }

	    @Override
	    public void onError(int error)
	    {
	         

	        //Log.d(TAG, "error = " + error);
	    }

	    @Override
	    public void onEvent(int eventType, Bundle params)
	    {

	    }

	    @Override
	    public void onPartialResults(Bundle partialResults)
	    {

	    }

	    @Override
	    public void onReadyForSpeech(Bundle params)
	    {
	        //Log.d(TAG, "onReadyForSpeech"); //$NON-NLS-1$
	    }

	    @Override
	    public void onResults(Bundle results)
	    {
	    	
	        //Log.d(TAG, "onResults"); //$NON-NLS-1$
	        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
	        // matches are the return values of speech recognition engine
	        // Use these values for whatever you wish to do
	        for (String member : matches){
	            if(member.equals(currentWord)){
	            	winGame();
	            	break;
	            }
	        }
	    }

	    @Override
	    public void onRmsChanged(float rmsdB)
	    {

	    }

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tap_to_guess);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		
		
		
		
		recordButton = (ImageButton) findViewById(R.id.imageButton1);
		recordButton.setVisibility(View.INVISIBLE);
		recordButton.setOnClickListener(new View.OnClickListener() {
			 
            @Override
            public void onClick(View v) {
            	if (!mIslistening)
            	{
            		SpeechRecognitionListener listener = new SpeechRecognitionListener(); 
            		mSpeechRecognizer.setRecognitionListener(listener);
            	    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
            	}
                
            }
        });
		
		super.onCreate(savedInstanceState);
	   
	    mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
	    mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	    mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	    mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
		
		start_new_game = (Button) findViewById(R.id.create_game);
		start_new_game.setOnClickListener(new OnClickListener() {
			public void onClick(View view){
				
				if(start_new_game.getText().equals("Give Up")){
					//start_new_game.setVisibility(View.INVISIBLE);
					quitGame();
				}
				
				else{
					start_new_game.setText("Give Up");
					recordButton.setVisibility(View.VISIBLE);
					cdTimer = new CountDownTimer(total, 1000) {
					// Start streaming video from YouTube
						
						
				     public void onTick(long millisUntilFinished) {
				    	 isRunning = true;
				    	 if(winner == true){
				    		 
				    	 }
				    	 else if(millisUntilFinished/1000 <10){
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
				    	 if(winner == true){
				    		 
				    	 }
				    	 else{
				    	 mycounter.setText(" Times Up!");
				    	 Builder alert = new AlertDialog.Builder(Tap_to_guess.this);
				            alert.setTitle("Alert");
				            alert.setMessage("Times Up! The word was " + currentWord);
				            alert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									// Close YouTube video
									finish();
								}
				            });
				            alert.show();
				    	 }
				     }
				     
					}.start();
				}
			}
		});
		mycounter = (EditText) findViewById(R.id.editText2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tap_to_guess, menu);
		return true;
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
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		if (mSpeechRecognizer != null)
		{
		        mSpeechRecognizer.destroy();
		}
	}

	// When back is pressed, quit game or restart activity
	@Override public void onBackPressed(){ 
		
        super.onBackPressed();
        //quitGame();
	}
	
	public void giveUp(){
		// Show the player what the word was and go back to New Game
    	Builder alert2 = new AlertDialog.Builder(Tap_to_guess.this);
        alert2.setTitle("The word was");
        alert2.setMessage(currentWord);
        alert2.setPositiveButton("OK",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				finish();
			}
        });
        alert2.show();
    }
	
	public void quitGame(){
		
    	Builder alert4 = new AlertDialog.Builder(Tap_to_guess.this);
        alert4.setTitle("Alert");
        alert4.setMessage("Quit game?");
        alert4.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// Upload the video to YouTube
				giveUp();
			}
        });
        alert4.setNegativeButton("No",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// Continue Game
				
			}
        });
        
        alert4.show();
    }

public void winGame(){
		winner = true;
    	Builder alert5 = new AlertDialog.Builder(Tap_to_guess.this);
        alert5.setTitle("You win!");
        alert5.setMessage("The word was " + currentWord);
        alert5.setPositiveButton("OK",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// End the game
				finish();
			}
        });
        
        alert5.show();
    }
}
