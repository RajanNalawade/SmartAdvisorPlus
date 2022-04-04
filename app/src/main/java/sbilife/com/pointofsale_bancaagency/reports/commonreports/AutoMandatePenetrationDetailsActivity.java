package sbilife.com.pointofsale_bancaagency.reports.commonreports;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
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
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

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

public class AutoMandatePenetrationDetailsActivity extends AppCompatActivity implements
        ServiceHits.DownLoadData {
    private final String METHOD_NAME_AUTO_MANDATE_PENETRATION_DETAILS_LIST = "getMandate_Penetration_detail_smrt";
    private String URl = ServiceURL.SERVICE_URL;
    private String NAMESPACE = "http://tempuri.org/";

    private RecyclerView recyclerviewAutoMandatePenetrationDetails;
    private EditText edt_search_recyclerview;
    private Context context;
    private CommonMethods commonMethods;
    private ProgressDialog mProgressDialog;
    private DownloadMandatePenetratonDetailsListAsync downloadMandatePenetratonDetailsListAsync;

    private String strType = "", strCIFBDMUserId = "", strCIFBDMEmailId = "",
            strCIFBDMPassword = "", strCIFBDMMObileNo = "", strFromDate = "", strToDate = "";

    private ArrayList<ParseXML.AutoMandatePenetrationDetailsValuesModel> mandatePenetrationDetailsList;

    private ServiceHits service;
    private SelectedAdapterMandatePenetrationDetailsList selectedAdapterMandatePenetrationDetailsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_auto_mandate_penetration_details);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Auto Mandate Penetration Details");

        Intent intent = getIntent();

        String fromHome = intent.getStringExtra("fromHome");
        strToDate = intent.getStringExtra("strToDate");
        strFromDate = intent.getStringExtra("strFromDate");
        strType = intent.getStringExtra("strType");

        if (fromHome != null && fromHome.equalsIgnoreCase("N")) {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");
            try {
                strCIFBDMPassword = SimpleCrypto.encrypt("SBIL", "sbil");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            getUserDetails();
        }


        recyclerviewAutoMandatePenetrationDetails = findViewById(R.id.recyclerviewAutoMandatePenetrationDetails);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerviewAutoMandatePenetrationDetails.setLayoutManager(linearLayoutManager);
        mandatePenetrationDetailsList = new ArrayList<>();
        selectedAdapterMandatePenetrationDetailsList = new SelectedAdapterMandatePenetrationDetailsList(
                mandatePenetrationDetailsList);
        recyclerviewAutoMandatePenetrationDetails.setAdapter(selectedAdapterMandatePenetrationDetailsList);
        recyclerviewAutoMandatePenetrationDetails.setItemAnimator(new DefaultItemAnimator());

        edt_search_recyclerview = findViewById(R.id.edt_search_recyclerview);
        edt_search_recyclerview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                selectedAdapterMandatePenetrationDetailsList.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        service_hits();
    }

    @Override
    public void downLoadData() {
        downloadMandatePenetratonDetailsListAsync = new DownloadMandatePenetratonDetailsListAsync();
        downloadMandatePenetratonDetailsListAsync.execute();
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
                METHOD_NAME_AUTO_MANDATE_PENETRATION_DETAILS_LIST, strType + "," + strFromDate + "," + strToDate,
                strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();
    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }

    class DownloadMandatePenetratonDetailsListAsync extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String strAutoMandateStatusListError = "";

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

                            if (downloadMandatePenetratonDetailsListAsync != null) {
                                downloadMandatePenetratonDetailsListAsync.cancel(true);
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

                /*string strCifNo, string strEmailId, string strMobileNo, string strAuthKey,
                        string strFromDate, string strToDate, string strType*/


                request = new SoapObject(NAMESPACE, METHOD_NAME_AUTO_MANDATE_PENETRATION_DETAILS_LIST);
                request.addProperty("strCifNo", strCIFBDMUserId);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());
                request.addProperty("strFromDate", strFromDate);
                request.addProperty("strToDate", strToDate);
                request.addProperty("strType", strType);
                commonMethods.TLSv12Enable();

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();


                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION_AUTO_MANDATE_PENETRATION_DETAILS_LIST = "http://tempuri.org/getMandate_Penetration_detail_smrt";
                androidHttpTranport.call(SOAP_ACTION_AUTO_MANDATE_PENETRATION_DETAILS_LIST, envelope);
                Object response = envelope.getResponse();

                if (!response.toString().contentEquals("<NewDataSet />")) {
                    System.out.println("response:" + response.toString());
                    ParseXML prsObj = new ParseXML();
                    String inputpolicylist = response.toString();
                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "CIFPolicyList");

                    if (inputpolicylist != null) {
                        // for agent policy list
                        List<String> Node = prsObj.parseParentNode(inputpolicylist, "Table");
                        List<ParseXML.AutoMandatePenetrationDetailsValuesModel> nodeData = prsObj
                                .parseNodeAutoMandatePenetrationDetails(Node);
                        mandatePenetrationDetailsList.clear();
                        mandatePenetrationDetailsList.addAll(nodeData);
                        strAutoMandateStatusListError = "success";
                    } else {
                        strAutoMandateStatusListError = "1";
                    }

                } else {
                    strAutoMandateStatusListError = "1";
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
            edt_search_recyclerview.setVisibility(View.GONE);
            if (running) {
                if (strAutoMandateStatusListError.equalsIgnoreCase("success")) {
                    edt_search_recyclerview.setVisibility(View.VISIBLE);
                    selectedAdapterMandatePenetrationDetailsList = new SelectedAdapterMandatePenetrationDetailsList(mandatePenetrationDetailsList);
                    recyclerviewAutoMandatePenetrationDetails.setAdapter(selectedAdapterMandatePenetrationDetailsList);
                    recyclerviewAutoMandatePenetrationDetails.invalidate();
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

    private void clearList() {
        if (mandatePenetrationDetailsList != null && selectedAdapterMandatePenetrationDetailsList != null) {
            mandatePenetrationDetailsList.clear();
            selectedAdapterMandatePenetrationDetailsList = new SelectedAdapterMandatePenetrationDetailsList(mandatePenetrationDetailsList);
            recyclerviewAutoMandatePenetrationDetails.setAdapter(selectedAdapterMandatePenetrationDetailsList);
            recyclerviewAutoMandatePenetrationDetails.invalidate();
        }
    }

    public class SelectedAdapterMandatePenetrationDetailsList extends RecyclerView.Adapter<SelectedAdapterMandatePenetrationDetailsList.ViewHolderAdapter> implements Filterable {

        private ArrayList<ParseXML.AutoMandatePenetrationDetailsValuesModel> lstAdapterList, lstSearch;

        SelectedAdapterMandatePenetrationDetailsList(ArrayList<ParseXML.AutoMandatePenetrationDetailsValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<ParseXML.AutoMandatePenetrationDetailsValuesModel> results = new ArrayList<>();

                    if (lstSearch == null)
                        lstSearch = lstAdapterList;

                    String strSearch = charSequence == null ? "" : charSequence.toString();

                    if (!strSearch.equals("")) {
                        if (lstSearch != null && lstSearch.size() > 0) {
                            for (final ParseXML.AutoMandatePenetrationDetailsValuesModel model : lstSearch) {
                                if (model.getPOLICY_NO().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getMANDATE_RCVD_DATE().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getPOL_HOLDER_NAME().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getMANDATE_TYPE().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                    results.add(model);
                                }
                            }
                        }
                        oReturn.values = results;
                    } else {
                        oReturn.values = mandatePenetrationDetailsList;
                    }
                    return oReturn;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    lstAdapterList = (ArrayList<ParseXML.AutoMandatePenetrationDetailsValuesModel>) results.values;
                    selectedAdapterMandatePenetrationDetailsList = new SelectedAdapterMandatePenetrationDetailsList(lstAdapterList);
                    recyclerviewAutoMandatePenetrationDetails.setAdapter(selectedAdapterMandatePenetrationDetailsList);
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
                    R.layout.list_item_mandate_penetration_list, parent, false);
            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final ViewHolderAdapter holder, final int position) {

            holder.textviewPolicyNumber.setText(lstAdapterList.get(position).getPOLICY_NO());
            holder.textviewPolicyHoldername.setText(lstAdapterList.get(position).getPOL_HOLDER_NAME());
            holder.textviewPolicyCurrentStatus.setText(lstAdapterList.get(position).getPOLICYCURRENTSTATUS());
            holder.textviewMobileNumber.setText(lstAdapterList.get(position).getCONTACT_MOBILE());
            holder.textviewMandateType.setText(lstAdapterList.get(position).getMANDATE_TYPE());
            holder.textviewMandateStatus.setText(lstAdapterList.get(position).getMANDATE_STATUS());
            holder.textviewRejectionReason.setText(lstAdapterList.get(position).getREJECTION_REASON());
            holder.textviewMandateReceivedDate.setText(lstAdapterList.get(position).getMANDATE_RCVD_DATE());


            holder.imgcontact_cust_r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!TextUtils.isEmpty(holder.textviewMobileNumber.getText().toString())) {
                        commonMethods.callMobileNumber(holder.textviewMobileNumber.getText().toString(), context);
                    }
                }
            });
            holder.imageviewSMS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //String body = "";

                    String userString = "";
                    if (commonMethods.GetUserType(context).equalsIgnoreCase("CIF")) {
                        userString = "Insurance Facilitator";
                    } else if (commonMethods.GetUserType(context).equalsIgnoreCase("Agent")) {
                        userString = "Life Mitra";
                    }


                    /*if (strType.equalsIgnoreCase("Mandate Rejected")) {
                        body = "The auto pay mandate submitted for your SBI Life policy number " + lstAdapterList.get(position).getPOLICY_NO()
                                + " is rejected due to " + lstAdapterList.get(position).getREJECTION_REASON()
                                + ". \nYou may click the below link to register now. https://bit.ly/2AhTSv6."
                                + " \nPlease contact for further details " + commonMethods.getUserName(context)
                                + ", " + userString + ", SBI Life.";

                    } else if (strType.equalsIgnoreCase("Mandate Not Received")) {
                        body = "You have not opted for auto pay facility for your policy number " + lstAdapterList.get(position).getPOLICY_NO()
                                + ". \nPlease click the below link to register now. https://bit.ly/2AhTSv6."
                                + " Please contact for further details " + commonMethods.getUserName(context)
                                + ", " + userString + ", SBI Life.";
                    }

                    if (!TextUtils.isEmpty(holder.textviewMobileNumber.getText().toString()) && !TextUtils.isEmpty(body)) {
                        commonMethods.sendSms(context, holder.textviewMobileNumber.getText().toString(), body);
                    }*/

                    String policyNumber = lstAdapterList.get(position).getPOLICY_NO();
                    //it is rejection  reason
                    String dueDate = lstAdapterList.get(position).getREJECTION_REASON();
                    String mobileNumber = holder.textviewMobileNumber.getText().toString();
                    String userName = commonMethods.getUserName(context);


                    if (!TextUtils.isEmpty(mobileNumber) && ((strType.equalsIgnoreCase("Mandate Rejected")
                            || strType.equalsIgnoreCase("Mandate Not Received")))) {
                        if (commonMethods.isNetworkConnected(context)) {
                            SendSmsAsync sendSmsAsync = new SendSmsAsync(policyNumber, dueDate, mobileNumber, strType, userName, userString);
                            sendSmsAsync.execute();
                        } else {
                            commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                        }

                    }
                }
            });

        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder {

            private final TextView textviewPolicyNumber, textviewPolicyHoldername, textviewPolicyCurrentStatus,
                    textviewMobileNumber, textviewMandateType, textviewMandateStatus, textviewRejectionReason,
                    textviewMandateReceivedDate;
            private final ImageView imgcontact_cust_r, imageviewSMS;

            ViewHolderAdapter(View v) {
                super(v);
                textviewPolicyNumber = v.findViewById(R.id.textviewPolicyNumber);
                textviewPolicyHoldername = v.findViewById(R.id.textviewPolicyHoldername);
                textviewPolicyCurrentStatus = v.findViewById(R.id.textviewPolicyCurrentStatus);
                textviewMobileNumber = v.findViewById(R.id.textviewMobileNumber);
                textviewMandateType = v.findViewById(R.id.textviewMandateType);
                textviewMandateStatus = v.findViewById(R.id.textviewMandateStatus);
                textviewRejectionReason = v.findViewById(R.id.textviewRejectionReason);
                textviewMandateReceivedDate = v.findViewById(R.id.textviewMandateReceivedDate);
                imgcontact_cust_r = v.findViewById(R.id.imgcontact_cust_r);
                imageviewSMS = v.findViewById(R.id.imageviewSMS);

                if (strType.equalsIgnoreCase("Mandate Penetration")
                        || strType.equalsIgnoreCase("Mandate Registered")) {
                    imageviewSMS.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    class SendSmsAsync extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String response = "";

        private final String policyNumber;
        private final String dueDate;
        private final String mobileNumber;
        private final String status;
        private final String userName;
        private final String userString;

        SendSmsAsync(String policyNumber, String dueDate, String mobileNumber, String status, String userName, String userString) {
            this.policyNumber = policyNumber;
            this.dueDate = dueDate;
            this.mobileNumber = mobileNumber;
            this.status = status;
            this.userName = userName;
            this.userString = userString;
        }

        // string strStatus,string strUserName,string strUserString


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

                running = true;

                //sendemandateSMS_SMRT(string strPolicyNo, string strDueDate, string strMobile,
                // string strStatus,string strUserName,string strUserString))
                String METHOD_NAME_SEND_SMS = "sendemandateSMS_SMRT";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SEND_SMS);
                request.addProperty("strPolicyNo", policyNumber);
                request.addProperty("strDueDate", dueDate);
                request.addProperty("strMobile", mobileNumber);//mobileNumber);
                request.addProperty("strStatus", status);
                request.addProperty("strUserName", userName);
                request.addProperty("strUserString", userString);


                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME_SEND_SMS;
                androidHttpTranport.call(SOAP_ACTION, envelope);

                SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();
                response = sa.toString();

                if (response.equalsIgnoreCase("1")) {
                    response = "success";
                } else {
                    response = "";
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

            if (running) {
                if (response.equalsIgnoreCase("success")) {
                    commonMethods.showMessageDialog(context, "Message sent successfully");
                } else {
                    commonMethods.showMessageDialog(context, "Message sending failed");
                }
            } else {
                commonMethods.showMessageDialog(context, "Message sending failed");
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

        if (downloadMandatePenetratonDetailsListAsync != null) {
            downloadMandatePenetratonDetailsListAsync.cancel(true);
        }
        if (service != null) {
            service.cancel(true);
        }
    }
}
