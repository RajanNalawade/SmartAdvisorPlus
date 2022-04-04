package sbilife.com.pointofsale_bancaagency.home.mhr;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
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
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
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
import sbilife.com.pointofsale_bancaagency.home.CarouselHomeActivity;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class MHRProposalListActivity extends AppCompatActivity implements ServiceHits.DownLoadData, View.OnClickListener {
    private final String METHOD_NAME_PROPOSAL_LIST = "getProposalList_mhr";


    private SelectedAdapterProposalList selectedAdapterProposalList;
    private ArrayList<ParseXML.ProposalListValuesModel> proposalList;
    private Context context;
    private CommonMethods commonMethods;

    private DownloadProposalListAsync downloadProposalListAsync;
    private ServiceHits service;
    private ProgressDialog mProgressDialog;

    private int mYear, mMonth, mDay;
    private final int DATE_DIALOG_ID = 1;
    private TextView txtFromDate, txtToDate;
    private String strDateFlag = "", strCIFBDMUserId = "", strCIFBDMEmailId = "",
            strCIFBDMPassword = "", strCIFBDMMObileNo = "";
    // policy list
    private TextView textviewRecordCount, txterrordesc;
    private String strUserType = "", fromHome = "", strFromDate = "", strToDate = "";
    private RecyclerView recyclerviewProposalList;
    private EditText edittextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_common_reports_proposal_list);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Proposal List");

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        txtFromDate = findViewById(R.id.txtFromDate);
        txtToDate = findViewById(R.id.txtToDate);
        edittextSearch = findViewById(R.id.edittextSearch);
        txtFromDate.setOnClickListener(this);
        txtToDate.setOnClickListener(this);

        recyclerviewProposalList = findViewById(R.id.recyclerviewProposalList);
        //lstReq.setHasFixedSize(true);
        // set a LinearLayoutManager with default horizontal orientation and false value for reverseLayout to show the items from start to end
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        // set LayoutManager to RecyclerView
        recyclerviewProposalList.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        proposalList = new ArrayList<>();
        selectedAdapterProposalList = new SelectedAdapterProposalList(proposalList);
        recyclerviewProposalList.setAdapter(selectedAdapterProposalList);
        recyclerviewProposalList.setItemAnimator(new DefaultItemAnimator());

        textviewRecordCount = findViewById(R.id.textviewRecordCount);
        txterrordesc = findViewById(R.id.txterrordesc);
        Button btn_reset_policylist = findViewById(R.id.btn_reset_policylist);
        Button btn_savepolicylist = findViewById(R.id.btn_savepolicylist);

        setDates();
        //getUserDetails();
        Intent intent = getIntent();

        fromHome = intent.getStringExtra("fromHome");
        if (fromHome != null && fromHome.equalsIgnoreCase("N")) {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");
            strUserType = intent.getStringExtra("strUserType");

            try {
                strCIFBDMPassword = SimpleCrypto.encrypt("SBIL", "sbil");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            strUserType = commonMethods.GetUserType(context);
            getUserDetails();
        }

        btn_savepolicylist.setOnClickListener(this);
        btn_reset_policylist.setOnClickListener(this);

        edittextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                selectedAdapterProposalList.getFilter().filter(s);
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
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }

    private void setDates() {
        txtFromDate.setText(commonMethods.getPreviousMonthDate());
        txtToDate.setText(commonMethods.getCurrentMonthDate());
    }

    private void service_hits(String input) {

        service = new ServiceHits(context, METHOD_NAME_PROPOSAL_LIST, input,
                strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, R.style.datepickerstyle,
                        mDateSetListener, mYear, mMonth, mDay);

            default:
                return null;
        }
    }

    private void updateDisplay(int year, int month, int day) {

        String y = String.valueOf(year);
        String m = String.valueOf(month + 1);
        String da = String.valueOf(day);

        m = commonMethods.getFullMonthName(m);
        String totaldate = da + "-" + m + "-" + y;

        if (strDateFlag.equalsIgnoreCase("from")) {
            txtFromDate.setText(totaldate);
        } else {
            txtToDate.setText(totaldate);
        }
    }

    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay(mYear, mMonth, mDay);

        }
    };

    class DownloadProposalListAsync extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String strPersistencyDueDataError = "";
        private int lstPersistencyDueDataCount = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txterrordesc = findViewById(R.id.txterrordesc);
            mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading Please wait...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);

            mProgressDialog.setButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            if (downloadProposalListAsync != null) {
                                downloadProposalListAsync.cancel(true);
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
                SoapObject request;
                //String UserType = commonMethods.GetUserType(context);
                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE,
                        METHOD_NAME_PROPOSAL_LIST);
                //getDetail_Notcollected_pers(string strAgenyCode, string strEmailId,
                // string strMobileNo, string strAuthKey)
                request.addProperty("strCode", strCIFBDMUserId);
                request.addProperty("strFromdate", strFromDate);
                request.addProperty("strTodate", strToDate);
                commonMethods.appendSecurityParams(context, request, strCIFBDMEmailId, strCIFBDMMObileNo);
                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    String SOAP_ACTION_PROPOSAL_LIST = "http://tempuri.org/getProposalList_mhr";
                    androidHttpTranport.call(
                            SOAP_ACTION_PROPOSAL_LIST, envelope);
                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("<NewDataSet />")) {
                        System.out.println("response:" + response.toString());
                        SoapPrimitive sa;
                        try {
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

                                List<String> Node = prsObj.parseParentNode(inputpolicylist, "Table");

                                List<ParseXML.ProposalListValuesModel> nodeData = prsObj
                                        .parseNodeProposalList(Node);
                                lstPersistencyDueDataCount = nodeData.size();
                                proposalList.clear();
                                proposalList.addAll(nodeData);
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

                } catch (IOException | XmlPullParserException e) {
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

                txterrordesc.setVisibility(View.VISIBLE);
                textviewRecordCount.setVisibility(View.VISIBLE);

                if (strPersistencyDueDataError == null) {
                    edittextSearch.setVisibility(View.VISIBLE);
                    txterrordesc.setText("");
                    textviewRecordCount.setText("Total Record : " + lstPersistencyDueDataCount);
                    selectedAdapterProposalList = new SelectedAdapterProposalList(proposalList);
                    recyclerviewProposalList.setAdapter(selectedAdapterProposalList);
                    recyclerviewProposalList.invalidate();

                } else {
                    txterrordesc.setText("No Record Found");
                    textviewRecordCount.setText("Total Record : " + 0);
                    clearList();
                }
            } else {
                commonMethods.showMessageDialog(context, "No record found");
            }
        }
    }


    @Override
    public void downLoadData() {
        downloadProposalListAsync = new DownloadProposalListAsync();
        downloadProposalListAsync.execute();
    }


    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        try {
            final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");
            switch (id) {
                case DATE_DIALOG_ID:
                    final Calendar cal = Calendar.getInstance();

                    if (strDateFlag.equalsIgnoreCase("from")) {
                        String str = txtFromDate.getText().toString();
                        if (!str.equalsIgnoreCase("")) {

                            Date d1 = null;
                            try {
                                d1 = formatter.parse(str);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            cal.setTime(d1);

                            mYear = cal.get(Calendar.YEAR);
                            mMonth = cal.get(Calendar.MONTH);
                            mDay = cal.get(Calendar.DAY_OF_MONTH);
                        }

                    } else {
                        String str = txtToDate.getText().toString();

                        if (!str.equalsIgnoreCase("")) {

                            Date d1 = null;
                            try {
                                d1 = formatter.parse(str);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            cal.setTime(d1);

                            mYear = cal.get(Calendar.YEAR);
                            mMonth = cal.get(Calendar.MONTH);
                            mDay = cal.get(Calendar.DAY_OF_MONTH);
                        }
                    }

                    ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.txtFromDate:
                strDateFlag = "FROM";
                showDialog(DATE_DIALOG_ID);
                break;

            case R.id.txtToDate:
                strDateFlag = "TO";
                showDialog(DATE_DIALOG_ID);
                break;
            case R.id.btn_savepolicylist:
                submitProposalList();
                break;
            case R.id.btn_reset_policylist:
                txtFromDate.setText("");
                txtToDate.setText("");
                txterrordesc.setVisibility(View.GONE);
                textviewRecordCount.setVisibility(View.GONE);
                edittextSearch.setVisibility(View.GONE);
                textviewRecordCount.setVisibility(View.GONE);
                txterrordesc.setVisibility(View.GONE);

                clearList();
                break;

        }
    }

    private void clearList() {
        if (proposalList != null && selectedAdapterProposalList != null) {
            proposalList.clear();
            selectedAdapterProposalList = new SelectedAdapterProposalList(proposalList);
            recyclerviewProposalList.setAdapter(selectedAdapterProposalList);
            recyclerviewProposalList.invalidate();
        }
    }

    private void submitProposalList() {


        strFromDate = txtFromDate.getText().toString();
        strToDate = txtToDate.getText().toString();

        if (strFromDate.equals("") || strToDate.equals("")) {
            commonMethods.showMessageDialog(context, "Please Select Dates");
        } else {

            try {
                String input = strFromDate + "," + strToDate;
                final SimpleDateFormat formatter = new SimpleDateFormat(
                        "dd-MMMM-yyyy");
                final SimpleDateFormat formatter1 = new SimpleDateFormat(
                        "MM-dd-yyyy");
                Date from_dt, to_dt;
                from_dt = formatter.parse(strFromDate);
                strFromDate = formatter1.format(from_dt);

                to_dt = formatter.parse(strToDate);
                strToDate = formatter1.format(to_dt);
                edittextSearch.setVisibility(View.GONE);
                edittextSearch.setText("");
                textviewRecordCount.setVisibility(View.GONE);
                txterrordesc.setVisibility(View.GONE);


                clearList();

                if ((to_dt != null && to_dt.after(from_dt)) || (to_dt != null && to_dt.equals(from_dt))) {

                    if (commonMethods.isNetworkConnected(context)) {

                        service_hits(input);
                    } else
                        commonMethods.showMessageDialog(context,
                                commonMethods.NO_INTERNET_MESSAGE);
                } else {
                    commonMethods.showMessageDialog(context,
                            "To date should be greater than From date");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public class SelectedAdapterProposalList extends RecyclerView.Adapter<SelectedAdapterProposalList.ViewHolderAdapter> implements Filterable {

        private ArrayList<ParseXML.ProposalListValuesModel> lstAdapterList, lstSearch;

        SelectedAdapterProposalList(ArrayList<ParseXML.ProposalListValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {


                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        lstSearch = proposalList;
                    } else {
                        final ArrayList<ParseXML.ProposalListValuesModel> results = new ArrayList<>();
                        for (final ParseXML.ProposalListValuesModel model : proposalList) {
                            if (model.getFULLNAME().toLowerCase().contains(charSequence.toString().toLowerCase())
                                    || model.getCHANNELCODE().toLowerCase().contains(charSequence.toString().toLowerCase())
                                    || model.getPROPOSAL_NO().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                results.add(model);
                            }
                        }

                        lstAdapterList = results;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = lstAdapterList;
                    return filterResults;


                    /*if (lstSearch == null)
                    {
                        lstSearch = lstAdapterList;
                    }*/

                    /*String strSearch = charSequence == null ? "" : charSequence.toString();

                    if (!strSearch.equals("")) {
                        if (lstSearch != null && lstSearch.size() > 0) {
                            for (final ParseXML.ProposalListValuesModel model : lstSearch) {
                                if (model.getFULLNAME().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getCHANNELCODE().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getPROPOSAL_NO().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                    results.add(model);
                                }
                            }
                        }
                        oReturn.values = results;
                    } else {
                        oReturn.values = proposalList;
                    }
                    return oReturn;*/
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    lstAdapterList = (ArrayList<ParseXML.ProposalListValuesModel>) results.values;
                    selectedAdapterProposalList = new SelectedAdapterProposalList(lstAdapterList);
                    recyclerviewProposalList.setAdapter(selectedAdapterProposalList);

                    notifyDataSetChanged();

                    textviewRecordCount.setText("Total : " + lstAdapterList.size());
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
                    R.layout.list_item_proposal_list, parent, false);

            // set the view's size, margins, paddings and layout parameters
            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(ViewHolderAdapter holder, int position) {
            if (strUserType.equalsIgnoreCase("UM") || strUserType.equalsIgnoreCase("Agent")) {
                holder.textviewUserCodeTitle.setText("IA Code");
            } else if (strUserType.equalsIgnoreCase("BDM") || strUserType.equalsIgnoreCase("CIF")) {
                holder.textviewUserCodeTitle.setText("CIF Code");
            }


            holder.textviewUserCode.setText(lstAdapterList.get(position).getCHANNELCODE() == null ? ""
                    : lstAdapterList.get(position).getCHANNELCODE());
            holder.textviewProposalListProposalNumber.setText(lstAdapterList.get(position).getPROPOSAL_NO() == null ? ""
                    : lstAdapterList.get(position).getPROPOSAL_NO());
            holder.textviewProposalListFullName.setText(lstAdapterList.get(position).getFULLNAME() == null ? ""
                    : lstAdapterList.get(position).getFULLNAME());
            holder.textviewProposalListStatus.setText(lstAdapterList.get(position).getSTATUS() == null ? ""
                    : lstAdapterList.get(position).getSTATUS());
        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,
                MenuItem.OnMenuItemClickListener {

            private final TextView textviewProposalListProposalNumber, textviewProposalListFullName,
                    textviewProposalListStatus, textviewUserCode, textviewUserCodeTitle;

            private LinearLayout llProposalListStatus;

            ViewHolderAdapter(View v) {
                super(v);
                textviewProposalListProposalNumber = v.findViewById(R.id.textviewProposalListProposalNumber);
                textviewProposalListFullName = v.findViewById(R.id.textviewProposalListFullName);
                textviewProposalListStatus = v.findViewById(R.id.textviewProposalListStatus);
                textviewUserCode = v.findViewById(R.id.textviewUserCode);

                textviewUserCodeTitle = v.findViewById(R.id.textviewUserCodeTitle);
                llProposalListStatus = v.findViewById(R.id.llProposalListStatus);
                llProposalListStatus.setVisibility(View.GONE);
                if (fromHome.equalsIgnoreCase("MHR")) {
                    v.setOnCreateContextMenuListener(this);
                }
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                if (fromHome.equalsIgnoreCase("MHR")) {
                    menu.setHeaderTitle("Select Action");
                    MenuItem myActionItem = menu.add("MHR");
                    myActionItem.setOnMenuItemClickListener(this);
                } else {
                    return;
                }

            }

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (fromHome.equalsIgnoreCase("MHR")) {
                    String proposalNumber = lstAdapterList.get(getAdapterPosition()).getPROPOSAL_NO();
                    if (item.getTitle() == "MHR") {
                        commonMethods.hideKeyboard(edittextSearch, context);
                        Intent intent = new Intent(context, MHRActivity.class);
                        intent.putExtra("fromHome", "ProposalList");
                        intent.putExtra("ProposalNumber", proposalNumber);
                        startActivity(intent);
                    }
                }
                return true;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (fromHome != null && fromHome.equalsIgnoreCase("Y")) {
            Intent i = new Intent(context, CarouselHomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        } else {
            super.onBackPressed();
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

        if (downloadProposalListAsync != null) {
            downloadProposalListAsync.cancel(true);
        }
        if (service != null) {
            service.cancel(true);
        }
    }
}
