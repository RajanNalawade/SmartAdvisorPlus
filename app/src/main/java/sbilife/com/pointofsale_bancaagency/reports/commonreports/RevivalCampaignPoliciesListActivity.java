package sbilife.com.pointofsale_bancaagency.reports.commonreports;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class RevivalCampaignPoliciesListActivity extends AppCompatActivity implements ServiceHits.DownLoadData, View.OnClickListener {
    private final String METHOD_NAME_BRANCH_CODE = "getBranchCode_smrt";

    private final String METHOD_NAME_REVIAL_POLICIES = "getRevivalCampainOther_smrt";
    private ProgressDialog mProgressDialog;
    private ServiceHits service;

    private DownloadBranchCodeAsyncTask downloadBranchCodeAsyncTask;
    private DownloadRevivalPoliciesListAsyncTask downloadRevivalPoliciesListAsyncTask;
    private ArrayList<ParseXML.BranchDetailsForBDMValuesModel> branchDetailsList;
    private SelectedAdapter selectedAdapter;
    private ArrayList<ParseXML.RevivalCampaignPoliciesValuesModel> globalDataList;

    private LinearLayout llBranchDetails;
    private Spinner spinnerBranchNames;
    private Button buttonOk;
    private RecyclerView recyclerview;
    private TextView textviewRecordCount;
    private EditText edittextSearch;

    private String branchCode, branchName;
    private String strCIFBDMUserId, strCIFBDMEmailId,
            strCIFBDMPassword, strCIFBDMMObileNo, fromHome;
    private Context context;
    private CommonMethods commonMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_revival_campaign_policies_list);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Revival Campaign Polices");

        llBranchDetails = findViewById(R.id.llBranchDetails);
        spinnerBranchNames = findViewById(R.id.spinnerBranchNames);
        buttonOk = findViewById(R.id.buttonOk);
        recyclerview = findViewById(R.id.recyclerview);
        textviewRecordCount = findViewById(R.id.textviewRecordCount);
        edittextSearch = findViewById(R.id.edittextSearch);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        // set LayoutManager to RecyclerView
        recyclerview.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        globalDataList = new ArrayList<>();
        selectedAdapter = new SelectedAdapter(globalDataList);
        recyclerview.setAdapter(selectedAdapter);
        recyclerview.setItemAnimator(new DefaultItemAnimator());


        mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

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
            CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
            strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
            strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
            strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
            strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
        }

        spinnerBranchNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                branchName = branchDetailsList.get(position).getBANKBRANCHNAME();
                branchCode = branchDetailsList.get(position).getBANKBRANCHCODE();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        edittextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                selectedAdapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        buttonOk.setOnClickListener(this);
        downloadBranchCodeAsyncTask = new DownloadBranchCodeAsyncTask();
        downloadBranchCodeAsyncTask.execute();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonOk:
                clearList();
                if (!TextUtils.isEmpty(branchName) && branchName.equalsIgnoreCase("Select Branch Name")) {
                    commonMethods.showMessageDialog(context, "Please Select Branch Name");
                } else {

                    String input = branchName + "," + branchCode;
                    service_hits(input);
                }
                break;

            default:
                break;
        }
    }

    private void service_hits(String input) {

        service = new ServiceHits(context, METHOD_NAME_BRANCH_CODE, input,
                strCIFBDMUserId, strCIFBDMEmailId,
                strCIFBDMMObileNo, strCIFBDMPassword, this);
        service.execute();
    }

    @Override
    public void downLoadData() {
        downloadRevivalPoliciesListAsyncTask = new DownloadRevivalPoliciesListAsyncTask();
        downloadRevivalPoliciesListAsyncTask.execute();
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
                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME_BRANCH_CODE);
                //string strPolicyNo, string strFromdate, string strTodate, string strCode
                request.addProperty("bdmcode", strCIFBDMUserId);

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME_BRANCH_CODE;
                androidHttpTranport.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();
                String inputpolicylist = response.toString();

                if (!inputpolicylist.contentEquals("<NewDataSet />")) {

                    ParseXML prsObj = new ParseXML();

                    inputpolicylist = prsObj.parseXmlTag(
                            inputpolicylist, "NewDataSet");
                    status = prsObj.parseXmlTag(
                            inputpolicylist, "ScreenData");

                    if (status == null) {

                        List<String> Node = prsObj.parseParentNode(
                                inputpolicylist, "Table");

                        List<ParseXML.BranchDetailsForBDMValuesModel> nodeData = prsObj
                                .parseNodeBranchDetailsForBDM(Node);

                        branchDetailsList = new ArrayList<>();
                        branchDetailsList.clear();
                        branchName = "Select Branch Name";
                        branchCode = "0";

                        ParseXML.BranchDetailsForBDMValuesModel listNode =
                                prsObj.new BranchDetailsForBDMValuesModel(branchCode,
                                        branchName);
                        nodeData.add(0,listNode);

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
            llBranchDetails.setVisibility(View.GONE);
            if (running) {
                if (status.equalsIgnoreCase("Success")) {
                    llBranchDetails.setVisibility(View.VISIBLE);
                    SpinnerAdapter spinnerAdapter = new SpinnerAdapter(branchDetailsList);
                    spinnerBranchNames.setAdapter(spinnerAdapter);
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

    class DownloadRevivalPoliciesListAsyncTask extends AsyncTask<String, String, String> {

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
                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME_REVIAL_POLICIES);
                //string strPolicyNo, string strFromdate, string strTodate, string strCode
                request.addProperty("bdmcode", strCIFBDMUserId);
                request.addProperty("brcode", branchCode);

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME_REVIAL_POLICIES;
                androidHttpTranport.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();
                String inputpolicylist = response.toString();

                if (!inputpolicylist.contentEquals("<NewDataSet />")) {

                    ParseXML prsObj = new ParseXML();

                    inputpolicylist = prsObj.parseXmlTag(
                            inputpolicylist, "NewDataSet");
                    status = prsObj.parseXmlTag(
                            inputpolicylist, "ScreenData");

                    if (status == null) {

                        List<String> Node = prsObj.parseParentNode(
                                inputpolicylist, "Table");

                        List<ParseXML.RevivalCampaignPoliciesValuesModel> nodeData = prsObj
                                .parseNodeRevivalCampaignPoliciesList(Node);
                        globalDataList = new ArrayList<>();
                        globalDataList.clear();

                        globalDataList.addAll(nodeData);
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

            if (running) {
                if (status.equalsIgnoreCase("Success")) {
                    edittextSearch.setVisibility(View.VISIBLE);
                    textviewRecordCount.setVisibility(View.VISIBLE);
                    recyclerview.setVisibility(View.VISIBLE);
                    textviewRecordCount.setText("Total Record : " + globalDataList.size());
                    selectedAdapter = new SelectedAdapter(globalDataList);
                    recyclerview.setAdapter(selectedAdapter);
                    recyclerview.invalidate();

                } else {
                    commonMethods.showMessageDialog(context, "No Record Found");
                    clearList();
                }
            } else {
                commonMethods.showMessageDialog(context, "No Record Found");
                clearList();
            }
        }
    }

    public class SelectedAdapter extends RecyclerView.Adapter<SelectedAdapter.
            ViewHolderAdapter> implements Filterable {

        private ArrayList<ParseXML.RevivalCampaignPoliciesValuesModel> lstAdapterList, lstSearch;

        SelectedAdapter(ArrayList<ParseXML.RevivalCampaignPoliciesValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<ParseXML.RevivalCampaignPoliciesValuesModel> results = new ArrayList<>();

                    if (lstSearch == null)
                        lstSearch = lstAdapterList;

                    String strSearch = charSequence == null ? "" : charSequence.toString();

                    if (!strSearch.equals("")) {
                        if (lstSearch != null && lstSearch.size() > 0) {
                            for (final ParseXML.RevivalCampaignPoliciesValuesModel model : lstSearch) {
                                if (model.getPOLICY_NO().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getHOLDERNAME().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getPRODUCTNAME().toLowerCase().contains(charSequence.toString().toLowerCase())
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

                    lstAdapterList = (ArrayList<ParseXML.RevivalCampaignPoliciesValuesModel>) results.values;
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
                    R.layout.list_item_revival_campaign_policies_list, parent, false);
            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(ViewHolderAdapter holder, int position) {
            holder.textviewPolicyNumber.setText(lstAdapterList.get(position).getPOLICY_NO());
            holder.textviewPolicyHolderName.setText(lstAdapterList.get(position).getHOLDERNAME());
            holder.textviewProductName.setText(lstAdapterList.get(position).getPRODUCTNAME());
            holder.textviewPolicySumAssured.setText(lstAdapterList.get(position).getPOLICYSUMASSURED());

            holder.textviewRAGFlag.setText(lstAdapterList.get(position).getRAG_FLAG());
            holder.textviewDGHRequirement.setText(lstAdapterList.get(position).getDGH_REQUIREMENT());
            holder.textviewNetRevivalAmoutStart.setText(lstAdapterList.get(position).getNET_REVIVAL_START());
            holder.textviewNetRevivalAmoutLast.setText(lstAdapterList.get(position).getNET_REVIVAL_LAST());
        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder {

            private final TextView textviewPolicyNumber, textviewPolicyHolderName, textviewProductName,
                    textviewPolicySumAssured, textviewRAGFlag, textviewDGHRequirement, textviewNetRevivalAmoutStart,
                    textviewNetRevivalAmoutLast;

            ViewHolderAdapter(View v) {
                super(v);
                textviewPolicyNumber = v.findViewById(R.id.textviewPolicyNumber);
                textviewPolicyHolderName = v.findViewById(R.id.textviewPolicyHolderName);
                textviewProductName = v.findViewById(R.id.textviewProductName);
                textviewPolicySumAssured = v.findViewById(R.id.textviewPolicySumAssured);

                textviewRAGFlag = v.findViewById(R.id.textviewRAGFlag);
                textviewDGHRequirement = v.findViewById(R.id.textviewDGHRequirement);
                textviewNetRevivalAmoutStart = v.findViewById(R.id.textviewNetRevivalAmoutStart);
                textviewNetRevivalAmoutLast = v.findViewById(R.id.textviewNetRevivalAmoutLast);

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
        if (downloadRevivalPoliciesListAsyncTask != null) {
            downloadRevivalPoliciesListAsyncTask.cancel(true);
        }
        if (service != null) {
            service.cancel(true);
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

    public class RevivalCampaignValuesModel {
		/*
         <POLICYNUMBER>2E329691206</POLICYNUMBER>
        <HOLDERNAME>Mulla Sadik Vali .</HOLDERNAME>
        <CONTACT_MOBILE>9985361022</CONTACT_MOBILE>
        <REVIVAL_PERIOD_ENDDATE>27-Feb-2022</REVIVAL_PERIOD_ENDDATE>
        <DGH_REQUIREMENT>DGH + Covid Questionnaire &amp; Waiver of Medical</DGH_REQUIREMENT>
        <RAG>Likely</RAG>
		 */

        private final String POLICYNUMBER;
        private final String HOLDERNAME;
        private final String CONTACT_MOBILE;
        private final String REVIVAL_PERIOD_ENDDATE;
        private final String DGH_REQUIREMENT;
        private final String RAG;
        private final String REVIVALDATE;
        private final String POLICYCURRENTSTATUS;
        private final String DOC;
        private final String FUP;

        public RevivalCampaignValuesModel(String POLICYNUMBER, String HOLDERNAME, String CONTACT_MOBILE, String REVIVAL_PERIOD_ENDDATE, String DGH_REQUIREMENT, String RAG, String REVIVALDATE, String POLICYCURRENTSTATUS, String DOC, String FUP) {
            this.POLICYNUMBER = POLICYNUMBER;
            this.HOLDERNAME = HOLDERNAME;
            this.CONTACT_MOBILE = CONTACT_MOBILE;
            this.REVIVAL_PERIOD_ENDDATE = REVIVAL_PERIOD_ENDDATE;
            this.DGH_REQUIREMENT = DGH_REQUIREMENT;
            this.RAG = RAG;
            this.REVIVALDATE = REVIVALDATE;
            this.POLICYCURRENTSTATUS = POLICYCURRENTSTATUS;
            this.DOC = DOC;
            this.FUP = FUP;
        }

        public String getPOLICYNUMBER() {
            return POLICYNUMBER;
        }

        public String getHOLDERNAME() {
            return HOLDERNAME;
        }

        public String getCONTACT_MOBILE() {
            return CONTACT_MOBILE;
        }

        public String getREVIVAL_PERIOD_ENDDATE() {
            return REVIVAL_PERIOD_ENDDATE;
        }

        public String getDGH_REQUIREMENT() {
            return DGH_REQUIREMENT;
        }

        public String getRAG() {
            return RAG;
        }

        public String getREVIVALDATE() {
            return REVIVALDATE;
        }

        public String getPOLICYCURRENTSTATUS() {
            return POLICYCURRENTSTATUS;
        }

        public String getDOC() {
            return DOC;
        }

        public String getFUP() {
            return FUP;
        }
    }

}
