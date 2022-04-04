package sbilife.com.pointofsale_bancaagency.reports.newbusiness;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import javax.mail.Message;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class NRIPaymentLinkActivity extends AppCompatActivity implements View.OnClickListener, ServiceHits.DownLoadData {

    private final String METHOD_NAME = "savePhysicalPropDet_u";
    private ProgressDialog mProgressDialog;
    private Context context;
    private CommonMethods commonMethods;
    private EditText etPolicyNumber, etTransactionAmount, etCustName,
            etCustMobile, etCustEmail, etConfirmCustEmail, etUserCode;
    private String policyNumber = "", transactionAmount = "", customerName = "",
            customerMobile = "", customerEmail = "", strCIFBDMUserId, userType = "", planCode = "",
            productSeriesNo = "", strUserType, strIACIFCode = "", strChannelType = "";
    private TextView tvUserCodeTitle;
    private ServiceHits service;
    private LinearLayout llUserCode;
    private DownloadPhysicalFormCashieringAsyncTask downloadPhysicalFormCashieringAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_nri_payment_link);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "NRI Payment Link");
        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");
        if (fromHome != null && fromHome.equalsIgnoreCase("N")) {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strUserType = intent.getStringExtra("strUserType");
        } else {
            CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods
                    .setUserDetails(context);
            strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
            strUserType = commonMethods.GetUserType(context);
        }


        etPolicyNumber = findViewById(R.id.etPolicyNumber);

        etTransactionAmount = findViewById(R.id.etTransactionAmount);
        etCustName = findViewById(R.id.etCustName);
        etCustMobile = findViewById(R.id.etCustMobile);
        etCustEmail = findViewById(R.id.etCustEmail);
        etConfirmCustEmail = findViewById(R.id.etConfirmCustEmail);
        tvUserCodeTitle = findViewById(R.id.tvUserCodeTitle);
        etUserCode = findViewById(R.id.etUserCode);
        llUserCode = findViewById(R.id.llUserCode);

        Button buttonOk = findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(this);
        llUserCode.setVisibility(View.GONE);

        if (strUserType.equalsIgnoreCase("Agent")) {
            strChannelType = "AGD";
            strIACIFCode = strCIFBDMUserId;
        } else if (strUserType.equalsIgnoreCase("CIF")) {
            strChannelType = "2";
            strIACIFCode = strCIFBDMUserId;
        } else if (strUserType.contentEquals("UM")
                || strUserType.contentEquals("BSM") || strUserType.contentEquals("DSM")
                || strUserType.equalsIgnoreCase("ASM") || strUserType.equalsIgnoreCase("RSM")) {
            llUserCode.setVisibility(View.VISIBLE);
            tvUserCodeTitle.setText("Agent Code");
            etUserCode.setHint("Enter Agent Code");
            strChannelType = "AGD";
        } else if (strUserType.equalsIgnoreCase("BDM")
                || strUserType.equalsIgnoreCase("AM") || strUserType.equalsIgnoreCase("SAM")
                || strUserType.equalsIgnoreCase("ZAM")) {
            llUserCode.setVisibility(View.VISIBLE);
            tvUserCodeTitle.setText("CIF Code");
            etUserCode.setHint("Enter CIF Code");
            strChannelType = "2";
        } else if (strUserType.equalsIgnoreCase("CAG")) {
            strChannelType = "CAG";
        }
        mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        mProgressDialog.setButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        if (downloadPhysicalFormCashieringAsyncTask != null) {
                            downloadPhysicalFormCashieringAsyncTask.cancel(true);
                        }

                        if (mProgressDialog != null) {
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }
                    }
                });

        mProgressDialog.setMax(100);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.buttonOk:
                commonMethods.hideKeyboard(etConfirmCustEmail, context);

                policyNumber = etPolicyNumber.getText().toString().trim();
                transactionAmount = etTransactionAmount.getText().toString().trim();
                customerName = etCustName.getText().toString().trim();
                customerMobile = etCustMobile.getText().toString().trim();
                customerEmail = etCustEmail.getText().toString().trim();
                String confirmEmail = etConfirmCustEmail.getText().toString().trim();


                if (commonMethods.isNetworkConnected(context)) {

                    if (TextUtils.isEmpty(policyNumber)) {
                        commonMethods.showMessageDialog(context, "Please Enter Proposal Number");
                    } else if (TextUtils.isEmpty(transactionAmount)) {
                        commonMethods.showMessageDialog(context, "Please Enter Transaction Amount");
                    } else if (TextUtils.isEmpty(customerName)) {
                        commonMethods.showMessageDialog(context, "Please Enter Customer Name");
                    } else if (TextUtils.isEmpty(customerMobile)) {
                        commonMethods.showMessageDialog(context, "Please Enter Customer Mobile");
                    } else if (TextUtils.isEmpty(customerEmail)) {
                        commonMethods.showMessageDialog(context, "Please Enter Customer Email");
                    } else if (!commonMethods.emailPatternValidation(etCustEmail, context)) {
                        commonMethods.showMessageDialog(context, "Please Enter Customer Email");
                    } else if (TextUtils.isEmpty(confirmEmail)) {
                        commonMethods.showMessageDialog(context, "Please Confirm Customer Email");
                    } else if (!customerEmail.equals(confirmEmail)) {
                        commonMethods.showMessageDialog(context, "Email Id Does Not Match");
                    } else if (strUserType.contentEquals("UM")
                            || strUserType.contentEquals("BSM") || strUserType.contentEquals("DSM")
                            || strUserType.equalsIgnoreCase("ASM")
                            || strUserType.equalsIgnoreCase("RSM")) {
                        strIACIFCode = etUserCode.getText().toString();
                        if (TextUtils.isEmpty(strIACIFCode)) {
                            commonMethods.showMessageDialog(context, "Please Enter Agent Code");
                    } else {
                            userType = commonMethods.GetUserType(context);
                            getPlanSeriresNo(policyNumber);
                        }
                    } else if (strUserType.equalsIgnoreCase("BDM")
                            || strUserType.equalsIgnoreCase("AM")
                            || strUserType.equalsIgnoreCase("SAM")
                            || strUserType.equalsIgnoreCase("ZAM")) {
                        strIACIFCode = etUserCode.getText().toString();
                        if (TextUtils.isEmpty(strIACIFCode)) {
                            commonMethods.showMessageDialog(context, "Please Enter CIF Code");
                        } else {
                            userType = commonMethods.GetUserType(context);
                            getPlanSeriresNo(policyNumber);
                    }
                    } else {
                        userType = commonMethods.GetUserType(context);
                        getPlanSeriresNo(policyNumber);
                    }
                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }
                break;
        }
    }

    private void service_hits(String input) {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        String strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        String strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();

        String strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
        service = new ServiceHits(context, METHOD_NAME, input,
                strCIFBDMUserId, strCIFBDMEmailId,
                strCIFBDMMObileNo, strCIFBDMPassword, this);
        service.execute();
    }

    @Override
    public void downLoadData() {
        downloadPhysicalFormCashieringAsyncTask = new DownloadPhysicalFormCashieringAsyncTask();
        downloadPhysicalFormCashieringAsyncTask.execute();
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

        if (downloadPhysicalFormCashieringAsyncTask != null) {
            downloadPhysicalFormCashieringAsyncTask.cancel(true);
        }

        if (service != null) {
            service.cancel(true);
        }
    }


    class DownloadPhysicalFormCashieringAsyncTask extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();

            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }


        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;

                /*public string savePhysicalPropDet(string strProposalNo,string strEmail,string strAmount,
                string strName,string strMobile,string strCode,string strProdCode,string strSeriesFlag)
                 */


                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("strProposalNo", policyNumber);
                request.addProperty("strEmail", customerEmail);
                request.addProperty("strAmount", transactionAmount);
                request.addProperty("strName", customerName);
                request.addProperty("strMobile", customerMobile);
                request.addProperty("strCode", strCIFBDMUserId);
                request.addProperty("strProdCode", planCode);
                request.addProperty("strSeriesFlag", productSeriesNo);
                request.addProperty("strIACIFCode", strIACIFCode);
                request.addProperty("strChannelType", strChannelType);

                commonMethods.TLSv12Enable();

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();


                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;
                androidHttpTranport.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();
                error = response.toString();
            } catch (Exception e) {
                mProgressDialog.dismiss();
                running = false;
            }
            return null;

        }


        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (running) {
                if (error.equalsIgnoreCase("1")) {
                    String msg = policyNumber + "%7C" + transactionAmount + "%7C" + "ConnectLife-NB%7C3";

                    String strurl = "https://sbilposservices.sbilife.co.in/PaymentRequest.aspx?REQ_CHANNEL=" + msg;

                    String strBody = "Dear Sir / Madam,<br><br>Greetings from SBI Life Insurance Company Ltd!!<br><br>At " +
                            "the outset we thank you for considering SBI Life for your insurance needs.<br><br>Please " +
                            "click on below link to make payment of Rs. " + transactionAmount + " against your " +
                            "Proposal Number " + policyNumber + ".<br><br>" + strurl + "<br><br>Warm Regards<br><br>SBI" +
                            " Life Insurance Company Limited";

                    String strToEmailId = customerEmail;

                    String strSubject1 = "Premium Payment Link for SBI Life Proposal Number " + policyNumber;

                    sendReciptMail(strToEmailId, strSubject1, strBody, policyNumber, msg);

                    //commonMethods.showMessageDialog(context, "Cashering Link Sent on Email Successfully");
                } else {
                    commonMethods.showMessageDialog(context, "Cashering Link Sending Failed");
                }
            } else {
                commonMethods.showMessageDialog(context, "Cashering Link Sending Failed");
            }
        }
    }

    private void getPlanSeriresNo(String proposalNumber) {

        String productCode = proposalNumber.substring(0, 2);
        System.out.println("productCode = " + productCode);

        if (productCode
                .equals(getString(R.string.sbi_life_smart_wealth_builder_code))) {
            planCode = "UPS2RP0";
            productSeriesNo = "3";
        } else if (productCode
                .equals(getString(R.string.sbi_life_smart_elite_code))) {
            planCode = "UPE3PGC0";
            productSeriesNo = "4";
        } else if (productCode
                .equals(getString(R.string.sbi_life_flexi_smart_plus_code))) {
            planCode = "VPLUSG";
            productSeriesNo = "";
        } else if (productCode
                .equals(getString(R.string.sbi_life_shubh_nivesh_code))) {
            planCode = "SSNEND";
            productSeriesNo = "4";

        } else if (productCode
                .equals(getString(R.string.sbi_life_smart_guaranteed_saving_plan_code))) {
            planCode = "1XSGS";
            productSeriesNo = "";
        } else if (productCode
                .equals(getString(R.string.sbi_life_smart_samriddhi_code))) {
            planCode = "2GSSM";
            productSeriesNo = "4";
        } else if (productCode
                .equals(getString(R.string.sbi_life_new_smart_samriddhi_code))) {
            planCode = "2PSSM";
            productSeriesNo = "1";
        } else if (productCode
                .equals(getString(R.string.sbi_life_saral_swadhan_plus_code))) {
            planCode = "SSWAP";
            productSeriesNo = "3";
        } else if (productCode
                .equals(getString(R.string.sbi_life_smart_champ_insurance_code))) {
            planCode = "SCI";
            productSeriesNo = "3";
        } else if (productCode
                .equalsIgnoreCase(getString(R.string.sbi_life_smart_money_back_gold_code))) {
            planCode = "GSMBP2";
            productSeriesNo = "3";

        } else if (productCode
                .equalsIgnoreCase(getString(R.string.sbi_life_smart_scholar_code))) {
            planCode = "UPCP30";
            productSeriesNo = "3";
        } else if (productCode
                .equalsIgnoreCase(getString(R.string.sbi_life_Retire_smart_code))) {
            planCode = "SRSRPA0";
            productSeriesNo = "2";
        } else if (productCode
                .equalsIgnoreCase(getString(R.string.sbi_life_smart_shield_code))) {
            planCode = "SHIELD2A";
            productSeriesNo = "7";

        } else if (productCode
                .equalsIgnoreCase(getString(R.string.sbi_life_smart_wealth_assure_code))) {
            planCode = "SSW0";
            productSeriesNo = "3";
        } else if (productCode
                .equalsIgnoreCase(getString(R.string.sbi_life_smart_power_insurance_code))) {
            planCode = "UPSPWICS";
            productSeriesNo = "2";
        } else if (productCode
                .equalsIgnoreCase(getString(R.string.sbi_life_saral_maha_anand_code))) {
            planCode = "UPMAP30";
        } else if (productCode
                .equalsIgnoreCase(getString(R.string.sbi_life_saral_shield_code))) {
            planCode = "SHIELD3A";
            productSeriesNo = "3";
        } else if (productCode
                .equalsIgnoreCase(getString(R.string.sbi_life_smart_money_planner_code))) {
            planCode = "SMP1";
            productSeriesNo = "3";

        } else if (productCode
                .equalsIgnoreCase(getString(R.string.sbi_life_smart_income_protect_code))) {
            planCode = "SIP";
            productSeriesNo = "4";
        } else if (productCode
                .equalsIgnoreCase(getString(R.string.sbi_life_saral_pension_code))) {
            planCode = "SRPEN";
            productSeriesNo = "3";
        } else if (productCode
                .equalsIgnoreCase(getString(R.string.sbi_life_smart_humsafar_code))) {
            planCode = "1WHS";
            productSeriesNo = "3";
        } else if (productCode
                .equalsIgnoreCase(getString(R.string.sbi_life_smart_swadhan_plus_code))) {
            planCode = "1ZSSPLP";
            productSeriesNo = "2";
        } else if (productCode
                .equalsIgnoreCase(getString(R.string.sbi_life_smart_women_advantage_code))) {
            planCode = "2CSWAG1";
        } else if (productCode
                .equals(getString(R.string.sbi_life_smart_privilege_code))) {
            planCode = "2BSPRP";
            productSeriesNo = "3";
        } else if (productCode
                .equals(getString(R.string.sbi_life_smart_bachat_code))) {
            planCode = "2DSBPA";
            productSeriesNo = "3";
        } else if (productCode
                .equals(getString(R.string.sbi_life_sampoorn_cancer_suraksha_code))) {
            planCode = "2ESCAP1";
            productSeriesNo = "3";
        } else if (productCode
                .equals(getString(R.string.sbi_life_poorn_suraksha_code))) {
            planCode = "2FPS";
            productSeriesNo = "3";
        } else if (productCode
                .equals(getString(R.string.sbi_life_saral_insure_wealth_plus_code))) {
            planCode = "2HSIPB";
            productSeriesNo = "2";
        } else if (productCode
                .equals(getString(R.string.sbi_life_smart_insure_wealth_plus_code))) {
            planCode = "2JSIPO";
            productSeriesNo = "2";
        } else if (productCode
                .equals(getString(R.string.sbi_life_smart_platina_assure_code))) {
            planCode = "2KSPA";
            productSeriesNo = "4";
        } else if (productCode
                .equals(getString(R.string.sbi_life_smart_future_choices_code))) {
            planCode = "2MSFCA";
            productSeriesNo = "1";
        } else if (productCode
                .equals(getString(R.string.sbi_life_eshield_next_code))) {
            planCode = "ESHNFPRP";
            productSeriesNo = "1";
        } else if (productCode
                .equals(getString(R.string.sbi_life_saral_jeevan_bima_code))) {
            planCode = "2QSJBSP";
            productSeriesNo = "1";
        } else if (productCode
                .equals(getString(R.string.sbi_life_annuity_plus_code))) {
            planCode = "APLUS";
            productSeriesNo = "11";
        } else if (productCode
                .equals(getString(R.string.sbi_life_saral_pension_new_code))) {
            planCode = "2RSPP";
            productSeriesNo = "1";
        } else if (productCode
                .equals(getString(R.string.sbi_life_corona_rakshak_code))) {
            //Corona Rakshak
            planCode = "SCR";
            productSeriesNo = "1";
        } else if (productCode
                .equals(getString(R.string.sbi_life_grameen_bima_code))) {
            //Grameen Beema
            planCode = "SCR";
            productSeriesNo = "2";
        } else if (productCode
                .equals(getString(R.string.sbi_life_Arogya_Shield_code))) {
            planCode = "2TASTSP";
            productSeriesNo = "1";
        }else if (productCode
                .equals(getString(R.string.sbi_life_smart_platina_plus_code))) {
            planCode = "2XSPPII";
            productSeriesNo = "1";
        }

        StringBuilder input = new StringBuilder();
        input.append(policyNumber + "," + transactionAmount + "," + customerName + ","
                + customerMobile + "," + customerEmail + "," + userType + "," + planCode
                + "," + productSeriesNo);
        System.out.println("input.toString() = " + input.toString());
        service_hits(input.toString());
    }

    private void sendReciptMail(String email, String subject, String messageBody, String ProposerNumber, String msg) {
        try {


            String str_payment_recipt_req = "";

            String subName = "Premium Payment Link for SBI Life Proposal Number "+ProposerNumber;
            String name = "Premium Payment Link";
            String fileName = "";
            str_payment_recipt_req = "{\n" +
                    "\"from\": {\n" +
                    "\"email\": \"noreply@sbilife.co.in\",\n" +
                    "\"name\": \""+name+"\"\n" +
                    "},\n" +
                    "\"subject\": \""+subject+"\",\n" +
                    "\"content\": [\n" +
                    "{\n" +
                    "\"type\": \"html\",\n" +
                    "\"value\": \""+messageBody+"\"\n" +
                    "}\n" +
                    "],\n" +
                   /* "\"attachments\": [\n" +
                    "{\n" +
                    "\"name\": \""+fileName+"\",\n" +
                    "\"content\": \""+fileName+"\"\n" +
                    "}\n" +
                    "],\n" +*/
                    "\"personalizations\": [\n" +
                    "{\n" +
                    "\"to\": [\n" +
                    "{\n" +
                    "\"email\": \""+email+"\",\n" +
                    "\"name\": \""+subName+"\"\n" +
                    "}\n" +
                    "]\n" +
                    "}\n" +
                    "]\n" +
                    "}";
            System.out.println("str_payment_recipt_req = " + str_payment_recipt_req);

            new SendRecieptMailTask(str_payment_recipt_req).execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class SendRecieptMailTask extends AsyncTask<Message, Void, Void> {
        private String msg, PremAck_response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();

            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        }

        public SendRecieptMailTask(String msg) {
            this.msg = msg;
            //commonMethods.showMessageDialog(context,msg);
        }

        @Override
        protected Void doInBackground(Message... messages) {

            try {
                //Transport.send(messages[0]);
                //HttpConnector1.setServerCert(getResources().openRawResource(R.raw.combocertificate_new));
                //HttpConnector1.setServerCert(getResources().openRawResource(R.raw.combohttpscertificate));

                //PremAck_response = HttpConnector1.getInstance().postData_meail("https://uat-api.sbilife.co.in/sbilife/uat/esb/Js/send_email_multi_att-v1/SendEmailMultiAtt",
               /* PremAck_response = HttpConnector1.getInstance().postData("https://uat-api.sbilife.co.in/sbilife/uat/esb/Js/send_email_multi_att-v1/SendEmailMultiAtt",
                        msg);*/
                //AppLogger.WriteIntoFile(PremAck_response);

                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, msg);
                Request request = new Request.Builder()
                        .url("https://api.pepipost.com/v5/mail/send")
                        .method("POST", body)
                        .addHeader("api_key", "346b6e44a7c599c3a6b3015b72a350cc")
                        .addHeader("Content-Type", "application/json")
                        .build();
                Response response = client.newCall(request).execute();
                PremAck_response = response.body().string();
                System.out.println("===== " + PremAck_response);


            } catch (Exception e) {
                e.printStackTrace();
                // AppLogger.WriteIntoFile(PremAck_response);
            }
            return null;

        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (commonMethods.isNetworkConnected(context)) {
                    commonMethods.showMessageDialog(context, "Cashering Link Sent on Email Successfully");
                } else {
                commonMethods.showMessageDialog(context,commonMethods.NO_INTERNET_MESSAGE);
            }
        }
    }


}




