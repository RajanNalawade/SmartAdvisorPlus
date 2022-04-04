package sbilife.com.pointofsale_bancaagency.home.lmcorner;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class CovidSelfDeclarationActivity extends AppCompatActivity implements ServiceHits.DownLoadData, View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME = "getSelfDeclarUserdet_smrt";
    private final String URl = ServiceURL.SERVICE_URL;
    private ServiceHits service;
    private ProgressDialog mProgressDialog;
    private CommonMethods commonMethods;
    private Context context;

    private EditText etEmergencyContactNumber, etEmergencyContactName;
    private CheckBox checkBoxUserDeclaration;
    private RadioGroup rgContainmentZone, rgCovidPatientContact, rgInternationalTravelHistory, rgChronicMedicalCondition,
            radioGroupFever, radioGroupCough, radioGroupShortBreath, radioGroupSoreThroat, rgFamilySympTomsStatus;
    private String containmentZone = "No", CovidPatientContact = "No", InternationalTravelHistory = "No",
            ChronicMedicalCondition = "No", fever = "No", cough = "No", shortBreath = "No", soreThroat = "No",
            familySymptomsStatus = "No";
    private String strCIFBDMUserId = "", strCIFBDMEmailId = "", strCIFBDMMObileNo = "", emergencyContact, emergencyContactName;

    private ArrayList<CovidSelfDeclarationValuesModel> globalDataList;
    private CovidSelfDeclarationAsynctask covidSelfDeclarationAsynctask;
    private SaveCovidSelfDeclarationAsyncTask saveCovidSelfDeclarationAsyncTask;
    private LinearLayout llCovidSelfDeclaration;
    private String greenRedTag = "Yes";
    private Button buttonDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_covid_self_declaration);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        commonMethods = new CommonMethods();
        context = this;
        commonMethods.setApplicationToolbarMenu(this, "Covid Self Declaration");

        etEmergencyContactNumber = findViewById(R.id.etEmergencyContactNumber);
        etEmergencyContactName = findViewById(R.id.etEmergencyContactName);

        checkBoxUserDeclaration = findViewById(R.id.checkBoxUserDeclaration);
        String declaration = "I hereby declare that the information shared above is true, complete and correct to the best of my knowledge and belief and I am attending the ch club convention & star league convention at my own risk, I will further also inform the key management of the retail agency channel if I  show any of the above symptoms during the period of my stay at goa, during the convention,\n" +
                "\n" +
                "I hereby also confirm that I have downloaded the arogya setu app on my mobile phone and my location setting as well as blue tooth are always on.\n" +
                "\n" +
                "I hereby undertake to fill this online form again if any information in the form regarding my status changes in the interim.  I understand that in event of my information being found false, or incorrect at any stage , a disciplinary action or legal action can be taken against me.\n" +
                "\n" +
                "I understand that the onus of looking after myself lies with me and I promise to wear a mask in public , to maintain social distancing as per covid guidelines at all times.";
        checkBoxUserDeclaration.setText(declaration);
        rgContainmentZone = findViewById(R.id.rgContainmentZone);
        ((RadioButton) rgContainmentZone.getChildAt(1)).setChecked(true);

        rgCovidPatientContact = findViewById(R.id.rgCovidPatientContact);
        ((RadioButton) rgCovidPatientContact.getChildAt(1)).setChecked(true);

        rgInternationalTravelHistory = findViewById(R.id.rgInternationalTravelHistory);
        ((RadioButton) rgInternationalTravelHistory.getChildAt(1)).setChecked(true);

        rgChronicMedicalCondition = findViewById(R.id.rgChronicMedicalCondition);
        ((RadioButton) rgChronicMedicalCondition.getChildAt(1)).setChecked(true);

        radioGroupFever = findViewById(R.id.radioGroupFever);
        ((RadioButton) radioGroupFever.getChildAt(1)).setChecked(true);

        radioGroupCough = findViewById(R.id.radioGroupCough);
        ((RadioButton) radioGroupCough.getChildAt(1)).setChecked(true);

        radioGroupShortBreath = findViewById(R.id.radioGroupShortBreath);
        ((RadioButton) radioGroupShortBreath.getChildAt(1)).setChecked(true);

        radioGroupSoreThroat = findViewById(R.id.radioGroupSoreThroat);
        ((RadioButton) radioGroupSoreThroat.getChildAt(1)).setChecked(true);

        rgFamilySympTomsStatus = findViewById(R.id.rgFamilySympTomsStatus);
        ((RadioButton) rgFamilySympTomsStatus.getChildAt(1)).setChecked(true);

        llCovidSelfDeclaration = findViewById(R.id.llCovidSelfDeclaration);
        llCovidSelfDeclaration.setVisibility(View.GONE);
        rgContainmentZone.setOnCheckedChangeListener(this);
        rgCovidPatientContact.setOnCheckedChangeListener(this);
        rgInternationalTravelHistory.setOnCheckedChangeListener(this);
        rgChronicMedicalCondition.setOnCheckedChangeListener(this);
        radioGroupFever.setOnCheckedChangeListener(this);
        radioGroupCough.setOnCheckedChangeListener(this);
        radioGroupShortBreath.setOnCheckedChangeListener(this);
        radioGroupSoreThroat.setOnCheckedChangeListener(this);
        rgFamilySympTomsStatus.setOnCheckedChangeListener(this);

        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");
        if (fromHome != null && fromHome.equalsIgnoreCase("N")) {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");
        } else {
            getUserDetails();
        }

        buttonDashboard = findViewById(R.id.buttonDashboard);
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        ArrayList<CovidSelfDeclarationDashboardActivity.DBCovidSelfDeclarationValuesModel> dataList = databaseHelper.getCovidSelfDeclarationByIACode(strCIFBDMUserId);

        if (dataList.size() > 0) {
            buttonDashboard.setVisibility(View.VISIBLE);
            buttonDashboard.setOnClickListener(this);
        } else {
            buttonDashboard.setVisibility(View.GONE);
        }

        Button buttonSubmit = findViewById(R.id.buttonSubmit);
        Button buttonView = findViewById(R.id.buttonView);
        buttonSubmit.setOnClickListener(this);
        buttonView.setOnClickListener(this);
        globalDataList = new ArrayList<>();
        mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        mProgressDialog.setButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        killTasks();
                    }
                });

        mProgressDialog.setMax(100);
        if (commonMethods.isNetworkConnected(context)) {
            service_hits();
        } else {
            commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
        }
    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }

    private void service_hits() {
        service = new ServiceHits(context, METHOD_NAME, strCIFBDMUserId,
                strCIFBDMUserId, strCIFBDMEmailId,
                strCIFBDMMObileNo, commonMethods.getStrAuth(), this);
        service.execute();
    }

    @Override
    public void downLoadData() {
        covidSelfDeclarationAsynctask = new CovidSelfDeclarationAsynctask();
        covidSelfDeclarationAsynctask.execute();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonSubmit:
                commonMethods.hideKeyboard(etEmergencyContactName, context);
                emergencyContact = etEmergencyContactNumber.getText().toString();
                emergencyContactName = etEmergencyContactName.getText().toString();

                if (TextUtils.isEmpty(emergencyContact)) {
                    commonMethods.showMessageDialog(context, "Please enter Emergency Contact Number.");
                    commonMethods.setFocusable(etEmergencyContactNumber);
                    etEmergencyContactNumber.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(emergencyContactName)) {
                    commonMethods.showMessageDialog(context, "Please enter Emergency Contact Name.");
                    commonMethods.setFocusable(etEmergencyContactName);
                    etEmergencyContactName.requestFocus();
                    return;
                }
                if (!checkBoxUserDeclaration.isChecked()) {
                    commonMethods.showMessageDialog(context, "Please accept the declaration");
                    commonMethods.setFocusable(checkBoxUserDeclaration);
                    checkBoxUserDeclaration.requestFocus();
                    return;
                }
                saveCovidSelfDeclarationAsyncTask = new SaveCovidSelfDeclarationAsyncTask();
                saveCovidSelfDeclarationAsyncTask.execute();
                break;
            case R.id.buttonView:
                break;
            case R.id.buttonDashboard:
                Intent intent = new Intent(context, CovidSelfDeclarationDashboardActivity.class);
                startActivity(intent);
                break;
            case R.id.tvContactNumber:
            case R.id.imgcontact_cust_r:
                String mobileNumber = globalDataList.get(0).getCONTACT_NUMBER();
                if (!TextUtils.isEmpty(mobileNumber)) {
                    commonMethods.callMobileNumber(mobileNumber, context);
                }
                break;
        }
    }

    private String generateCovidSelfDeclarationXML() {


        String resultXML = "<?xml version='1.0' encoding='utf-8' ?><CovidSelfDeclaration>" +
                "<NAME>" + globalDataList.get(0).getNAME() + "</NAME>" +
                "<DESIGNATION>" + globalDataList.get(0).getDESIGNATION() + "</DESIGNATION>" +
                "<EMPLOYEE_CODE>" + globalDataList.get(0).getEMPLOYEE_CODE() + "</EMPLOYEE_CODE>" +
                "<GENDER>" + globalDataList.get(0).getGENDER() + "</GENDER>" +
                "<BRANCH>" + globalDataList.get(0).getBRANCH() + "</BRANCH>" +
                "<REGION>" + globalDataList.get(0).getREGION() + "</REGION>" +
                "<ZONE>" + globalDataList.get(0).getZONE() + "</ZONE>" +
                "<CONTACT_NUMBER>" + globalDataList.get(0).getCONTACT_NUMBER() + "</CONTACT_NUMBER>" +
                "<EMERGENCY_CONTACT_NUMBER>" + emergencyContact + "</EMERGENCY_CONTACT_NUMBER>" +
                "<EMERGENCY_CONTACT_NAME>" + emergencyContactName + "</EMERGENCY_CONTACT_NAME>" +

                "<CONTAINMENT_ZONE_status>" + containmentZone + "</CONTAINMENT_ZONE_status>" +
                "<Covid_Patient_Contact>" + CovidPatientContact + "</Covid_Patient_Contact>" +
                "<International_Travel_History>" + InternationalTravelHistory + "</International_Travel_History>" +
                "<CHRONIC_MEDICAL_CONDITION>" + ChronicMedicalCondition + "</CHRONIC_MEDICAL_CONDITION>" +

                "<FEVER>" + fever + "</FEVER>" +
                "<COUGH>" + cough + "</COUGH>" +
                "<SHORTNESS_OF_BREATH>" + shortBreath + "</SHORTNESS_OF_BREATH>" +
                "<SORE_THROAT>" + soreThroat + "</SORE_THROAT>" +
                "<Family_Symptoms_Status>" + familySymptomsStatus + "</Family_Symptoms_Status>";

       /* String resultXML = "<?xml version='1.0' encoding='utf-8' ?><CovidSelfDeclaration>" +
                "<NAME></NAME>" +
                "<DESIGNATION></DESIGNATION>" +
                "<EMPLOYEE_CODE></EMPLOYEE_CODE>" +
                "<GENDER></GENDER>" +
                "<BRANCH></BRANCH>" +
                "<REGION></REGION>" +
                "<ZONE></ZONE>" +
                "<CONTACT_NUMBER></CONTACT_NUMBER>" +
                "<EMERGENCY_CONTACT_NUMBER></EMERGENCY_CONTACT_NUMBER>" +
                "<EMERGENCY_CONTACT_NAME></EMERGENCY_CONTACT_NAME>" +

                "<CONTAINMENT_ZONE_status></CONTAINMENT_ZONE_status>" +
                "<Covid_Patient_Contact></Covid_Patient_Contact>" +
                "<International_Travel_History></International_Travel_History>" +
                "<CHRONIC_MEDICAL_CONDITION></CHRONIC_MEDICAL_CONDITION>" +

                "<FEVER></FEVER>" +
                "<COUGH></COUGH>" +
                "<SHORTNESS_OF_BREATH></SHORTNESS_OF_BREATH>" +
                "<SORE_THROAT></SORE_THROAT>" +
                "<Family_Symptoms_Status></Family_Symptoms_Status>";*/

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdp = new SimpleDateFormat("MM-dd-yyyy");
        String createdDate = sdp.format(new Date(cal.getTimeInMillis()));
        resultXML += "<CREATEDDATE>" + createdDate + "</CREATEDDATE>";
        return resultXML;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

        System.out.println("checkedId = " + checkedId);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);
        String text = radioButton.getText() + "";
        System.out.println("text = " + text);

        switch (radioGroup.getId()) {
            case R.id.rgContainmentZone:
                containmentZone = text;
                break;
            case R.id.rgCovidPatientContact:
                CovidPatientContact = text;
                break;
            case R.id.rgInternationalTravelHistory:
                InternationalTravelHistory = text;
                break;
            case R.id.rgChronicMedicalCondition:
                ChronicMedicalCondition = text;
                break;
            case R.id.radioGroupFever:
                fever = text;
                break;
            case R.id.radioGroupCough:
                cough = text;
                break;
            case R.id.radioGroupShortBreath:
                shortBreath = text;
                break;
            case R.id.radioGroupSoreThroat:
                soreThroat = text;
                break;
            case R.id.rgFamilySympTomsStatus:
                familySymptomsStatus = text;
                break;

        }
    }

    class CovidSelfDeclarationAsynctask extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String error = "";
        private TextView tvName, tvDesignation, tvEmployeeCode, tvGender, tvBranch, tvRegion, tvZone, tvContactNumber;
        private ImageView imgcontact_cust_r;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
            tvName = findViewById(R.id.tvName);
            tvDesignation = findViewById(R.id.tvDesignation);
            tvEmployeeCode = findViewById(R.id.tvEmployeeCode);
            tvGender = findViewById(R.id.tvGender);

            tvBranch = findViewById(R.id.tvBranch);
            tvRegion = findViewById(R.id.tvRegion);
            tvZone = findViewById(R.id.tvZone);
            tvContactNumber = findViewById(R.id.tvContactNumber);
            imgcontact_cust_r = findViewById(R.id.imgcontact_cust_r);
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                //getLMFunddetail_smrt(string strCode, string strEmailId, string strMobileNo, string strAuthKey)

                running = true;
                SoapObject request;

                request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("strCode", strCIFBDMUserId);
                commonMethods.appendSecurityParams(context, request, strCIFBDMEmailId, strCIFBDMMObileNo);

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                androidHttpTranport.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();

                if (!response.toString().contentEquals("<CIFPolicyList />")) {
                    System.out.println("response:" + response.toString());
                    SoapPrimitive sa;
                    sa = (SoapPrimitive) envelope.getResponse();

                    String inputpolicylist = sa.toString();

                    ParseXML prsObj = new ParseXML();

                    inputpolicylist = prsObj.parseXmlTag(
                            inputpolicylist, "NewDataSet");
                    inputpolicylist = prsObj.parseXmlTag(
                            inputpolicylist, "ScreenData");
                    error = inputpolicylist;

                    if (error == null) {
                        // for agent policy list

                        inputpolicylist = sa.toString();
                        inputpolicylist = prsObj.parseXmlTag(
                                inputpolicylist, "NewDataSet");

                        List<String> Node = prsObj.parseParentNode(
                                inputpolicylist, "Table");

                        List<CovidSelfDeclarationValuesModel> nodeData = parseNodeFutureSecureFund(Node);
                        globalDataList.clear();
                        globalDataList.addAll(nodeData);
                    }

                } else {
                    running = false;
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
            llCovidSelfDeclaration.setVisibility(View.GONE);
            if (running) {
                if (error == null) {

                    llCovidSelfDeclaration.setVisibility(View.VISIBLE);

                    tvName.setText(globalDataList.get(0).getNAME());
                    tvDesignation.setText(globalDataList.get(0).getDESIGNATION());
                    tvEmployeeCode.setText(globalDataList.get(0).getEMPLOYEE_CODE());
                    tvGender.setText(globalDataList.get(0).getGENDER());
                    tvBranch.setText(globalDataList.get(0).getBRANCH());
                    tvRegion.setText(globalDataList.get(0).getREGION());
                    tvZone.setText(globalDataList.get(0).getZONE());
                    tvContactNumber.setText(globalDataList.get(0).getCONTACT_NUMBER());

                    imgcontact_cust_r.setOnClickListener((View.OnClickListener) context);
                    tvContactNumber.setOnClickListener((View.OnClickListener) context);
                } else {
                    commonMethods.showMessageDialog(context, "No Record Found");
                }
            } else {
                commonMethods.showMessageDialog(context, "No Record Found");
            }
        }
    }

    ArrayList<CovidSelfDeclarationValuesModel> parseNodeFutureSecureFund(List<String> lsNode) {
        ArrayList<CovidSelfDeclarationValuesModel> lsData = new ArrayList<>();
        ParseXML parseXML = new ParseXML();
        String NAME, DESIGNATION, EMPLOYEE_CODE, GENDER, BRANCH, REGION, ZONE, CONTACT_NUMBER;
        for (String Node : lsNode) {

            NAME = parseXML.parseXmlTag(Node, "NAME");
            NAME = NAME == null ? "" : NAME;

            DESIGNATION = parseXML.parseXmlTag(Node, "DESIGNATION");
            DESIGNATION = DESIGNATION == null ? "" : DESIGNATION;

            EMPLOYEE_CODE = parseXML.parseXmlTag(Node, "EMPLOYEE_CODE");
            EMPLOYEE_CODE = EMPLOYEE_CODE == null ? "" : EMPLOYEE_CODE;

            GENDER = parseXML.parseXmlTag(Node, "GENDER");
            GENDER = GENDER == null ? "" : GENDER;

            BRANCH = parseXML.parseXmlTag(Node, "BRANCH");
            BRANCH = BRANCH == null ? "" : BRANCH;

            REGION = parseXML.parseXmlTag(Node, "REGION");
            REGION = REGION == null ? "" : REGION;

            ZONE = parseXML.parseXmlTag(Node, "ZONE");
            ZONE = ZONE == null ? "" : ZONE;

            CONTACT_NUMBER = parseXML.parseXmlTag(Node, "CONTACT_NUMBER");
            CONTACT_NUMBER = CONTACT_NUMBER == null ? "" : CONTACT_NUMBER;

            CovidSelfDeclarationValuesModel nodeVal = new CovidSelfDeclarationValuesModel(NAME, DESIGNATION, EMPLOYEE_CODE, GENDER, BRANCH, REGION, ZONE, CONTACT_NUMBER);
            lsData.add(nodeVal);
        }
        return lsData;
    }


    class CovidSelfDeclarationValuesModel {
        private String NAME, DESIGNATION, EMPLOYEE_CODE, GENDER, BRANCH, REGION, ZONE, CONTACT_NUMBER;

        public CovidSelfDeclarationValuesModel(String NAME, String DESIGNATION, String EMPLOYEE_CODE, String GENDER, String BRANCH, String REGION, String ZONE, String CONTACT_NUMBER) {
            this.NAME = NAME;
            this.DESIGNATION = DESIGNATION;
            this.EMPLOYEE_CODE = EMPLOYEE_CODE;
            this.GENDER = GENDER;
            this.BRANCH = BRANCH;
            this.REGION = REGION;
            this.ZONE = ZONE;
            this.CONTACT_NUMBER = CONTACT_NUMBER;
        }

        public String getNAME() {
            return NAME;
        }

        public String getDESIGNATION() {
            return DESIGNATION;
        }

        public String getEMPLOYEE_CODE() {
            return EMPLOYEE_CODE;
        }

        public String getGENDER() {
            return GENDER;
        }

        public String getBRANCH() {
            return BRANCH;
        }

        public String getREGION() {
            return REGION;
        }

        public String getZONE() {
            return ZONE;
        }

        public String getCONTACT_NUMBER() {
            return CONTACT_NUMBER;
        }
    }

    @Override
    protected void onDestroy() {

        killTasks();
        super.onDestroy();
    }

    private void killTasks() {

        if (service != null) {
            service.cancel(true);
        }

        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }

        if (covidSelfDeclarationAsynctask != null) {
            covidSelfDeclarationAsynctask.cancel(true);
        }

        /*if (validateOTPGeneralAsyncTask != null) {
            validateOTPGeneralAsyncTask.cancel(true);
        }*/
    }

    class SaveCovidSelfDeclarationAsyncTask extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String result = "", xmlString = "", dbXmlString;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
            if (containmentZone.equalsIgnoreCase("No") && CovidPatientContact.equalsIgnoreCase("No")
                    && InternationalTravelHistory.equalsIgnoreCase("No") &&
                    ChronicMedicalCondition.equalsIgnoreCase("No") && fever.equalsIgnoreCase("No")
                    && cough.equalsIgnoreCase("No")
                    && shortBreath.equalsIgnoreCase("No") && soreThroat.equalsIgnoreCase("No") &&
                    familySymptomsStatus.equalsIgnoreCase("No")) {
                greenRedTag = "Yes";
            } else {
                greenRedTag = "No";
            }
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                Calendar c = Calendar.getInstance();

                int seconds = c.get(Calendar.SECOND);
                int minutes = c.get(Calendar.MINUTE);
                int hour = c.get(Calendar.HOUR);
                int ampm = c.get(Calendar.AM_PM);
                String AMPM;
                if (ampm == 0) {
                    AMPM = "AM";
                } else {
                    AMPM = "PM";
                }
                String time = hour + ":" + minutes + ":" + seconds + " " + AMPM;


                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH) + 1;
                int year = c.get(Calendar.YEAR);
                String date = day + "-" + month + "-" + year;
                //String dbDate = date + "," + time;


                xmlString = generateCovidSelfDeclarationXML();
                dbXmlString = xmlString;
                dbXmlString += "<GreenRedFlag>" + greenRedTag + "</GreenRedFlag>" +
                        "<DBTime>" + time + "</DBTime>" +
                        "<DBDate>" + date + "</DBDate></CovidSelfDeclaration>";
                xmlString += "</CovidSelfDeclaration>";
                running = true;
                DatabaseHelper databaseHelper = new DatabaseHelper(context);

                long rowAdded = databaseHelper.insertCovidSelfDeclarationDetails(strCIFBDMUserId, dbXmlString, date, time);
                if (rowAdded > 0) {
                    SoapObject request;
                    // saveSelfDeclardet_smrt(string xmlStr, string strEmailId, string strMobileNo, string strAuthKey)

                    String NAMESPACE = "http://tempuri.org/";
                    String METHOD_NAME = "saveSelfDeclardet_smrt";
                    request = new SoapObject(NAMESPACE, METHOD_NAME);
                    request.addProperty("xmlStr", xmlString);
                    commonMethods.appendSecurityParams(context, request, strCIFBDMEmailId, strCIFBDMMObileNo);
                    commonMethods.TLSv12Enable();

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);

                    commonMethods.TLSv12Enable();
                    //result = "1";
                    HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                    String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;
                    androidHttpTranport.call(SOAP_ACTION, envelope);
                    Object response = envelope.getResponse();
                    result = response.toString();
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
                if (result.equalsIgnoreCase("1")) {
                    buttonDashboard.setVisibility(View.VISIBLE);
                    buttonDashboard.setOnClickListener((View.OnClickListener) context);
                    //commonMethods.showMessageDialog(context, "Data Saved Successfully");
                    Intent intent = new Intent(context, ViewCovidSelfDeclarationActivity.class);
                    intent.putExtra("xmlString", dbXmlString);
                    startActivity(intent);
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