package csp.ela.develop.accidentalertsystem;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;


import android.net.ConnectivityManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	TextView tv_close,tv_reg,tv_luid,tv_lpwd,tv_ok,tv_title;
	EditText et_uid,et_pwd;
	
	
	Intent in;
	ProgressDialog pbar;
	int utype=0;
	String s_uid,s_pwd,res,nam;
	
		 public static String url="http://cyberstudents.in/android/safedrive/Service.asmx";
		 private static final String SOAP_ACTION = "http://tempuri.org/login";
		 private static final String METHOD_NAME = "login";
		 private static final String NAMESPACE = "http://tempuri.org/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tv_close=(TextView)findViewById(R.id.close);
		tv_reg=(TextView)findViewById(R.id.register);
		tv_ok=(TextView)findViewById(R.id.ok);
		tv_title=(TextView)findViewById(R.id.title);
		tv_luid=(TextView)findViewById(R.id.luid);
		tv_lpwd=(TextView)findViewById(R.id.lpwd);
		
		
		
		et_uid=(EditText)findViewById(R.id.uid);
		et_pwd=(EditText)findViewById(R.id.pwd);
		
		
		Typeface font = Typeface.createFromAsset(getAssets(), "font/FoglihtenPCS-068.otf");
		tv_close.setTypeface(font);
		tv_reg.setTypeface(font);
		tv_ok.setTypeface(font);
        tv_lpwd.setTypeface(font);
        tv_luid.setTypeface(font);
      
        tv_title.setTypeface(font);
		try {
		
		tv_close.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				System.exit(0);
				finish();
				
			}
		});
		tv_reg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				in=new Intent(MainActivity.this,Register.class);
				in.putExtra("type", 1);
				startActivity(in);
				finish();
				
			}
		});
		
		tv_ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				s_uid=et_uid.getText().toString();
				s_pwd=et_pwd.getText().toString();
				boolean internetconnection = checkInternetConnection();
				if(internetconnection) {
					pbar = ProgressDialog.show(MainActivity.this,"Authenticating ...!","Wait Please");
					pbar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					
					
					
					new Thread() {
						public void run() {
							
							SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
							SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
							envelope.dotNet = true;
							request.addProperty("uid", s_uid);
							request.addProperty("pwd", s_pwd);
							
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
    								if(nam.equals("0")) {
    									res="Not a Registered User";
    									
    								} 
    								if(nam.equals("1")) {
    									res="Authentication Success .. !";
    									
    								} 
    								if(nam.equals("2")) {
    									res="User and Password Incorrect";
    									
    								} 
    								
    								runOnUiThread(new Runnable() {
    							        @Override
    								        public void run() {
    								        	display(res);
    								        	in=new Intent(MainActivity.this,MainActivity.class);
    								        	if(nam.equals("1")) {
    								        		if(s_uid.equalsIgnoreCase("admin")) {
    								        			utype=1;
    								        			in=new Intent(MainActivity.this,AdminHome.class);
    	    								        	in.putExtra("utype", utype);
    								        		} else {
    								        			in=new Intent(MainActivity.this,Home.class);
    	    								        	in.putExtra("utype", utype);
    								        		}
    								        	
    								        	}
    								        	in.putExtra("uid", s_uid);
    											startActivity(in);
    											finish();
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
				
				
				
			}
		});

		
		
		} catch(Exception e) {
			e.printStackTrace();
			display(e.toString());
		}
		
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
