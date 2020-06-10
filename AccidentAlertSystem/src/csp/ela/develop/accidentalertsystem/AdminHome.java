package csp.ela.develop.accidentalertsystem;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AdminHome extends Activity {

	TextView tv_close,tv_lbid,tv_trace,tv_back,tv_tittle;
	Intent in;
	String uid,bid,s_lat,s_lng;
	Spinner sp_bid;
	ArrayAdapter<String> spbid;
	
	ProgressDialog pbar;
	Handler handle=new Handler();
	
	String[] busid=new String[100];
	int cnt=0;
	String s_bid;
	
	public static String url="http://cyberstudents.in/android/safedrive/Service.asmx";
	 private static final String SOAP_ACTION = "http://tempuri.org/userid";
	 private static final String METHOD_NAME = "userid";
	 private static final String NAMESPACE = "http://tempuri.org/";
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_home);
		
		tv_close=(TextView)findViewById(R.id.close);
		tv_lbid=(TextView)findViewById(R.id.luid);
		tv_back=(TextView)findViewById(R.id.back);
		tv_tittle=(TextView)findViewById(R.id.title);
		tv_trace=(TextView)findViewById(R.id.trace);
		
		
		sp_bid=(Spinner) findViewById(R.id.uid);
		
		Typeface font = Typeface.createFromAsset(getAssets(), "font/FoglihtenPCS-068.otf");
		tv_close.setTypeface(font);
		tv_back.setTypeface(font);
		tv_tittle.setTypeface(font);
        tv_lbid.setTypeface(font);
        tv_trace.setTypeface(font);
        
        
        spbid = new ArrayAdapter<String>(this, R.layout.spinner, R.id.sp_text);
        spbid.setDropDownViewResource(R.layout.dropdown);
        sp_bid.setAdapter(spbid);
        spbid.notifyDataSetChanged();
        sp_bid.setOnItemSelectedListener(new pselected());
		
	
		
       
        final Bundle getdata = getIntent().getExtras();
		if (getdata != null) {
			
			uid = getdata.getString("uid");
			
			
		}
		
		
		try {
			
			
			boolean internetconnection = checkInternetConnection();
			if(internetconnection) {
				pbar = ProgressDialog.show(AdminHome.this,"Please Wait","Fetching User Id");
				pbar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				
				
				
				new Thread() {
					public void run() {
						
						SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
						SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
						envelope.dotNet = true;
						envelope.setOutputSoapObject(request);
						HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
						try {
						try {
							androidHttpTransport.call(SOAP_ACTION, envelope);
						} catch (XmlPullParserException e) {
							display("Error While calling Web Method");
							e.printStackTrace();
						}
						SoapObject result = (SoapObject) envelope.getResponse();
						SoapObject res = (SoapObject) result.getProperty(0);
						int rcount = (res.getPropertyCount());
						
						pbar.dismiss();
						
						if (rcount>=1) {
							
							for (int i = 0; i < rcount; i++) {
								busid[cnt] = res.getProperty(i).toString();
								cnt++;
							}
							} else {
							runOnUiThread(new Runnable() {
						        @Override
							        public void run() {
							        	display("No Users Available");
							        }
							    });
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
						handle.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								spbid.clear();
								for (int i = 0; i < cnt; i++) {
									spbid.add(busid[i]);
								}
								
								
							}
						});
					}	
				}.start();
			}
			
			
 		        
			
		} catch(Exception e) {
			e.printStackTrace();
			display(e.toString());
		}
        
		try {
			
			
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
				
				in=new Intent(AdminHome.this,MainActivity.class);
				startActivity(in);
				finish();
				
			}
		});
		
		tv_trace.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				in=new Intent(AdminHome.this,Filter.class);
				in.putExtra("uid", s_bid);
				startActivity(in);
				finish();
				
			}
		});
		
		
		
		
		
		} catch(Exception e) {
			e.printStackTrace();
			display(e.toString());
		}
		
	}
	
	

	public class pselected implements OnItemSelectedListener {
 		 
		  public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
		  		   s_bid=parent.getItemAtPosition(pos).toString();
		  		  }
		   public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		
		  }
		 
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
