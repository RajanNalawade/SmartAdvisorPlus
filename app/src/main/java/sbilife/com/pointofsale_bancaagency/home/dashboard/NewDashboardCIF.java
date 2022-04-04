package sbilife.com.pointofsale_bancaagency.home.dashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.KeyEvent;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;

@SuppressWarnings("deprecation")
public class NewDashboardCIF extends AppCompatActivity {
    private ProgressDialog mProgressDialog;

    // Tab titles

    private final String NAMESPACE = "http://tempuri.org/";
    private final String URl = ServiceURL.SERVICE_URL;
    private final String METHOD_NAME_BDMCIF_DASH = "getBDMCIFDashBoard";
    private DownloadFileAsyncBDMCIF taskBDMCIF;

    private String strPolicyListErrorCOde;


    private String strCIFCode = "";
    private String strCIFBDMEmailId = "";
    private String strCIFBDMPassword = "";
    private String strCIFBDMMObileNo = "";

    private String renewedpolicy_trad = "0";
    private String newpolicy_trad = "0";
    private String renewalpremium_trad = "0";
    private String newbusinesspremium_trad = "0";

    private String renewedpolicy_ulip = "0";
    private String newpolicy_ulip = "0";
    private String renewalpremium_ulip = "0";
    private String newbusinesspremium_ulip = "0";
    private ViewPager viewPager;

