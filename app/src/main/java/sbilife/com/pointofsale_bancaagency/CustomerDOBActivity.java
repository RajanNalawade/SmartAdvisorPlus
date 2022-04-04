package sbilife.com.pointofsale_bancaagency;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolderDOB;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
@SuppressWarnings("deprecation")
public class CustomerDOBActivity extends AppCompatActivity {
		
	/*
	 * these are all global variables.
	 */
	
	private TextView txtdoberrordesc;
	
	 private List<XMLHolderDOB> lst;
	
	private String strPolicyListErrorCOde;
	
	private ListView lv;
	ArrayList<clsCalendar> lstmain = new ArrayList<clsCalendar>();	
	
	private final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;

	private  final String NAMESPACE = "http://tempuri.org/";
	private  final String URl = ServiceURL.SERVICE_URL;

	private  final String METHOD_NAME_CUSTOMER_DOB = "getAgentCustDoB";
	
	private String SOAP_ACTION_SH = "http://tempuri.org/saveSmartAdvisorServiceHits";
	private String METHOD_NAME_SH = "saveSmartAdvisorServiceHits";
	
	private String strCIFCode = "";
	private String strCIFBDMEmailId = "";
	private String strCIFBDMPassword = "";
	private String strCIFBDMMObileNo = "";

	private DownloadFileAsync taskCustDOB;
	private SelectedAdapter selectedAdapter;
		
	private DatabaseHelper dbhelper;
	private CommonMethods mCommonMethods;
	
