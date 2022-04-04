package sbilife.com.pointofsale_bancaagency.posp_ra.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
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

public class Activity_POSP_RA_AgentDetails extends AppCompatActivity implements ServiceHits.DownLoadData {

    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME_GET_POSP_RA_STATUS = "getPANStatusUMdash_smrt";

    private CommonMethods mCommonMethods;
    private Context mContext;

    private RecyclerView rvAgentDetails;
    private EditText edt_search_by_all;
    private TextView txtTotalCount;

    private List<ParseXML.PojoPOSPRAStatus> lstPOSP_RA_StatusDetails = new ArrayList<>();

    private AdapterPOSP_RA_STATUS_Details mAdapterPOSPRaStatusDetails;
    private String strCIFBDMUserId = "", strCIFBDMEmailId = "", strCIFBDMMObileNo = "",
            strCIFBDMPassword = "";
    private ProgressDialog mProgressDialog;

    private AsyncGetPOSPStatusDetails mAsyncGetPOSPStatusDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_posp_ra_agent_details);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        mCommonMethods = new CommonMethods();
        mContext = this;

        mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);

        mCommonMethods.setApplicationToolbarMenu(this, "PAN Details");

        getUserDetails();

        edt_search_by_all = (EditText) findViewById(R.id.edt_search_by_all);

        txtTotalCount = (TextView) findViewById(R.id.txtTotalCount);

        rvAgentDetails = findViewById(R.id.rvAgentDetails);
        //lstReq.setHasFixedSize(true);
        // set a LinearLayoutManager with default horizontal orientation and false value for reverseLayout to show the items from start to end
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        // set LayoutManager to RecyclerView
        rvAgentDetails.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        mAdapterPOSPRaStatusDetails = new AdapterPOSP_RA_STATUS_Details(mContext, lstPOSP_RA_StatusDetails);
        rvAgentDetails.setAdapter(mAdapterPOSPRaStatusDetails);
        rvAgentDetails.setItemAnimator(new DefaultItemAnimator());

        edt_search_by_all.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapterPOSPRaStatusDetails.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        if (mCommonMethods.isNetworkConnected(mContext))
            service_hits();
        else
            mCommonMethods.showCentralToast(mContext, mCommonMethods.NO_INTERNET_MESSAGE);
    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = mCommonMethods.setUserDetails(mContext);

        if (getIntent().hasExtra("UMCode")) {
            strCIFBDMUserId = getIntent().getStringExtra("UMCode");
        } else {
            strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        }
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }

    @Override
    public void downLoadData() {
        if (mCommonMethods.isNetworkConnected(mContext)) {
            mAsyncGetPOSPStatusDetails = new AsyncGetPOSPStatusDetails();
            mAsyncGetPOSPStatusDetails.execute();
        } else {
            mCommonMethods.showCentralToast(mContext, mCommonMethods.NO_INTERNET_MESSAGE);
        }
    }

    private void service_hits() {
        ServiceHits service = new ServiceHits(mContext,
                METHOD_NAME_GET_POSP_RA_STATUS, "", strCIFBDMUserId,
                strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();
    }

    class AsyncGetPOSPStatusDetails extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String strError = "";

        @Override
        protected void onPreExecute() {

            if (!mProgressDialog.isShowing()) {
                mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
                mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>Loading Please wait...<b></font>"));
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(true);
                mProgressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                SoapObject request;

                request = new SoapObject(NAMESPACE, METHOD_NAME_GET_POSP_RA_STATUS);

                request.addProperty("strUMCode", strCIFBDMUserId);
                mCommonMethods.appendSecurityParams(mContext, request, "", "");

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);

                androidHttpTranport.call(NAMESPACE + METHOD_NAME_GET_POSP_RA_STATUS, envelope);
                Object response = envelope.getResponse();

                if (!response.toString().contentEquals("anyType{}")) {

                    SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();

                    String strResponce = sa.toString();

                    ParseXML prsObj = new ParseXML();

                    strResponce = prsObj.parseXmlTag(strResponce, "NewDataSet");

                    strResponce = strResponce == null ? "" : strResponce;

                    if (!strResponce.equals("") || !strResponce.equals("0")) {

                        List<String> Node = prsObj.parseParentNode(strResponce, "Table");

                        lstPOSP_RA_StatusDetails = prsObj.parseNodePOSPRAStatusDetails(Node);

                        strError = "";

                    } else {
                        strError = "No Data Found";
                    }
                } else {
                    strError = "No Data Found";
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
                if (!strError.equals("")) {
                    txtTotalCount.setText(strError);
                    edt_search_by_all.setVisibility(View.GONE);
                } else {
                    if (lstPOSP_RA_StatusDetails.size() == 0) {
                        txtTotalCount.setText("Total : " + lstPOSP_RA_StatusDetails.size());
                        edt_search_by_all.setVisibility(View.GONE);
                    } else {
                        txtTotalCount.setText("Total : " + lstPOSP_RA_StatusDetails.size());
                    }
                }

                mAdapterPOSPRaStatusDetails = new AdapterPOSP_RA_STATUS_Details(mContext, lstPOSP_RA_StatusDetails);
                rvAgentDetails.setAdapter(mAdapterPOSPRaStatusDetails);
            } else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.SERVER_ERROR);
            }
        }
    }

    public class AdapterPOSP_RA_STATUS_Details extends RecyclerView.Adapter<AdapterPOSP_RA_STATUS_Details.ViewHolderAdapter> implements Filterable {

        private final Context mAdapterContext;
        private List<ParseXML.PojoPOSPRAStatus> lstAdapterList, lstSearch;

        AdapterPOSP_RA_STATUS_Details(Context mAdapterContext, List<ParseXML.PojoPOSPRAStatus> lstAdapterList) {
            this.mAdapterContext = mAdapterContext;
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<ParseXML.PojoPOSPRAStatus> results = new ArrayList<>();

                    if (lstSearch == null)
                        lstSearch = lstAdapterList;

                    String strSearch = charSequence == null ? "" : charSequence.toString();

                    if (!strSearch.equals("")) {
                        if (lstSearch != null && lstSearch.size() > 0) {
                            for (final ParseXML.PojoPOSPRAStatus g : lstSearch) {
                                if (g.getStrIACode().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || g.getStrStatus().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || g.getStrPAN().toLowerCase().contains(charSequence.toString().toLowerCase())
                                )
                                    results.add(g);
                            }
                        }
                        oReturn.values = results;
                    } else {
                        oReturn.values = lstPOSP_RA_StatusDetails;
                    }
                    return oReturn;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    lstAdapterList = (List<ParseXML.PojoPOSPRAStatus>) results.values;
                    notifyDataSetChanged();

                    txtTotalCount.setText("Total : " + lstAdapterList.size());
                }
            };
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

            holder.txtValueIACode.setText(lstAdapterList.get(position).getStrIACode() == null ? "" : lstAdapterList.get(position).getStrIACode());

            holder.txtTitleStatus.setText("Status : ");
            holder.txtValueStatus.setText(lstAdapterList.get(position).getStrStatus() == null ? "" : lstAdapterList.get(position).getStrStatus());

            //String strLastUpdatedDate = lstAdapterList.get(position).getStrVarible3() == null ? "" : lstAdapterList.get(position).getStrVarible3();
            String strPAN = lstAdapterList.get(position).getStrPAN() == null ? "" : lstAdapterList.get(position).getStrPAN();

            holder.txtTitleUserName.setText("PAN No. : ");
            holder.txtValueUserName.setText(strPAN);
        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder {

            private final TextView txtValueIACode, txtTitleStatus, txtValueStatus, txtTitleUserName, txtValueUserName;

            ViewHolderAdapter(View v) {
                super(v);

                txtValueIACode = v.findViewById(R.id.txtValueIACode);

                txtTitleStatus = v.findViewById(R.id.txtTitleStatus);
                txtValueStatus = v.findViewById(R.id.txtValueStatus);

                txtTitleUserName = v.findViewById(R.id.txtTitleUserName);
                txtValueUserName = v.findViewById(R.id.txtValueUserName);

                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        final int pos = getLayoutPosition();

                        Intent mIntent = new Intent(Activity_POSP_RA_AgentDetails.this, Activity_POSP_RA_AgentReqDoc.class);
                        mIntent.putExtra("AGENT_CODE", lstAdapterList.get(pos).getStrIACode());
                        mIntent.putExtra("DOC_TYPE", "STATUS_DOC");
                        startActivity(mIntent);


                        return true;
                    }
                });
            }
        }
    }
}
