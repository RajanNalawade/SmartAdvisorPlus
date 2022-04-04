package sbilife.com.pointofsale_bancaagency.common;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;

import android.text.TextUtils;


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
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsPersistency;

/**
 * Created by e24356 on 24-07-2017.
 */

public class PersistencyNotification {

    Context context;


    public PersistencyNotification(Context mContext) {
        context = mContext;
    }


    public void senNotificationPersistency() {

        DownloadPersistency downloadPersistency = new DownloadPersistency();
        downloadPersistency.execute();

    }

    /***********  Async Task - It will display notification prior one day*************/


    class DownloadPersistency extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String strpersistencylist = "", strpersistencyErrorCOde = "", strpersistencyErCd = "";
        private ArrayList<ParseXML.XMLHolderPersistency> lsPersistency;
        private String SOAP_ACTION = "http://tempuri.org/getPersistency";
        private final String METHOD_NAME_PERSISTENCY = "getPersistency";
        private CommonMethods commonMethods;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            commonMethods = new CommonMethods();
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                SoapObject request;

                int m = 1;

                Calendar cal = Calendar.getInstance();
                String monthName = new SimpleDateFormat("MMM").format(cal
                        .getTime());

                CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
                String strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
                String strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
                String strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
                String strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME_PERSISTENCY);

                request.addProperty("strAgenyCode", strCIFBDMUserId);
                request.addProperty("checkFlag", m);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());
                request.addProperty("strMonth", monthName.toUpperCase());


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

                    androidHttpTranport.call(SOAP_ACTION, envelope);
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
                                            .parseNodeElementPersistency(Node, "13");

                                    lsPersistency = new ArrayList<>();
                                    lsPersistency.clear();

                                    lsPersistency.addAll(nodeData);

                                }

                            }

                        } catch (Exception e) {

                            running = false;
                        }
                    }

                } catch (IOException | XmlPullParserException e) {

                    running = false;
                }
            } catch (Exception e) {

                running = false;
            }
            return null;

        }


        @Override
        protected void onPostExecute(String unused) {

            if (running) {


                if (strpersistencyErCd == null) {
                } else {
                    if (strpersistencyErrorCOde == null) {

                        int index = lsPersistency.size() - 1;
                        String message = "";
                        String actualPerString = lsPersistency.get(index).getCurmonth_ratio();

                        double actualPerDouble = 0.0, totalCollectedDouble = 0.0, totalCollectableDouble = 0.0;
                        if (!TextUtils.isEmpty(actualPerString)) {
                            actualPerString = actualPerString.replace("%", "");

                            try{
                                actualPerDouble = Double.parseDouble(actualPerString);
                                if (actualPerDouble < 87) {

                                    String ratio = String.format("%.1f", 87 - actualPerDouble);
                                    String totalCollectedString = lsPersistency.get(index).getCurmonth_collected_pre();
                                    String totalCollectableStirng = lsPersistency.get(index).getCurmonth_collectable_pre();

                                    if (!TextUtils.isEmpty(totalCollectedString)) {
                                        totalCollectedDouble = Double.parseDouble(totalCollectedString);
                                    }

                                    if (!TextUtils.isEmpty(totalCollectableStirng)) {
                                        totalCollectableDouble = Double.parseDouble(totalCollectableStirng);
                                    }

                                    String perNeeded = String.format("%.2f", ((totalCollectableDouble * 87) / 100) - totalCollectedDouble);
                                    //You are only 8.4 % away from your target. Need only 0.90 lakhs to hit 87% 13th month persistency
                                    message = "Dear " + commonMethods.getUserName(context) + "\nYou are only " + ratio + "% away from your target. Need only " + context.getString(R.string.Rs)
                                            + perNeeded + " lakhs to hit 87% 13th month persistency";

                                    // Gets an instance of the NotificationManager service

                                /*AppSharedPreferences.setData(context, commonMethods.PERSISTENCY_NOTIFICATION,
                                                "1");*/
                                    String title = "Persistency";
                                    int notificationId = -7;
                                    Intent i = new Intent(context, AgencyReportsPersistency.class);
                                    i.putExtra("fromHome", "Y");
                                    PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), i, 0);
                                    commonMethods.commonNotification(context, pendingIntent, title, message, notificationId);

                                    AppSharedPreferences.setData(context, new AppSharedPreferences().PERSISTENCY_KEY, "1");
                                }
                            }catch (NumberFormatException ex){
                                ex.printStackTrace();
                            }
                        }
                    }

                }
            }
        }
    }

}
/****************************** Async Version ends here *******************************************/







