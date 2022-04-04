package sbilife.com.pointofsale_bancaagency.reports;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.CustomerDetailActivity;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolderSBDueList;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.Utility;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods.UserDetailsValuesModel;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits.DownLoadData;

public class BancaReportsSBDueListActivity extends AppCompatActivity implements OnClickListener, DownLoadData {
    private final String METHOD_NAME_SB_DUE_LIST = "getSBDueList";

    private DownloadFileAsyncSBDueList taskSBDueList;

    private final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private final int DATE_DIALOG_ID = 1;
    private ProgressDialog mProgressDialog;
    private int mYear, mMonth, mDay, datecheck = 0;

    private Context context;
    private CommonMethods mCommonMethods;

    private String strCIFBDMUserId = "";
    private String strCIFBDMEmailId = "";
    private String strCIFBDMPassword = "";
    private String strCIFBDMMObileNo = "";

    private SelectedAdapterSbDueList selectedAdapterSbDueList;
    private TextView txtSbDueListlistcount, txterrordescSbDueList;

    //SB Due List
    private ListView listViewSBDueList;
    private LinearLayout lnSBDueList, lnsearchSBDueList;

    private Spinner spinSearchSBDueListStatus;
    private EditText edSearchSBDueListFN, edSearchSBDueListMD, edSearchSBDueListPN,editTextdtto,editTextdt;

    private List<XMLHolderSBDueList> lstSBDueLIst;
    private TextView txtMaturityListNote;

