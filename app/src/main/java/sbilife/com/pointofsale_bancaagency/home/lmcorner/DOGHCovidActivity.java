package sbilife.com.pointofsale_bancaagency.home.lmcorner;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.GenerateOTPGeneralAsyncTask;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.common.ValidateOTPGeneralAsyncTask;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class DOGHCovidActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener,
        GenerateOTPGeneralAsyncTask.GenerateOTPAsyncResultInterface, ValidateOTPGeneralAsyncTask.ValidateOTPAsyncResultInterface, ServiceHits.DownLoadData {
    private final String METHOD_NAME_DOGH_DETAIL = "geDOGHdetail_smrt";
    private final String NAMESPACE = "http://tempuri.org/";
    private final String URl = ServiceURL.SERVICE_URL;

    private StorageUtils mStorageUtils;
    private CommonMethods commonMethods;
    private Context context;
    private ProgressDialog mProgressDialog;
    private String strCIFBDMUserId = "";
    private String strCIFBDMEmailId = "";
    private String strCIFBDMPassword = "";
    private String strCIFBDMMObileNo = "";

    private String pastForeignVisit, pastCountryName, pastFromDuration, pastToDuration, pastReturnDate, screeneAtAirport,
            testedForCovid, homeQuarantine, underObservation, selfIsolation, futureForeignVisit, futureCountryName,
            futureFromDuration, futureToDuration, futureReturnDate, familyMemberSymptoms, respiratorySymptoms,
            contactWithCorona, place, illnessDetails, illnessDetailsRBString, anyQuestYesAnsweredDetails;

    private String OTP = "", IA_NAME, AGE, DOB, GENDER, POLICY_NUMBER, MPH_NAME;
    private RadioGroup radioGroupPastForeignVisit, radioGroupScreenedAtAirport, radioGroupTestedForCovid, radioGroupHomeQuarantine, radioGroupSelfIsolation,
            radioGroupunderObservation, radioGroupFutureForeignVisit, radioGroupSymptoms, radioGroupRespiratorySymptoms,
            radioGroupContactWithCorona, radioGroupIllnessDetails;

    private EditText etPastCountryName, etFutureCountryname, etOTP, edittextPlace, etIllnessDetails, etAnyQuestCoronaDetails;
    private TextView tvPastFromDuration, tvPastToDuration, tvPastReturnDate, tvFutureFromDuration, tvFutureToDuration, tvFutureReturnDate;

    private GenerateOTPGeneralAsyncTask generateOTPGeneralAsyncTask;
    private ValidateOTPGeneralAsyncTask validateOTPGeneralAsyncTask;
    private Button buttonValidateOTP, buttonGenerateOTP;
    private int mYear, mMonth, mDay, datecheck = 0;
    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {

            mYear = year;
            mMonth = month;
            mDay = day;
            updateDisplay(mYear, mMonth, mDay);
        }
    };
    private CheckBox checkBoxDisclaimer, checkBoxDisclaimerSecond;
    private LinearLayout llDOGHCovidDetails, llIllnessDetails;
    private DOGHDetailsAsyncTask doghDetailsAsyncTask;
    private CreatePDFAsync createPDFAsync;
    private UploadPDFService uploadPDFService;
    private File COVIDFilePath, DOGHFilePath;
    private String DOGHdeclarationString, DOGHdeclarationSecondString;
    private SaveCovidDataAsyncTask saveCovidDataAsyncTask;
    private ServiceHits service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_d_o_g_h_covid);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();

        mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        commonMethods.setApplicationToolbarMenu(this, "DOGH/Covid");
        llDOGHCovidDetails = findViewById(R.id.llDOGHCovidDetails);
        llDOGHCovidDetails.setVisibility(View.GONE);

        llIllnessDetails = findViewById(R.id.llIllnessDetails);
        llIllnessDetails.setVisibility(View.GONE);


        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");
        if (fromHome != null && fromHome.equalsIgnoreCase("N")) {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");

            try {
                strCIFBDMPassword = commonMethods.getStrAuth();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            getUserDetails();
        }

        radioGroupPastForeignVisit = findViewById(R.id.radioGroupPastForeignVisit);
        radioGroupScreenedAtAirport = findViewById(R.id.radioGroupScreenedAtAirport);
        radioGroupTestedForCovid = findViewById(R.id.radioGroupTestedForCovid19);
        radioGroupHomeQuarantine = findViewById(R.id.radioGroupHomeQuarantine);
        radioGroupSelfIsolation = findViewById(R.id.radioGroupSelfIsolation);
        radioGroupunderObservation = findViewById(R.id.radioGroupUnderObservation);
        radioGroupFutureForeignVisit = findViewById(R.id.radioGroupFutureCountryVisit);
        radioGroupSymptoms = findViewById(R.id.radioGroupSymptoms);
        radioGroupRespiratorySymptoms = findViewById(R.id.radioGroupRespiratorySymptoms);
        radioGroupContactWithCorona = findViewById(R.id.radioGroupContactWithCorona);
        radioGroupIllnessDetails = findViewById(R.id.radioGroupIllnessDetails);

        etPastCountryName = findViewById(R.id.etPastCountryVisited);
        etFutureCountryname = findViewById(R.id.etFutureCountryVisited);

        etOTP = findViewById(R.id.etOTP);
        edittextPlace = findViewById(R.id.edittextPlace);
        etIllnessDetails = findViewById(R.id.etIllnessDetails);
        etAnyQuestCoronaDetails = findViewById(R.id.etAnyQuestCoronaDetails);

        tvPastFromDuration = findViewById(R.id.tvPastFromDuration);
        tvPastToDuration = findViewById(R.id.tvPastToDuration);
        tvPastReturnDate = findViewById(R.id.tvPastReturnDate);
        tvFutureFromDuration = findViewById(R.id.tvFutureFromDuration);
        tvFutureToDuration = findViewById(R.id.tvFutureToDuration);
        tvFutureReturnDate = findViewById(R.id.tvFutureReturnDate);

        tvPastFromDuration.setOnClickListener(this);
        tvPastToDuration.setOnClickListener(this);
        tvPastReturnDate.setOnClickListener(this);
        tvFutureFromDuration.setOnClickListener(this);
        tvFutureToDuration.setOnClickListener(this);
        tvFutureReturnDate.setOnClickListener(this);

        radioGroupPastForeignVisit.setOnCheckedChangeListener(this);
        radioGroupScreenedAtAirport.setOnCheckedChangeListener(this);
        radioGroupTestedForCovid.setOnCheckedChangeListener(this);
        radioGroupHomeQuarantine.setOnCheckedChangeListener(this);
        radioGroupSelfIsolation.setOnCheckedChangeListener(this);
        radioGroupunderObservation.setOnCheckedChangeListener(this);
        radioGroupFutureForeignVisit.setOnCheckedChangeListener(this);
        radioGroupSymptoms.setOnCheckedChangeListener(this);
        radioGroupRespiratorySymptoms.setOnCheckedChangeListener(this);
        radioGroupContactWithCorona.setOnCheckedChangeListener(this);

        radioGroupIllnessDetails.setOnCheckedChangeListener(this);

        buttonValidateOTP = findViewById(R.id.buttonValidateOTP);
        buttonGenerateOTP = findViewById(R.id.buttonGenerateOTP);

        buttonGenerateOTP.setOnClickListener(this);
        buttonValidateOTP.setOnClickListener(this);

        checkBoxDisclaimer = findViewById(R.id.checkBoxDisclaimer);
        checkBoxDisclaimerSecond = findViewById(R.id.checkBoxDisclaimerSecond);

        if (commonMethods.isNetworkConnected(context)) {
            service_hits();
        } else {
            commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
        }
    }

    private void service_hits() {

        service = new ServiceHits(context,
                METHOD_NAME_DOGH_DETAIL, strCIFBDMUserId, strCIFBDMUserId,
                strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();

    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = commonMethods.getStrAuth();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.buttonValidateOTP:
                commonMethods.hideKeyboard(etOTP, context);

                if (commonMethods.isNetworkConnected(context)) {
                    OTP = etOTP.getText().toString();
                    if (TextUtils.isEmpty(OTP)) {
                        commonMethods.showMessageDialog(context, "Please Enter valid OTP.");
                        return;
                    } else {
                        validation("validate");
                    }

                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }
                break;
            case R.id.buttonGenerateOTP:
                commonMethods.hideKeyboard(etOTP, context);

                if (commonMethods.isNetworkConnected(context)) {
                    validation("Generate");
                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }
                break;
            case R.id.buttonOk:
                if (commonMethods.isNetworkConnected(context)) {

                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }
                break;
            case R.id.tvDateOfBirth:
                datecheck = 1;
                showDateDialog();
                break;
            case R.id.tvPastFromDuration:
                datecheck = 2;
                showDateDialog();
                break;
            case R.id.tvPastToDuration:
                datecheck = 3;
                showDateDialog();
                break;
            case R.id.tvPastReturnDate:
                datecheck = 4;
                showDateDialog();
                break;
            case R.id.tvFutureFromDuration:
                datecheck = 5;
                showFutureDateDialog();
                break;
            case R.id.tvFutureToDuration:
                datecheck = 6;
                showFutureDateDialog();
                break;
            case R.id.tvFutureReturnDate:
                datecheck = 7;
                showFutureDateDialog();
                break;

        }
    }

    private void validation(String flag) {

        commonMethods.hideKeyboard(etOTP, context);

        if (!checkBoxDisclaimer.isChecked()) {
            commonMethods.showMessageDialog(context, "Please accept the declaration.");
            commonMethods.setFocusable(checkBoxDisclaimer);
            checkBoxDisclaimer.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(illnessDetailsRBString)) {
            commonMethods.showMessageDialog(context, "Please give Illness details");
            commonMethods.setFocusable(radioGroupIllnessDetails);
            radioGroupIllnessDetails.requestFocus();
            return;
        } else {
            if (illnessDetailsRBString.equalsIgnoreCase("yes")) {
                illnessDetails = etIllnessDetails.getText().toString().trim();
                if (TextUtils.isEmpty(illnessDetails)) {
                    commonMethods.showMessageDialog(context, "Please enter Illness details");
                    commonMethods.setFocusable(etIllnessDetails);
                    etIllnessDetails.requestFocus();
                    return;
                }
            } else {
                illnessDetails = "";
                etIllnessDetails.setText("");
            }
        }

        if (!checkBoxDisclaimerSecond.isChecked()) {
            commonMethods.showMessageDialog(context, "Please accept the declaration.");
            commonMethods.setFocusable(checkBoxDisclaimerSecond);
            checkBoxDisclaimerSecond.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(pastForeignVisit)) {
            commonMethods.showMessageDialog(context, "Please check Question 1 of COVID Questionnaire");
            commonMethods.setFocusable(radioGroupPastForeignVisit);
            radioGroupPastForeignVisit.requestFocus();
            return;
        } else {

            if (pastForeignVisit.equalsIgnoreCase("yes")) {
                pastCountryName = etPastCountryName.getText().toString().trim();
                pastFromDuration = tvPastFromDuration.getText().toString();
                pastToDuration = tvPastToDuration.getText().toString();
                pastReturnDate = tvPastReturnDate.getText().toString();

                Date past_from_dt = null, past_to_dt = null, past_returnDate = null;

                try {
                    final SimpleDateFormat formatter = new SimpleDateFormat(
                            "dd-MMMM-yyyy");
                    past_from_dt = formatter.parse(pastFromDuration);
                    past_to_dt = formatter.parse(pastToDuration);
                    past_returnDate = formatter.parse(pastReturnDate);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(pastCountryName)) {
                    commonMethods.showMessageDialog(context, "Please enter name of Country visited after 1.1.2020");
                    commonMethods.setFocusable(etPastCountryName);
                    etPastCountryName.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(pastFromDuration)) {
                    commonMethods.showMessageDialog(context, "Please enter Duration");
                    commonMethods.setFocusable(tvPastFromDuration);
                    tvPastFromDuration.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(pastToDuration)) {
                    commonMethods.showMessageDialog(context, "Please enter Duration");
                    commonMethods.setFocusable(tvPastToDuration);
                    tvPastToDuration.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(pastReturnDate)) {
                    commonMethods.showMessageDialog(context, "Please enter return Date");
                    commonMethods.setFocusable(tvPastReturnDate);
                    tvPastReturnDate.requestFocus();
                    return;
                }
                if ((past_from_dt != null && past_from_dt.after(past_to_dt)) || (past_from_dt != null && past_from_dt.equals(past_to_dt))) {
                    commonMethods.showMessageDialog(context, "To Date cannot be less than or equal to From Date");
                    commonMethods.setFocusable(tvPastFromDuration);
                    tvPastFromDuration.requestFocus();
                }
                if ((past_returnDate != null && past_to_dt.after(past_returnDate))) {
                    commonMethods.showMessageDialog(context, "Return Date cannot be less than To Date");
                    tvPastReturnDate.setFocusable(true);
                    tvPastReturnDate.setFocusableInTouchMode(true);
                    tvPastReturnDate.requestFocus();
                }

                if (TextUtils.isEmpty(screeneAtAirport)) {
                    commonMethods.showMessageDialog(context, "Please check Question 1.4 of COVID Questionnaire");
                    commonMethods.setFocusable(radioGroupScreenedAtAirport);
                    radioGroupScreenedAtAirport.requestFocus();
                    return;
                }

            } else {
                pastCountryName = "";
                pastFromDuration = "";
                pastToDuration = "";
                pastReturnDate = "";
                screeneAtAirport = "";
                etPastCountryName.setText("");
                tvPastFromDuration.setText("");
                tvPastToDuration.setText("");
                tvPastReturnDate.setText("");
            }
        }

        if (TextUtils.isEmpty(testedForCovid)) {
            commonMethods.showMessageDialog(context, "Please check Question 2 of COVID Questionnaire");
            commonMethods.setFocusable(radioGroupTestedForCovid);
            radioGroupTestedForCovid.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(homeQuarantine)) {
            commonMethods.showMessageDialog(context, "Please check Question 3.1 of COVID Questionnaire");
            commonMethods.setFocusable(radioGroupHomeQuarantine);
            radioGroupHomeQuarantine.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(underObservation)) {
            commonMethods.showMessageDialog(context, "Please check Question 3.2 of COVID Questionnaire");
            radioGroupunderObservation.setFocusable(true);
            radioGroupunderObservation.setFocusableInTouchMode(true);
            radioGroupunderObservation.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(selfIsolation)) {
            commonMethods.showMessageDialog(context, "Please check Question 3.3 of COVID Questionnaire");
            commonMethods.setFocusable(radioGroupSelfIsolation);
            radioGroupSelfIsolation.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(futureForeignVisit)) {
            commonMethods.showMessageDialog(context, "Please check Question 4 of COVID Questionnaire");
            commonMethods.setFocusable(radioGroupFutureForeignVisit);
            radioGroupFutureForeignVisit.requestFocus();
            return;
        } else {

            if (futureForeignVisit.equalsIgnoreCase("yes")) {
                futureCountryName = etFutureCountryname.getText().toString();
                futureFromDuration = tvFutureFromDuration.getText().toString();
                futureToDuration = tvFutureToDuration.getText().toString();
                futureReturnDate = tvFutureReturnDate.getText().toString();

                Date future_from_dt = null, future_to_dt = null, future_returnDate = null;

                try {
                    final SimpleDateFormat formatter = new SimpleDateFormat(
                            "dd-MMMM-yyyy");
                    future_from_dt = formatter.parse(futureFromDuration);
                    future_to_dt = formatter.parse(futureToDuration);
                    future_returnDate = formatter.parse(futureReturnDate);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (TextUtils.isEmpty(futureCountryName)) {
                    commonMethods.showMessageDialog(context, "Please enter name of Country");
                    commonMethods.setFocusable(etFutureCountryname);
                    etFutureCountryname.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(futureFromDuration)) {
                    commonMethods.showMessageDialog(context, "Please enter Duration");
                    commonMethods.setFocusable(tvFutureFromDuration);
                    tvFutureFromDuration.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(futureToDuration)) {
                    commonMethods.showMessageDialog(context, "Please enter Duration");
                    commonMethods.setFocusable(tvFutureToDuration);
                    tvFutureToDuration.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(futureReturnDate)) {
                    commonMethods.showMessageDialog(context, "Please enter return Date");
                    commonMethods.setFocusable(tvFutureReturnDate);
                    tvFutureReturnDate.requestFocus();
                    return;
                }

                if ((future_from_dt != null && future_from_dt.after(future_to_dt)) || (future_from_dt != null && future_from_dt.equals(future_to_dt))) {
                    commonMethods.showMessageDialog(context, "To Date cannot be less than or equal to From Date");
                    commonMethods.setFocusable(tvFutureFromDuration);
                    tvFutureFromDuration.requestFocus();
                }
                if ((future_returnDate != null && future_to_dt.after(future_returnDate))) {
                    commonMethods.showMessageDialog(context, "Return Date cannot be less than To Date");
                    commonMethods.setFocusable(tvFutureReturnDate);
                    tvFutureReturnDate.requestFocus();
                }

            } else {
                futureCountryName = "";
                futureFromDuration = "";
                futureToDuration = "";
                futureReturnDate = "";
                etFutureCountryname.setText("");
                tvFutureFromDuration.setText("");
                tvFutureToDuration.setText("");
                tvFutureReturnDate.setText("");
            }
        }


        if (TextUtils.isEmpty(familyMemberSymptoms)) {
            commonMethods.showMessageDialog(context, "Please check Question 5 of COVID Questionnaire");
            commonMethods.setFocusable(radioGroupSymptoms);
            radioGroupSymptoms.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(respiratorySymptoms)) {
            commonMethods.showMessageDialog(context, "Please check Question 6 of COVID Questionnaire");
            commonMethods.setFocusable(radioGroupRespiratorySymptoms);
            radioGroupRespiratorySymptoms.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(contactWithCorona)) {
            commonMethods.showMessageDialog(context, "Please check Question 7 of COVID Questionnaire");
            commonMethods.setFocusable(radioGroupContactWithCorona);
            radioGroupContactWithCorona.requestFocus();
            return;
        }

        if (testedForCovid.equalsIgnoreCase("yes") || homeQuarantine.equalsIgnoreCase("yes")
                || underObservation.equalsIgnoreCase("yes") || selfIsolation.equalsIgnoreCase("yes")
                || familyMemberSymptoms.equalsIgnoreCase("yes") || respiratorySymptoms.equalsIgnoreCase("yes")
                || contactWithCorona.equalsIgnoreCase("yes")) {

            anyQuestYesAnsweredDetails = etAnyQuestCoronaDetails.getText().toString();
            if (TextUtils.isEmpty(anyQuestYesAnsweredDetails)) {
                commonMethods.showMessageDialog(context, "Please provide details for question answered Yes.");
                commonMethods.setFocusable(etAnyQuestCoronaDetails);
                etAnyQuestCoronaDetails.requestFocus();
                return;
            }
        }


        place = edittextPlace.getText().toString();
        if (TextUtils.isEmpty(place)) {
            commonMethods.showMessageDialog(context, "Please enter the Place.");
            commonMethods.setFocusable(edittextPlace);
            edittextPlace.requestFocus();
            return;
        }

        if (flag.equalsIgnoreCase("Generate")) {
            generateOTPGeneralAsyncTask = new GenerateOTPGeneralAsyncTask(strCIFBDMMObileNo,
                    this, context);
            generateOTPGeneralAsyncTask.execute();
        } else {
            validateOTPGeneralAsyncTask = new ValidateOTPGeneralAsyncTask(strCIFBDMMObileNo,
                    OTP, this, context);
            validateOTPGeneralAsyncTask.execute();
        }

    }

    public void generateOTPAsynResultMethod(int flag) {
        if (flag == 1) {
            buttonValidateOTP.setEnabled(true);
            buttonValidateOTP.setBackgroundColor(Color.parseColor("#00a1e3"));

            buttonGenerateOTP.setEnabled(false);
            buttonGenerateOTP.setBackgroundColor(Color.parseColor("#A9A9A9"));
            etOTP.setVisibility(View.VISIBLE);
            etOTP.setFocusable(true);
            etOTP.requestFocus();
            commonMethods.showMessageDialog(context, "OTP sent succesfully to your Mobile Number.");

        } else {
            commonMethods.showMessageDialog(context, "Please try after sometime");
        }
    }

    public void validateOTPAsynResultMethod(int flag) {
        if (flag == 1) {
            createPDFAsync = new CreatePDFAsync("PDF");
            createPDFAsync.execute();
        } else {
            buttonGenerateOTP.setEnabled(true);
            buttonGenerateOTP.setBackgroundColor(Color.parseColor("#00a1e3"));
            commonMethods.showMessageDialog(context, "Invalid Passcode. Please re-enter Passcode");
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {


        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);
        String text = radioButton.getText() + "";
        switch (radioGroup.getId()) {

            case R.id.radioGroupIllnessDetails:
                illnessDetailsRBString = text;
                if (illnessDetailsRBString.equalsIgnoreCase("yes")) {
                    etIllnessDetails.setEnabled(true);
                    llIllnessDetails.setVisibility(View.VISIBLE);
                } else {
                    etIllnessDetails.setEnabled(false);
                    llIllnessDetails.setVisibility(View.GONE);
                }
                break;
            case R.id.radioGroupPastForeignVisit:
                pastForeignVisit = text;

                if (pastForeignVisit.equalsIgnoreCase("Yes")) {
                    etPastCountryName.setEnabled(true);
                    tvPastFromDuration.setEnabled(true);
                    tvPastToDuration.setEnabled(true);
                    tvPastReturnDate.setEnabled(true);
                } else {
                    etPastCountryName.setEnabled(false);
                    tvPastFromDuration.setEnabled(false);
                    tvPastToDuration.setEnabled(false);
                    tvPastReturnDate.setEnabled(false);
                }
                break;

            case R.id.radioGroupScreenedAtAirport:
                screeneAtAirport = text;

                break;

            case R.id.radioGroupTestedForCovid19:
                testedForCovid = text;

                break;

            case R.id.radioGroupHomeQuarantine:
                homeQuarantine = text;

                break;

            case R.id.radioGroupUnderObservation:
                underObservation = text;

                break;

            case R.id.radioGroupSelfIsolation:
                selfIsolation = text;

                break;

            case R.id.radioGroupFutureCountryVisit:
                futureForeignVisit = text;

                if (futureForeignVisit.equalsIgnoreCase("Yes")) {
                    etFutureCountryname.setEnabled(true);
                    tvFutureFromDuration.setEnabled(true);
                    tvFutureToDuration.setEnabled(true);
                    tvFutureReturnDate.setEnabled(true);
                } else {
                    etFutureCountryname.setEnabled(false);
                    tvFutureFromDuration.setEnabled(false);
                    tvFutureToDuration.setEnabled(false);
                    tvFutureReturnDate.setEnabled(false);
                }
                break;

            case R.id.radioGroupSymptoms:
                familyMemberSymptoms = text;

                break;

            case R.id.radioGroupRespiratorySymptoms:
                respiratorySymptoms = text;

                break;

            case R.id.radioGroupContactWithCorona:
                contactWithCorona = text;

                break;
        }

    }

    private void showDateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(
                context,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                mYear, mMonth, mDay);
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void showFutureDateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(
                context,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                mYear, mMonth, mDay);

        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void updateDisplay(int year, int month, int day) {

        String y = String.valueOf(year);
        String m = String.valueOf(month + 1);
        String da = String.valueOf(day);
        m = commonMethods.getFullMonthName(m);
        String totaldate = da + "-" + m + "-" + y;

        Calendar present_date = Calendar.getInstance();
        int tDay = present_date.get(Calendar.DAY_OF_MONTH);
        int tMonth = present_date.get(Calendar.MONTH);
        int tYear = present_date.get(Calendar.YEAR);


        if (datecheck == 2) {
            tvPastFromDuration.setText(totaldate);
            pastFromDuration = totaldate;
        }
        if (datecheck == 3) {
            tvPastToDuration.setText(totaldate);
            pastToDuration = totaldate;
        }
        if (datecheck == 4) {
            tvPastReturnDate.setText(totaldate);
            pastReturnDate = totaldate;
        }
        if (datecheck == 5) {
            tvFutureFromDuration.setText(totaldate);
            futureFromDuration = totaldate;
        }
        if (datecheck == 6) {
            tvFutureToDuration.setText(totaldate);
            futureToDuration = totaldate;
        }
        if (datecheck == 7) {
            tvFutureReturnDate.setText(totaldate);
            futureReturnDate = totaldate;
        }
    }

    private void createCOVIDPDF() {
        try {


            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.BOLD);
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.NORMAL);
            COVIDFilePath = mStorageUtils.createFileToAppSpecificDir(context,
                    strCIFBDMUserId + "_COVID19" + "_.pdf");

            Rectangle rect = new Rectangle(594f, 792f);

            Document document = new Document(rect, 50, 50, 50, 50);
            @SuppressWarnings("unused")
            PdfWriter pdf_writer = null;
            pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(
                    COVIDFilePath.getAbsolutePath()));

            //HeaderFooter footer = new HeaderFooter();
            //pdf_writer.setPageEvent(footer);

            document.open();


            // For SBI- Life Logo starts
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext()
                    .getResources(), R.drawable.sbi_life_logo);
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            Image img_sbi_logo = Image.getInstance(stream.toByteArray());
            img_sbi_logo.setAlignment(Image.LEFT);
            img_sbi_logo.getSpacingAfter();
            img_sbi_logo.scaleToFit(80, 50);

            Paragraph para_img_logo = new Paragraph("");
            para_img_logo.add(img_sbi_logo);

            Paragraph para_img_logo_after_space_1 = new Paragraph(" ");

            document.add(para_img_logo);
            // For SBI- Life Logo ends

            // To draw line after the sbi logo image
            document.add(new LineSeparator());
            document.add(para_img_logo_after_space_1);

            PdfPCell cell;

            Paragraph new_line = new Paragraph("\n");

            //Policy Number Table
            PdfPTable policyNumberTable = new PdfPTable(2);
            policyNumberTable.setWidths(new float[]{5f, 5f});
            policyNumberTable.setWidthPercentage(100f);
            policyNumberTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell(new Phrase("COVID Questionnaire (UWM029)",
                    headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            policyNumberTable.addCell(cell);


            cell = new PdfPCell(new Phrase(
                    "Policy Number : ", small_bold));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            policyNumberTable.addCell(cell);

            cell = new PdfPCell(new Phrase(POLICY_NUMBER, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            policyNumberTable.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Name of the Proposer/ Life to be assured : ", small_bold));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            policyNumberTable.addCell(cell);

            cell = new PdfPCell(new Phrase(IA_NAME, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            policyNumberTable.addCell(cell);

            document.add(policyNumberTable);
            //End Policy Number Table


            document.add(new_line);

            //Question Table
            PdfPTable question1Table = new PdfPTable(2);
            question1Table.setWidths(new float[]{5f, 5f});
            question1Table.setWidthPercentage(100f);
            question1Table.setHorizontalAlignment(Element.ALIGN_LEFT);

            //Question 1 cell
            cell = new PdfPCell(new Phrase(
                    "1.Have you visited any foreign country after 1.1.2020,then please answer the following", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);


            //Column 2
            cell = new PdfPCell(new Phrase(
                    pastForeignVisit, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);


            if (pastForeignVisit.equalsIgnoreCase("yes")) {

                cell = new PdfPCell(new Phrase(
                        "1.1 Name of country visited", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        pastCountryName, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "1.2 Duration", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "From: " + pastFromDuration + "\n" + "To: " + pastToDuration, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "1.3 Date of return to India", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        pastReturnDate, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);


                cell = new PdfPCell(new Phrase(
                        "1.4 If you have been screened at the airport please providecopy of report provided", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        screeneAtAirport, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);
            } else {

                cell = new PdfPCell(new Phrase(
                        "1.1 Name of country visited", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "NA", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "1.2 Duration", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "From: NA\n" + "To: NA", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "1.3 Date of return to India", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "NA", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);


                cell = new PdfPCell(new Phrase(
                        "1.4 If you have been screened at the airport please providecopy of report provided", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "NA", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);
            }


            cell = new PdfPCell(new Phrase(
                    "2 Have you been tested for COVID-19. If yes, all thereports of same till date", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    testedForCovid, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);


            cell = new PdfPCell(new Phrase(
                    "3 Please confirm if either of the following is applicable to you", small_normal));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "3.1 Kept in home quarantine anytime (till date) since1.1.2020", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    homeQuarantine, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);


            cell = new PdfPCell(new Phrase(
                    "3.2 Kept under observation anytime (till date) since1.1.2020", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    underObservation, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);


            cell = new PdfPCell(new Phrase(
                    "3.3 Kept in home isolation/self isolation anytime (till date)since 1.1.2020", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    selfIsolation, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "4 Do you plan to travel to any foreign countries in next 6months", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    futureForeignVisit, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);

            if (futureForeignVisit.equalsIgnoreCase("yes")) {

                cell = new PdfPCell(new Phrase(
                        "4.1 Name of country visited", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        futureCountryName, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "4.2 Duration", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "From: " + futureFromDuration + "\n" + "To: " + futureToDuration, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "4.3 Date of return to India", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        futureReturnDate, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);

            } else {

                cell = new PdfPCell(new Phrase(
                        "4.1 Name of country visited", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "NA", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "4.2 Duration", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "From: NA\n" + "To: NA", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "4.3 Date of return to India", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "NA", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);

            }


            cell = new PdfPCell(new Phrase(
                    "5 Have you or your immediate family members/co-habitants suffered from any signs & symptoms of flu(cough, cold, fever more than 05 days) since 1.1.2020(whether any medical consultation taken or not)?", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    familyMemberSymptoms, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "6 Have you or your immediate family members undergoneor been advised to undergo any test/investigations orhospitalized for observation or treatment in past 2 monthsfor respiratory symptoms?", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    respiratorySymptoms, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "7 Have you or any of your immediate familymembers/cohabitants come in contact with suspected or confirmed cases of coronavirus", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    contactWithCorona, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Details for question answered Yes", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    anyQuestYesAnsweredDetails, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);

            document.add(question1Table);


            PdfPTable declarationTable = new PdfPTable(1);
            declarationTable.setWidthPercentage(100f);
            declarationTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell((new Phrase("Declaration to be given by the Proposer/Life to be assured", headerBold)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            declarationTable.addCell(cell);

            cell = new PdfPCell((new Phrase("I declare that the answers given above are true and to the best of my knowledge and that I have not withheld any materialinformation that may influence the assessment or acceptance of this application.", small_bold)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            declarationTable.addCell(cell);

            cell = new PdfPCell((new Phrase("I agree that this form will constitute part of my application for life assurance and that failure to disclose any material factknown to me may invalidate the contract.", small_bold)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            declarationTable.addCell(cell);

            document.add(declarationTable);

            PdfPTable signatureTable = new PdfPTable(2);
            signatureTable.setWidths(new float[]{5f, 5f});
            signatureTable.setWidthPercentage(100f);
            signatureTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell((new Phrase("Signature of Proposer/ Life to be assured ", small_bold)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            signatureTable.addCell(cell);

            cell = new PdfPCell((new Phrase("", small_bold)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            signatureTable.addCell(cell);

            document.add(signatureTable);


            PdfPTable placeTable = new PdfPTable(2);
            placeTable.setWidths(new float[]{5f, 5f});
            placeTable.setWidthPercentage(100f);
            placeTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell((new Phrase("Place : " + edittextPlace.getText().toString(), small_bold)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            placeTable.addCell(cell);

            Calendar present_date = Calendar.getInstance();
            int mDay = present_date.get(Calendar.DAY_OF_MONTH);
            int mMonth = present_date.get(Calendar.MONTH);
            int mYear = present_date.get(Calendar.YEAR);

            String CurrentDate = mDay + "-" + (mMonth + 1) + "-" + mYear;
            cell = new PdfPCell((new Phrase("Date: " + CurrentDate, small_bold)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            placeTable.addCell(cell);

            document.add(placeTable);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void manipulatePdf(String src, String dest, String passCode) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));

        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            PdfContentByte content = stamper.getUnderContent(i);

            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
                    BaseFont.WINANSI, BaseFont.EMBEDDED);

            content.beginText();
            content.setFontAndSize(bf, 8);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);//"yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date
            String message;

            if (i == reader.getNumberOfPages()) {
                message = "Authenticated via OTP - " + passCode + " shared for Policy Number. " + POLICY_NUMBER + " on "
                        + currentDateTime;//TimestampDate;
            } else {
                message = "Authenticated via OTP shared for Policy Number " + POLICY_NUMBER + " on "
                        + currentDateTime;//TimestampDate;
            }

            message += "Life Mitra Name : " + commonMethods.getUserName(context) +
                    "Life Mitra Code : " + strCIFBDMUserId;
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, message, 0,
                    20, 0);
            content.endText();
        }

        stamper.close();

    }

    private void createDOGHPDF() {
        try {


            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.BOLD);
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.NORMAL);
            Font medium_normal = new Font(Font.FontFamily.TIMES_ROMAN, 10,
                    Font.NORMAL);
            Font small_bold_for_name = new Font(Font.FontFamily.TIMES_ROMAN,
                    10, Font.BOLD);

            DOGHFilePath = mStorageUtils.createFileToAppSpecificDir(context, strCIFBDMUserId + "_DOGH" + "_.pdf");

            Rectangle rect = new Rectangle(594f, 792f);

            Document document = new Document(rect, 50, 50, 50, 50);
            @SuppressWarnings("unused")
            PdfWriter pdf_writer = null;
            pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(
                    DOGHFilePath.getAbsolutePath()));

            //HeaderFooter footer = new HeaderFooter();
            //pdf_writer.setPageEvent(footer);
            document.open();

            // For SBI- Life Logo starts
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.sbi_life_logo);

            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            Image img_sbi_logo = Image.getInstance(stream.toByteArray());
            img_sbi_logo.setAlignment(Image.LEFT);
            img_sbi_logo.getSpacingAfter();
            img_sbi_logo.scaleToFit(80, 50);

            Paragraph para_img_logo = new Paragraph("");
            para_img_logo.add(img_sbi_logo);

            Paragraph para_img_logo_after_space_1 = new Paragraph(" ");

            document.add(para_img_logo);
            // For SBI- Life Logo ends

            // To draw line after the sbi logo image
            document.add(new LineSeparator());
            document.add(para_img_logo_after_space_1);

            // For the BI Smart Elite Table Header(Grey One)

            PdfPTable headertable = new PdfPTable(1);
            headertable.setWidthPercentage(100);

            Paragraph para_address = new Paragraph(
                    "SBI Life Insurance Company Ltd" + "\n" + "Corporate Office: 'Natraj', M.V. " +
                            "Road and Western Express, Highway Junction, Andheri (East),Mumbai - 400069.",
                    small_bold_for_name);
            para_address.setAlignment(Element.ALIGN_CENTER);

            Paragraph para_address5 = new Paragraph(
                    " SBI Life Sampoorn Suraksha UIN: 111N040V04\nGOOD HEALTH DECLARATION FORM ",
                    headerBold);
            para_address5.setAlignment(Element.ALIGN_CENTER);

            document.add(para_address);
            document.add(para_img_logo_after_space_1);
            document.add(headertable);
            //document.add(para_img_logo_after_space_1);
            document.add(para_address5);
            document.add(para_img_logo_after_space_1);

            Paragraph guidelines = new Paragraph("Guidelines:",
                    medium_normal);
            guidelines.setAlignment(Element.ALIGN_LEFT);
            document.add(guidelines);

            PdfPTable guidelinesTable = new PdfPTable(1);
            guidelinesTable.setWidthPercentage(100);
            guidelinesTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st note
            PdfPCell cell = new PdfPCell(
                    new Phrase(
                            "All Sections/Fields in this form should be completed. Leaving the questions unanswered will not be accepted and may lead to rejection of the proposal. \n" +
                                    "Insurance is a contract of utmost good faith, trusting the life assured to disclose all the fact. In case of any doubt as to whether a fact is material or not, the fact should be disclosed.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            guidelinesTable.addCell(cell);
            document.add(guidelinesTable);

            document.add(para_img_logo_after_space_1);

            //Row One
            PdfPTable userDetailsRowOneTable = new PdfPTable(4);
            userDetailsRowOneTable.setWidthPercentage(100);

            PdfPCell nameTitleCell = new PdfPCell(new Paragraph(
                    "Name of the Life Assured (Member)", small_normal));
            nameTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell LANameCell = new PdfPCell(new Paragraph(
                    IA_NAME, small_normal));
            LANameCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell policyNumberTitleCell = new PdfPCell(new Paragraph(
                    "Policy Number", small_normal));
            policyNumberTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell policyNumberCell = new PdfPCell(new Paragraph(
                    POLICY_NUMBER, small_normal));
            policyNumberCell.setVerticalAlignment(Element.ALIGN_CENTER);

            nameTitleCell.setPadding(5);
            LANameCell.setPadding(5);
            policyNumberTitleCell.setPadding(5);
            policyNumberCell.setPadding(5);

            userDetailsRowOneTable.addCell(nameTitleCell);
            userDetailsRowOneTable.addCell(LANameCell);
            userDetailsRowOneTable.addCell(policyNumberTitleCell);
            userDetailsRowOneTable.addCell(policyNumberCell);
            document.add(userDetailsRowOneTable);
            //Row One End

            //Row Two
            PdfPTable userDetailsRowTwoTable = new PdfPTable(4);
            userDetailsRowTwoTable.setWidthPercentage(100);

            PdfPCell MPHTitleCell = new PdfPCell(new Paragraph(
                    "Name of the Master Policy Holder ", small_normal));
            MPHTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell MPHNameCell = new PdfPCell(new Paragraph(
                    MPH_NAME, small_normal));
            MPHNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell EmpNumberTitleCell = new PdfPCell(new Paragraph(
                    "Employee No./ Membership ID", small_normal));
            EmpNumberTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell empNumberCell = new PdfPCell(new Paragraph(
                    strCIFBDMUserId, small_normal));
            empNumberCell.setVerticalAlignment(Element.ALIGN_CENTER);

            MPHTitleCell.setPadding(5);
            MPHNameCell.setPadding(5);
            EmpNumberTitleCell.setPadding(5);
            empNumberCell.setPadding(5);

            userDetailsRowTwoTable.addCell(MPHTitleCell);
            userDetailsRowTwoTable.addCell(MPHNameCell);
            userDetailsRowTwoTable.addCell(EmpNumberTitleCell);
            userDetailsRowTwoTable.addCell(empNumberCell);
            document.add(userDetailsRowTwoTable);
            //Row Two End

            //Row Three
            PdfPTable userDetailsRowThreeTable = new PdfPTable(4);
            userDetailsRowThreeTable.setWidthPercentage(100);

            PdfPCell DOBTitleCell = new PdfPCell(new Paragraph(
                    " Date of Birth ", small_normal));
            DOBTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell DOBCell = new PdfPCell(new Paragraph(
                    DOB, small_normal));
            DOBCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell ageTitleCell = new PdfPCell(new Paragraph(
                    "Age", small_normal));
            ageTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell ageCell = new PdfPCell(new Paragraph(
                    AGE, small_normal));
            ageCell.setVerticalAlignment(Element.ALIGN_CENTER);

            DOBTitleCell.setPadding(5);
            DOBCell.setPadding(5);
            ageTitleCell.setPadding(5);
            ageCell.setPadding(5);

            userDetailsRowThreeTable.addCell(DOBTitleCell);
            userDetailsRowThreeTable.addCell(DOBCell);
            userDetailsRowThreeTable.addCell(ageTitleCell);
            userDetailsRowThreeTable.addCell(ageCell);
            document.add(userDetailsRowThreeTable);
            //Row Three End

            //Row Four
            PdfPTable userDetailsRowFourTable = new PdfPTable(2);
            userDetailsRowFourTable.setWidthPercentage(100);

            PdfPCell genderTitleCell = new PdfPCell(new Paragraph(
                    " Gender ", small_normal));
            genderTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell genderCell = new PdfPCell(new Paragraph(
                    GENDER, small_normal));
            genderCell.setHorizontalAlignment(Element.ALIGN_CENTER);


            genderTitleCell.setPadding(5);
            genderCell.setPadding(5);

            userDetailsRowFourTable.addCell(genderTitleCell);
            userDetailsRowFourTable.addCell(genderCell);
            document.add(userDetailsRowFourTable);
            //Row Four End


            Paragraph declarationPara = new Paragraph("DECLARATION :",
                    medium_normal);
            declarationPara.setAlignment(Element.ALIGN_LEFT);
            document.add(declarationPara);

            PdfPTable declarationTable = new PdfPTable(1);
            declarationTable.setWidthPercentage(100);
            declarationTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st note
            cell = new PdfPCell(
                    new Phrase(DOGHdeclarationString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            declarationTable.addCell(cell);
            document.add(declarationTable);
            document.add(para_img_logo_after_space_1);

            //Question Table
            PdfPTable illnessDetailsTable = new PdfPTable(1);
//            illnessDetailsTable.setWidths(new float[]{7f, 3f});
            illnessDetailsTable.setWidthPercentage(100f);
            illnessDetailsTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            //Question 1 cell
            if (illnessDetailsRBString.equalsIgnoreCase("yes")) {
                cell = new PdfPCell(new Phrase(
                        "Are you currently taking or in the past have taken any treatment or medications for any of illness mentioned above - " + illnessDetailsRBString +
                                " \n\nDetails :" + illnessDetails, small_bold));
            } else {
                cell = new PdfPCell(new Phrase(
                        "Are you currently taking or in the past have taken any treatment or medications for any of illness mentioned above - " + illnessDetailsRBString, small_normal));
            }
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            illnessDetailsTable.addCell(cell);


            //Column 2
            /*cell = new PdfPCell(new Phrase(
                    illnessDetailsRBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            illnessDetailsTable.addCell(cell);*/
            document.add(illnessDetailsTable);
            document.add(para_img_logo_after_space_1);
            //End Question 1 cell

            PdfPTable declarationSecondTable = new PdfPTable(1);
            declarationSecondTable.setWidthPercentage(100);
            declarationSecondTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st note
            cell = new PdfPCell(
                    new Phrase(DOGHdeclarationSecondString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            declarationSecondTable.addCell(cell);
            document.add(declarationSecondTable);
            document.add(para_img_logo_after_space_1);


            PdfPTable signGroupMemberTable = new PdfPTable(1);
            signGroupMemberTable.setWidthPercentage(100);

            PdfPCell signGroupMemberCell = new PdfPCell(
                    new Paragraph("Signature of Group Member______________________", small_bold));
            signGroupMemberCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            signGroupMemberCell.setPadding(5);
            signGroupMemberCell.setBorder(Rectangle.NO_BORDER);
            signGroupMemberTable.addCell(signGroupMemberCell);
            document.add(signGroupMemberTable);


            PdfPTable nameTable = new PdfPTable(1);
            nameTable.setWidthPercentage(100);

            PdfPCell namePage2Cell = new PdfPCell(
                    new Paragraph("Name______________________", small_bold));
            namePage2Cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            namePage2Cell.setPadding(5);
            namePage2Cell.setBorder(Rectangle.NO_BORDER);
            nameTable.addCell(namePage2Cell);
            document.add(nameTable);

            PdfPTable dateTable = new PdfPTable(1);
            dateTable.setWidthPercentage(100);
            PdfPCell datePage2Cell = new PdfPCell(
                    new Paragraph("Date______________________", small_bold));
            datePage2Cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            datePage2Cell.setPadding(5);
            datePage2Cell.setBorder(Rectangle.NO_BORDER);
            dateTable.addCell(datePage2Cell);
            document.add(dateTable);

            PdfPTable MPHDetailsTable = new PdfPTable(1);
            MPHDetailsTable.setWidthPercentage(100);

            PdfPCell MPHDetailsCell = new PdfPCell(
                    new Paragraph("Name of the MPH official / Witness______________________ " +
                            "\nSignature of MPH official / Witness______________________" +
                            "\nPlace _______________ Date_________________ ", small_bold_for_name));
            MPHDetailsCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            MPHDetailsCell.setPadding(5);
            MPHDetailsCell.setBorder(Rectangle.NO_BORDER);
            MPHDetailsTable.addCell(MPHDetailsCell);
            document.add(MPHDetailsTable);


            PdfPTable declarationPage2Table = new PdfPTable(1);
            declarationPage2Table.setWidthPercentage(100);
            declarationPage2Table.setHorizontalAlignment(Element.ALIGN_LEFT);

            document.add(para_img_logo_after_space_1);

            // 1st note
            cell = new PdfPCell(
                    new Phrase(
                            "DECLARATION WHEN THE MEMBERSHIP FORM IS FILLED BY A PERSON OTHER THAN THE GROUP MEMBER/GROUP MEMBER SIGNS IN A VERNACULAR LANGUAGE / GROUP MEMBER IS ILLITERATE (THUMB IMPRESSION CASES) ",
                            headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            declarationPage2Table.addCell(cell);


            cell = new PdfPCell(
                    new Phrase(
                            "I hereby declare that I have read out and explained the contents of " +
                                    "membership form and all other documents incidental to availing the Group " +
                                    "Insurance Scheme from SBI Life Insurance Company Ltd to the Group Member " +
                                    "and that he/she said that he/she had understood the same and the he/she " +
                                    "agrees to abide by all the terms and conditions of the same." +
                                    "\n\nI hereby declare that I have fully explained to the Group Member" +
                                    " that the answers to the questions form the basis for the Group Insurance" +
                                    " Cover and that if any untrue statement is contained herein, no benefits " +
                                    "will be payable by the SBI Life. I hereby declare that I have explained the " +
                                    "contents of this form to the Group Member in ______________Language, that I " +
                                    "have truly and correctly recorded the answers given by the Group Member and " +
                                    "that the Member has affixed his/her signature/ thumb impression on the membership" +
                                    " form in my presence, after fully understanding the contents thereof. ",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            declarationPage2Table.addCell(cell);
            document.add(declarationPage2Table);

            document.add(para_img_logo_after_space_1);

            PdfPTable signatureTablePage2 = new PdfPTable(2);
            signatureTablePage2.setWidthPercentage(100);

            PdfPCell signaturePage2Cell = new PdfPCell(
                    new Paragraph("_________________________________________________________" +
                            "\nSignature of the person making the declaration Name and Address", small_bold));
            signaturePage2Cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            signaturePage2Cell.setPadding(5);
            signaturePage2Cell.setBorder(Rectangle.NO_BORDER);
            signatureTablePage2.addCell(signaturePage2Cell);

            signaturePage2Cell = new PdfPCell(
                    new Paragraph("________________________________________ " +
                            "\nSignature of Group Member ", small_bold));
            signaturePage2Cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            signaturePage2Cell.setPadding(5);
            signaturePage2Cell.setBorder(Rectangle.NO_BORDER);
            signatureTablePage2.addCell(signaturePage2Cell);

            document.add(signatureTablePage2);

            PdfPTable sectionPage2Table = new PdfPTable(1);
            sectionPage2Table.setWidthPercentage(100);
            sectionPage2Table.setHorizontalAlignment(Element.ALIGN_LEFT);

            document.add(para_img_logo_after_space_1);

            // 1st note
            cell = new PdfPCell(
                    new Phrase(
                            "Section 41 of the Insurance Act, 1938, as amended from time to time  " +
                                    "\n:1) No person shall allow or offer to allow, either directly or indirectly, as an inducement to any person to take out or renew or continue an insurance in respect of any kind of risk relating to lives or property in India, any rebate of the whole or part of the commission payable or any rebate of the premium shown on the policy, nor shall any person taking out or renewing or continuing a policy accept any rebate, except such rebate as may be allowed in accordance with the published prospectuses or tables of the insurer." +
                                    "\nProvided that acceptance by an insurance agent of commission in connection with a policy of life insurance taken out by himself on his own life shall not be deemed to be acceptance of rebate of premium within the meaning of this sub-section if at the time of such acceptance the insurance agent satisfies the prescribed conditions establishing that he is a bona fide insurance agent employed by the insurer.  2)Any person making default in complying with the provisions of this section shall be liable for a penalty which may extend to ten lakh rupees. " +
                                    "\n\nExtract of Section 45 of Insurance Act, 1938, as amended from time to time " +
                                    "\n“No Policy of life insurance shall be called into question on any ground whatsoever after the expiry of three years from the date of policy.A policy of life insurance may be called into question at any time within 3 years from the date of policy, on the ground of fraud or on the ground that any statement of or suppression of a fact material to the expectancy of the life of the insured was incorrectly made in the proposal or other document on the basis of which the policy was issued or revived or rider issued.The insurer shall have to communicate in writing to the insured or legal representatives or nominees or assignees of the insured, the grounds and materials on which such decision is based. No insurer shall repudiate a life insurance policy on the ground of fraud if the insured can prove that the mis-statement or suppression of material fact was true to the best of his knowledge and belief or that there was no deliberate intention to suppress the fact or that such mis-statement or suppression are within the knowledge of the insurer. In case of fraud, the onus of disproving lies upon the beneficiaries, in case the policy holder is not alive. " +
                                    "\nIn case of repudiation of the policy on the ground of misstatement or suppression of a material fact and not on the grounds of fraud, the premiums collected on the policy till the date of repudiation shall be paid.Nothing in this section shall prevent the insurer from calling for proof of age at any time if he is entitled to do so, and no policy shall be deemed to be called in question merely because the terms of the policy are adjusted on subsequent proof that the age of the life insured was incorrectly stated in the proposal. " +
                                    "\nFor complete details of this section and the definition of ‘’date of policy’, please refer section 45 of the Insurance Act, 1938, as amended from time to time.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            sectionPage2Table.addCell(cell);
            document.add(sectionPage2Table);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void downLoadData() {
        doghDetailsAsyncTask = new DOGHDetailsAsyncTask();
        doghDetailsAsyncTask.execute();
    }

    private String generateXmlString() {

        String xmlTags = "<?xml version='1.0' encoding='utf-8' ?><COVID>" +
                "<IACode>" + strCIFBDMUserId + "</IACode>";
        xmlTags += "<HD_COVID_NAME_COUNTRY_VISITED>" + pastCountryName + "</HD_COVID_NAME_COUNTRY_VISITED>" +
                "<HD_COVID_VISIT_DURA_FRm_Dt>" + (TextUtils.isEmpty(pastFromDuration) ? "" : commonMethods.formatDateForerver(pastFromDuration)) + "</HD_COVID_VISIT_DURA_FRm_Dt>" +
                "<HD_COVID_VISIT_DURA_TO_Dt>" + (TextUtils.isEmpty(pastToDuration) ? "" : commonMethods.formatDateForerver(pastToDuration)) + "</HD_COVID_VISIT_DURA_TO_Dt>" +
                "<HD_COVID_Dt_OF_REt_TO_INDIA>" + (TextUtils.isEmpty(pastReturnDate) ? "" : commonMethods.formatDateForerver(pastReturnDate)) + "</HD_COVID_Dt_OF_REt_TO_INDIA>" +
                "<HD_COVID_SCREEN_AT_AIRPORT>" + screeneAtAirport + "</HD_COVID_SCREEN_AT_AIRPORT>" +
                "<HD_COVID_TEST_FOR_COVID19>" + testedForCovid + "</HD_COVID_TEST_FOR_COVID19>" +
                "<HD_COVID_KEPT_HOME_QUARTINE>" + homeQuarantine + "</HD_COVID_KEPT_HOME_QUARTINE>" +
                "<HD_COVID_KEPT_UNDER_OBSERV>" + underObservation + "</HD_COVID_KEPT_UNDER_OBSERV>" +
                "<HD_COVID_KEPT_HOME_ISOLAT>" + selfIsolation + "</HD_COVID_KEPT_HOME_ISOLAT>" +
                "<HD_COVID_PLN_TRA_FORE_COUNRTY>" + futureForeignVisit + "</HD_COVID_PLN_TRA_FORE_COUNRTY>" +
                "<HD_COVID_NM_COUN_VIS_NXT_6MON>" + futureCountryName + "</HD_COVID_NM_COUN_VIS_NXT_6MON>" +
                "<HD_COVID_VIS_DUR_FR_DT_NX_6MN>" + (TextUtils.isEmpty(futureFromDuration) ? "" : commonMethods.formatDateForerver(futureFromDuration)) + "</HD_COVID_VIS_DUR_FR_DT_NX_6MN>" +
                "<HD_COVID_VIS_DUR_TO_DT_NX_6MN>" + (TextUtils.isEmpty(futureToDuration) ? "" : commonMethods.formatDateForerver(futureToDuration)) + "</HD_COVID_VIS_DUR_TO_DT_NX_6MN>" +
                "<HD_COVID_DT_RE_TO_IND_NXT_6MN>" + (TextUtils.isEmpty(futureReturnDate) ? "" : commonMethods.formatDateForerver(futureReturnDate)) + "</HD_COVID_DT_RE_TO_IND_NXT_6MN>" +
                "<HD_COVID_FMLY_SUFF_ANY_SYMP>" + familyMemberSymptoms + "</HD_COVID_FMLY_SUFF_ANY_SYMP>" +
                "<HD_COVID_FMLY_UND_ANY_TEst>" + respiratorySymptoms + "</HD_COVID_FMLY_UND_ANY_TEst>" +
                "<HD_COVID_FMLY_CASE_CORONA>" + contactWithCorona + "</HD_COVID_FMLY_CASE_CORONA>" +
                "<HD_COVID_VISIT_ANYFORE_CNTRY>" + pastForeignVisit + "</HD_COVID_VISIT_ANYFORE_CNTRY>";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdp = new SimpleDateFormat("MM-dd-yyyy");
        String str_created_date = sdp.format(new Date(cal.getTimeInMillis()));
        xmlTags += "<createddate>" + str_created_date + "</createddate>";

        xmlTags += "<illnessDetailsYesNo>" + illnessDetailsRBString + "</illnessDetailsYesNo>" +
                "<illnessDetailsYesAnswer>" + illnessDetails + "</illnessDetailsYesAnswer>" +
                "<anyQuestYesAnsweredDetails>" + anyQuestYesAnsweredDetails + "</anyQuestYesAnsweredDetails>";
        xmlTags += "</COVID>";

        System.out.println("xmlTags:" + xmlTags);
        return xmlTags;
    }

    @Override
    protected void onDestroy() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (doghDetailsAsyncTask != null) {
            doghDetailsAsyncTask.cancel(true);
        }

        if (createPDFAsync != null) {
            createPDFAsync.cancel(true);
        }
        if (uploadPDFService != null) {
            uploadPDFService.cancel(true);
        }
        if (saveCovidDataAsyncTask != null) {
            saveCovidDataAsyncTask.cancel(true);
        }
        if (service != null) {
            service.cancel(true);
        }
        super.onDestroy();
    }

    class CreatePDFAsync extends AsyncTask<String, String, String> {
        private final String passCode;
        int flag = 0;
        private volatile boolean running = true;
        private File newFilePathCovid, newFilePathDOGH;

        private CreatePDFAsync(String passCode) {
            this.passCode = passCode;
        }

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

                if (passCode.equalsIgnoreCase("PDF")) {
                    createDOGHPDF();
                    createCOVIDPDF();
                } else {
                    newFilePathCovid = mStorageUtils.createFileToAppSpecificDir(context,
                            strCIFBDMUserId + "_COVID19" + ".pdf");

                    String covidFilePathName = newFilePathCovid.getAbsolutePath();
                    manipulatePdf(COVIDFilePath.getAbsolutePath(), covidFilePathName, passCode);
                    if (COVIDFilePath.exists()) {
                        COVIDFilePath.delete();
                    }
                    COVIDFilePath = newFilePathCovid;

                    newFilePathDOGH = mStorageUtils.createFileToAppSpecificDir(context,
                            strCIFBDMUserId + "_DOGH" + ".pdf");
                    String DOGHFilePathName = newFilePathDOGH.getAbsolutePath();
                    manipulatePdf(DOGHFilePath.getAbsolutePath(), DOGHFilePathName, passCode);
                    if (DOGHFilePath.exists()) {
                        DOGHFilePath.delete();
                    }
                    DOGHFilePath = newFilePathDOGH;
                }
                flag = 1;

            } catch (Exception e) {
                flag = 0;
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
                    if (passCode.equalsIgnoreCase("PDF")) {
                        createPDFAsync = new CreatePDFAsync(OTP);
                        createPDFAsync.execute();
                    } else {
                        saveCovidDataAsyncTask = new SaveCovidDataAsyncTask();
                        saveCovidDataAsyncTask.execute();
                    }
                } else {
                    buttonGenerateOTP.setEnabled(true);
                    buttonGenerateOTP.setBackgroundColor(Color.parseColor("#00a1e3"));
                    commonMethods.showMessageDialog(context, "Please try after sometime");
                }
            } else {
                buttonGenerateOTP.setEnabled(true);
                buttonGenerateOTP.setBackgroundColor(Color.parseColor("#00a1e3"));
                commonMethods.showMessageDialog(context, "Please try after sometime");
            }
        }
    }

    class UploadPDFService extends AsyncTask<String, String, String> {
        int flag = 0;
        private volatile boolean running = true;
        private String fileTag;

        public UploadPDFService(String fileTag) {
            this.fileTag = fileTag;
        }

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
                //UploadFileDOGH_GroupSMRT(byte[] f, string fileName, string strCode,
                // string strPolicyNo, string strEmailId, string strMobileNo, string strAuthKey)
                String METHOD_NAME = "UploadFileDOGH_GroupSMRT";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                byte[] BI_bytes;
                if (fileTag.equalsIgnoreCase("DOGH")) {
                    BI_bytes = new CommonMethods().read(DOGHFilePath);
                    // request.addProperty("filetype", "DGH");
                    request.addProperty("fileName", DOGHFilePath.getName());
                } else {
                    BI_bytes = new CommonMethods().read(COVIDFilePath);
                    request.addProperty("fileName", COVIDFilePath.getName());
                }
                request.addProperty("f", org.kobjects.base64.Base64.encode(BI_bytes));
                request.addProperty("strCode", strCIFBDMUserId);
                request.addProperty("strPolicyNo", POLICY_NUMBER);
                commonMethods.appendSecurityParams(context, request, strCIFBDMEmailId, strCIFBDMMObileNo);


                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                // allowAllSSL();
                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);

                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;
                androidHttpTranport.call(SOAP_ACTION,
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
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {
                if (flag == 1) {
                    if (fileTag.equalsIgnoreCase("DOGH")) {
                        uploadPDFService = new UploadPDFService("COVID");
                        uploadPDFService.execute();
                    } else {
                        llDOGHCovidDetails.setVisibility(View.GONE);
                        // commonMethods.showMessageDialog(context, "Data Saved successfully");
                        try {
                            final Dialog dialog = new Dialog(context);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_with_ok_button);
                            TextView text = dialog.findViewById(R.id.tv_title);
                            text.setText("Data Saved successfully");
                            Button dialogButton = dialog.findViewById(R.id.bt_ok);
                            dialogButton.setText("Ok");
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                            dialog.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    commonMethods.showMessageDialog(context, "PDF Upload Failed");
                }

            } else {
                commonMethods.showMessageDialog(context, "PDF Upload Failed");
            }
        }
    }

    class DOGHDetailsAsyncTask extends AsyncTask<String, String, String> {


        private volatile boolean running = true;
        private String error = "";

        private TextView tvIAName, tvPolicyNumber, tvAge, tvDOB, tvGender, tvMPHName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
            tvIAName = findViewById(R.id.tvIAName);
            tvPolicyNumber = findViewById(R.id.tvPolicyNumber);
            tvAge = findViewById(R.id.tvAge);
            tvDOB = findViewById(R.id.tvDOB);
            tvGender = findViewById(R.id.tvGender);
            tvMPHName = findViewById(R.id.tvMPHName);

        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;
                //geDOGHdetail_smrt(string strCode, string strEmailId, string strMobileNo, string strAuthKey)
                //geDOGHdetail_smrt(string strCode, string strEmailId, string strMobileNo, string strAuthKey)
                request = new SoapObject(NAMESPACE, METHOD_NAME_DOGH_DETAIL);
                request.addProperty("strCode", strCIFBDMUserId);
                commonMethods.appendSecurityParams(context, request, strCIFBDMEmailId, strCIFBDMMObileNo);

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME_DOGH_DETAIL;
                androidHttpTranport.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();

                if (!response.toString().contentEquals("<NewDataSet />")) {

                    ParseXML prsObj = new ParseXML();
                    String inputpolicylist = response.toString();
                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "NewDataSet");
                    error = prsObj.parseXmlTag(
                            inputpolicylist, "ScreenData");

                         /*<NewDataSet>
                         <Table>
                         <IA_NAME>RUPALI SHIPURKAR</IA_NAME>
                         <AGE>66</AGE>
                         <DOB>23-Nov-1954</DOB>
                         <GENDER>Female</GENDER>
                         <POLICY_NUMBER>60100013607</POLICY_NUMBER>
                         <MPH_NAME>STATE BANK OF INDIA LIFE INSURANCE CO.LTD</MPH_NAME>
                         </Table> </NewDataSet>
                          */
                    if (error == null) {
                        // for agent policy list
                        List<String> Node = prsObj.parseParentNode(inputpolicylist, "Table");
                        IA_NAME = prsObj.parseXmlTag(inputpolicylist, "IA_NAME");
                        IA_NAME = IA_NAME == null ? "" : IA_NAME;

                        DOB = prsObj.parseXmlTag(inputpolicylist, "DOB");
                        DOB = DOB == null ? "" : DOB;

                        POLICY_NUMBER = prsObj.parseXmlTag(inputpolicylist, "POLICY_NUMBER");
                        POLICY_NUMBER = POLICY_NUMBER == null ? "" : POLICY_NUMBER;

                        AGE = prsObj.parseXmlTag(inputpolicylist, "AGE");
                        AGE = AGE == null ? "" : AGE;

                        GENDER = prsObj.parseXmlTag(inputpolicylist, "GENDER");
                        GENDER = GENDER == null ? "" : GENDER;

                        MPH_NAME = prsObj.parseXmlTag(inputpolicylist, "MPH_NAME");
                        MPH_NAME = MPH_NAME == null ? "" : MPH_NAME;

                        error = "success";
                    } else {
                        error = "1";
                    }

                } else {
                    error = "1";
                }

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
                if (error.equalsIgnoreCase("success")) {
                    llDOGHCovidDetails.setVisibility(View.VISIBLE);
                    tvIAName.setText(IA_NAME);
                    tvPolicyNumber.setText(POLICY_NUMBER);
                    tvAge.setText(AGE);
                    tvDOB.setText(DOB);
                    tvGender.setText(GENDER);
                    tvMPHName.setText(MPH_NAME);

                    DOGHdeclarationString = "I hereby declare that I am joining the Group Policy on my own volition. I have been duly apprised of the benefits, terms and conditions of\n" +
                            "the Group Policy by the Master Policyholder.\n\nFurther, I consent on my enrolment of new membership under Group Insurance Policy, issued under the product SBI Life Sampoorn Suraksha. " +
                            "\n\nI declare that I am presently in sound mental and physical health. " +
                            "\nI also declare that I do not have any physical defect/deformity, and perform my routine activities independently. I have never suffered from nor am I currently not suffering from diabetes, hypertension (high blood-pressure), epilepsy, or tuberculosis or genetic disorder." +
                            "\nI have not been tested positive for Hepatitis B, Hepatitis C, or HIV and have " +
                            "not been treated or hospitalized in connection with alcohol, narcotic drugs or " +
                            "tobacco consumption. During the last 3 years, I have not been hospitalized for " +
                            "any ailment or disease. I have not taken any treatment nor am I currently receiving " +
                            "any treatment nor have I been advised to undergo medical tests or follow any prescribed " +
                            "line of treatment, for critical illness@ in the past or in the present." +
                            "\n\n         @ A Critical Illness is defined as any one of the following: " +
                            "\n         [1] have suffered or be suffering from cancer, " +
                            "\n         [2] be advised or be taking treatment for any heart disease," +
                            "\n         [3] have undergone any major surgery requiring full anaesthesia during the last 12 months, " +
                            "\n         [4] have undergone major organ transplant, " +
                            "\n         [5] have been advised medically to undergo chest/heart surgery or surgery requiring full anaesthesia within the following six months from the date of declaration, " +
                            "\n         [6] have kidney and/or liver failure, " +
                            "\n         [7] have suffered or be suffering from stroke, paralysis, or any mental illness,  " +
                            "\n         [8] have suffered or is suffering from any chronic, irreversible disease of the lungs or brain or liver, " +
                            "\n         [9] have suffered or be suffering from AIDS or venereal diseases." +
                            "\nI Declare that my work does not involve working in mine or exposure to harmful substances, chemical or gas";

                    checkBoxDisclaimer.setText(DOGHdeclarationString);

                    DOGHdeclarationSecondString = "";
                    if (GENDER.equalsIgnoreCase("female")) {
                        DOGHdeclarationSecondString += "For females only, " +
                                "\nI am not pregnant and there is no history of miscarriage, abortion or other gynaecological disorders in last three months.\n\n";
                    }

                    DOGHdeclarationSecondString += "I hereby understand and agree that no insurance cover will commence until the risk is " +
                            "accepted and requisite premium has been received by SBI Life and SBI Life conveys its written " +
                            "acceptance of this application for life insurance cover. I further understand and agree that life " +
                            "insurance cover provided to me shall be governed by the Master Policy Contract issued in favour of " +
                            "the Master Policyholder. Notwithstanding the provision of any law, usage, custom or convention for" +
                            " the time being in force prohibiting any doctor, hospital and/or employer from divulging any " +
                            "knowledge or information about me concerning my health, employment on the grounds of secrecy. " +
                            "I, my heirs, executors, administrator or any other person or persons having interest of any " +
                            "kind whatsoever in the life insurance cover provided to me, hereby agree that such authority, " +
                            "having such knowledge or information, shall at anytime be at liberty to divulge any such knowledge" +
                            " or information to the Company." +
                            "\n\nI hereby declare and agree that the foregoing declaration has been given after fully " +
                            "understanding the same and is true and complete to the best of my knowledge and that I have " +
                            "not withheld any information that may influence my admission into the Group Insurance Plan of SBI Life." +
                            "\n\nI hereby agree that this form including the declaration shall form the " +
                            "basis of my admission into the Group Insurance Plan and if any untrue statement " +
                            "be contained therein, I, my heirs, executors, administrators and assignees shall " +
                            "not be entitled to receive any benefits under the Group Insurance Plan, I also agree " +
                            "that the Company shall not be liable for any claim on account of illness, injury. " +
                            "or death, the cause of which was known prior to approval of my request for " +
                            "assurance or if I have withheld or concealed any material information in the " +
                            "above statements.";


                    checkBoxDisclaimerSecond.setText(DOGHdeclarationSecondString);

                } else {
                    commonMethods.showMessageDialog(context, "No record found");
                }
            }
        }
    }

    class SaveCovidDataAsyncTask extends AsyncTask<String, String, String> {

        private final String METHOD_NAME = "saveCOVIDdet_smrt";
        private volatile boolean running = true;
        private String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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
                //saveCOVIDdet_smrt(stringxmlStr, string strEmailId,string strMobileNo,string strAuthKey)
                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("xmlStr", generateXmlString());
                commonMethods.appendSecurityParams(context, request, strCIFBDMEmailId, strCIFBDMMObileNo);
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
                result = response.toString();

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
                if (result.equalsIgnoreCase("1")) {
                    uploadPDFService = new UploadPDFService("DOGH");
                    uploadPDFService.execute();
                } else if (result.equalsIgnoreCase("2")) {
                    commonMethods.showMessageDialog(context, "Data Already Exist");
                } else if (result.equalsIgnoreCase("0")) {
                    commonMethods.showMessageDialog(context, "Data Saving Failed");
                } else {
                    commonMethods.showMessageDialog(context, "Data Saving Failed");
                }
            } else {
                commonMethods.showMessageDialog(context, "Data Saving Failed");
            }
        }
    }


}