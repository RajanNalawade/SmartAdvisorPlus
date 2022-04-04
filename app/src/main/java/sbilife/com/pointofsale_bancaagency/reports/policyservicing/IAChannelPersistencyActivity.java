package sbilife.com.pointofsale_bancaagency.reports.policyservicing;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.Utility;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;
import sbilife.com.pointofsale_bancaagency.utility.GridHeight;

public class IAChannelPersistencyActivity extends AppCompatActivity implements View.OnClickListener, ServiceHits.DownLoadData {

    private final String METHOD_NAME = "getPersistency_IAC";
    private CommonMethods commonMethods;
    private Context context;
    private ProgressDialog mProgressDialog;

    private String strCIFBDMUserId = "";
    private String strCIFBDMEmailId = "";
    private String strCIFBDMPassword = "";
    private String strCIFBDMMObileNo = "";

    private SelectedAdapterPer selectedAdapterPer;

    private DownloadFileAsyncPer taskPer;


    private String strMonthsPer = "";

    private ListView lstPer;
    private TextView txterrordescper, textviewThirteenMonnthRollingPer, textviewThirteenMonnthUMPersistency;
    private LinearLayout linearLayoutPersistencyUM;
    private Spinner spnmonths;
    private List<IAChannelPersistency> lst;

    // private Adapter_Reports_RecyclerView   adapter_reports_recyclerView;
    private GridView gridviewPersistency;
    private HorizontalScrollView horizontalScrollViewPersistency;
    private String strUserType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_i_a_channel_persistency);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();

        mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        commonMethods.setApplicationToolbarMenu(this, "Persistency for IA Channel");

        mProgressDialog.setButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (taskPer != null) {
                            taskPer.cancel(true);
                        }
                        if (mProgressDialog != null) {
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }
                    }
                });

        mProgressDialog.setMax(100);
        lstPer = findViewById(R.id.lstPer);
        txterrordescper = findViewById(R.id.txterrordescper);
        textviewThirteenMonnthRollingPer = findViewById(R.id.textviewThirteenMonnthRollingPer);
        textviewThirteenMonnthUMPersistency = findViewById(R.id.textviewThirteenMonnthUMPersistency);
        linearLayoutPersistencyUM = findViewById(R.id.linearLayoutPersistencyUM);

        spnmonths = findViewById(R.id.spnmonths);
        gridviewPersistency = findViewById(R.id.gridviewPersistency);


        horizontalScrollViewPersistency = findViewById(R.id.horizontalScrollViewPersistency);

        Button btn_per = findViewById(R.id.btn_per);
        btn_per.setOnClickListener(this);

        Intent intent = getIntent();

        String fromHome = intent.getStringExtra("fromHome");

        if (fromHome != null && fromHome.equalsIgnoreCase("N")) {

            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");
            strUserType = intent.getStringExtra("strUserType");

            try {
                strCIFBDMPassword = SimpleCrypto.encrypt("SBIL", "sbil");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            strUserType = commonMethods.GetUserType(context);
            getUserDetails();

        }
    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

    }

    @Override
    public void downLoadData() {
        taskPer = new DownloadFileAsyncPer();
        taskPer.execute();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_per) {
            getPersistency();
        }
    }

    private void getPersistency() {
        taskPer = new DownloadFileAsyncPer();
        lstPer.setVisibility(View.VISIBLE);
        textviewThirteenMonnthRollingPer.setVisibility(View.GONE);
        textviewThirteenMonnthUMPersistency.setVisibility(View.GONE);
        txterrordescper.setVisibility(View.GONE);
        linearLayoutPersistencyUM.setVisibility(View.GONE);
        gridviewPersistency.setVisibility(View.GONE);
        horizontalScrollViewPersistency.setVisibility(View.GONE);

        if (selectedAdapterPer != null) {
            lst = new ArrayList<>();
            lst.clear();
            selectedAdapterPer.clear();
            selectedAdapterPer.notifyDataSetChanged();
        }
        strMonthsPer = spnmonths.getSelectedItem().toString();

        if (commonMethods.isNetworkConnected(context)) {
            service_hits();
        } else {
            commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
        }
    }

    private void service_hits() {
        ServiceHits service = new ServiceHits(context,
                METHOD_NAME, strMonthsPer, strCIFBDMUserId,
                strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();

    }

    class SelectedAdapterPer extends ArrayAdapter<IAChannelPersistency> {


        SelectedAdapterPer(Context context,
                           List<IAChannelPersistency> objects) {
            super(context, 0, objects);
            lst = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            TextView textviewPersistencyEmpId = null, textviewEmployeeName = null, textviewCollected = null, textviewPersistency = null, textviewCollectable = null,
                    textviewUnpaidPolicyCount = null, textviewPaidPolicyCount = null;
            TextView txthytdcldprem = null, txthytdclbprem = null, txthytdratio = null, textviewCommonTableRowFour = null, txtytdcldprem = null,
                    txtytdclbprem = null, txtytdratio = null, txtfyclbprem = null, txtfyratio = null;
            TableRow tablerowFive = null;

            if (!strMonthsPer.equalsIgnoreCase("25")) {
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater) this.getContext()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.list_item_persistency_new, null);
                }
                textviewPersistencyEmpId = v.findViewById(R.id.textviewPersistencyEmpId);
                textviewEmployeeName = v.findViewById(R.id.textviewEmployeeName);
                textviewCollected = v.findViewById(R.id.textviewCollected);
                textviewPersistency = v.findViewById(R.id.textviewPersistency);
                textviewCollectable = v.findViewById(R.id.textviewCollectable);
                textviewUnpaidPolicyCount = v.findViewById(R.id.textviewUnpaidPolicyCount);
                textviewPaidPolicyCount = v.findViewById(R.id.textviewPaidPolicyCount);
                textviewPaidPolicyCount.setVisibility(View.VISIBLE);
            } else {
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater) this.getContext()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.list_per, null);
                }
                txthytdcldprem = v
                        .findViewById(R.id.txthytdcurmntcltdpre);
                txthytdclbprem = v
                        .findViewById(R.id.txthytdcurmntclbpre);
                txthytdratio = v
                        .findViewById(R.id.txthytdcurmntratio);
                textviewCommonTableRowFour = v
                        .findViewById(R.id.textviewCommonTableRowFour);
                txtytdcldprem = v
                        .findViewById(R.id.txtytdcurmntcltdpre);
                txtytdclbprem = v
                        .findViewById(R.id.txtytdcurmntclbpre);
                txtytdratio = v
                        .findViewById(R.id.txtytdcurmntratio);
                txtfyclbprem = v
                        .findViewById(R.id.txtfycltbprem);
                txtfyratio = v
                        .findViewById(R.id.txtfyratio);

                tablerowFive = v.findViewById(R.id.tablerowFive);
            }

            Object obj = null;
            boolean i = lst.contains(null);

            if (!i) {

                if (!strMonthsPer.equalsIgnoreCase("25")) {

                    String empID = lst.get(position).getEMPLOYEEID();

                    String collected = lst.get(position)
                            .getCurmonth_collected_pre() == "" ? "0.0"
                            : lst.get(position)
                            .getCurmonth_collected_pre();

                    String collectable = lst.get(position)
                            .getCurmonth_collectable_pre() == "" ? "0.0"
                            : lst.get(position)
                            .getCurmonth_collectable_pre();

                    if (position == lst.size() - 1) {
                        int colorInt = Color.parseColor("#ADD8E6");
                        textviewPersistencyEmpId.setBackgroundColor(colorInt);
                        textviewCollected.setBackgroundColor(colorInt);
                        textviewPersistency.setBackgroundColor(colorInt);
                        textviewCollectable.setBackgroundColor(colorInt);
                        textviewUnpaidPolicyCount.setBackgroundColor(colorInt);
                        textviewPersistencyEmpId.setBackgroundColor(colorInt);
                        textviewPaidPolicyCount.setBackgroundColor(colorInt);
                    }

                    textviewPersistencyEmpId.setText(empID);
                    textviewEmployeeName.setText(lst.get(position).getEMPLOYEENAME());
                    textviewCollected.setText(collected);
                    textviewPersistency.setText(lst.get(position).getCurmonth_ratio());
                    textviewCollectable.setText(collectable);
                    textviewUnpaidPolicyCount.setText(lst.get(position)
                            .getUNPAID_POLICYCOUNT() == "" ? "0.0"
                            : lst.get(position).getUNPAID_POLICYCOUNT());

                    textviewPaidPolicyCount.setText(lst.get(position)
                            .getPAID_POLICYCOUNT() == "" ? "0"
                            : lst.get(position).getPAID_POLICYCOUNT());
                } else {
                    Calendar cal = Calendar.getInstance();
                    String monthName = new SimpleDateFormat("MMM")
                            .format(cal.getTime());

                    String strYTD_cur_month_collected_pre = "YTD " + monthName
                            + " Collected";
                    String strYTD_cur_month_collectable_pre = "YTD " + monthName
                            + " Collectable";
                    String strYTD_cur_month_ratio = "YTD " + monthName + " Ratio";


                    tablerowFive.setVisibility(View.VISIBLE);

                    txthytdcldprem.setText(strYTD_cur_month_collected_pre);
                    txthytdclbprem.setText(strYTD_cur_month_collectable_pre);
                    txthytdratio.setText(strYTD_cur_month_ratio);
                    textviewCommonTableRowFour.setText("FY Collectable ");

                    txtytdcldprem
                            .setText(lst.get(position)
                                    .getCurmonth_collected_pre() == "" ? "0.0"
                                    : lst.get(position)
                                    .getCurmonth_collected_pre());
                    txtytdclbprem.setText(lst.get(position)
                            .getCurmonth_collectable_pre() == "" ? "0.0"
                            : lst.get(position)
                            .getCurmonth_collectable_pre());
                    txtfyclbprem.setText(lst.get(position)
                            .getMarch_collectable_pre() == "" ? "0.0" : lst
                            .get(position).getMarch_collectable_pre());


                    txtytdratio.setText(lst.get(position).getCurmonth_ratio());
                    txtfyratio.setText(lst.get(position).getFy_ratio());
                }
            }

            return (v);
        }
    }


    public class IAChannelPersistency {

        private String curmonth_collected_pre;
        private String curmonth_collectable_pre;
        private String march_collectable_pre;
        private String curmonth_ratio;
        private String fy_ratio;
        private String UNPAID_POLICYCOUNT;
        private String EMPLOYEEID;
        private String EMPLOYEENAME;
        private int colorCode;
        private String PAID_POLICYCOUNT;

        /*<EMPLOYEEID>28309</EMPLOYEEID>
        <EMPLOYEENAME>SINDER SAINI</EMPLOYEENAME>
        <COLLECTABLE>47.654214</COLLECTABLE>
        <COLLECTED>41.121060</COLLECTED>
        <UNPAID_POLICYCOUNT>21</UNPAID_POLICYCOUNT>
        <PAID_POLICYCOUNT>125</PAID_POLICYCOUNT>*/


        public IAChannelPersistency(String curmonth_collected_pre, String curmonth_collectable_pre, String march_collectable_pre, String curmonth_ratio, String fy_ratio, String UNPAID_POLICYCOUNT, String EMPLOYEEID, String EMPLOYEENAME, int colorCode, String PAID_POLICYCOUNT) {
            this.curmonth_collected_pre = curmonth_collected_pre;
            this.curmonth_collectable_pre = curmonth_collectable_pre;
            this.march_collectable_pre = march_collectable_pre;
            this.curmonth_ratio = curmonth_ratio;
            this.fy_ratio = fy_ratio;
            this.UNPAID_POLICYCOUNT = UNPAID_POLICYCOUNT;
            this.EMPLOYEEID = EMPLOYEEID;
            this.EMPLOYEENAME = EMPLOYEENAME;
            this.colorCode = colorCode;
            this.PAID_POLICYCOUNT = PAID_POLICYCOUNT;
        }

        public String getPAID_POLICYCOUNT() {
            return PAID_POLICYCOUNT;
        }

        public String getCurmonth_collected_pre() {
            return curmonth_collected_pre;
        }

        public String getUNPAID_POLICYCOUNT() {
            return UNPAID_POLICYCOUNT;
        }


        public String getCurmonth_collectable_pre() {
            return curmonth_collectable_pre;
        }


        public String getMarch_collectable_pre() {
            return march_collectable_pre;
        }


        public String getCurmonth_ratio() {
            return curmonth_ratio;
        }


        public String getFy_ratio() {
            return fy_ratio;
        }


        public String getEMPLOYEEID() {
            return EMPLOYEEID;
        }

        public String getEMPLOYEENAME() {
            return EMPLOYEENAME;
        }

        public int getColorCode() {
            return colorCode;
        }
    }

    public List<IAChannelPersistency> parseNodeElementPersistency(
            List<String> lsNode, String strMonth) {

        List<IAChannelPersistency> lsData = new ArrayList<IAChannelPersistency>();
        ParseXML parseXML = new ParseXML();
        if (!strMonth.equalsIgnoreCase("25")) {

            double totalCollectable = 0;
            double totalCollected = 0;
            //  double totalPersistency = 0.0;
            double totalUnpaidCount = 0;
            double totalPaidCount = 0;

            for (int i = 0; i < lsNode.size(); i++) {

                String Node = lsNode.get(i);

                String collected = parseXML.parseXmlTag(Node,
                        "COLLECTED");

                String collectable = parseXML.parseXmlTag(Node,
                        "COLLECTABLE");
                String UNPAID_POLICYCOUNT = parseXML.parseXmlTag(Node,
                        "UNPAID_POLICYCOUNT");

                String EMPLOYEEID = parseXML.parseXmlTag(Node,
                        "EMPLOYEEID");
                String EMPLOYEENAME = parseXML.parseXmlTag(Node,
                        "EMPLOYEENAME");
                String PAID_POLICYCOUNT = parseXML.parseXmlTag(Node, "PAID_POLICYCOUNT");
                String curmonth_ratio = "";
                if (collected.contentEquals("null")
                        || collectable.contentEquals("null")) {
                    curmonth_ratio = "0";
                } else {
                    double dbl_cltd = Double.parseDouble(collected);
                    double dbl_cltb = Double
                            .parseDouble(collectable);

                    collected = String.format("%.2f", dbl_cltd);
                    collectable = String.format("%.2f", dbl_cltb);
                    double dbl_per = 0;
                    if (dbl_cltb != 0) {
                        dbl_per = (dbl_cltd / dbl_cltb) * 100;
                    }
                    curmonth_ratio = String.format("%.2f", dbl_per);

                    totalCollectable = totalCollectable + dbl_cltb;
                    totalCollected = totalCollected + dbl_cltd;
                    //totalPersistency = totalPersistency + dbl_per;
                    totalUnpaidCount = totalUnpaidCount + Double.parseDouble(UNPAID_POLICYCOUNT);
                    totalPaidCount = totalPaidCount + Double.parseDouble(PAID_POLICYCOUNT);
                }
                IAChannelPersistency nodeVal = new IAChannelPersistency(
                        collected == null ? "" : collected,
                        collectable == null ? "" : collectable, "",
                        curmonth_ratio + "%", "", UNPAID_POLICYCOUNT, EMPLOYEEID, EMPLOYEENAME,
                        Color.parseColor("#ffffff"), PAID_POLICYCOUNT);
                lsData.add(nodeVal);
            }

            // double averagePersistency = totalPersistency / lsNode.size();

            double averagePersistency = 0;
            if (totalCollectable != 0) {
                averagePersistency = (totalCollected / totalCollectable) * 100;
            }

            String collectable = String.format("%.2f", totalCollectable);
            String collected = String.format("%.2f", totalCollected);
            String perAverage = String.format("%.2f", averagePersistency) + "%";
            long unpaidLong = ((long) totalUnpaidCount);
            long paidLong = ((long) totalPaidCount);
            int colorInt = Color.parseColor("#ADD8E6");
            IAChannelPersistency nodeVal = new IAChannelPersistency(collected,
                    collectable, "",
                    perAverage, "", unpaidLong + "", "Total", ""
                    , colorInt, paidLong + "");
            lsData.add(nodeVal);
        } else {

            Calendar cal = Calendar.getInstance();
            String monthName = new SimpleDateFormat("MMM").format(cal.getTime());

            String curmonth_collected_pre = monthName.toUpperCase()
                    + "_COLLECTEDPREMIUM";
            String curmonth_collectable_pre = monthName.toUpperCase()
                    + "_COLLECTABLEPREMIUM";
            String curmonth_ratio = "";
            String fy_ratio = "";

            for (String Node : lsNode) {

                String strcurmonth_collected_pre = parseXML.parseXmlTag(Node,
                        curmonth_collected_pre);

                String strcurmonth_collectable_pre = parseXML.parseXmlTag(Node,
                        curmonth_collectable_pre);

                String strmarch_collectable_pre = parseXML.parseXmlTag(Node,
                        "MAR_COLLECTABLEPREMIUM");
                String PAID_POLICYCOUNT = parseXML.parseXmlTag(Node, "PAID_POLICYCOUNT");
                if (strcurmonth_collected_pre.contentEquals("null")
                        || strcurmonth_collectable_pre.contentEquals("null")) {
                    curmonth_ratio = "0";
                } else {
                    double dbl_cltd = Double.parseDouble(strcurmonth_collected_pre);
                    double dbl_cltb = Double
                            .parseDouble(strcurmonth_collectable_pre);
                    double dbl_per = 0;
                    if (dbl_cltb != 0) {
                        dbl_per = (dbl_cltd / dbl_cltb) * 100;
                    }
                    curmonth_ratio = String.valueOf(Math.round(dbl_per));
                }

                if (strmarch_collectable_pre.contentEquals("null")) {
                    fy_ratio = "0";
                } else {
                    double dbl_cltd = Double.parseDouble(strcurmonth_collected_pre);
                    double dbl_cltb = Double.parseDouble(strmarch_collectable_pre);
                    double dbl_per = (dbl_cltd / dbl_cltb) * 100;
                    fy_ratio = String.valueOf(Math.round(dbl_per));
                }

                IAChannelPersistency nodeVal = new IAChannelPersistency(
                        strcurmonth_collected_pre == null ? ""
                                : strcurmonth_collected_pre,
                        strcurmonth_collectable_pre == null ? ""
                                : strcurmonth_collectable_pre,
                        strmarch_collectable_pre == null ? ""
                                : strmarch_collectable_pre, curmonth_ratio + "%",
                        fy_ratio + "%", "", "", "",
                        0, PAID_POLICYCOUNT);

                lsData.add(nodeVal);

            }
        }

        return lsData;
    }

    class DownloadFileAsyncPer extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String strpersistencylist = "", strpersistencyErrorCOde = "", strpersistencyErCd = "";
        private ArrayList<IAChannelPersistency> lsPersistency;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                SoapObject request;

                int m = 0;
                System.out.println("month per: " + strMonthsPer);
                if (strMonthsPer.contentEquals("13")) {
                    m = 1;
                } else if (strMonthsPer.contentEquals("25")) {
                    m = 2;
                } else if (strMonthsPer.contentEquals("37")) {
                    m = 3;
                } else if (strMonthsPer.contentEquals("61")) {
                    m = 4;
                }

                Calendar cal = Calendar.getInstance();
                String monthName = new SimpleDateFormat("MMM").format(cal
                        .getTime());

                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME);

                request.addProperty("strAgenyCode", strCIFBDMUserId);
                request.addProperty("checkFlag", m);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());
                request.addProperty("strMonth", monthName.toUpperCase());

                System.out.println(strCIFBDMUserId + " v " + m + " " + strCIFBDMEmailId + " " + strCIFBDMMObileNo + " " + monthName);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();
                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl, 800000);
                try {

                    String SOAP_ACTION_PERSISTENCY = "http://tempuri.org/" + METHOD_NAME;
                    androidHttpTranport.call(SOAP_ACTION_PERSISTENCY, envelope);
                    Object response = envelope.getResponse();

                    System.out.println("response==:" + response.toString());
                    if (!response.toString().contentEquals("anyType{}")) {

                        SoapPrimitive sa;
                        try {
                            sa = (SoapPrimitive) envelope.getResponse();
                            strpersistencylist = sa.toString();
                            ParseXML prsObj = new ParseXML();

                            strpersistencylist = prsObj.parseXmlTag(
                                    strpersistencylist, "NewDataSet");
                            strpersistencyErCd = strpersistencylist;

                            if (strpersistencyErCd != null) {
                                strpersistencylist = sa.toString();

                                strpersistencylist = new ParseXML()
                                        .parseXmlTag(strpersistencylist,
                                                "NewDataSet");
                                strpersistencylist = new ParseXML()
                                        .parseXmlTag(strpersistencylist,
                                                "ScreenData");
                                strpersistencyErrorCOde = strpersistencylist;

                                if (strpersistencyErrorCOde == null) {

                                    strpersistencylist = sa.toString();
                                    strpersistencylist = prsObj.parseXmlTag(
                                            strpersistencylist, "NewDataSet");

                                    List<String> Node = prsObj.parseParentNode(
                                            strpersistencylist, "Table");

                                    List<IAChannelPersistency> nodeData = parseNodeElementPersistency(Node, strMonthsPer);

                                    lsPersistency = new ArrayList<>();
                                    lsPersistency.clear();

                                    lsPersistency.addAll(nodeData);

                                    selectedAdapterPer = new SelectedAdapterPer(
                                            context, lsPersistency);
                                    selectedAdapterPer.setNotifyOnChange(true);
                                    //registerForContextMenu(lstPer);
                                }

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

                mProgressDialog.dismiss();
                running = false;
            }
            return null;

        }

        @Override
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            textviewThirteenMonnthRollingPer.setVisibility(View.GONE);
            textviewThirteenMonnthUMPersistency.setVisibility(View.GONE);
            linearLayoutPersistencyUM.setVisibility(View.GONE);
            gridviewPersistency.setVisibility(View.GONE);
            txterrordescper.setVisibility(View.GONE);
            horizontalScrollViewPersistency.setVisibility(View.GONE);
            if (running) {


                if (strpersistencyErCd == null) {
                    txterrordescper.setVisibility(View.VISIBLE);
                    txterrordescper.setText("No Record Found");
                    List<IAChannelPersistency> lst;
                    IAChannelPersistency node = null;
                    lst = new ArrayList<>();
                    lst.clear();
                    lst.add(null);
                    selectedAdapterPer = new SelectedAdapterPer(context, lst);
                    selectedAdapterPer.setNotifyOnChange(true);
                    lstPer.setAdapter(selectedAdapterPer);

                    Utility.setListViewHeightBasedOnChildren(lstPer);

                    lstPer.setVisibility(View.GONE);
                } else {
                    if (strpersistencyErrorCOde == null) {

                        if (!strMonthsPer.equalsIgnoreCase("25")) {
                            if (strMonthsPer.contentEquals("61") || strMonthsPer.contentEquals("37")) {
                                textviewThirteenMonnthRollingPer.setVisibility(View.GONE);
                            } else {
                                textviewThirteenMonnthRollingPer.setVisibility(View.VISIBLE);
                            }

                            linearLayoutPersistencyUM.setVisibility(View.VISIBLE);
                            gridviewPersistency.setVisibility(View.VISIBLE);
                            horizontalScrollViewPersistency.setVisibility(View.VISIBLE);


                            TextView textviewPersistencyEmpId = findViewById(R.id.textviewPersistencyEmpId);
                            if (strUserType.equalsIgnoreCase("UM")) {
                                textviewThirteenMonnthUMPersistency.setVisibility(View.VISIBLE);
                                textviewPersistencyEmpId.setText("IA Code");
                            } else {
                                textviewPersistencyEmpId.setText("Code");
                            }

                            linearLayoutPersistencyUM.setBackgroundColor(Color.parseColor("#ADD8E6"));

                            Adapter_PersistencyGrid adapter = new Adapter_PersistencyGrid(
                                    context, lsPersistency);
                            gridviewPersistency.setAdapter(adapter);

                            GridHeight gh = new GridHeight();
                            gh.getheight(gridviewPersistency, String.valueOf(lsPersistency.size()));

                            gridviewPersistency.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    if (!strMonthsPer.equalsIgnoreCase("25")) {
                                        String employeeId = lst.get(position).getEMPLOYEEID();
                                        if (employeeId.contains("IA - ")) {
                                            employeeId = employeeId.replace("IA - ", "").trim();
                                        } else if (employeeId.contains("CIF - ")) {
                                            employeeId = employeeId.replace("CIF - ", "").trim();
                                        }

                                        if (employeeId.equalsIgnoreCase("Total")) {
                                            employeeId = strCIFBDMUserId;
                                        }

                                        Intent i = new Intent(context, IAChannelPersistencyDueDataActivity.class);
                                        i.putExtra("strBDMCifCOde", employeeId);
                                        startActivity(i);
                                    }

                                }
                            });


                        } else {
                            txterrordescper.setText("");
                            lstPer.setAdapter(selectedAdapterPer);
                            Utility.setListViewHeightBasedOnChildren(lstPer);

                            lstPer.setVisibility(View.VISIBLE);

                            lstPer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (!strMonthsPer.equalsIgnoreCase("25")) {


                                        String employeeId = lst.get(position).getEMPLOYEEID();
                                        if (employeeId.contains("IA - ")) {
                                            employeeId = employeeId.replace("IA - ", "");
                                        }

                                        if (employeeId.equalsIgnoreCase("Total")) {
                                            employeeId = strCIFBDMUserId;
                                        }

                                        Intent i = new Intent(context, IAChannelPersistencyDueDataActivity.class);
                                        i.putExtra("strBDMCifCOde", employeeId);
                                        startActivity(i);
                                    }
                                }
                            });
                        }


                    } else {
                        txterrordescper.setText("No Record Found");
                        List<IAChannelPersistency> lst;
                        IAChannelPersistency node = null;
                        lst = new ArrayList<>();
                        lst.clear();
                        lst.add(null);
                        selectedAdapterPer = new SelectedAdapterPer(context,
                                lst);
                        selectedAdapterPer.setNotifyOnChange(true);
                        lstPer.setAdapter(selectedAdapterPer);

                        Utility.setListViewHeightBasedOnChildren(lstPer);

                        lstPer.setVisibility(View.GONE);
                    }
                }
            } else {
                txterrordescper.setText("No Record Found");
                List<IAChannelPersistency> lst;
                IAChannelPersistency node = null;
                lst = new ArrayList<>();
                lst.clear();
                lst.add(null);
                selectedAdapterPer = new SelectedAdapterPer(context,
                        lst);
                selectedAdapterPer.setNotifyOnChange(true);
                lstPer.setAdapter(selectedAdapterPer);

                Utility.setListViewHeightBasedOnChildren(lstPer);

                lstPer.setVisibility(View.GONE);
                // mCommonMethods.showMessageDialog(context, mCommonMethods.SERVER_ERROR);
            }
        }
    }

    class Adapter_PersistencyGrid extends BaseAdapter {

        class ViewHolder {
            TextView textviewPersistencyEmpId, textviewEmployeeName, textviewCollected, textviewPersistency, textviewCollectable,
                    textviewUnpaidPolicyCount, textviewPaidPolicyCount;
        }

        final List<IAChannelPersistency> lst;
        final Context mContext;

        /*private int[] colors = new int[] { Color.parseColor("#DCDBDB"),
                Color.parseColor("#E8E8E8") };*/

        Adapter_PersistencyGrid(Context context, List<IAChannelPersistency> results) {
            lst = results;
            mContext = context;
        }


        public int getCount() {
            return lst.size();
        }


        public Object getItem(int position) {
            return null;
        }


        public long getItemId(int position) {
            return position;
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            Adapter_PersistencyGrid.ViewHolder holder;
            if (convertView == null) {
                LayoutInflater mInflater = LayoutInflater.from(mContext);
                convertView = mInflater.inflate(R.layout.list_item_persistency_new,
                        null);
                holder = new Adapter_PersistencyGrid.ViewHolder();
                holder.textviewPersistencyEmpId = convertView.findViewById(R.id.textviewPersistencyEmpId);
                holder.textviewEmployeeName = convertView.findViewById(R.id.textviewEmployeeName);
                holder.textviewCollected = convertView.findViewById(R.id.textviewCollected);
                holder.textviewPersistency = convertView.findViewById(R.id.textviewPersistency);
                holder.textviewCollectable = convertView.findViewById(R.id.textviewCollectable);
                holder.textviewUnpaidPolicyCount = convertView.findViewById(R.id.textviewUnpaidPolicyCount);
                holder.textviewPaidPolicyCount = convertView.findViewById(R.id.textviewPaidPolicyCount);
                holder.textviewPaidPolicyCount.setVisibility(View.VISIBLE);
                convertView.setTag(holder);
            } else {
                holder = (Adapter_PersistencyGrid.ViewHolder) convertView.getTag();
            }
            String empID = lst.get(position).getEMPLOYEEID();
            String collected = lst.get(position)
                    .getCurmonth_collected_pre() == "" ? "0.0"
                    : lst.get(position)
                    .getCurmonth_collected_pre();
            String collectable = lst.get(position)
                    .getCurmonth_collectable_pre() == "" ? "0.0"
                    : lst.get(position)
                    .getCurmonth_collectable_pre();

            holder.textviewPersistencyEmpId.setBackgroundColor(lst.get(position).getColorCode());
            holder.textviewCollected.setBackgroundColor(lst.get(position).getColorCode());
            holder.textviewPersistency.setBackgroundColor(lst.get(position).getColorCode());
            holder.textviewCollectable.setBackgroundColor(lst.get(position).getColorCode());
            holder.textviewUnpaidPolicyCount.setBackgroundColor(lst.get(position).getColorCode());
            holder.textviewPaidPolicyCount.setBackgroundColor(lst.get(position).getColorCode());
            holder.textviewPersistencyEmpId.setBackgroundColor(lst.get(position).getColorCode());
            holder.textviewEmployeeName.setBackgroundColor(lst.get(position).getColorCode());

            holder.textviewPersistencyEmpId.setText(empID);
            holder.textviewEmployeeName.setText(lst.get(position).getEMPLOYEENAME());
            holder.textviewCollected.setText(collected);
            holder.textviewPersistency.setText(lst.get(position).getCurmonth_ratio());
            holder.textviewCollectable.setText(collectable);

            holder.textviewUnpaidPolicyCount.setText(lst.get(position)
                    .getUNPAID_POLICYCOUNT() == "" ? "0.0"
                    : lst.get(position).getUNPAID_POLICYCOUNT());

            holder.textviewPaidPolicyCount.setText(lst.get(position)
                    .getPAID_POLICYCOUNT() == "" ? "0.0"
                    : lst.get(position).getPAID_POLICYCOUNT());

            /*int colorPos = position % colors.length;
            convertView.setBackgroundColor(colors[colorPos]);*/

            return convertView;
        }
    }
}