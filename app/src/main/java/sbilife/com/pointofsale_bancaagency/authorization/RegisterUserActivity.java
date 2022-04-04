package sbilife.com.pointofsale_bancaagency.authorization;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.cifenrollment.phase1.CIFEnrollmentPFActivity;
import sbilife.com.pointofsale_bancaagency.clsLogin;
import sbilife.com.pointofsale_bancaagency.common.AppSharedPreferences;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.home.CarouselHomeActivity;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits.DownLoadData;

public class RegisterUserActivity extends AppCompatActivity implements OnClickListener, DownLoadData, OnItemSelectedListener {
    private final String NAMESPACE = "http://tempuri.org/";
    private final String URl = ServiceURL.SERVICE_URL;

    private final String METHOD_NAME_VALIDATE_USER_CODE = "validateCode_SMRT";

    private final String METHOD_NAME_SAVE_REG_DETAIL_SMRT = "saveRegDetail_SMRT";

    private final String METHOD_NAME_CHK_VERSION = "checkAppVersion";

    private final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private int mYear, mMonth, mDay, datecheck = 0;
    private final int DATE_DIALOG_ID = 1;

    private DownloadFileAsyncSaveRegistrationDetail taskAsyncSaveRegistrationDetail;
    private CheckAppVesrion taskCheckAppVesrion;

    private CommonMethods mCommonMethods;
    private Button buttonGenerateOTP, buttonSaveRegistrationUser;
    private Spinner spinnerRoleRegistrationUser;

    private EditText edittextUserCodeRegistrationUser, edittextMobileNumberRegistrationUser,
            edittextEmailRegistrationUser, edittextPinRegistrationUser,
            edittextConfirmPinRegistrationUser, edittextAgentPasswordRegistrationUser,
            edittextDOBRegistrationUser, edittextOTP;
    private String strID = "", strEmailId = "", strMobilNo = "", strType = "Select Role", strPIN = "",
            strConfirmPIN = "", strDOB = "", str_service_type = "", strOTP = "";

    private Context context;

    private ProgressDialog progressDialog = null;
    private DatabaseHelper dbHelper;
    private LinearLayout linearLayoutRegistrationUser;
    private String isNewUser = "";
    private ServiceHits service;
    private GenerateOTPAsyncTask generateOTPAsyncTask;
    private ValidateOTPAsyncTask validateOTPAsyncTask;

