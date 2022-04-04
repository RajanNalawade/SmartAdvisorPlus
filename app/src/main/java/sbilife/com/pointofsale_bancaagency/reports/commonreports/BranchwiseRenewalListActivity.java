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
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.reports.BancaReportsRenewalActivity;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class BranchwiseRenewalListActivity extends AppCompatActivity implements View.OnClickListener,
        ServiceHits.DownLoadData,UpdateAltMobileNoCommonAsyncTask.UpdateAltMobNoInterface  {
    private final String URl = ServiceURL.SERVICE_URL;
    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME_BRANCH_CODE = "getBranchListbanca_smrt";
    private final String METHOD_NAME_BRANCHWISE_RENEWAL_LIST = "getbancaRenewalListBranchwise";

    private Spinner spinnerBranchCodes, spnRewmonths;
    private TextView textviewRecordCount;
    private EditText edittextSearch;
    private RecyclerView recyclerview;

    private CommonMethods commonMethods;
    private Context context;

    private int mYear, mMonth, mDay, datecheck = 0;

    private ArrayList<BDMBranchwiseRenewalListValuesModel> globalDataList;
    private SelectedAdapter selectedAdapter;

    private ProgressDialog mProgressDialog;
    private LinearLayout llBranchCodeList;

    private ServiceHits service;
    private DownloadBranchCodeAsyncTask downloadBranchCodeAsyncTask;
    private DownloadBranchWiseRenewalListAsync downloadBranchWiseRenewalListAsync;
    private ArrayList<ParseXML.BranchDetailsForBDMValuesModel> branchDetailsList;
    private String branchName = "Select Branch Name", branchCode = "0", strCIFBDMUserId = "", strCIFBDMEmailId = "",
            strCIFBDMPassword = "", strCIFBDMMObileNo = "";
    private ArrayList<String> bankTypeList;
    private LinearLayout llBankTYpe;
    private Spinner spinnerBankType;
    private String filterFlag = "";
    private FundValueAsyncTask fundValueAsyncTask;
    private GetPremiumAmountCommonAsync getPremiumAmountCommonAsync;
    private SendRenewalSMSAsynTask sendRenewalSMSAsynTask;
    private int finalPosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_branchwise_renewal_list);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Branchwise Renewal List");

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
        spnRewmonths = findViewById(R.id.spnRewmonths);

        Button buttonOk = findViewById(R.id.buttonOk);
        edittextSearch = findViewById(R.id.edittextSearch);
        recyclerview = findViewById(R.id.recyclerview);
        llBranchCodeList = findViewById(R.id.llBranchCodeList);

        llBankTYpe = findViewById(R.id.llBankTYpe);
        llBankTYpe.setVisibility(View.GONE);
        spinnerBankType = findViewById(R.id.spinnerBankType);

        spinnerBankType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    filterFlag = "bankType";
                    CharSequence key = spinnerBankType.getSelectedItem().toString();
                    selectedAdapter.getFilter().filter(key);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        // set LayoutManager to RecyclerView
        recyclerview.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        globalDataList = new ArrayList<>();
        selectedAdapter = new SelectedAdapter(globalDataList);
        recyclerview.setAdapter(selectedAdapter);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        buttonOk.setOnClickListener(this);

        edittextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterFlag = "et";
                spinnerBankType.setSelection(0);
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
                        if (downloadBranchWiseRenewalListAsync != null) {
                            downloadBranchWiseRenewalListAsync.cancel(true);
                        }

                        if (fundValueAsyncTask != null) {
                            fundValueAsyncTask.cancel(true);
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
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.buttonOk:
                commonMethods.hideKeyboard(edittextSearch, context);

                clearList();
                if (commonMethods.isNetworkConnected(context)) {
                    if (branchCode.equalsIgnoreCase("0")) {
                        commonMethods.showMessageDialog(context, "Please Select Branch Name");
                    } else {
                        StringBuilder input = new StringBuilder();
                        input.append(branchName + "," + branchCode);

                        input.append(",").append(spnRewmonths.getSelectedItem().toString());
                        service_hits(input.toString());
                    }
                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }
                break;
        }
    }

    private void service_hits(String input) {

        service = new ServiceHits(context, METHOD_NAME_BRANCHWISE_RENEWAL_LIST, input,
                strCIFBDMUserId, strCIFBDMEmailId,
                strCIFBDMMObileNo, strCIFBDMPassword, this);
        service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void downLoadData() {
        downloadBranchWiseRenewalListAsync = new DownloadBranchWiseRenewalListAsync();
        downloadBranchWiseRenewalListAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
                commonMethods.appendSecurityParams(context, request, strCIFBDMEmailId, strCIFBDMMObileNo);

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
                System.out.println("request:" + inputpolicylist);
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
                e.printStackTrace();
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

    class DownloadBranchWiseRenewalListAsync extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String error = "", fromDate = "", toDate = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();

            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

            String spnRewmonths_text = spnRewmonths.getSelectedItem().toString();

            SimpleDateFormat format1 = new SimpleDateFormat("MM-dd-yyyy");

            if (spnRewmonths_text.contentEquals("Previous Month")) {
                Calendar aCalendar = Calendar.getInstance();
                aCalendar.set(Calendar.DATE, 1);
                aCalendar.add(Calendar.DAY_OF_MONTH, -1);
                Date lastDateOfPreviousMonth = aCalendar.getTime();
                toDate = format1.format(lastDateOfPreviousMonth);

                aCalendar.set(Calendar.DATE, 1);
                aCalendar.add(Calendar.MONTH, -5);
                Date firstDateOfPreviousMonth = aCalendar.getTime();
                fromDate = format1.format(firstDateOfPreviousMonth);
            } else if (spnRewmonths_text.contentEquals("Current Month")) {
                Calendar cale = Calendar.getInstance();
                cale.set(Calendar.DATE,
                        cale.getActualMaximum(Calendar.DATE));
                Date lastDateOfCurrentMonth = cale.getTime();
                toDate = format1.format(lastDateOfCurrentMonth);
                cale.set(Calendar.DATE,
                        cale.getActualMinimum(Calendar.DATE));
                Date firstDateOfCurrentMonth = cale.getTime();
                fromDate = format1.format(firstDateOfCurrentMonth);
            } else if (spnRewmonths_text.contentEquals("Next Month")) {
                Calendar calen = Calendar.getInstance();
                // calen.set(Calendar.MONTH, calen.get(Calendar.MONTH));
                calen.add(Calendar.MONTH, +1);
                calen.set(Calendar.DATE, calen.getActualMaximum(Calendar.DATE));
                Date lastDateOfNextMonth = calen.getTime();
                toDate = format1.format(lastDateOfNextMonth);

                calen.set(Calendar.DATE, calen.getActualMinimum(Calendar.DATE));

                Date firstDateOfNextMonth = calen.getTime();
                fromDate = format1.format(firstDateOfNextMonth);
            }
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;

                /*getbancaRenewalListBranchwise(string strBrcode, string strFromReqDate, string strToReqDate,
                string strEmailId, string strMobileNo, string strAuthKey) */


                request = new SoapObject(NAMESPACE, METHOD_NAME_BRANCHWISE_RENEWAL_LIST);
                request.addProperty("strBrcode", branchCode);
                request.addProperty("strFromReqDate", fromDate);
                request.addProperty("strToReqDate", toDate);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);

                request.addProperty("strAuthKey", strCIFBDMPassword.trim());

                commonMethods.TLSv12Enable();

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();


                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME_BRANCHWISE_RENEWAL_LIST;
                androidHttpTranport.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();

                if (!response.toString().contentEquals("<CIFPolicyList />")) {

                    ParseXML prsObj = new ParseXML();
                    String inputpolicylist = response.toString();
                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "CIFPolicyList");
                    error = prsObj.parseXmlTag(
                            inputpolicylist, "ScreenData");

                    if (error == null) {
                        // for agent policy list
                        System.out.println("request:" + inputpolicylist);
                        List<String> Node = prsObj.parseParentNode(inputpolicylist, "Table");
                        List<BDMBranchwiseRenewalListValuesModel> nodeData = parseNodeBranchwiseRenewalList(Node);
                        globalDataList.clear();
                        globalDataList.addAll(nodeData);
                        error = "success";
                    } else {
                        error = "1";
                    }

                } else {
                    error = "1";
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
            llBankTYpe.setVisibility(View.GONE);
            textviewRecordCount.setVisibility(View.GONE);
            if (running) {
                if (error.equalsIgnoreCase("success")) {
                    textviewRecordCount.setText("Total Record :" + globalDataList.size());
                    textviewRecordCount.setVisibility(View.VISIBLE);
                    edittextSearch.setVisibility(View.VISIBLE);
                    llBankTYpe.setVisibility(View.VISIBLE);
                    selectedAdapter = new SelectedAdapter(globalDataList);
                    recyclerview.setAdapter(selectedAdapter);
                    recyclerview.invalidate();

                    SpinnerAdapterBankType spinnerAdapterBankType = new SpinnerAdapterBankType(bankTypeList);
                    spinnerBankType.setAdapter(spinnerAdapterBankType);

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

    public class SelectedAdapter extends RecyclerView.Adapter<SelectedAdapter.
            ViewHolderAdapter> implements Filterable {

        private ArrayList<BDMBranchwiseRenewalListValuesModel> lstAdapterList, lstSearch;

        SelectedAdapter(ArrayList<BDMBranchwiseRenewalListValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public Filter getFilter() {

            if (filterFlag.equalsIgnoreCase("et")) {
                return new Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence charSequence) {
                        final FilterResults oReturn = new FilterResults();
                        final ArrayList<BDMBranchwiseRenewalListValuesModel> results = new ArrayList<>();

                        System.out.println("charSequence.toString() = " + spinnerBankType.getSelectedItemPosition());
                        if (lstSearch == null)
                            lstSearch = lstAdapterList;

                        String strSearch = charSequence == null ? "" : charSequence.toString();
                        System.out.println("charSequence.toString() = " + lstSearch.size());
                        System.out.println("charSequence.toString() = " + lstAdapterList.size());
                        if (!strSearch.equals("")) {
                            if (lstSearch != null && lstSearch.size() > 0) {
                                for (final BDMBranchwiseRenewalListValuesModel model : lstSearch) {
                                    if (model.getPOLICYNUMBER().toLowerCase().contains(charSequence.toString().toLowerCase())
                                            || model.getHOLDERPERSONFIRSTNAME().toLowerCase().contains(charSequence.toString().toLowerCase())
                                            || model.getHOLDERPERSONLASTNAME().toLowerCase().contains(charSequence.toString().toLowerCase())
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

                        lstAdapterList = (ArrayList<BDMBranchwiseRenewalListValuesModel>) results.values;
                        selectedAdapter = new SelectedAdapter(lstAdapterList);
                        recyclerview.setAdapter(selectedAdapter);
                        notifyDataSetChanged();

                        textviewRecordCount.setText("Total Record :" + lstAdapterList.size());
                    }
                };
            } else {
                return new Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence charSequence) {
                        final FilterResults oReturn = new FilterResults();
                        final ArrayList<BDMBranchwiseRenewalListValuesModel> results = new ArrayList<>();
                        //if (lstSearch == null)
                        lstSearch = globalDataList;

                        String strSearch = charSequence == null ? "" : charSequence.toString();

                        if (!strSearch.equals("")) {
                            if (lstSearch != null && lstSearch.size() > 0) {
                                for (final BDMBranchwiseRenewalListValuesModel model : lstSearch) {
                                    if (model.getBANKTYPE().toLowerCase()
                                            .contains(charSequence.toString().toLowerCase())) {
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

                        lstAdapterList = (ArrayList<BDMBranchwiseRenewalListValuesModel>) results.values;
                        selectedAdapter = new SelectedAdapter(lstAdapterList);
                        recyclerview.setAdapter(selectedAdapter);
                        notifyDataSetChanged();
                        textviewRecordCount.setText("Total Record :" + lstAdapterList.size());
                    }
                };
            }

        }


        @Override
        public int getItemCount() {
            return lstAdapterList.size();
        }

        @Override
        public ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_item_branchwise_renewal_list, parent, false);

            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final ViewHolderAdapter holder, int position) {
            holder.textviewPolicyNumber.setText(lstAdapterList.get(position).getPOLICYNUMBER());
            holder.txtcustomercode.setText(lstAdapterList.get(position).getHOLDERID());
            String name = lstAdapterList.get(position).getHOLDERPERSONFIRSTNAME() + " "
                    + lstAdapterList.get(position).getHOLDERPERSONLASTNAME();

            holder.textviewPolicyHolderName.setText(name);

            String policyCurrentStatus = lstAdapterList.get(position).getPOLICYCURRENTSTATUS();

            holder.textviewPolicyCurrentStatus.setText(policyCurrentStatus);
            if (policyCurrentStatus.equalsIgnoreCase("Lapse")) {
                holder.imageviewSMS.setVisibility(View.INVISIBLE);
            } else {
                holder.imageviewSMS.setVisibility(View.VISIBLE);
            }

            holder.textviewDueDate.setText(lstAdapterList.get(position).getPREMIUMFUP());

            holder.textviewPremiumAmount.setText(lstAdapterList.get(position).getPREMIUMGROSSAMOUNT());

            holder.textviewPaymentType.setText(lstAdapterList.get(position).getPAYMENTTYPE());

            holder.textviewContactNumber.setText(lstAdapterList.get(position).getCONTACTMOBILE());
            holder.textviewBankType.setText(lstAdapterList.get(position).getBANKTYPE());

            holder.textviewDOC.setText(lstAdapterList.get(position).getPOLICYRISKCOMMENCEMENTDATE());
            holder.textviewBranchName.setText(branchName);
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
                    commonMethods.hideKeyboard(edittextSearch, context);
                    int index = holder.getAdapterPosition();
                    //showDispositionAlert(lstAdapterList.get(index));
                    final String premiumFUP = lstAdapterList.get(index).getPREMIUMFUP();
                    final String policyNumber = lstAdapterList.get(index).getPOLICYNUMBER();
                    commonMethods.updateAltMobileAlert(context, premiumFUP,  policyNumber,
                            BranchwiseRenewalListActivity.this);

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
                                BranchwiseRenewalListActivity.this::getPremiumInterfaceMethod);
                        getPremiumAmountCommonAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        String msg = "Gross Premium Amount is - " + lstAdapterList.get(index).getPREMIUMGROSSAMOUNT();
                        commonMethods.showMessageDialog(context, msg);
                    }
                }
            });

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
                            BranchwiseRenewalListActivity.this::getFundValueInterfaceMethod);
                    fundValueAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            });
            } else {
                holder.llFundValue.setVisibility(View.GONE);
            }


            if (TextUtils.isEmpty(lstAdapterList.get(position).getCONTACTOFFICE())) {
                holder.llOfficeContactMaster.setVisibility(View.GONE);
            } else {
                holder.llOfficeContactMaster.setVisibility(View.VISIBLE);
                holder.textviewContactOffice.setText(lstAdapterList.get(position).getCONTACTOFFICE());
                holder.textviewContactOffice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mobileNumber = holder.textviewContactOffice.getText().toString();

                        if (!TextUtils.isEmpty(mobileNumber)) {
                            commonMethods.callMobileNumber(mobileNumber, context);
                        }
                    }
                });
                holder.LLofficeContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mobileNumber = holder.textviewContactOffice.getText().toString();

                        if (!TextUtils.isEmpty(mobileNumber)) {
                            commonMethods.callMobileNumber(mobileNumber, context);
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

                                    final String dueDate = holder.textviewDueDate.getText().toString();
                                    final String status = holder.textviewPolicyCurrentStatus.getText().toString();
                                    final String amount = holder.textviewPremiumAmount.getText().toString();


                                    sendRenewalSMSAsynTask = new SendRenewalSMSAsynTask(policyNumber, mobileNumber, dueDate,
                                            status, amount, "English",
                                            context, BranchwiseRenewalListActivity.this::getSMSDetailsInterfaceMethod);
                                    sendRenewalSMSAsynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                }


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

        public class ViewHolderAdapter extends RecyclerView.ViewHolder {


            private final TextView textviewPolicyNumber, textviewPolicyHolderName, textviewPolicyCurrentStatus,
                    textviewDueDate, textviewPremiumAmount, textviewPaymentType, textviewContactNumber,
                    txtcustomercode,textviewBankType, textviewContactOffice, textviewDOC, textviewBranchName,
                    textviewResidenceOffice,buttonFundValue,tvActualPremium,tvPaymentMechanism;
            private ImageView imgcontact_cust_r,imageviewSMS;
            private LinearLayout LLofficeContact,llOfficeContactMaster, llFundValue, LLResidenceContact, llResidenceMaster;
            private final Button buttonCRM,buttonUpdatAltMobile;

            ViewHolderAdapter(View v) {
                super(v);
                textviewPolicyNumber = v.findViewById(R.id.textviewPolicyNumber);
                txtcustomercode = v.findViewById(R.id.txtcustomercode);
                textviewPolicyHolderName = v.findViewById(R.id.textviewPolicyHolderName);
                textviewPolicyCurrentStatus = v.findViewById(R.id.textviewPolicyCurrentStatus);
                textviewDueDate = v.findViewById(R.id.textviewDueDate);

                textviewPremiumAmount = v.findViewById(R.id.textviewPremiumAmount);
                textviewPaymentType = v.findViewById(R.id.textviewPaymentType);
                textviewContactNumber = v.findViewById(R.id.textviewContactNumber);
                textviewBankType = v.findViewById(R.id.textviewBankType);
                imgcontact_cust_r = v.findViewById(R.id.imgcontact_cust_r);
                imageviewSMS = v.findViewById(R.id.imageviewSMS);

                textviewDOC = v.findViewById(R.id.textviewDOC);
                textviewBranchName = v.findViewById(R.id.textviewBranchName);
                textviewContactOffice = v.findViewById(R.id.textviewContactOffice);
                LLofficeContact = v.findViewById(R.id.LLofficeContact);
                llOfficeContactMaster = v.findViewById(R.id.llOfficeContactMaster);

                textviewResidenceOffice = v.findViewById(R.id.textviewResidenceOffice);
                llResidenceMaster = v.findViewById(R.id.llResidenceMaster);
                LLResidenceContact = v.findViewById(R.id.LLResidenceContact);

                llFundValue = v.findViewById(R.id.llFundValue);
                llFundValue.setVisibility(View.GONE);
                buttonFundValue = v.findViewById(R.id.buttonFundValue);
                tvActualPremium = v.findViewById(R.id.tvActualPremium);
                tvPaymentMechanism = v.findViewById(R.id.tvPaymentMechanism);
                buttonCRM = v.findViewById(R.id.buttonCRM);
                buttonUpdatAltMobile = v.findViewById(R.id.buttonUpdatAltMobile);

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
        if (downloadBranchWiseRenewalListAsync != null) {
            downloadBranchWiseRenewalListAsync.cancel(true);
        }
        if (fundValueAsyncTask != null) {
            fundValueAsyncTask.cancel(true);
        }
        if (getPremiumAmountCommonAsync != null) {
            getPremiumAmountCommonAsync.cancel(true);
        }

        if (service != null) {
            service.cancel(true);
        }
    }

    private void clearList() {
        edittextSearch.setVisibility(View.GONE);
        textviewRecordCount.setVisibility(View.GONE);
        llBankTYpe.setVisibility(View.GONE);
        textviewRecordCount.setText("");
        edittextSearch.setText("");

        if (globalDataList != null && selectedAdapter != null) {
            globalDataList.clear();
            selectedAdapter = new SelectedAdapter(globalDataList);
            recyclerview.setAdapter(selectedAdapter);
            recyclerview.invalidate();
        }
    }

    public ArrayList<BDMBranchwiseRenewalListValuesModel> parseNodeBranchwiseRenewalList(List<String> lsNode) {
        ArrayList<BDMBranchwiseRenewalListValuesModel> lsData = new ArrayList<>();

        String POLICYNUMBER, HOLDERID, HOLDERPERSONFIRSTNAME, HOLDERPERSONLASTNAME, POLICYCURRENTSTATUS,
                PREMIUMFUP, PREMIUMGROSSAMOUNT, PAYMENTTYPE, CONTACTMOBILE, BANKTYPE;
        ParseXML parseXML = new ParseXML();
        bankTypeList = new ArrayList<>();
        bankTypeList.add("Filter By Bank Type");
        for (String Node : lsNode) {

            POLICYNUMBER = parseXML.parseXmlTag(Node, "POLICYNUMBER");
            POLICYNUMBER = POLICYNUMBER == null ? "" : POLICYNUMBER;

            HOLDERID = parseXML.parseXmlTag(Node, "HOLDERID");
            HOLDERID = HOLDERID == null ? "" : HOLDERID;

            HOLDERPERSONFIRSTNAME = parseXML.parseXmlTag(Node, "HOLDERPERSONFIRSTNAME");
            HOLDERPERSONFIRSTNAME = HOLDERPERSONFIRSTNAME == null ? "" : HOLDERPERSONFIRSTNAME;

            HOLDERPERSONLASTNAME = parseXML.parseXmlTag(Node, "HOLDERPERSONLASTNAME");
            HOLDERPERSONLASTNAME = HOLDERPERSONLASTNAME == null ? "" : HOLDERPERSONLASTNAME;

            POLICYCURRENTSTATUS = parseXML.parseXmlTag(Node, "POLICYCURRENTSTATUS");
            POLICYCURRENTSTATUS = POLICYCURRENTSTATUS == null ? "" : POLICYCURRENTSTATUS;

            PREMIUMFUP = parseXML.parseXmlTag(Node, "PREMIUMFUP");
            if (PREMIUMFUP == null) {
                PREMIUMFUP = "";
            } else {

                PREMIUMFUP = PREMIUMFUP.split("T")[0];

                Date dt1 = null;
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat df1 = new SimpleDateFormat("dd-MMMM-yyyy");
                try {
                    dt1 = df.parse(PREMIUMFUP);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                PREMIUMFUP = df1.format(dt1);

            }


            PREMIUMGROSSAMOUNT = parseXML.parseXmlTag(Node, "PREMIUMGROSSAMOUNT");
            PREMIUMGROSSAMOUNT = PREMIUMGROSSAMOUNT == null ? "" : PREMIUMGROSSAMOUNT;

            PAYMENTTYPE = parseXML.parseXmlTag(Node, "PAYMENTTYPE");
            PAYMENTTYPE = PAYMENTTYPE == null ? "" : PAYMENTTYPE;

            CONTACTMOBILE = parseXML.parseXmlTag(Node, "CONTACTMOBILE");
            CONTACTMOBILE = CONTACTMOBILE == null ? "" : CONTACTMOBILE;

            BANKTYPE = parseXML.parseXmlTag(Node, "BANKTYPE");
            BANKTYPE = BANKTYPE == null ? "" : BANKTYPE;

            if (!bankTypeList.contains(BANKTYPE)) {
                bankTypeList.add(BANKTYPE);
            }
            String strPayType = "";


            if (PAYMENTTYPE.contentEquals("Money IN")) {
                strPayType = "Paid";
            } else if (PAYMENTTYPE.contentEquals("Money OUT")) {
                strPayType = "Unpaid";
            }

            String POLICYRISKCOMMENCEMENTDATE = parseXML.parseXmlTag(Node, "POLICYRISKCOMMENCEMENTDATE");
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

            String CONTACTRESIDENCE = parseXML.parseXmlTag(Node, "CONTACTRESIDENCE");
            CONTACTRESIDENCE = CONTACTRESIDENCE == null ? "" : CONTACTRESIDENCE;

            String CONTACTOFFICE = parseXML.parseXmlTag(Node, "CONTACTOFFICE");
            CONTACTOFFICE = CONTACTOFFICE == null ? "" : CONTACTOFFICE;

            String POLICYTYPE = parseXML.parseXmlTag(Node, "POLICYTYPE");
            POLICYTYPE = POLICYTYPE == null ? "" : POLICYTYPE;


            String POLICYPAYMENTMECHANISM = parseXML.parseXmlTag(Node, "POLICYPAYMENTMECHANISM");
            POLICYPAYMENTMECHANISM = POLICYPAYMENTMECHANISM == null ? "" : POLICYPAYMENTMECHANISM;

            if (PAYMENTTYPE.contentEquals("Money OUT")) {

                BDMBranchwiseRenewalListValuesModel nodeVal = new BDMBranchwiseRenewalListValuesModel(POLICYNUMBER, HOLDERID, HOLDERPERSONFIRSTNAME, HOLDERPERSONLASTNAME, POLICYCURRENTSTATUS, PREMIUMFUP,
                        PREMIUMGROSSAMOUNT, PAYMENTTYPE, CONTACTMOBILE, BANKTYPE, POLICYRISKCOMMENCEMENTDATE, CONTACTRESIDENCE,
                        CONTACTOFFICE, POLICYTYPE, POLICYPAYMENTMECHANISM);
                lsData.add(nodeVal);

            }

        }
        return lsData;
    }

    public class BDMBranchwiseRenewalListValuesModel {

        /*<CIFPolicyList>
            <Table>
                <POLICYNUMBER>2D346698806</POLICYNUMBER>
                <HOLDERID>759347349</HOLDERID>
                <HOLDERPERSONFIRSTNAME>Rajib</HOLDERPERSONFIRSTNAME>
                <HOLDERPERSONLASTNAME>Ray</HOLDERPERSONLASTNAME>
                <POLICYCURRENTSTATUS>Inforce</POLICYCURRENTSTATUS>
                <PREMIUMFUP>2019-06-15T00:00:00-07:00</PREMIUMFUP>
                <PREMIUMGROSSAMOUNT>19692.32</PREMIUMGROSSAMOUNT>
        <PAYMENTTYPE>Money OUT</PAYMENTTYPE>
                <CONTACTMOBILE>9064522529</CONTACTMOBILE>
            </Table>
        </CIFPolicyList>*/

        private final String POLICYNUMBER;
        private final String HOLDERID;
        private final String HOLDERPERSONFIRSTNAME;
        private final String HOLDERPERSONLASTNAME;
        private final String POLICYCURRENTSTATUS;
        private final String PREMIUMFUP;
        private final String PREMIUMGROSSAMOUNT;
        private final String PAYMENTTYPE;
        private final String CONTACTMOBILE;
        private final String BANKTYPE;
        private final String POLICYRISKCOMMENCEMENTDATE, CONTACTRESIDENCE,
                CONTACTOFFICE, POLICYTYPE, POLICYPAYMENTMECHANISM;

        public BDMBranchwiseRenewalListValuesModel(String POLICYNUMBER, String HOLDERID, String HOLDERPERSONFIRSTNAME,
                                                   String HOLDERPERSONLASTNAME, String POLICYCURRENTSTATUS, String PREMIUMFUP,
                                                   String PREMIUMGROSSAMOUNT, String PAYMENTTYPE, String CONTACTMOBILE,
                                                   String BANKTYPE, String POLICYRISKCOMMENCEMENTDATE,
                                                   String CONTACTRESIDENCE, String CONTACTOFFICE,
                                                   String POLICYTYPE,String POLICYPAYMENTMECHANISM) {
            this.POLICYNUMBER = POLICYNUMBER;
            this.HOLDERID = HOLDERID;
            this.HOLDERPERSONFIRSTNAME = HOLDERPERSONFIRSTNAME;
            this.HOLDERPERSONLASTNAME = HOLDERPERSONLASTNAME;
            this.POLICYCURRENTSTATUS = POLICYCURRENTSTATUS;
            this.PREMIUMFUP = PREMIUMFUP;
            this.PREMIUMGROSSAMOUNT = PREMIUMGROSSAMOUNT;
            this.PAYMENTTYPE = PAYMENTTYPE;
            this.CONTACTMOBILE = CONTACTMOBILE;
            this.BANKTYPE = BANKTYPE;
            this.POLICYRISKCOMMENCEMENTDATE = POLICYRISKCOMMENCEMENTDATE;
            this.CONTACTRESIDENCE = CONTACTRESIDENCE;
            this.CONTACTOFFICE = CONTACTOFFICE;
            this.POLICYTYPE = POLICYTYPE;
            this.POLICYPAYMENTMECHANISM = POLICYPAYMENTMECHANISM;
        }

        public String getPOLICYNUMBER() {
            return POLICYNUMBER;
        }

        public String getHOLDERID() {
            return HOLDERID;
        }

        public String getHOLDERPERSONFIRSTNAME() {
            return HOLDERPERSONFIRSTNAME;
        }

        public String getHOLDERPERSONLASTNAME() {
            return HOLDERPERSONLASTNAME;
        }

        public String getPOLICYCURRENTSTATUS() {
            return POLICYCURRENTSTATUS;
        }

        public String getPREMIUMFUP() {
            return PREMIUMFUP;
        }

        public String getPREMIUMGROSSAMOUNT() {
            return PREMIUMGROSSAMOUNT;
        }

        public String getPAYMENTTYPE() {
            return PAYMENTTYPE;
        }

        public String getCONTACTMOBILE() {
            return CONTACTMOBILE;
        }

        public String getBANKTYPE() {
            return BANKTYPE;
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
    }

    class SpinnerAdapterBankType extends BaseAdapter {

        class ViewHolder {
            TextView c1;
        }

        private ArrayList<String> allElementDetails;
        private LayoutInflater mInflater;


        public SpinnerAdapterBankType(ArrayList<String> results) {
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
            SpinnerAdapterBankType.ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.textview_default, parent,
                        false);
                holder = new SpinnerAdapterBankType.ViewHolder();
                holder.c1 = convertView.findViewById(R.id.tv_content);

                convertView.setTag(holder);
            } else {
                holder = (SpinnerAdapterBankType.ViewHolder) convertView.getTag();
            }
            holder.c1.setText(String.valueOf(allElementDetails.get(position)));

            return convertView;
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

    void getPremiumInterfaceMethod(String premiumAmount, String result) {
        if (premiumAmount.equals("")) {
            commonMethods.showMessageDialog(context, result);
        } else {
            commonMethods.showMessageDialog(context, "Gross Premium Amount is - " + premiumAmount);
        }
    }

    void getSMSDetailsInterfaceMethod(String result) {
        if (result.equalsIgnoreCase("1")) {
            commonMethods.showMessageDialog(context, "Message sent successfully");
        } else {
            commonMethods.showMessageDialog(context, "Message sending failed");
        }
    }

    public void getUpdateAltMobResultMethod(String result) {
        if (result != null && result.equalsIgnoreCase("Success")) {
            commonMethods.showMessageDialog(context, "Mobile Number Updated Successfully");
        } else {
            commonMethods.showMessageDialog(context, "Mobile Number Not Updated.Please Try Again.");
        }
    }
}
