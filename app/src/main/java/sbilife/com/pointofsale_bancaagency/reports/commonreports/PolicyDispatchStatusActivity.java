package sbilife.com.pointofsale_bancaagency.reports.commonreports;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class PolicyDispatchStatusActivity extends AppCompatActivity implements View.OnClickListener, ServiceHits.DownLoadData {

    private final String METHOD_NAME = "getPolicyDispatchStatus";
    private ProgressDialog mProgressDialog;
    private ArrayList<ParseXML.PolicyDispathStatusValuesModel> globalDataList;
    private Context context;
    private CommonMethods commonMethods;
    private EditText etPolicyNumber, edittextSearch;
    private TextView textviewRecordCount;
    private RecyclerView recyclerview;
    private String policyNumber = "";

    private ServiceHits service;
    private SelectedAdapter selectedAdapter;
    private DownloadPolicyDispatchAsyncTask downloadPolicyDispatchAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_policy_dispatch_status);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Policy Dispatch Status");

        etPolicyNumber = findViewById(R.id.etPolicyNumber);
        edittextSearch = findViewById(R.id.edittextSearch);
        Button buttonOk = findViewById(R.id.buttonOk);
        textviewRecordCount = findViewById(R.id.textviewRecordCount);
        TextView txterrordesc = findViewById(R.id.txterrordesc);

        buttonOk.setOnClickListener(this);
        recyclerview = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        // set LayoutManager to RecyclerView
        recyclerview.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        globalDataList = new ArrayList<>();
        selectedAdapter = new SelectedAdapter(globalDataList);
        recyclerview.setAdapter(selectedAdapter);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

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

        mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        mProgressDialog.setButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        if (downloadPolicyDispatchAsyncTask != null) {
                            downloadPolicyDispatchAsyncTask.cancel(true);
                        }

                        if (mProgressDialog != null) {
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }
                    }
                });

        mProgressDialog.setMax(100);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.buttonOk:
                commonMethods.hideKeyboard(edittextSearch, context);

                clearList();
                policyNumber = etPolicyNumber.getText().toString().trim();

                if (commonMethods.isNetworkConnected(context)) {

                    if (TextUtils.isEmpty(policyNumber)) {
                        commonMethods.showMessageDialog(context, "Please Enter Policy Number");
                    } else {
                        service_hits(policyNumber);
                    }
                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }
                break;
        }
    }

    private void service_hits(String input) {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        String strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        String strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        String strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();

        String strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
        service = new ServiceHits(context, METHOD_NAME, input,
                strCIFBDMUserId, strCIFBDMEmailId,
                strCIFBDMMObileNo, strCIFBDMPassword, this);
        service.execute();
    }

    @Override
    public void downLoadData() {
        downloadPolicyDispatchAsyncTask = new DownloadPolicyDispatchAsyncTask();
        downloadPolicyDispatchAsyncTask.execute();
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

        if (downloadPolicyDispatchAsyncTask != null) {
            downloadPolicyDispatchAsyncTask.cancel(true);
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

    class DownloadPolicyDispatchAsyncTask extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();

            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }


        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;

                /*getPolicyDispatchStatus(string strPolicy)  */


                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("strPolicy", policyNumber);


                commonMethods.TLSv12Enable();

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();


                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;
                androidHttpTranport.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();

                if (!response.toString().contentEquals("<CIFPolicyList />")) {

                    ParseXML prsObj = new ParseXML();
                    String inputpolicylist = response.toString();
                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "AllDispatchdetails");
                    error = prsObj.parseXmlTag(
                            inputpolicylist, "ScreenData");

                    if (error == null) {
                        // for agent policy list
                        List<String> Node = prsObj.parseParentNode(inputpolicylist, "AllDispatchValues");
                        List<ParseXML.PolicyDispathStatusValuesModel> nodeData = prsObj
                                .parseNodePolicyDispatchStatus(Node);
                        globalDataList.clear();
                        globalDataList.addAll(nodeData);
                        error = "success";
                    } else {
                        error = "1";
                    }

                } else {
                    error = "1";
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
            edittextSearch.setVisibility(View.GONE);
            if (running) {
                if (error.equalsIgnoreCase("success")) {
                    textviewRecordCount.setText("Total Record :" + globalDataList.size());
                    edittextSearch.setVisibility(View.VISIBLE);
                    selectedAdapter = new SelectedAdapter(globalDataList);
                    recyclerview.setAdapter(selectedAdapter);
                    recyclerview.invalidate();
                } else {
                    commonMethods.showMessageDialog(context, "No Record found");
                    clearList();
                }
            } else {
                commonMethods.showMessageDialog(context, "No Record found");
                clearList();
            }
        }
    }

    public class SelectedAdapter extends RecyclerView.Adapter<SelectedAdapter.
            ViewHolderAdapter> implements Filterable {

        private ArrayList<ParseXML.PolicyDispathStatusValuesModel> lstAdapterList, lstSearch;

        SelectedAdapter(ArrayList<ParseXML.PolicyDispathStatusValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<ParseXML.PolicyDispathStatusValuesModel> results = new ArrayList<>();

                    if (lstSearch == null)
                        lstSearch = lstAdapterList;

                    String strSearch = charSequence == null ? "" : charSequence.toString();

                    if (!strSearch.equals("")) {
                        if (lstSearch != null && lstSearch.size() > 0) {
                            for (final ParseXML.PolicyDispathStatusValuesModel model : lstSearch) {
                                if (model.getREPORT_TYPE().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getPROPOSALNO().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getDOC_TYPE().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getDESP_DATE().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getCHEQUENO().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getPOLICYNO().toLowerCase().contains(charSequence.toString().toLowerCase())

                                        || model.getCHEQUEAMOUNT().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getCHEQUEDATE().toLowerCase().contains(charSequence.toString().toLowerCase())
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

                    lstAdapterList = (ArrayList<ParseXML.PolicyDispathStatusValuesModel>) results.values;
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
        public ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_item_policy_dispatch_status, parent, false);

            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final ViewHolderAdapter holder, final int position) {


            holder.textviewPolicyNumber.setText(lstAdapterList.get(position).getPOLICYNO());
            holder.textReportType.setText(lstAdapterList.get(position).getREPORT_TYPE());
            holder.textDocType.setText(lstAdapterList.get(position).getDOC_TYPE());
            holder.textProposalNumber.setText(lstAdapterList.get(position).getPROPOSALNO());

            holder.textDispatchTo.setText(lstAdapterList.get(position).getDISPATCH_TO());

            holder.textDispatchThrough.setText(lstAdapterList.get(position).getDISPATCH_THROUGH());

            holder.textAWBNumber.setText(lstAdapterList.get(position).getAWBNO());


            holder.textDespDate.setText(lstAdapterList.get(position).getDESP_DATE());

            holder.textChequeNumber.setText(lstAdapterList.get(position).getCHEQUENO());

            holder.textChequeDate.setText(lstAdapterList.get(position).getCHEQUEDATE());
            holder.textStatus.setText(lstAdapterList.get(position).getSTATUS());



           /* holder.linearlayoutAWBNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dispatchThrough = lstAdapterList.get(position).getDISPATCH_THROUGH().toLowerCase();
                    gotoLink(dispatchThrough, holder.textAWBNumber.getText().toString());
                }
            });*/
           /* holder.textAWBNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dispatchThrough = lstAdapterList.get(position).getDISPATCH_THROUGH().toLowerCase();
                    gotoLink(dispatchThrough, holder.textAWBNumber.getText().toString());
                }
            });*/

            holder.imageviewLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dispatchThrough = lstAdapterList.get(position).getDISPATCH_THROUGH().toLowerCase();
                    gotoLink(dispatchThrough);
                }
            });
        }

        private void gotoLink(String dispatchThrough) {

            if (dispatchThrough.equalsIgnoreCase("Blue Dart")
                    || dispatchThrough.equalsIgnoreCase("BlueDart")) {
                commonMethods.openWebLink(context, "https://www.bluedart.com/tracking");
            } else if (dispatchThrough.equalsIgnoreCase("Speed Post")
                    || dispatchThrough.equalsIgnoreCase("SpeedPost")
                    || dispatchThrough.equalsIgnoreCase("EMS Speed Post")
                    || dispatchThrough.equalsIgnoreCase("EMS SpeedPost")) {
                commonMethods.openWebLink(context, "https://www.indiapost.gov.in/_layouts/15/dop.portal.tracking/trackconsignment.aspx");
            }
        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder {


            private final TextView textviewPolicyNumber, textReportType, textDocType, textProposalNumber, textDispatchTo,
                    textDispatchThrough, textAWBNumber, textDespDate, textChequeNumber, textChequeDate, textStatus;

            //private LinearLayout linearlayoutAWBNumber;
            private ImageView imageviewLink;
            ViewHolderAdapter(View v) {
                super(v);
                textviewPolicyNumber = v.findViewById(R.id.textviewPolicyNumber);
                textReportType = v.findViewById(R.id.textReportType);
                textDocType = v.findViewById(R.id.textDocType);
                textProposalNumber = v.findViewById(R.id.textProposalNumber);
                textDispatchTo = v.findViewById(R.id.textDispatchTo);

                textDispatchThrough = v.findViewById(R.id.textDispatchThrough);
                textAWBNumber = v.findViewById(R.id.textAWBNumber);
                textDespDate = v.findViewById(R.id.textDespDate);
                textChequeNumber = v.findViewById(R.id.textChequeNumber);
                textChequeDate = v.findViewById(R.id.textChequeDate);
                textStatus = v.findViewById(R.id.textStatus);
                imageviewLink = v.findViewById(R.id.imageviewLink);
                //linearlayoutAWBNumber = v.findViewById(R.id.linearlayoutAWBNumber);
            }

        }

    }
}