    private  ArrayList<XMLHolderSBDueList> array_sort;
    //End SB Due List
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.banca_reports_sb_due_list);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        new CommonMethods().setApplicationToolbarMenu(this,"SB Due List");

        context = this;
        mCommonMethods = new CommonMethods();

        array_sort = new ArrayList<>();
        array_sort.clear();

        mProgressDialog = new ProgressDialog(this);
        taskSBDueList = new DownloadFileAsyncSBDueList();
        //SB Due List
        listViewSBDueList = findViewById(R.id.listViewSBDueList);
        lnSBDueList = findViewById(R.id.lnSBDueList);
        lnsearchSBDueList = findViewById(R.id.lnsearchSBDueList);

        txtSbDueListlistcount = findViewById(R.id.txtSbDueListlistcount);
        txterrordescSbDueList = findViewById(R.id.txterrordescSbDueList);

        spinSearchSBDueListStatus = findViewById(R.id.spinSearchSBDueListStatus);
        Button btn_click_SBDueList_status = findViewById(R.id.btn_click_SBDueList_status);
        Button btn_click_SBDueList_maturitydate = findViewById(R.id.btn_click_SBDueList_maturitydate);
        Button btn_click_SBDueList_policyno = findViewById(R.id.btn_click_SBDueList_policyno);
        Button btn_click_SBDueList_fn = findViewById(R.id.btn_click_SBDueList_fn);

        edSearchSBDueListFN = findViewById(R.id.edSearchSBDueListFN);
        edSearchSBDueListMD = findViewById(R.id.edSearchSBDueListMD);
        edSearchSBDueListPN = findViewById(R.id.edSearchSBDueListPN);

        ImageButton imageButtonSBDueListMD = findViewById(R.id.imageButtonSBDueListMD);
        imageButtonSBDueListMD.setOnClickListener(this);
        //End SB Due List

        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");
        if (fromHome != null && fromHome.equalsIgnoreCase("N")) {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");

            try {
                strCIFBDMPassword = SimpleCrypto.encrypt("SBIL", "sbil");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
                getUserDetails();
        }
        txtMaturityListNote = findViewById(R.id.txtMaturityListNote);
        txtMaturityListNote.setVisibility(View.GONE);


        btn_click_SBDueList_status.setOnClickListener(this);
        btn_click_SBDueList_maturitydate.setOnClickListener(this);
        btn_click_SBDueList_policyno.setOnClickListener(this);
        btn_click_SBDueList_fn.setOnClickListener(this);

        taskSBDueList = new DownloadFileAsyncSBDueList();
        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);



        editTextdtto = findViewById(R.id.editTextdtto);
        editTextdt = findViewById(R.id.editTextdt);
        Button buttonSubmitReportsSbDueList = findViewById(R.id.buttonSubmitReportsSbDueList);
        Button buttonResetReportsSbDueList = findViewById(R.id.buttonResetReportsSbDueList);

        ImageButton btnbtndateto = findViewById(R.id.btnbtndateto);
        ImageButton btndate = findViewById(R.id.btndate);

        buttonSubmitReportsSbDueList.setOnClickListener(this);
        buttonResetReportsSbDueList.setOnClickListener(this);

        btnbtndateto.setOnClickListener(this);
        btndate.setOnClickListener(this);

        /*if (mCommonMethods.isNetworkConnected(context)) {
            edSearchSBDueListPN.setText("");
            edSearchSBDueListFN.setText("");
            spinSearchSBDueListStatus.setSelection(0);
            edSearchSBDueListMD.setText("");
            spinSearchSBDueList.setSelection(0);

            array_sort = new ArrayList<>();
            array_sort.clear();

            service_hits();
        } else {
            //intereneterror();
            mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
        }*/

        setDates();
    }
    private void setDates() {
        Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        int mDay = cal.get(Calendar.DAY_OF_MONTH);

        String y = String.valueOf(mYear);
        String m = String.valueOf(mMonth + 1);
        String da = String.valueOf(mDay);

        m = mCommonMethods.getFullMonthName(m);
        String totaldate = da + "-" + m + "-" + y;
        editTextdt.setText(totaldate);

        Calendar calenderToDate = Calendar.getInstance();
        calenderToDate.add(Calendar.MONTH, 1);
        int mYeareCPT = calenderToDate.get(Calendar.YEAR);
        int mMontheCPT = calenderToDate.get(Calendar.MONTH);
        int mDayeCPT = calenderToDate.get(Calendar.DAY_OF_MONTH);

        String yeCPT = String.valueOf(mYeareCPT);
        String meCPT = String.valueOf(mMontheCPT + 1);
        String daeCPT = String.valueOf(mDayeCPT);

        String todate = daeCPT + "-" + mCommonMethods.getFullMonthName(meCPT) + "-" + yeCPT;
        editTextdtto.setText(todate);

    }

    private void service_hits(String input) {
        ServiceHits service = new ServiceHits(context,
                METHOD_NAME_SB_DUE_LIST, input, strCIFBDMUserId,
                strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();
    }

    private void getUserDetails() {
        UserDetailsValuesModel userDetailsValuesModel = mCommonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

    }

    @Override
    public void downLoadData() {
        WeakReference<BancaReportsSBDueListActivity> mainActivityWeakRef = new WeakReference<>(BancaReportsSBDueListActivity.this);

        if (mainActivityWeakRef.get() != null && !mainActivityWeakRef.get().isFinishing()) {
        startDownloadSBDueList();
    }
    }

    private void startDownloadSBDueList() {

        taskSBDueList = new DownloadFileAsyncSBDueList();
        taskSBDueList.execute();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(taskSBDueList!=null)
            {
                taskSBDueList.cancel(true);
            }
            if(mProgressDialog!=null)
            {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
                String Message = "Loading Please wait...";
                mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(true);

                mProgressDialog.setButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                if(taskSBDueList!=null)
                                {
                                    taskSBDueList.cancel(true);
                                }
                                if (mProgressDialog != null) {
                                    if (mProgressDialog.isShowing()) {
                                        mProgressDialog.dismiss();
                                    }
                            }
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

    private void updateDisplay(int year, int month, int day) {

        String y = String.valueOf(year);
        String m = String.valueOf(month + 1);
        String da = String.valueOf(day);

        m = mCommonMethods.getFullMonthName(m);
        String totaldate = da + "-" + m + "-" + y;

        switch (datecheck) {
            case 14:
                if (day < 10) {
                    da = "0" + da;
                    totaldate = da + "-" + m + "-" + y;
                }
                edSearchSBDueListMD.setText(totaldate);
                break;
            case 1:
                editTextdt.setText(totaldate);
                break;
            default:
                editTextdtto.setText(totaldate);
                break;
        }
    }

    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay(mYear, mMonth, mDay);

        }
    };

    @SuppressWarnings("deprecation")
    private void dismissProgressDialog() {
        dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
    }

    @SuppressWarnings("deprecation")
    private void showProgressDialog() {
        showDialog(DIALOG_DOWNLOAD_PROGRESS);
    }

    @SuppressWarnings("deprecation")
    private void showDateProgressDialog() {
        showDialog(DATE_DIALOG_ID);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();

        menu.setHeaderTitle("Services");

        int id = v.getId();
        if (id == R.id.listViewSBDueList) {
            inflater.inflate(R.menu.menu_sb_due_list, menu);
        }


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

        if (!item.toString().contentEquals("Policy Detail")
                && !item.toString().contentEquals("Customer Detail")) {
            if (item.getTitle().toString().contentEquals("Customer Details")) {
                int itemId = item.getItemId();
                if (itemId == R.id.sbduelist) {
                    String strSbdueListCustomerId = array_sort.get(info.position)
                            .getCustomerCode();
                    if (strSbdueListCustomerId != "" || strSbdueListCustomerId != null) {
                        Intent intent = new Intent(context,
                                CustomerDetailActivity.class);
                        intent.putExtra("CustomerId", strSbdueListCustomerId);
                        intent.putExtra("strUserType", "");
                        intent.putExtra("strAgentCode", strCIFBDMUserId);
                        intent.putExtra("strEmail", strCIFBDMEmailId);
                        intent.putExtra("strMobileNo", strCIFBDMMObileNo);
                        intent.putExtra("strPassword", strCIFBDMPassword.trim());
                        startActivity(intent);
                    }
                    return true;
                }
            }
        }

        return true;

    }


    class DownloadFileAsyncSBDueList extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        long lstSBDueListCount = 0;

        private String strSBDueListErrorCOde = "";

        String strfromdate = "",strtodate = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;

                //string strCifNo, string strEmailId, string strMobileNo, string strAuthKey, string strFromDate, string strToDate
                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE,
                        METHOD_NAME_SB_DUE_LIST);
                request.addProperty("strCifNo", strCIFBDMUserId);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());
                request.addProperty("strFromDate", strfromdate);
                request.addProperty("strToDate", strtodate);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    String SOAP_ACTION_SB_DUE_LIST = "http://tempuri.org/getSBDueList";
                    androidHttpTranport.call(SOAP_ACTION_SB_DUE_LIST,
                            envelope);

                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("anyType{}")) {

                        SoapPrimitive sa;
                        try {
                            sa = (SoapPrimitive) envelope.getResponse();

                            String inputpolicylist = sa.toString();

                            System.out.println("inputpolicylist:" + inputpolicylist);
                            ParseXML prsObj = new ParseXML();

                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "CIFPolicyList");
                            inputpolicylist = new ParseXML().parseXmlTag(
                                    inputpolicylist, "ScreenData");
                            strSBDueListErrorCOde = inputpolicylist;

                            if (strSBDueListErrorCOde == null) {
                                inputpolicylist = sa.toString();
                                inputpolicylist = prsObj.parseXmlTag(
                                        inputpolicylist, "CIFPolicyList");

                                List<String> Node = prsObj.parseParentNode(
                                        inputpolicylist, "Table");

                                List<XMLHolderSBDueList> nodeData = prsObj
                                        .parseNodeElementSBDueList(Node);

                                // final List<XMLHolderMaturity> lst;
                                lstSBDueLIst = new ArrayList<>();
                                lstSBDueLIst.clear();

                                lstSBDueLIst.addAll(nodeData);

                                lstSBDueListCount = lstSBDueLIst.size();
                                array_sort = new ArrayList<>(lstSBDueLIst);

                                selectedAdapterSbDueList = new SelectedAdapterSbDueList(
                                        context, lstSBDueLIst);
                                selectedAdapterSbDueList.setNotifyOnChange(true);

                                registerForContextMenu(listViewSBDueList);
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
                    }

                } catch (IOException | XmlPullParserException e) {
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
        protected void onProgressUpdate(String... progress) {
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }
        //570837 1234
        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                dismissProgressDialog();
            }
            if (running) {
				txtMaturityListNote.setVisibility(View.VISIBLE);
                if (strSBDueListErrorCOde == null) {
                    lnSBDueList.setVisibility(View.VISIBLE);
                    lnsearchSBDueList.setVisibility(View.VISIBLE);
                    listViewSBDueList.setVisibility(View.VISIBLE);

                    //String note = "<b>*Note:</b><i>SB Due Amount shown below is subject to payment of all due premiums. For Unit Linked products, the maturity value will be the Fund value on the Date of Maturity</i>";
                    txtMaturityListNote.setText("");

                    txterrordescSbDueList.setText("");
                    txtSbDueListlistcount.setText("Total Policy : "
                            + lstSBDueListCount);
                    listViewSBDueList.setAdapter(selectedAdapterSbDueList);

                    Utility.setListViewHeightBasedOnChildren(listViewSBDueList);

                    /*listViewSBDueList.getParent().requestChildFocus(
                            listViewSBDueList,
                            listViewSBDueList);*/
                } else if (lstSBDueListCount == 0) {

                    txtSbDueListlistcount.setText("Total Policy : "
                            + lstSBDueListCount);
                    lnSBDueList.setVisibility(View.VISIBLE);
                    lnsearchSBDueList.setVisibility(View.GONE);
                    listViewSBDueList.setVisibility(View.GONE);
                } else {
                    lnsearchSBDueList.setVisibility(View.GONE);
                    lnSBDueList.setVisibility(View.VISIBLE);
					txtMaturityListNote.setVisibility(View.GONE);
                    txterrordescSbDueList.setText("No Record Found");
                    txtSbDueListlistcount.setText("Total Policy : " + 0);
                    List<XMLHolderSBDueList> lst;
                    lst = new ArrayList<>();
                    lst.clear();
                    lst.add(null);

                    selectedAdapterSbDueList = new SelectedAdapterSbDueList(
                            context, lst);
                    selectedAdapterSbDueList.setNotifyOnChange(true);
                    listViewSBDueList.setAdapter(selectedAdapterSbDueList);

                    Utility.setListViewHeightBasedOnChildren(listViewSBDueList);
                }
            } else {
                //servererror();
                mCommonMethods.showMessageDialog(context, mCommonMethods.SERVER_ERROR);
            }
        }
    }


    class SelectedAdapterSbDueList extends ArrayAdapter<XMLHolderSBDueList> {

        // used to keep selected position in ListView
        //private int selectedPos = -1; // init value for not-selected

        final List<XMLHolderSBDueList> lst;

        SelectedAdapterSbDueList(Context context,
                                 List<XMLHolderSBDueList> objects) {
            super(context, 0, objects);

            lst = objects;

			if(lst.size()==0)
			{
				txtMaturityListNote.setVisibility(View.GONE);
			}
			else{
				txtMaturityListNote.setVisibility(View.VISIBLE);
			}
        }

		/*public void setSelectedPosition(int pos) {
            selectedPos = pos;
			// inform the view of this change
			notifyDataSetChanged();
		}

		public int getSelectedPosition() {
			return selectedPos;
		}*/

        @SuppressWarnings("deprecation")
        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            // only inflate the view if it's null
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) this.getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item_sb_due_list, null);
            }


            // get text view
            TextView textviewSBDueListPolicyNumber = v.findViewById(R.id.textviewSBDueListPolicyNumber);
            TextView textviewSBDueListCustomerCode = v.findViewById(R.id.textviewSBDueListCustomerCode);
            TextView txtSBDueListLifeAssuredName = v.findViewById(R.id.txtSBDueListLifeAssuredName);

            TextView txtSBDueListstatus = v.findViewById(R.id.txtSBDueListstatus);
            TextView txtSBDueListbenefitterm = v.findViewById(R.id.txtSBDueListbenefitterm);
            TextView txtSBDueListriskdate = v.findViewById(R.id.txtSBDueListriskdate);
            TextView txtSBDueListdate = v.findViewById(R.id.txtSBDueListdate);

            TextView txtSBDueListpayamt = v.findViewById(R.id.txtSBDueListpayamt);
            TextView textviewSBDueListFirstname = v.findViewById(R.id.textviewSBDueListFirstname);
            TextView textviewSBDueListDOB = v.findViewById(R.id.textviewSBDueListDOB);
            TextView textviewSBDueListCity = v.findViewById(R.id.textviewSBDueListCity);
            TextView textviewSBDueListAddress = v.findViewById(R.id.textviewSBDueListAddress);
            TextView textviewSBDueListPostalCode = v.findViewById(R.id.textviewSBDueListPostalCode);
            TextView textviewSBDueListState = v.findViewById(R.id.textviewSBDueListState);
            TextView textviewSBDueListContactNumber = v.findViewById(R.id.textviewSBDueListContactNumber);

            Object obj = null;
            boolean i = lst.contains(null);

            if (!i) {
				/*if (lst.get(position).getStatus().contentEquals("Inforce")) {
					txtSBDueListstatus.setBackgroundColor(Color.parseColor("#008000"));
				} else if (lst.get(position).getStatus().contentEquals("Lapse")) {
					txtSBDueListstatus.setBackgroundColor(Color.parseColor("#FFA500"));
				} else if (lst.get(position).getStatus()
						.contentEquals("Technical Lapse")) {
					txtSBDueListstatus.setBackgroundColor(Color.parseColor("#FFFF00"));
				} else if (lst.get(position).getStatus()
						.contentEquals("Maturity")) {
					txtSBDueListstatus.setBackgroundColor(Color.parseColor("#0000FF"));
				} else if (lst.get(position).getStatus().contentEquals("Claim")) {
					txtSBDueListstatus.setBackgroundColor(Color.parseColor("#FFC0CB"));
				} else if (lst.get(position).getStatus()
						.contentEquals("Surrender")) {
					txtSBDueListstatus.setBackgroundColor(Color.parseColor("#FF0000"));
				}*/


                txtSBDueListstatus.setText(lst.get(position).getStatus() == null ? "" : lst.get(
                        position).getStatus());
                textviewSBDueListPolicyNumber.setText(lst.get(position).getPolicyNumber() == null ? "" : lst.get(
                        position).getPolicyNumber());
                textviewSBDueListCustomerCode
                        .setText(lst.get(position).getCustomerCode() == null ? ""
                                : lst.get(position).getCustomerCode());

                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                int screenWidth = display.getWidth(); // Get full screen width

                int eightyPercent = (screenWidth * 50) / 100;

                String lifeAssuredName = lst.get(position).getLifeAssuredName() == null ? ""
                        : lst.get(position).getLifeAssuredName();

                txtSBDueListLifeAssuredName.setText(lifeAssuredName);

                float textWidthLifeAssuredName = textviewSBDueListAddress.getPaint().measureText(lifeAssuredName);

                float linesFloat = (textWidthLifeAssuredName / eightyPercent) + 0.7f;
                //System.out.println("linesFloat:"+linesFloat+":linesInt:"+linesInt);

                txtSBDueListLifeAssuredName.setLines(Math.round(linesFloat));

                txtSBDueListbenefitterm.setText(lst.get(position).getBenefitTerm() == null ? ""
                        : lst.get(position).getBenefitTerm());

                txtSBDueListriskdate.setText(lst.get(position).getPolicyRiskCommencementDate() == null ? ""
                        : lst.get(position).getPolicyRiskCommencementDate());

                //txtSBDueListdate.setBackgroundColor(Color.parseColor("#87CEFA"));
                txtSBDueListdate.setText(lst.get(position).getMaturityDate() == null ? ""
                        : lst.get(position).getMaturityDate());

                txtSBDueListpayamt.setText(lst.get(position).getPaymentAmount() == null ? ""
                        : lst.get(position).getPaymentAmount());
                textviewSBDueListFirstname.setText(lst.get(position).getFirstName() == null ? ""
                        : lst.get(position).getFirstName());
                textviewSBDueListFirstname.setLines(Math.round(linesFloat));

                textviewSBDueListDOB.setText(lst.get(position).getDOB() == null ? ""
                        : lst.get(position).getDOB());
                textviewSBDueListCity.setText(lst.get(position).getCity() == null ? ""
                        : lst.get(position).getCity());

                String address = lst.get(position).getAddress() == null ? "" : lst.get(position).getAddress();
                textviewSBDueListAddress.setText(address);

                // Calculate 80% of it
                // as my adapter was having almost 80% of the whole screen width
                float textWidthAddress = textviewSBDueListAddress.getPaint().measureText(address);
                // this method will give you the total width required to display total String
                int numberOfLinesAddress = Math.round((textWidthAddress / eightyPercent)) + 1;


                // calculate number of lines it might take
                textviewSBDueListAddress.setLines(numberOfLinesAddress);

                textviewSBDueListPostalCode.setText(lst.get(position).getPostalCode() == null ? ""
                        : lst.get(position).getPostalCode());
                textviewSBDueListState.setText(lst.get(position).getState() == null ? ""
                        : lst.get(position).getState());
                textviewSBDueListContactNumber.setText(lst.get(position).getState() == null ? ""
                        : lst.get(position).getContactNumber());
            }

            return (v);
        }
    }



    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.btn_click_SBDueList_maturitydate:
                searchByMaturityDate();
                break;
            case R.id.btn_click_SBDueList_status:
                searchByStatus();
                break;
            case R.id.btn_click_SBDueList_policyno:
                searchByPolicyNumber();
                break;
            case R.id.btn_click_SBDueList_fn:
                searchByFirstName();
                break;
            case R.id.imageButtonSBDueListMD:
                datecheck = 14;
                showDateProgressDialog();
                break;

            case R.id.buttonResetReportsSbDueList:
                editTextdt.setText("");
                editTextdtto.setText("");
                listViewSBDueList.setVisibility(View.GONE);
                txterrordescSbDueList.setVisibility(View.GONE);
                txtSbDueListlistcount.setVisibility(View.GONE);
                lnsearchSBDueList.setVisibility(View.GONE);
                txtMaturityListNote.setVisibility(View.GONE);

                array_sort = new ArrayList<>();
                array_sort.clear();

                if(selectedAdapterSbDueList!=null)
                {
                    selectedAdapterSbDueList.clear();
                }
                break;
            case R.id.buttonSubmitReportsSbDueList:
                String strfromdate = editTextdt.getText().toString();
                String strtodate = editTextdtto.getText().toString();

                if(!strfromdate.equals("")&& !strtodate.equals(""))
                {
                    submitSbDueList(strfromdate,strtodate);
                }
                else{
                    mCommonMethods.showMessageDialog(context, "All Fields Required..");
                }


                break;
            case R.id.btndate:
                datecheck = 1;
                showDateProgressDialog();
                break;

            case R.id.btnbtndateto:
                datecheck = 0;
                showDateProgressDialog();
                break;
        }

    }

    private void searchByMaturityDate() {
        if (edSearchSBDueListMD.getText().toString()
                .equalsIgnoreCase("")) {
            //duedatealert();
            mCommonMethods.showMessageDialog(context, mCommonMethods.DATE_ALERT);
        } else {
            mCommonMethods.hideKeyboard(edSearchSBDueListMD,context);

            array_sort = new ArrayList<>();

            array_sort.clear();
            String str = edSearchSBDueListMD.getText()
                    .toString().trim().toLowerCase();

            for (XMLHolderSBDueList node : lstSBDueLIst) {

                String maturityDate = node.getMaturityDate().toLowerCase().trim();
                if (maturityDate.contains(str)||maturityDate.startsWith(str)) {
                    array_sort.add(node);
                }
            }

            selectedAdapterSbDueList = new SelectedAdapterSbDueList(
                    context, array_sort);
            selectedAdapterSbDueList.setNotifyOnChange(true);
            listViewSBDueList.setAdapter(selectedAdapterSbDueList);
            Utility.setListViewHeightBasedOnChildren(listViewSBDueList);
        }
    }

    private void searchByStatus() {
        if (spinSearchSBDueListStatus.getSelectedItem().toString()
                .contentEquals("Select Status")) {
            //statusalert();
            mCommonMethods.showMessageDialog(context, mCommonMethods.STATUS_ALERT);
        } else {
            mCommonMethods.hideKeyboard(spinSearchSBDueListStatus,context);

            array_sort = new ArrayList<>();

            array_sort.clear();
            String str = spinSearchSBDueListStatus.getSelectedItem()
                    .toString().trim().toLowerCase();
            for (XMLHolderSBDueList node : lstSBDueLIst) {

                String status = node.getStatus().trim().toLowerCase();
                if (status.contains(str)||status.startsWith(str)) {
                    array_sort.add(node);
                }
            }

            selectedAdapterSbDueList = new SelectedAdapterSbDueList(
                    context, array_sort);
            selectedAdapterSbDueList.setNotifyOnChange(true);
            listViewSBDueList.setAdapter(selectedAdapterSbDueList);
            Utility.setListViewHeightBasedOnChildren(listViewSBDueList);
        }
    }

    private void searchByFirstName() {


        if (edSearchSBDueListFN.getText().toString()
                .equalsIgnoreCase("")) {
            //fnalert();
            mCommonMethods.showMessageDialog(context, mCommonMethods.FIRST_NAME_ALERT);
        } else {
            mCommonMethods.hideKeyboard(edSearchSBDueListFN,context);

            array_sort = new ArrayList<>();

            array_sort.clear();
            String str = edSearchSBDueListFN.getText().toString().trim().toLowerCase();
            for (XMLHolderSBDueList node : lstSBDueLIst) {

                String firstName = node.getFirstName().toLowerCase().trim();
                if (firstName.contains(str) || firstName.startsWith(str)) {
                    array_sort.add(node);
                }
            }

            selectedAdapterSbDueList = new SelectedAdapterSbDueList(
                    context, array_sort);
            selectedAdapterSbDueList.setNotifyOnChange(true);
            listViewSBDueList.setAdapter(selectedAdapterSbDueList);
            Utility.setListViewHeightBasedOnChildren(listViewSBDueList);
        }
    }

    private void searchByPolicyNumber() {
        if (edSearchSBDueListPN.getText().toString()
                .equalsIgnoreCase("")) {
            //
            //policynoalert();
            mCommonMethods.showMessageDialog(context, mCommonMethods.POLICY_NUMBER_ALERT);
        } else {
            mCommonMethods.hideKeyboard(edSearchSBDueListPN,context);

            array_sort = new ArrayList<>();

            array_sort.clear();
            String str = edSearchSBDueListPN.getText().toString().trim().toLowerCase();
            for (XMLHolderSBDueList node : lstSBDueLIst) {

                String policyNumber = node.getPolicyNumber().toLowerCase().trim();
                if (policyNumber.contains(str)||policyNumber.startsWith(str)) {
                    array_sort.add(node);
                }

            }

            selectedAdapterSbDueList = new SelectedAdapterSbDueList(
                    context, array_sort);
            selectedAdapterSbDueList.setNotifyOnChange(true);
            listViewSBDueList.setAdapter(selectedAdapterSbDueList);
            Utility.setListViewHeightBasedOnChildren(listViewSBDueList);
        }
    }

    private void submitSbDueList(String strfromdate,String strtodate) {
        final SimpleDateFormat formatter = new SimpleDateFormat(
                "dd-MMMM-yyyy");


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
            if (mCommonMethods.isNetworkConnected(context)) {
                edSearchSBDueListPN.setText("");
                edSearchSBDueListFN.setText("");
                spinSearchSBDueListStatus.setSelection(0);
                edSearchSBDueListMD.setText("");

                array_sort = new ArrayList<>();
                array_sort.clear();
                service_hits(strfromdate+","+strtodate);
            } else
                mCommonMethods.showMessageDialog(context,
                        mCommonMethods.NO_INTERNET_MESSAGE);
        } else {
            mCommonMethods.showMessageDialog(context,
                    "To date should be greater than From date");
        }

    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {

        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");
        switch (id) {
            case DATE_DIALOG_ID:

                switch (datecheck) {
                    case 14: {
                        String str = edSearchSBDueListMD.getText().toString();
                        if (!edSearchSBDueListMD.getText().toString().equalsIgnoreCase("")) {

                            final Calendar cal = Calendar.getInstance();
                            Date d1 = null;
                            try {
                                d1 = formatter.parse(str);
                                cal.setTime(d1);
                                mYear = cal.get(Calendar.YEAR);
                                mMonth = cal.get(Calendar.MONTH);
                                mDay = cal.get(Calendar.DAY_OF_MONTH);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    }
                    case 1: {
                        String str = editTextdt.getText().toString();
                        if (!editTextdt.getText().toString().equalsIgnoreCase("")) {

                            final Calendar cal = Calendar.getInstance();
                            Date d1 = null;
                            try {
                                d1 = formatter.parse(str);
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            cal.setTime(d1);

                            mYear = cal.get(Calendar.YEAR);
                            mMonth = cal.get(Calendar.MONTH);
                            mDay = cal.get(Calendar.DAY_OF_MONTH);
                        }

                        break;
                    }
                    default: {
                        String str = editTextdtto.getText().toString();

                        if (!editTextdtto.getText().toString().equalsIgnoreCase("")) {

                            final Calendar cal = Calendar.getInstance();
                            Date d1 = null;
                            try {
                                d1 = formatter.parse(str);
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            cal.setTime(d1);

                            mYear = cal.get(Calendar.YEAR);
                            mMonth = cal.get(Calendar.MONTH);
                            mDay = cal.get(Calendar.DAY_OF_MONTH);
                        }
                        break;
                    }
                }

                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
        }
    }
}
