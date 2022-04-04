package sbilife.com.pointofsale_bancaagency.reports.policyservicing;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

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
import java.util.Locale;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class UnrealizedDataActivity extends AppCompatActivity implements View.OnClickListener, ServiceHits.DownLoadData {

    private final String METHOD_NAME = "getOPSHierarchyBranchUnrealisedData";

    private transient CommonMethods commonMethods;
    private Context context;
    private String strCIFBDMUserId = "", strCIFBDMEmailId = "",
            strCIFBDMPassword = "", strCIFBDMMObileNo = "";
    private TextView textViewFromDate, textViewToDate, textviewRecordCount;

    private EditText edittextSearch;
    private RecyclerView recyclerview;
    private int mYear, mMonth, mDay, datecheck = 0;
    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            mYear = year;
            mMonth = month;
            mDay = day;
            updateDisplay(mYear, mMonth, mDay);
        }
    };
    private ServiceHits service;
    private String fromDate = "", toDate = "";
    private ProgressDialog mProgressDialog;
    private SelectedAdapter selectedAdapter;
    private ArrayList<UnrealisedDataValuesModel> globalList;
    private UnrealisedDataAsync unrealisedDataAsync;
    private RelativeLayout llHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_unrealized_data);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Unrealized Data");
        textViewFromDate = findViewById(R.id.textViewFromDate);
        textViewToDate = findViewById(R.id.textViewToDate);
        textviewRecordCount = findViewById(R.id.textviewRecordCount);
        Button buttonOk = findViewById(R.id.buttonOk);
        edittextSearch = findViewById(R.id.edittextSearch);
        recyclerview = findViewById(R.id.recyclerview);
        llHeader = findViewById(R.id.llHeader);
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);


        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");
        if (fromHome != null && fromHome.equalsIgnoreCase("Y")) {
            getUserDetails();
        } else {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");
            try {
                strCIFBDMPassword = commonMethods.getStrAuth();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerview.setLayoutManager(linearLayoutManager);

        globalList = new ArrayList<>();
        selectedAdapter = new SelectedAdapter(globalList);
        recyclerview.setAdapter(selectedAdapter);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        textViewFromDate.setOnClickListener(this);
        textViewToDate.setOnClickListener(this);
        buttonOk.setOnClickListener(this);
        setDates();

        edittextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                selectedAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = commonMethods.getStrAuth();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }

    private void setDates() {
        textViewFromDate.setText(commonMethods.getPreviousMonthDate());
        textViewToDate.setText(commonMethods.getCurrentMonthDate());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.textViewFromDate:
                datecheck = 1;
                showDateDialog();
                break;

            case R.id.textViewToDate:
                datecheck = 2;
                showDateDialog();
                break;
            case R.id.buttonOk:
                clearList();

                if (commonMethods.isNetworkConnected(context)) {
                    StringBuilder input = new StringBuilder();

                    fromDate = textViewFromDate.getText().toString();
                    toDate = textViewToDate.getText().toString();
                    input.append(fromDate).append(",").append(toDate);

                    if (TextUtils.isEmpty(toDate) || TextUtils.isEmpty(fromDate)) {
                        commonMethods.showMessageDialog(context, "Please Select Dates");
                    } else {
                        final SimpleDateFormat formatter = new SimpleDateFormat(
                                "dd-MMMM-yyyy", Locale.ENGLISH);
                        SimpleDateFormat finalDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

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

                        // if (differenceDates <= 181) {

                        if ((d2.after(d1)) || (d2.equals(d1))) {
                            service_hits(input.toString());
                        } else {
                            commonMethods.showMessageDialog(context, "To date should be greater than From date");
                        }
                       /* } else {
                            commonMethods.showMessageDialog(context, "Difference between To date and From date should not be more than 6 month");
                        }*/
                    }

                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }
                break;
        }
    }

    private void service_hits(String input) {
        service = new ServiceHits(context,
                METHOD_NAME, input,
                strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();
    }

    @Override
    public void downLoadData() {

        unrealisedDataAsync = new UnrealisedDataAsync();
        unrealisedDataAsync.execute();
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


        String monthFormatted = commonMethods.getFullMonthName(m);
        String totaldate = da + "-" + monthFormatted + "-" + y;

        if (datecheck == 1) {
            textViewFromDate.setText(totaldate);
        } else if (datecheck == 2) {
            textViewToDate.setText(totaldate);
        }
    }

    private void clearList() {
        edittextSearch.setVisibility(View.GONE);
        llHeader.setVisibility(View.GONE);
        if (globalList != null && selectedAdapter != null) {
            textviewRecordCount.setText("");
            globalList.clear();
            selectedAdapter = new SelectedAdapter(globalList);
            recyclerview.setAdapter(selectedAdapter);
            recyclerview.invalidate();
        }
    }

    @Override
    protected void onDestroy() {
        killTasks();
        super.onDestroy();
    }

    private void killTasks() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (unrealisedDataAsync != null) {
            unrealisedDataAsync.cancel(true);
        }

        if (service != null) {
            service.cancel(true);
        }
    }

    public ArrayList<UnrealisedDataValuesModel> parseNodeUnrealisedData(List<String> lsNode) {
        ArrayList<UnrealisedDataValuesModel> lsData = new ArrayList<>();
        ParseXML parseXML = new ParseXML();
        String POLICYPROPOSALNUMBER, POLICYNUMBER, PRODUCTNAME, POLICYCURRENTSTATUS,
                INSTALLMENT_PREMIUM, SERVICEBRANCHNAME, DRAWN_BANK, DRAWN_BRANCH, COLLECTION_BANK, MONEY_IN_DATE,
                INSTRUMENT_NUMBER, MONEY_IN_AMOUNT, CHEQUE_DATE, SBIL_BRANCH, PAYMENT_TYPE, RESIDUAL_AMOUNT, PAYMENT_MODE,
                CHEQUE_TYPE, SOURCE_ID, CASH_ENTRY_DATE, PAYMENTVALID, CUSTOMER_NAME, PREMIUMGROSSAMOUNT, FREQUENCY,
                DOC, PREMIUMFUP, ALTERNATEMODE, CONTACT_NUMBER_MOBILE, SERVICEREGIONNAME, SERVICEREGIONNAMEOPS,
                POLICYSUMASSURED, RAG_FLAG, TIER1_MKT_NAME, TIER2_MKT_NAME, TIERROC_MKT_NAME,
                POLICY_MATURITY_DATE, ANNUALIZEDPREMIUM, POLICY_TERM, POLICYPAYMENTTERM, IA_CIF_CODE, IA_CIF_NAME,
                UM_BANKNAME, CHANNEL_NAME, CHANNEL_ACTIVE_STATUS, POLICYPAYMENTMECHANISM, CUSTOMERID, HOLDERGENDER,
                POLICYISSUEDATE, UMBANKCODE, CHANNELTYPE, BRANCHAREA, BRANCHDIVISION, POLICYTYPE, POLICYTYPESUB,
                RESIDUALAMOUNT,
                CUSTOMERBANKACCOUNTNO;
        for (String Node : lsNode) {

            POLICYNUMBER = parseXML.parseXmlTag(Node, "POLICYNUMBER");
            POLICYNUMBER = POLICYNUMBER == null ? "" : POLICYNUMBER;

            POLICYPROPOSALNUMBER = parseXML.parseXmlTag(Node, "POLICYPROPOSALNUMBER");
            POLICYPROPOSALNUMBER = POLICYPROPOSALNUMBER == null ? "" : POLICYPROPOSALNUMBER;

            PRODUCTNAME = parseXML.parseXmlTag(Node, "PRODUCTNAME");
            PRODUCTNAME = PRODUCTNAME == null ? "" : PRODUCTNAME;

            INSTALLMENT_PREMIUM = parseXML.parseXmlTag(Node, "INSTALLMENT_PREMIUM");
            INSTALLMENT_PREMIUM = INSTALLMENT_PREMIUM == null ? "" : INSTALLMENT_PREMIUM;

            SERVICEBRANCHNAME = parseXML.parseXmlTag(Node, "SERVICEBRANCHNAME");
            SERVICEBRANCHNAME = SERVICEBRANCHNAME == null ? "" : SERVICEBRANCHNAME;

            DRAWN_BANK = parseXML.parseXmlTag(Node, "DRAWN_BANK");
            DRAWN_BANK = DRAWN_BANK == null ? "" : DRAWN_BANK;

            DRAWN_BRANCH = parseXML.parseXmlTag(Node, "DRAWN_BRANCH");
            DRAWN_BRANCH = DRAWN_BRANCH == null ? "" : DRAWN_BRANCH;

            COLLECTION_BANK = parseXML.parseXmlTag(Node, "COLLECTION_BANK");
            COLLECTION_BANK = COLLECTION_BANK == null ? "" : COLLECTION_BANK;

            MONEY_IN_DATE = parseXML.parseXmlTag(Node, "MONEY_IN_DATE");
            MONEY_IN_DATE = MONEY_IN_DATE == null ? "" : MONEY_IN_DATE;

            INSTRUMENT_NUMBER = parseXML.parseXmlTag(Node, "INSTRUMENT_NUMBER");
            INSTRUMENT_NUMBER = INSTRUMENT_NUMBER == null ? "" : INSTRUMENT_NUMBER;

            MONEY_IN_AMOUNT = parseXML.parseXmlTag(Node, "MONEY_IN_AMOUNT");
            MONEY_IN_AMOUNT = MONEY_IN_AMOUNT == null ? "" : MONEY_IN_AMOUNT;

            CHEQUE_DATE = parseXML.parseXmlTag(Node, "CHEQUE_DATE");
            CHEQUE_DATE = CHEQUE_DATE == null ? "" : CHEQUE_DATE;

            SBIL_BRANCH = parseXML.parseXmlTag(Node, "SBIL_BRANCH");
            SBIL_BRANCH = SBIL_BRANCH == null ? "" : SBIL_BRANCH;

            PAYMENT_TYPE = parseXML.parseXmlTag(Node, "PAYMENT_TYPE");
            PAYMENT_TYPE = PAYMENT_TYPE == null ? "" : PAYMENT_TYPE;

            RESIDUAL_AMOUNT = parseXML.parseXmlTag(Node, "RESIDUAL_AMOUNT");
            RESIDUAL_AMOUNT = RESIDUAL_AMOUNT == null ? "" : RESIDUAL_AMOUNT;

            PAYMENT_MODE = parseXML.parseXmlTag(Node, "PAYMENT_MODE");
            PAYMENT_MODE = PAYMENT_MODE == null ? "" : PAYMENT_MODE;

            CHEQUE_TYPE = parseXML.parseXmlTag(Node, "CHEQUE_TYPE");
            CHEQUE_TYPE = CHEQUE_TYPE == null ? "" : CHEQUE_TYPE;

            SOURCE_ID = parseXML.parseXmlTag(Node, "SOURCE_ID");
            SOURCE_ID = SOURCE_ID == null ? "" : SOURCE_ID;

            CASH_ENTRY_DATE = parseXML.parseXmlTag(Node, "CASH_ENTRY_DATE");
            CASH_ENTRY_DATE = CASH_ENTRY_DATE == null ? "" : CASH_ENTRY_DATE;

            PAYMENTVALID = parseXML.parseXmlTag(Node, "PAYMENTVALID");
            PAYMENTVALID = PAYMENTVALID == null ? "" : PAYMENTVALID;

            CUSTOMER_NAME = parseXML.parseXmlTag(Node, "CUSTOMER_NAME");
            CUSTOMER_NAME = CUSTOMER_NAME == null ? "" : CUSTOMER_NAME;

            PREMIUMGROSSAMOUNT = parseXML.parseXmlTag(Node, "PREMIUMGROSSAMOUNT");
            PREMIUMGROSSAMOUNT = PREMIUMGROSSAMOUNT == null ? "" : PREMIUMGROSSAMOUNT;

            FREQUENCY = parseXML.parseXmlTag(Node, "FREQUENCY");
            FREQUENCY = FREQUENCY == null ? "" : FREQUENCY;

            DOC = parseXML.parseXmlTag(Node, "DOC");
            DOC = DOC == null ? "" : DOC;

            PREMIUMFUP = parseXML.parseXmlTag(Node, "PREMIUMFUP");
            PREMIUMFUP = PREMIUMFUP == null ? "" : PREMIUMFUP;

            ALTERNATEMODE = parseXML.parseXmlTag(Node, "ALTERNATEMODE");
            ALTERNATEMODE = ALTERNATEMODE == null ? "" : ALTERNATEMODE;

            CONTACT_NUMBER_MOBILE = parseXML.parseXmlTag(Node, "CONTACT_NUMBER_MOBILE");
            CONTACT_NUMBER_MOBILE = CONTACT_NUMBER_MOBILE == null ? "" : CONTACT_NUMBER_MOBILE;

            SERVICEREGIONNAME = parseXML.parseXmlTag(Node, "SERVICEREGIONNAME");
            SERVICEREGIONNAME = SERVICEREGIONNAME == null ? "" : SERVICEREGIONNAME;

            POLICYCURRENTSTATUS = parseXML.parseXmlTag(Node, "POLICYCURRENTSTATUS");
            POLICYCURRENTSTATUS = POLICYCURRENTSTATUS == null ? "" : POLICYCURRENTSTATUS;

            SERVICEREGIONNAMEOPS = parseXML.parseXmlTag(Node, "SERVICEREGIONNAMEOPS");
            SERVICEREGIONNAMEOPS = SERVICEREGIONNAMEOPS == null ? "" : SERVICEREGIONNAMEOPS;

            POLICYSUMASSURED = parseXML.parseXmlTag(Node, "POLICYSUMASSURED");
            POLICYSUMASSURED = POLICYSUMASSURED == null ? "" : POLICYSUMASSURED;

            RAG_FLAG = parseXML.parseXmlTag(Node, "RAG_FLAG");
            RAG_FLAG = RAG_FLAG == null ? "" : RAG_FLAG;

            TIER1_MKT_NAME = parseXML.parseXmlTag(Node, "TIER1_MKT_NAME");
            TIER1_MKT_NAME = TIER1_MKT_NAME == null ? "" : TIER1_MKT_NAME;

            TIER2_MKT_NAME = parseXML.parseXmlTag(Node, "TIER2_MKT_NAME");
            TIER2_MKT_NAME = TIER2_MKT_NAME == null ? "" : TIER2_MKT_NAME;

            TIERROC_MKT_NAME = parseXML.parseXmlTag(Node, "TIERROC_MKT_NAME");
            TIERROC_MKT_NAME = TIERROC_MKT_NAME == null ? "" : TIERROC_MKT_NAME;

            POLICY_MATURITY_DATE = parseXML.parseXmlTag(Node, "POLICY_MATURITY_DATE");
            POLICY_MATURITY_DATE = POLICY_MATURITY_DATE == null ? "" : POLICY_MATURITY_DATE;

            ANNUALIZEDPREMIUM = parseXML.parseXmlTag(Node, "ANNUALIZEDPREMIUM");
            ANNUALIZEDPREMIUM = ANNUALIZEDPREMIUM == null ? "" : ANNUALIZEDPREMIUM;

            POLICY_TERM = parseXML.parseXmlTag(Node, "POLICY_TERM");
            POLICY_TERM = POLICY_TERM == null ? "" : POLICY_TERM;

            POLICYPAYMENTTERM = parseXML.parseXmlTag(Node, "POLICYPAYMENTTERM");
            POLICYPAYMENTTERM = POLICYPAYMENTTERM == null ? "" : POLICYPAYMENTTERM;

            IA_CIF_CODE = parseXML.parseXmlTag(Node, "IA_CIF_CODE");
            IA_CIF_CODE = IA_CIF_CODE == null ? "" : IA_CIF_CODE;

            IA_CIF_NAME = parseXML.parseXmlTag(Node, "IA_CIF_NAME");
            IA_CIF_NAME = IA_CIF_NAME == null ? "" : IA_CIF_NAME;

            UM_BANKNAME = parseXML.parseXmlTag(Node, "UM_BANKNAME");
            UM_BANKNAME = UM_BANKNAME == null ? "" : UM_BANKNAME;

            CHANNEL_NAME = parseXML.parseXmlTag(Node, "CHANNEL_NAME");
            CHANNEL_NAME = CHANNEL_NAME == null ? "" : CHANNEL_NAME;

            CHANNEL_ACTIVE_STATUS = parseXML.parseXmlTag(Node, "CHANNEL_ACTIVE_STATUS");
            CHANNEL_ACTIVE_STATUS = CHANNEL_ACTIVE_STATUS == null ? "" : CHANNEL_ACTIVE_STATUS;

            POLICYPAYMENTMECHANISM = parseXML.parseXmlTag(Node, "POLICYPAYMENTMECHANISM");
            POLICYPAYMENTMECHANISM = POLICYPAYMENTMECHANISM == null ? "" : POLICYPAYMENTMECHANISM;

            CUSTOMERID = parseXML.parseXmlTag(Node, "CUSTOMERID");
            CUSTOMERID = CUSTOMERID == null ? "" : CUSTOMERID;

            HOLDERGENDER = parseXML.parseXmlTag(Node, "HOLDERGENDER");
            HOLDERGENDER = HOLDERGENDER == null ? "" : HOLDERGENDER;

            POLICYISSUEDATE = parseXML.parseXmlTag(Node, "POLICYISSUEDATE");
            POLICYISSUEDATE = POLICYISSUEDATE == null ? "" : POLICYISSUEDATE;

            UMBANKCODE = parseXML.parseXmlTag(Node, "UMBANKCODE");
            UMBANKCODE = UMBANKCODE == null ? "" : UMBANKCODE;

            CHANNELTYPE = parseXML.parseXmlTag(Node, "CHANNELTYPE");
            CHANNELTYPE = CHANNELTYPE == null ? "" : CHANNELTYPE;

            BRANCHAREA = parseXML.parseXmlTag(Node, "BRANCHAREA");
            BRANCHAREA = BRANCHAREA == null ? "" : BRANCHAREA;

            BRANCHDIVISION = parseXML.parseXmlTag(Node, "BRANCHDIVISION");
            BRANCHDIVISION = BRANCHDIVISION == null ? "" : BRANCHDIVISION;

            POLICYTYPE = parseXML.parseXmlTag(Node, "POLICYTYPE");
            POLICYTYPE = POLICYTYPE == null ? "" : POLICYTYPE;

            POLICYTYPESUB = parseXML.parseXmlTag(Node, "POLICYTYPESUB");
            POLICYTYPESUB = POLICYTYPESUB == null ? "" : POLICYTYPESUB;

            RESIDUALAMOUNT = parseXML.parseXmlTag(Node, "RESIDUALAMOUNT");
            RESIDUALAMOUNT = RESIDUALAMOUNT == null ? "" : RESIDUALAMOUNT;

            CUSTOMERBANKACCOUNTNO = parseXML.parseXmlTag(Node, "CUSTOMERBANKACCOUNTNO");
            CUSTOMERBANKACCOUNTNO = CUSTOMERBANKACCOUNTNO == null ? "" : CUSTOMERBANKACCOUNTNO;

            UnrealisedDataValuesModel nodeVal = new UnrealisedDataValuesModel(POLICYPROPOSALNUMBER, POLICYNUMBER, PRODUCTNAME, POLICYCURRENTSTATUS, INSTALLMENT_PREMIUM, SERVICEBRANCHNAME, DRAWN_BANK, DRAWN_BRANCH, COLLECTION_BANK, MONEY_IN_DATE, INSTRUMENT_NUMBER, MONEY_IN_AMOUNT, CHEQUE_DATE, SBIL_BRANCH, PAYMENT_TYPE, RESIDUAL_AMOUNT, PAYMENT_MODE, CHEQUE_TYPE, SOURCE_ID, CASH_ENTRY_DATE, PAYMENTVALID, CUSTOMER_NAME, PREMIUMGROSSAMOUNT, FREQUENCY, DOC, PREMIUMFUP, ALTERNATEMODE, CONTACT_NUMBER_MOBILE, SERVICEREGIONNAME, SERVICEREGIONNAMEOPS, POLICYSUMASSURED, RAG_FLAG, TIER1_MKT_NAME, TIER2_MKT_NAME, TIERROC_MKT_NAME, POLICY_MATURITY_DATE, ANNUALIZEDPREMIUM, POLICY_TERM, POLICYPAYMENTTERM, IA_CIF_CODE, IA_CIF_NAME, UM_BANKNAME, CHANNEL_NAME, CHANNEL_ACTIVE_STATUS, POLICYPAYMENTMECHANISM, CUSTOMERID, HOLDERGENDER, POLICYISSUEDATE, UMBANKCODE, CHANNELTYPE, BRANCHAREA, BRANCHDIVISION, POLICYTYPE, POLICYTYPESUB, RESIDUALAMOUNT, CUSTOMERBANKACCOUNTNO);
            lsData.add(nodeVal);
        }
        return lsData;
    }

    class UnrealisedDataAsync extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                //getOPSHierarchyBranchUnrealisedData(string fromdate, string todate, string brcode,
                // string strEmailId, string strMobileNo, string strAuthKey)

                running = true;
                SoapObject request;
                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("fromdate", fromDate);
                request.addProperty("todate", toDate);
                request.addProperty("brcode", strCIFBDMUserId);
                commonMethods.appendSecurityParams(context, request, strCIFBDMEmailId, strCIFBDMMObileNo);

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;
                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                androidHttpTranport.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();

                if (!response.toString().contentEquals("<CIFPolicyList />")) {
                    System.out.println("response:" + response.toString());
                    SoapPrimitive sa;
                    sa = (SoapPrimitive) envelope.getResponse();

                    String inputpolicylist = sa.toString();

                    ParseXML prsObj = new ParseXML();

                    inputpolicylist = prsObj.parseXmlTag(
                            inputpolicylist, "CIFPolicyList");
                    inputpolicylist = prsObj.parseXmlTag(
                            inputpolicylist, "ScreenData");
                    error = inputpolicylist;

                    if (error == null) {
                        // for agent policy list

                        inputpolicylist = sa.toString();
                        inputpolicylist = prsObj.parseXmlTag(
                                inputpolicylist, "CIFPolicyList");

                        List<String> Node = prsObj.parseParentNode(
                                inputpolicylist, "Table");

                        List<UnrealisedDataValuesModel> nodeData = parseNodeUnrealisedData(Node);
                        globalList.clear();
                        globalList.addAll(nodeData);
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
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            textviewRecordCount.setVisibility(View.VISIBLE);
            if (running) {
                edittextSearch.setVisibility(View.GONE);
                llHeader.setVisibility(View.GONE);


                if (error == null) {
                    edittextSearch.setVisibility(View.VISIBLE);
                    llHeader.setVisibility(View.VISIBLE);
                    selectedAdapter = new SelectedAdapter(globalList);
                    recyclerview.setAdapter(selectedAdapter);
                    recyclerview.invalidate();
                    String count = "Total Count : " + globalList.size();
                    textviewRecordCount.setText(count);
                } else {
                    textviewRecordCount.setText("");
                    commonMethods.showMessageDialog(context, "No Record Found");
                    clearList();
                }
            } else {
                textviewRecordCount.setText("");
                commonMethods.showMessageDialog(context, "No Record Found");
                clearList();
            }
        }
    }

    public class SelectedAdapter extends RecyclerView.Adapter<SelectedAdapter.
            ViewHolderAdapter> implements Filterable {

        private ArrayList<UnrealisedDataValuesModel> lstAdapterList, lstSearch;

        SelectedAdapter(ArrayList<UnrealisedDataValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<UnrealisedDataValuesModel> results = new ArrayList<>();

                    if (lstSearch == null)
                        lstSearch = lstAdapterList;

                    String strSearch = charSequence == null ? "" : charSequence.toString();

                    if (!strSearch.equals("")) {
                        if (lstSearch != null && lstSearch.size() > 0) {
                            for (final UnrealisedDataValuesModel model : lstSearch) {
                                if (model.getPOLICYNUMBER().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                    results.add(model);
                                }
                            }
                        }
                        oReturn.values = results;
                    } else {
                        oReturn.values = globalList;
                    }
                    return oReturn;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    lstAdapterList = (ArrayList<UnrealisedDataValuesModel>) results.values;
                    selectedAdapter = new SelectedAdapter(lstAdapterList);
                    recyclerview.setAdapter(selectedAdapter);

                    notifyDataSetChanged();
                }
            };
        }


        @Override
        public int getItemCount() {
            return lstAdapterList.size();
        }

        @Override
        public ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            // infalte the item Layout
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_item_unrealised_data, parent, false);
            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final ViewHolderAdapter holder, final int position) {
            holder.tvPolicyNumber.setText(lstAdapterList.get(position).getPOLICYNUMBER());
            holder.textviewMobileNumber.setText(lstAdapterList.get(position).getCONTACT_NUMBER_MOBILE());

           /* holder.imgcontact_cust_r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mobileNumber = holder.textviewMobileNumber.getText().toString();

                    if (!TextUtils.isEmpty(mobileNumber)) {
                        commonMethods.callMobileNumber(mobileNumber, context);
                    }
                }
            });*/

            holder.textviewMobileNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mobileNumber = holder.textviewMobileNumber.getText().toString();

                    if (!TextUtils.isEmpty(mobileNumber)) {
                        commonMethods.callMobileNumber(mobileNumber, context);
                    }
                }
            });
            holder.buttonViewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Gson gson = new Gson();
                    String unrealiseDetails = gson.toJson(lstAdapterList.get(position));
                    Intent intent = new Intent(context, UnrealisedDataDetailsActivity.class);
                    intent.putExtra("UnrealiseDetails", unrealiseDetails);
                    startActivity(intent);
                }
            });
        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder {

            private final TextView tvPolicyNumber, textviewMobileNumber;
            //private final ImageView imgcontact_cust_r;
            private final Button buttonViewDetails;

            ViewHolderAdapter(View v) {
                super(v);
                tvPolicyNumber = v.findViewById(R.id.tvPolicyNumber);
                textviewMobileNumber = v.findViewById(R.id.textviewMobileNumber);
                //imgcontact_cust_r = v.findViewById(R.id.imgcontact_cust_r);
                buttonViewDetails = v.findViewById(R.id.buttonViewDetails);
            }
        }

    }

    class UnrealisedDataValuesModel {
        private final String POLICYPROPOSALNUMBER;
        private final String POLICYNUMBER;
        private final String PRODUCTNAME;
        private final String POLICYCURRENTSTATUS;
        private final String INSTALLMENT_PREMIUM;
        private final String SERVICEBRANCHNAME;
        private final String DRAWN_BANK;
        private final String DRAWN_BRANCH;
        private final String COLLECTION_BANK;
        private final String MONEY_IN_DATE;
        private final String INSTRUMENT_NUMBER;
        private final String MONEY_IN_AMOUNT;
        private final String CHEQUE_DATE;
        private final String SBIL_BRANCH;
        private final String PAYMENT_TYPE;
        private final String RESIDUAL_AMOUNT;
        private final String PAYMENT_MODE;
        private final String CHEQUE_TYPE;
        private final String SOURCE_ID;
        private final String CASH_ENTRY_DATE;
        private final String PAYMENTVALID;
        private final String CUSTOMER_NAME;
        private final String PREMIUMGROSSAMOUNT;
        private final String FREQUENCY;
        private final String DOC;
        private final String PREMIUMFUP;
        private final String ALTERNATEMODE;
        private final String CONTACT_NUMBER_MOBILE;
        private final String SERVICEREGIONNAME;
        private final String SERVICEREGIONNAMEOPS;
        private final String POLICYSUMASSURED;
        private final String RAG_FLAG;
        private final String TIER1_MKT_NAME;
        private final String TIER2_MKT_NAME;
        private final String TIERROC_MKT_NAME;
        private final String POLICY_MATURITY_DATE;
        private final String ANNUALIZEDPREMIUM;
        private final String POLICY_TERM;
        private final String POLICYPAYMENTTERM;
        private final String IA_CIF_CODE;
        private final String IA_CIF_NAME;
        private final String UM_BANKNAME;
        private final String CHANNEL_NAME;
        private final String CHANNEL_ACTIVE_STATUS;
        private final String POLICYPAYMENTMECHANISM;
        private final String CUSTOMERID;
        private final String HOLDERGENDER;
        private final String POLICYISSUEDATE;
        private final String UMBANKCODE;
        private final String CHANNELTYPE;
        private final String BRANCHAREA;
        private final String BRANCHDIVISION;
        private final String POLICYTYPE;
        private final String POLICYTYPESUB;
        private final String RESIDUALAMOUNT;
        private final String CUSTOMERBANKACCOUNTNO;

        public UnrealisedDataValuesModel(String POLICYPROPOSALNUMBER, String POLICYNUMBER, String PRODUCTNAME, String POLICYCURRENTSTATUS, String INSTALLMENT_PREMIUM, String SERVICEBRANCHNAME, String DRAWN_BANK, String DRAWN_BRANCH, String COLLECTION_BANK, String MONEY_IN_DATE, String INSTRUMENT_NUMBER, String MONEY_IN_AMOUNT, String CHEQUE_DATE, String SBIL_BRANCH, String PAYMENT_TYPE, String RESIDUAL_AMOUNT, String PAYMENT_MODE, String CHEQUE_TYPE, String SOURCE_ID, String CASH_ENTRY_DATE, String PAYMENTVALID, String CUSTOMER_NAME, String PREMIUMGROSSAMOUNT, String FREQUENCY, String DOC, String PREMIUMFUP, String ALTERNATEMODE, String CONTACT_NUMBER_MOBILE, String SERVICEREGIONNAME, String SERVICEREGIONNAMEOPS, String POLICYSUMASSURED, String RAG_FLAG, String TIER1_MKT_NAME, String TIER2_MKT_NAME, String TIERROC_MKT_NAME, String POLICY_MATURITY_DATE, String ANNUALIZEDPREMIUM, String POLICY_TERM, String POLICYPAYMENTTERM, String IA_CIF_CODE, String IA_CIF_NAME, String UM_BANKNAME, String CHANNEL_NAME, String CHANNEL_ACTIVE_STATUS, String POLICYPAYMENTMECHANISM, String CUSTOMERID, String HOLDERGENDER, String POLICYISSUEDATE, String UMBANKCODE, String CHANNELTYPE, String BRANCHAREA, String BRANCHDIVISION, String POLICYTYPE, String POLICYTYPESUB, String RESIDUALAMOUNT, String CUSTOMERBANKACCOUNTNO) {
            this.POLICYPROPOSALNUMBER = POLICYPROPOSALNUMBER;
            this.POLICYNUMBER = POLICYNUMBER;
            this.PRODUCTNAME = PRODUCTNAME;
            this.POLICYCURRENTSTATUS = POLICYCURRENTSTATUS;
            this.INSTALLMENT_PREMIUM = INSTALLMENT_PREMIUM;
            this.SERVICEBRANCHNAME = SERVICEBRANCHNAME;
            this.DRAWN_BANK = DRAWN_BANK;
            this.DRAWN_BRANCH = DRAWN_BRANCH;
            this.COLLECTION_BANK = COLLECTION_BANK;
            this.MONEY_IN_DATE = MONEY_IN_DATE;
            this.INSTRUMENT_NUMBER = INSTRUMENT_NUMBER;
            this.MONEY_IN_AMOUNT = MONEY_IN_AMOUNT;
            this.CHEQUE_DATE = CHEQUE_DATE;
            this.SBIL_BRANCH = SBIL_BRANCH;
            this.PAYMENT_TYPE = PAYMENT_TYPE;
            this.RESIDUAL_AMOUNT = RESIDUAL_AMOUNT;
            this.PAYMENT_MODE = PAYMENT_MODE;
            this.CHEQUE_TYPE = CHEQUE_TYPE;
            this.SOURCE_ID = SOURCE_ID;
            this.CASH_ENTRY_DATE = CASH_ENTRY_DATE;
            this.PAYMENTVALID = PAYMENTVALID;
            this.CUSTOMER_NAME = CUSTOMER_NAME;
            this.PREMIUMGROSSAMOUNT = PREMIUMGROSSAMOUNT;
            this.FREQUENCY = FREQUENCY;
            this.DOC = DOC;
            this.PREMIUMFUP = PREMIUMFUP;
            this.ALTERNATEMODE = ALTERNATEMODE;
            this.CONTACT_NUMBER_MOBILE = CONTACT_NUMBER_MOBILE;
            this.SERVICEREGIONNAME = SERVICEREGIONNAME;
            this.SERVICEREGIONNAMEOPS = SERVICEREGIONNAMEOPS;
            this.POLICYSUMASSURED = POLICYSUMASSURED;
            this.RAG_FLAG = RAG_FLAG;
            this.TIER1_MKT_NAME = TIER1_MKT_NAME;
            this.TIER2_MKT_NAME = TIER2_MKT_NAME;
            this.TIERROC_MKT_NAME = TIERROC_MKT_NAME;
            this.POLICY_MATURITY_DATE = POLICY_MATURITY_DATE;
            this.ANNUALIZEDPREMIUM = ANNUALIZEDPREMIUM;
            this.POLICY_TERM = POLICY_TERM;
            this.POLICYPAYMENTTERM = POLICYPAYMENTTERM;
            this.IA_CIF_CODE = IA_CIF_CODE;
            this.IA_CIF_NAME = IA_CIF_NAME;
            this.UM_BANKNAME = UM_BANKNAME;
            this.CHANNEL_NAME = CHANNEL_NAME;
            this.CHANNEL_ACTIVE_STATUS = CHANNEL_ACTIVE_STATUS;
            this.POLICYPAYMENTMECHANISM = POLICYPAYMENTMECHANISM;
            this.CUSTOMERID = CUSTOMERID;
            this.HOLDERGENDER = HOLDERGENDER;
            this.POLICYISSUEDATE = POLICYISSUEDATE;
            this.UMBANKCODE = UMBANKCODE;
            this.CHANNELTYPE = CHANNELTYPE;
            this.BRANCHAREA = BRANCHAREA;
            this.BRANCHDIVISION = BRANCHDIVISION;
            this.POLICYTYPE = POLICYTYPE;
            this.POLICYTYPESUB = POLICYTYPESUB;
            this.RESIDUALAMOUNT = RESIDUALAMOUNT;
            this.CUSTOMERBANKACCOUNTNO = CUSTOMERBANKACCOUNTNO;
        }

        public String getPOLICYPROPOSALNUMBER() {
            return POLICYPROPOSALNUMBER;
        }

        public String getPOLICYNUMBER() {
            return POLICYNUMBER;
        }

        public String getPRODUCTNAME() {
            return PRODUCTNAME;
        }

        public String getPOLICYCURRENTSTATUS() {
            return POLICYCURRENTSTATUS;
        }

        public String getINSTALLMENT_PREMIUM() {
            return INSTALLMENT_PREMIUM;
        }

        public String getSERVICEBRANCHNAME() {
            return SERVICEBRANCHNAME;
        }

        public String getDRAWN_BANK() {
            return DRAWN_BANK;
        }

        public String getDRAWN_BRANCH() {
            return DRAWN_BRANCH;
        }

        public String getCOLLECTION_BANK() {
            return COLLECTION_BANK;
        }

        public String getMONEY_IN_DATE() {
            return MONEY_IN_DATE;
        }

        public String getINSTRUMENT_NUMBER() {
            return INSTRUMENT_NUMBER;
        }

        public String getMONEY_IN_AMOUNT() {
            return MONEY_IN_AMOUNT;
        }

        public String getCHEQUE_DATE() {
            return CHEQUE_DATE;
        }

        public String getSBIL_BRANCH() {
            return SBIL_BRANCH;
        }

        public String getPAYMENT_TYPE() {
            return PAYMENT_TYPE;
        }

        public String getRESIDUAL_AMOUNT() {
            return RESIDUAL_AMOUNT;
        }

        public String getPAYMENT_MODE() {
            return PAYMENT_MODE;
        }

        public String getCHEQUE_TYPE() {
            return CHEQUE_TYPE;
        }

        public String getSOURCE_ID() {
            return SOURCE_ID;
        }

        public String getCASH_ENTRY_DATE() {
            return CASH_ENTRY_DATE;
        }

        public String getPAYMENTVALID() {
            return PAYMENTVALID;
        }

        public String getCUSTOMER_NAME() {
            return CUSTOMER_NAME;
        }

        public String getPREMIUMGROSSAMOUNT() {
            return PREMIUMGROSSAMOUNT;
        }

        public String getFREQUENCY() {
            return FREQUENCY;
        }

        public String getDOC() {
            return DOC;
        }

        public String getPREMIUMFUP() {
            return PREMIUMFUP;
        }

        public String getALTERNATEMODE() {
            return ALTERNATEMODE;
        }

        public String getCONTACT_NUMBER_MOBILE() {
            return CONTACT_NUMBER_MOBILE;
        }

        public String getSERVICEREGIONNAME() {
            return SERVICEREGIONNAME;
        }

        public String getSERVICEREGIONNAMEOPS() {
            return SERVICEREGIONNAMEOPS;
        }

        public String getPOLICYSUMASSURED() {
            return POLICYSUMASSURED;
        }

        public String getRAG_FLAG() {
            return RAG_FLAG;
        }

        public String getTIER1_MKT_NAME() {
            return TIER1_MKT_NAME;
        }

        public String getTIER2_MKT_NAME() {
            return TIER2_MKT_NAME;
        }

        public String getTIERROC_MKT_NAME() {
            return TIERROC_MKT_NAME;
        }

        public String getPOLICY_MATURITY_DATE() {
            return POLICY_MATURITY_DATE;
        }

        public String getANNUALIZEDPREMIUM() {
            return ANNUALIZEDPREMIUM;
        }

        public String getPOLICY_TERM() {
            return POLICY_TERM;
        }

        public String getPOLICYPAYMENTTERM() {
            return POLICYPAYMENTTERM;
        }

        public String getIA_CIF_CODE() {
            return IA_CIF_CODE;
        }

        public String getIA_CIF_NAME() {
            return IA_CIF_NAME;
        }

        public String getUM_BANKNAME() {
            return UM_BANKNAME;
        }

        public String getCHANNEL_NAME() {
            return CHANNEL_NAME;
        }

        public String getCHANNEL_ACTIVE_STATUS() {
            return CHANNEL_ACTIVE_STATUS;
        }

        public String getPOLICYPAYMENTMECHANISM() {
            return POLICYPAYMENTMECHANISM;
        }

        public String getCUSTOMERID() {
            return CUSTOMERID;
        }

        public String getHOLDERGENDER() {
            return HOLDERGENDER;
        }

        public String getPOLICYISSUEDATE() {
            return POLICYISSUEDATE;
        }

        public String getUMBANKCODE() {
            return UMBANKCODE;
        }

        public String getCHANNELTYPE() {
            return CHANNELTYPE;
        }

        public String getBRANCHAREA() {
            return BRANCHAREA;
        }

        public String getBRANCHDIVISION() {
            return BRANCHDIVISION;
        }

        public String getPOLICYTYPE() {
            return POLICYTYPE;
        }

        public String getPOLICYTYPESUB() {
            return POLICYTYPESUB;
        }

        public String getRESIDUALAMOUNT() {
            return RESIDUALAMOUNT;
        }

        public String getCUSTOMERBANKACCOUNTNO() {
            return CUSTOMERBANKACCOUNTNO;
        }
    }
}
