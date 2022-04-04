package sbilife.com.pointofsale_bancaagency.reports;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
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
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolder;
import sbilife.com.pointofsale_bancaagency.PolicyDetailActivity;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.Utility;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.ekyc.EkycPSClaimsActivity;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods.UserDetailsValuesModel;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits.DownLoadData;

@SuppressLint("SimpleDateFormat")
public class BancaReportsPolicyListActivity extends AppCompatActivity implements
        OnClickListener, DownLoadData {

    private final String METHOD_NAME_POLICY_LIST = "getCIFPoliciesList";

    private final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    private DownloadFileAsync taskPolicyList;

    private int mYear, mMonth, mDay, datecheck = 0;
    private final int DATE_DIALOG_ID = 1;

    private String strfromdate = "", strtodate = "";// ,y,m;

    private SelectedAdapter selectedAdapter;
    private List<XMLHolder> lstPolicyList;
    private ArrayList<String> lst_cif_product_name;
    private ArrayList<String> lst_policy_status;

    private long lstPolicyListCount = 0;

    private String strCIFBDMUserId = "", strCIFBDMEmailId = "",
            strCIFBDMPassword = "", strCIFBDMMObileNo = "", chflagname,
            strPolicyListErrorCOde = "";
    private Context context;
    private CommonMethods mCommonMethods;
    private ListView lvpolicylist;
    // for search in policy list
    private EditText edSearchPolicyListPN, edSearchPolicyListFN,
            edSearchPolicyListDD, editTextdt, editTextdtto;
    private Spinner spinSearchPolicyListStatus, spinSearchPolicyListPNM,
            chflag;

    // policy list
    private TextView txtpolicylistcount, txterrordesc;
    private ImageButton btndate, btnbtndateto;
    // for search
    private LinearLayout lnsearchpolicylist;

    private ArrayList<XMLHolder> array_sort;
    //private ArrayList<String> lstLinkedEkycPolicies = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.banca_reports_policy_list);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        context = this;
        mCommonMethods = new CommonMethods();
        mCommonMethods.setApplicationToolbarMenu(this, "Policy List");

        mProgressDialog = new ProgressDialog(context);


        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        mProgressDialog = new ProgressDialog(this);

        //get eKyc linked policies
        //lstLinkedEkycPolicies = db.getLinkedEkycPolicies(1);

        taskPolicyList = new DownloadFileAsync();

        lnsearchpolicylist = findViewById(R.id.lnsearchpolicylist);
        lvpolicylist = findViewById(R.id.listView1);

        txtpolicylistcount = findViewById(R.id.txtpolicylistcount);
        txterrordesc = findViewById(R.id.txterrordesc);

        editTextdt = findViewById(R.id.editTextdt);
        editTextdtto = findViewById(R.id.editTextdtto);
        edSearchPolicyListPN = findViewById(R.id.edSearchPolicyListPN);
        edSearchPolicyListFN = findViewById(R.id.edSearchPolicyListFN);
        edSearchPolicyListDD = findViewById(R.id.edSearchPolicyListDD);

        btndate = findViewById(R.id.btndate);
        btnbtndateto = findViewById(R.id.btnbtndateto);
        ImageButton imageButtonPolicySearchByDate = findViewById(R.id.imageButtonPolicySearchByDate);

        Button btn_savepolicylist = findViewById(R.id.btn_savepolicylist);
        Button btn_click_policylist_policyno = findViewById(R.id.btn_click_policylist_policyno);
        Button btn_click_policylist_fn = findViewById(R.id.btn_click_policylist_fn);
        Button btn_click_policylist_status = findViewById(R.id.btn_click_policylist_status);
        Button btn_click_policylist_duedate = findViewById(R.id.btn_click_policylist_duedate);
        Button btn_click_policylist_productname = findViewById(R.id.btn_click_policylist_productname);
        Button btn_reset_policylist = findViewById(R.id.btn_reset_policylist);

        spinSearchPolicyListStatus = findViewById(R.id.spinSearchPolicyListStatus);
        spinSearchPolicyListPNM = findViewById(R.id.spinSearchPolicyListPNM);

        imageButtonPolicySearchByDate.setOnClickListener(this);
        btn_savepolicylist.setOnClickListener(this);
        btn_click_policylist_policyno.setOnClickListener(this);
        btn_click_policylist_fn.setOnClickListener(this);
        btn_click_policylist_status.setOnClickListener(this);
        btn_click_policylist_duedate.setOnClickListener(this);
        btn_click_policylist_productname.setOnClickListener(this);
        btn_reset_policylist.setOnClickListener(this);

        onTouchListeners();
        setSpinners();
        setDates();
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
    }

    private void getUserDetails() {
        UserDetailsValuesModel userDetailsValuesModel = mCommonMethods
                .setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }

    private void setSpinners() {
        chflag = findViewById(R.id.chflag);

        chflag.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {
                chflagname = chflag.getSelectedItem().toString();

                if (chflagname.contentEquals("All")) {
                    chflagname = "A";
                } else if (chflagname.contentEquals("Ulip")) {
                    chflagname = "U";
                } else if (chflagname.contentEquals("Traditional")) {
                    chflagname = "T";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        chflag.setSelection(1);
    }

    private void setDates() {
        Calendar cal = Calendar.getInstance();
        int mDay = cal.get(Calendar.DAY_OF_MONTH);
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        String y1 = String.valueOf(mYear - 1);

        String y = String.valueOf(mYear);
        String m = String.valueOf(mMonth + 1);
        String da = String.valueOf(mDay);

        m = mCommonMethods.getFullMonthName(m);
        String totaldate = da + "-" + m + "-" + y;
        String fromdate = 01 + "-" + "April" + "-" + y1;
        editTextdt.setText(fromdate);
        editTextdtto.setText(totaldate);

    }

    @SuppressLint({"SimpleDateFormat", "ClickableViewAccessibility"})
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
        String da = String.valueOf(day);

        m = mCommonMethods.getFullMonthName(m);

        String totaldate = da + "-" + m + "-" + y;

        switch (datecheck) {
            case 1:
                editTextdt.setText(totaldate);
                break;
            case 2:
                edSearchPolicyListDD.setText(totaldate);
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
    @Override
    protected Dialog onCreateDialog(int id) {
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

                                if (taskPolicyList != null) {
                                    taskPolicyList.cancel(true);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        // MenuInflater inflater = getMenuInflater();

        menu.setHeaderTitle("Services");

        int id = v.getId();
        if (id == R.id.listView1) {
            // menu.add(0,v.getId(),0,"Policy Detail");
            /*if (UserType.contentEquals("CIF")||UserType.contentEquals("BDM")) {
                menu.add(0, v.getId(), 0, "Policy Detail");
                menu.add(0, v.getId(), 1, "Link To Aadhar");
            } else {
                menu.add(0, v.getId(), 0, "Policy Detail");
                menu.add(0, v.getId(), 1, "Customer Detail");
                menu.add(0, v.getId(), 2, "Link To Aadhar");
            }*/

            menu.add(0, v.getId(), 0, "Policy Detail");
            menu.add(0, v.getId(), 1, "Customer Detail");
            //menu.add(0, v.getId(), 2, "Link To Aadhar");
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        String User_Type = mCommonMethods.GetUserType(context);

        if (User_Type.contentEquals("CIF") || User_Type.contentEquals("BDM")) {

            if (item.getTitle() == "Policy Detail") {

                String strPolicyNo = array_sort.get(info.position)
                        .getNo();

                if (strPolicyNo != "" || strPolicyNo != null) {
                    Intent intent = new Intent(context,
                            PolicyDetailActivity.class);
                    intent.putExtra("PolicyNo", strPolicyNo);
                    intent.putExtra("strUserType", "CIF");
                    intent.putExtra("strUserId", strCIFBDMUserId);
                    startActivity(intent);
                }
            } else if (item.getTitle() == "Link To Aadhar") {

                String strPolicyNo = array_sort.get(info.position)
                        .getNo();
                if (strPolicyNo != "" || strPolicyNo != null) {
                    //call PS Claims Activity
                    Intent intent = new Intent(BancaReportsPolicyListActivity.this, EkycPSClaimsActivity.class);
                    intent.putExtra("POLICY_NUM", strPolicyNo);
                    startActivity(intent);
                }

            } else {
                String strCustomerId = array_sort
                        .get(info.position).getHolderId();

                if (strCustomerId != "" || strCustomerId != null) {
                    Intent intent = new Intent(context,
                            CustomerDetailActivity.class);
                    intent.putExtra("CustomerId", strCustomerId);
                    intent.putExtra("strUserType", "CIF");
                    intent.putExtra("strAgentCode", strCIFBDMUserId);
                    intent.putExtra("strEmail", strCIFBDMEmailId);
                    intent.putExtra("strMobileNo", strCIFBDMMObileNo);
                    intent.putExtra("strPassword", strCIFBDMPassword.trim());
                    startActivity(intent);
                }
            }
        }

        return true;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (taskPolicyList != null) {
                taskPolicyList.cancel(true);
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

    private void startDownloadPolicyList() {
        taskPolicyList = new DownloadFileAsync();
        taskPolicyList.execute("demo");
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

        String str = strfromdate +
                "," +
                strtodate +
                "," +
                chflagname;


        ServiceHits service = new ServiceHits(context, strServiceName, str,
                strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();
    }

    /*
     * public class ServiceHits extends AsyncTask<String, Void, String> { //
     * Context mContext; ProgressDialog progressDialog = null; String NAMESPACE
     * = ""; String URL = ""; String SOAP_ACTION = ""; String METHOD_NAME = "";
     * String strServiceName = "";
     *
     * public ServiceHits(Context mContext, ProgressDialog progressDialog,
     * String NAMESPACE, String URL, String SOAP_ACTION, String METHOD_NAME,
     * String strServiceName) { this.NAMESPACE = NAMESPACE; this.URL = URL;
     * this.SOAP_ACTION = SOAP_ACTION; this.METHOD_NAME = METHOD_NAME; //
     * this.mContext = mContext; this.strServiceName = strServiceName;
     * this.progressDialog = progressDialog; }
     *
     * @SuppressLint("SimpleDateFormat")
     *
     * @Override protected String doInBackground(String... param) {
     *
     * if (mCommonMethods.isNetworkConnected(context)) { try { SoapObject request
     * = new SoapObject(NAMESPACE, METHOD_NAME);
     * request.addProperty("serviceName", strServiceName);
     * request.addProperty("strProdCode", "");
     *
     * if (strServiceName.contentEquals(METHOD_NAME_POLICY_LIST) ||
     * strServiceName .contentEquals(METHOD_NAME_POLICY_LIST_AGENCY)) {
     * strfromdate = editTextdt.getText().toString(); strtodate =
     * editTextdtto.getText().toString();
     *
     * final SimpleDateFormat formatter = new SimpleDateFormat( "dd-MMMM-yyyy");
     * final SimpleDateFormat formatter1 = new SimpleDateFormat( "MM-dd-yyyy");
     * try { Date dt = formatter.parse(strfromdate); strfromdate =
     * formatter1.format(dt);
     *
     * Date dt1 = formatter.parse(strtodate); strtodate =
     * formatter1.format(dt1);
     *
     * } catch (ParseException e) { e.printStackTrace(); }
     *
     * StringBuilder str = new StringBuilder(); str.append(strfromdate);
     * str.append(","); str.append(strtodate); str.append(",");
     * str.append(chflagname);
     *
     * request.addProperty("serviceInput", str.toString()); } else {
     * request.addProperty("serviceInput", ""); }
     *
     * request.addProperty("serviceReqUserId", strCIFBDMUserId);
     * request.addProperty("strEmailId", strCIFBDMEmailId);
     * request.addProperty("strMobileNo", strCIFBDMMObileNo);
     * request.addProperty("strAuthKey", strCIFBDMPassword.trim());
     *
     * SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
     * SoapEnvelope.VER11); // Enable this envelope if service is written in dot
     * net envelope.dotNet = true; envelope.setOutputSoapObject(request);
     * HttpTransportSE androidHttpTransport = new HttpTransportSE( URL);
     *
     * // allowAllSSL();
     *
     * StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
     * .permitAll().build(); StrictMode.setThreadPolicy(policy);
     *
     * androidHttpTransport.call(SOAP_ACTION, envelope); SoapPrimitive response
     * = (SoapPrimitive) envelope .getResponse();
     *
     * String result = response.toString();
     *
     * if (result.contains("1")) { return "Success"; } else { return "Failure";
     * }
     *
     * } catch (Exception e) { return
     * "Server not Found. Please try after some time."; }
     *
     * } else return "Please Activate Internet connection";
     *
     * }
     *
     * @Override protected void onPostExecute(String result) {
     * super.onPostExecute(result); try { if (progressDialog.isShowing())
     * progressDialog.dismiss();
     *
     * } catch (Exception e) { e.getMessage(); }
     *
     * taskPolicyList = new DownloadFileAsync(); if
     * (strServiceName.contentEquals(METHOD_NAME_POLICY_LIST) || strServiceName
     * .contentEquals(METHOD_NAME_POLICY_LIST_AGENCY)) {
     * startDownloadPolicyList(); } } }
     */
    @Override
    public void downLoadData() {
        WeakReference<BancaReportsPolicyListActivity> mainActivityWeakRef = new WeakReference<>(BancaReportsPolicyListActivity.this);

        if (mainActivityWeakRef.get() != null && !mainActivityWeakRef.get().isFinishing()) {
            startDownloadPolicyList();
        }
    }

    class DownloadFileAsync extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        String editTextdt_text, editTextdtto_text;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
            editTextdt_text = editTextdt.getText().toString();
            editTextdtto_text = editTextdtto.getText().toString();
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;
                String UserType = mCommonMethods.GetUserType(context);

                strfromdate = editTextdt_text;
                strtodate = editTextdtto_text;

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

                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME_POLICY_LIST);
                request.addProperty("strCifNo", strCIFBDMUserId);

                request.addProperty("strfromDt", strfromdate);
                request.addProperty("strToDt", strtodate);
                request.addProperty("polType", chflagname);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // allowAllSSL();
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {

                    String SOAP_ACTION_POLICY_LIST = "http://tempuri.org/getCIFPoliciesList";
                    androidHttpTranport.call(SOAP_ACTION_POLICY_LIST, envelope);
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
                            strPolicyListErrorCOde = inputpolicylist;

                            if (strPolicyListErrorCOde == null) {
                                if (UserType.contentEquals("MAN")
                                        || UserType.contentEquals("BDM")
                                        || UserType.contentEquals("TBDM")
                                        || UserType.contentEquals("CIF")) {

                                    inputpolicylist = sa.toString();
                                    inputpolicylist = prsObj.parseXmlTag(
                                            inputpolicylist, "CIFPolicyList");

                                    List<String> Node = prsObj.parseParentNode(
                                            inputpolicylist, "Table");

                                    List<XMLHolder> nodeData = prsObj
                                            .parseNodeElement(Node);

                                    // final List<XMLHolder> lstPolicyList;
                                    lstPolicyList = new ArrayList<>();
                                    lstPolicyList.clear();

                                    lst_cif_product_name = new ArrayList<>();
                                    lst_cif_product_name.clear();

                                    lst_policy_status = new ArrayList<>();
                                    lst_policy_status.clear();
                                    int i = 0,j=0;

                                    for (XMLHolder node : nodeData) {

                                        lstPolicyList.add(node);

                                        if (node.getProductName() != null
                                                || node.getProductName() != "") {
                                            if (i == 0) {
                                                lst_cif_product_name
                                                        .add("Select Product");
                                                i = 1;
                                            }

                                            if (!lst_cif_product_name
                                                    .contains(node
                                                            .getProductName())) {
                                                lst_cif_product_name.add(node
                                                        .getProductName());
                                            }
                                        }

                                        if (!TextUtils.isEmpty(node.getStatus())) {
                                            if (j == 0) {
                                                lst_policy_status.add("Select Status");
                                                j = 1;
                                            }

                                            if (!lst_policy_status.contains(node
                                                    .getStatus())) {
                                                lst_policy_status.add(node
                                                        .getStatus());
                                            }
                                        }
                                    }

                                    lstPolicyListCount = lstPolicyList.size();
                                    array_sort = new ArrayList<>(lstPolicyList);

                                    selectedAdapter = new SelectedAdapter(
                                            context, lstPolicyList);
                                    selectedAdapter.setNotifyOnChange(true);

                                    registerForContextMenu(lvpolicylist);

                                }
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
                    mProgressDialog.dismiss();
                    running = false;
                }
            } catch (Exception e) {
                mProgressDialog.dismiss();
                running = false;
            }
            return null;

        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                dismissProgressDialog();
            }
            if (running) {
                txterrordesc.setVisibility(View.VISIBLE);
                txtpolicylistcount.setVisibility(View.VISIBLE);
                if (strPolicyListErrorCOde == null) {

                    lnsearchpolicylist.setVisibility(View.VISIBLE);
                    lvpolicylist.setVisibility(View.VISIBLE);

                    txterrordesc.setText("");
                    txtpolicylistcount.setText("Total Policy : "
                            + lstPolicyListCount);

                    lvpolicylist.setAdapter(selectedAdapter);

                    ArrayAdapter<String> productnameAdapter = new ArrayAdapter<>(
                            context, R.layout.spinner_item,
                            lst_cif_product_name);
                    productnameAdapter
                            .setDropDownViewResource(R.layout.spinner_dropdown);
                    spinSearchPolicyListPNM.setAdapter(productnameAdapter);
                    productnameAdapter.notifyDataSetChanged();

                    ArrayAdapter<String> statusNameAdapter = new ArrayAdapter<>(
                            context, R.layout.spinner_item,
                            lst_policy_status);
                    statusNameAdapter
                            .setDropDownViewResource(R.layout.spinner_dropdown);
                    spinSearchPolicyListStatus.setAdapter(statusNameAdapter);
                    statusNameAdapter.notifyDataSetChanged();


                    Utility.setListViewHeightBasedOnChildren(lvpolicylist);
                } else {
                    txterrordesc.setText("No Record Found");
                    txtpolicylistcount.setText("Total Policy : " + 0);
                }

            } else {
                mCommonMethods.showMessageDialog(context,
                        "Server Not Responding,Try again..");
            }
        }
    }

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

    class SelectedAdapter extends ArrayAdapter<XMLHolder> {

        private final List<XMLHolder> lst;

        SelectedAdapter(Context context,
                        List<XMLHolder> objects) {
            super(context, 0, objects);
            lst = objects;
        }


        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) this.getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item, null);
            }
            TextView txtno = v.findViewById(R.id.txtno);
            TextView txtcustomercode = v
                    .findViewById(R.id.txtcustomercode);
            TextView fname = v.findViewById(R.id.txtfirstname);

            TextView productname = v
                    .findViewById(R.id.txtproductname);
            TextView policytype = v.findViewById(R.id.txtpolicytype);
            TextView policypaymentterm = v
                    .findViewById(R.id.txtpolicypaymentterm);
            TextView policysumassured = v
                    .findViewById(R.id.txtpolicysumassured);
            TextView premfre = v
                    .findViewById(R.id.txtpremiumpaymentfreq);
            TextView grossamt = v
                    .findViewById(R.id.txtpremiumgrossamt);

            TextView status = v.findViewById(R.id.txtstatus);
            TextView premiumup = v.findViewById(R.id.txtpremiumup);
            TextView teaxtviewPremiumPaymentFrequency = v.findViewById(R.id.teaxtviewPremiumPaymentFrequency);

            //added by rajan
            //TextView txt_status_linked_aadhar = (TextView) v.findViewById(R.id.txt_status_linked_aadhar);

            boolean i = lst.contains(null);

            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            int screenWidth = display.getWidth(); // Get full screen width

            int eightyPercent = (screenWidth * 50) / 100;

            if (!i) {
                if (lst.get(position).getStatus().contentEquals("Inforce")) {
                    status.setBackgroundColor(Color.parseColor("#008000"));
                } else if (lst.get(position).getStatus().contentEquals("Lapse")) {
                    status.setBackgroundColor(Color.parseColor("#FFA500"));
                } else if (lst.get(position).getStatus()
                        .contentEquals("Technical Lapse")) {
                    status.setBackgroundColor(Color.parseColor("#FFFF00"));
                } else if (lst.get(position).getStatus()
                        .contentEquals("Maturity")) {
                    status.setBackgroundColor(Color.parseColor("#0000FF"));
                } else if (lst.get(position).getStatus().contentEquals("Claim")) {
                    status.setBackgroundColor(Color.parseColor("#FFC0CB"));
                } else if (lst.get(position).getStatus()
                        .contentEquals("Surrender")) {
                    status.setBackgroundColor(Color.parseColor("#FF0000"));
                }

                txtno.setText(lst.get(position).getNo() == null ? "" : lst.get(
                        position).getNo());
                txtcustomercode
                        .setText(lst.get(position).getHolderId() == null ? ""
                                : lst.get(position).getHolderId());

                String fnm = lst.get(position).getFName() == null ? "" : lst
                        .get(position).getFName();
                String lnm = lst.get(position).getLName() == null ? "" : lst
                        .get(position).getLName();
                String name = fnm + " " + lnm;

                // fname.setText(name);
                /*float textWidthLAName =fname.getPaint().measureText(name);

                int numberOflineForNameb =(int) (textWidthLAName / eightyPercent) + 1;
                fname.setLines(numberOflineForNameb);*/
                fname.setText(name);

                String premiumPayFreq = "Premium Payment Frequency: ";
                float textWidthPPF = teaxtviewPremiumPaymentFrequency.getPaint().measureText(premiumPayFreq);
                float numberOflinesForPPF = (textWidthPPF / eightyPercent) + 0.7f;

                teaxtviewPremiumPaymentFrequency.setLines(Math.round(numberOflinesForPPF));
                teaxtviewPremiumPaymentFrequency.setText(premiumPayFreq);

                // Calculate 80% of it
                // as my adapter was having almost 80% of the whole screen width
                float textWidthProductName = productname.getPaint().measureText(lst.get(position).getProductName() == null ? ""
                        : lst.get(position).getProductName());
                // this method will give you the total width required to display total String
                int numberOfLinesAddress = Math.round((textWidthProductName / eightyPercent)) + 1;

                // calculate number of lines it might take
                productname.setLines(numberOfLinesAddress);

                productname
                        .setText(lst.get(position).getProductName() == null ? ""
                                : lst.get(position).getProductName());
                policytype
                        .setText(lst.get(position).getPolicyType() == null ? ""
                                : lst.get(position).getPolicyType());
                policypaymentterm
                        .setText(lst.get(position).getPolicyPayTerm() == null ? ""
                                : lst.get(position).getPolicyPayTerm());
                policysumassured.setText(lst.get(position)
                        .getPolicySumAssured() == null ? "" : lst.get(position)
                        .getPolicySumAssured());
                premfre.setText(lst.get(position).getPremiumFreq() == null ? ""
                        : lst.get(position).getPremiumFreq());
                grossamt.setText(lst.get(position).getGrossAmt() == null ? ""
                        : lst.get(position).getGrossAmt());

                status.setText(lst.get(position).getStatus() == null ? "" : lst
                        .get(position).getStatus());
                premiumup.setText(lst.get(position).getPremiumUp() == null ? ""
                        : lst.get(position).getPremiumUp());


                /*txt_status_linked_aadhar.setTextColor(getResources().getColor(R.color.WHITE));
                if (lstLinkedEkycPolicies.contains(lst.get(position).getNo())){
                    txt_status_linked_aadhar.setText("Success");
                    txt_status_linked_aadhar.setBackgroundColor(getResources().getColor(R.color.Common_blue));
                }else{
                    txt_status_linked_aadhar.setText("Not Done");
                    txt_status_linked_aadhar.setBackgroundColor(Color.parseColor("#FF0000"));
                }*/
            }

            return (v);
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.btn_savepolicylist:
                getPolicyList();
                break;
            case R.id.btn_click_policylist_policyno:
                searchByPolicyNumber();
                break;
            case R.id.btn_click_policylist_fn:
                searchByFirstName();
                break;
            case R.id.btn_click_policylist_status:
                searchByStatus();
                break;
            case R.id.btn_click_policylist_duedate:
                searchBYDueDate();
                break;
            case R.id.btn_click_policylist_productname:
                searchBYProductName();
                break;
            case R.id.btn_reset_policylist:
                resetFields();
                break;
            case R.id.imageButtonPolicySearchByDate:
                datecheck = 2;
                showDateProgressDialog();
                break;
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void getPolicyList() {
        // dbhelper = new DatabaseHelper(context);
        taskPolicyList = new DownloadFileAsync();


        txterrordesc.setVisibility(View.GONE);
        txtpolicylistcount.setVisibility(View.GONE);
        lvpolicylist.setVisibility(View.GONE);
        lnsearchpolicylist.setVisibility(View.GONE);

        strfromdate = editTextdt.getText().toString();
        strtodate = editTextdtto.getText().toString();

        lnsearchpolicylist.setVisibility(View.GONE);
        txtpolicylistcount.setText("");
        array_sort = new ArrayList<>();
        array_sort.clear();

        lstPolicyList = new ArrayList<>();
        lstPolicyList.clear();
        if (selectedAdapter != null) {
            selectedAdapter.clear();
            selectedAdapter.setNotifyOnChange(true);
        }


        if (chflagname.contentEquals("Select Product Type")
                || strfromdate.equalsIgnoreCase("")
                || strtodate.equalsIgnoreCase("")) {
            // validationAlert();
            mCommonMethods.showMessageDialog(context, "All Fields Required..");
        } else {
            final SimpleDateFormat formatter = new SimpleDateFormat(
                    "dd-MMMM-yyyy");

            String strfromdate = editTextdt.getText().toString();
            String strtodate = editTextdtto.getText().toString();
            try {
                Date d1 = formatter.parse(strfromdate);


                Date d2 = formatter.parse(strtodate);


                if ((d2.after(d1)) || (d2.equals(d1))) {

                    if (new CommonMethods().isNetworkConnected(context)) {
                        edSearchPolicyListPN.setText("");
                        edSearchPolicyListFN.setText("");
                        spinSearchPolicyListStatus.setSelection(0);
                        edSearchPolicyListDD.setText("");

                        // startDownloadPolicyList();
                        service_hits(METHOD_NAME_POLICY_LIST);

                    } else {
                        // intereneterror();
                        mCommonMethods.showMessageDialog(context,
                                "Internet Connection Not Present,Try again..");
                    }
                } else {
                    mCommonMethods.showMessageDialog(context,
                            "To date should be greater than From date");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void searchByPolicyNumber() {
        if (edSearchPolicyListPN.getText().toString().equalsIgnoreCase("")) {
            // policynoalert();
            mCommonMethods.showMessageDialog(context,
                    "Please Enter Policy No...");
        } else {
            mCommonMethods.hideKeyboard(edSearchPolicyListPN, context);


            array_sort = new ArrayList<>();
            array_sort.clear();
            String str = edSearchPolicyListPN.getText().toString().trim().toLowerCase();
            for (XMLHolder node : lstPolicyList) {
                String policyNumber = node.getNo().trim().toLowerCase();
                if (policyNumber.contains(str) || policyNumber.startsWith(str)) {
                    array_sort.add(node);
                }
            }

            selectedAdapter = new SelectedAdapter(context, array_sort);
            selectedAdapter.setNotifyOnChange(true);
            lvpolicylist.setAdapter(selectedAdapter);
            Utility.setListViewHeightBasedOnChildren(lvpolicylist);
        }
    }

    private void searchByFirstName() {
        if (edSearchPolicyListFN.getText().toString().equalsIgnoreCase("")) {
            mCommonMethods.showMessageDialog(context,
                    "Please Enter First Name...");
        } else {
            mCommonMethods.hideKeyboard(edSearchPolicyListFN, context);
            array_sort = new ArrayList<>();
            array_sort.clear();
            String str = edSearchPolicyListFN.getText().toString().trim().toLowerCase();
            for (XMLHolder node : lstPolicyList) {
                String fname = node.getFName().trim().toLowerCase();
                if (fname.contains(str) || fname.startsWith(str)) {
                    array_sort.add(node);
                }
            }

            selectedAdapter = new SelectedAdapter(context, array_sort);
            selectedAdapter.setNotifyOnChange(true);
            lvpolicylist.setAdapter(selectedAdapter);
            Utility.setListViewHeightBasedOnChildren(lvpolicylist);
        }
    }

    private void searchByStatus() {

        if (spinSearchPolicyListStatus.getSelectedItem().toString()
                .contentEquals("Select Status")) {
            // statusalert();
            mCommonMethods.showMessageDialog(context, "Please Select Status...");
        } else {
            mCommonMethods.hideKeyboard(spinSearchPolicyListStatus, context);

            array_sort = new ArrayList<>();
            array_sort.clear();
            String str = spinSearchPolicyListStatus.getSelectedItem()
                    .toString().trim().toLowerCase();
            for (XMLHolder node : lstPolicyList) {
                String status = node.getStatus().trim().toLowerCase();
                if (status.contains(str) || status.startsWith(str)) {
                    array_sort.add(node);
                }
            }

            selectedAdapter = new SelectedAdapter(context, array_sort);
            selectedAdapter.setNotifyOnChange(true);
            lvpolicylist.setAdapter(selectedAdapter);
            Utility.setListViewHeightBasedOnChildren(lvpolicylist);
        }
    }

    private void searchBYDueDate() {

        if (edSearchPolicyListDD.getText().toString().equalsIgnoreCase("")) {
            // duedatealert();
            mCommonMethods.showMessageDialog(context, "Please Enter Date...");
        } else {
            mCommonMethods.hideKeyboard(edSearchPolicyListDD, context);

            array_sort = new ArrayList<>();
            array_sort.clear();
            String str = edSearchPolicyListDD.getText().toString().trim().toLowerCase();
            for (XMLHolder node : lstPolicyList) {
                String premiumUp = node.getPremiumUp().trim().toLowerCase();
                if (premiumUp.contains(str) || premiumUp.startsWith(str)) {
                    array_sort.add(node);
                }

            }

            selectedAdapter = new SelectedAdapter(context, array_sort);
            selectedAdapter.setNotifyOnChange(true);
            lvpolicylist.setAdapter(selectedAdapter);
            Utility.setListViewHeightBasedOnChildren(lvpolicylist);
        }
    }

    private void searchBYProductName() {

        if (spinSearchPolicyListPNM.getSelectedItem().toString()
                .contentEquals("Select Product")) {
            // productnamealert();
            mCommonMethods.showMessageDialog(context,
                    "Please Select Product Name...");
        } else {
            mCommonMethods.hideKeyboard(spinSearchPolicyListPNM, context);

            array_sort = new ArrayList<>();
            array_sort.clear();
            String str = spinSearchPolicyListPNM.getSelectedItem()
                    .toString().trim().toLowerCase();
            for (XMLHolder node : lstPolicyList) {

                String productName = node.getProductName().trim().toLowerCase();
                if (productName.contains(str) || productName.startsWith(str)) {
                    array_sort.add(node);
                }
            }

            selectedAdapter = new SelectedAdapter(context, array_sort);
            selectedAdapter.setNotifyOnChange(true);
            lvpolicylist.setAdapter(selectedAdapter);
            Utility.setListViewHeightBasedOnChildren(lvpolicylist);
        }
    }

    private void resetFields() {
        lnsearchpolicylist.setVisibility(View.GONE);

        // edpolicyCifNo.setText("");
        editTextdt.setText("");
        editTextdtto.setText("");
        chflag.setSelection(0);

        txtpolicylistcount.setText("");
        array_sort = new ArrayList<>();
        array_sort.clear();

        lstPolicyList = new ArrayList<>();
        lstPolicyList.clear();
        if (selectedAdapter != null) {
            selectedAdapter.clear();
        }

        txtpolicylistcount.setVisibility(View.GONE);
        txterrordesc.setVisibility(View.GONE);
        lvpolicylist.setVisibility(View.GONE);
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {

        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");
        switch (id) {
            case DATE_DIALOG_ID:
                final Calendar cal = Calendar.getInstance();

                switch (datecheck) {
                    case 1: {
                        String str = editTextdt.getText().toString();
                        if (!str.equalsIgnoreCase("")) {

                            Date d1 = null;
                            try {
                                d1 = formatter.parse(str);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            cal.setTime(d1);

                            mYear = cal.get(Calendar.YEAR);
                            mMonth = cal.get(Calendar.MONTH);
                            mDay = cal.get(Calendar.DAY_OF_MONTH);
                        }

                        break;
                    }
                    case 2: {
                        String str = edSearchPolicyListDD.getText().toString();
                        if (!str.equalsIgnoreCase("")) {
                            Date d1 = null;
                            try {
                                d1 = formatter.parse(str);
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
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

                        if (!str.equalsIgnoreCase("")) {

                            Date d1 = null;
                            try {
                                d1 = formatter.parse(str);
                            } catch (ParseException e) {
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