	private final Context context = this;
	private String username = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.showdob);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
            
        this.lv = this.findViewById(R.id.listdob);
        
        Button btnsendemailall = findViewById(R.id.btnsendemailall);
        Button btnsendsmsall = findViewById(R.id.btnsendsmsall);
        
        txtdoberrordesc = findViewById(R.id.txtdoberrordesc);
        
        mCommonMethods = new CommonMethods();
        
        dbhelper = new DatabaseHelper(this);
		 username =dbhelper.GetUserName() ;
		mCommonMethods.setApplicationToolbarMenu(this,"Customer DOB");
        
			try {
				strCIFCode = SimpleCrypto.decrypt("SBIL",dbhelper.GetCIFNo());
				strCIFBDMEmailId = SimpleCrypto.decrypt("SBIL",dbhelper.GetEmailId());
				strCIFBDMPassword = dbhelper.GetPassword();
				strCIFBDMMObileNo = SimpleCrypto.decrypt("SBIL",dbhelper.GetMobileNo());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		mProgressDialog = new ProgressDialog(this);
		taskCustDOB = new DownloadFileAsync();
        
        
        //downlaodcustdob();
		service_hits();
		
		btnsendemailall.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if(lv.getCount() != 0)
				{

					if (mCommonMethods.isNetworkConnected(context))
					{		
						 String sal =  "Dear Customer,";
						 String ip0 = "Wish you many happy returns of the day...!";
						 String ipend = "Thanks & Regards,";
						 String ipsign = "SBI Life Insurance Co.  Ltd.";						 
						
						String emailBody = sal + "\n" + ip0 
								+ "\n" + "\n" +ipend + "\n" + ipsign;
						
						List<String> items = new ArrayList<String>();
						items = new ArrayList<String>();
						for(int i=0;i<lst.size();i++)
						{
							if(!lst.get(i).getEmail().contentEquals(""))
							{
								items.add(lst.get(i).getEmail());
							}
						}
						
						Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
						emailIntent.setType("text/plain/email/dir");
						emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Greetings of the day!!");
						emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
						emailIntent.setData(Uri.parse("mailto:" + items));
						try {
							startActivity(emailIntent);
						} catch (android.content.ActivityNotFoundException e) {
							Toast.makeText(context, "There are No Email Client Installed",
									Toast.LENGTH_SHORT).show();
						}					
					}
					else
					{				
						Toast.makeText(context, "Internet Connection Not Present,Try again..",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		btnsendsmsall.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(lv.getCount() != 0)
				{
					String sal =  "Dear Customer,";
					 String ip0 = "Wish you many happy returns of the day...!";
					 String ipend = "Thanks & Regards,";
					 String ipsign = "SBI Life Insurance Co.  Ltd.";						 
					
					String smsBody = sal + "\n" + ip0 
							+ "\n" + "\n" +ipend + "\n" + ipsign;
					
					List<String> items = new ArrayList<String>();
					items = new ArrayList<String>();
					for(int i=0;i<lst.size();i++)
					{
						if(!lst.get(i).getMobile().contentEquals(""))
						{
							items.add(lst.get(i).getMobile());
						}
					}
					
					Uri uri = Uri.parse("smsto:" + items);
			    	Intent smsIntent = new Intent(Intent.ACTION_SENDTO,uri);
					smsIntent.putExtra("sms_body", smsBody);								
					startActivity(smsIntent);
				}
			}
		});
               
	}
	
	private void downlaodcustdob() {
		// String url =
		taskCustDOB.execute();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:
			mProgressDialog = new ProgressDialog(this,ProgressDialog.THEME_HOLO_LIGHT);	
			String Message = "Loading. Please wait...";
			mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(true);

			mProgressDialog.setButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							taskCustDOB.cancel(true);							
							mProgressDialog.dismiss();
						}
					});

			mProgressDialog.setMax(100);
			mProgressDialog.show();
			return mProgressDialog;
		default:
			return null;
		}
	}
	
	class DownloadFileAsync extends AsyncTask<String, String, String> {

		private volatile boolean running = true;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		@Override
		protected String doInBackground(String... aurl) {
			try {

				running = true;			

					SoapObject request = new SoapObject(NAMESPACE,
							METHOD_NAME_CUSTOMER_DOB);
					request.addProperty("strAgentNo", strCIFCode);					
					request.addProperty("strEmailId",strCIFBDMEmailId);
		    		request.addProperty("strMobileNo",strCIFBDMMObileNo);
		    		request.addProperty("strAuthKey",strCIFBDMPassword.trim());

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);
					envelope.dotNet = true;

					envelope.setOutputSoapObject(request);

				// 	allowAllSSL();
					mCommonMethods.TLSv12Enable();
					
					StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
							.permitAll().build();
					StrictMode.setThreadPolicy(policy);

					HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
					try {
						String SOAP_ACTION_CUSTOMER_DOB = "http://tempuri.org/getAgentCustDoB";
						androidHttpTranport.call(SOAP_ACTION_CUSTOMER_DOB,envelope);
						Object response = envelope.getResponse();

						SoapPrimitive sa = null;
						try {
							ParseXML prsObj = new ParseXML();

							if(!response.toString().contentEquals("anyType{}"))
							{
								
							sa = (SoapPrimitive) envelope.getResponse();

						String inputpolicylist = sa.toString();																								

							inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "CIFPolicyList");
							inputpolicylist = new ParseXML().parseXmlTag(inputpolicylist, "ScreenData");
							strPolicyListErrorCOde = inputpolicylist;

							if (strPolicyListErrorCOde == null) {
								inputpolicylist = sa.toString();							

								inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "CIFPolicyList");

								List<String> Node = prsObj.parseParentNode(inputpolicylist, "Table");

								List<XMLHolderDOB> nodeData = prsObj.parseNodeElementCust_DOB(Node);
																							
								lst = new ArrayList<XMLHolderDOB>();
								lst.clear();

								for (XMLHolderDOB node : nodeData) {

									lst.add(node);
								}

								selectedAdapter = new SelectedAdapter(context,0, lst);
								selectedAdapter.setNotifyOnChange(true);
								
								registerForContextMenu(lv);

							} else {

							}
							}
							else
							{
								
							}

						} catch (Exception e) {
							try {
								throw (e);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							mProgressDialog.dismiss();
							running = false;
						}

					} catch (IOException e) {
						try {
							throw (e);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						mProgressDialog.dismiss();
						running = false;
					} catch (XmlPullParserException e) {
						try {
							throw (e);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						mProgressDialog.dismiss();
						running = false;
					}
			
			} catch (Exception e) {
				try {
					throw (e);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				mProgressDialog.dismiss();
				running = false;
			}
			return null;

		}

		/*
		 * it is shows the progress of async task
		 */
		
		@Override
		protected void onProgressUpdate(String... progress) {
			Log.d("ANDRO_ASYNC", progress[0]);
			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}

		/*
		 * it is execute after complete the progress of async task
		 */
		
		@Override
		protected void onPostExecute(String unused) {
			
			try{
				dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
				}catch(Exception e){
					e.printStackTrace();
				}
			
			if (running) {
				
					if (strPolicyListErrorCOde == null)
					{	
						txtdoberrordesc.setVisibility(View.GONE);
						
						lv.setAdapter(selectedAdapter);						
					}
					else 
					{
						txtdoberrordesc.setVisibility(View.VISIBLE);
						txtdoberrordesc.setText("No Birthdays");
						Toast.makeText(context, "No Birthdays", Toast.LENGTH_LONG).show();
					}				
			} else {
				syncerror();
			}
		}
	}
	
	private void service_hits() 
	{
		//mProgressDialog = ProgressDialog.show(context,"Loading", "Please Wait");
		
		mProgressDialog = new ProgressDialog(this,ProgressDialog.THEME_HOLO_LIGHT);			
		String Message = "Loading Please wait...";			
		mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
		
		ServiceHits service = new ServiceHits(context,mProgressDialog,NAMESPACE,URl,SOAP_ACTION_SH,METHOD_NAME_SH);
		service.execute();		
	}


class ServiceHits extends AsyncTask<String, Void, String>
{
	Context mContext;
	ProgressDialog progressDialog=null;
	String NAMESPACE = "";
	String URL = "";
	String SOAP_ACTION = "";
	String METHOD_NAME = "";	

	ServiceHits(Context mContext, ProgressDialog progressDialog, String NAMESPACE, String URL, String SOAP_ACTION, String METHOD_NAME)
	{
		// TODO Auto-generated constructor stub

		this.NAMESPACE = NAMESPACE;
		this.URL = URL;
		this.SOAP_ACTION = SOAP_ACTION;
		this.METHOD_NAME = METHOD_NAME;
		this.mContext = mContext;
		this.progressDialog =progressDialog;		
	}

	@Override
	protected String doInBackground(String... param) {
		// TODO Auto-generated method stub

		if(mCommonMethods.isNetworkConnected(context))
		{

			try {
									
				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
														
				request.addProperty("serviceName",METHOD_NAME_CUSTOMER_DOB);				
				request.addProperty("strProdCode","");
				request.addProperty("serviceInput","");
				request.addProperty("serviceReqUserId",strCIFCode);
				request.addProperty("strEmailId",strCIFBDMEmailId);
				request.addProperty("strMobileNo",strCIFBDMMObileNo);					
				request.addProperty("strAuthKey",strCIFBDMPassword.trim());
				

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				//Enable this envelope if service is written in dot net
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

			// 	allowAllSSL();
				mCommonMethods.TLSv12Enable();

				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);

				androidHttpTransport.call(SOAP_ACTION, envelope);
				SoapPrimitive response = (SoapPrimitive)envelope.getResponse();  

				String result = response.toString();

				if (result.contains("1")) {						
					return "Success";
				} else {
					return "Failure";
				}

			}
			catch(Exception e)
			{					
				return "Server not Found. Please try after some time.";
			}

		}
		else
			return "Please Activate Internet connection";

	}

	@Override
	protected void  onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		try
		{
		if(progressDialog.isShowing())
			progressDialog.dismiss();
		
		}
		catch(Exception e)
		{
			e.getMessage();
		}

		taskCustDOB = new DownloadFileAsync();		

		if(result.equals("Success"))
		{								
			downlaodcustdob();			
		}
		else
		{						
			downlaodcustdob();			
		}
	}
}
		
	class SelectedAdapter extends ArrayAdapter<XMLHolderDOB> {

		// used to keep selected position in ListView
		private int selectedPos = -1; // init value for not-selected

		List<XMLHolderDOB> lst;

		SelectedAdapter(Context context, int textViewResourceId,
						List<XMLHolderDOB> objects) {
			super(context, textViewResourceId, objects);

			lst = objects;
		}

		public void setSelectedPosition(int pos) {
			selectedPos = pos;
			// inform the view of this change
			notifyDataSetChanged();
		}

		public int getSelectedPosition() {
			return selectedPos;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View v = convertView;				
					
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) this.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.customerdob, null);
			}
			
			TextView txtdob = v.findViewById(R.id.txtcustdob);
			TextView txtconon = v.findViewById(R.id.txtcustconno);
			TextView txtemail = v.findViewById(R.id.txtcustemail);
			
			ImageView imgcontact = v.findViewById(R.id.imgcontact_c);
			ImageView imgemail = v.findViewById(R.id.imgemail_c);

			Object obj = null;
			boolean i = lst.contains(obj);
			
			imgcontact.setOnClickListener(new OnClickListener() {
				
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					if(lv.getCount() != 0)
					{
						String sal =  "Dear Customer,";
						 String ip0 = "Wish you many happy returns of the day...!";
						 String ipend = "Thanks & Regards,";
						 String ipsign = "SBI Life Insurance Co.  Ltd.";						 
						
						final String smsBody = sal + "\n" + ip0 
								+ "\n" + "\n" +ipend + "\n" + ipsign;
						
						if(!lst.get(position).getMobile().contentEquals(""))
						{
							/*Uri uri = Uri.parse("smsto:" + lst.get(position).getMobile());
					    	Intent smsIntent = new Intent(Intent.ACTION_SENDTO,uri);
							smsIntent.putExtra("sms_body", smsBody);								
							startActivity(smsIntent);*/
							
							final Dialog dialog = new Dialog(context);		
		    		 		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 	
		    		 		dialog.setContentView(R.layout.loading_window_twobutton);		
		    		 		TextView text = dialog.findViewById(R.id.txtalertheader);
		    		 		text.setText("Do you want to make the call.");		
		    		 		Button dialogButton = dialog.findViewById(R.id.btnalert);
		    		 		dialogButton.setOnClickListener(new OnClickListener() {			
		    		 			public void onClick(View v) {
		    		 				dialog.dismiss();    		 				    		 			
		    		 				
		    		 				Uri uri = Uri.parse("smsto:" + lst.get(position).getMobile());
							    	Intent smsIntent = new Intent(Intent.ACTION_SENDTO,uri);
									smsIntent.putExtra("sms_body", smsBody);								
									startActivity(smsIntent);
		    		 			}
		    		 		});
		    		 		Button dialogButtoncancel = dialog.findViewById(R.id.btnalertcancel);
		    		 		dialogButtoncancel.setOnClickListener(new OnClickListener() {			
		    		 			public void onClick(View v) {
		    		 				dialog.dismiss();
		    		 			}
		    		 		});
		    		 		
		    		 		dialog.show();
						}											
					}
				}
			});
			
			imgemail.setOnClickListener(new OnClickListener() {
				
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					if(lv.getCount() != 0)
					{

						if (mCommonMethods.isNetworkConnected(context))
						{		
							 String sal =  "Dear Customer,";
							 String ip0 = "Wish you many happy returns of the day...!";
							 String ipend = "Thanks & Regards,";
							 String ipsign = "SBI Life Insurance Co.  Ltd.";						 
							
							String emailBody = sal + "\n" + ip0 
									+ "\n" + "\n" +ipend + "\n" + ipsign;
							
							if(!lst.get(position).getEmail().contentEquals(""))
							{
								Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
								emailIntent.setType("text/plain/email/dir");
								emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Greetings of the day!!");
								emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
								emailIntent.setData(Uri.parse("mailto:" + lst.get(position).getEmail()));
								try {
									startActivity(emailIntent);
								} catch (android.content.ActivityNotFoundException e) {
									Toast.makeText(context, "There are No Email Client Installed",
											Toast.LENGTH_SHORT).show();
								}
							}																	
						}
						else
						{				
							Toast.makeText(context, "Internet Connection Not Present,Try again..",
									Toast.LENGTH_SHORT).show();
						}
					}
				}
			});
			
			

			if (!i) {
				
				if(lst.get(position).getMobile().contentEquals(""))
				{
					imgcontact.setVisibility(View.GONE);
				}
				else
				{
					imgcontact.setVisibility(View.VISIBLE);
				}
				
				if(lst.get(position).getEmail().contentEquals(""))
				{
					imgemail.setVisibility(View.GONE);
				}
				else
				{
					imgemail.setVisibility(View.VISIBLE);
				}
				
				txtdob.setText(lst.get(position).getDOB());
				txtconon.setText(lst.get(position).getMobile());
				txtemail.setText(lst.get(position).getEmail());							
			}

			return (v);
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu,View v,ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, v, menuInfo);
		
		//MenuInflater inflater = getMenuInflater();
		
		menu.setHeaderTitle("Services");
		menu.add(0,v.getId(),0,"Email");
		menu.add(0,v.getId(),1,"Call");			
		
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();							
		
			if(item.getTitle().toString().contentEquals("Email"))
			{
				String strEmail = lst.get(info.position).getEmail();
    			
    			if(strEmail != "")
    			{    				
    				Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
    				emailIntent.setType("text/plain/email/dir");
    				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
    				emailIntent.putExtra(Intent.EXTRA_TEXT, "");						
    				emailIntent.setData(Uri.parse("mailto:" + strEmail));
    				try
    				{
    					startActivity(emailIntent);
    				}
    				catch (android.content.ActivityNotFoundException e) {
    					Toast.makeText(context, "There are No Email Client Installed", Toast.LENGTH_SHORT).show();
    				}
    			}
			}
			else {
				String strmob = lst.get(info.position).getMobile();

				if (strmob != "") {
					mCommonMethods.callMobileNumber(strmob, context);
				}

			}
		
		return true;

		}
	
	private void syncerror()
    {	    		    
    	final Dialog dialog = new Dialog(this);
    	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
    	dialog.setContentView(R.layout.loading_window);		
    	TextView text = dialog.findViewById(R.id.txtalertheader);
    	text.setText("Server Not Responding,Try again..");		
    	Button dialogButton = dialog.findViewById(R.id.btnalert);
    	dialogButton.setOnClickListener(new OnClickListener() {			
    		public void onClick(View v) {
    			dialog.dismiss();	    			
    		}
    	});
    	dialog.show();	    		    	
    }

	 @Override
		public void onBackPressed() {
		 Intent i = new Intent(this,CarouselCalendarView.class);
		 i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 startActivity(i);
		 finish();
		}
	

}