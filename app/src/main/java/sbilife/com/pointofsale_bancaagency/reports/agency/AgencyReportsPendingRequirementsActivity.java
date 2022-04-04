package sbilife.com.pointofsale_bancaagency.reports.agency;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolder_Req;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;
import sbilife.com.pointofsale_bancaagency.utility.AllDocumentsUploadActivity;

/**
 * Created by O0110 on 24/01/2018.
 */

public class AgencyReportsPendingRequirementsActivity extends AppCompatActivity implements ServiceHits.DownLoadData {

    private final String METHOD_NAME_REQ = "getAdRequirementDtls";

    private ProgressDialog mProgressDialog;

    private CommonMethods mCommonMethods;
    private Context context;
    private final List<XMLHolder_Req> lsAgent_Req = new ArrayList<>();
    private SelectedAdapterReq selectedAdapterReq;
    private long lstReqCount = 0;

    private String strCIFBDMUserId = "", strCIFBDMEmailId = "",
            strCIFBDMPassword = "", strCIFBDMMObileNo = "";

    private DownloadFileAsyncReq taskReq;

    private TextView txterrordescreq, txtreqcount;
    private EditText edt_search_proposal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.agency_reports_pending_requirements);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        mCommonMethods = new CommonMethods();
        mCommonMethods.setApplicationToolbarMenu(this, "Pending Requirement");

        edt_search_proposal = findViewById(R.id.edt_search_proposal);

        txterrordescreq = findViewById(R.id.txterrordescreq);
        txtreqcount = findViewById(R.id.txtreqcount);

        RecyclerView lstReq = findViewById(R.id.lstReq);
        //lstReq.setHasFixedSize(true);
        // set a LinearLayoutManager with default horizontal orientation and false value for reverseLayout to show the items from start to end
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        // set LayoutManager to RecyclerView
        lstReq.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        selectedAdapterReq = new SelectedAdapterReq(context, lsAgent_Req);
        lstReq.setAdapter(selectedAdapterReq);
        lstReq.setItemAnimator(new DefaultItemAnimator());

        getUserDetails();

        Intent intent = getIntent();
        String bdmCifCOde = intent.getStringExtra("strBDMCifCOde");
        if (bdmCifCOde != null ) {
            strCIFBDMUserId = bdmCifCOde;
        }

        if(mCommonMethods.isNetworkConnected(context)) {
            service_hits();
        } else {
            mCommonMethods.showMessageDialog(context,mCommonMethods.NO_INTERNET_MESSAGE);
        }

        edt_search_proposal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                selectedAdapterReq.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = mCommonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }

    private void service_hits() {
        ServiceHits service = new ServiceHits(context,
                METHOD_NAME_REQ, "", strCIFBDMUserId,
                strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();

    }

    private void startDownloadPersistency() {
        taskReq = new DownloadFileAsyncReq();
        taskReq.execute("demo");
    }

    @Override
    public void downLoadData() {
        taskReq = new DownloadFileAsyncReq();
        startDownloadPersistency();
    }

    class DownloadFileAsyncReq extends AsyncTask<String, String, String> {

        String inputpolicylist = "", strPReqErrorCode = "", strReqErrorCode = "";
        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
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

                // strhitsproposalno = edProposalNo.getText().toString();

                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME_REQ);

                request.addProperty("strAgentCode", strCIFBDMUserId);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {

                    String SOAP_ACTION_REQ = "http://tempuri.org/getAdRequirementDtls";
                    androidHttpTranport.call(SOAP_ACTION_REQ, envelope);
                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("anyType{}")) {

                        SoapPrimitive sa;
                        try {
                            sa = (SoapPrimitive) envelope.getResponse();

                            inputpolicylist = sa.toString();

                            ParseXML prsObj = new ParseXML();

                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "PolicyDetails");
                            strPReqErrorCode = inputpolicylist;

                            if (strPReqErrorCode != null) {
                                inputpolicylist = sa.toString();

                                inputpolicylist = new ParseXML().parseXmlTag(
                                        inputpolicylist, "PolicyDetails");
                                inputpolicylist = new ParseXML().parseXmlTag(
                                        inputpolicylist, "ScreenData");
                                strReqErrorCode = inputpolicylist;

                                if (strReqErrorCode == null) {

                                    inputpolicylist = sa.toString();
                                    inputpolicylist = prsObj.parseXmlTag(
                                            inputpolicylist, "PolicyDetails");

                                    List<String> Node = prsObj.parseParentNode(
                                            inputpolicylist, "Table");

                                    List<XMLHolder_Req> nodeData = prsObj
                                            .parseNodeElementReq(Node);

                                    lsAgent_Req.clear();

                                    lsAgent_Req.addAll(nodeData);

                                    lstReqCount = lsAgent_Req.size();
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
                if (strPReqErrorCode == null) {
                    edt_search_proposal.setVisibility(View.GONE);

                    txterrordescreq.setText("No Record Found");
                    txtreqcount.setText("Total : " + 0);

                    selectedAdapterReq.notifyDataSetChanged();

                    //Utility.setListViewHeightBasedOnChildren(lstReq);
                } else {
                    if (strReqErrorCode == null) {
                        edt_search_proposal.setVisibility(View.VISIBLE);

                        txterrordescreq.setText("");
                        txtreqcount.setText("Total : " + lstReqCount);

                        selectedAdapterReq.notifyDataSetChanged();

                        //Utility.setListViewHeightBasedOnChildren(lstReq);
                    } else {
                        edt_search_proposal.setVisibility(View.GONE);

                        txterrordescreq.setText("No Record Found");
                        txtreqcount.setText("Total : " + 0);

                        selectedAdapterReq.notifyDataSetChanged();

                        //Utility.setListViewHeightBasedOnChildren(lstReq);
                    }
                }
            } else {
                mCommonMethods.showMessageDialog(context, mCommonMethods.SERVER_ERROR);
            }
        }
    }

    public class SelectedAdapterReq extends RecyclerView.Adapter<SelectedAdapterReq.ViewHolderAdapter> implements Filterable{

        private final Context mAdapterContext;
        private List<XMLHolder_Req> lstAdapterList, lstSearch;

        SelectedAdapterReq(Context mAdapterContext, List<XMLHolder_Req> lstAdapterList) {
            this.mAdapterContext = mAdapterContext;
            this.lstAdapterList = lstAdapterList;

            notifyDataSetChanged();
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<XMLHolder_Req> results = new ArrayList<>();

                    if (lstSearch == null)
                        lstSearch = lstAdapterList;

                    String strSearch = charSequence == null ? "":charSequence.toString();

                    if (!strSearch.equals("")) {
                        if (lstSearch != null && lstSearch.size() > 0) {
                            for (final XMLHolder_Req g : lstSearch) {
                                if (g.getProposalNo().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || g.getReqflag().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || g.getRaiseddate().toLowerCase().contains(charSequence.toString().toLowerCase()))
                                    results.add(g);
                            }
                        }
                        oReturn.values = results;
                    }else {
                        oReturn.values = lsAgent_Req;
                    }
                    return oReturn;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    lstAdapterList = (List<XMLHolder_Req>) results.values;
                    /*selectedAdapterReq = new SelectedAdapterReq(context, lstAdapterList);
                    lstReq.setAdapter(selectedAdapterReq);*/

                    notifyDataSetChanged();

                    txtreqcount.setText("Total : " + lstAdapterList.size());
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
                    R.layout.list_req, parent, false);

            // set the view's size, margins, paddings and layout parameters
            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(ViewHolderAdapter holder, int position) {

            holder.txtflag.setText(lstAdapterList.get(position).getDesc() == null ? "" : lstAdapterList.get(position).getDesc());
            holder.txtdesc.setText(lstAdapterList.get(position).getReqflag() == null ? "" : lstAdapterList.get(position).getReqflag());
            holder.txtcomnts.setText(lstAdapterList.get(position).getComments() == null ? "" : lstAdapterList.get(position).getComments());
            holder.txtdate.setText(lstAdapterList.get(position).getRaiseddate() == null ? "" : lstAdapterList.get(position).getRaiseddate());
            holder.txtpayamt.setText(lstAdapterList.get(position).getPayment() == null ? "" : lstAdapterList.get(position).getPayment());
            holder.txtpropno.setText(lstAdapterList.get(position).getProposalNo() == null ? "" : lstAdapterList.get(position).getProposalNo());
            holder.txtpayageing.setText(lstAdapterList.get(position).getPayAgeing() == null ? "" : lstAdapterList.get(position).getPayAgeing());
        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder{

            private final TextView txtdesc ,txtflag,txtcomnts,txtdate,txtpropno,txtpayageing,txtpayamt;

            ViewHolderAdapter(View v) {
                super(v);

                txtdesc = v.findViewById(R.id.txtdesc);
                txtflag = v.findViewById(R.id.txtreqflag);
                txtcomnts = v.findViewById(R.id.txtreqcomnt);
                txtdate = v.findViewById(R.id.txtraiseddate);
                txtpropno = v.findViewById(R.id.txtpropono);
                txtpayageing = v.findViewById(R.id.txtpayageing);
                txtpayamt = v.findViewById(R.id.txtreqcashamt);

                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        int pos = getLayoutPosition();

                        if (lstAdapterList.get(pos).getReqflag().equalsIgnoreCase("NON-MEDICAL")){
                            Intent intent = new Intent(getApplicationContext(), AllDocumentsUploadActivity.class);
                            intent.putExtra("PROPOSAL_NO", lstAdapterList.get(pos).getProposalNo() == null ? ""
                                    : lstAdapterList.get(pos).getProposalNo());
                            startActivity(intent);
                        }

                        return true;
                    }
                });
            }
        }
    }
}
