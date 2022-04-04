package sbilife.com.pointofsale_bancaagency.home.rinnrakshareports.rinnrakshanb;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;
import sbilife.com.pointofsale_bancaagency.utility.AllDocumentsUploadActivity;

public class RinnRakshaProposalSearchActivity extends AppCompatActivity implements View.OnClickListener, ServiceHits.DownLoadData {
    private final String METHOD_NAME = "getRinnProposalSearch_smrt";
    private ProgressDialog mProgressDialog;
    private Context context;
    private CommonMethods commonMethods;
    private EditText edittextSearchAPI, edittextSearchFilter;
    private TextView textviewRecordCount, tvCashieringDate;
    private RecyclerView recyclerview;
    private ServiceHits service;
    private ArrayList<ProposalSearchValuesModel> globalDataList;
    private ProposalSearchAsync proposalSearchAsync;
    private SelectedAdapter selectedAdapter;
    private String strCIFBDMUserId = "", strCIFBDMEmailId = "",
            strCIFBDMMObileNo = "";
    private HorizontalScrollView horizontalviewTabId;
    private int mYear, mMonth, mDay, datecheck = 0;
    private LinearLayout llSearchFilter;
    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {

            mYear = year;
            mMonth = month;
            mDay = day;
            updateDisplay(mYear, mMonth, mDay);
        }
    };
    private String cashieringDate, searchText, searchTabFlag;
    private Button buttonUnderwritingStatus, buttonPIWC, buttonRealization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_rinn_raksha_proposal_search);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Proposal Search");

        edittextSearchAPI = findViewById(R.id.edittextSearchAPI);
        edittextSearchFilter = findViewById(R.id.edittextSearchFilter);
        llSearchFilter = findViewById(R.id.llSearchFilter);
        llSearchFilter.setVisibility(View.GONE);

        textviewRecordCount = findViewById(R.id.textviewRecordCount);
        tvCashieringDate = findViewById(R.id.tvCashieringDate);
        tvCashieringDate.setOnClickListener(this);
        horizontalviewTabId = findViewById(R.id.horizontalviewTabId);
        Button buttonOk, buttonReset, buttonFilter;
        buttonUnderwritingStatus = findViewById(R.id.buttonUnderwritingStatus);
        buttonPIWC = findViewById(R.id.buttonPIWC);
        buttonRealization = findViewById(R.id.buttonRealization);
        buttonOk = findViewById(R.id.buttonOk);
        buttonReset = findViewById(R.id.buttonReset);
        buttonFilter = findViewById(R.id.buttonFilter);

        buttonUnderwritingStatus.setOnClickListener(this);
        buttonPIWC.setOnClickListener(this);
        buttonRealization.setOnClickListener(this);
        buttonOk.setOnClickListener(this);
        buttonReset.setOnClickListener(this);
        buttonFilter.setOnClickListener(this);

        recyclerview = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(linearLayoutManager);
        globalDataList = new ArrayList<>();
        selectedAdapter = new SelectedAdapter(globalDataList);
        recyclerview.setAdapter(selectedAdapter);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");

        if (fromHome != null && fromHome.equalsIgnoreCase("Y")) {
            getUserDetails();
        } else {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");
        }


        mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        mProgressDialog.setButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (proposalSearchAsync != null) {
                            proposalSearchAsync.cancel(true);
                        }

                        if (mProgressDialog != null) {
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }
                    }
                });

        mProgressDialog.setMax(100);
        edittextSearchFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String charString = s.toString();
                if (charString.isEmpty()) {
                    selectedAdapter.getFilter().filter(s);
                }
                //selectedAdapter.getFilter().filter(s);
                System.out.println("s = " + s + ", start = " + start + ", before = " + before + ", count = " + count);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

    }

    private void service_hits(String input) {
        service = new ServiceHits(context, METHOD_NAME, input,
                strCIFBDMUserId, strCIFBDMEmailId,
                strCIFBDMMObileNo, commonMethods.getStrAuth(), this);
        service.execute();
    }

    @Override
    public void downLoadData() {
        proposalSearchAsync = new ProposalSearchAsync();
        proposalSearchAsync.execute();
    }

    void loadRecyclerView() {
        horizontalviewTabId.setVisibility(View.VISIBLE);
        llSearchFilter.setVisibility(View.VISIBLE);

        selectedAdapter = new SelectedAdapter(globalDataList);
        recyclerview.setAdapter(selectedAdapter);
        recyclerview.invalidate();
        String count = "Total Count : " + globalDataList.size();
        textviewRecordCount.setText(count);
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

        if (proposalSearchAsync != null) {
            proposalSearchAsync.cancel(true);
        }

        if (service != null) {
            service.cancel(true);
        }
    }

    private void clearList() {
        horizontalviewTabId.setVisibility(View.GONE);

        llSearchFilter.setVisibility(View.GONE);
        edittextSearchFilter.setText("");

        textviewRecordCount.setVisibility(View.GONE);
        textviewRecordCount.setText("");

        if (globalDataList != null && selectedAdapter != null) {
            globalDataList.clear();
            selectedAdapter = new SelectedAdapter(globalDataList);
            recyclerview.setAdapter(selectedAdapter);
            recyclerview.invalidate();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonOk:
                commonMethods.hideKeyboard(edittextSearchAPI, context);
                if (commonMethods.isNetworkConnected(context)) {
                    cashieringDate = tvCashieringDate.getText().toString();
                    searchText = edittextSearchAPI.getText().toString();
                    if (TextUtils.isEmpty(cashieringDate) && TextUtils.isEmpty(searchText)) {
                        commonMethods.showMessageDialog(context, "Please enter LAN, Form number, Suraksha account number or Select Cashiering Date.");
                        return;
                    }
                    clearList();
                    StringBuffer input = new StringBuffer();
                    if (!TextUtils.isEmpty(cashieringDate)) {
                        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");
                        SimpleDateFormat finalDateFormat = new SimpleDateFormat("MM/dd/yyyy");

                        Date d1 = null, d2 = null;
                        try {
                            d1 = formatter.parse(cashieringDate);
                            cashieringDate = finalDateFormat.format(d1);
                            cashieringDate = cashieringDate.toUpperCase();
                            input.append(cashieringDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    } else {
                        cashieringDate = "";
                    }
                    if (!TextUtils.isEmpty(searchText)) {
                        input.append(",").append(searchText);
                    } else {
                        searchText = "";
                    }

                    service_hits(input.toString());
                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }

                break;
            case R.id.buttonUnderwritingStatus:
                buttonUnderwritingStatus.setBackgroundColor(Color.parseColor("#00a1e3"));
                buttonPIWC.setBackgroundColor(Color.parseColor("#A9A9A9"));
                buttonRealization.setBackgroundColor(Color.parseColor("#A9A9A9"));
                searchTabFlag = "UnderwritingStatus";
                loadRecyclerView();
                break;
            case R.id.buttonPIWC:
                buttonPIWC.setBackgroundColor(Color.parseColor("#00a1e3"));
                buttonUnderwritingStatus.setBackgroundColor(Color.parseColor("#A9A9A9"));
                buttonRealization.setBackgroundColor(Color.parseColor("#A9A9A9"));
                searchTabFlag = "PIWC";
                loadRecyclerView();
                break;
            case R.id.buttonRealization:
                buttonRealization.setBackgroundColor(Color.parseColor("#00a1e3"));
                buttonUnderwritingStatus.setBackgroundColor(Color.parseColor("#A9A9A9"));
                buttonPIWC.setBackgroundColor(Color.parseColor("#A9A9A9"));
                searchTabFlag = "Realization";
                loadRecyclerView();
                break;
            case R.id.tvCashieringDate:
                datecheck = 1;
                commonMethods.showDateDialogCommon(mDateSetListener, context, mYear,
                        mMonth, mDay);
                break;
            case R.id.buttonReset:
                edittextSearchAPI.setText("");
                tvCashieringDate.setText("");
                searchText = "";
                cashieringDate = "";
                break;

            case R.id.buttonFilter:
                String filterKeyword = edittextSearchFilter.getText().toString();
                selectedAdapter.getFilter().filter(filterKeyword);
                break;
        }
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
            tvCashieringDate.setText(totaldate);
        }
    }

    ArrayList<ProposalSearchValuesModel> parseNodeProposalSearch(List<String> lsNode) {
        ArrayList<ProposalSearchValuesModel> lsData = new ArrayList<>();
        ParseXML parseXML = new ParseXML();
        String FORMNUMBER, STATUS, REQUIREMENT, OPENREQUIREMENTS, CALLFLAG,
                PIWCREQUIREMENTFLAG, REMARKS, CASHERINGDATE, PAYMENTTYPE,
                CHEQUEREALISEDDATE, LOANACCOUNTNUMBER, LOANPLUSACCOUNTNUMBER, LOANHOLDERNAME,
                LOANCATEG;
        for (String Node : lsNode) {

            FORMNUMBER = parseXML.parseXmlTag(Node, "FORMNUMBER");
            FORMNUMBER = FORMNUMBER == null ? "" : FORMNUMBER;

            STATUS = parseXML.parseXmlTag(Node, "STATUS");
            STATUS = STATUS == null ? "" : STATUS;

            REQUIREMENT = parseXML.parseXmlTag(Node, "REQUIREMENT");
            REQUIREMENT = REQUIREMENT == null ? "" : REQUIREMENT;

            OPENREQUIREMENTS = parseXML.parseXmlTag(Node, "OPENREQUIREMENTS");
            OPENREQUIREMENTS = OPENREQUIREMENTS == null ? "" : OPENREQUIREMENTS;

            CALLFLAG = parseXML.parseXmlTag(Node, "CALLFLAG");
            CALLFLAG = CALLFLAG == null ? "" : CALLFLAG;

            PIWCREQUIREMENTFLAG = parseXML.parseXmlTag(Node, "PIWCREQUIREMENTFLAG");
            PIWCREQUIREMENTFLAG = PIWCREQUIREMENTFLAG == null ? "" : PIWCREQUIREMENTFLAG;

            REMARKS = parseXML.parseXmlTag(Node, "REMARKS");
            REMARKS = REMARKS == null ? "" : REMARKS;

            CASHERINGDATE = parseXML.parseXmlTag(Node, "CASHERINGDATE");
            CASHERINGDATE = CASHERINGDATE == null ? "" : CASHERINGDATE;

            PAYMENTTYPE = parseXML.parseXmlTag(Node, "PAYMENTTYPE");
            PAYMENTTYPE = PAYMENTTYPE == null ? "" : PAYMENTTYPE;

            CHEQUEREALISEDDATE = parseXML.parseXmlTag(Node, "CHEQUEREALISEDDATE");
            CHEQUEREALISEDDATE = CHEQUEREALISEDDATE == null ? "" : CHEQUEREALISEDDATE;

            LOANACCOUNTNUMBER = parseXML.parseXmlTag(Node, "LOANACCOUNTNUMBER");
            LOANACCOUNTNUMBER = LOANACCOUNTNUMBER == null ? "" : LOANACCOUNTNUMBER;

            LOANPLUSACCOUNTNUMBER = parseXML.parseXmlTag(Node, "LOANPLUSACCOUNTNUMBER");
            LOANPLUSACCOUNTNUMBER = LOANPLUSACCOUNTNUMBER == null ? "" : LOANPLUSACCOUNTNUMBER;

            LOANHOLDERNAME = parseXML.parseXmlTag(Node, "LOANHOLDERNAME");
            LOANHOLDERNAME = LOANHOLDERNAME == null ? "" : LOANHOLDERNAME;

            LOANCATEG = parseXML.parseXmlTag(Node, "LOANCATEG");
            LOANCATEG = LOANCATEG == null ? "" : LOANCATEG;

            ProposalSearchValuesModel nodeVal = new ProposalSearchValuesModel(FORMNUMBER, STATUS, REQUIREMENT, OPENREQUIREMENTS, CALLFLAG, PIWCREQUIREMENTFLAG, REMARKS, CASHERINGDATE, PAYMENTTYPE, CHEQUEREALISEDDATE, LOANACCOUNTNUMBER, LOANPLUSACCOUNTNUMBER, LOANHOLDERNAME, LOANCATEG);
            lsData.add(nodeVal);
        }
        return lsData;
    }

    class ProposalSearchAsync extends AsyncTask<String, String, String> {

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

                //getRinnOutstanding_smrt(string strCode, string strEmailId, string strMobileNo, string strAuthKey)
                //getRinnProposalSearch_smrt(string strType,string date, string strEmailId, string strMobileNo, string strAuthKey)
                running = true;
                SoapObject request;
                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("strType", searchText);
                request.addProperty("date", cashieringDate);
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
                            inputpolicylist, "NewDataSet");
                    inputpolicylist = prsObj.parseXmlTag(
                            inputpolicylist, "ScreenData");
                    error = inputpolicylist;

                    if (error == null) {
                        // for agent policy list

                        inputpolicylist = sa.toString();
                        inputpolicylist = prsObj.parseXmlTag(
                                inputpolicylist, "NewDataSet");

                        List<String> Node = prsObj.parseParentNode(
                                inputpolicylist, "Table");

                        List<ProposalSearchValuesModel> nodeData = parseNodeProposalSearch(Node);
                        globalDataList.clear();
                        globalDataList.addAll(nodeData);
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
                if (error == null) {
                    buttonUnderwritingStatus.setBackgroundColor(Color.parseColor("#00a1e3"));
                    buttonPIWC.setBackgroundColor(Color.parseColor("#A9A9A9"));
                    buttonRealization.setBackgroundColor(Color.parseColor("#A9A9A9"));
                    searchTabFlag = "UnderwritingStatus";
                    loadRecyclerView();
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

        private ArrayList<ProposalSearchValuesModel> lstAdapterList;

        SelectedAdapter(ArrayList<ProposalSearchValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        lstAdapterList = globalDataList;
                    } else {
                        final ArrayList<ProposalSearchValuesModel> results = new ArrayList<>();
                        for (final ProposalSearchValuesModel model : globalDataList) {
                            if (model.getFORMNUMBER().toLowerCase().contains(charSequence.toString().toLowerCase())
                                    || model.getLOANACCOUNTNUMBER().toLowerCase().contains(charSequence.toString().toLowerCase())
                                    || model.getLOANPLUSACCOUNTNUMBER().toLowerCase().contains(charSequence.toString().toLowerCase())
                                    || model.getLOANCATEG().toLowerCase().contains(charSequence.toString().toLowerCase())
                                    || model.getLOANHOLDERNAME().toLowerCase().contains(charSequence.toString().toLowerCase())

                            ) {
                                results.add(model);
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

                    lstAdapterList = (ArrayList<ProposalSearchValuesModel>) results.values;
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
                    R.layout.list_item_rinn_raksha_proposal_search, parent, false);

            return new SelectedAdapter.ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final SelectedAdapter.ViewHolderAdapter holder, final int position) {

            holder.textviewFormNumber.setText(lstAdapterList.get(position).getFORMNUMBER());
            holder.textviewLoanAccountNumber.setText(lstAdapterList.get(position).getLOANACCOUNTNUMBER());
            holder.tvLoanPlusAccountNumber.setText(lstAdapterList.get(position).getLOANPLUSACCOUNTNUMBER());
            holder.tvLoanHolderName.setText(lstAdapterList.get(position).getLOANHOLDERNAME());
            holder.tvLoanCategory.setText(lstAdapterList.get(position).getLOANCATEG());

            holder.textviewStatus.setText(lstAdapterList.get(position).getSTATUS());
            holder.textRequirement.setText(lstAdapterList.get(position).getREQUIREMENT());
            String openReq = lstAdapterList.get(position).getOPENREQUIREMENTS();
            holder.textOpenRequirements.setText(openReq);
            if (TextUtils.isEmpty(openReq)) {
                holder.imageUploadOpenReqDoc.setVisibility(View.INVISIBLE);
            } else {
                holder.imageUploadOpenReqDoc.setVisibility(View.VISIBLE);
            }

            holder.textviewCallFlag.setText(lstAdapterList.get(position).getCALLFLAG());
            holder.textPIWCRequirementFlag.setText(lstAdapterList.get(position).getPIWCREQUIREMENTFLAG());
            holder.textRemarks.setText(lstAdapterList.get(position).getREMARKS());

            holder.textCashieringDate.setText(lstAdapterList.get(position).getCHEQUEREALISEDDATE());
            holder.textPaymentType.setText(lstAdapterList.get(position).getPAYMENTTYPE());
            holder.textChequeRealisedDate.setText(lstAdapterList.get(position).getCHEQUEREALISEDDATE());


            holder.imageUploadOpenReqDoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), AllDocumentsUploadActivity.class);
                    intent.putExtra("PROPOSAL_NO", lstAdapterList.get(position).getFORMNUMBER());
                    startActivity(intent);
                }
            });

        }


        public class ViewHolderAdapter extends RecyclerView.ViewHolder {

            private final TextView textviewFormNumber;
            private final TextView textviewLoanAccountNumber;
            private final TextView tvLoanPlusAccountNumber;
            private final TextView tvLoanHolderName;
            private final TextView tvLoanCategory;
            private final TextView textviewStatus;
            private final TextView textRequirement;
            private final TextView textOpenRequirements;
            private final ImageView imageUploadOpenReqDoc;

            private final TextView textviewCallFlag;
            private final TextView textPIWCRequirementFlag;
            private final TextView textRemarks;

            private final TextView textCashieringDate;
            private final TextView textPaymentType;
            private final TextView textChequeRealisedDate;

            ViewHolderAdapter(View v) {
                super(v);
                textviewFormNumber = v.findViewById(R.id.textviewFormNumber);
                textviewLoanAccountNumber = v.findViewById(R.id.textviewLoanAccountNumber);
                tvLoanPlusAccountNumber = v.findViewById(R.id.tvLoanPlusAccountNumber);
                tvLoanHolderName = v.findViewById(R.id.tvLoanHolderName);
                tvLoanCategory = v.findViewById(R.id.tvLoanCategory);

                LinearLayout llUnderwritingStatus = v.findViewById(R.id.llUnderwritingStatus);
                textviewStatus = v.findViewById(R.id.textviewStatus);
                textRequirement = v.findViewById(R.id.textRequirement);
                textOpenRequirements = v.findViewById(R.id.textOpenRequirements);
                imageUploadOpenReqDoc = v.findViewById(R.id.imageUploadOpenReqDoc);

                LinearLayout llPIWC = v.findViewById(R.id.llPIWC);
                textviewCallFlag = v.findViewById(R.id.textviewCallFlag);
                textPIWCRequirementFlag = v.findViewById(R.id.textPIWCRequirementFlag);
                textRemarks = v.findViewById(R.id.textRemarks);

                LinearLayout llRealization = v.findViewById(R.id.llRealization);
                textCashieringDate = v.findViewById(R.id.textCashieringDate);
                textPaymentType = v.findViewById(R.id.textPaymentType);
                textChequeRealisedDate = v.findViewById(R.id.textChequeRealisedDate);

                if (searchTabFlag.equalsIgnoreCase("UnderwritingStatus")) {
                    llUnderwritingStatus.setVisibility(View.VISIBLE);
                    llPIWC.setVisibility(View.GONE);
                    llRealization.setVisibility(View.GONE);
                } else if (searchTabFlag.equalsIgnoreCase("PIWC")) {
                    llUnderwritingStatus.setVisibility(View.GONE);
                    llPIWC.setVisibility(View.VISIBLE);
                    llRealization.setVisibility(View.GONE);
                } else if (searchTabFlag.equalsIgnoreCase("Realization")) {
                    llUnderwritingStatus.setVisibility(View.GONE);
                    llPIWC.setVisibility(View.GONE);
                    llRealization.setVisibility(View.VISIBLE);
                }

            }


        }

    }

    class ProposalSearchValuesModel {
        private final String FORMNUMBER;
        private final String STATUS;
        private final String REQUIREMENT;
        private final String OPENREQUIREMENTS;
        private final String CALLFLAG;
        private final String PIWCREQUIREMENTFLAG;
        private final String REMARKS;
        private final String CASHERINGDATE;
        private final String PAYMENTTYPE;
        private final String CHEQUEREALISEDDATE;
        private final String LOANACCOUNTNUMBER;
        private final String LOANPLUSACCOUNTNUMBER;
        private final String LOANHOLDERNAME;
        private final String LOANCATEG;

        public ProposalSearchValuesModel(String FORMNUMBER, String STATUS, String REQUIREMENT, String OPENREQUIREMENTS, String CALLFLAG, String PIWCREQUIREMENTFLAG, String REMARKS, String CASHERINGDATE, String PAYMENTTYPE, String CHEQUEREALISEDDATE, String LOANACCOUNTNUMBER, String LOANPLUSACCOUNTNUMBER, String LOANHOLDERNAME, String LOANCATEG) {
            this.FORMNUMBER = FORMNUMBER;
            this.STATUS = STATUS;
            this.REQUIREMENT = REQUIREMENT;
            this.OPENREQUIREMENTS = OPENREQUIREMENTS;
            this.CALLFLAG = CALLFLAG;
            this.PIWCREQUIREMENTFLAG = PIWCREQUIREMENTFLAG;
            this.REMARKS = REMARKS;
            this.CASHERINGDATE = CASHERINGDATE;
            this.PAYMENTTYPE = PAYMENTTYPE;
            this.CHEQUEREALISEDDATE = CHEQUEREALISEDDATE;
            this.LOANACCOUNTNUMBER = LOANACCOUNTNUMBER;
            this.LOANPLUSACCOUNTNUMBER = LOANPLUSACCOUNTNUMBER;
            this.LOANHOLDERNAME = LOANHOLDERNAME;
            this.LOANCATEG = LOANCATEG;
        }

        public String getFORMNUMBER() {
            return FORMNUMBER;
        }

        public String getSTATUS() {
            return STATUS;
        }

        public String getREQUIREMENT() {
            return REQUIREMENT;
        }

        public String getOPENREQUIREMENTS() {
            return OPENREQUIREMENTS;
        }

        public String getCALLFLAG() {
            return CALLFLAG;
        }

        public String getPIWCREQUIREMENTFLAG() {
            return PIWCREQUIREMENTFLAG;
        }

        public String getREMARKS() {
            return REMARKS;
        }

        public String getCASHERINGDATE() {
            return CASHERINGDATE;
        }

        public String getPAYMENTTYPE() {
            return PAYMENTTYPE;
        }

        public String getCHEQUEREALISEDDATE() {
            return CHEQUEREALISEDDATE;
        }

        public String getLOANACCOUNTNUMBER() {
            return LOANACCOUNTNUMBER;
        }

        public String getLOANPLUSACCOUNTNUMBER() {
            return LOANPLUSACCOUNTNUMBER;
        }

        public String getLOANHOLDERNAME() {
            return LOANHOLDERNAME;
        }

        public String getLOANCATEG() {
            return LOANCATEG;
        }
    }

}