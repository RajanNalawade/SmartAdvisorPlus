package sbilife.com.pointofsale_bancaagency.agent_on_boarding;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
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
import sbilife.com.pointofsale_bancaagency.Utility;
import sbilife.com.pointofsale_bancaagency.agent_on_boarding.dashboard.ActivityAOBAgentDetails;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.home.CarouselHomeActivity;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

/**
 * Created by O0110 on 14/05/2018.
 */

public class ActivityAOBUMListUnderBSM extends AppCompatActivity implements View.OnClickListener, ServiceHits.DownLoadData {

    private final String NAMESPACE = "http://tempuri.org/";

    private final String METHOD_NAME_AM_BDM_LIST = "getBaReRepEmpList";

    private DownloadFileAsyncChannelUserReports taskDownloadFileAsyncChannelUserReports;
    private ProgressDialog mProgressDialog;

    private CommonMethods mCommonMethods;
    private Context context;

    private ArrayList<ParseXML.ChannelUserReportsValuesModel> lstBDMList;

    private String strCIFBDMUserId = "";
    private String strCIFBDMEmailId = "";
    private String strCIFBDMPassword = "";
    private String strCIFBDMMObileNo = "";
    private String strUserType = "", isDashboard = "";
    private int lstBdmListCount = 0;

    private SelectedAdapterBdm selectedAdapterBdm;

    private ListView listviewChannelUserReports;

    private TableLayout tableLayoutSearchChannelUserReports;
    private TextView txterrordescbdm, txtbdmlistcount;
    private EditText edittextChannelUserReportsSearchByCode, edittextChannelUserReportsSearchByName;

