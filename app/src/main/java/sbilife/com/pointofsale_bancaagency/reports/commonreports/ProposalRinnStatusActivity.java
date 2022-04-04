package sbilife.com.pointofsale_bancaagency.reports.commonreports;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class ProposalRinnStatusActivity extends AppCompatActivity implements View.OnClickListener,
        ServiceHits.DownLoadData {

    // private ListView listviewAutoMandateStatus;
    private EditText edittextFormNumner, edittextSearch;

    private ProgressDialog mProgressDialog;
    private final String SOAP_ACTION_PROPOSAL_RINN_STATUS = "http://tempuri.org/getProposalStatusRinn_smrt";
    private final String METHOD_NAME_PROPOSAL_RINN_STATUS = "getProposalStatusRinn_smrt";

    private final String NAMESPACE = "http://tempuri.org/";
    private final String URl = ServiceURL.SERVICE_URL;

    private Context context;
    private CommonMethods commonMethods;
    private String formNumber = "";

    private DownloadProposalRinnStatusAsync downloadProposalRinnStatusAsync;
    private ServiceHits service;

    private ArrayList<ParseXML.ProposalRinnStatusValuesModel> proposalRinnStatusGlobalList;
    private SelectedAdapterProposalRinnStatusList selectedAdapterProposalRinnStatusList;
    private TextView textviewRecordCount;
    private RecyclerView recyclerviewPiwcRinnStatus;
    private TextView txterrordesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_piwc_rinn_status);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Rinn Raksha Proposal Tracker");

        TextView textviewFormNumnerTitle = findViewById(R.id.textviewFormNumnerTitle);
        textviewFormNumnerTitle.setText("Enter Form / Loan Account Number");

        Button buttonPiwcRinnStatus = findViewById(R.id.buttonPiwcRinnStatus);
        edittextFormNumner = findViewById(R.id.edittextFormNumner);
        edittextFormNumner.setHint("Enter Form / Loan Account Number");

        edittextSearch = findViewById(R.id.edittextSearch);
        textviewRecordCount = findViewById(R.id.textviewRecordCount);
        recyclerviewPiwcRinnStatus = findViewById(R.id.recyclerviewPiwcRinnStatus);
        txterrordesc = findViewById(R.id.txterrordesc);

        //lstReq.setHasFixedSize(true);
        // set a LinearLayoutManager with default horizontal orientation and false value for reverseLayout to show the items from start to end
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        // set LayoutManager to RecyclerView
        recyclerviewPiwcRinnStatus.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        proposalRinnStatusGlobalList = new ArrayList<>();
        selectedAdapterProposalRinnStatusList = new SelectedAdapterProposalRinnStatusList(
                proposalRinnStatusGlobalList);
        recyclerviewPiwcRinnStatus.setAdapter(selectedAdapterProposalRinnStatusList);
        recyclerviewPiwcRinnStatus.setItemAnimator(new DefaultItemAnimator());


        buttonPiwcRinnStatus.setOnClickListener(this);
        edittextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                selectedAdapterProposalRinnStatusList.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    @Override
    public void downLoadData() {
        downloadProposalRinnStatusAsync = new DownloadProposalRinnStatusAsync();
        downloadProposalRinnStatusAsync.execute();
    }

    class DownloadProposalRinnStatusAsync extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String strAutoMandateStatusListError = "";
        private int lstAutoMandateCount = 0;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading Please wait...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);

            mProgressDialog.setButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            if (downloadProposalRinnStatusAsync != null) {
                                downloadProposalRinnStatusAsync.cancel(true);
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
                request = new SoapObject(NAMESPACE,
                        METHOD_NAME_PROPOSAL_RINN_STATUS);
                request.addProperty("strFormNo", formNumber);

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();


                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    androidHttpTranport.call(
                            SOAP_ACTION_PROPOSAL_RINN_STATUS, envelope);
                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("<NewDataSet />")) {
                        System.out.println("response:" + response.toString());
                        SoapPrimitive sa;
                        try {
                            sa = (SoapPrimitive) envelope.getResponse();

                            String inputpolicylist = sa.toString();

                            ParseXML prsObj = new ParseXML();

                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "ProposalDetail");
                            strAutoMandateStatusListError = inputpolicylist;

                            if (inputpolicylist != null) {
                                // for agent policy list
                                List<String> Node = prsObj.parseParentNode(
                                        inputpolicylist, "Table");

                                List<ParseXML.ProposalRinnStatusValuesModel> nodeData = prsObj
                                        .parseNodeProposalRinnStatusTracker(Node);
                                proposalRinnStatusGlobalList.clear();
                                proposalRinnStatusGlobalList.addAll(nodeData);
                                lstAutoMandateCount = proposalRinnStatusGlobalList.size();
                            }

                        } catch (Exception e) {
                            mProgressDialog.dismiss();
                            running = false;
                        }
                    }

                } catch (IOException | XmlPullParserException e) {
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
                    txterrordesc.setText("");
                    textviewRecordCount.setText("Total Record : "
                            + lstAutoMandateCount);
                    selectedAdapterProposalRinnStatusList = new SelectedAdapterProposalRinnStatusList(proposalRinnStatusGlobalList);
                    recyclerviewPiwcRinnStatus.setAdapter(selectedAdapterProposalRinnStatusList);
                    recyclerviewPiwcRinnStatus.invalidate();
                } else {

                    txterrordesc.setText("No Record Found");
                    textviewRecordCount.setText("Total Record : " + 0);
                    clearList();
                }
            } else {
                commonMethods.showMessageDialog(context,
                        "Server Not Responding,Try again..");
            }
        }
    }

    private void service_hits() {
        String strCIFBDMUserId, strCIFBDMEmailId,
                strCIFBDMPassword, strCIFBDMMObileNo;
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods
                .setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

        service = new ServiceHits(context,
                METHOD_NAME_PROPOSAL_RINN_STATUS, formNumber,
                strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonPiwcRinnStatus:
                formNumber = edittextFormNumner.getText().toString();
                commonMethods.hideKeyboard(edittextFormNumner, context);

                clearList();
                textviewRecordCount.setVisibility(View.GONE);
                txterrordesc.setVisibility(View.GONE);
                if (commonMethods.isNetworkConnected(context)) {

                    if (!TextUtils.isEmpty(formNumber)) {
                        service_hits();
                    } else {
                        commonMethods.showMessageDialog(context, "Please Enter Form Number");
                    }
                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }
                break;
        }
    }

    private void clearList() {
        if (proposalRinnStatusGlobalList != null && selectedAdapterProposalRinnStatusList != null) {
            proposalRinnStatusGlobalList.clear();
            selectedAdapterProposalRinnStatusList = new SelectedAdapterProposalRinnStatusList(proposalRinnStatusGlobalList);
            recyclerviewPiwcRinnStatus.setAdapter(selectedAdapterProposalRinnStatusList);
            recyclerviewPiwcRinnStatus.invalidate();
        }
    }

    public class SelectedAdapterProposalRinnStatusList extends RecyclerView.Adapter<SelectedAdapterProposalRinnStatusList.
            ViewHolderAdapter> implements Filterable {

        private ArrayList<ParseXML.ProposalRinnStatusValuesModel> lstAdapterList, lstSearch;

        SelectedAdapterProposalRinnStatusList(ArrayList<ParseXML.ProposalRinnStatusValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<ParseXML.AutoMandateStatusListValuesModel> results = new ArrayList<>();

                    if (lstSearch == null)
                        lstSearch = lstAdapterList;

                    String strSearch = charSequence == null ? "" : charSequence.toString();

                    if (!strSearch.equals("")) {
                        if (lstSearch != null && lstSearch.size() > 0) {
                            for (final ParseXML.ProposalRinnStatusValuesModel model : lstSearch) {
                                /*if (model.getPOLICY_NO().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getSTATUS_UPDATE_DATE().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getPOLICY_STATUS().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getPOL_HOLDER_NAME().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                    results.add(model);
                                }*/
                            }
                        }
                        oReturn.values = results;
                    } else {
                        oReturn.values = proposalRinnStatusGlobalList;
                    }
                    return oReturn;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    lstAdapterList = (ArrayList<ParseXML.ProposalRinnStatusValuesModel>) results.values;
                    selectedAdapterProposalRinnStatusList = new SelectedAdapterProposalRinnStatusList(lstAdapterList);
                    recyclerviewPiwcRinnStatus.setAdapter(selectedAdapterProposalRinnStatusList);

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
                    R.layout.list_item_proposal_rinn_status, parent, false);

            // set the view's size, margins, paddings and layout parameters
            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(ViewHolderAdapter holder, int position) {
            holder.textviewPiwcRinnStatusFormNo.setText(lstAdapterList.get(position).getFORM_NO() == null ? ""
                            : lstAdapterList.get(position).getFORM_NO());

            holder.textviewPiwcRinnStatusLoanAccountNumber.setText(lstAdapterList.get(position).getLAN() == null ? "" : lstAdapterList.get(
                    position).getLAN());
            holder.textviewPiwcRinnStatusPolicyHolderName.setText(lstAdapterList.get(position).getPOLICY_HOLDER_NAME() == null ? "" : lstAdapterList.get(
                    position).getPOLICY_HOLDER_NAME());

            holder.textviewPiwcRinnStatusStatus.setText(lstAdapterList.get(position).getSTATUS() == null ? "" : lstAdapterList.get(
                    position).getSTATUS());

            holder.textviewPiwcRinnStatusLoanCategory .setText(lstAdapterList.get(position).getLOAN_CATEG() == null ? "" : lstAdapterList.get(
                    position).getLOAN_CATEG());


            holder.textviewPiwcRinnStatusRequiredType.setText(lstAdapterList.get(position).getREQ_TYPE() == null ? "" : lstAdapterList.get(
                    position).getREQ_TYPE());

            //Non medical
            holder.textviewPiwcRinnStatusNonMedicalRequirement.setText(lstAdapterList.get(position).getNON_MED_DESC() == null ? "" : lstAdapterList.get(
                    position).getNON_MED_DESC());
            holder.textviewPiwcRinnStatusNonMedicalDescription.setText(lstAdapterList.get(position).getNON_MED_COMMENTS() == null ? "" : lstAdapterList.get(
                    position).getNON_MED_COMMENTS());

            //medical Requirement
            holder.textviewPiwcRinnStatusMedicalRequirement.setText(lstAdapterList.get(position).getMED_DESC() == null ? "" : lstAdapterList.get(
                    position).getMED_DESC());
            holder.textviewPiwcRinnStatusMedicalDescription.setText(lstAdapterList.get(position).getMED_COMMENTS() == null ? "" : lstAdapterList.get(
                    position).getMED_COMMENTS());

            holder.textviewPiwcRinnStatusBorrowerType.setText(lstAdapterList.get(position).getBORROWER_TYPE() == null ? "" : lstAdapterList.get(
                    position).getBORROWER_TYPE());
            holder.textviewPiwcRinnStatusBankName.setText(lstAdapterList.get(position).getBANK_NAME() == null ? "" : lstAdapterList.get(
                    position).getBANK_NAME());
            holder.textviewPiwcRinnStatusBranchName.setText(lstAdapterList.get(position).getBRANCH_NAME() == null ? "" : lstAdapterList.get(
                    position).getBRANCH_NAME());

            holder.textviewPiwcRinnStatusPIWCStatus.setText(lstAdapterList.get(position).getPIWC_STATUS() == null ? "" : lstAdapterList.get(
                    position).getPIWC_STATUS());
            holder.textviewPiwcRinnStatusPIWCSubStatus.setText(lstAdapterList.get(position).getPIWC_SUB_STATUS() == null ? "" : lstAdapterList.get(
                    position).getPIWC_SUB_STATUS());


        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder {

            private final TextView textviewPiwcRinnStatusFormNo, textviewPiwcRinnStatusLoanAccountNumber,
                    textviewPiwcRinnStatusStatus,textviewPiwcRinnStatusLoanCategory,
                    textviewPiwcRinnStatusRequiredType,textviewPiwcRinnStatusNonMedicalRequirement,
                    textviewPiwcRinnStatusMedicalRequirement,textviewPiwcRinnStatusPolicyHolderName,
                    textviewPiwcRinnStatusNonMedicalDescription,textviewPiwcRinnStatusBorrowerType,
                     textviewPiwcRinnStatusBankName,textviewPiwcRinnStatusBranchName,
                    textviewPiwcRinnStatusMedicalDescription,textviewPiwcRinnStatusPIWCStatus,
                    textviewPiwcRinnStatusPIWCSubStatus;

            ViewHolderAdapter(View v) {
                super(v);
                textviewPiwcRinnStatusFormNo = v.findViewById(R.id.textviewPiwcRinnStatusFormNo);
                textviewPiwcRinnStatusStatus = v.findViewById(R.id.textviewPiwcRinnStatusStatus);
                textviewPiwcRinnStatusLoanAccountNumber = v.findViewById(R.id.textviewPiwcRinnStatusLoanAccountNumber);
                textviewPiwcRinnStatusLoanCategory = v.findViewById(R.id.textviewPiwcRinnStatusLoanCategory);
                textviewPiwcRinnStatusRequiredType = v.findViewById(R.id.textviewPiwcRinnStatusRequiredType);
                textviewPiwcRinnStatusNonMedicalRequirement = v.findViewById(R.id.textviewPiwcRinnStatusNonMedicalRequirement);
                textviewPiwcRinnStatusMedicalRequirement = v.findViewById(R.id.textviewPiwcRinnStatusMedicalRequirement);
                textviewPiwcRinnStatusPolicyHolderName = v.findViewById(R.id.textviewPiwcRinnStatusPolicyHolderName);
                textviewPiwcRinnStatusNonMedicalDescription = v.findViewById(R.id.textviewPiwcRinnStatusNonMedicalDescription);
                textviewPiwcRinnStatusBorrowerType = v.findViewById(R.id.textviewPiwcRinnStatusBorrowerType);
                textviewPiwcRinnStatusBankName = v.findViewById(R.id.textviewPiwcRinnStatusBankName);
                textviewPiwcRinnStatusBranchName = v.findViewById(R.id.textviewPiwcRinnStatusBranchName);
                textviewPiwcRinnStatusMedicalDescription = v.findViewById(R.id.textviewPiwcRinnStatusMedicalDescription);
                textviewPiwcRinnStatusPIWCStatus = v.findViewById(R.id.textviewPiwcRinnStatusPIWCStatus);
                textviewPiwcRinnStatusPIWCSubStatus = v.findViewById(R.id.textviewPiwcRinnStatusPIWCSubStatus);

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

        if (downloadProposalRinnStatusAsync != null) {
            downloadProposalRinnStatusAsync.cancel(true);
        }
        if (service != null) {
            service.cancel(true);
        }
    }

}

