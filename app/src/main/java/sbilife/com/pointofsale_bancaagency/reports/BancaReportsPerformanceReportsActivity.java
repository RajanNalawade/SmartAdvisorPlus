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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits.DownLoadData;

public class BancaReportsPerformanceReportsActivity extends AppCompatActivity implements DownLoadData {

	private Context mContext;
	private CommonMethods mCommonMethods;
	//private DatabaseHelper dbhelper;

	private final int DIALOG_DOWNLOAD_PROGRESS = 0;
	
	private ProgressDialog mProgressDialog;
	
	private Spinner spinnerPerformanceReport;
	private Button btnOkPerformanceReport, btnResetPerformanceReport;


    private  final String METHOD_NAME_PERFORMANCE_REPORT  = "getPerformanceReport";
	
	private TableLayout tblPerformanceReportsCIFBusinessReport,
			tblPerformanceReportsCIFServicingReport,
			tblPerformanceReportsBDMBusinessReport,
			tblPerformanceReportsBDMCreditLifeReport,
			tblPerformanceReportsBDMServicingIndividualReports;

	private DownloadFileAsyncPerformanceReport taskPerformanceReport;

	private String strCIFBDMUserId = "", strCIFBDMEmailId = "",
			strCIFBDMPassword = "", strCIFBDMMObileNo = "", performanceReportType = "";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.banca_reports_performance_reports);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		mProgressDialog = new ProgressDialog(this);
		
		initialise();
		mCommonMethods.setApplicationToolbarMenu(this,"Performance Report"); ;
		setSpinner();
		
		getUserDetails();

		spinnerPerformanceReport
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int pos, long id) {
						performanceReportType = spinnerPerformanceReport
								.getSelectedItem().toString();
						System.out.println("performanceReportType:"
								+ performanceReportType);

						hideCIFBusinessReport();
						hideCIFServicingReport();
						hideBDMBusinessReport();
						hideBDMCreditLifeReport();
						hideBDMServicingIndividualReports();
					}

					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		btnOkPerformanceReport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				taskPerformanceReport = new DownloadFileAsyncPerformanceReport();
				
				if (mCommonMethods.isNetworkConnected(mContext)) {
					service_hits();
				}else{
					mCommonMethods.showMessageDialog(mContext, "Internet Connection Not Present,Try again..");
				}
			}
		});
		
		btnResetPerformanceReport.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				spinnerPerformanceReport.setSelection(0);
			}
		});

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
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
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
							{
								taskPerformanceReport.cancel(true);
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

		default:
			return null;
		}
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
	private void service_hits() {

		
		ServiceHits service = new ServiceHits(mContext,
				METHOD_NAME_PERFORMANCE_REPORT,  performanceReportType,  strCIFBDMUserId,
				 strCIFBDMEmailId,  strCIFBDMMObileNo,
				 strCIFBDMPassword, this);
		service.execute();
	}

	private void initialise() {

		mContext = this;

		mCommonMethods = new CommonMethods();

		//dbhelper = new DatabaseHelper(mContext);
		
		mProgressDialog = new ProgressDialog(this);

		spinnerPerformanceReport = findViewById(R.id.spinnerPerformanceReport);

		tblPerformanceReportsCIFBusinessReport = findViewById(R.id.tblPerformanceReportsCIFBusinessReport);
		tblPerformanceReportsCIFServicingReport = findViewById(R.id.tblPerformanceReportsCIFServicingReport);
		tblPerformanceReportsBDMBusinessReport = findViewById(R.id.tblPerformanceReportsBDMBusinessReport);
		tblPerformanceReportsBDMCreditLifeReport = findViewById(R.id.tblPerformanceReportsBDMCreditLifeReport);
		tblPerformanceReportsBDMServicingIndividualReports = findViewById(R.id.tblPerformanceReportsBDMServicingIndividualReports);

		btnResetPerformanceReport = findViewById(R.id.btnResetPerformanceReport);
		btnOkPerformanceReport = findViewById(R.id.btnOkPerformanceReport);
	}

	private void setSpinner() {
		String strUType = mCommonMethods.GetUserType(mContext);

		if (strUType.contentEquals("CIF")) {
			String[] performanceReportCIFArray = getResources().getStringArray(
					R.array.performanceReportCIFArray);

			ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
					android.R.layout.simple_spinner_dropdown_item,
					performanceReportCIFArray);
			spinnerPerformanceReport.setAdapter(dataAdapter);
		} else if (strUType.contentEquals("BDM")) {
			String[] performanceReportBDMArray = getResources().getStringArray(
					R.array.performanceReportBDMArray);

			ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
					android.R.layout.simple_spinner_dropdown_item,
					performanceReportBDMArray);
			spinnerPerformanceReport.setAdapter(dataAdapter);
		}
	}

	class DownloadFileAsyncPerformanceReport extends
			AsyncTask<String, String, String> {

		private volatile boolean running = true;
		private String strPerformanceReportErrorCOde = "";

		private CIFServicingReportValuesModel cIFServicingReportValuesModel;
		private CIFBusinessReportValuesModel cIfBusinessReportValuesModel;
		private BDMBusinessReportValuesModel bdmBusinessReportValuesModel;
		private BDMCreditLifeReportValuesModel bDMCreditLifeReportValuesModel;
		private BDMServicingIndividualReportsValuesModel bDMServicingIndividualReportsValuesModel;

		// CIFServicingReport
		private TextView txtPerformanceReportsCIFServicingReportEmployeeName,
				CIFtextoneSevenDaysEFTCount,
				CIFtextoneSevenDaysPendingCasesCount,
				CIFtexteightFifteenDaysEFTCount,
				CIFtexteightFifteenDaysPendingCasesCount,
				CIFtextfifteenTwentyDaysEFTCount,
				CIFtextfifteenTwentyDaysPendingCasesCount,
				CIFtextgreaterThanTwentyOneDaysEFTCount,
				CIFtextgreaterThanTwentyOneDaysPendingCasesCount;

		// CIFBusinessReport
		private TextView txtPerformanceReportsCIFBusinessReportRatedPremiumMTD,
				txtPerformanceReportsCIFBusinessReportRatedPremiumQTD,
				txtPerformanceReportsCIFBusinessReportRatedPremiumYTD,
				txtPerformanceReportsCIFBusinessReportCLNOPMTD,
				txtPerformanceReportsCIFBusinessReportCLNOPQTD,
				txtPerformanceReportsCIFBusinessReportCLNOPYTD,
				txtPerformanceReportsCIFBusinessReportCLNBPMTD,
				txtPerformanceReportsCIFBusinessReportCLNBPtQTD,
				txtPerformanceReportsCIFBusinessReportCLNBPYTD,
				txtPerformanceReportsCIFBusinessReportThirteenMonthPercenAmtMTD,
				txtPerformanceReportsCIFBusinessReportThirteenMonthPercenAmtQTD,
				txtPerformanceReportsCIFBusinessReportThirteenMonthPercenAmtYTD,
				txtPerformanceReportsCIFBusinessReportThirteenMonthPercenCountMTD,
				txtPerformanceReportsCIFBusinessReportThirteenMonthPercenCountQTD,
				txtPerformanceReportsCIFBusinessReportThirteenMonthPercenCountYTD,
				txtPerformanceReportsCIFBusinessReportEmployeeName;

		// BDMBusinessReport
		private TextView txtPerformanceReportsBDMBusinessReportTotalBranches,
				txtPerformanceReportsBDMBusinessReportCIFName,
				txtPerformanceReportsBDMBusinessReportNBBudgetMTD,
				txtPerformanceReportsBDMBusinessReportNBBudgetQTD,
				txtPerformanceReportsBDMBusinessReportNBBudgetYTD,
				txtPerformanceReportsBDMBusinessReportPercentageAchievementMTD,
				txtPerformanceReportsBDMBusinessReportPercentageAchievementQTD,
				txtPerformanceReportsBDMBusinessReportPercentageAchievementYTD,
				txtPerformanceReportsBDMBusinessReportRatedAPremiumtMTD,
				txtPerformanceReportsBDMBusinessReportRatedAPremiumQTD,
				txtPerformanceReportsBDMBusinessReportRatedAPremiumYTD,
				txtPerformanceReportsBDMBusinessReportRenewalBudgettMTD,
				txtPerformanceReportsBDMBusinessReportRenewalBudgetQTD,
				txtPerformanceReportsBDMBusinessReportRenewalBudgetYTD,
				txtPerformanceReportsBDMBusinessReportRenewalAchievementMTD,
				txtPerformanceReportsBDMBusinessReportRenewalAchievementQTD,
				txtPerformanceReportsBDMBusinessReportRenewalAchievementYTD,
				txtPerformanceReportsBDMBusinessReportConnectLifeNopsMTD,
				txtPerformanceReportsBDMBusinessReportConnectLifeNopsQTD,
				txtPerformanceReportsBDMBusinessReportConnectLifeNopsYTD,
				txtPerformanceReportsBDMBusinessReportPercentageClNBpMTD,
				txtPerformanceReportsBDMBusinessReportPercentageClNBpQTD,
				txtPerformanceReportsBDMBusinessReportPercentageClNBpYTD,
				txtPerformanceReportsBDMBusinessReportPersistencyAmountPercentageMTD,
				txtPerformanceReportsBDMBusinessReportPersistencyAmountPercentageQTD,
				txtPerformanceReportsBDMBusinessReportPersistencyAmountPercentageYTD,
				txtPerformanceReportsBDMBusinessReportPersistencyCountPercentageMTD,
				txtPerformanceReportsBDMBusinessReportPersistencyCountPercentageQTD,
				txtPerformanceReportsBDMBusinessReportPersistencyCountPercentageYTD,
				txtPerformanceReportsBDMBusinessReportNumberOfMTD,
				txtPerformanceReportsBDMBusinessReportNumberOfQTD,
				txtPerformanceReportsBDMBusinessReportNumberOfYTD,
				txtPerformanceReportsBDMBusinessReportMDRTStatusMTD,
				txtPerformanceReportsBDMBusinessReportMDRTStatusQTD,
				txtPerformanceReportsBDMBusinessReportMDRTStatusYTD;

		// BDMCreditLifeReport
		private TextView textBDMCreditLifeReportRRYTDBusinessNOP,
				textBDMCreditLifeReportRRYTDBusinessPremiumAmt,
				textBDMCreditLifeReportHomeLoanPenetrationNOP,
				textBDMCreditLifeReportHomeLoanPenetrationPremiumAmt,
				textBDMCreditLifeReportPotentialLoansNOP,
				textBDMCreditLifeReportPotentialLoansPremiumAmt,
				textBDMCreditLifeReportCoveredIncShieldNOP,
				textBDMCreditLifeReportCoveredIncShieldPremiumAmt,
				textBDMCreditLifeReportPercentagePenetrationNOP,
				textBDMCreditLifeReportPercentagePenetrationPremiumAmt;

		// BDMServicingIndividualReports
		private TextView textoneSevenDaysEFTCount,
				textoneSevenDaysPendingCasesCount,
				texteightFifteenDaysEFTCount,
				texteightFifteenDaysPendingCasesCount,
				textfifteenTwentyDaysEFTCount,
				textfifteenTwentyDaysPendingCasesCount,
				textgreaterThanTwentyOneDaysEFTCount,
				textgreaterThanTwentyOneDaysPendingCasesCount;

		@Override
		protected void onPreExecute() {
			showProgressDialog();

			cIFServicingReportValuesModel = new CIFServicingReportValuesModel();
			cIfBusinessReportValuesModel = new CIFBusinessReportValuesModel();
			bdmBusinessReportValuesModel = new BDMBusinessReportValuesModel();
			bDMCreditLifeReportValuesModel = new BDMCreditLifeReportValuesModel();
			bDMServicingIndividualReportsValuesModel = new BDMServicingIndividualReportsValuesModel();

			String strUType = mCommonMethods.GetUserType(mContext);

			if (strUType.contentEquals("CIF")) {

				// CIFServicingReport
				txtPerformanceReportsCIFServicingReportEmployeeName = findViewById(R.id.txtPerformanceReportsCIFServicingReportEmployeeName);
				CIFtextoneSevenDaysEFTCount = findViewById(R.id.CIFtextoneSevenDaysEFTCount);
				CIFtextoneSevenDaysPendingCasesCount = findViewById(R.id.CIFtextoneSevenDaysPendingCasesCount);
				CIFtexteightFifteenDaysEFTCount = findViewById(R.id.CIFtexteightFifteenDaysEFTCount);
				CIFtexteightFifteenDaysPendingCasesCount = findViewById(R.id.CIFtexteightFifteenDaysPendingCasesCount);
				CIFtextfifteenTwentyDaysEFTCount = findViewById(R.id.CIFtextfifteenTwentyDaysEFTCount);
				CIFtextfifteenTwentyDaysPendingCasesCount = findViewById(R.id.CIFtextfifteenTwentyDaysPendingCasesCount);
				CIFtextgreaterThanTwentyOneDaysEFTCount = findViewById(R.id.CIFtextgreaterThanTwentyOneDaysEFTCount);
				CIFtextgreaterThanTwentyOneDaysPendingCasesCount = findViewById(R.id.CIFtextgreaterThanTwentyOneDaysPendingCasesCount);

				// CIFBusinessReportValuesModel
				txtPerformanceReportsCIFBusinessReportEmployeeName = findViewById(R.id.txtPerformanceReportsCIFBusinessReportEmployeeName);
				txtPerformanceReportsCIFBusinessReportRatedPremiumMTD = findViewById(R.id.txtPerformanceReportsCIFBusinessReportRatedPremiumMTD);
				txtPerformanceReportsCIFBusinessReportRatedPremiumQTD = findViewById(R.id.txtPerformanceReportsCIFBusinessReportRatedPremiumQTD);
				txtPerformanceReportsCIFBusinessReportRatedPremiumYTD = findViewById(R.id.txtPerformanceReportsCIFBusinessReportRatedPremiumYTD);
				txtPerformanceReportsCIFBusinessReportCLNOPMTD = findViewById(R.id.txtPerformanceReportsCIFBusinessReportCLNOPMTD);
				txtPerformanceReportsCIFBusinessReportCLNOPQTD = findViewById(R.id.txtPerformanceReportsCIFBusinessReportCLNOPQTD);
				txtPerformanceReportsCIFBusinessReportCLNOPYTD = findViewById(R.id.txtPerformanceReportsCIFBusinessReportCLNOPYTD);
				txtPerformanceReportsCIFBusinessReportCLNBPMTD = findViewById(R.id.txtPerformanceReportsCIFBusinessReportCLNBPMTD);
				txtPerformanceReportsCIFBusinessReportCLNBPtQTD = findViewById(R.id.txtPerformanceReportsCIFBusinessReportCLNBPtQTD);
				txtPerformanceReportsCIFBusinessReportCLNBPYTD = findViewById(R.id.txtPerformanceReportsCIFBusinessReportCLNBPYTD);
				txtPerformanceReportsCIFBusinessReportThirteenMonthPercenAmtMTD = findViewById(R.id.txtPerformanceReportsCIFBusinessReportThirteenMonthPercenAmtMTD);
				txtPerformanceReportsCIFBusinessReportThirteenMonthPercenAmtQTD = findViewById(R.id.txtPerformanceReportsCIFBusinessReportThirteenMonthPercenAmtQTD);
				txtPerformanceReportsCIFBusinessReportThirteenMonthPercenAmtYTD = findViewById(R.id.txtPerformanceReportsCIFBusinessReportThirteenMonthPercenAmtYTD);
				txtPerformanceReportsCIFBusinessReportThirteenMonthPercenCountMTD = findViewById(R.id.txtPerformanceReportsCIFBusinessReportThirteenMonthPercenCountMTD);
				txtPerformanceReportsCIFBusinessReportThirteenMonthPercenCountQTD = findViewById(R.id.txtPerformanceReportsCIFBusinessReportThirteenMonthPercenCountQTD);
				txtPerformanceReportsCIFBusinessReportThirteenMonthPercenCountYTD = findViewById(R.id.txtPerformanceReportsCIFBusinessReportThirteenMonthPercenCountYTD);

			} else if (strUType.contentEquals("BDM")) {
				// BDMBusinessReport
				txtPerformanceReportsBDMBusinessReportTotalBranches = findViewById(R.id.txtPerformanceReportsBDMBusinessReportTotalBranches);
				txtPerformanceReportsBDMBusinessReportCIFName = findViewById(R.id.txtPerformanceReportsBDMBusinessReportCIFName);
				txtPerformanceReportsBDMBusinessReportNBBudgetMTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportNBBudgetMTD);
				txtPerformanceReportsBDMBusinessReportNBBudgetQTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportNBBudgetQTD);
				txtPerformanceReportsBDMBusinessReportNBBudgetYTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportNBBudgetYTD);
				txtPerformanceReportsBDMBusinessReportPercentageAchievementMTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportPercentageAchievementMTD);
				txtPerformanceReportsBDMBusinessReportPercentageAchievementQTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportPercentageAchievementQTD);
				txtPerformanceReportsBDMBusinessReportPercentageAchievementYTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportPercentageAchievementYTD);
				txtPerformanceReportsBDMBusinessReportRatedAPremiumtMTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportRatedAPremiumtMTD);
				txtPerformanceReportsBDMBusinessReportRatedAPremiumQTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportRatedAPremiumQTD);
				txtPerformanceReportsBDMBusinessReportRatedAPremiumYTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportRatedAPremiumYTD);
				txtPerformanceReportsBDMBusinessReportRenewalBudgettMTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportRenewalBudgettMTD);
				txtPerformanceReportsBDMBusinessReportRenewalBudgetQTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportRenewalBudgetQTD);
				txtPerformanceReportsBDMBusinessReportRenewalBudgetYTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportRenewalBudgetYTD);
				txtPerformanceReportsBDMBusinessReportRenewalAchievementMTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportRenewalAchievementMTD);
				txtPerformanceReportsBDMBusinessReportRenewalAchievementQTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportRenewalAchievementQTD);
				txtPerformanceReportsBDMBusinessReportRenewalAchievementYTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportRenewalAchievementYTD);
				txtPerformanceReportsBDMBusinessReportConnectLifeNopsMTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportConnectLifeNopsMTD);
				txtPerformanceReportsBDMBusinessReportConnectLifeNopsQTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportConnectLifeNopsQTD);
				txtPerformanceReportsBDMBusinessReportConnectLifeNopsYTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportConnectLifeNopsYTD);
				txtPerformanceReportsBDMBusinessReportPercentageClNBpMTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportPercentageClNBpMTD);
				txtPerformanceReportsBDMBusinessReportPercentageClNBpQTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportPercentageClNBpQTD);
				txtPerformanceReportsBDMBusinessReportPercentageClNBpYTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportPercentageClNBpYTD);
				txtPerformanceReportsBDMBusinessReportPersistencyAmountPercentageMTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportPersistencyAmountPercentageMTD);
				txtPerformanceReportsBDMBusinessReportPersistencyAmountPercentageQTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportPersistencyAmountPercentageQTD);
				txtPerformanceReportsBDMBusinessReportPersistencyAmountPercentageYTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportPersistencyAmountPercentageYTD);
				txtPerformanceReportsBDMBusinessReportPersistencyCountPercentageMTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportPersistencyCountPercentageMTD);
				txtPerformanceReportsBDMBusinessReportPersistencyCountPercentageQTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportPersistencyCountPercentageQTD);
				txtPerformanceReportsBDMBusinessReportPersistencyCountPercentageYTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportPersistencyCountPercentageYTD);
				txtPerformanceReportsBDMBusinessReportNumberOfMTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportNumberOfMTD);
				txtPerformanceReportsBDMBusinessReportNumberOfQTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportNumberOfQTD);
				txtPerformanceReportsBDMBusinessReportNumberOfYTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportNumberOfYTD);

				txtPerformanceReportsBDMBusinessReportMDRTStatusMTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportMDRTStatusMTD);
				txtPerformanceReportsBDMBusinessReportMDRTStatusQTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportMDRTStatusQTD);
				txtPerformanceReportsBDMBusinessReportMDRTStatusYTD = findViewById(R.id.txtPerformanceReportsBDMBusinessReportMDRTStatusYTD);
				// BDMCreditLifeReport
				textBDMCreditLifeReportRRYTDBusinessNOP = findViewById(R.id.textBDMCreditLifeReportRRYTDBusinessNOP);
				textBDMCreditLifeReportRRYTDBusinessPremiumAmt = findViewById(R.id.textBDMCreditLifeReportRRYTDBusinessPremiumAmt);
				textBDMCreditLifeReportHomeLoanPenetrationNOP = findViewById(R.id.textBDMCreditLifeReportHomeLoanPenetrationNOP);
				textBDMCreditLifeReportHomeLoanPenetrationPremiumAmt = findViewById(R.id.textBDMCreditLifeReportHomeLoanPenetrationPremiumAmt);
				textBDMCreditLifeReportPotentialLoansNOP = findViewById(R.id.textBDMCreditLifeReportPotentialLoansNOP);
				textBDMCreditLifeReportPotentialLoansPremiumAmt = findViewById(R.id.textBDMCreditLifeReportPotentialLoansPremiumAmt);
				textBDMCreditLifeReportCoveredIncShieldNOP = findViewById(R.id.textBDMCreditLifeReportCoveredIncShieldNOP);
				textBDMCreditLifeReportCoveredIncShieldPremiumAmt = findViewById(R.id.textBDMCreditLifeReportCoveredIncShieldPremiumAmt);
				textBDMCreditLifeReportPercentagePenetrationNOP = findViewById(R.id.textBDMCreditLifeReportPercentagePenetrationNOP);
				textBDMCreditLifeReportPercentagePenetrationPremiumAmt = findViewById(R.id.textBDMCreditLifeReportPercentagePenetrationPremiumAmt);

				// BDMServicingIndividualReports
				textoneSevenDaysEFTCount = findViewById(R.id.textoneSevenDaysEFTCount);
				textoneSevenDaysPendingCasesCount = findViewById(R.id.textoneSevenDaysPendingCasesCount);
				texteightFifteenDaysEFTCount = findViewById(R.id.texteightFifteenDaysEFTCount);
				texteightFifteenDaysPendingCasesCount = findViewById(R.id.texteightFifteenDaysPendingCasesCount);
				textfifteenTwentyDaysEFTCount = findViewById(R.id.textfifteenTwentyDaysEFTCount);
				textfifteenTwentyDaysPendingCasesCount = findViewById(R.id.textfifteenTwentyDaysPendingCasesCount);
				textgreaterThanTwentyOneDaysEFTCount = findViewById(R.id.textgreaterThanTwentyOneDaysEFTCount);
				textgreaterThanTwentyOneDaysPendingCasesCount = findViewById(R.id.textgreaterThanTwentyOneDaysPendingCasesCount);
			}

		}

		@Override
		protected String doInBackground(String... aurl) {
			try {
				running = true;
				SoapObject request;

				// String UserType = GetUserType();

				// string strCode, string strEmailId, string strMobileNo, string
				// strAuthKey

				// string strCode, string strEmailId, string strMobileNo, string
				// strAuthKey, string strType

				String NAMESPACE = "http://tempuri.org/";
				request = new SoapObject(NAMESPACE,METHOD_NAME_PERFORMANCE_REPORT);

				request.addProperty("strEmailId", strCIFBDMEmailId);
				request.addProperty("strMobileNo", strCIFBDMMObileNo);
				request.addProperty("strAuthKey", strCIFBDMPassword.trim());
				request.addProperty("strCode", strCIFBDMUserId);
				request.addProperty("strType", performanceReportType);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);
				System.out.println("request performance:" + request.toString());
				// allowAllSSL();

				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
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

								inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "PolicyDetails");
								System.out.println("inputpolicylist:"+ inputpolicylist);

								if (inputpolicylist != null) {
									strPerformanceReportErrorCOde = inputpolicylist;

									inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "Table");
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

										if (performanceReportType
												.equalsIgnoreCase("CIF Servicing Report")) {
											/*
											 * <PolicyDetails> <Table>
											 * <CIF_CODE>990175895</CIF_CODE>
											 * <ONE_SEVEN_DAYS_EFT_COUNT
											 * >1</ONE_SEVEN_DAYS_EFT_COUNT>
											 * <ONE_SEVEN_DAYS_EFT_AMOUNT
											 * >150000<
											 * /ONE_SEVEN_DAYS_EFT_AMOUNT>
											 * <ONE_SEVEN_DAYS_COUN_PENDING
											 * >1</ONE_SEVEN_DAYS_COUN_PENDING>
											 * <
											 * ONE_SEVEN_DAYS_AMOUNT_PENDING>150000
											 * </ONE_SEVEN_DAYS_AMOUNT_PENDING>
											 * <
											 * FIFTEEN_TWENTY_DAYS_EFT_COUNT>0</
											 * FIFTEEN_TWENTY_DAYS_EFT_COUNT>
											 * <FIFTEEN_TWENTY_DAYS_EFT_AMOUNT
											 * >0<
											 * /FIFTEEN_TWENTY_DAYS_EFT_AMOUNT>
											 * <
											 * FIFTEEN_TWENTY_DAYS_COUN_PENDI>0<
											 * /FIFTEEN_TWENTY_DAYS_COUN_PENDI>
											 * <
											 * FIFTEEN_TWENTY_DAYS_AMOUNT_PEN>0<
											 * /FIFTEEN_TWENTY_DAYS_AMOUNT_PEN>
											 * <
											 * TWENTYONE_DAY_ABOVE_EFT_COUNT>0</
											 * TWENTYONE_DAY_ABOVE_EFT_COUNT>
											 * <TWENTYONE_DAY_ABOVE_EFT_AMOUNT
											 * >0<
											 * /TWENTYONE_DAY_ABOVE_EFT_AMOUNT>
											 * <
											 * TWENTYONE_DAY_ABOVE_COUN_PENDI>2<
											 * /TWENTYONE_DAY_ABOVE_COUN_PENDI>
											 * <
											 * TWENTYONE_DAY_ABOVE_AMOUNT_PEN>150000
											 * </TWENTYONE_DAY_ABOVE_AMOUNT_PEN>
											 * <EIGHT_FIFTEEN_DAYS_EFT_COUNT>0</
											 * EIGHT_FIFTEEN_DAYS_EFT_COUNT>
											 * <EIGHT_FIFTEEN_DAYS_EFT_AMOUNT
											 * >0</
											 * EIGHT_FIFTEEN_DAYS_EFT_AMOUNT>
											 * <EIGHT_FIFTEEN_DAYS_COUN_PENDIN
											 * >0<
											 * /EIGHT_FIFTEEN_DAYS_COUN_PENDIN>
											 * <
											 * EIGHT_FIFTEEN_DAYS_AMOUNT_PEND>0<
											 * /EIGHT_FIFTEEN_DAYS_AMOUNT_PEND>
											 * </Table> </PolicyDetails>
											 */
											cIFServicingReportValuesModel.setPerformanceReportsCIFServicingReportEmployeeName("");

											cIFServicingReportValuesModel.setCIFoneSevenDaysEFTCount(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"ONE_SEVEN_DAYS_EFT_COUNT")
															+ "("
															+ new ParseXML()
																	.parseXmlTag(
																			inputpolicylist,
																			"ONE_SEVEN_DAYS_EFT_AMOUNT")
															+ ")");
											cIFServicingReportValuesModel
													.setCIFoneSevenDaysPendingCasesCount(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"ONE_SEVEN_DAYS_COUN_PENDING")
															+ "("
															+ new ParseXML()
																	.parseXmlTag(
																			inputpolicylist,
																			"ONE_SEVEN_DAYS_AMOUNT_PENDING")
															+ ")");

											cIFServicingReportValuesModel
													.setCIFeightFifteenDaysEFTCount(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"EIGHT_FIFTEEN_DAYS_EFT_COUNT")
															+ "("
															+ new ParseXML()
																	.parseXmlTag(
																			inputpolicylist,
																			"EIGHT_FIFTEEN_DAYS_EFT_AMOUNT")
															+ ")");
											cIFServicingReportValuesModel
													.setCIFeightFifteenDaysPendingCasesCount(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"EIGHT_FIFTEEN_DAYS_COUN_PENDIN")
															+ "("
															+ new ParseXML()
																	.parseXmlTag(
																			inputpolicylist,
																			"EIGHT_FIFTEEN_DAYS_AMOUNT_PEND")
															+ ")");

											cIFServicingReportValuesModel
													.setCIFfifteenTwentyDaysEFTCount(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"FIFTEEN_TWENTY_DAYS_EFT_COUNT")
															+ "("
															+ new ParseXML()
																	.parseXmlTag(
																			inputpolicylist,
																			"FIFTEEN_TWENTY_DAYS_EFT_AMOUNT")
															+ ")");
											cIFServicingReportValuesModel
													.setCIFfifteenTwentyDaysPendingCasesCount(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"FIFTEEN_TWENTY_DAYS_COUN_PENDI")
															+ "("
															+ new ParseXML()
																	.parseXmlTag(
																			inputpolicylist,
																			"FIFTEEN_TWENTY_DAYS_AMOUNT_PEN")
															+ ")");

											cIFServicingReportValuesModel
													.setCIFgreaterThanTwentyOneDaysEFTCount(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"TWENTYONE_DAY_ABOVE_EFT_COUNT")
															+ "("
															+ new ParseXML()
																	.parseXmlTag(
																			inputpolicylist,
																			"TWENTYONE_DAY_ABOVE_EFT_AMOUNT")
															+ ")");
											cIFServicingReportValuesModel
													.setCIFgreaterThanTwentyOneDaysPendingCasesCount(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"TWENTYONE_DAY_ABOVE_COUN_PENDI")
															+ "("
															+ new ParseXML()
																	.parseXmlTag(
																			inputpolicylist,
																			"TWENTYONE_DAY_ABOVE_AMOUNT_PEN")
															+ ")");
										} else if (performanceReportType
												.equalsIgnoreCase("CIF Business Report")) {
											/*
											 * <CIF_CODE>990175895</CIF_CODE>
											 * <RATED_MTD>0</RATED_MTD>
											 * <RATED_YTD>2129251</RATED_YTD>
											 * <MTD_PERCENT_CL_NOP
											 * >0</MTD_PERCENT_CL_NOP>
											 * <MTD_PERCENT_CL_NBP
											 * >0</MTD_PERCENT_CL_NBP>
											 * <QTD_PERCENT_CL_NOP
											 * >0</QTD_PERCENT_CL_NOP>
											 * 
											 * <QTD_PERCENT_CL_NBP>0</
											 * QTD_PERCENT_CL_NBP>
											 * <YTD_PERCENT_CL_NOP
											 * >0</YTD_PERCENT_CL_NOP>
											 * <YTD_PERCENT_CL_NBP
											 * >0</YTD_PERCENT_CL_NBP>
											 * 
											 * <YTD_PERSISTENCY_PERCENT_COUNT>80<
											 * /YTD_PERSISTENCY_PERCENT_COUNT>
											 * <YTD_PERSISTENCY_PERCENT_AMT
											 * >90.71
											 * </YTD_PERSISTENCY_PERCENT_AMT>
											 * 
											 * <TOTALMDRTPREMIUM>4324700</
											 * TOTALMDRTPREMIUM>
											 * <MDRTSLABACHIEVED>MDRT
											 * Challenger</MDRTSLABACHIEVED>
											 * <NEXTSLAB>1675300</NEXTSLAB>
											 */
											//

											cIfBusinessReportValuesModel
													.setCIFBusinessReportEmployeeName(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	""));
											cIfBusinessReportValuesModel
													.setCIFBusinessReportRatedPremiumMTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"RATED_MTD"));
											cIfBusinessReportValuesModel
													.setCIFBusinessReportRatedPremiumQTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	""));
											cIfBusinessReportValuesModel
													.setCIFBusinessReportRatedPremiumYTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"RATED_YTD"));

											cIfBusinessReportValuesModel
													.setCIFBusinessReportCLNOPMTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"MTD_PERCENT_CL_NOP"));
											cIfBusinessReportValuesModel
													.setCIFBusinessReportCLNOPQTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"QTD_PERCENT_CL_NOP"));
											cIfBusinessReportValuesModel
													.setCIFBusinessReportCLNOPYTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"YTD_PERCENT_CL_NOP"));

											cIfBusinessReportValuesModel
													.setCIFBusinessReportCLNBPMTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"MTD_PERCENT_CL_NBP"));
											cIfBusinessReportValuesModel
													.setCIFBusinessReportCLNBPtQTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"QTD_PERCENT_CL_NBP"));
											cIfBusinessReportValuesModel
													.setCIFBusinessReportCLNBPYTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"YTD_PERCENT_CL_NBP"));

											cIfBusinessReportValuesModel
													.setCIFBusinessReportThirteenMonthPercenAmtMTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	""));
											cIfBusinessReportValuesModel
													.setCIFBusinessReportThirteenMonthPercenAmtQTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	""));
											cIfBusinessReportValuesModel
													.setCIFBusinessReportThirteenMonthPercenAmtYTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"YTD_PERSISTENCY_PERCENT_AMT"));

											cIfBusinessReportValuesModel
													.setCIFBusinessReportThirteenMonthPercenCountMTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"YTD_PERSISTENCY_PERCENT_COUNT"));
											cIfBusinessReportValuesModel
													.setCIFBusinessReportThirteenMonthPercenCountQTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	""));
											cIfBusinessReportValuesModel
													.setCIFBusinessReportThirteenMonthPercenCountYTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"YTD_PERSISTENCY_PERCENT_COUNT"));

										} else if (performanceReportType
												.equalsIgnoreCase("BDM Servicing Individual Report")) {
											/*
											 * <BDMCODE>6773</BDMCODE>
											 * 
											 * <BDMNAME>SANTANU LODH</BDMNAME>
											 * <ONE_SEVEN_DAYS_EFT_COUNT
											 * >1</ONE_SEVEN_DAYS_EFT_COUNT>
											 * <ONE_SEVEN_DAYS_EFT_AMOUNT
											 * >99000</
											 * ONE_SEVEN_DAYS_EFT_AMOUNT>
											 * <ONE_SEVEN_DAYS_COUN_PENDING
											 * >6</ONE_SEVEN_DAYS_COUN_PENDING>
											 * <
											 * ONE_SEVEN_DAYS_AMOUNT_PENDING>339000
											 * </ONE_SEVEN_DAYS_AMOUNT_PENDING>
											 * 
											 * <EIGHT_FIFTEEN_DAYS_EFT_COUNT>1</
											 * EIGHT_FIFTEEN_DAYS_EFT_COUNT>
											 * <EIGHT_FIFTEEN_DAYS_EFT_AMOUNT
											 * >80000
											 * </EIGHT_FIFTEEN_DAYS_EFT_AMOUNT>
											 * <
											 * EIGHT_FIFTEEN_DAYS_COUN_PENDIN>2<
											 * /EIGHT_FIFTEEN_DAYS_COUN_PENDIN>
											 * <
											 * EIGHT_FIFTEEN_DAYS_AMOUNT_PEND>140000
											 * </EIGHT_FIFTEEN_DAYS_AMOUNT_PEND>
											 * 
											 * <FIFTEEN_TWENTY_DAYS_EFT_COUNT>0</
											 * FIFTEEN_TWENTY_DAYS_EFT_COUNT>
											 * <FIFTEEN_TWENTY_DAYS_EFT_AMOUNT
											 * >0<
											 * /FIFTEEN_TWENTY_DAYS_EFT_AMOUNT>
											 * <
											 * FIFTEEN_TWENTY_DAYS_COUN_PENDI>2<
											 * /FIFTEEN_TWENTY_DAYS_COUN_PENDI>
											 * <
											 * FIFTEEN_TWENTY_DAYS_AMOUNT_PEN>250000
											 * </FIFTEEN_TWENTY_DAYS_AMOUNT_PEN>
											 * 
											 * <TWENTYONE_DAY_ABOVE_EFT_COUNT>0</
											 * TWENTYONE_DAY_ABOVE_EFT_COUNT>
											 * <TWENTYONE_DAY_ABOVE_EFT_AMOUNT
											 * >0<
											 * /TWENTYONE_DAY_ABOVE_EFT_AMOUNT>
											 * <
											 * TWENTYONE_DAY_ABOVE_COUN_PENDI>2<
											 * /TWENTYONE_DAY_ABOVE_COUN_PENDI>
											 * <
											 * TWENTYONE_DAY_ABOVE_AMOUNT_PEN>300000
											 * </TWENTYONE_DAY_ABOVE_AMOUNT_PEN>
											 */

											bDMServicingIndividualReportsValuesModel
													.setOneSevenDaysEFTCount(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"ONE_SEVEN_DAYS_EFT_COUNT")
															+ "("
															+ new ParseXML()
																	.parseXmlTag(
																			inputpolicylist,
																			"ONE_SEVEN_DAYS_EFT_AMOUNT")
															+ ")");
											bDMServicingIndividualReportsValuesModel
													.setOneSevenDaysPendingCasesCount(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"ONE_SEVEN_DAYS_COUN_PENDING")
															+ "("
															+ new ParseXML()
																	.parseXmlTag(
																			inputpolicylist,
																			"ONE_SEVEN_DAYS_AMOUNT_PENDING")
															+ ")");

											bDMServicingIndividualReportsValuesModel
													.setEightFifteenDaysEFTCount(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"EIGHT_FIFTEEN_DAYS_EFT_COUNT")
															+ "("
															+ new ParseXML()
																	.parseXmlTag(
																			inputpolicylist,
																			"EIGHT_FIFTEEN_DAYS_EFT_AMOUNT")
															+ ")");
											bDMServicingIndividualReportsValuesModel
													.setEightFifteenDaysPendingCasesCount(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"EIGHT_FIFTEEN_DAYS_COUN_PENDIN")
															+ "("
															+ new ParseXML()
																	.parseXmlTag(
																			inputpolicylist,
																			"EIGHT_FIFTEEN_DAYS_AMOUNT_PEND")
															+ ")");

											bDMServicingIndividualReportsValuesModel
													.setFifteenTwentyDaysEFTCount(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"FIFTEEN_TWENTY_DAYS_EFT_COUNT")
															+ "("
															+ new ParseXML()
																	.parseXmlTag(
																			inputpolicylist,
																			"FIFTEEN_TWENTY_DAYS_EFT_AMOUNT")
															+ ")");
                                            bDMServicingIndividualReportsValuesModel
													.setFifteenTwentyDaysPendingCasesCount(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"FIFTEEN_TWENTY_DAYS_COUN_PENDI")
															+ "("
															+ new ParseXML()
																	.parseXmlTag(
																			inputpolicylist,
																			"FIFTEEN_TWENTY_DAYS_AMOUNT_PEN")
															+ ")");

											bDMServicingIndividualReportsValuesModel
													.setGreaterThanTwentyOneDaysEFTCount(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"TWENTYONE_DAY_ABOVE_EFT_COUNT")
															+ "("
															+ new ParseXML()
																	.parseXmlTag(
																			inputpolicylist,
																			"TWENTYONE_DAY_ABOVE_EFT_AMOUNT")
															+ ")");
											bDMServicingIndividualReportsValuesModel
													.setGreaterThanTwentyOneDaysPendingCasesCount(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"TWENTYONE_DAY_ABOVE_COUN_PENDI")
															+ "("
															+ new ParseXML()
																	.parseXmlTag(
																			inputpolicylist,
																			"TWENTYONE_DAY_ABOVE_AMOUNT_PEN")
															+ ")");
										} else if (performanceReportType
												.equalsIgnoreCase("BDM Credit Life Report")) {

											/*
											 * <PolicyDetails> <Table>
											 * <EMPLOYEEID>2128</EMPLOYEEID>
											 * <RR_NOP>146</RR_NOP>
											 * <RR_TOTALPREMIUM
											 * >1710737</RR_TOTALPREMIUM>
											 * 
											 * <POTENTIAL_LOANS_NOP>64</
											 * POTENTIAL_LOANS_NOP>
											 * <POTENTIAL_LOANS_LIMIT
											 * >690.59</POTENTIAL_LOANS_LIMIT>
											 * <COVERED_NOP>57</COVERED_NOP>
											 * 
											 * <COVERED_LIMIT>612.91</COVERED_LIMIT
											 * > <PENITRATION_LIMIT_PERCENT>89</
											 * PENITRATION_LIMIT_PERCENT>
											 * <PENITRATION_NOP_PERCENT
											 * >89</PENITRATION_NOP_PERCENT>
											 * </Table> </PolicyDetails>
											 */

											bDMCreditLifeReportValuesModel
													.setBDMCreditLifeReportRRYTDBusinessNOP(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"RR_NOP"));
											bDMCreditLifeReportValuesModel
													.setBDMCreditLifeReportRRYTDBusinessPremiumAmt(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"RR_TOTALPREMIUM"));

											bDMCreditLifeReportValuesModel
													.setBDMCreditLifeReportHomeLoanPenetrationNOP("NOP");
											bDMCreditLifeReportValuesModel
													.setBDMCreditLifeReportHomeLoanPenetrationPremiumAmt("Limit(in Lakhs)");

											bDMCreditLifeReportValuesModel
													.setBDMCreditLifeReportPotentialLoansNOP(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"POTENTIAL_LOANS_NOP"));
											bDMCreditLifeReportValuesModel
													.setBDMCreditLifeReportPotentialLoansPremiumAmt(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"POTENTIAL_LOANS_LIMIT"));

											bDMCreditLifeReportValuesModel
													.setBDMCreditLifeReportCoveredIncShieldNOP(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"COVERED_NOP"));
											bDMCreditLifeReportValuesModel
													.setBDMCreditLifeReportCoveredIncShieldPremiumAmt(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"COVERED_LIMIT"));

											bDMCreditLifeReportValuesModel
													.setBDMCreditLifeReportPercentagePenetrationNOP(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"PENITRATION_LIMIT_PERCENT"));
											bDMCreditLifeReportValuesModel
													.setBDMCreditLifeReportPercentagePenetrationPremiumAmt(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"PENITRATION_LIMIT_PERCENT"));
										} else if (performanceReportType
												.equalsIgnoreCase("BDM Business Report")) {
											/*
											 * <EMPLOYEEID>12089</EMPLOYEEID>
											 * <TOTAL_BRANCHES
											 * >209</TOTAL_BRANCHES>
											 * <TOTAL_CIF>451</TOTAL_CIF>
											 * <RATED_MTD>10772760</RATED_MTD>
											 * <RATED_YTD>286202950</RATED_YTD>
											 * <YTD_RENEWAL_BUDGET>361047000</
											 * YTD_RENEWAL_BUDGET>
											 * 
											 * <YTD_NB_BUDGET>265487000</
											 * YTD_NB_BUDGET>
											 * <YTD_PERCENT_RENEWAL_ACHIEV
											 * >93</YTD_PERCENT_RENEWAL_ACHIEV>
											 * <YTD_PERCENT_NB_ACHIEV>108</
											 * YTD_PERCENT_NB_ACHIEV>
											 * <MTD_RENEWAL_BUDGET
											 * >53104000</MTD_RENEWAL_BUDGET>
											 * <MTD_NB_BUDGET
											 * >40815000</MTD_NB_BUDGET>
											 * <MTD_PERCENT_RENEWAL_ACHIEV
											 * >35</MTD_PERCENT_RENEWAL_ACHIEV>
											 * <MTD_PERCENT_NB_ACHIEV>26</
											 * MTD_PERCENT_NB_ACHIEV>
											 * <MTD_PERCENT_CL_NOP
											 * >0</MTD_PERCENT_CL_NOP>
											 * <QTD_PERCENT_CL_NBP
											 * >10.66</QTD_PERCENT_CL_NBP>
											 * <YTD_PERCENT_CL_NOP
											 * >13.99</YTD_PERCENT_CL_NOP>
											 * <MTD_PERCENT_CL_NBP
											 * >0</MTD_PERCENT_CL_NBP>
											 * <QTD_PERCENT_CL_NOP
											 * >4.87</QTD_PERCENT_CL_NOP>
											 * <YTD_PERCENT_CL_NBP
											 * >17.96</YTD_PERCENT_CL_NBP>
											 * 
											 * <YTD_PERSISTENCY_PERCENT_AMT>82.73
											 * </YTD_PERSISTENCY_PERCENT_AMT>
											 * <YTD_PERSISTENCY_PERCENT_COUNT
											 * >83.60
											 * </YTD_PERSISTENCY_PERCENT_COUNT>
											 * <NO_OF_MDRT_CHALLENGER>36</
											 * NO_OF_MDRT_CHALLENGER>
											 * <NO_OF_MDRTS_STAR
											 * >9</NO_OF_MDRTS_STAR>
											 * <NO_OF_COT>0</NO_OF_COT>
											 * <NO_OF_TOT>0</NO_OF_TOT>
											 * <DATA_CREATION_TIME
											 * >2016-11-10T08:
											 * 59:12.443957-08:00<
											 * /DATA_CREATION_TIME>
											 */

											bdmBusinessReportValuesModel
													.setBDMBusinessReportTotalBranches(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"TOTAL_BRANCHES"));
											bdmBusinessReportValuesModel
													.setBDMBusinessReportCIFName(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"TOTAL_CIF"));

											bdmBusinessReportValuesModel
													.setBDMBusinessReportNBBudgetMTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"MTD_NB_BUDGET"));
											bdmBusinessReportValuesModel
													.setBDMBusinessReportNBBudgetQTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	""));
											bdmBusinessReportValuesModel
													.setBDMBusinessReportNBBudgetYTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"YTD_NB_BUDGET"));

											bdmBusinessReportValuesModel
													.setBDMBusinessReportPercentageAchievementMTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"MTD_PERCENT_NB_ACHIEV"));
											bdmBusinessReportValuesModel
													.setBDMBusinessReportPercentageAchievementQTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	""));
											bdmBusinessReportValuesModel
													.setBDMBusinessReportPercentageAchievementYTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"YTD_PERCENT_NB_ACHIEV"));

											bdmBusinessReportValuesModel
													.setBDMBusinessReportRatedAPremiumtMTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"RATED_MTD"));
											bdmBusinessReportValuesModel
													.setBDMBusinessReportRatedAPremiumQTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	""));
											bdmBusinessReportValuesModel
													.setBDMBusinessReportRatedAPremiumYTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"RATED_YTD"));

											bdmBusinessReportValuesModel
													.setBDMBusinessReportRenewalBudgettMTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"MTD_RENEWAL_BUDGET"));
											bdmBusinessReportValuesModel
													.setBDMBusinessReportRenewalBudgetQTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	""));
											bdmBusinessReportValuesModel
													.setBDMBusinessReportRenewalBudgetYTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"YTD_RENEWAL_BUDGET"));

											bdmBusinessReportValuesModel
													.setBDMBusinessReportRenewalAchievementMTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"MTD_PERCENT_RENEWAL_ACHIEV"));
											bdmBusinessReportValuesModel
													.setBDMBusinessReportRenewalAchievementQTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	""));
											bdmBusinessReportValuesModel
													.setBDMBusinessReportRenewalAchievementYTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"YTD_PERCENT_RENEWAL_ACHIEV"));

											bdmBusinessReportValuesModel
													.setBDMBusinessReportConnectLifeNopsMTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"MTD_PERCENT_CL_NOP"));
											bdmBusinessReportValuesModel
													.setBDMBusinessReportConnectLifeNopsQTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"QTD_PERCENT_CL_NOP"));
											bdmBusinessReportValuesModel
													.setBDMBusinessReportConnectLifeNopsYTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"YTD_PERCENT_CL_NOP"));

											bdmBusinessReportValuesModel
													.setBDMBusinessReportPercentageClNBpMTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"MTD_PERCENT_CL_NBP"));
											bdmBusinessReportValuesModel
													.setBDMBusinessReportPercentageClNBpQTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"QTD_PERCENT_CL_NBP"));
											bdmBusinessReportValuesModel
													.setBDMBusinessReportPercentageClNBpYTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"YTD_PERCENT_CL_NBP"));

											bdmBusinessReportValuesModel
													.setBDMBusinessReportPersistencyAmountPercentageMTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	""));
											bdmBusinessReportValuesModel
													.setBDMBusinessReportPersistencyAmountPercentageQTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	""));
											bdmBusinessReportValuesModel
													.setBDMBusinessReportPersistencyAmountPercentageYTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"YTD_PERSISTENCY_PERCENT_AMT"));

											bdmBusinessReportValuesModel
													.setBDMBusinessReportPersistencyCountPercentageMTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	""));
											bdmBusinessReportValuesModel
													.setBDMBusinessReportPersistencyCountPercentageQTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	""));
											bdmBusinessReportValuesModel
													.setBDMBusinessReportPersistencyCountPercentageYTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"YTD_PERSISTENCY_PERCENT_COUNT"));

											bdmBusinessReportValuesModel
													.setBDMBusinessReportNumberOfMTD("No. of MDRTs");
											bdmBusinessReportValuesModel
													.setBDMBusinessReportNumberOfQTD("No. of COT");
											bdmBusinessReportValuesModel
													.setBDMBusinessReportNumberOfYTD("No. of TOT");

											bdmBusinessReportValuesModel
													.setBDMBusinessReportMDRTStatusMTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"NO_OF_MDRTS_STAR"));
											bdmBusinessReportValuesModel
													.setBDMBusinessReportMDRTStatusQTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"NO_OF_COT"));
											bdmBusinessReportValuesModel
													.setBDMBusinessReportMDRTStatusYTD(new ParseXML()
															.parseXmlTag(
																	inputpolicylist,
																	"NO_OF_TOT"));
										}

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
						/*
						 * tblChannelProposerTrackerList.getLayoutParams().height
						 * = LayoutParams.WRAP_CONTENT;
						 * tblChannelProposerTrackerList.requestLayout();
						 */

						if (performanceReportType
								.equalsIgnoreCase("CIF Servicing Report")) {
							
							tblPerformanceReportsCIFServicingReport.setVisibility(View.VISIBLE);
							hideCIFBusinessReport();
							hideBDMBusinessReport();
							hideBDMCreditLifeReport();
							hideBDMServicingIndividualReports();
							// CIFServicingReport
							txtPerformanceReportsCIFServicingReportEmployeeName
									.setText(cIFServicingReportValuesModel
											.getPerformanceReportsCIFServicingReportEmployeeName());
							CIFtextoneSevenDaysEFTCount
									.setText(cIFServicingReportValuesModel
											.getCIFoneSevenDaysEFTCount());
							CIFtextoneSevenDaysPendingCasesCount
									.setText(cIFServicingReportValuesModel
											.getCIFoneSevenDaysPendingCasesCount());

							CIFtexteightFifteenDaysEFTCount
									.setText(cIFServicingReportValuesModel
											.getCIFeightFifteenDaysEFTCount());
							CIFtexteightFifteenDaysPendingCasesCount
									.setText(cIFServicingReportValuesModel
											.getCIFeightFifteenDaysPendingCasesCount());

							CIFtextfifteenTwentyDaysEFTCount
									.setText(cIFServicingReportValuesModel
											.getCIFfifteenTwentyDaysEFTCount());
							CIFtextfifteenTwentyDaysPendingCasesCount
									.setText(cIFServicingReportValuesModel
											.getCIFfifteenTwentyDaysPendingCasesCount());

							CIFtextgreaterThanTwentyOneDaysEFTCount
									.setText(cIFServicingReportValuesModel
											.getCIFgreaterThanTwentyOneDaysEFTCount());
							CIFtextgreaterThanTwentyOneDaysPendingCasesCount
									.setText(cIFServicingReportValuesModel
											.getCIFgreaterThanTwentyOneDaysPendingCasesCount());

							tblPerformanceReportsCIFServicingReport
									.getParent()
									.requestChildFocus(
											tblPerformanceReportsCIFServicingReport,
											tblPerformanceReportsCIFServicingReport);

						} else if (performanceReportType
								.equalsIgnoreCase("CIF Business Report")) {
							hideCIFServicingReport();
							hideBDMBusinessReport();
							hideBDMCreditLifeReport();
							hideBDMServicingIndividualReports();

							// CIFBusinessReportValuesModel
							
							tblPerformanceReportsCIFBusinessReport.setVisibility(View.VISIBLE);
							
							txtPerformanceReportsCIFBusinessReportEmployeeName
									.setText(cIfBusinessReportValuesModel
											.getCIFBusinessReportEmployeeName());
							txtPerformanceReportsCIFBusinessReportRatedPremiumMTD
									.setText(cIfBusinessReportValuesModel
											.getCIFBusinessReportRatedPremiumMTD());
							txtPerformanceReportsCIFBusinessReportRatedPremiumQTD
									.setText(cIfBusinessReportValuesModel
											.getCIFBusinessReportRatedPremiumQTD());
							txtPerformanceReportsCIFBusinessReportRatedPremiumYTD
									.setText(cIfBusinessReportValuesModel
											.getCIFBusinessReportRatedPremiumYTD());

							txtPerformanceReportsCIFBusinessReportCLNOPMTD
									.setText(cIfBusinessReportValuesModel
											.getCIFBusinessReportCLNOPMTD());
							txtPerformanceReportsCIFBusinessReportCLNOPQTD
									.setText(cIfBusinessReportValuesModel
											.getCIFBusinessReportCLNOPQTD());
							txtPerformanceReportsCIFBusinessReportCLNOPYTD
									.setText(cIfBusinessReportValuesModel
											.getCIFBusinessReportCLNOPYTD());

							txtPerformanceReportsCIFBusinessReportCLNBPMTD
									.setText(cIfBusinessReportValuesModel
											.getCIFBusinessReportCLNBPMTD());
							txtPerformanceReportsCIFBusinessReportCLNBPtQTD
									.setText(cIfBusinessReportValuesModel
											.getCIFBusinessReportCLNBPtQTD());
							txtPerformanceReportsCIFBusinessReportCLNBPYTD
									.setText(cIfBusinessReportValuesModel
											.getCIFBusinessReportCLNBPYTD());

							txtPerformanceReportsCIFBusinessReportThirteenMonthPercenAmtMTD
									.setText(cIfBusinessReportValuesModel
											.getCIFBusinessReportThirteenMonthPercenAmtMTD());
							txtPerformanceReportsCIFBusinessReportThirteenMonthPercenAmtQTD
									.setText(cIfBusinessReportValuesModel
											.getCIFBusinessReportThirteenMonthPercenAmtQTD());
							txtPerformanceReportsCIFBusinessReportThirteenMonthPercenAmtYTD
									.setText(cIfBusinessReportValuesModel
											.getCIFBusinessReportThirteenMonthPercenAmtYTD());

							txtPerformanceReportsCIFBusinessReportThirteenMonthPercenCountMTD
									.setText(cIfBusinessReportValuesModel
											.getCIFBusinessReportThirteenMonthPercenCountMTD());
							txtPerformanceReportsCIFBusinessReportThirteenMonthPercenCountQTD
									.setText(cIfBusinessReportValuesModel
											.getCIFBusinessReportThirteenMonthPercenCountQTD());
							txtPerformanceReportsCIFBusinessReportThirteenMonthPercenCountYTD
									.setText(cIfBusinessReportValuesModel
											.getCIFBusinessReportThirteenMonthPercenCountYTD());

							tblPerformanceReportsCIFBusinessReport
									.getParent()
									.requestChildFocus(
											tblPerformanceReportsCIFBusinessReport,
											tblPerformanceReportsCIFBusinessReport);
						} else if (performanceReportType
								.equalsIgnoreCase("BDM Servicing Individual Report")) {
							hideCIFServicingReport();
							hideCIFBusinessReport();
							hideBDMBusinessReport();
							hideBDMCreditLifeReport();

							
							tblPerformanceReportsBDMServicingIndividualReports.setVisibility(View.VISIBLE);
									

							// BDMServicingIndividualReports
							textoneSevenDaysEFTCount
									.setText(bDMServicingIndividualReportsValuesModel
											.getOneSevenDaysEFTCount());
							textoneSevenDaysPendingCasesCount
									.setText(bDMServicingIndividualReportsValuesModel
											.getOneSevenDaysPendingCasesCount());

							texteightFifteenDaysEFTCount
									.setText(bDMServicingIndividualReportsValuesModel
											.getEightFifteenDaysEFTCount());
							texteightFifteenDaysPendingCasesCount
									.setText(bDMServicingIndividualReportsValuesModel
											.getEightFifteenDaysPendingCasesCount());

							textfifteenTwentyDaysEFTCount
									.setText(bDMServicingIndividualReportsValuesModel
											.getFifteenTwentyDaysEFTCount());
							textfifteenTwentyDaysPendingCasesCount
									.setText(bDMServicingIndividualReportsValuesModel
											.getFifteenTwentyDaysPendingCasesCount());

							textgreaterThanTwentyOneDaysEFTCount
									.setText(bDMServicingIndividualReportsValuesModel
											.getGreaterThanTwentyOneDaysEFTCount());
							textgreaterThanTwentyOneDaysPendingCasesCount
									.setText(bDMServicingIndividualReportsValuesModel
											.getGreaterThanTwentyOneDaysPendingCasesCount());

							tblPerformanceReportsBDMServicingIndividualReports
									.getParent()
									.requestChildFocus(
											tblPerformanceReportsBDMServicingIndividualReports,
											tblPerformanceReportsBDMServicingIndividualReports);
						} else if (performanceReportType
								.equalsIgnoreCase("BDM Credit Life Report")) {
							hideCIFServicingReport();
							hideCIFBusinessReport();
							hideBDMBusinessReport();
							hideBDMServicingIndividualReports();

							
							tblPerformanceReportsBDMCreditLifeReport.setVisibility(View.VISIBLE);

							// BDMCreditLifeReport
							textBDMCreditLifeReportRRYTDBusinessNOP
									.setText(bDMCreditLifeReportValuesModel
											.getBDMCreditLifeReportRRYTDBusinessNOP());
							textBDMCreditLifeReportRRYTDBusinessPremiumAmt
									.setText(bDMCreditLifeReportValuesModel
											.getBDMCreditLifeReportRRYTDBusinessPremiumAmt());

							textBDMCreditLifeReportHomeLoanPenetrationNOP
									.setText(bDMCreditLifeReportValuesModel
											.getBDMCreditLifeReportHomeLoanPenetrationNOP());
							textBDMCreditLifeReportHomeLoanPenetrationPremiumAmt
									.setText(bDMCreditLifeReportValuesModel
											.getBDMCreditLifeReportHomeLoanPenetrationPremiumAmt());

							textBDMCreditLifeReportPotentialLoansNOP
									.setText(bDMCreditLifeReportValuesModel
											.getBDMCreditLifeReportPotentialLoansNOP());
							textBDMCreditLifeReportPotentialLoansPremiumAmt
									.setText(bDMCreditLifeReportValuesModel
											.getBDMCreditLifeReportPotentialLoansPremiumAmt());

							textBDMCreditLifeReportCoveredIncShieldNOP
									.setText(bDMCreditLifeReportValuesModel
											.getBDMCreditLifeReportCoveredIncShieldNOP());
							textBDMCreditLifeReportCoveredIncShieldPremiumAmt
									.setText(bDMCreditLifeReportValuesModel
											.getBDMCreditLifeReportCoveredIncShieldPremiumAmt());

							textBDMCreditLifeReportPercentagePenetrationNOP
									.setText(bDMCreditLifeReportValuesModel
											.getBDMCreditLifeReportPercentagePenetrationNOP());
							textBDMCreditLifeReportPercentagePenetrationPremiumAmt
									.setText(bDMCreditLifeReportValuesModel
											.getBDMCreditLifeReportPercentagePenetrationPremiumAmt());

							tblPerformanceReportsBDMCreditLifeReport
									.getParent()
									.requestChildFocus(
											tblPerformanceReportsBDMCreditLifeReport,
											tblPerformanceReportsBDMCreditLifeReport);

						} else if (performanceReportType
								.equalsIgnoreCase("BDM Business Report")) {
							hideCIFServicingReport();
							hideCIFBusinessReport();
							hideBDMBusinessReport();
							hideBDMServicingIndividualReports();

							tblPerformanceReportsBDMBusinessReport.setVisibility(View.VISIBLE);

							txtPerformanceReportsBDMBusinessReportTotalBranches
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportTotalBranches());
							txtPerformanceReportsBDMBusinessReportCIFName
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportCIFName());

							txtPerformanceReportsBDMBusinessReportNBBudgetMTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportNBBudgetMTD());
							txtPerformanceReportsBDMBusinessReportNBBudgetQTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportNBBudgetQTD());
							txtPerformanceReportsBDMBusinessReportNBBudgetYTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportNBBudgetYTD());

							txtPerformanceReportsBDMBusinessReportPercentageAchievementMTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportPercentageAchievementMTD());
							txtPerformanceReportsBDMBusinessReportPercentageAchievementQTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportPercentageAchievementQTD());
							txtPerformanceReportsBDMBusinessReportPercentageAchievementYTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportPercentageAchievementYTD());

							txtPerformanceReportsBDMBusinessReportRatedAPremiumtMTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportRatedAPremiumtMTD());
							txtPerformanceReportsBDMBusinessReportRatedAPremiumQTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportRatedAPremiumQTD());
							txtPerformanceReportsBDMBusinessReportRatedAPremiumYTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportRatedAPremiumYTD());

							txtPerformanceReportsBDMBusinessReportRenewalBudgettMTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportRenewalBudgettMTD());
							txtPerformanceReportsBDMBusinessReportRenewalBudgetQTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportRenewalBudgetQTD());
							txtPerformanceReportsBDMBusinessReportRenewalBudgetYTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportRenewalBudgetYTD());

							txtPerformanceReportsBDMBusinessReportRenewalAchievementMTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportRenewalAchievementMTD());
							txtPerformanceReportsBDMBusinessReportRenewalAchievementQTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportRenewalAchievementQTD());
							txtPerformanceReportsBDMBusinessReportRenewalAchievementYTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportRenewalAchievementYTD());

							txtPerformanceReportsBDMBusinessReportConnectLifeNopsMTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportConnectLifeNopsMTD());
							txtPerformanceReportsBDMBusinessReportConnectLifeNopsQTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportConnectLifeNopsQTD());
							txtPerformanceReportsBDMBusinessReportConnectLifeNopsYTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportConnectLifeNopsYTD());

							txtPerformanceReportsBDMBusinessReportPercentageClNBpMTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportPercentageClNBpMTD());
							txtPerformanceReportsBDMBusinessReportPercentageClNBpQTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportPercentageClNBpQTD());
							txtPerformanceReportsBDMBusinessReportPercentageClNBpYTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportPercentageClNBpYTD());

							txtPerformanceReportsBDMBusinessReportPersistencyAmountPercentageMTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportPersistencyAmountPercentageMTD());
							txtPerformanceReportsBDMBusinessReportPersistencyAmountPercentageQTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportPersistencyAmountPercentageQTD());
							txtPerformanceReportsBDMBusinessReportPersistencyAmountPercentageYTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportPersistencyAmountPercentageYTD());

							txtPerformanceReportsBDMBusinessReportPersistencyCountPercentageMTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportPersistencyCountPercentageMTD());
							txtPerformanceReportsBDMBusinessReportPersistencyCountPercentageQTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportPersistencyCountPercentageQTD());
							txtPerformanceReportsBDMBusinessReportPersistencyCountPercentageYTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportPersistencyCountPercentageYTD());

							txtPerformanceReportsBDMBusinessReportNumberOfMTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportNumberOfMTD());
							txtPerformanceReportsBDMBusinessReportNumberOfQTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportNumberOfQTD());
							txtPerformanceReportsBDMBusinessReportNumberOfYTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportNumberOfYTD());

							txtPerformanceReportsBDMBusinessReportMDRTStatusMTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportMDRTStatusMTD());
							txtPerformanceReportsBDMBusinessReportMDRTStatusQTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportMDRTStatusQTD());
							txtPerformanceReportsBDMBusinessReportMDRTStatusYTD
									.setText(bdmBusinessReportValuesModel
											.getBDMBusinessReportMDRTStatusYTD());
							tblPerformanceReportsBDMBusinessReport
									.getParent()
									.requestChildFocus(
											tblPerformanceReportsBDMBusinessReport,
											tblPerformanceReportsBDMBusinessReport);
						}

					} else {
						hideCIFBusinessReport();
						hideCIFServicingReport();
						hideBDMBusinessReport();
						hideBDMCreditLifeReport();
						hideBDMServicingIndividualReports();
						Toast.makeText(getApplicationContext(),
								"No record found", Toast.LENGTH_LONG).show();
					}
				} else {
					hideCIFBusinessReport();
					hideCIFServicingReport();
					hideBDMBusinessReport();
					hideBDMCreditLifeReport();
					hideBDMServicingIndividualReports();
					Toast.makeText(getApplicationContext(),
							"You are not authorised user", Toast.LENGTH_LONG)
							.show();
				}
			} else {
				// servererror();
				hideCIFBusinessReport();
				hideCIFServicingReport();
				hideBDMBusinessReport();
				hideBDMCreditLifeReport();
				hideBDMServicingIndividualReports();
				Toast.makeText(getApplicationContext(), "No record found",
						Toast.LENGTH_LONG).show();
			}

		}

	}

	private void startDownloadPerformanceReport() {
		// String url =
		// "http://farm1..flickr.com/114/298125983_0e4bf66782_b.jpg";
		// new DownloadFileAsyncRevival().execute("demo");
		taskPerformanceReport = new DownloadFileAsyncPerformanceReport();
		taskPerformanceReport.execute("demo");
	}
	
	class CIFServicingReportValuesModel {
		private String PerformanceReportsCIFServicingReportEmployeeName,CIFoneSevenDaysEFTCount,
		CIFoneSevenDaysPendingCasesCount,CIFeightFifteenDaysEFTCount,
		CIFeightFifteenDaysPendingCasesCount,CIFfifteenTwentyDaysEFTCount,
		CIFfifteenTwentyDaysPendingCasesCount,CIFgreaterThanTwentyOneDaysEFTCount,
		CIFgreaterThanTwentyOneDaysPendingCasesCount;


		String getPerformanceReportsCIFServicingReportEmployeeName() {
			return PerformanceReportsCIFServicingReportEmployeeName;
		}

		void setPerformanceReportsCIFServicingReportEmployeeName(
                String performanceReportsCIFServicingReportEmployeeName) {
			PerformanceReportsCIFServicingReportEmployeeName = performanceReportsCIFServicingReportEmployeeName;
		}

		String getCIFoneSevenDaysEFTCount() {
			return CIFoneSevenDaysEFTCount;
		}

		void setCIFoneSevenDaysEFTCount(String cIFoneSevenDaysEFTCount) {
			CIFoneSevenDaysEFTCount = cIFoneSevenDaysEFTCount;
		}

		String getCIFoneSevenDaysPendingCasesCount() {
			return CIFoneSevenDaysPendingCasesCount;
		}

		void setCIFoneSevenDaysPendingCasesCount(
                String cIFoneSevenDaysPendingCasesCount) {
			CIFoneSevenDaysPendingCasesCount = cIFoneSevenDaysPendingCasesCount;
		}

		String getCIFeightFifteenDaysEFTCount() {
			return CIFeightFifteenDaysEFTCount;
		}

		void setCIFeightFifteenDaysEFTCount(String cIFeightFifteenDaysEFTCount) {
			CIFeightFifteenDaysEFTCount = cIFeightFifteenDaysEFTCount;
		}

		String getCIFeightFifteenDaysPendingCasesCount() {
			return CIFeightFifteenDaysPendingCasesCount;
		}

		void setCIFeightFifteenDaysPendingCasesCount(
                String cIFeightFifteenDaysPendingCasesCount) {
			CIFeightFifteenDaysPendingCasesCount = cIFeightFifteenDaysPendingCasesCount;
		}

		String getCIFfifteenTwentyDaysEFTCount() {
			return CIFfifteenTwentyDaysEFTCount;
		}

		void setCIFfifteenTwentyDaysEFTCount(String cIFfifteenTwentyDaysEFTCount) {
			CIFfifteenTwentyDaysEFTCount = cIFfifteenTwentyDaysEFTCount;
		}

		String getCIFfifteenTwentyDaysPendingCasesCount() {
			return CIFfifteenTwentyDaysPendingCasesCount;
		}

		void setCIFfifteenTwentyDaysPendingCasesCount(
                String cIFfifteenTwentyDaysPendingCasesCount) {
			CIFfifteenTwentyDaysPendingCasesCount = cIFfifteenTwentyDaysPendingCasesCount;
		}

		String getCIFgreaterThanTwentyOneDaysEFTCount() {
			return CIFgreaterThanTwentyOneDaysEFTCount;
		}

		void setCIFgreaterThanTwentyOneDaysEFTCount(
                String cIFgreaterThanTwentyOneDaysEFTCount) {
			CIFgreaterThanTwentyOneDaysEFTCount = cIFgreaterThanTwentyOneDaysEFTCount;
		}

		String getCIFgreaterThanTwentyOneDaysPendingCasesCount() {
			return CIFgreaterThanTwentyOneDaysPendingCasesCount;
		}

		void setCIFgreaterThanTwentyOneDaysPendingCasesCount(
                String cIFgreaterThanTwentyOneDaysPendingCasesCount) {
			CIFgreaterThanTwentyOneDaysPendingCasesCount = cIFgreaterThanTwentyOneDaysPendingCasesCount;
		}
	}
	
	
	class CIFBusinessReportValuesModel{

		private String CIFBusinessReportEmployeeName,CIFBusinessReportRatedPremiumMTD,CIFBusinessReportRatedPremiumQTD,
		CIFBusinessReportRatedPremiumYTD,CIFBusinessReportCLNOPMTD,
		CIFBusinessReportCLNOPQTD,CIFBusinessReportCLNOPYTD,
		CIFBusinessReportCLNBPMTD,CIFBusinessReportCLNBPtQTD,
		CIFBusinessReportCLNBPYTD,CIFBusinessReportThirteenMonthPercenAmtMTD,
		CIFBusinessReportThirteenMonthPercenAmtQTD,CIFBusinessReportThirteenMonthPercenAmtYTD,
		CIFBusinessReportThirteenMonthPercenCountMTD,CIFBusinessReportThirteenMonthPercenCountQTD,
		CIFBusinessReportThirteenMonthPercenCountYTD;

		String getCIFBusinessReportEmployeeName() {
			return CIFBusinessReportEmployeeName;
		}

		String getCIFBusinessReportRatedPremiumMTD() {
			return CIFBusinessReportRatedPremiumMTD;
		}

		String getCIFBusinessReportRatedPremiumQTD() {
			return CIFBusinessReportRatedPremiumQTD;
		}

		String getCIFBusinessReportRatedPremiumYTD() {
			return CIFBusinessReportRatedPremiumYTD;
		}

		String getCIFBusinessReportCLNOPMTD() {
			return CIFBusinessReportCLNOPMTD;
		}

		String getCIFBusinessReportCLNOPQTD() {
			return CIFBusinessReportCLNOPQTD;
		}

		String getCIFBusinessReportCLNOPYTD() {
			return CIFBusinessReportCLNOPYTD;
		}

		String getCIFBusinessReportCLNBPMTD() {
			return CIFBusinessReportCLNBPMTD;
		}

		String getCIFBusinessReportCLNBPtQTD() {
			return CIFBusinessReportCLNBPtQTD;
		}

		String getCIFBusinessReportCLNBPYTD() {
			return CIFBusinessReportCLNBPYTD;
		}

		String getCIFBusinessReportThirteenMonthPercenAmtMTD() {
			return CIFBusinessReportThirteenMonthPercenAmtMTD;
		}

		String getCIFBusinessReportThirteenMonthPercenAmtQTD() {
			return CIFBusinessReportThirteenMonthPercenAmtQTD;
		}

		String getCIFBusinessReportThirteenMonthPercenAmtYTD() {
			return CIFBusinessReportThirteenMonthPercenAmtYTD;
		}

		String getCIFBusinessReportThirteenMonthPercenCountMTD() {
			return CIFBusinessReportThirteenMonthPercenCountMTD;
		}

		String getCIFBusinessReportThirteenMonthPercenCountQTD() {
			return CIFBusinessReportThirteenMonthPercenCountQTD;
		}

		String getCIFBusinessReportThirteenMonthPercenCountYTD() {
			return CIFBusinessReportThirteenMonthPercenCountYTD;
		}
		
		void setCIFBusinessReportEmployeeName(
                String cIFBusinessReportEmployeeName) {
			CIFBusinessReportEmployeeName = cIFBusinessReportEmployeeName;
		}
		void setCIFBusinessReportRatedPremiumMTD(
                String cIFBusinessReportRatedPremiumMTD) {
			CIFBusinessReportRatedPremiumMTD = cIFBusinessReportRatedPremiumMTD;
		}

		void setCIFBusinessReportRatedPremiumQTD(
                String cIFBusinessReportRatedPremiumQTD) {
			CIFBusinessReportRatedPremiumQTD = cIFBusinessReportRatedPremiumQTD;
		}

		void setCIFBusinessReportRatedPremiumYTD(
                String cIFBusinessReportRatedPremiumYTD) {
			CIFBusinessReportRatedPremiumYTD = cIFBusinessReportRatedPremiumYTD;
		}

		void setCIFBusinessReportCLNOPMTD(String cIFBusinessReportCLNOPMTD) {
			CIFBusinessReportCLNOPMTD = cIFBusinessReportCLNOPMTD;
		}

		void setCIFBusinessReportCLNOPQTD(String cIFBusinessReportCLNOPQTD) {
			CIFBusinessReportCLNOPQTD = cIFBusinessReportCLNOPQTD;
		}

		void setCIFBusinessReportCLNOPYTD(String cIFBusinessReportCLNOPYTD) {
			CIFBusinessReportCLNOPYTD = cIFBusinessReportCLNOPYTD;
		}

		void setCIFBusinessReportCLNBPMTD(String cIFBusinessReportCLNBPMTD) {
			CIFBusinessReportCLNBPMTD = cIFBusinessReportCLNBPMTD;
		}

		void setCIFBusinessReportCLNBPtQTD(String cIFBusinessReportCLNBPtQTD) {
			CIFBusinessReportCLNBPtQTD = cIFBusinessReportCLNBPtQTD;
		}

		void setCIFBusinessReportCLNBPYTD(String cIFBusinessReportCLNBPYTD) {
			CIFBusinessReportCLNBPYTD = cIFBusinessReportCLNBPYTD;
		}

		void setCIFBusinessReportThirteenMonthPercenAmtMTD(
                String cIFBusinessReportThirteenMonthPercenAmtMTD) {
			CIFBusinessReportThirteenMonthPercenAmtMTD = cIFBusinessReportThirteenMonthPercenAmtMTD;
		}

		void setCIFBusinessReportThirteenMonthPercenAmtQTD(
                String cIFBusinessReportThirteenMonthPercenAmtQTD) {
			CIFBusinessReportThirteenMonthPercenAmtQTD = cIFBusinessReportThirteenMonthPercenAmtQTD;
		}

		void setCIFBusinessReportThirteenMonthPercenAmtYTD(
                String cIFBusinessReportThirteenMonthPercenAmtYTD) {
			CIFBusinessReportThirteenMonthPercenAmtYTD = cIFBusinessReportThirteenMonthPercenAmtYTD;
		}

		void setCIFBusinessReportThirteenMonthPercenCountMTD(
                String cIFBusinessReportThirteenMonthPercenCountMTD) {
			CIFBusinessReportThirteenMonthPercenCountMTD = cIFBusinessReportThirteenMonthPercenCountMTD;
		}

		void setCIFBusinessReportThirteenMonthPercenCountQTD(
                String cIFBusinessReportThirteenMonthPercenCountQTD) {
			CIFBusinessReportThirteenMonthPercenCountQTD = cIFBusinessReportThirteenMonthPercenCountQTD;
		}

		void setCIFBusinessReportThirteenMonthPercenCountYTD(
                String cIFBusinessReportThirteenMonthPercenCountYTD) {
			CIFBusinessReportThirteenMonthPercenCountYTD = cIFBusinessReportThirteenMonthPercenCountYTD;
		}
		
	}
	
	class BDMBusinessReportValuesModel{
		private String BDMBusinessReportTotalBranches,BDMBusinessReportCIFName,
		BDMBusinessReportNBBudgetMTD,BDMBusinessReportNBBudgetQTD,
		BDMBusinessReportNBBudgetYTD,BDMBusinessReportPercentageAchievementMTD,
		BDMBusinessReportPercentageAchievementQTD,BDMBusinessReportPercentageAchievementYTD,
		BDMBusinessReportRatedAPremiumtMTD,BDMBusinessReportRatedAPremiumQTD,
		BDMBusinessReportRatedAPremiumYTD,BDMBusinessReportRenewalBudgettMTD,
		BDMBusinessReportRenewalBudgetQTD,BDMBusinessReportRenewalBudgetYTD,
		BDMBusinessReportRenewalAchievementMTD,BDMBusinessReportRenewalAchievementQTD,
		BDMBusinessReportRenewalAchievementYTD,BDMBusinessReportConnectLifeNopsMTD,
		BDMBusinessReportConnectLifeNopsQTD,BDMBusinessReportConnectLifeNopsYTD,
		BDMBusinessReportPercentageClNBpMTD,BDMBusinessReportPercentageClNBpQTD,
		BDMBusinessReportPercentageClNBpYTD,BDMBusinessReportPersistencyAmountPercentageMTD,
		BDMBusinessReportPersistencyAmountPercentageQTD,BDMBusinessReportPersistencyAmountPercentageYTD,
		BDMBusinessReportPersistencyCountPercentageMTD,BDMBusinessReportPersistencyCountPercentageQTD,
		BDMBusinessReportPersistencyCountPercentageYTD,BDMBusinessReportNumberOfMTD,
		BDMBusinessReportNumberOfQTD,BDMBusinessReportNumberOfYTD,
		BDMBusinessReportMDRTStatusMTD,BDMBusinessReportMDRTStatusQTD,
		BDMBusinessReportMDRTStatusYTD;


		String getBDMBusinessReportTotalBranches() {
			return BDMBusinessReportTotalBranches;
		}

		String getBDMBusinessReportCIFName() {
			return BDMBusinessReportCIFName;
		}

		String getBDMBusinessReportNBBudgetMTD() {
			return BDMBusinessReportNBBudgetMTD;
		}

		String getBDMBusinessReportNBBudgetQTD() {
			return BDMBusinessReportNBBudgetQTD;
		}

		String getBDMBusinessReportNBBudgetYTD() {
			return BDMBusinessReportNBBudgetYTD;
		}

		String getBDMBusinessReportPercentageAchievementMTD() {
			return BDMBusinessReportPercentageAchievementMTD;
		}

		String getBDMBusinessReportPercentageAchievementQTD() {
			return BDMBusinessReportPercentageAchievementQTD;
		}

		String getBDMBusinessReportPercentageAchievementYTD() {
			return BDMBusinessReportPercentageAchievementYTD;
		}

		String getBDMBusinessReportRatedAPremiumtMTD() {
			return BDMBusinessReportRatedAPremiumtMTD;
		}

		String getBDMBusinessReportRatedAPremiumQTD() {
			return BDMBusinessReportRatedAPremiumQTD;
		}

		String getBDMBusinessReportRatedAPremiumYTD() {
			return BDMBusinessReportRatedAPremiumYTD;
		}

		String getBDMBusinessReportRenewalBudgettMTD() {
			return BDMBusinessReportRenewalBudgettMTD;
		}

		String getBDMBusinessReportRenewalBudgetQTD() {
			return BDMBusinessReportRenewalBudgetQTD;
		}

		String getBDMBusinessReportRenewalBudgetYTD() {
			return BDMBusinessReportRenewalBudgetYTD;
		}

		String getBDMBusinessReportRenewalAchievementMTD() {
			return BDMBusinessReportRenewalAchievementMTD;
		}

		String getBDMBusinessReportRenewalAchievementQTD() {
			return BDMBusinessReportRenewalAchievementQTD;
		}

		String getBDMBusinessReportRenewalAchievementYTD() {
			return BDMBusinessReportRenewalAchievementYTD;
		}

		String getBDMBusinessReportConnectLifeNopsMTD() {
			return BDMBusinessReportConnectLifeNopsMTD;
		}

		String getBDMBusinessReportConnectLifeNopsQTD() {
			return BDMBusinessReportConnectLifeNopsQTD;
		}

		String getBDMBusinessReportConnectLifeNopsYTD() {
			return BDMBusinessReportConnectLifeNopsYTD;
		}

		String getBDMBusinessReportPercentageClNBpMTD() {
			return BDMBusinessReportPercentageClNBpMTD;
		}

		String getBDMBusinessReportPercentageClNBpQTD() {
			return BDMBusinessReportPercentageClNBpQTD;
		}

		String getBDMBusinessReportPercentageClNBpYTD() {
			return BDMBusinessReportPercentageClNBpYTD;
		}

		String getBDMBusinessReportPersistencyAmountPercentageMTD() {
			return BDMBusinessReportPersistencyAmountPercentageMTD;
		}

		String getBDMBusinessReportPersistencyAmountPercentageQTD() {
			return BDMBusinessReportPersistencyAmountPercentageQTD;
		}

		String getBDMBusinessReportPersistencyAmountPercentageYTD() {
			return BDMBusinessReportPersistencyAmountPercentageYTD;
		}

		String getBDMBusinessReportPersistencyCountPercentageMTD() {
			return BDMBusinessReportPersistencyCountPercentageMTD;
		}

		String getBDMBusinessReportPersistencyCountPercentageQTD() {
			return BDMBusinessReportPersistencyCountPercentageQTD;
		}

		String getBDMBusinessReportPersistencyCountPercentageYTD() {
			return BDMBusinessReportPersistencyCountPercentageYTD;
		}

		String getBDMBusinessReportNumberOfMTD() {
			return BDMBusinessReportNumberOfMTD;
		}

		String getBDMBusinessReportNumberOfQTD() {
			return BDMBusinessReportNumberOfQTD;
		}

		String getBDMBusinessReportNumberOfYTD() {
			return BDMBusinessReportNumberOfYTD;
		}

		void setBDMBusinessReportTotalBranches(
                String bDMBusinessReportTotalBranches) {
			BDMBusinessReportTotalBranches = bDMBusinessReportTotalBranches;
		}

		void setBDMBusinessReportCIFName(String bDMBusinessReportCIFName) {
			BDMBusinessReportCIFName = bDMBusinessReportCIFName;
		}

		void setBDMBusinessReportNBBudgetMTD(String bDMBusinessReportNBBudgetMTD) {
			BDMBusinessReportNBBudgetMTD = bDMBusinessReportNBBudgetMTD;
		}

		void setBDMBusinessReportNBBudgetQTD(String bDMBusinessReportNBBudgetQTD) {
			BDMBusinessReportNBBudgetQTD = bDMBusinessReportNBBudgetQTD;
		}

		void setBDMBusinessReportNBBudgetYTD(String bDMBusinessReportNBBudgetYTD) {
			BDMBusinessReportNBBudgetYTD = bDMBusinessReportNBBudgetYTD;
		}

		void setBDMBusinessReportPercentageAchievementMTD(
                String bDMBusinessReportPercentageAchievementMTD) {
			BDMBusinessReportPercentageAchievementMTD = bDMBusinessReportPercentageAchievementMTD;
		}

		void setBDMBusinessReportPercentageAchievementQTD(
                String bDMBusinessReportPercentageAchievementQTD) {
			BDMBusinessReportPercentageAchievementQTD = bDMBusinessReportPercentageAchievementQTD;
		}

		void setBDMBusinessReportPercentageAchievementYTD(
                String bDMBusinessReportPercentageAchievementYTD) {
			BDMBusinessReportPercentageAchievementYTD = bDMBusinessReportPercentageAchievementYTD;
		}

		void setBDMBusinessReportRatedAPremiumtMTD(
                String bDMBusinessReportRatedAPremiumtMTD) {
			BDMBusinessReportRatedAPremiumtMTD = bDMBusinessReportRatedAPremiumtMTD;
		}

		void setBDMBusinessReportRatedAPremiumQTD(
                String bDMBusinessReportRatedAPremiumQTD) {
			BDMBusinessReportRatedAPremiumQTD = bDMBusinessReportRatedAPremiumQTD;
		}

		void setBDMBusinessReportRatedAPremiumYTD(
                String bDMBusinessReportRatedAPremiumYTD) {
			BDMBusinessReportRatedAPremiumYTD = bDMBusinessReportRatedAPremiumYTD;
		}

		void setBDMBusinessReportRenewalBudgettMTD(
                String bDMBusinessReportRenewalBudgettMTD) {
			BDMBusinessReportRenewalBudgettMTD = bDMBusinessReportRenewalBudgettMTD;
		}

		void setBDMBusinessReportRenewalBudgetQTD(
                String bDMBusinessReportRenewalBudgetQTD) {
			BDMBusinessReportRenewalBudgetQTD = bDMBusinessReportRenewalBudgetQTD;
		}

		void setBDMBusinessReportRenewalBudgetYTD(
                String bDMBusinessReportRenewalBudgetYTD) {
			BDMBusinessReportRenewalBudgetYTD = bDMBusinessReportRenewalBudgetYTD;
		}

		void setBDMBusinessReportRenewalAchievementMTD(
                String bDMBusinessReportRenewalAchievementMTD) {
			BDMBusinessReportRenewalAchievementMTD = bDMBusinessReportRenewalAchievementMTD;
		}

		void setBDMBusinessReportRenewalAchievementQTD(
                String bDMBusinessReportRenewalAchievementQTD) {
			BDMBusinessReportRenewalAchievementQTD = bDMBusinessReportRenewalAchievementQTD;
		}

		void setBDMBusinessReportRenewalAchievementYTD(
                String bDMBusinessReportRenewalAchievementYTD) {
			BDMBusinessReportRenewalAchievementYTD = bDMBusinessReportRenewalAchievementYTD;
		}

		void setBDMBusinessReportConnectLifeNopsMTD(
                String bDMBusinessReportConnectLifeNopsMTD) {
			BDMBusinessReportConnectLifeNopsMTD = bDMBusinessReportConnectLifeNopsMTD;
		}

		void setBDMBusinessReportConnectLifeNopsQTD(
                String bDMBusinessReportConnectLifeNopsQTD) {
			BDMBusinessReportConnectLifeNopsQTD = bDMBusinessReportConnectLifeNopsQTD;
		}

		void setBDMBusinessReportConnectLifeNopsYTD(
                String bDMBusinessReportConnectLifeNopsYTD) {
			BDMBusinessReportConnectLifeNopsYTD = bDMBusinessReportConnectLifeNopsYTD;
		}

		void setBDMBusinessReportPercentageClNBpMTD(
                String bDMBusinessReportPercentageClNBpMTD) {
			BDMBusinessReportPercentageClNBpMTD = bDMBusinessReportPercentageClNBpMTD;
		}

		void setBDMBusinessReportPercentageClNBpQTD(
                String bDMBusinessReportPercentageClNBpQTD) {
			BDMBusinessReportPercentageClNBpQTD = bDMBusinessReportPercentageClNBpQTD;
		}

		void setBDMBusinessReportPercentageClNBpYTD(
                String bDMBusinessReportPercentageClNBpYTD) {
			BDMBusinessReportPercentageClNBpYTD = bDMBusinessReportPercentageClNBpYTD;
		}

		void setBDMBusinessReportPersistencyAmountPercentageMTD(
                String bDMBusinessReportPersistencyAmountPercentageMTD) {
			BDMBusinessReportPersistencyAmountPercentageMTD = bDMBusinessReportPersistencyAmountPercentageMTD;
		}

		void setBDMBusinessReportPersistencyAmountPercentageQTD(
                String bDMBusinessReportPersistencyAmountPercentageQTD) {
			BDMBusinessReportPersistencyAmountPercentageQTD = bDMBusinessReportPersistencyAmountPercentageQTD;
		}

		void setBDMBusinessReportPersistencyAmountPercentageYTD(
                String bDMBusinessReportPersistencyAmountPercentageYTD) {
			BDMBusinessReportPersistencyAmountPercentageYTD = bDMBusinessReportPersistencyAmountPercentageYTD;
		}

		void setBDMBusinessReportPersistencyCountPercentageMTD(
                String bDMBusinessReportPersistencyCountPercentageMTD) {
			BDMBusinessReportPersistencyCountPercentageMTD = bDMBusinessReportPersistencyCountPercentageMTD;
		}

		void setBDMBusinessReportPersistencyCountPercentageQTD(
                String bDMBusinessReportPersistencyCountPercentageQTD) {
			BDMBusinessReportPersistencyCountPercentageQTD = bDMBusinessReportPersistencyCountPercentageQTD;
		}

		void setBDMBusinessReportPersistencyCountPercentageYTD(
                String bDMBusinessReportPersistencyCountPercentageYTD) {
			BDMBusinessReportPersistencyCountPercentageYTD = bDMBusinessReportPersistencyCountPercentageYTD;
		}

		void setBDMBusinessReportNumberOfMTD(String bDMBusinessReportNumberOfMTD) {
			BDMBusinessReportNumberOfMTD = bDMBusinessReportNumberOfMTD;
		}

		void setBDMBusinessReportNumberOfQTD(String bDMBusinessReportNumberOfQTD) {
			BDMBusinessReportNumberOfQTD = bDMBusinessReportNumberOfQTD;
		}

		void setBDMBusinessReportNumberOfYTD(String bDMBusinessReportNumberOfYTD) {
			BDMBusinessReportNumberOfYTD = bDMBusinessReportNumberOfYTD;
		}

		String getBDMBusinessReportMDRTStatusMTD() {
			return BDMBusinessReportMDRTStatusMTD;
		}

		void setBDMBusinessReportMDRTStatusMTD(
                String bDMBusinessReportMDRTStatusMTD) {
			BDMBusinessReportMDRTStatusMTD = bDMBusinessReportMDRTStatusMTD;
		}

		String getBDMBusinessReportMDRTStatusQTD() {
			return BDMBusinessReportMDRTStatusQTD;
		}

		void setBDMBusinessReportMDRTStatusQTD(
                String bDMBusinessReportMDRTStatusQTD) {
			BDMBusinessReportMDRTStatusQTD = bDMBusinessReportMDRTStatusQTD;
		}

		String getBDMBusinessReportMDRTStatusYTD() {
			return BDMBusinessReportMDRTStatusYTD;
		}

		void setBDMBusinessReportMDRTStatusYTD(
                String bDMBusinessReportMDRTStatusYTD) {
			BDMBusinessReportMDRTStatusYTD = bDMBusinessReportMDRTStatusYTD;
		}
		
	}
	
	class BDMCreditLifeReportValuesModel{
		private String BDMCreditLifeReportRRYTDBusinessNOP,BDMCreditLifeReportRRYTDBusinessPremiumAmt,
		BDMCreditLifeReportHomeLoanPenetrationNOP,BDMCreditLifeReportHomeLoanPenetrationPremiumAmt,
		BDMCreditLifeReportPotentialLoansNOP,BDMCreditLifeReportPotentialLoansPremiumAmt,
		BDMCreditLifeReportCoveredIncShieldNOP,BDMCreditLifeReportCoveredIncShieldPremiumAmt,
		BDMCreditLifeReportPercentagePenetrationNOP,BDMCreditLifeReportPercentagePenetrationPremiumAmt;

		String getBDMCreditLifeReportRRYTDBusinessNOP() {
			return BDMCreditLifeReportRRYTDBusinessNOP;
		}

		String getBDMCreditLifeReportRRYTDBusinessPremiumAmt() {
			return BDMCreditLifeReportRRYTDBusinessPremiumAmt;
		}

		String getBDMCreditLifeReportHomeLoanPenetrationNOP() {
			return BDMCreditLifeReportHomeLoanPenetrationNOP;
		}

		String getBDMCreditLifeReportHomeLoanPenetrationPremiumAmt() {
			return BDMCreditLifeReportHomeLoanPenetrationPremiumAmt;
		}

		String getBDMCreditLifeReportPotentialLoansNOP() {
			return BDMCreditLifeReportPotentialLoansNOP;
		}

		String getBDMCreditLifeReportPotentialLoansPremiumAmt() {
			return BDMCreditLifeReportPotentialLoansPremiumAmt;
		}

		String getBDMCreditLifeReportCoveredIncShieldNOP() {
			return BDMCreditLifeReportCoveredIncShieldNOP;
		}

		String getBDMCreditLifeReportCoveredIncShieldPremiumAmt() {
			return BDMCreditLifeReportCoveredIncShieldPremiumAmt;
		}

		String getBDMCreditLifeReportPercentagePenetrationNOP() {
			return BDMCreditLifeReportPercentagePenetrationNOP;
		}

		String getBDMCreditLifeReportPercentagePenetrationPremiumAmt() {
			return BDMCreditLifeReportPercentagePenetrationPremiumAmt;
		}

		void setBDMCreditLifeReportRRYTDBusinessNOP(
                String bDMCreditLifeReportRRYTDBusinessNOP) {
			BDMCreditLifeReportRRYTDBusinessNOP = bDMCreditLifeReportRRYTDBusinessNOP;
		}

		void setBDMCreditLifeReportRRYTDBusinessPremiumAmt(
                String bDMCreditLifeReportRRYTDBusinessPremiumAmt) {
			BDMCreditLifeReportRRYTDBusinessPremiumAmt = bDMCreditLifeReportRRYTDBusinessPremiumAmt;
		}

		void setBDMCreditLifeReportHomeLoanPenetrationNOP(
                String bDMCreditLifeReportHomeLoanPenetrationNOP) {
			BDMCreditLifeReportHomeLoanPenetrationNOP = bDMCreditLifeReportHomeLoanPenetrationNOP;
		}

		void setBDMCreditLifeReportHomeLoanPenetrationPremiumAmt(
                String bDMCreditLifeReportHomeLoanPenetrationPremiumAmt) {
			BDMCreditLifeReportHomeLoanPenetrationPremiumAmt = bDMCreditLifeReportHomeLoanPenetrationPremiumAmt;
		}

		void setBDMCreditLifeReportPotentialLoansNOP(
                String bDMCreditLifeReportPotentialLoansNOP) {
			BDMCreditLifeReportPotentialLoansNOP = bDMCreditLifeReportPotentialLoansNOP;
		}

		void setBDMCreditLifeReportPotentialLoansPremiumAmt(
                String bDMCreditLifeReportPotentialLoansPremiumAmt) {
			BDMCreditLifeReportPotentialLoansPremiumAmt = bDMCreditLifeReportPotentialLoansPremiumAmt;
		}

		void setBDMCreditLifeReportCoveredIncShieldNOP(
                String bDMCreditLifeReportCoveredIncShieldNOP) {
			BDMCreditLifeReportCoveredIncShieldNOP = bDMCreditLifeReportCoveredIncShieldNOP;
		}

		void setBDMCreditLifeReportCoveredIncShieldPremiumAmt(
                String bDMCreditLifeReportCoveredIncShieldPremiumAmt) {
			BDMCreditLifeReportCoveredIncShieldPremiumAmt = bDMCreditLifeReportCoveredIncShieldPremiumAmt;
		}

		void setBDMCreditLifeReportPercentagePenetrationNOP(
                String bDMCreditLifeReportPercentagePenetrationNOP) {
			BDMCreditLifeReportPercentagePenetrationNOP = bDMCreditLifeReportPercentagePenetrationNOP;
		}

		void setBDMCreditLifeReportPercentagePenetrationPremiumAmt(
                String bDMCreditLifeReportPercentagePenetrationPremiumAmt) {
			BDMCreditLifeReportPercentagePenetrationPremiumAmt = bDMCreditLifeReportPercentagePenetrationPremiumAmt;
		}
		
	}
	
	
	class BDMServicingIndividualReportsValuesModel{
		private String oneSevenDaysEFTCount,oneSevenDaysPendingCasesCount,
		eightFifteenDaysEFTCount,eightFifteenDaysPendingCasesCount,
		fifteenTwentyDaysEFTCount,fifteenTwentyDaysPendingCasesCount,
		greaterThanTwentyOneDaysEFTCount,greaterThanTwentyOneDaysPendingCasesCount;

		
		String getOneSevenDaysEFTCount() {
			return oneSevenDaysEFTCount;
		}

		String getOneSevenDaysPendingCasesCount() {
			return oneSevenDaysPendingCasesCount;
		}

		String getEightFifteenDaysEFTCount() {
			return eightFifteenDaysEFTCount;
		}

		String getEightFifteenDaysPendingCasesCount() {
			return eightFifteenDaysPendingCasesCount;
		}

		String getFifteenTwentyDaysEFTCount() {
			return fifteenTwentyDaysEFTCount;
		}

		String getFifteenTwentyDaysPendingCasesCount() {
			return fifteenTwentyDaysPendingCasesCount;
		}

		String getGreaterThanTwentyOneDaysEFTCount() {
			return greaterThanTwentyOneDaysEFTCount;
		}

		String getGreaterThanTwentyOneDaysPendingCasesCount() {
			return greaterThanTwentyOneDaysPendingCasesCount;
		}

		void setOneSevenDaysEFTCount(String oneSevenDaysEFTCount) {
			this.oneSevenDaysEFTCount = oneSevenDaysEFTCount;
		}

		void setOneSevenDaysPendingCasesCount(
                String oneSevenDaysPendingCasesCount) {
			this.oneSevenDaysPendingCasesCount = oneSevenDaysPendingCasesCount;
		}

		void setEightFifteenDaysEFTCount(String eightFifteenDaysEFTCount) {
			this.eightFifteenDaysEFTCount = eightFifteenDaysEFTCount;
		}

		void setEightFifteenDaysPendingCasesCount(
                String eightFifteenDaysPendingCasesCount) {
			this.eightFifteenDaysPendingCasesCount = eightFifteenDaysPendingCasesCount;
		}

		void setFifteenTwentyDaysEFTCount(String fifteenTwentyDaysEFTCount) {
			this.fifteenTwentyDaysEFTCount = fifteenTwentyDaysEFTCount;
		}

		void setFifteenTwentyDaysPendingCasesCount(
                String fifteenTwentyDaysPendingCasesCount) {
			this.fifteenTwentyDaysPendingCasesCount = fifteenTwentyDaysPendingCasesCount;
		}

		void setGreaterThanTwentyOneDaysEFTCount(
                String greaterThanTwentyOneDaysEFTCount) {
			this.greaterThanTwentyOneDaysEFTCount = greaterThanTwentyOneDaysEFTCount;
		}

		void setGreaterThanTwentyOneDaysPendingCasesCount(
                String greaterThanTwentyOneDaysPendingCasesCount) {
			this.greaterThanTwentyOneDaysPendingCasesCount = greaterThanTwentyOneDaysPendingCasesCount;
		}	
	}
	
	private void hideCIFBusinessReport() {
		tblPerformanceReportsCIFBusinessReport.setVisibility(View.GONE);
	}

	private void hideCIFServicingReport() {
		tblPerformanceReportsCIFServicingReport.setVisibility(View.GONE);
	}

	private void hideBDMBusinessReport() {
		tblPerformanceReportsBDMBusinessReport.setVisibility(View.GONE);
	}

	private void hideBDMCreditLifeReport() {
		tblPerformanceReportsBDMCreditLifeReport.setVisibility(View.GONE);
	}

	private void hideBDMServicingIndividualReports() {
		tblPerformanceReportsBDMServicingIndividualReports.setVisibility(View.GONE);
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
	public void downLoadData() {
		// TODO Auto-generated method stub
		taskPerformanceReport = new DownloadFileAsyncPerformanceReport();
		startDownloadPerformanceReport();
	}
	
}
