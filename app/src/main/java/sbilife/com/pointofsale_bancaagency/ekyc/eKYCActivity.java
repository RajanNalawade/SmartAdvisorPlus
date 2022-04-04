package sbilife.com.pointofsale_bancaagency.ekyc;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.Html;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.ecs.pidgen.PIDGenerator;
import com.ecs.pidgen.PidData;
import com.ecs.pidgen.data.BioMetricType;
import com.ecs.pidgen.data.BiometricData;
import com.ecs.pidgen.data.BiometricPosition;
import com.ecs.rdlibrary.ECSBioCaptureActivity;
import com.morpho.android.usb.USBManager;
import com.morpho.morphosmart.sdk.CallbackMessage;
import com.morpho.morphosmart.sdk.Coder;
import com.morpho.morphosmart.sdk.CompressionAlgorithm;
import com.morpho.morphosmart.sdk.DetectionMode;
import com.morpho.morphosmart.sdk.EnrollmentType;
import com.morpho.morphosmart.sdk.ErrorCodes;
import com.morpho.morphosmart.sdk.LatentDetection;
import com.morpho.morphosmart.sdk.MorphoDevice;
import com.morpho.morphosmart.sdk.MorphoImage;
import com.morpho.morphosmart.sdk.MorphoWakeUpMode;
import com.morpho.morphosmart.sdk.Template;
import com.morpho.morphosmart.sdk.TemplateFVPType;
import com.morpho.morphosmart.sdk.TemplateList;
import com.morpho.morphosmart.sdk.TemplateType;
import com.sec.biometric.license.SecBiometricLicenseManager;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.stream.Verbosity;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.TimeZone;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.ekyc.ekycRequest.ECSKycRequest;
import sbilife.com.pointofsale_bancaagency.ekyc.ekycRequest.ECSOtpKycRequest;
import sbilife.com.pointofsale_bancaagency.ekyc.ekycRequest.ECSOtpRequest;
import sbilife.com.pointofsale_bancaagency.ekyc.ekycRequest.ECSRDKYCRequest;
import sbilife.com.pointofsale_bancaagency.ekyc.ekycResponse.ECSKycResponse;
import sbilife.com.pointofsale_bancaagency.ekyc.ekycResponse.ECSOtpResponse;
import sbilife.com.pointofsale_bancaagency.ekyc.info.CaptureInfo;
import sbilife.com.pointofsale_bancaagency.ekyc.info.MorphoInfo;
import sbilife.com.pointofsale_bancaagency.ekyc.info.ProcessInfo;
import sbilife.com.pointofsale_bancaagency.ekyc.request.CustOpts;
import sbilife.com.pointofsale_bancaagency.ekyc.request.Demo;
import sbilife.com.pointofsale_bancaagency.ekyc.request.Opts;
import sbilife.com.pointofsale_bancaagency.ekyc.request.Param;
import sbilife.com.pointofsale_bancaagency.ekyc.request.PidOptions;
import sbilife.com.pointofsale_bancaagency.ekyc.utilites.Global;
import sbilife.com.pointofsale_bancaagency.ekyc.utilites.HttpConnector;
import sbilife.com.pointofsale_bancaagency.ekyc.utilites.XMLUtilities;
import sbilife.com.pointofsale_bancaagency.home.CarouselHomeActivity;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

/*Class created by Machindra on 23-03-2017 for ekycUI and Biometric Capture 10 jan 2017*/