    private ArrayList<ParseXML.ChannelUserReportsValuesModel> array_sort;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.banca_reports_channel_user_list);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        context = this;
        mCommonMethods = new CommonMethods();
        mProgressDialog = new ProgressDialog(context);

        isDashboard = getIntent().getStringExtra("fromHome");
        isDashboard = isDashboard == null ? "" : isDashboard;

        getUserDetails();

        if (strUserType.equalsIgnoreCase("AM") || strUserType.equalsIgnoreCase("SAM")) {
            mCommonMethods.setApplicationToolbarMenu(this, "BDM List");
        } else {
            mCommonMethods.setApplicationToolbarMenu(this, "UM List");
        }

        array_sort = new ArrayList<>();
        taskDownloadFileAsyncChannelUserReports = new DownloadFileAsyncChannelUserReports();
        listviewChannelUserReports = (ListView) findViewById(R.id.listviewChannelUserReports);
        tableLayoutSearchChannelUserReports = (TableLayout) findViewById(R.id.tableLayoutSearchChannelUserReports);
        txterrordescbdm = (TextView) findViewById(R.id.txterrordescbdn);
        txtbdmlistcount = (TextView) findViewById(R.id.txtbdnlistcount);

        Button buttonChannelUserReportsSearchByCode = (Button) findViewById(R.id.buttonChannelUserReportsSearchByCode);
        Button buttonChannelUserReportsSearchByName = (Button) findViewById(R.id.buttonChannelUserReportsSearchByName);

        edittextChannelUserReportsSearchByCode = (EditText) findViewById(R.id.edittextChannelUserReportsSearchByCode);
        edittextChannelUserReportsSearchByName = (EditText) findViewById(R.id.edittextChannelUserReportsSearchByName);

        buttonChannelUserReportsSearchByCode.setOnClickListener(this);
        buttonChannelUserReportsSearchByName.setOnClickListener(this);

        mProgressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        mProgressDialog.setButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (taskDownloadFileAsyncChannelUserReports != null) {
                            taskDownloadFileAsyncChannelUserReports.cancel(true);
                        }
                        if (mProgressDialog != null) {
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }
                    }
                });

        mProgressDialog.setMax(100);

        getUserReportsList();

        listviewChannelUserReports.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String strCifNo = array_sort.get(position).getEMPLOYEEID()
                        == null ? "" : array_sort.get(position).getEMPLOYEEID();

                if (!strCifNo.equals("")) {

                    if (isDashboard.equals("Y")) {

                        //mCommonMethods.callActivity(context, ActivityAOBAgentDetails.class);
                        Intent intent = new Intent(context, ActivityAOBAgentDetails.class);
                        intent.putExtra("UMCode", strCifNo);
                        startActivity(intent);

                    } else {
                        Intent intent = new Intent(context, ActivityAOBPANPendingAgentList.class);
                        intent.putExtra("UMCode", strCifNo);
                        startActivity(intent);

                    }
                }

                return true;
            }
        });
    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = mCommonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

    }

    private void service_hits(String strServiceName) {
        ServiceHits service = new ServiceHits(context, strServiceName, "",
                strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();
    }

    private void startDownloadBdmList() {
        taskDownloadFileAsyncChannelUserReports.execute("demo");
    }

    @Override
    public void downLoadData() {
        taskDownloadFileAsyncChannelUserReports = new DownloadFileAsyncChannelUserReports();
        startDownloadBdmList();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (taskDownloadFileAsyncChannelUserReports != null) {
                taskDownloadFileAsyncChannelUserReports.cancel(true);
            }
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
            finish();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.buttonChannelUserReportsSearchByCode:
                searchByCIFCode();
                break;
            case R.id.buttonChannelUserReportsSearchByName:
                searchByFirstName();
                break;
        }

    }

    private void getUserReportsList() {

        taskDownloadFileAsyncChannelUserReports = new DownloadFileAsyncChannelUserReports();
        array_sort = new ArrayList<>();
        array_sort.clear();
        if (mCommonMethods.isNetworkConnected(context)) {
            service_hits(METHOD_NAME_AM_BDM_LIST);
        } else {
            mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
        }

    }

    private void searchByCIFCode() {

        if (edittextChannelUserReportsSearchByCode.getText().toString()
                .equalsIgnoreCase("")) {
            mCommonMethods.showMessageDialog(context, "Please Enter Code...");
        } else {
            mCommonMethods.hideKeyboard(edittextChannelUserReportsSearchByCode, context);

            array_sort = new ArrayList<>();

            array_sort.clear();
            String str = edittextChannelUserReportsSearchByCode.getText().toString().trim().toLowerCase();

            for (ParseXML.ChannelUserReportsValuesModel node : lstBDMList) {
                String channelCode = node.getEMPLOYEEID().trim().toLowerCase();
                if (channelCode.contains(str) || channelCode.startsWith(str)) {
                    array_sort.add(node);
                }
            }

            selectedAdapterBdm = new SelectedAdapterBdm(context,
                    array_sort);
            selectedAdapterBdm.setNotifyOnChange(true);
            listviewChannelUserReports.setAdapter(selectedAdapterBdm);
            Utility.setListViewHeightBasedOnChildren(listviewChannelUserReports);
        }

        edittextChannelUserReportsSearchByCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0) {
                    refreOriginalList();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void refreOriginalList() {

        if (lstBDMList != null && lstBDMList.size() > 0) {
            array_sort = new ArrayList<>(lstBDMList);
            selectedAdapterBdm = new SelectedAdapterBdm(context,
                    array_sort);
            selectedAdapterBdm.setNotifyOnChange(true);
            listviewChannelUserReports.setAdapter(selectedAdapterBdm);
            Utility.setListViewHeightBasedOnChildren(listviewChannelUserReports);
        }

    }

    private void searchByFirstName() {

        if (edittextChannelUserReportsSearchByName.getText().toString()
                .equalsIgnoreCase("")) {
            //fnalert();
            mCommonMethods.showMessageDialog(context, mCommonMethods.FIRST_NAME_ALERT);
        } else {
            mCommonMethods.hideKeyboard(edittextChannelUserReportsSearchByName, context);

            array_sort = new ArrayList<>();

            array_sort.clear();
            String str = edittextChannelUserReportsSearchByName.getText().toString().trim().toLowerCase();

            for (ParseXML.ChannelUserReportsValuesModel node : lstBDMList) {
                String fName = node.getEMPLOYEENAME().trim().toLowerCase();
                if (fName.contains(str) || fName.startsWith(str)) {
                    array_sort.add(node);
                }
            }

            selectedAdapterBdm = new SelectedAdapterBdm(context,
                    array_sort);
            selectedAdapterBdm.setNotifyOnChange(true);
            listviewChannelUserReports.setAdapter(selectedAdapterBdm);
            Utility.setListViewHeightBasedOnChildren(listviewChannelUserReports);
        }
        edittextChannelUserReportsSearchByName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    refreOriginalList();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (taskDownloadFileAsyncChannelUserReports != null) {
            taskDownloadFileAsyncChannelUserReports.cancel(true);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActivityAOBUMListUnderBSM.this, CarouselHomeActivity.class));
    }

    class DownloadFileAsyncChannelUserReports extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String inputpolicylist = "", strBdmListErrorCOde1 = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                String soap_action;

                running = true;
                SoapObject request;

                // getBaReRepEmpList(string strRepId, string strEmailId, string strMobileNo, string strAuthKey)
                /*http://tempuri.org/"><CIFPolicyList> <Table>
                <EMPLOYEEID>24307</EMPLOYEEID>
                <EMPLOYEENAME>ASHISH K SWARNKAR</EMPLOYEENAME>
                <POSITIONTYPE>Unit Manager</POSITIONTYPE>
                 </Table> <Table> <EMPLOYEEID>22197</EMPLOYEEID>
                 <EMPLOYEENAME>TARUN KUMAR PATEL</EMPLOYEENAME>
                 <POSITIONTYPE>Unit Manager</POSITIONTYPE> </Table> */
                soap_action = "http://tempuri.org/getBaReRepEmpList";
                request = new SoapObject(NAMESPACE,
                        METHOD_NAME_AM_BDM_LIST);
                request.addProperty("strRepId", strCIFBDMUserId);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());


                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                try {
                    androidHttpTranport
                            .call(soap_action, envelope);
                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("anyType{}")) {

                        SoapPrimitive sa;
                        try {
                            sa = (SoapPrimitive) envelope.getResponse();

                            inputpolicylist = sa.toString();

                            ParseXML prsObj = new ParseXML();

                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "CIFPolicyList");
                            inputpolicylist = new ParseXML().parseXmlTag(
                                    inputpolicylist, "ScreenData");
                            strBdmListErrorCOde1 = inputpolicylist;

                            if (strBdmListErrorCOde1 == null) {
                                inputpolicylist = sa.toString();
                                inputpolicylist = prsObj.parseXmlTag(
                                        inputpolicylist, "CIFPolicyList");

                                List<String> Node = prsObj.parseParentNode(
                                        inputpolicylist, "Table");

                                List<ParseXML.ChannelUserReportsValuesModel> nodeData = prsObj
                                        .parseNodeChannelUserReports(Node);

                                // final List<XMLHolderBdm> lst;
                                lstBDMList = new ArrayList<>();
                                lstBDMList.clear();

                                lstBDMList = new ArrayList<>(nodeData);

                                lstBdmListCount = lstBDMList.size();
                                array_sort = new ArrayList<>(lstBDMList);

                                selectedAdapterBdm = new SelectedAdapterBdm(
                                        context, lstBDMList);
                                selectedAdapterBdm.setNotifyOnChange(true);
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
                    }

                } catch (IOException | XmlPullParserException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
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
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (running) {
                txterrordescbdm.setVisibility(View.VISIBLE);
                txtbdmlistcount.setVisibility(View.VISIBLE);
                listviewChannelUserReports.setVisibility(View.VISIBLE);
                if (strBdmListErrorCOde1 == null) {
                    tableLayoutSearchChannelUserReports.setVisibility(View.VISIBLE);

                    txterrordescbdm.setText("");

                    if (strUserType.equalsIgnoreCase("AM") || strUserType.equalsIgnoreCase("SAM")) {
                        txtbdmlistcount.setText("Total BDM : " + lstBdmListCount);
                    } else {
                        txtbdmlistcount.setText("Total UM : " + lstBdmListCount);
                    }

                    listviewChannelUserReports.setAdapter(selectedAdapterBdm);

                    Utility.setListViewHeightBasedOnChildren(listviewChannelUserReports);

                } else {
                    tableLayoutSearchChannelUserReports.setVisibility(View.GONE);
                    listviewChannelUserReports.setVisibility(View.VISIBLE);

                    txterrordescbdm.setText("No Record Found");
                    if (strUserType.equalsIgnoreCase("AM") || strUserType.equalsIgnoreCase("SAM")) {
                        txtbdmlistcount.setText("Total BDM : 0");
                    } else {
                        txtbdmlistcount.setText("Total UM : 0");
                    }
                    List<ParseXML.ChannelUserReportsValuesModel> lst;
                    ParseXML.ChannelUserReportsValuesModel node = null;
                    lst = new ArrayList<>();
                    lst.clear();
                    lst.add(node);
                    selectedAdapterBdm = new SelectedAdapterBdm(context, lst);
                    selectedAdapterBdm.setNotifyOnChange(true);
                    listviewChannelUserReports.setAdapter(selectedAdapterBdm);

                    Utility.setListViewHeightBasedOnChildren(listviewChannelUserReports);
                }
            } else {
                //servererror();
                mCommonMethods.showMessageDialog(context, mCommonMethods.SERVER_ERROR);
            }
        }
    }

    public class SelectedAdapterBdm extends ArrayAdapter<ParseXML.ChannelUserReportsValuesModel> {

        // used to keep selected position in ListView

        final List<ParseXML.ChannelUserReportsValuesModel> lst;

        public SelectedAdapterBdm(Context context,
                                  List<ParseXML.ChannelUserReportsValuesModel> objects) {
            super(context, 0, objects);
            lst = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) this.getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item_channel_user_list, null);
            }

            TextView textviewChannelReportsUserNameTitle = (TextView) v.findViewById(R.id.textviewChannelReportsUserNameTitle);
            TextView textviewChannelReportsUserName = (TextView) v.findViewById(R.id.textviewChannelReportsUserName);
            TextView textviewChannelReportsUserIdTitle = (TextView) v.findViewById(R.id.textviewChannelReportsUserIdTitle);
            TextView textviewChannelReportsUserId = (TextView) v.findViewById(R.id.textviewChannelReportsUserId);

            TextView textviewChannelReportsUserTypeTitle = (TextView) v.findViewById(R.id.textviewChannelReportsUserTypeTitle);
            TextView textviewChannelReportsUserType = (TextView) v.findViewById(R.id.textviewChannelReportsUserType);

            Object obj = null;
            boolean i = lst.contains(obj);

            if (!i) {

                if (strUserType.equalsIgnoreCase("AM") || strUserType.equalsIgnoreCase("SAM")) {

                    if (lst.get(position).getPOSITIONTYPE().equalsIgnoreCase("SAM M5 L1")
                            || lst.get(position).getPOSITIONTYPE().equalsIgnoreCase("SAM")
                            || lst.get(position).getPOSITIONTYPE().equalsIgnoreCase("Sr Area Manager M5 L1")) {
                        textviewChannelReportsUserIdTitle.setText("SAM Code: ");
                        textviewChannelReportsUserNameTitle.setText("SAM Name: ");

                    } else if (lst.get(position).getPOSITIONTYPE().equalsIgnoreCase("AM")
                            || lst.get(position).getPOSITIONTYPE().equalsIgnoreCase("Area Manager")) {
                        textviewChannelReportsUserIdTitle.setText("AM Code: ");
                        textviewChannelReportsUserNameTitle.setText("AM Name: ");
                    } else {
                        textviewChannelReportsUserIdTitle.setText("BDM Code: ");
                        textviewChannelReportsUserNameTitle.setText("BDM Name: ");
                    }
                } else if (strUserType.equalsIgnoreCase("UM")) {
                    textviewChannelReportsUserIdTitle.setText("UM Code: ");
                    textviewChannelReportsUserNameTitle.setText("UM Name: ");
                } else if (strUserType.equalsIgnoreCase("BSM")) {
                    textviewChannelReportsUserIdTitle.setText("BSM Code: ");
                    textviewChannelReportsUserNameTitle.setText("BSM Name: ");
                }

                String name = lst.get(position).getEMPLOYEENAME() == null ? ""
                        : lst.get(position).getEMPLOYEENAME();
                String userType = lst.get(position).getPOSITIONTYPE() == null ? "" : lst
                        .get(position).getPOSITIONTYPE();
                String userId = lst.get(position).getEMPLOYEEID() == null ? "" : lst
                        .get(position).getEMPLOYEEID();

                textviewChannelReportsUserName.setText(name);
                textviewChannelReportsUserId.setText(userId);
                textviewChannelReportsUserType.setText(userType);

            }

            return (v);
        }
    }
}