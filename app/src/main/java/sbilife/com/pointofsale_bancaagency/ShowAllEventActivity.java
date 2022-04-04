package sbilife.com.pointofsale_bancaagency;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class ShowAllEventActivity extends AppCompatActivity {
		
	/*
	 * these are all global variables.
	 */
    private ListView lv;
	private ArrayList<clsCalendar> lstmain = new ArrayList<clsCalendar>();
	
	//for store userid in bdm tracker table
    private String strCIFBDMUserId;
		private String strCIFBDMEmailId = "";
		private String strCIFBDMPassword = "";
		private String strCIFBDMMObileNo = "";
		
		private DatabaseHelper db;
	
	private final Context context = this;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.showallevent);
        new CommonMethods().setActionbarLayout(this);
            
        this.lv = this.findViewById(R.id.list);
        
        db = new DatabaseHelper(this);	
        
			try {
				strCIFBDMUserId = SimpleCrypto.decrypt("SBIL",db.GetCIFNo());
				strCIFBDMEmailId = SimpleCrypto.decrypt("SBIL",db.GetEmailId());
				strCIFBDMPassword = db.GetPassword();
				strCIFBDMMObileNo = SimpleCrypto.decrypt("SBIL",db.GetMobileNo());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        showallevents();
	}
	
	private void showallevents()
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
	
	
	
	 @Override
	public void onBackPressed() {
		 Intent i = new Intent(this,CarouselCalendarView.class);
		 i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 startActivity(i);
		 finish();
	}

	@Override
	protected void onResume() {
			super.onResume();
			
			getallevents();		
			
			// mCountDown.start();
		}
	 
	 /*@Override//
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
	    }*/
	    
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