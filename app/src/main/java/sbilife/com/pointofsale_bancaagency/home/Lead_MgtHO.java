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
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.service.ScheduleClient;

@SuppressWarnings("deprecation")
public class Lead_MgtHO extends AppCompatActivity {

    private DownloadPushLead taskPushLead;

    private final int DIALOG_DOWNLOAD_PROGRESS = 1;
    private ProgressDialog mProgressDialog;

    private String strLeadSubStatus = "";
    private String strcAge = "";
    private String strUserID = "";
    private String strLeadID = "";
    private String strRowId = "";
    private String strSyncFlag = "";

    private String strLeadErrorCode = "";

    private String strCIFBDMUserId = "";
    private String strCIFBDMEmailId = "";
    private String strCIFBDMPassword = "";
    private String strCIFBDMMObileNo = "";

    private Spinner selholeadstatus;
    private Spinner selholeadsubstatus;
    private String strholeadstatus;

    private TableRow tblhofollowupdate;
    private TableRow tblholeadsubstatus;
    private TableRow tblhoproposalno;

    private EditText edholeaddate;
    private EditText edhocustid;
    private EditText edhocustname;
    private EditText edholeadpriority;
    private EditText edhoproposalno;
    private EditText edhocomments;
    private EditText edhototalacc;
    private EditText edhobalance;
    private EditText edhobrcode;
    private TextView edhofolowdate;

    private CheckBox chkholead;

    ArrayList<clsHOLead> lsthoLead = new ArrayList<>();

    private int intday;
    private int intmonth;
    private int intyear;

    private ScheduleClient scheduleClient;

    private int mYear;
    private int mMonth;
    private int mDay;

    private final int DATE_DIALOG_ID = 0;

    private final Context con = this;

    private DatabaseHelper db;

