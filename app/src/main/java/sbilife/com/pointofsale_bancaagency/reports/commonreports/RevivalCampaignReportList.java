package sbilife.com.pointofsale_bancaagency.reports.commonreports;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SendSmsAsyncTask;
import sbilife.com.pointofsale_bancaagency.home.dgh.DGHActivity;
import sbilife.com.pointofsale_bancaagency.reports.RenewalCallingRemarksActivity;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;
import sbilife.com.pointofsale_bancaagency.reports.policyservicing.RevivalCampaignDashboardActivity;

public class RevivalCampaignReportList extends AppCompatActivity implements View.OnClickListener, ServiceHits.DownLoadData {
    private ProgressDialog mProgressDialog;
    private final String METHOD_NAME_REVIVAL_CAMPAIGN = "getRevivalCampain_smrt";
    private final String URl = ServiceURL.SERVICE_URL;
    private final String NAMESPACE = "http://tempuri.org/";
    private TextView textViewFromDate;
    private TextView textViewToDate;
    private TextView textviewRecordCount;
    private Spinner spinnerRevivalCampaignType;
    private RecyclerView recyclerviewRevivalCampaign;
    private CommonMethods commonMethods;
    private Context context;
    private int mYear, mMonth, mDay, datecheck = 0;
    private String revivalCampaignType = "";
    private ServiceHits service;
    private ArrayList<RevivalCampaignValuesModel> globalDataList;
    private DownloadRevialCampaignAsync downloadRevialCampaignAsync;
    private SelectedAdapterRevivalCampaign selectedAdapterRevivalCampaign;
    private EditText edittextSearch;
    private String strCIFBDMUserId = "", userType = "", strCIFBDMEmailId, strCIFBDMPassword, strCIFBDMMObileNo;
    private TextView txterrordesc;

