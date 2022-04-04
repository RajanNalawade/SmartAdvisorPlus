package sbilife.com.pointofsale_bancaagency.common;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import org.apache.commons.lang.StringUtils;
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
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import sbilife.com.pointofsale_bancaagency.CaptureSignature;
import sbilife.com.pointofsale_bancaagency.CreateMenuDialog;
import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.ProposerCaptureSignature;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.authorization.LoginUserActivity;
import sbilife.com.pointofsale_bancaagency.branchlocator.GPSTracker;
import sbilife.com.pointofsale_bancaagency.needanalysis.NeedAnalysisActivity;
import sbilife.com.pointofsale_bancaagency.reports.RenewalCallingRemarksActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.GetAltMobUpdateKeyAsync;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.UpdateAltMobileNoCommonAsyncTask;
import sbilife.com.pointofsale_bancaagency.utility.Element_TextView_BaseAdapter;
import sbilife.com.pointofsale_bancaagency.utility.Tls12SocketFactory;

public class CommonMethods {

    public static final String EXTERNAL_STORAGE_DIRECTORY = Environment.getExternalStorageDirectory().toString();

    public static final String DIRECT_DIRECTORY = "/SBI-Smart Advisor/";
    public static final String DIRECT_DIRECTORY_CIF = "/SbiLife-CIF/";

    public final String str_posp_ra_customer_type = "POSP RA";
    public final String str_ia_upgrade_customer_type = "IA Upgrade";

