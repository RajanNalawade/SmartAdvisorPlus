package sbilife.com.pointofsale_bancaagency.reports;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolderGroupTermRenewalList;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods.UserDetailsValuesModel;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits.DownLoadData;

public class GroupTermRenewalActivity extends AppCompatActivity implements OnClickListener, DownLoadData {

	private Context mContext;
	private CommonMethods mCommonMethods;

	private  final String NAMESPACE = "http://tempuri.org/";
	private  final String URl = ServiceURL.SERVICE_URL;

    private  final String METHOD_NAME_GROUP_TERM_RENEWAL_LIST = "getGroupTermRenewalList";
	
	private String SOAP_ACTION_SH = "http://tempuri.org/saveSmartAdvisorServiceHits";
	private String METHOD_NAME_SH = "saveSmartAdvisorServiceHits";
	
	private int mYear, mMonth, mDay, datecheck = 0;
	private  final int DATE_DIALOG_ID = 1;
	private final int DIALOG_DOWNLOAD_PROGRESS = 0;
	
	private EditText editTextdt, editTextdtto, edt_search_group_renewal;
	private ImageButton btndate, btnbtndateto;
	private Button btn_save_group_term_renewal, btn_reset_group_term_renewal;
	
	private ProgressDialog mProgressDialog;
	
	private String strCIFBDMUserId = "", strCIFBDMEmailId = "",
			strCIFBDMPassword = "", strCIFBDMMObileNo = "",
			strfromdate = "", strtodate = "";
	private LinearLayout ll_search_GroupTermRenewal;
	private TextView txtGroupTermRenewallistcount,txterrordescGroupTermRenewal;

    private long lstGroupTermRenewalListCount = 0;
	
	private ListView lv_GroupTermRenewalList;
	
	private SelectedAdapterGroupRenewal selectedAdapterGroupRenewal;
	
