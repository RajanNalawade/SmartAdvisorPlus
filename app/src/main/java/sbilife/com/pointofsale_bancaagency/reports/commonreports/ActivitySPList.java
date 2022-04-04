package sbilife.com.pointofsale_bancaagency.reports.commonreports;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.home.dashboard.NewDashboardAgent;
import sbilife.com.pointofsale_bancaagency.home.dashboard.NewDashboardCIF;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.reports.BancaReportsAdvisorProposalsStatusActivity;
import sbilife.com.pointofsale_bancaagency.reports.BancaReportsMandateRegistrationStatusActivity;
import sbilife.com.pointofsale_bancaagency.reports.BancaReportsMaturityActivity;
import sbilife.com.pointofsale_bancaagency.reports.BancaReportsPolicyDetailsActivity;
import sbilife.com.pointofsale_bancaagency.reports.BancaReportsPolicyListActivity;
import sbilife.com.pointofsale_bancaagency.reports.BancaReportsProposalTracker;
import sbilife.com.pointofsale_bancaagency.reports.BancaReportsRenewalActivity;
import sbilife.com.pointofsale_bancaagency.reports.BancaReportsRevivalActivity;
import sbilife.com.pointofsale_bancaagency.reports.BancaReportsSBDueListActivity;
import sbilife.com.pointofsale_bancaagency.reports.BancaReportsSurrenderActivity;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsAdvisorProposalsStatusActivity;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsCommissionActivity;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsMandateRegistrationStatusActivity;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsMaturityActivity;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsPersistency;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsPolicyDetailsActivity;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsPolicyListActivity;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsProposalTracker;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsSBDueListActivity;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsSurrenderActivity;
import sbilife.com.pointofsale_bancaagency.reports.newbusiness.BancaReportsPIWCActivity;
import sbilife.com.pointofsale_bancaagency.reports.newbusiness.InstaImageFailureCasesActivity;
import sbilife.com.pointofsale_bancaagency.reports.newbusiness.PIWCAudioCallingPendingListActivity;
import sbilife.com.pointofsale_bancaagency.utility.DocUploadNonMedicalPendingActivity;
import sbilife.com.pointofsale_bancaagency.utility.DocumentsUploadActivity;

public class ActivitySPList extends AppCompatActivity implements ServiceHits.DownLoadData {

    private CommonMethods mCommonMethods;
    private Context mContext;
    private String strCIFBDMUserId = "", strCIFBDMEmailId = "",
            strCIFBDMMObileNo = "", strCIFBDMPassword = "", branchName = "", branchCode = "";

    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME_SP_LIST = "getSPList_smrt";
    private ProgressDialog mProgressDialog;

    private TextView textviewRecordCount;
    private EditText edittextSearch;
    private RecyclerView recyclerviewSPList;
    private Spinner spinnerBranchCodes;

    private ArrayList<ParseXML.SPListValueModel> globalDataList = new ArrayList<>();
    private ArrayList<ParseXML.BranchDetailsForBDMValuesModel> branchDetailsList = new ArrayList<>();

