package sbilife.com.pointofsale_bancaagency.reports.commonreports;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.FileOutputStream;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class PremiumPaymentReceiptActivity extends AppCompatActivity implements ServiceHits.DownLoadData {

    private final String METHOD_NAME_PPR = "getPaymentAckPDF";
    private Context mContext;
    private CommonMethods mCommonMethods;
    private StorageUtils mStorageUtils;
    private DownloadFilePremiumPaymentReceiptAsync downloadFilePremiumPaymentReceiptAsync;
    private ProgressDialog mProgressDialog;
    private EditText ETPremiumPaymentReceiptProposalNo;
    private String strPPRProposalNumber = "";
    private ServiceHits service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_common_report_premium_payment_receipt);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        mCommonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        mCommonMethods.setApplicationToolbarMenu(this, "Premium Acknowledgement Receipt");
        mContext = this;
        ETPremiumPaymentReceiptProposalNo = findViewById(R.id.ETPremiumPaymentReceiptProposalNo);
        Button btnPremiumPaymentReceiptProposalNo = findViewById(R.id.btnPremiumPaymentReceiptProposalNo);

        btnPremiumPaymentReceiptProposalNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                strPPRProposalNumber = ETPremiumPaymentReceiptProposalNo.getText()
                        .toString();
                getPPRDetails();
            }
        });
    }

    private void getPPRDetails() {
        mCommonMethods.hideKeyboard(ETPremiumPaymentReceiptProposalNo, mContext);
        downloadFilePremiumPaymentReceiptAsync = new DownloadFilePremiumPaymentReceiptAsync();

        if (strPPRProposalNumber.equalsIgnoreCase("")) {
            mCommonMethods.showMessageDialog(mContext,
                    "Please Enter Proposal No..");
        } else {
            if (mCommonMethods.isNetworkConnected(mContext)) {
                service_hits();
            } else {
                mCommonMethods.showMessageDialog(mContext,
                        mCommonMethods.NO_INTERNET_MESSAGE);
            }
        }
    }

    private void service_hits() {
        String strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMPassword, strCIFBDMMObileNo;
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = mCommonMethods
                .setUserDetails(mContext);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

        service = new ServiceHits(mContext, METHOD_NAME_PPR, strPPRProposalNumber,
                strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();
    }

    @Override
    public void downLoadData() {
        downloadFilePremiumPaymentReceiptAsync = new DownloadFilePremiumPaymentReceiptAsync();
        downloadFilePremiumPaymentReceiptAsync.execute("demo");
    }

    @Override
    protected void onDestroy() {
        killTasks();
        super.onDestroy();
    }

    private void killTasks() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (downloadFilePremiumPaymentReceiptAsync != null) {
            downloadFilePremiumPaymentReceiptAsync.cancel(true);
        }
        if (service != null) {
            service.cancel(true);
        }
    }

    class DownloadFilePremiumPaymentReceiptAsync extends AsyncTask<String, String, String> {

        private String strPPRErrorCOde = "";
        private File pdfPPRFile;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext,
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

                            if (downloadFilePremiumPaymentReceiptAsync != null) {
                                downloadFilePremiumPaymentReceiptAsync.cancel(true);
                            }
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }
                    });

            mProgressDialog.setMax(100);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {
                String NAMESPACE = "http://tempuri.org/";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_PPR);
                request.addProperty("strProposalNo", strPPRProposalNumber.trim());

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

                String SOAP_ACTION_PPC = "http://tempuri.org/getPaymentAckPDF";
                androidHttpTranport.call(SOAP_ACTION_PPC, envelope);
                Object response = envelope.getResponse();
                String inputpolicylist = response.toString();

                if (!TextUtils.isEmpty(inputpolicylist)) {
                    byte[] byteArray = Base64.decode(inputpolicylist, Base64.DEFAULT);

                    pdfPPRFile = mStorageUtils.createFileToAppSpecificDir(mContext, strPPRProposalNumber + "_ppr.pdf");
                    FileOutputStream fileOutput = new FileOutputStream(pdfPPRFile);
                    fileOutput.write(byteArray);
                    // close the output stream when complete //
                    fileOutput.close();
                    strPPRErrorCOde = "success";
                } else {
                    strPPRErrorCOde = "0";
                }

            } catch (Exception e) {
                mProgressDialog.dismiss();
                strPPRErrorCOde = "0";
            }
            return null;

        }

        @Override
        protected void onPostExecute(String unused) {

            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (strPPRErrorCOde != null) {

                if (strPPRErrorCOde.equalsIgnoreCase("success")) {
                    mCommonMethods.showPDFFile(mContext, pdfPPRFile);
                    ETPremiumPaymentReceiptProposalNo.setText("");
                    mCommonMethods.showCentralToast(mContext, "Downloaded Successfully to Path " + pdfPPRFile.getAbsolutePath());
                    pdfPPRFile = null;
                } else {
                    mCommonMethods
                            .showMessageDialog(mContext,
                                    "Data is Not Available Please check Your Input");
                }
            } else {
                mCommonMethods.showMessageDialog(mContext,
                        "Server Not Responding,Try again..");
            }
        }
    }
}
