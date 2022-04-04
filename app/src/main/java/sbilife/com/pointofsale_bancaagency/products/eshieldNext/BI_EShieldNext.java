package sbilife.com.pointofsale_bancaagency.products.eshieldNext;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sbilife.com.pointofsale_bancaagency.CaptureSignature;
import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.NA_CBI_bean;
import sbilife.com.pointofsale_bancaagency.NeedAnalysisBIService;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.ProposerCaptureSignature;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.annuityplus.success;
import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.ProductInfo;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.needanalysis.NeedAnalysisActivity;
import sbilife.com.pointofsale_bancaagency.new_bussiness.ProductBIBean;

public class BI_EShieldNext extends AppCompatActivity {

    private int DIALOG_ID;
    private CommonForAllProd obj, cfap;

    private ParseXML prsObj;
    private Spinner Spinner_DistributonChannel, Spinner_LANameTitle, Spinner_LAState, Spinner_Plan,
            Spinner_WholeLifeOtherThanWholeLife, Spinner_PremiumPaymentOption, Spinner_PolicyTerm,
            Spinner_PremiumPaymentTerm, Spinner_PremiumFrequency, Spinner_DeathBenefitsPaymentOption,
            Spinner_BetterHalfBenefits, spnr_bi_eShield_next_spouse_title,
            spnr_EShieldnext_TermForADBRider, spnr_EShieldnext_TermForATPDBRider, spinner_age_proof,
            spinner_qualification, selGender, spinnerSpouseGender;

    private CheckBox CheckBox_StaffDiscount, CheckBox_AccidentalDeathBenefitRider,
            CheckBox_AccidentalTotalAndPermanentDisabilityBenefitRider, cb_kerladisc;
    private EditText EditText_LAFirstName, EditText_LAMiddleName, EditText_LALastName, EditText_LAAge,
            EditText_SumAssured, EditText_AgeOfSpouse, EditText_TermForADBRider,
            EditText_ADBRiderSumAssured, EditText_ATPDBRiderSumAssured, edittext_EShieldnext_AgeOfLA,
            edt_bi_eShield_next_first_spouse_name, edt_bi_eShield_next_spouse_middle_name,
            edt_bi_eShield_next_spouse_last_name, edittext_EShieldnext_AgeOfSpouse, edt_proposerdetail_basicdetail_contact_no,
            edt_proposerdetail_basicdetail_Email_id,
            edt_proposerdetail_basicdetail_ConfirmEmail_id;

    private String String_DistributionChannel = "Corporate Agent";
    private String String_StaffDiscount = "False";
    private String String_Smoker = "";
    private String String_Plan = "";
    private String String_WholeLifeOtherThanWholeLife = "";
    private String String_Gender = "";
    private String String_PremiumPaymentOption = "";
    private String String_PolicyTerm = "";
    private String String_PremiumPaymentTerm = "0";
    private String String_PremiumPaymentTerm_display = "";
    private String String_PremiumFrequency = "";
    private String String_SumAssured = "";
    private String String_DeathBenefitsPaymentOption = "";
    private String String_BetterHalfBenefits = "";
    private String String_AccidentalDeathBenefitRider = "";
    private String String_TermForADBRider = "";
    private String String_ADBRiderSumAssured = "";
    private String String_AccidentalTotalAndPermanentDisabilityBenefitRider = "";
    private String String_TermForATPDBRider = "";
    private String String_ATPDBRiderSumAssured = "";
    private String mobileNo = "";
    private String emailId = "";
    private String ConfirmEmailId = "";
    private String ProposerEmailId = "";
    private String flg_needAnalyis = "";
    private String STR_MAT_VALUE_PROD_CHANGE = "";
    private String STR_MAT_VALUE_PROD_QUT_NO = "";
    private String ProposalMode = "";
    private String proposer_Is_Same_As_Life_Assured = "Y";
    private String lifeAssured_Title = "";
    private String lifeAssured_First_Name = "";
    private String lifeAssured_Middle_Name = "";
    private String lifeAssured_Last_Name = "";
    private String name_of_life_assured = "";
    private String spouse_Title = "";
    private String spouse_First_Name = "";
    private String spouse_Middle_Name = "";
    private String spouse_Last_Name = "";
    private String name_of_spouse = "";
    private String spouse_date_of_birth = "";
    private String latestImage = "";
    private String name_of_proposer = "";
    private String name_of_person = "";
    private String place1 = "";
    private String place2 = "";
    private String date1 = "";
    private String date2 = "";
    private String agent_sign = "";
    private String proposer_sign = "";
    private String lifeAssured_date_of_birth = "";
    private String proposer_date_of_birth = "";
    private String String_Smoker_Spouse = "";
    private String String_Gender_spouse = "";
    private String ProposerAge = "0";
    private String SpouseAge = "0";
    private String basicpremium1 = "";
    private String BasicServicetax = "";
    private String SBC = "";
    private String KKC = "";
    private String PremiumwithST = "";
    private String ServiceTax = "";
    private String levelbasicpremium = "";
    private String increasebasicpremium = "";
    private String PA_Premium_LC = "";
    private String PA_Premium_IC = "";
    private String PA_Premium_ADB = "";
    private String PA_Premium_APPDB = "";
    private String Sumassured = "";
    private String BetterHalfBenefitPrem = "";
    private String InstallmentPremiumChannelDiscount = "";
    private String InstallmentpremiumwithStaffDiscount = "";
    private String basicpremium = "";
    private String BetterHalfBenefitPremium = "";
    private String adbpremium = "";
    private String atpdbpremium = "";
    private String totalpremiumwithoutST = "";
    private String YearlyInstPremWithTaxes = "";
    private String BetterHalfBenefitSum = "";
    private String InstallmentPremiumWithStaffDiscount = "";
    private String agentcode;
    private String agentMobile;
    private String agentEmail;
    private String userType;
    private String na_input = null;
    private String na_output = null;
    private String gender;
    private String bankUserType = "";
    private String product_Code;
    private String product_UIN;
    private String product_cateogory;
    private String product_type;
    private String Check = "";
    private String QuatationNumber;
    private String planName = "";
    private String staffStatus = "";
    private String staffRebate = "";
    private String String_spouse_qualification = "";
    private String String_spouse_age_proof = "";
    private String transactionMode = "";
    private TextView btn_bi_eShield_next_life_assured_date, btn_bi_eShield_next_spouse_date;
    private Button back, btnSubmit;

    private ImageView imageview_EShieldnext_Smoker_spouse, imageview_EShieldnext_NonSmoker_spouse,
            ImageViewSmoker, ImageViewNonSmoker;

    private TableRow tr_adb_term, tr_adb_SA, tr_atpdb_term, tr_atpdb_SA, tr_premium_frequency, tr_ppt;
    private Bitmap photoBitmap;
    private StringBuilder inputVal, retVal, bussIll;
    private DatabaseHelper db;
    private eshieldNextBean shieldNextBean;
    private DecimalFormat currencyFormat;

    private Double sumOfRiders;
    private eshieldNextBusinessLogic eshieldNextBusinessLogic;
    private boolean validationFla1 = false;
    private int String_BHB_PolicyTerm = 0;
    private int payoutMonth = 0;

    private int mYear, mMonth, mDay;
    private Context context;
    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;
    private NeedAnalysisBIService NABIObj;
    private NA_CBI_bean na_cbi_bean;
    private File needAnalysispath, newFile, mypath;

    private LinearLayout llSpouseDetails;
    private ImageButton Ibtn_signatureofMarketing, Ibtn_signatureofPolicyHolders, imageButtonProposerPhotograph;
    private int needAnalysis_flag = 0;
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay(DIALOG_ID);
        }
    };

    public String getDate(String OldDate) {
        String NewDate = "";
        try {
            DateFormat userDateFormat = new SimpleDateFormat("MM-dd-yyyy");
            DateFormat dateFormatNeeded = new SimpleDateFormat("dd-MM-yyyy");
            Date date = userDateFormat.parse(OldDate);

            NewDate = dateFormatNeeded.format(date);

        } catch (Exception e) {
            Log.e("Integrated Service", "Error in Getting Date");
        }

        return NewDate;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_bi_eshieldnext);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        initialiseDate();
        initControls();

        db = new DatabaseHelper(this);
        prsObj = new ParseXML();
        retVal = new StringBuilder();
        currencyFormat = new DecimalFormat("##,##,##,###");
        Intent intent = getIntent();

        context = this;
        mStorageUtils = new StorageUtils();
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, getString(R.string.app_name));

        NABIObj = new NeedAnalysisBIService(this);

        //Added Tushar Kadam Kerla Applicable Taxes 7-Jun-2019
        /*TableRow tablerowKerlaDiscount = findViewById(R.id.tablerowKerlaDiscount);
        cb_kerladisc = findViewById(R.id.cb_kerladisc);
        commonMethods.setKerlaDiscount(context, tablerowKerlaDiscount, cb_kerladisc);*/
        //End Added Tushar Kadam Kerla Applicable Taxes 7-Jun-2019

        String na_flag = intent.getStringExtra("NAFlag");


        if (na_flag != null) {
            if (na_flag.equalsIgnoreCase("1")) {
                needAnalysis_flag = 1;
                na_input = intent.getStringExtra("NaInput");
                na_output = intent.getStringExtra("NaOutput");
                String na_dob = intent.getStringExtra("custDOB");
                gender = intent.getStringExtra("custGender");
                bankUserType = intent.getStringExtra("Other");

                if (bankUserType != null) {

                } else {
                    bankUserType = "";
                }
                try {
                    agentcode = SimpleCrypto.decrypt("SBIL",
                            db.GetUserCode());

                    agentMobile = SimpleCrypto.decrypt("SBIL",
                            db.GetMobileNo());
                    agentEmail = SimpleCrypto.decrypt("SBIL",
                            db.GetEmailId());
                    userType = SimpleCrypto.decrypt("SBIL",
                            db.GetUserType());

                    planName = "eShield Next";
                    /* parivartan changes */
                    ProductInfo prodInfoObj = new ProductInfo(context);
                    product_Code = prodInfoObj.getProductCode(planName);
                    product_UIN = prodInfoObj.getProductUIN(planName);
                    product_cateogory = prodInfoObj.getProductCategory(planName);
                    product_type = prodInfoObj.getProductType(planName);
                    /* end */
                    System.out.println("product_Code = " + product_Code);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int k = 12 - (agentcode).length();
                StringBuilder zero = new StringBuilder();
                for (int i = 0; i < k; i++) {
                    zero = zero.append("0");
                }
                QuatationNumber = CommonForAllProd.getquotationNumber30(
                        product_Code, agentcode, zero + "");

            }
        } else
            needAnalysis_flag = 0;


        STR_MAT_VALUE_PROD_CHANGE = intent.getStringExtra("MAT_VALUE_PROD_CHANGE");
        STR_MAT_VALUE_PROD_QUT_NO = intent.getStringExtra("MAT_VALUE_PROD_QUT_NO");
        ProposalMode = intent.getStringExtra("ProposalMode");
        if (STR_MAT_VALUE_PROD_CHANGE == null) {
            STR_MAT_VALUE_PROD_CHANGE = "";
        }
        if (STR_MAT_VALUE_PROD_QUT_NO == null) {
            STR_MAT_VALUE_PROD_QUT_NO = "";
        }
        if (ProposalMode == null) {
            ProposalMode = "";
        }
        obj = new CommonForAllProd();


//        if (str_agent_bank_name != null && str_agent_bank_name.equalsIgnoreCase("YESBANK")) {
//            isYesBank = "TRUE";
//            TextView tv_staff_discount = (TextView) findViewById(R.id.tv_staff_discount);
//            tv_staff_discount.setText("SBI Staff Discount");
//        } else {
//            edt_proposerdetail_basicdetail_contact_no.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
//        }


        populateSpinners();
        setSpinnerListeners();
        otherListeners();

    }

    public void otherListeners() {
        CheckBox_AccidentalDeathBenefitRider.setOnCheckedChangeListener((compoundButton, b) -> {

            if (CheckBox_AccidentalDeathBenefitRider.isChecked()) {
                String_AccidentalDeathBenefitRider = "True";
                tr_adb_term.setVisibility(View.VISIBLE);
                tr_adb_SA.setVisibility(View.VISIBLE);

            } else {
                String_AccidentalDeathBenefitRider = "False";
                tr_adb_term.setVisibility(View.GONE);
                tr_adb_SA.setVisibility(View.GONE);
            }

        });

        CheckBox_AccidentalTotalAndPermanentDisabilityBenefitRider.setOnCheckedChangeListener((compoundButton, b) -> {

            if (CheckBox_AccidentalTotalAndPermanentDisabilityBenefitRider.isChecked()) {
                String_AccidentalTotalAndPermanentDisabilityBenefitRider = "True";
                tr_atpdb_term.setVisibility(View.VISIBLE);
                tr_atpdb_SA.setVisibility(View.VISIBLE);
            } else {
                String_AccidentalTotalAndPermanentDisabilityBenefitRider = "False";
                tr_atpdb_term.setVisibility(View.GONE);
                tr_atpdb_SA.setVisibility(View.GONE);
            }
        });

        CheckBox_StaffDiscount.setOnCheckedChangeListener((compoundButton, b) -> {
            if (CheckBox_StaffDiscount.isChecked()) {
                String_StaffDiscount = "True";
            } else {
                String_StaffDiscount = "False";
            }
        });

        edt_proposerdetail_basicdetail_contact_no
                .addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1,
                                                  int arg2, int arg3) {
                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {

                        String abc = edt_proposerdetail_basicdetail_contact_no
                                .getText().toString();
                        mobile_validation(abc);

                    }
                });

        edt_proposerdetail_basicdetail_Email_id
                .addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {


                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1,
                                                  int arg2, int arg3) {


                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {

                        ProposerEmailId = edt_proposerdetail_basicdetail_Email_id
                                .getText().toString();
                        //email_id_validation(ProposerEmailId);

                    }
                });

        edt_proposerdetail_basicdetail_ConfirmEmail_id
                .addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {


                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1,
                                                  int arg2, int arg3) {


                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {

                        String proposer_confirm_emailId = edt_proposerdetail_basicdetail_ConfirmEmail_id
                                .getText().toString();
                        confirming_email_id(proposer_confirm_emailId);

                    }
                });

