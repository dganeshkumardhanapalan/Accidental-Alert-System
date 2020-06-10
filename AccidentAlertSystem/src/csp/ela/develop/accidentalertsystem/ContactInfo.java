package csp.ela.develop.accidentalertsystem;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ContactInfo extends Activity {
	
	private TextView tv_lname1,tv_lname2,tv_lmno1,tv_lmno2,tv_save,tv_back,tv_close,tv_title,tv_clear;
	private EditText et_name1,et_name2,et_mno1,et_mno2,et_name3,et_mno3,et_name0,et_mno0;
	
	private ProgressDialog pro;
	String uid,res,nam,s_name0,s_mno0,s_name1,s_name2,s_mno1,s_mno2,s_name3,s_mno3;
	Intent in;
	Handler handle=new Handler();
	
	 public static String URL="http://cyberstudents.in/android/safedrive/Service.asmx";
   	 private static final String SOAP_ACTION = "http://tempuri.org/cupdate";
   	  private static final String METHOD_NAME = "cupdate";
   	private static final String NAMESPACE = "http://tempuri.org/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_info);
		
		tv_title=(TextView) findViewById (R.id.title);
		tv_clear=(TextView) findViewById (R.id.clear);
        tv_back=(TextView) findViewById (R.id.back);
        tv_close=(TextView) findViewById (R.id.close);
        tv_save=(TextView) findViewById (R.id.save);
        tv_lname1=(TextView) findViewById (R.id.lname1);
        tv_lname2=(TextView) findViewById (R.id.lname2);
        tv_lmno1=(TextView) findViewById (R.id.lmno1);
        tv_lmno2=(TextView) findViewById (R.id.lmno2);
        tv_save=(TextView) findViewById (R.id.save);
        tv_clear=(TextView) findViewById (R.id.clear);
        
        et_name0=(EditText) findViewById (R.id.name0);
      
        
        et_name1=(EditText) findViewById (R.id.name1);
        et_mno1=(EditText) findViewById (R.id.mno1);
        et_name2=(EditText) findViewById (R.id.name2);
        et_mno2=(EditText) findViewById (R.id.mno2);
        
        et_name3=(EditText) findViewById (R.id.name3);
        et_mno3=(EditText) findViewById (R.id.mno3);
        
        Typeface font = Typeface.createFromAsset(getAssets(), "font/FoglihtenPCS-068.otf");
        tv_title.setTypeface(font);
        tv_save.setTypeface(font);
        tv_lname1.setTypeface(font);
        tv_lname2.setTypeface(font);
        tv_lmno1.setTypeface(font);
        tv_lmno2.setTypeface(font);
        tv_clear.setTypeface(font);
        tv_back.setTypeface(font);
        tv_close.setTypeface(font);
        
        
        final Bundle getdata = getIntent().getExtras();
		if (getdata != null) {
			uid=getdata.getString("uid");
			}
		
		
		try {
			
			
			tv_save.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					s_name0=et_name0.getText().toString();
			
					s_name1=et_name1.getText().toString();
					s_name2=et_name2.getText().toString();
					s_name3=et_name3.getText().toString();
					s_mno1=et_mno1.getText().toString();
					s_mno2=et_mno2.getText().toString();
					s_mno3=et_mno3.getText().toString();
					if(!(s_name1.equals("") || s_name2.equals(""))) {
						if(s_mno1.length()==10 && s_mno2.length()==10) {
							
							webmethod();
							
						} else {
							display("Check your Mobile No");
						}
					} else {
						display("Fill the Names Properly");
					}
					
				}
			});
			
			tv_clear.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					et_name1.setText("");
					et_name2.setText("");
					et_mno1.setText("");
					et_mno2.setText("");
					
					
					
				}
			});
	      
	        
			tv_close.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					finish();
					System.exit(0);
					
					
				}
			});
	      
	        tv_back.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					 
						in=new Intent(ContactInfo.this,Home.class);
						in.putExtra("uid", uid);
						startActivity(in);
						finish();
					
					
				}
			});
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	 public boolean webmethod() {
			
		 try {
			 pro = ProgressDialog.show(ContactInfo.this,"Please Wait","Updating Contact info to Server");
			 pro.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				
			 new Thread() {
					public void run() {
				SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet=true;				
				request.addProperty("uid",uid);	
				Log.v("name1",s_name1);
				Log.v("uid",uid);
				Log.v("mno1",s_mno1);
				Log.v("name2",s_name2);
				Log.v("mno2",s_mno2);
				request.addProperty("name",s_name0);
			    request.addProperty("name1",s_name1);	
			    request.addProperty("mno1",s_mno1);
			    request.addProperty("name2",s_name2);	
			    request.addProperty("mno2",s_mno2);
			    request.addProperty("name3",s_name3);	
			    request.addProperty("mno3",s_mno3);
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);				
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
	public void display(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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

}
