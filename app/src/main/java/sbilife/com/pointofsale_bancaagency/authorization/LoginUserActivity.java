package sbilife.com.pointofsale_bancaagency.authorization;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.text.TextUtils;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.clsLogin;
import sbilife.com.pointofsale_bancaagency.common.AppSharedPreferences;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.MHRNotificationDailyBroadcastReceiver;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.home.CarouselHomeActivity;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits.DownLoadData;

public class LoginUserActivity extends AppCompatActivity implements OnClickListener, DownLoadData,
        OnItemSelectedListener {
    private final String NAMESPACE = "http://tempuri.org/";
    private final String URl = ServiceURL.SERVICE_URL;

    private final String METHOD_NAME_LOGIN_SMRT = "LoginSMRT_smrt";
    private final String METHOD_FILL_CAPCHA = "FillCapcha_smrt";
    private final int DIALOG_DOWNLOAD_PROGRESS = 0, PERMS_RUNTIME_REQ_CODE = 100;
    private DownloadFileAsyncValidateUser taskAsyncValidateUser;
    private CommonMethods mCommonMethods = new CommonMethods();
    private DatabaseHelper dbHelper;
    private ProgressDialog progressDialog = null;
    private EditText edittextBDMUMCode, editTextCIFAgentCode, edittextLoginOtp, editTextLoginEmailId, editTextMobileNumber,
            editTextLoginPIN;
    private TableRow tableRowLoginBDMUM, tableRowLoginCIF, tableRowLoginOTP, tableRowLoginEmailId, tableRowLoginMobileNo,
            tableRowLoginPIN;
    private LinearLayout linearlayoutLoginAuthenticationType, llCaptcha;
    private Spinner spinnerloginMode, spinnerSelectLoginUserType, spinnerloginAuthenticationType;

    private String strType = "", str_login_mode = "",
            strEmail = "", strMobile = "", strPIN = "",
            strBDMUMCode = "", strOtp = "", strID = "", strAuthenticationType = "",
            strOther = "";

    private TextView textViewSpinnerloginMode;

    //	private LinearLayout linearlayoutLoginMode;
    private Context context;
    private Button buttonLogin, buttonVerifyOTP;//buttonGenrateOtp,
    private CheckBox checkBoxLoginAsBDMOrCIF;

    private boolean isLoginAsCIF = false;

    private int current_version = 0;

    private CheckAppVesrion checkAppVesrion;
    private AsyncFillCaptcha mAsyncFillCaptcha;
    private ServiceHits service;

    private String[] arrPermissionRuntime = {Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.READ_PHONE_STATE};

    private SharedPreferences mPreferences;

    private ImageView imageCaptchaRefresh;
    private TextView tvCaptchaCode;
    private EditText edittextCaptcha;

    @SuppressWarnings("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.layout_new_login);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.smart_advisor_title);

        context = this;
        dbHelper = new DatabaseHelper(this);
        mCommonMethods = new CommonMethods();

        mPreferences = getPreferences(MODE_PRIVATE);

        setUI_Id();
        textViewSpinnerloginMode.setVisibility(View.GONE);
        spinnerloginMode.setVisibility(View.GONE);

        String[] userTypeList = {"Select User Type", "CIF(Banca)", "AGENT(Retail)", "BDM(Banca)", "UM(Retail)", "AM(Banca)",
                "SAM(Banca)", "ZAM(Banca)",
                "BSM(Retail)", "DSM(Retail)",/*"ASM(Retail)","RSM(Retail)",*/
                "CAG"};

        ArrayAdapter<String> userTypeAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, userTypeList);
        userTypeAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerSelectLoginUserType.setAdapter(userTypeAdapter);
        userTypeAdapter.notifyDataSetChanged();
        spinnerSelectLoginUserType.setOnItemSelectedListener(this);

        String[] autheticationType = {"Select Authentication Mode", "OTP", "PIN"};
        ArrayAdapter<String> autheticationTypeAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, autheticationType);
        autheticationTypeAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerloginAuthenticationType.setAdapter(autheticationTypeAdapter);
        autheticationTypeAdapter.notifyDataSetChanged();
        spinnerloginAuthenticationType.setOnItemSelectedListener(this);

       /* ArrayList<String> stringArrayList =new ArrayList<>();
        stringArrayList.add("2");
        stringArrayList.add(0,"3");

        Log.d("Arr",stringArrayList.size()+"");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        Date FUPDate = null;
        try {
            FUPDate = dateFormat.parse("01-JAN-2018");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date today = new Date();
        double diff =  Math.abs(today.getTime()-FUPDate.getTime());
        double numOfYear = Math.ceil((double) ((diff / (1000 * 60 * 60 * 24))/365));
        double halfYear = Math.ceil((double) ((diff / (1000 * 60 * 60 * 24))/180));
        double quarterly = Math.ceil((double) ((diff / (1000 * 60 * 60 * 24))/90));
        double monthly = Math.ceil((double) ((diff / (1000 * 60 * 60 * 24))/30));

        System.out.println("monthly = " + monthly);
        System.out.println("quarterly = " + quarterly);
        System.out.println("halfYear = " + halfYear);
        System.out.println("numOfYear = " + numOfYear);*/

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 9);
            calendar.set(Calendar.MINUTE, 59);

            if (calendar.getTime().compareTo(new Date()) < 0)
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            Intent intent = new Intent(getApplicationContext(), MHRNotificationDailyBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            if (alarmManager != null) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //Run time permission
        if (mCommonMethods.isDeviceAboveM()) {

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrPermissionRuntime, PERMS_RUNTIME_REQ_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        String str_grant_msg = "";

        switch (requestCode) {

            case PERMS_RUNTIME_REQ_CODE:

                if (grantResults != null) {

                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        str_grant_msg += "App requires Camera Access Permission\n";
                    }

                    if (grantResults[1] != PackageManager.PERMISSION_GRANTED
                            || grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                        str_grant_msg += "App requires Location Access Permission\n";
                    }

                    if (grantResults[3] != PackageManager.PERMISSION_GRANTED
                            || grantResults[4] != PackageManager.PERMISSION_GRANTED) {
                        str_grant_msg += "App requires Storage Access Permission\n";
                    }

                    if (grantResults[5] != PackageManager.PERMISSION_GRANTED) {
                        str_grant_msg += "App requires Bluetooth Permission\n";
                    }

                    if (grantResults[6] != PackageManager.PERMISSION_GRANTED) {
                        str_grant_msg += "App requires Bluetooth Admin Permission\n";
                    }

                    if (grantResults[7] != PackageManager.PERMISSION_GRANTED) {
                        str_grant_msg += "App requires Phone State Permission\n";
                    }

                    if (!str_grant_msg.equals("")) {
                        mCommonMethods.showMessageDialog(context, str_grant_msg);
                    }
                }

                break;

            default:
                break;

        }
    }

    /*private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle b = intent.getExtras();
            String message = b.getString("message");
            edittextLoginOtp.setText(message);
            Log.e("newmesage", "" + message);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        try {
            this.unregisterReceiver(broadcastReceiver);
        } catch (Exception ignored) {

        }
    }*/


    private void setUI_Id() {

        textViewSpinnerloginMode = findViewById(R.id.textViewSpinnerloginMode);
        TextView textViewUserManualLogin = findViewById(R.id.textViewUserManualLogin);

        edittextBDMUMCode = findViewById(R.id.edittextBDMUMCode);
        editTextCIFAgentCode = findViewById(R.id.editTextCIFAgentCode);
        edittextLoginOtp = findViewById(R.id.edittextLoginOtp);
        editTextLoginEmailId = findViewById(R.id.editTextLoginEmailId);
        editTextMobileNumber = findViewById(R.id.editTextMobileNumber);
        editTextLoginPIN = findViewById(R.id.editTextLoginPIN);

        spinnerloginMode = findViewById(R.id.spinnerloginMode);
        spinnerSelectLoginUserType = findViewById(R.id.spinnerSelectLoginUserType);
        spinnerloginAuthenticationType = findViewById(R.id.spinnerloginAuthenticationType);

        checkBoxLoginAsBDMOrCIF = findViewById(R.id.checkBoxLoginAsBDMOrCIF);

        tableRowLoginBDMUM = findViewById(R.id.tableRowLoginBDMUM);
        tableRowLoginCIF = findViewById(R.id.tableRowLoginCIF);
        tableRowLoginOTP = findViewById(R.id.tableRowLoginOTP);
        tableRowLoginEmailId = findViewById(R.id.tableRowLoginEmailId);
        tableRowLoginMobileNo = findViewById(R.id.tableRowLoginMobileNo);
        tableRowLoginPIN = findViewById(R.id.tableRowLoginPIN);

        linearlayoutLoginAuthenticationType = findViewById(R.id.linearlayoutLoginAuthenticationType);

        TextView textViewLoginNewUser = findViewById(R.id.textViewLoginNewUser);
        TextView textViewForgetPasswordLogin = findViewById(R.id.textViewForgetPasswordLogin);
        textViewLoginNewUser.setOnClickListener(this);
        textViewForgetPasswordLogin.setOnClickListener(this);


        buttonLogin = findViewById(R.id.buttonLogin);
        //buttonGenrateOtp = (Button) findViewById(R.id.buttonGenrateOtp);
        buttonVerifyOTP = findViewById(R.id.buttonVerifyOTP);
        buttonLogin.setOnClickListener(this);
        //buttonGenrateOtp.setOnClickListener(this);
        buttonVerifyOTP.setOnClickListener(this);
        textViewUserManualLogin.setOnClickListener(this);

        imageCaptchaRefresh = findViewById(R.id.imageCaptchaRefresh);
        imageCaptchaRefresh.setOnClickListener(this);

        tvCaptchaCode = findViewById(R.id.tvCaptchaCode);
        edittextCaptcha = findViewById(R.id.edittextCaptcha);
        llCaptcha = findViewById(R.id.llCaptcha);

        checkBoxLoginAsBDMOrCIF.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    isLoginAsCIF = true;

                    //buttonGenrateOtp.setVisibility(View.VISIBLE);
                    buttonVerifyOTP.setVisibility(View.GONE);
                    buttonLogin.setVisibility(View.VISIBLE);
                    buttonLogin.setText("Generate OTP");
                    tableRowLoginCIF.setVisibility(View.VISIBLE);
                    tableRowLoginBDMUM.setVisibility(View.VISIBLE);
                    linearlayoutLoginAuthenticationType.setVisibility(View.GONE);

                    tableRowLoginOTP.setVisibility(View.GONE);
                    tableRowLoginEmailId.setVisibility(View.GONE);
                    tableRowLoginMobileNo.setVisibility(View.GONE);
                    tableRowLoginPIN.setVisibility(View.GONE);

                    setBlankValueToEdittext();
                    setBlankValuesToString();

                    textViewSpinnerloginMode.setVisibility(View.GONE);
                    spinnerloginMode.setVisibility(View.GONE);
                    spinnerloginMode.setSelection(0);

                } else {
                    isLoginAsCIF = false;

                    buttonVerifyOTP.setVisibility(View.GONE);
                    buttonLogin.setVisibility(View.VISIBLE);
                    buttonLogin.setText("Login");
                    tableRowLoginCIF.setVisibility(View.GONE);
                    tableRowLoginBDMUM.setVisibility(View.GONE);
                    linearlayoutLoginAuthenticationType.setVisibility(View.GONE);
                    setBlankValueToEdittext();
                    setBlankValuesToString();

                    textViewSpinnerloginMode.setVisibility(View.VISIBLE);
                    spinnerloginMode.setVisibility(View.VISIBLE);
                    spinnerloginMode.setSelection(0);
                }
            }
        });


    }

    @SuppressWarnings("deprecation")
    private void showProgressDialog() {
        try {
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        } catch (Exception ignored) {

        }
    }

    @SuppressWarnings("deprecation")
    private void dismissProgressDialog() {
        try {
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
        } catch (Exception ignored) {

        }
    }

    @Override
    public void downLoadData() {

        //reset flags for dashborad renewal preferences
        try {
            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
            editor.putString(mCommonMethods.getDASHBOARD_RENEWAL_UPDATE_PREFERENCE(), "false");
            editor.putString(mCommonMethods.getNOTIFICATION_PREFERENCE(), "false");
            editor.putString(mCommonMethods.getClaimRequirementInfo(), "false");
            editor.putString(mCommonMethods.getLOGGED_PROPOSAL_COUNT_NOTIFICATION_PREFERENCE(), "false");
            editor.putString(mCommonMethods.getNON_MEDICAL_PENDING_REQ_COUNT_NOTIFICATION_PREFERENCE(), "False");
            editor.putString(mCommonMethods.getMEDICAL_PENDING_REQ_COUNT_NOTIFICATION_PREFERENCE(), "False");

            editor.putString(mCommonMethods.MATURITY_LIST_NOTIFICATION_PREFERENCE, "false");
            editor.putString(mCommonMethods.SB_DUE_LIST_NOTIFICATION_PREFERENCE, "false");
            editor.putString(mCommonMethods.getKYCMissingNotification(), "False");
            editor.apply();

            AppSharedPreferences.setData(context, new AppSharedPreferences().PERSISTENCY_KEY, "0");

            //Added Tushar Kadam

            String kerlaDiscountDetails = AppSharedPreferences.getData(context, (mCommonMethods.getKerlaDiscount()), "");

            if (!TextUtils.isEmpty(kerlaDiscountDetails)) {
                String[] split = kerlaDiscountDetails.split(",");
                String userCode = split[0];

                if (!userCode.equalsIgnoreCase(strID)) {
                    AppSharedPreferences.setData(context, (mCommonMethods.getKerlaDiscount()), "");
                }
            }
            //End Added Tushar Kadam

            Intent intent = new Intent(this, CarouselHomeActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startDownloadLoginSmrt() {
        if (str_login_mode.equalsIgnoreCase("Email-Id")) {

            if (!strEmail.equalsIgnoreCase("")) {
                strOther = strEmail;
            }

        } else if (str_login_mode.equalsIgnoreCase("Mobile Number")) {
            if (!strMobile.equalsIgnoreCase("")) {
                strOther = strMobile;
            }
        } else {
            strOther = "";
        }
        taskAsyncValidateUser = new DownloadFileAsyncValidateUser();
        taskAsyncValidateUser.execute();
    }

    private void service_hits() {
        CommonMethods.UserDetailsValuesModel userDetails = mCommonMethods
                .setUserDetails(context);

        service = new ServiceHits(this,
                METHOD_NAME_LOGIN_SMRT, String.valueOf(current_version), userDetails.getStrCIFBDMUserId(),
                userDetails.getStrCIFBDMEmailId(), userDetails.getStrCIFBDMMObileNo(),
                mCommonMethods.GetUserPassword(context), this);
        service.execute();
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {
            case R.id.buttonLogin:
                mCommonMethods.hideKeyboard(buttonLogin, context);
                strBDMUMCode = edittextBDMUMCode.getText().toString();
                strID = editTextCIFAgentCode.getText().toString();
                btnLoginValidation();

                break;
            case R.id.buttonVerifyOTP:
                mCommonMethods.hideKeyboard(buttonVerifyOTP, context);
                strBDMUMCode = edittextBDMUMCode.getText().toString();
                strID = editTextCIFAgentCode.getText().toString();
                if (strType.equalsIgnoreCase("BDM") || strType.equalsIgnoreCase("UM")) {
                    if (validateBDMUM() && validateOTP()) {
                        validateCaptcha();
                    }
                } else {
                    if (Validation() && validateOTP()) {
                        validateCaptcha();
                    }
                }
                break;
            case R.id.textViewLoginNewUser:
                mCommonMethods.hideKeyboard(buttonVerifyOTP, context);
                Intent i = new Intent(context, RegisterUserActivity.class);
                i.putExtra("isNewUser", "Y");
                startActivity(i);
                finish();
                break;
            case R.id.textViewForgetPasswordLogin:
                mCommonMethods.hideKeyboard(buttonVerifyOTP, context);
                Intent intent = new Intent(context, ForgetLoginPINActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.textViewUserManualLogin:
                mCommonMethods.loadDriveURL("https://drive.google.com/open?id=1NgRm9Jt8tEJHM8hAFZvB-SlFZKosaP6h", context);
                break;

            case R.id.imageCaptchaRefresh:
                getCaptcha();
                break;
        }
    }

    private void btnLoginValidation() {

        if (strType.equalsIgnoreCase("CIF") || strType.equalsIgnoreCase("Agent")
                //for Agent/BAP/CAG/IMF
                || strType.equalsIgnoreCase("BAP")
                || strType.equalsIgnoreCase("CAG") || strType.equalsIgnoreCase("IMF")) {
            str_login_mode = spinnerloginMode.getSelectedItem().toString();

            if (str_login_mode.equalsIgnoreCase("Select User Id")) {
                String error = "Please select User Id";
                mCommonMethods.showMessageDialog(context, error);
            } else {
                strID = editTextCIFAgentCode.getText().toString();

                strEmail = editTextLoginEmailId.getText().toString();
                strMobile = editTextMobileNumber.getText().toString();

                strPIN = editTextLoginPIN.getText().toString();
                strAuthenticationType = spinnerloginAuthenticationType.getSelectedItem().toString();

                if (Validation() && validateUserAuthentication()) {
                    if (strAuthenticationType.equalsIgnoreCase("PIN") && validatePIN()) {
                        strOtp = "";
                        validateCaptcha();
                    } else if (strAuthenticationType.equalsIgnoreCase("OTP")) {
                        strOtp = "OTP";
                        validateCaptcha();
                    }
                }

            }

        } else if (strType.equalsIgnoreCase("BDM") || strType.equalsIgnoreCase("UM")) {
            strBDMUMCode = edittextBDMUMCode.getText().toString();
            strID = editTextCIFAgentCode.getText().toString();
            strPIN = editTextLoginPIN.getText().toString();
            strAuthenticationType = spinnerloginAuthenticationType.getSelectedItem().toString();

            strEmail = editTextLoginEmailId.getText().toString();
            strMobile = editTextMobileNumber.getText().toString();

            if (!isLoginAsCIF) {
                // if (!strType.equalsIgnoreCase("UM")) {

                if (str_login_mode.equalsIgnoreCase("Select User Id")) {
                    String error = "Please select User Id";
                    mCommonMethods.showMessageDialog(context, error);
                } else if (Validation() && validateUserAuthentication()) {
                    if (strAuthenticationType.equalsIgnoreCase("PIN") && validatePIN()) {
                        strOtp = "";
                        validateCaptcha();
                    } else if (strAuthenticationType.equalsIgnoreCase("OTP")) {
                        strOtp = "OTP";
                        validateCaptcha();
                    }

                }

                /*} else if (validateBDMUM()) {
                    strOtp = "OTP";
                }*/
            } else if (validateBDMUM()) {
                strOtp = "OTP";
                validateCaptcha();
            }

        } else if (strType.equalsIgnoreCase("AM") || strType.equalsIgnoreCase("SAM")
                || strType.equalsIgnoreCase("ZAM") || strType.equalsIgnoreCase("BSM")
                || strType.equalsIgnoreCase("DSM") || strType.equalsIgnoreCase("ASM")
                || strType.equalsIgnoreCase("RSM")) {
            str_login_mode = spinnerloginMode.getSelectedItem().toString();
            if (str_login_mode.equalsIgnoreCase("Select User Id")) {
                String error = "Please select User Id";
                mCommonMethods.showMessageDialog(context, error);
            } else {
                strID = editTextCIFAgentCode.getText().toString();

                strEmail = editTextLoginEmailId.getText().toString();
                strMobile = editTextMobileNumber.getText().toString();

                strPIN = editTextLoginPIN.getText().toString();
                strAuthenticationType = spinnerloginAuthenticationType.getSelectedItem().toString();

                if (Validation() && validateUserAuthentication()) {
                    if (strAuthenticationType.equalsIgnoreCase("PIN") && validatePIN()) {
                        strOtp = "";
                        validateCaptcha();
                    } else if (strAuthenticationType.equalsIgnoreCase("OTP")) {
                        strOtp = "OTP";
                        validateCaptcha();
                    }
                }

            }
        } else if (strType.equalsIgnoreCase("Select User Type")) {
            String error = "Please select user type";
            mCommonMethods.showMessageDialog(context, error);
        }

    }

    //Validation for  USER id
    private boolean Validation() {
        boolean isvalid = true;
        try {
            String error = "";
            if (!mCommonMethods.isNetworkConnected(context)) {
                error = "Please turn on your internet connection";

                if (!error.equals("")) {
                    mCommonMethods.showMessageDialog(context, error);
                    return false;
                }
            }

            if (str_login_mode.equalsIgnoreCase("CIF Code")) {


                if (strID.equalsIgnoreCase("")) {
                    error = "Please enter CIF code";
                }

            } else if (str_login_mode.equalsIgnoreCase("UM Code")) {
                if (strBDMUMCode.equalsIgnoreCase("")) {
                    error = "Please enter UM code";
                }
            } else if (str_login_mode.equalsIgnoreCase("Agent Code")) {


                if (strID.equalsIgnoreCase("")) {
                    error = "Please enter Agent code";
                }

            } else if (str_login_mode.equalsIgnoreCase("BDM Code")) {


                if (strBDMUMCode.equalsIgnoreCase("")) {
                    error = "Please enter BDM code";
                }
            } else if (str_login_mode.equalsIgnoreCase("AM Code")) {
                if (strID.equalsIgnoreCase("")) {
                    error = "Please enter AM code";
                }
            } else if (str_login_mode.equalsIgnoreCase("SAM Code")) {
                if (strID.equalsIgnoreCase("")) {
                    error = "Please enter SAM code";
                }
            } else if (str_login_mode.equalsIgnoreCase("ZAM Code")) {
                if (strID.equalsIgnoreCase("")) {
                    error = "Please enter ZAM code";
                }
            } else if (str_login_mode.equalsIgnoreCase("BSM Code")) {
                if (strID.equalsIgnoreCase("")) {
                    error = "Please enter BSM code";
                }
            } else if (str_login_mode.equalsIgnoreCase("DSM Code")) {
                if (strID.equalsIgnoreCase("")) {
                    error = "Please enter DSM code";
                }
            } else if (str_login_mode.equalsIgnoreCase("ASM Code")) {
                if (strID.equalsIgnoreCase("")) {
                    error = "Please enter ASM code";
                }
            } else if (str_login_mode.equalsIgnoreCase("RSM Code")) {
                if (strID.equalsIgnoreCase("")) {
                    error = "Please enter RSM code";
                }
            } else if (str_login_mode.equalsIgnoreCase("Email-Id")) {

                if (strEmail.equalsIgnoreCase("")) {
                    error = "Please enter Email-Id";
                }
                isvalid = mCommonMethods.emailPatternValidation(editTextLoginEmailId, context);

            } else if (str_login_mode.equalsIgnoreCase("Mobile Number")) {
                if (strMobile.equalsIgnoreCase("")) {
                    error = "Please enter Mobile number";
                }
                isvalid = mCommonMethods.mobileNumberPatternValidation(editTextMobileNumber, context);
            }


            if (!error.equals("")) {
                mCommonMethods.showMessageDialog(context, error);
                isvalid = false;
            }
        } catch (Exception e) {
            return isvalid;
        }

        return isvalid;

    }

    private boolean validateBDMUM() {
        try {
            String error = "";
            if (!mCommonMethods.isNetworkConnected(context)) {
                error = "Please turn on your internet connection";
                if (!error.equals("")) {
                    mCommonMethods.showMessageDialog(context, error);
                    return false;
                }
            }

            if (strType.equalsIgnoreCase("BDM") && isLoginAsCIF) {
                if (strBDMUMCode.equalsIgnoreCase("")) {
                    error = "Please enter BDM code";
                } else if (strID.equalsIgnoreCase("")) {
                    error = "Please enter CIF code";
                }
            } else if (strType.equalsIgnoreCase("UM")) {
                if (strBDMUMCode.equalsIgnoreCase("")) {
                    error = "Please enter UM code";
                } /*else if (strID.equalsIgnoreCase("")) {
                    error = "Please enter Agent code";
                }*/
            }

            if (!error.equals("")) {
                mCommonMethods.showMessageDialog(context, error);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean validateUserAuthentication() {
        String error = "";

        if (strAuthenticationType.equalsIgnoreCase("Select Authentication Mode")) {
            error = "Please select Authentication Mode";
        }
        if (!error.equals("")) {
            mCommonMethods.showMessageDialog(context, error);
            return false;
        }
        return true;
    }

    private boolean validatePIN() {
        boolean isvalid = true;
        try {
            String error = "";

            if (strPIN.equalsIgnoreCase("")) {
                error = "Please enter PIN";

            } else if (strPIN.length() < 4) {
                error = "Please enter 4 or 6 digit PIN number";
                isvalid = false;
            } else if (strPIN.length() == 5) {
                error = "Please enter 4 or 6 digit PIN number";
                isvalid = false;
            }

            if (!error.equals("")) {
                mCommonMethods.showMessageDialog(context, error);
                isvalid = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            isvalid = false;
        }
        return isvalid;
    }

    private boolean validateOTP() {
        try {
            String error = "";
            strOtp = edittextLoginOtp.getText().toString();

            if (strOtp.equalsIgnoreCase("")) {
                error = "Please enter OTP";
            }

            if (!error.equals("")) {
                mCommonMethods.showMessageDialog(context, error);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View selectedItemView, int position,
                               long arg3) {
        Spinner spinner = (Spinner) parent;
        int id = spinner.getId();
        if (id == R.id.spinnerSelectLoginUserType) {
            strType = spinnerSelectLoginUserType.getSelectedItem().toString();
            strType = mCommonMethods.getUserTypeSelected(strType);
            edittextCaptcha.setText("");
            llCaptcha.setVisibility(View.GONE);
            str_login_mode = "";
            setBlankValueToEdittext();
            setBlankValuesToString();
            isLoginAsCIF = false;
            if (strType.equalsIgnoreCase("CIF")) {
                editTextCIFAgentCode.setHint(" * Enter CIF Code");
                editTextCIFAgentCode.requestFocus();
                hideAllRowsForCIFAgent();
                setCIFLoginSpinner();
                spinnerloginMode.setSelection(0);

            } else if (strType.equalsIgnoreCase("Agent")) {
                editTextCIFAgentCode.setHint(" * Enter Agent Code");
                editTextCIFAgentCode.requestFocus();
                hideAllRowsForCIFAgent();
                setAgentLoginSpinner();
                spinnerloginMode.setSelection(0);
            } else if (strType.equalsIgnoreCase("BDM")) {

                editTextCIFAgentCode.setHint(" * Enter CIF Code");
                edittextBDMUMCode.setHint(" * Enter BDM Code");
                checkBoxLoginAsBDMOrCIF.setText("Login behalf of CIF");

                //setFocusable(edittextBDMUMCode);
                edittextBDMUMCode.requestFocus();
                checkBoxLoginAsBDMOrCIF.setVisibility(View.VISIBLE);

                textViewSpinnerloginMode.setVisibility(View.VISIBLE);
                spinnerloginMode.setVisibility(View.VISIBLE);
                spinnerloginMode.setSelection(0);
                hideAllRowsForBDMUM();
                setBDMLoginSpinner();
                tableRowLoginCIF.setVisibility(View.GONE);
                tableRowLoginBDMUM.setVisibility(View.GONE);

            } else if (strType.equalsIgnoreCase("UM")) {
                editTextCIFAgentCode.setHint(" * Enter Agent Code");
                edittextBDMUMCode.setHint(" * Enter UM Code");
                checkBoxLoginAsBDMOrCIF.setVisibility(View.GONE);
                tableRowLoginCIF.setVisibility(View.VISIBLE);

                edittextBDMUMCode.requestFocus();
                textViewSpinnerloginMode.setVisibility(View.VISIBLE);
                spinnerloginMode.setVisibility(View.VISIBLE);
                spinnerloginMode.setSelection(0);

                edittextBDMUMCode.setFocusable(true);
                setFocusable(edittextBDMUMCode);
                edittextBDMUMCode.requestFocus();

                hideAllRowsForBDMUM();
                setUMLoginSpinner();
                tableRowLoginCIF.setVisibility(View.VISIBLE);
                linearlayoutLoginAuthenticationType.setVisibility(View.GONE);

            } //for Agent/BAP/CAG/IMF
            else if (strType.equalsIgnoreCase("BAP") || strType.equalsIgnoreCase("CAG")
                    || strType.equalsIgnoreCase("IMF")) {

                editTextCIFAgentCode.setHint(" * Enter SP Code");
                editTextCIFAgentCode.requestFocus();
                hideAllRowsForCIFAgent();
                setBAP_CAG_IMF_Spinner();
                spinnerloginMode.setSelection(0);

            } else if (strType.equalsIgnoreCase("AM")) {

                editTextCIFAgentCode.setHint(" * Enter AM Code");
                editTextCIFAgentCode.requestFocus();
                hideAllRowsForCIFAgent();

                setChannelReportsSpinner("AM Code");
                spinnerloginMode.setSelection(0);
            } else if (strType.equalsIgnoreCase("SAM")) {

                editTextCIFAgentCode.setHint(" * Enter SAM Code");
                editTextCIFAgentCode.requestFocus();
                hideAllRowsForCIFAgent();

                setChannelReportsSpinner("SAM Code");
                spinnerloginMode.setSelection(0);
            } else if (strType.equalsIgnoreCase("ZAM")) {

                editTextCIFAgentCode.setHint(" * Enter ZAM Code");
                editTextCIFAgentCode.requestFocus();
                hideAllRowsForCIFAgent();

                setChannelReportsSpinner("ZAM Code");
                spinnerloginMode.setSelection(0);
            } else if (strType.equalsIgnoreCase("BSM")) {

                editTextCIFAgentCode.setHint(" * Enter BSM Code");
                editTextCIFAgentCode.requestFocus();
                hideAllRowsForCIFAgent();

                setChannelReportsSpinner("BSM Code");
                spinnerloginMode.setSelection(0);
            } else if (strType.equalsIgnoreCase("DSM")) {

                editTextCIFAgentCode.setHint(" * Enter DSM Code");
                editTextCIFAgentCode.requestFocus();
                hideAllRowsForCIFAgent();

                setChannelReportsSpinner("DSM Code");
                spinnerloginMode.setSelection(0);
            } else if (strType.equalsIgnoreCase("ASM")) {

                editTextCIFAgentCode.setHint(" * Enter ASM Code");
                editTextCIFAgentCode.requestFocus();
                hideAllRowsForCIFAgent();

                setChannelReportsSpinner("ASM Code");
                spinnerloginMode.setSelection(0);
            } else if (strType.equalsIgnoreCase("RSM")) {

                editTextCIFAgentCode.setHint(" * Enter RSM Code");
                editTextCIFAgentCode.requestFocus();
                hideAllRowsForCIFAgent();

                setChannelReportsSpinner("RSM Code");
                spinnerloginMode.setSelection(0);
            } else {
                spinnerloginMode.setSelection(0);
                hideAllRowsForCIFAgent();

                textViewSpinnerloginMode.setVisibility(View.GONE);
                spinnerloginMode.setVisibility(View.GONE);
                linearlayoutLoginAuthenticationType.setVisibility(View.GONE);

            }
        } else if (id == R.id.spinnerloginMode) {
            str_login_mode = spinnerloginMode.getSelectedItem().toString();
            setBlankValueToEdittext();
            setBlankValuesToString();
            linearlayoutLoginAuthenticationType.setVisibility(View.VISIBLE);
            spinnerloginAuthenticationType.setSelection(0);
            tableRowLoginPIN.setVisibility(View.GONE);
            tableRowLoginOTP.setVisibility(View.GONE);
            if (position == 0) {
                tableRowLoginEmailId.setVisibility(View.GONE);
                tableRowLoginMobileNo.setVisibility(View.GONE);
                tableRowLoginPIN.setVisibility(View.GONE);
                tableRowLoginCIF.setVisibility(View.GONE);
                tableRowLoginBDMUM.setVisibility(View.GONE);
                linearlayoutLoginAuthenticationType.setVisibility(View.GONE);
            }
            if (position == 1) {
                if (strType.equalsIgnoreCase("BDM") || strType.equalsIgnoreCase("UM")) {
                    tableRowLoginBDMUM.setVisibility(View.VISIBLE);
                    tableRowLoginCIF.setVisibility(View.GONE);
                } else {
                    tableRowLoginBDMUM.setVisibility(View.GONE);
                    tableRowLoginCIF.setVisibility(View.VISIBLE);
                }
                tableRowLoginEmailId.setVisibility(View.GONE);
                tableRowLoginMobileNo.setVisibility(View.GONE);
            } else if (position == 2) {

                tableRowLoginEmailId.setVisibility(View.VISIBLE);
                tableRowLoginCIF.setVisibility(View.GONE);
                tableRowLoginMobileNo.setVisibility(View.GONE);
                tableRowLoginBDMUM.setVisibility(View.GONE);
            } else if (position == 3) {

                tableRowLoginMobileNo.setVisibility(View.VISIBLE);
                tableRowLoginCIF.setVisibility(View.GONE);
                tableRowLoginEmailId.setVisibility(View.GONE);
                tableRowLoginBDMUM.setVisibility(View.GONE);

            } /*else if (position == 4) {

				tableRowLoginMobileNo.setVisibility(View.VISIBLE);
				tableRowLoginPIN.setVisibility(View.VISIBLE);
				tableRowLoginCIF.setVisibility(View.GONE);
				tableRowLoginEmailId.setVisibility(View.GONE);
			}*/
        } else if (id == R.id.spinnerloginAuthenticationType) {
            strAuthenticationType = spinnerloginAuthenticationType.getSelectedItem().toString();
            strPIN = "";
            editTextLoginPIN.setText("");
            tableRowLoginOTP.setVisibility(View.GONE);

            if (strAuthenticationType.equalsIgnoreCase("OTP")) {
                tableRowLoginPIN.setVisibility(View.GONE);
                buttonLogin.setVisibility(View.VISIBLE);
                buttonLogin.setText("Generate OTP");
                buttonVerifyOTP.setVisibility(View.GONE);

                getCaptcha();

            } else if (strAuthenticationType.equalsIgnoreCase("PIN")) {
                tableRowLoginPIN.setVisibility(View.VISIBLE);
                buttonLogin.setVisibility(View.VISIBLE);
                buttonLogin.setText("Login");
                buttonVerifyOTP.setVisibility(View.GONE);
                editTextLoginPIN.setFocusable(true);
                editTextLoginPIN.requestFocus();

                getCaptcha();
            } else {
                tableRowLoginPIN.setVisibility(View.GONE);
                buttonLogin.setVisibility(View.VISIBLE);

                buttonLogin.setText("Login");
                buttonVerifyOTP.setVisibility(View.GONE);
                strAuthenticationType = "";

                llCaptcha.setVisibility(View.GONE);
                edittextCaptcha.setText("");
            }
        }

    }

    private void setBlankValueToEdittext() {
        edittextBDMUMCode.setText("");
        editTextCIFAgentCode.setText("");
        edittextLoginOtp.setText("");
        editTextLoginEmailId.setText("");
        editTextMobileNumber.setText("");
        editTextLoginPIN.setText("");

        editTextMobileNumber.setError(null);
        editTextLoginEmailId.setError(null);
    }

    private void setBlankValuesToString() {
        strID = "";
        strEmail = "";
        strMobile = "";
        strPIN = "";
        strBDMUMCode = "";
        strOtp = "";
        strAuthenticationType = "";

    }

    private void hideAllRowsForCIFAgent() {
        tableRowLoginEmailId.setVisibility(View.GONE);
        tableRowLoginMobileNo.setVisibility(View.GONE);
        tableRowLoginPIN.setVisibility(View.GONE);
        tableRowLoginCIF.setVisibility(View.GONE);
        tableRowLoginBDMUM.setVisibility(View.GONE);
        tableRowLoginOTP.setVisibility(View.GONE);
        checkBoxLoginAsBDMOrCIF.setVisibility(View.GONE);

        textViewSpinnerloginMode.setVisibility(View.VISIBLE);
        spinnerloginMode.setVisibility(View.VISIBLE);

        buttonLogin.setVisibility(View.VISIBLE);
        buttonLogin.setText("Login");
        buttonVerifyOTP.setVisibility(View.GONE);

        checkBoxLoginAsBDMOrCIF.setChecked(false);
    }

    private void hideAllRowsForBDMUM() {
        tableRowLoginEmailId.setVisibility(View.GONE);
        tableRowLoginMobileNo.setVisibility(View.GONE);
        tableRowLoginPIN.setVisibility(View.GONE);
        tableRowLoginCIF.setVisibility(View.VISIBLE);
        tableRowLoginBDMUM.setVisibility(View.VISIBLE);
        tableRowLoginOTP.setVisibility(View.GONE);


        buttonLogin.setVisibility(View.GONE);
        checkBoxLoginAsBDMOrCIF.setChecked(false);

        if (isLoginAsCIF) {
            buttonVerifyOTP.setVisibility(View.GONE);
            buttonLogin.setVisibility(View.VISIBLE);
            buttonLogin.setText("Generate OTP");
            tableRowLoginCIF.setVisibility(View.VISIBLE);
        } else {
            buttonVerifyOTP.setVisibility(View.GONE);
            buttonLogin.setVisibility(View.VISIBLE);
            buttonLogin.setText("Login");
            tableRowLoginCIF.setVisibility(View.GONE);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    @SuppressWarnings("deprecation")
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
                        if (taskAsyncValidateUser != null) {
                            taskAsyncValidateUser.cancel(true);
                        }
                        if (checkAppVesrion != null) {
                            checkAppVesrion.cancel(true);
                        }

                        progressDialog.dismiss();
                    }
                });

                progressDialog.setMax(100);
                progressDialog.show();
                return progressDialog;

            default:
                return null;
        }
    }

    private void setCIFLoginSpinner() {
        String[] planList = {"Select User Id", "CIF Code", "Email-Id", "Mobile Number"};

        ArrayAdapter<String> planAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, planList);
        planAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerloginMode.setAdapter(planAdapter);
        planAdapter.notifyDataSetChanged();
        spinnerloginMode.setOnItemSelectedListener(this);

        //editTextCIFAgentCode.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    private void setAgentLoginSpinner() {
        String[] planList = {"Select User Id", "Agent Code", "Email-Id", "Mobile Number"};

        ArrayAdapter<String> planAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, planList);
        planAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerloginMode.setAdapter(planAdapter);
        planAdapter.notifyDataSetChanged();
        spinnerloginMode.setOnItemSelectedListener(this);
        // editTextCIFAgentCode.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
    }

    private void setBDMLoginSpinner() {
        String[] planList = {"Select User Id", "BDM Code", "Email-Id", "Mobile Number"};

        ArrayAdapter<String> planAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, planList);
        planAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerloginMode.setAdapter(planAdapter);
        planAdapter.notifyDataSetChanged();
        spinnerloginMode.setOnItemSelectedListener(this);
        // editTextCIFAgentCode.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
    }

    private void setUMLoginSpinner() {
        String[] planList = {"Select User Id", "UM Code", "Email-Id", "Mobile Number"};

        ArrayAdapter<String> planAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, planList);
        planAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerloginMode.setAdapter(planAdapter);
        planAdapter.notifyDataSetChanged();
        spinnerloginMode.setOnItemSelectedListener(this);
    }

    private void setChannelReportsSpinner(String userCode) {
        String[] planList = {"Select User Id", userCode, "Email-Id", "Mobile Number"};

        ArrayAdapter<String> planAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, planList);
        planAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerloginMode.setAdapter(planAdapter);
        planAdapter.notifyDataSetChanged();
        spinnerloginMode.setOnItemSelectedListener(this);
    }

    public void setFocusable(View v) {
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
    }

    @Override
    public void onBackPressed() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_yes_no);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button bt_yes = dialog.findViewById(R.id.bt_yes);
        Button bt_no = dialog.findViewById(R.id.bt_no);
        ((TextView) dialog.findViewById(R.id.tv_title)).setText("Are you sure you want to exit?");
        bt_yes.setText("Yes");
        bt_no.setText("No");
        bt_yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
        bt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void VersionExpiredAlert() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        dialog.setCancelable(false);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("This Version of Smart Advisor has expired.please open google play store, log in and update Smart Advisor.");

        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            @SuppressWarnings("deprecation")
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

                //hit service
                service_hits();
            }
        });
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        killTask();
        super.onDestroy();
    }

    private void killTask() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (taskAsyncValidateUser != null) {
            taskAsyncValidateUser.cancel(true);
        }

        if (checkAppVesrion != null) {
            checkAppVesrion.cancel(true);
        }
        if (service != null) {
            service.cancel(true);
        }
        if (mAsyncFillCaptcha != null)
            mAsyncFillCaptcha.cancel(true);
    }

    //for Agent/BAP/CAG/IMF
    public void setBAP_CAG_IMF_Spinner() {
        String[] planList = {"Select User Id", "SP Code", "Email-Id", "Mobile Number"};

        ArrayAdapter<String> planAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, planList);
        planAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerloginMode.setAdapter(planAdapter);
        planAdapter.notifyDataSetChanged();
        spinnerloginMode.setOnItemSelectedListener(this);
    }

    private void getCaptcha() {

        String strUserCode = "";

        str_login_mode = spinnerloginMode.getSelectedItem().toString();
        strID = editTextCIFAgentCode.getText().toString();
        strBDMUMCode = edittextBDMUMCode.getText().toString();
        strEmail = editTextLoginEmailId.getText().toString();
        strMobile = editTextMobileNumber.getText().toString();

        if (str_login_mode.equalsIgnoreCase("Email-Id")) {
            if (!strEmail.equalsIgnoreCase("")) {
                strUserCode = strEmail;
            }
        } else if (str_login_mode.equalsIgnoreCase("Mobile Number")) {
            if (!strMobile.equalsIgnoreCase("")) {
                strUserCode = strMobile;
            }
        } else {

            if ((strType.equalsIgnoreCase("BDM") && !isLoginAsCIF) || strType.equalsIgnoreCase("UM")) {//strType.equalsIgnoreCase("Agent") ||
                if (!strBDMUMCode.equalsIgnoreCase(""))
                    strUserCode = strBDMUMCode;
            } else {
                if (!strID.equalsIgnoreCase(""))
                    strUserCode = strID;
            }
        }

        if (!strUserCode.equalsIgnoreCase("")) {
            mAsyncFillCaptcha = new AsyncFillCaptcha();
            mAsyncFillCaptcha.execute(strUserCode);
        } else {
            mCommonMethods.showMessageDialog(context, "Please select login mode");
        }
    }

    private void validateCaptcha() {

        str_login_mode = spinnerloginMode.getSelectedItem().toString();
        strID = editTextCIFAgentCode.getText().toString();
        strBDMUMCode = edittextBDMUMCode.getText().toString();
        strEmail = editTextLoginEmailId.getText().toString();
        strMobile = editTextMobileNumber.getText().toString();
        String strUserCode = "";

        if (str_login_mode.equalsIgnoreCase("Email-Id")) {
            if (!strEmail.equalsIgnoreCase("")) {
                strUserCode = strEmail;
            }
        } else if (str_login_mode.equalsIgnoreCase("Mobile Number")) {
            if (!strMobile.equalsIgnoreCase("")) {
                strUserCode = strMobile;
            }
        } else {

            if ((strType.equalsIgnoreCase("BDM") && !isLoginAsCIF) || strType.equalsIgnoreCase("UM")) {//strType.equalsIgnoreCase("Agent") ||
                if (!strBDMUMCode.equalsIgnoreCase(""))
                    strUserCode = strBDMUMCode;
            } else {
                if (!strID.equalsIgnoreCase(""))
                    strUserCode = strID;
            }
        }

        if (!strUserCode.equalsIgnoreCase("")) {
            String captchaEntered = edittextCaptcha.getText().toString();
            captchaEntered = captchaEntered == null ? "" : captchaEntered;
            if (!captchaEntered.equalsIgnoreCase("")) {
                startDownloadLoginSmrt();
            } else {
                mCommonMethods.showMessageDialog(context, "Please enter captcha");
            }
        } else {
            mCommonMethods.showMessageDialog(context, "Please select login mode");
        }


    }

    class DownloadFileAsyncValidateUser extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String inputpolicylist = "", strAuthUserErrorCOde = "";
        private String strTitle = "", strFame = "", strLName = "",
                strEmailId = "", strMobilNo = "", strDOB = "", strAddress = "", strStatus = "",
                captchaEntered;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
            captchaEntered = edittextCaptcha.getText().toString();
            captchaEntered = captchaEntered == null ? "" : captchaEntered;
            if (TextUtils.isEmpty(captchaEntered)) {
                return;
            }
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_LOGIN_SMRT);
                //string strCode, string strOther, string strPIN, string strOTP, string strType)

                if ((strType.equalsIgnoreCase("BDM") && !isLoginAsCIF) || strType.equalsIgnoreCase("UM")) {//strType.equalsIgnoreCase("Agent") ||
                    request.addProperty("strCode", strBDMUMCode);
                } else {
                    request.addProperty("strCode", strID);
                }

                request.addProperty("strOther", strOther);
                request.addProperty("strPIN", mCommonMethods.encryptData(strPIN));
                request.addProperty("strOTP", strOtp);
                if (isLoginAsCIF && strType.equalsIgnoreCase("CIF")) {
                    request.addProperty("strType", "CIF");
                } else {
                    request.addProperty("strType", strType);
                }
                request.addProperty("strAuthKey", mCommonMethods.getEncryptedAuthKey());
                request.addProperty("strCapcha", captchaEntered);
                System.out.println("request.toString() = " + request.toString());
                mCommonMethods.TLSv12Enable();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);


                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl, 50000);

                String SOAP_ACTION_LOGIN_SMRT = "http://tempuri.org/LoginSMRT_smrt";
                androidHttpTranport.call(SOAP_ACTION_LOGIN_SMRT, envelope);

                SoapPrimitive sa;
                sa = (SoapPrimitive) envelope.getResponse();
                inputpolicylist = sa.toString();
                System.out.println("request.toString() = 1" + inputpolicylist);
                if (!inputpolicylist.equalsIgnoreCase("1") && !inputpolicylist.equalsIgnoreCase("0")) {
                    strAuthUserErrorCOde = inputpolicylist;

                    ParseXML prsObj = new ParseXML();

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
                        //AGENT,BDM,UM-<USER_ID>
                        if (strType.equalsIgnoreCase("CIF")) {
                            strID = prsObj.parseXmlTag(inputpolicylist, "CI_ID");
                        } else {
                            strID = prsObj.parseXmlTag(inputpolicylist, "USER_ID");
                        }


                        strTitle = prsObj.parseXmlTag(inputpolicylist, "FAC_TITLE");
                        strFame = prsObj.parseXmlTag(inputpolicylist, "FAC_FIRST_NM");
                        strLName = prsObj.parseXmlTag(inputpolicylist, "FAC_LAST_NM");
                        strEmailId = prsObj.parseXmlTag(inputpolicylist, "FAC_EMAIL");
                        strMobilNo = prsObj.parseXmlTag(inputpolicylist, "FAC_MOBILE");
                        strDOB = prsObj.parseXmlTag(inputpolicylist, "FAC_DOB");
                        strPIN = prsObj.parseXmlTag(inputpolicylist, "PIN_NO");
                        strStatus = prsObj.parseXmlTag(inputpolicylist, "APPROVED_STATUS");

                        strAddress = prsObj.parseXmlTag(inputpolicylist, "FAC_ADDR1");

                        strType = prsObj.parseXmlTag(inputpolicylist, "FAC_TYPE");
                        if (strDOB == null) {
                            strDOB = "";
                        } else {
                            strDOB = strDOB.split("T")[0];
                            strDOB = mCommonMethods.getFormattedDate(strDOB);
                        }

                        strOtp = "";
                        strAuthUserErrorCOde = "success";
                    } else {
                        strAuthUserErrorCOde = "Failure";
                    }

                } else if (inputpolicylist.equalsIgnoreCase("1")) {
                    strAuthUserErrorCOde = inputpolicylist;
                } else {
                    strAuthUserErrorCOde = "";
                }
            } catch (Exception e) {
                progressDialog.dismiss();
                running = false;
                e.printStackTrace();
                System.out.println("request.toString() = 2" + inputpolicylist);
            }
            return null;

        }


        @Override
        protected void onPostExecute(String unused) {
            if (progressDialog.isShowing()) {
                dismissProgressDialog();
            }

            if (running) {
                if (strAuthUserErrorCOde.equals("1")) {

                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.loading_window);
                    dialog.setCancelable(false);
                    TextView text = dialog.findViewById(R.id.txtalertheader);
                    text.setText("OTP sent successfully");

                    Button dialogButton = dialog.findViewById(R.id.btnalert);
                    dialogButton.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            dialog.dismiss();
                            tableRowLoginOTP.setVisibility(View.VISIBLE);
                            buttonLogin.setVisibility(View.GONE);
                            buttonVerifyOTP.setVisibility(View.VISIBLE);
                        }
                    });
                    dialog.show();
                } else if (strAuthUserErrorCOde.equalsIgnoreCase("success")) {

                    boolean ok = true;
                    try {

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
                        ex.printStackTrace();
                    } finally {
                        if (ok) {
                            //check version 12-05-2017
                            if (mCommonMethods.isNetworkConnected(context)) {
                                checkAppVesrion = new CheckAppVesrion();
                                checkAppVesrion.execute(strEmailId, strMobilNo);
                            } else {
                                mCommonMethods.showMessageDialog(context, "Please turn on your internet connection");
                            }
                        }
                    }

                } else {

					/*if(strAuthUserErrorCOde.equalsIgnoreCase("0") ||strAuthUserErrorCOde.equalsIgnoreCase("failure")){
						buttonLogin.setVisibility(View.VISIBLE);
						buttonVerifyOTP.setVisibility(View.GONE);
						tableRowLoginOTP.setVisibility(View.GONE);
					}
					else{
						buttonLogin.setVisibility(View.GONE);
						buttonVerifyOTP.setVisibility(View.VISIBLE);
						tableRowLoginOTP.setVisibility(View.GONE);
					}*/
                    buttonLogin.setVisibility(View.VISIBLE);
                    buttonVerifyOTP.setVisibility(View.GONE);
                    tableRowLoginOTP.setVisibility(View.GONE);

                    //tableRowLoginOTP.setVisibility(View.GONE);
                    mCommonMethods.showMessageDialog(context, mCommonMethods.UNAUTHORISED_USER_ALERT);
                    getCaptcha();
                }
            } else {

                buttonLogin.setVisibility(View.VISIBLE);
                buttonVerifyOTP.setVisibility(View.GONE);
                tableRowLoginOTP.setVisibility(View.GONE);
                mCommonMethods.showMessageDialog(context, mCommonMethods.SERVER_ERROR);
                getCaptcha();
            }
        }
    }

    class CheckAppVesrion extends AsyncTask<String, Void, String> {

        private volatile boolean running = true;
        private String str_Version_success = "0", outResponce = "",
                strVersionError = "", str_VersionNumber = "", str_Launch_date = "",
                str_no_of_valid_days = "", str_Launch_message = "";
        private int mDay = 0, mMonth = 0, mYear = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... data) {
            try {
                running = true;

                String METHOD_NAME_CHK_VERSION = "checkAppVersion";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_CHK_VERSION);

                request.addProperty("strAppName", context.getResources().getString(R.string.app_name));
                request.addProperty("strEmailId", data[0]);
                request.addProperty("strMobileNo", data[1]);
                /*request.addProperty("AuthKey", SimpleCrypto.encrypt("SBIL", "sbil").trim());*/
                request.addProperty("AuthKey", mCommonMethods.getStrAuth());
                System.out.println("request.toString() = " + request.toString());
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

                SoapPrimitive sa;
                sa = (SoapPrimitive) envelope.getResponse();

                outResponce = sa.toString();
                System.out.println("request.toString() = " + sa.toString());
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
                e.printStackTrace();
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
                    } catch (NameNotFoundException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                        current_version = 0;
                    }

                    if (Integer.parseInt(str_VersionNumber) != current_version && current_version < Integer.parseInt(str_VersionNumber)) {

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                        try {

                            Date d1, d2;
                            String dtStart = str_Launch_date;

                            Date Launch_date = sdf.parse(dtStart);

                            Calendar c = Calendar.getInstance();
                            c.setTime(Launch_date);
                            c.add(Calendar.DATE, +Integer.parseInt(str_no_of_valid_days));

                            d1 = sdf.parse(dtStart = sdf.format(c.getTime()));

                            String current_date = setDefaultDate();

                            d2 = sdf.parse(current_date);

                            if (d1.compareTo(d2) <= 0) {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "This Version of Smart Advisor has expired.please open google play store, log in and update Smart Advisor.",
                                        Toast.LENGTH_LONG).show();

                                VersionExpiredAlert();

                                //go out

                            } else {

                                long diff = d1.getTime() - d2.getTime();
                                long diffDays = diff / (24 * 60 * 60 * 1000);

                                if (diffDays > 5) {

                                    Toast.makeText(getApplicationContext(),
                                            str_Launch_message, Toast.LENGTH_LONG).show();

                                    //hit service
                                    service_hits();

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

                        service_hits();
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

        private String setDefaultDate() {
            Calendar present_date = Calendar.getInstance();
            present_date.add(Calendar.DAY_OF_MONTH, +0);
            mDay = present_date.get(Calendar.DAY_OF_MONTH);
            mMonth = present_date.get(Calendar.MONTH);
            mYear = present_date.get(Calendar.YEAR);

            return mDay + "-" + (mMonth + 1) +
                    "-" + mYear;
        }
    }

    class AsyncFillCaptcha extends AsyncTask<String, String, String> {
        private volatile boolean running = true;
        private String str_output_result = "";

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {

            if (mCommonMethods.isNetworkConnected(context)) {
                try {
                    running = true;

                    SoapObject request = new SoapObject(NAMESPACE, METHOD_FILL_CAPCHA);

                    request.addProperty("strCode", strings[0]);
                    request.addProperty("strAuthKey", mCommonMethods.getEncryptedAuthKey());
                    System.out.println("request.toString() = " + request.toString());
                    mCommonMethods.TLSv12Enable();

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);
                    envelope.dotNet = true;

                    mCommonMethods.TLSv12Enable();

                    envelope.setOutputSoapObject(request);

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);


                    HttpTransportSE androidHttpTranport = new HttpTransportSE(URl, 50000);
                    try {

                        androidHttpTranport.call(NAMESPACE + METHOD_FILL_CAPCHA, envelope);
                        Object response = envelope.getResponse();
                        str_output_result = response.toString();
                        // SoapPrimitive sa = (SoapPrimitive)
                        // envelope.getResponse();
                    } catch (Exception e) {
                        running = false;
                        return mCommonMethods.WEEK_INTERNET_MESSAGE;
                    }

                } catch (Exception e) {
                    running = false;
                    return mCommonMethods.WEEK_INTERNET_MESSAGE;
                }
            } else {
                return mCommonMethods.NO_INTERNET_MESSAGE;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            if (running) {
                running = false;

                if (str_output_result.equals("0")) {
                    Toast.makeText(context, "PLease try agian later..", Toast.LENGTH_SHORT).show();
                    spinnerloginAuthenticationType.setSelection(0);
                } else {
                    llCaptcha.setVisibility(View.VISIBLE);
                    edittextCaptcha.setText("");
                    tvCaptchaCode.setText(str_output_result);
                    tvCaptchaCode.setPaintFlags(tvCaptchaCode.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }

            } else {
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                spinnerloginAuthenticationType.setSelection(0);
            }
        }
    }
}