    private long revivalPendingCount, revivalDoneCount, revivalPeriodEndCount, revivalPendingLikelyCount,
            revivalPendingHighLikelyCount, revivalPendingModerateCount, revivalPendingUnlikelyCount,
            revivalDoneLikelyCount, revivalDoneHighLikelyCount, revivalDoneModerateCount, revivalDoneUnlikelyCount,
            revivalPeriodEndLikelyCount, revivalPeriodEndHighLikelyCount, revivalPeriodEndModerateCount,
            revivalPeriodEndUnlikelyCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_revival_campaign_report_list);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Revival Campaign");

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        textViewFromDate = findViewById(R.id.textViewFromDate);
        textViewToDate = findViewById(R.id.textViewToDate);
        txterrordesc = findViewById(R.id.txterrordesc);
        spinnerRevivalCampaignType = findViewById(R.id.spinnerRevivalCampaignType);
        Button buttonOkRevivalCampaign = findViewById(R.id.buttonOkRevivalCampaign);
        Button buttonDashboard = findViewById(R.id.buttonDashboard);
        recyclerviewRevivalCampaign = findViewById(R.id.recyclerviewRevivalCampaign);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerviewRevivalCampaign.setLayoutManager(linearLayoutManager);
        globalDataList = new ArrayList<>();
        selectedAdapterRevivalCampaign = new SelectedAdapterRevivalCampaign(globalDataList);
        recyclerviewRevivalCampaign.setAdapter(selectedAdapterRevivalCampaign);
        recyclerviewRevivalCampaign.setItemAnimator(new DefaultItemAnimator());

        textviewRecordCount = findViewById(R.id.textviewRecordCount);
        textviewRecordCount.setVisibility(View.GONE);
        edittextSearch = findViewById(R.id.edittextSearch);

        textViewFromDate.setOnClickListener(this);
        textViewToDate.setOnClickListener(this);
        buttonOkRevivalCampaign.setOnClickListener(this);
        buttonDashboard.setOnClickListener(this);
        setDates();

        List<String> typeList = new ArrayList<>();

        typeList.add("Select Type");
        /*typeList.add("No waiver in Health Requirement");
        typeList.add("No DGH");
        typeList.add("DGH required");*/
        typeList.add("NON-ULIP");
        typeList.add("ULIP");

        // typeList.add("No DGH - All Document Waived");
        //typeList.add("DGH Required - Medical Waived");
        commonMethods.fillSpinnerValue(context, spinnerRevivalCampaignType, typeList);

        //edittextSearch.setVisibility(View.GONE);
        edittextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                selectedAdapterRevivalCampaign.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        Intent intent = getIntent();

        String fromHome = intent.getStringExtra("fromHome");
        userType = commonMethods.GetUserType(context);
        if (fromHome != null && fromHome.equalsIgnoreCase("N")) {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");
            userType = intent.getStringExtra("strUserType");

            try {
                strCIFBDMPassword = commonMethods.getStrAuth();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            getUserDetails();
        }

        revivalPendingCount = -1;
        revivalDoneCount = -1;
        revivalPeriodEndCount = -1;
        revivalPendingLikelyCount = -1;
        revivalPendingHighLikelyCount = -1;
        revivalPendingModerateCount = -1;
        revivalPendingUnlikelyCount = -1;
        revivalDoneLikelyCount = -1;
        revivalDoneHighLikelyCount = -1;
        revivalDoneModerateCount = -1;
        revivalDoneUnlikelyCount = -1;
        revivalPeriodEndLikelyCount = -1;
        revivalPeriodEndHighLikelyCount = -1;
        revivalPeriodEndModerateCount = -1;
        revivalPeriodEndUnlikelyCount = -1;
    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

    }

    private void setDates() {
        textViewFromDate.setText(commonMethods.getPreviousMonthDate());
        textViewToDate.setText(commonMethods.getCurrentMonthDate());
    }

    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {

            mYear = year;
            mMonth = month;
            mDay = day;
            updateDisplay(mYear, mMonth, mDay);
        }
    };

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.textViewFromDate:
                datecheck = 1;
                showDateDialog();
                break;

            case R.id.textViewToDate:
                datecheck = 2;
                showDateDialog();
                break;

            case R.id.buttonOkRevivalCampaign:
                commonMethods.hideKeyboard(edittextSearch, context);
                revivalCampaignType = spinnerRevivalCampaignType.getSelectedItem().toString();
                if (commonMethods.isNetworkConnected(context)) {
                    if (!revivalCampaignType.equalsIgnoreCase("Select Type")) {
                        clearList();
                        service_hits();
                    } else {
                        commonMethods.showMessageDialog(context, "Please Select Type");
                    }
                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }
                break;

            case R.id.buttonDashboard:

                if (globalDataList.size() > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString("revivalPendingCount", revivalPendingCount + "");
                    bundle.putString("revivalDoneCount", revivalDoneCount + "");
                    bundle.putString("revivalPeriodEndCount", revivalPeriodEndCount + "");
                    bundle.putString("revivalPendingLikelyCount", revivalPendingLikelyCount + "");
                    bundle.putString("revivalPendingHighLikelyCount", revivalPendingHighLikelyCount + "");
                    bundle.putString("revivalPendingModerateCount", revivalPendingModerateCount + "");
                    bundle.putString("revivalPendingUnlikelyCount", revivalPendingUnlikelyCount + "");
                    bundle.putString("revivalDoneLikelyCount", revivalDoneLikelyCount + "");
                    bundle.putString("revivalDoneHighLikelyCount", revivalDoneHighLikelyCount + "");
                    bundle.putString("revivalDoneModerateCount", revivalDoneModerateCount + "");
                    bundle.putString("revivalDoneUnlikelyCount", revivalDoneUnlikelyCount + "");
                    bundle.putString("revivalPeriodEndLikelyCount", revivalPeriodEndLikelyCount + "");
                    bundle.putString("revivalPeriodEndHighLikelyCount", revivalPeriodEndHighLikelyCount + "");
                    bundle.putString("revivalPeriodEndModerateCount", revivalPeriodEndModerateCount + "");
                    bundle.putString("revivalPeriodEndUnlikelyCount", revivalPeriodEndUnlikelyCount + "");

                    Intent intent = new Intent(context, RevivalCampaignDashboardActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    commonMethods.showMessageDialog(context, "Please click Ok button to get Revival Campaign.");
                }

                break;
        }
    }

    private void showDateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(
                context,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                mYear, mMonth, mDay);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
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
            textViewFromDate.setText(totaldate);
        } else if (datecheck == 2) {
            textViewToDate.setText(totaldate);
        }
    }

    @Override
    public void downLoadData() {
        downloadRevialCampaignAsync = new DownloadRevialCampaignAsync();
        downloadRevialCampaignAsync.execute();
    }

    class DownloadRevialCampaignAsync extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String strAutoMandateStatusListError = "";
        private int listCount = 0;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txterrordesc.setVisibility(View.GONE);

            mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading Please wait...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);

            mProgressDialog.setButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            if (downloadRevialCampaignAsync != null) {
                                downloadRevialCampaignAsync.cancel(true);
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

                //getRevivalCampain_smrt(string code, string type,string Role,
                // string strEmailId, string strMobileNo, string strAuthKey)
                request = new SoapObject(NAMESPACE, METHOD_NAME_REVIVAL_CAMPAIGN);
                request.addProperty("code", strCIFBDMUserId);//commonMethods.GetUserCode(context));
                request.addProperty("type", revivalCampaignType);
                request.addProperty("Role", userType);
                commonMethods.appendSecurityParams(context, request, "", "");
                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    String SOAP_ACTION_REVIVAL_CAMPAIGN = "http://tempuri.org/" + METHOD_NAME_REVIVAL_CAMPAIGN;
                    androidHttpTranport.call(SOAP_ACTION_REVIVAL_CAMPAIGN, envelope);
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
                            strAutoMandateStatusListError = inputpolicylist;

                            if (inputpolicylist != null) {
                                // for agent policy list
                                List<String> Node = prsObj.parseParentNode(
                                        inputpolicylist, "Table");

                                List<RevivalCampaignValuesModel> nodeData = parseNodeRevivalCampaign(Node);
                                globalDataList.clear();
                                globalDataList.addAll(nodeData);
                                listCount = globalDataList.size();
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
        protected void onProgressUpdate(String... progress) {
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (running) {
                edittextSearch.setVisibility(View.GONE);

                textviewRecordCount.setVisibility(View.VISIBLE);
                txterrordesc.setVisibility(View.VISIBLE);

                if (strAutoMandateStatusListError != null) {
                    edittextSearch.setVisibility(View.VISIBLE);
                    registerForContextMenu(recyclerviewRevivalCampaign);
                    txterrordesc.setText("");
                    textviewRecordCount.setText("Total Record : " + listCount);
                    selectedAdapterRevivalCampaign = new SelectedAdapterRevivalCampaign(globalDataList);
                    recyclerviewRevivalCampaign.setAdapter(selectedAdapterRevivalCampaign);
                    recyclerviewRevivalCampaign.invalidate();
                } else {

                    txterrordesc.setText("No Record Found");
                    textviewRecordCount.setText("Total Record : " + 0);
                    clearList();
                    commonMethods.showMessageDialog(context, "No Record Found");
                }
            } else {
                commonMethods.showMessageDialog(context,
                        "Server Not Responding,Try again..");
            }
        }
    }

    private void service_hits() {
        service = new ServiceHits(context,
                METHOD_NAME_REVIVAL_CAMPAIGN, revivalCampaignType,
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

        if (downloadRevialCampaignAsync != null) {
            downloadRevialCampaignAsync.cancel(true);
        }
        if (service != null) {
            service.cancel(true);
        }
    }

    public class SelectedAdapterRevivalCampaign extends RecyclerView.Adapter<SelectedAdapterRevivalCampaign.ViewHolderAdapter> implements Filterable {

        private ArrayList<RevivalCampaignValuesModel> lstAdapterList;


        SelectedAdapterRevivalCampaign(ArrayList<RevivalCampaignValuesModel> lstAdapterList) {
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
                        final ArrayList<RevivalCampaignValuesModel> results = new ArrayList<>();
                        for (final RevivalCampaignValuesModel model : globalDataList) {
                            if (model.getPOLICYNUMBER().toLowerCase().contains(charSequence.toString().toLowerCase())
                                    || model.getHOLDERNAME().toLowerCase().contains(charSequence.toString().toLowerCase())

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

                    lstAdapterList = (ArrayList<RevivalCampaignValuesModel>) results.values;
                    selectedAdapterRevivalCampaign = new SelectedAdapterRevivalCampaign(lstAdapterList);
                    recyclerviewRevivalCampaign.setAdapter(selectedAdapterRevivalCampaign);

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
                    R.layout.list_item_revival_campaign_list, parent, false);

            // set the view's size, margins, paddings and layout parameters
            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final ViewHolderAdapter holder, final int position) {

            holder.textviewRevivalCampaginPolicyNumber.setText(lstAdapterList.get(position).getPOLICYNUMBER());
            holder.textviewRevivalCampaginName.setText(lstAdapterList.get(position).getHOLDERNAME());
            holder.textviewMobileNumber.setText(lstAdapterList.get(position).getCONTACT_MOBILE());

            holder.tvRevivalPeriodEndDate.setText(lstAdapterList.get(position).getREVIVAL_PERIOD_ENDDATE());
            holder.tvDGHRequirement.setText(lstAdapterList.get(position).getDGH_REQUIREMENT());
            holder.textviewPolicyCurrentStatus.setText(lstAdapterList.get(position).getPOLICYCURRENTSTATUS());
            holder.textviewRevivalDate.setText(lstAdapterList.get(position).getREVIVALDATE());
            holder.textviewFUP.setText(lstAdapterList.get(position).getFUP());
            holder.textviewDOC.setText(lstAdapterList.get(position).getDOC());
            holder.textviewRAG.setText(lstAdapterList.get(position).getRAG());
            holder.tvFirstExtraCase.setText(lstAdapterList.get(position).getEXTRA_CASE());


            String mobileNumber = holder.textviewMobileNumber.getText().toString();
            if (TextUtils.isEmpty(mobileNumber)) {
                holder.imgcontact_cust_r.setVisibility(View.GONE);
            } else {
                holder.imgcontact_cust_r.setVisibility(View.GONE);
            }
            holder.imgcontact_cust_r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mobileNumber = holder.textviewMobileNumber.getText().toString();

                    if (!TextUtils.isEmpty(mobileNumber)) {
                        commonMethods.callMobileNumber(mobileNumber, context);
                    }
                }
            });

            holder.buttonCallMobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mobileNumber = holder.textviewMobileNumber.getText().toString();

                    if (!TextUtils.isEmpty(mobileNumber)) {
                        commonMethods.callMobileNumber(mobileNumber, context);
                    }
                }
            });

            holder.textviewMobileNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mobileNumber = holder.textviewMobileNumber.getText().toString();
                    if (!TextUtils.isEmpty(mobileNumber)) {
                        commonMethods.callMobileNumber(mobileNumber, context);
                    }
                }
            });

            holder.buttonCRM.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    commonMethods.hideKeyboard(edittextSearch, context);
                    int index = holder.getAdapterPosition();
                    showDispositionAlert(lstAdapterList.get(index));
                }
            });

            holder.buttonSendSMS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    commonMethods.hideKeyboard(edittextSearch, context);
                    String mobileNumber = holder.textviewMobileNumber.getText().toString();
                    int index = holder.getAdapterPosition();
                    if (!TextUtils.isEmpty(mobileNumber)) {
                        confirmDialog(mobileNumber, lstAdapterList.get(index).getPOLICYNUMBER());
                    }
                }
            });

        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,
                MenuItem.OnMenuItemClickListener {

            private final TextView textviewRevivalCampaginPolicyNumber, textviewRevivalCampaginName,
                    textviewMobileNumber, tvRevivalPeriodEndDate, tvDGHRequirement, textviewRAG,
                    textviewRevivalDate, textviewFUP, textviewPolicyCurrentStatus, textviewDOC,
                    tvFirstExtraCase;
            private final ImageView imgcontact_cust_r;
            private final Button buttonCRM, buttonCallMobile, buttonSendSMS;

            ViewHolderAdapter(View v) {
                super(v);
                textviewRevivalCampaginPolicyNumber = v.findViewById(R.id.textviewRevivalCampaginPolicyNumber);
                textviewRevivalCampaginName = v.findViewById(R.id.textviewRevivalCampaginName);
                textviewMobileNumber = v.findViewById(R.id.textviewMobileNumber);
                tvRevivalPeriodEndDate = v.findViewById(R.id.tvRevivalPeriodEndDate);
                tvDGHRequirement = v.findViewById(R.id.tvDGHRequirement);
                textviewPolicyCurrentStatus = v.findViewById(R.id.textviewPolicyCurrentStatus);
                textviewRevivalDate = v.findViewById(R.id.textviewRevivalDate);
                textviewFUP = v.findViewById(R.id.textviewFUP);
                textviewDOC = v.findViewById(R.id.textviewDOC);
                textviewRAG = v.findViewById(R.id.textviewRAG);
                tvFirstExtraCase = v.findViewById(R.id.tvFirstExtraCase);

                imgcontact_cust_r = v.findViewById(R.id.imgcontact_cust_r);
                buttonCRM = v.findViewById(R.id.buttonCRM);
                buttonCallMobile = v.findViewById(R.id.buttonCallMobile);
                buttonSendSMS = v.findViewById(R.id.buttonSendSMS);
                v.setOnCreateContextMenuListener(this);

            }

            /*@Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("Select Action");
                menu.add(0, v.getId(), 0, "Revival Quotation");//groupId, itemId, order, title
            }*/

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("Select Action");
                MenuItem myActionItem = menu.add(Menu.NONE, 1, 1, "Revival Quotation");
                myActionItem.setOnMenuItemClickListener(this);

               /* MenuItem DGHMenuItem = menu.add(Menu.NONE, 2, 2, "DGH");
                DGHMenuItem.setOnMenuItemClickListener(this);*/
            }

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Menu Item Clicked!
                System.out.println("item = " + item.getTitle() + " info.position:" + getAdapterPosition());
                String strPolicyNo = lstAdapterList.get(getAdapterPosition()).getPOLICYNUMBER();
                if (item.getTitle().toString().equalsIgnoreCase("Revival Quotation")) {
                    String mobileNumber = lstAdapterList.get(getAdapterPosition()).getCONTACT_MOBILE();
                    mobileNumber = mobileNumber == null ? "" : mobileNumber;

                    String emailId = "";//lstAdapterList.get(getAdapterPosition()).getEMAIL();
                    emailId = emailId == null ? "" : emailId;
                    System.out.println("strPolicyNo = " + strPolicyNo);
                    Intent intent = new Intent(context, RevivalQuotationActivity.class);
                    intent.putExtra("policyNumber", strPolicyNo);
                    intent.putExtra("mobileNumber", mobileNumber);
                    intent.putExtra("emailId", emailId);
                    startActivity(intent);
                } else if (item.getTitle().toString().equalsIgnoreCase("DGH")) {
                    Intent intent = new Intent(context, DGHActivity.class);
                    intent.putExtra("policyNumber", strPolicyNo);
                    startActivity(intent);
                }
                return true;
            }
        }
    }

    private void confirmDialog(final String mobileNumber, String policyNumber) {


        final String message = "Dear Customer, Renew your SBI Life Policy no " + policyNumber +
                " by clicking Https://sbi.life/mYr3q and enjoy your policy benefits.\n" +
                "Please ignore if already paid. In case of any query Call 18002679090 or Email us at info@sbilife.co.in\n";
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_yes_no);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button bt_yes = dialog.findViewById(R.id.bt_yes);
        Button bt_no = dialog.findViewById(R.id.bt_no);
        TextView tv_title = dialog.findViewById(R.id.tv_title);
        tv_title.setTextIsSelectable(true);
        tv_title.setText("Send Revival Bitly.");

        bt_yes.setText("Send");

        bt_no.setText("Cancel");
        bt_yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //mobileNumber
                SendSmsAsyncTask sendSmsAsyncTask = new SendSmsAsyncTask(context, mobileNumber, message);
                sendSmsAsyncTask.execute("");
            }
        });
        bt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void clearList() {
        revivalPendingCount = -1;
        revivalDoneCount = -1;
        revivalPeriodEndCount = -1;
        revivalPendingLikelyCount = -1;
        revivalPendingHighLikelyCount = -1;
        revivalPendingModerateCount = -1;
        revivalPendingUnlikelyCount = -1;
        revivalDoneLikelyCount = -1;
        revivalDoneHighLikelyCount = -1;
        revivalDoneModerateCount = -1;
        revivalDoneUnlikelyCount = -1;
        revivalPeriodEndLikelyCount = -1;
        revivalPeriodEndHighLikelyCount = -1;
        revivalPeriodEndModerateCount = -1;
        revivalPeriodEndUnlikelyCount = -1;
        txterrordesc.setText("");
        textviewRecordCount.setText("");
        textviewRecordCount.setVisibility(View.GONE);
        edittextSearch.setText("");
        edittextSearch.setVisibility(View.GONE);
        if (globalDataList != null && selectedAdapterRevivalCampaign != null) {
            globalDataList.clear();
            selectedAdapterRevivalCampaign = new SelectedAdapterRevivalCampaign(globalDataList);
            recyclerviewRevivalCampaign.setAdapter(selectedAdapterRevivalCampaign);
            recyclerviewRevivalCampaign.invalidate();

        }
    }

    public List<RevivalCampaignValuesModel> parseNodeRevivalCampaign(List<String> lsNode) {
        List<RevivalCampaignValuesModel> lsData = new ArrayList<>();

        String POLICYNUMBER;
        String HOLDERNAME;
        String CONTACT_MOBILE;
        String REVIVAL_PERIOD_ENDDATE;
        String DGH_REQUIREMENT;
        String RAG;
        String REVIVALDATE;
        String POLICYCURRENTSTATUS;
        String DOC;
        String FUP;
        String EXTRA_CASE;
        ParseXML parseXML = new ParseXML();
        revivalPendingCount = 0;
        revivalDoneCount = 0;
        revivalPeriodEndCount = 0;
        revivalPendingLikelyCount = 0;
        revivalPendingHighLikelyCount = 0;
        revivalPendingModerateCount = 0;
        revivalPendingUnlikelyCount = 0;
        revivalDoneLikelyCount = 0;
        revivalDoneHighLikelyCount = 0;
        revivalDoneModerateCount = 0;
        revivalDoneUnlikelyCount = 0;
        revivalPeriodEndLikelyCount = 0;
        revivalPeriodEndHighLikelyCount = 0;
        revivalPeriodEndModerateCount = 0;
        revivalPeriodEndUnlikelyCount = 0;
        for (String Node : lsNode) {

            POLICYNUMBER = parseXML.parseXmlTag(Node, "POLICYNUMBER");
            POLICYNUMBER = POLICYNUMBER == null ? "" : POLICYNUMBER;

            HOLDERNAME = parseXML.parseXmlTag(Node, "HOLDERNAME");
            HOLDERNAME = HOLDERNAME == null ? "" : HOLDERNAME;

            CONTACT_MOBILE = parseXML.parseXmlTag(Node, "CONTACT_MOBILE");
            CONTACT_MOBILE = CONTACT_MOBILE == null ? "" : CONTACT_MOBILE;

            REVIVAL_PERIOD_ENDDATE = parseXML.parseXmlTag(Node, "REVIVAL_PERIOD_ENDDATE");
            REVIVAL_PERIOD_ENDDATE = REVIVAL_PERIOD_ENDDATE == null ? "" : REVIVAL_PERIOD_ENDDATE;

            DGH_REQUIREMENT = parseXML.parseXmlTag(Node, "DGH_REQUIREMENT");
            DGH_REQUIREMENT = DGH_REQUIREMENT == null ? "" : DGH_REQUIREMENT;

            RAG = parseXML.parseXmlTag(Node, "RAG");
            RAG = RAG == null ? "" : RAG;

            REVIVALDATE = parseXML.parseXmlTag(Node, "REVIVALDATE");
            REVIVALDATE = REVIVALDATE == null ? "" : REVIVALDATE;

            POLICYCURRENTSTATUS = parseXML.parseXmlTag(Node, "POLICYCURRENTSTATUS");
            POLICYCURRENTSTATUS = POLICYCURRENTSTATUS == null ? "" : POLICYCURRENTSTATUS;

            DOC = parseXML.parseXmlTag(Node, "DOC");
            DOC = DOC == null ? "" : DOC;

            FUP = parseXML.parseXmlTag(Node, "FUP");
            FUP = FUP == null ? "" : FUP;

            EXTRA_CASE = parseXML.parseXmlTag(Node, "EXTRA_CASE");
            EXTRA_CASE = EXTRA_CASE == null ? "" : EXTRA_CASE;
            EXTRA_CASE = EXTRA_CASE.replace("&gt;",">");
            EXTRA_CASE = EXTRA_CASE.replace("&lt;","<");

            if (!TextUtils.isEmpty(REVIVAL_PERIOD_ENDDATE) && !POLICYCURRENTSTATUS.equalsIgnoreCase("inforce")) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                    Date revivalEndDate = sdf.parse(REVIVAL_PERIOD_ENDDATE);
                    if (new Date().before(revivalEndDate)) {
                        revivalPendingCount++;
                        if (RAG.equalsIgnoreCase("likely")) {
                            revivalPendingLikelyCount++;
                        } else if (RAG.equalsIgnoreCase("high likely")) {
                            revivalPendingHighLikelyCount++;
                        } else if (RAG.equalsIgnoreCase("Moderate")) {
                            revivalPendingModerateCount++;
                        } else if (RAG.equalsIgnoreCase("unlikely")) {
                            revivalPendingUnlikelyCount++;
                        }
                    } else if (new Date().after(revivalEndDate)) {
                        revivalPeriodEndCount++;
                        if (RAG.equalsIgnoreCase("likely")) {
                            revivalPeriodEndLikelyCount++;
                        } else if (RAG.equalsIgnoreCase("high likely")) {
                            revivalPeriodEndHighLikelyCount++;
                        } else if (RAG.equalsIgnoreCase("Moderate")) {
                            revivalPeriodEndModerateCount++;
                        } else if (RAG.equalsIgnoreCase("unlikely")) {
                            revivalPeriodEndUnlikelyCount++;
                        }
                    }
                } catch (Exception e) {

                }
            } else {
                revivalDoneCount++;
                if (RAG.equalsIgnoreCase("likely")) {
                    revivalDoneLikelyCount++;
                } else if (RAG.equalsIgnoreCase("high likely")) {
                    revivalDoneHighLikelyCount++;
                } else if (RAG.equalsIgnoreCase("Moderate")) {
                    revivalDoneModerateCount++;
                } else if (RAG.equalsIgnoreCase("unlikely")) {
                    revivalDoneUnlikelyCount++;
                }
            }
            RevivalCampaignValuesModel nodeVal = new RevivalCampaignValuesModel(POLICYNUMBER, HOLDERNAME, CONTACT_MOBILE,
                    REVIVAL_PERIOD_ENDDATE, DGH_REQUIREMENT, RAG, REVIVALDATE, POLICYCURRENTSTATUS, DOC, FUP, EXTRA_CASE);
            lsData.add(nodeVal);
        }
        return lsData;
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
        private final String EXTRA_CASE;

        public RevivalCampaignValuesModel(String POLICYNUMBER, String HOLDERNAME, String CONTACT_MOBILE,
                                          String REVIVAL_PERIOD_ENDDATE, String DGH_REQUIREMENT, String RAG, String REVIVALDATE,
                                          String POLICYCURRENTSTATUS, String DOC, String FUP, String EXTRA_CASE) {
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
            this.EXTRA_CASE = EXTRA_CASE;
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

        public String getEXTRA_CASE() {
            return EXTRA_CASE;
        }
    }


    private void showDispositionAlert(final RevivalCampaignValuesModel lstAdapterList) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);


        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dispositon_alert_dialog, null);
        alertDialogBuilder.setView(dialogView);
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();

        final TextView updateRemarkBtn, viewRemarkBtn;
        final LinearLayout updateRemarkLayout, viewRemarkLayout;
        final Button btn_submit_disposition_remark, sbiLifeRemarkBtn, callCenterRemarkBtn, salesRemarkBtn;

        final Spinner dispotionSpinner = dialogView.findViewById(R.id.spinner_disposition);
        final Spinner subdispotionSpinner = dialogView.findViewById(R.id.spinner_subdisposition);
        final EditText dispositionRemarkText = dialogView.findViewById(R.id.edt_disposition_remark);
        btn_submit_disposition_remark = dialogView.findViewById(R.id.btn_submit_disposition_remark);
        sbiLifeRemarkBtn = dialogView.findViewById(R.id.btn_sbi_life_remark);
        callCenterRemarkBtn = dialogView.findViewById(R.id.btn_call_center_remark);
        salesRemarkBtn = dialogView.findViewById(R.id.btn_sales_remark);

        updateRemarkBtn = dialogView.findViewById(R.id.btn_update_remark);
        updateRemarkBtn.setSelected(true);
        viewRemarkBtn = dialogView.findViewById(R.id.btn_view_remark);
        viewRemarkBtn.setSelected(false);
        updateRemarkLayout = dialogView.findViewById(R.id.ll_update_remark_parent);
        viewRemarkLayout = dialogView.findViewById(R.id.ll_view_remark_parent);

        final ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(context, R.layout.spinner_text, getResources().getStringArray(R.array.disposition_array));
        langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        dispotionSpinner.setAdapter(langAdapter);

        dispotionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] subDispositionArray = null;
                switch (i) {
                    case 0:
                        dispositionRemarkText.setVisibility(View.GONE);
                        btn_submit_disposition_remark.setVisibility(View.GONE);
                        subDispositionArray = new String[]{""};
                        break;
                    case 1:
                        subDispositionArray = new String[]{"Select Sub Disposition", "Callback_Call To Speak Later on for Retention", "Left Message",
                                "Received by Customer Representative. Asked for call back", "Out Of Station"};
                        break;
                    case 2:
                        subDispositionArray = new String[]{"Select Sub Disposition", "Already Paid To Branch", "Drop Box", "Online", "MP Online",
                                "Paid To Advisor/ CIF before 15 days", "Paid To Advisor/ CIF within 15 to 30 days"};
                        break;
                    case 3:
                        subDispositionArray = new String[]{"Select Sub Disposition", "Other Language / language barier"};
                        break;
                    case 4:
                        subDispositionArray = new String[]{"Select Sub Disposition", "Advisor Owned Policy", "Customer Expired",
                                "Do Not Disturb", "Employee Owned Policy", "Applied for Surrender the policy"};
                        break;
                    case 5:
                        subDispositionArray = new String[]{"Select Sub Disposition", "Intrested but financial problem", "Not Intested due to financial problem"};
                        break;
                    case 6:
                        subDispositionArray = new String[]{"Select Sub Disposition", "Address Changes", "Earlier complaint raised_No solution received",
                                "Mode Change", "Other", "Policy Document Not Recd", "Fund value / bonus statement"};
                        break;
                    case 7:
                        subDispositionArray = new String[]{"Select Sub Disposition", "Requesting for Pickup", "Will pay in Grace period",
                                "Promise To Pay", "Will Pay Later", "Interested to pay online"};
                        break;
                    case 8:
                        subDispositionArray = new String[]{"Select Sub Disposition", "features & Benefits notexplained", "Misselling false promises made",
                                "Misselling force selling", "Misselling high allocation charges", "Misselling wrong product sold",
                                "Sold As Single Premium or LPPT", "Policy Against KCC", "Poilcy Against Loan"};
                        break;
                    case 9:
                        subDispositionArray = new String[]{"Select Sub Disposition", "Beep Tone", "DIALER_SIT_TONE", "Number Does Not Exist",
                                "Customers Has Relocated (Abroad)", "Out Of Service"};
                        break;
                    case 10:
                        subDispositionArray = new String[]{"Select Sub Disposition", "Call Disconnected", "Engage", "Not Reachable", "Ringing",
                                "Switched Off", "Customer Busy"};
                        break;
                    case 11:
                        subDispositionArray = new String[]{"Select Sub Disposition", "Completed  Locking Period", "Customer Has Bought Competitor Policy",
                                "Low Return", "Not Ready For Stating Reason", "Refuse To Pay", "Taken Another Policy With Sbi Life",
                                "Renewal premium reminder Notice not received", "Not satisfied with advisor services", "Overall services of the company"};
                        break;
                    case 12:
                        subDispositionArray = new String[]{"Select Sub Disposition", "IA Number", "Not Related Patry"};
                        break;
                    case 13:
                        subDispositionArray = new String[]{"Select Sub Disposition", "Call Back", "Already Paid", "Language barier", "Do Not Call",
                                "Financial Problem", "Customer query / issue", "Interested", "Misselling", "Permanent Non Contactable",
                                "Temporary Non Contactable", "Not Interested", "Wrong number"};
                        break;
                    case 14:
                        subDispositionArray = new String[]{"Select Sub Disposition", "Contact Number Sourced", "Forward for structured follow up"};
                        break;
                    case 15:
                        subDispositionArray = new String[]{"Select Sub Disposition", "All Efforts Done"};
                        break;
                    case 16:
                        subDispositionArray = new String[]{"Select Sub Disposition", "New"};
                        break;

                }

                ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(context, R.layout.spinner_text, subDispositionArray);
                langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                subdispotionSpinner.setAdapter(langAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        subdispotionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String subDispositionString = subdispotionSpinner.getSelectedItem().toString();
                if (!(subDispositionString.equalsIgnoreCase("")) &&
                        !(subDispositionString.equalsIgnoreCase("Select Sub Disposition"))) {
                    dispositionRemarkText.setVisibility(View.VISIBLE);
                    btn_submit_disposition_remark.setVisibility(View.VISIBLE);
                } else {
                    dispositionRemarkText.setVisibility(View.GONE);
                    btn_submit_disposition_remark.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                dispositionRemarkText.setVisibility(View.GONE);
                btn_submit_disposition_remark.setVisibility(View.GONE);
            }
        });

        updateRemarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRemarkBtn.setSelected(true);
                viewRemarkBtn.setSelected(false);

                viewRemarkLayout.setVisibility(View.GONE);
                updateRemarkLayout.setVisibility(View.VISIBLE);
            }
        });
        viewRemarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewRemarkBtn.setSelected(true);
                updateRemarkBtn.setSelected(false);
                commonMethods.hideKeyboard(view, context);
                updateRemarkLayout.setVisibility(View.GONE);
                viewRemarkLayout.setVisibility(View.VISIBLE);
                dispositionRemarkText.setText("");
                dispotionSpinner.setSelection(0);
            }
        });
        callCenterRemarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                String objectType = lstAdapterList.getPOLICYCURRENTSTATUS();
                //String DueDate = lstAdapterList.getREVIVALDATE();
                String DueDate = lstAdapterList.getFUP();
                String PAY_EX1_74 = lstAdapterList.getPOLICYNUMBER();
                String StatusCodeID = "";


                Intent intent = new Intent(context, RenewalCallingRemarksActivity.class);
                intent.putExtra("objectType", objectType);
                intent.putExtra("DueDate", DueDate);
                intent.putExtra("PAY_EX1_74", PAY_EX1_74);
                intent.putExtra("StatusCodeID", StatusCodeID);
                startActivity(intent);
            }
        });
        sbiLifeRemarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //alert.dismiss();
                GetRemarksAsyncTask getRemarksAsyncTask = new GetRemarksAsyncTask(lstAdapterList.getPOLICYNUMBER());
                getRemarksAsyncTask.execute();
            }
        });
        salesRemarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //alert.dismiss();
                GetRemarksAsyncTask getRemarksAsyncTask = new GetRemarksAsyncTask(lstAdapterList.getPOLICYNUMBER());
                getRemarksAsyncTask.execute();
            }
        });
        btn_submit_disposition_remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String disposition = dispotionSpinner.getSelectedItem().toString();
                String subdisposition = subdispotionSpinner.getSelectedItem().toString();
                String dispositionRemark = dispositionRemarkText.getText().toString();

                if (disposition.equalsIgnoreCase("Select Disposition")) {
                    Toast.makeText(context, "Please select disposition option", Toast.LENGTH_LONG).show();
                } else if (subdisposition.equalsIgnoreCase("Select Sub Disposition")) {
                    Toast.makeText(context, "Please select sub disposition option", Toast.LENGTH_LONG).show();
                } else if (dispositionRemark == null || dispositionRemark.equalsIgnoreCase("")) {
                    Toast.makeText(context, "Please enter disposition remark", Toast.LENGTH_LONG).show();
                } else {
                    //saveCallingRemarks_smrt
                    alert.dismiss();
                    SubmitDispositionRemarkAsync submitDispositionRemarkAsync = new SubmitDispositionRemarkAsync(lstAdapterList.getPOLICYNUMBER(), disposition, subdisposition, dispositionRemark);
                    submitDispositionRemarkAsync.execute();
                }
            }
        });

    }

    class SubmitDispositionRemarkAsync extends AsyncTask<String, Void, String> {

        private volatile boolean running = true;
        private final String policyNo;
        private final String status;
        private final String substatus;
        private final String remarks;
        private String output;
        //Context context;
        ProgressDialog mProgressDialog;

        SubmitDispositionRemarkAsync(String policyNo, String status, String substatus, String remarks) {
            this.policyNo = policyNo;
            this.status = status;
            this.substatus = substatus;
            this.remarks = remarks;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading. Please wait...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#000000'><b>" + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setMax(100);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... data) {
            try {
                running = true;


                String METHOD_NAME_DISPOSITION_REMARK = "saveCallingRemarks_smrt";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_DISPOSITION_REMARK);

                request.addProperty("policyno", policyNo);
                request.addProperty("status", status);
                request.addProperty("substatus", substatus);
                request.addProperty("remarks", remarks);
                request.addProperty("empid", strCIFBDMUserId);
                request.addProperty("strEmailId", "a@g.com");
                request.addProperty("strMobileNo", "0000000000");
                request.addProperty("strAuthKey", commonMethods.getStrAuth());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION_DISPOSITION_REMARK = "http://tempuri.org/saveCallingRemarks_smrt";
                androidHttpTranport.call(SOAP_ACTION_DISPOSITION_REMARK, envelope);

                SoapPrimitive sa;
                sa = (SoapPrimitive) envelope.getResponse();

                output = sa.toString();
            } catch (Exception e) {
                running = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (running) {
                if (output.equalsIgnoreCase("1")) {
                    commonMethods.showMessageDialog(context, "Remarks updated");
                } else if (output.equalsIgnoreCase("2")) {
                    commonMethods.showMessageDialog(context, "You are not authorized user.");
                } else {
                    commonMethods.showMessageDialog(context, "Remarks updation failed. Please try again later");
                }
            } else {
                commonMethods.showMessageDialog(context, "Server not responding..");
            }
        }

    }

    class GetRemarksAsyncTask extends AsyncTask<String, Void, String> {

        private volatile boolean running = true;
        private final String policyNo;
        private ArrayList<ParseXML.RenewalRemark> renewalRemarkArrayList;
        //Context context;
        ProgressDialog mProgressDialog;

        GetRemarksAsyncTask(String policyNo) {
            this.policyNo = policyNo;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading. Please wait...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setMax(100);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... data) {
            try {
                running = true;


                String METHOD_NAME_GET_REMARK = "getCallingRemarks_smrt";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_GET_REMARK);

                request.addProperty("policyno", policyNo);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", commonMethods.getStrAuth());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION_DISPOSITION_REMARK = "http://tempuri.org/" + METHOD_NAME_GET_REMARK;
                androidHttpTranport.call(SOAP_ACTION_DISPOSITION_REMARK, envelope);

                SoapPrimitive sa;
                sa = (SoapPrimitive) envelope.getResponse();

                String output = sa.toString();
                //Log.d(TAG, "doInBackground:1 "+output);
                //ParseXML prsObj = new ParseXML();
                if (!output.contentEquals("<NewDataSet /> ")) {
                            /*<NewDataSet>
                              <QueryResults>
                                <PAY_EX1_91>Move to Call Centre</PAY_EX1_91>
                                <PAY_EX1_96>Forward for structured follow up</PAY_EX1_96>
                                <HTMLTEXT_280>LastModified By: 1point1 LastModified On: 02-07-2018 7:51:24 PM&lt;br /&gt;
                                                    Customer contact no is updated&lt;br /&gt;</HTMLTEXT_280>
                                <PAY_EX1_95>AMBER</PAY_EX1_95>
                                <ROWNUMBER>1</ROWNUMBER>
                                <Key>45693</Key>
                              </QueryResults>
                            </NewDataSet>*/
                    //SoapPrimitive sa = null;
                    try {
                        // sa = (SoapPrimitive) envelope.getResponse();
                        String inputpolicylist = output;

                        if (!inputpolicylist.equalsIgnoreCase("<NewDataSet /> ")) {

                            ParseXML parseXML = new ParseXML();
                            String DataResultXML = parseXML.parseXmlTag(
                                    inputpolicylist, "NewDataSet");
                            //DataResultXML = escapeXml(DataResultXML);
                            List<String> Node = parseXML.parseParentNode(
                                    DataResultXML, "Table");
                            renewalRemarkArrayList = parseXML
                                    .parseRenewalRemark(Node);

                        }

                    } catch (Exception e) {

                        mProgressDialog.dismiss();
                        running = false;
                    }
                }

            } catch (Exception e) {
                running = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (running) {
                if (renewalRemarkArrayList != null && renewalRemarkArrayList.size() > 0) {
                    displayRenewalRemarks(renewalRemarkArrayList);

                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_RECORD_FOUND);
                }
            } else {
                commonMethods.showMessageDialog(context, commonMethods.NO_RECORD_FOUND);
                //commonMethods.showMessageDialog(context, "Server not responding..");
            }
        }

    }

    private void displayRenewalRemarks(ArrayList<ParseXML.RenewalRemark> renewalRemarkArrayList) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);


        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.remark_alert_dialog, null);
        alertDialogBuilder.setView(dialogView);
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();

        RecyclerView remarkRecyclerView = dialogView.findViewById(R.id.remark_list_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        remarkRecyclerView.setLayoutManager(linearLayoutManager);

        RenewalRemarkAdapter renewalRemarkAdapter = new RenewalRemarkAdapter(renewalRemarkArrayList);
        remarkRecyclerView.setAdapter(renewalRemarkAdapter);


    }

    public class RenewalRemarkAdapter extends RecyclerView.Adapter<RenewalRemarkAdapter.ViewHolderAdapter> {

        private final ArrayList<ParseXML.RenewalRemark> lstAdapterList;

        RenewalRemarkAdapter(ArrayList<ParseXML.RenewalRemark> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }


        @Override
        public int getItemCount() {
            return lstAdapterList.size();
        }

        @Override
        public RenewalRemarkAdapter.ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.remark_item_layout, parent, false);

            return new RenewalRemarkAdapter.ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final RenewalRemarkAdapter.ViewHolderAdapter holder, int position) {
            ParseXML.RenewalRemark renewalRemark = lstAdapterList.get(position);

            holder.txtDispositionRemark.setText(renewalRemark.getSTATUS());
            holder.txtSubDispositionRemark.setText(renewalRemark.getSUBSTATUS());
            holder.txtRemark.setText(renewalRemark.getREMARKS());
            holder.txtCreatedDate.setText(renewalRemark.getCREATED_DATE());
        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder {


            private final TextView txtDispositionRemark, txtSubDispositionRemark, txtRemark, txtCreatedDate;

            ViewHolderAdapter(View v) {
                super(v);
                txtDispositionRemark = v.findViewById(R.id.txt_disposition_remark);
                txtSubDispositionRemark = v.findViewById(R.id.txt_subdisposition_remark);
                txtRemark = v.findViewById(R.id.txt_remark);
                txtCreatedDate = v.findViewById(R.id.txt_remark_created_date);
            }

        }

    }
}


