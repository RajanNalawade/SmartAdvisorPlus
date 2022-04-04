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
import java.util.List;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.home.CarouselHomeActivity;

public class NewDashboardAgent extends AppCompatActivity {
    private final String URl = ServiceURL.SERVICE_URL;
    // Tab titles
    private final String METHOD_NAME_AGENT_DASH = "getAgentDashBoard";
    private ProgressDialog mProgressDialog;
    private DownloadFileAsyncAGENT taskAGENT;

    private String strAgentErrorCode;
    private String strCIFCode = "";
    private String strCIFBDMEmailId = "";
    private String strCIFBDMPassword = "";
    private String strCIFBDMMObileNo = "";

    private String nbamount_mtd = "0";
    private String surableamount_mtd = "0";

    private String nbamount_ytd = "0";
    private String surableamount_ytd = "0";
    private ViewPager viewPager;

    private ServiceHits service;
    private CommonMethods commonMethods;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_newdashboardagent);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        new CommonMethods().setApplicationToolbarMenu(this, "Dashboard");

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
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        String METHOD_NAME_SH = "saveSmartAdvisorServiceHits";
        String SOAP_ACTION_SH = "http://tempuri.org/saveSmartAdvisorServiceHits";
        service = new ServiceHits(SOAP_ACTION_SH, METHOD_NAME_SH);
        service.execute();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (taskAGENT != null) {
                taskAGENT.cancel(true);
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
    public void onBackPressed() {
        Intent i = new Intent(NewDashboardAgent.this,
                CarouselHomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (taskAGENT != null) {
            taskAGENT.cancel(true);
        }

        if (service != null) {
            service.cancel(true);
        }

        super.onDestroy();
    }

    class DownloadFileAsyncAGENT extends AsyncTask<String, String, String> {

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

                                if (taskAGENT != null)
                                    taskAGENT.cancel(true);
                                if (mProgressDialog.isShowing())
                                    mProgressDialog.dismiss();
                            }
                        });

                mProgressDialog.setMax(100);
                mProgressDialog.show();
            } catch (Exception ignored) {

            }
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                String NAMESPACE = "http://tempuri.org/";
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_AGENT_DASH);
                request.addProperty("agentcode", strCIFCode);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    String SOAP_ACTION_AGENT_DASH = "http://tempuri.org/getAgentDashBoard";
                    androidHttpTranport.call(SOAP_ACTION_AGENT_DASH, envelope);
                    Object response = envelope.getResponse();

                    SoapPrimitive sa;
                    try {
                        ParseXML prsObj = new ParseXML();

                        if (!response.toString().contentEquals("anyType{}")
                                && !response.toString().contentEquals(
                                "<AgentPolicyList />")) {

                            sa = (SoapPrimitive) envelope.getResponse();

                            String inputAgent = sa.toString();


							 /*<Table>
                                <SERVICEREGIONNAME>Bhopal</SERVICEREGIONNAME>
							    <CHANNELCODE>990316619</CHANNELCODE>
							    <SURRENDER_COUNT>1</SURRENDER_COUNT>
							    <SURRENDERAMOUNT>187647</SURRENDERAMOUNT>
							    <NB_COUNT>1</NB_COUNT>
							    <NB_AMOUNT>1500</NB_AMOUNT>
							    <UPDATE_TIMESTAMP>2017-05-15T08:31:32+05:30</UPDATE_TIMESTAMP>
							    <FREQUENCY>YTD</FREQUENCY>
							    <SURABLE_COUNT>1</SURABLE_COUNT>
							    <RP_AMOUNT>14745</RP_AMOUNT>
							  </Table>*/

                            inputAgent = prsObj.parseXmlTag(inputAgent,
                                    "AgentPolicyList");
                            inputAgent = new ParseXML().parseXmlTag(inputAgent,
                                    "ScreenData");
                            strAgentErrorCode = inputAgent;

                            if (strAgentErrorCode == null) {
                                inputAgent = sa.toString();

                                inputAgent = prsObj.parseXmlTag(inputAgent,
                                        "AgentPolicyList");

                                List<String> Node = prsObj.parseParentNode(
                                        inputAgent, "Table");

                                String nbcount_mtd = prsObj.parseXmlTag(Node.get(0),
                                        "NB_COUNT");
                                nbamount_mtd = prsObj.parseXmlTag(Node.get(0),
                                        "NB_AMOUNT");
                                String surablecount_mtd = prsObj.parseXmlTag(
                                        Node.get(0), "SURABLE_COUNT");
                                surableamount_mtd = prsObj.parseXmlTag(
                                        Node.get(0), "RP_AMOUNT");

                                if (Node.size() > 1) {
                                    String nbcount_ytd = prsObj.parseXmlTag(Node.get(1),
                                            "NB_COUNT");
                                    nbamount_ytd = prsObj.parseXmlTag(Node.get(1),
                                            "NB_AMOUNT");
                                    String surablecount_ytd = prsObj.parseXmlTag(
                                            Node.get(1), "SURABLE_COUNT");
                                    surableamount_ytd = prsObj.parseXmlTag(
                                            Node.get(1), "RP_AMOUNT");
                                }

                            } else {
                                strAgentErrorCode = "";
                            }
                        } else {
                            strAgentErrorCode = "";
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
            try {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (running) {

                if (strAgentErrorCode == null) {
                    String strType = "";
                    viewPager.setAdapter(new ViewPagerAdapter(
                            getSupportFragmentManager(), nbamount_mtd,
                            surableamount_mtd, nbamount_ytd, surableamount_ytd,
                            surableamount_mtd, surableamount_ytd, "",
                            surableamount_ytd, strType));
                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_RECORD_FOUND);
                }
            } else {
                commonMethods.showMessageDialog(context, commonMethods.NO_RECORD_FOUND);
            }
        }
    }

    class ServiceHits extends AsyncTask<String, Void, String> {
        final String SOAP_ACTION;
        final String METHOD_NAME;

        ServiceHits(String SOAP_ACTION, String METHOD_NAME) {
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
            // TODO Auto-generated method stub

            if (new CommonMethods().isNetworkConnected(context)) {

                try {
                    String NAMESPACE = "http://tempuri.org/";
                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                    request.addProperty("serviceName", METHOD_NAME_AGENT_DASH);
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

            taskAGENT = new DownloadFileAsyncAGENT();
            taskAGENT.execute();
        }

    }

}
