package com.example.qthlocator;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;



public class MainActivity extends Activity {
	
	private ActionBar actionBar;
    private LocationManager lm;
    private PositionFragment aPositionFragment;
    private SearchFragment aSearchFragment;
    private static final String TAG="MainActivity"; 
    //位置监听
    private LocationListener locationListener=new LocationListener() {

        public void onLocationChanged(Location location) {
            aPositionFragment.updateView(location);
            aPositionFragment.setLastKnownLocator(new Locator(location));
        }
        
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
   
        public void onProviderEnabled(String provider) {
            Location location = lm.getLastKnownLocation(provider);
            	aPositionFragment.updateView(location);
        }
   
        public void onProviderDisabled(String provider) {

        	aPositionFragment.updateView(null);
        }   
    };
    
    
   

    private Criteria getCriteria(){
        Criteria criteria=new Criteria();
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);   
        //设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费  
        criteria.setCostAllowed(true);
        //设置是否需要方位信息
        criteria.setBearingRequired(false);
        //设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求  
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	
    	lm.removeUpdates(locationListener);
    }
    
    @Override
    protected void onStart() {
    	super.onStart();

    	lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		actionBar=getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		aPositionFragment=new PositionFragment();
		Tab tab1=actionBar.newTab();
		tab1.setText("QTH");
		tab1.setTabListener(new PositionListener(aPositionFragment));
		actionBar.addTab(tab1);
		Log.i(TAG, "tab1 created");
		
		aSearchFragment=new SearchFragment();
		Tab tab2=actionBar.newTab();
		tab2.setText("QTH?");
		tab2.setTabListener(new SearchListener(aSearchFragment));
		actionBar.addTab(tab2);
		Log.i(TAG, "tab2 created");
        
        lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        
        //判断GPS是否正常启动
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "请开启GPS导航...", Toast.LENGTH_SHORT).show();
            //返回开启GPS导航设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);   
            startActivityForResult(intent,0);
            return;
        }
        
        //为获取地理位置信息时设置查询条件
        String bestProvider = lm.getBestProvider(getCriteria(), false);
        //获取位置信息
        //如果不设置查询要求，getLastKnownLocation方法传人的参数为LocationManager.GPS_PROVIDER
        Location location = lm.getLastKnownLocation(bestProvider);
        if(location==null)
        	Log.i(TAG, "lastKnownLocation returned a null pointer");
        else
        	Log.i(TAG, "the best provider is "+bestProvider);
        aPositionFragment.setLastKnownLocator(new Locator(location));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	class PositionListener implements android.app.ActionBar.TabListener {
		private PositionFragment positionFragment;
		
		public PositionListener(PositionFragment pf) {
			this.positionFragment=pf;
		}
		
		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			
		}
		
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			ft.add(R.id.layout, positionFragment, null);
		}
		
		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			ft.remove(positionFragment);
		}
	}
	
	class SearchListener implements android.app.ActionBar.TabListener {
		private SearchFragment searchFragment;
		
		public SearchListener(SearchFragment sf) {
			this.searchFragment=sf;
		}
		
		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			
		}
		
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			ft.add(R.id.layout, searchFragment, null);
		}
		
		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			ft.remove(searchFragment);
		}
	}

}
