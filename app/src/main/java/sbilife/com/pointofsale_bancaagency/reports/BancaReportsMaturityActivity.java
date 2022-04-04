package sbilife.com.pointofsale_bancaagency.reports;

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
import android.view.inputmethod.InputMethodManager;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.CustomerDetailActivity;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolderMaturity;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.Utility;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods.UserDetailsValuesModel;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits.DownLoadData;

public class BancaReportsMaturityActivity extends AppCompatActivity implements OnClickListener, DownLoadData {

    private Context mContext;
    private CommonMethods mCommonMethods;


    private final String METHOD_NAME_MATURITY_LIST = "getCIFPoliciesMaturityList";

    private final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private final int DATE_DIALOG_ID = 1;
    private int mYear, mMonth, mDay;
    private ProgressDialog mProgressDialog;

    private TextView txterrordescmaturity, txtmaturitylistcount;

    private DownloadFileAsyncMaturity taskMaturity;

    private EditText edSearchMaturityMD, edSearchMaturityPN, edSearchMaturityFN, editTextdtto, editTextdt;

    private ListView lvMaturityList;

    private String strCIFBDMUserId = "", strCIFBDMEmailId = "",
            strCIFBDMPassword = "", strCIFBDMMObileNo = "", strMaturityListErrorCOde = "";

    private long lstMaturityListCount = 0;

    private LinearLayout lnsearchmaturitylist;
    private Spinner spinSearchMaturityStatus;//, spinSearchMaturityPay_Status;


    private int datecheck = 0;

    private List<XMLHolderMaturity> lstMaturity;
    private SelectedAdapterMaturity selectedAdapterMaturity;
    private TextView txtMaturityListNote;

