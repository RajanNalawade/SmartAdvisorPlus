package sbilife.com.pointofsale_bancaagency.home.lmsurvey;


import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.AppSharedPreferences;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class LMSurveyActivity extends AppCompatActivity implements View.OnClickListener, ServiceHits.DownLoadData,
        OnStartDragListener {
    private final String METHOD_NAME = "saveLMsurvey_SMRT";

    private ProgressDialog mProgressDialog;
    private CommonMethods commonMethods;
    private Context context;
    private EditText editTextLowSatisfactionScale;

    private Spinner spinnerAssociationSBIL, spinnerEducationalDetails,
            spinnerSBILProductsAware, spinnerSBILProductsSell,
            spinnerCustomerMeetCount, spinnerTotalTrainingDays,
            spinnerSupportStatus, spinnerIncentiveTime, spinnerSOSPolicy,
            spinnerPaisaGenieStaus;


    private CheckBox CBMutualFund, CBPostOfficePlans, CBHealthInsurance, CBGeneralInsurance, CBNo,
            CBproposalStatusEnq, CBRenewalDueList, CBProductDetails, CBPremiumCalculator, CBMTDYTD,
            CBNewsUpdates, CBOnetimedirectcredit, CBInsuranceCover, CBVisitingCards, CBGroupmedicalcover,
            CBAllowances, CBOpportunity;

    private EditText edittextPartimeOccupation, editTextAdvsiorFeatureRequired,
            editTextIncreaseProductvityNotes, editTextIncreaseLoyaltyNotes;
    private EditText edittextProductTraining, edittextNeedAnalysis, edittextHandedDetails,
            edittextCommissionStructure, edittextstrUsage;


    private Spinner spinnerWorkDetails;
    private StringBuilder LMSurvey;
    private String strCIFBDMUserId = "", strCIFBDMEmailId = "",
            strCIFBDMPassword = "", strCIFBDMMObileNo = "";
    private boolean isFetchData = true;
    private ServiceHits service;
    private LMSurveyAsynce lmSurveyAsynce;
    private RadioGroup radioGroup, radioGroupSatisfactionScale;
    private ItemTouchHelper mItemTouchHelperLMBiggestStrength, mItemTouchHelperLM_biggest_attraction, mItemTouchHelperLMProductivity;
    private RecyclerListAdapter adapterLMBiggestStrength, adapterLM_biggest_attraction, adapterLMProductivity;

    private String strProposalStatusEnqHindi = "प्रपोज़ल संबंधित जानकारी";
    private String strRenewalDueListHindi = "रिन्यूअल ड्यू लिस्ट";
    private String strProductDetailsHindi = "योजनाओं से संबंधित जानकारी";
    private String strPremiumCalcHindi = "प्रीमियम कैलकुलेटर";
    private String strMISMTDYTDHindi = "MTD और YTD बिज़नेस के लिए MIS";
    private String strSBINEwsHindi = "एसबीआई लाइफ की नवीन ख़बरें और समाचार";

    private String strOneTimeDirectCreditHindi = "वनटाइम डायरेक्ट क्रेडिट";
    private String strLifeINsurancePersonalAccCoverHindi = "जीवन बीमा और व्यक्तिगत दुर्घटना बीमा";
    private String strGroupMedicalCoverHindi = "ग्रुप मेडिकल कवर";
    private String strOppParConvHindi = "सम्मेलन में भाग लेने का अवसर";
    private String strVisitCardHindi = "विजिटिंग कार्ड्स और स्टेशनरी";
    private String strMaintAllowancesHindi = "सिलेक्टेड क्लब मेंबर्स के लिए मंथली मेंटेनेंस अलाउंस और टेलीफोन अलाउंस";

    private String strMutualFundHindi = "म्युचुअल फंड";
    private String strPostOfficePlansHindi = "पोस्ट ऑफिस प्लान्स";
    private String strHealthInsuranceHindi = "हेल्थ इंश्योरेंस";
    private String strGeneralInsuranceHindi = "जनरल इंश्योरेंस";
    private String strNOHindi = "नहीं";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_lmsurvey);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "LM Survey");
        context = this;

        spinnerWorkDetails = findViewById(R.id.spinnerWorkDetails);
        editTextLowSatisfactionScale = findViewById(R.id.editTextLowSatisfactionScale);

        spinnerAssociationSBIL = findViewById(R.id.spinnerAssociationSBIL);
        spinnerEducationalDetails = findViewById(R.id.spinnerEducationalDetails);
        spinnerSBILProductsAware = findViewById(R.id.spinnerSBILProductsAware);
        spinnerSBILProductsSell = findViewById(R.id.spinnerSBILProductsSell);

        spinnerCustomerMeetCount = findViewById(R.id.spinnerCustomerMeetCount);
        spinnerTotalTrainingDays = findViewById(R.id.spinnerTotalTrainingDays);
        spinnerSupportStatus = findViewById(R.id.spinnerSupportStatus);
        spinnerIncentiveTime = findViewById(R.id.spinnerIncentiveTime);
        spinnerSOSPolicy = findViewById(R.id.spinnerSOSPolicy);
        spinnerPaisaGenieStaus = findViewById(R.id.spinnerPaisaGenieStaus);

        CBproposalStatusEnq = findViewById(R.id.CBproposalStatusEnq);
        CBRenewalDueList = findViewById(R.id.CBRenewalDueList);
        CBProductDetails = findViewById(R.id.CBProductDetails);
        CBPremiumCalculator = findViewById(R.id.CBPremiumCalculator);
        CBMTDYTD = findViewById(R.id.CBMTDYTD);
        CBNewsUpdates = findViewById(R.id.CBNewsUpdates);

        CBMutualFund = findViewById(R.id.CBMutualFund);
        CBPostOfficePlans = findViewById(R.id.CBPostOfficePlans);
        CBHealthInsurance = findViewById(R.id.CBHealthInsurance);
        CBGeneralInsurance = findViewById(R.id.CBGeneralInsurance);
        CBNo = findViewById(R.id.CBNo);

        CBOnetimedirectcredit = findViewById(R.id.CBOnetimedirectcredit);
        CBInsuranceCover = findViewById(R.id.CBInsuranceCover);
        CBVisitingCards = findViewById(R.id.CBVisitingCards);
        CBGroupmedicalcover = findViewById(R.id.CBGroupmedicalcover);
        CBAllowances = findViewById(R.id.CBAllowances);
        CBOpportunity = findViewById(R.id.CBOpportunity);

        edittextPartimeOccupation = findViewById(R.id.edittextPartimeOccupation);
        editTextAdvsiorFeatureRequired = findViewById(R.id.editTextAdvsiorFeatureRequired);
        editTextIncreaseProductvityNotes = findViewById(R.id.editTextIncreaseProductvityNotes);
        editTextIncreaseLoyaltyNotes = findViewById(R.id.editTextIncreaseLoyaltyNotes);

        edittextProductTraining = findViewById(R.id.edittextProductTraining);
        edittextNeedAnalysis = findViewById(R.id.edittextNeedAnalysis);
        edittextHandedDetails = findViewById(R.id.edittextHandedDetails);
        edittextCommissionStructure = findViewById(R.id.edittextCommissionStructure);
        edittextstrUsage = findViewById(R.id.edittextstrUsage);

        radioGroup = findViewById(R.id.radioGroup);
        radioGroupSatisfactionScale = findViewById(R.id.radioGroupSatisfactionScale);
        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");
        if (fromHome != null && fromHome.equalsIgnoreCase("N")) {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");

            try {
                strCIFBDMPassword = SimpleCrypto.encrypt("SBIL", "sbil");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            getUserDetails();
        }

        Button buttonSave = findViewById(R.id.buttonSave);
        Button buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSave.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);

                String LMSurvey = "";
                ParseXML parseXML = new ParseXML();

                if (isFetchData) {
                    LMSurvey = "";
                } else {
                    LMSurvey = AppSharedPreferences.getData(context, (commonMethods.getLMSurveyString()), "");
                    isFetchData = true;
                }
                switch (radioButton.getText() + "") {
                    case "Hindi":
                        setResources("hi", LMSurvey, parseXML);
                        break;
                    case "English":
                        setResources("en", LMSurvey, parseXML);
                        break;

                }
            }
        });

        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_with_ok_button);
            TextView text = dialog.findViewById(R.id.tv_title);
            text.setTextColor(getResources().getColor(R.color.actionbar_black));
            text.setText("Dear Mr./Ms " + new DatabaseHelper(this).GetUserName()
                    + "\n \nThank you for being associated with SBI Life Insurance. We are on a quest to provide you with better productive opportunities, and your feedback would only help us reach a step closer to achieving it. \n" +
                    "Do spare a few minutes to take a quick survey to help us understand your recent experience with us by completing the survey");
            Button dialogButton = dialog.findViewById(R.id.bt_ok);
            dialogButton.setText("Ok");
            dialogButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        RadioButton radioButtonEnglish = (RadioButton) findViewById(R.id.radioButtonEnglish);
        String LMSurveyXml = AppSharedPreferences.getData(context, (commonMethods.getLMSurveyString()), "");
        if (!TextUtils.isEmpty(LMSurveyXml)) {
            ParseXML parseXML = new ParseXML();
            String userCode = parseXML.parseXmlTag(LMSurveyXml, "emp_id");
            if (userCode.equalsIgnoreCase(commonMethods.GetUserCode(context))) {
                isFetchData = false;
                String languageChosen = parseXML.parseXmlTag(LMSurveyXml, "languageChosen");
                if (!TextUtils.isEmpty(languageChosen) && languageChosen.equalsIgnoreCase("Hindi")) {
                    RadioButton radioButtonHindi = (RadioButton) findViewById(R.id.radioButtonHindi);
                    radioButtonHindi.toggle();
                } else {
                    radioButtonEnglish.toggle();
                }

            } else {
                radioButtonEnglish.toggle();
                AppSharedPreferences.setData(context, (commonMethods.getLMSurveyString()), "");
            }
        } else {
            radioButtonEnglish.toggle();
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder, String identifier) {
        if (identifier.equalsIgnoreCase("LMBigStrength")) {
            mItemTouchHelperLMBiggestStrength.startDrag(viewHolder);
        } else if (identifier.equalsIgnoreCase("LMBigAttraction")) {
            mItemTouchHelperLM_biggest_attraction.startDrag(viewHolder);
        } else if (identifier.equalsIgnoreCase("LMProductivity")) {
            mItemTouchHelperLMProductivity.startDrag(viewHolder);
        }

    }


    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods
                .setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.buttonSubmit:

                if (commonMethods.isNetworkConnected(context)) {
                     /* if ((workDetails.equalsIgnoreCase("Part time") && !TextUtils.isEmpty(partTimeOccup))
                            || workDetails.equalsIgnoreCase("Full time")) {

                    if (!TextUtils.isEmpty(satisfactionScale)) {
                        if (((satisfactionScale.equalsIgnoreCase("1")
                                || satisfactionScale.equalsIgnoreCase("2")
                                || satisfactionScale.equalsIgnoreCase("3"))
                                && !TextUtils.isEmpty(lowSatisfactionScale)) || TextUtils.isEmpty(lowSatisfactionScale)) {*/
                    String productTraining = edittextProductTraining.getText().toString();
                    String needAnalysis = edittextNeedAnalysis.getText().toString();
                    String handedDetails = edittextHandedDetails.getText().toString();
                    String commissionStructure = edittextCommissionStructure.getText().toString();
                    String strUsage = edittextstrUsage.getText().toString();

                    if (!CBMutualFund.isChecked() && !CBPostOfficePlans.isChecked()
                            && !CBHealthInsurance.isChecked() && !CBGeneralInsurance.isChecked()
                            && !CBNo.isChecked()) {
                        commonMethods.showMessageDialog(context, "Please choose options from question 5");
                        return;
                    }
                    if (TextUtils.isEmpty(productTraining) && TextUtils.isEmpty(needAnalysis)
                            && TextUtils.isEmpty(handedDetails) && TextUtils.isEmpty(commissionStructure)
                            && TextUtils.isEmpty(strUsage)) {
                        commonMethods.showMessageDialog(context, "Please describe question 6");
                        return;
                    }

                    if (!CBproposalStatusEnq.isChecked() && !CBRenewalDueList.isChecked()
                            && !CBProductDetails.isChecked() && !CBPremiumCalculator.isChecked()
                            && !CBMTDYTD.isChecked() && !CBNewsUpdates.isChecked()) {
                        commonMethods.showMessageDialog(context, "Please choose options from question 14");
                        return;
                    }

                    String AdvsiorFeatureRequired = editTextAdvsiorFeatureRequired.getText().toString();
                    if (TextUtils.isEmpty(AdvsiorFeatureRequired)) {
                        commonMethods.showMessageDialog(context, "Please describe question 15");
                        return;
                    }

                    int selectedId = radioGroupSatisfactionScale.getCheckedRadioButtonId();

                    RadioButton radioButton = findViewById(selectedId);
                    if (radioButton == null) {
                        commonMethods.showMessageDialog(context, "Please select satisfaction rating from question 17");
                        return;
                    }

                    if (!CBOnetimedirectcredit.isChecked() && !CBInsuranceCover.isChecked()
                            && !CBVisitingCards.isChecked() && !CBGroupmedicalcover.isChecked()
                            && !CBAllowances.isChecked() && !CBOpportunity.isChecked()) {
                        // commonMethods.showMessageDialog(context,"Please choose Club Membership program aware");
                        commonMethods.showMessageDialog(context, "Please choose options from question 19");
                        return;
                    }

                    String increaseProductvityNotes = editTextIncreaseProductvityNotes.getText().toString();
                    String increaseLoyaltyNotes = editTextIncreaseLoyaltyNotes.getText().toString();
                    if (TextUtils.isEmpty(increaseProductvityNotes)) {
                        commonMethods.showMessageDialog(context, "Please describe question 23");
                        return;
                    }

                    if (TextUtils.isEmpty(increaseLoyaltyNotes)) {
                        commonMethods.showMessageDialog(context, "Please describe question 24");
                        return;
                    }
                    fetchData("submit");


                        /*} else {
                            commonMethods.showMessageDialog(context, "Please enter reason for Low Satisfaction");
                        }
                    } else {
                        editTextSatisfactionScale.setFocusable(true);
                        commonMethods.showMessageDialog(context, "Please enter Satisfaction Scale");
                    }
                    } else {
                        edittextPartimeOccupation.setFocusable(true);
                        commonMethods.showMessageDialog(context, "Please enter Part Time Work Details");
                    }*/

                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }


                break;

            case R.id.buttonSave:
                String LMSurveyXml = fetchData("save");

                AppSharedPreferences.setData(context, (commonMethods.getLMSurveyString()), LMSurveyXml);
                commonMethods.showMessageDialog(context, "Data Saved Successfully");
                break;
        }

    }

    @Override
    public void downLoadData() {
        lmSurveyAsynce = new LMSurveyAsynce();
        lmSurveyAsynce.execute();
    }

    class LMSurveyAsynce extends AsyncTask<String, String, String> {
        private volatile boolean running = true;
        int flag = 0;

        private final String NAMESPACE = "http://tempuri.org/";
        private final String URl = ServiceURL.SERVICE_URL;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading Please wait...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);

            mProgressDialog.setButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            if (lmSurveyAsynce != null) {
                                lmSurveyAsynce.cancel(true);
                            }
                            if (mProgressDialog != null) {
                                if (mProgressDialog.isShowing()) {
                                    mProgressDialog.dismiss();
                                }
                            }
                        }
                    });

            mProgressDialog.setMax(100);
            mProgressDialog.show();

        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                SoapObject request;

                request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("xmlStr", LMSurvey.toString());
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", commonMethods.getStrAuth());

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
                androidHttpTranport.call(
                        SOAP_ACTION, envelope);

                SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();
                String inputpolicylist = sa.toString();

                if (inputpolicylist.equalsIgnoreCase("1")) {
                    flag = 1;
                } else if (inputpolicylist.equalsIgnoreCase("2")) {
                    flag = 2;
                } else {
                    flag = 0;
                }

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
                    commonMethods.showMessageDialog(context, "Data Uploaded Sucessfully");
                } else if (flag == 2) {
                    commonMethods.showMessageDialog(context, "Data Already Exist");
                } else {
                    commonMethods.showMessageDialog(context, "Data Upload Failed");
                }
            } else {
                commonMethods.showMessageDialog(context, "Server Not responding!");
            }
        }

    }

    @Override
    protected void onDestroy() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (lmSurveyAsynce != null) {
            lmSurveyAsynce.cancel(true);
        }

        if (service != null) {
            service.cancel(true);
        }

        super.onDestroy();
    }


    private void setResources(String language, String LMSurveyXml, ParseXML parseXML) {
        //Context context;
        Resources resources = getResources();
        TextView tvOptionLanguage, tvOptionOne, tvOptionTwo, tvOptionThree, tvOptionFour, tvOptionFive,
                tvOptionSix, tvOptionSeven, tvOptionEight, tvOptionNine, tvOptionTen,
                tvOptionEleven, tvOptionElevenSub, tvOptionTwelve, tvOptionTwelveSub, tvOptionThirteen,
                tvOptionThirteenSub, tvOptionFourteen, tvOptionFifteen,
                tvOptionsixteen, textViewSatisfactionScale, textviewBranchRating,
                tvOptionNineteen, tvOptionTwenty, tvOptionTwentyOne, tvOptionTwentyTwo,
                tvOptionTwentyThree, tvOptionTwentyFour, TVProductTraining, TVNeedAnalysis,
                TVHandedDetails, TVCommissionStructure, TVstrUsage;


        tvOptionLanguage = findViewById(R.id.tvOptionLanguage);
        tvOptionOne = findViewById(R.id.tvOptionOne);
        tvOptionTwo = findViewById(R.id.tvOptionTwo);
        tvOptionThree = findViewById(R.id.tvOptionThree);
        tvOptionFour = findViewById(R.id.tvOptionFour);
        tvOptionFive = findViewById(R.id.tvOptionFive);
        tvOptionSix = findViewById(R.id.tvOptionSix);
        tvOptionSeven = findViewById(R.id.tvOptionSeven);
        tvOptionEight = findViewById(R.id.tvOptionEight);
        tvOptionNine = findViewById(R.id.tvOptionNine);
        tvOptionTen = findViewById(R.id.tvOptionTen);
        tvOptionEleven = findViewById(R.id.tvOptionEleven);
        tvOptionElevenSub = findViewById(R.id.tvOptionElevenSub);
        tvOptionTwelve = findViewById(R.id.tvOptionTwelve);
        tvOptionTwelveSub = findViewById(R.id.tvOptionTwelveSub);
        tvOptionThirteen = findViewById(R.id.tvOptionThirteen);
        tvOptionThirteenSub = findViewById(R.id.tvOptionThirteenSub);
        tvOptionFourteen = findViewById(R.id.tvOptionFourteen);
        tvOptionFifteen = findViewById(R.id.tvOptionFifteen);
        tvOptionsixteen = findViewById(R.id.tvOptionsixteen);
        textViewSatisfactionScale = findViewById(R.id.textViewSatisfactionScale);
        textviewBranchRating = findViewById(R.id.textviewBranchRating);
        tvOptionNineteen = findViewById(R.id.tvOptionNineteen);
        tvOptionTwenty = findViewById(R.id.tvOptionTwenty);
        tvOptionTwentyOne = findViewById(R.id.tvOptionTwentyOne);
        tvOptionTwentyTwo = findViewById(R.id.tvOptionTwentyTwo);
        tvOptionTwentyThree = findViewById(R.id.tvOptionTwentyThree);
        tvOptionTwentyFour = findViewById(R.id.tvOptionTwentyFour);

        TVProductTraining = findViewById(R.id.TVProductTraining);
        TVNeedAnalysis = findViewById(R.id.TVNeedAnalysis);
        TVHandedDetails = findViewById(R.id.TVHandedDetails);
        TVCommissionStructure = findViewById(R.id.TVCommissionStructure);
        TVstrUsage = findViewById(R.id.TVstrUsage);

        if (language.equalsIgnoreCase("hi")) {
            String strLanguageChooseOption = "कृपया अपनी भाषा चुनें";
            String tvOptionOneHindi = "1. आप कब से लाइफ मित्र के रूप में एसबीआई लाइफ से जुड़े हैं?";
            String tvOptionTwoHindi = "2. कृपया हमें अपने शैक्षिक विवरण के बारे में बताएं ?";
            String tvOptionThreeHindi = "3.	कृपया हमें बताएं कि एसबीआई लाइफ के लिए आप किस तरह से काम करते हैं ?";
            String tvOptionFourHindi = "4. यदि उपरोक्त प्रश्न का उत्तर 'पार्ट टाइम' है तो आपका मुख्य व्यवसाय क्या है?";
            String tvOptionFiveHindi = "5. क्या आप एसबीआई लाइफ इंश्योरेंस प्रोडक्ट्स के अलावा नीचे दिए गए किसी अन्य फाइनेंशियल प्रोडक्ट को बेच रहे हैं ?";
            String tvOptionSixHindi = "6. संक्षेप में बताएं कि आपका SBI Life से जुड़ने का अनुभव (उन लाइफ मित्र  के लिए जो एक साल से भी कम समय में जॉइन हुए हैं) कैसा रहा";
            String tvOptionSevenHindi = "7. आप एसबीआई लाइफ के कितनी योजनाओं के बारे में जानते हैं";
            String tvOptionEightHindi = "8. आप एसबीआई लाइफ की कितनी योजनाएं बेचते हैं?";
            String tvOptionNineHindi = "9. आप एक सप्ताह में कितने ग्राहकों से मिलते हैं?";
            String tvOptionTenHindi = "10. पिछले 1 साल में आपने कितने दिनों की ट्रेनिंग में भाग लिया है?";
            String tvOptionElevenHindi = "11. आपके अनुसार ग्राहक के दृष्टिकोण से एसबीआई लाइफ की सबसे बड़ी ताकत क्या है? महत्व के क्रम में रैंक दें, 1 सबसे महत्वपूर्ण है और 4 सबसे कम महत्वपूर्ण है.";
            String tvOptionElevenSubHindi = "महत्त्व के अनुसार खींचें और व्यवस्थित करे.";
            String tvOptionTwelveHindi = "12. लाइफ मित्र  के रूप में, एसबीआई लाइफ में करियर के दृष्टिकोण से आपको क्या आकर्षक लगता है ? महत्व के क्रम में रैंक दें, 1 सबसे महत्वपूर्ण है और 5 सबसे कम महत्वपूर्ण है";
            String tvOptionThirteenHindi = "13.	अगर आने वाले 6 महीनों के लिए पूछा जाए तो अपनी प्रोडक्टिविटी बढ़ाने के लिए आपको किस तरह के प्रशिक्षण (लर्निंग इंटरवेंशन्स) की आवश्यकता है,? महत्व के क्रम में रैंक दें, 1 सबसे महत्वपूर्ण है और 5 सबसे कम महत्वपूर्ण है";
            String tvOptionFourteenHindi = "14.	स्मार्ट एडवाइजर ऐप की उन विशेषताओं का चयन करें जिनके बारे में आप जानते हैं। लागू होने पर एक से अधिक का चयन करें";
            String tvOptionFifteenHindi = "15.	स्मार्ट एडवाइजर ऐप में आप कौन सी नई सुविधाएँ जोड़ना चाहेंगे?";
            String tvOptionsixteenHindi = "16.	क्या आपको अपनी ब्रांच द्वारा लीड्स वितरण के माध्यम से सपोर्ट दिया जाता है?";
            String textViewSatisfactionScaleHindi = "17. 1-5 के पैमाने पर आप ब्रांच के इंफ्रास्ट्रक्चर और ब्रांच लेवल पर प्रदान किए गए सहयोग से कितने संतुष्ट हैं, जिसमें 1 बेहद खराब है और 5 बेहद अच्छा है?";
            String textviewBranchRatingHindi = "18.	यदि रेटिंग उपरोक्त प्रश्न के लिए 1-3 के बीच है तो आपको क्या लगता है कि क्या बेहतर किया जा सकता है?";
            String tvOptionNineteenHindi = "19.	एसबीआई लाइफ के क्लब मेम्बरशिप प्रोग्राम के लाभों का चयन करें जिनसे आप अवगत हैं? लागू होने पर एक से अधिक का चयन करें";
            String tvOptionTwentyHindi = "20. क्या आपको समय पर कमीशन और इंसेंटिव मिलते हैं?";
            String tvOptionTwentyOneHindi = "21. क्या आप एसबीआई लाइफ के लाइफ मित्र  के लिए SQS पॉलिसी के बारे में जानते हैं?";
            String tvOptionTwentyTwoHindi = "22. क्या आप एसबीआई लाइफ के ग्राहकों के लिए 'PaisaGenie' फाइनेंशियल प्लानिंग एप्लीकेशन के बारे में जानते हैं और ग्राहकों को इसे डाउनलोड करने के लिए कह रहे हैं?";
            String tvOptionTwentyThreeHindi = "23. आपके अनुसार एसबीआई लाइफ को अपने लाइफ मित्र  की आय और प्रोडक्टिविटी बढ़ाने के लिए क्या करना चाहिए?";
            String tvOptionTwentyFourHindi = "24. ग्राहकों में ब्रांड के प्रति विश्वसनीयता (brand loyalty) बढ़ाने के लिए एसबीआई लाइफ को क्या क़दम उठाने चाहिए?";
            tvOptionLanguage.setText(strLanguageChooseOption);
            tvOptionOne.setText(tvOptionOneHindi);
            tvOptionTwo.setText(tvOptionTwoHindi);
            tvOptionThree.setText(tvOptionThreeHindi);
            tvOptionFour.setText(tvOptionFourHindi);
            tvOptionFive.setText(tvOptionFiveHindi);
            tvOptionSix.setText(tvOptionSixHindi);
            tvOptionSeven.setText(tvOptionSevenHindi);
            tvOptionEight.setText(tvOptionEightHindi);
            tvOptionNine.setText(tvOptionNineHindi);
            tvOptionTen.setText(tvOptionTenHindi);
            tvOptionEleven.setText(tvOptionElevenHindi);
            tvOptionElevenSub.setText(tvOptionElevenSubHindi);
            tvOptionTwelve.setText(tvOptionTwelveHindi);
            tvOptionTwelveSub.setText(tvOptionElevenSubHindi);
            tvOptionThirteen.setText(tvOptionThirteenHindi);
            tvOptionThirteenSub.setText(tvOptionElevenSubHindi);
            tvOptionFourteen.setText(tvOptionFourteenHindi);
            tvOptionFifteen.setText(tvOptionFifteenHindi);
            tvOptionsixteen.setText(tvOptionsixteenHindi);
            textViewSatisfactionScale.setText(textViewSatisfactionScaleHindi);
            textviewBranchRating.setText(textviewBranchRatingHindi);
            tvOptionNineteen.setText(tvOptionNineteenHindi);
            tvOptionTwenty.setText(tvOptionTwentyHindi);
            tvOptionTwentyOne.setText(tvOptionTwentyOneHindi);
            tvOptionTwentyTwo.setText(tvOptionTwentyTwoHindi);
            tvOptionTwentyThree.setText(tvOptionTwentyThreeHindi);
            tvOptionTwentyFour.setText(tvOptionTwentyFourHindi);

            String strProductTrainingHindi = "प्रोडक्ट ट्रेनिंग दी गई";
            String strNeedAnalysisHindi = "समझाया गया कि नीड एनालिसिस कैसे करते हैं ";
            String strHandedDetailsHindi = "प्रोडक्ट फ्लायर/फॉर्म/कंटेस्ट डिटेल्स आदि दिए गए ";
            String strCommissionStructureHindi = "कमीशन विवरण समझाया गया ";
            String strUsageHindi = "mConnect और स्मार्ट एडवाइज़र जैसी डिजिटल Mobile Applications का उपयोग";

            TVProductTraining.setText(strProductTrainingHindi);
            TVNeedAnalysis.setText(strNeedAnalysisHindi);
            TVHandedDetails.setText(strHandedDetailsHindi);
            TVCommissionStructure.setText(strCommissionStructureHindi);
            TVstrUsage.setText(strUsageHindi);
        } else {
            tvOptionLanguage.setText(resources.getString(R.string.strLanguageChooseOption));
            tvOptionOne.setText(resources.getString(R.string.tvOptionOne));
            tvOptionTwo.setText(resources.getString(R.string.tvOptionTwo));
            tvOptionThree.setText(resources.getString(R.string.tvOptionThree));
            tvOptionFour.setText(resources.getString(R.string.tvOptionFour));
            tvOptionFive.setText(resources.getString(R.string.tvOptionFive));
            tvOptionSix.setText(resources.getString(R.string.tvOptionSix));
            tvOptionSeven.setText(resources.getString(R.string.tvOptionSeven));
            tvOptionEight.setText(resources.getString(R.string.tvOptionEight));
            tvOptionNine.setText(resources.getString(R.string.tvOptionNine));
            tvOptionTen.setText(resources.getString(R.string.tvOptionTen));
            tvOptionEleven.setText(resources.getString(R.string.tvOptionEleven));
            tvOptionElevenSub.setText(resources.getString(R.string.tvOptionElevenSub));
            tvOptionTwelve.setText(resources.getString(R.string.tvOptionTwelve));
            tvOptionTwelveSub.setText(resources.getString(R.string.tvOptionElevenSub));
            tvOptionThirteen.setText(resources.getString(R.string.tvOptionThirteen));
            tvOptionThirteenSub.setText(resources.getString(R.string.tvOptionElevenSub));
            tvOptionFourteen.setText(resources.getString(R.string.tvOptionFourteen));
            tvOptionFifteen.setText(resources.getString(R.string.tvOptionFifteen));
            tvOptionsixteen.setText(resources.getString(R.string.tvOptionsixteen));
            textViewSatisfactionScale.setText(resources.getString(R.string.textViewSatisfactionScale));
            textviewBranchRating.setText(resources.getString(R.string.textviewBranchRating));
            tvOptionNineteen.setText(resources.getString(R.string.tvOptionNineteen));
            tvOptionTwenty.setText(resources.getString(R.string.tvOptionTwenty));
            tvOptionTwentyOne.setText(resources.getString(R.string.tvOptionTwentyOne));
            tvOptionTwentyTwo.setText(resources.getString(R.string.tvOptionTwentyTwo));
            tvOptionTwentyThree.setText(resources.getString(R.string.tvOptionTwentyThree));
            tvOptionTwentyFour.setText(resources.getString(R.string.tvOptionTwentyFour));

            TVProductTraining.setText(resources.getString(R.string.strProductTraining));
            TVNeedAnalysis.setText(resources.getString(R.string.strNeedAnalysis));
            TVHandedDetails.setText(resources.getString(R.string.strHandedDetails));
            TVCommissionStructure.setText(resources.getString(R.string.strCommissionStructure));
            TVstrUsage.setText(resources.getString(R.string.strUsage));


        }


        setSpinners(resources, LMSurveyXml, parseXML, language);
        setSwippableRecyclerview(resources, language);

        if (language.equalsIgnoreCase("hi")) {


            CBproposalStatusEnq.setText(strProposalStatusEnqHindi);
            CBRenewalDueList.setText(strRenewalDueListHindi);
            CBProductDetails.setText(strProductDetailsHindi);
            CBPremiumCalculator.setText(strPremiumCalcHindi);
            CBMTDYTD.setText(strMISMTDYTDHindi);
            CBNewsUpdates.setText(strSBINEwsHindi);

            CBMutualFund.setText(strMutualFundHindi);
            CBPostOfficePlans.setText(strPostOfficePlansHindi);
            CBHealthInsurance.setText(strHealthInsuranceHindi);
            CBGeneralInsurance.setText(strGeneralInsuranceHindi);
            CBNo.setText(strNOHindi);

            CBOnetimedirectcredit.setText(strOneTimeDirectCreditHindi);
            CBInsuranceCover.setText(strLifeINsurancePersonalAccCoverHindi);
            CBVisitingCards.setText(strVisitCardHindi);
            CBGroupmedicalcover.setText(strGroupMedicalCoverHindi);
            CBAllowances.setText(strMaintAllowancesHindi);
            CBOpportunity.setText(strOppParConvHindi);

        } else {
            CBproposalStatusEnq.setText(resources.getString(R.string.strProposalStatusEnq));
            CBRenewalDueList.setText(resources.getString(R.string.strRenewalDueList));
            CBProductDetails.setText(resources.getString(R.string.strProductDetails));
            CBPremiumCalculator.setText(resources.getString(R.string.strPremiumCalc));
            CBMTDYTD.setText(resources.getString(R.string.strMISMTDYTD));
            CBNewsUpdates.setText(resources.getString(R.string.strSBINEws));

            CBMutualFund.setText(resources.getString(R.string.strMutualFund));
            CBPostOfficePlans.setText(resources.getString(R.string.strPostOfficePlans));
            CBHealthInsurance.setText(resources.getString(R.string.strHealthInsurance));
            CBGeneralInsurance.setText(resources.getString(R.string.strGeneralInsurance));
            CBNo.setText(resources.getString(R.string.strNO));

            CBOnetimedirectcredit.setText(resources.getString(R.string.strOneTimeDirectCredit));
            CBInsuranceCover.setText(resources.getString(R.string.strLifeINsurancePersonalAccCover));
            CBVisitingCards.setText(resources.getString(R.string.strVisitCard));
            CBGroupmedicalcover.setText(resources.getString(R.string.strGroupMedicalCover));
            CBAllowances.setText(resources.getString(R.string.strMaintAllowances));
            CBOpportunity.setText(resources.getString(R.string.strOppParConv));
        }

    }

    private void setSwippableRecyclerview(Resources resources, String language) {


        //LM Biggest Strength
        List<String> lmBiggestStrength;
        List<String> LM_biggest_attraction;
        List<String> LMProductivity;

        if (language.equalsIgnoreCase("hi")) {
            lmBiggestStrength = new ArrayList<>();
            lmBiggestStrength.add("ब्रांड");
            lmBiggestStrength.add("दमदार प्रोडक्ट्स");
            lmBiggestStrength.add("पॉलिसी पर उचित रिटर्न");
            lmBiggestStrength.add("तत्काल सेवा और क्लेम सेटलमेंट");
            lmBiggestStrength.add("अन्य");

            LM_biggest_attraction = new ArrayList<>();
            LM_biggest_attraction.add("मैनेजर और ब्रांच मैनेजर से सहयोग");
            LM_biggest_attraction.add("प्रशिक्षण");
            LM_biggest_attraction.add("योजनाओं का सही मिश्रण");
            LM_biggest_attraction.add("डिजिटल एप्लिकेशन के संदर्भ में उपयुक्त सहयोग");
            LM_biggest_attraction.add("पुरस्कार और सम्मान कार्यक्रम");
            LM_biggest_attraction.add("अन्य");

            LMProductivity = new ArrayList<>();
            LMProductivity.add("प्रोडक्ट");
            LMProductivity.add("फाइनेंशियल मार्केट्स");
            LMProductivity.add("नीड एनालिसिस");
            LMProductivity.add("सेलिंग स्किल्स");
            LMProductivity.add("डिजिटल एसेट्स का प्रयोग");
            LMProductivity.add("अन्य");

        } else {
            lmBiggestStrength = Arrays.asList(resources.getStringArray(R.array.LM_biggest_strength));
            LM_biggest_attraction = Arrays.asList(resources.getStringArray(R.array.LM_biggest_attraction));
            LMProductivity = Arrays.asList(resources.getStringArray(R.array.LM_Productivity));
        }


        adapterLMBiggestStrength = new RecyclerListAdapter(this, this,
                lmBiggestStrength, "LMBigStrength");
        RecyclerView recyclerViewLMBiggestStrength = findViewById(R.id.recyclerviewLMBiggestStrength);
        recyclerViewLMBiggestStrength.setHasFixedSize(true);
        recyclerViewLMBiggestStrength.setAdapter(adapterLMBiggestStrength);
        recyclerViewLMBiggestStrength.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.Callback callbackLMBiggestStrength = new SimpleItemTouchHelperCallback(adapterLMBiggestStrength);
        mItemTouchHelperLMBiggestStrength = new ItemTouchHelper(callbackLMBiggestStrength);
        mItemTouchHelperLMBiggestStrength.attachToRecyclerView(recyclerViewLMBiggestStrength);

        //END LM Biggest Strength


        //LM Biggest Attraction

        adapterLM_biggest_attraction = new RecyclerListAdapter(this, this,
                LM_biggest_attraction, "LMBigAttraction");

        RecyclerView recyclerviewLMAttracttion = findViewById(R.id.recyclerviewLMAttracttion);
        recyclerviewLMAttracttion.setHasFixedSize(true);
        recyclerviewLMAttracttion.setAdapter(adapterLM_biggest_attraction);
        recyclerviewLMAttracttion.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.Callback callbackLM_biggest_attraction = new SimpleItemTouchHelperCallback(adapterLM_biggest_attraction);
        mItemTouchHelperLM_biggest_attraction = new ItemTouchHelper(callbackLM_biggest_attraction);
        mItemTouchHelperLM_biggest_attraction.attachToRecyclerView(recyclerviewLMAttracttion);
        //End LM Biggest Attraction

        //LM Productitvity

        adapterLMProductivity = new RecyclerListAdapter(this, this,
                LMProductivity, "LMProductivity");

        RecyclerView recyclerviewLMProductivity = findViewById(R.id.recyclerviewLMProductivity);
        recyclerviewLMProductivity.setHasFixedSize(true);
        recyclerviewLMProductivity.setAdapter(adapterLMProductivity);
        recyclerviewLMProductivity.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.Callback callbackLMProductivity = new SimpleItemTouchHelperCallback(adapterLMProductivity);
        mItemTouchHelperLMProductivity = new ItemTouchHelper(callbackLMProductivity);
        mItemTouchHelperLMProductivity.attachToRecyclerView(recyclerviewLMProductivity);
        //End Productivity
    }

    private void setSpinners(Resources resources, String LMSurveyXml, ParseXML parseXML, String language) {

        ArrayAdapter<String> AssociationSBILAdapter;
        ArrayAdapter<String> EducationalDetailsAdapter, WorkDetailsSBILAdapter, ProductsAwareAdapter,
                ProductsSellAdapter, CustomerMeetCountAdapter, TotalTrainingDaysAdapter,
                SupportStatusAdapter, IncentiveTimeAdapter, SOSPolicyAdapter, PaisaGenieStausAdapter;
        if (language.equalsIgnoreCase("hi")) {

            String[] LMsbil_association = {"1 साल", "1-3 साल", "3-5 साल", "> 5 साल"};
            AssociationSBILAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item, LMsbil_association);

            String[] LM_education_details = {"अंडरग्रेजुएट", "ग्रेजुएट", "पोस्ट ग्रेजुएट"};
            EducationalDetailsAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, LM_education_details);

            String[] LM_work_details = {"फुल टाइम", "पार्ट टाइम"};
            WorkDetailsSBILAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item, LM_work_details);

            String[] LM_products_Aware = {"1-3", "4-6", "6 से ज़्यादा"};
            ProductsAwareAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item, LM_products_Aware);

            String[] LM_products_sell = {"1-3", "4-6", "6 से ज़्यादा"};
            ProductsSellAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item, LM_products_sell);

            String[] LM_cust_meet_count = {"2 से कम", "3 – 6", "6 – 12", "12 से ज़्यादा"};
            CustomerMeetCountAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item, LM_cust_meet_count);

            String[] LM_training_days = {"1-3 दिन", "4-6 दिन", "6 दिन से ज़्यादा"};
            TotalTrainingDaysAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item, LM_training_days);

            String[] LM_yes_no = {"हाँ", "नहीं"};

            SupportStatusAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item, LM_yes_no);

            IncentiveTimeAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item, LM_yes_no);

            SOSPolicyAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item, LM_yes_no);

            PaisaGenieStausAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item, LM_yes_no);
        } else {
            AssociationSBILAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item, getResources().
                    getStringArray(R.array.LMsbil_association));

            EducationalDetailsAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item, getResources().getStringArray(R.array.LM_education_details));

            WorkDetailsSBILAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item, getResources().getStringArray(R.array.LM_work_details));

            ProductsAwareAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item, resources.getStringArray(R.array.LM_products_Aware));

            ProductsSellAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item, resources.getStringArray(R.array.LM_products_sell));

            CustomerMeetCountAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item, resources.getStringArray(R.array.LM_cust_meet_count));

            TotalTrainingDaysAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item, resources.getStringArray(R.array.LM_training_days));

            SupportStatusAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item, resources.getStringArray(R.array.LM_yes_no));

            IncentiveTimeAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item, resources.getStringArray(R.array.LM_yes_no));

            SOSPolicyAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item, resources.getStringArray(R.array.LM_yes_no));

            PaisaGenieStausAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item, resources.getStringArray(R.array.LM_yes_no));
        }

        AssociationSBILAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spinnerAssociationSBIL.setAdapter(AssociationSBILAdapter);

        EducationalDetailsAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spinnerEducationalDetails.setAdapter(EducationalDetailsAdapter);

        WorkDetailsSBILAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spinnerWorkDetails.setAdapter(WorkDetailsSBILAdapter);


        ProductsAwareAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spinnerSBILProductsAware.setAdapter(ProductsAwareAdapter);

        ProductsSellAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spinnerSBILProductsSell.setAdapter(ProductsSellAdapter);


        CustomerMeetCountAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spinnerCustomerMeetCount.setAdapter(CustomerMeetCountAdapter);

        TotalTrainingDaysAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spinnerTotalTrainingDays.setAdapter(TotalTrainingDaysAdapter);


        SupportStatusAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spinnerSupportStatus.setAdapter(SupportStatusAdapter);

        IncentiveTimeAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spinnerIncentiveTime.setAdapter(IncentiveTimeAdapter);

        SOSPolicyAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spinnerSOSPolicy.setAdapter(SOSPolicyAdapter);

        PaisaGenieStausAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spinnerPaisaGenieStaus.setAdapter(PaisaGenieStausAdapter);

        if (!TextUtils.isEmpty(LMSurveyXml)) {
            try {
                String sbilAssociation = parseXML.parseXmlTag(LMSurveyXml, "sbil_association");
                String educationDetails = parseXML.parseXmlTag(LMSurveyXml, "educational_details");
                String workDetails = parseXML.parseXmlTag(LMSurveyXml, "sbil_work_time");
                String part_time_occu = parseXML.parseXmlTag(LMSurveyXml, "part_time_occu");

                String product_aware_count = parseXML.parseXmlTag(LMSurveyXml, "product_aware_count");
                String product_sell_count = parseXML.parseXmlTag(LMSurveyXml, "product_sell_count");
                String customer_meet_count = parseXML.parseXmlTag(LMSurveyXml, "customer_meet_count");
                String training_attend_days = parseXML.parseXmlTag(LMSurveyXml, "training_attend_days");
                String support_thru_leads = parseXML.parseXmlTag(LMSurveyXml, "support_thru_leads");
                String is_incentive_delay = parseXML.parseXmlTag(LMSurveyXml, "is_incentive_delay");
                String is_aware_SQS_policy = parseXML.parseXmlTag(LMSurveyXml, "is_aware_SQS_policy");
                String know_PaisGenie = parseXML.parseXmlTag(LMSurveyXml, "know_PaisGenie");

                String any_other_comp_prod = parseXML.parseXmlTag(LMSurveyXml, "any_other_comp_prod");
                String smartadvisor_feature_known = parseXML.parseXmlTag(LMSurveyXml, "smartadvisor_feature_known");
                String benefits_club_member = parseXML.parseXmlTag(LMSurveyXml, "benefits_club_member");
                String smartadvisor_feature_reqd = parseXML.parseXmlTag(LMSurveyXml, "smartadvisor_feature_reqd");


                if (!TextUtils.isEmpty(part_time_occu)) {
                    edittextPartimeOccupation.setText(part_time_occu);
                }

                spinnerAssociationSBIL.setSelection(AssociationSBILAdapter.getPosition(sbilAssociation));
                spinnerEducationalDetails.setSelection(EducationalDetailsAdapter.getPosition(educationDetails));
                spinnerWorkDetails.setSelection(WorkDetailsSBILAdapter.getPosition(workDetails));
                spinnerSBILProductsAware.setSelection(ProductsAwareAdapter.getPosition(product_aware_count));
                spinnerSBILProductsSell.setSelection(ProductsSellAdapter.getPosition(product_sell_count));
                spinnerCustomerMeetCount.setSelection(CustomerMeetCountAdapter.getPosition(customer_meet_count));
                spinnerTotalTrainingDays.setSelection(TotalTrainingDaysAdapter.getPosition(training_attend_days));
                spinnerSupportStatus.setSelection(SupportStatusAdapter.getPosition(support_thru_leads));
                spinnerIncentiveTime.setSelection(IncentiveTimeAdapter.getPosition(is_incentive_delay));
                spinnerSOSPolicy.setSelection(SOSPolicyAdapter.getPosition(is_aware_SQS_policy));
                spinnerPaisaGenieStaus.setSelection(PaisaGenieStausAdapter.getPosition(know_PaisGenie));

                if (!TextUtils.isEmpty(any_other_comp_prod)) {
                    String[] any_other_comp_prodArray = any_other_comp_prod.split(",");

                    String mutualFund = any_other_comp_prodArray[0];
                    String postOfficePlans = any_other_comp_prodArray[1];
                    String healthInsurance = any_other_comp_prodArray[2];
                    String generalInsurance = any_other_comp_prodArray[3];
                    String noOption = any_other_comp_prodArray[4];
                    if (TextUtils.isEmpty(mutualFund.trim())) {
                        CBMutualFund.setChecked(false);
                    } else {
                        CBMutualFund.setChecked(true);
                    }

                    if (TextUtils.isEmpty(postOfficePlans.trim())) {
                        CBPostOfficePlans.setChecked(false);
                    } else {
                        CBPostOfficePlans.setChecked(true);
                    }

                    if (TextUtils.isEmpty(healthInsurance.trim())) {
                        CBHealthInsurance.setChecked(false);
                    } else {
                        CBHealthInsurance.setChecked(true);
                    }
                    if (TextUtils.isEmpty(generalInsurance.trim())) {
                        CBGeneralInsurance.setChecked(false);
                    } else {
                        CBGeneralInsurance.setChecked(true);
                    }

                    if (TextUtils.isEmpty(noOption.trim())) {
                        CBNo.setChecked(false);
                    } else {
                        CBNo.setChecked(true);
                    }

                }

                if (!TextUtils.isEmpty(smartadvisor_feature_known)) {
                    String[] smartadvisor_feature_knownArray = smartadvisor_feature_known.split(",");

                    String proposalStatusEnq = smartadvisor_feature_knownArray[0];
                    String renewalDueList = smartadvisor_feature_knownArray[1];
                    String productDetails = smartadvisor_feature_knownArray[2];
                    String premiumCalculator = smartadvisor_feature_knownArray[3];
                    String MTDYTD = smartadvisor_feature_knownArray[4];
                    String newsUpdates = smartadvisor_feature_knownArray[5];

                    if (TextUtils.isEmpty(proposalStatusEnq.trim())) {
                        CBproposalStatusEnq.setChecked(false);
                    } else {
                        CBproposalStatusEnq.setChecked(true);
                    }

                    if (TextUtils.isEmpty(renewalDueList.trim())) {
                        CBRenewalDueList.setChecked(false);
                    } else {
                        CBRenewalDueList.setChecked(true);
                    }

                    if (TextUtils.isEmpty(productDetails.trim())) {
                        CBProductDetails.setChecked(false);
                    } else {
                        CBProductDetails.setChecked(true);
                    }

                    if (TextUtils.isEmpty(premiumCalculator.trim())) {
                        CBPremiumCalculator.setChecked(false);
                    } else {
                        CBPremiumCalculator.setChecked(true);
                    }

                    if (TextUtils.isEmpty(MTDYTD.trim())) {
                        CBMTDYTD.setChecked(false);
                    } else {
                        CBMTDYTD.setChecked(true);
                    }

                    if (TextUtils.isEmpty(newsUpdates.trim())) {
                        CBNewsUpdates.setChecked(false);
                    } else {
                        CBNewsUpdates.setChecked(true);
                    }

                }

                if (!TextUtils.isEmpty(benefits_club_member)) {
                    String[] benefits_club_memberArray = benefits_club_member.split(",");

                    String onetimedirectcredit = benefits_club_memberArray[0];
                    String InsuranceCover = benefits_club_memberArray[1];
                    String VisitingCards = benefits_club_memberArray[2];
                    String Groupmedicalcover = benefits_club_memberArray[3];
                    String allowances = benefits_club_memberArray[4];
                    String opportunity = benefits_club_memberArray[5];

                    if (TextUtils.isEmpty(onetimedirectcredit.trim())) {
                        CBOnetimedirectcredit.setChecked(false);
                    } else {
                        CBOnetimedirectcredit.setChecked(true);
                    }

                    if (TextUtils.isEmpty(InsuranceCover.trim())) {
                        CBInsuranceCover.setChecked(false);
                    } else {
                        CBInsuranceCover.setChecked(true);
                    }

                    if (TextUtils.isEmpty(VisitingCards.trim())) {
                        CBVisitingCards.setChecked(false);
                    } else {
                        CBVisitingCards.setChecked(true);
                    }

                    if (TextUtils.isEmpty(Groupmedicalcover.trim())) {
                        CBGroupmedicalcover.setChecked(false);
                    } else {
                        CBGroupmedicalcover.setChecked(true);
                    }

                    if (TextUtils.isEmpty(allowances.trim())) {
                        CBAllowances.setChecked(false);
                    } else {
                        CBAllowances.setChecked(true);
                    }

                    if (TextUtils.isEmpty(opportunity.trim())) {
                        CBOpportunity.setChecked(false);
                    } else {
                        CBOpportunity.setChecked(true);
                    }
                }

                String onboard_experience = parseXML.parseXmlTag(LMSurveyXml, "onboard_experience");
                if (!TextUtils.isEmpty(onboard_experience)) {
                    String[] onBoardExperiencearray = onboard_experience.split(",");

                    String[] productTrainingArray = onBoardExperiencearray[0].split("=");
                    String productTraining = "";
                    if (productTrainingArray.length > 1) {
                        productTraining = productTrainingArray[1];
                    }

                    String[] needAnalysisArray = onBoardExperiencearray[1].split("=");
                    String needAnalysis = "";
                    if (needAnalysisArray.length > 1) {
                        needAnalysis = needAnalysisArray[1];
                    }

                    String[] handedDetailsArray = onBoardExperiencearray[2].split("=");
                    String handedDetails = "";
                    if (handedDetailsArray.length > 1) {
                        handedDetails = handedDetailsArray[1];
                    }

                    String[] commissionStructureArray = onBoardExperiencearray[3].split("=");
                    String commissionStructure = "";
                    if (commissionStructureArray.length > 1) {
                        commissionStructure = commissionStructureArray[1];
                    }

                    String[] strUsageArray = onBoardExperiencearray[4].split("=");
                    String strUsage = "";
                    if (strUsageArray.length > 1) {
                        strUsage = strUsageArray[1];
                    }

                    edittextProductTraining.setText(productTraining);
                    edittextNeedAnalysis.setText(needAnalysis);
                    edittextHandedDetails.setText(handedDetails);
                    edittextCommissionStructure.setText(commissionStructure);
                    edittextstrUsage.setText(strUsage);
                }

                if (!TextUtils.isEmpty(smartadvisor_feature_reqd)) {
                    editTextAdvsiorFeatureRequired.setText(smartadvisor_feature_reqd);
                }

                String suggest_sbil = parseXML.parseXmlTag(LMSurveyXml, "suggest_sbil");
                if (!TextUtils.isEmpty(suggest_sbil)) {
                    editTextIncreaseProductvityNotes.setText(suggest_sbil);
                }


                String increase_brand_loyalty = parseXML.parseXmlTag(LMSurveyXml, "increase_brand_loyalty");
                if (!TextUtils.isEmpty(increase_brand_loyalty)) {
                    editTextIncreaseLoyaltyNotes.setText(increase_brand_loyalty);
                }

                String branch_suggestion = parseXML.parseXmlTag(LMSurveyXml, "branch_suggestion");
                if (!TextUtils.isEmpty(branch_suggestion)) {
                    editTextLowSatisfactionScale.setText(branch_suggestion);
                }

                String branch_satisfaction = parseXML.parseXmlTag(LMSurveyXml, "branch_satisfaction");
                if (!TextUtils.isEmpty(branch_satisfaction)) {

                    if (branch_satisfaction.equalsIgnoreCase("1")) {
                        radioGroupSatisfactionScale.check(R.id.radioButtonSatisfactionScaleOne);
                    } else if (branch_satisfaction.equalsIgnoreCase("2")) {
                        radioGroupSatisfactionScale.check(R.id.radioButtonSatisfactionScaleTwo);
                    } else if (branch_satisfaction.equalsIgnoreCase("3")) {
                        radioGroupSatisfactionScale.check(R.id.radioButtonSatisfactionScaleThree);
                    } else if (branch_satisfaction.equalsIgnoreCase("4")) {
                        radioGroupSatisfactionScale.check(R.id.radioButtonSatisfactionScaleFour);
                    } else if (branch_satisfaction.equalsIgnoreCase("5")) {
                        radioGroupSatisfactionScale.check(R.id.radioButtonSatisfactionScaleFive);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        WorkDetailsSBILAdapter.notifyDataSetChanged();
        AssociationSBILAdapter.notifyDataSetChanged();
        EducationalDetailsAdapter.notifyDataSetChanged();
        ProductsAwareAdapter.notifyDataSetChanged();
        ProductsSellAdapter.notifyDataSetChanged();
        CustomerMeetCountAdapter.notifyDataSetChanged();
        TotalTrainingDaysAdapter.notifyDataSetChanged();
        SupportStatusAdapter.notifyDataSetChanged();
        IncentiveTimeAdapter.notifyDataSetChanged();
        SOSPolicyAdapter.notifyDataSetChanged();
        PaisaGenieStausAdapter.notifyDataSetChanged();
    }

    private String fetchData(String buttonType) {
        int selectedLanguageId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButtonLanguage = findViewById(selectedLanguageId);

        String languageChosen = "";
        if (radioButtonLanguage != null) {
            languageChosen = radioButtonLanguage.getText() + "";
        }
        String productTraining = edittextProductTraining.getText().toString();
        String needAnalysis = edittextNeedAnalysis.getText().toString();
        String handedDetails = edittextHandedDetails.getText().toString();
        String commissionStructure = edittextCommissionStructure.getText().toString();
        String strUsage = edittextstrUsage.getText().toString();


        String advisorFeatureKnown = "";
        if (CBproposalStatusEnq.isChecked()) {

            if (languageChosen.equalsIgnoreCase("Hindi")) {
                advisorFeatureKnown = strProposalStatusEnqHindi + ",";
            } else {
                advisorFeatureKnown = "Proposal status enquiry,";
            }
        } else {
            advisorFeatureKnown = advisorFeatureKnown + " ,";
        }

        if (CBRenewalDueList.isChecked()) {

            if (languageChosen.equalsIgnoreCase("Hindi")) {
                advisorFeatureKnown = advisorFeatureKnown + strRenewalDueListHindi + ",";
            } else {
                advisorFeatureKnown = advisorFeatureKnown + "Renewal due list,";
            }
        } else {
            advisorFeatureKnown = advisorFeatureKnown + " ,";
        }

        if (CBProductDetails.isChecked()) {

            if (languageChosen.equalsIgnoreCase("Hindi")) {
                advisorFeatureKnown = advisorFeatureKnown + strProductDetailsHindi + ",";
            } else {
                advisorFeatureKnown = advisorFeatureKnown + "Product details,";
            }
        } else {
            advisorFeatureKnown = advisorFeatureKnown + " ,";
        }

        if (CBPremiumCalculator.isChecked()) {

            if (languageChosen.equalsIgnoreCase("Hindi")) {
                advisorFeatureKnown = advisorFeatureKnown + strPremiumCalcHindi + ",";
            } else {
                advisorFeatureKnown = advisorFeatureKnown + "Premium calculator,";
            }
        } else {
            advisorFeatureKnown = advisorFeatureKnown + " ,";
        }

        if (CBMTDYTD.isChecked()) {
            if (languageChosen.equalsIgnoreCase("Hindi")) {
                advisorFeatureKnown = advisorFeatureKnown + strMISMTDYTDHindi + ",";
            } else {
                advisorFeatureKnown = advisorFeatureKnown + "MIS about MTD and YTD Business,";
            }

        } else {
            advisorFeatureKnown = advisorFeatureKnown + " ,";
        }

        if (CBNewsUpdates.isChecked()) {

            if (languageChosen.equalsIgnoreCase("Hindi")) {
                advisorFeatureKnown = advisorFeatureKnown + strSBINEwsHindi;
            } else {
                advisorFeatureKnown = advisorFeatureKnown + "News and updates on SBI Life";
            }
        } else {
            advisorFeatureKnown = advisorFeatureKnown + " ,";
        }

       /* if (advisorFeatureKnown.length() > 1) {
            int advisorLastIndex = advisorFeatureKnown.length() - 1;
            String featurLastChar = (advisorFeatureKnown.charAt(advisorLastIndex)) + "";
            if (featurLastChar.equals(",")) {
                advisorFeatureKnown = advisorFeatureKnown.substring(0, advisorLastIndex);
            }
        }*/

        String clubBenefitsAware = "";


        if (CBOnetimedirectcredit.isChecked()) {

            if (languageChosen.equalsIgnoreCase("Hindi")) {
                clubBenefitsAware = strOneTimeDirectCreditHindi + ",";
            } else {
                clubBenefitsAware = "Onetime direct credit,";
            }
        } else {
            clubBenefitsAware = clubBenefitsAware + " ,";
        }

        if (CBInsuranceCover.isChecked()) {

            if (languageChosen.equalsIgnoreCase("Hindi")) {
                clubBenefitsAware = clubBenefitsAware + strLifeINsurancePersonalAccCoverHindi + ",";
            } else {
                clubBenefitsAware = clubBenefitsAware + "Life insurance and personal accident cover,";
            }
        } else {
            clubBenefitsAware = clubBenefitsAware + " ,";
        }

        if (CBVisitingCards.isChecked()) {

            if (languageChosen.equalsIgnoreCase("Hindi")) {
                clubBenefitsAware = clubBenefitsAware + strVisitCardHindi + ",";
            } else {
                clubBenefitsAware = clubBenefitsAware + "Visiting cards &amp; stationary,";
            }
        } else {
            clubBenefitsAware = clubBenefitsAware + " ,";
        }

        if (CBGroupmedicalcover.isChecked()) {

            if (languageChosen.equalsIgnoreCase("Hindi")) {
                clubBenefitsAware = clubBenefitsAware + strGroupMedicalCoverHindi + ",";
            } else {
                clubBenefitsAware = clubBenefitsAware + "Group medical cover,";
            }
        } else {
            clubBenefitsAware = clubBenefitsAware + " ,";
        }


        if (CBAllowances.isChecked()) {
            if (languageChosen.equalsIgnoreCase("Hindi")) {
                clubBenefitsAware = clubBenefitsAware + strMaintAllowancesHindi + ",";
            } else {
                clubBenefitsAware = clubBenefitsAware + "Monthly maintenance allowance &amp; telephone allowance for selected club members,";
            }
        } else {
            clubBenefitsAware = clubBenefitsAware + " ,";
        }

        if (CBOpportunity.isChecked()) {
            if (languageChosen.equalsIgnoreCase("Hindi")) {
                clubBenefitsAware = clubBenefitsAware + strOppParConvHindi + ",";
            } else {
                clubBenefitsAware = clubBenefitsAware + "Opportunity to participate in a convention";
            }


        } else {
            clubBenefitsAware = clubBenefitsAware + " ,";
        }

        /*if (clubBenefitsAware.length() > 1) {
            int lastIndex = clubBenefitsAware.length() - 1;
            String lastChar = (clubBenefitsAware.charAt(lastIndex)) + "";
            if (lastChar.equals(",")) {
                clubBenefitsAware = clubBenefitsAware.substring(0, lastIndex);
            }
        }*/


        String productSellDetails = "";

        if (CBMutualFund.isChecked()) {

            if (languageChosen.equalsIgnoreCase("Hindi")) {
                productSellDetails = strMutualFundHindi + ",";
            } else {
                productSellDetails = getResources().getString(R.string.strMutualFund) + ",";
            }

        } else {
            productSellDetails = " ,";
        }

        if (CBPostOfficePlans.isChecked()) {
            if (languageChosen.equalsIgnoreCase("Hindi")) {
                productSellDetails = productSellDetails + strPostOfficePlansHindi + ",";
            } else {
                productSellDetails = productSellDetails + getResources().getString(R.string.strPostOfficePlans) + ",";
            }

        } else {
            productSellDetails = productSellDetails + " ,";
        }

        if (CBHealthInsurance.isChecked()) {
            if (languageChosen.equalsIgnoreCase("Hindi")) {
                productSellDetails = productSellDetails + strHealthInsuranceHindi + ",";
            } else {
                productSellDetails = productSellDetails + getResources().getString(R.string.strHealthInsurance) + ",";
            }

        } else {
            productSellDetails = productSellDetails + " ,";
        }

        if (CBGeneralInsurance.isChecked()) {

            if (languageChosen.equalsIgnoreCase("Hindi")) {
                productSellDetails = productSellDetails + strGeneralInsuranceHindi + ",";
            } else {
                productSellDetails = productSellDetails + getResources().getString(R.string.strGeneralInsurance) + ",";
            }
        } else {
            productSellDetails = productSellDetails + " ,";
        }


        if (CBNo.isChecked()) {

            if (languageChosen.equalsIgnoreCase("Hindi")) {
                productSellDetails = productSellDetails + strNOHindi + ",";
            } else {
                productSellDetails = productSellDetails + getResources().getString(R.string.strNO) + ",";
            }
        } else {
            productSellDetails = productSellDetails + " ,";
        }

        /*if (productSellDetails.length() > 1) {
            int lastIndex = productSellDetails.length() - 1;
            String lastChar = (productSellDetails.charAt(lastIndex)) + "";
            if (lastChar.equals(",")) {
                productSellDetails = productSellDetails.substring(0, lastIndex);
            }
        }*/
        String sbilAssociation = spinnerAssociationSBIL.getSelectedItem().toString();
        String educationDetails = spinnerEducationalDetails.getSelectedItem().toString();
        String workDetails = spinnerWorkDetails.getSelectedItem().toString();
        String partTimeOccup = edittextPartimeOccupation.getText().toString();


        if (TextUtils.isEmpty(productTraining)) {
            productTraining = getResources().getString(R.string.strProductTraining) + "=,";
        } else {
            productTraining = getResources().getString(R.string.strProductTraining) + "=" + productTraining + ",";
        }

        if (TextUtils.isEmpty(needAnalysis)) {
            needAnalysis = getResources().getString(R.string.strNeedAnalysis) + "=,";
        } else {
            needAnalysis = getResources().getString(R.string.strNeedAnalysis) + "=" + needAnalysis + ",";
        }

        if (TextUtils.isEmpty(handedDetails)) {
            handedDetails = getResources().getString(R.string.strHandedDetails) + "=,";
        } else {
            handedDetails = getResources().getString(R.string.strHandedDetails) + "=" + handedDetails + ",";
        }

        if (TextUtils.isEmpty(commissionStructure)) {
            commissionStructure = getResources().getString(R.string.strCommissionStructure) + "=,";
        } else {
            commissionStructure = getResources().getString(R.string.strCommissionStructure) + "=" + commissionStructure + ",";
        }

        if (TextUtils.isEmpty(strUsage)) {
            strUsage = getResources().getString(R.string.strUsage) + "=,";
        } else {
            strUsage = getResources().getString(R.string.strUsage) + "=" + strUsage + ",";
        }
        String onBoardExperience = productTraining + needAnalysis + handedDetails + commissionStructure + strUsage;


        /*if (onBoardExperience.length() > 1) {
            int lastIndex = onBoardExperience.length() - 1;
            String lastChar = (onBoardExperience.charAt(lastIndex)) + "";
            if (lastChar.equals(",")) {
                onBoardExperience = onBoardExperience.substring(0, lastIndex);
            }
        }*/


        String productsAware = spinnerSBILProductsAware.getSelectedItem().toString();
        String productsSold = spinnerSBILProductsSell.getSelectedItem().toString();

        String custMeetCount = spinnerCustomerMeetCount.getSelectedItem().toString();
        String totalTrainingDays = spinnerTotalTrainingDays.getSelectedItem().toString();

        List<String> lmBiggestStrengthOrderedList = adapterLMBiggestStrength.getTotalItemList();
        String SBILStrength = Arrays.toString(lmBiggestStrengthOrderedList.toArray());//LM Strength

        List<String> lmBiggestAttractionOrderedList = adapterLM_biggest_attraction.getTotalItemList();
        String SBILAttracttion = Arrays.toString(lmBiggestAttractionOrderedList.toArray()); //LM Attraction


        List<String> lmProductivityOrderedList = adapterLMProductivity.getTotalItemList();
        String productivity = Arrays.toString(lmProductivityOrderedList.toArray());//LM Producitivty


        String supportStatus = spinnerSupportStatus.getSelectedItem().toString();
        String incentiveTime = spinnerIncentiveTime.getSelectedItem().toString();
        String SOSPolicy = spinnerSOSPolicy.getSelectedItem().toString();
        String PaisaGenieStaus = spinnerPaisaGenieStaus.getSelectedItem().toString();


        String AdvsiorFeatureRequired = editTextAdvsiorFeatureRequired.getText().toString();


        int selectedId = radioGroupSatisfactionScale.getCheckedRadioButtonId();

        String satisfactionScale = "";
        RadioButton radioButton = findViewById(selectedId);
        if (radioButton != null) {
            satisfactionScale = radioButton.getText() + "";
        }


        String lowSatisfactionScale = editTextLowSatisfactionScale.getText().toString();
        String increaseProductvityNotes = editTextIncreaseProductvityNotes.getText().toString();
        String increaseLoyaltyNotes = editTextIncreaseLoyaltyNotes.getText().toString();


        LMSurvey = new StringBuilder();
        LMSurvey.append("<?xml version='1.0' encoding='utf-8' ?><lm_survey>");

        LMSurvey.append("<emp_id>" + commonMethods.GetUserCode(context) + "</emp_id>");
        LMSurvey.append("<sbil_association>" + sbilAssociation + "</sbil_association>");
        LMSurvey.append("<educational_details>" + educationDetails + "</educational_details>");
        LMSurvey.append("<sbil_work_time>" + workDetails + "</sbil_work_time>");
        LMSurvey.append("<part_time_occu>" + partTimeOccup + "</part_time_occu>");
        LMSurvey.append("<any_other_comp_prod>" + productSellDetails + "</any_other_comp_prod>");
        LMSurvey.append("<onboard_experience>" + onBoardExperience + "</onboard_experience>");
        LMSurvey.append("<product_aware_count>" + productsAware + "</product_aware_count>");
        LMSurvey.append("<product_sell_count>" + productsSold + "</product_sell_count>");
        LMSurvey.append("<customer_meet_count>" + custMeetCount + "</customer_meet_count>");
        LMSurvey.append("<training_attend_days>" + totalTrainingDays + "</training_attend_days>");
        LMSurvey.append("<cust_view_on_sbil_strength>" + SBILStrength + "</cust_view_on_sbil_strength>");
        LMSurvey.append("<lm_career_view_sbil>" + SBILAttracttion + "</lm_career_view_sbil>");
        LMSurvey.append("<training_required>" + productivity + "</training_required>");
        LMSurvey.append("<smartadvisor_feature_known>" + advisorFeatureKnown + "</smartadvisor_feature_known>");
        LMSurvey.append("<smartadvisor_feature_reqd>" + AdvsiorFeatureRequired + "</smartadvisor_feature_reqd>");
        LMSurvey.append("<support_thru_leads>" + supportStatus + "</support_thru_leads>");
        LMSurvey.append("<branch_satisfaction>" + satisfactionScale + "</branch_satisfaction>");
        LMSurvey.append("<branch_suggestion>" + lowSatisfactionScale + "</branch_suggestion>");
        LMSurvey.append("<benefits_club_member>" + clubBenefitsAware + "</benefits_club_member>");
        LMSurvey.append("<is_incentive_delay>" + incentiveTime + "</is_incentive_delay>");
        LMSurvey.append("<is_aware_SQS_policy>" + SOSPolicy + "</is_aware_SQS_policy>");
        LMSurvey.append("<know_PaisGenie>" + PaisaGenieStaus + "</know_PaisGenie>");
        LMSurvey.append("<suggest_sbil>" + increaseProductvityNotes + "</suggest_sbil>");
        LMSurvey.append("<increase_brand_loyalty>" + increaseLoyaltyNotes + "</increase_brand_loyalty>");

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdp = new SimpleDateFormat("MM-dd-yyyy");
        String str_created_date = sdp.format(new Date(cal.getTimeInMillis()));
        LMSurvey.append("<created_date>" + str_created_date + "</created_date>");//created_date

        LMSurvey.append("<languageChosen>" + languageChosen + "" + "</languageChosen>");
        LMSurvey.append("</lm_survey>");

        if (buttonType.equalsIgnoreCase("submit")) {
            service = new ServiceHits(context, METHOD_NAME, "LMSurvey," + strCIFBDMUserId,//LMSurvey.toString(),
                    strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMMObileNo,
                    strCIFBDMPassword, this);
            service.execute();
        }

        return LMSurvey.toString();
    }


}


