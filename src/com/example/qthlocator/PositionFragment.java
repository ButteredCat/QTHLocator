package com.example.qthlocator;

import android.app.Fragment;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PositionFragment extends Fragment {

	private View aView;
	private TextView tvLabel, tvLat, tvLon, tvLocLabel, tvLoc, tvAlt, tvAcc;
	private Locator lastKnownLocator;
	private static final String TAG="PositionFragment"; 
	
	public PositionFragment() {
		// TODO Auto-generated constructor stub
	}
	
	private void setView(Locator locator) {
		tvLat.setText(locator.getLatitudeString());
        tvLon.setText(locator.getLongitudeString());
        if(locator.hasAltitude())
        	tvAlt.setText(locator.getAltitudeString());
        if(locator.hasAccuracy())
        	tvAcc.setText(locator.getAccuracyString());
        tvLoc.setText(locator.getMaidenhead());
        Log.i(TAG, "setView called");
	}
	
	private void clearView() {
		tvLat.getEditableText().clear();
        tvLon.getEditableText().clear();
        tvAlt.getEditableText().clear();
        tvAcc.getEditableText().clear();
        tvLoc.getEditableText().clear();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView is called");
		aView=inflater.inflate(R.layout.fragment_position, container, false);
		
		tvLabel=(TextView)aView.findViewById(R.id.tvLabel);
		tvLat=(TextView)aView.findViewById(R.id.tvLat);
		tvLon=(TextView)aView.findViewById(R.id.tvLon);
		tvLocLabel=(TextView)aView.findViewById(R.id.tvLocLabel);
		tvLoc=(TextView)aView.findViewById(R.id.tvLoc);
		tvAlt=(TextView)aView.findViewById(R.id.tvAlt);
		tvAcc=(TextView)aView.findViewById(R.id.tvAcc);
		
		Typeface hintTypeface=Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-BoldCondensed.ttf");
        tvLabel.setTypeface(hintTypeface);
        tvLocLabel.setTypeface(hintTypeface);
        
        if(lastKnownLocator!=null)
        	setView(lastKnownLocator);
        
		return aView;
	}
	
	public void setLastKnownLocator(Locator locator) {
		this.lastKnownLocator=locator;
	}
	
	public void updateView(Location location){
		Log.i(TAG, "updateView is called");
		if(isVisible()) {
	        if(location!=null)
	            setView(new Locator(location));
	        else
	            clearView();
		}
    }
}
