package sbilife.com.pointofsale_bancaagency.home.mhr;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.itextpdf.text.BaseColor;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.branchlocator.GPSTracker;
import sbilife.com.pointofsale_bancaagency.common.AsyncUploadFile_Common;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CompressImage;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class MHRActivity extends AppCompatActivity implements View.OnClickListener,
        ServiceHits.DownLoadData, RadioGroup.OnCheckedChangeListener, AsyncUploadFile_Common.Interface_Upload_File_Common {
    private final String NAMESPACE = "http://tempuri.org/";
    private final String URl = ServiceURL.SERVICE_URL;
    // private final String METHOD_NAME = "getACRdetail_smrt";
//    private final String METHOD_NAME = "getMHRdetail_smrt";
    private final String METHOD_NAME = "getMHRdetailShort_smrt";
    private final int REQUEST_CODE_PICK_PHOTO_FILE = 3;
    private ProgressDialog mProgressDialog;
    private Context context;
    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;

    private EditText etProposalNumber, etOTP;
    private LinearLayout llOTP, llMHRProposalLayout, llShortMHRQuestions, llProposalListLayout,
            llCoverJustification, llIAClarifiation, llApproximateAge;

    private ArrayList<ParseXML.MHRReportValuesModel> globalDataList;
    private DownloadMHRReportAsync downloadMHRReportAsync;
    private String strCIFBDMUserId = "", strCIFBDMEmailId = "", strCIFBDMMObileNo = "", proposalNumber = "",
            OTP = "", meetLAProposerStatus = "", meetingPlace = "",
            meetingDate = "", LANotMeet = "", LASocialStatus = "", injuryStatus = "",
            satisfactionStatus = "", LAsocialDetailsString = "", injuryDetailsString = "",
            satisfactionDetailsString = "", Check = "", fromDate = "", toDate = "", userType = "",
            justifiationCoverApplied = "", IAClarifiation = "", approximateAge = "", approximateAgeDetails = "";
    private ServiceHits service;
    private AuthenticatePDFAsync authenticatePDFAsync;
    private GenerateOTPAsyncTask generateOTPAsyncTask;
    private ValidateOTPAsyncTask validateOTPAsyncTask;
    private AsyncUploadFile_Common mAsyncUploadFileCommon;
    private SaveMHRProposalService saveMHRProposalService;
    private Button buttonValidateOTP, buttonGenerateOTP;
    private CheckBox checkBoxDisclaimer;

    private int mYear, mMonth, mDay, datecheck = 0;
    private TextView tvCurrentDate, tvProposalNumber, tvProposalName, tvSumAssured,
            tvIAName, tvIACode, tvRegion, tvSUC, tvChannel, textViewFromDate, textViewToDate;
    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {

            mYear = year;
            mMonth = month;
            mDay = day;
            updateDisplay(mYear, mMonth, mDay);
        }
    };
    private MHRExtraUnderwritingDetailsAsync mhrExtraUnderwritingDetailsAsync;
    private String NBD_NOMINEE_RELATION_WITH_LA = "", NBD_MAILING_STATE = "", NBD_PERMANENT_STATE = "",
            NBD_PROP_QUALIFICATION = "", ST_ID = "";
    private EditText etApproximateAge, etCoverJustification, etIAClarifiation, etMeetingPlace, etLifeAssuredMeetNo,
            etInjuryIllnessDetails, etSatisfactionDetails, etLifeAssuredDetails;
    private ImageView imagePhotoGraphGPSCord;
    private Bitmap customerPhotoBitmap;
    private LatLng mLatLng;
    private GPSTracker gpsTracker;
    private File customerPhotoFileName;
    private Spinner spinnerProopsalNumbers;
    private int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_mhr);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        context = this;
        commonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        commonMethods.setApplicationToolbarMenu(this, "Short MHR");
        globalDataList = new ArrayList<>();
        etProposalNumber = findViewById(R.id.etProposalNumber);
        etOTP = findViewById(R.id.etOTP);

        llProposalListLayout = findViewById(R.id.llProposalListLayout);
        llProposalListLayout.setVisibility(View.GONE);

        llCoverJustification = findViewById(R.id.llCoverJustification);
        llIAClarifiation = findViewById(R.id.llIAClarifiation);
        llApproximateAge = findViewById(R.id.llApproximateAge);

        llCoverJustification.setVisibility(View.GONE);
        llIAClarifiation.setVisibility(View.GONE);
        llApproximateAge.setVisibility(View.GONE);

        llShortMHRQuestions = findViewById(R.id.llShortMHRQuestions);
        llShortMHRQuestions.setVisibility(View.GONE);
        llOTP = findViewById(R.id.llOTP);
        llMHRProposalLayout = findViewById(R.id.llMHRProposalLayout);

        Button buttonOk = findViewById(R.id.buttonOk);
        buttonValidateOTP = findViewById(R.id.buttonValidateOTP);
        buttonGenerateOTP = findViewById(R.id.buttonGenerateOTP);
        checkBoxDisclaimer = findViewById(R.id.checkBoxDisclaimer);


        tvCurrentDate = findViewById(R.id.tvCurrentDate);
        tvProposalNumber = findViewById(R.id.tvProposalNumber);
        tvProposalName = findViewById(R.id.tvProposalName);
        tvSumAssured = findViewById(R.id.tvSumAssured);
        tvIAName = findViewById(R.id.tvIAName);
        tvIACode = findViewById(R.id.tvIACode);
        tvRegion = findViewById(R.id.tvRegion);
        tvSUC = findViewById(R.id.tvSUC);
        tvChannel = findViewById(R.id.tvChannel);
        textViewFromDate = findViewById(R.id.textViewFromDate);
        textViewToDate = findViewById(R.id.textViewToDate);

        etMeetingPlace = findViewById(R.id.etMeetingPlace);
        etLifeAssuredMeetNo = findViewById(R.id.etLifeAssuredMeetNo);
        etInjuryIllnessDetails = findViewById(R.id.etInjuryIllnessDetails);
        etSatisfactionDetails = findViewById(R.id.etSatisfactionDetails);
        etLifeAssuredDetails = findViewById(R.id.etLifeAssuredDetails);
        etApproximateAge = findViewById(R.id.etApproximateAge);
        etCoverJustification = findViewById(R.id.etCoverJustification);
        etIAClarifiation = findViewById(R.id.etIAClarifiation);

        imagePhotoGraphGPSCord = findViewById(R.id.imagePhotoGraphGPSCord);
        spinnerProopsalNumbers = findViewById(R.id.spinnerProopsalNumbers);

        buttonOk.setOnClickListener(this);
        buttonGenerateOTP.setOnClickListener(this);
        buttonValidateOTP.setOnClickListener(this);
        tvCurrentDate.setOnClickListener(this);
        imagePhotoGraphGPSCord.setOnClickListener(this);


        mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        mProgressDialog.setButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (mProgressDialog != null) {
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }

                        if (downloadMHRReportAsync != null) {
                            downloadMHRReportAsync.cancel(true);
                        }
                        if (service != null) {
                            service.cancel(true);
                        }

                        if (generateOTPAsyncTask != null) {
                            generateOTPAsyncTask.cancel(true);
                        }
                        if (validateOTPAsyncTask != null) {
                            validateOTPAsyncTask.cancel(true);
                        }
                        if (authenticatePDFAsync != null) {
                            authenticatePDFAsync.cancel(true);
                        }

                        if (mAsyncUploadFileCommon != null) {
                            mAsyncUploadFileCommon.cancel(true);
                        }
                        if (saveMHRProposalService != null) {
                            saveMHRProposalService.cancel(true);
                        }
                    }
                });

        mProgressDialog.setMax(100);
        getUserDetails();
        userType = commonMethods.GetUserType(context);
        /*Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");
        if (fromHome != null && fromHome.equalsIgnoreCase("ProposalList")) {
           // llMHRProposalLayout.setVisibility(View.GONE);
            proposalNumber = intent.getStringExtra("ProposalNumber");
            if (TextUtils.isEmpty(proposalNumber)) {
                llMHRProposalLayout.setVisibility(View.VISIBLE);
            } else {
                etProposalNumber.setText(proposalNumber.trim());

                if (commonMethods.isNetworkConnected(context)) {
                    StringBuilder input = new StringBuilder();
                    proposalNumber = etProposalNumber.getText().toString();

                    input.append(proposalNumber);
                    service_hits(input.toString());
                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }
            }
        }*/

        RadioGroup radioGroupMHR, radioGroupLifeAssuredDetails, radioGroupInjuryIllnessDetails,
                radioGroupSatisfactionDetails, radioGroupLifeAssuredCurrentAgeStatus;
        radioGroupMHR = findViewById(R.id.radioGroupMHR);
        radioGroupLifeAssuredDetails = findViewById(R.id.radioGroupLifeAssuredDetails);
        radioGroupInjuryIllnessDetails = findViewById(R.id.radioGroupInjuryIllnessDetails);
        radioGroupSatisfactionDetails = findViewById(R.id.radioGroupSatisfactionDetails);
        radioGroupLifeAssuredCurrentAgeStatus = findViewById(R.id.radioGroupLifeAssuredCurrentAgeStatus);

        radioGroupMHR.setOnCheckedChangeListener(this);
        radioGroupLifeAssuredDetails.setOnCheckedChangeListener(this);
        radioGroupInjuryIllnessDetails.setOnCheckedChangeListener(this);
        radioGroupSatisfactionDetails.setOnCheckedChangeListener(this);
        radioGroupLifeAssuredCurrentAgeStatus.setOnCheckedChangeListener(this);

        gpsTracker = new GPSTracker(context);
        mLatLng = new LatLng(0.0, 0.0);

        textViewFromDate.setOnClickListener(this);
        textViewToDate.setOnClickListener(this);
        getLocationPromt();
        setDates();

         /*
                        if (globalDataList.get(position).getFRAUDULENT_FLAG().equalsIgnoreCase("yes")) {
                            proceedToShortMHR();
                        } else {
                            if (globalDataList.get(position).getExperience() < 1) {
                                proceedToShortMHR();
                            } else {
                                String clubWithType = globalDataList.get(position).getCLUB_WITH_PERSISTENCE();
                                String clubWithoutType = globalDataList.get(position).getCLUB_WITHOUT_PERSISTENCE();
                                long SUC = Long.valueOf(globalDataList.get(position).getSUC());
                                // long sumAssured = Long.valueOf(globalDataList.get(position) .getSUMASSURED());
                                if (clubWithType.equalsIgnoreCase("PRESIDENT CLUB") || clubWithoutType.equalsIgnoreCase("PRESIDENT CLUB")) {
                                    if (SUC > 2000000) {
                                        proceedToShortMHR();
                                    } else {
                                        commonMethods.showMessageDialog(context, "You are not eligible for MHR");
        }
                                } else if (clubWithType.equalsIgnoreCase("MD CLUB")
                                        || clubWithoutType.equalsIgnoreCase("MD CLUB")) {
                                    if (SUC > 2500000) {
                                        proceedToShortMHR();
                                    } else {
                                        commonMethods.showMessageDialog(context, "You are not eligible for MHR");
                                    }
                                } else if (clubWithType.equalsIgnoreCase("REGIONAL DIRECTOR CLUB")
                                        || clubWithoutType.equalsIgnoreCase("REGIONAL DIRECTOR CLUB")) {
                                    if (SUC > 1000000) {
                                        proceedToShortMHR();
                                    } else {
                                        commonMethods.showMessageDialog(context, "You are not eligible for MHR");
                                    }
                                } else {
                                    proceedToShortMHR();
                                }
                            }
                        }*/

        spinnerProopsalNumbers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    proposalNumber = globalDataList.get(position).getPROPOSALNUMBER();
                    /*long result = new DatabaseHelper(context).insertShortMHRProposal(proposalNumber, fromDate, toDate);
                    if (result > 0) {
                        removeProopsalNumber();
                        llShortMHRQuestions.setVisibility(View.GONE);
                        llOTP.setVisibility(View.GONE);
                        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(globalDataList);
                        spinnerProopsalNumbers.setAdapter(spinnerAdapter);
                        spinnerAdapter.notifyDataSetChanged();

                        gotoHomeDialog("PDF Uploaded Successfully");
                    }*/


                    selectedPosition = position;
                    llShortMHRQuestions.setVisibility(View.GONE);
                    llOTP.setVisibility(View.GONE);
                    if (globalDataList.size() > 0) {

                        if (userType.equalsIgnoreCase("CIF") ||
                                userType.equalsIgnoreCase("BDM") ||
                                userType.equalsIgnoreCase("AM") ||
                                userType.equalsIgnoreCase("SAM") ||
                                userType.equalsIgnoreCase("ZAM")) {
                            if (globalDataList.get(position).getExperience() < 1) {
                                long SUC = Long.valueOf(globalDataList.get(position).getSUC());
                                if (SUC <= 1000000) {
                                    commonMethods.showMessageDialog(context, "You are not eligible for MHR");
                                    return;
                                }
                            }
                        }
                        proceedToShortMHR();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setDates() {
        textViewFromDate.setText(commonMethods.getPreviousMonthDate());
        textViewToDate.setText(commonMethods.getCurrentMonthDate());
    }

    private void getLocationPromt() {
        LocationManager locationmanager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            try {
                mLatLng = commonMethods.getCurrentLocation(context, gpsTracker);

                if (mLatLng.latitude == 0.0 && mLatLng.longitude == 0.0) {
                    commonMethods.showToast(context, "Please check your gps connection and try again");
                    mLatLng = new LatLng(0.0, 0.0);
                    mLatLng = commonMethods.getCurrentLocation(context, gpsTracker);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                mLatLng = commonMethods.getCurrentLocation(context, gpsTracker);
            }
        } else {
            commonMethods.showGPSDisabledAlertToUser(context);
        }
    }

    @Override
    public void onClick(View view) {
        //CreateShortMHRPdf();
        if (!TextUtils.isEmpty(meetLAProposerStatus)
                && meetLAProposerStatus.equalsIgnoreCase("yes")) {
            meetingPlace = etMeetingPlace.getText().toString();
            meetingDate = tvCurrentDate.getText().toString();
            LANotMeet = "";
            etLifeAssuredMeetNo.setText("");
        } else {
            meetingPlace = "";
            etMeetingPlace.setText(meetingPlace);
            meetingDate = "";
            LANotMeet = etLifeAssuredMeetNo.getText().toString();
        }


        if (!TextUtils.isEmpty(LASocialStatus)
                && LASocialStatus.equalsIgnoreCase("no")) {
            LAsocialDetailsString = etLifeAssuredDetails.getText().toString();

        } else {
            LAsocialDetailsString = "";
            etLifeAssuredDetails.setText(LAsocialDetailsString);

        }

        if (!TextUtils.isEmpty(injuryStatus)
                && injuryStatus.equalsIgnoreCase("yes")) {
            injuryDetailsString = etInjuryIllnessDetails.getText().toString();

        } else {
            injuryDetailsString = "";
            etInjuryIllnessDetails.setText(injuryDetailsString);
        }


        if (!TextUtils.isEmpty(satisfactionStatus)
                && satisfactionStatus.equalsIgnoreCase("no")) {
            satisfactionDetailsString = etSatisfactionDetails.getText().toString();

        } else {
            satisfactionDetailsString = "";
            etSatisfactionDetails.setText(satisfactionDetailsString);
        }

        if (!TextUtils.isEmpty(approximateAge)
                && approximateAgeDetails.equalsIgnoreCase("yes")) {
            approximateAgeDetails = etApproximateAge.getText().toString();

        } else {
            approximateAgeDetails = "";
            etApproximateAge.setText(approximateAgeDetails);
        }
        switch (view.getId()) {
            case R.id.buttonOk:
                clearList();
                commonMethods.hideKeyboard(etProposalNumber, context);
                llOTP.setVisibility(View.GONE);
                if (commonMethods.isNetworkConnected(context)) {
                    StringBuilder input = new StringBuilder();
                    /*proposalNumber = etProposalNumber.getText().toString();
                    if (TextUtils.isEmpty(proposalNumber)) {
                        commonMethods.showMessageDialog(context, "Please Enter Proposal Number");
                        return;
                    } else {
                        input.append(proposalNumber);
                    }*/

                    fromDate = textViewFromDate.getText().toString();
                    toDate = textViewToDate.getText().toString();
                    input.append(",").append(fromDate).append(",").append(toDate);

                    if (TextUtils.isEmpty(toDate) || TextUtils.isEmpty(fromDate)) {
                        commonMethods.showMessageDialog(context, "Please Select Dates");
                    } else {
                        final SimpleDateFormat formatter = new SimpleDateFormat(
                                "dd-MMMM-yyyy", Locale.ENGLISH);
                        SimpleDateFormat finalDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

                        Date d1 = null, d2 = null;
                        try {
                            d1 = formatter.parse(fromDate);
                            fromDate = finalDateFormat.format(d1);
                            fromDate = fromDate.toUpperCase();

                            d2 = formatter.parse(toDate);
                            toDate = finalDateFormat.format(d2);
                            toDate = toDate.toUpperCase();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if ((d2.after(d1)) || (d2.equals(d1))) {
                            service_hits(input.toString());
                        } else {
                            commonMethods.showMessageDialog(context, "To date should be greater than From date");
                        }
                    }

                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }

                break;
            case R.id.buttonGenerateOTP:
                commonMethods.hideKeyboard(etOTP, context);
                if (commonMethods.isNetworkConnected(context)) {
                    if (validateMHR()) {
                        generateOTPAsyncTask = new GenerateOTPAsyncTask();
                        generateOTPAsyncTask.execute();
                    }

                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }
                break;

            case R.id.buttonValidateOTP:
                commonMethods.hideKeyboard(etOTP, context);

                if (commonMethods.isNetworkConnected(context)) {
                    OTP = etOTP.getText().toString();
                    if (TextUtils.isEmpty(OTP)) {
                        commonMethods.showMessageDialog(context, "Please Enter valid OTP.");
                        return;
                    }

                    if (validateMHR()) {
                        validateOTPAsyncTask = new ValidateOTPAsyncTask();
                        validateOTPAsyncTask.execute();
                    }

                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }
                break;
            case R.id.tvCurrentDate:
                datecheck = 1;
                showDateDialog();
                break;

            case R.id.imagePhotoGraphGPSCord:
                Check = "CustomerPhoto";
                capture_all_docs();
                /*Intent intentPhoto = new Intent(MHRActivity.this, OcrActivity.class);
                startActivityForResult(intentPhoto, REQUEST_OCR);*/
                break;
            case R.id.textViewFromDate:
                datecheck = 2;
                showDateDialog();
                break;

            case R.id.textViewToDate:
                datecheck = 3;
                showDateDialog();
                break;

        }
    }

    private boolean validateMHR() {
        if (!checkBoxDisclaimer.isChecked()) {
            commonMethods.showMessageDialog(context, "Please Accept the Declaration.");
            return false;
        } else if (TextUtils.isEmpty(meetLAProposerStatus)) {
            commonMethods.showMessageDialog(context, "Please Answer \"Have you met the life to be assured/proposer?\" ");
            return false;
        } else if (TextUtils.isEmpty(meetingDate) && meetLAProposerStatus.equalsIgnoreCase("yes")) {
            commonMethods.showMessageDialog(context, "Please Select Meeting Date");
            return false;
        } else if (TextUtils.isEmpty(meetingPlace) && meetLAProposerStatus.equalsIgnoreCase("yes")) {
            commonMethods.showMessageDialog(context, "Please enter Place of Meeting");
            return false;
        } else if (TextUtils.isEmpty(LANotMeet) && meetLAProposerStatus.equalsIgnoreCase("no")) {
            commonMethods.showMessageDialog(context, "Please describe the manner in which you gathered information about this case.");
            return false;
        } else if (TextUtils.isEmpty(LASocialStatus)) {
            commonMethods.showMessageDialog(context, "Please answer the question number 2.");
            commonMethods.setFocusable(etLifeAssuredDetails);
            etLifeAssuredDetails.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(LAsocialDetailsString) && LASocialStatus.equalsIgnoreCase("no")) {
            commonMethods.showMessageDialog(context, "Please describe question number 2.");
            commonMethods.setFocusable(etLifeAssuredDetails);
            etLifeAssuredDetails.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(injuryStatus)) {
            commonMethods.showMessageDialog(context, "Please answer the question number 3.");
            commonMethods.setFocusable(etInjuryIllnessDetails);
            etInjuryIllnessDetails.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(injuryDetailsString) && injuryStatus.equalsIgnoreCase("yes")) {
            commonMethods.showMessageDialog(context, "Please describe question number 3.");
            commonMethods.setFocusable(etInjuryIllnessDetails);
            etInjuryIllnessDetails.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(satisfactionStatus)) {
            commonMethods.showMessageDialog(context, "Please describe the question number 4.");
            commonMethods.setFocusable(etSatisfactionDetails);
            etSatisfactionDetails.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(satisfactionDetailsString) && satisfactionStatus.equalsIgnoreCase("no")) {
            commonMethods.showMessageDialog(context, "Please describe question number 4.");
            commonMethods.setFocusable(etSatisfactionDetails);
            etSatisfactionDetails.requestFocus();
            return false;
        }


        if (!TextUtils.isEmpty(NBD_NOMINEE_RELATION_WITH_LA)) {
            if (!(NBD_NOMINEE_RELATION_WITH_LA.equalsIgnoreCase("SON") ||
                    NBD_NOMINEE_RELATION_WITH_LA.equalsIgnoreCase("DAUGHTER") ||
                    NBD_NOMINEE_RELATION_WITH_LA.equalsIgnoreCase("WIFE") ||
                    NBD_NOMINEE_RELATION_WITH_LA.equalsIgnoreCase("Spouse") ||
                    NBD_NOMINEE_RELATION_WITH_LA.equalsIgnoreCase("HUSBAND") ||
                    NBD_NOMINEE_RELATION_WITH_LA.equalsIgnoreCase("MOTHER") ||
                    NBD_NOMINEE_RELATION_WITH_LA.equalsIgnoreCase("FATHER"))) {
                justifiationCoverApplied = etCoverJustification.getText().toString();
                if (TextUtils.isEmpty(justifiationCoverApplied)) {
                    commonMethods.showMessageDialog(context, "Please enter Justification for cover applied.");
                    commonMethods.setFocusable(etCoverJustification);
                    etCoverJustification.requestFocus();
                    return false;
                }
            }
        }


        if (!TextUtils.isEmpty(NBD_MAILING_STATE)) {
            if (!ST_ID.equalsIgnoreCase(NBD_MAILING_STATE)) {
                IAClarifiation = etIAClarifiation.getText().toString();
                if (TextUtils.isEmpty(IAClarifiation)) {
                    commonMethods.showMessageDialog(context, "Please enter Clarification for proposal logged by IA in other than Home branch.");
                    commonMethods.setFocusable(etCoverJustification);
                    etCoverJustification.requestFocus();
                    return false;
                }
            }
        }
        if (!TextUtils.isEmpty(NBD_PERMANENT_STATE)) {
            if (!ST_ID.equalsIgnoreCase(NBD_PERMANENT_STATE)) {
                IAClarifiation = etIAClarifiation.getText().toString();
                if (TextUtils.isEmpty(IAClarifiation)) {
                    commonMethods.showMessageDialog(context, "Please enter Clarification for proposal logged by IA in other than Home branch.");
                    commonMethods.setFocusable(etCoverJustification);
                    etCoverJustification.requestFocus();
                    return false;
                }
            }
        }

        if (!TextUtils.isEmpty(NBD_PROP_QUALIFICATION)) {
            if (NBD_PROP_QUALIFICATION.equalsIgnoreCase("illiterate ")) {
                if (TextUtils.isEmpty(approximateAgeDetails) && approximateAge.equalsIgnoreCase("yes")) {
                    commonMethods.showMessageDialog(context, "Please Enter Approximate age.");
                    commonMethods.setFocusable(etApproximateAge);
                    etApproximateAge.requestFocus();
                    return false;
                }
            }
        }
        return true;
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

    private void updateDisplay(int year, int month, int day) {

        String y = String.valueOf(year);
        String m = String.valueOf(month + 1);
        String da = String.valueOf(day);
        // String totaldate = m + "-" + da + "-" + y;

        if (m.contentEquals("1")) {
            m = "January";

        } else if (m.contentEquals("2")) {
            m = "February";

        } else if (m.contentEquals("3")) {
            m = "March";

        } else if (m.contentEquals("4")) {
            m = "April";

        } else if (m.contentEquals("5")) {
            m = "May";

        } else if (m.contentEquals("6")) {
            m = "June";

        } else if (m.contentEquals("7")) {
            m = "July";

        } else if (m.contentEquals("8")) {
            m = "August";

        } else if (m.contentEquals("9")) {
            m = "September";

        } else if (m.contentEquals("10")) {
            m = "October";

        } else if (m.contentEquals("11")) {
            m = "November";

        } else if (m.contentEquals("12")) {
            m = "December";

        }

        String totaldate = da + "-" + m + "-" + y;

        if (datecheck == 1) {
            tvCurrentDate.setText(totaldate);
        } else if (datecheck == 2) {
            textViewFromDate.setText(totaldate);
        } else if (datecheck == 3) {
            textViewToDate.setText(totaldate);
        }
    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }

    private void service_hits(String input) {

        service = new ServiceHits(context, METHOD_NAME, input,
                strCIFBDMUserId, strCIFBDMEmailId,
                strCIFBDMMObileNo, commonMethods.getStrAuth(), this);
        service.execute();
    }

    @Override
    public void downLoadData() {
        downloadMHRReportAsync = new DownloadMHRReportAsync();
        downloadMHRReportAsync.execute();
    }

    private void clearList() {
        llShortMHRQuestions.setVisibility(View.GONE);
        llProposalListLayout.setVisibility(View.GONE);
        llCoverJustification.setVisibility(View.GONE);
        llIAClarifiation.setVisibility(View.GONE);
        llApproximateAge.setVisibility(View.GONE);
        if (globalDataList != null) {
            globalDataList.clear();
        }
    }

    private void gotoHomeDialog(String message) {

        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_with_ok_button);
            dialog.setCancelable(false);
            TextView text = dialog.findViewById(R.id.tv_title);
            text.setText(message);
            Button dialogButton = dialog.findViewById(R.id.bt_ok);
            dialogButton.setText("Ok");
            dialogButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                    /*Intent i = new Intent(context, CarouselHomeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();*/

                    llShortMHRQuestions.setVisibility(View.GONE);
                    llMHRProposalLayout.setVisibility(View.VISIBLE);

                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* private void CreateACRPdf() {
        try {

            float[] columnWidths2 = {2f, 1f};

            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.BOLD, BaseColor.WHITE);
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 10,
                    Font.BOLD);
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 10,
                    Font.NORMAL);

            String extStorageDirectory = Environment
                    .getExternalStorageDirectory().toString();
            String direct = "//SBI-Smart Advisor";
            File folder = new File(extStorageDirectory + direct + "/");

            if (!folder.exists()) {
                folder.mkdirs();
            }
            File mypath = new File(folder, proposalNumber + "_MHR.pdf");


            Rectangle rect = new Rectangle(594f, 792f);

            Document document = new Document(rect, 50, 50, 50, 50);
            // Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter pdf_writer = null;
            pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(
                    mypath.getAbsolutePath()));

            // float[] columnWidths_4 = { 2f, 1f, 2f, 1f };

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

            Paragraph para_img_logo_after_space_2 = new Paragraph(" ");

            document.add(para_img_logo);
            // For SBI- Life Logo ends

            // To draw line after the sbi logo image
            document.add(new LineSeparator());
            // document.add(para_img_logo_after_space_1);
            document.add(para_img_logo_after_space_2);

            // document.add(para_img_logo_after_space_1);
            // document.add(para_img_logo_after_space_1);
            Paragraph Para_Header_confidetialreport = new Paragraph();
            Para_Header_confidetialreport.add(new Paragraph(
                    "CONFIDENTIAL REPORT", headerBold));

            PdfPTable CR_headertable1 = new PdfPTable(1);
            CR_headertable1.setWidthPercentage(100);
            PdfPCell CR_c1 = new PdfPCell(new Phrase(
                    Para_Header_confidetialreport));
            CR_c1.setBackgroundColor(BaseColor.DARK_GRAY);
            CR_c1.setPadding(5);
            CR_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            CR_headertable1.addCell(CR_c1);
            CR_headertable1.setHorizontalAlignment(Element.ALIGN_CENTER);

            document.add(CR_headertable1);
            //pdf_writer.setPageEvent(new NeedAnalysisActivity.HeaderAndFooter());

             *//* Proposal Number Detail *//*

            PdfPTable CR_table_Proposal_No = new PdfPTable(2);
            CR_table_Proposal_No.setWidthPercentage(100);

            PdfPCell Proposal_No_cell_1 = new PdfPCell(new Paragraph(
                    "Proposal No.", small_normal));
            PdfPCell Proposal_No_cell_2 = new PdfPCell(new Paragraph(
                    proposalNumber, small_bold));

            Proposal_No_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);

            Proposal_No_cell_1.setPadding(5);
            Proposal_No_cell_2.setPadding(5);

            CR_table_Proposal_No.addCell(Proposal_No_cell_1);
            CR_table_Proposal_No.addCell(Proposal_No_cell_2);
            document.add(CR_table_Proposal_No);

            *//* Details of Sales Representative Tables *//*

            PdfPTable CR_table_Sales_representative = new PdfPTable(2);
            CR_table_Sales_representative.setWidthPercentage(100);

            PdfPCell Sales_representative_cell_1 = new PdfPCell(new Paragraph(
                    "Sales Representative Name", small_normal));
            PdfPCell Sales_representative_cell_2 = new PdfPCell(new Paragraph(
                    globalDataList.get(position) .getACR_SR_NAME(), small_bold));

            Sales_representative_cell_2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            Sales_representative_cell_1.setPadding(5);
            Sales_representative_cell_2.setPadding(5);

            CR_table_Sales_representative.addCell(Sales_representative_cell_1);
            CR_table_Sales_representative.addCell(Sales_representative_cell_2);
            document.add(CR_table_Sales_representative);

            PdfPTable CR_table1 = new PdfPTable(4);

            CR_table1.setWidthPercentage(100);

            PdfPCell Code_cell_1 = new PdfPCell(new Paragraph("Code",
                    small_normal));
            PdfPCell Code_cell_2 = new PdfPCell(new Paragraph(
                    globalDataList.get(position) .getACR_SR_CODE(), small_bold));
            Code_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell License_no_cell_3 = new PdfPCell(new Paragraph(
                    "License No.", small_normal));
            PdfPCell License_no_cell_4 = new PdfPCell(new Paragraph("", small_bold));
            License_no_cell_4.setHorizontalAlignment(Element.ALIGN_CENTER);

            Code_cell_1.setPadding(5);
            Code_cell_2.setPadding(5);
            License_no_cell_3.setPadding(5);
            License_no_cell_4.setPadding(5);

            CR_table1.addCell(Code_cell_1);
            CR_table1.addCell(Code_cell_2);
            CR_table1.addCell(License_no_cell_3);
            CR_table1.addCell(License_no_cell_4);
            document.add(CR_table1);

            String userType = commonMethods.GetUserType(context);
            if (userType.equalsIgnoreCase("CIF")) {


                PdfPTable CR_table2 = new PdfPTable(4);

                CR_table2.setWidthPercentage(100);

                PdfPCell Branch_Code_cell_1 = new PdfPCell(new Paragraph(
                        "CIF Branch Code", small_normal));
                PdfPCell Branch_Code_cell_2 = new PdfPCell(new Paragraph(
                        globalDataList.get(position) .getACR_SR_BRANCH_CODE(), small_bold));
                Code_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell Branch_name_cell_3 = new PdfPCell(new Paragraph(
                        "CIF Branch Name", small_normal));
                PdfPCell Branch_name_cell_4 = new PdfPCell(new Paragraph(
                        globalDataList.get(position) .getACR_SR_BRANCH_NAME(), small_bold));
                License_no_cell_4.setHorizontalAlignment(Element.ALIGN_CENTER);

                Branch_Code_cell_1.setPadding(5);
                Branch_Code_cell_2.setPadding(5);
                Branch_name_cell_3.setPadding(5);
                Branch_name_cell_4.setPadding(5);

                CR_table2.addCell(Branch_Code_cell_1);
                CR_table2.addCell(Branch_Code_cell_2);
                CR_table2.addCell(Branch_name_cell_3);
                CR_table2.addCell(Branch_name_cell_4);
                document.add(CR_table2);

            }

            PdfPTable CR_table_sr_code_no = new PdfPTable(2);

            CR_table_sr_code_no.setWidthPercentage(100);
            PdfPCell sr_code_no_cell_1;
            sr_code_no_cell_1 = new PdfPCell(new Paragraph(
                    "S.R. UM CODE No.", small_normal));

            PdfPCell sr_code_no_cell_2 = new PdfPCell(new Paragraph(
                    globalDataList.get(position) .getACR_SR_SR_CODE(), small_bold));
            sr_code_no_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);

            sr_code_no_cell_1.setPadding(5);
            sr_code_no_cell_2.setPadding(5);

            CR_table_sr_code_no.addCell(sr_code_no_cell_1);
            CR_table_sr_code_no.addCell(sr_code_no_cell_2);
            document.add(CR_table_sr_code_no);

            PdfPTable CR_table_life_assured_name = new PdfPTable(2);

            CR_table_life_assured_name.setWidthPercentage(100);

            PdfPCell life_assured_name_cell_1 = new PdfPCell(new Paragraph(
                    "Life Assured Name", small_normal));
            PdfPCell life_assured_name_cell_2 = new PdfPCell(new Paragraph(
                    LifeAssuredName, small_bold));
            life_assured_name_cell_2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            life_assured_name_cell_1.setPadding(5);
            life_assured_name_cell_2.setPadding(5);

            CR_table_life_assured_name.addCell(life_assured_name_cell_1);
            CR_table_life_assured_name.addCell(life_assured_name_cell_2);
            document.add(CR_table_life_assured_name);

            PdfPTable CR_table4 = new PdfPTable(4);

            CR_table4.setWidthPercentage(100);
            PdfPCell CR_Permnent_address_cell_1 = new PdfPCell(new Paragraph(
                    "Address", small_normal));
            PdfPCell CR_Permnent_address_cell_2 = new PdfPCell(new Paragraph(
                    globalDataList.get(position) .getACR_LA_MAILING_ADDRESS_TITLE() + " "
                            + globalDataList.get(position) .getACR_LA_MAILING_ADDRESS1() + " "
                            + globalDataList.get(position) .getACR_LA_MAILING_ADDRESS2() + " "
                            + globalDataList.get(position) .getACR_LA_MAILING_ADDRESS3() + " "
                            + globalDataList.get(position) .getACR_LA_MAILING_CITY() + " "
                            + globalDataList.get(position) .getACR_LA_MAILING_STATE() + " "
                            + globalDataList.get(position) .getACR_LA_MAILING_PINCODE(), small_bold));
            CR_Permnent_address_cell_2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell CR_telephone_no_cell_3 = new PdfPCell(new Paragraph(
                    "Mobile No/Fax No", small_normal));
            PdfPCell CR_telephone_no_cell_4 = new PdfPCell(new Paragraph(
                    globalDataList.get(position) .getACR_LA_CONTACT_NO(), small_bold));
            CR_telephone_no_cell_4.setHorizontalAlignment(Element.ALIGN_CENTER);

            CR_telephone_no_cell_4.setVerticalAlignment(Element.ALIGN_CENTER);

            CR_Permnent_address_cell_1.setPadding(5);
            CR_Permnent_address_cell_2.setPadding(5);
            CR_telephone_no_cell_3.setPadding(5);
            CR_telephone_no_cell_4.setPadding(5);

            CR_table4.addCell(CR_Permnent_address_cell_1);
            CR_table4.addCell(CR_Permnent_address_cell_2);
            CR_table4.addCell(CR_telephone_no_cell_3);
            CR_table4.addCell(CR_telephone_no_cell_4);
            document.add(CR_table4);

            PdfPTable CR_table6 = new PdfPTable(2);
            CR_table6.setWidths(columnWidths2);

            String strProCode = proposalNumber.substring(0, 2);
            String question_authenticate_name_address = globalDataList.get(position) .getACR_AUTHENTICATE_NAME_ADDRESS();

            if (question_authenticate_name_address.equalsIgnoreCase("y")) {
                question_authenticate_name_address = "Yes";
            } else {
                question_authenticate_name_address = "No";
            }

            String question_aware_of_other_factor_not_mentioned_in_form = globalDataList.get(position) .getACR_AWARE_OF_OTHER_FACTOR();
            String aware_of_other_factor_detail = globalDataList.get(position) .getACR_AWARE_OF_OTHER_FACTOR_DET();
            String proposer_PEP = globalDataList.get(position) .getACR_PROP_OR_LA_PEP();
            String proposer_PEP_Details = globalDataList.get(position) .getACR_PEP_DETAIL();
            String question_explained_term_and_condition = globalDataList.get(position) .getACR_EXPLAINED_TAC();
            if (question_explained_term_and_condition.equalsIgnoreCase("y")) {
                question_explained_term_and_condition = "Yes";
            } else {
                question_explained_term_and_condition = "No";
            }
            String question_discuss_replies_all_question = globalDataList.get(position) .getACR_DISCUSS_REPLIES_AQ();
            if (question_discuss_replies_all_question.equalsIgnoreCase("y")) {
                question_discuss_replies_all_question = "Yes";
            } else {
                question_discuss_replies_all_question = "No";
            }

            String PIWC_informed = globalDataList.get(position) .getACR_PIWC_INFORMED();
            if (PIWC_informed.equalsIgnoreCase("y")) {
                PIWC_informed = "Yes";
            } else {
                PIWC_informed = "No";
            }

            String life_assured_health_status = globalDataList.get(position) .getACR_LA_HS();
            String question_have_physical_deformity = globalDataList.get(position) .getACR_HAVE_PHYSICAL_DEFORMITY();
            if (question_have_physical_deformity.equalsIgnoreCase("y")) {
                question_have_physical_deformity = "Yes";
            } else {
                question_have_physical_deformity = "No";
            }
            String question_is_hospitalize = globalDataList.get(position) .getACR_HAVE_HOSPITALIZE();
            String hospitalize_detail = globalDataList.get(position) .getACR_HOSPITALIZE_DETAIL();
            String question_duration_proposer_was_customer = globalDataList.get(position) .getACR_TIME_PROP_CST_OF_BRANCH();
            String proposer_AnnualIncome = globalDataList.get(position) .getACR_GROSS_ANNUAL_INCOME();
            String source_of_income = globalDataList.get(position) .getACR_SOURCE_OF_INCOME();
            if (strProCode
                    .equalsIgnoreCase(getString(R.string.sbi_life_sampoorn_cancer_suraksha_code))) {

                PdfPTable CR_table18 = new PdfPTable(2);
                CR_table18.setWidths(columnWidths2);
                CR_table18.setWidthPercentage(100);
                PdfPCell CR_table18_cell1 = new PdfPCell(
                        new Paragraph(
                                "1. Have you verified the authenticity and correctness of name and address mentioned in all the documents and as stated in the proposal form",
                                small_normal));
                PdfPCell CR_table18_cell2 = new PdfPCell(new Paragraph(
                        question_authenticate_name_address, small_bold));

                CR_table18_cell1.setPadding(5);
                CR_table18_cell2.setPadding(5);

                CR_table18_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                CR_table18.addCell(CR_table18_cell1);
                CR_table18.addCell(CR_table18_cell2);

                document.add(CR_table18);

                PdfPTable CR_table15;
                PdfPCell CR_table15_cell1;
                PdfPCell CR_table15_cell2;
                PdfPCell CR_table15_cell3;
                PdfPCell CR_table15_cell4;
                if (question_aware_of_other_factor_not_mentioned_in_form
                        .equalsIgnoreCase("y")) {
                    CR_table15 = new PdfPTable(4);
                    CR_table15.setWidthPercentage(100);
                    float[] columnWidths = {2f, 1f, 2f, 1f};
                    CR_table15.setWidths(columnWidths);
                    CR_table15_cell1 = new PdfPCell(
                            new Paragraph(
                                    "2. Are you aware of any apparent risk factors with regard to his/her health and habits or any other issue that are likely to add to the risk?",
                                    small_normal));
                    CR_table15_cell2 = new PdfPCell(new Paragraph("Yes",
                            small_bold));
                    CR_table15_cell2
                            .setHorizontalAlignment(Element.ALIGN_CENTER);

                    CR_table15_cell3 = new PdfPCell(new Paragraph(
                            "Give full particulars", small_normal));
                    CR_table15_cell3
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    CR_table15_cell4 = new PdfPCell(new Paragraph(
                            aware_of_other_factor_detail, small_bold));
                    CR_table15_cell4
                            .setHorizontalAlignment(Element.ALIGN_CENTER);

                    CR_table15_cell3.setVerticalAlignment(Element.ALIGN_CENTER);
                    CR_table15_cell1.setPadding(5);
                    CR_table15_cell2.setPadding(5);
                    CR_table15_cell3.setPadding(5);
                    CR_table15_cell4.setPadding(5);

                    CR_table15.addCell(CR_table15_cell1);
                    CR_table15.addCell(CR_table15_cell2);
                    CR_table15.addCell(CR_table15_cell3);
                    CR_table15.addCell(CR_table15_cell4);

                } else {
                    CR_table15 = new PdfPTable(2);
                    CR_table15.setWidthPercentage(100);
                    CR_table15.setWidths(columnWidths2);
                    CR_table15_cell1 = new PdfPCell(
                            new Paragraph(
                                    "2. Are you aware of any apparent risk factors with regard to his/her health and habits or any other issue that are likely to add to the risk?",
                                    small_normal));
                    CR_table15_cell2 = new PdfPCell(new Paragraph("No",
                            small_bold));
                    CR_table15_cell2
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    CR_table15_cell1.setPadding(5);
                    CR_table15_cell2.setPadding(5);

                    CR_table15.addCell(CR_table15_cell1);
                    CR_table15.addCell(CR_table15_cell2);

                }
                document.add(CR_table15);

                PdfPTable CR_table20;
                PdfPCell CR_table20_cell1;
                PdfPCell CR_table20_cell2;
                PdfPCell CR_table20_cell3;
                PdfPCell CR_table20_cell4;

                if (proposer_PEP.equalsIgnoreCase("y")) {
                    CR_table20 = new PdfPTable(2);
                    CR_table20.setWidths(columnWidths2);
                    CR_table20.setWidthPercentage(100);
                    CR_table20_cell1 = new PdfPCell(
                            new Paragraph(
                                    "3. Whether the Proposer/ Life to be Assured is a Politically Exposed Person (PEP) or family member/ close relative of any PEP",
                                    small_normal));
                    CR_table20_cell2 = new PdfPCell(new Paragraph("Yes",
                            small_bold));
                    CR_table20_cell2
                            .setHorizontalAlignment(Element.ALIGN_CENTER);

                    CR_table20_cell3 = new PdfPCell(new Paragraph(
                            "Give Details", small_normal));
                    CR_table20_cell3
                            .setHorizontalAlignment(Element.ALIGN_CENTER);

                    CR_table20_cell4 = new PdfPCell(new Paragraph(
                            proposer_PEP_Details, small_bold));
                    CR_table20_cell4
                            .setHorizontalAlignment(Element.ALIGN_CENTER);

                    CR_table20_cell3.setVerticalAlignment(Element.ALIGN_CENTER);
                    CR_table20_cell1.setPadding(5);
                    CR_table20_cell2.setPadding(5);
                    CR_table20_cell3.setPadding(5);
                    CR_table20_cell4.setPadding(5);

                    CR_table20.addCell(CR_table20_cell1);
                    CR_table20.addCell(CR_table20_cell2);
                    // CR_table20.addCell(CR_table20_cell3);
                    // CR_table20.addCell(CR_table20_cell4);

                } else {
                    CR_table20 = new PdfPTable(2);
                    CR_table20.setWidthPercentage(100);
                    CR_table20.setWidths(columnWidths2);
                    CR_table20_cell1 = new PdfPCell(
                            new Paragraph(
                                    "3. Whether the Proposer/ Life to be Assured is a Politically Exposed Person (PEP) or family member/ close relative of any PEP",
                                    small_normal));
                    CR_table20_cell2 = new PdfPCell(new Paragraph("No",
                            small_bold));
                    CR_table20_cell2
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    CR_table20_cell1.setPadding(5);
                    CR_table20_cell2.setPadding(5);

                    CR_table20.addCell(CR_table20_cell1);
                    CR_table20.addCell(CR_table20_cell2);

                }
                document.add(CR_table20);


                CR_table6.setWidthPercentage(100);
                PdfPCell CR_table6_cell1 = new PdfPCell(
                        new Paragraph(
                                "4. Have you fully explained the features, terms and conditions of the Proposed health Insurance plan to the Proposer?",
                                small_normal));
                PdfPCell CR_table6_cell2 = new PdfPCell(new Paragraph(
                        question_explained_term_and_condition, small_bold));

                CR_table6_cell1.setPadding(5);
                CR_table6_cell2.setPadding(5);

                CR_table6_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                CR_table6.addCell(CR_table6_cell1);
                CR_table6.addCell(CR_table6_cell2);

                document.add(CR_table6);

                PdfPTable CR_table7 = new PdfPTable(2);
                CR_table7.setWidths(columnWidths2);

                CR_table7.setWidthPercentage(100);
                PdfPCell CR_table7_cell1 = new PdfPCell(
                        new Paragraph(
                                "5. Have you discussed the replies to all questions in the proposal form with the Proposer",
                                small_normal));
                PdfPCell CR_table7_cell2 = new PdfPCell(new Paragraph(
                        question_discuss_replies_all_question, small_bold));

                CR_table7_cell1.setPadding(5);
                CR_table7_cell2.setPadding(5);

                CR_table7_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                CR_table7.addCell(CR_table7_cell1);
                CR_table7.addCell(CR_table7_cell2);

                document.add(CR_table7);


                PdfPTable CR_table201 = new PdfPTable(2);
                CR_table201.setWidths(columnWidths2);
                CR_table201.setWidthPercentage(100);
                PdfPCell CR_table201_cell1 = new PdfPCell(
                        new Paragraph(
                                "6. Whether the proposer has been informed about PIWC process?",
                                small_normal));
                PdfPCell CR_table201_cell2 = new PdfPCell(new Paragraph(
                        PIWC_informed, small_bold));

                CR_table201_cell1.setPadding(5);
                CR_table201_cell2.setPadding(5);

                CR_table201_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                CR_table201.addCell(CR_table201_cell1);
                CR_table201.addCell(CR_table201_cell2);

                document.add(CR_table201);

                PdfPTable CR_table202 = new PdfPTable(1);
                CR_table202.setWidthPercentage(100);
                PdfPCell CR_table202_cell1 = new PdfPCell(
                        new Paragraph(
                                "I hereby declare that I have understand the profile of the Proposer and other lives to be assured and belives that this product suits the insurance needs of the Prospect and other members of his/her family and I have clearly explained the features of the product to the Proposer/Prospect and discussed with the Proposer/Prospect the replies to all the questions and the declarations in the Proposal form and their importance.",
                                small_normal));

                CR_table202_cell1.setPadding(5);

                CR_table202.addCell(CR_table202_cell1);
                document.add(CR_table202);

                PdfPTable CR_table203 = new PdfPTable(1);
                CR_table203.setWidthPercentage(100);
                PdfPCell CR_table203_cell1 = new PdfPCell(
                        new Paragraph(
                                "'I do hereby confirm that the above proposal is canvassed by me and I certify that I have taken all possible precaution to ensure compliance with the KYC /Anti Money Laundering  Guidelines and the Anti Money Laundering policy of the company and verified at best of my knowledge that the prospect is not anonymous ,fictious and /or a benami person'",
                                small_normal));

                CR_table203_cell1.setPadding(5);

                CR_table203.addCell(CR_table203_cell1);
                document.add(CR_table203);

            } else if (strProCode
                    .equals(getString(R.string.sbi_life_smart_samriddhi_code))) {

                PdfPTable CR_table18 = new PdfPTable(2);
                CR_table18.setWidths(columnWidths2);
                CR_table18.setWidthPercentage(100);
                PdfPCell CR_table18_cell1 = new PdfPCell(
                        new Paragraph(
                                "1. Have you personally met the proposer/ Life to be assured and verified his identity",
                                small_normal));
                PdfPCell CR_table18_cell2 = new PdfPCell(new Paragraph(
                        question_authenticate_name_address, small_bold));

                CR_table18_cell1.setPadding(5);
                CR_table18_cell2.setPadding(5);

                CR_table18_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                CR_table18.addCell(CR_table18_cell1);
                CR_table18.addCell(CR_table18_cell2);
                document.add(CR_table18);


                PdfPTable CR_table12 = new PdfPTable(2);
                CR_table12.setWidthPercentage(100);
                CR_table12.setWidths(columnWidths2);
                PdfPCell CR_table12_cell1 = new PdfPCell(
                        new Paragraph(
                                "2A. What is the general state of health of the Life to be Assured",
                                small_normal));
                PdfPCell CR_table12_cell2 = new PdfPCell(new Paragraph(
                        life_assured_health_status, small_bold));

                CR_table12_cell1.setPadding(5);
                CR_table12_cell2.setPadding(5);

                CR_table12_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                CR_table12.addCell(CR_table12_cell1);
                CR_table12.addCell(CR_table12_cell2);
                document.add(CR_table12);


                PdfPTable CR_table13 = new PdfPTable(2);
                CR_table13.setWidthPercentage(100);
                CR_table13.setWidths(columnWidths2);
                PdfPCell CR_table13_cell1 = new PdfPCell(
                        new Paragraph(
                                "2B. Does he/ she have any physical deformity or mental retardation",
                                small_normal));
                PdfPCell CR_table13_cell2 = new PdfPCell(new Paragraph(
                        question_have_physical_deformity, small_bold));

                CR_table13_cell1.setPadding(5);
                CR_table13_cell2.setPadding(5);

                CR_table13_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                CR_table13.addCell(CR_table13_cell1);
                CR_table13.addCell(CR_table13_cell2);
                document.add(CR_table13);

                PdfPTable CR_table14;
                PdfPCell CR_table14_cell1;
                PdfPCell CR_table14_cell2;
                PdfPCell CR_table14_cell3;
                PdfPCell CR_table14_cell4;
                if (question_is_hospitalize.equalsIgnoreCase("y")) {
                    CR_table14 = new PdfPTable(4);
                    CR_table14.setWidthPercentage(100);
                    float[] columnWidths = {2f, 1f, 2f, 1f};
                    CR_table14.setWidths(columnWidths);
                    CR_table14_cell1 = new PdfPCell(
                            new Paragraph(
                                    "2C. Has he/ she undergone hospitalization or any surgery",
                                    small_normal));
                    CR_table14_cell2 = new PdfPCell(new Paragraph("Yes",
                            small_bold));
                    CR_table14_cell2
                            .setHorizontalAlignment(Element.ALIGN_CENTER);

                    CR_table14_cell3 = new PdfPCell(new Paragraph(
                            "Give full particulars", small_normal));
                    CR_table14_cell3
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    CR_table14_cell4 = new PdfPCell(new Paragraph(
                            hospitalize_detail, small_bold));
                    CR_table14_cell4
                            .setHorizontalAlignment(Element.ALIGN_CENTER);

                    CR_table14_cell3.setVerticalAlignment(Element.ALIGN_CENTER);
                    CR_table14_cell1.setPadding(5);
                    CR_table14_cell2.setPadding(5);
                    CR_table14_cell3.setPadding(5);
                    CR_table14_cell4.setPadding(5);

                    CR_table14.addCell(CR_table14_cell1);
                    CR_table14.addCell(CR_table14_cell2);
                    CR_table14.addCell(CR_table14_cell3);
                    CR_table14.addCell(CR_table14_cell4);

                } else {
                    CR_table14 = new PdfPTable(2);
                    CR_table14.setWidthPercentage(100);
                    CR_table14.setWidths(columnWidths2);
                    CR_table14_cell1 = new PdfPCell(
                            new Paragraph(
                                    "2C. Has he/ she undergone hospitalization or any surgery",
                                    small_normal));
                    CR_table14_cell2 = new PdfPCell(new Paragraph("No",
                            small_bold));
                    CR_table14_cell2
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    CR_table14_cell1.setPadding(5);
                    CR_table14_cell2.setPadding(5);

                    CR_table14.addCell(CR_table14_cell1);
                    CR_table14.addCell(CR_table14_cell2);

                }
                document.add(CR_table14);


                PdfPTable CR_table15;
                PdfPCell CR_table15_cell1;
                PdfPCell CR_table15_cell2;
                PdfPCell CR_table15_cell3;
                PdfPCell CR_table15_cell4;
                if (question_aware_of_other_factor_not_mentioned_in_form
                        .equalsIgnoreCase("y")) {
                    CR_table15 = new PdfPTable(4);
                    CR_table15.setWidthPercentage(100);
                    float[] columnWidths = {2f, 1f, 2f, 1f};
                    CR_table15.setWidths(columnWidths);
                    CR_table15_cell1 = new PdfPCell(
                            new Paragraph(
                                    "3. Are you aware of any apparent risk factors with regard to his/her health and habits or any other issue that are likely to add to the risk?",
                                    small_normal));
                    CR_table15_cell2 = new PdfPCell(new Paragraph("Yes",
                            small_bold));
                    CR_table15_cell2
                            .setHorizontalAlignment(Element.ALIGN_CENTER);

                    CR_table15_cell3 = new PdfPCell(new Paragraph(
                            "Give full particulars", small_normal));
                    CR_table15_cell3
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    CR_table15_cell4 = new PdfPCell(new Paragraph(
                            aware_of_other_factor_detail, small_bold));
                    CR_table15_cell4
                            .setHorizontalAlignment(Element.ALIGN_CENTER);

                    CR_table15_cell3.setVerticalAlignment(Element.ALIGN_CENTER);
                    CR_table15_cell1.setPadding(5);
                    CR_table15_cell2.setPadding(5);
                    CR_table15_cell3.setPadding(5);
                    CR_table15_cell4.setPadding(5);

                    CR_table15.addCell(CR_table15_cell1);
                    CR_table15.addCell(CR_table15_cell2);
                    CR_table15.addCell(CR_table15_cell3);
                    CR_table15.addCell(CR_table15_cell4);

                } else {
                    CR_table15 = new PdfPTable(2);
                    CR_table15.setWidthPercentage(100);
                    CR_table15.setWidths(columnWidths2);
                    CR_table15_cell1 = new PdfPCell(
                            new Paragraph(
                                    "3. Are you aware of any apparent risk factors with regard to his/her health and habits or any other issue that are likely to add to the risk?",
                                    small_normal));
                    CR_table15_cell2 = new PdfPCell(new Paragraph("No",
                            small_bold));
                    CR_table15_cell2
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    CR_table15_cell1.setPadding(5);
                    CR_table15_cell2.setPadding(5);

                    CR_table15.addCell(CR_table15_cell1);
                    CR_table15.addCell(CR_table15_cell2);

                }
                document.add(CR_table15);

                PdfPTable CR_table20;
                PdfPCell CR_table20_cell1;
                PdfPCell CR_table20_cell2;
                PdfPCell CR_table20_cell3;
                PdfPCell CR_table20_cell4;

                if (proposer_PEP.equalsIgnoreCase("y")) {
                    CR_table20 = new PdfPTable(2);
                    CR_table20.setWidths(columnWidths2);
                    CR_table20.setWidthPercentage(100);
                    CR_table20_cell1 = new PdfPCell(
                            new Paragraph(
                                    "4. Whether the Proposer/ Life to be Assured is a Politically Exposed Person (PEP) or family member/ close relative of any PEP",
                                    small_normal));
                    CR_table20_cell2 = new PdfPCell(new Paragraph("Yes",
                            small_bold));
                    CR_table20_cell2
                            .setHorizontalAlignment(Element.ALIGN_CENTER);

                    CR_table20_cell3 = new PdfPCell(new Paragraph(
                            "Give Details", small_normal));
                    CR_table20_cell3
                            .setHorizontalAlignment(Element.ALIGN_CENTER);

                    CR_table20_cell4 = new PdfPCell(new Paragraph(
                            proposer_PEP_Details, small_bold));
                    CR_table20_cell4
                            .setHorizontalAlignment(Element.ALIGN_CENTER);

                    CR_table20_cell3.setVerticalAlignment(Element.ALIGN_CENTER);
                    CR_table20_cell1.setPadding(5);
                    CR_table20_cell2.setPadding(5);
                    CR_table20_cell3.setPadding(5);
                    CR_table20_cell4.setPadding(5);

                    CR_table20.addCell(CR_table20_cell1);
                    CR_table20.addCell(CR_table20_cell2);
                    // CR_table20.addCell(CR_table20_cell3);
                    // CR_table20.addCell(CR_table20_cell4);

                } else {
                    CR_table20 = new PdfPTable(2);
                    CR_table20.setWidthPercentage(100);
                    CR_table20.setWidths(columnWidths2);
                    CR_table20_cell1 = new PdfPCell(
                            new Paragraph(
                                    "4. Whether the Proposer/ Life to be Assured is a Politically Exposed Person (PEP) or family member/ close relative of any PEP",
                                    small_normal));
                    CR_table20_cell2 = new PdfPCell(new Paragraph("No",
                            small_bold));
                    CR_table20_cell2
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    CR_table20_cell1.setPadding(5);
                    CR_table20_cell2.setPadding(5);

                    CR_table20.addCell(CR_table20_cell1);
                    CR_table20.addCell(CR_table20_cell2);

                }
                document.add(CR_table20);

                PdfPTable CR_table201 = new PdfPTable(2);
                CR_table201.setWidths(columnWidths2);
                CR_table201.setWidthPercentage(100);
                PdfPCell CR_table201_cell1 = new PdfPCell(
                        new Paragraph(
                                "5. Whether the proposer has been informed about PIWC process?",
                                small_normal));
                PdfPCell CR_table201_cell2 = new PdfPCell(new Paragraph(
                        PIWC_informed, small_bold));

                CR_table201_cell1.setPadding(5);
                CR_table201_cell2.setPadding(5);

                CR_table201_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                CR_table201.addCell(CR_table201_cell1);
                CR_table201.addCell(CR_table201_cell2);

                document.add(CR_table201);


                PdfPTable CR_table203 = new PdfPTable(1);
                CR_table203.setWidthPercentage(100);
                PdfPCell CR_table203_cell1 = new PdfPCell(
                        new Paragraph(
                                "'I do hereby confirm that the above proposal is canvassed by me and I certify that I have taken all possible precaution to ensure compliance with the KYC /Anti Money Laundering  Guidelines and the Anti Money Laundering policy of the company and verified at best of my knowledge that the prospect is not an anonymous fictitious and /or a benami person'.I recommend this case for acceptance",
                                small_normal));

                CR_table203_cell1.setPadding(5);

                CR_table203.addCell(CR_table203_cell1);
                document.add(CR_table203);

            } else {

                CR_table6.setWidthPercentage(100);
                PdfPCell CR_table6_cell1 = new PdfPCell(
                        new Paragraph(
                                "1. Have you fully explained the terms and conditions of the Proposed Insurance plan to the Proposer",
                                small_normal));
                PdfPCell CR_table6_cell2 = new PdfPCell(new Paragraph(
                        question_explained_term_and_condition, small_bold));

                CR_table6_cell1.setPadding(5);
                CR_table6_cell2.setPadding(5);

                CR_table6_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                CR_table6.addCell(CR_table6_cell1);
                CR_table6.addCell(CR_table6_cell2);

                document.add(CR_table6);

                PdfPTable CR_table7 = new PdfPTable(2);
                CR_table7.setWidths(columnWidths2);
                CR_table7.setWidthPercentage(100);
                PdfPCell CR_table7_cell1 = new PdfPCell(
                        new Paragraph(
                                "2. Have you discussed the replies to all questions in the proposal form with the Proposer",
                                small_normal));
                PdfPCell CR_table7_cell2 = new PdfPCell(new Paragraph(
                        question_discuss_replies_all_question, small_bold));

                CR_table7_cell1.setPadding(5);
                CR_table7_cell2.setPadding(5);

                CR_table7_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                CR_table7.addCell(CR_table7_cell1);
                CR_table7.addCell(CR_table7_cell2);

                document.add(CR_table7);

                PdfPTable CR_table8 = new PdfPTable(2);
                CR_table8.setWidths(columnWidths2);
                CR_table8.setWidthPercentage(100);
                PdfPCell CR_table8_cell1 = new PdfPCell(
                        new Paragraph(
                                "3. How long has the Proposer been a customer of the branch or known to you",
                                small_normal));
                PdfPCell CR_table8_cell2 = new PdfPCell(new Paragraph(
                        question_duration_proposer_was_customer + " ",
                        small_bold));

                CR_table8_cell1.setPadding(5);
                CR_table8_cell2.setPadding(5);

                CR_table8_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                CR_table8.addCell(CR_table8_cell1);
                CR_table8.addCell(CR_table8_cell2);
                document.add(CR_table8);

                PdfPTable CR_table9 = new PdfPTable(1);
                CR_table9.setWidthPercentage(100);
                PdfPCell CR_table9_cell1 = new PdfPCell(new Paragraph(
                        "4. Financial status of the Proposer", small_normal));

                CR_table9_cell1.setPadding(5);

                CR_table9.addCell(CR_table9_cell1);
                document.add(CR_table9);

                PdfPTable CR_table10 = new PdfPTable(4);

                CR_table10.setWidthPercentage(100);

                PdfPCell CR_table10_cell_1 = new PdfPCell(new Paragraph(
                        "A. Gross Annual Income", small_normal));
                PdfPCell CR_table10_cell_2 = new PdfPCell(new Paragraph("Rs. "
                        + proposer_AnnualIncome, small_bold));
                CR_table10_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell CR_table10_cell_3 = new PdfPCell(new Paragraph(
                        "B. Source Of Income", small_normal));
                PdfPCell CR_table10_cell_4 = new PdfPCell(new Paragraph(
                        source_of_income, small_bold));
                CR_table10_cell_4.setHorizontalAlignment(Element.ALIGN_CENTER);

                CR_table10_cell_4.setVerticalAlignment(Element.ALIGN_CENTER);

                CR_table10_cell_1.setPadding(5);
                CR_table10_cell_2.setPadding(5);
                CR_table10_cell_3.setPadding(5);
                CR_table10_cell_4.setPadding(5);

                CR_table10.addCell(CR_table10_cell_1);
                CR_table10.addCell(CR_table10_cell_2);
                CR_table10.addCell(CR_table10_cell_3);
                CR_table10.addCell(CR_table10_cell_4);
                document.add(CR_table10);

                PdfPTable CR_table11 = new PdfPTable(2);

                String question_satisfy_with_proposer_financial_standing = globalDataList.get(position) .getACR_SATISFY_WITH_PROP_FS();
                if (question_satisfy_with_proposer_financial_standing
                        .equalsIgnoreCase("y")) {

                    question_satisfy_with_proposer_financial_standing = "Yes";

                } else {
                    question_satisfy_with_proposer_financial_standing = "No";

                }
                CR_table11.setWidthPercentage(100);
                CR_table11.setWidths(columnWidths2);
                PdfPCell CR_table11_cell1 = new PdfPCell(
                        new Paragraph(
                                "C. Are you personally satisfied with the financial standing of the Proposer",
                                small_normal));
                PdfPCell CR_table11_cell2 = new PdfPCell(new Paragraph(
                        question_satisfy_with_proposer_financial_standing,
                        small_bold));

                CR_table11_cell1.setPadding(5);
                CR_table11_cell2.setPadding(5);

                CR_table11_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                CR_table11.addCell(CR_table11_cell1);
                CR_table11.addCell(CR_table11_cell2);

                document.add(CR_table11);

                PdfPTable CR_table12 = new PdfPTable(2);
                CR_table12.setWidthPercentage(100);
                CR_table12.setWidths(columnWidths2);
                PdfPCell CR_table12_cell1 = new PdfPCell(
                        new Paragraph(
                                "5A. What is the general state of health of the Life to be Assured",
                                small_normal));
                PdfPCell CR_table12_cell2 = new PdfPCell(new Paragraph(
                        life_assured_health_status, small_bold));

                CR_table12_cell1.setPadding(5);
                CR_table12_cell2.setPadding(5);

                CR_table12_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                CR_table12.addCell(CR_table12_cell1);
                CR_table12.addCell(CR_table12_cell2);
                document.add(CR_table12);

                PdfPTable CR_table13 = new PdfPTable(2);
                CR_table13.setWidthPercentage(100);
                CR_table13.setWidths(columnWidths2);
                PdfPCell CR_table13_cell1 = new PdfPCell(
                        new Paragraph(
                                "5B. Does he/ she have any physical deformity or mental retardation",
                                small_normal));
                PdfPCell CR_table13_cell2 = new PdfPCell(new Paragraph(
                        question_have_physical_deformity, small_bold));

                CR_table13_cell1.setPadding(5);
                CR_table13_cell2.setPadding(5);

                CR_table13_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                CR_table13.addCell(CR_table13_cell1);
                CR_table13.addCell(CR_table13_cell2);
                document.add(CR_table13);

                PdfPTable CR_table14;
                PdfPCell CR_table14_cell1;
                PdfPCell CR_table14_cell2;
                PdfPCell CR_table14_cell3;
                PdfPCell CR_table14_cell4;
                if (question_is_hospitalize.equalsIgnoreCase("y")) {
                    CR_table14 = new PdfPTable(4);
                    CR_table14.setWidthPercentage(100);
                    float[] columnWidths = {2f, 1f, 2f, 1f};
                    CR_table14.setWidths(columnWidths);
                    CR_table14_cell1 = new PdfPCell(
                            new Paragraph(
                                    "5C. Has he/ she undergone hospitalization or any surgery",
                                    small_normal));
                    CR_table14_cell2 = new PdfPCell(new Paragraph("Yes",
                            small_bold));
                    CR_table14_cell2
                            .setHorizontalAlignment(Element.ALIGN_CENTER);

                    CR_table14_cell3 = new PdfPCell(new Paragraph(
                            "Give full particulars", small_normal));
                    CR_table14_cell3
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    CR_table14_cell4 = new PdfPCell(new Paragraph(
                            hospitalize_detail, small_bold));
                    CR_table14_cell4
                            .setHorizontalAlignment(Element.ALIGN_CENTER);

                    CR_table14_cell3.setVerticalAlignment(Element.ALIGN_CENTER);
                    CR_table14_cell1.setPadding(5);
                    CR_table14_cell2.setPadding(5);
                    CR_table14_cell3.setPadding(5);
                    CR_table14_cell4.setPadding(5);

                    CR_table14.addCell(CR_table14_cell1);
                    CR_table14.addCell(CR_table14_cell2);
                    CR_table14.addCell(CR_table14_cell3);
                    CR_table14.addCell(CR_table14_cell4);

                } else {
                    CR_table14 = new PdfPTable(2);
                    CR_table14.setWidthPercentage(100);
                    CR_table14.setWidths(columnWidths2);
                    CR_table14_cell1 = new PdfPCell(
                            new Paragraph(
                                    "5C. Has he/ she undergone hospitalization or any surgery",
                                    small_normal));
                    CR_table14_cell2 = new PdfPCell(new Paragraph("No",
                            small_bold));
                    CR_table14_cell2
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    CR_table14_cell1.setPadding(5);
                    CR_table14_cell2.setPadding(5);

                    CR_table14.addCell(CR_table14_cell1);
                    CR_table14.addCell(CR_table14_cell2);

                }
                document.add(CR_table14);

                PdfPTable CR_table15;
                PdfPCell CR_table15_cell1;
                PdfPCell CR_table15_cell2;
                PdfPCell CR_table15_cell3;
                PdfPCell CR_table15_cell4;
                if (question_aware_of_other_factor_not_mentioned_in_form
                        .equalsIgnoreCase("y")) {
                    CR_table15 = new PdfPTable(4);
                    CR_table15.setWidthPercentage(100);
                    float[] columnWidths = {2f, 1f, 2f, 1f};
                    CR_table15.setWidths(columnWidths);
                    CR_table15_cell1 = new PdfPCell(
                            new Paragraph(
                                    "6. Are you aware of any other factors not indicated in the proposal form that are likely to add to the risk",
                                    small_normal));
                    CR_table15_cell2 = new PdfPCell(new Paragraph("Yes",
                            small_bold));
                    CR_table15_cell2
                            .setHorizontalAlignment(Element.ALIGN_CENTER);

                    CR_table15_cell3 = new PdfPCell(new Paragraph(
                            "Give full particulars", small_normal));
                    CR_table15_cell3
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    CR_table15_cell4 = new PdfPCell(new Paragraph(
                            aware_of_other_factor_detail, small_bold));
                    CR_table15_cell4
                            .setHorizontalAlignment(Element.ALIGN_CENTER);

                    CR_table15_cell3.setVerticalAlignment(Element.ALIGN_CENTER);
                    CR_table15_cell1.setPadding(5);
                    CR_table15_cell2.setPadding(5);
                    CR_table15_cell3.setPadding(5);
                    CR_table15_cell4.setPadding(5);

                    CR_table15.addCell(CR_table15_cell1);
                    CR_table15.addCell(CR_table15_cell2);
                    CR_table15.addCell(CR_table15_cell3);
                    CR_table15.addCell(CR_table15_cell4);

                } else {
                    CR_table15 = new PdfPTable(2);
                    CR_table15.setWidthPercentage(100);
                    CR_table15.setWidths(columnWidths2);
                    CR_table15_cell1 = new PdfPCell(
                            new Paragraph(
                                    "6. Are you aware of any other factors not indicated in the proposal form that are likely to add to the risk",
                                    small_normal));
                    CR_table15_cell2 = new PdfPCell(new Paragraph("No",
                            small_bold));
                    CR_table15_cell2
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    CR_table15_cell1.setPadding(5);
                    CR_table15_cell2.setPadding(5);

                    CR_table15.addCell(CR_table15_cell1);
                    CR_table15.addCell(CR_table15_cell2);

                }
                document.add(CR_table15);
                String proposer_seems_to_overweight = globalDataList.get(position) .getACR_PROPOSER_SEEMS_UNDERWEIGHT();
                if (proposer_seems_to_overweight.equalsIgnoreCase("y")) {
                    proposer_seems_to_overweight = "Yes";
                } else {
                    proposer_seems_to_overweight = "No";
                }

                PdfPTable CR_table16 = new PdfPTable(2);
                CR_table16.setWidths(columnWidths2);
                CR_table16.setWidthPercentage(100);
                PdfPCell CR_table16_cell1 = new PdfPCell(
                        new Paragraph(
                                "7. Does the Proposer seem to be overweight/ underweight in relation to his/her height",
                                small_normal));
                PdfPCell CR_table16_cell2 = new PdfPCell(new Paragraph(
                        proposer_seems_to_overweight, small_bold));

                CR_table16_cell1.setPadding(5);
                CR_table16_cell2.setPadding(5);

                CR_table16_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                CR_table16.addCell(CR_table16_cell1);
                CR_table16.addCell(CR_table16_cell2);

                document.add(CR_table16);

                String str_idetification_mark = globalDataList.get(position) .getACR_IDENTIFICATION_MARK();
                if (str_idetification_mark.trim().equals("")) {
                    str_idetification_mark = "No";
                }
                PdfPTable CR_table17 = new PdfPTable(2);
                CR_table17.setWidths(columnWidths2);
                CR_table17.setWidthPercentage(100);
                PdfPCell CR_table17_cell1 = new PdfPCell(new Paragraph(
                        "8. Identification Mark", small_normal));
                PdfPCell CR_table17_cell2 = new PdfPCell(new Paragraph(
                        str_idetification_mark, small_bold));

                CR_table17_cell1.setPadding(5);
                CR_table17_cell2.setPadding(5);

                CR_table17_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                CR_table17.addCell(CR_table17_cell1);
                CR_table17.addCell(CR_table17_cell2);
                document.add(CR_table17);

                PdfPTable CR_table18 = new PdfPTable(2);
                CR_table18.setWidths(columnWidths2);
                CR_table18.setWidthPercentage(100);
                PdfPCell CR_table18_cell1 = new PdfPCell(
                        new Paragraph(
                                "9. Have you verified the authenticity and correctness of name and address mentioned in all the documents and as stated in the proposal form",
                                small_normal));
                PdfPCell CR_table18_cell2 = new PdfPCell(new Paragraph(
                        question_authenticate_name_address, small_bold));

                CR_table18_cell1.setPadding(5);
                CR_table18_cell2.setPadding(5);

                CR_table18_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                CR_table18.addCell(CR_table18_cell1);
                CR_table18.addCell(CR_table18_cell2);

                document.add(CR_table18);
                String proposer_Under_scheme = globalDataList.get(position) .getACR_PROP_OR_LA_NRI();
                String question_proposer_or_life_assured_nri_or_not;
                if (proposer_Under_scheme.equalsIgnoreCase("NRI")) {
                    question_proposer_or_life_assured_nri_or_not = "Yes";
                } else {
                    question_proposer_or_life_assured_nri_or_not = "No";

                }
                PdfPTable CR_table19 = new PdfPTable(2);
                CR_table19.setWidths(columnWidths2);
                CR_table19.setWidthPercentage(100);

                PdfPCell CR_table19_cell1;
                if (strProCode
                        .equals("22")) {
                    CR_table19_cell1 = new PdfPCell(
                            new Paragraph(
                                    "10. Whether the Proposer/ Life to be Assured is an NRI",
                                    small_normal));
                } else {
                    CR_table19_cell1 = new PdfPCell(
                            new Paragraph(
                                    "10. Whether the Proposer/ Life to be Assured is an NRI/PIO",
                                    small_normal));
                }
                PdfPCell CR_table19_cell2 = new PdfPCell(new Paragraph(
                        question_proposer_or_life_assured_nri_or_not,
                        small_bold));

                CR_table19_cell1.setPadding(5);
                CR_table19_cell2.setPadding(5);

                CR_table19_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                CR_table19.addCell(CR_table19_cell1);
                CR_table19.addCell(CR_table19_cell2);

                document.add(CR_table19);

                PdfPTable CR_table20;
                PdfPCell CR_table20_cell1;
                PdfPCell CR_table20_cell2;
                PdfPCell CR_table20_cell3;
                PdfPCell CR_table20_cell4;

                if (proposer_PEP.equalsIgnoreCase("y")) {
                    CR_table20 = new PdfPTable(2);
                    CR_table20.setWidths(columnWidths2);
                    CR_table20.setWidthPercentage(100);
                    CR_table20_cell1 = new PdfPCell(
                            new Paragraph(
                                    "11. Whether the Proposer/ Life to be Assured is a Politically Exposed Person (PEP) or family member/ close relative of any PEP",
                                    small_normal));
                    CR_table20_cell2 = new PdfPCell(new Paragraph("Yes",
                            small_bold));
                    CR_table20_cell2
                            .setHorizontalAlignment(Element.ALIGN_CENTER);

                    CR_table20_cell3 = new PdfPCell(new Paragraph(
                            "Give Details", small_normal));
                    CR_table20_cell3
                            .setHorizontalAlignment(Element.ALIGN_CENTER);

                    CR_table20_cell4 = new PdfPCell(new Paragraph(
                            proposer_PEP_Details, small_bold));
                    CR_table20_cell4
                            .setHorizontalAlignment(Element.ALIGN_CENTER);

                    CR_table20_cell3.setVerticalAlignment(Element.ALIGN_CENTER);
                    CR_table20_cell1.setPadding(5);
                    CR_table20_cell2.setPadding(5);
                    CR_table20_cell3.setPadding(5);
                    CR_table20_cell4.setPadding(5);

                    CR_table20.addCell(CR_table20_cell1);
                    CR_table20.addCell(CR_table20_cell2);
                    // CR_table20.addCell(CR_table20_cell3);
                    // CR_table20.addCell(CR_table20_cell4);

                } else {
                    CR_table20 = new PdfPTable(2);
                    CR_table20.setWidthPercentage(100);
                    CR_table20.setWidths(columnWidths2);
                    CR_table20_cell1 = new PdfPCell(
                            new Paragraph(
                                    "11. Whether the Proposer/ Life to be Assured is a Politically Exposed Person (PEP) or family member/ close relative of any PEP",
                                    small_normal));
                    CR_table20_cell2 = new PdfPCell(new Paragraph("No",
                            small_bold));
                    CR_table20_cell2
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    CR_table20_cell1.setPadding(5);
                    CR_table20_cell2.setPadding(5);

                    CR_table20.addCell(CR_table20_cell1);
                    CR_table20.addCell(CR_table20_cell2);

                }
                document.add(CR_table20);

                PdfPTable CR_table201 = new PdfPTable(2);
                CR_table201.setWidths(columnWidths2);
                CR_table201.setWidthPercentage(100);
                PdfPCell CR_table201_cell1 = new PdfPCell(
                        new Paragraph(
                                "12. Whether the proposer has been informed about PIWC process?",
                                small_normal));
                PdfPCell CR_table201_cell2 = new PdfPCell(new Paragraph(
                        PIWC_informed, small_bold));

                CR_table201_cell1.setPadding(5);
                CR_table201_cell2.setPadding(5);

                CR_table201_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                CR_table201.addCell(CR_table201_cell1);
                CR_table201.addCell(CR_table201_cell2);
                document.add(CR_table201);


                PdfPTable MHR_DEC_Table = new PdfPTable(1);
                MHR_DEC_Table.setWidthPercentage(100);
                PdfPCell MHR_DEC_cell = new PdfPCell(
                        new Paragraph(
                                "I do hereby confirm that the above proposal is canvassed by me and that I am satisfied with the identity of the party. I also declare that the foregoing statements are true and correct to the best of my belief and knowledge.I hereby confirm that I have followed and completed all the Know Your Customer (KYC) norms as prescribed in the Anti Money Laundering Policy of SBI Life and in the IRDAI Anti Money Laundering Guidelines. I also certify that I have taken all possible precautions to ensure compliance with the Anti Money Laundering Guidelines and the Anti Money Laundering Policy of the Company and have verified to the best of my knowledge that the prospect is not an anonymous, fictitious and / or a benami person. Further, I certify that I have not accepted any premium or deposit towards procuring insurance in cash",
                                small_normal));
                MHR_DEC_cell.setPadding(5);
                MHR_DEC_Table.addCell(MHR_DEC_cell);
                document.add(MHR_DEC_Table);

                PdfPTable CR_table23 = new PdfPTable(1);
                CR_table23.setWidthPercentage(100);
                PdfPCell CR_table23_cell1 = new PdfPCell(
                        new Paragraph(
                                " I hereby undertake to submit/dispatch premium collected on behalf of SBI Life under this proposal at the designated branch/office of SBI Life immediately on receipt.",
                                small_normal));

                CR_table23_cell1.setPadding(5);
                CR_table23.addCell(CR_table23_cell1);
                document.add(CR_table23);


            }


            String declaration = "";
            if (meetLAProposerStatus.equalsIgnoreCase("yes")) {
                declaration = "Date : " + meetingDate + " Place Of Meeting : " + meetingPlace
                        + "\n" + context.getString(R.string.digitalMHRMeetingQuestionYesDesc);
            } else {
                declaration = "Details : " + LANotMeet + "\n" + context.getString(R.string.digitalMHRMeetingQuestionNoDesc);
            }


            PdfPTable tableLAPropMeet = new PdfPTable(1);
            tableLAPropMeet.setWidthPercentage(100);
            PdfPCell tableLAPropMeetCell = new PdfPCell(new Paragraph("Have you met the life to be assured/proposer? "
                    //+ "\nAnswer : " + meetLAProposerStatus + "\n" + declaration, small_bold));
                    + "\nAnswer : " + meetLAProposerStatus, small_bold));
            tableLAPropMeetCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableLAPropMeet.addCell(tableLAPropMeetCell);
            document.add(tableLAPropMeet);

            if (!TextUtils.isEmpty(LAsocialDetailsString)) {
                LAsocialDetailsString = "Details : " + LAsocialDetailsString;
            }

            PdfPTable tableLASocialStatus = new PdfPTable(1);
            tableLASocialStatus.setWidthPercentage(100);
            PdfPCell tableLASocialStatusCell = new PdfPCell(new Paragraph("Have you verified occupation, financial & social position, and personal habits of the Life to be assured as stated in the proposal form & found them to be correct? "
                    + "\nAnswer : " + LASocialStatus + "\n" + LAsocialDetailsString, small_bold));
            tableLASocialStatusCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableLASocialStatus.addCell(tableLASocialStatusCell);
            document.add(tableLASocialStatus);

            if (!TextUtils.isEmpty(injuryDetailsString)) {
                injuryDetailsString = "Details : " + injuryDetailsString;
            }
            PdfPTable tableLAInjuryDetails = new PdfPTable(1);
            tableLAInjuryDetails.setWidthPercentage(100);
            PdfPCell tableLAINjuryDeatilsCell = new PdfPCell(new Paragraph("Do you have any knowledge of Life to be Assured suffering from any serious illness or injury or undergone any operation or medical investigation or aware of having any apparent physical deformity or mental retardation or any other circumstances that are likely to add to the risk?"
                    + "\nAnswer : " + injuryStatus + "\n" + injuryDetailsString, small_bold));
            tableLAINjuryDeatilsCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableLAInjuryDetails.addCell(tableLAINjuryDeatilsCell);
            document.add(tableLAInjuryDetails);

            if (!TextUtils.isEmpty(satisfactionDetailsString)) {
                satisfactionDetailsString = "Details : " + satisfactionDetailsString;
            }
            PdfPTable tableLASatisfactionDetails = new PdfPTable(1);
            tableLASatisfactionDetails.setWidthPercentage(100);
            PdfPCell tableLASatisfactionDeatilsCell = new PdfPCell(new Paragraph("Are you fully satisfied with the overall profile & identity (including KYC details) of the Life to be assured & recommend for policy issuance?"
                    + "\nAnswer : " + satisfactionStatus + "\n" + satisfactionDetailsString, small_bold));
            tableLASatisfactionDeatilsCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableLASatisfactionDetails.addCell(tableLASatisfactionDeatilsCell);
            document.add(tableLASatisfactionDetails);

            PdfPTable CR_table21 = new PdfPTable(1);
            CR_table21.setWidthPercentage(100);
               *//* PdfPCell CR_table21_cell1 = new PdfPCell(
                        new Paragraph("- I have discussed the Proposal with the Sales Representative.\n" +
                                "- I have scrutinized the Proposal Form, the Sales Representative Report and on the basis of my independent enquiries, I recommend the Proposal for acceptance.", small_normal));*//*


            PdfPCell CR_table21_cell1 = new PdfPCell(
                    new Paragraph("I hereby declare that I have discussed this case with the Advisor and confirm on the basis of my independent assessment and evaluation that the foregoing statements are correct.", small_normal));

            CR_table21_cell1.setPadding(5);
            CR_table21.addCell(CR_table21_cell1);
            document.add(CR_table21);


            String str_sign_header;
            String code = globalDataList.get(position) .getACR_SR_CODE();
            String name = globalDataList.get(position) .getACR_SR_NAME();
            if (userType.equals("UM")) {
                str_sign_header = "(IA code- "
                        + code + ")" + "\n" + "Name of IA- " + name + "\n"
                        + "Authenticated by Id & Password";
            } else if (userType.equals("CIF")) {
                str_sign_header = "(CIF code- "
                        + code
                        + ")"
                        + "\n"
                        + "Name of CIF- "
                        + name
                        + "\n"
                        + "Authenticated by Id & Password"
                        + "\n";

            } else if (userType.equals("BAP")) {
                str_sign_header = "(BAP code- "
                        + code
                        + ")"
                        + "\n"
                        + "Name of Broker- "
                        + name
                        + "\n"
                        + "Authenticated by Id & Password"
                        + "\n";

            } else if (userType.equals("CAG")) {
                str_sign_header = "(CAG code- "
                        + code
                        + ")"
                        + "\n"
                        + "Name of Corporate Agent- "
                        + name + "\n" + "Authenticated by Id & Password"
                        + "\n";

            } else if (userType.equals("IMF")) {
                str_sign_header = "Signature of Sales Representative(ISP code- "
                        + code
                        + ")"
                        + "\n"
                        + "ISP Name- "
                        + name
                        + "\n"
                        + "Authenticated by Id & Password"
                        + "\n";

            } else {
                str_sign_header = "Signature of Sales Representative";
            }


            String date = globalDataList.get(position) .getACR_DATE();
            PdfPTable CR_table22 = new PdfPTable(2);
            CR_table22.setWidthPercentage(100);

            PdfPCell CR_table22_cell2 = new PdfPCell(new Paragraph("Date : " + date,
                    small_normal));
            CR_table22_cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            CR_table22.addCell(CR_table22_cell2);

            PdfPCell CR_table22_cell1 = new PdfPCell(new Paragraph(
                    str_sign_header, small_bold));
            CR_table22_cell1.setPaddingBottom(Element.ALIGN_CENTER);
            CR_table22.addCell(CR_table22_cell1);
            document.add(CR_table22);

            document.close();
        } catch (Exception e) {

            Log.e("PDF_CPFActivity", e.toString()
                    + "Error while generating PDF");
        }
    }
