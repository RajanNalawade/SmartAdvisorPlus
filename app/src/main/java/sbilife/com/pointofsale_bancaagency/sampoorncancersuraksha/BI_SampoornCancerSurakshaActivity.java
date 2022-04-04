package sbilife.com.pointofsale_bancaagency.sampoorncancersuraksha;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.ProductInfo;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.needanalysis.NeedAnalysisActivity;
import sbilife.com.pointofsale_bancaagency.new_bussiness.ProductBIBean;

@SuppressWarnings("deprecation")
public class BI_SampoornCancerSurakshaActivity extends AppCompatActivity implements
        OnEditorActionListener {

    private NeedAnalysisBIService NABIObj;
    private NA_CBI_bean na_cbi_bean;

    private File needAnalysispath, newFile;
    private String agentcode, agentMobile, agentEmail, userType;
    private int needAnalysis_flag = 0;
    private String na_input = null;
    private String na_output = null;

    private String na_dob = "";
    private int flag = 0;

    private EditText edt_bi_sampoorn_cancer_suraksha_life_assured_first_name,
            edt_bi_sampoorn_cancer_suraksha_life_assured_middle_name,
            edt_bi_sampoorn_cancer_suraksha_life_assured_last_name,
            edt_bi_sampoorn_cancer_suraksha_sum_assured;

    private EditText edt_proposerdetail_basicdetail_contact_no,
            edt_proposerdetail_basicdetail_Email_id,
            edt_proposerdetail_basicdetail_ConfirmEmail_id;

    private Spinner spnr_bi_sampoorn_cancer_suraksha_life_assured_title,
            spnr_bi_sampoorn_cancer_suraksha_life_assured_age;

    private Spinner spnrPremFrequency, spnrPolicyTerm, spnrPlan, spnrGender,
            spnr_bi_sampoorn_cancer_suraksha_proposer_Gender;
    private Button btn_bi_sampoorn_cancer_suraksha_life_assured_date, btnBack,
            btnSubmit;

    private CheckBox CbJkResident, cb_staffdisc;

    private ScrollView sv_bi_sampoorn_cancer_suraksha_main;

    private Button btn_PolicyholderDate;

    private DatabaseHelper db;
    private StringBuilder inputVal;

    private String lifeAssured_Title = "", lifeAssured_First_Name = "",
            lifeAssured_Middle_Name = "", lifeAssured_Last_Name = "",
            name_of_life_assured = "", lifeAssured_date_of_birth = "",
            str_lifeAssured_gender = "", str_proposer_gender = "";

    private String mobileNo = "";
    private String emailId = "";
    private String ConfirmEmailId = "";
    private boolean validationFla1 = false;
    private String ProposerEmailId = "";
    private CommonForAllProd obj;
    private ParseXML prsObj;
    private AlertDialog.Builder showAlert;

    private String QuatationNumber;
    private String planName = "";
    private int mYear;
    private int mMonth;
    private int mDay;
    private String str_usertype = "";
    private final int DATE_DIALOG_ID = 1;
    private int DIALOG_ID;
    private String proposer_Is_Same_As_Life_Assured = "Y";
    private String proposer_Title = "", proposer_First_Name = "",
            proposer_Middle_Name = "", proposer_Last_Name = "";

    private String proposer_date_of_birth = "", name_of_proposer = "";
    private SampoornCancerSurakshaBean sampoornCancerSurakshaBean;

    private StringBuilder retVal;

    private String name_of_person = "";
    private String place2 = "";
    private String date2 = "", date1 = "";
    private String agent_sign = "";
    private String proposer_sign = "";

    private Bitmap photoBitmap;
    private String planProposedName = "", premPayingMode = "", policyTerm = "",
            ageAtEntry = "", gender = "", sumAssured = "", premiumAmt = "",
            isJkResident = "", staffdiscount = "";

    private SampoornCancerSurakshaBusinesslogic sampoorncancersurakshabusinesslogic = new SampoornCancerSurakshaBusinesslogic();
    private CommonForAllProd commonForAllProd;
    private StringBuilder bussIll = null;
    private ImageButton Ibtn_signatureofMarketing;
    private ImageButton Ibtn_signatureofPolicyHolders;
    private String latestImage = "";
    private Dialog d;

    private String PremiumBeforeST_Standard = "",
            PremiumWithServTax_Standard = "", GST_Standard = "",
            PremiumBeforeST_Classic = "", PremiumWithServTax_Classic = "",
            GST_Classic = "",

    PremiumBeforeST_Enhanced = "", PremiumWithServTax_Enhanced = "",
            GST_Enhanced = "";

    DecimalFormat currencyFormat, decimalcurrencyFormat;

    private final int SIGNATURE_ACTIVITY = 1;
    private File mypath;
    private TableLayout tablelayoutSampoornaCancerSurakshaProposerTitle;
    private Spinner spnr_bi_sampoorn_cancer_suraksha_proposer_title;
    private EditText edt_bi_sampoorn_cancer_suraksha_proposer_last_name,
            edt_bi_sampoorn_cancer_suraksha_proposer_middle_name,
            edt_bi_sampoorn_cancer_suraksha_proposer_first_name;
    private Button btn_bi_sampoorn_cancer_suraksha_proposer_date;
    private String product_Code, product_UIN, product_cateogory, product_type;
    private String bankUserType = "", mode = "";
    private Context context;

    private String Check = "";
    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;
    private ImageButton imageButtonSmartPrivilegeProposerPhotograph;
    String Company_policy_surrender_dec = "";

    private LinearLayout linearlayoutThirdpartySignature;
    private LinearLayout linearlayoutAppointeeSignature;
    private ImageButton Ibtn_signatureofThirdParty, Ibtn_signatureofAppointee,
            Ibtn_signatureofLifeAssured;
    private String thirdPartySign = "";
    private String appointeeSign = "";
    private String proposerAsLifeAssuredSign = "";

    String str_kerla_discount = "No";

    private CheckBox cb_kerladisc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bi_sampoorn_cancer_surakshamain);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        commonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        commonMethods.setApplicationToolbarMenu(this, getString(R.string.app_name));
        context = this;
        commonForAllProd = new CommonForAllProd();
        initialiseDate();
        db = new DatabaseHelper(this);
        Intent intent = getIntent();
        obj = new CommonForAllProd();

        obj = new CommonForAllProd();
        showAlert = new AlertDialog.Builder(this);
        prsObj = new ParseXML();


        setIntialize_ID();

        NABIObj = new NeedAnalysisBIService(this);
        prsObj = new ParseXML();

        //Added Tushar Kadam Kerla Applicable Taxes 7-Jun-2019
        TableRow tablerowKerlaDiscount = findViewById(R.id.tablerowKerlaDiscount);
        cb_kerladisc = findViewById(R.id.cb_kerladisc);
        commonMethods.setKerlaDiscount(context, tablerowKerlaDiscount, cb_kerladisc);
        //End Added Tushar Kadam Kerla Applicable Taxes 7-Jun-2019

        String na_flag = intent.getStringExtra("NAFlag");

        if (na_flag != null) {
            if (na_flag.equalsIgnoreCase("1")) {
                needAnalysis_flag = 1;
                na_input = intent.getStringExtra("NaInput");
                na_output = intent.getStringExtra("NaOutput");
                na_dob = intent.getStringExtra("custDOB");
                gender = intent.getStringExtra("custGender");

                bankUserType = intent.getStringExtra("Other");

                if (bankUserType != null) {

                } else {
                    bankUserType = "";
                }

                try {
                    agentcode = SimpleCrypto.decrypt("SBIL", db.GetUserCode());

                    agentMobile = SimpleCrypto
                            .decrypt("SBIL", db.GetMobileNo());
                    agentEmail = SimpleCrypto.decrypt("SBIL", db.GetEmailId());
                    userType = SimpleCrypto.decrypt("SBIL", db.GetUserType());

                    ProductInfo prodInfoObj = new ProductInfo();
                    planName = "Sampoorn Cancer Suraksha";
                    product_Code = prodInfoObj.getProductCode(planName);
                    product_UIN = prodInfoObj.getProductUIN(planName);
                    product_cateogory = prodInfoObj
                            .getProductCategory(planName);
                    product_type = prodInfoObj.getProductType(planName);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
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

        String[] planList = {"Standard", "Classic", "Enhanced"};
        ArrayAdapter<String> planAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, planList);
        planAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnrPlan.setAdapter(planAdapter);
        planAdapter.notifyDataSetChanged();

        String[] premFreqList = {"Yearly", "Half-Yearly", "Quarterly",
                "Monthly"};
        ArrayAdapter<String> premFreqAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, premFreqList);
        premFreqAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnrPremFrequency.setAdapter(premFreqAdapter);
        premFreqAdapter.notifyDataSetChanged();

		/*
		  Age 18 to 60 Change as per 1,Jan,2014 by Akshaya Mirajkar.
		 */
        // Age
        String[] ageList = new String[60];
        for (int i = 6; i <= 65; i++) {
            ageList[i - 6] = i + "";
        }
        ArrayAdapter<String> ageAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, ageList);
        ageAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_sampoorn_cancer_suraksha_life_assured_age
                .setAdapter(ageAdapter);
        ageAdapter.notifyDataSetChanged();

        // Gender
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, getResources().getStringArray(R.array.gender_all_arrays));
        genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnrGender.setAdapter(genderAdapter);
        genderAdapter.notifyDataSetChanged();


        ArrayAdapter<String> genderAdapter_prop = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, getResources().getStringArray(R.array.gender_all_arrays));
        genderAdapter_prop.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_sampoorn_cancer_suraksha_proposer_Gender
                .setAdapter(genderAdapter);
        genderAdapter_prop.notifyDataSetChanged();

        commonMethods.fillSpinnerValue(context, spnr_bi_sampoorn_cancer_suraksha_life_assured_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));
        commonMethods.fillSpinnerValue(context, spnr_bi_sampoorn_cancer_suraksha_proposer_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));


        setSpinnerAndOtherListner();

        edt_bi_sampoorn_cancer_suraksha_life_assured_first_name
                .setOnEditorActionListener(this);
        edt_bi_sampoorn_cancer_suraksha_life_assured_middle_name
                .setOnEditorActionListener(this);
        edt_bi_sampoorn_cancer_suraksha_life_assured_last_name
                .setOnEditorActionListener(this);

        edt_bi_sampoorn_cancer_suraksha_sum_assured
                .setOnEditorActionListener(this);

        edt_proposerdetail_basicdetail_contact_no
                .setOnEditorActionListener(this);
        edt_proposerdetail_basicdetail_Email_id.setOnEditorActionListener(this);
        edt_proposerdetail_basicdetail_ConfirmEmail_id
                .setOnEditorActionListener(this);


        TableRow tr_staff_disc;

        try {
            str_usertype = commonMethods.GetUserType(context);

        } catch (Exception e) {
            Log.e("BIActivity", e.toString()
                    + "Error  in getting in Login Table");
            e.printStackTrace();
        }

        tr_staff_disc = findViewById(R.id.tr_sampoorn_cancer_suraksha_staff_disc);
        try {
            str_usertype = SimpleCrypto.decrypt("SBIL",
                    db.GetUserType());

        } catch (Exception e) {
            e.printStackTrace();
        }
		/*if (str_usertype.equalsIgnoreCase("BAP")
				|| str_usertype.equalsIgnoreCase("CAG")
				|| str_usertype.equalsIgnoreCase("IMF")) {
			tr_staff_disc.setVisibility(View.GONE);
		}*/

        if (needAnalysis_flag == 1 && !TextUtils.isEmpty(gender)) {
            spnrGender.setSelection(genderAdapter.getPosition(gender));
            onClickLADob(spnrGender);
        }

    }

    private void setIntialize_ID() {

        sv_bi_sampoorn_cancer_suraksha_main = findViewById(R.id.sv_bi_sampoorn_cancer_suraksha_main);

        spnr_bi_sampoorn_cancer_suraksha_life_assured_title = findViewById(R.id.spnr_bi_sampoorn_cancer_suraksha_life_assured_title);
        edt_bi_sampoorn_cancer_suraksha_life_assured_first_name = findViewById(R.id.edt_bi_sampoorn_cancer_suraksha_life_assured_first_name);
        edt_bi_sampoorn_cancer_suraksha_life_assured_middle_name = findViewById(R.id.edt_bi_sampoorn_cancer_suraksha_life_assured_middle_name);
        edt_bi_sampoorn_cancer_suraksha_life_assured_last_name = findViewById(R.id.edt_bi_sampoorn_cancer_suraksha_life_assured_last_name);
        edt_bi_sampoorn_cancer_suraksha_sum_assured = findViewById(R.id.edt_bi_sampoorn_cancer_suraksha_sum_assured);

        edt_proposerdetail_basicdetail_contact_no = findViewById(R.id.edt_sampoorn_cancer_suraksha_contact_no);
        edt_proposerdetail_basicdetail_Email_id = findViewById(R.id.edt_sampoorn_cancer_suraksha_Email_id);
        edt_proposerdetail_basicdetail_ConfirmEmail_id = findViewById(R.id.edt_sampoorn_cancer_suraksha_ConfirmEmail_id);

        spnr_bi_sampoorn_cancer_suraksha_life_assured_age = findViewById(R.id.spnr_bi_sampoorn_cancer_suraksha_age);
        spnr_bi_sampoorn_cancer_suraksha_life_assured_age.setEnabled(false);
        spnrPlan = findViewById(R.id.spnr_bi_sampoorn_cancer_suraksha_plan);
        spnrGender = findViewById(R.id.spnr_bi_sampoorn_cancer_suraksha_selGender);

        spnr_bi_sampoorn_cancer_suraksha_proposer_Gender = findViewById(R.id.spnr_bi_sampoorn_cancer_suraksha_proposer_Gender);
        //spnrGender.setClickable(false);
        //spnrGender.setEnabled(false);

        spnrPremFrequency = findViewById(R.id.spnr_bi_sampoorn_cancer_suraksha_premium_paying_mode);
        spnrPolicyTerm = findViewById(R.id.spnr_bi_sampoorn_cancer_suraksha_policyterm);

        CbJkResident = findViewById(R.id.cb_bi_sampoorn_cancer_suraksha_JKResident);
        cb_staffdisc = findViewById(R.id.cb_staffdisc);

        btn_bi_sampoorn_cancer_suraksha_life_assured_date = findViewById(R.id.btn_bi_sampoorn_cancer_suraksha_life_assured_date);

        // Go home Button
        btnBack = findViewById(R.id.btn_bi_sampoorn_cancer_suraksha_btnback);
        // Submit Button
        btnSubmit = findViewById(R.id.btn_bi_sampoorn_cancer_suraksha_btnSubmit);

        // Proposer
        edt_bi_sampoorn_cancer_suraksha_proposer_last_name = findViewById(R.id.edt_bi_sampoorn_cancer_suraksha_proposer_last_name);
        edt_bi_sampoorn_cancer_suraksha_proposer_middle_name = findViewById(R.id.edt_bi_sampoorn_cancer_suraksha_proposer_middle_name);
        edt_bi_sampoorn_cancer_suraksha_proposer_first_name = findViewById(R.id.edt_bi_sampoorn_cancer_suraksha_proposer_first_name);
        spnr_bi_sampoorn_cancer_suraksha_proposer_title = findViewById(R.id.spnr_bi_sampoorn_cancer_suraksha_proposer_title);
        tablelayoutSampoornaCancerSurakshaProposerTitle = findViewById(R.id.tablelayoutSampoornaCancerSurakshaProposerTitle);
        // tr_sampoorn_cancer_suraksha_proposer_detail2 = (TableRow)
        // findViewById(R.id.tr_sampoorn_cancer_suraksha_proposer_detail2);
        btn_bi_sampoorn_cancer_suraksha_proposer_date = findViewById(R.id.btn_bi_sampoorn_cancer_suraksha_proposer_date);

    }

    private void setSpinnerAndOtherListner() {
        // TODO Auto-generated method stub

        /********************** Item Listener starts here ********************************************/

        // Staff Discount
        cb_staffdisc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_staffdisc.isChecked()) {
                    cb_staffdisc.setChecked(true);
                } else {
                    cb_staffdisc.setChecked(false);
                }
            }
        });

        cb_kerladisc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_kerladisc.isChecked()) {
                    str_kerla_discount = "Yes";
                    clearFocusable(cb_kerladisc);
                    setFocusable(spnr_bi_sampoorn_cancer_suraksha_life_assured_title);
                    spnr_bi_sampoorn_cancer_suraksha_life_assured_title.requestFocus();
                } else {
                    str_kerla_discount = "No";
                    clearFocusable(cb_kerladisc);
                    setFocusable(spnr_bi_sampoorn_cancer_suraksha_life_assured_title);
                    spnr_bi_sampoorn_cancer_suraksha_life_assured_title.requestFocus();
                }
            }
        });


        CbJkResident.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (CbJkResident.isChecked()) {
                    CbJkResident.setChecked(true);
                } else {
                    CbJkResident.setChecked(false);
                }
            }
        });

        spnr_bi_sampoorn_cancer_suraksha_life_assured_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {

                        // TODO Auto-generated method stub
                        if (position == 0) {
                            lifeAssured_Title = "";
                        } else {
                            lifeAssured_Title = spnr_bi_sampoorn_cancer_suraksha_life_assured_title
                                    .getSelectedItem().toString();

                            clearFocusable(spnr_bi_sampoorn_cancer_suraksha_life_assured_title);
                            setFocusable(edt_bi_sampoorn_cancer_suraksha_life_assured_first_name);

                            edt_bi_sampoorn_cancer_suraksha_life_assured_first_name
                                    .requestFocus();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_sampoorn_cancer_suraksha_proposer_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position == 0) {
                            proposer_Title = "";
                        } else {
                            proposer_Title = spnr_bi_sampoorn_cancer_suraksha_proposer_title
                                    .getSelectedItem().toString();

                            clearFocusable(spnr_bi_sampoorn_cancer_suraksha_proposer_title);

                            setFocusable(edt_bi_sampoorn_cancer_suraksha_proposer_first_name);

                            edt_bi_sampoorn_cancer_suraksha_proposer_first_name
                                    .requestFocus();

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnrGender.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {

                // TODO Auto-generated method stub
                if (position == 0) {
                    str_lifeAssured_gender = "";
                } else {

                    str_lifeAssured_gender = spnrGender.getSelectedItem()
                            .toString();

					/*title_list.add("Select Title");
					title_list.add("Mr.");
					title_list.add("Ms.");
					title_list.add("Mrs.");*/

					/*if ((lifeAssured_Title.equalsIgnoreCase("Mrs.")||lifeAssured_Title.equalsIgnoreCase("Ms."))
							&& str_lifeAssured_gender.equalsIgnoreCase("Male"))
					{
						spnr_bi_sampoorn_cancer_suraksha_life_assured_title.setSelection(1);
					}*/


                    clearFocusable(spnrGender);
                    setFocusable(edt_bi_sampoorn_cancer_suraksha_life_assured_first_name);

                    spnrPlan.requestFocus();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        spnr_bi_sampoorn_cancer_suraksha_proposer_Gender
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {

                        if (position == 0) {
                            str_proposer_gender = "";
                        } else {

                            str_proposer_gender = spnr_bi_sampoorn_cancer_suraksha_proposer_Gender
                                    .getSelectedItem().toString();
                            clearFocusable(spnr_bi_sampoorn_cancer_suraksha_proposer_Gender);
                            setFocusable(edt_proposerdetail_basicdetail_contact_no);
                            edt_proposerdetail_basicdetail_contact_no
                                    .requestFocus();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });
        // Age
        spnr_bi_sampoorn_cancer_suraksha_life_assured_age
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        // Plan
        spnrPlan.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

        // Term
        spnrPolicyTerm.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        spnrPremFrequency
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        edt_proposerdetail_basicdetail_contact_no
                .addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1,
                                                  int arg2, int arg3) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {
                        // TODO Auto-generated method stub
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
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1,
                                                  int arg2, int arg3) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {
                        // TODO Auto-generated method stub
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
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1,
                                                  int arg2, int arg3) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {
                        // TODO Auto-generated method stub
                        String proposer_confirm_emailId = edt_proposerdetail_basicdetail_ConfirmEmail_id
                                .getText().toString();
                        //confirming_email_id(proposer_confirm_emailId);

                    }
                });

        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                setResult(RESULT_OK);
                finish();
            }
        });

        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                inputVal = new StringBuilder();
                retVal = new StringBuilder();

                str_proposer_gender = spnr_bi_sampoorn_cancer_suraksha_proposer_Gender.getSelectedItem().toString();
                str_lifeAssured_gender = spnrGender.getSelectedItem().toString();

                proposer_First_Name = edt_bi_sampoorn_cancer_suraksha_proposer_first_name
                        .getText().toString();
                proposer_Middle_Name = edt_bi_sampoorn_cancer_suraksha_proposer_middle_name
                        .getText().toString();
                proposer_Last_Name = edt_bi_sampoorn_cancer_suraksha_proposer_last_name
                        .getText().toString();

                name_of_proposer = proposer_Title + " " + proposer_First_Name
                        + " " + proposer_Middle_Name + " " + proposer_Last_Name;

                lifeAssured_First_Name = edt_bi_sampoorn_cancer_suraksha_life_assured_first_name
                        .getText().toString();
                lifeAssured_Middle_Name = edt_bi_sampoorn_cancer_suraksha_life_assured_middle_name
                        .getText().toString();
                lifeAssured_Last_Name = edt_bi_sampoorn_cancer_suraksha_life_assured_last_name
                        .getText().toString();

                name_of_life_assured = lifeAssured_Title + " "
                        + lifeAssured_First_Name + " "
                        + lifeAssured_Middle_Name + " " + lifeAssured_Last_Name;

                mobileNo = edt_proposerdetail_basicdetail_contact_no.getText()
                        .toString();
                emailId = edt_proposerdetail_basicdetail_Email_id.getText()
                        .toString();
                ConfirmEmailId = edt_proposerdetail_basicdetail_ConfirmEmail_id
                        .getText().toString();

                if (valLifeAssuredProposerDetail() && valDob()
                        && valProposerDob() && valBasicDetail()
                        && val_proposer_gender() && valSA()) {

                    Date();

                    if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
                        proposer_Title = "";
                        proposer_First_Name = "";
                        proposer_Middle_Name = "";
                        proposer_Last_Name = "";
                        name_of_proposer = "";
                        proposer_date_of_birth = "";
                    }
                    addListenerOnSubmit();
                    getInput(sampoornCancerSurakshaBean);

                    if (needAnalysis_flag == 0) {
                        DecimalFormat currencyFormat = new DecimalFormat(
                                "##,##,##,###");
                        Intent i = new Intent(
                                BI_SampoornCancerSurakshaActivity.this,
                                success.class);
                        i.putExtra(
                                "op",
                                "Basic Premium is Rs. "
                                        + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
                                        retVal.toString(),
                                        "PremiumBeforeST_Standard"))));

                        i.putExtra(
                                "op1",
                                "Applicable Taxes is Rs. "
                                        + currencyFormat.format(Double.parseDouble(prsObj
                                        .parseXmlTag(retVal.toString(),
                                                "GST_Standard"))));

                        i.putExtra(
                                "op2",
                                "Premium with Applicable Taxes is Rs. "
                                        + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
                                        retVal.toString(),
                                        "PremiumWithServTax_Standard"))));
                        i.putExtra(
                                "op3",
                                "Basic Premium is Rs. "
                                        + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
                                        retVal.toString(),
                                        "PremiumBeforeST_Classic"))));

                        i.putExtra(
                                "op4",
                                "Applicable Taxes is Rs. "
                                        + currencyFormat.format(Double
                                        .parseDouble(prsObj
                                                .parseXmlTag(retVal
                                                                .toString(),
                                                        "GST_Classic"))));

                        i.putExtra(
                                "op5",
                                "Premium with Applicable Taxes is Rs. "
                                        + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
                                        retVal.toString(),
                                        "PremiumWithServTax_Classic"))));
                        i.putExtra(
                                "op6",
                                "Basic Premium is Rs. "
                                        + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
                                        retVal.toString(),
                                        "PremiumBeforeST_Enhanced"))));

                        i.putExtra(
                                "op7",
                                "Applicable Taxes is Rs. "
                                        + currencyFormat.format(Double.parseDouble(prsObj
                                        .parseXmlTag(retVal.toString(),
                                                "GST_Enhanced"))));

                        i.putExtra(
                                "op8",
                                "Premium with Applicable Taxes is Rs. "
                                        + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
                                        retVal.toString(),
                                        "PremiumWithServTax_Enhanced"))));
                        i.putExtra("ProductName",
                                "SBI Life - Sampoorn Cancer Suraksha");
                        startActivity(i);

                    } else
                        Dialog();

                }

            }
        });

    }

    private void setFocusable(View v) {
        // TODO Auto-generated method stub
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
    }

    // method to set a clearing a element
    private void clearFocusable(View v) {
        // TODO Auto-generated method stub
        v.setFocusable(false);
        v.setFocusableInTouchMode(false);
        // v.clearFocus();
    }

    private void mobile_validation(String number) {
        if ((number.length() != 10)) {
            edt_proposerdetail_basicdetail_contact_no
                    .setError("Please provide correct 10-digit mobile number");
        } else if ((number.length() == 10)) {
        }
    }

    private void email_id_validation(String email_id) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email_id);
        if (!(matcher.matches())) {
            edt_proposerdetail_basicdetail_Email_id
                    .setError("Please provide the correct email address");
            validationFla1 = false;
        } else if (!cb_staffdisc.isChecked()
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


    /*
     * private void getBasicDetail() { List<M_basicDetail> list_BasicDetail = db
     * .getBasicDetails(QuatationNumber);
     *
     * int i = 0; list_BasicDetail = db.getBasicDetails(QuatationNumber); if
     * (list_BasicDetail.size() > 0) { flagFocus = false;
     *
     * edt_proposerdetail_basicdetail_contact_no.setText(list_BasicDetail
     * .get(i).getMobileNo());
     * edt_proposerdetail_basicdetail_Email_id.setText(list_BasicDetail
     * .get(i).getEmailId()); edt_proposerdetail_basicdetail_ConfirmEmail_id
     * .setText(list_BasicDetail.get(i).getEmailId());
     *
     * }
     *
     * }
     */

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        boolean flagFocus = true;
        if (!flagFocus) {
            sv_bi_sampoorn_cancer_suraksha_main.requestFocus();
        } else {
            edt_bi_sampoorn_cancer_suraksha_life_assured_first_name
                    .requestFocus();
        }

    }

    private void initialiseDate() {
        Calendar calender = Calendar.getInstance();
        mYear = calender.get(Calendar.YEAR);
        mMonth = calender.get(Calendar.MONTH);
        mDay = calender.get(Calendar.DAY_OF_MONTH);

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // TODO Auto-generated method stub
        if (v.getId() == edt_bi_sampoorn_cancer_suraksha_life_assured_first_name
                .getId()) {
            setFocusable(edt_bi_sampoorn_cancer_suraksha_life_assured_middle_name);
            edt_bi_sampoorn_cancer_suraksha_life_assured_middle_name
                    .requestFocus();
        } else if (v.getId() == edt_bi_sampoorn_cancer_suraksha_life_assured_middle_name
                .getId()) {
            setFocusable(edt_bi_sampoorn_cancer_suraksha_life_assured_last_name);
            edt_bi_sampoorn_cancer_suraksha_life_assured_last_name
                    .requestFocus();
        } else if (v.getId() == edt_bi_sampoorn_cancer_suraksha_life_assured_last_name
                .getId()) {
            setFocusable(btn_bi_sampoorn_cancer_suraksha_life_assured_date);
            btn_bi_sampoorn_cancer_suraksha_life_assured_date.requestFocus();
        } else if (v.getId() == edt_proposerdetail_basicdetail_contact_no
                .getId()) {
            // clearFocusable(premiumAmt);
            setFocusable(edt_proposerdetail_basicdetail_Email_id);
            edt_proposerdetail_basicdetail_Email_id.requestFocus();
        } else if (v.getId() == edt_proposerdetail_basicdetail_Email_id.getId()) {
            // clearFocusable(premiumAmt);
            setFocusable(edt_proposerdetail_basicdetail_ConfirmEmail_id);
            edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
        } else if (v.getId() == edt_proposerdetail_basicdetail_ConfirmEmail_id
                .getId()) {

            setFocusable(edt_bi_sampoorn_cancer_suraksha_sum_assured);
            edt_bi_sampoorn_cancer_suraksha_sum_assured.requestFocus();
        } else if (v.getId() == edt_bi_sampoorn_cancer_suraksha_sum_assured
                .getId()) {
            commonMethods.hideKeyboard(
                    edt_bi_sampoorn_cancer_suraksha_sum_assured,
                    BI_SampoornCancerSurakshaActivity.this);
        }

        return true;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, R.style.datepickerstyle,
                        mDateSetListener, mDay, mMonth, mYear);
            default:
                break;
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
            default:
                break;

        }
    }

    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // if (view.isShown()) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay(DIALOG_ID);
            // }
        }
    };

    // For Displaying StartDate and EndDate after its selection
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
        } else
            age = tYear - mYear - 1;

        String final_age = Integer.toString(age);

        if ((id != 7) && (id != 8) && final_age.contains("-")) {
            commonMethods.dialogWarning(context, "Please fill Valid Birth Date", false);
        } else {
            switch (id) {

                case 2:
                    btn_PolicyholderDate.setText(date);
                    break;

                case 4:
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Birth Date");
                    } else {
                        if (18 <= age) {

                            btn_bi_sampoorn_cancer_suraksha_proposer_date
                                    .setText(date);
                            proposer_date_of_birth = getDate1(date + "");

                            clearFocusable(btn_bi_sampoorn_cancer_suraksha_proposer_date);
                            setFocusable(edt_proposerdetail_basicdetail_contact_no);
                            edt_proposerdetail_basicdetail_contact_no
                                    .requestFocus();

                        } else {
                            commonMethods.BICommonDialog(context, "Minimum age should be 18 yrs for proposer");
                            btn_bi_sampoorn_cancer_suraksha_proposer_date
                                    .setText("Select Date");
                            proposer_date_of_birth = "";
                            clearFocusable(btn_bi_sampoorn_cancer_suraksha_proposer_date);
                            setFocusable(btn_bi_sampoorn_cancer_suraksha_proposer_date);
                            btn_bi_sampoorn_cancer_suraksha_proposer_date
                                    .requestFocus();
                        }
                    }
                    break;

                case 5:

                    final_age = Integer.toString(age);
                    // lifeAssuredAge = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Bith Date");
                    } else {

                        if (6 <= age && age <= 65) {

                            btn_bi_sampoorn_cancer_suraksha_life_assured_date
                                    .setText(date);

                            spnr_bi_sampoorn_cancer_suraksha_life_assured_age
                                    .setSelection(
                                            getIndex(
                                                    spnr_bi_sampoorn_cancer_suraksha_life_assured_age,
                                                    final_age), false);
                            setPolicyTerm(final_age);

                            lifeAssured_date_of_birth = getDate1(date + "");

                            if (Integer.parseInt(final_age) < 18) {
                                tablelayoutSampoornaCancerSurakshaProposerTitle
                                        .setVisibility(View.VISIBLE);
                                /*
                                 * tr_sampoorn_cancer_suraksha_proposer_detail2
                                 * .setVisibility(View.VISIBLE);
                                 */
                                proposer_Is_Same_As_Life_Assured = "n";

                                clearFocusable(btn_bi_sampoorn_cancer_suraksha_life_assured_date);
                                setFocusable(spnr_bi_sampoorn_cancer_suraksha_proposer_title);
                                spnr_bi_sampoorn_cancer_suraksha_proposer_title
                                        .requestFocus();
                            } else {

                                tablelayoutSampoornaCancerSurakshaProposerTitle
                                        .setVisibility(View.GONE);
                                /*
                                 * tr_sampoorn_cancer_suraksha_proposer_detail2
                                 * .setVisibility(View.GONE);
                                 */
                                proposer_Is_Same_As_Life_Assured = "y";

                                clearFocusable(btn_bi_sampoorn_cancer_suraksha_life_assured_date);
                                setFocusable(edt_proposerdetail_basicdetail_contact_no);
                                edt_proposerdetail_basicdetail_contact_no
                                        .requestFocus();
                            }

                        } else {

                            commonMethods.BICommonDialog(context, "The Age of Life Assured should be between 6 & 65 yrs");
                            btn_bi_sampoorn_cancer_suraksha_life_assured_date
                                    .setText("Select Date");
                            lifeAssured_date_of_birth = "";
                            clearFocusable(btn_bi_sampoorn_cancer_suraksha_life_assured_date);
                            setFocusable(btn_bi_sampoorn_cancer_suraksha_life_assured_date);
                            btn_bi_sampoorn_cancer_suraksha_life_assured_date
                                    .requestFocus();
                        }
                    }
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
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
                        d.dismiss();

                    } else if (latestImage.equalsIgnoreCase("thirdParty")) {
                        Ibtn_signatureofThirdParty
                                .setImageBitmap(ProposerCaptureSignature.scaled);

                        Bitmap signature = ProposerCaptureSignature.scaled;
                        if (signature != null) {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            signature.compress(Bitmap.CompressFormat.PNG, 100,
                                    out);
                            byte[] signByteArray = out.toByteArray();
                            thirdPartySign = Base64.encodeToString(
                                    signByteArray, Base64.DEFAULT);
                        }
                    } else if (latestImage.equalsIgnoreCase("Appointee")) {
                        Ibtn_signatureofAppointee
                                .setImageBitmap(ProposerCaptureSignature.scaled);

                        Bitmap signature = ProposerCaptureSignature.scaled;
                        if (signature != null) {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            signature.compress(Bitmap.CompressFormat.PNG, 100,
                                    out);
                            byte[] signByteArray = out.toByteArray();
                            appointeeSign = Base64.encodeToString(
                                    signByteArray, Base64.DEFAULT);
                        }

                    } else if (latestImage
                            .equalsIgnoreCase("proposerSignOnLifeAssured")) {

                        Ibtn_signatureofLifeAssured
                                .setImageBitmap(ProposerCaptureSignature.scaled);

                        Bitmap signature = ProposerCaptureSignature.scaled;
                        if (signature != null) {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            signature.compress(Bitmap.CompressFormat.PNG, 100,
                                    out);
                            byte[] signByteArray = out.toByteArray();
                            proposerAsLifeAssuredSign = Base64.encodeToString(
                                    signByteArray, Base64.DEFAULT);
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
                    Uri imageUri;
                    try {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            imageUri = commonMethods.getContentUri(context, new File(Photo.toString()));
                        } else {
                            imageUri = Uri.fromFile(new File(Photo.toString()));
                        }

                        b = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
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
                        imageButtonSmartPrivilegeProposerPhotograph
                                .setImageBitmap(scaled);
                    } else {
                        photoBitmap = null;
                    }
                }
            }
        }
    }

    public void onClickProposerDob(View v) {
        initialiseDateParameter(proposer_date_of_birth, 35);
        DIALOG_ID = 4;
        showDialog(DATE_DIALOG_ID);
    }

    public void onClickLADob(View v) {

        if (!na_dob.equals("") && flag == 0) {
            System.out.println(" na_dob : " + na_dob);
            initialiseDateParameter(na_dob, 35);
            DIALOG_ID = 5;
            updateDisplay(DIALOG_ID);
            flag = 1;
        } else {
            initialiseDateParameter(lifeAssured_date_of_birth, 35);
            DIALOG_ID = 5;
            showDialog(DATE_DIALOG_ID);
        }
    }

    private int getIndex(Spinner s1, String value) {

        int index = 0;

        for (int i = 0; i < s1.getCount(); i++) {
            if (s1.getItemAtPosition(i).equals(value)) {
                index = i;
            }
        }
        return index;
    }

    private String getformatedThousandString(int number) {
        return NumberFormat.getNumberInstance(Locale.US)
                .format(number);
    }

    private String getCurrentDate() {
        Calendar present_date = Calendar.getInstance();
        int mDay = present_date.get(Calendar.DAY_OF_MONTH);
        int mMonth = present_date.get(Calendar.MONTH) + 1;
        int mYear = present_date.get(Calendar.YEAR);

        return mDay + "-" + mMonth + "-" + mYear;

    }

    /**
     * Used To Change date From mm-dd-yyyy to dd-mm-yyyy
     */
    private String getDate(String OldDate) {
        String NewDate = "";
        try {
            DateFormat userDateFormat = new SimpleDateFormat("MM-dd-yyyy");

            DateFormat dateFormatNeeded = new SimpleDateFormat("dd/MM/yyyy");
            Date date = userDateFormat.parse(OldDate);

            NewDate = dateFormatNeeded.format(date);

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Integrated Service", "Error in Getting Date");
        }

        return NewDate;
    }

    /**
     * Used To Change date From dd-mm-yyyy to mm-dd-yyyy
     */
    private String getDate1(String OldDate) {
        String NewDate = "";
        try {
            DateFormat userDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            DateFormat dateFormatNeeded = new SimpleDateFormat("MM-dd-yyyy");
            Date date = userDateFormat.parse(OldDate);

            NewDate = dateFormatNeeded.format(date);

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Integrated Service", "Error in Getting Date");
        }

        return NewDate;
    }

    private void initialiseDateParameter(String date_value, int id) {
        if (date_value.equals("")) {
            setDefaultDate(id);
        } else {
            String[] array = date_value.split("-");
            mMonth = Integer.parseInt(array[0]) - 1;
            mDay = Integer.parseInt(array[1]);
            mYear = Integer.parseInt(array[2]);
        }
    }

    private void setDefaultDate(int id) {
        Calendar present_date = Calendar.getInstance();
        present_date.add(Calendar.YEAR, -id);
        mDay = present_date.get(Calendar.DAY_OF_MONTH);
        mMonth = present_date.get(Calendar.MONTH);
        mYear = present_date.get(Calendar.YEAR);
    }

    private void Date() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // formattedDate = df.format(c.getTime());
        // formattedDate have current date/time
        // Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
        c.add(Calendar.DATE, 30);
        // ExpiredDate = df.format(c.getTime());
    }


    private boolean valLifeAssuredProposerDetail() {

        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("Y")) {
            if (lifeAssured_Title.equals("")
                    || lifeAssured_First_Name.equals("")
                    || lifeAssured_Last_Name.equals("")) {

                showAlert.setMessage("Please Fill Name Detail For LifeAssured");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (lifeAssured_Title.equals("")) {
                                    // apply focusable method
                                    setFocusable(spnr_bi_sampoorn_cancer_suraksha_life_assured_title);
                                    spnr_bi_sampoorn_cancer_suraksha_life_assured_title
                                            .requestFocus();
                                } else if (lifeAssured_First_Name.equals("")) {
                                    setFocusable(edt_bi_sampoorn_cancer_suraksha_life_assured_first_name);
                                    edt_bi_sampoorn_cancer_suraksha_life_assured_first_name
                                            .requestFocus();
                                } else {
                                    setFocusable(edt_bi_sampoorn_cancer_suraksha_life_assured_last_name);
                                    edt_bi_sampoorn_cancer_suraksha_life_assured_last_name
                                            .requestFocus();
                                }
                            }
                        });
                showAlert.show();

                return false;
            } else if (lifeAssured_Title.equalsIgnoreCase("Mr.")
                    && str_lifeAssured_gender.equalsIgnoreCase("Female")) {

                showAlert
                        .setMessage("Life Assured Title and Gender is not Valid");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_sampoorn_cancer_suraksha_life_assured_title);
                                spnr_bi_sampoorn_cancer_suraksha_life_assured_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Ms.")
                    && str_lifeAssured_gender.equalsIgnoreCase("Male")) {

                showAlert
                        .setMessage("Life Assured Title and Gender is not Valid");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_sampoorn_cancer_suraksha_life_assured_title);
                                spnr_bi_sampoorn_cancer_suraksha_life_assured_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Mrs.")
                    && str_lifeAssured_gender.equalsIgnoreCase("Male")) {

                showAlert
                        .setMessage("Life Assured Title and Gender is not Valid");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_sampoorn_cancer_suraksha_life_assured_title);
                                spnr_bi_sampoorn_cancer_suraksha_life_assured_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;

            } else {
                return true;
            }

        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {

            if (lifeAssured_Title.equals("")
                    || lifeAssured_First_Name.equals("")
                    || lifeAssured_Last_Name.equals("")) {

                showAlert.setMessage("Please Fill Name Detail For LifeAssured");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (lifeAssured_Title.equals("")) {
                                    // apply focusable method
                                    setFocusable(spnr_bi_sampoorn_cancer_suraksha_life_assured_title);
                                    spnr_bi_sampoorn_cancer_suraksha_life_assured_title
                                            .requestFocus();
                                } else if (lifeAssured_First_Name.equals("")) {

                                    edt_bi_sampoorn_cancer_suraksha_life_assured_first_name
                                            .requestFocus();
                                } else {
                                    edt_bi_sampoorn_cancer_suraksha_life_assured_last_name
                                            .requestFocus();
                                }
                            }
                        });
                showAlert.show();

                return false;
            } else if (lifeAssured_Title.equalsIgnoreCase("Mr.")
                    && str_lifeAssured_gender.equalsIgnoreCase("Female")) {

                showAlert
                        .setMessage("Life Assured Title and Gender is not Valid");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_sampoorn_cancer_suraksha_life_assured_title);
                                spnr_bi_sampoorn_cancer_suraksha_life_assured_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Ms.")
                    && str_lifeAssured_gender.equalsIgnoreCase("Male")) {

                showAlert
                        .setMessage("Life Assured Title and Gender is not Valid");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_sampoorn_cancer_suraksha_life_assured_title);
                                spnr_bi_sampoorn_cancer_suraksha_life_assured_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Mrs.")
                    && str_lifeAssured_gender.equalsIgnoreCase("Male")) {

                showAlert
                        .setMessage("Life Assured Title and Gender is not Valid");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_sampoorn_cancer_suraksha_life_assured_title);
                                spnr_bi_sampoorn_cancer_suraksha_life_assured_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;

            } else if (proposer_Title.equals("")
                    || proposer_First_Name.equals("")
                    || proposer_Last_Name.equals("")) {

                showAlert.setMessage("Please Fill Name Detail For Proposer");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (proposer_Title.equals("")) {
                                    // apply focusable method
                                    setFocusable(spnr_bi_sampoorn_cancer_suraksha_proposer_title);
                                    spnr_bi_sampoorn_cancer_suraksha_proposer_title
                                            .requestFocus();
                                } else if (proposer_First_Name.equals("")) {
                                    edt_bi_sampoorn_cancer_suraksha_proposer_first_name
                                            .requestFocus();
                                } else {
                                    edt_bi_sampoorn_cancer_suraksha_proposer_last_name
                                            .requestFocus();
                                }
                            }
                        });
                showAlert.show();

                return false;

            } else if (proposer_Title.equalsIgnoreCase("Mr.")
                    && str_proposer_gender.equalsIgnoreCase("Female")) {

                showAlert.setMessage("Proposar Title and Gender is not Valid");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_sampoorn_cancer_suraksha_proposer_title);
                                spnr_bi_sampoorn_cancer_suraksha_proposer_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;

            } else if (proposer_Title.equalsIgnoreCase("Ms.")
                    && str_proposer_gender.equalsIgnoreCase("Male")) {

                showAlert.setMessage("Proposar Title and Gender is not Valid");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_sampoorn_cancer_suraksha_proposer_title);
                                spnr_bi_sampoorn_cancer_suraksha_proposer_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;

            } else if (proposer_Title.equalsIgnoreCase("Mrs.")
                    && str_proposer_gender.equalsIgnoreCase("Male")) {

                showAlert.setMessage("Proposar Title and Gender is not Valid");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_sampoorn_cancer_suraksha_proposer_title);
                                spnr_bi_sampoorn_cancer_suraksha_proposer_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;

            } else {
                return true;
            }
        } else {
            return true;
        }

    }

    private boolean valDob() {

        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {

            if (lifeAssured_date_of_birth.equals("")
                    || lifeAssured_date_of_birth
                    .equalsIgnoreCase("select Date")) {
                showAlert
                        .setMessage("Please Select Valid Date Of Birth For LifeAssured");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(btn_bi_sampoorn_cancer_suraksha_life_assured_date);
                                btn_bi_sampoorn_cancer_suraksha_life_assured_date
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;
            } else {
                return true;
            }
        } else
            return true;
    }

    private boolean valProposerDob() {

        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {

            if (proposer_date_of_birth.equals("")
                    || proposer_date_of_birth.equalsIgnoreCase("Select Date")) {
                showAlert
                        .setMessage("Please Select Valid Date Of Birth For Proposer");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(btn_bi_sampoorn_cancer_suraksha_proposer_date);
                                btn_bi_sampoorn_cancer_suraksha_proposer_date
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;
            } else {
                return true;
            }
        } else
            return true;
    }

    private boolean valBasicDetail() {
        if (spnrGender.getSelectedItem().toString().equals("Select Gender")) {
            commonMethods.dialogWarning(context, "Please Select Gender", true);
            spnrGender.requestFocus();
            return false;
        } else if (edt_proposerdetail_basicdetail_contact_no.getText()
                .toString().equals("")) {
            commonMethods.dialogWarning(context, "Please Fill  Mobile No", true);
            edt_proposerdetail_basicdetail_contact_no.requestFocus();
            return false;
        } else if (edt_proposerdetail_basicdetail_contact_no.getText()
                .toString().length() != 10) {
            commonMethods.dialogWarning(context, "Please Fill 10 Digit Mobile No", true);
            edt_proposerdetail_basicdetail_contact_no.requestFocus();
            return false;
        }

		/*else if (emailId.equals("")) {
			commonMethods.dialogWarning(context,"Please Fill Email Id", true);
			edt_proposerdetail_basicdetail_Email_id.requestFocus();
			return false;

		}

		else if (ConfirmEmailId.equals("")) {

			commonMethods.dialogWarning(context,"Please Fill Confirm Email Id", true);
			edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
			return false;
		} else if (!ConfirmEmailId.equals(emailId)) {
			commonMethods.dialogWarning(context,"Email Id Does Not Match", true);
			return false;
		}*/

        else if (!emailId.equals("")) {

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

    private void addListenerOnSubmit() {
        // TODO Auto-generated method stub
        sampoornCancerSurakshaBean = new SampoornCancerSurakshaBean();

        if (cb_staffdisc.isChecked()) {
            sampoornCancerSurakshaBean.setStaffDisc(true);
        } else {
            sampoornCancerSurakshaBean.setStaffDisc(false);
        }
        if (cb_kerladisc.isChecked()) {
            sampoornCancerSurakshaBean.setKerlaDisc(true);
            sampoornCancerSurakshaBean.setServiceTax(true);
        } else {
            sampoornCancerSurakshaBean.setServiceTax(false);
            sampoornCancerSurakshaBean.setKerlaDisc(false);
        }
        if (CbJkResident.isChecked()) {
            sampoornCancerSurakshaBean.setJKResident(true);
        } else {
            sampoornCancerSurakshaBean.setJKResident(false);
        }

        sampoornCancerSurakshaBean.setGender(spnrGender.getSelectedItem()
                .toString());

        sampoornCancerSurakshaBean
                .setProposer_gender(spnr_bi_sampoorn_cancer_suraksha_proposer_Gender.getSelectedItem()
                        .toString());

        sampoornCancerSurakshaBean.setAge(Integer
                .parseInt(spnr_bi_sampoorn_cancer_suraksha_life_assured_age
                        .getSelectedItem().toString()));

        sampoornCancerSurakshaBean.setPlanName(spnrPlan.getSelectedItem()
                .toString());

        sampoornCancerSurakshaBean.setBasicTerm(Integer.parseInt(spnrPolicyTerm
                .getSelectedItem().toString()));

        sampoornCancerSurakshaBean.setPremFreq(spnrPremFrequency
                .getSelectedItem().toString());

        sampoornCancerSurakshaBean.setBasicSA(Integer
                .parseInt(edt_bi_sampoorn_cancer_suraksha_sum_assured.getText()
                        .toString()));

        // Show Output Form
        showSampoornCancerSurakshaOutputPg(sampoornCancerSurakshaBean);
    }

    private void getInput(SampoornCancerSurakshaBean sampoornCancerSurakshaBean) {
        inputVal = new StringBuilder();

        int age = sampoornCancerSurakshaBean.getAge();
        String planName = sampoornCancerSurakshaBean.getPlanName();
        String gender = sampoornCancerSurakshaBean.getGender();
        String gender_proposer = sampoornCancerSurakshaBean
                .getProposer_gender();
        int basicPolicyTerm = sampoornCancerSurakshaBean.getBasicTerm();

        double basicSumAssured = sampoornCancerSurakshaBean.getBasicSA();

        String PremPayingMode = sampoornCancerSurakshaBean.getPremFreq();

        boolean isJKresident = sampoornCancerSurakshaBean.isJKResident();
        boolean isStaffOrNot = sampoornCancerSurakshaBean.getStaffDisc();

        String LifeAssured_title = spnr_bi_sampoorn_cancer_suraksha_life_assured_title
                .getSelectedItem().toString();
        String LifeAssured_firstName = edt_bi_sampoorn_cancer_suraksha_life_assured_first_name
                .getText().toString();
        String LifeAssured_middleName = edt_bi_sampoorn_cancer_suraksha_life_assured_middle_name
                .getText().toString();
        String LifeAssured_lastName = edt_bi_sampoorn_cancer_suraksha_life_assured_last_name
                .getText().toString();
        String LifeAssured_DOB = btn_bi_sampoorn_cancer_suraksha_life_assured_date
                .getText().toString();
        String LifeAssured_age = spnr_bi_sampoorn_cancer_suraksha_life_assured_age
                .getSelectedItem().toString();

        String proposer_firstName = "";
        String proposer_middleName = "";
        String proposer_lastName = "";
        String proposer_DOB = "";
        String proposer_age = "";
        String proposer_gender = "";

        if (!spnr_bi_sampoorn_cancer_suraksha_proposer_Gender.getSelectedItem()
                .toString().equals("") && !spnr_bi_sampoorn_cancer_suraksha_proposer_Gender.getSelectedItem()
                .toString().equals("Select Gender")) {
            proposer_gender = spnr_bi_sampoorn_cancer_suraksha_proposer_Gender
                    .getSelectedItem().toString();

        }

        if (!edt_bi_sampoorn_cancer_suraksha_proposer_first_name.getText()
                .toString().equals(""))
            proposer_firstName = edt_bi_sampoorn_cancer_suraksha_proposer_first_name
                    .getText().toString();

        if (!edt_bi_sampoorn_cancer_suraksha_proposer_middle_name.getText()
                .toString().equals(""))
            proposer_middleName = edt_bi_sampoorn_cancer_suraksha_proposer_middle_name
                    .getText().toString();
        if (!edt_bi_sampoorn_cancer_suraksha_proposer_last_name.getText()
                .toString().equals(""))
            proposer_lastName = edt_bi_sampoorn_cancer_suraksha_proposer_last_name
                    .getText().toString();

        if (!btn_bi_sampoorn_cancer_suraksha_proposer_date.getText().toString()
                .equals("Select Date")) {
            Calendar present_date = Calendar.getInstance();
            int tDay = present_date.get(Calendar.DAY_OF_MONTH);
            int tMonth = present_date.get(Calendar.MONTH);
            int tYear = present_date.get(Calendar.YEAR);
            proposer_DOB = btn_bi_sampoorn_cancer_suraksha_proposer_date
                    .getText().toString();
            proposer_age = commonForAllProd.calculateMyAge(tYear, tMonth + 1,
                    tDay, getDate1(proposer_DOB)) + "";
        }

        // boolean staffDisc = sampoornCancerSurakshaBean.getStaffDisc();

        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><sampoorncancersuraksha>");

        inputVal.append("<LifeAssured_title>").append(LifeAssured_title).append("</LifeAssured_title>");
        inputVal.append("<LifeAssured_firstName>").append(LifeAssured_firstName).append("</LifeAssured_firstName>");
        inputVal.append("<LifeAssured_middleName>").append(LifeAssured_middleName).append("</LifeAssured_middleName>");
        inputVal.append("<LifeAssured_lastName>").append(LifeAssured_lastName).append("</LifeAssured_lastName>");
        inputVal.append("<LifeAssured_DOB>").append(LifeAssured_DOB).append("</LifeAssured_DOB>");
        inputVal.append("<LifeAssured_age>").append(LifeAssured_age).append("</LifeAssured_age>");
        inputVal.append("<gender>").append(gender).append("</gender>");

        inputVal.append("<proposer_title>").append(proposer_Title).append("</proposer_title>");
        inputVal.append("<proposer_firstName>").append(proposer_firstName).append("</proposer_firstName>");
        inputVal.append("<proposer_middleName>").append(proposer_middleName).append("</proposer_middleName>");
        inputVal.append("<proposer_lastName>").append(proposer_lastName).append("</proposer_lastName>");
        inputVal.append("<proposer_DOB>").append(proposer_DOB).append("</proposer_DOB>");
        inputVal.append("<proposer_age>").append(proposer_age).append("</proposer_age>");
        inputVal.append("<proposer_gender>").append(proposer_gender).append("</proposer_gender>");

        inputVal.append("<product_name>").append(planName).append("</product_name>");
        inputVal.append("<product_Code>").append(product_Code).append("</product_Code>");
        inputVal.append("<product_UIN>").append(product_UIN).append("</product_UIN>");
        inputVal.append("<product_cateogory>").append(product_cateogory).append("</product_cateogory>");
        inputVal.append("<product_type>").append(product_type).append("</product_type>");

        inputVal.append("<proposer_Is_Same_As_Life_Assured>").append(proposer_Is_Same_As_Life_Assured).append("</proposer_Is_Same_As_Life_Assured>");

        inputVal.append("<isJKResident>").append(isJKresident).append("</isJKResident>");
        inputVal.append("<isStaff>").append(isStaffOrNot).append("</isStaff>");
        inputVal.append("<str_kerla_discount>" + str_kerla_discount + "</str_kerla_discount>");
        inputVal.append("<age>").append(age).append("</age>");
        inputVal.append("<plan>").append(planName).append("</plan>");

        inputVal.append("<policyTerm>").append(basicPolicyTerm).append("</policyTerm>");

        inputVal.append("<premFreq>").append(PremPayingMode).append("</premFreq>");
        inputVal.append("<sumAssured>").append(basicSumAssured).append("</sumAssured>");

        inputVal.append("<Product_Cat>").append(product_cateogory).append("</Product_Cat>");

        //Added Tushar Kadam Kerla Applicable Taxes 7-Jun-2019
        str_kerla_discount = "N";
        if (cb_kerladisc.isChecked()) {
            str_kerla_discount = "Y";
        }

        inputVal.append("<KFC>" + str_kerla_discount + "</KFC>");
        //End Added Tushar Kadam Kerla Applicable Taxes 7-Jun-2019


        inputVal.append("</sampoorncancersuraksha>");

    }


    private void alert(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                BI_SampoornCancerSurakshaActivity.this);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private boolean valSA() {
        boolean valSA = true;
        if (edt_bi_sampoorn_cancer_suraksha_sum_assured.getText().toString()
                .equals("")) {
            alert("Sum Assured Cannot be blank");
            valSA = false;
        } else if (Double
                .parseDouble(edt_bi_sampoorn_cancer_suraksha_sum_assured
                        .getText().toString()) % 100000 != 0) {
            alert("Sum Assured should be in multiples of 100000");
            valSA = false;
        } else if (Double
                .parseDouble(edt_bi_sampoorn_cancer_suraksha_sum_assured
                        .getText().toString()) < 1000000
                || Double
                .parseDouble(edt_bi_sampoorn_cancer_suraksha_sum_assured
                        .getText().toString()) > 5000000) {
            alert("Sum Assured should be between 1000000 to 5000000");
            valSA = false;

        } else {
            valSA = true;
        }
        return valSA;
    }

    public boolean valMaturity() {
        boolean valMaturity = true;
        int policyterm = Integer.parseInt(spnrPolicyTerm.getSelectedItem()
                .toString());
        int age = Integer
                .parseInt(spnr_bi_sampoorn_cancer_suraksha_life_assured_age
                        .getSelectedItem().toString());
        int sum = policyterm + age;
        if (sum > 75) {
            alert("Maturity Age Cannot be greater than 75");
            valMaturity = false;
        } else {
            valMaturity = true;
        }

        return valMaturity;

    }

    private void setPolicyTerm(String age) {

        int pos = Integer.parseInt(age);

        String[] policyterm = new String[26];
        if (pos < 46) {
            policyterm = new String[26];
            for (int i = 5; i <= 30; i++) {
                policyterm[i - 5] = i + "";
            }
        } else if (pos == 46) {
            policyterm = new String[25];
            for (int i = 5; i <= 29; i++) {
                policyterm[i - 5] = i + "";
            }

        } else if (pos == 47) {
            policyterm = new String[24];
            for (int i = 5; i <= 28; i++) {
                policyterm[i - 5] = i + "";
            }

        } else if (pos == 48) {
            policyterm = new String[23];
            for (int i = 5; i <= 27; i++) {
                policyterm[i - 5] = i + "";
            }

        } else if (pos == 49) {
            policyterm = new String[22];
            for (int i = 5; i <= 26; i++) {
                policyterm[i - 5] = i + "";
            }

        } else if (pos == 50) {
            policyterm = new String[21];
            for (int i = 5; i <= 25; i++) {
                policyterm[i - 5] = i + "";
            }

        } else if (pos == 51) {
            policyterm = new String[20];
            for (int i = 5; i <= 24; i++) {
                policyterm[i - 5] = i + "";
            }

        } else if (pos == 52) {
            policyterm = new String[19];
            for (int i = 5; i <= 23; i++) {
                policyterm[i - 5] = i + "";
            }

        } else if (pos == 53) {
            policyterm = new String[18];
            for (int i = 5; i <= 22; i++) {
                policyterm[i - 5] = i + "";
            }

        } else if (pos == 54) {
            policyterm = new String[17];
            for (int i = 5; i <= 21; i++) {
                policyterm[i - 5] = i + "";
            }

        } else if (pos == 55) {
            policyterm = new String[16];
            for (int i = 5; i <= 20; i++) {
                policyterm[i - 5] = i + "";
            }

        } else if (pos == 56) {
            policyterm = new String[15];
            for (int i = 5; i <= 19; i++) {
                policyterm[i - 5] = i + "";
            }

        } else if (pos == 57) {
            policyterm = new String[14];
            for (int i = 5; i <= 18; i++) {
                policyterm[i - 5] = i + "";
            }

        } else if (pos == 58) {
            policyterm = new String[13];
            for (int i = 5; i <= 17; i++) {
                policyterm[i - 5] = i + "";
            }

        } else if (pos == 59) {
            policyterm = new String[12];
            for (int i = 5; i <= 16; i++) {
                policyterm[i - 5] = i + "";
            }

        } else if (pos == 60) {
            policyterm = new String[11];
            for (int i = 5; i <= 15; i++) {
                policyterm[i - 5] = i + "";
            }

        } else if (pos == 61) {
            policyterm = new String[10];
            for (int i = 5; i <= 14; i++) {
                policyterm[i - 5] = i + "";
            }

        } else if (pos == 62) {
            policyterm = new String[9];
            for (int i = 5; i <= 13; i++) {
                policyterm[i - 5] = i + "";
            }

        } else if (pos == 63) {
            policyterm = new String[8];
            for (int i = 5; i <= 12; i++) {
                policyterm[i - 5] = i + "";
            }

        } else if (pos == 64) {
            policyterm = new String[7];
            for (int i = 5; i <= 11; i++) {
                policyterm[i - 5] = i + "";
            }

        } else if (pos == 65) {
            policyterm = new String[6];
            for (int i = 5; i <= 10; i++) {
                policyterm[i - 5] = i + "";
            }

        }

        ArrayAdapter<String> planAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, policyterm);
        planAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnrPolicyTerm.setAdapter(planAdapter);
        planAdapter.notifyDataSetChanged();

    }

    private void showSampoornCancerSurakshaOutputPg(
            SampoornCancerSurakshaBean sampoorncancersurakshabean) {
        // TODO Auto-generated method stub

        /* Added by Vrushali on 12-May-15 start ***/
        String staffStatus = "";
        if (cb_staffdisc.isChecked()) {
            staffStatus = "sbi";
            // disc_Basic_SelFreq
        } else
            staffStatus = "none";

        String[] outputArr = getOutput("BI_of_Sampoorn_Cancer_Suraksha",
                sampoorncancersurakshabean);

        String val = "", totInstPrem_exclST_exclDisc = "0", totInstPrem_exclST_exclDisc_exclFreqLoading = "0", TotalTaxes = "0";
        switch (sampoorncancersurakshabean.getPlanName()) {
            case "Standard":
                totInstPrem_exclST_exclDisc = outputArr[10];
                totInstPrem_exclST_exclDisc_exclFreqLoading = outputArr[13];
                TotalTaxes = outputArr[3];
                break;
            case "Classic":
                totInstPrem_exclST_exclDisc = outputArr[11];
                totInstPrem_exclST_exclDisc_exclFreqLoading = outputArr[14];
                TotalTaxes = outputArr[4];
                break;
            default:
                totInstPrem_exclST_exclDisc = outputArr[12];
                totInstPrem_exclST_exclDisc_exclFreqLoading = outputArr[15];
                TotalTaxes = outputArr[5];
                break;
        }

        String val_standard = (valminprem(outputArr[0]));
        String val_classic = (valminprem(outputArr[1]));
        String val_enhanced = (valminprem(outputArr[2]));

        try {

            boolean flag_success = false;
            if (!val_standard.equals("") && !val_classic.equals("")
                    && !val_enhanced.equals("")) {
                alert(val_standard);
                flag_success = false;
            } else if (sampoorncancersurakshabean.getPlanName().equals(
                    "Standard")
                    && !val_standard.equals("")) {
                alert(val_standard
                        + ".So either increase the Sum Assured or change the plan type with current sum assured");
                flag_success = false;
            } else if (sampoorncancersurakshabean.getPlanName().equals(
                    "Classic")
                    && !val_classic.equals("")) {
                alert(val_classic
                        + ".So either increase the Sum Assured or change the plan type with current sum assured");
                flag_success = false;
            } else if (sampoorncancersurakshabean.getPlanName().equals(
                    "Enhanced")
                    && !val_enhanced.equals("")) {
                alert(val_enhanced
                        + ".So either increase the Sum Assured or change the plan type with current sum assured");
                flag_success = false;
            } else {
                flag_success = true;
                if (val_standard != "") {
                    retVal.append("<?xml version='1.0' encoding='utf-8' ?><SampoornCancerSuraksha>");
                    retVal.append("<errCode>0</errCode>");
                    retVal.append("<PremiumBeforeST_Standard>" + "NA" + "</PremiumBeforeST_Standard>" + "<PremiumBeforeST_Classic>").append(outputArr[1]).append("</PremiumBeforeST_Classic>").append("<PremiumBeforeST_Enhanced>").append(outputArr[2]).append("</PremiumBeforeST_Enhanced>").append("<GST_Standard>").append("NA").append("</GST_Standard>").append("<GST_Classic>").append(outputArr[4]).append("</GST_Classic>").append("<GST_Enhanced>").append(outputArr[5]).append("</GST_Enhanced>").append("<PremiumWithServTax_Standard>").append("NA").append("</PremiumWithServTax_Standard>").append("<PremiumWithServTax_Classic>").append(outputArr[7]).append("</PremiumWithServTax_Classic>").append("<PremiumWithServTax_Enhanced>").append(outputArr[8]).append("</PremiumWithServTax_Enhanced>").append(bussIll.toString());
                    //retVal.append("</SampoornCancerSuraksha>");
                    // System.out.println("output " + retVal.toString());
                } else if (val_classic != "") {

                    retVal.append("<?xml version='1.0' encoding='utf-8' ?><SampoornCancerSuraksha>");
                    retVal.append("<errCode>0</errCode>");
                    retVal.append("<PremiumBeforeST_Standard>").append(outputArr[0]).append("</PremiumBeforeST_Standard>").append("<PremiumBeforeST_Classic>").append("NA").append("</PremiumBeforeST_Classic>").append("<PremiumBeforeST_Enhanced>").append(outputArr[2]).append("</PremiumBeforeST_Enhanced>").append("<GST_Standard>").append(outputArr[3]).append("</GST_Standard>").append("<GST_Classic>").append("NA").append("</GST_Classic>").append("<GST_Enhanced>").append(outputArr[5]).append("</GST_Enhanced>").append("<PremiumWithServTax_Standard>").append(outputArr[6]).append("</PremiumWithServTax_Standard>").append("<PremiumWithServTax_Classic>").append("NA").append("</PremiumWithServTax_Classic>").append("<PremiumWithServTax_Enhanced>").append(outputArr[8]).append("</PremiumWithServTax_Enhanced>").append(bussIll.toString());
                    //retVal.append("</SampoornCancerSuraksha>");
                    // System.out.println("output " + retVal.toString());
                } else if (val_enhanced != "") {

                    retVal.append("<?xml version='1.0' encoding='utf-8' ?><SampoornCancerSuraksha>");
                    retVal.append("<errCode>0</errCode>");
                    retVal.append("<PremiumBeforeST_Standard>").append(outputArr[0]).append("</PremiumBeforeST_Standard>")
                            .append("<PremiumBeforeST_Classic>").append(outputArr[1]).append("</PremiumBeforeST_Classic>")
                            .append("<PremiumBeforeST_Enhanced>").append("NA").append("</PremiumBeforeST_Enhanced>").append("<GST_Standard>").append(outputArr[3]).append("</GST_Standard>").append("<GST_Classic>").append(outputArr[4]).append("</GST_Classic>").append("<GST_Enhanced>").append("NA").append("</GST_Enhanced>").append("<PremiumWithServTax_Standard>").append(outputArr[6]).append("</PremiumWithServTax_Standard>").append("<PremiumWithServTax_Classic>").append(outputArr[7]).append("</PremiumWithServTax_Classic>")
                            .append("<PremiumWithServTax_Enhanced>").append("NA").append("</PremiumWithServTax_Enhanced>").append(bussIll.toString());
                    //retVal.append("</SampoornCancerSuraksha>");
                    // System.out.println("output " + retVal.toString());
                } else if (val_standard != "" && val_classic != "") {

                    retVal.append("<?xml version='1.0' encoding='utf-8' ?><SampoornCancerSuraksha>");
                    retVal.append("<errCode>0</errCode>");
                    retVal.append("<PremiumBeforeST_Standard>" + "NA" + "</PremiumBeforeST_Standard>" + "<PremiumBeforeST_Classic>" + "NA" + "</PremiumBeforeST_Classic>" + "<PremiumBeforeST_Enhanced>").append(outputArr[2]).append("</PremiumBeforeST_Enhanced>").append("<GST_Standard>").append("NA").append("</GST_Standard>").append("<GST_Classic>").append("NA").append("</GST_Classic>").append("<GST_Enhanced>").append(outputArr[5]).append("</GST_Enhanced>").append("<PremiumWithServTax_Standard>").append("NA").append("</PremiumWithServTax_Standard>").append("<PremiumWithServTax_Classic>").append("NA").append("</PremiumWithServTax_Classic>").append("<PremiumWithServTax_Enhanced>").append(outputArr[8]).append("</PremiumWithServTax_Enhanced>").append(bussIll.toString());
                    //retVal.append("</SampoornCancerSuraksha>");
                    // System.out.println("output " + retVal.toString());
                } else if (val_classic != "" && val_enhanced != "") {

                    retVal.append("<?xml version='1.0' encoding='utf-8' ?><SampoornCancerSuraksha>");
                    retVal.append("<errCode>0</errCode>");
                    retVal.append("<PremiumBeforeST_Standard>").append(outputArr[0]).append("</PremiumBeforeST_Standard>").append("<PremiumBeforeST_Classic>").append("NA").append("</PremiumBeforeST_Classic>").append("<PremiumBeforeST_Enhanced>").append("NA").append("</PremiumBeforeST_Enhanced>").append("<GST_Standard>").append(outputArr[3]).append("</GST_Standard>").append("<GST_Classic>").append("NA").append("</GST_Classic>").append("<GST_Enhanced>").append("NA").append("</GST_Enhanced>").append("<PremiumWithServTax_Standard>").append(outputArr[6]).append("</PremiumWithServTax_Standard>").append("<PremiumWithServTax_Classic>").append("NA").append("</PremiumWithServTax_Classic>").append("<PremiumWithServTax_Enhanced>").append("NA").append("</PremiumWithServTax_Enhanced>").append(bussIll.toString());
                    //retVal.append("</SampoornCancerSuraksha>");
                    // System.out.println("output " + retVal.toString());
                } else if (val_standard != "" && val_enhanced != "") {

                    retVal.append("<?xml version='1.0' encoding='utf-8' ?><SampoornCancerSuraksha>");
                    retVal.append("<errCode>0</errCode>");
                    retVal.append("<PremiumBeforeST_Standard>" + "NA" + "</PremiumBeforeST_Standard>" + "<PremiumBeforeST_Classic>").append(outputArr[1]).append("</PremiumBeforeST_Classic>").append("<PremiumBeforeST_Enhanced>").append("NA").append("</PremiumBeforeST_Enhanced>").append("<GST_Standard>").append("NA").append("</GST_Standard>").append("<GST_Classic>").append(outputArr[4]).append("</GST_Classic>").append("<GST_Enhanced>").append("NA").append("</GST_Enhanced>").append("<PremiumWithServTax_Standard>").append("NA").append("</PremiumWithServTax_Standard>").append("<PremiumWithServTax_Classic>").append(outputArr[7]).append("</PremiumWithServTax_Classic>").append("<PremiumWithServTax_Enhanced>").append("NA").append("</PremiumWithServTax_Enhanced>").append(bussIll.toString());
                    //retVal.append("</SampoornCancerSuraksha>");
                    // System.out.println("output " + retVal.toString());
                } else {

                    retVal.append("<?xml version='1.0' encoding='utf-8' ?><SampoornCancerSuraksha>");
                    retVal.append("<errCode>0</errCode>");
                    retVal.append("<PremiumBeforeST_Standard>").append(outputArr[0]).append("</PremiumBeforeST_Standard>").append("<PremiumBeforeST_Classic>").append(outputArr[1]).append("</PremiumBeforeST_Classic>").append("<PremiumBeforeST_Enhanced>").append(outputArr[2]).append("</PremiumBeforeST_Enhanced>").append("<GST_Standard>").append(outputArr[3]).append("</GST_Standard>").append("<GST_Classic>").append(outputArr[4]).append("</GST_Classic>").append("<GST_Enhanced>").append(outputArr[5]).append("</GST_Enhanced>").append("<PremiumWithServTax_Standard>").append(outputArr[6]).append("</PremiumWithServTax_Standard>").append("<PremiumWithServTax_Classic>").append(outputArr[7]).append("</PremiumWithServTax_Classic>").append("<PremiumWithServTax_Enhanced>").append(outputArr[8]).append("</PremiumWithServTax_Enhanced>").append(bussIll.toString());
                    //retVal.append("</SampoornCancerSuraksha>");
                    // System.out.println("output " + retVal.toString());
                }

				/*
				  Added by vrushali on 19th DEC 2016 start
				 */
                //retVal.append("<?xml version='1.0' encoding='utf-8' ?><SampoornCancerSuraksha>");
                retVal.append("<staffStatus>").append(staffStatus).append("</staffStatus>");
                retVal.append("<staffRebate>").append(outputArr[9]).append("</staffRebate>");
                retVal.append("<FYServiceTax>").append(TotalTaxes).append("</FYServiceTax>");
                retVal.append("<InstmntPrem>").append(totInstPrem_exclST_exclDisc).append("</InstmntPrem>");
                retVal.append("<basicPremWithoutDisc>").append(totInstPrem_exclST_exclDisc).append("</basicPremWithoutDisc>");
                retVal.append("<basicPremWithoutDiscSA>").append(totInstPrem_exclST_exclDisc_exclFreqLoading).append("</basicPremWithoutDiscSA>");
                retVal.append("</SampoornCancerSuraksha>");

            }
        } catch (Exception e) {
            retVal.append("<?xml version='1.0' encoding='utf-8' ?><SampoornCancerSuraksha>" + "<errCode>1</errCode>" + "<errorMessage>").append(e.getMessage()).append("</errorMessage></SampoornCancerSuraksha>");
        }
        // System.out.print("Output"+ retVal);

    }

    private String[] getOutput(String string,
                               SampoornCancerSurakshaBean sampoorncancersurakshabean) {

        bussIll = new StringBuilder();

        // String BasePremiumRate =
        // commonForAllProd.getStringWithout_E(sampoorncancersurakshabusinesslogic.getBasePremiumRate(sampoorncancersurakshabean.getGender(),
        // sampoorncancersurakshabean.getPlantype(),
        // sampoorncancersurakshabean.getAge(),
        // sampoorncancersurakshabean.getPolicyterm()));

        String BasePremiumRate_Standard = commonForAllProd
                .getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getBasePremiumRate(
                                sampoorncancersurakshabean.getGender(),
                                "Standard",
                                sampoorncancersurakshabean.getAge(),
                                sampoorncancersurakshabean.getBasicTerm()));
        String BasePremiumRate_Classic = commonForAllProd
                .getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getBasePremiumRate(
                                sampoorncancersurakshabean.getGender(),
                                "Classic", sampoorncancersurakshabean.getAge(),
                                sampoorncancersurakshabean.getBasicTerm()));
        String BasePremiumRate_Enhanced = commonForAllProd
                .getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getBasePremiumRate(
                                sampoorncancersurakshabean.getGender(),
                                "Enhanced",
                                sampoorncancersurakshabean.getAge(),
                                sampoorncancersurakshabean.getBasicTerm()));

        String StaffRebate = commonForAllProd
                .getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getStaffRebate(sampoorncancersurakshabean
                                .getStaffDisc()));

        String SARebate = commonForAllProd
                .getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getSARebate(sampoorncancersurakshabean.getBasicSA()));

        // String BasePremiumRateWithoutSARebate =
        // commonForAllProd.getStringWithout_E(sampoorncancersurakshabusinesslogic.getBasePremiumRateWithoutSARebate(sampoorncancersurakshabean.getGender(),
        // sampoorncancersurakshabean.getPlantype(),
        // sampoorncancersurakshabean.getAge(),
        // sampoorncancersurakshabean.getPolicyterm(),sampoorncancersurakshabean.getSumAssured()))
        // ;

        String BasePremiumRateWithoutSARebate_Standard = commonForAllProd
                .getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getBasePremiumRateWithoutSARebate(
                                sampoorncancersurakshabean.getGender(),
                                "Standard",
                                sampoorncancersurakshabean.getAge(),
                                sampoorncancersurakshabean.getBasicTerm(),
                                sampoorncancersurakshabean.getBasicSA()));
        String BasePremiumRateWithoutSARebate_Classic = commonForAllProd
                .getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getBasePremiumRateWithoutSARebate(
                                sampoorncancersurakshabean.getGender(),
                                "Classic", sampoorncancersurakshabean.getAge(),
                                sampoorncancersurakshabean.getBasicTerm(),
                                sampoorncancersurakshabean.getBasicSA()));
        String BasePremiumRateWithoutSARebate_Enhanced = commonForAllProd
                .getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getBasePremiumRateWithoutSARebate(
                                sampoorncancersurakshabean.getGender(),
                                "Enhanced",
                                sampoorncancersurakshabean.getAge(),
                                sampoorncancersurakshabean.getBasicTerm(),
                                sampoorncancersurakshabean.getBasicSA()));

        // String StaffDiscountRate =
        // commonForAllProd.getStringWithout_E(sampoorncancersurakshabusinesslogic.getStaffDiscountRate(sampoorncancersurakshabean.getIsForStaffOrNot(),sampoorncancersurakshabean.getGender(),
        // sampoorncancersurakshabean.getPlantype(),
        // sampoorncancersurakshabean.getAge(),
        // sampoorncancersurakshabean.getPolicyterm(),sampoorncancersurakshabean.getSumAssured()));
        String StaffDiscountRate_Standard = commonForAllProd
                .getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getStaffDiscountRate(
                                sampoorncancersurakshabean.getStaffDisc(),
                                sampoorncancersurakshabean.getGender(),
                                "Standard",
                                sampoorncancersurakshabean.getAge(),
                                sampoorncancersurakshabean.getBasicTerm(),
                                sampoorncancersurakshabean.getBasicSA()));
        String StaffDiscountRate_Classic = commonForAllProd
                .getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getStaffDiscountRate(
                                sampoorncancersurakshabean.getStaffDisc(),
                                sampoorncancersurakshabean.getGender(),
                                "Classic", sampoorncancersurakshabean.getAge(),
                                sampoorncancersurakshabean.getBasicTerm(),
                                sampoorncancersurakshabean.getBasicSA()));
        String StaffDiscountRate_Enhanced = commonForAllProd
                .getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getStaffDiscountRate(
                                sampoorncancersurakshabean.getStaffDisc(),
                                sampoorncancersurakshabean.getGender(),
                                "Enhanced",
                                sampoorncancersurakshabean.getAge(),
                                sampoorncancersurakshabean.getBasicTerm(),
                                sampoorncancersurakshabean.getBasicSA()));

        // String FinalBasePremiumRate =
        // commonForAllProd.getStringWithout_E(sampoorncancersurakshabusinesslogic.getFinalBasePremiumRate(sampoorncancersurakshabean.getIsForStaffOrNot(),sampoorncancersurakshabean.getGender(),
        // sampoorncancersurakshabean.getPlantype(),
        // sampoorncancersurakshabean.getAge(),
        // sampoorncancersurakshabean.getPolicyterm(),sampoorncancersurakshabean.getSumAssured()));
        String FinalBasePremiumRate_Standard = commonForAllProd
                .getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getFinalBasePremiumRate(
                                sampoorncancersurakshabean.getStaffDisc(),
                                sampoorncancersurakshabean.getGender(),
                                "Standard",
                                sampoorncancersurakshabean.getAge(),
                                sampoorncancersurakshabean.getBasicTerm(),
                                sampoorncancersurakshabean.getBasicSA()));
        String FinalBasePremiumRate_Classic = commonForAllProd
                .getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getFinalBasePremiumRate(
                                sampoorncancersurakshabean.getStaffDisc(),
                                sampoorncancersurakshabean.getGender(),
                                "Classic", sampoorncancersurakshabean.getAge(),
                                sampoorncancersurakshabean.getBasicTerm(),
                                sampoorncancersurakshabean.getBasicSA()));
        String FinalBasePremiumRate_Enhanced = commonForAllProd
                .getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getFinalBasePremiumRate(
                                sampoorncancersurakshabean.getStaffDisc(),
                                sampoorncancersurakshabean.getGender(),
                                "Enhanced",
                                sampoorncancersurakshabean.getAge(),
                                sampoorncancersurakshabean.getBasicTerm(),
                                sampoorncancersurakshabean.getBasicSA()));

        // String AnnualPremium =
        // commonForAllProd.getStringWithout_E(sampoorncancersurakshabusinesslogic.getAnnualPremium(sampoorncancersurakshabean.getIsForStaffOrNot(),sampoorncancersurakshabean.getGender(),
        // sampoorncancersurakshabean.getPlantype(),
        // sampoorncancersurakshabean.getAge(),
        // sampoorncancersurakshabean.getPolicyterm(),sampoorncancersurakshabean.getSumAssured()));
        String AnnualPremium_Standard = commonForAllProd
                .getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getAnnualPremium(
                                sampoorncancersurakshabean.getStaffDisc(),
                                sampoorncancersurakshabean.getGender(),
                                "Standard",
                                sampoorncancersurakshabean.getAge(),
                                sampoorncancersurakshabean.getBasicTerm(),
                                sampoorncancersurakshabean.getBasicSA()));
        String AnnualPremium_Classic = commonForAllProd
                .getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getAnnualPremium(
                                sampoorncancersurakshabean.getStaffDisc(),
                                sampoorncancersurakshabean.getGender(),
                                "Classic", sampoorncancersurakshabean.getAge(),
                                sampoorncancersurakshabean.getBasicTerm(),
                                sampoorncancersurakshabean.getBasicSA()));
        String AnnualPremium_Enhanced = commonForAllProd
                .getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getAnnualPremium(
                                sampoorncancersurakshabean.getStaffDisc(),
                                sampoorncancersurakshabean.getGender(),
                                "Enhanced",
                                sampoorncancersurakshabean.getAge(),
                                sampoorncancersurakshabean.getBasicTerm(),
                                sampoorncancersurakshabean.getBasicSA()));

        // String PremiumBeforeST =
        // commonForAllProd.getStringWithout_E(sampoorncancersurakshabusinesslogic.getPremium(sampoorncancersurakshabean.getPremiumFrequency(),sampoorncancersurakshabean.getIsForStaffOrNot(),sampoorncancersurakshabean.getGender(),
        // sampoorncancersurakshabean.getPlantype(),
        // sampoorncancersurakshabean.getAge(),
        // sampoorncancersurakshabean.getPolicyterm(),sampoorncancersurakshabean.getSumAssured()));
        String PremiumBeforeST_Standard = commonForAllProd
                .getRoundUp(commonForAllProd.getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getPremium(sampoorncancersurakshabean.getPremFreq(),
                                sampoorncancersurakshabean.getStaffDisc(),
                                sampoorncancersurakshabean.getGender(),
                                "Standard",
                                sampoorncancersurakshabean.getAge(),
                                sampoorncancersurakshabean.getBasicTerm(),
                                sampoorncancersurakshabean.getBasicSA())));

        String PremiumBeforeST_Classic = commonForAllProd
                .getRoundUp(commonForAllProd.getRoundOffLevel2New(commonForAllProd.getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getPremium(sampoorncancersurakshabean.getPremFreq(),
                                sampoorncancersurakshabean.getStaffDisc(),
                                sampoorncancersurakshabean.getGender(),
                                "Classic", sampoorncancersurakshabean.getAge(),
                                sampoorncancersurakshabean.getBasicTerm(),
                                sampoorncancersurakshabean.getBasicSA()))));
        String PremiumBeforeST_Enhanced = commonForAllProd
                .getRoundUp(commonForAllProd.getRoundOffLevel2New(commonForAllProd.getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getPremium(sampoorncancersurakshabean.getPremFreq(),
                                sampoorncancersurakshabean.getStaffDisc(),
                                sampoorncancersurakshabean.getGender(),
                                "Enhanced",
                                sampoorncancersurakshabean.getAge(),
                                sampoorncancersurakshabean.getBasicTerm(),
                                sampoorncancersurakshabean.getBasicSA()))));

		/*
		  Added by vrushali on 12-May-2017
		 */

        String totInstPrem_exclST_exclDisc_Standard = commonForAllProd
                .getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getPremiumWithoutSTWithoutDisc(
                                sampoorncancersurakshabean.getPremFreq(),
                                sampoorncancersurakshabean.getStaffDisc(),
                                sampoorncancersurakshabean.getGender(),
                                "Standard",
                                sampoorncancersurakshabean.getAge(),
                                sampoorncancersurakshabean.getBasicTerm(),
                                sampoorncancersurakshabean.getBasicSA()));
        String totInstPrem_exclST_exclDisc_Classic = commonForAllProd
                .getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getPremiumWithoutSTWithoutDisc(
                                sampoorncancersurakshabean.getPremFreq(),
                                sampoorncancersurakshabean.getStaffDisc(),
                                sampoorncancersurakshabean.getGender(),
                                "Classic", sampoorncancersurakshabean.getAge(),
                                sampoorncancersurakshabean.getBasicTerm(),
                                sampoorncancersurakshabean.getBasicSA()));
        String totInstPrem_exclST_exclDisc_Enhanced = commonForAllProd
                .getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getPremiumWithoutSTWithoutDisc(
                                sampoorncancersurakshabean.getPremFreq(),
                                sampoorncancersurakshabean.getStaffDisc(),
                                sampoorncancersurakshabean.getGender(),
                                "Enhanced",
                                sampoorncancersurakshabean.getAge(),
                                sampoorncancersurakshabean.getBasicTerm(),
                                sampoorncancersurakshabean.getBasicSA()));

        // String totInstPrem_exclST_exclDisc_exclFreqLoading_Standard =
        // commonForAllProd
        // .getStringWithout_E(sampoorncancersurakshabusinesslogic
        // .getAnnualPremiumWithoutDiscWithoutFreqLoading(
        // sampoorncancersurakshabean.getStaffDisc(),
        // sampoorncancersurakshabean.getGender(),
        // "Standard",
        // sampoorncancersurakshabean.getAge(),
        // sampoorncancersurakshabean.getBasicTerm(),
        // sampoorncancersurakshabean.getBasicSA()));
        // String totInstPrem_exclST_exclDisc_exclFreqLoading_Classic =
        // commonForAllProd
        // .getStringWithout_E(sampoorncancersurakshabusinesslogic
        // .getAnnualPremiumWithoutDiscWithoutFreqLoading(
        // sampoorncancersurakshabean.getStaffDisc(),
        // sampoorncancersurakshabean.getGender(),
        // "Classic", sampoorncancersurakshabean.getAge(),
        // sampoorncancersurakshabean.getBasicTerm(),
        // sampoorncancersurakshabean.getBasicSA()));
        // String totInstPrem_exclST_exclDisc_exclFreqLoading_Enhanced =
        // commonForAllProd
        // .getStringWithout_E(sampoorncancersurakshabusinesslogic
        // .getAnnualPremiumWithoutDiscWithoutFreqLoading(
        // sampoorncancersurakshabean.getStaffDisc(),
        // sampoorncancersurakshabean.getGender(),
        // "Enhanced",
        // sampoorncancersurakshabean.getAge(),
        // sampoorncancersurakshabean.getBasicTerm(),
        // sampoorncancersurakshabean.getBasicSA()));

        String totInstPrem_exclST_exclDisc_exclFreqLoading_Standard = commonForAllProd
                .getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getAnnualPremiumWithoutDiscWithoutFreqLoadingWithSAZero(
                                sampoorncancersurakshabean.getStaffDisc(),
                                sampoorncancersurakshabean.getGender(),
                                "Standard",
                                sampoorncancersurakshabean.getAge(),
                                sampoorncancersurakshabean.getBasicTerm(),
                                sampoorncancersurakshabean.getBasicSA()));
        String totInstPrem_exclST_exclDisc_exclFreqLoading_Classic = commonForAllProd
                .getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getAnnualPremiumWithoutDiscWithoutFreqLoadingWithSAZero(
                                sampoorncancersurakshabean.getStaffDisc(),
                                sampoorncancersurakshabean.getGender(),
                                "Classic", sampoorncancersurakshabean.getAge(),
                                sampoorncancersurakshabean.getBasicTerm(),
                                sampoorncancersurakshabean.getBasicSA()));
        String totInstPrem_exclST_exclDisc_exclFreqLoading_Enhanced = commonForAllProd
                .getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getAnnualPremiumWithoutDiscWithoutFreqLoadingWithSAZero(
                                sampoorncancersurakshabean.getStaffDisc(),
                                sampoorncancersurakshabean.getGender(),
                                "Enhanced",
                                sampoorncancersurakshabean.getAge(),
                                sampoorncancersurakshabean.getBasicTerm(),
                                sampoorncancersurakshabean.getBasicSA()));

        // String loadingfreq =
        // commonForAllProd.getStringWithout_E(sampoorncancersurakshabusinesslogic.getLoadingFrequencyPremium(sampoorncancersurakshabean.getPremiumFrequency()));

        // String PremiumBeforeST =
        // commonForAllProd.getRoundOffLevel2New(commonForAllProd.getStringWithout_E(sampoorncancersurakshabusinesslogic.getPremiumbeforeST(sampoorncancersurakshabean.getGender(),
        // sampoorncancersurakshabean.getPlantype(),
        // sampoorncancersurakshabean.getAge(),
        // sampoorncancersurakshabean.getPolicyterm(),
        // sampoorncancersurakshabean.getSumAssured(),
        // sampoorncancersurakshabean.getIsForStaffOrNot(),
        // sampoorncancersurakshabean.getPremiumFrequency())));

        // String PremiumWithoutServTax =
        // commonForAllProd.getStringWithout_E(sampoorncancersurakshabusinesslogic.getMonthlyPremium_1stYear(sampoorncancersurakshabean.getGender(),
        // sampoorncancersurakshabean.getPlantype(),
        // sampoorncancersurakshabean.getAge(),
        // sampoorncancersurakshabean.getPolicyterm(),
        // sampoorncancersurakshabean.getSumAssured(),
        // sampoorncancersurakshabean.getIsForStaffOrNot(),
        // sampoorncancersurakshabean.getPremiumFrequency()));

        // String basicServiceTax =
        // commonForAllProd.getStringWithout_E(sampoorncancersurakshabusinesslogic.getBasicServiceTax(sampoorncancersurakshabean.getIsJammuResident(),sampoorncancersurakshabean.getGender(),
        // sampoorncancersurakshabean.getPlantype(),
        // sampoorncancersurakshabean.getAge(),
        // sampoorncancersurakshabean.getPolicyterm(),
        // sampoorncancersurakshabean.getSumAssured(),
        // sampoorncancersurakshabean.getIsForStaffOrNot(),
        // sampoorncancersurakshabean.getPremiumFrequency()));

        String basicServiceTax_Standard = commonForAllProd
                .getRoundUp(commonForAllProd.getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getBasicServiceTax(sampoorncancersurakshabean
                                        .isJKResident(),
                                PremiumBeforeST_Standard)));
        String basicServiceTax_Classic = commonForAllProd
                .getRoundUp(commonForAllProd.getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getBasicServiceTax(sampoorncancersurakshabean
                                        .isJKResident(),
                                PremiumBeforeST_Classic)));
        String basicServiceTax_Enhanced = commonForAllProd
                .getRoundUp(commonForAllProd.getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getBasicServiceTax(sampoorncancersurakshabean
                                        .isJKResident(),
                                PremiumBeforeST_Enhanced)));

        // String SBCServiceTax =
        // commonForAllProd.getStringWithout_E(sampoorncancersurakshabusinesslogic.getSBC(sampoorncancersurakshabean.getIsJammuResident(),sampoorncancersurakshabean.getGender(),
        // sampoorncancersurakshabean.getPlantype(),
        // sampoorncancersurakshabean.getAge(),
        // sampoorncancersurakshabean.getPolicyterm(),
        // sampoorncancersurakshabean.getSumAssured(),
        // sampoorncancersurakshabean.getIsForStaffOrNot(),
        // sampoorncancersurakshabean.getPremiumFrequency()));
        String SBCServiceTax_Standard = commonForAllProd
                .getRoundUp(commonForAllProd.getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getSBC(sampoorncancersurakshabean
                                        .isJKResident(),
                                PremiumBeforeST_Standard, sampoorncancersurakshabean.getServiceTax())));
        String SBCServiceTax_Classic = commonForAllProd
                .getRoundUp(commonForAllProd.getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getSBC(sampoorncancersurakshabean
                                        .isJKResident(),
                                PremiumBeforeST_Classic, sampoorncancersurakshabean.getServiceTax())));
        String SBCServiceTax_Enhanced = commonForAllProd
                .getRoundUp(commonForAllProd.getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getSBC(sampoorncancersurakshabean
                                        .isJKResident(),
                                PremiumBeforeST_Enhanced, sampoorncancersurakshabean.getServiceTax())));

        // String KKCServiceTax =
        // commonForAllProd.getStringWithout_E(sampoorncancersurakshabusinesslogic.getKKC(sampoorncancersurakshabean.getIsJammuResident(),sampoorncancersurakshabean.getGender(),
        // sampoorncancersurakshabean.getPlantype(),
        // sampoorncancersurakshabean.getAge(),
        // sampoorncancersurakshabean.getPolicyterm(),
        // sampoorncancersurakshabean.getSumAssured(),
        // sampoorncancersurakshabean.getIsForStaffOrNot(),
        // sampoorncancersurakshabean.getPremiumFrequency()));
        String KKCServiceTax_Standard = commonForAllProd
                .getRoundUp(commonForAllProd.getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getKKC(sampoorncancersurakshabean
                                        .isJKResident(),
                                PremiumBeforeST_Standard)));
        String KKCServiceTax_Classic = commonForAllProd
                .getRoundUp(commonForAllProd.getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getKKC(sampoorncancersurakshabean
                                        .isJKResident(),
                                PremiumBeforeST_Classic)));
        String KKCServiceTax_Enhanced = commonForAllProd
                .getRoundUp(commonForAllProd.getStringWithout_E(sampoorncancersurakshabusinesslogic
                        .getKKC(sampoorncancersurakshabean
                                        .isJKResident(),
                                PremiumBeforeST_Enhanced)));

        // String TotalTaxes =
        // commonForAllProd.getStringWithout_E(sampoorncancersurakshabusinesslogic.getTotalTaxes(sampoorncancersurakshabean.getIsJammuResident(),sampoorncancersurakshabean.getGender(),
        // sampoorncancersurakshabean.getPlantype(),
        // sampoorncancersurakshabean.getAge(),
        // sampoorncancersurakshabean.getPolicyterm(),
        // sampoorncancersurakshabean.getSumAssured(),
        // sampoorncancersurakshabean.getIsForStaffOrNot(),
        // sampoorncancersurakshabean.getPremiumFrequency()));
        String TotalTaxes_Standard = commonForAllProd.getStringWithout_E(Double
                .parseDouble(basicServiceTax_Standard)
                + Double.parseDouble(SBCServiceTax_Standard)
                + Double.parseDouble(KKCServiceTax_Standard));
        String TotalTaxes_Classic = commonForAllProd.getStringWithout_E(Double
                .parseDouble(basicServiceTax_Classic)
                + Double.parseDouble(SBCServiceTax_Classic)
                + Double.parseDouble(KKCServiceTax_Classic));
        String TotalTaxes_Enhanced = commonForAllProd.getStringWithout_E(Double
                .parseDouble(basicServiceTax_Enhanced)
                + Double.parseDouble(SBCServiceTax_Enhanced)
                + Double.parseDouble(KKCServiceTax_Enhanced));

        String PremiumWithServTax_Standard = commonForAllProd
                .getRoundUp(commonForAllProd.getStringWithout_E(Double
                        .parseDouble(PremiumBeforeST_Standard)
                        + Double.parseDouble(TotalTaxes_Standard)));
        String PremiumWithServTax_Classic = commonForAllProd
                .getRoundUp(commonForAllProd.getStringWithout_E(Double
                        .parseDouble(PremiumBeforeST_Classic)
                        + Double.parseDouble(TotalTaxes_Classic)));
        String PremiumWithServTax_Enhanced = commonForAllProd
                .getRoundUp(commonForAllProd.getStringWithout_E(Double
                        .parseDouble(PremiumBeforeST_Enhanced)
                        + Double.parseDouble(TotalTaxes_Enhanced)));

        // System.out.print("StaffDiscountRate_Standard = "+StaffDiscountRate_Standard+",StaffDiscountRate_Classic = "+StaffDiscountRate_Classic+",StaffDiscountRate_Enhanced="+StaffDiscountRate_Enhanced);

        return new String[]{PremiumBeforeST_Standard,
                PremiumBeforeST_Classic, PremiumBeforeST_Enhanced,
                TotalTaxes_Standard, TotalTaxes_Classic, TotalTaxes_Enhanced,
                PremiumWithServTax_Standard, PremiumWithServTax_Classic,
                PremiumWithServTax_Enhanced, StaffRebate,
                totInstPrem_exclST_exclDisc_Standard,
                totInstPrem_exclST_exclDisc_Classic,
                totInstPrem_exclST_exclDisc_Enhanced,
                totInstPrem_exclST_exclDisc_exclFreqLoading_Standard,
                totInstPrem_exclST_exclDisc_exclFreqLoading_Classic,
                totInstPrem_exclST_exclDisc_exclFreqLoading_Enhanced};

    }

    private void Dialog() {

        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));
        d.setContentView(R.layout.layout_sampoorn_cancer_suraksha_bi_grid);

        TextView tv_proposername = d
                .findViewById(R.id.tv_sampoorn_cancer_suraksha_proposername);


        TextView tv_bi_is_state = (TextView) d
                .findViewById(R.id.tv_bi_is_state);

        if (cb_kerladisc.isChecked()) {
            tv_bi_is_state.setText("Kerala");
        } else {
            tv_bi_is_state.setText("Non Kerala");
        }


        TextView tv_proposal_number = d
                .findViewById(R.id.tv_sampoorn_cancer_suraksha_proposal_number);
        TextView tv_bi_is_Staff = d
                .findViewById(R.id.tv_bi_is_Staff);
        TextView tv_bi_is_jk = d.findViewById(R.id.tv_bi_is_jk);

        TextView tv_bi_sampoorn_cancer_suraksha_plan = d
                .findViewById(R.id.tv_bi_sampoorn_cancer_suraksha_plan);
        TextView tv_life_age_at_entry = d
                .findViewById(R.id.tv_bi_sampoorn_cancer_suraksha_life_assured_age);
        TextView tv_life_assured_gender = d
                .findViewById(R.id.tv_bi_sampoorn_cancer_suraksha_life_assured_gender);
        TextView tv_premPaymentfrequency = d
                .findViewById(R.id.tv_bi_sampoorn_cancer_suraksha_frequency);

        TextView tv_basic_cover_term = d
                .findViewById(R.id.tv_sampoorn_cancer_suraksha_term_basic_cover);
        TextView tv_basic_cover_sum_assured = d
                .findViewById(R.id.tv_sampoorn_cancer_suraksha_sum_assured_basic_cover);

        TextView tv_bi_sampoorn_cancer_suraksha_frequency_premium = d
                .findViewById(R.id.tv_for_frequency_premium);
        TextView tv_bi_sampoorn_cancer_suraksha_frequency_premium_for_service_tax = d
                .findViewById(R.id.tv_for_frequency_premium_for_service_Tax);

        final EditText edt_Policyholderplace = d
                .findViewById(R.id.edt_sampoorn_cancer_suraksha_Policyholderplace);

        final TextView edt_proposer_name = d
                .findViewById(R.id.edt_sampoorn_cancer_suraksha_proposer_name);


        final TextView edt_proposer_name_e_sign = d
                .findViewById(R.id.edt_proposer_name_e_sign);


        final CheckBox cb_statement = d
                .findViewById(R.id.cb_sampoorn_cancer_suraksha_statement);

        TextView tv_bi_sampoorn_cancer_suraksha_installment_premium_standard = d
                .findViewById(R.id.tv_bi_sampoorn_cancer_suraksha_installment_premium_standard);

        TextView tv_bi_sampoorn_cancer_suraksha_basic_service_tax_standard = d
                .findViewById(R.id.tv_bi_sampoorn_cancer_suraksha_basic_service_tax_standard);

        TextView tv_bi_sampoorn_cancer_suraksha_installment_premium_with_tax_standard = d
                .findViewById(R.id.tv_bi_sampoorn_cancer_suraksha_installment_premium_with_tax_standard);

        TextView tv_bi_sampoorn_cancer_suraksha_installment_premium_classic = d
                .findViewById(R.id.tv_bi_sampoorn_cancer_suraksha_installment_premium_classic);
        TextView tv_bi_sampoorn_cancer_suraksha_basic_service_tax_classic = d
                .findViewById(R.id.tv_bi_sampoorn_cancer_suraksha_basic_service_tax_classic);

        TextView tv_Company_policy_surrender_dec = d.findViewById(R.id.tv_Company_policy_surrender_dec);

        TextView tv_bi_sampoorn_cancer_suraksha_installment_premium_with_tax_classic = d
                .findViewById(R.id.tv_bi_sampoorn_cancer_suraksha_installment_premium_with_tax_classic);

        TextView tv_bi_sampoorn_cancer_suraksha_installment_premium_enhanced = d
                .findViewById(R.id.tv_bi_sampoorn_cancer_suraksha_installment_premium_enhanced);
        TextView tv_bi_sampoorn_cancer_suraksha_basic_service_tax_enhanced = d
                .findViewById(R.id.tv_bi_sampoorn_cancer_suraksha_basic_service_tax_enhanced);

        TextView tv_bi_sampoorn_cancer_suraksha_installment_premium_with_tax_enhanced = d
                .findViewById(R.id.tv_bi_sampoorn_cancer_suraksha_installment_premium_with_tax_enhanced);

        /* Need Analysis */
        final TextView edt_proposer_name_need_analysis = d
                .findViewById(R.id.edt_proposer_name_need_analysis);

        final CheckBox cb_statement_need_analysis = d
                .findViewById(R.id.cb_statement_need_analysis);
        cb_statement_need_analysis.setChecked(true);
        TableRow tr_need_analysis = d
                .findViewById(R.id.tr_need_analysis);

        /*
         * if (NeedAnalysisActivity.str_need_analysis != null) { if
         * (NeedAnalysisActivity.str_need_analysis.equals("1")) {
         *
         * File(tempDir + direct + "/");
         *
         * File mypath_old = new File(folder, "NA" + ".pdf"); File mypath_new =
         * new File(folder, QuatationNumber + "_NA" + ".pdf"); if
         * (mypath_old.exists()) { mypath_old.renameTo(mypath_new); }
         * flg_needAnalyis = "1"; tr_need_analysis.setVisibility(View.VISIBLE);
         * } else { cb_statement_need_analysis.setChecked(true);
         * tr_need_analysis.setVisibility(View.GONE); }
         *
         * }
         */

        Button btn_proceed = d
                .findViewById(R.id.btn_sampoorn_cancer_suraksha_proceed);

        Button btn_sampoorn_cancer_suraksha_MarketingOfficalDate = d
                .findViewById(R.id.btn_sampoorn_cancer_suraksha_MarketingOfficalDate);
        btn_PolicyholderDate = d
                .findViewById(R.id.btn_sampoorn_cancer_suraksha_PolicyholderDate);

        Ibtn_signatureofMarketing = d
                .findViewById(R.id.Ibtn_sampoorn_cancer_suraksha_signatureofMarketing);
        Ibtn_signatureofPolicyHolders = d
                .findViewById(R.id.Ibtn_sampoorn_cancer_suraksha_signatureofPolicyHolders);

        // getValueFromDatabase();

        if (!proposer_sign.equals("")) {
            String flg_needAnalyis = "";
            if (flg_needAnalyis.equals("1")) {
                tr_need_analysis.setVisibility(View.VISIBLE);
            } else {
                cb_statement_need_analysis.setChecked(true);
                tr_need_analysis.setVisibility(View.GONE);
            }
        }
        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
            // edt_proposer_name.setText(name_of_life_assured);

            edt_proposer_name
                    .setText("I, "
                            + name_of_life_assured
                            + ", having received the information with respect to the above, have understood the above statement before entering into the contract.");

            edt_proposer_name_e_sign.setText("I, "
                    + name_of_life_assured
                    + ", accept and agree to affix my electronic signature here.");

            // edt_proposer_name_need_analysis.setText(name_of_life_assured);

            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_life_assured
                            + ", have undergone the Need Analysis after having reviewed the SBI Life Product options, I have opted for SBI LIFE-Sampoorn Cancer Suraksha.");

            // tv_life_assured_name.setText(name_of_life_assured);
            tv_proposername.setText(name_of_life_assured);
        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
            // edt_proposer_name.setText(name_of_proposer);
            // edt_proposer_name_e_sign.setText(name_of_proposer);
            // edt_proposer_name_need_analysis.setText(name_of_proposer);

            edt_proposer_name
                    .setText("I, "
                            + name_of_proposer
                            + ", having received the information with respect to the above, have understood the above statement before entering into the contract.");

            edt_proposer_name_e_sign.setText("I, "
                    + name_of_proposer
                    + ", accept and agree to affix my electronic signature here.");

            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_proposer
                            + ", have undergone the Need Analysis after having reviewed the SBI Life Product options, I have opted for SBI LIFE-Sampoorn Cancer Suraksha.");

            tv_proposername.setText(name_of_proposer);

            // #######
            LinearLayout linearlayoutLifeAssuredSignature = d
                    .findViewById(R.id.linearlayoutLifeAssuredSignature);
            Ibtn_signatureofLifeAssured = d
                    .findViewById(R.id.Ibtn_signatureofLifeAssured);

            linearlayoutLifeAssuredSignature.setVisibility(View.GONE);
            if (proposerAsLifeAssuredSign != null
                    && !proposerAsLifeAssuredSign.equals("")) {
                byte[] signByteArray = Base64.decode(proposerAsLifeAssuredSign,
                        0);
                Bitmap bitmap = BitmapFactory.decodeByteArray(signByteArray, 0,
                        signByteArray.length);
                Ibtn_signatureofLifeAssured.setImageBitmap(bitmap);
            }

            Ibtn_signatureofLifeAssured
                    .setOnClickListener(new OnClickListener() {

                        public void onClick(View view) {

                            if (cb_statement.isChecked()
                                    && cb_statement_need_analysis.isChecked()) {
                                latestImage = "proposerSignOnLifeAssured";
                                commonMethods.windowmessageProposersgin(
                                        context, NeedAnalysisActivity.URN_NO
                                                + "_cust2sign");
                            } else {
                                commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                                setFocusable(cb_statement);
                                cb_statement.requestFocus();
                            }

                        }
                    });
        }

        tv_proposal_number.setText(QuatationNumber);


        if (!date2.equals("")) {
            btn_PolicyholderDate.setText(getDate(date2));
        } else {
            date2 = getDate1(getCurrentDate());
            btn_PolicyholderDate.setText(getCurrentDate());
        }
        if (!date1.equals("")) {
            btn_sampoorn_cancer_suraksha_MarketingOfficalDate.setText(getDate(date1));
        } else {
            date1 = getDate1(getCurrentDate());
            btn_sampoorn_cancer_suraksha_MarketingOfficalDate.setText(getCurrentDate());
        }

        if (agent_sign != null && !agent_sign.equals("")) {
            cb_statement.setChecked(true);
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

        imageButtonSmartPrivilegeProposerPhotograph = d
                .findViewById(R.id.imageButtonSmartPrivilegeProposerPhotograph);
        imageButtonSmartPrivilegeProposerPhotograph
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Check = "Photo";
                        commonMethods.windowmessage(context, "_cust1Photo.jpg");
                    }
                });

        Ibtn_signatureofMarketing
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cb_statement.isChecked()
                                && cb_statement_need_analysis.isChecked()) {
                            latestImage = "agent";
                            windowmessagesgin();
                        } else {
                            commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                            setFocusable(cb_statement);
                            cb_statement.requestFocus();
                        }

                    }
                });

        Ibtn_signatureofPolicyHolders
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cb_statement.isChecked()
                                && cb_statement_need_analysis.isChecked()) {
                            latestImage = "proposer";
                            commonMethods.windowmessageProposersgin(context,
                                    NeedAnalysisActivity.URN_NO + "_cust1sign");
                        } else {
                            commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                            setFocusable(cb_statement);
                            cb_statement.requestFocus();
                        }

                    }
                });

        linearlayoutThirdpartySignature = d
                .findViewById(R.id.linearlayoutThirdpartySignature);
        Ibtn_signatureofThirdParty = d
                .findViewById(R.id.Ibtn_signatureofThirdParty);

        if (thirdPartySign != null && !thirdPartySign.equals("")) {
            byte[] signByteArray = Base64.decode(thirdPartySign, 0);
            Bitmap bitmap = BitmapFactory.decodeByteArray(signByteArray, 0,
                    signByteArray.length);
            Ibtn_signatureofThirdParty.setImageBitmap(bitmap);
        }

        final RadioButton radioButtonDepositPaymentYes = d
                .findViewById(R.id.radioButtonDepositPaymentYes);
        final RadioButton radioButtonDepositPaymentNo = d
                .findViewById(R.id.radioButtonDepositPaymentNo);

        radioButtonDepositPaymentNo
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {
                            linearlayoutThirdpartySignature
                                    .setVisibility(View.VISIBLE);
                        } else {
                            linearlayoutThirdpartySignature
                                    .setVisibility(View.GONE);

                            String thirdyPartySignName = NeedAnalysisActivity.URN_NO
                                    + "_thirdParty.png";
                            File thirdyPartySignFile = mStorageUtils.createFileToAppSpecificDir(context,
                                    thirdyPartySignName);

                            if (thirdyPartySignFile.exists()) {
                                thirdyPartySignFile.delete();
                            }

                            thirdPartySign = "";
                            Ibtn_signatureofThirdParty.setImageBitmap(null);

                        }
                    }
                });

        Ibtn_signatureofThirdParty
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {

                        if (cb_statement.isChecked()
                                && cb_statement_need_analysis.isChecked()) {
                            latestImage = "thirdParty";
                            commonMethods
                                    .windowmessageProposersgin(context,
                                            NeedAnalysisActivity.URN_NO
                                                    + "_thirdParty");
                        } else {
                            commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                            setFocusable(cb_statement);
                            cb_statement.requestFocus();
                        }

                    }
                });

        // ////

        linearlayoutAppointeeSignature = d
                .findViewById(R.id.linearlayoutAppointeeSignature);
        Ibtn_signatureofAppointee = d
                .findViewById(R.id.Ibtn_signatureofAppointee);

        if (appointeeSign != null && !appointeeSign.equals("")) {
            byte[] signByteArray = Base64.decode(appointeeSign, 0);
            Bitmap bitmap = BitmapFactory.decodeByteArray(signByteArray, 0,
                    signByteArray.length);
            Ibtn_signatureofAppointee.setImageBitmap(bitmap);
        }

        final RadioButton radioButtonAppointeeYes = d
                .findViewById(R.id.radioButtonAppointeeYes);
        final RadioButton radioButtonAppointeeNo = d
                .findViewById(R.id.radioButtonAppointeeNo);

        radioButtonAppointeeYes
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {
                            linearlayoutAppointeeSignature
                                    .setVisibility(View.VISIBLE);
                        } else {
                            linearlayoutAppointeeSignature
                                    .setVisibility(View.GONE);
                            String appointeeSignName = NeedAnalysisActivity.URN_NO
                                    + "_appointee.png";

                            File appointeeSignFile = mStorageUtils.createFileToAppSpecificDir(context,
                                    appointeeSignName);
                            if (appointeeSignFile.exists()) {
                                appointeeSignFile.delete();
                            }

                            appointeeSign = "";
                            Ibtn_signatureofAppointee.setImageBitmap(null);
                        }
                    }
                });

        if (photoBitmap != null) {
            imageButtonSmartPrivilegeProposerPhotograph
                    .setImageBitmap(photoBitmap);
        }

        Ibtn_signatureofAppointee
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {

                        if (cb_statement.isChecked()
                                && cb_statement_need_analysis.isChecked()) {
                            latestImage = "Appointee";
                            commonMethods.windowmessageProposersgin(context,
                                    NeedAnalysisActivity.URN_NO + "_appointee");
                        } else {
                            commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                            setFocusable(cb_statement);
                            cb_statement.requestFocus();
                        }

                    }
                });

        final RadioButton radioButtonTrasactionModeManual = d
                .findViewById(R.id.radioButtonTrasactionModeManual);
        final RadioButton radioButtonTrasactionModeParivartan = d
                .findViewById(R.id.radioButtonTrasactionModeParivartan);
        final LinearLayout linearlayoutTrasactionModeParivartan = d
                .findViewById(R.id.linearlayoutTrasactionModeParivartan);

        radioButtonTrasactionModeParivartan
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {
                            linearlayoutTrasactionModeParivartan
                                    .setVisibility(View.VISIBLE);
                        } else {
                            linearlayoutTrasactionModeParivartan
                                    .setVisibility(View.GONE);

                            String appointeeSignName = NeedAnalysisActivity.URN_NO
                                    + "_appointee.png";

                            File appointeeSignFile = mStorageUtils.createFileToAppSpecificDir(context,
                                    appointeeSignName);
                            if (appointeeSignFile.exists()) {
                                appointeeSignFile.delete();
                            }

                            appointeeSign = "";
                            Ibtn_signatureofAppointee.setImageBitmap(null);

                            String thirdyPartySignName = NeedAnalysisActivity.URN_NO
                                    + "_thirdParty.png";

                            File thirdyPartySignFile = mStorageUtils.createFileToAppSpecificDir(context,
                                    thirdyPartySignName);

                            if (thirdyPartySignFile.exists()) {
                                thirdyPartySignFile.delete();
                            }

                            thirdPartySign = "";
                            Ibtn_signatureofThirdParty.setImageBitmap(null);

                            String customerPhotoName = NeedAnalysisActivity.URN_NO
                                    + "_cust1Photo.jpg";
                            File customerPhotoFile = mStorageUtils.createFileToAppSpecificDir(context,
                                    customerPhotoName);
                            if (customerPhotoFile.exists()) {
                                customerPhotoFile.delete();
                            }

                            photoBitmap = null;
                            imageButtonSmartPrivilegeProposerPhotograph
                                    .setImageDrawable(getResources()
                                            .getDrawable(
                                                    R.drawable.focus_imagebutton_photo));

                            RadioGroup radioGroupDepositPayment = d
                                    .findViewById(R.id.radioGroupDepositPayment);
                            radioGroupDepositPayment.clearCheck();

                            RadioGroup radioGroupAppointee = d
                                    .findViewById(R.id.radioGroupAppointee);
                            radioGroupAppointee.clearCheck();

                            if (proposer_Is_Same_As_Life_Assured
                                    .equalsIgnoreCase("n")) {

                                String lifeassuredSignName = NeedAnalysisActivity.URN_NO
                                        + "_cust2sign.png";

                                File lifeassuredSignFile = mStorageUtils.createFileToAppSpecificDir(context,
                                        lifeassuredSignName);

                                if (lifeassuredSignFile.exists()) {
                                    lifeassuredSignFile.delete();
                                }

                                proposerAsLifeAssuredSign = "";
                                Ibtn_signatureofLifeAssured
                                        .setImageBitmap(null);
                            }
                        }
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

        if (mode.equalsIgnoreCase("Manual")) {
            radioButtonTrasactionModeManual.setChecked(true);
        } else if (mode.equalsIgnoreCase("Parivartan")) {
            radioButtonTrasactionModeParivartan.setChecked(true);
        }

        if (!TextUtils.isEmpty(place2)) {
            edt_Policyholderplace.setText(place2);
        }


        btn_proceed.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                name_of_person = edt_proposer_name.getText().toString();
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
                        && (cb_statement.isChecked())
                        && (((photoBitmap != null
                        //remove parivartan validation
								/*&& ((radioButtonDepositPaymentNo.isChecked() == true && !thirdPartySign
						.equals("")) || radioButtonDepositPaymentYes
						.isChecked() == true)
						&& ((radioButtonAppointeeYes.isChecked() == true && !appointeeSign
						.equals("")) || radioButtonAppointeeNo
						.isChecked() == true) && ((!proposerAsLifeAssuredSign
								.equalsIgnoreCase("y"))*/
                ) && radioButtonTrasactionModeParivartan
                        .isChecked()) || radioButtonTrasactionModeManual
                        .isChecked())) {
                    NeedAnalysisActivity.str_need_analysis = "";

                    switch (sampoornCancerSurakshaBean.getPlanName()) {
                        case "Standard":
                            premiumAmt = PremiumWithServTax_Standard;

                            break;
                        case "Classic":
                            premiumAmt = PremiumWithServTax_Classic;
                            break;
                        case "Enhanced":
                            premiumAmt = PremiumWithServTax_Enhanced;
                            break;
                    }

                    na_cbi_bean = new NA_CBI_bean(QuatationNumber, agentcode,
                            "", userType, "", lifeAssured_Title,
                            lifeAssured_First_Name, lifeAssured_Middle_Name,
                            lifeAssured_Last_Name, planName,
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(sumAssured.equals("") ? "0"
                                            : sumAssured))), obj
                            .getRound(premiumAmt), emailId, mobileNo,
                            agentEmail, agentMobile, na_input, na_output,
                            premPayingMode, Integer.parseInt(policyTerm), 0,
                            product_Code, getDate(lifeAssured_date_of_birth),
                            getDate(proposer_date_of_birth), inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), ""));

                    System.out.println("na_cbi_bean>tostring:"
                            + na_cbi_bean.toString());

                    name_of_person = name_of_life_assured;


                    if (radioButtonTrasactionModeParivartan.isChecked()) {
                        mode = "Parivartan";
                    } else if (radioButtonTrasactionModeManual.isChecked()) {
                        mode = "Manual";
                    }

                    db.AddNeedAnalysisDashboardDetails(new ProductBIBean("",
                            QuatationNumber, planName, getCurrentDate(),
                            mobileNo, getCurrentDate(), db.GetUserCode(),
                            emailId, "", "", agentcode, "", userType, "",
                            lifeAssured_Title, lifeAssured_First_Name,
                            lifeAssured_Middle_Name, lifeAssured_Last_Name,
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(sumAssured.equals("") ? "0"
                                            : sumAssured))), obj
                            .getRound(premiumAmt), agentEmail,
                            agentMobile, na_input, na_output, premPayingMode,
                            Integer.parseInt(policyTerm), 0, product_Code,
                            getDate(lifeAssured_date_of_birth),
                            getDate(proposer_date_of_birth), "", mode, inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), "")));

                    createPdf();


                    NABIObj.serviceHit(context, na_cbi_bean, newFile,
                            needAnalysispath.getPath(), mypath.getPath(),
                            name_of_person, QuatationNumber, mode);

                    d.dismiss();

                } else {

                    if (proposer_sign.equals("")
                            && !bankUserType.equalsIgnoreCase("Y")) {
                        commonMethods.dialogWarning(context, "Please Make Signature for Proposer ", true);
                        setFocusable(Ibtn_signatureofPolicyHolders);
                        Ibtn_signatureofPolicyHolders.requestFocus();
                    } else if (place2.equals("")) {
                        commonMethods.dialogWarning(context, "Please Fill Place Detail", true);
                        setFocusable(edt_Policyholderplace);
                        edt_Policyholderplace.requestFocus();

                    } else if (agent_sign.equals("")
                            && !bankUserType.equalsIgnoreCase("Y")) {
                        commonMethods.dialogWarning(context, "Please Make Signature for Sales Representative",
                                true);
                        setFocusable(Ibtn_signatureofMarketing);
                        Ibtn_signatureofMarketing.requestFocus();
                    } else if (!cb_statement.isChecked()) {
                        commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                        setFocusable(cb_statement);
                        cb_statement.requestFocus();
                    } else if (!radioButtonTrasactionModeParivartan.isChecked()
                            && !radioButtonTrasactionModeManual.isChecked()) {
                        commonMethods.dialogWarning(context, "Please Select Transaction Mode", true);
                        setFocusable(linearlayoutTrasactionModeParivartan);
                        linearlayoutTrasactionModeParivartan.requestFocus();
                    } else if (photoBitmap == null) {
                        commonMethods.dialogWarning(context, "Please Capture the Photo", true);
                        setFocusable(imageButtonSmartPrivilegeProposerPhotograph);
                        imageButtonSmartPrivilegeProposerPhotograph
                                .requestFocus();
                    } else if (!radioButtonDepositPaymentYes.isChecked()
                            && !radioButtonDepositPaymentNo.isChecked()) {
                        commonMethods.dialogWarning(context, "Please Select Third Party Payment", true);
                        setFocusable(linearlayoutThirdpartySignature);
                        linearlayoutThirdpartySignature.requestFocus();
                    } else if (radioButtonDepositPaymentNo.isChecked()
                            && thirdPartySign.equals("")) {
                        commonMethods.dialogWarning(context, "Please Make Signature for Third party ", true);
                        setFocusable(Ibtn_signatureofThirdParty);
                        Ibtn_signatureofThirdParty.requestFocus();
                    } else if (!radioButtonAppointeeYes.isChecked()
                            && !radioButtonAppointeeNo.isChecked()) {
                        commonMethods.dialogWarning(context, "Please Select Appointee Payment", true);
                        setFocusable(linearlayoutAppointeeSignature);
                        linearlayoutAppointeeSignature.requestFocus();
                    } else if (radioButtonAppointeeYes.isChecked()
                            && appointeeSign.equals("")) {
                        commonMethods.dialogWarning(context, "Please Make Signature for Appointee ", true);
                        setFocusable(Ibtn_signatureofAppointee);
                        Ibtn_signatureofAppointee.requestFocus();
                    } else if (proposer_Is_Same_As_Life_Assured
                            .equalsIgnoreCase("n")
                            && proposerAsLifeAssuredSign.equals("")) {
                        commonMethods.dialogWarning(context, "Please Make Signature for Life Assured ", true);
                        setFocusable(Ibtn_signatureofLifeAssured);
                        Ibtn_signatureofLifeAssured.requestFocus();
                    }
                }

            }
        });

        btn_PolicyholderDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                DIALOG_ID = 2;
                showDialog(DATE_DIALOG_ID);
            }
        });


        String input = inputVal.toString();
        String output = retVal.toString();
        staffdiscount = prsObj.parseXmlTag(input, "isStaff");
        if (staffdiscount.equalsIgnoreCase("true")) {
            tv_bi_is_Staff.setText("Yes");
        } else {
            tv_bi_is_Staff.setText("No");
        }
        isJkResident = prsObj.parseXmlTag(input, "isJKResident");

        if (isJkResident.equalsIgnoreCase("true")) {
            tv_bi_is_jk.setText("Yes");
        } else {
            tv_bi_is_jk.setText("No");
        }

        ageAtEntry = prsObj.parseXmlTag(input, "age");
        tv_life_age_at_entry.setText(ageAtEntry + " Years");

        gender = prsObj.parseXmlTag(input, "gender");
        tv_life_assured_gender.setText(gender);

        premPayingMode = prsObj.parseXmlTag(input, "premFreq");
        tv_premPaymentfrequency.setText(premPayingMode);

        if (premPayingMode.equalsIgnoreCase("Half Yearly")) {

        } else if (premPayingMode.equalsIgnoreCase("Quarterly")) {

        } else if (premPayingMode.equalsIgnoreCase("Monthly")) {

        }

        policyTerm = prsObj.parseXmlTag(input, "policyTerm");
        tv_basic_cover_term.setText(policyTerm + " Years");

        planProposedName = prsObj.parseXmlTag(input, "plan");
        tv_bi_sampoorn_cancer_suraksha_plan.setText(planProposedName);

        sumAssured = prsObj.parseXmlTag(input, "sumAssured");

        tv_basic_cover_sum_assured.setText("Rs. "
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj
                .getStringWithout_E(Double.valueOf((sumAssured
                        .equals("") || sumAssured == null) ? "0"
                        : sumAssured))))));

        // Changes done by amit
		/*tv_bi_sampoorn_cancer_suraksha_frequency_premium
				.setText((premPayingMode + " Premium (Rs.)"));
		tv_bi_sampoorn_cancer_suraksha_frequency_premium_for_service_tax
				.setText((premPayingMode + " Premium with GST(Rs.)"));
*/
        // Standard

        PremiumBeforeST_Standard = prsObj.parseXmlTag(output,
                "PremiumBeforeST_Standard");

        if (PremiumBeforeST_Standard != null
                && !PremiumBeforeST_Standard.equalsIgnoreCase("NA")) {

            PremiumBeforeST_Standard = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "PremiumBeforeST_Standard"))) + "";
            tv_bi_sampoorn_cancer_suraksha_installment_premium_standard
                    .setText(PremiumBeforeST_Standard);
        } else {
            PremiumBeforeST_Standard = "NA";
            tv_bi_sampoorn_cancer_suraksha_installment_premium_standard
                    .setText(PremiumBeforeST_Standard);
        }

        PremiumWithServTax_Standard = prsObj.parseXmlTag(output,
                "PremiumWithServTax_Standard");

        if (PremiumWithServTax_Standard != null
                && !PremiumWithServTax_Standard.equalsIgnoreCase("NA")) {
            PremiumWithServTax_Standard = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "PremiumWithServTax_Standard"))) + "";
            tv_bi_sampoorn_cancer_suraksha_installment_premium_with_tax_standard
                    .setText(PremiumWithServTax_Standard);
        } else {
            PremiumWithServTax_Standard = "NA";
            tv_bi_sampoorn_cancer_suraksha_installment_premium_with_tax_standard
                    .setText(PremiumWithServTax_Standard);
        }

        GST_Standard = prsObj.parseXmlTag(output, "GST_Standard");

        if (GST_Standard != null && !GST_Standard.equalsIgnoreCase("NA")) {

            tv_bi_sampoorn_cancer_suraksha_basic_service_tax_standard
                    .setText(""
                            + obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(GST_Standard.equals("") ? "0"
                                    : GST_Standard))));
        } else {
            GST_Standard = "NA";
            tv_bi_sampoorn_cancer_suraksha_basic_service_tax_standard
                    .setText(GST_Standard);
        }


        // Classic
        PremiumBeforeST_Classic = prsObj.parseXmlTag(output,
                "PremiumBeforeST_Classic");
        if (PremiumBeforeST_Classic != null
                && !PremiumBeforeST_Classic.equalsIgnoreCase("NA")) {
            PremiumBeforeST_Classic = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "PremiumBeforeST_Classic"))) + "";
            tv_bi_sampoorn_cancer_suraksha_installment_premium_classic
                    .setText(PremiumBeforeST_Classic);
        } else {
            PremiumBeforeST_Classic = "NA";
            tv_bi_sampoorn_cancer_suraksha_installment_premium_classic
                    .setText(PremiumBeforeST_Classic);
        }

        PremiumWithServTax_Classic = prsObj.parseXmlTag(output,
                "PremiumWithServTax_Classic");
        if (PremiumWithServTax_Classic != null
                && !PremiumWithServTax_Classic.equalsIgnoreCase("NA")) {
            PremiumWithServTax_Classic = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "PremiumWithServTax_Classic"))) + "";
            tv_bi_sampoorn_cancer_suraksha_installment_premium_with_tax_classic
                    .setText(PremiumWithServTax_Classic);

        } else {
            PremiumWithServTax_Classic = "NA";
            tv_bi_sampoorn_cancer_suraksha_installment_premium_with_tax_classic
                    .setText(PremiumWithServTax_Classic);
        }

        GST_Classic = prsObj.parseXmlTag(output, "GST_Classic");
        if (GST_Classic != null && !GST_Classic.equalsIgnoreCase("NA")) {
            tv_bi_sampoorn_cancer_suraksha_basic_service_tax_classic
                    .setText(""
                            + obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(GST_Classic.equals("") ? "0"
                                    : GST_Classic))));
        } else {
            GST_Classic = "NA";
            tv_bi_sampoorn_cancer_suraksha_basic_service_tax_classic
                    .setText(GST_Classic);
        }


        // Enhanced
        PremiumBeforeST_Enhanced = prsObj.parseXmlTag(output,
                "PremiumBeforeST_Enhanced");
        if (PremiumBeforeST_Enhanced != null
                && !PremiumBeforeST_Enhanced.equalsIgnoreCase("NA")) {
            PremiumBeforeST_Enhanced = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "PremiumBeforeST_Enhanced"))) + "";
            tv_bi_sampoorn_cancer_suraksha_installment_premium_enhanced
                    .setText(PremiumBeforeST_Enhanced);
        } else {
            PremiumBeforeST_Enhanced = "NA";
            tv_bi_sampoorn_cancer_suraksha_installment_premium_enhanced
                    .setText(PremiumBeforeST_Enhanced);
        }

        PremiumWithServTax_Enhanced = prsObj.parseXmlTag(output,
                "PremiumWithServTax_Enhanced");
        if (PremiumWithServTax_Enhanced != null
                && !PremiumWithServTax_Enhanced.equalsIgnoreCase("NA")) {
            PremiumWithServTax_Enhanced = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "PremiumWithServTax_Enhanced"))) + "";
            tv_bi_sampoorn_cancer_suraksha_installment_premium_with_tax_enhanced
                    .setText(PremiumWithServTax_Enhanced);
        } else {
            PremiumWithServTax_Enhanced = "NA";
            tv_bi_sampoorn_cancer_suraksha_installment_premium_with_tax_enhanced
                    .setText(PremiumWithServTax_Enhanced);
        }

        GST_Enhanced = prsObj.parseXmlTag(output, "GST_Enhanced");
        if (GST_Enhanced != null && !GST_Enhanced.equalsIgnoreCase("NA")) {
            tv_bi_sampoorn_cancer_suraksha_basic_service_tax_enhanced
                    .setText(""
                            + obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(GST_Enhanced.equals("") ? "0"
                                    : GST_Enhanced))));
        } else {

            GST_Enhanced = "NA";
            tv_bi_sampoorn_cancer_suraksha_basic_service_tax_enhanced
                    .setText(GST_Enhanced);
        }

        /*TextView tv_Company_policy_surrender_dec = (TextView) d
                .findViewById(R.id.tv_Company_policy_surrender_dec);*/


        String str_premPayingMode = "";
        if (premPayingMode.equalsIgnoreCase("Single")) {
            str_premPayingMode = "Single";
        } else {
            str_premPayingMode = "Regular";
        }

        Company_policy_surrender_dec = "Your SBI Life - Sampoorn Cancer Surakhsa (UIN: 111N109V03) is a "
                + str_premPayingMode
                + " premium policy, and you are required to pay a Premium for "

                + policyTerm
                + " years"

                + " and Basic Sum Assured is Rs. "
                +

                getformatedThousandString(Integer.parseInt(obj.getRound(obj
                        .getStringWithout_E(Double.valueOf((sumAssured
                                .equals("") || sumAssured == null) ? "0"
                                : sumAssured)))))
                + ". The premium rates for this product are fixed for a block of 5 policy years and may be revised after each block of 5 years thereafter.The revised premium rates would be based on age at entry.";

        tv_Company_policy_surrender_dec.setText(Company_policy_surrender_dec);


        d.show();

    }

    private void windowmessagesgin() {

        d = new Dialog(BI_SampoornCancerSurakshaActivity.this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));

        d.setContentView(R.layout.window_message_signature);
        final Button btn_save = d.findViewById(R.id.save);
        final Button btn_cancel = d.findViewById(R.id.cancel);

        Button btn_takeSign = d.findViewById(R.id.takesignature);

        btn_takeSign.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_save.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(
                        BI_SampoornCancerSurakshaActivity.this,
                        CaptureSignature.class);
                startActivityForResult(intent, SIGNATURE_ACTIVITY);

            }
        });

        btn_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                d.dismiss();
            }
        });
        d.show();

    }


    private void createPdf() {
        currencyFormat = new DecimalFormat("##,##,##,###");
        decimalcurrencyFormat = new DecimalFormat("##,##,##,###.##");

        try {

            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 11,
                    Font.BOLD, BaseColor.WHITE);
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);
            Font small_bold_for_name = new Font(Font.FontFamily.TIMES_ROMAN,
                    10, Font.BOLD);
            Font small_bold1 = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.BOLD);

            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.NORMAL);

            // mypath = new File(folder, QuatationNumber + "P01.pdf");

            // needAnalysispath = new File(folder, "NA.pdf");
            // newFile = new File(folder, QuatationNumber + "P01.pdf");

            mypath = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "BI.pdf");
            needAnalysispath = mStorageUtils.createFileToAppSpecificDir(context, "NA.pdf");
            // needAnalysis_Tabular_path = new File(folder, "NA_Tabular.pdf");
            newFile = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "P01.pdf");
            PdfPCell cell;

            Rectangle rect = new Rectangle(594f, 792f);

            Document document = new Document(rect, 50, 50, 50, 50);
            // Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            @SuppressWarnings("unused")
            PdfWriter pdf_writer = null;
            pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(
                    mypath.getAbsolutePath()));

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
            Paragraph Para_Header = new Paragraph();
            Para_Header
                    .add(new Paragraph(
                            "SBI LIFE - Sampoorn Cancer Suraksha (UIN: 111N109V03)",
                            headerBold));

            PdfPTable headertable = new PdfPTable(1);
            headertable.setWidthPercentage(100);
            PdfPCell c1 = new PdfPCell(new Phrase(Para_Header));
            c1.setBackgroundColor(BaseColor.DARK_GRAY);
            c1.setPadding(5);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            headertable.addCell(c1);
            headertable.setHorizontalAlignment(Element.ALIGN_CENTER);
            Paragraph para_address = new Paragraph(
                    "SBI Life Insurance Company Ltd" + "\n" + "Registered & Corporate Office: 'Natraj', M.V. Road and Western Express, Highway Junction, Andheri (East),Mumbai - 400069.IRDAI Registration No. 111",

                    small_bold_for_name);
            para_address.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address1 = new Paragraph(
                    "Website: www.sbilife.co.in | Email: info@sbilife.co.in | CIN: L99999MH2000PLC129113. Toll Free: 1800 267 9090 (Between 9.00 am & 9.00 pm)",
                    small_bold);
            para_address1.setAlignment(Element.ALIGN_CENTER);
            document.add(para_address);
            document.add(para_address1);
            document.add(para_img_logo_after_space_1);
            document.add(headertable);
            document.add(para_img_logo_after_space_1);

            document.add(para_img_logo_after_space_1);
            if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("Y")) {
                name_of_proposer = name_of_life_assured;
            }

            PdfPTable table_proposer_name = new PdfPTable(4);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_proposer_name.setWidthPercentage(100);

            PdfPCell ProposalNumber_cell_1 = new PdfPCell(new Paragraph(
                    "Quotation Number", small_normal));
            PdfPCell ProposalNumber_cell_2 = new PdfPCell(new Paragraph(
                    QuatationNumber, small_bold1));
            ProposalNumber_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell NameofProposal_cell_3 = new PdfPCell(new Paragraph(
                    "Proposer Name ", small_normal));
            PdfPCell NameofProposal_cell_4 = new PdfPCell(new Paragraph(
                    name_of_proposer, small_bold1));
            NameofProposal_cell_4.setHorizontalAlignment(Element.ALIGN_CENTER);

            NameofProposal_cell_4.setVerticalAlignment(Element.ALIGN_CENTER);

            ProposalNumber_cell_1.setPadding(5);
            ProposalNumber_cell_2.setPadding(5);
            NameofProposal_cell_3.setPadding(5);
            NameofProposal_cell_4.setPadding(5);

            table_proposer_name.addCell(ProposalNumber_cell_1);
            table_proposer_name.addCell(ProposalNumber_cell_2);
            table_proposer_name.addCell(NameofProposal_cell_3);
            table_proposer_name.addCell(NameofProposal_cell_4);
            document.add(table_proposer_name);
            document.add(para_img_logo_after_space_1);


            // inputTable here -1

            PdfPTable personalDetail_table = new PdfPTable(4);
            personalDetail_table.setWidths(new float[]{5f, 5f, 5f, 5f});
            personalDetail_table.setWidthPercentage(100f);

            personalDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st row
            cell = new PdfPCell(new Phrase(
                    "SBI Life - Sampoorn Cancer Suraksha (UIN: 111N109V03) ", small_bold));
            cell.setColspan(4);
            cell.setPadding(5);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Policy Term(yrs)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(policyTerm + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Age(last birthday) ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(ageAtEntry + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Gender", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(gender + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Staff/Non-Staff ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            if (staffdiscount.equalsIgnoreCase("true")) {
                cell = new PdfPCell(new Phrase("Staff", small_normal));
            } else {
                cell = new PdfPCell(new Phrase("Non-Staff", small_normal));
            }
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Premium Payment Frequency",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(premPayingMode + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Plan", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(planProposedName + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Base Sum Assured (in Rs.)",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            cell.setColspan(2);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(
                            ""
                                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((sumAssured.equals("") || sumAssured == null) ? "0"
                                            : sumAssured))))) + "",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            cell.setColspan(2);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("State", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            //personalDetail_table.addCell(cell);
            cell = new PdfPCell(new Phrase("", small_normal));
            if (cb_kerladisc.isChecked()) {
                cell = new PdfPCell(new Phrase("Kerala", small_normal));
            } else {
                cell = new PdfPCell(new Phrase("Non Kerala", small_normal));
            }
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            //personalDetail_table.addCell(cell);

            document.add(para_img_logo_after_space_1);
            document.add(para_img_logo_after_space_1);
            // Basic Cover
            document.add(personalDetail_table);

            document.add(para_img_logo_after_space_1);
            PdfPTable premiumDetail_table = new PdfPTable(4);
            premiumDetail_table.setWidths(new float[]{5f, 4f, 5f, 4f});
            premiumDetail_table.setWidthPercentage(100f);
            premiumDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            document.add(premiumDetail_table);

            document.add(para_img_logo_after_space_1);

            PdfPTable basicCover_table = new PdfPTable(4);
            basicCover_table.setWidths(new float[]{6f, 4f, 4f, 4f});
            basicCover_table.setWidthPercentage(100f);
            basicCover_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st row
			/*cell = new PdfPCell(new Phrase(premPayingMode + " Premium",
					small_bold));*/
            cell = new PdfPCell(new Phrase("Benefit Option/Premium Details",
                    small_bold));
            cell.setColspan(1);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setBorder(Rectangle.BOTTOM | Rectangle.TOP | Rectangle.LEFT
                    | Rectangle.RIGHT);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Standard", small_bold));
            cell.setColspan(1);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setBorder(Rectangle.BOTTOM | Rectangle.TOP | Rectangle.LEFT
                    | Rectangle.RIGHT);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Classic", small_bold));
            cell.setColspan(1);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setBorder(Rectangle.BOTTOM | Rectangle.TOP | Rectangle.LEFT
                    | Rectangle.RIGHT);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Enhanced", small_bold));
            cell.setColspan(1);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setBorder(Rectangle.BOTTOM | Rectangle.TOP | Rectangle.LEFT
                    | Rectangle.RIGHT);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Basic Premium (Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            basicCover_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase(currencyFormat.format(Double.parseDouble(PremiumBeforeST_Standard)),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double.parseDouble(PremiumBeforeST_Classic)),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase(currencyFormat.format(Double.parseDouble(PremiumBeforeST_Enhanced)),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Applicable Taxes (Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double.parseDouble(GST_Standard)), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double.parseDouble(GST_Classic)), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase(currencyFormat.format(Double.parseDouble(GST_Enhanced)), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Premium Including Applicable Taxes (Rs.)",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double.parseDouble(PremiumWithServTax_Standard)),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double.parseDouble(PremiumWithServTax_Classic)),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double.parseDouble(PremiumWithServTax_Enhanced)),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            document.add(basicCover_table);

            document.add(para_img_logo_after_space_1);

            // document.add(new_line);
            PdfPTable BI_Pdftable_note = new PdfPTable(1);
            BI_Pdftable_note.setWidthPercentage(100);
            PdfPCell BI_Pdftable_note_cell1 = new PdfPCell(new Paragraph(
                    "Benefit option Description", small_bold));
            BI_Pdftable_note_cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_note_cell1.setPadding(5);
            BI_Pdftable_note.addCell(BI_Pdftable_note_cell1);
            document.add(BI_Pdftable_note);

            PdfPTable BI_Pdftable6 = new PdfPTable(1);
            BI_Pdftable6.setWidthPercentage(100);
            PdfPCell BI_Pdftable6_cell6 = new PdfPCell(
                    new Paragraph(
                            "Standard - Covers Minor & major cancer Premium Waiver Benefit for 5 years ",
                            small_normal));
            BI_Pdftable6_cell6.setPadding(5);
            BI_Pdftable6.addCell(BI_Pdftable6_cell6);
            document.add(BI_Pdftable6);

            PdfPTable BI_Pdftable8 = new PdfPTable(1);
            BI_Pdftable8.setWidthPercentage(100);
            PdfPCell BI_Pdftable8_cell1 = new PdfPCell(
                    new Paragraph(
                            "Classic - Covers Minor, major & advanced cancer Premium Waiver Benefit for entire outstanding term",
                            small_normal));
            BI_Pdftable8_cell1.setPadding(5);
            BI_Pdftable8.addCell(BI_Pdftable8_cell1);
            document.add(BI_Pdftable8);

            PdfPTable BI_Pdftable9 = new PdfPTable(1);
            BI_Pdftable9.setWidthPercentage(100);
            PdfPCell BI_Pdftable9_cell1 = new PdfPCell(
                    new Paragraph(
                            "Enhanced - Covers Minor, major & advanced cancer Premium Waiver Benefit for entire outstanding term Sum Assured Reset Benefit ",
                            small_normal));
            BI_Pdftable9_cell1.setPadding(5);
            BI_Pdftable9.addCell(BI_Pdftable9_cell1);
            document.add(BI_Pdftable9);

            //document.add(para_img_logo_after_space_1);
            //document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_OtherTermCondition = new PdfPTable(1);
            BI_Pdftable_OtherTermCondition.setWidthPercentage(100);
            PdfPCell BI_Pdftable_OtherTermCondition_cell1 = new PdfPCell(
                    new Paragraph("Notes", small_bold));
            BI_Pdftable_OtherTermCondition_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_OtherTermCondition_cell1.setPadding(5);

            BI_Pdftable_OtherTermCondition
                    .addCell(BI_Pdftable_OtherTermCondition_cell1);
            //	document.add(BI_Pdftable_OtherTermCondition);

            PdfPTable BI_PdftableOtherTermCondition1 = new PdfPTable(1);
            BI_PdftableOtherTermCondition1.setWidthPercentage(100);
            PdfPCell BI_PdftableOtherTermCondition1_cell6 = new PdfPCell(
                    new Paragraph(
                            "*Applicable Taxes would be applicable at the rate notified by the Central Government/ State Government / Union Territories of India from time to time, as per the provisions of the prevalent tax laws. ",
                            small_normal));

            BI_PdftableOtherTermCondition1_cell6.setPadding(5);

            BI_PdftableOtherTermCondition1
                    .addCell(BI_PdftableOtherTermCondition1_cell6);
            document.add(BI_PdftableOtherTermCondition1);

            document.add(para_img_logo_after_space_1);

            document.add(para_img_logo_after_space_1);
            PdfPTable BI_Pdftable19 = new PdfPTable(1);
            BI_Pdftable19.setWidthPercentage(100);
            PdfPCell BI_Pdftable19_cell1 = new PdfPCell(new Paragraph(
                    "Disclaimer ", small_bold));
            BI_Pdftable19_cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable19_cell1.setPadding(5);

            BI_Pdftable19.addCell(BI_Pdftable19_cell1);
            document.add(BI_Pdftable19);

            PdfPTable BI_Pdftable20 = new PdfPTable(1);
            BI_Pdftable20.setWidthPercentage(100);
            PdfPCell BI_Pdftable20_cell1 = new PdfPCell(
                    new Paragraph(
                            "The Premium calculation may change if there are changes in the applicable tax rate.",
                            small_normal));

            BI_Pdftable20_cell1.setPadding(5);

            BI_Pdftable20.addCell(BI_Pdftable20_cell1);
            document.add(BI_Pdftable20);

            PdfPTable BI_Pdftable21 = new PdfPTable(1);
            BI_Pdftable21.setWidthPercentage(100);
            PdfPCell BI_Pdftable21_cell1 = new PdfPCell(
                    new Paragraph(
                            "Tax benefit under Section 80D is available.",

                            small_normal));

            BI_Pdftable21_cell1.setPadding(5);

            BI_Pdftable21.addCell(BI_Pdftable21_cell1);
            document.add(BI_Pdftable21);

            PdfPTable BI_Pdftable22 = new PdfPTable(1);
            BI_Pdftable22.setWidthPercentage(100);
            PdfPCell BI_Pdftable22_cell1 = new PdfPCell(
                    new Paragraph(
                            "Tax benefits, are as per the provisions of the Income Tax laws and are subject to change from time to time. Please consult your tax advisor for details.",
                            small_normal));

            BI_Pdftable22_cell1.setPadding(5);

            BI_Pdftable22.addCell(BI_Pdftable22_cell1);
            document.add(BI_Pdftable22);

            PdfPTable BI_Pdftable23 = new PdfPTable(1);
            BI_Pdftable23.setWidthPercentage(100);
            PdfPCell BI_Pdftable23_cell1 = new PdfPCell(
                    new Paragraph(
                            "For more details on risk factors, terms and conditions please read sales brochure carefully before concluding a sale.",

                            small_normal));

            BI_Pdftable23_cell1.setPadding(5);

            BI_Pdftable23.addCell(BI_Pdftable23_cell1);
            document.add(BI_Pdftable23);
            document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_CompanysPolicySurrender2 = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender2.setWidthPercentage(100);
            PdfPCell BI_Pdftable_CompanysPolicySurrender2_cell = new PdfPCell(
                    new Paragraph(
                            "You may receive a Welcome Call from our representative to confirm your proposal details like Date of Birth, Nominee Name, Address, Email ID, Sum Assured, Premium amount, Premium Payment Term etc.",
                            small_normal));

            BI_Pdftable_CompanysPolicySurrender2_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender2
                    .addCell(BI_Pdftable_CompanysPolicySurrender2_cell);
            document.add(BI_Pdftable_CompanysPolicySurrender2);

            PdfPTable BI_Pdftable_CompanysPolicySurrender5 = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender5.setWidthPercentage(100);
            PdfPCell BI_Pdftable_CompanysPolicySurrender5_cell = new PdfPCell(
                    new Paragraph(Company_policy_surrender_dec, small_normal));

            BI_Pdftable_CompanysPolicySurrender5_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender5
                    .addCell(BI_Pdftable_CompanysPolicySurrender5_cell);
            document.add(BI_Pdftable_CompanysPolicySurrender5);

            document.add(para_img_logo_after_space_1);
            Calendar present_date = Calendar.getInstance();
            int mDay = present_date.get(Calendar.DAY_OF_MONTH);
            int mMonth = present_date.get(Calendar.MONTH);
            int mYear = present_date.get(Calendar.YEAR);

            String CurrentDate = mDay + "-" + (mMonth + 1) + "-" + mYear;
            PdfPTable BI_Pdftable26 = new PdfPTable(1);
            BI_Pdftable26.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_26);
            PdfPCell BI_Pdftable26_cell1 = new PdfPCell(
                    new Paragraph(
                            "I, "
                                    + name_of_proposer
                                    + "     having received the information with respect to the above, have understood the above statement before entering into the contract.",
                            small_bold));

            BI_Pdftable26_cell1.setPadding(5);

            BI_Pdftable26.addCell(BI_Pdftable26_cell1);
            document.add(BI_Pdftable26);
            document.add(BI_Pdftable26_cell1);
            document.add(para_img_logo_after_space_1);

            if (!bankUserType.equalsIgnoreCase("Y")) {
                PdfPTable BI_PdftablePolicyHolder = new PdfPTable(1);
                BI_PdftablePolicyHolder.setWidthPercentage(100);
                PdfPCell BI_PdftablePolicyHolder_signature_cell = new PdfPCell(
                        new Paragraph("PolicyHolder's Signature", small_bold));
                BI_PdftablePolicyHolder_signature_cell
                        .setBackgroundColor(BaseColor.LIGHT_GRAY);
                BI_PdftablePolicyHolder_signature_cell
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftablePolicyHolder_signature_cell.setPadding(5);
                BI_PdftablePolicyHolder
                        .addCell(BI_PdftablePolicyHolder_signature_cell);
                document.add(BI_PdftablePolicyHolder);
            }

            PdfPTable BI_PdftablePolicyHolder_signature;

            if (!bankUserType.equalsIgnoreCase("Y")) {
                BI_PdftablePolicyHolder_signature = new PdfPTable(3);
            } else {
                BI_PdftablePolicyHolder_signature = new PdfPTable(2);
            }

            BI_PdftablePolicyHolder_signature.setWidthPercentage(100);
            PdfPCell BI_PdftablePolicyHolder_signature_1 = new PdfPCell(
                    new Paragraph("Place :" + place2, small_normal));
            PdfPCell BI_PdftablePolicyHolder_signature_2 = new PdfPCell(
                    new Paragraph("Date  :" + CurrentDate, small_normal));

            PdfPCell BI_PdftablePolicyHolder_signature_3 = new PdfPCell();
            if (!bankUserType.equalsIgnoreCase("Y")) {
                byte[] fbyt_Proposer = Base64.decode(proposer_sign, 0);
                Bitmap Proposerbitmap = BitmapFactory.decodeByteArray(
                        fbyt_Proposer, 0, fbyt_Proposer.length);

                BI_PdftablePolicyHolder_signature_3.setFixedHeight(60f);
                ByteArrayOutputStream PolicyHolder_signature_stream = new ByteArrayOutputStream();
                (Proposerbitmap).compress(Bitmap.CompressFormat.PNG, 50,
                        PolicyHolder_signature_stream);
                Image PolicyHolder_signature = Image
                        .getInstance(PolicyHolder_signature_stream
                                .toByteArray());
                BI_PdftablePolicyHolder_signature_3
                        .setImage(PolicyHolder_signature);
            }
            BI_PdftablePolicyHolder_signature_1.setPadding(5);
            BI_PdftablePolicyHolder_signature_2.setPadding(5);
            BI_PdftablePolicyHolder_signature
                    .addCell(BI_PdftablePolicyHolder_signature_1);
            BI_PdftablePolicyHolder_signature
                    .addCell(BI_PdftablePolicyHolder_signature_2);

            if (!bankUserType.equalsIgnoreCase("Y")) {
                BI_PdftablePolicyHolder_signature
                        .addCell(BI_PdftablePolicyHolder_signature_3);
            }
            document.add(BI_PdftablePolicyHolder_signature);
            document.add(para_img_logo_after_space_1);

			/*PdfPTable BI_Pdftable_eSign = new PdfPTable(1);
			BI_Pdftable_eSign.setWidthPercentage(100);
			// CR_table6.setWidths(columnWidths_26);
			PdfPCell BI_Pdftable_eSign_cell1 = new PdfPCell(new Paragraph(

					"This document is eSigned by " + name_of_person, small_bold));

			BI_Pdftable_eSign_cell1.setPadding(5);

			BI_Pdftable_eSign.addCell(BI_Pdftable_eSign_cell1);
			document.add(BI_Pdftable_eSign);
			document.add(para_img_logo_after_space_1);*/

            if (!bankUserType.equalsIgnoreCase("Y")) {
                PdfPTable BI_PdftableMarketing = new PdfPTable(1);
                BI_PdftableMarketing.setWidthPercentage(100);
                PdfPCell BI_PdftableMarketing_signature_cell = new PdfPCell(
                        new Paragraph("Marketing official's Signature & Company Seal", small_bold));
                BI_PdftableMarketing_signature_cell
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableMarketing_signature_cell
                        .setBackgroundColor(BaseColor.LIGHT_GRAY);
                BI_PdftableMarketing_signature_cell.setPadding(5);

                BI_PdftableMarketing
                        .addCell(BI_PdftableMarketing_signature_cell);
                document.add(BI_PdftableMarketing);

                // M_ChannelDetails list_channel_detail =
                // db.getChannelDetail(sr_Code);

                byte[] fbyt_agent = Base64.decode(agent_sign, 0);
                Bitmap Agentbitmap = BitmapFactory.decodeByteArray(fbyt_agent,
                        0, fbyt_agent.length);

				/*String code = commonMethods.setUserDetails(context)
						.getStrCIFBDMUserId();
				String name = commonMethods.getUserName((Activity) context);
				String str_sign_header = "";
				switch (str_usertype) {
					case "AGENT":
						str_sign_header = "(IA code- " + code + ")" + "\n"
								+ "Name of IA- " + name + "\n"
								+ "Authenticated by Id & Password";
						break;
					case "CIF":
						str_sign_header = "(CIF code- " + code + ")" + "\n"
								+ "Name of CIF- " + name + "\n"
								+ "Authenticated by Id & Password";

						break;
					case "BAP":
						str_sign_header = "(BAP code- " + code + ")" + "\n"
								+ "Name of Broker- " + name + "\n"
								+ "Authenticated by Id & Password";

						break;
					case "CAG":
						str_sign_header = "(CAG code- " + code + ")" + "\n"
								+ "Name of Corporate Agent- " + name + "\n"
								+ "Authenticated by Id & Password";

						break;
					case "IMF":
				str_sign_header = "(ISP code- " + code + ")" + "\n"
						+ "ISP Name- " + name + "\n"
								+ "Authenticated by Id & Password";

						break;
				}*/
                PdfPTable BI_PdftableMarketing_signature = new PdfPTable(3);

                BI_PdftableMarketing_signature.setWidthPercentage(100);

                PdfPCell BI_PdftableMarketing_signature_1 = new PdfPCell(
                        new Paragraph("Place :" + place2, small_normal));
                PdfPCell BI_PdftableMarketing_signature_2 = new PdfPCell(
                        new Paragraph("Date  :" + CurrentDate, small_normal));
                PdfPCell BI_PdftableMarketing_signature_3 = new PdfPCell();

                BI_PdftableMarketing_signature_3.setFixedHeight(60f);
                ByteArrayOutputStream Marketing_officials_signature_stream = new ByteArrayOutputStream();

                (Agentbitmap).compress(Bitmap.CompressFormat.PNG, 50,
                        Marketing_officials_signature_stream);
                Image Marketing_officials_signature = Image
                        .getInstance(Marketing_officials_signature_stream
                                .toByteArray());

                BI_PdftableMarketing_signature_3
                        .setImage(Marketing_officials_signature);

                BI_PdftableMarketing_signature_1.setPadding(5);
                BI_PdftableMarketing_signature_2.setPadding(5);

                BI_PdftableMarketing_signature
                        .addCell(BI_PdftableMarketing_signature_1);
                BI_PdftableMarketing_signature
                        .addCell(BI_PdftableMarketing_signature_2);
                BI_PdftableMarketing_signature
                        .addCell(BI_PdftableMarketing_signature_3);
                document.add(BI_PdftableMarketing_signature);
            }
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String valminprem(String val) {
        String error = "";

        if (Double.parseDouble(val) < 600
                && spnrPremFrequency.getSelectedItem().toString()
                .equals("Yearly")) {
            error = "Minimum Premium for Yearly Frequency mode must be greater than 600";

        } else if (Double.parseDouble(val) < 300
                && spnrPremFrequency.getSelectedItem().toString()
                .equals("Half-Yearly")) {
            error = "Minimum Premium for Half - Yearly Frequency mode must be greater than 300";

        } else if (Double.parseDouble(val) < 150
                && spnrPremFrequency.getSelectedItem().toString()
                .equals("Quarterly")) {
            error = "Minimum Premium for Quarterly Frequency mode must be greater than 150";
        } else if (Double.parseDouble(val) < 50
                && spnrPremFrequency.getSelectedItem().toString()
                .equals("Monthly")) {
            error = "Minimum Premium for Monthly Frequency mode must be greater than 50";
        }

        return error;
    }

    private boolean val_proposer_gender() {

        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
            if (spnr_bi_sampoorn_cancer_suraksha_proposer_Gender
                    .getSelectedItem().toString().equals("Select Gender")) {
                commonMethods.dialogWarning(context, "Please Select Proposer Gender", true);
                spnr_bi_sampoorn_cancer_suraksha_proposer_Gender.requestFocus();
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

}
