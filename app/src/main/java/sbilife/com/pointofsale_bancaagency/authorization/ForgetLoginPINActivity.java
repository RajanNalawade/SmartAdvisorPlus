package sbilife.com.pointofsale_bancaagency.authorization;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.ref.WeakReference;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class ForgetLoginPINActivity extends AppCompatActivity implements OnClickListener,ServiceHits.DownLoadData {
	private  final String NAMESPACE = "http://tempuri.org/";
	private  final String METHOD_NAME_FORGET_PASSWORD_SMRT = "ForgetPIN_SMRT";
	private  final String URl = ServiceURL.SERVICE_URL;
	
	private  final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private CommonMethods mCommonMethods;
	private ProgressDialog progressDialog = null;
	private Context context;

    private EditText editTextForgetPINUserCode,editTextForgetPINEmailId;
	
	private String userCode = "",emailId = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.forget_login_pin);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.smart_advisor_title);
		
		TextView tv_title_common = this.findViewById(R.id.tv_title_cif_common);
		tv_title_common.setText("Forgot PIN");
		context = this;
		mCommonMethods = new CommonMethods();
        Button buttonForgetPINReset = findViewById(R.id.buttonForgetPINReset);
		
		editTextForgetPINUserCode = findViewById(R.id.editTextForgetPINUserCode);
		editTextForgetPINEmailId = findViewById(R.id.editTextForgetPINEmailId);
		
		buttonForgetPINReset.setOnClickListener(this);
	}
	
	@Override
	public void downLoadData() {
	  
		WeakReference<ForgetLoginPINActivity> mainActivityWeakRef = new WeakReference<>(ForgetLoginPINActivity.this);
		  
		  if (mainActivityWeakRef.get() != null && !mainActivityWeakRef.get().isFinishing()) {
			final Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.loading_window);
			dialog.setCancelable(false);
			TextView text = dialog.findViewById(R.id.txtalertheader);
			text.setText("PIN Sent Successfully");

			Button dialogButton = dialog.findViewById(R.id.btnalert);
			dialogButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dialog.dismiss();
					Intent intent = new Intent(context, LoginUserActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
				}
			});
			dialog.show();
		}
	}
	private void service_hits() {
		 
		ServiceHits service = new ServiceHits(this,
				 METHOD_NAME_FORGET_PASSWORD_SMRT,  "", userCode,
				 emailId,  "", "", this);
		service.execute();
	}
	@Override
	public void onClick(View view) {
		
		int id = view.getId();
		if (id == R.id.buttonForgetPINReset) {
			userCode = editTextForgetPINUserCode.getText().toString();
			emailId =  editTextForgetPINEmailId.getText().toString();
			if(validation()){
                DownloadFileAsyncForgetPINSMRT taskAsyncForgetPINSMRT = new DownloadFileAsyncForgetPINSMRT();
				taskAsyncForgetPINSMRT.execute();
			}
		}
	}
		
	private boolean validation(){
		String error = "";
		boolean isvalid = true;
		try {
				if(!mCommonMethods.isNetworkConnected(context)){
					error = "Please turn on your internet connection";
				}
				else if (userCode.equalsIgnoreCase("")) {
						error = "Please enter user code";
					} else if (emailId.equalsIgnoreCase("")) {
						error = "Please enter Email-Id";
					}
					isvalid = mCommonMethods.emailPatternValidation(editTextForgetPINEmailId, context);
			
				if (!error.equals("")) {
					mCommonMethods.showMessageDialog(context, error);
					isvalid = false;
				}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return isvalid;
	}
	
	
	class DownloadFileAsyncForgetPINSMRT extends AsyncTask<String, String, String> {

		private volatile boolean running = true;
		private String inputpolicylist = "",strAuthUserErrorCOde = "";
		private final String SOAP_ACTION_FORGET_PASSWORD_SMRT = "http://tempuri.org/ForgetPIN_SMRT";

		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog();
			//buttonGenrateOtp.setVisibility(View.GONE);
		}

		@Override
		protected String doInBackground(String... aurl) {
			try {
				
				running = true;
				SoapObject request = new SoapObject(NAMESPACE,
						METHOD_NAME_FORGET_PASSWORD_SMRT);
				
				request.addProperty("strEmailID", emailId);
				request.addProperty("strCode", userCode);

				mCommonMethods.TLSv12Enable();
				
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
				try {
					androidHttpTranport.call(SOAP_ACTION_FORGET_PASSWORD_SMRT,envelope);

					SoapPrimitive sa;
					try {
						sa = (SoapPrimitive) envelope.getResponse();
						inputpolicylist = sa.toString();

						if (inputpolicylist.equalsIgnoreCase("1")) {
							strAuthUserErrorCOde = inputpolicylist;
							
						} else {
							strAuthUserErrorCOde = "";
						}

					} catch (Exception e) {
						try {
							throw (e);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						progressDialog.dismiss();
						running = false;
					}

				} catch (IOException | XmlPullParserException e) {
					try {
						throw (e);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					progressDialog.dismiss();
					running = false;
				}
			} catch (Exception e) {
				try {
					throw (e);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				progressDialog.dismiss();
				running = false;
			}
			return null;

		}


		@Override
		protected void onPostExecute(String unused) {
			if(progressDialog.isShowing())
			{
				dismissProgressDialog();
			}
			
			if (running) {
				if (strAuthUserErrorCOde.equals("1")) {
					
					service_hits();
				} else {
					mCommonMethods.showMessageDialog(context, mCommonMethods.SERVER_ERROR);
				}
			} else {
				mCommonMethods.showMessageDialog(context, mCommonMethods.SERVER_ERROR);
			}
		}
	}
	@SuppressWarnings("deprecation")
	private void showProgressDialog()
	{
		showDialog(DIALOG_DOWNLOAD_PROGRESS);
	}
	@SuppressWarnings("deprecation")
	private void dismissProgressDialog()
	{
		dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
	}
	@SuppressWarnings("deprecation")
	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:
			progressDialog = new ProgressDialog(this,ProgressDialog.THEME_HOLO_LIGHT);			
			String Message = "Loading Please wait...";			
			progressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(true);
			
			progressDialog.setButton("Cancel", new DialogInterface.OnClickListener() {				
				public void onClick(DialogInterface dialog, int which) {
					progressDialog.dismiss();
				}
			});
			
			progressDialog.setMax(100); 
			progressDialog.show();
			return progressDialog;
				
		default:
			return null;
        }
    }
	
	@Override
    public void onBackPressed() {
		Intent intent = new Intent(context, LoginUserActivity.class);
	    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
	    context.startActivity(intent); 
	    finish();
    }
}
