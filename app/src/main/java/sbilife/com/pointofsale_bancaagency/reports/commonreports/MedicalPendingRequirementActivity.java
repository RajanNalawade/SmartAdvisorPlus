package sbilife.com.pointofsale_bancaagency.reports.commonreports;

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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
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
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.home.CarouselHomeActivity;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class MedicalPendingRequirementActivity extends AppCompatActivity implements View.OnClickListener,
        ServiceHits.DownLoadData {

    // private ListView listviewAutoMandateStatus;
    private EditText editextSearchMedicalPendingRequirement;

    private ProgressDialog mProgressDialog;

    private final String SOAP_ACTION_MEDICAL_PENDING_REQUIREMENT = "http://tempuri.org/getMedReq_smrt";
    private final String METHOD_NAME_MEDICAL_PENDING_REQUIREMENT = "getMedReq_smrt";

    private final String NAMESPACE = "http://tempuri.org/";
    private final String URl = ServiceURL.SERVICE_URL;

    private Context context;
    private CommonMethods commonMethods;

    private DownloadPendingRequirementListAsync downloadPendingRequirementListAsync;
    private ServiceHits service;

    private ArrayList<ParseXML.MedicalPendingRequirementValuesModel> medicalPendingRequirementList;
    private SelectedAdapterMedicalPendingRequirement selectedAdapterMedicalPendingRequirement;
    private TextView textviewRecordCount;
    private RecyclerView recyclerviewMedicalPendingRequirement;
    private String strDateFlag = "", strCIFBDMUserId = "", strCIFBDMEmailId = "",
            strCIFBDMPassword = "", strCIFBDMMObileNo = "", strFromDate = "", strToDate = "";
    private String fromHome, strUserType;
    private final int DATE_DIALOG_ID = 1;
    private TextView txtFromDate, txtToDate;
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_medical_pending_requirement);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Pending Requirements - Medical");

        // listviewAutoMandateStatus = (ListView)findViewById(R.id.listviewAutoMandateStatus);
        Button btnDocUploaNonMedicalOK = findViewById(R.id.btnDocUploaNonMedicalOK);
        editextSearchMedicalPendingRequirement = findViewById(R.id.editextSearchMedicalPendingRequirement);
        textviewRecordCount = findViewById(R.id.textviewRecordCount);

        txtFromDate = findViewById(R.id.txtFromDate);
        txtToDate = findViewById(R.id.txtToDate);
        txtFromDate.setOnClickListener(this);
        txtToDate.setOnClickListener(this);

        recyclerviewMedicalPendingRequirement = findViewById(R.id.recyclerviewMedicalPendingRequirement);
        //lstReq.setHasFixedSize(true);
        // set a LinearLayoutManager with default horizontal orientation and false value for reverseLayout to show the items from start to end
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        // set LayoutManager to RecyclerView
        recyclerviewMedicalPendingRequirement.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        medicalPendingRequirementList = new ArrayList<>();
        selectedAdapterMedicalPendingRequirement = new SelectedAdapterMedicalPendingRequirement(medicalPendingRequirementList);
        recyclerviewMedicalPendingRequirement.setAdapter(selectedAdapterMedicalPendingRequirement);
        recyclerviewMedicalPendingRequirement.setItemAnimator(new DefaultItemAnimator());

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

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
        setDates();
        btnDocUploaNonMedicalOK.setOnClickListener(this);
        editextSearchMedicalPendingRequirement.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                selectedAdapterMedicalPendingRequirement.getFilter().filter(s);
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

            case R.id.btnDocUploaNonMedicalOK:

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
                        editextSearchMedicalPendingRequirement.setVisibility(View.GONE);
                        textviewRecordCount.setVisibility(View.GONE);

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
                break;
            default:
                break;
        }
    }

    private void clearList() {
        if (medicalPendingRequirementList != null && selectedAdapterMedicalPendingRequirement != null) {
            medicalPendingRequirementList.clear();
            selectedAdapterMedicalPendingRequirement = new SelectedAdapterMedicalPendingRequirement(medicalPendingRequirementList);
            recyclerviewMedicalPendingRequirement.setAdapter(selectedAdapterMedicalPendingRequirement);
            recyclerviewMedicalPendingRequirement.invalidate();
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
    public void downLoadData() {
        downloadPendingRequirementListAsync = new DownloadPendingRequirementListAsync();
        downloadPendingRequirementListAsync.execute();
    }

    class DownloadPendingRequirementListAsync extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String strAutoMandateStatusListError = "";
        private int lstMedicalPendingRequirement = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textviewRecordCount = findViewById(R.id.textviewRecordCount);
            mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading Please wait...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);

            mProgressDialog.setButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            if (downloadPendingRequirementListAsync != null) {
                                downloadPendingRequirementListAsync.cancel(true);
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
                request = new SoapObject(NAMESPACE, METHOD_NAME_MEDICAL_PENDING_REQUIREMENT);
                request.addProperty("strCode", strCIFBDMUserId);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());
                String CHANNEL_PROPOSAL_TRACKER_TYPE = "Pending for Requirement - Medical";
                request.addProperty("strType", CHANNEL_PROPOSAL_TRACKER_TYPE);
                request.addProperty("strFromDate", strFromDate);
                request.addProperty("strToDate", strToDate);
                commonMethods.TLSv12Enable();

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();


                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    androidHttpTranport.call(
                            SOAP_ACTION_MEDICAL_PENDING_REQUIREMENT, envelope);
                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("<NewDataSet />")) {
                        System.out.println("response:" + response.toString());
                        try {
                            ParseXML prsObj = new ParseXML();
                            String inputpolicylist = response.toString();
                            inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "PolicyDetails");

                            if (inputpolicylist != null) {
                                // for agent policy list
                                List<String> Node = prsObj.parseParentNode(inputpolicylist, "Table");
                                List<ParseXML.MedicalPendingRequirementValuesModel> nodeData = prsObj
                                        .parseNodeMedicalPendingRequirement(Node);
                                medicalPendingRequirementList.clear();
                                medicalPendingRequirementList.addAll(nodeData);
                                lstMedicalPendingRequirement = medicalPendingRequirementList.size();
                                strAutoMandateStatusListError = "success";
                            } else {
                                strAutoMandateStatusListError = "1";
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
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            editextSearchMedicalPendingRequirement.setVisibility(View.GONE);
            if (running) {
                textviewRecordCount.setVisibility(View.VISIBLE);
                if (strAutoMandateStatusListError.equalsIgnoreCase("success")) {
                    textviewRecordCount.setText("Total Record : " + lstMedicalPendingRequirement);
                    selectedAdapterMedicalPendingRequirement = new SelectedAdapterMedicalPendingRequirement(medicalPendingRequirementList);
                    //recyclerviewProposalList.setAdapter(selectedAdapterProposalList);
                    recyclerviewMedicalPendingRequirement.setAdapter(selectedAdapterMedicalPendingRequirement);
                    recyclerviewMedicalPendingRequirement.invalidate();
                    editextSearchMedicalPendingRequirement.setVisibility(View.VISIBLE);
                } else {
                    textviewRecordCount.setText("Total Record : " + 0);
                    clearList();
                }
            } else {
                commonMethods.showMessageDialog(context, "No Record found");
            }
        }
    }

    private void service_hits(String input) {
        String strCIFBDMUserId, strCIFBDMEmailId,
                strCIFBDMPassword, strCIFBDMMObileNo;
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods
                .setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

        service = new ServiceHits(context,
                METHOD_NAME_MEDICAL_PENDING_REQUIREMENT, input,
                strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();
    }


    public class SelectedAdapterMedicalPendingRequirement extends RecyclerView.Adapter<SelectedAdapterMedicalPendingRequirement.ViewHolderAdapter> implements Filterable {

        private ArrayList<ParseXML.MedicalPendingRequirementValuesModel> lstAdapterList, lstSearch;

        SelectedAdapterMedicalPendingRequirement(ArrayList<ParseXML.MedicalPendingRequirementValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<ParseXML.MedicalPendingRequirementValuesModel> results = new ArrayList<>();

                    if (lstSearch == null)
                        lstSearch = lstAdapterList;

                    String strSearch = charSequence == null ? "" : charSequence.toString();

                    if (!strSearch.equals("")) {
                        if (lstSearch != null && lstSearch.size() > 0) {
                            for (final ParseXML.MedicalPendingRequirementValuesModel model : lstSearch) {
                                if (model.getPOLICYPROPOSALNUMBER().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getCHANNELCODE().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getCASHIERINGDATE().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                    results.add(model);
                                }
                            }
                        }
                        oReturn.values = results;
                    } else {
                        oReturn.values = medicalPendingRequirementList;
                    }
                    return oReturn;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    lstAdapterList = (ArrayList<ParseXML.MedicalPendingRequirementValuesModel>) results.values;
                    selectedAdapterMedicalPendingRequirement = new SelectedAdapterMedicalPendingRequirement(lstAdapterList);
                    recyclerviewMedicalPendingRequirement.setAdapter(selectedAdapterMedicalPendingRequirement);

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
                    R.layout.list_medical_pending_requirement, parent, false);

            // set the view's size, margins, paddings and layout parameters
            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(ViewHolderAdapter holder, int position) {
            if (strUserType.equalsIgnoreCase("UM") || strUserType.equalsIgnoreCase("Agent")) {
                holder.textviewMedicalPendingRequirementChannelCodeTitle.setText("IA Code");
            } else if (strUserType.equalsIgnoreCase("BDM") || strUserType.equalsIgnoreCase("CIF")) {
                holder.textviewMedicalPendingRequirementChannelCodeTitle.setText("CIF Code");
            }


            holder.textviewMedicalPendingRequirementPolicyProposalNumber
                    .setText(lstAdapterList.get(position).getPOLICYPROPOSALNUMBER() == null ? ""
                            : lstAdapterList.get(position).getPOLICYPROPOSALNUMBER());

            holder.textviewMedicalPendingRequirementChannelCode.setText(lstAdapterList.get(position).getCHANNELCODE() == null ? "" : lstAdapterList.get(
                    position).getCHANNELCODE());
            holder.textviewMedicalPendingRequirementStatus.setText(lstAdapterList.get(position).getSTATUS() == null ? "" : lstAdapterList.get(
                    position).getSTATUS());

            holder.textviewMedicalPendingRequirementCashieringDate.setText(lstAdapterList.get(position).getCASHIERINGDATE() == null ? ""
                    : lstAdapterList.get(position).getCASHIERINGDATE());
            holder.textviewMedicalPendingRequirementPaymentAmount.setText(lstAdapterList.get(position).getPAYMENTAMOUNT() == null ? ""
                    : lstAdapterList.get(position).getPAYMENTAMOUNT());

            holder.textviewMedicalPendingRequirementPendingStatus.setText(lstAdapterList.get(position).getPENDINGSTATUS() == null ? ""
                    : lstAdapterList.get(position).getPENDINGSTATUS());
        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder {

            private final TextView textviewMedicalPendingRequirementPolicyProposalNumber, textviewMedicalPendingRequirementChannelCode,
                    textviewMedicalPendingRequirementChannelCodeTitle, textviewMedicalPendingRequirementStatus,
                    textviewMedicalPendingRequirementCashieringDate, textviewMedicalPendingRequirementPaymentAmount,
                    textviewMedicalPendingRequirementPendingStatus;

            ViewHolderAdapter(View v) {
                super(v);
                textviewMedicalPendingRequirementPolicyProposalNumber = v.findViewById(R.id.textviewMedicalPendingRequirementPolicyProposalNumber);
                textviewMedicalPendingRequirementChannelCode = v.findViewById(R.id.textviewMedicalPendingRequirementChannelCode);
                textviewMedicalPendingRequirementChannelCodeTitle = v.findViewById(R.id.textviewMedicalPendingRequirementChannelCodeTitle);
                textviewMedicalPendingRequirementStatus = v.findViewById(R.id.textviewMedicalPendingRequirementStatus);

                textviewMedicalPendingRequirementCashieringDate = v.findViewById(R.id.textviewMedicalPendingRequirementCashieringDate);
                textviewMedicalPendingRequirementPaymentAmount = v.findViewById(R.id.textviewMedicalPendingRequirementPaymentAmount);
                textviewMedicalPendingRequirementPendingStatus = v.findViewById(R.id.textviewMedicalPendingRequirementPendingStatus);

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

        if (downloadPendingRequirementListAsync != null) {
            downloadPendingRequirementListAsync.cancel(true);
        }
        if (service != null) {
            service.cancel(true);
        }
    }
}