/*        edt_proposerdetail_basicdetail_contact_no
                .setOnEditorActionListener(this);
        edt_proposerdetail_basicdetail_Email_id.setOnEditorActionListener(this);
        edt_proposerdetail_basicdetail_ConfirmEmail_id
                .setOnEditorActionListener(this);*/

        // Go Home Button
        back.setOnClickListener(arg0 -> {
            setResult(RESULT_OK);
            finish();
        });

        // Submit Button
        btnSubmit.setOnClickListener(arg0 -> {
            // TODO Auto-generated method stu

            inputVal = new StringBuilder();
            retVal = new StringBuilder();
            bussIll = new StringBuilder();


            lifeAssured_First_Name = EditText_LAFirstName
                    .getText().toString().trim();
            lifeAssured_Middle_Name = EditText_LAMiddleName
                    .getText().toString().trim();
            lifeAssured_Last_Name = EditText_LALastName
                    .getText().toString().trim();

            name_of_life_assured = lifeAssured_Title + " "
                    + lifeAssured_First_Name + " "
                    + lifeAssured_Middle_Name + " " + lifeAssured_Last_Name;

            gender = selGender.getSelectedItem().toString();
            String_Gender = selGender.getSelectedItem().toString();

            spouse_First_Name = edt_bi_eShield_next_first_spouse_name
                    .getText().toString().trim();
            spouse_Middle_Name = edt_bi_eShield_next_spouse_middle_name
                    .getText().toString().trim();
            spouse_Last_Name = edt_bi_eShield_next_spouse_last_name
                    .getText().toString().trim();

            name_of_spouse = spouse_Title + " "
                    + spouse_First_Name + " "
                    + spouse_Middle_Name + " " + spouse_Last_Name;

            String_Gender_spouse = spinnerSpouseGender.getSelectedItem().toString();

            mobileNo = edt_proposerdetail_basicdetail_contact_no.getText()
                    .toString();
            emailId = edt_proposerdetail_basicdetail_Email_id.getText()
                    .toString();
            ConfirmEmailId = edt_proposerdetail_basicdetail_ConfirmEmail_id
                    .getText().toString();

            Date();


            if (valLifeAssuredProposerDetail() && valDob() && valPlanDetail() && valAge() && valTerm()
                    && valMaturityAge() && valSpouseDetail()
                    && valBasicDetail() && valTermRider()
                    && valSA()) {

                if (String_BetterHalfBenefits.equalsIgnoreCase("Yes")) {
                    String_BHB_PolicyTerm = ((60 - Integer.parseInt(SpouseAge)) > Integer.parseInt(String_PolicyTerm) ? Integer.parseInt(String_PolicyTerm) : (60 - Integer.parseInt(SpouseAge)));
                }
                //  && valminPremiumValueAndRider()) {
                addListenerOnSubmit();
                getInput(shieldNextBean);
                if (CalculatePrem(String_StaffDiscount, String_Plan, String_PremiumPaymentOption, String_WholeLifeOtherThanWholeLife,
                        String_Smoker, ProposerAge, String_Gender, String_PolicyTerm, String_PremiumPaymentTerm,
                        String_PremiumFrequency, String_SumAssured, String_BetterHalfBenefits, SpouseAge,
                        String_Smoker_Spouse, String_Gender_spouse, String_DistributionChannel, String_AccidentalDeathBenefitRider,
                        String_TermForADBRider, String_ADBRiderSumAssured, String_AccidentalTotalAndPermanentDisabilityBenefitRider,
                        String_TermForATPDBRider, String_ATPDBRiderSumAssured, "False")) {
                   /* if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
                        proposer_Title = "";
                        proposer_First_Name = "";
                        proposer_Middle_Name = "";
                        proposer_Last_Name = "";
                        name_of_proposer = "";
                        proposer_date_of_birth = "";
                    }*/

                    if (needAnalysis_flag == 0) {
                        Intent i = new Intent(context, success.class);

                        String basicPrem = ((prsObj.parseXmlTag(retVal.toString(),
                                "basicpremium") == null) || (prsObj
                                .parseXmlTag(retVal.toString(), "basicpremium")
                                .equals(""))) ? "0" : prsObj
                                .parseXmlTag(retVal.toString(), "basicpremium");
                        i.putExtra("op", "Basic Premium is Rs." + currencyFormat.format(Double.parseDouble(basicPrem)));

                        String installmntPrem = obj
                                .getRound(obj.getStringWithout_E(Double
                                        .parseDouble(((prsObj.parseXmlTag(retVal.toString(),
                                                "totalpremiumwithoutST") == null) || (prsObj
                                                .parseXmlTag(retVal.toString(), "totalpremiumwithoutST")
                                                .equals(""))) ? "0" : prsObj
                                                .parseXmlTag(retVal.toString(), "totalpremiumwithoutST"))));
                        i.putExtra("op1", "Installment Premium is Rs." + currencyFormat.format(Double.
                                parseDouble(installmntPrem)));



                        if (CheckBox_StaffDiscount.isChecked()) {
                            String installmntPremWithSD = obj.getRound(obj.getStringWithout_E(Double
                                    .parseDouble(((prsObj.parseXmlTag(retVal.toString(),
                                            "InstallmentPremiumWithStaffDiscount") == null) || (prsObj
                                            .parseXmlTag(retVal.toString(), "InstallmentPremiumWithStaffDiscount")
                                            .equals(""))) ? "0" : prsObj
                                            .parseXmlTag(retVal.toString(), "InstallmentPremiumWithStaffDiscount"))));

                            i.putExtra("op2", "Installment Premium with Staff Discount is Rs."
                                    + currencyFormat.format(Double.parseDouble(installmntPremWithSD)));
                        }

                        String installmntPremWithST = obj.getRound(obj.getStringWithout_E(Double
                                .parseDouble(((prsObj.parseXmlTag(retVal.toString(),
                                        "PremiumwithST") == null) || (prsObj
                                        .parseXmlTag(retVal.toString(), "PremiumwithST")
                                        .equals(""))) ? "0" : prsObj
                                        .parseXmlTag(retVal.toString(), "PremiumwithST"))));

                        i.putExtra("op3", "Installment Premium Inclusive Applicable Taxes is Rs."
                                + currencyFormat.format(Double.parseDouble(installmntPremWithST)));


                        if (CheckBox_AccidentalDeathBenefitRider.isChecked()) {
                            String adbRiderPrem = obj.getRound(obj.getStringWithout_E(Double
                                    .parseDouble(((prsObj.parseXmlTag(retVal.toString(),
                                            "adbpremium") == null) || (prsObj
                                            .parseXmlTag(retVal.toString(), "adbpremium")
                                            .equals(""))) ? "0" : prsObj
                                            .parseXmlTag(retVal.toString(), "adbpremium"))));
                            i.putExtra("op5", "Accidental Death Benefit Rider Premium is Rs."
                                    + currencyFormat.format(Double.parseDouble(adbRiderPrem)));
                        }


                        if (CheckBox_AccidentalTotalAndPermanentDisabilityBenefitRider.isChecked()) {
                            String atpdbRiderPrem = obj.getRound(obj.getStringWithout_E(Double
                                    .parseDouble(((prsObj.parseXmlTag(retVal.toString(),
                                            "atpdbpremium") == null) || (prsObj
                                            .parseXmlTag(retVal.toString(), "atpdbpremium")
                                            .equals(""))) ? "0" : prsObj
                                            .parseXmlTag(retVal.toString(), "atpdbpremium"))));
                            i.putExtra("op6", "Accidental Total & Permanent Disability Benefit Rider Premium is Rs."
                                    + currencyFormat.format(Double.parseDouble(atpdbRiderPrem)));
                        }

                        i.putExtra("ProductDescName", "SBI Life - " + getString(R.string.sbi_life_eshield_next));
                        i.putExtra("productUINName", "(UIN:" + getString(R.string.sbi_life_eshield_next_uin) + ")");

                        context.startActivity(i);
                    } else {
                        Dialog();
                    }

                }

            }
        });

    }

    public void setSpinnerListeners() {
        Spinner_WholeLifeOtherThanWholeLife.setOnItemSelectedListener
                (
                        new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position > 0) {
                                    String_WholeLifeOtherThanWholeLife = Spinner_WholeLifeOtherThanWholeLife.getSelectedItem().toString();
                                    if (String_WholeLifeOtherThanWholeLife.equalsIgnoreCase("Whole Life")) {
                                        String_PremiumPaymentOption = "";
                                        List<String> Array_PremiumPaymentOption = new ArrayList<>();
                                        Array_PremiumPaymentOption.add("Select");
                                        Array_PremiumPaymentOption.add("LPPT");
                                        ArrayAdapter<String> Adapter_PremiumPaymentOption = new ArrayAdapter<>(
                                                BI_EShieldNext.this, android.R.layout.simple_spinner_item, Array_PremiumPaymentOption);
                                        Adapter_PremiumPaymentOption.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        Spinner_PremiumPaymentOption.setAdapter(Adapter_PremiumPaymentOption);

                                    } else if (String_Plan.equalsIgnoreCase("Level Cover with Future Proofing Benefits")) {
                                        String_PremiumPaymentOption = "";
                                        List<String> Array_PremiumPaymentOption = new ArrayList<>();
                                        Array_PremiumPaymentOption.add("Select");
                                        Array_PremiumPaymentOption.add("Regular");
                                        ArrayAdapter<String> Adapter_PremiumPaymentOption = new ArrayAdapter<>(
                                                BI_EShieldNext.this, android.R.layout.simple_spinner_item, Array_PremiumPaymentOption);
                                        Adapter_PremiumPaymentOption.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        Spinner_PremiumPaymentOption.setAdapter(Adapter_PremiumPaymentOption);
                                    } else {
                                        String_PremiumPaymentOption = "";
                                        List<String> Array_PremiumPaymentOption = new ArrayList<>();
                                        Array_PremiumPaymentOption.add("Select");
                                        Array_PremiumPaymentOption.add("Single");
                                        Array_PremiumPaymentOption.add("Regular");
                                        Array_PremiumPaymentOption.add("LPPT");
                                        ArrayAdapter<String> Adapter_PremiumPaymentOption = new ArrayAdapter<>(
                                                BI_EShieldNext.this, android.R.layout.simple_spinner_item, Array_PremiumPaymentOption);
                                        Adapter_PremiumPaymentOption.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        Spinner_PremiumPaymentOption.setAdapter(Adapter_PremiumPaymentOption);

                                    }


                                } else {
                                    String_WholeLifeOtherThanWholeLife = "";
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        }
                );


        Spinner_PremiumPaymentOption.setOnItemSelectedListener
                (
                        new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position > 0) {
                                    String_PremiumPaymentOption = Spinner_PremiumPaymentOption.getSelectedItem().toString();
                                    if (String_PremiumPaymentOption.equalsIgnoreCase("Single")) {
                                        String_PremiumFrequency = "Single";
                                        tr_premium_frequency.setVisibility(View.GONE);
                                    } else {
                                        String_PremiumFrequency = "";
                                        Spinner_PremiumFrequency.setSelection(0, false);
                                        tr_premium_frequency.setVisibility(View.VISIBLE);
                                    }
                                    SetPolicyTerm(String_PremiumPaymentOption, String_WholeLifeOtherThanWholeLife, ProposerAge);
                                    if (String_PremiumPaymentOption.equalsIgnoreCase("LPPT")) {
                                        tr_ppt.setVisibility(View.VISIBLE);
                                    } else {
                                        tr_ppt.setVisibility(View.GONE);
                                    }

                                    // SetPPT(String_PremiumPaymentOption, String_WholeLifeOtherThanWholeLife);

                                } else {
                                    String_PremiumPaymentOption = "";
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        }
                );


        Spinner_Plan.setOnItemSelectedListener
                (
                        new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position > 0) {
                                    String_Plan = Spinner_Plan.getSelectedItem().toString();
                                    if (String_Plan.equalsIgnoreCase("Level Cover with Future Proofing Benefits")) {
                                        List<String> Array_WholeLifeOrOther = new ArrayList<>();
                                        Array_WholeLifeOrOther.add("Select");
                                        Array_WholeLifeOrOther.add("Other Than Whole Life");

                                        ArrayAdapter<String> Adapter_WholeLifeOrOther = new ArrayAdapter<>(
                                                BI_EShieldNext.this, android.R.layout.simple_spinner_item, Array_WholeLifeOrOther);
                                        Adapter_WholeLifeOrOther.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        Spinner_WholeLifeOtherThanWholeLife.setAdapter(Adapter_WholeLifeOrOther);
                                    } else {
                                        List<String> Array_WholeLifeOrOther = new ArrayList<>();
                                        Array_WholeLifeOrOther.add("Select");
                                        Array_WholeLifeOrOther.add("Whole Life");
                                        Array_WholeLifeOrOther.add("Other Than Whole Life");
                                        ArrayAdapter<String> Adapter_WholeLifeOrOther = new ArrayAdapter<>(
                                                BI_EShieldNext.this, android.R.layout.simple_spinner_item, Array_WholeLifeOrOther);
                                        Adapter_WholeLifeOrOther.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        Spinner_WholeLifeOtherThanWholeLife.setAdapter(Adapter_WholeLifeOrOther);
                                    }

                                } else {
                                    String_Plan = "";
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        }
                );


        Spinner_PremiumFrequency.setOnItemSelectedListener
                (
                        new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position > 0) {
                                    String_PremiumFrequency = Spinner_PremiumFrequency.getSelectedItem().toString();
                                } else {
                                    String_PremiumFrequency = "";
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        }
                );


        /*Spinner_PremiumPaymentTerm.setOnItemSelectedListener
                (
                        new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position > 0) {
                                    String_PremiumPaymentTerm = Spinner_PremiumPaymentTerm.getSelectedItem().toString();
                                    String_PremiumPaymentTerm = String_PremiumPaymentTerm;
                                    if (String_PremiumPaymentTerm.equalsIgnoreCase("PT less 5")) {
                                        String_PremiumPaymentTerm = String.valueOf(Integer.parseInt(Spinner_PolicyTerm.getSelectedItem().toString()) - 5);
                                    }

                                } else {
                                    String_PremiumPaymentTerm = "";
                                    String_PremiumPaymentTerm="";
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        }
                );*/


        spinner_age_proof.setOnItemSelectedListener
                (
                        new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position > 0) {

                                    String_spouse_age_proof =
                                            spinner_age_proof.getSelectedItem().toString();
                                } else {
                                    String_spouse_age_proof = "";
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        }
                );


        spinner_qualification.setOnItemSelectedListener
                (
                        new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position > 0) {

                                    String_spouse_qualification =
                                            spinner_qualification.getSelectedItem().toString();
                                } else {
                                    String_spouse_qualification = "";
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        }
                );


        Spinner_DeathBenefitsPaymentOption.setOnItemSelectedListener
                (
                        new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position > 0) {
                                    String_DeathBenefitsPaymentOption = Spinner_DeathBenefitsPaymentOption.getSelectedItem().toString();
                                } else {
                                    String_DeathBenefitsPaymentOption = "";
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        }
                );

        Spinner_PolicyTerm.setOnItemSelectedListener
                (
                        new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String_PolicyTerm = Spinner_PolicyTerm.getSelectedItem().toString();
                                SetPPT(String_PremiumPaymentOption, String_WholeLifeOtherThanWholeLife, String_PolicyTerm);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        }
                );


        Spinner_BetterHalfBenefits.setOnItemSelectedListener
                (
                        new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                if (position > 0) {
                                    String_BetterHalfBenefits = Spinner_BetterHalfBenefits.getSelectedItem().toString();

                                    if (String_BetterHalfBenefits.equalsIgnoreCase("Yes")) {
                                        llSpouseDetails.setVisibility(View.VISIBLE);
                                    } else if (String_BetterHalfBenefits.equalsIgnoreCase("No")) {
                                        llSpouseDetails.setVisibility(View.GONE);
                                    }
                                } else {
                                    String_BetterHalfBenefits = "";
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        }
                );


        Spinner_LANameTitle
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {

                        if (position > 0) {
                            lifeAssured_Title = Spinner_LANameTitle
                                    .getSelectedItem().toString();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {


                    }
                });


        spnr_bi_eShield_next_spouse_title
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {

                        if (position > 0) {
                            spouse_Title = spnr_bi_eShield_next_spouse_title
                                    .getSelectedItem().toString();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });


    }

    public void initControls() {
        back = findViewById(R.id.back);
        btnSubmit = findViewById(R.id.btnSubmit);
        Spinner_DistributonChannel = findViewById(R.id.spinner_EShieldnext_DistributionChannel);
        CheckBox_StaffDiscount = findViewById(R.id.checkbox_EShieldnext_StaffDiscount);
        Spinner_LANameTitle = findViewById(R.id.spinner_EShieldnext_LATitle);
        EditText_LAFirstName = findViewById(R.id.edittext_EShieldnext_LAFirstName);
        EditText_LAMiddleName = findViewById(R.id.edittext_EShieldnext_LAMiddleName);
        EditText_LALastName = findViewById(R.id.edittext_EShieldnext_LALastName);
        Spinner_LAState = findViewById(R.id.spinner_EShieldnext_State);
        ImageViewSmoker = findViewById(R.id.imageview_EShieldnext_Smoker);
        ImageViewNonSmoker = findViewById(R.id.imageview_EShieldnext_NonSmoker);
        Spinner_Plan = findViewById(R.id.spinner_EShieldnext_Plan);
        Spinner_WholeLifeOtherThanWholeLife = findViewById(R.id.spinner_EShieldnext_WholeLifeOrOther);
        EditText_LAAge = findViewById(R.id.edittext_EShieldnext_AgeOfLA);
        Spinner_PremiumPaymentOption = findViewById(R.id.spinner_EShieldnext_PremiumPaymentOption);
        Spinner_PolicyTerm = findViewById(R.id.spinner_EShieldnext_PolicyTerm);
        Spinner_PremiumPaymentTerm = findViewById(R.id.spinner_EShieldnext_PremiumPaymentTerm);
        Spinner_PremiumFrequency = findViewById(R.id.spinner_EShieldnext_PremiumFrequency);
        EditText_SumAssured = findViewById(R.id.edittext_EShieldnext_SumAssured);
        Spinner_DeathBenefitsPaymentOption = findViewById(R.id.spinner_EShieldnext_DeathBenefitsPaymentOption);
        Spinner_BetterHalfBenefits = findViewById(R.id.spinner_EShieldnext_BetterHalfBenefits);
        EditText_AgeOfSpouse = findViewById(R.id.edittext_EShieldnext_AgeOfSpouse);
        CheckBox_AccidentalDeathBenefitRider = findViewById(R.id.checkbox_EShieldnext_AccidentalDeathBenefitRider);
        EditText_ADBRiderSumAssured = findViewById(R.id.edittext_EShieldnext_ADBRiderSumAssured);
        CheckBox_AccidentalTotalAndPermanentDisabilityBenefitRider = findViewById(R.id.checkbox_EShieldnext_PermanantDisabilityBenefitRider);
        EditText_ATPDBRiderSumAssured = findViewById(R.id.edittext_EShieldnext_ATPDBRiderSumAssured);


        spnr_EShieldnext_TermForADBRider = findViewById(R.id.spnr_EShieldnext_TermForADBRider);
        spnr_EShieldnext_TermForATPDBRider = findViewById(R.id.spnr_EShieldnext_TermForATPDBRider);


        btn_bi_eShield_next_life_assured_date = findViewById(R.id.btn_bi_eShield_next_life_assured_date);
        btn_bi_eShield_next_spouse_date = findViewById(R.id.btn_bi_eShield_next_spouse_date);
        edittext_EShieldnext_AgeOfLA = findViewById(R.id.edittext_EShieldnext_AgeOfLA);
        edt_bi_eShield_next_first_spouse_name = findViewById(R.id.edt_bi_eShield_next_first_spouse_name);
        edt_bi_eShield_next_spouse_middle_name = findViewById(R.id.edt_bi_eShield_next_spouse_middle_name);
        edt_bi_eShield_next_spouse_last_name = findViewById(R.id.edt_bi_eShield_next_spouse_last_name);
        edittext_EShieldnext_AgeOfSpouse = findViewById(R.id.edittext_EShieldnext_AgeOfSpouse);
        spnr_bi_eShield_next_spouse_title = findViewById(R.id.spnr_bi_eShield_next_spouse_title);

        selGender = findViewById(R.id.selGender);
        spinnerSpouseGender = findViewById(R.id.spinnerSpouseGender);

        imageview_EShieldnext_Smoker_spouse = findViewById(R.id.imageview_EShieldnext_Smoker_spouse);
        imageview_EShieldnext_NonSmoker_spouse = findViewById(R.id.imageview_EShieldnext_NonSmoker_spouse);

        tr_adb_term = findViewById(R.id.tr_adb_term);
        tr_adb_SA = findViewById(R.id.tr_adb_SA);
        tr_atpdb_term = findViewById(R.id.tr_atpdb_term);
        tr_atpdb_SA = findViewById(R.id.tr_atpdb_SA);


        edt_proposerdetail_basicdetail_contact_no = findViewById(R.id.edt_eshieldnext_contact_no);
        edt_proposerdetail_basicdetail_Email_id = findViewById(R.id.edt_eshieldnext_Email_id);
        edt_proposerdetail_basicdetail_ConfirmEmail_id = findViewById(R.id.edt_eshieldnext_ConfirmEmail_id);

        tr_premium_frequency = findViewById(R.id.tr_premium_frequency);


        llSpouseDetails = findViewById(R.id.llSpouseDetails);


        llSpouseDetails.setVisibility(View.GONE);

        tr_ppt = findViewById(R.id.tr_ppt);


        spinner_age_proof = findViewById(R.id.spinner_age_proof);
        spinner_qualification = findViewById(R.id.spinner_qualification);
    }

    public void getValues() {

        String TAG = "EShieldValues";

        eshieldNextBean nextBean = new eshieldNextBean();

        String_DistributionChannel = Spinner_DistributonChannel.getSelectedItem().toString();
        nextBean.setChannel(String_DistributionChannel);
        if (CheckBox_StaffDiscount.isChecked()) {
            String_StaffDiscount = "True";
        } else {
            String_StaffDiscount = "False";
        }

//        nextBean.setStaff(Boolean.parseBoolean(String_StaffDiscount));

        Log.d(TAG, "Staff Discount " + String_StaffDiscount);

        String string_LANameTitle = Spinner_LANameTitle.getSelectedItem().toString();

        Log.d(TAG, "Name Title " + string_LANameTitle);
        String string_LAFirstName = EditText_LAFirstName.getText().toString().trim();
        Log.d(TAG, "First Name " + string_LAFirstName);

        String string_LAMiddleName = EditText_LAMiddleName.getText().toString().trim();
        Log.d(TAG, "Middle Name " + string_LAMiddleName);
        String string_LALastName = EditText_LALastName.getText().toString().trim();
        Log.d(TAG, "Last Name " + string_LALastName);
        String string_LAState = Spinner_LAState.getSelectedItem().toString();
//        nextBean.setState( Boolean.parseBoolean( String_LAState));
        Log.d(TAG, "State " + string_LAState);
        String_Plan = Spinner_Plan.getSelectedItem().toString();
//        nextBean.setPlanoption(String_Plan);
        Log.d(TAG, "Plan " + String_Plan);
        String_WholeLifeOtherThanWholeLife = Spinner_WholeLifeOtherThanWholeLife.getSelectedItem().toString();
//        nextBean.setWholeLifeoption(String_WholeLifeOtherThanWholeLife);
//        String_WholeLifeOtherThanWholeLife = "unset";
        Log.d(TAG, "Whole life other than whole life " + String_WholeLifeOtherThanWholeLife);
        String string_LAAge = EditText_LAAge.getText().toString().trim();
//        nextBean.setAge( Integer.parseInt( String_LAAge));
        Log.d(TAG, "Age " + string_LAAge);
        String_PremiumPaymentOption = Spinner_PremiumPaymentOption.getSelectedItem().toString();
//        nextBean.setPremiumPayoption(String_PremiumPaymentOption);
        Log.d(TAG, "Premium Payment option " + String_PremiumPaymentOption);
        String_PolicyTerm = Spinner_PolicyTerm.getSelectedItem().toString();
//        nextBean.setPolicyterm(Integer.parseInt(String_PolicyTerm));
        Log.d(TAG, "Policy term " + String_PolicyTerm);
        String_PremiumPaymentTerm = "0";

        String_PremiumFrequency = "";


        if (!Spinner_PremiumPaymentOption.getSelectedItem().toString().equals("Select")) {
            String_PremiumPaymentTerm = Spinner_PremiumPaymentTerm.getSelectedItem().toString();
            Log.d(TAG, "Premium payment term " + String_PremiumPaymentTerm);
            String_PremiumFrequency = Spinner_PremiumFrequency.getSelectedItem().toString();
            Log.d(TAG, "Premium Frequency " + String_PremiumFrequency);
        }

//        nextBean.setPPT(Integer.parseInt(String_PremiumPaymentTerm));

//        nextBean.setPremiumFrequency(String_PremiumFrequency);


        String_SumAssured = EditText_SumAssured.getText().toString().trim();
//        nextBean.setSumassured( Integer.parseInt( String_SumAssured));
        Log.d(TAG, "Sum Assured " + String_SumAssured);
        String_DeathBenefitsPaymentOption = Spinner_DeathBenefitsPaymentOption.getSelectedItem().toString();
//        nextBean.set
        Log.d(TAG, "Death Benefit Payment Option " + String_DeathBenefitsPaymentOption);
        String_BetterHalfBenefits = Spinner_BetterHalfBenefits.getSelectedItem().toString();
//        nextBean.setBetterHalfBenifit( Boolean.parseBoolean( String_BetterHalfBenefits));
        Log.d(TAG, "Better half benefit " + String_BetterHalfBenefits);
//        String_BetterHalfBenefits = "unset";
        String string_AgeOfSpouse = EditText_AgeOfSpouse.getText().toString().trim();
//        nextBean.setBetterHalfBenifitAge(Integer.parseInt(String_AgeOfSpouse));
        Log.d(TAG, "Age of Spouse " + string_AgeOfSpouse);

//        String_AccidentalDeathBenefitRider = "unset";

        if (CheckBox_AccidentalDeathBenefitRider.isChecked()) {
            String_AccidentalDeathBenefitRider = "Eligible";
        } else {
            String_AccidentalDeathBenefitRider = "Not Eligible";
        }


        Log.d(TAG, "Accidental Death Benefit Rider " + String_AccidentalDeathBenefitRider);

        String_TermForADBRider = EditText_TermForADBRider.getText().toString().trim();
        Log.d(TAG, "Term for ADB Rider " + String_TermForADBRider);
//        nextBean.setPolicyTerm_ADB(Integer.parseInt(String_TermForADBRider));
        String_ADBRiderSumAssured = EditText_ADBRiderSumAssured.getText().toString().trim();
        Log.d(TAG, "ADB Rider sum assured " + String_ADBRiderSumAssured);
//        nextBean.setSumAssured_ADB( Integer.parseInt( String_ADBRiderSumAssured));

//        String_AccidentalTotalAndPermanentDisabilityBenefitRider = "unset";

        if (CheckBox_AccidentalTotalAndPermanentDisabilityBenefitRider.isChecked()) {
            String_AccidentalTotalAndPermanentDisabilityBenefitRider = "Eligible";
        }
        {
            String_AccidentalTotalAndPermanentDisabilityBenefitRider = "Not Eligible";
        }

        Log.d(TAG, "Accidental total and premanent disability benefit rider " + String_AccidentalTotalAndPermanentDisabilityBenefitRider);

        String_TermForATPDBRider = spnr_EShieldnext_TermForADBRider.getSelectedItem().toString();

//        nextBean.setPolicyTerm_ATPDB(Integer.parseInt(String_TermForATPDBRider));

        Log.d(TAG, "Term for ATPDB Rider " + String_TermForATPDBRider);
        String_ATPDBRiderSumAssured = EditText_ATPDBRiderSumAssured.getText().toString().trim();
//        nextBean.setSumAssured_ATPDB(Integer.parseInt(String_ATPDBRiderSumAssured));
        Log.d(TAG, "ATPDB Rider Sum Assured " + String_ATPDBRiderSumAssured);


    }

    public void populateSpinners() {

        List<String> Array_DistributionChannel = new ArrayList<>();
        Array_DistributionChannel.add("Online");
        Array_DistributionChannel.add("Web aggregator");
        Array_DistributionChannel.add("Broker");
        Array_DistributionChannel.add("Corporate Agent");
        Array_DistributionChannel.add("Individual Agent");
        Array_DistributionChannel.add("Direct Marketing");
        Array_DistributionChannel.add("Insurance Marketing Firms (IMFs)");

        ArrayAdapter<String> Adapter_Distribution = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Array_DistributionChannel);
        Adapter_Distribution.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_DistributonChannel.setAdapter(Adapter_Distribution);

        commonMethods.fillSpinnerValue(context, Spinner_LANameTitle,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        commonMethods.fillSpinnerValue(context, spnr_bi_eShield_next_spouse_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        //Gender
        String[] genderList = {"Male", "Female", "Third Gender"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, genderList);
        genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
        selGender.setAdapter(genderAdapter);
        spinnerSpouseGender.setAdapter(genderAdapter);
        genderAdapter.notifyDataSetChanged();

        List<String> ls_AgeProof = new ArrayList<>();
        ls_AgeProof.add("Select Document");
        ls_AgeProof.add("AADHAAR Card No");
        ls_AgeProof.add("AADHAAR VID No");
        ls_AgeProof.add("AADHAAR eKYC ref. No");
        ls_AgeProof.add("Birth Certificate");
        ls_AgeProof.add("School/College Certificate");
        ls_AgeProof.add("Passport");
        ls_AgeProof.add("Service Extract (PSU)");
        ls_AgeProof.add("Identity Card (Defence)");
        ls_AgeProof.add("Driving License");
        ls_AgeProof.add("Pancard");
        ls_AgeProof.add("Voter s Identity Card");

        ArrayAdapter<String> Adapter_AgeProof = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, ls_AgeProof);
        Adapter_AgeProof.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_age_proof.setAdapter(Adapter_AgeProof);


        List<String> ls_Qualification = new ArrayList<>();
        ls_Qualification.add("Select Qualification");
        ls_Qualification.add("Diploma (after 10th Std/ITI)");
        // ls_Qualification.add("Diploma (after 12th Std)");
        ls_Qualification.add("Literate/Non-SSC");
        ls_Qualification.add("10th Pass/SSC");
        ls_Qualification.add("12th Pass/HSC");
        ls_Qualification.add("Post Graduate/Professional");
        ls_Qualification.add("Graduate");

        ArrayAdapter<String> Adapter_Qualification = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, ls_Qualification);
        Adapter_Qualification.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_qualification.setAdapter(Adapter_Qualification);


        List<String> Array_StateAndUTs = new ArrayList<>();
        Array_StateAndUTs.add("Select");
        Array_StateAndUTs.add("Andra Pradesh");
        Array_StateAndUTs.add("Arunachal Pradesh");
        Array_StateAndUTs.add("Assam");
        Array_StateAndUTs.add("Bihar");
        Array_StateAndUTs.add("Chhattisgarh");
        Array_StateAndUTs.add("Goa");
        Array_StateAndUTs.add("Gujarat");
        Array_StateAndUTs.add("Haryana");
        Array_StateAndUTs.add("Himachal Pradesh");
        Array_StateAndUTs.add("Jharkhand");
        Array_StateAndUTs.add("Karnataka");
        Array_StateAndUTs.add("Kerala");
        Array_StateAndUTs.add("Madhya Pradesh");
        Array_StateAndUTs.add("Maharashtra");
        Array_StateAndUTs.add("Manipur");
        Array_StateAndUTs.add("Meghalaya");
        Array_StateAndUTs.add("Mizoram");
        Array_StateAndUTs.add("Nagaland");
        Array_StateAndUTs.add("Odisha");
        Array_StateAndUTs.add("Punjab");
        Array_StateAndUTs.add("Rajasthan");
        Array_StateAndUTs.add("Sikkim");
        Array_StateAndUTs.add("Tamil Nadu");
        Array_StateAndUTs.add("Telangana");
        Array_StateAndUTs.add("Tripura");
        Array_StateAndUTs.add("Uttar Pradesh");
        Array_StateAndUTs.add("Uttarakhand");
        Array_StateAndUTs.add("West Bengal");
        Array_StateAndUTs.add("Andaman and Nicobar Islands");
        Array_StateAndUTs.add("Chandigarh");
        Array_StateAndUTs.add("Dadra abd Nagar Haveli");
        Array_StateAndUTs.add("Daman And Diu");
        Array_StateAndUTs.add("Delhi");
        Array_StateAndUTs.add("Jammu and Kashmir");
        Array_StateAndUTs.add("Ladakh");
        Array_StateAndUTs.add("Lakshadweep");
        Array_StateAndUTs.add("Puducherry");

        ArrayAdapter<String> Adapter_StateAndUTs = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Array_StateAndUTs);
        Adapter_StateAndUTs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_LAState.setAdapter(Adapter_StateAndUTs);


        List<String> Array_Plan = new ArrayList<>();
        Array_Plan.add("Select");
        Array_Plan.add("Level Cover");
        Array_Plan.add("Increasing Cover");
        Array_Plan.add("Level Cover with Future Proofing Benefits");

        ArrayAdapter<String> Adapter_Plan = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Array_Plan);
        Adapter_Plan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_Plan.setAdapter(Adapter_Plan);


      /*  List<String> Array_PremiumPaymentOption = new ArrayList<String>();
        Array_PremiumPaymentOption.add("Select");
        Array_PremiumPaymentOption.add("Single");
        Array_PremiumPaymentOption.add("Regular");
        Array_PremiumPaymentOption.add("LPPT");


        ArrayAdapter<String> Adapter_PremiumPaymentOption = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, Array_PremiumPaymentOption);
        Adapter_PremiumPaymentOption.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_PremiumPaymentOption.setAdapter(Adapter_PremiumPaymentOption);*/


//        List<String> Array_PolicyTerm =  new ArrayList<String>();
//        Array_PolicyTerm.add("Select");
//        Array_PolicyTerm.add("1");
//        Array_PolicyTerm.add("2");
//        Array_PolicyTerm.add("3");
//        Array_PolicyTerm.add("4");
//        Array_PolicyTerm.add("5");
//        Array_PolicyTerm.add("6");
//        Array_PolicyTerm.add("7");
//        Array_PolicyTerm.add("8");
//        Array_PolicyTerm.add("9");
//        Array_PolicyTerm.add("10");
//        Array_PolicyTerm.add("11");
//        Array_PolicyTerm.add("12");
//        Array_PolicyTerm.add("13");
//        Array_PolicyTerm.add("14");
//        Array_PolicyTerm.add("15");
//        Array_PolicyTerm.add("16");
//        Array_PolicyTerm.add("17");
//        Array_PolicyTerm.add("18");
//        Array_PolicyTerm.add("19");
//        Array_PolicyTerm.add("20");
//
//        ArrayAdapter<String> Adapter_PolicyTerm = new ArrayAdapter<String>(
//                this, android.R.layout.simple_spinner_item, Array_PolicyTerm);
//        Adapter_Distribution.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        Spinner_PolicyTerm.setAdapter(Adapter_PolicyTerm);


//
//        List<String> Array_PremiumPaymentTerm_Single =  new ArrayList<String>();
//        List<String> Array_PremiumPaymentTerm_Regular = new ArrayList<String>();
//        List<String> Array_PremiumPaymentTerm_LPPT = new ArrayList<String>();
//
//        Array_PremiumPaymentTerm_Single.add("Select");
//        Array_PremiumPaymentTerm_Single.add("1");
//
//        Array_PremiumPaymentTerm_Regular.add("Select");
//        Array_PremiumPaymentTerm_Regular.add("50");
//
//        Array_PremiumPaymentTerm_Single.add("Select");
//        Array_PremiumPaymentTerm_LPPT.add("5");
//        Array_PremiumPaymentTerm_LPPT.add("7");
//        Array_PremiumPaymentTerm_LPPT.add("10");
//        Array_PremiumPaymentTerm_LPPT.add("15");
//        Array_PremiumPaymentTerm_LPPT.add("20");
//        Array_PremiumPaymentTerm_LPPT.add("25");
//        Array_PremiumPaymentTerm_LPPT.add("25");
//
//        if(Spinner_PremiumPaymentOption.getSelectedItem().toString().equals("Single Premium")){
//            ArrayAdapter<String> Adapter_PremiumPaymentTerm_Single = new ArrayAdapter<String>(
//                    this, android.R.layout.simple_spinner_item, Array_PremiumPaymentTerm_Single);
//            Adapter_Distribution.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            Spinner_PremiumPaymentTerm.setAdapter(Adapter_PremiumPaymentTerm_Single);
//
//        }
//
//        if (Spinner_PremiumPaymentOption.getSelectedItem().toString().equals("Regular Premium")){
//            ArrayAdapter<String> Adapter_PremiumPaymentTerm_Regular = new ArrayAdapter<String>(
//                    this, android.R.layout.simple_spinner_item, Array_PremiumPaymentTerm_Regular);
//            Adapter_Distribution.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            Spinner_PremiumPaymentTerm.setAdapter(Adapter_PremiumPaymentTerm_Regular);
//
//        }
//
//        if(Spinner_PremiumPaymentOption.getSelectedItem().toString().equals("LPPT")){
//            ArrayAdapter<String> Adapter_PremiumPaymentTerm_LPPT = new ArrayAdapter<String>(
//                    this, android.R.layout.simple_spinner_item, Array_PremiumPaymentTerm_LPPT);
//            Adapter_Distribution.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            Spinner_PremiumPaymentTerm.setAdapter(Adapter_PremiumPaymentTerm_LPPT);
//
//        }