	private DownloadFileAsyncGroupTermRenewal taskNewBusiness;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_group_term_renewal);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		
		taskNewBusiness = new DownloadFileAsyncGroupTermRenewal();
		initialization();
		
		onTouchListeners();
		getUserDetails();
		
		btn_save_group_term_renewal.setOnClickListener(this);
		btn_reset_group_term_renewal.setOnClickListener(this);

		setDates();
	}

	private void setDates() {
			Calendar cal = Calendar.getInstance();
			mYear = cal.get(Calendar.YEAR);
			mMonth = cal.get(Calendar.MONTH);
			mDay = cal.get(Calendar.DAY_OF_MONTH);
			
			int mYear = cal.get(Calendar.YEAR);
			int mMonth = cal.get(Calendar.MONTH);
			int mDay = cal.get(Calendar.DAY_OF_MONTH);
		
			String y = String.valueOf(mYear);
			String m = String.valueOf(mMonth + 1);
			String da = String.valueOf(mDay);
			
			m = mCommonMethods.getFullMonthName(m);
			String totaldate = da + "-" + m + "-" + y;
			editTextdtto.setText(totaldate);
			
			Calendar calenderToDate = Calendar.getInstance();
			calenderToDate.add(Calendar.MONTH, -1);
			int mYeareCPT = calenderToDate.get(Calendar.YEAR);
			int mMontheCPT = calenderToDate.get(Calendar.MONTH);
			int mDayeCPT = calenderToDate.get(Calendar.DAY_OF_MONTH);

			String yeCPT = String.valueOf(mYeareCPT);
			String meCPT = String.valueOf(mMontheCPT + 1);
			String daeCPT = String.valueOf(mDayeCPT);

			String todate = daeCPT + "-" + mCommonMethods.getFullMonthName(meCPT) + "-" + yeCPT;
			editTextdt.setText(todate);
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
	public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
		super.onCreate(savedInstanceState, persistentState);
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:
			mProgressDialog = new ProgressDialog(this,
					ProgressDialog.THEME_HOLO_LIGHT);
			String Message = "Loading Please wait...";
			mProgressDialog.setMessage(Html
					.fromHtml("<font color='#00a1e3'><b>" + Message
							+ "<b></font>"));
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(true);

			mProgressDialog.setButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							//taskPolicyList.cancel(true);
							mProgressDialog.dismiss();
						}
					});

			mProgressDialog.setMax(100);
			mProgressDialog.show();
			return mProgressDialog;

		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, R.style.datepickerstyle,
					mDateSetListener, mYear, mMonth, mDay);

		default:
			return null;
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_save_group_term_renewal:
			
			lv_GroupTermRenewalList.setVisibility(View.GONE);
			txterrordescGroupTermRenewal.setVisibility(View.GONE);
			txtGroupTermRenewallistcount.setVisibility(View.GONE);
			ll_search_GroupTermRenewal.setVisibility(View.GONE);
			txterrordescGroupTermRenewal.setText("");
			txtGroupTermRenewallistcount.setText("");
			if(selectedAdapterGroupRenewal !=null)
			{
				selectedAdapterGroupRenewal.notifyDataSetChanged();
			}
			getNewBussinessList();
			break;
		case R.id.btn_reset_group_term_renewal:
			txterrordescGroupTermRenewal.setText("");
			txtGroupTermRenewallistcount.setText("");
			resetFields();
			break;

		default:
			break;
		}
	}
	
	
	private void initialization() {
		
		mContext = this;
		mCommonMethods = new CommonMethods();
		mCommonMethods.setApplicationToolbarMenu(this,"Group Renewal List"); ;
		
		Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		
		editTextdt = findViewById(R.id.editTextdt);
		btndate = findViewById(R.id.btndate);
		
		editTextdtto = findViewById(R.id.editTextdtto);
		btnbtndateto = findViewById(R.id.btnbtndateto);

		edt_search_group_renewal = findViewById(R.id.edt_search_group_renewal);
		
		btn_save_group_term_renewal = findViewById(R.id.btn_save_group_term_renewal);
		btn_reset_group_term_renewal = findViewById(R.id.btn_reset_group_term_renewal);
		
		ll_search_GroupTermRenewal = findViewById(R.id.ll_search_GroupTermRenewal);
		
		txtGroupTermRenewallistcount = findViewById(R.id.txtGroupTermRenewallistcount);
		
		txterrordescGroupTermRenewal = findViewById(R.id.txterrordescGroupTermRenewal);
		lv_GroupTermRenewalList = findViewById(R.id.lv_GroupTermRenewalList);

		mProgressDialog = new ProgressDialog(this);

		edt_search_group_renewal.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				selectedAdapterGroupRenewal.getFilter().filter(charSequence);
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});
	}
	
	@SuppressLint({ "SimpleDateFormat", "ClickableViewAccessibility" })
    private void onTouchListeners() {

		btndate.setOnTouchListener(new OnTouchListener() {

			// @Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				datecheck = 1;
				showDateProgressDialog();
				return false;
			}
		});

		btnbtndateto.setOnTouchListener(new OnTouchListener() {

			// @Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				datecheck = 0;
				showDateProgressDialog();

				return false;
			}
		});

	}
	
	private void updateDisplay(int year, int month, int day) {

		String y = String.valueOf(year);
		String m = String.valueOf(month + 1);
		// d = String.valueOf(day);
		String da = String.valueOf(day);
		// String totaldate = m + "-" + da + "-" + y;

		m = mCommonMethods.getFullMonthName(m);
	

		String totaldate = da + "-" + m + "-" + y;

		if (datecheck == 1) {
			// datecheck = 0;
			editTextdt.setText(totaldate);
		} /*else if (datecheck == 5) {
			edt_search_GroupTermRenewal_by_end_date.setText(totaldate);
		} */else {
			editTextdtto.setText(totaldate);
		}
	}
	
	@SuppressWarnings("deprecation")
    private void showDateProgressDialog() {
		showDialog(DATE_DIALOG_ID);
	}
	
	@SuppressWarnings("deprecation")
    private void dismissProgressDialog() {
		dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
	}

	@SuppressWarnings("deprecation")
    private void showProgressDialog() {
		showDialog(DIALOG_DOWNLOAD_PROGRESS);
	}
	
	private void getUserDetails() {
		UserDetailsValuesModel userDetailsValuesModel = mCommonMethods
				.setUserDetails(mContext);
		strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
		strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
		strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
		strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
		
		mCommonMethods.printLog("user details", "strCIFBDMUserId:" + strCIFBDMUserId
				+ " strCIFBDMEmailId:" + strCIFBDMEmailId
				+ " strCIFBDMPassword:" + strCIFBDMPassword
				+ " strCIFBDMMObileNo:" + strCIFBDMMObileNo);
	}
	
	private void resetFields() {
		
		lv_GroupTermRenewalList.setVisibility(View.GONE);
		txterrordescGroupTermRenewal.setVisibility(View.GONE);
		txtGroupTermRenewallistcount.setVisibility(View.GONE);
		ll_search_GroupTermRenewal.setVisibility(View.GONE);
		edt_search_group_renewal.setVisibility(View.GONE);
		if(selectedAdapterGroupRenewal !=null)
		{
			selectedAdapterGroupRenewal.notifyDataSetChanged();
		}

		editTextdt.setText("");
		editTextdtto.setText("");

		txtGroupTermRenewallistcount.setVisibility(View.GONE);
		txterrordescGroupTermRenewal.setVisibility(View.GONE);
		
		Calendar cal = Calendar.getInstance();
		mYear = cal.get(Calendar.YEAR);
		mMonth = cal.get(Calendar.MONTH);
		mDay = cal.get(Calendar.DAY_OF_MONTH);
	}
	
	private void getNewBussinessList() {
		taskNewBusiness = new DownloadFileAsyncGroupTermRenewal();
		txtGroupTermRenewallistcount.setVisibility(View.VISIBLE);

		strfromdate = editTextdt.getText().toString();
		strtodate = editTextdtto.getText().toString();

		if (strfromdate.equalsIgnoreCase("")
				|| strtodate.equalsIgnoreCase("")) {
			// validationAlert();
			mCommonMethods.showMessageDialog(mContext, "All Fields Required..");
		} else {
			final SimpleDateFormat formatter = new SimpleDateFormat(
					"dd-MMMM-yyyy");

			String strfromdate = editTextdt.getText().toString();
			String strtodate = editTextdtto.getText().toString();

			Date d1 = null;
			try {
				d1 = formatter.parse(strfromdate);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			Date d2 = null;
			try {
				d2 = formatter.parse(strtodate);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			if ((d2.after(d1)) || (d2.equals(d1))) {

				if (mCommonMethods.isNetworkConnected(mContext)) {
					service_hits(METHOD_NAME_GROUP_TERM_RENEWAL_LIST);

				} else {
					mCommonMethods.showMessageDialog(mContext,
							"Internet Connection Not Present,Try again..");
				}
			} else {
				mCommonMethods.showMessageDialog(mContext,
						"To date should be greater than From date");
			}
		}
	}
	
	private void service_hits(String strServiceName) {

		strfromdate = editTextdt.getText().toString();
		strtodate = editTextdtto.getText().toString();

		final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");
		final SimpleDateFormat formatter1 = new SimpleDateFormat("MM-dd-yyyy");
		try {
			Date dt = formatter.parse(strfromdate);
			strfromdate = formatter1.format(dt);

			Date dt1 = formatter.parse(strtodate);
			strtodate = formatter1.format(dt1);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		StringBuilder str = new StringBuilder();
		str.append(strfromdate);
		str.append(",");
		str.append(strtodate);

		ServiceHits service = new ServiceHits(mContext,strServiceName, str.toString(),
				strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMMObileNo,
				strCIFBDMPassword, this);
		service.execute();
	}

	@Override
	public void downLoadData() {
		//call service of new bussiness list
		taskNewBusiness = new DownloadFileAsyncGroupTermRenewal();
		taskNewBusiness.execute();
	}
	
	class DownloadFileAsyncGroupTermRenewal extends AsyncTask<String, String, String>{

		private volatile boolean running = true; 
		private String inputpolicylist = "",strGroupNewBusinessListErrorCOde = "",
				strfromdate = "",strtodate = "";
		@Override
		protected void onPreExecute() {
			showProgressDialog();
			 strfromdate = editTextdt.getText().toString();
			 strtodate = editTextdtto.getText().toString();

			final SimpleDateFormat formatter = new SimpleDateFormat(
					"dd-MMMM-yyyy");
			final SimpleDateFormat formatter1 = new SimpleDateFormat(
					"MM-dd-yyyy");
			try {
				Date dt = formatter.parse(strfromdate);
				strfromdate = formatter1.format(dt);

				Date dt1 = formatter.parse(strtodate);
				strtodate = formatter1.format(dt1);

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		protected void onProgressUpdate(String... progress) {
			// TODO Auto-generated method stub
			mCommonMethods.printLog("ANDRO_ASYNC", progress[0]);
			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
			
			
		}
		
		@Override
		protected String doInBackground(String... params) {
			try {

				running = true;
				SoapObject request = null;
				
				request = new SoapObject(NAMESPACE,METHOD_NAME_GROUP_TERM_RENEWAL_LIST);
				request.addProperty("strFromdate", strfromdate);
				request.addProperty("strTodate", strtodate);
				request.addProperty("strCode", mCommonMethods.setUserDetails(mContext).getStrCIFBDMUserId());
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);

				mCommonMethods.TLSv12Enable();

				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);

				HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
				try {

                    String SOAP_ACTION_GROUP_TERM_RENEWAL_LIST = "http://tempuri.org/getGroupTermRenewalList";
                    androidHttpTranport.call(
                            SOAP_ACTION_GROUP_TERM_RENEWAL_LIST, envelope);
					Object response = envelope.getResponse();

					if (!response.toString().contentEquals("anyType{}")) {

						SoapPrimitive sa = null;
						try {
							sa = (SoapPrimitive) envelope.getResponse();

							inputpolicylist = sa.toString();

							ParseXML prsObj = new ParseXML();

							inputpolicylist = prsObj.parseXmlTag(
									inputpolicylist, "CIFPolicyList");
							inputpolicylist = new ParseXML().parseXmlTag(
									inputpolicylist, "ScreenData");
							strGroupNewBusinessListErrorCOde = inputpolicylist;

							if (strGroupNewBusinessListErrorCOde == null) {
								inputpolicylist = sa.toString();
								inputpolicylist = prsObj.parseXmlTag(
										inputpolicylist, "CIFPolicyList");

								List<String> Node = prsObj.parseParentNode(
										inputpolicylist, "Table");

								List<XMLHolderGroupTermRenewalList> nodeData = prsObj
										.parseNodeGroupTermRenewalList(Node);

								// final List<XMLHolderMaturity> lst;
                                List<XMLHolderGroupTermRenewalList> lstGroupTermRenewal = new ArrayList<XMLHolderGroupTermRenewalList>();
								lstGroupTermRenewal.clear();

								for (XMLHolderGroupTermRenewalList node : nodeData) {

									lstGroupTermRenewal.add(node);
								}

								lstGroupTermRenewalListCount = lstGroupTermRenewal.size();
								selectedAdapterGroupRenewal = new SelectedAdapterGroupRenewal(
										0, mContext, lstGroupTermRenewal);
								//selectedAdapterGroupRenewal.setNotifyOnChange(true);

								//registerForContextMenu(lv_GroupTermRenewalList);

							} else {
								// txterrordesc.setText("No Data");
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
					} else {

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
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (mProgressDialog.isShowing()) {
				dismissProgressDialog();
			}
			//lvMaturityList.setVisibility(View.GONE);
			if (running) {
				
				//txterrordescmaturity.setVisibility(View.VISIBLE);
				//txtmaturitylistcount.setVisibility(View.VISIBLE);
				if (strGroupNewBusinessListErrorCOde == null) {
					ll_search_GroupTermRenewal.setVisibility(View.VISIBLE);
					lv_GroupTermRenewalList.setVisibility(View.VISIBLE);
					edt_search_group_renewal.setVisibility(View.VISIBLE);

					txterrordescGroupTermRenewal.setText("");
					txtGroupTermRenewallistcount.setText("Total Policy : "
							+ lstGroupTermRenewalListCount);
					lv_GroupTermRenewalList.setAdapter(selectedAdapterGroupRenewal);

					//Utility.setListViewHeightBasedOnChildren(lv_GroupTermRenewalList);
					
				} else {
					ll_search_GroupTermRenewal.setVisibility(View.GONE);
					edt_search_group_renewal.setVisibility(View.GONE);

					txterrordescGroupTermRenewal.setText("No Record Found");
					txtGroupTermRenewallistcount.setText("Total Policy : " + 0);
					List<XMLHolderGroupTermRenewalList> lst;
					XMLHolderGroupTermRenewalList node = null;
					lst = new ArrayList<XMLHolderGroupTermRenewalList>();
					lst.clear();
					lst.add(node);
					selectedAdapterGroupRenewal = new SelectedAdapterGroupRenewal(
							0, mContext, lst);
					/*selectedAdapterGroupRenewal.setNotifyOnChange(true);*/
					lv_GroupTermRenewalList.setAdapter(selectedAdapterGroupRenewal);

					//Utility.setListViewHeightBasedOnChildren(lv_GroupTermRenewalList);
					
				}
			} else {
				mCommonMethods.showMessageDialog(mContext, mCommonMethods.WEEK_INTERNET_MESSAGE);
			}
		}
	}
	
	class SelectedAdapterGroupRenewal extends BaseAdapter implements Filterable {

		// used to keep selected position in ListView
		private int selectedPos = -1; // init value for not-selected
		private Context adapterContext;
		List<XMLHolderGroupTermRenewalList> lst, lstSearch;

		SelectedAdapterGroupRenewal(int selectedPos, Context adapterContext, List<XMLHolderGroupTermRenewalList> lst) {
			this.selectedPos = selectedPos;
			this.adapterContext = adapterContext;
			this.lst = lst;
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
		public int getCount() {
			return this.lst.size();
		}

		@Override
		public Object getItem(int i) {
			return this.lst.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public Filter getFilter() {
			return new Filter() {
				@Override
				protected FilterResults performFiltering(CharSequence charSequence) {

					final FilterResults oReturn = new FilterResults();
					final ArrayList<XMLHolderGroupTermRenewalList> results = new ArrayList<>();

					if (lstSearch == null)
						lstSearch = lst;

					if (charSequence != null) {
						if (lstSearch != null && lstSearch.size() > 0) {
							for (final XMLHolderGroupTermRenewalList g : lstSearch) {
								if (g.getPOLICY_NO().contains(charSequence.toString()))
									results.add(g);
								else if (g.getPOLICY_HOLDER_NAME().toLowerCase().contains(charSequence.toString()))
									results.add(g);
							}
						}
						oReturn.values = results;
					}
					return oReturn;
				}

				@Override
				protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
					lst = (ArrayList<XMLHolderGroupTermRenewalList>) filterResults.values;
					notifyDataSetChanged();

					txtGroupTermRenewallistcount.setText("Total Policy : "
							+ lst.size());
				}
			};
		}

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}

		class Holder {
			private TextView textviewPolicyNumber, textviewPolicyHolderName ,textviewPolicyRenewalDueDate;
		}

		@SuppressWarnings("deprecation")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			Holder mHolder;

			// only inflate the view if it's null
			if (convertView == null) {
				/*LayoutInflater vi = (LayoutInflater) adapterContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.list_item_group_term_renewal_list, null);*/
				convertView=LayoutInflater.from(adapterContext).inflate(R.layout.list_item_group_term_renewal_list, parent, false);
				mHolder = new Holder();

				mHolder.textviewPolicyNumber = convertView.findViewById(R.id.textviewPolicyNumber);
				mHolder.textviewPolicyHolderName = convertView.findViewById(R.id.textviewPolicyHolderName);
				//TextView textviewPolicyStartDate = (TextView) v.findViewById(R.id.textviewPolicyStartDate);

				mHolder.textviewPolicyRenewalDueDate = convertView.findViewById(R.id.textviewPolicyRenewalDueDate);

				convertView.setTag(mHolder);
			}else{
				mHolder = (Holder) convertView.getTag();
			}


			mHolder.textviewPolicyNumber.setText(lst.get(position).getPOLICY_NO() == null ? ""
						: lst.get(position).getPOLICY_NO());
				
				 /*WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
				 Display display = wm.getDefaultDisplay();
				 int screenWidth = display.getWidth(); // Get full screen width
				
				 int eightyPercent = (screenWidth * 50) / 100;*/
				 
				 String policyHolderName = lst.get(position).getPOLICY_HOLDER_NAME() == null ? ""
							: lst.get(position).getPOLICY_HOLDER_NAME();

			mHolder.textviewPolicyHolderName.setText(policyHolderName);

				 /*float textWidthLifeAssuredName = mHolder.textviewPolicyHolderName.getPaint().measureText(policyHolderName);
				 
				 float linesFloat = (textWidthLifeAssuredName/eightyPercent) + 0.7f;
				 int linesInt = Math.round(linesFloat);
				 
				 int numberOfLineslifeAssuredName =  linesInt ;

			mHolder.textviewPolicyHolderName.setLines(numberOfLineslifeAssuredName);*/

				mHolder.textviewPolicyRenewalDueDate.setText(lst.get(position).getPOL_START_DATE() == null ? ""
							: lst.get(position).getPOL_START_DATE());
				
				 /*textviewPolicyRenewalDueDate.setText(lst.get(position).getRENEWAL_DUE_DATE() == null ? ""
							: lst.get(position).getRENEWAL_DUE_DATE());*/
				 /*textviewPolicyStatus.setText(lst.get(position).getPOLICY_STATUS() == null ? ""
							: lst.get(position).getPOLICY_STATUS());*/

			return (convertView);
		}
	}

}