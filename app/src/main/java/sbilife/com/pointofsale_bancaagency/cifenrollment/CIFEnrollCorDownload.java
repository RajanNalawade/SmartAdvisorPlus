package sbilife.com.pointofsale_bancaagency.cifenrollment;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.ekyc.utilites.Base64;

public class CIFEnrollCorDownload extends AppCompatActivity {

    private Context mContext;
    private CommonMethods mCommonMethods;
    private StorageUtils mStorageUtils;

    private EditText edt_tcc_cor_download_urn;

    private ProgressDialog mProgressDialog;

    private final String NAMESPACE = "http://tempuri.org/";

    private final String METHOD_NAME_CIF_DOWNLOAD_COR = "downloadCOR_CIFenroll";

    private String str_urn_no = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_cifenroll_cor_download);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.cifenrollment_pf_activity_title);

        initialisation();
    }

    private void initialisation(){

        mContext = this;

        mCommonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();

        mCommonMethods.setApplicationToolbarMenu1(this, "CIF on Boarding");

        edt_tcc_cor_download_urn = findViewById(R.id.edt_tcc_cor_download_urn);
        Button btn_tcc_cor_download = findViewById(R.id.btn_tcc_cor_download);

        btn_tcc_cor_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!edt_tcc_cor_download_urn.getText().toString().replaceAll("\\s+", "").trim().equals("")){
                    //1st get exam details from server and save it to locally

                    str_urn_no = edt_tcc_cor_download_urn.getText().toString().replaceAll("\\s+", "").trim();

                    //validate urn 1st
                    //call download OCR docs
                    new DownloadCorDocuments().execute();

                }else{
                    mCommonMethods.showToast(mContext, "Please Enter URN Number");
                }

            }
        });
    }

    class DownloadCorDocuments extends AsyncTask<String, String, String>{

        private volatile boolean running = true;
        private byte[] rslt = null;
        private File mCoRFile = null;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext,
                    ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setTitle(Html
                            .fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            if (mCommonMethods.isNetworkConnected(mContext)) {
                try {
                    running = true;

                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_CIF_DOWNLOAD_COR);

                    request.addProperty("strNo", str_urn_no);

                    mCommonMethods.TLSv12Enable();

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);
                    envelope.dotNet = true;

                    new MarshalBase64().register(envelope); // serialization

                    envelope.setOutputSoapObject(request);

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL, 50000);
                    try {

                        androidHttpTranport.call(NAMESPACE + METHOD_NAME_CIF_DOWNLOAD_COR, envelope);
                        Object response = envelope.getResponse();
                        rslt = Base64.decode(response.toString());

                        String FILE_NAME = "COR_";
                        mCoRFile = mStorageUtils.createFileToAppSpecificDirCIF(mContext, FILE_NAME + str_urn_no+ ".pdf");

                        FileOutputStream fileOuputStream = null;
                        //if (mCoRFile.exists()){
                            try {
                                fileOuputStream = new FileOutputStream(mCoRFile);
                                fileOuputStream.write(rslt);

                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                if (fileOuputStream != null) {
                                    try {
                                        fileOuputStream.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        /*}else
                            mCommonMethods.showToast(mContext, "File Does not exists");*/

                    } catch (Exception e) {
                        e.printStackTrace();
                        running = false;
                        return mCommonMethods.WEEK_INTERNET_MESSAGE;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    running = false;
                    return mCommonMethods.WEEK_INTERNET_MESSAGE;
                }
            } else {
                return mCommonMethods.NO_INTERNET_MESSAGE;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                if (rslt != null){

                    running =false;

                    try {

                        mCommonMethods.openAllDocs(mContext, mCoRFile);

                    } catch (IOException e) {
                        mCommonMethods.showToast(mContext, "No PDF reader found to open this file.");
                    }

                }else{
                    mCommonMethods.showToast(mContext, "Invalid Entered Details");
                }

            }else{

                mCommonMethods.showToast(mContext, s);
            }
        }
    }
}
