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

public class UnclaimedDataActivity extends AppCompatActivity implements View.OnClickListener, ServiceHits.DownLoadData {

    private final String METHOD_NAME = "getOPSHierarchyBranchUnclaimedDue";

    private transient CommonMethods commonMethods;
    private Context context;
    private String strCIFBDMUserId = "", strCIFBDMEmailId = "",
            strCIFBDMPassword = "", strCIFBDMMObileNo = "";
    private TextView textViewFromDate, textViewToDate, textviewRecordCount;

    private EditText edittextSearch;
    private RecyclerView recyclerview;
    private int mYear, mMonth, mDay, datecheck = 0;
    private ServiceHits service;
    private String fromDate = "", toDate = "";
    private ProgressDialog mProgressDialog;

    private SelectedAdapter selectedAdapter;
    private ArrayList<UnclaimedDataValuesModel> globalList;
    private UnclaimedDataAsync unclaimedDataAsync;
    private RelativeLayout llHeader;

    //D04018983100
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_unclaimed_data);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Unclaimed Data");

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

        unclaimedDataAsync = new UnclaimedDataAsync();
        unclaimedDataAsync.execute();
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

    class UnclaimedDataAsync extends AsyncTask<String, String, String> {

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

                //getOPSHierarchyBranchUnclaimedDue(string fromdate,
                // string todate, string brcode, string strEmailId, string strMobileNo, string strAuthKey)

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

                        List<UnclaimedDataValuesModel> nodeData = parseNodeUnclaimedData(Node);
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

        private ArrayList<UnclaimedDataValuesModel> lstAdapterList, lstSearch;

        SelectedAdapter(ArrayList<UnclaimedDataValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<UnclaimedDataValuesModel> results = new ArrayList<>();

                    if (lstSearch == null)
                        lstSearch = lstAdapterList;

                    String strSearch = charSequence == null ? "" : charSequence.toString();

                    if (!strSearch.equals("")) {
                        if (lstSearch != null && lstSearch.size() > 0) {
                            for (final UnclaimedDataValuesModel model : lstSearch) {
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

                    lstAdapterList = (ArrayList<UnclaimedDataValuesModel>) results.values;
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
                    R.layout.list_item_unclaimed_data, parent, false);
            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final ViewHolderAdapter holder, final int position) {
            holder.tvPolicyNumber.setText(lstAdapterList.get(position).getPOLICYNUMBER());
            holder.textviewMobileNumber.setText(lstAdapterList.get(position).getCONTACTMOBILE());

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
                    String unclaimDetails = gson.toJson(lstAdapterList.get(position));
                    Intent intent = new Intent(context, UnclaimedDataPolicyDetailsActivity.class);
                    intent.putExtra("UnclaimDetails", unclaimDetails);
                    startActivity(intent);
                }
            });
        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder {

            private final TextView tvPolicyNumber, textviewMobileNumber;
            //private final ImageView imgcontact_cust_r;
            private Button buttonViewDetails;

            ViewHolderAdapter(View v) {
                super(v);
                tvPolicyNumber = v.findViewById(R.id.tvPolicyNumber);
                textviewMobileNumber = v.findViewById(R.id.textviewMobileNumber);
                //imgcontact_cust_r = v.findViewById(R.id.imgcontact_cust_r);
                buttonViewDetails = v.findViewById(R.id.buttonViewDetails);
            }
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

        if (unclaimedDataAsync != null) {
            unclaimedDataAsync.cancel(true);
        }

        if (service != null) {
            service.cancel(true);
        }
    }

    public class UnclaimedDataValuesModel {
        private String POLICYNUMBER, POLICYPROPOSALNUMBER, POLICYISSUEDATE, OPERATION,
                DUE_DATE, EFFECTIVE_DATE, CHEQUE_NO, UNCLAIMED_AMT, OPERATION_STATUS, STALE_DC_ACTION,
                UNCLAIMED_UPLOADED_DATE, ENTRY_DATE, ENTRY_AMOUNT, FUND_VALUE_AT_ENTRY, ENTRY_UNIT, ENTY_NAV_VALUE, ISSUANCEAGEING,
                CHQ_REALISED_DATE, AGEING, PC_NAME, REGION_NAME, UNCLAIM_ID, NAVVALUE, POLICYCURRENTSTATUS,
                CORRESPONDENCEADDRESS1, CORRESPONDENCEADDRESS2, CORRESPONDENCEADDRESS3,
                CORRESPONDENCECITY, CORRESPONDENCESTATE,
                CORRESPONDENCEPOSTCODE, CONTACTMOBILE,
                HOLDERNAME, LIFEASSUREDNAME, CHANNELTYPE, CUSTOMER_NAME, PREMIUMGROSSAMOUNT,
                FREQUENCY, DOC, PREMIUMFUP, SERVICEREGIONNAMEOPS, POLICYSUMASSURED, TIER1_MKT_NAME,
                TIERROC_MKT_NAME, ANNUALIZED_PREMIUM, POLICY_TERM, POLICYPAYMENTTERM, IA_CIF_CODE, IA_CIF_NAME,
                UM_BANKNAME, CHANNELACTIVESTATUS, POLICYPAYMENTMECHANISM, CUSTOMER_ID,
                HOLDER_GENDER, UM_BANKCODE, BANKTYPE, BANKCODE, BANKNAME, BANKBRANCHCODE, BANKBRANCHNAME, BANKCIRCLENAME,
                BANKMODULENAME, BANKREGION, POLICYTYPE, POLICYTYPESUB,
                ACCURED_EXIT_PAYABLE_AMOUNT;


        public UnclaimedDataValuesModel(String POLICYNUMBER, String POLICYPROPOSALNUMBER, String POLICYISSUEDATE, String OPERATION, String DUE_DATE, String EFFECTIVE_DATE, String CHEQUE_NO, String UNCLAIMED_AMT, String OPERATION_STATUS, String STALE_DC_ACTION, String UNCLAIMED_UPLOADED_DATE, String ENTRY_DATE, String ENTRY_AMOUNT, String FUND_VALUE_AT_ENTRY, String ENTRY_UNIT, String ENTY_NAV_VALUE, String ISSUANCEAGEING, String CHQ_REALISED_DATE, String AGEING, String PC_NAME, String REGION_NAME, String UNCLAIM_ID, String NAVVALUE, String POLICYCURRENTSTATUS, String CORRESPONDENCEADDRESS1, String CORRESPONDENCEADDRESS2, String CORRESPONDENCEADDRESS3, String CORRESPONDENCECITY, String CORRESPONDENCESTATE, String CORRESPONDENCEPOSTCODE, String CONTACTMOBILE, String HOLDERNAME, String LIFEASSUREDNAME, String CHANNELTYPE, String CUSTOMER_NAME, String PREMIUMGROSSAMOUNT, String FREQUENCY, String DOC, String PREMIUMFUP, String SERVICEREGIONNAMEOPS, String POLICYSUMASSURED, String TIER1_MKT_NAME, String TIERROC_MKT_NAME, String ANNUALIZED_PREMIUM, String POLICY_TERM, String POLICYPAYMENTTERM, String IA_CIF_CODE, String IA_CIF_NAME, String UM_BANKNAME, String CHANNELACTIVESTATUS, String POLICYPAYMENTMECHANISM, String CUSTOMER_ID, String HOLDER_GENDER, String UM_BANKCODE, String BANKTYPE, String BANKCODE, String BANKNAME, String BANKBRANCHCODE, String BANKBRANCHNAME, String BANKCIRCLENAME, String BANKMODULENAME, String BANKREGION, String POLICYTYPE, String POLICYTYPESUB, String ACCURED_EXIT_PAYABLE_AMOUNT) {
            this.POLICYNUMBER = POLICYNUMBER;
            this.POLICYPROPOSALNUMBER = POLICYPROPOSALNUMBER;
            this.POLICYISSUEDATE = POLICYISSUEDATE;
            this.OPERATION = OPERATION;
            this.DUE_DATE = DUE_DATE;
            this.EFFECTIVE_DATE = EFFECTIVE_DATE;
            this.CHEQUE_NO = CHEQUE_NO;
            this.UNCLAIMED_AMT = UNCLAIMED_AMT;
            this.OPERATION_STATUS = OPERATION_STATUS;
            this.STALE_DC_ACTION = STALE_DC_ACTION;
            this.UNCLAIMED_UPLOADED_DATE = UNCLAIMED_UPLOADED_DATE;
            this.ENTRY_DATE = ENTRY_DATE;
            this.ENTRY_AMOUNT = ENTRY_AMOUNT;
            this.FUND_VALUE_AT_ENTRY = FUND_VALUE_AT_ENTRY;
            this.ENTRY_UNIT = ENTRY_UNIT;
            this.ENTY_NAV_VALUE = ENTY_NAV_VALUE;
            this.ISSUANCEAGEING = ISSUANCEAGEING;
            this.CHQ_REALISED_DATE = CHQ_REALISED_DATE;
            this.AGEING = AGEING;
            this.PC_NAME = PC_NAME;
            this.REGION_NAME = REGION_NAME;
            this.UNCLAIM_ID = UNCLAIM_ID;
            this.NAVVALUE = NAVVALUE;
            this.POLICYCURRENTSTATUS = POLICYCURRENTSTATUS;
            this.CORRESPONDENCEADDRESS1 = CORRESPONDENCEADDRESS1;
            this.CORRESPONDENCEADDRESS2 = CORRESPONDENCEADDRESS2;
            this.CORRESPONDENCEADDRESS3 = CORRESPONDENCEADDRESS3;
            this.CORRESPONDENCECITY = CORRESPONDENCECITY;
            this.CORRESPONDENCESTATE = CORRESPONDENCESTATE;
            this.CORRESPONDENCEPOSTCODE = CORRESPONDENCEPOSTCODE;
            this.CONTACTMOBILE = CONTACTMOBILE;
            this.HOLDERNAME = HOLDERNAME;
            this.LIFEASSUREDNAME = LIFEASSUREDNAME;
            this.CHANNELTYPE = CHANNELTYPE;
            this.CUSTOMER_NAME = CUSTOMER_NAME;
            this.PREMIUMGROSSAMOUNT = PREMIUMGROSSAMOUNT;
            this.FREQUENCY = FREQUENCY;
            this.DOC = DOC;
            this.PREMIUMFUP = PREMIUMFUP;
            this.SERVICEREGIONNAMEOPS = SERVICEREGIONNAMEOPS;
            this.POLICYSUMASSURED = POLICYSUMASSURED;
            this.TIER1_MKT_NAME = TIER1_MKT_NAME;
            this.TIERROC_MKT_NAME = TIERROC_MKT_NAME;
            this.ANNUALIZED_PREMIUM = ANNUALIZED_PREMIUM;
            this.POLICY_TERM = POLICY_TERM;
            this.POLICYPAYMENTTERM = POLICYPAYMENTTERM;
            this.IA_CIF_CODE = IA_CIF_CODE;
            this.IA_CIF_NAME = IA_CIF_NAME;
            this.UM_BANKNAME = UM_BANKNAME;
            this.CHANNELACTIVESTATUS = CHANNELACTIVESTATUS;
            this.POLICYPAYMENTMECHANISM = POLICYPAYMENTMECHANISM;
            this.CUSTOMER_ID = CUSTOMER_ID;
            this.HOLDER_GENDER = HOLDER_GENDER;
            this.UM_BANKCODE = UM_BANKCODE;
            this.BANKTYPE = BANKTYPE;
            this.BANKCODE = BANKCODE;
            this.BANKNAME = BANKNAME;
            this.BANKBRANCHCODE = BANKBRANCHCODE;
            this.BANKBRANCHNAME = BANKBRANCHNAME;
            this.BANKCIRCLENAME = BANKCIRCLENAME;
            this.BANKMODULENAME = BANKMODULENAME;
            this.BANKREGION = BANKREGION;
            this.POLICYTYPE = POLICYTYPE;
            this.POLICYTYPESUB = POLICYTYPESUB;
            this.ACCURED_EXIT_PAYABLE_AMOUNT = ACCURED_EXIT_PAYABLE_AMOUNT;
        }

        public String getPOLICYNUMBER() {
            return POLICYNUMBER;
        }

        public String getPOLICYPROPOSALNUMBER() {
            return POLICYPROPOSALNUMBER;
        }

        public String getPOLICYISSUEDATE() {
            return POLICYISSUEDATE;
        }

        public String getOPERATION() {
            return OPERATION;
        }

        public String getDUE_DATE() {
            return DUE_DATE;
        }

        public String getEFFECTIVE_DATE() {
            return EFFECTIVE_DATE;
        }

        public String getCHEQUE_NO() {
            return CHEQUE_NO;
        }

        public String getUNCLAIMED_AMT() {
            return UNCLAIMED_AMT;
        }

        public String getOPERATION_STATUS() {
            return OPERATION_STATUS;
        }

        public String getSTALE_DC_ACTION() {
            return STALE_DC_ACTION;
        }

        public String getUNCLAIMED_UPLOADED_DATE() {
            return UNCLAIMED_UPLOADED_DATE;
        }

        public String getENTRY_DATE() {
            return ENTRY_DATE;
        }

        public String getENTRY_AMOUNT() {
            return ENTRY_AMOUNT;
        }

        public String getFUND_VALUE_AT_ENTRY() {
            return FUND_VALUE_AT_ENTRY;
        }

        public String getENTRY_UNIT() {
            return ENTRY_UNIT;
        }

        public String getENTY_NAV_VALUE() {
            return ENTY_NAV_VALUE;
        }

        public String getISSUANCEAGEING() {
            return ISSUANCEAGEING;
        }

        public String getCHQ_REALISED_DATE() {
            return CHQ_REALISED_DATE;
        }

        public String getAGEING() {
            return AGEING;
        }

        public String getPC_NAME() {
            return PC_NAME;
        }

        public String getREGION_NAME() {
            return REGION_NAME;
        }

        public String getUNCLAIM_ID() {
            return UNCLAIM_ID;
        }

        public String getNAVVALUE() {
            return NAVVALUE;
        }

        public String getPOLICYCURRENTSTATUS() {
            return POLICYCURRENTSTATUS;
        }

        public String getCORRESPONDENCEADDRESS1() {
            return CORRESPONDENCEADDRESS1;
        }

        public String getCORRESPONDENCEADDRESS2() {
            return CORRESPONDENCEADDRESS2;
        }

        public String getCORRESPONDENCEADDRESS3() {
            return CORRESPONDENCEADDRESS3;
        }

        public String getCORRESPONDENCECITY() {
            return CORRESPONDENCECITY;
        }

        public String getCORRESPONDENCESTATE() {
            return CORRESPONDENCESTATE;
        }

        public String getCORRESPONDENCEPOSTCODE() {
            return CORRESPONDENCEPOSTCODE;
        }

        public String getCONTACTMOBILE() {
            return CONTACTMOBILE;
        }

        public String getHOLDERNAME() {
            return HOLDERNAME;
        }

        public String getLIFEASSUREDNAME() {
            return LIFEASSUREDNAME;
        }

        public String getCHANNELTYPE() {
            return CHANNELTYPE;
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

        public String getSERVICEREGIONNAMEOPS() {
            return SERVICEREGIONNAMEOPS;
        }

        public String getPOLICYSUMASSURED() {
            return POLICYSUMASSURED;
        }

        public String getTIER1_MKT_NAME() {
            return TIER1_MKT_NAME;
        }

        public String getTIERROC_MKT_NAME() {
            return TIERROC_MKT_NAME;
        }

        public String getANNUALIZED_PREMIUM() {
            return ANNUALIZED_PREMIUM;
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

        public String getCHANNELACTIVESTATUS() {
            return CHANNELACTIVESTATUS;
        }

        public String getPOLICYPAYMENTMECHANISM() {
            return POLICYPAYMENTMECHANISM;
        }

        public String getCUSTOMER_ID() {
            return CUSTOMER_ID;
        }

        public String getHOLDER_GENDER() {
            return HOLDER_GENDER;
        }

        public String getUM_BANKCODE() {
            return UM_BANKCODE;
        }

        public String getBANKTYPE() {
            return BANKTYPE;
        }

        public String getBANKCODE() {
            return BANKCODE;
        }

        public String getBANKNAME() {
            return BANKNAME;
        }

        public String getBANKBRANCHCODE() {
            return BANKBRANCHCODE;
        }

        public String getBANKBRANCHNAME() {
            return BANKBRANCHNAME;
        }

        public String getBANKCIRCLENAME() {
            return BANKCIRCLENAME;
        }

        public String getBANKMODULENAME() {
            return BANKMODULENAME;
        }

        public String getBANKREGION() {
            return BANKREGION;
        }

        public String getPOLICYTYPE() {
            return POLICYTYPE;
        }

        public String getPOLICYTYPESUB() {
            return POLICYTYPESUB;
        }

        public String getACCURED_EXIT_PAYABLE_AMOUNT() {
            return ACCURED_EXIT_PAYABLE_AMOUNT;
        }
    }


    public ArrayList<UnclaimedDataValuesModel> parseNodeUnclaimedData(List<String> lsNode) {
        ArrayList<UnclaimedDataValuesModel> lsData = new ArrayList<>();
        ParseXML parseXML = new ParseXML();
        String POLICYNUMBER, POLICYPROPOSALNUMBER, POLICYISSUEDATE, OPERATION,
                DUE_DATE, EFFECTIVE_DATE, CHEQUE_NO, UNCLAIMED_AMT, OPERATION_STATUS, STALE_DC_ACTION,
                UNCLAIMED_UPLOADED_DATE, ENTRY_DATE, ENTRY_AMOUNT, FUND_VALUE_AT_ENTRY, ENTRY_UNIT, ENTY_NAV_VALUE, ISSUANCEAGEING,
                CHQ_REALISED_DATE, AGEING, PC_NAME, REGION_NAME, UNCLAIM_ID, NAVVALUE, POLICYCURRENTSTATUS,
                CORRESPONDENCEADDRESS1, CORRESPONDENCEADDRESS2, CORRESPONDENCEADDRESS3,
                CORRESPONDENCECITY, CORRESPONDENCESTATE,
                CORRESPONDENCEPOSTCODE, CONTACTMOBILE,
                HOLDERNAME, LIFEASSUREDNAME, CHANNELTYPE, CUSTOMER_NAME, PREMIUMGROSSAMOUNT,
                FREQUENCY, DOC, PREMIUMFUP, SERVICEREGIONNAMEOPS, POLICYSUMASSURED, TIER1_MKT_NAME,
                TIERROC_MKT_NAME, ANNUALIZED_PREMIUM, POLICY_TERM, POLICYPAYMENTTERM, IA_CIF_CODE, IA_CIF_NAME,
                UM_BANKNAME, CHANNELACTIVESTATUS, POLICYPAYMENTMECHANISM, CUSTOMER_ID,
                HOLDER_GENDER, UM_BANKCODE, BANKTYPE, BANKCODE, BANKNAME, BANKBRANCHCODE, BANKBRANCHNAME, BANKCIRCLENAME,
                BANKMODULENAME, BANKREGION, POLICYTYPE, POLICYTYPESUB,
                ACCURED_EXIT_PAYABLE_AMOUNT;
        for (String Node : lsNode) {

            POLICYNUMBER = parseXML.parseXmlTag(Node, "POLICYNUMBER");
            POLICYNUMBER = POLICYNUMBER == null ? "" : POLICYNUMBER;

            POLICYPROPOSALNUMBER = parseXML.parseXmlTag(Node, "POLICYPROPOSALNUMBER");
            POLICYPROPOSALNUMBER = POLICYPROPOSALNUMBER == null ? "" : POLICYPROPOSALNUMBER;

            POLICYISSUEDATE = parseXML.parseXmlTag(Node, "POLICYISSUEDATE");
            POLICYISSUEDATE = POLICYISSUEDATE == null ? "" : POLICYISSUEDATE;

            OPERATION = parseXML.parseXmlTag(Node, "OPERATION");
            OPERATION = OPERATION == null ? "" : OPERATION;

            DUE_DATE = parseXML.parseXmlTag(Node, "DUE_DATE");
            DUE_DATE = DUE_DATE == null ? "" : DUE_DATE;

            EFFECTIVE_DATE = parseXML.parseXmlTag(Node, "EFFECTIVE_DATE");
            EFFECTIVE_DATE = EFFECTIVE_DATE == null ? "" : EFFECTIVE_DATE;

            CHEQUE_NO = parseXML.parseXmlTag(Node, "CHEQUE_NO");
            CHEQUE_NO = CHEQUE_NO == null ? "" : CHEQUE_NO;


            UNCLAIMED_AMT = parseXML.parseXmlTag(Node, "UNCLAIMED_AMT");
            UNCLAIMED_AMT = UNCLAIMED_AMT == null ? "" : UNCLAIMED_AMT;

            OPERATION_STATUS = parseXML.parseXmlTag(Node, "OPERATION_STATUS");
            OPERATION_STATUS = OPERATION_STATUS == null ? "" : OPERATION_STATUS;

            STALE_DC_ACTION = parseXML.parseXmlTag(Node, "STALE_DC_ACTION");
            STALE_DC_ACTION = STALE_DC_ACTION == null ? "" : STALE_DC_ACTION;


            UNCLAIMED_UPLOADED_DATE = parseXML.parseXmlTag(Node, "UNCLAIMED_UPLOADED_DATE");
            UNCLAIMED_UPLOADED_DATE = UNCLAIMED_UPLOADED_DATE == null ? "" : UNCLAIMED_UPLOADED_DATE;

            ENTRY_DATE = parseXML.parseXmlTag(Node, "ENTRY_DATE");
            ENTRY_DATE = ENTRY_DATE == null ? "" : ENTRY_DATE;

            ENTRY_AMOUNT = parseXML.parseXmlTag(Node, "ENTRY_AMOUNT");
            ENTRY_AMOUNT = ENTRY_AMOUNT == null ? "" : ENTRY_AMOUNT;

            FUND_VALUE_AT_ENTRY = parseXML.parseXmlTag(Node, "FUND_VALUE_AT_ENTRY");
            FUND_VALUE_AT_ENTRY = FUND_VALUE_AT_ENTRY == null ? "" : FUND_VALUE_AT_ENTRY;

            ENTRY_UNIT = parseXML.parseXmlTag(Node, "ENTRY_UNIT");
            ENTRY_UNIT = ENTRY_UNIT == null ? "" : ENTRY_UNIT;

            ENTY_NAV_VALUE = parseXML.parseXmlTag(Node, "ENTY_NAV_VALUE");
            ENTY_NAV_VALUE = ENTY_NAV_VALUE == null ? "" : ENTY_NAV_VALUE;

            ISSUANCEAGEING = parseXML.parseXmlTag(Node, "ISSUANCEAGEING");
            ISSUANCEAGEING = ISSUANCEAGEING == null ? "" : ISSUANCEAGEING;


            CHQ_REALISED_DATE = parseXML.parseXmlTag(Node, "CHQ_REALISED_DATE");
            CHQ_REALISED_DATE = CHQ_REALISED_DATE == null ? "" : CHQ_REALISED_DATE;

            AGEING = parseXML.parseXmlTag(Node, "AGEING");
            AGEING = AGEING == null ? "" : AGEING;

            PC_NAME = parseXML.parseXmlTag(Node, "PC_NAME");
            PC_NAME = PC_NAME == null ? "" : PC_NAME;

            REGION_NAME = parseXML.parseXmlTag(Node, "REGION_NAME");
            REGION_NAME = REGION_NAME == null ? "" : REGION_NAME;

            UNCLAIM_ID = parseXML.parseXmlTag(Node, "UNCLAIM_ID");
            UNCLAIM_ID = UNCLAIM_ID == null ? "" : UNCLAIM_ID;

            NAVVALUE = parseXML.parseXmlTag(Node, "NAVVALUE");
            NAVVALUE = NAVVALUE == null ? "" : NAVVALUE;

            POLICYCURRENTSTATUS = parseXML.parseXmlTag(Node, "POLICYCURRENTSTATUS");
            POLICYCURRENTSTATUS = POLICYCURRENTSTATUS == null ? "" : POLICYCURRENTSTATUS;

            CORRESPONDENCEADDRESS1 = parseXML.parseXmlTag(Node, "CORRESPONDENCEADDRESS1");
            CORRESPONDENCEADDRESS1 = CORRESPONDENCEADDRESS1 == null ? "" : CORRESPONDENCEADDRESS1;

            CORRESPONDENCEADDRESS2 = parseXML.parseXmlTag(Node, "CORRESPONDENCEADDRESS2");
            CORRESPONDENCEADDRESS2 = CORRESPONDENCEADDRESS2 == null ? "" : CORRESPONDENCEADDRESS2;

            CORRESPONDENCEADDRESS3 = parseXML.parseXmlTag(Node, "CORRESPONDENCEADDRESS3");
            CORRESPONDENCEADDRESS3 = CORRESPONDENCEADDRESS3 == null ? "" : CORRESPONDENCEADDRESS3;

            CORRESPONDENCECITY = parseXML.parseXmlTag(Node, "CORRESPONDENCECITY");
            CORRESPONDENCECITY = CORRESPONDENCECITY == null ? "" : CORRESPONDENCECITY;

            CORRESPONDENCESTATE = parseXML.parseXmlTag(Node, "CORRESPONDENCESTATE");
            CORRESPONDENCESTATE = CORRESPONDENCESTATE == null ? "" : CORRESPONDENCESTATE;


            CORRESPONDENCEPOSTCODE = parseXML.parseXmlTag(Node, "CORRESPONDENCEPOSTCODE");
            CORRESPONDENCEPOSTCODE = CORRESPONDENCEPOSTCODE == null ? "" : CORRESPONDENCEPOSTCODE;

            CONTACTMOBILE = parseXML.parseXmlTag(Node, "CONTACTMOBILE");
            CONTACTMOBILE = CONTACTMOBILE == null ? "" : CONTACTMOBILE;


            HOLDERNAME = parseXML.parseXmlTag(Node, "HOLDERNAME");
            HOLDERNAME = HOLDERNAME == null ? "" : HOLDERNAME;

            LIFEASSUREDNAME = parseXML.parseXmlTag(Node, "LIFEASSUREDNAME");
            LIFEASSUREDNAME = LIFEASSUREDNAME == null ? "" : LIFEASSUREDNAME;

            CHANNELTYPE = parseXML.parseXmlTag(Node, "CHANNELTYPE");
            CHANNELTYPE = CHANNELTYPE == null ? "" : CHANNELTYPE;

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

            SERVICEREGIONNAMEOPS = parseXML.parseXmlTag(Node, "SERVICEREGIONNAMEOPS");
            SERVICEREGIONNAMEOPS = SERVICEREGIONNAMEOPS == null ? "" : SERVICEREGIONNAMEOPS;

            POLICYSUMASSURED = parseXML.parseXmlTag(Node, "POLICYSUMASSURED");
            POLICYSUMASSURED = POLICYSUMASSURED == null ? "" : POLICYSUMASSURED;

            TIER1_MKT_NAME = parseXML.parseXmlTag(Node, "TIER1_MKT_NAME");
            TIER1_MKT_NAME = TIER1_MKT_NAME == null ? "" : TIER1_MKT_NAME;


            TIERROC_MKT_NAME = parseXML.parseXmlTag(Node, "TIERROC_MKT_NAME");
            TIERROC_MKT_NAME = TIERROC_MKT_NAME == null ? "" : TIERROC_MKT_NAME;

            ANNUALIZED_PREMIUM = parseXML.parseXmlTag(Node, "ANNUALIZED_PREMIUM");
            ANNUALIZED_PREMIUM = ANNUALIZED_PREMIUM == null ? "" : ANNUALIZED_PREMIUM;

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

            CHANNELACTIVESTATUS = parseXML.parseXmlTag(Node, "CHANNELACTIVESTATUS");
            CHANNELACTIVESTATUS = CHANNELACTIVESTATUS == null ? "" : CHANNELACTIVESTATUS;

            POLICYPAYMENTMECHANISM = parseXML.parseXmlTag(Node, "POLICYPAYMENTMECHANISM");
            POLICYPAYMENTMECHANISM = POLICYPAYMENTMECHANISM == null ? "" : POLICYPAYMENTMECHANISM;

            CUSTOMER_ID = parseXML.parseXmlTag(Node, "CUSTOMER_ID");
            CUSTOMER_ID = CUSTOMER_ID == null ? "" : CUSTOMER_ID;

            HOLDER_GENDER = parseXML.parseXmlTag(Node, "HOLDER_GENDER");
            HOLDER_GENDER = HOLDER_GENDER == null ? "" : HOLDER_GENDER;

            UM_BANKCODE = parseXML.parseXmlTag(Node, "UM_BANKCODE");
            UM_BANKCODE = UM_BANKCODE == null ? "" : UM_BANKCODE;

            BANKTYPE = parseXML.parseXmlTag(Node, "BANKTYPE");
            BANKTYPE = BANKTYPE == null ? "" : BANKTYPE;

            BANKCODE = parseXML.parseXmlTag(Node, "BANKCODE");
            BANKCODE = BANKCODE == null ? "" : BANKCODE;

            BANKNAME = parseXML.parseXmlTag(Node, "BANKNAME");
            BANKNAME = BANKNAME == null ? "" : BANKNAME;

            BANKBRANCHCODE = parseXML.parseXmlTag(Node, "BANKBRANCHCODE");
            BANKBRANCHCODE = BANKBRANCHCODE == null ? "" : BANKBRANCHCODE;

            BANKBRANCHNAME = parseXML.parseXmlTag(Node, "BANKBRANCHNAME");
            BANKBRANCHNAME = BANKBRANCHNAME == null ? "" : BANKBRANCHNAME;

            BANKCIRCLENAME = parseXML.parseXmlTag(Node, "BANKCIRCLENAME");
            BANKCIRCLENAME = BANKCIRCLENAME == null ? "" : BANKCIRCLENAME;


            BANKMODULENAME = parseXML.parseXmlTag(Node, "BANKMODULENAME");
            BANKMODULENAME = BANKMODULENAME == null ? "" : BANKMODULENAME;

            BANKREGION = parseXML.parseXmlTag(Node, "BANKREGION");
            BANKREGION = BANKREGION == null ? "" : BANKREGION;

            POLICYTYPE = parseXML.parseXmlTag(Node, "POLICYTYPE");
            POLICYTYPE = POLICYTYPE == null ? "" : POLICYTYPE;

            POLICYTYPESUB = parseXML.parseXmlTag(Node, "POLICYTYPESUB");
            POLICYTYPESUB = POLICYTYPESUB == null ? "" : POLICYTYPESUB;

            ACCURED_EXIT_PAYABLE_AMOUNT = parseXML.parseXmlTag(Node, "ACCURED_EXIT_PAYABLE_AMOUNT");
            ACCURED_EXIT_PAYABLE_AMOUNT = ACCURED_EXIT_PAYABLE_AMOUNT == null ? "" : ACCURED_EXIT_PAYABLE_AMOUNT;

            UnclaimedDataValuesModel nodeVal = new UnclaimedDataValuesModel(POLICYNUMBER, POLICYPROPOSALNUMBER, POLICYISSUEDATE, OPERATION, DUE_DATE, EFFECTIVE_DATE, CHEQUE_NO, UNCLAIMED_AMT, OPERATION_STATUS, STALE_DC_ACTION, UNCLAIMED_UPLOADED_DATE, ENTRY_DATE, ENTRY_AMOUNT, FUND_VALUE_AT_ENTRY, ENTRY_UNIT, ENTY_NAV_VALUE, ISSUANCEAGEING, CHQ_REALISED_DATE, AGEING, PC_NAME, REGION_NAME, UNCLAIM_ID, NAVVALUE, POLICYCURRENTSTATUS, CORRESPONDENCEADDRESS1, CORRESPONDENCEADDRESS2, CORRESPONDENCEADDRESS3, CORRESPONDENCECITY, CORRESPONDENCESTATE, CORRESPONDENCEPOSTCODE, CONTACTMOBILE, HOLDERNAME, LIFEASSUREDNAME, CHANNELTYPE, CUSTOMER_NAME, PREMIUMGROSSAMOUNT, FREQUENCY, DOC, PREMIUMFUP, SERVICEREGIONNAMEOPS, POLICYSUMASSURED, TIER1_MKT_NAME, TIERROC_MKT_NAME, ANNUALIZED_PREMIUM, POLICY_TERM, POLICYPAYMENTTERM, IA_CIF_CODE, IA_CIF_NAME, UM_BANKNAME, CHANNELACTIVESTATUS, POLICYPAYMENTMECHANISM, CUSTOMER_ID, HOLDER_GENDER, UM_BANKCODE, BANKTYPE, BANKCODE, BANKNAME, BANKBRANCHCODE, BANKBRANCHNAME, BANKCIRCLENAME, BANKMODULENAME, BANKREGION, POLICYTYPE, POLICYTYPESUB, ACCURED_EXIT_PAYABLE_AMOUNT);
            lsData.add(nodeVal);
        }
        return lsData;
    }
}
