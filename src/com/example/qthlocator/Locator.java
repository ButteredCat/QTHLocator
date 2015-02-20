package com.example.qthlocator;

import java.text.DecimalFormat;
import java.util.Locale;

import android.location.Location;


public class Locator extends Location {
	
	private DecimalFormat latitudeFormat;
	private DecimalFormat accuracyFormat;

	public Locator(String provider) {
		super(provider);
		// TODO Auto-generated constructor stub
		initFormat();
	}

	public Locator(Location l) {
		super(l);
		// TODO Auto-generated constructor stub
		initFormat();
	}

	private void initFormat() {
		latitudeFormat=new DecimalFormat("0.000");
		accuracyFormat=new DecimalFormat("#0");
	}
	
	public String getLatitudeString()
	{
		String[] lat=convert(getLatitude(), Location.FORMAT_SECONDS).split(":");
		return lat[0]+"бу"+lat[1]+"'"+latitudeFormat.format(Double.valueOf(lat[2]))+"\"";
	}

	public String getLongitudeString()
	{
		String[] lon=convert(getLongitude(), Location.FORMAT_SECONDS).split(":");
		return lon[0]+"бу"+lon[1]+"'"+latitudeFormat.format(Double.valueOf(lon[2]))+"\"";
	}
	
	public String getAltitudeString()
	{
		return accuracyFormat.format(getAltitude()) + "m";
	}
	
	public String getAccuracyString()
	{
		return accuracyFormat.format(getAccuracy()) + "m";
	}
	
	public String getMaidenhead()
	{
    	double lon=getLongitude()+180;
    	double lat=getLatitude()+90;
    	String alphabet="ABCDEFGHIJKLMNOPQRSTUVWX";
    	
    	int field1=(int)lon/20;
    	int field2=(int)lat/10;
    	int square1=(int)(lon%20)/2;
    	int square2=(int)(lat%10);
    	int subsquare1=(int)((lon-(int)(lon/2)*2)*12);
    	int subsquare2=(int)((lat-(int)(lat/1)*1)*24);
    	
    	return alphabet.substring(field1, field1+1) + alphabet.substring(field2, field2+1)
    			+ square1 + square2
    			+ alphabet.substring(subsquare1, subsquare1+1).toLowerCase(Locale.ENGLISH)
    			+ alphabet.substring(subsquare2, subsquare2+1).toLowerCase(Locale.ENGLISH);
	}
	
	public Coordinate convertFromMaidenhead(String q)
	{
		q=q.toUpperCase(Locale.ENGLISH);
		// field
		double lon=(q.charAt(0)-'A')*20-180;
		double lat=(q.charAt(1)-'A')*10-90;
		// square
		lon+=(q.charAt(2)-'0')*2;
		lat+=(q.charAt(3)-'0');
		// subsquare
		lon+=(q.charAt(4)-'A')/12;
		lat+=(q.charAt(5)-'A')/24;
		// move to center of subsquare
		lon+=2.5/60;
		lat+=1.25/60;
		
		return new Coordinate(lat, lon);
	}
	
	public float distanceTo(Coordinate postion)
	{
		float[] results=new float[3];
		Location.distanceBetween(getLatitude(), getLongitude(), postion.getLat(), postion.getLon(), results);
		return results[0];
	}
	
	public static class Coordinate
	{
		private double latitude;
		private double longitude;
		
		public Coordinate(double lat, double lon)
		{
			latitude=lat;
			longitude=lon;
		}
		
		public double getLat() { return latitude;}
		public double getLon() { return longitude;}
	}
}
