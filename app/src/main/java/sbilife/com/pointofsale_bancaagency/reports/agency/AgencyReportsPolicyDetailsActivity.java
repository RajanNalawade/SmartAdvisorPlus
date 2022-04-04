package sbilife.com.pointofsale_bancaagency.reports.agency;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods.UserDetailsValuesModel;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits.DownLoadData;

@SuppressLint("SimpleDateFormat") 
public class AgencyReportsPolicyDetailsActivity extends AppCompatActivity implements OnClickListener,DownLoadData{

    private  final String METHOD_NAME_POLICY_DETAIL_AGENCY = "getAgentPolicyDtls";

	private DownloadFileAsyncPolicyDetail taskPolicyDetail;
    private final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;

    private CommonMethods commonMethods;
	private Context context;
	private ServiceHits service;
	
	private String strCIFBDMUserId = "", strCIFBDMEmailId = "",
		       strCIFBDMPassword = "",strCIFBDMMObileNo = "",
		       strPolicyDetailErrorCOde="",strPolicyDetailpolicyno = "",
		       strPolicyDetailPolNo = "",strPolicyDetailCustCode,
		       strPolicyDetailFame = "",strPolicyDetailLName = "",
		   	   strPolicyDetailStatus = "",strPolicyDetailPremiumUp = "",
		   	   strPolicyDetailGrossAmt = "";
	
