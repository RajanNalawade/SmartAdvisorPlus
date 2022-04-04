package sbilife.com.pointofsale_bancaagency.home.lmcorner;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
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

public class LMClubMembershipReportActivity extends AppCompatActivity implements ServiceHits.DownLoadData {
    private final String METHOD_NAME = "getLMreport";

    private final String NAMESPACE = "http://tempuri.org/";
    private final String URl = ServiceURL.SERVICE_URL;
    private ProgressDialog mProgressDialog;
    private ArrayList<LMClubMembershipReportValuesModel> globalDataList;
    private Context context;
    private CommonMethods commonMethods;
    private TextView textviewRecordCount;
    private RecyclerView recyclerview;

    private SelectedAdapter selectedAdapter;
    private DownloadLMClubMembershipReportAsyncTask downloadLMClubMembershipReportAsyncTask;
    private ServiceHits service;
    private String strCIFBDMUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_lmclub_membership_report);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "LM Club Membership Report");

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


                        if (downloadLMClubMembershipReportAsyncTask != null) {
                            downloadLMClubMembershipReportAsyncTask.cancel(true);
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
        downloadLMClubMembershipReportAsyncTask = new DownloadLMClubMembershipReportAsyncTask();
        downloadLMClubMembershipReportAsyncTask.execute();
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

        if (downloadLMClubMembershipReportAsyncTask != null) {
            downloadLMClubMembershipReportAsyncTask.cancel(true);
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

    public ArrayList<LMClubMembershipReportValuesModel> parseNodeLMClubMembershipReport(List<String> lsNode) {
        ArrayList<LMClubMembershipReportValuesModel> lsData = new ArrayList<>();

        String IACODE, IANAME, BRANCHCODE, BRANCHNAME, AREA, REGION, DATE_OF_JOINING, DATE_OF_REINSTATSEMENT,
                AAD_STATUS, LY_PREMIUM_AMT_NB, CY_PREMIUM_AMT_NB, CUMULATIVE_PREM_AMT_NB, YR_CMS_COMM_AMT, PERRATIO1,
                FYNL_CLUB_WITHOUT_PERS, FYNL_CLUB_WITH_PERS, UM_CODE, UM_NAME, LY_NOPS, CY_NOPS,
                ZONE, IQA_QUALIFICATION, PERSISTENCY_RATIO, FINAL_CLUB_PROD_YR_PER_21_22, FINAL_CLUB_OF_PROD_YR_20_21,
                PROTECTION_BUSINESS;


        ParseXML parseXML = new ParseXML();
        for (String Node : lsNode) {

            IACODE = parseXML.parseXmlTag(Node, "IACODE");
            IACODE = IACODE == null ? "" : IACODE;

            IANAME = parseXML.parseXmlTag(Node, "IANAME");
            IANAME = IANAME == null ? "" : IANAME;

            BRANCHCODE = parseXML.parseXmlTag(Node, "BRANCHCODE");
            BRANCHCODE = BRANCHCODE == null ? "" : BRANCHCODE;

            BRANCHNAME = parseXML.parseXmlTag(Node, "BRANCHNAME");
            BRANCHNAME = BRANCHNAME == null ? "" : BRANCHNAME;

            AREA = parseXML.parseXmlTag(Node, "AREA");
            AREA = AREA == null ? "" : AREA;

            REGION = parseXML.parseXmlTag(Node, "REGION");
            REGION = REGION == null ? "" : REGION;

            DATE_OF_JOINING = parseXML.parseXmlTag(Node, "DATE_OF_JOINING");
            DATE_OF_JOINING = DATE_OF_JOINING == null ? "" : DATE_OF_JOINING;

            DATE_OF_REINSTATSEMENT = parseXML.parseXmlTag(Node, "DATE_OF_REINSTATSEMENT");
            DATE_OF_REINSTATSEMENT = DATE_OF_REINSTATSEMENT == null ? "" : DATE_OF_REINSTATSEMENT;

            AAD_STATUS = parseXML.parseXmlTag(Node, "AAD_STATUS");
            AAD_STATUS = AAD_STATUS == null ? "" : AAD_STATUS;

            LY_PREMIUM_AMT_NB = parseXML.parseXmlTag(Node, "LY_PREMIUM_AMT_NB");
            LY_PREMIUM_AMT_NB = LY_PREMIUM_AMT_NB == null ? "" : LY_PREMIUM_AMT_NB;

            CY_PREMIUM_AMT_NB = parseXML.parseXmlTag(Node, "CY_PREMIUM_AMT_NB");
            CY_PREMIUM_AMT_NB = CY_PREMIUM_AMT_NB == null ? "" : CY_PREMIUM_AMT_NB;

            CUMULATIVE_PREM_AMT_NB = parseXML.parseXmlTag(Node, "CUMULATIVE_PREM_AMT_NB");
            CUMULATIVE_PREM_AMT_NB = CUMULATIVE_PREM_AMT_NB == null ? "" : CUMULATIVE_PREM_AMT_NB;

            YR_CMS_COMM_AMT = parseXML.parseXmlTag(Node, "YR_CMS_COMM_AMT");
            YR_CMS_COMM_AMT = YR_CMS_COMM_AMT == null ? "" : YR_CMS_COMM_AMT;

            PERRATIO1 = parseXML.parseXmlTag(Node, "PERRATIO1");
            PERRATIO1 = PERRATIO1 == null ? "" : PERRATIO1;

            FYNL_CLUB_WITHOUT_PERS = parseXML.parseXmlTag(Node, "FYNL_CLUB_WITHOUT_PERS");
            FYNL_CLUB_WITHOUT_PERS = FYNL_CLUB_WITHOUT_PERS == null ? "" : FYNL_CLUB_WITHOUT_PERS;

            FYNL_CLUB_WITH_PERS = parseXML.parseXmlTag(Node, "FYNL_CLUB_WITH_PERS");
            FYNL_CLUB_WITH_PERS = FYNL_CLUB_WITH_PERS == null ? "" : FYNL_CLUB_WITH_PERS;

            UM_CODE = parseXML.parseXmlTag(Node, "UM_CODE");
            UM_CODE = UM_CODE == null ? "" : UM_CODE;

            UM_NAME = parseXML.parseXmlTag(Node, "UM_NAME");
            UM_NAME = UM_NAME == null ? "" : UM_NAME;

            LY_NOPS = parseXML.parseXmlTag(Node, "LY_NOPS");
            LY_NOPS = LY_NOPS == null ? "" : LY_NOPS;

            CY_NOPS = parseXML.parseXmlTag(Node, "CY_NOPS");
            CY_NOPS = CY_NOPS == null ? "" : CY_NOPS;

            ZONE = parseXML.parseXmlTag(Node, "ZONE");
            ZONE = ZONE == null ? "" : ZONE;

            IQA_QUALIFICATION = parseXML.parseXmlTag(Node, "IQA_QUALIFICATION");
            IQA_QUALIFICATION = IQA_QUALIFICATION == null ? "" : IQA_QUALIFICATION;

            PERSISTENCY_RATIO = parseXML.parseXmlTag(Node, "PERSISTENCY_RATIO");
            PERSISTENCY_RATIO = PERSISTENCY_RATIO == null ? "" : PERSISTENCY_RATIO;

            FINAL_CLUB_PROD_YR_PER_21_22 = parseXML.parseXmlTag(Node, "FINAL_CLUB_PROD_YR_PER_21_22");
            FINAL_CLUB_PROD_YR_PER_21_22 = FINAL_CLUB_PROD_YR_PER_21_22 == null ? "" : FINAL_CLUB_PROD_YR_PER_21_22;

            FINAL_CLUB_OF_PROD_YR_20_21 = parseXML.parseXmlTag(Node, "FINAL_CLUB_OF_PROD_YR_20_21");
            FINAL_CLUB_OF_PROD_YR_20_21 = FINAL_CLUB_OF_PROD_YR_20_21 == null ? "" : FINAL_CLUB_OF_PROD_YR_20_21;

            PROTECTION_BUSINESS = parseXML.parseXmlTag(Node, "PROTECTION_BUSINESS");
            PROTECTION_BUSINESS = PROTECTION_BUSINESS == null ? "" : PROTECTION_BUSINESS;

            LMClubMembershipReportValuesModel nodeVal = new LMClubMembershipReportValuesModel(IACODE, IANAME, BRANCHCODE, BRANCHNAME, AREA, REGION, DATE_OF_JOINING, DATE_OF_REINSTATSEMENT, AAD_STATUS, LY_PREMIUM_AMT_NB, CY_PREMIUM_AMT_NB, CUMULATIVE_PREM_AMT_NB, YR_CMS_COMM_AMT, PERRATIO1, FYNL_CLUB_WITHOUT_PERS, FYNL_CLUB_WITH_PERS, UM_CODE, UM_NAME, LY_NOPS, CY_NOPS,
                    ZONE, IQA_QUALIFICATION, PERSISTENCY_RATIO, FINAL_CLUB_PROD_YR_PER_21_22, FINAL_CLUB_OF_PROD_YR_20_21,
                    PROTECTION_BUSINESS);
            lsData.add(nodeVal);

        }
        return lsData;
    }

    class DownloadLMClubMembershipReportAsyncTask extends AsyncTask<String, String, String> {

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
                commonMethods.appendSecurityParams(context, request, "", "");
                commonMethods.TLSv12Enable();

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
                        List<LMClubMembershipReportValuesModel> nodeData = parseNodeLMClubMembershipReport(Node);
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

        private ArrayList<LMClubMembershipReportValuesModel> lstAdapterList, lstSearch;

        SelectedAdapter(ArrayList<LMClubMembershipReportValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }


        @Override
        public int getItemCount() {
            return lstAdapterList.size();
        }

        @Override
        public ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_item_lmclub_membership_report, parent, false);

            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final ViewHolderAdapter holder, int position) {


            holder.textviewIACode.setText(lstAdapterList.get(position).getIACODE());

            holder.txtIAName.setText(lstAdapterList.get(position).getIANAME());
            holder.textviewBranchCode.setText(lstAdapterList.get(position).getBRANCHCODE());
            holder.textviewBranchName.setText(lstAdapterList.get(position).getBRANCHNAME());
            holder.textviewArea.setText(lstAdapterList.get(position).getAREA());
            holder.textviewRegion.setText(lstAdapterList.get(position).getREGION());
            holder.textviewJoiningDate.setText(lstAdapterList.get(position).getDATE_OF_JOINING());
            holder.textviewReinstatementDate.setText(lstAdapterList.get(position).getDATE_OF_REINSTATSEMENT());
            holder.textviewAADStatus.setText(lstAdapterList.get(position).getAAD_STATUS());
            holder.textviewLYPremiumAmount.setText(lstAdapterList.get(position).getLY_PREMIUM_AMT_NB());
            holder.textviewCYPremiumAmount.setText(lstAdapterList.get(position).getCY_PREMIUM_AMT_NB());
            holder.textviewCumulativePremiumAmount.setText(lstAdapterList.get(position).getCUMULATIVE_PREM_AMT_NB());
            holder.textviewYRCMSCommAmount.setText(lstAdapterList.get(position).getYR_CMS_COMM_AMT());
            holder.textviewPERRATIO.setText(lstAdapterList.get(position).getPERRATIO1());
            holder.textviewFYNL_CLUB_WITHOUT_PERS.setText(lstAdapterList.get(position).getFYNL_CLUB_WITHOUT_PERS());
            holder.textviewFYNL_CLUB_WITH_PERS.setText(lstAdapterList.get(position).getFYNL_CLUB_WITH_PERS());
            holder.textviewUMCode.setText(lstAdapterList.get(position).getUM_CODE());
            holder.textviewUMName.setText(lstAdapterList.get(position).getUM_NAME());
            holder.textviewLYNops.setText(lstAdapterList.get(position).getLY_NOPS());
            holder.textviewCYNops.setText(lstAdapterList.get(position).getCY_NOPS());
            holder.textviewZone.setText(lstAdapterList.get(position).getZONE());
            holder.tvIQAQualification.setText(lstAdapterList.get(position).getIQA_QUALIFICATION());
            holder.tvPersistencyRatio.setText(lstAdapterList.get(position).getPERSISTENCY_RATIO());
            holder.tvFinalClubProdyrPer.setText(lstAdapterList.get(position).getFINAL_CLUB_PROD_YR_PER_21_22());
            holder.tvFinalClubProdyr.setText(lstAdapterList.get(position).getFINAL_CLUB_OF_PROD_YR_20_21());
            holder.tvProtectionBusiness.setText(lstAdapterList.get(position).getPROTECTION_BUSINESS());

        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder {


            private final TextView textviewIACode, txtIAName, textviewBranchCode, textviewBranchName, textviewArea,
                    textviewRegion, textviewJoiningDate, textviewReinstatementDate, textviewAADStatus,
                    textviewLYPremiumAmount, textviewCYPremiumAmount, textviewCumulativePremiumAmount,
                    textviewYRCMSCommAmount, textviewPERRATIO, textviewFYNL_CLUB_WITHOUT_PERS,
                    textviewFYNL_CLUB_WITH_PERS, textviewUMCode, textviewUMName, textviewLYNops, textviewCYNops,
                    textviewZone, tvIQAQualification, tvPersistencyRatio, tvFinalClubProdyrPer, tvFinalClubProdyr,
                    tvProtectionBusiness;

            ViewHolderAdapter(View v) {
                super(v);
                textviewIACode = v.findViewById(R.id.textviewIACode);
                txtIAName = v.findViewById(R.id.txtIAName);
                textviewBranchCode = v.findViewById(R.id.textviewBranchCode);
                textviewBranchName = v.findViewById(R.id.textviewBranchName);
                textviewArea = v.findViewById(R.id.textviewArea);
                textviewRegion = v.findViewById(R.id.textviewRegion);
                textviewJoiningDate = v.findViewById(R.id.textviewJoiningDate);
                textviewReinstatementDate = v.findViewById(R.id.textviewReinstatementDate);
                textviewAADStatus = v.findViewById(R.id.textviewAADStatus);
                textviewLYPremiumAmount = v.findViewById(R.id.textviewLYPremiumAmount);
                textviewCYPremiumAmount = v.findViewById(R.id.textviewCYPremiumAmount);
                textviewCumulativePremiumAmount = v.findViewById(R.id.textviewCumulativePremiumAmount);
                textviewYRCMSCommAmount = v.findViewById(R.id.textviewYRCMSCommAmount);
                textviewPERRATIO = v.findViewById(R.id.textviewPERRATIO);
                textviewFYNL_CLUB_WITHOUT_PERS = v.findViewById(R.id.textviewFYNL_CLUB_WITHOUT_PERS);
                textviewFYNL_CLUB_WITH_PERS = v.findViewById(R.id.textviewFYNL_CLUB_WITH_PERS);
                textviewUMCode = v.findViewById(R.id.textviewUMCode);
                textviewUMName = v.findViewById(R.id.textviewUMName);
                textviewLYNops = v.findViewById(R.id.textviewLYNops);
                textviewCYNops = v.findViewById(R.id.textviewCYNops);
                textviewZone = v.findViewById(R.id.textviewZone);
                tvIQAQualification = v.findViewById(R.id.tvIQAQualification);
                tvPersistencyRatio = v.findViewById(R.id.tvPersistencyRatio);
                tvFinalClubProdyrPer = v.findViewById(R.id.tvFinalClubProdyrPer);
                tvFinalClubProdyr = v.findViewById(R.id.tvFinalClubProdyr);
                tvProtectionBusiness = v.findViewById(R.id.tvProtectionBusiness);
            }

        }

    }

    public class LMClubMembershipReportValuesModel {
        private String IACODE, IANAME, BRANCHCODE, BRANCHNAME, AREA, REGION, DATE_OF_JOINING, DATE_OF_REINSTATSEMENT,
                AAD_STATUS, LY_PREMIUM_AMT_NB, CY_PREMIUM_AMT_NB, CUMULATIVE_PREM_AMT_NB, YR_CMS_COMM_AMT, PERRATIO1,
                FYNL_CLUB_WITHOUT_PERS, FYNL_CLUB_WITH_PERS, UM_CODE, UM_NAME, LY_NOPS, CY_NOPS,
                ZONE, IQA_QUALIFICATION, PERSISTENCY_RATIO, FINAL_CLUB_PROD_YR_PER_21_22, FINAL_CLUB_OF_PROD_YR_20_21,
                PROTECTION_BUSINESS;

        public LMClubMembershipReportValuesModel(String IACODE, String IANAME, String BRANCHCODE, String BRANCHNAME,
                                                 String AREA, String REGION, String DATE_OF_JOINING,
                                                 String DATE_OF_REINSTATSEMENT, String AAD_STATUS,
                                                 String LY_PREMIUM_AMT_NB, String CY_PREMIUM_AMT_NB,
                                                 String CUMULATIVE_PREM_AMT_NB, String YR_CMS_COMM_AMT,
                                                 String PERRATIO1, String FYNL_CLUB_WITHOUT_PERS,
                                                 String FYNL_CLUB_WITH_PERS, String UM_CODE, String UM_NAME,
                                                 String LY_NOPS, String CY_NOPS, String ZONE, String IQA_QUALIFICATION,
                                                 String PERSISTENCY_RATIO, String FINAL_CLUB_PROD_YR_PER_21_22,
                                                 String FINAL_CLUB_OF_PROD_YR_20_21, String PROTECTION_BUSINESS) {
            this.IACODE = IACODE;
            this.IANAME = IANAME;
            this.BRANCHCODE = BRANCHCODE;
            this.BRANCHNAME = BRANCHNAME;
            this.AREA = AREA;
            this.REGION = REGION;
            this.DATE_OF_JOINING = DATE_OF_JOINING;
            this.DATE_OF_REINSTATSEMENT = DATE_OF_REINSTATSEMENT;
            this.AAD_STATUS = AAD_STATUS;
            this.LY_PREMIUM_AMT_NB = LY_PREMIUM_AMT_NB;
            this.CY_PREMIUM_AMT_NB = CY_PREMIUM_AMT_NB;
            this.CUMULATIVE_PREM_AMT_NB = CUMULATIVE_PREM_AMT_NB;
            this.YR_CMS_COMM_AMT = YR_CMS_COMM_AMT;
            this.PERRATIO1 = PERRATIO1;
            this.FYNL_CLUB_WITHOUT_PERS = FYNL_CLUB_WITHOUT_PERS;
            this.FYNL_CLUB_WITH_PERS = FYNL_CLUB_WITH_PERS;
            this.UM_CODE = UM_CODE;
            this.UM_NAME = UM_NAME;
            this.LY_NOPS = LY_NOPS;
            this.CY_NOPS = CY_NOPS;
            this.ZONE = ZONE;
            this.IQA_QUALIFICATION = IQA_QUALIFICATION;
            this.PERSISTENCY_RATIO = PERSISTENCY_RATIO;
            this.FINAL_CLUB_PROD_YR_PER_21_22 = FINAL_CLUB_PROD_YR_PER_21_22;
            this.FINAL_CLUB_OF_PROD_YR_20_21 = FINAL_CLUB_OF_PROD_YR_20_21;
            this.PROTECTION_BUSINESS = PROTECTION_BUSINESS;
        }

        public String getIACODE() {
            return IACODE;
        }

        public String getIANAME() {
            return IANAME;
        }

        public String getBRANCHCODE() {
            return BRANCHCODE;
        }

        public String getBRANCHNAME() {
            return BRANCHNAME;
        }

        public String getAREA() {
            return AREA;
        }

        public String getREGION() {
            return REGION;
        }

        public String getDATE_OF_JOINING() {
            return DATE_OF_JOINING;
        }

        public String getDATE_OF_REINSTATSEMENT() {
            return DATE_OF_REINSTATSEMENT;
        }

        public String getAAD_STATUS() {
            return AAD_STATUS;
        }

        public String getLY_PREMIUM_AMT_NB() {
            return LY_PREMIUM_AMT_NB;
        }

        public String getCY_PREMIUM_AMT_NB() {
            return CY_PREMIUM_AMT_NB;
        }

        public String getCUMULATIVE_PREM_AMT_NB() {
            return CUMULATIVE_PREM_AMT_NB;
        }

        public String getYR_CMS_COMM_AMT() {
            return YR_CMS_COMM_AMT;
        }

        public String getPERRATIO1() {
            return PERRATIO1;
        }

        public String getFYNL_CLUB_WITHOUT_PERS() {
            return FYNL_CLUB_WITHOUT_PERS;
        }

        public String getFYNL_CLUB_WITH_PERS() {
            return FYNL_CLUB_WITH_PERS;
        }

        public String getUM_CODE() {
            return UM_CODE;
        }

        public String getUM_NAME() {
            return UM_NAME;
        }

        public String getLY_NOPS() {
            return LY_NOPS;
        }

        public String getCY_NOPS() {
            return CY_NOPS;
        }

        public String getZONE() {
            return ZONE;
        }

        public String getIQA_QUALIFICATION() {
            return IQA_QUALIFICATION;
        }

        public String getPERSISTENCY_RATIO() {
            return PERSISTENCY_RATIO;
        }

        public String getFINAL_CLUB_PROD_YR_PER_21_22() {
            return FINAL_CLUB_PROD_YR_PER_21_22;
        }

        public String getFINAL_CLUB_OF_PROD_YR_20_21() {
            return FINAL_CLUB_OF_PROD_YR_20_21;
        }

        public String getPROTECTION_BUSINESS() {
            return PROTECTION_BUSINESS;
        }
    }
}
