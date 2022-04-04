package sbilife.com.pointofsale_bancaagency.home;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;

@SuppressWarnings("deprecation")
public class Lead_MgtHOList extends AppCompatActivity {
    /**
     * Called when the activity is first created.
     */
    // server side lead

    private DownloadLead taskLead;

    private final int DIALOG_DOWNLOAD_PROGRESS = 1;
    private ProgressDialog mProgressDialog;

    private final String NAMESPACE = "http://tempuri.org/";
    private final String URl = ServiceURL.SERVICE_URL;

    private final String SOAP_ACTION_LEADLIST = "http://tempuri.org/getLeadList";
    private final String METHOD_NAME_LEADLIST = "getLeadList";

    private String strLeadTag = "";
    private String strLeadErrorCode = "";

    // end server side lead

    // Search ho lead

    private Spinner spsrholead;
    private Spinner spsrholeadpriority;
    private Spinner spsrholeadstatus;
    private Button btnsrrefrholead;
    private Button btnsrsearchholeaddate;
    private Button btnsrsearchhofoldate;
    private Button btnsrsearchholeadpr;
    private Button btnsrsearchholeadstatus;
    private Button btnsrsearchholeadno;
    private TableLayout tblsearchholead;
    private TableRow tblsrholddate;
    private TableRow tblsrholdfolwdate;
    private TableRow tblsrholdpri;
    private TableRow tblsrholdsts;
    private TableRow tblsrholdno;
    private EditText edsrholeadfdate;
    private EditText edsrholeadtdate;
    private EditText edsrhofolfdate;
    private EditText edsrhofoltdate;
    private ImageButton imgbtnsrholeadfdate;
    private ImageButton imgbtnsrholeadtdate;
    private ImageButton imgbtnsrhofolfdate;
    private ImageButton imgbtnsrhofoltdate;
    private TextView txtholeadcount;
    private EditText edsrsearchholeadno;

    private String strsrholeadfilter;

    private long lstHOLeadCount = 0;

    // end search ho lead

    private ListView lvholead;

    private ArrayList<clsHOLead> lsthoLead = new ArrayList<clsHOLead>();

    private int mYear;
    private int mMonth;
    private int mDay;

    private String y;
    private String m;
    private String d;

    private int datecheck = 0;

    private final int DATE_DIALOG_ID = 0;

    private Context con = this;

    // for store userid in bdm tracker table
    private String strCIFBDMUserId;
    private String strCIFBDMUserType;
    private String strCIFBDMEmailId = "";
    private String strCIFBDMPassword = "";
    private String strCIFBDMMObileNo = "";
    private DatabaseHelper db;