	private TextView txtpolicydetailno, txtpolicydetailcustcode,
			 txtpolicydetailfirstname,txterrordescpolicydetail,
			 txtpolicydetailstatus,txtpolicydetailpremiumup,
			 txtpolicydetailpremiumamt;
	private TableLayout tblPolicyDetail;
	private EditText edpolicyno;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.banca_reports_policy_details);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		context = this;
		commonMethods = new CommonMethods();
		
		commonMethods.setApplicationToolbarMenu(this,"Help Desk"); ;
		
        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");
        if (fromHome != null && fromHome.equalsIgnoreCase("N")) {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");

            try {
                strCIFBDMPassword = SimpleCrypto.encrypt("SBIL", "sbil");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            getUserDetails();
        }

		taskPolicyDetail = new DownloadFileAsyncPolicyDetail();
		mProgressDialog = new ProgressDialog(context);
		
		txtpolicydetailno = findViewById(R.id.txtpolicydetailno);
		txtpolicydetailcustcode = findViewById(R.id.txtpolicydetailcustcode);
		txtpolicydetailfirstname = findViewById(R.id.txtpolicydetailfirstname);
		txterrordescpolicydetail = findViewById(R.id.txterrordescpolicydetail);
		txtpolicydetailstatus = findViewById(R.id.txtpolicydetailstatus);
		txtpolicydetailpremiumup = findViewById(R.id.txtpolicydetailpremiumup);
		txtpolicydetailpremiumamt = findViewById(R.id.txtpolicydetailpremiumamt);

		tblPolicyDetail = findViewById(R.id.tblPolicyDetail);
		edpolicyno  = findViewById(R.id.edpolicyno);
        Button btn_savepolicydetail = findViewById(R.id.btn_savepolicydetail);
        Button btn_reset_policy_detail = findViewById(R.id.btn_reset_policy_detail);
        Button btnviewmoredetail = findViewById(R.id.btnviewmoredetail);
        Button btnviewcustdetail = findViewById(R.id.btnviewcustdetail);
		
		btn_savepolicydetail.setOnClickListener(this);
		btn_reset_policy_detail.setOnClickListener(this);
		btnviewmoredetail.setOnClickListener(this);
		btnviewcustdetail.setOnClickListener(this);

	}
	private void getUserDetails(){
		UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
		strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
	    strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
	    strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
	    strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

	}
	private void service_hits() {
        service = new ServiceHits(context,
				METHOD_NAME_POLICY_DETAIL_AGENCY,strPolicyDetailpolicyno,  strCIFBDMUserId,
				 strCIFBDMEmailId,  strCIFBDMMObileNo,
				 strCIFBDMPassword, this);
		service.execute();
	}


	class DownloadFileAsyncPolicyDetail extends
			AsyncTask<String, String, String> {

		private volatile boolean running = true;
		private String inputpolicylist = "";
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog();
		}

		@Override
		protected String doInBackground(String... aurl) {
			try {

				running = true;
				SoapObject request;

				String NAMESPACE = "http://tempuri.org/";
				request = new SoapObject(NAMESPACE,
						METHOD_NAME_POLICY_DETAIL_AGENCY);
				request.addProperty("strAgentNo", strCIFBDMUserId);
				
				request.addProperty("policyNo", strPolicyDetailpolicyno);
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

				String URl = ServiceURL.SERVICE_URL;
				HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
				try {

                    String SOAP_ACTION_POLICY_DETAIL_AGENCY = "http://tempuri.org/getAgentPolicyDtls";
                    androidHttpTranport.call(
                            SOAP_ACTION_POLICY_DETAIL_AGENCY, envelope);
					
					Object response = envelope.getResponse();

					if (!response.toString().contentEquals("anyType{}")) {

						SoapPrimitive sa;
						try {
							sa = (SoapPrimitive) envelope.getResponse();

							inputpolicylist = sa.toString();

							ParseXML prsObj = new ParseXML();

							inputpolicylist = prsObj.parseXmlTag(
									inputpolicylist, "CIFPolicyList");

							if (inputpolicylist != null) {

								inputpolicylist = new ParseXML().parseXmlTag(
										inputpolicylist, "ScreenData");
								strPolicyDetailErrorCOde = inputpolicylist;

								if (strPolicyDetailErrorCOde == null) {
									inputpolicylist = sa.toString();
									inputpolicylist = prsObj.parseXmlTag(
											inputpolicylist, "CIFPolicyList");
									inputpolicylist = new ParseXML()
											.parseXmlTag(inputpolicylist,
													"Table");

									strPolicyDetailPolNo = new ParseXML()
											.parseXmlTag(inputpolicylist,
													"POLICYNUMBER");
									strPolicyDetailCustCode = new ParseXML()
											.parseXmlTag(inputpolicylist,
													"HOLDERID");
									strPolicyDetailFame = new ParseXML()
											.parseXmlTag(inputpolicylist,
													"HOLDERPERSONFIRSTNAME");
									strPolicyDetailLName = new ParseXML()
											.parseXmlTag(inputpolicylist,
													"HOLDERPERSONLASTNAME");
									strPolicyDetailStatus = new ParseXML()
											.parseXmlTag(inputpolicylist,
													"POLICYCURRENTSTATUS");
									strPolicyDetailPremiumUp = new ParseXML()
											.parseXmlTag(inputpolicylist,
													"PREMIUMFUP");
									strPolicyDetailGrossAmt = new ParseXML()
											.parseXmlTag(inputpolicylist,
													"PREMIUMGROSSAMOUNT");

									if (strPolicyDetailPremiumUp == null) {
										strPolicyDetailPremiumUp = "";
									} else {

										strPolicyDetailPremiumUp = strPolicyDetailPremiumUp
												.split("T")[0];

										Date dt1 = null;
										SimpleDateFormat df = new SimpleDateFormat(
												"yyyy-MM-dd");
										SimpleDateFormat df1 = new SimpleDateFormat(
												"dd-MMMM-yyyy");
										try {
											dt1 = df.parse(strPolicyDetailPremiumUp);
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										strPolicyDetailPremiumUp = df1
												.format(dt1);

									}

								}
							} else {
								strPolicyDetailErrorCOde = "1";
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
		protected void onProgressUpdate(String... progress) {
			Log.d("ANDRO_ASYNC", progress[0]);
			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}

		@Override
		protected void onPostExecute(String unused) {
			if (mProgressDialog.isShowing()) {
				dismissProgressDialog();
			}
			if (running) {
				if (strPolicyDetailErrorCOde == null) {
					txterrordescpolicydetail.setText("");

					txtpolicydetailno.setText(strPolicyDetailPolNo);
					txtpolicydetailcustcode.setText(strPolicyDetailCustCode);
					txtpolicydetailfirstname.setText(strPolicyDetailFame + " "
							+ strPolicyDetailLName);
					// txtpolicydetaillastname.setText(strPolicyDetailLName);
					txtpolicydetailstatus.setText(strPolicyDetailStatus);
					txtpolicydetailpremiumup.setText(strPolicyDetailPremiumUp);
					txtpolicydetailpremiumamt.setText(strPolicyDetailGrossAmt);

					tblPolicyDetail.setVisibility(View.VISIBLE);
					/*tblPolicyDetail.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
					tblPolicyDetail.requestLayout();*/
					
				} else {
					txterrordescpolicydetail.setText("No Record Found");

					txtpolicydetailno.setText("");
					txtpolicydetailcustcode.setText("");
					txtpolicydetailfirstname.setText("");
					// txtpolicydetaillastname.setText("");
					txtpolicydetailstatus.setText("");
					txtpolicydetailpremiumup.setText("");
					txtpolicydetailpremiumamt.setText("");

					/*tblPolicyDetail.getLayoutParams().height = 0;
					tblPolicyDetail.requestLayout();*/
					tblPolicyDetail.setVisibility(View.GONE);
				}
			} else {
				//servererror();
				commonMethods.showMessageDialog(context, commonMethods.SERVER_ERROR);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
    private void dismissProgressDialog() {
		dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
	}

	@SuppressWarnings("deprecation")
    private void showProgressDialog() {
		showDialog(DIALOG_DOWNLOAD_PROGRESS);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			if(taskPolicyDetail!=null)
			{
				taskPolicyDetail.cancel(true);
			}
			if(mProgressDialog!=null)
			{
				if (mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}
			}
			finish();

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:
			mProgressDialog = new ProgressDialog(this,ProgressDialog.THEME_HOLO_LIGHT);	
			String Message = "Loading Please wait...";
			mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(true);

			mProgressDialog.setButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if(taskPolicyDetail!=null)
							{
								taskPolicyDetail.cancel(true);
							}
							if(mProgressDialog!=null)
							{
								if (mProgressDialog.isShowing()) {
									mProgressDialog.dismiss();
								}
							}
						}
					});

			mProgressDialog.setMax(100);
			mProgressDialog.show();
			return mProgressDialog;

		/*case DATE_DIALOG_ID:
			return new DatePickerDialog(this, R.style.datepickerstyle,
					mDateSetListener, mYear, mMonth, mDay);*/

		default:
			return null;
		}
	}
	
	private void startDownloadPolicyDetail() {
		// new DownloadFileAsyncPolicyDetail().execute("demo");
		taskPolicyDetail = new DownloadFileAsyncPolicyDetail();
		taskPolicyDetail.execute("demo");
	}
	
	@Override
	public void downLoadData() {
		startDownloadPolicyDetail();
	}
	@Override
	public void onClick(View v) {
		
		int id = v.getId();
		switch (id) {
			case R.id.btn_savepolicydetail:
				getPolicyDetails();
				break;
			case R.id.btn_reset_policy_detail:
				btnResetPolicyDetail();
				break;
			case R.id.btnviewmoredetail:
				viewMorePolicy();
				break;
			case R.id.btnviewcustdetail:
				viewCustomerDetails();
				break;
		}
	}
	
	private void getPolicyDetails()
	{
		taskPolicyDetail = new DownloadFileAsyncPolicyDetail();

		txterrordescpolicydetail.setVisibility(View.VISIBLE);
		tblPolicyDetail.setVisibility(View.GONE);
		strPolicyDetailpolicyno = edpolicyno.getText().toString();

		if (strPolicyDetailpolicyno.equalsIgnoreCase("")) {
			// validationAlert();
			//PolicyDetailvalidationAlert();
			commonMethods.showMessageDialog(context, commonMethods.POLICY_NUMBER_ALERT);
		} else {

			if (commonMethods.isNetworkConnected(context)) {
				// startDownloadPolicyDetail();
				service_hits();
			} else {
				//intereneterror();
				commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
			}
		}
	}
	private void btnResetPolicyDetail() {
		edpolicyno.setText("");
		txterrordescpolicydetail.setVisibility(View.GONE);
		txtpolicydetailno.setText("");
		txtpolicydetailfirstname.setText("");
		txtpolicydetailstatus.setText("");
		txtpolicydetailpremiumup.setText("");
		txtpolicydetailpremiumamt.setText("");
		tblPolicyDetail.setVisibility(View.GONE);
	}
	private void viewMorePolicy(){

	}
	
	private void viewCustomerDetails()
	{


    }

    @Override
    protected void onDestroy() {
        if(mProgressDialog!=null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (taskPolicyDetail!= null) {
            taskPolicyDetail.cancel(true);
        }
		if (service!= null) {
			service.cancel(true);
		}

        super.onDestroy();
	}
}
