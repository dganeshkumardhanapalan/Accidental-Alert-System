package csp.ela.develop.accidentalertsystem;


import java.util.Calendar;


import android.net.ConnectivityManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class Filter extends Activity {
	TextView tv_close,tv_back,tv_title,tv_lfrmdate,tv_lfrmtm,tv_ltotm,tv_rtrace,tv_ltrace;
	CheckedTextView ctv_ltodate;
	DatePicker dp_fromdate,dp_todate;
	TimePicker tp_frmtm,tp_totm;
	Intent in;
	
	int yr,mon,dy,hr,min;
	Calendar calendar;
	
	String uid,fdate,tdate;
	int ttype=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);
		
		tv_close=(TextView)findViewById(R.id.close);
		tv_back=(TextView)findViewById(R.id.back);
		tv_title=(TextView)findViewById(R.id.title);
		tv_lfrmdate=(TextView)findViewById(R.id.lfrmdate);
		
		tv_lfrmtm=(TextView)findViewById(R.id.lfrmtm);
		tv_ltotm=(TextView)findViewById(R.id.ltotm);
		tv_rtrace=(TextView)findViewById(R.id.rtrace);
		tv_ltrace=(TextView)findViewById(R.id.ltrace);
		
		ctv_ltodate=(CheckedTextView)findViewById(R.id.ltodate);
		
		dp_fromdate=(DatePicker) findViewById(R.id.fromdate);
		dp_todate=(DatePicker) findViewById(R.id.todate);
		
		tp_frmtm=(TimePicker)findViewById(R.id.frmtm);
		tp_totm=(TimePicker)findViewById(R.id.totm);
	
		
		Typeface font = Typeface.createFromAsset(getAssets(), "font/FoglihtenPCS-068.otf");
		tv_close.setTypeface(font);
		tv_back.setTypeface(font);
		tv_title.setTypeface(font);
		tv_lfrmdate.setTypeface(font);
		tv_lfrmtm.setTypeface(font);
		tv_ltotm.setTypeface(font);
		tv_rtrace.setTypeface(font);
		tv_ltrace.setTypeface(font);
		ctv_ltodate.setTypeface(font);
		
		 final Bundle getdata = getIntent().getExtras();
			if (getdata != null) {
				
				uid = getdata.getString("uid");
				}
       
       try {
    	   		calendar = Calendar.getInstance();
    	   		yr=calendar.get(Calendar.YEAR);
    	   		mon =calendar.get(Calendar.MONTH)+1;
    	   		dy =calendar.get(Calendar.DATE);
    	   		hr=calendar.get(Calendar.HOUR_OF_DAY);
    	   		min=calendar.get(Calendar.MINUTE);
    	   
    	   ctv_ltodate.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(ctv_ltodate.isChecked()) {
						ctv_ltodate.setChecked(false);
					} else {
						ctv_ltodate.setChecked(true);
					}
					
				}
			});
			
    	   
    	   tv_rtrace.setOnClickListener(new View.OnClickListener() {
      			
      			@Override
      			public void onClick(View v) {
      				// TODO Auto-generated method stub
      				
      				 int fday = dp_fromdate.getDayOfMonth();
					 int fmonth = dp_fromdate.getMonth() + 1;
					 int fyear = dp_fromdate.getYear();
					 
					 int fhr= tp_frmtm.getCurrentHour();
					 int fmin= tp_frmtm.getCurrentMinute();
					 
					 
					 int tday = dp_todate.getDayOfMonth();
					 int tmonth = dp_todate.getMonth() + 1;
					 int tyear = dp_todate.getYear();
					 
					 int thr= tp_totm.getCurrentHour();
					 int tmin= tp_totm.getCurrentMinute();
					 
					 
					 
					
					 if(yr>=fyear &&mon>=fmonth && dy>=fday && hr>=fhr && min>=fmin ) {
						 
						 if(ctv_ltodate.isChecked()) {
							
							 if( yr>=tyear &&mon>=tmonth && dy>=tday && hr>=thr && min>=tmin && tyear>=fyear && tmonth>=fmonth && tday>=fday && thr>=fhr && tmin>=fmin ) {
							
								 StringBuilder sfdate=new StringBuilder(fmonth+"/"+fday+"/"+fyear+" "+fhr+":"+fmin);
								 fdate=sfdate.toString();
								 StringBuilder stdate=new StringBuilder(tmonth+"/"+tday+"/"+tyear+" "+thr+":"+tmin);
								 tdate=stdate.toString();
								
								
								 
								 
							 } else {
								 display("Error in date Difference");
							 }
						 } else {
							 
							 StringBuilder sfdate=new StringBuilder(fmonth+"/"+fday+"/"+fyear+" "+fhr+":"+fmin);
							 fdate=sfdate.toString();
							 StringBuilder stdate=new StringBuilder(fmonth+"/"+fday+"/"+fyear+" "+thr+":"+tmin);
							 tdate=stdate.toString();
							 
						 }
						 
						 
						 final CharSequence[] lan = {"Road Roughness","Lane Changes"};
						    AlertDialog.Builder alert = new AlertDialog.Builder(Filter.this);
						    alert.setTitle("Select the Trace");
						    alert.setIcon(R.drawable.choice);
						    alert.setSingleChoiceItems(lan,-1, new DialogInterface.OnClickListener()
						    {
						    	
						        public void onClick(DialogInterface dialog, int which) 
						        {
						        	
						            if(lan[which]=="Road Roughness")
						            {
						            	ttype=1;	
						            }
						            else if (lan[which]=="Lane Changes")
						            {
						            	ttype=2;	
						            }
						        }
						        
						       
						    });
						    alert.setPositiveButton("Trace", new DialogInterface.OnClickListener() { 
				        		public void onClick(DialogInterface dialog, int whichButton) {
				        		in=new Intent(Filter.this,ChartView.class);
				        		in.putExtra("uid", uid);
				        		in.putExtra("fdate", fdate);
				        		in.putExtra("tdate", tdate);
				        		in.putExtra("ttype", ttype);
				        		startActivity(in);
					
								
								
				        		   
				        		}
				        		});
						    alert.show();
						    
						 
					 } else {
						 display("Dont enter the Future date to trace in From date");
					 }
					 
					
      			}
      		});
    	   
    	   tv_ltrace.setOnClickListener(new View.OnClickListener() {
     			
     			@Override
     			public void onClick(View v) {
     				// TODO Auto-generated method stub
     				
     				 int fday = dp_fromdate.getDayOfMonth();
					 int fmonth = dp_fromdate.getMonth() + 1;
					 int fyear = dp_fromdate.getYear();
					 
					 int fhr= tp_frmtm.getCurrentHour();
					 int fmin= tp_frmtm.getCurrentMinute();
					 
					 
					 int tday = dp_todate.getDayOfMonth();
					 int tmonth = dp_todate.getMonth() + 1;
					 int tyear = dp_todate.getYear();
					 
					 int thr= tp_totm.getCurrentHour();
					 int tmin= tp_totm.getCurrentMinute();
					 
					 
					 
					
					 if(yr>=fyear &&mon>=fmonth && dy>=fday && hr>=fhr && min>=fmin ) {
						 
						 if(ctv_ltodate.isChecked()) {
							
							 if( yr>=tyear &&mon>=tmonth && dy>=tday && hr>=thr && min>=tmin && tyear>=fyear && tmonth>=fmonth && tday>=fday && thr>=fhr && tmin>=fmin ) {
							
								 StringBuilder sfdate=new StringBuilder(fmonth+"/"+fday+"/"+fyear+" "+fhr+":"+fmin);
								 fdate=sfdate.toString();
								 StringBuilder stdate=new StringBuilder(tmonth+"/"+tday+"/"+tyear+" "+thr+":"+tmin);
								 tdate=stdate.toString();
								 
								
							 } else {
								 display("Error in date Difference");
							 }
						 } else {
							 StringBuilder sfdate=new StringBuilder(fmonth+"/"+fday+"/"+fyear+" "+fhr+":"+fmin);
							 fdate=sfdate.toString();
							 StringBuilder stdate=new StringBuilder(fmonth+"/"+fday+"/"+fyear+" "+thr+":"+tmin);
							 tdate=stdate.toString();
							 
							 
						 }
						 in=new Intent(Filter.this,ChartView.class);
			        		in.putExtra("uid", uid);
			        		in.putExtra("fdate", fdate);
			        		in.putExtra("tdate", tdate);
			        		in.putExtra("ttype", 3);
			        		startActivity(in);
						 
					 } else {
						 display("Dont enter the Future date to trace in From date");
					 }
					 	 
     				
     			}
     		});
   	   
    	   
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
   				if(uid.equalsIgnoreCase("admin")) {
   					in=new Intent(Filter.this,AdminHome.class);
   					in.putExtra("utype", 1);
   				} else {
   					in=new Intent(Filter.this,Home.class);
   					
   				}
   				in.putExtra("uid", uid);
   				startActivity(in);
   				finish();
   				
   			}
   		});
   		
   		} catch(Exception e) {
   			e.printStackTrace();
   			display(e.toString());
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
