package sbilife.com.pointofsale_bancaagency;

/*
 * it is same as calendar view but it will show in independent activity, called when carousel view is there.
 * 
 */

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.home.CarouselHomeActivity;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.service.ScheduleClient;
@SuppressWarnings("deprecation")
public class CarouselCalendarView extends AppCompatActivity {
	
	private Button btnshowcust_dob;
	private EditText ednotes;
	
	protected	ListView lvSetting;	
	
	private Button btnaddevent;
    private Button btnshowallevents;
    private Button btnadd;
    private Button btncancel;
	private ImageButton btndate;
	
	//ImageButton imagtbt_option ; 
	//String title = "" ;
	//String username ="" ;
	//TextView tv_title_cif_common ;
    private TableLayout tbl;
	
	//set alrm
	
	private int intday;
	private int intmonth;
	private int intyear;
	
	private CheckBox chk;
	private ScheduleClient scheduleClient;

	private ArrayList<clsCalendar> lstmain = new ArrayList<clsCalendar>();
	private ListView lv;
	private final Context context = this;

	// for time picker

	private EditText tvDisplayTime;

    private int hour;
	private int minute;
	private int ampm;

	private String strampm;

	 private final int TIME_DIALOG_ID = 999;

    private int mYear;
	private int mMonth;
	private int mDay;
	 private final int DATE_DIALOG_ID = 0;

	private int strmonth;
	private int stryear;

	private String y;
	private String m;
	
	private String d;
	
	private DatabaseHelper db;
	private EditText etdate;
	private EditText edeventname;

	private GregorianCalendar month;
    private GregorianCalendar itemmonth;// calendar instances.

	private CalendarAdapter adapter;// adapter instance
	private Handler handler;// for grabbing some event values for showing the dot
							// marker.
                            private ArrayList<String> items; // container to store calendar items which
									// needs showing the event marker
	
	//for store userid in bdm tracker table
    private String strCIFBDMUserId;
		private String strCIFBDMEmailId = "";
		private String strCIFBDMPassword = "";
		private String strCIFBDMMObileNo = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.calendar);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		
		db = new DatabaseHelper(this);
		 new CommonMethods().setApplicationToolbarMenu(this,"Calender");
		//imagtbt_option=(ImageButton)findViewById(R.id.imagtbt_option);
		
		
		Locale.setDefault(Locale.US);
		month = (GregorianCalendar) Calendar.getInstance();
		itemmonth = (GregorianCalendar) month.clone();

		items = new ArrayList<String>();
		adapter = new CalendarAdapter(this, month);

		strmonth = month.get(Calendar.MONTH) + 1;
		stryear = month.get(Calendar.YEAR);

		db = new DatabaseHelper(this);		
		etdate = findViewById(R.id.editTextdt);
		edeventname = findViewById(R.id.edeventname);

        Button selectedDayMonthYearButton = this
                .findViewById(R.id.selectedDayMonthYear);
		selectedDayMonthYearButton.setText("Selected: ");		
		
		tbl = findViewById(R.id.tbl);
		
		btnaddevent = findViewById(R.id.btnaddevent);
		btnshowallevents = findViewById(R.id.btnshowallevents);
		btnadd = findViewById(R.id.btnadd);
		btncancel = findViewById(R.id.btncancel);
		btndate = findViewById(R.id.btndate);
		
		ednotes = findViewById(R.id.ednotes);
		
		btnshowcust_dob = findViewById(R.id.btnshowcust_dob);
		if(!GetUserType().contentEquals("AGENT")){
			btnshowcust_dob.setVisibility(View.GONE);
		}else{
			btnshowcust_dob.setVisibility(View.VISIBLE);
		}
		
		
/*imagtbt_option.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onApplicationMenu(imagtbt_option);
				
			}
		});*/
		
