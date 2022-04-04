package sbilife.com.pointofsale_bancaagency.reports.agency;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods.UserDetailsValuesModel;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits.DownLoadData;

public class AgencyReportsPerformanceReportActivity extends AppCompatActivity implements
		DownLoadData {

	private Context mContext;
	private CommonMethods mCommonMethods;
	//private DatabaseHelper dbhelper;
	private ProgressDialog mProgressDialog;

	private final int DIALOG_DOWNLOAD_PROGRESS = 0;

    private  final String METHOD_NAME_PERFORMANCE_REPORT  = "getPerformanceReport";
	
	private DownloadFileAsyncPerformanceReport taskPerformanceReport;


	//private LinearLayout lnPerformanceReports;

    private String strCIFBDMUserId = "";
    private String strCIFBDMEmailId = "";
    private String strCIFBDMPassword = "";
    private String strCIFBDMMObileNo = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.agency_reports_performance_report);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		
		mProgressDialog = new ProgressDialog(this);
		
		initialise();
		new CommonMethods().setApplicationToolbarMenu(this,"Performance Report"); ;
	}
	
	private void getUserDetails() {
		UserDetailsValuesModel userDetailsValuesModel = mCommonMethods.setUserDetails(mContext);
		strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
		strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
		strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
		strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
		mCommonMethods.printLog("User Details", "strCIFBDMUserId:" + strCIFBDMUserId
				+ " strCIFBDMEmailId:" + strCIFBDMEmailId
				+ " strCIFBDMPassword:" + strCIFBDMPassword
				+ " strCIFBDMMObileNo:" + strCIFBDMMObileNo);
	}

	private void initialise() {

		mContext = this;
		mCommonMethods = new CommonMethods();

		getUserDetails();
		//dbhelper = new DatabaseHelper(mContext);

		/*lnPerformanceReports = (LinearLayout) findViewById(R.id.lnPerformanceReports);
		lnPerformanceReports.setVisibility(View.GONE);*/
		//blPerformanceReportsBDMCIF = (TableLayout) findViewById(R.id.tblPerformanceReportsBDMCIF);

		taskPerformanceReport = new DownloadFileAsyncPerformanceReport();

		if (mCommonMethods.isNetworkConnected(mContext)) {
			service_hits();
		} else
			mCommonMethods.showMessageDialog(mContext, mCommonMethods.NO_INTERNET_MESSAGE);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			if(taskPerformanceReport!=null)
			{
				taskPerformanceReport.cancel(true);
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
	@Override
	@Deprecated
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
							if(taskPerformanceReport!=null)
							taskPerformanceReport.cancel(true);

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

		default:
			return null;
		}
	}

	private void service_hits() {

		ServiceHits service = new ServiceHits(mContext, METHOD_NAME_PERFORMANCE_REPORT,"",
				strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMMObileNo,
				strCIFBDMPassword, this);
		service.execute();
	}

	@Override
	public void downLoadData() {
		// TODO Auto-generated method stub
		startDownloadPerformanceReport();
	}

	private void startDownloadPerformanceReport() {
		taskPerformanceReport.execute("demo");
	}

	class DownloadFileAsyncPerformanceReport extends
			AsyncTask<String, String, String> {

		private volatile boolean running = true;
		private String strPerformanceReportErrorCOde = "";

		private String employeeName = "", NBPMTD = "", NBPYTD = "",
				NBPIssuedMTD = "", NBPIssuedYTD = "", NOPMTD = "", NOPYTD = "",
				ThirteenMonPersiPerMTD = "", ThirteenMonPersiPerYTD = "",
				NoProposalsPendingMTD = "", NoProposalsPendingYTD = "",
				PendingPremiumMTD = "", PendingPremiumYTD = "";

		private TableLayout tblPerformanceReportsIABusinessReport;

		// IABusinessReport

		private TextView txtPerformanceReportsIABusinessReportEmployeeName,
				txtIABusinessReportNBPMTD, txtIABusinessReportNBPYTD,
				txtIABusinessReportNBPIssuedMTD,
				txtIABusinessReportNBPIssuedYTD, txtIABusinessReportNOPMTD,
				txtIABusinessReportNOPYTD,
				txtIABusinessReportThirteenMonPersiPerMTD,
				txtIABusinessReportThirteenMonPersiPerYTD,
				txtIABusinessReportNoProposalsPendingMTD,
				txtIABusinessReportNoProposalsPendingYTD,
				txtIABusinessReportPendingPremiumMTD,
				txtIABusinessReportPendingPremiumYTD;
		private LinearLayout lnPerformanceReports;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog();

			// CIFServicingReport
			txtPerformanceReportsIABusinessReportEmployeeName = findViewById(R.id.txtPerformanceReportsIABusinessReportEmployeeName);
			txtIABusinessReportNBPMTD = findViewById(R.id.txtIABusinessReportNBPMTD);
			txtIABusinessReportNBPYTD = findViewById(R.id.txtIABusinessReportNBPYTD);
			txtIABusinessReportNBPIssuedMTD = findViewById(R.id.txtIABusinessReportNBPIssuedMTD);
			txtIABusinessReportNBPIssuedYTD = findViewById(R.id.txtIABusinessReportNBPIssuedYTD);
			txtIABusinessReportNOPMTD = findViewById(R.id.txtIABusinessReportNOPMTD);
			txtIABusinessReportNOPYTD = findViewById(R.id.txtIABusinessReportNOPYTD);
			txtIABusinessReportThirteenMonPersiPerMTD = findViewById(R.id.txtIABusinessReportThirteenMonPersiPerMTD);
			txtIABusinessReportThirteenMonPersiPerYTD = findViewById(R.id.txtIABusinessReportThirteenMonPersiPerYTD);

			txtIABusinessReportNoProposalsPendingMTD = findViewById(R.id.txtIABusinessReportNoProposalsPendingMTD);
			txtIABusinessReportNoProposalsPendingYTD = findViewById(R.id.txtIABusinessReportNoProposalsPendingYTD);
			txtIABusinessReportPendingPremiumMTD = findViewById(R.id.txtIABusinessReportPendingPremiumMTD);
			txtIABusinessReportPendingPremiumYTD = findViewById(R.id.txtIABusinessReportPendingPremiumYTD);

			tblPerformanceReportsIABusinessReport = findViewById(R.id.tblPerformanceReportsIABusinessReport);
			
			lnPerformanceReports = findViewById(R.id.lnPerformanceReports);
		}

		@Override
		protected String doInBackground(String... aurl) {
			try {
				running = true;
				SoapObject request;

				String NAMESPACE = "http://tempuri.org/";
				request = new SoapObject(NAMESPACE, METHOD_NAME_PERFORMANCE_REPORT);

				request.addProperty("strEmailId", strCIFBDMEmailId);
				request.addProperty("strMobileNo", strCIFBDMMObileNo);
				request.addProperty("strAuthKey", strCIFBDMPassword.trim());
				request.addProperty("strCode", strCIFBDMUserId);
				request.addProperty("strType", "IA Business Report");

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);
				System.out.println("request performance:" + request.toString());
				// allowAllSSL();

				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);

				String URl = ServiceURL.SERVICE_URL;
				HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
				try {

                    String SOAP_ACTION_PERFORMANCE_REPORT = "http://tempuri.org/getPerformanceReport";
                    androidHttpTranport.call(SOAP_ACTION_PERFORMANCE_REPORT, envelope);
					Object response = envelope.getResponse();
					System.out.println("response:" + response.toString());
					if (!response.toString().contentEquals("<PolicyDetails />")) {

						SoapPrimitive sa;
						try {
							sa = (SoapPrimitive) envelope.getResponse();

                            String inputpolicylist = sa.toString();

							if (!sa.toString().equalsIgnoreCase("1")) {
								ParseXML prsObj = new ParseXML();

								// <ReqDtls><Table1><Status>Cancelled</Status><ProposalNo>53NA062275</ProposalNo>
								// <Reason>Sent for
								// Cancel/Refund</Reason></Table1></ReqDtls>

								inputpolicylist = prsObj.parseXmlTag(
                                        inputpolicylist, "PolicyDetails");
								System.out.println("inputpolicylist:"
										+ inputpolicylist);

								if (inputpolicylist != null) {
									strPerformanceReportErrorCOde = inputpolicylist;

									inputpolicylist = prsObj.parseXmlTag(
                                            inputpolicylist, "Table");
									strPerformanceReportErrorCOde = inputpolicylist;
									System.out
											.println("strPIWCTrackerErrorCOde:"
													+ strPerformanceReportErrorCOde);

									if (strPerformanceReportErrorCOde != null) {

										// CIF Servicing Report
										// CIF Business Report
										// BDM Servicing Individual Report
										// BDM Credit Life Report
										// BDM Business Report

										/*
										 * <?xml version="1.0"
										 * encoding="UTF-8"?> <PolicyDetails>
										 * <Table> <IACODE>990144910</IACODE>
										 * <CASH_MDT>0</CASH_MDT>
										 * <CASH_YTD>247700</CASH_YTD>
										 * <ISSUE_MDT>0</ISSUE_MDT>
										 * <ISSUE_YTD>271738</ISSUE_YTD>
										 * <NOP_MDT>0</NOP_MDT>
										 * <NOP_YTD>9</NOP_YTD>
										 * <PERSISTENCY_AMT>69</PERSISTENCY_AMT>
										 * </Table> </PolicyDetails>
										 */

										employeeName = new ParseXML()
												.parseXmlTag(inputpolicylist,"");
										NBPMTD = new ParseXML().parseXmlTag(
                                                inputpolicylist, "CASH_MDT");
										NBPYTD = new ParseXML().parseXmlTag(
                                                inputpolicylist, "CASH_YTD");
										NBPIssuedMTD = new ParseXML()
												.parseXmlTag(inputpolicylist,"ISSUE_MDT");
										NBPIssuedYTD = new ParseXML()
												.parseXmlTag(inputpolicylist,"ISSUE_YTD");
										NOPMTD = new ParseXML().parseXmlTag(
                                                inputpolicylist, "NOP_MDT");
										NOPYTD = new ParseXML().parseXmlTag(
                                                inputpolicylist, "NOP_YTD");
										ThirteenMonPersiPerMTD = new ParseXML()
												.parseXmlTag(inputpolicylist,"");
										ThirteenMonPersiPerYTD = new ParseXML()
												.parseXmlTag(inputpolicylist,"");
										NoProposalsPendingMTD = new ParseXML()
												.parseXmlTag(inputpolicylist,"");
										NoProposalsPendingYTD = new ParseXML()
												.parseXmlTag(inputpolicylist, "");
										PendingPremiumMTD = new ParseXML()
												.parseXmlTag(inputpolicylist,"");
										PendingPremiumYTD = new ParseXML()
												.parseXmlTag(inputpolicylist,"");

										strPerformanceReportErrorCOde = "success";

									}

								} else {
									strPerformanceReportErrorCOde = "0";
								}
							} else {
								strPerformanceReportErrorCOde = "1";
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

				if (strPerformanceReportErrorCOde != null) {

					if (strPerformanceReportErrorCOde
							.equalsIgnoreCase("success")) {
						lnPerformanceReports.setVisibility(View.VISIBLE);

						tblPerformanceReportsIABusinessReport.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
						tblPerformanceReportsIABusinessReport.requestLayout();

						txtPerformanceReportsIABusinessReportEmployeeName
								.setText(employeeName);
						txtIABusinessReportNBPMTD.setText(NBPMTD);
						txtIABusinessReportNBPYTD.setText(NBPYTD);
						txtIABusinessReportNBPIssuedMTD.setText(NBPIssuedMTD);
						txtIABusinessReportNBPIssuedYTD.setText(NBPIssuedYTD);
						txtIABusinessReportNOPMTD.setText(NOPMTD);
						txtIABusinessReportNOPYTD.setText(NOPYTD);
						txtIABusinessReportThirteenMonPersiPerMTD
								.setText(ThirteenMonPersiPerMTD);
						txtIABusinessReportThirteenMonPersiPerYTD
								.setText(ThirteenMonPersiPerYTD);

						txtIABusinessReportNoProposalsPendingMTD
								.setText(NoProposalsPendingMTD);
						txtIABusinessReportNoProposalsPendingYTD
								.setText(NoProposalsPendingYTD);
						txtIABusinessReportPendingPremiumMTD
								.setText(PendingPremiumMTD);
						txtIABusinessReportPendingPremiumYTD
								.setText(PendingPremiumYTD);

						tblPerformanceReportsIABusinessReport.getParent()
								.requestChildFocus(
										tblPerformanceReportsIABusinessReport,
										tblPerformanceReportsIABusinessReport);

					} else {
						hideIABusinessReport();
						Toast.makeText(getApplicationContext(),
								"No record found", Toast.LENGTH_LONG).show();
					}
				} else {
					hideIABusinessReport();
					Toast.makeText(getApplicationContext(),
							"You are not authorised user", Toast.LENGTH_LONG)
							.show();
				}
			} else {
				// servererror();
				hideIABusinessReport();
				Toast.makeText(getApplicationContext(), "No record found",
						Toast.LENGTH_LONG).show();
			}

		}

		void hideIABusinessReport() {
			tblPerformanceReportsIABusinessReport.getLayoutParams().height = 0;
			tblPerformanceReportsIABusinessReport.requestLayout();

			txtPerformanceReportsIABusinessReportEmployeeName.setText("");
			txtIABusinessReportNBPMTD.setText("0");
			txtIABusinessReportNBPYTD.setText("0");
			txtIABusinessReportNBPIssuedMTD.setText("0");
			txtIABusinessReportNBPIssuedYTD.setText("0");
			txtIABusinessReportNOPMTD.setText("0");
			txtIABusinessReportNOPYTD.setText("0");
			txtIABusinessReportThirteenMonPersiPerMTD.setText("0");
			txtIABusinessReportThirteenMonPersiPerYTD.setText("0");

			txtIABusinessReportNoProposalsPendingMTD.setText("0");
			txtIABusinessReportNoProposalsPendingYTD.setText("0");
			txtIABusinessReportPendingPremiumMTD.setText("0");
			txtIABusinessReportPendingPremiumYTD.setText("0");
		}

	}
	
	@SuppressWarnings("deprecation")
    private void showProgressDialog(){
		showDialog(DIALOG_DOWNLOAD_PROGRESS);
	}
	@SuppressWarnings("deprecation")
    private void dismissProgressDialog(){
		dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
	}

}
