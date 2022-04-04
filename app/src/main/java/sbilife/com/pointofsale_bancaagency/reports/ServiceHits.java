package sbilife.com.pointofsale_bancaagency.reports;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.text.Html;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class ServiceHits extends AsyncTask<String, Void, String> {
	// Context mContext;
    private final ProgressDialog progressDialog;
	private final String strServiceName;
    private final String serviceInput;
    private final String strCIFBDMUserId;
    private final String strCIFBDMEmailId;
    private final String strCIFBDMMObileNo;
    private final String strCIFBDMPassword;

    private final DownLoadData listener;
	private final CommonMethods mCommonMethods;
	private Context mContext;

	public ServiceHits(Context context,String strServiceName, String serviceInput, String strCIFBDMUserId,
			String strCIFBDMEmailId, String strCIFBDMMObileNo,
			String strCIFBDMPassword,DownLoadData listener) {

		this.mContext = context;

		progressDialog = new ProgressDialog(context,ProgressDialog.THEME_HOLO_LIGHT);			
		String Message = "Loading Please wait...";			
		progressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(false);
		progressDialog.setMax(100); 
		
		
		this.strServiceName = strServiceName;
		this.serviceInput = serviceInput;
		this.strCIFBDMUserId = strCIFBDMUserId;
		this.strCIFBDMEmailId = strCIFBDMEmailId;
		this.strCIFBDMMObileNo = strCIFBDMMObileNo;
		this.strCIFBDMPassword = strCIFBDMPassword;
		this.listener = listener;
		
		mCommonMethods = new CommonMethods();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if (progressDialog != null)
			progressDialog.show();
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	protected String doInBackground(String... param) {

			if (mCommonMethods.isNetworkConnected(mContext)) {
				try {
					String NAMESPACE_SERV = "http://tempuri.org/";
					String METHOD_NAME_SERV = "saveSmartAdvisorServiceHits";
					SoapObject request = new SoapObject(NAMESPACE_SERV, METHOD_NAME_SERV);

				/* <serviceName>string</serviceName>
				  <strProdCode>string</strProdCode>
				  <serviceInput>string</serviceInput>
				  <serviceReqUserId>string</serviceReqUserId>
				  <strEmailId>string</strEmailId>
				  <strMobileNo>string</strMobileNo>
				  <strAuthKey>string</strAuthKey>*/
					request.addProperty("serviceName", strServiceName);
					request.addProperty("strProdCode", "");


					request.addProperty("serviceInput", serviceInput);
					request.addProperty("serviceReqUserId", strCIFBDMUserId);
					request.addProperty("strEmailId", strCIFBDMEmailId);
					request.addProperty("strMobileNo", strCIFBDMMObileNo);
					request.addProperty("strAuthKey", strCIFBDMPassword.trim());

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);
					// Enable this envelope if service is written in dot net
					envelope.dotNet = true;
					envelope.setOutputSoapObject(request);
					String URl = ServiceURL.SERVICE_URL;
					HttpTransportSE androidHttpTransport = new HttpTransportSE(
							URl);

					// allowAllSSL();
					mCommonMethods.TLSv12Enable();

					StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
							.permitAll().build();
					StrictMode.setThreadPolicy(policy);

					String SOAP_ACTION_SERV = "http://tempuri.org/saveSmartAdvisorServiceHits";
					androidHttpTransport.call(SOAP_ACTION_SERV, envelope);
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
			}else{
				return  mCommonMethods.NO_INTERNET_MESSAGE;
			}
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try {
			if (progressDialog.isShowing())
				progressDialog.dismiss();

		} catch (Exception e) {
			e.getMessage();
		}

		listener.downLoadData();
	}
	
	public interface DownLoadData{
		void downLoadData();
	}

}
