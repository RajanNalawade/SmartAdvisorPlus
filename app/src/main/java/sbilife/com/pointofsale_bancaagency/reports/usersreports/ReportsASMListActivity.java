package sbilife.com.pointofsale_bancaagency.reports.usersreports;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsPersistency;

public class ReportsASMListActivity extends AppCompatActivity implements ServiceHits.DownLoadData {


    private final String METHOD_NAME_AM_BDM_LIST = "getBaReRepEmpList";

    private DownloadFileAsyncChannelUserReports taskDownloadFileAsyncChannelUserReports;
    private ProgressDialog mProgressDialog;

    private CommonMethods mCommonMethods;
    private Context context;

    private ArrayList<ParseXML.ChannelUserReportsValuesModel> globalList;

    private String strCIFBDMUserId = "";
    private String strCIFBDMEmailId = "";
    private String strCIFBDMPassword = "";
    private String strCIFBDMMObileNo = "";
    private String strUserType = "";
    private int lstBdmListCount = 0;

    private SelectedAdapterASMReports selectedAdapterASMReports;

    private RecyclerView recyclerViewUserReports;

    private TextView txterrordescbdm, txtbdmlistcount;

    private EditText edittextSearch;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.user_reports);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        context = this;
        mCommonMethods = new CommonMethods();
        mProgressDialog = new ProgressDialog(context);

        strUserType = mCommonMethods.GetUserType(context);
        taskDownloadFileAsyncChannelUserReports = new DownloadFileAsyncChannelUserReports();

        edittextSearch = findViewById(R.id.edittextSearch);
        edittextSearch.setVisibility(View.VISIBLE);
        recyclerViewUserReports = findViewById(R.id.recyclerViewUserReports);
        recyclerViewUserReports.setVisibility(View.VISIBLE);
        //lstReq.setHasFixedSize(true);
        // set a LinearLayoutManager with default horizontal orientation and false value for reverseLayout to show the items from start to end
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        // set LayoutManager to RecyclerView
        recyclerViewUserReports.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        globalList = new ArrayList<>();
        selectedAdapterASMReports = new SelectedAdapterASMReports(globalList);
        recyclerViewUserReports.setAdapter(selectedAdapterASMReports);
        recyclerViewUserReports.setItemAnimator(new DefaultItemAnimator());

        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");

        if (fromHome.equalsIgnoreCase("Y")) {
            strUserType = mCommonMethods.GetUserType(context);
            getUserDetails();
        } else {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");
            strUserType = intent.getStringExtra("strUserType");

            try {
                strCIFBDMPassword = SimpleCrypto.encrypt("SBIL", "sbil");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(Arrays.asList(getResources().getStringArray(R.array.list_asm)).indexOf(strUserType.toLowerCase()) != -1) {
        /*if (strUserType.equalsIgnoreCase("ASM")) {*/
            mCommonMethods.setApplicationToolbarMenu(this, "DSM List");
        }


        txterrordescbdm = findViewById(R.id.txterrordescbdn);
        txtbdmlistcount = findViewById(R.id.txtbdnlistcount);

        mProgressDialog = new ProgressDialog(this,
                ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html
                .fromHtml("<font color='#00a1e3'><b>" + Message
                        + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        mProgressDialog.setButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (taskDownloadFileAsyncChannelUserReports != null) {
                            taskDownloadFileAsyncChannelUserReports.cancel(true);
                        }
                        if (mProgressDialog != null) {
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }
                    }
                });

        mProgressDialog.setMax(100);
        edittextSearch.setVisibility(View.GONE);
        edittextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                selectedAdapterASMReports.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        getUserReportsList();
    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = mCommonMethods
                .setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }


    private void startDownloadPolicyList() {
        taskDownloadFileAsyncChannelUserReports = new DownloadFileAsyncChannelUserReports();
        taskDownloadFileAsyncChannelUserReports.execute("demo");
    }


    @Override
    public void downLoadData() {
        startDownloadPolicyList();
    }

    class DownloadFileAsyncChannelUserReports extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String inputpolicylist = "", strBdmListErrorCOde1 = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                String soap_action;

                running = true;
                SoapObject request;

                // getBaReRepEmpList(string strRepId, string strEmailId, string strMobileNo, string strAuthKey)
                /*http://tempuri.org/"><CIFPolicyList> <Table>
                <EMPLOYEEID>24307</EMPLOYEEID>
                <EMPLOYEENAME>ASHISH K SWARNKAR</EMPLOYEENAME>
                <POSITIONTYPE>Unit Manager</POSITIONTYPE>
                 </Table> <Table> <EMPLOYEEID>22197</EMPLOYEEID>
                 <EMPLOYEENAME>TARUN KUMAR PATEL</EMPLOYEENAME>
                 <POSITIONTYPE>Unit Manager</POSITIONTYPE> </Table> */

                soap_action = "http://tempuri.org/getBaReRepEmpList";
                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE,
                        METHOD_NAME_AM_BDM_LIST);
                request.addProperty("strRepId", strCIFBDMUserId);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());


                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    androidHttpTranport
                            .call(soap_action, envelope);
                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("anyType{}")) {

                        SoapPrimitive sa;
                        try {
                            sa = (SoapPrimitive) envelope.getResponse();

                            inputpolicylist = sa.toString();

                            ParseXML prsObj = new ParseXML();

                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "CIFPolicyList");
                            inputpolicylist = new ParseXML().parseXmlTag(
                                    inputpolicylist, "ScreenData");
                            strBdmListErrorCOde1 = inputpolicylist;

                            if (strBdmListErrorCOde1 == null) {
                                inputpolicylist = sa.toString();
                                inputpolicylist = prsObj.parseXmlTag(
                                        inputpolicylist, "CIFPolicyList");

                                List<String> Node = prsObj.parseParentNode(
                                        inputpolicylist, "Table");

                                List<ParseXML.ChannelUserReportsValuesModel> nodeData = prsObj
                                        .parseNodeChannelUserReports(Node);

                                // final List<XMLHolderBdm> lst;
                                globalList = new ArrayList<>();
                                globalList.clear();

                                globalList = new ArrayList<>(nodeData);

                                lstBdmListCount = globalList.size();
                            }

                        } catch (Exception e) {
                            try {
                                throw (e);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            mProgressDialog.dismiss();
                            running = false;
                        }
                    }

                } catch (IOException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
                } catch (XmlPullParserException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
                }
            } catch (Exception e) {
                try {
                    throw (e);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
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
            if (running) {

                txterrordescbdm.setVisibility(View.VISIBLE);
                txtbdmlistcount.setVisibility(View.VISIBLE);

                if (strBdmListErrorCOde1 == null) {
                    edittextSearch.setVisibility(View.VISIBLE);
                    txterrordescbdm.setText("");
                    selectedAdapterASMReports = new SelectedAdapterASMReports(globalList);
                    recyclerViewUserReports.setAdapter(selectedAdapterASMReports);
                    recyclerViewUserReports.invalidate();

                    if(Arrays.asList(getResources().getStringArray(R.array.list_asm)).indexOf(strUserType.toLowerCase()) != -1) {
                    /*if (strUserType.equalsIgnoreCase("ASM")) {*/
                        txtbdmlistcount.setText("Total DSM : " + lstBdmListCount);
                    }

                } else {

                    txterrordescbdm.setText("No Record Found");

                    /*if (strUserType.equalsIgnoreCase("ASM")) {*/
                    if(Arrays.asList(getResources().getStringArray(R.array.list_asm)).indexOf(strUserType.toLowerCase()) != -1) {
                        txtbdmlistcount.setText("Total DSM : 0");
                    }
                    clearList();

                }
            } else {
                mCommonMethods.showMessageDialog(context, mCommonMethods.SERVER_ERROR);
            }
        }
    }


    private void getUserReportsList() {

        taskDownloadFileAsyncChannelUserReports = new DownloadFileAsyncChannelUserReports();
        if (mCommonMethods.isNetworkConnected(context)) {
            service_hits(METHOD_NAME_AM_BDM_LIST);
        } else {
            mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
        }

    }

    private void service_hits(String strServiceName) {

        String strbdmstatusstring = "Active";
        ServiceHits service = new ServiceHits(context,
                strServiceName, strbdmstatusstring, strCIFBDMUserId,
                strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (taskDownloadFileAsyncChannelUserReports != null) {
            taskDownloadFileAsyncChannelUserReports.cancel(true);
        }
    }

    public class SelectedAdapterASMReports extends RecyclerView.Adapter<SelectedAdapterASMReports.ViewHolderAdapter> implements Filterable {

        private ArrayList<ParseXML.ChannelUserReportsValuesModel> lstAdapterList, lstSearch;


        SelectedAdapterASMReports(ArrayList<ParseXML.ChannelUserReportsValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<ParseXML.ChannelUserReportsValuesModel> results = new ArrayList<>();

                    if (lstSearch == null)
                        lstSearch = lstAdapterList;

                    String strSearch = charSequence == null ? "" : charSequence.toString();

                    if (!strSearch.equals("")) {
                        if (lstSearch != null && lstSearch.size() > 0) {
                            for (final ParseXML.ChannelUserReportsValuesModel model : lstSearch) {
                                if (model.getEMPLOYEENAME().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getEMPLOYEEID().toLowerCase().contains(charSequence.toString().toLowerCase())) {
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

                    lstAdapterList = (ArrayList<ParseXML.ChannelUserReportsValuesModel>) results.values;
                    selectedAdapterASMReports = new SelectedAdapterASMReports(lstAdapterList);
                    recyclerViewUserReports.setAdapter(selectedAdapterASMReports);

                    notifyDataSetChanged();
                    /*if (strUserType.equalsIgnoreCase("ASM")) {*/
                    if(Arrays.asList(getResources().getStringArray(R.array.list_asm)).indexOf(strUserType.toLowerCase()) != -1) {
                        txtbdmlistcount.setText("Total DSM : " + lstAdapterList.size());
                    }
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
                    R.layout.list_item_channel_user_list, parent, false);

            // set the view's size, margins, paddings and layout parameters
            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final ViewHolderAdapter holder, final int position) {


            /*if (strUserType.equalsIgnoreCase("ASM")) {*/

            if(Arrays.asList(getResources().getStringArray(R.array.list_asm)).indexOf(strUserType.toLowerCase()) != -1) {
                holder.textviewChannelReportsUserIdTitle.setText("DSM Code: ");
                holder.textviewChannelReportsUserNameTitle.setText("DSM Name: ");
            }

            String name = lstAdapterList.get(position).getEMPLOYEENAME() == null ? ""
                    : lstAdapterList.get(position).getEMPLOYEENAME();
            String userType = lstAdapterList.get(position).getPOSITIONTYPE() == null ? "" : lstAdapterList
                    .get(position).getPOSITIONTYPE();
            String userId = lstAdapterList.get(position).getEMPLOYEEID() == null ? "" : lstAdapterList
                    .get(position).getEMPLOYEEID();

            holder.textviewChannelReportsUserName.setText(name);
            holder.textviewChannelReportsUserId.setText(userId);
            holder.textviewChannelReportsUserType.setText(userType);
            holder.textviewMobileNumber.setText(lstAdapterList.get(position).getCONTACTMOBILE());
            String mobileNumber = holder.textviewMobileNumber.getText().toString();
            if(TextUtils.isEmpty(mobileNumber)){
                holder.llMobileLayout.setVisibility(View.GONE);
            }else{
                holder.llMobileLayout.setVisibility(View.VISIBLE);
            }
            holder.viewDivider.setVisibility(View.VISIBLE);

            holder.imgcontact_cust_r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mobileNumber = holder.textviewMobileNumber.getText().toString();
                    if (!TextUtils.isEmpty(mobileNumber)) {
                        mCommonMethods.callMobileNumber(mobileNumber, context);
                    }
                }
            });
            holder.textviewMobileNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mobileNumber = holder.textviewMobileNumber.getText().toString();
                    if(!TextUtils.isEmpty(mobileNumber)) {
                        mCommonMethods.callMobileNumber(mobileNumber, context);
                    }
                }
            });

        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,
                MenuItem.OnMenuItemClickListener {


            final TextView textviewChannelReportsUserNameTitle;
            final TextView textviewChannelReportsUserName;
            final TextView textviewChannelReportsUserIdTitle;
            final TextView textviewChannelReportsUserId;
            final TextView textviewChannelReportsUserType;
            final View viewDivider;
            final TextView textviewMobileNumber;
            ImageView imgcontact_cust_r;
            final LinearLayout llMobileLayout;
            ViewHolderAdapter(View v) {
                super(v);

                textviewChannelReportsUserNameTitle = v.findViewById(R.id.textviewChannelReportsUserNameTitle);
                textviewChannelReportsUserName = v.findViewById(R.id.textviewChannelReportsUserName);
                textviewChannelReportsUserIdTitle = v.findViewById(R.id.textviewChannelReportsUserIdTitle);
                textviewChannelReportsUserId = v.findViewById(R.id.textviewChannelReportsUserId);
                textviewChannelReportsUserType = v.findViewById(R.id.textviewChannelReportsUserType);
                viewDivider = v.findViewById(R.id.viewDivider);
                textviewMobileNumber = v.findViewById(R.id.textviewMobileNumber);
                imgcontact_cust_r = v.findViewById(R.id.imgcontact_cust_r);
                llMobileLayout = v.findViewById(R.id.llMobileLayout);
                v.setOnCreateContextMenuListener(this);

            }


            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                /*if (strUserType.equalsIgnoreCase("ASM")) {*/
                if(Arrays.asList(getResources().getStringArray(R.array.list_asm)).indexOf(strUserType.toLowerCase()) != -1) {
                    String selectedListItem = lstAdapterList.get(getAdapterPosition()).getPOSITIONTYPE();
                    /*if (selectedListItem.equalsIgnoreCase("DSM")
                            || selectedListItem.equalsIgnoreCase("SrDSM")) {*/
                    if(Arrays.asList(getResources().getStringArray(R.array.list_dsm)).indexOf(selectedListItem.toLowerCase()) != -1) {

                        menu.setHeaderTitle("Select Action");
                        menu.add("BSM List");

                        MenuItem myActionItem = menu.add("Persistency");
                        myActionItem.setOnMenuItemClickListener(this);
                    }
                }
            }

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Menu Item Clicked!
                System.out.println("item = " + item.getTitle() + " info.position:" + getAdapterPosition());

                String title = item.getTitle().toString();
                String userCode = lstAdapterList.get(getAdapterPosition()).getEMPLOYEEID();
                String selectedListItem = lstAdapterList.get(getAdapterPosition()).getPOSITIONTYPE();
                Intent intent = null;

                if (title.equalsIgnoreCase("BSM List")) {
                    intent = new Intent(context,
                            ReportsAMBSMListActivity.class);
                } else if (title.equalsIgnoreCase("Persistency")) {
                    intent = new Intent(context, AgencyReportsPersistency.class);
                }

                if (intent != null) {
                    intent.putExtra("fromHome", "N");
                    intent.putExtra("strBDMCifCOde", userCode);
                    intent.putExtra("strEmailId", strCIFBDMEmailId);
                    intent.putExtra("strPassword", strCIFBDMPassword.trim());
                    intent.putExtra("strMobileNo", strCIFBDMMObileNo);
                    /*if (selectedListItem.equalsIgnoreCase("DSM")
                            || selectedListItem.equalsIgnoreCase("SrDSM")) {*/
                    if(Arrays.asList(getResources().getStringArray(R.array.list_dsm)).indexOf(selectedListItem.toLowerCase()) != -1) {
                        intent.putExtra("strUserType", "DSM");
                    }
                    startActivity(intent);
                }

                return true;
            }
        }
    }

    private void clearList() {
        if (globalList != null && selectedAdapterASMReports != null) {
            globalList.clear();
            selectedAdapterASMReports = new SelectedAdapterASMReports(globalList);
            recyclerViewUserReports.setAdapter(selectedAdapterASMReports);
            recyclerViewUserReports.invalidate();

        }
    }
}