*/
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
                message = "Authenticated via OTP - " + passCode + " shared for quotation no. " + proposalNumber + " on "
                        + currentDateTime;//TimestampDate;
            } else {
                message = "Authenticated via OTP shared for quotation no. " + proposalNumber + " on "
                        + currentDateTime;//TimestampDate;
            }

            message += " " + userType + " Name : " + commonMethods.getUserName(context) +
                    userType + " Code : " + strCIFBDMUserId;
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, message, 0,
                    20, 0);
            content.endText();
        }

        stamper.close();

    }

    @Override
    protected void onDestroy() {
        killTasks();
        super.onDestroy();
    }


    private void killTasks() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (downloadMHRReportAsync != null) {
            downloadMHRReportAsync.cancel(true);
        }
        if (service != null) {
            service.cancel(true);
        }

        if (generateOTPAsyncTask != null) {
            generateOTPAsyncTask.cancel(true);
        }
        if (validateOTPAsyncTask != null) {
            validateOTPAsyncTask.cancel(true);
        }
        if (authenticatePDFAsync != null) {
            authenticatePDFAsync.cancel(true);
        }

        if (mAsyncUploadFileCommon != null) {
            mAsyncUploadFileCommon.cancel(true);
        }

        if (saveMHRProposalService != null) {
            saveMHRProposalService.cancel(true);
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);
        String s = radioButton.getText() + "";

        switch (radioGroup.getId()) {
            case R.id.radioGroupMHR:
                //TextView tvDeclarationOption = findViewById(R.id.tvDeclarationOption);
                LinearLayout llMeetingPlace = findViewById(R.id.llMeetingPlace);
                LinearLayout llLifeAssuredMeetNo = findViewById(R.id.llLifeAssuredMeetNo);

                //String s = radioButton.getText() + "";
                if ("Yes".equals(s)) {
                    meetLAProposerStatus = "Yes";
                    llMeetingPlace.setVisibility(View.VISIBLE);
                    llLifeAssuredMeetNo.setVisibility(View.GONE);
                    //tvDeclarationOption.setText(context.getString(R.string.digitalMHRMeetingQuestionYesDesc));
                } else if ("No".equals(s)) {
                    meetLAProposerStatus = "No";
                    llMeetingPlace.setVisibility(View.GONE);
                    llLifeAssuredMeetNo.setVisibility(View.VISIBLE);
                    // tvDeclarationOption.setText(context.getString(R.string.digitalMHRMeetingQuestionNoDesc));
                }

                break;

            case R.id.radioGroupLifeAssuredDetails:
                LinearLayout llLifeAssuredDetails = findViewById(R.id.llLifeAssuredDetails);
                if ("Yes".equals(s)) {
                    LASocialStatus = "Yes";
                    llLifeAssuredDetails.setVisibility(View.GONE);
                } else if ("No".equals(s)) {
                    LASocialStatus = "No";
                    llLifeAssuredDetails.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.radioGroupInjuryIllnessDetails:
                LinearLayout llInjuryIllnessDetails = findViewById(R.id.llInjuryIllnessDetails);
                if ("Yes".equals(s)) {
                    injuryStatus = "Yes";
                    llInjuryIllnessDetails.setVisibility(View.VISIBLE);
                } else if ("No".equals(s)) {
                    injuryStatus = "No";
                    llInjuryIllnessDetails.setVisibility(View.GONE);
                }
                break;
            case R.id.radioGroupSatisfactionDetails:
                LinearLayout llSatisfactionDetails = findViewById(R.id.llSatisfactionDetails);
                if ("Yes".equals(s)) {
                    satisfactionStatus = "Yes";
                    llSatisfactionDetails.setVisibility(View.GONE);
                } else if ("No".equals(s)) {
                    satisfactionStatus = "No";
                    llSatisfactionDetails.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.radioGroupLifeAssuredCurrentAgeStatus:
                LinearLayout llLifeAssuredApproximateAge = findViewById(R.id.llLifeAssuredApproximateAge);
                if ("Yes".equals(s)) {
                    approximateAge = "Yes";
                    llLifeAssuredApproximateAge.setVisibility(View.GONE);
                } else if ("No".equals(s)) {
                    approximateAge = "No";
                    llLifeAssuredApproximateAge.setVisibility(View.VISIBLE);
                }
                break;
        }


    }

    private void CreateShortMHRPdf() {
        try {

            float[] columnWidths2 = {2f, 1f};

            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.BOLD, BaseColor.WHITE);
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 10,
                    Font.BOLD);
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 10,
                    Font.NORMAL);
            File mypath = mStorageUtils.createFileToAppSpecificDir(context, proposalNumber + "_MHR.pdf");


            Rectangle rect = new Rectangle(594f, 792f);

            Document document = new Document(rect, 50, 50, 50, 50);
            // Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter pdf_writer = null;
            pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(
                    mypath.getAbsolutePath()));

            // float[] columnWidths_4 = { 2f, 1f, 2f, 1f };

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

            Paragraph para_img_logo_after_space_2 = new Paragraph(" ");

            document.add(para_img_logo);
            // For SBI- Life Logo ends

            // To draw line after the sbi logo image
            document.add(new LineSeparator());
            // document.add(para_img_logo_after_space_1);
            document.add(para_img_logo_after_space_2);

            // document.add(para_img_logo_after_space_1);
            // document.add(para_img_logo_after_space_1);
            Paragraph Para_Header_confidetialreport = new Paragraph();
            Para_Header_confidetialreport.add(new Paragraph(
                    "MHR", headerBold));

            PdfPTable CR_headertable1 = new PdfPTable(1);
            CR_headertable1.setWidthPercentage(100);
            PdfPCell CR_c1 = new PdfPCell(new Phrase(
                    Para_Header_confidetialreport));
            CR_c1.setBackgroundColor(BaseColor.DARK_GRAY);
            CR_c1.setPadding(5);
            CR_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            CR_headertable1.addCell(CR_c1);
            CR_headertable1.setHorizontalAlignment(Element.ALIGN_CENTER);

            document.add(CR_headertable1);
            document.add(para_img_logo_after_space_2);

            //pdf_writer.setPageEvent(new NeedAnalysisActivity.HeaderAndFooter());

            /* Proposal Number Detail */


            PdfPTable CR_table_Proposal_No = new PdfPTable(2);
            CR_table_Proposal_No.setWidthPercentage(100);

            PdfPCell Proposal_No_cell_1 = new PdfPCell(new Paragraph(
                    "Proposal Number", small_normal));
            PdfPCell Proposal_No_cell_2 = new PdfPCell(new Paragraph(
                    proposalNumber, small_bold));

            Proposal_No_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);

            Proposal_No_cell_1.setPadding(5);
            Proposal_No_cell_2.setPadding(5);

            CR_table_Proposal_No.addCell(Proposal_No_cell_1);
            CR_table_Proposal_No.addCell(Proposal_No_cell_2);
            document.add(CR_table_Proposal_No);

            ///Row
            PdfPTable tableProposalName = new PdfPTable(2);
            tableProposalName.setWidthPercentage(100);

            PdfPCell cellTitle = new PdfPCell(new Paragraph(
                    "Proposal Name", small_normal));
            PdfPCell valueCell = new PdfPCell(new Paragraph(
                    globalDataList.get(selectedPosition).getNAME(), small_bold));

            valueCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            cellTitle.setPadding(5);
            valueCell.setPadding(5);

            tableProposalName.addCell(cellTitle);
            tableProposalName.addCell(valueCell);
            document.add(tableProposalName);
            ////End Row


            ///Row
            PdfPTable tableSumAssured = new PdfPTable(2);
            tableSumAssured.setWidthPercentage(100);

            cellTitle = new PdfPCell(new Paragraph(
                    "Sum Assured", small_normal));
            valueCell = new PdfPCell(new Paragraph(
                    globalDataList.get(selectedPosition).getSUMASSURED(), small_bold));

            valueCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            cellTitle.setPadding(5);
            valueCell.setPadding(5);

            tableSumAssured.addCell(cellTitle);
            tableSumAssured.addCell(valueCell);
            document.add(tableSumAssured);
            ////End Row


            ///Row
            PdfPTable tableIAName = new PdfPTable(2);
            tableIAName.setWidthPercentage(100);

            cellTitle = new PdfPCell(new Paragraph(
                    "IA Name", small_normal));
            valueCell = new PdfPCell(new Paragraph(
                    globalDataList.get(selectedPosition).getIANAME(), small_bold));

            valueCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            cellTitle.setPadding(5);
            valueCell.setPadding(5);

            tableIAName.addCell(cellTitle);
            tableIAName.addCell(valueCell);
            document.add(tableIAName);
            ////End Row


            ///Row
            PdfPTable tableIACode = new PdfPTable(2);
            tableIACode.setWidthPercentage(100);

            cellTitle = new PdfPCell(new Paragraph(
                    "IA Code", small_normal));
            valueCell = new PdfPCell(new Paragraph(
                    globalDataList.get(selectedPosition).getIACODE(), small_bold));

            valueCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            cellTitle.setPadding(5);
            valueCell.setPadding(5);

            tableIACode.addCell(cellTitle);
            tableIACode.addCell(valueCell);
            document.add(tableIACode);
            ////End Row


            ///Row
            PdfPTable tableRegion = new PdfPTable(2);
            tableRegion.setWidthPercentage(100);

            cellTitle = new PdfPCell(new Paragraph(
                    "Region", small_normal));
            valueCell = new PdfPCell(new Paragraph(
                    globalDataList.get(selectedPosition).getREGION(), small_bold));

            valueCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            cellTitle.setPadding(5);
            valueCell.setPadding(5);

            tableRegion.addCell(cellTitle);
            tableRegion.addCell(valueCell);
            document.add(tableRegion);
            ////End Row


            ///Row
            PdfPTable tableSUC = new PdfPTable(2);
            tableSUC.setWidthPercentage(100);

            cellTitle = new PdfPCell(new Paragraph(
                    "SUC", small_normal));
            valueCell = new PdfPCell(new Paragraph(
                    globalDataList.get(selectedPosition).getSUC(), small_bold));

            valueCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            cellTitle.setPadding(5);
            valueCell.setPadding(5);

            tableSUC.addCell(cellTitle);
            tableSUC.addCell(valueCell);
            document.add(tableSUC);
            ////End Row

            ///Row
            PdfPTable tableChannel = new PdfPTable(2);
            tableChannel.setWidthPercentage(100);

            cellTitle = new PdfPCell(new Paragraph(
                    "Channel", small_normal));
            valueCell = new PdfPCell(new Paragraph(
                    globalDataList.get(selectedPosition).getCHANNEL(), small_bold));

            valueCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            cellTitle.setPadding(5);
            valueCell.setPadding(5);

            tableChannel.addCell(cellTitle);
            tableChannel.addCell(valueCell);
            document.add(tableChannel);
            ////End Row
            document.add(para_img_logo_after_space_2);

            /* Details of Sales Representative Tables */


            PdfPTable tableLAPropMeet = new PdfPTable(3);
            tableLAPropMeet.setWidths(new float[]{4.5f, 2f, 3.5f});
            tableLAPropMeet.setWidthPercentage(100);

            // 1st Row
            PdfPCell cell = new PdfPCell(new Phrase("Question", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableLAPropMeet.addCell(cell);

            cell = new PdfPCell(new Phrase("Answer", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableLAPropMeet.addCell(cell);

            cell = new PdfPCell(new Phrase("Details", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableLAPropMeet.addCell(cell);
            document.add(tableLAPropMeet);

            //End 1st Row


            //2nd Row
            PdfPTable tableLAPropMeet2ndRow = new PdfPTable(3);
            tableLAPropMeet2ndRow.setWidths(new float[]{4.5f, 2f, 3.5f});
            tableLAPropMeet2ndRow.setWidthPercentage(100);

            String declaration = "";
            if (meetLAProposerStatus.equalsIgnoreCase("yes")) {
                declaration = "Date : " + meetingDate + " Place Of Meeting : " + meetingPlace;
            } else {
                declaration = LANotMeet;
            }

            cell = new PdfPCell(new Phrase("Have you met the life to be assured/proposer? ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableLAPropMeet2ndRow.addCell(cell);

            cell = new PdfPCell(new Phrase(meetLAProposerStatus, small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableLAPropMeet2ndRow.addCell(cell);

            cell = new PdfPCell(new Phrase(declaration, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableLAPropMeet2ndRow.addCell(cell);
            document.add(tableLAPropMeet2ndRow);

            if (meetLAProposerStatus.equalsIgnoreCase("yes")) {
                if (customerPhotoBitmap != null) {
                    PdfPTable tableCustomerPhoto = new PdfPTable(3);
                    tableCustomerPhoto.setWidths(new float[]{4.5f, 2f, 3.5f});
                    tableCustomerPhoto.setWidthPercentage(100);

                    cell = new PdfPCell(new Phrase("Customer Photo", small_bold));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    tableCustomerPhoto.addCell(cell);

                    ByteArrayOutputStream userPhotoStream = new ByteArrayOutputStream();
                    customerPhotoBitmap.compress(Bitmap.CompressFormat.PNG, 100, userPhotoStream);
                    Image userPhotoImage = Image.getInstance(userPhotoStream.toByteArray());
                    userPhotoImage.scaleToFit(90, 90);

                    PdfPCell imageCell = new PdfPCell(userPhotoImage);
                    imageCell.setHorizontalAlignment(Element.ALIGN_TOP);
                    imageCell.setPadding(5);
                    imageCell.setColspan(2);
                    imageCell.setBorder(Rectangle.NO_BORDER);
                    tableCustomerPhoto.addCell(imageCell);
                    document.add(tableCustomerPhoto);
                }

            }

            //End second Row


            //3rd Row
            PdfPTable tableSocialDetails = new PdfPTable(3);
            tableSocialDetails.setWidths(new float[]{4.5f, 2f, 3.5f});
            tableSocialDetails.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Have you verified occupation, financial & social position, and personal habits of the Life to be assured as stated in the proposal form & found them to be correct? ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableSocialDetails.addCell(cell);

            cell = new PdfPCell(new Phrase(LASocialStatus, small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableSocialDetails.addCell(cell);

            cell = new PdfPCell(new Phrase(LAsocialDetailsString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableSocialDetails.addCell(cell);
            document.add(tableSocialDetails);
            //End 3rd Row

            //4th Row
            PdfPTable tableInjuryDetails = new PdfPTable(3);
            tableInjuryDetails.setWidths(new float[]{4.5f, 2f, 3.5f});
            tableInjuryDetails.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Do you have any knowledge of Life to be Assured suffering from any serious illness or injury or undergone any operation or medical investigation or aware of having any apparent physical deformity or mental retardation or any other circumstances that are likely to add to the risk?", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableInjuryDetails.addCell(cell);

            cell = new PdfPCell(new Phrase(injuryStatus, small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableInjuryDetails.addCell(cell);

            cell = new PdfPCell(new Phrase(injuryDetailsString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableInjuryDetails.addCell(cell);
            document.add(tableInjuryDetails);
            //End 4th Row

            //5th Row
            PdfPTable tableSatisFactionDetails = new PdfPTable(3);
            tableSatisFactionDetails.setWidths(new float[]{4.5f, 2f, 3.5f});
            tableSatisFactionDetails.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Are you fully satisfied with the overall profile & identity (including KYC details) of the Life to be assured & recommend for policy issuance?", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableSatisFactionDetails.addCell(cell);

            cell = new PdfPCell(new Phrase(satisfactionStatus, small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableSatisFactionDetails.addCell(cell);

            cell = new PdfPCell(new Phrase(satisfactionDetailsString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableSatisFactionDetails.addCell(cell);
            document.add(tableSatisFactionDetails);
            //End 5th Row

            document.add(para_img_logo_after_space_2);
            if (!(NBD_NOMINEE_RELATION_WITH_LA.equalsIgnoreCase("SON") ||
                    NBD_NOMINEE_RELATION_WITH_LA.equalsIgnoreCase("DAUGHTER") ||
                    NBD_NOMINEE_RELATION_WITH_LA.equalsIgnoreCase("WIFE") ||
                    NBD_NOMINEE_RELATION_WITH_LA.equalsIgnoreCase("HUSBAND") ||
                    NBD_NOMINEE_RELATION_WITH_LA.equalsIgnoreCase("MOTHER") ||
                    NBD_NOMINEE_RELATION_WITH_LA.equalsIgnoreCase("FATHER"))) {
                //1st Row
                PdfPTable tableJustifiationCoverApplied = new PdfPTable(3);
                tableJustifiationCoverApplied.setWidths(new float[]{4.5f, 2f, 3.5f});
                tableJustifiationCoverApplied.setWidthPercentage(100);

                cell = new PdfPCell(new Phrase("Justification for cover applied", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setColspan(2);
                tableJustifiationCoverApplied.addCell(cell);

                cell = new PdfPCell(new Phrase(justifiationCoverApplied, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setColspan(1);
                document.add(tableJustifiationCoverApplied);
                //End 1st Row
            } else if (!ST_ID.equalsIgnoreCase(NBD_MAILING_STATE) || !ST_ID.equalsIgnoreCase(NBD_PERMANENT_STATE)) {
                IAClarifiation = etIAClarifiation.getText().toString();

                //2nd Row
                PdfPTable tableIAClarifiation = new PdfPTable(3);
                tableIAClarifiation.setWidths(new float[]{4.5f, 2f, 3.5f});
                tableIAClarifiation.setWidthPercentage(100);

                cell = new PdfPCell(new Phrase("Clarification for proposal logged by IA in other than Home branch.", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setColspan(2);
                tableIAClarifiation.addCell(cell);

                cell = new PdfPCell(new Phrase(IAClarifiation, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setColspan(1);
                document.add(tableIAClarifiation);
                //End 2nd Row

            }

            if (NBD_PROP_QUALIFICATION.equalsIgnoreCase("illiterate ")) {

                //3rd Row
                PdfPTable tableApproximateAge = new PdfPTable(3);
                tableApproximateAge.setWidths(new float[]{4.5f, 2f, 3.5f});
                tableApproximateAge.setWidthPercentage(100);

                cell = new PdfPCell(new Phrase("Does the life assured looks older than what is declared in the proposal?If yes give the approximate age he/she looks like.", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                tableApproximateAge.addCell(cell);

                cell = new PdfPCell(new Phrase(approximateAge, small_bold));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                tableApproximateAge.addCell(cell);

                cell = new PdfPCell(new Phrase(approximateAgeDetails, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                tableApproximateAge.addCell(cell);
                document.add(tableApproximateAge);
                //End 3rd Row
            }


            PdfPTable CR_table21 = new PdfPTable(1);
            CR_table21.setWidthPercentage(100);

            PdfPCell CR_table21_cell1 = new PdfPCell(
                    new Paragraph("I hereby declare that I have discussed this case with the Advisor and confirm on the basis of my independent assessment and evaluation that the foregoing statements are correct.", small_normal));

            CR_table21_cell1.setPadding(5);
            CR_table21.addCell(CR_table21_cell1);
            document.add(CR_table21);

            document.add(para_img_logo_after_space_2);

            String str_sign_header;
            String code = strCIFBDMUserId;
            String name = commonMethods.getUserName(context);
            if (userType.equals("UM")) {
                str_sign_header = " (UM code- "
                        + strCIFBDMUserId + ")" + "\n" + " Name of UM- " + name + "\n"
                        + " Authenticated by Id & Password\n";
            } else if (userType.equals("CIF")) {
                str_sign_header = "(CIF code- "
                        + code
                        + ")"
                        + "\n"
                        + "Name of CIF- "
                        + name
                        + "\n"
                        + "Authenticated by Id & Password"
                        + "\n";

            } else if (userType.equals("BAP")) {
                str_sign_header = "(BAP code- "
                        + code
                        + ")"
                        + "\n"
                        + "Name of Broker- "
                        + name
                        + "\n"
                        + "Authenticated by Id & Password"
                        + "\n";

            } else if (userType.equals("CAG")) {
                str_sign_header = "(CAG code- "
                        + code
                        + ")"
                        + "\n"
                        + "Name of Corporate Agent- "
                        + name + "\n" + "Authenticated by Id & Password"
                        + "\n";

            } else if (userType.equals("IMF")) {
                str_sign_header = "Signature of Sales Representative(ISP code- "
                        + code
                        + ")"
                        + "\n"
                        + "ISP Name- "
                        + name
                        + "\n"
                        + "Authenticated by Id & Password"
                        + "\n";

            } else {
                str_sign_header = "Signature of Sales Representative";
            }


            String date = commonMethods.getCurrentMonthDate();
            PdfPTable CR_table22 = new PdfPTable(2);
            CR_table22.setWidthPercentage(100);

            PdfPCell CR_table22_cell2 = new PdfPCell(new Paragraph("Date : " + date,
                    small_normal));
            CR_table22_cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            CR_table22.addCell(CR_table22_cell2);

            PdfPCell CR_table22_cell1 = new PdfPCell(new Paragraph(
                    str_sign_header, small_bold));
            CR_table22_cell1.setPaddingBottom(Element.ALIGN_CENTER);
            CR_table22.addCell(CR_table22_cell1);
            document.add(CR_table22);

            document.close();
        } catch (Exception e) {

            Log.e("PDF_CPFActivity", e.toString()
                    + "Error while generating PDF");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_PHOTO_FILE) {
            if (resultCode == RESULT_OK) {

                try {

                    if (Check.equals("CustomerPhoto")) {
                        //File destinationFile = commonMethods.createCaptureImg( "_cust1Photo.jpg");
                        String textPrint = " Latitude: " + mLatLng.latitude + ", Longitude: " + mLatLng.longitude + "\n"
                                + "Date : " + commonMethods.getCurrentMonthDate() + "\n";
                        //image compression by bhalla
                        CompressImage.compressImage(customerPhotoFileName.getAbsolutePath());
                        //commonMethods.copyFile(new FileInputStream(customerPhotoFileName), new FileOutputStream(destinationFile));
                        customerPhotoBitmap = commonMethods.mergeBitmap(commonMethods.
                                        getBitmap(customerPhotoFileName.getAbsolutePath()),
                                commonMethods.convertStringToBitMap(context, textPrint));

                        commonMethods.storeImage(context, customerPhotoBitmap, customerPhotoFileName.getAbsolutePath());

                        imagePhotoGraphGPSCord.setImageBitmap(customerPhotoBitmap);
                        commonMethods.setFocusable(imagePhotoGraphGPSCord);
                        imagePhotoGraphGPSCord.requestFocus();

                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                Toast.makeText(context, "Data not receive", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void capture_all_docs() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String imageFileName = "custPhoto.jpg";

        //customerPhotoFileName = commonMethods.createCaptureImg(imageFileName);
        customerPhotoFileName = mStorageUtils.createFileToAppSpecificDir(context, imageFileName);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, commonMethods.getContentUri(context,
                    customerPhotoFileName));
        } else {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(customerPhotoFileName));
        }

        startActivityForResult(takePictureIntent, REQUEST_CODE_PICK_PHOTO_FILE);

    }

    private void proceedToShortMHR() {
        /*long sumAssured = Long.valueOf(globalDataList.get(selectedPosition).getSUMASSURED());
        if (sumAssured > 500000 && sumAssured <= 2500000) {
            llOTP.setVisibility(View.VISIBLE);

            llShortMHRQuestions.setVisibility(View.VISIBLE);
            llOTP.getParent().requestChildFocus(llOTP, llOTP);

            tvProposalNumber.setText(globalDataList.get(selectedPosition).getPROPOSALNUMBER());
            tvProposalName.setText(globalDataList.get(selectedPosition).getNAME());
            tvSumAssured.setText(globalDataList.get(selectedPosition).getSUMASSURED());
            tvIAName.setText(globalDataList.get(selectedPosition).getIANAME());
            tvIACode.setText(globalDataList.get(selectedPosition).getIACODE());
            tvRegion.setText(globalDataList.get(selectedPosition).getREGION());
            tvSUC.setText(globalDataList.get(selectedPosition).getSUC());
            tvChannel.setText(globalDataList.get(selectedPosition).getCHANNEL());
            checkBoxDisclaimer.setText("I hereby declare that I have discussed this case with the Advisor and confirm on the basis of my independent assessment and evaluation that the foregoing statements are correct.");
        } else {
            commonMethods.showMessageDialog(context, "You are not eligible for MHR");
        }*/

        llOTP.setVisibility(View.VISIBLE);
        llShortMHRQuestions.setVisibility(View.VISIBLE);
        llOTP.getParent().requestChildFocus(llOTP, llOTP);

        tvProposalNumber.setText(globalDataList.get(selectedPosition).getPROPOSALNUMBER());
        tvProposalName.setText(globalDataList.get(selectedPosition).getNAME());
        tvSumAssured.setText(globalDataList.get(selectedPosition).getSUMASSURED());
        tvIAName.setText(globalDataList.get(selectedPosition).getIANAME());
        tvIACode.setText(globalDataList.get(selectedPosition).getIACODE());
        tvRegion.setText(globalDataList.get(selectedPosition).getREGION());
        tvSUC.setText(globalDataList.get(selectedPosition).getSUC());
        tvChannel.setText(globalDataList.get(selectedPosition).getCHANNEL());
        checkBoxDisclaimer.setText("I hereby declare that I have discussed this case with the Advisor and confirm on the basis of my independent assessment and evaluation that the foregoing statements are correct.");
        mhrExtraUnderwritingDetailsAsync = new MHRExtraUnderwritingDetailsAsync(globalDataList.get(selectedPosition).getPROPOSALNUMBER());
        mhrExtraUnderwritingDetailsAsync.execute();
    }

    void removeProopsalNumber() {

        for (int i = 0; i <= globalDataList.size(); i++) {
            ParseXML.MHRReportValuesModel mhrReportValuesModel = globalDataList.get(i);
            if (mhrReportValuesModel.getPROPOSALNUMBER() == proposalNumber) {
                globalDataList.remove(globalDataList.get(i));
                break;
            }

        }
    }

    private void createSoapRequestToUploadDoc(final File newFilePath) {
        byte[] BI_bytes = new CommonMethods().read(newFilePath);

        String METHOD_NAME = "UploadFile_SMRT";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("f", org.kobjects.base64.Base64.encode(BI_bytes));
        request.addProperty("fileName", newFilePath.getName());
        request.addProperty("qNo", "");
        request.addProperty("agentCode", commonMethods.GetUserCode(context));
        request.addProperty("strEmailId", strCIFBDMEmailId);
        request.addProperty("strMobileNo", strCIFBDMMObileNo);
        request.addProperty("strAuthKey", commonMethods.getStrAuth());

        mAsyncUploadFileCommon = new AsyncUploadFile_Common(context, this, request, METHOD_NAME);
        mAsyncUploadFileCommon.execute();
    }

    @Override
    public void onUploadComplete(Boolean result) {

        if (result) {
            saveMHRProposalService = new SaveMHRProposalService();
            saveMHRProposalService.execute();

        } else {
            commonMethods.showMessageDialog(context, "PDF Upload Failed");
        }
    }

    class DownloadMHRReportAsync extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String status = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;

                request = new SoapObject(NAMESPACE, METHOD_NAME);
                //getMHRdetail_smrt(string strCode, string strFromdate,
                // string strTodate, string strEmailId, string strMobileNo, string strAuthKey)
                request.addProperty("strCode", strCIFBDMUserId);
                request.addProperty("strFromdate", fromDate);
                request.addProperty("strTodate", toDate);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", commonMethods.getStrAuth());

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                commonMethods.TLSv12Enable();


                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;
                androidHttpTranport.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();

                if (!response.toString().contentEquals("<NewDataSet />")) {
                    SoapPrimitive sa;
                    sa = (SoapPrimitive) envelope.getResponse();

                    String inputpolicylist = sa.toString();

                    ParseXML prsObj = new ParseXML();

                    inputpolicylist = prsObj.parseXmlTag(
                            inputpolicylist, "NewDataSet");
                    status = prsObj.parseXmlTag(
                            inputpolicylist, "ScreenData");

                    if (status == null) {
                        List<String> Node = prsObj.parseParentNode(
                                inputpolicylist, "Table");

                        ArrayList<ParseXML.MHRReportValuesModel> nodeData = prsObj
                                .parseNodeMHRReport(Node);
                        globalDataList.clear();
                        globalDataList.addAll(nodeData);
                        ParseXML.MHRReportValuesModel nodeVal = prsObj.new MHRReportValuesModel("Select Proposal Number",
                                "", "", "", "", "", "", "", "",
                                "", "", "",
                                "", 0);
                        globalDataList.add(0, nodeVal);
                        status = "Success";
                    }

                } else {
                    running = false;
                }
            } catch (Exception e) {
                running = false;
            }
            return null;

        }


        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            llShortMHRQuestions.setVisibility(View.GONE);
            llProposalListLayout.setVisibility(View.GONE);
            llCoverJustification.setVisibility(View.GONE);
            llIAClarifiation.setVisibility(View.GONE);
            llApproximateAge.setVisibility(View.GONE);
            llOTP.setVisibility(View.GONE);
            if (running) {
                if (status.equalsIgnoreCase("Success")) {
                    llProposalListLayout.setVisibility(View.VISIBLE);
                    ReadProposalNumbersAsynctask readProposalNumbersAsynctask = new ReadProposalNumbersAsynctask();
                    readProposalNumbersAsynctask.execute();
                } else {
                    clearList();
                    commonMethods.showMessageDialog(context, "No record found");
                }
            } else {
                commonMethods.showMessageDialog(context, "No record found");
                clearList();
            }
        }
    }

    private class ReadProposalNumbersAsynctask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.checkShortMHRProposalNumberExists(globalDataList);
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            SpinnerAdapter spinnerAdapter = new SpinnerAdapter(globalDataList);
            spinnerProopsalNumbers.setAdapter(spinnerAdapter);
        }
    }

    class GenerateOTPAsyncTask extends AsyncTask<String, String, String> {
        private final String SOAP_ACTION_GENERATE_PASSCODE = "http://tempuri.org/GenerateOTP_SMRT";
        private final String METHOD_NAME_GENERATE_PASSCODE = "GenerateOTP_SMRT";//GenerateOTP_SBIL
        int flag = 0;
        private volatile boolean running = true;

        //,mergedNACBIFile;
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
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_GENERATE_PASSCODE);
                //string ,string , string , string , string , string
                request.addProperty("strQuot", proposalNumber);
                request.addProperty("strProposalNo", proposalNumber);
                request.addProperty("strMobile", strCIFBDMMObileNo);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", commonMethods.getStrAuth());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

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
                    buttonValidateOTP.setEnabled(true);
                    buttonValidateOTP.setBackgroundColor(Color.parseColor("#00a1e3"));

                    buttonGenerateOTP.setEnabled(false);
                    buttonGenerateOTP.setBackgroundColor(Color.parseColor("#A9A9A9"));
                    etOTP.setVisibility(View.VISIBLE);
                    commonMethods.showMessageDialog(context, "OTP sent succesfully to your Mobile Number.");

                } else {
                    commonMethods.showMessageDialog(context, "Please try after sometime");
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

            } else {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                commonMethods.showMessageDialog(context, "Please try after sometime");
            }
        }
    }

    class ValidateOTPAsyncTask extends AsyncTask<String, String, String> {
        private final String SOAP_ACTION_VALIDATE_OTP = "http://tempuri.org/ValidateOTP_SMRT";
        private final String METHOD_NAME_VALIDATE_OTP = "ValidateOTP_SMRT";
        int flag = 0;
        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {

            try {

                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_VALIDATE_OTP);
                request.addProperty("strQuot", proposalNumber);
                request.addProperty("strProposalNo", proposalNumber);
                request.addProperty("strMobile", strCIFBDMMObileNo);
                request.addProperty("strOTP", OTP);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", commonMethods.getStrAuth());


                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

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

            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                if (flag == 1) {
                    authenticatePDFAsync = new AuthenticatePDFAsync("PDF");
                    authenticatePDFAsync.execute();

                } else {
                    buttonGenerateOTP.setEnabled(true);
                    buttonGenerateOTP.setBackgroundColor(Color.parseColor("#00a1e3"));
                    commonMethods.showMessageDialog(context, "Invalid Passcode. Please re-enter Passcode");
                }

            } else {
                buttonGenerateOTP.setEnabled(true);
                buttonGenerateOTP.setBackgroundColor(Color.parseColor("#00a1e3"));
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                commonMethods.showMessageDialog(context, "Invalid Passcode. Please re-enter Passcode");
            }
        }
    }

    class AuthenticatePDFAsync extends AsyncTask<String, String, String> {
        private final String passCode;
        int flag = 0;
        private volatile boolean running = true;
        private File newFilePath;

        private AuthenticatePDFAsync(String passCode) {
            this.passCode = passCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgressDialog != null) {
                mProgressDialog.show();
            } else {
                mProgressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                if (passCode.equalsIgnoreCase("PDF")) {
                    //CreateACRPdf();
                    CreateShortMHRPdf();
                } else {
                    newFilePath = mStorageUtils.createFileToAppSpecificDir(context, proposalNumber + "_ShortMHR_R01.pdf");
                    File oldPath = mStorageUtils.createFileToAppSpecificDir(context, proposalNumber + "_MHR.pdf");
                    String newBIPathName = newFilePath.getAbsolutePath();
                    manipulatePdf(oldPath.getAbsolutePath(), newBIPathName, passCode);
                    if (oldPath.exists()) {
                        oldPath.delete();
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
                        authenticatePDFAsync = new AuthenticatePDFAsync(OTP);
                        authenticatePDFAsync.execute();
                    } else {

                        createSoapRequestToUploadDoc(newFilePath);
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

    class SaveMHRProposalService extends AsyncTask<String, String, String> {
        int flag = 0;
        private volatile boolean running = true;

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

                String METHOD_NAME = "saveMHRdet_smrt";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("strProposalNo", proposalNumber);
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
                    etOTP.setText("");
                    buttonValidateOTP.setEnabled(false);
                    buttonValidateOTP.setBackgroundColor(Color.parseColor("#A9A9A9"));
                    buttonGenerateOTP.setEnabled(true);
                    buttonGenerateOTP.setBackgroundColor(Color.parseColor("#00a1e3"));

                    etOTP.setVisibility(View.GONE);
                    llOTP.setVisibility(View.GONE);

                    DatabaseHelper databaseHelper = new DatabaseHelper(context);
                    long result = databaseHelper.insertShortMHRProposal(proposalNumber, fromDate, toDate);
                    if (result > 0) {
                        removeProopsalNumber();
                        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(globalDataList);
                        spinnerProopsalNumbers.setAdapter(spinnerAdapter);
                        spinnerAdapter.notifyDataSetChanged();
                        gotoHomeDialog("PDF Uploaded Successfully");
                    }

                } else {
                    commonMethods.showMessageDialog(context, "PDF Upload Failed");
                }

            } else {
                commonMethods.showMessageDialog(context, "PDF Upload Failed");
            }
        }
    }

    class SpinnerAdapter extends BaseAdapter {

        private ArrayList<ParseXML.MHRReportValuesModel> allElementDetails;
        private LayoutInflater mInflater;
        public SpinnerAdapter(ArrayList<ParseXML.MHRReportValuesModel> results) {
            allElementDetails = results;
            mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return allElementDetails.size();
        }

        public Object getItem(int position) {
            return allElementDetails.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public int getPosition(String resource_type) {
            return allElementDetails.indexOf(resource_type);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.textview_default, parent,
                        false);
                holder = new ViewHolder();
                holder.c1 = convertView.findViewById(R.id.tv_content);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.c1.setText(String.valueOf(allElementDetails.get(position).getPROPOSALNUMBER()));

            return convertView;
        }

        class ViewHolder {
            TextView c1;
        }

    }

    class MHRExtraUnderwritingDetailsAsync extends AsyncTask<String, String, String> {
        private final String METHOD_NAME_EXTRA = "getShortMHRdet_smrt";
        private volatile boolean running = true;
        private String status = "", proposalNumber;

        MHRExtraUnderwritingDetailsAsync(String proposalNumber) {
            this.proposalNumber = proposalNumber;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
            NBD_NOMINEE_RELATION_WITH_LA = "";
            NBD_MAILING_STATE = "";
            NBD_PERMANENT_STATE = "";
            NBD_PROP_QUALIFICATION = "";
            ST_ID = "";
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;

                request = new SoapObject(NAMESPACE, METHOD_NAME_EXTRA);

                //getShortMHRdet_smrt(string strProposalNo, string strEmailId, string strMobileNo, string strAuthKey)
                request.addProperty("strProposalNo", proposalNumber);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", commonMethods.getStrAuth());

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                commonMethods.TLSv12Enable();


                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME_EXTRA;
                androidHttpTranport.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();

                if (!response.toString().contentEquals("<NewDataSet />")) {
                    SoapPrimitive sa;
                    sa = (SoapPrimitive) envelope.getResponse();

                    String inputpolicylist = sa.toString();

                    ParseXML prsObj = new ParseXML();

                    inputpolicylist = prsObj.parseXmlTag(
                            inputpolicylist, "NewDataSet");
                    status = prsObj.parseXmlTag(
                            inputpolicylist, "ScreenData");

                    if (status == null) {
                        List<String> Node = prsObj.parseParentNode(
                                inputpolicylist, "Table");


                        NBD_NOMINEE_RELATION_WITH_LA = prsObj.parseXmlTag(inputpolicylist, "NBD_NOMINEE_RELATION_WITH_LA");
                        NBD_NOMINEE_RELATION_WITH_LA = NBD_NOMINEE_RELATION_WITH_LA == null ? "" : NBD_NOMINEE_RELATION_WITH_LA;

                        NBD_MAILING_STATE = prsObj.parseXmlTag(inputpolicylist, "NBD_MAILING_STATE");
                        NBD_MAILING_STATE = NBD_MAILING_STATE == null ? "" : NBD_MAILING_STATE;

                        NBD_PERMANENT_STATE = prsObj.parseXmlTag(inputpolicylist, "NBD_PERMANENT_STATE");
                        NBD_PERMANENT_STATE = NBD_PERMANENT_STATE == null ? "" : NBD_PERMANENT_STATE;

                        NBD_PROP_QUALIFICATION = prsObj.parseXmlTag(inputpolicylist, "NBD_PROP_QUALIFICATION");
                        NBD_PROP_QUALIFICATION = NBD_PROP_QUALIFICATION == null ? "" : NBD_PROP_QUALIFICATION;

                        ST_ID = prsObj.parseXmlTag(inputpolicylist, "ST_ID");
                        ST_ID = ST_ID == null ? "" : ST_ID;
                        status = "Success";
                    }

                } else {
                    running = false;
                }
            } catch (Exception e) {
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
                if (status.equalsIgnoreCase("Success")) {
                    //Spouse/Children/Parents

                    if (!(NBD_NOMINEE_RELATION_WITH_LA.equalsIgnoreCase("SON") ||
                            NBD_NOMINEE_RELATION_WITH_LA.equalsIgnoreCase("DAUGHTER") ||
                            NBD_NOMINEE_RELATION_WITH_LA.equalsIgnoreCase("WIFE") ||
                            NBD_NOMINEE_RELATION_WITH_LA.equalsIgnoreCase("Spouse") ||
                            NBD_NOMINEE_RELATION_WITH_LA.equalsIgnoreCase("HUSBAND") ||
                            NBD_NOMINEE_RELATION_WITH_LA.equalsIgnoreCase("MOTHER") ||
                            NBD_NOMINEE_RELATION_WITH_LA.equalsIgnoreCase("FATHER")

                    )) {
                        llCoverJustification.setVisibility(View.VISIBLE);
                    }

                    if (!ST_ID.equalsIgnoreCase(NBD_MAILING_STATE) || !ST_ID.equalsIgnoreCase(NBD_PERMANENT_STATE)) {
                        llIAClarifiation.setVisibility(View.VISIBLE);
                    }

                    if (NBD_PROP_QUALIFICATION.equalsIgnoreCase("illiterate ")) {
                        llApproximateAge.setVisibility(View.VISIBLE);
                    }
                } else {
                    NBD_NOMINEE_RELATION_WITH_LA = "";
                    NBD_MAILING_STATE = "";
                    NBD_PERMANENT_STATE = "";
                    NBD_PROP_QUALIFICATION = "";
                    ST_ID = "";
                }
            } else {
                NBD_NOMINEE_RELATION_WITH_LA = "";
                NBD_MAILING_STATE = "";
                NBD_PERMANENT_STATE = "";
                NBD_PROP_QUALIFICATION = "";
                ST_ID = "";
            }
        }
    }
}
