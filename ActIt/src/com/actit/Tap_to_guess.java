package com.actit;

import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
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


import android.widget.MediaController;
import android.widget.VideoView;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.services.samples.youtube.cmdline.Auth;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.Search;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;


public class Tap_to_guess extends Activity  {

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
	private static VideoView videoView;
	
	private static final String PROPERTIES_FILENAME = "youtube.properties";

    private static final long NUMBER_OF_VIDEOS_RETURNED = 5;

    private static YouTube youtube;

	
	
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
	    {	button_to_normal();
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
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		videoView =(VideoView)findViewById(R.id.videoView1);
	    //MediaController mediaController= new MediaController(this);
	   // mediaController.setAnchorView(videoView);
	    String path = "http://www.youtube.com/watch?v=VXPJEj2m6sw";
        Uri uri= Uri.parse(path);
	    videoView.setMediaController(null);
	    videoView.setVideoURI(uri);
	    //videoView.requestFocus();
		
	    
		recordButton = (ImageButton) findViewById(R.id.imageButton1);
		recordButton.setVisibility(View.INVISIBLE);
		recordButton.setOnClickListener(new View.OnClickListener() {
			 
            @Override
            public void onClick(View v) {
            	if (!mIslistening)
            	{	recordButton.setImageResource(R.drawable.microphone_red);
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
				videoView.start();
				if(start_new_game.getText().equals("Give Up")){
					//start_new_game.setVisibility(View.INVISIBLE);
					quitGame();
				}
				
				else{
					start_new_game.setText("Give Up");
					recordButton.setVisibility(View.VISIBLE);
					startStreaming();
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
				            alert.setCancelable(false);
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
		
        //super.onBackPressed();
        //quitGame();
	}
	
	public void giveUp(){
		// Show the player what the word was and go back to New Game
    	Builder alert2 = new AlertDialog.Builder(Tap_to_guess.this);
        alert2.setTitle("The word was");
        alert2.setMessage(currentWord);
        alert2.setCancelable(false);
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
        alert4.setCancelable(false);
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
	
	public void button_to_normal(){
		recordButton.setImageResource(R.drawable.ic_action_microphone);
	}

	public void winGame(){
		winner = true;
    	Builder alert5 = new AlertDialog.Builder(Tap_to_guess.this);
        alert5.setTitle("You win!");
        alert5.setMessage("The word was " + currentWord);
        alert5.setCancelable(false);
        alert5.setPositiveButton("OK",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// End the game
				finish();
			}
        });
        
        alert5.show();
    }
	
	public void startStreaming(){
		try {
            // This object is used to make YouTube Data API requests. The last
            // argument is required, but since we don't need anything
            // initialized when the HttpRequest is initialized, we override
            // the interface and provide a no-op function.
            youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName((String.valueOf(R.string.app_name))).build();

            // Prompt the user to enter a query term.
            String queryTerm = "act";

            // Define the API request for retrieving search results.
            YouTube.Search.List search = youtube.search().list("id,snippet");

            // Set your developer key from the Google Developers Console for
            // non-authenticated requests. See:
            // https://console.developers.google.com/
            String apiKey = "AIzaSyBoywB6T_-cE07FoyC2PMKNw7QvwK0vfuk";
            search.setKey(apiKey);
            search.setQ(queryTerm);
            search.setChannelId("UC4JUIHjQX0JnwV9BVbHw_iQ");

            // Restrict the search results to only include videos. See:
            // https://developers.google.com/youtube/v3/docs/search/list#type
            search.setType("video");

            // To increase efficiency, only retrieve the fields that the
            // application uses.
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

            // Call the API and print results.
            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            int listSize = searchResultList.size();
            
            if (searchResultList != null) {
                prettyPrint(searchResultList.iterator(), queryTerm);
            }
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
	
	private static void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {

        System.out.println("\n=============================================================");
        System.out.println(
                "   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + query + "\".");
        System.out.println("=============================================================\n");

        if (!iteratorSearchResults.hasNext()) {
            System.out.println(" There aren't any results for your query.");
        }

        
        while (iteratorSearchResults.hasNext()) {

            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();
            

            // Confirm that the result represents a video. Otherwise, the
            // item will not contain a video ID.
            if (rId.getKind().equals("youtube#video")) {
                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
                
                
                
                		
                System.out.println(" Video Id" + rId.getVideoId());
                System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
                System.out.println(" Thumbnail: " + thumbnail.getUrl());
                System.out.println("\n-------------------------------------------------------------\n");
            }
        }
    }
	
}
