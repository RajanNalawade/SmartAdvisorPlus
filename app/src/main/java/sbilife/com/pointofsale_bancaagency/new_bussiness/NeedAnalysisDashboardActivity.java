package sbilife.com.pointofsale_bancaagency.new_bussiness;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.NA_CBI_bean;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.BIPdfMail;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SendSmsAsyncTask;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.needanalysis.NeedAnalysisActivity;
import sbilife.com.pointofsale_bancaagency.needanalysis.OthersProductListActivity;

@SuppressLint("ClickableViewAccessibility")
public class NeedAnalysisDashboardActivity extends AppCompatActivity {

    private final String NAMESPACE = "http://tempuri.org/";
    private final String URl = ServiceURL.SERVICE_URL;

    private List<String> isSyncList = null;
    private DatabaseHelper dbhelper;
    private List<ProductBIBean> beans = null;
    private ProductBIBean obj_bean;

    private Context context;

    private ProgressDialog mProgressDialog;

    private DatabaseHelper dbHelper;
    private CommonMethods mCommonMethods;
    private StorageUtils mStorageUtils;

    private String retailUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.need_analysis_dashboard_layout);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        dbhelper = new DatabaseHelper(this);
        mCommonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();

        mCommonMethods.setApplicationToolbarMenu(this, "Need Analysis Dashboard");
        context = this;
        dbHelper = new DatabaseHelper(context);

        mProgressDialog = new ProgressDialog(context,
                ProgressDialog.THEME_HOLO_LIGHT);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog
                .setTitle(Html
                        .fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
        mProgressDialog.setMax(100);

        setDashBoardDetails();

    }

    private void setDashBoardDetails() {
        TextView txt_Error = findViewById(R.id.txt_Error);
        beans = dbhelper.GetNeedAnalysisDashboard(dbhelper.GetUserCode());
        List<HashMap<String, String>> hm_rp_details = new ArrayList<>();

        if (beans != null) {
            isSyncList = new ArrayList<>();
            String urns = "";
            int i = 1;
            for (ProductBIBean bean : beans) {

                System.out.println("bean.getTransactionMode = " + bean.getTransactionMode());

                HashMap<String, String> hm_rp_detail = new HashMap<>();
                isSyncList.add(bean.getSyncStatus());

                try {
                    String custTitle = ((bean.getCust_title() == null || bean.getCust_title().equals("")) ? "" : bean.getCust_title());
                    String firstName = ((bean.cust_first_name == null || bean.cust_first_name.equals("")) ? "" : bean.cust_first_name);
                    String middleName = ((bean.cust_mid_name == null || bean.cust_mid_name.equals("")) ? "" : bean.cust_mid_name);
                    String lastName = ((bean.cust_last_name == null || bean.cust_last_name.equals("")) ? "" : bean.cust_last_name);

                    hm_rp_detail.put("proposer_name", custTitle + " "
                            + firstName + " "
                            + middleName + " "
                            + lastName);
                    hm_rp_detail.put("plan_selected", bean.getPlanSelected());
                    hm_rp_detail.put("quotation_no", ((bean.getQuotationNo() == null || bean.getQuotationNo().equals("")) ? "" : bean.getQuotationNo()));
                    hm_rp_detail.put("proposal_date", ((bean.getProposalDate() == null || bean.getProposalDate().equals("")) ? "" : bean.getProposalDate()));
                    hm_rp_detail.put("proposer_mobile", ((bean.getMobileNo() == null || bean.getMobileNo().equals("")) ? "" : bean.getMobileNo()));
                    hm_rp_detail.put("proposer_email", ((bean.getEmail() == null || bean.getEmail().equals("")) ? "" : bean.getEmail()));


                    String syncStatus = ((bean.getSyncStatus() == null || bean.getSyncStatus().equals("")) ? "" : bean.getSyncStatus());

                    if (syncStatus.equalsIgnoreCase("1"))
                        hm_rp_detail.put("sync_status", "Sync");
                    else if (syncStatus.equalsIgnoreCase("0") && (!bean.getUinNo().equals("Fail")))
                        hm_rp_detail.put("sync_status", "Not Sync");
                    else if (syncStatus.equalsIgnoreCase(""))
                        hm_rp_detail.put("sync_status", "");
                    else
                        hm_rp_detail.put("sync_status", "Not Sync");

                    hm_rp_detail.put("uin_no", bean.getUinNo());
                    hm_rp_detail.put("group_name", bean.getNa_group());
                    hm_rp_detail.put("transaction_mode", bean.getTransactionMode());
                    urns += i + ") " + bean.getPlanSelected() + " - " + bean.getUinNo() + "\n";
                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                hm_rp_details.add(hm_rp_detail);
            }

            System.out.println("urns:" + urns);
            String[] str_from = {"proposer_name", "uin_no", "plan_selected", "quotation_no", "proposal_date", "proposer_mobile", "proposer_email", "sync_status", "group_name", "transaction_mode"};
            int[] in_to = {R.id.tv_proposername, R.id.tv_uin_no, R.id.tv_plan_selected, R.id.tv_quotation_no, R.id.tv_proposal_date, R.id.tv_proposer_mobile,
                    R.id.tv_proposer_email, R.id.tv_sync_status, R.id.tv_group_name, R.id.textviewTransactionMode};

            SimpleAdapter adapter = new SimpleAdapter(NeedAnalysisDashboardActivity.this,
                    hm_rp_details, R.layout.need_analysis_dashboard_listview, str_from,
                    in_to);
            ListView listView = findViewById(R.id.lv_need_analysis_dashboard_list);
            listView.setAdapter(adapter);

            listView.setOnItemLongClickListener(new OnItemLongClickListener() {
                public boolean onItemLongClick(AdapterView<?> parent, View v,
                                               int pos, long id) {
                    String isSync = isSyncList.get(pos);
                    obj_bean = beans.get(pos);
                    String syncStatus = ((obj_bean.getSyncStatus() == null || obj_bean.getSyncStatus().equals("")) ? "0" : obj_bean.getSyncStatus());


                    String naGroup = ((obj_bean.getNa_group() == null || obj_bean.getNa_group().equals("")) ? "" : obj_bean.getNa_group());
                    if (syncStatus.equalsIgnoreCase("1")) {
                        Toast.makeText(NeedAnalysisDashboardActivity.this, "Details already synced",
                                Toast.LENGTH_SHORT).show();
                    } else {

                        if (naGroup.equalsIgnoreCase("Other") && !syncStatus.equalsIgnoreCase("Sync")) {
                            retailUserType = "Other";
                            proceedalertDialog();
                        } else {
                            retailUserType = "SBI/Retail";
                            alertDialog(isSync);
                        }

                    }

                    return false;
                }
            });

            if (beans.size() > 0)
                txt_Error.setVisibility(View.GONE);
            else
                txt_Error.setVisibility(View.VISIBLE);
        } else
            txt_Error.setVisibility(View.VISIBLE);
    }


    // Alert dialog
    private void alertDialog(final String isSync) {


        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog
                .findViewById(R.id.txtalertheader);
        text.setText("Next Step...");

        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setText("Sync");
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                dialog.dismiss();
                if (mCommonMethods.isNetworkConnected(context)) {
                    switch (isSync) {
                        case "0":
                            new NA_CBI_ServiceHit().execute();

                            break;
                        case "10":
                            new UploadFilesBIService().execute();
                            break;
                        case "20":

                            new UploadFilesNAService().execute();
                            break;
                        case "30":

                            new UploadCustomerPhotoService().execute();
                            break;
                        case "40":

                            new UploadProposerPhotoService().execute();
                            break;
                        case "50":

                            new UploadProposerSignService().execute();
                            break;
                    }
                } else {
                    Toast.makeText(NeedAnalysisDashboardActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();

                }
         /* else if(isSync.equals("60")){

				if(new ConnectionDetector(NeedAnalysisDashboardActivity.this).isConnectingToInternet()){
					new UploadThirdPartySignService().execute();
				}
				else{
					Toast.makeText(NeedAnalysisDashboardActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();

				}
		  }
		  else if(isSync.equals("70")){

				if(new ConnectionDetector(NeedAnalysisDashboardActivity.this).isConnectingToInternet()){
					new UploadAppointeeSignService().execute();
				}
				else{
					Toast.makeText(NeedAnalysisDashboardActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();

				}
		  }*/

            }
        });
        dialog.show();


    }

    private void proceedalertDialog() {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog
                .findViewById(R.id.txtalertheader);
        text.setText("Next Step...");

        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setText("Proceed");
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                dialog.dismiss();
                Intent i = new Intent(NeedAnalysisDashboardActivity.this,
                        OthersProductListActivity.class);
                i.putExtra("NaInput", obj_bean.getNa_input());
                i.putExtra("NaOutput", obj_bean.getNa_output());
                i.putExtra("URNNumber", obj_bean.getUinNo());
                i.putExtra("group_name", obj_bean.getNa_group());
                startActivity(i);
            }
        });
        dialog.show();
    }

    private void sendSms(String strMailID) {

        try {
            String str_message =

                    "Thank you. Your URN for requested plan '"
                            + obj_bean.getPlanSelected() + "' is " + obj_bean.getUinNo() + ". "

                            + " T&C apply, refer sbilife.co.in. ~ SBI Life";

            if (mCommonMethods.isNetworkConnected(context)) {
                SendSmsAsyncTask sendSmsAsyncTask = new SendSmsAsyncTask(context, obj_bean.getMobileNo(), str_message);
                sendSmsAsyncTask.execute(strMailID);
            } else {
                mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "SMS sending failed.", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private class NA_CBI_ServiceHit extends AsyncTask<String, String, String> {
        private final String SOAP_ACTION_NA_CBI_DETAILS = "http://tempuri.org/saveSMRTNACBIDtls_Updated";
        private final String METHOD_NAME_NA_CBI_DETAILS = "saveSMRTNACBIDtls_Updated";
        int flag = -1;
        private volatile boolean running = true;
        private String inputpolicylist;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_NA_CBI_DETAILS);

                request.addProperty("UNIQUE_REF_NO",
                        obj_bean.getQuotationNo());
                request.addProperty("SR_CODE", obj_bean.getSr_code());
                request.addProperty("SR_SR_CODE", obj_bean.getSr_sr_code());
                request.addProperty("SR_TYPE", obj_bean.getSr_type());
                request.addProperty("SR_SR_TYPE", obj_bean.getSr_sr_type());
                request.addProperty("CUST_TITLE", obj_bean.getCust_title());
                request.addProperty("CUST_FIRST_NAME",
                        obj_bean.getCust_first_name());
                request.addProperty("CUST_MIDDLE_NAME",
                        obj_bean.getCust_mid_name());
                request.addProperty("CUST_LAST_NAME",
                        obj_bean.getCust_last_name());
                request.addProperty("PLAN_NAME", obj_bean.getPlanSelected());
                request.addProperty("BASIC_SUM_ASSURED",
                        obj_bean.getSumassured());
                request.addProperty("TOTAL_PREMIUM_AMOUNT",
                        obj_bean.getPremium());
                request.addProperty("CUST__EMAIL", obj_bean.getEmail());
                request.addProperty("CUST__MOBILE",
                        obj_bean.getMobileNo());
                request.addProperty("SR_EMAIL", obj_bean.getSr_email());
                request.addProperty("SR_MOBILE", obj_bean.getSr_mobile());
                request.addProperty("NA_INPUT", obj_bean.getNa_input());
                request.addProperty("NA_OUTPUT", obj_bean.getNa_output());
                request.addProperty("uin_no", obj_bean.getUinNo());
                request.addProperty("Source", "SMRT_BANCA");

                String channelType, channelId;
                String userType = mCommonMethods.GetUserType(context);
                if (userType.equals("CIF")
                        || userType.equals("BDM")) {
                    channelType = "Bancassurance";
                    channelId = "2";
                } else {
                    channelType = "AGENCY";
                    channelId = "AGD";
                }

                request.addProperty("CHANNEL_TYPE", channelType);
                request.addProperty("CHANNEL_ID", channelId);
                request.addProperty("FREQUENCY", obj_bean.getFrequency());
                request.addProperty("POLICY_TERM", obj_bean.getPolicyTerm());
                request.addProperty("PREMIUM_PAYING_TERM",
                        obj_bean.getPrem_paying_term());
                request.addProperty("PLAN_CODE", obj_bean.getPlan_code());
                request.addProperty("LA_DOB", obj_bean.getLA_dob());
                request.addProperty("PROPOSER_DOB",
                        obj_bean.getProposer_dob());

                request.addProperty("BI_INPUT", obj_bean.getBi_inputVal());
                request.addProperty("BI_OUTPUT", obj_bean.getBi_outputVal());
                request.addProperty("Imgarray", "");
                request.addProperty("NAImgarray", "");

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);

                androidHttpTranport.call(SOAP_ACTION_NA_CBI_DETAILS,
                        envelope);

                SoapPrimitive sa;

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

                    //sendSms();
                    dbHelper.updateNA_CBI_UINNum(
                            obj_bean.getQuotationNo(), obj_bean.uinNo, "10", retailUserType);
                    new UploadFilesBIService().execute();

					/*if (!obj_bean.getTransactionMode().equalsIgnoreCase("Manual")) {
						dbHelper.updateNA_CBI_UINNum(
								obj_bean.getQuotationNo(), obj_bean.uinNo, "10", retailUserType);
						new UploadFilesBIService().execute();
					} else {

						dbHelper.updateNA_CBI_UINNum(
								obj_bean.getQuotationNo(), obj_bean.uinNo, "1", retailUserType);

						Toast.makeText(context, "Details Sync Succesfully",
								Toast.LENGTH_LONG).show();
						setDashBoardDetails();
					}*/

                } else {

                    dbHelper.updateNA_CBI_UINNum(
                            obj_bean.getQuotationNo(), obj_bean.uinNo, "0", retailUserType);
                    Toast.makeText(context, "Details not Synced",
                            Toast.LENGTH_LONG).show();
                }


            }
        }
    }

    class UploadFilesBIService extends AsyncTask<String, String, String> {
        private final String SOAP_ACTION_UPLOAD_FILE_SERVICE_URN = "http://tempuri.org/UploadFile_URN";
        private final String METHOD_NAME_UPLOAD_FILE_SERVICE_URN = "UploadFile_URN";
        int flag = 0;
        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {

            try {

                String bi_path = obj_bean.getQuotationNo() + "BI.pdf";
                File mypath = mStorageUtils.createFileToAppSpecificDir(context, bi_path);

                byte[] BI_bytes = new CommonMethods().read(mypath);

                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_UPLOAD_FILE_SERVICE_URN);

                request.addProperty("f", Base64.encode(BI_bytes));
                request.addProperty("fileName", obj_bean.getUinNo() + "_BI.pdf");
                System.out.println("request.toString()1:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);

                androidHttpTranport.call(SOAP_ACTION_UPLOAD_FILE_SERVICE_URN,
                        envelope);

                SoapPrimitive sa;

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
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (running) {

                if (flag == 1) {
                    dbHelper.updateNA_CBI_UINNum(
                            obj_bean.getQuotationNo(), obj_bean.uinNo, "20", retailUserType);
                    new UploadFilesNAService().execute();
                } else {

                    dbHelper.updateNA_CBI_UINNum(
                            obj_bean.getQuotationNo(), obj_bean.uinNo, "10", retailUserType);
                    Toast.makeText(context, "Details not Synced",
                            Toast.LENGTH_LONG).show();
                }


            } else {
                dbHelper.updateNA_CBI_UINNum(
                        obj_bean.getQuotationNo(), obj_bean.uinNo, "10", retailUserType);
                Toast.makeText(context, "Details not Synced",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    class UploadFilesNAService extends AsyncTask<String, String, String> {
        private final String SOAP_ACTION_UPLOAD_FILE_SERVICE_URN = "http://tempuri.org/UploadFile_URN";
        private final String METHOD_NAME_UPLOAD_FILE_SERVICE_URN = "UploadFile_URN";
        int flag = 0;
        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {

            try {
                String needAnalysispath = obj_bean.getUinNo() + "_NA.pdf";
                File mypath = mStorageUtils.createFileToAppSpecificDir(context, needAnalysispath);

                byte[] NA_bytes = new CommonMethods().read(mypath);
                //byte[] BI_bytes = read(new File(bi_path));

                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_UPLOAD_FILE_SERVICE_URN);

                request.addProperty("f", Base64.encode(NA_bytes));
                request.addProperty("fileName", obj_bean.getUinNo() + "_NA.pdf");

                System.out.println("request.toString():" + request.toString());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);

                androidHttpTranport.call(SOAP_ACTION_UPLOAD_FILE_SERVICE_URN,
                        envelope);

                SoapPrimitive sa;

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
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (running) {

                if (flag == 1) {


                    if (!obj_bean.getTransactionMode().equalsIgnoreCase("Manual")) {
                        dbHelper.updateNA_CBI_UINNum(
                                obj_bean.getQuotationNo(), obj_bean.uinNo, "30", retailUserType);
                        new UploadCustomerPhotoService().execute();
                    } else {
                        dbHelper.updateNA_CBI_UINNum(
                                obj_bean.getQuotationNo(), obj_bean.uinNo, "50", retailUserType);
                        new UploadProposerSignService().execute();
                    }

                } else {
                    dbHelper.updateNA_CBI_UINNum(
                            obj_bean.getQuotationNo(), obj_bean.uinNo, "20", retailUserType);
                    Toast.makeText(context, "Details not Synced",
                            Toast.LENGTH_LONG).show();
                }

            } else {
                dbHelper.updateNA_CBI_UINNum(
                        obj_bean.getQuotationNo(), obj_bean.uinNo, "20", retailUserType);
                Toast.makeText(context, "Details not Synced",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    class UploadCustomerPhotoService extends AsyncTask<String, String, String> {
        private final String SOAP_ACTION_UPLOAD_FILE_SERVICE_URN = "http://tempuri.org/UploadFile_URN";
        private final String METHOD_NAME_UPLOAD_FILE_SERVICE_URN = "UploadFile_URN";
        int flag = 0;
        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {

            try {
                String photoName = obj_bean.getUinNo() + "_cust1Photo.jpg";
                File customerPhotoFile = mStorageUtils.createFileToAppSpecificDir(context, photoName);

                //compressImageGetBitmap(customerPhotoFile);

                mCommonMethods.compressImage(customerPhotoFile, obj_bean.getUinNo(), "_cust1Photo.jpg",context);

                File customerCompressedPhotoFile = mStorageUtils.createFileToAppSpecificDir(context, photoName);
                byte[] custPhoto_bytes = mCommonMethods.read(customerCompressedPhotoFile);

                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_UPLOAD_FILE_SERVICE_URN);

                request.addProperty("f", Base64.encode(custPhoto_bytes));
                request.addProperty("fileName", photoName);

                System.out.println("request.toString():" + request.toString());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);

                androidHttpTranport.call(SOAP_ACTION_UPLOAD_FILE_SERVICE_URN,
                        envelope);

                SoapPrimitive sa;

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
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (running) {

                if (flag == 1) {

                    if (obj_bean.getPlanSelected().equalsIgnoreCase("Smart Humsafar")) {
                        dbHelper.updateNA_CBI_UINNum(
                                obj_bean.getQuotationNo(), obj_bean.uinNo, "40", retailUserType);
                        new UploadProposerPhotoService().execute();
                    } else {

                        dbHelper.updateNA_CBI_UINNum(
                                obj_bean.getQuotationNo(), obj_bean.uinNo, "50", retailUserType);
                        new UploadProposerSignService().execute();
                    }

                } else {

                    dbHelper.updateNA_CBI_UINNum(
                            obj_bean.getQuotationNo(), obj_bean.uinNo, "30", retailUserType);
                    Toast.makeText(context, "Details not Synced",
                            Toast.LENGTH_LONG).show();
                }


            } else {
                dbHelper.updateNA_CBI_UINNum(
                        obj_bean.getQuotationNo(), obj_bean.uinNo, "30", retailUserType);
            }
        }
    }

    class UploadProposerPhotoService extends AsyncTask<String, String, String> {
        private final String SOAP_ACTION_UPLOAD_FILE_SERVICE_URN = "http://tempuri.org/UploadFile_URN";
        private final String METHOD_NAME_UPLOAD_FILE_SERVICE_URN = "UploadFile_URN";
        int flag = 0;
        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {

            try {
                String photoName = obj_bean.getUinNo() + "_cust2Photo.jpg";
                File customerPhotoFile = mStorageUtils.createFileToAppSpecificDir(context, photoName);

                mCommonMethods.compressImage(customerPhotoFile, obj_bean.getUinNo(), "_cust2Photo.jpg",context);

                File customerCompressedPhotoFile = mStorageUtils.createFileToAppSpecificDir(context, photoName);
                byte[] custPhoto_bytes = mCommonMethods.read(customerCompressedPhotoFile);

                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_UPLOAD_FILE_SERVICE_URN);

                request.addProperty("f", Base64.encode(custPhoto_bytes));
                request.addProperty("fileName", photoName);

                System.out.println("request.toString():" + request.toString());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);

                androidHttpTranport.call(SOAP_ACTION_UPLOAD_FILE_SERVICE_URN,
                        envelope);

                SoapPrimitive sa;

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
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (running) {

                if (flag == 1) {
                    dbHelper.updateNA_CBI_UINNum(
                            obj_bean.getQuotationNo(), obj_bean.uinNo, "50", retailUserType);
                    new UploadProposerSignService().execute();

                } else {
                    dbHelper.updateNA_CBI_UINNum(
                            obj_bean.getQuotationNo(), obj_bean.uinNo, "40", retailUserType);
                    Toast.makeText(context, "Details not Synced",
                            Toast.LENGTH_LONG).show();
                }


            } else {
                dbHelper.updateNA_CBI_UINNum(
                        obj_bean.getQuotationNo(), obj_bean.uinNo, "40", retailUserType);
                Toast.makeText(context, "Details not Synced",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    class UploadProposerSignService extends AsyncTask<String, String, String> {
        private final String SOAP_ACTION_UPLOAD_FILE_SERVICE_URN = "http://tempuri.org/UploadFile_URN";
        private final String METHOD_NAME_UPLOAD_FILE_SERVICE_URN = "UploadFile_URN";
        int flag = 0;
        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {

            try {
                String photoName = obj_bean.getUinNo() + "_cust1sign.png";
                File customerCompressedPhotoFile = mStorageUtils.createFileToAppSpecificDir(context, photoName);
                byte[] custSign_bytes = mCommonMethods.read(customerCompressedPhotoFile);

                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_UPLOAD_FILE_SERVICE_URN);

                request.addProperty("f", Base64.encode(custSign_bytes));
                request.addProperty("fileName", photoName);

                System.out.println("request.toString():" + request.toString());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);

                androidHttpTranport.call(SOAP_ACTION_UPLOAD_FILE_SERVICE_URN,
                        envelope);

                SoapPrimitive sa;

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
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (running) {
                if (flag == 1) {
                    String quotationNumber = obj_bean.getQuotationNo();

                    dbHelper.updateNA_CBI_UINNum(
                            quotationNumber, obj_bean.uinNo, "1", retailUserType);

					/*Toast.makeText(context, "Details Sync Succesfully",
							Toast.LENGTH_LONG).show();*/

                    BIPdfMail objBIPdfMail = new BIPdfMail();
                    NA_CBI_bean na_cbi_bean = new NA_CBI_bean();
                    na_cbi_bean.setCust_email(obj_bean.getEmail());
                    na_cbi_bean.setPlanName(obj_bean.getPlanSelected());

                    String name_of_person = obj_bean.getCust_title() + " "
                            + obj_bean.getCust_first_name() + " "
                            + obj_bean.getCust_mid_name() + " " + obj_bean.getCust_last_name();

                    File Graphical_NACBI_File = mStorageUtils.createFileToAppSpecificDir(context,
                            "Graphical_NACBI_" + quotationNumber + ".pdf");
                    File newFile = mStorageUtils.createFileToAppSpecificDir(context,
                            quotationNumber + "P01.pdf");

                    //email non mendate 29/11/2019 changes by rajan starts

                    String str_email = na_cbi_bean.getCust_email();
                    str_email = str_email == null ? "" : str_email;

                    if (str_email.equals("")) {
                        sendSms(str_email);
                    } else {
                        sendSms(str_email);

                        objBIPdfMail.MailPDF(na_cbi_bean.getCust_email(), na_cbi_bean, name_of_person,
                                newFile, Graphical_NACBI_File, context, quotationNumber, "Dashboard");
                    }

                    //email non mendate 29/11/2019 changes by rajan ends

                    NeedAnalysisActivity.URN_NO = "";
                    setDashBoardDetails();
                } else {
                    dbHelper.updateNA_CBI_UINNum(
                            obj_bean.getQuotationNo(), obj_bean.uinNo, "50", retailUserType);
                    Toast.makeText(context, "Details not Synced",
                            Toast.LENGTH_LONG).show();
                    NeedAnalysisActivity.URN_NO = "";
                    setDashBoardDetails();
                }
            } else {
                dbHelper.updateNA_CBI_UINNum(
                        obj_bean.getQuotationNo(), obj_bean.uinNo, "50", retailUserType);
                setDashBoardDetails();
            }
        }
    }


}
