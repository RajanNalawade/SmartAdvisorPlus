package sbilife.com.pointofsale_bancaagency.reports;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLChannelProposerTrackerStatusList;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.Utility;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods.UserDetailsValuesModel;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits.DownLoadData;

@SuppressLint({"InflateParams", "SimpleDateFormat"})
public class BancaReportsAdvisorProposalsStatusActivity extends AppCompatActivity implements OnClickListener, DownLoadData {

    private final String METHOD_NAME_CHANNEL_PROPOSAL_TRACKER_LIST = "getChannelProposalStatus";

    private DownloadFileAsyncChannelProposalTrackerStatus taskchannelProposalTrackerStatus;
    private SelectedAdapterChannelProposalTracker adapterChannelProposalTracker;
    private final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;

    private int mYear, mMonth, mDay, datecheck = 0;
    private final int DATE_DIALOG_ID = 1;
    private Context context;
    private CommonMethods mCommonMethods;

    private String channelProposaltrackerStatusSpinner = "";
    private EditText editTextFromdateChannelProposerTracker, editTextToDateChannelProposerTracker;
    private Spinner spinnerChannelProposerTrackerList;

    private ListView listViewChannelProposerStatus;

    private String strCIFBDMUserId = "";
    private String strCIFBDMEmailId = "";
    private String strCIFBDMPassword = "";
    private String strCIFBDMMObileNo = "";
    private ServiceHits service;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.reports_advisor_proposals_status);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        context = this;
        mCommonMethods = new CommonMethods();
        taskchannelProposalTrackerStatus = new DownloadFileAsyncChannelProposalTrackerStatus();
        mProgressDialog = new ProgressDialog(this);

        mCommonMethods.setApplicationToolbarMenu(this, getString(R.string.channelProposerTrackerLabel));

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        editTextFromdateChannelProposerTracker = findViewById(R.id.editTextFromdateChannelProposerTracker);
        editTextToDateChannelProposerTracker = findViewById(R.id.editTextToDateChannelProposerTracker);
        ImageButton btnFromDateChannelProposerTrackerList = findViewById(R.id.btnFromDateChannelProposerTrackerList);
        ImageButton btnToDateChannelProposerTrackerList = findViewById(R.id.btnToDateChannelProposerTrackerList);
        spinnerChannelProposerTrackerList = findViewById(R.id.spinnerChannelProposerTrackerList);

        Button btnOkChannelProposerList = findViewById(R.id.btnOkChannelProposerList);
        Button btnResetChannelProposerList = findViewById(R.id.btnResetChannelProposerList);

        listViewChannelProposerStatus = findViewById(R.id.listViewChannelProposerStatus);

        btnFromDateChannelProposerTrackerList.setOnClickListener(this);
        btnToDateChannelProposerTrackerList.setOnClickListener(this);
        btnOkChannelProposerList.setOnClickListener(this);
        btnResetChannelProposerList.setOnClickListener(this);

        spinnerChannelProposerTrackerList
                .setOnItemSelectedListener(new OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                        channelProposaltrackerStatusSpinner = spinnerChannelProposerTrackerList
                                .getSelectedItem().toString();
                        System.out
                                .println("channelProposaltrackerStatusSpinner:"
                                        + channelProposaltrackerStatusSpinner);
                        if (adapterChannelProposalTracker != null) {
                            adapterChannelProposalTracker.clear();
                        }
                        listViewChannelProposerStatus.setVisibility(View.GONE);
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
        setDates();
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
        } else {
            getUserDetails();
        }

    }

    private void getUserDetails() {
        UserDetailsValuesModel userDetailsValuesModel = mCommonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

    }

    private void setDates() {
        Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        int mDay = cal.get(Calendar.DAY_OF_MONTH);

        String y = String.valueOf(mYear);
        String m = String.valueOf(mMonth + 1);
        String da = String.valueOf(mDay);

        m = mCommonMethods.getFullMonthName(m);
        String totaldate = da + "-" + m + "-" + y;
        editTextFromdateChannelProposerTracker.setText(totaldate);

        Calendar calenderChannelProposerTracker = Calendar.getInstance();
        calenderChannelProposerTracker.add(Calendar.MONTH, 1);
        int mYeareCPT = calenderChannelProposerTracker.get(Calendar.YEAR);
        int mMontheCPT = calenderChannelProposerTracker.get(Calendar.MONTH);
        int mDayeCPT = calenderChannelProposerTracker.get(Calendar.DAY_OF_MONTH);

        String yeCPT = String.valueOf(mYeareCPT);
        String meCPT = String.valueOf(mMontheCPT + 1);
        String daeCPT = String.valueOf(mDayeCPT);

        String channelProposerTodate = daeCPT + "-" + mCommonMethods.getFullMonthName(meCPT) + "-" + yeCPT;
        editTextToDateChannelProposerTracker.setText(channelProposerTodate);

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

                mProgressDialog.setButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                if (taskchannelProposalTrackerStatus != null) {
                                    taskchannelProposalTrackerStatus.cancel(true);
                                }
                                if (mProgressDialog != null) {
                                    if (mProgressDialog.isShowing()) {
                                        mProgressDialog.dismiss();
                                    }
                                }
                            }
                        });

                mProgressDialog.setMax(100);
                mProgressDialog.show();
                return mProgressDialog;

            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, R.style.datepickerstyle,
                        mDateSetListener, mYear, mMonth, mDay);

            default:
                return null;
        }
    }

    private void updateDisplay(int year, int month, int day) {

        String y = String.valueOf(year);
        String m = String.valueOf(month + 1);
        String da = String.valueOf(day);

        m = mCommonMethods.getFullMonthName(m);
        String totaldate = da + "-" + m + "-" + y;

        if (datecheck == 12) {
            editTextFromdateChannelProposerTracker.setText(totaldate);
        } else if (datecheck == 13) {
            editTextToDateChannelProposerTracker.setText(totaldate);
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

    @SuppressWarnings("deprecation")
    private void dismissProgressDialog() {
        dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
    }

    @SuppressWarnings("deprecation")
    private void showProgressDialog() {
        showDialog(DIALOG_DOWNLOAD_PROGRESS);
    }

    @SuppressWarnings("deprecation")
    private void showDateProgressDialog() {
        showDialog(DATE_DIALOG_ID);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnFromDateChannelProposerTrackerList:
                datecheck = 12;
                showDateProgressDialog();
                break;
            case R.id.btnToDateChannelProposerTrackerList:
                datecheck = 13;
                showDateProgressDialog();
                break;
            case R.id.btnOkChannelProposerList:
                getChannelProposalStatus();
                break;
            case R.id.btnResetChannelProposerList:
                resetChannelProposalStatus();
                break;
        }

    }

    private void resetChannelProposalStatus() {
        editTextFromdateChannelProposerTracker.setText("");
        editTextToDateChannelProposerTracker.setText("");
        spinnerChannelProposerTrackerList.setSelection(0);

        listViewChannelProposerStatus.setVisibility(View.GONE);
    }

    private void getChannelProposalStatus() {

        taskchannelProposalTrackerStatus = new DownloadFileAsyncChannelProposalTrackerStatus();
        listViewChannelProposerStatus.setVisibility(View.VISIBLE);

        // strcifnoforpolicylist = edpolicyCifNo.getText().toString();
        String channelProposaltrackerStatusFromDate = editTextFromdateChannelProposerTracker.getText().toString();
        String channelProposaltrackerStatusToDate = editTextToDateChannelProposerTracker.getText().toString();

        if (channelProposaltrackerStatusFromDate.equalsIgnoreCase("")
                || channelProposaltrackerStatusToDate.equalsIgnoreCase("")) {
            //validationAlert();
            mCommonMethods.showMessageDialog(context, mCommonMethods.ALL_FIELDS_REQUIRED_ALERT);
        } else {
            final SimpleDateFormat formatter = new SimpleDateFormat(
                    "dd-MMMM-yyyy");

            Date d1 = null;
            try {
                d1 = formatter.parse(channelProposaltrackerStatusFromDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Date d2 = null;
            try {
                d2 = formatter.parse(channelProposaltrackerStatusToDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
            long difference = Math.abs(d1.getTime() - d2.getTime());
            long differenceDates = difference / (24 * 60 * 60 * 1000);
            System.out.println("dayDifference:" + differenceDates);

            if (differenceDates <= 31) {

                if ((d2.after(d1)) || (d2.equals(d1))) {
                    if(mCommonMethods.isNetworkConnected(context)) {
                        String input = channelProposaltrackerStatusSpinner + "," + channelProposaltrackerStatusFromDate + "," +
                                channelProposaltrackerStatusToDate;

                        service_hits(input);
                    } else {
                        mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
                    }
                } else {
                    mCommonMethods.showMessageDialog(context, "To date should be greater than From date");
                }
            } else {
                mCommonMethods.showMessageDialog(context, "Difference between To date and From date should not be more than 1 month");
            }
        }
    }

    private void service_hits(String input) {

        service = new ServiceHits(context,
                METHOD_NAME_CHANNEL_PROPOSAL_TRACKER_LIST, input, strCIFBDMUserId,
                strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();
    }

    @Override
    public void downLoadData() {
        startDownloadchannelProposalTrackerStatus();
    }

    private void startDownloadchannelProposalTrackerStatus() {
        taskchannelProposalTrackerStatus = new DownloadFileAsyncChannelProposalTrackerStatus();
        taskchannelProposalTrackerStatus.execute("demo");
    }

    class DownloadFileAsyncChannelProposalTrackerStatus extends
            AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String strChannelProposalTrackerErrorCOde = "";
        private ArrayList<XMLChannelProposerTrackerStatusList> lstChannelProposerTrackerStatusXml;
        String editTextFromdateChannelProposerTracker_text, editTextToDateChannelProposerTracker_text;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
            editTextFromdateChannelProposerTracker_text = editTextFromdateChannelProposerTracker.getText().toString();
            editTextToDateChannelProposerTracker_text = editTextToDateChannelProposerTracker.getText().toString();
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;

                String sdate = editTextFromdateChannelProposerTracker_text;
                String edate = editTextToDateChannelProposerTracker_text;

                final SimpleDateFormat formatter = new SimpleDateFormat(
                        "dd-MMMM-yyyy");
                final SimpleDateFormat formatter1 = new SimpleDateFormat(
                        "MM/dd/yyyy");
                try {
                    Date dt = formatter.parse(sdate);
                    sdate = formatter1.format(dt);

                    Date dt1 = formatter.parse(edate);
                    edate = formatter1.format(dt1);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE,
                        METHOD_NAME_CHANNEL_PROPOSAL_TRACKER_LIST);

                request.addProperty("strCode", strCIFBDMUserId);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());
                request.addProperty("strType", channelProposaltrackerStatusSpinner);
                request.addProperty("strFromDate", sdate);
                request.addProperty("strToDate", edate);

                System.out.println("doinback:" + request.toString());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {

                    String SOAP_ACTION_CHANNEL_PROPOSAL_TRACKER_LIST = "http://tempuri.org/getChannelProposalStatus";
                    androidHttpTranport
                            .call(SOAP_ACTION_CHANNEL_PROPOSAL_TRACKER_LIST,
                                    envelope);
                    Object response = envelope.getResponse();
                    System.out.println("response:" + response.toString());
                    if (!response.toString().contentEquals("<PolicyDetails />")) {

                        SoapPrimitive sa;
                        try {
                            sa = (SoapPrimitive) envelope.getResponse();

                            String inputpolicylist = sa.toString();

                            if (!sa.toString().equalsIgnoreCase("")) {
                                ParseXML prsObj = new ParseXML();

                                inputpolicylist = prsObj.parseXmlTag(
                                        inputpolicylist, "PolicyDetails");
                                if (inputpolicylist != null) {

                                    /*
                                     * <PolicyDetails>
                                     * <Table>
                                     * <POLICYPROPOSALNUMBER>53AL912929</
                                     * POLICYPROPOSALNUMBER>
                                     * <PAYMENTAMOUNT>200000</PAYMENTAMOUNT>
                                     * <CASHIERINGDATE
                                     * >30-09-2016</CASHIERINGDATE> <STATUS>NON
                                     * MEDICAL</STATUS>
                                     * </Table>
                                     * </PolicyDetails>
                                     */

                                    lstChannelProposerTrackerStatusXml = new ArrayList<>();
                                    List<String> Node = prsObj.parseParentNode(
                                            inputpolicylist, "Table");

                                    List<XMLChannelProposerTrackerStatusList> nodeData = prsObj
                                            .parseNodeElement_ChannelProposertrackerStatusList(Node);

                                    lstChannelProposerTrackerStatusXml = new ArrayList<>();
                                    // lstPolicyList.clear();
                                    lstChannelProposerTrackerStatusXml.addAll(nodeData);

                                    adapterChannelProposalTracker = new SelectedAdapterChannelProposalTracker(
                                            context,
                                            lstChannelProposerTrackerStatusXml);
                                    adapterChannelProposalTracker
                                            .setNotifyOnChange(true);

                                    registerForContextMenu(listViewChannelProposerStatus);

                                    strChannelProposalTrackerErrorCOde = "success";

                                    System.out
                                            .println("strChannelProposalTrackerErrorCOde:"
                                                    + strChannelProposalTrackerErrorCOde);

                                } else {
                                    strChannelProposalTrackerErrorCOde = "1";
                                }
                            } else {
                                running = false;
                                mProgressDialog.dismiss();

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
                    } else {
                        mProgressDialog.dismiss();
                        running = false;
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

                if (strChannelProposalTrackerErrorCOde != null) {

                    if (strChannelProposalTrackerErrorCOde
                            .equalsIgnoreCase("success")) {
                        listViewChannelProposerStatus
                                .setAdapter(adapterChannelProposalTracker);
                        Utility.setListViewHeightBasedOnChildren(listViewChannelProposerStatus);

                        listViewChannelProposerStatus.getParent()
                                .requestChildFocus(
                                        listViewChannelProposerStatus,
                                        listViewChannelProposerStatus);


                    } else {
                        listViewChannelProposerStatus.setVisibility(View.GONE);
                        if (adapterChannelProposalTracker != null) {
                            adapterChannelProposalTracker.clear();
                        }
                        Toast.makeText(getApplicationContext(),
                                "No record found", Toast.LENGTH_LONG).show();
                    }
                } else {
                    listViewChannelProposerStatus.setVisibility(View.GONE);
                    if (adapterChannelProposalTracker != null) {
                        adapterChannelProposalTracker.clear();
                    }
                    Toast.makeText(getApplicationContext(),
                            "You are not authorised user", Toast.LENGTH_LONG)
                            .show();
                }
            } else {
                // servererror();
                listViewChannelProposerStatus.setVisibility(View.GONE);
                if (adapterChannelProposalTracker != null) {
                    adapterChannelProposalTracker.clear();
                }
                Toast.makeText(getApplicationContext(), "No record found",
                        Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (taskchannelProposalTrackerStatus != null) {
                taskchannelProposalTrackerStatus.cancel(true);
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

    class SelectedAdapterChannelProposalTracker extends ArrayAdapter<XMLChannelProposerTrackerStatusList> {

        private final List<XMLChannelProposerTrackerStatusList> lst;

        SelectedAdapterChannelProposalTracker(Context context,
                                              List<XMLChannelProposerTrackerStatusList> objects) {
            super(context, 0, objects);
            lst = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) this.getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.channel_proposal_tracker_status_listview_items, null);
            }

            TextView txtpropsalno = v
                    .findViewById(R.id.txtproposalnoCPTS);
            TextView paymentAmount = v.findViewById(R.id.txtPaymentAmtCPTS);
            TextView txtCashieringDate = v.findViewById(R.id.txtcashieringDateCPTS);
            TextView status = v.findViewById(R.id.txtstatusCPTS);

            txtpropsalno.setText(lst.get(position).getProposalNumber());
            paymentAmount.setText(lst.get(position).getPaymentAmount());
            txtCashieringDate.setText(lst.get(position).getCashieringDate());

            String statusString = lst.get(position).getStatus();
            status.setText(statusString);

            TextView txtpendingstatusChannelProposalStatus = v.findViewById(R.id.txtpendingstatusChannelProposalStatus);
            TableRow tablerowPendingstatusChannelProposalStatus = v.findViewById(R.id.tablerowPendingstatusChannelProposalStatus);
            if (statusString.equalsIgnoreCase("NON MEDICAL") || statusString.equalsIgnoreCase("MEDICAL")) {
                tablerowPendingstatusChannelProposalStatus.setVisibility(View.VISIBLE);
                txtpendingstatusChannelProposalStatus.setText(lst.get(position).getPendingStatus());
            } else {
                tablerowPendingstatusChannelProposalStatus.setVisibility(View.GONE);
            }
            return (v);
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

        if (taskchannelProposalTrackerStatus != null) {
            taskchannelProposalTrackerStatus.cancel(true);
        }
        if (service != null) {
            service.cancel(true);
        }

    }
}
