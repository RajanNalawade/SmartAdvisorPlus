package sbilife.com.pointofsale_bancaagency.posp_ra;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.agent_on_boarding.ActivityAOBDocumentUpload;
import sbilife.com.pointofsale_bancaagency.agent_on_boarding.Activity_AOB_Authentication;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class Activity_POSP_RA_Rejection_Remarks extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, AsyncGetLM_POSP_Data.InterfaceAsyncGetLM_POSP_Data {

    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME_GET_UM_REJECTION_STATUS = "getAgentPanStatusUM_smrt";

    private Context mContext;
    private CommonMethods mCommonMethods;
    private DatabaseHelper db;

    private TextView txt_posp_ra_reject_no_data, txt_posp_ra_reject_tittle;
    private Spinner spnr_posp_ra_reject_pan;
    private RecyclerView recycle_posp_ra_reject_list;
    private Button btn_posp_ra_reject_submit;

    private ArrayList<String> listPan = new ArrayList<>();
    private String strUMCode = "", str_enrollment_type = "";
    private ArrayList<POJO_POSP_RA_Rejection> listRejectionData = new ArrayList<>();
    private Async_Get_UM_Rejection_Status mAsyncGetUmRejectionStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_posp_ra_rejection_remarks);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.cifenrollment_pf_activity_title);

        initialisation();
    }

    private void initialisation() {
        mContext = this;
        mCommonMethods = new CommonMethods();

        db = new DatabaseHelper(mContext);

        strUMCode = mCommonMethods.GetUserCode(mContext);

        str_enrollment_type = getIntent().getStringExtra("enrollment_type");

        txt_posp_ra_reject_tittle = findViewById(R.id.txt_posp_ra_reject_tittle);

        if (str_enrollment_type.equals(mCommonMethods.str_ia_upgrade_customer_type)) {
            txt_posp_ra_reject_tittle.setText("IA Upgrade Rejection");
            mCommonMethods.setApplicationToolbarMenu1(this,"Agent On Boarding");
        } else if (str_enrollment_type.equals(mCommonMethods.str_posp_ra_customer_type)) {
            txt_posp_ra_reject_tittle.setText("POSP-RA Rejection");
            mCommonMethods.setApplicationToolbarMenu1(this, "POSP-RA");
        }

        txt_posp_ra_reject_no_data = findViewById(R.id.txt_posp_ra_reject_no_data);
        txt_posp_ra_reject_no_data.setVisibility(View.VISIBLE);

        spnr_posp_ra_reject_pan = findViewById(R.id.spnr_posp_ra_reject_pan);
        spnr_posp_ra_reject_pan.setOnItemSelectedListener(this);

        recycle_posp_ra_reject_list = findViewById(R.id.recycle_posp_ra_reject_list);

        btn_posp_ra_reject_submit = findViewById(R.id.btn_posp_ra_reject_submit);
        btn_posp_ra_reject_submit.setOnClickListener(this);

        spnr_posp_ra_reject_pan.setVisibility(View.GONE);
        recycle_posp_ra_reject_list.setVisibility(View.GONE);
        btn_posp_ra_reject_submit.setVisibility(View.GONE);


        mAsyncGetUmRejectionStatus = new Async_Get_UM_Rejection_Status();
        mAsyncGetUmRejectionStatus.execute();

        SwipeRefreshLayout refersh_posp_ra_rejection = findViewById(R.id.refersh_posp_ra_rejection);
        refersh_posp_ra_rejection.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAsyncGetUmRejectionStatus = new Async_Get_UM_Rejection_Status();
                mAsyncGetUmRejectionStatus.execute();
                refersh_posp_ra_rejection.setRefreshing(false);
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (str_enrollment_type.equals(mCommonMethods.str_posp_ra_customer_type))
            startActivity(new Intent(Activity_POSP_RA_Rejection_Remarks.this, Activity_POSP_RA_Authentication.class));
        else if (str_enrollment_type.equals(mCommonMethods.str_ia_upgrade_customer_type))
            startActivity(new Intent(Activity_POSP_RA_Rejection_Remarks.this, Activity_AOB_Authentication.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_posp_ra_reject_submit:

                if (spnr_posp_ra_reject_pan.getSelectedItemPosition() == 0) {
                    mCommonMethods.showMessageDialog(mContext, "Please select PAN Number");
                } else {
                    //check wheter pan is available or not
                    String strPan = spnr_posp_ra_reject_pan.getSelectedItem().toString();
                    ArrayList<String> lstRslt = db.get_POS_RA_ID(strPan);

                    if (lstRslt.size() > 0) {
                        Activity_POSP_RA_Authentication.row_details = Integer.parseInt(lstRslt.get(0));

                        if (listRejectionData.size() > 0) {

                            if (str_enrollment_type.equals(mCommonMethods.str_posp_ra_customer_type)) {
                                if (listRejectionData.get(0).getStrReqStatus().equals("Rejected")
                                        && listRejectionData.get(0).getStrReqRaised().equals("HO User")) {
                                    Intent mIntent = new Intent(Activity_POSP_RA_Rejection_Remarks.this, Activity_POSP_RA_DocumentUpload.class);
                                    mIntent.putExtra("is_rejection", true);
                                    mIntent.putExtra("is_from_ho", true);
                                    startActivity(mIntent);
                                } else {
                                    Intent mIntent = new Intent(Activity_POSP_RA_Rejection_Remarks.this, Activity_POSP_RA_PersonalInfo.class);
                                    mIntent.putExtra("is_rejection", true);
                                    startActivity(mIntent);
                                }
                            } else if (str_enrollment_type.equals(mCommonMethods.str_ia_upgrade_customer_type)) {
                                Intent mIntent = new Intent(Activity_POSP_RA_Rejection_Remarks.this, ActivityAOBDocumentUpload.class);
                                mIntent.putExtra("is_ia_upgrade", true);
                                mIntent.putExtra("is_rejection", true);
                                mIntent.putExtra("pan_no", spnr_posp_ra_reject_pan.getSelectedItem().toString());
                                startActivity(mIntent);
                            }
                        }
                    } else {
                        //get Data from server
                        //get all POSP data against respective PAN from server
                        new AsyncGetLM_POSP_Data(Activity_POSP_RA_Rejection_Remarks.this,
                                strPan, mContext, mCommonMethods.str_posp_ra_customer_type)
                                .execute();
                    }
                }

                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {

            case R.id.spnr_posp_ra_reject_pan:

                if (position != 0) {
                    listRejectionData = db.getPOSP_RA_Rejection_By_PAN(listPan.get(position), str_enrollment_type);

                    // set a LinearLayoutManager with default horizontal orientation and false value for reverseLayout to show the items from start to end
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                    // set LayoutManager to RecyclerView
                    recycle_posp_ra_reject_list.setLayoutManager(linearLayoutManager);
                    // call the constructor of CustomAdapter to send the reference and data to Adapter
                    AdapterRejectionData mHomeMenuAdapter = new AdapterRejectionData(listRejectionData);
                    recycle_posp_ra_reject_list.setAdapter(mHomeMenuAdapter);
                    recycle_posp_ra_reject_list.setItemAnimator(new DefaultItemAnimator());
                } else {
                    listRejectionData.clear();
                }
                break;

            default:
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDataSuccess(int row_details) {
        if (row_details == -1) {
            mCommonMethods.showMessageDialog(mContext, mCommonMethods.WEEK_INTERNET_MESSAGE);
        } else {
            Activity_POSP_RA_Authentication.row_details = row_details;

            if (listRejectionData.size() > 0) {

                if (str_enrollment_type.equals(mCommonMethods.str_posp_ra_customer_type)) {
                    if (listRejectionData.get(0).getStrReqStatus().equals("Rejected")
                            && listRejectionData.get(0).getStrReqRaised().equals("HO User")) {
                        Intent mIntent = new Intent(Activity_POSP_RA_Rejection_Remarks.this, Activity_POSP_RA_DocumentUpload.class);
                        mIntent.putExtra("is_rejection", true);
                        mIntent.putExtra("is_from_ho", true);
                        startActivity(mIntent);
                    } else {
                        Intent mIntent = new Intent(Activity_POSP_RA_Rejection_Remarks.this, Activity_POSP_RA_PersonalInfo.class);
                        mIntent.putExtra("is_rejection", true);
                        startActivity(mIntent);
                    }
                } else if (str_enrollment_type.equals(mCommonMethods.str_ia_upgrade_customer_type)) {
                    Intent mIntent = new Intent(Activity_POSP_RA_Rejection_Remarks.this, ActivityAOBDocumentUpload.class);
                    mIntent.putExtra("is_ia_upgrade", true);
                    mIntent.putExtra("is_rejection", true);
                    mIntent.putExtra("pan_no", spnr_posp_ra_reject_pan.getSelectedItem().toString());
                    startActivity(mIntent);
                }
            }
        }

    }

    class AdapterRejectionData extends RecyclerView.Adapter<AdapterRejectionData.ViewHolderAdapter> {

        private ArrayList<POJO_POSP_RA_Rejection> listAdapterData = new ArrayList<>();

        public AdapterRejectionData(ArrayList<POJO_POSP_RA_Rejection> listAdapterData) {
            this.listAdapterData = listAdapterData;
        }

        @Override
        public int getItemCount() {
            return listAdapterData.size();
        }

        @NonNull
        @Override
        public ViewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_posp_ra_rejection_data, parent, false);

            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final ViewHolderAdapter holder, int position) {
            holder.tvPOSPRARejectIACode.setText(listAdapterData.get(position).getStrIACode());
            holder.tvPOSPRARejectRaisedStatus.setText(listAdapterData.get(position).getStrReqStatus());
            holder.tvPOSPRARejectRaisedBy.setText(listAdapterData.get(position).getStrReqRaised());
            holder.tvPOSPRARejectDocName.setText(listAdapterData.get(position).getStrDocName());
            holder.tvPOSPRARejectOptVal.setText(listAdapterData.get(position).getStrDocOptionalVal());
            holder.tvPOSPRARejectRemarks.setText(listAdapterData.get(position).getStrRemarks());
        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder {

            private final TextView tvPOSPRARejectIACode, tvPOSPRARejectRaisedStatus, tvPOSPRARejectRaisedBy,
                    tvPOSPRARejectDocName, tvPOSPRARejectOptVal, tvPOSPRARejectRemarks;

            ViewHolderAdapter(View v) {
                super(v);
                tvPOSPRARejectIACode = v.findViewById(R.id.tvPOSPRARejectIACode);
                tvPOSPRARejectRaisedStatus = v.findViewById(R.id.tvPOSPRARejectRaisedStatus);
                tvPOSPRARejectRaisedBy = v.findViewById(R.id.tvPOSPRARejectRaisedBy);
                tvPOSPRARejectDocName = v.findViewById(R.id.tvPOSPRARejectDocName);
                tvPOSPRARejectOptVal = v.findViewById(R.id.tvPOSPRARejectOptVal);
                tvPOSPRARejectRemarks = v.findViewById(R.id.tvPOSPRARejectRemarks);
            }

        }
    }

    class Async_Get_UM_Rejection_Status extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private List<ParseXML.Pojo_POSP_RA_Rejection> lstRejectionData = new ArrayList<>();
        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>Please wait...<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... input) {

            if (mCommonMethods.isNetworkConnected(mContext)) {

                try {
                    running = true;

                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_GET_UM_REJECTION_STATUS);
                    request.addProperty("strUMCode", strUMCode);
                    mCommonMethods.appendSecurityParams(mContext, request, "", "");

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL, 50000);
                    androidHttpTranport.call(NAMESPACE + METHOD_NAME_GET_UM_REJECTION_STATUS, envelope);

                    SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();
                    String inputpolicylist = sa.toString();

                    if (inputpolicylist.equals("0")) {
                        return inputpolicylist;
                    } else {
                        ParseXML prsObj = new ParseXML();

                        inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "NewDataSet");
                        List<String> nodeData = prsObj.parseParentNode(inputpolicylist, "Table");
                        lstRejectionData = prsObj.parseNodeElementPOSP_RA_Rejection(nodeData, str_enrollment_type);

                        return "Success";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    running = false;
                    return e.getMessage();
                }

            } else {
                running = false;
            }
            return null;

        }

        @Override
        protected void onPostExecute(String unused) {

            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                if (unused.equals("0")) {
                    txt_posp_ra_reject_no_data.setVisibility(View.VISIBLE);

                    spnr_posp_ra_reject_pan.setVisibility(View.GONE);
                    recycle_posp_ra_reject_list.setVisibility(View.GONE);
                    btn_posp_ra_reject_submit.setVisibility(View.GONE);
                } else {
                    if (lstRejectionData.size() == 0) {
                        txt_posp_ra_reject_no_data.setVisibility(View.VISIBLE);

                        spnr_posp_ra_reject_pan.setVisibility(View.GONE);
                        recycle_posp_ra_reject_list.setVisibility(View.GONE);
                        btn_posp_ra_reject_submit.setVisibility(View.GONE);
                    } else {

                        txt_posp_ra_reject_no_data.setVisibility(View.GONE);

                        spnr_posp_ra_reject_pan.setVisibility(View.VISIBLE);
                        recycle_posp_ra_reject_list.setVisibility(View.VISIBLE);
                        btn_posp_ra_reject_submit.setVisibility(View.VISIBLE);

                        db.delete_POSP_RA_Rejection_all_row();

                        listPan.clear();
                        listPan.add("Select PAN Number");

                        for (ParseXML.Pojo_POSP_RA_Rejection obj : lstRejectionData) {
                            ContentValues cv = new ContentValues();
                            cv.put(db.POSP_RA_DATA_ID, Activity_POSP_RA_Authentication.row_details);
                            cv.put(db.POSP_RA_REQ_PAN, obj.getStr_req_pan());
                            cv.put(db.POSP_RA_REQ_IA_CODE, obj.getStr_req_ia_code());
                            cv.put(db.POSP_RA_REQ_RAISED, obj.getStr_req_raised());
                            cv.put(db.POSP_RA_REQ_RAISED_REMARK, obj.getStr_req_raised_remark());
                            cv.put(db.POSP_RA_REQ_RAISED_DOC_STATUS, obj.getStr_req_raised_document_status());
                            cv.put(db.POSP_RA_REQ_RAISED_STATUS, obj.getStr_req_raised_status());
                            cv.put(db.POSP_RA_REQ_RAISED_DOC_NAME, obj.getStr_req_raised_document_name());
                            cv.put(db.POSP_RA_REQ_RAISED_DOC_OPTION_VAL, obj.getStr_req_raised_document_option_value());
                            cv.put(db.POSP_RA_REQ_RAISED_UM_CODE, strUMCode);
                            cv.put(db.POSP_RA_REQ_RAISED_ENROLLMENT_TYPE, obj.getStr_req_raised_enrollment_type());

                            db.insert_POSP_RA_Rejection(cv);

                            if (!listPan.contains(obj.getStr_req_pan()))
                                listPan.add(obj.getStr_req_pan());
                        }

                        ArrayAdapter<String> reject_pan_adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_aob, listPan);
                        spnr_posp_ra_reject_pan.setAdapter(reject_pan_adapter);
                        reject_pan_adapter.notifyDataSetChanged();
                    }
                }
            } else {
                mCommonMethods.showToast(mContext, mCommonMethods.WEEK_INTERNET_MESSAGE);
            }
        }
    }
}