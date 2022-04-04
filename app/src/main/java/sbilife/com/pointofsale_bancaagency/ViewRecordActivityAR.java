package sbilife.com.pointofsale_bancaagency;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolderSubCategory;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.home.clsActivitySubCategory;
import sbilife.com.pointofsale_bancaagency.home.clsCalendarActivityRecorder;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
@SuppressWarnings("deprecation")
public class ViewRecordActivityAR extends AppCompatActivity
{
	private Spinner selActivityTime_view;
	
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
	private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	private String strId = "";
	//String username = "";
	private DownloadSubActivity taskSubActivity;
	private static final int DIALOG_DOWNLOAD_PROGRESS = 1;
	 private ProgressDialog mProgressDialog;
	 
	 private static final String NAMESPACE = "http://tempuri.org/";
		//private static final String URl = "http://172.17.114.196/posservics/Service.asmx?wsdl";
		//private static final String URl = "https://125.18.9.94/service.asmx?wsdl";
	 	private static final String URl = ServiceURL.SERVICE_URL;
//		private static final String SOAP_ACTION_CATEGORY = "http://tempuri.org/getCategoriesList";
//		private static final String METHOD_NAME_CATEGORY = "getCategoriesList";
		
		private static final String SOAP_ACTION_SUBCATEGORY = "http://tempuri.org/getSubCategoriesList";
		private static final String METHOD_NAME_SUBCATEGORY = "getSubCategoriesList";
	 
	private DatabaseHelper db;
	private ArrayList<clsActivitySubCategory> lstevent = new ArrayList<clsActivitySubCategory>();
	private String strSubCategoryName;
	
	private int hour;
	private int minute;

    private static final int TIME_DIALOG_ID = 999;
	private int mYear;
	private int mMonth;
	private int mDay;
	private static final int DATE_DIALOG_ID = 0;
	int strmonth;
	int stryear;
	private String y;
	private String m;
	private String d;
	
	private int timecheck = 0;
	
	private TextView effectiveDate;
	private TextView timeFrom;
	private TextView timeTo;
	private EditText edBranch;
	private EditText edActivity;
	private EditText rdremark;
	private EditText rdactlead;
	private Spinner selSubActivity;
	int hourFrom,minuteFrom,hourTo,minuteTo;
	private String strampm;
	private Button btnSubmit;
	private Button back;
	
    private final Context context = this;
	
	protected	ListView lv;	
	
	private String strDate;
	private String strBranch;
	private String strActivity;
	private String strremark;
	private String strtime;
	private String strSubActivity;
	private String strTimeTo;
	private String strRowId;
	private String strLead;
	
