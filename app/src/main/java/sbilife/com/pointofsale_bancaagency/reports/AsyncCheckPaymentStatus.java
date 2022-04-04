package sbilife.com.pointofsale_bancaagency.reports;

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

import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class AsyncCheckPaymentStatus extends AsyncTask<String, Void, String> {
    // Context mContext;
    private final ProgressDialog mProgressDialog;
    private final String policyNumber;
    private final String str_BatchNumber;
    private final String str_amount;
    private final String TransationId;
    private final String str_Status;
    private final String str_Error;
    private final String cardType;
    boolean running = true;
    String strPayment_Status;

    private final CommonMethods mCommonMethods;
    private Context mContext;

    public AsyncCheckPaymentStatus(Context context, String policyNumber, String str_BatchNumber, String str_amount,
                                   String TransationId, String str_Status, String str_Error, String cardType) {

        this.mContext = context;

        mProgressDialog = new ProgressDialog(context,ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMax(100);


        this.policyNumber = policyNumber;
        this.str_BatchNumber = str_BatchNumber;
        this.str_amount = str_amount;
        this.TransationId = TransationId;
        this.str_Status = str_Status;
        this.str_Error = str_Error;
        this.cardType = cardType;

        mCommonMethods = new CommonMethods();
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        mProgressDialog.show();
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected String doInBackground(String... param) {
        // TODO Auto-generated method stub
        // Get Channel Detail

//        running = true;
        String NAMESPACE = "http://tempuri.org/";
        String URl = ServiceURL.SERVICE_URL;
        /*String SOAP_ACTION_saveEzTabPaymentTrans = "http://tempuri.org/savePaymentTrans_cl";
        String METHOD_NAME_saveEzTabPaymentTrans = "savePaymentTrans_cl";*/
        String SOAP_ACTION_saveEzTabPaymentTrans = "http://tempuri.org/savePaymentTrans_ea";
        String METHOD_NAME_saveEzTabPaymentTrans = "savePaymentTrans_ea";

        running = true;

        try {
            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME_saveEzTabPaymentTrans);

            request.addProperty("strSource", "SBI-mPOS");
            request.addProperty("quNo", policyNumber);
            request.addProperty("orderNo", str_BatchNumber);
            request.addProperty("grossPremAMT", str_amount);
            request.addProperty("txnRefNo", TransationId);
           //request.addProperty("bankCode", "00");
            request.addProperty("bankCode", cardType);
            request.addProperty("strTime", System.currentTimeMillis());
            request.addProperty("strStatus", str_Status);
            request.addProperty("authStatus", "0300");
            request.addProperty("strErrDesc", str_Error);
             /*       request.addProperty("strCARD_TYPE", str_CardType);
                    request.addProperty("strINVIOICE_NO", str_InvoiceNumber);
                    request.addProperty("strMERCHANT_NAME", str_MerchantName);
                    request.addProperty("strNAME_ON_CARD", str_NameOnCard);
                    request.addProperty("strPAYMENT_MODE", str_PaymentMode);
                    request.addProperty("strEmailId", email_Id);
                    request.addProperty("strMobileNo", mobile_No);
                    request.addProperty("strAuthKey", encr_Password.trim());*/
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            mCommonMethods.TLSv12Enable();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            HttpTransportSE androidHttpTranport = new HttpTransportSE(
                    URl);

            androidHttpTranport.call(SOAP_ACTION_saveEzTabPaymentTrans,
                    envelope);


            Object response = envelope.getResponse();

            strPayment_Status = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            running = false;

            return "Server not responding...";
        }


        return null;

    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();

        if (running != false) {

            if (strPayment_Status.equals("1")) {
                mCommonMethods.showMessageDialog(mContext, "Payment Success.Thanks For Payment");
            } else {
                mCommonMethods.showMessageDialog(mContext, "Data Not Sync. Please Try Again");
            }
        } else {
            mCommonMethods.showMessageDialog(mContext, "Not Sync. Server not Responding.. please try Again");


        }
    }



}
