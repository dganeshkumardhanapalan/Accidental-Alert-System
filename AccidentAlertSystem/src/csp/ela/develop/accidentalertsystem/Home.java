package csp.ela.develop.accidentalertsystem;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
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
import android.os.Handler;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends Activity implements LocationListener{

	TextView tv_back,tv_close,tv_title,tv_set_speed,tv_ealert,tv_disp,tv_cinfo;
	//TextView tv_vtrace,tv_strace;
	Gpsservice myGpsclass;
	double mysourcelat,mySourcelng;
	
	LocationManager lm;
	Timer tm =new Timer();
	Intent in;
	ProgressDialog pbar;
	int addflg=0;
	static String uid,rloc;
	double lat=0,lng=0;
	Location location;
    double xval,yval,zval;
    Address address;
    private ProgressDialog pro;
	boolean lop=true,isGPS,internetconnection,active=true,flg=true,sms=false;
	String s_lat,s_log,disp,res,aAddressfull;
	Handler handle=new Handler();
	boolean emgact=false; 
	 AlertDialog.Builder myAlertDialog;
	 PendingIntent sentPI;
	
	String s_xval,s_yval,s_zval,s_lng,s_loc,s_area,s_zone,s_addr,s_uid,nam,s_mname,s_n1,s_n2,s_n3,s_mno1,s_mno2,s_mno3,chk,SENT = "SMS_SENT";
	
	SensorManager sm=null;
	Sensor acc_sen;
	int ac=0,utype;
	static double thres=-0.5;
	int delay=5000,rcount;
	
	private double speed = 0.0;
    //private int measurement_index = Constants.INDEX_KM;
	
	int set_speed=0;
	int cur_speed=0;
	
	
	 public static String url="http://cyberstudents.in/android/safedrive/Service.asmx";
	 private static final String SOAP_ACTION = "http://tempuri.org/details";
	 private static final String METHOD_NAME = "details";
	 private static final String NAMESPACE = "http://tempuri.org/";
	 
	 private static final String EMG_SOAP_ACTION = "http://tempuri.org/emg";
	 private static final String EMG_METHOD_NAME = "emg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		
		myGpsclass=new Gpsservice(getApplicationContext());
		
		tv_close=(TextView)findViewById(R.id.close);
		tv_back=(TextView)findViewById(R.id.back);
		tv_title=(TextView)findViewById(R.id.title);
		//tv_vtrace=(TextView)findViewById(R.id.vtrace);
		
		//tv_strace=(TextView)findViewById(R.id.strace);
		tv_set_speed=(TextView)findViewById(R.id.set_speed);
		tv_ealert=(TextView)findViewById(R.id.ealert);
		tv_disp=(TextView)findViewById(R.id.disp);
		tv_cinfo=(TextView)findViewById(R.id.cinfo);
		
		
	
		
		Typeface font = Typeface.createFromAsset(getAssets(), "font/FoglihtenPCS-068.otf");
		tv_close.setTypeface(font);
		tv_back.setTypeface(font);
		tv_title.setTypeface(font);
        //tv_vtrace.setTypeface(font);
        tv_cinfo.setTypeface(font);
        //tv_strace.setTypeface(font);
        tv_set_speed.setTypeface(font);
        tv_ealert.setTypeface(font);
        tv_disp.setTypeface(font);
        
        
        sm=(SensorManager) getSystemService(SENSOR_SERVICE);	
		acc_sen = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sm.registerListener(getSensorListener(), acc_sen, SensorManager.SENSOR_DELAY_FASTEST); 
	    
	    
        lm=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		/*
		final List<String> providers = lm.getProviders(true);
        
        for (final String provider : providers)
        {
             lm.requestLocationUpdates(provider, 500, 0, locationListener);
        }
		*/
		isGPS =lm.isProviderEnabled (LocationManager.GPS_PROVIDER);
		internetconnection=checkInternetConnection();
        
        final Bundle getdata = getIntent().getExtras();
		if (getdata != null) {
			
			uid = getdata.getString("uid");
			utype=getdata.getInt("utype");
		}
		try {
			
			myAlertDialog = new AlertDialog.Builder(Home.this);
	        myAlertDialog.setTitle("Emergency Auto Alert Activated");
	        myAlertDialog.setMessage("Emergency Auto Alert Activated");
	        myAlertDialog.setIcon(R.drawable.alert);
	        myAlertDialog.setNegativeButton("Deactivate", new DialogInterface.OnClickListener() {
	        	
	         public void onClick(DialogInterface arg0, int arg1) {
	         // do something when the OK button is clicked
	        	 active=false;
	        	 Toast.makeText(getApplicationContext(), "Auto Alert System Aborted by User", Toast.LENGTH_LONG).show();
	         }});
		
		tv_close.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				System.exit(0);
				finish();
				
			}
		});
		tv_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				in=new Intent(Home.this,MainActivity.class);
				
				startActivity(in);
				finish();
				
			}
		});
		/*
		tv_strace.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//sendBroadcast(new Intent(getApplicationContext(), OnBoot.class));	
				
				tm.schedule(new task(),10,10000);
				
				
				
			}
		});
		*/
		tv_set_speed.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				LayoutInflater li=LayoutInflater.from(Home.this);
				
				View layoutFromInflater = li.inflate(R.layout.speed,(ViewGroup) findViewById(R.id.linearlayout_speed));
				
			       //final View v1=li.inflate(R.layout.speed, null);
			       
			       AlertDialog.Builder builder = new AlertDialog.Builder(Home.this)
			       .setTitle("Set Speed Limit") 
		           .setMessage("If you select 0 not Calculate speed Limit")
			       .setView(layoutFromInflater)
			       .setPositiveButton("Ok", new DialogInterface.OnClickListener() 
			       { 
		        		public void onClick(DialogInterface dialog, int whichButton) 
		        		{ 
		        			Log.i("set speed",Integer.toString(set_speed));
		        			dialog.dismiss();
		        		}              
		        	});
			       AlertDialog alertDialog = builder.create();
			       alertDialog.show();
			       
			       
			       final TextView tv_seek_speed=(TextView) layoutFromInflater.findViewById(R.id.tv_seek_speed);
			       SeekBar seek_speed = (SeekBar)layoutFromInflater.findViewById(R.id.seek_speed);
			       seek_speed.setMax(0);
			       seek_speed.setMax(100);
			       seek_speed.setProgress(set_speed);
			       tv_seek_speed.setText(Integer.toString(set_speed));
			       
			       seek_speed.setOnSeekBarChangeListener(new OnSeekBarChangeListener() 
			       {
			           public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			           {
			               //Do something here with new value
			        	   tv_seek_speed.setText(Integer.toString(progress));
			        	   set_speed=progress;
			        	   //show_toast(progress+"");
			           }

					@Override
					public void onStartTrackingTouch(SeekBar seekBar)
					{
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar)
					{
						// TODO Auto-generated method stub
						
					}
			       });
			      
			}
		});
		/*
		tv_vtrace.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				in=new Intent(Home.this,Filter.class);
				in.putExtra("uid", uid);
				startActivity(in);
				finish();
				
			}
		});
		*/
		tv_cinfo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				in=new Intent(Home.this,ContactInfo.class);
				in.putExtra("uid", uid);
				startActivity(in);
				finish();
				
			}
		});
		
			
		tv_ealert.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(emgact) {
				emgact=false;
				display("Emergency Auto Alert DeActivated");
				} else {
					
					emgact=true;
					 pro = ProgressDialog.show(Home.this,"Please Wait","Fetching Emergency Contact Info");
		 			 pro.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		 				
		 			 new Thread() {
		 					public void run() {
		 				SoapObject request = new SoapObject(NAMESPACE,EMG_METHOD_NAME);
		 				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		 				envelope.dotNet=true;				
		 				request.addProperty("uid", uid);
						envelope.setOutputSoapObject(request);
		 				HttpTransportSE androidHttpTransport = new HttpTransportSE(url);				
		 				try {
		 					androidHttpTransport.call(EMG_SOAP_ACTION, envelope);
		 				} catch (IOException e1) {
		 					// TODO Auto-generated catch block
		 					e1.printStackTrace();
		 				} catch (XmlPullParserException e1) {
		 					// TODO Auto-generated catch block
		 					e1.printStackTrace();
		 				}				
		 				
		 				
		 				try {
		 					SoapObject result = (SoapObject) envelope.getResponse();
		 					SoapObject res = (SoapObject) result.getProperty(0);
		 					rcount = (res.getPropertyCount());
		 					
		 					pro.dismiss();
		 					
		 					if (rcount>0) {
		 						chk=res.getProperty(0).toString();
		 						if(!(chk.equalsIgnoreCase("0"))) {
		 						sms=true;
		 							s_mname=res.getProperty(0).toString();
		 							s_n1=res.getProperty(1).toString();
		 							s_mno1=res.getProperty(2).toString();
		 							s_n2=res.getProperty(3).toString();
		 							s_mno2=res.getProperty(4).toString();
		 							s_n3=res.getProperty(5).toString();
		 							s_mno3=res.getProperty(6).toString();
		 						
		 						} else {
		 						runOnUiThread(new Runnable() {
		 					        @Override
		 						        public void run() {
		 					        	sms=false;
		 					        	display("No Emergency Contact Info Saved");
		 						        }
		 						    });
		 						}
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
		 					display("Emergency Auto Alert Activated");	
				}
				
				
			}
		});
		  sentPI = PendingIntent.getBroadcast(getApplicationContext(), 0,
  	            new Intent(SENT), 0);
  		
  		 registerReceiver(new BroadcastReceiver(){
  	            @Override
  	            public void onReceive(Context arg0, Intent arg1) {
  	                switch (getResultCode())
  	                {
  	                    case Activity.RESULT_OK:
  	                        Toast.makeText(getBaseContext(), "SMS sent", 
  	                                Toast.LENGTH_SHORT).show();
  	                        break;
  	                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
  	                      //  Toast.makeText(getBaseContext(), "Generic failure", 
  	                             //   Toast.LENGTH_SHORT).show();
  	                        break;
  	                    case SmsManager.RESULT_ERROR_NO_SERVICE:
  	                        Toast.makeText(getBaseContext(), "No service", 
  	                                Toast.LENGTH_SHORT).show();
  	                        break;
  	                    case SmsManager.RESULT_ERROR_NULL_PDU:
  	                        Toast.makeText(getBaseContext(), "Null PDU", 
  	                                Toast.LENGTH_SHORT).show();
  	                        break;
  	                    case SmsManager.RESULT_ERROR_RADIO_OFF:
  	                        Toast.makeText(getBaseContext(), "Radio off", 
  	                                Toast.LENGTH_SHORT).show();
  	                        break;
  	                }
  	            }
  	        }, new IntentFilter(SENT));
  	 
		
		
		} catch(Exception e) {
			e.printStackTrace();
			display(e.toString());
		}
		
	}
	
	public SensorEventListener getSensorListener() {
	   	 return new SensorEventListener() {
	   		 public void onSensorChanged(SensorEvent e) {
	   			xval = e.values[0];
	   			yval = e.values[1];
	   			zval = e.values[2];
	   			s_xval=xval+"";
	   			s_yval=yval+"";
	   			s_zval=zval+"";
	   			
				if (yval < thres && flg && emgact)
				{
					flg = false;
					
					try
					{
						myAlertDialog.show();
					} catch (Exception ee)
					{
						ee.printStackTrace();
					}
					tm.schedule(new etask(), delay);
					
				}
	   			}
					
	   		 public void onAccuracyChanged(Sensor s, int i) {
	   	            // TODO
	   	         }
	   		 
	   	  };
		 }
	
	class task extends TimerTask {
		 public void run() {
			 Home.this.runOnUiThread(new Runnable() {
				 public void run() {
					
					rloc= gpsadd();	
					
					
					if(addflg==1) {
					 webmethod(uid,s_lat,s_log);
					}
					tv_disp.setText(rloc);
					}
			 });
		 }
	 };
	 
	 class etask extends TimerTask {
		 public void run() {
			 Home.this.runOnUiThread(new Runnable() {
				 public void run() {
					
					rloc = gpsadd();
					
					
					if (set_speed == 0)
					{
						//lat=location.getLatitude();
		   	  			//lng=location.getLongitude();
						mysourcelat=myGpsclass.Findlatitude();
						mySourcelng=myGpsclass.FindLongitude();
						StringBuilder sb = new StringBuilder();
//						lat=location.getLatitude();
//		   	  			lng=location.getLongitude();
						
						Log.e("lats",""+ mysourcelat);
						Geocoder aGeocoder=new Geocoder(getApplicationContext());
 						try {
							List<Address> aList = aGeocoder.getFromLocation(mysourcelat, mySourcelng, 2);
							String aPlacename=aList.get(0).getLocality();
							Log.e("location name",aPlacename);
							String aSublocality=aList.get(0).getSubLocality();
							Log.e("Sub locality",aSublocality);
							String mySourceplace=mysourcelat+","+mySourcelng;
							String aaddress=aList.get(0).getFeatureName();
							Log.e("feature name",aaddress);

							String aaddress1=aList.get(0).getPostalCode();
							Log.e("through fare",aaddress1);
							aAddressfull=aSublocality+","+aaddress+","+aPlacename+","+aaddress1;

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
						
		   	  			Log.e("mysourcelat", String.valueOf(mysourcelat));
						sb.append("Hi"+ " " + s_mname +" is Emergency with Loction"+ mysourcelat+ mySourcelng+"In the Area"+aAddressfull);
						sendmessage(s_mno1, sb.toString());
						int h=sb.capacity();
						sb.delete(0, h);
						sb.append("Hi " + " " + s_mname +" is Emergency with Loction"+ mySourcelng+ mySourcelng+"In the Area"+aAddressfull);
						
						sendmessage(s_mno2, sb.toString());
						int a=sb.capacity();
						sb.delete(0, a);
						sb.append("Hi " +" " + s_mname+" is Emergency with Loction" +mysourcelat+ mySourcelng+"In the Area"+aAddressfull);
						sendmessage(s_mno3, sb.toString());
					}
					
					else if (set_speed > 0)
						{
							//lat=location.getLatitude();
			   	  			//lng=location.getLongitude();
							mysourcelat=myGpsclass.Findlatitude();
							mySourcelng=myGpsclass.FindLongitude();
							StringBuilder sb = new StringBuilder();
//							lat=location.getLatitude();
//			   	  			lng=location.getLongitude();
			   	  			Log.e("mysourcelat", String.valueOf(mysourcelat));
							sb.append("Hi"+ " " + s_mname +" is Emergency with Loction"+ mysourcelat+ mySourcelng+"Speed ="+Integer.toString(set_speed));
							sendmessage(s_mno1, sb.toString());
							int h=sb.capacity();
							sb.delete(0, h);
							sb.append("Hi " + " " + s_mname +" is Emergency with Loction"+ mySourcelng+ mySourcelng+"Speed ="+Integer.toString(set_speed));
							
							sendmessage(s_mno2, sb.toString());
							int a=sb.capacity();
							sb.delete(0, a);
							sb.append("Hi " +" " + s_mname+" is Emergency with Loction" +mysourcelat+ mySourcelng+"Speed ="+Integer.toString(set_speed));
							sendmessage(s_mno3, sb.toString());
						}
					
					
						else
						{
							lat=location.getLatitude();
		   	  			lng=location.getLongitude();
							StringBuilder sb = new StringBuilder();
							sb.append("Hi " + s_mname + " is in emergency"+lat+""+lng);
							sendmessage(s_mno1, sb.toString());
							sb.append("Hi " + s_mname + " is in emergency"+lat+""+lng);
							sendmessage(s_mno2, sb.toString());
							sb.append("Hi "+ " " + s_mname + " is in emergency."+lat+""+lng);
							sendmessage(s_mno3, sb.toString());
						}
					
					
					tv_disp.setText(rloc);
					}
			 });
		 }
	 };
	 public void sendmessage(String no,String msg) {
			try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(no, null, msg, sentPI, null);
			} catch(Exception e) {
				e.printStackTrace();
				display("Error in sending message");
			}
			
		}
	 
	 public boolean webmethod(final String uid,final String lat,final String lng) {
	  		
 		 try {
 			 pro = ProgressDialog.show(Home.this,"Please Wait","Uploading location Information");
 			 pro.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
 				
 			 new Thread() {
 					public void run() {
 				SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
 				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
 				envelope.dotNet=true;				
 				request.addProperty("uid", uid);
				request.addProperty("xval", s_xval);
				request.addProperty("yval", s_yval);
				request.addProperty("zval", s_zval);
				request.addProperty("lat", s_lat);
				request.addProperty("lng", s_log);
				request.addProperty("loc", s_loc);
				request.addProperty("area", s_area);
				request.addProperty("zone", s_zone);
				request.addProperty("addr", s_addr);
				
 			  
 				envelope.setOutputSoapObject(request);
 				HttpTransportSE androidHttpTransport = new HttpTransportSE(url);				
 				try {
 					androidHttpTransport.call(SOAP_ACTION, envelope);
 				} catch (IOException e1) {
 					// TODO Auto-generated catch block
 					e1.printStackTrace();
 				} catch (XmlPullParserException e1) {
 					// TODO Auto-generated catch block
 					e1.printStackTrace();
 				}				
 				
 				
 				try {
 					SoapObject result=(SoapObject)envelope.bodyIn;	   
 						res=result.getProperty(0).toString();
 						pro.dismiss();
 						handle.post(new Runnable() {
 							@Override
 							public void run() {
 								// TODO Auto-generated method stu
 								display(res);
 					        	
 							}
 						});
 						
 					
 				} catch(Exception e) {
 					e.printStackTrace();
 					
 				}
 					}
 					}.start();
 					
 				
 			} catch(Exception e) {
 				e.printStackTrace();
 				
 				
 			}
 			
 			
 			
 			
 		 return true;
 	 }
	 
	 public final LocationListener locationListener = new LocationListener() 
	    {
	        public void onLocationChanged(Location location) {
	        	Home.this.location=location;
	        }
	        public void onProviderDisabled(String provider) {
	        	Home.this.location=null;
	          }
	        public void onProviderEnabled(String provider) {
	        }
	        public void onStatusChanged(String provider, int status, Bundle extras) {
	        }
	      };
	
	public String gpsadd() {
		try {
		
		
	    if(isGPS) {
			 	
      		        
      		        Geocoder gc = new Geocoder(Home.this, Locale.getDefault());
   			 try 
   	          {
   				addflg=0;
   	        	lat=location.getLatitude();
   	  			lng=location.getLongitude();
   	  			
   	  			
   	  			speed = location.getSpeed();
            
   	  			String speedString = "" + roundDecimal(convertSpeed(speed),2);
   	  			String unitString = measurementUnitString(Constants.INDEX_KM);
   	  			
   	  			cur_speed= Integer.parseInt(speedString);
   	  			
   				boolean lop=true;
   				while(lop) {
   	            List<Address> addresses = gc.getFromLocation(lat, lng, 1);
   	            if (addresses.size() > 0) 
   	            {
   	            lop=false;
   	            	addflg=1;
   	             address = addresses.get(0);
   	             s_lat=lat+""; 
   	             s_log=lng+""; 
   	             
   	             	
	   	           	s_area= address.getFeatureName();
	   	            s_loc=address.getLocality();
	   	            s_zone=address.getSubAdminArea();
	   	            s_addr=address.getAddressLine(0);
	   	            disp="Location\n "+address.getAddressLine(0)+"\n"+address.getAddressLine(1)+"\n"+address.getAddressLine(2);
	   	            String[] str=new String[100];
	   	            str= s_addr.split(",");
	   	            
	   	         s_loc=str[0];
	   	         s_area=str[1];
   	           
   	            }
   	        
   	            }
   	       
   	        	  
   	          }
   	          catch (Exception e)
   	          {
   	        	  e.printStackTrace();
   	          }
   		} else {
   			display("GPS not Enabled");
   			
   		}
		} catch(Exception e) {
			e.printStackTrace();
			
		}
		
		return disp;
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

private double convertSpeed(double speed){
    return ((speed * Constants.HOUR_MULTIPLIER) * Constants.UNIT_MULTIPLIERS[Constants.INDEX_KM]);
}

private String measurementUnitString(int unitIndex){
    String string = "";
     
    switch(unitIndex)
    {
            case Constants.INDEX_KM:                string = "km/h";        break;
            case Constants.INDEX_MILES:     string = "mi/h";        break;
    }
     
    return string;
}

private double roundDecimal(double value, final int decimalPlace)
{
    BigDecimal bd = new BigDecimal(value);
     
    bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
    value = bd.doubleValue();
     
    return value;
}
public void show_toast(String msg) {
	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
}

@Override
public void onLocationChanged(Location location) {


    // Setting Current Longitude
 lat=location.getLongitude();
 lng=location.getLongitude();

    // Setting Current Latitude
   
}

@Override
public void onStatusChanged(String provider, int status, Bundle extras) {
	// TODO Auto-generated method stub
	
}

@Override
public void onProviderEnabled(String provider) {
	// TODO Auto-generated method stub
	
}

@Override
public void onProviderDisabled(String provider) {
	// TODO Auto-generated method stub
	
}
}
