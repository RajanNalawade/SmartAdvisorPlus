package sbilife.com.pointofsale_bancaagency.reports.agency;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolder_Agent_Comm;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.Utility;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods.UserDetailsValuesModel;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits.DownLoadData;

public class AgencyReportsCommissionActivity extends AppCompatActivity implements OnClickListener, DownLoadData {
    private  final String METHOD_NAME_COMM = "getAgentCommission";
    private DownloadFileAsyncComm taskComm;
    private CommonMethods commonMethods;
    private Context context;
    private  final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    private String strCIFBDMUserId = "";
    private String strCIFBDMEmailId = "";
    private String strCIFBDMPassword = "";
    private String strCIFBDMMObileNo = "";
    private String strfromdate = "";
    private String strCommErrorCode = "";
    private Spinner spinComm;
    private SelectedAdapterAgent_Comm selectedAdapterAgent_Comm;
    private TextView txterrordesccomm;
    private ListView lstComm;
    private ServiceHits service;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.reports_commission);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        taskComm = new DownloadFileAsyncComm();
        commonMethods = new CommonMethods();
        mProgressDialog = new ProgressDialog(context);
        commonMethods.setApplicationToolbarMenu(this, "Commission");
        spinComm = findViewById(R.id.spinComm);
        txterrordesccomm = findViewById(R.id.txterrordesccomm);
        lstComm = findViewById(R.id.lstComm);
        Button btn_comm = findViewById(R.id.btn_comm);
        Button btn_reset_comm = findViewById(R.id.btn_reset_comm);
        btn_comm.setOnClickListener(this);
        btn_reset_comm.setOnClickListener(this);
        setSpinner();
        getUserDetails();

        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");
        if (fromHome != null && fromHome.equalsIgnoreCase("N")) {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");

            try {
                strCIFBDMPassword = SimpleCrypto.encrypt("SBIL", "sbil");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            getUserDetails();

        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
                String Message = "Loading Please wait...";
                mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(true);
                mProgressDialog.setButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (taskComm != null) taskComm.cancel(true);
                        if (mProgressDialog != null)
                            if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
                    }
                });
                mProgressDialog.setMax(100);
                mProgressDialog.show();
                return mProgressDialog; /*case DATE_DIALOG_ID: return new DatePickerDialog(this, R.style.datepickerstyle, mDateSetListener, mYear, mMonth, mDay);*/
            default:
                return null;
        }
    }

    private void getUserDetails() {
        UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }

    private void setSpinner() {
        List<String> lst = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        int mDay = cal.get(Calendar.DAY_OF_MONTH);
        String fyear;
        String lastd;
        String py; /*String y = String.valueOf(mYear);*/
        String m = String.valueOf(mMonth + 1);
        if (m.contentEquals("1")) {
            fyear = String.valueOf(mYear - 1);
            py = String.valueOf(mYear - 1);
            lastd = String.valueOf(mYear % 100);
            if (mDay > 15 || mDay == 15) if (mDay == 31) {
                lst.add("31-January-" + mYear);
                lst.add("15-January-" + mYear);
            } else lst.add("15-January-" + mYear);
            lst.add("31-December-" + py);
            lst.add("15-December-" + py);
            lst.add("30-November-" + py);
            lst.add("15-November-" + py);
            lst.add("31-October-" + py);
            lst.add("15-October-" + py);
            lst.add("30-September-" + py);
            lst.add("15-September-" + py);
            lst.add("31-August-" + py);
            lst.add("15-August-" + py);
            lst.add("31-July-" + py);
            lst.add("15-July-" + py);
            lst.add("30-June-" + py);
            lst.add("15-June-" + py);
        } else if (m.contentEquals("2")) {
            fyear = String.valueOf(mYear - 1);
            py = String.valueOf(mYear - 1);
            lastd = String.valueOf(mYear % 100);
            if (mDay > 15 || mDay == 15) if (mDay == 28) {
                lst.add("28-February-" + mYear);
                lst.add("15-February-" + mYear);
            } else lst.add("15-February-" + mYear);
            lst.add("31-January-" + mYear);
            lst.add("15-January-" + mYear);
            lst.add("31-December-" + py);
            lst.add("15-December-" + py);
            lst.add("30-November-" + py);
            lst.add("15-November-" + py);
            lst.add("31-October-" + py);
            lst.add("15-October-" + py);
            lst.add("30-September-" + py);
            lst.add("15-September-" + py);
            lst.add("31-August-" + py);
            lst.add("15-August-" + py);
            lst.add("31-July-" + py);
            lst.add("15-July-" + py);
            lst.add("30-June-" + py);
            lst.add("15-June-" + py);
        } else if (m.contentEquals("3")) {
            fyear = String.valueOf(mYear - 1);
            py = String.valueOf(mYear - 1);
            lastd = String.valueOf(mYear % 100);
            if (mDay > 15 || mDay == 15) if (mDay == 31) {
                lst.add("31-March-" + mYear);
                lst.add("15-March-" + mYear);
            } else lst.add("15-March-" + mYear);
            lst.add("28-February-" + mYear);
            lst.add("15-February-" + mYear);
            lst.add("31-January-" + mYear);
            lst.add("15-January-" + mYear);
            lst.add("31-December-" + py);
            lst.add("15-December-" + py);
            lst.add("30-November-" + py);
            lst.add("15-November-" + py);
            lst.add("31-October-" + py);
            lst.add("15-October-" + py);
            lst.add("30-September-" + py);
            lst.add("15-September-" + py);
            lst.add("31-August-" + py);
            lst.add("15-August-" + py);
            lst.add("31-July-" + py);
            lst.add("15-July-" + py);
            lst.add("30-June-" + py);
            lst.add("15-June-" + py);
        } else if (m.contentEquals("4")) {
            fyear = String.valueOf(mYear - 1);
            py = String.valueOf(mYear - 1);
            lastd = String.valueOf(mYear % 100);
            if (mDay > 15 || mDay == 15) if (mDay == 30) {
                lst.add("30-April-" + mYear);
                lst.add("15-April-" + mYear);
            } else lst.add("15-April-" + mYear);
            lst.add("31-March-" + mYear);
            lst.add("15-March-" + mYear);
            lst.add("28-February-" + mYear);
            lst.add("15-February-" + mYear);
            lst.add("31-January-" + mYear);
            lst.add("15-January-" + mYear);
            lst.add("31-December-" + py);
            lst.add("15-December-" + py);
            lst.add("30-November-" + py);
            lst.add("15-November-" + py);
            lst.add("31-October-" + py);
            lst.add("15-October-" + py);
            lst.add("30-September-" + py);
            lst.add("15-September-" + py);
            lst.add("31-August-" + py);
            lst.add("15-August-" + py);
            lst.add("31-July-" + py);
            lst.add("15-July-" + py);
            lst.add("30-June-" + py);
            lst.add("15-June-" + py);
        } else if (m.contentEquals("5")) {
            fyear = String.valueOf(mYear - 1);
            py = String.valueOf(mYear - 1);
            lastd = String.valueOf(mYear % 100);
            if (mDay > 15 || mDay == 15) if (mDay == 31) {
                lst.add("31-May-" + mYear);
                lst.add("15-May-" + mYear);
            } else lst.add("15-May-" + mYear);
            lst.add("30-April-" + mYear);
            lst.add("15-April-" + mYear);
            lst.add("31-March-" + mYear);
            lst.add("15-March-" + mYear);
            lst.add("28-February-" + mYear);
            lst.add("15-February-" + mYear);
            lst.add("31-January-" + mYear);
            lst.add("15-January-" + mYear);
            lst.add("31-December-" + py);
            lst.add("15-December-" + py);
            lst.add("30-November-" + py);
            lst.add("15-November-" + py);
            lst.add("31-October-" + py);
            lst.add("15-October-" + py);
            lst.add("30-September-" + py);
            lst.add("15-September-" + py);
            lst.add("31-August-" + py);
            lst.add("15-August-" + py);
            lst.add("31-July-" + py);
            lst.add("15-July-" + py);
            lst.add("30-June-" + py);
            lst.add("15-June-" + py);
        } else if (m.contentEquals("6")) {
            fyear = String.valueOf(mYear - 1);
            py = String.valueOf(mYear - 1);
            lastd = String.valueOf(mYear % 100);
            if (mDay > 15 || mDay == 15) if (mDay == 30) {
                lst.add("30-June-" + mYear);
                lst.add("15-June-" + mYear);
            } else lst.add("15-June-" + mYear);
            lst.add("31-May-" + mYear);
            lst.add("15-May-" + mYear);
            lst.add("30-April-" + mYear);
            lst.add("15-April-" + mYear);
            lst.add("31-March-" + mYear);
            lst.add("15-March-" + mYear);
            lst.add("28-February-" + mYear);
            lst.add("15-February-" + mYear);
            lst.add("31-January-" + mYear);
            lst.add("15-January-" + mYear);
            lst.add("31-December-" + py);
            lst.add("15-December-" + py);
            lst.add("30-November-" + py);
            lst.add("15-November-" + py);
            lst.add("31-October-" + py);
            lst.add("15-October-" + py);
            lst.add("30-September-" + py);
            lst.add("15-September-" + py);
            lst.add("31-August-" + py);
            lst.add("15-August-" + py);
            lst.add("31-July-" + py);
            lst.add("15-July-" + py);
            lst.add("30-June-" + py);
            lst.add("15-June-" + py);
        } else if (m.contentEquals("7")) {
            fyear = String.valueOf(mYear - 1);
            py = String.valueOf(mYear - 1);
            lastd = String.valueOf(mYear % 100);
            if (mDay > 15 || mDay == 15) if (mDay == 31) {
                lst.add("31-July-" + mYear);
                lst.add("15-July-" + mYear);
            } else lst.add("15-July-" + mYear);
            lst.add("30-June-" + mYear);
            lst.add("15-June-" + mYear);
            lst.add("31-May-" + mYear);
            lst.add("15-May-" + mYear);
            lst.add("30-April-" + mYear);
            lst.add("15-April-" + mYear);
            lst.add("31-March-" + mYear);
            lst.add("15-March-" + mYear);
            lst.add("28-February-" + mYear);
            lst.add("15-February-" + mYear);
            lst.add("31-January-" + mYear);
            lst.add("15-January-" + mYear);
            lst.add("31-December-" + py);
            lst.add("15-December-" + py);
            lst.add("30-November-" + py);
            lst.add("15-November-" + py);
            lst.add("31-October-" + py);
            lst.add("15-October-" + py);
            lst.add("30-September-" + py);
            lst.add("15-September-" + py);
            lst.add("31-August-" + py);
            lst.add("15-August-" + py);
            lst.add("31-July-" + py);
            lst.add("15-July-" + py);
            lst.add("30-June-" + py);
            lst.add("15-June-" + py);
        } else if (m.contentEquals("8")) {
            fyear = String.valueOf(mYear - 1);
            py = String.valueOf(mYear - 1);
            lastd = String.valueOf(mYear % 100);
            if (mDay > 15 || mDay == 15) if (mDay == 31) {
                lst.add("31-August-" + mYear);
                lst.add("15-August-" + mYear);
            } else lst.add("15-August-" + mYear);
            lst.add("31-July-" + mYear);
            lst.add("15-July-" + mYear);
            lst.add("30-June-" + mYear);
            lst.add("15-June-" + mYear);
            lst.add("31-May-" + mYear);
            lst.add("15-May-" + mYear);
            lst.add("30-April-" + mYear);
            lst.add("15-April-" + mYear);
            lst.add("31-March-" + mYear);
            lst.add("15-March-" + mYear);
            lst.add("28-February-" + mYear);
            lst.add("15-February-" + mYear);
            lst.add("31-January-" + mYear);
            lst.add("15-January-" + mYear);
            lst.add("31-December-" + py);
            lst.add("15-December-" + py);
            lst.add("30-November-" + py);
            lst.add("15-November-" + py);
            lst.add("31-October-" + py);
            lst.add("15-October-" + py);
            lst.add("30-September-" + py);
            lst.add("15-September-" + py);
            lst.add("31-August-" + py);
            lst.add("15-August-" + py);
            lst.add("31-July-" + py);
            lst.add("15-July-" + py);
            lst.add("30-June-" + py);
            lst.add("15-June-" + py);
        } else if (m.contentEquals("9")) {
            fyear = String.valueOf(mYear - 1);
            py = String.valueOf(mYear - 1);
            lastd = String.valueOf(mYear % 100);
            if (mDay > 15 || mDay == 15) if (mDay == 30) {
                lst.add("30-September-" + mYear);
                lst.add("15-September-" + mYear);
            } else lst.add("15-September-" + mYear);
            lst.add("31-August-" + mYear);
            lst.add("15-August-" + mYear);
            lst.add("31-July-" + mYear);
            lst.add("15-July-" + mYear);
            lst.add("30-June-" + mYear);
            lst.add("15-June-" + mYear);
            lst.add("31-May-" + mYear);
            lst.add("15-May-" + mYear);
            lst.add("30-April-" + mYear);
            lst.add("15-April-" + mYear);
            lst.add("31-March-" + mYear);
            lst.add("15-March-" + mYear);
            lst.add("28-February-" + mYear);
            lst.add("15-February-" + mYear);
            lst.add("31-January-" + mYear);
            lst.add("15-January-" + mYear);
            lst.add("31-December-" + py);
            lst.add("15-December-" + py);
            lst.add("30-November-" + py);
            lst.add("15-November-" + py);
            lst.add("31-October-" + py);
            lst.add("15-October-" + py);
            lst.add("30-September-" + py);
            lst.add("15-September-" + py);
            lst.add("31-August-" + py);
            lst.add("15-August-" + py);
            lst.add("31-July-" + py);
            lst.add("15-July-" + py);
            lst.add("30-June-" + py);
            lst.add("15-June-" + py);
        } else if (m.contentEquals("10")) {
            fyear = String.valueOf(mYear - 1);
            py = String.valueOf(mYear - 1);
            lastd = String.valueOf(mYear % 100);
            if (mDay > 15 || mDay == 15) if (mDay == 31) {
                lst.add("31-October-" + mYear);
                lst.add("15-October-" + mYear);
            } else lst.add("15-October-" + mYear);
            lst.add("30-September-" + mYear);
            lst.add("15-September-" + mYear);
            lst.add("31-August-" + mYear);
            lst.add("15-August-" + mYear);
            lst.add("31-July-" + mYear);
            lst.add("15-July-" + mYear);
            lst.add("30-June-" + mYear);
            lst.add("15-June-" + mYear);
            lst.add("31-May-" + mYear);
            lst.add("15-May-" + mYear);
            lst.add("30-April-" + mYear);
            lst.add("15-April-" + mYear);
            lst.add("31-March-" + mYear);
            lst.add("15-March-" + mYear);
            lst.add("28-February-" + mYear);
            lst.add("15-February-" + mYear);
            lst.add("31-January-" + mYear);
            lst.add("15-January-" + mYear);

            lst.add("31-December-" + py);
            lst.add("15-December-" + py);
            lst.add("30-November-" + py);
            lst.add("15-November-" + py);
            lst.add("31-October-" + py);
            lst.add("15-October-" + py);
            lst.add("30-September-" + py);
            lst.add("15-September-" + py);
            lst.add("31-August-" + py);
            lst.add("15-August-" + py);
            lst.add("31-July-" + py);
            lst.add("15-July-" + py);
            lst.add("30-June-" + py);
            lst.add("15-June-" + py);

        } else if (m.contentEquals("11")) {

            fyear = String.valueOf(mYear - 1);
            py = String.valueOf(mYear - 1);
            lastd = String.valueOf(mYear % 100);

            if (mDay > 15 || mDay == 15) {
                if (mDay == 30) {
                    lst.add("30-November-" + mYear);
                    lst.add("15-November-" + mYear);
                } else {
                    lst.add("15-November-" + mYear);
                }
            }

            lst.add("31-October-" + mYear);
            lst.add("15-October-" + mYear);
            lst.add("30-September-" + mYear);
            lst.add("15-September-" + mYear);
            lst.add("31-August-" + mYear);
            lst.add("15-August-" + mYear);
            lst.add("31-July-" + mYear);
            lst.add("15-July-" + mYear);
            lst.add("30-June-" + mYear);
            lst.add("15-June-" + mYear);
            lst.add("31-May-" + mYear);
            lst.add("15-May-" + mYear);
            lst.add("30-April-" + mYear);
            lst.add("15-April-" + mYear);
            lst.add("31-March-" + mYear);
            lst.add("15-March-" + mYear);
            lst.add("28-February-" + mYear);
            lst.add("15-February-" + mYear);
            lst.add("31-January-" + mYear);
            lst.add("15-January-" + mYear);

            lst.add("31-December-" + py);
            lst.add("15-December-" + py);
            lst.add("30-November-" + py);
            lst.add("15-November-" + py);
            lst.add("31-October-" + py);
            lst.add("15-October-" + py);
            lst.add("30-September-" + py);
            lst.add("15-September-" + py);
            lst.add("31-August-" + py);
            lst.add("15-August-" + py);
            lst.add("31-July-" + py);
            lst.add("15-July-" + py);
            lst.add("30-June-" + py);
            lst.add("15-June-" + py);

        } else if (m.contentEquals("12")) {

            fyear = String.valueOf(mYear - 1);
            py = String.valueOf(mYear - 1);
            lastd = String.valueOf(mYear % 100);

            if (mDay > 15 || mDay == 15) {
                if (mDay == 31) {
                    lst.add("31-December-" + mYear);
                    lst.add("15-December-" + mYear);
                } else {
                    lst.add("15-December-" + mYear);
                }
            }

            lst.add("30-November-" + mYear);
            lst.add("15-November-" + mYear);
            lst.add("31-October-" + mYear);
            lst.add("15-October-" + mYear);
            lst.add("30-September-" + mYear);
            lst.add("15-September-" + mYear);
            lst.add("31-August-" + mYear);
            lst.add("15-August-" + mYear);
            lst.add("31-July-" + mYear);
            lst.add("15-July-" + mYear);
            lst.add("30-June-" + mYear);
            lst.add("15-June-" + mYear);
            lst.add("31-May-" + mYear);
            lst.add("15-May-" + mYear);
            lst.add("30-April-" + mYear);
            lst.add("15-April-" + mYear);
            lst.add("31-March-" + mYear);
            lst.add("15-March-" + mYear);
            lst.add("28-February-" + mYear);
            lst.add("15-February-" + mYear);
            lst.add("31-January-" + mYear);
            lst.add("15-January-" + mYear);

            lst.add("31-December-" + py);
            lst.add("15-December-" + py);
            lst.add("30-November-" + py);
            lst.add("15-November-" + py);
            lst.add("31-October-" + py);
            lst.add("15-October-" + py);
            lst.add("30-September-" + py);
            lst.add("15-September-" + py);
            lst.add("31-August-" + py);
            lst.add("15-August-" + py);
            lst.add("31-July-" + py);
            lst.add("15-July-" + py);
            lst.add("30-June-" + py);
            lst.add("15-June-" + py);

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                R.layout.spinner_item, lst);
        spinComm.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @SuppressLint("SimpleDateFormat")
    private void service_hits() {


        strfromdate = spinComm.getSelectedItem().toString();

        final SimpleDateFormat formatter = new SimpleDateFormat(
                "dd-MMMM-yyyy");
        final SimpleDateFormat formatter1 = new SimpleDateFormat(
                "MM-dd-yyyy");
        try {
            Date dt = formatter.parse(strfromdate);
            strfromdate = formatter1.format(dt);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        service = new ServiceHits(context,
                METHOD_NAME_COMM, strfromdate, strCIFBDMUserId,
                strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();

    }


    private void startDownloadComm() {
        taskComm = new DownloadFileAsyncComm();
        taskComm.execute("demo");
    }

    @Override
    public void downLoadData() {
        startDownloadComm();
    }

    class DownloadFileAsyncComm extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        String spinComm_text;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
            spinComm_text = spinComm.getSelectedItem().toString();
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                SoapObject request;

                // strfromdate = editTextdtcomm.getText().toString();
                strfromdate = spinComm_text;

                final SimpleDateFormat formatter = new SimpleDateFormat(
                        "dd-MMMM-yyyy");
                final SimpleDateFormat formatter1 = new SimpleDateFormat(
                        "MM-dd-yyyy");
                try {
                    Date dt = formatter.parse(strfromdate);
                    strfromdate = formatter1.format(dt);

                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME_COMM);

                request.addProperty("strAgentNo", strCIFBDMUserId);
                request.addProperty("strBillCycleDt", strfromdate);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());

                System.out.println("request.toString():" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {

                    String SOAP_ACTION_COMM = "http://tempuri.org/getAgentCommission";
                    androidHttpTranport.call(SOAP_ACTION_COMM, envelope);
                    Object response = envelope.getResponse();

                    System.out.println("=====response:" + response);

                    if (!response.toString().contentEquals("anyType{}")) {

                        SoapPrimitive sa;
                        try {
                            sa = (SoapPrimitive) envelope.getResponse();

                            String inputpolicylist = sa.toString();

                            ParseXML prsObj = new ParseXML();

                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "Commission");
                            inputpolicylist = new ParseXML().parseXmlTag(
                                    inputpolicylist, "ScreenData");
                            strCommErrorCode = inputpolicylist;

                            if (strCommErrorCode == null) {
                                inputpolicylist = sa.toString();
                                inputpolicylist = prsObj.parseXmlTag(
                                        inputpolicylist, "Commission");

                                List<String> Node = prsObj.parseParentNode(
                                        inputpolicylist, "Table");

                                List<XMLHolder_Agent_Comm> nodeData = prsObj
                                        .parseNodeElementAgent_Comm(Node);

                                ArrayList<XMLHolder_Agent_Comm> lsAgent_Comm = new ArrayList<>();
                                lsAgent_Comm.clear();

                                lsAgent_Comm.addAll(nodeData);

                                selectedAdapterAgent_Comm = new SelectedAdapterAgent_Comm(
                                        context, lsAgent_Comm);
                                selectedAdapterAgent_Comm
                                        .setNotifyOnChange(true);

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
                    }

                } catch (IOException | XmlPullParserException e) {
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
            if (mProgressDialog.isShowing()) {
                dismissProgressDialog();
            }
            if (running) {
                if (strCommErrorCode == null) {
                    txterrordesccomm.setText("");
                    lstComm.setAdapter(selectedAdapterAgent_Comm);
                    Utility.setListViewHeightBasedOnChildren(lstComm);
                } else {
                    txterrordesccomm.setText("No Record Found");

                    List<XMLHolder_Agent_Comm> lst;
                    XMLHolder_Agent_Comm node = null;
                    lst = new ArrayList<>();
                    lst.clear();
                    lst.add(null);
                    selectedAdapterAgent_Comm = new SelectedAdapterAgent_Comm(
                            context, lst);
                    selectedAdapterAgent_Comm.setNotifyOnChange(true);
                    lstComm.setAdapter(selectedAdapterAgent_Comm);

                    Utility.setListViewHeightBasedOnChildren(lstComm);
                }
            } else {
                //servererror();
                commonMethods.showMessageDialog(context, commonMethods.SERVER_ERROR);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void dismissProgressDialog() {
        if (mProgressDialog.isShowing()) {
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
        }
    }

    @SuppressWarnings("deprecation")
    private void showProgressDialog() {
        showDialog(DIALOG_DOWNLOAD_PROGRESS);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (taskComm != null) {
                taskComm.cancel(true);
            }
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
            finish();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class SelectedAdapterAgent_Comm extends ArrayAdapter<XMLHolder_Agent_Comm> {

        // used to keep selected position in ListView
        private int selectedPos = -1; // init value for not-selected

        final List<XMLHolder_Agent_Comm> lst;

        SelectedAdapterAgent_Comm(Context context,
                                  List<XMLHolder_Agent_Comm> objects) {
            super(context, 0, objects);

            lst = objects;
        }


        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            // only inflate the view if it's null
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) this.getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_agent_comm, null);
            }

            // get text view
            TextView txtempname = v.findViewById(R.id.txtempname);
            TextView txtempid = v.findViewById(R.id.txtempid);
            TextView txtpostype = v.findViewById(R.id.txtpostype);
            TextView txtchanltype = v
                    .findViewById(R.id.txtchanltype);
            TextView txtcomm = v.findViewById(R.id.txtcomm);
            TextView txttds = v.findViewById(R.id.txttds);
            TextView txtadjustment = v
                    .findViewById(R.id.txtadjustment);
            TextView txtst = v.findViewById(R.id.txtst);
            TextView txtfamt = v.findViewById(R.id.txtfamt);

            Object obj = null;
            boolean i = lst.contains(null);

            if (!i) {
                ArrayList<String> lstevent = new ArrayList<>();
                lstevent.clear();

                String name = "";
                String code = "";

                    DatabaseHelper dbhelper = new DatabaseHelper(context);
                    Cursor c = dbhelper.GetProfile();
                    if (c.getCount() > 0) {
                        c.moveToFirst();
                        for (int ii = 0; ii < c.getCount(); ii++) {
                            lstevent.add(c.getString(c
                                    .getColumnIndex("LoginTitle")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("LoginFirstName")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("LoginLastName")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("LoginAddress")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("LoginStatus")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("LoginCIFNo")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("LoginPateName")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("LoginEmail")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("LoginPassword")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("LoginConfirmPassword")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("LoginQuestion")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("LoginMobileNo")));
                            lstevent.add(c.getString(c
                                    .getColumnIndex("LoginDOB")));
                            c.moveToNext();
                        }
                    }
                    c.close();
                    try {
                        name = SimpleCrypto.decrypt("SBIL", lstevent.get(0))
                                + " "
                                + SimpleCrypto.decrypt("SBIL", lstevent.get(1))
                                + " "
                                + SimpleCrypto.decrypt("SBIL", lstevent.get(2));

                        code = SimpleCrypto.decrypt("SBIL", lstevent.get(5));

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                // txtempname.setText(lst.get(position).getEmpname() == null ?
                // "" : lst.get(position).getEmpname());
                txtempname.setText(name == null ? "" : name);
                // txtempid.setText(lst.get(position).getEmpid() == null ? "" :
                // lst.get(position).getEmpid());
                txtempid.setText(code == null ? "" : code);
                txtpostype
                        .setText(lst.get(position).getPositiontype() == null ? ""
                                : lst.get(position).getPositiontype());
                txtchanltype
                        .setText(lst.get(position).getChanneltype() == null ? ""
                                : lst.get(position).getChanneltype());
                txtcomm.setText(lst.get(position).getComm() == null ? "" : lst
                        .get(position).getComm());
                txttds.setText(lst.get(position).getTds() == null ? "" : lst
                        .get(position).getTds());
                txtadjustment
                        .setText(lst.get(position).getAdjustment() == null ? ""
                                : lst.get(position).getAdjustment());
                txtst.setText(lst.get(position).getServicetax() == null ? ""
                        : lst.get(position).getServicetax());
                txtfamt.setText(lst.get(position).getFinalamt() == null ? ""
                        : lst.get(position).getFinalamt());
            }

            return (v);
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.btn_comm) {
            getCommission();
        } else if (id == R.id.btn_reset_comm) {
            txterrordesccomm.setVisibility(View.GONE);
            lstComm.setVisibility(View.GONE);
        }
    }

    private void getCommission() {
        taskComm = new DownloadFileAsyncComm();
        txterrordesccomm.setVisibility(View.VISIBLE);
        lstComm.setVisibility(View.VISIBLE);

        if (commonMethods.isNetworkConnected(context)) {
            service_hits();
        } else {
            commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
        }
    }
    @Override
    protected void onDestroy() {
        if(mProgressDialog!=null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (taskComm != null) {
            taskComm.cancel(true);
        }

        if (service != null) {
            service.cancel(true);
        }

        super.onDestroy();
    }

}