public class eKYCActivity extends AppCompatActivity implements OnItemClickListener,
        OnCheckedChangeListener, OnEditorActionListener,
        SearchView.OnQueryTextListener, Observer {
    /* For Fingerprint */

    private MorphoDevice morphoDevice;
    private String sensorName = "";
    private boolean isCaptureVerif = false;
    private Handler mHandler = new Handler();
    private String strMessage = "";
    private ImageView iv_ekyc_fingerprintcapture;
    Bitmap decodedByte;
    private String base64FingerTemplate = "";
    public static boolean isRebootSoft = false;
    private ProgressBar progressBar;
    private TextView textViewMessage;
    private String eSignAuth = "";
    private String deviceCode = "";
    String AuthXMLbase64 = "";
    private static HashMap<String, Boolean> mSupportedDevicelist;

    // private SecIrisManager secIrisManager;
    private X509Certificate uidaiCert;
    private int mRotation;
    private byte[] mBytesData;
    private boolean isCapturinginProgress = false;
    private TextView eyeuiInfo;
    Properties uidPrefs;
    private static SurfaceView preview;
    private int goodCount;
    private int tooFarCount;
    private int openWidelyCount;
    private int tooCloseCount;
    private int eyesNotDetected;
    int uidOp = 0;
    private static int openingCount;
    private SensorManager mSensorManager;

    private SensorEventListener proximitySensorEventListener;
    private OnCheckedChangeListener listener;
    private boolean isProtoBuf = false;
    private String mEyeDist;
    private String mEyeOpen;
    private boolean singleChecked;
    private SurfaceHolder holder;
    private SecBiometricLicenseManager mLicenseMgr;
    private LinearLayout ll_header_part;
    private TextView tvStatus;

    // Samsung SDK Specific Variables
    private static final String TAG = "eKYCActivity";
    private Canvas canvas = new Canvas();
    private String customer_name = "";
    private String customer_AadharNumber = "";
    private String customer_aadhar_name = "";
    private String ProposalNumber = "";

    private byte[] isoTemplate;
    private PidData pidData;
    private AlertDialog.Builder showAlert;
    private List<BiometricData> bios = new ArrayList<>();

    /* Database Class Declaration */
    private DatabaseHelper db = new DatabaseHelper(this);

    private static int DEVICE_CAPTURE_REQUEST_CODE = 1;
    private static int IRIS_CAPTURE_REQUEST_CODE = 2;
    private String pidDataXMLbase64 = "";

    private ProgressDialog mProgressDialog;
    private static final int DIALOG_PROGRESS = 3;

    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String URl = ServiceURL.SERVICE_URL;

    //Method name LinkAadharDtls changed to  LinkAadharDtls_SMRT
    private static final String SOAP_ACTION_LINK_AADHAR_DETAILS = "http://tempuri.org/LinkAadharDtls_SMRT";
    private static final String METHOD_NAME_LINK_AADHAR_DETAILS = "LinkAadharDtls_SMRT";

    private String resGenerateOTPString = "", str_OTP = "", str_otp_txn = "", str_biometric_type = "", str_from = "";

    private boolean isSuccessEkyc = false;
    private Date date1;
    private Context mContext;
    private CommonMethods mCommonMethods;
    private Asynch_link_aadhar_details mAsynch_link_aadhar_details;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ekyc_popup);

        mContext = this;
        mCommonMethods = new CommonMethods();

        showAlert = new AlertDialog.Builder(this);

        Intent i = getIntent();
        ProposalNumber = i.getStringExtra("ProposalNumber");
        customer_name = i.getStringExtra("CustomerName");
        customer_AadharNumber = i.getStringExtra("CustomerAadharNumber");
        customer_aadhar_name = i.getStringExtra("CustomerAadharName");
        str_from = i.getStringExtra("FROM") == null ? "" : i.getStringExtra("FROM"); // NA or NB or PS_CLAIM
        //customer_AadharNumber = "946667488536";//tushar
        //customer_AadharNumber = "949711038989";//rajan
        //customer_AadharNumber = "664926239602";//macchi

        mSupportedDevicelist = new HashMap<>();
        mSupportedDevicelist.put("SM-T116IR", true);

        date1 = new Date();
        mAsynch_link_aadhar_details = new Asynch_link_aadhar_details();

        eKYC_main();
    }

    /*@Override
    public void onBackPressed() {
        //super.onBackPressed();

        Intent i = new Intent(eKYCActivity.this, EkycPSClaimsActivity.class);
        i.putExtra("POLICY_NUM", "");
        startActivity(i);
    }*/

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_PROGRESS:
                mProgressDialog = new ProgressDialog(this,
                        ProgressDialog.THEME_HOLO_LIGHT);
                String Message = "Loading Please wait...";
                mProgressDialog.setMessage(Html
                        .fromHtml("<font color='#00a1e3'><b>" + Message
                                + "<b></font>"));
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;

            default:
                return null;
        }
    }

    private void eKYC_main() {

        // ll_layout_otp = (LinearLayout) d.findViewById(R.id.ll_layout_otp);
        // ll_layout_otp.setVisibility(View.VISIBLE);
        progressBar = findViewById(R.id.vertical_progressbar);
        //ll_progress_layout = (LinearLayout) findViewById(R.id.ll_progress_layout);
        textViewMessage = findViewById(R.id.textViewMessage);
        tvStatus = findViewById(R.id.textViewStatus);
        ll_header_part = findViewById(R.id.ll_header_part);
        final EditText edt_otp = findViewById(R.id.edt_otp);

        Button btn_submit = findViewById(R.id.btn_submit);
        Button btn_GenerateOTP = findViewById(R.id.btn_GenerateOTP);
        Button btn_cancel = findViewById(R.id.btn_cancel);

        final Button btn_ekyc_capture = findViewById(R.id.btn_ekyc_capture);
        final RadioButton rb_OTP = findViewById(R.id.rb_OTP);

        final RadioButton rb_finger_print = findViewById(R.id.rb_finger_print);
        final RadioButton rb_IRIS = findViewById(R.id.rb_IRIS);
        final RadioButton rb_single_iris = findViewById(R.id.rb_single_iris);
        final RadioButton rb_dual_iris = findViewById(R.id.rb_dual_iris);
        iv_ekyc_fingerprintcapture = findViewById(R.id.iv_ekyc_fingerprintcapture);
        final LinearLayout ll_OTP = findViewById(R.id.ll_OTP);
        final LinearLayout ll_Fingerprint = findViewById(R.id.ll_Fingerprint);
        final LinearLayout ll_IRIS = findViewById(R.id.ll_IRIS);
        final CheckBox checkBox = findViewById(R.id.checkBox);
        TextView tv_declaration_text = findViewById(R.id.tv_declaration_text);

        checkBox.setChecked(false);
        rb_finger_print.setChecked(false);
        rb_IRIS.setChecked(false);
        rb_OTP.setChecked(false);

        rb_finger_print.setClickable(false);
        rb_finger_print.setEnabled(false);

        rb_IRIS.setClickable(false);
        rb_IRIS.setEnabled(false);

        rb_OTP.setClickable(false);
        rb_OTP.setEnabled(false);

        iv_ekyc_fingerprintcapture.setImageBitmap(null);
        textViewMessage.setText("");
        progressBar.setProgress(0);

        eSignAuth = "";
        base64FingerTemplate = "";
        deviceCode = null;

        checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                // TODO Auto-generated method stub
                if (isChecked) {

                    rb_finger_print.setClickable(true);
                    rb_finger_print.setEnabled(true);

                    rb_IRIS.setClickable(true);
                    rb_IRIS.setEnabled(true);

                    rb_OTP.setClickable(true);
                    rb_OTP.setEnabled(true);

                } else if (!isChecked) {

                    rb_finger_print.setClickable(false);
                    rb_finger_print.setEnabled(false);

                    rb_IRIS.setClickable(false);
                    rb_IRIS.setEnabled(false);

                    rb_OTP.setClickable(false);
                    rb_OTP.setEnabled(false);

                    iv_ekyc_fingerprintcapture.setImageBitmap(null);
                    textViewMessage.setText("");
                    progressBar.setProgress(0);

                    eSignAuth = "";
                    base64FingerTemplate = "";
                    deviceCode = null;
                    ll_OTP.setVisibility(View.GONE);
                    ll_IRIS.setVisibility(View.GONE);
                    ll_Fingerprint.setVisibility(View.GONE);

                }
            }
        });

        ll_OTP.setVisibility(View.GONE);
        ll_Fingerprint.setVisibility(View.GONE);
        ll_IRIS.setVisibility(View.GONE);

		/* Consent for ekYC declaration */

        /*String str_declaration = "I, "
                + "<font color=\"blue\">"
                + customer_name
                + "</font>"
                + ", holder of Aadhar Number "
                + "<font color=\"blue\">"
                + customer_AadharNumber
                + "</font>"
                + " , hereby give my consent to SBI Life Insurance Co. Ltd. (“SBI Life”)  to obtain my Aadhaar number issued by UIDAI and PAN issued by the Income Tax department and provide voluntary consent to link my Aadhaar and PAN with all my SBI Life policies. I give my consent to obtain and use my Aadhaar Number, Name, Date of Birth, Biometric (Fingerprint/Iris) and my Aadhaar details to authenticate me with UIDAI as per the Aadhaar (Targeted Delivery of Financial and Other Subsidies, Benefits and Services) Act, 2016 and all other applicable laws. SBI Life has informed me that my Aadhaar details and identity information would only be used for authentication either through Yes/No authentication facility or e-KYC facility in accordance with applicable regulations. SBI Life has also informed me that my Aadhaar details and identity information will be used only for KYC purpose and for all service aspects related to SBI Life Insurance and my biometrics will not be stored /shared by SBI Life. I will not hold SBI Life or any of its officials responsible in case of any incorrect information provided by me. I further authorize SBI Life to use my mobile number for sending SMS alerts regarding this purpose.<br><br>"
                + "<b>Note:-</b> Please provide your consent by ticking the checkbox above to proceed further.";*/

        boolean isVirtualID = false;
        if (customer_AadharNumber.length() == 16)
            isVirtualID = true;

        String str_declaration = "I "
                + "<font color=\"blue\">" + customer_name + "</font>, holder of ";

        if (isVirtualID)
            str_declaration += "Virtual ID " + "<font color=\"blue\">" + customer_AadharNumber + "</font>"
                    +", hereby give my consent to SBI Life Insurance Co. Ltd. (“SBI Life”)  to obtain my Aadhaar number issued by UIDAI and PAN issued by the Income Tax department and provide my voluntary consent to link my Aadhaar and PAN with all my SBI Life policies. I give my consent to obtain and use my Virtual ID, Name, Date of Birth, Fingerprint/Iris and my Aadhaar details to authenticate me with UIDAI as per the Aadhaar (Targeted Delivery of Financial and Other Subsidies, Benefits and Services) Act, 2016 and all other applicable laws. SBI Life has informed me that my Virtual ID and identity information would only be used for authentication either through Yes/No authentication facility or e-KYC facility in accordance with applicable regulations. SBI Life has also informed me that my Virtual ID and identity information will be used only for KYC purpose and for all service aspects related to SBI Life and my biometrics will not be stored /shared by SBI Life. I will not hold SBI Life or any of its authorised officials responsible in case of any incorrect information provided by me. I further authorize SBI Life to use my mobile number for sending SMS alerts to me regarding this purpose.";
        else
            str_declaration += "Aadhaar number " + "<font color=\"blue\">" + customer_AadharNumber + "</font>"
                    +", hereby give my consent to SBI Life Insurance Co. Ltd. (“SBI Life”)  to obtain my Aadhaar number issued by UIDAI and PAN issued by the Income Tax department and provide my voluntary consent to link my Aadhaar and PAN with all my SBI Life policies. I give my consent to obtain and use my Aadhaar Number, Name, Date of Birth, Fingerprint/Iris and my Aadhaar details to authenticate me with UIDAI as per the Aadhaar (Targeted Delivery of Financial and Other Subsidies, Benefits and Services) Act, 2016 and all other applicable laws. SBI Life has informed me that my Aadhaar details and identity information would only be used for authentication either through Yes/No authentication facility or e-KYC facility in accordance with applicable regulations. SBI Life has also informed me that my Aadhaar details and identity information will be used only for KYC purpose and for all service aspects related to SBI Life and my biometrics will not be stored /shared by SBI Life. I will not hold SBI Life or any of its authorised officials responsible in case of any incorrect information provided by me. I further authorize SBI Life to use my mobile number for sending SMS alerts to me regarding this purpose.";

        /*+ "<font color=\"blue\">"
                + "SBI Life-"
                + planName
                + "</font>"
                + " and also informed that my biometric will not be stored/ shared and will be submitted to CIDR only for purpose of Authentication.";*/

        tv_declaration_text.setText(Html.fromHtml(str_declaration),
                BufferType.SPANNABLE);

        try{
            String devicename = android.os.Build.MODEL;
            if (mSupportedDevicelist.get(devicename) == null) {
                rb_IRIS.setVisibility(View.INVISIBLE);
            } else {
                mLicenseMgr = SecBiometricLicenseManager.getInstance(this);
                if (!hasPermission()) {
                    activateIrisLicense();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            rb_IRIS.setVisibility(View.INVISIBLE);
        }

        btn_GenerateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (checkBox.isChecked()) {

                    if (mCommonMethods.isNetworkConnected(mContext)) {
                        GenerateOTP();
                    } else {
                        Toast.makeText(eKYCActivity.this,
                                "Sorry,No Internet Connection !!",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    checkBox.requestFocus();
                    Toast.makeText(eKYCActivity.this,
                            "Please accept the consent first",
                            Toast.LENGTH_LONG).show();

                }

            }
        });
        // btn_GenerateOTP.setOnClickListener(new View.OnClickListener() {
        // @Override
        // public void onClick(View arg0) {
        //
        // if (checkBox.isChecked()) {
        //
        // if (mCommonMethods.isNetworkConnected(con)) {
        // GenerateOTP();
        // } else {
        // Toast.makeText(Integrate_serviceActivity.this,
        // "Sorry,No Internet Connection !!",
        // Toast.LENGTH_LONG).show();
        // }
        // } else {
        // checkBox.requestFocus();
        // Toast.makeText(Integrate_serviceActivity.this,
        // "Please accept the consent first",
        // Toast.LENGTH_LONG).show();
        //
        // }
        //
        // }
        // });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (checkBox.isChecked()) {
                    // if (rb_OTP.isChecked()) {
                    // str_OTP = edt_otp.getText().toString();
                    // if (str_OTP.equals("")) {
                    // Toast.makeText(MainActivity.this,
                    // "Please enter valid OTP ",
                    // Toast.LENGTH_SHORT).show();
                    // } else {
                    // ll_layout_otp.setVisibility(View.GONE);
                    // uploadHashPdf();
                    // }
                    // }
                    String EKYC_PS_CLAIMS_EKYC_MODE = "ekyc_mode";
                    if (rb_OTP.isChecked()) {
                        str_OTP = edt_otp.getText().toString();

                        //update table eKYC for PS and Claims

                        try{
                            ContentValues cv = new ContentValues();
                            cv.put(db.EKYC_PS_CLAIMS_EKYC_MODE, "OTP");

                            /*int i = db.update_eKYC_PS_Claims_Details(cv, new String[]{ProposalNumber, customer_AadharNumber});*/
                            int i = db.update_eKYC_PS_Claims_Details(cv, new String[]{ProposalNumber});
                            if (i == -2) {
                                Toast.makeText(mContext, "error while updating data", Toast.LENGTH_LONG).show();
                            } else {
                                if (str_OTP.equals("")) {
                                    Toast.makeText(eKYCActivity.this,
                                            "Please enter valid OTP ",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    onOTPSuccess();
                                }
                            }
                        }catch (Exception ex){
                            ex.printStackTrace();
                            Toast.makeText(mContext, "error while updating data", Toast.LENGTH_LONG).show();
                        }
                    } else if (rb_finger_print.isChecked()) {
                        // if (base64FingerTemplate.equals("")) {
                        // Toast.makeText(eKYCActivity.this,
                        // "Please capture fingerprint Image",
                        // Toast.LENGTH_SHORT).show();

                        //update table eKYC for PS and Claims

                        ContentValues cv = new ContentValues();
                        cv.put(db.EKYC_PS_CLAIMS_EKYC_MODE, "FingerPrint");

                        /*int i = db.update_eKYC_PS_Claims_Details(cv, new String[]{ProposalNumber, customer_AadharNumber});*/
                        int i = db.update_eKYC_PS_Claims_Details(cv, new String[]{ProposalNumber});
                        if (i == -2) {
                            Toast.makeText(mContext, "error while updating data", Toast.LENGTH_LONG).show();
                        } else {
                            startFPSRDCapture();
                        }

                        // OnFingerPrintCapture();

                        // } else {
                        // ll_header_part.setVisibility(View.GONE);
                        // ll_progress_layout.setVisibility(View.VISIBLE);
                        // onFingerPrintCaptureSuccess();
                        // // uploadHashPdf();
                        // }
                    } else if (rb_IRIS.isChecked()) {

                        //update table eKYC for PS and Claims
                        try{
                            ContentValues cv = new ContentValues();
                            cv.put(db.EKYC_PS_CLAIMS_EKYC_MODE, "IRIS");

                            /*int i = db.update_eKYC_PS_Claims_Details(cv, new String[]{ProposalNumber, customer_AadharNumber});*/
                            int i = db.update_eKYC_PS_Claims_Details(cv, new String[]{ProposalNumber});
                            if (i == -2) {
                                Toast.makeText(mContext, "error while updating data", Toast.LENGTH_LONG).show();
                            } else {
                                if (rb_single_iris.isChecked()
                                        || rb_dual_iris.isChecked()) {

                                    singleChecked = rb_single_iris.isChecked();
                                    StartRDIRISCapture();
                            /* Commented on 25-07-2017 for RD service */
                                    // mSensorManager
                                    // .registerListener(
                                    // proximitySensorEventListener,
                                    // mSensorManager
                                    // .getDefaultSensor(Sensor.TYPE_PROXIMITY),
                                    // SensorManager.SENSOR_DELAY_NORMAL);
                                    // startCapture();
							/* End */

                                } else {
                                    clearFocusable(rb_single_iris);
                                    clearFocusable(rb_dual_iris);
                                    Toast.makeText(eKYCActivity.this,
                                            "Please select capture type ",
                                            Toast.LENGTH_SHORT).show();
                                    // uploadHashPdf();
                                }
                            }
                        }catch (Exception ex){
                            ex.printStackTrace();
                            Toast.makeText(mContext, "error while updating data", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // rb_OTP.requestFocus();
                        Toast.makeText(eKYCActivity.this,
                                "Please select e-Sign option first",
                                Toast.LENGTH_LONG).show();
                    }

                } else {
                    // checkBox.requestFocus();
                    // clearFocusable(rb_OTP);
                    // clearFocusable(rb_finger_print);
                    // clearFocusable(rb_IRIS);
                    // clearFocusable(rb_single_iris);
                    // clearFocusable(rb_dual_iris);
                    Toast.makeText(eKYCActivity.this,
                            "Please accept consent first", Toast.LENGTH_LONG)
                            .show();
                }

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                onBackPressed();

            }
        });

        btn_ekyc_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                try {

                    morphoDevice = new MorphoDevice();

                    USBManager.getInstance().initialize(eKYCActivity.this,
                            "com.morpho.morphosample.USB_ACTION");

                    if (USBManager.getInstance().isDevicesHasPermission()) {
                        btn_ekyc_capture.setEnabled(true);

                        if (base64FingerTemplate != null) {

                            CaptureInfo.getInstance().setIDNumber(
                                    customer_AadharNumber);

                            Integer nbUsbDevice = 0;

                            int ret = morphoDevice
                                    .initUsbDevicesNameEnum(nbUsbDevice);

                            if (ret == ErrorCodes.MORPHO_OK) {

                                if (nbUsbDevice > 0) {
                                    sensorName = morphoDevice
                                            .getUsbDeviceName(0);

                                    String[] getUsbDeviceName = morphoDevice
                                            .getUsbDeviceName(0).split("-");
                                    deviceCode = getUsbDeviceName[1];

                                    // Toast.makeText(this, sensorName,
                                    // Toast.LENGTH_SHORT).show();

                                    if (connection()) {

                                        ProcessInfo.getInstance()
                                                .setMSOSerialNumber(sensorName);
                                        String productDescriptor = morphoDevice
                                                .getProductDescriptor();
                                        java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(
                                                productDescriptor, "\n");
                                        if (tokenizer.hasMoreTokens()) {
                                            String l_s_current = tokenizer
                                                    .nextToken();
                                            if (l_s_current
                                                    .contains("FINGER VP")
                                                    || l_s_current
                                                    .contains("FVP")) {
                                                MorphoInfo.m_b_fvp = true;
                                            }
                                        }

                                        try {

                                            int ret1 = morphoDevice
                                                    .openUsbDevice(sensorName,
                                                            0);
                                            if (ret1 == ErrorCodes.MORPHO_OK)

                                            {

                                                ProcessInfo.getInstance()
                                                        .setMorphoDevice(
                                                                morphoDevice);

                                                MorphoInfo info = CaptureInfo
                                                        .getInstance();
                                                if (info != null) {
                                                    ProcessInfo
                                                            .getInstance()
                                                            .setMorphoInfo(info);
                                                    ProcessInfo
                                                            .getInstance()
                                                            .seteKYCActivity(eKYCActivity.this);
                                                    try {
                                                        ProcessInfo
                                                                .getInstance()
                                                                .setCommandBioStart(
                                                                        true);
                                                        ProcessInfo
                                                                .getInstance()
                                                                .setStarted(
                                                                        true);
                                                        // lockScreenOrientation();
                                                        // Capture();
                                                        startFPSRDCapture();
                                                        // morphoDeviceCapture(this);
                                                    } catch (Exception ignored) {
                                                    }

                                                } else {

                                                    Toast.makeText(mContext,
                                                            "Info is Null",
                                                            Toast.LENGTH_SHORT)
                                                            .show();
                                                }

                                            } else {
                                                finish();
                                            }
                                            // }

                                        } catch (Exception e) {

                                            Log.e(TAG,
                                                    e.toString()
                                                            + " error<-- during capturing FingerPrint");

                                        }

                                    } else {
                                        final AlertDialog alertDialog = new AlertDialog.Builder(
                                                mContext).create();
                                        alertDialog.setTitle(mContext
                                                .getResources().getString(
                                                        R.string.morphosample));
                                        alertDialog.setMessage(ErrorCodes
                                                .getError(ret, morphoDevice
                                                        .getInternalError()));
                                        alertDialog.setCancelable(false);
                                        alertDialog
                                                .setButton(
                                                        DialogInterface.BUTTON_POSITIVE,
                                                        mContext.getResources()
                                                                .getString(
                                                                        R.string.ok),
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(
                                                                    DialogInterface dialog,
                                                                    int which) {
                                                                finish();
                                                            }
                                                        });
                                        alertDialog.show();
                                    }

                                } else {
                                    final AlertDialog alertDialog = new AlertDialog.Builder(
                                            eKYCActivity.this).create();
                                    alertDialog.setTitle(eKYCActivity.this
                                            .getResources().getString(
                                                    R.string.morphosample));
                                    alertDialog
                                            .setMessage("The device is not detected");
                                    alertDialog.setCancelable(false);
                                    alertDialog
                                            .setButton(
                                                    DialogInterface.BUTTON_POSITIVE,
                                                    eKYCActivity.this
                                                            .getResources()
                                                            .getString(
                                                                    R.string.ok),
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int which) {
                                                            // finish();
                                                        }
                                                    });
                                    alertDialog.show();
                                }
                            } else {
                                final AlertDialog alertDialog = new AlertDialog.Builder(
                                        mContext).create();
                                alertDialog.setTitle(eKYCActivity.this
                                        .getResources().getString(
                                                R.string.morphosample));
                                alertDialog.setMessage(ErrorCodes.getError(ret,
                                        morphoDevice.getInternalError()));
                                alertDialog.setCancelable(false);
                                alertDialog.setButton(
                                        DialogInterface.BUTTON_POSITIVE,
                                        mContext.getResources().getString(
                                                R.string.ok),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                // finish();
                                            }
                                        });
                                alertDialog.show();
                            }
                        } else {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                    mContext);
                            alertDialog.setNeutralButton("OK", null);
                            alertDialog
                                    .setMessage("Please ensure all data are filled");
                            alertDialog.show();
                        }
                    }

                    // else {
                    // AlertDialog.Builder alertDialog = new
                    // AlertDialog.Builder(
                    // context);
                    // alertDialog.setNeutralButton("OK", null);
                    // alertDialog
                    // .setMessage("Device does not have permission for USB");
                    // alertDialog.show();
                    // }

                } catch (Exception e) {

                    if (AppConstants.DEBUG)
                        Log.e(TAG, e.toString()
                                + " error<-- in getting FingerPrint");
                    Toast.makeText(getApplicationContext(), e.toString(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        rb_OTP.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub

                if (isChecked) {
                    if (checkBox.isChecked()) {
                        // rb_OTP.setChecked(true);
                        iv_ekyc_fingerprintcapture.setImageBitmap(null);
                        textViewMessage.setText("");
                        progressBar.setProgress(0);

                        eSignAuth = "OTP";
                        base64FingerTemplate = "";
                        deviceCode = null;
                        ll_OTP.setVisibility(View.VISIBLE);
                        ll_IRIS.setVisibility(View.GONE);
                        ll_Fingerprint.setVisibility(View.GONE);

                    } else {
                        // clearFocusable(rb_OTP);
                        // checkBox.requestFocus();
                        Toast.makeText(eKYCActivity.this,
                                "Please accept the consent first",
                                Toast.LENGTH_LONG).show();

                    }

                }
            }
        });

        rb_finger_print
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {

                            if (checkBox.isChecked()) {
                                eSignAuth = "BIOMETRIC";
                                base64FingerTemplate = "";
                                ll_OTP.setVisibility(View.GONE);
                                ll_IRIS.setVisibility(View.GONE);
                                ll_Fingerprint.setVisibility(View.GONE);
                                // rb_finger_print.setChecked(true);

                            } else {
                                // clearFocusable(rb_finger_print);
                                // checkBox.requestFocus();
                                Toast.makeText(eKYCActivity.this,
                                        "Please accept the consent first",
                                        Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                });

        rb_IRIS.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {

                    if (checkBox.isChecked()) {
                        // rb_IRIS.setChecked(true);
                        iv_ekyc_fingerprintcapture.setImageBitmap(null);
                        textViewMessage.setText("");
                        progressBar.setProgress(0);

                        base64FingerTemplate = "";
                        eSignAuth = "IRIS";

                        // secIrisManager = SecIrisManager.getInstance();
                        // mSensorManager = (SensorManager)
                        // getSystemService(SENSOR_SERVICE);

                        ll_OTP.setVisibility(View.GONE);

                        ll_Fingerprint.setVisibility(View.GONE);
                        ll_IRIS.setVisibility(View.VISIBLE);

                    } else {
                        // clearFocusable(rb_IRIS);
                        // checkBox.requestFocus();
                        Toast.makeText(eKYCActivity.this,
                                "Please accept the consent first",
                                Toast.LENGTH_LONG).show();

                    }
                }
            }
        });

        // proximitySensorEventListener = new SensorEventListener() {
        // @Override
        // public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // // TODO Auto-generated method stub
        // }
        //
        // @Override
        // public void onSensorChanged(SensorEvent event) {
        // // TODO Auto-generated method stub
        // Log.i(TAG, " values   " + event.values[0]);
        // if (event.values[0] == 0.0) {
        // Log.i(TAG, "calling stop cature ");
        // stopCapture();
        // } else {
        // Log.i(TAG, "calling startCapture ");
        // startCapture();
        // }
        // }
        // };
    }

    public void OnFingerPrintCapture() {
        try {

            morphoDevice = new MorphoDevice();

            USBManager.getInstance().initialize(eKYCActivity.this,
                    "com.morpho.morphosample.USB_ACTION");

            if (USBManager.getInstance().isDevicesHasPermission()) {

                if (base64FingerTemplate != null) {

                    CaptureInfo.getInstance()
                            .setIDNumber(customer_AadharNumber);

                    Integer nbUsbDevice = 0;

                    int ret = morphoDevice.initUsbDevicesNameEnum(nbUsbDevice);

                    if (ret == ErrorCodes.MORPHO_OK) {

                        if (nbUsbDevice > 0) {
                            sensorName = morphoDevice.getUsbDeviceName(0);

                            String[] getUsbDeviceName = morphoDevice
                                    .getUsbDeviceName(0).split("-");
                            deviceCode = getUsbDeviceName[1];

                            // Toast.makeText(this, sensorName,
                            // Toast.LENGTH_SHORT).show();

                            if (connection()) {

                                ProcessInfo.getInstance().setMSOSerialNumber(
                                        sensorName);
                                String productDescriptor = morphoDevice
                                        .getProductDescriptor();
                                java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(
                                        productDescriptor, "\n");
                                if (tokenizer.hasMoreTokens()) {
                                    String l_s_current = tokenizer.nextToken();
                                    if (l_s_current.contains("FINGER VP")
                                            || l_s_current.contains("FVP")) {
                                        MorphoInfo.m_b_fvp = true;
                                    }
                                }

                                try {

                                    int ret1 = morphoDevice.openUsbDevice(
                                            sensorName, 0);
                                    if (ret1 == ErrorCodes.MORPHO_OK)

                                    {

                                        ProcessInfo.getInstance()
                                                .setMorphoDevice(morphoDevice);

                                        MorphoInfo info = CaptureInfo
                                                .getInstance();
                                        if (info != null) {
                                            ProcessInfo.getInstance()
                                                    .setMorphoInfo(info);
                                            ProcessInfo.getInstance()
                                                    .seteKYCActivity(eKYCActivity.this);
                                            try {
                                                ProcessInfo.getInstance()
                                                        .setCommandBioStart(
                                                                true);
                                                ProcessInfo.getInstance()
                                                        .setStarted(true);
                                                // lockScreenOrientation();
                                                // Capture();
                                                startFPSRDCapture();
                                                // morphoDeviceCapture(this);
                                            } catch (Exception ignored) {
                                            }

                                        } else {

                                            Toast.makeText(mContext,
                                                    "Info is Null",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        finish();
                                    }
                                    // }

                                } catch (Exception e) {

                                    Log.e(TAG,
                                            e.toString()
                                                    + " error<-- during capturing FingerPrint");

                                }

                            } else {
                                final AlertDialog alertDialog = new AlertDialog.Builder(
                                        mContext).create();
                                alertDialog.setTitle(mContext.getResources()
                                        .getString(R.string.morphosample));
                                alertDialog.setMessage(ErrorCodes.getError(ret,
                                        morphoDevice.getInternalError()));
                                alertDialog.setCancelable(false);
                                alertDialog.setButton(
                                        DialogInterface.BUTTON_POSITIVE,
                                        mContext.getResources().getString(
                                                R.string.ok),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                finish();
                                            }
                                        });
                                alertDialog.show();
                            }

                        } else {
                            final AlertDialog alertDialog = new AlertDialog.Builder(
                                    eKYCActivity.this).create();
                            alertDialog.setTitle(eKYCActivity.this
                                    .getResources().getString(
                                            R.string.morphosample));
                            alertDialog
                                    .setMessage("The device is not detected");
                            alertDialog.setCancelable(false);
                            alertDialog.setButton(
                                    DialogInterface.BUTTON_POSITIVE,
                                    eKYCActivity.this.getResources().getString(
                                            R.string.ok),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            // finish();
                                        }
                                    });
                            alertDialog.show();
                        }
                    } else {
                        final AlertDialog alertDialog = new AlertDialog.Builder(
                                mContext).create();
                        alertDialog.setTitle(eKYCActivity.this.getResources()
                                .getString(R.string.morphosample));
                        alertDialog.setMessage(ErrorCodes.getError(ret,
                                morphoDevice.getInternalError()));
                        alertDialog.setCancelable(false);
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                                mContext.getResources().getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // finish();
                                    }
                                });
                        alertDialog.show();
                    }
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            mContext);
                    alertDialog.setNeutralButton("OK", null);
                    alertDialog.setMessage("Please ensure all data are filled");
                    alertDialog.show();
                }
            }

            // else {
            // AlertDialog.Builder alertDialog = new
            // AlertDialog.Builder(
            // context);
            // alertDialog.setNeutralButton("OK", null);
            // alertDialog
            // .setMessage("Device does not have permission for USB");
            // alertDialog.show();
            // }

        } catch (Exception e) {

            if (AppConstants.DEBUG)
                Log.e(TAG, e.toString() + " error<-- in getting FingerPrint");
            Toast.makeText(getApplicationContext(), e.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private boolean hasPermission() {
        try {
            String BIOMETRIC_LICENSE_PERMISSION = "com.sec.enterprise.biometric.permission.IRIS_RECOGNITION";
            PackageManager packageManager = this.getApplicationContext()
                    .getPackageManager();

            if (packageManager.checkPermission(BIOMETRIC_LICENSE_PERMISSION,
                    this.getApplicationContext().getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    private void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void startCapture() {
        // kyctxtGone();
        int numeye = 2;

        if (singleChecked)
            numeye = 1;
        // secIrisManager.registerCallback(this);
        // secIrisManager.startCapture(numeye);
        isCapturinginProgress = true;
        preview = findViewById(R.id.preview);

        ll_header_part.setVisibility(View.GONE);

    }

    private void stopCapture() {
        preview.setVisibility(View.GONE);
        tvStatus.setVisibility(View.GONE);
        //ll_header_part.setVisibility(View.GONE);
        //ll_progress_layout.setVisibility(View.VISIBLE);
        showDialog(DIALOG_PROGRESS);


        // secIrisManager.stopCapture();
        isCapturinginProgress = false;
    }

    private void updateEyeInfo(String eyeDist, String eyeOpen) {
        int eye_distance = Integer.valueOf(eyeDist);
        {
            if (eye_distance < 16 && eye_distance > 0) {
                tooCloseCount++;
                if (tooCloseCount >= 3) {
                    if (singleChecked)
                        preview.setBackgroundResource(R.drawable.red_single);
                    else
                        preview.setBackgroundResource(R.drawable.red);
                    // t.run();
                    return;
                }
                if (eyeOpen == "Open Eyes") {
                    if (openingCount >= 0) {
                        openWidelyCount++;
                        if (openWidelyCount >= 0) {
                            // Log.d(toString(),
                            // " feedback Changes open widely ");
                            // resultTxtVu.setText("Open Widely");
                            // resultTxtVu.setTextColor(Color.RED);
                            if (singleChecked)
                                preview.setBackgroundResource(R.drawable.red_single);
                            else
                                preview.setBackgroundResource(R.drawable.red);
                            // t.run();
                            return;
                        }
                    }
                } else {
                    openingCount = 0;
                }
            } else if (eye_distance >= 25) {
                tooFarCount++;
                if (tooFarCount >= 3) {
                    if (singleChecked)
                        preview.setBackgroundResource(R.drawable.red_single);
                    else
                        preview.setBackgroundResource(R.drawable.red);
                    // t.run();
                    return;
                }
                if (eyeOpen == "Open Eyes") {
                    if (openingCount >= 0) {
                        openWidelyCount++;
                        if (openWidelyCount >= 0) {
                            if (singleChecked)
                                preview.setBackgroundResource(R.drawable.red_single);
                            else
                                preview.setBackgroundResource(R.drawable.red);
                            return;
                        }
                    }
                } else {
                    openingCount = 0;
                }
            } else if (eye_distance <= 0) {
                eyesNotDetected++;
                if (eyesNotDetected >= 10) {
                    if (singleChecked)
                        preview.setBackgroundResource(R.drawable.red_single);
                    else
                        preview.setBackgroundResource(R.drawable.red);
                }
            } else if (eye_distance >= 16 && eye_distance < 25) {
                if (eyeOpen == "Open Eyes Wider") {
                    openWidelyCount++;
                    if (openWidelyCount >= 0) {
                        if (singleChecked)
                            preview.setBackgroundResource(R.drawable.green_single);
                        else
                            preview.setBackgroundResource(R.drawable.green);
                        return;
                    }
                } else {
                    goodCount++;
                    if (goodCount >= 0) {
                        if (singleChecked)
                            preview.setBackgroundResource(R.drawable.green_single);
                        else
                            preview.setBackgroundResource(R.drawable.green);
                        goodCount = 0;
                        tooFarCount = 0;
                        openWidelyCount = 0;
                        tooCloseCount = 0;
                        eyesNotDetected = 0;
                    }
                    return;
                }
            } else {
                if (singleChecked)
                    preview.setBackgroundResource(R.drawable.grey_single);
                else
                    preview.setBackgroundResource(R.drawable.grey);
            }
        }
        return;
    }

    public void onCaptureFailed(int arg0) {
        mHandler.post(new Runnable() {
            public void run() {
                mSensorManager.unregisterListener(proximitySensorEventListener);
                toast("onCaptureFailed");
                stopCapture();
            }
        });
    }

    public void onEyeInfoUiHints(String EyeDist, String EyeOpen) {
        mEyeOpen = EyeOpen;
        mEyeDist = EyeDist;
        mHandler.post(new Runnable() {
            public void run() {
                updateEyeInfo(mEyeDist, mEyeOpen);
            }
        });
    }

    public void onTimeOut() {
        // TODO Auto-generated method stub
        mHandler.post(new Runnable() {
            public void run() {
                toast("onTimeOut");
                mSensorManager.unregisterListener(proximitySensorEventListener);
                stopCapture();
            }
        });
    }

    /* PID Block Generation method */
    private PidDetails getPIDXML() {
        PidDetails result = new PidDetails();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String ts = sdf.format(new Date());

        String dname = "";
        String pid = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<ns2:Pid xmlns:ns2=\"http://www.uidai.gov.in/authentication/uid-auth-request-data/1.0\" "
                + "ts=\""
                + ts
                + "\" >"
                + "<Demo>"
                + "<Pi "
                + "name=\""
                + dname
                + "\"/>" + "</Demo>" + "<Bios>" + "</Bios>" + "</ns2:Pid>";

        byte[] piddata = pid.getBytes();

        result.setTs(ts);
        result.setPiddata(piddata);
        return result;
    }

    private String getcertIdentifierNew() {
        String certificateIdentifier = null;
        try {
            InputStream is = getResources().openRawResource(
                    Global.EncryptionCertificateId_new);
            CertificateFactory certFactory = CertificateFactory
                    .getInstance("X509");
            uidaiCert = (X509Certificate) certFactory.generateCertificate(is);
            Date certExpiryDate = uidaiCert.getNotAfter();
            SimpleDateFormat ciDateFormat = new SimpleDateFormat("yyyyMMdd");
            ciDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            certificateIdentifier = ciDateFormat.format(certExpiryDate);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return certificateIdentifier;
    }

    private String getcertIdentifierOld() {
        String certificateIdentifier = null;
        try {
            InputStream is = getResources().openRawResource(
                    Global.EncryptionCertificateId_old);
            CertificateFactory certFactory = CertificateFactory
                    .getInstance("X509");
            uidaiCert = (X509Certificate) certFactory.generateCertificate(is);
            Date certExpiryDate = uidaiCert.getNotAfter();
            SimpleDateFormat ciDateFormat = new SimpleDateFormat("yyyyMMdd");
            ciDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            certificateIdentifier = ciDateFormat.format(certExpiryDate);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return certificateIdentifier;
    }

    private Properties loadPreferences(String prefsFile) {
        InputStream is = null;
        Properties prefs = null;
        try {
            is = getApplicationContext().getAssets().open(prefsFile);
            if (is != null) {
                prefs = new Properties();
                prefs.load(is);
            }
        } catch (IOException ex) {
            Log.d(this.toString(), "Error accessing preferences file");
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
                Log.d(this.toString(), "Error closing preferences file");
            }
        }
        return prefs;
    }

    public void showPreviewFrame(byte[] bytesPreview, int width, int height,
                                 int rotation) {
        mBytesData = bytesPreview;

        mRotation = rotation;
        final int w = width;
        final int h = height;
        mHandler.post(new Runnable() {
            @SuppressWarnings("deprecation")
            public void run() {
                preview = findViewById(R.id.preview);
                preview.setVisibility(View.VISIBLE);
                tvStatus.setVisibility(View.VISIBLE);
                // int width = device.getWidth(), height = device.getHeight();
				/*
				 * int width = 1024; int height = 800; int w = width / scale, h
				 * = height / scale;
				 */
                Paint circlePaint = null, reflectPaint = null, bitmapPaint = null;
                int[] pixelsPreview = null;
                if (pixelsPreview == null) {
                    pixelsPreview = new int[w * h];
                }
                for (int i = 0; i < pixelsPreview.length; i++) {
                    final int index = mRotation > 90 ? pixelsPreview.length - i
                            - 1 : i;
                    int p = (mBytesData[index] & 0xff);
                    pixelsPreview[i] = p | (p << 8) | (p << 16) | 0xff000000;
                }
                holder = preview.getHolder();
                canvas = null;
                try {
                    canvas = holder.lockCanvas();
                    if (canvas != null) {
                        if (mRotation == 90) {
                            canvas.scale(canvas.getWidth() * 1.0f / h,
                                    -canvas.getHeight() * 1.0f / w);
                            canvas.rotate(90);
                            canvas.translate(-w, -h);
                        } else {
                            canvas.scale(3, 3);
                        }
                        if (bitmapPaint == null) {
                            bitmapPaint = new Paint(Paint.DITHER_FLAG
                                    | Paint.FILTER_BITMAP_FLAG);
                            bitmapPaint.setColor(0xff40d0ff);
                            bitmapPaint.setStyle(Paint.Style.STROKE);
                            bitmapPaint.setStrokeWidth(1f);
                            circlePaint = new Paint(bitmapPaint);
                            circlePaint.setColor(0x8040d0ff);
                            reflectPaint = new Paint(bitmapPaint);
                            reflectPaint.setColor(0xffff0000);
                        }
                        System.gc();
                        canvas.drawColor(Color.TRANSPARENT,
                                android.graphics.PorterDuff.Mode.CLEAR);
                        canvas.drawBitmap(pixelsPreview, 0, w, 0, 0, w, h,
                                true, bitmapPaint);
                    }
                } finally {
                    if (canvas != null) {
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        });
    }

	/* Method for Captured Success of IRIS */
    // public void onCaptureSuccess() {
    // // progressBar1.setVisibility(View.VISIBLE);
    // runOnUiThread(new Runnable() {
    // public void run() {
    // mSensorManager.unregisterListener(proximitySensorEventListener);
    // stopCapture();
    // ci = getcertIdentifierNew();
    // X509Certificate[] chain = new X509Certificate[1];
    // chain[0] = uidaiCert;
    // final PidDetails pidDetails = getPIDXML();
    // int pidtype = SecIrisConstants.PIDTYPE_XML;
    //
    // /* To get Encrypted PID Data */
    //
    // if (singleChecked) {
    // encPIDData = secIrisManager.getEncryptedPid(
    // pidDetails.getPiddata(), pidtype,
    // SecIrisConstants.BIOTYPE_UNKNOWN_IRIS, chain);
    // } else {
    // encPIDData = secIrisManager.getEncryptedPid(
    // pidDetails.getPiddata(), pidtype,
    // SecIrisConstants.BIOTYPE_BOTH_IRIS, chain);
    // }
    //
    // // if (encPIDData.length == 0) {
    // //
    // // ci = getcertIdentifierOld();
    // // chain[0] = uidaiCert;
    // // if (singleChecked) {
    // // encPIDData = secIrisManager.getEncryptedPid(
    // // pidDetails.getPiddata(), pidtype,
    // // SecIrisConstants.BIOTYPE_UNKNOWN_IRIS, chain);
    // // } else {
    // // encPIDData = secIrisManager.getEncryptedPid(
    // // pidDetails.getPiddata(), pidtype,
    // // SecIrisConstants.BIOTYPE_BOTH_IRIS, chain);
    // // }
    // //
    // // }
    //
    // final byte[] encryptedHmacBytes = secIrisManager
    // .getEncryptedHMAC();
    // final byte[] sKey = secIrisManager.getEncryptedSessionKey();
    // new Thread() {
    // public void run() {
    // try {
    // String androidId = Settings.Secure.getString(
    // getContentResolver(),
    // Settings.Secure.ANDROID_ID);
    //
    // final TelephonyManager tm = (TelephonyManager) getBaseContext()
    // .getSystemService(Context.TELEPHONY_SERVICE);
    //
    // String str_IMEI_No = "" + tm.getDeviceId();
    //
    // ECSKycRequest request = new ECSKycRequest();
    // request.setAadhaarNumber(proposer_AadharNumber);
    // request.setCi(ci);
    // request.setUdc(str_IMEI_No);
    // request.setTerminalId("public");
    // request.setFdc("NA");
    // request.setHmac(com.sbilife.ekyc.utilites.Base64
    // .encode(encryptedHmacBytes));
    // request.setIdc("NC");
    // request.setKey(com.sbilife.ekyc.utilites.Base64
    // .encode(sKey));
    // request.setPfr("Y");
    // request.setPidTs(pidDetails.getTs());
    // request.setPid(com.sbilife.ekyc.utilites.Base64
    // .encode(encPIDData));
    // request.setSubType("IIR");
    //
    // String dataAsXml = XMLUtilities.getXML(request);
    // StringBuilder str_kycXML = new StringBuilder();
    // str_kycXML.append(dataAsXml);
    // str_kycXML.append("<AppName>" + "Connect Life"
    // + "</AppName>");
    // str_kycXML.append("<ReqID>" + QuatationNumber
    // + "</ReqID>");
    //
    // /* str_kycXML is generated request XML */
    //
    // final String resString = HttpConnector
    // str_kycXML.toString());
    //
    // if (resString.startsWith("<Error>")) {
    // runOnUiThread(new Runnable() {
    // @Override
    // public void run() {
    // // pd.dismiss();
    // String errorMessage = getErrorMessage(resString);
    // showErrorActivity(errorMessage);
    // }
    // });
    // return;
    // }
    //
    // if (resString.contains("<ECSKycResponse") == true) {
    // final ECSKycResponse res = (ECSKycResponse) XMLUtilities
    // .parseXML(ECSKycResponse.class,
    // resString);
    //
    // if (res.getErr()) {
    // runOnUiThread(new Runnable() {
    // @Override
    // public void run() {
    //
    // String errorMessage = res
    // .getErrCode()
    // + "-"
    // + res.getErrMsg();
    // showErrorActivity(errorMessage);
    // }
    // });
    // return;
    // }
    //
    // runOnUiThread(new Runnable() {
    // @Override
    // public void run() {
    // System.out.println("Success");
    // try {
    //
    // StringBuilder str_kycResXML = new StringBuilder();
    // str_kycResXML.append(resString);
    //
    // str_kycResXML
    // .append("<ConsentAccepted>"
    // + "Y"
    // + "</ConsentAccepted>");
    //
    // str_kycResXML
    // .append("<BIOMETRICMODE>"
    // + "IIR"
    // + "</BIOMETRICMODE>");
    //
    // /* KYC Encrypted response */
    //
    // String str_encyptedRes = SimpleCrypto
    // .encrypt("SBIL",
    // str_kycResXML
    // .toString());
    //
    // String Str_AppVersion = String
    // .valueOf(ServiceURL.APP_VERSION);
    //
    // long rowId = db.insertKYCDetails(
    // str_encyptedRes,
    // QuatationNumber,
    // Str_AppVersion);
    // Toast.makeText(
    // context,
    // "KYC Details Added Successfully",
    // Toast.LENGTH_LONG).show();
    //
    // Intent intent = new Intent();
    // intent.putExtra("KYC_XML", res);
    // setResult(RESULT_OK, intent);
    // finish();
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    //
    // showErrorActivity("Error in KYC Response!\n"
    // + e.getMessage());
    // }
    //
    // }
    // });
    // }
    //
    // /************** KYC ***************************/
    //
    // } catch (final Exception e) {
    // System.out.println("Inside Exception");
    // e.printStackTrace();
    // showErrorActivity("Error sending Kyc request!\n"
    // + e.getMessage());
    //
    // }
    // } // run()
    // }.start(); // new Thread()
    //
    // }
    // });
    // }

    private void onCaptureRDIRISSuccess() {

        // progressBar1.setVisibility(View.VISIBLE);
        runOnUiThread(new Runnable() {
            public void run() {
                new Thread() {
                    public void run() {
                        try {
                            /*String androidId = Settings.Secure.getString(
                                    getContentResolver(),
                                    Settings.Secure.ANDROID_ID);

                            final TelephonyManager tm = (TelephonyManager) getBaseContext()
                                    .getSystemService(Context.TELEPHONY_SERVICE);

                            String str_IMEI_No = "" + tm.getDeviceId();*/
                            ECSRDKYCRequest request = new ECSRDKYCRequest();
                            request.setAadhaarNumber(customer_AadharNumber);
                            request.setPidData(pidDataXMLbase64);
                            request.setTid("registered");
                           // request.setUdc(str_IMEI_No);
                            request.setUsesBio(true);
                            request.setUsesBt("IIR");
                            request.setUsesOtp(false);
                            request.setLr("true");
                            request.setPfr("true");

                            String dataAsXml = XMLUtilities.getXML(request);
                            String str_kycXML = dataAsXml +
                                    "<AppName>" + "Smart Advisor"
                                    + "</AppName>" +
                                    "<ReqID>" + ProposalNumber + "-" + date1.getTime() + "</ReqID>" +
                                    "<KYCType>" + "IIR"
                                    + "</KYCType>";

                            /* str_kycXML is generated request XML */

                            final String resString  = changeUrl(str_kycXML);

                            if (mProgressDialog.isShowing())
                                mProgressDialog.dismiss();

                            str_biometric_type = "IIR";

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                            //save to server
                                    linkAadharDetails( resString);
                                }
                            });


                            /*if (resString.startsWith("<Error>")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // pd.dismiss();
                                        String errorMessage = getErrorMessage(resString);

                                        showErrorActivity(errorMessage);
                                    }
                                });
                                return;
                            }

                            if (resString.contains("<ECSKycResponse") == true) {
                                final ECSKycResponse res = (ECSKycResponse) XMLUtilities
                                        .parseXML(ECSKycResponse.class,
                                                resString);

                                if (res.getErr()) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            String errorMessage = res
                                                    .getErrCode()
                                                    + "-"
                                                    + res.getErrMsg();

                                            showErrorActivity(errorMessage);
                                        }
                                    });
                                    return;
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        System.out.println("Success");
                                        try {
                                            StringBuilder str_kycResXML = new StringBuilder();
                                            str_kycResXML.append(resString);

                                            str_kycResXML.append("<ConsentAccepted>"
                                                            + "Y"
                                                            + "</ConsentAccepted>");

                                            str_kycResXML.append("<BIOMETRICMODE>"
                                                            + "IIR"
                                                            + "</BIOMETRICMODE>");

                                            //ll_progress_layout.setVisibility(View.GONE);
                                            //ll_header_part.setVisibility(View.VISIBLE);
                                            if (mProgressDialog.isShowing())
                                                mProgressDialog.dismiss();

                                            //show data on dialog

                                        } catch (Exception e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();

                                            showErrorActivity("Error in KYC Response!\n"
                                                    + e.getMessage());
                                        }

                                    }
                                });
                            }*/

                            /********** KYC ***************************/

                        } catch (final Exception e) {
                            System.out.println("Inside Exception");
                            e.printStackTrace();
                            showErrorActivity("Error sending Kyc request!\n"
                                    + e.getMessage());

                        }
                    } // run()
                }.start(); // new Thread()

            }
        });

    }

    /* Method for capture success of FingerPrint */
    public void onFingerPrintCaptureSuccess() {
        // ll_progress_bar.setVisibility(View.VISIBLE);

        runOnUiThread(new Runnable() {
            public void run() {

                new Thread() {
                    public void run() {
                        try {
                            String androidId = Settings.Secure.getString(
                                    getContentResolver(),
                                    Settings.Secure.ANDROID_ID);

                            bios.add(new BiometricData(
                                    BiometricPosition.UNKNOWN,
                                    BioMetricType.FMR, isoTemplate));
                            InputStream is = getResources().openRawResource(
                                    Global.EncryptionCertificateId_new);
                            pidData = PIDGenerator.generateBiometricPIDBlock(
                                    "2.0", bios, is);

                            /********** AUTH ***************************/

                            ECSKycRequest request = new ECSKycRequest();
                            request.setAadhaarNumber(customer_AadharNumber);
                            request.setSubType("FMR");
                            request.setFdc("NC");
                            request.setIdc("NA");
                            request.setCi(pidData.getCertificateIdentifier());
                            request.setKey(sbilife.com.pointofsale_bancaagency.ekyc.utilites.Base64
                                    .encode(pidData.getEncryptedSessionKey()));
                            request.setHmac(sbilife.com.pointofsale_bancaagency.ekyc.utilites.Base64
                                    .encode(pidData.getEncryptedHmac()));
                            request.setPid(sbilife.com.pointofsale_bancaagency.ekyc.utilites.Base64
                                    .encode(pidData.getEncPIDData()));
                            request.setPidTs(pidData.getPidTimestamp());
                            request.setTerminalId("public");
                            // request.setUdc(androidId);
                            request.setUdc(deviceCode);

                            String dataAsXml = XMLUtilities.getXML(request);
                            String str_kycXML = dataAsXml +
                                    "<AppName>" + "Smart Advisor"
                                    + "</AppName>" +
                                    "<ReqID>" + ProposalNumber + "-" + date1.getTime() + "</ReqID>";

                            /* str_kycXML is generated request XML */


                            final String resString  = changeUrl(str_kycXML);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    linkAadharDetails(resString);
                                }
                            });

                            /*if (resString.startsWith("<Error>")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // pd.dismiss();
                                        String errorMessage = getErrorMessage(resString);
                                        showErrorActivity(errorMessage);
                                    }
                                });
                                return;
                            }

                            if (resString.contains("<ECSKycResponse")) {
                                final ECSKycResponse res = (ECSKycResponse) XMLUtilities
                                        .parseXML(ECSKycResponse.class,
                                                resString);

                                if (res.getErr()) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            String errorMessage = res
                                                    .getErrCode()
                                                    + "-"
                                                    + res.getErrMsg();
                                            showErrorActivity(errorMessage);
                                        }
                                    });
                                    return;
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        System.out.println("Success");
                                        try {

                                            String str_kycResXML = resString +
                                                    "<ConsentAccepted>"
                                                    + "Y"
                                                    + "</ConsentAccepted>" +
                                                    "<BIOMETRICMODE>"
                                                    + "FMR"
                                                    + "</BIOMETRICMODE>";

                                            *//* KYC Encrypted response *//*

                                            String str_encyptedRes = SimpleCrypto
                                                    .encrypt("SBIL",
                                                            str_kycResXML);

											*//*String Str_AppVersion = String
													.valueOf(ServiceURL.APP_VERSION);*//*
                                            int Str_AppVersion = 0;
                                            PackageManager manager = mContext.getPackageManager();
                                            PackageInfo info;
                                            try {
                                                info = manager.getPackageInfo(mContext.getPackageName(), 0);
                                                Str_AppVersion = info.versionCode;
                                            } catch (PackageManager.NameNotFoundException e1) {
                                                // TODO Auto-generated catch block
                                                e1.printStackTrace();
                                                Str_AppVersion = 0;
                                            }

											*//*long rowId = db.insertKYCDetails(
													str_encyptedRes,
													QuatationNumber,
													Str_AppVersion+"");*//*
                                            Toast.makeText(
                                                    mContext,
                                                    "KYC Details Added Successfully",
                                                    Toast.LENGTH_LONG).show();

                                            // obj.SetKYCDetails(res);
                                            Intent intent = new Intent();
                                            intent.putExtra("KYC_XML", res);
                                            // setResult(10, intent);
                                            setResult(RESULT_OK, intent);
                                            finish();
                                        } catch (Exception e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                            showErrorActivity("Error in KYC Response!\n"
                                                    + e.getMessage());
                                        }

                                    }
                                });
                            }*/

                            /********** KYC ***************************/

                        } catch (final Exception e) {
                            System.out.println("Inside Exception");
                            e.printStackTrace();
                            showErrorActivity("Error sending Kyc request!\n"
                                    + e.getMessage());
                        }
                    } // run()
                }.start(); // new Thread()

            }
        });
    }

    private void onFingerPrintRDServiceCaptureSuccess() {

        // ll_progress_bar.setVisibility(View.VISIBLE);

        runOnUiThread(new Runnable() {
            public void run() {

                new Thread() {
                    public void run() {
                        try {
                            /*final TelephonyManager tm = (TelephonyManager) getBaseContext()
                                    .getSystemService(Context.TELEPHONY_SERVICE);

                            String str_IMEI_No = "" + tm.getDeviceId();*/

                            ECSRDKYCRequest request = new ECSRDKYCRequest();
                            request.setAadhaarNumber(customer_AadharNumber);
                            request.setPidData(pidDataXMLbase64);
                            request.setTid("registered");
                          //  request.setUdc(str_IMEI_No);
                            request.setUsesBio(true);
                            request.setUsesBt("FMR");
                            request.setUsesOtp(false);
                            request.setLr("true");
                            request.setPfr("true");

                            String dataAsXml = XMLUtilities.getXML(request);
                            String str_kycXML = dataAsXml +
                                    "<AppName>" + "Smart Advisor"
                                    + "</AppName>" +
                                    "<ReqID>" + ProposalNumber + "-" + date1.getTime() + "</ReqID>" +
                                    "<KYCType>" + "FMR"
                                    + "</KYCType>";

                             //str_kycXML is generated request XML

                            final String resString  = changeUrl(str_kycXML);
                            if (mProgressDialog.isShowing())
                                mProgressDialog.dismiss();

                            str_biometric_type = "FMR";

                            //save to server
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    linkAadharDetails(resString);
                                }
                            });

                            /*if (resString.startsWith("<Error>")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // pd.dismiss();
                                        String errorMessage = getErrorMessage(resString);
                                        showErrorActivity(errorMessage);
                                    }
                                });
                                return;
                            }

                            if (resString.contains("<ECSKycResponse") == true) {
                                final ECSKycResponse res = (ECSKycResponse) XMLUtilities
                                        .parseXML(ECSKycResponse.class,
                                                resString);

                                if (res.getErr()) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            String errorMessage = res
                                                    .getErrCode()
                                                    + "-"
                                                    + res.getErrMsg();
                                            showErrorActivity(errorMessage);
                                        }
                                    });
                                    return;
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        System.out.println("Success");
                                        try {

                                            StringBuilder str_kycResXML = new StringBuilder();
                                            str_kycResXML.append(resString);

                                            str_kycResXML
                                                    .append("<ConsentAccepted>"
                                                            + "Y"
                                                            + "</ConsentAccepted>");

                                            str_kycResXML
                                                    .append("<BIOMETRICMODE>"
                                                            + "FMR"
                                                            + "</BIOMETRICMODE>");

                                            //ll_progress_layout.setVisibility(View.GONE);
                                            //ll_header_part.setVisibility(View.VISIBLE);
                                            if (mProgressDialog.isShowing())
                                                mProgressDialog.dismiss();

                                            //show data on dialog

                                        } catch (Exception e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                            showErrorActivity("Error in KYC Response!\n"
                                                    + e.getMessage());
                                        }

                                    }
                                });
                            }*/

                            /********** KYC ***************************/

                        } catch (final Exception e) {
                            System.out.println("Inside Exception");
                            e.printStackTrace();
                            showErrorActivity("Error sending Kyc request!\n"
                                    + e.getMessage());
                        }
                    } // run()
                }.start(); // new Thread()

            }
        });

    }

    private String getErrorMessage(String error) {
        int startIndex = "<Error>".length();
        int endIndex = error.indexOf("</Error>");

        if (endIndex != -1) {
            return error.substring(startIndex, endIndex);
        }

        return "";
    }

    private void showErrorActivity(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                showAlert.setMessage(message);
                showAlert.setCancelable(false);
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                //ll_header_part.setVisibility(View.VISIBLE);
                                //ll_progress_layout.setVisibility(View.GONE);
                                if (mProgressDialog.isShowing())
                                    mProgressDialog.dismiss();

                                /*//flag to show error dialog
                                isSuccessEkyc = false;*/

                                //call link to aadahr service to save data
                                /*mAsynch_link_aadhar_details = new Asynch_link_aadhar_details();
                                mAsynch_link_aadhar_details.execute();*/
                            }
                        });
                showAlert.show();
            }
        });
    }

    private class PidDetails {
        private String ts;
        private byte[] piddata;

        public String getTs() {
            return ts;
        }

        public void setTs(String ts) {
            this.ts = ts;
        }

        public byte[] getPiddata() {
            return piddata;
        }

        public void setPiddata(byte[] piddata) {
            this.piddata = piddata;
        }
    }

    private void activateIrisLicense() {
        try {

            String key = "5B481893587214DD076EE91D706FE2DE704F8EEE351C8A6C6E81BAE63C6BE8BE201167A9B97A4421864D7C03685957016B474B066AC9C58FCB27B831DC084524";

            String packageName = getApplicationContext().getPackageName();
            Log.i("Activation", "packageName: " + packageName);
            mLicenseMgr.activateLicense(key, packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("Received " + intent.getAction());
            String action = intent.getAction();
            if (action.equals(SecBiometricLicenseManager.ACTION_LICENSE_STATUS)) {
                unregisterReceiver(mReceiver);

                Bundle extras = intent.getExtras();
                String status = extras
                        .getString(SecBiometricLicenseManager.EXTRA_LICENSE_STATUS);
                int err_code = extras
                        .getInt(SecBiometricLicenseManager.EXTRA_LICENSE_ERROR_CODE);

                if (status != null && status.equals("success")) {
                    Toast toast = Toast.makeText(context, "",
                            Toast.LENGTH_SHORT);
                    toast.setText("status = " + status + " , "
                            + " License activated");
                    toast.show();
                    finish();
                } else {
                    Toast toast = Toast.makeText(context, "",
                            Toast.LENGTH_SHORT);
                    toast.setText("status = " + status + " , "
                            + "error code = " + err_code);
                    toast.show();
                }
            }

        }
    };

    @SuppressLint("UseValueOf")
    private boolean connection() {

        boolean connectionflag = false;
        int ret = morphoDevice.openUsbDevice(sensorName, 0);

        connectionflag = ret == 0;
        return connectionflag;
    }

    public void Capture() {
        isCaptureVerif = false;
        final TemplateList templateList = new TemplateList();
        MorphoInfo morphoInfo = ProcessInfo.getInstance().getMorphoInfo();
        ProcessInfo processInfo = ProcessInfo.getInstance();
        int timeout;
        int acquisitionThreshold = 0;
        int advancedSecurityLevelsRequired;
        TemplateType templateType;
        TemplateFVPType templateFVPType;
        int maxSizeTemplate = 255;
        EnrollmentType enrollType;
        LatentDetection latentDetection;
        Coder coderChoice;
        int detectModeChoice;
        boolean exportFVP = false, exportFP = false;
        timeout = processInfo.getTimeout();

        if (processInfo.isFingerprintQualityThreshold()) {
            acquisitionThreshold = processInfo
                    .getFingerprintQualityThresholdvalue();
        }

        templateType = ((CaptureInfo) morphoInfo).getTemplateType();
        templateFVPType = ((CaptureInfo) morphoInfo).getTemplateFVPType();

        if (templateType != TemplateType.MORPHO_NO_PK_FP) {
            exportFP = true;
            if (templateType == TemplateType.MORPHO_PK_MAT
                    || templateType == TemplateType.MORPHO_PK_MAT_NORM
                    || templateType == TemplateType.MORPHO_PK_PKLITE) {
                maxSizeTemplate = 1;
            } else {
                maxSizeTemplate = 255;
            }
        } else {
            if (!MorphoInfo.m_b_fvp) {
                templateType = TemplateType.MORPHO_PK_COMP;
            }
            maxSizeTemplate = 255;
        }

        if (templateFVPType != TemplateFVPType.MORPHO_NO_PK_FVP) {
            exportFVP = true;
        }

        isCaptureVerif = true;
        enrollType = EnrollmentType.ONE_ACQUISITIONS;

        if (((CaptureInfo) morphoInfo).isLatentDetect()) {
            latentDetection = LatentDetection.LATENT_DETECT_ENABLE;
        } else {
            latentDetection = LatentDetection.LATENT_DETECT_DISABLE;
        }

        coderChoice = processInfo.getCoder();

        detectModeChoice = DetectionMode.MORPHO_ENROLL_DETECT_MODE.getValue();

        if (processInfo.isForceFingerPlacementOnTop()) {
            detectModeChoice |= DetectionMode.MORPHO_FORCE_FINGER_ON_TOP_DETECT_MODE
                    .getValue();
        }

        if (processInfo.isWakeUpWithLedOff()) {
            detectModeChoice |= MorphoWakeUpMode.MORPHO_WAKEUP_LED_OFF
                    .getCode();
        }

        advancedSecurityLevelsRequired = 0;
        // if (((CaptureInfo) morphoInfo).getCaptureType() != CaptureType.Verif)
        // {
        //
        // if (processInfo.isAdvancedSecLevCompReq()) {
        // advancedSecurityLevelsRequired = 1;
        // } else {
        // advancedSecurityLevelsRequired = 0;
        // }
        // } else {
        // advancedSecurityLevelsRequired = 0xFF;
        // if (processInfo.isAdvancedSecLevCompReq()) {
        // advancedSecurityLevelsRequired = 1;
        // }
        // }

        int callbackCmd = ProcessInfo.getInstance().getCallbackCmd();

        int nbFinger = ((CaptureInfo) morphoInfo).getFingerNumber();
        final String idUser = ((CaptureInfo) morphoInfo).getIDNumber();

        int ret = morphoDevice.setStrategyAcquisitionMode(ProcessInfo
                .getInstance().getStrategyAcquisitionMode());

        if (ret == 0) {
            ret = morphoDevice.capture(timeout, acquisitionThreshold,
                    advancedSecurityLevelsRequired, nbFinger,
                    TemplateType.MORPHO_PK_ISO_FMR,
                    TemplateFVPType.MORPHO_NO_PK_FVP, 255, enrollType,
                    latentDetection, coderChoice, detectModeChoice,
                    CompressionAlgorithm.MORPHO_NO_COMPRESS, 0, templateList,
                    callbackCmd, this);
        }

        ProcessInfo.getInstance().setCommandBioStart(false);

        String str_message = "";
        // try {
        if (ret == ErrorCodes.MORPHO_OK) {
            byte[] data = null;
            try {
                int NbTemplate = templateList.getNbTemplate();

                for (int i = 0; i < NbTemplate; i++) {
                    Template t = templateList.getTemplate(i);
                    data = t.getData();
                }

                int flags = Base64.NO_WRAP;

                base64FingerTemplate = Base64.encodeToString(data,
                        0, data != null ? data.length : 0, flags);

                isoTemplate = data;

                Toast.makeText(this, "Fingerprint capture successfully",
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();

            }

        }

        final String alertMessage = str_message;
        final int internalError = morphoDevice.getInternalError();
        final int retvalue = ret;
        mHandler.post(new Runnable() {
            @Override
            public synchronized void run() {
                alert(retvalue, internalError, "Capture", alertMessage);
            }
        });
        // notifyEndProcess();

    }

    private void updateImage(Bitmap bitmap) {
        try {
            // ImageView iv = (ImageView) findViewById(id);
            iv_ekyc_fingerprintcapture.setImageBitmap(bitmap);

        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void alert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.app_name);
        alertDialog.setMessage(msg);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        alertDialog.show();
    }

    private void alert(int codeError, int internalError, String title,
                       String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        String msg;
        if (codeError == 0) {
            msg = getString(R.string.OP_SUCCESS);
        } else {
            String errorInternationalization = convertToInternationalMessage(
                    codeError, internalError);
            msg = getString(R.string.OP_FAILED) + "\n"
                    + errorInternationalization;
        }
        msg += ((message.equalsIgnoreCase("")) ? "" : "\n" + message);
        alertDialog.setMessage(msg);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        alertDialog.show();
    }

    /* For Updating progress and sensor message */
    @SuppressWarnings("deprecation")
    private void updateSensorProgressBar(int level) {
        try {

            final float[] roundedCorners = new float[]{5, 5, 5, 5, 5, 5, 5, 5};
            ShapeDrawable pgDrawable = new ShapeDrawable(new RoundRectShape(
                    roundedCorners, null, null));

            int color = Color.GREEN;

            if (level <= 25) {
                color = Color.RED;
            } else if (level <= 50) {
                color = Color.YELLOW;
            }
            pgDrawable.getPaint().setColor(color);
            ClipDrawable progress = new ClipDrawable(pgDrawable, Gravity.LEFT,
                    ClipDrawable.HORIZONTAL);
            progressBar.setProgressDrawable(progress);
            progressBar.setBackgroundDrawable(getResources().getDrawable(
                    android.R.drawable.progress_horizontal));
            progressBar.setProgress(level);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public synchronized void update(Observable o, Object arg) {
        try {
            // convert the object to a callback back message.
            CallbackMessage message = (CallbackMessage) arg;

            int type = message.getMessageType();

            switch (type) {

                case 1:
                    // message is a command.
                    Integer command = (Integer) message.getMessage();

                    // Analyze the command.
                    switch (command) {
                        case 0:
                            strMessage = "move-no-finger";
                            break;
                        case 1:
                            strMessage = "move-finger-up";
                            break;
                        case 2:
                            strMessage = "move-finger-down";
                            break;
                        case 3:
                            strMessage = "move-finger-left";
                            break;
                        case 4:
                            strMessage = "move-finger-right";
                            break;
                        case 5:
                            strMessage = "press-harder";
                            break;
                        case 6:
                            strMessage = "move-latent";
                            break;
                        case 7:
                            strMessage = "remove-finger";
                            break;
                        case 8:
                            strMessage = "finger-ok";
                            // switch live acquisition ImageView
                            if (isCaptureVerif) {
                                isCaptureVerif = false;
                            }

                    }

                    mHandler.post(new Runnable() {
                        @Override
                        public synchronized void run() {
                            updateSensorMessage(strMessage);
                        }
                    });

                    break;
                case 2:
                    // message is a low resolution image, display it.

                    byte[] image = (byte[]) message.getMessage();

                    MorphoImage morphoImage = MorphoImage.getMorphoImageFromLive(image);

                    int imageRowNumber = morphoImage.getMorphoImageHeader()
                            .getNbRow();
                    int imageColumnNumber = morphoImage.getMorphoImageHeader()
                            .getNbColumn();
                    final Bitmap imageBmp = Bitmap.createBitmap(imageColumnNumber,
                            imageRowNumber, Config.ALPHA_8);

                    imageBmp.copyPixelsFromBuffer(ByteBuffer.wrap(
                            morphoImage.getImage(), 0,
                            morphoImage.getImage().length));

                    mHandler.post(new Runnable() {
                        @Override
                        public synchronized void run() {
                            updateImage(imageBmp);
                        }
                    });
                    break;
                case 3:
                    // message is the coded image quality.
                    final Integer quality = (Integer) message.getMessage();
                    mHandler.post(new Runnable() {
                        @Override
                        public synchronized void run() {
                            updateSensorProgressBar(quality);
                        }
                    });
                    break;
                // case 4:
                // byte[] enrollcmd = (byte[]) message.getMessage();
            }
        } catch (Exception e) {
            alert(e.getMessage());
        }
    }

    private void updateSensorMessage(String sensorMessage) {
        try {

            textViewMessage.setText(sensorMessage);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @SuppressLint("SimpleDateFormat")
    public void getAndWriteFFDLogs() {
        String ffdLogs = morphoDevice.getFFDLogs();

        if (ffdLogs != null) {
            String serialNbr = ProcessInfo.getInstance().getMSOSerialNumber();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String currentDateandTime = sdf.format(new Date());
            String saveFile = "sdcard/" + serialNbr + "_" + currentDateandTime
                    + "_Audit.log";

            try {
                FileWriter fstream = new FileWriter(saveFile, true);
                BufferedWriter out = new BufferedWriter(fstream);
                out.write(ffdLogs);
                out.close();
            } catch (IOException e) {
                Log.e("getAndWriteFFDLogs", e.getMessage());
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private String convertToInternationalMessage(int iCodeError,
                                                 int iInternalError) {
        switch (iCodeError) {
            case ErrorCodes.MORPHO_OK:
                return getString(R.string.MORPHO_OK);
            case ErrorCodes.MORPHOERR_INTERNAL:
                return getString(R.string.MORPHOERR_INTERNAL);
            case ErrorCodes.MORPHOERR_PROTOCOLE:
                return getString(R.string.MORPHOERR_PROTOCOLE);
            case ErrorCodes.MORPHOERR_CONNECT:
                return getString(R.string.MORPHOERR_CONNECT);
            case ErrorCodes.MORPHOERR_CLOSE_COM:
                return getString(R.string.MORPHOERR_CLOSE_COM);
            case ErrorCodes.MORPHOERR_BADPARAMETER:
                return getString(R.string.MORPHOERR_BADPARAMETER);
            case ErrorCodes.MORPHOERR_MEMORY_PC:
                return getString(R.string.MORPHOERR_MEMORY_PC);
            case ErrorCodes.MORPHOERR_MEMORY_DEVICE:
                return getString(R.string.MORPHOERR_MEMORY_DEVICE);
            case ErrorCodes.MORPHOERR_NO_HIT:
                return getString(R.string.MORPHOERR_NO_HIT);
            case ErrorCodes.MORPHOERR_STATUS:
                return getString(R.string.MORPHOERR_STATUS);
            case ErrorCodes.MORPHOERR_DB_FULL:
                return getString(R.string.MORPHOERR_DB_FULL);
            case ErrorCodes.MORPHOERR_DB_EMPTY:
                return getString(R.string.MORPHOERR_DB_EMPTY);
            case ErrorCodes.MORPHOERR_ALREADY_ENROLLED:
                return getString(R.string.MORPHOERR_ALREADY_ENROLLED);
            case ErrorCodes.MORPHOERR_BASE_NOT_FOUND:
                return getString(R.string.MORPHOERR_BASE_NOT_FOUND);
            case ErrorCodes.MORPHOERR_BASE_ALREADY_EXISTS:
                return getString(R.string.MORPHOERR_BASE_ALREADY_EXISTS);
            case ErrorCodes.MORPHOERR_NO_ASSOCIATED_DB:
                return getString(R.string.MORPHOERR_NO_ASSOCIATED_DB);
            case ErrorCodes.MORPHOERR_NO_ASSOCIATED_DEVICE:
                return getString(R.string.MORPHOERR_NO_ASSOCIATED_DEVICE);
            case ErrorCodes.MORPHOERR_INVALID_TEMPLATE:
                return getString(R.string.MORPHOERR_INVALID_TEMPLATE);
            case ErrorCodes.MORPHOERR_NOT_IMPLEMENTED:
                return getString(R.string.MORPHOERR_NOT_IMPLEMENTED);
            case ErrorCodes.MORPHOERR_TIMEOUT:
                return getString(R.string.MORPHOERR_TIMEOUT);
            case ErrorCodes.MORPHOERR_NO_REGISTERED_TEMPLATE:
                return getString(R.string.MORPHOERR_NO_REGISTERED_TEMPLATE);
            case ErrorCodes.MORPHOERR_FIELD_NOT_FOUND:
                return getString(R.string.MORPHOERR_FIELD_NOT_FOUND);
            case ErrorCodes.MORPHOERR_CORRUPTED_CLASS:
                return getString(R.string.MORPHOERR_CORRUPTED_CLASS);
            case ErrorCodes.MORPHOERR_TO_MANY_TEMPLATE:
                return getString(R.string.MORPHOERR_TO_MANY_TEMPLATE);
            case ErrorCodes.MORPHOERR_TO_MANY_FIELD:
                return getString(R.string.MORPHOERR_TO_MANY_FIELD);
            case ErrorCodes.MORPHOERR_MIXED_TEMPLATE:
                return getString(R.string.MORPHOERR_MIXED_TEMPLATE);
            case ErrorCodes.MORPHOERR_CMDE_ABORTED:
                return getString(R.string.MORPHOERR_CMDE_ABORTED);
            case ErrorCodes.MORPHOERR_INVALID_PK_FORMAT:
                return getString(R.string.MORPHOERR_INVALID_PK_FORMAT);
            case ErrorCodes.MORPHOERR_SAME_FINGER:
                return getString(R.string.MORPHOERR_SAME_FINGER);
            case ErrorCodes.MORPHOERR_OUT_OF_FIELD:
                return getString(R.string.MORPHOERR_OUT_OF_FIELD);
            case ErrorCodes.MORPHOERR_INVALID_USER_ID:
                return getString(R.string.MORPHOERR_INVALID_USER_ID);
            case ErrorCodes.MORPHOERR_INVALID_USER_DATA:
                return getString(R.string.MORPHOERR_INVALID_USER_DATA);
            case ErrorCodes.MORPHOERR_FIELD_INVALID:
                return getString(R.string.MORPHOERR_FIELD_INVALID);
            case ErrorCodes.MORPHOERR_USER_NOT_FOUND:
                return getString(R.string.MORPHOERR_USER_NOT_FOUND);
            case ErrorCodes.MORPHOERR_COM_NOT_OPEN:
                return getString(R.string.MORPHOERR_COM_NOT_OPEN);
            case ErrorCodes.MORPHOERR_ELT_ALREADY_PRESENT:
                return getString(R.string.MORPHOERR_ELT_ALREADY_PRESENT);
            case ErrorCodes.MORPHOERR_NOCALLTO_DBQUERRYFIRST:
                return getString(R.string.MORPHOERR_NOCALLTO_DBQUERRYFIRST);
            case ErrorCodes.MORPHOERR_USER:
                return getString(R.string.MORPHOERR_USER);
            case ErrorCodes.MORPHOERR_BAD_COMPRESSION:
                return getString(R.string.MORPHOERR_BAD_COMPRESSION);
            case ErrorCodes.MORPHOERR_SECU:
                return getString(R.string.MORPHOERR_SECU);
            case ErrorCodes.MORPHOERR_CERTIF_UNKNOW:
                return getString(R.string.MORPHOERR_CERTIF_UNKNOW);
            case ErrorCodes.MORPHOERR_INVALID_CLASS:
                return getString(R.string.MORPHOERR_INVALID_CLASS);
            case ErrorCodes.MORPHOERR_USB_DEVICE_NAME_UNKNOWN:
                return getString(R.string.MORPHOERR_USB_DEVICE_NAME_UNKNOWN);
            case ErrorCodes.MORPHOERR_CERTIF_INVALID:
                return getString(R.string.MORPHOERR_CERTIF_INVALID);
            case ErrorCodes.MORPHOERR_SIGNER_ID:
                return getString(R.string.MORPHOERR_SIGNER_ID);
            case ErrorCodes.MORPHOERR_SIGNER_ID_INVALID:
                return getString(R.string.MORPHOERR_SIGNER_ID_INVALID);
            case ErrorCodes.MORPHOERR_FFD:
                return getString(R.string.MORPHOERR_FFD);
            case ErrorCodes.MORPHOERR_MOIST_FINGER:
                return getString(R.string.MORPHOERR_MOIST_FINGER);
            case ErrorCodes.MORPHOERR_NO_SERVER:
                return getString(R.string.MORPHOERR_NO_SERVER);
            case ErrorCodes.MORPHOERR_OTP_NOT_INITIALIZED:
                return getString(R.string.MORPHOERR_OTP_NOT_INITIALIZED);
            case ErrorCodes.MORPHOERR_OTP_PIN_NEEDED:
                return getString(R.string.MORPHOERR_OTP_PIN_NEEDED);
            case ErrorCodes.MORPHOERR_OTP_REENROLL_NOT_ALLOWED:
                return getString(R.string.MORPHOERR_OTP_REENROLL_NOT_ALLOWED);
            case ErrorCodes.MORPHOERR_OTP_ENROLL_FAILED:
                return getString(R.string.MORPHOERR_OTP_ENROLL_FAILED);
            case ErrorCodes.MORPHOERR_OTP_IDENT_FAILED:
                return getString(R.string.MORPHOERR_OTP_IDENT_FAILED);
            case ErrorCodes.MORPHOERR_NO_MORE_OTP:
                return getString(R.string.MORPHOERR_NO_MORE_OTP);
            case ErrorCodes.MORPHOERR_OTP_NO_HIT:
                return getString(R.string.MORPHOERR_OTP_NO_HIT);
            case ErrorCodes.MORPHOERR_OTP_ENROLL_NEEDED:
                return getString(R.string.MORPHOERR_OTP_ENROLL_NEEDED);
            case ErrorCodes.MORPHOERR_DEVICE_LOCKED:
                return getString(R.string.MORPHOERR_DEVICE_LOCKED);
            case ErrorCodes.MORPHOERR_DEVICE_NOT_LOCK:
                return getString(R.string.MORPHOERR_DEVICE_NOT_LOCK);
            case ErrorCodes.MORPHOERR_OTP_LOCK_GEN_OTP:
                return getString(R.string.MORPHOERR_OTP_LOCK_GEN_OTP);
            case ErrorCodes.MORPHOERR_OTP_LOCK_SET_PARAM:
                return getString(R.string.MORPHOERR_OTP_LOCK_SET_PARAM);
            case ErrorCodes.MORPHOERR_OTP_LOCK_ENROLL:
                return getString(R.string.MORPHOERR_OTP_LOCK_ENROLL);
            case ErrorCodes.MORPHOERR_FVP_MINUTIAE_SECURITY_MISMATCH:
                return getString(R.string.MORPHOERR_FVP_MINUTIAE_SECURITY_MISMATCH);
            case ErrorCodes.MORPHOERR_FVP_FINGER_MISPLACED_OR_WITHDRAWN:
                return getString(R.string.MORPHOERR_FVP_FINGER_MISPLACED_OR_WITHDRAWN);
            case ErrorCodes.MORPHOERR_LICENSE_MISSING:
                return getString(R.string.MORPHOERR_LICENSE_MISSING);
            case ErrorCodes.MORPHOERR_CANT_GRAN_PERMISSION_USB:
                return getString(R.string.MORPHOERR_CANT_GRAN_PERMISSION_USB);
            default:
                return String.format("Unknown error %d, Internal Error = %d",
                        iCodeError, iInternalError);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub

    }

    private void startFPSRDCapture() {

        Intent intentCapture = new Intent(eKYCActivity.this,
                ECSBioCaptureActivity.class);
        intentCapture.putExtra("OPERATION", "K");
        intentCapture.putExtra("DEVICETYPE", "F");
        // Environment values are S for Staging,
        // pp for pre-production and P for Production S for Staging
        intentCapture.putExtra("UIDAIENVIRONMENT", "P");
        intentCapture.putExtra("PFR", true);
        intentCapture.putExtra("DE", false);
        intentCapture.putExtra("LR", true);
        intentCapture.putExtra("KYCVER", "2.5");
        startActivityForResult(intentCapture, DEVICE_CAPTURE_REQUEST_CODE);

        // Intent intentCapture = new
        // Intent("in.gov.uidai.rdservice.fp.CAPTURE");
        //
        // PackageManager packageManager = getPackageManager();
        // List activities = packageManager.queryIntentActivities(intentCapture,
        // PackageManager.MATCH_DEFAULT_ONLY);
        // boolean isIntentSafe = activities.size() > 0;
        //
        // if (!isIntentSafe) {
        // showDialog(
        // "Error:",
        // "Fingerprint Scanner Support not available. Please install and configure RD Services for your device from google playstore");
        // return;
        // }
        // Opts opts = new Opts();
        // opts.setfCount("1");
        // opts.setfType("0");
        // opts.setFormat("0");
        // opts.setPidVer("2.0");
        // opts.setTimeout("10000");
        // opts.setPosh("UNKNOWN");
        // opts.setEnv("pp"); // Environment values are S for Staging, pp for
        // // pre-production and P for Production
        //
        // String wadh = null;
        //
        // //
        // ***************************************************************************************************************
        // // Do this for KYC ONLY
        // try {
        // wadh = Base64.encodeToString(
        // getKycWadh("2.1", true, false, false, true, true, true,
        // false), Base64.NO_WRAP);
        // } catch (Exception ex) {
        // ex.printStackTrace();
        // }
        // opts.setWadh(wadh);
        // //
        // ***************************************************************************************************************
        //
        // CustOpts custOpts = new CustOpts();
        // custOpts.setParams(new ArrayList<Param>());
        //
        // Demo demo = null;
        //
        // PidOptions pidOptions = new PidOptions();
        // pidOptions.setVer("1.0");
        // pidOptions.setOpts(opts);
        // pidOptions.setCustOpts(custOpts);
        // pidOptions.setDemo(new Demo());
        // String pidOptXml = "";
        //
        // try {
        // pidOptXml = getXMLNoFormatting(pidOptions);
        // } catch (Exception ex) {
        // showDialog("Error:", ex.getMessage());
        // return;
        // }
        //
        // pidOptXml = pidOptXml.replace("<CustOpts/>",
        // "<CustOpts></CustOpts>");
        // pidOptXml = pidOptXml.replace("<Demo/>", "<Demo></Demo>");
        //
        // // Log.d("PIDOptions", pidOptXml);
        //
        // intentCapture.putExtra("PID_OPTIONS", pidOptXml);
        // startActivityForResult(intentCapture, DEVICE_CAPTURE_REQUEST_CODE);
    }

    private void StartRDIRISCapture() {
        Intent intentCapture = new Intent("in.gov.uidai.rdservice.iris.CAPTURE");

        PackageManager packageManager = getPackageManager();
        List activities = packageManager.queryIntentActivities(intentCapture,
                PackageManager.MATCH_DEFAULT_ONLY);
        boolean isIntentSafe = activities.size() > 0;

        if (!isIntentSafe) {
            showDialog(
                    "Error:",
                    "IRIS Scanner Support not available. Please update the latest software in your device");
            return;
        }

        int numeye = 2;
        if (singleChecked)
            numeye = 1;

        Opts opts = new Opts();
        opts.setiCount(String.valueOf(numeye));
        opts.setiType("0");
        opts.setFormat("0");
        opts.setPidVer("2.0");
        opts.setTimeout("10000");
        opts.setPosh("UNKNOWN");
        // Environment values are S for Staging,
        // pp for pre-production and P for Production and S For Staging
        opts.setEnv("P");

        String wadh = null;

        // ***************************************************************************************************************
        // Do this for KYC ONLY
        try {

            /*wadh = Base64.encodeToString(
                    getKycWadh("2.1", false, true, false, true, true, true,
                            true), Base64.NO_WRAP);*/
            //eKYC 2.5
            wadh = Base64.encodeToString(
                    getKycWadh("2.5", false, true, false, true, true, false,
                            true), Base64.NO_WRAP);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        opts.setWadh(wadh);
        // ***************************************************************************************************************

        CustOpts custOpts = new CustOpts();
        custOpts.setParams(new ArrayList<Param>());

        Demo demo = null;

        PidOptions pidOptions = new PidOptions();
        pidOptions.setVer("1.0");
        pidOptions.setOpts(opts);
        pidOptions.setCustOpts(custOpts);
        pidOptions.setDemo(new Demo());
        String pidOptXml = "";

        try {
            pidOptXml = getXMLNoFormatting(pidOptions);
        } catch (Exception ex) {
            showDialog("Error:", ex.getMessage());
            return;
        }

        pidOptXml = pidOptXml.replace("<CustOpts/>", "<CustOpts></CustOpts>");
        pidOptXml = pidOptXml.replace("<Demo/>", "<Demo></Demo>");

        // Log.d("PIDOptions", pidOptXml);

        intentCapture.putExtra("PID_OPTIONS", pidOptXml);
        startActivityForResult(intentCapture, IRIS_CAPTURE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == DEVICE_CAPTURE_REQUEST_CODE) {

                if (resultCode != AppCompatActivity.RESULT_OK) {
                    String errorMessage = data.getStringExtra("ERROR_MESSAGE");
                    if (errorMessage.contains("Device not registered")) {
                        Toast.makeText(eKYCActivity.this,
                                "wait Registeration Process will Start",
                                Toast.LENGTH_LONG).show();

                        registeredIfRequired();

                        // showTokenDialog();
                        return;
                    } else if (errorMessage
                            .contains("device from google playstore")) {

                        showDialogGogoglePayStore("Error",
                                "Please install the Morpho RD service for your device from google play store");

                        // showTokenDialog();
                        return;
                    } else {
                        showDialog("Error", errorMessage);
                        return;
                    }
                }

                String pidDataXML = data.getStringExtra("PID_DATA");

                if (pidDataXML == null) {
                    showDialog("Error",
                            "Unable to capture fingerprint. pidData is Null");
                    return;
                }

                sbilife.com.pointofsale_bancaagency.ekyc.response.PidData pidData = (sbilife.com.pointofsale_bancaagency.ekyc.response.PidData) parseXML(
                        sbilife.com.pointofsale_bancaagency.ekyc.response.PidData.class, pidDataXML);

                if (pidData.getResp() == null) {
                    showDialog("Error",
                            "Capture fingerprint failed. Resp is NULL");
                    return;
                }

                if (!pidData.getResp().getErrCode().equals("0")) {
                    showDialog("Error", "Capture fingerprint failed. Reason: "
                            + pidData.getResp().getErrInfo());
                    return;
                }

                byte[] data_pidDataXML = pidDataXML.getBytes(StandardCharsets.UTF_8);

                pidDataXMLbase64 = Base64.encodeToString(data_pidDataXML,
                        Base64.DEFAULT);

                //ll_header_part.setVisibility(View.GONE);
                //ll_progress_layout.setVisibility(View.VISIBLE);
                showDialog(DIALOG_PROGRESS);


                onFingerPrintRDServiceCaptureSuccess();

            } else if (requestCode == IRIS_CAPTURE_REQUEST_CODE) {

                if (resultCode != AppCompatActivity.RESULT_OK) {
                    showDialog("Error", "RD Service Error. Code : "
                            + resultCode);
                    return;
                }

                String pidDataXML = data.getStringExtra("PID_DATA");

                if (pidDataXML == null) {
                    showDialog("Error",
                            "Unable to capture IRIS. pidData is Null");
                    return;
                }

                sbilife.com.pointofsale_bancaagency.ekyc.response.PidData pidData = (sbilife.com.pointofsale_bancaagency.ekyc.response.PidData) parseXML(sbilife.com.pointofsale_bancaagency.ekyc.response.PidData.class, pidDataXML);

                if (pidData.getResp() == null) {
                    showDialog("Error", "Capture IRIS failed. Resp is NULL");
                    return;
                }

                if (!pidData.getResp().getErrCode().equals("0")) {
                    showDialog("Error", "Capture IRIS failed. Reason: "
                            + pidData.getResp().getErrInfo());
                    return;
                }

                byte[] data_pidDataXML = pidDataXML.getBytes(StandardCharsets.UTF_8);

                pidDataXMLbase64 = Base64.encodeToString(data_pidDataXML,
                        Base64.DEFAULT);
                //ll_header_part.setVisibility(View.GONE);
                //ll_progress_layout.setVisibility(View.VISIBLE);
                showDialog(DIALOG_PROGRESS);

                onCaptureRDIRISSuccess();

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showDialog("Error", ex.getMessage());
            return;
        }
    }

    private void registeredIfRequired(String otp) {
        Intent intent = new Intent(
                "android.intent.action.SCL_RDSERVICE_OTP_RECIEVER");
        intent.putExtra("OTP", otp);
        intent.setPackage("com.scl.rdservice");
        sendBroadcast(intent);
        try {
            sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(eKYCActivity.this, "RD Service not installed",
                    Toast.LENGTH_LONG);
        }
    }

    private void registeredIfRequired() {
        Intent intent = new Intent(
                "android.intent.action.SCL_RDSERVICE_OTP_RECIEVER");
        intent.putExtra("OTP", "SCL-9999-8888-777");
        intent.setPackage("com.scl.rdservice");
        // sendBroadcast(intent);
        try {
            sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(eKYCActivity.this, "RD Service not installed",
                    Toast.LENGTH_LONG);
        }
    }

    private String getXMLNoFormatting(Object input) throws Exception {
        StringWriter xml = new StringWriter();
        Format format = new Format(0, Verbosity.LOW);

        Serializer serializer = new Persister(format);
        try {
            serializer.write(input, xml);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return xml.toString();
    }

    private Object parseXML(Class clazz, String xmlToParse) throws Exception {
        Serializer serializer = new Persister();
        Reader reader = new StringReader(xmlToParse);
        return serializer.read(clazz, reader, false);
    }

    private byte[] getKycWadh(String kycVersion, boolean fingerprint,
                              boolean iris, boolean otp, boolean rc, boolean lr, boolean de,
                              boolean pfr) throws Exception {
        String ra = "";

        if (fingerprint)
            ra += "F";
        if (iris)
            ra += "I";
        if (otp)
            ra += "O";

        if (ra.isEmpty())
            throw new Exception("Invalid value for Ra for generating Wadh");

        if (!rc)
            throw new Exception("Invalid Resident consent");

        String data = kycVersion + ra + (rc ? "Y" : "N") + (lr ? "Y" : "N")
                + (de ? "Y" : "N") + (pfr ? "Y" : "N");

        byte[] hash = null;

        MessageDigest digest;
        digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        hash = digest.digest(data.getBytes());

        return hash;
    }

    private void showDialog(String title, String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(eKYCActivity.this);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setMessage(text);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        Dialog dlg = builder.create();
        dlg.show();
    }

	/*private void showTokenDialog() {
		View dialogLayout = getLayoutInflater().inflate(R.layout.layout_token,
				null);
		AlertDialog.Builder builder = new AlertDialog.Builder(eKYCActivity.this);
		builder.setView(dialogLayout);
		final AlertDialog dialog = builder.create();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		final EditText edt_token = (EditText) dialogLayout
				.findViewById(R.id.edt_token);
		Button okBtn = (Button) dialogLayout.findViewById(R.id.ok_btn);

		okBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (edt_token.getText().toString().isEmpty()) {

					Toast.makeText(eKYCActivity.this, "Please Enter the Token",
							Toast.LENGTH_LONG).show();
				} else {
					registeredIfRequired(edt_token.getText().toString());
					if (dialog.isShowing())
						dialog.dismiss();
				}
			}
		});

	}*/

    // method to set a clearing a element
    private void clearFocusable(View v) {
        // TODO Auto-generated method stub
        v.setFocusable(false);
        v.setFocusableInTouchMode(false);
        // v.clearFocus();
    }

    private void showDialogGogoglePayStore(String title, String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(eKYCActivity.this);
        builder.setTitle(title);
        builder.setMessage(text);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                mCommonMethods.openWebLink(mContext, "https://play.google.com/store/apps/details?id=com.scl.rdservice&hl=en");
            }
        });
        Dialog dlg = builder.create();
        dlg.show();
    }

    private void parse_show_ekyc_details(Context con, final String strData) {

        //parse ekyc responce

        if (strData != null) {

            try {
                final ECSKycResponse res = (ECSKycResponse) XMLUtilities
                        .parseXML(ECSKycResponse.class, strData);

                // ECSKycResponse res = (ECSKycResponse) data
                // .getSerializableExtra("KYC_XML");

                // UidData uidData;

                String str_ekyc_pic = res.getPoi().getPhoto() == null ? "" : res.getPoi().getPhoto();

                String str_Address_title = res.getPoa().getCo() == null ? ""
                        : res.getPoa().getCo();
                String str_aad_Address1 = (res.getPoa().getLoc() == null ? ""
                        : res.getPoa().getLoc() + " ")
                        + (res.getPoa().getHouse() == null ? "" : res
                        .getPoa().getHouse() + " ")
                        + (res.getPoa().getStreet() == null ? "" : res
                        .getPoa().getStreet() + " ")
                        + (res.getPoa().getLm() == null ? "" : res.getPoa()
                        .getLm());
                String str_aad_Address2 = (res.getPoa().getSubdist() == null ? ""
                        : res.getPoa().getSubdist() + " ")
                        + (res.getPoa().getPo() == null ? "" : res.getPoa()
                        .getPo() + " ");
                String str_aad_Address3 = (res.getPoa().getVtc() == null ? ""
                        : res.getPoa().getVtc());
                String str_PinCode = (res.getPoa().getPc() == null ? ""
                        : res.getPoa().getPc());
                String str_City = (res.getPoa().getDist() == null ? ""
                        : res.getPoa().getDist());
                String str_State = (res.getPoa().getState() == null ? ""
                        : res.getPoa().getState());
                String str_prop_aadhar_email_id = res.getPoi().getEmail() == null ? ""
                        : res.getPoi().getEmail();
                String str_prop_aadhar_mobile_no = res.getPoi().getPhone() == null ? ""
                        : res.getPoi().getPhone();
                String str_prop_aadhar_dob = res.getPoi().getDob() == null ? ""
                        : res.getPoi().getDob();
                String str_prop_aadhar_name = res.getPoi().getDob() == null ? ""
                        : res.getPoi().getName();
                String str_prop_aadhar_number = res.getPoi().getAadhaarNumber() == null ? ""
                        : res.getPoi().getAadhaarNumber();
                String str_prop_aadhar_gender = res.getPoi().getGender() == null ? ""
                        : res.getPoi().getGender();

                /*map_result.put("aadhar_no", str_prop_aadhar_number);
                map_result.put("aadhar_name", str_prop_aadhar_name);*/

                boolean isValidDate = isThisDateValid(str_prop_aadhar_dob);
                if (!isValidDate) {
                    str_prop_aadhar_dob = "01-01-1990";
                }
                //map_result.put("aadhar_dob", str_prop_aadhar_dob);
                String str_ekyc_address = str_Address_title + "\n"
                        + str_aad_Address1 + "\n" + str_aad_Address2
                        + str_aad_Address3 + "\n" + str_PinCode + "\n"
                        + str_City + "\n" + str_State;

                final Dialog dialog = new Dialog(con);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_ekyc_details);
                dialog.setCancelable(false);

                // Get screen width and height in pixels
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                // The absolute width of the available display size in pixels.
                int displayWidth = displayMetrics.widthPixels;
                // The absolute height of the available display size in pixels.
                //int displayHeight = displayMetrics.heightPixels;

                // Initialize a new window manager layout parameters
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

                // Copy the alert dialog window attributes to new layout parameter instance
                layoutParams.copyFrom(dialog.getWindow().getAttributes());

                // Set alert dialog width equal to screen width 70%
                // Set alert dialog height equal to screen height 70%
                //int dialogWindowHeight = (int) (displayHeight * 0.7f);

                // Set the width and height for the layout parameters
                // This will bet the width and height of alert dialog
                layoutParams.width = (int) (displayWidth * 0.7f);
                //layoutParams.height = dialogWindowHeight;

                // Apply the newly created layout parameters to the alert dialog window
                dialog.getWindow().setAttributes(layoutParams);

                //image
                ImageView iv_ekyc_pic = dialog.findViewById(R.id.iv_ekyc_pic);
                if (str_ekyc_pic.equals("")) {
                    iv_ekyc_pic.setVisibility(View.GONE);
                } else {
                    iv_ekyc_pic.setVisibility(View.VISIBLE);
                    //decode base64 string to image
                    byte[] imageBytes = Base64.decode(str_ekyc_pic, Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    iv_ekyc_pic.setImageBitmap(decodedImage);
                }

                //name
                LinearLayout ll_ekyc_name = dialog.findViewById(R.id.ll_ekyc_name);
                TextView txt_ekyc_name = dialog.findViewById(R.id.txt_ekyc_name);
                if (str_prop_aadhar_name.equals("")) {
                    ll_ekyc_name.setVisibility(View.GONE);
                } else {
                    ll_ekyc_name.setVisibility(View.VISIBLE);
                    txt_ekyc_name.setText(str_prop_aadhar_name);
                }

                //DOB
                LinearLayout ll_ekyc_dob = dialog.findViewById(R.id.ll_ekyc_dob);
                TextView txt_ekyc_dob = dialog.findViewById(R.id.txt_ekyc_dob);
                if (str_prop_aadhar_dob.equals("")) {
                    ll_ekyc_dob.setVisibility(View.GONE);
                } else {
                    ll_ekyc_dob.setVisibility(View.VISIBLE);
                    txt_ekyc_dob.setText(str_prop_aadhar_dob);
                }

                //Gender
                LinearLayout ll_ekyc_gender = dialog.findViewById(R.id.ll_ekyc_gender);
                TextView txt_ekyc_gender = dialog.findViewById(R.id.txt_ekyc_gender);
                if (str_prop_aadhar_gender.equals("")) {
                    ll_ekyc_gender.setVisibility(View.GONE);
                } else {
                    ll_ekyc_gender.setVisibility(View.VISIBLE);
                    txt_ekyc_gender.setText(str_prop_aadhar_gender);
                }

                //Mobile
                LinearLayout ll_ekyc_mobile = dialog.findViewById(R.id.ll_ekyc_mobile);
                TextView txt_ekyc_mobile = dialog.findViewById(R.id.txt_ekyc_mobile);
                if (str_prop_aadhar_mobile_no.equals("")) {
                    ll_ekyc_mobile.setVisibility(View.GONE);
                } else {
                    ll_ekyc_mobile.setVisibility(View.VISIBLE);
                    txt_ekyc_mobile.setText(str_prop_aadhar_mobile_no);
                }

                //email
                LinearLayout ll_ekyc_email = dialog.findViewById(R.id.ll_ekyc_email);
                TextView txt_ekyc_email = dialog.findViewById(R.id.txt_ekyc_email);
                if (str_prop_aadhar_email_id.equals("")) {
                    ll_ekyc_email.setVisibility(View.GONE);
                } else {
                    ll_ekyc_email.setVisibility(View.VISIBLE);
                    txt_ekyc_email.setText(str_prop_aadhar_email_id);
                }

                //Address
                LinearLayout ll_ekyc_address = dialog.findViewById(R.id.ll_ekyc_address);
                TextView txt_ekyc_address = dialog.findViewById(R.id.txt_ekyc_address);
                if (str_ekyc_address.equals("")) {
                    ll_ekyc_address.setVisibility(View.GONE);
                } else {
                    ll_ekyc_address.setVisibility(View.VISIBLE);
                    txt_ekyc_address.setText(str_ekyc_address);
                }

                Button btn_save_ekyc_details = dialog.findViewById(R.id.btn_save_ekyc_details);
                btn_save_ekyc_details.setText("Ok");
                btn_save_ekyc_details.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        //save data

                        /*try {

                            *//* KYC Encrypted response *//*
                            String str_encyptedRes = SimpleCrypto
                                    .encrypt("SBIL",
                                            strData);

                            int Str_AppVersion = 0;
                            PackageManager manager = mContext.getPackageManager();
                            PackageInfo info;
                            try {
                                info = manager.getPackageInfo(mContext.getPackageName(), 0);
                                Str_AppVersion = info.versionCode;
                            } catch (PackageManager.NameNotFoundException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                                Str_AppVersion = 0;
                            }

                            ContentValues cv = new ContentValues();
                            cv.put(DatabaseHelper.EKYC_PS_CLAIMS_EKYC_RESULT, str_encyptedRes);
                            cv.put(DatabaseHelper.EKYC_PS_CLAIMS_APP_VERSION, Str_AppVersion + "");
                            cv.put(DatabaseHelper.EKYC_PS_CLAIMS_EKYC_STATUS, 1);

                            int i = db.update_eKYC_PS_Claims_Details(cv, new String[]{ProposalNumber, customer_AadharNumber});

                            if (i == -2) {
                                Toast.makeText(mContext,
                                        "KYC Details Saving Failed",
                                        Toast.LENGTH_LONG).show();
                            } else {

                                //flag to success ekyc
                                isSuccessEkyc = true;

                                Toast.makeText(mContext,
                                        "KYC Details Added Successfully",
                                        Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                            Toast.makeText(mContext,
                                    "KYC Details Saving Failed",
                                    Toast.LENGTH_LONG).show();
                        }*/


                        mCommonMethods.showToast(mContext, "Ekyc Done Successfully!!");

                        if (str_from.equalsIgnoreCase("NA")) {
                            /*new NeedAnalysisBIService(mContext).serviceHit(eKYCActivity.this,
                                    ekyc_na_cbi_bean, newFile, str_need_analysis_path,
                                    str_my_path, str_name_of_person, str_quote_number, str_mode);*/
                        } else {
                            Intent i = new Intent(eKYCActivity.this, CarouselHomeActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        }

                        dialog.dismiss();
                    }
                });
                dialog.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class Asynch_link_aadhar_details extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String str_output = "", str_ekyc = "", str_error = "", str_prop_aadhar_number = "",
                str_uid_token = "", strUserCode = "", strUserType = "", strSource = "", strPAFlag ="",
                strPan = "";
        //date conversion
        final SimpleDateFormat dd_mm_yyyy_format = new SimpleDateFormat("dd-MM-yyyy");
        final SimpleDateFormat mm_dd_yyyy_format = new SimpleDateFormat("MM/dd/yyyy");

        @Override
        protected void onPreExecute() {
            showDialog(DIALOG_PROGRESS);
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                running = true;
                str_ekyc = params[0];
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_LINK_AADHAR_DETAILS);

                if (ProposalNumber.trim().length() == 10){
                    request.addProperty("strPropNo", ProposalNumber);//10digit
                    request.addProperty("strPolNo", "");//11digit
                }else if (ProposalNumber.trim().length() == 11){
                    request.addProperty("strPropNo", "");//10digit
                    request.addProperty("strPolNo", ProposalNumber);//11digit
                }

                /*Cursor cur = db.get_eKYC_Details(ProposalNumber, customer_AadharNumber);*/
                Cursor cur = db.get_eKYC_Details(ProposalNumber);
                if (cur.moveToLast()){

                    strUserCode = cur.getInt(1) +"";
                    strUserType = cur.getString(2);
                    strSource = "Smart Advisor";
                    strPAFlag = cur.getString(11);

                    strPan = cur.getString(10).trim();

                    /*request.addProperty("strAadharNo", cur.getString(8).trim());*/

                    //str_ekyc = SimpleCrypto.decrypt("SBIL", cur.getString(13).toString());
                }
                cur.close();

                        try{
                            //String str_ekyc_pic = res.getPoi().getPhoto() == null ? "" : res.getPoi().getPhoto();


                            String str_kycResXML = str_ekyc +
                                    "<ConsentAccepted>" + "Y" + "</ConsentAccepted>" +
                                    "<BIOMETRICMODE>" + str_biometric_type + "</BIOMETRICMODE>";

                            //IIR or FMR or OTP

                            final ECSKycResponse res = (ECSKycResponse) XMLUtilities
                                    .parseXML(ECSKycResponse.class, str_kycResXML);

                            isSuccessEkyc = true;

                            String str_Address_title = res.getPoa().getCo() == null ? ""
                                    : res.getPoa().getCo();
                            String str_aad_Address1 = (res.getPoa().getLoc() == null ? ""
                                    : res.getPoa().getLoc() + " ")
                                    + (res.getPoa().getHouse() == null ? "" : res
                                    .getPoa().getHouse() + " ")
                                    + (res.getPoa().getStreet() == null ? "" : res
                                    .getPoa().getStreet() + " ")
                                    + (res.getPoa().getLm() == null ? "" : res.getPoa()
                                    .getLm());
                            String str_aad_Address2 = (res.getPoa().getSubdist() == null ? ""
                                    : res.getPoa().getSubdist() + " ")
                                    + (res.getPoa().getPo() == null ? "" : res.getPoa()
                                    .getPo() + " ");
                            String str_aad_Address3 = (res.getPoa().getVtc() == null ? ""
                                    : res.getPoa().getVtc());
                            String str_PinCode = (res.getPoa().getPc() == null ? ""
                                    : res.getPoa().getPc());
                            String str_City = (res.getPoa().getDist() == null ? ""
                                    : res.getPoa().getDist());
                            String str_State = (res.getPoa().getState() == null ? ""
                                    : res.getPoa().getState());
                            String str_prop_aadhar_email_id = res.getPoi().getEmail() == null ? ""
                                    : res.getPoi().getEmail();
                            String str_prop_aadhar_mobile_no = res.getPoi().getPhone() == null ? ""
                                    : res.getPoi().getPhone();
                            String str_prop_aadhar_dob = res.getPoi().getDob() == null ? ""
                                    : res.getPoi().getDob();

                            String str_prop_aadhar_name = res.getPoi().getDob() == null ? ""
                                    : res.getPoi().getName();
                            str_prop_aadhar_number = res.getPoi().getAadhaarNumber() == null ? ""
                                    : res.getPoi().getAadhaarNumber();

                    //ekyc 2.5 changes
                    str_uid_token = res.getPoi().getUidtoken() == null ? ""
                            : res.getPoi().getUidtoken();

                            String str_prop_aadhar_gender = res.getPoi().getGender() == null ? ""
                                    : res.getPoi().getGender();

                            boolean isValidDate = isThisDateValid(str_prop_aadhar_dob);
                            if (!isValidDate) {
                                str_prop_aadhar_dob = "01-01-1990";
                            }

                            try {
                                Date dt = dd_mm_yyyy_format.parse(str_prop_aadhar_dob);
                                str_prop_aadhar_dob = mm_dd_yyyy_format.format(dt);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            String str_ekyc_address = str_Address_title + "\n"
                                    + str_aad_Address1 + "\n" + str_aad_Address2
                                    + str_aad_Address3 + "\n" + str_PinCode + "\n"
                                    + str_City + "\n" + str_State;

                    request.addProperty("strAadharNo", str_prop_aadhar_number);
                    request.addProperty("strPan", strPan);
                            request.addProperty("strFirstName", "");
                            request.addProperty("strMiddleName", "");
                            request.addProperty("strLastName", "");
                            request.addProperty("strAddress", str_ekyc_address);
                            request.addProperty("strDob", str_prop_aadhar_dob);
                            request.addProperty("strGen", str_prop_aadhar_gender);
                            request.addProperty("strMailId", str_prop_aadhar_email_id);
                            request.addProperty("strMobileNo", str_prop_aadhar_mobile_no);
                            request.addProperty("strFullName", str_prop_aadhar_name);

                    request.addProperty("strUserType", strUserType);
                    request.addProperty("strUserCode", strUserCode);
                    request.addProperty("strSource", strSource);
                    request.addProperty("strPAFlag", strPAFlag);

                            request.addProperty("strAuthKey", "");
                            request.addProperty("strStatus", "Success");

                //new changes 16-03-2018
                if (str_from.equals("NA"))
                    request.addProperty("module_name", "NACBI");
                else if (str_from.equals("NB"))
                    request.addProperty("module_name", "NB");
                else if (str_from.equals("PS_CLAIM"))
                    request.addProperty("module_name", "PS");

                String str_user_type = mCommonMethods.GetUserType(mContext);
                if (str_user_type.equalsIgnoreCase("CIF") || str_user_type.equalsIgnoreCase("BDM")
                        || str_user_type.equalsIgnoreCase("AM") || str_user_type.equalsIgnoreCase("SAM")
                        || str_user_type.equalsIgnoreCase("ZAM"))
                    request.addProperty("channel_type", "Banca");
                else if (str_user_type.equalsIgnoreCase("AGENT") || str_user_type.equalsIgnoreCase("UM")
                        || str_user_type.equalsIgnoreCase("BSM") || str_user_type.equalsIgnoreCase("DSM"))
                    request.addProperty("channel_type", "Retail");

                    request.addProperty("uid_token", str_uid_token);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {

                    androidHttpTranport.call(SOAP_ACTION_LINK_AADHAR_DETAILS, envelope);
                    //Object response = envelope.getResponse();

                    SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();

                    str_output = sa.toString();

                    return str_output;

                } catch (Exception e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;

                        str_error = e.getMessage();
                        isSuccessEkyc = false;
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    str_error = e.getMessage();
                    isSuccessEkyc = false;
                }

            } catch (Exception e) {
                try {
                    throw (e);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                mProgressDialog.dismiss();
                running = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                if (str_output.equalsIgnoreCase("Success")) {
                    mCommonMethods.showToast(mContext, "Data Synched Successfully!!");

                    try{

                        int Str_AppVersion = 0;
                        PackageManager manager = mContext.getPackageManager();
                        PackageInfo info;
                        try {
                            info = manager.getPackageInfo(mContext.getPackageName(), 0);
                            Str_AppVersion = info.versionCode;
                        } catch (PackageManager.NameNotFoundException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                            Str_AppVersion = 0;
                        }

                        ContentValues cv = new ContentValues();

                        //show ekyc success or failure dialog

                        if (isSuccessEkyc){
                            /* KYC Encrypted response */
                            String str_encyptedRes = SimpleCrypto.encrypt("SBIL", str_ekyc);

                            cv.put(db.EKYC_PS_CLAIMS_AADHAAR_NUMBER, str_prop_aadhar_number);
                            cv.put(db.EKYC_PS_CLAIMS_UID_TOKEN_ID, str_uid_token);
                            cv.put(db.EKYC_PS_CLAIMS_EKYC_RESULT, str_encyptedRes);
                            cv.put(db.EKYC_PS_CLAIMS_APP_VERSION, Str_AppVersion + "");
                            cv.put(db.EKYC_PS_CLAIMS_EKYC_STATUS, 1);

                            /*db.update_eKYC_PS_Claims_Details(cv, new String[]{ProposalNumber, customer_AadharNumber});*/
                            db.update_eKYC_PS_Claims_Details(cv, new String[]{ProposalNumber});

                            //show_eKYC_error_MessageDialog("Proposal/Policy No. " + ProposalNumber + " has Successfully linked to aadhar.");

                            if (str_from.equals("NA") || str_from.equals("NB")) {
                                //parse_show_ekyc_details(mContext, str_ekyc);



                                try {
                                    final Dialog dialog = new Dialog(mContext);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setContentView(R.layout.dialog_with_ok_button);
                                    TextView text = dialog.findViewById(R.id.tv_title);
                                    text.setText("Ekyc Done Successfully!!");
                                    Button dialogButton = dialog.findViewById(R.id.bt_ok);
                                    dialogButton.setText("Ok");
                                    dialogButton.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            if (str_from.equalsIgnoreCase("NA")) {

                                            } else {
                                                Intent i = new Intent(eKYCActivity.this, CarouselHomeActivity.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(i);
                                                finish();
                                            }
                                        }
                                    });
                                    dialog.show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            show_eKYC_error_MessageDialog("Proposal/Policy No. " + ProposalNumber + " has Successfully linked to aadhar.");
                        }else{

                            cv.put(db.EKYC_PS_CLAIMS_EKYC_RESULT, "");
                            cv.put(db.EKYC_PS_CLAIMS_APP_VERSION, Str_AppVersion + "");
                            cv.put(db.EKYC_PS_CLAIMS_EKYC_STATUS, 0);

                            /*db.update_eKYC_PS_Claims_Details(cv, new String[]{ProposalNumber, customer_AadharNumber});*/
                            db.update_eKYC_PS_Claims_Details(cv, new String[]{ProposalNumber});

                            show_eKYC_error_MessageDialog(str_error+"\n"+"We have captured your data for validation purpose.");
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                } else {
                    mCommonMethods.showToast(mContext, str_output);
                }
                running = false;
                str_output = "";

            } else {

                mCommonMethods.showMessageDialog(mContext, mCommonMethods.SERVER_ERROR);
            }
        }
    }

    /* Service for OTP e kyc */
    private void onOTPSuccess() {

        //ll_header_part.setVisibility(View.GONE);
        // ll_progress_layout.setVisibility(View.VISIBLE);
        showDialog(DIALOG_PROGRESS);
        runOnUiThread(new Runnable() {
            public void run() {

                new Thread() {
                    public void run() {
                        try {
                            /*final TelephonyManager tm = (TelephonyManager) getBaseContext()
                                    .getSystemService(Context.TELEPHONY_SERVICE);

                            String str_IMEI_No = "" + tm.getDeviceId();*/

                            ECSOtpKycRequest request = new ECSOtpKycRequest();
                            request.setAadhaarNumber(customer_AadharNumber);

                            request.setOtp(str_OTP);
                            request.setOtpReqTxnId(str_otp_txn);
                            //request.setUdc(str_IMEI_No);
                            request.setLr(true);
                            request.setPfr(true);

                            String dataAsXml = XMLUtilities.getXML(request);
                            String str_kycXML = dataAsXml +
                                    "<AppName>" + "Smart Advisor"
                                    + "</AppName>" +
                                    "<ReqID>" + ProposalNumber + "-" + date1.getTime() + "</ReqID>" +
                                    "<KYCType>" + "OTP"
                                    + "</KYCType>";

                            /* str_kycXML is generated request XML */

                            final String resString  = changeUrl(str_kycXML);
                            if (mProgressDialog.isShowing())
                                mProgressDialog.dismiss();

                            str_biometric_type = "OTP";

                            //save to server
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    linkAadharDetails(resString);
                                }
                            });

                            /*if (resString.startsWith("<Error>")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // pd.dismiss();
                                        String errorMessage = getErrorMessage(resString);
                                        showErrorActivity(errorMessage);
                                    }
                                });
                                return;
                            }

                            if (resString.contains("<ECSKycResponse") == true) {
                                final ECSKycResponse res = (ECSKycResponse) XMLUtilities
                                        .parseXML(ECSKycResponse.class,
                                                resString);

                                if (res.getErr()) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            String errorMessage = res
                                                    .getErrCode()
                                                    + "-"
                                                    + res.getErrMsg();
                                            showErrorActivity(errorMessage);
                                        }
                                    });
                                    return;
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        System.out.println("Success");
                                        try {

                                            StringBuilder str_kycResXML = new StringBuilder();
                                            str_kycResXML.append(resString);

                                            str_kycResXML
                                                    .append("<ConsentAccepted>"
                                                            + "Y"
                                                            + "</ConsentAccepted>");

                                            str_kycResXML
                                                    .append("<BIOMETRICMODE>"
                                                            + "OTP"
                                                            + "</BIOMETRICMODE>");

                                        *//*long rowId = db.insertKYCDetails(
                                                str_encyptedRes,
                                                QuatationNumber,
                                                Str_AppVersion);
                                            Toast.makeText(
                                                    mContext,
                                                    "KYC Details Added Successfully",
                                                    Toast.LENGTH_LONG).show();*//*


                                            //show data on dialog

                                        } catch (Exception e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                            showErrorActivity("Error in KYC Response!\n"
                                                    + e.getMessage());
                                        }

                                    }
                                });
                            }*/

                            /********** KYC ***************************/

                        } catch (final Exception e) {
                            System.out.println("Inside Exception");
                            e.printStackTrace();
                            showErrorActivity("Error sending Kyc request!\n"
                                    + e.getMessage());
                        }
                    } // run()
                }.start(); // new Thread()

            }
        });

    }

    private void GenerateOTP() {

        showDialog(DIALOG_PROGRESS);

        AsyncGenerateOTP serviceGenerateOTP = new AsyncGenerateOTP();
        serviceGenerateOTP.execute();
    }

    public class AsyncGenerateOTP extends AsyncTask<String, Void, String> {
        private volatile boolean running = true;

        protected String doInBackground(String... param) {
            // TODO Auto-generated method stub

            if (mCommonMethods.isNetworkConnected(mContext)) {
                try {
                    running = true;
                    /*final TelephonyManager tm = (TelephonyManager) getBaseContext()
                            .getSystemService(Context.TELEPHONY_SERVICE);

                    String str_IMEI_No = "" + tm.getDeviceId();*/
                    ECSOtpRequest request = new ECSOtpRequest();
                    request.setAadhaarNumber(customer_AadharNumber);
                    request.setSms(true);
                    request.setEmail(true);
                    String dataAsXml = XMLUtilities.getXML(request);
                    String str_kycXML = dataAsXml +
                            "<AppName>" + "Smart Advisor"
                            + "</AppName>" +
                            "<ReqID>" + ProposalNumber + "-" + date1.getTime() + "</ReqID>" +
                            "<KYCType>" + "OTP"
                            + "</KYCType>";

                    /* str_kycXML is generated request XML */

                    resGenerateOTPString = changeUrl(str_kycXML);

                } catch (Exception e) {
                    e.printStackTrace();
                    running = false;
                    return "Server not responding,Please try again";
                }
            } else
                return "Please Activate Internet connection";

            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {
                ECSOtpResponse ores = null;
                if (resGenerateOTPString.startsWith("<Error>")) {

                    String errorMessage = getErrorMessage(resGenerateOTPString);
                    showErrorActivity(errorMessage);

                } else if (resGenerateOTPString.contains("<ECSOtpResponse")) {

                    try {

                        // ores = (ECSOtpResponse) XMLUtilities.deserialize(
                        // ECSOtpResponse.class, resGenerateOTPString);
                        ores = (ECSOtpResponse) XMLUtilities.parseXML(
                                ECSOtpResponse.class, resGenerateOTPString);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if (ores.getErr()) {
                        String errorMessage = ores.getErrCode() + "-"
                                + ores.getErrMsg();
                        showErrorActivity(errorMessage);
                    } else {
                        str_otp_txn = ores.getTxn();
                        if (ores.getMaskedEmail() == null
                                && ores.getMaskedMobile() == null)

                            Toast.makeText(mContext,
                                    "OTP Generated successfully",
                                    Toast.LENGTH_LONG).show();

                        else if (ores.getMaskedEmail() == null
                                && ores.getMaskedMobile() != null)

                            Toast.makeText(
                                    mContext,
                                    String.format(
                                            "OTP sent to Mobile Number %s",
                                            ores.getMaskedMobile()),
                                    Toast.LENGTH_LONG).show();

                        else if (ores.getMaskedEmail() != null
                                && ores.getMaskedMobile() == null)

                            Toast.makeText(
                                    mContext,
                                    String.format("OTP sent to Email Id %s",
                                            ores.getMaskedEmail()),
                                    Toast.LENGTH_LONG).show();

                        else

                            Toast.makeText(
                                    mContext,
                                    String.format(
                                            "OTP sent to Mobile Number %s Email Id %s",
                                            ores.getMaskedMobile(),
                                            ores.getMaskedEmail()),
                                    Toast.LENGTH_LONG).show();

                    }

                }

            } else {
                Toast.makeText(mContext,
                        "Server not responding,Please try again",
                        Toast.LENGTH_LONG).show();
            }
        }

    }

    private void show_eKYC_error_MessageDialog(String message) {
        try {
            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_with_ok_button);
            TextView text = dialog.findViewById(R.id.tv_title);
            text.setText(message);
            Button dialogButton = dialog.findViewById(R.id.bt_ok);
            dialogButton.setText("Ok");
            dialogButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();

                    //if (isSuccessEkyc){
                        //navigate to ekyc main Activity
                        /*Intent i = new Intent(eKYCActivity.this, EkycPSClaimsActivity.class);
                        i.putExtra("POLICY_NUM", "");
                        startActivity(i);*/

                    if (str_from.equalsIgnoreCase("NA")) {
                        /*new NeedAnalysisBIService(mContext).serviceHit(eKYCActivity.this,
                                ekyc_na_cbi_bean, newFile, str_need_analysis_path,
                                str_my_path, str_name_of_person, str_quote_number, str_mode);*/
                    } else {
                        Intent i = new Intent(eKYCActivity.this, CarouselHomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                    //}
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isThisDateValid(String dateToValidate) {

        if (dateToValidate == null) {
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false);

        try {
            // if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void linkAadharDetails(String resString){
        if (resString.startsWith("<Error>")){
            mCommonMethods.showMessageDialog(mContext,"Ekyc failed");
        }else if(resString.contains("<ECSKycResponse")) {

            ECSKycResponse res1 = null;
            try {
                res1 = (ECSKycResponse) XMLUtilities
                        .parseXML(ECSKycResponse.class, resString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (res1!=null && res1.getErr()) {
                mCommonMethods.showMessageDialog(mContext,"Ekyc failed");
            } else {
                mAsynch_link_aadhar_details = new Asynch_link_aadhar_details();
                mAsynch_link_aadhar_details.execute(resString);
            }
        }
    }

    private String changeUrl(String str_kycXML) throws Exception{
        //for production
        //String resString = HttpConnector.getInstance().postData(str_kycXML);

        //for staging
         String resString = HttpConnector.getInstance().eKYCWrapper(str_kycXML);
        return resString ;
    }
}