    // final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    private String stredProposalNo = "";
    private CommonMethods mCommonMethods;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.holead);

        db = new DatabaseHelper(con);
        mCommonMethods = new CommonMethods();

        mProgressDialog = new ProgressDialog(con);
        taskPushLead = new DownloadPushLead();

        try {
            strCIFBDMUserId = SimpleCrypto.decrypt("SBIL", db.GetCIFNo());
            strCIFBDMEmailId = SimpleCrypto
                    .decrypt("SBIL", db.GetEmailId());
            strCIFBDMPassword = db.GetPassword();
            strCIFBDMMObileNo = SimpleCrypto.decrypt("SBIL",
                    db.GetMobileNo());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        selholeadstatus = findViewById(R.id.selholeadstatus);
        selholeadsubstatus = findViewById(R.id.selholeadsubstatus);

        edholeaddate = findViewById(R.id.edholeaddate);
        edhocustid = findViewById(R.id.edhocustid);
        edhocustname = findViewById(R.id.edhocustname);
        edhoproposalno = findViewById(R.id.edhoproposalno);
        edhofolowdate = findViewById(R.id.edhofolowdate);
        edhocomments = findViewById(R.id.edhocomments);
        EditText edhoage = findViewById(R.id.edhoage);
        edhobrcode = findViewById(R.id.edhobrcode);
        edholeadpriority = findViewById(R.id.edholeadpriority);
        edhototalacc = findViewById(R.id.edhototalacc);
        edhobalance = findViewById(R.id.edhobalance);

        // Create a new service client and bind our activity to this service
        scheduleClient = new ScheduleClient(con);
        scheduleClient.doBindService();

        chkholead = findViewById(R.id.chkholead);

        chkholead.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

                String fulldate = edhofolowdate.getText().toString();

                if (fulldate.equalsIgnoreCase("")) {
                    if (arg1) {
                        Toast.makeText(con, "Please Select Date First..",
                                Toast.LENGTH_LONG).show();
                        chkholead.setChecked(false);
                    }
                } else {
                    String[] spl = fulldate.split("-");

                    String a = spl[0];
                    String b = spl[1];
                    String c = spl[2];

                    /*
                     * if (b.contentEquals("January")) { b = "0"; } else if
                     * (b.contentEquals("February")) { b = "1"; } else if
                     * (b.contentEquals("March")) { b = "2"; } else if
                     * (b.contentEquals("April")) { b = "3"; } else if
                     * (b.contentEquals("May")) { b = "4"; } else if
                     * (b.contentEquals("June")) { b = "5"; } else if
                     * (b.contentEquals("July")) { b = "6"; } else if
                     * (b.contentEquals("August")) { b = "7"; } else if
                     * (b.contentEquals("September")) { b = "8"; } else if
                     * (b.contentEquals("October")) { b = "9"; } else if
                     * (b.contentEquals("November")) { b = "10"; } else if
                     * (b.contentEquals("December")) { b = "11"; }
                     */

                    if (b.contentEquals("Jan")) {
                        b = "0";
                    } else if (b.contentEquals("Feb")) {
                        b = "1";
                    } else if (b.contentEquals("Mar")) {
                        b = "2";
                    } else if (b.contentEquals("Apr")) {
                        b = "3";
                    } else if (b.contentEquals("May")) {
                        b = "4";
                    } else if (b.contentEquals("Jun")) {
                        b = "5";
                    } else if (b.contentEquals("Jul")) {
                        b = "6";
                    } else if (b.contentEquals("Aug")) {
                        b = "7";
                    } else if (b.contentEquals("Sep")) {
                        b = "8";
                    } else if (b.contentEquals("Oct")) {
                        b = "9";
                    } else if (b.contentEquals("Nov")) {
                        b = "10";
                    } else if (b.contentEquals("Dec")) {
                        b = "11";
                    }

                    // Get the date from our datepicker
                    intday = Integer.parseInt(a);
                    intmonth = Integer.parseInt(b);
                    intyear = Integer.parseInt(c);
                }
            }
        });

        tblhofollowupdate = findViewById(R.id.tblhofollowupdate);
        tblholeadsubstatus = findViewById(R.id.tblholeadsubstatus);
        tblhoproposalno = findViewById(R.id.tblhoproposalno);

        Button btnaddholead = findViewById(R.id.btnaddholead);
        Button btncancelholead = findViewById(R.id.btncancelholead);

        Intent i = getIntent();
        String strLeadDate = i.getStringExtra("LeadDate");
        String strCustId = i.getStringExtra("CustId");
        String strCustName = i.getStringExtra("CustName");
        String strLeadPriority = i.getStringExtra("LeadPriority");
        String strLeadStatus = i.getStringExtra("LeadStatus");
        strLeadSubStatus = i.getStringExtra("LeadSubStatus");
        String strProposalNo = i.getStringExtra("ProposalNo");
        String strFollowupdate = i.getStringExtra("Followupdate");
        String strComments = i.getStringExtra("Comments");
        strcAge = i.getStringExtra("Age");
        String strTotalAcc = i.getStringExtra("TotalAcc");
        String strBalance = i.getStringExtra("Balance");
        String strBranchCode = i.getStringExtra("BranchCode");
        strUserID = i.getStringExtra("UserID");
        strLeadID = i.getStringExtra("LeadServerID");
        strRowId = i.getStringExtra("RowID");
        strSyncFlag = i.getStringExtra("SyncFlag");

        edholeaddate.setText(strLeadDate);
        edhocustid.setText(strCustId);
        edhocustname.setText(strCustName);
        edholeadpriority.setText(strLeadPriority);
        edhoproposalno.setText(strProposalNo);
        edhofolowdate.setText(strFollowupdate);
        edhocomments.setText(strComments);

        double value = Double.parseDouble(strcAge);
        int ii = (int) value;
        String strvalue = String.valueOf(ii);

        edhoage.setText(strvalue);

        // edhoage.setText(strAge);
        edhototalacc.setText(strTotalAcc);
        edhobalance.setText(strBalance);
        edhobrcode.setText(strBranchCode);

        selholeadstatus.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {
                strholeadstatus = selholeadstatus.getSelectedItem().toString();
                if (strholeadstatus.contentEquals("Dropped")) {
                    tblholeadsubstatus.setVisibility(View.VISIBLE);
                } else {
                    tblholeadsubstatus.setVisibility(View.GONE);
                    tblhofollowupdate.setVisibility(View.GONE);
                }

                if (strholeadstatus.contentEquals("In Progress")
                        || strholeadstatus.contentEquals("On Hold")) {
                    tblhofollowupdate.setVisibility(View.VISIBLE);
                    chkholead.setVisibility(View.VISIBLE);
                } else {
                    tblhofollowupdate.setVisibility(View.GONE);
                    chkholead.setVisibility(View.GONE);
                }

                if (strholeadstatus.contentEquals("Converted")) {
                    tblhoproposalno.setVisibility(View.VISIBLE);
                } else {
                    tblhoproposalno.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        edhocomments.addTextChangedListener(new TextWatcher() {

            // @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub

                if (selholeadstatus.getSelectedItem().toString()
                        .contentEquals("Yet to Attend")) {
                    Toast.makeText(con, "Please Change Lead Status..",
                            Toast.LENGTH_SHORT).show();
                    edhocomments.getText().clear();
                }

            }

            // @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            // @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        btnaddholead.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                String strLeadDate = edholeaddate.getText().toString();
                String strCustId = edhocustid.getText().toString();
                String strCustName = edhocustname.getText().toString();
                String strLeadPriority = edholeadpriority.getText().toString();
                String strLeadStatus = selholeadstatus.getSelectedItem()
                        .toString();
                String strLeadSubStatus = selholeadsubstatus.getSelectedItem()
                        .toString();
                stredProposalNo = edhoproposalno.getText().toString();
                //String strFollowupdate = edhofolowdate.getText().toString();
                String strComments = edhocomments.getText().toString();
                // String strAge = edhoage.getText().toString();
                String strAge = strcAge;
                String strTotalAcc = edhototalacc.getText().toString();
                String strBalance = edhobalance.getText().toString();
                String strBranchCode = edhobrcode.getText().toString();

                if (strLeadDate.equalsIgnoreCase("")
                        || strCustId.equalsIgnoreCase("")
                        || strCustName.equalsIgnoreCase("")
                        || strLeadPriority.equalsIgnoreCase("")
                        || strLeadStatus.equalsIgnoreCase("")
                        || strAge.equalsIgnoreCase("")
                        || strTotalAcc.equalsIgnoreCase("")
                        || strBalance.equalsIgnoreCase("")
                        || strBranchCode.equalsIgnoreCase("")) {
                    Toast.makeText(con, "All Fields Required..",
                            Toast.LENGTH_LONG).show();
                } else {
                    if (strLeadStatus.contentEquals("Yet to Attend")) {
                        Toast.makeText(con, "Please Change Lead Status..",
                                Toast.LENGTH_LONG).show();
                    } else {

                        if (strLeadStatus.contentEquals("Dropped")) {
                            if (strLeadSubStatus.equalsIgnoreCase("")) {
                                Toast.makeText(con, "Enter Lead Sub Status..",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                /*
                                 * if(stredProposalNo.length() != 10) {
                                 * Toast.makeText(con,
                                 * "Enter Valid Proposal No..",
                                 * Toast.LENGTH_LONG).show(); } else {
                                 */
                                strLeadSubStatus = selholeadsubstatus
                                        .getSelectedItem().toString();

                                ArrayList<String> lstevent = new ArrayList<>();
                                lstevent.clear();

                                Cursor c = db.Get_HOLeadUpdateRowId(strUserID,
                                        strRowId);
                                if (c.getCount() > 0) {
                                    c.moveToFirst();
                                    for (int ii = 0; ii < c.getCount(); ii++) {
                                        lstevent.add(c.getString(c
                                                .getColumnIndex("HOLeadDate")));
                                        lstevent.add(c.getString(c
                                                .getColumnIndex("HOLeadCustomerId")));
                                        lstevent.add(c.getString(c
                                                .getColumnIndex("HOLeadCustomerName")));
                                        lstevent.add(c.getString(c
                                                .getColumnIndex("HOLeadPriority")));
                                        // lstevent.add(c.getString(c.getColumnIndex("HOLeadProposalNo")));
                                        lstevent.add(c.getString(c
                                                .getColumnIndex("HOLeadFollowUpDate")));
                                        lstevent.add(c.getString(c
                                                .getColumnIndex("HOLeadAge")));
                                        lstevent.add(c.getString(c
                                                .getColumnIndex("HOLeadTotalAcc")));
                                        lstevent.add(c.getString(c
                                                .getColumnIndex("HOLeadBalance")));
                                        lstevent.add(c.getString(c
                                                .getColumnIndex("HOLeadBranchCode")));
                                        lstevent.add(c.getString(c
                                                .getColumnIndex("HOLeadSync")));
                                        lstevent.add(c.getString(c
                                                .getColumnIndex("HOLeadServerID")));
                                        lstevent.add(c.getString(c
                                                .getColumnIndex("HOLeadBDMName")));
                                        lstevent.add(c.getString(c
                                                .getColumnIndex("HOLeadSource")));

                                        c.moveToNext();
                                    }

                                    clsHOLead ObhclsHOLead = new clsHOLead(
                                            lstevent.get(0),
                                            lstevent.get(1),
                                            lstevent.get(2),
                                            lstevent.get(3),
                                            strLeadStatus, strLeadSubStatus,
                                            stredProposalNo, lstevent.get(4), strComments,
                                            lstevent.get(5),
                                            lstevent.get(6),
                                            lstevent.get(7),
                                            lstevent.get(8),
                                            strUserID, lstevent.get(9), lstevent.get(
                                            10), lstevent
                                            .get(11),
                                            lstevent.get(12));
                                    db.UpdateHOLead(ObhclsHOLead, strRowId);

                                    // updatealert();
                                    // Toast.makeText(con,
                                    // "Save Successfully...",
                                    // Toast.LENGTH_LONG).show();

                                    if (mCommonMethods.isNetworkConnected(con)) {
                                        startupdateLead();
                                    } else {
                                        if (strSyncFlag.contentEquals("Close")) {
                                            clsHOLead ObhclsHOLeadS = new clsHOLead(
                                                    lstevent.get(0),
                                                    lstevent.get(1),
                                                    lstevent.get(2),
                                                    lstevent.get(3),
                                                    strLeadStatus,
                                                    strLeadSubStatus,
                                                    stredProposalNo,
                                                    lstevent.get(4),
                                                    strComments,
                                                    lstevent.get(5),
                                                    lstevent.get(6),
                                                    lstevent.get(7),
                                                    lstevent.get(8),
                                                    strUserID,
                                                    "Open",
                                                    lstevent.get(10),
                                                    lstevent.get(11),
                                                    lstevent.get(12));
                                            db.UpdateHOLead(ObhclsHOLeadS,
                                                    strRowId);
                                        }
                                        Toast.makeText(
                                                con,
                                                "Internet Connection Not Present,Try again..",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }

                                if (chkholead.isChecked()) {

                                    // Create a new calendar set to the date
                                    // chosen
                                    // we set the time to midnight (i.e. the
                                    // first minute of that day)
                                    Calendar ca = Calendar.getInstance();
                                    ca.set(intyear, intmonth, intday);
                                    ca.set(Calendar.HOUR_OF_DAY, 0);
                                    ca.set(Calendar.MINUTE, 0);
                                    ca.set(Calendar.SECOND, 0);
                                    // Ask our service to set an alarm for that
                                    // date, this activity talks to the client
                                    // that talks to the service
                                    scheduleClient.setAlarmForNotification(ca);
                                    // Notify the user what they just did
                                    // Toast.makeText(this,
                                    // "Notification set for: "+ day +"/"+
                                    // (month+1) +"/"+ year,
                                    // Toast.LENGTH_SHORT).show();

                                }

                                chkholead.setChecked(false);
                                // }
                            }
                        } else {

                            if (strLeadStatus.contentEquals("In Progress")
                                    || strLeadStatus.contentEquals("On Hold")) {

                                if (edhofolowdate.getText().toString()
                                        .equalsIgnoreCase("")) {

                                    Toast.makeText(con,
                                            "Please Select Follow up Date",
                                            Toast.LENGTH_LONG).show();
                                } else {

                                    // today date validation
                                    // final SimpleDateFormat formatter = new
                                    // SimpleDateFormat("dd-MMMM-yyyy");
                                    final SimpleDateFormat formatter = new SimpleDateFormat(
                                            "dd-MMM-yyyy");

                                    Calendar cala = Calendar.getInstance();
                                    int mYearc = cala.get(Calendar.YEAR);
                                    int mMonthc = cala.get(Calendar.MONTH);
                                    int mDayc = cala.get(Calendar.DAY_OF_MONTH);

                                    //String fyear = "";
                                    //	String lastd = "";

                                    String ya = String.valueOf(mYearc);
                                    String mo = String.valueOf(mMonthc + 1);
                                    //String d = String.valueOf(mDayc);
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

                                    // String totaldate = dat + "-" + mo + "-" +
                                    // ya;
                                    String totaldate = dat + "-"
                                            + mo.substring(0, 3) + "-" + ya;

                                    Date d1 = null;
                                    try {
                                        d1 = formatter.parse(edhofolowdate
                                                .getText().toString());
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

                                    // can not select future
                                    // if ((d2.after(d1))||(d2.equals(d1)))

                                    // can not select past
                                    if ((d1.after(d2)) || (d1.equals(d2))) {
                                        /*
                                         * if(stredProposalNo.length() != 10) {
                                         * Toast.makeText(con,
                                         * "Enter Valid Proposal No..",
                                         * Toast.LENGTH_LONG).show(); }
                                         */
                                        /*
                                         * else {
                                         */

                                        Date dt2 = null;
                                        try {
                                            dt2 = dateFormat
                                                    .parse(edhofolowdate
                                                            .getText()
                                                            .toString());
                                        } catch (ParseException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                        String strFollodate = df.format(dt2);

                                        strLeadSubStatus = "";

                                        ArrayList<String> lstevent = new ArrayList<>();
                                        lstevent.clear();

                                        Cursor c = db.Get_HOLeadUpdateRowId(
                                                strUserID, strRowId);
                                        if (c.getCount() > 0) {
                                            c.moveToFirst();
                                            for (int ii = 0; ii < c.getCount(); ii++) {
                                                lstevent.add(c.getString(c
                                                        .getColumnIndex("HOLeadDate")));
                                                lstevent.add(c.getString(c
                                                        .getColumnIndex("HOLeadCustomerId")));
                                                lstevent.add(c.getString(c
                                                        .getColumnIndex("HOLeadCustomerName")));
                                                lstevent.add(c.getString(c
                                                        .getColumnIndex("HOLeadPriority")));
                                                // lstevent.add(c.getString(c.getColumnIndex("HOLeadProposalNo")));
                                                // lstevent.add(c.getString(c.getColumnIndex("HOLeadFollowUpDate")));
                                                lstevent.add(c.getString(c
                                                        .getColumnIndex("HOLeadAge")));
                                                lstevent.add(c.getString(c
                                                        .getColumnIndex("HOLeadTotalAcc")));
                                                lstevent.add(c.getString(c
                                                        .getColumnIndex("HOLeadBalance")));
                                                lstevent.add(c.getString(c
                                                        .getColumnIndex("HOLeadBranchCode")));
                                                lstevent.add(c.getString(c
                                                        .getColumnIndex("HOLeadSync")));
                                                lstevent.add(c.getString(c
                                                        .getColumnIndex("HOLeadServerID")));
                                                lstevent.add(c.getString(c
                                                        .getColumnIndex("HOLeadBDMName")));
                                                lstevent.add(c.getString(c
                                                        .getColumnIndex("HOLeadSource")));

                                                c.moveToNext();
                                            }

                                            clsHOLead ObhclsHOLead = new clsHOLead(
                                                    lstevent.get(0),
                                                    lstevent.get(1),
                                                    lstevent.get(2),
                                                    lstevent.get(3),
                                                    strLeadStatus,
                                                    strLeadSubStatus,
                                                    stredProposalNo,
                                                    strFollodate,
                                                    strComments,
                                                    lstevent.get(4),
                                                    lstevent.get(5),
                                                    lstevent.get(6),
                                                    lstevent.get(7),
                                                    strUserID,
                                                    lstevent.get(8),
                                                    lstevent.get(9),
                                                    lstevent.get(10),
                                                    lstevent.get(11));
                                            db.UpdateHOLead(ObhclsHOLead,
                                                    strRowId);

                                            // updatealert();
                                            // Toast.makeText(con,
                                            // "Save Successfully...",
                                            // Toast.LENGTH_LONG).show();

                                            if (mCommonMethods.isNetworkConnected(con)) {
                                                startupdateLead();
                                            } else {
                                                if (strSyncFlag
                                                        .contentEquals("Close")) {
                                                    clsHOLead ObhclsHOLeadS = new clsHOLead(
                                                            lstevent.get(0),
                                                            lstevent.get(1),
                                                            lstevent.get(2),
                                                            lstevent.get(3),
                                                            strLeadStatus,
                                                            strLeadSubStatus,
                                                            stredProposalNo,
                                                            strFollodate,
                                                            strComments,
                                                            lstevent.get(4),
                                                            lstevent.get(5),
                                                            lstevent.get(6),
                                                            lstevent.get(7),
                                                            strUserID,
                                                            "Open",
                                                            lstevent.get(9),
                                                            lstevent.get(10),
                                                            lstevent.get(11));
                                                    db.UpdateHOLead(
                                                            ObhclsHOLeadS,
                                                            strRowId);
                                                }

                                                Toast.makeText(
                                                        con,
                                                        "Internet Connection Not Present,Try again..",
                                                        Toast.LENGTH_LONG)
                                                        .show();
                                            }

                                        }

                                        if (chkholead.isChecked()) {

                                            // Create a new calendar set to the
                                            // date
                                            // chosen
                                            // we set the time to midnight (i.e.
                                            // the
                                            // first minute of that day)
                                            Calendar ca = Calendar
                                                    .getInstance();
                                            ca.set(intyear, intmonth, intday);
                                            ca.set(Calendar.HOUR_OF_DAY, 0);
                                            ca.set(Calendar.MINUTE, 0);
                                            ca.set(Calendar.SECOND, 0);
                                            // Ask our service to set an alarm
                                            // for
                                            // that date, this activity talks to
                                            // the
                                            // client that talks to the service
                                            scheduleClient
                                                    .setAlarmForNotification(ca);
                                            // Notify the user what they just
                                            // did
                                            // Toast.makeText(this,
                                            // "Notification set for: "+ day
                                            // +"/"+
                                            // (month+1) +"/"+ year,
                                            // Toast.LENGTH_SHORT).show();

                                        }

                                        chkholead.setChecked(false);
                                        // }
                                    } else {
                                        dateselecterror();
                                    }
                                }
                            } else if (strLeadStatus.contentEquals("Converted")) {
                                String strSts = "";
                                Cursor c2 = db.GetStatus_based_on_lead_id(strCIFBDMUserId, strLeadID);
                                ArrayList<String> lsteventa = new ArrayList<>();
                                lsteventa.clear();
                                if (c2.getCount() > 0) {
                                    c2.moveToFirst();
                                    for (int ri = 0; ri < c2.getCount(); ri++) {
                                        lsteventa.add(c2.getString(c2.getColumnIndex("HOLeadStatus")));
                                        c2.moveToNext();
                                    }

                                    strSts = lsteventa.get(0);
                                }

                                if (strSyncFlag.contentEquals("Close") && strSts.contentEquals("Converted")) {
                                    Toast.makeText(
                                            con,
                                            "Converted Sync lead can not be edit..",
                                            Toast.LENGTH_LONG).show();
                                } else {

                                    if (stredProposalNo.length() != 10) {
                                        Toast.makeText(con,
                                                "Enter Valid Proposal No..",
                                                Toast.LENGTH_LONG).show();
                                    } else {

                                        // if(String.valueOf(stredProposalNo.charAt(0)).matches("\\d+"))
                                        // {
                                        // if(String.valueOf(stredProposalNo.charAt(1)).matches("[A-Z]"))
                                        // {
                                        strLeadSubStatus = "";

                                        ArrayList<String> lstevent = new ArrayList<>();
                                        lstevent.clear();

                                        Cursor c = db.Get_HOLeadUpdateRowId(
                                                strUserID, strRowId);
                                        if (c.getCount() > 0) {
                                            c.moveToFirst();
                                            for (int ii = 0; ii < c.getCount(); ii++) {
                                                lstevent.add(c.getString(c
                                                        .getColumnIndex("HOLeadDate")));
                                                lstevent.add(c.getString(c
                                                        .getColumnIndex("HOLeadCustomerId")));
                                                lstevent.add(c.getString(c
                                                        .getColumnIndex("HOLeadCustomerName")));
                                                lstevent.add(c.getString(c
                                                        .getColumnIndex("HOLeadPriority")));
                                                // lstevent.add(c.getString(c.getColumnIndex("HOLeadProposalNo")));
                                                lstevent.add(c.getString(c
                                                        .getColumnIndex("HOLeadFollowUpDate")));
                                                lstevent.add(c.getString(c
                                                        .getColumnIndex("HOLeadAge")));
                                                lstevent.add(c.getString(c
                                                        .getColumnIndex("HOLeadTotalAcc")));
                                                lstevent.add(c.getString(c
                                                        .getColumnIndex("HOLeadBalance")));
                                                lstevent.add(c.getString(c
                                                        .getColumnIndex("HOLeadBranchCode")));
                                                lstevent.add(c.getString(c
                                                        .getColumnIndex("HOLeadSync")));
                                                lstevent.add(c.getString(c
                                                        .getColumnIndex("HOLeadServerID")));
                                                lstevent.add(c.getString(c
                                                        .getColumnIndex("HOLeadBDMName")));
                                                lstevent.add(c.getString(c
                                                        .getColumnIndex("HOLeadSource")));

                                                c.moveToNext();
                                            }

                                            clsHOLead ObhclsHOLead = new clsHOLead(
                                                    lstevent.get(0),
                                                    lstevent.get(1),
                                                    lstevent.get(2),
                                                    lstevent.get(3),
                                                    strLeadStatus,
                                                    strLeadSubStatus,
                                                    stredProposalNo,
                                                    lstevent.get(4),
                                                    strComments,
                                                    lstevent.get(5),
                                                    lstevent.get(6),
                                                    lstevent.get(7),
                                                    lstevent.get(8),
                                                    strUserID,
                                                    lstevent.get(9),
                                                    lstevent.get(10),
                                                    lstevent.get(11),
                                                    lstevent.get(12));
                                            db.UpdateHOLead(ObhclsHOLead,
                                                    strRowId);

                                            // updatealert();
                                            // Toast.makeText(con,
                                            // "Save Successfully...",
                                            // Toast.LENGTH_LONG).show();

                                            if (mCommonMethods.isNetworkConnected(con)) {
                                                startupdateLead();
                                            } else {
                                                if (strSyncFlag
                                                        .contentEquals("Close")) {
                                                    clsHOLead ObhclsHOLeadS = new clsHOLead(
                                                            lstevent.get(0),
                                                            lstevent.get(1),
                                                            lstevent.get(2),
                                                            lstevent.get(3),
                                                            strLeadStatus,
                                                            strLeadSubStatus,
                                                            stredProposalNo,
                                                            lstevent.get(4),
                                                            strComments,
                                                            lstevent.get(5),
                                                            lstevent.get(6),
                                                            lstevent.get(7),
                                                            lstevent.get(8),
                                                            strUserID,
                                                            "Open",
                                                            lstevent.get(10),
                                                            lstevent.get(11),
                                                            lstevent.get(12));
                                                    db.UpdateHOLead(
                                                            ObhclsHOLeadS,
                                                            strRowId);
                                                }

                                                Toast.makeText(
                                                        con,
                                                        "Internet Connection Not Present,Try again..",
                                                        Toast.LENGTH_LONG)
                                                        .show();
                                            }

                                        }

                                        if (chkholead.isChecked()) {

                                            // Create a new calendar set to the
                                            // date chosen
                                            // we set the time to midnight (i.e.
                                            // the first minute of that day)
                                            Calendar ca = Calendar
                                                    .getInstance();
                                            ca.set(intyear, intmonth, intday);
                                            ca.set(Calendar.HOUR_OF_DAY, 0);
                                            ca.set(Calendar.MINUTE, 0);
                                            ca.set(Calendar.SECOND, 0);
                                            // Ask our service to set an alarm
                                            // for that date, this activity
                                            // talks to the client that talks to
                                            // the service
                                            scheduleClient
                                                    .setAlarmForNotification(ca);
                                            // Notify the user what they just
                                            // did
                                            // Toast.makeText(this,
                                            // "Notification set for: "+ day
                                            // +"/"+ (month+1) +"/"+ year,
                                            // Toast.LENGTH_SHORT).show();

                                        }

                                        chkholead.setChecked(false);

                                        // }
                                        // else
                                        // {
                                        // Toast.makeText(con,
                                        // "Enter Valid Proposal No..",
                                        // Toast.LENGTH_LONG).show();
                                        // }
                                        // }
                                        // else
                                        // {
                                        // Toast.makeText(con,
                                        // "Enter Valid Proposal No..",
                                        // Toast.LENGTH_LONG).show();
                                        // }
                                    }
                                }
                            } else {/*
                             * if(stredProposalNo.length() != 10) {
                             * Toast.makeText(con,
                             * "Enter Valid Proposal No..",
                             * Toast.LENGTH_LONG).show(); } else {
                             */
                                strLeadSubStatus = "";

                                ArrayList<String> lstevent = new ArrayList<>();
                                lstevent.clear();

                                Cursor c = db.Get_HOLeadUpdateRowId(strUserID,
                                        strRowId);
                                if (c.getCount() > 0) {
                                    c.moveToFirst();
                                    for (int ii = 0; ii < c.getCount(); ii++) {
                                        lstevent.add(c.getString(c
                                                .getColumnIndex("HOLeadDate")));
                                        lstevent.add(c.getString(c
                                                .getColumnIndex("HOLeadCustomerId")));
                                        lstevent.add(c.getString(c
                                                .getColumnIndex("HOLeadCustomerName")));
                                        lstevent.add(c.getString(c
                                                .getColumnIndex("HOLeadPriority")));
                                        // lstevent.add(c.getString(c.getColumnIndex("HOLeadProposalNo")));
                                        lstevent.add(c.getString(c
                                                .getColumnIndex("HOLeadFollowUpDate")));
                                        lstevent.add(c.getString(c
                                                .getColumnIndex("HOLeadAge")));
                                        lstevent.add(c.getString(c
                                                .getColumnIndex("HOLeadTotalAcc")));
                                        lstevent.add(c.getString(c
                                                .getColumnIndex("HOLeadBalance")));
                                        lstevent.add(c.getString(c
                                                .getColumnIndex("HOLeadBranchCode")));
                                        lstevent.add(c.getString(c
                                                .getColumnIndex("HOLeadSync")));
                                        lstevent.add(c.getString(c
                                                .getColumnIndex("HOLeadServerID")));
                                        lstevent.add(c.getString(c
                                                .getColumnIndex("HOLeadBDMName")));
                                        lstevent.add(c.getString(c
                                                .getColumnIndex("HOLeadSource")));

                                        c.moveToNext();
                                    }

                                    clsHOLead ObhclsHOLead = new clsHOLead(
                                            lstevent.get(0),
                                            lstevent.get(1),
                                            lstevent.get(2),
                                            lstevent.get(3),
                                            strLeadStatus, strLeadSubStatus,
                                            stredProposalNo, lstevent.get(4), strComments,
                                            lstevent.get(5),
                                            lstevent.get(6),
                                            lstevent.get(7),
                                            lstevent.get(8),
                                            strUserID, lstevent.get(9), lstevent.get(
                                            10), lstevent
                                            .get(11),
                                            lstevent.get(12));
                                    db.UpdateHOLead(ObhclsHOLead, strRowId);

                                    // updatealert();
                                    // Toast.makeText(con,
                                    // "Save Successfully...",
                                    // Toast.LENGTH_LONG).show();

                                    if (mCommonMethods.isNetworkConnected(con)) {
                                        startupdateLead();
                                    } else {
                                        if (strSyncFlag.contentEquals("Close")) {
                                            clsHOLead ObhclsHOLeadS = new clsHOLead(
                                                    lstevent.get(0),
                                                    lstevent.get(1),
                                                    lstevent.get(2),
                                                    lstevent.get(3),
                                                    strLeadStatus,
                                                    strLeadSubStatus,
                                                    stredProposalNo,
                                                    lstevent.get(4),
                                                    strComments,
                                                    lstevent.get(5),
                                                    lstevent.get(6),
                                                    lstevent.get(7),
                                                    lstevent.get(8),
                                                    strUserID,
                                                    "Open",
                                                    lstevent.get(10),
                                                    lstevent.get(11),
                                                    lstevent.get(12));
                                            db.UpdateHOLead(ObhclsHOLeadS,
                                                    strRowId);
                                        }

                                        Toast.makeText(
                                                con,
                                                "Internet Connection Not Present,Try again..",
                                                Toast.LENGTH_LONG).show();
                                    }

                                }

                                if (chkholead.isChecked()) {

                                    // Create a new calendar set to the date
                                    // chosen
                                    // we set the time to midnight (i.e. the
                                    // first minute of that day)
                                    Calendar ca = Calendar.getInstance();
                                    ca.set(intyear, intmonth, intday);
                                    ca.set(Calendar.HOUR_OF_DAY, 0);
                                    ca.set(Calendar.MINUTE, 0);
                                    ca.set(Calendar.SECOND, 0);
                                    // Ask our service to set an alarm for that
                                    // date, this activity talks to the client
                                    // that talks to the service
                                    scheduleClient.setAlarmForNotification(ca);
                                    // Notify the user what they just did
                                    // Toast.makeText(this,
                                    // "Notification set for: "+ day +"/"+
                                    // (month+1) +"/"+ year,
                                    // Toast.LENGTH_SHORT).show();

                                }

                                chkholead.setChecked(false);
                                // }
                            }
                        }

                    }
                }

            }
        });

        btncancelholead.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                finish();
            }
        });

        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        edhofolowdate.setClickable(true);

        edhofolowdate.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDialog(DATE_DIALOG_ID);

            }
        });

        if (strLeadStatus.contentEquals("Yet to Attend")) {
            selholeadstatus.setSelection(0);

            tblholeadsubstatus.setVisibility(View.GONE);
            tblhofollowupdate.setVisibility(View.GONE);
            chkholead.setVisibility(View.GONE);

            tblhoproposalno.setVisibility(View.GONE);

            selholeadstatus.setEnabled(true);
            edhoproposalno.setEnabled(true);
        }
        /*
         * else if(strLeadStatus.contentEquals("Open")) {
         * selholeadstatus.setSelection(1);
         *
         * tblholeadsubstatus.setVisibility(View.GONE);
         * tblhofollowupdate.setVisibility(View.GONE);
         * chkholead.setVisibility(View.GONE);
         *
         * tblhoproposalno.setVisibility(View.GONE);
         *
         * selholeadstatus.setEnabled(true); edhoproposalno.setEnabled(true); }
         */
        else if (strLeadStatus.contentEquals("In Progress")) {
            selholeadstatus.setSelection(1);

            tblhofollowupdate.setVisibility(View.VISIBLE);
            chkholead.setVisibility(View.VISIBLE);

            tblhoproposalno.setVisibility(View.GONE);
            tblholeadsubstatus.setVisibility(View.GONE);

            selholeadstatus.setEnabled(true);
            edhoproposalno.setEnabled(true);
        } else if (strLeadStatus.contentEquals("On Hold")) {
            selholeadstatus.setSelection(2);

            tblhofollowupdate.setVisibility(View.VISIBLE);
            chkholead.setVisibility(View.VISIBLE);

            tblhoproposalno.setVisibility(View.GONE);
            tblholeadsubstatus.setVisibility(View.GONE);

            selholeadstatus.setEnabled(true);
            edhoproposalno.setEnabled(true);
        } else if (strLeadStatus.contentEquals("Dropped")) {
            selholeadstatus.setSelection(3);

            tblholeadsubstatus.setVisibility(View.VISIBLE);
            chkholead.setVisibility(View.GONE);
            tblhofollowupdate.setVisibility(View.GONE);
            tblhoproposalno.setVisibility(View.GONE);

            selholeadstatus.setEnabled(true);
            edhoproposalno.setEnabled(true);
        } else if (strLeadStatus.contentEquals("Converted")) {
            selholeadstatus.setSelection(4);

            chkholead.setVisibility(View.GONE);
            tblhoproposalno.setVisibility(View.VISIBLE);
            tblholeadsubstatus.setVisibility(View.GONE);
            tblhofollowupdate.setVisibility(View.GONE);

            selholeadstatus.setEnabled(false);
            edhoproposalno.setEnabled(false);
        }

        if (strLeadSubStatus.contentEquals("Not Contactable")) {
            selholeadsubstatus.setSelection(0);
        } else if (strLeadSubStatus.contentEquals("Not Interested")) {
            selholeadsubstatus.setSelection(1);
        } else if (strLeadSubStatus.contentEquals("Recontact after 3 months")) {
            selholeadsubstatus.setSelection(2);
        } else if (strLeadSubStatus.contentEquals("Already has SBIL Policy")) {
            selholeadsubstatus.setSelection(3);
        } else if (strLeadSubStatus.contentEquals("Has other Co. Policy")) {
            selholeadsubstatus.setSelection(4);
        } else if (strLeadSubStatus
                .contentEquals("Dissatisfied with SBIL / Product")) {
            selholeadsubstatus.setSelection(5);
        }

    }

    private void updateDisplay(int year, int month, int day) {
        String y = String.valueOf(year);
        String m = String.valueOf(month + 1);
        String d = String.valueOf(day);
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

        edhofolowdate.setText(totaldate);

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

            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(con, ProgressDialog.THEME_HOLO_LIGHT);
                String Message = "Loading. Please wait...";
                mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(true);

                mProgressDialog.setButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                taskPushLead.cancel(true);
                                if (mProgressDialog.isShowing())
                                    mProgressDialog.dismiss();
                            }
                        });

                mProgressDialog.setMax(100);
                mProgressDialog.show();
                return mProgressDialog;

            case DATE_DIALOG_ID:

			/*return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);*/
                return new DatePickerDialog(this, R.style.datepickerstyle,
                        mDateSetListener, mYear, mMonth, mDay);
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DATE_DIALOG_ID:
                if (!this.isFinishing())
                    ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
        }
    }

    private void startupdateLead() {
        taskPushLead.execute("demo");
    }

    class DownloadPushLead extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        String selholeadstatus_text, selholeadsubstatus_text, edhofolowdate_text, edhocomments_text;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
            selholeadstatus_text = selholeadstatus.getSelectedItem().toString();
            selholeadsubstatus_text = selholeadsubstatus.getSelectedItem().toString();
            edhofolowdate_text = edhofolowdate.getText().toString();
            edhocomments_text = edhocomments.getText().toString();
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                String METHOD_NAME_UPDATELEAD = "updateLead";
                String NAMESPACE = "http://tempuri.org/";
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_UPDATELEAD);

                if (selholeadstatus_text.contentEquals("Dropped")) {
                    strLeadSubStatus = selholeadsubstatus_text;
                } else {
                    strLeadSubStatus = "";
                }

                String strFollodate = "";

                if (selholeadstatus_text
                        .contentEquals("In Progress")
                        || selholeadstatus_text
                        .contentEquals("On Hold")) {
                    final SimpleDateFormat df = new SimpleDateFormat(
                            "MM-dd-yyyy");

                    Date dt2 = null;
                    try {
                        dt2 = dateFormat.parse(edhofolowdate_text);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    strFollodate = df.format(dt2);
                } else {
                    strFollodate = "";
                }

                request.addProperty("bdmCode", strUserID);
                request.addProperty("intLeadId", strLeadID);
                if (strFollodate.contentEquals("")) {
                    request.addProperty("strStatus", selholeadstatus_text
                    );
                } else {
                    request.addProperty("strStatus", selholeadstatus_text
                            + "|" + strFollodate);
                }

                request.addProperty("strRemarks", edhocomments_text);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());
                request.addProperty("strSubStatus", strLeadSubStatus);
                request.addProperty("strProNum", stredProposalNo);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                //allowAllSSL();
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    String SOAP_ACTION_UPDATELEAD = "http://tempuri.org/updateLead";
                    androidHttpTranport.call(SOAP_ACTION_UPDATELEAD, envelope);
                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("anyType{}")) {

                        strLeadErrorCode = response.toString();

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

                if (strLeadErrorCode.contentEquals("1")) {

                    ArrayList<String> lstevent = new ArrayList<>();
                    lstevent.clear();

                    Cursor c = db.Get_HOLeadUpdateRowId(strUserID, strRowId);
                    if (c.getCount() > 0) {
                        c.moveToFirst();
                        for (int ii = 0; ii < c.getCount(); ii++) {
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadDate")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadCustomerId")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadCustomerName")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadPriority")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadStatus")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadSubStatus")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadProposalNo")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadFollowUpDate")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadComments")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadAge")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadTotalAcc")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadBalance")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadBranchCode")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadUserID")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadServerID")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadBDMName")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadSource")));

                            c.moveToNext();
                        }

                        clsHOLead ObhclsHOLead = new clsHOLead(lstevent.get(0), lstevent.get(1),
                                lstevent.get(2), lstevent.get(3),
                                lstevent.get(4), lstevent.get(5),
                                lstevent.get(6), lstevent.get(7),
                                lstevent.get(8), lstevent.get(9), lstevent.get(10), lstevent.get(11), lstevent.get(12), lstevent.get(13), "Close", lstevent.get(14), lstevent.get(15), lstevent.get(16));
                        db.UpdateHOLead(ObhclsHOLead, strRowId);

                        Toast.makeText(con, "Sync Lead Successfully..",
                                Toast.LENGTH_LONG).show();

                        finish();

                    } else {

                        Toast.makeText(con,
                                "Server Not Responding,Try again..",
                                Toast.LENGTH_LONG).show();
                    }

                } else {

                    ArrayList<String> lstevent = new ArrayList<>();
                    lstevent.clear();

                    Cursor c = db.Get_HOLeadUpdateRowId(strUserID, strRowId);
                    if (c.getCount() > 0) {
                        c.moveToFirst();
                        for (int ii = 0; ii < c.getCount(); ii++) {
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadDate")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadCustomerId")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadCustomerName")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadPriority")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadStatus")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadSubStatus")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadProposalNo")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadFollowUpDate")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadComments")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadAge")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadTotalAcc")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadBalance")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadBranchCode")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadUserID")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadServerID")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadBDMName")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("HOLeadSource")));

                            c.moveToNext();
                        }

                        clsHOLead ObhclsHOLead = new clsHOLead(lstevent.get(0), lstevent.get(1),
                                lstevent.get(2), lstevent.get(3),
                                lstevent.get(4), lstevent.get(5),
                                lstevent.get(6), lstevent.get(7),
                                lstevent.get(8), lstevent.get(9), lstevent.get(10), lstevent.get(11), lstevent.get(12), lstevent.get(13), "Open", lstevent.get(14), lstevent.get(15), lstevent.get(16));
                        db.UpdateHOLead(ObhclsHOLead, strRowId);
                    }

                    // syncerror();
                    Toast.makeText(con, "Server Not Responding,Try again..",
                            Toast.LENGTH_LONG).show();
                }
            } else {
                ArrayList<String> lstevent = new ArrayList<>();
                lstevent.clear();

                Cursor c = db.Get_HOLeadUpdateRowId(strUserID, strRowId);
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    for (int ii = 0; ii < c.getCount(); ii++) {
                        lstevent.add(c.getString(c.getColumnIndex("HOLeadDate")));
                        lstevent.add(c.getString(c
                                .getColumnIndex("HOLeadCustomerId")));
                        lstevent.add(c.getString(c
                                .getColumnIndex("HOLeadCustomerName")));
                        lstevent.add(c.getString(c
                                .getColumnIndex("HOLeadPriority")));
                        lstevent.add(c.getString(c
                                .getColumnIndex("HOLeadStatus")));
                        lstevent.add(c.getString(c
                                .getColumnIndex("HOLeadSubStatus")));
                        lstevent.add(c.getString(c
                                .getColumnIndex("HOLeadProposalNo")));
                        lstevent.add(c.getString(c
                                .getColumnIndex("HOLeadFollowUpDate")));
                        lstevent.add(c.getString(c
                                .getColumnIndex("HOLeadComments")));
                        lstevent.add(c.getString(c.getColumnIndex("HOLeadAge")));
                        lstevent.add(c.getString(c
                                .getColumnIndex("HOLeadTotalAcc")));
                        lstevent.add(c.getString(c
                                .getColumnIndex("HOLeadBalance")));
                        lstevent.add(c.getString(c
                                .getColumnIndex("HOLeadBranchCode")));
                        lstevent.add(c.getString(c
                                .getColumnIndex("HOLeadUserID")));
                        lstevent.add(c.getString(c
                                .getColumnIndex("HOLeadServerID")));
                        lstevent.add(c.getString(c
                                .getColumnIndex("HOLeadBDMName")));
                        lstevent.add(c.getString(c
                                .getColumnIndex("HOLeadSource")));

                        c.moveToNext();
                    }

                    clsHOLead ObhclsHOLead = new clsHOLead(lstevent.get(0), lstevent.get(1), lstevent
                            .get(2), lstevent.get(3),
                            lstevent.get(4), lstevent.get(5), lstevent.get(6),
                            lstevent.get(7), lstevent.get(8), lstevent.get(9),
                            lstevent.get(10), lstevent.get(11), lstevent.get(12),
                            lstevent.get(13), "Open", lstevent.get(
                            14),
                            lstevent.get(15), lstevent.get(16));
                    db.UpdateHOLead(ObhclsHOLead, strRowId);
                }

                // syncerror();
                Toast.makeText(con, "Server Not Responding,Try again..",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void dateselecterror() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Date should be greater or equal than Today's date");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}