    public final int SIGNATURE_ACTIVITY = 1;
    final String Chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public String NO_INTERNET_MESSAGE = "Internet connection not present,try again..";
    public String WEEK_INTERNET_MESSAGE = "Please Check Internet connection,try again..";
    public String SERVER_ERROR = "Server not responding,try again..";
    public String POLICY_NUMBER_ALERT = "Please enter policy no...";
    public String NO_RECORD_FOUND = "No record found";
    public String FIRST_NAME_ALERT = "Please enter first name...";
    public String DATE_ALERT = "Please enter date...";
    public String STATUS_ALERT = "Please select status...";
    public String PREMIUM_ALERT = "Please enter premium...";
    public String PROPOSAL_EMPTY_ALERT = "Please enter Proposal no..";
    public String ALL_FIELDS_REQUIRED_ALERT = "All fields required..";
    public String DOB_ALERT = "Please select your DOB..";
    public String PASSWORD_ALERT = "Enter your password..";
    public String TWENTY_DIGIT_ALERT = "You can  not enter more than 20 Character";
    public String INVALID_DATE_ALERT = "Invalid date format...";
    public String UNAUTHORISED_USER_ALERT = "You are not authorised user..";
    public String INVALID_DOB_ALERT = "Please select your DOB..";
    public String USER_CODE_ALERT = "Please enter your user code..";
    public int FILE_UPLOAD_RESTRICT_SIZE = 2048;
    public int FILE_UPLOAD_CIF_PHOTO_SIGN_RESTRICT_SIZE = 50;
    public String FILE_UPLOAD_RESTRICT_SIZE_MSG = "File Size should be less than 2MB";
    public String FILE_UPLOAD_CIF_PHOTO_SIGN_RESTRICT_SIZE_MSG = "Photo and Sign Size should be less than 50K";
    public String SB_DUE_LIST_NOTIFICATION_PREFERENCE = "sb_due_list_count";
    public String MATURITY_LIST_NOTIFICATION_PREFERENCE = "maturity_list_count";
    public String RENEWAL_NOTE = "<b>*Note:</b><i>For Technical Lapse & Lapse policy kindly hold policy number & select Revival Quotation for exact premium amount.</i>";
    private String custPhotoName = "";
    private String strAuth = "QzhCNDc0OTU4NzZDQjI3RTQ4OEMyNEQ3MUZCQjE2QTY=";
    private String str_cif_auth_key = "qwertyuioplkjhgfdhbhytr";
    //to block specail character
    //private String blockCharacterSet = "'\"()";
    private String blockCharacterSet = "[$&+,:;=\\\\?@#|/'<>^*()%!-]";
    public InputFilter aob_address_filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }

            return null;
        }
    };
    private String blockMobileCharacterSet = "[$&+,:;=\\\\?@#|./'<>^*()%!-]";
    public InputFilter aob_mobile_filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockMobileCharacterSet.contains(("" + source))) {
                return "";
            }

            //to block spaces
            for (int i = start; i < end; i++) {
                if (Character.isWhitespace(source.charAt(i))) {
                    if (dstart == 0)
                        return "";
                }
            }

            return null;
        }
    };
    private File f;
    private String mCurrentPhotoPath = "";
    private Context contextPhoto;

    private static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /*public static String getFilePathFromURI(Context context, Uri contentUri) {
        //copy file and send new file path
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
            File copyFile = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + fileName);
            copy(context, contentUri, copyFile);
            return copyFile.getPath();
        }
        return null;
    }*/

    /*public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            IOUtil.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public String getStrAuth() {
        return strAuth;
    }

    public boolean isDeviceAboveM() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public String getStr_cif_auth_key() {
        return str_cif_auth_key;
    }

    //to remove extra spaces
    public String removeExtraWhiteSpaces(EditText mEditText) {
        return StringUtils.normalizeSpace(mEditText.getText().toString());
    }

    public void setApplicationToolbarMenu(final AppCompatActivity activity, String title) {

        //getting the toolbar
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar_menu);
        //placing toolbar in place of actionbar
        ((AppCompatActivity) activity).setSupportActionBar(toolbar);

        final ImageButton imagtbt_option = activity.findViewById(R.id.imagtbt_option);

        TextView tv_title_common = activity.findViewById(R.id.tv_commonTitle);
        tv_title_common.setText(title);

        final String userNameSaved = getUserName(activity);
        System.out.println(" :userNameSaved:" + userNameSaved);

        System.out.println("activity:Comm:" + activity.getCallingPackage());
        System.out.println("activity.getClass:Comm:" + activity.getClass());

        imagtbt_option.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onApplicationMenu(imagtbt_option, userNameSaved, activity);

            }
        });
    }

    public void setApplicationToolbarMenu1(final AppCompatActivity activity, String title) {

        //getting the toolbar
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar_menu);
        //placing toolbar in place of actionbar
        ((AppCompatActivity) activity).setSupportActionBar(toolbar);

        TextView tv_title_common = activity.findViewById(R.id.tv_title_cif_common);
        tv_title_common.setText(title);

    }

    public String getDASHBOARD_RENEWAL_UPDATE_PREFERENCE() {
        String DASHBOARD_RENEWAL_UPDATE_PREFERENCE = "dashboard_renewal_update_pref";
        return DASHBOARD_RENEWAL_UPDATE_PREFERENCE;
    }

    public String getNOTIFICATION_PREFERENCE() {
        String NOTIFICATION_PREFERENCE = "notify_pref";
        return NOTIFICATION_PREFERENCE;
    }

    public String getLOGGED_PROPOSAL_COUNT_NOTIFICATION_PREFERENCE() {
        String LOGGED_PROPOSAL_COUNT_NOTIFICATION_PREFERENCE = "logged_proposal_count";
        return LOGGED_PROPOSAL_COUNT_NOTIFICATION_PREFERENCE;
    }

    public String getNON_MEDICAL_PENDING_REQ_COUNT_NOTIFICATION_PREFERENCE() {
        String NON_MEDICAL_PENDING_REQ_COUNT_NOTIFICATION_PREFERENCE = "non_med_pend_req";
        return NON_MEDICAL_PENDING_REQ_COUNT_NOTIFICATION_PREFERENCE;
    }

    public String getMEDICAL_PENDING_REQ_COUNT_NOTIFICATION_PREFERENCE() {
        String MEDICAL_PENDING_REQ_COUNT_NOTIFICATION_PREFERENCE = "med_pend_req";
        return MEDICAL_PENDING_REQ_COUNT_NOTIFICATION_PREFERENCE;
    }

    public String getClaimRequirementInfo() {
        String ClaimRequirementInfo = "ClaimRequirementInfo";
        return ClaimRequirementInfo;
    }

    public String getRevivalNotificationInfo() {
        String revivalNotificationInfo = "RevivalNotificationInfo";
        return revivalNotificationInfo;
    }

    public String getKYCMissingNotification() {
        String revivalNotificationInfo = "KYCMissingNotification";
        return revivalNotificationInfo;
    }

    public String getKerlaDiscount() {
        String kerlaDiscount = "kerlaDiscount";
        return kerlaDiscount;
    }

    public String getLMSurveyString() {
        String LMSurveyStr = "LMSurveyXmlString";
        return LMSurveyStr;
    }

    public String getMPOSDisclaimer() {
        String MPOSDisclaimer = "MPOSDisclaimer";
        return MPOSDisclaimer;
    }

    public String getPersistencyNotLaunchDate() {
        String lastLaunchDate = "LAST_PER_NOT_LAUNCH_DATE";
        return lastLaunchDate;
    }

    public void TLSv12Enable() {
        try {
            SSLContext sslcontext = SSLContext.getInstance("TLSv1.2");

            sslcontext.init(null, null, null);
            Tls12SocketFactory tls12SocketFactory = new Tls12SocketFactory(
                    sslcontext.getSocketFactory());
            // SSLSocketFactory NoSSLv3Factory = new
            // NoSSLv3SocketFactory(sslcontext.getSocketFactory());

            HttpsURLConnection.setDefaultSSLSocketFactory(tls12SocketFactory);

        } catch (Exception e) {
            Log.e("allowAllSSL", e.toString());
        }
    }

    public void showMessageDialog(Context context, String message) {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_with_ok_button);
            TextView text = dialog.findViewById(R.id.tv_title);
            text.setText(message);
            Button dialogButton = dialog.findViewById(R.id.bt_ok);
            dialogButton.setText("Ok");

            dialogButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void BICommonDialog(Context mContext, String msg) {

        final Dialog d = new Dialog(mContext);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        d.setContentView(R.layout.window_agreement);
        TextView textMessage = d.findViewById(R.id.textMessage);
        textMessage.setText(msg);
        Button ok = d.findViewById(R.id.idbtnagreement);
        ok.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                d.dismiss();
            }

        });
        d.setCancelable(true);
        d.setCanceledOnTouchOutside(true);
        d.show();
    }

    public void dialogWarning(Context context, String msg, boolean ismandatry) {

        final Dialog d = new Dialog(context);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        d.setContentView(R.layout.window_pop_up_message_with_single_options);
        TextView text_mssg_1 = d.findViewById(R.id.text_mssg_1);
        text_mssg_1.setText(msg);
        TextView text_mssg_2 = d.findViewById(R.id.text_mssg_2);
        TextView text_mssg_3 = d.findViewById(R.id.text_mssg_3);
        if (ismandatry) {
            text_mssg_2.setVisibility(View.GONE);
            text_mssg_3.setVisibility(View.GONE);
        }
        Button ok = d.findViewById(R.id.idbtnagreement);
        ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                d.dismiss();
            }

        });
        d.setCancelable(true);
        d.setCanceledOnTouchOutside(true);
        d.show();

    }

    public boolean isNetworkConnected(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            // There are active networks.
            return ni != null && ni.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void showToast(Context context, String message) {
        try {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showCentralToast(Context context, String message) {
        try {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String GetUserCode(Context context) {

        DatabaseHelper dbhelper = new DatabaseHelper(context);
        String strUserType = "";
        try {
            strUserType = SimpleCrypto.decrypt("SBIL", dbhelper.GetUserCode());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return strUserType;
    }

    public String GetUserType(Context context) {

        DatabaseHelper dbhelper = new DatabaseHelper(context);
        String strUserType = "";
        try {
            strUserType = SimpleCrypto.decrypt("SBIL",
                    dbhelper.GetUserType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strUserType;
    }

    public String GetUserID(Context context) {

        DatabaseHelper dbhelper = new DatabaseHelper(context);
        String strCIFBDMUserId = "";
        try {
            strCIFBDMUserId = SimpleCrypto.decrypt("SBIL", dbhelper.GetCIFNo());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("strCIFBDMUserId:" + strCIFBDMUserId);
        return strCIFBDMUserId;
    }

    public String GetUserEmail(Context context) {

        DatabaseHelper dbhelper = new DatabaseHelper(context);
        String strUserType = "";
        try {
            strUserType = SimpleCrypto.decrypt("SBIL", dbhelper.GetUserEmailId());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return strUserType;
    }

    public String GetUserPassword(Context context) {

        DatabaseHelper dbhelper = new DatabaseHelper(context);
        String strUserType = "";
        try {
            strUserType = SimpleCrypto.decrypt("SBIL", dbhelper.GetPassword());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return strUserType;
    }

    public String GetUserMobile(Context context) {

        DatabaseHelper dbhelper = new DatabaseHelper(context);
        String strUserType = "";
        try {
            strUserType = SimpleCrypto.decrypt("SBIL", dbhelper.GetMobileNo());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strUserType;
    }

    public String getFullMonthName(String month) {
        String fullMonthName = "";

        if (month.contentEquals("1") || month.contentEquals("01")) {
            fullMonthName = "January";
        } else if (month.contentEquals("2") || month.contentEquals("02")) {
            fullMonthName = "February";

        } else if (month.contentEquals("3") || month.contentEquals("03")) {
            fullMonthName = "March";

        } else if (month.contentEquals("4") || month.contentEquals("04")) {
            fullMonthName = "April";

        } else if (month.contentEquals("5") || month.contentEquals("05")) {
            fullMonthName = "May";

        } else if (month.contentEquals("6") || month.contentEquals("06")) {
            fullMonthName = "June";

        } else if (month.contentEquals("7") || month.contentEquals("07")) {
            fullMonthName = "July";

        } else if (month.contentEquals("8") || month.contentEquals("08")) {
            fullMonthName = "August";

        } else if (month.contentEquals("9") || month.contentEquals("09")) {
            fullMonthName = "September";

        } else if (month.contentEquals("10")) {
            fullMonthName = "October";

        } else if (month.contentEquals("11")) {
            fullMonthName = "November";

        } else if (month.contentEquals("12")) {
            fullMonthName = "December";
        }
        return fullMonthName;
    }

    public String getShortMonthName(String month) {
        String fullMonthName = "";

        if (month.contentEquals("1") || month.contentEquals("01")) {
            fullMonthName = "Jan";
        } else if (month.contentEquals("2") || month.contentEquals("02")) {
            fullMonthName = "Feb";

        } else if (month.contentEquals("3") || month.contentEquals("03")) {
            fullMonthName = "Mar";

        } else if (month.contentEquals("4") || month.contentEquals("04")) {
            fullMonthName = "Apr";

        } else if (month.contentEquals("5") || month.contentEquals("05")) {
            fullMonthName = "May";

        } else if (month.contentEquals("6") || month.contentEquals("06")) {
            fullMonthName = "Jun";

        } else if (month.contentEquals("7") || month.contentEquals("07")) {
            fullMonthName = "Jul";

        } else if (month.contentEquals("8") || month.contentEquals("08")) {
            fullMonthName = "Aug";

        } else if (month.contentEquals("9") || month.contentEquals("09")) {
            fullMonthName = "Sep";

        } else if (month.contentEquals("10")) {
            fullMonthName = "Oct";

        } else if (month.contentEquals("11")) {
            fullMonthName = "Nov";

        } else if (month.contentEquals("12")) {
            fullMonthName = "Dec";
        }
        return fullMonthName;
    }

    public String getMonthNumber(String fullMonthName) {
        String monthNumber = "";

        if (fullMonthName.equalsIgnoreCase("January") || fullMonthName.equalsIgnoreCase("Jan")) {
            monthNumber = "01";
        } else if (fullMonthName.equalsIgnoreCase("February") || fullMonthName.equalsIgnoreCase("Feb")) {
            monthNumber = "02";
        } else if (fullMonthName.equalsIgnoreCase("March") || fullMonthName.equalsIgnoreCase("Mar")) {
            monthNumber = "03";
        } else if (fullMonthName.equalsIgnoreCase("April") || fullMonthName.equalsIgnoreCase("Apr")) {
            monthNumber = "04";
        } else if (fullMonthName.equalsIgnoreCase("May")) {
            monthNumber = "05";
        } else if (fullMonthName.equalsIgnoreCase("June") || fullMonthName.equalsIgnoreCase("Jun")) {
            monthNumber = "06";
        } else if (fullMonthName.equalsIgnoreCase("July") || fullMonthName.equalsIgnoreCase("Jul")) {
            monthNumber = "07";
        } else if (fullMonthName.equalsIgnoreCase("August") || fullMonthName.equalsIgnoreCase("Aug")) {
            monthNumber = "08";
        } else if (fullMonthName.equalsIgnoreCase("September") || fullMonthName.equalsIgnoreCase("Sep")) {
            monthNumber = "09";
        } else if (fullMonthName.equalsIgnoreCase("October") || fullMonthName.equalsIgnoreCase("Oct")) {
            monthNumber = "10";
        } else if (fullMonthName.equalsIgnoreCase("November") || fullMonthName.equalsIgnoreCase("Nov")) {
            monthNumber = "11";
        } else if (fullMonthName.equalsIgnoreCase("December") || fullMonthName.equalsIgnoreCase("Dec")) {
            monthNumber = "12";
        }
        return monthNumber;
    }

    public void printLog(String title, String msg) {
        Log.d(title, msg);
    }

    public String getFormattedDate(String strDOB) {

        Date dt1 = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df1 = new SimpleDateFormat("d-MMMM-yyyy");
        try {
            dt1 = df.parse(strDOB);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return df1.format(dt1);

    }

    public String formatDateForerver(String strDOB) {

        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");
        final SimpleDateFormat formatter1 = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date dt = formatter.parse(strDOB);
            strDOB = formatter1.format(dt);
        } catch (ParseException e) {
            e.printStackTrace();
            strDOB = "";
        }
        return strDOB;
    }

    @SuppressWarnings("deprecation")
    public void showProgressDialog(AppCompatActivity activity,
                                   int DIALOG_DOWNLOAD_PROGRESS) {
        activity.showDialog(DIALOG_DOWNLOAD_PROGRESS);
    }

    public UserDetailsValuesModel setUserDetails(Context context) {
        String strCIFBDMUserId = "", strCIFBDMEmailId = "", strCIFBDMPassword = "", strCIFBDMMObileNo = "";
        DatabaseHelper dbhelper = new DatabaseHelper(context);
        try {
            strCIFBDMUserId = SimpleCrypto.decrypt("SBIL",
                    dbhelper.GetCIFNo());
            strCIFBDMEmailId = SimpleCrypto.decrypt("SBIL",
                    dbhelper.GetEmailId());
            strCIFBDMPassword = dbhelper.GetPassword();
            strCIFBDMMObileNo = SimpleCrypto.decrypt("SBIL",
                    dbhelper.GetMobileNo());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbhelper.close();
        }


        return new UserDetailsValuesModel(
                strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMPassword,
                strCIFBDMMObileNo);
    }

    /*public void setApplicationMenu(final Activity activity, String title) {
        final ImageButton imagtbt_option = activity
                .findViewById(R.id.imagtbt_option);

        TextView tv_title_common = activity
                .findViewById(R.id.tv_commonTitle);
        tv_title_common.setText(title);

        final String userNameSaved = getUserName(activity);
        System.out.println(" :userNameSaved:" + userNameSaved);

        System.out.println("activity:Comm:" + activity.getCallingPackage());
        System.out.println("activity.getClass:Comm:" + activity.getClass());

        imagtbt_option.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onApplicationMenu(imagtbt_option, userNameSaved, activity);

            }
        });
    }*/

    public void onApplicationMenu(View v, String username, AppCompatActivity activity) {
        CreateMenuDialog objMenu = new CreateMenuDialog(username, activity);
        objMenu.createMenu();
    }

    public void setActionbarLayout(final AppCompatActivity activity) {
        /*activity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                R.layout.window_title);
        setApplicationMenu(activity, "Smart Advisor");*/
        setApplicationToolbarMenu(activity, "Smart Advisor");
    }

    public boolean emailPatternValidation(EditText et_email, Context context) {
        boolean emailpattern = true;

        String emailval = et_email.getText().toString();

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailval).matches()) {
            et_email.setError("Invalid email-id");
            et_email.requestFocus();
            emailpattern = false;
        } else {
            emailpattern = true;
        }
        return emailpattern;
    }

    public boolean edittextLengthValidation(EditText editText, String erromessage, int length, int otherLength) {
        boolean isLengthWrong = true;

        String edittextValue = editText.getText().toString();

        if (otherLength == 0) {
            if (edittextValue.length() < length) {
                editText.setError(erromessage);
                editText.requestFocus();
                isLengthWrong = false;
            } else {
                isLengthWrong = true;
            }
        } else {
            if (edittextValue.length() > otherLength) {
                editText.setError(erromessage);
                editText.requestFocus();
                isLengthWrong = false;
            } else {
                isLengthWrong = true;
            }
        }

        return isLengthWrong;
    }

    public boolean mobileNumberPatternValidation(EditText etmobileNumber,
                                                 Context context) {
        boolean mobilePattern = true;

        String mobileNumberVal = etmobileNumber.getText().toString();

        /*if(!android.util.Patterns.PHONE.matcher(mobileNumberVal) .matches()){
            etmobileNumber.setError("Invalid mobile number");
            etmobileNumber.requestFocus();
            mobilePattern = false;
        } else {
            mobilePattern = true ;
        }*/
        if (mobileNumberVal.length() < 10) {
            etmobileNumber.setError("Invalid mobile number");
            etmobileNumber.requestFocus();
            mobilePattern = false;
        } else {
            mobilePattern = true;
        }
        return mobilePattern;
    }

    public boolean PINPatternValidation(EditText etPIN, Context context) {
        boolean mobilePattern = true;

        String mobileNumberVal = etPIN.getText().toString();

        if (mobileNumberVal.length() < 4) {
            etPIN.setError("Invalid PIN");
            etPIN.requestFocus();
            mobilePattern = false;
        } else {
            mobilePattern = true;
        }
        return mobilePattern;
    }

    private String getUserName(AppCompatActivity context) {
        String userName = "";
        try {
            userName = new DatabaseHelper(context).GetUserName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userName;
    }

    public String getUserName(Context context) {
        String userName = "";
        try {
            userName = new DatabaseHelper(context).GetUserName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userName;
    }

    @SuppressWarnings("resource")
    public byte[] read(File file) {
        byte[] bytes = null;

        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int bytesRead;
            while ((bytesRead = fin.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }

            bytes = bos.toByteArray();

            bos.flush();
            bos.close();

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return null;
        }
        return bytes;
    }
    // public final String JPEG_FILE_SUFFIX = ".jpg";
    // private static final String JPEG_FILE_PREFIX = "IMG_";

    public void hideKeyboard(View view, Context context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void logoutToLoginActivity(Context context) {
        if (!GetUserType(context).contentEquals("AGENT")) {
            new MediaPlayer();
            MediaPlayer mp1 = MediaPlayer.create(context, R.raw.bye);
            mp1.start();
        }

        Intent intent = new Intent(context, LoginUserActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        ((AppCompatActivity) context).finish();
    }

    public void windowmessageProposersgin(final Context context,
                                          final String signName) {

        final Dialog d = new Dialog(context);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));

        d.setContentView(R.layout.window_message_signature);
        final Button btn_save = d.findViewById(R.id.save);
        final Button btn_cancel = d.findViewById(R.id.cancel);

        Button btn_takeSign = d.findViewById(R.id.takesignature);

        btn_takeSign.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                btn_save.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(context,
                        ProposerCaptureSignature.class);
                intent.putExtra("uniqueId", signName);
                ((AppCompatActivity) context).startActivityForResult(intent,
                        SIGNATURE_ACTIVITY);
                d.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                d.dismiss();
            }
        });
        d.show();

    }

    public void windowMessageSign(final Context context,
                                  final String signName) {

        final Dialog d = new Dialog(context);
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
                Intent intent = new Intent(context,
                        CaptureSignature.class);
                ((AppCompatActivity) context).startActivityForResult(intent, SIGNATURE_ACTIVITY);
                d.dismiss();

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

    public void windowmessage(final Context context,
                              final String custPhotoTypeName) {

        contextPhoto = context;

        // d = new Dialog(context);
        final Dialog d = new Dialog(context);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));

        d.setContentView(R.layout.window_message_proposer_photo);
        final Button btn_cancel = d.findViewById(R.id.cancel_photo);

        // Button btn_uploadphoto = (Button) d.findViewById(R.id.uploadphoto);
        Button btn_takephoto = d.findViewById(R.id.takephoto);

        btn_takephoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        OnClickListener OnClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
                dispatchTakePictureIntent(context, custPhotoTypeName);

                final Toast toast = Toast
                        .makeText(context,
                                "Capture Customer's Real time Photo",
                                Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 105, 50);
                toast.show();

            }
        };
        setBtnListenerOrDisable(btn_takephoto, OnClickListener,
                MediaStore.ACTION_IMAGE_CAPTURE);

        btn_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                d.dismiss();
            }
        });
        d.show();

    }

    private void setBtnListenerOrDisable(Button btn,
                                         OnClickListener mTakePicOnClickListener, String intentName) {
        if (isIntentAvailable(contextPhoto, intentName)) {
            btn.setOnClickListener(mTakePicOnClickListener);
        } else {
            btn.setTag(contextPhoto.getText(R.string.cannot).toString() + " "
                    + btn.getContext());
            btn.setClickable(false);
        }
    }

    private void dispatchTakePictureIntent(Context context, String custPhotoTypeName) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = null;

        try {
            f = setUpPhotoFile(context, custPhotoTypeName);
            mCurrentPhotoPath = f.getPath();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getContentUri(context, f));
            } else {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int REQUEST_CODE_PICK_PHOTO_FILE = 3;
        ((AppCompatActivity) context).startActivityForResult(takePictureIntent,
                REQUEST_CODE_PICK_PHOTO_FILE);
    }


    private File setUpPhotoFile(Context mContext, String custPhotoTypeName) throws IOException {

        f = createImageFile(mContext, custPhotoTypeName);
        mCurrentPhotoPath = f.getPath();
        return f;
    }

    private File createImageFile(Context mContext, String custPhotoTypeName) {
        // 1600008523_Cust1Photo

        if (NeedAnalysisActivity.URN_NO != null
                && NeedAnalysisActivity.URN_NO != "") {
            custPhotoName = NeedAnalysisActivity.URN_NO + custPhotoTypeName;
        }

        // String imageFileName = JPEG_FILE_PREFIX + QuatationNumber + "_X0" +
        // "_";

        // imageF = File.createTempFile(custPhotoName, JPEG_FILE_SUFFIX,
        // folder);

        // f = new File(f, "1k768443_P01" + "." + "pdf");
        // f = new File(f, ProposerNumber + "_X06" + "." + "pdf");

        File imageF = new StorageUtils().createFileToAppSpecificDir(mContext, custPhotoName);

        return imageF;
    }

    public File galleryAddPic(Context con) {
        Intent mediaScanIntent = new Intent(
                "android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        f = new File(mCurrentPhotoPath);

        Intent imageIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);

        /*File folder = new File(CommonMethods.EXTERNAL_STORAGE_DIRECTORY + CommonMethods.DIRECT_DIRECTORY);

        if (!folder.exists()) {
            folder.mkdirs();
        }*/

        // File image = new File(folder, custPhotoName);

        //nought changes
        Uri contentUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, getContentUri(con, f));

            contentUri = getContentUri(con, f);
        } else {
            imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

            contentUri = Uri.fromFile(f);
        }

        mediaScanIntent.setData(contentUri);
        contextPhoto.sendBroadcast(mediaScanIntent);
        return f;
    }

    @SuppressWarnings("deprecation")
    public Bitmap compressImage(File filePath, String URN_NO,
                                String custPhotoTypeName, Context context) {

        // String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

        // by setting this field as true, the actual bitmap pixels are not
        // loaded in the memory. Just the bounds are loaded. If
        // you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath.getPath(),
                options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        // max Height and width values of the compressed image is taken as
        // 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        // width and height values are set maintaining the aspect ratio of the
        // image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        // setting inSampleSize value allows to load a scaled down version of
        // the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth,
                actualHeight);

        // inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

        // this options allow android to claim the bitmap memory if it runs low
        // on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            // load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath.getPath(), options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
                    Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
                middleY - bmp.getHeight() / 2, new Paint(
                        Paint.FILTER_BITMAP_FLAG));

        // check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath.getPath());

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        File toWriteFile = new StorageUtils().createFileToAppSpecificDir(context, URN_NO + custPhotoTypeName);
        try {
            out = new FileOutputStream(toWriteFile.getPath());

            // write the compressed bitmap at the destination specified by
            // filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return scaledBitmap;

    }

    private int calculateInSampleSize(BitmapFactory.Options options,
                                      int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    /*public File getDirectory() {

        // External sdcard location
        File mediaStorageDir = new File(EXTERNAL_STORAGE_DIRECTORY + DIRECT_DIRECTORY);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                printLog("Create Directory : ", "oops Failed create");
            }
        }

        return mediaStorageDir;
    }*/

    @SuppressWarnings("deprecation")
    /*public void CopyAssets(String FileName, String extension, Context context) {

        AssetManager assetManager = context.getAssets();

        InputStream in = null;
        OutputStream out = null;
        //File file = new File(context.getFilesDir(), FileName);
        File file = new StorageUtils().createFileToAppSpecificDir(context, FileName);
        try {
            in = assetManager.open(FileName);
            //nought changes
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                out = context.openFileOutput(file.getName(), Context.MODE_PRIVATE);
            } else {
                out = context.openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);
            }
            // System.out.println("name:"+file.getName()+" dir:"+file.getPath());

            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

        try {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri path = null;
            //nought changes
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                path = getContentUri(context, file);
                intent.setData(path);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                path = Uri.fromFile(file);
                intent.setData(path);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            }

            Intent chooser = Intent.createChooser(intent, "Open With");
            // Verify the original intent will resolve to at least one activity
            if (chooser.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(chooser);
            } else {
                printLog("File Error : ", "Unable to open File");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/

    /*public void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }*/

    /*public boolean copyFileServiceRequst(InputStream in, OutputStream out) {
        try {
//            in = in = context.getContentResolver().openInputStream(sourceFile);;
//            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }*/

    /*public boolean copyFile(Context context, Uri sourceFile, File destFile) throws IOException {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = in = context.getContentResolver().openInputStream(sourceFile);
            ;
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            in.close();
            out.close();
        }
        return false;
    }*/

    /*public File createFile(String intial, String type, String memType) throws IOException {

        String userImagePath = intial + "_" + type + "_01" + memType;
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(),
                DIRECT_DIRECTORY);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                //printLog("Create Directory : ", "oops Failed create");
            }
        }

        File mCaptureFile = new File(mediaStorageDir.getPath() + File.separator + userImagePath);
        mCaptureFile.createNewFile();
        return mCaptureFile;
    }*/

    public void openPDFAction(final File f, Context context) {
        Intent target = new Intent(Intent.ACTION_VIEW);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            target.setDataAndType(getContentUri(context, f), "application/pdf");
            target.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            target.setDataAndType(Uri.fromFile(f), "application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        }

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
            printLog("File Error : ", e.getMessage());
        }
    }

    public void openWebLink(Context mContext, String strURL) {
        if (isNetworkConnected(mContext)) {

            String title = "Open with";
            Intent myWebLink = new Intent(
                    Intent.ACTION_VIEW);
            myWebLink.setData(Uri.parse(strURL));

            Intent chooser = Intent.createChooser(myWebLink, title);
            // Verify the original intent will resolve to at least one
            // activity
            //if (myWebLink.resolveActivity(mContext.getPackageManager()) != null) {
            myWebLink.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(chooser);
            //}
        } else {
            showMessageDialog(mContext, NO_INTERNET_MESSAGE);
        }
    }

    public void openAllDocs(Context context, File mFile) throws IOException {

        if (mFile.exists()) {

            try {

                Intent target = new Intent(Intent.ACTION_VIEW);
                Uri mUri;

                // Check what kind of file you are trying to open, by comparing the url with extensions.
                // When the if condition is matched, plugin sets the correct intent (mime) type,
                // so Android knew what application to use to open the file
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mUri = getContentUri(context, mFile);
                    target.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    mUri = Uri.fromFile(mFile);
                    target.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }

                if (mFile.toString().contains(".doc") || mFile.toString().contains(".docx")) {
                    // Word document
                    target.setDataAndType(mUri, "application/msword");
                } else if (mFile.toString().contains(".pdf")) {
                    // PDF file
                    target.setDataAndType(mUri, "application/pdf");
                } else if (mFile.toString().contains(".ppt") || mFile.toString().contains(".pptx")) {
                    // Powerpoint file
                    target.setDataAndType(mUri, "application/vnd.ms-powerpoint");
                } else if (mFile.toString().contains(".xls") || mFile.toString().contains(".xlsx")) {
                    // Excel file
                    target.setDataAndType(mUri, "application/vnd.ms-excel");
                } else if (mFile.toString().contains(".zip") || mFile.toString().contains(".rar")) {
                    // WAV audio file
                    target.setDataAndType(mUri, "application/x-wav");
                } else if (mFile.toString().contains(".rtf")) {
                    // RTF file
                    target.setDataAndType(mUri, "application/rtf");
                } else if (mFile.toString().contains(".wav") || mFile.toString().contains(".mp3")) {
                    // WAV audio file
                    target.setDataAndType(mUri, "audio/x-wav");
                } else if (mFile.toString().contains(".gif")) {
                    // GIF file
                    target.setDataAndType(mUri, "image/gif");
                } else if (mFile.toString().contains(".jpg") || mFile.toString().contains(".jpeg") || mFile.toString().contains(".png")) {
                    // JPG file
                    target.setDataAndType(mUri, "image/jpeg");
                } else if (mFile.toString().contains(".txt")) {
                    // Text file
                    target.setDataAndType(mUri, "text/plain");
                } else if (mFile.toString().contains(".3gp") || mFile.toString().contains(".mpg") || mFile.toString().contains(".mpeg") || mFile.toString().contains(".mpe") || mFile.toString().contains(".mp4") || mFile.toString().contains(".avi")) {
                    // Video files
                    target.setDataAndType(mUri, "video/*");
                } else {
                    //if you want you can also define the intent type for any other file

                    //additionally use else clause below, to manage other unknown extensions
                    //in this case, Android will show all applications installed on the device
                    //so you can choose which application to use
                    target.setDataAndType(mUri, "*/*");
                }

                //choose action
                Intent intent = Intent.createChooser(target, "Open File");

                // validate that the device can open your File!
                PackageManager pm = context.getPackageManager();
                if (intent.resolveActivity(pm) != null) {
                    context.startActivity(intent);
                }
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
                printLog("File Error : ", e.getMessage());
            }
        } else {
            showToast(context, "file does not exists");
        }
    }

    public Session createSessionObject() {
        Properties properties = new Properties();
        // properties.put("mail.smtp.auth", "true");
        // properties.put("mail.smtp.starttls.enable", "true");
        // properties.put("mail.smtp.host", "smtp.sbi-life.com");
        // properties.put("mail.smtp.port", "25");

        properties.put("mail.smtp.host", "webmail.sbi-life.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        "sbilconnectlife@sbi-life.com", "sky@12345");
            }
        });
    }

    public String getUserTypeSelected(String userType) {


        if (userType.equalsIgnoreCase("CIF(Banca)")) {
            userType = "CIF";
        } else if (userType.equalsIgnoreCase("AGENT(Retail)")) {
            userType = "AGENT";
        } else if (userType.equalsIgnoreCase("BDM(Banca)")) {
            userType = "BDM";
        } else if (userType.equalsIgnoreCase("UM(Retail)")) {
            userType = "UM";
        } else if (userType.equalsIgnoreCase("BSM(Retail)")) {
            userType = "BSM";
        } else if (userType.equalsIgnoreCase("DSM(Retail)")) {
            userType = "DSM";
        } else if (userType.equalsIgnoreCase("AM(Banca)")) {
            userType = "AM";
        } else if (userType.equalsIgnoreCase("SAM(Banca)")) {
            userType = "SAM";
        } else if (userType.equalsIgnoreCase("ZAM(Banca)")) {
            userType = "ZAM";
        } else if (userType.equalsIgnoreCase("ASM(Retail)")) {
            userType = "ASM";
        } else if (userType.equalsIgnoreCase("RSM(Retail)")) {
            userType = "RSM";
        }

        return userType;
    }

    public void fillSpinnerValue(Context con, Spinner spinner, List<String> value_list) {

        Element_TextView_BaseAdapter retd_adapter = new Element_TextView_BaseAdapter(
                con, value_list);
        spinner.setAdapter(retd_adapter);

    }

    public boolean valPancard(String s, EditText editText) {
        // String s = "ABCDE1234F"; // get your editext value here
        Pattern pattern = Pattern
                .compile("[A-Z]{3}[P]{1}[A-Z]{1}[0-9]{4}[A-Z]{1}");

        Matcher matcher = pattern.matcher(s);
        // Check if pattern matches
        if (s == null || s.equals("")) {
            editText.setError("Please enter a valid Pan Card Number");
            return false;

        } else if (!(matcher.matches())) {
            editText.setError("Please enter a valid Pan Card Number");
            return false;

        } else if (matcher.matches()) {
            editText.setError(null);
            return true;
        }
        return false;
    }

    public Bitmap ShrinkBitmap(String file, int width, int height) {

        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
                / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
                / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }

    public String getPreviousMonthDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        int mDay = cal.get(Calendar.DAY_OF_MONTH);

        String y = String.valueOf(mYear);
        String m = String.valueOf(mMonth + 1);
        String da = String.valueOf(mDay);

        m = getFullMonthName(m);
        String totaldate = da + "-" + m + "-" + y;
        return totaldate;
    }

    public String getCurrentMonthDate() {
        Calendar calenderToDate = Calendar.getInstance();

        int mYeareCPT = calenderToDate.get(Calendar.YEAR);
        int mMontheCPT = calenderToDate.get(Calendar.MONTH);
        int mDayeCPT = calenderToDate.get(Calendar.DAY_OF_MONTH);

        String yeCPT = String.valueOf(mYeareCPT);
        String meCPT = String.valueOf(mMontheCPT + 1);
        String daeCPT = String.valueOf(mDayeCPT);

        String todate = daeCPT + "-" + getFullMonthName(meCPT) + "-" + yeCPT;
        return todate;
    }

    public String getNextMonthDate() {
        Calendar calenderToDate = Calendar.getInstance();
        calenderToDate.add(Calendar.MONTH, 1);
        int mYeareCPT = calenderToDate.get(Calendar.YEAR);
        int mMontheCPT = calenderToDate.get(Calendar.MONTH);
        int mDayeCPT = calenderToDate.get(Calendar.DAY_OF_MONTH);

        String yeCPT = String.valueOf(mYeareCPT);
        String meCPT = String.valueOf(mMontheCPT + 1);
        String daeCPT = String.valueOf(mDayeCPT);

        String todate = daeCPT + "-" + getFullMonthName(meCPT) + "-" + yeCPT;
        return todate;
    }

    public String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    /*public File createCaptureCIFImages(String strCIFFileName) throws IOException {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                CommonMethods.DIRECT_DIRECTORY_CIF);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                printLog("Create Directory : ", "oops Failed create");
            }
        }

        File mCaptureFile = new File(mediaStorageDir.getPath() + File.separator + strCIFFileName);

        return mCaptureFile;
    }*/

    //use this only for to store images
    /*public File createCaptureImg(String strFileName) throws IOException {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                CommonMethods.DIRECT_DIRECTORY);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                printLog("Create Directory : ", "oops Failed create");
            }
        }

        File mCaptureFile = new File(mediaStorageDir.getPath() + File.separator + strFileName);

        return mCaptureFile;
    }*/

    public Uri getContentUri(Context mContetCon, File f) {
        Uri captureUri = FileProvider.getUriForFile(mContetCon,
                mContetCon.getString(R.string.file_provider_authority), f);
        return captureUri;
    }


    // Used To Change date From dd-mm-yyyy to mm-dd-yyyy

    public void loadDriveURL(String url, Context context) {
        try {
            Intent myWebLink = new Intent(Intent.ACTION_VIEW);
            String title = "Open with";
            myWebLink.setData(Uri.parse(url));

            Intent chooser = Intent.createChooser(myWebLink, title);
            // Verify the original intent will resolve to at least one activity
            if (myWebLink.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(chooser);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Used To Change date From mm-dd-yyyy to dd-mm-yyyy
    public String getDDMMYYYYDate(String OldDate) {
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

    public String getMMDDYYYYDatabaseDate(String OldDate) {
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

    public int getIndex(Spinner s1, String value) {

        int index = 0;

        for (int i = 0; i < s1.getCount(); i++) {
            if (s1.getItemAtPosition(i).equals(value)) {
                index = i;
            }
        }
        return index;
    }

    // method to set a focusable a element
    public void setFocusable(View v) {

        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
    }

    // method to set a clearing a element
    public void clearFocusable(View v) {
        v.setFocusable(false);
        v.setFocusableInTouchMode(false);
    }

	/*public void sendSms(Context context, String mobileNumber, String body) {

        try {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + mobileNumber));
			intent.putExtra("sms_body", body);
                context.startActivity(intent);
		} catch (SecurityException e) {
			showToast(context, "There might be issue in Permission");
        } catch (Exception e) {
			showMessageDialog(context, "Problem in sending SMS, please Try Again");
        }
    }*/

    public void showPDFFile(Context context, File file) {
        try {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri path = null;
            //nought changes
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                path = getContentUri(context, file);
                intent.setDataAndType(path, "application/pdf");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            } else {
                path = Uri.fromFile(file);
                intent.setDataAndType(path, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }

            // validate that the device can open your File!
            PackageManager pm = context.getPackageManager();
            if (intent.resolveActivity(pm) != null) {
                context.startActivity(intent);
            } else {
                showToast(context, "Unable to open File");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showToast(context, "Unable to open File");
        }
    }

    public void callMobileNumber(final String mobileNumber, final Context context) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window_twobutton);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Do you want to make the call.");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL,
                            Uri.parse("tel:" + mobileNumber));

					/*Intent intent = new Intent(Intent.ACTION_CALL,
							Uri.parse("tel:" + mobileNumber));*/
                    context.startActivity(intent);
                } catch (SecurityException e) {
                    showToast(context, "There might be issue in Permission");
                } catch (Exception e) {
                    showMessageDialog(context, "Problem in calling please Try Again");
                }
            }
        });
        Button dialogButtoncancel = dialog.findViewById(R.id.btnalertcancel);
        dialogButtoncancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public String getformatedThousandString(int number) {
        return NumberFormat.getNumberInstance(Locale.US)
                .format(number);
    }

    public String getformatedThousandString(double number) {
        String formatedstring = NumberFormat.getNumberInstance(Locale.US)
                .format(number);
        return formatedstring;
    }

    public void callActivity(Context context, Class aClass) {
        Intent i = new Intent(context, aClass);
        context.startActivity(i);
    }

    public void callActivityWithHomeTagYes(Context context, Class aClass) {
        Intent i = new Intent(context, aClass);
        i.putExtra("fromHome", "Y");
        context.startActivity(i);
    }

    public void callActivityWithTagM(Context context, Class aClass) {
        Intent i = new Intent(context, aClass);
        i.putExtra("fromHome", "M");
        context.startActivity(i);
    }

    public void commonNotification(Context context, PendingIntent pendingIntent, String title, String message, int notificationId) {
        try {
            final String NOTIFICATION_CHANNEL_ID = "10003";

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.icon_small_notification)
                    .setColor(Color.RED)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            // Sets an ID for the notification

            // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                assert mNotifyMgr != null;
                notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                mNotifyMgr.createNotificationChannel(notificationChannel);
            }
            assert mNotifyMgr != null;
            // Builds the notification and issues it.
            mNotifyMgr.notify(notificationId, notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int calculateMyAge(int year, int month, int day, String strBirthdate) {
        Calendar nowCal = new GregorianCalendar(year, month, day);

        String[] ProposerDob = strBirthdate.split("-");

        int age = nowCal.get(Calendar.YEAR) - Integer.parseInt(ProposerDob[2]);

        boolean isMonthGreater = Integer.parseInt(ProposerDob[1]) > nowCal.get(Calendar.MONTH);

        boolean isMonthSameButDayGreater = Integer.parseInt(ProposerDob[1]) == nowCal
                .get(Calendar.MONTH)
                && Integer.parseInt(ProposerDob[0]) > nowCal
                .get(Calendar.DAY_OF_MONTH);

        if (isMonthGreater || isMonthSameButDayGreater) {
            age = age - 1;
        }
        return age;
    }

    /* ---- get content path  starts new changes 26-09-2019----- */

    public String getCurrentDateMM_DD_YYYY() {
        Calendar calender = Calendar.getInstance();

        String mont = ((calender.get(Calendar.MONTH) + 1) < 10 ? "0" : "") + (calender.get(Calendar.MONTH) + 1);
        String day = (calender.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "") + calender.get(Calendar.DAY_OF_MONTH);

        return mont + "-" + day + "-" + calender.get(Calendar.YEAR);
    }

    public void setKerlaDiscount(Context context, TableRow tablerowKerlaDiscount, CheckBox cb_kerladisc) {


        String kerlaDiscountDetails = AppSharedPreferences.getData(context, (new CommonMethods().getKerlaDiscount()), "");
        tablerowKerlaDiscount.setVisibility(View.GONE);

        /*if (!TextUtils.isEmpty(kerlaDiscountDetails)) {
            String[] split = kerlaDiscountDetails.split(",");
            String stateId = split[1];

            if (!TextUtils.isEmpty(stateId) && stateId.equals("15")) {
                tablerowKerlaDiscount.setVisibility(View.VISIBLE);
            }
        }*/

    }

    /*public String getFilePath(Uri selectedImage, Context mContext) {
        if (!selectedImage.toString().contains("com.google.android")) {
            Log.e("uri", "File Uri: " + selectedImage.toString());
            // Get the path

            if (selectedImage.toString().contains("google")) {
                String path = getUriRealPath(mContext, selectedImage);
                Log.e("path", "File Path: " + path);

                return path;
            } else {
                String path = "";
                try {
                    path = getFilePathFromURI(mContext, selectedImage);

                    return path;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.e("path", "File Path: " + path);
                assert path != null;

                try {
                    try {
                        String file_name = path.substring(path.lastIndexOf('/') + 1);
                        int num = Integer.parseInt(file_name);

                    } catch (Exception e) {
                        e.printStackTrace();
                                *//*if (!path.contains(".pdf") && !path.contains(".PDF")) {
                                    Toast.makeText(this, getResources().getString(R.string.str_only_files) +
                                            "", Toast.LENGTH_SHORT).show();
                                } else {

                                    String file_name = path.substring(path.lastIndexOf('/') + 1);
                                    ModelImage modelImage = new ModelImage();
                                    modelImage.setImage_Name("DOC" + file_name);
                                    modelImage.setImage_Path(path);
                                    modelImage.setImage_Type("4");

                                    list_attachment.add(modelImage);
                                    Log.e("image is", modelImage.getImage_Path());
                                    show_list();
                                }*//*
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            printLog("DOC Type :", "google doc..");
        }
        return "";
    }*/

    /*private String getUriRealPath(Context ctx, Uri uri) {
        String ret = "";

        if (isAboveKitKat()) {
            // Android OS above sdk version 19.
            ret = getUriRealPathAboveKitkat(ctx, uri);
        } else {
            // Android OS below sdk version 19
            ret = getImageRealPath(ctx.getContentResolver(), uri, null);
        }

        return ret;
    }*/

    @TargetApi(19)
    /*private String getUriRealPathAboveKitkat(Context ctx, Uri uri) {
        String ret = "";

        if (ctx != null && uri != null) {

            if (isContentUri(uri)) {
                if (isGooglePhotoDoc(uri.getAuthority())) {
                    ret = uri.getLastPathSegment();
                } else {
                    ret = getImageRealPath(ctx.getContentResolver(), uri, null);
                }
            } else if (isFileUri(uri)) {
                ret = uri.getPath();
            } else if (isDocumentUri(ctx, uri)) {

                // Get uri related document id.
                String documentId = DocumentsContract.getDocumentId(uri);

                // Get uri authority.
                String uriAuthority = uri.getAuthority();

                if (isMediaDoc(uriAuthority)) {
                    String idArr[] = documentId.split(":");
                    if (idArr.length == 2) {
                        // First item is document type.
                        String docType = idArr[0];

                        // Second item is document real id.
                        String realDocId = idArr[1];

                        // Get content uri by document type.
                        Uri mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        if ("image".equals(docType)) {
                            mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        } else if ("video".equals(docType)) {
                            mediaContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        } else if ("audio".equals(docType)) {
                            mediaContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }

                        // Get where clause with real document id.
                        String whereClause = MediaStore.Images.Media._ID + " = " + realDocId;

                        ret = getImageRealPath(ctx.getContentResolver(), mediaContentUri, whereClause);
                    }

                } else if (isDownloadDoc(uriAuthority)) {
                    // Build download uri.
                    Uri downloadUri = Uri.parse("content://downloads/public_downloads");

                    // Append download document id at uri end.
                    Uri downloadUriAppendId = ContentUris.withAppendedId(downloadUri, Long.valueOf(documentId));

                    ret = getImageRealPath(ctx.getContentResolver(), downloadUriAppendId, null);

                } else if (isExternalStoreDoc(uriAuthority)) {
                    String idArr[] = documentId.split(":");
                    if (idArr.length == 2) {
                        String type = idArr[0];
                        String realDocId = idArr[1];

                        if ("primary".equalsIgnoreCase(type)) {
                            ret = Environment.getExternalStorageDirectory() + "/" + realDocId;
                        }
                    }
                }
            }
        }

        return ret;
    }*/

    /* Check whether current android os version is bigger than kitkat or not. */
    private boolean isAboveKitKat() {
        boolean ret = false;
        ret = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        return ret;
    }

    /* Check whether this uri represent a document or not. */
    @TargetApi(19)
    private boolean isDocumentUri(Context ctx, Uri uri) {
        boolean ret = false;
        if (ctx != null && uri != null) {
            ret = DocumentsContract.isDocumentUri(ctx, uri);
        }
        return ret;
    }

    /* Check whether this uri is a content uri or not.
     *  content uri like content://media/external/images/media/1302716
     *  */
    private boolean isContentUri(Uri uri) {
        boolean ret = false;
        if (uri != null) {
            String uriSchema = uri.getScheme();
            if ("content".equalsIgnoreCase(uriSchema)) {
                ret = true;
            }
        }
        return ret;
    }

    /* Check whether this uri is a file uri or not.
     *  file uri like file:///storage/41B7-12F1/DCIM/Camera/IMG_20180211_095139.jpg
     * */
    private boolean isFileUri(Uri uri) {
        boolean ret = false;
        if (uri != null) {
            String uriSchema = uri.getScheme();
            if ("file".equalsIgnoreCase(uriSchema)) {
                ret = true;
            }
        }
        return ret;
    }

    /* Check whether this document is provided by ExternalStorageProvider. */
    private boolean isExternalStoreDoc(String uriAuthority) {
        boolean ret = false;

        if ("com.android.externalstorage.documents".equals(uriAuthority)) {
            ret = true;
        }

        return ret;
    }

    /* Check whether this document is provided by DownloadsProvider. */
    private boolean isDownloadDoc(String uriAuthority) {
        boolean ret = false;

        if ("com.android.providers.downloads.documents".equals(uriAuthority)) {
            ret = true;
        }

        return ret;
    }

    /* Check whether this document is provided by MediaProvider. */
    private boolean isMediaDoc(String uriAuthority) {
        boolean ret = false;

        if ("com.android.providers.media.documents".equals(uriAuthority)) {
            ret = true;
        }

        return ret;
    }

    /* Check whether this document is provided by google photos. */
    private boolean isGooglePhotoDoc(String uriAuthority) {
        boolean ret = false;

        if ("com.google.android.apps.photos.content".equals(uriAuthority)) {
            ret = true;
        }

        return ret;
    }

    /* Return uri represented document file real local path.*/
    private String getImageRealPath(ContentResolver contentResolver, Uri uri, String whereClause) {
        String ret = "";

        // Query the uri with condition.
        Cursor cursor = contentResolver.query(uri, null, whereClause, null, null);

        if (cursor != null) {
            boolean moveToFirst = cursor.moveToFirst();
            if (moveToFirst) {

                // Get columns name by uri type.
                String columnName = MediaStore.Images.Media.DATA;

                if (uri == MediaStore.Images.Media.EXTERNAL_CONTENT_URI) {
                    columnName = MediaStore.Images.Media.DATA;
                } else if (uri == MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) {
                    columnName = MediaStore.Audio.Media.DATA;
                } else if (uri == MediaStore.Video.Media.EXTERNAL_CONTENT_URI) {
                    columnName = MediaStore.Video.Media.DATA;
                }


                // Get column index.
                int imageColumnIndex = cursor.getColumnIndex(columnName);

                // Get column value which is the uri related file local path.
                ret = cursor.getString(imageColumnIndex);
            }
        }

        return ret;
    }

    public Object[] getContentURIDetails(Context mContext, Uri selectedImage) {

        String str_extension = "";

        /*Get the file's content URI from the incoming Intent, then
                    get the file's MIME type*/

        String mimeType = mContext.getContentResolver().getType(selectedImage);
        if (mimeType.equals("application/pdf"))
            str_extension = ".pdf";
        else if (mimeType.equals("image/jpeg") || mimeType.equals("image/jpg"))
            str_extension = ".jpg";
        else if (mimeType.equals("image/png"))
            str_extension = ".png";
        else if (mimeType.equals("image/tiff"))
            str_extension = ".tif";

        /*
         * Get the file's content URI from the incoming Intent,
         * then query the server app to get the file's display name
         * and size.
         */
        Cursor returnCursor = mContext.getContentResolver().query(selectedImage, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         * move to the first row in the Cursor, get the data,
         * and display it.
         */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String fileName = returnCursor.getString(nameIndex);
        double kilobyte = returnCursor.getLong(sizeIndex) / 1024;

        return new Object[]{str_extension, fileName, kilobyte};
    }

    public String getContentURIDetailsServiceRequest(Context mContext, Uri selectedImage) {
        String str_extension = "";

        /*Get the file's content URI from the incoming Intent, then
                    get the file's MIME type*/

        String mimeType = mContext.getContentResolver().getType(selectedImage);
        if (mimeType.equals("application/pdf"))
            str_extension = ".pdf";
        else if (mimeType.equals("image/jpeg") || mimeType.equals("image/jpg"))
            str_extension = ".jpg";
        else if (mimeType.equals("image/png"))
            str_extension = ".png";
        else if (mimeType.equals("image/tiff"))
            str_extension = ".tif";

        return str_extension;
    }

    /* ---- get content path  end----- */
    //strEmailId, string strMobileNo, string strAuthKey
    public void appendSecurityParams(Context context, SoapObject request, String emailID, String mobileNumber) {

        if (TextUtils.isEmpty(emailID)) {
            emailID = GetUserEmail(context);
        }

        if (TextUtils.isEmpty(mobileNumber)) {
            mobileNumber = GetUserMobile(context);
        }
        request.addProperty("strEmailId", emailID);
        request.addProperty("strMobileNo", mobileNumber);
        request.addProperty("strAuthKey", strAuth);

    }

    public boolean validateSpecailChar(String strVal) {
        return strVal.matches("[a-zA-Z0-9]*");

        //return  strVal.matches("[a-zA-Z0-9.? ]*");
    }

    public boolean isThisDateValid(String dateToValidate) {

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

    public String[] Split_EKYC(String text, int chunkSize, int maxLength) {
        char[] data = text.toCharArray();
        int len = Math.min(data.length, maxLength);
        String[] result = new String[(len + chunkSize - 1) / chunkSize];
        int linha = 0;
        for (int i = 0; i < len; i += chunkSize) {
            result[linha] = new String(data, i, Math.min(chunkSize, len - i));
            linha++;
        }
        return result;
    }

    public void openDialogToRedirectActivity(final Context context, final Intent intent, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_yes_no);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        Button bt_yes = dialog.findViewById(R.id.bt_yes);
        Button bt_no = dialog.findViewById(R.id.bt_no);
        ((TextView) dialog.findViewById(R.id.tv_title))
                .setText(message);
        bt_yes.setText("Yes");
        bt_no.setText("No");
        bt_yes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                if (intent != null) {
                    context.startActivity(intent);
                }
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

    public String getAgentDeclaration(Context context) {
        String agentDeclaration = "I, " + getUserName(context)
                + " have explained the premiums and benefits under the product fully to the prospect/policyholder.";
        return agentDeclaration;
    }

    /*add location details to bitmap starts*/
    public String getCompleteAddressString(Context mContext, double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

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

    public String getCurrentTimeStamp() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public Bitmap mergeBitmap(Bitmap c, Bitmap s) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom
        try {
            Bitmap cs = null;

            int width, height = 0;

            width = c.getWidth();
            height = c.getHeight() + s.getHeight();

        /*if(c.getWidth() > s.getWidth()) {
            width = c.getWidth() + s.getWidth();
            height = c.getHeight();
        } else {
            width = s.getWidth() + s.getWidth();
            height = c.getHeight();
        }*/

            cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            Canvas comboImage = new Canvas(cs);

            comboImage.drawBitmap(c, 0f, 0f, null);
            comboImage.drawBitmap(s, 0f, c.getHeight(), null);

            // this is an extra bit I added, just incase you want to save the new image somewhere and then return the location
    /*String tmpImg = String.valueOf(System.currentTimeMillis()) + ".png";

    OutputStream os = null;
    try {
      os = new FileOutputStream(loc + tmpImg);
      cs.compress(CompressFormat.PNG, 100, os);
    } catch(IOException e) {
      Log.e("combineImages", "problem combining images", e);
    }*/

            return cs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap convertStringToBitMap(Context context, String name) {

        try {

            int noOfLines = 0;
            for (String line : name.split("\n")) {
                noOfLines++;
            }

            // Create bitmap
            Bitmap bitmap = Bitmap.createBitmap(600, noOfLines * 17, Bitmap.Config.ARGB_8888);
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(context.getResources().getColor(R.color.transparent_tabMedium));

            // new antialised Paint
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            // text color - #3D3D3D
            paint.setColor(context.getResources().getColor(R.color.WHITE));
            // text size in pixels
            paint.setTextSize(15);
            // draw text to the Canvas center
            Rect bounds = new Rect();
            paint.getTextBounds(name, 0, name.length(), bounds);

            int left = 0;
            int top = (bitmap.getHeight() - bounds.height() * (noOfLines + 1));
            int right = bitmap.getWidth();
            int bottom = bitmap.getHeight();

            int x = (bitmap.getWidth() - bounds.width()) / 2;
            int y = (bitmap.getHeight() + bounds.height()) / 2;

            int vertEnd = 0;
            for (String line : name.split("\n")) {
                vertEnd += paint.descent() - paint.ascent();
            }

            y = y - vertEnd;
            int canvasTop = y - 15;

            int vertical = 15;

            for (String line : name.split("\n")) {
                canvas.drawText(line, 10, vertical, paint);
                vertical += paint.descent() - paint.ascent();
            }

            //canvas.drawText(name, 10, 20, paint);

            /*paint.setColor(context.getResources().getColor(R.color.transparent_tabMedium));
            paint.setStrokeWidth(3);
            paint.setStyle(Paint.Style.STROKE);*/

            // draw corner to the Canvas
            //canvas.drawRect(x - 5, 0.0f, x + bounds.width() + 5, 16.0f, paint);
            canvas.drawRect(left, canvasTop, right, top, paint);

            return bitmap;

            // save on disk
            /*FileOutputStream out = null;
            File file = null;
            try {
                String path = Environment.getExternalStorageDirectory().toString();
                file = new File(path + "/Download/", "item.png");
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
                out.close();

                MediaStore.Images.Media.insertImage(context.getContentResolver(),file.getPath(),file.getName(),file.getName());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public Bitmap getBitmap(String path) {
        try {
            Bitmap bitmap = null;
            File f = new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            return bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public LatLng getCurrentLocation(Context mContext, GPSTracker mGPGpsTracker) {

        LatLng mLatLng;

        if (mGPGpsTracker.canGetLocation()) {
            Double latitude = mGPGpsTracker.getLatitude();
            Double longitude = mGPGpsTracker.getLongitude();
            Log.d("showMarkerCurrent", "showMarkerCurrentLocation: " + latitude + " == " + longitude);

            mLatLng = new LatLng(latitude, longitude);
        } else {
            showToast(mContext, "Please check your gps connection and try again");
            mLatLng = new LatLng(0.0, 0.0);
        }
        return mLatLng;
    }
    /*add location details to bitmap end*/

    public LatLng getLocationPromt(Context context, GPSTracker gpsTracker) {
        LocationManager locationmanager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        LatLng mLatLng = null;
        if (locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            try {
                mLatLng = getCurrentLocation(context, gpsTracker);

                if (mLatLng.latitude == 0.0 && mLatLng.longitude == 0.0) {
                    showToast(context, "Please check your gps connection and try again");
                    mLatLng = new LatLng(0.0, 0.0);
                    mLatLng = getCurrentLocation(context, gpsTracker);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                mLatLng = getCurrentLocation(context, gpsTracker);
            }
        } else {
            showGPSDisabledAlertToUser(context);
        }
        return mLatLng;
    }

    public void showGPSDisabledAlertToUser(final Context mContext) {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                mContext, android.app.AlertDialog.THEME_HOLO_LIGHT);


        alertDialogBuilder
                .setMessage(
                        Html
                                .fromHtml("<font color='#00a1e3'>GPS is disabled in your device. Would you like to enable it?</font>"))
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                mContext.startActivity(callGPSSettingIntent);

                            }
                        });

        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void storeImage(Context mContext, Bitmap mBitmap, String path) {

        OutputStream fOut = null;

        File file = new File(path);

        try {

            fOut = new FileOutputStream(file);

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        }

        mBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);

        try {

            fOut.flush();

        } catch (IOException e) {

            e.printStackTrace();

        }

        try {

            fOut.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

        try {

            MediaStore.Images.Media.insertImage(mContext.getContentResolver(), file.getPath(), file.getName(), file.getName());

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        }

    }

    /*public File createAllFiles(Context mContext, String strFileName) throws IOException {

        String strSubFolderPath = mContext.getExternalFilesDir(null).getAbsolutePath()
                + File.separator + DIRECT_DIRECTORY;

        File subFolder = new File(strSubFolderPath);
        if (!subFolder.exists()){
            subFolder.mkdirs();
        }

        return new File(subFolder, strFileName);
    }*/

    public boolean DetectFace(Context mContext, Bitmap myBitmap) {
        Log.d("DetectFace", "DetectFace: " + myBitmap);

        int face_det_incrment = 0;

        FaceDetector faceDetector = new
                FaceDetector.Builder(mContext).setTrackingEnabled(false)
                .build();
        if (!faceDetector.isOperational()) {
            // new Builder(this).setMessage("Could not set up the face detector!").show();
            return true;
        }
        Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
        SparseArray<Face> faces = faceDetector.detect(frame);
        if (faces.size() > 0) {
            return true;
        } else {
            face_det_incrment = face_det_incrment + 1;

            if (face_det_incrment > 2) {
                return true;
            } else {
                return false;
            }

     /*   imageWidth = myBitmap.getWidth();
    imageHeight = myBitmap.getHeight();
    myFace = new android.media.FaceDetector.Face[numberOfFace];
    myFaceDetect = new android.media.FaceDetector(imageWidth, imageHeight, numberOfFace);
    numberOfFaceDetected = myFaceDetect.findFaces(myBitmap, myFace);
        if (numberOfFaceDetected > 0) {
            return true;
        }
        else{
        return false;
        }*/
        }
    }

    public Bitmap exif(String photoPath, Bitmap bitmap) {
        ExifInterface ei = null;
        Bitmap rotatedBitmap = null;
        try {
            ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            Log.d("exif", "exif: " + orientation);

            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotatedBitmap;
    }

    public Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public String getCurrentDateSlashFormat() {
        Calendar present_date = Calendar.getInstance();
        int mDay = present_date.get(Calendar.DAY_OF_MONTH);
        int mMonth = present_date.get(Calendar.MONTH) + 1;
        int mYear = present_date.get(Calendar.YEAR);

        return mDay + "/" + mMonth + "/" + mYear;
    }

    public void showDateDialogCommon(DatePickerDialog.OnDateSetListener mDateSetListener, Context context, int mYear,
                                     int mMonth, int mDay) {
        DatePickerDialog dialog = new DatePickerDialog(
                context,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                mYear, mMonth, mDay);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public String generateCaptcha() {

        String[] alpha = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String a = "", b = "", c = "", d = "", e = "", f = "", g = "";
        for (int i = 0; i < 6; i++) {
            int length = alpha.length;
            double multiplicationA = Math.random() * length;
            a = alpha[(int) Math.floor(multiplicationA)];

            double multiplicationB = Math.random() * length;
            b = alpha[(int) Math.floor(multiplicationB)];

            double multiplicationC = Math.random() * length;
            c = alpha[(int) Math.floor(multiplicationC)];

            double multiplicationD = Math.random() * length;
            d = alpha[(int) Math.floor(multiplicationD)];

            double multiplicationE = Math.random() * length;
            e = alpha[(int) Math.floor(multiplicationE)];

            double multiplicationF = Math.random() * length;
            f = alpha[(int) Math.floor(multiplicationF)];

            double multiplicationG = Math.random() * length;
            g = alpha[(int) Math.floor(multiplicationG)];
        }
        String code = a + ' ' + b + ' ' + ' ' + c + ' ' + d + ' ' + e + ' ' + f + ' ' + g;
        /*document.getElementById("mainCaptcha").innerHTML = code
        document.getElementById("mainCaptcha").value = code*/

        return code;
    }

    public boolean validateCaptcha(Context context, TextView tvCaptchaCode, EditText edittextCaptcha) {
        boolean isvalid = true;
        try {
            String error = "";
            String captchaShown = tvCaptchaCode.getText().toString();
            captchaShown = captchaShown.replaceAll("\\s", "");
            String captchaEntered = edittextCaptcha.getText().toString();

            System.out.println("captchaShown:" + captchaShown);
            System.out.println("captchaEntered:" + captchaEntered);
            if (!captchaShown.equals(captchaEntered)) {
                error = "Please enter valid captcha";
                isvalid = false;
            }

            if (!error.equals("")) {
                showMessageDialog(context, error);
                isvalid = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            isvalid = false;
        }
        return isvalid;
    }

    public byte[] convertFileToByteArray(Context mContext, File inputFile) {

        try {

            if (inputFile != null) {
                FileInputStream fin = new FileInputStream(inputFile);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] b = new byte[1024];
                int bytesRead = 0;
                try {
                    while ((bytesRead = fin.read(b)) != -1) {
                        bos.write(b, 0, bytesRead);
                    }

                    byte[] mAllBytes = bos.toByteArray();
                    bos.flush();
                    bos.close();
                    return mAllBytes;

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
        return null;
    }

    //AOB added 19-01-2021
    public String getSubStringByString(String mainString, String exctractString) {
        String endString = "";
        String startString = "<" + exctractString + ">";
        String lastString = "</" + exctractString + ">";

        int endIndex = mainString.indexOf(lastString) + (startString.length() + 1);
        int startIndex = mainString.indexOf(startString);
        printLog("message", "" + mainString.substring(startIndex, endIndex));
        endString = mainString.substring(startIndex, endIndex);
        return endString;
    }

    public String getEncryptedAuthKey() {
        String resultOne = "", resultOnePost = "", pinOne = "1234";
        String resultTwo = "", resultTwoPost = "", pinTwo = "5678";

        for (int i = 12; i > 0; --i) {
            if (i == 10) {
                resultOnePost += pinOne.charAt(2);
                resultOne += Chars.charAt((int) Math.floor(Math.random() * Chars.length()));

                resultTwoPost += pinTwo.charAt(2);
                resultTwo += Chars.charAt((int) Math.floor(Math.random() * Chars.length()));
            } else if (i == 8) {
                resultOne += pinOne.charAt(0);
                resultOnePost += Chars.charAt((int) Math.floor(Math.random() * Chars.length()));

                resultTwo += pinTwo.charAt(0);
                resultTwoPost += Chars.charAt((int) Math.floor(Math.random() * Chars.length()));
            } else if (i == 5) {
                resultOnePost += pinOne.charAt(3);
                resultOne += Chars.charAt((int) Math.floor(Math.random() * Chars.length()));

                resultTwoPost += pinTwo.charAt(3);
                resultTwo += Chars.charAt((int) Math.floor(Math.random() * Chars.length()));
            } else if (i == 3) {
                resultOne += pinOne.charAt(1);
                resultOnePost += Chars.charAt((int) Math.floor(Math.random() * Chars.length()));

                resultTwo += pinTwo.charAt(1);
                resultTwoPost += Chars.charAt((int) Math.floor(Math.random() * Chars.length()));
            } else {
                resultOne += Chars.charAt((int) Math.floor(Math.random() * Chars.length()));
                resultOnePost += Chars.charAt((int) Math.floor(Math.random() * Chars.length()));

                resultTwo += Chars.charAt((int) Math.floor(Math.random() * Chars.length()));
                resultTwoPost += Chars.charAt((int) Math.floor(Math.random() * Chars.length()));
            }
        }
        String hashPass = "03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4";

        String resultOneWh = resultOne + resultOnePost;
        resultOneWh = resultOneWh.substring(0, resultOneWh.length() - 4);

        String resultTwoWh = resultTwo + resultTwoPost;
        resultTwoWh = resultTwoWh.substring(0, resultTwoWh.length() - 4);

        //String authkey = resultOne + resultOnePost + hashPass + resultTwo + resultTwoPost;
        String authkey = resultOneWh + hashPass + resultTwoWh;
        return authkey;
    }

    public String formatDateMMDDyyyyCommon(String strDOB) {

        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");
        final SimpleDateFormat formatter1 = new SimpleDateFormat("MM-dd-yyyy");
        try {
            Date dt = formatter.parse(strDOB);
            strDOB = formatter1.format(dt);
        } catch (ParseException e) {
            e.printStackTrace();
            strDOB = "";
        }
        return strDOB;
    }

    //added 17-07-2021
    /*public boolean isDD_MM_YYYY_String(String dateToValdate) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        //To make strict date format validation
        formatter.setLenient(false);
        try {
            Date parsedDate = formatter.parse(dateToValdate);
            System.out.println("++validated DATE TIME ++"+formatter.format(parsedDate));
            return true;
        } catch (ParseException e) {
            //Handle exception
            e.printStackTrace();
            return false;
        }
    }*/

    public String formatDateDDMMyyyyCommon(String strDOB) {

        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");
        final SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date dt = formatter.parse(strDOB);
            strDOB = formatter1.format(dt);
        } catch (ParseException e) {
            e.printStackTrace();
            strDOB = "";
        }
        return strDOB;
    }

    public void setImageBtnListenerOrDisable(ImageButton btn,
                                             View.OnClickListener mTakePicOnClickListener, String intentName, Context context) {
        if (isIntentAvailable(context, intentName)) {
            btn.setOnClickListener(mTakePicOnClickListener);
        } else {
            btn.setTag(context.getText(R.string.cannot).toString() + " "
                    + btn.getContext());
            btn.setClickable(false);
        }
    }

    public void showDispositionAlert(Context context, String premiumFUP, String policyNumber,
                                     String strCIFBDMEmailId, String strCIFBDMMObileNo,
                                     String strCIFBDMUserId) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);


        LayoutInflater inflater = ((AppCompatActivity) context).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dispositon_alert_dialog, null);
        alertDialogBuilder.setView(dialogView);
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();

        final TextView updateRemarkBtn, viewRemarkBtn, btn_renewal_remarks, btn_revival_remarks;
        final LinearLayout updateRemarkLayout, viewRemarkLayout, llCallCenterRemarks;
        final Button btn_submit_disposition_remark, sbiLifeRemarkBtn, callCenterRemarkBtn, salesRemarkBtn;

        final Spinner dispotionSpinner = dialogView.findViewById(R.id.spinner_disposition);
        final Spinner subdispotionSpinner = dialogView.findViewById(R.id.spinner_subdisposition);
        final EditText dispositionRemarkText = dialogView.findViewById(R.id.edt_disposition_remark);
        btn_submit_disposition_remark = dialogView.findViewById(R.id.btn_submit_disposition_remark);
        sbiLifeRemarkBtn = dialogView.findViewById(R.id.btn_sbi_life_remark);
        callCenterRemarkBtn = dialogView.findViewById(R.id.btn_call_center_remark);
        salesRemarkBtn = dialogView.findViewById(R.id.btn_sales_remark);

        updateRemarkBtn = dialogView.findViewById(R.id.btn_update_remark);
        updateRemarkBtn.setSelected(true);
        viewRemarkBtn = dialogView.findViewById(R.id.btn_view_remark);
        viewRemarkBtn.setSelected(false);

        btn_renewal_remarks = dialogView.findViewById(R.id.btn_renewal_remarks);
        btn_revival_remarks = dialogView.findViewById(R.id.btn_revival_remarks);

        updateRemarkLayout = dialogView.findViewById(R.id.ll_update_remark_parent);
        viewRemarkLayout = dialogView.findViewById(R.id.ll_view_remark_parent);
        llCallCenterRemarks = dialogView.findViewById(R.id.llCallCenterRemarks);

        final ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(context, R.layout.spinner_text, context.getResources().getStringArray(R.array.disposition_array));
        langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        dispotionSpinner.setAdapter(langAdapter);

        dispotionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] subDispositionArray = null;
                switch (i) {
                    case 0:
                        dispositionRemarkText.setVisibility(View.GONE);
                        btn_submit_disposition_remark.setVisibility(View.GONE);
                        subDispositionArray = new String[]{""};
                        break;
                    case 1:
                        subDispositionArray = new String[]{"Select Sub Disposition", "Callback_Call To Speak Later on for Retention", "Left Message",
                                "Received by Customer Representative. Asked for call back", "Out Of Station"};
                        break;
                    case 2:
                        subDispositionArray = new String[]{"Select Sub Disposition", "Already Paid To Branch", "Drop Box", "Online", "MP Online",
                                "Paid To Advisor/ CIF before 15 days", "Paid To Advisor/ CIF within 15 to 30 days"};
                        break;
                    case 3:
                        subDispositionArray = new String[]{"Select Sub Disposition", "Other Language / language barier"};
                        break;
                    case 4:
                        subDispositionArray = new String[]{"Select Sub Disposition", "Advisor Owned Policy", "Customer Expired",
                                "Do Not Disturb", "Employee Owned Policy", "Applied for Surrender the policy"};
                        break;
                    case 5:
                        subDispositionArray = new String[]{"Select Sub Disposition", "Intrested but financial problem", "Not Intested due to financial problem"};
                        break;
                    case 6:
                        subDispositionArray = new String[]{"Select Sub Disposition", "Address Changes", "Earlier complaint raised_No solution received",
                                "Mode Change", "Other", "Policy Document Not Recd", "Fund value / bonus statement"};
                        break;
                    case 7:
                        subDispositionArray = new String[]{"Select Sub Disposition", "Requesting for Pickup", "Will pay in Grace period",
                                "Promise To Pay", "Will Pay Later", "Interested to pay online"};
                        break;
                    case 8:
                        subDispositionArray = new String[]{"Select Sub Disposition", "features & Benefits notexplained", "Misselling false promises made",
                                "Misselling force selling", "Misselling high allocation charges", "Misselling wrong product sold",
                                "Sold As Single Premium or LPPT", "Policy Against KCC", "Poilcy Against Loan"};
                        break;
                    case 9:
                        subDispositionArray = new String[]{"Select Sub Disposition", "Beep Tone", "DIALER_SIT_TONE", "Number Does Not Exist",
                                "Customers Has Relocated (Abroad)", "Out Of Service"};
                        break;
                    case 10:
                        subDispositionArray = new String[]{"Select Sub Disposition", "Call Disconnected", "Engage", "Not Reachable", "Ringing",
                                "Switched Off", "Customer Busy"};
                        break;
                    case 11:
                        subDispositionArray = new String[]{"Select Sub Disposition", "Completed  Locking Period", "Customer Has Bought Competitor Policy",
                                "Low Return", "Not Ready For Stating Reason", "Refuse To Pay", "Taken Another Policy With Sbi Life",
                                "Renewal premium reminder Notice not received", "Not satisfied with advisor services", "Overall services of the company"};
                        break;
                    case 12:
                        subDispositionArray = new String[]{"Select Sub Disposition", "IA Number", "Not Related Patry"};
                        break;
                    case 13:
                        subDispositionArray = new String[]{"Select Sub Disposition", "Call Back", "Already Paid", "Language barier", "Do Not Call",
                                "Financial Problem", "Customer query / issue", "Interested", "Misselling", "Permanent Non Contactable",
                                "Temporary Non Contactable", "Not Interested", "Wrong number"};
                        break;
                    case 14:
                        subDispositionArray = new String[]{"Select Sub Disposition", "Contact Number Sourced", "Forward for structured follow up"};
                        break;
                    case 15:
                        subDispositionArray = new String[]{"Select Sub Disposition", "All Efforts Done"};
                        break;
                    case 16:
                        subDispositionArray = new String[]{"Select Sub Disposition", "New"};
                        break;

                }

                ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(context, R.layout.spinner_text, subDispositionArray);
                langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                subdispotionSpinner.setAdapter(langAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        subdispotionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String subDispositionString = subdispotionSpinner.getSelectedItem().toString();
                if (!(subDispositionString.equalsIgnoreCase("")) &&
                        !(subDispositionString.equalsIgnoreCase("Select Sub Disposition"))) {
                    dispositionRemarkText.setVisibility(View.VISIBLE);
                    btn_submit_disposition_remark.setVisibility(View.VISIBLE);
                } else {
                    dispositionRemarkText.setVisibility(View.GONE);
                    btn_submit_disposition_remark.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                dispositionRemarkText.setVisibility(View.GONE);
                btn_submit_disposition_remark.setVisibility(View.GONE);
            }
        });

        updateRemarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRemarkBtn.setSelected(true);
                viewRemarkBtn.setSelected(false);

                viewRemarkLayout.setVisibility(View.GONE);
                updateRemarkLayout.setVisibility(View.VISIBLE);
                llCallCenterRemarks.setVisibility(View.GONE);
            }
        });
        viewRemarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewRemarkBtn.setSelected(true);
                updateRemarkBtn.setSelected(false);
                hideKeyboard(view, context);
                updateRemarkLayout.setVisibility(View.GONE);
                viewRemarkLayout.setVisibility(View.VISIBLE);
                dispositionRemarkText.setText("");
                dispotionSpinner.setSelection(0);

                llCallCenterRemarks.setVisibility(View.GONE);
            }
        });
        callCenterRemarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                llCallCenterRemarks.setVisibility(View.VISIBLE);

                btn_renewal_remarks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                        String DueDate = premiumFUP;
                        String PAY_EX1_74 = policyNumber;
                        Intent intent = new Intent(context, RenewalCallingRemarksActivity.class);
                        intent.putExtra("objectType", "");//
                        intent.putExtra("DueDate", DueDate);
                        intent.putExtra("PAY_EX1_74", PAY_EX1_74);
                        intent.putExtra("StatusCodeID", "");//
                        context.startActivity(intent);
                    }
                });

                btn_revival_remarks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                        GetRemarksCommonAsyncTask getRemarksCommonAsyncTask = new GetRemarksCommonAsyncTask(policyNumber,
                                strCIFBDMEmailId, strCIFBDMMObileNo, context, "Revival Remarks");
                        getRemarksCommonAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                });

            }
        });
        sbiLifeRemarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llCallCenterRemarks.setVisibility(View.GONE);
                //alert.dismiss();
                GetRemarksCommonAsyncTask getRemarksCommonAsyncTask = new GetRemarksCommonAsyncTask(policyNumber,
                        strCIFBDMEmailId, strCIFBDMMObileNo, context, "");
                getRemarksCommonAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        salesRemarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llCallCenterRemarks.setVisibility(View.GONE);
                //alert.dismiss();
                GetRemarksCommonAsyncTask getRemarksCommonAsyncTask = new GetRemarksCommonAsyncTask(policyNumber,
                        strCIFBDMEmailId, strCIFBDMMObileNo, context, "");
                getRemarksCommonAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        btn_submit_disposition_remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String disposition = dispotionSpinner.getSelectedItem().toString();
                String subdisposition = subdispotionSpinner.getSelectedItem().toString();
                String dispositionRemark = dispositionRemarkText.getText().toString();

                if (disposition.equalsIgnoreCase("Select Disposition")) {
                    Toast.makeText(context, "Please select disposition option", Toast.LENGTH_LONG).show();
                } else if (subdisposition.equalsIgnoreCase("Select Sub Disposition")) {
                    Toast.makeText(context, "Please select sub disposition option", Toast.LENGTH_LONG).show();
                } else if (dispositionRemark == null || dispositionRemark.equalsIgnoreCase("")) {
                    Toast.makeText(context, "Please enter disposition remark", Toast.LENGTH_LONG).show();
                } else {
                    //saveCallingRemarks_smrt
                    alert.dismiss();
                    SubmitDispositionRemarCommonkAsync submitDispositionRemarkAsync = new SubmitDispositionRemarCommonkAsync(
                            policyNumber, disposition, subdisposition, dispositionRemark, strCIFBDMUserId, context);
                    submitDispositionRemarkAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        });

    }

    private void displayRenewalRemarks(ArrayList<ParseXML.RenewalRemark> renewalRemarkArrayList, Context context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
        LayoutInflater inflater = ((AppCompatActivity) context).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.remark_alert_dialog, null);
        alertDialogBuilder.setView(dialogView);
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();

        RecyclerView remarkRecyclerView = dialogView.findViewById(R.id.remark_list_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        remarkRecyclerView.setLayoutManager(linearLayoutManager);

        RenewalRemarkAdapter renewalRemarkAdapter = new RenewalRemarkAdapter(renewalRemarkArrayList);
        remarkRecyclerView.setAdapter(renewalRemarkAdapter);
    }

    public void updateAltMobileAlert(Context context, String dueDate, String pAY_EX1_74,
                                     UpdateAltMobileNoCommonAsyncTask.UpdateAltMobNoInterface listener) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);


        LayoutInflater inflater = ((AppCompatActivity) context).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_update_alt_mob_no, null);
        alertDialogBuilder.setView(dialogView);
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();

        final Button buttonUpdateAltMobileCommon;

        final EditText etUpdateAltMobileNo = dialogView.findViewById(R.id.etUpdateAltMobileNo);
        buttonUpdateAltMobileCommon = dialogView.findViewById(R.id.buttonUpdateAltMobileCommon);

        buttonUpdateAltMobileCommon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobileNumber = etUpdateAltMobileNo.getText().toString();
                if (TextUtils.isEmpty(mobileNumber)) {
                    Toast.makeText(context, "Please enter Mobile Number", Toast.LENGTH_LONG).show();
                } else {
                    alert.dismiss();
                    GetAltMobUpdateKeyAsync getAltMobUpdateKeyAsync = new GetAltMobUpdateKeyAsync(dueDate,
                            pAY_EX1_74, mobileNumber,
                            context, listener);
                    getAltMobUpdateKeyAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        });

    }

    public String encryptData(String pin) {
        String result = "", resultPost = "";
        //var charsArray = pin.split('');
        System.out.println("pin = " + pin.length() + " == " + pin);
        if (pin.length() == 6) {
            for (int i = 17; i > 0; --i) {
                if (i == 15) {
                    resultPost += pin.charAt(3);
                    //resultPost += "#";
                    result += Chars.charAt((int) Math.floor(Math.random() * Chars.length()));
                } else if (i == 13) {
                    result += pin.charAt(0);
                    //result += "#";
                    resultPost += Chars.charAt((int) Math.floor(Math.random() * Chars.length()));
                } else if (i == 10) {
                    resultPost += pin.charAt(4);
                    //resultPost += "#";
                    result += Chars.charAt((int) Math.floor(Math.random() * Chars.length()));
                } else if (i == 8) {
                    result += pin.charAt(1);
                    //result += "#";
                    resultPost += Chars.charAt((int) Math.floor(Math.random() * Chars.length()));
                } else if (i == 5) {
                    resultPost += pin.charAt(5);
                    //resultPost += "#";
                    result += Chars.charAt((int) Math.floor(Math.random() * Chars.length()));
                } else if (i == 3) {
                    result += pin.charAt(2);
                    //result += "#";
                    resultPost += Chars.charAt((int) Math.floor(Math.random() * Chars.length()));
                } else {
                    result += Chars.charAt((int) Math.floor(Math.random() * Chars.length()));
                    resultPost += Chars.charAt((int) Math.floor(Math.random() * Chars.length()));
                }
            }
            result += resultPost;
        } else {
            for (int i = 12; i > 0; --i) {
                if (i == 10) {
                    resultPost += pin.charAt(2);
                    //resultPost += "#";
                    result += Chars.charAt((int) Math.floor(Math.random() * Chars.length()));
                } else if (i == 8) {
                    result += pin.charAt(0);
                    //result += "#";
                    resultPost += Chars.charAt((int) Math.floor(Math.random() * Chars.length()));
                } else if (i == 5) {
                    resultPost += pin.charAt(3);
                    //resultPost += "#";
                    result += Chars.charAt((int) Math.floor(Math.random() * Chars.length()));
                } else if (i == 3) {
                    result += pin.charAt(1);
                    //result += "#";
                    resultPost += Chars.charAt((int) Math.floor(Math.random() * Chars.length()));
                } else {
                    result += Chars.charAt((int) Math.floor(Math.random() * Chars.length()));
                    resultPost += Chars.charAt((int) Math.floor(Math.random() * Chars.length()));
                }
            }
            result += resultPost;
        }

        return result;
    }

    public Object[] compressImageCIF_Photo_Sign(Context mContext, File filePath, int imgRate) {

        Bitmap scaledBitmap = null;
        File outputFile = null;
        // String filePath = getRealPathFromURI(imageUri);
        try {

            BitmapFactory.Options options = new BitmapFactory.Options();

            // by setting this field as true, the actual bitmap pixels are not
            // loaded in the memory. Just the bounds are loaded. If
            // you try the use the bitmap here, you will get null.
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(filePath.getAbsolutePath(), options);

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;

            // max Height and width values of the compressed image is taken as
            // 816x612
            float maxHeight = 816.0f;
            float maxWidth = 612.0f;

            if (imgRate == 1) {
                maxHeight = 816.0f;
                maxWidth = 612.0f;
            } else if (imgRate == 2) {
                maxHeight = 716.0f;
                maxWidth = 512.0f;
            } else if (imgRate == 3) {
                maxHeight = 616.0f;
                maxWidth = 412.0f;
            } else if (imgRate == 4) {
                maxHeight = 516.0f;
                maxWidth = 312.0f;
            }

            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

            // width and height values are set maintaining the aspect ratio of the
            // image

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;
                }
            }

            // setting inSampleSize value allows to load a scaled down version of
            // the original image

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

            // inJustDecodeBounds set to false to load the actual bitmap
            options.inJustDecodeBounds = false;

            // this options allow android to claim the bitmap memory if it runs low
            // on memory
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            // load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath.getAbsolutePath(), options);

            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
                    Bitmap.Config.ARGB_8888);

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
                    middleY - bmp.getHeight() / 2, new Paint(
                            Paint.FILTER_BITMAP_FLAG));

            // check the rotation of the image and display it properly
            ExifInterface exif = new ExifInterface(filePath.getAbsolutePath());

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);

            outputFile = new StorageUtils().createFileToAppSpecificDirCIF(mContext, System.currentTimeMillis() + ".jpg");

            FileOutputStream out = new FileOutputStream(outputFile);

            // write the compressed bitmap at the destination specified by
            // filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

            double file_size = outputFile.length() / 1024;

            if (file_size > 150) {
                compressImageCIF_Photo_Sign(mContext, filePath, 2);//2
            } else if (file_size > 100) {
                compressImageCIF_Photo_Sign(mContext, filePath, 3);//3
            } else if (file_size > 50) {
                compressImageCIF_Photo_Sign(mContext, filePath, 4);//4
            }

        } catch (Exception er) {
            er.printStackTrace();
        }
        return new Object[]{scaledBitmap, outputFile};
    }

    public class UserDetailsValuesModel {

        private String strCIFBDMUserId = "", strCIFBDMEmailId = "",
                strCIFBDMPassword = "", strCIFBDMMObileNo = "";

        UserDetailsValuesModel(String strCIFBDMUserId,
                               String strCIFBDMEmailId, String strCIFBDMPassword,
                               String strCIFBDMMObileNo) {
            super();
            this.strCIFBDMUserId = strCIFBDMUserId;
            this.strCIFBDMEmailId = strCIFBDMEmailId;
            this.strCIFBDMPassword = strCIFBDMPassword;
            this.strCIFBDMMObileNo = strCIFBDMMObileNo;
        }

        public String getStrCIFBDMUserId() {
            return strCIFBDMUserId;
        }

        public String getStrCIFBDMEmailId() {
            return strCIFBDMEmailId;
        }

        public String getStrCIFBDMPassword() {
            return strCIFBDMPassword;
        }

        public String getStrCIFBDMMObileNo() {
            return strCIFBDMMObileNo;
        }
    }

    class MyValueFormatter extends PercentFormatter {
        DecimalFormat mFormat;
        PieChart mPieChart;

        public MyValueFormatter(PieChart pieChart) {
            mFormat = new DecimalFormat("###,###,##0.0");
            mPieChart = pieChart;
        }


        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(value) + "%";
        }

        @Override
        public String getPieLabel(float value, PieEntry pieEntry) {
            if (mPieChart != null && mPieChart.isUsePercentValuesEnabled()) {
                // Converted to percent
                return getFormattedValue(value);
            } else {
                // raw value, skip percent sign
                return mFormat.format(value);
            }
        }
    }

    class GetRemarksCommonAsyncTask extends AsyncTask<String, Void, String> {

        private final String policyNo;
        private final Context context;
        private final String strCIFBDMEmailId;
        private final String strCIFBDMMObileNo;
        private final String flag;
        ProgressDialog mProgressDialog;
        private volatile boolean running = true;
        private ArrayList<ParseXML.RenewalRemark> renewalRemarkArrayList;
        private CommonMethods commonMethods;

        GetRemarksCommonAsyncTask(String policyNo, String strCIFBDMEmailId, String strCIFBDMMObileNo,
                                  Context context, String flag) {
            this.policyNo = policyNo;
            this.context = context;
            this.strCIFBDMEmailId = strCIFBDMEmailId;
            this.strCIFBDMMObileNo = strCIFBDMMObileNo;
            commonMethods = new CommonMethods();
            this.flag = flag;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading. Please wait...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setMax(100);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... data) {
            try {
                running = true;

                String NAMESPACE = "http://tempuri.org/";
                String METHOD_NAME_GET_REMARK = "";
                if (flag.equalsIgnoreCase("Revival Remarks")) {
                    //getCallingRemarksContact_smrt(string strBRcode,string strFromDate,
                    // string strToDate,string strEmailId, string strMobileNo, string strAuthKey)
                    METHOD_NAME_GET_REMARK = "getCallingRemarksContactSales_smrt";
                } else {
                    METHOD_NAME_GET_REMARK = "getCallingRemarks_smrt";
                }

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_GET_REMARK);

                request.addProperty("policyno", policyNo);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", commonMethods.getStrAuth());
                //appendSecurityParams(context,request,"","");
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION_DISPOSITION_REMARK = "http://tempuri.org/" + METHOD_NAME_GET_REMARK;
                androidHttpTranport.call(SOAP_ACTION_DISPOSITION_REMARK, envelope);

                SoapPrimitive sa;
                sa = (SoapPrimitive) envelope.getResponse();

                String output = sa.toString();
                //Log.d(TAG, "doInBackground:1 "+output);
                //ParseXML prsObj = new ParseXML();
                if (!output.contentEquals("<NewDataSet /> ")) {
                            /*<NewDataSet>
                              <QueryResults>
                                <PAY_EX1_91>Move to Call Centre</PAY_EX1_91>
                                <PAY_EX1_96>Forward for structured follow up</PAY_EX1_96>
                                <HTMLTEXT_280>LastModified By: 1point1 LastModified On: 02-07-2018 7:51:24 PM&lt;br /&gt;
                                                    Customer contact no is updated&lt;br /&gt;</HTMLTEXT_280>
                                <PAY_EX1_95>AMBER</PAY_EX1_95>
                                <ROWNUMBER>1</ROWNUMBER>
                                <Key>45693</Key>
                              </QueryResults>
                            </NewDataSet>*/
                    //SoapPrimitive sa = null;
                    try {
                        // sa = (SoapPrimitive) envelope.getResponse();
                        String inputpolicylist = output;

                        if (!inputpolicylist.equalsIgnoreCase("<NewDataSet /> ")) {

                            ParseXML parseXML = new ParseXML();
                            String DataResultXML = parseXML.parseXmlTag(
                                    inputpolicylist, "NewDataSet");
                            //DataResultXML = escapeXml(DataResultXML);
                            List<String> Node = parseXML.parseParentNode(
                                    DataResultXML, "Table");
                            renewalRemarkArrayList = parseXML
                                    .parseRenewalRemark(Node);

                        }

                    } catch (Exception e) {

                        mProgressDialog.dismiss();
                        running = false;
                    }
                }

            } catch (Exception e) {
                running = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (running) {
                if (renewalRemarkArrayList != null && renewalRemarkArrayList.size() > 0) {
                    displayRenewalRemarks(renewalRemarkArrayList, context);
                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_RECORD_FOUND);
                }
            } else {
                commonMethods.showMessageDialog(context, commonMethods.NO_RECORD_FOUND);
            }
        }

    }

    public class RenewalRemarkAdapter extends RecyclerView.Adapter<RenewalRemarkAdapter.ViewHolderAdapter> {

        private final ArrayList<ParseXML.RenewalRemark> lstAdapterList;

        RenewalRemarkAdapter(ArrayList<ParseXML.RenewalRemark> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }


        @Override
        public int getItemCount() {
            return lstAdapterList.size();
        }

        @Override
        public RenewalRemarkAdapter.ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.remark_item_layout, parent, false);

            return new RenewalRemarkAdapter.ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final RenewalRemarkAdapter.ViewHolderAdapter holder, int position) {
            ParseXML.RenewalRemark renewalRemark = lstAdapterList.get(position);

            holder.txtDispositionRemark.setText(renewalRemark.getSTATUS());
            holder.txtSubDispositionRemark.setText(renewalRemark.getSUBSTATUS());
            holder.txtRemark.setText(renewalRemark.getREMARKS());
            holder.txtCreatedDate.setText(renewalRemark.getCREATED_DATE());
        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder {


            private final TextView txtDispositionRemark, txtSubDispositionRemark, txtRemark, txtCreatedDate;

            ViewHolderAdapter(View v) {
                super(v);
                txtDispositionRemark = v.findViewById(R.id.txt_disposition_remark);
                txtSubDispositionRemark = v.findViewById(R.id.txt_subdisposition_remark);
                txtRemark = v.findViewById(R.id.txt_remark);
                txtCreatedDate = v.findViewById(R.id.txt_remark_created_date);
            }

        }

    }

    class SubmitDispositionRemarCommonkAsync extends AsyncTask<String, Void, String> {

        private final String policyNo;
        private final String status;
        private final String substatus;
        private final String remarks;
        private final Context context;
        private final String strCIFBDMUserId;
        //Context context;
        ProgressDialog mProgressDialog;
        private volatile boolean running = true;
        private String output;
        private CommonMethods commonMethods;

        SubmitDispositionRemarCommonkAsync(String policyNo, String status, String substatus, String remarks,
                                           String strCIFBDMUserId, Context context) {
            this.policyNo = policyNo;
            this.status = status;
            this.substatus = substatus;
            this.remarks = remarks;
            this.context = context;
            this.strCIFBDMUserId = strCIFBDMUserId;
            commonMethods = new CommonMethods();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading. Please wait...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#000000'><b>" + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setMax(100);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... data) {
            try {
                running = true;

                String NAMESPACE = "http://tempuri.org/";
                String METHOD_NAME_DISPOSITION_REMARK = "saveCallingRemarks_smrt";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_DISPOSITION_REMARK);

                request.addProperty("policyno", policyNo);
                request.addProperty("status", status);
                request.addProperty("substatus", substatus);
                request.addProperty("remarks", remarks);
                request.addProperty("empid", strCIFBDMUserId);
                request.addProperty("strEmailId", "a@g.com");
                request.addProperty("strMobileNo", "0000000000");
                request.addProperty("strAuthKey", commonMethods.getStrAuth());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION_DISPOSITION_REMARK = "http://tempuri.org/saveCallingRemarks_smrt";
                androidHttpTranport.call(SOAP_ACTION_DISPOSITION_REMARK, envelope);

                SoapPrimitive sa;
                sa = (SoapPrimitive) envelope.getResponse();

                output = sa.toString();
            } catch (Exception e) {
                running = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (running) {
                if (output.equalsIgnoreCase("1")) {
                    commonMethods.showMessageDialog(context, "Remarks updated");
                } else if (output.equalsIgnoreCase("2")) {
                    commonMethods.showMessageDialog(context, "You are not authorized user.");
                } else {
                    commonMethods.showMessageDialog(context, "Remarks updation failed. Please try again later");
                }
            } else {
                commonMethods.showMessageDialog(context, "Server not responding..");
            }
        }

    }
}
