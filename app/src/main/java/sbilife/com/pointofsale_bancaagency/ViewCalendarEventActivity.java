package sbilife.com.pointofsale_bancaagency;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
@SuppressWarnings("deprecation")
public class ViewCalendarEventActivity extends AppCompatActivity
{
	private EditText vednotes;
	
	String strId = "";
			
	private DatabaseHelper db;
	
	private int hour;
	private int minute;

     private final int TIME_DIALOG_ID = 999;
	private int mYear;
	private int mMonth;
	private int mDay;
	 private final int DATE_DIALOG_ID = 0;
	int strmonth;
	int stryear;
	private String y;
	private String m;
	private String d;
	
	private int timecheck = 0;
	
	private TextView effectiveDate;
    private TextView timeFrom;
    TextView timeTo;
	EditText edBranch;
    private EditText edActivity;
    private EditText rdremark;
	Spinner selSubActivity;
	int hourFrom,minuteFrom,hourTo,minuteTo;
	private String strampm;
	private Button btnSubmit;
    private Button back;
    private Button btnDelete;
	
    final Context context = this;
	
	protected	ListView lv;	
	
	private String strDate;
    String strBranch;
    private String strActivity;
    String strremark;
    private String strtime;
    String strSubActivity;
    String strTimeTo;
    private String strRowId;
    private String strNotes;
	