	//for store userid in bdm tracker table
	private String strCIFBDMUserId;
			private String strCIFBDMEmailId = "";
			private String strCIFBDMPassword = "";
			private String strCIFBDMMObileNo = "";
	private CommonMethods mCommonMethods;
			
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.viewrecordar);
		
		mCommonMethods = new CommonMethods();
		mCommonMethods.setActionbarLayout(this);
		
		db = new DatabaseHelper(this);
		mCommonMethods.setApplicationToolbarMenu(this,"Video");
		taskSubActivity = new DownloadSubActivity();
		
		effectiveDate = findViewById(R.id.effectiveDate);
		edBranch = findViewById(R.id.Branch);
		edActivity = findViewById(R.id.Activity);
		selSubActivity = findViewById(R.id.selSubActivity);
		timeFrom = findViewById(R.id.timeFrom);
		timeTo = findViewById(R.id.timeTo);
		rdremark = findViewById(R.id.rdremark);
		rdactlead = findViewById(R.id.rdactlead);
		
		selActivityTime_view = findViewById(R.id.selActivityTime_view);
		
		//store userid in bdm trackler table
			try {
				strCIFBDMUserId = SimpleCrypto.decrypt("SBIL",db.GetCIFNo());
				strCIFBDMEmailId = SimpleCrypto.decrypt("SBIL",db.GetEmailId());
				strCIFBDMPassword = db.GetPassword();
				strCIFBDMMObileNo = SimpleCrypto.decrypt("SBIL",db.GetMobileNo());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		//strCIFBDMUserId = "12089";
		
		Intent i = getIntent();
		strDate = i.getStringExtra("Date");
		strBranch = i.getStringExtra("Branch");
		strActivity = i.getStringExtra("Activity");
		strremark = i.getStringExtra("Remark");
		strtime = i.getStringExtra("Time");
		strSubActivity = i.getStringExtra("SubActivity");
		strTimeTo = i.getStringExtra("TimeTo");	
		strLead = i.getStringExtra("ActivityLead");
		
		
		strRowId = i.getStringExtra("RowId");	
		
		effectiveDate.setText(strDate);		
		edBranch.setText(strBranch);
		edActivity.setText(strActivity);
		rdremark.setText(strremark);
		timeFrom.setText(strtime);
		
		timeTo.setText(strTimeTo);
		
		rdactlead.setText(strLead);
		
		
		
		if(strtime.contentEquals("1st Half"))
		{
			selActivityTime_view.setSelection(1);
		}
		else if(strtime.contentEquals("2nd Half"))
		{
			selActivityTime_view.setSelection(2);
		}
		else if(strtime.contentEquals("Full Day"))
		{
			selActivityTime_view.setSelection(3);
		}
		
		//selSubActivity.setSelection(1);
		
		/*final Calendar c = Calendar.getInstance();
		hourFrom = c.get(Calendar.HOUR_OF_DAY);
		hourTo = c.get(Calendar.HOUR_OF_DAY);
		minuteFrom = c.get(Calendar.MINUTE);
		minuteTo = c.get(Calendar.MINUTE);*/
		
		Calendar cal = Calendar.getInstance();
        mYear= cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);             
		hour = cal.get(Calendar.HOUR_OF_DAY);
		minute = cal.get(Calendar.MINUTE);

        int ampm = cal.get(Calendar.AM_PM);
		if (ampm == 0) {
			strampm = "AM";
		} else {
			strampm = "PM";
		}
		
		//set current time into textview		
		/*timeFrom.setText(new StringBuilder().append(pad(hour)).append(":")
				.append(pad(minute)).append(" ").append(strampm));		
		timeTo.setText(new StringBuilder().append(pad(hour)).append(":")
				.append(pad(minute)).append(" ").append(strampm));*/

		System.out.println("activity **" + strActivity+"**");
		
		selSubActivity.setOnItemSelectedListener(new OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
													
					 strSubCategoryName  = lstevent.get(position).getName();					
					 										
				}

				public void onNothingSelected(AdapterView<?> parent) {

				}
			});
		
		//if(strActivity.trim().equals("Met CIF"))
		if(strActivity.trim().equals("Business Connect"))
		{
			 strId = db.getActivityId(strActivity);
			
			int count = db.GetSubActivityCount(strId);
			  if(count == 0)
			  {
				  taskSubActivity = new DownloadSubActivity();				  
				  startDownloadSubActivity(); 
			  }
			  else
			  {				  		
			
			Cursor c1 = db.getSubActivityName(strId);								
			
			lstevent.clear();

			if (c1.getCount() > 0) {
				c1.moveToFirst();
				for (int ii = 0; ii < c1.getCount(); ii++) {
					clsActivitySubCategory single = new clsActivitySubCategory("", "", "", "", "", "", "","");
					single.setName(c1.getString(c1.getColumnIndex("ActivitySubCategoryName")));					
					lstevent.add(single);
					c1.moveToNext();
				}
			}
			
			/*ItemsAdapterSubActivity adapter = new ItemsAdapterSubActivity(context,0, lstevent);
			adapter.setNotifyOnChange(true);
			selSubActivity.setAdapter(adapter);*/
			
			Spinner_BaseAdapterCategory selectedAdapterCategory = new Spinner_BaseAdapterCategory(context,lstevent);			
			selSubActivity.setAdapter(selectedAdapterCategory);	
			
		}
			
			
			/*System.out.println("Inside Met CIF");
			String [] subActivityList = {"Select","Product Training","MIS Sharing & Business"};
			ArrayAdapter<String> subActivityAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_list_item_1, android.R.id.text1,subActivityList);
			selSubActivity.setAdapter(subActivityAdapter);
			subActivityAdapter.notifyDataSetChanged();*/
			
			
			if(!strSubActivity.contentEquals(""))
			{			
			
				//selSubActivity.setSelection(getIndex(selectedAdapterCategory,strSubActivity));
				
				/*for(int ia=0;ia<selectedAdapterCategory.getCount();ia++)
				{					
					if(strSubActivity.trim().equals(selectedAdapterCategory.getItem(ia).toString()))
					{
						selSubActivity.setSelection(ia);
						
						break;
					}														
				}*/
				
				selSubActivity.setSelection(getItemIndexById(strSubActivity));
				
			 /*
			
			 int textlength = strSubActivity.length();
			 array_sort.clear();
			 for (int ii = 0; ii < lstevent.size(); ii++)
			 {									
			 if (textlength <= lstevent.get(ii).getName().length())
			 {
			 if(strSubActivity.toString().equalsIgnoreCase((String)lstevent.get(ii).getName().subSequence(0,textlength)))
			 {											
			 array_sort.add(lstevent.get(ii));												
			 }
			 }
			 }
			 
			 ItemsAdapterSubActivity adap = new ItemsAdapterSubActivity(context,0, array_sort);
			 adap.setNotifyOnChange(true);
			 selSubActivity.setAdapter(adap);
			 
			 Spinner_BaseAdapterCategory selectedAdapterCat = new Spinner_BaseAdapterCategory(context,array_sort);			
				selSubActivity.setAdapter(selectedAdapterCat);	*/
				
			}
		}
		//else if(strActivity.equals("Met BM"))
		else if(strActivity.equals("Promotional Activities"))
		{
			/*String [] subActivityList = {"Select","MIS Sharing & Business","R & R and Campain Discussion"};
			ArrayAdapter<String> subActivityAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_list_item_1, android.R.id.text1,subActivityList);
			selSubActivity.setAdapter(subActivityAdapter);
			subActivityAdapter.notifyDataSetChanged();*/
			
			 strId = db.getActivityId(strActivity);
			
			int count = db.GetSubActivityCount(strId);
			  if(count == 0)
			  {
				  taskSubActivity = new DownloadSubActivity();				  
				  startDownloadSubActivity(); 
			  }
			  else
			  {		
			Cursor c1 = db.getSubActivityName(strId);								
			
			lstevent.clear();

			if (c1.getCount() > 0) {
				c1.moveToFirst();
				for (int ii = 0; ii < c1.getCount(); ii++) {
					clsActivitySubCategory single = new clsActivitySubCategory("", "", "", "", "", "", "","");
					single.setName(c1.getString(c1.getColumnIndex("ActivitySubCategoryName")));					
					lstevent.add(single);
					c1.moveToNext();
				}
			}

			Spinner_BaseAdapterCategory selectedAdapterCategory = new Spinner_BaseAdapterCategory(context,lstevent);			
						selSubActivity.setAdapter(selectedAdapterCategory);
						
			  }

			if(!strSubActivity.contentEquals(""))
						{
			selSubActivity.setSelection(getItemIndexById(strSubActivity));
			}

		}
		//else if(strActivity.equals("Recruitment of CIF"))
		else if(strActivity.equals("Bank Connect"))
		{
			 strId = db.getActivityId(strActivity);
			
			int count = db.GetSubActivityCount(strId);
			  if(count == 0)
			  {
				  taskSubActivity = new DownloadSubActivity();				  
				  startDownloadSubActivity(); 
			  }
			  else
			  {		
			
			Cursor c1 = db.getSubActivityName(strId);								
			
			lstevent.clear();

			if (c1.getCount() > 0) {
				c1.moveToFirst();
				for (int ii = 0; ii < c1.getCount(); ii++) {
					clsActivitySubCategory single = new clsActivitySubCategory("", "", "", "", "", "", "","");
					single.setName(c1.getString(c1.getColumnIndex("ActivitySubCategoryName")));					
					lstevent.add(single);
					c1.moveToNext();
				}
			}

			Spinner_BaseAdapterCategory selectedAdapterCategory = new Spinner_BaseAdapterCategory(context,lstevent);			
						selSubActivity.setAdapter(selectedAdapterCategory);
						
			  }

			if(!strSubActivity.contentEquals(""))
						{
			selSubActivity.setSelection(getItemIndexById(strSubActivity));
			}

			
			/*String [] subActivityList = {"Select","In-house CRP"};
			ArrayAdapter<String> subActivityAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_list_item_1, android.R.id.text1,subActivityList);
			selSubActivity.setAdapter(subActivityAdapter);
			subActivityAdapter.notifyDataSetChanged();*/
		}
		//else if(strActivity.equals("CRP"))
		else if(strActivity.equals("Customer Servicing"))
		{
			 strId = db.getActivityId(strActivity);
			
			int count = db.GetSubActivityCount(strId);
			  if(count == 0)
			  {
				  taskSubActivity = new DownloadSubActivity();				  
				  startDownloadSubActivity(); 
			  }
			  else
			  {		
			
			Cursor c1 = db.getSubActivityName(strId);								
			
			lstevent.clear();

			if (c1.getCount() > 0) {
				c1.moveToFirst();
				for (int ii = 0; ii < c1.getCount(); ii++) {
					clsActivitySubCategory single = new clsActivitySubCategory("", "", "", "", "", "", "","");
					single.setName(c1.getString(c1.getColumnIndex("ActivitySubCategoryName")));					
					lstevent.add(single);
					c1.moveToNext();
				}
			}

			Spinner_BaseAdapterCategory selectedAdapterCategory = new Spinner_BaseAdapterCategory(context,lstevent);			
						selSubActivity.setAdapter(selectedAdapterCategory);
						
			  }

			if(!strSubActivity.contentEquals(""))
						{
			selSubActivity.setSelection(getItemIndexById(strSubActivity));
			}

			
			/*String [] subActivityList = {"Select","In-house CRP"};
			ArrayAdapter<String> subActivityAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_list_item_1, android.R.id.text1,subActivityList);
			selSubActivity.setAdapter(subActivityAdapter);
			subActivityAdapter.notifyDataSetChanged();*/
		}
		//else if(strActivity.equals("Visit to RBO"))
		else if(strActivity.equals("SBI Life Office"))
		{
			 strId = db.getActivityId(strActivity);
			
			int count = db.GetSubActivityCount(strId);
			  if(count == 0)
			  {
				  taskSubActivity = new DownloadSubActivity();				  
				  startDownloadSubActivity(); 
			  }
			  else
			  {		
			
			Cursor c1 = db.getSubActivityName(strId);								
			
			lstevent.clear();

			if (c1.getCount() > 0) {
				c1.moveToFirst();
				for (int ii = 0; ii < c1.getCount(); ii++) {
					clsActivitySubCategory single = new clsActivitySubCategory("", "", "", "", "", "", "","");
					single.setName(c1.getString(c1.getColumnIndex("ActivitySubCategoryName")));					
					lstevent.add(single);
					c1.moveToNext();
				}
			}

			Spinner_BaseAdapterCategory selectedAdapterCategory = new Spinner_BaseAdapterCategory(context,lstevent);			
						selSubActivity.setAdapter(selectedAdapterCategory);
						
			  }

			if(!strSubActivity.contentEquals(""))
						{
			selSubActivity.setSelection(getItemIndexById(strSubActivity));
			}

			
			/*String [] subActivityList = {"Select","Met Region Manager"};
			ArrayAdapter<String> subActivityAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_list_item_1, android.R.id.text1,subActivityList);
			selSubActivity.setAdapter(subActivityAdapter);
			subActivityAdapter.notifyDataSetChanged();*/
		}
		//else if(strActivity.equals("Visit to RACPC etc"))
		else if(strActivity.equals("Misc."))
		{
			 strId = db.getActivityId(strActivity);
			
			int count = db.GetSubActivityCount(strId);
			  if(count == 0)
			  {
				  taskSubActivity = new DownloadSubActivity();				  
				  startDownloadSubActivity(); 
			  }
			  else
			  {		
				  			  
			Cursor c1 = db.getSubActivityName(strId);								
			
			lstevent.clear();

			if (c1.getCount() > 0) {
				c1.moveToFirst();
				for (int ii = 0; ii < c1.getCount(); ii++) {
					clsActivitySubCategory single = new clsActivitySubCategory("", "", "", "", "", "", "","");
					single.setName(c1.getString(c1.getColumnIndex("ActivitySubCategoryName")));					
					lstevent.add(single);
					c1.moveToNext();
				}
			}

			Spinner_BaseAdapterCategory selectedAdapterCategory = new Spinner_BaseAdapterCategory(context,lstevent);			
						selSubActivity.setAdapter(selectedAdapterCategory);
						
			  }

			if(!strSubActivity.contentEquals(""))
						{
			selSubActivity.setSelection(getItemIndexById(strSubActivity));
			}

			
			/*String [] subActivityList = {"Select","Met Region Manager"};
			ArrayAdapter<String> subActivityAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_list_item_1, android.R.id.text1,subActivityList);
			selSubActivity.setAdapter(subActivityAdapter);
			subActivityAdapter.notifyDataSetChanged();*/
		}
		//else if(strActivity.equals("SBI Life Office"))
		/*else if(strActivity.equals("SBI Life Office"))
		{
			 strId = db.getActivityId(strActivity);
			
			int count = db.GetSubActivityCount(strId);
			  if(count == 0)
			  {
				  taskSubActivity = new DownloadSubActivity();				  
				  startDownloadSubActivity(); 
			  }
			  else
			  {		
			
			Cursor c1 = db.getSubActivityName(strId);								
			
			lstevent.clear();

			if (c1.getCount() > 0) {
				c1.moveToFirst();
				for (int ii = 0; ii < c1.getCount(); ii++) {
					clsActivitySubCategory single = new clsActivitySubCategory("", "", "", "", "", "", "","");
					single.setName(c1.getString(c1.getColumnIndex("ActivitySubCategoryName")));					
					lstevent.add(single);
					c1.moveToNext();
				}
			}

			Spinner_BaseAdapterCategory selectedAdapterCategory = new Spinner_BaseAdapterCategory(context,lstevent);			
						selSubActivity.setAdapter(selectedAdapterCategory);
						
			  }

			if(!strSubActivity.contentEquals(""))
						{
			selSubActivity.setSelection(getItemIndexById(strSubActivity));
			}

			
			String [] subActivityList = {"Select","Met Region Manager"};
			ArrayAdapter<String> subActivityAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_list_item_1, android.R.id.text1,subActivityList);
			selSubActivity.setAdapter(subActivityAdapter);
			subActivityAdapter.notifyDataSetChanged();
		}
		//else if(strActivity.equals("On Leave"))
		else if(strActivity.equals("On Leave"))
		{
			 strId = db.getActivityId(strActivity);
			
			int count = db.GetSubActivityCount(strId);
			  if(count == 0)
			  {
				  taskSubActivity = new DownloadSubActivity();				  
				  startDownloadSubActivity(); 
			  }
			  else
			  {		
			
			Cursor c1 = db.getSubActivityName(strId);								
			
			lstevent.clear();

			if (c1.getCount() > 0) {
				c1.moveToFirst();
				for (int ii = 0; ii < c1.getCount(); ii++) {
					clsActivitySubCategory single = new clsActivitySubCategory("", "", "", "", "", "", "","");
					single.setName(c1.getString(c1.getColumnIndex("ActivitySubCategoryName")));					
					lstevent.add(single);
					c1.moveToNext();
				}
			}

			Spinner_BaseAdapterCategory selectedAdapterCategory = new Spinner_BaseAdapterCategory(context,lstevent);			
						selSubActivity.setAdapter(selectedAdapterCategory);
						
			  }

			if(!strSubActivity.contentEquals(""))
						{
			selSubActivity.setSelection(getItemIndexById(strSubActivity));
			}

			
			String [] subActivityList = {"Select","Met Region Manager"};
			ArrayAdapter<String> subActivityAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_list_item_1, android.R.id.text1,subActivityList);
			selSubActivity.setAdapter(subActivityAdapter);
			subActivityAdapter.notifyDataSetChanged();
		}*/

		
		effectiveDate.setClickable(true);
		timeFrom.setClickable(true);
		timeTo.setClickable(true);
		
		//Date listerner to set date in Textview
		/*final DatePickerDialog.OnDateSetListener datepickerListener = new DatePickerDialog.OnDateSetListener() {

			
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				effectiveDate.setText(new StringBuilder().append(dayOfMonth).append("-").append(monthOfYear+1).append("-").append(year).append(" "));
				//	    			System.out.println("Before valRider in dob");						
				//					System.out.println("After valRider in dob");
			}
		};*/

		//effective date
		effectiveDate.setOnClickListener(new OnClickListener()
		{

			
			public void onClick(View v) {
				// TODO Auto-generated method stub

				/*String dateArray[] = effectiveDate.getText().toString().split("-");				
				new DatePickerDialog(ViewRecordActivity.this, datepickerListener, Integer.parseInt(dateArray[2].trim()),Integer.parseInt(dateArray[1].trim())-1,Integer.parseInt(dateArray[0].trim())).show();*/
				
				showDialog(DATE_DIALOG_ID);

			}
		});
		
		/*final TimePickerDialog.OnTimeSetListener timePickerListenerFrom = new TimePickerDialog.OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int selectedHour,
					int selectedMinute) {
				hourFrom = selectedHour;
				minuteFrom = selectedMinute;

				if (selectedHour > 12) {

					hourFrom = selectedHour-12;
					strampm = "PM";
				}
				else if (selectedHour == 12) {
					strampm = "PM";
				}
				else if (selectedHour < 12) {
					strampm = "AM";
				}

				// set current time into textview
				timeFrom.setText(new StringBuilder().append(pad(hourFrom))
						.append(":").append(pad(minuteFrom)).append(" ")
						.append(strampm));

			}
		};
		
		
		
		final TimePickerDialog.OnTimeSetListener timePickerListenerTo = new TimePickerDialog.OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int selectedHour,
					int selectedMinute) {
				hourTo = selectedHour;
				minuteTo = selectedMinute;

				if (selectedHour > 12) {

					hourTo = selectedHour-12;
					strampm = "PM";
				}
				else if (selectedHour == 12) {
					strampm = "PM";
				}
				else if (selectedHour < 12) {
					strampm = "AM";
				}

				// set current time into textview
				timeTo.setText(new StringBuilder().append(pad(hourTo))
						.append(":").append(pad(minuteTo)).append(" ")
						.append(strampm));

			}
		};*/
		
		
		
		//time from
		timeFrom.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v) {
				
				//new TimePickerDialog(ViewRecordActivity.this, timePickerListenerFrom, hourFrom,minuteFrom,false).show();
				
				timecheck = 1;
				showDialog(TIME_DIALOG_ID);

			}
		});
		
		//time from
		timeTo.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v) {
				
				//new TimePickerDialog(ViewRecordActivity.this, timePickerListenerTo, hourTo,minuteTo,false).show();
				
				showDialog(TIME_DIALOG_ID);

			}
		});
		
		
		back = findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				setResult(RESULT_OK);
				finish();
			}
		});
		
		
		btnSubmit = findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				
				
				if(strSubCategoryName.trim().contentEquals("Select") || effectiveDate.getText().toString().equalsIgnoreCase("") ||
						edBranch.getText().toString().equalsIgnoreCase("") ||
						edActivity.getText().toString().equalsIgnoreCase("") ||
						strtime.contentEquals("Select Time Slot") ||						
						rdremark.getText().toString().equalsIgnoreCase(""))
				{
					validation();
				}
				else
				{
					
					//today date validation
					final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");								
				
					Calendar cala = Calendar.getInstance();
			        int mYearc= cala.get(Calendar.YEAR);
			        int mMonthc = cala.get(Calendar.MONTH);
			        int mDayc = cala.get(Calendar.DAY_OF_MONTH);
			        
			        
			       String ya = String.valueOf(mYearc);
			       String mo = String.valueOf(mMonthc + 1);
					String dat = String.valueOf(mDayc);
					if (mo.contentEquals("1")) {
						mo = "January";					
						
					} else if (mo.contentEquals("2")) {
						mo = "February";						
						
					} else if (mo.contentEquals("3")) {
						mo = "March";
						
					} else if (mo.contentEquals("4")) {
						mo = "April";
											
					} else if (mo.contentEquals("5")) {
						mo = "May";						
						
					} else if (mo.contentEquals("6")) {
						mo = "June";
												
					} else if (mo.contentEquals("7")) {
						mo = "July";					
						
					} else if (mo.contentEquals("8")) {
						mo = "August";
											
					} else if (mo.contentEquals("9")) {
						mo = "September";
										
					} else if (mo.contentEquals("10")) {
						mo = "October";
											
					} else if (mo.contentEquals("11")) {
						mo = "November";
											
					} else if (mo.contentEquals("12")) {
						mo = "December";					
					}


					String totaldate = dat + "-" + mo + "-" + ya;
					
					Date d1 = null;
					try {
						d1 = formatter.parse(effectiveDate.getText().toString());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Date d2 = null;
					try {
						d2 = formatter.parse(totaldate);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					//can not select past
					//if ((d1.after(d2))||(d1.equals(d2)))
					
					//can not select future
					if ((d2.after(d1))||(d2.equals(d1)))
					{												
					//end validation					
					
					if(edActivity.getText().toString().contentEquals("Business Connect") || edActivity.getText().toString().contentEquals("Promotional Activities"))
					{
						if(rdactlead.getText().toString().equalsIgnoreCase(""))
						{
							validation_lead();
						}
						else
						{
							Date dt12 = null;
							try {
								dt12 = dateFormat.parse(effectiveDate.getText().toString());
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							String effdate1 = df.format(dt12);
							
							int recordcount = db.RecordExist(edActivity.getText().toString(), strSubCategoryName, edBranch.getText().toString(), effdate1, selActivityTime_view.getSelectedItem().toString(), rdremark.getText().toString(), rdactlead.getText().toString());
							if(recordcount == 0)
							{													
							
							ArrayList<String> lstevent = new ArrayList<String>();				
							lstevent.clear();
							
							//Cursor c = db.GetAllActivity();
							Cursor c = db.UpdateSynsStatusAR(strRowId);
							if (c.getCount() > 0) {
								c.moveToFirst();
								for (int ii = 0; ii < c.getCount(); ii++) {
									lstevent.add(c.getString(c.getColumnIndex("DateNameAR")));
									lstevent.add(c.getString(c.getColumnIndex("EventNameAR")));
									lstevent.add(c.getString(c.getColumnIndex("MonthAR")));
									lstevent.add(c.getString(c.getColumnIndex("YearAR")));
									lstevent.add(c.getString(c.getColumnIndex("TimeAR")));
									lstevent.add(c.getString(c.getColumnIndex("RemarkAR")));
									lstevent.add(c.getString(c.getColumnIndex("ActivityAR")));
									lstevent.add(c.getString(c.getColumnIndex("SubActivityAR")));
									lstevent.add(c.getString(c.getColumnIndex("TimeToAR")));	
									lstevent.add(c.getString(c.getColumnIndex("UserIDAR")));	
									lstevent.add(c.getString(c.getColumnIndex("ActivityStatusAR")));
									lstevent.add(c.getString(c.getColumnIndex("ActivitySyncAR")));
									lstevent.add(c.getString(c.getColumnIndex("ActivityCreatedDateAR")));
									lstevent.add(c.getString(c.getColumnIndex("ActivityModifiedDateAR")));								
									lstevent.add(c.getString(c.getColumnIndex("ActivityLeadAR")));
									lstevent.add(c.getString(c.getColumnIndex("DateIDAR")));
									
									c.moveToNext();
								}
							}			        					
							
							clsCalendarActivityRecorder objcla = null;
							/*try {
								objcla = new clsCalendar(lstevent.get(0).toString(),
								lstevent.get(1).toString(),
								lstevent.get(2).toString(),
								lstevent.get(3).toString(),
								lstevent.get(4).toString(),
								lstevent.get(5).toString(),
								lstevent.get(6).toString(),
								strSubCategoryName,					
								timeTo.getText().toString());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}*/
							try {
								
								Date dt1 = null;
								try {
									dt1 = dateFormat.parse(effectiveDate.getText().toString());
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								String effdate = df.format(dt1);
								
								
								
								
								//objcla = new clsBDMTrackerCalendar(effectiveDate.getText().toString(),
								objcla = new clsCalendarActivityRecorder(effdate,
								edBranch.getText().toString(),
								m == null ? "" : m,
								y == null ? "" : y,
								selActivityTime_view.getSelectedItem().toString(),
								rdremark.getText().toString(),
								edActivity.getText().toString(),
								strSubCategoryName,					
								timeTo.getText().toString(),
                                        lstevent.get(9),
                                        lstevent.get(11),
                                        lstevent.get(12),
                                        lstevent.get(13),
                                        lstevent.get(10),
								/*lstevent.get(10).toString(),
								lstevent.get(11).toString(),
								lstevent.get(12).toString(),
								lstevent.get(13).toString(),*/
								rdactlead.getText().toString());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							//db.UpdateActivityRecord(objcla,lstevent.get(9).toString());
							db.UpdateActivityRecordAR(objcla,strRowId);
							
							updatealert();
							
							finish();
							
						}
						else
						{
							AlertRecordExist();
						}
						}
					}
					else
					{		
						
						Date dt12 = null;
						try {
							dt12 = dateFormat.parse(effectiveDate.getText().toString());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String effdate1 = df.format(dt12);
						
						int recordcount = db.RecordExist(edActivity.getText().toString(), strSubCategoryName, edBranch.getText().toString(), effdate1, selActivityTime_view.getSelectedItem().toString(), rdremark.getText().toString(), rdactlead.getText().toString());
						if(recordcount == 0)
						{													
				
				ArrayList<String> lstevent = new ArrayList<String>();				
				lstevent.clear();
				
				//Cursor c = db.GetAllActivity();
				Cursor c = db.UpdateSynsStatusAR(strRowId);
				if (c.getCount() > 0) {
					c.moveToFirst();
					for (int ii = 0; ii < c.getCount(); ii++) {
						lstevent.add(c.getString(c.getColumnIndex("DateNameAR")));
						lstevent.add(c.getString(c.getColumnIndex("EventNameAR")));
						lstevent.add(c.getString(c.getColumnIndex("MonthAR")));
						lstevent.add(c.getString(c.getColumnIndex("YearAR")));
						lstevent.add(c.getString(c.getColumnIndex("TimeAR")));
						lstevent.add(c.getString(c.getColumnIndex("RemarkAR")));
						lstevent.add(c.getString(c.getColumnIndex("ActivityAR")));
						lstevent.add(c.getString(c.getColumnIndex("SubActivityAR")));
						lstevent.add(c.getString(c.getColumnIndex("TimeToAR")));	
						lstevent.add(c.getString(c.getColumnIndex("UserIDAR")));	
						lstevent.add(c.getString(c.getColumnIndex("ActivityStatusAR")));
						lstevent.add(c.getString(c.getColumnIndex("ActivitySyncAR")));
						lstevent.add(c.getString(c.getColumnIndex("ActivityCreatedDateAR")));
						lstevent.add(c.getString(c.getColumnIndex("ActivityModifiedDateAR")));								
						lstevent.add(c.getString(c.getColumnIndex("ActivityLeadAR")));
						lstevent.add(c.getString(c.getColumnIndex("DateIDAR")));
						
						c.moveToNext();
					}
				}			        					
				
				clsCalendarActivityRecorder objcla = null;
				/*try {
					objcla = new clsCalendar(lstevent.get(0).toString(),
					lstevent.get(1).toString(),
					lstevent.get(2).toString(),
					lstevent.get(3).toString(),
					lstevent.get(4).toString(),
					lstevent.get(5).toString(),
					lstevent.get(6).toString(),
					strSubCategoryName,					
					timeTo.getText().toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
				Date dt1 = null;
				try {
					dt1 = dateFormat.parse(effectiveDate.getText().toString());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String effdate = df.format(dt1);
				
				
				
				try {
					//objcla = new clsBDMTrackerCalendar(effectiveDate.getText().toString(),
					objcla = new clsCalendarActivityRecorder(effdate,
					edBranch.getText().toString(),
					m == null ? "" : m,
					y == null ? "" : y,
					selActivityTime_view.getSelectedItem().toString(),
					rdremark.getText().toString(),
					edActivity.getText().toString(),
					strSubCategoryName,					
					timeTo.getText().toString(),
                            lstevent.get(9),
                            lstevent.get(11),
                            lstevent.get(12),
                            lstevent.get(13),
                            lstevent.get(10),
					/*lstevent.get(10).toString(),
					lstevent.get(11).toString(),
					lstevent.get(12).toString(),
					lstevent.get(13).toString(),*/
					rdactlead.getText().toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//db.UpdateActivityRecord(objcla,lstevent.get(9).toString());
				db.UpdateActivityRecordAR(objcla,strRowId);
				
				updatealert();
				
				finish();
				
					}
					else
					{
						AlertRecordExist();
					}
				
				}
					
				}
					else
					{
						dateselecterror();
					}
				
				}
				
				//setResult(RESULT_OK);
				//finish();
			}
		});
	}
	
	/*private int getIndex(Spinner spin,String str)
	{
		int index = 0;
		for(int i=0;i<spin.getCount();i++)
		{
			Object obj = spin.getItemAtPosition(i);
			String y = obj.toString();
			
			if(spin.getItemAtPosition(i).equals(obj))
			{
				index  =i;
			}
		}
		return index;
	}*/
	
	private int getItemIndexById(String str)
	{
		int index = 0;
		
		for(int i=0;i<lstevent.size();i++)
		{
			//String str1 = lstevent.get(i).getName().toString();
			
			if(lstevent.get(i).getName().contentEquals(str))
			{
				//return lstevent.indexOf(i);
				return index = i;
			}					
		}
		
		return index;
	}
	
	private void updateDisplay(int year, int month, int day) {
		y = String.valueOf(year);
		m = String.valueOf(month + 1);
		d = String.valueOf(day);
		String da = String.valueOf(day);
		if (m.contentEquals("1")) {
			m = "January";
		} else if (m.contentEquals("2")) {
			m = "February";
		} else if (m.contentEquals("3")) {
			m = "March";
		} else if (m.contentEquals("4")) {
			m = "April";
		} else if (m.contentEquals("5")) {
			m = "May";
		} else if (m.contentEquals("6")) {
			m = "June";
		} else if (m.contentEquals("7")) {
			m = "July";
		} else if (m.contentEquals("8")) {
			m = "August";
		} else if (m.contentEquals("9")) {
			m = "September";
		} else if (m.contentEquals("10")) {
			m = "October";
		} else if (m.contentEquals("11")) {
			m = "November";
		} else if (m.contentEquals("12")) {
			m = "December";
		}

		String totaldate = da + "-" + m + "-" + y;

		effectiveDate.setText(totaldate);			

	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay(mYear, mMonth, mDay);

		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:

			/*return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);*/
			return new DatePickerDialog(this, R.style.datepickerstyle,
					mDateSetListener, mYear, mMonth, mDay);

		case TIME_DIALOG_ID:
			// set time picker as current time
			return new TimePickerDialog(this, timePickerListener, hour, minute,
					false);
			
		case DIALOG_DOWNLOAD_PROGRESS:
			mProgressDialog = new ProgressDialog(this,ProgressDialog.THEME_HOLO_LIGHT);		
			String Message = "Loading. Please wait...";			
			mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(true);
			
			mProgressDialog.setButton("Cancel", new DialogInterface.OnClickListener() {				
				public void onClick(DialogInterface dialog, int which) {
										
					taskSubActivity.cancel(true);
					mProgressDialog.dismiss();
				}
			});
			
			mProgressDialog.setMax(100); 
			mProgressDialog.show();
			return mProgressDialog;

		}
		return null;
	}
	
	@Override
    protected void onPrepareDialog(int id,Dialog dialog) {
		switch(id){
		case DATE_DIALOG_ID:
			((DatePickerDialog)dialog).updateDate(mYear, mMonth, mDay);
			break;
		}
	}

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour = selectedHour;
			minute = selectedMinute;

			if (selectedHour > 12) {

				strampm = "PM";
			}
			if (selectedHour == 12) {
				strampm = "PM";
			}
			if (selectedHour < 12) {
				strampm = "AM";
			}

			// set current time into textview
			
			if(timecheck == 1)
			{
				timecheck = 0;
				
				timeFrom.setText(new StringBuilder().append(pad(hour))
					.append(":").append(pad(minute)).append(" ")
					.append(strampm));
			}
			else
			{						
				timeTo.setText(new StringBuilder().append(pad(hour))
					.append(":").append(pad(minute)).append(" ")
					.append(strampm));
			}

		}
	};

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + c;
	}
	
	/*private class ItemsAdapterSubActivity extends ArrayAdapter {
		private int selectedPos = -1;

		public ItemsAdapterSubActivity(Context context,int textViewResourceId, List<clsActivityCategory> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}
		
		public void setSelectedPosition(int pos){
	        selectedPos = pos;
	             // inform the view of this change
	             notifyDataSetChanged();
	        }
	        public int getSelectedPosition(){
	             return selectedPos;
	        }

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {						
			
			View view = convertView;
			if (view == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);				
				view = vi.inflate(R.layout.list_subactivity, null);
			}
			
			TextView datename = (TextView) view.findViewById(R.id.txtsubactivityspin);						
			

			datename.setText(items.get(position).getName());			
			
			return view;
		}

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

	}*/
	
	private void startDownloadSubActivity() {
	       
		taskSubActivity.execute("SubActivity");
    }
	
	class DownloadSubActivity extends AsyncTask<String, String, String> {
	  	   
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
    		
    		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SUBCATEGORY);		
    		
    		request.addProperty("strBDMID",strCIFBDMUserId);	
    		request.addProperty("subCatId",strId);
    		request.addProperty("strEmailId",strCIFBDMEmailId);
			request.addProperty("strMobileNo",strCIFBDMMObileNo);
			request.addProperty("strAuthKey",strCIFBDMPassword.trim());
    		
    		SoapSerializationEnvelope envelope = 
    				new SoapSerializationEnvelope(SoapEnvelope.VER11);
    		envelope.dotNet = true;    		    	
    		
    		envelope.setOutputSoapObject(request);	
    		
    	// 	allowAllSSL();
    		mCommonMethods.TLSv12Enable();
    		
    		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    		StrictMode.setThreadPolicy(policy);
    		
    		HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
    		try {
    			androidHttpTranport.call(SOAP_ACTION_SUBCATEGORY, envelope);
    			
    			SoapPrimitive sa = (SoapPrimitive)envelope.getResponse();
    			  
    			ParseXML prsObj = new ParseXML();
    			
    		String inputpolicylist = sa.toString();	
    		inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "BDMTracker");
            
            List<String> Node = prsObj.parseParentNode(inputpolicylist, "Table");
            List<XMLHolderSubCategory> nodeData = prsObj.parseNodeElementSubCategory(Node);
            
            final List<XMLHolderSubCategory> lst;
    		lst = new ArrayList<XMLHolderSubCategory>();
    		lst.clear();
    		
    		for (XMLHolderSubCategory node : nodeData) {
    			
    			lst.add(node);
    		}    
    		
    		
    		for(int i=0;i<lst.size();i++)
			{
				String strMasterId = lst.get(i).getId();
				String strName = lst.get(i).getName();
				
				clsActivitySubCategory obj = new clsActivitySubCategory(strName, strName, strId, "0", "2013-05-22", "0", "0",strMasterId);
				db.AddActivitySubCategory(obj);
			}
    		
    		}
    		 catch (XmlPullParserException e) {
				try {
 					throw (e);
 				} catch (Exception e1) {		 					
 					e1.printStackTrace();
 				} 		 	    		
 	    		mProgressDialog.dismiss();		 	    		
 	    		running = false;
			}   	
 				}
 				catch (Exception e) {
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
    	@Override
		protected void onProgressUpdate(String... progress) {
    		 Log.d("ANDRO_ASYNC",progress[0]);
    		 mProgressDialog.setProgress(Integer.parseInt(progress[0]));
    	}

    	@Override
    	protected void onPostExecute(String unused) {
    		dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
    		if(running)
    		{
    			Cursor c1 = db.getSubActivityName(strId);								
    			
    			lstevent.clear();

    			if (c1.getCount() > 0) {
    				c1.moveToFirst();
    				for (int ii = 0; ii < c1.getCount(); ii++) {
    					clsActivitySubCategory single = new clsActivitySubCategory("", "", "", "", "", "", "","");
    					single.setName(c1.getString(c1.getColumnIndex("ActivitySubCategoryName")));					
    					lstevent.add(single);
    					c1.moveToNext();
    				}
    			}    			    		
    			
    			Spinner_BaseAdapterCategory selectedAdapterCategory = new Spinner_BaseAdapterCategory(context,lstevent);			
    			selSubActivity.setAdapter(selectedAdapterCategory);	   			  			    		
    		}
    		else
    		{
    			syncerror();
    		}
    	}
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
	
	private void updatealert()
    {	    		    
    	final Dialog dialog = new Dialog(this);
    	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
    	dialog.setContentView(R.layout.loading_window);		
    	TextView text = dialog.findViewById(R.id.txtalertheader);
    	text.setText("Submit Successfully...");		
    	Button dialogButton = dialog.findViewById(R.id.btnalert);
    	dialogButton.setOnClickListener(new OnClickListener() {			
    		public void onClick(View v) {
    			dialog.dismiss();	    			
    		}
    	});
    	dialog.show();	    		    	
    }
	
	private void validation()
    {	    		    
    	final Dialog dialog = new Dialog(this);
    	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
    	dialog.setContentView(R.layout.loading_window);		
    	TextView text = dialog.findViewById(R.id.txtalertheader);
    	text.setText("Please Select Sub Activity...");		
    	Button dialogButton = dialog.findViewById(R.id.btnalert);
    	dialogButton.setOnClickListener(new OnClickListener() {			
    		public void onClick(View v) {
    			dialog.dismiss();	    			
    		}
    	});
    	dialog.show();	    		    	
    }
	
	private void validation_lead() {

		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.loading_window);
		TextView text = dialog.findViewById(R.id.txtalertheader);
		text.setText("Enter your Lead..");
		Button dialogButton = dialog.findViewById(R.id.btnalert);
		dialogButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();

	}
	
	private void dateselecterror()
	{	
		
		final Dialog dialog = new Dialog(this);		
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 	
		dialog.setContentView(R.layout.loading_window);		
		TextView text = dialog.findViewById(R.id.txtalertheader);
		text.setText("Date should be greater or equal than Today's date");		
		Button dialogButton = dialog.findViewById(R.id.btnalert);
		dialogButton.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	
	private void AlertRecordExist()
	{
		final Dialog dialog = new Dialog(this);		
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 	
		dialog.setContentView(R.layout.loading_window);		
		TextView text = dialog.findViewById(R.id.txtalertheader);
		text.setText("Record Already Exist..");		
		Button dialogButton = dialog.findViewById(R.id.btnalert);
		dialogButton.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	
	
	
	
	@Override
    public boolean dispatchTouchEvent(final MotionEvent ev) {
        // all touch events close the keyboard before they are processed except EditText instances.
        // if focus is an EditText we need to check, if the touchevent was inside the focus editTexts
        final View currentFocus = getCurrentFocus();
        if (!(currentFocus instanceof EditText) || !isTouchInsideView(ev, currentFocus)) {
            ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);                       

        }
        return super.dispatchTouchEvent(ev);
    }
    
    private boolean isTouchInsideView(final MotionEvent ev, final View currentFocus) {
        final int[] loc = new int[2];
        currentFocus.getLocationOnScreen(loc);
        return ev.getRawX() > loc[0] && ev.getRawY() > loc[1] && ev.getRawX() < (loc[0] + currentFocus.getWidth())
            && ev.getRawY() < (loc[1] + currentFocus.getHeight());
    }
    
    public String GetUserType()
	{
		String  strUserType = "";
			try {
				strUserType = SimpleCrypto.decrypt("SBIL", db.GetUserType());
			} catch (Exception e) {
				strUserType = "";
				e.printStackTrace();
			}			        	

		return strUserType;
	}
    

}
