package sbilife.com.pointofsale_bancaagency.reports;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.Element_TextView_BaseAdapter;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods.UserDetailsValuesModel;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits.DownLoadData;

public class BancaReportsPPCActivity extends AppCompatActivity implements DownLoadData {

	private Context mContext;
	private CommonMethods mCommonMethods;

	private  final String METHOD_NAME_PPC = "getPPC_EasyAccess_mail";

	private DownloadFileAsyncPPC taskAsyncPPC;

	private final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;

	private EditText edittextPPCPolicyNumber;
	private Spinner spinnerPPCPeriod;

	private String strPPCErrorCOde = "";
    private String strCIFBDMUserId = "";
    private String strCIFBDMEmailId = "";
    private String strCIFBDMPassword = "";
    private String strCIFBDMMObileNo = "";
    private String strPPCPolicyNumber = "";
    private String strPeriod = "";

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.banca_reports_ppc);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

		new CommonMethods().setApplicationToolbarMenu(this,"PPC");

		mCommonMethods = new CommonMethods();
		mContext = this;
		edittextPPCPolicyNumber = findViewById(R.id.edittextPPCPolicyNumber);
		spinnerPPCPeriod = findViewById(R.id.spinnerPPCPeriod);
		Button buttonPPCOk = findViewById(R.id.buttonPPCOk);

		Calendar c = Calendar.getInstance();
		int CurrentYear = c.get(Calendar.YEAR);
		int CurrentMonth = c.get(Calendar.MONTH);
		getUserDetails();

		ArrayList<String> periodList = new ArrayList<>();
		periodList.add("Select Period");

		if (CurrentMonth > 2) {
			int nextYear = CurrentYear + 1;
			int prevYear = CurrentYear - 1;

			periodList.add(CurrentYear + "-"
					+ String.valueOf(nextYear).substring(2));
			periodList.add(prevYear + "-"
					+ String.valueOf(CurrentYear).substring(2));

		} else {

			int prevYear = CurrentYear - 1;
			int befPrevYear = CurrentYear - 2;

			periodList.add(prevYear + "-"
					+ String.valueOf(CurrentYear).substring(2));
			periodList.add(befPrevYear + "-"
					+ String.valueOf(prevYear).substring(2));

		}

		fillSpinnerValue(spinnerPPCPeriod, periodList);

		mProgressDialog = new ProgressDialog(this);
		//mCommonMethods.setApplicationMenu(this, "PPC");

		buttonPPCOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				strPPCPolicyNumber = edittextPPCPolicyNumber.getText()
						.toString();
				strPeriod = spinnerPPCPeriod.getSelectedItem().toString();
				getPPCDetails();
			}
		});
	}

	private void getPPCDetails() {
		mCommonMethods.hideKeyboard(edittextPPCPolicyNumber, mContext);
		taskAsyncPPC = new DownloadFileAsyncPPC();

		if (strPPCPolicyNumber.equalsIgnoreCase("")) {
			mCommonMethods.showMessageDialog(mContext,
					"Please Enter Policy No..");
		} else if (strPeriod.equalsIgnoreCase("Select Period")) {
			mCommonMethods
					.showMessageDialog(mContext, "Please Select Period..");
		} else {

			if (mCommonMethods.isNetworkConnected(mContext)) {
				service_hits(strPPCPolicyNumber+","+strPeriod);
			} else {
				mCommonMethods.showMessageDialog(mContext,
						mCommonMethods.NO_INTERNET_MESSAGE);
			}
		}
	}

	private void fillSpinnerValue(Spinner spinner, List<String> value_list) {

		Element_TextView_BaseAdapter retd_adapter = new Element_TextView_BaseAdapter(
				BancaReportsPPCActivity.this, value_list);
		spinner.setAdapter(retd_adapter);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (taskAsyncPPC != null) {
				taskAsyncPPC.cancel(true);
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

	public void getUserDetails() {
		UserDetailsValuesModel userDetailsValuesModel = mCommonMethods
				.setUserDetails(mContext);
		strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
		strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
		strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
		strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
		mCommonMethods.printLog("User Details", "strCIFBDMUserId:"
				+ strCIFBDMUserId + " strCIFBDMEmailId:" + strCIFBDMEmailId
				+ " strCIFBDMPassword:" + strCIFBDMPassword
				+ " strCIFBDMMObileNo:" + strCIFBDMMObileNo);
	}

	class DownloadFileAsyncPPC extends AsyncTask<String, String, String> {

		private volatile boolean running = true;

		/*
		 * private String proposalTrackerStatus = "",proposalTrackerReason = "",
		 * proposalTrackerNO = "";
		 */

		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}

		@Override
		protected String doInBackground(String... aurl) {
			try {
				running = true;
				SoapObject request;
				String NAMESPACE = "http://tempuri.org/";
				request = new SoapObject(NAMESPACE, METHOD_NAME_PPC);
				request.addProperty("strPolicyNumber",
						strPPCPolicyNumber.trim());
				request.addProperty("strFinYear",  strPeriod );//"2015-16"

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);

				mCommonMethods.TLSv12Enable();

				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);

				String URl = ServiceURL.SERVICE_URL;
				HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
				try {

                    String SOAP_ACTION_PPC = "http://tempuri.org/getPPC_EasyAccess_mail";
                    androidHttpTranport.call(SOAP_ACTION_PPC, envelope);
					Object response = envelope.getResponse();
					System.out.println("response:" + response.toString());
					if (!response.toString().contentEquals("anyType{}")) {

						SoapPrimitive sa;
						try {
							sa = (SoapPrimitive) envelope.getResponse();

                            String inputpolicylist = sa.toString();

							if (sa.toString().equalsIgnoreCase("1")) {
								if (inputpolicylist != null) {
									strPPCErrorCOde = inputpolicylist;
									strPPCErrorCOde = "success";

								}
							} else {
								strPPCErrorCOde = "0";

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
					} else {
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

			if (mProgressDialog.isShowing()) {
				closeProgressDialog();
			}
			if (running) {

				if (strPPCErrorCOde != null) {
					// txterrordescpolicydetail.setText("");

					if (strPPCErrorCOde.equalsIgnoreCase("success")) {

						mCommonMethods
								.showMessageDialog(mContext,
										"PDF Sent Successfully to Your Registered Email-Id");

						edittextPPCPolicyNumber.setText("");
						spinnerPPCPeriod.setSelection(0);
					} else {
						mCommonMethods
								.showMessageDialog(mContext,
										"Data is Not Available Please check Your Input");
					}
				} else {
					mCommonMethods.showMessageDialog(mContext,
							"Server Not Responding,Try again..");
					// setProposalTrackerStatusToBlank();
				}
			} else {
				mCommonMethods.showMessageDialog(mContext,
						"Server Not Responding,Try again..");
				// setProposalTrackerStatusToBlank();
			}
		}
	}

	private void service_hits(String input) {

            ServiceHits service = new ServiceHits(mContext,METHOD_NAME_PPC, input,
					strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMMObileNo,
					strCIFBDMPassword, this);
			service.execute();
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
			case DIALOG_DOWNLOAD_PROGRESS:
				mProgressDialog = new ProgressDialog(this,
						ProgressDialog.THEME_HOLO_LIGHT);
				String Message = "Loading Please wait...";
				mProgressDialog.setMessage(Html
						.fromHtml("<font color='#00a1e3'><b>" + Message
								+ "<b></font>"));
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgressDialog.setCancelable(true);

				mProgressDialog.setButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {

								if (taskAsyncPPC != null) {
									taskAsyncPPC.cancel(true);
								}
									if (mProgressDialog.isShowing()) {
										mProgressDialog.dismiss();
									}
							}
						});

				mProgressDialog.setMax(100);
				mProgressDialog.show();
				return mProgressDialog;

			default:
				return null;
		}
	}

	private void startDownloadProposalTracker() {
		taskAsyncPPC =  new DownloadFileAsyncPPC();
		taskAsyncPPC.execute("demo");
	}

	@SuppressWarnings("deprecation")
    private void showProgressDialog() {
		showDialog(DIALOG_DOWNLOAD_PROGRESS);
	}

	@SuppressWarnings("deprecation")
    private void closeProgressDialog() {
		dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
	}

	@Override
	public void downLoadData() {
		taskAsyncPPC = new DownloadFileAsyncPPC();
		startDownloadProposalTracker();
	}
}