    private SelectedAdapter mSelectedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_splist);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        mContext = this;
        mCommonMethods = new CommonMethods();

        mCommonMethods.setApplicationToolbarMenu(this, "SP List");

        textviewRecordCount = findViewById(R.id.textviewRecordCount);
        edittextSearch = findViewById(R.id.edittextSearch);
        spinnerBranchCodes = findViewById(R.id.spinnerBranchCodes);
        recyclerviewSPList = findViewById(R.id.recyclerviewSPList);

        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = mCommonMethods.setUserDetails(mContext);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        // set LayoutManager to RecyclerView
        recyclerviewSPList.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        mSelectedAdapter = new SelectedAdapter(globalDataList);
        recyclerviewSPList.setAdapter(mSelectedAdapter);
        recyclerviewSPList.setItemAnimator(new DefaultItemAnimator());

        edittextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mSelectedAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        spinnerBranchCodes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                branchName = branchDetailsList.get(position).getBANKBRANCHNAME();
                branchCode = branchDetailsList.get(position).getBANKBRANCHCODE();

                if (position != 0) {

                    edittextSearch.setVisibility(View.GONE);
                    textviewRecordCount.setVisibility(View.GONE);

                    globalDataList = new ArrayList<>();
                    mSelectedAdapter = new SelectedAdapter(globalDataList);
                    recyclerviewSPList.setAdapter(mSelectedAdapter);

                    service_hits(branchCode);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        DownloadBranchCodeAsyncTask mDownloadBranchCodeAsyncTask = new DownloadBranchCodeAsyncTask();
        mDownloadBranchCodeAsyncTask.execute();
    }

    class DownloadBranchCodeAsyncTask extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String status = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading Please wait...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;

                String METHOD_NAME_BRANCH_CODE = "getBranchListbanca_smrt";
                request = new SoapObject(NAMESPACE, METHOD_NAME_BRANCH_CODE);
                request.addProperty("strEmpID", strCIFBDMUserId);
                mCommonMethods.appendSecurityParams(mContext, request, strCIFBDMEmailId, strCIFBDMMObileNo);
                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
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

                        ParseXML.BranchDetailsForBDMValuesModel listNode = prsObj.new BranchDetailsForBDMValuesModel(branchCode, branchName);
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

            if (running) {
                if (status.equalsIgnoreCase("Success")) {
                    SpinnerAdapter spinnerAdapter = new SpinnerAdapter(branchDetailsList);
                    spinnerBranchCodes.setAdapter(spinnerAdapter);

                } else {
                    mCommonMethods.showMessageDialog(mContext, "No Record Found");
                }
            } else {
                mCommonMethods.showMessageDialog(mContext, "No Record Found");
            }
        }
    }

    class SpinnerAdapter extends BaseAdapter {

        class ViewHolder {
            TextView c1;
        }

        private final ArrayList<ParseXML.BranchDetailsForBDMValuesModel> allElementDetails;
        private final LayoutInflater mInflater;


        SpinnerAdapter(ArrayList<ParseXML.BranchDetailsForBDMValuesModel> results) {
            allElementDetails = results;
            mInflater = LayoutInflater.from(mContext);
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
            SpinnerAdapter.ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.textview_default, parent,
                        false);
                holder = new SpinnerAdapter.ViewHolder();
                holder.c1 = convertView.findViewById(R.id.tv_content);

                convertView.setTag(holder);
            } else {
                holder = (SpinnerAdapter.ViewHolder) convertView.getTag();
            }
            holder.c1.setText(String.valueOf(allElementDetails.get(position).getBANKBRANCHNAME()));

            return convertView;
        }

    }

    private void service_hits(String input) {

        ServiceHits service = new ServiceHits(mContext, METHOD_NAME_SP_LIST, input,
                strCIFBDMUserId, strCIFBDMEmailId,
                strCIFBDMMObileNo, strCIFBDMPassword, this);
        service.execute();
    }

    @Override
    public void downLoadData() {
        new AsyncTaskGetSPList().execute();
    }

    class AsyncTaskGetSPList extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String status = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading Please wait...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);

            mProgressDialog.show();
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;

                request = new SoapObject(NAMESPACE, METHOD_NAME_SP_LIST);
                request.addProperty("strBrCode", branchCode);
                mCommonMethods.appendSecurityParams(mContext, request, strCIFBDMEmailId, strCIFBDMMObileNo);
                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME_SP_LIST;
                androidHttpTranport.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();
                String inputpolicylist = response.toString();
                //String inputpolicylist = "<CIFPolicyList> <Table> <CHANNELTECHNICALID>990852220</CHANNELTECHNICALID> <UM_BANKNAME>Stock Holding Corporation Of India Limited</UM_BANKNAME> <BANKNAME>Sriganganagar</BANKNAME> </Table> <Table> <CHANNELTECHNICALID>990993159</CHANNELTECHNICALID> <UM_BANKNAME>Stock Holding Corporation Of India Limited</UM_BANKNAME> <BANKNAME>Sriganganagar</BANKNAME> </Table> <Table> <CHANNELTECHNICALID>991105657</CHANNELTECHNICALID> <UM_BANKNAME>Stock Holding Corporation Of India Limited</UM_BANKNAME> <BANKNAME>Bikaner</BANKNAME> </Table> <Table> <CHANNELTECHNICALID>990783804</CHANNELTECHNICALID> <UM_BANKNAME>Stock Holding Corporation Of India Limited</UM_BANKNAME> <BANKNAME>Bikaner</BANKNAME> </Table> <Table> <CHANNELTECHNICALID>991067636</CHANNELTECHNICALID> <UM_BANKNAME>Stock Holding Corporation Of India Limited</UM_BANKNAME> <BANKNAME>Bikaner</BANKNAME> </Table> <Table> <CHANNELTECHNICALID>990866165</CHANNELTECHNICALID> <UM_BANKNAME>Punjab And Sind Bank</UM_BANKNAME> <BANKNAME>Bikaner</BANKNAME> </Table> <Table> <CHANNELTECHNICALID>990861134</CHANNELTECHNICALID> <UM_BANKNAME>Punjab And Sind Bank</UM_BANKNAME> <BANKNAME>Sri Ganga Nagar -Main</BANKNAME> </Table> <Table> <CHANNELTECHNICALID>990861220</CHANNELTECHNICALID> <UM_BANKNAME>Punjab And Sind Bank</UM_BANKNAME> <BANKNAME>Hanuman Garh Town</BANKNAME> </Table> </CIFPolicyList>";

                if (!inputpolicylist.contentEquals("<CIFPolicyList/>")) {

                    ParseXML prsObj = new ParseXML();

                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "CIFPolicyList");
                    //status = prsObj.parseXmlTag(inputpolicylist, "ScreenData");

                    //if (status == null) {

                    List<String> Node = prsObj.parseParentNode(inputpolicylist, "Table");

                    globalDataList = new ParseXML().parseNodeSPListDeatils(Node);

                    status = "Success";
                    //}

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

            textviewRecordCount.setVisibility(View.VISIBLE);

            if (running) {

                if (status.equalsIgnoreCase("Success")) {
                    mSelectedAdapter = new SelectedAdapter(globalDataList);
                    recyclerviewSPList.setAdapter(mSelectedAdapter);


                    if (globalDataList.size() == 0) {
                        edittextSearch.setVisibility(View.GONE);
                    } else {
                        edittextSearch.setVisibility(View.VISIBLE);
                    }
                    textviewRecordCount.setText("Total Count : " + globalDataList.size());
                } else {
                    edittextSearch.setVisibility(View.GONE);
                    textviewRecordCount.setText("No Record Found");
                }
            } else {
                edittextSearch.setVisibility(View.GONE);
                textviewRecordCount.setText("No Record Found");
            }
        }
    }

    public class SelectedAdapter extends RecyclerView.Adapter<SelectedAdapter.
            ViewHolderAdapter> implements Filterable {

        private ArrayList<ParseXML.SPListValueModel> lstAdapterList, lstSearch;

        private int viewPosition = -1;

        SelectedAdapter(ArrayList<ParseXML.SPListValueModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }

        int getViewPosition() {
            return viewPosition;
        }

        void setViewPosition(int viewPosition) {
            this.viewPosition = viewPosition;
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<ParseXML.SPListValueModel> results = new ArrayList<>();

                    if (lstSearch == null)
                        lstSearch = lstAdapterList;

                    String strSearch = charSequence == null ? "" : charSequence.toString();

                    if (!strSearch.equals("")) {
                        if (lstSearch != null && lstSearch.size() > 0) {
                            for (final ParseXML.SPListValueModel model : lstSearch) {
                                if (model.getStrChannelID().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getStrBankName().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getStrUMBankName().toLowerCase().contains(charSequence.toString().toLowerCase())
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

                    lstAdapterList = (ArrayList<ParseXML.SPListValueModel>) results.values;
                    mSelectedAdapter = new SelectedAdapter(lstAdapterList);
                    recyclerviewSPList.setAdapter(mSelectedAdapter);

                    textviewRecordCount.setText("Total Count : " + lstAdapterList.size());

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
                    R.layout.row_sp_list, parent, false);

            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final ViewHolderAdapter holder, int position) {
            holder.tvSPChannelID.setText(lstAdapterList.get(position).getStrChannelID());
            holder.tvSPUMBankName.setText(lstAdapterList.get(position).getStrUMBankName());
            holder.tvSPBankName.setText(lstAdapterList.get(position).getStrBankName());

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    setViewPosition(holder.getPosition());

                    return false;
                }
            });
        }

        @Override
        public void onViewRecycled(@NonNull ViewHolderAdapter holder) {
            holder.itemView.setOnLongClickListener(null);
            super.onViewRecycled(holder);
        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {


            private final TextView tvSPChannelID, tvSPBankName, tvSPUMBankName;

            ViewHolderAdapter(View v) {
                super(v);
                tvSPChannelID = v.findViewById(R.id.tvSPChannelID);
                tvSPUMBankName = v.findViewById(R.id.tvSPUMBankName);
                tvSPBankName = v.findViewById(R.id.tvSPBankName);

                v.setOnCreateContextMenuListener(this);

                /*v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        int position = getLayoutPosition();

                        String strID = lstAdapterList.get(position).getStrChannelID();

                        mCommonMethods.printLog("ID : ", "Selected : " + strID);

                        return false;
                    }
                });*/
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo contextMenuInfo) {

                menu.setHeaderTitle("Services");
                int id = v.getId();

                /*if (id == R.id.recyclerviewSPList) {*/
                menu.add(0, v.getId(), 0, "Policy List");
                menu.add(0, v.getId(), 1, "Policy Maturity List");
                menu.add(0, v.getId(), 2, "Policy Surrender List");
                menu.add(0, v.getId(), 3, "Policy Revival List");
                menu.add(0, v.getId(), 4, "Policy Renewal List");
                menu.add(0, v.getId(), 5, "Help Desk");
                menu.add(0, v.getId(), 6, "Mandate Registration Status");
                menu.add(0, v.getId(), 7, "PIWC Status");
                menu.add(0, v.getId(), 8, "Proposal Status Tracker");
                menu.add(0, v.getId(), 9, "Advisor Proposals Status");
                menu.add(0, v.getId(), 10, "SB Due List");
                menu.add(0, v.getId(), 11, "Download/share PPC");
                menu.add(0, v.getId(), 12, "Pending Requirements - Non-Medical");
                menu.add(0, v.getId(), 13, "Proposal List");
                menu.add(0, v.getId(), 14, "Persistency");
                menu.add(0, v.getId(), 15, "DashBoard");
                menu.add(0, v.getId(), 16, "Pending Requirements - Medical");
                menu.add(0, v.getId(), 17, "Revival Campaign");
                menu.add(0, v.getId(), 18, "Premium Acknowledgement Receipt");
                menu.add(0, v.getId(), 19, "Rinn Raksha Proposal Tracker");
                menu.add(0, v.getId(), 20, "Auto Mandate Status List");

                menu.add(0, v.getId(), 21, "Alternate Mode Collection Status");
                menu.add(0, v.getId(), 22, "View Medical Status");
                menu.add(0, v.getId(), 23, "Policy Dispatch Status");
                menu.add(0, v.getId(), 24, "PIWC(Audio Calling) List");
                menu.add(0, v.getId(), 25, "Insta Image Verification Status");
                    /*if (strUserType.equalsIgnoreCase("UM")) {
                        menu.add(0, v.getId(), 24, "Commission");
                    }*/
                //}

            }

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        //System.out.println("item = " + item.getTitle() + " info.position:" + mSelectedAdapter.getViewPosition());

        String title = item.getTitle().toString();
        String channelID = globalDataList.get(mSelectedAdapter.getViewPosition()).getStrChannelID();

        if (!TextUtils.isEmpty(channelID)) {

            Intent intent = null;
            String strUserType = mCommonMethods.GetUserType(mContext);

            if (title.equalsIgnoreCase("DashBoard")) {

                if (strUserType.equalsIgnoreCase("UM")) {
                    intent = new Intent(getApplicationContext(), NewDashboardAgent.class);
                } else {
                    intent = new Intent(getApplicationContext(), NewDashboardCIF.class);
                }

            } else if (title.equalsIgnoreCase("Policy Renewal List")) {

                intent = new Intent(mContext, BancaReportsRenewalActivity.class);

            } else if (title.equalsIgnoreCase("Policy Revival List")) {
                intent = new Intent(mContext, BancaReportsRevivalActivity.class);
            } else if (title.equalsIgnoreCase("Policy Surrender List")) {
                if (strUserType.equalsIgnoreCase("AGENT") || strUserType.contentEquals("UM")) {
                    intent = new Intent(mContext, AgencyReportsSurrenderActivity.class);
                } else {
                    intent = new Intent(mContext, BancaReportsSurrenderActivity.class);
                }
            } else if (title.equalsIgnoreCase("Policy List")) {
                if (strUserType.equalsIgnoreCase("AGENT") || strUserType.contentEquals("UM")) {
                    intent = new Intent(mContext, AgencyReportsPolicyListActivity.class);
                } else {
                    intent = new Intent(mContext, BancaReportsPolicyListActivity.class);
                }
            } else if (title.equalsIgnoreCase("Policy Maturity List")) {
                if (strUserType.equalsIgnoreCase("AGENT") || strUserType.contentEquals("UM")) {
                    intent = new Intent(mContext, AgencyReportsMaturityActivity.class);
                } else {
                    intent = new Intent(mContext, BancaReportsMaturityActivity.class);
                }
            } else if (item.getTitle().equals("Persistency")) {
                intent = new Intent(mContext, AgencyReportsPersistency.class);
                if (strUserType.equalsIgnoreCase("UM")) {
                    intent.putExtra("strUserType", "Agent");
                } else {
                    intent.putExtra("strUserType", "CIF");
                }
            } else if (item.getTitle().equals("Commission")) {
                intent = new Intent(mContext, AgencyReportsCommissionActivity.class);
            } else if (title.equalsIgnoreCase("Help Desk")) {
                if (strUserType.equalsIgnoreCase("UM")) {
                    intent = new Intent(mContext, AgencyReportsPolicyDetailsActivity.class);
                } else {
                    intent = new Intent(mContext, BancaReportsPolicyDetailsActivity.class);
                }
            } else if (title.equalsIgnoreCase("Mandate Registration Status")) {
                if (strUserType.equalsIgnoreCase("UM")) {
                    intent = new Intent(mContext, AgencyReportsMandateRegistrationStatusActivity.class);
                } else {
                    intent = new Intent(mContext, BancaReportsMandateRegistrationStatusActivity.class);
                }
            } else if (title.equalsIgnoreCase("PIWC Status")) {
                intent = new Intent(mContext, BancaReportsPIWCActivity.class);
            } else if (title.equalsIgnoreCase("Proposal Status Tracker")) {

                if (strUserType.equalsIgnoreCase("UM")) {
                    intent = new Intent(mContext, AgencyReportsProposalTracker.class);
                } else {
                    intent = new Intent(mContext, BancaReportsProposalTracker.class);
                }

            } else if (title.equalsIgnoreCase("Advisor Proposals Status")) {

                if (strUserType.equalsIgnoreCase("UM")) {
                    intent = new Intent(mContext, AgencyReportsAdvisorProposalsStatusActivity.class);
                } else {
                    intent = new Intent(mContext, BancaReportsAdvisorProposalsStatusActivity.class);
                }

            } else if (title.equalsIgnoreCase("SB Due List")) {

                if (strUserType.equalsIgnoreCase("UM")) {
                    intent = new Intent(mContext, AgencyReportsSBDueListActivity.class);
                } else {
                    intent = new Intent(mContext, BancaReportsSBDueListActivity.class);
                }

            } else if (title.equalsIgnoreCase("Download/share PPC")) {
                intent = new Intent(mContext, DownloadPPCActivity.class);
            } else if (title.equalsIgnoreCase("Requirement Upload")) {
                intent = new Intent(mContext, DocumentsUploadActivity.class);
                if (strUserType.equalsIgnoreCase("UM")) {
                    intent.putExtra("strUserType", "Agent");
                } else {
                    intent.putExtra("strUserType", "CIF");

                }

            } else if (title.equalsIgnoreCase("Proposal List")) {
                intent = new Intent(mContext, CommonReportsProposalListActivity.class);
                if (strUserType.equalsIgnoreCase("UM")) {
                    intent.putExtra("strUserType", "Agent");
                } else {
                    intent.putExtra("strUserType", "CIF");
                }
            } else if (title.equals("Pending Requirements - Non-Medical")) {
                intent = new Intent(mContext, DocUploadNonMedicalPendingActivity.class);
                if (strUserType.equalsIgnoreCase("UM")) {
                    intent.putExtra("strUserType", "Agent");
                } else {
                    intent.putExtra("strUserType", "CIF");
                }
            } else if (title.equalsIgnoreCase("Pending Requirements - Medical")) {
                intent = new Intent(mContext, MedicalPendingRequirementActivity.class);
                if (strUserType.equalsIgnoreCase("UM")) {
                    intent.putExtra("strUserType", "Agent");
                } else {
                    intent.putExtra("strUserType", "CIF");
                }
            } else if (title.equalsIgnoreCase("Revival Campaign")) {
                intent = new Intent(mContext, RevivalCampaignReportList.class);
            } else if (title.equalsIgnoreCase("Rinn Raksha Proposal Tracker")) {
                intent = new Intent(mContext, ProposalRinnStatusActivity.class);
            } else if (title.equalsIgnoreCase("Premium Acknowledgement Receipt")) {
                intent = new Intent(mContext, PremiumPaymentReceiptActivity.class);
            } else if (title.equalsIgnoreCase("Auto Mandate Status List")) {
                intent = new Intent(mContext, AutoMandateRegistrationStatusListReportsActivity.class);
            } else if (title.equalsIgnoreCase("Alternate Mode Collection Status")) {
                intent = new Intent(mContext, AlternetModeCollectionStatusActivity.class);
            } else if (title.equalsIgnoreCase("View Medical Status")) {
                intent = new Intent(mContext, ViewMedicalStatusActivity.class);
            } else if (title.equalsIgnoreCase("Policy Dispatch Status")) {
                intent = new Intent(mContext, PolicyDispatchStatusActivity.class);
            }else if(title.equalsIgnoreCase("PIWC(Audio Calling) List")){
                intent = new Intent(mContext, PIWCAudioCallingPendingListActivity.class);
            }else if(title.equalsIgnoreCase("Insta Image Verification Status")){
                intent = new Intent(mContext, InstaImageFailureCasesActivity.class);
            }

            if (intent != null) {
                intent.putExtra("fromHome", "N");
                intent.putExtra("strBDMCifCOde", channelID);
                intent.putExtra("strMenuItem", item.getTitle());
                intent.putExtra("strEmailId", strCIFBDMEmailId);
                intent.putExtra("strPassword", strCIFBDMPassword.trim());
                intent.putExtra("strMobileNo", strCIFBDMMObileNo);
                startActivity(intent);
            }

        }

        return false;
    }
}
