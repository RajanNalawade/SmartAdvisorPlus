package sbilife.com.pointofsale_bancaagency.reports.underwriting;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Spinner;
import android.widget.TextView;

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
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class SendLinkActivity extends AppCompatActivity implements View.OnClickListener, ServiceHits.DownLoadData {

    private final String METHOD_NAME = "getAnyProposalDetail_smrt";
    private ProgressDialog mProgressDialog;
    private ArrayList<ParseXML.SendSMSLinkValuesModel> globalDataList;
    private Context context;
    private EditText etProposalNumber, edittextSearch;
    private TextView textviewRecordCount;
    private RecyclerView recyclerview;
    private String proposalNumber = "";

    private ServiceHits service;
    private SelectedAdapter selectedAdapter;
    private DownloadSendSMSDataAsyncTask downloadSendSMSDataAsyncTask;
    private CommonMethods commonMethods;
    private String strCIFBDMUserId = "", strCIFBDMEmailId = "", strCIFBDMPassword = "",
            strCIFBDMMObileNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_send_link);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Send Link");

        etProposalNumber = findViewById(R.id.etProposalNumber);
        edittextSearch = findViewById(R.id.edittextSearch);
        Button buttonOk = findViewById(R.id.buttonOk);
        textviewRecordCount = findViewById(R.id.textviewRecordCount);

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

        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");

        if (fromHome != null && fromHome.equalsIgnoreCase("Y")) {
            getUserDetails();
        } else {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");
            try {
                strCIFBDMPassword = SimpleCrypto.encrypt("SBIL", "sbil");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        mProgressDialog.setButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        if (downloadSendSMSDataAsyncTask != null) {
                            downloadSendSMSDataAsyncTask.cancel(true);
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

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = commonMethods.getStrAuth();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.buttonOk:
                commonMethods.hideKeyboard(edittextSearch, context);

                clearList();
                proposalNumber = etProposalNumber.getText().toString().trim();
                if (commonMethods.isNetworkConnected(context)) {

                    if (TextUtils.isEmpty(proposalNumber)) {
                        commonMethods.showMessageDialog(context, "Please Enter Proposal Number");
                    } else {
                        service_hits(proposalNumber);
                    }
                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }
                break;
        }
    }

    private void service_hits(String input) {
        service = new ServiceHits(context, METHOD_NAME, input,
                strCIFBDMUserId, strCIFBDMEmailId,
                strCIFBDMMObileNo, strCIFBDMPassword, this);
        service.execute();
    }

    @Override
    public void downLoadData() {
        downloadSendSMSDataAsyncTask = new DownloadSendSMSDataAsyncTask();
        downloadSendSMSDataAsyncTask.execute();
    }

    class DownloadSendSMSDataAsyncTask extends AsyncTask<String, String, String> {

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
                //getAnyProposalDetail_smrt(string strCode, string strProposalNo, string strEmailId,
                // string strMobileNo, string strAuthKey)

                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("strCode", strCIFBDMUserId);
                request.addProperty("strProposalNo", proposalNumber);
                commonMethods.appendSecurityParams(context, request, strCIFBDMEmailId, strCIFBDMMObileNo);
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

                if (!response.toString().contentEquals("<NewDataSet />")) {

                    ParseXML prsObj = new ParseXML();
                    String inputpolicylist = response.toString();
                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "NewDataSet");
                    error = prsObj.parseXmlTag(
                            inputpolicylist, "ScreenData");

                    if (error == null) {
                        // for agent policy list
                        List<String> Node = prsObj.parseParentNode(inputpolicylist, "Table");
                        List<ParseXML.SendSMSLinkValuesModel> nodeData = prsObj
                                .parseNodeSendSMSLink(Node);
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
                    //edittextSearch.setVisibility(View.VISIBLE);
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

        private ArrayList<ParseXML.SendSMSLinkValuesModel> lstAdapterList, lstSearch;

        SelectedAdapter(ArrayList<ParseXML.SendSMSLinkValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<ParseXML.SendSMSLinkValuesModel> results = new ArrayList<>();

                    if (lstSearch == null)
                        lstSearch = lstAdapterList;

                    String strSearch = charSequence == null ? "" : charSequence.toString();

                    if (!strSearch.equals("")) {
                        if (lstSearch != null && lstSearch.size() > 0) {
                            for (final ParseXML.SendSMSLinkValuesModel model : lstSearch) {
                                if (model.getEMAILID().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getNAME().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getMOBILE().toLowerCase().contains(charSequence.toString().toLowerCase())) {
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

                    lstAdapterList = (ArrayList<ParseXML.SendSMSLinkValuesModel>) results.values;
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
                    R.layout.list_item_send_sms_link, parent, false);

            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final ViewHolderAdapter holder, final int position) {


            holder.textviewProposalNumber.setText(proposalNumber);
            holder.textviewCustomerName.setText(lstAdapterList.get(position).getNAME());
            holder.textMobileNumber.setText(lstAdapterList.get(position).getMOBILE());

            holder.textMobileNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mobileNumber = holder.textMobileNumber.getText().toString();
                    if (!TextUtils.isEmpty(mobileNumber)) {
                        commonMethods.callMobileNumber(mobileNumber, context);
                    }
                }
            });
            //Consent for Extra Premium
            /*String[] ls_type = {"Select Type", "Consent for Extra Premium/Shortage/Revised BI",
                    "Payment Shortage",
                    "Questionnaire"};
            ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                    context, R.layout.spinner_item, ls_type);
            typeAdapter.setDropDownViewResource(R.layout.spinner_item1);
            holder.spinnerTypeSendSMSLink.setAdapter(typeAdapter);
            typeAdapter.notifyDataSetChanged();*/

            holder.spinnerTypeSendSMSLink.setAdapter(ArrayAdapter.createFromResource(context,
                    R.array.sendLinkArray,
                    R.layout.spinner_large_words));

            holder.buttonCallMobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mobileNumber = holder.textMobileNumber.getText().toString();
                    if (!TextUtils.isEmpty(mobileNumber)) {
                        commonMethods.callMobileNumber(mobileNumber, context);
                    }
                }
            });

            holder.buttonSendLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String mobileNumber = holder.textMobileNumber.getText().toString();

                    if (!TextUtils.isEmpty(mobileNumber)) {

                        final String typeString = holder.spinnerTypeSendSMSLink.getSelectedItem().toString();

                        if (typeString.equalsIgnoreCase("Select Type")) {
                            commonMethods.showMessageDialog(context, "Please select type");
                        } else {
                            final Dialog dialog = new Dialog(context);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.loading_window_twobutton);
                            TextView text = dialog.findViewById(R.id.txtalertheader);
                            text.setText("Do you want to send Link.");
                            Button dialogButton = dialog.findViewById(R.id.btnalert);
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    try {
                                        SendSmsLinkAsyncTask sendSmsALternateProcessAsyncTask
                                                = new SendSmsLinkAsyncTask(mobileNumber, typeString);
                                        sendSmsALternateProcessAsyncTask.execute();
                                    } catch (Exception e) {
                                        commonMethods.showMessageDialog(context, "Problem in sending Link, please Try Again");
                                    }
                                }
                            });
                            Button dialogButtoncancel = dialog.findViewById(R.id.btnalertcancel);
                            dialogButtoncancel.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();
                        }


                    }
                }
            });
        }


        public class ViewHolderAdapter extends RecyclerView.ViewHolder {
            private final TextView textviewProposalNumber, textviewCustomerName, textMobileNumber;
            private Button buttonSendLink, buttonCallMobile;
            private Spinner spinnerTypeSendSMSLink;

            ViewHolderAdapter(View v) {
                super(v);
                textviewProposalNumber = v.findViewById(R.id.textviewProposalNumber);
                textviewCustomerName = v.findViewById(R.id.textviewCustomerName);
                textMobileNumber = v.findViewById(R.id.textMobileNumber);
                buttonSendLink = v.findViewById(R.id.buttonSendLink);
                buttonCallMobile = v.findViewById(R.id.buttonCallMobile);
                spinnerTypeSendSMSLink = v.findViewById(R.id.spinnerTypeSendSMSLink);
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

        if (downloadSendSMSDataAsyncTask != null) {
            downloadSendSMSDataAsyncTask.cancel(true);
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

    public class SendSmsLinkAsyncTask extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String response = "";
        private final String mobileNumber;
        private String typeString;

        public SendSmsLinkAsyncTask(String mobileNumber, String typeString) {
            this.mobileNumber = mobileNumber;
            this.typeString = typeString;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {
                // sendLink_smrt(string strProposalNo, string strMobile,string strType ,
                // string strEmailId, string strMobileNo, string strAuthKey)

                running = true;
                String NAMESPACE = "http://tempuri.org/";
                String METHOD_NAME_SEND_SMS = "sendLink_smrt";

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SEND_SMS);
                request.addProperty("strProposalNo", proposalNumber);
                request.addProperty("strMobile", mobileNumber);// mobileNumber);

                if (typeString.equalsIgnoreCase("Consent for Extra Premium/Shortage/Revised BI")) {
                    request.addProperty("strType", "Consent for Extra Premium");
                } else {
                    request.addProperty("strType", typeString);
                }
                // mobileNumber);
                commonMethods.appendSecurityParams(context, request, strCIFBDMEmailId, strCIFBDMMObileNo);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                new CommonMethods().TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME_SEND_SMS;
                androidHttpTranport.call(SOAP_ACTION, envelope);

                SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();
                response = sa.toString();
                /*if (response.equalsIgnoreCase("1")) {
                    response = "success";
                } else {
                    response = "";
                }*/
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

            if (running) {
                if (response.equalsIgnoreCase("1")) {
                    commonMethods.showMessageDialog(context, "SMS sent successfully");
                } else if (response.equalsIgnoreCase("2")) {
                    if (typeString.equalsIgnoreCase("Questionnaire")) {
                        commonMethods.showMessageDialog(context, "There is no requirement for Questionnaire");
                    } else if (typeString.equalsIgnoreCase("Consent for Extra Premium/Shortage/Revised BI")) {
                        commonMethods.showMessageDialog(context, "There is no requirement for Consent");
                    } else if (typeString.equalsIgnoreCase("COVID Questionnaire")) {
                        commonMethods.showMessageDialog(context, "There is no requirement for COVID Questionnaire");
                    } else {
                        commonMethods.showMessageDialog(context, "There is no pending shortage.");
                    }

                } else if (response.equalsIgnoreCase("3")) {
                    commonMethods.showMessageDialog(context, "There is no requirement");
                } else {
                    commonMethods.showMessageDialog(context, "SMS sending failed");
                }
            } else {
                commonMethods.showMessageDialog(context, "SMS sending failed");
            }
        }
    }
}
