package sbilife.com.pointofsale_bancaagency;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;

import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sbilife.com.pointofsale_bancaagency.common.BIPdfMail;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SendSmsAsyncTask;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.needanalysis.NeedAnalysisActivity;
import sbilife.com.pointofsale_bancaagency.needanalysis.OthersProductListActivity;
import sbilife.com.pointofsale_bancaagency.new_bussiness.NewBusinessHomeGroupingActivity;

public class NeedAnalysisBIService {

    private ProgressDialog mProgressDialog;
    private static String UIN_NO;
    private final String NAMESPACE = "http://tempuri.org/";
    private final String URl = ServiceURL.SERVICE_URL;
    private File Graphical_NACBI_File;
    private File needAnalysisPathFile;


    private BIPdfMail objBIPdfMail;
    private File newFile;
    private String bi_path, name_of_person,
            QuatationNumber;
    private DatabaseHelper dbHelper;
    private String inputpolicylist;
    //private int flag = 0;
    private boolean uinflag = false;
    private Context context;
    private NA_CBI_bean na_cbi_bean;
    private String channelType, channelId;
    private CommonMethods mCommonMethods;
    private StorageUtils mStorageUtils;

    private String APPLICATION_MODE = "";
    private String retailUserType = "";

    private String strCIFBDMEmailId, strCIFBDMMObileNo ;

    public NeedAnalysisBIService(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
        mCommonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
    }

    public void serviceHit(final Context context, NA_CBI_bean na_cbi_beanObj,
                           File newFile, String needanalysis_path, String bi_path,
                           String name_of_person, String QuatationNumber, String APPLICATION_MODE) {
        this.context = context;
        uinflag = false;
        na_cbi_bean = na_cbi_beanObj;
        objBIPdfMail = new BIPdfMail();
        this.newFile = newFile;
        this.name_of_person = name_of_person;
        this.APPLICATION_MODE = APPLICATION_MODE;
        this.bi_path = bi_path;
        this.QuatationNumber = QuatationNumber;


        strCIFBDMEmailId = mCommonMethods.GetUserEmail(context);
        strCIFBDMMObileNo = mCommonMethods.GetUserMobile(context);

        na_cbi_bean.setSr_code(mCommonMethods.GetUserCode(context));
        na_cbi_bean.setSr_type(mCommonMethods.GetUserType(context));
        na_cbi_bean.setSr_email(strCIFBDMEmailId);
        na_cbi_bean.setSr_mobile(strCIFBDMMObileNo);



        if (na_cbi_bean.getSr_type().equals("CIF")
                || na_cbi_bean.getSr_type().equals("BDM")) {
            channelType = "Bancassurance";
            channelId = "2";
        } else if (na_cbi_bean.getSr_type().equals("CAG")){//for BAP/CAG/IMF
            channelType = "CAG";
            channelId = "CAG";
        } else {
            channelType = "AGENCY";
            channelId = "AGD";
        }

        mProgressDialog = new ProgressDialog(context,
                ProgressDialog.THEME_HOLO_LIGHT);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setTitle(Html.fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
        mProgressDialog.setMax(100);

        if (!(TextUtils.isEmpty(na_cbi_bean.getBi_inputVal()) && TextUtils.isEmpty(na_cbi_bean.getBi_outputVal()))) {

            if (OthersProductListActivity.URNNumber.equalsIgnoreCase("")
                    && OthersProductListActivity.groupName.equalsIgnoreCase("")) {
                retailUserType = "SBI/Retail";
                new UIN_NO_ServiceHit().execute(null, null, null);
            } else {
                retailUserType = "Other";
                UIN_NO = OthersProductListActivity.URNNumber;
                new Copy_PDF_ServiceHit().execute(null, null, null);
            }
        } else {
            gotoNeedAnalysisHomeDialog("Due to some data issue kindly regenerate the URN to process");
        }
    }

    private void gotoNeedAnalysisHomeDialog(String message) {

        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_with_ok_button);
            dialog.setCancelable(false);
            TextView text = dialog.findViewById(R.id.tv_title);
            text.setText(message);
            Button dialogButton = dialog.findViewById(R.id.bt_ok);
            dialogButton.setText("Ok");
            dialogButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();

                    Intent i = new Intent(context,
                            NewBusinessHomeGroupingActivity.class);
                    context.startActivity(i);
                    NeedAnalysisActivity.URN_NO = "";
                    OthersProductListActivity.URNNumber = "";
                    OthersProductListActivity.groupName = "";
                    ((AppCompatActivity) context).finish();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class UIN_NO_ServiceHit extends AsyncTask<String, String, String> {
        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                inputpolicylist = NeedAnalysisActivity.URN_NO;

                System.out.println(" Service : " + inputpolicylist);
                if (inputpolicylist != null) {
                    if (inputpolicylist.equals("Fail")
                            || inputpolicylist.equals(""))
                        UIN_NO = "Fail";
                    else {
                        if (Integer.parseInt(inputpolicylist) > 0
                                && inputpolicylist.length() == 10)
                            UIN_NO = inputpolicylist;
                        else
                            uinflag = true;
                    }
                } else
                    UIN_NO = "Fail";

            } catch (Exception e) {
                uinflag = true;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                if (!uinflag) {
                    showDialog("Click Ok to Sync details to server.");
                } else {
                    Toast.makeText(context, "Server Not responding!",
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(context,
                            NewBusinessHomeGroupingActivity.class);
                    context.startActivity(i);
                }
            } else {
                Toast.makeText(context, "Server Not responding!",
                        Toast.LENGTH_LONG).show();
                Intent i = new Intent(context,
                        NewBusinessHomeGroupingActivity.class);
                context.startActivity(i);
            }
        }
    }