    private CommonMethods commonMethods;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_newdashboardcif);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Dashboard");
        mProgressDialog = new ProgressDialog(context,
                ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading. Please wait...";
        mProgressDialog.setMessage(Html
                .fromHtml("<font color='#00a1e3'><b>" + Message
                        + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);


        DatabaseHelper dbhelper = new DatabaseHelper(this);

        viewPager = findViewById(R.id.pager);

        // Set the ViewPagerAdapter into ViewPager

        ViewFlipper mFlipper = findViewById(R.id.flipperNews);
        mFlipper.startFlipping();

        mFlipper.setInAnimation(AnimationUtils.loadAnimation(context,
                R.anim.slideintext));

        mFlipper.setOutAnimation(AnimationUtils.loadAnimation(context,
                R.anim.slideouttext));

        PagerTabStrip pagerTabStrip = findViewById(R.id.pager_title_strip);
        pagerTabStrip.setDrawFullUnderline(true);
        pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.Cyan));


        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");

        if (fromHome != null && fromHome.equalsIgnoreCase("N")) {
            strCIFCode = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");

            try {
                /*strCIFBDMPassword = SimpleCrypto.encrypt("SBIL", "sbil");*/
                strCIFBDMPassword = commonMethods.getStrAuth();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                strCIFCode = SimpleCrypto.decrypt("SBIL", dbhelper.GetCIFNo());
                strCIFBDMEmailId = SimpleCrypto.decrypt("SBIL",
                        dbhelper.GetEmailId());
                strCIFBDMPassword = dbhelper.GetPassword();
                strCIFBDMMObileNo = SimpleCrypto.decrypt("SBIL",
                        dbhelper.GetMobileNo());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String METHOD_NAME_SH = "saveSmartAdvisorServiceHits";
        String SOAP_ACTION_SH = "http://tempuri.org/saveSmartAdvisorServiceHits";
        ServiceHits service = new ServiceHits(SOAP_ACTION_SH, METHOD_NAME_SH);
        service.execute();

    }

    class DownloadFileAsyncBDMCIF extends AsyncTask<String, String, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                mProgressDialog = new ProgressDialog(context,
                        ProgressDialog.THEME_HOLO_LIGHT);
                String Message = "Loading. Please wait...";
                mProgressDialog.setMessage(Html
                        .fromHtml("<font color='#00a1e3'><b>" + Message
                                + "<b></font>"));
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(true);

                mProgressDialog.setButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (taskBDMCIF != null) {
                                    taskBDMCIF.cancel(true);
                                }
                                if (mProgressDialog.isShowing())
                                    mProgressDialog.dismiss();
                            }
                        });

                mProgressDialog.setMax(100);
                mProgressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... aurl) {
            if (commonMethods.isNetworkConnected(context)) {
                try {

                    running = true;

                    SoapObject request = new SoapObject(NAMESPACE,
                            METHOD_NAME_BDMCIF_DASH);
                    request.addProperty("strBdmCiFNo", strCIFCode);
                    request.addProperty("strUserType", "CIF");
                    request.addProperty("strEmailId", strCIFBDMEmailId);
                    request.addProperty("strMobileNo", strCIFBDMMObileNo);
                    request.addProperty("strAuthKey", strCIFBDMPassword.trim());

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);
                    envelope.dotNet = true;

                    envelope.setOutputSoapObject(request);

                    // allowAllSSL();

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    HttpTransportSE androidHttpTranport = new HttpTransportSE(
                            URl);
                    try {
                        String SOAP_ACTION_BDMCIF_DASH = "http://tempuri.org/getBDMCIFDashBoard";
                        androidHttpTranport.call(SOAP_ACTION_BDMCIF_DASH,
                                envelope);
                        Object response = envelope.getResponse();

                        SoapPrimitive sa = null;
                        try {
                            ParseXML prsObj = new ParseXML();

                            if (!response.toString().contentEquals("anyType{}")) {

                                sa = (SoapPrimitive) envelope.getResponse();

                                String inputpolicylist = sa.toString();

                                inputpolicylist = prsObj.parseXmlTag(
                                        inputpolicylist, "CIFPolicyList");
                                inputpolicylist = new ParseXML().parseXmlTag(
                                        inputpolicylist, "ScreenData");
                                strPolicyListErrorCOde = inputpolicylist;

                                if (strPolicyListErrorCOde == null) {
                                    inputpolicylist = sa.toString();

                                    /*
                                     * inputpolicylist =
                                     * prsObj.parseXmlTag(inputpolicylist,
                                     * "CIFPolicyList");
                                     *
                                     * inputpolicylist = new
                                     * ParseXML().parseXmlTag(inputpolicylist,
                                     * "CifCount");
                                     *
                                     * if(inputpolicylist != null) {
                                     * strPolicyCount = new
                                     * ParseXML().parseXmlTag(inputpolicylist,
                                     * "TotalCIFCount");
                                     * txttotalcifno.setText(strPolicyCount); }
                                     *
                                     * inputpolicylist = sa.toString();
                                     */

                                    inputpolicylist = prsObj.parseXmlTag(
                                            inputpolicylist, "CIFPolicyList");

                                    List<String> Node = prsObj.parseParentNode(
                                            inputpolicylist, "Table");

                                    String policytype_trad = prsObj.parseXmlTag(
                                            Node.get(0), "POLICYTYPE");
                                    renewedpolicy_trad = prsObj.parseXmlTag(
                                            Node.get(0), "RENEWEDPOLICY");
                                    newpolicy_trad = prsObj.parseXmlTag(
                                            Node.get(0), "NEWPOLICY");
                                    renewalpremium_trad = prsObj.parseXmlTag(
                                            Node.get(0), "RENEWALPREMIUM");
                                    newbusinesspremium_trad = prsObj
                                            .parseXmlTag(Node.get(0),
                                                    "NEWBUSINESSPREMIUM");
                                    if (Node.size() > 1) {
                                        String policytype_ulip = prsObj.parseXmlTag(
                                                Node.get(1), "POLICYTYPE");
                                        renewedpolicy_ulip = prsObj.parseXmlTag(
                                                Node.get(1), "RENEWEDPOLICY");
                                        newpolicy_ulip = prsObj.parseXmlTag(
                                                Node.get(1), "NEWPOLICY");
                                        renewalpremium_ulip = prsObj.parseXmlTag(
                                                Node.get(1), "RENEWALPREMIUM");
                                        newbusinesspremium_ulip = prsObj
                                                .parseXmlTag(Node.get(1),
                                                        "NEWBUSINESSPREMIUM");
                                    }

                                    double dbltot = 0;

                                    dbltot = dbltot + Double.parseDouble(newbusinesspremium_trad == null ? "0" : newbusinesspremium_trad)
                                            + Double.parseDouble(renewalpremium_trad == null ? "0" : renewalpremium_trad);
                                    String strtotal = String.valueOf(dbltot);

                                    DecimalFormat currencyFormat = new DecimalFormat(
                                            "##,##,##,###");

                                    String strtoal = currencyFormat.format(Double
                                            .parseDouble(strtotal));


                                }
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
            }
            return null;

        }


        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (running) {

                if (strPolicyListErrorCOde == null) {
                    String strType = commonMethods.GetUserType(context);
                    viewPager.setAdapter(new ViewPagerAdapter(
                            getSupportFragmentManager(), renewedpolicy_trad,
                            renewedpolicy_ulip, newpolicy_trad, newpolicy_ulip,
                            renewalpremium_trad, renewalpremium_ulip,
                            newbusinesspremium_trad, newbusinesspremium_ulip,
                            strType));
                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_RECORD_FOUND);
                }
            } else {
                commonMethods.showMessageDialog(context, commonMethods.NO_RECORD_FOUND);
            }
        }
    }

    class ServiceHits extends AsyncTask<String, Void, String> {
        String SOAP_ACTION = "";
        String METHOD_NAME = "";

        ServiceHits(String SOAP_ACTION,
                    String METHOD_NAME) {

            this.SOAP_ACTION = SOAP_ACTION;
            this.METHOD_NAME = METHOD_NAME;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... param) {


            if (commonMethods.isNetworkConnected(context)) {

                try {

                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                    request.addProperty("serviceName", METHOD_NAME_BDMCIF_DASH);

                    request.addProperty("strProdCode", "");
                    request.addProperty("serviceInput", "");
                    request.addProperty("serviceReqUserId", strCIFCode);
                    request.addProperty("strEmailId", strCIFBDMEmailId);
                    request.addProperty("strMobileNo", strCIFBDMMObileNo);
                    request.addProperty("strAuthKey", strCIFBDMPassword.trim());

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);
                    // Enable this envelope if service is written in dot net
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(
                            URl);

                    // allowAllSSL();

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    androidHttpTransport.call(SOAP_ACTION, envelope);
                    SoapPrimitive response = (SoapPrimitive) envelope
                            .getResponse();

                    String result = response.toString();

                    if (result.contains("1")) {
                        return "Success";
                    } else {
                        return "Failure";
                    }

                } catch (Exception e) {
                    return "Server not Found. Please try after some time.";
                }

            } else
                return "Please Activate Internet connection";

        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            try {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

            } catch (Exception e) {
                e.getMessage();
            }

            taskBDMCIF = new DownloadFileAsyncBDMCIF();
            taskBDMCIF.execute();
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (taskBDMCIF != null) {
                taskBDMCIF.cancel(true);
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
    protected void onDestroy() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (taskBDMCIF != null) {
            taskBDMCIF.cancel(true);
        }

        super.onDestroy();
    }
}
