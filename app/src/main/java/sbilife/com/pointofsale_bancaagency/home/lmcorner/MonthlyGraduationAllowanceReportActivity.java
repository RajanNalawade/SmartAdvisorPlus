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

public class MonthlyGraduationAllowanceReportActivity extends AppCompatActivity implements ServiceHits.DownLoadData {

    private final String METHOD_NAME = "getLMMGAR_smrt";
    private ProgressDialog mProgressDialog;
    private Context context;
    private CommonMethods commonMethods;
    private TextView textviewRecordCount;
    private RecyclerView recyclerview;
    private ServiceHits service;
    private String strCIFBDMUserId = "";
    private String strCIFBDMEmailId = "";
    private String strCIFBDMMObileNo = "", strCIFBDMPassword = "";
    private ArrayList<MonthlyGraduationAllowanceReportValuesModel> globalDataList;
    private SelectedAdapter selectedAdapter;
    private DownloadMonthlyGraduationAllowanceReportAsync downloadMonthlyGraduationAllowanceReportAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_monthly_graduation_allowance_report);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Monthly Graduation Allowance Report");

        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");
        if (fromHome != null && fromHome.equalsIgnoreCase("N")) {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");
        } else {
            CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods
                    .setUserDetails(context);
            strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
            strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
            strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
        }
        try {
            strCIFBDMPassword = commonMethods.getStrAuth();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                        killTasks();
                    }
                });

        mProgressDialog.setMax(100);


        service_hits();
    }

    private void service_hits() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        String input = strCIFBDMUserId;
        service = new ServiceHits(context, METHOD_NAME, input,
                strCIFBDMUserId, strCIFBDMEmailId,
                strCIFBDMMObileNo, strCIFBDMPassword, this);
        service.execute();
    }

    @Override
    public void downLoadData() {
        downloadMonthlyGraduationAllowanceReportAsync = new DownloadMonthlyGraduationAllowanceReportAsync();
        downloadMonthlyGraduationAllowanceReportAsync.execute();
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

        if (downloadMonthlyGraduationAllowanceReportAsync != null) {
            downloadMonthlyGraduationAllowanceReportAsync.cancel(true);
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

    class DownloadMonthlyGraduationAllowanceReportAsync extends AsyncTask<String, String, String> {

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
                //getLMMGAR_smrt(string strCode, string strEmailId, string strMobileNo, string strAuthKey)
                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("strCode", strCIFBDMUserId);
                commonMethods.appendSecurityParams(context, request, strCIFBDMEmailId, strCIFBDMMObileNo);
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
                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "NewDataSet");
                    error = prsObj.parseXmlTag(
                            inputpolicylist, "ScreenData");

                    if (error == null) {
                        // for agent policy list
                        List<String> Node = prsObj.parseParentNode(inputpolicylist, "Table");
                        List<MonthlyGraduationAllowanceReportValuesModel> nodeData = parseNodeMonthGradAllowReport(Node);
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

    public class SelectedAdapter extends RecyclerView.Adapter<SelectedAdapter.ViewHolderAdapter> {

        private final ArrayList<MonthlyGraduationAllowanceReportValuesModel> lstAdapterList;

        SelectedAdapter(ArrayList<MonthlyGraduationAllowanceReportValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }


        @Override
        public int getItemCount() {
            return lstAdapterList.size();
        }

        @Override
        public ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_item_monthly_allowance_graduation_report, parent, false);

            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final ViewHolderAdapter holder, int position) {


            holder.textviewLMCode.setText(lstAdapterList.get(position).getIACODE());
            holder.txtLMName.setText(lstAdapterList.get(position).getIANAME());
            holder.textviewBranchName.setText(lstAdapterList.get(position).getBRANCHNAME());
            holder.textviewBranchCode.setText(lstAdapterList.get(position).getBRANCHCODE());
            holder.textviewArea.setText(lstAdapterList.get(position).getAREA());
            holder.textviewRegion.setText(lstAdapterList.get(position).getREGION());

            holder.textviewStatus.setText(lstAdapterList.get(position).getSTATUS());
            holder.txtAllowanceEligibility.setText(lstAdapterList.get(position).getALLOWANCEELIGIBILITY());
            holder.tvMinMonthlySlab1.setText(lstAdapterList.get(position).getMINIMUMMONTHLYPREMIUMSLAB1());
            holder.tvMinMonthlySlab2.setText(lstAdapterList.get(position).getMINIMUMMONTHLYPREMIUMSLAB2());
            holder.tvMTDRegularPremAmount.setText(lstAdapterList.get(position).getMTDREGULARPREMIUMAMOUNT());
            holder.tvPremShortageSlab1.setText(lstAdapterList.get(position).getPREMIUMSHORTAGEFORSLAB1());
            holder.tvPremShortageSlab2.setText(lstAdapterList.get(position).getPREMIUMSHORTAGEFORSLAB2());

            holder.tvCurrentQualificationSlab.setText(lstAdapterList.get(position).getCURRENTQUALIFICATIONSLAB());
            holder.tvRemark.setText(lstAdapterList.get(position).getREMARK());

        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder {

            private final TextView textviewLMCode, txtLMName, textviewBranchName,
                    textviewBranchCode, textviewArea, textviewRegion, textviewStatus,
                    txtAllowanceEligibility, tvMinMonthlySlab1, tvMinMonthlySlab2,
                    tvMTDRegularPremAmount, tvPremShortageSlab1, tvPremShortageSlab2,
                    tvCurrentQualificationSlab, tvRemark;

            ViewHolderAdapter(View v) {
                super(v);
                textviewLMCode = v.findViewById(R.id.textviewLMCode);
                txtLMName = v.findViewById(R.id.txtLMName);
                textviewBranchName = v.findViewById(R.id.textviewBranchName);
                textviewBranchCode = v.findViewById(R.id.textviewBranchCode);
                textviewArea = v.findViewById(R.id.textviewArea);
                textviewRegion = v.findViewById(R.id.textviewRegion);
                textviewStatus = v.findViewById(R.id.textviewStatus);
                txtAllowanceEligibility = v.findViewById(R.id.txtAllowanceEligibility);
                tvMinMonthlySlab1 = v.findViewById(R.id.tvMinMonthlySlab1);

                tvMinMonthlySlab2 = v.findViewById(R.id.tvMinMonthlySlab2);
                tvMTDRegularPremAmount = v.findViewById(R.id.tvMTDRegularPremAmount);

                tvPremShortageSlab1 = v.findViewById(R.id.tvPremShortageSlab1);
                tvPremShortageSlab2 = v.findViewById(R.id.tvPremShortageSlab2);

                tvCurrentQualificationSlab = v.findViewById(R.id.tvCurrentQualificationSlab);
                tvRemark = v.findViewById(R.id.tvRemark);
            }

        }

    }

    public ArrayList<MonthlyGraduationAllowanceReportValuesModel> parseNodeMonthGradAllowReport(List<String> lsNode) {
        ArrayList<MonthlyGraduationAllowanceReportValuesModel> lsData = new ArrayList<>();

        String IACODE, IANAME, BRANCHCODE, BRANCHNAME, AREA, REGION, STATUS, ALLOWANCEELIGIBILITY,
                MINIMUMMONTHLYPREMIUMSLAB1, MINIMUMMONTHLYPREMIUMSLAB2, MTDREGULARPREMIUMAMOUNT, PREMIUMSHORTAGEFORSLAB1,
                PREMIUMSHORTAGEFORSLAB2, CURRENTQUALIFICATIONSLAB, REMARK;

//        CommonForAllProd mCommonForAllProd = new CommonForAllProd();
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

            STATUS = parseXML.parseXmlTag(Node, "STATUS");
            STATUS = STATUS == null ? "" : STATUS;

            ALLOWANCEELIGIBILITY = parseXML.parseXmlTag(Node, "ALLOWANCEELIGIBILITY");
            ALLOWANCEELIGIBILITY = ALLOWANCEELIGIBILITY == null ? "" : ALLOWANCEELIGIBILITY;

            MINIMUMMONTHLYPREMIUMSLAB1 = parseXML.parseXmlTag(Node, "MINIMUMMONTHLYPREMIUMSLAB1");
            MINIMUMMONTHLYPREMIUMSLAB1 = MINIMUMMONTHLYPREMIUMSLAB1 == null ? "" : MINIMUMMONTHLYPREMIUMSLAB1;

            MINIMUMMONTHLYPREMIUMSLAB2 = parseXML.parseXmlTag(Node, "MINIMUMMONTHLYPREMIUMSLAB2");
            MINIMUMMONTHLYPREMIUMSLAB2 = MINIMUMMONTHLYPREMIUMSLAB2 == null ? "" : MINIMUMMONTHLYPREMIUMSLAB2;

            MTDREGULARPREMIUMAMOUNT = parseXML.parseXmlTag(Node, "MTDREGULARPREMIUMAMOUNT");
            MTDREGULARPREMIUMAMOUNT = MTDREGULARPREMIUMAMOUNT == null ? "" : MTDREGULARPREMIUMAMOUNT;

            PREMIUMSHORTAGEFORSLAB1 = parseXML.parseXmlTag(Node, "PREMIUMSHORTAGEFORSLAB1");
            PREMIUMSHORTAGEFORSLAB1 = PREMIUMSHORTAGEFORSLAB1 == null ? "" : PREMIUMSHORTAGEFORSLAB1;

            PREMIUMSHORTAGEFORSLAB2 = parseXML.parseXmlTag(Node, "PREMIUMSHORTAGEFORSLAB2");
            PREMIUMSHORTAGEFORSLAB2 = PREMIUMSHORTAGEFORSLAB2 == null ? "" : PREMIUMSHORTAGEFORSLAB2;
            //GAP_TO_NEXT_SLAB = mCommonForAllProd.roundUp_Level2(GAP_TO_NEXT_SLAB);

            CURRENTQUALIFICATIONSLAB = parseXML.parseXmlTag(Node, "CURRENTQUALIFICATIONSLAB");
            CURRENTQUALIFICATIONSLAB = CURRENTQUALIFICATIONSLAB == null ? "" : CURRENTQUALIFICATIONSLAB;

            REMARK = parseXML.parseXmlTag(Node, "REMARK");
            REMARK = REMARK == null ? "" : REMARK;

            MonthlyGraduationAllowanceReportValuesModel nodeVal = new MonthlyGraduationAllowanceReportValuesModel(IACODE, IANAME, BRANCHCODE, BRANCHNAME, AREA, REGION, STATUS, ALLOWANCEELIGIBILITY, MINIMUMMONTHLYPREMIUMSLAB1, MINIMUMMONTHLYPREMIUMSLAB2, MTDREGULARPREMIUMAMOUNT, PREMIUMSHORTAGEFORSLAB1, PREMIUMSHORTAGEFORSLAB2, CURRENTQUALIFICATIONSLAB, REMARK);
            lsData.add(nodeVal);
        }
        return lsData;
    }

    class MonthlyGraduationAllowanceReportValuesModel {
        /*<IACODE>991063088</IACODE>
                <IANAME>POONAM MAHANGADE</IANAME>
                <BRANCHCODE>8771195</BRANCHCODE>
                <BRANCHNAME>PUNE</BRANCHNAME>
                <AREA>ROM 1</AREA>
                <REGION>MAHARASHTRA</REGION>
                <STATUS>Active</STATUS>
                <ALLOWANCEELIGIBILITY>BRANCH MANAGER CLUB</ALLOWANCEELIGIBILITY>
                <MINIMUMMONTHLYPREMIUMSLAB1>30000</MINIMUMMONTHLYPREMIUMSLAB1>
                <MINIMUMMONTHLYPREMIUMSLAB2>50000</MINIMUMMONTHLYPREMIUMSLAB2>
                <MTDREGULARPREMIUMAMOUNT>33700</MTDREGULARPREMIUMAMOUNT>
                <PREMIUMSHORTAGEFORSLAB1>0</PREMIUMSHORTAGEFORSLAB1>
                <PREMIUMSHORTAGEFORSLAB2>16300</PREMIUMSHORTAGEFORSLAB2>
                <CURRENTQUALIFICATIONSLAB>SLAB-1</CURRENTQUALIFICATIONSLAB>
                <REMARK>Shortage Premium 16300 for Slab 2</REMARK>*/
        private String IACODE, IANAME, BRANCHCODE, BRANCHNAME, AREA, REGION, STATUS, ALLOWANCEELIGIBILITY,
                MINIMUMMONTHLYPREMIUMSLAB1, MINIMUMMONTHLYPREMIUMSLAB2, MTDREGULARPREMIUMAMOUNT, PREMIUMSHORTAGEFORSLAB1,
                PREMIUMSHORTAGEFORSLAB2, CURRENTQUALIFICATIONSLAB, REMARK;

        public MonthlyGraduationAllowanceReportValuesModel(String IACODE, String IANAME, String BRANCHCODE, String BRANCHNAME, String AREA, String REGION, String STATUS, String ALLOWANCEELIGIBILITY, String MINIMUMMONTHLYPREMIUMSLAB1, String MINIMUMMONTHLYPREMIUMSLAB2, String MTDREGULARPREMIUMAMOUNT, String PREMIUMSHORTAGEFORSLAB1, String PREMIUMSHORTAGEFORSLAB2, String CURRENTQUALIFICATIONSLAB, String REMARK) {
            this.IACODE = IACODE;
            this.IANAME = IANAME;
            this.BRANCHCODE = BRANCHCODE;
            this.BRANCHNAME = BRANCHNAME;
            this.AREA = AREA;
            this.REGION = REGION;
            this.STATUS = STATUS;
            this.ALLOWANCEELIGIBILITY = ALLOWANCEELIGIBILITY;
            this.MINIMUMMONTHLYPREMIUMSLAB1 = MINIMUMMONTHLYPREMIUMSLAB1;
            this.MINIMUMMONTHLYPREMIUMSLAB2 = MINIMUMMONTHLYPREMIUMSLAB2;
            this.MTDREGULARPREMIUMAMOUNT = MTDREGULARPREMIUMAMOUNT;
            this.PREMIUMSHORTAGEFORSLAB1 = PREMIUMSHORTAGEFORSLAB1;
            this.PREMIUMSHORTAGEFORSLAB2 = PREMIUMSHORTAGEFORSLAB2;
            this.CURRENTQUALIFICATIONSLAB = CURRENTQUALIFICATIONSLAB;
            this.REMARK = REMARK;
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

        public String getSTATUS() {
            return STATUS;
        }

        public String getALLOWANCEELIGIBILITY() {
            return ALLOWANCEELIGIBILITY;
        }

        public String getMINIMUMMONTHLYPREMIUMSLAB1() {
            return MINIMUMMONTHLYPREMIUMSLAB1;
        }

        public String getMINIMUMMONTHLYPREMIUMSLAB2() {
            return MINIMUMMONTHLYPREMIUMSLAB2;
        }

        public String getMTDREGULARPREMIUMAMOUNT() {
            return MTDREGULARPREMIUMAMOUNT;
        }

        public String getPREMIUMSHORTAGEFORSLAB1() {
            return PREMIUMSHORTAGEFORSLAB1;
        }

        public String getPREMIUMSHORTAGEFORSLAB2() {
            return PREMIUMSHORTAGEFORSLAB2;
        }

        public String getCURRENTQUALIFICATIONSLAB() {
            return CURRENTQUALIFICATIONSLAB;
        }

        public String getREMARK() {
            return REMARK;
        }
    }
}