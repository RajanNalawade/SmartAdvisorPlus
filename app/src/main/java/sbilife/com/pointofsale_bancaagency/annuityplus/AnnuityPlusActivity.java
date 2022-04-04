package sbilife.com.pointofsale_bancaagency.annuityplus;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DecimalFormat;
import java.util.Calendar;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.ProductDesc_webview;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;

@SuppressLint("ClickableViewAccessibility")
public class AnnuityPlusActivity extends AppCompatActivity {
    private TextView inputActivityHeader;

    // UI Elements
    private CheckBox selJKResident;
    private CheckBox selADBRider;
    private CheckBox selAdvAnnPayout;
    private Spinner selSourceOfBusiness;
    private Spinner selChannelDetails;
    private Spinner selModeofAnnuityPayouts;
    private Spinner selAnnuityOption;
    private Spinner selApplicableFor;
    private Spinner selFirstAnnutantAge;
    private Spinner selFirstAnnutantGender;
    private Spinner selSecondAnnutantAge;
    private Spinner selSecondAnnutantGender;
    private Spinner selOptFor;
    private TextView proposalDate;
    private TextView AdvAnnPayoutDate;
    private TextView txtApplicableFor;
    private TextView txtSecondAnnutantAge;
    private TextView txtSecondAnnutantGender;
    private TextView txtAdditionalAmt;
    private TextView txtVestingAmt;
    private TextView txtannuityAmt;
    private TextView txtAdvAnnPayoutDate;
    private EditText edAnnuityAmt;
    private EditText edVestingAmt;
    private EditText edAdditionalAmt;

    private AlertDialog.Builder showAlert;
    private ProgressDialog progressDialog;

    // Class declaration
    private AnnuityPlusProperties Prop;
    private Context mContext;

    // server variables
    private final String NAMESPACE_CALC = "http://tempuri.org/";
    private final String URL = ServiceURL.SERVICE_URL;
    private final String METHOD_NAME_CALC = "getPremiumAnnuityPlusNew";


