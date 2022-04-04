
package sbilife.com.pointofsale_bancaagency.reports.commonreports;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.Element_TextView_BaseAdapter;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class AutoMandateRegistrationStatusListReportsActivity extends AppCompatActivity implements View.OnClickListener,
        ServiceHits.DownLoadData, AdapterView.OnItemSelectedListener {

    // private ListView listviewAutoMandateStatus;
    private EditText edittextAutoMandateStatus,edt_search_proposal;

    private ProgressDialog mProgressDialog;
    private  final String SOAP_ACTION_AUTO_MANDATE_STATUS_LIST = "http://tempuri.org/getMMS_Status";
    private  final String METHOD_NAME_AUTO_MANDATE_STATUS_LIST = "getMMS_Status";

    private final String SOAP_ACTION_AUTO_MANDATE_PENETRATION_STATUS_LIST = "http://tempuri.org/getMandate_Penetration_smrt";
    private final String METHOD_NAME_AUTO_MANDATE_PENETRATION_STATUS_LIST = "getMandate_Penetration_smrt";

    private  final String NAMESPACE = "http://tempuri.org/";
    private  final String URl = ServiceURL.SERVICE_URL;

    private Context context;
    private CommonMethods commonMethods;
    private String policyNumber = "", autoMandateTypeSelected = "",
            autoMandateStatusSelected = "", fromDate = "", toDate = "";
    private TextView textViewFromDate, textViewToDate;

    private DownloadAutoMandateStatusListAsync autoMandateStatusListAsync;
    private DownloadAutoMandatePenetrationStatusAsync autoMandatePenetrationStatusAsync;
    private ServiceHits service;

    private ArrayList<ParseXML.AutoMandateStatusListValuesModel> autoMandateGlobalList;
    private SelectedAdapterAutoMandateList selectedAdapterAutoMandateList;
    private TextView txterrordesc;
    private RecyclerView recyclerviewAutoMandate;
    private LinearLayout linearLayoutAutomandatePolicyNumber, linearLayoutAutomandatePenetrationStatus,
            linearLayoutAutomandatePenetrationMonth;

    private String strCIFBDMUserId, strCIFBDMEmailId,
            strCIFBDMPassword, strCIFBDMMObileNo,fromHome;
    private LinearLayout linearLayoutExternalResultMandatePenetration;
    private Spinner spinnerAuotMandatePenetrationStatus, spinnerAuotMandatePenetrationMonth;
    private int mYear, mMonth, mDay, datecheck = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_auto_mandate_registration_status_reports);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context =this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Auto Mandate Status List");

        // listviewAutoMandateStatus = (ListView)findViewById(R.id.listviewAutoMandateStatus);
        Button buttonAutoMandateStatus = findViewById(R.id.buttonAutoMandateStatus);
        edittextAutoMandateStatus = findViewById(R.id.edittextAutoMandateStatus);
        edt_search_proposal = findViewById(R.id.edt_search_proposal);
        recyclerviewAutoMandate = findViewById(R.id.recyclerviewAutoMandate);

        txterrordesc = findViewById(R.id.txterrordesc);
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        textViewFromDate = findViewById(R.id.textViewFromDate);
        textViewToDate = findViewById(R.id.textViewToDate);

        //lstReq.setHasFixedSize(true);
        // set a LinearLayoutManager with default horizontal orientation and false value for reverseLayout to show the items from start to end
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        // set LayoutManager to RecyclerView
        recyclerviewAutoMandate.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        autoMandateGlobalList = new ArrayList<>();
        selectedAdapterAutoMandateList = new SelectedAdapterAutoMandateList(
                autoMandateGlobalList);
        recyclerviewAutoMandate.setAdapter(selectedAdapterAutoMandateList);
        recyclerviewAutoMandate.setItemAnimator(new DefaultItemAnimator());

        Spinner spinnerAutoMandateType = findViewById(R.id.spinnerAutoMandateType);
        spinnerAuotMandatePenetrationStatus = findViewById(R.id.spinnerAuotMandatePenetrationStatus);
        spinnerAuotMandatePenetrationMonth = findViewById(R.id.spinnerAuotMandatePenetrationMonth);

        linearLayoutAutomandatePolicyNumber = findViewById(R.id.linearLayoutAutomandatePolicyNumber);
        linearLayoutAutomandatePenetrationStatus = findViewById(R.id.linearLayoutAutomandatePenetrationStatus);
        linearLayoutAutomandatePenetrationMonth = findViewById(R.id.linearLayoutAutomandatePenetrationMonth);
        linearLayoutExternalResultMandatePenetration = findViewById(R.id.linearLayoutExternalResultMandatePenetration);
        LinearLayout linearLayoutMandateStatus = findViewById(R.id.linearLayoutMandateStatus);

        buttonAutoMandateStatus.setOnClickListener(this);

        linearLayoutAutomandatePolicyNumber.setVisibility(View.GONE);
        linearLayoutAutomandatePenetrationMonth.setVisibility(View.GONE);
        linearLayoutAutomandatePenetrationStatus.setVisibility(View.GONE);
        linearLayoutExternalResultMandatePenetration.setVisibility(View.GONE);

        spinnerAutoMandateType.setOnItemSelectedListener(this);
        spinnerAuotMandatePenetrationStatus.setOnItemSelectedListener(this);
        spinnerAuotMandatePenetrationMonth.setOnItemSelectedListener(this);




        Intent intent = getIntent();
        fromHome = intent.getStringExtra("fromHome");
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

        mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        String[] monthList = {"Select Month", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

        Element_TextView_BaseAdapter retd_adapter = new Element_TextView_BaseAdapter(
                context, monthList);
        spinnerAuotMandatePenetrationMonth.setAdapter(retd_adapter);

        edt_search_proposal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                selectedAdapterAutoMandateList.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        mProgressDialog.setButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (autoMandateStatusListAsync != null) {
                            autoMandateStatusListAsync.cancel(true);
                        }
                        if (autoMandatePenetrationStatusAsync != null) {
                            autoMandatePenetrationStatusAsync.cancel(true);
                        }
                        if (mProgressDialog != null) {
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }
                    }
                });

        mProgressDialog.setMax(100);

        linearLayoutMandateStatus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!TextUtils.isEmpty(autoMandateStatusSelected)
                        && !autoMandateStatusSelected.equalsIgnoreCase("Select Mandate Status")
                        && !TextUtils.isEmpty(fromDate) && !TextUtils.isEmpty(toDate)) {
                    try {
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_with_ok_button);
                        TextView text = dialog.findViewById(R.id.tv_title);
                        text.setText("Penetration Details");
                        Button dialogButton = dialog.findViewById(R.id.bt_ok);
                        dialogButton.setText("Ok");
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                dialog.dismiss();
                                Intent intent = new Intent(context, AutoMandatePenetrationDetailsActivity.class);
                                intent.putExtra("strFromDate", fromDate);
                                intent.putExtra("strToDate", toDate);
                                intent.putExtra("strType", autoMandateStatusSelected);
                                startActivity(intent);
                            }
                        });
                        dialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
        textViewFromDate.setOnClickListener(this);
        textViewToDate.setOnClickListener(this);
        setDates();
    }
    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }
    private void setDates() {
        textViewFromDate.setText(commonMethods.getPreviousMonthDate());
        textViewToDate.setText(commonMethods.getCurrentMonthDate());
    }

    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {

            mYear = year;
            mMonth = month;
            mDay = day;
            updateDisplay(mYear, mMonth, mDay);
        }
    };

    @Override
    public void downLoadData() {
        if (autoMandateTypeSelected.equalsIgnoreCase("Policy Number")) {
        autoMandateStatusListAsync = new DownloadAutoMandateStatusListAsync();
        autoMandateStatusListAsync.execute();
        } else if (autoMandateTypeSelected.equalsIgnoreCase("Month")) {
            autoMandatePenetrationStatusAsync = new DownloadAutoMandatePenetrationStatusAsync();
            autoMandatePenetrationStatusAsync.execute();
    }
    }


    class DownloadAutoMandateStatusListAsync extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String strAutoMandateStatusListError = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;
                //String UserType = commonMethods.GetUserType(context);
                request = new SoapObject(NAMESPACE,
                        METHOD_NAME_AUTO_MANDATE_STATUS_LIST);
                request.addProperty("strPolicyNo", policyNumber);

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                    androidHttpTranport.call(
                            SOAP_ACTION_AUTO_MANDATE_STATUS_LIST, envelope);
                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("<NewDataSet />")) {
                        System.out.println("response:" + response.toString());
                        SoapPrimitive sa;
                            sa = (SoapPrimitive) envelope.getResponse();

                            String inputpolicylist = sa.toString();

                            ParseXML prsObj = new ParseXML();

                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "NewDataSet");
                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "ScreenData");
                            strAutoMandateStatusListError = inputpolicylist;

                            if (strAutoMandateStatusListError == null) {
                                // for agent policy list

                                inputpolicylist = sa.toString();
                                inputpolicylist = prsObj.parseXmlTag(
                                        inputpolicylist, "NewDataSet");

                                List<String> Node = prsObj.parseParentNode(
                                        inputpolicylist, "Table");

                                List<ParseXML.AutoMandateStatusListValuesModel> nodeData = prsObj
                                        .parseNodeAutoMandateStatusList(Node);
                                autoMandateGlobalList.clear();
                                autoMandateGlobalList.addAll(nodeData);
                            }

                } else {
                    running = false;
                }
            } catch (Exception e) {
                mProgressDialog.dismiss();
                running = false;
            }
            return null;

        }

        @Override
        protected void onProgressUpdate(String... progress) {
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            txterrordesc.setVisibility(View.VISIBLE);
            if (running) {
                edt_search_proposal.setVisibility(View.GONE);

                if (strAutoMandateStatusListError == null) {
                    txterrordesc.setText("");
                    selectedAdapterAutoMandateList = new SelectedAdapterAutoMandateList(autoMandateGlobalList);
                    recyclerviewAutoMandate.setAdapter(selectedAdapterAutoMandateList);
                    recyclerviewAutoMandate.invalidate();
                    //edt_search_proposal.setVisibility(View.VISIBLE);
                } else {

                    txterrordesc.setText("No Record Found");
                    clearList();
                }
            } else {
                txterrordesc.setText("No Record Found");
                clearList();
            }
            }
        }

    class DownloadAutoMandatePenetrationStatusAsync extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private float policyIssued = 0, mandateCount = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;
                //String UserType = commonMethods.GetUserType(context);
                request = new SoapObject(NAMESPACE,
                        METHOD_NAME_AUTO_MANDATE_PENETRATION_STATUS_LIST);
                request.addProperty("strCifNo", strCIFBDMUserId);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", commonMethods.getStrAuth());
                request.addProperty("strFromDate", fromDate);
                request.addProperty("strToDate", toDate);
                request.addProperty("strType", autoMandateStatusSelected);

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();


                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                androidHttpTranport.call(
                        SOAP_ACTION_AUTO_MANDATE_PENETRATION_STATUS_LIST, envelope);
                Object response = envelope.getResponse();

                if (!response.toString().contentEquals("<NewDataSet />")) {
                    System.out.println("response:" + response.toString());
                    SoapPrimitive sa;
                    sa = (SoapPrimitive) envelope.getResponse();

                    String inputpolicylist = sa.toString();

                    ParseXML prsObj = new ParseXML();
                    // for agent policy list
                    inputpolicylist = sa.toString();
                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "CIFPolicyList");

                    String Node = prsObj.parseXmlTag(inputpolicylist, "Table");

                    String policyIssuedString = "", mandateReceivedString = "";
                    policyIssuedString = prsObj.parseXmlTag(Node, "POLICYISSUED");
                    if (autoMandateStatusSelected.equalsIgnoreCase("Mandate Penetration")) {
                        mandateReceivedString = prsObj.parseXmlTag(Node, "MANDATE_RECEIVED");
                    } else if (autoMandateStatusSelected.equalsIgnoreCase("Mandate Registered")) {
                        mandateReceivedString = prsObj.parseXmlTag(Node, "MANDATE_REGISTERED");
                    } else if (autoMandateStatusSelected.equalsIgnoreCase("Mandate Rejected")) {
                        mandateReceivedString = prsObj.parseXmlTag(Node, "MANDATE_REJECTED");
                    } else if (autoMandateStatusSelected.equalsIgnoreCase("Mandate Not Received")) {
                        mandateReceivedString = prsObj.parseXmlTag(Node, "MANDATE_NOT_RECEIVED");
                    } else {
                        mandateReceivedString = "0";
                    }

                    if (policyIssuedString != null) {
                        try {
                            policyIssued = Float.parseFloat(policyIssuedString);
                        } catch (Exception e) {
                            policyIssued = 0;
                        }
                    } else {
                        policyIssued = 0;
                    }

                    if (mandateReceivedString != null) {
                        try {
                            mandateCount = Float.parseFloat(mandateReceivedString);
                        } catch (Exception e) {
                            mandateCount = 0;
                        }
                    } else {
                        mandateCount = 0;
                    }


                } else {
                    running = false;
                }

            } catch (Exception e) {
                running = false;
            }
            return null;

        }


        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            txterrordesc.setVisibility(View.VISIBLE);
            linearLayoutExternalResultMandatePenetration.setVisibility(View.GONE);
            if (running) {
                edt_search_proposal.setVisibility(View.GONE);
                if (autoMandateTypeSelected.equalsIgnoreCase("Policy Number")) {
                    txterrordesc.setText("");
                    selectedAdapterAutoMandateList = new SelectedAdapterAutoMandateList(autoMandateGlobalList);
                    recyclerviewAutoMandate.setAdapter(selectedAdapterAutoMandateList);
                    recyclerviewAutoMandate.invalidate();
                    //edt_search_proposal.setVisibility(View.VISIBLE);
                } else if (autoMandateTypeSelected.equalsIgnoreCase("Month")) {
                    txterrordesc.setText("");
                    linearLayoutExternalResultMandatePenetration.setVisibility(View.VISIBLE);
                    TextView textViewAutoMandatePenetrationMainTitle = findViewById(R.id.textViewAutoMandatePenetrationMainTitle);
                    TextView textViewAutoMandatePenetrationMandateReceivedTitle = findViewById(R.id.textViewAutoMandatePenetrationMandateReceivedTitle);
                    TextView textViewAutoMandatePercentageTitle = findViewById(R.id.textViewAutoMandatePercentageTitle);
                    TextView textViewAutoMandatePenetration = findViewById(R.id.textViewAutoMandatePenetration);
                    TextView textViewAutoMandatePenetrationMandateReceived = findViewById(R.id.textViewAutoMandatePenetrationMandateReceived);
                    TextView textViewAutoMandatePenetrationPolicyIssued = findViewById(R.id.textViewAutoMandatePenetrationPolicyIssued);

                    if (autoMandateStatusSelected.equalsIgnoreCase("Mandate Penetration")) {
                        textViewAutoMandatePenetrationMainTitle.setText("Mandate Penetration");
                        textViewAutoMandatePenetrationMandateReceivedTitle.setText("Mandate Received");
                        textViewAutoMandatePercentageTitle.setText("Mandate Received %");
                    } else if (autoMandateStatusSelected.equalsIgnoreCase("Mandate Registered")) {
                        textViewAutoMandatePenetrationMainTitle.setText("Mandate Registered");
                        textViewAutoMandatePenetrationMandateReceivedTitle.setText("Mandate Registered");
                        textViewAutoMandatePercentageTitle.setText("Mandate Registered %");
                    } else if (autoMandateStatusSelected.equalsIgnoreCase("Mandate Rejected")) {
                        textViewAutoMandatePenetrationMainTitle.setText("Mandate Rejected");
                        textViewAutoMandatePenetrationMandateReceivedTitle.setText("Mandate Rejected");
                        textViewAutoMandatePercentageTitle.setText("Mandate Rejected %");
                    } else if (autoMandateStatusSelected.equalsIgnoreCase("Mandate Not Received")) {
                        textViewAutoMandatePenetrationMainTitle.setText("Mandate Not Received");
                        textViewAutoMandatePenetrationMandateReceivedTitle.setText("Mandate Not Received");
                        textViewAutoMandatePercentageTitle.setText("Mandate Not Received %");
                    }
                    String penetration = "";

                    if (policyIssued != 0) {
                        double dbl_per = ((double) (mandateCount / policyIssued)) * 100;
                        penetration = String.format("%.2f", dbl_per);
                    } else {
                        penetration = "0.0";
                    }
                    textViewAutoMandatePenetrationPolicyIssued.setText(Math.round(policyIssued) + "");
                    textViewAutoMandatePenetrationMandateReceived.setText(Math.round(mandateCount) + "");
                    textViewAutoMandatePenetration.setText((int) Math.round(Double.parseDouble(penetration)) + "%");

                } else {
                    txterrordesc.setText("No record found");
    }


            } else {
                txterrordesc.setText("No record found");
            }
        }
    }

    private void service_hits( String METHOD_NAME,String input) {


        service = new ServiceHits(context,
                METHOD_NAME, input,
                strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.textViewFromDate:
                datecheck = 1;
                showDateDialog();
                break;

            case R.id.textViewToDate:
                datecheck = 2;
                showDateDialog();
                break;
            case R.id.buttonAutoMandateStatus:
                commonMethods.hideKeyboard(edittextAutoMandateStatus,context);

                txterrordesc.setVisibility(View.GONE);
                linearLayoutExternalResultMandatePenetration.setVisibility(View.GONE);
                clearList();

                if(commonMethods.isNetworkConnected(context)){
                    StringBuilder input = new StringBuilder();
                    input.append(autoMandateTypeSelected);

                    if (autoMandateTypeSelected.equalsIgnoreCase("Policy Number")) {
                        policyNumber = edittextAutoMandateStatus.getText().toString();

                    if(!TextUtils.isEmpty(policyNumber)){
                            input.append(",").append(policyNumber);
                            service_hits( METHOD_NAME_AUTO_MANDATE_STATUS_LIST,input.toString());
                        } else {
                        commonMethods.showMessageDialog(context,"Please Enter Policy Number");
                    }
                    } else if (autoMandateTypeSelected.equalsIgnoreCase("Month")) {

                        if (!autoMandateStatusSelected.equalsIgnoreCase("Select Mandate Status")) {


                            fromDate = textViewFromDate.getText().toString();
                            toDate = textViewToDate.getText().toString();
                            input.append(",").append(autoMandateStatusSelected).append(",").
                                    append(fromDate).append(",").append(toDate);

                            if (TextUtils.isEmpty(toDate) || TextUtils.isEmpty(fromDate)) {
                                commonMethods.showMessageDialog(context, "Please Select Dates");
                            } else {
                                final SimpleDateFormat formatter = new SimpleDateFormat(
                                        "dd-MMMM-yyyy");
                                SimpleDateFormat finalDateFormat = new SimpleDateFormat("dd-MMM-yyyy");

                                Date d1 = null, d2 = null;
                                try {
                                    d1 = formatter.parse(fromDate);
                                    fromDate = finalDateFormat.format(d1);
                                    fromDate = fromDate.toUpperCase();

                                    d2 = formatter.parse(toDate);
                                    toDate = finalDateFormat.format(d2);
                                    toDate = toDate.toUpperCase();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                long difference = Math.abs(d1.getTime() - d2.getTime());
                                long differenceDates = difference / (24 * 60 * 60 * 1000);
                                System.out.println("dayDifference:" + differenceDates);
                                if (differenceDates <= 31) {

                                    if ((d2.after(d1)) || (d2.equals(d1))) {
                                        service_hits(METHOD_NAME_AUTO_MANDATE_PENETRATION_STATUS_LIST,input.toString());
                                    } else {
                                        commonMethods.showMessageDialog(context, "To date should be greater than From date");
                }
                                } else {
                                    commonMethods.showMessageDialog(context, "Difference between To date and From date should not be more than 1 month");
                                }
                            }
                        } else {
                            commonMethods.showMessageDialog(context, "Please Select Mandate Status");
                        }

                    } else {
                        commonMethods.showMessageDialog(context, "Please Select Type");
                    }

                } else {
                    commonMethods.showMessageDialog(context,commonMethods.NO_INTERNET_MESSAGE);
                }
                break;
        }
    }

    private void showDateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(
                context,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                mYear, mMonth, mDay);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void updateDisplay(int year, int month, int day) {

        String y = String.valueOf(year);
        String m = String.valueOf(month + 1);
        String da = String.valueOf(day);
        // String totaldate = m + "-" + da + "-" + y;

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

        if (datecheck == 1) {
            textViewFromDate.setText(totaldate);
        } else if (datecheck == 2) {
            textViewToDate.setText(totaldate);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Spinner spinnerSelected = (Spinner) adapterView;
        switch (spinnerSelected.getId()) {
            case R.id.spinnerAutoMandateType:
                autoMandateTypeSelected = spinnerSelected.getSelectedItem().toString();
                linearLayoutExternalResultMandatePenetration.setVisibility(View.GONE);
                txterrordesc.setVisibility(View.GONE);
                edittextAutoMandateStatus.setText("");
                spinnerAuotMandatePenetrationStatus.setSelection(0);
                spinnerAuotMandatePenetrationMonth.setSelection(0);

                if (autoMandateTypeSelected.equalsIgnoreCase("Policy Number")) {
                    linearLayoutAutomandatePolicyNumber.setVisibility(View.VISIBLE);
                    linearLayoutAutomandatePenetrationMonth.setVisibility(View.GONE);
                    linearLayoutAutomandatePenetrationStatus.setVisibility(View.GONE);

                } else if (autoMandateTypeSelected.equalsIgnoreCase("Month")) {
                    linearLayoutAutomandatePolicyNumber.setVisibility(View.GONE);
                    linearLayoutAutomandatePenetrationMonth.setVisibility(View.VISIBLE);
                    linearLayoutAutomandatePenetrationStatus.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutAutomandatePolicyNumber.setVisibility(View.GONE);
                    linearLayoutAutomandatePenetrationMonth.setVisibility(View.GONE);
                    linearLayoutAutomandatePenetrationStatus.setVisibility(View.GONE);
                }
                break;

            case R.id.spinnerAuotMandatePenetrationStatus:
                autoMandateStatusSelected = spinnerSelected.getSelectedItem().toString();
                break;

            case R.id.spinnerAuotMandatePenetrationMonth:

                int monthSelected = spinnerSelected.getSelectedItemPosition();
                if (monthSelected != 0) {

                    SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM-yyyy");
                    Calendar calen = Calendar.getInstance();

                    calen.set(Calendar.MONTH, (monthSelected - 1));
                    calen.set(Calendar.DATE,
                            calen.getActualMaximum(Calendar.DATE));
                    Date lastDateOfMonth = calen.getTime();
                    toDate = format1.format(lastDateOfMonth);
                    toDate = toDate.toUpperCase();
                    calen.set(Calendar.DATE,
                            calen.getActualMinimum(Calendar.DATE));
                    Date firstDateOfMonth = calen.getTime();
                    fromDate = format1.format(firstDateOfMonth);
                    fromDate = fromDate.toUpperCase();
                }

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void clearList() {
        if (autoMandateGlobalList != null && selectedAdapterAutoMandateList != null) {
            autoMandateGlobalList.clear();
            selectedAdapterAutoMandateList = new SelectedAdapterAutoMandateList(autoMandateGlobalList);
            recyclerviewAutoMandate.setAdapter(selectedAdapterAutoMandateList);
            recyclerviewAutoMandate.invalidate();
        }
    }

    public class SelectedAdapterAutoMandateList extends RecyclerView.Adapter<SelectedAdapterAutoMandateList.
            ViewHolderAdapter> implements Filterable {

        private ArrayList<ParseXML.AutoMandateStatusListValuesModel>  lstAdapterList, lstSearch;

        SelectedAdapterAutoMandateList(ArrayList<ParseXML.AutoMandateStatusListValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<ParseXML.AutoMandateStatusListValuesModel> results = new ArrayList<>();

                    if (lstSearch == null)
                        lstSearch = lstAdapterList;

                    String strSearch = charSequence == null ? "":charSequence.toString();

                    if (!strSearch.equals("")) {
                        if (lstSearch != null && lstSearch.size() > 0) {
                            for (final ParseXML.AutoMandateStatusListValuesModel model : lstSearch) {
                                if (model.getPOLICY_NO().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getSTATUS_UPDATE_DATE().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getPOLICY_STATUS().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getPOL_HOLDER_NAME().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                    results.add(model);
                                }
                            }
                        }
                        oReturn.values = results;
                    }else {
                        oReturn.values = autoMandateGlobalList;
                    }
                    return oReturn;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    lstAdapterList = (ArrayList<ParseXML.AutoMandateStatusListValuesModel>) results.values;
                    selectedAdapterAutoMandateList = new SelectedAdapterAutoMandateList(lstAdapterList);
                    recyclerviewAutoMandate.setAdapter(selectedAdapterAutoMandateList);

                    notifyDataSetChanged();
                }
            };}


        @Override
        public int getItemCount() {
            return lstAdapterList.size();
        }

        @Override
        public ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            // infalte the item Layout
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_item_auto_mandate_status, parent, false);

            // set the view's size, margins, paddings and layout parameters
            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(ViewHolderAdapter holder, int position) {
            String title = "Rejection Reason (for rejected cases) :";
            holder.textviewAutoMandateStatusListRejectionReasonTitle.setText(title);

            holder.textviewAutoMandateStatusListPolicyNumber
                    .setText(lstAdapterList.get(position).getPOLICY_NO() == null ? ""
                            : lstAdapterList.get(position).getPOLICY_NO());

            holder.textviewAutoMandateStatusListMandateType.setText(lstAdapterList.get(position).getMANDATE_TYPE() == null ? "" : lstAdapterList.get(
                    position).getMANDATE_TYPE());
            holder.textviewAutoMandateStatusListMandateStatus.setText(lstAdapterList.get(position).getMANDATE_STATUS() == null ? "" : lstAdapterList.get(
                    position).getMANDATE_STATUS());

            holder.textviewAutoMandateStatusListPolicyHolderName.setText(lstAdapterList.get(position).getPOL_HOLDER_NAME() == null ? ""
                    : lstAdapterList.get(position).getPOL_HOLDER_NAME());
            holder.textviewAutoMandateStatusListPolicyStatus.setText(lstAdapterList.get(position).getPOLICY_STATUS() == null ? ""
                    : lstAdapterList.get(position).getPOLICY_STATUS());
            holder.textviewAutoMandateStatusListPolicyStatusUpdate.setText(lstAdapterList.get(position).getSTATUS_UPDATE_DATE() == null ? ""
                    : lstAdapterList.get(position).getSTATUS_UPDATE_DATE());
            holder.textviewAutoMandateStatusListRejectionReason.setText(lstAdapterList.get(position).getREJECTION_REASON() == null ? ""
                    : lstAdapterList.get(position).getREJECTION_REASON());
        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder/* implements View.OnCreateContextMenuListener,
                MenuItem.OnMenuItemClickListener*/ {

            private final TextView textviewAutoMandateStatusListPolicyNumber,textviewAutoMandateStatusListMandateType ,
                    textviewAutoMandateStatusListMandateStatus,textviewAutoMandateStatusListPolicyHolderName,
                    textviewAutoMandateStatusListPolicyStatus,textviewAutoMandateStatusListPolicyStatusUpdate,
                    textviewAutoMandateStatusListRejectionReason,textviewAutoMandateStatusListRejectionReasonTitle;

            ViewHolderAdapter(View v) {
                super(v);
                textviewAutoMandateStatusListPolicyNumber = v.findViewById(R.id.textviewAutoMandateStatusListPolicyNumber);
                textviewAutoMandateStatusListMandateType = v.findViewById(R.id.textviewAutoMandateStatusListMandateType);
                textviewAutoMandateStatusListMandateStatus = v.findViewById(R.id.textviewAutoMandateStatusListMandateStatus);
                textviewAutoMandateStatusListPolicyHolderName = v.findViewById(R.id.textviewAutoMandateStatusListPolicyHolderName);

                textviewAutoMandateStatusListPolicyStatus = v.findViewById(R.id.textviewAutoMandateStatusListPolicyStatus);
                textviewAutoMandateStatusListPolicyStatusUpdate = v.findViewById(R.id.textviewAutoMandateStatusListPolicyStatusUpdate);
                textviewAutoMandateStatusListRejectionReason = v.findViewById(R.id.textviewAutoMandateStatusListRejectionReason);
                textviewAutoMandateStatusListRejectionReasonTitle = v.findViewById(R.id.textviewAutoMandateStatusListRejectionReasonTitle);

                //  v.setOnCreateContextMenuListener(this);
            }
           /* @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("Select Action");
                MenuItem myActionItem = menu.add("Penetration Details");
                myActionItem.setOnMenuItemClickListener(this);
        }

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Menu Item Clicked!
                System.out.println("item = " + item.getTitle() + " info.position:" + getAdapterPosition());
                if (item.getTitle() == "Penetration Details") {


                    Intent intent = new Intent(context, AutoMandatePenetrationDetailsActivity.class);
                    intent.putExtra("strFromDate", fromDate);
                    intent.putExtra("strToDate",toDate);
                    intent.putExtra("strType",autoMandateTypeSelected);
                    startActivity(intent);
                }
                return true;
            }*/
        }


    }

    @Override
    protected void onDestroy() {
        killTasks();
        super.onDestroy();
    }


    private void killTasks(){
        if(mProgressDialog!=null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if(autoMandateStatusListAsync!=null){
            autoMandateStatusListAsync.cancel(true);
        }

        if (autoMandatePenetrationStatusAsync != null) {
            autoMandatePenetrationStatusAsync.cancel(true);
        }

        if(service!=null){
            service.cancel(true);
        }
    }


}
