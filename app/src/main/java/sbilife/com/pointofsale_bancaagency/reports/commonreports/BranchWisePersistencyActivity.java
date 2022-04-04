package sbilife.com.pointofsale_bancaagency.reports.commonreports;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class BranchWisePersistencyActivity extends AppCompatActivity implements View.OnClickListener,
        ServiceHits.DownLoadData,UpdateAltMobileNoCommonAsyncTask.UpdateAltMobNoInterface {
    private final String URl = ServiceURL.SERVICE_URL;
    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME_BRANCH_CODE = "getBranchListbanca_smrt";

    private Spinner spinnerBranchCodes/*,spnRewmonths*/;
    private TextView textviewRecordCount;
    private Button buttonOk;
    private EditText edittextSearch;
    private RecyclerView recyclerview;

    private CommonMethods commonMethods;
    private Context context;

    private DownloadFileAsyncPer taskPer;
    private Spinner spnmonths;
    private String strMonthsPer = "";
    private final String METHOD_NAME_BRANCHWISEPERSISTENCY = "getPersistency_Branch";
//    private DownloadFileAsyncPer taskPer;

    private int mYear, mMonth, mDay, datecheck = 0;

    private ArrayList<XMLHolderBranchWisePersistency> globalDataList;
    private SelectedAdapter selectedAdapter;

    private ProgressDialog mProgressDialog;
    private LinearLayout llBranchCodeList;

    private ServiceHits service;
    private DownloadBranchCodeAsyncTask downloadBranchCodeAsyncTask;
    //    private DownloadBranchWiseRenewalListAsync downloadBranchWiseRenewalListAsync;
    private ArrayList<ParseXML.BranchDetailsForBDMValuesModel> branchDetailsList;
    private String branchName = "", branchCode = "", strCIFBDMUserId = "", strCIFBDMEmailId = "",
            strCIFBDMPassword = "", strCIFBDMMObileNo = "";
    private FundValueAsyncTask fundValueAsyncTask;
    private SendRenewalSMSAsynTask sendRenewalSMSAsynTask;
    private GetPremiumAmountCommonAsync getPremiumAmountCommonAsync;
    private int finalPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.agency_reports_branchwisepersistency);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Branch Wise Persistency");

        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");

        if (fromHome != null && fromHome.equalsIgnoreCase("Y")) {
            getUserDetails();
        } else {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");


            try {
                strCIFBDMPassword = SimpleCrypto.encrypt("SBIL", "sbil");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        spinnerBranchCodes = findViewById(R.id.spinnerBranchCodes);
        textviewRecordCount = findViewById(R.id.textviewRecordCount);
//        spnRewmonths = findViewById(R.id.spnRewmonths);

        Button buttonOk = findViewById(R.id.buttonOk);
        edittextSearch = findViewById(R.id.edittextSearch);
//        recyclerview = findViewById(R.id.recyclerview);
        llBranchCodeList = findViewById(R.id.llBranchCodeList);

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        recyclerview = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        // set LayoutManager to RecyclerView
        recyclerview.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        globalDataList = new ArrayList<>();
        selectedAdapter = new SelectedAdapter(globalDataList);
        recyclerview.setAdapter(selectedAdapter);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        spnmonths = findViewById(R.id.spnmonths);

        buttonOk.setOnClickListener(this);

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
        spinnerBranchCodes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                branchName = branchDetailsList.get(position).getBANKBRANCHNAME();
                branchCode = branchDetailsList.get(position).getBANKBRANCHCODE();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        mProgressDialog.setButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (downloadBranchCodeAsyncTask != null) {
                            downloadBranchCodeAsyncTask.cancel(true);
                        }

                        if (service != null) {
                            service.cancel(true);
                        }

                        if (fundValueAsyncTask != null) {
                            fundValueAsyncTask.cancel(true);
                        }

                        if (sendRenewalSMSAsynTask != null) {
                            sendRenewalSMSAsynTask.cancel(true);
                        }
                        if (getPremiumAmountCommonAsync != null) {
                            getPremiumAmountCommonAsync.cancel(true);
                        }

                        if (mProgressDialog != null) {
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }
                    }
                });

        mProgressDialog.setMax(100);


        downloadBranchCodeAsyncTask = new DownloadBranchCodeAsyncTask();
        downloadBranchCodeAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = commonMethods.getStrAuth();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.buttonOk:
                commonMethods.hideKeyboard(edittextSearch, context);

                clearList();
