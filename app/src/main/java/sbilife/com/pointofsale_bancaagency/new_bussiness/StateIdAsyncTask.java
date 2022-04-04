package sbilife.com.pointofsale_bancaagency.new_bussiness;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.text.Html;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.AppSharedPreferences;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class StateIdAsyncTask extends AsyncTask<String, String, String> {
    private volatile boolean running = true;
    private final String NAMESPACE = "http://tempuri.org/";
    private final String URl = ServiceURL.SERVICE_URL;

    private final String SOAP_ACTION_STATE_ID = "http://tempuri.org/getStateID";
    private final String METHOD_NAME_STATE_ID = "getStateID";
    private ProgressDialog mProgressDialog;
    private String inputpolicylist = "";
    private Context context;
    private String strCode, strType;

    public StateIdAsyncTask(Context context, String strCode, String strType) {
        this.context = context;
        this.strCode = strCode;
        this.strType = strType;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        mProgressDialog = new ProgressDialog(context,
                ProgressDialog.THEME_HOLO_LIGHT);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setTitle(Html.fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    protected String doInBackground(String... aurl) {

        try {
            running = true;

            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME_STATE_ID);

            request.addProperty("strCode", strCode);
            request.addProperty("strType", strType);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
            androidHttpTranport.call(SOAP_ACTION_STATE_ID,
                    envelope);

            Object sa = envelope.getResponse();
            inputpolicylist = sa.toString();

            System.out.println("response:" + inputpolicylist);
            if (!inputpolicylist.contentEquals("0")) {

                try {

                } catch (Exception e) {

                    mProgressDialog.dismiss();
                    running = false;
                }
            } else {
                mProgressDialog.dismiss();
                running = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            running = false;
        }
        return null;

    }

    @Override
    protected void onPostExecute(String unused) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        if (running) {
            if (!inputpolicylist.contentEquals("0")) {

                String kerlaDiscDetails = strCode + "," + inputpolicylist;
                AppSharedPreferences.setData(context, (new CommonMethods().getKerlaDiscount()), kerlaDiscDetails);

            } else {

            }

        }


    }
}
