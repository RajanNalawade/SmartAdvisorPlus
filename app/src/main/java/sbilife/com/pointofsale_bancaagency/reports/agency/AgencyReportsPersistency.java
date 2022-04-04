package sbilife.com.pointofsale_bancaagency.reports.agency;

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
import sbilife.com.pointofsale_bancaagency.reports.commonreports.CommonReportsPersistencyDueDataActivity;
import sbilife.com.pointofsale_bancaagency.utility.GridHeight;

/**
 * Created by O0110 on 09/01/2018.
 */

public class AgencyReportsPersistency extends AppCompatActivity implements View.OnClickListener, ServiceHits.DownLoadData {


    private final String METHOD_NAME_PERSISTENCY = "getPersistency";

    private SelectedAdapterPer selectedAdapterPer;

    private DownloadFileAsyncPer taskPer;

    private ProgressDialog mProgressDialog;

    private CommonMethods mCommonMethods;
    private Context context;

    private String strCIFBDMUserId = "", strCIFBDMEmailId = "",
            strCIFBDMPassword = "", strCIFBDMMObileNo = "";

    private String strMonthsPer = "";

    private ListView lstPer;
    private TextView txterrordescper, textviewThirteenMonnthRollingPer, textviewThirteenMonnthUMPersistency;
    private LinearLayout linearLayoutPersistencyUM;
    private Spinner spnmonths;
    private List<ParseXML.XMLHolderPersistency> lst;

    // private Adapter_Reports_RecyclerView   adapter_reports_recyclerView;
    private GridView gridviewPersistency;
    private HorizontalScrollView horizontalScrollViewPersistency;
    private String strUserType = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.agency_reports_persistency);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        mCommonMethods = new CommonMethods();
        mCommonMethods.setApplicationToolbarMenu(this, "Persistency");

        taskPer = new DownloadFileAsyncPer();
        mProgressDialog = new ProgressDialog(context);

        mProgressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

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
            strUserType = mCommonMethods.GetUserType(context);
            getUserDetails();

        }
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
                METHOD_NAME_PERSISTENCY, strMonthsPer, strCIFBDMUserId,
                strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();

    }


    class DownloadFileAsyncPer extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String strpersistencylist = "", strpersistencyErrorCOde = "", strpersistencyErCd = "";
        private ArrayList<ParseXML.XMLHolderPersistency> lsPersistency;

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
                }else if (strMonthsPer.contentEquals("61")) {
                    m = 4;
                }

                Calendar cal = Calendar.getInstance();
                String monthName = new SimpleDateFormat("MMM").format(cal
                        .getTime());

                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME_PERSISTENCY);

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
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl, 800000);
                try {

                    String SOAP_ACTION_PERSISTENCY = "http://tempuri.org/getPersistency";
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

                                    List<ParseXML.XMLHolderPersistency> nodeData = prsObj
                                            .parseNodeElementPersistency(Node, strMonthsPer);

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
                    List<ParseXML.XMLHolderPersistency> lst;
                    ParseXML.XMLHolderPersistency node = null;
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
                            if(strMonthsPer.contentEquals("61") ||strMonthsPer.contentEquals("37")){
                                textviewThirteenMonnthRollingPer.setVisibility(View.GONE);
                            }else{
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
                                        employeeId = employeeId == null ? "" : employeeId;

                                        if (employeeId.contains("IA - ")) {
                                            employeeId = employeeId.replace("IA - ", "").trim();
                                        } else if (employeeId.contains("CIF - ")) {
                                            employeeId = employeeId.replace("CIF - ", "").trim();
                                        }

                                        if (employeeId.equalsIgnoreCase("Total")) {
                                            employeeId = strCIFBDMUserId;
                                        }

                                        Intent i = new Intent(context, CommonReportsPersistencyDueDataActivity.class);
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

                                        Intent i = new Intent(context, CommonReportsPersistencyDueDataActivity.class);
                                        i.putExtra("strBDMCifCOde", employeeId);
                                        startActivity(i);
                                    }
                                }
                            });
                        }


                    } else {
                        txterrordescper.setText("No Record Found");
                        List<ParseXML.XMLHolderPersistency> lst;
                        ParseXML.XMLHolderPersistency node = null;
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
                List<ParseXML.XMLHolderPersistency> lst;
                ParseXML.XMLHolderPersistency node = null;
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


    private void startDownloadPersistency() {
        taskPer.execute("demo");
    }

    @Override
    public void downLoadData() {
        taskPer = new DownloadFileAsyncPer();
        startDownloadPersistency();
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

        if (mCommonMethods.isNetworkConnected(context)) {
            service_hits();
        } else {
            mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
        }
    }

    class SelectedAdapterPer extends ArrayAdapter<ParseXML.XMLHolderPersistency> {


        SelectedAdapterPer(Context context,
                           List<ParseXML.XMLHolderPersistency> objects) {
            super(context, 0, objects);
            lst = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            TextView textviewPersistencyEmpId = null, textviewEmployeeName = null, textviewCollected = null, textviewPersistency = null, textviewCollectable = null,
                    textviewUnpaidPolicyCount = null;
            TextView txthytdcldprem = null, txthytdclbprem = null, txthytdratio = null, textviewCommonTableRowFour = null, txtytdcldprem = null,
                    txtytdclbprem = null, txtytdratio = null, txtfyclbprem = null, txtfyratio = null;
            TableRow tablerowFive = null;

            if (!strMonthsPer.equalsIgnoreCase("25") ) {
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
                    }

                    textviewPersistencyEmpId.setText(empID);
                    textviewEmployeeName.setText(lst.get(position).getEMPLOYEENAME());
                    textviewCollected.setText(collected);
                    textviewPersistency.setText(lst.get(position).getCurmonth_ratio());
                    textviewCollectable.setText(collectable);
                    textviewUnpaidPolicyCount.setText(lst.get(position)
                            .getUNPAID_POLICYCOUNT() == "" ? "0.0"
                            : lst.get(position).getUNPAID_POLICYCOUNT());
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


    @Override
    protected void onDestroy() {

        if (taskPer != null) {
            taskPer.cancel(true);
        }
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
        super.onDestroy();
    }

    class Adapter_PersistencyGrid extends BaseAdapter {

        class ViewHolder {


            TextView textviewPersistencyEmpId, textviewEmployeeName, textviewCollected, textviewPersistency, textviewCollectable,
                    textviewUnpaidPolicyCount;
        }

        final List<ParseXML.XMLHolderPersistency> lst;
        final Context mContext;

        /*private int[] colors = new int[] { Color.parseColor("#DCDBDB"),
                Color.parseColor("#E8E8E8") };*/

        Adapter_PersistencyGrid(Context context, List<ParseXML.XMLHolderPersistency> results) {
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
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater mInflater = LayoutInflater.from(mContext);
                convertView = mInflater.inflate(R.layout.list_item_persistency_new,
                        null);
                holder = new ViewHolder();


                holder.textviewPersistencyEmpId = convertView.findViewById(R.id.textviewPersistencyEmpId);
                holder.textviewEmployeeName = convertView.findViewById(R.id.textviewEmployeeName);
                holder.textviewCollected = convertView.findViewById(R.id.textviewCollected);
                holder.textviewPersistency = convertView.findViewById(R.id.textviewPersistency);
                holder.textviewCollectable = convertView.findViewById(R.id.textviewCollectable);
                holder.textviewUnpaidPolicyCount = convertView.findViewById(R.id.textviewUnpaidPolicyCount);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
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

            /*int colorPos = position % colors.length;
            convertView.setBackgroundColor(colors[colorPos]);*/

            return convertView;
        }
    }


}


