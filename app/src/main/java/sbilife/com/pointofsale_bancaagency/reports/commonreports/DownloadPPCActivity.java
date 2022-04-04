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
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class DownloadPPCActivity extends AppCompatActivity implements ServiceHits.DownLoadData, View.OnClickListener {


    private final String NAMESPACE = "http://tempuri.org/";
    private final String URl = ServiceURL.SERVICE_URL;
    private final String METHOD_NAME_PPC_DOWNLOAD = "getPPCPDF";
    private final String METHOD_NAME_PPC = "getPPC_EasyAccess_mail";
    private Context mContext;
    private CommonMethods mCommonMethods;
    private StorageUtils mStorageUtils;
    private DownloadFileAsyncPPCDownload downloadFileAsyncPPCDownload;
    private DownloadFileAsyncPPC downloadFileAsyncPPC;

    private ProgressDialog mProgressDialog;

    private EditText edittextPPCPolicyNumber;
    private Spinner spinnerPPCPeriod;


    private String strPPCPolicyNumber = "";
    private String strPeriod = "";
    private ServiceHits service;
    private boolean isDownloadPPC = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_common_report_download_ppc);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        mCommonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        mCommonMethods.setApplicationToolbarMenu(this, "PPC");

        mContext = this;
        edittextPPCPolicyNumber = findViewById(R.id.edittextPPCPolicyNumber);
        spinnerPPCPeriod = findViewById(R.id.spinnerPPCPeriod);


        Calendar c = Calendar.getInstance();
        int CurrentYear = c.get(Calendar.YEAR);
        int CurrentMonth = c.get(Calendar.MONTH);

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

        mCommonMethods.fillSpinnerValue(mContext, spinnerPPCPeriod, periodList);

        Button buttonPPCOk = findViewById(R.id.buttonPPCOk);
        Button btnDownloadPPC = findViewById(R.id.btnDownloadPPC);
        buttonPPCOk.setOnClickListener(this);
        btnDownloadPPC.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnDownloadPPC:
                isDownloadPPC = true;
                getPPCDetails(METHOD_NAME_PPC_DOWNLOAD);
                break;
            case R.id.buttonPPCOk:
                isDownloadPPC = false;
                getPPCDetails(METHOD_NAME_PPC);
                break;
        }
    }

    private void getPPCDetails(String methodName) {
        strPPCPolicyNumber = edittextPPCPolicyNumber.getText()
                .toString();
        strPeriod = spinnerPPCPeriod.getSelectedItem().toString();

        String input = strPPCPolicyNumber + "," + strPeriod;
        mCommonMethods.hideKeyboard(edittextPPCPolicyNumber, mContext);
        downloadFileAsyncPPCDownload = new DownloadFileAsyncPPCDownload();
        downloadFileAsyncPPC = new DownloadFileAsyncPPC();

        if (strPPCPolicyNumber.equalsIgnoreCase("")) {
            mCommonMethods.showMessageDialog(mContext, "Please Enter Policy No..");
        } else if (strPeriod.equalsIgnoreCase("Select Period")) {
            mCommonMethods.showMessageDialog(mContext, "Please Select Period..");
        } else {

            if (mCommonMethods.isNetworkConnected(mContext)) {
                service_hits(methodName, input);
            } else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.NO_INTERNET_MESSAGE);
            }
        }
    }

    private void service_hits(String strServiceName, String input) {
        String strCIFBDMUserId;
        String strCIFBDMEmailId;
        String strCIFBDMPassword;
        String strCIFBDMMObileNo;
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = mCommonMethods
                .setUserDetails(mContext);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

        service = new ServiceHits(mContext, strServiceName, input,
                strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();

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

                        if (downloadFileAsyncPPCDownload != null) {
                            downloadFileAsyncPPCDownload.cancel(true);
                        }
                        if (downloadFileAsyncPPC != null) {
                            downloadFileAsyncPPC.cancel(true);

                        }
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                    }
                });

        mProgressDialog.setMax(100);
    }

    @Override
    public void downLoadData() {
        if (isDownloadPPC) {
            downloadFileAsyncPPCDownload = new DownloadFileAsyncPPCDownload();
            downloadFileAsyncPPCDownload.execute();
        } else {
            downloadFileAsyncPPC = new DownloadFileAsyncPPC();
            downloadFileAsyncPPC.execute();
        }
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

        if (downloadFileAsyncPPCDownload != null) {
            downloadFileAsyncPPCDownload.cancel(true);
        }

        if (downloadFileAsyncPPC != null) {
            downloadFileAsyncPPC.cancel(true);
        }

        if (service != null) {
            service.cancel(true);
        }
    }

    class DownloadFileAsyncPPCDownload extends AsyncTask<String, String, String> {

        private String strPPCErrorCOde = "";
        private File pdfPPCFile;

        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {
                SoapObject request;
                request = new SoapObject(NAMESPACE, METHOD_NAME_PPC_DOWNLOAD);
                request.addProperty("strPolicyNumber", strPPCPolicyNumber.trim());
                request.addProperty("strFinYear", strPeriod);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);

                String SOAP_ACTION_PPC = "http://tempuri.org/getPPCPDF";
                androidHttpTranport.call(SOAP_ACTION_PPC, envelope);

                Object response = envelope.getResponse();
                String inputpolicylist = response.toString();

                if (!TextUtils.isEmpty(inputpolicylist)) {
                    byte[] byteArray = Base64.decode(inputpolicylist, Base64.DEFAULT);

                    pdfPPCFile = mStorageUtils.createFileToAppSpecificDir(mContext,
                            strPPCPolicyNumber + "_ppc.pdf");
                    FileOutputStream fileOutput = new FileOutputStream(pdfPPCFile);
                    fileOutput.write(byteArray);
                    fileOutput.close();
                    strPPCErrorCOde = "success";
                } else {
                    strPPCErrorCOde = "0";
                }

            } catch (Exception e) {
                mProgressDialog.dismiss();
                strPPCErrorCOde = "0";
            }
            return null;

        }

        @Override
        protected void onPostExecute(String unused) {

            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (strPPCErrorCOde != null) {

                if (strPPCErrorCOde.equalsIgnoreCase("success")) {
                    mCommonMethods.showPDFFile(mContext, pdfPPCFile);
                    edittextPPCPolicyNumber.setText("");
                    spinnerPPCPeriod.setSelection(0);
                    mCommonMethods.showCentralToast(mContext, "Downloaded Successfully to Path " + pdfPPCFile.getAbsolutePath());
                    pdfPPCFile = null;
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

    class DownloadFileAsyncPPC extends AsyncTask<String, String, String> {

        private String strPPCErrorCOde = "";

        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {
                SoapObject request;
                request = new SoapObject(NAMESPACE, METHOD_NAME_PPC);
                request.addProperty("strPolicyNumber", strPPCPolicyNumber.trim());
                request.addProperty("strFinYear", strPeriod);//"2015-16"
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);

                String SOAP_ACTION_PPC = "http://tempuri.org/getPPC_EasyAccess_mail";
                androidHttpTranport.call(SOAP_ACTION_PPC, envelope);
                Object response = envelope.getResponse();
                System.out.println("response:" + response.toString());
                SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();

                String inputpolicylist = sa.toString();
                if (inputpolicylist.equalsIgnoreCase("1")) {
                    strPPCErrorCOde = "success";
                } else {
                    strPPCErrorCOde = "0";
                }

            } catch (Exception e) {
                mProgressDialog.dismiss();
                strPPCErrorCOde = "0";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String unused) {

            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (strPPCErrorCOde != null) {
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
            }
        }
    }
}