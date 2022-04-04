package sbilife.com.pointofsale_bancaagency.home.rinnrakshareports.rinnrakshanb;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class RinnRakshaOutstandingActivity extends AppCompatActivity implements ServiceHits.DownLoadData {

    private final String METHOD_NAME = "getRinnOutstanding_smrt";
    private ProgressDialog mProgressDialog;
    private ArrayList<RinnRakshaOutstandingValuesModel> globalDataList;
    private Context context;
    private EditText edittextSearch;
    private TextView textviewRecordCount;
    private RecyclerView recyclerview;

    private ServiceHits service;
    private SelectedAdapter selectedAdapter;
    private CommonMethods commonMethods;
    private String strCIFBDMUserId = "", strCIFBDMEmailId = "",
            strCIFBDMMObileNo = "";
    private RinnRakshaOutstandingAsync rinnRakshaOutstandingAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_rinn_raksha_outstanding);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "PDR Report");

        edittextSearch = findViewById(R.id.edittextSearch);
        textviewRecordCount = findViewById(R.id.textviewRecordCount);

        recyclerview = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        // set LayoutManager to RecyclerView
        recyclerview.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
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
                        if (rinnRakshaOutstandingAsync != null) {
                            rinnRakshaOutstandingAsync.cancel(true);
                        }

                        if (mProgressDialog != null) {
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }
                    }
                });

        mProgressDialog.setMax(100);
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
        service_hits(strCIFBDMUserId);
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
        rinnRakshaOutstandingAsync = new RinnRakshaOutstandingAsync();
        rinnRakshaOutstandingAsync.execute();
    }

    class RinnRakshaOutstandingAsync extends AsyncTask<String, String, String> {

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

                running = true;
                SoapObject request;
                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("strCode", strCIFBDMUserId);
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

                        List<RinnRakshaOutstandingValuesModel> nodeData = parseNodeRinnRakshaOutstanding(Node);
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
                edittextSearch.setVisibility(View.GONE);
                if (error == null) {
                    edittextSearch.setVisibility(View.VISIBLE);
                    selectedAdapter = new SelectedAdapter(globalDataList);
                    recyclerview.setAdapter(selectedAdapter);
                    recyclerview.invalidate();
                    String count = "Total Count : " + globalDataList.size();
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

        private ArrayList<RinnRakshaOutstandingValuesModel> lstAdapterList, lstSearch;

        SelectedAdapter(ArrayList<RinnRakshaOutstandingValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {


                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<RinnRakshaOutstandingValuesModel> results = new ArrayList<>();

                    if (lstSearch == null)
                        lstSearch = lstAdapterList;

                    String strSearch = charSequence == null ? "" : charSequence.toString();

                    if (!strSearch.equals("")) {
                        if (lstSearch != null && lstSearch.size() > 0) {
                            for (final RinnRakshaOutstandingValuesModel model : lstSearch) {
                                if (model.getZONE().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getREGION().toLowerCase().contains(charSequence.toString().toLowerCase())
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

                    lstAdapterList = (ArrayList<RinnRakshaOutstandingValuesModel>) results.values;
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
                    R.layout.list_item_rinn_raksha_outstanding, parent, false);

            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final ViewHolderAdapter holder, final int position) {

            holder.textviewZone.setText(lstAdapterList.get(position).getZONE());
            holder.textviewRegion.setText(lstAdapterList.get(position).getREGION());
            holder.textMPN.setText(lstAdapterList.get(position).getMPN());
            holder.textMPNCount.setText(lstAdapterList.get(position).getMPN_CNT());
            holder.textRequirementMedical.setText(lstAdapterList.get(position).getREQUIREMENT_x0020_MED());

            holder.textRequirementMedicalCount.setText(lstAdapterList.get(position).getREQUIREMENT_x0020_MED_CNT());
            holder.textRequirementNonMedical.setText(lstAdapterList.get(position).getREQUIREMENT_x0020_NONMED());
            holder.textRequirementNonMedicalCount.setText(lstAdapterList.get(position).getREQUIREMENT_x0020_NONMED_CNT());
            holder.textBillPIWC.setText(lstAdapterList.get(position).getBILL_PIWC());
            holder.textBillPIWCCount.setText(lstAdapterList.get(position).getBILL_PIWC_CNT());

        }


        public class ViewHolderAdapter extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,
                MenuItem.OnMenuItemClickListener {
            private final TextView textviewZone, textviewRegion, textMPN, textMPNCount,
                    textRequirementMedical, textRequirementMedicalCount, textRequirementNonMedical,
                    textRequirementNonMedicalCount, textBillPIWC, textBillPIWCCount;

            ViewHolderAdapter(View v) {
                super(v);
                textviewZone = v.findViewById(R.id.textviewZone);
                textviewRegion = v.findViewById(R.id.textviewRegion);
                textMPN = v.findViewById(R.id.textMPN);
                textMPNCount = v.findViewById(R.id.textMPNCount);
                textRequirementMedical = v.findViewById(R.id.textRequirementMedical);

                textRequirementMedicalCount = v.findViewById(R.id.textRequirementMedicalCount);
                textRequirementNonMedical = v.findViewById(R.id.textRequirementNonMedical);
                textRequirementNonMedicalCount = v.findViewById(R.id.textRequirementNonMedicalCount);
                textBillPIWC = v.findViewById(R.id.textBillPIWC);
                textBillPIWCCount = v.findViewById(R.id.textBillPIWCCount);
                v.setOnCreateContextMenuListener(this);

            }


            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("Services");

                MenuItem rinn_raksha_outstanding_detailsMenu = menu.add(Menu.NONE, 1, 1, "PDR Report Details");
                rinn_raksha_outstanding_detailsMenu.setOnMenuItemClickListener(this);
            }

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case 1:
                        commonMethods.callActivityWithHomeTagYes(context, RinnRakshaOutstandingDetailsActivity.class);
                        break;
                }
                return true;
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

        if (rinnRakshaOutstandingAsync != null) {
            rinnRakshaOutstandingAsync.cancel(true);
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

    class RinnRakshaOutstandingValuesModel {

        private String ZONE, REGION, MPN, MPN_CNT, REQUIREMENT_x0020_MED, REQUIREMENT_x0020_MED_CNT,
                REQUIREMENT_x0020_NONMED, REQUIREMENT_x0020_NONMED_CNT, BILL_PIWC, BILL_PIWC_CNT;

        public RinnRakshaOutstandingValuesModel(String ZONE, String REGION, String MPN, String MPN_CNT, String REQUIREMENT_x0020_MED, String REQUIREMENT_x0020_MED_CNT, String REQUIREMENT_x0020_NONMED, String REQUIREMENT_x0020_NONMED_CNT, String BILL_PIWC, String BILL_PIWC_CNT) {
            this.ZONE = ZONE;
            this.REGION = REGION;
            this.MPN = MPN;
            this.MPN_CNT = MPN_CNT;
            this.REQUIREMENT_x0020_MED = REQUIREMENT_x0020_MED;
            this.REQUIREMENT_x0020_MED_CNT = REQUIREMENT_x0020_MED_CNT;
            this.REQUIREMENT_x0020_NONMED = REQUIREMENT_x0020_NONMED;
            this.REQUIREMENT_x0020_NONMED_CNT = REQUIREMENT_x0020_NONMED_CNT;
            this.BILL_PIWC = BILL_PIWC;
            this.BILL_PIWC_CNT = BILL_PIWC_CNT;
        }

        public String getZONE() {
            return ZONE;
        }

        public String getREGION() {
            return REGION;
        }

        public String getMPN() {
            return MPN;
        }

        public String getMPN_CNT() {
            return MPN_CNT;
        }

        public String getREQUIREMENT_x0020_MED() {
            return REQUIREMENT_x0020_MED;
        }

        public String getREQUIREMENT_x0020_MED_CNT() {
            return REQUIREMENT_x0020_MED_CNT;
        }

        public String getREQUIREMENT_x0020_NONMED() {
            return REQUIREMENT_x0020_NONMED;
        }

        public String getREQUIREMENT_x0020_NONMED_CNT() {
            return REQUIREMENT_x0020_NONMED_CNT;
        }

        public String getBILL_PIWC() {
            return BILL_PIWC;
        }

        public String getBILL_PIWC_CNT() {
            return BILL_PIWC_CNT;
        }
    }


    ArrayList<RinnRakshaOutstandingValuesModel> parseNodeRinnRakshaOutstanding(List<String> lsNode) {
        ArrayList<RinnRakshaOutstandingValuesModel> lsData = new ArrayList<>();
        ParseXML parseXML = new ParseXML();
        String ZONE, REGION, MPN, MPN_CNT, REQUIREMENT_x0020_MED, REQUIREMENT_x0020_MED_CNT,
                REQUIREMENT_x0020_NONMED, REQUIREMENT_x0020_NONMED_CNT, BILL_PIWC, BILL_PIWC_CNT;
        for (String Node : lsNode) {

            ZONE = parseXML.parseXmlTag(Node, "ZONE");
            ZONE = ZONE == null ? "" : ZONE;

            REGION = parseXML.parseXmlTag(Node, "REGION");
            REGION = REGION == null ? "" : REGION;

            MPN = parseXML.parseXmlTag(Node, "MPN");
            MPN = MPN == null ? "" : MPN;

            MPN_CNT = parseXML.parseXmlTag(Node, "MPN_CNT");
            MPN_CNT = MPN_CNT == null ? "" : MPN_CNT;

            REQUIREMENT_x0020_MED = parseXML.parseXmlTag(Node, "REQUIREMENT_x0020_MED");
            REQUIREMENT_x0020_MED = REQUIREMENT_x0020_MED == null ? "" : REQUIREMENT_x0020_MED;

            REQUIREMENT_x0020_MED_CNT = parseXML.parseXmlTag(Node, "REQUIREMENT_x0020_MED_CNT");
            REQUIREMENT_x0020_MED_CNT = REQUIREMENT_x0020_MED_CNT == null ? "" : REQUIREMENT_x0020_MED_CNT;

            REQUIREMENT_x0020_NONMED = parseXML.parseXmlTag(Node, "REQUIREMENT_x0020_NONMED");
            REQUIREMENT_x0020_NONMED = REQUIREMENT_x0020_NONMED == null ? "" : REQUIREMENT_x0020_NONMED;


            REQUIREMENT_x0020_NONMED_CNT = parseXML.parseXmlTag(Node, "REQUIREMENT_x0020_NONMED_CNT");
            REQUIREMENT_x0020_NONMED_CNT = REQUIREMENT_x0020_NONMED_CNT == null ? "" : REQUIREMENT_x0020_NONMED_CNT;

            BILL_PIWC = parseXML.parseXmlTag(Node, "BILL_PIWC");
            BILL_PIWC = BILL_PIWC == null ? "" : BILL_PIWC;

            BILL_PIWC_CNT = parseXML.parseXmlTag(Node, "BILL_PIWC_CNT");
            BILL_PIWC_CNT = BILL_PIWC_CNT == null ? "" : BILL_PIWC_CNT;

            RinnRakshaOutstandingValuesModel nodeVal = new RinnRakshaOutstandingValuesModel(ZONE, REGION, MPN, MPN_CNT, REQUIREMENT_x0020_MED, REQUIREMENT_x0020_MED_CNT, REQUIREMENT_x0020_NONMED, REQUIREMENT_x0020_NONMED_CNT, BILL_PIWC, BILL_PIWC_CNT);
            lsData.add(nodeVal);
        }
        return lsData;
    }
}