//                branchName = spinnerBranchCodes.getSelectedItem().toString();
                if (commonMethods.isNetworkConnected(context)) {
                    // if (!TextUtils.isEmpty(branchCode) && branchCode.equalsIgnoreCase("Select Branch Code")) {
                    if (!TextUtils.isEmpty(branchName) && branchName.equalsIgnoreCase("Select Branch Name")) {
                        commonMethods.showMessageDialog(context, "Please Select Branch Name");
                    } else {

                        getBranchWisePersistency(branchCode, branchName);
//                        StringBuilder input = new StringBuilder();
//                        input.append(branchName + "," + branchCode);
//
//                        input.append(",").append(spnRewmonths.getSelectedItem().toString());
//                        service_hits(input.toString());
                    }
                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }
                break;
        }
    }

    private void getBranchWisePersistency(String branchCode, String branchName) {
        taskPer = new DownloadFileAsyncPer();
        strMonthsPer = spnmonths.getSelectedItem().toString();

        if (commonMethods.isNetworkConnected(context)) {
            StringBuilder input = new StringBuilder();
            input.append(branchName + "," + branchCode);
            service_hits(input.toString());
        } else {
            commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
        }
    }

    private void clearList() {
        edittextSearch.setVisibility(View.GONE);
        textviewRecordCount.setVisibility(View.GONE);
        textviewRecordCount.setText("");
        edittextSearch.setText("");

        if (globalDataList != null && selectedAdapter != null) {
            globalDataList.clear();
            selectedAdapter = new SelectedAdapter(globalDataList);
            recyclerview.setAdapter(selectedAdapter);
            recyclerview.invalidate();
        }
    }

    private void service_hits(String input) {

        service = new ServiceHits(context, METHOD_NAME_BRANCHWISEPERSISTENCY, input,
                strCIFBDMUserId, strCIFBDMEmailId,
                strCIFBDMMObileNo, strCIFBDMPassword, this);
        service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    class DownloadFileAsyncPer extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String strpersistencylist = "", strpersistencyErCd = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                SoapObject request;

                int m = 0;
                System.out.println("month per: " + strMonthsPer);
                if (strMonthsPer.contentEquals("13")) {
                    m = 1;
                } else if (strMonthsPer.contentEquals("25")) {
                    m = 2;
                } else if (strMonthsPer.contentEquals("37")) {
                    m = 3;
                } else if (strMonthsPer.contentEquals("61")) {
                    m = 4;
                }

                Calendar cal = Calendar.getInstance();
                String monthName = new SimpleDateFormat("MMM").format(cal
                        .getTime());
                //getPersistency_Branch(string strBrCode, int checkFlag,
                //string strEmailId, string strMobileNo, string strAuthKey, string strMonth)

                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME_BRANCHWISEPERSISTENCY);
                request.addProperty("strBrCode", branchCode);
                request.addProperty("checkFlag", m);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());
                request.addProperty("strMonth", monthName.toUpperCase());

                System.out.println(branchCode + " " + m + " " + strCIFBDMEmailId + " " + strCIFBDMMObileNo + " " + monthName);

                Log.d("TAG", "REQUEST" + request);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();
                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl, 800000);
                String SOAP_ACTION_PERSISTENCY = "http://tempuri.org/" + METHOD_NAME_BRANCHWISEPERSISTENCY;
                androidHttpTranport.call(SOAP_ACTION_PERSISTENCY, envelope);
                Object response = envelope.getResponse();

                if (!response.toString().contentEquals("<NewDataSet />")) {

                    ParseXML prsObj = new ParseXML();
                    String inputpolicylist = response.toString();
                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "NewDataSet");
                    strpersistencyErCd = prsObj.parseXmlTag(
                            inputpolicylist, "ScreenData");

                    if (strpersistencyErCd == null) {
                        // for agent policy list
                        List<String> Node = prsObj.parseParentNode(
                                inputpolicylist, "Table");

                        List<XMLHolderBranchWisePersistency> nodeData = parseNodeElementBranchWisePersistency(Node, strMonthsPer);

                        globalDataList.clear();
                        globalDataList.addAll(nodeData);
                        strpersistencyErCd = "success";
                    } else {
                        strpersistencyErCd = "1";
                    }

                } else {
                    strpersistencyErCd = "1";
                }
                return null;
            } catch (Exception e) {
                mProgressDialog.dismiss();
                running = false;
            }
            return null;
        }


        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            if (running) {
                if (strpersistencyErCd.equalsIgnoreCase("success")) {
                    textviewRecordCount.setText("Total Record :" + globalDataList.size());
                    edittextSearch.setVisibility(View.VISIBLE);
                    selectedAdapter = new SelectedAdapter(globalDataList);
                    recyclerview.setAdapter(selectedAdapter);
                    recyclerview.invalidate();
                } else {
                    commonMethods.showMessageDialog(context, "No Record found");
                    clearList();
                }
            } else {
                commonMethods.showMessageDialog(context, "No Record found");
                clearList();
            }

        }
    }


    private void startDownloadPersistency() {
        taskPer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void downLoadData() {
        taskPer = new DownloadFileAsyncPer();
        startDownloadPersistency();
    }


    class DownloadBranchCodeAsyncTask extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String status = "";

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

                request = new SoapObject(NAMESPACE, METHOD_NAME_BRANCH_CODE);
                request.addProperty("strEmpID", strCIFBDMUserId);
                commonMethods.appendSecurityParams(context, request, "", "");

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();


                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME_BRANCH_CODE;
                androidHttpTranport.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();
                String inputpolicylist = response.toString();

                if (!inputpolicylist.contentEquals("<CIFPolicyList />")) {

                    ParseXML prsObj = new ParseXML();

                    inputpolicylist = prsObj.parseXmlTag(
                            inputpolicylist, "CIFPolicyList");
                    status = prsObj.parseXmlTag(
                            inputpolicylist, "ScreenData");

                    if (status == null) {

                        List<String> Node = prsObj.parseParentNode(
                                inputpolicylist, "Table");

                        ParseXML parseXML = new ParseXML();

                        List<ParseXML.BranchDetailsForBDMValuesModel> nodeData = prsObj
                                .parseNodeBranchDetailsForBDM(Node);

                        branchDetailsList = new ArrayList<>();
                        branchDetailsList.clear();
                        branchName = "Select Branch Name";
                        branchCode = "0";

                        ParseXML.BranchDetailsForBDMValuesModel listNode =
                                prsObj.new BranchDetailsForBDMValuesModel(branchCode,
                                        branchName);
                        nodeData.add(0, listNode);

                        branchDetailsList.addAll(nodeData);
                        status = "Success";
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
            llBranchCodeList.setVisibility(View.GONE);
            if (running) {
                if (status.equalsIgnoreCase("Success")) {
                    llBranchCodeList.setVisibility(View.VISIBLE);
                    SpinnerAdapter spinnerAdapter = new SpinnerAdapter(branchDetailsList);
                    spinnerBranchCodes.setAdapter(spinnerAdapter);

                } else {
                    commonMethods.showMessageDialog(context, "No Record Found");
                }
            } else {
                commonMethods.showMessageDialog(context, "No Record Found");
            }
        }
    }

    class SpinnerAdapter extends BaseAdapter {

        class ViewHolder {
            TextView c1;
        }

        private ArrayList<ParseXML.BranchDetailsForBDMValuesModel> allElementDetails;
        private LayoutInflater mInflater;


        public SpinnerAdapter(ArrayList<ParseXML.BranchDetailsForBDMValuesModel> results) {
            allElementDetails = results;
            mInflater = LayoutInflater.from(context);
        }


        public int getCount() {
            return allElementDetails.size();
        }

        public Object getItem(int position) {
            return allElementDetails.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public int getPosition(String resource_type) {
            return allElementDetails.indexOf(resource_type);
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.textview_default, parent,
                        false);
                holder = new ViewHolder();
                holder.c1 = convertView.findViewById(R.id.tv_content);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.c1.setText(String.valueOf(allElementDetails.get(position).getBANKBRANCHNAME()));

            return convertView;
        }

    }


    public class SelectedAdapter extends RecyclerView.Adapter<SelectedAdapter.
            ViewHolderAdapter> implements Filterable {

        private ArrayList<XMLHolderBranchWisePersistency> lstAdapterList, lstSearch;

        SelectedAdapter(ArrayList<XMLHolderBranchWisePersistency> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<XMLHolderBranchWisePersistency> results = new ArrayList<>();

                    if (lstSearch == null)
                        lstSearch = lstAdapterList;

                    String strSearch = charSequence == null ? "" : charSequence.toString();

                    if (!strSearch.equals("")) {
                        if (lstSearch != null && lstSearch.size() > 0) {
                            for (final XMLHolderBranchWisePersistency model : lstSearch) {
                                if (model.getPOLICYNUMBER().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getCUSTOMERNAME().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getPOLICYCURRENTSTATUS().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getPREMIUMGROSSAMOUNT().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getPREMIUMFUP().toLowerCase().contains(charSequence.toString().toLowerCase())
                                ) {
                                    results.add(model);
                                }
                            }
                        }
                        oReturn.values = results;
                    } else {
                        oReturn.values = globalDataList;
                    }
                    return oReturn;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    lstAdapterList = (ArrayList<XMLHolderBranchWisePersistency>) results.values;
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
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_item_branchwise_persistency, parent, false);

            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final ViewHolderAdapter holder, int position) {
            holder.textviewPolicyNumber.setText(lstAdapterList.get(position).getPOLICYNUMBER());


            holder.textviewContactNumber.setText(lstAdapterList.get(position).getCUSTOMERMOBILE());

            holder.tvPremiumGrossAmount.setText(lstAdapterList.get(position).getPREMIUMGROSSAMOUNT());
            holder.tvResidualAmount.setText(lstAdapterList.get(position).getRESIDUAL_AMOUNT());
            holder.tvRAGFlag.setText(lstAdapterList.get(position).getRAG_FLAG());
            holder.tvPremiumFUP.setText(lstAdapterList.get(position).getPREMIUMFUP());
            holder.tvCustomerName.setText(lstAdapterList.get(position).getCUSTOMERNAME());
            holder.tvCollectedAmount.setText(lstAdapterList.get(position).getCOLLECTED_AMOUNT());
            holder.tvDueDate.setText(lstAdapterList.get(position).getDUE_DATE());


            holder.tvCollectableAmount.setText(lstAdapterList.get(position).getCOLLECTABLE_AMOUNT());

            String policyCurrentStatus = lstAdapterList.get(position).getPOLICYCURRENTSTATUS();

            holder.txtPolicyCurrentStatus.setText(policyCurrentStatus);
            if (policyCurrentStatus.equalsIgnoreCase("Lapse")) {
                holder.imageviewSMS.setVisibility(View.INVISIBLE);
            } else {
                holder.imageviewSMS.setVisibility(View.VISIBLE);
            }


            holder.imgcontact_cust_r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mobileNumber = holder.textviewContactNumber.getText().toString();

                    if (!TextUtils.isEmpty(mobileNumber)) {
                        commonMethods.callMobileNumber(mobileNumber, context);
                    }
                }
            });
            holder.textviewContactNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mobileNumber = holder.textviewContactNumber.getText().toString();
                    if (!TextUtils.isEmpty(mobileNumber)) {
                        commonMethods.callMobileNumber(mobileNumber, context);
                    }
                }
            });

            holder.imageviewSMS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //commonMethods.hideKeyboard(, context);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                    builder.setTitle("Choose Communication Medium");
                    finalPosition = 0;
                    //final String[] languagesArray = {"English", "Hindi", "Telugu"};