//        ArrayAdapter<String> Adapter_PremiumPaymentTerm = new ArrayAdapter<String>(
//                this, android.R.layout.simple_spinner_item, Array_PremiumPaymentTerm);
//        Adapter_Distribution.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        Spinner_PremiumPaymentTerm.setAdapter(Adapter_PremiumPaymentTerm);


//        List<String> Array_PremiumFrequency_Single =  new ArrayList<String>();
//        List<String> Array_PremiumFrequency_RegularLPPT = new ArrayList<String>();
//
//        Array_PremiumFrequency_Single.add("Select");
//        Array_PremiumFrequency_Single.add("Single");
//
//        Array_PremiumFrequency_RegularLPPT.add("Select");
//        Array_PremiumFrequency_RegularLPPT.add("Yearly");
//        Array_PremiumFrequency_RegularLPPT.add("Half Yearly");
//        Array_PremiumFrequency_RegularLPPT.add("Monthly");
//
//        if (Spinner_PremiumPaymentOption.getSelectedItem().toString().equals("Single")){
//            ArrayAdapter<String> Adapter_PremiumFrequency_Single = new ArrayAdapter<String>(
//                    this, android.R.layout.simple_spinner_item, Array_PremiumFrequency_Single);
//            Adapter_Distribution.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            Spinner_PremiumFrequency.setAdapter(Adapter_PremiumFrequency_Single);
//        }
//
//        if (Spinner_PremiumPaymentOption.getSelectedItem().toString().equals("Regular") ||
//                Spinner_PremiumPaymentOption.getSelectedItem().toString().equals("LPPT") ){
//            ArrayAdapter<String> Adapter_PremiumFrequency_RegularLPPT = new ArrayAdapter<String>(
//                    this, android.R.layout.simple_spinner_item, Array_PremiumFrequency_RegularLPPT);
//            Adapter_Distribution.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            Spinner_PremiumFrequency.setAdapter(Adapter_PremiumFrequency_RegularLPPT);
//        }

