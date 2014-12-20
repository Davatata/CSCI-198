package com.fragments;

import java.util.Arrays;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.actit.New_game;
import com.actit.R;

public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";
    private UiLifecycleHelper uiHelper;
    private Button button_continue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);

        LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
        authButton.setFragment(this);
        authButton.setReadPermissions(Arrays.asList("user_friends", "public_profile", "email"));
        
        button_continue = (Button) view.findViewById(R.id.create_game);
        button_continue.setVisibility(View.INVISIBLE);
        button_continue.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view){
				//Toast.makeText(getActivity(), "Continue", Toast.LENGTH_SHORT).show(); //dark writing on bottom of screen when clicked
				Intent intent = new Intent(getActivity(), New_game.class);
		  		startActivity(intent);
			}
		});
        
        return view;
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
    	ActionBar actionBar = getActivity().getActionBar();
        if (state.isOpened()) {
            Log.w(TAG, "Logged in...");
            button_continue.setVisibility(View.VISIBLE);
            actionBar.show();
            
        } else if (state.isClosed()) {
        	Log.w(TAG, "Logged out...");
        	button_continue.setVisibility(View.INVISIBLE);
        	/*
        	android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
        	for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {    
        	    fm.popBackStack();
        	    System.out.println("clear stack");
        	}*/
        	
        	actionBar.hide();
        }
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null && (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }


}