    class Copy_PDF_ServiceHit extends AsyncTask<String, String, String> {
        private volatile boolean running = true;
        int result = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                // createPdf();
                // File mypath = new File(folder, PropserNumber +
                // "Proposalno_p02.pdf");
                File needanalysispath_graphical = mStorageUtils.createFileToAppSpecificDir(context,
                        NeedAnalysisActivity.URN_NO + "_NA_Graphical.pdf");

                Graphical_NACBI_File = mStorageUtils.createFileToAppSpecificDir(context, "Graphical_NACBI_"
                        + QuatationNumber + ".pdf");

                copyPdf(Graphical_NACBI_File,
                        needanalysispath_graphical.getPath(), bi_path);

                needAnalysisPathFile = mStorageUtils.createFileToAppSpecificDir(context,
                        NeedAnalysisActivity.URN_NO + "_NA.pdf");

                copyPdf(newFile, needAnalysisPathFile.getPath(), bi_path);

                result = 1;

            } catch (Exception e) {
                result = 0;
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {
                if (mCommonMethods.isNetworkConnected(context)) {
                            /*objBIPdfMail.MailPDF(na_cbi_bean.getCust_email(),
								na_cbi_bean, name_of_person, newFile,
								Graphical_NACBI_File, context, QuatationNumber);*/
                    new NA_CBI_ServiceHit().execute(null, null, null);

                } else {
                    Toast.makeText(context, "Please check your internet connection.",
                            Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(context, "Server Not responding!",
                        Toast.LENGTH_LONG).show();
                Intent i = new Intent(context,
                        NewBusinessHomeGroupingActivity.class);
                context.startActivity(i);
            }
        }
    }

    class NA_CBI_ServiceHit extends AsyncTask<String, String, String> {
        private volatile boolean running = true;
        int flag = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgressDialog != null) {
                mProgressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;
                if (OthersProductListActivity.URNNumber.equalsIgnoreCase("")
                        && OthersProductListActivity.groupName
                        .equalsIgnoreCase("")) {
                    String METHOD_NAME_NA_CBI_DETAILS = "saveSMRTNACBIDtls_Updated";
                    request = new SoapObject(NAMESPACE,
                            METHOD_NAME_NA_CBI_DETAILS);
                } else {
                    String METHOD_NAME_NA_CBI_OTHER_BANK_DETAILS = "saveSMRTNACBIDtls_otherbank";
                    request = new SoapObject(NAMESPACE,
                            METHOD_NAME_NA_CBI_OTHER_BANK_DETAILS);
                }

                request.addProperty("UNIQUE_REF_NO",
                        na_cbi_bean.getUnique_ref_no());
                request.addProperty("SR_CODE", na_cbi_bean.getSr_code());
                request.addProperty("SR_SR_CODE", na_cbi_bean.getSr_sr_code());
                request.addProperty("SR_TYPE", na_cbi_bean.getSr_type());
                request.addProperty("SR_SR_TYPE", na_cbi_bean.getSr_sr_type());
                request.addProperty("CUST_TITLE", na_cbi_bean.getCust_title());
                request.addProperty("CUST_FIRST_NAME",
                        na_cbi_bean.getCust_first_name());
                request.addProperty("CUST_MIDDLE_NAME",
                        na_cbi_bean.getCust_mid_name());
                request.addProperty("CUST_LAST_NAME",
                        na_cbi_bean.getCust_last_name());
                request.addProperty("PLAN_NAME", na_cbi_bean.getPlanName());
                request.addProperty("BASIC_SUM_ASSURED",
                        na_cbi_bean.getSumassured());
                request.addProperty("TOTAL_PREMIUM_AMOUNT",
                        na_cbi_bean.getPremium());
                request.addProperty("CUST__EMAIL", na_cbi_bean.getCust_email());
                request.addProperty("CUST__MOBILE",
                        na_cbi_bean.getCust_mobile());
                request.addProperty("SR_EMAIL", na_cbi_bean.getSr_email());
                request.addProperty("SR_MOBILE", na_cbi_bean.getSr_mobile());
                request.addProperty("NA_INPUT", na_cbi_bean.getNa_input());
                request.addProperty("NA_OUTPUT", na_cbi_bean.getNa_output());
                request.addProperty("uin_no", UIN_NO.trim());
                request.addProperty("Source", "SMRT_BANCA");

                request.addProperty("CHANNEL_TYPE", channelType);
                request.addProperty("CHANNEL_ID", channelId);
                request.addProperty("FREQUENCY", na_cbi_bean.getFrequency());
                request.addProperty("POLICY_TERM", na_cbi_bean.getPolicyTerm());
                request.addProperty("PREMIUM_PAYING_TERM",
                        na_cbi_bean.getPrem_paying_term());
                request.addProperty("PLAN_CODE", na_cbi_bean.getPlan_code());
                request.addProperty("LA_DOB", na_cbi_bean.getLA_dob());
                request.addProperty("PROPOSER_DOB",
                        na_cbi_bean.getProposer_dob());

                request.addProperty("BI_INPUT", na_cbi_bean.getBi_inputVal());
                request.addProperty("BI_OUTPUT", na_cbi_bean.getBi_outputVal());
                request.addProperty("Imgarray", "");
                request.addProperty("NAImgarray", "");


                if (!(OthersProductListActivity.URNNumber.equalsIgnoreCase("") && OthersProductListActivity.groupName
                        .equalsIgnoreCase(""))) {
                    request.addProperty("source_system", "Other Bank");
                }
                mCommonMethods.appendSecurityParams(context,request,strCIFBDMEmailId,strCIFBDMMObileNo);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // allowAllSSL();
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);

                if (OthersProductListActivity.URNNumber.equalsIgnoreCase("")
                        && OthersProductListActivity.groupName
                        .equalsIgnoreCase("")) {
                    String SOAP_ACTION_NA_CBI_DETAILS = "http://tempuri.org/saveSMRTNACBIDtls_Updated";
                    androidHttpTranport.call(SOAP_ACTION_NA_CBI_DETAILS,
                            envelope);
                } else {
                    String SOAP_ACTION_NA_CBI_OTHER_BANK_DETAILS = "http://tempuri.org/saveSMRTNACBIDtls_otherbank";
                    androidHttpTranport.call(
                            SOAP_ACTION_NA_CBI_OTHER_BANK_DETAILS, envelope);
                }

                SoapPrimitive sa = null;

                sa = (SoapPrimitive) envelope.getResponse();
                inputpolicylist = sa.toString();

                if (inputpolicylist.equalsIgnoreCase("1"))
                    flag = 1;
                else
                    flag = 0;

            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;

        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                if (flag == 1) {

                    if (OthersProductListActivity.URNNumber
                            .equalsIgnoreCase("")
                            && OthersProductListActivity.groupName
                            .equalsIgnoreCase("")) {

						/*if (!APPLICATION_MODE
								.equalsIgnoreCase("Manual")) {
							dbHelper.updateNA_CBI_UINNum(
									na_cbi_bean.getUnique_ref_no(), UIN_NO,
									"10",retailUserType);
							new UploadFilesBIService().execute();
						} else {

							dbHelper.updateNA_CBI_UINNum(
									na_cbi_bean.getUnique_ref_no(), UIN_NO,
									"1",retailUserType);

							gotoNeedAnalysisHomeDialog("Details Sync Succesfully");
						}*/

                        dbHelper.updateNA_CBI_UINNum(
                                na_cbi_bean.getUnique_ref_no(), UIN_NO,
                                "10", retailUserType);
                        new UploadFilesBIService().execute();

                    } else {

                        dbHelper.updateNA_CBI_UINNumOtherPolicyList(
                                na_cbi_bean.getUnique_ref_no(), UIN_NO, "10");
                        new UploadFilesBIService().execute();

						/*if (!APPLICATION_MODE
								.equalsIgnoreCase("Manual")) {

							dbHelper.updateNA_CBI_UINNumOtherPolicyList(
									na_cbi_bean.getUnique_ref_no(), UIN_NO, "10");

							new UploadFilesBIService().execute();
						} else {

							dbHelper.updateNA_CBI_UINNum(
									na_cbi_bean.getUnique_ref_no(), UIN_NO,
									"1",retailUserType);


							Toast.makeText(context, "Details Sync Succesfully",
									Toast.LENGTH_LONG).show();

							Intent i = new Intent(context,
									NewBusinessHomeGroupingActivity.class);
							context.startActivity(i);
							NeedAnalysisActivity.URN_NO = "";
							OthersProductListActivity.URNNumber = "";
							OthersProductListActivity.groupName = "";
							((Activity) context).finish();
						}*/
                    }

                    //sendSms();
                } else {

                    if (OthersProductListActivity.URNNumber
                            .equalsIgnoreCase("")
                            && OthersProductListActivity.groupName
                            .equalsIgnoreCase("")) {
                        dbHelper.updateNA_CBI_UINNum(
                                na_cbi_bean.getUnique_ref_no(), UIN_NO, "0", retailUserType);
                    } else {
                        dbHelper.updateNA_CBI_UINNumOtherPolicyList(
                                na_cbi_bean.getUnique_ref_no(), UIN_NO, "0");
                    }


                    gotoNeedAnalysisHomeDialog("Details not Synced.Kindly resync from Need Analysis DashBoard");
                }

				/**/

            } else {
                Toast.makeText(context, "Server Not responding!",
                        Toast.LENGTH_LONG).show();
                Intent i = new Intent(context,
                        NewBusinessHomeGroupingActivity.class);
                context.startActivity(i);
            }
        }

    }

    class UploadFilesBIService extends AsyncTask<String, String, String> {
        private volatile boolean running = true;
        private  final String SOAP_ACTION_UPLOAD_FILE_SERVICE_URN = "http://tempuri.org/UploadFile_URN";
        private  final String METHOD_NAME_UPLOAD_FILE_SERVICE_URN = "UploadFile_URN";

        int flag = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (mProgressDialog != null) {
                mProgressDialog.show();
            }

        }

        @Override
        protected String doInBackground(String... aurl) {

            try {
                byte[] BI_bytes = new CommonMethods().read(new File(bi_path));

                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_UPLOAD_FILE_SERVICE_URN);

                request.addProperty("f", Base64.encode(BI_bytes));
                request.addProperty("fileName", NeedAnalysisActivity.URN_NO
                        + "_BI.pdf");
                mCommonMethods.appendSecurityParams(context,request,strCIFBDMEmailId,strCIFBDMMObileNo);
                System.out.println("request.toString()1:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // allowAllSSL();
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);

                androidHttpTranport.call(SOAP_ACTION_UPLOAD_FILE_SERVICE_URN,
                        envelope);

                SoapPrimitive sa = null;

                sa = (SoapPrimitive) envelope.getResponse();
                inputpolicylist = sa.toString();

                if (inputpolicylist.equalsIgnoreCase("1"))
                    flag = 1;
                else
                    flag = 0;

            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;

        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                if (flag == 1) {

                    if (na_cbi_bean.getSr_type().equals("BAP")
                            || na_cbi_bean.getSr_type().equals("CAG")
                            || na_cbi_bean.getSr_type().equals("IMF")){
                        //for Agent/BAP/CAG/IMF

                        dbHelper.updateNA_CBI_UINNum(na_cbi_bean.getUnique_ref_no(), UIN_NO, "30",retailUserType);

                        new UploadCustomerPhotoService().execute();
                    }else {
                        dbHelper.updateNA_CBI_UINNum(
                                na_cbi_bean.getUnique_ref_no(), UIN_NO, "20", retailUserType);
                        new UploadFilesNAService().execute();
                    }
                } else {
                    dbHelper.updateNA_CBI_UINNum(
                            na_cbi_bean.getUnique_ref_no(), UIN_NO, "10", retailUserType);
                    gotoNeedAnalysisHomeDialog("Details not Synced.Kindly resync from Need Analysis DashBoard");
                }

            } else {
                dbHelper.updateNA_CBI_UINNum(na_cbi_bean.getUnique_ref_no(),
                        UIN_NO, "10", retailUserType);

                gotoNeedAnalysisHomeDialog("Details not Synced.Kindly resync from Need Analysis DashBoard");
            }
        }
    }

    class UploadFilesNAService extends AsyncTask<String, String, String> {
        private volatile boolean running = true;
        private  final String SOAP_ACTION_UPLOAD_FILE_SERVICE_URN = "http://tempuri.org/UploadFile_URN";
        private  final String METHOD_NAME_UPLOAD_FILE_SERVICE_URN = "UploadFile_URN";

        int flag = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgressDialog != null) {
                mProgressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... aurl) {

            try {
                byte[] NA_bytes = new CommonMethods().read(needAnalysisPathFile);
                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_UPLOAD_FILE_SERVICE_URN);

                request.addProperty("f", Base64.encode(NA_bytes));
                request.addProperty("fileName", NeedAnalysisActivity.URN_NO
                        + "_NA.pdf");
                mCommonMethods.appendSecurityParams(context,request,strCIFBDMEmailId,strCIFBDMMObileNo);
                System.out.println("request.toString():" + request.toString());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // allowAllSSL();
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);

                androidHttpTranport.call(SOAP_ACTION_UPLOAD_FILE_SERVICE_URN,
                        envelope);

                SoapPrimitive sa = null;

                sa = (SoapPrimitive) envelope.getResponse();
                inputpolicylist = sa.toString();

                if (inputpolicylist.equalsIgnoreCase("1"))
                    flag = 1;
                else
                    flag = 0;

            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;

        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                if (flag == 1) {

                    if (!APPLICATION_MODE
                            .equalsIgnoreCase("Manual")) {
                        dbHelper.updateNA_CBI_UINNum(
                                na_cbi_bean.getUnique_ref_no(), UIN_NO, "30", retailUserType);
                        new UploadCustomerPhotoService().execute();
                    } else {
                        dbHelper.updateNA_CBI_UINNum(
                                na_cbi_bean.getUnique_ref_no(), UIN_NO, "50", retailUserType);
                        new UploadProposerSignService().execute();
                    }


                } else {

                    dbHelper.updateNA_CBI_UINNum(
                            na_cbi_bean.getUnique_ref_no(), UIN_NO, "20", retailUserType);
                    gotoNeedAnalysisHomeDialog("Details not Synced.Kindly resync from Need Analysis DashBoard");
                }

            } else {
                dbHelper.updateNA_CBI_UINNum(na_cbi_bean.getUnique_ref_no(),
                        UIN_NO, "20", retailUserType);
                gotoNeedAnalysisHomeDialog("Details not Synced.Kindly resync from Need Analysis DashBoard");
            }
        }
    }

    class UploadCustomerPhotoService extends AsyncTask<String, String, String> {
        private volatile boolean running = true;
        private  final String SOAP_ACTION_UPLOAD_FILE_SERVICE_URN = "http://tempuri.org/UploadFile_URN";
        private  final String METHOD_NAME_UPLOAD_FILE_SERVICE_URN = "UploadFile_URN";

        int flag = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgressDialog != null) {
                mProgressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... aurl) {

            try {
                String photoName = NeedAnalysisActivity.URN_NO
                        + "_cust1Photo.jpg";
                File customerPhotoFile = mStorageUtils.createFileToAppSpecificDir(context, photoName);

                mCommonMethods.compressImage(customerPhotoFile,
                        NeedAnalysisActivity.URN_NO, "_cust1Photo.jpg",context);

                File customerCompressedPhotoFile = mStorageUtils.createFileToAppSpecificDir(context, photoName);

                /*//compression code added by bhalla
                CompressImage.compressImageGetBitmap(customerPhotoFile.getPath());*/

                byte[] custPhoto_bytes = new CommonMethods().read(customerCompressedPhotoFile);

                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_UPLOAD_FILE_SERVICE_URN);

                request.addProperty("f", Base64.encode(custPhoto_bytes));
                request.addProperty("fileName", photoName);
                mCommonMethods.appendSecurityParams(context,request,strCIFBDMEmailId,strCIFBDMMObileNo);

                System.out.println("request.toString():3" + request.toString());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // allowAllSSL();
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);

                androidHttpTranport.call(SOAP_ACTION_UPLOAD_FILE_SERVICE_URN,
                        envelope);

                SoapPrimitive sa = null;

                sa = (SoapPrimitive) envelope.getResponse();
                inputpolicylist = sa.toString();

                if (inputpolicylist.equalsIgnoreCase("1"))
                    flag = 1;
                else
                    flag = 0;

            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;

        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                if (flag == 1) {

                    if (na_cbi_bean.getPlanName().equalsIgnoreCase(
                            "Smart Humsafar")) {
                        dbHelper.updateNA_CBI_UINNum(
                                na_cbi_bean.getUnique_ref_no(), UIN_NO, "40", retailUserType);
                        new UploadProposerPhotoService().execute();
                    } else {
                        dbHelper.updateNA_CBI_UINNum(
                                na_cbi_bean.getUnique_ref_no(), UIN_NO, "50", retailUserType);
                        new UploadProposerSignService().execute();
                    }

                } else {

                    dbHelper.updateNA_CBI_UINNum(
                            na_cbi_bean.getUnique_ref_no(), UIN_NO, "30", retailUserType);
                    gotoNeedAnalysisHomeDialog("Details not Synced.Kindly resync from Need Analysis DashBoard");
                }

            } else {
                dbHelper.updateNA_CBI_UINNum(na_cbi_bean.getUnique_ref_no(),
                        UIN_NO, "30", retailUserType);
                gotoNeedAnalysisHomeDialog("Details not Synced.Kindly resync from Need Analysis DashBoard");
            }
        }
    }

    class UploadProposerPhotoService extends AsyncTask<String, String, String> {
        private volatile boolean running = true;
        private  final String SOAP_ACTION_UPLOAD_FILE_SERVICE_URN = "http://tempuri.org/UploadFile_URN";
        private  final String METHOD_NAME_UPLOAD_FILE_SERVICE_URN = "UploadFile_URN";

        int flag = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgressDialog != null) {
                mProgressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... aurl) {

            try {
                String photoName = NeedAnalysisActivity.URN_NO
                        + "_cust2Photo.jpg";
                File customerPhotoFile = mStorageUtils.createFileToAppSpecificDir(context, photoName);

                mCommonMethods.compressImage(customerPhotoFile,
                        NeedAnalysisActivity.URN_NO, "_cust2Photo.jpg",context);

                File customerCompressedPhotoFile = mStorageUtils.createFileToAppSpecificDir(context, photoName);

                /*//compression code added by bhalla
                CompressImage.compressImageGetBitmap(customerPhotoFile.getPath());*/

                byte[] custPhoto_bytes = new CommonMethods()
                        .read(customerCompressedPhotoFile);

                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_UPLOAD_FILE_SERVICE_URN);

                request.addProperty("f", Base64.encode(custPhoto_bytes));
                request.addProperty("fileName", photoName);
                mCommonMethods.appendSecurityParams(context,request,strCIFBDMEmailId,strCIFBDMMObileNo);
                System.out.println("request.toString():4" + request.toString());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // allowAllSSL();
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);

                androidHttpTranport.call(SOAP_ACTION_UPLOAD_FILE_SERVICE_URN,
                        envelope);

                SoapPrimitive sa = null;

                sa = (SoapPrimitive) envelope.getResponse();
                inputpolicylist = sa.toString();

                if (inputpolicylist.equalsIgnoreCase("1"))
                    flag = 1;
                else
                    flag = 0;

            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;

        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                if (flag == 1) {
                    dbHelper.updateNA_CBI_UINNum(
                            na_cbi_bean.getUnique_ref_no(), UIN_NO, "50", retailUserType);
                    new UploadProposerSignService().execute();

                } else {
                    dbHelper.updateNA_CBI_UINNum(
                            na_cbi_bean.getUnique_ref_no(), UIN_NO, "40", retailUserType);
                    gotoNeedAnalysisHomeDialog("Details not Synced.Kindly resync from Need Analysis DashBoard");
                }

            } else {
                dbHelper.updateNA_CBI_UINNum(na_cbi_bean.getUnique_ref_no(),
                        UIN_NO, "40", retailUserType);
                gotoNeedAnalysisHomeDialog("Details not Synced.Kindly resync from Need Analysis DashBoard");
            }
        }
    }

    class UploadProposerSignService extends AsyncTask<String, String, String> {
        private volatile boolean running = true;
        private  final String SOAP_ACTION_UPLOAD_FILE_SERVICE_URN = "http://tempuri.org/UploadFile_URN";
        private  final String METHOD_NAME_UPLOAD_FILE_SERVICE_URN = "UploadFile_URN";

        int flag = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgressDialog != null) {
                mProgressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... aurl) {

            try {
                String photoName = NeedAnalysisActivity.URN_NO
                        + "_cust1sign.png";
                // File customerPhotoFile = new File(folder, photoName);

                // new
                // CommonMethods().compressImageGetBitmap(customerPhotoFile,NeedAnalysisActivity.URN_NO,"_cust1sign.png");

                File customerCompressedPhotoFile = mStorageUtils.createFileToAppSpecificDir(context,
                        photoName);
                byte[] custSign_bytes = new CommonMethods()
                        .read(customerCompressedPhotoFile);

                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_UPLOAD_FILE_SERVICE_URN);

                request.addProperty("f", Base64.encode(custSign_bytes));
                request.addProperty("fileName", photoName);
                mCommonMethods.appendSecurityParams(context,request,strCIFBDMEmailId,strCIFBDMMObileNo);
                System.out.println("request.toString():5" + request.toString());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // allowAllSSL();
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);

                androidHttpTranport.call(SOAP_ACTION_UPLOAD_FILE_SERVICE_URN,
                        envelope);

                SoapPrimitive sa = null;

                sa = (SoapPrimitive) envelope.getResponse();
                inputpolicylist = sa.toString();

                if (inputpolicylist.equalsIgnoreCase("1"))
                    flag = 1;
                else
                    flag = 0;

            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;

        }

        @Override
        protected void onPostExecute(String unused) {

            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (running) {

                if (flag == 1) {

                    dbHelper.updateNA_CBI_UINNum(
                            na_cbi_bean.getUnique_ref_no(), UIN_NO, "1", retailUserType);
                    //Toast.makeText(context, "Details Sync Succesfully",Toast.LENGTH_LONG).show();

                    //email non mendate 29/11/2019 changes by rajan starts
                    String str_email = na_cbi_bean.getCust_email();
                    str_email = str_email == null ? "" : str_email;

                    if (str_email.equals("")){
                        sendSms(str_email);
                    } else {
                        sendSms(str_email);

                        objBIPdfMail.MailPDF(na_cbi_bean.getCust_email(), na_cbi_bean, name_of_person,
                            newFile, Graphical_NACBI_File, context, QuatationNumber,UIN_NO);
                    }
                    //email non mendate 29/11/2019 changes by rajan ends
                    //gotoNeedAnalysisHomeDialog("URN  : " + UIN_NO + "\n\n Details Sync Succesfully");

                } else {


                    dbHelper.updateNA_CBI_UINNum(
                            na_cbi_bean.getUnique_ref_no(), UIN_NO, "1", retailUserType);
                    Toast.makeText(context, "Details Sync Succesfully",
                            Toast.LENGTH_LONG).show();

                    Intent i = new Intent(context,
                            NewBusinessHomeGroupingActivity.class);
                    context.startActivity(i);
                    NeedAnalysisActivity.URN_NO = "";
                    OthersProductListActivity.URNNumber = "";
                    OthersProductListActivity.groupName = "";
                    ((AppCompatActivity) context).finish();
                }

            } else {


                dbHelper.updateNA_CBI_UINNum(na_cbi_bean.getUnique_ref_no(),
                        UIN_NO, "50", retailUserType);
                gotoNeedAnalysisHomeDialog("Details not Synced.Kindly resync from Need Analysis DashBoard");
            }
        }
    }

	/*class UploadThirdPartySignService extends AsyncTask<String, String, String> {
		private volatile boolean running = true;
		private  final String SOAP_ACTION_UPLOAD_FILE_SERVICE_URN = "http://tempuri.org/UploadFile_URN";
		private  final String METHOD_NAME_UPLOAD_FILE_SERVICE_URN = "UploadFile_URN";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = new ProgressDialog(context,
					ProgressDialog.THEME_HOLO_LIGHT);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog
					.setTitle(Html
							.fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
			mProgressDialog.setMax(100);
			mProgressDialog.show();
		}

		@Override
		protected String doInBackground(String... aurl) {

			try {
				String thirdyPartySignName = NeedAnalysisActivity.URN_NO
						+ "_thirdParty.png";
				String extStorageDirectory = Environment
						.getExternalStorageDirectory().toString();
				String direct = "/SBI-Smart Advisor";
				File folder = new File(extStorageDirectory + direct + "/");

				if (!folder.exists()) {
					folder.mkdirs();
				}

				File thirdyPartySignFile = new File(folder, thirdyPartySignName);
				byte[] custSign_bytes = new CommonMethods()
						.read(thirdyPartySignFile);

				running = true;

				SoapObject request = new SoapObject(NAMESPACE,
						METHOD_NAME_UPLOAD_FILE_SERVICE_URN);

				request.addProperty("f", Base64.encode(custSign_bytes));
				request.addProperty("fileName", thirdyPartySignName);

				System.out.println("request.toString():" + request.toString());

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);

				// allowAllSSL();
				mCommonMethods.TLSv12Enable();

				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);

				HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);

				androidHttpTranport.call(SOAP_ACTION_UPLOAD_FILE_SERVICE_URN,
						envelope);

				SoapPrimitive sa = null;

				sa = (SoapPrimitive) envelope.getResponse();
				inputpolicylist = sa.toString();

				if (inputpolicylist.equalsIgnoreCase("1"))
					flag = 1;
				else
					flag = 0;

			} catch (Exception e) {
				e.printStackTrace();

			}
			return null;

		}

		@Override
		protected void onProgressUpdate(String... progress) {
			Log.d("ANDRO_ASYNC", progress[0]);
		}

		@Override
		protected void onPostExecute(String unused) {
			if (mProgressDialog.isShowing())
				mProgressDialog.dismiss();

			if (running != false) {

				if (flag == 1) {

					String extStorageDirectory = Environment
							.getExternalStorageDirectory().toString();
					String direct = "/SBI-Smart Advisor";
					File folder = new File(extStorageDirectory + direct + "/");

					String appointeeSignName = NeedAnalysisActivity.URN_NO
							+ "_appointee.png";
					File appointeeSignFile = new File(folder, appointeeSignName);

					String lifeAssuredSignName = NeedAnalysisActivity.URN_NO
							+ "_cust2sign.png";
					File lifeAssuredSignFile = new File(folder,
							lifeAssuredSignName);

					if (appointeeSignFile.exists()) {
						dbHelper.updateNA_CBI_UINNum(
								na_cbi_bean.getUnique_ref_no(), UIN_NO, "70",retailUserType);
						new UploadAppointeeSignService().execute();
					} else if (lifeAssuredSignFile.exists()) {
						dbHelper.updateNA_CBI_UINNum(
								na_cbi_bean.getUnique_ref_no(), UIN_NO, "80",retailUserType);
						new UploadLifeAssuredSignService().execute();
					} else {
						dbHelper.updateNA_CBI_UINNum(
								na_cbi_bean.getUnique_ref_no(), UIN_NO, "1",retailUserType);
						new UploadAppointeeSignService().execute();
						Toast.makeText(context, "Details Sync Succesfully",
								Toast.LENGTH_LONG).show();
						Intent i = new Intent(context,
								NewBusinessHomeGroupingActivity.class);
						context.startActivity(i);
						NeedAnalysisActivity.URN_NO = "";
						OthersProductListActivity.URNNumber = "";
						OthersProductListActivity.groupName = "";
						((Activity) context).finish();
					}

				} else {

					dbHelper.updateNA_CBI_UINNum(
							na_cbi_bean.getUnique_ref_no(), UIN_NO, "60",retailUserType);
					Toast.makeText(context, "Details not Synced",
							Toast.LENGTH_LONG).show();
					Intent i = new Intent(context,
							NewBusinessHomeGroupingActivity.class);
					context.startActivity(i);
					NeedAnalysisActivity.URN_NO = "";
					OthersProductListActivity.URNNumber = "";
					OthersProductListActivity.groupName = "";
					((Activity) context).finish();
				}

			} else {
				dbHelper.updateNA_CBI_UINNum(na_cbi_bean.getUnique_ref_no(),
						UIN_NO, "60",retailUserType);
				Intent i = new Intent(context,
						NewBusinessHomeGroupingActivity.class);
				context.startActivity(i);
				NeedAnalysisActivity.URN_NO = "";
				OthersProductListActivity.URNNumber = "";
				OthersProductListActivity.groupName = "";
				((Activity) context).finish();
			}
		}
	}

	class UploadAppointeeSignService extends AsyncTask<String, String, String> {
		private volatile boolean running = true;
		private  final String SOAP_ACTION_UPLOAD_FILE_SERVICE_URN = "http://tempuri.org/UploadFile_URN";
		private  final String METHOD_NAME_UPLOAD_FILE_SERVICE_URN = "UploadFile_URN";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = new ProgressDialog(context,
					ProgressDialog.THEME_HOLO_LIGHT);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog
					.setTitle(Html
							.fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
			mProgressDialog.setMax(100);
			mProgressDialog.show();
		}

		@Override
		protected String doInBackground(String... aurl) {

			try {
				String appointeeSignName = NeedAnalysisActivity.URN_NO
						+ "_appointee.png";
				String extStorageDirectory = Environment
						.getExternalStorageDirectory().toString();
				String direct = "/SBI-Smart Advisor";
				File folder = new File(extStorageDirectory + direct + "/");

				if (!folder.exists()) {
					folder.mkdirs();
				}

				File thirdyPartySignFile = new File(folder, appointeeSignName);
				byte[] appointeeSign_bytes = new CommonMethods()
						.read(thirdyPartySignFile);

				running = true;

				SoapObject request = new SoapObject(NAMESPACE,
						METHOD_NAME_UPLOAD_FILE_SERVICE_URN);

				request.addProperty("f", Base64.encode(appointeeSign_bytes));
				request.addProperty("fileName", appointeeSignName);

				System.out.println("request.toString():" + request.toString());

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);

				// allowAllSSL();
				mCommonMethods.TLSv12Enable();

				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);

				HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);

				androidHttpTranport.call(SOAP_ACTION_UPLOAD_FILE_SERVICE_URN,
						envelope);

				SoapPrimitive sa = null;

				sa = (SoapPrimitive) envelope.getResponse();
				String inputpolicylist = sa.toString();

				if (inputpolicylist.equalsIgnoreCase("1"))
					flag = 1;
				else
					flag = 0;

			} catch (Exception e) {
				e.printStackTrace();

			}
			return null;

		}

		@Override
		protected void onProgressUpdate(String... progress) {
			Log.d("ANDRO_ASYNC", progress[0]);
		}

		@Override
		protected void onPostExecute(String unused) {
			if (mProgressDialog.isShowing())
				mProgressDialog.dismiss();

			if (running != false) {

				if (flag == 1) {

					String extStorageDirectory = Environment
							.getExternalStorageDirectory().toString();
					String direct = "/SBI-Smart Advisor";
					File folder = new File(extStorageDirectory + direct + "/");

					String lifeAssuredSignName = NeedAnalysisActivity.URN_NO
							+ "_cust2sign.png";
					File lifeAssuredSignFile = new File(folder,
							lifeAssuredSignName);

					if (lifeAssuredSignFile.exists()) {
						dbHelper.updateNA_CBI_UINNum(
								na_cbi_bean.getUnique_ref_no(), UIN_NO, "80",retailUserType);
						new UploadLifeAssuredSignService().execute();
					} else {
						dbHelper.updateNA_CBI_UINNum(
								na_cbi_bean.getUnique_ref_no(), UIN_NO, "1",retailUserType);
						Toast.makeText(context, "Details Sync Succesfully",
								Toast.LENGTH_LONG).show();
						Intent i = new Intent(context,
								NewBusinessHomeGroupingActivity.class);
						context.startActivity(i);
						NeedAnalysisActivity.URN_NO = "";
						OthersProductListActivity.URNNumber = "";
						OthersProductListActivity.groupName = "";
						((Activity) context).finish();
					}

				} else {

					dbHelper.updateNA_CBI_UINNum(
							na_cbi_bean.getUnique_ref_no(), UIN_NO, "70",retailUserType);
					Toast.makeText(context, "Details not Synced",
							Toast.LENGTH_LONG).show();

					Intent i = new Intent(context,
							NewBusinessHomeGroupingActivity.class);
					context.startActivity(i);
					NeedAnalysisActivity.URN_NO = "";
					OthersProductListActivity.URNNumber = "";
					OthersProductListActivity.groupName = "";
					((Activity) context).finish();
				}

			} else {
				dbHelper.updateNA_CBI_UINNum(na_cbi_bean.getUnique_ref_no(),
						UIN_NO, "70",retailUserType);
				Intent i = new Intent(context,
						NewBusinessHomeGroupingActivity.class);
				context.startActivity(i);
				NeedAnalysisActivity.URN_NO = "";
				OthersProductListActivity.URNNumber = "";
				OthersProductListActivity.groupName = "";
				((Activity) context).finish();
			}
		}
	}

	class UploadLifeAssuredSignService extends
			AsyncTask<String, String, String> {
		private volatile boolean running = true;
		private  final String SOAP_ACTION_UPLOAD_FILE_SERVICE_URN = "http://tempuri.org/UploadFile_URN";
		private  final String METHOD_NAME_UPLOAD_FILE_SERVICE_URN = "UploadFile_URN";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = new ProgressDialog(context,
					ProgressDialog.THEME_HOLO_LIGHT);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog
					.setTitle(Html
							.fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
			mProgressDialog.setMax(100);
			mProgressDialog.show();
		}

		@Override
		protected String doInBackground(String... aurl) {

			try {
				String lifeAssuredSignName = NeedAnalysisActivity.URN_NO
						+ "_cust2sign.png";
				String extStorageDirectory = Environment
						.getExternalStorageDirectory().toString();
				String direct = "/SBI-Smart Advisor";
				File folder = new File(extStorageDirectory + direct + "/");

				if (!folder.exists()) {
					folder.mkdirs();
				}

				File lifeAssuredSignFile = new File(folder, lifeAssuredSignName);
				byte[] appointeeSign_bytes = new CommonMethods()
						.read(lifeAssuredSignFile);

				running = true;

				SoapObject request = new SoapObject(NAMESPACE,
						METHOD_NAME_UPLOAD_FILE_SERVICE_URN);

				request.addProperty("f", Base64.encode(appointeeSign_bytes));
				request.addProperty("fileName", lifeAssuredSignName);

				System.out.println("request.toString():" + request.toString());

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);

				// allowAllSSL();
				mCommonMethods.TLSv12Enable();

				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);

				HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);

				androidHttpTranport.call(SOAP_ACTION_UPLOAD_FILE_SERVICE_URN,
						envelope);

				SoapPrimitive sa = null;

				sa = (SoapPrimitive) envelope.getResponse();
				String inputpolicylist = sa.toString();

				if (inputpolicylist.equalsIgnoreCase("1"))
					flag = 1;
				else
					flag = 0;

			} catch (Exception e) {
				e.printStackTrace();

			}
			return null;

		}

		@Override
		protected void onProgressUpdate(String... progress) {
			Log.d("ANDRO_ASYNC", progress[0]);
		}

		@Override
		protected void onPostExecute(String unused) {
			if (mProgressDialog.isShowing())
				mProgressDialog.dismiss();

			if (running != false) {

				if (flag == 1) {
					dbHelper.updateNA_CBI_UINNum(
							na_cbi_bean.getUnique_ref_no(), UIN_NO, "1",retailUserType);
					Toast.makeText(context, "Details Sync Succesfully",
							Toast.LENGTH_LONG).show();

				} else {

					dbHelper.updateNA_CBI_UINNum(
							na_cbi_bean.getUnique_ref_no(), UIN_NO, "80",retailUserType);
					Toast.makeText(context, "Details not Synced",
							Toast.LENGTH_LONG).show();
				}

				Intent i = new Intent(context,
						NewBusinessHomeGroupingActivity.class);
				context.startActivity(i);
				NeedAnalysisActivity.URN_NO = "";
				OthersProductListActivity.URNNumber = "";
				OthersProductListActivity.groupName = "";
				((Activity) context).finish();

			} else {
				dbHelper.updateNA_CBI_UINNum(na_cbi_bean.getUnique_ref_no(),
						UIN_NO, "80",retailUserType);
				Intent i = new Intent(context,
						NewBusinessHomeGroupingActivity.class);
				context.startActivity(i);
				NeedAnalysisActivity.URN_NO = "";
				OthersProductListActivity.URNNumber = "";
				OthersProductListActivity.groupName = "";
				((Activity) context).finish();
			}
		}
	}*/

    // Send SMS
    private void sendSms(String isEmailID) {

        try {
            String str_message =

                    "Thank you. Your URN for requested plan '"
                            + na_cbi_bean.getPlanName() + "' is " + UIN_NO + ". "

                            + " T&C apply, refer sbilife.co.in. ~ SBI Life";

            if(mCommonMethods.isNetworkConnected(context)){
                SendSmsAsyncTask sendSmsAsyncTask = new SendSmsAsyncTask(context,na_cbi_bean.getCust_mobile(),str_message);
                sendSmsAsyncTask.execute(isEmailID);
            }else{
                mCommonMethods.showMessageDialog(context,mCommonMethods.NO_INTERNET_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "SMS sending failed.", Toast.LENGTH_SHORT)
                    .show();
        }
    }


    private void showDialog(String msg) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.window_agreement);
        dialog.setCanceledOnTouchOutside(false);
        TextView text = dialog.findViewById(R.id.textMessage);
        text.setText(msg);
        Button dialogButton = dialog.findViewById(R.id.idbtnagreement);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                String str_type = mCommonMethods.GetUserType(context);

                if (str_type.equalsIgnoreCase("BAP") || str_type.equalsIgnoreCase("CAG")
                        || str_type.equalsIgnoreCase("IMF")){
                    //for Agent/BAP/CAG/IMF
                    new NA_CBI_ServiceHit().execute(null, null, null);
                }else{
                    new Copy_PDF_ServiceHit().execute(null, null, null);
                }
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void copyPdf(File newFile, String needanalysis_path, String bi_path) {
        try {

            Document document = new Document();
            PdfCopy copy_tabular = new PdfCopy(document, new FileOutputStream(
                    newFile));

            document.open();

            PdfReader readerOne = new PdfReader(needanalysis_path);
            PdfReader readerTwo = new PdfReader(bi_path);

            copy_tabular.addDocument(readerOne);
            copy_tabular.addDocument(readerTwo);

            document.close();

            System.out.println("Copy succesfull " + newFile.toString());
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

    }

}