//        ArrayAdapter<String> Adapter_PremiumFrequency = new ArrayAdapter<String>(
//                this, android.R.layout.simple_spinner_item, Array_PremiumFrequency);
//        Adapter_Distribution.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        Spinner_PremiumFrequency.setAdapter(Adapter_PremiumFrequency);

        List<String> Array_DeathBenefitPaymentMode = new ArrayList<>();
        Array_DeathBenefitPaymentMode.add("Select");
        Array_DeathBenefitPaymentMode.add("Lump sum");
        Array_DeathBenefitPaymentMode.add("Monthly Installments");
        Array_DeathBenefitPaymentMode.add("Lumpsum + Monthly Installments");

        ArrayAdapter<String> Adapter_DeathBenefitPaymentMode = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Array_DeathBenefitPaymentMode);
        Adapter_DeathBenefitPaymentMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_DeathBenefitsPaymentOption.setAdapter(Adapter_DeathBenefitPaymentMode);


        List<String> Array_BetterHalfBenefits = new ArrayList<>();
        Array_BetterHalfBenefits.add("Select");
        Array_BetterHalfBenefits.add("Yes");
        Array_BetterHalfBenefits.add("No");

        ArrayAdapter<String> Adapter_BetterHalfBenefits = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Array_BetterHalfBenefits);
        Adapter_BetterHalfBenefits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_BetterHalfBenefits.setAdapter(Adapter_BetterHalfBenefits);

        List<String> Array_WholeLifeOrOther = new ArrayList<>();
        Array_WholeLifeOrOther.add("Select");
        Array_WholeLifeOrOther.add("Whole Life");
        Array_WholeLifeOrOther.add("Other Than Whole Life");

        ArrayAdapter<String> Adapter_WholeLifeOrOther = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Array_WholeLifeOrOther);
        Adapter_WholeLifeOrOther.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_WholeLifeOtherThanWholeLife.setAdapter(Adapter_WholeLifeOrOther);


        // Policy Term
        String[] policyTermList = new String[78];
        for (int i = 5; i <= 82; i++) {
            policyTermList[i - 5] = i + "";
        }
        ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, policyTermList);
        policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        Spinner_PolicyTerm.setAdapter(policyTermAdapter);
        policyTermAdapter.notifyDataSetChanged();


        // adb Term
        String[] adbTermList = new String[53];
        for (int i = 5; i <= 57; i++) {
            adbTermList[i - 5] = i + "";
        }
        ArrayAdapter<String> adbTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, adbTermList);
        adbTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_EShieldnext_TermForADBRider.setAdapter(adbTermAdapter);
        adbTermAdapter.notifyDataSetChanged();


        // atpdb Term
        String[] atpdbTermList = new String[53];
        for (int i = 5; i <= 57; i++) {
            atpdbTermList[i - 5] = i + "";
        }
        ArrayAdapter<String> atpdbTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, atpdbTermList);
        atpdbTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_EShieldnext_TermForATPDBRider.setAdapter(atpdbTermAdapter);
        atpdbTermAdapter.notifyDataSetChanged();


        List<String> Array_PremiumFrequency_RegularLPPT = new ArrayList<>();
        Array_PremiumFrequency_RegularLPPT.add("Select");
        Array_PremiumFrequency_RegularLPPT.add("Yearly");
        Array_PremiumFrequency_RegularLPPT.add("Half-Yearly");
        Array_PremiumFrequency_RegularLPPT.add("Monthly");
        ArrayAdapter<String> Adapter_PremiumFrequency_RegularLPPT = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, Array_PremiumFrequency_RegularLPPT);
        Adapter_PremiumFrequency_RegularLPPT.setDropDownViewResource(R.layout.spinner_item1);
        Spinner_PremiumFrequency.setAdapter(Adapter_PremiumFrequency_RegularLPPT);
        Adapter_PremiumFrequency_RegularLPPT.notifyDataSetChanged();

    }

    public void onSmoker(View v) {
        String_Smoker = "Smoker";
        ImageViewSmoker.setImageDrawable(getResources().getDrawable(
                R.drawable.smoker_selected));
        ImageViewNonSmoker.setImageDrawable(getResources().getDrawable(
                R.drawable.nonsmoker_nonselected));

        Toast.makeText(this, "Selected " + String_Smoker, Toast.LENGTH_SHORT).show();

    }

    public void onNonSmoker(View v) {
        String_Smoker = "Non Smoker";
        ImageViewSmoker.setImageDrawable(getResources().getDrawable(
                R.drawable.smoker_nonselected));
        ImageViewNonSmoker.setImageDrawable(getResources().getDrawable(
                R.drawable.nonsmoker_selected));

        Toast.makeText(this, "Selected " + String_Smoker, Toast.LENGTH_SHORT).show();

    }

    public void onSmoker_Spouse(View v) {
        String_Smoker_Spouse = "Smoker";
        imageview_EShieldnext_Smoker_spouse.setImageDrawable(getResources().getDrawable(
                R.drawable.smoker_selected));
        imageview_EShieldnext_NonSmoker_spouse.setImageDrawable(getResources().getDrawable(
                R.drawable.nonsmoker_nonselected));


    }

    public void onNonSmoker_Spouse(View v) {
        String_Smoker_Spouse = "Non Smoker";
        imageview_EShieldnext_Smoker_spouse.setImageDrawable(getResources().getDrawable(
                R.drawable.smoker_nonselected));
        imageview_EShieldnext_NonSmoker_spouse.setImageDrawable(getResources().getDrawable(
                R.drawable.nonsmoker_selected));


    }

    private void initialiseDate() {
        Calendar calender = Calendar.getInstance();
        mYear = calender.get(Calendar.YEAR);
        mMonth = calender.get(Calendar.MONTH);
        mDay = calender.get(Calendar.DAY_OF_MONTH);

    }


    public void setDefaultDate(int id) {
        Calendar present_date = Calendar.getInstance();
        present_date.add(Calendar.YEAR, -id);
        mDay = present_date.get(Calendar.DAY_OF_MONTH);
        mMonth = present_date.get(Calendar.MONTH);
        mYear = present_date.get(Calendar.YEAR);
    }

    public String getDate_DD_MM_YY(String OldDate) {
        String NewDate = "";
        try {
            DateFormat userDateFormat = new SimpleDateFormat("MM-dd-yyyy");
            DateFormat dateFormatNeeded = new SimpleDateFormat("dd-MM-yyyy");
            Date date = userDateFormat.parse(OldDate);

            NewDate = dateFormatNeeded.format(date);

        } catch (Exception e) {

            Log.e("Integrated Service", "Error in Getting Date");
        }

        return NewDate;
    }

    public String getDate_MM_dd_yy(String OldDate) {
        String NewDate = "";
        try {
            DateFormat userDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            DateFormat dateFormatNeeded = new SimpleDateFormat("MM-dd-yyyy");
            Date date = userDateFormat.parse(OldDate);

            NewDate = dateFormatNeeded.format(date);

        } catch (Exception e) {

            Log.e("Integrated Service", "Error in Getting Date");
        }

        return NewDate;
    }

    public String getformatedThousandString(int number) {
        return NumberFormat.getNumberInstance(Locale.US)
                .format(number);
    }

    public String getCurrentDate() {
        Calendar present_date = Calendar.getInstance();
        int mDay = present_date.get(Calendar.DAY_OF_MONTH);
        int mMonth = present_date.get(Calendar.MONTH) + 1;
        int mYear = present_date.get(Calendar.YEAR);

        return mDay + "-" + mMonth + "-" + mYear;

    }

    // @Override
    // public void onBackPressed() {
    // 
    // super.onBackPressed();
    // d.dismiss();
    // }



    /* Basic Details Method */

    /**
     * Used To Change date From dd-mm-yyyy to mm-dd-yyyy
     */
    public String getDate1(String OldDate) {
        String NewDate = "";
        try {
            DateFormat userDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            DateFormat dateFormatNeeded = new SimpleDateFormat("MM-dd-yyyy");
            Date date = userDateFormat.parse(OldDate);

            NewDate = dateFormatNeeded.format(date);

        } catch (Exception e) {

            Log.e("Integrated Service", "Error in Getting Date");
        }

        return NewDate;
    }

    private boolean valBasicDetail() {
        if (edt_proposerdetail_basicdetail_contact_no.getText()
                .toString().equals("")) {
            commonMethods.dialogWarning(context, "Please Fill  Mobile No", true);
            edt_proposerdetail_basicdetail_contact_no.requestFocus();
            return false;
        } else if (edt_proposerdetail_basicdetail_contact_no.getText()
                .toString().length() != 10) {
            commonMethods.dialogWarning(context, "Please Fill 10 Digit Mobile No", true);
            edt_proposerdetail_basicdetail_contact_no.requestFocus();
            return false;
        } else if (!emailId.equals("")) {

            email_id_validation(emailId);
            if (validationFla1) {
                if (ConfirmEmailId.equals("")) {
                    commonMethods.dialogWarning(context, "Please Fill Confirm Email Id", true);
                    edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
                    return false;
                } else if (!ConfirmEmailId.equals(emailId)) {
                    commonMethods.dialogWarning(context, "Email Id Does Not Match", true);
                    return false;
                }

                return true;
            } else {
                commonMethods.dialogWarning(context, "Please Fill Valid Email Id", true);
                edt_proposerdetail_basicdetail_Email_id.requestFocus();
                return false;
            }
        } else if (!ConfirmEmailId.equals("")) {

            email_id_validation(ConfirmEmailId);
            if (validationFla1) {

                if (emailId.equals("")) {
                    commonMethods.dialogWarning(context, "Please Fill Email Id", true);
                    edt_proposerdetail_basicdetail_Email_id.requestFocus();
                    return false;

                }

                return true;
            } else {
                commonMethods.dialogWarning(context, "Please Fill Valid Confirm Email Id", true);
                edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
                return false;
            }
        } else {
            return true;
        }

    }

    public void mobile_validation(String number) {
        String isYesBank = "";
        if ((number.length() != 10) && !isYesBank.equalsIgnoreCase("TRUE")) {
            edt_proposerdetail_basicdetail_contact_no
                    .setError("Please provide correct 10-digit mobile number");
        } else if ((number.length() == 10)) {
        }
    }

    public void email_id_validation(String email_id) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email_id);
        if (!(matcher.matches())) {
            edt_proposerdetail_basicdetail_Email_id
                    .setError("Please provide the correct email address");
            validationFla1 = false;
        } else if (!CheckBox_StaffDiscount.isChecked()
                && (email_id.contains("@sbi.co.in")
                || email_id.contains("@sbm.co.in")
                || email_id.contains("@sbbj.co.in")
                || email_id.contains("@sbhyd.co.in")
                || email_id.contains("@sbp.co.in")
                || email_id.contains("@sbt.co.in")
                || email_id.contains("@sbi-life.com") || email_id
                .contains("@sbilife.co.in"))) {
            edt_proposerdetail_basicdetail_Email_id
                    .setError("Please provide the Personal email address");
            validationFla1 = false;
        } else if ((matcher.matches())) {
            validationFla1 = true;
        }
    }

    public void confirming_email_id(String email_id) {

        if (!(email_id.equals(ProposerEmailId))) {
            edt_proposerdetail_basicdetail_ConfirmEmail_id
                    .setError("Email id does not match");
        } else if ((email_id.equals(ProposerEmailId))) {
        }

    }

    private void initialiseDateParameter(String date_value) {
        if (date_value.equals("")) {
            setDefaultDate(35);
        } else {
            String[] array = date_value.split("-");
            mMonth = Integer.parseInt(array[0]) - 1;
            mDay = Integer.parseInt(array[1]);
            mYear = Integer.parseInt(array[2]);
        }
    }


    private void updateDisplay(int id) {

        String mont = (mMonth + 1 < 10 ? "0" : "") + (mMonth + 1);
        String day = (mDay < 10 ? "0" : "") + mDay;

        StringBuilder date = new StringBuilder().append(day).append("-")
                .append(mont).append("-").append(mYear);

        Calendar present_date = Calendar.getInstance();
        int tDay = present_date.get(Calendar.DAY_OF_MONTH);
        int tMonth = present_date.get(Calendar.MONTH);
        int tYear = present_date.get(Calendar.YEAR);

        int age;

        if (tMonth == mMonth) {
            if (tDay >= mDay) {
                age = tYear - mYear;
            } else
                age = tYear - mYear - 1;
        } else if (tMonth > mMonth) {
            age = tYear - mYear;
        } else {
            age = tYear - mYear - 1;
        }

        String final_age = Integer.toString(age);

        if (final_age.contains("-")) {
            commonMethods.dialogWarning(context, "Please fill Valid Date", true);
        } else {
            switch (id) {
                case 1:
                    ProposerAge = final_age;
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.showMessageDialog(context, "Please fill Valid Birth Date");
                    } else {
                        int maxLimit = 0;
                        int minLimit = 0;


                       /* if (Spinner_Plan.getSelectedItem().toString()
                                .equals("Level Cover with Future Proofing Benefits")) {
                            maxLimit = 45;
                            minLimit = 18;

                        }*/
                        //if (String_WholeLifeOtherThanWholeLife.equalsIgnoreCase("")) {
                        if (Spinner_Plan.getSelectedItem().toString()
                                .equals("Level Cover with Future Proofing Benefits")) {
                            minLimit = 18;
                            maxLimit = 45;
                        } else if (Spinner_WholeLifeOtherThanWholeLife.getSelectedItem().toString()
                                .equals("Whole Life")) {
                            minLimit = 45;
                            if (Spinner_PremiumPaymentOption.getSelectedItem().toString()
                                    .equals("Single") || Spinner_PremiumPaymentOption.getSelectedItem().toString()
                                    .equals("LPPT"))
                                maxLimit = 65;
                            else
                                maxLimit = 60;
                        } else if (Spinner_WholeLifeOtherThanWholeLife.getSelectedItem().toString()
                                .equals("Other Than Whole Life")) {
                            minLimit = 18;
                            if (Spinner_PremiumPaymentOption.getSelectedItem().toString()
                                    .equals("Single") || Spinner_PremiumPaymentOption.getSelectedItem().toString()
                                    .equals("LPPT"))
                                maxLimit = 65;
                            else
                                maxLimit = 60;
                        } else
                            maxLimit = 65;


                        if (minLimit <= age && age <= maxLimit) {
                            btn_bi_eShield_next_life_assured_date.setText(date);
                            edittext_EShieldnext_AgeOfLA.setText(final_age);
                            lifeAssured_date_of_birth = getDate1(date + "");
                            SetPolicyTerm(String_PremiumPaymentOption, String_WholeLifeOtherThanWholeLife, ProposerAge);

                        } else {
                            commonMethods.showMessageDialog(context, "Minimum Age should be " + minLimit + " yrs and Maximum Age should be "
                                    + maxLimit + " yrs For LifeAssured");
                            btn_bi_eShield_next_life_assured_date
                                    .setText("Select Date");
                            edittext_EShieldnext_AgeOfLA.setText("");
                            lifeAssured_date_of_birth = "";
                        }
                    }
                    break;

                case 2:

                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.showMessageDialog(context, "Please fill Valid Birth Date");
                    } else {
                        int maxLimit;

                        maxLimit = 55;
                        int AgeDiff = 0;
                        if (Integer.parseInt(ProposerAge) > Integer.parseInt(final_age)) {
                            AgeDiff = (Integer.parseInt(ProposerAge) - Integer.parseInt(final_age));
                        } else {
                            AgeDiff = (Integer.parseInt(final_age) - Integer.parseInt(ProposerAge));
                        }

                        if (AgeDiff > 10) {
                            commonMethods.showMessageDialog(context, "The age difference between life assured and spouse is less than or equal to 10 years");
                            btn_bi_eShield_next_spouse_date
                                    .setText("Select Date");
                            edittext_EShieldnext_AgeOfSpouse.setText("");
                            spouse_date_of_birth = "";
                        } else if (18 <= age && age <= maxLimit) {
                            SpouseAge = final_age;
                            btn_bi_eShield_next_spouse_date.setText(date);
                            edittext_EShieldnext_AgeOfSpouse.setText(final_age);
                            spouse_date_of_birth = getDate1(date + "");


                        } else {
                            commonMethods.showMessageDialog(context, "Minimum Age should be 18 yrs and Maximum Age should be "
                                    + maxLimit + " yrs For Spouse");
                            btn_bi_eShield_next_spouse_date
                                    .setText("Select Date");
                            edittext_EShieldnext_AgeOfSpouse.setText("");
                            spouse_date_of_birth = "";


                        }
                    }
                    break;


                default:
                    break;

            }

        }

    }

    public void onClickLADob(View v) {
        initialiseDateParameter(lifeAssured_date_of_birth);
        DIALOG_ID = 1;
        showDateDialog();
    }

    public void onClickSpouseDob(View v) {
        initialiseDateParameter(spouse_date_of_birth);
        DIALOG_ID = 2;
        showDateDialog();
    }

    private void addListenerOnSubmit() {
        String_SumAssured = EditText_SumAssured.getText().toString().trim();
        SpouseAge = EditText_AgeOfSpouse.getText().toString().trim();
        ProposerAge = edittext_EShieldnext_AgeOfLA.getText().toString().trim();
        String_PolicyTerm = Spinner_PolicyTerm.getSelectedItem().toString();
        String_PremiumPaymentTerm = Spinner_PremiumPaymentTerm.getSelectedItem().toString();
        String_TermForATPDBRider = spnr_EShieldnext_TermForATPDBRider.getSelectedItem().toString();
        String_ATPDBRiderSumAssured = EditText_ATPDBRiderSumAssured.getText().toString().trim();

        String_TermForADBRider = spnr_EShieldnext_TermForADBRider.getSelectedItem().toString();
        String_ADBRiderSumAssured = EditText_ADBRiderSumAssured.getText().toString().trim();


        if (String_PremiumPaymentTerm.equalsIgnoreCase("PT less 5")) {
            String_PremiumPaymentTerm_display = String.valueOf(Integer.parseInt(Spinner_PolicyTerm.getSelectedItem().toString()) - 5);
        } else {
            String_PremiumPaymentTerm_display = String_PremiumPaymentTerm;
        }

        if (SpouseAge.equalsIgnoreCase("")) {
            SpouseAge = "0";
        }
        if (String_TermForATPDBRider.equalsIgnoreCase("")) {
            String_TermForATPDBRider = "0";
        }
        if (String_ATPDBRiderSumAssured.equalsIgnoreCase("")) {
            String_ATPDBRiderSumAssured = "0";
        }
        if (String_TermForADBRider.equalsIgnoreCase("")) {
            String_ADBRiderSumAssured = "0";
        }

        if (String_ADBRiderSumAssured.equalsIgnoreCase("")) {
            String_ADBRiderSumAssured = "0";
        }

        if (String_PremiumPaymentOption.equalsIgnoreCase("Regular")) {
            String_PremiumPaymentTerm = String_PolicyTerm;
        }
        if (String_PremiumPaymentOption.equalsIgnoreCase("Single")) {
            String_PremiumPaymentTerm = "1";
        }
        if (SpouseAge.equalsIgnoreCase("")) {
            SpouseAge = "0";
        }

        shieldNextBean = new eshieldNextBean();

        shieldNextBean.setStaff(CheckBox_StaffDiscount.isChecked());
        shieldNextBean.setSmoker(String_Smoker);
        shieldNextBean.setState(false);
        shieldNextBean.setServiceTax(false);
        shieldNextBean.setAge(Integer.parseInt(ProposerAge));
        shieldNextBean.setGender(String_Gender);
        shieldNextBean.setPolicyterm(Integer.parseInt(String_PolicyTerm));


        if (String_PremiumPaymentTerm.equalsIgnoreCase("PT less 5")) {
            shieldNextBean.setPPT(Integer.parseInt("6"));
        } else {
            shieldNextBean.setPPT(Integer.parseInt(String_PremiumPaymentTerm));
        }
        shieldNextBean.setPremiumFrequency(String_PremiumFrequency);
        shieldNextBean.setPlanoption(String_Plan);
        shieldNextBean.setPremiumPayoption(String_PremiumPaymentOption);
        shieldNextBean.setWholeLifeoption(String_WholeLifeOtherThanWholeLife);
        shieldNextBean.setChannel(String_DistributionChannel);
        shieldNextBean.setSumassured(Double.parseDouble(String_SumAssured));
        shieldNextBean.setUnderwriting("");
        shieldNextBean.setBetterHalfBenifit(Boolean.parseBoolean(String_BetterHalfBenefits));
        shieldNextBean.setBetterHalfBenifitAge(Integer.parseInt(SpouseAge));
        shieldNextBean.setSmoker_spouse(String_Smoker_Spouse);
        shieldNextBean.setGender_spouse(String_Gender_spouse);
        shieldNextBean.setDeathBenefitPaymentOption(String_DeathBenefitsPaymentOption);

        shieldNextBean.setADBRiderStatus(CheckBox_AccidentalDeathBenefitRider.isChecked());


        shieldNextBean.setATPDBRiderStatus(CheckBox_AccidentalTotalAndPermanentDisabilityBenefitRider.isChecked());
        if (CheckBox_AccidentalDeathBenefitRider.isChecked()) {
            shieldNextBean.setSumAssured_ADB(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                    .parseDouble(EditText_ADBRiderSumAssured.getText()
                            .toString())))));

            shieldNextBean.setPolicyTerm_ADB(Integer.parseInt(spnr_EShieldnext_TermForADBRider
                    .getSelectedItem().toString()));
        }


        if (CheckBox_AccidentalTotalAndPermanentDisabilityBenefitRider.isChecked()) {

            shieldNextBean.setSumAssured_ATPDB(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                    .parseDouble(EditText_ATPDBRiderSumAssured.getText()
                            .toString())))));
            shieldNextBean.setPolicyTerm_ATPDB(Integer.parseInt(spnr_EShieldnext_TermForATPDBRider
                    .getSelectedItem().toString()));
        }
    }

    public void getInput(eshieldNextBean eshieldNextBean) {

        inputVal = new StringBuilder();
        // From GUI Input
        boolean staffDisc = eshieldNextBean.getStaff();
        boolean isJKResident = false;
        boolean bancAssuranceDisc = false;
        int policyTerm = eshieldNextBean.getPolicyterm();
        String premFreq = eshieldNextBean.getPremiumFrequency();
        String PaymentOption = eshieldNextBean.getPremiumPayoption();
        double sumAssured = eshieldNextBean.getSumassured();
        String planOption = eshieldNextBean.getPlanoption();
        boolean isADBRider = eshieldNextBean.getADBRiderStatus();
        int adbTerm = eshieldNextBean.getPolicyTerm_ADB();
        double adbSA = eshieldNextBean.getSumAssured_ADB();
        boolean isATPDBRider = eshieldNextBean.getATPDBRiderStatus();
        int atpdbTerm = eshieldNextBean.getPolicyTerm_ATPDB();
        double atpdbSA = eshieldNextBean.getSumAssured_ATPDB();

        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><eShieldNext>");
        inputVal.append("<isStaff>").append(String.valueOf(staffDisc)).append("</isStaff>");
        inputVal.append("<str_kerla_discount>" + "No" + "</str_kerla_discount>");
        inputVal.append("<isJKResident>").append(String.valueOf(isJKResident)).append("</isJKResident>");
        inputVal.append("<isBancAssuranceDisc>").append(String.valueOf(bancAssuranceDisc)).append("</isBancAssuranceDisc>");
        inputVal.append("<age>").append(edittext_EShieldnext_AgeOfLA.getText().toString()).append("</age>");
        inputVal.append("<gender>").append(String_Gender).append("</gender>");

        inputVal.append("<Smoker>").append(String_Smoker).append("</Smoker>");

        inputVal.append("<policyTerm>").append(policyTerm).append("</policyTerm>");

        inputVal.append("<premiumTerm>").append(String_PremiumPaymentTerm).append("</premiumTerm>");

        inputVal.append("<premiumTerm_PT_LESS_5>").append(String_PremiumPaymentTerm).append("</premiumTerm_PT_LESS_5>");
        inputVal.append("<premFreq>").append(premFreq).append("</premFreq>");

        inputVal.append("<sumAssured>").append(sumAssured).append("</sumAssured>");
        inputVal.append("<plan>").append(planOption).append("</plan>");
        inputVal.append("<PaymentOption>").append(PaymentOption).append("</PaymentOption>");

        inputVal.append("<DeathBenefitsOption>").append(String_DeathBenefitsPaymentOption).append("</DeathBenefitsOption>");

        inputVal.append("<BetterHalfBenefits>").append(String_BetterHalfBenefits).append("</BetterHalfBenefits>");
        inputVal.append("<isADBRider>").append(String.valueOf(isADBRider)).append("</isADBRider>");
        inputVal.append("<adbTerm>").append(adbTerm).append("</adbTerm>");
        inputVal.append("<adbSA>").append(adbSA).append("</adbSA>");
        inputVal.append("<isATPDBRider>").append(String.valueOf(isATPDBRider)).append("</isATPDBRider>");
        inputVal.append("<atpdbTerm>").append(atpdbTerm).append("</atpdbTerm>");
        inputVal.append("<atpdbSA>").append(atpdbSA).append("</atpdbSA>");

        inputVal.append("<Product_Cat>").append(product_cateogory).append("</Product_Cat>");
        inputVal.append("<MAT_VALUE_PROD_CHANGE>").append(STR_MAT_VALUE_PROD_CHANGE).append("</MAT_VALUE_PROD_CHANGE>");

        inputVal.append("<MAT_VALUE_PROD_QUT_NO>").append(STR_MAT_VALUE_PROD_QUT_NO).append("</MAT_VALUE_PROD_QUT_NO>");
        inputVal.append("<ProposalMode>").append(ProposalMode).append("</ProposalMode>");

        inputVal.append("<spouse_Title>").append(spouse_Title).append("</spouse_Title>");
        inputVal.append("<spouse_First_Name>").append(spouse_First_Name).append("</spouse_First_Name>");

        inputVal.append("<spouse_Middle_Name>").append(spouse_Middle_Name).append("</spouse_Middle_Name>");

        inputVal.append("<spouse_Last_Name>").append(spouse_Last_Name).append("</spouse_Last_Name>");
        inputVal.append("<name_of_spouse>").append(name_of_spouse).append("</name_of_spouse>");

        inputVal.append("<Gender_Spouse>").append(String_Gender_spouse).append("</Gender_Spouse>");

        inputVal.append("<Smoker_Spouse>").append(String_Smoker_Spouse).append("</Smoker_Spouse>");
        inputVal.append("<dob_Spouse>").append(spouse_date_of_birth).append("</dob_Spouse>");

        inputVal.append("<Age_Spouse>").append(edittext_EShieldnext_AgeOfSpouse.getText().toString()).append("</Age_Spouse>");

        inputVal.append("<WholeLifeOption>").append(String_WholeLifeOtherThanWholeLife).append("</WholeLifeOption>");
        inputVal.append("<BHB_PolicyTerm>").append(String_BHB_PolicyTerm).append("</BHB_PolicyTerm>");

        inputVal.append("<spouse_age_proof>").append(String_spouse_age_proof).append("</spouse_age_proof>");
        inputVal.append("<spouse_qualification>").append(String_spouse_qualification).append("</spouse_qualification>");

        inputVal.append("<product_name>").append(planName).append("</product_name>");
        inputVal.append("<product_Code>").append(product_Code).append("</product_Code>");
        inputVal.append("<product_UIN>").append(product_UIN).append("</product_UIN>");
        inputVal.append("<product_cateogory>").append(product_cateogory).append("</product_cateogory>");
        inputVal.append("<product_type>").append(product_type).append("</product_type>");


        String str_kerla_discount = "N";
        /*if (cb_kerladisc.isChecked()) {
            str_kerla_discount = "Y";
        }*/

        inputVal.append("<KFC>").append(str_kerla_discount).append("</KFC>");
        inputVal.append("</eShieldNext>");

        System.out.println("inputVal.toString() = " + inputVal.toString());
    }

    // Validate Sum Assured
    Boolean valSA() {
        double minRiderLimit, maxRiderLimit;
        StringBuilder error = new StringBuilder();

        if (EditText_SumAssured.getText().toString().equals("")) {
            error.append("Please enter Sum Assured.");
            EditText_SumAssured.requestFocus();
        } else if (Double.parseDouble(EditText_SumAssured.getText().toString()) % 100000 != 0) {
            error.append("Sum assured should be multiple of 1,00,000");
            EditText_SumAssured.requestFocus();
        } else if (Double.parseDouble(EditText_SumAssured.getText().toString()) < 7500000) {
            error.append("Sum assured should be greater than or equal to 75,00,000");
            EditText_SumAssured.requestFocus();

        } else if (Double.parseDouble(EditText_SumAssured.getText().toString()) > 9900000 && String_Smoker.equalsIgnoreCase("Smoker")) {
            error.append("Sum assured should not be greater than 9900000");
            EditText_SumAssured.requestFocus();

        } else {
            if (CheckBox_AccidentalDeathBenefitRider.isChecked()) {
                minRiderLimit = 25000;
                maxRiderLimit = Math.min(5000000,
                        Double.parseDouble(EditText_SumAssured.getText().toString()));

                if (EditText_ADBRiderSumAssured.getText().toString().equals("")
                        || Double.parseDouble(EditText_ADBRiderSumAssured.getText().toString()) < minRiderLimit
                        || Double.parseDouble(EditText_ADBRiderSumAssured.getText().toString()) > maxRiderLimit) {
                    error.append("\nEnter Sum assured for Accidental Death Benefit Rider between ").append(currencyFormat.format(minRiderLimit)).append(" and ").append(currencyFormat.format(maxRiderLimit));
                    EditText_ADBRiderSumAssured.requestFocus();
                }
                if (Double.parseDouble(EditText_ADBRiderSumAssured.getText().toString()) % 1000 != 0) {
                    error.append("Sum assured should be multiple of 1,000");
                    EditText_ADBRiderSumAssured.requestFocus();
                }

            }


            if (CheckBox_AccidentalTotalAndPermanentDisabilityBenefitRider.isChecked()) {
                minRiderLimit = 25000;
                maxRiderLimit = Math.min(5000000,
                        Double.parseDouble(EditText_SumAssured.getText().toString()));
                if (EditText_ATPDBRiderSumAssured.getText().toString().equals("")
                        || Double.parseDouble(EditText_ATPDBRiderSumAssured.getText().toString()) < minRiderLimit
                        || Double.parseDouble(EditText_ATPDBRiderSumAssured.getText().toString()) > maxRiderLimit) {
                    error.append("\nEnter Sum assured for Accidental Total and Permenent Disability Benefit Rider between ").append(currencyFormat.format(minRiderLimit)).append(" and ").append(currencyFormat.format(maxRiderLimit));
                    EditText_ATPDBRiderSumAssured.requestFocus();
                }
                if (Double.parseDouble(EditText_ATPDBRiderSumAssured.getText().toString()) % 1000 != 0) {
                    error.append("Sum assured should be multiple of 1,000");
                    EditText_ATPDBRiderSumAssured.requestFocus();
                }
            }

        }

        if (!error.toString().equals("")) {
            commonMethods.showMessageDialog(context, error.toString());
            return false;
        } else
            return true;
    }

    // Rider term


    //Validate Minimum premium
    private boolean valInstPremium(String premiumSingleInstBasic, String premFreq) {
        String error = "";
        if (premFreq.equals("Single") && (Double.parseDouble(premiumSingleInstBasic) < 19000)) {
            error = "Minimum premium for Yearly Mode under this product is Rs.19000";

        } else if (premFreq.equals("Yearly") && (Double.parseDouble(premiumSingleInstBasic) < 3600)) {
            error = "Minimum premium for Yearly Mode under this product is Rs.3600";

        } else if (premFreq.equals("Half-Yearly") && (Double.parseDouble(premiumSingleInstBasic) < 1836)) {
            error = "Minimum premium for Half Yearly Mode under this product is Rs.1836";
        } else if (premFreq.contains("Monthly") && (Double.parseDouble(premiumSingleInstBasic) < 306)) {
            error = "Minimum premium for Monthly Mode under this product is Rs.306";
        } else {
            return true;
        }
        if (!error.equals("")) {
            commonMethods.showMessageDialog(context, error);
            return false;
        } else
            return true;


    }

    //Validate Rider Premium
    private boolean valRiderPremium(double premBasic, double sumOfRiders) {
        String error = "";
        if ((premBasic * 0.3) < sumOfRiders) {
            error = "Total of Rider Premium should not be greater than 30% of the Base Premium";
        } else {
            return true;
        }
        if (!error.equals("")) {
            commonMethods.showMessageDialog(context, error);
            return false;
        } else
            return true;


    }

    private boolean valMaturityAge() {
        String error = "";
        int Age = Integer.parseInt(edittext_EShieldnext_AgeOfLA.getText().toString());
        int PolicyTerm = Integer.parseInt(Spinner_PolicyTerm.getSelectedItem()
                .toString());

        if ((String_PremiumPaymentOption.equals("Single") || String_PremiumPaymentOption.equals("Regular"))
                && ((Age + PolicyTerm) > 85)) {
            error = "Max. Maturity age is 85 years";

        } else if (String_PremiumPaymentOption.equals("LPPT") && String_WholeLifeOtherThanWholeLife.
                equalsIgnoreCase("Other Than Whole Life") && ((Age + PolicyTerm) > 85)) {
            error = "Max. Maturity age is 85 years";

        } else if (String_PremiumPaymentOption.equals("LPPT") && String_WholeLifeOtherThanWholeLife.
                equalsIgnoreCase("Whole Life") && ((Age + PolicyTerm) > 100)) {
            error = "Max. Maturity age is 100 years";
        }

        if (!error.equals("")) {
            commonMethods.showMessageDialog(context, error);
            return false;
        } else {
            return true;
        }

    }


    private boolean valAge() {
        String error = "";
        int Age = Integer.parseInt(edittext_EShieldnext_AgeOfLA.getText().toString());
        int minLimit = 18, maxLimit = 65;
        if (Spinner_Plan.getSelectedItem().toString()
                .equals("Level Cover with Future Proofing Benefits")) {
            minLimit = 18;
            maxLimit = 45;
        } else if (Spinner_WholeLifeOtherThanWholeLife.getSelectedItem().toString()
                .equals("Whole Life")) {
            minLimit = 45;
            if (Spinner_PremiumPaymentOption.getSelectedItem().toString()
                    .equals("Single") || Spinner_PremiumPaymentOption.getSelectedItem().toString()
                    .equals("LPPT"))
                maxLimit = 65;
            else
                maxLimit = 60;
        } else if (Spinner_WholeLifeOtherThanWholeLife.getSelectedItem().toString()
                .equals("Other Than Whole Life")) {
            minLimit = 18;
            if (Spinner_PremiumPaymentOption.getSelectedItem().toString()
                    .equals("Single") || Spinner_PremiumPaymentOption.getSelectedItem().toString()
                    .equals("LPPT"))
                maxLimit = 65;
            else
                maxLimit = 60;
        } else
            maxLimit = 65;


        if (minLimit <= Age && Age <= maxLimit) {
            return true;
        } else {
            commonMethods.showMessageDialog(context, "Minimum Age should be " + minLimit + " yrs and Maximum Age should be "
                    + maxLimit + " yrs For LifeAssured");
            return false;
        }


    }

    public void SetPolicyTerm(String String_PremiumPaymentOption, String String_WholeLifeOtherThanWholeLife, String Age) {

        if (String_PremiumPaymentOption.equals("LPPT") && String_WholeLifeOtherThanWholeLife.equalsIgnoreCase("Whole Life")) {
            int Term = 100 - Integer.parseInt(Age);
            String[] policyTermList = {String.valueOf(Term)};
            ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item, policyTermList);
            policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
            Spinner_PolicyTerm.setAdapter(policyTermAdapter);
            policyTermAdapter.notifyDataSetChanged();
        } else {
            // Policy Term
            String[] policyTermList = new String[78];
            for (int i = 5; i <= 82; i++) {
                policyTermList[i - 5] = i + "";
            }
            ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item, policyTermList);
            policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
            Spinner_PolicyTerm.setAdapter(policyTermAdapter);
            policyTermAdapter.notifyDataSetChanged();


        }

    }

    public void SetPPT(String String_PremiumPaymentOption, String String_WholeLifeOtherThanWholeLife, String Term) {
        List<String> Array_PremiumPaymentTerm_Single = new ArrayList<>();
        List<String> Array_PremiumPaymentTerm_Regular = new ArrayList<>();
        List<String> Array_PremiumPaymentTerm_LPPT_whole_life = new ArrayList<>();
        List<String> Array_PremiumPaymentTerm_LPPT_other_whole_life = new ArrayList<>();


        Array_PremiumPaymentTerm_Single.add("1");
        Array_PremiumPaymentTerm_Regular.add(Term);

        //Array_PremiumPaymentTerm_LPPT_whole_life.add("Select");
        Array_PremiumPaymentTerm_LPPT_whole_life.add("7");
        Array_PremiumPaymentTerm_LPPT_whole_life.add("10");
        Array_PremiumPaymentTerm_LPPT_whole_life.add("15");
        Array_PremiumPaymentTerm_LPPT_whole_life.add("20");
        Array_PremiumPaymentTerm_LPPT_whole_life.add("25");

        // Array_PremiumPaymentTerm_LPPT_other_whole_life.add("Select");
        if (Integer.parseInt(Term) >= 10 && Integer.parseInt(Term) <= 11) {
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("5");
            //if ((Integer.parseInt(Term) - 5) != 5) {
            //  Array_PremiumPaymentTerm_LPPT_other_whole_life.add(String.valueOf(Integer.parseInt(Term) - 5));
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("PT less 5");
            // }

        } else if (Integer.parseInt(Term) >= 12 && Integer.parseInt(Term) <= 14) {
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("5");
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("7");
            // if ((Integer.parseInt(Term) - 5) != 7) {
            // Array_PremiumPaymentTerm_LPPT_other_whole_life.add(String.valueOf(Integer.parseInt(Term) - 5));

            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("PT less 5");
            // }
        } else if (Integer.parseInt(Term) >= 15 && Integer.parseInt(Term) <= 19) {
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("5");
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("7");
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("10");
            // if ((Integer.parseInt(Term) - 5) != 10) {
            // Array_PremiumPaymentTerm_LPPT_other_whole_life.add(String.valueOf(Integer.parseInt(Term) - 5));
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("PT less 5");

            // }
        } else if (Integer.parseInt(Term) >= 20 && Integer.parseInt(Term) <= 24) {
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("5");
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("7");
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("10");
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("15");
            //Array_PremiumPaymentTerm_LPPT_other_whole_life.add(String.valueOf(Integer.parseInt(Term) - 5));
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("PT less 5");
        } else if (Integer.parseInt(Term) >= 25 && Integer.parseInt(Term) <= 29) {
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("5");
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("7");
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("10");
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("15");
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("20");
            // if ((Integer.parseInt(Term) - 5) != 20) {

            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("PT less 5");
            //Array_PremiumPaymentTerm_LPPT_other_whole_life.add(String.valueOf(Integer.parseInt(Term) - 5));
            // }
        } else if (Integer.parseInt(Term) >= 30) {
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("5");
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("7");
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("10");
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("15");
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("20");
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("25");
            //if ((Integer.parseInt(Term) - 5) != 25) {
            //Array_PremiumPaymentTerm_LPPT_other_whole_life.add(String.valueOf(Integer.parseInt(Term) - 5));

            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("PT less 5");
            // }
        } else {
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("5");
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("7");
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("10");
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("15");
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("20");
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("25");
            // if ((Integer.parseInt(Term) - 5) != 25) {
            // Array_PremiumPaymentTerm_LPPT_other_whole_life.add(String.valueOf(Integer.parseInt(Term) - 5));
            Array_PremiumPaymentTerm_LPPT_other_whole_life.add("PT less 5");
            // }
        }


        switch (String_PremiumPaymentOption) {
            case "LPPT":
                if (String_PremiumPaymentOption.equals("LPPT") && String_WholeLifeOtherThanWholeLife.equalsIgnoreCase("Whole Life")) {
                    ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                            getApplicationContext(), R.layout.spinner_item, Array_PremiumPaymentTerm_LPPT_whole_life);
                    policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
                    Spinner_PremiumPaymentTerm.setAdapter(policyTermAdapter);
                    policyTermAdapter.notifyDataSetChanged();
                } else {
                    ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                            getApplicationContext(), R.layout.spinner_item, Array_PremiumPaymentTerm_LPPT_other_whole_life);
                    policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
                    Spinner_PremiumPaymentTerm.setAdapter(policyTermAdapter);
                    policyTermAdapter.notifyDataSetChanged();
                }
                break;
            case "Single": {
                ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                        getApplicationContext(), R.layout.spinner_item, Array_PremiumPaymentTerm_Single);
                policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
                Spinner_PremiumPaymentTerm.setAdapter(policyTermAdapter);
                policyTermAdapter.notifyDataSetChanged();
                break;
            }
            case "Regular": {
                ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                        getApplicationContext(), R.layout.spinner_item, Array_PremiumPaymentTerm_Regular);
                policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
                Spinner_PremiumPaymentTerm.setAdapter(policyTermAdapter);
                policyTermAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    boolean valTermRider() {
        boolean flagTermRider = false;
        int minLimit = 5;
        int vage = Integer.parseInt(edittext_EShieldnext_AgeOfLA.getText().toString());
        int maxLimit = Math.min(57, Math.min(Integer.parseInt(Spinner_PolicyTerm.getSelectedItem().toString()), 75 - vage));
        if (String_PremiumPaymentOption.equalsIgnoreCase("LPPT")) {
            if (Spinner_PremiumPaymentTerm.getSelectedItem().toString().equalsIgnoreCase("PT less 5")) {
                minLimit = (Integer.parseInt(Spinner_PolicyTerm.getSelectedItem().toString()) - 4);
            } else {
                minLimit = Integer.parseInt(Spinner_PremiumPaymentTerm.getSelectedItem().toString()) + 1;
            }

            //maxLimit = Integer.parseInt(Spinner_PolicyTerm.getSelectedItem().toString());
        }


        // int maxLimit = Math.min(50, Math.min(Integer.parseInt(spnrPolicyTerm.getSelectedItem().toString()), 70 - vage));


        StringBuilder error = new StringBuilder();


        if (CheckBox_AccidentalDeathBenefitRider.isChecked()
                && (Integer.parseInt(spnr_EShieldnext_TermForADBRider.getSelectedItem().toString()) > maxLimit ||
                Integer.parseInt(spnr_EShieldnext_TermForADBRider.getSelectedItem().toString()) < minLimit)) {
            spnr_EShieldnext_TermForADBRider.setSelection(0, false);
            error.append("\nPlease enter Policy Term of Accidental Death Benefit Rider between ").append(minLimit).append(" and ").append(maxLimit);
        }

        if (CheckBox_AccidentalTotalAndPermanentDisabilityBenefitRider.isChecked()
                && (Integer
                .parseInt(spnr_EShieldnext_TermForATPDBRider.getSelectedItem().toString()) > maxLimit || Integer
                .parseInt(spnr_EShieldnext_TermForATPDBRider.getSelectedItem().toString()) < minLimit)) {
            spnr_EShieldnext_TermForATPDBRider.setSelection(0, false);
            error.append("\nPlease select Policy Term of Accidental Total and Permanent Disability Benefit Rider between ").append(minLimit).append(" and ").append(maxLimit);
        }

        if (!error.toString().equals("")) {
            commonMethods.showMessageDialog(context, error.toString());
        }
        if (error.toString().equals("")) {
            flagTermRider = true;
        }

        return flagTermRider;
    }

    boolean valTerm() {

        int maxLimit = 0;
        int minLimit = 0;

        int Term = 0;
        if ((String_PremiumPaymentOption.equalsIgnoreCase("Single") ||
                String_PremiumPaymentOption.equalsIgnoreCase("Regular")) && !String_Plan.equalsIgnoreCase("Increasing Cover")) {
            minLimit = 5;
            maxLimit = 85 - Integer.parseInt(ProposerAge);
        } else if ((String_PremiumPaymentOption.equalsIgnoreCase("Single") ||
                String_PremiumPaymentOption.equalsIgnoreCase("Regular")) && String_Plan.equalsIgnoreCase("Increasing Cover")) {

            minLimit = 10;
            maxLimit = 85 - Integer.parseInt(ProposerAge);
        } else if (String_PremiumPaymentOption.equalsIgnoreCase("LPPT") && String_WholeLifeOtherThanWholeLife.equalsIgnoreCase("other than whole life")) {
            minLimit = 10;
            maxLimit = 85 - Integer.parseInt(ProposerAge);
        } else if (String_PremiumPaymentOption.equalsIgnoreCase("LPPT") && String_WholeLifeOtherThanWholeLife.equalsIgnoreCase("whole life")) {
            Term = 100 - Integer.parseInt(ProposerAge);
        }

        if ((Integer.parseInt(Spinner_PolicyTerm.getSelectedItem().toString()) < minLimit || Integer.parseInt(Spinner_PolicyTerm.getSelectedItem().toString()) > maxLimit) && !String_WholeLifeOtherThanWholeLife.equalsIgnoreCase("whole life")) {
            commonMethods.showMessageDialog(context, "Please enter Policy Term between " + minLimit + " and "
                    + maxLimit);
            Spinner_PolicyTerm.setSelection(0, false);
            return false;
        } else {
            return true;
        }
    }


    public boolean valDob() {

        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {

            if (lifeAssured_date_of_birth.equals("")
                    || lifeAssured_date_of_birth
                    .equalsIgnoreCase("select Date")) {
                commonMethods.showMessageDialog(context, "Please Select Valid Date Of Birth For LifeAssured");
                commonMethods.setFocusable(btn_bi_eShield_next_life_assured_date);
                btn_bi_eShield_next_life_assured_date
                        .requestFocus();
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public void Date() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        // formattedDate have current date/time
        // Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
        c.add(Calendar.DATE, 30);
        String expiredDate = df.format(c.getTime());
    }

    public boolean valLifeAssuredProposerDetail() {
        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
            if (lifeAssured_Title.equals("")
                    || lifeAssured_First_Name.equals("")
                    || lifeAssured_Last_Name.equals("")) {

                commonMethods.showMessageDialog(context, "Please Fill Name Detail For LifeAssured");
                if (lifeAssured_Title.equals("")) {
                    // apply focusable method
                    commonMethods.setFocusable(Spinner_LANameTitle);
                    Spinner_LANameTitle
                            .requestFocus();
                } else if (lifeAssured_First_Name.equals("")) {
                    EditText_LAFirstName
                            .requestFocus();
                } else {
                    EditText_LAFirstName
                            .requestFocus();
                }

                return false;
            } else if (String_Gender.equalsIgnoreCase("")) {

                commonMethods.showMessageDialog(context, "Please select Gender");
                commonMethods.setFocusable(Spinner_LANameTitle);
                Spinner_LANameTitle.requestFocus();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Mr.")
                    && String_Gender.equalsIgnoreCase("Female")) {
                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                commonMethods.setFocusable(Spinner_LANameTitle);
                Spinner_LANameTitle.requestFocus();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Ms.")
                    && String_Gender.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                commonMethods.setFocusable(Spinner_LANameTitle);
                Spinner_LANameTitle.requestFocus();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Mrs.")
                    && String_Gender.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                commonMethods.setFocusable(Spinner_LANameTitle);
                Spinner_LANameTitle.requestFocus();

                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }


    public boolean valSpouseDetail() {
        int AgeDiff = 0;
        if (String_BetterHalfBenefits.equalsIgnoreCase("Yes")) {
            if (Integer.parseInt(ProposerAge) > Integer.parseInt(SpouseAge)) {
                AgeDiff = (Integer.parseInt(ProposerAge) - Integer.parseInt(SpouseAge));
            } else {
                AgeDiff = (Integer.parseInt(SpouseAge) - Integer.parseInt(ProposerAge));
            }


            if (spouse_Title.equals("")
                    || spouse_First_Name.equals("")
                    || spouse_Last_Name.equals("")) {

                commonMethods.showMessageDialog(context, "Please Fill Name Detail For Spouse");
                commonMethods.setFocusable(spnr_bi_eShield_next_spouse_title);
                spnr_bi_eShield_next_spouse_title
                        .requestFocus();

                return false;
            } else if (String_Gender_spouse.equalsIgnoreCase("")) {
                commonMethods.showMessageDialog(context, "Please select Gender for spouse");
                commonMethods.setFocusable(spinnerSpouseGender);
                spinnerSpouseGender.requestFocus();
                return false;
            } else if (spouse_Title.equalsIgnoreCase("Mr.")
                    && String_Gender_spouse.equalsIgnoreCase("Female")) {
                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                commonMethods.setFocusable(spinnerSpouseGender);
                spinnerSpouseGender.requestFocus();
                return false;

            } else if (spouse_Title.equalsIgnoreCase("Ms.")
                    && String_Gender_spouse.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                commonMethods.setFocusable(spinnerSpouseGender);
                spinnerSpouseGender.requestFocus();
                return false;

            } else if (spouse_Title.equalsIgnoreCase("Mrs.")
                    && String_Gender_spouse.equalsIgnoreCase("Male")) {
                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                commonMethods.setFocusable(spinnerSpouseGender);
                spinnerSpouseGender.requestFocus();
                return false;
            } else if (spouse_date_of_birth
                    .equalsIgnoreCase("select Date")) {
                commonMethods.showMessageDialog(context, "Please Select Valid Date Of Birth For Spouse");
                commonMethods.setFocusable(btn_bi_eShield_next_spouse_date);
                btn_bi_eShield_next_spouse_date.requestFocus();

                return false;
            } else if (String_Smoker_Spouse
                    .equalsIgnoreCase("")) {
                commonMethods.showMessageDialog(context, "Please Select Smoker status For Spouse");
                return false;

            } else if (String_spouse_age_proof
                    .equalsIgnoreCase("")) {
                commonMethods.showMessageDialog(context, "Please Select spouse Age Proof");
                return false;

            } else if (String_spouse_qualification
                    .equalsIgnoreCase("")) {
                commonMethods.showMessageDialog(context, "Please Select spouse Qualfication");
                return false;

            } else if (AgeDiff > 10) {
                commonMethods.showMessageDialog(context, "The age difference between life assured and spouse is less than or equal to 10 years");
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    /* Basic Details Method */

    public boolean valPlanDetail() {

        if (String_Plan
                .equals("")) {
            commonMethods.dialogWarning(context, "Please select Plan", true);
            Spinner_Plan.requestFocus();
            return false;
        } else if (String_WholeLifeOtherThanWholeLife
                .equals("")) {
            commonMethods.dialogWarning(context, "Please select Whole Life Option", true);
            Spinner_WholeLifeOtherThanWholeLife.requestFocus();
            return false;
        } else if (String_PremiumPaymentOption
                .equals("")) {
            commonMethods.dialogWarning(context, "Please select Premium Payment Option", true);
            Spinner_PremiumPaymentOption.requestFocus();
            return false;
        } else if (String_PremiumFrequency
                .equals("")) {
            commonMethods.dialogWarning(context, "Please select Premium Frequency", true);
            Spinner_PremiumFrequency.requestFocus();
            return false;
        } else if (String_DeathBenefitsPaymentOption
                .equals("")) {
            commonMethods.dialogWarning(context, "Please select Death Benefits Payment Option", true);
            Spinner_DeathBenefitsPaymentOption.requestFocus();
            return false;
        } else if (String_BetterHalfBenefits
                .equals("")) {
            commonMethods.dialogWarning(context, "Please select Better Half Benefits Option", true);
            Spinner_DeathBenefitsPaymentOption.requestFocus();
            return false;
        } else {
            return true;
        }


    }

    private void Dialog() {


        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.BLACK));

        d.setContentView(R.layout.activity_bi_eshieldnext_output);


        TextView Textview_Plan, Textview_Age, Textview_Gender, Textview_SmokerNonSmoker, Textview_PremiumPaymentTerm, Textview_Frequency, Textview_DeathBenefitPaymentMode,
                Textview_PolicyTerm, Textview_PolicyTerm2, Textview_PremiumPaymentTerm1, Textview_PremiumPaymentTerm2, Textview_SumAssured, Textview_SumAssured2, Textview_TermforADBRider, Textview_PPTforADBRider, Textview_ADBRiderSumAssured,
                Textview_TermForATPDBRider, Textview_PPTForATPDBRider, Textview_ATPDBRiderSumAssured;

        TextView Textview_BottomConclusion;
        TextView Textview_EShieldNext_better_half_benfits, Textview_EShieldNext_Spouse_Name, Textview_EShieldNext_Spouse_Age, Textview_EShieldNext_smoker_spouse, Textview_EShieldNext_Gender_spouse;

        TextView Textview_BasicCoverPremium, Textview_BetterHalfBenefitsPremium, Textview_ADBRiderPremium, Textview_ATPDBRiderPremium,
                Textview_YearlyInstallmentsPremium, Textview_YearlyInstallmentsPremiumWithChannelDiscount, Textview_ApplicableTaxes,
                Textview_YearlyInstallmentsPremiumWithApplicableTaxes, Tv_installment_Premium, tv_installment_prem_tax, Tv_installment_Premium_staff_discount, Textview_EShieldNext_PremiumPaymentOption;

        TableRow tr_installment_premium_staff_discount;

        Textview_EShieldNext_PremiumPaymentOption = d.findViewById(R.id.Textview_EShieldNext_PremiumPaymentOption);

        Tv_installment_Premium_staff_discount = d.findViewById(R.id.Tv_installment_Premium_staff_discount);
        Textview_Plan = d.findViewById(R.id.Textview_EShieldNext_plan);
        Textview_Age = d.findViewById(R.id.Textview_EShieldNext_Age);
        Textview_Gender = d.findViewById(R.id.Textview_EShieldNext_Gender);
        Textview_SmokerNonSmoker = d.findViewById(R.id.Textview_EShieldNext_Smoker);
        Textview_Frequency = d.findViewById(R.id.Textview_EShieldNext_Frequency);
        Textview_DeathBenefitPaymentMode = d.findViewById(R.id.Textview_EShieldNext_DeathBenefitPaymentMode);
        Textview_PolicyTerm = d.findViewById(R.id.Textview_EShieldNext_PolicyTerm);
        Textview_PolicyTerm2 = d.findViewById(R.id.Textview_EShieldNext_PolicyTerm2);
        Textview_PremiumPaymentTerm1 = d.findViewById(R.id.Textview_EShieldNext_PremiumPaymentTerm1);
        Textview_PremiumPaymentTerm2 = d.findViewById(R.id.Textview_EShieldNext_PremiumPaymentTerm2);
        Textview_SumAssured = d.findViewById(R.id.Textview_EShieldNext_SumAssured);
        Textview_SumAssured2 = d.findViewById(R.id.Textview_EShieldNext_SumAssured2);
        Textview_TermforADBRider = d.findViewById(R.id.Textview_EShieldNext_TermForADBRider);
        Textview_PPTforADBRider = d.findViewById(R.id.Textview_EShieldNext_PPTForADBRider);
        Textview_ADBRiderSumAssured = d.findViewById(R.id.Textview_EShieldNext_ADBRiderSumAssured);
        Textview_TermForATPDBRider = d.findViewById(R.id.Textview_EShieldNext_TermForATPDBRider);

        Textview_PPTForATPDBRider = d.findViewById(R.id.Textview_EShieldNext_PPTForATPDBRider);
        Textview_ATPDBRiderSumAssured = d.findViewById(R.id.Textview_EShieldNext_ATPDBRiderSumAssured);
        Textview_BottomConclusion = d.findViewById(R.id.Textview_EShieldNext_BottomConclusion);

        Textview_EShieldNext_better_half_benfits = d.findViewById(R.id.Textview_EShieldNext_better_half_benfits);
        Textview_EShieldNext_Spouse_Name = d.findViewById(R.id.Textview_EShieldNext_Spouse_Name);
        Textview_EShieldNext_Spouse_Age = d.findViewById(R.id.Textview_EShieldNext_Spouse_Age);
        Textview_EShieldNext_smoker_spouse = d.findViewById(R.id.Textview_EShieldNext_smoker_spouse);
        Textview_EShieldNext_Gender_spouse = d.findViewById(R.id.Textview_EShieldNext_Gender_spouse);


        Textview_BasicCoverPremium = d.findViewById(R.id.Textview_EShieldNext_BasiCoverPremium);
        Textview_BetterHalfBenefitsPremium = d.findViewById(R.id.Textview_EShieldNext_BetterHalfBenefitsPremium);
        Textview_ADBRiderPremium = d.findViewById(R.id.Textview_EShieldNext_ADBRiderPremium);
        Textview_ATPDBRiderPremium = d.findViewById(R.id.Textview_EShieldNext_ATPDBRiderPremium);
        Textview_YearlyInstallmentsPremium = d.findViewById(R.id.Textview_EShieldNext_YearlyInstallmentsPremium);
        Textview_YearlyInstallmentsPremiumWithChannelDiscount = d.findViewById(R.id.Textview_EShieldNext_YearlyInstallmentsPremiumWithChannelDiscount);
        Textview_ApplicableTaxes = d.findViewById(R.id.Textview_EshieldNext_ApplicableTaxes);
        Textview_YearlyInstallmentsPremiumWithApplicableTaxes = d.findViewById(R.id.Textview_EShieldNext_YearlyInstallmentsPremiumWithApplicableTaxes);


        tr_installment_premium_staff_discount = d.findViewById(R.id.tr_installment_premium_staff_discount);

        Tv_installment_Premium = d.findViewById(R.id.Tv_installment_Premium);
        tv_installment_prem_tax = d.findViewById(R.id.tv_installment_prem_tax);

        Tv_installment_Premium.setText(String_PremiumFrequency + " Installment Premium");
        Tv_installment_Premium_staff_discount.setText(String_PremiumFrequency + " Installment Premium with Staff Discount");
        tv_installment_prem_tax.setText(String_PremiumFrequency + " Installment Premium with Applicable taxes");


        final EditText edt_Policyholderplace = d
                .findViewById(R.id.edt_Policyholderplace);

        final EditText edt_proposer_name = d
                .findViewById(R.id.edt_smart_shield_proposer_name);


        /* Need Analysis */
        final TextView txt_proposer_name_need_analysis = d
                .findViewById(R.id.txt_proposer_name_need_analysis);

        final CheckBox cb_statement_need_analysis = d
                .findViewById(R.id.cb_statement_need_analysis);
        cb_statement_need_analysis.setChecked(true);
        TableRow tr_need_analysis = d
                .findViewById(R.id.tr_need_analysis);


        if (NeedAnalysisActivity.str_need_analysis != null) {
            if (NeedAnalysisActivity.str_need_analysis.equals("1")) {

                File mypath_old = mStorageUtils.createFileToAppSpecificDir(context, "NA" + ".pdf");
                File mypath_new = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "_NA"
                        + ".pdf");
                if (mypath_old.exists()) {
                    mypath_old.renameTo(mypath_new);
                }

                File mypath_old_Annexure1 = mStorageUtils.createFileToAppSpecificDir(context, "NA_Annexure1.pdf");
                File mypath_old_Annexure2 = mStorageUtils.createFileToAppSpecificDir(context, "NA_Annexure2.pdf");

                File mypath_new_Annexure1 = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "_NA1"
                        + ".pdf");
                File mypath_new_Annexure2 = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "_NA2"
                        + ".pdf");

                if (mypath_old_Annexure1.exists()) {
                    mypath_old_Annexure1.renameTo(mypath_new_Annexure1);
                }
                if (mypath_old_Annexure2.exists()) {
                    mypath_old_Annexure2.renameTo(mypath_new_Annexure2);
                }
                flg_needAnalyis = "1";
                tr_need_analysis.setVisibility(View.VISIBLE);
            } else {
                cb_statement_need_analysis.setChecked(true);
                tr_need_analysis.setVisibility(View.GONE);
            }

        }

        Button btn_proceed = d
                .findViewById(R.id.btn_smart_shield_proceed);

        Button btn_MarketingOfficalDate = d
                .findViewById(R.id.btn_MarketingOfficalDate);
        Button btn_PolicyholderDate = d
                .findViewById(R.id.btn_PolicyholderDate);

        Ibtn_signatureofMarketing = d
                .findViewById(R.id.Ibtn_signatureofMarketing);
        Ibtn_signatureofPolicyHolders = d
                .findViewById(R.id.Ibtn_signatureofPolicyHolders);

        imageButtonProposerPhotograph = d
                .findViewById(R.id.imageButtonProposerPhotograph);
        Calendar calender = Calendar.getInstance();
        int Year = calender.get(Calendar.YEAR);
        int Month = calender.get(Calendar.MONTH);
        int Day = calender.get(Calendar.DAY_OF_MONTH);

        if (!proposer_sign.equals("")) {
            if (flg_needAnalyis.equals("1")) {
                tr_need_analysis.setVisibility(View.VISIBLE);
            } else {
                cb_statement_need_analysis.setChecked(true);
                tr_need_analysis.setVisibility(View.GONE);
            }
        }
        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
            edt_proposer_name.setText(name_of_life_assured);
            txt_proposer_name_need_analysis.setText("I, " + name_of_life_assured
                    + " have undergone the Need Analysis & after having reviewed the SBI Life Product options, I have opted for 'SBI LIFE-eShield Next'.");
            // tv_proposername.setText(name_of_life_assured);
        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
            edt_proposer_name.setText(name_of_proposer);
            txt_proposer_name_need_analysis.setText("I, " + name_of_proposer
                    + " have undergone the Need Analysis & after having reviewed the SBI Life Product options, I have opted for 'SBI LIFE-eShield Next'.");
            // tv_proposername.setText(name_of_proposer);
        }


        String str_cif_city = "";
        if (str_cif_city == null) {
            str_cif_city = "";
        }
        if (place2.equals("")) {
            edt_Policyholderplace.setText(str_cif_city);
        } else {
            edt_Policyholderplace.setText(place2);
        }

        if (!date2.equals("")) {
            btn_PolicyholderDate.setText(getDate(date2));
        } else {
            date2 = getDate1(getCurrentDate());
            btn_PolicyholderDate.setText(getCurrentDate());
        }

        if (!date1.equals("")) {
            btn_MarketingOfficalDate.setText(getDate(date1));
        } else {
            date1 = getDate1(getCurrentDate());
            btn_MarketingOfficalDate.setText(getCurrentDate());
        }

        if (agent_sign != null && !agent_sign.equals("")) {
            byte[] signByteArray = Base64.decode(agent_sign, 0);
            Bitmap bitmap = BitmapFactory.decodeByteArray(signByteArray, 0,
                    signByteArray.length);
            Ibtn_signatureofMarketing.setImageBitmap(bitmap);
        }

        if (proposer_sign != null && !proposer_sign.equals("")) {
            byte[] signByteArray = Base64.decode(proposer_sign, 0);
            Bitmap bitmap = BitmapFactory.decodeByteArray(signByteArray, 0,
                    signByteArray.length);
            Ibtn_signatureofPolicyHolders.setImageBitmap(bitmap);
        }


        imageButtonProposerPhotograph = d
                .findViewById(R.id.imageButtonProposerPhotograph);
        imageButtonProposerPhotograph
                .setOnClickListener(view -> {
                    Check = "Photo";
                    commonMethods.windowmessage(context, "_cust1Photo.jpg");
                });

        Ibtn_signatureofMarketing
                .setOnClickListener(view -> {
                    if (/*cb_statement.isChecked()
                            && */cb_statement_need_analysis.isChecked()
                        /*&& checkboxAgentStatement.isChecked()*/) {
                        latestImage = "agent";
                        commonMethods.windowMessageSign(context, latestImage);
                    } else {
                        commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                        commonMethods.setFocusable(cb_statement_need_analysis);
                        cb_statement_need_analysis.requestFocus();
                    }

                });

        Ibtn_signatureofPolicyHolders
                .setOnClickListener(view -> {
                    if (/*cb_statement.isChecked()
                            &&*/ cb_statement_need_analysis.isChecked()
                        /*&& checkboxAgentStatement.isChecked()*/) {
                        latestImage = "proposer";
                        commonMethods.windowmessageProposersgin(context,
                                NeedAnalysisActivity.URN_NO + "_cust1sign");
                    } else {
                        commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                        commonMethods.setFocusable(cb_statement_need_analysis);
                        cb_statement_need_analysis.requestFocus();
                    }

                });

        LinearLayout linearLayoutProposerSignature = d
                .findViewById(R.id.linearLayoutProposerSignature);
        LinearLayout linearLayoutmarketingOfficerSignature = d
                .findViewById(R.id.linearLayoutmarketingOfficerSignature);

        if (bankUserType.equalsIgnoreCase("Y")) {
            linearLayoutProposerSignature.setVisibility(View.GONE);
            linearLayoutmarketingOfficerSignature.setVisibility(View.GONE);
        } else {
            linearLayoutProposerSignature.setVisibility(View.VISIBLE);
            linearLayoutmarketingOfficerSignature.setVisibility(View.VISIBLE);
        }

        if (photoBitmap != null) {
            imageButtonProposerPhotograph
                    .setImageBitmap(photoBitmap);
        }

        final RadioButton radioButtonTrasactionModeManual = d
                .findViewById(R.id.radioButtonTrasactionModeManual);
        final RadioButton radioButtonTrasactionModeParivartan = d
                .findViewById(R.id.radioButtonTrasactionModeParivartan);
        final LinearLayout linearlayoutTrasactionModeParivartan = d
                .findViewById(R.id.linearlayoutTrasactionModeParivartan);

        radioButtonTrasactionModeParivartan
                .setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        transactionMode = "Parivartan";
                        linearlayoutTrasactionModeParivartan
                                .setVisibility(View.VISIBLE);
                    } else {
                        linearlayoutTrasactionModeParivartan
                                .setVisibility(View.GONE);

                        String customerPhotoName = NeedAnalysisActivity.URN_NO
                                + "_cust1Photo.jpg";
                        File customerPhotoFile = mStorageUtils.createFileToAppSpecificDir(context,
                                customerPhotoName);
                        if (customerPhotoFile.exists()) {
                            customerPhotoFile.delete();
                        }

                        photoBitmap = null;
                        imageButtonProposerPhotograph
                                .setImageDrawable(getResources()
                                        .getDrawable(
                                                R.drawable.focus_imagebutton_photo));
                    }
                });

        radioButtonTrasactionModeManual
                .setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        transactionMode = "Manual";
                    } else {

                    }
                });
        /* end */

        if (transactionMode.equalsIgnoreCase("Manual")) {
            radioButtonTrasactionModeManual.setChecked(true);
        } else if (transactionMode.equalsIgnoreCase("Parivartan")) {
            radioButtonTrasactionModeParivartan.setChecked(true);
        }

        if (!TextUtils.isEmpty(place2)) {
            edt_Policyholderplace.setText(place2);
        }

        btn_proceed.setOnClickListener(v -> {

            name_of_person = edt_proposer_name.getText().toString();
            place1 = "";
            place2 = edt_Policyholderplace.getText().toString();

            if ((!name_of_proposer.equals("") || !name_of_life_assured
                    .equals(""))
                    && !place2.equals("")
                    && !date1.equals("Select Date")
                    && (!date2.equals("Select Date") || bankUserType
                    .equalsIgnoreCase("Y"))
                    && (!agent_sign.equals("") || bankUserType
                    .equalsIgnoreCase("Y"))
                    && (!proposer_sign.equals("") || bankUserType
                    .equalsIgnoreCase("Y"))
                    && (cb_statement_need_analysis.isChecked())
                    /*&& (cb_statement.isChecked())
                    && checkboxAgentStatement.isChecked()*/
                    && (((photoBitmap != null) && radioButtonTrasactionModeParivartan
                    .isChecked()) || radioButtonTrasactionModeManual
                    .isChecked())) {
                NeedAnalysisActivity.str_need_analysis = "";
                String productCode = "";

                if (String_Plan.equalsIgnoreCase("Level Cover with Future Proofing Benefits")
                        && String_WholeLifeOtherThanWholeLife.equalsIgnoreCase("Other Than Whole Life")
                        && String_PremiumPaymentOption.equalsIgnoreCase("Regular")) {
                    productCode = "ESHNFPRP";
                }

                if (String_Plan.equalsIgnoreCase("Increasing Cover")
                        && String_WholeLifeOtherThanWholeLife.equalsIgnoreCase("Other Than Whole Life")
                        && String_PremiumPaymentOption.equalsIgnoreCase("LPPT")) {
                    productCode = "ESHNICLP";
                }
                if (String_Plan.equalsIgnoreCase("Increasing Cover")
                        && String_WholeLifeOtherThanWholeLife.equalsIgnoreCase("Other Than Whole Life")
                        && String_PremiumPaymentOption.equalsIgnoreCase("Regular")) {
                    productCode = "ESHNICRP";
                }
                if (String_Plan.equalsIgnoreCase("Increasing Cover")
                        && String_WholeLifeOtherThanWholeLife.equalsIgnoreCase("Other Than Whole Life")
                        && String_PremiumPaymentOption.equalsIgnoreCase("Single")) {
                    productCode = "ESHNICSP";
                }


                if (String_Plan.equalsIgnoreCase("Level Cover")
                        && String_WholeLifeOtherThanWholeLife.equalsIgnoreCase("Other Than Whole Life")
                        && String_PremiumPaymentOption.equalsIgnoreCase("LPPT")) {
                    productCode = "ESHNLCLP";
                }
                if (String_Plan.equalsIgnoreCase("Level Cover")
                        && String_WholeLifeOtherThanWholeLife.equalsIgnoreCase("Other Than Whole Life")
                        && String_PremiumPaymentOption.equalsIgnoreCase("Regular")) {
                    productCode = "ESHNLCRP";
                }
                if (String_Plan.equalsIgnoreCase("Level Cover")
                        && String_WholeLifeOtherThanWholeLife.equalsIgnoreCase("Other Than Whole Life")
                        && String_PremiumPaymentOption.equalsIgnoreCase("Single")) {
                    productCode = "ESHNLCSP";
                }


                if (String_Plan.equalsIgnoreCase("Increasing Cover")
                        && String_WholeLifeOtherThanWholeLife.equalsIgnoreCase("Whole Life")
                        && String_PremiumPaymentOption.equalsIgnoreCase("LPPT")) {
                    productCode = "ESHNWLICLP";
                }
                if (String_Plan.equalsIgnoreCase("Level Cover")
                        && String_WholeLifeOtherThanWholeLife.equalsIgnoreCase("Whole Life")
                        && String_PremiumPaymentOption.equalsIgnoreCase("LPPT")) {
                    productCode = "ESHNWLLCLP";
                }
                if (String_Plan.equalsIgnoreCase("Increasing Cover")
                        && String_WholeLifeOtherThanWholeLife.equalsIgnoreCase("Whole Life")
                        && String_PremiumPaymentOption.equalsIgnoreCase("Single")) {
                    productCode = "ESHNLCSP";
                }

                        na_cbi_bean = new NA_CBI_bean(QuatationNumber, agentcode,
                        "", userType, "", lifeAssured_Title,
                        lifeAssured_First_Name, lifeAssured_Middle_Name,
                        lifeAssured_Last_Name, planName,
                        obj.getRound(obj.getStringWithout_E(Double
                                .parseDouble(Sumassured.equals("") ? "0"
                                        : Sumassured))), obj
                        .getRound(PremiumwithST), emailId,
                        mobileNo, agentEmail, agentMobile, na_input,
                        na_output, shieldNextBean.getPremiumFrequency(),shieldNextBean.getPolicyterm(),
                        Integer.parseInt(String_PremiumPaymentTerm), productCode,commonMethods.getDDMMYYYYDate(lifeAssured_date_of_birth),
                        commonMethods.getDDMMYYYYDate(proposer_date_of_birth), inputVal.toString(), retVal.toString().replace(
                        bussIll.toString(), ""));

                name_of_person = name_of_life_assured;

                if (radioButtonTrasactionModeParivartan.isChecked()) {
                    transactionMode = "Parivartan";
                } else if (radioButtonTrasactionModeManual.isChecked()) {
                    transactionMode = "Manual";
                }

                db.AddNeedAnalysisDashboardDetails(new ProductBIBean("",
                        QuatationNumber, planName, getCurrentDate(),
                        mobileNo, getCurrentDate(), db.GetUserCode(),
                        emailId, "", "", agentcode, "", userType, "",
                        lifeAssured_Title, lifeAssured_First_Name,
                        lifeAssured_Middle_Name, lifeAssured_Last_Name,
                        obj.getRound(obj.getStringWithout_E(Double
                                .parseDouble(Sumassured.equals("") ? "0"
                                        : Sumassured))), obj
                        .getRound(PremiumwithST), agentEmail,
                        agentMobile, na_input, na_output,
                        shieldNextBean.getPremiumFrequency(), shieldNextBean.getPolicyterm(), Integer.parseInt(String_PremiumPaymentTerm),
                        productCode,commonMethods.getDDMMYYYYDate(lifeAssured_date_of_birth),
                        commonMethods.getDDMMYYYYDate(proposer_date_of_birth), "", transactionMode, inputVal
                        .toString(), retVal.toString().replace(
                        bussIll.toString(), "")));
                createPdf();
                NABIObj.serviceHit(context,
                        na_cbi_bean, newFile, needAnalysispath.getPath(),
                        mypath.getPath(), name_of_person, QuatationNumber, transactionMode);
                d.dismiss();

            } else {

                if (proposer_sign.equals("")
                        && !bankUserType.equalsIgnoreCase("Y")) {
                    commonMethods.dialogWarning(context, "Please Make Signature for Proposer ", true);
                    commonMethods.setFocusable(Ibtn_signatureofPolicyHolders);
                    Ibtn_signatureofPolicyHolders.requestFocus();
                } else if (place2.equals("")) {
                    commonMethods.dialogWarning(context, "Please Fill Place Detail", true);
                    commonMethods.setFocusable(edt_Policyholderplace);
                    edt_Policyholderplace.requestFocus();

                } else if (agent_sign.equals("")
                        && !bankUserType.equalsIgnoreCase("Y")) {
                    commonMethods.dialogWarning(context, "Please Make Signature for Sales Representative",
                            true);
                    commonMethods.setFocusable(Ibtn_signatureofMarketing);
                    Ibtn_signatureofMarketing.requestFocus();
                } else if (!cb_statement_need_analysis.isChecked()) {
                    commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                    commonMethods.setFocusable(cb_statement_need_analysis);
                    cb_statement_need_analysis.requestFocus();
                }/*else if (!cb_statement.isChecked()) {
                    commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                    commonMethods.setFocusable(cb_statement);
                    cb_statement.requestFocus();
                } else if (!checkboxAgentStatement.isChecked()) {
                    commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                    commonMethods.setFocusable(checkboxAgentStatement);
                    checkboxAgentStatement.requestFocus();
                }*/ else if (!radioButtonTrasactionModeParivartan.isChecked()
                        && !radioButtonTrasactionModeManual.isChecked()) {
                    commonMethods.dialogWarning(context, "Please Select Transaction Mode", true);
                    commonMethods.setFocusable(linearlayoutTrasactionModeParivartan);
                    linearlayoutTrasactionModeParivartan.requestFocus();
                } else if (photoBitmap == null) {
                    commonMethods.dialogWarning(context, "Please Capture the Photo", true);
                    commonMethods.setFocusable(imageButtonProposerPhotograph);
                    imageButtonProposerPhotograph.requestFocus();
                }
            }


        });

        btn_PolicyholderDate.setOnClickListener(v -> {


            DIALOG_ID = 2;
            showDateDialog();
        });

        btn_MarketingOfficalDate.setOnClickListener(v -> {

            DIALOG_ID = 3;
            showDateDialog();

        });
        if (CheckBox_StaffDiscount.isChecked()) {
            tr_installment_premium_staff_discount.setVisibility(View.VISIBLE);
        }


        String output = retVal.toString();
        basicpremium = ((prsObj.parseXmlTag(output,
                "basicpremium") == null) || (prsObj
                .parseXmlTag(output, "basicpremium")
                .equals(""))) ? "0" : prsObj
                .parseXmlTag(output, "basicpremium");


        BetterHalfBenefitPremium = ((prsObj.parseXmlTag(output,
                "BetterHalfBenefitPremium") == null) || (prsObj
                .parseXmlTag(output, "BetterHalfBenefitPremium")
                .equals(""))) ? "0" : prsObj
                .parseXmlTag(output, "BetterHalfBenefitPremium");


        adbpremium = ((prsObj.parseXmlTag(output,
                "adbpremium") == null) || (prsObj
                .parseXmlTag(output, "adbpremium")
                .equals(""))) ? "0" : prsObj
                .parseXmlTag(output, "adbpremium");


        atpdbpremium = ((prsObj.parseXmlTag(output,
                "atpdbpremium") == null) || (prsObj
                .parseXmlTag(output, "atpdbpremium")
                .equals(""))) ? "0" : prsObj
                .parseXmlTag(output, "atpdbpremium");

        totalpremiumwithoutST = Integer.parseInt(obj
                .getRound(obj.getStringWithout_E(Double
                        .parseDouble(((prsObj.parseXmlTag(output,
                                "totalpremiumwithoutST") == null) || (prsObj
                                .parseXmlTag(output, "totalpremiumwithoutST")
                                .equals(""))) ? "0" : prsObj
                                .parseXmlTag(output, "totalpremiumwithoutST")))))
                + "";

        BasicServicetax = Integer.parseInt(obj
                .getRound(obj.getStringWithout_E(Double
                        .parseDouble(((prsObj.parseXmlTag(output,
                                "BasicServicetax") == null) || (prsObj
                                .parseXmlTag(output, "BasicServicetax")
                                .equals(""))) ? "0" : prsObj
                                .parseXmlTag(output, "BasicServicetax")))))
                + "";

        YearlyInstPremWithTaxes = Integer.parseInt(obj
                .getRound(obj.getStringWithout_E(Double
                        .parseDouble(((prsObj.parseXmlTag(output,
                                "YearlyInstPremWithTaxes") == null) || (prsObj
                                .parseXmlTag(output, "YearlyInstPremWithTaxes")
                                .equals(""))) ? "0" : prsObj
                                .parseXmlTag(output, "YearlyInstPremWithTaxes")))))
                + "";


        PremiumwithST = Integer.parseInt(obj
                .getRound(obj.getStringWithout_E(Double
                        .parseDouble(((prsObj.parseXmlTag(output,
                                "PremiumwithST") == null) || (prsObj
                                .parseXmlTag(output, "PremiumwithST")
                                .equals(""))) ? "0" : prsObj
                                .parseXmlTag(output, "PremiumwithST")))))
                + "";

        InstallmentPremiumWithStaffDiscount = Integer.parseInt(obj
                .getRound(obj.getStringWithout_E(Double
                        .parseDouble(((prsObj.parseXmlTag(output,
                                "InstallmentPremiumWithStaffDiscount") == null) || (prsObj
                                .parseXmlTag(output, "InstallmentPremiumWithStaffDiscount")
                                .equals(""))) ? "0" : prsObj
                                .parseXmlTag(output, "InstallmentPremiumWithStaffDiscount")))))
                + "";


        Textview_YearlyInstallmentsPremium.setText(getformatedThousandString(Integer.parseInt(obj
                .getRound(obj.getStringWithout_E(Double
                        .parseDouble(totalpremiumwithoutST)))))
                + "");
        Textview_ApplicableTaxes.setText(getformatedThousandString(Integer.parseInt(obj
                .getRound(obj.getStringWithout_E(Double
                        .parseDouble(BasicServicetax)))))
                + "");
        Textview_YearlyInstallmentsPremiumWithApplicableTaxes.setText(getformatedThousandString(Integer.parseInt(obj
                .getRound(obj.getStringWithout_E(Double
                        .parseDouble(PremiumwithST)))))
                + "");
        Textview_EShieldNext_PremiumPaymentOption.setText(String_PremiumPaymentOption);
        Textview_Plan.setText(String_Plan);
        Textview_Age.setText(ProposerAge);
        Textview_Gender.setText(String_Gender);
        Textview_SmokerNonSmoker.setText(String_Smoker);
        Textview_Frequency.setText(String_PremiumFrequency);
        Textview_DeathBenefitPaymentMode.setText(String_DeathBenefitsPaymentOption);
        Textview_PolicyTerm.setText(String_PolicyTerm);


        Textview_PremiumPaymentTerm1.setText(String_PremiumPaymentTerm_display);
        Textview_PremiumPaymentTerm2.setText(String_PremiumPaymentTerm_display);
        Textview_SumAssured.setText(getformatedThousandString(Integer.parseInt(obj
                .getRound(obj.getStringWithout_E(Double
                        .parseDouble(String_SumAssured)))))
                + "");

        Textview_YearlyInstallmentsPremiumWithChannelDiscount.setText(getformatedThousandString(Integer.parseInt(obj
                .getRound(obj.getStringWithout_E(Double
                        .parseDouble(InstallmentPremiumWithStaffDiscount)))))
                + "");


        Textview_BasicCoverPremium.setText(basicpremium);
        Textview_YearlyInstallmentsPremium.setText(getformatedThousandString(Integer.parseInt(obj
                .getRound(obj.getStringWithout_E(Double
                        .parseDouble(totalpremiumwithoutST)))))
                + "");
        Textview_YearlyInstallmentsPremiumWithApplicableTaxes.setText(getformatedThousandString(Integer.parseInt(obj
                .getRound(obj.getStringWithout_E(Double
                        .parseDouble(YearlyInstPremWithTaxes)))))
                + "");

        if (String_AccidentalDeathBenefitRider.equalsIgnoreCase("True")) {
            String adbRiderPremum = prsObj.parseXmlTag(output,
                    "adbpremium");
            Textview_TermforADBRider.setText(String_TermForADBRider);

            if (String_PremiumPaymentOption.equalsIgnoreCase("LPPT")) {
                Textview_PPTforADBRider.setText(String_PremiumPaymentTerm_display);
            } else if (String_PremiumPaymentOption.equalsIgnoreCase("Single")) {
                Textview_PPTforADBRider.setText("1");
            } else {
                Textview_PPTforADBRider.setText(String_TermForADBRider);
            }

            Textview_ADBRiderSumAssured.setText(getformatedThousandString(Integer.parseInt(obj
                    .getRound(obj.getStringWithout_E(Double
                            .parseDouble(String_ADBRiderSumAssured))))));
            Textview_ADBRiderPremium.setText(adbRiderPremum);

        } else {
            Textview_TermforADBRider.setText("-");
            Textview_PPTforADBRider.setText("-");
            Textview_ADBRiderSumAssured.setText("-");
            Textview_ADBRiderPremium.setText("-");
        }

        if (String_AccidentalTotalAndPermanentDisabilityBenefitRider.equalsIgnoreCase("True")) {

            String atpdbRiderPremium = prsObj.parseXmlTag(output,
                    "atpdbpremium");
            Textview_TermForATPDBRider.setText(String_TermForATPDBRider);


            if (String_PremiumPaymentOption.equalsIgnoreCase("LPPT")) {
                Textview_PPTForATPDBRider.setText(String_PremiumPaymentTerm_display);
            } else if (String_PremiumPaymentOption.equalsIgnoreCase("Single")) {
                Textview_PPTForATPDBRider.setText("1");
            } else {
                Textview_PPTForATPDBRider.setText(String_TermForATPDBRider);
            }


            Textview_ATPDBRiderSumAssured.setText(getformatedThousandString(Integer.parseInt(obj
                    .getRound(obj.getStringWithout_E(Double
                            .parseDouble(String_ATPDBRiderSumAssured))))));
            Textview_ATPDBRiderPremium.setText(atpdbRiderPremium);
        } else {
            Textview_TermForATPDBRider.setText("-");
            Textview_PPTForATPDBRider.setText("-");
            Textview_ATPDBRiderSumAssured.setText("-");
            Textview_ATPDBRiderPremium.setText("-");
        }


        if (String_BetterHalfBenefits.equalsIgnoreCase("Yes")) {
            Textview_PolicyTerm2.setText(String.valueOf(String_BHB_PolicyTerm));
            Textview_PremiumPaymentTerm2.setText(String_PremiumPaymentTerm_display);
            Textview_SumAssured2.setText(BetterHalfBenefitSum);
            Textview_BetterHalfBenefitsPremium.setText(BetterHalfBenefitPremium);
            Textview_EShieldNext_better_half_benfits.setText(String_BetterHalfBenefits);
            Textview_EShieldNext_Spouse_Name.setText(name_of_spouse);
            Textview_EShieldNext_Spouse_Age.setText(SpouseAge);
            Textview_EShieldNext_smoker_spouse.setText(String_Smoker_Spouse);
            Textview_EShieldNext_Gender_spouse.setText(String_Gender_spouse);
        } else {
            Textview_EShieldNext_better_half_benfits.setText("No");
            Textview_EShieldNext_Spouse_Name.setText("-");
            Textview_EShieldNext_Spouse_Age.setText("-");
            Textview_EShieldNext_smoker_spouse.setText("-");
            Textview_EShieldNext_Gender_spouse.setText("-");

            Textview_PolicyTerm2.setText("-");
            Textview_PremiumPaymentTerm2.setText("-");
            Textview_SumAssured2.setText("-");
            Textview_BetterHalfBenefitsPremium.setText("-");
        }

        if (String_PremiumPaymentOption.equalsIgnoreCase("Single")) {

            Textview_BottomConclusion.setText("Your SBI Life – eShield Next (UIN: 111N132V01) is a " + String_PremiumPaymentOption +
                    " premium policy and you are required to pay Single Premium of Rs. " + getformatedThousandString(Integer.parseInt(obj
                    .getRound(obj.getStringWithout_E(Double
                            .parseDouble(YearlyInstPremWithTaxes))))) + ". Your Policy Term is " + String_PolicyTerm +
                    "years and Basic Sum Assured is Rs. " + getformatedThousandString(Integer.parseInt(obj
                    .getRound(obj.getStringWithout_E(Double
                            .parseDouble(String_SumAssured))))));

        } else if (String_PremiumPaymentOption.equalsIgnoreCase("Regular")) {

            Textview_BottomConclusion.setText("Your SBI Life – eShield Next (UIN: 111N132V01) is a " + String_PremiumPaymentOption +
                    "Premium  policy, for which your " + String_PremiumFrequency + " premium is Rs. " + getformatedThousandString(Integer.parseInt(obj
                    .getRound(obj.getStringWithout_E(Double
                            .valueOf(YearlyInstPremWithTaxes))))) + ". Your Policy Term is " + String_PolicyTerm +
                    "years, Premium Payment Term is " + String_PremiumPaymentTerm + " years and Basic Sum Assured is Rs. " + getformatedThousandString(Integer.parseInt(obj
                    .getRound(obj.getStringWithout_E(Double
                            .valueOf(String_SumAssured))))));
        } else {

            Textview_BottomConclusion.setText("Your SBI Life – eShield Next (UIN: 111N132V01) is a " + String_PremiumPaymentOption +
                    " policy, for which your " + String_PremiumFrequency + " premium is Rs. " + getformatedThousandString(Integer.parseInt(obj
                    .getRound(obj.getStringWithout_E(Double
                            .parseDouble(YearlyInstPremWithTaxes))))) + ". Your Policy Term is " + String_PolicyTerm +
                    "years, Premium Payment Term is " + String_PremiumPaymentTerm + " years and Basic Sum Assured is Rs. " + getformatedThousandString(Integer.parseInt(obj
                    .getRound(obj.getStringWithout_E(Double
                            .parseDouble(String_SumAssured))))));
        }
        d.show();
    }


    public boolean CalculatePrem(String Staff, String planoption, String premiumPayoption, String WholeLifeoption, String smokertype, String age, String gender, String policyTerm, String PPT, String premFreq, String sumassured, String isBetterHalfBenifit, String BetterHalfBenifitAge, String BetterHalfBenifitsmokartype, String BetterHalfBenifitGender, String Channel, String isadb, String adbterm, String adbsumassured, String isatpdb, String atpdbterm, String atpdbsumassured, String KFC) {
        eshieldNextBean eshieldNextBean = new eshieldNextBean();
        eshieldNextBusinessLogic = new eshieldNextBusinessLogic(eshieldNextBean);
        cfap = new CommonForAllProd();
        eshieldNextBean.setStaff(Boolean.parseBoolean(Staff));
        eshieldNextBean.setSmoker(smokertype);
        //eshieldNextBean.setBTSmoker(BetterHalfBenifitGender);
        eshieldNextBean.setGender_spouse(BetterHalfBenifitsmokartype);
        eshieldNextBean.setState(Boolean.parseBoolean(KFC));
        eshieldNextBean.setServiceTax(Boolean.parseBoolean(KFC));
        eshieldNextBean.setAge(Integer.parseInt(age));
        eshieldNextBean.setGender(gender);
        eshieldNextBean.setGender_spouse(BetterHalfBenifitGender);
        eshieldNextBean.setPolicyterm(Integer.parseInt(policyTerm));
        if (String_PremiumPaymentTerm.equalsIgnoreCase("PT less 5")) {
            eshieldNextBean.setPPT(6);
        } else {
            eshieldNextBean.setPPT(Integer.parseInt(PPT));
        }


        eshieldNextBean.setPremiumFrequency(premFreq);
        eshieldNextBean.setPlanoption(planoption);
        //System.out.println("planoption "+planoption);
        eshieldNextBean.setPremiumPayoption(premiumPayoption);
        eshieldNextBean.setWholeLifeoption(WholeLifeoption);
        eshieldNextBean.setChannel(Channel);
        eshieldNextBean.setSumassured(Double.parseDouble(sumassured));
        String underwriting = "";
        eshieldNextBean.setUnderwriting(underwriting);
        eshieldNextBean.setBetterHalfBenifit(String_BetterHalfBenefits.equalsIgnoreCase("Yes"));
        eshieldNextBean.setSmoker_spouse(BetterHalfBenifitsmokartype);
        // eshieldNextBean.setBetterHalfBenifit(Boolean.parseBoolean(isBetterHalfBenifit));
        eshieldNextBean.setBetterHalfBenifitAge(Integer.parseInt(BetterHalfBenifitAge));
        //	eshieldNextBean.setisadb(isadb);
        //	eshieldNextBean.setisatpdb(isatpdb);

        if (isadb.equalsIgnoreCase("true")) {
            eshieldNextBean.setPolicyTerm_ADB(Integer.parseInt(adbterm));
            eshieldNextBean.setSumAssured_ADB(Integer.parseInt(adbsumassured));
        } else {
            eshieldNextBean.setPolicyTerm_ADB(Integer.parseInt("0"));
            eshieldNextBean.setSumAssured_ADB(Integer.parseInt("0"));
        }
        //ATPDB Rider
        if (isatpdb.equalsIgnoreCase("true")) {
            eshieldNextBean.setPolicyTerm_ATPDB(Integer.parseInt(atpdbterm));
            eshieldNextBean.setSumAssured_ATPDB(Integer.parseInt(atpdbsumassured));
        } else {
            eshieldNextBean.setPolicyTerm_ATPDB(Integer.parseInt("0"));
            eshieldNextBean.setSumAssured_ATPDB(Integer.parseInt("0"));
        }

        return showEShieldOutputPg(eshieldNextBean);


    }

    private boolean showEShieldOutputPg(eshieldNextBean eshieldNextBean) {

        String[] outputArr = getOutput(eshieldNextBean);

        boolean valRiderPremiumError = valRiderPremium(Double.parseDouble(basicpremium), sumOfRiders);
        boolean valMinPremiumError = valInstPremium(basicpremium, String_PremiumFrequency);
        // valRiderPremiumError = true;
        //valMinPremiumError = true;
        try {
            retVal.append("<?xml version='1.0' encoding='utf-8' ?><EShield>");
            if (valRiderPremiumError && valMinPremiumError) {
                retVal.append("<errCode>0</errCode>");
                retVal.append("<basicpremium>").append(outputArr[0]).append("</basicpremium>");
                retVal.append("<BetterHalfBenefitPremium>").append(outputArr[16]).append("</BetterHalfBenefitPremium>");
                retVal.append("<BetterHalfBenefitSumassured>").append(outputArr[20]).append("</BetterHalfBenefitSumassured>");
                retVal.append("<Levelbasicpremium>").append(outputArr[1]).append("</Levelbasicpremium>");
                retVal.append("<Increasebasicpremium>").append(outputArr[2]).append("</Increasebasicpremium>");
                retVal.append("<adbpremium>").append(outputArr[3]).append("</adbpremium>");
                retVal.append("<atpdbpremium>").append(outputArr[4]).append("</atpdbpremium>");
                retVal.append("<totalpremiumwithoutST>").append(outputArr[5]).append("</totalpremiumwithoutST>");
                retVal.append("<BasicServicetax>").append(outputArr[6]).append("</BasicServicetax>");
                retVal.append("<SBC>").append(outputArr[7]).append("</SBC>");
                retVal.append("<KKC>").append(outputArr[8]).append("</KKC>");
                retVal.append("<ServiceTax>").append(outputArr[9]).append("</ServiceTax>");
                retVal.append("<PremiumwithST>").append(outputArr[10]).append("</PremiumwithST>");
                retVal.append("<PA_Premium_LC>").append(outputArr[11]).append("</PA_Premium_LC>");
                retVal.append("<PA_Premium_IC>").append(outputArr[12]).append("</PA_Premium_IC>");
                retVal.append("<PA_Premium_ADB>").append(outputArr[13]).append("</PA_Premium_ADB>");
                retVal.append("<PA_Premium_APPDB>").append(outputArr[14]).append("</PA_Premium_APPDB>");
                retVal.append("<SumAssured>").append(outputArr[15]).append("</SumAssured>");
                retVal.append("<InstallmentPremiumWithChannelDiscount>").append(outputArr[17]).append("</InstallmentPremiumWithChannelDiscount>");
                retVal.append("<InstallmentPremiumWithStaffDiscount>").append(outputArr[19]).append("</InstallmentPremiumWithStaffDiscount>");

                retVal.append("<YearlyInstPremWithTaxes>").append(outputArr[18]).append("</YearlyInstPremWithTaxes>");

                retVal.append("<staffStatus>").append(staffStatus).append("</staffStatus>");
                retVal.append("<staffRebate>").append(staffRebate).append("</staffRebate>");
                retVal.append("</EShield>");
                System.out.println("retVal = " + retVal.toString());
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            retVal.append("<?xml version='1.0' encoding='utf-8' ?><EShield>" + "<errCode>1</errCode>" + "<errorMessage>").append(e.getMessage()).append("</errorMessage></EShield>");
            return false;
        }
    }

    private String[] getOutput(eshieldNextBean eshieldNextBean) {
        if (CheckBox_StaffDiscount.isChecked()) {
            staffStatus = "sbi";
            // disc_Basic_SelFreq
        } else {
            staffStatus = "none";
        }
        staffRebate = eshieldNextBusinessLogic
                .getStaffRebate(eshieldNextBean.getPlanoption(), CheckBox_StaffDiscount.isChecked()) + "";
        try {
            String basicpremiumrate = cfap.getStringWithout_E(eshieldNextBusinessLogic.getbasicpremiumrate(eshieldNextBean.getGender(), eshieldNextBean.getPlanoption(), eshieldNextBean.getPremiumPayoption(), eshieldNextBean.getSmoker(), eshieldNextBean.getSumassured(), eshieldNextBean.getAge(), eshieldNextBean.getPolicyterm(), eshieldNextBean.getPPT(), eshieldNextBean.getWholeLifeoption()));
            String basicpremiumrateLevel = cfap.getStringWithout_E(eshieldNextBusinessLogic.getbasicpremiumrate(eshieldNextBean.getGender(), eshieldNextBean.getPlanoption(), eshieldNextBean.getPremiumPayoption(), eshieldNextBean.getSmoker(), eshieldNextBean.getSumassured(), eshieldNextBean.getAge(), eshieldNextBean.getPolicyterm(), eshieldNextBean.getPPT(), eshieldNextBean.getWholeLifeoption()));
            //basicpremiumrateLevel = cfap.getStringWithout_E(eshieldNextBusinessLogic.getbasicpremiumrate(eshieldNextBean.getGender(), "Level Cover",eshieldNextBean.getPremiumPayoption(),eshieldNextBean.getSmoker(), eshieldNextBean.getSumassured(), eshieldNextBean.getAge(), eshieldNextBean.getPolicyterm(),eshieldNextBean.getPPT(),eshieldNextBean.getWholeLifeoption()));
            String basicpremiumrateIncrease = cfap.getStringWithout_E(eshieldNextBusinessLogic.getbasicpremiumrate(eshieldNextBean.getGender(), eshieldNextBean.getPlanoption(), eshieldNextBean.getPremiumPayoption(), eshieldNextBean.getSmoker(), eshieldNextBean.getSumassured(), eshieldNextBean.getAge(), eshieldNextBean.getPolicyterm(), eshieldNextBean.getPPT(), eshieldNextBean.getWholeLifeoption()));
            //basicpremiumrateIncrease = cfap.getStringWithout_E(eshieldNextBusinessLogic.getbasicpremiumrate(eshieldNextBean.getGender(), "Increasing Cover",eshieldNextBean.getPremiumPayoption(), eshieldNextBean.getSmoker(), eshieldNextBean.getSumassured(), eshieldNextBean.getAge(), eshieldNextBean.getPolicyterm(),eshieldNextBean.getPPT(),eshieldNextBean.getWholeLifeoption()));
            basicpremium1 = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(eshieldNextBusinessLogic.getbasicpremium(eshieldNextBean.getPremiumFrequency(), eshieldNextBean.getSumassured(), basicpremiumrate)));
            basicpremium = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(eshieldNextBusinessLogic.getbasicpremium(eshieldNextBean.getPremiumFrequency(), eshieldNextBean.getSumassured(), basicpremiumrate)));
            levelbasicpremium = cfap.getRoundUp(cfap.getStringWithout_E(eshieldNextBusinessLogic.getLevelbasicpremium(eshieldNextBean.getPremiumFrequency(), eshieldNextBean.getSumassured(), basicpremiumrateLevel)));
//		System.out.println("levelbasicpremium "+levelbasicpremium);
            increasebasicpremium = cfap.getRoundUp(cfap.getStringWithout_E(eshieldNextBusinessLogic.getIncreasebasicpremium(eshieldNextBean.getPremiumFrequency(), eshieldNextBean.getSumassured(), basicpremiumrateIncrease)));

            adbpremium = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(eshieldNextBusinessLogic.getadbpremium(eshieldNextBean.getPremiumFrequency(), eshieldNextBean.getSumAssured_ADB()
                    , eshieldNextBean.getAge()
                    , eshieldNextBean.getPolicyTerm_ADB(), eshieldNextBean.getPremiumPayoption(), eshieldNextBean.getPPT())));

            atpdbpremium = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(eshieldNextBusinessLogic.getatpdbpremium(eshieldNextBean.getPremiumFrequency(), eshieldNextBean.getSumAssured_ATPDB()
                    , eshieldNextBean.getAge(), eshieldNextBean.getPolicyTerm_ATPDB(), eshieldNextBean.getPremiumPayoption(), eshieldNextBean.getPPT())));
            String betterHalfBenefitPremRate = cfap.getStringWithout_E(eshieldNextBusinessLogic.getBetterHalfBenefitPtemiumRate(eshieldNextBean.getGender_spouse(), eshieldNextBean.getPlanoption(), eshieldNextBean.getPremiumPayoption(), eshieldNextBean.getSmoker_spouse(), eshieldNextBean.getSumassured(), eshieldNextBean.getBetterHalfBenifitAge(), eshieldNextBean.getPolicyterm(), eshieldNextBean.getPPT(), eshieldNextBean.getWholeLifeoption()));

            if (eshieldNextBean.getBetterHalfBenifit()) {
                BetterHalfBenefitPrem = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(eshieldNextBusinessLogic.getBetterHalfBenefitPtemium(eshieldNextBean.getPremiumFrequency(), eshieldNextBean.getSumassured(), betterHalfBenefitPremRate)));
            } else {
                BetterHalfBenefitPrem = "0";

            }
            if (eshieldNextBean.getBetterHalfBenifit())
                BetterHalfBenefitSum = "2500000";
            else
                BetterHalfBenefitSum = "0";
            System.out.println("BetterHalfBenefitSum " + BetterHalfBenefitSum);

            totalpremiumwithoutST = cfap.getRoundUp(cfap.getStringWithout_E(eshieldNextBusinessLogic.gettotalpremiumwithoutST(basicpremium, adbpremium, atpdbpremium, BetterHalfBenefitPrem)));

            String basicChannelDiscount = cfap.getStringWithout_E(eshieldNextBusinessLogic.getBasicChannelDiscount(eshieldNextBean.getStaff(), eshieldNextBean.getChannel(), Double.parseDouble(basicpremium1), eshieldNextBean.getPremiumPayoption(), eshieldNextBean.getSumassured(), Double.parseDouble(basicpremiumrate)));
            String ADBChannelDiscount = cfap.getStringWithout_E(eshieldNextBusinessLogic.getADBChannelDiscount(eshieldNextBean.getStaff(), eshieldNextBean.getChannel(), Double.parseDouble(adbpremium), eshieldNextBean.getPremiumPayoption(), eshieldNextBean.getSumAssured_ADB(), Double.parseDouble(basicpremiumrate), eshieldNextBean.getPolicyTerm_ADB(), eshieldNextBean.getPPT()));
            String ATPDBChannelDiscount = cfap.getStringWithout_E(eshieldNextBusinessLogic.getATPDBChannelDiscount(eshieldNextBean.getStaff(), eshieldNextBean.getChannel(), Double.parseDouble(atpdbpremium), eshieldNextBean.getPremiumPayoption(), eshieldNextBean.getSumAssured_ATPDB(), Double.parseDouble(basicpremiumrate), eshieldNextBean.getPolicyTerm_ATPDB(), eshieldNextBean.getPPT()));
            String betterHalfChannelDiscount = cfap.getStringWithout_E(eshieldNextBusinessLogic.getBetterHalfChannelDiscount(eshieldNextBean.getStaff(), eshieldNextBean.getChannel(), Double.parseDouble(BetterHalfBenefitPrem), eshieldNextBean.getPremiumPayoption(), Double.parseDouble(betterHalfBenefitPremRate)));

            String basicFrequencyloading = cfap.getStringWithout_E(eshieldNextBusinessLogic.getbasicFrequencyLoading(eshieldNextBean.getPremiumFrequency(), Double.parseDouble(basicpremium1), Double.parseDouble(basicChannelDiscount), eshieldNextBean.getSumassured(), Double.parseDouble(basicpremiumrate)));
            String ADBFrequencyLoading = cfap.getStringWithout_E(eshieldNextBusinessLogic.getADBFrequencyLoading(eshieldNextBean.getPremiumFrequency(), Double.parseDouble(adbpremium), Double.parseDouble(ADBChannelDiscount), eshieldNextBean.getSumAssured_ADB(), eshieldNextBean.getPremiumPayoption(), eshieldNextBean.getPolicyTerm_ADB(), eshieldNextBean.getPPT()));
            String ATPDBFrquencyloading = cfap.getStringWithout_E(eshieldNextBusinessLogic.getATPDBFrequencyLoading(eshieldNextBean.getPremiumFrequency(), Double.parseDouble(atpdbpremium), Double.parseDouble(ATPDBChannelDiscount), eshieldNextBean.getSumAssured_ATPDB(), eshieldNextBean.getPremiumPayoption(), eshieldNextBean.getPolicyTerm_ATPDB(), eshieldNextBean.getPPT()));

            String betterHalfFrequencyLoading = "";
            if (eshieldNextBean.getBetterHalfBenifit()) {
                betterHalfFrequencyLoading = cfap.getStringWithout_E(eshieldNextBusinessLogic.getBetterHalfFrequencyLoading(eshieldNextBean.getPremiumFrequency(), Double.parseDouble(BetterHalfBenefitPrem), Double.parseDouble(betterHalfChannelDiscount), Double.parseDouble(betterHalfBenefitPremRate)));
            } else {
                betterHalfFrequencyLoading = "0";
            }

            InstallmentPremiumChannelDiscount = cfap.getRoundUp(cfap.getStringWithout_E(eshieldNextBusinessLogic.getinstallmentPremWithChannelDis(eshieldNextBean.getStaff(), eshieldNextBean.getChannel())));
            double staffdiscount = (Double.parseDouble(basicpremium1) + Double.parseDouble(adbpremium) + Double.parseDouble(atpdbpremium) + Double.parseDouble(BetterHalfBenefitPrem));
            String InstStaff = cfap.getRoundUp(cfap.getStringWithout_E(staffdiscount));


            String bsicCover = cfap.getStringWithout_E(eshieldNextBusinessLogic.getbasicCover(eshieldNextBean.getSumassured(), Double.parseDouble(basicpremiumrate)));
            String basicStaffDiscount = cfap.getStringWithout_E(eshieldNextBusinessLogic.getStaffDisc(eshieldNextBean.getPremiumPayoption(), eshieldNextBean.getStaff(), Double.parseDouble(basicpremium1), eshieldNextBean.getPolicyterm(), eshieldNextBean.getSumassured(), Double.parseDouble(basicpremiumrate)));
            String ADBStaffDiscount = cfap.getStringWithout_E(eshieldNextBusinessLogic.getADBStaffDisc(eshieldNextBean.getPremiumPayoption(), eshieldNextBean.getStaff(), Double.parseDouble(adbpremium), eshieldNextBean.getPolicyTerm_ADB(), eshieldNextBean.getSumAssured_ADB(), eshieldNextBean.getPPT()));
            String ATPDBStaffDiscount = cfap.getStringWithout_E(eshieldNextBusinessLogic.getATPDBStaffDisc(eshieldNextBean.getPremiumPayoption(), eshieldNextBean.getStaff(), Double.parseDouble(atpdbpremium), eshieldNextBean.getPolicyTerm_ATPDB(), eshieldNextBean.getSumAssured_ATPDB(), eshieldNextBean.getPPT()));
            String betterHalfStaffDiscount = cfap.getStringWithout_E(eshieldNextBusinessLogic.getbetterhalfStaffDisc(eshieldNextBean.getPremiumPayoption(), eshieldNextBean.getStaff(), Double.parseDouble(BetterHalfBenefitPrem), eshieldNextBean.getPolicyterm()));

