package sbilife.com.pointofsale_bancaagency.new_bussiness.pivc;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.Html;

import androidx.appcompat.app.AppCompatActivity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.ServiceURL;


public class Online_BI_Service extends AsyncTask<String, Object, String> {

    private static final String URl = ServiceURL.SERVICE_URL;
    private AppCompatActivity mActivity = null;
    private SoapObject request = null;
    private String soap_action = "", soap_output = "", soap_xml_main_tag;
    private Interface_Online_All_BI mInteAll_BI;
    private volatile boolean running = false;
    private ProgressDialog progressDialog;
    private ParseXML prsObj;

    public Online_BI_Service(AppCompatActivity mActivity, SoapObject request,
                             String soap_action, Interface_Online_All_BI mInteAll_BI, String soap_xml_main_tag) {
        super();
        this.mActivity = mActivity;
        this.request = request;
        this.soap_action = soap_action;
        this.mInteAll_BI = mInteAll_BI;
        this.soap_xml_main_tag = soap_xml_main_tag;
        prsObj = new ParseXML();
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        progressDialog = new ProgressDialog(mActivity,
                ProgressDialog.THEME_HOLO_LIGHT);

        String Message = "Please wait ,Loading...";

        progressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>"
                + Message + "<b></font>"));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub

        running = true;

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
        try {
            androidHttpTranport
                    .call(soap_action, envelope);

            SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();

            if (!sa.toString().equals(""))
                soap_output = prsObj
                        .parseXmlTag(sa.toString(), soap_xml_main_tag);
            else
                soap_output = "";

        } catch (Exception e) {
            e.printStackTrace();
            running = false;
            soap_output = "";
        }
        return soap_output;
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        mInteAll_BI.get_BI_Output(running, result);
    }
}
