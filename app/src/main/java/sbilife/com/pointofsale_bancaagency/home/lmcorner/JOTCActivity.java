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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class JOTCActivity extends AppCompatActivity implements ServiceHits.DownLoadData {

    private final String METHOD_NAME = "getJOTC";
    private ProgressDialog mProgressDialog;
    private ArrayList<JOTCReportValuesModel> globalDataList;
    private Context context;
    private CommonMethods commonMethods;
    private TextView textviewRecordCount;
    private RecyclerView recyclerview;

    private SelectedAdapter selectedAdapter;
    private DownloadJOTCReportAsyncTask downloadJOTCReportAsyncTask;
    private ServiceHits service;
    private String strCIFBDMUserId = "";
    private String strCIFBDMEmailId = "";
    private String strCIFBDMMObileNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_jotc);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "JOTC Report");

        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");
        if (fromHome != null && fromHome.equalsIgnoreCase("N")) {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
        } else {
            CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods
                    .setUserDetails(context);
            strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
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
                        if (downloadJOTCReportAsyncTask != null) {
                            downloadJOTCReportAsyncTask.cancel(true);
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
        String strCIFBDMPassword = commonMethods.getStrAuth();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
        service = new ServiceHits(context, METHOD_NAME, input,
                strCIFBDMUserId, strCIFBDMEmailId,
                strCIFBDMMObileNo, strCIFBDMPassword, this);
        service.execute();
    }

    @Override
    public void downLoadData() {
        downloadJOTCReportAsyncTask = new DownloadJOTCReportAsyncTask();
        downloadJOTCReportAsyncTask.execute();
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

        if (downloadJOTCReportAsyncTask != null) {
            downloadJOTCReportAsyncTask.cancel(true);
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

    public ArrayList<JOTCReportValuesModel> parseNodeJOTCReport(List<String> lsNode) {
        ArrayList<JOTCReportValuesModel> lsData = new ArrayList<>();

        String LM_CODE, LM_NAME, UM_CODE, UM_NAME, BRANCH_CODE, BRANCH_NAME, DIVISION_NAME,
                AREA_NAME, REGION, ZONE, DATE_OF_JOINING, JOTC_STATUS, GAP_TO_NEXT_SLAB,
                BALANCE_TO_RUBY, BALANCE_TO_DIAMOND, NET_RATED_AMT, YTD_ADDITIONAL_RATED_NB_CREDIT,
                TOTAL_NET_RATED_PREMIUM, PERSISTENCY_13, PROTECTION_NOP, PROTECTION_NB_YTD;

        CommonForAllProd mCommonForAllProd = new CommonForAllProd();
        ParseXML parseXML = new ParseXML();
        for (String Node : lsNode) {

            LM_CODE = parseXML.parseXmlTag(Node, "LM_CODE");
            LM_CODE = LM_CODE == null ? "" : LM_CODE;

            LM_NAME = parseXML.parseXmlTag(Node, "LM_NAME");
            LM_NAME = LM_NAME == null ? "" : LM_NAME;

            UM_CODE = parseXML.parseXmlTag(Node, "UM_CODE");
            UM_CODE = UM_CODE == null ? "" : UM_CODE;

            UM_NAME = parseXML.parseXmlTag(Node, "UM_NAME");
            UM_NAME = UM_NAME == null ? "" : UM_NAME;

            BRANCH_CODE = parseXML.parseXmlTag(Node, "BRANCH_CODE");
            BRANCH_CODE = BRANCH_CODE == null ? "" : BRANCH_CODE;

            BRANCH_NAME = parseXML.parseXmlTag(Node, "BRANCH_NAME");
            BRANCH_NAME = BRANCH_NAME == null ? "" : BRANCH_NAME;

            DIVISION_NAME = parseXML.parseXmlTag(Node, "DIVISION_NAME");
            DIVISION_NAME = DIVISION_NAME == null ? "" : DIVISION_NAME;

            AREA_NAME = parseXML.parseXmlTag(Node, "AREA_NAME");
            AREA_NAME = AREA_NAME == null ? "" : AREA_NAME;

            REGION = parseXML.parseXmlTag(Node, "REGION");
            REGION = REGION == null ? "" : REGION;

            ZONE = parseXML.parseXmlTag(Node, "ZONE");
            ZONE = ZONE == null ? "" : ZONE;

            DATE_OF_JOINING = parseXML.parseXmlTag(Node, "DATE_OF_JOINING");
            DATE_OF_JOINING = DATE_OF_JOINING == null ? "" : DATE_OF_JOINING;


            if (DATE_OF_JOINING == null) {
                DATE_OF_JOINING = "";
            } else {

                DATE_OF_JOINING = DATE_OF_JOINING.split("T")[0];

                Date dt1 = null;
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat df1 = new SimpleDateFormat("dd-MMMM-yyyy");
                try {
                    dt1 = df.parse(DATE_OF_JOINING);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DATE_OF_JOINING = df1.format(dt1);

            }

            JOTC_STATUS = parseXML.parseXmlTag(Node, "JOTC_STATUS");
            JOTC_STATUS = JOTC_STATUS == null ? "" : JOTC_STATUS;

            GAP_TO_NEXT_SLAB = parseXML.parseXmlTag(Node, "GAP_TO_NEXT_SLAB");
            GAP_TO_NEXT_SLAB = GAP_TO_NEXT_SLAB == null ? "" : GAP_TO_NEXT_SLAB;
            GAP_TO_NEXT_SLAB = mCommonForAllProd.roundUp_Level2(GAP_TO_NEXT_SLAB);

            BALANCE_TO_RUBY = parseXML.parseXmlTag(Node, "BALANCE_TO_RUBY");
            BALANCE_TO_RUBY = BALANCE_TO_RUBY == null ? "" : BALANCE_TO_RUBY;
            BALANCE_TO_RUBY = mCommonForAllProd.roundUp_Level2(BALANCE_TO_RUBY);

            BALANCE_TO_DIAMOND = parseXML.parseXmlTag(Node, "BALANCE_TO_DIAMOND");
            BALANCE_TO_DIAMOND = BALANCE_TO_DIAMOND == null ? "" : BALANCE_TO_DIAMOND;
            BALANCE_TO_DIAMOND = mCommonForAllProd.roundUp_Level2(BALANCE_TO_DIAMOND);

            NET_RATED_AMT = parseXML.parseXmlTag(Node, "NET_RATED_AMT");
            NET_RATED_AMT = NET_RATED_AMT == null ? "" : NET_RATED_AMT;
            NET_RATED_AMT = mCommonForAllProd.roundUp_Level2(NET_RATED_AMT);

            YTD_ADDITIONAL_RATED_NB_CREDIT = parseXML.parseXmlTag(Node, "YTD_ADDITIONAL_RATED_NB_CREDIT");
            YTD_ADDITIONAL_RATED_NB_CREDIT = YTD_ADDITIONAL_RATED_NB_CREDIT == null ? "" : YTD_ADDITIONAL_RATED_NB_CREDIT;

            TOTAL_NET_RATED_PREMIUM = parseXML.parseXmlTag(Node, "TOTAL_NET_RATED_PREMIUM");
            TOTAL_NET_RATED_PREMIUM = TOTAL_NET_RATED_PREMIUM == null ? "" : TOTAL_NET_RATED_PREMIUM;
            TOTAL_NET_RATED_PREMIUM = mCommonForAllProd.roundUp_Level2(TOTAL_NET_RATED_PREMIUM);

            PERSISTENCY_13 = parseXML.parseXmlTag(Node, "PERSISTENCY_13");
            PERSISTENCY_13 = PERSISTENCY_13 == null ? "" : PERSISTENCY_13;

            PROTECTION_NOP = parseXML.parseXmlTag(Node, "PROTECTION_NOP");
            PROTECTION_NOP = PROTECTION_NOP == null ? "" : PROTECTION_NOP;

            PROTECTION_NB_YTD = parseXML.parseXmlTag(Node, "PROTECTION_NB_YTD");
            PROTECTION_NB_YTD = PROTECTION_NB_YTD == null ? "" : PROTECTION_NB_YTD;

            JOTCReportValuesModel nodeVal = new JOTCReportValuesModel(LM_CODE, LM_NAME, UM_CODE, UM_NAME, BRANCH_CODE, BRANCH_NAME, DIVISION_NAME, AREA_NAME, REGION, ZONE, DATE_OF_JOINING, JOTC_STATUS, GAP_TO_NEXT_SLAB, BALANCE_TO_RUBY, BALANCE_TO_DIAMOND, NET_RATED_AMT, YTD_ADDITIONAL_RATED_NB_CREDIT, TOTAL_NET_RATED_PREMIUM, PERSISTENCY_13, PROTECTION_NOP, PROTECTION_NB_YTD);
            lsData.add(nodeVal);
        }
        return lsData;
    }

    class DownloadJOTCReportAsyncTask extends AsyncTask<String, String, String> {

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
                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "Data");
                    error = prsObj.parseXmlTag(
                            inputpolicylist, "ScreenData");

                    if (error == null) {
                        // for agent policy list
                        List<String> Node = prsObj.parseParentNode(inputpolicylist, "Table");
                        List<JOTCReportValuesModel> nodeData = parseNodeJOTCReport(Node);
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

        private final ArrayList<JOTCReportValuesModel> lstAdapterList;

        SelectedAdapter(ArrayList<JOTCReportValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }


        @Override
        public int getItemCount() {
            return lstAdapterList.size();
        }

        @Override
        public ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_item_jotc_report, parent, false);

            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final ViewHolderAdapter holder, int position) {

            holder.textviewLMCode.setText(lstAdapterList.get(position).getLM_CODE());
            holder.txtLMName.setText(lstAdapterList.get(position).getLM_NAME());
            holder.textviewUMCode.setText(lstAdapterList.get(position).getUM_CODE());
            holder.txtUMName.setText(lstAdapterList.get(position).getUM_NAME());
            holder.textviewBranchName.setText(lstAdapterList.get(position).getBRANCH_NAME());
            holder.textviewBranchCode.setText(lstAdapterList.get(position).getBRANCH_CODE());
            holder.textviewDivision.setText(lstAdapterList.get(position).getDIVISION_NAME());
            holder.textviewArea.setText(lstAdapterList.get(position).getAREA_NAME());
            holder.textviewRegion.setText(lstAdapterList.get(position).getREGION());
            holder.textviewZone.setText(lstAdapterList.get(position).getZONE());
            holder.textviewDateOfJoining.setText(lstAdapterList.get(position).getDATE_OF_JOINING());

            holder.textviewJOTCStatus.setText(lstAdapterList.get(position).getJOTC_STATUS());
            holder.textviewGapToNextSlot.setText(lstAdapterList.get(position).getGAP_TO_NEXT_SLAB());

            holder.textviewBalanceToRuby.setText(lstAdapterList.get(position).getBALANCE_TO_RUBY());
            holder.textviewBalanceToDiamond.setText(lstAdapterList.get(position).getBALANCE_TO_DIAMOND());
            holder.textviewNetRatedAmount.setText(lstAdapterList.get(position).getNET_RATED_AMT());
            holder.textviewYTDAdditionalRatedNBCredit.setText(lstAdapterList.get(position).getYTD_ADDITIONAL_RATED_NB_CREDIT());

            holder.textviewTotalNetRatedPremium.setText(lstAdapterList.get(position).getTOTAL_NET_RATED_PREMIUM());
            holder.textviewPersistencyThirteen.setText(lstAdapterList.get(position).getPERSISTENCY_13());
            holder.textviewProtectionNOP.setText(lstAdapterList.get(position).getPROTECTION_NOP());
            holder.textviewProtectionNBYTD.setText(lstAdapterList.get(position).getPROTECTION_NB_YTD());

        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder {

            private final TextView textviewLMCode, txtLMName, textviewUMCode, txtUMName, textviewBranchName,
                    textviewBranchCode, textviewDivision, textviewArea, textviewRegion,
                    textviewZone, textviewDateOfJoining, textviewJOTCStatus, textviewGapToNextSlot,
                    textviewBalanceToRuby, textviewBalanceToDiamond, textviewNetRatedAmount,
                    textviewYTDAdditionalRatedNBCredit, textviewTotalNetRatedPremium,
                    textviewPersistencyThirteen, textviewProtectionNOP, textviewProtectionNBYTD;

            ViewHolderAdapter(View v) {
                super(v);
                textviewLMCode = v.findViewById(R.id.textviewLMCode);
                txtLMName = v.findViewById(R.id.txtLMName);
                textviewUMCode = v.findViewById(R.id.textviewUMCode);
                txtUMName = v.findViewById(R.id.txtUMName);
                textviewBranchName = v.findViewById(R.id.textviewBranchName);
                textviewBranchCode = v.findViewById(R.id.textviewBranchCode);
                textviewDivision = v.findViewById(R.id.textviewDivision);
                textviewArea = v.findViewById(R.id.textviewArea);
                textviewRegion = v.findViewById(R.id.textviewRegion);
                textviewZone = v.findViewById(R.id.textviewZone);
                textviewDateOfJoining = v.findViewById(R.id.textviewDateOfJoining);

                textviewJOTCStatus = v.findViewById(R.id.textviewJOTCStatus);
                textviewGapToNextSlot = v.findViewById(R.id.textviewGapToNextSlot);

                textviewBalanceToRuby = v.findViewById(R.id.textviewBalanceToRuby);
                textviewBalanceToDiamond = v.findViewById(R.id.textviewBalanceToDiamond);
                textviewNetRatedAmount = v.findViewById(R.id.textviewNetRatedAmount);
                textviewYTDAdditionalRatedNBCredit = v.findViewById(R.id.textviewYTDAdditionalRatedNBCredit);

                textviewTotalNetRatedPremium = v.findViewById(R.id.textviewTotalNetRatedPremium);
                textviewPersistencyThirteen = v.findViewById(R.id.textviewPersistencyThirteen);
                textviewProtectionNOP = v.findViewById(R.id.textviewProtectionNOP);
                textviewProtectionNBYTD = v.findViewById(R.id.textviewProtectionNBYTD);
            }

        }

    }

    public class JOTCReportValuesModel {

        private String LM_CODE, LM_NAME, UM_CODE, UM_NAME, BRANCH_CODE, BRANCH_NAME, DIVISION_NAME,
                AREA_NAME, REGION, ZONE, DATE_OF_JOINING, JOTC_STATUS, GAP_TO_NEXT_SLAB,
                BALANCE_TO_RUBY, BALANCE_TO_DIAMOND, NET_RATED_AMT, YTD_ADDITIONAL_RATED_NB_CREDIT,
                TOTAL_NET_RATED_PREMIUM, PERSISTENCY_13, PROTECTION_NOP, PROTECTION_NB_YTD;

        public JOTCReportValuesModel(String LM_CODE, String LM_NAME, String UM_CODE, String UM_NAME, String BRANCH_CODE, String BRANCH_NAME, String DIVISION_NAME, String AREA_NAME, String REGION, String ZONE, String DATE_OF_JOINING, String JOTC_STATUS, String GAP_TO_NEXT_SLAB, String BALANCE_TO_RUBY, String BALANCE_TO_DIAMOND, String NET_RATED_AMT, String YTD_ADDITIONAL_RATED_NB_CREDIT, String TOTAL_NET_RATED_PREMIUM, String PERSISTENCY_13, String PROTECTION_NOP, String PROTECTION_NB_YTD) {
            this.LM_CODE = LM_CODE;
            this.LM_NAME = LM_NAME;
            this.UM_CODE = UM_CODE;
            this.UM_NAME = UM_NAME;
            this.BRANCH_CODE = BRANCH_CODE;
            this.BRANCH_NAME = BRANCH_NAME;
            this.DIVISION_NAME = DIVISION_NAME;
            this.AREA_NAME = AREA_NAME;
            this.REGION = REGION;
            this.ZONE = ZONE;
            this.DATE_OF_JOINING = DATE_OF_JOINING;
            this.JOTC_STATUS = JOTC_STATUS;
            this.GAP_TO_NEXT_SLAB = GAP_TO_NEXT_SLAB;
            this.BALANCE_TO_RUBY = BALANCE_TO_RUBY;
            this.BALANCE_TO_DIAMOND = BALANCE_TO_DIAMOND;
            this.NET_RATED_AMT = NET_RATED_AMT;
            this.YTD_ADDITIONAL_RATED_NB_CREDIT = YTD_ADDITIONAL_RATED_NB_CREDIT;
            this.TOTAL_NET_RATED_PREMIUM = TOTAL_NET_RATED_PREMIUM;
            this.PERSISTENCY_13 = PERSISTENCY_13;
            this.PROTECTION_NOP = PROTECTION_NOP;
            this.PROTECTION_NB_YTD = PROTECTION_NB_YTD;
        }

        public String getLM_CODE() {
            return LM_CODE;
        }

        public String getLM_NAME() {
            return LM_NAME;
        }

        public String getUM_CODE() {
            return UM_CODE;
        }

        public String getUM_NAME() {
            return UM_NAME;
        }

        public String getBRANCH_CODE() {
            return BRANCH_CODE;
        }

        public String getBRANCH_NAME() {
            return BRANCH_NAME;
        }

        public String getDIVISION_NAME() {
            return DIVISION_NAME;
        }

        public String getAREA_NAME() {
            return AREA_NAME;
        }

        public String getREGION() {
            return REGION;
        }

        public String getZONE() {
            return ZONE;
        }

        public String getDATE_OF_JOINING() {
            return DATE_OF_JOINING;
        }

        public String getJOTC_STATUS() {
            return JOTC_STATUS;
        }

        public String getGAP_TO_NEXT_SLAB() {
            return GAP_TO_NEXT_SLAB;
        }

        public String getBALANCE_TO_RUBY() {
            return BALANCE_TO_RUBY;
        }

        public String getBALANCE_TO_DIAMOND() {
            return BALANCE_TO_DIAMOND;
        }

        public String getNET_RATED_AMT() {
            return NET_RATED_AMT;
        }

        public String getYTD_ADDITIONAL_RATED_NB_CREDIT() {
            return YTD_ADDITIONAL_RATED_NB_CREDIT;
        }

        public String getTOTAL_NET_RATED_PREMIUM() {
            return TOTAL_NET_RATED_PREMIUM;
        }

        public String getPERSISTENCY_13() {
            return PERSISTENCY_13;
        }

        public String getPROTECTION_NOP() {
            return PROTECTION_NOP;
        }

        public String getPROTECTION_NB_YTD() {
            return PROTECTION_NB_YTD;
        }
    }
}