//		System.out.println("\nBasicStaffDiscount "+BasicStaffDiscount);
//		System.out.println("ADBStaffDiscount "+ADBStaffDiscount);
//		System.out.println("ATPDBStaffDiscount "+ATPDBStaffDiscount);
//		System.out.println("BetterHalfStaffDiscount "+BetterHalfStaffDiscount);

            String basicFrequencyLoadingStaff = cfap.getStringWithout_E(eshieldNextBusinessLogic.getbasicFrequencyLoadingStaffDiscount(eshieldNextBean.getPremiumFrequency(), Double.parseDouble(bsicCover), Double.parseDouble(basicStaffDiscount)));
            String ADBFrequencyLoadingStaff = cfap.getStringWithout_E(eshieldNextBusinessLogic.getADBFrequencyLoadingStaffDiscount(eshieldNextBean.getPremiumFrequency(), Double.parseDouble(adbpremium), Double.parseDouble(ADBStaffDiscount), eshieldNextBean.getSumAssured_ADB(), eshieldNextBean.getPremiumPayoption(), eshieldNextBean.getPolicyTerm_ADB(), eshieldNextBean.getPPT()));
            String ATPDBFrequencyLoadingStaff = cfap.getStringWithout_E(eshieldNextBusinessLogic.getATPDBFrequencyLoadingStaffDiscount(eshieldNextBean.getPremiumFrequency(), Double.parseDouble(atpdbpremium), Double.parseDouble(ATPDBStaffDiscount), eshieldNextBean.getSumAssured_ATPDB(), eshieldNextBean.getPremiumPayoption(), eshieldNextBean.getPolicyTerm_ATPDB(), eshieldNextBean.getPPT()));
            String betterHalfFrequencyLoadingStaff = "";
            if (eshieldNextBean.getBetterHalfBenifit()) {
                betterHalfFrequencyLoadingStaff = cfap.getStringWithout_E(eshieldNextBusinessLogic.getBetterhalfFrequencyLoadingStaffDiscount(eshieldNextBean.getPremiumFrequency(), Double.parseDouble(BetterHalfBenefitPrem), Double.parseDouble(betterHalfStaffDiscount)));
            } else {
                betterHalfFrequencyLoadingStaff = "0";
            }
            //	InstallmentpremiumwithStaffDiscount =(BasicFrequencyLoadingStaff+ADBFrequencyLoadingStaff+ATPDBFrequencyLoadingStaff+BetterHalfFrequencyLoadingStaff);
            //	InstallmentpremiumwithStaffDiscount =  cfap.getRoundUp(cfap.getStringWithout_E(eshieldNextBusinessLogic.getinstallmentPremWithStaffDis(eshieldNextBean.getStaff())));
            InstallmentpremiumwithStaffDiscount = cfap.getRoundUp(cfap.getStringWithout_E(Double.parseDouble(basicFrequencyLoadingStaff) + Double.parseDouble(ADBFrequencyLoadingStaff) + Double.parseDouble(ATPDBFrequencyLoadingStaff) + Double.parseDouble(betterHalfFrequencyLoadingStaff)));
