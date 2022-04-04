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

import sbilife.com.pointofsale_bancaagency.CustomerDetailActivity;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class RinnRakshaOutstandingDetailsActivity extends AppCompatActivity implements ServiceHits.DownLoadData {
    private final String METHOD_NAME = "getRinnOutstandingDetail_smrt";
    private ProgressDialog mProgressDialog;
    private ArrayList<RinnRakshaOutstandingDetailsValuesModel> globalDataList;
    private Context context;
    private EditText edittextSearch;
    private TextView textviewRecordCount;
    private RecyclerView recyclerview;

    private ServiceHits service;
    private SelectedAdapter selectedAdapter;
    private CommonMethods commonMethods;
    private String strCIFBDMUserId = "", strCIFBDMEmailId = "",
            strCIFBDMMObileNo = "";
    private RinnRakshaOutstandingDetailsAsync rinnRakshaOutstandingDetailsAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_rinn_raksha_outstanding_details);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Rinn Raksha Outstanding Details");

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
                        if (rinnRakshaOutstandingDetailsAsync != null) {
                            rinnRakshaOutstandingDetailsAsync.cancel(true);
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
        rinnRakshaOutstandingDetailsAsync = new RinnRakshaOutstandingDetailsAsync();
        rinnRakshaOutstandingDetailsAsync.execute();
    }

    class RinnRakshaOutstandingDetailsAsync extends AsyncTask<String, String, String> {

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

                        List<RinnRakshaOutstandingDetailsValuesModel> nodeData = parseNodeRinnRakshaOutstandingDetails(Node);
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

        private ArrayList<RinnRakshaOutstandingDetailsValuesModel> lstAdapterList, lstSearch;

        SelectedAdapter(ArrayList<RinnRakshaOutstandingDetailsValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {


                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<RinnRakshaOutstandingDetailsValuesModel> results = new ArrayList<>();

                    if (lstSearch == null)
                        lstSearch = lstAdapterList;

                    String strSearch = charSequence == null ? "" : charSequence.toString();

                    if (!strSearch.equals("")) {
                        if (lstSearch != null && lstSearch.size() > 0) {
                            for (final RinnRakshaOutstandingDetailsValuesModel model : lstSearch) {
                                if (model.getFORMNUMBER().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getLOANACCOUNTNUMBER().toLowerCase().contains(charSequence.toString().toLowerCase())
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

                    lstAdapterList = (ArrayList<RinnRakshaOutstandingDetailsValuesModel>) results.values;
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
                    R.layout.list_item_rinn_raksha_oustanding_details, parent, false);

            return new SelectedAdapter.ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final SelectedAdapter.ViewHolderAdapter holder, final int position) {

            holder.textviewZone.setText(lstAdapterList.get(position).getZONE());
            holder.textviewRegion.setText(lstAdapterList.get(position).getREGION());

            holder.textFormNumber.setText(lstAdapterList.get(position).getFORMNUMBER());
            holder.textLoanAccountNumber.setText(lstAdapterList.get(position).getLOANACCOUNTNUMBER());
            holder.textStatus.setText(lstAdapterList.get(position).getSTATUS());
            holder.textMoneyIn.setText(lstAdapterList.get(position).getMONEYIN());

        }


        public class ViewHolderAdapter extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,
                MenuItem.OnMenuItemClickListener {
            private final TextView textviewZone, textviewRegion, textFormNumber, textLoanAccountNumber, textStatus, textMoneyIn;

            ViewHolderAdapter(View v) {
                super(v);
                textviewZone = v.findViewById(R.id.textviewZone);
                textviewRegion = v.findViewById(R.id.textviewRegion);

                textFormNumber = v.findViewById(R.id.textFormNumber);
                textLoanAccountNumber = v.findViewById(R.id.textLoanAccountNumber);
                textStatus = v.findViewById(R.id.textStatus);
                textMoneyIn = v.findViewById(R.id.textMoneyIn);
                v.setOnCreateContextMenuListener(this);

            }


            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("Services");

                MenuItem rinn_raksha_outstanding_detailsMenu = menu.add(Menu.NONE, 1, 1, "Rinn Raksha Outstanding Details");
                rinn_raksha_outstanding_detailsMenu.setOnMenuItemClickListener(this);
            }

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case 1:
                        Intent intent = new Intent(context, CustomerDetailActivity.class);
                        startActivity(intent);
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

        if (rinnRakshaOutstandingDetailsAsync != null) {
            rinnRakshaOutstandingDetailsAsync.cancel(true);
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

    public class RinnRakshaOutstandingDetailsValuesModel {
        private String ZONE, REGION, FORMNUMBER, LOANACCOUNTNUMBER, STATUS, MONEYIN, ROWNUMBER;

        public RinnRakshaOutstandingDetailsValuesModel(String ZONE, String REGION, String FORMNUMBER, String LOANACCOUNTNUMBER, String STATUS, String MONEYIN, String ROWNUMBER) {
            this.ZONE = ZONE;
            this.REGION = REGION;
            this.FORMNUMBER = FORMNUMBER;
            this.LOANACCOUNTNUMBER = LOANACCOUNTNUMBER;
            this.STATUS = STATUS;
            this.MONEYIN = MONEYIN;
            this.ROWNUMBER = ROWNUMBER;
        }

        public String getZONE() {
            return ZONE;
        }

        public String getREGION() {
            return REGION;
        }

        public String getFORMNUMBER() {
            return FORMNUMBER;
        }

        public String getLOANACCOUNTNUMBER() {
            return LOANACCOUNTNUMBER;
        }

        public String getSTATUS() {
            return STATUS;
        }

        public String getMONEYIN() {
            return MONEYIN;
        }

        public String getROWNUMBER() {
            return ROWNUMBER;
        }
    }


    public ArrayList<RinnRakshaOutstandingDetailsValuesModel> parseNodeRinnRakshaOutstandingDetails(List<String> lsNode) {
        ArrayList<RinnRakshaOutstandingDetailsValuesModel> lsData = new ArrayList<>();
        ParseXML parseXML = new ParseXML();
        String ZONE, REGION, FORMNUMBER, LOANACCOUNTNUMBER, STATUS, MONEYIN, ROWNUMBER;
        for (String Node : lsNode) {

            ZONE = parseXML.parseXmlTag(Node, "ZONE");
            ZONE = ZONE == null ? "" : ZONE;

            REGION = parseXML.parseXmlTag(Node, "REGION");
            REGION = REGION == null ? "" : REGION;

            FORMNUMBER = parseXML.parseXmlTag(Node, "FORMNUMBER");
            FORMNUMBER = FORMNUMBER == null ? "" : FORMNUMBER;

            LOANACCOUNTNUMBER = parseXML.parseXmlTag(Node, "LOANACCOUNTNUMBER");
            LOANACCOUNTNUMBER = LOANACCOUNTNUMBER == null ? "" : LOANACCOUNTNUMBER;

            STATUS = parseXML.parseXmlTag(Node, "STATUS");
            STATUS = STATUS == null ? "" : STATUS;

            MONEYIN = parseXML.parseXmlTag(Node, "MONEYIN");
            MONEYIN = MONEYIN == null ? "" : MONEYIN;

            ROWNUMBER = parseXML.parseXmlTag(Node, "ROWNUMBER");
            ROWNUMBER = ROWNUMBER == null ? "" : ROWNUMBER;


            RinnRakshaOutstandingDetailsValuesModel nodeVal = new RinnRakshaOutstandingDetailsValuesModel(ZONE, REGION, FORMNUMBER, LOANACCOUNTNUMBER, STATUS, MONEYIN, ROWNUMBER);
            lsData.add(nodeVal);
        }
        return lsData;
    }
}
