package csp.ela.develop.accidentalertsystem;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity {

	private TextView tv_lname,tv_lpwd,tv_lcpwd,tv_lmno,tv_reg,tv_back,tv_close,tv_hd,tv_clear,tv_shd,tv_luid;
	private EditText et_uid,et_pwd,et_cpwd,et_mno,rt_uid,et_name;
	String uid=null,pwd=null,cpwd=null,mno=null,res=null,suid=null,name;
	private ProgressDialog pro;
	int sflg=1,type;
	Intent in;
	Handler handle=new Handler();
	
	 public static String URL="http://cyberstudents.in/android/safedrive/Service.asmx";
   	 private static final String SOAP_ACTION = "http://tempuri.org/register";
   	  private static final String METHOD_NAME = "register";
   	private static final String NAMESPACE = "http://tempuri.org/";
   	  
   	 private static final String UPDATE_SOAP_ACTION = "http://tempuri.org/register";
  	  private static final String UPDATE_METHOD_NAME = "register";
        
   	 final Intent intnt=new Intent();
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_register);
	        
	        tv_hd=(TextView) findViewById (R.id.title);
	        tv_shd=(TextView) findViewById (R.id.stitle);
	        tv_lname=(TextView) findViewById (R.id.lname);
	        tv_luid=(TextView) findViewById (R.id.luid);
	        tv_lpwd=(TextView) findViewById (R.id.lpwd);
	        
	        tv_lcpwd=(TextView) findViewById (R.id.lcpwd);
	      
	        tv_lmno=(TextView) findViewById (R.id.lmno);
	        
	        tv_reg=(TextView) findViewById (R.id.regis);
	        tv_clear=(TextView) findViewById (R.id.clear);
	        tv_back=(TextView) findViewById (R.id.back);
	        tv_close=(TextView) findViewById (R.id.close);
	        
	        et_name=(EditText) findViewById (R.id.name);
	        et_pwd=(EditText) findViewById (R.id.pwd);
	        et_cpwd=(EditText) findViewById (R.id.cpwd);
	        et_mno=(EditText) findViewById (R.id.mno);
	        et_uid=(EditText) findViewById (R.id.uid);
	        
	        
	        Typeface font = Typeface.createFromAsset(getAssets(), "font/FoglihtenPCS-068.otf");
	        tv_hd.setTypeface(font);
	        tv_shd.setTypeface(font);
	        tv_luid.setTypeface(font);
	        tv_lname.setTypeface(font);
	        tv_lpwd.setTypeface(font);
	        tv_lcpwd.setTypeface(font);
	        tv_lmno.setTypeface(font);
	        tv_reg.setTypeface(font);
	        tv_clear.setTypeface(font);
	        tv_back.setTypeface(font);
	        tv_close.setTypeface(font);
	        
	        
	        final Bundle getdata = getIntent().getExtras();
			if (getdata != null) {
				
				type = getdata.getInt("type");
				if(type==2) {
					uid=getdata.getString("uid");
				}
				
			}
			
	       
	        
	        //------------ For Registration  -----------------
			tv_luid.setVisibility(View.INVISIBLE);
	        et_uid.setVisibility(View.INVISIBLE);
	        et_uid.setEnabled(false);
	        if(type==1) {
	        tv_shd.setText("Account Registration");
	        tv_reg.setText("Register");
	        
	        
	        } else if (type==2) {
	        	tv_shd.setText("Account Updation\n User Id : "+uid);
		        tv_reg.setText("Update");
		       
		       
	        }
	        
	        // ------------------------------------------------
	        
	        
	        tv_reg.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					
					name=et_name.getText().toString();
					pwd=et_pwd.getText().toString();
					cpwd=et_cpwd.getText().toString();
					mno=et_mno.getText().toString();
					
					try {
					if(!(name.equals("") || pwd.equals("") || cpwd.equals("") || mno.equals(""))) {
						if(pwd.equals(cpwd)) {
							if(mno.length()==10) {
							
							
      						 if(type==1) {
      							tv_luid.setVisibility(View.VISIBLE);
      					        et_uid.setVisibility(View.VISIBLE);
      						webmethod(METHOD_NAME,SOAP_ACTION,1);
      						 } else if (type==2){
							webmethod(UPDATE_METHOD_NAME,UPDATE_SOAP_ACTION,2);
      						 }
							} else {
								display("Invalid Mobile No");
							}
							
						} else {
							display("Password Mismatch");
							et_pwd.setText("");
							et_cpwd.setText("");
						}
					} else {
						display("Fill the details corrrectly");
					}
					} catch(Exception e) {
						e.printStackTrace();
						display(e.toString());
					}
					
				}
			});
	        
	       
	        tv_clear.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					et_uid.setText("");
					et_name.setText("");
					et_pwd.setText("");
					et_cpwd.setText("");
					et_mno.setText("");
					
					
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
					
					 
						in=new Intent(Register.this,MainActivity.class);
						startActivity(in);
						finish();
					
					
				}
			});
	        
	       
	        
	        
	 }
	 
	 public boolean webmethod(final String method_name, final String soap_action, final int f) {
		
		 try {
			 pro = ProgressDialog.show(Register.this,"Please Wait","Registering to Server");
			 pro.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				
			 new Thread() {
					public void run() {
				SoapObject request = new SoapObject(NAMESPACE,method_name);
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet=true;				
				request.addProperty("uid",uid);
			    request.addProperty("pwd",pwd);	
			    request.addProperty("name",name);	
			    request.addProperty("mno",mno);	
			    request.addProperty("flg",f);	
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);				
				try {
					androidHttpTransport.call(soap_action, envelope);
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
					        	et_uid.setText(res);
					        	if(type==1) {
					        	in=new Intent(Register.this,Register.class);
					        	} else if(type==2) {
					        		in=new Intent(Register.this,Home.class);
					        		in.putExtra("uid", uid);
					        	}
								startActivity(in);
								finish();
								
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
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
