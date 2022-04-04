package sbilife.com.pointofsale_bancaagency;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
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
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.service.ScheduleClient;

public class CalendarView extends Fragment {
	
	/*
	 * these are all global variable.
	 */
	
	private Button btnshowcust_dob;
	
	private EditText ednotes;
	
	private TextView title;
	
	private Button btnaddevent;
    private Button btnshowallevents;
    private Button btnadd;
    private Button btncancel;
	private ImageButton btndate;
	
	private TableLayout tbl;
	
	//set alrm
	
	private int intday;
	private int intmonth;
	private int intyear;
	
	private CheckBox chk;
	private ScheduleClient scheduleClient;

	private ArrayList<clsCalendar> lstmain = new ArrayList<clsCalendar>();
	private ListView lv;
	private final Context context = getActivity();

	// for time picker

	private EditText tvDisplayTime;

    private int hour;
	private int minute;
	private int ampm;

	private String strampm;

	private static final int TIME_DIALOG_ID = 999;

    private int mYear;
	private int mMonth;
	private int mDay;
	private static final int DATE_DIALOG_ID = 0;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {
		 View rootView = inflater.inflate(R.layout.calendar, container, false);
		Locale.setDefault(Locale.US);
		month = (GregorianCalendar) Calendar.getInstance();
		itemmonth = (GregorianCalendar) month.clone();

		items = new ArrayList<String>();
		adapter = new CalendarAdapter(getActivity(), month);

		strmonth = month.get(Calendar.MONTH) + 1;
		stryear = month.get(Calendar.YEAR);

		db = new DatabaseHelper(getActivity());		
		etdate = rootView.findViewById(R.id.editTextdt);
		edeventname = rootView.findViewById(R.id.edeventname);

         Button selectedDayMonthYearButton = rootView.findViewById(R.id.selectedDayMonthYear);
		selectedDayMonthYearButton.setText("Selected: ");		
		
		tbl = rootView.findViewById(R.id.tbl);
		
		btnaddevent = rootView.findViewById(R.id.btnaddevent);
		btnshowallevents = rootView.findViewById(R.id.btnshowallevents);
		btnadd = rootView.findViewById(R.id.btnadd);
		btncancel = rootView.findViewById(R.id.btncancel);
		btndate = rootView.findViewById(R.id.btndate);
		
		ednotes = rootView.findViewById(R.id.ednotes);
		
		btnshowcust_dob = rootView.findViewById(R.id.btnshowcust_dob);
		if(!GetUserType().contentEquals("AGENT"))
			{
		btnshowcust_dob.setVisibility(View.GONE);
			}
		else
		{
			btnshowcust_dob.setVisibility(View.VISIBLE);
		}
		
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
				Intent inte = new Intent(getActivity(), ShowAllEventActivity.class);
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
});

		btncancel.setOnClickListener(new OnClickListener() {
	
	//@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		tbl.getLayoutParams().height = 0; 
		tbl.requestLayout();
	}
});			
		
		btndate.setOnClickListener(new OnClickListener() {
			
			//@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//getActivity().showDialog(DATE_DIALOG_ID);
				onCreateDialog(DATE_DIALOG_ID);
			}
		});
		
		btnshowcust_dob.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Intent inte = new Intent(getActivity(), CustomerDOBActivity.class);
				startActivity(inte);
			}
		});

		GridView gridview = rootView.findViewById(R.id.gridview);
		gridview.setAdapter(adapter);

		handler = new Handler();
		handler.post(calendarUpdater);

		title = rootView.findViewById(R.id.title);
		title.setText(DateFormat.format("MMMM yyyy", month));

		RelativeLayout previous = rootView.findViewById(R.id.previous);
		
			try {
				strCIFBDMUserId = SimpleCrypto.decrypt("SBIL",db.GetCIFNo());
				strCIFBDMEmailId = SimpleCrypto.decrypt("SBIL",db.GetEmailId());
				strCIFBDMPassword = db.GetPassword();
				strCIFBDMMObileNo = SimpleCrypto.decrypt("SBIL",db.GetMobileNo());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		/*
		 * when you click on previous button in calander it will show previous
		 * month. 
		 */
		
		previous.setOnClickListener(new OnClickListener() {

			
			public void onClick(View v) {
				setPreviousMonth();
				refreshCalendar();
			}
		});

		RelativeLayout next = rootView.findViewById(R.id.next);
		
		/*
		 * when you click on next button in calander it will show next
		 * month. 
		 */
		
		next.setOnClickListener(new OnClickListener() {

			
			public void onClick(View v) {
				setNextMonth();
				refreshCalendar();

			}
		});

		/*
		 * when you click on calender in specific date it will show event if there.		 
		 */
		
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
					ItemsAdapter adapter = new ItemsAdapter(getActivity(), lstmain);
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
									
									Intent i = new Intent(getActivity(), ViewCalendarEventActivity.class);
									
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

					ItemsAdapter adapter = new ItemsAdapter(getActivity(), lstmain);
					lv.setAdapter(adapter);
					Utility.setListViewHeightBasedOnChildren(lv);
				}


			}
		});

		tvDisplayTime = rootView.findViewById(R.id.tvTime);
         ImageButton btnChangeTime = rootView.findViewById(R.id.btnChangeTime);

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

				//getActivity().showDialog(TIME_DIALOG_ID);
				onCreateDialog(TIME_DIALOG_ID);
			}
		});

		this.lv = rootView.findViewById(R.id.list);
		
		 // Create a new service client and bind our activity to this service
        scheduleClient = new ScheduleClient(getActivity());
        scheduleClient.doBindService();
		
		chk = rootView.findViewById(R.id.chk);
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
			
			 rootView.setOnTouchListener(new OnTouchListener() {
					
					//@Override
					public boolean onTouch(View arg0, MotionEvent arg1) {
						// TODO Auto-generated method stub
						final View currentFocus = getActivity().getCurrentFocus();
				        if (!(currentFocus instanceof EditText) || !isTouchInsideView(arg1, currentFocus)) {
				            ((InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE))
				                .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);                       

				        }
						return false;
					}
				});
			
			return rootView;

	}

	/*
	 * it is regarding calender control
	 */
	
	private void setNextMonth() {
		if (month.get(Calendar.MONTH) == month
				.getActualMaximum(Calendar.MONTH)) {
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

	/*
	 * it is regarding calender control
	 */
	
	private void setPreviousMonth() {
		if (month.get(Calendar.MONTH) == month
				.getActualMinimum(Calendar.MONTH)) {
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
		Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();

	}

	/*
	 * after add event in calender it will aotomatic refresh calander.
	 */
	
	private void refreshCalendar() {
		//TextView title = (TextView) rootView.findViewById(R.id.title);

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

	/*
	 * it is get data whatever you add in event form and store in local db.
	 */
	
	public void add(View v) {
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
	
	/*
	 * it is regarding whatever button you click it will show that layout.
	 */
	
	public void cancel(View v) {
		
		tbl.getLayoutParams().height = 0; 
		tbl.requestLayout();
	}
	
	/*
	 * it is regarding whatever button you click it will show that layout.
	 */
	
	public void addevent(View v) {
		
			tbl.getLayoutParams().height = LayoutParams.WRAP_CONTENT; 
			tbl.requestLayout();
	}
	
	/*
	 * when you click show all event bottn it will show all event whatever
	 * you add in local database.
	 */
	
	public void showallevents(View v)
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
			ItemsAdapter adapter = new ItemsAdapter(getActivity(), lstmain);
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
							
							Intent i = new Intent(getActivity(), ViewCalendarEventActivity.class);
							
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

			ItemsAdapter adapter = new ItemsAdapter(getActivity(), lstmain);
			lv.setAdapter(adapter);
			Utility.setListViewHeightBasedOnChildren(lv);
		}
				
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
			ItemsAdapter adapter = new ItemsAdapter(getActivity(), lstmain);
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
							
							Intent i = new Intent(getActivity(), ViewCalendarEventActivity.class);
							
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

			ItemsAdapter adapter = new ItemsAdapter(getActivity(), lstmain);
			lv.setAdapter(adapter);
			Utility.setListViewHeightBasedOnChildren(lv);
		}
				
	}

	/*
	 * when you click on calender icon button to choose date,it will fire
	 * date picker control.
	 */
	
	@SuppressWarnings("deprecation")
	public void btndate(View v) {
		getActivity().showDialog(DATE_DIALOG_ID);
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

	/*
	 * when you click on date picker icon it will shoe date picker control
	 * and if you click on time picker icon it will show time picker control.	 
	 */
	
	//@Override
    private Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:

			/*return new DatePickerDialog(getActivity(), mDateSetListener, mYear, mMonth,
					mDay);*/
			DatePickerDialog dialog = new DatePickerDialog(getActivity(), mDateSetListener, 
					mYear, mMonth,mDay);
			dialog.show();

			return dialog;

		case TIME_DIALOG_ID:
			// set time picker as current time
			/*return new TimePickerDialog(getActivity(), timePickerListener, hour, minute,
					false);*/
			/*TimePickerDialog Tdialog = new TimePickerDialog(getActivity(), timePickerListener, hour, minute,
					false);
			
			Tdialog.show();
			
			return Tdialog;*/
			
			TimePickerDialog Tdialog = new TimePickerDialog(getActivity(), timePickerListener, hour, minute,
					false);
			
			String str = tvDisplayTime.getText().toString();
			
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
	        Date date = null;
	        try {
	            date = sdf.parse(str);
	        } catch (ParseException e) {
	        }
	        Calendar c = Calendar.getInstance();
	        c.setTime(date);

	        TimePicker picker = new TimePicker(getActivity().getApplicationContext());
	        picker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY ));
	        picker.setCurrentMinute(c.get(Calendar.MINUTE));
	        //String a = (c.get(Calendar.AM_PM)==(Calendar.AM))? "AM":"PM";
				
	        int hr=picker.getCurrentHour();
	        int mn = picker.getCurrentMinute();	       
	       
	        ((Tdialog)).updateTime(hr, mn);
	        Tdialog.show();
	        return Tdialog;

		}
		return null;
	}
	
	//@Override
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

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + c;
	}

	/*
	 * it is do the binding of event data to listview.
	 */
	
	private class ItemsAdapter extends BaseAdapter {
		ArrayList<clsCalendar> items;

		ItemsAdapter(Context context, ArrayList<clsCalendar> items) {
			this.items = items;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			TextView title;
			TextView notes;
			TextView mDescription;
			TextView datename;
			View view = convertView;
			if (view == null) {
				LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = vi.inflate(R.layout.list_with_image, null);
			}
			
			datename = view.findViewById(R.id.img);
			title = view.findViewById(R.id.title);
			notes = view.findViewById(R.id.nots);
			mDescription = view.findViewById(R.id.detail);

			datename.setText(items.get(position).getDateName());
			title.setText(items.get(position).getEventName());
			notes.setText(items.get(position).getNotes());
			mDescription.setText(items.get(position).getTime());

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
	
	/*
	 * when any field is empty in input form while press button then it will pop up.
	 */
	
	private void validation()
	{

		final Dialog dialog = new Dialog(getActivity());		
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
	
	/*
	 * if event is store in local database then this alert is show.
	 */
	
	private void ok()
	{		       
         final Dialog dialog = new Dialog(getActivity());		
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
	
	/*
	 * when you set for alert in perticular event that time date shpuld be
	 * mandatory.so if you don not select date while chek alert box that time
	 * it is fire. 
	 */
	
	private void validatedate()
	{	        
         final Dialog dialog = new Dialog(getActivity());		
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

		final Dialog dialog = new Dialog(getActivity());		
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
	public void onStop() {
	    	// When our activity is stopped ensure we also stop the connection to the service
	    	// this stops us leaking our activity into the system *bad*
	    	if(scheduleClient != null)
	    		scheduleClient.doUnbindService();
	    	super.onStop();
	    }
	 
	 /*
		 * when you press hard ware back button then it will fire.
		 */
	 
	// @Override
		public boolean onKeyDown(int keyCode,KeyEvent event)
		{
			if(keyCode == KeyEvent.KEYCODE_BACK)
			{
				final Dialog dialog = new Dialog(getActivity());		
		 		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 	
		 		dialog.setContentView(R.layout.loading_window_twobutton);		
		 		TextView text = dialog.findViewById(R.id.txtalertheader);
		 		text.setText("Do you want to Log out..?");		
		 		Button dialogButton = dialog.findViewById(R.id.btnalert);
		 		dialogButton.setOnClickListener(new OnClickListener() {			
		 			public void onClick(View v) {
		 				dialog.dismiss();
		 				
		 				new CommonMethods().logoutToLoginActivity(context);
		 			}
		 		});
		 		Button dialogButtoncancel = dialog.findViewById(R.id.btnalertcancel);
		 		dialogButtoncancel.setOnClickListener(new OnClickListener() {			
		 			public void onClick(View v) {
		 				dialog.dismiss();
		 			}
		 		});
		 		
		 		dialog.show();
				return true;
			}
			return super.getActivity().onKeyDown(keyCode, event);
		}
		
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
	public void onResume() {
			super.onResume();
			
			getallevents();			
		}
	 	
	    private boolean isTouchInsideView(final MotionEvent ev, final View currentFocus) {
	        final int[] loc = new int[2];
	        currentFocus.getLocationOnScreen(loc);
	        return ev.getRawX() > loc[0] && ev.getRawY() > loc[1] && ev.getRawX() < (loc[0] + currentFocus.getWidth())
	            && ev.getRawY() < (loc[1] + currentFocus.getHeight());
	    }
}
