package sbilife.com.pointofsale_bancaagency.reports.commonreports;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.branchlocator.GPSTracker;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class RenewalPolicyListGeoLocationActivity extends AppCompatActivity implements View.OnClickListener, ServiceHits.DownLoadData {
    private final String URl = ServiceURL.SERVICE_URL;
    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME_BRANCH_CODE = "getBranchListbanca_smrt";
    private final String METHOD_NAME_BRANCHWISE_RENEWAL_LIST= "getAgentPoliciesRenewalListMonthwise_gio";

    private Spinner spnRewmonths;

    private Button buttonOk;
    private EditText edittextSearch;


    private CommonMethods commonMethods;
    private Context context;

    private int mYear, mMonth, mDay, datecheck = 0;

    private ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> globalDataList;


    private ProgressDialog mProgressDialog;
    private LinearLayout llBranchCodeList;

    private ServiceHits service;
    private ParseXML objParse;
    private GPSTracker gpsTracker;


    private DownloadBranchWiseRenewalListAsync downloadBranchWiseRenewalListAsync;
    private ArrayList<ParseXML.BranchDetailsForBDMValuesModel> branchDetailsList;
    private String branchName = "",branchCode = "",strCIFBDMUserId = "", strCIFBDMEmailId = "",
            strCIFBDMPassword = "", strCIFBDMMObileNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_branchwise_renewal_list);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Policies Geo Location");
        objParse = new ParseXML();
        gpsTracker = new GPSTracker(this);

        getUserDetails();


        spnRewmonths = findViewById(R.id.spnRewmonths);

        Button buttonOk = findViewById(R.id.buttonOk);


        llBranchCodeList = findViewById(R.id.llBranchCodeList);

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        // call the constructor of CustomAdapter to send the reference and data to Adapter
        globalDataList = new ArrayList<>();

        buttonOk.setOnClickListener(this);

    }
    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.buttonOk:
                    System.out.println("strCIFBDMUserId = "+strCIFBDMUserId);
                    System.out.println("strCIFBDMEmailId = "+strCIFBDMEmailId);
                    System.out.println("strCIFBDMMObileNo = "+strCIFBDMMObileNo);
                    StringBuilder input = new StringBuilder();
                    input.append(strCIFBDMUserId + "," + strCIFBDMEmailId+","+strCIFBDMMObileNo);

                    input.append(",").append(spnRewmonths.getSelectedItem().toString());
                    service_hits(input.toString());
                break;
        }
    }

    private void service_hits(String input) {
        LocationManager locationmanager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);

        if (locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            service = new ServiceHits(context, METHOD_NAME_BRANCHWISE_RENEWAL_LIST, input,
                    strCIFBDMUserId, strCIFBDMEmailId,
                    strCIFBDMMObileNo, strCIFBDMPassword, this);
            service.execute();
        } else {
            commonMethods.showGPSDisabledAlertToUser(context);
        }
    }

    @Override
    public void downLoadData() {

            downloadBranchWiseRenewalListAsync = new DownloadBranchWiseRenewalListAsync();
            downloadBranchWiseRenewalListAsync.execute();

    }


    class DownloadBranchWiseRenewalListAsync extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String error = "",fromDate = "",toDate = "";
        String output;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading Please wait...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);
            mProgressDialog.show();

            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

            String spnRewmonths_text = spnRewmonths.getSelectedItem().toString();

            SimpleDateFormat format1 = new SimpleDateFormat("MM-dd-yyyy");

            if (spnRewmonths_text.contentEquals("Previous Month")) {
                Calendar aCalendar = Calendar.getInstance();
                aCalendar.set(Calendar.DATE, 1);
                aCalendar.add(Calendar.DAY_OF_MONTH, -1);
                Date lastDateOfPreviousMonth = aCalendar.getTime();
                toDate = format1.format(lastDateOfPreviousMonth);

                aCalendar.set(Calendar.DATE, 1);
                aCalendar.add(Calendar.MONTH, -5);
                Date firstDateOfPreviousMonth = aCalendar.getTime();
                fromDate = format1.format(firstDateOfPreviousMonth);
            } else if (spnRewmonths_text.contentEquals("Current Month")) {
                Calendar cale = Calendar.getInstance();
                cale.set(Calendar.DATE,
                        cale.getActualMaximum(Calendar.DATE));
                Date lastDateOfCurrentMonth = cale.getTime();
                toDate = format1.format(lastDateOfCurrentMonth);
                cale.set(Calendar.DATE,
                        cale.getActualMinimum(Calendar.DATE));
                Date firstDateOfCurrentMonth = cale.getTime();
                fromDate = format1.format(firstDateOfCurrentMonth);
            } else if (spnRewmonths_text.contentEquals("Next Month")) {
                Calendar calen = Calendar.getInstance();
                // calen.set(Calendar.MONTH, calen.get(Calendar.MONTH));
                calen.add(Calendar.MONTH, +1);
                calen.set(Calendar.DATE, calen.getActualMaximum(Calendar.DATE));
                Date lastDateOfNextMonth = calen.getTime();
                toDate = format1.format(lastDateOfNextMonth);

                calen.set(Calendar.DATE, calen.getActualMinimum(Calendar.DATE));

                Date firstDateOfNextMonth = calen.getTime();
                fromDate = format1.format(firstDateOfNextMonth);
            }
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;

                /*getbancaRenewalListBranchwise(string strBrcode, string strFromReqDate, string strToReqDate,
                string strEmailId, string strMobileNo, string strAuthKey) */


                request = new SoapObject(NAMESPACE, METHOD_NAME_BRANCHWISE_RENEWAL_LIST);
                request.addProperty("strAgentNo", strCIFBDMUserId);
                request.addProperty("strFromReqDate", fromDate);
                request.addProperty("strToReqDate", toDate);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);

                request.addProperty("strAuthKey", "QzhCNDc0OTU4NzZDQjI3RTQ4OEMyNEQ3MUZCQjE2QTY=");

                commonMethods.TLSv12Enable();

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();


                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION= "http://tempuri.org/"+METHOD_NAME_BRANCHWISE_RENEWAL_LIST;
                androidHttpTranport.call(SOAP_ACTION, envelope);
                System.out.println("envelope.getResponse() = "+envelope.getResponse());
                SoapPrimitive response = (SoapPrimitive)envelope.getResponse();

                if (!response.toString().contentEquals("<CIFPolicyList />")) {

                    ParseXML prsObj = new ParseXML();
                    String inputpolicylist = response.toString();
                    System.out.println("inputpolicylist = "+inputpolicylist);
                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "CIFPolicyList");

                    error = prsObj.parseXmlTag(inputpolicylist, "ScreenData");

                    if (error == null) {
                        // for agent policy list
//                        List<String> Node = prsObj.parseParentNode(inputpolicylist, "Table");
//                        List<ParseXML.AgentPoliciesRenewalListMonthwiseGio> nodeData = prsObj
//                                .parseNodeBranchwiseRenewalListGio(Node);
//                        globalDataList.clear();
//                        globalDataList.addAll(nodeData);

                        output = inputpolicylist.toString();
                        System.out.println("output = "+output.toString());
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
                    AsynGetLatLang objAsynLoadMap = new AsynGetLatLang(
                            RenewalPolicyListGeoLocationActivity.this, output);
                    objAsynLoadMap.execute();
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

    class AsynGetLatLang extends AsyncTask<String, Void, String> {
        final Context mContext;
        ProgressDialog progressDialog = null;
        double Lati, Longi;
        ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> AgentPoliciesRenewalListMonthwiseGioList;
        int size;
        final String res;

        AsynGetLatLang(Context mContext,String result) {
            // TODO Auto-generated constructor stub

            res = result;
            System.out.println("res = "+res);
            this.mContext = mContext;

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            progressDialog = new ProgressDialog(mContext,
                    ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Please wait...";
            progressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>"
                    + Message + "<b></font>"));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Loading");
            progressDialog.setMax(100);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            if (commonMethods.isNetworkConnected(mContext)) {

                try {
                    Geocoder geocoder = new Geocoder(mContext,
                            Locale.getDefault());
                    try {
//                        ArrayList<String> strRes = objParse
//                                .parseXmlTagMultiple(res, "Table");
                        List<String> strRes = objParse.parseParentNode(res, "Table");
                        AgentPoliciesRenewalListMonthwiseGioList = objParse.parseNodeBranchwiseRenewalListGio(strRes);

                        size = AgentPoliciesRenewalListMonthwiseGioList.size();
                        for (int i = 0; i < AgentPoliciesRenewalListMonthwiseGioList.size(); i++) {

                            if (AgentPoliciesRenewalListMonthwiseGioList.get(i).getPERMANENTPOSTCODE() != null) {
                                @SuppressWarnings("rawtypes")
//                                List addressList = geocoder
//                                        .getFromLocationName(PERMANENTPOSTCODE, 1);
//                                if (addressList != null
//                                        && addressList.size() > 0) {
//                                    Address address = (Address) addressList
//                                            .get(0);
//                                    Lati = address.getLatitude() + "";
//                                    Longi = address.getLongitude() + "";
//
//                                }
                                String locationAddress = AgentPoliciesRenewalListMonthwiseGioList.get(i).getPERMANENTADDRESS1()
                                                        +" "+AgentPoliciesRenewalListMonthwiseGioList.get(i).getPERMANENTADDRESS2()
                                                        + " "+AgentPoliciesRenewalListMonthwiseGioList.get(i).getPERMANENTADDRESS3()
                                                        +" "+AgentPoliciesRenewalListMonthwiseGioList.get(i).getPERMANENTCITY()
                                                        +" "+AgentPoliciesRenewalListMonthwiseGioList.get(i).getPERMANENTSTATE()
                                                        +" "+AgentPoliciesRenewalListMonthwiseGioList.get(i).getPERMANENTPOSTCODE();
                                List addressList = geocoder.getFromLocationName(locationAddress, 1);
                                if (addressList != null && addressList.size() > 0) {
                                    Address address = (Address) addressList.get(0);
//                                    StringBuilder sb = new StringBuilder();
//                                    sb.append(address.getLatitude()).append("\n");
//                                    sb.append(address.getLongitude()).append("\n");
//                                    result = sb.toString();
                                    Lati = address.getLatitude() ;
                                    Longi = address.getLongitude() ;
                                    AgentPoliciesRenewalListMonthwiseGioList.get(i).setPOLICYLATITUDE(Lati);
                                    AgentPoliciesRenewalListMonthwiseGioList.get(i).setPOLICYLONGITUDE(Longi);
                                    System.out.println("Lati = "+AgentPoliciesRenewalListMonthwiseGioList.get(i).getPOLICYLATITUDE()+" = Longi "+
                                            AgentPoliciesRenewalListMonthwiseGioList.get(i).getPOLICYLONGITUDE());
                                }



                            }
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return "Server not Found. Please try after some time.";
                }

            } else
                return "Please Activate Internet connection";

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

                Intent intent = new Intent(RenewalPolicyListGeoLocationActivity.this,
                        PolicyHolderLocatorMapFragmentActivity.class);
                intent.putExtra("retVal", AgentPoliciesRenewalListMonthwiseGioList);
                intent.putExtra("size", size);

                startActivity(intent);

            } catch (Exception e) {
                e.getMessage();
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


        if (downloadBranchWiseRenewalListAsync != null) {
            downloadBranchWiseRenewalListAsync.cancel(true);
        }
        if (service != null) {
            service.cancel(true);
        }
    }

    private void clearList() {



        if (globalDataList != null) {
            globalDataList.clear();
        }
    }

    /*private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                RenewalPolicyListGeoLocationActivity.this, AlertDialog.THEME_HOLO_LIGHT);


        alertDialogBuilder
                .setMessage(
                        Html
                                .fromHtml("<font color='#00a1e3'>GPS is disabled in your device. Would you like to enable it?</font>"))
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);

                            }
                        });

        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }// ;*/
}
