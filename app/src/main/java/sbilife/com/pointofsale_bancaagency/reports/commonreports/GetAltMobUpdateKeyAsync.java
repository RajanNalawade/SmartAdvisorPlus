package sbilife.com.pointofsale_bancaagency.reports.commonreports;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.text.Html;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.UpdateAltMobileNoCommonAsyncTask;

public class GetAltMobUpdateKeyAsync extends AsyncTask<String, String, String> {

    private volatile boolean running = true;
    private final String dueDate;
    private final String pAY_EX1_74;

    private final String mobileNumber;
    private final ProgressDialog progressDialog;
    private final UpdateAltMobileNoCommonAsyncTask.UpdateAltMobNoInterface listener;
    private final CommonMethods commonMethods;
    private final Context context;
    private List<ParseXML.XMLHolderAdvanceQueryResult> nodeData;

    public GetAltMobUpdateKeyAsync(String dueDate, String pAY_EX1_74, String mobileNumber,
                                   Context context, UpdateAltMobileNoCommonAsyncTask.UpdateAltMobNoInterface listener) {
        this.dueDate = dueDate;
        this.pAY_EX1_74 = pAY_EX1_74;
        this.mobileNumber = mobileNumber;
        this.listener = listener;
        this.context = context;

        progressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        progressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.setMax(100);

        commonMethods = new CommonMethods();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected String doInBackground(String... aurl) {
        try {

            running = true;
            SoapObject request;

            String NAMESPACE = "http://tempuri.org/";

            String METHOD_NAME_ADVANCE_QUERY = "getCRMList";

            request = new SoapObject(NAMESPACE,
                    METHOD_NAME_ADVANCE_QUERY);
            String newDueDate = "";
            try {
                String[] separated = dueDate.split("-");
                String day = separated[0]; // this will contain "Fruit"
                String month = commonMethods.getMonthNumber(separated[1]);
                String year = separated[2];
                int lastTwoDigits = Integer.parseInt(year) % 100;

                newDueDate = day + "-" + month + "-" + lastTwoDigits;
            } catch (Exception e) {
                e.printStackTrace();
            }


            request.addProperty("strPolicyNo", pAY_EX1_74);//"98989898");// pAY_EX1_74);
            request.addProperty("strDueDate", newDueDate);// "03-01-18");// newDueDate);

            System.out.println("request:" + request.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            // allowAllSSL();
            commonMethods.TLSv12Enable();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String URl = ServiceURL.SERVICE_URL;
            HttpTransportSE androidHttpTranport = new HttpTransportSE(URl, 300000);
            try {
                String SOAP_ACTION_ADVANCE_QUERY = "http://tempuri.org/getCRMList";
                androidHttpTranport.call(SOAP_ACTION_ADVANCE_QUERY,
                        envelope);

                Object response = envelope.getResponse();

                if (!response.toString().contentEquals("<NewDataSet /> ")) {
                            /*<NewDataSet>
                              <QueryResults>
                                <PAY_EX1_91>Move to Call Centre</PAY_EX1_91>
                                <PAY_EX1_96>Forward for structured follow up</PAY_EX1_96>
                                <HTMLTEXT_280>LastModified By: 1point1 LastModified On: 02-07-2018 7:51:24 PM&lt;br /&gt;
                                                    Customer contact no is updated&lt;br /&gt;</HTMLTEXT_280>
                                <PAY_EX1_95>AMBER</PAY_EX1_95>
                                <ROWNUMBER>1</ROWNUMBER>
                                <Key>45693</Key>
                              </QueryResults>
                            </NewDataSet>*/
                    //SoapPrimitive sa = null;
                    try {
                        // sa = (SoapPrimitive) envelope.getResponse();
                        String inputpolicylist = response.toString();
                        System.out.println("request:" + inputpolicylist);
                        if (!inputpolicylist.equalsIgnoreCase("<NewDataSet /> ")) {

                            ParseXML parseXML = new ParseXML();
                            String DataResultXML = parseXML.parseXmlTag(
                                    inputpolicylist, "NewDataSet");
                            //DataResultXML = escapeXml(DataResultXML);
                            List<String> Node = parseXML.parseParentNode(
                                    DataResultXML, "QueryResults");
                            nodeData = parseXML
                                    .parseNodeAdvanceQueryResult(Node);

                        }

                    } catch (Exception e) {

                        progressDialog.dismiss();
                        running = false;
                    }
                }

            } catch (IOException e) {

                progressDialog.dismiss();
                running = false;
            } catch (XmlPullParserException e) {
                progressDialog.dismiss();
                running = false;
            }
        } catch (Exception e) {
            progressDialog.dismiss();
            running = false;
        }
        return null;

    }

    @Override
    protected void onPostExecute(String unused) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (running) {

            if (nodeData.size() > 0) {
                String paymentKey = nodeData.get(0).getKey();
                UpdateAltMobileNoCommonAsyncTask updateAltMobileNoCommonAsyncTask = new UpdateAltMobileNoCommonAsyncTask(
                        paymentKey, mobileNumber, context, listener);
                updateAltMobileNoCommonAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                commonMethods.showMessageDialog(context, commonMethods.NO_RECORD_FOUND);
            }
        } else {
            commonMethods.showMessageDialog(context, commonMethods.NO_RECORD_FOUND);
        }
    }
}
