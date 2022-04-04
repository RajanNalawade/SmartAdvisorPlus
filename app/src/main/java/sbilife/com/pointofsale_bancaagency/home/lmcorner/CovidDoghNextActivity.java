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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import java.util.ArrayList;
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

public class CovidDoghNextActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener,
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

    private String covidQueNoOneRBString, covidQueNoTwoRBString, nomineeDOB,
            covidQueNoThreeRBString, covidQueNoFourRBString, covidQueNoFiveRBString, covidQueNoSixRBString,
            designation, natureOfDuty, nomineeTitle, nomineeName, nomineeRelation, addressOne, addressTwo, addressThree,
            taluka, distric, state, PIN, contactNumber, place, marritalStatusRBString, residenceStatusRBString, countryResidence,
            majorSurgeryRBString, anyCovidQuestYesAnsweredDetails, heights, weight, multipleDiseaseRBString,
            cigarBidiRBString, sticksPackets, noOfYearsUsed, proposalRejectionRBString, anyQuestYesHealthDetails;

    private String OTP = "", IA_NAME, AGE, DOB, GENDER, POLICY_NUMBER, MPH_NAME, TYPE;
    private RadioGroup radioGroupMarritalStatus, radioGroupResidentStatus, radioGroupMajorSurgeryDetails,
            radioGroupSufferedDisease, radioGroupCigarBidi, radioGroupProposalRejection,
            radioGroupCovidQuesNoOne, radioGroupCovidQuesNoTwo, radioGroupCovidQueNoThree, radioGroupCovidQueNoFour,
            radioGroupCovidQueNoFive, radioGroupCovidQueNoSix;
    private Spinner spinnerNomineeTitle;
    private EditText etDesignation, etDutyNature, etNomineeName, etNomineeRelation,
            etAddressOne, etAddressTwo, etAddressThree, etAddressTaluka, etAddressDistrict, etAddressState, etAddressPIN,
            etContactNumber, etHeights, etWeight, etSticksPacketsCigarBidi,
            etNoOfYearsUsedCigarBidi, etOTP, edittextPlace, etAnyQuestCoronaDetails, etAnyQuestHealthDetails;
    private TextView tvNomineeDOB;
    private LinearLayout llCigaretteBidiDetails, llCovidQuestions, llDOGHQuestion;
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
    private AutoCompleteTextView autoCompleteTextViewCountryResidence;
    private CheckBox checkBoxDisclaimer, checkBoxCovidDisclaimer, checkBoxDisclaimerSecond, checkBoxDisclaimerIlleterate;
    private LinearLayout llDOGHCovidDetails;//, llMajorSurgery;
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
        setContentView(R.layout.activity_covid_dogh_next);
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

        llCovidQuestions = findViewById(R.id.llCovidQuestions);
        llCovidQuestions.setVisibility(View.GONE);

        llDOGHQuestion = findViewById(R.id.llDOGHQuestion);
        llDOGHQuestion.setVisibility(View.GONE);

        /*llMajorSurgery = findViewById(R.id.llMajorSurgery);
        llMajorSurgery.setVisibility(View.GONE);*/


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

        radioGroupCovidQuesNoOne = findViewById(R.id.radioGroupCovidQuesNoOne);
        radioGroupCovidQuesNoTwo = findViewById(R.id.radioGroupCovidQuesNoTwo);
        radioGroupCovidQueNoThree = findViewById(R.id.radioGroupCovidQueNoThree);
        radioGroupCovidQueNoFour = findViewById(R.id.radioGroupCovidQueNoFour);
        radioGroupCovidQueNoFive = findViewById(R.id.radioGroupCovidQueNoFive);
        radioGroupCovidQueNoSix = findViewById(R.id.radioGroupCovidQueNoSix);

        radioGroupMarritalStatus = findViewById(R.id.radioGroupMarritalStatus);
        radioGroupResidentStatus = findViewById(R.id.radioGroupResidentStatus);
        radioGroupMajorSurgeryDetails = findViewById(R.id.radioGroupMajorSurgeryDetails);
        radioGroupSufferedDisease = findViewById(R.id.radioGroupSufferedDisease);
        radioGroupCigarBidi = findViewById(R.id.radioGroupCigarBidi);
        radioGroupProposalRejection = findViewById(R.id.radioGroupProposalRejection);

        llCigaretteBidiDetails = findViewById(R.id.llCigaretteBidiDetails);
        llCigaretteBidiDetails.setVisibility(View.GONE);

        etHeights = findViewById(R.id.etHeights);
        etWeight = findViewById(R.id.etWeight);
        etSticksPacketsCigarBidi = findViewById(R.id.etSticksPacketsCigarBidi);
        etNoOfYearsUsedCigarBidi = findViewById(R.id.etNoOfYearsUsedCigarBidi);
        etDesignation = findViewById(R.id.etDesignation);
        etDutyNature = findViewById(R.id.etDutyNature);
        etNomineeName = findViewById(R.id.etNomineeName);
        etNomineeRelation = findViewById(R.id.etNomineeRelation);
        etAddressOne = findViewById(R.id.etAddressOne);
        etAddressTwo = findViewById(R.id.etAddressTwo);
        etAddressThree = findViewById(R.id.etAddressThree);
        etAddressTaluka = findViewById(R.id.etAddressTaluka);
        etAddressDistrict = findViewById(R.id.etAddressDistrict);
        etAddressState = findViewById(R.id.etAddressState);
        etAddressPIN = findViewById(R.id.etAddressPIN);
        etContactNumber = findViewById(R.id.etContactNumber);

        etOTP = findViewById(R.id.etOTP);
        edittextPlace = findViewById(R.id.edittextPlace);
        etAnyQuestCoronaDetails = findViewById(R.id.etAnyQuestCoronaDetails);
        etAnyQuestHealthDetails = findViewById(R.id.etAnyQuestHealthDetails);

        tvNomineeDOB = findViewById(R.id.tvNomineeDOB);
        spinnerNomineeTitle = findViewById(R.id.spinnerNomineeTitle);

        List<String> titleArray = new ArrayList<>();
        titleArray.add("Select Title");
        titleArray.add("Mr.");
        titleArray.add("Mrs.");
        titleArray.add("Ms.");
        titleArray.add("Dr.");
        ArrayAdapter<String> Adapter_PremiumPaymentOption = new ArrayAdapter<>(
                CovidDoghNextActivity.this, android.R.layout.simple_spinner_item, titleArray);
        Adapter_PremiumPaymentOption.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNomineeTitle.setAdapter(Adapter_PremiumPaymentOption);

        autoCompleteTextViewCountryResidence = findViewById(R.id.autoCompleteTextViewCountryResidence);

        String[] countryList = {"Select Country",
                "India", "United States (USA)",
                "United Kingdom (UK)", "United Arab Emirates(UAE)",
                "Turkey", "Taiwan", "Switzerland", "Sweden", "Spain", "South Korea",
                "Singapore", "Qatar", "Portugal", "Oman", "New Zealand", "Norway",
                "Malaysia", "Luxembourg",
                "Kuwait",
                "Japan",
                "Italy",
                "Ireland",
                "Hungary",
                "Netherlands (Holland)",
                "Hong Kong",
                "Greece",
                "Germany",
                "France",
                "Finland",
                "Egypt",
                "Denmark",
                "Czech Republic",
                "Cyprus",
                "Canada",
                "Belgium",
                "Bahrain",
                "Austria",
                "Australia",
                "Russia",
                "Indonesia",
                "Kenya",
                "Abidjan (West Africa)",
                "Nepal",
                "Zimbabwe",
                "Zambia",
                "Zaire",
                "Yugoslavia",
                "Yemen",
                "Western Sahara",
                "Uganda",
                "Tokelau",
                "Tonga",
                "Togo",
                "Tajikistan",
                "Tanzania",
                "Swaziland",
                "Suriname",
                "Sudan",
                "Somalia",
                "Solomon Islands",
                "Sierra Leone",
                "Serbia",
                "Senegal",
                "Rwanda",
                "Panama",
                "Palestine",
                "Palau",
                "Nigeria",
                "Niger",
                "Nicaragua",
                "Namibia",
                "Mozambique",
                "Morocco",
                "Montserrat",
                "Mongolia",
                "Moldova",
                "Mali",
                "Malawi",
                "Madagascar",
                "Macedonia",
                "Libya",
                "Liberia",
                "Lesotho",
                "Lebanon",
                "Ivory Coast",
                "Honduras",
                "Haiti",
                "Guyana",
                "Guinea-Bissau",
                "Guatemala",
                "Ghana",
                "Georgia",
                "Gambia",
                "Gabon",
                "Federated States of Micronesia",
                "Ethiopia",
                "Estonia",
                "Eritrea",
                "Equatorial Guinea",
                "El Salvador",
                "Djibouti",
                "Curacao",
                "Cuba",
                "Congo",
                "Comoros",
                "Chad",
                "Central African Republic",
                "Cayman Islands",
                "Cape Verde",
                "Canary Islands",
                "Cameroon",
                "Cambodia (Kampuchea)",
                "Burundi",
                "Burkina Faso",
                "Botswana",
                "Bosnia-Herzegovina",
                "Benin",
                "Antigua, Barbuda",
                "Anguilla",
                "Angola",
                "Albania",
                "Papua New Guinea",
                "Marshall Islands",
                "Kiribati",
                "Vietnam - Danag City",
                "Bhutan - Shemgag",
                "Phillipines - Manila",
                "Phillipines - Lucena City",
                "Russia - Valadivostok-Nerchinskay",
                "Russia - St. Petersbury",
                "Bangladesh - Wardha",
                "Bangladesh - Dhaka",
                "Indonesia - Tangerang",
                "Indonesia - Jakarta",
                "Indonesia - Purwakarta",
                "KSA - Al Alsa",
                "KSA - Jeddah",
                "KSA - Riyadh",
                "Kingdom Of Saudi Arabia",
                "Brunei",
                "Jordon",
                "Walmer South Africa",
                "Belarus",
                "Uzbekistan",
                "Turkmenistan",
                "Tajikstan",
                "Sao Tome Principe",
                "Pakistan",
                "Northern Parts of Cyprus",
                "Myanmar",
                "Laos",
                "Kazakhstan",
                "Iraq",
                "Iran",
                // India",
                "All Central and South American countries",
                "Cambodia",
                "Azerbaijan",
                "Armenia",
                "Algeria",
                "Afghanistan",
                "North Korea",
                "Krygystan",
                "Phillipines",
                "American Samoa",
                "Andorra",
                "Antarctica",
                "Antigua and Barbuda",
                "Argentina",
                "Aruba",
                "Ashmore and Cartier Islands",
                "Bahamas, The",
                "Barbados",
                "Bassas da India",
                "Belize",
                "Bermuda",
                "Bolivia",
                "Bosnia and Herzegovina",
                "Bouvet Island",
                "Brazil",
                "British Indian Ocean Territory",
                "British Virgin Islands",
                "Bulgaria",
                "Burma",
                "Chile",
                "China",
                "Christmas Island",
                "Clipperton Island",
                "Cocos (Keeling) Islands",
                "Colombia",
                "Congo, Democratic Republic of the",
                "Congo, Republic of the",
                "Cook Islands",
                "Coral Sea Islands",
                "Costa Rica",
                "Cote dIvoire",
                "Croatia",
                "Dhekelia",
                "Dominica",
                "Dominican Republic",
                "Ecuador",
                "Europa Island",
                "Falkland Islands (Islas Malvinas)",
                "Faroe Islands",
                "Fiji",
                "French Guiana",
                "French Polynesia",
                "French Southern and Antarctic Lands",
                "Gambia,The",
                "Gaza Strip",
                "Gibraltar",
                "Glorioso Islands",
                "Greenland",
                "Grenada",
                "Guadeloupe",
                "Guam",
                "Guernsey",
                "Guinea",
                "Heard Island and McDonald Islands",
                "Holy See (Vatican City)",
                "Iceland",
                "Isle of Man",
                "Israel",
                "Jamaica",
                "Jan Mayen",
                "Jersey",
                "Jordan",
                "Juan de Nova Island",
                "Korea, North",
                "Kyrgyzstan",
                "Latvia",
                "Liechtenstein",
                "Lithuania",
                "Macau",
                "Maldives",
                "Malta",
                "Martinique",
                "Mauritania",
                "Mauritius",
                "Mayotte",
                "Mexico",
                "Micronesia Federated States of",
                "Monaco",
                "Nauru",
                "Navassa Island",
                "Netherlands Antilles",
                "New Caledonia",
                "Niue",
                "Norfolk Island",
                "Northern Mariana Islands",
                "Paracel Islands",
                "Paraguay",
                "Peru",
                "Philippines",
                "Pitcairn Islands",
                "Poland",
                "Puerto Rico",
                "Reunion",
                "Romania",
                "Saint Helena",
                "Saint Kitts and Nevis",
                "Saint Lucia",
                "Saint Pierre and Miquelon",
                "Saint Vincent and the Grenadines",
                "Samoa",
                "San Marino",
                "Sao Tome and Principe",
                "Saudi Arabia",
                "Serbia and Montenegro",
                "Seychelles",
                "Slovakia",
                "Slovenia",
                "South Africa",
                "South Georgia and the South Sandwich Islands",
                "Spratly Islands",
                "Sri Lanka",
                "Svalbard",
                "Syria",
                "Thailand",
                "Tibet",
                "Timor-Leste",
                "Trinidad and Tobago",
                "Tromelin Island",
                "Tunisia",
                "Turks and Caicos Islands",
                "Tuvalu",
                "Ukraine",
                "Uruguay",
                "Vanuatu",
                "Venezuela",
                "Virgin Islands", "Wake Island", "Wallis and Futuna", "West Bank", "Akrotiri"};


        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, countryList);
        //Getting the instance of AutoCompleteTextView
        autoCompleteTextViewCountryResidence.setThreshold(1);//will start working from first character
        autoCompleteTextViewCountryResidence.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

        tvNomineeDOB.setOnClickListener(this);

        radioGroupCovidQuesNoOne.setOnCheckedChangeListener(this);
        radioGroupCovidQuesNoTwo.setOnCheckedChangeListener(this);
        radioGroupCovidQueNoThree.setOnCheckedChangeListener(this);
        radioGroupCovidQueNoFour.setOnCheckedChangeListener(this);
        radioGroupCovidQueNoFive.setOnCheckedChangeListener(this);
        radioGroupCovidQueNoSix.setOnCheckedChangeListener(this);


        radioGroupMarritalStatus.setOnCheckedChangeListener(this);
        radioGroupResidentStatus.setOnCheckedChangeListener(this);
        radioGroupMajorSurgeryDetails.setOnCheckedChangeListener(this);
        radioGroupSufferedDisease.setOnCheckedChangeListener(this);
        radioGroupCigarBidi.setOnCheckedChangeListener(this);
        radioGroupProposalRejection.setOnCheckedChangeListener(this);

        buttonValidateOTP = findViewById(R.id.buttonValidateOTP);
        buttonGenerateOTP = findViewById(R.id.buttonGenerateOTP);

        buttonGenerateOTP.setOnClickListener(this);
        buttonValidateOTP.setOnClickListener(this);

        checkBoxDisclaimer = findViewById(R.id.checkBoxDisclaimer);
        checkBoxCovidDisclaimer = findViewById(R.id.checkBoxCovidDisclaimer);
        checkBoxDisclaimerSecond = findViewById(R.id.checkBoxDisclaimerSecond);
        checkBoxDisclaimerIlleterate = findViewById(R.id.checkBoxDisclaimerIlleterate);

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
        service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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
            case R.id.tvNomineeDOB:
                datecheck = 2;
                showDateDialog();
                break;


        }
    }

    private void validation(String flag) {

        commonMethods.hideKeyboard(etOTP, context);
        if (TYPE.equalsIgnoreCase("CHQ")) {
            if (!checkBoxDisclaimer.isChecked()) {
                commonMethods.showMessageDialog(context, "Please accept the declaration.");
                commonMethods.setFocusable(checkBoxDisclaimer);
                checkBoxDisclaimer.requestFocus();
                return;
            }

            if (!checkBoxCovidDisclaimer.isChecked()) {
                commonMethods.showMessageDialog(context, "Please accept the declaration to be given by the Proposer/Life to be assured.");
                commonMethods.setFocusable(checkBoxCovidDisclaimer);
                checkBoxCovidDisclaimer.requestFocus();
                return;
            }

            designation = etDesignation.getText().toString().trim();
            if (TextUtils.isEmpty(designation)) {
                commonMethods.showMessageDialog(context, "Please enter Designation");
                commonMethods.setFocusable(etDesignation);
                etDesignation.requestFocus();
                return;
            }

            natureOfDuty = etDutyNature.getText().toString().trim();
            if (TextUtils.isEmpty(natureOfDuty)) {
                commonMethods.showMessageDialog(context, "Please enter Nature Of Duties");
                commonMethods.setFocusable(etDutyNature);
                etDutyNature.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(marritalStatusRBString)) {
                commonMethods.showMessageDialog(context, "Please select Marrital Status");
                commonMethods.setFocusable(radioGroupMarritalStatus);
                radioGroupMarritalStatus.requestFocus();
                return;
            }

            nomineeTitle = spinnerNomineeTitle.getSelectedItem().toString();
            if (!TextUtils.isEmpty(nomineeTitle) && nomineeTitle.equalsIgnoreCase("Select Title")) {
                nomineeTitle = "";
                commonMethods.showMessageDialog(context, "Please Select Nominee Title");
                commonMethods.setFocusable(spinnerNomineeTitle);
                spinnerNomineeTitle.requestFocus();
                return;
            }

            nomineeName = etNomineeName.getText().toString().trim();
            if (TextUtils.isEmpty(nomineeName)) {
                commonMethods.showMessageDialog(context, "Please enter Nominee Name");
                commonMethods.setFocusable(etNomineeName);
                etNomineeName.requestFocus();
                return;
            }

            nomineeDOB = tvNomineeDOB.getText().toString().trim();
            if (TextUtils.isEmpty(nomineeDOB)) {
                commonMethods.showMessageDialog(context, "Please enter Nominee DOB");
                commonMethods.setFocusable(tvNomineeDOB);
                tvNomineeDOB.requestFocus();
                return;
            }

            nomineeRelation = etNomineeRelation.getText().toString().trim();
            if (TextUtils.isEmpty(nomineeRelation)) {
                commonMethods.showMessageDialog(context, "Please enter Nominee Relation");
                commonMethods.setFocusable(etNomineeRelation);
                etNomineeRelation.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(residenceStatusRBString)) {
                commonMethods.showMessageDialog(context, "Please check Residence Status");
                commonMethods.setFocusable(radioGroupResidentStatus);
                radioGroupResidentStatus.requestFocus();
                return;
            }

            countryResidence = autoCompleteTextViewCountryResidence.getEditableText().toString().trim();
            if (!TextUtils.isEmpty(countryResidence) && countryResidence.equalsIgnoreCase("Select Country")) {
                commonMethods.showMessageDialog(context, "Please enter Country of Residence");
                commonMethods.setFocusable(autoCompleteTextViewCountryResidence);
                autoCompleteTextViewCountryResidence.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(countryResidence)) {
                commonMethods.showMessageDialog(context, "Please enter Country of Residence");
                commonMethods.setFocusable(autoCompleteTextViewCountryResidence);
                autoCompleteTextViewCountryResidence.requestFocus();
                return;
            }

            addressOne = etAddressOne.getText().toString().trim();
            if (TextUtils.isEmpty(addressOne)) {
                commonMethods.showMessageDialog(context, "Please enter Address One");
                commonMethods.setFocusable(etAddressOne);
                etAddressOne.requestFocus();
                return;
            }

            addressTwo = etAddressTwo.getText().toString().trim();
       /* if (TextUtils.isEmpty(addressTwo)) {
            commonMethods.showMessageDialog(context, "Please enter Address Two");
            commonMethods.setFocusable(etAddressTwo);
            etAddressTwo.requestFocus();
            return;
        }*/

            addressThree = etAddressThree.getText().toString().trim();
        /*if (TextUtils.isEmpty(addressThree)) {
            commonMethods.showMessageDialog(context, "Please enter Address Three");
            commonMethods.setFocusable(etAddressThree);
            etAddressThree.requestFocus();
            return;
        }*/
            taluka = etAddressTaluka.getText().toString().trim();
            distric = etAddressDistrict.getText().toString().trim();

            state = etAddressState.getText().toString().trim();
            if (TextUtils.isEmpty(state)) {
                commonMethods.showMessageDialog(context, "Please enter State");
                commonMethods.setFocusable(etAddressState);
                etAddressState.requestFocus();
                return;
            }

            PIN = etAddressPIN.getText().toString().trim();
            if (TextUtils.isEmpty(PIN)) {
                commonMethods.showMessageDialog(context, "Please enter PIN Code");
                commonMethods.setFocusable(etAddressPIN);
                etAddressPIN.requestFocus();
                return;
            }

            contactNumber = etContactNumber.getText().toString().trim();
            if (TextUtils.isEmpty(contactNumber)) {
                commonMethods.showMessageDialog(context, "Please enter Contact Number");
                commonMethods.setFocusable(etContactNumber);
                etContactNumber.requestFocus();
                return;
            }

            heights = etHeights.getText().toString().trim();
            if (TextUtils.isEmpty(heights)) {
                commonMethods.showMessageDialog(context, "Please enter Height(in Cms)");
                commonMethods.setFocusable(etHeights);
                etHeights.requestFocus();
                return;
            } /*else if (!TextUtils.isEmpty(heights) && Double.parseDouble(heights) < 132.0) {
            commonMethods.showMessageDialog(context, "Minimum height is 132(in Cms)");
            commonMethods.setFocusable(etHeights);
            etHeights.requestFocus();
            return;
        }*/

            weight = etWeight.getText().toString().trim();
            if (TextUtils.isEmpty(weight)) {
                commonMethods.showMessageDialog(context, "Please enter Weight(in Kgs)");
                commonMethods.setFocusable(etWeight);
                etWeight.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(majorSurgeryRBString)) {
                commonMethods.showMessageDialog(context, "Please give Treatment/Major Surgery Details");
                commonMethods.setFocusable(radioGroupMajorSurgeryDetails);
                radioGroupMajorSurgeryDetails.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(multipleDiseaseRBString)) {
                commonMethods.showMessageDialog(context, "Please give Multiple Disease Details");
                commonMethods.setFocusable(radioGroupSufferedDisease);
                radioGroupSufferedDisease.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(cigarBidiRBString)) {
                commonMethods.showMessageDialog(context, "Please give Cigarette/Bidi Consumption Details");
                commonMethods.setFocusable(radioGroupCigarBidi);
                radioGroupCigarBidi.requestFocus();
                return;
            } else {
                if (cigarBidiRBString.equalsIgnoreCase("yes")) {

                    sticksPackets = etSticksPacketsCigarBidi.getText().toString().trim();
                    noOfYearsUsed = etNoOfYearsUsedCigarBidi.getText().toString().trim();
                    if (TextUtils.isEmpty(sticksPackets)) {
                        commonMethods.showMessageDialog(context, "Please enter Sticks/Packets Per day");
                        commonMethods.setFocusable(etSticksPacketsCigarBidi);
                        etSticksPacketsCigarBidi.requestFocus();
                        return;
                    }

                    if (TextUtils.isEmpty(noOfYearsUsed)) {
                        commonMethods.showMessageDialog(context, "Please enter number of years used");
                        commonMethods.setFocusable(etNoOfYearsUsedCigarBidi);
                        etNoOfYearsUsedCigarBidi.requestFocus();
                        return;
                    }
                } else {
                    sticksPackets = "";
                    noOfYearsUsed = "";
                    etSticksPacketsCigarBidi.setText("");
                    etNoOfYearsUsedCigarBidi.setText("");
                }
            }

            if (TextUtils.isEmpty(proposalRejectionRBString)) {
                commonMethods.showMessageDialog(context, "Please give Proposal Rejection(if any) Details");
                commonMethods.setFocusable(radioGroupProposalRejection);
                radioGroupProposalRejection.requestFocus();
                return;
            }

            if (majorSurgeryRBString.equalsIgnoreCase("yes") || multipleDiseaseRBString.equalsIgnoreCase("yes")
                    || cigarBidiRBString.equalsIgnoreCase("yes") || proposalRejectionRBString.equalsIgnoreCase("yes")) {

                anyQuestYesHealthDetails = etAnyQuestHealthDetails.getText().toString();
                if (TextUtils.isEmpty(anyQuestYesHealthDetails)) {
                    commonMethods.showMessageDialog(context, "Please provide details for question answered Yes.");
                    commonMethods.setFocusable(etAnyQuestHealthDetails);
                    etAnyQuestHealthDetails.requestFocus();
                    return;
                }
            }

            if (!checkBoxDisclaimerSecond.isChecked()) {
                commonMethods.showMessageDialog(context, "Please accept the declaration.");
                commonMethods.setFocusable(checkBoxDisclaimerSecond);
                checkBoxDisclaimerSecond.requestFocus();
                return;
            }

            if (!checkBoxDisclaimerIlleterate.isChecked()) {
                commonMethods.showMessageDialog(context, "Please accept the declaration.");
                commonMethods.setFocusable(checkBoxDisclaimerIlleterate);
                checkBoxDisclaimerIlleterate.requestFocus();
                return;
            }

        } else if (TYPE.equalsIgnoreCase("Covid Q")) {

            if (TextUtils.isEmpty(covidQueNoOneRBString)) {
                commonMethods.showMessageDialog(context, "Please check Question 1 of COVID Questionnaire");
                commonMethods.setFocusable(radioGroupCovidQuesNoOne);
                radioGroupCovidQuesNoOne.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(covidQueNoTwoRBString)) {
                commonMethods.showMessageDialog(context, "Please check Question 2 of COVID Questionnaire");
                commonMethods.setFocusable(radioGroupCovidQuesNoTwo);
                radioGroupCovidQuesNoTwo.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(covidQueNoThreeRBString)) {
                commonMethods.showMessageDialog(context, "Please check Question 3 of COVID Questionnaire");
                commonMethods.setFocusable(radioGroupCovidQueNoThree);
                radioGroupCovidQueNoThree.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(covidQueNoFourRBString)) {
                commonMethods.showMessageDialog(context, "Please check Question 4 of COVID Questionnaire");
                commonMethods.setFocusable(radioGroupCovidQueNoFour);
                radioGroupCovidQueNoFour.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(covidQueNoFiveRBString)) {
                commonMethods.showMessageDialog(context, "Please check Question 5 of COVID Questionnaire");
                radioGroupCovidQueNoFive.setFocusable(true);
                radioGroupCovidQueNoFive.setFocusableInTouchMode(true);
                radioGroupCovidQueNoFive.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(covidQueNoSixRBString)) {
                commonMethods.showMessageDialog(context, "Please check Question 6 of COVID Questionnaire");
                commonMethods.setFocusable(radioGroupCovidQueNoSix);
                radioGroupCovidQueNoSix.requestFocus();
                return;
            }

            if (covidQueNoThreeRBString.equalsIgnoreCase("yes") || covidQueNoFourRBString.equalsIgnoreCase("yes")
                    || covidQueNoFiveRBString.equalsIgnoreCase("yes") || covidQueNoOneRBString.equalsIgnoreCase("yes")
                    || covidQueNoTwoRBString.equalsIgnoreCase("yes") || covidQueNoSixRBString.equalsIgnoreCase("yes")) {

                anyCovidQuestYesAnsweredDetails = etAnyQuestCoronaDetails.getText().toString();
                if (TextUtils.isEmpty(anyCovidQuestYesAnsweredDetails)) {
                    commonMethods.showMessageDialog(context, "Please provide details for question answered Yes.");
                    commonMethods.setFocusable(etAnyQuestCoronaDetails);
                    etAnyQuestCoronaDetails.requestFocus();
                    return;
                }
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
            generateOTPGeneralAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            validateOTPGeneralAsyncTask = new ValidateOTPGeneralAsyncTask(strCIFBDMMObileNo,
                    OTP, this, context);
            validateOTPGeneralAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
            createPDFAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

            case R.id.radioGroupMajorSurgeryDetails:
                majorSurgeryRBString = text;
                break;
            case R.id.radioGroupMarritalStatus:
                marritalStatusRBString = text;
                break;
            case R.id.radioGroupResidentStatus:
                residenceStatusRBString = text;
                break;
            case R.id.radioGroupSufferedDisease:
                multipleDiseaseRBString = text;
                break;
            case R.id.radioGroupCigarBidi:
                cigarBidiRBString = text;
                if (cigarBidiRBString.equalsIgnoreCase("yes")) {
                    etSticksPacketsCigarBidi.setEnabled(true);
                    etNoOfYearsUsedCigarBidi.setEnabled(true);
                    llCigaretteBidiDetails.setVisibility(View.VISIBLE);
                } else {
                    etSticksPacketsCigarBidi.setEnabled(false);
                    etNoOfYearsUsedCigarBidi.setEnabled(false);
                    llCigaretteBidiDetails.setVisibility(View.GONE);
                }
                break;
            case R.id.radioGroupProposalRejection:
                proposalRejectionRBString = text;
                break;
            case R.id.radioGroupCovidQuesNoOne:
                covidQueNoOneRBString = text;
                break;

            case R.id.radioGroupCovidQuesNoTwo:
                covidQueNoTwoRBString = text;
                break;

            case R.id.radioGroupCovidQueNoThree:
                covidQueNoThreeRBString = text;

                break;

            case R.id.radioGroupCovidQueNoFour:
                covidQueNoFourRBString = text;

                break;

            case R.id.radioGroupCovidQueNoFive:
                covidQueNoFiveRBString = text;
                break;

            case R.id.radioGroupCovidQueNoSix:
                covidQueNoSixRBString = text;
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
            tvNomineeDOB.setText(totaldate);
            nomineeDOB = totaldate;
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
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidths(new float[]{5f, 5f});
            headerTable.setWidthPercentage(100f);
            headerTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell(new Phrase("COVID Questionnaire (UWM029)",
                    headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            headerTable.addCell(cell);

            cell = new PdfPCell(new Phrase("(For Group Business - To be filled by Proposer/Life to be Assured)",
                    headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            headerTable.addCell(cell);
            document.add(headerTable);
            document.add(para_img_logo_after_space_1);

            PdfPTable policyNumberTable = new PdfPTable(2);
            policyNumberTable.setWidths(new float[]{5f, 5f});
            policyNumberTable.setWidthPercentage(100f);
            policyNumberTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell(new Phrase("Policy Number : ", small_bold));
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
            question1Table.setWidths(new float[]{3f, 1f});
            question1Table.setWidthPercentage(100f);
            question1Table.setHorizontalAlignment(Element.ALIGN_LEFT);

            //Question 1 cell
            cell = new PdfPCell(new Phrase(
                    "1)In last one month did you have symptoms like loss of sense of smell or taste, any " +
                            "fever, Cough, Shortness of breath, Malaise (flu-like tiredness), Rhinorrhea (mucus " +
                            "discharge from the nose/running nose), Sore throat, or Gastro-intestinal " +
                            "symptoms such as nausea, vomiting and/or diarrhea, body ache or backache?", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);


            //Column 2
            cell = new PdfPCell(new Phrase(
                    covidQueNoOneRBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);


            cell = new PdfPCell(new Phrase(
                    "2) Have you or your family member(s) been diagnosed with Covid 19 or have " +
                            "been advised to take Covid 19 test or kept in quarantine where results " +
                            "are awaited?", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    covidQueNoTwoRBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);


            cell = new PdfPCell(new Phrase(
                    "3) Have you or your family member(s) been in close contact with anyone " +
                            "who has been quarantined / who has been diagnosed with COVID-19 OR " +
                            "advised to be tested for COVID19 OR awaiting the result of COVID19 test?", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    covidQueNoThreeRBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);


            cell = new PdfPCell(new Phrase(
                    "4) Have you or your family members travelled overseas over last 2 months OR plan to travel during the next 6 months ?", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    covidQueNoFourRBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);


            cell = new PdfPCell(new Phrase(
                    "5) Do you work in an occupation (like health care worker/Corona warrior etc) " +
                            "where you have a higher risk to get in close contact with COVID-19 patients?", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    covidQueNoFiveRBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "6) Have you experienced any adverse reaction post COVID19 vaccination? (if " +
                            "vaccinated) ", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    covidQueNoSixRBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Details for question answered Yes", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    anyCovidQuestYesAnsweredDetails, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);

            document.add(question1Table);


            PdfPTable declarationTable = new PdfPTable(1);
            declarationTable.setWidthPercentage(100f);
            declarationTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell((new Phrase("Declaration to be given by the Proposer/Life to be assured", headerBold)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            declarationTable.addCell(cell);

            cell = new PdfPCell((new Phrase("I declare that the answers given above are true and to the best of my knowledge and that I " +
                    "have not withheld any material information that may influence the assessment or " +
                    "acceptance of this application. I agree that this form will constitute part of my application " +
                    "for life Insurance and that failure to disclose any material fact known to me may invalidate " +
                    "the contract.", small_bold)));
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

            DOGHFilePath = mStorageUtils.createFileToAppSpecificDir(context,
                    strCIFBDMUserId + "_DOGH" + "_.pdf");

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
                    "Health Questionnaire for Group Life Insurance",
                    headerBold);
            para_address5.setAlignment(Element.ALIGN_CENTER);

            document.add(para_address);
            document.add(para_img_logo_after_space_1);
            document.add(headertable);
            //document.add(para_img_logo_after_space_1);
            document.add(para_address5);
            document.add(para_img_logo_after_space_1);


            Font underlineFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD | Font.UNDERLINE);

            Paragraph paraLADetails = new Paragraph("Details of the Life to be insured (Member):",
                    underlineFont);
            paraLADetails.setAlignment(Element.ALIGN_LEFT);
            document.add(paraLADetails);
            PdfPCell cell;
            /*PdfPTable guidelinesTable = new PdfPTable(1);
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
            document.add(guidelinesTable);*/

            document.add(para_img_logo_after_space_1);

            //Row One
            PdfPTable userDetailsRowOneTable = new PdfPTable(4);
            userDetailsRowOneTable.setWidthPercentage(100);

            PdfPCell nameTitleCell = new PdfPCell(new Paragraph(
                    "Name of the employer:", small_normal));
            nameTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell empNameCell = new PdfPCell(new Paragraph(
                    MPH_NAME, small_normal));
            empNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell empIdTitlaCell = new PdfPCell(new Paragraph(
                    "Emp Id: ", small_normal));
            empIdTitlaCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell empIdCell = new PdfPCell(new Paragraph(
                    strCIFBDMUserId, small_normal));
            empIdCell.setVerticalAlignment(Element.ALIGN_CENTER);

            nameTitleCell.setPadding(5);
            empNameCell.setPadding(5);
            empIdTitlaCell.setPadding(5);
            empIdCell.setPadding(5);

            userDetailsRowOneTable.addCell(nameTitleCell);
            userDetailsRowOneTable.addCell(empNameCell);
            userDetailsRowOneTable.addCell(empIdTitlaCell);
            userDetailsRowOneTable.addCell(empIdCell);
            document.add(userDetailsRowOneTable);
            //Row One End

            //Row Two
            PdfPTable userDetailsRowTwoTable = new PdfPTable(4);
            userDetailsRowTwoTable.setWidthPercentage(100);

            PdfPCell designationTitleCell = new PdfPCell(new Paragraph(
                    "Designation ", small_normal));
            designationTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell designationCell = new PdfPCell(new Paragraph(
                    designation, small_normal));
            designationCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell LANameTitleCell = new PdfPCell(new Paragraph(
                    "Name of the life assured (Mr./Mrs./Ms./Dr) ", small_normal));
            LANameTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell LANameCell = new PdfPCell(new Paragraph(
                    IA_NAME, small_normal));
            LANameCell.setVerticalAlignment(Element.ALIGN_CENTER);

            designationTitleCell.setPadding(5);
            designationCell.setPadding(5);
            LANameTitleCell.setPadding(5);
            LANameCell.setPadding(5);

            userDetailsRowTwoTable.addCell(designationTitleCell);
            userDetailsRowTwoTable.addCell(designationCell);
            userDetailsRowTwoTable.addCell(LANameTitleCell);
            userDetailsRowTwoTable.addCell(LANameCell);
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

            PdfPCell NODTitleCell = new PdfPCell(new Paragraph(
                    "Nature of duties:", small_normal));
            NODTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell NODCell = new PdfPCell(new Paragraph(
                    natureOfDuty, small_normal));
            NODCell.setVerticalAlignment(Element.ALIGN_CENTER);

            DOBTitleCell.setPadding(5);
            DOBCell.setPadding(5);
            NODTitleCell.setPadding(5);
            NODCell.setPadding(5);

            userDetailsRowThreeTable.addCell(DOBTitleCell);
            userDetailsRowThreeTable.addCell(DOBCell);
            userDetailsRowThreeTable.addCell(NODTitleCell);
            userDetailsRowThreeTable.addCell(NODCell);
            document.add(userDetailsRowThreeTable);
            //Row Three End


            //Row Four
            PdfPTable userDetailsRowFourTable = new PdfPTable(4);
            userDetailsRowFourTable.setWidthPercentage(100);

            PdfPCell maritalStatusTitleCell = new PdfPCell(new Paragraph(
                    " Marital Status ", small_normal));
            maritalStatusTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell maritalStatusCell = new PdfPCell(new Paragraph(
                    marritalStatusRBString, small_normal));
            maritalStatusCell.setHorizontalAlignment(Element.ALIGN_CENTER);


            PdfPCell genderTitleCell = new PdfPCell(new Paragraph(
                    " Gender ", small_normal));
            genderTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell genderCell = new PdfPCell(new Paragraph(
                    GENDER, small_normal));
            genderCell.setHorizontalAlignment(Element.ALIGN_CENTER);


            maritalStatusTitleCell.setPadding(5);
            maritalStatusCell.setPadding(5);
            genderTitleCell.setPadding(5);
            genderCell.setPadding(5);

            userDetailsRowFourTable.addCell(maritalStatusTitleCell);
            userDetailsRowFourTable.addCell(maritalStatusCell);
            userDetailsRowFourTable.addCell(genderTitleCell);
            userDetailsRowFourTable.addCell(genderCell);
            document.add(userDetailsRowFourTable);
            //Row Four End

            //Row Five
            PdfPTable userDetailsRowFiveTable = new PdfPTable(4);
            userDetailsRowFiveTable.setWidthPercentage(100);

            PdfPCell nomineeNameTitleCell = new PdfPCell(new Paragraph(
                    " Nominee Name ", small_normal));
            nomineeNameTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell nomineeNameCell = new PdfPCell(new Paragraph(
                    nomineeName, small_normal));
            nomineeNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);


            PdfPCell nomineeDOBTitleCell = new PdfPCell(new Paragraph(
                    " Nominee DOB ", small_normal));
            nomineeDOBTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell nomineeDOBCell = new PdfPCell(new Paragraph(
                    nomineeDOB, small_normal));
            nomineeDOBCell.setHorizontalAlignment(Element.ALIGN_CENTER);


            nomineeNameTitleCell.setPadding(5);
            nomineeNameCell.setPadding(5);
            nomineeDOBTitleCell.setPadding(5);
            nomineeDOBCell.setPadding(5);

            userDetailsRowFiveTable.addCell(nomineeNameTitleCell);
            userDetailsRowFiveTable.addCell(nomineeNameCell);
            userDetailsRowFiveTable.addCell(nomineeDOBTitleCell);
            userDetailsRowFiveTable.addCell(nomineeDOBCell);
            document.add(userDetailsRowFiveTable);
            //Row Five End

            //Row Six
            PdfPTable userDetailsRowSixTable = new PdfPTable(4);
            userDetailsRowSixTable.setWidthPercentage(100);

            PdfPCell nomineeRelationTitleCell = new PdfPCell(new Paragraph(
                    " Relation With Nominee", small_normal));
            nomineeRelationTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell nomineeRelationCell = new PdfPCell(new Paragraph(
                    nomineeRelation, small_normal));
            nomineeRelationCell.setHorizontalAlignment(Element.ALIGN_CENTER);


            PdfPCell residentialStatusTitleCell = new PdfPCell(new Paragraph(
                    " Residential Status ", small_normal));
            residentialStatusTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell residentialStatusCell = new PdfPCell(new Paragraph(
                    residenceStatusRBString, small_normal));
            residentialStatusCell.setHorizontalAlignment(Element.ALIGN_CENTER);


            nomineeRelationTitleCell.setPadding(5);
            nomineeRelationCell.setPadding(5);
            residentialStatusTitleCell.setPadding(5);
            residentialStatusCell.setPadding(5);

            userDetailsRowSixTable.addCell(nomineeRelationTitleCell);
            userDetailsRowSixTable.addCell(nomineeRelationCell);
            userDetailsRowSixTable.addCell(residentialStatusTitleCell);
            userDetailsRowSixTable.addCell(residentialStatusCell);
            document.add(userDetailsRowSixTable);
            //Row Six End
            //Row Seven
            //"countryname in case NRI"
            PdfPTable userDetailsRowSevenTable = new PdfPTable(2);
            userDetailsRowSevenTable.setWidthPercentage(100);

            PdfPCell countryNameTitleCell = new PdfPCell(new Paragraph(
                    " Country Name", small_normal));
            countryNameTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell countryNameCell = new PdfPCell(new Paragraph(
                    countryResidence, small_normal));
            countryNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);


            countryNameTitleCell.setPadding(5);
            countryNameCell.setPadding(5);

            userDetailsRowSevenTable.addCell(countryNameTitleCell);
            userDetailsRowSevenTable.addCell(countryNameCell);
            document.add(userDetailsRowSevenTable);

            //Row Seven End

            //Row eight
            PdfPTable userDetailsRowEightTable = new PdfPTable(new float[]{1, 3});
            userDetailsRowEightTable.setWidthPercentage(100);

            PdfPCell addressTitleCell = new PdfPCell(new Paragraph(
                    " Address-", small_normal));
            addressTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            String addressString = "";
            addressString = addressOne;

            if (!TextUtils.isEmpty(addressTwo)) {
                addressString += ", " + addressTwo;
            }

            if (!TextUtils.isEmpty(addressThree)) {
                addressString += ", " + addressThree;
            }

            if (!TextUtils.isEmpty(taluka)) {
                addressString += "\nTaluka - " + taluka;
            }

            if (!TextUtils.isEmpty(distric)) {
                addressString += " District - " + distric;
            }

            addressString += " State - " + state;
            addressString += " PIN Code- " + PIN;
            addressString += "\nContact Number - " + contactNumber;
            PdfPCell addressCell = new PdfPCell(new Paragraph(
                    addressString, small_normal));
            addressCell.setHorizontalAlignment(Element.ALIGN_LEFT);


            addressTitleCell.setPadding(5);
            addressCell.setPadding(5);

            userDetailsRowEightTable.addCell(addressTitleCell);
            userDetailsRowEightTable.addCell(addressCell);
            document.add(userDetailsRowEightTable);

            //Row eight End

            document.add(para_img_logo_after_space_1);

            PdfPTable healthDetailsTable = new PdfPTable(2);
            healthDetailsTable.setWidthPercentage(100);
            healthDetailsTable.setHorizontalAlignment(Element.ALIGN_LEFT);


            cell = new PdfPCell(
                    new Phrase("HEALTH DETAILS OF PROPOSED INSURED", small_bold_for_name));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            healthDetailsTable.addCell(cell);
            document.add(healthDetailsTable);
            document.add(para_img_logo_after_space_1);


            /*Paragraph declarationPara = new Paragraph("HEALTH DETAILS OF PROPOSED INSURED",
                    small_bold_for_name);
            declarationPara.setAlignment(Element.ALIGN_LEFT);
            document.add(declarationPara);*/

            //Height & Weight
            PdfPTable heightWeightTable = new PdfPTable(4);
            heightWeightTable.setWidths(new float[]{5f, 5f, 5f, 5f});
            heightWeightTable.setWidthPercentage(100f);
            heightWeightTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            //Height cell
            cell = new PdfPCell(new Phrase(
                    "Height (in Cms) : ", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            heightWeightTable.addCell(cell);

            cell = new PdfPCell(new Phrase(heights, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            heightWeightTable.addCell(cell);

            //Weight Cell
            cell = new PdfPCell(new Phrase(
                    "Weight (in Kgs)  : ", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            heightWeightTable.addCell(cell);

            cell = new PdfPCell(new Phrase(weight, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            heightWeightTable.addCell(cell);

            document.add(heightWeightTable);
            //End Height & WeightId

            PdfPTable question1Table = new PdfPTable(2);
            question1Table.setWidths(new float[]{3f, 1f});
            question1Table.setWidthPercentage(100f);
            question1Table.setHorizontalAlignment(Element.ALIGN_LEFT);

            //Question 1 cell
            cell = new PdfPCell(new Phrase(
                    "1) In last five years, apart from minor ailments like cough/cold etc, have you received any treatment \n" +
                            "under doctor’s consultation and/or undergone any major surgery and/or been hospitalized and/or \n" +
                            "undergone major investigations like CT/MRI scan, Angiogram, Endoscopy, Biopsy etc ?", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);


            //Column 2
            cell = new PdfPCell(new Phrase(
                    majorSurgeryRBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);

            //Question 2 cell
            cell = new PdfPCell(new Phrase(
                    "2) Do you suffer from or have ever suffered from:\nRaised Blood sugar, Diabetes mellitus," +
                            " high blood pressure, Disease of heart, lung, kidney, Liver, thyroid, " +
                            "brain/nervous system, bone/joint/spine, Genitourinary tract, any part of the" +
                            " body or blood disorder, digestive disorder, Gynaecological disorder, Psychiatric" +
                            " disorder, HIV/AIDS and/or cancer/tumor of any part of the body ?", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);
            //Column 2
            cell = new PdfPCell(new Phrase(
                    multipleDiseaseRBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);
            //End Question 2 cell


            //Question 3 cell


            if (cigarBidiRBString.equalsIgnoreCase("yes")) {
                cell = new PdfPCell(new Phrase(
                        "3) Do you consume more than 10 cigarettes / bidi's per day? \n" +
                                "Sticks/packets Per Week-" + sticksPackets + "\n" +
                                "No of years used-" + noOfYearsUsed, small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);


                //Question 3 Sub-cell
                /*PdfPTable question3SubTable = new PdfPTable(4);
                question3SubTable.setWidths(new float[]{3f, 2f, 3f, 2f});
                question3SubTable.setWidthPercentage(100f);
                question3SubTable.setHorizontalAlignment(Element.ALIGN_LEFT);

                cell = new PdfPCell(new Phrase(
                        "Sticks/packets Per Week", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question3SubTable.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        sticksPackets, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question3SubTable.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "No of years used", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question3SubTable.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        noOfYearsUsed, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question3SubTable.addCell(cell);
                document.add(question3SubTable);*/
                // End Sub-
            } else {
                cell = new PdfPCell(new Phrase(
                        "3) Do you consume more than 10 cigarettes / bidi's per day? If yes, please " +
                                "provide duration since when and quantity per week", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);
            }
            //Column 2
            cell = new PdfPCell(new Phrase(
                    cigarBidiRBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);
            document.add(question1Table);

            //End Question 3 cell

            PdfPTable question4Table = new PdfPTable(2);
            question4Table.setWidths(new float[]{3f, 1f});
            question4Table.setWidthPercentage(100f);
            question4Table.setHorizontalAlignment(Element.ALIGN_LEFT);

            //Question 4 cell
            cell = new PdfPCell(new Phrase(
                    "4) Has your proposal for life insurance, critical illness or health insurance," +
                            " ever been denied, declined, rejected,postponed or accepted with extra premium," +
                            " due to health or lifestyle reason?", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question4Table.addCell(cell);
            //Column 2
            cell = new PdfPCell(new Phrase(
                    proposalRejectionRBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question4Table.addCell(cell);
            //End Question 4 cell


            cell = new PdfPCell(new Phrase(
                    "Details for question answered Yes", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question4Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    anyQuestYesHealthDetails, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question4Table.addCell(cell);
            document.add(question4Table);

            document.add(para_img_logo_after_space_1);
            document.add(para_img_logo_after_space_1);

            Paragraph paradeclaration = new Paragraph("DECLARATION OF THE PROPOSED LIFE ASSURED",
                    underlineFont);
            paradeclaration.setAlignment(Element.ALIGN_LEFT);
            document.add(paradeclaration);


            //Declaration First
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
            //End Declaration First

            //Declaration Second
            PdfPTable declarationSecondTable = new PdfPTable(1);
            declarationSecondTable.setWidthPercentage(100);
            declarationSecondTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell(new Phrase(DOGHdeclarationSecondString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            declarationSecondTable.addCell(cell);
            document.add(declarationSecondTable);
            document.add(para_img_logo_after_space_1);
            //End Declaration Second


            //Signature
            PdfPTable signatureTablePage1 = new PdfPTable(2);
            signatureTablePage1.setWidthPercentage(100);

            PdfPCell signaturePage2Cell = new PdfPCell(
                    new Paragraph("_________________________________________________________" +
                            "\nSignature / thumb impression of the Member" +
                            "\n\nDate______________________", small_bold));
            signaturePage2Cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            signaturePage2Cell.setPadding(5);
            signaturePage2Cell.setBorder(Rectangle.NO_BORDER);
            signatureTablePage1.addCell(signaturePage2Cell);

            signaturePage2Cell = new PdfPCell(
                    new Paragraph("________________________________________ " +
                            "\nSignature /Thumb Impression of the Witness ", small_bold));
            signaturePage2Cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            signaturePage2Cell.setPadding(5);
            signaturePage2Cell.setBorder(Rectangle.NO_BORDER);
            signatureTablePage1.addCell(signaturePage2Cell);

            document.add(signatureTablePage1);
            //End Signature

            PdfPTable namenAddressTable = new PdfPTable(1);
            namenAddressTable.setWidthPercentage(100);

            PdfPCell namenAddressCell = new PdfPCell(
                    new Paragraph("Name and address of the Witness______________________\n" +
                            "______________________________________________________\n" +
                            "______________________________________________________\n" +
                            "______________________________________________________", small_bold));
            namenAddressCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            namenAddressCell.setPadding(5);
            namenAddressCell.setBorder(Rectangle.NO_BORDER);
            namenAddressTable.addCell(namenAddressCell);
            document.add(namenAddressTable);

            document.add(para_img_logo_after_space_1);
            //Name Place Date
            PdfPTable namePlaceDateTable = new PdfPTable(3);
            namePlaceDateTable.setWidthPercentage(100);

            PdfPCell namePlaceDateCell = new PdfPCell(
                    new Paragraph("________________________________________" +
                            "\nName", small_bold));
            namePlaceDateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            namePlaceDateCell.setBorder(Rectangle.NO_BORDER);
            namePlaceDateTable.addCell(namePlaceDateCell);

            namePlaceDateCell = new PdfPCell(
                    new Paragraph("________________________________________" +
                            "\nPlace", small_bold));
            namePlaceDateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            namePlaceDateCell.setBorder(Rectangle.NO_BORDER);
            namePlaceDateTable.addCell(namePlaceDateCell);

            namePlaceDateCell = new PdfPCell(
                    new Paragraph("________________________________________" +
                            "\nDate", small_bold));
            namePlaceDateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            namePlaceDateCell.setBorder(Rectangle.NO_BORDER);
            namePlaceDateTable.addCell(namePlaceDateCell);

            document.add(namePlaceDateTable);
            //End Name Place Date

            Paragraph paradeIlleterateclaration = new Paragraph("Declaration to be given if Proposal is" +
                    " signed in vernacular or if the life to be assured is illiterate.",
                    small_bold);
            paradeIlleterateclaration.setAlignment(Element.ALIGN_LEFT);
            document.add(paradeIlleterateclaration);


            //Declaration First
            PdfPTable illeteratedeclarationTable = new PdfPTable(1);
            illeteratedeclarationTable.setWidthPercentage(100);
            illeteratedeclarationTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st note
            cell = new PdfPCell(
                    new Phrase("I have explained the contents of this form to the life to be assured " +
                            "and ensured that the contents have been fully understood by him/her. I have accurately " +
                            "recorded his/her responses to the information sought in the proposal form and I" +
                            " have read out the responses to the life to be assured and he / she has confirmed that they" +
                            " are correct.", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            illeteratedeclarationTable.addCell(cell);
            document.add(illeteratedeclarationTable);
            document.add(para_img_logo_after_space_1);
            //End Declaration First

            //Signature Two
            PdfPTable signatureTablePage2 = new PdfPTable(2);
            signatureTablePage2.setWidthPercentage(100);

            signaturePage2Cell = new PdfPCell(
                    new Paragraph("_________________________________________________________" +
                            "\nSignature of the person making the declaration" +
                            "\n\nName of the declarant_________________________", small_bold));
            signaturePage2Cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            signaturePage2Cell.setBorder(Rectangle.NO_BORDER);
            signatureTablePage2.addCell(signaturePage2Cell);

            signaturePage2Cell = new PdfPCell(
                    new Paragraph("________________________________________ " +
                            "\nSignature/Thumb Impression of life to be assured ", small_bold));
            signaturePage2Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            signaturePage2Cell.setBorder(Rectangle.NO_BORDER);
            signatureTablePage2.addCell(signaturePage2Cell);

            document.add(signatureTablePage2);
            //End Signature Two
            document.add(para_img_logo_after_space_1);

            //Address Place Date
            PdfPTable addressPlaceDateTable = new PdfPTable(3);
            addressPlaceDateTable.setWidthPercentage(100);

            PdfPCell addressPlaceDateCell = new PdfPCell(
                    new Paragraph("Address______________________________\n" +
                            "____________________________________\n" +
                            "____________________________________\n" +
                            "____________________________________", small_bold));
            addressPlaceDateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            addressPlaceDateCell.setBorder(Rectangle.NO_BORDER);
            addressPlaceDateTable.addCell(addressPlaceDateCell);

            addressPlaceDateCell = new PdfPCell(
                    new Paragraph("Place____________________", small_bold));
            addressPlaceDateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            addressPlaceDateCell.setBorder(Rectangle.NO_BORDER);
            addressPlaceDateTable.addCell(addressPlaceDateCell);

            addressPlaceDateCell = new PdfPCell(
                    new Paragraph("Date____________________ ", small_bold));
            addressPlaceDateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            addressPlaceDateCell.setBorder(Rectangle.NO_BORDER);
            addressPlaceDateTable.addCell(addressPlaceDateCell);

            document.add(addressPlaceDateTable);
            //End Address Place Date

            PdfPTable declarationPageMasterTable = new PdfPTable(1);
            declarationPageMasterTable.setWidthPercentage(100);
            declarationPageMasterTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPTable declarationPage2Table = new PdfPTable(1);
            declarationPage2Table.setWidthPercentage(100);
            declarationPage2Table.setHorizontalAlignment(Element.ALIGN_LEFT);

            document.add(para_img_logo_after_space_1);

            // 1st note
            cell = new PdfPCell(
                    new Phrase(
                            "Extract of Section 45 of the Insurance Act 1938, as amended from time to time",
                            headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            declarationPage2Table.addCell(cell);


            cell = new PdfPCell(
                    new Phrase(
                            "No policy of life insurance shall be called into question on any ground whatsoever after the expiry of three years from the " +
                                    "date of policy. A policy of life insurance may be called into question at anytime within three years from the date of policy, on " +
                                    "the ground of fraud or on the ground that any statement of or suppression of a fact material to the expectancy of the life of" +
                                    "the insured was incorrectly made in the proposal or other document on the basis of which the policy was issued or revived or " +
                                    "rider issued. The insurer shall have to communicate in writing to the insured or legal representatives or nominees or assignees " +
                                    "of the insured, the grounds and materials on which such decision is based. " +
                                    "No insurer shall repudiate a life insurance policy on the ground of fraud if the insured can prove that the mis-statement or " +
                                    "suppression of material fact was true to the best of his knowledge and belief or that there was no deliberate intention to " +
                                    "suppress the fact or that such mis-statement or suppression are within the knowledge of the insurer. In case of fraud, the " +
                                    "onus of disproving lies upon the beneficiaries, in case the policyholder is not alive. In case of repudiation of the policy on the " +
                                    "ground of misstatement or suppression of a material fact and not on the grounds of fraud, the premiums collected on the " +
                                    "policy till the date of repudiation shall be paid. " +
                                    "Nothing in this section shall prevent the insurer from calling for proof of age at any time if he is entitled to do so, and no " +
                                    "policy shall be deemed to be called in question merely because the terms of the policy are adjusted on subsequent proof that " +
                                    "the age of the life insured was incorrectly stated in the proposal. " +
                                    "For complete details of the section and the definition of 'date of policy', please refer Section 45 of the Insurance Act 1938, as " +
                                    "amended from time to time. Trade logo displayed above belongs to State Bank of India and is used by SBI Life under license",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            declarationPage2Table.addCell(cell);

            declarationPageMasterTable.addCell(declarationPage2Table);
            document.add(declarationPageMasterTable);
            //document.add(declarationPage2Table);

            document.add(para_img_logo_after_space_1);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void downLoadData() {
        doghDetailsAsyncTask = new DOGHDetailsAsyncTask();
        doghDetailsAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private String generateXmlString() {

        String xmlTags = "<?xml version='1.0' encoding='utf-8' ?><COVID>" +
                "<IACode>" + strCIFBDMUserId + "</IACode>";
        xmlTags += "<HD_COVID_NAME_COUNTRY_VISITED></HD_COVID_NAME_COUNTRY_VISITED>" +
                "<HD_COVID_VISIT_DURA_FRm_Dt></HD_COVID_VISIT_DURA_FRm_Dt>" +
                "<HD_COVID_VISIT_DURA_TO_Dt></HD_COVID_VISIT_DURA_TO_Dt>" +
                "<HD_COVID_Dt_OF_REt_TO_INDIA></HD_COVID_Dt_OF_REt_TO_INDIA>" +
                "<HD_COVID_SCREEN_AT_AIRPORT></HD_COVID_SCREEN_AT_AIRPORT>" +
                "<HD_COVID_TEST_FOR_COVID19></HD_COVID_TEST_FOR_COVID19>" +
                "<HD_COVID_KEPT_HOME_QUARTINE></HD_COVID_KEPT_HOME_QUARTINE>" +
                "<HD_COVID_KEPT_UNDER_OBSERV></HD_COVID_KEPT_UNDER_OBSERV>" +
                "<HD_COVID_KEPT_HOME_ISOLAT></HD_COVID_KEPT_HOME_ISOLAT>" +
                "<HD_COVID_PLN_TRA_FORE_COUNRTY></HD_COVID_PLN_TRA_FORE_COUNRTY>" +
                "<HD_COVID_NM_COUN_VIS_NXT_6MON></HD_COVID_NM_COUN_VIS_NXT_6MON>" +
                "<HD_COVID_VIS_DUR_FR_DT_NX_6MN></HD_COVID_VIS_DUR_FR_DT_NX_6MN>" +
                "<HD_COVID_VIS_DUR_TO_DT_NX_6MN></HD_COVID_VIS_DUR_TO_DT_NX_6MN>" +
                "<HD_COVID_DT_RE_TO_IND_NXT_6MN></HD_COVID_DT_RE_TO_IND_NXT_6MN>" +
                "<HD_COVID_FMLY_SUFF_ANY_SYMP></HD_COVID_FMLY_SUFF_ANY_SYMP>" +
                "<HD_COVID_FMLY_UND_ANY_TEst></HD_COVID_FMLY_UND_ANY_TEst>" +
                "<HD_COVID_FMLY_CASE_CORONA></HD_COVID_FMLY_CASE_CORONA>" +
                "<HD_COVID_VISIT_ANYFORE_CNTRY></HD_COVID_VISIT_ANYFORE_CNTRY>";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdp = new SimpleDateFormat("MM-dd-yyyy");
        String str_created_date = sdp.format(new Date(cal.getTimeInMillis()));
        xmlTags += "<createddate>" + str_created_date + "</createddate>";

        xmlTags += "<illnessDetailsYesNo></illnessDetailsYesNo>" +
                "<illnessDetailsYesAnswer></illnessDetailsYesAnswer>" +
                "<anyQuestYesAnsweredDetails></anyQuestYesAnsweredDetails>";

        if (TYPE.equalsIgnoreCase("CHQ")) {
            xmlTags += "<COVID_QUE_ONE></COVID_QUE_ONE>" +
                    "<COVID_QUE_TWO></COVID_QUE_TWO>" +
                    "<COVID_QUE_THREE></COVID_QUE_THREE>" +
                    "<COVID_QUE_FOUR></COVID_QUE_FOUR>" +
                    "<COVID_QUE_FIVE></COVID_QUE_FIVE>" +
                    "<COVID_QUE_SIX></COVID_QUE_SIX>" +
                    "<COVID_QUE_ANSWER_YES></COVID_QUE_ANSWER_YES>";

            xmlTags += "<DOGH_QUE_ONE>" + majorSurgeryRBString + "</DOGH_QUE_ONE>" +
                    "<DOGH_QUE_TWO>" + multipleDiseaseRBString + "</DOGH_QUE_TWO>" +
                    "<DOGH_QUE_THREE>" + cigarBidiRBString + "</DOGH_QUE_THREE>" +
                    "<DOGH_QUE_FOUR>" + proposalRejectionRBString + "</DOGH_QUE_FOUR>" +
                    "<DOGH_QUE_ANSWER_YES>" + anyQuestYesHealthDetails + "</DOGH_QUE_ANSWER_YES>" +

                    "<DOGH_QUE_THREE_STICKS_PER_WEEK>" + sticksPackets + "</DOGH_QUE_THREE_STICKS_PER_WEEK>" +
                    "<DOGH_QUE_THREE_NO_OF_YEAR>" + noOfYearsUsed + "</DOGH_QUE_THREE_NO_OF_YEAR>" +
                    "<DOGH_HEIGHT>" + heights + "</DOGH_HEIGHT>" +
                    "<DOGH_WEIGHT>" + weight + "</DOGH_WEIGHT>";
        } else if (TYPE.equalsIgnoreCase("Covid Q")) {

            xmlTags += "<COVID_QUE_ONE>" + covidQueNoOneRBString + "</COVID_QUE_ONE>" +
                    "<COVID_QUE_TWO>" + covidQueNoTwoRBString + "</COVID_QUE_TWO>" +
                    "<COVID_QUE_THREE>" + covidQueNoThreeRBString + "</COVID_QUE_THREE>" +
                    "<COVID_QUE_FOUR>" + covidQueNoFourRBString + "</COVID_QUE_FOUR>" +
                    "<COVID_QUE_FIVE>" + covidQueNoFiveRBString + "</COVID_QUE_FIVE>" +
                    "<COVID_QUE_SIX>" + covidQueNoSixRBString + "</COVID_QUE_SIX>" +
                    "<COVID_QUE_ANSWER_YES>" + anyCovidQuestYesAnsweredDetails + "</COVID_QUE_ANSWER_YES>";

            xmlTags += "<DOGH_QUE_ONE></DOGH_QUE_ONE>" +
                    "<DOGH_QUE_TWO></DOGH_QUE_TWO>" +
                    "<DOGH_QUE_THREE></DOGH_QUE_THREE>" +
                    "<DOGH_QUE_FOUR></DOGH_QUE_FOUR>" +
                    "<DOGH_QUE_ANSWER_YES></DOGH_QUE_ANSWER_YES>" +
                    "<DOGH_QUE_THREE_STICKS_PER_WEEK></DOGH_QUE_THREE_STICKS_PER_WEEK>" +
                    "<DOGH_QUE_THREE_NO_OF_YEAR></DOGH_QUE_THREE_NO_OF_YEAR>" +
                    "<DOGH_HEIGHT></DOGH_HEIGHT>" +
                    "<DOGH_WEIGHT></DOGH_WEIGHT>";
        }


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
                    if (TYPE.equalsIgnoreCase("CHQ")) {
                        createDOGHPDF();
                    } else if (TYPE.equalsIgnoreCase("Covid Q")) {
                        createCOVIDPDF();
                    }

                } else {
                    if (TYPE.equalsIgnoreCase("CHQ")) {
                        newFilePathDOGH = mStorageUtils.createFileToAppSpecificDir(context,
                                strCIFBDMUserId + "_DOGH" + ".pdf");
                        String DOGHFilePathName = newFilePathDOGH.getAbsolutePath();
                        manipulatePdf(DOGHFilePath.getAbsolutePath(), DOGHFilePathName, passCode);
                        if (DOGHFilePath.exists()) {
                            DOGHFilePath.delete();
                        }
                        DOGHFilePath = newFilePathDOGH;
                    } else if (TYPE.equalsIgnoreCase("Covid Q")) {
                        newFilePathCovid = mStorageUtils.createFileToAppSpecificDir(context,
                                strCIFBDMUserId + "_COVID19" + ".pdf");

                        String covidFilePathName = newFilePathCovid.getAbsolutePath();
                        manipulatePdf(COVIDFilePath.getAbsolutePath(), covidFilePathName, passCode);
                        if (COVIDFilePath.exists()) {
                            COVIDFilePath.delete();
                        }
                        COVIDFilePath = newFilePathCovid;
                    }

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
                        createPDFAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        saveCovidDataAsyncTask = new SaveCovidDataAsyncTask();
                        saveCovidDataAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
        private final String fileTag;
        int flag = 0;
        private volatile boolean running = true;

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
                    llDOGHCovidDetails.setVisibility(View.GONE);
                    llCovidQuestions.setVisibility(View.GONE);
                    llDOGHQuestion.setVisibility(View.GONE);
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

                        TYPE = prsObj.parseXmlTag(inputpolicylist, "TYPE");
                        TYPE = TYPE == null ? "" : TYPE;

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

                    if (TYPE.equalsIgnoreCase("CHQ")) {
                        llDOGHQuestion.setVisibility(View.VISIBLE);
                        llCovidQuestions.setVisibility(View.GONE);
                        DOGHdeclarationString = "I would like to become a member of the “____________” offered by SBI Life Insurance" +
                                " Company Ltd. (SBI Life) and I have fully understood and agree to all the terms and \n" +
                                "conditions of the scheme and agree that the Master policyholder shall act as the Group " +
                                "Administrator.\n" +
                                "I undertake to furnish any other details that may be required with regard to my proposed " +
                                "insurance cover. I hereby permit the master policyholder to convey the above \n" +
                                "particulars regarding my admission into the SBI Life- ____________. I also permit SBI Life" +
                                " to approach me directly for any clarification/other purposes. I hereby understand \n" +
                                "and agree that no insurance cover will commence until the risk is accepted and requisite" +
                                " premium has been remitted to SBI Life by the Master policy Holder and SBI Life \n" +
                                "conveys its written acceptance of this application for insurance cover. I further" +
                                " understand and agree that insurance cover provided to me shall be governed by the Master " +
                                "Policy \n" +
                                "Contract issued in favour of the Group Master Policyholder" +
                                "\n\nNotwithstanding the provision of any law, usage, custom or convention for the time being in " +
                                "force prohibiting any doctor, hospital and/or employer from divulging any \n" +
                                "knowledge or information about me concerning my health, employment on the grounds of secrecy, I, " +
                                "my heirs, executors, administrators or any other person or persons having \n" +
                                "interest of any kind whatsoever in the insurance cover provided to me, hereby agree that such " +
                                "authority, having such knowledge or information, shall at any time be at liberty to \n" +
                                "divulge any such knowledge or information to the Company.\n";

                        checkBoxDisclaimer.setText(DOGHdeclarationString);

                        checkBoxCovidDisclaimer.setText("I declare that the answers given above are true and to the best of my knowledge and that I " +
                                "have not withheld any material information that may influence the assessment or " +
                                "acceptance of this application. I agree that this form will constitute part of my application " +
                                "for life Insurance and that failure to disclose any material fact known to me may invalidate " +
                                "the contract.");

                        DOGHdeclarationSecondString = "";
                        DOGHdeclarationSecondString += "I hereby understand and agree that the total benefits payable under " +
                                "this product shall not exceed the maximum applicable for this product irrespective of the number of \n" +
                                "Memberships forms signed. I hereby declare and agree that the foregoing declaration has been " +
                                "given after fully understanding the same and is true and complete to the best of \n" +
                                "my knowledge and that I have not withheld any information that may influence my admission " +
                                "into the Group Insurance Scheme of SBI Life Insurance Co. Ltd. I hereby agree \n" +
                                "that this form including the declaration shall form the basis of my admission into the " +
                                "Group Insurance Scheme and if any untrue statement be contained therein, I, my heirs, \n" +
                                "executors, administrators and assignees shall not be entitled to receive any benefits " +
                                "under the Group Insurance Scheme. I also agree that the Company shall not be liable for any \n" +
                                "claim on account of illness, injury, or death, the cause of which was known prior to " +
                                "approval of my request for assurance or withheld or concealed in the above statements.";


                        checkBoxDisclaimerSecond.setText(DOGHdeclarationSecondString);

                        checkBoxDisclaimerIlleterate.setText("I have explained the contents of this form to the life to be assured and ensured that the contents have been fully understood by him/her. I have accurately\n" +
                                "recorded his/her responses to the information sought in the proposal form and I have read out the responses to the life to be assured and he / she has\n" +
                                "confirmed that they are correct.");

                    } else if (TYPE.equalsIgnoreCase("Covid Q")) {
                        llDOGHQuestion.setVisibility(View.GONE);
                        llCovidQuestions.setVisibility(View.VISIBLE);
                    }
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
                    if (TYPE.equalsIgnoreCase("CHQ")) {
                        uploadPDFService = new UploadPDFService("DOGH");
                        uploadPDFService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else if (TYPE.equalsIgnoreCase("Covid Q")) {
                        uploadPDFService = new UploadPDFService("COVID");
                        uploadPDFService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }

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