    private ArrayList<XMLHolderMaturity> array_sort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.banca_reports_maturity);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        new CommonMethods().setApplicationToolbarMenu(this, "Maturity");

        mProgressDialog = new ProgressDialog(this);
        //initialisation
        array_sort = new ArrayList<>();
        array_sort.clear();

        initialise();
        //mCommonMethods.setApplicationMenu(this,"Maturity") ;

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

    private void initialise() {

        //mActivity = this;
        mContext = this;
        mProgressDialog = new ProgressDialog(this);
        taskMaturity = new DownloadFileAsyncMaturity();
        mCommonMethods = new CommonMethods();

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

        txterrordescmaturity = findViewById(R.id.txterrordescmaturity);
        txterrordescmaturity.setVisibility(View.VISIBLE);
        txtmaturitylistcount = findViewById(R.id.txtmaturitylistcount);
        txtMaturityListNote = findViewById(R.id.txtMaturityListNote);

        txtmaturitylistcount.setVisibility(View.VISIBLE);
        lvMaturityList = findViewById(R.id.MaturitylistView1);
        lvMaturityList.setVisibility(View.VISIBLE);
        // for search
        lnsearchmaturitylist = findViewById(R.id.lnsearchmaturitylist);
        edSearchMaturityPN = findViewById(R.id.edSearchMaturityPN);
        edSearchMaturityFN = findViewById(R.id.edSearchMaturityFN);
        edSearchMaturityMD = findViewById(R.id.edSearchMaturityMD);

        ImageButton imageButtonSearchByMaturityDate = findViewById(R.id.imageButtonSearchByMaturityDate);

        editTextdtto = findViewById(R.id.editTextdtto);
        editTextdt = findViewById(R.id.editTextdt);

        spinSearchMaturityStatus = findViewById(R.id.spinSearchMaturityStatus);
        //spinSearchMaturityPay_Status = (Spinner) findViewById(R.id.spinSearchMaturityPay_Status);
        //btn_click_maturity_pay_status = (Button) findViewById(R.id.btn_click_maturity_pay_status);

        Button btn_click_maturity_policyno = findViewById(R.id.btn_click_maturity_policyno);
        Button btn_click_maturity_fn = findViewById(R.id.btn_click_maturity_fn);
        Button btn_click_maturity_status = findViewById(R.id.btn_click_maturity_status);
        Button btn_click_maturity_maturitydate = findViewById(R.id.btn_click_maturity_maturitydate);

        Button buttonSubmitReportsMaturity = findViewById(R.id.buttonSubmitReportsMaturity);
        Button buttonResetReportsMaturity = findViewById(R.id.buttonResetReportsMaturity);

        ImageButton btnbtndateto = findViewById(R.id.btnbtndateto);
        ImageButton btndate = findViewById(R.id.btndate);

        btn_click_maturity_policyno.setOnClickListener(this);
        btn_click_maturity_fn.setOnClickListener(this);
        btn_click_maturity_status.setOnClickListener(this);
        btn_click_maturity_maturitydate.setOnClickListener(this);
        buttonSubmitReportsMaturity.setOnClickListener(this);
        buttonResetReportsMaturity.setOnClickListener(this);

        imageButtonSearchByMaturityDate.setOnClickListener(this);

        btnbtndateto.setOnClickListener(this);
        btndate.setOnClickListener(this);
    }


    private void service_hits(String input ) {


        ServiceHits service = new ServiceHits(mContext,
                METHOD_NAME_MATURITY_LIST, input, strCIFBDMUserId,
                strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();
    }

    private void getUserDetails() {
        UserDetailsValuesModel userDetailsValuesModel = mCommonMethods.setUserDetails(mContext);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (taskMaturity != null) {
                taskMaturity.cancel(true);
            }
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }

            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void startDownloadMaturityList() {
        taskMaturity = new DownloadFileAsyncMaturity();
        taskMaturity.execute("demo");
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
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
                                if (taskMaturity != null) {
                                    taskMaturity.cancel(true);
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
            case 5:
                if (day < 10) {
                    da = "0" + da;
                    totaldate = da + "-" + m + "-" + y;
                }
                edSearchMaturityMD.setText(totaldate);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();

        menu.setHeaderTitle("Services");

        int id = v.getId();
        if (id == R.id.MaturitylistView1) {
            inflater.inflate(R.menu.menu_maturity, menu);
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
                if (itemId == R.id.matucust) {
                    String strmatuCustomerId = array_sort.get(info.position)
                            .getCustomerCode();
                    if (strmatuCustomerId != "" || strmatuCustomerId != null) {
                        Intent intent = new Intent(mContext,
                                CustomerDetailActivity.class);
                        intent.putExtra("CustomerId", strmatuCustomerId);
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

    class DownloadFileAsyncMaturity extends AsyncTask<String, String, String> {

        private volatile boolean running = true;

        String strfromdate = "", strtodate = "";

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
                SoapObject request;

                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE,
                        METHOD_NAME_MATURITY_LIST);
                request.addProperty("strCifNo", strCIFBDMUserId);


				/*getCIFPoliciesMaturityList(string strCifNo, string strEmailId, string strMobileNo, string strAuthKey,
						string strFromDate, string strToDate) */

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
                    String SOAP_ACTION_MATURITY_LIST = "http://tempuri.org/getCIFPoliciesMaturityList";
                    androidHttpTranport.call(SOAP_ACTION_MATURITY_LIST,
                            envelope);
                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("anyType{}")) {

                        SoapPrimitive sa;
                        try {
                            sa = (SoapPrimitive) envelope.getResponse();

                            String inputpolicylist = sa.toString();

                            ParseXML prsObj = new ParseXML();

                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "CIFPolicyList");
                            inputpolicylist = new ParseXML().parseXmlTag(
                                    inputpolicylist, "ScreenData");
                            strMaturityListErrorCOde = inputpolicylist;

                            if (strMaturityListErrorCOde == null) {
                                inputpolicylist = sa.toString();
                                inputpolicylist = prsObj.parseXmlTag(
                                        inputpolicylist, "CIFPolicyList");

                                List<String> Node = prsObj.parseParentNode(
                                        inputpolicylist, "Table");

                                List<XMLHolderMaturity> nodeData = prsObj
                                        .parseNodeElementMaturity(Node);

                                // final List<XMLHolderMaturity> lst;
                                lstMaturity = new ArrayList<>();
                                lstMaturity.clear();

                                lstMaturity.addAll(nodeData);

                                lstMaturityListCount = lstMaturity.size();
                                array_sort = new ArrayList<>(lstMaturity);

                                selectedAdapterMaturity = new SelectedAdapterMaturity(
                                        mContext, lstMaturity);
                                selectedAdapterMaturity.setNotifyOnChange(true);

                                registerForContextMenu(lvMaturityList);


                            }

                        } catch (Exception e) {

                            mProgressDialog.dismiss();
                            running = false;
                        }
                    }

                } catch (IOException e) {
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
                closeProgressDialog();
            }
            lvMaturityList.setVisibility(View.GONE);
            if (running) {

                txterrordescmaturity.setVisibility(View.VISIBLE);
                txtmaturitylistcount.setVisibility(View.VISIBLE);
                txtMaturityListNote.setVisibility(View.VISIBLE);
                if (strMaturityListErrorCOde == null) {
                    lnsearchmaturitylist.setVisibility(View.VISIBLE);
                    lvMaturityList.setVisibility(View.VISIBLE);

                    String note = "<b>*Note:</b><i>Maturity Amount shown below is subject to payment of all due premiums. For Unit Linked products, the maturity value will be the Fund value on the Date of Maturity</i>";
                    txtMaturityListNote.setText(Html.fromHtml(note));
                    txterrordescmaturity.setText("");
                    txtmaturitylistcount.setText("Total Policy : "
                            + lstMaturityListCount);
                    lvMaturityList.setAdapter(selectedAdapterMaturity);

                    Utility.setListViewHeightBasedOnChildren(lvMaturityList);


                } else {
                    lnsearchmaturitylist.setVisibility(View.GONE);
                    txtMaturityListNote.setVisibility(View.GONE);
                    txterrordescmaturity.setText("No Record Found");
                    txtmaturitylistcount.setText("Total Policy : " + 0);
                    List<XMLHolderMaturity> lst;
                    lst = new ArrayList<>();
                    lst.clear();
                    lst.add(null);
                    selectedAdapterMaturity = new SelectedAdapterMaturity(
                            mContext, lst);
                    selectedAdapterMaturity.setNotifyOnChange(true);
                    lvMaturityList.setAdapter(selectedAdapterMaturity);

                    Utility.setListViewHeightBasedOnChildren(lvMaturityList);

                }
            } else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.SERVER_ERROR);
            }
        }
    }

    class SelectedAdapterMaturity extends ArrayAdapter<XMLHolderMaturity> {

        // used to keep selected position in ListView
        final List<XMLHolderMaturity> lst;

        SelectedAdapterMaturity(Context context,
                                List<XMLHolderMaturity> objects) {
            super(context, 0, objects);

            lst = objects;

            if (lst.size() == 0) {
                txtMaturityListNote.setVisibility(View.GONE);
            } else {
                txtMaturityListNote.setVisibility(View.VISIBLE);
            }
        }

        @SuppressWarnings("deprecation")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            // only inflate the view if it's null
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) this.getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item_maturitylist, null);
            }


            // get text view
            TextView textviewMaturityPolicyNumber = v.findViewById(R.id.textviewMaturityPolicyNumber);
            TextView textviewMaturityCustomerCode = v.findViewById(R.id.textviewMaturityCustomerCode);
            TextView txtmaturityLifeAssuredName = v.findViewById(R.id.txtmaturityLifeAssuredName);

            TextView txtmaturitystatus = v.findViewById(R.id.txtmaturitystatus);
            TextView txtmaturitybenefitterm = v.findViewById(R.id.txtmaturitybenefitterm);
            TextView txtmaturityriskdate = v.findViewById(R.id.txtmaturityriskdate);
            TextView txtmaturitydate = v.findViewById(R.id.txtmaturitydate);

            TextView txtpayamt = v.findViewById(R.id.txtpayamt);
            TextView textviewFirstname = v.findViewById(R.id.textviewFirstname);
            TextView textviewMaturityDOB = v.findViewById(R.id.textviewMaturityDOB);
            TextView textviewMaturityCity = v.findViewById(R.id.textviewMaturityCity);
            TextView textviewMaturityAddress = v.findViewById(R.id.textviewMaturityAddress);
            TextView textviewMaturityPostalCode = v.findViewById(R.id.textviewMaturityPostalCode);
            TextView textviewMaturityState = v.findViewById(R.id.textviewMaturityState);
            TextView textviewMaturityContactNumber = v.findViewById(R.id.textviewMaturityContactNumber);

            boolean i = lst.contains(null);

            if (!i) {
				/*if (lst.get(position).getStatus().contentEquals("Inforce")) {
					txtmaturitystatus.setBackgroundColor(Color.parseColor("#008000"));
				} else if (lst.get(position).getStatus().contentEquals("Lapse")) {
					txtmaturitystatus.setBackgroundColor(Color.parseColor("#FFA500"));
				} else if (lst.get(position).getStatus()
						.contentEquals("Technical Lapse")) {
					txtmaturitystatus.setBackgroundColor(Color.parseColor("#FFFF00"));
				} else if (lst.get(position).getStatus()
						.contentEquals("Maturity")) {
					txtmaturitystatus.setBackgroundColor(Color.parseColor("#0000FF"));
				} else if (lst.get(position).getStatus().contentEquals("Claim")) {
					txtmaturitystatus.setBackgroundColor(Color.parseColor("#FFC0CB"));
				} else if (lst.get(position).getStatus()
						.contentEquals("Surrender")) {
					txtmaturitystatus.setBackgroundColor(Color.parseColor("#FF0000"));
				}*/

                //txtmaturitystatus.setBackgroundColor(Color.parseColor("#87CEFA"));//#add8e6
                txtmaturitystatus.setText(lst.get(position).getStatus() == null ? "" : lst.get(
                        position).getStatus());

                textviewMaturityPolicyNumber.setText(lst.get(position).getPolicyNumber() == null ? "" : lst.get(
                        position).getPolicyNumber());
                textviewMaturityCustomerCode
                        .setText(lst.get(position).getCustomerCode() == null ? ""
                                : lst.get(position).getCustomerCode());

				 /*txtmaturityLifeAssuredName.setText(lst.get(position).getLifeAssuredName() == null ? ""
							: lst.get(position).getLifeAssuredName());*/

                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                int screenWidth = display.getWidth(); // Get full screen width

                int eightyPercent = (screenWidth * 50) / 100;

                String lifeAssuredName = lst.get(position).getLifeAssuredName() == null ? ""
                        : lst.get(position).getLifeAssuredName();

                txtmaturityLifeAssuredName.setText(lifeAssuredName);

                float textWidthLifeAssuredName = txtmaturityLifeAssuredName.getPaint().measureText(lifeAssuredName);

                float linesFloat = (textWidthLifeAssuredName / eightyPercent) + 0.7f;

                txtmaturityLifeAssuredName.setLines(Math.round(linesFloat));

                txtmaturitybenefitterm.setText(lst.get(position).getBenefitTerm() == null ? ""
                        : lst.get(position).getBenefitTerm());

                txtmaturityriskdate.setText(lst.get(position).getPolicyRiskCommencementDate() == null ? ""
                        : lst.get(position).getPolicyRiskCommencementDate());

                //txtmaturitydate.setBackgroundColor(Color.parseColor("#87CEFA"));
                txtmaturitydate.setText(lst.get(position).getMaturityDate() == null ? ""
                        : lst.get(position).getMaturityDate());

                String paymentAmount = lst.get(position).getPaymentAmount() == null ? "0" : lst.get(position).getPaymentAmount();

                double paymentAmountDouble = Double.valueOf(paymentAmount);
                long roundedPaymentAmt = Math.round(paymentAmountDouble);

                System.out.println("paymentAmount:" + paymentAmount + " roundedPaymentAmt:" + roundedPaymentAmt);

                txtpayamt.setText(roundedPaymentAmt + "");
                textviewFirstname.setText(lst.get(position).getFirstName() == null ? ""
                        : lst.get(position).getFirstName());

                textviewFirstname.setLines(Math.round(linesFloat));

                textviewMaturityDOB.setText(lst.get(position).getDOB() == null ? ""
                        : lst.get(position).getDOB());
                textviewMaturityCity.setText(lst.get(position).getCity() == null ? ""
                        : lst.get(position).getCity());

				 /*textviewMaturityAddress.setText(lst.get(position).getAddress() == null ? ""
							: lst.get(position).getAddress());*/

                String address = lst.get(position).getAddress() == null ? "" : lst.get(position).getAddress();
                textviewMaturityAddress.setText(address);

                // Calculate 80% of it
                // as my adapter was having almost 80% of the whole screen width
                float textWidthAddress = textviewMaturityAddress.getPaint().measureText(address);
                // this method will give you the total width required to display total String
                int numberOfLinesAddress = Math.round((textWidthAddress / eightyPercent)) + 1;

                // calculate number of lines it might take
                textviewMaturityAddress.setLines(numberOfLinesAddress);

                textviewMaturityPostalCode.setText(lst.get(position).getPostalCode() == null ? ""
                        : lst.get(position).getPostalCode());
                textviewMaturityState.setText(lst.get(position).getState() == null ? ""
                        : lst.get(position).getState());
                textviewMaturityContactNumber.setText(lst.get(position).getContactNumber() == null ? ""
                        : lst.get(position).getContactNumber());
            }

            return (v);
        }
    }

    @SuppressWarnings("deprecation")
    private void showProgressDialog() {
        showDialog(DIALOG_DOWNLOAD_PROGRESS);
    }

    @SuppressWarnings("deprecation")
    private void closeProgressDialog() {
        dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
    }

    @SuppressWarnings("deprecation")
    private void showDateProgressDialog() {
        showDialog(DATE_DIALOG_ID);
    }

    @Override
    public void downLoadData() {
        taskMaturity = new DownloadFileAsyncMaturity();
        startDownloadMaturityList();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();


        switch (id) {
            case R.id.btn_click_maturity_policyno:
                searchByPolicyNumber();
                break;
            case R.id.btn_click_maturity_fn:
                searchByFirstName();
                break;
            case R.id.btn_click_maturity_status:
                searchByStatus();

                break;
            case R.id.btn_click_maturity_maturitydate:
                searchByMaturityDate();
                break;

            case R.id.buttonResetReportsMaturity:
                editTextdt.setText("");
                editTextdtto.setText("");
                lvMaturityList.setVisibility(View.GONE);
                txterrordescmaturity.setVisibility(View.GONE);
                txtmaturitylistcount.setVisibility(View.GONE);
                lnsearchmaturitylist.setVisibility(View.GONE);
                txtMaturityListNote.setVisibility(View.GONE);

                array_sort = new ArrayList<>();
                array_sort.clear();

                if (selectedAdapterMaturity != null) {
                    selectedAdapterMaturity.clear();
                }
                break;
            case R.id.buttonSubmitReportsMaturity:
                String strfromdate = editTextdt.getText().toString();
                String strtodate = editTextdtto.getText().toString();

                if (!strfromdate.equals("") && !strtodate.equals("")) {
                    submitMaturity(strfromdate, strtodate);
                } else {
                    mCommonMethods.showMessageDialog(mContext, "All Fields Required..");
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

            case R.id.imageButtonSearchByMaturityDate:
                datecheck = 5;
                showDateProgressDialog();
                break;
        }
    }

    private void submitMaturity(String strfromdate, String strtodate) {
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
            if (mCommonMethods.isNetworkConnected(mContext)) {
                edSearchMaturityPN.setText("");
                edSearchMaturityFN.setText("");
                spinSearchMaturityStatus.setSelection(0);
                edSearchMaturityMD.setText("");

                array_sort = new ArrayList<>();
                array_sort.clear();
                service_hits(strfromdate + "," +strtodate);
            } else
                mCommonMethods.showMessageDialog(mContext,
                        mCommonMethods.NO_INTERNET_MESSAGE);
        } else {
            mCommonMethods.showMessageDialog(mContext,
                    "To date should be greater than From date");
        }

    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {

        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");
        switch (id) {
            case DATE_DIALOG_ID:

                if (datecheck == 1) {
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

                } else {
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
                }

                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
        }
    }

    private void searchByMaturityDate() {

        if (edSearchMaturityMD.getText().toString()
                .equalsIgnoreCase("")) {
            //duedatealert();
            mCommonMethods.showMessageDialog(mContext, mCommonMethods.DATE_ALERT);
        } else {


            array_sort = new ArrayList<>();

            array_sort.clear();
            String str = edSearchMaturityMD.getText()
                    .toString().trim().toLowerCase();
            for (XMLHolderMaturity node : lstMaturity) {

                String maturityDate = node.getMaturityDate().toLowerCase();
                if (maturityDate.contains(str) || maturityDate.startsWith(str)) {
                    array_sort.add(node);
                }
            }

            selectedAdapterMaturity = new SelectedAdapterMaturity(
                    mContext, array_sort);
            selectedAdapterMaturity.setNotifyOnChange(true);
            lvMaturityList
                    .setAdapter(selectedAdapterMaturity);
            Utility.setListViewHeightBasedOnChildren(lvMaturityList);
        }
    }

    private void searchByStatus() {

        if (spinSearchMaturityStatus.getSelectedItem().toString()
                .contentEquals("Select Status")) {
            //statusalert();
            mCommonMethods.showMessageDialog(mContext, mCommonMethods.STATUS_ALERT);
        } else {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

            array_sort = new ArrayList<>();

            array_sort.clear();
            String str = spinSearchMaturityStatus.getSelectedItem()
                    .toString().trim().toLowerCase();
            for (XMLHolderMaturity node : lstMaturity) {

                String status = node.getStatus().toLowerCase();
                if (status.contains(str) || status.startsWith(str)) {
                    array_sort.add(node);
                }
            }

            selectedAdapterMaturity = new SelectedAdapterMaturity(
                    mContext, array_sort);
            selectedAdapterMaturity.setNotifyOnChange(true);
            lvMaturityList.setAdapter(selectedAdapterMaturity);
            Utility.setListViewHeightBasedOnChildren(lvMaturityList);
        }
    }

    private void searchByFirstName() {
        if (edSearchMaturityFN.getText().toString()
                .equalsIgnoreCase("")) {
            //fnalert();
            mCommonMethods.showMessageDialog(mContext, mCommonMethods.FIRST_NAME_ALERT);
        } else {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

            array_sort = new ArrayList<>();

            array_sort.clear();
            String str = edSearchMaturityFN.getText().toString().trim().toLowerCase();
            for (XMLHolderMaturity node : lstMaturity) {
                String firstName = node.getFirstName().toLowerCase().trim();
                if (firstName.contains(str) || firstName.startsWith(str)) {
                    array_sort.add(node);
                }
            }

            selectedAdapterMaturity = new SelectedAdapterMaturity(
                    mContext, array_sort);
            selectedAdapterMaturity.setNotifyOnChange(true);
            lvMaturityList.setAdapter(selectedAdapterMaturity);
            Utility.setListViewHeightBasedOnChildren(lvMaturityList);
        }
    }

    private void searchByPolicyNumber() {

        if (edSearchMaturityPN.getText().toString()
                .equalsIgnoreCase("")) {
            //policynoalert();
            mCommonMethods.showMessageDialog(mContext, mCommonMethods.POLICY_NUMBER_ALERT);
        } else {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

            array_sort = new ArrayList<>();

            array_sort.clear();
            String str = edSearchMaturityPN.getText().toString().trim().toLowerCase();
            for (XMLHolderMaturity node : lstMaturity) {

                String policyNumber = node.getPolicyNumber();
                if (policyNumber.contains(str) || policyNumber.startsWith(str)) {
                    array_sort.add(node);
                }
            }

            selectedAdapterMaturity = new SelectedAdapterMaturity(
                    mContext, array_sort);
            selectedAdapterMaturity.setNotifyOnChange(true);
            lvMaturityList.setAdapter(selectedAdapterMaturity);
            Utility.setListViewHeightBasedOnChildren(lvMaturityList);
        }
    }
}
