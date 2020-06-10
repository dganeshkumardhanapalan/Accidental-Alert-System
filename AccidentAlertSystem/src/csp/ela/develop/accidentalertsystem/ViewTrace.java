package csp.ela.develop.accidentalertsystem;



import java.util.List;
import java.util.Locale;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ViewTrace extends Activity {
	SensorManager sm=null;
	LocationManager lm;
	LocationListener mlocListener;
	Location location;
	ProgressDialog pbar;
	double xval,yval,zval;
	boolean isGPS,gaddr,internetconnection;
	int addflg=0;
	double lat=0,lng=0;
	Address address;
	String s_xval,s_yval,s_zval,s_lat,s_lng,s_loc,s_area,s_zone,s_addr,s_uid,nam;
	
	public static String url="http://cyberstudents.in/android/safedrive/Service.asmx";
	 private static final String SOAP_ACTION = "http://tempuri.org/details";
	 private static final String METHOD_NAME = "details";
	 private static final String NAMESPACE = "http://tempuri.org/";
Button bt;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_trace);
		final Bundle getdata = getIntent().getExtras();
		bt=(Button) findViewById(R.id.button1);
		
		
		if (getdata != null) {
			
			s_uid = getdata.getString("uid");
		}
		lm=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		try {
			isGPS =lm.isProviderEnabled (LocationManager.GPS_PROVIDER);
			internetconnection=checkInternetConnection();
			//if(isGPS&&internetconnection) {
			sm=(SensorManager) getSystemService(SENSOR_SERVICE);	
			Sensor acc_sen = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		    sm.registerListener(getSensorListener(), acc_sen, SensorManager.SENSOR_DELAY_NORMAL);
		    lm=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
			
			 lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	 		 lm.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	 		 
	 		 
			//}
			
			bt.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					//gaddr=gpsadd();

		 			pbar = ProgressDialog.show(ViewTrace.this,"Authenticating ...!","Wait Please");
					pbar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					
					
					Geocoder gc = new Geocoder(ViewTrace.this, Locale.getDefault());
	     			 try 
	     	          {
	     				addflg=0;
	     	        	lat=location.getLatitude();
	     	  			lng=location.getLongitude();
	     				boolean lop=true;
	     				while(lop) {
	     	            List<Address> addresses = gc.getFromLocation(lat, lng, 1);
	     	            if (addresses.size() > 0) 
	     	            {
	     	            lop=false;
	     	            addflg=1;
	     	            address = addresses.get(0);
	     	             s_lat=lat+""; 
	     	             s_lng=lng+""; 
	     	             s_addr=address.toString();
	     	             s_zone=address.getSubAdminArea();
	     	             s_loc= address.getSubLocality();
	     	             s_area= address.getFeatureName();
	     	           
	     	            }
	     	        
	     	            }
	     	        
	     	          }
	     	          catch (Exception e)
	     	          {
	     	        	  
	     	        	  e.printStackTrace();
	     	          }
			 		
				
						
						
						new Thread() {
							public void run() {
								
								SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
								SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
								envelope.dotNet = true;
								request.addProperty("uid", s_uid);
								request.addProperty("xval", s_xval);
								request.addProperty("yval", s_yval);
								request.addProperty("zval", s_zval);
								request.addProperty("lat", s_lat);
								request.addProperty("lng", s_lng);
								request.addProperty("loc", s_loc);
								request.addProperty("area", s_area);
								request.addProperty("zone", s_zone);
								request.addProperty("addr", s_addr);
								
								envelope.setOutputSoapObject(request);
								HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
								try {
								try {
									androidHttpTransport.call(SOAP_ACTION, envelope);
								} catch (XmlPullParserException e) {
									display("Error While calling Web Method");
									e.printStackTrace();
								}
								
								
								try {
		    						
									SoapObject result=(SoapObject)envelope.bodyIn;	   
										nam=result.getProperty(0).toString();
										pbar.dismiss();
										
										runOnUiThread(new Runnable() {
									        @Override
										        public void run() {
										        	display(nam);
										        	
										        }
										    });
							
										
									
								} catch(Exception e) {
									e.printStackTrace();
									
									
								}
								
								} catch (Exception e) {
									runOnUiThread(new Runnable() {
								        @Override
									        public void run() {
								        	display("Error in Retriving Information");
									        }
									    });
								e.printStackTrace();	
								}
								
							}	
						}.start();
					
					
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			display(e.toString());
		}
		
		
	}
	 private SensorEventListener getSensorListener() {
	   	 return new SensorEventListener() {
	   		 public void onSensorChanged(SensorEvent e) {
	   			xval = e.values[0];
	   			yval = e.values[1];
	   			zval = e.values[2];
	   			s_xval=xval+"";
	   			s_yval=yval+"";
	   			s_zval=zval+"";
	   			}
					
	   		 public void onAccuracyChanged(Sensor s, int i) {
	   	            // TODO
	   	         }
	   		 
	   	  };
		 }
	 private final LocationListener locationListener = new LocationListener() 
	    {
	        public void onLocationChanged(Location location) {
	        	ViewTrace.this.location=location;
	        }
	        public void onProviderDisabled(String provider) {
	        	ViewTrace.this.location=null;
	          }
	        public void onProviderEnabled(String provider) {
	        }
	        public void onStatusChanged(String provider, int status, Bundle extras) {
	        }
	      };
	      public boolean gpsadd() {
	    	  boolean rt=false;
	  		try {
	  			
	  		if(isGPS) {
	  			Geocoder gc = new Geocoder(ViewTrace.this, Locale.getDefault());
	     			 try 
	     	          {
	     				addflg=0;
	     	        	lat=location.getLatitude();
	     	  			lng=location.getLongitude();
	     				boolean lop=true;
	     				while(lop) {
	     	            List<Address> addresses = gc.getFromLocation(lat, lng, 1);
	     	            if (addresses.size() > 0) 
	     	            {
	     	            lop=false;
	     	            rt=true;
	     	            addflg=1;
	     	            address = addresses.get(0);
	     	             s_lat=lat+""; 
	     	             s_lng=lng+""; 
	     	             s_addr=address.toString();
	     	             s_zone=address.getSubAdminArea();
	     	             s_loc= address.getSubLocality();
	     	             s_area= address.getFeatureName();
	     	           
	     	            }
	     	        
	     	            }
	     	        
	     	          }
	     	          catch (Exception e)
	     	          {
	     	        	  rt=false;
	     	        	  e.printStackTrace();
	     	          }
	     		} 
	  		} catch(Exception e) {
	  			 rt=false;
	  			e.printStackTrace();
	  			
	  		}
	  		
	  		return rt ;
	  	}
	      private boolean checkInternetConnection() {
	  		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	  		// test for connection
	  		if (cm.getActiveNetworkInfo() != null
	  				&& cm.getActiveNetworkInfo().isAvailable()
	  				&& cm.getActiveNetworkInfo().isConnected()) {
	  			
	  			return true;
	  		} else {
	  			Toast.makeText(getApplicationContext(), "No Network Connection ", Toast.LENGTH_LONG).show();
	  			return false;
	  		}
	  	}

public void display(String msg) {
	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
}	
}
