package sbilife.com.pointofsale_bancaagency.utility;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import sbilife.com.pointofsale_bancaagency.home.CarouselHomeActivity;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class DocUploadNonMedicalPendingActivity extends AppCompatActivity implements View.OnClickListener, ServiceHits.DownLoadData {

    private Context mContext;
    private CommonMethods mCommonMethods;

    private TextView txtFromDate, txtToDate;
    private RecyclerView recyViewListDocUpload;

    private String strDateFlag = "", strCIFBDMUserId = "", strCIFBDMEmailId = "", strCIFBDMPassword = "",
            strCIFBDMMObileNo = "", strFromDate = "", strToDate = "";

    private final int DATE_DIALOG_ID = 1;


    private final String NAMESPACE = "http://tempuri.org/";
    //private  final String METHOD_NAME_CHANNEL_PROPOSAL_TRACKER_LIST = "getChannelProposalStatus";
    private final String METHOD_NAME_CHANNEL_PROPOSAL_TRACKER_LIST = "getNonMedReq_smrt";
    private ProgressDialog mProgressDialog;

    private ArrayList<ParseXML.XMLChannelProposerTrackerStatusList> lstChannelProposerTrackerStatusXml ;
    private AdapterNonMedicalPending mAdapterNonMedicalPending;

    private String fromHome = "",strUserType = "";
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_doc_upload_non_medical_pending);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        initialisation();
    }


    private void initialisation() {

        mContext = this;
        mCommonMethods = new CommonMethods();
        Intent intent = getIntent();
        mCommonMethods.setApplicationToolbarMenu(this, "Requirement Upload - Non-Medical");

        txtFromDate = findViewById(R.id.txtFromDate);
        txtFromDate.setOnClickListener(this);
        txtToDate = findViewById(R.id.txtToDate);
        txtToDate.setOnClickListener(this);

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        Button btnDocUploaNonMedicalOK = findViewById(R.id.btnDocUploaNonMedicalOK);
        btnDocUploaNonMedicalOK.setOnClickListener(this);

        // get the reference of RecyclerView
        recyViewListDocUpload = findViewById(R.id.recyViewListDocUpload);
        recyViewListDocUpload.setHasFixedSize(true);
        // set a LinearLayoutManager with default horizontal orientation and false value for reverseLayout to show the items from start to end
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        // set LayoutManager to RecyclerView
        recyViewListDocUpload.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        lstChannelProposerTrackerStatusXml = new ArrayList<>();
        mAdapterNonMedicalPending = new AdapterNonMedicalPending(mContext, lstChannelProposerTrackerStatusXml);
        recyViewListDocUpload.setAdapter(mAdapterNonMedicalPending);
        recyViewListDocUpload.setItemAnimator(new DefaultItemAnimator());

         fromHome = intent.getStringExtra("fromHome");
        if (fromHome != null && fromHome.equalsIgnoreCase("N")) {

            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");
            strUserType = intent.getStringExtra("strUserType");
            try {
                /*strCIFBDMPassword = SimpleCrypto.encrypt("SBIL", "sbil");*/

                strCIFBDMPassword = mCommonMethods.getStrAuth();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            strUserType = mCommonMethods.GetUserType(mContext);
            getUserDetails();
        }
        //set dates
        txtFromDate.setText(mCommonMethods.getPreviousMonthDate());
        txtToDate.setText(mCommonMethods.getCurrentMonthDate());
    }
    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = mCommonMethods.setUserDetails(mContext);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
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
                    mCommonMethods.showMessageDialog(mContext, "Please Select Dates");
                } else {
                    String input = strFromDate + "," + strToDate;

                    final SimpleDateFormat formatter = new SimpleDateFormat(
                            "dd-MMMM-yyyy");
                    final SimpleDateFormat formatter1 = new SimpleDateFormat(
                            "MM-dd-yyyy");

                    try {
                        Date from_dt, to_dt ;
                        from_dt = formatter.parse(strFromDate);
                        strFromDate = formatter1.format(from_dt);

                        to_dt = formatter.parse(strToDate);
                        strToDate = formatter1.format(to_dt);

                        if(lstChannelProposerTrackerStatusXml!=null && mAdapterNonMedicalPending!=null){
                            lstChannelProposerTrackerStatusXml.clear();
                            mAdapterNonMedicalPending.notifyDataSetChanged();
                        }

                        if ((to_dt != null && to_dt.after(from_dt)) || (to_dt != null && to_dt.equals(from_dt))) {
                            // call list asynch task
                            service_hits(input);
                        } else {
                            mCommonMethods.showMessageDialog(mContext, "Please Select one month date defference!");
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                break;

            default:
                break;
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

        m = mCommonMethods.getFullMonthName(m);
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
    class AsynchGetNonMedicalPendingList extends AsyncTask<String, String, String> {

        private volatile boolean running = true;

        private String strChannelProposalTrackerErrorCOde = "";

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>Please wait...<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                running = true;

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_CHANNEL_PROPOSAL_TRACKER_LIST);
                request.addProperty("strCode", strCIFBDMUserId);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());
                String CHANNEL_PROPOSAL_TRACKER_TYPE = "Pending Requirement Status - Non Medical";
                request.addProperty("strType", CHANNEL_PROPOSAL_TRACKER_TYPE);
                request.addProperty("strFromDate", strFromDate);
                request.addProperty("strToDate", strToDate);
                mCommonMethods.TLSv12Enable();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);

                try {
                    String SOAP_ACTION_CHANNEL_PROPOSAL_TRACKER_LIST = "http://tempuri.org/getNonMedReq_smrt";
                    androidHttpTranport.call(SOAP_ACTION_CHANNEL_PROPOSAL_TRACKER_LIST, envelope);
                    //Object response = envelope.getResponse();

                    Object sa = null;
                    sa = envelope.getResponse();

                    String inputpolicylist = sa.toString();

                    /*inputpolicylist = "<PolicyDetails><Table><POLICYPROPOSALNUMBER>53AL912929</POLICYPROPOSALNUMBER><PAYMENTAMOUNT>200000</PAYMENTAMOUNT><CASHIERINGDATE >30-09-2016</CASHIERINGDATE> <STATUS>NON MEDICAL</STATUS></Table></PolicyDetails>";*/

                    if (!sa.toString().equalsIgnoreCase("")) {
                        ParseXML prsObj = new ParseXML();

                        inputpolicylist = prsObj.parseXmlTag(
                                inputpolicylist, "PolicyDetails");
                        if (inputpolicylist != null) {

                            List<String> Node = prsObj.parseParentNode(inputpolicylist, "Table");

                            List<ParseXML.XMLChannelProposerTrackerStatusList> nodeData = prsObj.
                                    parseNodeElement_ChannelProposertrackerStatusList(Node);

                            lstChannelProposerTrackerStatusXml = new ArrayList<>();
                            // lstPolicyList.clear();
                            for (ParseXML.XMLChannelProposerTrackerStatusList node : nodeData) {
                                lstChannelProposerTrackerStatusXml.add(node);
                            }

                            strChannelProposalTrackerErrorCOde = "success";

                        } else {
                            strChannelProposalTrackerErrorCOde = "1";
                        }
                    } else {
                        running = false;
                    }
                } catch (Exception e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    running = false;
                }
            } catch (Exception e) {
                try {
                    throw (e);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                running = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();


            if (running) {

                if (strChannelProposalTrackerErrorCOde != null) {

                    if (strChannelProposalTrackerErrorCOde.equalsIgnoreCase("success")) {

                        recyViewListDocUpload.setVisibility(View.VISIBLE);
                        mAdapterNonMedicalPending = new AdapterNonMedicalPending(mContext, lstChannelProposerTrackerStatusXml);
                        recyViewListDocUpload.setAdapter(mAdapterNonMedicalPending);
                        recyViewListDocUpload.getParent().requestChildFocus(recyViewListDocUpload, recyViewListDocUpload);

                    } else {
                        recyViewListDocUpload.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "No record found", Toast.LENGTH_LONG).show();
                    }
                } else {
                    recyViewListDocUpload.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "You are not authorised user", Toast.LENGTH_LONG).show();
                }
            } else {
                recyViewListDocUpload.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "No record found",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public class AdapterNonMedicalPending extends RecyclerView.Adapter<AdapterNonMedicalPending.NonMedicalViewHolder> {

        private final Context mAdapterContext;
        private ArrayList<ParseXML.XMLChannelProposerTrackerStatusList> mAdapterList = new ArrayList<>();

        AdapterNonMedicalPending(Context mAdapterContext,
                                 ArrayList<ParseXML.XMLChannelProposerTrackerStatusList> mAdapterList) {
            this.mAdapterContext = mAdapterContext;
            this.mAdapterList = mAdapterList;
        }

        @Override
        public int getItemCount() {
            return mAdapterList.size();
        }

        @Override
        public NonMedicalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            // infalte the item Layout
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_doc_upload_non_medical_pending, parent, false);

            // set the view's size, margins, paddings and layout parameters
            NonMedicalViewHolder mHolder = new NonMedicalViewHolder(mView); // pass the view to View Holder
            return mHolder;
        }

        @Override
        public void onBindViewHolder(NonMedicalViewHolder holder, final int position) {

            if (strUserType.equalsIgnoreCase("UM") || strUserType.equalsIgnoreCase("Agent")) {
                holder.textviewUserCodeTitle.setText("IA Code");
            } else if (strUserType.equalsIgnoreCase("BDM") || strUserType.equalsIgnoreCase("CIF")) {
                holder.textviewUserCodeTitle.setText("CIF Code");
            }
            holder.textviewUserCode.setText(mAdapterList.get(position).getCHANNELCODE());
            holder.txtUploadNonMedicalProposal.setText(mAdapterList.get(position).getProposalNumber());
            holder.txtUploadNonMedicalPaymentAmount.setText(mAdapterList.get(position).getPaymentAmount());
            holder.txtUploadNonMedicalCashDate.setText(mAdapterList.get(position).getCashieringDate());
            holder.txtUploadNonMedicalStatus.setText(mAdapterList.get(position).getStatus());
            holder.txtUploadNonMedicalPendingStatus.setText(mAdapterList.get(position).getPendingStatus());

            holder.btnUploadNonMedicalDoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), AllDocumentsUploadActivity.class);
                    intent.putExtra("PROPOSAL_NO", mAdapterList.get(position).getProposalNumber());
                    startActivity(intent);
                }
            });
        }

        public class NonMedicalViewHolder extends RecyclerView.ViewHolder {
            private final TextView textviewUserCode, txtUploadNonMedicalProposal, txtUploadNonMedicalPaymentAmount,
                    txtUploadNonMedicalCashDate, txtUploadNonMedicalStatus, txtUploadNonMedicalPendingStatus;
            private final TextView textviewUserCodeTitle;
            private final ImageView btnUploadNonMedicalDoc;

            NonMedicalViewHolder(View itemView) {
                super(itemView);
                textviewUserCodeTitle = itemView.findViewById(R.id.textviewUserCodeTitle);
                textviewUserCode = itemView.findViewById(R.id.textviewUserCode);
                txtUploadNonMedicalProposal = itemView.findViewById(R.id.txtUploadNonMedicalProposal);
                txtUploadNonMedicalPaymentAmount = itemView.findViewById(R.id.txtUploadNonMedicalPaymentAmount);
                txtUploadNonMedicalCashDate = itemView.findViewById(R.id.txtUploadNonMedicalCashDate);
                txtUploadNonMedicalStatus = itemView.findViewById(R.id.txtUploadNonMedicalStatus);
                txtUploadNonMedicalPendingStatus = itemView.findViewById(R.id.txtUploadNonMedicalPendingStatus);

                btnUploadNonMedicalDoc = itemView.findViewById(R.id.btnUploadNonMedicalDoc);
            }
        }
    }

    private void service_hits(String input) {

        ServiceHits service = new ServiceHits(mContext,  METHOD_NAME_CHANNEL_PROPOSAL_TRACKER_LIST, input, strCIFBDMUserId,
                strCIFBDMEmailId, strCIFBDMMObileNo, strCIFBDMPassword, this);
        service.execute();
    }

    @Override
    public void downLoadData() {
        AsynchGetNonMedicalPendingList mAsynchGetNonMedicalPendingList = new AsynchGetNonMedicalPendingList();
        mAsynchGetNonMedicalPendingList.execute();
    }

    @Override
    public void onBackPressed() {
        if (fromHome.equalsIgnoreCase("Y")) {
            Intent i = new Intent(mContext, CarouselHomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