btnaddevent.setOnClickListener(new OnClickListener() {
			
			//@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				tbl.getLayoutParams().height = LayoutParams.WRAP_CONTENT; 
				tbl.requestLayout();
			}
		});
		
		btnshowallevents.setOnClickListener(new OnClickListener() {
			
			//@Override
			public void onClick(View arg0) {
				Intent inte = new Intent(context, ShowAllEventActivity.class);
				startActivity(inte);
			}
		});

		btnadd.setOnClickListener(new OnClickListener() {
	
	//@Override
	public void onClick(View arg0) {
		
		String date = etdate.getText().toString();
		String eventname = edeventname.getText().toString();
		String month = m;
		String year = y;
		String time = tvDisplayTime.getText().toString();
		String notes = ednotes.getText().toString();

		if (date.equalsIgnoreCase("") || eventname.equalsIgnoreCase("")
				|| time.equalsIgnoreCase("")|| notes.equalsIgnoreCase("")) {

			validation();
			
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy");
			Date strDate = null;
			try {
				strDate = sdf.parse(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Calendar cal = Calendar.getInstance();
			String formattedDate = sdf.format(cal.getTime());
			Date strcdate = null;
			try {
				strcdate = sdf.parse(formattedDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (strcdate.after(strDate))
			{
				datevalidation();
			}
			else
			{
			clsCalendar cls = new clsCalendar(date, eventname, month, year, time,strCIFBDMUserId,notes);
			db.AddEvent(cls);
		
			ok();
			
			getallevents();	
								
			etdate.setText("");
			edeventname.setText("");
			ednotes.setText("");
			
			final Calendar c = Calendar.getInstance();
			hour = c.get(Calendar.HOUR_OF_DAY);
			minute = c.get(Calendar.MINUTE);

			ampm = c.get(Calendar.AM_PM);
			if (ampm == 0) {
				strampm = "AM";
			} else {
				strampm = "PM";
			}			
			tvDisplayTime.setText(new StringBuilder().append(pad(hour)).append(":")
					.append(pad(minute)).append(" ").append(strampm));							
			
			// refresh grid
			refreshCalendar();
			
			//set alrm
			
			if(chk.isChecked())
			{
				
			
			// Create a new calendar set to the date chosen
	    	// we set the time to midnight (i.e. the first minute of that day)
	    	Calendar ca = Calendar.getInstance();
	    	ca.set(intyear, intmonth, intday);
	    	ca.set(Calendar.HOUR_OF_DAY, 0);
	    	ca.set(Calendar.MINUTE, 0);
	    	ca.set(Calendar.SECOND, 0);
	    	// Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
	    	scheduleClient.setAlarmForNotification(ca);
	    	// Notify the user what they just did
	    	//Toast.makeText(this, "Notification set for: "+ day +"/"+ (month+1) +"/"+ year, Toast.LENGTH_SHORT).show();
	    	
			}
			
			chk.setChecked(false);

		}
		}
	}
});

		btncancel.setOnClickListener(new OnClickListener() {
	
	//@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		tbl.getLayoutParams().height = 0; 
		tbl.requestLayout();
	}
});
		
		btndate.setOnTouchListener(new View.OnTouchListener() {
			
			//@Override
			
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				
				showDialog(DATE_DIALOG_ID);
				
				return false;
			}
		});
		
		btnshowcust_dob.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Intent inte = new Intent(context, CustomerDOBActivity.class);
				startActivity(inte);
			}
		});
		

		GridView gridview = findViewById(R.id.gridview);
		gridview.setAdapter(adapter);

		handler = new Handler();
		handler.post(calendarUpdater);

		TextView title = findViewById(R.id.title);
		title.setText(DateFormat.format("MMMM yyyy", month));

		RelativeLayout previous = findViewById(R.id.previous);
		
			try {
				strCIFBDMUserId = SimpleCrypto.decrypt("SBIL",db.GetCIFNo());
				strCIFBDMEmailId = SimpleCrypto.decrypt("SBIL",db.GetEmailId());
				strCIFBDMPassword = db.GetPassword();
				strCIFBDMMObileNo = SimpleCrypto.decrypt("SBIL",db.GetMobileNo());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		previous.setOnClickListener(new OnClickListener() {

			
			public void onClick(View v) {
				setPreviousMonth();
				refreshCalendar();
			}
		});

		RelativeLayout next = findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {

			
			public void onClick(View v) {
				setNextMonth();
				refreshCalendar();

			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				((CalendarAdapter) parent.getAdapter()).setSelected(v);
				String selectedGridDate = CalendarAdapter.dayString
						.get(position);
				String[] separatedTime = selectedGridDate.split("-");
				String gridvalueString = separatedTime[2].replaceFirst("^0*",
						"");// taking last part of date. ie; 2 from 2012-12-02.
				int gridvalue = Integer.parseInt(gridvalueString);
				// navigate to next or previous month on clicking offdays.
				if ((gridvalue > 10) && (position < 8)) {
					setPreviousMonth();
					refreshCalendar();
				} else if ((gridvalue < 7) && (position > 28)) {
					setNextMonth();
					refreshCalendar();
				}
				((CalendarAdapter) parent.getAdapter()).setSelected(v);

				// showToast(selectedGridDate);

				Date dt1 = null;
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				try {
					dt1 = df.parse(selectedGridDate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"dd-MMMM-yyyy");
				selectedGridDate = dateFormat.format(dt1);

				String[] spl = selectedGridDate.split("-");

				String a = spl[0];
				String b = spl[1];
				String c = spl[2];

				if (a.contentEquals("01")) {
					a = "1";
					selectedGridDate = a + "-" + b + "-" + c;
				} else if (a.contentEquals("02")) {
					a = "2";
					selectedGridDate = a + "-" + b + "-" + c;
				} else if (a.contentEquals("03")) {
					a = "3";
					selectedGridDate = a + "-" + b + "-" + c;
				} else if (a.contentEquals("04")) {
					a = "4";
					selectedGridDate = a + "-" + b + "-" + c;
				} else if (a.contentEquals("05")) {
					a = "5";
					selectedGridDate = a + "-" + b + "-" + c;
				} else if (a.contentEquals("06")) {
					a = "6";
					selectedGridDate = a + "-" + b + "-" + c;
				} else if (a.contentEquals("07")) {
					a = "7";
					selectedGridDate = a + "-" + b + "-" + c;
				} else if (a.contentEquals("08")) {
					a = "8";
					selectedGridDate = a + "-" + b + "-" + c;
				} else if (a.contentEquals("09")) {
					a = "9";
					selectedGridDate = a + "-" + b + "-" + c;
				}

				/*ArrayList<String> lstevent = new ArrayList<String>();
				ArrayList<String> lsttime = new ArrayList<String>();*/

				Cursor c1 = db.geteventname(selectedGridDate,strCIFBDMUserId);				

				/*if (c1.getCount() > 0) {
					c1.moveToFirst();
					for (int ii = 0; ii < c1.getCount(); ii++) {
						lstevent.add(c1.getString(c1
								.getColumnIndex("EventName")));
						lsttime.add(c1.getString(c1.getColumnIndex("Time")));
						c1.moveToNext();
					}
				}*/

				String strdate = "";
				String strevent = "";
				String strtmonth = "";
				String strtyear = "";
				String strtime = "";
				String struserid = "";
				String strnotes = "";

				lstmain.clear();

				if (c1.getCount() > 0) {
					c1.moveToFirst();
					for (int ii = 0; ii < c1.getCount(); ii++) {
						clsCalendar single = new clsCalendar(strdate, strevent,
								strtmonth, strtyear, strtime,struserid,strnotes);
						single.setDateName(c1.getString(c1
								.getColumnIndex("DateName")));
						single.setEventName(c1.getString(c1
								.getColumnIndex("EventName")));
						single.setNotes(c1.getString(c1
								.getColumnIndex("Notes")));
						single.setTime(c1.getString(c1.getColumnIndex("Time")));
						lstmain.add(single);
						c1.moveToNext();
					}
				}

				if (lstmain.size() > 0) {
					ItemsAdapter adapter = new ItemsAdapter(context, lstmain);
					lv.setAdapter(adapter);
					Utility.setListViewHeightBasedOnChildren(lv);
					
					lv.setOnItemClickListener(new OnItemClickListener() {

						public void onItemClick(AdapterView<?> arg0, View arg1,
								int position, long arg3) {
							
							String date = ((TextView) arg1.findViewById(R.id.img)).getText().toString();
							String event = ((TextView) arg1.findViewById(R.id.title)).getText().toString();
							String time = ((TextView) arg1.findViewById(R.id.detail)).getText().toString();
							String notes = ((TextView) arg1.findViewById(R.id.nots)).getText().toString();
												
							String rowid = db.GetRowID(date, event, time, strCIFBDMUserId,notes);
							
							if(!rowid.contentEquals(""))
							{

								
								Cursor c1 = db.getSelectedRowEvent(Integer.parseInt(rowid),strCIFBDMUserId);

								ArrayList<String> lstevent = new ArrayList<String>();

								lstevent.clear();

								if (c1.getCount() > 0) {
									c1.moveToFirst();
									for (int ri = 0; ri < c1.getCount(); ri++) {
										lstevent.add(c1.getString(c1
												.getColumnIndex("DateName")));
										lstevent.add(c1.getString(c1
												.getColumnIndex("EventName")));
										lstevent.add(c1.getString(c1
												.getColumnIndex("Time")));	
										lstevent.add(c1.getString(c1
												.getColumnIndex("Notes")));	
										c1.moveToNext();
									}
									
									Intent i = new Intent(context, ViewCalendarEventActivity.class);
									
									i.putExtra("Date", lstevent.get(0));
									i.putExtra("Event", lstevent.get(1));
									i.putExtra("Time", lstevent.get(2));
									i.putExtra("Notes", lstevent.get(3));
									i.putExtra("RowId", rowid);
									
									startActivity(i);
									
								}											
						}
						}

					});
					
				} else {
					ArrayList<clsCalendar> lstmain = new ArrayList<clsCalendar>();

					ItemsAdapter adapter = new ItemsAdapter(context, lstmain);
					lv.setAdapter(adapter);
					Utility.setListViewHeightBasedOnChildren(lv);
				}


			}
		});

		tvDisplayTime = findViewById(R.id.tvTime);
        ImageButton btnChangeTime = findViewById(R.id.btnChangeTime);

		final Calendar c = Calendar.getInstance();
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);

		ampm = c.get(Calendar.AM_PM);
		if (ampm == 0) {
			strampm = "AM";
		} else {
			strampm = "PM";
		}

		// set current time into textview
		/*tvDisplayTime.setText(new StringBuilder().append(pad(hour)).append(":")
				.append(pad(minute)).append(" ").append(strampm));*/
		
		String delegate = "hh:mm aaa"; 
        String str = (String)DateFormat.format(delegate,Calendar.getInstance().getTime()); 
        tvDisplayTime.setText(str);

		btnChangeTime.setOnClickListener(new OnClickListener() {

			
			public void onClick(View arg0) {

				showDialog(TIME_DIALOG_ID);
			}
		});

		this.lv = this.findViewById(R.id.list);
		
		 // Create a new service client and bind our activity to this service
        scheduleClient = new ScheduleClient(this);
        scheduleClient.doBindService();
		
		chk = findViewById(R.id.chk);
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				
				String fulldate = etdate.getText().toString();
				
				if(fulldate.equalsIgnoreCase(""))
				{
					if(arg1)
					{
						validatedate();	
						chk.setChecked(false);
					}
				}
				else
				{
					String[] spl = fulldate.split("-");

					String a = spl[0];
					String b = spl[1];
					String c = spl[2];
					
					if (b.contentEquals("January")) {
						b = "0";
					} else if (b.contentEquals("February")) {
						b = "1";
					} else if (b.contentEquals("March")) {
						b = "2";
					} else if (b.contentEquals("April")) {
						b = "3";
					} else if (b.contentEquals("May")) {
						b = "4";
					} else if (b.contentEquals("June")) {
						b = "5";
					} else if (b.contentEquals("July")) {
						b = "6";
					} else if (b.contentEquals("August")) {
						b = "7";
					} else if (b.contentEquals("September")) {
						b = "8";
					} else if (b.contentEquals("October")) {
						b = "9";
					} else if (b.contentEquals("November")) {
						b = "10";
					} else if (b.contentEquals("December")) {
						b = "11";
					}
					
					// Get the date from our datepicker
			    	 intday = Integer.parseInt(a);
			    	 intmonth = Integer.parseInt(b);	
			    	 intyear = Integer.parseInt(c);
			    	
			    	/*// Create a new calendar set to the date chosen
			    	// we set the time to midnight (i.e. the first minute of that day)
			    	Calendar ca = Calendar.getInstance();
			    	ca.set(intyear, intmonth, intday);
			    	ca.set(Calendar.HOUR_OF_DAY, 0);
			    	ca.set(Calendar.MINUTE, 0);
			    	ca.set(Calendar.SECOND, 0);
			    	// Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
			    	scheduleClient.setAlarmForNotification(ca);
			    	// Notify the user what they just did
			    	//Toast.makeText(this, "Notification set for: "+ day +"/"+ (month+1) +"/"+ year, Toast.LENGTH_SHORT).show();			    	
*/				}
			}
		});
		
		 	Calendar cal = Calendar.getInstance();
	        mYear= cal.get(Calendar.YEAR);
	        mMonth = cal.get(Calendar.MONTH);
	        mDay = cal.get(Calendar.DAY_OF_MONTH);
	        
	        y = String.valueOf(mYear);
			m = String.valueOf(mMonth + 1);
			d = String.valueOf(mDay);
			String da = String.valueOf(mDay);
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
			etdate.setText(totaldate);

	}
	
	/*public void onApplicationMenu(View v) {
		CreateMenuDialog objMenu=new CreateMenuDialog(username,CarouselCalendarView.this);
		objMenu.createMenu(v);

	}*/

	private void setNextMonth() {
		if (month.get(Calendar.MONTH) == month.getActualMaximum(Calendar.MONTH)) {
			month.set((month.get(Calendar.YEAR) + 1),
					month.getActualMinimum(Calendar.MONTH), 1);

			strmonth = month.get(Calendar.MONTH) + 1;
			stryear = month.get(Calendar.YEAR) + 1;

		} else {
			month.set(Calendar.MONTH,
					month.get(Calendar.MONTH) + 1);

			strmonth = month.get(Calendar.MONTH) + 1;
			stryear = month.get(Calendar.YEAR);
		}

	}

	private void setPreviousMonth() {
		if (month.get(Calendar.MONTH) == month.getActualMinimum(Calendar.MONTH)) {
			month.set((month.get(Calendar.YEAR) - 1),
					month.getActualMaximum(Calendar.MONTH), 1);

			strmonth = month.get(Calendar.MONTH);
			stryear = month.get(Calendar.YEAR) - 1;

		} else {
			month.set(Calendar.MONTH,
					month.get(Calendar.MONTH) - 1);

			strmonth = month.get(Calendar.MONTH) + 1;
			stryear = month.get(Calendar.YEAR);
		}

	}

	protected void showToast(String string) {
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();

	}

	private void refreshCalendar() {
		TextView title = findViewById(R.id.title);

		adapter.refreshDays();
		adapter.notifyDataSetChanged();
		handler.post(calendarUpdater); // generate some calendar items

		title.setText(DateFormat.format("MMMM yyyy", month));
	}

	private Runnable calendarUpdater = new Runnable() {

		
		public void run() {
			items.clear();

			String strm = String.valueOf(strmonth);

			if (strm.contentEquals("1")) {
				strm = "January";
			} else if (strm.contentEquals("2")) {
				strm = "February";
			} else if (strm.contentEquals("3")) {
				strm = "March";
			} else if (strm.contentEquals("4")) {
				strm = "April";
			} else if (strm.contentEquals("5")) {
				strm = "May";
			} else if (strm.contentEquals("6")) {
				strm = "June";
			} else if (strm.contentEquals("7")) {
				strm = "July";
			} else if (strm.contentEquals("8")) {
				strm = "August";
			} else if (strm.contentEquals("9")) {
				strm = "September";
			} else if (strm.contentEquals("10")) {
				strm = "October";
			} else if (strm.contentEquals("11")) {
				strm = "November";
			} else if (strm.contentEquals("12")) {
				strm = "December";
			}

			Cursor c = db.getdate(strm, String.valueOf(stryear),strCIFBDMUserId);			

			if (c.getCount() > 0) {
				c.moveToFirst();
				for (int ii = 0; ii < c.getCount(); ii++) {
					items.add(c.getString(c.getColumnIndex("DateName")));
					c.moveToNext();
				}
			}

			/*
			 * // Print dates of the current week DateFormat df = new
			 * SimpleDateFormat("yyyy-MM-dd", Locale.US); Stringitemvalue; for
			 * (int i = 0; i < 7; i++) { itemvalue =
			 * df.format(itemmonth.getTime());
			 * itemmonth.add(GregorianCalendar.DATE, 1);
			 * items.add("2012-09-12"); items.add("2012-10-07");
			 * items.add("2012-10-15"); items.add("2012-10-20");
			 * items.add("2012-11-30"); items.add("2012-11-28"); }
			 */

			adapter.setItems(items);
			adapter.notifyDataSetChanged();
		}
	};

	public void add(View v) {
		String date = etdate.getText().toString();
		String eventname = edeventname.getText().toString();
		String month = m;
		String year = y;
		String time = tvDisplayTime.getText().toString();
		String notes = ednotes.getText().toString();

		if (date.equalsIgnoreCase("") || eventname.equalsIgnoreCase("")
				|| time.equalsIgnoreCase("") || notes.equalsIgnoreCase("")) {

			validation();
			
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy");
			Date strDate = null;
			try {
				strDate = sdf.parse(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Calendar cal = Calendar.getInstance();
			String formattedDate = sdf.format(cal.getTime());
			Date strcdate = null;
			try {
				strcdate = sdf.parse(formattedDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (strcdate.after(strDate))
			{
				datevalidation();
			}
			else
			{
			clsCalendar cls = new clsCalendar(date, eventname, month, year, time,strCIFBDMUserId,notes);
			db.AddEvent(cls);
		
			ok();
			
			getallevents();
								
			etdate.setText("");
			edeventname.setText("");
			ednotes.setText("");
			
			final Calendar c = Calendar.getInstance();
			hour = c.get(Calendar.HOUR_OF_DAY);
			minute = c.get(Calendar.MINUTE);

			ampm = c.get(Calendar.AM_PM);
			if (ampm == 0) {
				strampm = "AM";
			} else {
				strampm = "PM";
			}			
			tvDisplayTime.setText(new StringBuilder().append(pad(hour)).append(":")
					.append(pad(minute)).append(" ").append(strampm));							
			
			// refresh grid
			refreshCalendar();
			
			//set alrm
			
			if(chk.isChecked())
			{
				
			
			// Create a new calendar set to the date chosen
	    	// we set the time to midnight (i.e. the first minute of that day)
	    	Calendar ca = Calendar.getInstance();
	    	ca.set(intyear, intmonth, intday);
	    	ca.set(Calendar.HOUR_OF_DAY, 0);
	    	ca.set(Calendar.MINUTE, 0);
	    	ca.set(Calendar.SECOND, 0);
	    	// Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
	    	scheduleClient.setAlarmForNotification(ca);
	    	// Notify the user what they just did
	    	//Toast.makeText(this, "Notification set for: "+ day +"/"+ (month+1) +"/"+ year, Toast.LENGTH_SHORT).show();
	    	
			}
			
			chk.setChecked(false);

		}
		}

	}
	
	public void cancel(View v) {
		
		tbl.getLayoutParams().height = 0; 
		tbl.requestLayout();
	}
	
	public void addevent(View v) {
		
			//tbl.getLayoutParams().height = 200;
		tbl.getLayoutParams().height = LayoutParams.WRAP_CONTENT; 
		tbl.requestLayout();
	}
	
	public void showallevents(View v)
	{
		Intent inte = new Intent(this, ShowAllEventActivity.class);
		startActivity(inte);
	}
	
	private void getallevents()
	{
		Cursor c1 = db.getAllEvents(strCIFBDMUserId);		
		
		String strdate = "";
		String strevent = "";
		String strtmonth = "";
		String strtyear = "";
		String strtime = "";
		String struserid = "";
		String strnotes = "";

		lstmain.clear();

		if (c1.getCount() > 0) {
			c1.moveToFirst();
			for (int ii = 0; ii < c1.getCount(); ii++) {
				clsCalendar single = new clsCalendar(strdate, strevent,
						strtmonth, strtyear, strtime,struserid,strnotes);
				single.setDateName(c1.getString(c1
						.getColumnIndex("DateName")));
				single.setEventName(c1.getString(c1
						.getColumnIndex("EventName")));
				single.setNotes(c1.getString(c1
						.getColumnIndex("Notes")));
				single.setTime(c1.getString(c1.getColumnIndex("Time")));
				lstmain.add(single);
				c1.moveToNext();
			}
		}

		if (lstmain.size() > 0) {
			ItemsAdapter adapter = new ItemsAdapter(context, lstmain);
			lv.setAdapter(adapter);
			Utility.setListViewHeightBasedOnChildren(lv);
			
			lv.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					

					String date = ((TextView) arg1.findViewById(R.id.img)).getText().toString();
					String event = ((TextView) arg1.findViewById(R.id.title)).getText().toString();
					String time = ((TextView) arg1.findViewById(R.id.detail)).getText().toString();
					String notes = ((TextView) arg1.findViewById(R.id.nots)).getText().toString();
										
					String rowid = db.GetRowID(date, event, time, strCIFBDMUserId,notes);
					
					if(!rowid.contentEquals(""))
					{

					/*String ii = lv.getAdapter().getItem(position).toString();
					String g = lv.getItemAtPosition(position).toString();

					int rowid = Integer.parseInt(g) + 1;			

						Cursor c1 = db.getSelectedRowEvent(rowid,strCIFBDMUserId);*/
						
						Cursor c1 = db.getSelectedRowEvent(Integer.parseInt(rowid),strCIFBDMUserId);

						ArrayList<String> lstevent = new ArrayList<String>();

						lstevent.clear();

						if (c1.getCount() > 0) {
							c1.moveToFirst();
							for (int ri = 0; ri < c1.getCount(); ri++) {
								lstevent.add(c1.getString(c1
										.getColumnIndex("DateName")));
								lstevent.add(c1.getString(c1
										.getColumnIndex("EventName")));
								lstevent.add(c1.getString(c1
										.getColumnIndex("Time")));	
								lstevent.add(c1.getString(c1
										.getColumnIndex("Notes")));	
								c1.moveToNext();
							}
							
							Intent i = new Intent(context, ViewCalendarEventActivity.class);
							
							i.putExtra("Date", lstevent.get(0));
							i.putExtra("Event", lstevent.get(1));
							i.putExtra("Time", lstevent.get(2));
							i.putExtra("Notes", lstevent.get(3));
							i.putExtra("RowId", rowid);
							
							startActivity(i);
						}						
				}
				}

			});
			
		} else {
			ArrayList<clsCalendar> lstmain = new ArrayList<clsCalendar>();

			ItemsAdapter adapter = new ItemsAdapter(context, lstmain);
			lv.setAdapter(adapter);
			Utility.setListViewHeightBasedOnChildren(lv);
		}			
	}

	public void btndate(View v) {
		showDialog(DATE_DIALOG_ID);
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

		etdate.setText(totaldate);
		
		chk.setChecked(false);

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
			tvDisplayTime.setText(new StringBuilder().append(pad(hour))
					.append(":").append(pad(minute)).append(" ")
					.append(strampm));*/
			
			if(selectedHour>12)
		    {

				tvDisplayTime.setText((selectedHour - 12) + ":"+(selectedMinute +" pm"));
		    }
		    if(selectedHour==12)
		    {
		    	tvDisplayTime.setText("12"+ ":"+(selectedMinute +" pm"));
		    }
		    if(selectedHour<12)
		    {
		    	tvDisplayTime.setText(selectedHour + ":"+(selectedMinute +" am"));
		    }

		}
	};

	private  String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + c;
	}

	private class ItemsAdapter extends BaseAdapter {
		ArrayList<clsCalendar> items;

		ItemsAdapter(Context context, ArrayList<clsCalendar> items) {
			this.items = items;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			TextView title;
			TextView mDescription;
			TextView datename;
			TextView notes;
			View view = convertView;
			if (view == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = vi.inflate(R.layout.list_with_image, null);
			}
			
			datename = view.findViewById(R.id.img);
			title = view.findViewById(R.id.title);
			mDescription = view.findViewById(R.id.detail);
			notes = view.findViewById(R.id.nots);

			datename.setText(items.get(position).getDateName());
			title.setText(items.get(position).getEventName());
			mDescription.setText(items.get(position).getTime());
			notes.setText(items.get(position).getNotes());

			return view;
		}

		public int getCount() {
			return items.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

	}
	
	private void validation()
	{

		final Dialog dialog = new Dialog(this);		
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 	
		dialog.setContentView(R.layout.loading_window);		
		TextView text = dialog.findViewById(R.id.txtalertheader);
		text.setText("All Fields Required..");		
		Button dialogButton = dialog.findViewById(R.id.btnalert);
		dialogButton.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();

	}
	
	private void ok()
	{		       
         final Dialog dialog = new Dialog(this);		
 		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 	
 		dialog.setContentView(R.layout.loading_window);		
 		TextView text = dialog.findViewById(R.id.txtalertheader);
 		text.setText("Event Added Successfully..");		
 		Button dialogButton = dialog.findViewById(R.id.btnalert);
 		dialogButton.setOnClickListener(new OnClickListener() {			
 			public void onClick(View v) {
 				dialog.dismiss();
 				
 				tbl.getLayoutParams().height = 0; 
        		tbl.requestLayout();
 			}
 		});

 		dialog.show();

	}
	
	private void validatedate()
	{	        
         final Dialog dialog = new Dialog(this);		
 		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 	
 		dialog.setContentView(R.layout.loading_window);		
 		TextView text = dialog.findViewById(R.id.txtalertheader);
 		text.setText("Please Select Date First..!");		
 		Button dialogButton = dialog.findViewById(R.id.btnalert);
 		dialogButton.setOnClickListener(new OnClickListener() {			
 			public void onClick(View v) {
 				dialog.dismiss();
 			}
 		});

 		dialog.show();

	}
	
	private void datevalidation()
	{

		final Dialog dialog = new Dialog(this);		
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 	
		dialog.setContentView(R.layout.loading_window);		
		TextView text = dialog.findViewById(R.id.txtalertheader);
		text.setText("You can not add event for date \n which is come before current date..");		
		Button dialogButton = dialog.findViewById(R.id.btnalert);
		dialogButton.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();

	}
	
	 @Override
	    protected void onStop() {
	    	// When our activity is stopped ensure we also stop the connection to the service
	    	// this stops us leaking our activity into the system *bad*
	    	if(scheduleClient != null)
	    		scheduleClient.doUnbindService();
	    	super.onStop();
	    }
	 
	/* @Override
	protected void onResume() {
			super.onResume();
			
			getallevents();	
			
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
		} */
	 
		
		
	    
	  //after 5 minute it will get log out
	   /* private CountDownTimer mCountDown = new CountDownTimer(300000, 300000)
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
	    	};*/
	    	
	    	private String GetUserType()
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
	    	
	    	@Override
	        public void onBackPressed() {
	    			Intent i = new Intent(CarouselCalendarView.this, CarouselHomeActivity.class);
	    			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
	    			startActivity(i);
	    			finish();
	    		}
}
