package sbilife.com.pointofsale_bancaagency.reports.usersreports;

import android.annotation.SuppressLint;
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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.Arrays;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.Utility;
import sbilife.com.pointofsale_bancaagency.home.ScoreCardActivity;
import sbilife.com.pointofsale_bancaagency.home.npsscore.NPSScoreActivity;
import sbilife.com.pointofsale_bancaagency.reports.BancaReportsCIFListActivity;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;
import sbilife.com.pointofsale_bancaagency.reports.newbusiness.SendMHRLinkAOLActivity;
import sbilife.com.pointofsale_bancaagency.reports.newbusiness.SendSMSAlternateProcessActivity;
import sbilife.com.pointofsale_bancaagency.reports.policyservicing.SendMandateLinkActivity;
import sbilife.com.pointofsale_bancaagency.reports.underwriting.SendLinkActivity;

/**
 * Created by O0110 on 30/12/2017.
 */

public class ReportsSAMDSMListActivity extends AppCompatActivity implements
        View.OnClickListener, ServiceHits.DownLoadData {


    private final String METHOD_NAME_AM_BDM_LIST = "getBaReRepEmpList";
    private final String URl = ServiceURL.SERVICE_URL;

    private DownloadFileAsyncChannelUserReports taskDownloadFileAsyncChannelUserReports;
    private ProgressDialog mProgressDialog;

    private CommonMethods mCommonMethods;
    private Context context;

    private ArrayList<ParseXML.ChannelUserReportsValuesModel> lstBDMList;

    private String strCIFBDMUserId = "";
    private String strCIFBDMEmailId = "";
    private String strCIFBDMPassword = "";
    private String strCIFBDMMObileNo = "";
    private String strUserType = "";
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

        strUserType = mCommonMethods.GetUserType(context);
        array_sort = new ArrayList<>();
        taskDownloadFileAsyncChannelUserReports = new DownloadFileAsyncChannelUserReports();

        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");

        if (fromHome.equalsIgnoreCase("Y")) {
            strUserType = mCommonMethods.GetUserType(context);
            getUserDetails();
        } else {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");
            strUserType = intent.getStringExtra("strUserType");

            try {
                strCIFBDMPassword = SimpleCrypto.encrypt("SBIL", "sbil");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (strUserType.equalsIgnoreCase("ZAM")) {
            mCommonMethods.setApplicationToolbarMenu(this, "AM/SAM/BDM List");
        } else {
            mCommonMethods.setApplicationToolbarMenu(this, "BSM List");
        }

        tableLayoutSearchChannelUserReports = findViewById(R.id.tableLayoutSearchChannelUserReports);
        listviewChannelUserReports = findViewById(R.id.listviewChannelUserReports);
        txterrordescbdm = findViewById(R.id.txterrordescbdn);
        txtbdmlistcount = findViewById(R.id.txtbdnlistcount);


        Button buttonChannelUserReportsSearchByCode = findViewById(R.id.buttonChannelUserReportsSearchByCode);
        Button buttonChannelUserReportsSearchByName = findViewById(R.id.buttonChannelUserReportsSearchByName);

        edittextChannelUserReportsSearchByCode = findViewById(R.id.edittextChannelUserReportsSearchByCode);
        edittextChannelUserReportsSearchByName = findViewById(R.id.edittextChannelUserReportsSearchByName);

        buttonChannelUserReportsSearchByCode.setOnClickListener(this);
        buttonChannelUserReportsSearchByName.setOnClickListener(this);

        mProgressDialog = new ProgressDialog(this,
                ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html
                .fromHtml("<font color='#00a1e3'><b>" + Message
                        + "<b></font>"));
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
    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = mCommonMethods
                .setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Services");

        int id = v.getId();
        if (id == R.id.listviewChannelUserReports) {

            if (strUserType.equalsIgnoreCase("ZAM")) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                String selectedListItem = array_sort.get(info.position).getPOSITIONTYPE();
                if(selectedListItem.equalsIgnoreCase("BDM")){
                    menu.add(0, v.getId(), 0, "CIF List");
                }else{
                    menu.add(0, v.getId(), 0, "BDM List");
                }
                menu.add(1, v.getId(), 0, getString(R.string.scorecardTitle));

            } else {
                menu.add(0, v.getId(), 0, "BSM List");
            }
            menu.add(2, v.getId(), 0, "NPS");
            menu.add(3, v.getId(),0, "Send SMS Alternate Process");
            menu.add(4, v.getId(),0, "Send eMandate Link");
            menu.add(5, v.getId(),0, "Send MHR Link (Agent Own Life)");
            menu.add(6, v.getId(),0, "Send Link");
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        String strCifNo;
        strCifNo = array_sort.get(info.position).getEMPLOYEEID();
        String title = item.getTitle().toString() ;

        if (!TextUtils.isEmpty(strCifNo)) {
            Intent intent = null;
            String strUserType;
            String positionType = array_sort.get(info.position).getPOSITIONTYPE();
            /*if(positionType.equalsIgnoreCase("SAM M5 L1")
                    ||positionType.equalsIgnoreCase("SAM")
                    ||positionType.equalsIgnoreCase("Sr Area Manager M5 L1")) {*/
            if(Arrays.asList(getResources().getStringArray(R.array.list_sam)).indexOf(positionType.toLowerCase().toLowerCase()) != -1) {
                strUserType= "SAM";
            }else if(positionType.equalsIgnoreCase("BDM")){
                strUserType= "BDM";
                }
                else{
                strUserType= "AM";
            }

            if (title.equalsIgnoreCase("CIF List") || title.equalsIgnoreCase("BDM List")) {
                if(positionType.equalsIgnoreCase("BDM")){
                    intent = new Intent(context,BancaReportsCIFListActivity.class);
                }
                else{
                    intent = new Intent(context,BancaReportsBDMUMListActivity.class);
                }
            }else if (title.equalsIgnoreCase("BSM List")) {
                intent = new Intent(context, ReportsAMBSMListActivity.class);
                strUserType = "DSM";
            }else if(title.equalsIgnoreCase(getString(R.string.scorecardTitle))){
                intent = new Intent(context, ScoreCardActivity.class);
            }else if (title.equalsIgnoreCase("NPS")) {
                intent = new Intent(context, NPSScoreActivity.class);
            }else if(title.equalsIgnoreCase("Send SMS Alternate Process")){
                intent = new Intent(context, SendSMSAlternateProcessActivity.class);
            }else if(title.equalsIgnoreCase("Send eMandate Link")){
                intent = new Intent(context, SendMandateLinkActivity.class);
            }else if(title.equalsIgnoreCase("Send MHR Link (Agent Own Life)")){
                intent = new Intent(context, SendMHRLinkAOLActivity.class);
            }else if(title.equalsIgnoreCase("Send Link")){
                intent = new Intent(context, SendLinkActivity.class);
            }

            if(intent != null) {
                intent.putExtra("fromHome", "N");
                intent.putExtra("strUserType", strUserType);
                intent.putExtra("strBDMCifCOde", strCifNo);
                intent.putExtra("strEmailId", strCIFBDMEmailId);//change it
                intent.putExtra("strMobileNo", strCIFBDMMObileNo);//change it
                startActivity(intent);
            }

        }

        return true;

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

    private void startDownloadPolicyList() {
        taskDownloadFileAsyncChannelUserReports = new DownloadFileAsyncChannelUserReports();
        taskDownloadFileAsyncChannelUserReports.execute("demo");
    }


    @Override
    public void downLoadData() {
        startDownloadPolicyList();
    }

    class DownloadFileAsyncChannelUserReports extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String inputpolicylist = "", strBdmListErrorCOde1 = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @SuppressLint("SimpleDateFormat")
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
                String NAMESPACE = "http://tempuri.org/";
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

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
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
                                registerForContextMenu(listviewChannelUserReports);
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

                } catch (IOException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
                } catch (XmlPullParserException e) {
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
                    listviewChannelUserReports.setAdapter(selectedAdapterBdm);
                    txterrordescbdm.setText("");

                    if (strUserType.equalsIgnoreCase("ZAM")) {
                        txtbdmlistcount.setText("Total AM/SAM/BDM : " + lstBdmListCount);
                    } else {
                        txtbdmlistcount.setText("Total DSM : " + lstBdmListCount);
                    }
                    selectedAdapterBdm = new SelectedAdapterBdm(
                            context, lstBDMList);
                    selectedAdapterBdm.setNotifyOnChange(true);
                    listviewChannelUserReports.setAdapter(selectedAdapterBdm);
                    Utility.setListViewHeightBasedOnChildren(listviewChannelUserReports);
                } else {

                    tableLayoutSearchChannelUserReports.setVisibility(View.GONE);
                    listviewChannelUserReports.setVisibility(View.VISIBLE);

                    txterrordescbdm.setText("No Record Found");

                    if (strUserType.equalsIgnoreCase("ZAM")) {
                        txtbdmlistcount.setText("Total AM/SAM/BDM : 0");
                    } else {
                        txtbdmlistcount.setText("Total DSM : 0");
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


    class SelectedAdapterBdm extends ArrayAdapter<ParseXML.ChannelUserReportsValuesModel> {

        private final List<ParseXML.ChannelUserReportsValuesModel> lst;

        SelectedAdapterBdm(Context context,
                           List<ParseXML.ChannelUserReportsValuesModel> objects) {
            super(context, 0, objects);
            lst = objects;
        }



        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) this.getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item_channel_user_list, null);
            }
            TextView textviewChannelReportsUserNameTitle = v.findViewById(R.id.textviewChannelReportsUserNameTitle);
            TextView textviewChannelReportsUserName = v.findViewById(R.id.textviewChannelReportsUserName);
            TextView textviewChannelReportsUserIdTitle = v.findViewById(R.id.textviewChannelReportsUserIdTitle);
            TextView textviewChannelReportsUserId = v.findViewById(R.id.textviewChannelReportsUserId);

            TextView textviewChannelReportsUserTypeTitle = v.findViewById(R.id.textviewChannelReportsUserTypeTitle);
            TextView textviewChannelReportsUserType = v.findViewById(R.id.textviewChannelReportsUserType);
            final TextView textviewMobileNumber = v.findViewById(R.id.textviewMobileNumber);
            ImageView imgcontact_cust_r = v.findViewById(R.id.imgcontact_cust_r);
            LinearLayout llMobileLayout = v.findViewById(R.id.llMobileLayout);
            Object obj = null;
            boolean i = lst.contains(obj);


            if (!i) {
                if (strUserType.equalsIgnoreCase("ZAM")) {

                    /*if(lst.get(position).getPOSITIONTYPE().equalsIgnoreCase("SAM M5 L1")
                            ||lst.get(position).getPOSITIONTYPE().equalsIgnoreCase("SAM")
                            ||lst.get(position).getPOSITIONTYPE().equalsIgnoreCase("Sr Area Manager M5 L1")) {*/
                    if(Arrays.asList(getResources().getStringArray(R.array.list_sam)).indexOf(lst.get(position).getPOSITIONTYPE().toLowerCase()) != -1) {
                        textviewChannelReportsUserIdTitle.setText("SAM Code: ");
                        textviewChannelReportsUserNameTitle.setText("SAM Name: ");

                    }/*else if(lst.get(position).getPOSITIONTYPE().equalsIgnoreCase("AM")
                            ||lst.get(position).getPOSITIONTYPE().equalsIgnoreCase("Area Manager")){*/
                    else if(Arrays.asList(getResources().getStringArray(R.array.list_am)).indexOf(lst.get(position).getPOSITIONTYPE().toLowerCase()) != -1) {
                        textviewChannelReportsUserIdTitle.setText("AM Code: ");
                        textviewChannelReportsUserNameTitle.setText("AM Name: ");
                    }else {
                        textviewChannelReportsUserIdTitle.setText("BDM Code: ");
                        textviewChannelReportsUserNameTitle.setText("BDM Name: ");
                    }

                } else {
                    textviewChannelReportsUserIdTitle.setText("DSM Code: ");
                    textviewChannelReportsUserNameTitle.setText("DSM Name: ");
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
                textviewMobileNumber.setText(lst.get(position).getCONTACTMOBILE());
                String mobileNumber = textviewMobileNumber.getText().toString();
                if(TextUtils.isEmpty(mobileNumber)){
                    llMobileLayout.setVisibility(View.GONE);
                }else{
                    llMobileLayout.setVisibility(View.VISIBLE);
                }
                imgcontact_cust_r.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mobileNumber = textviewMobileNumber.getText().toString();

                        if (!TextUtils.isEmpty(mobileNumber)) {
                            mCommonMethods.callMobileNumber(mobileNumber, context);
                        }
                    }
                });
                textviewMobileNumber.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mobileNumber = textviewMobileNumber.getText().toString();
                        if(!TextUtils.isEmpty(mobileNumber)) {
                            mCommonMethods.callMobileNumber(mobileNumber, context);
                        }
                    }
                });
            }
            return (v);
        }
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

                if(s.length()==0){
                    refreOriginalList();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void refreOriginalList() {

        if(lstBDMList!=null && lstBDMList.size()>0){
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
                if(s.length()==0){
                    refreOriginalList();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

    private void service_hits(String strServiceName) {

        String strbdmstatusstring = "Active";
        ServiceHits service = new ServiceHits(context,
                strServiceName, strbdmstatusstring, strCIFBDMUserId,
                strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();
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
}


