package sbilife.com.pointofsale_bancaagency.home;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import sbilife.com.pointofsale_bancaagency.CalendarAdapter;
import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolderAdvances;
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolderBDM_MAIL_DATA;
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolderBankBranch;
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolderBranch_Profile;
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolderCategory;
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolderDeposit;
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolderParamList;
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolderSubCategory;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.ViewRecordActivityAR;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.service.ScheduleClient;

@SuppressWarnings("deprecation")
public class BancaBDMTracker extends AppCompatActivity {

    //HO Lead

    //TBDM DOB as a password in Lead
    private TableLayout tblchecktbdmdob;
    private EditText edtbdmdob;
    private String strTBDMDOBPassword = "";
    private String strTpDob = "";
    //end TBDM DOB as a password in Lead

    private LinearLayout lnholead;

    private String strLeadTag = "";
    private String strLeadErrorCode = "";

    private Spinner spsrholead, spsrholeadpriority, spsrholeadstatus;
    private TableLayout tblsearchholead;
    private TableRow tblsrholddate, tblsrholdfolwdate, tblsrholdpri, tblsrholdsts;
    private EditText edsrholeadfdate, edsrholeadtdate, edsrhofolfdate, edsrhofoltdate;

    private String strsrholeadfilter;

    //end search ho lead

    private ListView lvholead;

    private final ArrayList<clsHOLead> lsthoLead = new ArrayList<>();

    //end HO lead

    private Button btnleadfromho, btnselflead;

    private EditText edlead, edlead_act;
    private Spinner selActivityTime;

    private String strBranchCodeErrorCode = "";
    private String strParamListErrorCode = "";
    private String strActivityCategoryErrorCode = "";
    private String strSubActivityErrorCode = "";

    // for bdm_mail_data
    private String strNBPMTD = "";
    private String strNBPYTD = "";
    private String strNOPMTD = "";
    private String strNOPYTD = "";

    private String strType = "";
    // end bdm_mail_data

    protected ListView lvSetting;

    private DecimalFormat currencyFormat;

    private String Sync_strId = "";
    private String Sync_cross = "";
    private String Sync_crossnop = "";
    private String Sync_crosspre = "";

    //private boolean syncbranchprofile_running = true;

    private String strBtnType = "";

    private String strDepositPerticular = "";
    private String strDepositCategory = "";

    private String strAdvancesPerticular = "";
    private String strAdvancesCategory = "";

    private String strBranchProfileErrorCode = "";
    private String strBranchDepositsErrorCode = "";
    private String strBranchAdvancesErrorCode = "";
    private String strBranchCode = "";

    private Thread splashTread;
    private boolean defineobjective_running = true;
    private String paramid = "";
    private int value = 0;

    private String strPullMasterId = "";

    private String strActivityRowId = "";
    private ArrayList<String> lstSyncTaskList = new ArrayList<>();
    private ArrayList<String> lstSyncTaskListSeq = new ArrayList<>();

    private ArrayList<String> lstActRecord = new ArrayList<>();

    private String strResult = "";
    private String strId = "";

    private DownloadActivity taskActivity;
    private DownloadSubActivity taskSubActivity;
    private PushTaskList taskList;
    private PushTaskListRecord taskListRecord;
    private PushTaskListDetail taskListDetail;
    private PushTaskListSeq taskListSeq;
    private DownloadBankBranch taskListBankBranch;
    private DownloadParamList taskListParamList;
    private DownloadSaveObjective taskListSaveObjective;
    private DownloadBranchProfile taskBranchProfile;
    private DownloadBranchDeposits taskBranchDeposits;
    private DownloadBranchAdvances taskBranchAdvances;
    private DownloadSyncBranchProfile taskSyncBranchProfile;
    private DownloadRinRaksha taskRinRaksha;
    private DownloadBdm_Dashboard taskBdm_Dashboard;

    private DownloadBdm_mail_data taskBdm_mail_data;

    private DownloadLead taskLead;

    private final int DIALOG_DOWNLOAD_PROGRESS = 1;
    private ProgressDialog mProgressDialog;

    private final String NAMESPACE = "http://tempuri.org/";
    // private  final String URl =
    // "http://172.17.114.196/posservics/Service.asmx?wsdl";
    // private  final String URl =
    // "https://125.18.9.94/service.asmx?wsdl";

    private final String URl = ServiceURL.SERVICE_URL;

    private final String SOAP_ACTION_ADDTASKLIST = "http://tempuri.org/saveUpdateMainTaskList";
    private final String METHOD_NAME_ADDTASKLIST = "saveUpdateMainTaskList";

    private final String SOAP_ACTION_SAVE_OBJECTIVE = "http://tempuri.org/saveActionOnObjective";
    private final String METHOD_NAME_SAVE_OBJECTIVE = "saveActionOnObjective";


    private TextView title;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH);
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    private TableLayout tbl;
    private LinearLayout setObjectiveLayout;

    // set alrm

    private int intday;
    private int intmonth;
    private int intyear;

    private CheckBox chk;
    private ScheduleClient scheduleClient;

    private final ArrayList<clsBDMTrackerCalendar> lstmain = new ArrayList<>();
    private final ArrayList<clsBDMTrackerCalendar> todaysmain = new ArrayList<>();
    private ListView lv;
    private final Context context = this;
    private ListView todaylv;

    // for time picker

    private EditText tvDisplayTime;

    private int hour;
    private int minute;
    private int ampm;

    private String strampm;

    private int hourT;
    private int minuteT;
    private int ampmT;
    private String strampmT;

    private final int TIME_DIALOG_ID = 999;

    private int mYear;
    private int mMonth;
    private int mDay;
    private final int DATE_DIALOG_ID = 0;

    private int mYearT;
    private int mMonthT;
    private int mDayT;

    private int strmonth;
    private int stryear;

    private String y;
    private String m;

    private String d;

    private DatabaseHelper db;
    private EditText etdate;
    private EditText edeventname;

    private GregorianCalendar month;

    private CalendarAdapter adapter;// adapter instance
    private Handler handler;// for grabbing some event values for showing the dot
    // marker.
    private ArrayList<String> items; // container to store calendar items which
    // needs showing the event marker

    private Spinner selActivity;
    private Spinner selBranch;

    private LinearLayout lnplanner;
    private LinearLayout lnrecordactivity;
    private LinearLayout lnmailer;
    private LinearLayout lnbranchprofile;
    private LinearLayout lnleadmgt;

    private TextView todaysActiviy;
    private RelativeLayout header;
    private LinearLayout lnmonthsel;

    private GridView gridview;

    private Button btnplanner;
    private Button btnactivityrecorder;
    private Button btndefineobj;
    private Button btnmailer;
    private Button btnbranchpro;
    private Button btnleadmgt;

    private Button btnaddactivity;
    private Button btnshowactivity;

    private Button btnbmreport;
    private Button btncifreport;
    private Button btnagmreport;
    private Button btnrmreport;

    private final ArrayList<clsCalendarActivityRecorder> todaysmainra = new ArrayList<>();
    private ListView todaylvra;

    private EditText edremark;

    private Button btnaddemail;
    private Button btnviewemail;
    private TableLayout tbladdemail;
    private TableLayout tblviewemail;

    private Spinner spgroup;
    private Spinner spviewgroup;
    private EditText editEmailId;
    private String strGroupEmail;
    private String strViewGroupEmail;
    private ListView lstEmail;

    private final ArrayList<clsEmail> lstemaillist = new ArrayList<>();

    private Spinner selActivityRecordActivity;
    private Spinner selSubActivityRecordActivity;
    private Spinner selBranchRecordActivity;
    private EditText editTextdtRecordActivity;
    private EditText tvTimeRecordActivity;
    private EditText edremarkRecordActivity;
    private EditText tvTimeToRecordActivity;
    private String strActivity = "";
    private String strSubActivity = "";

    private int timecheck = 0;
    private int datecheck = 0;

    ArrayList<clsActivityCategory> lstevent = new ArrayList<>();

    // search record

    private EditText edfromdate;
    private EditText edtodate;
    private TableLayout tblsearchactvity;
    private Button btnsearch;
    private Button btnrefresh;

    // for define objective
    private Spinner selCommitment;
    private Spinner selAchievement;
    private Spinner selPerAchievement;
    private EditText edcommitbname;
    private EditText edcommitnewbusinesscash;
    private EditText edcommithomeloan;
    private EditText edcommitnewbusinesspre;
    private EditText edcommitsharesingle;
    private EditText edcommitremark;
    private EditText edachievebrname;
    private EditText edperachievebrname;
    private EditText edcommitnewbusinesstot;
    private EditText edcommithomeloantot;
    private EditText edcommitnewbusinesspretot;
    private EditText edcommitsharesingletot;
    private EditText edcommitremarktot;

    private String strCode = "";
    private String strCode_ach = "";
    private String strCode_per = "";

    private TextView txtcommtnewbuscash;
    private TextView txtcommtshare;
    private TextView txtcommtnewbuspre;
    private TextView txtcommthomeloan;
    private TextView txtachnewbuscash;
    private TextView txtachshare;
    private TextView txtachnewbuspre;
    private TextView txtachhomeloan;
    private TextView txtperachnewbuscash;
    private TextView txtperachshare;
    private TextView txtperachnewbuspre;
    private TextView txtperachhomeloan;
    private int totalparam = 0;

    private EditText edachievenewbusinesscash;
    private EditText edachievehomeloan;
    private EditText edachievenewbusinesspre;
    private EditText edachievesharesingle;

    private EditText edperachievenewbusiness;
    private EditText edperachieveshare;
    private EditText edperachievenewbusinesspre;
    private EditText edperachievehomeloan;

    private String strRinRaksha = "";
    private String strNewPolicy = "";
    private String strRenewalPremium = "";
    private String strNewBusinessPre = "";

    // for branch profile
    private Spinner selbranchprofilecode;
    private Spinner seldeposit;
    private Spinner seldepositcategory;
    private Spinner seladvances;
    private Spinner seladvancescategory;
    private TableRow tblrdeposit;
    private TableRow tblradvances;
    private Button btndeposits;
    private Button btnadvances;
    private EditText edbranchname;
    private EditText edbranchopendate;
    private EditText edbranchnetresult;
    private EditText eddepositretail;
    EditText eddepositnonretail;
    private EditText eddepositcrosssel;
    private EditText eddepositcrossselnop;
    private EditText eddepositcrossselpot;
    private EditText edadvancesretail;
    EditText edadvancesnonretail;
    private EditText edadvancescrosssel;
    private EditText edadvancescrossselnop;
    private EditText edadvancescrossselpot;

    // for store userid in bdm tracker table
    private String strCIFBDMUserId;
    private String strCIFBDMUserType;
    private String strCIFBDMEmailId = "";
    private String strCIFBDMPassword = "";
    private String strCIFBDMMObileNo = "";
    private CommonMethods mCommonMethods;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.bdmtracker);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);


        Locale.setDefault(Locale.US);
        month = (GregorianCalendar) Calendar.getInstance();
        // calendar instances.
        GregorianCalendar itemmonth = (GregorianCalendar) month.clone();

        items = new ArrayList<>();
        adapter = new CalendarAdapter(this, month);

        strmonth = month.get(Calendar.MONTH) + 1;
        stryear = month.get(Calendar.YEAR);

        db = new DatabaseHelper(this);
        etdate = findViewById(R.id.editTextdt);
        edeventname = findViewById(R.id.edeventname);

        Button selectedDayMonthYearButton = this
                .findViewById(R.id.selectedDayMonthYear);
        selectedDayMonthYearButton.setText("Selected: ");

        tbl = findViewById(R.id.tbl);
        setObjectiveLayout = findViewById(R.id.setObjectiveLayout);

        gridview = findViewById(R.id.gridview);
        gridview.setAdapter(adapter);

        handler = new Handler();
        handler.post(calendarUpdater);

        mProgressDialog = new ProgressDialog(this);

        taskActivity = new DownloadActivity();
        taskSubActivity = new DownloadSubActivity();
        taskList = new PushTaskList();
        taskListRecord = new PushTaskListRecord();
        taskListDetail = new PushTaskListDetail();
        taskListSeq = new PushTaskListSeq();
        taskListBankBranch = new DownloadBankBranch();
        taskListParamList = new DownloadParamList();
        taskListSaveObjective = new DownloadSaveObjective();
        taskBranchProfile = new DownloadBranchProfile();
        taskBranchDeposits = new DownloadBranchDeposits();
        taskBranchAdvances = new DownloadBranchAdvances();
        taskSyncBranchProfile = new DownloadSyncBranchProfile();
        taskRinRaksha = new DownloadRinRaksha();
        taskBdm_Dashboard = new DownloadBdm_Dashboard();
        taskBdm_mail_data = new DownloadBdm_mail_data();
        taskLead = new DownloadLead();

        selActivity = findViewById(R.id.selActivity);

        mCommonMethods = new CommonMethods();

        DatabaseHelper dbhelper = new DatabaseHelper(this);
        String username = dbhelper.GetUserName();

        mCommonMethods.setApplicationToolbarMenu(this, "BDM Tracker");

        selBranch = findViewById(R.id.selBranch);

        // for define objective
        selCommitment = findViewById(R.id.selCommitment);
        selAchievement = findViewById(R.id.selAchievement);
        selPerAchievement = findViewById(R.id.selPerAchievement);
        edcommitbname = findViewById(R.id.edcommitbname);

        edachievebrname = findViewById(R.id.edachievebrname);
        edperachievebrname = findViewById(R.id.edperachievebrname);

        edcommitnewbusinesscash = findViewById(R.id.edcommitnewbusinesscash);
        edcommithomeloan = findViewById(R.id.edcommithomeloan);
        edcommitnewbusinesspre = findViewById(R.id.edcommitnewbusinesspre);
        edcommitsharesingle = findViewById(R.id.edcommitsharesingle);
        edcommitremark = findViewById(R.id.edcommitremark);

        edcommitnewbusinesstot = findViewById(R.id.edcommitnewbusinesstot);
        edcommithomeloantot = findViewById(R.id.edcommithomeloantot);
        edcommitnewbusinesspretot = findViewById(R.id.edcommitnewbusinesspretot);
        edcommitsharesingletot = findViewById(R.id.edcommitsharesingletot);
        edcommitremarktot = findViewById(R.id.edcommitremarktot);

        txtcommtnewbuscash = findViewById(R.id.txtcommtnewbuscash);
        txtcommtshare = findViewById(R.id.txtcommtshare);
        txtcommtnewbuspre = findViewById(R.id.txtcommtnewbuspre);
        txtcommthomeloan = findViewById(R.id.txtcommthomeloan);
        txtachnewbuscash = findViewById(R.id.txtachnewbuscash);
        txtachshare = findViewById(R.id.txtachshare);
        txtachnewbuspre = findViewById(R.id.txtachnewbuspre);
        txtachhomeloan = findViewById(R.id.txtachhomeloan);
        txtperachnewbuscash = findViewById(R.id.txtperachnewbuscash);
        txtperachshare = findViewById(R.id.txtperachshare);
        txtperachnewbuspre = findViewById(R.id.txtperachnewbuspre);
        txtperachhomeloan = findViewById(R.id.txtperachhomeloan);

        currencyFormat = new DecimalFormat("##,##,##,###.##");

        // for branch profile
        selbranchprofilecode = findViewById(R.id.selbranchprofilecode);
        seldeposit = findViewById(R.id.seldeposit);
        seldepositcategory = findViewById(R.id.seldepositcategory);
        seladvances = findViewById(R.id.seladvances);
        seladvancescategory = findViewById(R.id.seladvancescategory);

        tblrdeposit = findViewById(R.id.tblrdeposit);
        tblradvances = findViewById(R.id.tblradvances);

        btndeposits = findViewById(R.id.btndeposits);
        btnadvances = findViewById(R.id.btnadvances);

        edbranchname = findViewById(R.id.edbranchname);
        edbranchopendate = findViewById(R.id.edbranchopendate);
        edbranchnetresult = findViewById(R.id.edbranchnetresult);

        eddepositretail = findViewById(R.id.eddepositretail);
        edadvancesretail = findViewById(R.id.edadvancesretail);

        eddepositcrosssel = findViewById(R.id.eddepositcrosssel);
        eddepositcrossselnop = findViewById(R.id.eddepositcrossselnop);
        eddepositcrossselpot = findViewById(R.id.eddepositcrossselpot);

        edadvancescrosssel = findViewById(R.id.edadvancescrosssel);
        edadvancescrossselnop = findViewById(R.id.edadvancescrossselnop);
        edadvancescrossselpot = findViewById(R.id.edadvancescrossselpot);

        edachievenewbusinesscash = findViewById(R.id.edachievenewbusinesscash);
        edachievehomeloan = findViewById(R.id.edachievehomeloan);
        edachievenewbusinesspre = findViewById(R.id.edachievenewbusinesspre);
        edachievesharesingle = findViewById(R.id.edachievesharesingle);

        edperachievenewbusiness = findViewById(R.id.edperachievenewbusiness);
        edperachieveshare = findViewById(R.id.edperachieveshare);
        edperachievenewbusinesspre = findViewById(R.id.edperachievenewbusinesspre);
        edperachievehomeloan = findViewById(R.id.edperachievehomeloan);

        // TextView title = (TextView) findViewById(R.id.title);
        title = findViewById(R.id.title);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

        lnplanner = findViewById(R.id.lnplanner);
        lnrecordactivity = findViewById(R.id.lnrecordactivity);
        lnmailer = findViewById(R.id.lnmailer);
        lnbranchprofile = findViewById(R.id.lnbranchprofile);

        lnleadmgt = findViewById(R.id.lnleadmgt);

        todaysActiviy = findViewById(R.id.todaysActiviy);

        todaysActiviy.setText(Html.fromHtml("<u>Todays Activities</u>"));

        header = findViewById(R.id.header_bdm);
        lnmonthsel = findViewById(R.id.lnmonthsel);

        btnplanner = findViewById(R.id.btnplanner);
        btnactivityrecorder = findViewById(R.id.btnactivityrecorder);
        btndefineobj = findViewById(R.id.btndefineobj);
        btnmailer = findViewById(R.id.btnmailer);
        btnbranchpro = findViewById(R.id.btnbranchpro);

        btnleadmgt = findViewById(R.id.btnleadmgt);

        btnaddactivity = findViewById(R.id.btnaddactivity);
        btnshowactivity = findViewById(R.id.btnshowactivity);

        edremark = findViewById(R.id.edremark);

        btnbmreport = findViewById(R.id.btnbmreport);
        btncifreport = findViewById(R.id.btncifreport);
        btnagmreport = findViewById(R.id.btnagmreport);
        btnrmreport = findViewById(R.id.btnrmreport);

        TextView txttitleactivity = findViewById(R.id.txttitleactivity);
        txttitleactivity.setText(Html.fromHtml("<u>Activities</u>"));

        btnaddemail = findViewById(R.id.btnaddemail);
        btnviewemail = findViewById(R.id.btnviewemail);

        tbladdemail = findViewById(R.id.tbladdemail);
        tblviewemail = findViewById(R.id.tblviewemail);

        spgroup = findViewById(R.id.spgroup);
        spviewgroup = findViewById(R.id.spviewgroup);
        editEmailId = findViewById(R.id.editEmailId);

        lstEmail = findViewById(R.id.lstEmail);

        // activity recorder
        // Activity Recorder
        TableLayout tblRecordActivity = findViewById(R.id.tblRecordActivity);
        selActivityRecordActivity = findViewById(R.id.selActivityRecordActivity);
        selSubActivityRecordActivity = findViewById(R.id.selSubActivityRecordActivity);
        editTextdtRecordActivity = findViewById(R.id.editTextdtRecordActivity);
        tvTimeRecordActivity = findViewById(R.id.tvTimeRecordActivity);
        edremarkRecordActivity = findViewById(R.id.edremarkRecordActivity);
        tvTimeToRecordActivity = findViewById(R.id.tvTimeToRecordActivity);

        // search record
        edfromdate = findViewById(R.id.edfromdate);
        edtodate = findViewById(R.id.edtodate);
        tblsearchactvity = findViewById(R.id.tblsearchactvity);
        btnsearch = findViewById(R.id.btnsearch);
        btnrefresh = findViewById(R.id.btnrefresh);

        Button btn_getcommittotal = findViewById(R.id.btn_getcommittotal);
        Button btnaddObjective = findViewById(R.id.btnaddObjective);
        Button btnsyncObjective = findViewById(R.id.btnsyncObjective);
        ImageButton btndateRecordActivity = findViewById(R.id.btndateRecordActivity);
        Button btnadd_RecordActivity = findViewById(R.id.btnadd_RecordActivity);
        Button btn_saveEmail = findViewById(R.id.btn_saveEmail);
        Button btn_cancelemail = findViewById(R.id.btn_cancelemail);
        Button btnaddBranchProfile = findViewById(R.id.btnaddBranchProfile);
        Button btnsyncBranchProfile = findViewById(R.id.btnsyncBranchProfile);
        ImageButton btndate = findViewById(R.id.btndate);
        Button btnadd = findViewById(R.id.btnadd);
        Button btncancel = findViewById(R.id.btncancel);
        ImageButton btndateone = findViewById(R.id.btndateone);
        ImageButton btntodate = findViewById(R.id.btntodate);

        selActivityTime = findViewById(R.id.selActivityTime);
        edlead = findViewById(R.id.edlead);
        edlead_act = findViewById(R.id.edlead_act);


        btnleadfromho = findViewById(R.id.btnleadfromho);
        btnselflead = findViewById(R.id.btnselflead);

        edlead.setText("0");
        edlead_act.setText("0");


        //HO Lead
        lnholead = findViewById(R.id.lnholead);
        lvholead = findViewById(R.id.lvholead);
        spsrholead = findViewById(R.id.spsrholead);
        spsrholeadpriority = findViewById(R.id.spsrholeadpriority);
        spsrholeadstatus = findViewById(R.id.spsrholeadstatus);
        Button btnsrrefrholead = findViewById(R.id.btnsrrefrholead);
        Button btnsrsearchholeaddate = findViewById(R.id.btnsrsearchholeaddate);
        Button btnsrsearchhofoldate = findViewById(R.id.btnsrsearchhofoldate);
        Button btnsrsearchholeadpr = findViewById(R.id.btnsrsearchholeadpr);
        Button btnsrsearchholeadstatus = findViewById(R.id.btnsrsearchholeadstatus);
        tblsearchholead = findViewById(R.id.tblsearchholead);
        tblsrholddate = findViewById(R.id.tblsrholddate);
        tblsrholdfolwdate = findViewById(R.id.tblsrholdfolwdate);
        tblsrholdpri = findViewById(R.id.tblsrholdpri);
        tblsrholdsts = findViewById(R.id.tblsrholdsts);
        edsrholeadfdate = findViewById(R.id.edsrholeadfdate);
        edsrholeadtdate = findViewById(R.id.edsrholeadtdate);
        edsrhofolfdate = findViewById(R.id.edsrhofolfdate);
        edsrhofoltdate = findViewById(R.id.edsrhofoltdate);
        ImageButton imgbtnsrholeadfdate = findViewById(R.id.imgbtnsrholeadfdate);
        ImageButton imgbtnsrholeadtdate = findViewById(R.id.imgbtnsrholeadtdate);
        ImageButton imgbtnsrhofolfdate = findViewById(R.id.imgbtnsrhofolfdate);
        ImageButton imgbtnsrhofoltdate = findViewById(R.id.imgbtnsrhofoltdate);


        //tbdm dob as a password

        tblchecktbdmdob = findViewById(R.id.tblchecktbdmdob);
        edtbdmdob = findViewById(R.id.edtbdmdob);
        ImageView btntbdmdobdate = findViewById(R.id.btntbdmdobdate);
        Button btn_check_dob = findViewById(R.id.btn_check_dob);

        //end tbdm dob as a password

        btntbdmdobdate.setOnClickListener(new OnClickListener() {

            //@Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                datecheck = 7;
                showDialog(DATE_DIALOG_ID);
            }
        });

        btn_check_dob.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                String pass = edtbdmdob.getText().toString();

                if (pass.equalsIgnoreCase("")) {
                    Toast.makeText(context, "Please Enter DOB..", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        strTBDMDOBPassword = db.checkDOB(SimpleCrypto.encrypt("SBIL", strTpDob).trim());
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    if (strTBDMDOBPassword != "") {
                        lnleadmgt.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        lnleadmgt.requestLayout();
                    } else {
                        BDMDOBAlert();
                    }

                }
            }
        });

        btnsrrefrholead.setOnClickListener(new OnClickListener() {

            //@Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                GetAllLead();
            }
        });

        spsrholead.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long id) {
                strsrholeadfilter = spsrholead.getSelectedItem().toString();
                if (strsrholeadfilter.contentEquals("Lead Date")) {
                    tblsrholddate.setVisibility(View.VISIBLE);
                    tblsrholdfolwdate.setVisibility(View.GONE);
                    tblsrholdpri.setVisibility(View.GONE);
                    tblsrholdsts.setVisibility(View.GONE);
                } else if (strsrholeadfilter.contentEquals("Lead Priority")) {
                    tblsrholddate.setVisibility(View.GONE);
                    tblsrholdfolwdate.setVisibility(View.GONE);
                    tblsrholdpri.setVisibility(View.VISIBLE);
                    tblsrholdsts.setVisibility(View.GONE);
                } else if (strsrholeadfilter.contentEquals("Lead Status")) {
                    tblsrholddate.setVisibility(View.GONE);
                    tblsrholdfolwdate.setVisibility(View.GONE);
                    tblsrholdpri.setVisibility(View.GONE);
                    tblsrholdsts.setVisibility(View.VISIBLE);
                } else if (strsrholeadfilter.contentEquals("Follow Up Date")) {
                    tblsrholddate.setVisibility(View.GONE);
                    tblsrholdfolwdate.setVisibility(View.VISIBLE);
                    tblsrholdpri.setVisibility(View.GONE);
                    tblsrholdsts.setVisibility(View.GONE);
                }

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


        imgbtnsrholeadfdate.setOnClickListener(new OnClickListener() {

            //@Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                datecheck = 3;
                onCreateDialog(DATE_DIALOG_ID);
            }
        });

        imgbtnsrholeadtdate.setOnClickListener(new OnClickListener() {

            //@Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                datecheck = 4;
                onCreateDialog(DATE_DIALOG_ID);
            }
        });

        imgbtnsrhofolfdate.setOnClickListener(new OnClickListener() {

            //@Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                datecheck = 5;
                onCreateDialog(DATE_DIALOG_ID);
            }
        });

        imgbtnsrhofoltdate.setOnClickListener(new OnClickListener() {

            //@Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                datecheck = 6;
                onCreateDialog(DATE_DIALOG_ID);
            }
        });

        btnsrsearchholeaddate.setOnClickListener(new OnClickListener() {

            //@Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                String strFromDate = edsrholeadfdate.getText().toString();
                String strToDate = edsrholeadtdate.getText().toString();
                if (strFromDate.equalsIgnoreCase("")
                        || strToDate.equalsIgnoreCase("")) {
                    //validation();
                    Toast.makeText(context, "All Fields Required..", Toast.LENGTH_LONG).show();
                } else {
                    // final SimpleDateFormat formatter = new
                    // SimpleDateFormat("dd-MMMM-yyyy");
                    final SimpleDateFormat formatter = new SimpleDateFormat(
                            "dd-MMMM-yyyy", Locale.ENGLISH);
                    final SimpleDateFormat df = new SimpleDateFormat(
                            "yyyy-MM-dd", Locale.ENGLISH);

                    String strfromdate = edsrholeadfdate.getText().toString();
                    String strtodate = edsrholeadtdate.getText().toString();


                    Date d1 = null;
                    try {
                        d1 = formatter.parse(strfromdate);
                        strfromdate = df.format(d1);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    Date d2 = null;
                    try {
                        d2 = formatter.parse(strtodate);
                        strtodate = df.format(d2);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    final SimpleDateFormat formatter1 = new SimpleDateFormat(
                            "yyyy", Locale.ENGLISH);

                    Integer i = Integer.parseInt(formatter1.format(d2))
                            - Integer.parseInt(formatter1.format(d1));
                    String str = String.valueOf(i);

                    if (str.contains("-")) {
                        //errordateselect();
                        Toast.makeText(context, "From Date is Not Greater Than To Date..", Toast.LENGTH_LONG).show();
                    } else {
                        final SimpleDateFormat formatter2 = new SimpleDateFormat(
                                "MM", Locale.ENGLISH);

                        Integer ii = Integer.parseInt(formatter2.format(d2))
                                - Integer.parseInt(formatter2.format(d1));
                        String strm = String.valueOf(ii);

                        if (strm.contains("-")) {
                            //errordateselect();
                            Toast.makeText(context, "From Date is Not Greater Than To Date..", Toast.LENGTH_LONG).show();
                        } else {
                            final SimpleDateFormat formatter3 = new SimpleDateFormat(
                                    "dd", Locale.ENGLISH);

                            Integer i3 = Integer.parseInt(formatter3.format(d2))
                                    - Integer.parseInt(formatter3.format(d1));
                            String strm3 = String.valueOf(i3);

                            if (strm3.contains("-")) {
                                //errordateselect();
                                Toast.makeText(context, "From Date is Not Greater Than To Date..", Toast.LENGTH_LONG).show();
                            } else {
                                Cursor c1 = db.GetDataBetweenTowHODate(strfromdate, strtodate, strCIFBDMUserId);

                                String strLeadDate = "";
                                String strCustId = "";
                                String strCustName = "";
                                String strLeadPriority = "";
                                String strLeadStatus = "";
                                String strLeadSubStatus = "";
                                String strProposalNo = "";
                                String strFollowupdate = "";
                                String strComments = "";
                                String strAge = "";
                                String strTotalAcc = "";
                                String strBalance = "";
                                String strBranchCode = "";
                                String strSyncStatus = "";
                                String strLeadID = "";
                                String strBDMName = "";
                                String strSource = "";

                                lsthoLead.clear();

                                if (c1.getCount() > 0) {
                                    c1.moveToFirst();
                                    for (int iv = 0; iv < c1.getCount(); iv++) {
                                        clsHOLead objClshoLead = new clsHOLead(strLeadDate, strCustId, strCustName, strLeadPriority, strLeadStatus, strLeadSubStatus, strProposalNo, strFollowupdate, strComments, strAge, strTotalAcc, strBalance, strBranchCode, strCIFBDMUserId, strSyncStatus, strLeadID, strBDMName, strSource);

                                        String strdate1 = c1.getString(c1.getColumnIndex("HOLeadDate"));

                                        Date dt1 = null;
                                        try {
                                            dt1 = df.parse(strdate1);
                                        } catch (ParseException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                        strdate1 = dateFormat.format(dt1);


                                        String strfdate = c1.getString(c1.getColumnIndex("HOLeadFollowUpDate"));

                                        if (!strfdate.equalsIgnoreCase("")) {
                                            Date dt2 = null;
                                            try {
                                                dt2 = df.parse(strfdate);
                                            } catch (ParseException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }
                                            strfdate = dateFormat.format(dt2);
                                        }

                                        objClshoLead.set_date(strdate1);
                                        objClshoLead.set_custid(c1.getString(c1.getColumnIndex("HOLeadCustomerId")));
                                        objClshoLead.set_custname(c1.getString(c1.getColumnIndex("HOLeadCustomerName")));
                                        objClshoLead.set_priority(c1.getString(c1.getColumnIndex("HOLeadPriority")));
                                        objClshoLead.set_status(c1.getString(c1.getColumnIndex("HOLeadStatus")));
                                        objClshoLead.set_substatus(c1.getString(c1.getColumnIndex("HOLeadSubStatus")));
                                        objClshoLead.set_proposalno(c1.getString(c1.getColumnIndex("HOLeadProposalNo")));
                                        //objClshoLead.set_followupdate(c1.getString(c1.getColumnIndex("HOLeadFollowUpDate")));
                                        objClshoLead.set_followupdate(strfdate);
                                        objClshoLead.set_comments(c1.getString(c1.getColumnIndex("HOLeadComments")));
                                        objClshoLead.set_age(c1.getString(c1.getColumnIndex("HOLeadAge")));
                                        objClshoLead.set_totalacc(c1.getString(c1.getColumnIndex("HOLeadTotalAcc")));
                                        objClshoLead.set_balance(c1.getString(c1.getColumnIndex("HOLeadBalance")));
                                        objClshoLead.set_branchcode(c1.getString(c1.getColumnIndex("HOLeadBranchCode")));
                                        objClshoLead.set_sync(c1.getString(c1.getColumnIndex("HOLeadSync")));
                                        objClshoLead.set_userid(c1.getString(c1.getColumnIndex("HOLeadUserID")));
                                        objClshoLead.set_name(c1.getString(c1.getColumnIndex("HOLeadBDMName")));
                                        objClshoLead.set_leadid(c1.getString(c1.getColumnIndex("HOLeadServerID")));
                                        lsthoLead.add(objClshoLead);
                                        c1.moveToNext();
                                    }
                                }

                                if (lsthoLead.size() > 0) {
                                    ItemsAdapter_HOLead adapter = new ItemsAdapter_HOLead(context, 0, lsthoLead);
                                    adapter.setNotifyOnChange(true);
                                    lvholead.setAdapter(adapter);
                                    setTodaysList(lvholead);

                                } else {
                                    ArrayList<clsHOLead> lsthoLead = new ArrayList<>();

                                    ItemsAdapter_HOLead adapter = new ItemsAdapter_HOLead(context, 0, lsthoLead);
                                    adapter.setNotifyOnChange(true);
                                    lvholead.setAdapter(adapter);
                                    setTodaysList(lvholead);
                                }
                            }

                        }
                    }
                }

            }
        });

        btnsrsearchhofoldate.setOnClickListener(new OnClickListener() {

            //@Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                String strFromDate = edsrhofolfdate.getText().toString();
                String strToDate = edsrhofoltdate.getText().toString();
                if (strFromDate.equalsIgnoreCase("")
                        || strToDate.equalsIgnoreCase("")) {
                    //validation();
                    Toast.makeText(context, "All Fields Required..", Toast.LENGTH_LONG).show();
                } else {
                    // final SimpleDateFormat formatter = new
                    // SimpleDateFormat("dd-MMMM-yyyy");
                    final SimpleDateFormat formatter = new SimpleDateFormat(
                            "dd-MMMM-yyyy", Locale.ENGLISH);
                    final SimpleDateFormat df = new SimpleDateFormat(
                            "yyyy-MM-dd", Locale.ENGLISH);

                    String strfromdate = edsrhofolfdate.getText().toString();
                    String strtodate = edsrhofoltdate.getText().toString();


                    Date d1 = null;
                    try {
                        d1 = formatter.parse(strfromdate);
                        strfromdate = df.format(d1);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    Date d2 = null;
                    try {
                        d2 = formatter.parse(strtodate);
                        strtodate = df.format(d2);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    final SimpleDateFormat formatter1 = new SimpleDateFormat(
                            "yyyy", Locale.ENGLISH);

                    Integer i = Integer.parseInt(formatter1.format(d2))
                            - Integer.parseInt(formatter1.format(d1));
                    String str = String.valueOf(i);

                    if (str.contains("-")) {
                        //errordateselect();
                        Toast.makeText(context, "From Date is Not Greater Than To Date..", Toast.LENGTH_LONG).show();
                    } else {
                        final SimpleDateFormat formatter2 = new SimpleDateFormat(
                                "MM", Locale.ENGLISH);

                        Integer ii = Integer.parseInt(formatter2.format(d2))
                                - Integer.parseInt(formatter2.format(d1));
                        String strm = String.valueOf(ii);

                        if (strm.contains("-")) {
                            //errordateselect();
                            Toast.makeText(context, "From Date is Not Greater Than To Date..", Toast.LENGTH_LONG).show();
                        } else {
                            final SimpleDateFormat formatter3 = new SimpleDateFormat(
                                    "dd", Locale.ENGLISH);

                            Integer i3 = Integer.parseInt(formatter3.format(d2))
                                    - Integer.parseInt(formatter3.format(d1));
                            String strm3 = String.valueOf(i3);

                            if (strm3.contains("-")) {
                                //errordateselect();
                                Toast.makeText(context, "From Date is Not Greater Than To Date..", Toast.LENGTH_LONG).show();
                            } else {
                                Cursor c1 = db.GetDataBetweenTowFollowUpDate(strfromdate, strtodate, strCIFBDMUserId);

                                String strLeadDate = "";
                                String strCustId = "";
                                String strCustName = "";
                                String strLeadPriority = "";
                                String strLeadStatus = "";
                                String strLeadSubStatus = "";
                                String strProposalNo = "";
                                String strFollowupdate = "";
                                String strComments = "";
                                String strAge = "";
                                String strTotalAcc = "";
                                String strBalance = "";
                                String strBranchCode = "";
                                String strSyncStatus = "";
                                String strLeadID = "";
                                String strBDMName = "";
                                String strSource = "";

                                lsthoLead.clear();

                                if (c1.getCount() > 0) {
                                    c1.moveToFirst();
                                    for (int iv = 0; iv < c1.getCount(); iv++) {
                                        clsHOLead objClshoLead = new clsHOLead(strLeadDate, strCustId, strCustName, strLeadPriority, strLeadStatus, strLeadSubStatus, strProposalNo, strFollowupdate, strComments, strAge, strTotalAcc, strBalance, strBranchCode, strCIFBDMUserId, strSyncStatus, strLeadID, strBDMName, strSource);


                                        String strdate2 = c1.getString(c1.getColumnIndex("HOLeadDate"));

                                        Date dt2 = null;
                                        try {
                                            dt2 = df.parse(strdate2);
                                        } catch (ParseException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                        strdate2 = dateFormat.format(dt2);


                                        String strdate1 = c1.getString(c1.getColumnIndex("HOLeadFollowUpDate"));

                                        if (!strdate1.equalsIgnoreCase("")) {

                                            Date dt1 = null;
                                            try {
                                                dt1 = df.parse(strdate1);
                                            } catch (ParseException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }
                                            strdate1 = dateFormat.format(dt1);

                                        }

                                        //objClshoLead.set_date(c1.getString(c1.getColumnIndex("HOLeadDate")));
                                        objClshoLead.set_date(strdate2);
                                        objClshoLead.set_custid(c1.getString(c1.getColumnIndex("HOLeadCustomerId")));
                                        objClshoLead.set_custname(c1.getString(c1.getColumnIndex("HOLeadCustomerName")));
                                        objClshoLead.set_priority(c1.getString(c1.getColumnIndex("HOLeadPriority")));
                                        objClshoLead.set_status(c1.getString(c1.getColumnIndex("HOLeadStatus")));
                                        objClshoLead.set_substatus(c1.getString(c1.getColumnIndex("HOLeadSubStatus")));
                                        objClshoLead.set_proposalno(c1.getString(c1.getColumnIndex("HOLeadProposalNo")));
                                        objClshoLead.set_followupdate(strdate1);
                                        objClshoLead.set_comments(c1.getString(c1.getColumnIndex("HOLeadComments")));
                                        objClshoLead.set_age(c1.getString(c1.getColumnIndex("HOLeadAge")));
                                        objClshoLead.set_totalacc(c1.getString(c1.getColumnIndex("HOLeadTotalAcc")));
                                        objClshoLead.set_balance(c1.getString(c1.getColumnIndex("HOLeadBalance")));
                                        objClshoLead.set_branchcode(c1.getString(c1.getColumnIndex("HOLeadBranchCode")));
                                        objClshoLead.set_sync(c1.getString(c1.getColumnIndex("HOLeadSync")));
                                        objClshoLead.set_userid(c1.getString(c1.getColumnIndex("HOLeadUserID")));
                                        objClshoLead.set_name(c1.getString(c1.getColumnIndex("HOLeadBDMName")));
                                        objClshoLead.set_leadid(c1.getString(c1.getColumnIndex("HOLeadServerID")));
                                        lsthoLead.add(objClshoLead);
                                        c1.moveToNext();
                                    }
                                }

                                if (lsthoLead.size() > 0) {
                                    ItemsAdapter_HOLead adapter = new ItemsAdapter_HOLead(context, 0, lsthoLead);
                                    adapter.setNotifyOnChange(true);
                                    lvholead.setAdapter(adapter);
                                    setTodaysList(lvholead);

                                } else {
                                    ArrayList<clsHOLead> lsthoLead = new ArrayList<>();

                                    ItemsAdapter_HOLead adapter = new ItemsAdapter_HOLead(context, 0, lsthoLead);
                                    adapter.setNotifyOnChange(true);
                                    lvholead.setAdapter(adapter);
                                    setTodaysList(lvholead);
                                }
                            }

                        }
                    }
                }
            }
        });

        btnsrsearchholeadpr.setOnClickListener(new OnClickListener() {

            //@Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                String strPriority = spsrholeadpriority.getSelectedItem().toString();

                Cursor c1 = db.GetAllLead_BasedOnPriority(strCIFBDMUserId, strPriority);

                String strLeadDate = "";
                String strCustId = "";
                String strCustName = "";
                String strLeadPriority = "";
                String strLeadStatus = "";
                String strLeadSubStatus = "";
                String strProposalNo = "";
                String strFollowupdate = "";
                String strComments = "";
                String strAge = "";
                String strTotalAcc = "";
                String strBalance = "";
                String strBranchCode = "";
                String strSyncStatus = "";
                String strLeadID = "";
                String strBDMName = "";
                String strSource = "";

                lsthoLead.clear();

                if (c1.getCount() > 0) {
                    c1.moveToFirst();
                    for (int ii = 0; ii < c1.getCount(); ii++) {
                        clsHOLead objClshoLead = new clsHOLead(strLeadDate, strCustId, strCustName, strLeadPriority, strLeadStatus, strLeadSubStatus, strProposalNo, strFollowupdate, strComments, strAge, strTotalAcc, strBalance, strBranchCode, strCIFBDMUserId, strSyncStatus, strLeadID, strBDMName, strSource);
                        //objClshoLead.set_date(c1.getString(c1.getColumnIndex("HOLeadDate")));


                        String strfromdate = c1.getString(c1.getColumnIndex("HOLeadDate"));

                        Date dt1 = null;
                        try {
                            dt1 = df.parse(strfromdate);
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        strfromdate = dateFormat.format(dt1);


                        String strfdate = c1.getString(c1.getColumnIndex("HOLeadFollowUpDate"));
                        if (!strfdate.equalsIgnoreCase("")) {
                            Date dt2 = null;
                            try {
                                dt2 = df.parse(strfdate);
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            strfdate = dateFormat.format(dt2);
                        }

                        objClshoLead.set_date(strfromdate);

                        objClshoLead.set_custid(c1.getString(c1.getColumnIndex("HOLeadCustomerId")));
                        objClshoLead.set_custname(c1.getString(c1.getColumnIndex("HOLeadCustomerName")));
                        objClshoLead.set_priority(c1.getString(c1.getColumnIndex("HOLeadPriority")));
                        objClshoLead.set_status(c1.getString(c1.getColumnIndex("HOLeadStatus")));
                        objClshoLead.set_substatus(c1.getString(c1.getColumnIndex("HOLeadSubStatus")));
                        objClshoLead.set_proposalno(c1.getString(c1.getColumnIndex("HOLeadProposalNo")));
                        //objClshoLead.set_followupdate(c1.getString(c1.getColumnIndex("HOLeadFollowUpDate")));
                        objClshoLead.set_followupdate(strfdate);
                        objClshoLead.set_comments(c1.getString(c1.getColumnIndex("HOLeadComments")));
                        objClshoLead.set_age(c1.getString(c1.getColumnIndex("HOLeadAge")));
                        objClshoLead.set_totalacc(c1.getString(c1.getColumnIndex("HOLeadTotalAcc")));
                        objClshoLead.set_balance(c1.getString(c1.getColumnIndex("HOLeadBalance")));
                        objClshoLead.set_branchcode(c1.getString(c1.getColumnIndex("HOLeadBranchCode")));
                        objClshoLead.set_sync(c1.getString(c1.getColumnIndex("HOLeadSync")));
                        objClshoLead.set_userid(c1.getString(c1.getColumnIndex("HOLeadUserID")));
                        objClshoLead.set_name(c1.getString(c1.getColumnIndex("HOLeadBDMName")));
                        objClshoLead.set_leadid(c1.getString(c1.getColumnIndex("HOLeadServerID")));

                        lsthoLead.add(objClshoLead);
                        c1.moveToNext();
                    }
                }

                if (lsthoLead.size() > 0) {
                    ItemsAdapter_HOLead adapter = new ItemsAdapter_HOLead(context, 0, lsthoLead);
                    adapter.setNotifyOnChange(true);
                    lvholead.setAdapter(adapter);
                    setTodaysList(lvholead);

                } else {
                    ArrayList<clsHOLead> lsthoLead = new ArrayList<>();

                    ItemsAdapter_HOLead adapter = new ItemsAdapter_HOLead(context, 0, lsthoLead);
                    adapter.setNotifyOnChange(true);
                    lvholead.setAdapter(adapter);
                    setTodaysList(lvholead);
                }
            }
        });

        btnsrsearchholeadstatus.setOnClickListener(new OnClickListener() {

            //@Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                String strStatus = spsrholeadstatus.getSelectedItem().toString();

                Cursor c1 = db.GetAllLead_BasedOnStatus(strCIFBDMUserId, strStatus);

                String strLeadDate = "";
                String strCustId = "";
                String strCustName = "";
                String strLeadPriority = "";
                String strLeadStatus = "";
                String strLeadSubStatus = "";
                String strProposalNo = "";
                String strFollowupdate = "";
                String strComments = "";
                String strAge = "";
                String strTotalAcc = "";
                String strBalance = "";
                String strBranchCode = "";
                String strSyncStatus = "";
                String strLeadID = "";
                String strBDMName = "";
                String strSource = "";

                lsthoLead.clear();

                if (c1.getCount() > 0) {
                    c1.moveToFirst();
                    for (int ii = 0; ii < c1.getCount(); ii++) {
                        clsHOLead objClshoLead = new clsHOLead(strLeadDate, strCustId, strCustName, strLeadPriority, strLeadStatus, strLeadSubStatus, strProposalNo, strFollowupdate, strComments, strAge, strTotalAcc, strBalance, strBranchCode, strCIFBDMUserId, strSyncStatus, strLeadID, strBDMName, strSource);
                        //objClshoLead.set_date(c1.getString(c1.getColumnIndex("HOLeadDate")));
                        String strfromdate = c1.getString(c1.getColumnIndex("HOLeadDate"));

                        Date dt1 = null;
                        try {
                            dt1 = df.parse(strfromdate);
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        strfromdate = dateFormat.format(dt1);

                        String strfdate = c1.getString(c1.getColumnIndex("HOLeadFollowUpDate"));
                        if (!strfdate.equalsIgnoreCase("")) {
                            Date dt2 = null;
                            try {
                                dt2 = df.parse(strfdate);
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            strfdate = dateFormat.format(dt2);
                        }

                        objClshoLead.set_date(strfromdate);
                        objClshoLead.set_custid(c1.getString(c1.getColumnIndex("HOLeadCustomerId")));
                        objClshoLead.set_custname(c1.getString(c1.getColumnIndex("HOLeadCustomerName")));
                        objClshoLead.set_priority(c1.getString(c1.getColumnIndex("HOLeadPriority")));
                        objClshoLead.set_status(c1.getString(c1.getColumnIndex("HOLeadStatus")));
                        objClshoLead.set_substatus(c1.getString(c1.getColumnIndex("HOLeadSubStatus")));
                        objClshoLead.set_proposalno(c1.getString(c1.getColumnIndex("HOLeadProposalNo")));
                        //objClshoLead.set_followupdate(c1.getString(c1.getColumnIndex("HOLeadFollowUpDate")));
                        objClshoLead.set_followupdate(strfdate);
                        objClshoLead.set_comments(c1.getString(c1.getColumnIndex("HOLeadComments")));
                        objClshoLead.set_age(c1.getString(c1.getColumnIndex("HOLeadAge")));
                        objClshoLead.set_totalacc(c1.getString(c1.getColumnIndex("HOLeadTotalAcc")));
                        objClshoLead.set_balance(c1.getString(c1.getColumnIndex("HOLeadBalance")));
                        objClshoLead.set_branchcode(c1.getString(c1.getColumnIndex("HOLeadBranchCode")));
                        objClshoLead.set_sync(c1.getString(c1.getColumnIndex("HOLeadSync")));
                        objClshoLead.set_userid(c1.getString(c1.getColumnIndex("HOLeadUserID")));
                        objClshoLead.set_name(c1.getString(c1.getColumnIndex("HOLeadBDMName")));
                        objClshoLead.set_leadid(c1.getString(c1.getColumnIndex("HOLeadServerID")));

                        lsthoLead.add(objClshoLead);
                        c1.moveToNext();
                    }
                }

                if (lsthoLead.size() > 0) {
                    ItemsAdapter_HOLead adapter = new ItemsAdapter_HOLead(context, 0, lsthoLead);
                    adapter.setNotifyOnChange(true);
                    lvholead.setAdapter(adapter);
                    setTodaysList(lvholead);

                } else {
                    //ArrayList<clsHOLead> lstmain = new ArrayList<clsHOLead>();

                    ItemsAdapter_HOLead adapter = new ItemsAdapter_HOLead(context, 0, lsthoLead);
                    adapter.setNotifyOnChange(true);
                    lvholead.setAdapter(adapter);
                    setTodaysList(lvholead);
                }
            }
        });


        lvholead.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub

                String strLeadDate = lsthoLead.get(position).get_date();
                String strCustId = lsthoLead.get(position).get_custid();
                String strCustName = lsthoLead.get(position).get_custname();
                String strLeadPriority = lsthoLead.get(position).get_priority();
                String strLeadStatus = lsthoLead.get(position).get_status();
                String strLeadSubStatus = lsthoLead.get(position).get_substatus();
                String strProposalNo = lsthoLead.get(position).get_proposalno();
                String strFollowupdate = lsthoLead.get(position).get_followupdate();
                String strComments = lsthoLead.get(position).get_comments();
                String strAge = lsthoLead.get(position).get_age();
                String strTotalAcc = lsthoLead.get(position).get_totalacc();
                String strBalance = lsthoLead.get(position).get_balance();
                String strBranchCode = lsthoLead.get(position).get_branchcode();
                //String strSyncStatus = lsthoLead.get(position).get_sync();
                String strLeadID = lsthoLead.get(position).get_leadid();
                //String strBDMName = lsthoLead.get(position).get_name();
                //String strSource = lsthoLead.get(position).get_source();
                String strUserID = lsthoLead.get(position).get_userid();

                Cursor c2 = db.Get_HOLeadRowId(strUserID, strLeadID);

                ArrayList<String> lsteventa = new ArrayList<>();
                lsteventa.clear();

                if (c2.getCount() > 0) {
                    c2.moveToFirst();
                    for (int ri = 0; ri < c2.getCount(); ri++) {
                        lsteventa.add(c2.getString(c2.getColumnIndex("HOLeadSync")));
                        lsteventa.add(c2.getString(c2.getColumnIndex("HOLeadID")));
                        c2.moveToNext();
                    }

                    String syncflag = lsteventa.get(0);
                    String rowid = lsteventa.get(1);

                    if (syncflag.contentEquals("Open")) {
                        Intent i = new Intent(context, Lead_MgtHO.class);

                        i.putExtra("LeadDate", strLeadDate);
                        i.putExtra("CustId", strCustId);
                        i.putExtra("CustName", strCustName);
                        i.putExtra("LeadPriority", strLeadPriority);
                        i.putExtra("LeadStatus", strLeadStatus);
                        i.putExtra("LeadSubStatus", strLeadSubStatus);
                        i.putExtra("ProposalNo", strProposalNo);
                        i.putExtra("Followupdate", strFollowupdate);
                        i.putExtra("Comments", strComments);
                        i.putExtra("Age", strAge);
                        i.putExtra("TotalAcc", strTotalAcc);
                        i.putExtra("Balance", strBalance);
                        i.putExtra("BranchCode", strBranchCode);
                        i.putExtra("UserID", strUserID);
                        i.putExtra("LeadServerID", strLeadID);
                        i.putExtra("RowID", rowid);

                        startActivity(i);

                    } else {
                        //activityeditalert();
                        Toast.makeText(context, "Sync lead can not be edit..", Toast.LENGTH_LONG).show();
                    }

                }

            }

        });

        //end HO Lead

        btnplanner.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                // search record
                tblsearchactvity.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                tblsearchactvity.requestLayout();
                // end search record

                lnplanner.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                lnplanner.requestLayout();

                /*
                 * tbl.getLayoutParams().height = 300; tbl.requestLayout();
                 */

                todaysActiviy.setVisibility(View.VISIBLE);
                todaylv.setVisibility(View.VISIBLE);
                header.setVisibility(View.VISIBLE);
                gridview.setVisibility(View.VISIBLE);
                lnmonthsel.setVisibility(View.VISIBLE);
                lv.setVisibility(View.VISIBLE);

                setObjectiveLayout.getLayoutParams().height = 0;
                setObjectiveLayout.requestLayout();


                lnrecordactivity.setVisibility(View.GONE);

                lnbranchprofile.getLayoutParams().height = 0;
                lnbranchprofile.requestLayout();

                lnmailer.getLayoutParams().height = 0;
                lnmailer.requestLayout();

                lnleadmgt.getLayoutParams().height = 0;
                lnleadmgt.requestLayout();

                tblchecktbdmdob.getLayoutParams().height = 0;
                tblchecktbdmdob.requestLayout();

                tblchecktbdmdob.setVisibility(View.GONE);

                btnleadmgt.setBackgroundResource(R.drawable.exp_unselected);

                btnplanner.setBackgroundResource(R.drawable.exp_selected);
                btnactivityrecorder
                        .setBackgroundResource(R.drawable.exp_unselected);
                btndefineobj.setBackgroundResource(R.drawable.exp_unselected);
                btnmailer.setBackgroundResource(R.drawable.exp_unselected);
                btnbranchpro.setBackgroundResource(R.drawable.exp_unselected);

                btnaddactivity.setBackgroundResource(R.drawable.exp_unselected_sub);
                btnshowactivity
                        .setBackgroundResource(R.drawable.exp_unselected_sub);
            }
        });

        btnactivityrecorder.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                tblsearchactvity.getLayoutParams().height = 0;
                tblsearchactvity.requestLayout();
                timecheck = 1; // when select time from and time to


                Calendar cal = Calendar.getInstance();
                mYearT = cal.get(Calendar.YEAR);
                mMonthT = cal.get(Calendar.MONTH);
                mDayT = cal.get(Calendar.DAY_OF_MONTH);

                y = String.valueOf(mYearT);
                m = String.valueOf(mMonthT + 1);
                d = String.valueOf(mDayT);
                String da = String.valueOf(mDayT);

                m = mCommonMethods.getFullMonthName(m);

                String totaldate = da + "-" + m + "-" + y;

                editTextdtRecordActivity.setText(totaldate);

                final Calendar c = Calendar.getInstance();
                hourT = c.get(Calendar.HOUR_OF_DAY);
                minuteT = c.get(Calendar.MINUTE);

                ampmT = c.get(Calendar.AM_PM);
                if (ampmT == 0) {
                    strampmT = "AM";
                } else {
                    strampmT = "PM";
                }

                tvTimeRecordActivity.setText(new StringBuilder()
                        .append(pad(hourT)).append(":").append(pad(minuteT))
                        .append(" ").append(strampmT));

                tvTimeToRecordActivity.setText(new StringBuilder()
                        .append(pad(hourT)).append(":").append(pad(minuteT))
                        .append(" ").append(strampmT));
                // end current time


                lnrecordactivity.setVisibility(View.VISIBLE);

                displayTodayEventsra();

                tbl.getLayoutParams().height = 0;
                tbl.requestLayout();

                todaysActiviy.setVisibility(View.GONE);
                todaylv.setVisibility(View.GONE);
                header.setVisibility(View.GONE);
                gridview.setVisibility(View.GONE);
                lnmonthsel.setVisibility(View.GONE);
                lv.setVisibility(View.GONE);

                lnplanner.getLayoutParams().height = 0;
                lnplanner.requestLayout();

                setObjectiveLayout.getLayoutParams().height = 0;
                setObjectiveLayout.requestLayout();

                lnbranchprofile.getLayoutParams().height = 0;
                lnbranchprofile.requestLayout();

                lnmailer.getLayoutParams().height = 0;
                lnmailer.requestLayout();

                lnleadmgt.getLayoutParams().height = 0;
                lnleadmgt.requestLayout();

                tblchecktbdmdob.getLayoutParams().height = 0;
                tblchecktbdmdob.requestLayout();
                tblchecktbdmdob.setVisibility(View.GONE);

                lnholead.getLayoutParams().height = 0;
                lnholead.requestLayout();

                btnleadmgt.setBackgroundResource(R.drawable.exp_unselected);

                btnplanner.setBackgroundResource(R.drawable.exp_unselected);
                btnactivityrecorder
                        .setBackgroundResource(R.drawable.exp_selected);
                btndefineobj.setBackgroundResource(R.drawable.exp_unselected);
                btnmailer.setBackgroundResource(R.drawable.exp_unselected);
                btnbranchpro.setBackgroundResource(R.drawable.exp_unselected);

                edlead.setText("0");
            }
        });

        btndefineobj.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                // search record
                tblsearchactvity.getLayoutParams().height = 0;
                tblsearchactvity.requestLayout();
                // end search record

                // setObjectiveLayout.getLayoutParams().height = 1100;
                setObjectiveLayout.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                setObjectiveLayout.requestLayout();

                lnbranchprofile.getLayoutParams().height = 0;
                lnbranchprofile.requestLayout();

                tbl.getLayoutParams().height = 0;
                tbl.requestLayout();

                todaysActiviy.setVisibility(View.GONE);
                todaylv.setVisibility(View.GONE);
                header.setVisibility(View.GONE);
                gridview.setVisibility(View.GONE);
                lnmonthsel.setVisibility(View.GONE);
                lv.setVisibility(View.GONE);

                lnmailer.getLayoutParams().height = 0;
                lnmailer.requestLayout();

                lnrecordactivity.setVisibility(View.GONE);

                lnplanner.getLayoutParams().height = 0;
                lnplanner.requestLayout();

                lnleadmgt.getLayoutParams().height = 0;
                lnleadmgt.requestLayout();

                tblchecktbdmdob.getLayoutParams().height = 0;
                tblchecktbdmdob.requestLayout();
                tblchecktbdmdob.setVisibility(View.GONE);


                btnleadmgt.setBackgroundResource(R.drawable.exp_unselected);

                btnplanner.setBackgroundResource(R.drawable.exp_unselected);
                btnactivityrecorder
                        .setBackgroundResource(R.drawable.exp_unselected);
                btndefineobj.setBackgroundResource(R.drawable.exp_selected);
                btnmailer.setBackgroundResource(R.drawable.exp_unselected);
                btnbranchpro.setBackgroundResource(R.drawable.exp_unselected);
            }
        });

        btnbranchpro.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                // search record
                tblsearchactvity.getLayoutParams().height = 0;
                tblsearchactvity.requestLayout();
                // end search record

                lnbranchprofile.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                lnbranchprofile.requestLayout();

                tbl.getLayoutParams().height = 0;
                tbl.requestLayout();

                todaysActiviy.setVisibility(View.GONE);
                todaylv.setVisibility(View.GONE);
                header.setVisibility(View.GONE);
                gridview.setVisibility(View.GONE);
                lnmonthsel.setVisibility(View.GONE);
                lv.setVisibility(View.GONE);

                lnmailer.getLayoutParams().height = 0;
                lnmailer.requestLayout();

                lnrecordactivity.setVisibility(View.GONE);

                lnplanner.getLayoutParams().height = 0;
                lnplanner.requestLayout();

                setObjectiveLayout.getLayoutParams().height = 0;
                setObjectiveLayout.requestLayout();

                lnleadmgt.getLayoutParams().height = 0;
                lnleadmgt.requestLayout();

                tblchecktbdmdob.getLayoutParams().height = 0;
                tblchecktbdmdob.requestLayout();
                tblchecktbdmdob.setVisibility(View.GONE);

                btnleadmgt.setBackgroundResource(R.drawable.exp_unselected);

                btnplanner.setBackgroundResource(R.drawable.exp_unselected);
                btnactivityrecorder
                        .setBackgroundResource(R.drawable.exp_unselected);
                btndefineobj.setBackgroundResource(R.drawable.exp_unselected);
                btnmailer.setBackgroundResource(R.drawable.exp_unselected);
                btnbranchpro.setBackgroundResource(R.drawable.exp_selected);
            }
        });

        btnmailer.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                btnbmreport.setBackgroundResource(R.drawable.exp_unselected);
                btncifreport.setBackgroundResource(R.drawable.exp_unselected);
                btnagmreport.setBackgroundResource(R.drawable.exp_unselected);
                btnrmreport.setBackgroundResource(R.drawable.exp_unselected);

                lstEmail.setVisibility(View.GONE);
                tbladdemail.getLayoutParams().height = 0;
                tbladdemail.requestLayout();
                tblviewemail.getLayoutParams().height = 0;
                tblviewemail.requestLayout();
                btnviewemail.setBackgroundResource(R.drawable.exp_unselected);
                btnaddemail.setBackgroundResource(R.drawable.exp_unselected);

                // search record
                tblsearchactvity.getLayoutParams().height = 0;
                tblsearchactvity.requestLayout();
                // end search record

                lnmailer.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                lnmailer.requestLayout();

                tbl.getLayoutParams().height = 0;
                tbl.requestLayout();

                todaysActiviy.setVisibility(View.GONE);
                todaylv.setVisibility(View.GONE);
                header.setVisibility(View.GONE);
                gridview.setVisibility(View.GONE);
                lnmonthsel.setVisibility(View.GONE);
                lv.setVisibility(View.GONE);

                lnrecordactivity.setVisibility(View.GONE);

                lnplanner.getLayoutParams().height = 0;
                lnplanner.requestLayout();

                setObjectiveLayout.getLayoutParams().height = 0;
                setObjectiveLayout.requestLayout();

                lnbranchprofile.getLayoutParams().height = 0;
                lnbranchprofile.requestLayout();

                lnleadmgt.getLayoutParams().height = 0;
                lnleadmgt.requestLayout();

                tblchecktbdmdob.getLayoutParams().height = 0;
                tblchecktbdmdob.requestLayout();
                tblchecktbdmdob.setVisibility(View.GONE);

                btnleadmgt.setBackgroundResource(R.drawable.exp_unselected);

                btnplanner.setBackgroundResource(R.drawable.exp_unselected);
                btnactivityrecorder
                        .setBackgroundResource(R.drawable.exp_unselected);
                btndefineobj.setBackgroundResource(R.drawable.exp_unselected);
                btnmailer.setBackgroundResource(R.drawable.exp_selected);
                btnbranchpro.setBackgroundResource(R.drawable.exp_unselected);
            }
        });

        btnleadmgt.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                // search record
                tblsearchactvity.getLayoutParams().height = 0;
                tblsearchactvity.requestLayout();
                // end search record


				/*lnleadmgt.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
				lnleadmgt.requestLayout();*/

                lnleadmgt.getLayoutParams().height = 0;
                lnleadmgt.requestLayout();

                edtbdmdob.setText("");

                tblchecktbdmdob.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                tblchecktbdmdob.requestLayout();

                tblchecktbdmdob.setVisibility(View.VISIBLE);

                lnplanner.getLayoutParams().height = 0;
                lnplanner.requestLayout();

                /*
                 * tbl.getLayoutParams().height = 300; tbl.requestLayout();
                 */

                tbl.getLayoutParams().height = 0;
                tbl.requestLayout();


                todaysActiviy.setVisibility(View.GONE);
                todaylv.setVisibility(View.GONE);
                header.setVisibility(View.GONE);
                gridview.setVisibility(View.GONE);
                lnmonthsel.setVisibility(View.GONE);
                lv.setVisibility(View.GONE);

                setObjectiveLayout.getLayoutParams().height = 0;
                setObjectiveLayout.requestLayout();

                lnrecordactivity.setVisibility(View.GONE);

                lnbranchprofile.getLayoutParams().height = 0;
                lnbranchprofile.requestLayout();

                lnmailer.getLayoutParams().height = 0;
                lnmailer.requestLayout();

                btnleadmgt.setBackgroundResource(R.drawable.exp_selected);

                btnplanner.setBackgroundResource(R.drawable.exp_unselected);
                btnactivityrecorder
                        .setBackgroundResource(R.drawable.exp_unselected);
                btndefineobj.setBackgroundResource(R.drawable.exp_unselected);
                btnmailer.setBackgroundResource(R.drawable.exp_unselected);
                btnbranchpro.setBackgroundResource(R.drawable.exp_unselected);

                btnleadfromho.setBackgroundResource(R.drawable.exp_unselected_sub);
                btnselflead
                        .setBackgroundResource(R.drawable.exp_unselected_sub);
            }
        });

        btnaddactivity.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                // search record
                tblsearchactvity.getLayoutParams().height = 0;
                tblsearchactvity.requestLayout();
                // end search record

                // etdate.setText(""); //when click on add button date is blank
                // coz
                // activity recorder date also assign same

                // set current date in add activity and add record activiti dt

                Calendar cal = Calendar.getInstance();
                mYearT = cal.get(Calendar.YEAR);
                mMonthT = cal.get(Calendar.MONTH);
                mDayT = cal.get(Calendar.DAY_OF_MONTH);

                y = String.valueOf(mYearT);
                m = String.valueOf(mMonthT + 1);
                d = String.valueOf(mDayT);
                String da = String.valueOf(mDayT);
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

                etdate.setText(totaldate);

                // end set date default in edittext in date

                // set current time
                final Calendar c = Calendar.getInstance();
                hourT = c.get(Calendar.HOUR_OF_DAY);
                minuteT = c.get(Calendar.MINUTE);

                ampmT = c.get(Calendar.AM_PM);
                if (ampmT == 0) {
                    strampmT = "AM";
                } else {
                    strampmT = "PM";
                }

                tvDisplayTime.setText(new StringBuilder().append(pad(hourT))
                        .append(":").append(pad(minuteT)).append(" ")
                        .append(strampmT));
                // end set current time

                btnaddactivity.setBackgroundResource(R.drawable.exp_selected_sub);
                btnshowactivity
                        .setBackgroundResource(R.drawable.exp_unselected_sub);

                tbl.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                tbl.requestLayout();

                lv.setVisibility(View.GONE);

                setObjectiveLayout.getLayoutParams().height = 0;
                setObjectiveLayout.requestLayout();

                edlead_act.setText("0");
            }
        });

        btnshowactivity.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                // search record
                btnsearch.setBackgroundResource(R.drawable.exp_unselected);
                btnrefresh.setBackgroundResource(R.drawable.exp_unselected);

                edfromdate.setText("");
                edtodate.setText("");

                tblsearchactvity.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                tblsearchactvity.requestLayout();
                // end search record

                btnaddactivity.setBackgroundResource(R.drawable.exp_unselected_sub);
                btnshowactivity.setBackgroundResource(R.drawable.exp_selected_sub);

                tbl.getLayoutParams().height = 0;
                tbl.requestLayout();

                lv.setVisibility(View.VISIBLE);

                Cursor c1 = db.getAllEventsBDMT(strCIFBDMUserId);

                String strdate = "";
                String strevent = "";
                String strtmonth = "";
                String strtyear = "";
                String strtime = "";
                String strremark = "";
                String stractivity = "";

                String strSubActivity = "";
                String strTimeTo = "";
                String strUserID = "";
                String strStatus = "";
                String strSync = "";

                String strCreatedDate = "";
                String strModifiedDate = "";

                String strServerMasterId = "";

                String strLead = "";

                lstmain.clear();

                if (c1.getCount() > 0) {
                    c1.moveToFirst();
                    for (int ii = 0; ii < c1.getCount(); ii++) {
                        clsBDMTrackerCalendar single = new clsBDMTrackerCalendar(
                                strdate, strevent, strtmonth, strtyear,
                                strtime, strremark, stractivity,
                                strSubActivity, strTimeTo, strUserID,
                                strStatus, strSync, strCreatedDate,
                                strModifiedDate, strServerMasterId, strLead);
                        // single.setDateName(c1.getString(c1.getColumnIndex("DateName")));

                        String strfromdate = c1.getString(c1
                                .getColumnIndex("DateNameBDMT"));

                        Date dt1 = null;
                        try {
                            dt1 = df.parse(strfromdate);
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        strfromdate = dateFormat.format(dt1);

                        single.setDateName(strfromdate);
                        single.setEventName(c1.getString(c1
                                .getColumnIndex("EventNameBDMT")));
                        single.setTime(c1.getString(c1
                                .getColumnIndex("TimeBDMT")));
                        single.setRemark(c1.getString(c1
                                .getColumnIndex("Remark")));
                        single.setActivity(c1.getString(c1
                                .getColumnIndex("Activity")));
                        single.setSubActivity(c1.getString(c1
                                .getColumnIndex("SubActivity")));
                        single.setTimeTo(c1.getString(c1
                                .getColumnIndex("TimeTo")));
                        single.setStatus(c1.getString(c1
                                .getColumnIndex("ActivityStatus")));
                        single.setLead(c1.getString(c1
                                .getColumnIndex("ActivityLead")));
                        lstmain.add(single);
                        c1.moveToNext();
                    }
                }

                if (lstmain.size() > 0) {
                    ItemsAdapter adapter = new ItemsAdapter(context, 0, lstmain);
                    adapter.setNotifyOnChange(true);
                    lv.setAdapter(adapter);
                    setShowallList(lv);
                } else {
                    ArrayList<clsBDMTrackerCalendar> lstmain = new ArrayList<>();

                    ItemsAdapter adapter = new ItemsAdapter(context, 0, lstmain);
                    adapter.setNotifyOnChange(true);
                    lv.setAdapter(adapter);
                    setShowallList(lv);
                }
            }
        });

        btn_getcommittotal.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                Cursor c1 = db.GetDefineObjectiveAllRecord(strCIFBDMUserId);
                ArrayList<clsDefineObjective> lstbc = new ArrayList<>();

                lstbc.clear();

                if (c1.getCount() > 0) {
                    c1.moveToFirst();
                    for (int ri = 0; ri < c1.getCount(); ri++) {

                        clsDefineObjective obj = new clsDefineObjective("", "",
                                "", "", "", "", "", "");

                        obj.setNewBusCash(c1.getString(c1
                                .getColumnIndex("DefineObjectiveNewBusCash")));
                        obj.setHomeLoan(c1.getString(c1
                                .getColumnIndex("DefineObjectiveHomeLoan")));
                        obj.setNewBusPre(c1.getString(c1
                                .getColumnIndex("DefineObjectiveNewBusPre")));
                        obj.setShare(c1.getString(c1
                                .getColumnIndex("DefineObjectiveShare")));
                        obj.setRemark(c1.getString(c1
                                .getColumnIndex("DefineObjectiveRemark")));
                        lstbc.add(obj);
                        c1.moveToNext();
                    }
                }

                double dcash = 0;
                double dloan = 0;
                double dpre = 0;
                double dshare = 0;
                //double dremark = 0;

                for (int i = 0; i < lstbc.size(); i++) {
                    dcash += Double
                            .parseDouble(lstbc.get(i).getNewBusCash() == null ? "0"
                                    : lstbc.get(i).getNewBusCash());
                    dloan = Double
                            .parseDouble(lstbc.get(i).getHomeLoan() == null ? "0"
                                    : lstbc.get(i).getHomeLoan());
                    dpre = Double
                            .parseDouble(lstbc.get(i).getNewBusPre() == null ? "0"
                                    : lstbc.get(i).getNewBusPre());
                    dshare = Double
                            .parseDouble(lstbc.get(i).getShare() == null ? "0"
                                    : lstbc.get(i).getShare());
					/*dremark = Double
							.parseDouble(lstbc.get(i).getRemark() == null ? "0"
									: lstbc.get(i).getRemark());*/
                }

                if (lstbc.size() > 0) {
                    edcommitnewbusinesstot.setText(String.valueOf(dcash));
                    edcommithomeloantot.setText(String.valueOf(dloan));
                    edcommitnewbusinesspretot.setText(String.valueOf(dpre));
                    edcommitsharesingletot.setText(String.valueOf(dshare));
                    //edcommitremarktot.setText(String.valueOf(dremark));
                }
            }
        });

        btnaddObjective.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                /*
                 * setObjectiveLayout.getLayoutParams().height = 0;
                 * setObjectiveLayout.requestLayout();
                 */

                String strbranchname = edcommitbname.getText().toString();

                String strnewBusinesscash = edcommitnewbusinesscash.getText()
                        .toString();
                String strhomeloan = edcommithomeloan.getText().toString();
                String strnewbusinesspre = edcommitnewbusinesspre.getText()
                        .toString();
                String strsharesingle = edcommitsharesingle.getText()
                        .toString();
                String strremark = edcommitremark.getText().toString();

                if (strCode.equalsIgnoreCase("Select Branch Code")
                        || strbranchname.equalsIgnoreCase("Select Branch Name")) {
                    validation();
                } else {
                    if (strnewBusinesscash.equalsIgnoreCase("")
                            && strhomeloan.equalsIgnoreCase("")
                            && strnewbusinesspre.equalsIgnoreCase("")
                            && strsharesingle.equalsIgnoreCase("")) {
                        validation();
                    } else {

                        int count = db.DefineObjectiveExistorNot(strCode,
                                strCIFBDMUserId);
                        if (count == 0) {
                            clsDefineObjective obj = new clsDefineObjective(
                                    strCode, strbranchname, strnewBusinesscash,
                                    strhomeloan, strnewbusinesspre,
                                    strsharesingle, strremark, strCIFBDMUserId);
                            db.AddDefineObjective(obj);

                            savedefineobjectivelert();
                        } else {
                            existdefineobjectivelert();

                            // for update record

                            /*
                             * ArrayList<String> lstevent = new
                             * ArrayList<String>();
                             *
                             * Cursor c = db.GetDefineObjectiveRecord(strCode,
                             * strCIFBDMUserId); if (c.getCount() > 0) {
                             * c.moveToFirst(); for (int ii = 0; ii <
                             * c.getCount(); ii++) { lstevent.add(c.getString(c
                             * .getColumnIndex("DefineObjectiveBranchCode")));
                             * lstevent.add(c.getString(c
                             * .getColumnIndex("DefineObjectiveBranchName")));
                             * lstevent.add(c.getString(c
                             * .getColumnIndex("DefineObjectiveNewBusCash")));
                             * lstevent.add(c.getString(c
                             * .getColumnIndex("DefineObjectiveHomeLoan")));
                             * lstevent.add(c.getString(c
                             * .getColumnIndex("DefineObjectiveNewBusPre")));
                             * lstevent.add(c.getString(c
                             * .getColumnIndex("DefineObjectiveShare")));
                             * lstevent.add(c.getString(c
                             * .getColumnIndex("DefineObjectiveRemark")));
                             * lstevent.add(c.getString(c
                             * .getColumnIndex("DefineObjectiveUserId")));
                             * lstevent.add(c.getString(c
                             * .getColumnIndex("DefineObjectiveID")));
                             * c.moveToNext(); } }
                             *
                             * clsDefineObjective objcla = null; try { objcla =
                             * new clsDefineObjective(lstevent.get(0)
                             * .toString(), lstevent.get(1).toString(),
                             * strnewBusinesscash, strhomeloan,
                             * strnewbusinesspre, strsharesingle, strremark,
                             * lstevent.get(7).toString()); } catch (Exception
                             * e) { // TODO Auto-generated catch block
                             * e.printStackTrace(); }
                             *
                             * db.UpdateDefineObjectiveRecord(objcla,
                             * lstevent.get(8) .toString());
                             *
                             * updatedefineobjectivelert();
                             */

                        }

                    }
                }
            }
        });

        btnsyncObjective.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                /*
                 * setObjectiveLayout.getLayoutParams().height = 0;
                 * setObjectiveLayout.requestLayout();
                 */

                final String strbranchname = edcommitbname.getText().toString();
                final String strnewBusinesscash = edcommitnewbusinesscash
                        .getText().toString();
                final String strhomeloan = edcommithomeloan.getText()
                        .toString();
                final String strnewbusinesspre = edcommitnewbusinesspre
                        .getText().toString();
                final String strsharesingle = edcommitsharesingle.getText()
                        .toString();
                final String strremark = edcommitremark.getText().toString();

                if (!strnewBusinesscash.equalsIgnoreCase("")) {
                    totalparam += 1;
                } else if (!strsharesingle.equalsIgnoreCase("")) {
                    totalparam += 1;
                } else if (!strnewbusinesspre.equalsIgnoreCase("")) {
                    totalparam += 1;
                } else if (!strhomeloan.equalsIgnoreCase("")) {
                    totalparam += 1;
                }

                /*
                 * final String paramid = ""; final int value = 0;
                 */

                if (strCode.equalsIgnoreCase("Select Branch Code")
                        || strbranchname.equalsIgnoreCase("Select Branch Name")) {
                    validation();
                } else {
                    // startDownloadSaveObjective();
                    if (strnewBusinesscash.equalsIgnoreCase("")
                            && strhomeloan.equalsIgnoreCase("")
                            && strnewbusinesspre.equalsIgnoreCase("")
                            && strsharesingle.equalsIgnoreCase("")) {
                        validation();
                    } else {

                        String strFlag = db.GetDefineObjectiveSyncRecord(
                                strCode, strCIFBDMUserId);
                        if (strFlag.contentEquals("True")) {
                            activitysynalert();
                        } else {

                            // final boolean running = true;

                            mProgressDialog = new ProgressDialog(
                                    BancaBDMTracker.this, ProgressDialog.THEME_HOLO_LIGHT);
                            String Message = "Loading. Please wait...";
                            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
                            mProgressDialog
                                    .setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            mProgressDialog.setCancelable(true);

                            mProgressDialog.setButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {

                                            taskActivity.cancel(true);
                                            taskSubActivity.cancel(true);
                                            taskList.cancel(true);
                                            taskListRecord.cancel(true);
                                            taskListDetail.cancel(true);
                                            taskListSeq.cancel(true);
                                            taskListBankBranch.cancel(true);
                                            taskListParamList.cancel(true);
                                            taskListSaveObjective.cancel(true);
                                            taskBranchProfile.cancel(true);
                                            taskBranchDeposits.cancel(true);
                                            taskBranchAdvances.cancel(true);
                                            taskSyncBranchProfile.cancel(true);
                                            taskRinRaksha.cancel(true);
                                            taskBdm_Dashboard.cancel(true);
                                            taskBdm_mail_data.cancel(true);
                                            taskLead.cancel(true);
                                            mProgressDialog.dismiss();
                                        }
                                    });

                            mProgressDialog.setMax(100);
                            mProgressDialog.show();

                            splashTread = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        synchronized (this) {
                                            // wait(_splashTime);
                                        }

                                    } finally {

                                        if (!strnewBusinesscash
                                                .equalsIgnoreCase("")) {
                                            if (defineobjective_running) {


                                                paramid = "1";
                                                value = Integer
                                                        .parseInt(strnewBusinesscash);

                                                // boolean running = true;

                                                SoapObject request = new SoapObject(
                                                        NAMESPACE,
                                                        METHOD_NAME_SAVE_OBJECTIVE);

                                                request.addProperty(
                                                        "objective_Param_mast_id",
                                                        paramid);
                                                request.addProperty("bdmId",
                                                        strCIFBDMUserId);
                                                request.addProperty("brCode",
                                                        strCode);
                                                request.addProperty("brName",
                                                        strbranchname);
                                                request.addProperty(
                                                        "parmaValue", value);
                                                request.addProperty("remarks",
                                                        strremark);
                                                request.addProperty(
                                                        "strEmailId",
                                                        strCIFBDMEmailId);
                                                request.addProperty(
                                                        "strMobileNo",
                                                        strCIFBDMMObileNo);
                                                request.addProperty(
                                                        "strAuthKey",
                                                        strCIFBDMPassword
                                                                .trim());

                                                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                                                        SoapEnvelope.VER11);
                                                envelope.dotNet = true;

                                                envelope.setOutputSoapObject(request);

                                                // 	allowAllSSL();

                                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                                        .permitAll().build();
                                                StrictMode
                                                        .setThreadPolicy(policy);

                                                HttpTransportSE androidHttpTranport = new HttpTransportSE(
                                                        URl);
                                                try {
                                                    try {
                                                        androidHttpTranport
                                                                .call(SOAP_ACTION_SAVE_OBJECTIVE,
                                                                        envelope);
                                                    } catch (XmlPullParserException e) {
                                                        try {
                                                            throw (e);
                                                        } catch (Exception e1) {
                                                            e1.printStackTrace();
                                                        }
                                                        mProgressDialog
                                                                .dismiss();
                                                        defineobjective_running = false;
                                                    }
                                                    Object response = envelope
                                                            .getResponse();
                                                    strResult = response
                                                            .toString();
                                                } catch (IOException e) {
                                                    try {
                                                        throw (e);
                                                    } catch (Exception e1) {
                                                        e1.printStackTrace();
                                                    }
                                                    mProgressDialog.dismiss();
                                                    defineobjective_running = false;
                                                }

                                                if (defineobjective_running) {
                                                    if (!strResult
                                                            .contentEquals("0")) {
                                                        // mProgressDialog.dismiss();
                                                        // mProgressDialog.dismiss();
                                                        // syncerror();

                                                        /*
                                                         * BDMTracker.getActivity
                                                         * () .runOnUiThread(new
                                                         * Runnable() {
                                                         *
                                                         * @Override public void
                                                         * run() { // TODO //
                                                         * Auto-generated //
                                                         * method stub
                                                         * tasksyncerror(); }
                                                         * });
                                                         */
                                                    } else {
                                                        defineobjective_running = false;
                                                        mProgressDialog
                                                                .dismiss();
                                                        // syncerror();

                                                        BancaBDMTracker.this
                                                                .runOnUiThread(new Runnable() {

                                                                    // @Override
                                                                    public void run() {
                                                                        // TODO
                                                                        // Auto-generated
                                                                        // method
                                                                        // stub
                                                                        syncerror();
                                                                    }
                                                                });
                                                    }
                                                } else {
                                                    mProgressDialog.dismiss();
                                                    // syncerror();
                                                    BancaBDMTracker.this
                                                            .runOnUiThread(new Runnable() {

                                                                // @Override
                                                                public void run() {
                                                                    // TODO
                                                                    // Auto-generated
                                                                    // method
                                                                    // stub
                                                                    syncerror();
                                                                }
                                                            });
                                                }
                                            }
                                        }
                                        if (!strsharesingle
                                                .equalsIgnoreCase("")) {
                                            if (defineobjective_running) {

                                                paramid = "2";
                                                value = Integer
                                                        .parseInt(strsharesingle);

                                                // boolean running = true;

                                                SoapObject request = new SoapObject(
                                                        NAMESPACE,
                                                        METHOD_NAME_SAVE_OBJECTIVE);

                                                request.addProperty(
                                                        "objective_Param_mast_id",
                                                        paramid);
                                                request.addProperty("bdmId",
                                                        strCIFBDMUserId);
                                                request.addProperty("brCode",
                                                        strCode);
                                                request.addProperty("brName",
                                                        strbranchname);
                                                request.addProperty(
                                                        "parmaValue", value);
                                                request.addProperty("remarks",
                                                        strremark);
                                                request.addProperty(
                                                        "strEmailId",
                                                        strCIFBDMEmailId);
                                                request.addProperty(
                                                        "strMobileNo",
                                                        strCIFBDMMObileNo);
                                                request.addProperty(
                                                        "strAuthKey",
                                                        strCIFBDMPassword
                                                                .trim());

                                                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                                                        SoapEnvelope.VER11);
                                                envelope.dotNet = true;

                                                envelope.setOutputSoapObject(request);

                                                // 	allowAllSSL();

                                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                                        .permitAll().build();
                                                StrictMode
                                                        .setThreadPolicy(policy);

                                                HttpTransportSE androidHttpTranport = new HttpTransportSE(
                                                        URl);
                                                try {
                                                    try {
                                                        androidHttpTranport
                                                                .call(SOAP_ACTION_SAVE_OBJECTIVE,
                                                                        envelope);
                                                    } catch (XmlPullParserException e) {
                                                        // TODO Auto-generated
                                                        // catch block
                                                        try {
                                                            throw (e);
                                                        } catch (Exception e1) {
                                                            e1.printStackTrace();
                                                        }
                                                        mProgressDialog
                                                                .dismiss();
                                                        defineobjective_running = false;
                                                    }
                                                    Object response = envelope
                                                            .getResponse();
                                                    strResult = response
                                                            .toString();
                                                } catch (IOException e) {
                                                    try {
                                                        throw (e);
                                                    } catch (Exception e1) {
                                                        e1.printStackTrace();
                                                    }
                                                    mProgressDialog.dismiss();
                                                    defineobjective_running = false;
                                                }

                                                if (defineobjective_running) {
                                                    if (!strResult
                                                            .contentEquals("0")) {
                                                        // mProgressDialog.dismiss();
                                                    } else {
                                                        defineobjective_running = false;
                                                        mProgressDialog
                                                                .dismiss();
                                                        // syncerror();
                                                        BancaBDMTracker.this
                                                                .runOnUiThread(new Runnable() {

                                                                    // @Override
                                                                    public void run() {
                                                                        // TODO
                                                                        // Auto-generated
                                                                        // method
                                                                        // stub
                                                                        syncerror();
                                                                    }
                                                                });
                                                    }
                                                } else {
                                                    mProgressDialog.dismiss();
                                                    // syncerror();
                                                    BancaBDMTracker.this
                                                            .runOnUiThread(new Runnable() {

                                                                // @Override
                                                                public void run() {
                                                                    // TODO
                                                                    // Auto-generated
                                                                    // method
                                                                    // stub
                                                                    syncerror();
                                                                }
                                                            });
                                                }
                                            }
                                        }
                                        if (!strnewbusinesspre
                                                .equalsIgnoreCase("")) {
                                            if (defineobjective_running) {

                                                paramid = "3";
                                                value = Integer
                                                        .parseInt(strnewbusinesspre);

                                                // boolean running = true;

                                                SoapObject request = new SoapObject(
                                                        NAMESPACE,
                                                        METHOD_NAME_SAVE_OBJECTIVE);

                                                request.addProperty(
                                                        "objective_Param_mast_id",
                                                        paramid);
                                                request.addProperty("bdmId",
                                                        strCIFBDMUserId);
                                                request.addProperty("brCode",
                                                        strCode);
                                                request.addProperty("brName",
                                                        strbranchname);
                                                request.addProperty(
                                                        "parmaValue", value);
                                                request.addProperty("remarks",
                                                        strremark);
                                                request.addProperty(
                                                        "strEmailId",
                                                        strCIFBDMEmailId);
                                                request.addProperty(
                                                        "strMobileNo",
                                                        strCIFBDMMObileNo);
                                                request.addProperty(
                                                        "strAuthKey",
                                                        strCIFBDMPassword
                                                                .trim());

                                                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                                                        SoapEnvelope.VER11);
                                                envelope.dotNet = true;

                                                envelope.setOutputSoapObject(request);

                                                // 	allowAllSSL();

                                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                                        .permitAll().build();
                                                StrictMode
                                                        .setThreadPolicy(policy);

                                                HttpTransportSE androidHttpTranport = new HttpTransportSE(
                                                        URl);
                                                try {
                                                    try {
                                                        androidHttpTranport
                                                                .call(SOAP_ACTION_SAVE_OBJECTIVE,
                                                                        envelope);
                                                    } catch (XmlPullParserException e) {
                                                        try {
                                                            throw (e);
                                                        } catch (Exception e1) {
                                                            e1.printStackTrace();
                                                        }
                                                        mProgressDialog
                                                                .dismiss();
                                                        defineobjective_running = false;
                                                    }
                                                    Object response = envelope
                                                            .getResponse();
                                                    strResult = response
                                                            .toString();
                                                } catch (IOException e) {
                                                    try {
                                                        throw (e);
                                                    } catch (Exception e1) {
                                                        e1.printStackTrace();
                                                    }
                                                    mProgressDialog.dismiss();
                                                    defineobjective_running = false;
                                                }

                                                if (defineobjective_running) {
                                                    if (!strResult
                                                            .contentEquals("0")) {
                                                        // mProgressDialog.dismiss();
                                                    } else {
                                                        defineobjective_running = false;
                                                        mProgressDialog
                                                                .dismiss();
                                                        // syncerror();
                                                        BancaBDMTracker.this
                                                                .runOnUiThread(new Runnable() {

                                                                    // @Override
                                                                    public void run() {
                                                                        // TODO
                                                                        // Auto-generated
                                                                        // method
                                                                        // stub
                                                                        syncerror();
                                                                    }
                                                                });
                                                    }
                                                } else {
                                                    mProgressDialog.dismiss();
                                                    // syncerror();
                                                    BancaBDMTracker.this
                                                            .runOnUiThread(new Runnable() {

                                                                // @Override
                                                                public void run() {
                                                                    // TODO
                                                                    // Auto-generated
                                                                    // method
                                                                    // stub
                                                                    syncerror();
                                                                }
                                                            });
                                                }
                                            }
                                        }
                                        if (!strhomeloan.equalsIgnoreCase("")) {
                                            if (defineobjective_running) {

                                                paramid = "4";
                                                value = Integer
                                                        .parseInt(strhomeloan);

                                                // boolean running = true;

                                                SoapObject request = new SoapObject(
                                                        NAMESPACE,
                                                        METHOD_NAME_SAVE_OBJECTIVE);

                                                request.addProperty(
                                                        "objective_Param_mast_id",
                                                        paramid);
                                                request.addProperty("bdmId",
                                                        strCIFBDMUserId);
                                                request.addProperty("brCode",
                                                        strCode);
                                                request.addProperty("brName",
                                                        strbranchname);
                                                request.addProperty(
                                                        "parmaValue", value);
                                                request.addProperty("remarks",
                                                        strremark);
                                                request.addProperty(
                                                        "strEmailId",
                                                        strCIFBDMEmailId);
                                                request.addProperty(
                                                        "strMobileNo",
                                                        strCIFBDMMObileNo);
                                                request.addProperty(
                                                        "strAuthKey",
                                                        strCIFBDMPassword
                                                                .trim());

                                                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                                                        SoapEnvelope.VER11);
                                                envelope.dotNet = true;

                                                envelope.setOutputSoapObject(request);

                                                // 	allowAllSSL();

                                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                                        .permitAll().build();
                                                StrictMode
                                                        .setThreadPolicy(policy);

                                                HttpTransportSE androidHttpTranport = new HttpTransportSE(
                                                        URl);
                                                try {
                                                    try {
                                                        androidHttpTranport
                                                                .call(SOAP_ACTION_SAVE_OBJECTIVE,
                                                                        envelope);
                                                    } catch (XmlPullParserException e) {
                                                        try {
                                                            throw (e);
                                                        } catch (Exception e1) {
                                                            e1.printStackTrace();
                                                        }
                                                        mProgressDialog
                                                                .dismiss();
                                                        defineobjective_running = false;
                                                    }
                                                    Object response = envelope
                                                            .getResponse();
                                                    strResult = response
                                                            .toString();
                                                } catch (IOException e) {
                                                    try {
                                                        throw (e);
                                                    } catch (Exception e1) {
                                                        e1.printStackTrace();
                                                    }
                                                    mProgressDialog.dismiss();
                                                    defineobjective_running = false;
                                                }

                                                if (defineobjective_running) {
                                                    if (!strResult
                                                            .contentEquals("0")) {
                                                        // mProgressDialog.dismiss();
                                                    } else {
                                                        defineobjective_running = false;
                                                        mProgressDialog
                                                                .dismiss();
                                                        // syncerror();
                                                        BancaBDMTracker.this
                                                                .runOnUiThread(new Runnable() {

                                                                    // @Override
                                                                    public void run() {
                                                                        // TODO
                                                                        // Auto-generated
                                                                        // method
                                                                        // stub
                                                                        syncerror();
                                                                    }
                                                                });
                                                    }
                                                } else {
                                                    mProgressDialog.dismiss();
                                                    // syncerror();
                                                    BancaBDMTracker.this
                                                            .runOnUiThread(new Runnable() {

                                                                // @Override
                                                                public void run() {
                                                                    // TODO
                                                                    // Auto-generated
                                                                    // method
                                                                    // stub
                                                                    syncerror();
                                                                }
                                                            });
                                                }
                                            }
                                        }

                                    }

                                    // mProgressDialog.dismiss();

                                    if (defineobjective_running) {
                                        mProgressDialog.dismiss();
                                        BancaBDMTracker.this
                                                .runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        // TODO Auto-generated
                                                        // method stub
                                                        tasksyncerror();

                                                        clsDefineObjectiveSync obj = new clsDefineObjectiveSync(
                                                                strCode,
                                                                strCIFBDMUserId,
                                                                "True");
                                                        db.AddDefineObjectiveSync(obj);
                                                    }
                                                });
                                    }
                                }
                            };

                            splashTread.start();

                            /*
                             * if(defineobjective_running == true) {
                             * mProgressDialog.dismiss(); tasksyncerror(); }
                             */

                            /*
                             * clsDefineObjectiveSync obj = new
                             * clsDefineObjectiveSync(strCode, strCIFBDMUserId,
                             * "True"); db.AddDefineObjectiveSync(obj);
                             */

                        }
                    }
                }
            }
        });

        btndateRecordActivity.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                datecheck = 1;

                showDialog(DATE_DIALOG_ID);
            }
        });

        btnadd_RecordActivity.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                String strbranch = selBranchRecordActivity.getSelectedItem()
                        .toString();
                String strdate = editTextdtRecordActivity.getText().toString();
                /*
                 * String strtime = tvTimeRecordActivity.getText().toString();
                 * String strtimeto =
                 * tvTimeToRecordActivity.getText().toString();
                 */
                //String strtime = "";
                String strtimeto = "";
                String strremark = edremarkRecordActivity.getText().toString();

                String strSlot = selActivityTime.getSelectedItem().toString();
                String strLead = edlead.getText().toString();

                String month = m;
                String year = y;

                String strSync = "False";
                String strStatus = "Open";

                // get current date and formate that into yyyy-MM-dd
                Calendar cal = Calendar.getInstance();
                int mYear = cal.get(Calendar.YEAR);
                int mMonth = cal.get(Calendar.MONTH);
                int mDay = cal.get(Calendar.DAY_OF_MONTH);

                String y = String.valueOf(mYear);
                String m = String.valueOf(mMonth + 1);
                String da = String.valueOf(mDay);
                if (m.contentEquals("1")) {
                    m = "01";
                } else if (m.contentEquals("2")) {
                    m = "02";
                } else if (m.contentEquals("3")) {
                    m = "03";
                } else if (m.contentEquals("4")) {
                    m = "04";
                } else if (m.contentEquals("5")) {
                    m = "05";
                } else if (m.contentEquals("6")) {
                    m = "06";
                } else if (m.contentEquals("7")) {
                    m = "07";
                } else if (m.contentEquals("8")) {
                    m = "08";
                } else if (m.contentEquals("9")) {
                    m = "09";
                }

                if (da.contentEquals("1")) {
                    da = "01";
                } else if (da.contentEquals("2")) {
                    da = "02";
                } else if (da.contentEquals("3")) {
                    da = "03";
                } else if (da.contentEquals("4")) {
                    da = "04";
                } else if (da.contentEquals("5")) {
                    da = "05";
                } else if (da.contentEquals("6")) {
                    da = "06";
                } else if (da.contentEquals("7")) {
                    da = "07";
                } else if (da.contentEquals("8")) {
                    da = "08";
                } else if (da.contentEquals("9")) {
                    da = "09";
                }

                String strCreatedDate = y + "-" + m + "-" + da;
                String strModifiedDate = "0";

                /*
                 * if (strActivity.contentEquals("Select") ||
                 * strSubActivity.contentEquals("Select") ||
                 * strbranch.contentEquals("Select Branch Name") ||
                 * strdate.equalsIgnoreCase("") || strtime.equalsIgnoreCase("")
                 * || strremark.equalsIgnoreCase("") ||
                 * strtimeto.equalsIgnoreCase(""))
                 */

                if (strActivity.contentEquals("Select")
                        || strSubActivity.contentEquals("Select")
                        || strbranch.contentEquals("Select Branch Name")
                        || strdate.equalsIgnoreCase("")
                        || strSlot.contentEquals("Select Time Slot")
                        || strremark.equalsIgnoreCase("")) {
                    validation();
                } else {


                    //today date validation
                    final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH);

                    Calendar cala = Calendar.getInstance();
                    int mYearc = cala.get(Calendar.YEAR);
                    int mMonthc = cala.get(Calendar.MONTH);
                    int mDayc = cala.get(Calendar.DAY_OF_MONTH);

                    // String fyear = "";
                    // String lastd = "";

                    String ya = String.valueOf(mYearc);
                    String mo = String.valueOf(mMonthc + 1);
                    // String d = String.valueOf(mDayc);
                    String dat = String.valueOf(mDayc);
                    if (mo.contentEquals("1")) {
                        mo = "January";

                    } else if (mo.contentEquals("2")) {
                        mo = "February";

                    } else if (mo.contentEquals("3")) {
                        mo = "March";

                    } else if (mo.contentEquals("4")) {
                        mo = "April";

                    } else if (mo.contentEquals("5")) {
                        mo = "May";

                    } else if (mo.contentEquals("6")) {
                        mo = "June";

                    } else if (mo.contentEquals("7")) {
                        mo = "July";

                    } else if (mo.contentEquals("8")) {
                        mo = "August";

                    } else if (mo.contentEquals("9")) {
                        mo = "September";

                    } else if (mo.contentEquals("10")) {
                        mo = "October";

                    } else if (mo.contentEquals("11")) {
                        mo = "November";

                    } else if (mo.contentEquals("12")) {
                        mo = "December";
                    }


                    String totaldate = dat + "-" + mo + "-" + ya;

                    Date d1 = null;
                    try {
                        d1 = formatter.parse(strdate);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    Date d2 = null;
                    try {
                        d2 = formatter.parse(totaldate);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    //can not select past
                    //if ((d1.after(d2))||(d1.equals(d2)))

                    //can not select future
                    if ((d2.after(d1)) || (d2.equals(d1))) {
                        //end validation

                        if (strActivity.contentEquals("Business Connect")
                                || strActivity
                                .contentEquals("Promotional Activities")) {
                            if (strLead.equalsIgnoreCase("")) {
                                validation_lead();
                            } else {
                                Date dt1 = null;
                                try {
                                    dt1 = dateFormat.parse(strdate);
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                strdate = df.format(dt1);

                                /*
                                 * clsCalendarActivityRecorder cls = new
                                 * clsCalendarActivityRecorder( strdate, strbranch,
                                 * month == null ? "" : month, year == null ? "" : year,
                                 * strtime, strremark, strActivity, strSubActivity,
                                 * strtimeto, strCIFBDMUserId, strSync, strCreatedDate,
                                 * strModifiedDate, strStatus);
                                 */

                                int recordcount = db.RecordExist(strActivity, strSubActivity, strbranch, strdate, strSlot, strremark, strLead);
                                if (recordcount == 0) {

                                    clsCalendarActivityRecorder cls = new clsCalendarActivityRecorder(
                                            strdate, strbranch, month == null ? "" : month,
                                            year == null ? "" : year, strSlot, strremark,
                                            strActivity, strSubActivity, strtimeto,
                                            strCIFBDMUserId, strSync, strCreatedDate,
                                            strModifiedDate, strStatus, strLead);
                                    db.AddActivityRecordEvent(cls);

                                    //ok();

                                    selActivityRecordActivity.setSelection(0);
                                    selSubActivityRecordActivity.setSelection(0);
                                    selBranchRecordActivity.setSelection(0);
                                    editTextdtRecordActivity.setText("");
                                    tvTimeRecordActivity.setText("");
                                    tvTimeToRecordActivity.setText("");
                                    edremarkRecordActivity.setText("");

                                    selActivityTime.setSelection(0);

                                    displayTodayEventsra();

                                    if (mCommonMethods.isNetworkConnected(context)) {

                                        String categoryid = db.getActivityId(strActivity);
                                        String subcategoryid = db.GetSubCategoryMasterId(categoryid, strSubActivity);

                                        lstSyncTaskList.clear();

                                        lstSyncTaskList = new ArrayList<>();
                                        lstSyncTaskList.add(strCIFBDMUserId);
                                        lstSyncTaskList.add(categoryid);
                                        lstSyncTaskList.add(strbranch);
                                        lstSyncTaskList.add(strSlot);
                                        lstSyncTaskList.add(strremark + "|" + strLead);
                                        lstSyncTaskList.add(strStatus);
                                        lstSyncTaskList.add(subcategoryid);


                                        lstActRecord.clear();
                                        lstActRecord = new ArrayList<>();

                                        lstActRecord.add(strActivity);
                                        lstActRecord.add(strSubActivity);
                                        lstActRecord.add(strbranch);
                                        lstActRecord.add(strdate);
                                        lstActRecord.add(strSlot);
                                        lstActRecord.add(strremark);
                                        lstActRecord.add(strLead);

                                        taskListRecord = new PushTaskListRecord();
                                        startPushTaskListRecord();

                                    } else {
                                        mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
                                    }

                                } else {
                                    AlertRecordExist();
                                }
                            }
                        } else {

                            Date dt1 = null;
                            try {
                                dt1 = dateFormat.parse(strdate);
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            strdate = df.format(dt1);

                            /*
                             * clsCalendarActivityRecorder cls = new
                             * clsCalendarActivityRecorder( strdate, strbranch,
                             * month == null ? "" : month, year == null ? "" : year,
                             * strtime, strremark, strActivity, strSubActivity,
                             * strtimeto, strCIFBDMUserId, strSync, strCreatedDate,
                             * strModifiedDate, strStatus);
                             */

                            int recordcount = db.RecordExist(strActivity, strSubActivity, strbranch, strdate, strSlot, strremark, strLead);
                            if (recordcount == 0) {

                                clsCalendarActivityRecorder cls = new clsCalendarActivityRecorder(
                                        strdate, strbranch, month == null ? "" : month,
                                        year == null ? "" : year, strSlot, strremark,
                                        strActivity, strSubActivity, strtimeto,
                                        strCIFBDMUserId, strSync, strCreatedDate,
                                        strModifiedDate, strStatus, strLead);
                                db.AddActivityRecordEvent(cls);

                                //ok();

                                selActivityRecordActivity.setSelection(0);
                                selSubActivityRecordActivity.setSelection(0);
                                selBranchRecordActivity.setSelection(0);
                                editTextdtRecordActivity.setText("");
                                tvTimeRecordActivity.setText("");
                                tvTimeToRecordActivity.setText("");
                                edremarkRecordActivity.setText("");

                                selActivityTime.setSelection(0);

                                displayTodayEventsra();

                                if (mCommonMethods.isNetworkConnected(context)) {

                                    String categoryid = db.getActivityId(strActivity);
                                    String subcategoryid = db.GetSubCategoryMasterId(categoryid, strSubActivity);

                                    lstSyncTaskList.clear();

                                    lstSyncTaskList = new ArrayList<>();
                                    lstSyncTaskList.add(strCIFBDMUserId);
                                    lstSyncTaskList.add(categoryid);
                                    lstSyncTaskList.add(strbranch);
                                    lstSyncTaskList.add(strSlot);
                                    lstSyncTaskList.add(strremark + "|" + strLead);
                                    lstSyncTaskList.add(strStatus);
                                    lstSyncTaskList.add(subcategoryid);


                                    lstActRecord.clear();
                                    lstActRecord = new ArrayList<>();

                                    lstActRecord.add(strActivity);
                                    lstActRecord.add(strSubActivity);
                                    lstActRecord.add(strbranch);
                                    lstActRecord.add(strdate);
                                    lstActRecord.add(strSlot);
                                    lstActRecord.add(strremark);
                                    lstActRecord.add(strLead);

                                    taskListRecord = new PushTaskListRecord();
                                    startPushTaskListRecord();

                                } else {
                                    mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
                                }

                            } else {
                                AlertRecordExist();
                            }

                        }

                    } else {
                        dateselecterror();
                    }
                }
            }
        });

        btnleadfromho.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub


                btnleadfromho.setBackgroundResource(R.drawable.exp_selected_sub);
                btnselflead
                        .setBackgroundResource(R.drawable.exp_unselected_sub);


                Intent inte = new Intent(context, Lead_MgtHOList.class);
                startActivity(inte);


            }
        });

        btnselflead.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                btnleadfromho.setBackgroundResource(R.drawable.exp_unselected_sub);
                btnselflead
                        .setBackgroundResource(R.drawable.exp_selected_sub);

                lnholead.getLayoutParams().height = 0;
                lnholead.requestLayout();
            }
        });


        btnbmreport.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                strType = "Branch Manager";

                if (mCommonMethods.isNetworkConnected(context)) {

                    taskBdm_mail_data = new DownloadBdm_mail_data();
                    startdownloadBdm_mail_data();
                } else {
                    Toast.makeText(context,
                            "Internet Connection Not Present,Try again..",
                            Toast.LENGTH_SHORT).show();
                    // for standard mail formate
                    String strEmailID = "";
                    String strMobileNo = "";

                    try {
                        strEmailID = SimpleCrypto.decrypt("SBIL",
                                db.GetEmailId());
                        strMobileNo = SimpleCrypto.decrypt("SBIL",
                                db.GetMobileNo());
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    String emailBody = "Dear Sir / Ma�am,"
                            + "\n"
                            + "\n"
                            + "Greetings of the day!!"
                            + "\n"
                            + "\n"
                            + "We place below the SBI Life cross selling performance of your branch as on ...."
                            + "\n"
                            + "Particulars              Business Sourced MTD      Business Issued YTD"
                            + "\n"
                            + "New Business Premium (NBP)"
                            + "\n"
                            + "Individual NoP"
                            + "\n"
                            + "All Business figures are on Billed basis."
                            + "\n"
                            + "\n"
                            + "For more details on performance, please feel free to contact undersigned,"
                            + "\n" + "\n" + "Happy Selling !!" + "\n" + "\n"
                            + "\n" + "Thanks & regards," + "\n" + "abc" + "\n"
                            + "SBI Life Insurance Co. Ltd." + "\n"
                            + "CPC Belapur Mumbai" + "\n" + "Email  : "
                            + strEmailID + "\n" + "Mobile  : " + strMobileNo;

                    btnbmreport.setBackgroundResource(R.drawable.exp_selected);
                    btncifreport
                            .setBackgroundResource(R.drawable.exp_unselected);
                    btnagmreport
                            .setBackgroundResource(R.drawable.exp_unselected);
                    btnrmreport
                            .setBackgroundResource(R.drawable.exp_unselected);

                    // strType = "Branch Manager";
                    Cursor c = db.getGroupListEmail(strType, strCIFBDMUserId);
                    ArrayList<String> items = new ArrayList<>();
                    if (c.getCount() > 0) {
                        c.moveToFirst();
                        for (int ii = 0; ii < c.getCount(); ii++) {
                            items.add(c.getString(c.getColumnIndex("EmailName")));
                            c.moveToNext();
                        }
                    }

                    //String str = "branch.manager@sbilife.co.in";
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setType("text/plain/email/dir");
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                            "Branch Performance");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
                    emailIntent.setData(Uri.parse("mailto:" + items));
                    try {
                        startActivity(emailIntent);
                    } catch (android.content.ActivityNotFoundException e) {
                        Toast.makeText(context,
                                "There are No Email Client Installed",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btncifreport.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                strType = "CIF";

                if (mCommonMethods.isNetworkConnected(context)) {

                    taskBdm_mail_data = new DownloadBdm_mail_data();
                    startdownloadBdm_mail_data();
                } else {
                    Toast.makeText(context,
                            "Internet Connection Not Present,Try again..",
                            Toast.LENGTH_SHORT).show();
                    // for standard mail formate
                    String strEmailID = "";
                    String strMobileNo = "";

                    try {
                        strEmailID = SimpleCrypto.decrypt("SBIL",
                                db.GetEmailId());
                        strMobileNo = SimpleCrypto.decrypt("SBIL",
                                db.GetMobileNo());
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    String emailBody = "Dear Sir / Ma�am,"
                            + "\n"
                            + "\n"
                            + "Greetings of the day!!"
                            + "\n"
                            + "\n"
                            + "We place below your SBI Life business details "
                            + "\n"
                            + "Particulars              Business Sourced MTD      Business Issued YTD"
                            + "\n"
                            + "New Business Premium (NBP)"
                            + "\n"
                            + "Individual NoP"
                            + "\n"
                            + "All Business figures are on Billed basis."
                            + "\n"
                            + "\n"
                            + "For more details on performance, please feel free to contact undersigned,"
                            + "\n" + "\n" + "Happy Selling !!" + "\n" + "\n"
                            + "\n" + "Thanks & regards," + "\n" + "abc" + "\n"
                            + "SBI Life Insurance Co. Ltd." + "\n"
                            + "CPC Belapur Mumbai" + "\n" + "Email  : "
                            + strEmailID + "\n" + "Mobile  : " + strMobileNo;

                    btnbmreport
                            .setBackgroundResource(R.drawable.exp_unselected);
                    btncifreport.setBackgroundResource(R.drawable.exp_selected);
                    btnagmreport
                            .setBackgroundResource(R.drawable.exp_unselected);
                    btnrmreport
                            .setBackgroundResource(R.drawable.exp_unselected);

                    // strType = "CIF";
                    Cursor c = db.getGroupListEmail(strType, strCIFBDMUserId);
                    ArrayList<String> items = new ArrayList<>();
                    if (c.getCount() > 0) {
                        c.moveToFirst();
                        for (int ii = 0; ii < c.getCount(); ii++) {
                            items.add(c.getString(c.getColumnIndex("EmailName")));
                            c.moveToNext();
                        }
                    }

                    //String str = "cif@sbilife.co.in";
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setType("text/plain/email/dir");
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                            "Insurance Detail");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
                    emailIntent.setData(Uri.parse("mailto:" + items));
                    try {
                        startActivity(emailIntent);
                    } catch (android.content.ActivityNotFoundException e) {
                        Toast.makeText(context,
                                "There are No Email Client Installed",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnagmreport.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                // for standard mail formate
                String strEmailID = "";
                String strMobileNo = "";

                try {
                    strEmailID = SimpleCrypto.decrypt("SBIL",
                            db.GetEmailId());
                    strMobileNo = SimpleCrypto.decrypt("SBIL",
                            db.GetMobileNo());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                String emailBody = "Dear Sir / Ma�am,"
                        + "\n"
                        + "\n"
                        + "Greetings of the day!!"
                        + "\n"
                        + "\n"
                        + "We place below the SBI Life cross selling performance of Turbo branch as on ..."
                        + "\n"
                        + "Branch Name     NBP MTD    NBP YTD   MTD Individual NoP   YTD Individual NoP"
                        + "\n"
                        + "\n"
                        + "All Business figures are on Billed basis."
                        + "\n"
                        + "\n"
                        + "For more details on performance, please feel free to contact undersigned,"
                        + "\n" + "\n" + "Happy Selling !!" + "\n" + "\n" + "\n"
                        + "Thanks & regards," + "\n" + "abc" + "\n"
                        + "SBI Life Insurance Co. Ltd." + "\n"
                        + "CPC Belapur Mumbai" + "\n" + "Email  : "
                        + strEmailID + "\n" + "Mobile  : " + strMobileNo;

                btnbmreport.setBackgroundResource(R.drawable.exp_unselected);
                btncifreport.setBackgroundResource(R.drawable.exp_unselected);
                btnagmreport.setBackgroundResource(R.drawable.exp_selected);
                btnrmreport.setBackgroundResource(R.drawable.exp_unselected);

                strType = "AGM";
                Cursor c = db.getGroupListEmail(strType, strCIFBDMUserId);
                ArrayList<String> items = new ArrayList<>();
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    for (int ii = 0; ii < c.getCount(); ii++) {
                        items.add(c.getString(c.getColumnIndex("EmailName")));
                        c.moveToNext();
                    }
                }

                //String str = "agm@sbilife.co.in";
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setType("text/plain/email/dir");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                        "Turbo Branch Performance");
                emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
                emailIntent.setData(Uri.parse("mailto:" + items));
                try {
                    startActivity(emailIntent);
                } catch (android.content.ActivityNotFoundException e) {
                    Toast.makeText(context,
                            "There are No Email Client Installed",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnrmreport.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                // for standard mail formate
                String strEmailID = "";
                String strMobileNo = "";

                try {
                    strEmailID = SimpleCrypto.decrypt("SBIL",
                            db.GetEmailId());
                    strMobileNo = SimpleCrypto.decrypt("SBIL",
                            db.GetMobileNo());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                String emailBody = "Dear Sir / Ma�am,"
                        + "\n"
                        + "\n"
                        + "Greetings of the day!!"
                        + "\n"
                        + "\n"
                        + "We place below the SBI Life cross selling performance of Turbo branch as on ..."
                        + "\n"
                        + "Branch Name     NBP MTD    NBP YTD   MTD Individual NoP   YTD Individual NoP"
                        + "\n"
                        + "\n"
                        + "All Business figures are on Billed basis."
                        + "\n"
                        + "\n"
                        + "For more details on performance, please feel free to contact undersigned,"
                        + "\n" + "\n" + "Happy Selling !!" + "\n" + "\n" + "\n"
                        + "Thanks & regards," + "\n" + "abc" + "\n"
                        + "SBI Life Insurance Co. Ltd." + "\n"
                        + "CPC Belapur Mumbai" + "\n" + "Email  : "
                        + strEmailID + "\n" + "Mobile  : " + strMobileNo;

                btnbmreport.setBackgroundResource(R.drawable.exp_unselected);
                btncifreport.setBackgroundResource(R.drawable.exp_unselected);
                btnagmreport.setBackgroundResource(R.drawable.exp_unselected);
                btnrmreport.setBackgroundResource(R.drawable.exp_selected);

                strType = "RM and AM";
                Cursor c = db.getGroupListEmail(strType, strCIFBDMUserId);
                ArrayList<String> items = new ArrayList<>();
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    for (int ii = 0; ii < c.getCount(); ii++) {
                        items.add(c.getString(c.getColumnIndex("EmailName")));
                        c.moveToNext();
                    }
                }

                //String str = "ra@sbilife.co.in";
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setType("text/plain/email/dir");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                        "Turbo Branch Performance");
                emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
                emailIntent.setData(Uri.parse("mailto:" + items));
                try {
                    startActivity(emailIntent);
                } catch (android.content.ActivityNotFoundException e) {
                    Toast.makeText(context,
                            "There are No Email Client Installed",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnaddemail.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                lstEmail.setVisibility(View.GONE);

                btnbmreport.setBackgroundResource(R.drawable.exp_unselected);
                btncifreport.setBackgroundResource(R.drawable.exp_unselected);
                btnagmreport.setBackgroundResource(R.drawable.exp_unselected);
                btnrmreport.setBackgroundResource(R.drawable.exp_unselected);

                spgroup.setSelection(0);

                btnaddemail.setBackgroundResource(R.drawable.exp_selected);
                btnviewemail.setBackgroundResource(R.drawable.exp_unselected);

                tbladdemail.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                tbladdemail.requestLayout();

                tblviewemail.getLayoutParams().height = 0;
                tblviewemail.requestLayout();
            }
        });

        btnviewemail.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                lstEmail.setVisibility(View.VISIBLE);

                btnbmreport.setBackgroundResource(R.drawable.exp_unselected);
                btncifreport.setBackgroundResource(R.drawable.exp_unselected);
                btnagmreport.setBackgroundResource(R.drawable.exp_unselected);
                btnrmreport.setBackgroundResource(R.drawable.exp_unselected);

                List<clsEmail> lst;
                clsEmail node;
                node = new clsEmail("", "", "", "", "", "", "");
                lst = new ArrayList<>();
                lst.clear();
                lst.add(node);
                ItemsAdapterEmail adapter = new ItemsAdapterEmail(context, 0,
                        lst);
                adapter.setNotifyOnChange(true);
                lstEmail.setAdapter(adapter);
                setShowallList(lstEmail);

                spviewgroup.setSelection(0);

                btnviewemail.setBackgroundResource(R.drawable.exp_selected);
                btnaddemail.setBackgroundResource(R.drawable.exp_unselected);

                tblviewemail.getLayoutParams().height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
                tblviewemail.requestLayout();

                tbladdemail.getLayoutParams().height = 0;
                tbladdemail.requestLayout();
            }
        });

        btn_saveEmail.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                String strEmailId = editEmailId.getText().toString();

                if (strEmailId.equalsIgnoreCase("")
                        || strGroupEmail.contentEquals("Select")) {
                    validation();
                } else {
                    int recordexist = db.EmailExistorNot(strGroupEmail,
                            strEmailId);

                    if (recordexist == 1) {
                        existlert();
                    } else {

                        clsEmail objEmail = new clsEmail(strGroupEmail,
                                strEmailId, "0", "0", "0", "0", strCIFBDMUserId);
                        db.AddEmail(objEmail);

                        savealert();

                        editEmailId.setText("");

                    }
                }
            }
        });

        btn_cancelemail.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                tbladdemail.getLayoutParams().height = 0;
                tbladdemail.requestLayout();
            }
        });

        btndeposits.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if (!strBranchCode.contentEquals("Select Branch Code")) {
                    btndeposits.setBackgroundResource(R.drawable.exp_selected);
                    btnadvances
                            .setBackgroundResource(R.drawable.exp_unselected);

                    tblrdeposit.setVisibility(View.VISIBLE);
                    tblradvances.setVisibility(View.GONE);

                    strBtnType = "D";

                    int count = db.BranchDepositsExistorNot(strBranchCode);
                    if (count == 0) {
                        taskBranchDeposits = new DownloadBranchDeposits();
                        if (mCommonMethods.isNetworkConnected(context)) {
                            startdownloadbranchdeposits();
                        } else {
                            mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
                        }

                    } else {
                        /*
                         * String strDepositB1K =
                         * db.GetBranchDepositsB1K(strBranchCode,
                         * strDepositPerticular);
                         */

                        String strDepositB1K = db.GetBranchDepositsB1K(
                                strBranchCode, seldeposit.getSelectedItem()
                                        .toString());

                        eddepositretail.setText(strDepositB1K);
                    }
                } else {
                    branch_code_validation();
                }
            }
        });

        btnadvances.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if (!strBranchCode.contentEquals("Select Branch Code")) {
                    btndeposits
                            .setBackgroundResource(R.drawable.exp_unselected);
                    btnadvances.setBackgroundResource(R.drawable.exp_selected);

                    tblrdeposit.setVisibility(View.GONE);
                    tblradvances.setVisibility(View.VISIBLE);

                    strBtnType = "A";

                    int count = db.BranchAdvancesExistorNot(strBranchCode);
                    if (count == 0) {
                        taskBranchAdvances = new DownloadBranchAdvances();
                        if (mCommonMethods.isNetworkConnected(context)) {
                            startdownloadbranchadvances();
                        } else {
                            mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
                        }
                    } else {
                        /*
                         * String strAdvancesB1L =
                         * db.GetBranchAdvacesB1L(strBranchCode,
                         * strAdvancesPerticular);
                         */

                        String strAdvancesB1L = db.GetBranchAdvacesB1L(
                                strBranchCode, seladvances.getSelectedItem()
                                        .toString());

                        edadvancesretail.setText(strAdvancesB1L);

                    }
                } else {
                    branch_code_validation();
                }
            }
        });

        btnaddBranchProfile.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                String dcross = eddepositcrosssel.getText().toString();
                String dcrossnop = eddepositcrossselnop.getText().toString();
                String dcrosspre = eddepositcrossselpot.getText().toString();

                String across = edadvancescrosssel.getText().toString();
                String acrossnop = edadvancescrossselnop.getText().toString();
                String acrosspre = edadvancescrossselpot.getText().toString();

                if (!strBranchCode.contentEquals("Select Branch Code")) {
                    if (!strBtnType.contentEquals("")) {

                        if (strBtnType.contentEquals("D")) {
                            if (dcross.equalsIgnoreCase("")
                                    || dcrossnop.equalsIgnoreCase("")
                                    || dcrosspre.equalsIgnoreCase("")) {
                                validation();
                            } else {
                                int count = db.SyncBranchProfileExistorNot(
                                        strBranchCode, strDepositPerticular,
                                        strDepositCategory);
                                if (count == 0) {
                                    clsSyncBranchProfile obj = new clsSyncBranchProfile(
                                            strBranchCode,
                                            strDepositPerticular,
                                            strDepositCategory, dcross,
                                            dcrossnop, dcrosspre,
                                            strCIFBDMUserId);
                                    db.AddSyncBranchProfile(obj);

                                    savedefineobjectivelert();

                                } else {
                                    existdefineobjectivelert();
                                }
                            }
                        } else if (strBtnType.contentEquals("A")) {
                            if (across.equalsIgnoreCase("")
                                    || acrossnop.equalsIgnoreCase("")
                                    || acrosspre.equalsIgnoreCase("")) {
                                validation();
                            } else {
                                int count = db.SyncBranchProfileExistorNot(
                                        strBranchCode, strAdvancesPerticular,
                                        strAdvancesCategory);
                                if (count == 0) {
                                    clsSyncBranchProfile obj = new clsSyncBranchProfile(
                                            strBranchCode,
                                            strAdvancesPerticular,
                                            strAdvancesCategory, across,
                                            acrossnop, acrosspre,
                                            strCIFBDMUserId);
                                    db.AddSyncBranchProfile(obj);

                                    savedefineobjectivelert();

                                } else {
                                    existdefineobjectivelert();
                                }
                            }
                        }
                    } else {
                        deposits_advances_validation();
                    }
                } else {
                    branch_code_validation();
                }
            }
        });

        btnsyncBranchProfile.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                /*
                 * String Sync_strId = ""; String Sync_cross = ""; String
                 * Sync_crossnop = ""; String Sync_crosspre = "";
                 */

                if (!strBranchCode.contentEquals("Select Branch Code")) {

                    if (!strBtnType.contentEquals("")) {

                        if (strBtnType.contentEquals("D")) {
                            // strId = db.GetBranchDepositsId(strBranchCode,
                            // strDepositPerticular);
                            Sync_strId = db.GetBranchDepositsId(strBranchCode,
                                    seldeposit.getSelectedItem().toString());
                            Sync_cross = eddepositcrosssel.getText().toString();
                            Sync_crossnop = eddepositcrossselnop.getText()
                                    .toString();
                            Sync_crosspre = eddepositcrossselpot.getText()
                                    .toString();

                            if (Sync_cross.equalsIgnoreCase("")
                                    || Sync_crossnop.equalsIgnoreCase("")
                                    || Sync_crosspre.equalsIgnoreCase("")) {
                                validation();
                            } else {
                                if (mCommonMethods.isNetworkConnected(context)) {

                                    taskSyncBranchProfile = new DownloadSyncBranchProfile();

                                    uploadsyncbranchprofile();

                                    /*
                                     * SoapObject request = new
                                     * SoapObject(NAMESPACE,
                                     * METHOD_NAME_BRANCH_PROFILE_TRANS);
                                     *
                                     * request.addProperty(
                                     * "BRANCH_ADVANCES_DEPOSITS_ID", strId);
                                     * request
                                     * .addProperty("ADVANCES_DEPOSIT_FLAG",
                                     * strBtnType);
                                     * request.addProperty("BDM_ID",
                                     * strCIFBDMUserId);
                                     * request.addProperty("CROSS_SELL_PROD",
                                     * cross);
                                     * request.addProperty("CROSS_SELL_POT_NOPS"
                                     * , crossnop);
                                     * request.addProperty("CROSS_SELL_POT_PREMIUM"
                                     * , crosspre);
                                     *
                                     * SoapSerializationEnvelope envelope = new
                                     * SoapSerializationEnvelope(
                                     * SoapEnvelope.VER11); envelope.dotNet =
                                     * true;
                                     *
                                     * envelope.setOutputSoapObject(request);
                                     *
                                     * allowAllSSL();
                                     *
                                     * StrictMode.ThreadPolicy policy = new
                                     * StrictMode.ThreadPolicy.Builder()
                                     * .permitAll().build();
                                     * StrictMode.setThreadPolicy(policy);
                                     *
                                     * HttpTransportSE androidHttpTranport = new
                                     * HttpTransportSE( URl); try { try {
                                     * androidHttpTranport.call(
                                     * SOAP_ACTION_BRANCH_PROFILE_TRANS,
                                     * envelope); } catch
                                     * (XmlPullParserException e) { try { throw
                                     * (e); } catch (Exception e1) {
                                     * e1.printStackTrace(); }
                                     * mProgressDialog.dismiss();
                                     * syncbranchprofile_running = false; }
                                     * Object response = envelope.getResponse();
                                     * strResult = response.toString(); } catch
                                     * (IOException e) { try { throw (e); }
                                     * catch (Exception e1) {
                                     * e1.printStackTrace(); }
                                     * mProgressDialog.dismiss();
                                     * syncbranchprofile_running = false; }
                                     *
                                     * if (syncbranchprofile_running != false) {
                                     * if (!strResult.contentEquals("0")) {
                                     *
                                     * BDMTracker.getActivity().runOnUiThread(new
                                     * Runnable() {
                                     *
                                     * @Override public void run() { // TODO
                                     * Auto-generated method stub
                                     * mProgressDialog.dismiss();
                                     * tasksyncerror(); } }); } else {
                                     * syncbranchprofile_running = false;
                                     * mProgressDialog.dismiss(); //
                                     * syncerror();
                                     *
                                     * BDMTracker.getActivity().runOnUiThread(new
                                     * Runnable() {
                                     *
                                     * @Override public void run() { // TODO
                                     * Auto-generated method stub
                                     * syncbranchprofile_running = false;
                                     * mProgressDialog.dismiss(); syncerror(); }
                                     * }); } } else {
                                     * //mProgressDialog.dismiss(); //
                                     * syncerror();
                                     * BDMTracker.getActivity().runOnUiThread
                                     * (new Runnable() {
                                     *
                                     * @Override public void run() { // TODO
                                     * Auto-generated method stub
                                     * mProgressDialog.dismiss(); syncerror(); }
                                     * }); }
                                     */
                                } else {
                                    mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
                                }
                            }

                        } else if (strBtnType.contentEquals("A")) {
                            // strId = db.GetBranchAdvacesId(strBranchCode,
                            // strAdvancesPerticular);
                            Sync_strId = db.GetBranchAdvacesId(strBranchCode,
                                    seladvances.getSelectedItem().toString());
                            Sync_cross = edadvancescrosssel.getText()
                                    .toString();
                            Sync_crossnop = edadvancescrossselnop.getText()
                                    .toString();
                            Sync_crosspre = edadvancescrossselpot.getText()
                                    .toString();

                            if (Sync_cross.equalsIgnoreCase("")
                                    || Sync_crossnop.equalsIgnoreCase("")
                                    || Sync_crosspre.equalsIgnoreCase("")) {
                                validation();
                            } else {
                                if (mCommonMethods.isNetworkConnected(context)) {

                                    taskSyncBranchProfile = new DownloadSyncBranchProfile();

                                    uploadsyncbranchprofile();

                                    /*
                                     * SoapObject request = new
                                     * SoapObject(NAMESPACE,
                                     * METHOD_NAME_BRANCH_PROFILE_TRANS);
                                     *
                                     * request.addProperty(
                                     * "BRANCH_ADVANCES_DEPOSITS_ID", strId);
                                     * request
                                     * .addProperty("ADVANCES_DEPOSIT_FLAG",
                                     * strBtnType);
                                     * request.addProperty("BDM_ID",
                                     * strCIFBDMUserId);
                                     * request.addProperty("CROSS_SELL_PROD",
                                     * cross);
                                     * request.addProperty("CROSS_SELL_POT_NOPS"
                                     * , crossnop);
                                     * request.addProperty("CROSS_SELL_POT_PREMIUM"
                                     * , crosspre);
                                     *
                                     * SoapSerializationEnvelope envelope = new
                                     * SoapSerializationEnvelope(
                                     * SoapEnvelope.VER11); envelope.dotNet =
                                     * true;
                                     *
                                     * envelope.setOutputSoapObject(request);
                                     *
                                     * allowAllSSL();
                                     *
                                     * StrictMode.ThreadPolicy policy = new
                                     * StrictMode.ThreadPolicy.Builder()
                                     * .permitAll().build();
                                     * StrictMode.setThreadPolicy(policy);
                                     *
                                     * HttpTransportSE androidHttpTranport = new
                                     * HttpTransportSE(URl); try { try {
                                     * androidHttpTranport.call(
                                     * SOAP_ACTION_BRANCH_PROFILE_TRANS,
                                     * envelope); } catch
                                     * (XmlPullParserException e) { try { throw
                                     * (e); } catch (Exception e1) {
                                     * e1.printStackTrace(); }
                                     * mProgressDialog.dismiss();
                                     * syncbranchprofile_running = false; }
                                     * Object response = envelope.getResponse();
                                     * strResult = response.toString(); } catch
                                     * (IOException e) { try { throw (e); }
                                     * catch (Exception e1) {
                                     * e1.printStackTrace(); }
                                     * mProgressDialog.dismiss();
                                     * syncbranchprofile_running = false; }
                                     *
                                     * if (syncbranchprofile_running != false) {
                                     * if (!strResult.contentEquals("0")) {
                                     * //mProgressDialog.dismiss(); //
                                     * syncerror();
                                     *
                                     * BDMTracker.getActivity().runOnUiThread(new
                                     * Runnable() {
                                     *
                                     * @Override public void run() { // TODO
                                     * Auto-generated method stub
                                     * mProgressDialog.dismiss();
                                     * tasksyncerror(); } }); } else {
                                     * syncbranchprofile_running = false;
                                     * mProgressDialog.dismiss(); //
                                     * syncerror();
                                     *
                                     * BDMTracker.getActivity().runOnUiThread(new
                                     * Runnable() {
                                     *
                                     * @Override public void run() { // TODO
                                     * Auto-generated method stub
                                     * syncbranchprofile_running = false;
                                     * mProgressDialog.dismiss(); syncerror(); }
                                     * }); } } else {
                                     * //mProgressDialog.dismiss(); //
                                     * syncerror();
                                     * BDMTracker.getActivity().runOnUiThread
                                     * (new Runnable() {
                                     *
                                     * @Override public void run() { // TODO
                                     * Auto-generated method stub
                                     * mProgressDialog.dismiss(); syncerror(); }
                                     * }); }
                                     */
                                } else {
                                    mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
                                }
                            }
                        }
                    } else {
                        deposits_advances_validation();
                    }
                } else {
                    branch_code_validation();
                }
            }
        });

        btndate.setOnTouchListener(new View.OnTouchListener() {

            // @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub

                datecheck = 1;

                showDialog(DATE_DIALOG_ID);

                return false;
            }
        });

        btnadd.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                String date = etdate.getText().toString();
                // String eventname = "Branch: "
                // +selBranch.getSelectedItem().toString();
                String eventname = selBranch.getSelectedItem().toString();
                String month = m;
                String year = y;
                // String remark = "Remark: " + edremark.getText().toString();
                String remark = edremark.getText().toString();
                // String time = "Time: " + tvDisplayTime.getText().toString();
                String time = tvDisplayTime.getText().toString();
                // String activity = "Activity: "
                // +selActivity.getSelectedItem().toString();
                String activity = selActivity.getSelectedItem().toString();

                String strLead = edlead_act.getText().toString();

                String strSubActivity = "";
                String strTimeTo = "";
                String strSync = "False";
                String strServerMasterId = "";

                // get current date and formate that into yyyy-MM-dd
                Calendar cal = Calendar.getInstance();
                int mYear = cal.get(Calendar.YEAR);
                int mMonth = cal.get(Calendar.MONTH);
                int mDay = cal.get(Calendar.DAY_OF_MONTH);

                String y = String.valueOf(mYear);
                String m = String.valueOf(mMonth + 1);
                String da = String.valueOf(mDay);
                if (m.contentEquals("1")) {
                    m = "01";
                } else if (m.contentEquals("2")) {
                    m = "02";
                } else if (m.contentEquals("3")) {
                    m = "03";
                } else if (m.contentEquals("4")) {
                    m = "04";
                } else if (m.contentEquals("5")) {
                    m = "05";
                } else if (m.contentEquals("6")) {
                    m = "06";
                } else if (m.contentEquals("7")) {
                    m = "07";
                } else if (m.contentEquals("8")) {
                    m = "08";
                } else if (m.contentEquals("9")) {
                    m = "09";
                }

                if (da.contentEquals("1")) {
                    da = "01";
                } else if (da.contentEquals("2")) {
                    da = "02";
                } else if (da.contentEquals("3")) {
                    da = "03";
                } else if (da.contentEquals("4")) {
                    da = "04";
                } else if (da.contentEquals("5")) {
                    da = "05";
                } else if (da.contentEquals("6")) {
                    da = "06";
                } else if (da.contentEquals("7")) {
                    da = "07";
                } else if (da.contentEquals("8")) {
                    da = "08";
                } else if (da.contentEquals("9")) {
                    da = "09";
                }

                String strCreatedDate = y + "-" + m + "-" + da;
                String strModifiedDate = "0";

                // if (date.equalsIgnoreCase("") ||
                // eventname.equalsIgnoreCase("")
                // || time.equalsIgnoreCase("")) {
                if (date.equalsIgnoreCase("")
                        || time.equalsIgnoreCase("")
                        || selActivity.getSelectedItem().toString().equals("")
                        || selBranch.getSelectedItem().toString()
                        .equals("Select Branch Name")) {
                    validation();

                } else {


                    //today date validation
                    final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH);

                    Calendar cala = Calendar.getInstance();
                    int mYearc = cala.get(Calendar.YEAR);
                    int mMonthc = cala.get(Calendar.MONTH);
                    int mDayc = cala.get(Calendar.DAY_OF_MONTH);

                    //String fyear = "";
                    //String lastd = "";

                    String ya = String.valueOf(mYearc);
                    String mo = String.valueOf(mMonthc + 1);
                    // String d = String.valueOf(mDayc);
                    String dat = String.valueOf(mDayc);
                    if (mo.contentEquals("1")) {
                        mo = "January";

                    } else if (mo.contentEquals("2")) {
                        mo = "February";

                    } else if (mo.contentEquals("3")) {
                        mo = "March";

                    } else if (mo.contentEquals("4")) {
                        mo = "April";

                    } else if (mo.contentEquals("5")) {
                        mo = "May";

                    } else if (mo.contentEquals("6")) {
                        mo = "June";

                    } else if (mo.contentEquals("7")) {
                        mo = "July";

                    } else if (mo.contentEquals("8")) {
                        mo = "August";

                    } else if (mo.contentEquals("9")) {
                        mo = "September";

                    } else if (mo.contentEquals("10")) {
                        mo = "October";

                    } else if (mo.contentEquals("11")) {
                        mo = "November";

                    } else if (mo.contentEquals("12")) {
                        mo = "December";
                    }


                    String totaldate = dat + "-" + mo + "-" + ya;

                    Date d1 = null;
                    try {
                        d1 = formatter.parse(date);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    Date d2 = null;
                    try {
                        d2 = formatter.parse(totaldate);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if ((d1.after(d2)) || (d1.equals(d2))) {
                        //end validation


                        if (strActivity.contentEquals("Business Connect")
                                || strActivity
                                .contentEquals("Promotional Activities")) {
                            if (strLead.equalsIgnoreCase("")) {
                                validation_lead();
                            } else {

                                Date dt1 = null;
                                try {
                                    dt1 = dateFormat.parse(date);
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                date = df.format(dt1);


                                int recordcount = db.RecordExist(activity, strSubActivity, eventname, date, time, remark, strLead);
                                if (recordcount == 0) {

                                    clsBDMTrackerCalendar cls = new clsBDMTrackerCalendar(
                                            date, eventname,
                                            month == null ? "" : month,
                                            year == null ? "" : year, time, remark,
                                            activity, strSubActivity, strTimeTo,
                                            strCIFBDMUserId, "Open", strSync,
                                            strCreatedDate, strModifiedDate,
                                            strServerMasterId, strLead);
                                    db.AddEventBDMT(cls);

                                    ok();

                                    etdate.setText("");
                                    edeventname.setText("");
                                    selActivity.setSelection(0);
                                    selBranch.setSelection(0);
                                    edremark.setText("");

                                    final Calendar c = Calendar.getInstance();
                                    hour = c.get(Calendar.HOUR_OF_DAY);
                                    minute = c.get(Calendar.MINUTE);

                                    ampm = c.get(Calendar.AM_PM);
                                    if (ampm == 0) {
                                        strampm = "AM";
                                    } else {
                                        strampm = "PM";
                                    }
                                    tvDisplayTime.setText(new StringBuilder()
                                            .append(pad(hour)).append(":")
                                            .append(pad(minute)).append(" ")
                                            .append(strampm));

                                    // refresh grid
                                    refreshCalendar();

                                    // set alrm

                                    if (chk.isChecked()) {

                                        // Create a new calendar set to the date chosen
                                        // we set the time to midnight (i.e. the first
                                        // minute of that
                                        // day)
                                        Calendar ca = Calendar.getInstance();
                                        ca.set(intyear, intmonth, intday);
                                        ca.set(Calendar.HOUR_OF_DAY, 0);
                                        ca.set(Calendar.MINUTE, 0);
                                        ca.set(Calendar.SECOND, 0);
                                        // Ask our service to set an alarm for that
                                        // date,
                                        // getActivity() activity
                                        // talks to the client that talks to the service
                                        scheduleClient.setAlarmForNotification(ca);
                                        // Notify the user what they just did
                                        // Toast.makeText(getActivity(),
                                        // "Notification set for: "+ day +"/"+
                                        // (month+1) +"/"+ year,
                                        // Toast.LENGTH_SHORT).show();

                                    }

                                    chk.setChecked(false);

                                } else {
                                    AlertRecordExist();
                                }

                            }
                        } else {

                            Date dt1 = null;
                            try {
                                dt1 = dateFormat.parse(date);
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            date = df.format(dt1);


                            int recordcount = db.RecordExist(activity, strSubActivity, eventname, date, time, remark, strLead);
                            if (recordcount == 0) {

                                clsBDMTrackerCalendar cls = new clsBDMTrackerCalendar(
                                        date, eventname, month == null ? "" : month,
                                        year == null ? "" : year, time, remark,
                                        activity, strSubActivity, strTimeTo,
                                        strCIFBDMUserId, "Open", strSync,
                                        strCreatedDate, strModifiedDate,
                                        strServerMasterId, strLead);
                                db.AddEventBDMT(cls);

                                ok();

                                etdate.setText("");
                                edeventname.setText("");
                                selActivity.setSelection(0);
                                selBranch.setSelection(0);
                                edremark.setText("");

                                final Calendar c = Calendar.getInstance();
                                hour = c.get(Calendar.HOUR_OF_DAY);
                                minute = c.get(Calendar.MINUTE);

                                ampm = c.get(Calendar.AM_PM);
                                if (ampm == 0) {
                                    strampm = "AM";
                                } else {
                                    strampm = "PM";
                                }
                                tvDisplayTime.setText(new StringBuilder()
                                        .append(pad(hour)).append(":")
                                        .append(pad(minute)).append(" ")
                                        .append(strampm));

                                // refresh grid
                                refreshCalendar();

                                // set alrm

                                if (chk.isChecked()) {

                                    // Create a new calendar set to the date chosen
                                    // we set the time to midnight (i.e. the first
                                    // minute of that
                                    // day)
                                    Calendar ca = Calendar.getInstance();
                                    ca.set(intyear, intmonth, intday);
                                    ca.set(Calendar.HOUR_OF_DAY, 0);
                                    ca.set(Calendar.MINUTE, 0);
                                    ca.set(Calendar.SECOND, 0);
                                    // Ask our service to set an alarm for that date,
                                    // getActivity() activity
                                    // talks to the client that talks to the service
                                    scheduleClient.setAlarmForNotification(ca);
                                    // Notify the user what they just did
                                    // Toast.makeText(getActivity(),
                                    // "Notification set for: "+ day +"/"+
                                    // (month+1) +"/"+ year, Toast.LENGTH_SHORT).show();

                                }

                                chk.setChecked(false);

                            } else {
                                AlertRecordExist();
                            }

                        }
                    } else {
                        dateselecterror();
                    }

                }
            }
        });

        btncancel.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                tbl.getLayoutParams().height = 0;
                tbl.requestLayout();
            }
        });

        btndateone.setOnTouchListener(new View.OnTouchListener() {

            // @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub

                datecheck = 1;
                showDialog(DATE_DIALOG_ID);

                return false;
            }
        });

        btntodate.setOnTouchListener(new View.OnTouchListener() {

            // @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub

                datecheck = 0;
                showDialog(DATE_DIALOG_ID);

                return false;
            }
        });

        btnsearch.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                btnsearch.setBackgroundResource(R.drawable.exp_selected);
                btnrefresh.setBackgroundResource(R.drawable.exp_unselected);

                String strFromDate = edfromdate.getText().toString();
                String strToDate = edtodate.getText().toString();
                if (strFromDate.equalsIgnoreCase("")
                        || strToDate.equalsIgnoreCase("")) {
                    validation();
                } else {
                    // final SimpleDateFormat formatter = new
                    // SimpleDateFormat("dd-MMMM-yyyy");
                    final SimpleDateFormat formatter = new SimpleDateFormat(
                            "dd-MMMM-yyyy", Locale.ENGLISH);
                    final SimpleDateFormat df = new SimpleDateFormat(
                            "yyyy-MM-dd", Locale.ENGLISH);

                    String strfromdate = edfromdate.getText().toString();
                    String strtodate = edtodate.getText().toString();

                    Date d1 = null;
                    try {
                        d1 = formatter.parse(strfromdate);
                        strfromdate = df.format(d1);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    Date d2 = null;
                    try {
                        d2 = formatter.parse(strtodate);
                        strtodate = df.format(d2);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    final SimpleDateFormat formatter1 = new SimpleDateFormat(
                            "yyyy", Locale.ENGLISH);

                    Integer i = Integer.parseInt(formatter1.format(d2))
                            - Integer.parseInt(formatter1.format(d1));
                    String str = String.valueOf(i);

                    if (str.contains("-")) {
                        errordateselect();
                    } else {
                        final SimpleDateFormat formatter2 = new SimpleDateFormat(
                                "MM", Locale.ENGLISH);

                        Integer ii = Integer.parseInt(formatter2.format(d2))
                                - Integer.parseInt(formatter2.format(d1));
                        String strm = String.valueOf(ii);

                        if (strm.contains("-")) {
                            errordateselect();
                        } else {
                            final SimpleDateFormat formatter3 = new SimpleDateFormat(
                                    "dd", Locale.ENGLISH);

                            Integer i3 = Integer.parseInt(formatter3.format(d2))
                                    - Integer.parseInt(formatter3.format(d1));
                            String strm3 = String.valueOf(i3);

                            if (strm3.contains("-")) {
                                errordateselect();
                            } else {
                                Cursor c1 = db
                                        .GetDataBetweenTowDate(strfromdate,
                                                strtodate, strCIFBDMUserId);

                                String strdate = "";
                                String strevent = "";
                                String strtmonth = "";
                                String strtyear = "";
                                String strtime = "";
                                String strremark = "";
                                String stractivity = "";

                                String strSubActivity = "";
                                String strTimeTo = "";
                                String strUserID = "";
                                String strStatus = "";
                                String strSync = "";

                                String strCreatedDate = "";
                                String strModifiedDate = "";

                                String strServerMasterId = "";
                                String strLead = "";

                                lstmain.clear();

                                if (c1.getCount() > 0) {
                                    c1.moveToFirst();
                                    for (int iv = 0; iv < c1.getCount(); iv++) {
                                        clsBDMTrackerCalendar single = new clsBDMTrackerCalendar(
                                                strdate, strevent, strtmonth,
                                                strtyear, strtime, strremark,
                                                stractivity, strSubActivity,
                                                strTimeTo, strUserID,
                                                strStatus, strSync,
                                                strCreatedDate,
                                                strModifiedDate,
                                                strServerMasterId, strLead);
                                        // single.setDateName(c1.getString(c1.getColumnIndex("DateName")));

                                        String strdate1 = c1.getString(c1
                                                .getColumnIndex("DateNameBDMT"));

                                        Date dt1 = null;
                                        try {
                                            dt1 = df.parse(strdate1);
                                        } catch (ParseException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                        strdate1 = dateFormat.format(dt1);

                                        single.setDateName(strdate1);
                                        single.setEventName(c1.getString(c1
                                                .getColumnIndex("EventNameBDMT")));
                                        single.setTime(c1.getString(c1
                                                .getColumnIndex("TimeBDMT")));
                                        single.setRemark(c1.getString(c1
                                                .getColumnIndex("Remark")));
                                        single.setActivity(c1.getString(c1
                                                .getColumnIndex("Activity")));
                                        single.setSubActivity(c1.getString(c1
                                                .getColumnIndex("SubActivity")));
                                        single.setTimeTo(c1.getString(c1
                                                .getColumnIndex("TimeTo")));
                                        single.setStatus(c1.getString(c1
                                                .getColumnIndex("ActivityStatus")));
                                        single.setLead(c1.getString(c1
                                                .getColumnIndex("ActivityLead")));
                                        lstmain.add(single);
                                        c1.moveToNext();
                                    }
                                }

                                if (lstmain.size() > 0) {
                                    ItemsAdapter adapter = new ItemsAdapter(
                                            context, 0, lstmain);
                                    adapter.setNotifyOnChange(true);
                                    lv.setAdapter(adapter);
                                    setShowallList(lv);
                                } else {
                                    ArrayList<clsBDMTrackerCalendar> lstmain = new ArrayList<>();

                                    ItemsAdapter adapter = new ItemsAdapter(
                                            context, 0, lstmain);
                                    adapter.setNotifyOnChange(true);
                                    lv.setAdapter(adapter);
                                    setShowallList(lv);
                                }
                            }

                        }
                    }
                }
            }
        });

        btnrefresh.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                btnsearch.setBackgroundResource(R.drawable.exp_unselected);
                btnrefresh.setBackgroundResource(R.drawable.exp_selected);

                getallrecord();
            }
        });

        // store userid in bdm trackler table
        try {
            strCIFBDMUserId = SimpleCrypto.decrypt("SBIL", db.GetCIFNo());
            strCIFBDMUserType = SimpleCrypto.decrypt("SBIL",
                    db.GetUserType());
            strCIFBDMEmailId = SimpleCrypto
                    .decrypt("SBIL", db.GetEmailId());
            strCIFBDMPassword = db.GetPassword();
            strCIFBDMMObileNo = SimpleCrypto.decrypt("SBIL",
                    db.GetMobileNo());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // strCIFBDMUserId = "12089";

        // for branch profile

        String[] depositperticular = {"Savings A/c", "Current A/c",
                "Corporate Salary", "NRI Deposits", "PPF A/c", "RD",
                "Term Deposit"};
        ArrayAdapter<String> depositperticularAdapter = new ArrayAdapter<>(
                context, android.R.layout.simple_list_item_1,
                android.R.id.text1, depositperticular);
        seldeposit.setAdapter(depositperticularAdapter);
        depositperticularAdapter.notifyDataSetChanged();

        String[] depositcategory = {"Below 10 K", "10 K to Below 1 Lac",
                "1 Lac to 5 Lacs", "5 Lacs and Above"};
        ArrayAdapter<String> depositcategoryAdapter = new ArrayAdapter<>(
                context, android.R.layout.simple_list_item_1,
                android.R.id.text1, depositcategory);
        seldepositcategory.setAdapter(depositcategoryAdapter);
        depositcategoryAdapter.notifyDataSetChanged();

        String[] advancesperticular = {"Edu Loan", "Housing Loan",
                "Auto Loan", "Personal Loan"};
        ArrayAdapter<String> advancesperticularAdapter = new ArrayAdapter<>(
                context, android.R.layout.simple_list_item_1,
                android.R.id.text1, advancesperticular);
        seladvances.setAdapter(advancesperticularAdapter);
        advancesperticularAdapter.notifyDataSetChanged();

        String[] advancescategory = {"Below 1 Lac", "1 Lac to 5 Lacs",
                "Above 5 Lacs"};
        ArrayAdapter<String> advancescategoryAdapter = new ArrayAdapter<>(
                context, android.R.layout.simple_list_item_1,
                android.R.id.text1, advancescategory);
        seladvancescategory.setAdapter(advancescategoryAdapter);
        advancescategoryAdapter.notifyDataSetChanged();

        selbranchprofilecode
                .setOnItemSelectedListener(new OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {

                        strBranchCode = selbranchprofilecode.getSelectedItem()
                                .toString();

                        /*
                         * String[] depositperticular = { "Savings A/c",
                         * "Current A/c", "Corporate Salary", "NRI Deposits",
                         * "PPF A/c", "RD", "Term Deposit" };
                         * ArrayAdapter<String> depositperticularAdapter = new
                         * ArrayAdapter<String>( context,
                         * android.R.layout.simple_list_item_1,
                         * android.R.id.text1, depositperticular);
                         * seldeposit.setAdapter(depositperticularAdapter);
                         * depositperticularAdapter.notifyDataSetChanged();
                         *
                         * String[] depositcategory = { "Below 10 K",
                         * "10 K to Below 1 Lac", "1 Lac to 5 Lacs",
                         * "5 Lacs and Above" }; ArrayAdapter<String>
                         * depositcategoryAdapter = new ArrayAdapter<String>(
                         * context, android.R.layout.simple_list_item_1,
                         * android.R.id.text1, depositcategory);
                         * seldepositcategory
                         * .setAdapter(depositcategoryAdapter);
                         * depositcategoryAdapter.notifyDataSetChanged();
                         *
                         * String[] advancesperticular = { "Edu Loan",
                         * "Housing Loan", "Auto Loan", "Personal Loan" };
                         * ArrayAdapter<String> advancesperticularAdapter = new
                         * ArrayAdapter<String>( context,
                         * android.R.layout.simple_list_item_1,
                         * android.R.id.text1, advancesperticular);
                         * seladvances.setAdapter(advancesperticularAdapter);
                         * advancesperticularAdapter.notifyDataSetChanged();
                         *
                         * String[] advancescategory = { "Below 1 Lac",
                         * "1 Lac to 5 Lacs", "Above 5 Lacs" };
                         * ArrayAdapter<String> advancescategoryAdapter = new
                         * ArrayAdapter<String>( context,
                         * android.R.layout.simple_list_item_1,
                         * android.R.id.text1, advancescategory);
                         * seladvancescategory
                         * .setAdapter(advancescategoryAdapter);
                         * advancescategoryAdapter.notifyDataSetChanged();
                         */

                        if (!strBranchCode.contentEquals("Select Branch Code")) {

                            // for when branch code is changed that time below
                            // view are gone for refresh purpose


                            btndeposits
                                    .setBackgroundResource(R.drawable.exp_unselected);
                            btnadvances
                                    .setBackgroundResource(R.drawable.exp_unselected);

                            tblrdeposit.setVisibility(View.GONE);
                            tblradvances.setVisibility(View.GONE);

                            int count = db
                                    .BranchProfileExistorNot(strBranchCode);
                            if (count == 0) {
                                taskBranchProfile = new DownloadBranchProfile();
                                if (mCommonMethods.isNetworkConnected(context)) {
                                    startdownlaodbranchprofile();
                                } else {
                                    mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
                                }
                            } else {
                                Cursor c1 = db
                                        .GetBranchProfile(strBranchCode);

                                ArrayList<String> lst = new ArrayList<>();
                                lst.clear();

                                if (c1.getCount() > 0) {
                                    c1.moveToFirst();
                                    for (int ii = 0; ii < c1.getCount(); ii++) {
                                        lst.add(c1.getString(c1
                                                .getColumnIndex("BranchCode")));
                                        lst.add(c1.getString(c1
                                                .getColumnIndex("BranchProfileName")));
                                        lst.add(c1.getString(c1
                                                .getColumnIndex("BranchOpenDate")));
                                        lst.add(c1.getString(c1
                                                .getColumnIndex("BranchNetResult")));
                                        lst.add(c1.getString(c1
                                                .getColumnIndex("BranchProfileCreatedDate")));
                                        c1.moveToNext();
                                    }
                                }

                                edbranchname.setText(lst.get(1));
                                edbranchopendate.setText(lst.get(2));
                                edbranchnetresult.setText(lst.get(3));
                            }

                        } else {
                            edbranchname.setText("");
                            edbranchopendate.setText("");
                            edbranchnetresult.setText("");
                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        seldeposit.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {

                strDepositPerticular = seldeposit.getSelectedItem().toString();

                if (strDepositCategory.contentEquals("Below 10 K")) {
                    String strDepositB1K = db.GetBranchDepositsB1K(
                            strBranchCode, strDepositPerticular);

                    eddepositretail.setText(strDepositB1K);

                } else if (strDepositCategory
                        .contentEquals("10 K to Below 1 Lac")) {
                    String strDepositB1K = db.GetBranchDeposits10Kto1L(
                            strBranchCode, strDepositPerticular);

                    eddepositretail.setText(strDepositB1K);

                } else if (strDepositCategory.contentEquals("1 Lac to 5 Lacs")) {
                    String strDepositB1K = db.GetBranchDeposits1Lto5L(
                            strBranchCode, strDepositPerticular);

                    eddepositretail.setText(strDepositB1K);

                } else if (strDepositCategory.contentEquals("5 Lacs and Above")) {
                    String strDepositB1K = db.GetBranchDeposits5LandA(
                            strBranchCode, strDepositPerticular);

                    eddepositretail.setText(strDepositB1K);

                }

                // get local store data based on selection
                ArrayList<String> lstsynprofile = new ArrayList<>();
                Cursor c = db.GetBranchAandD_local(strBranchCode,
                        strDepositPerticular, strDepositCategory,
                        strCIFBDMUserId);
                lstsynprofile.clear();

                if (c.getCount() > 0) {
                    c.moveToFirst();
                    for (int ii = 0; ii < c.getCount(); ii++) {
                        lstsynprofile.add(c.getString(c
                                .getColumnIndex("SyncBranchCrossSalingProduct")));
                        lstsynprofile.add(c.getString(c
                                .getColumnIndex("SyncBranchCrossSalingPotenNops")));
                        lstsynprofile.add(c.getString(c
                                .getColumnIndex("SyncBranchCrossSalingPotenPre")));
                        c.moveToNext();
                    }
                }

                if (lstsynprofile.size() > 0) {
                    eddepositcrosssel.setText(lstsynprofile.get(0));
                    eddepositcrossselnop.setText(lstsynprofile.get(1));
                    eddepositcrossselpot.setText(lstsynprofile.get(2));
                } else {
                    eddepositcrosssel.setText("");
                    eddepositcrossselnop.setText("");
                    eddepositcrossselpot.setText("");
                }

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        seldepositcategory
                .setOnItemSelectedListener(new OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {

                        strDepositCategory = seldepositcategory
                                .getSelectedItem().toString();

                        if (strDepositCategory.contentEquals("Below 10 K")) {
                            String strDepositB1K = db.GetBranchDepositsB1K(
                                    strBranchCode, strDepositPerticular);

                            eddepositretail.setText(strDepositB1K);

                        } else if (strDepositCategory
                                .contentEquals("10 K to Below 1 Lac")) {
                            String strDepositB1K = db.GetBranchDeposits10Kto1L(
                                    strBranchCode, strDepositPerticular);

                            eddepositretail.setText(strDepositB1K);

                        } else if (strDepositCategory
                                .contentEquals("1 Lac to 5 Lacs")) {
                            String strDepositB1K = db.GetBranchDeposits1Lto5L(
                                    strBranchCode, strDepositPerticular);
                            if (strDepositB1K.contentEquals(""))

                                eddepositretail.setText(strDepositB1K);

                        } else if (strDepositCategory
                                .contentEquals("5 Lacs and Above")) {
                            String strDepositB1K = db.GetBranchDeposits5LandA(
                                    strBranchCode, strDepositPerticular);

                            eddepositretail.setText(strDepositB1K);

                        }

                        // get local store data based on selection
                        ArrayList<String> lstsynprofile = new ArrayList<>();
                        Cursor c = db.GetBranchAandD_local(strBranchCode,
                                strDepositPerticular, strDepositCategory,
                                strCIFBDMUserId);
                        lstsynprofile.clear();

                        if (c.getCount() > 0) {
                            c.moveToFirst();
                            for (int ii = 0; ii < c.getCount(); ii++) {
                                lstsynprofile.add(c.getString(c
                                        .getColumnIndex("SyncBranchCrossSalingProduct")));
                                lstsynprofile.add(c.getString(c
                                        .getColumnIndex("SyncBranchCrossSalingPotenNops")));
                                lstsynprofile.add(c.getString(c
                                        .getColumnIndex("SyncBranchCrossSalingPotenPre")));
                                c.moveToNext();
                            }
                        }

                        if (lstsynprofile.size() > 0) {
                            eddepositcrosssel.setText(lstsynprofile.get(0));
                            eddepositcrossselnop.setText(lstsynprofile.get(1));
                            eddepositcrossselpot.setText(lstsynprofile.get(2));
                        } else {
                            eddepositcrosssel.setText("");
                            eddepositcrossselnop.setText("");
                            eddepositcrossselpot.setText("");
                        }

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        seladvances.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {

                strAdvancesPerticular = seladvances.getSelectedItem()
                        .toString();

                if (strAdvancesCategory.contentEquals("Below 1 Lac")) {
                    String strAdvancesB1L = db.GetBranchAdvacesB1L(
                            strBranchCode, strAdvancesPerticular);

                    edadvancesretail.setText(strAdvancesB1L);

                } else if (strAdvancesCategory.contentEquals("1 Lac to 5 Lacs")) {
                    String strAdvancesB1L = db.GetBranchAdvaces1Lto5L(
                            strBranchCode, strAdvancesPerticular);

                    edadvancesretail.setText(strAdvancesB1L);

                } else if (strAdvancesCategory.contentEquals("Above 5 Lacs")) {
                    String strAdvancesB1L = db.GetBranchAdvacesA5L(
                            strBranchCode, strAdvancesPerticular);

                    edadvancesretail.setText(strAdvancesB1L);

                }

                // get local store data based on selection
                ArrayList<String> lstsynprofile = new ArrayList<>();
                Cursor c = db.GetBranchAandD_local(strBranchCode,
                        strAdvancesPerticular, strAdvancesCategory,
                        strCIFBDMUserId);
                lstsynprofile.clear();

                if (c.getCount() > 0) {
                    c.moveToFirst();
                    for (int ii = 0; ii < c.getCount(); ii++) {
                        lstsynprofile.add(c.getString(c
                                .getColumnIndex("SyncBranchCrossSalingProduct")));
                        lstsynprofile.add(c.getString(c
                                .getColumnIndex("SyncBranchCrossSalingPotenNops")));
                        lstsynprofile.add(c.getString(c
                                .getColumnIndex("SyncBranchCrossSalingPotenPre")));
                        c.moveToNext();
                    }
                }

                if (lstsynprofile.size() > 0) {
                    edadvancescrosssel.setText(lstsynprofile.get(0));
                    edadvancescrossselnop.setText(lstsynprofile.get(1));
                    edadvancescrossselpot.setText(lstsynprofile.get(2));
                } else {
                    edadvancescrosssel.setText("");
                    edadvancescrossselnop.setText("");
                    edadvancescrossselpot.setText("");
                }

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        seladvancescategory
                .setOnItemSelectedListener(new OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {

                        strAdvancesCategory = seladvancescategory
                                .getSelectedItem().toString();

                        if (strAdvancesCategory.contentEquals("Below 1 Lac")) {
                            String strAdvancesB1L = db.GetBranchAdvacesB1L(
                                    strBranchCode, strAdvancesPerticular);

                            edadvancesretail.setText(strAdvancesB1L);

                        } else if (strAdvancesCategory
                                .contentEquals("1 Lac to 5 Lacs")) {
                            String strAdvancesB1L = db.GetBranchAdvaces1Lto5L(
                                    strBranchCode, strAdvancesPerticular);

                            edadvancesretail.setText(strAdvancesB1L);

                        } else if (strAdvancesCategory
                                .contentEquals("Above 5 Lacs")) {
                            String strAdvancesB1L = db.GetBranchAdvacesA5L(
                                    strBranchCode, strAdvancesPerticular);

                            edadvancesretail.setText(strAdvancesB1L);

                        }

                        // get local store data based on selection
                        ArrayList<String> lstsynprofile = new ArrayList<>();
                        Cursor c = db.GetBranchAandD_local(strBranchCode,
                                strAdvancesPerticular, strAdvancesCategory,
                                strCIFBDMUserId);
                        lstsynprofile.clear();

                        if (c.getCount() > 0) {
                            c.moveToFirst();
                            for (int ii = 0; ii < c.getCount(); ii++) {
                                lstsynprofile.add(c.getString(c
                                        .getColumnIndex("SyncBranchCrossSalingProduct")));
                                lstsynprofile.add(c.getString(c
                                        .getColumnIndex("SyncBranchCrossSalingPotenNops")));
                                lstsynprofile.add(c.getString(c
                                        .getColumnIndex("SyncBranchCrossSalingPotenPre")));
                                c.moveToNext();
                            }
                        }

                        if (lstsynprofile.size() > 0) {
                            edadvancescrosssel.setText(lstsynprofile.get(0));
                            edadvancescrossselnop.setText(lstsynprofile.get(1));
                            edadvancescrossselpot.setText(lstsynprofile.get(2));
                        } else {
                            edadvancescrosssel.setText("");
                            edadvancescrossselnop.setText("");
                            edadvancescrossselpot.setText("");
                        }

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        /*
         * String [] activityListActivityRecorder =
         * {"Select","Met CIF","Met BM",
         * "Recruitment of CIF","CRP","Visit to RBO"
         * ,"Visit to RACPC etc","SBI Life Office","On Leave"};
         * ArrayAdapter<String> activityAdapterActivityRecorder = new
         * ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
         * android.R.id.text1,activityListActivityRecorder);
         * //genderAdapter.setDropDownViewResource
         * (android.R.layout.simple_spinner_dropdown_item);
         * selActivityRecordActivity
         * .setAdapter(activityAdapterActivityRecorder);
         * activityAdapterActivityRecorder.notifyDataSetChanged();
         *
         * selActivityRecordActivity.setOnItemSelectedListener(new
         * OnItemSelectedListener() { public void onItemSelected(AdapterView<?>
         * parent, View view, int position, long id) {
         *
         * strActivity = selActivityRecordActivity.getSelectedItem().toString();
         * if(!strActivity.contentEquals("Select")) {
         * if(strActivity.contentEquals("Met CIF")) { String strPid = "1";
         *
         * Cursor c1 = db.getSubActivity(strPid);
         *
         * lstevent.clear();
         *
         * if (c1.getCount() > 0) { c1.moveToFirst(); for (int ii = 0; ii <
         * c1.getCount(); ii++) { clsActivityCategory single = new
         * clsActivityCategory("", "", "", "", "", "", "");
         * single.setName(c1.getString
         * (c1.getColumnIndex("ActivityCategorySubCategoryName")));
         * lstevent.add(single); c1.moveToNext(); } }
         *
         * Spinner_BaseAdapterCategory selectedAdapterCategory = new
         * Spinner_BaseAdapterCategory(context,lstevent);
         * selSubActivityRecordActivity.setAdapter(selectedAdapterCategory); }
         * else if(strActivity.contentEquals("Met BM")) { String strPid = "2";
         *
         * Cursor c1 = db.getSubActivity(strPid);
         *
         * lstevent.clear();
         *
         * if (c1.getCount() > 0) { c1.moveToFirst(); for (int ii = 0; ii <
         * c1.getCount(); ii++) { clsActivityCategory single = new
         * clsActivityCategory("", "", "", "", "", "", "");
         * single.setName(c1.getString
         * (c1.getColumnIndex("ActivityCategorySubCategoryName")));
         * lstevent.add(single); c1.moveToNext(); } }
         *
         * Spinner_BaseAdapterCategory selectedAdapterCategory = new
         * Spinner_BaseAdapterCategory(context,lstevent);
         * selSubActivityRecordActivity.setAdapter(selectedAdapterCategory); }
         * else if(strActivity.contentEquals("Recruitment of CIF")) { String
         * strPid = "3";
         *
         * Cursor c1 = db.getSubActivity(strPid);
         *
         * lstevent.clear();
         *
         * if (c1.getCount() > 0) { c1.moveToFirst(); for (int ii = 0; ii <
         * c1.getCount(); ii++) { clsActivityCategory single = new
         * clsActivityCategory("", "", "", "", "", "", "");
         * single.setName(c1.getString
         * (c1.getColumnIndex("ActivityCategorySubCategoryName")));
         * lstevent.add(single); c1.moveToNext(); } }
         *
         * Spinner_BaseAdapterCategory selectedAdapterCategory = new
         * Spinner_BaseAdapterCategory(context,lstevent);
         * selSubActivityRecordActivity.setAdapter(selectedAdapterCategory); }
         * else if(strActivity.contentEquals("CRP")) { String strPid = "4";
         *
         * Cursor c1 = db.getSubActivity(strPid);
         *
         * lstevent.clear();
         *
         * if (c1.getCount() > 0) { c1.moveToFirst(); for (int ii = 0; ii <
         * c1.getCount(); ii++) { clsActivityCategory single = new
         * clsActivityCategory("", "", "", "", "", "", "");
         * single.setName(c1.getString
         * (c1.getColumnIndex("ActivityCategorySubCategoryName")));
         * lstevent.add(single); c1.moveToNext(); } }
         *
         * Spinner_BaseAdapterCategory selectedAdapterCategory = new
         * Spinner_BaseAdapterCategory(context,lstevent);
         * selSubActivityRecordActivity.setAdapter(selectedAdapterCategory); }
         * else if(strActivity.contentEquals("Visit to RBO")) { String strPid =
         * "5";
         *
         * Cursor c1 = db.getSubActivity(strPid);
         *
         * lstevent.clear();
         *
         * if (c1.getCount() > 0) { c1.moveToFirst(); for (int ii = 0; ii <
         * c1.getCount(); ii++) { clsActivityCategory single = new
         * clsActivityCategory("", "", "", "", "", "", "");
         * single.setName(c1.getString
         * (c1.getColumnIndex("ActivityCategorySubCategoryName")));
         * lstevent.add(single); c1.moveToNext(); } }
         *
         * Spinner_BaseAdapterCategory selectedAdapterCategory = new
         * Spinner_BaseAdapterCategory(context,lstevent);
         * selSubActivityRecordActivity.setAdapter(selectedAdapterCategory); }
         * else if(strActivity.contentEquals("Visit to RACPC etc")) { String
         * strPid = "6";
         *
         * Cursor c1 = db.getSubActivity(strPid);
         *
         * lstevent.clear();
         *
         * if (c1.getCount() > 0) { c1.moveToFirst(); for (int ii = 0; ii <
         * c1.getCount(); ii++) { clsActivityCategory single = new
         * clsActivityCategory("", "", "", "", "", "", "");
         * single.setName(c1.getString
         * (c1.getColumnIndex("ActivityCategorySubCategoryName")));
         * lstevent.add(single); c1.moveToNext(); } }
         *
         * Spinner_BaseAdapterCategory selectedAdapterCategory = new
         * Spinner_BaseAdapterCategory(context,lstevent);
         * selSubActivityRecordActivity.setAdapter(selectedAdapterCategory); }
         * else if(strActivity.contentEquals("SBI Life Office")) { String strPid
         * = "7";
         *
         * Cursor c1 = db.getSubActivity(strPid);
         *
         * lstevent.clear();
         *
         * if (c1.getCount() > 0) { c1.moveToFirst(); for (int ii = 0; ii <
         * c1.getCount(); ii++) { clsActivityCategory single = new
         * clsActivityCategory("", "", "", "", "", "", "");
         * single.setName(c1.getString
         * (c1.getColumnIndex("ActivityCategorySubCategoryName")));
         * lstevent.add(single); c1.moveToNext(); } }
         *
         * Spinner_BaseAdapterCategory selectedAdapterCategory = new
         * Spinner_BaseAdapterCategory(context,lstevent);
         * selSubActivityRecordActivity.setAdapter(selectedAdapterCategory); }
         * else if(strActivity.contentEquals("On Leave")) { String strPid = "8";
         *
         * Cursor c1 = db.getSubActivity(strPid);
         *
         * lstevent.clear();
         *
         * if (c1.getCount() > 0) { c1.moveToFirst(); for (int ii = 0; ii <
         * c1.getCount(); ii++) { clsActivityCategory single = new
         * clsActivityCategory("", "", "", "", "", "", "");
         * single.setName(c1.getString
         * (c1.getColumnIndex("ActivityCategorySubCategoryName")));
         * lstevent.add(single); c1.moveToNext(); } }
         *
         * ArrayList<clsActivityCategory> lstevent = new
         * ArrayList<clsActivityCategory>(); clsActivityCategory single = new
         * clsActivityCategory("", "", "", "", "", "", ""); single.setName("");
         * lstevent.add(single);
         *
         * Spinner_BaseAdapterCategory selectedAdapterCategory = new
         * Spinner_BaseAdapterCategory(context,lstevent);
         * selSubActivityRecordActivity.setAdapter(selectedAdapterCategory); } }
         *
         * }
         *
         * public void onNothingSelected(AdapterView<?> parent) {
         *
         * } });
         *
         * selSubActivityRecordActivity.setOnItemSelectedListener(new
         * OnItemSelectedListener() { public void onItemSelected(AdapterView<?>
         * parent, View view, int position, long id) {
         *
         * //strSubActivity =
         * selSubActivityRecordActivity.getSelectedItem().toString();
         * strSubActivity = lstevent.get(position).getName();
         *
         * }
         *
         * public void onNothingSelected(AdapterView<?> parent) {
         *
         * } });
         */

        selBranchRecordActivity = findViewById(R.id.selBranchRecordActivity);
        /*
         * String [] branchListRecordActivity =
         * {"Select","Branch A","Branch B","Branch C","Branch D"};
         * ArrayAdapter<String>branchAdapterRecordActivity = new
         * ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
         * android.R.id.text1,branchListRecordActivity);
         * //genderAdapter.setDropDownViewResource
         * (android.R.layout.simple_spinner_dropdown_item);
         * selBranchRecordActivity.setAdapter(branchAdapterRecordActivity);
         * branchAdapterRecordActivity.notifyDataSetChanged();
         */

		/*//temp
		ArrayList<String> lsteventAct1 = new ArrayList<String>();
		lsteventAct1.add("Select Branch Name");
		lsteventAct1.add("Krishnagar");

		ArrayAdapter<String> bankbranchAdapterActivityRecorder1 = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_1,
				android.R.id.text1, lsteventAct1);
		selBranchRecordActivity
				.setAdapter(bankbranchAdapterActivityRecorder1);
		bankbranchAdapterActivityRecorder1.notifyDataSetChanged();

		//end temp
*/

        lstEmail.setOnItemClickListener(new OnItemClickListener() {

            // @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //String g = lstEmail.getItemAtPosition(position).toString();

                //int rowid = Integer.parseInt(g) + 1;

                String strEmailId = ((TextView) view
                        .findViewById(R.id.txtspinitem)).getText().toString();

                Intent intent = new Intent(context, ViewEmail.class);
                intent.putExtra("strEmailId", strEmailId);
                // intent.putExtra("strRowId", String.valueOf(rowid));
                startActivity(intent);

            }
        });

        spgroup.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {

                strGroupEmail = spgroup.getSelectedItem().toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        spviewgroup.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {

                strViewGroupEmail = spviewgroup.getSelectedItem().toString();

                if (!strViewGroupEmail.contentEquals("Select")) {
                    Cursor c = db.getGroupListEmail(strViewGroupEmail,
                            strCIFBDMUserId);

                    lstemaillist.clear();

                    if (c.getCount() > 0) {
                        c.moveToFirst();
                        for (int ii = 0; ii < c.getCount(); ii++) {
                            clsEmail single = new clsEmail("", "", "", "", "",
                                    "", "");
                            single.setName(c.getString(c
                                    .getColumnIndex("EmailName")));
                            lstemaillist.add(single);
                            c.moveToNext();
                        }
                    }

                    ItemsAdapterEmail adapter = new ItemsAdapterEmail(context,
                            0, lstemaillist);
                    adapter.setNotifyOnChange(true);
                    lstEmail.setAdapter(adapter);
                    setShowallList(lstEmail);

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        RelativeLayout previous = findViewById(R.id.previous);

        previous.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        RelativeLayout next = findViewById(R.id.next);
        next.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();

            }
        });

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                ((CalendarAdapter) parent.getAdapter()).setSelected(v);
                String selectedGridDate = CalendarAdapter.dayString
                        .get(position);
                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*",
                        "");// taking last part of date. ie; 2 from 2012-12-02.
                int gridvalue = Integer.parseInt(gridvalueString);
                // navigate to next or previous month on clicking offdays.
                if ((gridvalue > 10) && (position < 8)) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    setNextMonth();
                    refreshCalendar();
                }
                ((CalendarAdapter) parent.getAdapter()).setSelected(v);


                System.out.println("Systemdate " + selectedGridDate);

                Cursor c1 = db.geteventnameBDMT(selectedGridDate,
                        strCIFBDMUserId);


                // modified by after given change by bdm tracker guy

                final SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "dd-MMMM-yyyy", Locale.ENGLISH);
                final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                String strdate = "";
                String strevent = "";
                String strtmonth = "";
                String strtyear = "";
                String strtime = "";
                String strremark = "";
                String stractivity = "";

                String strSubActivity = "";
                String strTimeTo = "";
                String strUserID = "";
                String strStatus = "";
                String strSync = "";

                String strCreatedDate = "";
                String strModifiedDate = "";

                String strServermasterid = "";

                String strLead = "";

                lstmain.clear();

                if (c1.getCount() > 0) {
                    c1.moveToFirst();
                    for (int ii = 0; ii < c1.getCount(); ii++) {
                        clsBDMTrackerCalendar single = new clsBDMTrackerCalendar(
                                strdate, strevent, strtmonth, strtyear,
                                strtime, strremark, stractivity,
                                strSubActivity, strTimeTo, strUserID,
                                strStatus, strSync, strCreatedDate,
                                strModifiedDate, strServermasterid, strLead);

                        String strfromdate = c1.getString(c1
                                .getColumnIndex("DateNameBDMT"));

                        Date dt1 = null;
                        try {
                            dt1 = df.parse(strfromdate);
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        strfromdate = dateFormat.format(dt1);

                        // single.setDateName(c1.getString(c1.getColumnIndex("DateName")));
                        single.setDateName(strfromdate);
                        single.setEventName(c1.getString(c1
                                .getColumnIndex("EventNameBDMT")));
                        single.setTime(c1.getString(c1
                                .getColumnIndex("TimeBDMT")));
                        single.setRemark(c1.getString(c1
                                .getColumnIndex("Remark")));
                        single.setActivity(c1.getString(c1
                                .getColumnIndex("Activity")));
                        single.setSubActivity(c1.getString(c1
                                .getColumnIndex("SubActivity")));
                        single.setTimeTo(c1.getString(c1
                                .getColumnIndex("TimeTo")));
                        single.setStatus(c1.getString(c1
                                .getColumnIndex("ActivityStatus")));
                        single.setLead(c1.getString(c1
                                .getColumnIndex("ActivityLead")));
                        lstmain.add(single);
                        c1.moveToNext();
                    }
                }

                if (lstmain.size() > 0) {
                    ItemsAdapter adapter = new ItemsAdapter(context, 0, lstmain);
                    adapter.setNotifyOnChange(true);
                    todaylv.setAdapter(adapter);
                    setTodaysList(todaylv);
                } else {
                    ArrayList<clsBDMTrackerCalendar> lstmain = new ArrayList<>();

                    ItemsAdapter adapter = new ItemsAdapter(context, 0, lstmain);
                    adapter.setNotifyOnChange(true);
                    todaylv.setAdapter(adapter);
                    setTodaysList(todaylv);
                }


            }
        });

        tvDisplayTime = findViewById(R.id.tvTime);
        ImageButton btnChangeTime = findViewById(R.id.btnChangeTime);

        ImageButton btnChangeTimeTo = findViewById(R.id.btnChangeTimeTo);

        ImageButton btnChangeTimeRecordActivity = findViewById(R.id.btnChangeTimeRecordActivity);

        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        ampm = c.get(Calendar.AM_PM);
        if (ampm == 0) {
            strampm = "AM";
        } else {
            strampm = "PM";
        }


        btnChangeTime.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {

                timecheck = 1;

                showDialog(TIME_DIALOG_ID);
            }
        });

        btnChangeTimeRecordActivity.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {

                timecheck = 1;

                showDialog(TIME_DIALOG_ID);
            }
        });

        btnChangeTimeTo.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                timecheck = 0;
                showDialog(TIME_DIALOG_ID);

            }
        });

        lv = this.findViewById(R.id.list);

        registerForContextMenu(lv);

        lv.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {

            }

        });

        // Create a new service client and bind our activity to this service
        scheduleClient = new ScheduleClient(this);
        scheduleClient.doBindService();

        chk = findViewById(R.id.chk);
        chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

                String fulldate = etdate.getText().toString();

                if (fulldate.equalsIgnoreCase("")) {
                    if (arg1) {
                        validatedate();
                        chk.setChecked(false);
                    }
                } else {
                    // if(isOldDate())
                    // {
                    String[] spl = fulldate.split("-");

                    String a = spl[0];
                    String b = spl[1];
                    String c = spl[2];

                    if (b.contentEquals("January")) {
                        b = "0";
                    } else if (b.contentEquals("February")) {
                        b = "1";
                    } else if (b.contentEquals("March")) {
                        b = "2";
                    } else if (b.contentEquals("April")) {
                        b = "3";
                    } else if (b.contentEquals("May")) {
                        b = "4";
                    } else if (b.contentEquals("June")) {
                        b = "5";
                    } else if (b.contentEquals("July")) {
                        b = "6";
                    } else if (b.contentEquals("August")) {
                        b = "7";
                    } else if (b.contentEquals("September")) {
                        b = "8";
                    } else if (b.contentEquals("October")) {
                        b = "9";
                    } else if (b.contentEquals("November")) {
                        b = "10";
                    } else if (b.contentEquals("December")) {
                        b = "11";
                    }

                    // Get the date from our datepicker
                    intday = Integer.parseInt(a);
                    intmonth = Integer.parseInt(b);
                    intyear = Integer.parseInt(c);

                    /*
                     * // Create a new calendar set to the date chosen // we set
                     * the time to midnight (i.e. the first minute of that day)
                     * Calendar ca = Calendar.getInstance(); ca.set(intyear,
                     * intmonth, intday); ca.set(Calendar.HOUR_OF_DAY, 0);
                     * ca.set(Calendar.MINUTE, 0); ca.set(Calendar.SECOND, 0);
                     * // Ask our service to set an alarm for that date, this
                     * activity talks to the client that talks to the service
                     * scheduleClient.setAlarmForNotification(ca); // Notify the
                     * user what they just did //Toast.makeText(this,
                     * "Notification set for: "+ day +"/"+ (month+1) +"/"+ year,
                     * Toast.LENGTH_SHORT).show();
                     */
                }
                // }

            }
        });

        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        /*
         * mYearT= cal.get(Calendar.YEAR); mMonthT = cal.get(Calendar.MONTH);
         * mDayT= cal.get(Calendar.DAY_OF_MONTH);
         */

        todaylv = findViewById(R.id.todayslist);

        displayTodayEvents();

        // activity recorder

        todaylvra = findViewById(R.id.todayslistra);

        registerForContextMenu(todaylvra);

        todaylvra.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub


				  /*String ii = todaylvra.getAdapter().getItem(position).toString();
				  String g = todaylvra.getItemAtPosition(position).toString();

				  int rowid = Integer.parseInt(g) + 1;

				 String strrowid = String.valueOf(rowid);

				  String strSync = db.getSyncAR(strrowid);
					if (strSync.contentEquals("False")) {

				  Cursor c1 = db.getSelectedRowRecord_ar(rowid);

				  ArrayList<String> lstevent = new ArrayList<String>();

				  lstevent.clear();

				  if (c1.getCount() > 0) { c1.moveToFirst(); for (int ri = 0;
				  ri < c1.getCount(); ri++) {
				  lstevent.add(c1.getString(c1.getColumnIndex("DateNameAR")));
				  lstevent.add(c1.getString(c1.getColumnIndex("EventNameAR")));
				  lstevent.add(c1.getString(c1.getColumnIndex("TimeAR")));
				  lstevent.add(c1.getString(c1.getColumnIndex("RemarkAR")));
				  lstevent.add(c1.getString(c1.getColumnIndex("ActivityAR")));
				  lstevent.add(c1.getString(c1.getColumnIndex("SubActivityAR")));
				  lstevent.add(c1.getString(c1.getColumnIndex("TimeToAR")));
				  lstevent.add(c1.getString(c1.getColumnIndex("ActivityLeadAR")));
				  c1.moveToNext(); } }

				  Intent i = new Intent(context, ViewRecordActivityAR.class);

					Date dt1 = null;
					try {
						dt1 = (Date) df.parse(lstevent.get(0).toString());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String effdate = dateFormat.format(dt1);

					i.putExtra("Date", effdate);
					i.putExtra("Branch", lstevent.get(1).toString());
					i.putExtra("Activity", lstevent.get(4).toString());
					i.putExtra("Remark", lstevent.get(3).toString());
					i.putExtra("Time", lstevent.get(2).toString());

					i.putExtra("SubActivity", lstevent.get(5).toString());
					i.putExtra("TimeTo", lstevent.get(6).toString());
					i.putExtra("ActivityLead", lstevent.get(7).toString());

					i.putExtra("RowId", String.valueOf(rowid));

					startActivity(i);

					}*/
            }

        });

        displayTodayEventsra();


        int intActivityCount = db.GetActivityCount();
        if (intActivityCount == 0) {
            if (mCommonMethods.isNetworkConnected(context)) {
                startDownloadActivity();
            } else {
                mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
            }
        } else {

            Cursor c1 = db.GetAllActivityName();

            ArrayList<String> lstevent = new ArrayList<>();

            lstevent.clear();

            if (c1.getCount() > 0) {
                c1.moveToFirst();
                for (int ri = 0; ri < c1.getCount(); ri++) {
                    lstevent.add(c1.getString(c1
                            .getColumnIndex("ActivityCategoryName")));
                    c1.moveToNext();
                }
            }

            ArrayAdapter<String> activityAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1,
                    android.R.id.text1, lstevent);
            selActivity.setAdapter(activityAdapter);
            activityAdapter.notifyDataSetChanged();

            ArrayList<String> lsteventAct = new ArrayList<>();
            lsteventAct.clear();

            if (c1.getCount() > 0) {
                c1.moveToFirst();
                for (int ri = 0; ri < c1.getCount(); ri++) {
                    lsteventAct.add(c1.getString(c1
                            .getColumnIndex("ActivityCategoryName")));
                    c1.moveToNext();
                }
            }

            ArrayAdapter<String> activityAdapterActivityRecorder = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1,
                    android.R.id.text1, lsteventAct);
            selActivityRecordActivity
                    .setAdapter(activityAdapterActivityRecorder);
            activityAdapterActivityRecorder.notifyDataSetChanged();

            int intBDMBankBranchCount = db.GetBranchCount(strCIFBDMUserId);
            if (intBDMBankBranchCount == 0) {
                if (mCommonMethods.isNetworkConnected(context)) {
                    startDownloadBankBranch();
                } else {
                    mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
                }
            }

        }

        selActivityRecordActivity
                .setOnItemSelectedListener(new OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {

                        strActivity = selActivityRecordActivity
                                .getSelectedItem().toString();

                        strId = db.getActivityId(strActivity);

                        ArrayList<String> lstSubAct = new ArrayList<>();

                        int count = db.GetSubActivityCount(strId);
                        if (count == 0) {
                            taskSubActivity = new DownloadSubActivity();

                            if (mCommonMethods.isNetworkConnected(context)) {
                                startDownloadSubActivity();
                            } else {
                                mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
                            }
                        } else {
                            Cursor c1 = db.getSubActivityName(strId);

                            lstSubAct.clear();

                            if (c1.getCount() > 0) {
                                c1.moveToFirst();
                                for (int ri = 0; ri < c1.getCount(); ri++) {
                                    lstSubAct.add(c1.getString(c1
                                            .getColumnIndex("ActivitySubCategoryName")));
                                    c1.moveToNext();
                                }
                            }

                            ArrayAdapter<String> activityAdapterActivityRecorder = new ArrayAdapter<>(
                                    context,
                                    android.R.layout.simple_list_item_1,
                                    android.R.id.text1, lstSubAct);
                            selSubActivityRecordActivity
                                    .setAdapter(activityAdapterActivityRecorder);
                            activityAdapterActivityRecorder
                                    .notifyDataSetChanged();
                        }

                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        selSubActivityRecordActivity
                .setOnItemSelectedListener(new OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {

                        // strSubActivity =
                        // selSubActivityRecordActivity.getSelectedItem().toString();
                        strSubActivity = selSubActivityRecordActivity
                                .getItemAtPosition(position).toString();

                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        // get branch

        int intBDMBankBranchCount = db.GetBranchCount(strCIFBDMUserId);
        if (intBDMBankBranchCount == 0) {

            selCommitment
                    .setOnItemSelectedListener(new OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent,
                                                   View view, int position, long id) {

                            strCode = selCommitment.getItemAtPosition(position)
                                    .toString();

                            /* Changes done by machindra 23/09/2014 */


                            String strBranchName = db.getBranchName(strCode);

                            if (strBranchName
                                    .contentEquals("Select Branch Name")) {
                                edcommitbname.setText("");
                            } else {
                                edcommitbname.setText(strBranchName);
                            }

                            // when change branch code total field become blank
                            edcommitnewbusinesstot.setText("");
                            edcommithomeloantot.setText("");
                            edcommitnewbusinesspretot.setText("");
                            edcommitsharesingletot.setText("");
                            edcommitremarktot.setText("");

                            edcommitnewbusinesscash.setText("");
                            edcommithomeloan.setText("");
                            edcommitnewbusinesspre.setText("");
                            edcommitsharesingle.setText("");
                            edcommitremark.setText("");

                            ArrayList<String> lstevent = new ArrayList<>();

                            Cursor c = db.GetDefineObjectiveRecord(strCode,
                                    strCIFBDMUserId);
                            if (c.getCount() > 0) {
                                c.moveToFirst();
                                for (int ii = 0; ii < c.getCount(); ii++) {
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("DefineObjectiveBranchCode")));
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("DefineObjectiveBranchName")));
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("DefineObjectiveNewBusCash")));
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("DefineObjectiveHomeLoan")));
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("DefineObjectiveNewBusPre")));
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("DefineObjectiveShare")));
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("DefineObjectiveRemark")));
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("DefineObjectiveUserId")));
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("DefineObjectiveID")));
                                    c.moveToNext();
                                }

                                edcommitnewbusinesscash.setText(lstevent.get(2) == null ? "" : lstevent
                                        .get(2));
                                edcommithomeloan.setText(lstevent.get(3) == null ? "" : lstevent
                                        .get(3));
                                edcommitnewbusinesspre.setText(lstevent.get(4) == null ? "" : lstevent
                                        .get(4));
                                edcommitsharesingle.setText(lstevent.get(5) == null ? "" : lstevent
                                        .get(5));
                                edcommitremark.setText(lstevent.get(6) == null ? "" : lstevent
                                        .get(6));
                            }

                        }

                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

            selAchievement
                    .setOnItemSelectedListener(new OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent,
                                                   View view, int position, long id) {

                            strCode_ach = selAchievement.getItemAtPosition(
                                    position).toString();


                            String strBranchName = db.getBranchName(strCode_ach);

                            if (strBranchName
                                    .contentEquals("Select Branch Name")) {
                                edachievebrname.setText("");
                            } else {
                                edachievebrname.setText(strBranchName);

                                if (mCommonMethods.isNetworkConnected(context)) {
                                    taskRinRaksha = new DownloadRinRaksha();
                                    startdownloadRinraksha();
                                } else {
                                    mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
                                }
                            }
                        }

                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

            selPerAchievement
                    .setOnItemSelectedListener(new OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent,
                                                   View view, int position, long id) {

                            strCode_per = selPerAchievement.getItemAtPosition(
                                    position).toString();


                            String strBranchName = db.getBranchName(strCode_per);

                            if (strBranchName
                                    .contentEquals("Select Branch Name")) {
                                edperachievebrname.setText("");
                            } else {
                                edperachievebrname.setText(strBranchName);

                                // calculation

                                if (strCode_per.contentEquals(strCode)) {
                                    if (strCode_per.contentEquals(strCode_ach)) {
                                        if (edcommitnewbusinesscash.getText()
                                                .toString()
                                                .equalsIgnoreCase("")
                                                && edcommithomeloan.getText()
                                                .toString()
                                                .equalsIgnoreCase("")
                                                && edcommitnewbusinesspre
                                                .getText().toString()
                                                .equalsIgnoreCase("")
                                                && edcommitsharesingle
                                                .getText().toString()
                                                .equalsIgnoreCase("")) {
                                            if (edachievenewbusinesscash
                                                    .getText().toString()
                                                    .equalsIgnoreCase("")
                                                    && edachievehomeloan
                                                    .getText()
                                                    .toString()
                                                    .equalsIgnoreCase(
                                                            "")
                                                    && edachievenewbusinesspre
                                                    .getText()
                                                    .toString()
                                                    .equalsIgnoreCase(
                                                            "")
                                                    && edachievesharesingle
                                                    .getText()
                                                    .toString()
                                                    .equalsIgnoreCase(
                                                            "")) {

                                                double newbusiness;
                                                double individual;
                                                double renewal;
                                                double rin;

                                                if (edcommitnewbusinesscash
                                                        .getText().toString()
                                                        .contentEquals("0")
                                                        || edachievenewbusinesscash
                                                        .getText()
                                                        .toString()
                                                        .contentEquals(
                                                                "0")
                                                        || edcommitnewbusinesscash
                                                        .getText()
                                                        .toString()
                                                        .contentEquals(
                                                                "")
                                                        || edachievenewbusinesscash
                                                        .getText()
                                                        .toString()
                                                        .contentEquals(
                                                                "")) {
                                                    newbusiness = 0;
                                                } else {
                                                    newbusiness = (Double
                                                            .parseDouble(edcommitnewbusinesscash
                                                                    .getText()
                                                                    .toString())
                                                            / Double.parseDouble(edachievenewbusinesscash
                                                            .getText()
                                                            .toString()) * 100);
                                                }

                                                if (edcommithomeloan.getText()
                                                        .toString()
                                                        .contentEquals("0")
                                                        || edachievehomeloan
                                                        .getText()
                                                        .toString()
                                                        .contentEquals(
                                                                "0")
                                                        || edcommithomeloan
                                                        .getText()
                                                        .toString()
                                                        .contentEquals(
                                                                "")
                                                        || edachievehomeloan
                                                        .getText()
                                                        .toString()
                                                        .contentEquals(
                                                                "")) {
                                                    individual = 0;
                                                } else {
                                                    individual = (Double
                                                            .parseDouble(edcommithomeloan
                                                                    .getText()
                                                                    .toString())
                                                            / Double.parseDouble(edachievehomeloan
                                                            .getText()
                                                            .toString()) * 100);
                                                }

                                                if (edcommitnewbusinesspre
                                                        .getText().toString()
                                                        .contentEquals("0")
                                                        || edachievenewbusinesspre
                                                        .getText()
                                                        .toString()
                                                        .contentEquals(
                                                                "0")
                                                        || edcommitnewbusinesspre
                                                        .getText()
                                                        .toString()
                                                        .contentEquals(
                                                                "")
                                                        || edachievenewbusinesspre
                                                        .getText()
                                                        .toString()
                                                        .contentEquals(
                                                                "")) {
                                                    renewal = 0;
                                                } else {
                                                    renewal = (Double
                                                            .parseDouble(edcommitnewbusinesspre
                                                                    .getText()
                                                                    .toString())
                                                            / Double.parseDouble(edachievenewbusinesspre
                                                            .getText()
                                                            .toString()) * 100);
                                                }

                                                if (edcommitsharesingle
                                                        .getText().toString()
                                                        .contentEquals("0")
                                                        || edachievesharesingle
                                                        .getText()
                                                        .toString()
                                                        .contentEquals(
                                                                "0")
                                                        || edcommitsharesingle
                                                        .getText()
                                                        .toString()
                                                        .contentEquals(
                                                                "")
                                                        || edachievesharesingle
                                                        .getText()
                                                        .toString()
                                                        .contentEquals(
                                                                "")) {
                                                    rin = 0;
                                                } else {
                                                    rin = (Double
                                                            .parseDouble(edcommitsharesingle
                                                                    .getText()
                                                                    .toString())
                                                            / Double.parseDouble(edachievesharesingle
                                                            .getText()
                                                            .toString()) * 100);
                                                }

                                                edperachievenewbusiness.setText(currencyFormat
                                                        .format(newbusiness));
                                                edperachieveshare.setText(currencyFormat
                                                        .format(individual));
                                                edperachievenewbusinesspre.setText(currencyFormat
                                                        .format(renewal));
                                                edperachievehomeloan.setText(currencyFormat
                                                        .format(rin));
                                            } else {
                                                edperachievenewbusiness
                                                        .setText("");
                                                edperachieveshare.setText("");
                                                edperachievenewbusinesspre
                                                        .setText("");
                                                edperachievehomeloan
                                                        .setText("");
                                            }
                                        } else {
                                            edperachievenewbusiness.setText("");
                                            edperachieveshare.setText("");
                                            edperachievenewbusinesspre
                                                    .setText("");
                                            edperachievehomeloan.setText("");
                                        }
                                    } else {
                                        edperachievenewbusiness.setText("");
                                        edperachieveshare.setText("");
                                        edperachievenewbusinesspre.setText("");
                                        edperachievehomeloan.setText("");
                                    }
                                } else {
                                    edperachievenewbusiness.setText("");
                                    edperachieveshare.setText("");
                                    edperachievenewbusinesspre.setText("");
                                    edperachievehomeloan.setText("");
                                }
                            }
                        }

                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
        } else {
            Cursor c1 = db.GetAllBranchName(strCIFBDMUserId);

            ArrayList<String> lstevent = new ArrayList<>();

            lstevent.clear();

            if (c1.getCount() > 0) {
                c1.moveToFirst();
                for (int ri = 0; ri < c1.getCount(); ri++) {
                    lstevent.add(c1.getString(c1.getColumnIndex("BranchName")));
                    c1.moveToNext();
                }
            }

            ArrayAdapter<String> bankbranchAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1,
                    android.R.id.text1, lstevent);
            selBranch.setAdapter(bankbranchAdapter);
            bankbranchAdapter.notifyDataSetChanged();

            ArrayList<String> lsteventAct = new ArrayList<>();
            lsteventAct.clear();

            if (c1.getCount() > 0) {
                c1.moveToFirst();
                for (int ri = 0; ri < c1.getCount(); ri++) {
                    lsteventAct.add(c1.getString(c1
                            .getColumnIndex("BranchName")));
                    c1.moveToNext();
                }
            }

            ArrayAdapter<String> bankbranchAdapterActivityRecorder = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1,
                    android.R.id.text1, lsteventAct);
            selBranchRecordActivity
                    .setAdapter(bankbranchAdapterActivityRecorder);
            bankbranchAdapterActivityRecorder.notifyDataSetChanged();

            // for define objective
            ArrayList<String> lstbc = new ArrayList<>();

            lstbc.clear();

            if (c1.getCount() > 0) {
                c1.moveToFirst();
                for (int ri = 0; ri < c1.getCount(); ri++) {
                    lstbc.add(c1.getString(c1.getColumnIndex("BranchParentId")));
                    c1.moveToNext();
                }
            }

            ArrayAdapter<String> commitmentAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1,
                    android.R.id.text1, lstbc);
            selCommitment.setAdapter(commitmentAdapter);
            commitmentAdapter.notifyDataSetChanged();

            selCommitment
                    .setOnItemSelectedListener(new OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent,
                                                   View view, int position, long id) {

                            strCode = selCommitment.getItemAtPosition(position)
                                    .toString();
                            String strBranchName = db.getBranchName(strCode);
                            if (strBranchName
                                    .contentEquals("Select Branch Name")) {
                                edcommitbname.setText("");
                            } else {
                                edcommitbname.setText(strBranchName);
                            }

                            // when change branch code total field become blank
                            edcommitnewbusinesstot.setText("");
                            edcommithomeloantot.setText("");
                            edcommitnewbusinesspretot.setText("");
                            edcommitsharesingletot.setText("");
                            edcommitremarktot.setText("");

                            edcommitnewbusinesscash.setText("");
                            edcommithomeloan.setText("");
                            edcommitnewbusinesspre.setText("");
                            edcommitsharesingle.setText("");
                            edcommitremark.setText("");

                            ArrayList<String> lstevent = new ArrayList<>();

                            Cursor c = db.GetDefineObjectiveRecord(strCode,
                                    strCIFBDMUserId);
                            if (c.getCount() > 0) {
                                c.moveToFirst();
                                for (int ii = 0; ii < c.getCount(); ii++) {
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("DefineObjectiveBranchCode")));
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("DefineObjectiveBranchName")));
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("DefineObjectiveNewBusCash")));
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("DefineObjectiveHomeLoan")));
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("DefineObjectiveNewBusPre")));
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("DefineObjectiveShare")));
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("DefineObjectiveRemark")));
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("DefineObjectiveUserId")));
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("DefineObjectiveID")));
                                    c.moveToNext();
                                }

                                edcommitnewbusinesscash.setText(lstevent.get(2) == null ? "" : lstevent
                                        .get(2));
                                edcommithomeloan.setText(lstevent.get(3) == null ? "" : lstevent
                                        .get(3));
                                edcommitnewbusinesspre.setText(lstevent.get(4) == null ? "" : lstevent
                                        .get(4));
                                edcommitsharesingle.setText(lstevent.get(5) == null ? "" : lstevent
                                        .get(5));
                                edcommitremark.setText(lstevent.get(6) == null ? "" : lstevent
                                        .get(6));
                            }

                        }

                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

            ArrayAdapter<String> achievementAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1,
                    android.R.id.text1, lstbc);
            selAchievement.setAdapter(achievementAdapter);
            achievementAdapter.notifyDataSetChanged();

            selAchievement
                    .setOnItemSelectedListener(new OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent,
                                                   View view, int position, long id) {

                            strCode_ach = selAchievement.getItemAtPosition(
                                    position).toString();
                            String strBranchName = db
                                    .getBranchName(strCode_ach);
                            if (strBranchName
                                    .contentEquals("Select Branch Name")) {
                                edachievebrname.setText("");
                            } else {
                                edachievebrname.setText(strBranchName);

                                if (mCommonMethods.isNetworkConnected(context)) {
                                    taskRinRaksha = new DownloadRinRaksha();
                                    startdownloadRinraksha();
                                } else {
                                    mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
                                }
                            }
                        }

                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

            ArrayAdapter<String> perachievementAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1,
                    android.R.id.text1, lstbc);
            selPerAchievement.setAdapter(perachievementAdapter);
            perachievementAdapter.notifyDataSetChanged();

            selPerAchievement
                    .setOnItemSelectedListener(new OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent,
                                                   View view, int position, long id) {

                            strCode_per = selPerAchievement.getItemAtPosition(
                                    position).toString();
                            String strBranchName = db
                                    .getBranchName(strCode_per);
                            if (strBranchName
                                    .contentEquals("Select Branch Name")) {
                                edperachievebrname.setText("");
                            } else {
                                edperachievebrname.setText(strBranchName);

                                // calculation

                                if (strCode_per.contentEquals(strCode)) {
                                    if (strCode_per.contentEquals(strCode_ach)) {
                                        if (!edcommitnewbusinesscash.getText()
                                                .toString()
                                                .equalsIgnoreCase("")
                                                && !edcommithomeloan.getText()
                                                .toString()
                                                .equalsIgnoreCase("")
                                                && !edcommitnewbusinesspre
                                                .getText().toString()
                                                .equalsIgnoreCase("")
                                                && !edcommitsharesingle
                                                .getText().toString()
                                                .equalsIgnoreCase("")) {
                                            if (!edachievenewbusinesscash
                                                    .getText().toString()
                                                    .equalsIgnoreCase("")
                                                    && !edachievehomeloan
                                                    .getText()
                                                    .toString()
                                                    .equalsIgnoreCase(
                                                            "")
                                                    && !edachievenewbusinesspre
                                                    .getText()
                                                    .toString()
                                                    .equalsIgnoreCase(
                                                            "")
                                                    && !edachievesharesingle
                                                    .getText()
                                                    .toString()
                                                    .equalsIgnoreCase(
                                                            "")) {

                                                double newbusiness;
                                                double individual;
                                                double renewal;
                                                double rin;

                                                if (edcommitnewbusinesscash
                                                        .getText().toString()
                                                        .contentEquals("0")
                                                        || edachievenewbusinesscash
                                                        .getText()
                                                        .toString()
                                                        .contentEquals(
                                                                "0")
                                                        || edcommitnewbusinesscash
                                                        .getText().toString()
                                                        .contentEquals("")
                                                        || edachievenewbusinesscash
                                                        .getText()
                                                        .toString()
                                                        .contentEquals(
                                                                "")) {
                                                    newbusiness = 0;
                                                } else {
                                                    newbusiness = (Double
                                                            .parseDouble(edcommitnewbusinesscash
                                                                    .getText()
                                                                    .toString())
                                                            / Double.parseDouble(edachievenewbusinesscash
                                                            .getText()
                                                            .toString()) * 100);
                                                }

                                                if (edcommithomeloan.getText()
                                                        .toString()
                                                        .contentEquals("0")
                                                        || edachievehomeloan
                                                        .getText()
                                                        .toString()
                                                        .contentEquals(
                                                                "0")
                                                        || edcommithomeloan
                                                        .getText()
                                                        .toString()
                                                        .contentEquals(
                                                                "")
                                                        || edachievehomeloan
                                                        .getText()
                                                        .toString()
                                                        .contentEquals(
                                                                "")) {
                                                    individual = 0;
                                                } else {
                                                    individual = (Double
                                                            .parseDouble(edcommithomeloan
                                                                    .getText()
                                                                    .toString())
                                                            / Double.parseDouble(edachievehomeloan
                                                            .getText()
                                                            .toString()) * 100);
                                                }

                                                if (edcommitnewbusinesspre
                                                        .getText().toString()
                                                        .contentEquals("0")
                                                        || edachievenewbusinesspre
                                                        .getText()
                                                        .toString()
                                                        .contentEquals(
                                                                "0")
                                                        || edcommitnewbusinesspre
                                                        .getText()
                                                        .toString()
                                                        .contentEquals(
                                                                "")
                                                        || edachievenewbusinesspre
                                                        .getText()
                                                        .toString()
                                                        .contentEquals(
                                                                "")) {
                                                    renewal = 0;
                                                } else {
                                                    renewal = (Double
                                                            .parseDouble(edcommitnewbusinesspre
                                                                    .getText()
                                                                    .toString())
                                                            / Double.parseDouble(edachievenewbusinesspre
                                                            .getText()
                                                            .toString()) * 100);
                                                }

                                                if (edcommitsharesingle
                                                        .getText().toString()
                                                        .contentEquals("0")
                                                        || edachievesharesingle
                                                        .getText()
                                                        .toString()
                                                        .contentEquals(
                                                                "0")
                                                        || edcommitsharesingle
                                                        .getText()
                                                        .toString()
                                                        .contentEquals(
                                                                "")
                                                        || edachievesharesingle
                                                        .getText()
                                                        .toString()
                                                        .contentEquals(
                                                                "")) {
                                                    rin = 0;
                                                } else {
                                                    rin = (Double
                                                            .parseDouble(edcommitsharesingle
                                                                    .getText()
                                                                    .toString())
                                                            / Double.parseDouble(edachievesharesingle
                                                            .getText()
                                                            .toString()) * 100);
                                                }

                                                edperachievenewbusiness.setText(currencyFormat
                                                        .format(newbusiness));
                                                edperachieveshare.setText(currencyFormat
                                                        .format(individual));
                                                edperachievenewbusinesspre.setText(currencyFormat
                                                        .format(renewal));
                                                edperachievehomeloan.setText(currencyFormat
                                                        .format(rin));
                                            } else {
                                                edperachievenewbusiness
                                                        .setText("");
                                                edperachieveshare.setText("");
                                                edperachievenewbusinesspre
                                                        .setText("");
                                                edperachievehomeloan
                                                        .setText("");
                                            }
                                        } else {
                                            edperachievenewbusiness.setText("");
                                            edperachieveshare.setText("");
                                            edperachievenewbusinesspre
                                                    .setText("");
                                            edperachievehomeloan.setText("");
                                        }
                                    } else {
                                        edperachievenewbusiness.setText("");
                                        edperachieveshare.setText("");
                                        edperachievenewbusinesspre.setText("");
                                        edperachievehomeloan.setText("");
                                    }
                                } else {
                                    edperachievenewbusiness.setText("");
                                    edperachieveshare.setText("");
                                    edperachievenewbusinesspre.setText("");
                                    edperachievehomeloan.setText("");
                                }

                            }
                        }

                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

            // for branch pofile
            ArrayList<String> lstbf = new ArrayList<>();

            lstbf.clear();

            if (c1.getCount() > 0) {
                c1.moveToFirst();
                for (int ri = 0; ri < c1.getCount(); ri++) {
                    lstbf.add(c1.getString(c1.getColumnIndex("BranchParentId")));
                    c1.moveToNext();
                }
            }

            ArrayAdapter<String> bcodeAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1,
                    lstbf);
            selbranchprofilecode.setAdapter(bcodeAdapter);
            bcodeAdapter.notifyDataSetChanged();

            int intParamCount = db.GetParamCount();
            if (intParamCount == 0) {

                if (mCommonMethods.isNetworkConnected(context)) {
                    startDownloadParamList();
                } else {
                    mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
                }
            }

        }

        // get param list
        int intParamCount = db.GetParamCount();
        if (intParamCount == 0) {

        } else {
            Cursor c1 = db.GetAllParamName();

            ArrayList<String> lstevent = new ArrayList<>();

            lstevent.clear();

            if (c1.getCount() > 0) {
                c1.moveToFirst();
                for (int ri = 0; ri < c1.getCount(); ri++) {
                    lstevent.add(c1.getString(c1.getColumnIndex("ParamName")));
                    c1.moveToNext();
                }
            }

            for (int i = 0; i < lstevent.size(); i++) {
                if (i == 0) {
                    txtcommtnewbuscash.setText(lstevent.get(i));
                    txtachnewbuscash.setText(lstevent.get(i));
                    txtperachnewbuscash.setText(lstevent.get(i));
                } else if (i == 1) {

                    txtcommtshare.setText(lstevent.get(i));
                    txtachshare.setText(lstevent.get(i));
                    txtperachshare.setText(lstevent.get(i));
                } else if (i == 2) {

                    txtcommtnewbuspre.setText(lstevent.get(i));
                    txtachnewbuspre.setText(lstevent.get(i));
                    txtperachnewbuspre.setText(lstevent.get(i));
                } else if (i == 3) {

                    txtcommthomeloan.setText(lstevent.get(i));
                    txtachhomeloan.setText(lstevent.get(i));
                    txtperachhomeloan.setText(lstevent.get(i));
                }
            }
        }


        //Turbo BDM Condition

        if (strCIFBDMUserType.contentEquals("TBDM")) {
            btnleadmgt.setVisibility(View.VISIBLE);
        } else {
            btnleadmgt.setVisibility(View.GONE);
        }

        //end Turbo BDM Condition

    }

    private void setNextMonth() {
        if (month.get(Calendar.MONTH) == month
                .getActualMaximum(Calendar.MONTH)) {
            month.set((month.get(Calendar.YEAR) + 1),
                    month.getActualMinimum(Calendar.MONTH), 1);

            strmonth = month.get(Calendar.MONTH) + 1;
            stryear = month.get(Calendar.YEAR) + 1;

        } else {
            month.set(Calendar.MONTH,
                    month.get(Calendar.MONTH) + 1);

            strmonth = month.get(Calendar.MONTH) + 1;
            stryear = month.get(Calendar.YEAR);
        }

    }

    private void setPreviousMonth() {
        if (month.get(Calendar.MONTH) == month.getActualMinimum(Calendar.MONTH)) {
            month.set((month.get(Calendar.YEAR) - 1),
                    month.getActualMaximum(Calendar.MONTH), 1);

            strmonth = month.get(Calendar.MONTH);
            stryear = month.get(Calendar.YEAR) - 1;

        } else {
            month.set(Calendar.MONTH,
                    month.get(Calendar.MONTH) - 1);

            strmonth = month.get(Calendar.MONTH) + 1;
            stryear = month.get(Calendar.YEAR);
        }

    }


    private void refreshCalendar() {
        // TextView title = (TextView) findViewById(R.id.title);

        adapter.refreshDays();
        adapter.notifyDataSetChanged();
        handler.post(calendarUpdater); // generate some calendar items

        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

        displayTodayEvents();
    }

    private final Runnable calendarUpdater = new Runnable() {

        public void run() {
            items.clear();

            String strm = String.valueOf(strmonth);

            if (strm.contentEquals("1")) {
                strm = "January";
            } else if (strm.contentEquals("2")) {
                strm = "February";
            } else if (strm.contentEquals("3")) {
                strm = "March";
            } else if (strm.contentEquals("4")) {
                strm = "April";
            } else if (strm.contentEquals("5")) {
                strm = "May";
            } else if (strm.contentEquals("6")) {
                strm = "June";
            } else if (strm.contentEquals("7")) {
                strm = "July";
            } else if (strm.contentEquals("8")) {
                strm = "August";
            } else if (strm.contentEquals("9")) {
                strm = "September";
            } else if (strm.contentEquals("10")) {
                strm = "October";
            } else if (strm.contentEquals("11")) {
                strm = "November";
            } else if (strm.contentEquals("12")) {
                strm = "December";
            }

            final SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "dd-MMMM-yyyy", Locale.ENGLISH);
            final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            Cursor c = db.getdateBDMT(strm, String.valueOf(stryear));

            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int ii = 0; ii < c.getCount(); ii++) {
                    // items.add(c.getString(c.getColumnIndex("DateName")));
                    String strfromdate = c.getString(c
                            .getColumnIndex("DateNameBDMT"));

                    Date dt1 = null;
                    try {
                        dt1 = df.parse(strfromdate);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    strfromdate = dateFormat.format(dt1);

                    // 03-May-2013 become 3-May-2013 for appear symbol on date
                    // grid control

                    String[] spl = strfromdate.split("-");

                    String a = spl[0];
                    String b = spl[1];
                    String c1 = spl[2];

                    if (a.contentEquals("01")) {
                        a = "1";
                        strfromdate = a + "-" + b + "-" + c1;
                    } else if (a.contentEquals("02")) {
                        a = "2";
                        strfromdate = a + "-" + b + "-" + c1;
                    } else if (a.contentEquals("03")) {
                        a = "3";
                        strfromdate = a + "-" + b + "-" + c1;
                    } else if (a.contentEquals("04")) {
                        a = "4";
                        strfromdate = a + "-" + b + "-" + c1;
                    } else if (a.contentEquals("05")) {
                        a = "5";
                        strfromdate = a + "-" + b + "-" + c1;
                    } else if (a.contentEquals("06")) {
                        a = "6";
                        strfromdate = a + "-" + b + "-" + c1;
                    } else if (a.contentEquals("07")) {
                        a = "7";
                        strfromdate = a + "-" + b + "-" + c1;
                    } else if (a.contentEquals("08")) {
                        a = "8";
                        strfromdate = a + "-" + b + "-" + c1;
                    } else if (a.contentEquals("09")) {
                        a = "9";
                        strfromdate = a + "-" + b + "-" + c1;
                    }

                    // end of 03-May-2013 become 3-May-2013 for appear symbol on
                    // date grid control

                    items.add(strfromdate);
                    c.moveToNext();
                }
            }

            /*
             * // Print dates of the current week DateFormat df = new
             * SimpleDateFormat("yyyy-MM-dd", Locale.US); Stringitemvalue; for
             * (int i = 0; i < 7; i++) { itemvalue =
             * df.format(itemmonth.getTime());
             * itemmonth.add(GregorianCalendar.DATE, 1);
             * items.add("2012-09-12"); items.add("2012-10-07");
             * items.add("2012-10-15"); items.add("2012-10-20");
             * items.add("2012-11-30"); items.add("2012-11-28"); }
             */

            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }
    };

    public void add(View v) {
        String date = etdate.getText().toString();
        // String eventname = "Branch: "
        // +selBranch.getSelectedItem().toString();
        String eventname = selBranch.getSelectedItem().toString();
        String month = m;
        String year = y;
        // String remark = "Remark: " + edremark.getText().toString();
        String remark = edremark.getText().toString();
        // String time = "Time: " + tvDisplayTime.getText().toString();
        String time = tvDisplayTime.getText().toString();
        // String activity = "Activity: "
        // +selActivity.getSelectedItem().toString();
        String activity = selActivity.getSelectedItem().toString();

        String lead = edlead_act.getText().toString();

        String strSubActivity = "";
        String strTimeTo = "";
        String strSync = "False";
        String strServerMasterId = "";

        // get current date and formate that into yyyy-MM-dd
        Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        int mDay = cal.get(Calendar.DAY_OF_MONTH);

        String y = String.valueOf(mYear);
        String m = String.valueOf(mMonth + 1);
        String da = String.valueOf(mDay);
        if (m.contentEquals("1")) {
            m = "01";
        } else if (m.contentEquals("2")) {
            m = "02";
        } else if (m.contentEquals("3")) {
            m = "03";
        } else if (m.contentEquals("4")) {
            m = "04";
        } else if (m.contentEquals("5")) {
            m = "05";
        } else if (m.contentEquals("6")) {
            m = "06";
        } else if (m.contentEquals("7")) {
            m = "07";
        } else if (m.contentEquals("8")) {
            m = "08";
        } else if (m.contentEquals("9")) {
            m = "09";
        }

        if (da.contentEquals("1")) {
            da = "01";
        } else if (da.contentEquals("2")) {
            da = "02";
        } else if (da.contentEquals("3")) {
            da = "03";
        } else if (da.contentEquals("4")) {
            da = "04";
        } else if (da.contentEquals("5")) {
            da = "05";
        } else if (da.contentEquals("6")) {
            da = "06";
        } else if (da.contentEquals("7")) {
            da = "07";
        } else if (da.contentEquals("8")) {
            da = "08";
        } else if (da.contentEquals("9")) {
            da = "09";
        }

        String strCreatedDate = y + "-" + m + "-" + da;
        String strModifiedDate = "0";

        // if (date.equalsIgnoreCase("") || eventname.equalsIgnoreCase("")
        // || time.equalsIgnoreCase("")) {
        if (date.equalsIgnoreCase("")
                || time.equalsIgnoreCase("")
                || selActivity.getSelectedItem().toString().equals("")
                || selBranch.getSelectedItem().toString()
                .equals("Select Branch Name")) {
            validation();

        } else {

            if (strActivity.contentEquals("Business Connect")
                    || strActivity.contentEquals("Promotional Activities")) {
                if (lead.equalsIgnoreCase("")) {
                    validation_lead();
                }
            } else {

                Date dt1 = null;
                try {
                    dt1 = dateFormat.parse(date);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                date = df.format(dt1);

                clsBDMTrackerCalendar cls = new clsBDMTrackerCalendar(date,
                        eventname, month == null ? "" : month,
                        year == null ? "" : year, time, remark, activity,
                        strSubActivity, strTimeTo, strCIFBDMUserId, "Open",
                        strSync, strCreatedDate, strModifiedDate,
                        strServerMasterId, lead);
                db.AddEventBDMT(cls);

                ok();

                etdate.setText("");
                edeventname.setText("");
                selActivity.setSelection(0);
                selBranch.setSelection(0);
                edremark.setText("");

                final Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);

                ampm = c.get(Calendar.AM_PM);
                if (ampm == 0) {
                    strampm = "AM";
                } else {
                    strampm = "PM";
                }
                tvDisplayTime.setText(new StringBuilder().append(pad(hour))
                        .append(":").append(pad(minute)).append(" ")
                        .append(strampm));

                // refresh grid
                refreshCalendar();

                // set alrm

                if (chk.isChecked()) {

                    // Create a new calendar set to the date chosen
                    // we set the time to midnight (i.e. the first minute of
                    // that
                    // day)
                    Calendar ca = Calendar.getInstance();
                    ca.set(intyear, intmonth, intday);
                    ca.set(Calendar.HOUR_OF_DAY, 0);
                    ca.set(Calendar.MINUTE, 0);
                    ca.set(Calendar.SECOND, 0);
                    // Ask our service to set an alarm for that date, this
                    // activity
                    // talks to the client that talks to the service
                    scheduleClient.setAlarmForNotification(ca);
                    // Notify the user what they just did
                    // Toast.makeText(this, "Notification set for: "+ day +"/"+
                    // (month+1) +"/"+ year, Toast.LENGTH_SHORT).show();

                }

                chk.setChecked(false);

            }

        }

    }

    public void cancel(View v) {

        tbl.getLayoutParams().height = 0;
        tbl.requestLayout();
    }

    public void addevent(View v) {

        // search record
        tblsearchactvity.getLayoutParams().height = 0;
        tblsearchactvity.requestLayout();
        // end search record

        // etdate.setText(""); //when click on add button date is blank coz
        // activity recorder date also assign same

        // set current date in add activity and add record activiti dt

        Calendar cal = Calendar.getInstance();
        mYearT = cal.get(Calendar.YEAR);
        mMonthT = cal.get(Calendar.MONTH);
        mDayT = cal.get(Calendar.DAY_OF_MONTH);

        y = String.valueOf(mYearT);
        m = String.valueOf(mMonthT + 1);
        d = String.valueOf(mDayT);
        String da = String.valueOf(mDayT);
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

        etdate.setText(totaldate);

        // end set date default in edittext in date

        // set current time
        final Calendar c = Calendar.getInstance();
        hourT = c.get(Calendar.HOUR_OF_DAY);
        minuteT = c.get(Calendar.MINUTE);

        ampmT = c.get(Calendar.AM_PM);
        if (ampmT == 0) {
            strampmT = "AM";
        } else {
            strampmT = "PM";
        }

        tvDisplayTime.setText(new StringBuilder().append(pad(hourT))
                .append(":").append(pad(minuteT)).append(" ").append(strampmT));
        // end set current time

        btnaddactivity.setBackgroundResource(R.drawable.exp_selected_sub);
        btnshowactivity.setBackgroundResource(R.drawable.exp_unselected_sub);

        tbl.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        tbl.requestLayout();

        lv.setVisibility(View.GONE);

        setObjectiveLayout.getLayoutParams().height = 0;
        setObjectiveLayout.requestLayout();
    }

    public void showallevents(View v) {
        // search record
        btnsearch.setBackgroundResource(R.drawable.exp_unselected);
        btnrefresh.setBackgroundResource(R.drawable.exp_unselected);

        edfromdate.setText("");
        edtodate.setText("");

        tblsearchactvity.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        tblsearchactvity.requestLayout();
        // end search record

        btnaddactivity.setBackgroundResource(R.drawable.exp_unselected_sub);
        btnshowactivity.setBackgroundResource(R.drawable.exp_selected_sub);

        tbl.getLayoutParams().height = 0;
        tbl.requestLayout();

        lv.setVisibility(View.VISIBLE);

        Cursor c1 = db.getAllEventsBDMT(strCIFBDMUserId);

        String strdate = "";
        String strevent = "";
        String strtmonth = "";
        String strtyear = "";
        String strtime = "";
        String strremark = "";
        String stractivity = "";

        String strSubActivity = "";
        String strTimeTo = "";
        String strUserID = "";
        String strStatus = "";
        String strSync = "";

        String strCreatedDate = "";
        String strModifiedDate = "";

        String strServerMasterId = "";

        String strLead = "";

        lstmain.clear();

        if (c1.getCount() > 0) {
            c1.moveToFirst();
            for (int ii = 0; ii < c1.getCount(); ii++) {
                clsBDMTrackerCalendar single = new clsBDMTrackerCalendar(
                        strdate, strevent, strtmonth, strtyear, strtime,
                        strremark, stractivity, strSubActivity, strTimeTo,
                        strUserID, strStatus, strSync, strCreatedDate,
                        strModifiedDate, strServerMasterId, strLead);
                // single.setDateName(c1.getString(c1.getColumnIndex("DateName")));

                String strfromdate = c1.getString(c1
                        .getColumnIndex("DateNameBDMT"));

                Date dt1 = null;
                try {
                    dt1 = df.parse(strfromdate);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                strfromdate = dateFormat.format(dt1);

                single.setDateName(strfromdate);
                single.setEventName(c1.getString(c1
                        .getColumnIndex("EventNameBDMT")));
                single.setTime(c1.getString(c1.getColumnIndex("TimeBDMT")));
                single.setRemark(c1.getString(c1.getColumnIndex("Remark")));
                single.setActivity(c1.getString(c1.getColumnIndex("Activity")));
                single.setSubActivity(c1.getString(c1
                        .getColumnIndex("SubActivity")));
                single.setTimeTo(c1.getString(c1.getColumnIndex("TimeTo")));
                single.setStatus(c1.getString(c1
                        .getColumnIndex("ActivityStatus")));
                single.setLead(c1.getString(c1.getColumnIndex("ActivityLead")));
                lstmain.add(single);
                c1.moveToNext();
            }
        }

        if (lstmain.size() > 0) {
            ItemsAdapter adapter = new ItemsAdapter(context, 0, lstmain);
            adapter.setNotifyOnChange(true);
            lv.setAdapter(adapter);
            setShowallList(lv);
        } else {
            ArrayList<clsBDMTrackerCalendar> lstmain = new ArrayList<>();

            ItemsAdapter adapter = new ItemsAdapter(context, 0, lstmain);
            adapter.setNotifyOnChange(true);
            lv.setAdapter(adapter);
            setShowallList(lv);
        }
    }

    public void setobjective(View v) {
        // search record
        tblsearchactvity.getLayoutParams().height = 0;
        tblsearchactvity.requestLayout();
        // end search record

        // setObjectiveLayout.getLayoutParams().height = 1100;
        setObjectiveLayout.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        setObjectiveLayout.requestLayout();

        lnbranchprofile.getLayoutParams().height = 0;
        lnbranchprofile.requestLayout();

        tbl.getLayoutParams().height = 0;
        tbl.requestLayout();

        todaysActiviy.setVisibility(View.GONE);
        todaylv.setVisibility(View.GONE);
        header.setVisibility(View.GONE);
        gridview.setVisibility(View.GONE);
        lnmonthsel.setVisibility(View.GONE);
        lv.setVisibility(View.GONE);

        lnmailer.getLayoutParams().height = 0;
        lnmailer.requestLayout();


        lnrecordactivity.setVisibility(View.GONE);

        lnplanner.getLayoutParams().height = 0;
        lnplanner.requestLayout();

        btnplanner.setBackgroundResource(R.drawable.exp_unselected);
        btnactivityrecorder.setBackgroundResource(R.drawable.exp_unselected);
        btndefineobj.setBackgroundResource(R.drawable.exp_selected);
        btnmailer.setBackgroundResource(R.drawable.exp_unselected);
        btnbranchpro.setBackgroundResource(R.drawable.exp_unselected);
    }

    public void addObjective(View v) {
        /*
         * setObjectiveLayout.getLayoutParams().height = 0;
         * setObjectiveLayout.requestLayout();
         */

        String strbranchname = edcommitbname.getText().toString();

        String strnewBusinesscash = edcommitnewbusinesscash.getText()
                .toString();
        String strhomeloan = edcommithomeloan.getText().toString();
        String strnewbusinesspre = edcommitnewbusinesspre.getText().toString();
        String strsharesingle = edcommitsharesingle.getText().toString();
        String strremark = edcommitremark.getText().toString();

        if (strCode.equalsIgnoreCase("Select Branch Code")
                || strbranchname.equalsIgnoreCase("Select Branch Name")) {
            validation();
        } else {
            if (strnewBusinesscash.equalsIgnoreCase("")
                    && strhomeloan.equalsIgnoreCase("")
                    && strnewbusinesspre.equalsIgnoreCase("")
                    && strsharesingle.equalsIgnoreCase("")) {
                validation();
            } else {

                int count = db.DefineObjectiveExistorNot(strCode,
                        strCIFBDMUserId);
                if (count == 0) {
                    clsDefineObjective obj = new clsDefineObjective(strCode,
                            strbranchname, strnewBusinesscash, strhomeloan,
                            strnewbusinesspre, strsharesingle, strremark,
                            strCIFBDMUserId);
                    db.AddDefineObjective(obj);

                    savedefineobjectivelert();
                } else {
                    existdefineobjectivelert();


                }

            }
        }

    }

    public void syncObjective(View v) {
        /*
         * setObjectiveLayout.getLayoutParams().height = 0;
         * setObjectiveLayout.requestLayout();
         */

        final String strbranchname = edcommitbname.getText().toString();
        final String strnewBusinesscash = edcommitnewbusinesscash.getText()
                .toString();
        final String strhomeloan = edcommithomeloan.getText().toString();
        final String strnewbusinesspre = edcommitnewbusinesspre.getText()
                .toString();
        final String strsharesingle = edcommitsharesingle.getText().toString();
        final String strremark = edcommitremark.getText().toString();

        if (!strnewBusinesscash.equalsIgnoreCase("")) {
            totalparam += 1;
        } else if (!strsharesingle.equalsIgnoreCase("")) {
            totalparam += 1;
        } else if (!strnewbusinesspre.equalsIgnoreCase("")) {
            totalparam += 1;
        } else if (!strhomeloan.equalsIgnoreCase("")) {
            totalparam += 1;
        }

        /*
         * final String paramid = ""; final int value = 0;
         */

        if (strCode.equalsIgnoreCase("Select Branch Code")
                || strbranchname.equalsIgnoreCase("Select Branch Name")) {
            validation();
        } else {
            // startDownloadSaveObjective();
            if (strnewBusinesscash.equalsIgnoreCase("")
                    && strhomeloan.equalsIgnoreCase("")
                    && strnewbusinesspre.equalsIgnoreCase("")
                    && strsharesingle.equalsIgnoreCase("")) {
                validation();
            } else {

                String strFlag = db.GetDefineObjectiveSyncRecord(strCode,
                        strCIFBDMUserId);
                if (strFlag.contentEquals("True")) {
                    activitysynalert();
                } else {

                    // final boolean running = true;

                    mProgressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
                    String Message = "Loading. Please wait...";
                    mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
                    mProgressDialog
                            .setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mProgressDialog.setCancelable(true);

                    mProgressDialog.setButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    taskActivity.cancel(true);
                                    taskSubActivity.cancel(true);
                                    taskList.cancel(true);
                                    taskListRecord.cancel(true);
                                    taskListDetail.cancel(true);
                                    taskListSeq.cancel(true);
                                    taskListBankBranch.cancel(true);
                                    taskListParamList.cancel(true);
                                    taskListSaveObjective.cancel(true);
                                    taskBranchProfile.cancel(true);
                                    taskBranchDeposits.cancel(true);
                                    taskBranchAdvances.cancel(true);
                                    taskSyncBranchProfile.cancel(true);
                                    taskRinRaksha.cancel(true);
                                    taskBdm_Dashboard.cancel(true);
                                    taskBdm_mail_data.cancel(true);
                                    taskLead.cancel(true);
                                    mProgressDialog.dismiss();
                                }
                            });

                    mProgressDialog.setMax(100);
                    mProgressDialog.show();

                    splashTread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                synchronized (this) {
                                    // wait(_splashTime);
                                }

                            } finally {

                                if (!strnewBusinesscash.equalsIgnoreCase("")) {
                                    if (defineobjective_running) {


                                        paramid = "1";
                                        value = Integer
                                                .parseInt(strnewBusinesscash);

                                        // boolean running = true;

                                        SoapObject request = new SoapObject(
                                                NAMESPACE,
                                                METHOD_NAME_SAVE_OBJECTIVE);

                                        request.addProperty(
                                                "objective_Param_mast_id",
                                                paramid);
                                        request.addProperty("bdmId",
                                                strCIFBDMUserId);
                                        request.addProperty("brCode", strCode);
                                        request.addProperty("brName",
                                                strbranchname);
                                        request.addProperty("parmaValue", value);
                                        request.addProperty("remarks",
                                                strremark);
                                        request.addProperty("strEmailId",
                                                strCIFBDMEmailId);
                                        request.addProperty("strMobileNo",
                                                strCIFBDMMObileNo);
                                        request.addProperty("strAuthKey",
                                                strCIFBDMPassword.trim());

                                        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                                                SoapEnvelope.VER11);
                                        envelope.dotNet = true;

                                        envelope.setOutputSoapObject(request);

                                        // 	allowAllSSL();

                                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                                .permitAll().build();
                                        StrictMode.setThreadPolicy(policy);

                                        HttpTransportSE androidHttpTranport = new HttpTransportSE(
                                                URl);
                                        try {
                                            try {
                                                androidHttpTranport
                                                        .call(SOAP_ACTION_SAVE_OBJECTIVE,
                                                                envelope);
                                            } catch (XmlPullParserException e) {
                                                try {
                                                    throw (e);
                                                } catch (Exception e1) {
                                                    e1.printStackTrace();
                                                }
                                                mProgressDialog.dismiss();
                                                defineobjective_running = false;
                                            }
                                            Object response = envelope
                                                    .getResponse();
                                            strResult = response.toString();
                                        } catch (IOException e) {
                                            try {
                                                throw (e);
                                            } catch (Exception e1) {
                                                e1.printStackTrace();
                                            }
                                            mProgressDialog.dismiss();
                                            defineobjective_running = false;
                                        }

                                        if (defineobjective_running) {
                                            if (!strResult.contentEquals("0")) {
                                                // mProgressDialog.dismiss();
                                                // mProgressDialog.dismiss();
                                                // syncerror();

                                                /*
                                                 * BDMTracker.this
                                                 * .runOnUiThread(new Runnable()
                                                 * {
                                                 *
                                                 * @Override public void run() {
                                                 * // TODO // Auto-generated //
                                                 * method stub tasksyncerror();
                                                 * } });
                                                 */
                                            } else {
                                                defineobjective_running = false;
                                                mProgressDialog.dismiss();
                                                // syncerror();

                                                BancaBDMTracker.this
                                                        .runOnUiThread(new Runnable() {

                                                            // @Override
                                                            public void run() {
                                                                // TODO
                                                                // Auto-generated
                                                                // method stub
                                                                syncerror();
                                                            }
                                                        });
                                            }
                                        } else {
                                            mProgressDialog.dismiss();
                                            // syncerror();
                                            BancaBDMTracker.this
                                                    .runOnUiThread(new Runnable() {

                                                        // @Override
                                                        public void run() {
                                                            // TODO
                                                            // Auto-generated
                                                            // method stub
                                                            syncerror();
                                                        }
                                                    });
                                        }
                                    }
                                }
                                if (!strsharesingle.equalsIgnoreCase("")) {
                                    if (defineobjective_running) {

                                        paramid = "2";
                                        value = Integer
                                                .parseInt(strsharesingle);

                                        // boolean running = true;

                                        SoapObject request = new SoapObject(
                                                NAMESPACE,
                                                METHOD_NAME_SAVE_OBJECTIVE);

                                        request.addProperty(
                                                "objective_Param_mast_id",
                                                paramid);
                                        request.addProperty("bdmId",
                                                strCIFBDMUserId);
                                        request.addProperty("brCode", strCode);
                                        request.addProperty("brName",
                                                strbranchname);
                                        request.addProperty("parmaValue", value);
                                        request.addProperty("remarks",
                                                strremark);
                                        request.addProperty("strEmailId",
                                                strCIFBDMEmailId);
                                        request.addProperty("strMobileNo",
                                                strCIFBDMMObileNo);
                                        request.addProperty("strAuthKey",
                                                strCIFBDMPassword.trim());

                                        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                                                SoapEnvelope.VER11);
                                        envelope.dotNet = true;

                                        envelope.setOutputSoapObject(request);

                                        // 	allowAllSSL();

                                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                                .permitAll().build();
                                        StrictMode.setThreadPolicy(policy);

                                        HttpTransportSE androidHttpTranport = new HttpTransportSE(
                                                URl);
                                        try {
                                            try {
                                                androidHttpTranport
                                                        .call(SOAP_ACTION_SAVE_OBJECTIVE,
                                                                envelope);
                                            } catch (XmlPullParserException e) {
                                                // TODO Auto-generated catch
                                                // block
                                                try {
                                                    throw (e);
                                                } catch (Exception e1) {
                                                    e1.printStackTrace();
                                                }
                                                mProgressDialog.dismiss();
                                                defineobjective_running = false;
                                            }
                                            Object response = envelope
                                                    .getResponse();
                                            strResult = response.toString();
                                        } catch (IOException e) {
                                            try {
                                                throw (e);
                                            } catch (Exception e1) {
                                                e1.printStackTrace();
                                            }
                                            mProgressDialog.dismiss();
                                            defineobjective_running = false;
                                        }

                                        if (defineobjective_running) {
                                            if (!strResult.contentEquals("0")) {
                                                // mProgressDialog.dismiss();
                                            } else {
                                                defineobjective_running = false;
                                                mProgressDialog.dismiss();
                                                // syncerror();
                                                BancaBDMTracker.this
                                                        .runOnUiThread(new Runnable() {

                                                            // @Override
                                                            public void run() {
                                                                // TODO
                                                                // Auto-generated
                                                                // method stub
                                                                syncerror();
                                                            }
                                                        });
                                            }
                                        } else {
                                            mProgressDialog.dismiss();
                                            // syncerror();
                                            BancaBDMTracker.this
                                                    .runOnUiThread(new Runnable() {

                                                        // @Override
                                                        public void run() {
                                                            // TODO
                                                            // Auto-generated
                                                            // method stub
                                                            syncerror();
                                                        }
                                                    });
                                        }
                                    }
                                }
                                if (!strnewbusinesspre.equalsIgnoreCase("")) {
                                    if (defineobjective_running) {

                                        paramid = "3";
                                        value = Integer
                                                .parseInt(strnewbusinesspre);

                                        // boolean running = true;

                                        SoapObject request = new SoapObject(
                                                NAMESPACE,
                                                METHOD_NAME_SAVE_OBJECTIVE);

                                        request.addProperty(
                                                "objective_Param_mast_id",
                                                paramid);
                                        request.addProperty("bdmId",
                                                strCIFBDMUserId);
                                        request.addProperty("brCode", strCode);
                                        request.addProperty("brName",
                                                strbranchname);
                                        request.addProperty("parmaValue", value);
                                        request.addProperty("remarks",
                                                strremark);
                                        request.addProperty("strEmailId",
                                                strCIFBDMEmailId);
                                        request.addProperty("strMobileNo",
                                                strCIFBDMMObileNo);
                                        request.addProperty("strAuthKey",
                                                strCIFBDMPassword.trim());

                                        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                                                SoapEnvelope.VER11);
                                        envelope.dotNet = true;

                                        envelope.setOutputSoapObject(request);

                                        // 	allowAllSSL();

                                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                                .permitAll().build();
                                        StrictMode.setThreadPolicy(policy);

                                        HttpTransportSE androidHttpTranport = new HttpTransportSE(
                                                URl);
                                        try {
                                            try {
                                                androidHttpTranport
                                                        .call(SOAP_ACTION_SAVE_OBJECTIVE,
                                                                envelope);
                                            } catch (XmlPullParserException e) {
                                                try {
                                                    throw (e);
                                                } catch (Exception e1) {
                                                    e1.printStackTrace();
                                                }
                                                mProgressDialog.dismiss();
                                                defineobjective_running = false;
                                            }
                                            Object response = envelope
                                                    .getResponse();
                                            strResult = response.toString();
                                        } catch (IOException e) {
                                            try {
                                                throw (e);
                                            } catch (Exception e1) {
                                                e1.printStackTrace();
                                            }
                                            mProgressDialog.dismiss();
                                            defineobjective_running = false;
                                        }

                                        if (defineobjective_running) {
                                            if (!strResult.contentEquals("0")) {
                                                // mProgressDialog.dismiss();
                                            } else {
                                                defineobjective_running = false;
                                                mProgressDialog.dismiss();
                                                // syncerror();
                                                BancaBDMTracker.this
                                                        .runOnUiThread(new Runnable() {

                                                            // @Override
                                                            public void run() {
                                                                // TODO
                                                                // Auto-generated
                                                                // method stub
                                                                syncerror();
                                                            }
                                                        });
                                            }
                                        } else {
                                            mProgressDialog.dismiss();
                                            // syncerror();
                                            BancaBDMTracker.this
                                                    .runOnUiThread(new Runnable() {

                                                        // @Override
                                                        public void run() {
                                                            // TODO
                                                            // Auto-generated
                                                            // method stub
                                                            syncerror();
                                                        }
                                                    });
                                        }
                                    }
                                }
                                if (!strhomeloan.equalsIgnoreCase("")) {
                                    if (defineobjective_running) {

                                        paramid = "4";
                                        value = Integer.parseInt(strhomeloan);

                                        // boolean running = true;

                                        SoapObject request = new SoapObject(
                                                NAMESPACE,
                                                METHOD_NAME_SAVE_OBJECTIVE);

                                        request.addProperty(
                                                "objective_Param_mast_id",
                                                paramid);
                                        request.addProperty("bdmId",
                                                strCIFBDMUserId);
                                        request.addProperty("brCode", strCode);
                                        request.addProperty("brName",
                                                strbranchname);
                                        request.addProperty("parmaValue", value);
                                        request.addProperty("remarks",
                                                strremark);
                                        request.addProperty("strEmailId",
                                                strCIFBDMEmailId);
                                        request.addProperty("strMobileNo",
                                                strCIFBDMMObileNo);
                                        request.addProperty("strAuthKey",
                                                strCIFBDMPassword.trim());

                                        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                                                SoapEnvelope.VER11);
                                        envelope.dotNet = true;

                                        envelope.setOutputSoapObject(request);

                                        // 	allowAllSSL();

                                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                                .permitAll().build();
                                        StrictMode.setThreadPolicy(policy);

                                        HttpTransportSE androidHttpTranport = new HttpTransportSE(
                                                URl);
                                        try {
                                            try {
                                                androidHttpTranport
                                                        .call(SOAP_ACTION_SAVE_OBJECTIVE,
                                                                envelope);
                                            } catch (XmlPullParserException e) {
                                                try {
                                                    throw (e);
                                                } catch (Exception e1) {
                                                    e1.printStackTrace();
                                                }
                                                mProgressDialog.dismiss();
                                                defineobjective_running = false;
                                            }
                                            Object response = envelope
                                                    .getResponse();
                                            strResult = response.toString();
                                        } catch (IOException e) {
                                            try {
                                                throw (e);
                                            } catch (Exception e1) {
                                                e1.printStackTrace();
                                            }
                                            mProgressDialog.dismiss();
                                            defineobjective_running = false;
                                        }

                                        if (defineobjective_running) {
                                            if (!strResult.contentEquals("0")) {
                                                // mProgressDialog.dismiss();
                                            } else {
                                                defineobjective_running = false;
                                                mProgressDialog.dismiss();
                                                // syncerror();
                                                BancaBDMTracker.this
                                                        .runOnUiThread(new Runnable() {

                                                            // @Override
                                                            public void run() {
                                                                // TODO
                                                                // Auto-generated
                                                                // method stub
                                                                syncerror();
                                                            }
                                                        });
                                            }
                                        } else {
                                            mProgressDialog.dismiss();
                                            // syncerror();
                                            BancaBDMTracker.this
                                                    .runOnUiThread(new Runnable() {

                                                        // @Override
                                                        public void run() {
                                                            // TODO
                                                            // Auto-generated
                                                            // method stub
                                                            syncerror();
                                                        }
                                                    });
                                        }
                                    }
                                }

                            }

                            // mProgressDialog.dismiss();

                            if (defineobjective_running) {
                                mProgressDialog.dismiss();
                                BancaBDMTracker.this
                                        .runOnUiThread(new Runnable() {
                                            public void run() {
                                                // TODO Auto-generated method
                                                // stub
                                                tasksyncerror();

                                                clsDefineObjectiveSync obj = new clsDefineObjectiveSync(
                                                        strCode,
                                                        strCIFBDMUserId, "True");
                                                db.AddDefineObjectiveSync(obj);
                                            }
                                        });
                            }
                        }
                    };

                    splashTread.start();

                    /*
                     * if(defineobjective_running == true) {
                     * mProgressDialog.dismiss(); tasksyncerror(); }
                     */

                    /*
                     * clsDefineObjectiveSync obj = new
                     * clsDefineObjectiveSync(strCode, strCIFBDMUserId, "True");
                     * db.AddDefineObjectiveSync(obj);
                     */

                }
            }
        }

    }

    public void btn_getcommittotal(View v) {
        Cursor c1 = db.GetDefineObjectiveAllRecord(strCIFBDMUserId);
        ArrayList<clsDefineObjective> lstbc = new ArrayList<>();

        lstbc.clear();

        if (c1.getCount() > 0) {
            c1.moveToFirst();
            for (int ri = 0; ri < c1.getCount(); ri++) {

                clsDefineObjective obj = new clsDefineObjective("", "", "", "",
                        "", "", "", "");

                obj.setNewBusCash(c1.getString(c1
                        .getColumnIndex("DefineObjectiveNewBusCash")));
                obj.setHomeLoan(c1.getString(c1
                        .getColumnIndex("DefineObjectiveHomeLoan")));
                obj.setNewBusPre(c1.getString(c1
                        .getColumnIndex("DefineObjectiveNewBusPre")));
                obj.setShare(c1.getString(c1
                        .getColumnIndex("DefineObjectiveShare")));
                obj.setRemark(c1.getString(c1
                        .getColumnIndex("DefineObjectiveRemark")));
                lstbc.add(obj);
                c1.moveToNext();
            }
        }

        double dcash = 0;
        double dloan = 0;
        double dpre = 0;
        double dshare = 0;
        //double dremark = 0;

        for (int i = 0; i < lstbc.size(); i++) {
            dcash += Double
                    .parseDouble(lstbc.get(i).getNewBusCash() == null ? "0"
                            : lstbc.get(i).getNewBusCash());
            dloan = Double.parseDouble(lstbc.get(i).getHomeLoan() == null ? "0"
                    : lstbc.get(i).getHomeLoan());
            dpre = Double.parseDouble(lstbc.get(i).getNewBusPre() == null ? "0"
                    : lstbc.get(i).getNewBusPre());
            dshare = Double.parseDouble(lstbc.get(i).getShare() == null ? "0"
                    : lstbc.get(i).getShare());
			/*dremark = Double.parseDouble(lstbc.get(i).getRemark() == null ? "0"
					: lstbc.get(i).getRemark());*/
        }

        if (lstbc.size() > 0) {
            edcommitnewbusinesstot.setText(String.valueOf(dcash));
            edcommithomeloantot.setText(String.valueOf(dloan));
            edcommitnewbusinesspretot.setText(String.valueOf(dpre));
            edcommitsharesingletot.setText(String.valueOf(dshare));
            //edcommitremarktot.setText(String.valueOf(dremark));
        }
    }

    public void btndate(View v) {

        datecheck = 1;

        showDialog(DATE_DIALOG_ID);
    }

    public void btntodate(View v) {
        datecheck = 0;
        showDialog(DATE_DIALOG_ID);
    }

    private void updateDisplay(int year, int month, int day) {
        y = String.valueOf(year);
        m = String.valueOf(month + 1);
        d = String.valueOf(day);
        String da = String.valueOf(day);

        m = new CommonMethods().getFullMonthName(m);

		/*if (m.contentEquals("1")) {
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
		}*/

        String totaldate = da + "-" + m + "-" + y;

        if (datecheck == 1) // for search record
        {

            datecheck = 0; // for search record

            etdate.setText(totaldate);

            // Activity Recorder

            editTextdtRecordActivity.setText(totaldate);

            chk.setChecked(false);

            // for search record

            edfromdate.setText(totaldate);

        } else if (datecheck == 4) {
            edsrholeadtdate.setText(totaldate);
        } else if (datecheck == 5) {
            edsrhofolfdate.setText(totaldate);
        } else if (datecheck == 6) {
            edsrhofoltdate.setText(totaldate);
        } else if (datecheck == 7) {
            totaldate = da + "-" + m.substring(0, 3) + "-" + y;

            strTpDob = da + "-" + m + "-" + y;

            edtbdmdob.setText(totaldate);
        } else {
            edtodate.setText(totaldate);// for search record
        }

    }

    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay(mYear, mMonth, mDay);

        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:

                return new DatePickerDialog(this, R.style.datepickerstyle, mDateSetListener, mYear, mMonth,
                        mDay);

            case TIME_DIALOG_ID:
                // set time picker as current time
                return new TimePickerDialog(this, timePickerListener, hour, minute,
                        false);

            case DIALOG_DOWNLOAD_PROGRESS:

                mProgressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
                String Message = "Loading. Please wait...";
                mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(true);


                mProgressDialog.setButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                taskActivity.cancel(true);
                                taskSubActivity.cancel(true);
                                taskList.cancel(true);
                                taskListRecord.cancel(true);
                                taskListDetail.cancel(true);
                                taskListSeq.cancel(true);
                                taskListBankBranch.cancel(true);
                                taskListParamList.cancel(true);
                                taskListSaveObjective.cancel(true);
                                taskBranchProfile.cancel(true);
                                taskBranchDeposits.cancel(true);
                                taskBranchAdvances.cancel(true);
                                taskSyncBranchProfile.cancel(true);
                                taskRinRaksha.cancel(true);
                                taskBdm_Dashboard.cancel(true);
                                taskBdm_mail_data.cancel(true);
                                taskLead.cancel(true);

                                if (mProgressDialog.isShowing())
                                    mProgressDialog.dismiss();
                            }
                        });

                mProgressDialog.setMax(100);
                mProgressDialog.show();
                return mProgressDialog;

        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        if (id == DATE_DIALOG_ID) {
            ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
        }
    }

    private final TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour,
                              int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;

            if (selectedHour > 12) {

                strampm = "PM";
            }
            if (selectedHour == 12) {
                strampm = "PM";
            }
            if (selectedHour < 12) {
                strampm = "AM";
            }

            if (timecheck == 1) {
                timecheck = 0;

                // set current time into textview
                tvDisplayTime.setText(new StringBuilder().append(pad(hour))
                        .append(":").append(pad(minute)).append(" ")
                        .append(strampm));

                // set current time into textview (Activity Recorder)
                tvTimeRecordActivity.setText(new StringBuilder()
                        .append(pad(hour)).append(":").append(pad(minute))
                        .append(" ").append(strampm));

            } else {
                tvTimeToRecordActivity.setText(new StringBuilder()
                        .append(pad(hour)).append(":").append(pad(minute))
                        .append(" ").append(strampm));
            }

        }
    };

    private String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + c;
    }

    @SuppressWarnings("rawtypes")
    private class ItemsAdapter extends ArrayAdapter {
        //private int selectedPos = -1;
        final List<clsBDMTrackerCalendar> items;

        @SuppressWarnings("unchecked")
        ItemsAdapter(Context context, int textViewResourceId,
                     List<clsBDMTrackerCalendar> items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }


        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {

            /*
             * TextView title; TextView mDescription; TextView datename;
             * TextView lastdetail;
             */
            View view = convertView;
            if (view == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R.layout.clist_with_image, null);
            }

            TextView datename = view.findViewById(R.id.img);
            TextView title = view.findViewById(R.id.title);

            TextView activityname = view
                    .findViewById(R.id.txtactivityname);

            TextView mDescription = view.findViewById(R.id.detail);
            TextView lastdetail = view.findViewById(R.id.lastdetail);

            TextView leadact = view.findViewById(R.id.leadact);

            TextView activitystatus = view
                    .findViewById(R.id.activitystatus);

            TextView subactivity = view
                    .findViewById(R.id.txtsubactivity);
            TextView timeto = view.findViewById(R.id.txttimeto);

            datename.setText(items.get(position).getDateName());
            title.setText("Branch : " + items.get(position).getEventName());
            activityname.setText("Activity : "
                    + items.get(position).getActivity());
            mDescription
                    .setText("Time From : " + items.get(position).getTime());
            lastdetail.setText("Remark : " + items.get(position).getRemark());

            leadact.setText("Lead : " + items.get(position).getLead());

            activitystatus.setText("Status : "
                    + items.get(position).getStatus());

            subactivity.setText("Sub Activity : "
                    + items.get(position).getSubActivity());
            timeto.setText("Time To : " + items.get(position).getTimeTo());

            return view;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

    }

    private class ItemsAdapterEmail extends ArrayAdapter<clsEmail> {
        //private int selectedPos = -1;
        final List<clsEmail> items;

        ItemsAdapterEmail(Context context, int textViewResourceId,
                          List<clsEmail> items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }

//		public void setSelectedPosition(int pos) {
//			selectedPos = pos;
//			// inform the view of this change
//			notifyDataSetChanged();
//		}
//
//		public int getSelectedPosition() {
//			return selectedPos;
//		}

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {

            View view = convertView;
            if (view == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R.layout.spinner_item, null);
            }

            TextView datename = view.findViewById(R.id.txtspinitem);
            datename.setText("Email Id: " + items.get(position).getName());

            return view;
        }

        @Override
        public int getCount() {
            return items.size();
        }

		/*@Override
		public Object getItem(int position) {
			return position;
		}*/

        @Override
        public long getItemId(int position) {
            return position;
        }

    }

    private void validation() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("All Fields Required..");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void validation_lead() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Enter your Lead..");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void ok() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Event Added Successfully..");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

                tbl.getLayoutParams().height = 0;
                tbl.requestLayout();
            }
        });

        dialog.show();

    }

    private void validatedate() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Please Select Date First..!");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void savealert() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Save Successfully...");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void existlert() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Email Id Already Exist...");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void existdefineobjectivelert() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Record Already Exist...");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void savedefineobjectivelert() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Save Successfully...");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void updatedefineobjectivelert() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Update Successfully...");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void errordateselect() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("From Date is Not Greater Than To Date..");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void dateselecterror() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Date should be smaller or equal than Today's date");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void AlertRecordExist() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Record Already Exist..");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    @Override
    protected void onStop() {
        // When our activity is stopped ensure we also stop the connection to
        // the service
        // this stops us leaking our activity into the system *bad*
        if (scheduleClient != null)
            scheduleClient.doUnbindService();
        super.onStop();
    }


    private void displayTodayEvents() {


        Date dt = new Date(Calendar.getInstance().getTimeInMillis());
        // String selectedGridDate = dateFormat.format(dt); //after change
        // requirement bdm tracker search
        String selectedGridDate = df.format(dt);


        System.out.println("My date***" + selectedGridDate);

        Cursor c1 = db.geteventnameBDMT(selectedGridDate, strCIFBDMUserId);

        String strdate = "";
        String strevent = "";
        String strtmonth = "";
        String strtyear = "";
        String strtime = "";
        String strremark = "";
        String stractivity = "";

        String strSubActivity = "";
        String strTimeTo = "";
        String strUserID = "";
        String strStatus = "";
        String strSync = "";

        String strCreatedDate = "";
        String strModifiedDate = "";

        String strServerMasterId = "";

        String strLead = "";

        todaysmain.clear();

        if (c1.getCount() > 0) {
            c1.moveToFirst();
            for (int ii = 0; ii < c1.getCount(); ii++) {
                clsBDMTrackerCalendar single = new clsBDMTrackerCalendar(
                        strdate, strevent, strtmonth, strtyear, strtime,
                        strremark, stractivity, strSubActivity, strTimeTo,
                        strUserID, strStatus, strSync, strCreatedDate,
                        strModifiedDate, strServerMasterId, strLead);
                // single.setDateName(c1.getString(c1.getColumnIndex("DateName")));
                String strfromdate = c1.getString(c1
                        .getColumnIndex("DateNameBDMT"));

                Date dt1 = null;
                try {
                    dt1 = df.parse(strfromdate);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                strfromdate = dateFormat.format(dt1);

                single.setDateName(strfromdate);
                single.setEventName(c1.getString(c1
                        .getColumnIndex("EventNameBDMT")));
                single.setTime(c1.getString(c1.getColumnIndex("TimeBDMT")));
                single.setRemark(c1.getString(c1.getColumnIndex("Remark")));
                single.setActivity(c1.getString(c1.getColumnIndex("Activity")));
                single.setSubActivity(c1.getString(c1
                        .getColumnIndex("SubActivity")));
                single.setTimeTo(c1.getString(c1.getColumnIndex("TimeTo")));
                single.setStatus(c1.getString(c1
                        .getColumnIndex("ActivityStatus")));
                single.setLead(c1.getString(c1.getColumnIndex("ActivityLead")));
                todaysmain.add(single);
                c1.moveToNext();
            }
        }

        System.out.println("todaysmain size ***" + todaysmain.size());

        if (todaysmain.size() > 0) {
            ItemsAdapter adapter = new ItemsAdapter(context, 0, todaysmain);
            adapter.setNotifyOnChange(true);
            todaylv.setAdapter(adapter);
            setTodaysList(todaylv);
        } else {
            ArrayList<clsBDMTrackerCalendar> todaysmain = new ArrayList<>();

            ItemsAdapter adapter = new ItemsAdapter(context, 0, todaysmain);
            adapter.setNotifyOnChange(true);
            todaylv.setAdapter(adapter);
            setTodaysList(todaylv);
        }

    }

    private void displayTodayEventsra() {
        Cursor c1 = db.GetAllActivityRecord(strCIFBDMUserId);

        String strdate = "";
        String strevent = "";
        String strtmonth = "";
        String strtyear = "";
        String strtime = "";
        String strremark = "";
        String stractivity = "";
        String strSubActivity = "";
        String strTimeTo = "";
        String strUserID = "";
        String strSync = "";
        String lead = "";

        String strCreatedDate = "";
        String strModifiedDate = "";
        String status = "";

        todaysmainra.clear();

        if (c1.getCount() > 0) {
            c1.moveToFirst();
            for (int ii = 0; ii < c1.getCount(); ii++) {
                clsCalendarActivityRecorder single = new clsCalendarActivityRecorder(
                        strdate, strevent, strtmonth, strtyear, strtime,
                        strremark, stractivity, strSubActivity, strTimeTo,
                        strUserID, strSync, strCreatedDate, strModifiedDate,
                        status, lead);
                // single.setDateName(c1.getString(c1.getColumnIndex("DateNameAR")));
                String strfromdate = c1.getString(c1
                        .getColumnIndex("DateNameAR"));

                Date dt1 = null;
                try {
                    dt1 = df.parse(strfromdate);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                strfromdate = dateFormat.format(dt1);

                single.setDateName(strfromdate);
                single.setEventName(c1.getString(c1
                        .getColumnIndex("EventNameAR")));
                single.setTime(c1.getString(c1.getColumnIndex("TimeAR")));
                single.setRemark(c1.getString(c1.getColumnIndex("RemarkAR")));
                single.setActivity(c1.getString(c1.getColumnIndex("ActivityAR")));
                single.setSubActivity(c1.getString(c1
                        .getColumnIndex("SubActivityAR")));
                single.setTimeTo(c1.getString(c1.getColumnIndex("TimeToAR")));
                single.setStatus(c1.getString(c1
                        .getColumnIndex("ActivityStatusAR")));
                single.setLead(c1.getString(c1.getColumnIndex("ActivityLeadAR")));
                todaysmainra.add(single);
                c1.moveToNext();
            }
        }
        if (todaysmainra.size() > 0) {
            ItemsAdapterra adapter = new ItemsAdapterra(context, 0,
                    todaysmainra);
            adapter.setNotifyOnChange(true);
            todaylvra.setAdapter(adapter);
            setTodaysList(todaylvra);
        } else {
            ArrayList<clsCalendarActivityRecorder> todaysmainra = new ArrayList<>();

            ItemsAdapterra adapter = new ItemsAdapterra(context, 0,
                    todaysmainra);
            adapter.setNotifyOnChange(true);
            todaylvra.setAdapter(adapter);
            setTodaysList(todaylvra);
        }
    }

    private void refreshemaillist() {
        strViewGroupEmail = spviewgroup.getSelectedItem().toString();

        if (!strViewGroupEmail.contentEquals("Select")) {
            Cursor c = db.getGroupListEmail(strViewGroupEmail, strCIFBDMUserId);

            lstemaillist.clear();

            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int ii = 0; ii < c.getCount(); ii++) {
                    clsEmail single = new clsEmail("", "", "", "", "", "", "");
                    single.setName(c.getString(c.getColumnIndex("EmailName")));
                    lstemaillist.add(single);
                    c.moveToNext();
                }
            }

            ItemsAdapterEmail adapter = new ItemsAdapterEmail(context, 0,
                    lstemaillist);
            adapter.setNotifyOnChange(true);
            lstEmail.setAdapter(adapter);
            setShowallList(lstEmail);

        }
    }

    private void getallrecord() {
        Cursor c1 = db.getAllEventsBDMT(strCIFBDMUserId);

        String strdate = "";
        String strevent = "";
        String strtmonth = "";
        String strtyear = "";
        String strtime = "";
        String strremark = "";
        String stractivity = "";

        String strSubActivity = "";
        String strTimeTo = "";
        String strUserID = "";
        String strStatus = "";
        String strSync = "";

        String strCreatedDate = "";
        String strModifiedDate = "";

        String strServerMasterId = "";

        String strLead = "";

        lstmain.clear();

        if (c1.getCount() > 0) {
            c1.moveToFirst();
            for (int ii = 0; ii < c1.getCount(); ii++) {
                clsBDMTrackerCalendar single = new clsBDMTrackerCalendar(
                        strdate, strevent, strtmonth, strtyear, strtime,
                        strremark, stractivity, strSubActivity, strTimeTo,
                        strUserID, strStatus, strSync, strCreatedDate,
                        strModifiedDate, strServerMasterId, strLead);
                // single.setDateName(c1.getString(c1.getColumnIndex("DateName")));
                String strfromdate = c1.getString(c1
                        .getColumnIndex("DateNameBDMT"));

                Date dt1 = null;
                try {
                    dt1 = df.parse(strfromdate);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                strfromdate = dateFormat.format(dt1);

                single.setDateName(strfromdate);
                single.setEventName(c1.getString(c1
                        .getColumnIndex("EventNameBDMT")));
                single.setTime(c1.getString(c1.getColumnIndex("TimeBDMT")));
                single.setRemark(c1.getString(c1.getColumnIndex("Remark")));
                single.setActivity(c1.getString(c1.getColumnIndex("Activity")));
                single.setSubActivity(c1.getString(c1
                        .getColumnIndex("SubActivity")));
                single.setTimeTo(c1.getString(c1.getColumnIndex("TimeTo")));
                single.setStatus(c1.getString(c1
                        .getColumnIndex("ActivityStatus")));
                single.setLead(c1.getString(c1.getColumnIndex("ActivityLead")));
                lstmain.add(single);
                c1.moveToNext();
            }
        }

        if (lstmain.size() > 0) {
            ItemsAdapter adapter = new ItemsAdapter(context, 0, lstmain);
            adapter.setNotifyOnChange(true);
            lv.setAdapter(adapter);
            setShowallList(lv);
        } else {
            ArrayList<clsBDMTrackerCalendar> lstmain = new ArrayList<>();

            ItemsAdapter adapter = new ItemsAdapter(context, 0, lstmain);
            adapter.setNotifyOnChange(true);
            lv.setAdapter(adapter);
            setShowallList(lv);
        }
    }

    private class ItemsAdapterra extends ArrayAdapter<clsCalendarActivityRecorder> {
        //private int selectedPos = -1;
        final List<clsCalendarActivityRecorder> items;

        ItemsAdapterra(Context context, int textViewResourceId,
                       List<clsCalendarActivityRecorder> items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }


        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {


            View view = convertView;
            if (view == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R.layout.recordlist_with_image, null);
            }

            TextView datename = view.findViewById(R.id.img);
            TextView title = view.findViewById(R.id.title);

            TextView activityname = view
                    .findViewById(R.id.txtactivityname);

            TextView mDescription = view.findViewById(R.id.detail);
            TextView lastdetail = view.findViewById(R.id.lastdetail);

            TextView subactivity = view
                    .findViewById(R.id.txtsubactivity);
            TextView timeto = view.findViewById(R.id.txttimeto);

            TextView status = view
                    .findViewById(R.id.activityrecordstatus);

            TextView Lead = view
                    .findViewById(R.id.activityrecordlead);

            datename.setText(items.get(position).getDateName());
            title.setText("Branch : " + items.get(position).getEventName());
            activityname.setText("Activity : "
                    + items.get(position).getActivity());
            /*
             * mDescription .setText("Time From : " +
             * items.get(position).getTime());
             */
            mDescription.setText("Activity Time : "
                    + items.get(position).getTime());
            lastdetail.setText("Remark : " + items.get(position).getRemark());

            subactivity.setText("Sub Activity : "
                    + items.get(position).getSubActivity());
            timeto.setText("Time To : " + items.get(position).getTimeTo());

            Lead.setText("Lead : " + items.get(position).getLead());

            status.setText("Status : " + items.get(position).getStatus());

            return view;
        }

        @Override
        public int getCount() {
            return items.size();
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

    }

    public void btn_planner(View v) {
        // search record
        tblsearchactvity.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        tblsearchactvity.requestLayout();
        // end search record

        lnplanner.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        lnplanner.requestLayout();

        /*
         * tbl.getLayoutParams().height = 300; tbl.requestLayout();
         */

        todaysActiviy.setVisibility(View.VISIBLE);
        todaylv.setVisibility(View.VISIBLE);
        header.setVisibility(View.VISIBLE);
        gridview.setVisibility(View.VISIBLE);
        lnmonthsel.setVisibility(View.VISIBLE);
        lv.setVisibility(View.VISIBLE);

        setObjectiveLayout.getLayoutParams().height = 0;
        setObjectiveLayout.requestLayout();

        lnrecordactivity.setVisibility(View.GONE);

        lnbranchprofile.getLayoutParams().height = 0;
        lnbranchprofile.requestLayout();

        lnmailer.getLayoutParams().height = 0;
        lnmailer.requestLayout();

        btnplanner.setBackgroundResource(R.drawable.exp_selected);
        btnactivityrecorder.setBackgroundResource(R.drawable.exp_unselected);
        btndefineobj.setBackgroundResource(R.drawable.exp_unselected);
        btnmailer.setBackgroundResource(R.drawable.exp_unselected);
        btnbranchpro.setBackgroundResource(R.drawable.exp_unselected);

        btnaddactivity.setBackgroundResource(R.drawable.exp_unselected_sub);
        btnshowactivity.setBackgroundResource(R.drawable.exp_unselected_sub);

    }


    // activity recorder


    public void btn_refreshrecord(View v) {
        btnsearch.setBackgroundResource(R.drawable.exp_unselected);
        btnrefresh.setBackgroundResource(R.drawable.exp_selected);

        getallrecord();
    }

    @Override
    protected void onResume() {
        super.onResume();

        displayTodayEvents();
        // displayTodayEventsra();
        getallrecord();
        refreshemaillist();

        displayTodayEventsra();

        GetAllLead();

        //mCountDown.start();
    }

    @Override
    public void onPause() {
        super.onPause();

        //mCountDown.cancel();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();

        // user interact cancel the timer and restart to countdown to next
        // interaction
        //mCountDown.cancel();
        //mCountDown.start();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Services");

        int id = v.getId();
        if (id == R.id.list) {
            menu.add(0, v.getId(), 0, "Sync Activity");
        } else if (id == R.id.todayslistra) {
            menu.add(0, v.getId(), 0, "Sync Record Activity");
            menu.add(0, v.getId(), 1, "Edit");
            menu.add(0, v.getId(), 2, "Delete");
        } else {
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

        long id = info.id;
        strActivityRowId = String.valueOf(id + 1);

        if (item.toString().contentEquals("Sync Activity")) {
            String strSync = db.getSync(strActivityRowId);
            if (strSync.contentEquals("False")) {

                String strActivity = lstmain.get(info.position).getActivity();

                // String bdmid = lstmain.get(info.position).getUserId();
                String categoryid = db.getActivityId(strActivity);
                String branchname = lstmain.get(info.position).getEventName();
                String time = lstmain.get(info.position).getTime();
                String remark = lstmain.get(info.position).getRemark();
                String status = lstmain.get(info.position).getStatus();

                String lead = lstmain.get(info.position).getLead();

                String timeto = lstmain.get(info.position).getTimeTo();
                if (!timeto.equalsIgnoreCase("")) {
                    time = time + " To " + timeto;
                }

                String subactivity = lstmain.get(info.position)
                        .getSubActivity();
                if (subactivity.equalsIgnoreCase("")) {

                    lstSyncTaskList = new ArrayList<>();
                    lstSyncTaskList.add(strCIFBDMUserId);
                    lstSyncTaskList.add(categoryid);
                    lstSyncTaskList.add(branchname);
                    lstSyncTaskList.add(time);
                    lstSyncTaskList.add(remark + "|" + lead);
                    lstSyncTaskList.add(status);
                    lstSyncTaskList.add("");
                } else {
                    lstSyncTaskListSeq = new ArrayList<>();
                    lstSyncTaskListSeq.add(strCIFBDMUserId);
                    lstSyncTaskListSeq.add(categoryid);
                    lstSyncTaskListSeq.add(branchname);
                    lstSyncTaskListSeq.add(time);
                    lstSyncTaskListSeq.add(remark + "|" + lead);
                    lstSyncTaskListSeq.add(status);

                    String subcatmasterid = db.GetSubCategoryMasterId(
                            categoryid, subactivity);

                    lstSyncTaskListSeq.add(subcatmasterid);

                    lstSyncTaskList = new ArrayList<>();
                    lstSyncTaskList.add(strCIFBDMUserId);
                    lstSyncTaskList.add(strActivityRowId);
                    lstSyncTaskList.add(categoryid);
                    lstSyncTaskList.add(subcatmasterid);
                    lstSyncTaskList.add(branchname);
                    lstSyncTaskList.add(time);
                    lstSyncTaskList.add(remark + "|" + lead);
                    lstSyncTaskList.add(subcatmasterid);
                }

                if (strActivity != "" || strActivity != null) {

                    if (mCommonMethods.isNetworkConnected(context)) {
                        String subact = lstmain.get(info.position)
                                .getSubActivity();
                        if (subact.equalsIgnoreCase("")) {
                            taskList = new PushTaskList();

                            startPushTaskList();
                        } else {
                            /*
                             * taskListDetail = new PushTaskListDetail();
                             * startPushTaskListDetail();
                             */

                            taskListSeq = new PushTaskListSeq();

                            startPushTaskListSeq();
                        }
                    } else {
                        mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
                    }

                }
            } else {
                activitysynalert();
            }
        } else {


            if (item.getTitle().toString().contentEquals("Sync Record Activity")) {

                //String strSync = db.getSyncAR(strActivityRowId);


                String actname = todaysmainra.get(info.position).getActivity();
                String subactname = todaysmainra.get(info.position).getSubActivity();
                String branchname = todaysmainra.get(info.position).getEventName();
                String date = todaysmainra.get(info.position).getDateName();
                String time = todaysmainra.get(info.position).getTime();
                String remark = todaysmainra.get(info.position).getRemark();
                String lead = todaysmainra.get(info.position).getLead();

                Date dt12 = null;
                try {
                    dt12 = dateFormat.parse(date);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                date = df.format(dt12);

                Cursor c2 = db.getSelectedRowRecord_ar(actname, subactname, branchname, date, time, remark, lead);

                ArrayList<String> lsteventa = new ArrayList<>();

                lsteventa.clear();

                if (c2.getCount() > 0) {
                    c2.moveToFirst();
                    for (int ri = 0; ri < c2.getCount(); ri++) {
                        lsteventa.add(c2.getString(c2.getColumnIndex("ActivitySyncAR")));
                        c2.moveToNext();
                    }

                    String strSync = lsteventa.get(0);

                    if (strSync.contentEquals("False")) {

                        String strRecordActivity = todaysmainra.get(info.position)
                                .getActivity();

                        // String bdmid = lstmain.get(info.position).getUserId();
                        String categoryid = db.getActivityId(strRecordActivity);
                        String subcategoryid = db.GetSubCategoryMasterId(categoryid, subactname);
							/*String branchname = todaysmainra.get(info.position)
									.getEventName();
							String time = todaysmainra.get(info.position).getTime();
							String remark = todaysmainra.get(info.position).getRemark();*/
                        String status = todaysmainra.get(info.position).getStatus();

                        //String timeto = todaysmainra.get(info.position).getTimeTo();
                        // time = time + " To " + timeto;
                        //time = time;

                        //String lead = todaysmainra.get(info.position).getLead();

                        lstSyncTaskList.clear();

                        lstSyncTaskList = new ArrayList<>();
                        lstSyncTaskList.add(strCIFBDMUserId);
                        lstSyncTaskList.add(categoryid);
                        lstSyncTaskList.add(branchname);
                        lstSyncTaskList.add(time);
                        lstSyncTaskList.add(remark + "|" + lead);
                        lstSyncTaskList.add(status);
                        lstSyncTaskList.add(subcategoryid);


                        lstActRecord.clear();
                        lstActRecord = new ArrayList<>();

                        lstActRecord.add(actname);
                        lstActRecord.add(subactname);
                        lstActRecord.add(branchname);
                        lstActRecord.add(date);
                        lstActRecord.add(time);
                        lstActRecord.add(remark);
                        lstActRecord.add(lead);
                        if (strRecordActivity != "" || strRecordActivity != null) {
                            if (mCommonMethods.isNetworkConnected(context)) {

                                taskListRecord = new PushTaskListRecord();
                                startPushTaskListRecord();

                            } else {
                                mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
                            }
                        }
                    } else {
                        activitysynalert();
                    }

                }


            } else if (item.getTitle().toString().contentEquals("Edit")) {


                String actname = todaysmainra.get(info.position).getActivity();
                String subactname = todaysmainra.get(info.position).getSubActivity();
                String branchname = todaysmainra.get(info.position).getEventName();
                String date = todaysmainra.get(info.position).getDateName();
                String time = todaysmainra.get(info.position).getTime();
                String remark = todaysmainra.get(info.position).getRemark();
                String lead = todaysmainra.get(info.position).getLead();


                Date dt12 = null;
                try {
                    dt12 = dateFormat.parse(date);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                date = df.format(dt12);


                Cursor c2 = db.getSelectedRowRecord_ar(actname, subactname, branchname, date, time, remark, lead);

                ArrayList<String> lsteventa = new ArrayList<>();

                lsteventa.clear();

                if (c2.getCount() > 0) {
                    c2.moveToFirst();
                    for (int ri = 0; ri < c2.getCount(); ri++) {
                        lsteventa.add(c2.getString(c2.getColumnIndex("ActivityCreatedDateAR")));
                        lsteventa.add(c2.getString(c2.getColumnIndex("ActivitySyncAR")));
                        c2.moveToNext();
                    }

                    String createddate = lsteventa.get(0);
                    String syncflag = lsteventa.get(1);

                    // get current date and formate that into yyyy-MM-dd
                    Calendar cal = Calendar.getInstance();
                    int mYear = cal.get(Calendar.YEAR);
                    int mMonth = cal.get(Calendar.MONTH);
                    int mDay = cal.get(Calendar.DAY_OF_MONTH);

                    String y = String.valueOf(mYear);
                    String m = String.valueOf(mMonth + 1);
                    String da = String.valueOf(mDay);
                    if (m.contentEquals("1")) {
                        m = "01";
                    } else if (m.contentEquals("2")) {
                        m = "02";
                    } else if (m.contentEquals("3")) {
                        m = "03";
                    } else if (m.contentEquals("4")) {
                        m = "04";
                    } else if (m.contentEquals("5")) {
                        m = "05";
                    } else if (m.contentEquals("6")) {
                        m = "06";
                    } else if (m.contentEquals("7")) {
                        m = "07";
                    } else if (m.contentEquals("8")) {
                        m = "08";
                    } else if (m.contentEquals("9")) {
                        m = "09";
                    }

                    if (da.contentEquals("1")) {
                        da = "01";
                    } else if (da.contentEquals("2")) {
                        da = "02";
                    } else if (da.contentEquals("3")) {
                        da = "03";
                    } else if (da.contentEquals("4")) {
                        da = "04";
                    } else if (da.contentEquals("5")) {
                        da = "05";
                    } else if (da.contentEquals("6")) {
                        da = "06";
                    } else if (da.contentEquals("7")) {
                        da = "07";
                    } else if (da.contentEquals("8")) {
                        da = "08";
                    } else if (da.contentEquals("9")) {
                        da = "09";
                    }

                    String currentdate = y + "-" + m + "-" + da;

                    final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);


                    Date d1 = null;
                    try {
                        d1 = df.parse(createddate);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    Date d2 = null;
                    try {
                        d2 = df.parse(currentdate);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if (d1.equals(d2)) {

                        if (syncflag.contentEquals("False")) {

                            Cursor c1 = db.getSelectedRowRecord_ar(actname, subactname, branchname, date, time, remark, lead);

                            ArrayList<String> lstevent = new ArrayList<>();

                            lstevent.clear();

                            if (c1.getCount() > 0) {
                                c1.moveToFirst();
                                for (int ri = 0;
                                     ri < c1.getCount(); ri++) {
                                    lstevent.add(c1.getString(c1.getColumnIndex("DateNameAR")));
                                    lstevent.add(c1.getString(c1.getColumnIndex("EventNameAR")));
                                    lstevent.add(c1.getString(c1.getColumnIndex("TimeAR")));
                                    lstevent.add(c1.getString(c1.getColumnIndex("RemarkAR")));
                                    lstevent.add(c1.getString(c1.getColumnIndex("ActivityAR")));
                                    lstevent.add(c1.getString(c1.getColumnIndex("SubActivityAR")));
                                    lstevent.add(c1.getString(c1.getColumnIndex("TimeToAR")));
                                    lstevent.add(c1.getString(c1.getColumnIndex("ActivityLeadAR")));
                                    lstevent.add(c1.getString(c1.getColumnIndex("DateIDAR")));
                                    c1.moveToNext();
                                }
                            }

                            Intent i = new Intent(context, ViewRecordActivityAR.class);

                            Date dt1 = null;
                            try {
                                dt1 = df.parse(lstevent.get(0));
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            String effdate = dateFormat.format(dt1);

                            i.putExtra("Date", effdate);
                            i.putExtra("Branch", lstevent.get(1));
                            i.putExtra("Activity", lstevent.get(4));
                            i.putExtra("Remark", lstevent.get(3));
                            i.putExtra("Time", lstevent.get(2));

                            i.putExtra("SubActivity", lstevent.get(5));
                            i.putExtra("TimeTo", lstevent.get(6));
                            i.putExtra("ActivityLead", lstevent.get(7));

                            //i.putExtra("RowId", String.valueOf(rowid));
                            i.putExtra("RowId", lstevent.get(8));

                            startActivity(i);

                        } else {
                            activityeditalert();
                        }

                    }
                }

            } else if (item.getTitle().toString().contentEquals("Delete")) {

                //for current date validation
                //String ga = todaylvra.getItemAtPosition(info.position).toString();

                String actname = todaysmainra.get(info.position).getActivity();
                String subactname = todaysmainra.get(info.position).getSubActivity();
                String branchname = todaysmainra.get(info.position).getEventName();
                String date = todaysmainra.get(info.position).getDateName();
                String time = todaysmainra.get(info.position).getTime();
                String remark = todaysmainra.get(info.position).getRemark();
                String lead = todaysmainra.get(info.position).getLead();


                Date dt12 = null;
                try {
                    dt12 = dateFormat.parse(date);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                date = df.format(dt12);

				 /* int rowida = Integer.parseInt(ga) + 1;

				  Cursor c2 = db.getSelectedRowRecord_ar(rowida);*/

                Cursor c2 = db.getSelectedRowRecord_ar(actname, subactname, branchname, date, time, remark, lead);

                ArrayList<String> lsteventa = new ArrayList<>();

                lsteventa.clear();

                if (c2.getCount() > 0) {
                    c2.moveToFirst();
                    for (int ri = 0; ri < c2.getCount(); ri++) {
                        lsteventa.add(c2.getString(c2.getColumnIndex("ActivityCreatedDateAR")));
                        lsteventa.add(c2.getString(c2.getColumnIndex("ActivitySyncAR")));
                        c2.moveToNext();
                    }

                    String createddate = lsteventa.get(0);
                    String syncflag = lsteventa.get(1);

                    // get current date and formate that into yyyy-MM-dd
                    Calendar cal = Calendar.getInstance();
                    int mYear = cal.get(Calendar.YEAR);
                    int mMonth = cal.get(Calendar.MONTH);
                    int mDay = cal.get(Calendar.DAY_OF_MONTH);

                    String y = String.valueOf(mYear);
                    String m = String.valueOf(mMonth + 1);
                    String da = String.valueOf(mDay);
                    if (m.contentEquals("1")) {
                        m = "01";
                    } else if (m.contentEquals("2")) {
                        m = "02";
                    } else if (m.contentEquals("3")) {
                        m = "03";
                    } else if (m.contentEquals("4")) {
                        m = "04";
                    } else if (m.contentEquals("5")) {
                        m = "05";
                    } else if (m.contentEquals("6")) {
                        m = "06";
                    } else if (m.contentEquals("7")) {
                        m = "07";
                    } else if (m.contentEquals("8")) {
                        m = "08";
                    } else if (m.contentEquals("9")) {
                        m = "09";
                    }

                    if (da.contentEquals("1")) {
                        da = "01";
                    } else if (da.contentEquals("2")) {
                        da = "02";
                    } else if (da.contentEquals("3")) {
                        da = "03";
                    } else if (da.contentEquals("4")) {
                        da = "04";
                    } else if (da.contentEquals("5")) {
                        da = "05";
                    } else if (da.contentEquals("6")) {
                        da = "06";
                    } else if (da.contentEquals("7")) {
                        da = "07";
                    } else if (da.contentEquals("8")) {
                        da = "08";
                    } else if (da.contentEquals("9")) {
                        da = "09";
                    }

                    String currentdate = y + "-" + m + "-" + da;

                    final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);


                    Date d1 = null;
                    try {
                        d1 = df.parse(createddate);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    Date d2 = null;
                    try {
                        d2 = df.parse(currentdate);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if (d1.equals(d2)) {

                        if (syncflag.contentEquals("False")) {

                            //db.DeleteRecordActivity(strrowid);
                            db.DeleteRecordActivity(actname, subactname, branchname, date, time, remark, lead);

                            deletealert();

                            displayTodayEventsra();

                        } else {
                            activitydeletealert();
                        }

                    }
                }
            }


        }


        return true;

    }

    private void startDownloadActivity() {

        taskActivity.execute("Activity");
    }

    private void startDownloadSubActivity() {

        taskSubActivity.execute("SubActivity");
    }

    private void startPushTaskList() {

        taskList.execute("TaskList");
    }

    private void startPushTaskListRecord() {

        taskListRecord.execute("TaskListRecord");
    }

    private void startPushTaskListDetail() {

        taskListDetail.execute("TaskListDetail");
    }

    private void startPushTaskListSeq() {

        taskListSeq.execute("TaskListSeq");
    }

    private void startDownloadBankBranch() {
        taskListBankBranch.execute("TaskListBankBranch");
    }

    private void startDownloadParamList() {
        taskListParamList.execute("TaskListParamList");
    }

    private void startDownloadSaveObjective() {
        taskListSaveObjective.execute("TaskListSaveObjective");
    }

    private void startdownlaodbranchprofile() {
        // String url =
        // "http://farm1..flickr.com/114/298125983_0e4bf66782_b.jpg";
        // new DownloadFileAsyncMaturity().execute("demo");
        taskBranchProfile.execute("demo");
    }

    private void startdownloadbranchdeposits() {
        // String url =
        // "http://farm1..flickr.com/114/298125983_0e4bf66782_b.jpg";
        // new DownloadFileAsyncMaturity().execute("demo");
        taskBranchDeposits.execute("demo");
    }

    private void startdownloadbranchadvances() {
        // String url =
        // "http://farm1..flickr.com/114/298125983_0e4bf66782_b.jpg";
        // new DownloadFileAsyncMaturity().execute("demo");
        taskBranchAdvances.execute("demo");
    }

    private void uploadsyncbranchprofile() {
        // String url =
        // "http://farm1..flickr.com/114/298125983_0e4bf66782_b.jpg";
        // new DownloadFileAsyncMaturity().execute("demo");
        taskSyncBranchProfile.execute("demo");
    }

    private void startdownloadRinraksha() {
        taskRinRaksha.execute("demo");
    }

    private void startdownloadBdm_Dashboard() {
        taskBdm_Dashboard.execute("demo");
    }

    private void startdownloadBdm_mail_data() {
        taskBdm_mail_data.execute("demo");
    }

//	private void startdownloadLead() {
//		taskLead.execute("demo");
//	}

    class DownloadActivity extends AsyncTask<String, String, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                String METHOD_NAME_CATEGORY = "getCategoriesList";
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_CATEGORY);
                request.addProperty("strBDMID", strCIFBDMUserId);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());

                mCommonMethods.TLSv12Enable();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    String SOAP_ACTION_CATEGORY = "http://tempuri.org/getCategoriesList";
                    androidHttpTranport.call(SOAP_ACTION_CATEGORY, envelope);
                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("anyType{}")) {

                        SoapPrimitive sa = (SoapPrimitive) envelope
                                .getResponse();

                        ParseXML prsObj = new ParseXML();

                        String inputpolicylist = sa.toString();
                        inputpolicylist = prsObj.parseXmlTag(inputpolicylist,
                                "BDMTracker");

                        inputpolicylist = new ParseXML().parseXmlTag(
                                inputpolicylist, "ScreenData");
                        strActivityCategoryErrorCode = inputpolicylist;

                        if (strActivityCategoryErrorCode == null) {
                            inputpolicylist = sa.toString();
                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "BDMTracker");

                            List<String> Node = prsObj.parseParentNode(
                                    inputpolicylist, "Table");
                            List<XMLHolderCategory> nodeData = prsObj
                                    .parseNodeElementCategory(Node);

                            final List<XMLHolderCategory> lst;
                            lst = new ArrayList<>();
                            lst.clear();

                            lst.addAll(nodeData);

//							int count = lst.size();
                            for (int i = 0; i < lst.size(); i++) {
                                String strId = lst.get(i).getId();
                                String strName = lst.get(i).getName();

                                clsActivityCategory obj = new clsActivityCategory(
                                        strName, strName, strId, "0",
                                        "2013-05-22", "0", "0");
                                db.AddActivityCategory(obj);
                            }
                        } else {

                        }
                    } else {
                        running = false;
                    }

                } catch (XmlPullParserException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
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
        protected void onProgressUpdate(String... progress) {
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing())
                dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            if (running) {

                if (strActivityCategoryErrorCode == null) {

                    Cursor c1 = db.GetAllActivityName();

                    ArrayList<String> lstevent = new ArrayList<>();

                    lstevent.clear();

                    if (c1.getCount() > 0) {
                        c1.moveToFirst();
                        for (int ri = 0; ri < c1.getCount(); ri++) {
                            lstevent.add(c1.getString(c1
                                    .getColumnIndex("ActivityCategoryName")));
                            c1.moveToNext();
                        }
                    }

                    ArrayAdapter<String> activityAdapter = new ArrayAdapter<>(
                            context, android.R.layout.simple_list_item_1,
                            android.R.id.text1, lstevent);
                    selActivity.setAdapter(activityAdapter);
                    activityAdapter.notifyDataSetChanged();

                    ArrayList<String> lsteventAct = new ArrayList<>();
                    lsteventAct.clear();

                    if (c1.getCount() > 0) {
                        c1.moveToFirst();
                        for (int ri = 0; ri < c1.getCount(); ri++) {
                            lsteventAct.add(c1.getString(c1
                                    .getColumnIndex("ActivityCategoryName")));
                            c1.moveToNext();
                        }
                    }

                    ArrayAdapter<String> activityAdapterActivityRecorder = new ArrayAdapter<>(
                            context, android.R.layout.simple_list_item_1,
                            android.R.id.text1, lsteventAct);
                    selActivityRecordActivity
                            .setAdapter(activityAdapterActivityRecorder);
                    activityAdapterActivityRecorder.notifyDataSetChanged();

                    /*
                     * strActivity =
                     * selActivityRecordActivity.getSelectedItem().toString();
                     * strId = db.getActivityId(strActivity);
                     *
                     * taskSubActivity = new DownloadSubActivity();
                     * startDownloadSubActivity();
                     */
                } else {

                }
            } else {
                syncerror();
            }
        }
    }

    class DownloadSubActivity extends AsyncTask<String, String, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                String METHOD_NAME_SUBCATEGORY = "getSubCategoriesList";
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_SUBCATEGORY);

                request.addProperty("strBDMID", strCIFBDMUserId);
                request.addProperty("subCatId", strId);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());

                mCommonMethods.TLSv12Enable();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    String SOAP_ACTION_SUBCATEGORY = "http://tempuri.org/getSubCategoriesList";
                    androidHttpTranport.call(SOAP_ACTION_SUBCATEGORY, envelope);
                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("anyType{}")) {

                        SoapPrimitive sa = (SoapPrimitive) envelope
                                .getResponse();

                        ParseXML prsObj = new ParseXML();

                        String inputpolicylist = sa.toString();
                        inputpolicylist = prsObj.parseXmlTag(inputpolicylist,
                                "BDMTracker");

                        inputpolicylist = new ParseXML().parseXmlTag(
                                inputpolicylist, "ScreenData");
                        strSubActivityErrorCode = inputpolicylist;

                        if (strSubActivityErrorCode == null) {
                            inputpolicylist = sa.toString();
                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "BDMTracker");

                            List<String> Node = prsObj.parseParentNode(
                                    inputpolicylist, "Table");
                            List<XMLHolderSubCategory> nodeData = prsObj
                                    .parseNodeElementSubCategory(Node);

                            final List<XMLHolderSubCategory> lst;
                            lst = new ArrayList<>();
                            lst.clear();

                            lst.addAll(nodeData);

                            //int count = lst.size();

                            for (int i = 0; i < lst.size(); i++) {
                                String strMasterId = lst.get(i).getId();
                                String strName = lst.get(i).getName();

                                clsActivitySubCategory obj = new clsActivitySubCategory(
                                        strName, strName, strId, "0",
                                        "2013-05-22", "0", "0", strMasterId);
                                db.AddActivitySubCategory(obj);
                            }
                        } else {

                        }
                    } else {
                        running = false;
                    }

                } catch (XmlPullParserException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
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
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing())
                dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            if (running) {

                if (strSubActivityErrorCode == null) {

                    Cursor c = db.getSubActivityName(strId);

                    ArrayList<String> lstSubAct = new ArrayList<>();

                    lstSubAct.clear();

                    if (c.getCount() > 0) {
                        c.moveToFirst();
                        for (int ri = 0; ri < c.getCount(); ri++) {
                            lstSubAct
                                    .add(c.getString(c
                                            .getColumnIndex("ActivitySubCategoryName")));
                            c.moveToNext();
                        }
                    }

                    ArrayAdapter<String> activityAdapterActivityRecorder = new ArrayAdapter<>(
                            context, android.R.layout.simple_list_item_1,
                            android.R.id.text1, lstSubAct);
                    selSubActivityRecordActivity
                            .setAdapter(activityAdapterActivityRecorder);
                    activityAdapterActivityRecorder.notifyDataSetChanged();

                    int intBDMBankBranchCount = db
                            .GetBranchCount(strCIFBDMUserId);
                    if (intBDMBankBranchCount == 0) {

                        taskListBankBranch = new DownloadBankBranch();
                        startDownloadBankBranch();

                    }
                } else {

                }
            } else {
                syncerror();
            }
        }
    }

    class PushTaskList extends AsyncTask<String, String, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_ADDTASKLIST);

                request.addProperty("intReqId", "0");
                request.addProperty("activity_task_mast_id", "NA");
                request.addProperty("bdmid", lstSyncTaskList.get(0));
                request.addProperty("category_Mast_Id", lstSyncTaskList.get(1));
                request.addProperty("branch_name", lstSyncTaskList.get(2));
                request.addProperty("time_slot", lstSyncTaskList.get(3));
                request.addProperty("remarks", lstSyncTaskList.get(4));
                request.addProperty("activity_status", lstSyncTaskList.get(5));
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());
                request.addProperty("strSubstatus", lstSyncTaskList.get(6));

                mCommonMethods.TLSv12Enable();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    androidHttpTranport.call(SOAP_ACTION_ADDTASKLIST, envelope);
                    Object response = envelope.getResponse();

                    strResult = response.toString();

                } catch (XmlPullParserException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
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
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing())
                dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            if (running) {
                if (!strResult.contains("0")) {
                    tasksyncerror();

                    ArrayList<String> lstevent = new ArrayList<>();

                    Cursor c = db.UpdateSynsStatus(strActivityRowId);
                    if (c.getCount() > 0) {
                        c.moveToFirst();
                        for (int ii = 0; ii < c.getCount(); ii++) {
                            lstevent.add(c.getString(c
                                    .getColumnIndex("DateNameBDMT")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("EventNameBDMT")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("MonthBDMT")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("YearBDMT")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("TimeBDMT")));
                            lstevent.add(c.getString(c.getColumnIndex("Remark")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("Activity")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("SubActivity")));
                            lstevent.add(c.getString(c.getColumnIndex("TimeTo")));
                            lstevent.add(c.getString(c.getColumnIndex("UserID")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("ActivityStatus")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("ActivitySync")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("ActivityCreatedDate")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("ActivityModifiedDate")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("ActivityServerMasterId")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("ActivityLead")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("DateIDBDMT")));
                            c.moveToNext();
                        }
                    }

                    clsBDMTrackerCalendar objcla = null;
                    try {
                        objcla = new clsBDMTrackerCalendar(lstevent.get(0) == null ? "" : lstevent.get(0),
                                lstevent.get(1) == null ? ""
                                        : lstevent.get(1), lstevent
                                .get(2) == null ? ""
                                : lstevent.get(2), lstevent
                                .get(3) == null ? ""
                                : lstevent.get(3), lstevent
                                .get(4) == null ? ""
                                : lstevent.get(4), lstevent
                                .get(5) == null ? ""
                                : lstevent.get(5), lstevent
                                .get(6) == null ? ""
                                : lstevent.get(6), lstevent
                                .get(7) == null ? ""
                                : lstevent.get(7), lstevent
                                .get(8) == null ? ""
                                : lstevent.get(8), lstevent
                                .get(9) == null ? ""
                                : lstevent.get(9), "Close",
                                "True",
                                lstevent.get(12) == null ? ""
                                        : lstevent.get(12), lstevent
                                .get(13) == null ? ""
                                : lstevent.get(13),
                                strResult,
                                lstevent.get(15) == null ? ""
                                        : lstevent.get(15));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    db.UpdateActivityRecord(objcla, lstevent.get(16));

                    getallrecord();
                    displayTodayEvents();

                } else {
                    syncerror();
                }
            } else {
                syncerror();
            }
        }
    }

    class PushTaskListRecord extends AsyncTask<String, String, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_ADDTASKLIST);

                request.addProperty("intReqId", "0");
                request.addProperty("activity_task_mast_id", "NA");
                request.addProperty("bdmid", lstSyncTaskList.get(0));
                request.addProperty("category_Mast_Id", lstSyncTaskList.get(1));
                request.addProperty("branch_name", lstSyncTaskList.get(2));
                request.addProperty("time_slot", lstSyncTaskList.get(3));
                request.addProperty("remarks", lstSyncTaskList.get(4));
                request.addProperty("activity_status", lstSyncTaskList.get(5));
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());
                request.addProperty("strSubstatus", lstSyncTaskList.get(6));

                mCommonMethods.TLSv12Enable();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    androidHttpTranport.call(SOAP_ACTION_ADDTASKLIST, envelope);
                    Object response = envelope.getResponse();

                    strResult = response.toString();

                } catch (XmlPullParserException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
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
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing())
                dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            if (running) {
                if (!strResult.contains("0")) {
                    tasksyncerror();

                    ArrayList<String> lstevent = new ArrayList<>();

                    Cursor c1 = db.getSelectedRowRecord_ar(lstActRecord.get(0), lstActRecord.get(1), lstActRecord.get(2), lstActRecord.get(3), lstActRecord.get(4), lstActRecord.get(5), lstActRecord.get(6));

                    ArrayList<String> lsteventa = new ArrayList<>();

                    lsteventa.clear();

                    if (c1.getCount() > 0) {
                        c1.moveToFirst();
                        for (int ri = 0;
                             ri < c1.getCount(); ri++) {
                            lsteventa.add(c1.getString(c1.getColumnIndex("DateIDAR")));
                            c1.moveToNext();
                        }
                    }

                    strActivityRowId = lsteventa.get(0);

                    Cursor c = db.UpdateSynsStatusAR(strActivityRowId);
                    if (c.getCount() > 0) {
                        c.moveToFirst();
                        for (int ii = 0; ii < c.getCount(); ii++) {
                            lstevent.add(c.getString(c
                                    .getColumnIndex("DateNameAR")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("EventNameAR")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("MonthAR")));
                            lstevent.add(c.getString(c.getColumnIndex("YearAR")));
                            lstevent.add(c.getString(c.getColumnIndex("TimeAR")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("RemarkAR")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("ActivityAR")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("SubActivityAR")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("TimeToAR")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("UserIDAR")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("ActivitySyncAR")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("ActivityCreatedDateAR")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("ActivityModifiedDateAR")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("ActivityStatusAR")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("ActivityLeadAR")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("DateIDAR")));
                            c.moveToNext();
                        }
                    }

                    clsCalendarActivityRecorder objcla = null;
                    try {
						/*objcla = new clsCalendarActivityRecorder(lstevent
								.get(0).toString() == null ? "" : lstevent.get(
								0).toString(),
								lstevent.get(1).toString() == null ? ""
										: lstevent.get(1).toString(), lstevent
										.get(2).toString() == null ? ""
										: lstevent.get(2).toString(), lstevent
										.get(3).toString() == null ? ""
										: lstevent.get(3).toString(), lstevent
										.get(4).toString() == null ? ""
										: lstevent.get(4).toString(), lstevent
										.get(5).toString() == null ? ""
										: lstevent.get(5).toString(), lstevent
										.get(6).toString() == null ? ""
										: lstevent.get(6).toString(), lstevent
										.get(7).toString() == null ? ""
										: lstevent.get(7).toString(), lstevent
										.get(8).toString() == null ? ""
										: lstevent.get(8).toString(), lstevent
										.get(9).toString() == null ? ""
										: lstevent.get(9).toString(), "True",
								lstevent.get(11).toString() == null ? ""
										: lstevent.get(12).toString(), lstevent
										.get(12).toString() == null ? ""
										: lstevent.get(13).toString(), "Close",
								lstevent.get(14).toString() == null ? ""
										: lstevent.get(14).toString());*/

                        objcla = new clsCalendarActivityRecorder(lstevent
                                .get(0) == null ? "" : lstevent.get(
                                0),
                                lstevent.get(1) == null ? ""
                                        : lstevent.get(1), lstevent
                                .get(2) == null ? ""
                                : lstevent.get(2), lstevent
                                .get(3) == null ? ""
                                : lstevent.get(3), lstevent
                                .get(4) == null ? ""
                                : lstevent.get(4), lstevent
                                .get(5) == null ? ""
                                : lstevent.get(5), lstevent
                                .get(6) == null ? ""
                                : lstevent.get(6), lstevent
                                .get(7) == null ? ""
                                : lstevent.get(7), lstevent
                                .get(8) == null ? ""
                                : lstevent.get(8), lstevent
                                .get(9) == null ? ""
                                : lstevent.get(9), "True",
                                lstevent.get(11) == null ? ""
                                        : lstevent.get(11), lstevent
                                .get(12) == null ? ""
                                : lstevent.get(12), "Close",
                                lstevent.get(14) == null ? ""
                                        : lstevent.get(14));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    db.UpdateActivityRecordAR(objcla, lstevent.get(15));

                    displayTodayEventsra();

                } else {
                    syncerror();
                }
            } else {
                syncerror();
            }
        }
    }

    class PushTaskListDetail extends AsyncTask<String, String, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                String METHOD_NAME_ADDTASKLISTDETAIL = "saveUpdateTaskListDetails";
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_ADDTASKLISTDETAIL);

                request.addProperty("intReqId", "0");
                request.addProperty("bdmid", lstSyncTaskList.get(0));
                request.addProperty("activity_task_detail_Id", "NA");
                // request.addProperty("activity_task_Mast_Id",lstSyncTaskList.get(1).toString());
                request.addProperty("activity_task_Mast_Id", strPullMasterId);
                request.addProperty("category_Mast_Id", lstSyncTaskList.get(1));
                request.addProperty("subcategory_Mast_Id",
                        lstSyncTaskList.get(6));
                request.addProperty("time_slot", lstSyncTaskList.get(3));
                request.addProperty("remarks", lstSyncTaskList.get(4));
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());

                mCommonMethods.TLSv12Enable();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    String SOAP_ACTION_ADDTASKLISTDETAIL = "http://tempuri.org/saveUpdateTaskListDetails";
                    androidHttpTranport.call(SOAP_ACTION_ADDTASKLISTDETAIL,
                            envelope);
                    Object response = envelope.getResponse();

                    strResult = response.toString();

                } catch (XmlPullParserException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
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
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing())
                dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            if (running) {
                if (!strResult.contains("0")) {
                    tasksyncerror();

                    ArrayList<String> lstevent = new ArrayList<>();

                    Cursor c = db.UpdateSynsStatus(strActivityRowId);
                    if (c.getCount() > 0) {
                        c.moveToFirst();
                        for (int ii = 0; ii < c.getCount(); ii++) {
                            lstevent.add(c.getString(c
                                    .getColumnIndex("DateNameBDMT")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("EventNameBDMT")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("MonthBDMT")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("YearBDMT")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("TimeBDMT")));
                            lstevent.add(c.getString(c.getColumnIndex("Remark")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("Activity")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("SubActivity")));
                            lstevent.add(c.getString(c.getColumnIndex("TimeTo")));
                            lstevent.add(c.getString(c.getColumnIndex("UserID")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("ActivityStatus")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("ActivitySync")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("ActivityCreatedDate")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("ActivityModifiedDate")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("ActivityServerMasterId")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("ActivityLead")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("DateIDBDMT")));
                            c.moveToNext();
                        }
                    }

                    clsBDMTrackerCalendar objcla = null;
                    try {
                        objcla = new clsBDMTrackerCalendar(lstevent.get(0) == null ? "" : lstevent.get(0),
                                lstevent.get(1) == null ? ""
                                        : lstevent.get(1), lstevent
                                .get(2) == null ? ""
                                : lstevent.get(2), lstevent
                                .get(3) == null ? ""
                                : lstevent.get(3), lstevent
                                .get(4) == null ? ""
                                : lstevent.get(4), lstevent
                                .get(5) == null ? ""
                                : lstevent.get(5), lstevent
                                .get(6) == null ? ""
                                : lstevent.get(6), lstevent
                                .get(7) == null ? ""
                                : lstevent.get(7), lstevent
                                .get(8) == null ? ""
                                : lstevent.get(8), lstevent
                                .get(9) == null ? ""
                                : lstevent.get(9), "Close",
                                "True",
                                lstevent.get(12) == null ? ""
                                        : lstevent.get(12), lstevent
                                .get(13) == null ? ""
                                : lstevent.get(13),
                                strPullMasterId,
                                lstevent.get(15) == null ? ""
                                        : lstevent.get(15));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    db.UpdateActivityRecord(objcla, lstevent.get(16));

                    getallrecord();
                    displayTodayEvents();

                } else {
                    syncerror();
                }
            } else {
                syncerror();
            }
        }
    }

    class PushTaskListSeq extends AsyncTask<String, String, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_ADDTASKLIST);

                request.addProperty("intReqId", "0");
                request.addProperty("activity_task_mast_id", "NA");
                request.addProperty("bdmid", lstSyncTaskListSeq.get(0));
                request.addProperty("category_Mast_Id",
                        lstSyncTaskListSeq.get(1));
                request.addProperty("branch_name", lstSyncTaskListSeq.get(2));
                request.addProperty("time_slot", lstSyncTaskListSeq.get(3));
                request.addProperty("remarks", lstSyncTaskListSeq.get(4));
                request.addProperty("activity_status", lstSyncTaskListSeq
                        .get(5));
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());
                request.addProperty("strSubstatus", lstSyncTaskListSeq.get(6));

                mCommonMethods.TLSv12Enable();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    androidHttpTranport.call(SOAP_ACTION_ADDTASKLIST, envelope);
                    Object response = envelope.getResponse();

                    strResult = response.toString();

                } catch (XmlPullParserException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
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
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing())
                dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            if (running) {
                if (!strResult.contains("0")) {
                    strPullMasterId = strResult;

                    taskListDetail = new PushTaskListDetail();

                    startPushTaskListDetail();
                } else {
                    syncerror();
                }
            } else {
                syncerror();
            }
        }
    }

    class DownloadBankBranch extends AsyncTask<String, String, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                String METHOD_NAME_BDMBANKBRANCH = "getBdmBankBranch";
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_BDMBANKBRANCH);
                request.addProperty("bdmid", strCIFBDMUserId);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());

                mCommonMethods.TLSv12Enable();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl,
                        10000);
                try {
                    String SOAP_ACTION_BDMBANKBRANCH = "http://tempuri.org/getBdmBankBranch";
                    androidHttpTranport.call(SOAP_ACTION_BDMBANKBRANCH,
                            envelope);
                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("anyType{}")) {

                        SoapPrimitive sa = (SoapPrimitive) envelope
                                .getResponse();

                        ParseXML prsObj = new ParseXML();

                        String inputpolicylist = sa.toString();
                        inputpolicylist = prsObj.parseXmlTag(inputpolicylist,
                                "BDMTracker");

                        inputpolicylist = new ParseXML().parseXmlTag(
                                inputpolicylist, "ScreenData");
                        strBranchCodeErrorCode = inputpolicylist;

                        if (strBranchCodeErrorCode == null) {

                            inputpolicylist = sa.toString();
                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "BDMTracker");

                            List<String> Node = prsObj.parseParentNode(
                                    inputpolicylist, "Table");
                            List<XMLHolderBankBranch> nodeData = prsObj
                                    .parseNodeElementBankBranch(Node);

                            final List<XMLHolderBankBranch> lst;
                            lst = new ArrayList<>();
                            lst.clear();

                            lst.addAll(nodeData);

                            //int count = lst.size();
                            int temp = 0;
                            for (int i = 0; i < lst.size(); i++) {
                                if (temp == 0) {
                                    clsBranch obj = new clsBranch(
                                            "Select Branch Name", "",
                                            "Select Branch Code", "", "", "",
                                            "", strCIFBDMUserId);
                                    db.AddBranch(obj);
                                }
                                temp = 1;
                                String strId = lst.get(i).getId();
                                String strName = lst.get(i).getName();

                                clsBranch obj = new clsBranch(strName, strName,
                                        strId, "0", "2013-05-22", "0", "0",
                                        strCIFBDMUserId);
                                db.AddBranch(obj);
                            }
                        } else {

                        }
                    } else {
                        running = false;
                    }

                } catch (XmlPullParserException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
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
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing())
                dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            if (running) {

                if (strBranchCodeErrorCode == null) {

                    Cursor c1 = db.GetAllBranchName(strCIFBDMUserId);

                    ArrayList<String> lstevent = new ArrayList<>();

                    lstevent.clear();

                    if (c1.getCount() > 0) {
                        c1.moveToFirst();
                        for (int ri = 0; ri < c1.getCount(); ri++) {
                            lstevent.add(c1.getString(c1
                                    .getColumnIndex("BranchName")));
                            c1.moveToNext();
                        }
                    }

                    ArrayAdapter<String> bankbranchAdapter = new ArrayAdapter<>(
                            context, android.R.layout.simple_list_item_1,
                            android.R.id.text1, lstevent);
                    selBranch.setAdapter(bankbranchAdapter);
                    bankbranchAdapter.notifyDataSetChanged();

                    ArrayList<String> lsteventAct = new ArrayList<>();
                    lsteventAct.clear();

                    if (c1.getCount() > 0) {
                        c1.moveToFirst();
                        for (int ri = 0; ri < c1.getCount(); ri++) {
                            lsteventAct.add(c1.getString(c1
                                    .getColumnIndex("BranchName")));
                            c1.moveToNext();
                        }
                    }

                    ArrayAdapter<String> bankbranchAdapterActivityRecorder = new ArrayAdapter<>(
                            context, android.R.layout.simple_list_item_1,
                            android.R.id.text1, lsteventAct);
                    selBranchRecordActivity
                            .setAdapter(bankbranchAdapterActivityRecorder);
                    bankbranchAdapterActivityRecorder.notifyDataSetChanged();

                    // for define objective
                    ArrayList<String> lstbc = new ArrayList<>();

                    lstbc.clear();

                    if (c1.getCount() > 0) {
                        c1.moveToFirst();
                        for (int ri = 0; ri < c1.getCount(); ri++) {
                            //String b = c1.getString(c1.getColumnIndex("BranchName"));
                            String co = (c1.getString(c1
                                    .getColumnIndex("BranchParentId")));
                            // lstbc.add(co+ " " +b);
                            lstbc.add(co);
                            c1.moveToNext();
                        }
                    }

                    ArrayAdapter<String> commitmentAdapter = new ArrayAdapter<>(
                            context, android.R.layout.simple_list_item_1,
                            android.R.id.text1, lstbc);
                    selCommitment.setAdapter(commitmentAdapter);
                    commitmentAdapter.notifyDataSetChanged();

                    strCode = selCommitment.getSelectedItem().toString();
                    String strBranchName = db.getBranchName(strCode);
                    edcommitbname.setText(strBranchName);

                    ArrayAdapter<String> achievementAdapter = new ArrayAdapter<>(
                            context, android.R.layout.simple_list_item_1,
                            android.R.id.text1, lstbc);
                    selAchievement.setAdapter(achievementAdapter);
                    achievementAdapter.notifyDataSetChanged();

                    strCode = selAchievement.getSelectedItem().toString();
                    String strBranchName_ach = db.getBranchName(strCode);
                    edachievebrname.setText(strBranchName_ach);

                    ArrayAdapter<String> perachievementAdapter = new ArrayAdapter<>(
                            context, android.R.layout.simple_list_item_1,
                            android.R.id.text1, lstbc);
                    selPerAchievement.setAdapter(perachievementAdapter);
                    perachievementAdapter.notifyDataSetChanged();

                    strCode = selPerAchievement.getSelectedItem().toString();
                    String strBranchName_per = db.getBranchName(strCode);
                    edperachievebrname.setText(strBranchName_per);

                    // for branch pofile
                    ArrayList<String> lstbf = new ArrayList<>();

                    lstbf.clear();

                    if (c1.getCount() > 0) {
                        c1.moveToFirst();
                        for (int ri = 0; ri < c1.getCount(); ri++) {
                            lstbf.add(c1.getString(c1
                                    .getColumnIndex("BranchParentId")));
                            c1.moveToNext();
                        }
                    }

                    ArrayAdapter<String> bcodeAdapter = new ArrayAdapter<>(
                            context, android.R.layout.simple_list_item_1,
                            android.R.id.text1, lstbf);
                    selbranchprofilecode.setAdapter(bcodeAdapter);
                    bcodeAdapter.notifyDataSetChanged();

                    taskListParamList = new DownloadParamList();

                    startDownloadParamList();

                } else {

                }

            } else {
                syncerror();
            }
        }
    }

    class DownloadParamList extends AsyncTask<String, String, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                String METHOD_NAME_PARAMLIST = "getObjectiveParamList";
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_PARAMLIST);
                request.addProperty("bdmid", strCIFBDMUserId);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());

                mCommonMethods.TLSv12Enable();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    String SOAP_ACTION_PARAMLIST = "http://tempuri.org/getObjectiveParamList";
                    androidHttpTranport.call(SOAP_ACTION_PARAMLIST, envelope);
                    Object response = envelope.getResponse();
                    if (!response.toString().contentEquals("anyType{}")) {

                        SoapPrimitive sa = (SoapPrimitive) envelope
                                .getResponse();

                        ParseXML prsObj = new ParseXML();

                        String inputpolicylist = sa.toString();
                        inputpolicylist = prsObj.parseXmlTag(inputpolicylist,
                                "BDMTracker");

                        inputpolicylist = new ParseXML().parseXmlTag(
                                inputpolicylist, "ScreenData");
                        strParamListErrorCode = inputpolicylist;

                        if (strParamListErrorCode == null) {
                            inputpolicylist = sa.toString();
                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "BDMTracker");

                            List<String> Node = prsObj.parseParentNode(
                                    inputpolicylist, "Table");
                            List<XMLHolderParamList> nodeData = prsObj
                                    .parseNodeElementParamList(Node);

                            final List<XMLHolderParamList> lst;
                            lst = new ArrayList<>();
                            lst.clear();

                            lst.addAll(nodeData);

                            //int count = lst.size();
                            for (int i = 0; i < lst.size(); i++) {
                                String strId = lst.get(i).getId();
                                String strName = lst.get(i).getName();

                                clsParamList obj = new clsParamList(strName,
                                        strName, strId, "0", "2013-05-22", "0",
                                        "0");
                                db.AddParam(obj);
                            }
                        } else {

                        }
                    } else {
                        running = false;
                    }

                } catch (XmlPullParserException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
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
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing())
                dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            if (running) {

                if (strParamListErrorCode == null) {

                    Cursor c1 = db.GetAllParamName();

                    ArrayList<String> lstevent = new ArrayList<>();

                    lstevent.clear();

                    if (c1.getCount() > 0) {
                        c1.moveToFirst();
                        for (int ri = 0; ri < c1.getCount(); ri++) {
                            lstevent.add(c1.getString(c1
                                    .getColumnIndex("ParamName")));
                            c1.moveToNext();
                        }
                    }

                    for (int i = 0; i < lstevent.size(); i++) {
                        if (i == 0) {
                            txtcommtnewbuscash.setText(lstevent.get(i));
                            txtachnewbuscash
                                    .setText(lstevent.get(i));
                            txtperachnewbuscash.setText(lstevent.get(i));
                        } else if (i == 1) {

                            txtcommtshare.setText(lstevent.get(i));
                            txtachshare.setText(lstevent.get(i));
                            txtperachshare.setText(lstevent.get(i));
                        } else if (i == 2) {

                            txtcommtnewbuspre.setText(lstevent.get(i));
                            txtachnewbuspre.setText(lstevent.get(i));
                            txtperachnewbuspre.setText(lstevent.get(i));
                        } else if (i == 3) {

                            txtcommthomeloan
                                    .setText(lstevent.get(i));
                            txtachhomeloan.setText(lstevent.get(i));
                            txtperachhomeloan.setText(lstevent.get(i));
                        }
                    }
                } else {

                }
            } else {
                syncerror();
            }
        }
    }

    class DownloadSaveObjective extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        String edcommitbname_text, edcommitnewbusinesscash_text, edcommithomeloan_text, edcommitnewbusinesspre_text, edcommitsharesingle_text,
                edcommitremark_text;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
            edcommitbname_text = edcommitbname.getText().toString();
            edcommitnewbusinesscash_text = edcommitnewbusinesscash.getText().toString();
            edcommithomeloan_text = edcommithomeloan.getText().toString();
            edcommitnewbusinesspre_text = edcommitnewbusinesspre.getText().toString();
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_SAVE_OBJECTIVE);

                String strbranchname = edcommitbname_text;
                String strnewBusinesscash = edcommitnewbusinesscash_text;
                String strhomeloan = edcommithomeloan_text;
                String strnewbusinesspre = edcommitnewbusinesspre_text;
                String strsharesingle = edcommitsharesingle_text;
                String strremark = edcommitremark_text;

                int paramid = 0;
                //String value = "";
                if (!strnewBusinesscash.equalsIgnoreCase("")) {
                    paramid = 1;
                    //value = strnewBusinesscash;
                } else if (!strsharesingle.equalsIgnoreCase("")) {
                    paramid = 2;
                    //value = strsharesingle;
                } else if (!strnewbusinesspre.equalsIgnoreCase("")) {
                    paramid = 3;
                    //value = strnewbusinesspre;
                } else if (!strhomeloan.equalsIgnoreCase("")) {
                    paramid = 4;
                    //value = strhomeloan;
                }

                /*
                 * request.addProperty("objective_Param_value", value);
                 * request.addProperty("objective_Param_mast_id", paramid);
                 * request.addProperty("bdmid", strCIFBDMUserId);
                 * request.addProperty("brCode", strCode);
                 * request.addProperty("brName", strbranchname);
                 * request.addProperty("remarks", strremark);
                 */
                request.addProperty("objective_Param_mast_id", paramid);
                request.addProperty("bdmid", strCIFBDMUserId);
                request.addProperty("brCode", strCode);
                request.addProperty("brName", strbranchname);
                request.addProperty("parmaValue", paramid);
                request.addProperty("remarks", strremark);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());

                mCommonMethods.TLSv12Enable();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    androidHttpTranport.call(SOAP_ACTION_SAVE_OBJECTIVE,
                            envelope);
                    Object response = envelope.getResponse();

                    strResult = response.toString();

                } catch (XmlPullParserException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
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
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing())
                dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            if (running) {
                if (!strResult.contains("0")) {

                    for (int i = 0; i < totalparam - 1; i++) {
                        taskListSaveObjective = new DownloadSaveObjective();
                        startDownloadSaveObjective();
                    }
                } else {
                    syncerror();
                }
            } else {
                syncerror();
            }
        }
    }

    class DownloadBranchProfile extends AsyncTask<String, String, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                String METHOD_NAME_BRANCH_PROFILE = "getBranchProfile";
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_BRANCH_PROFILE);

                request.addProperty("bdmid", strCIFBDMUserId);
                request.addProperty("branchId", strBranchCode);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());

                mCommonMethods.TLSv12Enable();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    String SOAP_ACTION_BRANCH_PROFILE = "http://tempuri.org/getBranchProfile";
                    androidHttpTranport.call(SOAP_ACTION_BRANCH_PROFILE,
                            envelope);

                    SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();

                    ParseXML prsObj = new ParseXML();

                    String inputbranchprofile = sa.toString();

                    inputbranchprofile = prsObj.parseXmlTag(inputbranchprofile,
                            "BDMTracker");
                    inputbranchprofile = new ParseXML().parseXmlTag(
                            inputbranchprofile, "ScreenData");
                    strBranchProfileErrorCode = inputbranchprofile;

                    if (strBranchProfileErrorCode == null) {

                        inputbranchprofile = sa.toString();

                        inputbranchprofile = prsObj.parseXmlTag(
                                inputbranchprofile, "BDMTracker");

                        List<String> Node = prsObj.parseParentNode(
                                inputbranchprofile, "Table");
                        List<XMLHolderBranch_Profile> nodeData = prsObj
                                .parseNodeElementBranchProfile(Node);

                        final List<XMLHolderBranch_Profile> lst;
                        lst = new ArrayList<>();
                        lst.clear();

                        lst.addAll(nodeData);

                        //int count = lst.size();
                        for (int i = 0; i < lst.size(); i++) {
                            String branchcode = lst.get(i).getBranchcode();
                            String branch_name = lst.get(i).getBranch_name();
                            String branch_open_date = lst.get(i)
                                    .getBranch_open_date();
                            String branch_net_result = lst.get(i)
                                    .getBranch_net_result();
                            String branch_created_date = lst.get(i)
                                    .getBranch_created_date();

                            clsBranchProfile obj = new clsBranchProfile(
                                    branchcode, branch_name, branch_open_date,
                                    branch_net_result, branch_created_date);
                            db.AddBranchProfile(obj);

                            /*
                             * edbranchname.setText(branch_name);
                             * edbranchopendate.setText(branch_open_date);
                             * edbranchnetresult.setText(branch_net_result);
                             */
                        }

                    } else {

                    }

                } catch (XmlPullParserException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
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
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing())
                dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            if (running) {
                if (strBranchProfileErrorCode == null) {

                    Cursor c1 = db.GetBranchProfile(strBranchCode);

                    ArrayList<String> lst = new ArrayList<>();
                    lst.clear();

                    if (c1.getCount() > 0) {
                        c1.moveToFirst();
                        for (int ii = 0; ii < c1.getCount(); ii++) {
                            lst.add(c1.getString(c1
                                    .getColumnIndex("BranchCode")));
                            lst.add(c1.getString(c1
                                    .getColumnIndex("BranchProfileName")));
                            lst.add(c1.getString(c1
                                    .getColumnIndex("BranchOpenDate")));
                            lst.add(c1.getString(c1
                                    .getColumnIndex("BranchNetResult")));
                            lst.add(c1.getString(c1
                                    .getColumnIndex("BranchProfileCreatedDate")));
                            c1.moveToNext();
                        }
                    }

                    edbranchname.setText(lst.get(1));
                    edbranchopendate.setText(lst.get(2));
                    edbranchnetresult.setText(lst.get(3));

                } else {
                    // syncerror();
                }
            } else {
                syncerror();
            }
        }
    }

    class DownloadBranchDeposits extends AsyncTask<String, String, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                String METHOD_NAME_BRANCH_DEPOSIT = "getBranchDeposits";
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_BRANCH_DEPOSIT);

                request.addProperty("bdmid", strCIFBDMUserId);
                request.addProperty("branchId", strBranchCode);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());

                mCommonMethods.TLSv12Enable();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    String SOAP_ACTION_BRANCH_DEPOSIT = "http://tempuri.org/getBranchDeposits";
                    androidHttpTranport.call(SOAP_ACTION_BRANCH_DEPOSIT,
                            envelope);

                    SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();

                    ParseXML prsObj = new ParseXML();

                    String inputbranchprofile = sa.toString();

                    inputbranchprofile = prsObj.parseXmlTag(inputbranchprofile,
                            "BDMTracker");
                    inputbranchprofile = new ParseXML().parseXmlTag(
                            inputbranchprofile, "ScreenData");
                    strBranchDepositsErrorCode = inputbranchprofile;

                    if (strBranchDepositsErrorCode == null) {

                        inputbranchprofile = sa.toString();

                        inputbranchprofile = prsObj.parseXmlTag(
                                inputbranchprofile, "BDMTracker");

                        List<String> Node = prsObj.parseParentNode(
                                inputbranchprofile, "Table");
                        List<XMLHolderDeposit> nodeData = prsObj
                                .parseNodeElementDeposit(Node);

                        final List<XMLHolderDeposit> lst;
                        lst = new ArrayList<>();
                        lst.clear();

                        lst.addAll(nodeData);

                        //int count = lst.size();
                        for (int i = 0; i < lst.size(); i++) {
                            String depositid = lst.get(i).getDepositid();
                            String branchcode = lst.get(i).getBranchcode();
                            String tot_acc = lst.get(i).getTot_acc();
                            String tot_amt = lst.get(i).getTot_amt();
                            String new_acc_b1k = lst.get(i).getNew_acc_b1k();
                            String new_balance_b1k = lst.get(i)
                                    .getNew_balance_b1k();
                            String new_acc_10kto1l = lst.get(i)
                                    .getNew_acc_10kto1l();
                            String new_bal_10kto1l = lst.get(i)
                                    .getNew_bal_10kto1l();
                            String new_acc_1lto5l = lst.get(i)
                                    .getNew_acc_1lto5l();
                            String new_bal_1lto5l = lst.get(i)
                                    .getNew_bal_1lto5l();
                            String new_acc_5landA = lst.get(i)
                                    .getNew_acc_5landA();
                            String new_bal_5landA = lst.get(i)
                                    .getNew_bal_5landA();
                            String category = lst.get(i).getCategory();

                            clsBranchDeposits obj = new clsBranchDeposits(
                                    depositid, branchcode, tot_acc, tot_amt,
                                    new_acc_b1k, new_balance_b1k,
                                    new_acc_10kto1l, new_bal_10kto1l,
                                    new_acc_1lto5l, new_bal_1lto5l,
                                    new_acc_5landA, new_bal_5landA, category);
                            db.AddBranchDeposits(obj);

                            /*
                             * if(i==0) { eddepositretail.setText(new_acc_b1k);
                             * }
                             */
                        }

                    } else {

                    }

                } catch (XmlPullParserException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
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
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing())
                dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            if (running) {
                if (strBranchDepositsErrorCode == null) {

                    String strDepositB1K = db.GetBranchDepositsB1K(
                            strBranchCode, strDepositPerticular);
                    eddepositretail.setText(strDepositB1K);

                } else {
                    // syncerror();
                }
            } else {
                syncerror();
            }
        }
    }

    class DownloadBranchAdvances extends AsyncTask<String, String, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                String METHOD_NAME_BRANCH_ADVANCES = "getBranchAdvances";
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_BRANCH_ADVANCES);

                request.addProperty("bdmid", strCIFBDMUserId);
                request.addProperty("branchId", strBranchCode);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());

                mCommonMethods.TLSv12Enable();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    String SOAP_ACTION_BRANCH_ADVANCES = "http://tempuri.org/getBranchAdvances";
                    androidHttpTranport.call(SOAP_ACTION_BRANCH_ADVANCES,
                            envelope);

                    SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();

                    ParseXML prsObj = new ParseXML();

                    String inputbranchprofile = sa.toString();

                    inputbranchprofile = prsObj.parseXmlTag(inputbranchprofile,
                            "BDMTracker");
                    inputbranchprofile = new ParseXML().parseXmlTag(
                            inputbranchprofile, "ScreenData");
                    strBranchAdvancesErrorCode = inputbranchprofile;

                    if (strBranchAdvancesErrorCode == null) {

                        inputbranchprofile = sa.toString();

                        inputbranchprofile = prsObj.parseXmlTag(
                                inputbranchprofile, "BDMTracker");

                        List<String> Node = prsObj.parseParentNode(
                                inputbranchprofile, "Table");
                        List<XMLHolderAdvances> nodeData = prsObj
                                .parseNodeElementAdvances(Node);

                        final List<XMLHolderAdvances> lst;
                        lst = new ArrayList<>();
                        lst.clear();

                        lst.addAll(nodeData);

                        //int count = lst.size();
                        for (int i = 0; i < lst.size(); i++) {
                            String advancesid = lst.get(i).getAdvancesid();
                            String branchcode = lst.get(i).getBranchcode();
                            String tot_no_of_acc = lst.get(i)
                                    .getTot_no_of_acc();
                            String tot_out = lst.get(i).getTot_out();
                            String no_of_acc_b1l = lst.get(i)
                                    .getNo_of_acc_b1l();
                            String tot_out_b1l = lst.get(i).getTot_out_b1l();
                            String no_of_acc_1lto5l = lst.get(i)
                                    .getNo_of_acc_1lto5l();
                            String tot_out_1lto5l = lst.get(i)
                                    .getTot_out_1lto5l();
                            String no_of_acc_a5l = lst.get(i)
                                    .getNo_of_acc_a5l();
                            String tot_out_a5l = lst.get(i).getTot_out_a5l();
                            String category = lst.get(i).getCategory();

                            clsBranchAdvances obj = new clsBranchAdvances(
                                    advancesid, branchcode, tot_no_of_acc,
                                    tot_out, no_of_acc_b1l, tot_out_b1l,
                                    no_of_acc_1lto5l, tot_out_1lto5l,
                                    no_of_acc_a5l, tot_out_a5l, category);
                            db.AddBranchAdvances(obj);

                            /*
                             * if(i==0) {
                             * edadvancesretail.setText(no_of_acc_b1l); }
                             */
                        }

                    } else {

                    }

                } catch (XmlPullParserException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
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
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing())
                dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            if (running) {
                if (strBranchAdvancesErrorCode == null) {

                    String strAdvancesB1L = db.GetBranchAdvacesB1L(
                            strBranchCode, strAdvancesPerticular);
                    edadvancesretail.setText(strAdvancesB1L);

                } else {
                    // syncerror();
                }
            } else {
                syncerror();
            }
        }
    }

    class DownloadSyncBranchProfile extends AsyncTask<String, String, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                String METHOD_NAME_BRANCH_PROFILE_TRANS = "saveBranchProfileTrans";
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_BRANCH_PROFILE_TRANS);

                request.addProperty("BRANCH_ADVANCES_DEPOSITS_ID", Sync_strId);
                request.addProperty("ADVANCES_DEPOSIT_FLAG", strBtnType);
                request.addProperty("BDM_ID", strCIFBDMUserId);
                request.addProperty("CROSS_SELL_PROD", Sync_cross);
                request.addProperty("CROSS_SELL_POT_NOPS", Sync_crossnop);
                request.addProperty("CROSS_SELL_POT_PREMIUM", Sync_crosspre);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());

                mCommonMethods.TLSv12Enable();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    try {
                        String SOAP_ACTION_BRANCH_PROFILE_TRANS = "http://tempuri.org/saveBranchProfileTrans";
                        androidHttpTranport.call(
                                SOAP_ACTION_BRANCH_PROFILE_TRANS, envelope);
                    } catch (XmlPullParserException e) {
                        try {
                            throw (e);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        mProgressDialog.dismiss();
                        //syncbranchprofile_running = false;
                    }
                    Object response = envelope.getResponse();
                    strResult = response.toString();
                } catch (IOException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    //syncbranchprofile_running = false;
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
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing())
                dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            if (running) {

                if (!strResult.contentEquals("0")) {
                    tasksyncerror();
                } else {
                    syncerror();
                }

            } else {
                syncerror();
            }
        }
    }

    class DownloadRinRaksha extends AsyncTask<String, String, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                String METHOD_NAME_RINRAKSHA = "getRinnRakashActBusi";
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_RINRAKSHA);
                request.addProperty("bdmid", strCIFBDMUserId);
                request.addProperty("branchId", strCode_ach);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());

                mCommonMethods.TLSv12Enable();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl,
                        10000);
                try {
                    String SOAP_ACTION_RINRAKSHA = "http://tempuri.org/getRinnRakashActBusi";
                    androidHttpTranport.call(SOAP_ACTION_RINRAKSHA, envelope);
                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("anyType{}")) {
                        strRinRaksha = response.toString();
                    } else {
                        strRinRaksha = "0";
                    }

                } catch (XmlPullParserException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
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
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing())
                dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            if (running) {

                edachievesharesingle.setText(strRinRaksha);

                taskBdm_Dashboard = new DownloadBdm_Dashboard();

                startdownloadBdm_Dashboard();
            } else {
                syncerror();
            }
        }
    }

    class DownloadBdm_Dashboard extends AsyncTask<String, String, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                String METHOD_NAME_BDM_BR_WISE_DASH = "getBDMBranchwiseDashBoard";
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_BDM_BR_WISE_DASH);

                request.addProperty("strBdmCiFNo", strCIFBDMUserId);
                request.addProperty("strBranchCode", strCode_ach);
                request.addProperty("strUserType", strCIFBDMUserType);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());

                mCommonMethods.TLSv12Enable();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    String SOAP_ACTION_BDM_BR_WISE_DASH = "http://tempuri.org/getBDMBranchwiseDashBoard";
                    androidHttpTranport.call(SOAP_ACTION_BDM_BR_WISE_DASH,
                            envelope);

                    SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();

                    ParseXML prsObj = new ParseXML();

                    String inputbranchprofile = sa.toString();

                    inputbranchprofile = prsObj.parseXmlTag(inputbranchprofile,
                            "CIFPolicyList");
                    inputbranchprofile = new ParseXML().parseXmlTag(
                            inputbranchprofile, "ScreenData");
                    strBranchProfileErrorCode = inputbranchprofile;

                    String strRenewelPolicy = "";
                    if (strBranchProfileErrorCode != null) {

                        inputbranchprofile = sa.toString();

                        inputbranchprofile = prsObj.parseXmlTag(
                                inputbranchprofile, "CIFPolicyList");

                        inputbranchprofile = prsObj.parseXmlTag(
                                inputbranchprofile, "ScreenData");

                        strRenewelPolicy = new ParseXML().parseXmlTag(
                                inputbranchprofile, "RenewedPolicy");
                        strNewPolicy = new ParseXML().parseXmlTag(
                                inputbranchprofile, "NewPolicy");
                        strRenewalPremium = new ParseXML().parseXmlTag(
                                inputbranchprofile, "RenewalPremium");
                        strNewBusinessPre = new ParseXML().parseXmlTag(
                                inputbranchprofile, "NewBusinessPremium");

                    } else {
                        strRenewelPolicy = "";
                        strNewPolicy = "";
                        strRenewalPremium = "";
                        strNewBusinessPre = "";
                    }

                } catch (XmlPullParserException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
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
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing())
                dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            if (running) {
                if (strBranchProfileErrorCode != null) {

                    edachievenewbusinesscash.setText(strNewBusinessPre);
                    edachievehomeloan.setText(strNewPolicy);
                    edachievenewbusinesspre.setText(strRenewalPremium);

                } else {
                    edachievenewbusinesscash.setText("0");
                    edachievehomeloan.setText("0");
                    edachievenewbusinesspre.setText("0");
                }
            } else {
                syncerror();
            }
        }
    }

    class DownloadBdm_mail_data extends AsyncTask<String, String, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                String METHOD_NAME_BDM_MAIL_DATA = "getBDMMailData";
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_BDM_MAIL_DATA);

                request.addProperty("bdmId", strCIFBDMUserId);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());

                mCommonMethods.TLSv12Enable();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    String SOAP_ACTION_BDM_MAIL_DATA = "http://tempuri.org/getBDMMailData";
                    androidHttpTranport.call(SOAP_ACTION_BDM_MAIL_DATA,
                            envelope);
                    Object response = envelope.getResponse();

                    SoapPrimitive sa;

                    ParseXML prsObj = new ParseXML();

                    if (!response.toString().contentEquals("anyType{}")) {
                        sa = (SoapPrimitive) envelope.getResponse();

                        String inputbdmmaildata = sa.toString();

                        inputbdmmaildata = prsObj.parseXmlTag(inputbdmmaildata,
                                "NewDataSet");
                        String strBdm_mail_data_ErrorCode = inputbdmmaildata;

                        if (strBdm_mail_data_ErrorCode != null) {

                            inputbdmmaildata = sa.toString();

                            inputbdmmaildata = prsObj.parseXmlTag(
                                    inputbdmmaildata, "NewDataSet");

                            inputbdmmaildata = prsObj.parseXmlTag(
                                    inputbdmmaildata, "North2");

                            List<String> Node = prsObj.parseParentNode(
                                    inputbdmmaildata, "North2");

                            List<XMLHolderBDM_MAIL_DATA> nodeData = prsObj
                                    .parseNodeElementBDM_MAIl_DATA(Node);

                            final List<XMLHolderBDM_MAIL_DATA> lst;
                            lst = new ArrayList<>();
                            lst.clear();

                            lst.addAll(nodeData);

                            for (int i = 0; i < lst.size(); i++) {
                                strNBPMTD = lst.get(i).getNBPMTD();
                                strNBPYTD = lst.get(i).getNBPYTD();
                                strNOPMTD = lst.get(i).getNOPMTD();
                                strNOPYTD = lst.get(i).getNOPYTD();
                            }

                        } else {
                            strNBPMTD = "";
                            strNBPYTD = "";
                            strNOPMTD = "";
                            strNOPYTD = "";
                        }

                    } else {

                    }

                } catch (XmlPullParserException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
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
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            // getActivity().dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            if (mProgressDialog.isShowing())
                dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            if (running) {

                if (strType.contentEquals("Branch Manager")) {
                    // for standard mail formate
                    String strEmailID = "";
                    String strMobileNo = "";

                    try {
                        strEmailID = SimpleCrypto.decrypt("SBIL",
                                db.GetEmailId());
                        strMobileNo = SimpleCrypto.decrypt("SBIL",
                                db.GetMobileNo());
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    String emailBody = "Dear Sir / Ma�am,"
                            + "\n"
                            + "\n"
                            + "Greetings of the day!!"
                            + "\n"
                            + "\n"
                            + "We place below the SBI Life cross selling performance of your branch as on ...."
                            + "\n"
                            + "Particulars              Business Sourced MTD      Business Issued YTD"
                            + "\n" + "New Business Premium (NBP)"
                            + strNBPMTD
                            + strNBPYTD
                            + "\n"
                            + "Individual NoP"
                            + strNOPMTD
                            + strNOPYTD
                            + "\n"
                            + "All Business figures are on Billed basis."
                            + "\n"
                            + "\n"
                            + "For more details on performance, please feel free to contact undersigned,"
                            + "\n"
                            + "\n"
                            + "Happy Selling !!"
                            + "\n"
                            + "\n"
                            + "\n"
                            + "Thanks & regards,"
                            + "\n"
                            + "abc"
                            + "\n"
                            + "SBI Life Insurance Co. Ltd."
                            + "\n"
                            + "CPC Belapur Mumbai"
                            + "\n"
                            + "Email  : "
                            + strEmailID + "\n" + "Mobile  : " + strMobileNo;

                    btnbmreport.setBackgroundResource(R.drawable.exp_selected);
                    btncifreport
                            .setBackgroundResource(R.drawable.exp_unselected);
                    btnagmreport
                            .setBackgroundResource(R.drawable.exp_unselected);
                    btnrmreport
                            .setBackgroundResource(R.drawable.exp_unselected);

                    // strType = "Branch Manager";
                    Cursor c = db.getGroupListEmail(strType, strCIFBDMUserId);
                    ArrayList<String> items = new ArrayList<>();
                    if (c.getCount() > 0) {
                        c.moveToFirst();
                        for (int ii = 0; ii < c.getCount(); ii++) {
                            items.add(c.getString(c.getColumnIndex("EmailName")));
                            c.moveToNext();
                        }
                    }

                    //String str = "branch.manager@sbilife.co.in";
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setType("text/plain/email/dir");
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                            "Branch Performance");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
                    emailIntent.setData(Uri.parse("mailto:" + items));
                    try {
                        startActivity(emailIntent);
                    } catch (android.content.ActivityNotFoundException e) {
                        Toast.makeText(context,
                                "There are No Email Client Installed",
                                Toast.LENGTH_SHORT).show();
                    }
                } else if (strType.contentEquals("CIF")) {
                    // for standard mail formate
                    String strEmailID = "";
                    String strMobileNo = "";

                    try {
                        strEmailID = SimpleCrypto.decrypt("SBIL",
                                db.GetEmailId());
                        strMobileNo = SimpleCrypto.decrypt("SBIL",
                                db.GetMobileNo());
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    String emailBody = "Dear Sir / Ma�am,"
                            + "\n"
                            + "\n"
                            + "Greetings of the day!!"
                            + "\n"
                            + "\n"
                            + "We place below your SBI Life business details "
                            + "\n"
                            + "Particulars              Business Sourced MTD      Business Issued YTD"
                            + "\n" + "New Business Premium (NBP)"
                            + strNBPMTD
                            + strNBPYTD
                            + "\n"
                            + "Individual NoP"
                            + strNOPMTD
                            + strNOPYTD
                            + "\n"
                            + "All Business figures are on Billed basis."
                            + "\n"
                            + "\n"
                            + "For more details on performance, please feel free to contact undersigned,"
                            + "\n"
                            + "\n"
                            + "Happy Selling !!"
                            + "\n"
                            + "\n"
                            + "\n"
                            + "Thanks & regards,"
                            + "\n"
                            + "abc"
                            + "\n"
                            + "SBI Life Insurance Co. Ltd."
                            + "\n"
                            + "CPC Belapur Mumbai"
                            + "\n"
                            + "Email  : "
                            + strEmailID + "\n" + "Mobile  : " + strMobileNo;

                    btnbmreport
                            .setBackgroundResource(R.drawable.exp_unselected);
                    btncifreport.setBackgroundResource(R.drawable.exp_selected);
                    btnagmreport
                            .setBackgroundResource(R.drawable.exp_unselected);
                    btnrmreport
                            .setBackgroundResource(R.drawable.exp_unselected);

                    // strType = "CIF";
                    Cursor c = db.getGroupListEmail(strType, strCIFBDMUserId);
                    ArrayList<String> items = new ArrayList<>();
                    if (c.getCount() > 0) {
                        c.moveToFirst();
                        for (int ii = 0; ii < c.getCount(); ii++) {
                            items.add(c.getString(c.getColumnIndex("EmailName")));
                            c.moveToNext();
                        }
                    }

                    //String str = "cif@sbilife.co.in";
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setType("text/plain/email/dir");
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                            "Insurance Detail");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
                    emailIntent.setData(Uri.parse("mailto:" + items));
                    try {
                        startActivity(emailIntent);
                    } catch (android.content.ActivityNotFoundException e) {
                        Toast.makeText(context,
                                "There are No Email Client Installed",
                                Toast.LENGTH_SHORT).show();
                    }
                }

            } else {
                Toast.makeText(context, "Server Not Responding,Try again..",
                        Toast.LENGTH_SHORT).show();

                if (strType.contentEquals("Branch Manager")) {
                    // for standard mail formate
                    String strEmailID = "";
                    String strMobileNo = "";

                    try {
                        strEmailID = SimpleCrypto.decrypt("SBIL",
                                db.GetEmailId());
                        strMobileNo = SimpleCrypto.decrypt("SBIL",
                                db.GetMobileNo());
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    String emailBody = "Dear Sir / Ma�am,"
                            + "\n"
                            + "\n"
                            + "Greetings of the day!!"
                            + "\n"
                            + "\n"
                            + "We place below the SBI Life cross selling performance of your branch as on ...."
                            + "\n"
                            + "Particulars              Business Sourced MTD      Business Issued YTD"
                            + "\n" + "New Business Premium (NBP)"
                            + strNBPMTD
                            + strNBPYTD
                            + "\n"
                            + "Individual NoP"
                            + strNOPMTD
                            + strNOPYTD
                            + "\n"
                            + "All Business figures are on Billed basis."
                            + "\n"
                            + "\n"
                            + "For more details on performance, please feel free to contact undersigned,"
                            + "\n"
                            + "\n"
                            + "Happy Selling !!"
                            + "\n"
                            + "\n"
                            + "\n"
                            + "Thanks & regards,"
                            + "\n"
                            + "abc"
                            + "\n"
                            + "SBI Life Insurance Co. Ltd."
                            + "\n"
                            + "CPC Belapur Mumbai"
                            + "\n"
                            + "Email  : "
                            + strEmailID + "\n" + "Mobile  : " + strMobileNo;

                    btnbmreport.setBackgroundResource(R.drawable.exp_selected);
                    btncifreport
                            .setBackgroundResource(R.drawable.exp_unselected);
                    btnagmreport
                            .setBackgroundResource(R.drawable.exp_unselected);
                    btnrmreport
                            .setBackgroundResource(R.drawable.exp_unselected);

                    // strType = "Branch Manager";
                    Cursor c = db.getGroupListEmail(strType, strCIFBDMUserId);
                    ArrayList<String> items = new ArrayList<>();
                    if (c.getCount() > 0) {
                        c.moveToFirst();
                        for (int ii = 0; ii < c.getCount(); ii++) {
                            items.add(c.getString(c.getColumnIndex("EmailName")));
                            c.moveToNext();
                        }
                    }

                    //String str = "branch.manager@sbilife.co.in";
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setType("text/plain/email/dir");
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                            "Branch Performance");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
                    emailIntent.setData(Uri.parse("mailto:" + items));
                    try {
                        startActivity(emailIntent);
                    } catch (android.content.ActivityNotFoundException e) {
                        Toast.makeText(context,
                                "There are No Email Client Installed",
                                Toast.LENGTH_SHORT).show();
                    }
                } else if (strType.contentEquals("CIF")) {
                    // for standard mail formate
                    String strEmailID = "";
                    String strMobileNo = "";

                    try {
                        strEmailID = SimpleCrypto.decrypt("SBIL",
                                db.GetEmailId());
                        strMobileNo = SimpleCrypto.decrypt("SBIL",
                                db.GetMobileNo());
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    String emailBody = "Dear Sir / Ma�am,"
                            + "\n"
                            + "\n"
                            + "Greetings of the day!!"
                            + "\n"
                            + "\n"
                            + "We place below your SBI Life business details "
                            + "\n"
                            + "Particulars              Business Sourced MTD      Business Issued YTD"
                            + "\n" + "New Business Premium (NBP)"
                            + strNBPMTD
                            + strNBPYTD
                            + "\n"
                            + "Individual NoP"
                            + strNOPMTD
                            + strNOPYTD
                            + "\n"
                            + "All Business figures are on Billed basis."
                            + "\n"
                            + "\n"
                            + "For more details on performance, please feel free to contact undersigned,"
                            + "\n"
                            + "\n"
                            + "Happy Selling !!"
                            + "\n"
                            + "\n"
                            + "\n"
                            + "Thanks & regards,"
                            + "\n"
                            + "abc"
                            + "\n"
                            + "SBI Life Insurance Co. Ltd."
                            + "\n"
                            + "CPC Belapur Mumbai"
                            + "\n"
                            + "Email  : "
                            + strEmailID + "\n" + "Mobile  : " + strMobileNo;

                    btnbmreport
                            .setBackgroundResource(R.drawable.exp_unselected);
                    btncifreport.setBackgroundResource(R.drawable.exp_selected);
                    btnagmreport
                            .setBackgroundResource(R.drawable.exp_unselected);
                    btnrmreport
                            .setBackgroundResource(R.drawable.exp_unselected);

                    // strType = "CIF";
                    Cursor c = db.getGroupListEmail(strType, strCIFBDMUserId);
                    ArrayList<String> items = new ArrayList<>();
                    if (c.getCount() > 0) {
                        c.moveToFirst();
                        for (int ii = 0; ii < c.getCount(); ii++) {
                            items.add(c.getString(c.getColumnIndex("EmailName")));
                            c.moveToNext();
                        }
                    }

                    //String str = "cif@sbilife.co.in";
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setType("text/plain/email/dir");
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                            "Insurance Detail");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
                    emailIntent.setData(Uri.parse("mailto:" + items));
                    try {
                        startActivity(emailIntent);
                    } catch (android.content.ActivityNotFoundException e) {
                        Toast.makeText(context,
                                "There are No Email Client Installed",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    class DownloadLead extends AsyncTask<String, String, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                String METHOD_NAME_LEADLIST = "getLeadList";
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_LEADLIST);

                request.addProperty("strBdmcode", strCIFBDMUserId);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());

                mCommonMethods.TLSv12Enable();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    String SOAP_ACTION_LEADLIST = "http://tempuri.org/getLeadList";
                    androidHttpTranport.call(SOAP_ACTION_LEADLIST, envelope);
                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("anyType{}")) {

                        SoapPrimitive sa = (SoapPrimitive) envelope
                                .getResponse();

                        ParseXML prsObj = new ParseXML();

                        String inputpolicylist = sa.toString();
                        inputpolicylist = prsObj.parseXmlTag(inputpolicylist,
                                "LeadList");

                        strLeadTag = inputpolicylist;

                        if (strLeadTag != null) {

                            inputpolicylist = sa.toString();
                            inputpolicylist = prsObj.parseXmlTag(inputpolicylist,
                                    "LeadList");

                            inputpolicylist = new ParseXML().parseXmlTag(
                                    inputpolicylist, "ScreenData");
                            strLeadErrorCode = inputpolicylist;

                            if (strLeadErrorCode == null) {
                                inputpolicylist = sa.toString();
                                inputpolicylist = prsObj.parseXmlTag(
                                        inputpolicylist, "LeadList");

                                List<String> Node = prsObj.parseParentNode(
                                        inputpolicylist, "Table");
                                List<clsHOLead> nodeData = prsObj
                                        .parseNodeElementLead(Node);

                                final List<clsHOLead> lst;
                                lst = new ArrayList<>();
                                lst.clear();

                                lst.addAll(nodeData);

                                //int count = lst.size();
                                for (int i = 0; i < lst.size(); i++) {
                                    String strLeadDate = lst.get(i).get_date();
                                    String strCustId = lst.get(i).get_custid();
                                    String strCustName = lst.get(i).get_custname();
                                    String strLeadPriority = lst.get(i).get_priority();
                                    String strLeadStatus = lst.get(i).get_status();
                                    String strLeadSubStatus = lst.get(i).get_substatus();
                                    String strProposalNo = lst.get(i).get_proposalno();
                                    String strFollowupdate = lst.get(i).get_followupdate();
                                    String strComments = lst.get(i).get_comments();
                                    String strAge = lst.get(i).get_age();
                                    String strTotalAcc = lst.get(i).get_totalacc();
                                    String strBalance = lst.get(i).get_balance();
                                    String strBranchCode = lst.get(i).get_branchcode();
                                    String strCIFBDMUserId = lst.get(i).get_userid();
                                    String strSyncStatus = "Open";
                                    String strLeadID = lst.get(i).get_leadid();
                                    String strBDMName = lst.get(i).get_name();
                                    String strSource = lst.get(i).get_source();

                                    int count = db.GetHOLeadServerId(strLeadID);

                                    if (count == 0) {
                                        clsHOLead cls = new clsHOLead(strLeadDate, strCustId, strCustName, strLeadPriority, strLeadStatus, strLeadSubStatus, strProposalNo, strFollowupdate, strComments, strAge, strTotalAcc, strBalance, strBranchCode, strCIFBDMUserId, strSyncStatus, strLeadID, strBDMName, strSource);
                                        db.AddHOLead(cls);
                                    }

                                }
                            } else {

                            }

                        }
                    } else {
                        running = false;
                    }

                } catch (XmlPullParserException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
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
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing())
                dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            if (running) {

                if (strLeadTag != null) {

                    if (strLeadErrorCode == null) {

                        GetAllLead();

                    } else {

                    }
                }
            } else {
                //syncerror();
                Toast.makeText(context, "Server Not Responding,Try again..", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void GetAllLead() {
        Cursor c1 = db.GetAllLead(strCIFBDMUserId);

        String strLeadDate = "";
        String strCustId = "";
        String strCustName = "";
        String strLeadPriority = "";
        String strLeadStatus = "";
        String strLeadSubStatus = "";
        String strProposalNo = "";
        String strFollowupdate = "";
        String strComments = "";
        String strAge = "";
        String strTotalAcc = "";
        String strBalance = "";
        String strBranchCode = "";
        String strSyncStatus = "";
        String strLeadID = "";
        String strBDMName = "";
        String strSource = "";

        lsthoLead.clear();

        if (c1.getCount() > 0) {
            c1.moveToFirst();
            for (int ii = 0; ii < c1.getCount(); ii++) {
                clsHOLead objClshoLead = new clsHOLead(strLeadDate, strCustId, strCustName, strLeadPriority, strLeadStatus, strLeadSubStatus, strProposalNo, strFollowupdate, strComments, strAge, strTotalAcc, strBalance, strBranchCode, strCIFBDMUserId, strSyncStatus, strLeadID, strBDMName, strSource);
                //objClshoLead.set_date(c1.getString(c1.getColumnIndex("HOLeadDate")));
                String strfromdate = c1.getString(c1.getColumnIndex("HOLeadDate"));

                Date dt1 = null;
                try {
                    dt1 = df.parse(strfromdate);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                strfromdate = dateFormat.format(dt1);


                String strfdate = c1.getString(c1.getColumnIndex("HOLeadFollowUpDate"));

                if (!strfdate.equalsIgnoreCase("")) {
                    Date dt2 = null;
                    try {
                        dt2 = df.parse(strfdate);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    strfdate = dateFormat.format(dt2);
                }

                objClshoLead.set_date(strfromdate);
                objClshoLead.set_custid(c1.getString(c1.getColumnIndex("HOLeadCustomerId")));
                objClshoLead.set_custname(c1.getString(c1.getColumnIndex("HOLeadCustomerName")));
                objClshoLead.set_priority(c1.getString(c1.getColumnIndex("HOLeadPriority")));
                objClshoLead.set_status(c1.getString(c1.getColumnIndex("HOLeadStatus")));
                objClshoLead.set_substatus(c1.getString(c1.getColumnIndex("HOLeadSubStatus")));
                objClshoLead.set_proposalno(c1.getString(c1.getColumnIndex("HOLeadProposalNo")));
                //objClshoLead.set_followupdate(c1.getString(c1.getColumnIndex("HOLeadFollowUpDate")));
                objClshoLead.set_followupdate(strfdate);
                objClshoLead.set_comments(c1.getString(c1.getColumnIndex("HOLeadComments")));
                objClshoLead.set_age(c1.getString(c1.getColumnIndex("HOLeadAge")));
                objClshoLead.set_totalacc(c1.getString(c1.getColumnIndex("HOLeadTotalAcc")));
                objClshoLead.set_balance(c1.getString(c1.getColumnIndex("HOLeadBalance")));
                objClshoLead.set_branchcode(c1.getString(c1.getColumnIndex("HOLeadBranchCode")));
                objClshoLead.set_sync(c1.getString(c1.getColumnIndex("HOLeadSync")));
                objClshoLead.set_name(c1.getString(c1.getColumnIndex("HOLeadSync")));
                objClshoLead.set_userid(c1.getString(c1.getColumnIndex("HOLeadUserID")));
                objClshoLead.set_name(c1.getString(c1.getColumnIndex("HOLeadBDMName")));
                objClshoLead.set_leadid(c1.getString(c1.getColumnIndex("HOLeadServerID")));

                lsthoLead.add(objClshoLead);
                c1.moveToNext();
            }
        }

        if (lsthoLead.size() > 0) {
            ItemsAdapter_HOLead adapter = new ItemsAdapter_HOLead(context, 0, lsthoLead);
            adapter.setNotifyOnChange(true);
            lvholead.setAdapter(adapter);
            setTodaysList(lvholead);

            tblsearchholead.setVisibility(View.VISIBLE);

        } else {
            //ArrayList<clsHOLead> lstmain = new ArrayList<clsHOLead>();

            ItemsAdapter_HOLead adapter = new ItemsAdapter_HOLead(context, 0, lsthoLead);
            adapter.setNotifyOnChange(true);
            lvholead.setAdapter(adapter);
            setTodaysList(lvholead);

            tblsearchholead.setVisibility(View.GONE);
        }

    }


    private class ItemsAdapter_HOLead extends ArrayAdapter<clsHOLead> {
        //xprivate int selectedPos = -1;
        final ArrayList<clsHOLead> items;

        ItemsAdapter_HOLead(Context context, int textViewResourceId, ArrayList<clsHOLead> items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }

			/*public void setSelectedPosition(int pos) {
				selectedPos = pos;
				// inform the view of getActivity() change
				notifyDataSetChanged();
			}

			public int getSelectedPosition() {
				return selectedPos;
			}*/

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {

            TextView date;
            TextView custid;
            TextView custname;
            TextView priority;
            TextView status;
            TextView substatus;
            TextView propno;
            TextView followdate;
            TextView comments;
            TextView age;
            TextView totalacc;
            TextView balance;
            TextView brcode;
            TextView sync;
            TextView code;
            TextView name;

            View view = convertView;
            if (view == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R.layout.ho_lead_list_item, null);
            }

            date = view.findViewById(R.id.txtholeaddate);
            custid = view.findViewById(R.id.txtholeadcustid);
            custname = view.findViewById(R.id.txtholeadcustname);
            priority = view.findViewById(R.id.txtholeadpriority);
            status = view.findViewById(R.id.txtholeadstatus);
            substatus = view.findViewById(R.id.txtholeadsubstatus);
            propno = view.findViewById(R.id.txtholeadproposalno);
            followdate = view.findViewById(R.id.txtholeadfollowupdate);
            comments = view.findViewById(R.id.txtholeadcomnts);
            age = view.findViewById(R.id.txtholeadage);
            totalacc = view.findViewById(R.id.txtholeadtotalacc);
            balance = view.findViewById(R.id.txtholeadbalance);
            brcode = view.findViewById(R.id.txtholeadbrcode);
            sync = view.findViewById(R.id.txtholeadsync);
            code = view.findViewById(R.id.txtholeadbdmcode);
            name = view.findViewById(R.id.txtholeadbdmname);

            date.setText(items.get(position).get_date());
            custid.setText(items.get(position).get_custid());
            custname.setText(items.get(position).get_custname());
            priority.setText(items.get(position).get_priority());
            status.setText(items.get(position).get_status());
            substatus.setText(items.get(position).get_substatus());
            propno.setText(items.get(position).get_proposalno());
            followdate.setText(items.get(position).get_followupdate());
            comments.setText(items.get(position).get_comments());

				/*double value = Double.parseDouble(items.get(position).get_age());
				int i = (int) value;
				String strvalue  = String.valueOf(i);

				age.setText(strvalue);*/

            age.setText(items.get(position).get_age());
            totalacc.setText(items.get(position).get_totalacc());
            balance.setText(items.get(position).get_balance());
            brcode.setText(items.get(position).get_branchcode());
            sync.setText(items.get(position).get_sync());
            code.setText(strCIFBDMUserId);
            name.setText(items.get(position).get_name());

            return view;
        }

        @Override
        public int getCount() {
            return items.size();
        }

			/*@Override
			public Object getItem(int position) {
				return position;
			}*/

        @Override
        public long getItemId(int position) {
            return position;
        }

    }


    private void syncerror() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Server Not Responding,Try again..");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void tasksyncerror() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Record Successfully Sync ..");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void activitysynalert() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Record Already Sync ..");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void activityeditalert() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Sync record can not be edit..");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void activitydeletealert() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Sync record can not be delete..");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void branch_code_validation() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Please Select Branch Code..");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void deposits_advances_validation() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Please Select 'Deposits' or 'Advances'..");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void deletealert() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Delete Successfully...");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void BDMDOBAlert() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("You are Not Authorised User..");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void btn_deposits(View v) {

        if (!strBranchCode.contentEquals("Select Branch Code")) {
            btndeposits.setBackgroundResource(R.drawable.exp_selected);
            btnadvances.setBackgroundResource(R.drawable.exp_unselected);

            tblrdeposit.setVisibility(View.VISIBLE);
            tblradvances.setVisibility(View.GONE);

            strBtnType = "D";

            int count = db.BranchDepositsExistorNot(strBranchCode);
            if (count == 0) {
                taskBranchDeposits = new DownloadBranchDeposits();
                if (mCommonMethods.isNetworkConnected(context)) {
                    startdownloadbranchdeposits();
                } else {
                    mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
                }

            } else {
                /*
                 * String strDepositB1K = db.GetBranchDepositsB1K(strBranchCode,
                 * strDepositPerticular);
                 */

                String strDepositB1K = db.GetBranchDepositsB1K(strBranchCode,
                        seldeposit.getSelectedItem().toString());

                eddepositretail.setText(strDepositB1K);
            }
        } else {
            branch_code_validation();
        }
    }

    public void btn_advances(View v) {

        if (!strBranchCode.contentEquals("Select Branch Code")) {
            btndeposits.setBackgroundResource(R.drawable.exp_unselected);
            btnadvances.setBackgroundResource(R.drawable.exp_selected);

            tblrdeposit.setVisibility(View.GONE);
            tblradvances.setVisibility(View.VISIBLE);

            strBtnType = "A";

            int count = db.BranchAdvancesExistorNot(strBranchCode);
            if (count == 0) {
                taskBranchAdvances = new DownloadBranchAdvances();
                if (mCommonMethods.isNetworkConnected(context)) {
                    startdownloadbranchadvances();
                } else {
                    mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
                }
            } else {
                /*
                 * String strAdvancesB1L = db.GetBranchAdvacesB1L(strBranchCode,
                 * strAdvancesPerticular);
                 */

                String strAdvancesB1L = db.GetBranchAdvacesB1L(strBranchCode,
                        seladvances.getSelectedItem().toString());

                edadvancesretail.setText(strAdvancesB1L);

            }
        } else {
            branch_code_validation();
        }
    }

    // save branch profile locally

    public void addBranchProfile(View v) {
        String dcross = eddepositcrosssel.getText().toString();
        String dcrossnop = eddepositcrossselnop.getText().toString();
        String dcrosspre = eddepositcrossselpot.getText().toString();

        String across = edadvancescrosssel.getText().toString();
        String acrossnop = edadvancescrossselnop.getText().toString();
        String acrosspre = edadvancescrossselpot.getText().toString();

        if (!strBranchCode.contentEquals("Select Branch Code")) {
            if (!strBtnType.contentEquals("")) {

                if (strBtnType.contentEquals("D")) {
                    if (dcross.equalsIgnoreCase("")
                            || dcrossnop.equalsIgnoreCase("")
                            || dcrosspre.equalsIgnoreCase("")) {
                        validation();
                    } else {
                        int count = db.SyncBranchProfileExistorNot(
                                strBranchCode, strDepositPerticular,
                                strDepositCategory);
                        if (count == 0) {
                            clsSyncBranchProfile obj = new clsSyncBranchProfile(
                                    strBranchCode, strDepositPerticular,
                                    strDepositCategory, dcross, dcrossnop,
                                    dcrosspre, strCIFBDMUserId);
                            db.AddSyncBranchProfile(obj);

                            savedefineobjectivelert();

                        } else {
                            existdefineobjectivelert();
                        }
                    }
                } else if (strBtnType.contentEquals("A")) {
                    if (across.equalsIgnoreCase("")
                            || acrossnop.equalsIgnoreCase("")
                            || acrosspre.equalsIgnoreCase("")) {
                        validation();
                    } else {
                        int count = db.SyncBranchProfileExistorNot(
                                strBranchCode, strAdvancesPerticular,
                                strAdvancesCategory);
                        if (count == 0) {
                            clsSyncBranchProfile obj = new clsSyncBranchProfile(
                                    strBranchCode, strAdvancesPerticular,
                                    strAdvancesCategory, across, acrossnop,
                                    acrosspre, strCIFBDMUserId);
                            db.AddSyncBranchProfile(obj);

                            savedefineobjectivelert();

                        } else {
                            existdefineobjectivelert();
                        }
                    }
                }
            } else {
                deposits_advances_validation();
            }
        } else {
            branch_code_validation();
        }
    }

    // sync branch profile locally

    public void syncBranchProfile(View v) {
        /*
         * String Sync_strId = ""; String Sync_cross = ""; String Sync_crossnop
         * = ""; String Sync_crosspre = "";
         */

        if (!strBranchCode.contentEquals("Select Branch Code")) {

            if (!strBtnType.contentEquals("")) {

                if (strBtnType.contentEquals("D")) {
                    // strId = db.GetBranchDepositsId(strBranchCode,
                    // strDepositPerticular);
                    Sync_strId = db.GetBranchDepositsId(strBranchCode,
                            seldeposit.getSelectedItem().toString());
                    Sync_cross = eddepositcrosssel.getText().toString();
                    Sync_crossnop = eddepositcrossselnop.getText().toString();
                    Sync_crosspre = eddepositcrossselpot.getText().toString();

                    if (Sync_cross.equalsIgnoreCase("")
                            || Sync_crossnop.equalsIgnoreCase("")
                            || Sync_crosspre.equalsIgnoreCase("")) {
                        validation();
                    } else {
                        if (mCommonMethods.isNetworkConnected(context)) {
                            taskSyncBranchProfile = new DownloadSyncBranchProfile();
                            uploadsyncbranchprofile();
                        } else {
                            mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
                        }
                    }

                } else if (strBtnType.contentEquals("A")) {
                    // strId = db.GetBranchAdvacesId(strBranchCode,
                    // strAdvancesPerticular);
                    Sync_strId = db.GetBranchAdvacesId(strBranchCode,
                            seladvances.getSelectedItem().toString());
                    Sync_cross = edadvancescrosssel.getText().toString();
                    Sync_crossnop = edadvancescrossselnop.getText().toString();
                    Sync_crosspre = edadvancescrossselpot.getText().toString();

                    if (Sync_cross.equalsIgnoreCase("")
                            || Sync_crossnop.equalsIgnoreCase("")
                            || Sync_crosspre.equalsIgnoreCase("")) {
                        validation();
                    } else {
                        if (mCommonMethods.isNetworkConnected(context)) {

                            taskSyncBranchProfile = new DownloadSyncBranchProfile();

                            uploadsyncbranchprofile();

                        } else {
                            mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
                        }
                    }
                }
            } else {
                deposits_advances_validation();
            }
        } else {
            branch_code_validation();
        }
    }

    @Override
    protected void onDestroy() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (taskActivity != null) {
            taskActivity.cancel(true);
        }
        if (taskSubActivity != null) {
            taskSubActivity.cancel(true);
        }
        if (taskList != null) {
            taskList.cancel(true);
        }
        if (taskListRecord != null) {
            taskListRecord.cancel(true);
        }
        if (taskListDetail != null) {
            taskListDetail.cancel(true);
        }
        if (taskListSeq != null) {
            taskListSeq.cancel(true);
        }
        if (taskListBankBranch != null) {
            taskListBankBranch.cancel(true);
        }
        if (taskListParamList != null) {
            taskListParamList.cancel(true);
        }
        if (taskListSaveObjective != null) {
            taskListSaveObjective.cancel(true);
        }
        if (taskBranchProfile != null) {
            taskBranchProfile.cancel(true);
        }
        if (taskBranchDeposits != null) {
            taskBranchDeposits.cancel(true);
        }
        if (taskBranchAdvances != null) {
            taskBranchAdvances.cancel(true);
        }
        if (taskSyncBranchProfile != null) {
            taskSyncBranchProfile.cancel(true);
        }
        if (taskRinRaksha != null) {
            taskRinRaksha.cancel(true);
        }
        if (taskBdm_Dashboard != null) {
            taskBdm_Dashboard.cancel(true);
        }

        if (taskBdm_mail_data != null) {
            taskBdm_mail_data.cancel(true);
        }

        if (taskLead != null) {
            taskLead.cancel(true);
        }
        super.onDestroy();
    }

    private void setTodaysList(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void setShowallList(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
