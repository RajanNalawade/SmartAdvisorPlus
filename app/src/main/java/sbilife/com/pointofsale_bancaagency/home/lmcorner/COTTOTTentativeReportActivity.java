package sbilife.com.pointofsale_bancaagency.home.lmcorner;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
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
import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class COTTOTTentativeReportActivity extends AppCompatActivity implements ServiceHits.DownLoadData {
    private ProgressDialog mProgressDialog;
    private final String METHOD_NAME = "getCOTTOT";

    private final String NAMESPACE = "http://tempuri.org/";
    private final String URl = ServiceURL.SERVICE_URL;

    private ArrayList<COTTOTTentativeReportValuesModel> globalDataList;
    private Context context;
    private CommonMethods commonMethods;
    private TextView textviewRecordCount;
    private RecyclerView recyclerview;

    private SelectedAdapter selectedAdapter;
    private DownloadCOTTOTTentativeReportAsyncTask downloadCOTTOTTentativeReportAsyncTask;
    private ServiceHits service;
    private String strCIFBDMUserId = "", strCIFBDMEmailId = "", strCIFBDMPassword = "",
            strCIFBDMMObileNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_cottottentative_report);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "COT and TOT Tentative Standings Report");

        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");
        if (fromHome != null && fromHome.equalsIgnoreCase("N")) {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
        } else {
            CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods
                    .setUserDetails(context);
            strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        }

        Button buttonOk = findViewById(R.id.buttonOk);
        textviewRecordCount = findViewById(R.id.textviewRecordCount);

        recyclerview = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        // set LayoutManager to RecyclerView
        recyclerview.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        globalDataList = new ArrayList<>();
        selectedAdapter = new SelectedAdapter(globalDataList);
        recyclerview.setAdapter(selectedAdapter);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        mProgressDialog.setButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        if (downloadCOTTOTTentativeReportAsyncTask != null) {
                            downloadCOTTOTTentativeReportAsyncTask.cancel(true);
                        }

                        if (mProgressDialog != null) {
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }
                    }
                });

        mProgressDialog.setMax(100);

        service_hits();
    }

    private void service_hits() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        String input = strCIFBDMUserId;
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = commonMethods.getStrAuth();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
        service = new ServiceHits(context, METHOD_NAME, input,
                strCIFBDMUserId, strCIFBDMEmailId,
                strCIFBDMMObileNo, strCIFBDMPassword, this);
        service.execute();
    }

    @Override
    public void downLoadData() {
        downloadCOTTOTTentativeReportAsyncTask = new DownloadCOTTOTTentativeReportAsyncTask();
        downloadCOTTOTTentativeReportAsyncTask.execute();
    }

    class DownloadCOTTOTTentativeReportAsyncTask extends AsyncTask<String, String, String> {

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


                request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("strCode", strCIFBDMUserId);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", "QzhCNDc0OTU4NzZDQjI3RTQ4OEMyNEQ3MUZCQjE2QTY=");
//                commonMethods.appendSecurityParams(context,request,strCIFBDMEmailId,strCIFBDMMObileNo);
//
//                commonMethods.TLSv12Enable();

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();


                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;
                androidHttpTranport.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();

                if (!response.toString().contentEquals("<CIFPolicyList />")) {

                    ParseXML prsObj = new ParseXML();
                    String inputpolicylist = response.toString();
                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "Data");
                    error = prsObj.parseXmlTag(
                            inputpolicylist, "ScreenData");

                    if (error == null) {
                        // for agent policy list
                        List<String> Node = prsObj.parseParentNode(inputpolicylist, "Table");
                        List<COTTOTTentativeReportValuesModel> nodeData = parseNodeCOTTOTTentativeReport(Node);
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
            if (running) {
                if (error.equalsIgnoreCase("success")) {
                    textviewRecordCount.setText("Total Record :" + globalDataList.size());
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
            ViewHolderAdapter> {

        private ArrayList<COTTOTTentativeReportValuesModel> lstAdapterList, lstSearch;

        SelectedAdapter(ArrayList<COTTOTTentativeReportValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }


        @Override
        public int getItemCount() {
            return lstAdapterList.size();
        }

        @Override
        public ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_item_cottottentative_report, parent, false);

            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final ViewHolderAdapter holder, int position) {


            holder.textviewLMCode.setText(lstAdapterList.get(position).getIA_CODE());

            holder.txtLMName.setText(lstAdapterList.get(position).getIA_NAME());
            holder.textviewBranch.setText(lstAdapterList.get(position).getBRANCH_NAME());
            holder.textviewArea.setText(lstAdapterList.get(position).getAREA_NAME());
            holder.textviewRegion.setText(lstAdapterList.get(position).getREGION());
            holder.textviewZone.setText(lstAdapterList.get(position).getZONE());
            holder.textviewPremium.setText(lstAdapterList.get(position).getNET_AMT_NORMS());
            holder.textviewQualification.setText(lstAdapterList.get(position).getSTATUS());
            holder.textviewPremiumTitle.setText(("Premium NB As On " + commonMethods.getCurrentMonthDate()));//+"(in Crs)"

        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder {


            private final TextView textviewLMCode, txtLMName, textviewBranch, textviewArea, textviewRegion,
                    textviewZone, textviewPremium, textviewQualification, textviewPremiumTitle;

            ViewHolderAdapter(View v) {
                super(v);
                textviewLMCode = v.findViewById(R.id.textviewLMCode);
                txtLMName = v.findViewById(R.id.txtLMName);
                textviewBranch = v.findViewById(R.id.textviewBranch);
                textviewArea = v.findViewById(R.id.textviewArea);
                textviewRegion = v.findViewById(R.id.textviewRegion);
                textviewZone = v.findViewById(R.id.textviewZone);
                textviewPremium = v.findViewById(R.id.textviewPremium);
                textviewQualification = v.findViewById(R.id.textviewQualification);
                textviewPremiumTitle = v.findViewById(R.id.textviewPremiumTitle);
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

        if (downloadCOTTOTTentativeReportAsyncTask != null) {
            downloadCOTTOTTentativeReportAsyncTask.cancel(true);
        }

        if (service != null) {
            service.cancel(true);
        }
    }

    private void clearList() {
        textviewRecordCount.setVisibility(View.GONE);
        textviewRecordCount.setText("");

        if (globalDataList != null && selectedAdapter != null) {
            globalDataList.clear();
            selectedAdapter = new SelectedAdapter(globalDataList);
            recyclerview.setAdapter(selectedAdapter);
            recyclerview.invalidate();
        }
    }

    public class COTTOTTentativeReportValuesModel {
        private String IA_CODE, REGION, ZONE, WEIGHTED_PREMIUM,
                NET_AMT_NORMS, IA_NAME, BRANCH_NAME, AREA_NAME, PERSISTENCY_13, STATUS;

        public COTTOTTentativeReportValuesModel(String IA_CODE, String REGION, String ZONE, String WEIGHTED_PREMIUM,
                                                String NET_AMT_NORMS, String IA_NAME, String BRANCH_NAME, String AREA_NAME, String PERSISTENCY_13, String STATUS) {
            this.IA_CODE = IA_CODE;
            this.REGION = REGION;
            this.ZONE = ZONE;
            this.WEIGHTED_PREMIUM = WEIGHTED_PREMIUM;
            this.NET_AMT_NORMS = NET_AMT_NORMS;
            this.IA_NAME = IA_NAME;
            this.BRANCH_NAME = BRANCH_NAME;
            this.AREA_NAME = AREA_NAME;
            this.PERSISTENCY_13 = PERSISTENCY_13;
            this.STATUS = STATUS;

        }

        public String getIA_CODE() {
            return IA_CODE;
        }

        public String getREGION() {
            return REGION;
        }

        public String getZONE() {
            return ZONE;
        }

        public String getWEIGHTED_PREMIUM() {
            return WEIGHTED_PREMIUM;
        }

        public String getNET_AMT_NORMS() {
            return NET_AMT_NORMS;
        }

        public String getIA_NAME() {
            return IA_NAME;
        }

        public String getBRANCH_NAME() {
            return BRANCH_NAME;
        }

        public String getAREA_NAME() {
            return AREA_NAME;
        }

        public String getPERSISTENCY_13() {
            return PERSISTENCY_13;
        }

        public String getSTATUS() {
            return STATUS;
        }
    }

    public ArrayList<COTTOTTentativeReportValuesModel> parseNodeCOTTOTTentativeReport(List<String> lsNode) {
        ArrayList<COTTOTTentativeReportValuesModel> lsData = new ArrayList<>();

        String IA_CODE, REGION, ZONE, WEIGHTED_PREMIUM,
                NET_AMT_NORMS, IA_NAME, BRANCH_NAME, AREA_NAME, PERSISTENCY_13, STATUS;
/*<IA_CODE>17076945</IA_CODE>
        <REGION>CHANDIGARH</REGION>
        <ZONE>ZONE 1</ZONE>
        <WEIGHTED_PREMIUM>8278980.3122468865930540</WEIGHTED_PREMIUM>
        <NET_AMT_NORMS>6973076.2982468865938540</NET_AMT_NORMS>
        <IA_NAME>Priya Soyee</IA_NAME>
        <BRANCH_NAME>KARNAL</BRANCH_NAME>
        <AREA_NAME>HARYANA 1</AREA_NAME>
        <PERSISTENCY_13>87.775865</PERSISTENCY_13>
        <STATUS>NOT QUALIFIED</STATUS>*/
        CommonForAllProd mCommonForAllProd = new CommonForAllProd();
        ParseXML parseXML = new ParseXML();
        for (String Node : lsNode) {

            IA_CODE = parseXML.parseXmlTag(Node, "IA_CODE");
            IA_CODE = IA_CODE == null ? "" : IA_CODE;

            ZONE = parseXML.parseXmlTag(Node, "ZONE");
            ZONE = ZONE == null ? "" : ZONE;

            WEIGHTED_PREMIUM = parseXML.parseXmlTag(Node, "WEIGHTED_PREMIUM");
            WEIGHTED_PREMIUM = WEIGHTED_PREMIUM == null ? "" : WEIGHTED_PREMIUM;
            WEIGHTED_PREMIUM = mCommonForAllProd.roundUp_Level2(WEIGHTED_PREMIUM);

            NET_AMT_NORMS = parseXML.parseXmlTag(Node, "NET_AMT_NORMS");
            NET_AMT_NORMS = NET_AMT_NORMS == null ? "" : NET_AMT_NORMS;
            //NET_AMT_NORMS = mCommonForAllProd.roundUp_Level2(NET_AMT_NORMS);

            IA_NAME = parseXML.parseXmlTag(Node, "IA_NAME");
            IA_NAME = IA_NAME == null ? "" : IA_NAME;

            REGION = parseXML.parseXmlTag(Node, "REGION");
            REGION = REGION == null ? "" : REGION;

            BRANCH_NAME = parseXML.parseXmlTag(Node, "BRANCH_NAME");
            BRANCH_NAME = BRANCH_NAME == null ? "" : BRANCH_NAME;

            AREA_NAME = parseXML.parseXmlTag(Node, "AREA_NAME");
            AREA_NAME = AREA_NAME == null ? "" : AREA_NAME;

            PERSISTENCY_13 = parseXML.parseXmlTag(Node, "PERSISTENCY_13");
            PERSISTENCY_13 = PERSISTENCY_13 == null ? "" : PERSISTENCY_13;

            STATUS = parseXML.parseXmlTag(Node, "STATUS");
            STATUS = STATUS == null ? "" : STATUS;


            COTTOTTentativeReportValuesModel nodeVal = new COTTOTTentativeReportValuesModel(IA_CODE, REGION, ZONE, WEIGHTED_PREMIUM,
                    NET_AMT_NORMS, IA_NAME, BRANCH_NAME, AREA_NAME, PERSISTENCY_13, STATUS);
            lsData.add(nodeVal);


        }
        return lsData;
    }
}
