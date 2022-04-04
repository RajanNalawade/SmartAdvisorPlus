package sbilife.com.pointofsale_bancaagency.posp_ra.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

public class Activity_POSP_RA_AgentReqDoc extends AppCompatActivity implements ServiceHits.DownLoadData {

    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME_GET_AOB_AGENT_REQ_DOC = "getAgentReqDoc_smrt";

    private final String METHOD_NAME_GET_AOB_AGENT_DOC_STATUS = "getAgentDocStatus_smrt";

    private CommonMethods mCommonMethods;
    private Context mContext;

    private RecyclerView rvAgentReqDoc;
    //private EditText edt_agentReqDoc_search_by_all;
    private TextView txtReqDocTotalCount;

    private List<ParseXML.PojoAOBAgentDetails> lstAOBAgentReqDoc = new ArrayList();
    private List<ParseXML.PojoAOBAgentDetails> lstAOBAgentDocStatus = new ArrayList();

    private AdapterAOBAgentReqDoc mAdapterAOBAgentReqDoc;
    private String strCIFBDMUserId = "", strCIFBDMEmailId = "", strCIFBDMMObileNo = "",
            strCIFBDMPassword = "", strDocType = "";
    private ProgressDialog mProgressDialog;

    private AsyncGetAOBAgentReqDoc mAsyncGetAOBAgentDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_posp_ra_agent_req_doc);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        mCommonMethods = new CommonMethods();
        mContext = this;

        mCommonMethods.setApplicationToolbarMenu(this, "Agent Documents Status");

        getUserDetails();

        strCIFBDMUserId = getIntent().getStringExtra("AGENT_CODE");
        strDocType = "STATUS_DOC";

        txtReqDocTotalCount = (TextView) findViewById(R.id.txtReqDocTotalCount);

        rvAgentReqDoc = findViewById(R.id.rvAgentReqDoc);
        //lstReq.setHasFixedSize(true);
        // set a LinearLayoutManager with default horizontal orientation and false value for reverseLayout to show the items from start to end
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        // set LayoutManager to RecyclerView
        rvAgentReqDoc.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        mAdapterAOBAgentReqDoc = new AdapterAOBAgentReqDoc(mContext, lstAOBAgentDocStatus);
        rvAgentReqDoc.setAdapter(mAdapterAOBAgentReqDoc);
        rvAgentReqDoc.setItemAnimator(new DefaultItemAnimator());

        if (mCommonMethods.isNetworkConnected(mContext))
            service_hits();
        else
            mCommonMethods.showCentralToast(mContext, mCommonMethods.NO_INTERNET_MESSAGE);
    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = mCommonMethods.setUserDetails(mContext);
        //strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }

    @Override
    public void downLoadData() {
        if (mCommonMethods.isNetworkConnected(mContext)) {
            mAsyncGetAOBAgentDetails = new AsyncGetAOBAgentReqDoc();
            mAsyncGetAOBAgentDetails.execute();
        } else {
            mCommonMethods.showCentralToast(mContext, mCommonMethods.NO_INTERNET_MESSAGE);
        }
    }

    private void service_hits() {
        ServiceHits service = new ServiceHits(mContext,
                METHOD_NAME_GET_AOB_AGENT_REQ_DOC, "", strCIFBDMUserId,
                strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();
    }

    class AsyncGetAOBAgentReqDoc extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String strError = "";

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>Loading Please wait...<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                SoapObject request;

                if (strDocType.equals("REQ_DOC")) {
                    request = new SoapObject(NAMESPACE, METHOD_NAME_GET_AOB_AGENT_REQ_DOC);
                } else {
                    request = new SoapObject(NAMESPACE, METHOD_NAME_GET_AOB_AGENT_DOC_STATUS);
                }

                request.addProperty("strCode", strCIFBDMUserId);//7160
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", mCommonMethods.getStrAuth());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                try {

                    if (strDocType.equals("REQ_DOC")) {
                        androidHttpTranport.call(NAMESPACE + METHOD_NAME_GET_AOB_AGENT_REQ_DOC, envelope);
                    } else {
                        androidHttpTranport.call(NAMESPACE + METHOD_NAME_GET_AOB_AGENT_DOC_STATUS, envelope);
                    }

                    Object response = envelope.getResponse();

                    if (response.toString().equals("0") || response.toString().contentEquals("anyType{}")) {
                        strError = "No Data Found";
                    } else {

                        SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();

                        String strResponce = sa.toString();

                        ParseXML prsObj = new ParseXML();

                        strResponce = prsObj.parseXmlTag(strResponce, "NewDataSet");

                        strResponce = strResponce == null ? "" : strResponce;

                        if (!strResponce.equals("") && !strResponce.equals("0")) {

                            List<String> Node = prsObj.parseParentNode(strResponce, "Table");

                            if (strDocType.equals("REQ_DOC")) {
                                lstAOBAgentReqDoc = prsObj.parseNodeAOBAgentDetails(Node, "REQ_DOC");
                            } else {
                                lstAOBAgentDocStatus = prsObj.parseNodeAOBAgentDetails(Node, "STATUS_DOC");
                            }
                            strError = "";
                        } else {
                            strError = "No Data Found";
                        }
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

                if (strDocType.equals("REQ_DOC")) {
                    if (strError.equals("No Data Found")) {
                        mCommonMethods.showToast(mContext, "No Data Found");
                    } else {
                        //show dialog
                        final Dialog dialog = new Dialog(mContext, R.style.MyAlertDialogStyle);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dailog_aob_requirement_upload);
                        dialog.setCancelable(false);

                        Button btnAOBRequirementOK = (Button) dialog.findViewById(R.id.btnAOBRequirementOK);
                        RecyclerView rvAOBRequirementDetails = dialog.findViewById(R.id.rvAOBRequirementDetails);

                        // set a LinearLayoutManager with default horizontal orientation and false value for reverseLayout to show the items from start to end
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                        // set LayoutManager to RecyclerView
                        rvAOBRequirementDetails.setLayoutManager(linearLayoutManager);
                        // call the constructor of CustomAdapter to send the reference and data to Adapter
                        AdapterAOBReqDoc mAdapterAOBReqDoc = new AdapterAOBReqDoc(mContext, lstAOBAgentReqDoc);
                        rvAOBRequirementDetails.setAdapter(mAdapterAOBReqDoc);

                        btnAOBRequirementOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                strDocType = "STATUS_DOC";
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                    }

                } else {

                    if (!strError.equals("")) {
                        txtReqDocTotalCount.setText(strError);

                    } else {

                        if (lstAOBAgentDocStatus.size() == 0) {
                            txtReqDocTotalCount.setText("No Data Found");
                        }

                        mAdapterAOBAgentReqDoc = new AdapterAOBAgentReqDoc(mContext, lstAOBAgentDocStatus);
                        rvAgentReqDoc.setAdapter(mAdapterAOBAgentReqDoc);
                    }
                }

            } else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.SERVER_ERROR);
            }
        }
    }

    public class AdapterAOBAgentReqDoc extends RecyclerView.Adapter<AdapterAOBAgentReqDoc.ViewHolderAdapter> {

        private final Context mAdapterContext;
        private List<ParseXML.PojoAOBAgentDetails> lstAdapterList;

        AdapterAOBAgentReqDoc(Context mAdapterContext, List<ParseXML.PojoAOBAgentDetails> lstAdapterList) {
            this.mAdapterContext = mAdapterContext;
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public int getItemCount() {
            return lstAdapterList.size();
        }

        @NonNull
        @Override
        public ViewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            // infalte the item Layout
            //layout also used ActivityAOBAgentReqDoc.java
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_aob_agent_details, parent, false);

            // set the view's size, margins, paddings and layout parameters
            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(ViewHolderAdapter holder, int position) {

            if (strDocType.equals("REQ_DOC")) {

                String strVar1 = lstAdapterList.get(position).getStrVarible1() == null ? "" : lstAdapterList.get(position).getStrVarible1();
                String strVar2 = lstAdapterList.get(position).getStrVarible2() == null ? "" : lstAdapterList.get(position).getStrVarible2();

                holder.txtTitleIACode.setText("Document Name :");
                holder.txtValueIACode.setText(strVar1);

                holder.txtTitleStatus.setText("Remarks :");
                holder.txtValueStatus.setText(strVar2);

                holder.llReqAOBUserName.setVisibility(View.GONE);

            } else {
                String strVar1 = lstAdapterList.get(position).getStrVarible1() == null ? "" : lstAdapterList.get(position).getStrVarible1();
                //String strVar2 = lstAdapterList.get(position).getStrVarible2() == null ? "" : lstAdapterList.get(position).getStrVarible2();
                String strVar3 = lstAdapterList.get(position).getStrVarible3() == null ? "" : lstAdapterList.get(position).getStrVarible3();
                String strVar4 = lstAdapterList.get(position).getStrVarible4() == null ? "" : lstAdapterList.get(position).getStrVarible4();

                holder.llReqAOBUserName.setVisibility(View.VISIBLE);

                holder.txtTitleIACode.setText("Coding Status :");
                holder.txtValueIACode.setText(strVar1);

                holder.txtTitleStatus.setText("Last Updated Date :");
                holder.txtValueStatus.setText(strVar3);

                holder.txtTitleUserName.setText("User Name :");
                holder.txtValueUserName.setText(strVar4);

            }
        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder {

            private TextView txtTitleIACode, txtValueIACode, txtTitleStatus, txtValueStatus, txtTitleUserName, txtValueUserName;

            private LinearLayout llReqAOBUserName/*, llReqAOBStatus, llReqAOBIACode*/;

            ViewHolderAdapter(View v) {
                super(v);

                txtTitleIACode = v.findViewById(R.id.txtTitleIACode);
                txtValueIACode = v.findViewById(R.id.txtValueIACode);

                txtTitleStatus = v.findViewById(R.id.txtTitleStatus);
                txtValueStatus = v.findViewById(R.id.txtValueStatus);

                txtTitleUserName = v.findViewById(R.id.txtTitleUserName);
                txtValueUserName = v.findViewById(R.id.txtValueUserName);

                llReqAOBUserName = v.findViewById(R.id.llReqAOBUserName);
                /*llReqAOBStatus = v.findViewById(R.id.llReqAOBStatus);
                llReqAOBIACode = v.findViewById(R.id.llReqAOBIACode);*/

                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        //int pos = getLayoutPosition();

                        if (strDocType.equals("STATUS_DOC")) {

                            String strStatus = txtValueIACode.getText().toString();

                            if (strStatus.equalsIgnoreCase("Vendor Scrutiny Rejected")
                                    || strStatus.equalsIgnoreCase("Case Rejected by HO")
                                    || strStatus.equalsIgnoreCase("Pending Requirements")
                                    || strStatus.equalsIgnoreCase("Requirements Pending")
                                    || strStatus.equalsIgnoreCase("Pending Vendor Scrutiny (WI Re-Processed)")) {
                                if (mCommonMethods.isNetworkConnected(mContext)) {

                                    lstAOBAgentReqDoc.clear();

                                    strDocType = "REQ_DOC";
                                    service_hits();
                                } else
                                    mCommonMethods.showCentralToast(mContext, mCommonMethods.NO_INTERNET_MESSAGE);
                            } else {
                                mCommonMethods.showToast(mContext, "Document Requirement not Available for this status!!");
                            }

                        }
                        return true;
                    }
                });
            }
        }
    }

    public class AdapterAOBReqDoc extends RecyclerView.Adapter<AdapterAOBReqDoc.ViewHolderAdapter> {

        private final Context mAdapterContext;
        private List<ParseXML.PojoAOBAgentDetails> lstAdapterList;

        AdapterAOBReqDoc(Context mAdapterContext, List<ParseXML.PojoAOBAgentDetails> lstAdapterList) {
            this.mAdapterContext = mAdapterContext;
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public int getItemCount() {
            return lstAdapterList.size();
        }

        @Override
        public ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {

            // infalte the item Layout
            //layout also used ActivityAOBAgentReqDoc.java
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_aob_agent_details, parent, false);

            // set the view's size, margins, paddings and layout parameters
            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(ViewHolderAdapter holder, int position) {

            String strRemarks = lstAOBAgentReqDoc.get(position).getStrVarible1() == null ? "" : lstAOBAgentReqDoc.get(position).getStrVarible1();
            String strDocName = lstAOBAgentReqDoc.get(position).getStrVarible2() == null ? "" : lstAOBAgentReqDoc.get(position).getStrVarible2();

            holder.txtTitleIACode.setText("Document Name :");
            holder.txtValueIACode.setText(strDocName);

            holder.txtTitleStatus.setText("Remarks :");
            holder.txtValueStatus.setText(strRemarks);

            holder.llReqAOBUserName.setVisibility(View.GONE);

            holder.viewRow.setVisibility(View.VISIBLE);
        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder {

            private TextView txtTitleIACode, txtValueIACode, txtTitleStatus,
                    txtValueStatus;
            private LinearLayout llReqAOBUserName;
            private View viewRow;

            ViewHolderAdapter(View v) {
                super(v);

                txtTitleIACode = v.findViewById(R.id.txtTitleIACode);
                txtValueIACode = v.findViewById(R.id.txtValueIACode);

                txtTitleStatus = v.findViewById(R.id.txtTitleStatus);
                txtValueStatus = v.findViewById(R.id.txtValueStatus);

                llReqAOBUserName = v.findViewById(R.id.llReqAOBUserName);

                viewRow = v.findViewById(R.id.viewRow);
            }
        }
    }
}
