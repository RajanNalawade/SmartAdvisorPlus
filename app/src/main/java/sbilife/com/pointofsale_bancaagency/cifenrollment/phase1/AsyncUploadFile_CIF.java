package sbilife.com.pointofsale_bancaagency.cifenrollment.phase1;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.text.Html;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class AsyncUploadFile_CIF extends AsyncTask<String, String, Boolean> {

    private final String NAMESPACE = "http://tempuri.org/",
            strSoapAction;

    private final ProgressDialog mProgressDialog;
    private final Interface_Upload_CIF_Files listener;
    private final CommonMethods mCommonMethods;
    private Context mContext;
    private SoapObject mRequest;

    public AsyncUploadFile_CIF(Context mContext, Interface_Upload_CIF_Files listener, SoapObject mRequest,
                               final String strSoapAction) {
        this.listener = listener;
        this.mContext = mContext;
        this.mRequest = mRequest;
        this.strSoapAction = strSoapAction;

        mCommonMethods = new CommonMethods();
        mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setTitle(Html
                .fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog.show();
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        if (mCommonMethods.isNetworkConnected(mContext)) {
            try {

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);

                new MarshalBase64().register(envelope); // serialization
                // Enable this envelope if service is written in dot net
                envelope.dotNet = true;
                envelope.setOutputSoapObject(mRequest);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(ServiceURL.SERVICE_URL);

                // allowAllSSL();
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                androidHttpTransport.call(NAMESPACE + strSoapAction, envelope);

                SoapObject soapObject;
                SoapPrimitive soapPrimitive;
                String result = "";

                if(envelope.getResponse() instanceof SoapObject){
                    soapObject = (SoapObject) envelope.getResponse();
                    result = soapObject.toString();
                }else {
                    soapPrimitive = (SoapPrimitive) envelope.getResponse();
                    result = soapPrimitive.toString();
                }

                if (result.contains("1")) {
                    return true;
                } else {
                    return false;
                }

            } catch (Exception e) {
                return false;
            }
        }else{
            return  false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        //super.onPostExecute(result);
        try {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

        } catch (Exception e) {
            e.getMessage();
        }

        listener.onUploadComplete(result);
    }

    public interface Interface_Upload_CIF_Files {
        void onUploadComplete(Boolean result);
    }
}