	//for store userid in bdm tracker table
    private String strCIFBDMUserId;
			private String strCIFBDMEmailId = "";
			private String strCIFBDMPassword = "";
			private String strCIFBDMMObileNo = "";
			
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.viewcalendarevent);
		 new CommonMethods().setActionbarLayout(this);
		
		db = new DatabaseHelper(this);			
		
		effectiveDate = findViewById(R.id.effectiveDate);
		edActivity = findViewById(R.id.Activity);
		timeFrom = findViewById(R.id.timeFrom);
		rdremark = findViewById(R.id.rdremark);
		vednotes = findViewById(R.id.vednotes);
		
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
		strActivity = i.getStringExtra("Event");	
		strNotes = i.getStringExtra("Notes");
		strtime = i.getStringExtra("Time");				
		strRowId = i.getStringExtra("RowId");	
		
		effectiveDate.setText(strDate);				
		edActivity.setText(strActivity);		
		timeFrom.setText(strtime);
		vednotes.setText(strNotes);
				
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
		

		
		effectiveDate.setClickable(true);
		timeFrom.setClickable(true);		
		
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
		
		/*//time from
		timeTo.setOnClickListener(new View.OnClickListener() 
		{

			public void onClick(View v) {
				
				//new TimePickerDialog(ViewRecordActivity.this, timePickerListenerTo, hourTo,minuteTo,false).show();
				
				showDialog(TIME_DIALOG_ID);

			}
		});*/
		
		
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
				
				
				if(effectiveDate.getText().toString().equalsIgnoreCase("") ||						
						edActivity.getText().toString().equalsIgnoreCase("") ||
						timeFrom.getText().toString().equalsIgnoreCase("")||
						vednotes.getText().toString().equalsIgnoreCase(""))						
				{
					validation();
				}
				else
				{
				
				ArrayList<String> lstevent = new ArrayList<String>();				
				lstevent.clear();
				
				Cursor c = db.UpdateCalendarEventStatus(strRowId,strCIFBDMUserId);
				if (c.getCount() > 0) {
					c.moveToFirst();
					for (int ii = 0; ii < c.getCount(); ii++) {
						lstevent.add(c.getString(c.getColumnIndex("DateName")));
						lstevent.add(c.getString(c.getColumnIndex("EventName")));
						lstevent.add(c.getString(c.getColumnIndex("Month")));
						lstevent.add(c.getString(c.getColumnIndex("Year")));
						lstevent.add(c.getString(c.getColumnIndex("Time")));						
						lstevent.add(c.getString(c.getColumnIndex("CalendarUserID")));							
						lstevent.add(c.getString(c.getColumnIndex("DateID")));
						lstevent.add(c.getString(c.getColumnIndex("Notes")));
						
						c.moveToNext();
					}
				}			        					
				
				clsCalendar objcla = null;				
				try {
					objcla = new clsCalendar(effectiveDate.getText().toString(),
							edActivity.getText().toString(),
					m == null ? "" : m,
					y == null ? "" : y,
					timeFrom.getText().toString(),
					strCIFBDMUserId,vednotes.getText().toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
								
				db.UpdateEvent(objcla,strRowId);
				
				updatealert();
				
				finish();
				
				}
							
			}
		});
		
		btnDelete = findViewById(R.id.btnDelete);
		btnDelete.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				
				
				if(effectiveDate.getText().toString().equalsIgnoreCase("") ||						
						edActivity.getText().toString().equalsIgnoreCase("") ||
						timeFrom.getText().toString().equalsIgnoreCase(""))						
				{
					validation();
				}
				else
				{								
					db.DeleteEvent(strRowId);
				
					deletealert();
				
					finish();
				
				}
							
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
	
	/*public int getItemIndexById(String str)
	{
		int index = 0;
		
		for(int i=0;i<lstevent.size();i++)
		{
			String str1 = lstevent.get(i).getName().toString();
			
			if(lstevent.get(i).getName().contentEquals(str))
			{
				//return lstevent.indexOf(i);
				return index = i;
			}					
		}
		
		return index;
	}*/
	
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
			/*return new TimePickerDialog(this, timePickerListener, hour, minute,
					false);	*/	
			
			TimePickerDialog Tdialog = new TimePickerDialog(this, timePickerListener, hour, minute,
					false);
			
			String str = timeFrom.getText().toString();
			
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
	        Date date = null;
	        try {
	            date = sdf.parse(str);
	        } catch (ParseException e) {
	        }
	        Calendar c = Calendar.getInstance();
	        c.setTime(date);

	        TimePicker picker = new TimePicker(getApplicationContext());
	        picker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY ));
	        picker.setCurrentMinute(c.get(Calendar.MINUTE));
	        //String a = (c.get(Calendar.AM_PM)==(Calendar.AM))? "AM":"PM";
				
	        int hr=picker.getCurrentHour();
	        int mn = picker.getCurrentMinute();	       
	       
	        ((Tdialog)).updateTime(hr, mn);
	        return Tdialog;

		}
		return null;
	}
	
	@Override
    protected void onPrepareDialog(int id,Dialog dialog) {
		switch(id){
		case DATE_DIALOG_ID:
			
			String str = effectiveDate.getText().toString();
			
			final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");
			final Calendar cal = Calendar.getInstance();
			Date d1 = null;
			try {
				d1 = formatter.parse(str);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			

			cal.setTime(d1);
			
			 mYear= cal.get(Calendar.YEAR);
		        mMonth = cal.get(Calendar.MONTH);
		        mDay = cal.get(Calendar.DAY_OF_MONTH);
		        
			((DatePickerDialog)dialog).updateDate(mYear, mMonth, mDay);
			break;
		}
	}

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			
			if(selectedHour>12)
		    {
				timeFrom.setText((selectedHour - 12) + ":"+(selectedMinute +" pm"));
		    }
		    if(selectedHour==12)
		    {
		    	timeFrom.setText("12"+ ":"+(selectedMinute +" pm"));
		    }
		    if(selectedHour<12)
		    {
		    	timeFrom.setText(selectedHour + ":"+(selectedMinute +" am"));
		    }
		    
			/*hour = selectedHour;
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
			}*/

		}
	};

	/*private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}*/
	
	/*private class ItemsAdapterSubActivity extends ArrayAdapter<clsActivityCategory> {
		private int selectedPos = -1;
		List<clsActivityCategory> items;

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
		
	

	public void syncerror()
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
    	text.setText("Update Successfully...");		
    	Button dialogButton = dialog.findViewById(R.id.btnalert);
    	dialogButton.setOnClickListener(new OnClickListener() {			
    		public void onClick(View v) {
    			dialog.dismiss();	    			
    		}
    	});
    	dialog.show();	    		    	
    }
	
	private void deletealert()
    {	    		    
    	final Dialog dialog = new Dialog(this);
    	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
    	dialog.setContentView(R.layout.loading_window);		
    	TextView text = dialog.findViewById(R.id.txtalertheader);
    	text.setText("Delete Successfully...");		
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			        	

		return strUserType;
	}
    
  //after 5 minute it will get log out
  	/*private CountDownTimer mCountDown = new CountDownTimer(300000, 300000)
  		{

  		    @Override
  		    public void onTick(long millisUntilFinished)
  		    {

  		    }


  		    @Override
  		    public void onFinish()
  		    {
  		        //show your dialog here
  		    	new CommonMethods().logoutToLoginActivity(context);
  		    }
  		};  


  		@Override
  		public void onResume()
  		{
  		    super.onResume();

  		    mCountDown.start();
  		}  
  		@Override
  		public void onPause()
  		{
  		    super.onPause();

  		    mCountDown.cancel();
  		}  
  		@Override
  		public void onUserInteraction()
  		{
  		    super.onUserInteraction();

  		    // user interact cancel the timer and restart to countdown to next interaction
  		    mCountDown.cancel();
  		    mCountDown.start();
  		}*/
	
}
