package csp.ela.develop.accidentalertsystem;





import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;






import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class ChartView extends Activity {
private GraphicalView ymChart;

private ListView lview;
ArrayAdapter<String> spview;


ProgressDialog pbar;
String uid,fdate,tdate,chk,gtype;
int rcount=0;
String[][] gval=new String[100][15];
int cnt=0,ttype=0;
Handler handle=new Handler();
LinearLayout chartContainer;
	
	
	private XYSeries yvisitsSeries ;
	
	
	
	private XYMultipleSeriesDataset dataset;
	private XYSeriesRenderer visitsRenderer;
	private XYMultipleSeriesRenderer multiRenderer;
	
	public static String url="http://cyberstudents.in/android/safedrive/Service.asmx";
	private static final String NAMESPACE = "http://tempuri.org/";
	private static final String SOAP_ACTION = "http://tempuri.org/traces";
	private static final String METHOD_NAME = "traces";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart_view);
		
		final Bundle getdata = getIntent().getExtras();
		if (getdata != null) {
			uid= getdata.getString("uid");
			fdate= getdata.getString("fdate");
			tdate= getdata.getString("tdate");
			ttype= getdata.getInt("ttype");
			if(ttype==2) {
				gtype="Lane Changes";
			} else if(ttype==1) {
				gtype="Road Roughness";
			} else if(ttype==3) {
				
			}
		}
		
		chartContainer = (LinearLayout) findViewById(R.id.mapview);
		lview = new ListView(ChartView.this);
		lview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    	
    			
    	spview = new ArrayAdapter<String>(this,R.layout.textview);
		lview.setAdapter(spview);
		
		
		pbar = ProgressDialog.show(ChartView.this,"Wait","Fetching Available Details");
		pbar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		
		
		if(ttype==3) {
			vlist();
		}
		
		
		
		new Thread() {
			public void run() {
				
				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet = true;
				request.addProperty("uid", uid);
				request.addProperty("fdate", fdate);
				request.addProperty("tdate", tdate);
				
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
				rcount = (res.getPropertyCount());
				
				pbar.dismiss();
				
				if (rcount>=1) {
					chk=res.getProperty(0).toString();
					if(!(chk.equalsIgnoreCase("Nil") || chk.equalsIgnoreCase("Error"))) {
					for (int i = 0; i < rcount; i++) {
						gval[cnt][0]=res.getProperty(i).toString();
						i++;	
						gval[cnt][1]=res.getProperty(i).toString();
						i++;
						gval[cnt][2]=res.getProperty(i).toString();
						i++;
						gval[cnt][3]=res.getProperty(i).toString();
						i++;
						gval[cnt][4]=res.getProperty(i).toString();
						i++;
						gval[cnt][5]=res.getProperty(i).toString();
						i++;
						gval[cnt][6]=res.getProperty(i).toString();
						i++;
						gval[cnt][7]=res.getProperty(i).toString();
						i++;
						gval[cnt][8]=res.getProperty(i).toString();
						i++;
						gval[cnt][9]=res.getProperty(i).toString();
						
				
						cnt++;
					}
				} else {
					
					rcount=0;
					cnt=0;
				}
					} else {
					runOnUiThread(new Runnable() {
				        @Override
					        public void run() {
				        	    	display("No Entries");
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
						if((chk.equalsIgnoreCase("Nil") || chk.equalsIgnoreCase("Error"))) {
						display("No Traces");
						}
						if(cnt>0) {
							if(ttype!=3) {
						 setupChart();
						 new ChartTask().execute();
							} else if(ttype==3) {
								for(int i=0;i<cnt;i++) {
									spview.add("Date & Time : "+gval[i][0]+"\nAddress : "+gval[i][6]+", "+gval[i][7]+", "+gval[i][8]);
								}
								
							}
						}
					}
				});
			}	
		}.start();
		
		
	}
	
	
	public void vlist() {
		
		chartContainer.addView(lview);
	}
	 private void setupChart(){
	    	
		 	
	    	yvisitsSeries = new XYSeries(gtype);
	    	dataset = new XYMultipleSeriesDataset();
	    	dataset.addSeries(yvisitsSeries);   
	    	
	    	
	    	//dataset.addSeries(xvisitsSeries);  
	    	
	    	
	    	//dataset.addSeries(xvisitsSeries); 
	    	
	    	visitsRenderer = new XYSeriesRenderer();
	    	visitsRenderer.setColor(Color.LTGRAY);
	    	visitsRenderer.setPointStyle(PointStyle.CIRCLE);
	    	visitsRenderer.setFillPoints(true);
	      	visitsRenderer.setDisplayChartValues(true);
	    	
	      	multiRenderer = new XYMultipleSeriesRenderer();
	    	multiRenderer.setAxisTitleTextSize(16);
	    	multiRenderer.setChartTitleTextSize(20);
	    	multiRenderer.setLabelsTextSize(15);
	    	multiRenderer.setLegendTextSize(15);
	    	multiRenderer.setPointSize(5f);
	    	multiRenderer.setMargins(new int[] { 20, 30, 15, 20 });
	    	
	    	multiRenderer.setChartTitle(gtype);
	    	multiRenderer.setXTitle("Seconds");
	    	multiRenderer.setYTitle("Accleration");
	    	//multiRenderer.setZoomButtonsVisible(true);
	    	
	    	multiRenderer.setXAxisMin(0);
	    	multiRenderer.setXAxisMax(10);
	    	
	    	multiRenderer.setYAxisMin(0);
	    	multiRenderer.setYAxisMax(10);
	    	
	    	//multiRenderer.setBarSpacing(2);
	    	
	    	
	    	// Adding visitsRenderer to multipleRenderer
	    	// Note: The order of adding dataseries to dataset and renderers to multipleRenderer
	    	// should be same
	    	multiRenderer.addSeriesRenderer(visitsRenderer);
	    	
	    	// Getting a reference to LinearLayout of the MainActivity Layout
	    	LinearLayout chartContainer = (LinearLayout) findViewById(R.id.mapview);
	    	
	   		
	    	//mChart = (GraphicalView) ChartFactory.getBarChartView(getBaseContext(), dataset, multiRenderer, Type.DEFAULT);
	    	//mChart = (GraphicalView) ChartFactory.getTimeChartView(getBaseContext(), dataset, multiRenderer,"MMM yyyy");
	    	//mChart = (GraphicalView) ChartFactory.getLineChartView(getBaseContext(), dataset, multiRenderer);
	    	
	    	//xmChart = (GraphicalView) ChartFactory.getCubeLineChartView(getBaseContext(), xdataset, multiRenderer, 0.5f);
	   		// Adding the Line Chart to the LinearLayout
	    	
	    	
	    	ymChart = (GraphicalView) ChartFactory.getCubeLineChartView(getBaseContext(), dataset, multiRenderer, 0.5f);
	    	
	    	chartContainer.addView(ymChart);
	    	//chartContainer.addView(xmChart);
	    	
	    	
	    }
	 private class ChartTask extends AsyncTask<Void, String, Void>{

	    	// Generates dummy data in a non-ui thread
			@Override
			protected Void doInBackground(Void... params) {
				int i = 0;
				try{
					while(i<(cnt)) {
						String [] values = new String[4];
						values[0] = i+"";
						values[1] = gval[i][1];
						values[2] = gval[i][2];
						//values[3] = gval[i][2];
						
						publishProgress(values);					
						Thread.sleep(1000);
						i++;
					}
				}catch(Exception e){ }
				return null;
			}
			
			// Plotting generated data in the graph
			@Override
			protected void onProgressUpdate(String... values) {
				if(ttype==1)
				yvisitsSeries.add(Integer.parseInt(values[0]), Float.parseFloat(values[2]));
				if(ttype==2)
				yvisitsSeries.add(Integer.parseInt(values[0]), Float.parseFloat(values[1]));
			//	xvisitsSeries.add(Integer.parseInt(values[0]), Float.parseFloat(values[2]));
				ymChart.repaint();
				ymChart.scrollTo(1000,1000);
			
				
			}
	    	
	    }    
	 public void display(String msg) {
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
		}
}
