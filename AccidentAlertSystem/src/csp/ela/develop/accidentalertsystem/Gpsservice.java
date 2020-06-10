package csp.ela.develop.accidentalertsystem;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;


public class Gpsservice extends Service implements LocationListener{


	Context myContext;
	Location myLocation;
	LocationManager myLocationmanager;
	double myLatitude,myLongitude;
	boolean mylocationNotfind=false;
	private static final long distance_updates= 10; 
	private static final long time_updates=1000 * 60 * 1;

	public Gpsservice(Context aContext)
	{
		myContext=aContext;
		MyLocation();
	}
	public Location MyLocation()
	{
		try {
			myLocationmanager = (LocationManager) myContext.getSystemService(LOCATION_SERVICE);
			boolean isGPSEnabled =myLocationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			boolean isNetworkEnabled = myLocationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			if (!isGPSEnabled && !isNetworkEnabled) {}
			else {
				this.mylocationNotfind = true;
				if (isNetworkEnabled) 
				{
					myLocationmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,time_updates,distance_updates, this);
					if (myLocationmanager != null) 
					{
						myLocation = myLocationmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (myLocation != null) 
						{
							myLatitude=myLocation.getLatitude();
							myLongitude=myLocation.getLongitude();
						}
					}
				}
				if (isGPSEnabled) {
					if (myLocation == null) {
						myLocationmanager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								time_updates,
								distance_updates, this);
						Log.d("GPS Enabled", "GPS Enabled");
						if (myLocationmanager != null) {
							myLocation = myLocationmanager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (myLocation != null) {
								myLatitude = myLocation.getLatitude();
								myLongitude= myLocation.getLongitude();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return myLocation;
	}
	public double Findlatitude()
	{
		if(myLocation!=null)
		{
			myLatitude=myLocation.getLatitude();
		}
		return myLatitude;
	}
	public double FindLongitude()
	{
		if(myLocation!=null)
		{
			myLongitude=myLocation.getLongitude();
		}
		return myLongitude;
	}

	public boolean Locationnotfind() {
		return this.mylocationNotfind;
	}
	public void onLocationChanged(Location location) {}
	public void onStatusChanged(String provider, int status, Bundle extras) {}
	public void onProviderEnabled(String provider) {}
	public void onProviderDisabled(String provider) {}
	public IBinder onBind(Intent intent) {
		return null;
	}
}