//		System.out.println("BasicFrequencyLoadingStaff1 "+BasicFrequencyLoadingStaff);
//		System.out.println("ADBFrequencyLoadingStaff2  "+ADBFrequencyLoadingStaff);
//		System.out.println("ATPDBFrequencyLoadingStaff3 "+ATPDBFrequencyLoadingStaff);
//		System.out.println("BetterHalfFrequencyLoadingStaff4 "+BetterHalfFrequencyLoadingStaff);
//
//
//		System.out.println("InstallmentpremiumwithStaffDiscount "+InstallmentpremiumwithStaffDiscount);
//
            BasicServicetax = cfap.getStringWithout_E(eshieldNextBusinessLogic.getBasicServiceTax(eshieldNextBean.getStaff(), eshieldNextBean.getState(), eshieldNextBean.getChannel(), Double.parseDouble(InstallmentPremiumChannelDiscount), Double.parseDouble(InstallmentpremiumwithStaffDiscount)));
//		System.out.println("BasicServicetax "+BasicServicetax);

            SBC = cfap.getStringWithout_E(eshieldNextBusinessLogic.getSBC(eshieldNextBean.getServiceTax()));
            KKC = cfap.getStringWithout_E(eshieldNextBusinessLogic.getKKC());

            ServiceTax = cfap.getStringWithout_E(eshieldNextBusinessLogic.getServiceTax(eshieldNextBean.getStaff(), eshieldNextBean.getChannel()));
            //System.out.println("ServiceTax "+ServiceTax);


            YearlyInstPremWithTaxes = cfap.getRoundUp(cfap.getStringWithout_E(eshieldNextBusinessLogic.getYearlyinstWithTaxes(eshieldNextBean.getStaff(), eshieldNextBean.getChannel(), Double.parseDouble(InstallmentPremiumChannelDiscount), Double.parseDouble(InstallmentpremiumwithStaffDiscount))));
            PremiumwithST = cfap.getStringWithout_E(eshieldNextBusinessLogic.getPremiumwithST(totalpremiumwithoutST));
            sumOfRiders = Double.parseDouble(adbpremium) + Double.parseDouble(atpdbpremium);
            PA_Premium_LC = cfap.getStringWithout_E((eshieldNextBean.getSumassured() * Double.parseDouble(basicpremiumrateLevel)) / 1000);
            PA_Premium_IC = cfap.getStringWithout_E((eshieldNextBean.getSumassured() * Double.parseDouble(basicpremiumrateIncrease)) / 1000);

            PA_Premium_ADB = cfap.getRoundOffLevel2(cfap.getStringWithout_E((eshieldNextBean.getSumAssured_ADB() * 50) / 100000));
            PA_Premium_APPDB = cfap.getRoundOffLevel2(cfap.getStringWithout_E((eshieldNextBean.getSumAssured_ATPDB() * 40) / 100000));
            Sumassured = cfap.getStringWithout_E(eshieldNextBean.getSumassured());


        } catch (Exception e) {
            e.printStackTrace();
        }

        return new String[]
                {
                        //	basicpremium,
                        basicpremium1,
                        levelbasicpremium,
                        increasebasicpremium,
                        adbpremium,
                        atpdbpremium,
                        totalpremiumwithoutST,
                        BasicServicetax,
                        SBC,
                        KKC,
                        ServiceTax,
                        PremiumwithST,
                        PA_Premium_LC,
                        PA_Premium_IC,
                        PA_Premium_ADB,
                        PA_Premium_APPDB,
                        Sumassured,
                        BetterHalfBenefitPrem,
                        InstallmentPremiumChannelDiscount,
                        YearlyInstPremWithTaxes,
                        InstallmentpremiumwithStaffDiscount, BetterHalfBenefitSum
                };
    }

    public void createPdf() {
        try {
            float[] columnWidths2 = {2f, 1f};
            float[] columnWidths4 = {2f, 1f, 2f, 1f};
            mypath = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "BI.pdf");
            needAnalysispath = mStorageUtils.createFileToAppSpecificDir(context, "NA.pdf");
            // needAnalysis_Tabular_path = new File(folder, "NA_Tabular.pdf");
            newFile = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "P01.pdf");

            Rectangle rect = new Rectangle(594f, 792f);

            Document document = new Document(rect, 50, 50, 50, 50);
            // Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter pdf_writer = null;
            pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(
                    mypath.getAbsolutePath()));

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


            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 11,
                    Font.BOLD, BaseColor.BLACK);
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);
            Font small_bold_for_name = new Font(Font.FontFamily.TIMES_ROMAN,
                    10, Font.BOLD);
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.NORMAL);
            Font sub_headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 7,
                    Font.BOLD);
            Font small_bold1 = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.BOLD);
            Font white_headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.BOLD, BaseColor.WHITE);
            Font small_normal_biFont = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.NORMAL);
            Font big_normal_biFont = new Font(Font.FontFamily.TIMES_ROMAN, 11,
                    Font.BOLD);
            Font medium_normal_underline_biFont = new Font(
                    Font.FontFamily.TIMES_ROMAN, 10, Font.UNDERLINE);
            Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD,
                    BaseColor.RED);
            Font small_normal1 = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.NORMAL);


            Paragraph Para_Header1 = new Paragraph(
                    "SBI Life Insurance Co. Ltd \nRegistered & Corporate Office: 'Natraj', M.V. Road and Western Express, Highway Junction, Andheri(East),\n Mumbai 400069.IRDAI Regn No. 111"
                    , headerBold);
            Para_Header1.setAlignment(Element.ALIGN_CENTER);


            Paragraph Para_Header2 = new Paragraph(
                    "Website: www.sbilife.co.in | Email: info@sbilife.co.in | CIN: L99999MH2000PLC129113.\n Toll Free: 1800 267 9090 (Between 9.00 am & 9.00 pm) \n Premium Quotation \n SBI Life - eShield Next (111N132V01) | An Individual, Non-Linked, Non-Participating Life Insurance Pure Risk Premium Product"
                    , headerBold);

            Para_Header2.setAlignment(Element.ALIGN_CENTER);

            document.add(Para_Header1);
            document.add(Para_Header2);


            document.add(para_img_logo_after_space_1);


            PdfPTable EShieldInfoTable = new PdfPTable(2);
            EShieldInfoTable.setWidthPercentage(100);

            PdfPCell Product = new PdfPCell(new Paragraph("Product"));
            Product.setHorizontalAlignment(Element.ALIGN_LEFT);
            Product.setPadding(5);
            EShieldInfoTable.addCell(Product);

            PdfPCell VarProduct = new PdfPCell(new Paragraph("SBI Life - eShield Next"));
            VarProduct.setHorizontalAlignment(Element.ALIGN_LEFT);
            VarProduct.setPadding(5);
            EShieldInfoTable.addCell(VarProduct);

            PdfPCell Plan = new PdfPCell(new Paragraph("Plan"));
            Plan.setHorizontalAlignment(Element.ALIGN_LEFT);
            Plan.setPadding(5);
            EShieldInfoTable.addCell(Plan);

            PdfPCell VarPlan = new PdfPCell(new Paragraph(String_Plan));
            VarPlan.setHorizontalAlignment(Element.ALIGN_LEFT);
            VarPlan.setPadding(5);
            EShieldInfoTable.addCell(VarPlan);

            PdfPCell Age = new PdfPCell(new Paragraph("Age"));
            Age.setHorizontalAlignment(Element.ALIGN_LEFT);
            Age.setPadding(5);
            EShieldInfoTable.addCell(Age);

            PdfPCell VarAge = new PdfPCell(new Paragraph(ProposerAge));
            VarAge.setHorizontalAlignment(Element.ALIGN_LEFT);
            VarAge.setPadding(5);
            EShieldInfoTable.addCell(VarAge);


            PdfPCell Gender = new PdfPCell(new Paragraph("Gender"));
            Gender.setHorizontalAlignment(Element.ALIGN_LEFT);
            Gender.setPadding(5);
            EShieldInfoTable.addCell(Gender);

            PdfPCell VarGender = new PdfPCell(new Paragraph(gender));
            VarGender.setHorizontalAlignment(Element.ALIGN_LEFT);
            VarGender.setPadding(5);
            EShieldInfoTable.addCell(VarGender);


            PdfPCell Smoker = new PdfPCell(new Paragraph("Smoker/Non Smoker"));
            Smoker.setHorizontalAlignment(Element.ALIGN_LEFT);
            Smoker.setPadding(5);
            EShieldInfoTable.addCell(Smoker);

            PdfPCell VarSmoker = new PdfPCell(new Paragraph(String_Smoker));
            VarSmoker.setHorizontalAlignment(Element.ALIGN_LEFT);
            VarSmoker.setPadding(5);
            EShieldInfoTable.addCell(VarSmoker);

            PdfPCell PremiumPaymentOption = new PdfPCell(new Paragraph("Premium Payment Option"));
            PremiumPaymentOption.setHorizontalAlignment(Element.ALIGN_LEFT);
            PremiumPaymentOption.setPadding(5);
            EShieldInfoTable.addCell(PremiumPaymentOption);

            PdfPCell VarPremiumPaymentOption = new PdfPCell(new Paragraph(String_PremiumPaymentOption + " Premium"));
            VarPremiumPaymentOption.setHorizontalAlignment(Element.ALIGN_LEFT);
            VarPremiumPaymentOption.setPadding(5);
            EShieldInfoTable.addCell(VarPremiumPaymentOption);


            PdfPCell Frequency = new PdfPCell(new Paragraph("Frequency"));
            Frequency.setHorizontalAlignment(Element.ALIGN_LEFT);
            Frequency.setPadding(5);
            EShieldInfoTable.addCell(Frequency);

            PdfPCell VarFrequency = new PdfPCell(new Paragraph(String_PremiumFrequency));
            VarFrequency.setHorizontalAlignment(Element.ALIGN_LEFT);
            VarFrequency.setPadding(5);
            EShieldInfoTable.addCell(VarFrequency);


            PdfPCell DeathBenefitPaymentMode = new PdfPCell(new Paragraph("Death Benefit Payment Mode"));
            DeathBenefitPaymentMode.setHorizontalAlignment(Element.ALIGN_LEFT);
            DeathBenefitPaymentMode.setPadding(5);
            EShieldInfoTable.addCell(DeathBenefitPaymentMode);

            PdfPCell VarDeathBenefitPaymentMode = new PdfPCell(new Paragraph(String_DeathBenefitsPaymentOption));
            VarDeathBenefitPaymentMode.setHorizontalAlignment(Element.ALIGN_LEFT);
            VarDeathBenefitPaymentMode.setPadding(5);
            EShieldInfoTable.addCell(VarDeathBenefitPaymentMode);
            if (String_BetterHalfBenefits.equalsIgnoreCase("Yes")) {

                PdfPCell BetterHalfBenefit = new PdfPCell(new Paragraph("Better Half Benefit(BHB)"));
                BetterHalfBenefit.setHorizontalAlignment(Element.ALIGN_LEFT);
                BetterHalfBenefit.setPadding(5);
                EShieldInfoTable.addCell(BetterHalfBenefit);

                PdfPCell VarBetterHalfBenefit = new PdfPCell(new Paragraph(String_BetterHalfBenefits));
                VarBetterHalfBenefit.setHorizontalAlignment(Element.ALIGN_LEFT);
                VarBetterHalfBenefit.setPadding(5);
                EShieldInfoTable.addCell(VarBetterHalfBenefit);


                PdfPCell NameOfSpouse = new PdfPCell(new Paragraph("Name Of Spouse"));
                NameOfSpouse.setHorizontalAlignment(Element.ALIGN_LEFT);
                NameOfSpouse.setPadding(5);
                EShieldInfoTable.addCell(NameOfSpouse);

                PdfPCell VarNameOfSpouse = new PdfPCell(new Paragraph(name_of_spouse));
                VarNameOfSpouse.setHorizontalAlignment(Element.ALIGN_LEFT);
                VarNameOfSpouse.setPadding(5);
                EShieldInfoTable.addCell(VarNameOfSpouse);


                PdfPCell AgeOfSpouse = new PdfPCell(new Paragraph("Age of Spouse"));
                AgeOfSpouse.setHorizontalAlignment(Element.ALIGN_LEFT);
                AgeOfSpouse.setPadding(5);
                EShieldInfoTable.addCell(AgeOfSpouse);

                PdfPCell VarAgeOfSpouse = new PdfPCell(new Paragraph(SpouseAge));
                VarAgeOfSpouse.setHorizontalAlignment(Element.ALIGN_LEFT);
                VarAgeOfSpouse.setPadding(5);
                EShieldInfoTable.addCell(VarAgeOfSpouse);


                PdfPCell SmokerSpouse = new PdfPCell(new Paragraph("Smoker/Non Smoker of Spouse"));
                SmokerSpouse.setHorizontalAlignment(Element.ALIGN_LEFT);
                SmokerSpouse.setPadding(5);
                EShieldInfoTable.addCell(SmokerSpouse);

                PdfPCell VarSmokerSpouse = new PdfPCell(new Paragraph(String_Smoker_Spouse));
                VarSmokerSpouse.setHorizontalAlignment(Element.ALIGN_LEFT);
                VarSmokerSpouse.setPadding(5);
                EShieldInfoTable.addCell(VarSmokerSpouse);


                PdfPCell GenderSpouse = new PdfPCell(new Paragraph("Gender of Spouse"));
                GenderSpouse.setHorizontalAlignment(Element.ALIGN_LEFT);
                GenderSpouse.setPadding(5);
                EShieldInfoTable.addCell(GenderSpouse);

                PdfPCell VarGenderSpouse = new PdfPCell(new Paragraph(String_Gender_spouse));
                VarGenderSpouse.setHorizontalAlignment(Element.ALIGN_LEFT);
                VarGenderSpouse.setPadding(5);
                EShieldInfoTable.addCell(VarGenderSpouse);
            } else {

                PdfPCell BetterHalfBenefit = new PdfPCell(new Paragraph("Better Half Benefit(BHB)"));
                BetterHalfBenefit.setHorizontalAlignment(Element.ALIGN_LEFT);
                BetterHalfBenefit.setPadding(5);
                EShieldInfoTable.addCell(BetterHalfBenefit);

                PdfPCell VarBetterHalfBenefit = new PdfPCell(new Paragraph(String_BetterHalfBenefits));
                VarBetterHalfBenefit.setHorizontalAlignment(Element.ALIGN_LEFT);
                VarBetterHalfBenefit.setPadding(5);
                EShieldInfoTable.addCell(VarBetterHalfBenefit);


                PdfPCell NameOfSpouse = new PdfPCell(new Paragraph("Name Of Spouse"));
                NameOfSpouse.setHorizontalAlignment(Element.ALIGN_LEFT);
                NameOfSpouse.setPadding(5);
                EShieldInfoTable.addCell(NameOfSpouse);

                PdfPCell VarNameOfSpouse = new PdfPCell(new Paragraph("-"));
                VarNameOfSpouse.setHorizontalAlignment(Element.ALIGN_LEFT);
                VarNameOfSpouse.setPadding(5);
                EShieldInfoTable.addCell(VarNameOfSpouse);


                PdfPCell AgeOfSpouse = new PdfPCell(new Paragraph("Age of Spouse"));
                AgeOfSpouse.setHorizontalAlignment(Element.ALIGN_LEFT);
                AgeOfSpouse.setPadding(5);
                EShieldInfoTable.addCell(AgeOfSpouse);

                PdfPCell VarAgeOfSpouse = new PdfPCell(new Paragraph("-"));
                VarAgeOfSpouse.setHorizontalAlignment(Element.ALIGN_LEFT);
                VarAgeOfSpouse.setPadding(5);
                EShieldInfoTable.addCell(VarAgeOfSpouse);


                PdfPCell SmokerSpouse = new PdfPCell(new Paragraph("Smoker/Non Smoker of Spouse"));
                SmokerSpouse.setHorizontalAlignment(Element.ALIGN_LEFT);
                SmokerSpouse.setPadding(5);
                EShieldInfoTable.addCell(SmokerSpouse);

                PdfPCell VarSmokerSpouse = new PdfPCell(new Paragraph("-"));
                VarSmokerSpouse.setHorizontalAlignment(Element.ALIGN_LEFT);
                VarSmokerSpouse.setPadding(5);
                EShieldInfoTable.addCell(VarSmokerSpouse);


                PdfPCell GenderSpouse = new PdfPCell(new Paragraph("Gender of Spouse"));
                GenderSpouse.setHorizontalAlignment(Element.ALIGN_LEFT);
                GenderSpouse.setPadding(5);
                EShieldInfoTable.addCell(GenderSpouse);

                PdfPCell VarGenderSpouse = new PdfPCell(new Paragraph("-"));
                VarGenderSpouse.setHorizontalAlignment(Element.ALIGN_LEFT);
                VarGenderSpouse.setPadding(5);
                EShieldInfoTable.addCell(VarGenderSpouse);
            }

            document.add(EShieldInfoTable);

            document.add(para_img_logo_after_space_1);


            PdfPTable EShieldBasicCoverTable = new PdfPTable(5);
            EShieldBasicCoverTable.setWidthPercentage(100);

            PdfPCell Spacer = new PdfPCell(new Paragraph(""));
            Spacer.setHorizontalAlignment(Element.ALIGN_LEFT);
            Spacer.setPadding(5);
            EShieldBasicCoverTable.addCell(Spacer);

            PdfPCell PolicyTerm = new PdfPCell(new Paragraph("PolicyTerm (Years)"));
            PolicyTerm.setHorizontalAlignment(Element.ALIGN_LEFT);
            PolicyTerm.setPadding(5);
            EShieldBasicCoverTable.addCell(PolicyTerm);

            PdfPCell PremiumPaymentTerms = new PdfPCell(new Paragraph("Premium Payment Term (Years)"));
            PremiumPaymentTerms.setHorizontalAlignment(Element.ALIGN_LEFT);
            PremiumPaymentTerms.setPadding(5);
            EShieldBasicCoverTable.addCell(PremiumPaymentTerms);

            PdfPCell SumAssured = new PdfPCell(new Paragraph("Sum Assured (Rs.)"));
            SumAssured.setHorizontalAlignment(Element.ALIGN_LEFT);
            SumAssured.setPadding(5);
            EShieldBasicCoverTable.addCell(SumAssured);

            PdfPCell Premium = new PdfPCell(new Paragraph("Premium (Rs.)"));
            Premium.setHorizontalAlignment(Element.ALIGN_LEFT);
            Premium.setPadding(5);
            EShieldBasicCoverTable.addCell(Premium);


            PdfPCell BasicCover = new PdfPCell(new Paragraph("Basic Cover"));
            BasicCover.setHorizontalAlignment(Element.ALIGN_LEFT);
            BasicCover.setPadding(5);
            EShieldBasicCoverTable.addCell(BasicCover);

            PdfPCell BasicCoverPolicyTerm = new PdfPCell(new Paragraph(String_PolicyTerm));
            BasicCoverPolicyTerm.setHorizontalAlignment(Element.ALIGN_LEFT);
            BasicCoverPolicyTerm.setPadding(5);
            EShieldBasicCoverTable.addCell(BasicCoverPolicyTerm);

            PdfPCell BasicCoverPremiumPaymentTerms = new PdfPCell(new Paragraph(String_PremiumPaymentTerm_display));
            BasicCoverPremiumPaymentTerms.setHorizontalAlignment(Element.ALIGN_LEFT);
            BasicCoverPremiumPaymentTerms.setPadding(5);
            EShieldBasicCoverTable.addCell(BasicCoverPremiumPaymentTerms);

            PdfPCell BasicCoverSumAssured = new PdfPCell(new Paragraph(currencyFormat.format(Double
                    .parseDouble(obj.getStringWithout_E(Double
                            .parseDouble(String_SumAssured))))));
            BasicCoverSumAssured.setHorizontalAlignment(Element.ALIGN_LEFT);
            BasicCoverSumAssured.setPadding(5);
            EShieldBasicCoverTable.addCell(BasicCoverSumAssured);

            PdfPCell BasicCoverPremium = new PdfPCell(new Paragraph(basicpremium));
            BasicCoverPremium.setHorizontalAlignment(Element.ALIGN_LEFT);
            BasicCoverPremium.setPadding(5);
            EShieldBasicCoverTable.addCell(BasicCoverPremium);

            if (String_BetterHalfBenefits.equalsIgnoreCase("Yes")) {

                PdfPCell BetterHalfBenefitTable = new PdfPCell(new Paragraph("Better Half Benefit"));
                BetterHalfBenefitTable.setHorizontalAlignment(Element.ALIGN_LEFT);
                BetterHalfBenefitTable.setPadding(5);
                EShieldBasicCoverTable.addCell(BetterHalfBenefitTable);

                PdfPCell BetterHalfBenefitTablePolicyTerm = new PdfPCell(new Paragraph(String.valueOf(String_BHB_PolicyTerm)));
                BetterHalfBenefitTablePolicyTerm.setHorizontalAlignment(Element.ALIGN_LEFT);
                BetterHalfBenefitTablePolicyTerm.setPadding(5);
                EShieldBasicCoverTable.addCell(BetterHalfBenefitTablePolicyTerm);

                PdfPCell BetterHalfBenefitTablePremiumPaymentTerms = new PdfPCell(new Paragraph(String_PremiumPaymentTerm_display));
                BetterHalfBenefitTablePremiumPaymentTerms.setHorizontalAlignment(Element.ALIGN_LEFT);
                BetterHalfBenefitTablePremiumPaymentTerms.setPadding(5);
                EShieldBasicCoverTable.addCell(BetterHalfBenefitTablePremiumPaymentTerms);

                PdfPCell BetterHalfBenefitSumAssured = new PdfPCell(new Paragraph(currencyFormat.format(Double
                        .parseDouble(obj.getStringWithout_E(Double
                                .parseDouble(BetterHalfBenefitSum))))));
                BetterHalfBenefitSumAssured.setHorizontalAlignment(Element.ALIGN_LEFT);
                BetterHalfBenefitSumAssured.setPadding(5);
                EShieldBasicCoverTable.addCell(BetterHalfBenefitSumAssured);

                PdfPCell betterHalfBenefitPremium = new PdfPCell(new Paragraph(BetterHalfBenefitPremium));
                betterHalfBenefitPremium.setHorizontalAlignment(Element.ALIGN_LEFT);
                betterHalfBenefitPremium.setPadding(5);
                EShieldBasicCoverTable.addCell(betterHalfBenefitPremium);

            } else {
                PdfPCell BetterHalfBenefitTable = new PdfPCell(new Paragraph("Better Half Benefit"));
                BetterHalfBenefitTable.setHorizontalAlignment(Element.ALIGN_LEFT);
                BetterHalfBenefitTable.setPadding(5);
                EShieldBasicCoverTable.addCell(BetterHalfBenefitTable);

                PdfPCell BetterHalfBenefitTablePolicyTerm = new PdfPCell(new Paragraph("No"));
                BetterHalfBenefitTablePolicyTerm.setHorizontalAlignment(Element.ALIGN_LEFT);
                BetterHalfBenefitTablePolicyTerm.setPadding(5);
                EShieldBasicCoverTable.addCell(BetterHalfBenefitTablePolicyTerm);

                PdfPCell BetterHalfBenefitTablePremiumPaymentTerms = new PdfPCell(new Paragraph("-"));
                BetterHalfBenefitTablePremiumPaymentTerms.setHorizontalAlignment(Element.ALIGN_LEFT);
                BetterHalfBenefitTablePremiumPaymentTerms.setPadding(5);
                EShieldBasicCoverTable.addCell(BetterHalfBenefitTablePremiumPaymentTerms);

                PdfPCell BetterHalfBenefitSumAssured = new PdfPCell(new Paragraph("-"));
                BetterHalfBenefitSumAssured.setHorizontalAlignment(Element.ALIGN_LEFT);
                BetterHalfBenefitSumAssured.setPadding(5);
                EShieldBasicCoverTable.addCell(BetterHalfBenefitSumAssured);

                PdfPCell BetterHalfBenefitPremium = new PdfPCell(new Paragraph("-"));
                BetterHalfBenefitPremium.setHorizontalAlignment(Element.ALIGN_LEFT);
                BetterHalfBenefitPremium.setPadding(5);
                EShieldBasicCoverTable.addCell(BetterHalfBenefitPremium);
            }
            if (CheckBox_AccidentalDeathBenefitRider.isChecked()) {
                PdfPCell ADBRider = new PdfPCell(new Paragraph("Accidental Death Benefit Rider (UIN:111B015V03)"));
                ADBRider.setHorizontalAlignment(Element.ALIGN_LEFT);
                ADBRider.setPadding(5);
                EShieldBasicCoverTable.addCell(ADBRider);

                PdfPCell ADBRiderPolicyTerm = new PdfPCell(new Paragraph(String_TermForADBRider));
                ADBRiderPolicyTerm.setHorizontalAlignment(Element.ALIGN_LEFT);
                ADBRiderPolicyTerm.setPadding(5);
                EShieldBasicCoverTable.addCell(ADBRiderPolicyTerm);

                PdfPCell ADBRiderPremiumPaymentTerms;
                if (String_PremiumPaymentOption.equalsIgnoreCase("LPPT")) {
                    ADBRiderPremiumPaymentTerms = new PdfPCell(new Paragraph(String_PremiumPaymentTerm_display));
                } else if (String_PremiumPaymentOption.equalsIgnoreCase("Single")) {
                    ADBRiderPremiumPaymentTerms = new PdfPCell(new Paragraph("1"));
                } else {
                    ADBRiderPremiumPaymentTerms = new PdfPCell(new Paragraph(String_TermForADBRider));
                }


                ADBRiderPremiumPaymentTerms.setHorizontalAlignment(Element.ALIGN_LEFT);
                ADBRiderPremiumPaymentTerms.setPadding(5);
                EShieldBasicCoverTable.addCell(ADBRiderPremiumPaymentTerms);

                PdfPCell ADBRiderSumAssured = new PdfPCell(new Paragraph(currencyFormat.format(Double
                        .parseDouble(obj.getStringWithout_E(Double
                                .parseDouble(String_ADBRiderSumAssured))))));
                ADBRiderSumAssured.setHorizontalAlignment(Element.ALIGN_LEFT);
                ADBRiderSumAssured.setPadding(5);
                EShieldBasicCoverTable.addCell(ADBRiderSumAssured);

                PdfPCell ADBRiderPremium = new PdfPCell(new Paragraph(adbpremium));
                ADBRiderPremium.setHorizontalAlignment(Element.ALIGN_LEFT);
                ADBRiderPremium.setPadding(5);
                EShieldBasicCoverTable.addCell(ADBRiderPremium);
            } else {

                PdfPCell ADBRider = new PdfPCell(new Paragraph("Accidental Death Benefit Rider (UIN:111B015V03)"));
                ADBRider.setHorizontalAlignment(Element.ALIGN_LEFT);
                ADBRider.setPadding(5);
                EShieldBasicCoverTable.addCell(ADBRider);

                PdfPCell ADBRiderPolicyTerm = new PdfPCell(new Paragraph("-"));
                ADBRiderPolicyTerm.setHorizontalAlignment(Element.ALIGN_LEFT);
                ADBRiderPolicyTerm.setPadding(5);
                EShieldBasicCoverTable.addCell(ADBRiderPolicyTerm);

                PdfPCell ADBRiderPremiumPaymentTerms = new PdfPCell(new Paragraph("-"));
                ADBRiderPremiumPaymentTerms.setHorizontalAlignment(Element.ALIGN_LEFT);
                ADBRiderPremiumPaymentTerms.setPadding(5);
                EShieldBasicCoverTable.addCell(ADBRiderPremiumPaymentTerms);

                PdfPCell ADBRiderSumAssured = new PdfPCell(new Paragraph("-"));
                ADBRiderSumAssured.setHorizontalAlignment(Element.ALIGN_LEFT);
                ADBRiderSumAssured.setPadding(5);
                EShieldBasicCoverTable.addCell(ADBRiderSumAssured);


                PdfPCell ADBRiderPremium = new PdfPCell(new Paragraph("-"));
                ADBRiderPremium.setHorizontalAlignment(Element.ALIGN_LEFT);
                ADBRiderPremium.setPadding(5);
                EShieldBasicCoverTable.addCell(ADBRiderPremium);
            }

            if (CheckBox_AccidentalTotalAndPermanentDisabilityBenefitRider.isChecked()) {

                PdfPCell ATPDBRider = new PdfPCell(new Paragraph("Accidental Total & Permanent Disability Benefit Rider (UIN:111B016V03)"));
                ATPDBRider.setHorizontalAlignment(Element.ALIGN_LEFT);
                ATPDBRider.setPadding(5);
                EShieldBasicCoverTable.addCell(ATPDBRider);

                PdfPCell ATPDBRiderPolicyTerm = new PdfPCell(new Paragraph(String_TermForATPDBRider));
                ATPDBRiderPolicyTerm.setHorizontalAlignment(Element.ALIGN_LEFT);
                ATPDBRiderPolicyTerm.setPadding(5);
                EShieldBasicCoverTable.addCell(ATPDBRiderPolicyTerm);

                PdfPCell ATPDBRiderPremiumPaymentTerms;
                if (String_PremiumPaymentOption.equalsIgnoreCase("LPPT")) {
                    ATPDBRiderPremiumPaymentTerms = new PdfPCell(new Paragraph(String_PremiumPaymentTerm_display));
                } else if (String_PremiumPaymentOption.equalsIgnoreCase("Single")) {
                    ATPDBRiderPremiumPaymentTerms = new PdfPCell(new Paragraph("1"));
                } else {
                    ATPDBRiderPremiumPaymentTerms = new PdfPCell(new Paragraph(String_TermForATPDBRider));
                }
                ATPDBRiderPremiumPaymentTerms.setHorizontalAlignment(Element.ALIGN_LEFT);
                ATPDBRiderPremiumPaymentTerms.setPadding(5);
                EShieldBasicCoverTable.addCell(ATPDBRiderPremiumPaymentTerms);


                PdfPCell ATPDBRiderSumAssured = new PdfPCell(new Paragraph(currencyFormat.format(Double

                        .parseDouble(obj.getStringWithout_E(Double
                                .parseDouble(String_ATPDBRiderSumAssured))))));
                ATPDBRiderSumAssured.setHorizontalAlignment(Element.ALIGN_LEFT);
                ATPDBRiderSumAssured.setPadding(5);
                EShieldBasicCoverTable.addCell(ATPDBRiderSumAssured);

                PdfPCell ATPDBRiderPremium = new PdfPCell(new Paragraph(atpdbpremium));
                ATPDBRiderPremium.setHorizontalAlignment(Element.ALIGN_LEFT);
                ATPDBRiderPremium.setPadding(5);
                EShieldBasicCoverTable.addCell(ATPDBRiderPremium);
            } else {
                PdfPCell ATPDBRider = new PdfPCell(new Paragraph("Accidental Total & Permanent Disability Benefit Rider (UIN:111B016V03)"));
                ATPDBRider.setHorizontalAlignment(Element.ALIGN_LEFT);
                ATPDBRider.setPadding(5);
                EShieldBasicCoverTable.addCell(ATPDBRider);

                PdfPCell ATPDBRiderPolicyTerm = new PdfPCell(new Paragraph("-"));
                ATPDBRiderPolicyTerm.setHorizontalAlignment(Element.ALIGN_LEFT);
                ATPDBRiderPolicyTerm.setPadding(5);
                EShieldBasicCoverTable.addCell(ATPDBRiderPolicyTerm);

                PdfPCell ATPDBRiderPremiumPaymentTerms = new PdfPCell(new Paragraph("-"));
                ATPDBRiderPremiumPaymentTerms.setHorizontalAlignment(Element.ALIGN_LEFT);
                ATPDBRiderPremiumPaymentTerms.setPadding(5);
                EShieldBasicCoverTable.addCell(ATPDBRiderPremiumPaymentTerms);

                PdfPCell ATPDBRiderSumAssured = new PdfPCell(new Paragraph("-"));
                ATPDBRiderSumAssured.setHorizontalAlignment(Element.ALIGN_LEFT);
                ATPDBRiderSumAssured.setPadding(5);
                EShieldBasicCoverTable.addCell(ATPDBRiderSumAssured);

                PdfPCell ATPDBRiderPremium = new PdfPCell(new Paragraph("-"));
                ATPDBRiderPremium.setHorizontalAlignment(Element.ALIGN_LEFT);
                ATPDBRiderPremium.setPadding(5);
                EShieldBasicCoverTable.addCell(ATPDBRiderPremium);
            }
            document.add(EShieldBasicCoverTable);
            document.add(para_img_logo_after_space_1);

            PdfPTable EShieldPremiumTable = new PdfPTable(2);
            EShieldPremiumTable.setWidthPercentage(100);


            PdfPCell SingleInstallmentPremium = new PdfPCell(new Paragraph(String_PremiumFrequency + " Installment Premium"));
            SingleInstallmentPremium.setHorizontalAlignment(Element.ALIGN_LEFT);
            SingleInstallmentPremium.setPadding(5);
            EShieldPremiumTable.addCell(SingleInstallmentPremium);

            PdfPCell VarSingleInstallmentPremium = new PdfPCell(new Paragraph(currencyFormat.format(Double
                    .parseDouble(obj.getStringWithout_E(Double
                            .parseDouble(totalpremiumwithoutST))))));
            VarSingleInstallmentPremium.setHorizontalAlignment(Element.ALIGN_LEFT);
            VarSingleInstallmentPremium.setPadding(5);
            EShieldPremiumTable.addCell(VarSingleInstallmentPremium);

            if (CheckBox_StaffDiscount.isChecked()) {
                PdfPCell SingleInstallmentPremiumWithChannelDiscount = new PdfPCell(new Paragraph(String_PremiumFrequency + " Installment Premium with Staff Discount"));
                SingleInstallmentPremiumWithChannelDiscount.setHorizontalAlignment(Element.ALIGN_LEFT);
                SingleInstallmentPremiumWithChannelDiscount.setPadding(5);
                EShieldPremiumTable.addCell(SingleInstallmentPremiumWithChannelDiscount);

                PdfPCell VarSingleInstallmentPremiumWithChannelDiscount = new PdfPCell(new Paragraph(currencyFormat.format(Double
                        .parseDouble(obj.getStringWithout_E(Double
                                .parseDouble(InstallmentPremiumWithStaffDiscount))))));
                VarSingleInstallmentPremiumWithChannelDiscount.setHorizontalAlignment(Element.ALIGN_LEFT);
                VarSingleInstallmentPremiumWithChannelDiscount.setPadding(5);
                EShieldPremiumTable.addCell(VarSingleInstallmentPremiumWithChannelDiscount);
            }

            PdfPCell ApplicableTaxes = new PdfPCell(new Paragraph("Applicable Taxes"));
            ApplicableTaxes.setHorizontalAlignment(Element.ALIGN_LEFT);
            ApplicableTaxes.setPadding(5);
            EShieldPremiumTable.addCell(ApplicableTaxes);

            PdfPCell VarApplicableTaxes = new PdfPCell(new Paragraph(currencyFormat.format(Double
                    .parseDouble(obj.getStringWithout_E(Double
                            .valueOf(BasicServicetax))))));
            VarApplicableTaxes.setHorizontalAlignment(Element.ALIGN_LEFT);
            VarApplicableTaxes.setPadding(5);
            EShieldPremiumTable.addCell(VarApplicableTaxes);


            PdfPCell SingleInstallmentPremiumWithApplicableTaxes = new PdfPCell(new Paragraph(String_PremiumFrequency + " Installment Premium with Applicable Taxes"));
            SingleInstallmentPremiumWithApplicableTaxes.setHorizontalAlignment(Element.ALIGN_LEFT);
            SingleInstallmentPremiumWithApplicableTaxes.setPadding(5);
            EShieldPremiumTable.addCell(SingleInstallmentPremiumWithApplicableTaxes);

            PdfPCell VarSingleInstallmentPremiumWithApplicableTaxes = new PdfPCell(new Paragraph(currencyFormat.format(Double
                    .parseDouble(obj.getStringWithout_E(Double
                            .valueOf(YearlyInstPremWithTaxes))))));
            VarSingleInstallmentPremiumWithApplicableTaxes.setHorizontalAlignment(Element.ALIGN_LEFT);
            VarSingleInstallmentPremiumWithApplicableTaxes.setPadding(5);
            EShieldPremiumTable.addCell(VarSingleInstallmentPremiumWithApplicableTaxes);

          /*  PdfPTable EShieldApplicableTaxesTable = new PdfPTable(1);
            EShieldApplicableTaxesTable.setWidthPercentage(100);

            Paragraph Para_ApplicableTaxes = new Paragraph(
                    "Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium/ or any other charges as per the product features. \n"
            );
            Para_ApplicableTaxes.setAlignment(Element.ALIGN_CENTER);

            document.add(para_img_logo_after_space_1);

            PdfPCell ApplicableTaxesCell = new PdfPCell(Para_ApplicableTaxes);
            ApplicableTaxesCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            ApplicableTaxesCell.setPadding(5);
            EShieldApplicableTaxesTable.addCell(ApplicableTaxesCell);*/


            document.add(EShieldPremiumTable);

            //  document.add(EShieldApplicableTaxesTable);
            document.add(para_img_logo_after_space_1);

            PdfPTable EShieldDisclaimersTable = new PdfPTable(1);
            EShieldDisclaimersTable.setWidthPercentage(100);


            PdfPCell DisclaimerHead = new PdfPCell(new Paragraph("Disclaimers :", headerBold));
            DisclaimerHead.setHorizontalAlignment(Element.ALIGN_LEFT);
            DisclaimerHead.setPadding(5);
            EShieldDisclaimersTable.addCell(DisclaimerHead);


            PdfPCell Disclaimer1 = new PdfPCell(new Paragraph("" +
                    "1. For more details on risk factors, terms and conditions please read sales brochure carefully before concluding a sale. \n" +
                    "2. The exact premium can be determined only at the time of acceptance of risk cover on the life to be assured after taking into consideration any extras required to be imposed. \n " +
                    "3. Tax benefits, are as per the Income Tax laws & are subject to change from time to time. Please consult your tax advisor for details. \n " +
                    "4.Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium/ or any other charges as per the product features. \n "
            ));
            Disclaimer1.setHorizontalAlignment(Element.ALIGN_LEFT);
            Disclaimer1.setPadding(5);
            EShieldDisclaimersTable.addCell(Disclaimer1);

            document.add(EShieldDisclaimersTable);

            PdfPTable EShieldConclusionTable = new PdfPTable(1);
            EShieldConclusionTable.setWidthPercentage(100);
            String finalOutputString = "";
            if (String_PremiumPaymentOption.equalsIgnoreCase("Single")) {
                finalOutputString = "Your SBI Life – eShield Next (UIN: 111N132V01) is a " + String_PremiumPaymentOption +
                        " premium policy and you are required to pay Single Premium of Rs. " + getformatedThousandString(Integer.parseInt(obj
                        .getRound(obj.getStringWithout_E(Double
                                .valueOf(YearlyInstPremWithTaxes))))) + ". Your Policy Term is " + String_PolicyTerm +
                        "years and Basic Sum Assured is Rs. " + currencyFormat.format(Double
                        .parseDouble(obj.getStringWithout_E(Double
                                .valueOf(String_SumAssured))));

            } else if (String_PremiumPaymentOption.equalsIgnoreCase("Regular")) {
                finalOutputString = "Your SBI Life – eShield Next (UIN: 111N132V01) is a " + String_PremiumPaymentOption +
                        "Premium policy, for which your " + String_PremiumFrequency + " premium is Rs. " +
                        currencyFormat.format(Double
                                .parseDouble(obj.getStringWithout_E(Double
                                        .valueOf(YearlyInstPremWithTaxes)))) + ". Your Policy Term is " + String_PolicyTerm +
                        "years, Premium Payment Term is " + String_PremiumPaymentTerm + "years and Basic Sum Assured is Rs. " + currencyFormat.format(Double
                        .parseDouble(obj.getStringWithout_E(Double
                                .valueOf(String_SumAssured))));

            } else {
                finalOutputString = "Your SBI Life – eShield Next (UIN: 111N132V01) is a " + String_PremiumPaymentOption +
                        " policy, for which your " + String_PremiumFrequency + " premium is Rs. " +
                        currencyFormat.format(Double
                                .parseDouble(obj.getStringWithout_E(Double
                                        .valueOf(YearlyInstPremWithTaxes)))) + ". Your Policy Term is " + String_PolicyTerm +
                        "years, Premium Payment Term is " + String_PremiumPaymentTerm + "years and Basic Sum Assured is Rs. " + currencyFormat.format(Double
                        .parseDouble(obj.getStringWithout_E(Double
                                .valueOf(String_SumAssured))));
            }
            PdfPCell Conclusion1 = new PdfPCell(new Paragraph(
                    "You may receive a Welcome Call from our representative to confirm your proposal details like Date of Birth, Nominee Name, Address, Email ID, Sum Assured, Premium amount, Premium Payment Term etc. \n " +
                            "You may have to undergo Medical tests based on our underwriting requirements.\n\n" +
                            "\n\n" + finalOutputString

            ));
            Conclusion1.setHorizontalAlignment(Element.ALIGN_LEFT);
            Conclusion1.setPadding(5);
            EShieldConclusionTable.addCell(Conclusion1);
            document.add(EShieldConclusionTable);


            document.close();

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Message" + e.getMessage());

        }

    }

    private void showDateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(
                context,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                mYear, mMonth, mDay);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        int SIGNATURE_ACTIVITY = 1;
        if (requestCode == SIGNATURE_ACTIVITY) {
            if (resultCode == RESULT_OK) {

                Bundle bundle = data.getExtras();
                String status = bundle.getString("status");
                if (status != null && status.equalsIgnoreCase("done")) {
                    Toast toast = Toast
                            .makeText(this, "Signature capture successful!",
                                    Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 105, 50);
                    toast.show();
                    if (latestImage.equalsIgnoreCase("proposer")) {
                        Ibtn_signatureofPolicyHolders
                                .setImageBitmap(ProposerCaptureSignature.scaled);
                        Bitmap signature = ProposerCaptureSignature.scaled;
                        if (signature != null) {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            signature.compress(Bitmap.CompressFormat.PNG, 100,
                                    out);
                            byte[] signByteArray = out.toByteArray();
                            proposer_sign = Base64.encodeToString(
                                    signByteArray, Base64.DEFAULT);
                        }
                        // ProductHomePageActivity.customer_Signature =
                        // CaptureSignature.scaled;
                    } else if (latestImage.equalsIgnoreCase("agent")) {
                        Ibtn_signatureofMarketing
                                .setImageBitmap(CaptureSignature.scaled);
                        Bitmap signature = CaptureSignature.scaled;
                        if (signature != null) {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            signature.compress(Bitmap.CompressFormat.PNG, 100,
                                    out);
                            byte[] signByteArray = out.toByteArray();
                            agent_sign = Base64.encodeToString(signByteArray,
                                    Base64.DEFAULT);
                        }

                    }

                }
            }
        } else if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                if (Check.equals("Photo")) {

                    File Photo = commonMethods.galleryAddPic(context);
                    Bitmap bmp = BitmapFactory.decodeFile(Photo
                            .getAbsolutePath());

                    Bitmap b = null;
                    Uri imageUri = Uri.fromFile(new File(Photo.toString()));
                    try {
                        b = MediaStore.Images.Media.getBitmap(

                                getContentResolver(), imageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap mFaceBitmap = b != null ? b.copy(Bitmap.Config.RGB_565, true) : null;
                    assert b != null;
                    b.recycle();
                    if (mFaceBitmap != null) {
                        Bitmap scaled = Bitmap.createScaledBitmap(bmp, 230,
                                200, true);
                        photoBitmap = scaled;
                        imageButtonProposerPhotograph
                                .setImageBitmap(scaled);
                    } else {
                        photoBitmap = null;
                    }
                }
            }
        }
        /* end */
    }
}