    // final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    protected ListView lv;
    private CommonMethods mCommonMethods;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.holeadlist);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        db = new DatabaseHelper(con);
        mCommonMethods = new CommonMethods();

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

        lvholead = findViewById(R.id.lvholead);

        mProgressDialog = new ProgressDialog(con);
        taskLead = new DownloadLead();

        // search ho lead

        spsrholead = findViewById(R.id.spsrholead);
        spsrholeadpriority = findViewById(R.id.spsrholeadpriority);
        spsrholeadstatus = findViewById(R.id.spsrholeadstatus);
        btnsrrefrholead = findViewById(R.id.btnsrrefrholead);
        btnsrsearchholeaddate = findViewById(R.id.btnsrsearchholeaddate);
        btnsrsearchhofoldate = findViewById(R.id.btnsrsearchhofoldate);
        btnsrsearchholeadpr = findViewById(R.id.btnsrsearchholeadpr);
        btnsrsearchholeadstatus = findViewById(R.id.btnsrsearchholeadstatus);
        tblsearchholead = findViewById(R.id.tblsearchholead);
        tblsrholddate = findViewById(R.id.tblsrholddate);
        tblsrholdfolwdate = findViewById(R.id.tblsrholdfolwdate);
        tblsrholdpri = findViewById(R.id.tblsrholdpri);
        tblsrholdsts = findViewById(R.id.tblsrholdsts);
        edsrholeadfdate = findViewById(R.id.edsrholeadfdate);
        edsrholeadtdate = findViewById(R.id.edsrholeadtdate);
        edsrhofolfdate = findViewById(R.id.edsrhofolfdate);
        edsrhofoltdate = findViewById(R.id.edsrhofoltdate);
        imgbtnsrholeadfdate = findViewById(R.id.imgbtnsrholeadfdate);
        imgbtnsrholeadtdate = findViewById(R.id.imgbtnsrholeadtdate);
        imgbtnsrhofolfdate = findViewById(R.id.imgbtnsrhofolfdate);
        imgbtnsrhofoltdate = findViewById(R.id.imgbtnsrhofoltdate);

        tblsrholdno = findViewById(R.id.tblsrholdno);
        edsrsearchholeadno = findViewById(R.id.edsrsearchholeadno);
        btnsrsearchholeadno = findViewById(R.id.btnsrsearchholeadno);

        // end search ho lead

        txtholeadcount = findViewById(R.id.txtholeadcount);

        btnsrrefrholead.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                GetAllLead();
            }
        });

        spsrholead.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {
                strsrholeadfilter = spsrholead.getSelectedItem().toString();
                if (strsrholeadfilter.contentEquals("Lead Date")) {
                    tblsrholddate.setVisibility(View.VISIBLE);
                    tblsrholdfolwdate.setVisibility(View.GONE);
                    tblsrholdpri.setVisibility(View.GONE);
                    tblsrholdsts.setVisibility(View.GONE);
                    tblsrholdno.setVisibility(View.GONE);
                } else if (strsrholeadfilter.contentEquals("Lead Priority")) {
                    tblsrholddate.setVisibility(View.GONE);
                    tblsrholdfolwdate.setVisibility(View.GONE);
                    tblsrholdpri.setVisibility(View.VISIBLE);
                    tblsrholdsts.setVisibility(View.GONE);
                    tblsrholdno.setVisibility(View.GONE);
                } else if (strsrholeadfilter.contentEquals("Lead Status")) {
                    tblsrholddate.setVisibility(View.GONE);
                    tblsrholdfolwdate.setVisibility(View.GONE);
                    tblsrholdpri.setVisibility(View.GONE);
                    tblsrholdsts.setVisibility(View.VISIBLE);
                    tblsrholdno.setVisibility(View.GONE);
                } else if (strsrholeadfilter.contentEquals("Follow Up Date")) {
                    tblsrholddate.setVisibility(View.GONE);
                    tblsrholdfolwdate.setVisibility(View.VISIBLE);
                    tblsrholdpri.setVisibility(View.GONE);
                    tblsrholdsts.setVisibility(View.GONE);
                    tblsrholdno.setVisibility(View.GONE);
                } else if (strsrholeadfilter.contentEquals("Lead No")) {
                    tblsrholddate.setVisibility(View.GONE);
                    tblsrholdfolwdate.setVisibility(View.GONE);
                    tblsrholdpri.setVisibility(View.GONE);
                    tblsrholdsts.setVisibility(View.GONE);
                    tblsrholdno.setVisibility(View.VISIBLE);
                }

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        imgbtnsrholeadfdate.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                datecheck = 3;
                onCreateDialog(DATE_DIALOG_ID);
            }
        });

        imgbtnsrholeadtdate.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                datecheck = 4;
                onCreateDialog(DATE_DIALOG_ID);
            }
        });

        imgbtnsrhofolfdate.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                datecheck = 5;
                onCreateDialog(DATE_DIALOG_ID);
            }
        });

        imgbtnsrhofoltdate.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                datecheck = 6;
                onCreateDialog(DATE_DIALOG_ID);
            }
        });

        btnsrsearchholeaddate.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                String strFromDate = edsrholeadfdate.getText().toString();
                String strToDate = edsrholeadtdate.getText().toString();
                if (strFromDate.equalsIgnoreCase("")
                        || strToDate.equalsIgnoreCase("")) {
                    // validation();
                    Toast.makeText(con, "All Fields Required..",
                            Toast.LENGTH_LONG).show();
                } else {
                    // final SimpleDateFormat formatter = new
                    // SimpleDateFormat("dd-MMMM-yyyy");
                    /*
                     * final SimpleDateFormat formatter = new SimpleDateFormat(
                     * "dd-MMMM-yyyy");
                     */
                    final SimpleDateFormat formatter = new SimpleDateFormat(
                            "dd-MMM-yyyy");
                    final SimpleDateFormat df = new SimpleDateFormat(
                            "yyyy-MM-dd");

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
                            "yyyy");

                    Integer i = Integer.parseInt(formatter1.format(d2))
                            - Integer.parseInt(formatter1.format(d1));
                    String str = String.valueOf(i);

                    if (str.contains("-")) {
                        // errordateselect();
                        Toast.makeText(con,
                                "From Date is Not Greater Than To Date..",
                                Toast.LENGTH_LONG).show();
                    } else {
                        final SimpleDateFormat formatter2 = new SimpleDateFormat(
                                "MM");

                        Integer ii = Integer.parseInt(formatter2.format(d2))
                                - Integer.parseInt(formatter2.format(d1));
                        String strm = String.valueOf(ii);

                        if (strm.contains("-")) {
                            // errordateselect();
                            Toast.makeText(con,
                                    "From Date is Not Greater Than To Date..",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            final SimpleDateFormat formatter3 = new SimpleDateFormat(
                                    "dd");

                            Integer i3 = Integer.parseInt(formatter3.format(d2))
                                    - Integer.parseInt(formatter3.format(d1));
                            String strm3 = String.valueOf(i3);

                            if (strm3.contains("-")) {
                                // errordateselect();
                                Toast.makeText(
                                        con,
                                        "From Date is Not Greater Than To Date..",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Cursor c1 = db
                                        .GetDataBetweenTowHODate(strfromdate,
                                                strtodate, strCIFBDMUserId);

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
                                        clsHOLead objClshoLead = new clsHOLead(
                                                strLeadDate, strCustId,
                                                strCustName, strLeadPriority,
                                                strLeadStatus,
                                                strLeadSubStatus,
                                                strProposalNo, strFollowupdate,
                                                strComments, strAge,
                                                strTotalAcc, strBalance,
                                                strBranchCode, strCIFBDMUserId,
                                                strSyncStatus, strLeadID,
                                                strBDMName, strSource);

                                        String strdate1 = c1.getString(c1
                                                .getColumnIndex("HOLeadDate"));

                                        Date dt1 = null;
                                        try {
                                            dt1 = df.parse(strdate1);
                                        } catch (ParseException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                        strdate1 = dateFormat.format(dt1);

                                        String strfdate = c1.getString(c1
                                                .getColumnIndex("HOLeadFollowUpDate"));

                                        if (!strfdate.equalsIgnoreCase("")) {
                                            Date dt2 = null;
                                            try {
                                                dt2 = df.parse(strfdate);
                                            } catch (ParseException e) {
                                                // TODO Auto-generated catch
                                                // block
                                                e.printStackTrace();
                                            }
                                            strfdate = dateFormat.format(dt2);
                                        }

                                        objClshoLead.set_date(strdate1);
                                        objClshoLead.set_custid(c1.getString(c1
                                                .getColumnIndex("HOLeadCustomerId")));
                                        objClshoLead.set_custname(c1.getString(c1
                                                .getColumnIndex("HOLeadCustomerName")));
                                        objClshoLead.set_priority(c1.getString(c1
                                                .getColumnIndex("HOLeadPriority")));
                                        objClshoLead.set_status(c1.getString(c1
                                                .getColumnIndex("HOLeadStatus")));
                                        objClshoLead.set_substatus(c1.getString(c1
                                                .getColumnIndex("HOLeadSubStatus")));
                                        objClshoLead.set_proposalno(c1.getString(c1
                                                .getColumnIndex("HOLeadProposalNo")));
                                        // objClshoLead.set_followupdate(c1.getString(c1.getColumnIndex("HOLeadFollowUpDate")));
                                        objClshoLead.set_followupdate(strfdate);
                                        objClshoLead.set_comments(c1.getString(c1
                                                .getColumnIndex("HOLeadComments")));
                                        objClshoLead.set_age(c1.getString(c1
                                                .getColumnIndex("HOLeadAge")));
                                        objClshoLead.set_totalacc(c1.getString(c1
                                                .getColumnIndex("HOLeadTotalAcc")));
                                        objClshoLead.set_balance(c1.getString(c1
                                                .getColumnIndex("HOLeadBalance")));
                                        objClshoLead.set_branchcode(c1.getString(c1
                                                .getColumnIndex("HOLeadBranchCode")));
                                        objClshoLead.set_sync(c1.getString(c1
                                                .getColumnIndex("HOLeadSync")));
                                        objClshoLead.set_userid(c1.getString(c1
                                                .getColumnIndex("HOLeadUserID")));
                                        objClshoLead.set_name(c1.getString(c1
                                                .getColumnIndex("HOLeadBDMName")));
                                        objClshoLead.set_leadid(c1.getString(c1
                                                .getColumnIndex("HOLeadServerID")));
                                        lsthoLead.add(objClshoLead);
                                        c1.moveToNext();
                                    }
                                }

                                txtholeadcount.setText("Total Lead : "
                                        + lsthoLead.size());

                                if (lsthoLead.size() > 0) {
                                    ItemsAdapter adapter = new ItemsAdapter(
                                            con, lsthoLead);
                                    lvholead.setAdapter(adapter);
                                    // Utility.setListViewHeightBasedOnChildren(lvholead);

                                } else {
                                    ArrayList<clsHOLead> lsthoLead = new ArrayList<clsHOLead>();

                                    ItemsAdapter adapter = new ItemsAdapter(
                                            con, lsthoLead);
                                    lvholead.setAdapter(adapter);
                                    // Utility.setListViewHeightBasedOnChildren(lvholead);
                                }
                            }

                        }
                    }
                }

            }
        });

        btnsrsearchhofoldate.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                String strFromDate = edsrhofolfdate.getText().toString();
                String strToDate = edsrhofoltdate.getText().toString();
                if (strFromDate.equalsIgnoreCase("")
                        || strToDate.equalsIgnoreCase("")) {
                    // validation();
                    Toast.makeText(con, "All Fields Required..",
                            Toast.LENGTH_LONG).show();
                } else {
                    // final SimpleDateFormat formatter = new
                    // SimpleDateFormat("dd-MMMM-yyyy");
                    /*
                     * final SimpleDateFormat formatter = new SimpleDateFormat(
                     * "dd-MMMM-yyyy");
                     */
                    final SimpleDateFormat formatter = new SimpleDateFormat(
                            "dd-MMM-yyyy");
                    final SimpleDateFormat df = new SimpleDateFormat(
                            "yyyy-MM-dd");

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
                            "yyyy");

                    Integer i = Integer.parseInt(formatter1.format(d2))
                            - Integer.parseInt(formatter1.format(d1));
                    String str = String.valueOf(i);

                    if (str.contains("-")) {
                        // errordateselect();
                        Toast.makeText(con,
                                "From Date is Not Greater Than To Date..",
                                Toast.LENGTH_LONG).show();
                    } else {
                        final SimpleDateFormat formatter2 = new SimpleDateFormat(
                                "MM");

                        Integer ii = Integer.parseInt(formatter2.format(d2))
                                - Integer.parseInt(formatter2.format(d1));
                        String strm = String.valueOf(ii);

                        if (strm.contains("-")) {
                            // errordateselect();
                            Toast.makeText(con,
                                    "From Date is Not Greater Than To Date..",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            final SimpleDateFormat formatter3 = new SimpleDateFormat(
                                    "dd");

                            Integer i3 = Integer.parseInt(formatter3.format(d2))
                                    - Integer.parseInt(formatter3.format(d1));
                            String strm3 = String.valueOf(i3);

                            if (strm3.contains("-")) {
                                // errordateselect();
                                Toast.makeText(
                                        con,
                                        "From Date is Not Greater Than To Date..",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Cursor c1 = db
                                        .GetDataBetweenTowFollowUpDate(
                                                strfromdate, strtodate,
                                                strCIFBDMUserId);

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
                                        clsHOLead objClshoLead = new clsHOLead(
                                                strLeadDate, strCustId,
                                                strCustName, strLeadPriority,
                                                strLeadStatus,
                                                strLeadSubStatus,
                                                strProposalNo, strFollowupdate,
                                                strComments, strAge,
                                                strTotalAcc, strBalance,
                                                strBranchCode, strCIFBDMUserId,
                                                strSyncStatus, strLeadID,
                                                strBDMName, strSource);

                                        String strdate2 = c1.getString(c1
                                                .getColumnIndex("HOLeadDate"));

                                        Date dt2 = null;
                                        try {
                                            dt2 = df.parse(strdate2);
                                        } catch (ParseException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                        strdate2 = dateFormat.format(dt2);

                                        String strdate1 = c1.getString(c1
                                                .getColumnIndex("HOLeadFollowUpDate"));

                                        if (!strdate1.equalsIgnoreCase("")) {

                                            Date dt1 = null;
                                            try {
                                                dt1 = df.parse(strdate1);
                                            } catch (ParseException e) {
                                                // TODO Auto-generated catch
                                                // block
                                                e.printStackTrace();
                                            }
                                            strdate1 = dateFormat.format(dt1);

                                        }

                                        // objClshoLead.set_date(c1.getString(c1.getColumnIndex("HOLeadDate")));
                                        objClshoLead.set_date(strdate2);
                                        objClshoLead.set_custid(c1.getString(c1
                                                .getColumnIndex("HOLeadCustomerId")));
                                        objClshoLead.set_custname(c1.getString(c1
                                                .getColumnIndex("HOLeadCustomerName")));
                                        objClshoLead.set_priority(c1.getString(c1
                                                .getColumnIndex("HOLeadPriority")));
                                        objClshoLead.set_status(c1.getString(c1
                                                .getColumnIndex("HOLeadStatus")));
                                        objClshoLead.set_substatus(c1.getString(c1
                                                .getColumnIndex("HOLeadSubStatus")));
                                        objClshoLead.set_proposalno(c1.getString(c1
                                                .getColumnIndex("HOLeadProposalNo")));
                                        objClshoLead.set_followupdate(strdate1);
                                        objClshoLead.set_comments(c1.getString(c1
                                                .getColumnIndex("HOLeadComments")));
                                        objClshoLead.set_age(c1.getString(c1
                                                .getColumnIndex("HOLeadAge")));
                                        objClshoLead.set_totalacc(c1.getString(c1
                                                .getColumnIndex("HOLeadTotalAcc")));
                                        objClshoLead.set_balance(c1.getString(c1
                                                .getColumnIndex("HOLeadBalance")));
                                        objClshoLead.set_branchcode(c1.getString(c1
                                                .getColumnIndex("HOLeadBranchCode")));
                                        objClshoLead.set_sync(c1.getString(c1
                                                .getColumnIndex("HOLeadSync")));
                                        objClshoLead.set_userid(c1.getString(c1
                                                .getColumnIndex("HOLeadUserID")));
                                        objClshoLead.set_name(c1.getString(c1
                                                .getColumnIndex("HOLeadBDMName")));
                                        objClshoLead.set_leadid(c1.getString(c1
                                                .getColumnIndex("HOLeadServerID")));
                                        lsthoLead.add(objClshoLead);
                                        c1.moveToNext();
                                    }
                                }

                                txtholeadcount.setText("Total Lead : "
                                        + lsthoLead.size());

                                if (lsthoLead.size() > 0) {
                                    ItemsAdapter adapter = new ItemsAdapter(
                                            con, lsthoLead);
                                    lvholead.setAdapter(adapter);
                                    // Utility.setListViewHeightBasedOnChildren(lvholead);

                                } else {
                                    ArrayList<clsHOLead> lsthoLead = new ArrayList<clsHOLead>();

                                    ItemsAdapter adapter = new ItemsAdapter(
                                            con, lsthoLead);
                                    lvholead.setAdapter(adapter);
                                    // Utility.setListViewHeightBasedOnChildren(lvholead);
                                }
                            }

                        }
                    }
                }
            }
        });

        btnsrsearchholeadpr.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                String strPriority = spsrholeadpriority.getSelectedItem()
                        .toString();

                Cursor c1 = db.GetAllLead_BasedOnPriority(strCIFBDMUserId,
                        strPriority);

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
                        clsHOLead objClshoLead = new clsHOLead(strLeadDate,
                                strCustId, strCustName, strLeadPriority,
                                strLeadStatus, strLeadSubStatus, strProposalNo,
                                strFollowupdate, strComments, strAge,
                                strTotalAcc, strBalance, strBranchCode,
                                strCIFBDMUserId, strSyncStatus, strLeadID,
                                strBDMName, strSource);
                        // objClshoLead.set_date(c1.getString(c1.getColumnIndex("HOLeadDate")));

                        String strfromdate = c1.getString(c1
                                .getColumnIndex("HOLeadDate"));

                        Date dt1 = null;
                        try {
                            dt1 = df.parse(strfromdate);
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        strfromdate = dateFormat.format(dt1);

                        String strfdate = c1.getString(c1
                                .getColumnIndex("HOLeadFollowUpDate"));
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

                        objClshoLead.set_custid(c1.getString(c1
                                .getColumnIndex("HOLeadCustomerId")));
                        objClshoLead.set_custname(c1.getString(c1
                                .getColumnIndex("HOLeadCustomerName")));
                        objClshoLead.set_priority(c1.getString(c1
                                .getColumnIndex("HOLeadPriority")));
                        objClshoLead.set_status(c1.getString(c1
                                .getColumnIndex("HOLeadStatus")));
                        objClshoLead.set_substatus(c1.getString(c1
                                .getColumnIndex("HOLeadSubStatus")));
                        objClshoLead.set_proposalno(c1.getString(c1
                                .getColumnIndex("HOLeadProposalNo")));
                        // objClshoLead.set_followupdate(c1.getString(c1.getColumnIndex("HOLeadFollowUpDate")));
                        objClshoLead.set_followupdate(strfdate);
                        objClshoLead.set_comments(c1.getString(c1
                                .getColumnIndex("HOLeadComments")));
                        objClshoLead.set_age(c1.getString(c1
                                .getColumnIndex("HOLeadAge")));
                        objClshoLead.set_totalacc(c1.getString(c1
                                .getColumnIndex("HOLeadTotalAcc")));
                        objClshoLead.set_balance(c1.getString(c1
                                .getColumnIndex("HOLeadBalance")));
                        objClshoLead.set_branchcode(c1.getString(c1
                                .getColumnIndex("HOLeadBranchCode")));
                        objClshoLead.set_sync(c1.getString(c1
                                .getColumnIndex("HOLeadSync")));
                        objClshoLead.set_userid(c1.getString(c1
                                .getColumnIndex("HOLeadUserID")));
                        objClshoLead.set_name(c1.getString(c1
                                .getColumnIndex("HOLeadBDMName")));
                        objClshoLead.set_leadid(c1.getString(c1
                                .getColumnIndex("HOLeadServerID")));

                        lsthoLead.add(objClshoLead);
                        c1.moveToNext();
                    }
                }

                txtholeadcount.setText("Total Lead : " + lsthoLead.size());

                if (lsthoLead.size() > 0) {
                    ItemsAdapter adapter = new ItemsAdapter(con, lsthoLead);
                    lvholead.setAdapter(adapter);
                    // Utility.setListViewHeightBasedOnChildren(lvholead);

                } else {
                    ArrayList<clsHOLead> lsthoLead = new ArrayList<clsHOLead>();

                    ItemsAdapter adapter = new ItemsAdapter(con, lsthoLead);
                    lvholead.setAdapter(adapter);
                    // Utility.setListViewHeightBasedOnChildren(lvholead);
                }
            }
        });

        btnsrsearchholeadstatus.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                String strStatus = spsrholeadstatus.getSelectedItem()
                        .toString();

                Cursor c1 = db.GetAllLead_BasedOnStatus(strCIFBDMUserId,
                        strStatus);

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
                        clsHOLead objClshoLead = new clsHOLead(strLeadDate,
                                strCustId, strCustName, strLeadPriority,
                                strLeadStatus, strLeadSubStatus, strProposalNo,
                                strFollowupdate, strComments, strAge,
                                strTotalAcc, strBalance, strBranchCode,
                                strCIFBDMUserId, strSyncStatus, strLeadID,
                                strBDMName, strSource);
                        // objClshoLead.set_date(c1.getString(c1.getColumnIndex("HOLeadDate")));
                        String strfromdate = c1.getString(c1
                                .getColumnIndex("HOLeadDate"));

                        Date dt1 = null;
                        try {
                            dt1 = df.parse(strfromdate);
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        strfromdate = dateFormat.format(dt1);

                        String strfdate = c1.getString(c1
                                .getColumnIndex("HOLeadFollowUpDate"));
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
                        objClshoLead.set_custid(c1.getString(c1
                                .getColumnIndex("HOLeadCustomerId")));
                        objClshoLead.set_custname(c1.getString(c1
                                .getColumnIndex("HOLeadCustomerName")));
                        objClshoLead.set_priority(c1.getString(c1
                                .getColumnIndex("HOLeadPriority")));
                        objClshoLead.set_status(c1.getString(c1
                                .getColumnIndex("HOLeadStatus")));
                        objClshoLead.set_substatus(c1.getString(c1
                                .getColumnIndex("HOLeadSubStatus")));
                        objClshoLead.set_proposalno(c1.getString(c1
                                .getColumnIndex("HOLeadProposalNo")));
                        // objClshoLead.set_followupdate(c1.getString(c1.getColumnIndex("HOLeadFollowUpDate")));
                        objClshoLead.set_followupdate(strfdate);
                        objClshoLead.set_comments(c1.getString(c1
                                .getColumnIndex("HOLeadComments")));
                        objClshoLead.set_age(c1.getString(c1
                                .getColumnIndex("HOLeadAge")));
                        objClshoLead.set_totalacc(c1.getString(c1
                                .getColumnIndex("HOLeadTotalAcc")));
                        objClshoLead.set_balance(c1.getString(c1
                                .getColumnIndex("HOLeadBalance")));
                        objClshoLead.set_branchcode(c1.getString(c1
                                .getColumnIndex("HOLeadBranchCode")));
                        objClshoLead.set_sync(c1.getString(c1
                                .getColumnIndex("HOLeadSync")));
                        objClshoLead.set_userid(c1.getString(c1
                                .getColumnIndex("HOLeadUserID")));
                        objClshoLead.set_name(c1.getString(c1
                                .getColumnIndex("HOLeadBDMName")));
                        objClshoLead.set_leadid(c1.getString(c1
                                .getColumnIndex("HOLeadServerID")));

                        lsthoLead.add(objClshoLead);
                        c1.moveToNext();
                    }
                }

                txtholeadcount.setText("Total Lead : " + lsthoLead.size());

                if (lsthoLead.size() > 0) {
                    ItemsAdapter adapter = new ItemsAdapter(con, lsthoLead);
                    lvholead.setAdapter(adapter);
                    // Utility.setListViewHeightBasedOnChildren(lvholead);

                } else {

                    ItemsAdapter adapter = new ItemsAdapter(con, lsthoLead);
                    lvholead.setAdapter(adapter);
                    // Utility.setListViewHeightBasedOnChildren(lvholead);
                }
            }
        });

        btnsrsearchholeadno.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                String strLeadNo = edsrsearchholeadno.getText().toString();

                if (strLeadNo.equalsIgnoreCase("")) {
                    Toast.makeText(con, "Enter Lead No..", Toast.LENGTH_LONG)
                            .show();
                } else {

                    Cursor c1 = db.GetAllLead_BasedOnLeadNo(strCIFBDMUserId,
                            strLeadNo);

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
                            clsHOLead objClshoLead = new clsHOLead(strLeadDate,
                                    strCustId, strCustName, strLeadPriority,
                                    strLeadStatus, strLeadSubStatus,
                                    strProposalNo, strFollowupdate,
                                    strComments, strAge, strTotalAcc,
                                    strBalance, strBranchCode, strCIFBDMUserId,
                                    strSyncStatus, strLeadID, strBDMName,
                                    strSource);
                            // objClshoLead.set_date(c1.getString(c1.getColumnIndex("HOLeadDate")));
                            String strfromdate = c1.getString(c1
                                    .getColumnIndex("HOLeadDate"));

                            Date dt1 = null;
                            try {
                                dt1 = df.parse(strfromdate);
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            strfromdate = dateFormat.format(dt1);

                            String strfdate = c1.getString(c1
                                    .getColumnIndex("HOLeadFollowUpDate"));
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
                            objClshoLead.set_custid(c1.getString(c1
                                    .getColumnIndex("HOLeadCustomerId")));
                            objClshoLead.set_custname(c1.getString(c1
                                    .getColumnIndex("HOLeadCustomerName")));
                            objClshoLead.set_priority(c1.getString(c1
                                    .getColumnIndex("HOLeadPriority")));
                            objClshoLead.set_status(c1.getString(c1
                                    .getColumnIndex("HOLeadStatus")));
                            objClshoLead.set_substatus(c1.getString(c1
                                    .getColumnIndex("HOLeadSubStatus")));
                            objClshoLead.set_proposalno(c1.getString(c1
                                    .getColumnIndex("HOLeadProposalNo")));
                            // objClshoLead.set_followupdate(c1.getString(c1.getColumnIndex("HOLeadFollowUpDate")));
                            objClshoLead.set_followupdate(strfdate);
                            objClshoLead.set_comments(c1.getString(c1
                                    .getColumnIndex("HOLeadComments")));
                            objClshoLead.set_age(c1.getString(c1
                                    .getColumnIndex("HOLeadAge")));
                            objClshoLead.set_totalacc(c1.getString(c1
                                    .getColumnIndex("HOLeadTotalAcc")));
                            objClshoLead.set_balance(c1.getString(c1
                                    .getColumnIndex("HOLeadBalance")));
                            objClshoLead.set_branchcode(c1.getString(c1
                                    .getColumnIndex("HOLeadBranchCode")));
                            objClshoLead.set_sync(c1.getString(c1
                                    .getColumnIndex("HOLeadSync")));
                            objClshoLead.set_userid(c1.getString(c1
                                    .getColumnIndex("HOLeadUserID")));
                            objClshoLead.set_name(c1.getString(c1
                                    .getColumnIndex("HOLeadBDMName")));
                            objClshoLead.set_leadid(c1.getString(c1
                                    .getColumnIndex("HOLeadServerID")));

                            lsthoLead.add(objClshoLead);
                            c1.moveToNext();
                        }
                    }

                    txtholeadcount.setText("Total Lead : " + lsthoLead.size());

                    if (lsthoLead.size() > 0) {
                        ItemsAdapter adapter = new ItemsAdapter(con, lsthoLead);
                        lvholead.setAdapter(adapter);
                        // Utility.setListViewHeightBasedOnChildren(lvholead);

                    } else {

                        ItemsAdapter adapter = new ItemsAdapter(con, lsthoLead);
                        lvholead.setAdapter(adapter);
                        // Utility.setListViewHeightBasedOnChildren(lvholead);
                    }
                }
            }
        });

        GetAllLead();

        if (mCommonMethods.isNetworkConnected(con)) {
            startdownloadLead();
        } else {
            // intereneterror();
            Toast.makeText(con, "Internet Connection Not Present,Try again..",
                    Toast.LENGTH_LONG).show();
        }

        lvholead.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub

                String strLeadDate = lsthoLead.get(position).get_date();
                String strCustId = lsthoLead.get(position).get_custid();
                String strCustName = lsthoLead.get(position).get_custname();
                String strLeadPriority = lsthoLead.get(position).get_priority();
                String strLeadStatus = lsthoLead.get(position).get_status();
                String strLeadSubStatus = lsthoLead.get(position)
                        .get_substatus();
                String strProposalNo = lsthoLead.get(position).get_proposalno();
                String strFollowupdate = lsthoLead.get(position)
                        .get_followupdate();
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

                ArrayList<String> lsteventa = new ArrayList<String>();
                lsteventa.clear();

                if (c2.getCount() > 0) {
                    c2.moveToFirst();
                    for (int ri = 0; ri < c2.getCount(); ri++) {
                        lsteventa.add(c2.getString(c2
                                .getColumnIndex("HOLeadSync")));
                        lsteventa.add(c2.getString(c2
                                .getColumnIndex("HOLeadID")));
                        c2.moveToNext();
                    }

                    String syncflag = lsteventa.get(0);
                    String rowid = lsteventa.get(1);

                    // if (syncflag.contentEquals("Open"))
                    // {
                    Intent i = new Intent(con, Lead_MgtHO.class);

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
                    i.putExtra("SyncFlag", syncflag);

                    startActivity(i);

                    // }
                    // else
                    // {

                    // if(!strLeadStatus.contentEquals("Converted"))
                    // {
                    /*
                     * Intent i = new Intent(con, Lead_MgtHO.class);
                     *
                     * i.putExtra("LeadDate", strLeadDate); i.putExtra("CustId",
                     * strCustId); i.putExtra("CustName", strCustName);
                     * i.putExtra("LeadPriority", strLeadPriority);
                     * i.putExtra("LeadStatus", strLeadStatus);
                     * i.putExtra("LeadSubStatus", strLeadSubStatus);
                     * i.putExtra("ProposalNo", strProposalNo);
                     * i.putExtra("Followupdate", strFollowupdate);
                     * i.putExtra("Comments", strComments); i.putExtra("Age",
                     * strAge); i.putExtra("TotalAcc", strTotalAcc);
                     * i.putExtra("Balance", strBalance);
                     * i.putExtra("BranchCode", strBranchCode);
                     * i.putExtra("UserID", strUserID);
                     * i.putExtra("LeadServerID", strLeadID);
                     * i.putExtra("RowID", rowid);
                     *
                     * startActivity(i);
                     */
                    // }
                    // else
                    // {
                    // activityeditalert();
                    // Toast.makeText(con, "Sync lead can not be edit..",
                    // Toast.LENGTH_LONG).show();
                    // }
                    // }

                }

            }

        });

        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

    }

    private void startdownloadLead() {
        taskLead.execute("demo");
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

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_LEADLIST);

                request.addProperty("strBdmcode", strCIFBDMUserId);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
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
                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "LeadList");

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
                                lst = new ArrayList<clsHOLead>();
                                lst.clear();

                                for (clsHOLead node : nodeData) {

                                    lst.add(node);
                                }

                                lstHOLeadCount = lst.size();

                                for (int i = 0; i < lst.size(); i++) {
                                    String strLeadDate = lst.get(i).get_date();
                                    String strCustId = lst.get(i).get_custid();
                                    String strCustName = lst.get(i)
                                            .get_custname();
                                    String strLeadPriority = lst.get(i)
                                            .get_priority();
                                    String strLeadStatus = lst.get(i)
                                            .get_status();
                                    String strLeadSubStatus = lst.get(i)
                                            .get_substatus();
                                    String strProposalNo = lst.get(i)
                                            .get_proposalno();
                                    String strFollowupdate = lst.get(i)
                                            .get_followupdate();
                                    String strComments = lst.get(i)
                                            .get_comments();
                                    String strAge = lst.get(i).get_age();
                                    String strTotalAcc = lst.get(i)
                                            .get_totalacc();
                                    String strBalance = lst.get(i)
                                            .get_balance();
                                    String strBranchCode = lst.get(i)
                                            .get_branchcode();
                                    String strCIFBDMUserId = lst.get(i)
                                            .get_userid();
                                    String strSyncStatus = "Open";
                                    String strLeadID = lst.get(i).get_leadid();
                                    String strBDMName = lst.get(i).get_name();
                                    String strSource = lst.get(i).get_source();

                                    int count = db.GetHOLeadServerId(strLeadID);

                                    if (count == 0) {

                                        if (strLeadStatus == null) {
                                            strLeadStatus = "";

                                        }

                                        if (strLeadStatus.equalsIgnoreCase("")
                                                || strLeadStatus
                                                .equalsIgnoreCase("Open")) {

                                            strLeadStatus = "Yet to Attend";

                                        }

                                        clsHOLead cls = new clsHOLead(strLeadDate,
                                                strCustId, strCustName,
                                                strLeadPriority, strLeadStatus,
                                                strLeadSubStatus, strProposalNo,
                                                strFollowupdate, strComments,
                                                strAge, strTotalAcc, strBalance,
                                                strBranchCode, strCIFBDMUserId,
                                                strSyncStatus, strLeadID,
                                                strBDMName, strSource);
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
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            if (running) {

                if (strLeadTag != null) {

                    if (strLeadErrorCode == null) {

                        txtholeadcount
                                .setText("Total Lead : " + lstHOLeadCount);

                        GetAllLead();

                    } else {
                        txtholeadcount.setText("Total Lead : " + 0);
                    }
                }
            } else {
                // syncerror();
                Toast.makeText(con, "Server Not Responding,Try again..",
                        Toast.LENGTH_LONG).show();
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
                clsHOLead objClshoLead = new clsHOLead(strLeadDate, strCustId,
                        strCustName, strLeadPriority, strLeadStatus,
                        strLeadSubStatus, strProposalNo, strFollowupdate,
                        strComments, strAge, strTotalAcc, strBalance,
                        strBranchCode, strCIFBDMUserId, strSyncStatus,
                        strLeadID, strBDMName, strSource);
                // objClshoLead.set_date(c1.getString(c1.getColumnIndex("HOLeadDate")));
                String strfromdate = c1.getString(c1
                        .getColumnIndex("HOLeadDate"));

                Date dt1 = null;
                try {
                    dt1 = df.parse(strfromdate);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                strfromdate = dateFormat.format(dt1);

                String strfdate = c1.getString(c1
                        .getColumnIndex("HOLeadFollowUpDate"));

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
                objClshoLead.set_custid(c1.getString(c1
                        .getColumnIndex("HOLeadCustomerId")));
                objClshoLead.set_custname(c1.getString(c1
                        .getColumnIndex("HOLeadCustomerName")));
                objClshoLead.set_priority(c1.getString(c1
                        .getColumnIndex("HOLeadPriority")));
                objClshoLead.set_status(c1.getString(c1
                        .getColumnIndex("HOLeadStatus")));
                objClshoLead.set_substatus(c1.getString(c1
                        .getColumnIndex("HOLeadSubStatus")));
                objClshoLead.set_proposalno(c1.getString(c1
                        .getColumnIndex("HOLeadProposalNo")));
                // objClshoLead.set_followupdate(c1.getString(c1.getColumnIndex("HOLeadFollowUpDate")));
                objClshoLead.set_followupdate(strfdate);
                objClshoLead.set_comments(c1.getString(c1
                        .getColumnIndex("HOLeadComments")));
                objClshoLead.set_age(c1.getString(c1
                        .getColumnIndex("HOLeadAge")));
                objClshoLead.set_totalacc(c1.getString(c1
                        .getColumnIndex("HOLeadTotalAcc")));
                objClshoLead.set_balance(c1.getString(c1
                        .getColumnIndex("HOLeadBalance")));
                objClshoLead.set_branchcode(c1.getString(c1
                        .getColumnIndex("HOLeadBranchCode")));
                objClshoLead.set_sync(c1.getString(c1
                        .getColumnIndex("HOLeadSync")));
                objClshoLead.set_name(c1.getString(c1
                        .getColumnIndex("HOLeadSync")));
                objClshoLead.set_userid(c1.getString(c1
                        .getColumnIndex("HOLeadUserID")));
                objClshoLead.set_name(c1.getString(c1
                        .getColumnIndex("HOLeadBDMName")));
                objClshoLead.set_leadid(c1.getString(c1
                        .getColumnIndex("HOLeadServerID")));

                lsthoLead.add(objClshoLead);
                c1.moveToNext();
            }
        }

        txtholeadcount.setText("Total Lead : " + lsthoLead.size());

        if (lsthoLead.size() > 0) {
            ItemsAdapter adapter = new ItemsAdapter(con, lsthoLead);
            lvholead.setAdapter(adapter);
            // Utility.setListViewHeightBasedOnChildren(lvholead);

            tblsearchholead.setVisibility(View.VISIBLE);

        } else {

            ItemsAdapter adapter = new ItemsAdapter(con, lsthoLead);
            lvholead.setAdapter(adapter);
            // Utility.setListViewHeightBasedOnChildren(lvholead);

            tblsearchholead.setVisibility(View.GONE);
        }

    }

    private class ItemsAdapter extends BaseAdapter {
        ArrayList<clsHOLead> items;

        ItemsAdapter(Context context, ArrayList<clsHOLead> items) {
            this.items = items;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {

            TextView date;
            TextView leadno;
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
            leadno = view.findViewById(R.id.txtholeadno);
            custid = view.findViewById(R.id.txtholeadcustid);
            custname = view.findViewById(R.id.txtholeadcustname);
            priority = view.findViewById(R.id.txtholeadpriority);
            status = view.findViewById(R.id.txtholeadstatus);
            substatus = view.findViewById(R.id.txtholeadsubstatus);
            propno = view.findViewById(R.id.txtholeadproposalno);
            followdate = view
                    .findViewById(R.id.txtholeadfollowupdate);
            comments = view.findViewById(R.id.txtholeadcomnts);
            age = view.findViewById(R.id.txtholeadage);
            totalacc = view.findViewById(R.id.txtholeadtotalacc);
            balance = view.findViewById(R.id.txtholeadbalance);
            brcode = view.findViewById(R.id.txtholeadbrcode);
            sync = view.findViewById(R.id.txtholeadsync);
            code = view.findViewById(R.id.txtholeadbdmcode);
            name = view.findViewById(R.id.txtholeadbdmname);

            date.setText(items.get(position).get_date());
            leadno.setText(items.get(position).get_leadid());
            custid.setText(items.get(position).get_custid());
            custname.setText(items.get(position).get_custname());
            priority.setText(items.get(position).get_priority());
            status.setText(items.get(position).get_status());
            substatus.setText(items.get(position).get_substatus());
            propno.setText(items.get(position).get_proposalno());
            followdate.setText(items.get(position).get_followupdate());
            comments.setText(items.get(position).get_comments());
            age.setText(items.get(position).get_age());
            totalacc.setText(items.get(position).get_totalacc());
            balance.setText(items.get(position).get_balance());
            brcode.setText(items.get(position).get_branchcode());
            sync.setText(items.get(position).get_sync());
            code.setText(strCIFBDMUserId);
            name.setText(items.get(position).get_name());

            return view;
        }

        public int getCount() {
            return items.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

    }

    private void updateDisplay(int year, int month, int day) {
        y = String.valueOf(year);
        m = String.valueOf(month + 1);
        d = String.valueOf(day);
        String da = String.valueOf(day);
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

        // String totaldate = da + "-" + m + "-" + y;
        String totaldate = da + "-" + m.substring(0, 3) + "-" + y;

        if (datecheck == 3) {
            edsrholeadfdate.setText(totaldate);
        } else if (datecheck == 4) {
            edsrholeadtdate.setText(totaldate);
        } else if (datecheck == 5) {
            edsrhofolfdate.setText(totaldate);
        } else if (datecheck == 6) {
            edsrhofoltdate.setText(totaldate);
        }

    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
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

            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(con, ProgressDialog.THEME_HOLO_LIGHT);
                String Message = "Loading. Please wait...";
                mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(true);

                mProgressDialog.setButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                taskLead.cancel(true);

                                mProgressDialog.dismiss();
                            }
                        });

                mProgressDialog.setMax(100);
                mProgressDialog.show();
                return mProgressDialog;

            case DATE_DIALOG_ID:

                DatePickerDialog dialog = new DatePickerDialog(con,
                        mDateSetListener, mYear, mMonth, mDay);
                dialog.show();

                return dialog;

        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        GetAllLead();
    }


    public String GetUserType() {
        String strUserType = "";
        try {
            strUserType = SimpleCrypto.decrypt("SBIL", db.GetUserType());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return strUserType;
    }
}