    private String strCIFCode = "";
    private String strCIFBDMEmailId = "";
    private String strCIFBDMPassword = "";
    private String strCIFBDMMObileNo = "";
    private CommonMethods mCommonMethods;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.annuityplusmain);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        mCommonMethods = new CommonMethods();

        mContext = this;

        // Class Declaration
        Prop = new AnnuityPlusProperties();

        // Variables
        DecimalFormat currencyFormat = new DecimalFormat("##,##,##,###");
        showAlert = new AlertDialog.Builder(this);

        mCommonMethods.setApplicationToolbarMenu(this, getString(R.string.app_name));

        inputActivityHeader = findViewById(R.id.txt_input_activityheader);

        inputActivityHeader.setText(Html
                .fromHtml("<u>SBI Life - Annuity Plus</u>"));

        OnTouchListener txttouch = new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    Intent intent = new Intent(mContext,
                            ProductDesc_webview.class);
                    intent.putExtra("name", inputActivityHeader.getText()
                            .toString());
                    startActivity(intent);
                }
                return false;
            }
        };

        inputActivityHeader.setOnTouchListener(txttouch);
        // UI elements
        selJKResident = findViewById(R.id.JKResident);

        Spinner selStaffNonStaff = findViewById(R.id.selStaffNonStaff);
        String[] staffNonstaffList = {"Staff", "Non Staff"};
        ArrayAdapter<String> staffNonstaffAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                staffNonstaffList);
        // staffNonstaffAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        selStaffNonStaff.setAdapter(staffNonstaffAdapter);
        staffNonstaffAdapter.notifyDataSetChanged();

        selSourceOfBusiness = findViewById(R.id.selSourceOfBusiness);
        String[] sourceOfBussList = {"Vesting/Death/Surrender of existing SBI Life's pension policy",
                "Open Market Option (Any Other Life Insurance Company Pension Policy)", "New Proposal"};
        ArrayAdapter<String> sourceOfBussAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                sourceOfBussList);
        sourceOfBussAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        selSourceOfBusiness.setAdapter(sourceOfBussAdapter);
        sourceOfBussAdapter.notifyDataSetChanged();

        proposalDate = findViewById(R.id.proposalDate);

        selChannelDetails = findViewById(R.id.selChannelDetails);
        String[] channelDetailsList = {"Retail Agency", "Bancassurance",
                "Broking", "Corporate Agency", "Corporate Solutions", "Direct",
                "Others"};
        ArrayAdapter<String> channelDetailsAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                channelDetailsList);
        channelDetailsAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        selChannelDetails.setAdapter(channelDetailsAdapter);
        channelDetailsAdapter.notifyDataSetChanged();

        selModeofAnnuityPayouts = findViewById(R.id.selModeofAnnuityPayouts);
        String[] modeOfPayoutList = {"Monthly", "Quarterly", "Half Yearly",
                "Yearly"};
        ArrayAdapter<String> modeOfPayoutAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                modeOfPayoutList);
        modeOfPayoutAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        selModeofAnnuityPayouts.setAdapter(modeOfPayoutAdapter);
        modeOfPayoutAdapter.notifyDataSetChanged();

        selAnnuityOption = findViewById(R.id.selAnnuityOption);
        String[] annuityOptionList = {"Lifetime Income",
                "Lifetime Income with Capital Refund",
                "Lifetime Income with Capital Refund in Parts",
                "Lifetime Income with Balance Capital Refund",
                "Lifetime Income with Annual Increase of 3%",
                "Lifetime Income with Annual Increase of 5%",
                "Lifetime Income with Certain Period of 5 Years",
                "Lifetime Income with Certain Period of 10 Years",
                "Lifetime Income with Certain Period of 15 Years",
                "Lifetime Income with Certain Period of 20 Years",
                "Life and Last Survivor - 50% Income",
                "Life and Last Survivor - 100% Income",
                "Life and Last Survivor with Capital Refund - 50% Income",
                "Life and Last Survivor with Capital Refund - 100% Income"};
        ArrayAdapter<String> annuityOptionAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                annuityOptionList);
        annuityOptionAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        selAnnuityOption.setAdapter(annuityOptionAdapter);
        annuityOptionAdapter.notifyDataSetChanged();

        selADBRider = findViewById(R.id.ADB);

        txtApplicableFor = findViewById(R.id.txtApplicableFor);

        selApplicableFor = findViewById(R.id.selApplicableFor);
        String[] applicableForList = {"First Annuitant"};
        ArrayAdapter<String> applicableForAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                applicableForList);
        applicableForAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        selApplicableFor.setAdapter(applicableForAdapter);
        applicableForAdapter.notifyDataSetChanged();

        selAdvAnnPayout = findViewById(R.id.AdvAnnPayout);
        txtAdvAnnPayoutDate = findViewById(R.id.txtAdvAnnPayoutDate);
        AdvAnnPayoutDate = findViewById(R.id.AdvAnnPayoutDate);

        selFirstAnnutantAge = findViewById(R.id.selFirtAnnutantAge);
        String[] firstAnnAgeList = new String[81];
        for (int i = 0; i <= 80; i++) {
            firstAnnAgeList[i] = i + "";
        }
        ArrayAdapter<String> firstAnnAgeAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, firstAnnAgeList);
        firstAnnAgeAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        selFirstAnnutantAge.setAdapter(firstAnnAgeAdapter);
        firstAnnAgeAdapter.notifyDataSetChanged();

        selFirstAnnutantGender = findViewById(R.id.selFirstAnnutantGender);
        String[] firstAnnGenderList = {"Male", "Female"};
        ArrayAdapter<String> firstAnnGenderAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                firstAnnGenderList);
        firstAnnGenderAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        selFirstAnnutantGender.setAdapter(firstAnnGenderAdapter);
        firstAnnGenderAdapter.notifyDataSetChanged();

        txtSecondAnnutantAge = findViewById(R.id.txtSecondAnnutantAge);

        selSecondAnnutantAge = findViewById(R.id.selSecondAnnutantAge);
        String[] secondAnnAgeList = new String[81];
        for (int i = 0; i <= 80; i++) {
            secondAnnAgeList[i] = i + "";
        }
        ArrayAdapter<String> secondAnnAgeAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                secondAnnAgeList);
        secondAnnAgeAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        selSecondAnnutantAge.setAdapter(secondAnnAgeAdapter);
        secondAnnAgeAdapter.notifyDataSetChanged();

        txtSecondAnnutantGender = findViewById(R.id.txtSecondAnnutantGender);

        selSecondAnnutantGender = findViewById(R.id.selSecondAnnutantGender);
        String[] secondAnnGenderList = {"Male", "Female"};
        ArrayAdapter<String> secondAnnGenderAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                secondAnnGenderList);
        secondAnnGenderAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        selSecondAnnutantGender.setAdapter(secondAnnGenderAdapter);
        secondAnnGenderAdapter.notifyDataSetChanged();

        selOptFor = findViewById(R.id.selOptFor);
        String[] optForList = {"Annuity Payout Amount", "Premium Amount"};
        ArrayAdapter<String> optForAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, optForList);
        optForAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        selOptFor.setAdapter(optForAdapter);
        optForAdapter.notifyDataSetChanged();

        txtannuityAmt = findViewById(R.id.txtannuityAmt);
        edAnnuityAmt = findViewById(R.id.edAnnuityAmt);
        txtVestingAmt = findViewById(R.id.txtVestingAmt);
        edVestingAmt = findViewById(R.id.edVestingAmt);
        txtAdditionalAmt = findViewById(R.id.txtAdditionalAmt);
        edAdditionalAmt = findViewById(R.id.edAdditionalAmt);

        // Set Proposer Date
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);
        String Today = (mMonth + 1) + "/" + mDay + "/" + mYear;
        proposalDate.setText(Today);
        AdvAnnPayoutDate.setText(Today);


        // ADB Rider
        selADBRider
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked && valRider()) {

                            txtApplicableFor.setVisibility(View.VISIBLE);
                            selApplicableFor.setVisibility(View.VISIBLE);
                        } else {
                            txtApplicableFor.setVisibility(View.GONE);
                            selApplicableFor.setVisibility(View.GONE);
                        }
                    }
                });

        // Modes of annuity payouts
        selModeofAnnuityPayouts
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        if (selModeofAnnuityPayouts.getSelectedItem()
                                .toString().equals("Yearly")
                                || selModeofAnnuityPayouts.getSelectedItem()
                                .toString().equals("Half Yearly")) {
                            selAdvAnnPayout.setVisibility(View.VISIBLE);
                        } else {
                            selAdvAnnPayout.setVisibility(View.GONE);
                            txtAdvAnnPayoutDate.setVisibility(View.GONE);
                            AdvAnnPayoutDate.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        // Advance annuity payout
        selAdvAnnPayout
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            txtAdvAnnPayoutDate.setVisibility(View.VISIBLE);
                            AdvAnnPayoutDate.setVisibility(View.VISIBLE);
                        } else {
                            txtAdvAnnPayoutDate.setVisibility(View.GONE);
                            AdvAnnPayoutDate.setVisibility(View.GONE);
                        }
                    }
                });

        // Annuity Option
        selAnnuityOption
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        // TODO Auto-generated method stub

                        updateField_FirstOrBothAnnuitant();
                        addOrRemoveSecAnnuitantFields();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        // Source of Business
        selSourceOfBusiness
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        // TODO Auto-generated method stub

                        deleteAndAddFieldsUnderOptForField();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        // Opt for
        selOptFor.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub

                deleteAndAddFieldsUnderOptForField();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        selApplicableFor.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub

                valRider();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        // First Annuitant Age
        selFirstAnnutantAge
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        // TODO Auto-generated method stub

                        valRider();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        // Second Annuitant Age
        selSecondAnnutantAge
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        // TODO Auto-generated method stub

                        valRider();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        // Date listerner to set date in Textview
        final DatePickerDialog.OnDateSetListener datepickerListener = new DatePickerDialog.OnDateSetListener() {

            // @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                AdvAnnPayoutDate.setText(new StringBuilder()
                        .append(monthOfYear + 1).append("/").append(dayOfMonth)
                        .append("/").append(year).append(" "));
                // System.out.println("Before valRider in dob");

                // System.out.println("After valRider in dob");
            }
        };

        // Advance annuity payout date
        AdvAnnPayoutDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String[] dateArray = AdvAnnPayoutDate.getText().toString()
                        .split("/");
                // System.out.println(dateArray.length);
                // System.out.println(Integer.parseInt(dateArray[0])+"");
                // System.out.println(Integer.parseInt(dateArray[1])+"");
                // System.out.println(Integer.parseInt(dateArray[2].trim())+"");
                new DatePickerDialog(AnnuityPlusActivity.this,
                        datepickerListener, Integer.parseInt(dateArray[2]
                        .trim()),
                        Integer.parseInt(dateArray[0].trim()) - 1, Integer
                        .parseInt(dateArray[1].trim())).show();

            }
        });

        // Date listerner to set date in Textview
        final DatePickerDialog.OnDateSetListener proposalDatepickerListener = new DatePickerDialog.OnDateSetListener() {

            // @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                proposalDate.setText(new StringBuilder()
                        .append(monthOfYear + 1).append("/").append(dayOfMonth)
                        .append("/").append(year).append(" "));

            }
        };

        // Proposal date
        proposalDate.setOnClickListener(new View.OnClickListener() {

            // @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String[] dateArray = proposalDate.getText().toString()
                        .split("/");
                // System.out.println(dateArray.length);
                // System.out.println(Integer.parseInt(dateArray[0])+"");
                // System.out.println(Integer.parseInt(dateArray[1])+"");
                // System.out.println(Integer.parseInt(dateArray[2].trim())+"");
                new DatePickerDialog(AnnuityPlusActivity.this,
                        proposalDatepickerListener, Integer
                        .parseInt(dateArray[2].trim()), Integer
                        .parseInt(dateArray[0].trim()) - 1, Integer
                        .parseInt(dateArray[1].trim())).show();

            }
        });

        // Go Home Button
        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                setResult(RESULT_OK);
                finish();
            }
        });

        // Calculate premium
        Button btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View arg0) {

                if (valInputScreen() && valAge() && valAgeDifferenceBetweenAnnutants()) {

                    service_hits();

                }
            }
        });

        // Item Listener ends here

        try {
            strCIFCode = SimpleCrypto.decrypt("SBIL", dbHelper.GetCIFNo());
            strCIFBDMEmailId = SimpleCrypto.decrypt("SBIL",
                    dbHelper.GetEmailId());
            strCIFBDMPassword = dbHelper.GetPassword();
            strCIFBDMMObileNo = SimpleCrypto.decrypt("SBIL",
                    dbHelper.GetMobileNo());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    // Store user input in Bean object
    private void addListenerOnSubmit() {

        //Service starts here
        /*
         *
         * /******************** Service starts here
         * **********************************************
         */

        progressDialog = new ProgressDialog(mContext,
                ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Please Wait";
        progressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>"
                + Message + "<b></font>"));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle(Html
                .fromHtml("<font color='#00a1e3'><b>Calculating<b></font>"));
        progressDialog.setMax(100);
        progressDialog.show();
        AsyncAnnuityPlus service = new AsyncAnnuityPlus(mContext, progressDialog);
        service.execute();
        //Service ends here

    }

    // Service Hits
    private void service_hits() {

        //Service starts here
        progressDialog = new ProgressDialog(mContext,
                ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Please Wait";
        progressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>"
                + Message + "<b></font>"));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog.setTitle(Html
                .fromHtml("<font color='#00a1e3'><b>Calculating<b></font>"));
        progressDialog.setMax(100);
        progressDialog.show();
        String METHOD_NAME_SH = "saveSmartAdvisorServiceHits";
        String SOAP_ACTION_SH = "http://tempuri.org/saveSmartAdvisorServiceHits";
        ServiceHits service = new ServiceHits(mContext, progressDialog);
        service.execute();
        // Service ends here
    }


    // Used in Annuity Option Item Listener
    private void updateField_FirstOrBothAnnuitant() {
        if (selAnnuityOption.getSelectedItem().toString()
                .equals("Life and Last Survivor - 50% Income")
                || selAnnuityOption.getSelectedItem().toString()
                .equals("Life and Last Survivor - 100% Income")
                || selAnnuityOption
                .getSelectedItem()
                .toString()
                .equals("Life and Last Survivor with Capital Refund - 50% Income")
                || selAnnuityOption
                .getSelectedItem()
                .toString()
                .equals("Life and Last Survivor with Capital Refund - 100% Income")) {
            String[] applicableForList = {"First Annuitant", "Both Annuitant"};
            ArrayAdapter<String> applicableForAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item,
                    applicableForList);
            applicableForAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
            selApplicableFor.setAdapter(applicableForAdapter);
            applicableForAdapter.notifyDataSetChanged();

        } else {
            String[] applicableForList = {"First Annuitant"};
            ArrayAdapter<String> applicableForAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item,
                    applicableForList);
            applicableForAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
            selApplicableFor.setAdapter(applicableForAdapter);
            applicableForAdapter.notifyDataSetChanged();

        }
    }

    // Used in Annuity Option Item Listener
    private void addOrRemoveSecAnnuitantFields() {

        if (selAnnuityOption.getSelectedItem().toString()
                .equals("Life and Last Survivor - 50% Income")
                || selAnnuityOption.getSelectedItem().toString()
                .equals("Life and Last Survivor - 100% Income")
                || selAnnuityOption
                .getSelectedItem()
                .toString()
                .equals("Life and Last Survivor with Capital Refund - 50% Income")
                || selAnnuityOption
                .getSelectedItem()
                .toString()
                .equals("Life and Last Survivor with Capital Refund - 100% Income")) {
            txtSecondAnnutantAge.setVisibility(View.VISIBLE);
            txtSecondAnnutantGender.setVisibility(View.VISIBLE);
            selSecondAnnutantAge.setVisibility(View.VISIBLE);
            selSecondAnnutantGender.setVisibility(View.VISIBLE);
        } else {
            txtSecondAnnutantAge.setVisibility(View.GONE);
            txtSecondAnnutantGender.setVisibility(View.GONE);
            selSecondAnnutantAge.setVisibility(View.GONE);
            selSecondAnnutantGender.setVisibility(View.GONE);
        }

    }

    // Used in source of business and opt for item listener
    private void deleteAndAddFieldsUnderOptForField() {
        txtannuityAmt.setVisibility(View.GONE);
        edAnnuityAmt.setVisibility(View.GONE);
        txtVestingAmt.setVisibility(View.GONE);
        edVestingAmt.setVisibility(View.GONE);
        txtVestingAmt.setVisibility(View.GONE);
        edVestingAmt.setVisibility(View.GONE);
        txtAdditionalAmt.setVisibility(View.GONE);
        edAdditionalAmt.setVisibility(View.GONE);

        if (selOptFor.getSelectedItem().toString()
                .equals("Annuity Payout Amount")) {
            txtannuityAmt.setVisibility(View.VISIBLE);
            edAnnuityAmt.setVisibility(View.VISIBLE);

            if (selSourceOfBusiness.getSelectedItem().toString().equals("Vesting/Death/Surrender of existing SBI Life's pension policy")) {
                txtVestingAmt.setVisibility(View.VISIBLE);
                edVestingAmt.setVisibility(View.VISIBLE);
            }

        } else if (selOptFor.getSelectedItem().toString()
                .equals("Premium Amount")) {
            txtVestingAmt.setVisibility(View.VISIBLE);
            edVestingAmt.setVisibility(View.VISIBLE);

            if (selSourceOfBusiness.getSelectedItem().toString().equals("Vesting/Death/Surrender of existing SBI Life's pension policy")) {
                txtAdditionalAmt.setVisibility(View.VISIBLE);
                edAdditionalAmt.setVisibility(View.VISIBLE);
            }
        }

    }

    private boolean valInputScreen() {

        // validate Annuity Amount
        if (txtannuityAmt.getVisibility() == View.VISIBLE) {
            if (!valAnnuityAmt()) {
                return false;
            }
        }

        // validate Vesting Amount
        if (txtVestingAmt.getVisibility() == View.VISIBLE) {
            if (!valVestingAmt()) {
                return false;
            }
        }

        // validate additional amount
        if (txtAdditionalAmt.getVisibility() == View.VISIBLE) {
            return valAdditionalAmt();
        }

        return true;
    }

    // Validate Annuity Amount [called from valInputScreen()]
    private boolean valAnnuityAmt() {
        String error = "";
        if (edAnnuityAmt.getText().toString().equals("")) {
            error = "Please enter Annuity Amount in Rs.";
        } else if (selModeofAnnuityPayouts.getSelectedItem().toString().equals("Yearly") && Double.parseDouble(edAnnuityAmt.getText().toString()) < 12000) {
            error = "Annuity Amount should not be less than Rs.12000";
        } else if (selModeofAnnuityPayouts.getSelectedItem().toString().equals("Half Yearly") && Double.parseDouble(edAnnuityAmt.getText().toString()) < 6000) {
            error = "Annuity Amount should not be less than Rs.6000";
        } else if (selModeofAnnuityPayouts.getSelectedItem().toString().equals("Quarterly") && Double.parseDouble(edAnnuityAmt.getText().toString()) < 3000) {
            error = "Annuity Amount should not be less than Rs.3000";
        } else if (selModeofAnnuityPayouts.getSelectedItem().toString().equals("Monthly") && Double.parseDouble(edAnnuityAmt.getText().toString()) < 1000) {
            error = "Annuity Amount should not be less than Rs.1000";
        }

        if (!error.equals("")) {
            showAlert.setMessage(error);
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();
            return false;
        }

        return true;
    }

    // Validate Vesting Amount [called from valInputScreen()]
    private boolean valVestingAmt() {
        if (edVestingAmt.getText().toString().equals("")) {
            showAlert.setMessage("Please enter Vesting Amount in Rs.");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();
            return false;
        }

        return true;
    }

    // Validate Additional Amount [called from valInputScreen()]
    private boolean valAdditionalAmt() {
        if (edAdditionalAmt.getText().toString().equals("")) {
            showAlert.setMessage("Please enter Additional Amount in Rs.");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();
            return false;
        }

        return true;
    }

    // validate Eligibility for ADB Rider
    private boolean valRider() {
        StringBuilder error = new StringBuilder();

        if (selADBRider.isChecked()) {

            if (Integer.parseInt(selFirstAnnutantAge.getSelectedItem().toString()) < Prop.minAgeOfAnnuitant || Integer.parseInt(selFirstAnnutantAge.getSelectedItem().toString()) > Prop.maxAgeOfAnnuitantWhenRider) {
                error.append("Age limit of Accidental Death Benefit Rider for First Annuitant is between ").append(Prop.minAgeOfAnnuitant).append(" and ").append(Prop.maxAgeOfAnnuitantWhenRider).append("\n");
            }
            if ((selAnnuityOption.getSelectedItem().toString().equals("Life and Last Survivor - 50% Income") || selAnnuityOption.getSelectedItem().toString().equals("Life and Last Survivor - 100% Income") || selAnnuityOption.getSelectedItem().toString().equals("Life and Last Survivor with Capital Refund - 50% Income") || selAnnuityOption.getSelectedItem().toString().equals("Life and Last Survivor with Capital Refund - 100% Income")) && selApplicableFor.getSelectedItem().toString().equals("Both Annuitant") && (Integer.parseInt(selSecondAnnutantAge.getSelectedItem().toString()) < Prop.minAgeOfAnnuitant || Integer.parseInt(selSecondAnnutantAge.getSelectedItem().toString()) > Prop.maxAgeOfAnnuitantWhenRider)) {
                error.append("Maximum Age limit of Accidental Death Benefit Rider for Second Annuitant is ").append(Prop.maxAgeOfAnnuitantWhenRider).append("\n");
            }

            if (!error.toString().equals("")) {
                selADBRider.setChecked(false);
                if (txtApplicableFor.getVisibility() == View.VISIBLE) {
                    txtApplicableFor.setVisibility(View.GONE);
                    selApplicableFor.setVisibility(View.GONE);
                }

                showAlert.setMessage(error.toString());
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                            }
                        });
                showAlert.show();
                return false;
            }

        }
        return true;
    }


    /*** Modified by Akshaya on 14-SEPT-16  start****/
    private boolean valAge() {
        StringBuilder error = new StringBuilder();


        if (!selSourceOfBusiness.getSelectedItem().toString().equals("Vesting/Death/Surrender of existing SBI Life's pension policy")) {
            if (Integer.parseInt(selFirstAnnutantAge.getSelectedItem().toString()) < 40) {
                error.append("Minimum Age Limit for First Annutant is 40");
            }
        }

        if (!selSourceOfBusiness.getSelectedItem().toString().equals("Vesting/Death/Surrender of existing SBI Life's pension policy") && (selAnnuityOption.getSelectedItem().toString().equals("Life and Last Survivor - 50% Income") || selAnnuityOption.getSelectedItem().toString().equals("Life and Last Survivor - 100% Income") || selAnnuityOption.getSelectedItem().toString().equals("Life and Last Survivor with Capital Refund - 50% Income") || selAnnuityOption.getSelectedItem().toString().equals("Life and Last Survivor with Capital Refund - 100% Income"))) {
            if (Integer.parseInt(selFirstAnnutantAge.getSelectedItem().toString()) < 40) {
                error.append("Minimum Age Limit for Second Annutant is 40");
            }
        }


        if (!error.toString().equals("")) {
            showAlert.setMessage(error.toString());
            showAlert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            showAlert.show();
            return false;
        }


        return true;
    }


    private boolean valAgeDifferenceBetweenAnnutants() {
        int difference = Integer.parseInt(selFirstAnnutantAge.getSelectedItem().toString()) - Integer.parseInt(selSecondAnnutantAge.getSelectedItem().toString());


        if ((selAnnuityOption.getSelectedItem().toString().equals("Life and Last Survivor - 50% Income") || selAnnuityOption.getSelectedItem().toString().equals("Life and Last Survivor - 100% Income") || selAnnuityOption.getSelectedItem().toString().equals("Life and Last Survivor with Capital Refund - 50% Income") || selAnnuityOption.getSelectedItem().toString().equals("Life and Last Survivor with Capital Refund - 100% Income")) && Math.abs(difference) > 30) {

            showAlert.setMessage("Maximum Age difference between First Annuitant and Second Annutant is 30 Years");
            showAlert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            showAlert.show();
            return false;
        }

        return true;
    }


    /****************************** Async Task starts here *******************************************/


    class AsyncAnnuityPlus extends AsyncTask<String, Void, String> {
        final Context mContext;
        final ProgressDialog progressDialog;
        final String NAMESPACE;

        final String SOAP_ACTION;
        final String METHOD_NAME;
        String modeOfAnnuityPayout, annuityAmtPayable, purchasePrice,
                riderSumAssured, riderPrem, totServiceTax, advancePremPayable,
                totalPrem, totalPremReq, addPremPayable;
        final DecimalFormat currencyFormat;
        String selModeofAnnuityPayoutsText, selApplicableForText, selSourceOfBusinessText, selChannelDetailsText,
                selAnnuityOptionText, selFirstAnnutantGenderText, selOptForText, proposalDateText, edAdditionalAmtText,
                selSecondAnnutantAgeText, selFirstAnnutantAgeText, selSecondAnnutantGenderText, edVestingAmtText, edAnnuityAmtText,
                AdvAnnPayoutDateText;
        Boolean selJKResidentBool, selAdvAnnPayoutBool, selADBRiderBool;


        AsyncAnnuityPlus(Context mContext, ProgressDialog progressDialog) {
            // TODO Auto-generated constructor stub

            this.NAMESPACE = "http://tempuri.org/";

            this.SOAP_ACTION = "http://tempuri.org/getPremiumAnnuityPlusNew";
            this.METHOD_NAME = "getPremiumAnnuityPlusNew";
            this.mContext = mContext;
            this.progressDialog = progressDialog;
            currencyFormat = new DecimalFormat("##,##,##,###");
        }

        @Override
        protected void onPreExecute() {
            selModeofAnnuityPayoutsText = selModeofAnnuityPayouts.getSelectedItem().toString();
            selApplicableForText = selApplicableFor.getSelectedItem().toString();
            selSourceOfBusinessText = selSourceOfBusiness.getSelectedItem().toString();
            selChannelDetailsText = selChannelDetails.getSelectedItem().toString();
            selAnnuityOptionText = selAnnuityOption.getSelectedItem().toString();
            selFirstAnnutantGenderText = selFirstAnnutantGender.getSelectedItem().toString();
            selOptForText = selOptFor.getSelectedItem().toString();
            proposalDateText = proposalDate.getText().toString();
            edAdditionalAmtText = edAdditionalAmt.getText().toString();
            selSecondAnnutantAgeText = selSecondAnnutantAge.getSelectedItem().toString();
            selFirstAnnutantAgeText = selFirstAnnutantAge.getSelectedItem().toString();
            selSecondAnnutantGenderText = selSecondAnnutantGender.getSelectedItem().toString();
            edVestingAmtText = edVestingAmt.getText().toString();
            edAnnuityAmtText = edAnnuityAmt.getText().toString();
            AdvAnnPayoutDateText = AdvAnnPayoutDate.getText().toString();
            selJKResidentBool = selJKResident.isChecked();
            selAdvAnnPayoutBool = selAdvAnnPayout.isChecked();
            selADBRiderBool = selADBRider.isChecked();

        }

        @Override
        protected String doInBackground(String... param) {
            // TODO Auto-generated method stub

            if (mCommonMethods.isNetworkConnected(mContext)) {

                try {
                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                    request.addProperty("isStaff", "false");

                    request.addProperty("isJkResident", String.valueOf(selJKResidentBool));


                    if ((selModeofAnnuityPayoutsText.equalsIgnoreCase("Half Yearly") || selModeofAnnuityPayoutsText.equalsIgnoreCase("Yearly")) && selAdvAnnPayoutBool) {
                        request.addProperty("isAdvanceAnnuityPayout", "true");
                    } else if ((selModeofAnnuityPayoutsText.equalsIgnoreCase("Half Yearly") || selModeofAnnuityPayoutsText.equalsIgnoreCase("Yearly")) && !selAdvAnnPayoutBool) {
                        request.addProperty("isAdvanceAnnuityPayout", "false");
                    } else {
                        request.addProperty("isAdvanceAnnuityPayout", "false");
                    }

                    if (selADBRiderBool) {
                       /* request.addProperty("isADB", "true");
                        request.addProperty("applicableFor", selApplicableForText);*/
                        request.addProperty("isADB", "false");
                        request.addProperty("applicableFor", "");


                    } else {
                        request.addProperty("isADB", "false");
                        request.addProperty("applicableFor", "");
                    }

                    request.addProperty("sourceOfBusiness", selSourceOfBusinessText);
                    request.addProperty("channelDetails", selChannelDetailsText);
                    request.addProperty("modeOfAnnuityPayout",
                            selModeofAnnuityPayoutsText);
                    request.addProperty("annuityOption", selAnnuityOptionText);
                    request.addProperty("genderOfFirstAnnuitant",
                            selFirstAnnutantGenderText);
                    request.addProperty("optFor", selOptForText);
                    //Added By Manish on 31-07-2019
                    request.addProperty("KFC", "false");


                    try {
                        // request.addProperty("proposalDate",new
                        // SimpleDateFormat("MM/dd/yyyy",Locale.ENGLISH).parse(proposalDate.getText().toString()));
                        request.addProperty("proposalDate", proposalDateText);

                    } catch (Exception ignored) {

                    }

                    if (selAdvAnnPayoutBool) {
                        try {
                            request.addProperty("annuityPayoutDate",
                                    AdvAnnPayoutDateText);

                        } catch (Exception e) {
                            //        	  System.out.println("In exception " + e.getMessage());
                        }
                    } else {
                        request.addProperty("annuityPayoutDate", "");
                    }

                    request.addProperty("ageOfFirstAnnuitant",
                            selFirstAnnutantAgeText);


                    if ((selAnnuityOptionText.equals("Life and Last Survivor - 50% Income") || selAnnuityOptionText.equals("Life and Last Survivor - 100% Income") || selAnnuityOptionText.equals("Life and Last Survivor with Capital Refund - 50% Income") || selAnnuityOptionText.equals("Life and Last Survivor with Capital Refund - 100% Income"))) {

                        request.addProperty("ageOfSecondAnnuitant",
                                selSecondAnnutantAgeText);
                        request.addProperty("genderOfSecondAnnuitant",
                                selSecondAnnutantGenderText);

                    } else {
                        request.addProperty("ageOfSecondAnnuitant", "");
                        request.addProperty("genderOfSecondAnnuitant", "");
                    }

                    if (selSourceOfBusinessText.equals("Vesting/Death/Surrender of existing SBI Life's pension policy")) {

                        if (selOptForText.equals("Annuity Payout Amount")) {

                            request.addProperty("additionalAmountIfAny", "0");
                            request.addProperty("vestingAmount", edVestingAmtText);
                            request.addProperty("annuityAmount", edAnnuityAmtText);

                        } else if (selOptForText.equals("Premium Amount")) {
                            request.addProperty("additionalAmountIfAny",
                                    edAdditionalAmtText);
                            request.addProperty("vestingAmount", edVestingAmtText);
                            request.addProperty("annuityAmount", "0");

                        }
                    }
                    //Other than Vesting/Death/Surrender of existing SBI Life's pension policy
                    else {
                        if (selOptForText.equals("Annuity Payout Amount")) {
                            request.addProperty("additionalAmountIfAny", "0");
                            request.addProperty("vestingAmount", "0");
                            request.addProperty("annuityAmount", edAnnuityAmtText);

                        } else if (selOptForText.equals("Premium Amount")) {
                            request.addProperty("additionalAmountIfAny", "0");
                            request.addProperty("vestingAmount", edVestingAmtText);
                            request.addProperty("annuityAmount", "0");

                        }
                    }


                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    // Enable this envelope if service is written in dot net
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(
                            URL);

                    mCommonMethods.TLSv12Enable();

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    androidHttpTransport.call(SOAP_ACTION, envelope);
                    SoapPrimitive response = (SoapPrimitive) envelope
                            .getResponse();

                    String result = response.toString();

                    // System.out.println("result " + result);

                    if (!result.contains("<errorMessage>")) {

                        ParseXML prsObj = new ParseXML();

                        if (result.contains("<annuityPayoutDateError>")) {
                            result = prsObj.parseXmlTag(result,
                                    "annuityPayoutDateError");
                            return result;
                        } else if (result.contains("<minAnnuityAmountError>")) {
                            result = prsObj.parseXmlTag(result,
                                    "minAnnuityAmountError");
                            return result;
                        } else {

                            result = prsObj.parseXmlTag(result, "annuityPlus");
                            modeOfAnnuityPayout = prsObj.parseXmlTag(result,
                                    "modeOfAnnuityPayout");
                            annuityAmtPayable = prsObj.parseXmlTag(result,
                                    "annuityAmtPayable");
                            purchasePrice = prsObj.parseXmlTag(result,
                                    "purchasePrice");
                            riderSumAssured = prsObj.parseXmlTag(result,
                                    "riderSumAssured");
                            riderPrem = prsObj.parseXmlTag(result, "riderPrem");
                            totServiceTax = prsObj.parseXmlTag(result,
                                    "totServiceTax");
                            advancePremPayable = prsObj.parseXmlTag(result,
                                    "advancePremPayable");
                            totalPrem = prsObj.parseXmlTag(result, "totalPrem");
                            totalPremReq = prsObj.parseXmlTag(result,
                                    "totalPremReq");
                            addPremPayable = prsObj.parseXmlTag(result, "addPremPayable");


                        }
                        return "Success";

                    } else {
                        return "Server not Found. Please try after some time.";
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return "Server not Found. Please try after some time.";
                }

            } else
                return "Please Activate Internet connection";

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            try {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

            } catch (Exception e) {
                e.getMessage();
            }

            if (result.equals("Success")) {

                Intent i = new Intent(mContext, success.class);

                i.putExtra("op", "Mode of Annuity Payout is "
                        + modeOfAnnuityPayout);
                i.putExtra(
                        "op1",
                        "Annuity Amount Payable is Rs."
                                + currencyFormat.format(Double
                                .parseDouble(annuityAmtPayable)));
                i.putExtra(
                        "op2",
                        "Purchase Price is Rs. "
                                + currencyFormat.format(Double
                                .parseDouble(purchasePrice)));

                if (selADBRiderBool) {
                    i.putExtra(
                            "op3",
                            "Rider Sum Assured is Rs. "
                                    + currencyFormat.format(Double
                                    .parseDouble(riderSumAssured)));
                    i.putExtra(
                            "op4",
                            "Rider Premium is Rs. "
                                    + currencyFormat.format(Double
                                    .parseDouble(riderPrem)));
                }

                i.putExtra("op5", "Total Applicable Taxes is Rs. " + currencyFormat.format(Double.parseDouble(totServiceTax)));

                if (selAdvAnnPayoutBool) {
                    i.putExtra(
                            "op6",
                            "Advance Premium Payable is Rs. "
                                    + currencyFormat.format(Double
                                    .parseDouble(advancePremPayable)));
                }

                i.putExtra(
                        "op7",
                        "Total Premium is Rs. "
                                + currencyFormat.format(Double
                                .parseDouble(totalPrem)));

                if (selSourceOfBusinessText.equals("Vesting/Death/Surrender of existing SBI Life's pension policy")
                        || selSourceOfBusinessText.equals("Open Market Option (Any Other Life Insurance Company Pension Policy)")) {
                    i.putExtra(
                            "op8",
                            "Total Premium Required is Rs. "
                                    + currencyFormat.format(Double
                                    .parseDouble(totalPremReq)));
                    i.putExtra(
                            "op9",
                            "Additional Premium Payable is Rs. "
                                    + currencyFormat.format(Double
                                    .parseDouble(addPremPayable)));
                }

                i.putExtra("ProductDescName", "SBI Life - Annuity Plus");
                i.putExtra("productUINName", "(UIN:" + getString(R.string.sbi_life_annuity_plus_uin) + ")");
                mContext.startActivity(i);

            } else {
                Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
            }
        }
    }

    class ServiceHits extends AsyncTask<String, Void, String> {
        final Context mContext;
        ProgressDialog progressDialog;
        final String NAMESPACE;
        final String SOAP_ACTION;
        final String METHOD_NAME;

        final DecimalFormat currencyFormat;
        String selJKResidentText, selModeofAnnuityPayoutsText, selApplicableForText, selSourceOfBusinessText, selChannelDetailsText,
                selAnnuityOptionText, selFirstAnnutantGenderText, selOptForText, proposalDateText, AdvAnnPayoutDateText,
                selFirstAnnutantAgeText, selSecondAnnutantAgeText, selSecondAnnutantGenderText, edVestingAmtText, edAnnuityAmtText,
                edAdditionalAmtText;
        Boolean selAdvAnnPayoutBool, selADBRiderBool, selJKResidentBool;

        ServiceHits(Context mContext, ProgressDialog progressDialog) {
            // TODO Auto-generated constructor stub
            this.NAMESPACE = "http://tempuri.org/";

            this.SOAP_ACTION = "http://tempuri.org/saveSmartAdvisorServiceHits";
            this.METHOD_NAME = "saveSmartAdvisorServiceHits";
            this.mContext = mContext;
            this.progressDialog = progressDialog;
            currencyFormat = new DecimalFormat("##,##,##,###");
        }

        @Override
        protected void onPreExecute() {
            selModeofAnnuityPayoutsText = selModeofAnnuityPayouts.getSelectedItem().toString();
            selApplicableForText = selApplicableFor.getSelectedItem().toString();
            selSourceOfBusinessText = selSourceOfBusiness.getSelectedItem().toString();
            selChannelDetailsText = selChannelDetails.getSelectedItem().toString();
            selAnnuityOptionText = selAnnuityOption.getSelectedItem().toString();
            selFirstAnnutantGenderText = selFirstAnnutantGender.getSelectedItem().toString();
            selOptForText = selOptFor.getSelectedItem().toString();
            proposalDateText = proposalDate.getText().toString();
            edAdditionalAmtText = edAdditionalAmt.getText().toString();
            selSecondAnnutantAgeText = selSecondAnnutantAge.getSelectedItem().toString();
            selFirstAnnutantAgeText = selFirstAnnutantAge.getSelectedItem().toString();
            selSecondAnnutantGenderText = selSecondAnnutantGender.getSelectedItem().toString();
            edVestingAmtText = edVestingAmt.getText().toString();
            edAnnuityAmtText = edAnnuityAmt.getText().toString();
            AdvAnnPayoutDateText = AdvAnnPayoutDate.getText().toString();
            selJKResidentBool = selJKResident.isChecked();
            selAdvAnnPayoutBool = selAdvAnnPayout.isChecked();
            selADBRiderBool = selADBRider.isChecked();

        }

        @Override
        protected String doInBackground(String... param) {
            // TODO Auto-generated method stub

            if (mCommonMethods.isNetworkConnected(mContext)) {

                try {

                    StringBuilder serviceInput = new StringBuilder();


                    serviceInput.append("isStaff=" + "nonStaff");


                    serviceInput.append(",isJkResident=").append(selJKResidentBool);


                    if ((selModeofAnnuityPayoutsText.equalsIgnoreCase("Half Yearly") || selModeofAnnuityPayoutsText.equalsIgnoreCase("Yearly")) && selAdvAnnPayoutBool) {
                        serviceInput.append(",isAdvanceAnnuityPayout=" + "true");
                    } else if ((selModeofAnnuityPayoutsText.equalsIgnoreCase("Half Yearly") || selModeofAnnuityPayoutsText.equalsIgnoreCase("Yearly")) && !selAdvAnnPayoutBool) {
                        serviceInput.append(",isAdvanceAnnuityPayout=" + "false");
                    } else {
                        serviceInput.append(",isAdvanceAnnuityPayout=" + "false");
                    }
                    if (selADBRiderBool) {
                        serviceInput.append(",isADB=" + "true");
                        serviceInput.append(",applicableFor=").append(selApplicableForText);

                    } else {
                        serviceInput.append(",isADB=" + "false");
                        serviceInput.append(",applicableFor=" + "");
                    }


                    serviceInput.append(",sourceOfBusiness=").append(selSourceOfBusinessText);
                    serviceInput.append(",channelDetails=").append(selChannelDetailsText);
                    serviceInput.append(",modeOfAnnuityPayout=").append(selModeofAnnuityPayoutsText);
                    serviceInput.append(",annuityOption=").append(selAnnuityOptionText);
                    serviceInput.append(",genderOfFirstAnnuitant=").append(selFirstAnnutantGenderText);
                    serviceInput.append(",optFor=").append(selOptForText);


                    try {
                        serviceInput.append(",proposalDate=").append(proposalDateText);

                    } catch (Exception ignored) {

                    }

                    if (selAdvAnnPayoutBool) {
                        try {
                            serviceInput.append(",annuityPayoutDate=").append(AdvAnnPayoutDateText);

                        } catch (Exception e) {
                            //        	  System.out.println("In exception " + e.getMessage());
                        }
                    } else {
                        serviceInput.append(",annuityPayoutDate=" + "");
                    }

                    serviceInput.append(",ageOfFirstAnnuitant=").append(selFirstAnnutantAgeText);


                    if ((selAnnuityOptionText.equals("Life and Last Survivor - 50% Income")
                            || selAnnuityOptionText.equals("Life and Last Survivor - 100% Income")
                            || selAnnuityOptionText.equals("Life and Last Survivor with Capital Refund - 50% Income") ||
                            selAnnuityOptionText.equals("Life and Last Survivor with Capital Refund - 100% Income"))) {

                        serviceInput.append(",ageOfSecondAnnuitant=").append(selSecondAnnutantAgeText);
                        serviceInput.append(",genderOfSecondAnnuitant=").append(selSecondAnnutantGenderText);

                    } else {
                        serviceInput.append(",ageOfSecondAnnuitant=" + "");
                        serviceInput.append(",genderOfSecondAnnuitant=" + "");
                    }


                    if (selSourceOfBusinessText.equals("Vesting/Death/Surrender of existing SBI Life's pension policy")) {
                        if (selOptForText.equals("Annuity Payout Amount")) {

                            serviceInput.append(",additionalAmountIfAny=" + "0");
                            serviceInput.append(",vestingAmount=").append(edVestingAmtText);
                            serviceInput.append(",annuityAmount=").append(edAnnuityAmtText);

                        } else if (selOptForText.equals("Premium Amount")) {
                            serviceInput.append(",additionalAmountIfAny=").append(edAdditionalAmtText);
                            serviceInput.append(",vestingAmount=").append(edVestingAmtText);
                            serviceInput.append(",annuityAmount=" + "0");

                        }
                    }
                    //Other than Vesting/Death/Surrender of existing SBI Life's pension policy
                    else {
                        if (selOptForText.equals("Annuity Payout Amount")) {
                            serviceInput.append(",additionalAmountIfAny=" + "0");
                            serviceInput.append(",vestingAmount=" + "0");
                            serviceInput.append(",annuityAmount=").append(edAnnuityAmtText);

                        } else if (selOptForText.equals("Premium Amount")) {
                            serviceInput.append(",additionalAmountIfAny=" + "0");
                            serviceInput.append(",vestingAmount=").append(edVestingAmtText);
                            serviceInput.append(",annuityAmount=" + "0");

                        }
                    }


//					System.out.println("serviceInput"+serviceInput.toString());

                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);


                    request.addProperty("serviceName", METHOD_NAME);
                    request.addProperty("strProdCode", "");


                    request.addProperty("serviceInput", serviceInput);
                    request.addProperty("serviceReqUserId", strCIFCode);
                    request.addProperty("strEmailId", strCIFBDMEmailId);
                    request.addProperty("strMobileNo", strCIFBDMMObileNo);
                    request.addProperty("strAuthKey", strCIFBDMPassword.trim());

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);
                    // Enable this envelope if service is written in dot net
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(
                            URL);

                    mCommonMethods.TLSv12Enable();

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    androidHttpTransport.call(SOAP_ACTION, envelope);
                    SoapPrimitive response = (SoapPrimitive) envelope
                            .getResponse();

                    String result = response.toString();

                    if (result.contains("<ErrCode>0</ErrCode>")) {

                        return "Success";
                    } else
                        return "Server not Found. Please try after some time.";

//					return "Success";

                } catch (Exception e) {
                    e.printStackTrace();
                    return "Server not Found. Please try after some time.";
                }

            } else
                return "Please Activate Internet connection";

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            try {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

            } catch (Exception e) {
                e.getMessage();
            }

            addListenerOnSubmit();
        }
    }

    /******************** SSL certificate end *****************************************************************/

    public void OnProductDtls(View v) {
        Intent intent = new Intent(mContext, ProductDesc_webview.class);
        intent.putExtra("name", inputActivityHeader.getText().toString());
        startActivity(intent);
    }


}
