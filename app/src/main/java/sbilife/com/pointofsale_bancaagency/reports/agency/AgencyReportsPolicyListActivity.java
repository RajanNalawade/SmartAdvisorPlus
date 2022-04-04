package sbilife.com.pointofsale_bancaagency.reports.agency;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.CustomerDetailActivity;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolder_Agent;
import sbilife.com.pointofsale_bancaagency.PolicyDetailActivity;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.Utility;
import sbilife.com.pointofsale_bancaagency.ekyc.EkycPSClaimsActivity;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods.UserDetailsValuesModel;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits.DownLoadData;

@SuppressLint("SimpleDateFormat")
public class AgencyReportsPolicyListActivity extends AppCompatActivity implements
        OnClickListener, DownLoadData {

    private final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    private DownloadFileAsync taskPolicyList;

    private int mYear, mMonth, mDay, datecheck = 0;
    private final int DATE_DIALOG_ID = 1;

    private String strfromdate = "", strtodate = "";// ,y,m;

    private final String METHOD_NAME_POLICY_LIST_AGENCY = "getAgentPoliciesList";

    private List<XMLHolder_Agent> lstPolicyListAgent;
    private ArrayList<String> lst_Agent_product_name;
    private ArrayList<String> lst_policy_status;

    private long lstPolicyListCount = 0;

    private String strCIFBDMUserId = "", strCIFBDMEmailId = "",
            strCIFBDMPassword = "", strCIFBDMMObileNo = "", chflagname,
            strPolicyListErrorCOde = "";
    private Context context;

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

    private CommonMethods commonMethods;
    private SelectedAdapterAgent selectedAdapterAgent;
    private ArrayList<XMLHolder_Agent> array_sort;
    //private ArrayList<String> lstLinkedEkycPolicies = new ArrayList<>();
    private ServiceHits service;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.banca_reports_policy_list);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);


        context = this;
        commonMethods = new CommonMethods();
        mProgressDialog = new ProgressDialog(context);
        commonMethods.setApplicationToolbarMenu(this, "Policy List");

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

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
        //edSearchPolicyListDD.setOnClickListener(this);
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
        UserDetailsValuesModel userDetailsValuesModel = commonMethods
                .setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
        System.out.println("strCIFBDMUserId:" + strCIFBDMUserId
                + " strCIFBDMEmailId:" + strCIFBDMEmailId
                + " strCIFBDMPassword:" + strCIFBDMPassword
                + " strCIFBDMMObileNo:" + strCIFBDMMObileNo);
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
        String m = String.valueOf(mMonth + 1);
        String y = String.valueOf(mYear);

        String y1 = String.valueOf(mYear - 1);

        y = String.valueOf(mYear);
        m = String.valueOf(mMonth + 1);
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

        m = commonMethods.getFullMonthName(m);

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
                mProgressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
                String Message = "Loading Please wait...";
                mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
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
            /*String UserType = commonMethods.GetUserType(context);
            if (UserType.contentEquals("CIF")) {

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

        if (item.getTitle() == "Policy Detail") {
            String strPolicyNo = array_sort.get(info.position).getNo();

            if (strPolicyNo != "" || strPolicyNo != null) {
                Intent intent = new Intent(context,
                        PolicyDetailActivity.class);
                intent.putExtra("PolicyNo", strPolicyNo);
                intent.putExtra("strUserType", "Agent");
                intent.putExtra("strUserId", strCIFBDMUserId);
                startActivity(intent);
            }
        } else if (item.getTitle() == "Customer Detail") {
            String strCustomerId = array_sort.get(info.position).getHolderId();

            if (strCustomerId != "" || strCustomerId != null) {
                Intent intent = new Intent(context,
                        CustomerDetailActivity.class);
                intent.putExtra("CustomerId", strCustomerId);
                intent.putExtra("strUserType", "Agent");
                intent.putExtra("strAgentCode", strCIFBDMUserId);
                intent.putExtra("strEmail", strCIFBDMEmailId);
                intent.putExtra("strMobileNo", strCIFBDMMObileNo);
                intent.putExtra("strPassword", strCIFBDMPassword.trim());
                startActivity(intent);
            }
        } else if (item.getTitle() == "Link To Aadhar") {

            String strPolicyNo = array_sort.get(info.position).getNo();

            if (strPolicyNo != "" || strPolicyNo != null) {
                //call PS Claims Activity
                Intent intent = new Intent(AgencyReportsPolicyListActivity.this, EkycPSClaimsActivity.class);
                intent.putExtra("POLICY_NUM", strPolicyNo);
                startActivity(intent);
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

    private void service_hits() {

        strfromdate = editTextdt.getText().toString();
        strtodate = editTextdtto.getText().toString();
        String str = strfromdate +"," +strtodate +"," +
                chflagname;

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


        service = new ServiceHits(context,  METHOD_NAME_POLICY_LIST_AGENCY, str,
                strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();
    }


    @Override
    public void downLoadData() {
            startDownloadPolicyList();
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
                //String UserType = commonMethods.GetUserType(context);

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
                request = new SoapObject(NAMESPACE,
                        METHOD_NAME_POLICY_LIST_AGENCY);
                request.addProperty("strAgentNo", strCIFBDMUserId);
                request.addProperty("strfromDt", strfromdate);
                request.addProperty("strToDt", strtodate);
                request.addProperty("polType", chflagname);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    String SOAP_ACTION_POLICY_LIST_AGENCY = "http://tempuri.org/getAgentPoliciesList";
                    androidHttpTranport.call(
                            SOAP_ACTION_POLICY_LIST_AGENCY, envelope);
                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("anyType{}")) {
                        System.out.println("response:" + response.toString());
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
                                // for agent policy list

                                inputpolicylist = sa.toString();
                                inputpolicylist = prsObj.parseXmlTag(
                                        inputpolicylist, "CIFPolicyList");

                                List<String> Node = prsObj.parseParentNode(
                                        inputpolicylist, "Table");

                                List<XMLHolder_Agent> nodeData = prsObj
                                        .parseNodeElementAgent(Node);

                                // final List<XMLHolder> lstPolicyList;
                                lstPolicyListAgent = new ArrayList<>();
                                lstPolicyListAgent.clear();

                                lst_Agent_product_name = new ArrayList<>();
                                lst_Agent_product_name.clear();

                                lst_policy_status = new ArrayList<>();
                                lst_policy_status.clear();

                                int i = 0,j = 0;

                                for (XMLHolder_Agent node : nodeData) {

                                    lstPolicyListAgent.add(node);

                                    if (node.getProductName() != null
                                            || node.getProductName() != "") {
                                        if (i == 0) {
                                            lst_Agent_product_name
                                                    .add("Select Product");
                                            i = 1;
                                        }

                                        if (!lst_Agent_product_name
                                                .contains(node
                                                        .getProductName())) {
                                            lst_Agent_product_name.add(node
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

                                lstPolicyListCount = lstPolicyListAgent
                                        .size();
                                array_sort = new ArrayList<>(lstPolicyListAgent);
                                selectedAdapterAgent = new SelectedAdapterAgent(
                                        context, 0, lstPolicyListAgent);
                                selectedAdapterAgent
                                        .setNotifyOnChange(true);

                                registerForContextMenu(lvpolicylist);
                            }

                        } catch (Exception e) {
                            mProgressDialog.dismiss();
                            running = false;
                        }
                    } else {

                    }

                } catch (IOException | XmlPullParserException e) {
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

            if (running) {

                txterrordesc.setVisibility(View.VISIBLE);
                txtpolicylistcount.setVisibility(View.VISIBLE);

                if (strPolicyListErrorCOde == null) {
                    lnsearchpolicylist.setVisibility(View.VISIBLE);
                    lvpolicylist.setVisibility(View.VISIBLE);

                    txterrordesc.setText("");
                    txtpolicylistcount.setText("Total Policy : "
                            + lstPolicyListCount);
                    lvpolicylist.setAdapter(selectedAdapterAgent);
                    Utility.setListViewHeightBasedOnChildren(lvpolicylist);

                    ArrayAdapter<String> productnameAdapter = new ArrayAdapter<>(
                            context, R.layout.spinner_item,
                            lst_Agent_product_name);
                    productnameAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
                    spinSearchPolicyListPNM.setAdapter(productnameAdapter);
                    productnameAdapter.notifyDataSetChanged();

                    ArrayAdapter<String> statusNameAdapter = new ArrayAdapter<>(
                            context, R.layout.spinner_item,
                            lst_policy_status);
                    statusNameAdapter
                            .setDropDownViewResource(R.layout.spinner_dropdown);
                    spinSearchPolicyListStatus.setAdapter(statusNameAdapter);
                    statusNameAdapter.notifyDataSetChanged();


                } else {
                    lnsearchpolicylist.setVisibility(View.GONE);
                    txterrordesc.setText("No Record Found");
                    txtpolicylistcount.setText("Total Policy : " + 0);
                }
            } else {
                commonMethods.showMessageDialog(context,
                        "Server Not Responding,Try again..");
            }

            if (mProgressDialog.isShowing()) {
                dismissProgressDialog();
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void dismissProgressDialog() {
        if (mProgressDialog.isShowing()) {
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
        }
    }

    @SuppressWarnings("deprecation")
    private void showProgressDialog() {
        showDialog(DIALOG_DOWNLOAD_PROGRESS);
    }

    @SuppressWarnings("deprecation")
    private void showDateProgressDialog() {
        showDialog(DATE_DIALOG_ID);
    }

    class SelectedAdapterAgent extends ArrayAdapter<XMLHolder_Agent> {

        // used to keep selected position in ListView
        private int selectedPos = -1; // init value for not-selected

        final List<XMLHolder_Agent> lst;

        SelectedAdapterAgent(Context context, int textViewResourceId,
                             List<XMLHolder_Agent> objects) {
            super(context, textViewResourceId, objects);

            lst = objects;
        }



        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            // only inflate the view if it's null
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) this.getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_itemagent, null);
            }

            // get text view
            TextView txtpropsalno = v
                    .findViewById(R.id.txtproposalno);
            TextView txtholderid = v.findViewById(R.id.txtholderid);

            TextView txtno = v.findViewById(R.id.txtno);
            TextView fname = v.findViewById(R.id.txtfirstname);
            /* TextView lname = (TextView)v.findViewById(R.id.txtlastname); */

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

            //added by rajan
            //TextView txt_status_linked_aadhar_agency = (TextView) v.findViewById(R.id.txt_status_linked_aadhar_agency);
            TextView teaxtviewPremiumPaymentFrequency = v.findViewById(R.id.teaxtviewPremiumPaymentFrequency);
            // change the row color based on selected state

            // label.setBackgroundColor(Color.CYAN);

            // sreid.setText(this.getItem(position).toString());

            Object obj = null;
            boolean i = lst.contains(obj);

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

                txtpropsalno
                        .setText(lst.get(position).getProposalNo() == null ? ""
                                : lst.get(position).getProposalNo());
                txtholderid
                        .setText(lst.get(position).getHolderId() == null ? ""
                                : lst.get(position).getHolderId());

                txtno.setText(lst.get(position).getNo() == null ? "" : lst.get(
                        position).getNo());

                String fnm = lst.get(position).getFName() == null ? "" : lst
                        .get(position).getFName();
                String lnm = lst.get(position).getLName() == null ? "" : lst
                        .get(position).getLName();
                String name = fnm + " " + lnm;

               /* float textWidthLAName =fname.getPaint().measureText(name);

                float numberOflineForNameb = (textWidthLAName / eightyPercent) + 0.7f;
                fname.setLines(Math.round(numberOflineForNameb));*/
                fname.setText(name);

                String premiumPayFreq = "Premium Payment Frequency: ";
                float textWidthPPF = teaxtviewPremiumPaymentFrequency.getPaint().measureText(premiumPayFreq);
                float numberOflinesForPPF = (textWidthPPF / eightyPercent) + 0.7f;

                teaxtviewPremiumPaymentFrequency.setLines(Math.round(numberOflinesForPPF));
                teaxtviewPremiumPaymentFrequency.setText(premiumPayFreq);

                // Calculate 80% of it
                // as my adapter was having almost 80% of the whole screen width
                float textWidthProductName = productname.getPaint().measureText(lst.get(position).getProductName() == null ? ""
                        : lst.get(position).getProductName().trim());
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

                /*txt_status_linked_aadhar_agency.setTextColor(getResources().getColor(R.color.WHITE));
                if (lstLinkedEkycPolicies.contains(lst.get(position).getNo())) {
                    txt_status_linked_aadhar_agency.setText("Success");
                    txt_status_linked_aadhar_agency.setBackgroundColor(getResources().getColor(R.color.Common_blue));
                } else {
                    txt_status_linked_aadhar_agency.setText("Not Done");
                    txt_status_linked_aadhar_agency.setBackgroundColor(Color.parseColor("#FF0000"));
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

        array_sort = new ArrayList<>();
        array_sort.clear();

        strfromdate = editTextdt.getText().toString();
        strtodate = editTextdtto.getText().toString();

        if (chflagname.contentEquals("Select Product Type")
                || strfromdate.equalsIgnoreCase("")
                || strtodate.equalsIgnoreCase("")) {
            // validationAlert();
            commonMethods.showMessageDialog(context, "All Fields Required..");
        } else {
            final SimpleDateFormat formatter = new SimpleDateFormat(
                    "dd-MMMM-yyyy");

            String strfromdate = editTextdt.getText().toString();
            String strtodate = editTextdtto.getText().toString();

            Date d1, d2;
            try {
                d1 = formatter.parse(strfromdate);
                d2 = formatter.parse(strtodate);

                if ((d2.after(d1)) || (d2.equals(d1))) {

                    if (new CommonMethods().isNetworkConnected(context)) {
                        edSearchPolicyListPN.setText("");
                        edSearchPolicyListFN.setText("");
                        spinSearchPolicyListStatus.setSelection(0);
                        edSearchPolicyListDD.setText("");

                        // startDownloadPolicyList();
                        service_hits();
                    } else {
                        // intereneterror();
                        commonMethods.showMessageDialog(context,
                                "Internet Connection Not Present,Try again..");
                    }
                } else {
                    commonMethods.showMessageDialog(context,
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
            commonMethods.showMessageDialog(context,
                    "Please Enter Policy No...");
        } else {
            commonMethods.hideKeyboard(edSearchPolicyListPN, context);

            //String UserType = commonMethods.GetUserType(context);
            array_sort = new ArrayList<>();
            array_sort.clear();

            String str = edSearchPolicyListPN.getText().toString().trim().toLowerCase();
            for (XMLHolder_Agent node : lstPolicyListAgent) {

                String number = node.getNo().toLowerCase().trim();
                if (number.contains(str) || number.startsWith(str)) {
                    array_sort.add(node);
                }
            }

            selectedAdapterAgent = new SelectedAdapterAgent(
                    context, 0, array_sort);
            selectedAdapterAgent.setNotifyOnChange(true);
            lvpolicylist.setAdapter(selectedAdapterAgent);
            Utility.setListViewHeightBasedOnChildren(lvpolicylist);
        }
    }

    private void searchByFirstName() {

        String str = edSearchPolicyListFN.getText().toString();
        str = str == null ? "" : str;

        if (str.equalsIgnoreCase("")) {
            // fnalert();
            commonMethods.showMessageDialog(context,
                    "Please Enter First Name...");
        } else {
            commonMethods.hideKeyboard(edSearchPolicyListFN, context);

            //String UserType = commonMethods.GetUserType(context);

            array_sort = new ArrayList<>();
            array_sort.clear();

            str = str.toLowerCase().trim();
            for (XMLHolder_Agent node : lstPolicyListAgent) {
                String fName = node.getFName().trim().toLowerCase();
                if (fName.contains(str) || fName.startsWith(str)) {
                    array_sort.add(node);
                }
            }

            selectedAdapterAgent = new SelectedAdapterAgent(
                    context, 0, array_sort);
            selectedAdapterAgent.setNotifyOnChange(true);
            lvpolicylist.setAdapter(selectedAdapterAgent);
            Utility.setListViewHeightBasedOnChildren(lvpolicylist);
        }
    }

    private void searchByStatus() {

        if (spinSearchPolicyListStatus.getSelectedItem().toString()
                .contentEquals("Select Status")) {
            // statusalert();
            commonMethods.showMessageDialog(context, "Please Select Status...");
        } else {
            commonMethods.hideKeyboard(spinSearchPolicyListStatus, context);

            //String UserType = commonMethods.GetUserType(context);
            array_sort = new ArrayList<>();
            array_sort.clear();

            String str = spinSearchPolicyListStatus
                    .getSelectedItem().toString().toLowerCase().trim();

            for (XMLHolder_Agent node : lstPolicyListAgent) {
                String status = node.getStatus().toLowerCase().trim();
                if (status.contains(str) || status.startsWith(str)) {
                    array_sort.add(node);
                }
            }
            selectedAdapterAgent = new SelectedAdapterAgent(
                    context, 0, array_sort);
            selectedAdapterAgent.setNotifyOnChange(true);
            lvpolicylist.setAdapter(selectedAdapterAgent);
            Utility.setListViewHeightBasedOnChildren(lvpolicylist);
        }
    }

    private void searchBYDueDate() {

        if (edSearchPolicyListDD.getText().toString().equalsIgnoreCase("")) {
            // duedatealert();
            commonMethods.showMessageDialog(context, "Please Enter Date...");
        } else {
            commonMethods.hideKeyboard(edSearchPolicyListDD, context);

            //String UserType = commonMethods.GetUserType(context);
            array_sort = new ArrayList<>();
            array_sort.clear();

            String str = edSearchPolicyListDD.getText().toString().trim().toLowerCase();
            for (XMLHolder_Agent node : lstPolicyListAgent) {
                String premiumUp = node.getPremiumUp().toLowerCase().trim();
                if (premiumUp.contains(str) || premiumUp.startsWith(str)) {
                    array_sort.add(node);
                }
            }

            selectedAdapterAgent = new SelectedAdapterAgent(
                    context, 0, array_sort);
            selectedAdapterAgent.setNotifyOnChange(true);
            lvpolicylist.setAdapter(selectedAdapterAgent);
            Utility.setListViewHeightBasedOnChildren(lvpolicylist);
        }
    }

    private void searchBYProductName() {

        if (spinSearchPolicyListPNM.getSelectedItem().toString()
                .contentEquals("Select Product")) {
            // productnamealert();
            commonMethods.showMessageDialog(context,
                    "Please Select Product Name...");
        } else {
            commonMethods.hideKeyboard(spinSearchPolicyListPNM, context);

            //String UserType = commonMethods.GetUserType(context);

            array_sort = new ArrayList<>();
            array_sort.clear();

            String str = spinSearchPolicyListPNM
                    .getSelectedItem().toString().toLowerCase().trim();
            for (XMLHolder_Agent node : lstPolicyListAgent) {

                String productName = node.getProductName().toLowerCase().trim().toLowerCase();
                if (productName.contains(str) || productName.startsWith(str)) {
                    array_sort.add(node);
                }
            }

            selectedAdapterAgent = new SelectedAdapterAgent(
                    context, 0, array_sort);
            selectedAdapterAgent.setNotifyOnChange(true);
            lvpolicylist.setAdapter(selectedAdapterAgent);
            Utility.setListViewHeightBasedOnChildren(lvpolicylist);
        }
    }

    private void resetFields() {
        lnsearchpolicylist.setVisibility(View.GONE);

        // edpolicyCifNo.setText("");
        editTextdt.setText("");
        editTextdtto.setText("");
        chflag.setSelection(0);
        array_sort = new ArrayList<>();
        array_sort.clear();

        txtpolicylistcount.setVisibility(View.GONE);
        txterrordesc.setVisibility(View.GONE);
        lvpolicylist.setVisibility(View.GONE);
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {

        try {
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
                                } catch (Exception e) {
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
                                } catch (Exception e) {
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

                                Date d1;
                                try {
                                    d1 = formatter.parse(str);
                                    cal.setTime(d1);

                                    mYear = cal.get(Calendar.YEAR);
                                    mMonth = cal.get(Calendar.MONTH);
                                    mDay = cal.get(Calendar.DAY_OF_MONTH);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        }
                    }

                    ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                    break;
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (taskPolicyList != null) {
            taskPolicyList.cancel(true);
        }

        if (service != null) {
            service.cancel(true);
        }

        super.onDestroy();
    }
}