    private ImageView imageCaptchaRefresh;
    private TextView tvCaptchaCode;
    private EditText edittextCaptcha;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.register_user);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.registration_title);

        mCommonMethods = new CommonMethods();

        context = this;
        dbHelper = new DatabaseHelper(context);
        isNewUser = getIntent().getStringExtra("isNewUser");

        if (isNewUser != null) {

        } else {
            isNewUser = "";
        }

        final SharedPreferences preferences = getPreferences(MODE_PRIVATE);

        int UserId = dbHelper.GetUserId();

        if (UserId == 0 || isNewUser.equalsIgnoreCase("Y")) {
            //Display disclaimer if user is using application for the first time.
            //Check value in share preferences id it is null. If null display disclaimer.
            Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            buttonGenerateOTP = findViewById(R.id.buttonGenerateOTP);
            buttonSaveRegistrationUser = findViewById(R.id.buttonSaveRegistrationUser);

            spinnerRoleRegistrationUser = findViewById(R.id.spinnerRoleRegistrationUser);

            edittextUserCodeRegistrationUser = findViewById(R.id.edittextUserCodeRegistrationUser);
            edittextMobileNumberRegistrationUser = findViewById(R.id.edittextMobileNumberRegistrationUser);
            edittextEmailRegistrationUser = findViewById(R.id.edittextEmailRegistrationUser);
            edittextPinRegistrationUser = findViewById(R.id.edittextPinRegistrationUser);
            edittextConfirmPinRegistrationUser = findViewById(R.id.edittextConfirmPinRegistrationUser);
            edittextAgentPasswordRegistrationUser = findViewById(R.id.edittextAgentPasswordRegistrationUser);
            edittextDOBRegistrationUser = findViewById(R.id.edittextDOBRegistrationUser);
            edittextOTP = findViewById(R.id.edittextOTP);


            linearLayoutRegistrationUser = findViewById(R.id.linearLayoutRegistrationUser);
            LinearLayout layout_cif_on_boarding = findViewById(R.id.layout_cif_on_boarding);

            ImageButton imagebuttonCIFEnrollment = findViewById(R.id.imagebuttonCIFEnrollment);
            TextView textviewCIFEnrollment = findViewById(R.id.textviewCIFEnrollment);

            buttonGenerateOTP.setOnClickListener(this);
            buttonSaveRegistrationUser.setOnClickListener(this);
            layout_cif_on_boarding.setOnClickListener(this);
            imagebuttonCIFEnrollment.setOnClickListener(this);
            textviewCIFEnrollment.setOnClickListener(this);

            linearLayoutRegistrationUser.setVisibility(View.GONE);
            setSpinner();

            edittextDOBRegistrationUser.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    datecheck = 1;
                    showDateProgressDialog();
                    return false;
                }
            });

            String Disclaimer = preferences.getString("disclamer", null);
            if (Disclaimer == null && !isNewUser.equalsIgnoreCase("Y")) {
                String message = "SBI Life Smart Advisor is an Android Mobile Application specifically developed to provide SBI Life Insurance Advisors access to their key business data like Policy & Customer details, Product & Premium details News & Updates on SBI Life etc. \n\n" +

                        "Your use of SBI Life Smart Advisor application amounts to your unconditional acceptance of the terms and conditions of the said application for the use, access, etc. as stated herein below or as amended by SBI Life Insurance Company Ltd. (SBI Life) from time to time.\n\n" +
                        "Upon completion of download and installation of this application on your device you are given a limited non exclusive, non transferable, non assignable, non sub-licensable and revocable license to use the application.\n\n" +
                        "You unconditionally accept that you shall use SBI Life Smart Advisor absolutely at your own risk and SBI Life shall not be responsible for any losses or damages of whatsoever nature you may incur while using or downloading this application. \n\n" +
                        "In case of any technical problem, SBI Life will make its best endeavor to resolve the problem within shortest possible time and shall not be responsible for any technical failure or malfunctioning of the software/ hardware components of your mobile phone or delays of any kind. Failure of SBI Life to solve the technical problems, if any, shall not make SBI Life liable for any consequences whatsoever and in any manner whatsoever.  This service is strictly a free service aimed at providing some technical tools to you to enable you to access certain information on their Smartphones.\n\n" +
                        "The information, material, advices, suggestions, illustrations notifications, circulars etc. are collectively stated \"the content\" in this website/application. If the said content contains any mistakes, omissions, inaccuracies and typographical errors, etc. SBI Life takes no responsibility thereof.\n\n" +
                        "SBI Life makes no warranty or representation regarding any content provided through this application and disclaims its liabilities in respect thereof. Any action on your part on the basis of the said content is at your own risk and responsibility. \n\n" +
                        "You agree to keep the information received through the application as CONFIDENTIAL and shall not disclose the same to any third party without prior written consent of SBI Life. Any breach of this condition would lead to action as per the internal policy of the Company.\n\n" +
                        "The information received by you through this application is solely for use by you as an agent of SBI Life for solicitation of insurance and servicing of policies thereof. On discontinuation of your agency by way of termination, transfer or expiry of license, you shall discontinue use of the application immediately. Any breach of this condition or mis-use of information would lead to action as per the internal policy of the Company.\n\n" +
                        "SBI Life reserves its right to amend or update any part of the said content at any time as and when required at its sole discretion. The content of this application shall not be displayed or printed in any form in part or whole without the prior written approval of SBI Life.\n\n" +
                        "E-mail messages sent by SBI Life over the Internet cannot be guaranteed to be completely secure. The integrity of such messages cannot be guaranteed on the Internet and SBI Life will not be responsible for any damages incurred by users due to messages sent or received by them to and from SBI Life.\n\n" +
                        "SBI Life does not send emails from other than domain names - sbilife.co.in & sbi-life.com. Please do not respond to any mails other than email IDs with this domain name as same may be fraudulent/phishing emails. You agree that it shall be in your own interest and shall be your own duty to check the authenticity of  any message or  authenticity of sender of any message before you act on such message.\n\n" +
                        "You further agree that SBI Life may withdraw this facility or application at its sole discretion at any time without giving any advance notice whatsoever and you shall have no right or whatsoever to question such withdrawal of this facility. You also agree and understand that this facility shall always be subject to the relevant Indian Laws and their amendments and you undertake that you shall comply with the necessary regulatory requirements, if any, which are to be complied with by you for using this application.\n\n" +
                        "All the end customer�s use of this Mobile Application are governed by and shall be construed in accordance with the laws of India and any dispute as to its terms shall be submitted to the exclusive jurisdiction of the courts of Mumbai.\n\n" +
                        "Any dispute, difference and/ or claims arising out of in connection with or in relation to these terms and conditions, shall be settled by arbitration in accordance with the provisions of the Arbitration and Conciliation Act, 1996, by a sole arbitrator, whose appointment shall be made at the instance and discretion of SBI Life Insurance Limited. The arbitrator appointed shall be competent to decide whether any matter or dispute or difference referred to the arbitrator falls within the purview of arbitration as provided for in this clause and/or should be decided under the Arbitration and Conciliation Act, 1996. Any arbitration award granted shall be final and binding on the Parties. The venue and seat of the Arbitral Tribunal shall be at Mumbai.\n\n" +
                        "Insurance is the subject matter of solicitation. IRDA Registration no. 111.";

                final Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.disclaimerdialog);
                dialog.setCancelable(false);
                Button bt_yes = dialog.findViewById(R.id.bt_yes);
                Button bt_no = dialog.findViewById(R.id.bt_no);
                ((TextView) dialog.findViewById(R.id.tv_title)).setText("Disclaimer");
                DisplayMetrics dm = new DisplayMetrics();
                ((AppCompatActivity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
                int screenWidth = dm.widthPixels;
                int screenheight = dm.heightPixels;
                dialog.findViewById(R.id.scrollView_disclaimer).setMinimumHeight(screenheight - 120);
                dialog.findViewById(R.id.scrollView_disclaimer).setMinimumWidth(screenWidth - 200);
                ((TextView) dialog.findViewById(R.id.tv_message)).setText(message);
                bt_yes.setText("Accept");
                bt_no.setText("Decline");
                bt_yes.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //When user accepts the disclaimer store value in share preferences as true.
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("disclamer", "true");
                        editor.apply();
                        dialog.dismiss();
                    }
                });


                bt_no.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        setResult(RESULT_OK);
                        finish();

                    }
                });
                dialog.setOnKeyListener(new OnKeyListener() {

                    // @Override
                    public boolean onKey(DialogInterface arg0, int arg1,
                                         KeyEvent arg2) {
                        // TODO Auto-generated method stub
                        finish();
                        return false;
                    }
                });
                dialog.show();
            }

        } else {
            Intent i = new Intent(context, LoginUserActivity.class);
            startActivity(i);
            finish();

        }

        imageCaptchaRefresh = findViewById(R.id.imageCaptchaRefresh);
        tvCaptchaCode = findViewById(R.id.tvCaptchaCode);
        edittextCaptcha = findViewById(R.id.edittextCaptcha);
        imageCaptchaRefresh.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        Intent intent;

        switch (id) {
            case R.id.buttonGenerateOTP:
                if (mCommonMethods.isNetworkConnected(context)) {
                    mCommonMethods.hideKeyboard(edittextConfirmPinRegistrationUser,context);
                    strID = edittextUserCodeRegistrationUser.getText().toString().trim();
                    strDOB = mCommonMethods.formatDateForerver(edittextDOBRegistrationUser.getText().toString().trim());
                    strMobilNo = edittextMobileNumberRegistrationUser.getText().toString().trim();
                    strEmailId = edittextEmailRegistrationUser.getText().toString().trim();
                    strConfirmPIN = edittextConfirmPinRegistrationUser.getText().toString().trim();
                    strPIN = edittextPinRegistrationUser.getText().toString().trim();
                    if (validateUserCode() && validateFields() && mCommonMethods.mobileNumberPatternValidation(edittextMobileNumberRegistrationUser, context)
                            && mCommonMethods.emailPatternValidation(edittextEmailRegistrationUser, context)
                            && mCommonMethods.validateCaptcha(context,tvCaptchaCode,edittextCaptcha)) {
                        generateOTPAsyncTask = new GenerateOTPAsyncTask();
                        generateOTPAsyncTask.execute();
                    }
                } else {
                    mCommonMethods.showToast(context, mCommonMethods.NO_INTERNET_MESSAGE);
                }
                break;
            case R.id.buttonSaveRegistrationUser:
                if (mCommonMethods.isNetworkConnected(context)) {
                    mCommonMethods.hideKeyboard(edittextConfirmPinRegistrationUser,context);
                    strID = edittextUserCodeRegistrationUser.getText().toString().trim();
                    strDOB = mCommonMethods.formatDateForerver(edittextDOBRegistrationUser.getText().toString().trim());
                    strMobilNo = edittextMobileNumberRegistrationUser.getText().toString().trim();
                    strEmailId = edittextEmailRegistrationUser.getText().toString().trim();
                    strConfirmPIN = edittextConfirmPinRegistrationUser.getText().toString().trim();
                    strPIN = edittextPinRegistrationUser.getText().toString().trim();
                    strOTP = edittextOTP.getText().toString();
                    mCommonMethods.hideKeyboard(edittextConfirmPinRegistrationUser, context);

                    if (validateUserCode() && validateFields() && mCommonMethods.mobileNumberPatternValidation(edittextMobileNumberRegistrationUser, context)
                            && mCommonMethods.emailPatternValidation(edittextEmailRegistrationUser, context)
                            && mCommonMethods.validateCaptcha(context,tvCaptchaCode,edittextCaptcha)) {
                        /*taskAsyncSaveRegistrationDetail = new DownloadFileAsyncSaveRegistrationDetail();
                        taskAsyncSaveRegistrationDetail.execute();*/
                        if (TextUtils.isEmpty(strOTP)) {
                            mCommonMethods.showMessageDialog(context, "Please Enter Valid OTP");
                        } else {
                            validateOTPAsyncTask = new ValidateOTPAsyncTask();
                            validateOTPAsyncTask.execute();
                        }

                    }
                } else {
                    mCommonMethods.showToast(context, mCommonMethods.NO_INTERNET_MESSAGE);
                }
                break;
            case R.id.layout_cif_on_boarding:
                taskCheckAppVesrion = new CheckAppVesrion();
                taskCheckAppVesrion.execute();
                break;

            case R.id.textviewCIFEnrollment:
                taskCheckAppVesrion = new CheckAppVesrion();
                taskCheckAppVesrion.execute();
                break;

            case R.id.imagebuttonCIFEnrollment:
                taskCheckAppVesrion = new CheckAppVesrion();
                taskCheckAppVesrion.execute();
                break;
        }
    }

    private boolean validateUserCode() {
        boolean isvalid = true;
        if (strType.equalsIgnoreCase("Select Role")) {
            isvalid = false;
            mCommonMethods.showMessageDialog(context, "Select your role");
        } else if (strID.length() == 0) {
            isvalid = false;
            mCommonMethods.showMessageDialog(context, mCommonMethods.USER_CODE_ALERT);
        }

        return isvalid;
    }

    private boolean validateFields() {
        boolean isvalid = true;
        if (strMobilNo.length() == 0 || strEmailId.length() == 0
                || strPIN.length() == 0 || strConfirmPIN.length() == 0 || strDOB.length() == 0) {
            isvalid = false;
            mCommonMethods.showMessageDialog(context, mCommonMethods.ALL_FIELDS_REQUIRED_ALERT);
        } else if (strPIN.length() < 6) {
            String error = "Please enter 6 digit PIN number";
            isvalid = false;
            mCommonMethods.showMessageDialog(context, error);
        } else if (!strPIN.equalsIgnoreCase(strConfirmPIN)) {
            isvalid = false;
            mCommonMethods.showMessageDialog(context, "PIN does not match");
        }
        return isvalid;
    }

    private void updateDisplay(int year, int month, int day) {

        String y = String.valueOf(year);
        String m = String.valueOf(month + 1);
        String da = String.valueOf(day);

        m = mCommonMethods.getFullMonthName(m);
        String totaldate = da + "-" + m + "-" + y;

        if (datecheck == 1) {
            edittextDOBRegistrationUser.setText(totaldate);
        }
    }

    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay(mYear, mMonth, mDay);

        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
                String Message = "Loading Please wait...";
                progressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(true);

                progressDialog.setButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (taskAsyncSaveRegistrationDetail != null) {
                            taskAsyncSaveRegistrationDetail.cancel(true);
                        }

                        if (taskCheckAppVesrion != null)
                            taskCheckAppVesrion.cancel(true);

                        progressDialog.dismiss();
                    }
                });

                progressDialog.setMax(100);
                progressDialog.show();
                return progressDialog;

            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, R.style.datepickerstyle,
                        mDateSetListener, mYear, mMonth, mDay);

            default:
                return null;
        }
    }


    class DownloadFileAsyncSaveRegistrationDetail extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String inputpolicylist = "", strAuthUserErrorCOde = "";

        private String strTitle = "", strFame = "", strLName = "", strAddress = "", strStatus = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_SAVE_REG_DETAIL_SMRT);

                request.addProperty("strCode", strID);
                request.addProperty("strEmail", strEmailId);
                request.addProperty("strMobile", strMobilNo);
                request.addProperty("strPIN", strPIN);
                request.addProperty("strType", strType);
                /*request.addProperty("strPass", SimpleCrypto.encrypt("SBIL", "sbil"));*/
                request.addProperty("strPass", mCommonMethods.getStrAuth());
                request.addProperty("strDOB", strDOB);

                mCommonMethods.TLSv12Enable();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    String SOAP_ACTION_SAVE_REG_DETAIL_SMRT = "http://tempuri.org/saveRegDetail_SMRT";
                    androidHttpTranport.call(SOAP_ACTION_SAVE_REG_DETAIL_SMRT, envelope);

                    SoapPrimitive sa = null;
                    try {
                        sa = (SoapPrimitive) envelope.getResponse();
                        inputpolicylist = sa.toString();
                        ParseXML prsObj = new ParseXML();

                        /*<CIFPolicyList> <Table>
                         * <FAC_TITLE>Mr</FAC_TITLE>
                         * <FAC_FIRST_NM>SUJIT</FAC_FIRST_NM>
                         * <FAC_LAST_NM>KUMAR GHOSH</FAC_LAST_NM>
                         *  <APPROVED_STATUS>Y</APPROVED_STATUS>
                         *  <FAC_EMAIL>mukesh.kumar@sbilife.co.in</FAC_EMAIL>
                         *  <FAC_MOBILE>9434197714</FAC_MOBILE>
                         *  <FAC_DOB>1962-09-15T00:00:00-07:00</FAC_DOB>
                         *  <PIN_NO>1234</PIN_NO> </Table> </CIFPolicyList>*/

                        inputpolicylist = prsObj.parseXmlTag(inputpolicylist,
                                "CIFPolicyList");
                        inputpolicylist = new ParseXML().parseXmlTag(
                                inputpolicylist, "ScreenData");
                        strAuthUserErrorCOde = inputpolicylist;

                        if (strAuthUserErrorCOde == null) {
                            inputpolicylist = sa.toString();
                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "CIFPolicyList");
                            inputpolicylist = new ParseXML().parseXmlTag(
                                    inputpolicylist, "Table");

                            strTitle = prsObj.parseXmlTag(inputpolicylist, "FAC_TITLE");
                            strFame = prsObj.parseXmlTag(inputpolicylist, "FAC_FIRST_NM");
                            strLName = prsObj.parseXmlTag(inputpolicylist, "FAC_LAST_NM");
                            strEmailId = prsObj.parseXmlTag(inputpolicylist, "FAC_EMAIL");
                            strMobilNo = prsObj.parseXmlTag(inputpolicylist, "FAC_MOBILE");
                            strDOB = prsObj.parseXmlTag(inputpolicylist, "FAC_DOB");
                            strPIN = prsObj.parseXmlTag(inputpolicylist, "PIN_NO");

                            strAddress = prsObj.parseXmlTag(inputpolicylist, "FAC_ADDR1");
                            strStatus = prsObj.parseXmlTag(inputpolicylist, "APPROVED_STATUS");

                            strType = prsObj.parseXmlTag(inputpolicylist, "FAC_TYPE");
                            if (strDOB == null) {
                                strDOB = "";
                            } else {
                                strDOB = strDOB.split("T")[0];
                                strDOB = mCommonMethods.getFormattedDate(strDOB);
                            }
                        }

                    } catch (Exception e) {
                        try {
                            throw (e);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        progressDialog.dismiss();
                        running = false;
                    }

                } catch (IOException | XmlPullParserException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    progressDialog.dismiss();
                    running = false;
                }
            } catch (Exception e) {
                try {
                    throw (e);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                progressDialog.dismiss();
                running = false;
            }
            return null;

        }

        @Override
        protected void onPostExecute(String unused) {
            if (progressDialog.isShowing())
                dismissProgressDialog();

            if (running) {
                if (strAuthUserErrorCOde == null) {
                    boolean ok = true;
                    try {

                        edittextEmailRegistrationUser.setText(strEmailId);
                        edittextMobileNumberRegistrationUser.setText(strMobilNo);

                        String strPass = "sbil";

                        clsLogin log = new clsLogin(
                                SimpleCrypto.encrypt("SBIL",
                                        strTitle == null ? "" : strTitle),
                                SimpleCrypto.encrypt("SBIL",
                                        strFame == null ? "" : strFame),
                                SimpleCrypto.encrypt("SBIL",
                                        strLName == null ? "" : strLName),
                                strAddress,//address
                                strStatus,//status
                                SimpleCrypto.encrypt("SBIL", strID),
                                SimpleCrypto.encrypt("SBIL", "SBIL".toLowerCase()),
                                SimpleCrypto.encrypt("SBIL", strEmailId == null ? "" : strEmailId),
                                SimpleCrypto.encrypt("SBIL", strPass),
                                SimpleCrypto.encrypt("SBIL", strPass),
                                SimpleCrypto.encrypt("SBIL", "SBIL"),
                                SimpleCrypto.encrypt("SBIL", strMobilNo == null ? "" : strMobilNo),
                                SimpleCrypto.encrypt("SBIL", strDOB).trim(),
                                SimpleCrypto.encrypt("SBIL", strType),
                                SimpleCrypto.encrypt("SBIL", strPIN));


                           /* int UserId = dbHelper.GetUserId();
                            if (UserId == 0) {
                                dbHelper.AddLogin(log);
                            }*/
                        int UserId = dbHelper.GetUserId();
                        if (UserId == 0) {
                            dbHelper.AddLogin(log);
                        } else {
                            int rowsDeleted = dbHelper.deletePreviousUser();
                            if (rowsDeleted > 0) {
                                dbHelper.AddLogin(log);
                            }
                        }

                        //}

                    } catch (Exception ex) {
                        ok = false;
                    } finally {
                        if (ok) {
                            service_hits(METHOD_NAME_SAVE_REG_DETAIL_SMRT);
                        }
                    }
                } else {
                    mCommonMethods.showMessageDialog(context, mCommonMethods.UNAUTHORISED_USER_ALERT);
                }
            } else {
                mCommonMethods.showMessageDialog(context, mCommonMethods.SERVER_ERROR);
            }
        }
    }

    @Override
    public void downLoadData() {
        try {
            Intent intent;
            if (str_service_type == METHOD_NAME_CHK_VERSION) {
                intent = new Intent(RegisterUserActivity.this, CIFEnrollmentPFActivity.class);
                startActivity(intent);
            } else {

                //reset flags for dashborad renewal preferences
                SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                editor.putString(mCommonMethods.getDASHBOARD_RENEWAL_UPDATE_PREFERENCE(), "false");
                editor.putString(mCommonMethods.getNOTIFICATION_PREFERENCE(), "false");
                editor.putString(mCommonMethods.getLOGGED_PROPOSAL_COUNT_NOTIFICATION_PREFERENCE(), "false");
                editor.putString(mCommonMethods.getNON_MEDICAL_PENDING_REQ_COUNT_NOTIFICATION_PREFERENCE(), "false");
                editor.putString(mCommonMethods.getMEDICAL_PENDING_REQ_COUNT_NOTIFICATION_PREFERENCE(), "False");
                editor.putString(mCommonMethods.getClaimRequirementInfo(), "false");
                editor.putString(mCommonMethods.MATURITY_LIST_NOTIFICATION_PREFERENCE, "false");
                editor.putString(mCommonMethods.SB_DUE_LIST_NOTIFICATION_PREFERENCE, "false");
                editor.putString(mCommonMethods.getKYCMissingNotification(), "False");
                editor.apply();

                AppSharedPreferences.setData(context, new AppSharedPreferences().PERSISTENCY_KEY, "0");
                intent = new Intent(this, CarouselHomeActivity.class);
                startActivity(intent);
                finish();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void service_hits(String strServiceName) {

        CommonMethods.UserDetailsValuesModel userDetails = mCommonMethods
                .setUserDetails(context);

        service = new ServiceHits(this,
                strServiceName, "", strID,
                userDetails.getStrCIFBDMEmailId(), userDetails.getStrCIFBDMMObileNo(),
                mCommonMethods.GetUserPassword(context), this);
        service.execute();
    }

    private void showProgressDialog() {
        showDialog(DIALOG_DOWNLOAD_PROGRESS);
    }

    private void dismissProgressDialog() {
        dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
    }

    private void showDateProgressDialog() {
        showDialog(DATE_DIALOG_ID);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View selectedItemView, int arg2,
                               long arg3) {
        Spinner spinner = (Spinner) parent;
        int id = spinner.getId();
        if (id == R.id.spinnerRoleRegistrationUser) {
            strID = "";
            strEmailId = "";
            strMobilNo = "";
            strType = "";
            strPIN = "";
            strConfirmPIN = "";
            edittextUserCodeRegistrationUser.setText("");
            edittextMobileNumberRegistrationUser.setText("");
            edittextEmailRegistrationUser.setText("");
            edittextPinRegistrationUser.setText("");
            edittextConfirmPinRegistrationUser.setText("");
            edittextAgentPasswordRegistrationUser.setText("");
            edittextDOBRegistrationUser.setText("");
            edittextMobileNumberRegistrationUser.setError(null);
            edittextEmailRegistrationUser.setError(null);
            strType = spinnerRoleRegistrationUser.getSelectedItem().toString();
            if (strType.equalsIgnoreCase("Select Role")) {
                linearLayoutRegistrationUser.setVisibility(View.GONE);
            } else {
                strType = mCommonMethods.getUserTypeSelected(strType);
                linearLayoutRegistrationUser.setVisibility(View.VISIBLE);
                edittextCaptcha.setText("");
                tvCaptchaCode.setText(mCommonMethods.generateCaptcha());
                tvCaptchaCode.setPaintFlags(tvCaptchaCode.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }


    private boolean isUserTypeAGENT() {
        return strType.equalsIgnoreCase("AGENT");
    }

    private void setSpinner() {
        String[] planList = {"Select Role", "CIF(Banca)", "AGENT(Retail)", "BDM(Banca)", "UM(Retail)", "AM(Banca)", "SAM(Banca)", "ZAM(Banca)",
                "BSM(Retail)", "DSM(Retail)"/*,"ASM(Retail)","RSM(Retail)"*/, "CAG"};
        ArrayAdapter<String> planAdapter = new ArrayAdapter<>(context,
                R.layout.spinner_item, planList);
        planAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerRoleRegistrationUser.setAdapter(planAdapter);
        planAdapter.notifyDataSetChanged();
        spinnerRoleRegistrationUser.setOnItemSelectedListener(this);
    }

    private void showOtherDetails() {


        if (isUserTypeAGENT()) {
            edittextAgentPasswordRegistrationUser.setVisibility(View.VISIBLE);
        } else {
            edittextAgentPasswordRegistrationUser.setVisibility(View.GONE);
        }


    }

    @Override
    public void onBackPressed() {

        if (!isNewUser.equalsIgnoreCase("Y")) {

            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_yes_no);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button bt_yes = dialog.findViewById(R.id.bt_yes);
            Button bt_no = dialog.findViewById(R.id.bt_no);
            ((TextView) dialog.findViewById(R.id.tv_title)).setText("Are you sure you want to exit?");
            bt_yes.setText("Yes");
            bt_no.setText("No");
            bt_yes.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    finish();
                    System.exit(0);

                }
            });


            bt_no.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });
            dialog.show();
        } else {
            Intent intent = new Intent(context, LoginUserActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
            ((AppCompatActivity) context).finish();
        }
    }

    class CheckAppVesrion extends AsyncTask<String, Void, String> {

        private volatile boolean running = true;
        private String str_Version_success = "0", outResponce = "",
                strVersionError = "", str_VersionNumber = "", str_Launch_date = "",
                str_no_of_valid_days = "", str_Launch_message = "";
        private int mDay = 0, mMonth = 0, mYear = 0, current_version = 0;

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... data) {
            try {
                running = true;

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_CHK_VERSION);

                request.addProperty("strAppName", context.getResources().getString(R.string.app_name));
                request.addProperty("strEmailId", "");
                request.addProperty("strMobileNo", "");
                /*request.addProperty("AuthKey", SimpleCrypto.encrypt("SBIL", "sbil").trim());*/
                request.addProperty("AuthKey", mCommonMethods.getStrAuth());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION_CHK_VERSION = "http://tempuri.org/checkAppVersion";
                androidHttpTranport.call(SOAP_ACTION_CHK_VERSION, envelope);

                SoapPrimitive sa = null;
                sa = (SoapPrimitive) envelope.getResponse();

                outResponce = sa.toString();

                ParseXML prsObj = new ParseXML();

                if (outResponce.equalsIgnoreCase("<PolicyData />")
                        || outResponce.equalsIgnoreCase("anyType{}")
                        || outResponce == null) {
                    str_Version_success = "0";
                } else {
                    outResponce = prsObj.parseXmlTag(outResponce, "PolicyData");

                    outResponce = new ParseXML().parseXmlTag(outResponce, "ScreenData");
                    strVersionError = outResponce;

                    if (strVersionError == null) {

                        outResponce = sa.toString();
                        outResponce = prsObj.parseXmlTag(outResponce, "PolicyData");
                        outResponce = prsObj.parseXmlTag(outResponce, "Table");

                        str_VersionNumber = prsObj.parseXmlTag(
                                outResponce, "APP_VERSION");

                        str_Launch_date = prsObj.parseXmlTag(
                                outResponce, "APP_EFFECTIVE_DATE");

                        str_no_of_valid_days = prsObj.parseXmlTag(
                                outResponce, "APP_NO_OF_DAYS");

                        str_Launch_message = prsObj.parseXmlTag(
                                outResponce, "APP_MSG");

                        str_Version_success = "1";

                    }
                }
            } catch (Exception e) {
                running = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (progressDialog.isShowing()) {
                dismissProgressDialog();
            }

            if (running) {
                if (str_Version_success.equalsIgnoreCase("1")) {

                    PackageManager manager = context.getPackageManager();
                    PackageInfo info;
                    try {
                        info = manager.getPackageInfo(context.getPackageName(), 0);
                        current_version = info.versionCode;
                    } catch (PackageManager.NameNotFoundException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                        current_version = 0;
                    }

                    if (Integer.parseInt(str_VersionNumber) != current_version && current_version < Integer.parseInt(str_VersionNumber)) {

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

                        try {

                            Date d1, d2;
                            String dtStart = str_Launch_date;

                            Date Launch_date = sdf.parse(dtStart);

                            Calendar c = Calendar.getInstance();
                            c.setTime(Launch_date);
                            c.add(Calendar.DATE, +Integer.parseInt(str_no_of_valid_days));

                            d1 = sdf.parse(dtStart = sdf.format(c.getTime()));

                            String current_date = setDefaultDate(0);

                            d2 = sdf.parse(current_date);

                            if (d1.compareTo(d2) <= 0) {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "This Version of Smart Advisor has expired.please open google play store, log in and update Smart Advisor.",
                                        Toast.LENGTH_LONG).show();

                                VersionExpiredAlert("This Version of Smart Advisor has expired.please open google play store, log in and update Smart Advisor.");

                                //go out

                            } else {

                                long diff = d1.getTime() - d2.getTime();
                                long diffDays = diff / (24 * 60 * 60 * 1000);

                                if (diffDays > 5) {

                                    Toast.makeText(getApplicationContext(),
                                            str_Launch_message, Toast.LENGTH_LONG).show();

                                    str_service_type = METHOD_NAME_CHK_VERSION;
                                    //hit service
                                    service_hits(METHOD_NAME_SAVE_REG_DETAIL_SMRT);

                                } else {
                                    VersionExpiryAlert(str_Launch_message);

                                    Toast.makeText(getApplicationContext(),
                                            str_Launch_message, Toast.LENGTH_LONG).show();
                                }
                            }

                        } catch (java.text.ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    } else {
						/*Toast.makeText(getApplicationContext(),
								str_Launch_message, Toast.LENGTH_LONG).show();*/

                        str_service_type = METHOD_NAME_CHK_VERSION;

                        service_hits(METHOD_NAME_SAVE_REG_DETAIL_SMRT);
                    }
                } else {
                    //okAlert();
                    Toast.makeText(getApplicationContext(),
                            "Server not responding..", Toast.LENGTH_LONG).show();
                }
            } else {
                mCommonMethods.showMessageDialog(context, "Server not responding..");
            }
        }

        private String setDefaultDate(int id) {
            Calendar present_date = Calendar.getInstance();
            present_date.add(Calendar.DAY_OF_MONTH, +id);
            mDay = present_date.get(Calendar.DAY_OF_MONTH);
            mMonth = present_date.get(Calendar.MONTH);
            mYear = present_date.get(Calendar.YEAR);

            return mDay + "-" + (mMonth + 1) +
                    "-" + mYear;
        }
    }

    private void VersionExpiredAlert(String Message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        dialog.setCancelable(false);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText(Message);

        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

                System.runFinalizersOnExit(true);
                System.exit(0);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        dialog.show();
    }

    private void VersionExpiryAlert(String Message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        dialog.setCancelable(false);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText(Message);

        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

                str_service_type = METHOD_NAME_CHK_VERSION;

                //hit service
                service_hits(METHOD_NAME_SAVE_REG_DETAIL_SMRT);
            }
        });
        dialog.show();
    }

    class GenerateOTPAsyncTask extends AsyncTask<String, String, String> {
        private final String SOAP_ACTION_GENERATE_PASSCODE = "http://tempuri.org/GenerateOTP_EasyAccess";
        private final String METHOD_NAME_GENERATE_PASSCODE = "GenerateOTP_EasyAccess";
        int flag = 0;
        private volatile boolean running = true;

        //,mergedNACBIFile;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... aurl) {

            try {

                running = true;
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_GENERATE_PASSCODE);
                request.addProperty("MOBILE_NO", strMobilNo);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);

                androidHttpTranport.call(SOAP_ACTION_GENERATE_PASSCODE, envelope);

                SoapPrimitive sa;

                sa = (SoapPrimitive) envelope.getResponse();
                String inputpolicylist = sa.toString();

                if (inputpolicylist.equalsIgnoreCase("0") || inputpolicylist.equalsIgnoreCase("2")) {
                    flag = 0;
                } else {
                    flag = 1;
                }

            } catch (Exception e) {
                e.printStackTrace();
                running = false;
            }
            return null;

        }

        @Override
        protected void onPostExecute(String unused) {


            if (running) {
                if (flag == 1) {
                    buttonSaveRegistrationUser.setEnabled(true);
                    buttonSaveRegistrationUser.setBackgroundColor(Color.parseColor("#00a1e3"));

                    /*buttonGenerateOTP.setEnabled(false);
                    buttonGenerateOTP.setBackgroundColor(Color.parseColor("#A9A9A9"));*/
                    edittextOTP.setVisibility(View.VISIBLE);
                    mCommonMethods.showMessageDialog(context, "OTP generated successfully.");

                } else {
                    mCommonMethods.showMessageDialog(context, "Please try after sometime");
                }
                dismissProgressDialog();

            } else {
                dismissProgressDialog();
                mCommonMethods.showMessageDialog(context, "Please try after sometime");
            }
        }
    }

    class ValidateOTPAsyncTask extends AsyncTask<String, String, String> {
        private final String SOAP_ACTION_VALIDATE_OTP = "http://tempuri.org/validateOTP_easyacess";
        private final String METHOD_NAME_VALIDATE_OTP = "validateOTP_easyacess";
        int flag = 0;
        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... aurl) {

            try {

                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_VALIDATE_OTP);
                request.addProperty("strOTP", strOTP);
                request.addProperty("MOBILE_NO", strMobilNo);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);

                androidHttpTranport.call(SOAP_ACTION_VALIDATE_OTP, envelope);

                SoapPrimitive sa;

                sa = (SoapPrimitive) envelope.getResponse();
                String inputpolicylist = sa.toString();

                if (inputpolicylist.equalsIgnoreCase("1")) {
                    flag = 1;
                } else
                    flag = 0;

            } catch (Exception e) {
                e.printStackTrace();
                running = false;

            }
            return null;

        }

        @Override
        protected void onPostExecute(String unused) {

            dismissProgressDialog();

            if (running) {

                if (flag == 1) {
                    taskAsyncSaveRegistrationDetail = new DownloadFileAsyncSaveRegistrationDetail();
                    taskAsyncSaveRegistrationDetail.execute();
                } else {
                    buttonGenerateOTP.setEnabled(true);
                    buttonGenerateOTP.setBackgroundColor(Color.parseColor("#00a1e3"));
                    mCommonMethods.showMessageDialog(context, "Invalid OTP. Please re-enter OTP");
                }

            } else {
                buttonGenerateOTP.setEnabled(true);
                buttonGenerateOTP.setBackgroundColor(Color.parseColor("#00a1e3"));

                mCommonMethods.showMessageDialog(context, "Invalid OTP. Please re-enter OTP");
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }


        if (service != null) {
            service.cancel(true);
        }
        if (taskCheckAppVesrion != null) {
            taskCheckAppVesrion.cancel(true);
        }
        if (taskAsyncSaveRegistrationDetail != null) {
            taskAsyncSaveRegistrationDetail.cancel(true);
        }
        if (generateOTPAsyncTask != null) {
            generateOTPAsyncTask.cancel(true);
        }
        if (validateOTPAsyncTask != null) {
            validateOTPAsyncTask.cancel(true);
        }
        super.onDestroy();
    }


}
