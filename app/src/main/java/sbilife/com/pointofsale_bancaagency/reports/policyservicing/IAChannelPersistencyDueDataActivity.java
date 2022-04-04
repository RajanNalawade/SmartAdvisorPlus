package sbilife.com.pointofsale_bancaagency.reports.policyservicing;

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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.RevivalQuotationActivity;

public class IAChannelPersistencyDueDataActivity extends AppCompatActivity implements ServiceHits.DownLoadData {
    private final String METHOD_NAME = "getDetail_Notcollected_pers_IAC";
    private String NAMESPACE = "http://tempuri.org/";
    private String URl = ServiceURL.SERVICE_URL;
    private Context context;
    private CommonMethods commonMethods;

    private DownloadPersistencyDueDataAsync downloadPersistencyDueDataAsync;
    private ServiceHits service;
    private ProgressDialog mProgressDialog;

    private String strCIFBDMUserId = "", strCIFBDMEmailId = "",
            strCIFBDMPassword = "", strCIFBDMMObileNo = "";
    private int finalPosition = 0;
    private ArrayList<IAChannelPersistencyDueDataValuesModel> globleList;

    private RecyclerView recyclerview;
    private TextView textviewRecordCount;
    private EditText edittextSearch;
    private Spinner spinnerPaymentStatus;
    private SelectedAdapter selectedAdapter;
    private boolean filterFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_i_a_channel_persistency_due_data);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Persistency Due Data");

        recyclerview = findViewById(R.id.recyclerview);
        spinnerPaymentStatus = findViewById(R.id.spinnerPaymentStatus);
        textviewRecordCount = findViewById(R.id.textviewRecordCount);
        textviewRecordCount.setVisibility(View.GONE);
        edittextSearch = findViewById(R.id.edittextSearch);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        // set LayoutManager to RecyclerView
        recyclerview.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        globleList = new ArrayList<>();
        selectedAdapter = new SelectedAdapter(globleList);
        recyclerview.setAdapter(selectedAdapter);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        edittextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterFlag = true;
                selectedAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        spinnerPaymentStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                    //edittextSearch.setVisibility(View.VISIBLE);
                    textviewRecordCount.setText("Total Record :" + globleList.size());
                    selectedAdapter = new SelectedAdapter(globleList);
                    recyclerview.setAdapter(selectedAdapter);
                    recyclerview.invalidate();
                } else {
                    filterFlag = false;
                    String paymentStatus = spinnerPaymentStatus.getSelectedItem().toString();
                    selectedAdapter.getFilter().filter(paymentStatus);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Intent intent = getIntent();
        getUserDetails();
        String bdmCifCOde = intent.getStringExtra("strBDMCifCOde");
        if (bdmCifCOde != null) {
            strCIFBDMUserId = bdmCifCOde;
        }
        if (commonMethods.isNetworkConnected(context)) {
            service_hits();
        } else {
            commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
        }
    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }


    @Override
    public void downLoadData() {
        downloadPersistencyDueDataAsync = new DownloadPersistencyDueDataAsync();
        downloadPersistencyDueDataAsync.execute();
    }

    class DownloadPersistencyDueDataAsync extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String strPersistencyDueDataError = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading Please wait...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);

            mProgressDialog.setButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            if (downloadPersistencyDueDataAsync != null) {
                                downloadPersistencyDueDataAsync.cancel(true);
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
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request = null;
                //String UserType = commonMethods.GetUserType(context);

                request = new SoapObject(NAMESPACE,
                        METHOD_NAME);
                //getDetail_Notcollected_pers_IAC(string strAgenyCode,
                // string strEmailId, string strMobileNo, string strAuthKey)
                request.addProperty("strAgenyCode", strCIFBDMUserId);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword);


                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);


                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl, 300000);
                try {
                    String SOAP_ACTION_PERSISTENCY_DUE_DATA = "http://tempuri.org/" + METHOD_NAME;
                    androidHttpTranport.call(
                            SOAP_ACTION_PERSISTENCY_DUE_DATA, envelope);
                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("<NewDataSet />")) {
                        System.out.println("response:" + response.toString());
                        SoapPrimitive sa = null;
                        sa = (SoapPrimitive) envelope.getResponse();

                        String inputpolicylist = sa.toString();

                        ParseXML prsObj = new ParseXML();

                        inputpolicylist = prsObj.parseXmlTag(
                                inputpolicylist, "NewDataSet");
                        inputpolicylist = new ParseXML().parseXmlTag(
                                inputpolicylist, "ScreenData");
                        strPersistencyDueDataError = inputpolicylist;

                        if (strPersistencyDueDataError == null) {
                            // for agent policy list

                            inputpolicylist = sa.toString();
                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "NewDataSet");

                            List<String> Node = prsObj.parseParentNode(
                                    inputpolicylist, "Table");

                            List<IAChannelPersistencyDueDataValuesModel> nodeData = parseNodeIAChannelPersistencyDueData(Node);

                            // final List<XMLHolder> lstPolicyList;
                            globleList = new ArrayList<>();
                            globleList.clear();
                            globleList.addAll(nodeData);
                        }
                    }

                } catch (Exception e) {
                    mProgressDialog.dismiss();
                    running = false;
                    e.printStackTrace();
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

            edittextSearch.setVisibility(View.GONE);
            textviewRecordCount.setVisibility(View.GONE);
            if (running) {
                if (strPersistencyDueDataError == null) {
                    //edittextSearch.setVisibility(View.VISIBLE);
                    textviewRecordCount.setVisibility(View.VISIBLE);
                    textviewRecordCount.setText("Total Record :" + globleList.size());
                    selectedAdapter = new SelectedAdapter(globleList);
                    recyclerview.setAdapter(selectedAdapter);
                    recyclerview.invalidate();
                } else {
                    clearList();
                    commonMethods.showMessageDialog(context, "No record found.");
                }
            } else {
                textviewRecordCount.setText("Total Record :0");
                clearList();
            }
        }
    }


    public class SelectedAdapter extends RecyclerView.Adapter<SelectedAdapter.
            ViewHolderAdapter> implements Filterable {

        private ArrayList<IAChannelPersistencyDueDataValuesModel> lstAdapterList, lstSearch;

        SelectedAdapter(ArrayList<IAChannelPersistencyDueDataValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        lstSearch = globleList;
                    } else {
                        final ArrayList<IAChannelPersistencyDueDataValuesModel> results = new ArrayList<>();
                        for (final IAChannelPersistencyDueDataValuesModel model : globleList) {
                            if (filterFlag) {
                                if (model.getPOLICYNUMBER().toLowerCase().contains(charSequence.toString().toLowerCase())
                                       /* || model.getCUSTOMER_NAME().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getMEDICAL_DONE_DATE().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getMAIN_STATUS().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getINTIMATION_DATE().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getTEST_NAME().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getSUB_STATUS().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getSUB_STATUS1().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getCALLING_HISTORY().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getCALLING_REMARKS().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getLINK_FOR_SCHEDULING().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getTYPE().toLowerCase().contains(charSequence.toString().toLowerCase())*/
                                        || model.getPREMIUMPAYMENTFREQUENCY().toLowerCase().contains(charSequence.toString().toLowerCase())
                                ) {
                                    results.add(model);
                                }
                            } else {
                                if (model.getPAID_STATUS().toLowerCase().equalsIgnoreCase(charSequence.toString().toLowerCase())) {
                                    results.add(model);
                                }
                            }

                        }

                        lstAdapterList = results;
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = lstAdapterList;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    lstAdapterList = (ArrayList<IAChannelPersistencyDueDataValuesModel>) results.values;
                    textviewRecordCount.setText("Total Record :" + lstAdapterList.size());
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
        public SelectedAdapter.ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_item_persistency_due_data, parent, false);

            return new SelectedAdapter.ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final SelectedAdapter.ViewHolderAdapter holder, final int position) {
            holder.textviewPersistencyDueDataCustomerName.setText(lstAdapterList.get(position).getCUSTOMERNAME() == null ? "" : lstAdapterList.get(position).getCUSTOMERNAME());
            holder.textviewPersistencyDueDataPolicyNumber
                    .setText(lstAdapterList.get(position).getPOLICYNUMBER() == null ? "" : lstAdapterList.get(position).getPOLICYNUMBER());
            holder.textviewPersistencyDueDataHolderId
                    .setText(lstAdapterList.get(position).getHOLDERID() == null ? ""
                            : lstAdapterList.get(position).getHOLDERID());

            String policyCurrentStatus = lstAdapterList.get(position).getPOLICYCURRENTSTATUS() == null ? "" : lstAdapterList.get(
                    position).getPOLICYCURRENTSTATUS();
            holder.textviewPersistencyDueDataPolicyCurrentStatus.setText(policyCurrentStatus);
            holder.textviewPersistencyDueDataPremiumGrossAmount.setText(lstAdapterList.get(position).getPREMIUMGROSSAMOUNT() == null ? "" : lstAdapterList.get(position).getPREMIUMGROSSAMOUNT());

            if (policyCurrentStatus.equalsIgnoreCase("Lapse")) {
                holder.imageviewSMS.setVisibility(View.INVISIBLE);
            } else {
                holder.imageviewSMS.setVisibility(View.VISIBLE);

            }

            holder.textviewPersistencyDueDataDueDate.setText(lstAdapterList.get(position).getDUE_DATE() == null ? "" : lstAdapterList.get(
                    position).getDUE_DATE());

            holder.textviewPersistencyDueDataCollectableAmount.setText(lstAdapterList.get(position).getCOLLECTABLE() == null ? "" : lstAdapterList.get(
                    position).getCOLLECTABLE());
            holder.textviewPersistencyDueDataCollectedAmount.setText(lstAdapterList.get(position).getCOLLECTED() == null ? "" : lstAdapterList.get(
                    position).getCOLLECTED());
            holder.textviewPersistencyDueDataMobileNumber.setText(lstAdapterList.get(position).getCUSTOMERMOBILE() == null ? "" : lstAdapterList.get(position).getCUSTOMERMOBILE());

            String premiumPayFreq = "Premium Payment Frequency: ";
            holder.textviewPersistencyDueDataPremiumPaymentFrequencyTitle.setText(premiumPayFreq);

            String FUPDateString = lstAdapterList.get(position).getPREMIUMFUP() == null ? "" : lstAdapterList.get(position).getPREMIUMFUP();
            String premiumPaymentFrequency = lstAdapterList.get(position).getPREMIUMPAYMENTFREQUENCY() == null ? "" : lstAdapterList.get(position).getPREMIUMPAYMENTFREQUENCY();
            holder.textviewPersistencyDueDataPremiumPaymentFrequency.setText(premiumPaymentFrequency);
            holder.textviewPersistencyDueDataPremiumUp.setText(FUPDateString);


            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                Date FUPDate = dateFormat.parse(FUPDateString);
                Date today = new Date();

                double diff = Math.abs(today.getTime() - FUPDate.getTime());
                String unpaidDues;
                if (premiumPaymentFrequency.equalsIgnoreCase("Yearly")) {
                    double numOfYear = Math.ceil((diff / (1000 * 60 * 60 * 24)) / 365);
                    unpaidDues = (int) numOfYear + "";
                } else if (premiumPaymentFrequency.contains("Quarterly")) {
                    double quarterly = Math.ceil((diff / (1000 * 60 * 60 * 24)) / 90);
                    unpaidDues = (int) quarterly + "";
                } else if (premiumPaymentFrequency.contains("Monthly")) {
                    double monthly = Math.ceil((diff / (1000 * 60 * 60 * 24)) / 30);
                    unpaidDues = (int) monthly + "";
                } else {
                    double halfYear = Math.ceil((diff / (1000 * 60 * 60 * 24)) / 180);
                    unpaidDues = (int) halfYear + "";
                }

                holder.textviewPersistencyDueDataNumberOfUnpaidDues.setText(unpaidDues);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.imgcontact_cust_r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (!TextUtils.isEmpty(holder.textviewPersistencyDueDataMobileNumber.getText().toString())) {
                        commonMethods.callMobileNumber(holder.textviewPersistencyDueDataMobileNumber.getText().toString(), context);
                    }
                }
            });

            holder.textviewPersistencyDueDataMobileNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(holder.textviewPersistencyDueDataMobileNumber.getText().toString())) {
                        commonMethods.callMobileNumber(holder.textviewPersistencyDueDataMobileNumber.getText().toString(), context);
                    }
                }
            });

            holder.imageviewSMS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String mobileNumber = holder.textviewPersistencyDueDataMobileNumber.getText().toString();
                    final String policyNumber = holder.textviewPersistencyDueDataPolicyNumber.getText().toString();

                    final String dueDate = holder.textviewPersistencyDueDataDueDate.getText().toString();
                    final String status = holder.textviewPersistencyDueDataPolicyCurrentStatus.getText().toString();
                    final String amount = holder.textviewPersistencyDueDataPremiumGrossAmount.getText().toString();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                    builder.setTitle("Choose Language");
                    finalPosition = 0;
                    final String[] languagesArray = {"English", "Hindi", "Telugu"};
                    // cow
                    builder.setSingleChoiceItems(languagesArray, finalPosition, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finalPosition = which;
                        }
                    });

                    builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println("which = " + which);
                            System.out.println("checkedItem = " + finalPosition);

                            String language = languagesArray[finalPosition];

                            if (commonMethods.isNetworkConnected(context)) {
                                SendSmsAsync sendSmsAsync = new SendSmsAsync(policyNumber, mobileNumber, dueDate,
                                        status, amount, language);
                                sendSmsAsync.execute();
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

        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,
                MenuItem.OnMenuItemClickListener {

            private final TextView textviewPersistencyDueDataPolicyNumber, textviewPersistencyDueDataHolderId,
                    textviewPersistencyDueDataPolicyCurrentStatus,
                    textviewPersistencyDueDataDueDate, textviewPersistencyDueDataCollectableAmount,
                    textviewPersistencyDueDataCollectedAmount, textviewPersistencyDueDataMobileNumber,
                    textviewPersistencyDueDataCustomerName, textviewPersistencyDueDataPremiumPaymentFrequency,
                    textviewPersistencyDueDataPremiumPaymentFrequencyTitle, textviewPersistencyDueDataPremiumUp,
                    textviewPersistencyDueDataPremiumGrossAmount, textviewPersistencyDueDataNumberOfUnpaidDues;
            private ImageView imageviewSMS, imgcontact_cust_r;

            ViewHolderAdapter(View v) {
                super(v);

                textviewPersistencyDueDataPolicyNumber = v.findViewById(R.id.textviewPersistencyDueDataPolicyNumber);
                textviewPersistencyDueDataHolderId = v.findViewById(R.id.textviewPersistencyDueDataHolderId);
                textviewPersistencyDueDataPolicyCurrentStatus = v.findViewById(R.id.textviewPersistencyDueDataPolicyCurrentStatus);
                textviewPersistencyDueDataDueDate = v.findViewById(R.id.textviewPersistencyDueDataDueDate);
                textviewPersistencyDueDataCollectableAmount = v.findViewById(R.id.textviewPersistencyDueDataCollectableAmount);
                textviewPersistencyDueDataCollectedAmount = v.findViewById(R.id.textviewPersistencyDueDataCollectedAmount);
                textviewPersistencyDueDataMobileNumber = v.findViewById(R.id.textviewPersistencyDueDataMobileNumber);
                textviewPersistencyDueDataCustomerName = v.findViewById(R.id.textviewPersistencyDueDataCustomerName);
                textviewPersistencyDueDataPremiumPaymentFrequency = v.findViewById(R.id.textviewPersistencyDueDataPremiumPaymentFrequency);
                textviewPersistencyDueDataPremiumPaymentFrequencyTitle = v.findViewById(R.id.textviewPersistencyDueDataPremiumPaymentFrequencyTitle);
                textviewPersistencyDueDataPremiumUp = v.findViewById(R.id.textviewPersistencyDueDataPremiumUp);
                textviewPersistencyDueDataPremiumGrossAmount = v.findViewById(R.id.textviewPersistencyDueDataPremiumGrossAmount);
                textviewPersistencyDueDataNumberOfUnpaidDues = v.findViewById(R.id.textviewPersistencyDueDataNumberOfUnpaidDues);
                imgcontact_cust_r = v.findViewById(R.id.imgcontact_cust_r);
                imageviewSMS = v.findViewById(R.id.imageviewSMS);

                v.setOnCreateContextMenuListener(this);
            }


            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {

                menu.setHeaderTitle("Select Action");
                String status = globleList.get(getAdapterPosition()).getPOLICYCURRENTSTATUS();

                if (status.equalsIgnoreCase("Lapse") || status.equalsIgnoreCase("Technical Lapse")) {
                    MenuItem revivalQuotation = menu.add(Menu.NONE, 1, 1, "Revival Quotation");
                    revivalQuotation.setOnMenuItemClickListener(this);
                }
            }

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case 1:
                        String strPolicyNo = globleList.get(getAdapterPosition()).getPOLICYNUMBER();
                        String mobileNumber = globleList.get(getAdapterPosition()).getCUSTOMERMOBILE();
                        String emailId = "";

                        if (!TextUtils.isEmpty(strPolicyNo)) {
                            Intent intent = new Intent(context, RevivalQuotationActivity.class);
                            intent.putExtra("policyNumber", strPolicyNo);
                            intent.putExtra("mobileNumber", mobileNumber);
                            intent.putExtra("emailId", emailId);
                            startActivity(intent);
                        }
                        break;
                }
                return true;
            }
        }

    }

    private void clearList() {
        if (globleList != null && selectedAdapter != null) {
            globleList.clear();
            selectedAdapter = new SelectedAdapter(globleList);
            recyclerview.setAdapter(selectedAdapter);
            recyclerview.invalidate();
        }
    }


    class SendSmsAsync extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String response = "";

        private final String policyNumber;
        private final String mobileNumber;
        private final String dueDate;
        private final String status;
        private final String amount;
        private final String language;

        SendSmsAsync(String policyNumber, String mobileNumber, String dueDate, String status, String amount, String language) {
            this.policyNumber = policyNumber;
            this.mobileNumber = mobileNumber;
            this.dueDate = dueDate;
            this.status = status;
            this.amount = amount;
            this.language = language;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                String METHOD_NAME_SEND_SMS = "SendRenewalDueSMS_SMRT";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SEND_SMS);
                request.addProperty("strPolicyNo", policyNumber);
                request.addProperty("strMobileNo", mobileNumber);
                request.addProperty("strDueDate", dueDate);
                request.addProperty("strStatus", status);
                request.addProperty("strAmt", amount);
                request.addProperty("strLanguage", language);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME_SEND_SMS;
                androidHttpTranport.call(SOAP_ACTION, envelope);

                SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();
                response = sa.toString();
                if (response.equalsIgnoreCase("1")) {
                    response = "success";
                } else {
                    response = "";
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

            if (running) {
                if (response.equalsIgnoreCase("success")) {
                    commonMethods.showMessageDialog(context, "Message sent successfully");
                } else {
                    commonMethods.showMessageDialog(context, "Message sending failed");
                }
            } else {
                commonMethods.showMessageDialog(context, "Message sending failed");
            }
        }
    }

    private void service_hits() {
        String strCIFBDMUserId = "", strCIFBDMEmailId = "",
                strCIFBDMPassword = "", strCIFBDMMObileNo = "";
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods
                .setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

        service = new ServiceHits(context,
                METHOD_NAME, "",
                strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();
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

        if (downloadPersistencyDueDataAsync != null) {
            downloadPersistencyDueDataAsync.cancel(true);
        }
        if (service != null) {
            service.cancel(true);
        }
    }


    public class IAChannelPersistencyDueDataValuesModel {
        /*<POLICYNUMBER>1W372630804</POLICYNUMBER>
        <HOLDERID>694525999</HOLDERID>
        <POLICYCURRENTSTATUS>Technical Lapse</POLICYCURRENTSTATUS>
        <DUE_DATE>24-JUN-2020</DUE_DATE>
        <COLLECTABLE>34418.28</COLLECTABLE>
        <COLLECTED>34418.28</COLLECTED>
        <CUSTOMERMOBILE>6568170562</CUSTOMERMOBILE>
        <CUSTOMERNAME>Pankaj Gupta</CUSTOMERNAME>
        <PREMIUMPAYMENTFREQUENCY>Monthly</PREMIUMPAYMENTFREQUENCY>
        <PREMIUMFUP>24-JUN-2020</PREMIUMFUP>
        <PREMIUMGROSSAMOUNT>2932.73</PREMIUMGROSSAMOUNT>*/

        private String POLICYNUMBER, HOLDERID, POLICYCURRENTSTATUS, DUE_DATE, COLLECTABLE,
                COLLECTED, CUSTOMERMOBILE, CUSTOMERNAME, PREMIUMPAYMENTFREQUENCY,
                PREMIUMFUP, PREMIUMGROSSAMOUNT, PAID_STATUS;

        public IAChannelPersistencyDueDataValuesModel(String POLICYNUMBER, String HOLDERID, String POLICYCURRENTSTATUS, String DUE_DATE, String COLLECTABLE, String COLLECTED, String CUSTOMERMOBILE, String CUSTOMERNAME, String PREMIUMPAYMENTFREQUENCY, String PREMIUMFUP, String PREMIUMGROSSAMOUNT, String PAID_STATUS) {
            this.POLICYNUMBER = POLICYNUMBER;
            this.HOLDERID = HOLDERID;
            this.POLICYCURRENTSTATUS = POLICYCURRENTSTATUS;
            this.DUE_DATE = DUE_DATE;
            this.COLLECTABLE = COLLECTABLE;
            this.COLLECTED = COLLECTED;
            this.CUSTOMERMOBILE = CUSTOMERMOBILE;
            this.CUSTOMERNAME = CUSTOMERNAME;
            this.PREMIUMPAYMENTFREQUENCY = PREMIUMPAYMENTFREQUENCY;
            this.PREMIUMFUP = PREMIUMFUP;
            this.PREMIUMGROSSAMOUNT = PREMIUMGROSSAMOUNT;
            this.PAID_STATUS = PAID_STATUS;
        }

        public String getPOLICYNUMBER() {
            return POLICYNUMBER;
        }

        public String getHOLDERID() {
            return HOLDERID;
        }

        public String getPOLICYCURRENTSTATUS() {
            return POLICYCURRENTSTATUS;
        }

        public String getDUE_DATE() {
            return DUE_DATE;
        }

        public String getCOLLECTABLE() {
            return COLLECTABLE;
        }

        public String getCOLLECTED() {
            return COLLECTED;
        }

        public String getCUSTOMERMOBILE() {
            return CUSTOMERMOBILE;
        }

        public String getCUSTOMERNAME() {
            return CUSTOMERNAME;
        }

        public String getPREMIUMPAYMENTFREQUENCY() {
            return PREMIUMPAYMENTFREQUENCY;
        }

        public String getPREMIUMFUP() {
            return PREMIUMFUP;
        }

        public String getPREMIUMGROSSAMOUNT() {
            return PREMIUMGROSSAMOUNT;
        }

        public String getPAID_STATUS() {
            return PAID_STATUS;
        }
    }

    public List<IAChannelPersistencyDueDataValuesModel> parseNodeIAChannelPersistencyDueData(List<String> lsNode) {
        List<IAChannelPersistencyDueDataValuesModel> lsData = new ArrayList<>();

        String POLICYNUMBER, HOLDERID, POLICYCURRENTSTATUS, DUE_DATE, COLLECTABLE,
                COLLECTED, CUSTOMERMOBILE, CUSTOMERNAME, PREMIUMPAYMENTFREQUENCY,
                PREMIUMFUP, PREMIUMGROSSAMOUNT, PAID_STATUS;

        ParseXML parseXML = new ParseXML();
        for (String Node : lsNode) {

            POLICYNUMBER = parseXML.parseXmlTag(Node, "POLICYNUMBER");
            HOLDERID = parseXML.parseXmlTag(Node, "HOLDERID");
            POLICYCURRENTSTATUS = parseXML.parseXmlTag(Node, "POLICYCURRENTSTATUS");
            DUE_DATE = parseXML.parseXmlTag(Node, "DUE_DATE");
            COLLECTABLE = parseXML.parseXmlTag(Node, "COLLECTABLE");
            COLLECTED = parseXML.parseXmlTag(Node, "COLLECTED");
            CUSTOMERMOBILE = parseXML.parseXmlTag(Node, "CUSTOMERMOBILE");
            CUSTOMERNAME = parseXML.parseXmlTag(Node, "CUSTOMERNAME");
            PREMIUMPAYMENTFREQUENCY = parseXML.parseXmlTag(Node, "PREMIUMPAYMENTFREQUENCY");
            PREMIUMFUP = parseXML.parseXmlTag(Node, "PREMIUMFUP");
            PREMIUMGROSSAMOUNT = parseXML.parseXmlTag(Node, "PREMIUMGROSSAMOUNT");
            PAID_STATUS = parseXML.parseXmlTag(Node, "PAID_STATUS");

            IAChannelPersistencyDueDataValuesModel nodeVal = new IAChannelPersistencyDueDataValuesModel(POLICYNUMBER, HOLDERID, POLICYCURRENTSTATUS, DUE_DATE, COLLECTABLE, COLLECTED, CUSTOMERMOBILE, CUSTOMERNAME, PREMIUMPAYMENTFREQUENCY, PREMIUMFUP, PREMIUMGROSSAMOUNT, PAID_STATUS);
            lsData.add(nodeVal);
        }
        return lsData;
    }
}
