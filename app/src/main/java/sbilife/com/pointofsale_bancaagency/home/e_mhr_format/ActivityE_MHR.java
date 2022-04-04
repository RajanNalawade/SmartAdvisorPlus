package sbilife.com.pointofsale_bancaagency.home.e_mhr_format;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.maps.model.LatLng;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.agent_on_boarding.Activity_AOB_Authentication;
import sbilife.com.pointofsale_bancaagency.branchlocator.GPSTracker;
import sbilife.com.pointofsale_bancaagency.common.AsyncUploadFile_Common;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CompressImage;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.home.CarouselHomeActivity;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class ActivityE_MHR extends AppCompatActivity implements View.OnClickListener, ServiceHits.DownLoadData,
        AsyncUploadFile_Common.Interface_Upload_File_Common {

    private final int PERMS_RUNTIME_REQ_CODE = 100, VIDEO_CAPTURE_CODE = 101,
            REQUEST_CODE_CAPTURE_SIGN_FILE = 102, REQUEST_CODE_CAPTURE_PIC_FILE = 103;
    private final String MHR_VIDEO_DOC = "mhr_video_doc";
    private final String MHR_PDF_DOC = "mhr_pdf_doc";
    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME_MHR_DETAILS_SMRT = "getMHRdetail_smrt";
    private final String METHOD_NAME_UPLOAD_ALL_DOC = "UploadFile_SMRT";
    private int DATE_DIALOG_ID = 0;
    private CommonMethods mCommonMethods;
    private StorageUtils mStorageUtils;
    private Context mContext;
    private String[] arrPermissionRuntime = {Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private LatLng mLatLng;
    private String strCompleteAddress = "", strProposerName = "", strRegion = "", strChannel = "",
            strPCName = "", strSUC = "", strSumAssured = "", strProposerNumber = "", str_doc = "";
    /*private TextView txt_mhr_proposer_meet_date2, txt_mhr_proposer_meet_date3;*/

    /*private EditText edt_mhr_details_if_no2, edt_mhr_details_if_no3,edt_mhr_proposer_meet_place2,
            edt_mhr_proposer_meet_place3;*/
    private long UPDATE_INTERVAL = 10 * 1000;

    /*private RadioGroup rg_mhr_personal_meet2, rg_mhr_personal_meet3;*/
    private long FASTEST_INTERVAL = 2 * 1000;

    /*private RadioButton rb_mhr_personal_meet_yes2, rb_mhr_personal_meet_yes3,rb_mhr_personal_meet_no2,rb_mhr_personal_meet_no3;*/
    private ProgressDialog mProgressDialog;

    private GPSTracker mGPGpsTracker;

    private TextView txt_mhr_proposer_name, txt_mhr_region, txt_mhr_channel, txt_mhr_pc_name,
            txt_mhr_proposer_name_details, txt_mhr_proposer_meet_date1,
            txt_mhr_personal_dob_details, txt_mhr_personal_age, txtMHRUploadVideo,
            txt_mhr_report_pic;
    private EditText edt_mhr_proposal_no, edt_mhr_sum_assured, edt_mhr_suc, edt_mhr_proposer_name_details, edt_mhr_details_if_no1,
            edt_mhr_proposer_meet_place1,
            edt_mhr_personal_address_details, edt_mhr_professional_qualification, edt_mhr_proposer_pep_details,
            edt_mhr_income_salary, edt_mhr_assets_fd, edt_mhr_income_business, edt_mhr_assets_ac, edt_mhr_income_agri,
            edt_mhr_assets_equity, edt_mhr_income_family, edt_mhr_assets_agri_land, edt_mhr_income_other,
            edt_mhr_assets_self_own, edt_mhr_income_total, edt_mhr_assets_total, edt_mhr_liability_loan,
            edt_mhr_liability_mortagage, edt_mhr_liability_total, edt_mhr_info_income_person, edt_mhr_nature_duration_buss,
            edt_mhr_name_add_emp, edt_mhr_turnover_year1, edt_mhr_turnover_year2, edt_mhr_turnover_year3,
            edt_mhr_investment_y1, edt_mhr_investment_y2, edt_mhr_investment_y3,
            edt_mhr_turnovr_y1, edt_mhr_turnovr_y2, edt_mhr_turnovr_y3, edt_mhr_profit_y1, edt_mhr_profit_y2,
            edt_mhr_profit_y3, edt_mhr_agri_occu_landholding, edt_mhr_agri_no_owners, edt_mhr_agri_share_in_land,
            edt_mhr_agri_corp_pattern, edt_mhr_agri_income_3yr, edt_mhr_agri_doc_verify, edt_mhr_agri_icome_other,
            rb_mhr_medical_status_details, edt_mhr_is_legal_risk_details, edt_mhr_oponion_recommend, edt_mhr_suc_last,
            edt_mhr_auth_suc, edt_mhr_place_last, edt_mhr_last_date, edtMHRNameDesign;
    private RadioGroup rg_mhr_details_check, rg_mhr_personal_meet1, rg_mhr_is_proposer_income/*, rg_mhr_medical_status, rg_mhr_is_legal_risk*/;
    private RadioButton rb_mhr_proposer_checked, rb_mhr_life_assured_checked, rb_mhr_premium_payer_checked,
            rb_mhr_personal_meet_yes1, rb_mhr_personal_meet_no1,
            rb_mhr_over_aged_y, rb_mhr_over_aged_n,
            rb_mhr_is_proposer_income_y, rb_mhr_is_proposer_income_n;

    private LinearLayout ll_mhr_enable_ui, ll_mhr_proposer_personal_meet_section1, ll_mhr_occupation_being_agri, ll_mhr_occupation_being_agri1,
            ll_mhr_occupation_being_agri2, ll_mhr_over_aged, ll_mhr_occupation_being_self_emp;
    /*private LinearLayout ll_mhr_proposer_personal_meet_section2,
            ll_mhr_proposer_personal_meet_section3;*/
    private RelativeLayout rl_mhr_upload_video;
    private VideoView vv_mhr_upload_video;
    private ImageView iv_mhr_upload_video_play, iv_mhr_upload_video_img, iv_mhr_report_pic;

    private Spinner spnr_mhr_personal_dob_doc, spnr_mhr_personal_Identity_doc, spnr_mhr_personal_address_doc,
            spnr_mhr_occupation_param, spnr_mhr_occupation_details, spnr_mhr_agri_occupation;

    private CheckBox cb_mhr_over_aged_y, cb_mhr_over_aged_n, cb_mhr_proposer_pep_cat_y, cb_mhr_proposer_pep_cat_n,
            cb_mhr_medical_status_yes, cb_mhr_medical_status_no, cb_mhr_is_legal_risk_y, cb_mhr_is_legal_risk_n;

    private Button btnSubmitMHR, btnSubmitProposal;
    private SharedPreferences mPreferences;
    private File mMHRVideoFile, mMHRPdf, mMHRReportPic;
    private boolean isVideoTaken = false;
    private Calendar myCalendar = Calendar.getInstance();
    private MediaController mediaController;
    private Bitmap videoImgBitmap;
    private byte[] mAllBytes;
    private GetMHRDetails mGetMHRDetails;
    private AsyncUploadFile_Common mAsyncUploadFileCommon;

    private Date currentDate;
    DatePickerDialog.OnDateSetListener datePickListMeetDate1 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            String strSelectedDate = (dayOfMonth < 10 ? "0" : "") + dayOfMonth + "-"
                    + ((monthOfYear + 1) < 10 ? "0" : "") + (monthOfYear + 1) + "-"
                    + year;

            try {

                SimpleDateFormat sdp = new SimpleDateFormat("dd-MM-yyyy");
                Date selectedDate = sdp.parse(strSelectedDate);

                switch (DATE_DIALOG_ID) {
                    case R.id.txt_mhr_proposer_meet_date1:

                        if (!selectedDate.after(currentDate)) {
                            txt_mhr_proposer_meet_date1.setText(strSelectedDate);
                        } else {
                            mCommonMethods.showMessageDialog(mContext, "Future date not allowed");
                        }

                        break;

                    case R.id.txt_mhr_personal_dob_details:

                        if (!selectedDate.after(currentDate) && !strSelectedDate.equals(sdp.format(currentDate))) {
                            txt_mhr_personal_dob_details.setText(strSelectedDate);
                        } else {
                            mCommonMethods.showMessageDialog(mContext, "Future / current date not allowed");
                        }
                        break;
                }

            } catch (ParseException e) {
                e.printStackTrace();
                mCommonMethods.showToast(mContext, "parse error \n" + e.getMessage());
            }

        }
    };
    private int mDOBYear, mDOBMonth, mDOBDay;

    private LocationManager locationmanager;
    private ServiceHits service;
    private ActivityResultLauncher<Intent> mMHRVideoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            if (result.getResultCode() == RESULT_OK) {

                mCommonMethods.showToast(mContext, "Video has been saved to:\n" +
                        result.getData().getData());

                try {

                    /*rl_mhr_upload_video.setVisibility(View.VISIBLE);
                    vv_mhr_upload_video.setVisibility(View.GONE);
                    iv_mhr_upload_video_img.setVisibility(View.VISIBLE);
                    iv_mhr_upload_video_play.setVisibility(View.VISIBLE);

                    videoImgBitmap = createVideoThumbNail(mMHRVideoFile.getPath());
                    iv_mhr_upload_video_img.setImageBitmap(videoImgBitmap);

                    vv_mhr_upload_video.setVideoURI(Uri.fromFile(mMHRVideoFile));

                    isVideoTaken = true;*/

                    //new VideoCompressAsyncTask(mContext).execute();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    isVideoTaken = false;

                    rl_mhr_upload_video.setVisibility(View.GONE);
                }

            } else if (result.getResultCode() == RESULT_CANCELED) {
                mCommonMethods.showToast(mContext, "Video recording cancelled.");
                isVideoTaken = false;
                rl_mhr_upload_video.setVisibility(View.GONE);
            } else {
                mCommonMethods.showToast(mContext, "Failed to record video");
                isVideoTaken = false;
                rl_mhr_upload_video.setVisibility(View.GONE);
            }

        }
    });

    private ActivityResultLauncher<Intent> mMHRImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                if (mMHRReportPic != null) {

                    Bitmap imageBitmap = BitmapFactory.decodeFile(mMHRReportPic.getAbsolutePath());
                    imageBitmap = mCommonMethods.exif(mMHRReportPic.getAbsolutePath(), imageBitmap);

                    CompressImage.compressImage(mMHRReportPic.getAbsolutePath());
                    if (imageBitmap != null && mCommonMethods.DetectFace(mContext, imageBitmap)) {

                        iv_mhr_report_pic.setVisibility(View.VISIBLE);
                        iv_mhr_report_pic.setImageURI(Uri.fromFile(mMHRReportPic));

                    } else {
                        mCommonMethods.showToast(mContext, "Please capture proper image");
                    }
                }

            } else if (result.getResultCode() == RESULT_CANCELED) {
                mCommonMethods.showToast(mContext, "Capturing cancelled.");
                iv_mhr_report_pic.setVisibility(View.GONE);
            } else {
                mCommonMethods.showToast(mContext, "Failed to capture");
                iv_mhr_report_pic.setVisibility(View.GONE);
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_e_mhr);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        initViews();
        //call auto Populate data api
    }

    private void initViews() {

        mContext = this;
        mCommonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        mCommonMethods.setApplicationToolbarMenu(this, "e-MHR");

        mPreferences = getPreferences(MODE_PRIVATE);

        mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);

        mediaController = new MediaController(this);

        mGPGpsTracker = new GPSTracker(this);

        locationmanager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);

        Calendar c = Calendar.getInstance();
        mDOBYear = c.get(Calendar.YEAR);
        mDOBMonth = c.get(Calendar.MONTH);
        mDOBDay = c.get(Calendar.DAY_OF_MONTH);

        currentDate = new Date(c.getTimeInMillis());

        edt_mhr_proposal_no = findViewById(R.id.edt_mhr_proposal_no);
        txt_mhr_proposer_name = findViewById(R.id.txt_mhr_proposer_name);
        txt_mhr_region = findViewById(R.id.txt_mhr_region);
        txt_mhr_channel = findViewById(R.id.txt_mhr_channel);
        txt_mhr_pc_name = findViewById(R.id.txt_mhr_pc_name);
        txt_mhr_proposer_name_details = findViewById(R.id.txt_mhr_proposer_name_details);
        txt_mhr_proposer_meet_date1 = findViewById(R.id.txt_mhr_proposer_meet_date1);
        txt_mhr_proposer_meet_date1.setOnClickListener(this);
        /*txt_mhr_proposer_meet_date2 = findViewById(R.id.txt_mhr_proposer_meet_date2);
        txt_mhr_proposer_meet_date3 = findViewById(R.id.txt_mhr_proposer_meet_date3);*/
        txt_mhr_personal_dob_details = findViewById(R.id.txt_mhr_personal_dob_details);
        txt_mhr_personal_dob_details.setOnClickListener(this);

        txt_mhr_personal_age = findViewById(R.id.txt_mhr_personal_age);

        edt_mhr_sum_assured = findViewById(R.id.edt_mhr_sum_assured);
        edt_mhr_suc = findViewById(R.id.edt_mhr_suc);
        edt_mhr_proposer_name_details = findViewById(R.id.edt_mhr_proposer_name_details);
        edt_mhr_details_if_no1 = findViewById(R.id.edt_mhr_details_if_no1);
        /*edt_mhr_details_if_no2 = findViewById(R.id.edt_mhr_details_if_no2);
        edt_mhr_details_if_no3 = findViewById(R.id.edt_mhr_details_if_no3);*/
        edt_mhr_proposer_meet_place1 = findViewById(R.id.edt_mhr_proposer_meet_place1);
        /*edt_mhr_proposer_meet_place2 = findViewById(R.id.edt_mhr_proposer_meet_place2);
        edt_mhr_proposer_meet_place3 = findViewById(R.id.edt_mhr_proposer_meet_place3);*/

        edt_mhr_personal_address_details = findViewById(R.id.edt_mhr_personal_address_details);
        edt_mhr_professional_qualification = findViewById(R.id.edt_mhr_professional_qualification);
        edt_mhr_proposer_pep_details = findViewById(R.id.edt_mhr_proposer_pep_details);
        edt_mhr_income_salary = findViewById(R.id.edt_mhr_income_salary);
        edt_mhr_assets_fd = findViewById(R.id.edt_mhr_assets_fd);
        edt_mhr_income_business = findViewById(R.id.edt_mhr_income_business);
        edt_mhr_assets_ac = findViewById(R.id.edt_mhr_assets_ac);
        edt_mhr_income_agri = findViewById(R.id.edt_mhr_income_agri);
        edt_mhr_assets_equity = findViewById(R.id.edt_mhr_assets_equity);
        edt_mhr_income_family = findViewById(R.id.edt_mhr_income_family);
        edt_mhr_assets_agri_land = findViewById(R.id.edt_mhr_assets_agri_land);
        edt_mhr_income_other = findViewById(R.id.edt_mhr_income_other);
        edt_mhr_assets_self_own = findViewById(R.id.edt_mhr_assets_self_own);
        edt_mhr_income_total = findViewById(R.id.edt_mhr_income_total);
        edt_mhr_assets_total = findViewById(R.id.edt_mhr_assets_total);
        edt_mhr_liability_loan = findViewById(R.id.edt_mhr_liability_loan);
        edt_mhr_liability_mortagage = findViewById(R.id.edt_mhr_liability_mortagage);
        edt_mhr_liability_total = findViewById(R.id.edt_mhr_liability_total);
        edt_mhr_info_income_person = findViewById(R.id.edt_mhr_info_income_person);
        edt_mhr_nature_duration_buss = findViewById(R.id.edt_mhr_nature_duration_buss);
        edt_mhr_name_add_emp = findViewById(R.id.edt_mhr_name_add_emp);

        edt_mhr_turnover_year1 = findViewById(R.id.edt_mhr_turnover_year1);
        edt_mhr_turnover_year2 = findViewById(R.id.edt_mhr_turnover_year2);
        edt_mhr_turnover_year3 = findViewById(R.id.edt_mhr_turnover_year3);

        edt_mhr_investment_y1 = findViewById(R.id.edt_mhr_investment_y1);
        edt_mhr_investment_y2 = findViewById(R.id.edt_mhr_investment_y2);
        edt_mhr_investment_y3 = findViewById(R.id.edt_mhr_investment_y3);
        edt_mhr_turnovr_y1 = findViewById(R.id.edt_mhr_turnovr_y1);
        edt_mhr_turnovr_y2 = findViewById(R.id.edt_mhr_turnovr_y2);
        edt_mhr_turnovr_y3 = findViewById(R.id.edt_mhr_turnovr_y3);
        edt_mhr_profit_y1 = findViewById(R.id.edt_mhr_profit_y1);
        edt_mhr_profit_y2 = findViewById(R.id.edt_mhr_profit_y2);
        edt_mhr_profit_y3 = findViewById(R.id.edt_mhr_profit_y3);
        edt_mhr_agri_occu_landholding = findViewById(R.id.edt_mhr_agri_occu_landholding);
        edt_mhr_agri_no_owners = findViewById(R.id.edt_mhr_agri_no_owners);
        edt_mhr_agri_share_in_land = findViewById(R.id.edt_mhr_agri_share_in_land);
        edt_mhr_agri_corp_pattern = findViewById(R.id.edt_mhr_agri_corp_pattern);
        edt_mhr_agri_income_3yr = findViewById(R.id.edt_mhr_agri_income_3yr);
        edt_mhr_agri_doc_verify = findViewById(R.id.edt_mhr_agri_doc_verify);
        edt_mhr_agri_icome_other = findViewById(R.id.edt_mhr_agri_icome_other);
        rb_mhr_medical_status_details = findViewById(R.id.rb_mhr_medical_status_details);
        edt_mhr_is_legal_risk_details = findViewById(R.id.edt_mhr_is_legal_risk_details);
        edt_mhr_oponion_recommend = findViewById(R.id.edt_mhr_oponion_recommend);
        edt_mhr_suc_last = findViewById(R.id.edt_mhr_suc_last);
        edt_mhr_auth_suc = findViewById(R.id.edt_mhr_auth_suc);
        edt_mhr_place_last = findViewById(R.id.edt_mhr_place_last);
        //set Current Date
        edt_mhr_last_date = findViewById(R.id.edt_mhr_last_date);

        edtMHRNameDesign = findViewById(R.id.edtMHRNameDesign);

        rg_mhr_details_check = findViewById(R.id.rg_mhr_details_check);
        rg_mhr_personal_meet1 = findViewById(R.id.rg_mhr_personal_meet1);
        /*rg_mhr_personal_meet2 = findViewById(R.id.rg_mhr_personal_meet2);
        rg_mhr_personal_meet3 = findViewById(R.id.rg_mhr_personal_meet3);*/

        rg_mhr_is_proposer_income = findViewById(R.id.rg_mhr_is_proposer_income);
        /*rg_mhr_medical_status = findViewById(R.id.rg_mhr_medical_status);
        rg_mhr_is_legal_risk = findViewById(R.id.rg_mhr_is_legal_risk);*/

        rb_mhr_proposer_checked = findViewById(R.id.rb_mhr_proposer_checked);
        rb_mhr_life_assured_checked = findViewById(R.id.rb_mhr_life_assured_checked);
        rb_mhr_premium_payer_checked = findViewById(R.id.rb_mhr_premium_payer_checked);
        rb_mhr_personal_meet_yes1 = findViewById(R.id.rb_mhr_personal_meet_yes1);
        /*rb_mhr_personal_meet_yes2 = findViewById(R.id.rb_mhr_personal_meet_yes2);
        rb_mhr_personal_meet_yes3 = findViewById(R.id.rb_mhr_personal_meet_yes3);*/
        rb_mhr_personal_meet_no1 = findViewById(R.id.rb_mhr_personal_meet_no1);
        /*rb_mhr_personal_meet_no2 = findViewById(R.id.rb_mhr_personal_meet_no2);
        rb_mhr_personal_meet_no3 = findViewById(R.id.rb_mhr_personal_meet_no3);*/
        cb_mhr_over_aged_y = findViewById(R.id.cb_mhr_over_aged_y);
        cb_mhr_over_aged_n = findViewById(R.id.cb_mhr_over_aged_n);
        cb_mhr_proposer_pep_cat_y = findViewById(R.id.cb_mhr_proposer_pep_cat_y);
        cb_mhr_proposer_pep_cat_n = findViewById(R.id.cb_mhr_proposer_pep_cat_n);
        rb_mhr_is_proposer_income_y = findViewById(R.id.rb_mhr_is_proposer_income_y);
        rb_mhr_is_proposer_income_n = findViewById(R.id.rb_mhr_is_proposer_income_n);

        cb_mhr_medical_status_yes = findViewById(R.id.cb_mhr_medical_status_yes);
        cb_mhr_medical_status_no = findViewById(R.id.cb_mhr_medical_status_no);
        cb_mhr_is_legal_risk_y = findViewById(R.id.cb_mhr_is_legal_risk_y);
        cb_mhr_is_legal_risk_n = findViewById(R.id.cb_mhr_is_legal_risk_n);

        ll_mhr_enable_ui = findViewById(R.id.ll_mhr_enable_ui);
        ll_mhr_enable_ui.setVisibility(View.GONE);

        ll_mhr_proposer_personal_meet_section1 = findViewById(R.id.ll_mhr_proposer_personal_meet_section1);
        /*ll_mhr_proposer_personal_meet_section2 = findViewById(R.id.ll_mhr_proposer_personal_meet_section2);
        ll_mhr_proposer_personal_meet_section3 = findViewById(R.id.ll_mhr_proposer_personal_meet_section3);*/

        ll_mhr_occupation_being_self_emp = findViewById(R.id.ll_mhr_occupation_being_self_emp);

        ll_mhr_occupation_being_agri = findViewById(R.id.ll_mhr_occupation_being_agri);
        ll_mhr_occupation_being_agri1 = findViewById(R.id.ll_mhr_occupation_being_agri1);
        ll_mhr_occupation_being_agri2 = findViewById(R.id.ll_mhr_occupation_being_agri2);

        ll_mhr_over_aged = findViewById(R.id.ll_mhr_over_aged);

        rl_mhr_upload_video = findViewById(R.id.rl_mhr_upload_video);
        vv_mhr_upload_video = findViewById(R.id.vv_mhr_upload_video);
        iv_mhr_upload_video_play = findViewById(R.id.iv_mhr_upload_video_play);
        iv_mhr_upload_video_play.setOnClickListener(this);
        iv_mhr_upload_video_img = findViewById(R.id.iv_mhr_upload_video_img);

        iv_mhr_report_pic = findViewById(R.id.iv_mhr_report_pic);

        txtMHRUploadVideo = findViewById(R.id.txtMHRUploadVideo);
        txtMHRUploadVideo.setOnClickListener(this);

        txt_mhr_report_pic = findViewById(R.id.txt_mhr_report_pic);
        txt_mhr_report_pic.setOnClickListener(this);

        btnSubmitMHR = findViewById(R.id.btnSubmitMHR);
        btnSubmitMHR.setOnClickListener(this);

        btnSubmitProposal = findViewById(R.id.btnSubmitProposal);
        btnSubmitProposal.setOnClickListener(this);

        spnr_mhr_personal_dob_doc = findViewById(R.id.spnr_mhr_personal_dob_doc);
        mCommonMethods.fillSpinnerValue(mContext, spnr_mhr_personal_dob_doc,
                Arrays.asList(getResources().getStringArray(R.array.arr_mhr_personal_dob_details)));

        spnr_mhr_personal_Identity_doc = findViewById(R.id.spnr_mhr_personal_Identity_doc);
        mCommonMethods.fillSpinnerValue(mContext, spnr_mhr_personal_Identity_doc,
                Arrays.asList(getResources().getStringArray(R.array.arr_mhr_personal_identity_details)));

        spnr_mhr_personal_address_doc = findViewById(R.id.spnr_mhr_personal_address_doc);
        mCommonMethods.fillSpinnerValue(mContext, spnr_mhr_personal_address_doc,
                Arrays.asList(getResources().getStringArray(R.array.arr_mhr_personal_address_details)));

        spnr_mhr_occupation_param = findViewById(R.id.spnr_mhr_occupation_param);
        mCommonMethods.fillSpinnerValue(mContext, spnr_mhr_occupation_param,
                Arrays.asList(getResources().getStringArray(R.array.arr_mhr_occupation_verification)));

        spnr_mhr_occupation_details = findViewById(R.id.spnr_mhr_occupation_details);
        mCommonMethods.fillSpinnerValue(mContext, spnr_mhr_occupation_details,
                Arrays.asList(getResources().getStringArray(R.array.arr_mhr_occupation_details)));

        spnr_mhr_agri_occupation = findViewById(R.id.spnr_mhr_agri_occupation);
        mCommonMethods.fillSpinnerValue(mContext, spnr_mhr_agri_occupation,
                Arrays.asList(getResources().getStringArray(R.array.arr_mhr_agri_ownership)));

        edtMHRNameDesign.setText(mCommonMethods.GetUserType(mContext) + "-" + mCommonMethods.getUserName(mContext));

        rg_mhr_details_check.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_mhr_proposer_checked:

                        txt_mhr_proposer_name_details.setText("Name of Proposer");

                        break;

                    case R.id.rb_mhr_life_assured_checked:

                        txt_mhr_proposer_name_details.setText("Name of Life to be Assured");

                        break;

                    case R.id.rb_mhr_premium_payer_checked:

                        txt_mhr_proposer_name_details.setText("Name of Premium Payer");

                        break;
                }
            }
        });

        rg_mhr_personal_meet1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {

                    case R.id.rb_mhr_personal_meet_yes1:

                        ll_mhr_proposer_personal_meet_section1.setVisibility(View.VISIBLE);
                        edt_mhr_details_if_no1.setVisibility(View.GONE);

                        break;

                    case R.id.rb_mhr_personal_meet_no1:

                        ll_mhr_proposer_personal_meet_section1.setVisibility(View.GONE);
                        edt_mhr_details_if_no1.setVisibility(View.VISIBLE);

                        break;
                }

            }
        });

        /*rg_mhr_personal_meet2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i){

                    case R.id.rb_mhr_personal_meet_yes2:
                        ll_mhr_proposer_personal_meet_section2.setVisibility(View.GONE);
                        break;

                    case R.id.rb_mhr_personal_meet_no2:
                        ll_mhr_proposer_personal_meet_section2.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });*/

        /*rg_mhr_personal_meet3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i){

                    case R.id.rb_mhr_personal_meet_yes3:
                        ll_mhr_proposer_personal_meet_section3.setVisibility(View.GONE);
                        break;

                    case R.id.rb_mhr_personal_meet_no3:
                        ll_mhr_proposer_personal_meet_section3.setVisibility(View.VISIBLE);
                        break;
                }

            }
        });*/

        changeIncomeTotal(edt_mhr_income_salary);

        changeIncomeTotal(edt_mhr_income_business);

        changeIncomeTotal(edt_mhr_income_agri);

        changeIncomeTotal(edt_mhr_income_family);

        changeIncomeTotal(edt_mhr_income_other);

        //assets listener
        changeAssetsTotal(edt_mhr_assets_fd);
        changeAssetsTotal(edt_mhr_assets_ac);
        changeAssetsTotal(edt_mhr_assets_equity);
        changeAssetsTotal(edt_mhr_assets_agri_land);
        changeAssetsTotal(edt_mhr_assets_self_own);

        //liability listener
        changeLiabilityTotal(edt_mhr_liability_loan);
        changeLiabilityTotal(edt_mhr_liability_mortagage);

        //investment year
        validateInvestmentYear(edt_mhr_investment_y1);
        validateInvestmentYear(edt_mhr_turnovr_y1);
        validateInvestmentYear(edt_mhr_profit_y1);

        //turnover year
        validateTurnoverYear(edt_mhr_investment_y2);
        validateTurnoverYear(edt_mhr_turnovr_y2);
        validateTurnoverYear(edt_mhr_profit_y2);

        //profit year
        validateProfitYear(edt_mhr_investment_y3);
        validateProfitYear(edt_mhr_turnovr_y3);
        validateProfitYear(edt_mhr_profit_y3);

        cb_mhr_over_aged_y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_mhr_over_aged_y.isChecked() && cb_mhr_over_aged_n.isChecked())
                    cb_mhr_over_aged_n.setChecked(false);
            }
        });

        cb_mhr_over_aged_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_mhr_over_aged_n.isChecked() && cb_mhr_over_aged_y.isChecked()) {
                    cb_mhr_over_aged_y.setChecked(false);
                }
            }
        });

        cb_mhr_proposer_pep_cat_y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_mhr_proposer_pep_cat_y.isChecked() && cb_mhr_proposer_pep_cat_n.isChecked())
                    cb_mhr_proposer_pep_cat_n.setChecked(false);

                edt_mhr_proposer_pep_details.setVisibility(View.VISIBLE);
            }
        });

        cb_mhr_proposer_pep_cat_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_mhr_proposer_pep_cat_n.isChecked() && cb_mhr_proposer_pep_cat_y.isChecked())
                    cb_mhr_proposer_pep_cat_y.setChecked(false);

                edt_mhr_proposer_pep_details.setVisibility(View.GONE);
            }
        });

        cb_mhr_medical_status_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cb_mhr_medical_status_yes.isChecked() && cb_mhr_medical_status_no.isChecked())
                    cb_mhr_medical_status_no.setChecked(false);

                rb_mhr_medical_status_details.setVisibility(View.VISIBLE);

            }
        });
        cb_mhr_medical_status_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cb_mhr_medical_status_no.isChecked() && cb_mhr_medical_status_yes.isChecked())
                    cb_mhr_medical_status_yes.setChecked(false);

                rb_mhr_medical_status_details.setVisibility(View.GONE);

            }
        });

        cb_mhr_is_legal_risk_y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cb_mhr_is_legal_risk_y.isChecked() && cb_mhr_is_legal_risk_n.isChecked())
                    cb_mhr_is_legal_risk_n.setChecked(false);

                edt_mhr_is_legal_risk_details.setVisibility(View.VISIBLE);

            }
        });
        cb_mhr_is_legal_risk_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cb_mhr_is_legal_risk_n.isChecked() && cb_mhr_is_legal_risk_y.isChecked())
                    cb_mhr_is_legal_risk_y.setChecked(false);

                edt_mhr_is_legal_risk_details.setVisibility(View.GONE);

            }
        });


        spnr_mhr_occupation_details.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Agriculturist
                if (i == 3 || i == 4) {

                    ll_mhr_occupation_being_self_emp.setVisibility(View.VISIBLE);

                } else if (i == 5) {
                    ll_mhr_occupation_being_agri.setVisibility(View.VISIBLE);
                    ll_mhr_occupation_being_agri1.setVisibility(View.VISIBLE);
                    ll_mhr_occupation_being_agri2.setVisibility(View.VISIBLE);
                } else {
                    ll_mhr_occupation_being_agri.setVisibility(View.GONE);
                    ll_mhr_occupation_being_agri1.setVisibility(View.GONE);
                    ll_mhr_occupation_being_agri2.setVisibility(View.GONE);

                    ll_mhr_occupation_being_self_emp.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        int dayOfMonth = myCalendar.get(Calendar.DAY_OF_MONTH);
        int monthOfYear = myCalendar.get(Calendar.MONTH);
        int year = myCalendar.get(Calendar.YEAR);

        String strCurrentDate = (dayOfMonth < 10 ? "0" : "") + dayOfMonth + "-"
                + ((monthOfYear + 1) < 10 ? "0" : "") + (monthOfYear + 1) + "-"
                + year;

        edt_mhr_last_date.setText(strCurrentDate);

    }

    public void submitMHR() {

        String strValidError = validateDetails();
        if (strValidError.equals("")) {

            //create pdf
            createMHRPdf();

            service_hits();

        } else {
            mCommonMethods.showMessageDialog(mContext, strValidError);
        }
    }

    public void upload_docs() {

        FileInputStream fin = null;

        try {

            if (str_doc.equals(MHR_VIDEO_DOC)) {
                if (mMHRVideoFile != null) {
                    fin = new FileInputStream(mMHRVideoFile);
                }
            } else if (str_doc.equals(MHR_PDF_DOC)) {
                if (mMHRPdf != null) {
                    fin = new FileInputStream(mMHRPdf);
                }
            }

            if (fin != null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] b = new byte[1024];
                int bytesRead = 0;
                try {
                    while ((bytesRead = fin.read(b)) != -1) {
                        bos.write(b, 0, bytesRead);
                    }

                    mAllBytes = bos.toByteArray();
                    bos.flush();
                    bos.close();

                    //upload document
                    createSoapRequestToUploadDoc();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(mContext, "Please Browse/Capture Document", Toast.LENGTH_LONG).show();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String validateDetails() {

        String strSumAssured = edt_mhr_sum_assured.getText().toString();
        strSumAssured = strSumAssured == null ? "0" : strSumAssured;

        String strSUC = edt_mhr_suc.getText().toString();
        strSUC = strSUC == null ? "" : strSUC;

        String strProposerName = edt_mhr_proposer_name_details.getText().toString();
        strProposerName = strProposerName == null ? "" : strProposerName;

        /*if (strSumAssured.equals("0")) {
            edt_mhr_sum_assured.requestFocus();
            return "Please Enter Sum Assured.";
        }
        if (strSUC.equals("")) {
            edt_mhr_suc.requestFocus();
            return "Please Enter SUC.";
        }*/

        if (strProposerName.equals("")) {

            edt_mhr_proposer_name_details.requestFocus();

            if (rb_mhr_proposer_checked.isChecked())
                return "Please Enter Name of Proposer.";

            else if (rb_mhr_life_assured_checked.isChecked())
                return "Please Enter Name of Life to be Assured.";

            else if (rb_mhr_premium_payer_checked.isChecked())
                return "Please Enter Name of Premium Payer.";
        }
        if (rb_mhr_personal_meet_yes1.isChecked()) {

            String strMeetDate1 = txt_mhr_proposer_meet_date1.getText().toString();
            strMeetDate1 = strMeetDate1 == null ? "" : strMeetDate1;

            String strMeetPlace1 = edt_mhr_proposer_meet_place1.getText().toString();
            strMeetPlace1 = strMeetPlace1 == null ? "" : strMeetPlace1;

            if (strMeetDate1.equals("")) {
                txt_mhr_proposer_meet_date1.requestFocus();
                return "Please Select Date of Meeting 1st";
            } else if (strMeetPlace1.equals("")) {
                edt_mhr_proposer_meet_place1.requestFocus();
                return "Please Select Place of Meeting 1st";
            }
        } else {
            String strPersonalMeetNo1 = edt_mhr_details_if_no1.getText().toString();
            strPersonalMeetNo1 = strPersonalMeetNo1 == null ? "" : strPersonalMeetNo1;

            if (strPersonalMeetNo1.equals("")) {
                edt_mhr_details_if_no1.requestFocus();
                return "Please Enter How you have gathered the information about him/her";
            }
        }

        /*if (rb_mhr_personal_meet_yes2.isChecked()){

            String strMeetDate2 = txt_mhr_proposer_meet_date2.getText().toString();
            strMeetDate2 = strMeetDate2 == null ? "" : strMeetDate2;

            String strMeetPlace2 = edt_mhr_proposer_meet_place2.getText().toString();
            strMeetPlace2 = strMeetPlace2 == null ? "" : strMeetPlace2;

            if (strMeetDate2.equals("")){
                txt_mhr_proposer_meet_date2.requestFocus();
                return "Please Select Date of Meeting 2nd";
            }else if (strMeetPlace2.equals("")){
                edt_mhr_proposer_meet_place2.requestFocus();
                return "Please Select Place of Meeting 2nd";
            }
        }else{
            String strPersonalMeetNo2 = edt_mhr_details_if_no2.getText().toString();
            strPersonalMeetNo2 = strPersonalMeetNo2== null ? "" : strPersonalMeetNo2;

            if (strPersonalMeetNo2.equals("")){
                edt_mhr_details_if_no2.requestFocus();
                return "Please Enter How you have gathered the information about him/her";
            }
        }*/
        /*if (rb_mhr_personal_meet_yes3.isChecked()){

            String strMeetDate3 = txt_mhr_proposer_meet_date3.getText().toString();
            strMeetDate3 = strMeetDate3 == null ? "" : strMeetDate3;

            String strMeetPlace3 = edt_mhr_proposer_meet_place3.getText().toString();
            strMeetPlace3 = strMeetPlace3 == null ? "" : strMeetPlace3;

            if (strMeetDate3.equals("")){
                txt_mhr_proposer_meet_date3.requestFocus();
                return "Please Select Date of Meeting 3rd";
            }else if (strMeetPlace3.equals("")){
                edt_mhr_proposer_meet_place3.requestFocus();
                return "Please Select Place of Meeting 3rd";
            }
        }else{
            String strPersonalMeetNo3 = edt_mhr_details_if_no3.getText().toString();
            strPersonalMeetNo3 = strPersonalMeetNo3== null ? "" : strPersonalMeetNo3;

            if (strPersonalMeetNo3.equals("")){
                edt_mhr_details_if_no3.requestFocus();
                return "Please Enter How you have gathered the information about him/her";
            }
        }*/

        String strPerDOBDetails = txt_mhr_personal_dob_details.getText().toString();
        strPerDOBDetails = strPerDOBDetails == null ? "" : strPerDOBDetails;

        String strPerAgeDetails = txt_mhr_personal_age.getText().toString();
        strPerAgeDetails = strPerAgeDetails == null ? "" : strPerAgeDetails;

        if (strPerDOBDetails.equals("")) {
            txt_mhr_personal_dob_details.requestFocus();
            return "Please select details of DOB of life assured";
        }

        if (strPerAgeDetails.equals("")) {
            txt_mhr_personal_dob_details.requestFocus();
            return "Please select details of DOB of life assured";
        }

        if (spnr_mhr_personal_dob_doc.getSelectedItemId() == 0) {
            spnr_mhr_personal_dob_doc.requestFocus();
            return "Please select Documents to verify details (Proof)";
        }

        if (!cb_mhr_over_aged_y.isChecked() && !cb_mhr_over_aged_n.isChecked()) {
            cb_mhr_over_aged_y.requestFocus();
            return "Please select over aged option.";
        }

        if (spnr_mhr_personal_Identity_doc.getSelectedItemId() == 0) {
            spnr_mhr_personal_Identity_doc.requestFocus();
            return "Please select Documents to verify details (Proof)";
        }

        String strPerAddressDetails = edt_mhr_personal_address_details.getText().toString();
        strPerAddressDetails = strPerAddressDetails == null ? "" : strPerAddressDetails;

        if (strPerAddressDetails.equals("")) {
            edt_mhr_personal_address_details.requestFocus();
            return "Mention Full Address";
        }

        if (spnr_mhr_personal_address_doc.getSelectedItemId() == 0) {
            spnr_mhr_personal_address_doc.requestFocus();
            return "Please select Documents to verify details (Proof)";
        }

        if (spnr_mhr_occupation_param.getSelectedItemId() == 0) {
            spnr_mhr_occupation_param.requestFocus();
            return "Please select role as occupation";
        }

        if (spnr_mhr_occupation_details.getSelectedItemId() == 0) {
            spnr_mhr_occupation_details.requestFocus();
            return "Please select occupation";
        }

        String strProfQualify = edt_mhr_professional_qualification.getText().toString();
        strProfQualify = strProfQualify == null ? "" : strProfQualify;

        if (strProfQualify.equals("")) {
            edt_mhr_professional_qualification.requestFocus();
            return "Please enter Professional Qualification.";
        }

        if (!cb_mhr_proposer_pep_cat_y.isChecked() && !cb_mhr_proposer_pep_cat_n.isChecked()) {
            cb_mhr_proposer_pep_cat_y.requestFocus();
            return "Please select PEP category";
        }

        String strCatPep = edt_mhr_proposer_pep_details.getText().toString();
        strCatPep = strCatPep == null ? "" : strCatPep;
        if (cb_mhr_proposer_pep_cat_y.isChecked() && strCatPep.equals("")) {
            edt_mhr_proposer_pep_details.requestFocus();
            return "Please provide details of PEP category";
        }

        String strIncomeTotal = edt_mhr_income_total.getText().toString();
        strIncomeTotal = (strIncomeTotal == null || strIncomeTotal.equals("")) ? "0" : strIncomeTotal;
        if (strIncomeTotal.equals("0") || strIncomeTotal.equals("")) {
            edt_mhr_income_total.requestFocus();
            return "Please provide any one Income Details";
        }

        String strNatureBusiness = edt_mhr_nature_duration_buss.getText().toString();
        strNatureBusiness = strNatureBusiness == null ? "" : strNatureBusiness;
        if (strNatureBusiness.equals("")) {
            edt_mhr_nature_duration_buss.requestFocus();
            return "Please enter nature and duration of business/Occupation/Profession";
        }

        String strNameAddEmp = edt_mhr_name_add_emp.getText().toString();
        strNameAddEmp = strNameAddEmp == null ? "" : strNameAddEmp;
        if (strNameAddEmp.equals("")) {
            edt_mhr_name_add_emp.requestFocus();
            return "Please enter name and address of employee";
        }

        if (spnr_mhr_occupation_details.getSelectedItemId() == 3
                || spnr_mhr_occupation_details.getSelectedItemId() == 4) {
            if (!validateInstaTurnoverProfit()) {
                edt_mhr_turnover_year1.requestFocus();
                return "Please enter valid Investment/Turnover/Profit details";
            }
        }

        //Agriculturist
        if (spnr_mhr_occupation_details.getSelectedItemId() == 5) {
            String strLandHold = edt_mhr_agri_occu_landholding.getText().toString();
            strLandHold = (strLandHold == null || strLandHold.equals("")) ? "0" : strLandHold;
            if (strLandHold.equals("0") || strLandHold.equals("")) {
                edt_mhr_agri_occu_landholding.requestFocus();
                return "Please enter land holding details.";
            }

            if (spnr_mhr_agri_occupation.getSelectedItemId() == 0) {
                spnr_mhr_agri_occupation.requestFocus();
                return "Please select ownership";
            }

            String strNoOwners = edt_mhr_agri_no_owners.getText().toString();
            strNoOwners = (strNoOwners == null || strNoOwners.equals("")) ? "0" : strNoOwners;
            if (strNoOwners.equals("0") || strNoOwners.equals("")) {
                edt_mhr_agri_no_owners.requestFocus();
                return "Please enter no. of owners.";
            }

            String strShareLand = edt_mhr_agri_share_in_land.getText().toString();
            strShareLand = (strShareLand == null || strShareLand.equals("")) ? "0" : strShareLand;
            if (strShareLand.equals("0") || strShareLand.equals("")) {
                edt_mhr_agri_share_in_land.requestFocus();
                return "Please enter share in land.";
            }

            String strCorpPattern = edt_mhr_agri_corp_pattern.getText().toString();
            strCorpPattern = (strCorpPattern == null || strCorpPattern.equals("")) ? "0" : strCorpPattern;
            if (strCorpPattern.equals("0") || strCorpPattern.equals("")) {
                edt_mhr_agri_corp_pattern.requestFocus();
                return "Please enter corp pattern.";
            }

            String str3YrIncome = edt_mhr_agri_income_3yr.getText().toString();
            str3YrIncome = (str3YrIncome == null || str3YrIncome.equals("")) ? "0" : str3YrIncome;
            if (str3YrIncome.equals("0") || str3YrIncome.equals("")) {
                edt_mhr_agri_income_3yr.requestFocus();
                return "Please provide last 3 years income.";
            }

            String strAgriDocVerify = edt_mhr_agri_doc_verify.getText().toString();
            strAgriDocVerify = strAgriDocVerify == null ? "" : strAgriDocVerify;
            if (strAgriDocVerify.equals("")) {
                edt_mhr_agri_doc_verify.requestFocus();
                return "Please enter doc verify.";
            }
        }

        if (!cb_mhr_medical_status_yes.isChecked() && !cb_mhr_medical_status_no.isChecked()) {
            cb_mhr_medical_status_yes.requestFocus();
            return "Please select Health and Medical status.";
        }

        if (cb_mhr_medical_status_yes.isChecked()) {
            String strMedicalStatus = rb_mhr_medical_status_details.getText().toString();
            strMedicalStatus = strMedicalStatus == null ? "" : strMedicalStatus;
            if (strMedicalStatus.equals("")) {
                rb_mhr_medical_status_details.requestFocus();
                return "Please enter Health and Medical status details.";
            }
        }

        if (!cb_mhr_is_legal_risk_y.isChecked() && !cb_mhr_is_legal_risk_n.isChecked()) {
            cb_mhr_is_legal_risk_y.requestFocus();
            return "Please select legal risk status";
        }

        if (cb_mhr_is_legal_risk_y.isChecked()) {
            String strLegalRisk = edt_mhr_is_legal_risk_details.getText().toString();
            strLegalRisk = strLegalRisk == null ? "" : strLegalRisk;
            if (strLegalRisk.equals("")) {
                edt_mhr_is_legal_risk_details.requestFocus();
                return "Please enter legal risk details.";
            }
        }

        String strOponionRecommend = edt_mhr_oponion_recommend.getText().toString();
        strOponionRecommend = strOponionRecommend == null ? "" : strOponionRecommend;
        if (strOponionRecommend.equals("")) {
            edt_mhr_oponion_recommend.requestFocus();
            return "Please enter Recommendation.";
        }

        String strPlace = edt_mhr_place_last.getText().toString();
        strPlace = strPlace == null ? "" : strPlace;
        if (strPlace.equals("")) {
            edt_mhr_place_last.requestFocus();
            return "Please enter Place.";
        }

        /*if (!isVideoTaken){
            txtMHRUploadVideo.requestFocus();
            return "Please take video";
        }*/

        String strNameDesign = edtMHRNameDesign.getText().toString();
        strNameDesign = strNameDesign == null ? "" : strNameDesign;
        if (strNameDesign.equals("")) {
            edtMHRNameDesign.requestFocus();
            return "Please enter Name and Designation.";
        }

        return "";
    }

    private boolean validateInstaTurnoverProfit() {

        String strInvestment1 = edt_mhr_investment_y1.getText().toString();
        strInvestment1 = (strInvestment1 == null || strInvestment1.equals("")) ? "0" : strInvestment1;

        String strTurnover1 = edt_mhr_turnovr_y1.getText().toString();
        strTurnover1 = (strTurnover1 == null || strTurnover1.equals("")) ? "0" : strTurnover1;

        String strProfit1 = edt_mhr_profit_y1.getText().toString();
        strProfit1 = (strProfit1 == null || strProfit1.equals("")) ? "0" : strProfit1;

        String strInvestment2 = edt_mhr_investment_y2.getText().toString();
        strInvestment2 = (strInvestment2 == null || strInvestment2.equals("")) ? "0" : strInvestment2;

        String strTurnover2 = edt_mhr_turnovr_y2.getText().toString();
        strTurnover2 = (strTurnover2 == null || strTurnover2.equals("")) ? "0" : strTurnover2;

        String strProfit2 = edt_mhr_profit_y2.getText().toString();
        strProfit2 = (strProfit2 == null || strProfit2.equals("")) ? "0" : strProfit2;

        String strInvestment3 = edt_mhr_investment_y3.getText().toString();
        strInvestment3 = (strInvestment3 == null || strInvestment3.equals("")) ? "0" : strInvestment3;

        String strTurnover3 = edt_mhr_turnovr_y3.getText().toString();
        strTurnover3 = (strTurnover3 == null || strTurnover3.equals("")) ? "0" : strTurnover3;

        String strProfit3 = edt_mhr_profit_y3.getText().toString();
        strProfit3 = (strProfit3 == null || strProfit3.equals("")) ? "0" : strProfit3;

        String strError1 = "", strError2 = "", strError3 = "";

        if (strInvestment1.equals("0") && strTurnover1.equals("0") && strProfit1.equals("0")) {
            strError1 = "1";
        } else {
            strError1 = "0";
        }

        if (strInvestment2.equals("0") && strTurnover2.equals("0") && strProfit2.equals("0")) {
            strError2 = "1";
        } else {
            strError2 = "0";
        }

        if (strInvestment3.equals("0") && strTurnover3.equals("0") && strProfit3.equals("0")) {
            strError3 = "1";
        } else {
            strError3 = "0";
        }

        if (strError1.equals("1") && strError2.equals("1") && strError3.equals("1")) {
            return false;
        } else {
            return true;
        }
    }

    private void validateProfitYear(final EditText edt) {
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String strTurnoverYear3 = edt_mhr_turnover_year3.getText().toString();
                strTurnoverYear3 = (strTurnoverYear3 == null || strTurnoverYear3.equals("")) ? "0" : strTurnoverYear3;

                if (strTurnoverYear3.equals("0")) {
                    edt_mhr_turnover_year3.setError("Please Enter Year of Profit");
                }

                if (edt_mhr_turnover_year3.length() != 4) {
                    edt_mhr_turnover_year3.setError("Please Enter valid Year of Profit");
                }
            }
        });
    }

    private void validateTurnoverYear(final EditText edt) {

        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String strTurnoverYear2 = edt_mhr_turnover_year2.getText().toString();
                strTurnoverYear2 = (strTurnoverYear2 == null || strTurnoverYear2.equals("")) ? "0" : strTurnoverYear2;

                if (strTurnoverYear2.equals("0")) {
                    edt_mhr_turnover_year2.setError("Please Enter Year of Turnover");
                }

                if (edt_mhr_turnover_year2.length() != 4) {
                    edt_mhr_turnover_year2.setError("Please Enter valid Year of Turnover");
                }
            }
        });
    }

    private void validateInvestmentYear(final EditText edt) {

        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String strTurnoverYear1 = edt_mhr_turnover_year1.getText().toString();
                strTurnoverYear1 = (strTurnoverYear1 == null || strTurnoverYear1.equals("")) ? "0" : strTurnoverYear1;

                if (strTurnoverYear1.equals("0")) {
                    edt_mhr_turnover_year1.setError("Please Enter Year of Investment");
                }

                if (edt_mhr_turnover_year1.length() != 4) {
                    edt_mhr_turnover_year1.setError("Please Enter valid Year of Investment");
                }
            }
        });
    }

    private void changeLiabilityTotal(final EditText edt) {
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


                try {

                    String strLiableLoan = edt_mhr_liability_loan.getText().toString();
                    strLiableLoan = (strLiableLoan == null || strLiableLoan.equals("")) ? "0" : strLiableLoan;

                    String strLiableMortagage = edt_mhr_liability_mortagage.getText().toString();
                    strLiableMortagage = (strLiableMortagage == null || strLiableMortagage.equals("")) ? "0" : strLiableMortagage;

                    Long total = Long.valueOf(strLiableLoan) + Long.valueOf(strLiableMortagage);

                    edt_mhr_liability_total.setText(total + "");

                } catch (Exception ex) {
                    mCommonMethods.showToast(mContext, "Please enter valid number.");
                }
            }
        });
    }

    private void changeAssetsTotal(final EditText edt) {
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                try {

                    String strAssetsFD = edt_mhr_assets_fd.getText().toString();
                    strAssetsFD = (strAssetsFD == null || strAssetsFD.equals("")) ? "0" : strAssetsFD;

                    String strAssetsAC = edt_mhr_assets_ac.getText().toString();
                    strAssetsAC = (strAssetsAC == null || strAssetsAC.equals("")) ? "0" : strAssetsAC;

                    String strAssetsEquity = edt_mhr_assets_equity.getText().toString();
                    strAssetsEquity = (strAssetsEquity == null || strAssetsEquity.equals("")) ? "0" : strAssetsEquity;

                    String strAssetsAgreeLand = edt_mhr_assets_agri_land.getText().toString();
                    strAssetsAgreeLand = (strAssetsAgreeLand == null || strAssetsAgreeLand.equals("")) ? "0" : strAssetsAgreeLand;

                    String strAssetsSelfOwned = edt_mhr_assets_self_own.getText().toString();
                    strAssetsSelfOwned = (strAssetsSelfOwned == null || strAssetsSelfOwned.equals("")) ? "0" : strAssetsSelfOwned;

                    Long total = Long.valueOf(strAssetsFD) + Long.valueOf(strAssetsAC) + Long.valueOf(strAssetsEquity)
                            + Long.valueOf(strAssetsAgreeLand) + Long.valueOf(strAssetsSelfOwned);

                    edt_mhr_assets_total.setText(total + "");

                } catch (Exception ex) {
                    mCommonMethods.showToast(mContext, "Please enter valid number.");
                }
            }
        });
    }

    private void changeIncomeTotal(final EditText edt) {
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                try {
                    String strIncomeSal = edt_mhr_income_salary.getText().toString();
                    strIncomeSal = (strIncomeSal == null || strIncomeSal.equals("")) ? "0" : strIncomeSal;

                    String strIncomeBusiness = edt_mhr_income_business.getText().toString();
                    strIncomeBusiness = (strIncomeBusiness == null || strIncomeBusiness.equals("")) ? "0" : strIncomeBusiness;

                    String strIncomeAgri = edt_mhr_income_agri.getText().toString();
                    strIncomeAgri = (strIncomeAgri == null || strIncomeAgri.equals("")) ? "0" : strIncomeAgri;

                    String strIncomeFamily = edt_mhr_income_family.getText().toString();
                    strIncomeFamily = (strIncomeFamily == null || strIncomeFamily.equals("")) ? "0" : strIncomeFamily;

                    String strIncomeOther = edt_mhr_income_other.getText().toString();
                    strIncomeOther = (strIncomeOther == null || strIncomeOther.equals("")) ? "0" : strIncomeOther;

                    Long total = Long.valueOf(strIncomeSal) + Long.valueOf(strIncomeBusiness) + Long.valueOf(strIncomeAgri)
                            + Long.valueOf(strIncomeFamily) + Long.valueOf(strIncomeOther);

                    edt_mhr_income_total.setText(total + "");
                } catch (Exception ex) {
                    mCommonMethods.showToast(mContext, "Please enter valid number.");
                }
            }
        });
    }

    @SuppressWarnings("NewApi")
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnSubmitProposal:

                strProposerNumber = edt_mhr_proposal_no.getText().toString();
                strProposerNumber = edt_mhr_proposal_no.getText().toString() == null ? "" : strProposerNumber;

                if (!strProposerNumber.equals("")) {
                    mGetMHRDetails = new GetMHRDetails();
                    mGetMHRDetails.execute();
                } else {
                    mCommonMethods.showMessageDialog(mContext, "Please enter proper proposer number.");
                }

                break;

            case R.id.txt_mhr_personal_dob_details:

                DATE_DIALOG_ID = R.id.txt_mhr_personal_dob_details;

                DatePickerDialog dialog = new DatePickerDialog(
                        mContext,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        datePickListMeetDate1, mDOBYear, mDOBMonth, mDOBDay);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                break;

            case R.id.txt_mhr_proposer_meet_date1:

                DATE_DIALOG_ID = R.id.txt_mhr_proposer_meet_date1;

                DatePickerDialog dialog1 = new DatePickerDialog(
                        mContext,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        datePickListMeetDate1, mDOBYear, mDOBMonth, mDOBDay);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog1.show();

                break;

            case R.id.txtMHRUploadVideo:

                rl_mhr_upload_video.setVisibility(View.GONE);

                vv_mhr_upload_video.setVisibility(View.GONE);
                iv_mhr_upload_video_img.setVisibility(View.GONE);
                iv_mhr_upload_video_play.setVisibility(View.GONE);

                videoImgBitmap = null;

                //Run time permission
                if (mCommonMethods.isDeviceAboveM()) {

                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(arrPermissionRuntime, PERMS_RUNTIME_REQ_CODE);
                    } else {

                        try {
                            mLatLng = mCommonMethods.getCurrentLocation(mContext, mGPGpsTracker);

                            //if (mLatLng.latitude != 0.0 && mLatLng.longitude != 0.0){
                            //mMHRVideoFile = mStorageUtils.createFileToAppSpecificDir(mContext,strProposerNumber + "_MHR.mp4");
                            mMHRVideoFile = mStorageUtils.createFileToAppSpecificDir(mContext,
                                    strProposerNumber + "_MHR.mp4");

                            //record video
                            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);

                            // Continue only if the File was successfully created
                            if (mMHRVideoFile != null) {
                                Uri photoURI = FileProvider.getUriForFile(this,
                                        "sbilife.com.pointofsale_bancaagency",
                                        mMHRVideoFile);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                mMHRVideoLauncher.launch(intent);
                                //startActivityForResult(intent, VIDEO_CAPTURE_CODE);
                            }
                                /*}else {
                                    mCommonMethods.showToast(mContext, "Please check your gps connection and try again");
                                    mLatLng = mCommonMethods.getCurrentLocation(mContext, mGPGpsTracker);
                                }*/

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            mLatLng = mCommonMethods.getCurrentLocation(mContext, mGPGpsTracker);
                        }
                    }
                } else {

                    try {
                        mLatLng = mCommonMethods.getCurrentLocation(mContext, mGPGpsTracker);

                        //if (mLatLng.latitude != 0.0 && mLatLng.longitude != 0.0){

                        mMHRVideoFile = mStorageUtils.createFileToAppSpecificDir(mContext,
                                strProposerNumber + "_MHR.mp4");

                        //record video
                        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);

                        // Continue only if the File was successfully created
                        if (mMHRVideoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(this,
                                    "sbilife.com.pointofsale_bancaagency",
                                    mMHRVideoFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            mMHRVideoLauncher.launch(intent);
                            //startActivityForResult(intent, VIDEO_CAPTURE_CODE);
                        }
                            /*} else {
                                mCommonMethods.showToast(mContext, "Please check your gps connection and try again");
                                mLatLng = mCommonMethods.getCurrentLocation(mContext, mGPGpsTracker);
                            }*/

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        mLatLng = mCommonMethods.getCurrentLocation(mContext, mGPGpsTracker);
                    }
                }

                break;

            case R.id.btnSubmitMHR:

                //Run time permission
                if (mCommonMethods.isDeviceAboveM()) {

                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(arrPermissionRuntime, PERMS_RUNTIME_REQ_CODE);
                    } else {

                        try {
                            mLatLng = mCommonMethods.getCurrentLocation(mContext, mGPGpsTracker);

                            //if (mLatLng.latitude != 0.0 && mLatLng.longitude != 0.0){
                            submitMHR();
                                /*}else {
                                    mCommonMethods.showToast(mContext, "Please check your gps connection and try again");
                                    mLatLng = mCommonMethods.getCurrentLocation(mContext, mGPGpsTracker);
                                }*/

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            mLatLng = mCommonMethods.getCurrentLocation(mContext, mGPGpsTracker);
                        }
                    }
                } else {

                    try {
                        mLatLng = mCommonMethods.getCurrentLocation(mContext, mGPGpsTracker);

                        //if (mLatLng.latitude != 0.0 && mLatLng.longitude != 0.0){
                        submitMHR();
                            /*} else {
                                mCommonMethods.showToast(mContext, "Please check your gps connection and try again");
                                mLatLng = mCommonMethods.getCurrentLocation(mContext, mGPGpsTracker);
                            }*/

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        mLatLng = mCommonMethods.getCurrentLocation(mContext, mGPGpsTracker);
                    }
                }

                break;

            case R.id.iv_mhr_upload_video_play:

                vv_mhr_upload_video.setVisibility(View.VISIBLE);
                rl_mhr_upload_video.setVisibility(View.VISIBLE);
                iv_mhr_upload_video_img.setVisibility(View.GONE);
                iv_mhr_upload_video_play.setVisibility(View.GONE);

                mediaController.setAnchorView(vv_mhr_upload_video);
                vv_mhr_upload_video.setMediaController(mediaController);

                vv_mhr_upload_video.start();

                break;

            case R.id.txt_mhr_report_pic:

                iv_mhr_report_pic.setVisibility(View.GONE);
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mMHRReportPic = null;

                String imageFilePicName = strProposerNumber + "_MHR_Pic" + ".jpg";

                try {
                    //mMHRReportPic = mCommonMethods.createCaptureImg(imageFilePicName);
                    mMHRReportPic = mStorageUtils.createFileToAppSpecificDir(mContext, imageFilePicName);

                    // Continue only if the File was successfully created
                    if (mMHRReportPic != null) {
                        Uri photoURI = FileProvider.getUriForFile(this,
                                "sbilife.com.pointofsale_bancaagency",
                                mMHRReportPic);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        mMHRImageLauncher.launch(takePictureIntent);
                        //startActivityForResult(takePictureIntent, REQUEST_CODE_CAPTURE_PIC_FILE);
                    }
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
                break;
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

                    if (grantResults[1] != PackageManager.PERMISSION_GRANTED || grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                        str_grant_msg += "App requires Location Access Permission\n";
                    }

                    if (!str_grant_msg.equals("")) {
                        mCommonMethods.showMessageDialog(mContext, str_grant_msg);
                    }
                }

                break;

        }
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        mCommonMethods.showToast(mContext, msg);

        strCompleteAddress = getCompleteAddressString(location.getLatitude(), location.getLongitude());
        // You can now create a LatLng Object for use with maps
        //mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction", strReturnedAddress.toString());
            } else {
                Log.w("My Current loction", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction", "Canont get Address!");
        }
        return strAdd;
    }

    private Bitmap createVideoThumbNail(String strPath) throws IOException {
        // Create an image file name
        return ThumbnailUtils.createVideoThumbnail(strPath, MediaStore.Video.Thumbnails.MICRO_KIND);
    }

    /*public void updateDisplay(int id){
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        switch (id){
            case R.id.txt_mhr_proposer_meet_date1:
                txt_mhr_proposer_meet_date1.setText(sdf.format(myCalendar.getTime()));
                break;

            case R.id.txt_mhr_personal_dob_details:
                txt_mhr_personal_dob_details.setText(sdf.format(myCalendar.getTime()));
                break;
        }
    }*/

    private void createMHRPdf() {

        try {

            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD);
            Font small_bold1 = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
            Font small_bold2 = new Font(Font.FontFamily.TIMES_ROMAN, 4, Font.BOLD);
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);

            mMHRPdf = mStorageUtils.createFileToAppSpecificDir(mContext,
                    strProposerNumber + "_R1_MHR.pdf");

            Rectangle rect = new Rectangle(594f, 792f);

            //Document document = new Document(rect, 50, 50, 50, 50);
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(mMHRPdf.getAbsolutePath()));

            document.open();

            // For SBI- Life Logo starts
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.sbi_life_logo);
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
            Paragraph Para_Header = new Paragraph("Special Mortal Hazard And KYC Verification Report(UWN024)", headerBold);
            Para_Header.setAlignment(Element.ALIGN_CENTER);
            document.add(Para_Header);

            document.add(para_img_logo_after_space_1);

            PdfPTable table_proposer_no = new PdfPTable(4);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_proposer_no.setWidthPercentage(100);

            PdfPCell ProposalNumber_cell_1 = new PdfPCell(new Paragraph(
                    "Proposal Number", small_normal));
            PdfPCell ProposalNumber_cell_1_val = new PdfPCell(new Paragraph(
                    edt_mhr_proposal_no.getText().toString(), small_bold1));
            ProposalNumber_cell_1_val.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell sum_asssured_cell_2 = new PdfPCell(new Paragraph(
                    "Sum Asssured", small_normal));
            PdfPCell sum_asssured_cell_2_val = new PdfPCell(new Paragraph(
                    edt_mhr_sum_assured.getText().toString(), small_bold1));
            sum_asssured_cell_2_val.setHorizontalAlignment(Element.ALIGN_CENTER);

            sum_asssured_cell_2_val.setVerticalAlignment(Element.ALIGN_CENTER);

            ProposalNumber_cell_1.setPadding(5);
            ProposalNumber_cell_1_val.setPadding(5);
            sum_asssured_cell_2.setPadding(5);
            sum_asssured_cell_2_val.setPadding(5);

            PdfPCell ProposerName_cell_3 = new PdfPCell(new Paragraph(
                    "Name of the proposer", small_normal));
            PdfPCell ProposerName_cell_3_val = new PdfPCell(new Paragraph(
                    txt_mhr_proposer_name.getText().toString(), small_bold1));
            ProposerName_cell_3_val.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell suc_cell_4 = new PdfPCell(new Paragraph(
                    "SUC", small_normal));
            PdfPCell suc_cell_4_val = new PdfPCell(new Paragraph(
                    edt_mhr_sum_assured.getText().toString(), small_bold1));
            suc_cell_4_val.setHorizontalAlignment(Element.ALIGN_CENTER);

            suc_cell_4_val.setVerticalAlignment(Element.ALIGN_CENTER);

            ProposerName_cell_3.setPadding(5);
            ProposerName_cell_3_val.setPadding(5);
            suc_cell_4.setPadding(5);
            suc_cell_4_val.setPadding(5);

            table_proposer_no.addCell(ProposalNumber_cell_1);
            table_proposer_no.addCell(ProposalNumber_cell_1_val);
            table_proposer_no.addCell(sum_asssured_cell_2);
            table_proposer_no.addCell(sum_asssured_cell_2_val);

            table_proposer_no.addCell(ProposerName_cell_3);
            table_proposer_no.addCell(ProposerName_cell_3_val);
            table_proposer_no.addCell(suc_cell_4);
            table_proposer_no.addCell(suc_cell_4_val);

            document.add(table_proposer_no);

            PdfPTable table_proposer_reg = new PdfPTable(6);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_proposer_reg.setWidthPercentage(100);

            PdfPCell region_cell_1 = new PdfPCell(new Paragraph(
                    "Region", small_normal));
            PdfPCell region_cell_1_val = new PdfPCell(new Paragraph(
                    txt_mhr_region.getText().toString(), small_bold1));
            region_cell_1_val.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell channel_cell_2 = new PdfPCell(new Paragraph(
                    "Channel", small_normal));
            PdfPCell channel_cell_2_val = new PdfPCell(new Paragraph(
                    txt_mhr_channel.getText().toString(), small_bold1));
            channel_cell_2_val.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell pcName_cell3 = new PdfPCell(new Paragraph(
                    "PC Name", small_normal));
            PdfPCell pcName_cell3_val = new PdfPCell(new Paragraph(
                    txt_mhr_pc_name.getText().toString(), small_bold1));
            pcName_cell3_val.setHorizontalAlignment(Element.ALIGN_CENTER);

            region_cell_1.setPadding(5);
            region_cell_1_val.setPadding(5);
            channel_cell_2.setPadding(5);
            channel_cell_2_val.setPadding(5);
            pcName_cell3.setPadding(5);
            pcName_cell3_val.setPadding(5);

            table_proposer_reg.addCell(region_cell_1);
            table_proposer_reg.addCell(region_cell_1_val);
            table_proposer_reg.addCell(channel_cell_2);
            table_proposer_reg.addCell(channel_cell_2_val);
            table_proposer_reg.addCell(pcName_cell3);
            table_proposer_reg.addCell(pcName_cell3_val);

            document.add(table_proposer_reg);

            document.add(para_img_logo_after_space_1);

            PdfPTable table_mhr_intro = new PdfPTable(1);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_mhr_intro.setWidthPercentage(100);
            PdfPCell table_mhr_intro_cell = new PdfPCell(new Paragraph(
                    getResources().getString(R.string.str_mhr_introduction), small_normal));
            table_mhr_intro_cell.setPadding(5);
            table_mhr_intro.addCell(table_mhr_intro_cell);
            document.add(table_mhr_intro);

            document.add(para_img_logo_after_space_1);

            //part A starts
            PdfPTable table_mhr_details_of = new PdfPTable(1);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_mhr_details_of.setWidthPercentage(100);
            PdfPCell table_mhr_details_of_cell1 = new PdfPCell();
            if (rb_mhr_proposer_checked.isChecked())
                table_mhr_details_of_cell1 = new PdfPCell(new Paragraph("A. Please mention whether this contains details of : Proposer", small_bold));
            else if (rb_mhr_life_assured_checked.isChecked())
                table_mhr_details_of_cell1 = new PdfPCell(new Paragraph("A. Please mention whether this contains details of : Life to be Assured", small_bold));
            else if (rb_mhr_premium_payer_checked.isChecked())
                table_mhr_details_of_cell1 = new PdfPCell(new Paragraph("A. Please mention whether this contains details of : Premium Payer", small_bold));

            table_mhr_details_of.addCell(table_mhr_details_of_cell1);

            PdfPCell table_mhr_details_of_cell2 = new PdfPCell(new Paragraph("Please give details of your meeting.(please mention names of the person in case report is about multiple persons)", small_normal));
            table_mhr_details_of_cell2.setPadding(5);
            table_mhr_details_of.addCell(table_mhr_details_of_cell2);

            document.add(table_mhr_details_of);

            PdfPTable table_mhr_details_meeting = new PdfPTable(4);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_mhr_details_meeting.setWidthPercentage(100);

            PdfPCell table_mhr_details_meeting_cell1 = new PdfPCell(new Paragraph("Name of Person : " + edt_mhr_proposer_name_details.getText().toString(), small_normal));
            table_mhr_details_meeting_cell1.setPadding(5);
            table_mhr_details_meeting.addCell(table_mhr_details_meeting_cell1);

            PdfPCell table_mhr_details_meeting_cell2;
            if (rb_mhr_personal_meet_yes1.isChecked())
                table_mhr_details_meeting_cell2 = new PdfPCell(new Paragraph("Did you personally meet him/her : Yes", small_normal));
            else
                table_mhr_details_meeting_cell2 = new PdfPCell(new Paragraph("Did you personally meet him/her : No", small_normal));
            table_mhr_details_meeting_cell2.setPadding(5);
            table_mhr_details_meeting.addCell(table_mhr_details_meeting_cell2);

            PdfPCell table_mhr_details_meeting_cell3 = new PdfPCell(new Paragraph("Date of meeting : " + txt_mhr_proposer_meet_date1.getText().toString(), small_normal));
            table_mhr_details_meeting_cell3.setPadding(5);
            table_mhr_details_meeting.addCell(table_mhr_details_meeting_cell3);

            PdfPCell table_mhr_details_meeting_cell4 = new PdfPCell(new Paragraph("Place of meeting : " + edt_mhr_proposer_meet_place1.getText().toString(), small_normal));
            table_mhr_details_meeting_cell4.setPadding(5);
            table_mhr_details_meeting.addCell(table_mhr_details_meeting_cell4);

            document.add(table_mhr_details_meeting);

            PdfPTable table_mhr_details_meeting_no = new PdfPTable(1);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_mhr_details_meeting_no.setWidthPercentage(100);

            PdfPCell table_mhr_details_meeting_no_cel = new PdfPCell(new Paragraph("If any of the above answer is 'NO', then please mention as to how, you have gathered the information about him/her.", small_normal));
            table_mhr_details_meeting_no_cel.setPadding(5);
            table_mhr_details_meeting_no.addCell(table_mhr_details_meeting_no_cel);

            table_mhr_details_meeting_no_cel = new PdfPCell(new Paragraph(edt_mhr_details_if_no1.getText().toString(), small_normal));
            table_mhr_details_meeting_no_cel.setPadding(5);
            table_mhr_details_meeting_no.addCell(table_mhr_details_meeting_no_cel);

            document.add(table_mhr_details_meeting_no);

            document.add(para_img_logo_after_space_1);
            //part A ends

            //Part B starts
            PdfPTable table_mhr_part_b1 = new PdfPTable(1);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_mhr_part_b1.setWidthPercentage(100);

            PdfPCell table_mhr_part_b1_cell1 = new PdfPCell(new Paragraph("B.Verification of Personal and Financial Details", small_bold));
            table_mhr_part_b1_cell1.setPadding(5);
            table_mhr_part_b1.addCell(table_mhr_part_b1_cell1);

            table_mhr_part_b1_cell1 = new PdfPCell(new Paragraph("Please ensure that all the facts are verified on the basis of origial documents", small_normal));
            table_mhr_part_b1_cell1.setPadding(5);
            table_mhr_part_b1.addCell(table_mhr_part_b1_cell1);

            document.add(table_mhr_part_b1);

            PdfPTable table_mhr_part_b2 = new PdfPTable(3);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_mhr_part_b2.setWidthPercentage(100);

            //row 1
            PdfPCell table_mhr_part_b2_c1 = new PdfPCell(new Paragraph("Verification Parameters", small_bold));
            table_mhr_part_b2_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b2_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b2_c1.setPadding(5);
            table_mhr_part_b2.addCell(table_mhr_part_b2_c1);

            table_mhr_part_b2_c1 = new PdfPCell(new Paragraph("Mention Details", small_bold));
            table_mhr_part_b2_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b2_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b2_c1.setPadding(5);
            table_mhr_part_b2.addCell(table_mhr_part_b2_c1);

            table_mhr_part_b2_c1 = new PdfPCell(new Paragraph("Mention Documents checked to verify details (Proof)", small_bold));
            table_mhr_part_b2_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b2_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b2_c1.setPadding(5);
            table_mhr_part_b2.addCell(table_mhr_part_b2_c1);

            //row2
            table_mhr_part_b2_c1 = new PdfPCell(new Paragraph("DOB and Age of life Assured", small_bold));
            table_mhr_part_b2_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b2_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b2_c1.setPadding(5);
            table_mhr_part_b2.addCell(table_mhr_part_b2_c1);

            table_mhr_part_b2_c1 = new PdfPCell(new Paragraph(txt_mhr_personal_dob_details.getText().toString(), small_normal));
            table_mhr_part_b2_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b2_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b2_c1.setPadding(5);
            table_mhr_part_b2.addCell(table_mhr_part_b2_c1);

            table_mhr_part_b2_c1 = new PdfPCell(new Paragraph(spnr_mhr_personal_dob_doc.getSelectedItem().toString(), small_normal));
            table_mhr_part_b2_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b2_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b2_c1.setPadding(5);
            table_mhr_part_b2.addCell(table_mhr_part_b2_c1);

            //row3
            table_mhr_part_b2_c1 = new PdfPCell(new Paragraph("Photo Identification", small_bold));
            table_mhr_part_b2_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b2_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b2_c1.setPadding(5);
            table_mhr_part_b2.addCell(table_mhr_part_b2_c1);

            table_mhr_part_b2_c1 = new PdfPCell(new Paragraph("", small_normal));
            table_mhr_part_b2_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b2_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b2_c1.setPadding(5);
            table_mhr_part_b2.addCell(table_mhr_part_b2_c1);

            table_mhr_part_b2_c1 = new PdfPCell(new Paragraph(spnr_mhr_personal_Identity_doc.getSelectedItem().toString(), small_normal));
            table_mhr_part_b2_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b2_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b2_c1.setPadding(5);
            table_mhr_part_b2.addCell(table_mhr_part_b2_c1);

            //row 4
            table_mhr_part_b2_c1 = new PdfPCell(new Paragraph("Mention Full Address as Verified", small_bold));
            table_mhr_part_b2_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b2_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b2_c1.setPadding(5);
            table_mhr_part_b2.addCell(table_mhr_part_b2_c1);

            table_mhr_part_b2_c1 = new PdfPCell(new Paragraph(edt_mhr_personal_address_details.getText().toString(), small_normal));
            table_mhr_part_b2_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b2_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b2_c1.setPadding(5);
            table_mhr_part_b2.addCell(table_mhr_part_b2_c1);

            table_mhr_part_b2_c1 = new PdfPCell(new Paragraph(spnr_mhr_personal_address_doc.getSelectedItem().toString(), small_normal));
            table_mhr_part_b2_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b2_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b2_c1.setPadding(5);
            table_mhr_part_b2.addCell(table_mhr_part_b2_c1);

            document.add(table_mhr_part_b2);

            PdfPTable table_mhr_part_b3 = new PdfPTable(1);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_mhr_part_b3.setWidthPercentage(100);

            //row 1
            PdfPCell table_mhr_part_b3_c1;
            if (cb_mhr_over_aged_y.isChecked()) {
                table_mhr_part_b3_c1 = new PdfPCell(new Paragraph("Did he/she appear over aged : Yes", small_normal));
            } else {
                table_mhr_part_b3_c1 = new PdfPCell(new Paragraph("Did he/she appear over aged : No", small_normal));
            }
            table_mhr_part_b3_c1.setPadding(5);
            table_mhr_part_b3.addCell(table_mhr_part_b3_c1);

            document.add(table_mhr_part_b3);

            PdfPTable table_mhr_part_b4 = new PdfPTable(3);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_mhr_part_b4.setWidthPercentage(100);

            //row 1
            PdfPCell table_mhr_part_b4_c1 = new PdfPCell(new Paragraph("Occupation : "
                    + spnr_mhr_occupation_param.getSelectedItem().toString(), small_normal));
            table_mhr_part_b4_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b4_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b4_c1.setPadding(5);
            table_mhr_part_b4.addCell(table_mhr_part_b4_c1);

            table_mhr_part_b4_c1 = new PdfPCell(new Paragraph(spnr_mhr_occupation_details.getSelectedItem().toString()
                    , small_normal));
            table_mhr_part_b4_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b4_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b4_c1.setPadding(5);
            table_mhr_part_b4.addCell(table_mhr_part_b4_c1);

            table_mhr_part_b4_c1 = new PdfPCell(new Paragraph("Professional Qualification : "
                    + edt_mhr_professional_qualification.getText().toString(), small_normal));
            table_mhr_part_b4_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b4_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b4_c1.setPadding(5);
            table_mhr_part_b4.addCell(table_mhr_part_b4_c1);

            document.add(table_mhr_part_b4);

            PdfPTable table_mhr_part_b5 = new PdfPTable(1);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_mhr_part_b5.setWidthPercentage(100);

            //row 1
            PdfPCell table_mhr_part_b5_c1;
            if (cb_mhr_proposer_pep_cat_y.isChecked()) {
                table_mhr_part_b5_c1 = new PdfPCell(new Paragraph(
                        "Does the Proposer/Life assured/Premium payer belong to PEP Category : Yes", small_normal));
            } else {
                table_mhr_part_b5_c1 = new PdfPCell(new Paragraph(
                        "Does the Proposer/Life assured/Premium payer belong to PEP Category : No", small_normal));
            }
            table_mhr_part_b5_c1.setPadding(5);
            table_mhr_part_b5.addCell(table_mhr_part_b5_c1);

            table_mhr_part_b5_c1 = new PdfPCell(new Paragraph("If yes, please provide the details....", small_normal));
            table_mhr_part_b5_c1.setPadding(5);
            table_mhr_part_b5.addCell(table_mhr_part_b5_c1);

            table_mhr_part_b5_c1 = new PdfPCell(new Paragraph(edt_mhr_proposer_pep_details.getText().toString(), small_normal));
            table_mhr_part_b5_c1.setPadding(5);
            table_mhr_part_b5.addCell(table_mhr_part_b5_c1);

            table_mhr_part_b5_c1 = new PdfPCell(new Paragraph("Details of Income and Assets and Liabilities on the basis of your verification and assessment", small_bold));
            table_mhr_part_b5_c1.setPadding(5);
            table_mhr_part_b5.addCell(table_mhr_part_b5_c1);

            if (rb_mhr_is_proposer_income_y.isChecked())
                table_mhr_part_b5_c1 = new PdfPCell(new Paragraph("Please mention whether following details pertain to : Proposer", small_normal));
            else if (rb_mhr_is_proposer_income_n.isChecked())
                table_mhr_part_b5_c1 = new PdfPCell(new Paragraph("Please mention whether following details pertain to : Premium Payer", small_normal));

            table_mhr_part_b5_c1.setPadding(5);
            table_mhr_part_b5.addCell(table_mhr_part_b5_c1);

            document.add(table_mhr_part_b5);

            PdfPTable table_mhr_part_b6 = new PdfPTable(6);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_mhr_part_b6.setWidthPercentage(100);

            //row 1
            PdfPCell table_mhr_part_b6_c1 = new PdfPCell(new Paragraph("Income (last FY)", small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph("Amount (Rs.)"
                    , small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph("Assets", small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph("Value (Rs.)", small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph("Liabilities", small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph("Value (Rs.)", small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            //row 2
            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph("Salary", small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph(edt_mhr_income_salary.getText().toString(), small_normal));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph("Fixed Deposits/Bonds", small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph(edt_mhr_assets_fd.getText().toString(), small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph("Loan", small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph(edt_mhr_liability_loan.getText().toString(), small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            //row 3
            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph("Business & Profession", small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph(edt_mhr_income_business.getText().toString(), small_normal));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph("Bank A/c Balances", small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph(edt_mhr_assets_ac.getText().toString(), small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph("Mortgage", small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph(edt_mhr_liability_mortagage.getText().toString(), small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            //row 4
            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph("Agriculture", small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph(edt_mhr_income_agri.getText().toString(), small_normal));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph("Equity/Shares", small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph(edt_mhr_assets_equity.getText().toString(), small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph("", small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph("", small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            //row 5
            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph("Family Income", small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph(edt_mhr_income_family.getText().toString(), small_normal));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph("Agricultural Land", small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph(edt_mhr_assets_agri_land.getText().toString(), small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph("", small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph("", small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            //row 6
            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph("Other Income", small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph(edt_mhr_income_other.getText().toString(), small_normal));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph("Self owned Building/Flat", small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph(edt_mhr_assets_self_own.getText().toString(), small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph("", small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph("", small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            //row 7
            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph("Total Income", small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph(edt_mhr_income_total.getText().toString(), small_normal));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph("Total Assets", small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph(edt_mhr_assets_total.getText().toString(), small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph("Total Liability", small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            table_mhr_part_b6_c1 = new PdfPCell(new Paragraph(edt_mhr_liability_total.getText().toString(), small_bold));
            table_mhr_part_b6_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_b6_c1.setPadding(5);
            table_mhr_part_b6.addCell(table_mhr_part_b6_c1);

            document.add(table_mhr_part_b6);

            PdfPTable table_mhr_part_b7 = new PdfPTable(1);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_mhr_part_b7.setWidthPercentage(100);

            //row 1
            PdfPCell table_mhr_part_b7_c1 = new PdfPCell(new Paragraph(
                    "You may highlight any specific information about the income and occupation of the person and documents seen by you.", small_normal));
            table_mhr_part_b7_c1.setPadding(5);
            table_mhr_part_b7.addCell(table_mhr_part_b7_c1);

            //row 2
            table_mhr_part_b7_c1 = new PdfPCell(new Paragraph(
                    edt_mhr_info_income_person.getText().toString(), small_normal));
            table_mhr_part_b7_c1.setPadding(5);
            table_mhr_part_b7.addCell(table_mhr_part_b7_c1);

            document.add(table_mhr_part_b7);

            document.add(para_img_logo_after_space_1);

            PdfPTable table_mhr_part_c1 = new PdfPTable(1);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_mhr_part_c1.setWidthPercentage(100);

            //row 1
            PdfPCell table_mhr_part_c1_c1 = new PdfPCell(new Paragraph(
                    "C. Specific verification of income and occupation in following cases", small_bold));
            table_mhr_part_c1_c1.setPadding(5);
            table_mhr_part_c1.addCell(table_mhr_part_c1_c1);

            table_mhr_part_c1_c1 = new PdfPCell(new Paragraph(
                    "In case of occupation being business or profession or self employment :", small_bold));
            table_mhr_part_c1_c1.setPadding(5);
            table_mhr_part_c1.addCell(table_mhr_part_c1_c1);

            document.add(table_mhr_part_c1);

            PdfPTable table_mhr_part_c2 = new PdfPTable(2);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_mhr_part_c2.setWidthPercentage(100);

            //row 1
            PdfPCell table_mhr_part_c2_c1 = new PdfPCell(new Paragraph(
                    "Describe nature and duration of business/Occupation/Profession :", small_bold));
            table_mhr_part_c2_c1.setPadding(5);
            table_mhr_part_c2.addCell(table_mhr_part_c2_c1);

            table_mhr_part_c2_c1 = new PdfPCell(new Paragraph(edt_mhr_nature_duration_buss.getText().toString(), small_normal));
            table_mhr_part_c2_c1.setPadding(5);
            table_mhr_part_c2.addCell(table_mhr_part_c2_c1);

            //row 2
            PdfPCell table_mhr_part_r2_c1 = new PdfPCell(new Paragraph(
                    "Name and Address of Employer or Firm/Company (owned or associated with) :", small_bold));
            table_mhr_part_r2_c1.setPadding(5);
            table_mhr_part_c2.addCell(table_mhr_part_r2_c1);

            table_mhr_part_r2_c1 = new PdfPCell(new Paragraph(edt_mhr_name_add_emp.getText().toString(), small_normal));
            table_mhr_part_r2_c1.setPadding(5);
            table_mhr_part_c2.addCell(table_mhr_part_r2_c1);

            //document.add(table_mhr_part_c2);

            //row 3
            table_mhr_part_c2_c1 = new PdfPCell(new Paragraph(
                    "Invest, Turnover and profit from Business/Occupation/Profession in last three years :", small_bold));
            table_mhr_part_c2_c1.setPadding(5);
            table_mhr_part_c2_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2.addCell(table_mhr_part_c2_c1);

            PdfPTable table_mhr_part_c2_invest = new PdfPTable(4);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_mhr_part_c2_invest.setWidthPercentage(100);

            //row 1
            PdfPCell table_mhr_part_c2_invest_c1 = new PdfPCell(new Paragraph("", small_bold));
            table_mhr_part_c2_invest_c1.setPadding(5);
            table_mhr_part_c2_invest.addCell(table_mhr_part_c2_invest_c1);

            table_mhr_part_c2_invest_c1 = new PdfPCell(new Paragraph("Year - "
                    + edt_mhr_turnover_year1.getText().toString(), small_bold));
            table_mhr_part_c2_invest_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest_c1.setPadding(5);
            table_mhr_part_c2_invest.addCell(table_mhr_part_c2_invest_c1);

            table_mhr_part_c2_invest_c1 = new PdfPCell(new Paragraph("Year - "
                    + edt_mhr_turnover_year2.getText().toString(), small_bold));
            table_mhr_part_c2_invest_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest_c1.setPadding(5);
            table_mhr_part_c2_invest.addCell(table_mhr_part_c2_invest_c1);

            table_mhr_part_c2_invest_c1 = new PdfPCell(new Paragraph("Year - "
                    + edt_mhr_turnover_year3.getText().toString(), small_bold));
            table_mhr_part_c2_invest_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest_c1.setPadding(5);
            table_mhr_part_c2_invest.addCell(table_mhr_part_c2_invest_c1);

            //row 2
            table_mhr_part_c2_invest_c1 = new PdfPCell(new Paragraph("Investment", small_bold));
            table_mhr_part_c2_invest_c1.setPadding(5);
            table_mhr_part_c2_invest_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest.addCell(table_mhr_part_c2_invest_c1);

            table_mhr_part_c2_invest_c1 = new PdfPCell(new Paragraph(
                    edt_mhr_investment_y1.getText().toString(), small_bold));
            table_mhr_part_c2_invest_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest_c1.setPadding(5);
            table_mhr_part_c2_invest.addCell(table_mhr_part_c2_invest_c1);

            table_mhr_part_c2_invest_c1 = new PdfPCell(new Paragraph(edt_mhr_investment_y2.getText().toString(), small_bold));
            table_mhr_part_c2_invest_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest_c1.setPadding(5);
            table_mhr_part_c2_invest.addCell(table_mhr_part_c2_invest_c1);

            table_mhr_part_c2_invest_c1 = new PdfPCell(new Paragraph(edt_mhr_investment_y3.getText().toString(), small_bold));
            table_mhr_part_c2_invest_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest_c1.setPadding(5);
            table_mhr_part_c2_invest.addCell(table_mhr_part_c2_invest_c1);

            //row 3
            table_mhr_part_c2_invest_c1 = new PdfPCell(new Paragraph("Turnover", small_bold));
            table_mhr_part_c2_invest_c1.setPadding(5);
            table_mhr_part_c2_invest_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest.addCell(table_mhr_part_c2_invest_c1);

            table_mhr_part_c2_invest_c1 = new PdfPCell(new Paragraph(
                    edt_mhr_turnovr_y1.getText().toString(), small_bold));
            table_mhr_part_c2_invest_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest_c1.setPadding(5);
            table_mhr_part_c2_invest.addCell(table_mhr_part_c2_invest_c1);

            table_mhr_part_c2_invest_c1 = new PdfPCell(new Paragraph(edt_mhr_turnovr_y2.getText().toString(), small_bold));
            table_mhr_part_c2_invest_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest_c1.setPadding(5);
            table_mhr_part_c2_invest.addCell(table_mhr_part_c2_invest_c1);

            table_mhr_part_c2_invest_c1 = new PdfPCell(new Paragraph(edt_mhr_turnovr_y3.getText().toString(), small_bold));
            table_mhr_part_c2_invest_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest_c1.setPadding(5);
            table_mhr_part_c2_invest.addCell(table_mhr_part_c2_invest_c1);

            //row 4
            table_mhr_part_c2_invest_c1 = new PdfPCell(new Paragraph("Profit", small_bold));
            table_mhr_part_c2_invest_c1.setPadding(5);
            table_mhr_part_c2_invest_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest.addCell(table_mhr_part_c2_invest_c1);

            table_mhr_part_c2_invest_c1 = new PdfPCell(new Paragraph(
                    edt_mhr_profit_y1.getText().toString(), small_bold));
            table_mhr_part_c2_invest_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest_c1.setPadding(5);
            table_mhr_part_c2_invest.addCell(table_mhr_part_c2_invest_c1);

            table_mhr_part_c2_invest_c1 = new PdfPCell(new Paragraph(edt_mhr_profit_y2.getText().toString(), small_bold));
            table_mhr_part_c2_invest_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest_c1.setPadding(5);
            table_mhr_part_c2_invest.addCell(table_mhr_part_c2_invest_c1);

            table_mhr_part_c2_invest_c1 = new PdfPCell(new Paragraph(edt_mhr_profit_y3.getText().toString(), small_bold));
            table_mhr_part_c2_invest_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c2_invest_c1.setPadding(5);
            table_mhr_part_c2_invest.addCell(table_mhr_part_c2_invest_c1);

            //document.add(table_mhr_part_b6);

            table_mhr_part_c2_c1 = new PdfPCell(table_mhr_part_c2_invest);
            table_mhr_part_c2.addCell(table_mhr_part_c2_c1);

            document.add(table_mhr_part_c2);

            PdfPTable table_mhr_part_c3 = new PdfPTable(1);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_mhr_part_c3.setWidthPercentage(100);

            //row 1
            PdfPCell table_mhr_part_c3_c1 = new PdfPCell(new Paragraph(
                    "In case of occupation being agriculture :", small_bold));
            table_mhr_part_c3_c1.setPadding(5);
            table_mhr_part_c3.addCell(table_mhr_part_c3_c1);

            document.add(table_mhr_part_c3);

            PdfPTable table_mhr_part_c4 = new PdfPTable(2);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_mhr_part_c4.setWidthPercentage(100);

            //row 1
            PdfPCell table_mhr_part_c4_c1 = new PdfPCell(new Paragraph(
                    "Describe Land Holding, crop pattern, Share of Proposer/Premium payer, whether land is held in joint ownership/tenancy and Income from agriculture.:", small_bold));
            table_mhr_part_c4_c1.setPadding(5);
            table_mhr_part_c4.addCell(table_mhr_part_c4_c1);

            PdfPTable table_mhr_part_c4_land = new PdfPTable(4);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_mhr_part_c4_land.setWidthPercentage(100);

            //row 1
            PdfPCell table_mhr_part_c4_land_c1 = new PdfPCell(new Paragraph("Land Holding(Acres)", small_bold));
            table_mhr_part_c4_land_c1.setPadding(5);
            table_mhr_part_c4_land.addCell(table_mhr_part_c4_land_c1);

            table_mhr_part_c4_land_c1 = new PdfPCell(new Paragraph(edt_mhr_agri_occu_landholding.getText().toString(), small_bold));
            table_mhr_part_c4_land_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c4_land_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c4_land_c1.setPadding(5);
            table_mhr_part_c4_land.addCell(table_mhr_part_c4_land_c1);

            //row 2
            table_mhr_part_c4_land_c1 = new PdfPCell(new Paragraph("Ownership", small_bold));
            table_mhr_part_c4_land_c1.setPadding(5);
            table_mhr_part_c4_land.addCell(table_mhr_part_c4_land_c1);

            table_mhr_part_c4_land_c1 = new PdfPCell(new Paragraph(
                    spnr_mhr_agri_occupation.getSelectedItem().toString(), small_bold));
            table_mhr_part_c4_land_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c4_land_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c4_land_c1.setPadding(5);
            table_mhr_part_c4_land.addCell(table_mhr_part_c4_land_c1);

            //row 3
            table_mhr_part_c4_land_c1 = new PdfPCell(new Paragraph("No. of owners(In case of land owner)", small_bold));
            table_mhr_part_c4_land_c1.setPadding(5);
            table_mhr_part_c4_land.addCell(table_mhr_part_c4_land_c1);

            table_mhr_part_c4_land_c1 = new PdfPCell(new Paragraph(
                    edt_mhr_agri_no_owners.getText().toString(), small_bold));
            table_mhr_part_c4_land_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c4_land_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c4_land_c1.setPadding(5);
            table_mhr_part_c4_land.addCell(table_mhr_part_c4_land_c1);

            //row 4
            table_mhr_part_c4_land_c1 = new PdfPCell(new Paragraph("Share in land(Acers)", small_bold));
            table_mhr_part_c4_land_c1.setPadding(5);
            table_mhr_part_c4_land.addCell(table_mhr_part_c4_land_c1);

            table_mhr_part_c4_land_c1 = new PdfPCell(new Paragraph(
                    edt_mhr_agri_share_in_land.getText().toString(), small_bold));
            table_mhr_part_c4_land_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c4_land_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c4_land_c1.setPadding(5);
            table_mhr_part_c4_land.addCell(table_mhr_part_c4_land_c1);

            //row 5
            table_mhr_part_c4_land_c1 = new PdfPCell(new Paragraph("Corp Pattern", small_bold));
            table_mhr_part_c4_land_c1.setPadding(5);
            table_mhr_part_c4_land.addCell(table_mhr_part_c4_land_c1);

            table_mhr_part_c4_land_c1 = new PdfPCell(new Paragraph(
                    edt_mhr_agri_corp_pattern.getText().toString(), small_bold));
            table_mhr_part_c4_land_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c4_land_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c4_land_c1.setPadding(5);
            table_mhr_part_c4_land.addCell(table_mhr_part_c4_land_c1);

            //row 6
            table_mhr_part_c4_land_c1 = new PdfPCell(new Paragraph("Income in last Three years", small_bold));
            table_mhr_part_c4_land_c1.setPadding(5);
            table_mhr_part_c4_land.addCell(table_mhr_part_c4_land_c1);

            table_mhr_part_c4_land_c1 = new PdfPCell(new Paragraph(
                    edt_mhr_agri_income_3yr.getText().toString(), small_bold));
            table_mhr_part_c4_land_c1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c4_land_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_mhr_part_c4_land_c1.setPadding(5);
            table_mhr_part_c4_land.addCell(table_mhr_part_c4_land_c1);

            table_mhr_part_c4_c1 = new PdfPCell(table_mhr_part_c4_land);
            table_mhr_part_c4.addCell(table_mhr_part_c4_c1);

            //row 2
            table_mhr_part_c4_c1 = new PdfPCell(new Paragraph(
                    "Please mention the documents seen to verify Agricultured income", small_bold));
            table_mhr_part_c4_c1.setPadding(5);
            table_mhr_part_c4.addCell(table_mhr_part_c4_c1);

            table_mhr_part_c4_c1 = new PdfPCell(new Paragraph("" +
                    edt_mhr_agri_doc_verify.getText().toString(), small_bold));
            table_mhr_part_c4_c1.setPadding(5);
            table_mhr_part_c4.addCell(table_mhr_part_c4_c1);

            //row 3
            table_mhr_part_c4_c1 = new PdfPCell(new Paragraph(
                    "Describe income from other sources - rent, interest etc. Please mention the documents seen to verify Other income", small_bold));
            table_mhr_part_c4_c1.setPadding(5);
            table_mhr_part_c4.addCell(table_mhr_part_c4_c1);

            table_mhr_part_c4_c1 = new PdfPCell(new Paragraph("" +
                    edt_mhr_agri_icome_other.getText().toString(), small_bold));
            table_mhr_part_c4_c1.setPadding(5);
            table_mhr_part_c4.addCell(table_mhr_part_c4_c1);

            document.add(table_mhr_part_c4);

            document.add(para_img_logo_after_space_1);

            PdfPTable table_mhr_part_d = new PdfPTable(1);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_mhr_part_d.setWidthPercentage(100);

            //row 1
            PdfPCell table_mhr_part_d_c1;

            if (cb_mhr_medical_status_yes.isChecked())
                table_mhr_part_d_c1 = new PdfPCell(new Paragraph(
                        getResources().getString(R.string.str_mhr_point_d) + " : Yes", small_bold));
            else
                table_mhr_part_d_c1 = new PdfPCell(new Paragraph(
                        getResources().getString(R.string.str_mhr_point_d) + " : No", small_bold));

            table_mhr_part_d_c1.setPadding(5);
            table_mhr_part_d.addCell(table_mhr_part_d_c1);

            table_mhr_part_d_c1 = new PdfPCell(new Paragraph("If yes, please provide the details..\n"
                    + rb_mhr_medical_status_details.getText().toString(), small_bold));
            table_mhr_part_d_c1.setPadding(5);
            table_mhr_part_d.addCell(table_mhr_part_d_c1);

            document.add(table_mhr_part_d);

            document.add(para_img_logo_after_space_1);

            PdfPTable table_mhr_part_e = new PdfPTable(1);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_mhr_part_e.setWidthPercentage(100);

            //row 1
            PdfPCell table_mhr_part_e_c1;

            if (cb_mhr_is_legal_risk_y.isChecked())
                table_mhr_part_e_c1 = new PdfPCell(new Paragraph(
                        getResources().getString(R.string.str_mhr_point_e) + " : Yes", small_bold));
            else
                table_mhr_part_e_c1 = new PdfPCell(new Paragraph(
                        getResources().getString(R.string.str_mhr_point_e) + " : No", small_bold));

            table_mhr_part_e_c1.setPadding(5);
            table_mhr_part_e.addCell(table_mhr_part_e_c1);

            table_mhr_part_e_c1 = new PdfPCell(new Paragraph("If yes, please provide the details..\n"
                    + edt_mhr_is_legal_risk_details.getText().toString(), small_bold));
            table_mhr_part_e_c1.setPadding(5);
            table_mhr_part_e.addCell(table_mhr_part_e_c1);

            document.add(table_mhr_part_e);

            document.add(para_img_logo_after_space_1);

            PdfPTable table_mhr_part_f = new PdfPTable(1);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_mhr_part_f.setWidthPercentage(100);

            //row 1
            PdfPCell table_mhr_part_f_c1 = new PdfPCell(new Paragraph(
                    getResources().getString(R.string.str_mhr_point_f), small_bold));
            table_mhr_part_f_c1.setPadding(5);
            table_mhr_part_f.addCell(table_mhr_part_f_c1);

            table_mhr_part_f_c1 = new PdfPCell(new Paragraph("" + edt_mhr_oponion_recommend.getText().toString(), small_bold));
            table_mhr_part_f_c1.setPadding(5);
            table_mhr_part_f.addCell(table_mhr_part_f_c1);

            table_mhr_part_f_c1 = new PdfPCell(new Paragraph(getResources().getString(R.string.str_mhr_point_elabrorate), small_bold));
            table_mhr_part_f_c1.setPadding(5);
            table_mhr_part_f.addCell(table_mhr_part_f_c1);

            document.add(table_mhr_part_f);

            //go to new page
            document.newPage();

            if (mMHRReportPic != null) {
                Paragraph para_applicant_pic = new Paragraph("Applicant Photo", small_bold);
                para_applicant_pic.setAlignment(Element.ALIGN_RIGHT);
                document.add(para_applicant_pic);

                //applicant main image
                Bitmap applicantPicBitmap = mCommonMethods.getBitmap(mMHRReportPic.getAbsolutePath());

                ByteArrayOutputStream applicant_pic_stream = new ByteArrayOutputStream();

                (applicantPicBitmap).compress(Bitmap.CompressFormat.PNG, 50, applicant_pic_stream);

                Image applicant_photo = Image.getInstance(applicant_pic_stream.toByteArray());
                applicant_photo.setAlignment(Element.ALIGN_RIGHT);
                applicant_photo.scaleToFit(120, 120);
                //applicant_photo.setAbsolutePosition(100f, 30f);
                document.add(applicant_photo);
                document.add(para_img_logo_after_space_1);
            }

            Paragraph para_mhr_declaration = new Paragraph(getResources().getString(R.string.str_mhr_point_verify), small_normal);
            document.add(para_mhr_declaration);

            document.add(para_img_logo_after_space_1);

            Paragraph para_mhr_suc = new Paragraph("SUC : " + edt_mhr_suc.getText().toString(), small_bold);
            document.add(para_mhr_suc);

            Paragraph para_mhr_suc_auth = new Paragraph("MHR Authority as per SUC : ", small_bold);
            document.add(para_mhr_suc_auth);

            /*Paragraph para_mhr_Designation = new Paragraph("Name and Designation : "
                    + edtMHRNameDesign.getText().toString(), small_bold);
            document.add(para_mhr_Designation);

            Paragraph para_mhr_lat_long = new Paragraph("Latitude : "
                    + mLatLng.latitude + " and Longitude : " + mLatLng.longitude, small_bold);
            document.add(para_mhr_lat_long);

            Paragraph para_mhr_lat_long_address = new Paragraph("Address as per Latitude/Longitude : "
                    + mCommonMethods.getCompleteAddressString(mContext, mLatLng.latitude, mLatLng.longitude), small_bold);
            document.add(para_mhr_lat_long_address);*/

            document.add(para_img_logo_after_space_1);

            PdfPTable table_mhr_place_sign = new PdfPTable(3);
            table_mhr_place_sign.setWidths(new float[]{5f, 5f, 5f});
            table_mhr_place_sign.setWidthPercentage(100);

            /*byte[] fbyt_applicant = Base64.decode(str_signature_applicant, 0);
            Bitmap applicantBitmap = BitmapFactory.decodeByteArray(fbyt_applicant, 0, fbyt_applicant.length);

            ByteArrayOutputStream applicant_signature_stream = new ByteArrayOutputStream();

            (applicantBitmap).compress(Bitmap.CompressFormat.PNG, 50, applicant_signature_stream);
            Image applicant_signature = Image.getInstance(applicant_signature_stream.toByteArray());

            applicant_signature.scaleToFit(90, 90);*/

            PdfPCell Nocell = new PdfPCell(new Paragraph("", small_bold));
            Nocell.setBorder(Rectangle.NO_BORDER);

            //row 1
            table_mhr_place_sign.addCell(Nocell);
            table_mhr_place_sign.addCell(Nocell);
            table_mhr_place_sign.addCell(Nocell);

            //row 2
            PdfPCell sign_cell = new PdfPCell(new Paragraph(
                    "Place : " + edt_mhr_place_last.getText().toString(), small_bold));
            sign_cell.setHorizontalAlignment(Element.ALIGN_BOTTOM);
            sign_cell.setBorder(Rectangle.NO_BORDER);
            table_mhr_place_sign.addCell(sign_cell);

            table_mhr_place_sign.addCell(Nocell);
            table_mhr_place_sign.addCell(Nocell);

            //row 3
            sign_cell = new PdfPCell(new Paragraph(
                    "Date : " + edt_mhr_last_date.getText().toString(), small_bold));
            sign_cell.setHorizontalAlignment(Element.ALIGN_TOP);
            sign_cell.setBorder(Rectangle.NO_BORDER);
            table_mhr_place_sign.addCell(sign_cell);

            table_mhr_place_sign.addCell(Nocell);

            String str_usertype = mCommonMethods.GetUserType(mContext);
            String str_sign_header;
            String code = mCommonMethods.GetUserCode(mContext);
            String name = mCommonMethods.getUserName(mContext);

            str_sign_header = "(" + str_usertype + " code - " + code
                    + ")\nName of " + str_usertype + " - " + name
                    + "\nAuthenticated by Id & Password";

            sign_cell = new PdfPCell(new Paragraph(
                    str_sign_header, small_bold));
            sign_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            sign_cell.setPadding(5);
            table_mhr_place_sign.addCell(sign_cell);


            //footer
            String[] strFooter = new String[3];
            strFooter[0] = "Name and Designation : " + edtMHRNameDesign.getText().toString();
            strFooter[1] = "Latitude : " + mLatLng.latitude + " and Longitude : " + mLatLng.longitude;
            strFooter[2] = "Address as per Latitude/Longitude : "
                    + mCommonMethods.getCompleteAddressString(mContext, mLatLng.latitude, mLatLng.longitude);

            /*Paragraph para_mhr_Designation = new Paragraph("Name and Designation : "
                    + edtMHRNameDesign.getText().toString(), small_bold);
            document.add(para_mhr_Designation);

            Paragraph para_mhr_lat_long = new Paragraph("Latitude : "
                    + mLatLng.latitude + " and Longitude : " + mLatLng.longitude, small_bold);
            document.add(para_mhr_lat_long);

            Paragraph para_mhr_lat_long_address = new Paragraph("Address as per Latitude/Longitude : "
                    + mCommonMethods.getCompleteAddressString(mContext, mLatLng.latitude, mLatLng.longitude), small_bold);
            document.add(para_mhr_lat_long_address);*/

            HeaderFooter headerFooter = new HeaderFooter(strFooter);
            pdf_writer.setPageEvent(headerFooter);

            document.add(table_mhr_place_sign);

            document.close();

        } catch (Exception ex) {
            mCommonMethods.showCentralToast(mContext, "Error: " + ex.getMessage());
        }
    }

    public void gotoHomeDialog(String message) {
        try {
            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_with_ok_button);
            dialog.setCancelable(false);

            TextView text = (TextView) dialog.findViewById(R.id.tv_title);
            text.setText(message);

            Button dialogButton = (Button) dialog.findViewById(R.id.bt_ok);
            dialogButton.setText("Ok");
            dialogButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();

                    Activity_AOB_Authentication.row_details = 0;

                    //redirect to home page
                    Intent i = new Intent(ActivityE_MHR.this, CarouselHomeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        /*if (mediaController.isShowing())
            mediaController.hide();*/

        if (mGetMHRDetails != null) {
            mGetMHRDetails.cancel(true);
        }

        if (mAsyncUploadFileCommon != null) {
            mAsyncUploadFileCommon.cancel(true);
        }

        if (service != null) {
            service.cancel(true);
        }
    }

    @Override
    public void downLoadData() {
        //synch video and pdf
        //str_doc = MHR_VIDEO_DOC;
        str_doc = MHR_PDF_DOC;
        upload_docs();
    }

    private void service_hits() {

        service = new ServiceHits(mContext,
                METHOD_NAME_MHR_DETAILS_SMRT, strProposerNumber,
                mCommonMethods.GetUserID(mContext),
                mCommonMethods.GetUserEmail(mContext), mCommonMethods.GetUserMobile(mContext),
                mCommonMethods.GetUserPassword(mContext), this);
        service.execute();

    }

    /*class VideoCompressAsyncTask extends AsyncTask<String, String, String> {

        Context mContext;

        public VideoCompressAsyncTask(Context context){
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext,
                    ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog
                    .setTitle(Html
                            .fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... paths) {
            String filePath = "";
            try {

                filePath = SiliCompressor.with(mContext).compressVideo(Uri.fromFile(mMHRVideoFile),
                        mCommonMethods.EXTERNAL_STORAGE_DIRECTORY + "/SBI-Smart Advisor");

            } catch (URISyntaxException e) {
                e.printStackTrace();
                filePath = "";
            }
            return filePath;

        }


        @Override
        protected void onPostExecute(String compressedFilePath) {

            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (!compressedFilePath.equals("")){

                try{

                    File mCompressedFile = new File(compressedFilePath);

                    mCommonMethods.copyFile(new FileInputStream(mCompressedFile),
                            new FileOutputStream(mMHRVideoFile));

                    rl_mhr_upload_video.setVisibility(View.VISIBLE);
                    vv_mhr_upload_video.setVisibility(View.GONE);
                    iv_mhr_upload_video_img.setVisibility(View.VISIBLE);
                    iv_mhr_upload_video_play.setVisibility(View.VISIBLE);

                    videoImgBitmap = createVideoThumbNail(mMHRVideoFile.getPath());
                    iv_mhr_upload_video_img.setImageBitmap(videoImgBitmap);

                    vv_mhr_upload_video.setVideoURI(Uri.fromFile(mMHRVideoFile));

                    isVideoTaken = true;

                    if (mCompressedFile.exists())
                        mCompressedFile.delete();

                }catch (IOException ex){
                    ex.printStackTrace();
                    isVideoTaken = false;
                }
                //mCommonMethods.showToast(mContext, "file compressed successfull..");

            }else {
                mCommonMethods.showMessageDialog(mContext, "Error while Compressing data..");
            }
        }
    }*/

    private void createSoapRequestToUploadDoc() {
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_UPLOAD_ALL_DOC);

        request.addProperty("f", mAllBytes);

        if (str_doc.equals(MHR_VIDEO_DOC)) {
            request.addProperty("fileName", mMHRVideoFile.getName().toString());
        } else if (str_doc.equals(MHR_PDF_DOC)) {
            request.addProperty("fileName", mMHRPdf.getName().toString());
        }

        request.addProperty("qNo", strProposerNumber);
        request.addProperty("agentCode", mCommonMethods.GetUserID(mContext));
        request.addProperty("strEmailId", mCommonMethods.GetUserEmail(mContext));
        request.addProperty("strMobileNo", mCommonMethods.GetUserMobile(mContext));
        request.addProperty("strAuthKey", "");

        mAsyncUploadFileCommon = new AsyncUploadFile_Common(mContext, this, request, METHOD_NAME_UPLOAD_ALL_DOC);
        mAsyncUploadFileCommon.execute();
    }

    @Override
    public void onUploadComplete(Boolean result) {
        if (result) {

            if (str_doc.equals(MHR_VIDEO_DOC)) {

                if (mMHRVideoFile.exists())
                    mMHRVideoFile.delete();

                str_doc = MHR_PDF_DOC;
                upload_docs();

            } else if (str_doc.equals(MHR_PDF_DOC)) {

                if (mMHRPdf.exists())
                    mMHRPdf.delete();

                if (mMHRReportPic != null) {
                    if (mMHRReportPic.exists())
                        mMHRReportPic.delete();
                }

                gotoHomeDialog("MHR submitted successfully!!");
            }

        } else {
            mCommonMethods.showToast(mContext, "PLease try agian later..");
        }
    }

    class GetMHRDetails extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String inputpolicylist = "", strAuthUserErrorCOde = "";
        ;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setTitle(Html.fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {

            if (mCommonMethods.isNetworkConnected(mContext)) {
                try {

                    running = true;
                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_MHR_DETAILS_SMRT);

                    request.addProperty("strCode", mCommonMethods.GetUserCode(mContext));
                    request.addProperty("strProposalNo", strProposerNumber.toUpperCase());
                    mCommonMethods.appendSecurityParams(mContext, request,
                            mCommonMethods.GetUserEmail(mContext), mCommonMethods.GetUserMobile(mContext));

                    mCommonMethods.TLSv12Enable();

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);
                    envelope.dotNet = true;

                    envelope.setOutputSoapObject(request);

                    HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);

                    String SOAP_ACTION_MHR_DETAILS_SMRT = "http://tempuri.org/getMHRdetail_smrt";
                    androidHttpTranport.call(SOAP_ACTION_MHR_DETAILS_SMRT, envelope);

                    SoapPrimitive sa;
                    try {

                        sa = (SoapPrimitive) envelope.getResponse();
                        inputpolicylist = sa.toString();

                        /*<NewDataSet> <Table> <PROPOSALNUMBER>2DQV666555</PROPOSALNUMBER> <NAME>SAPNA .</NAME> <REGION>Delhi</REGION> <CHANNEL>Bancassurance</CHANNEL> <PCNAME>New Delhi Pc</PCNAME> <SUC>0</SUC> <SUMASSURED>125000</SUMASSURED> </Table> </NewDataSet>*/
                        if (inputpolicylist.equals("0")) {
                            strAuthUserErrorCOde = "0";
                        } else {

                            ParseXML prsObj = new ParseXML();

                            inputpolicylist = prsObj.parseXmlTag(inputpolicylist,
                                    "NewDataSet");
                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "Table");

                            strProposerName = prsObj.parseXmlTag(inputpolicylist, "NAME");
                            strRegion = prsObj.parseXmlTag(inputpolicylist, "REGION");
                            strChannel = prsObj.parseXmlTag(inputpolicylist, "CHANNEL");
                            strPCName = prsObj.parseXmlTag(inputpolicylist, "PCNAME");
                            strSUC = prsObj.parseXmlTag(inputpolicylist, "SUC");
                            strSumAssured = prsObj.parseXmlTag(inputpolicylist, "SUMASSURED");

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        running = false;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    running = false;
                }
            } else {
                running = true;
                strAuthUserErrorCOde = mCommonMethods.NO_INTERNET_MESSAGE;
            }
            return null;
        }


        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (running) {

                if (strAuthUserErrorCOde.equals(mCommonMethods.NO_INTERNET_MESSAGE)) {
                    ll_mhr_enable_ui.setVisibility(View.GONE);
                    mCommonMethods.showMessageDialog(mContext, mCommonMethods.NO_INTERNET_MESSAGE);
                } else if (strAuthUserErrorCOde.equals("0")) {
                    ll_mhr_enable_ui.setVisibility(View.GONE);
                    mCommonMethods.showMessageDialog(mContext, "No record found..");
                } else {

                    String userType = mCommonMethods.GetUserType(mContext);

                    strSumAssured = strSumAssured == null ? "0" : strSumAssured;
                    int sumAssurred = Integer.parseInt(strSumAssured);

                    ll_mhr_enable_ui.setVisibility(View.VISIBLE);

                    edt_mhr_sum_assured.setText(strSumAssured);
                    txt_mhr_proposer_name.setText(strProposerName);
                    edt_mhr_suc.setText(strSumAssured);
                    edt_mhr_suc_last.setText(strSumAssured);
                    txt_mhr_region.setText(strRegion);
                    txt_mhr_channel.setText(strChannel);
                    txt_mhr_pc_name.setText(strPCName);

                    mCommonMethods.showGPSDisabledAlertToUser(mContext);

                    //Agency
                    /*if (userType.equals("UM") || userType.equals("BSM") || userType.equals("DSM")){

                        if (sumAssurred >= 2500000){
                            ll_mhr_enable_ui.setVisibility(View.VISIBLE);

                            edt_mhr_sum_assured.setText(strSumAssured);
                            txt_mhr_proposer_name.setText(strProposerName);
                            edt_mhr_suc.setText(strSumAssured);
                            edt_mhr_suc_last.setText(strSumAssured);
                            txt_mhr_region.setText(strRegion);
                            txt_mhr_channel.setText(strChannel);
                            txt_mhr_pc_name.setText(strPCName);
                        }else {
                            ll_mhr_enable_ui.setVisibility(View.GONE);
                            mCommonMethods.showMessageDialog(mContext, "Sum Assured should be above 25 lacs.");
                        }

                    }
                    else if (userType.equals("BDM") || userType.equals("AM")
                            || userType.equals("SAM") || userType.equals("ZAM")){//Banca

                        if (sumAssurred >= 3000000){
                            ll_mhr_enable_ui.setVisibility(View.VISIBLE);

                            edt_mhr_sum_assured.setText(strSumAssured);
                            txt_mhr_proposer_name.setText(strProposerName);
                            edt_mhr_suc.setText(strSumAssured);
                            edt_mhr_suc_last.setText(strSumAssured);
                            txt_mhr_region.setText(strRegion);
                            txt_mhr_channel.setText(strChannel);
                            txt_mhr_pc_name.setText(strPCName);
                        }else {
                            ll_mhr_enable_ui.setVisibility(View.GONE);
                            mCommonMethods.showMessageDialog(mContext, "Sum Assured should be above 30 lacs.");
                        }
                    }*/
                }

            } else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.SERVER_ERROR);
            }
        }
    }
}