//                    final String[] languagesArray = {"English"};
                    //final String[] commMediumArray = {"SMS", "Email"};
                    final String[] commMediumArray = {"SMS"};
                    // cow
                    builder.setSingleChoiceItems(commMediumArray, finalPosition, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finalPosition = which;
                        }
                    });

                    builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String commMedium = commMediumArray[finalPosition];
                            if (commonMethods.isNetworkConnected(context)) {
                                int index = holder.getAdapterPosition();
                                if (commMedium.equalsIgnoreCase("SMS")) {

                                    final String mobileNumber = holder.textviewContactNumber.getText().toString();
                                    final String policyNumber = holder.textviewPolicyNumber.getText().toString();

                                    final String dueDate = holder.tvDueDate.getText().toString();
                                    final String status = holder.txtPolicyCurrentStatus.getText().toString();
                                    final String amount = holder.tvPremiumGrossAmount.getText().toString();


                                    sendRenewalSMSAsynTask = new SendRenewalSMSAsynTask(policyNumber, mobileNumber, dueDate,
                                            status, amount, "English",
                                            context, BranchWisePersistencyActivity.this::getSMSDetailsInterfaceMethod);
                                    sendRenewalSMSAsynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else if (commMedium.equalsIgnoreCase("Email")) {
                                    /*final String dueDate = holder.textviewPersistencyDueDataDueDate.getText().toString();
                                    final String paymentMechanism = "";//lst.get(index).getPOLICYPAYMENTMECHANISM();
                                    final String policyNumber = holder.textviewPersistencyDueDataPolicyNumber.getText().toString();
                                    final String emailid = "";//lst.get(index).getEMAILID();
                                    final String amount = holder.textviewPersistencyDueDataPremiumGrossAmount.getText().toString();
                                    final String name = lst.get(index).getCUSTOMERNAME();

                                    String mode = "";

                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy");
                                    Date renewalDate = null;
                                    try {
                                        renewalDate = sdf.parse(dueDate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if (new Date().before(renewalDate)
                                            && !(paymentMechanism.equalsIgnoreCase("ATM"))
                                            && !(paymentMechanism.equalsIgnoreCase("Direct Bill"))) {
                                        mode = "Pre Alter";
                                    } else if (new Date().before(renewalDate)
                                            && paymentMechanism.equalsIgnoreCase("ATM")
                                            && paymentMechanism.equalsIgnoreCase("Direct Bill")) {
                                        mode = "Pre Non Alter";
                                    } else if (new Date().after(renewalDate)) {
                                        mode = "Post Non Alter";
                                    }
                                    System.out.println("mode = " + mode);
                                    System.out.println("mode = " + paymentMechanism);
                                    CommonReportsPersistencyDueDataActivity.SendRenewalDueEmailAsyncTask sendRenewalDueEmailAsyncTask =
                                            new CommonReportsPersistencyDueDataActivity.SendRenewalDueEmailAsyncTask(policyNumber, emailid, dueDate, amount, mode,
                                                    name);
                                    sendRenewalDueEmailAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                */}


                            } else {
                                commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", null);

                    AlertDialog dialog = builder.create();
                    dialog.setCancelable(false);
                    dialog.show();
                }
            });


            if (TextUtils.isEmpty(lstAdapterList.get(position).getCONTACTOFFICE())) {
                holder.llOfficeMaster.setVisibility(View.GONE);
            } else {
                holder.llOfficeMaster.setVisibility(View.VISIBLE);
                holder.textviewContactOffice.setText(lstAdapterList.get(position).getCONTACTOFFICE());
                holder.textviewContactOffice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(holder.textviewContactOffice.getText().toString())) {
                            commonMethods.callMobileNumber(holder.textviewContactOffice.getText().toString(), context);
                        }
                    }
                });
                holder.LLofficeContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(holder.textviewContactOffice.getText().toString())) {
                            commonMethods.callMobileNumber(holder.textviewContactOffice.getText().toString(), context);
                        }
                    }
                });
            }


            if (TextUtils.isEmpty(lstAdapterList.get(position).getCONTACTRESIDENCE())) {
                holder.llResidenceMaster.setVisibility(View.GONE);
            } else {
                holder.llResidenceMaster.setVisibility(View.VISIBLE);
                holder.textviewResidenceOffice.setText(lstAdapterList.get(position).getCONTACTRESIDENCE());
                holder.textviewResidenceOffice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(holder.textviewResidenceOffice.getText().toString())) {
                            commonMethods.callMobileNumber(holder.textviewResidenceOffice.getText().toString(), context);
                        }
                    }
                });
                holder.LLResidenceContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(holder.textviewResidenceOffice.getText().toString())) {
                            commonMethods.callMobileNumber(holder.textviewResidenceOffice.getText().toString(), context);
                        }
                    }
                });

            }

            if (lstAdapterList.get(position).getPOLICYTYPE().equalsIgnoreCase("ULIP")) {
            holder.llFundValue.setVisibility(View.VISIBLE);
            holder.buttonFundValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //commonMethods.hideKeyboard(edittextSearch, context);
                    int index = holder.getAdapterPosition();
                    String holderId = lstAdapterList.get(index).getHOLDERID();
                    String policyNumber = lstAdapterList.get(index).getPOLICYNUMBER();
                    fundValueAsyncTask = new FundValueAsyncTask(context, holderId, policyNumber,
                            BranchWisePersistencyActivity.this::getFundValueInterfaceMethod);
                    fundValueAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            });

            }else{
                holder.llFundValue.setVisibility(View.GONE);
            }
            holder.tvDOC.setText(lstAdapterList.get(position).getPOLICYRISKCOMMENCEMENTDATE());
            holder.tvPaymentMechanism.setText(lstAdapterList.get(position).getPOLICYPAYMENTMECHANISM());

            holder.buttonCRM.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    int index = holder.getAdapterPosition();
                    String premiumFUP = lstAdapterList.get(index).getPREMIUMFUP();
                    String selectedPolicyNumber = lstAdapterList.get(index).getPOLICYNUMBER();
                    commonMethods.showDispositionAlert(context, premiumFUP, selectedPolicyNumber,
                            strCIFBDMEmailId, strCIFBDMMObileNo, strCIFBDMUserId);
                }
            });

            holder.buttonUpdatAltMobile.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int index = holder.getAdapterPosition();
                    //showDispositionAlert(lstAdapterList.get(index));
                    final String premiumFUP = lstAdapterList.get(index).getPREMIUMFUP();
                    final String policyNumber = lstAdapterList.get(index).getPOLICYNUMBER();
                    commonMethods.updateAltMobileAlert(context, premiumFUP,  policyNumber,
                            BranchWisePersistencyActivity.this);

                }
            });
            holder.tvActualPremium.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = holder.getAdapterPosition();
                    String selectedPolicyNumber = lstAdapterList.get(index).getPOLICYNUMBER();
                    if (lstAdapterList.get(position).getPOLICYCURRENTSTATUS().equalsIgnoreCase("Lapse") ||
                            lstAdapterList.get(position).getPOLICYCURRENTSTATUS().equalsIgnoreCase("Technical Lapse")) {
                        getPremiumAmountCommonAsync = new GetPremiumAmountCommonAsync(selectedPolicyNumber, context,
                                BranchWisePersistencyActivity.this::getPremiumInterfaceMethod);
                        getPremiumAmountCommonAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        String msg = "Gross Premium Amount is - " + lstAdapterList.get(index).getPREMIUMGROSSAMOUNT();
                        commonMethods.showMessageDialog(context, msg);
                    }
                }
            });
        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder {


            private final TextView tvPremiumGrossAmount, tvResidualAmount, tvRAGFlag,
                    tvPremiumFUP, tvCustomerName, tvCollectedAmount, tvDueDate, txtPolicyCurrentStatus,
                    textviewContactNumber, textviewPolicyNumber,tvCollectableAmount;
            private ImageView imgcontact_cust_r,imageviewSMS;

            private final TextView tvDOC, textviewContactOffice, textviewResidenceOffice,buttonFundValue,
                    tvActualPremium,tvPaymentMechanism;
            private final LinearLayout LLofficeContact, LLResidenceContact, llResidenceMaster, llOfficeMaster,
                    llFundValue,llCRMPaymentDetails;
            private final Button buttonUpdatAltMobile, buttonCRM;

            ViewHolderAdapter(View v) {
                super(v);
                textviewPolicyNumber = v.findViewById(R.id.textviewPolicyNumber);
                tvPremiumGrossAmount = v.findViewById(R.id.tvPremiumGrossAmount);
                tvResidualAmount = v.findViewById(R.id.tvResidualAmount);
                tvRAGFlag = v.findViewById(R.id.tvRAGFlag);
                tvPremiumFUP = v.findViewById(R.id.tvPremiumFUP);
                tvCustomerName = v.findViewById(R.id.tvCustomerName);
                tvCollectedAmount = v.findViewById(R.id.tvCollectedAmount);
                tvDueDate = v.findViewById(R.id.tvDueDate);
                txtPolicyCurrentStatus = v.findViewById(R.id.txtPolicyCurrentStatus);
                textviewContactNumber = v.findViewById(R.id.textviewContactNumber);
                tvCollectableAmount = v.findViewById(R.id.tvCollectableAmount);
                imgcontact_cust_r = v.findViewById(R.id.imgcontact_cust_r);
                imageviewSMS = v.findViewById(R.id.imageviewSMS);


                tvDOC = v.findViewById(R.id.tvDOC);
                textviewContactOffice = v.findViewById(R.id.textviewContactOffice);
                LLofficeContact = v.findViewById(R.id.LLofficeContact);
                llOfficeMaster = v.findViewById(R.id.llOfficeMaster);

                textviewResidenceOffice = v.findViewById(R.id.textviewResidenceOffice);
                LLResidenceContact = v.findViewById(R.id.LLResidenceContact);
                llResidenceMaster = v.findViewById(R.id.llResidenceMaster);

                llFundValue = v.findViewById(R.id.llFundValue);
                llFundValue.setVisibility(View.GONE);
                buttonFundValue = v.findViewById(R.id.buttonFundValue);
                tvActualPremium = v.findViewById(R.id.tvActualPremium);
                tvPaymentMechanism = v.findViewById(R.id.tvPaymentMechanism);
                buttonUpdatAltMobile = v.findViewById(R.id.buttonUpdatAltMobile);
                buttonCRM = v.findViewById(R.id.buttonCRM);

                llCRMPaymentDetails = v.findViewById(R.id.llCRMPaymentDetails);
                llCRMPaymentDetails.setVisibility(View.VISIBLE);
            }

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

        if (downloadBranchCodeAsyncTask != null) {
            downloadBranchCodeAsyncTask.cancel(true);
        }
        if (taskPer != null) {
            taskPer.cancel(true);
        }
        if (service != null) {
            service.cancel(true);
        }

        if (fundValueAsyncTask != null) {
            fundValueAsyncTask.cancel(true);
        }

        if (sendRenewalSMSAsynTask != null) {
            sendRenewalSMSAsynTask.cancel(true);
        }
        if (getPremiumAmountCommonAsync != null) {
            getPremiumAmountCommonAsync.cancel(true);
        }
    }


    public void getFundValueInterfaceMethod(List<String> Node, String policyNumber) {

        if (TextUtils.isEmpty(policyNumber)) {
            commonMethods.showMessageDialog(context, commonMethods.NO_RECORD_FOUND);
        } else {
            String result = "", fundValueStr = "";
            ParseXML parseXML = new ParseXML();
            List<ParseXML.XMLFundSwitchHolder> nodeData = parseXML.parseNodeElementFundSwitch(Node);
            for (ParseXML.XMLFundSwitchHolder node : nodeData) {
                System.out.println("sa.toString() = " + policyNumber);
                if (policyNumber.equalsIgnoreCase(node.getPOLICYNO())) {
                    fundValueStr = node.getFUNDVALUE();
                    System.out.println("sa.toString() = " + fundValueStr);
                    result = "Success";
                }
            }

            if (result.equalsIgnoreCase("Success")) {
                commonMethods.showMessageDialog(context, "Fund Value Is : " + fundValueStr);
            } else {
                commonMethods.showMessageDialog(context, commonMethods.NO_RECORD_FOUND);
            }
        }
    }

    void getSMSDetailsInterfaceMethod(String result) {
        if (result.equalsIgnoreCase("1")) {
            commonMethods.showMessageDialog(context, "Message sent successfully");
        } else {
            commonMethods.showMessageDialog(context, "Message sending failed");
        }
    }

    void getPremiumInterfaceMethod(String premiumAmount,String result) {
        if (premiumAmount.equals("")) {
            commonMethods.showMessageDialog(context, result);
        } else {
            commonMethods.showMessageDialog(context, "Gross Premium Amount is - " + premiumAmount);
        }
    }

    public void getUpdateAltMobResultMethod(String result) {
        if (result != null && result.equalsIgnoreCase("Success")) {
            commonMethods.showMessageDialog(context, "Mobile Number Updated Successfully");
        } else {
            commonMethods.showMessageDialog(context, "Mobile Number Not Updated.Please Try Again.");
        }
    }


    public List<XMLHolderBranchWisePersistency> parseNodeElementBranchWisePersistency(
            List<String> lsNode, String strMonth) {

        List<XMLHolderBranchWisePersistency> lsData = new ArrayList<XMLHolderBranchWisePersistency>();

        String POLICYNUMBER, POLICYCURRENTSTATUS, DUE_DATE, COLLECTABLE_AMOUNT, COLLECTED_AMOUNT,
                CUSTOMERMOBILE, CUSTOMERNAME, PREMIUMFUP, PREMIUMGROSSAMOUNT, RAG_FLAG, RESIDUAL_AMOUNT;

        String POLICYRISKCOMMENCEMENTDATE, CONTACTRESIDENCE,CONTACTOFFICE,POLICYTYPE,
                POLICYPAYMENTMECHANISM,HOLDERID;
        int colorCode = 0;
        ParseXML parseXML = new ParseXML();
        for (String Node : lsNode) {
            POLICYNUMBER = parseXML.parseXmlTag(Node, "POLICYNUMBER");
            POLICYNUMBER = POLICYNUMBER == null ? "" : POLICYNUMBER;

            HOLDERID = parseXML.parseXmlTag(Node, "HOLDERID");
            HOLDERID = HOLDERID == null ? "" : HOLDERID;

            POLICYCURRENTSTATUS = parseXML.parseXmlTag(Node, "POLICYCURRENTSTATUS");
            POLICYCURRENTSTATUS = POLICYCURRENTSTATUS == null ? "" : POLICYCURRENTSTATUS;

            DUE_DATE = parseXML.parseXmlTag(Node, "DUE_DATE");
            DUE_DATE = DUE_DATE == null ? "" : DUE_DATE;

            COLLECTABLE_AMOUNT = parseXML.parseXmlTag(Node, "COLLECTABLE_AMOUNT");
            COLLECTABLE_AMOUNT = COLLECTABLE_AMOUNT == null ? "" : COLLECTABLE_AMOUNT;

            COLLECTED_AMOUNT = parseXML.parseXmlTag(Node, "COLLECTED_AMOUNT");
            COLLECTED_AMOUNT = COLLECTED_AMOUNT == null ? "" : COLLECTED_AMOUNT;

            CUSTOMERMOBILE = parseXML.parseXmlTag(Node, "CUSTOMERMOBILE");
            CUSTOMERMOBILE = CUSTOMERMOBILE == null ? "" : CUSTOMERMOBILE;

            CUSTOMERNAME = parseXML.parseXmlTag(Node, "CUSTOMERNAME");
            CUSTOMERNAME = CUSTOMERNAME == null ? "" : CUSTOMERNAME;

            PREMIUMFUP = parseXML.parseXmlTag(Node, "PREMIUMFUP");
            PREMIUMFUP = PREMIUMFUP == null ? "" : PREMIUMFUP;

            PREMIUMGROSSAMOUNT = parseXML.parseXmlTag(Node, "PREMIUMGROSSAMOUNT");
            PREMIUMGROSSAMOUNT = PREMIUMGROSSAMOUNT == null ? "" : PREMIUMGROSSAMOUNT;

            RAG_FLAG = parseXML.parseXmlTag(Node, "RAG_FLAG");
            RAG_FLAG = RAG_FLAG == null ? "" : RAG_FLAG;

            RESIDUAL_AMOUNT = parseXML.parseXmlTag(Node, "RESIDUAL_AMOUNT");
            RESIDUAL_AMOUNT = RESIDUAL_AMOUNT == null ? "" : RESIDUAL_AMOUNT;

            POLICYRISKCOMMENCEMENTDATE = parseXML.parseXmlTag(Node, "POLICYRISKCOMMENCEMENTDATE");
            POLICYRISKCOMMENCEMENTDATE = POLICYRISKCOMMENCEMENTDATE == null ? "" : POLICYRISKCOMMENCEMENTDATE;

            try {
                POLICYRISKCOMMENCEMENTDATE = POLICYRISKCOMMENCEMENTDATE.split("T")[0];

                Date dt1 = null;
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat df1 = new SimpleDateFormat("dd-MMMM-yyyy");

                dt1 = df.parse(POLICYRISKCOMMENCEMENTDATE);

                POLICYRISKCOMMENCEMENTDATE = df1.format(dt1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            CONTACTRESIDENCE = parseXML.parseXmlTag(Node, "CONTACTRESIDENCE");
            CONTACTRESIDENCE = CONTACTRESIDENCE == null ? "" : CONTACTRESIDENCE;

            CONTACTOFFICE = parseXML.parseXmlTag(Node, "CONTACTOFFICE");
            CONTACTOFFICE = CONTACTOFFICE == null ? "" : CONTACTOFFICE;


            POLICYTYPE = parseXML.parseXmlTag(Node, "POLICYTYPE");
            POLICYTYPE = POLICYTYPE == null ? "" : POLICYTYPE;


            POLICYPAYMENTMECHANISM = parseXML.parseXmlTag(Node, "POLICYPAYMENTMECHANISM");
            POLICYPAYMENTMECHANISM = POLICYPAYMENTMECHANISM == null ? "" : POLICYPAYMENTMECHANISM;

            XMLHolderBranchWisePersistency nodeVal = new XMLHolderBranchWisePersistency(POLICYNUMBER,
                    POLICYCURRENTSTATUS, DUE_DATE, COLLECTABLE_AMOUNT, COLLECTED_AMOUNT, CUSTOMERMOBILE,
                    CUSTOMERNAME, PREMIUMFUP, PREMIUMGROSSAMOUNT, RAG_FLAG, RESIDUAL_AMOUNT,
                    colorCode,POLICYRISKCOMMENCEMENTDATE, CONTACTRESIDENCE, CONTACTOFFICE,
                    POLICYTYPE,POLICYPAYMENTMECHANISM,HOLDERID);
            lsData.add(nodeVal);
        }
        return lsData;
    }

    public class XMLHolderBranchWisePersistency {

        String POLICYNUMBER, POLICYCURRENTSTATUS, DUE_DATE, COLLECTABLE_AMOUNT, COLLECTED_AMOUNT,
                CUSTOMERMOBILE, CUSTOMERNAME, PREMIUMFUP, PREMIUMGROSSAMOUNT, RAG_FLAG, RESIDUAL_AMOUNT;
        private int colorCode;
        private String POLICYRISKCOMMENCEMENTDATE, CONTACTRESIDENCE,
                CONTACTOFFICE,POLICYTYPE,POLICYPAYMENTMECHANISM;
        private String HOLDERID;
        public XMLHolderBranchWisePersistency(String POLICYNUMBER, String POLICYCURRENTSTATUS, String DUE_DATE,
                                              String COLLECTABLE_AMOUNT, String COLLECTED_AMOUNT,
                                              String CUSTOMERMOBILE, String CUSTOMERNAME, String PREMIUMFUP,
                                              String PREMIUMGROSSAMOUNT, String RAG_FLAG, String RESIDUAL_AMOUNT,
                                              int colorCode, String POLICYRISKCOMMENCEMENTDATE,
                                              String CONTACTRESIDENCE, String CONTACTOFFICE,String POLICYTYPE,
                                              String POLICYPAYMENTMECHANISM,String HOLDERID) {
            this.POLICYNUMBER = POLICYNUMBER;
            this.POLICYCURRENTSTATUS = POLICYCURRENTSTATUS;
            this.DUE_DATE = DUE_DATE;
            this.COLLECTABLE_AMOUNT = COLLECTABLE_AMOUNT;
            this.COLLECTED_AMOUNT = COLLECTED_AMOUNT;
            this.CUSTOMERMOBILE = CUSTOMERMOBILE;
            this.CUSTOMERNAME = CUSTOMERNAME;
            this.PREMIUMFUP = PREMIUMFUP;
            this.PREMIUMGROSSAMOUNT = PREMIUMGROSSAMOUNT;
            this.RAG_FLAG = RAG_FLAG;
            this.RESIDUAL_AMOUNT = RESIDUAL_AMOUNT;
            this.colorCode = colorCode;

            this.POLICYRISKCOMMENCEMENTDATE = POLICYRISKCOMMENCEMENTDATE;
            this.CONTACTRESIDENCE = CONTACTRESIDENCE;
            this.CONTACTOFFICE = CONTACTOFFICE;
            this.POLICYTYPE = POLICYTYPE;
            this.POLICYPAYMENTMECHANISM = POLICYPAYMENTMECHANISM;
            this.HOLDERID = HOLDERID;
        }

        public String getPOLICYNUMBER() {
            return POLICYNUMBER;
        }

        public String getPOLICYCURRENTSTATUS() {
            return POLICYCURRENTSTATUS;
        }

        public String getDUE_DATE() {
            return DUE_DATE;
        }

        public String getCOLLECTABLE_AMOUNT() {
            return COLLECTABLE_AMOUNT;
        }

        public String getCOLLECTED_AMOUNT() {
            return COLLECTED_AMOUNT;
        }

        public String getCUSTOMERMOBILE() {
            return CUSTOMERMOBILE;
    }

        public String getCUSTOMERNAME() {
            return CUSTOMERNAME;
        }

        public String getPREMIUMFUP() {
            return PREMIUMFUP;
        }

        public String getPREMIUMGROSSAMOUNT() {
            return PREMIUMGROSSAMOUNT;
        }

        public String getRAG_FLAG() {
            return RAG_FLAG;
        }

        public String getRESIDUAL_AMOUNT() {
            return RESIDUAL_AMOUNT;
        }

        public int getColorCode() {
            return colorCode;
        }

        public String getPOLICYRISKCOMMENCEMENTDATE() {
            return POLICYRISKCOMMENCEMENTDATE;
        }

        public String getCONTACTRESIDENCE() {
            return CONTACTRESIDENCE;
        }

        public String getCONTACTOFFICE() {
            return CONTACTOFFICE;
        }

        public String getPOLICYTYPE() {
            return POLICYTYPE;
        }

        public String getPOLICYPAYMENTMECHANISM() {
            return POLICYPAYMENTMECHANISM;
        }

        public String getHOLDERID() {
            return HOLDERID;
        }
    }

}
