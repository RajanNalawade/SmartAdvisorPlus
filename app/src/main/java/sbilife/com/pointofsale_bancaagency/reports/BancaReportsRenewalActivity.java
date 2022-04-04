package sbilife.com.pointofsale_bancaagency.reports;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.mosambee.lib.ResultData;

import org.apache.commons.io.IOUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sbilife.com.pointofsale_bancaagency.CustomerDetailActivity;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolderRenewal;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.branchlocator.GPSTracker;
import sbilife.com.pointofsale_bancaagency.common.AppSharedPreferences;
import sbilife.com.pointofsale_bancaagency.common.BillDeskPayment;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods.UserDetailsValuesModel;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.reports.JarClientDemo.JARClassImplementation;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits.DownLoadData;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.FundValueAsyncTask;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.GetPremiumAmountCommonAsync;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.PolicyHolderLocatorMapFragmentActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.RevivalQuotationActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.SendRenewalSMSAsynTask;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.UpdateAltMobileNoCommonAsyncTask;

public class BancaReportsRenewalActivity extends AppCompatActivity implements
        OnClickListener, DownLoadData, UpdateAltMobileNoCommonAsyncTask.UpdateAltMobNoInterface {

    private final String METHOD_NAME_RENEWAL_LIST_UPDATE = "getAgentPoliciesRenewalListMonthwise";
    private final String METHOD_NAME_BRANCHWISE_RENEWAL_LIST_GIO = "getAgentPoliciesRenewalListMonthwise_gio";

    private final String NAMESPACE = "http://tempuri.org/";
    private final String URl = ServiceURL.SERVICE_URL;
    TextView textviewRenewalNote;
    ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> AgentPoliciesRenewalListMonthwiseGioList;
    private ProgressDialog mProgressDialog;
    private CommonMethods mCommonMethods;
    private Context context;
    private String strCIFBDMUserId = "";
    private String strCIFBDMEmailId = "";
    private String strCIFBDMPassword = "";
    private String strCIFBDMMObileNo = "", str_amount = "";
    private String strFilterClickAction;

    private DownloadFileAsyncRenewal_update taskRenewal_update;
    private long lstRevivalListCount1 = 0;

    private Spinner spnRewmonths;

    private TextView txterrordescrevival1, txtrevivallistcount1, textviewSearchHint;

    private RecyclerView recyclerview;
    private SelectedAdapter selectedAdapter;

    private ArrayList<XMLHolderRenewal> globalDataList;
    private EditText edittextSearch;
    private GetProbableCommissionAsyncTask getProbableCommissionAsyncTask;
    private ServiceHits service;
    private DownloadBranchWiseRenewalListAsync downloadBranchWiseRenewalListAsync;
    private ParseXML objParse;
    private GPSTracker gpsTracker;
    private LatLng latLng;
    private AsyncCheckPaymentStatus asyncCheckPaymentStatus;
    private String TransationId, str_Status = "", str_Error = "";
    private AlertDialog alertDialog;
    private String selectedPolicyNumber = "";
    private FundValueAsyncTask fundValueAsyncTask;
    private GetPremiumAmountAsync getPremiumAmountAsync;
    private SendRenewalSMSAsynTask sendRenewalSMSAsynTask;
    private GetPremiumAmountCommonAsync getPremiumAmountCommonAsync;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.banca_reports_renewal);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        context = this;
        mCommonMethods = new CommonMethods();
        objParse = new ParseXML();
        gpsTracker = new GPSTracker(this);

        mCommonMethods.setApplicationToolbarMenu(this, "Renewal Due List");

        taskRenewal_update = new DownloadFileAsyncRenewal_update();

        textviewSearchHint = findViewById(R.id.textviewSearchHint);
        edittextSearch = findViewById(R.id.edittextSearch);
        textviewRenewalNote = findViewById(R.id.textviewRenewalNote);

        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setVisibility(View.VISIBLE);
        //lstReq.setHasFixedSize(true);
        // set a LinearLayoutManager with default horizontal orientation and false value for reverseLayout to show the items from start to end
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        // set LayoutManager to RecyclerView
        recyclerview.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        globalDataList = new ArrayList<>();
        selectedAdapter = new SelectedAdapter(globalDataList);
        recyclerview.setAdapter(selectedAdapter);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        spnRewmonths = findViewById(R.id.spnRewmonths);

        txterrordescrevival1 = findViewById(R.id.txterrordescrevival1);
        txtrevivallistcount1 = findViewById(R.id.txtrevivallistcount1);

        Button btn_ren = findViewById(R.id.btn_ren);

        btn_ren.setOnClickListener(this);

        Button gotoGeoLocationTxt = findViewById(R.id.gotoGeoLocationTxt);
        gotoGeoLocationTxt.setOnClickListener(this);


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

        mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        mProgressDialog.setButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (taskRenewal_update != null) {
                            taskRenewal_update.cancel(true);
                        }
                        if (asyncCheckPaymentStatus != null) {
                            asyncCheckPaymentStatus.cancel(true);
                        }
                        if (fundValueAsyncTask != null) {
                            fundValueAsyncTask.cancel(true);
                        }
                        if (getPremiumAmountAsync != null) {
                            getPremiumAmountAsync.cancel(true);
                        }
                        if (sendRenewalSMSAsynTask != null) {
                            sendRenewalSMSAsynTask.cancel(true);
                        }
                        if (getPremiumAmountCommonAsync != null) {
                            getPremiumAmountCommonAsync.cancel(true);
                        }
                        if (mProgressDialog != null) {
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }
                    }
                });

        mProgressDialog.setMax(100);

        edittextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                selectedAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private void service_hits() {

        ServiceHits service = new ServiceHits(context,
                METHOD_NAME_RENEWAL_LIST_UPDATE, spnRewmonths.getSelectedItem().toString(), strCIFBDMUserId,
                strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void getUserDetails() {
        UserDetailsValuesModel userDetailsValuesModel = mCommonMethods
                .setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }

    @Override
    public void onClick(View arg0) {

        int id = arg0.getId();
        switch (id) {
            case R.id.btn_ren:
                getRenewalData();
                break;
            case R.id.gotoGeoLocationTxt:
                //mCommonMethods.callActivity(context,BancaReportsPolicyListActivity.class);
                strFilterClickAction = "GeoLocationBtnClicked";
                StringBuilder input = new StringBuilder();
                input.append(strCIFBDMUserId + "," + strCIFBDMEmailId + "," + strCIFBDMMObileNo);

                input.append(",").append(spnRewmonths.getSelectedItem().toString());
                service_hits(input.toString());
                break;
        }
    }

    private void getRenewalData() {

        taskRenewal_update = new DownloadFileAsyncRenewal_update();

        txterrordescrevival1.setVisibility(View.VISIBLE);
        txtrevivallistcount1.setVisibility(View.VISIBLE);

        edittextSearch.setVisibility(View.GONE);
        textviewSearchHint.setVisibility(View.GONE);
        textviewRenewalNote.setVisibility(View.GONE);
        txtrevivallistcount1.setText("");
        selectedPolicyNumber = "";
        clearList();

        if (mCommonMethods.isNetworkConnected(context)) {
            service_hits();
        } else {
            mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (taskRenewal_update != null) {
                taskRenewal_update.cancel(true);
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


    private void startDownloadRevivalList1() {
        taskRenewal_update = new DownloadFileAsyncRenewal_update();
        taskRenewal_update.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "demo");
    }

    @Override
    public void downLoadData() {
        if (strFilterClickAction != null && strFilterClickAction.equals("GeoLocationBtnClicked")) {
            downloadBranchWiseRenewalListAsync = new DownloadBranchWiseRenewalListAsync();
            downloadBranchWiseRenewalListAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            startDownloadRevivalList1();
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

        if (taskRenewal_update != null) {
            taskRenewal_update.cancel(true);
        }
        if (asyncCheckPaymentStatus != null) {
            asyncCheckPaymentStatus.cancel(true);
        }

        if (fundValueAsyncTask != null) {
            fundValueAsyncTask.cancel(true);
        }

        if (getPremiumAmountAsync != null) {
            getPremiumAmountAsync.cancel(true);
        }

        if (sendRenewalSMSAsynTask != null) {
            sendRenewalSMSAsynTask.cancel(true);
        }
        if (getPremiumAmountCommonAsync != null) {
            getPremiumAmountCommonAsync.cancel(true);
        }

        if (service != null) {
            service.cancel(true);
        }
    }

    private void clearList() {
        if (globalDataList != null && selectedAdapter != null) {
            globalDataList.clear();
            selectedAdapter = new SelectedAdapter(globalDataList);
            recyclerview.setAdapter(selectedAdapter);
            recyclerview.invalidate();
        }
    }

    private void service_hits(String input) {
        LocationManager locationmanager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);

        if (locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            latLng = mCommonMethods.getCurrentLocation(context, gpsTracker);
            service = new ServiceHits(context, METHOD_NAME_BRANCHWISE_RENEWAL_LIST_GIO, input,
                    strCIFBDMUserId, strCIFBDMEmailId,
                    strCIFBDMMObileNo, strCIFBDMPassword, this);
            service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            mCommonMethods.showGPSDisabledAlertToUser(context);
        }
    }


    public String getDistance(final double lat1, final double lon1, final double lat2, final double lon2) {
        String parsedDistance = "";
        String response;
        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/directions/json?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&sensor=false&units=metric&mode=driving&&key=AIzaSyCqIpNkXExQ86zMXx7h_9txgsG70naoG2Y");
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = IOUtil.toString(in, "UTF-8");
            //Log.d("doInBackground", "doInBackground: "+response);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray array = jsonObject.getJSONArray("routes");
            //Log.d("doInBackground", "doInBackground: "+array.toString());
            JSONObject routes = array.getJSONObject(0);
            JSONArray legs = routes.getJSONArray("legs");
            JSONObject steps = legs.getJSONObject(0);
            JSONObject distance = steps.getJSONObject("distance");
            parsedDistance = distance.getString("text");
        } catch (Exception e) {
            e.printStackTrace();
            parsedDistance = "";
        }


        return parsedDistance;
    }


    void connectToMPOS(int index, String premiumPayable, JARClassImplementation.TransactionResultInterface classContext,
                       FrameLayout container, String mobileNumber, String emailId) {


        String message = "I agree to the following terms and conditions for use of the Mobile Point of Sale (MPOS) device supplied to me:" +
                "<br><br>1. I understand that the MPOS device provided to me is the property of SBI LIFE INSURANCE CO LTD and has been provided to me to facilitate collection of monies against SBI LIFE policies and proposals sourced by me." +
                "<br><br>2. I understand that the device is provided for my exclusive use and is NOT transferable. I agree that I shall not share the device and the user credentials with anyone else and will always keep it in my possession." +
                "<br><br>3. I agree that I shall be responsible for any loss caused to SBI Life due to the misuse or Loss of the device whilst in my custody and undertake to make good any such loss." +
                "<br><br>4. I undertake to report the loss, misuse or malfunction of the device to  SBI Life office immediately on being detected." +
                "<br><br>5. In case of loss  or unauthorised use of the device whilst in my custody, I authorize SBI Life to recover the cost of the device and/or the loss resulting from its misuse from my commission or other monies payable to me by SBI Life and in case such amounts are insufficient to recover the loss suffered by SBI Life, I will pay the said amount immediately upon receipt of notice from SBI Life and in case I fail to pay, I understand and agree that SBI Life shall be at liberty to initiate necessary legal proceedings against me at my cost." +
                "<br><br>6. I understand that payments against a policy or proposal are to be collected from the Card or Bank account or Wallet of the policy owner OR from acceptable Third Party like spouse and parents ,in accordance with IRDAI Master Circular in AML/CFT for Life Insurers" +
                "<br><br>7. I understand that the payment has to be collected in compliance with Section 64VB of Insurance Act, 1938 and IRDA (Manner of Receipt of Premium) Regulations, 2002." +
                "<br><br>8. I undertake that I will NOT accept payments from unrelated third parties including my own bank account or credit card or wallet for premium collection on policies or proposals belonging to someone else." +
                "<br><br>9. I agree that SBI Life reserves the right to withdraw the facility provided to me at its sole discretion and without ascribing any reason and without any prior notice notice." +
                "<br><br>10. I undertake to return the device to the nearest SBI Life Branch in a good working condition 5 days of being so advised, whenever I am called upon to do so by SBI Life." +

                "<br><br>11. I undertake to return the device to the respective business owner/ asset owner upon my termination i.e. on my last working day in a good working condition." +
                "<br><br>12. I undertake to adhere to SBI LIFE's relevant security policies and standards from time-to-time." +

                "<br>";


        String disclaimer = AppSharedPreferences.getData(context, (mCommonMethods.getMPOSDisclaimer()), "");
        //String mobileNo = lstAdapterList.get(index).getCONTACTMOBILE();

        final JARClassImplementation jarClass = new JARClassImplementation();
        jarClass.setContext(context);
        jarClass.setActivity((AppCompatActivity) context);

                        /*jarClass.startProcess("", "1234", "SALE", holder.container,
                                "1KNA123456", String.format("%.2f", "1"),classContext);*/
        //final String mobileNo = "9768693970";
        //String emailId = "machindra.yewale91@gmail.com";
        //String policyNumber = "1KNA123456";
        //String premiumAmount = "1";
        //String password = "1234";//stag
        //String password = "8547";//stag
        String password = "7559";//prod
        AlertDialog.Builder showAlert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
        showAlert.setTitle("Disclaimer");
        showAlert.setMessage(Html.fromHtml(message));
        showAlert.setPositiveButton("Accept", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //When user accepts the disclaimer store value in share preferences as true.
                String disclaimerText = strCIFBDMUserId;
                AppSharedPreferences.setData(context, mCommonMethods.getMPOSDisclaimer(), disclaimerText);
                if (alertDialog.isShowing())
                    alertDialog.dismiss();

                if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                    mProgressDialog.show();
                }
                jarClass.startProcess(strCIFBDMUserId, password, "SALE", container,
                        selectedPolicyNumber, premiumPayable, classContext, mobileNumber, "");

            }
        });


        showAlert.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (alertDialog.isShowing())
                    alertDialog.dismiss();
            }
        });
        showAlert.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
                if (alertDialog.isShowing())
                    alertDialog.dismiss();
                return false;
            }
        });
        alertDialog = showAlert.create();
        if (TextUtils.isEmpty(disclaimer)) {
            alertDialog.show();
        } else if (!disclaimer.equalsIgnoreCase(strCIFBDMUserId)) {
            alertDialog.show();
        } else {
            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
            jarClass.startProcess(strCIFBDMUserId, password, "SALE", container,
                    selectedPolicyNumber, premiumPayable, classContext, mobileNumber, "");
        }
    }

    public void getFundValueInterfaceMethod(List<String> Node, String policyNumber) {

        if (TextUtils.isEmpty(policyNumber)) {
            mCommonMethods.showMessageDialog(context, mCommonMethods.NO_RECORD_FOUND);
        } else {
            String result = "", fundValueStr = "";
            ParseXML parseXML = new ParseXML();
            List<ParseXML.XMLFundSwitchHolder> nodeData = parseXML.parseNodeElementFundSwitch(Node);
            for (ParseXML.XMLFundSwitchHolder node : nodeData) {
                System.out.println("sa.toString() = " + policyNumber);
                if (policyNumber.equalsIgnoreCase(node.getPOLICYNO())) {
                    fundValueStr = node.getFUNDVALUE();
                    System.out.println("sa.toString() = " + fundValueStr);
                    result = "Success";
                }
            }

            if (result.equalsIgnoreCase("Success")) {
                mCommonMethods.showMessageDialog(context, "Fund Value Is : " + fundValueStr);
            } else {
                mCommonMethods.showMessageDialog(context, mCommonMethods.NO_RECORD_FOUND);
            }
        }
    }

    void getSMSDetailsInterfaceMethod(String result) {
        if (result.equalsIgnoreCase("1")) {
            mCommonMethods.showMessageDialog(context, "Message sent successfully");
        } else {
            mCommonMethods.showMessageDialog(context, "Message sending failed");
        }
    }

    void getPremiumInterfaceMethod(String premiumAmount, String result) {
        if (premiumAmount.equals("")) {
            mCommonMethods.showMessageDialog(context, result);
        } else {
            mCommonMethods.showMessageDialog(context, "Gross Premium Amount is - " + premiumAmount);
        }
    }

    public void getUpdateAltMobResultMethod(String result) {
        if (result != null && result.equalsIgnoreCase("Success")) {
            mCommonMethods.showMessageDialog(context, "Mobile Number Updated Successfully");
        } else {
            mCommonMethods.showMessageDialog(context, "Mobile Number Not Updated.Please Try Again.");
        }
    }

    class DownloadFileAsyncRenewal_update extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String strRevivalListErrorCOde1 = "", spnRewmonths_text, toreqdt = "", frreqdt = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

            spnRewmonths_text = spnRewmonths.getSelectedItem().toString();

            SimpleDateFormat format1 = new SimpleDateFormat("MM-dd-yyyy");

            if (spnRewmonths_text.contentEquals("Previous Month")) {
                Calendar aCalendar = Calendar.getInstance();
                aCalendar.set(Calendar.DATE, 1);
                aCalendar.add(Calendar.DAY_OF_MONTH, -1);
                Date lastDateOfPreviousMonth = aCalendar.getTime();
                toreqdt = format1.format(lastDateOfPreviousMonth);

                aCalendar.set(Calendar.DATE, 1);
                aCalendar.add(Calendar.MONTH, -5);
                Date firstDateOfPreviousMonth = aCalendar.getTime();
                frreqdt = format1.format(firstDateOfPreviousMonth);
            } else if (spnRewmonths_text.contentEquals("Current Month")) {
                Calendar cale = Calendar.getInstance();
                cale.set(Calendar.DATE,
                        cale.getActualMaximum(Calendar.DATE));
                Date lastDateOfCurrentMonth = cale.getTime();
                toreqdt = format1.format(lastDateOfCurrentMonth);
                cale.set(Calendar.DATE,
                        cale.getActualMinimum(Calendar.DATE));
                Date firstDateOfCurrentMonth = cale.getTime();
                frreqdt = format1.format(firstDateOfCurrentMonth);
            } else if (spnRewmonths_text.contentEquals("Next Month")) {
                Calendar calen = Calendar.getInstance();
                // calen.set(Calendar.MONTH, calen.get(Calendar.MONTH));
                calen.add(Calendar.MONTH, +1);
                calen.set(Calendar.DATE, calen.getActualMaximum(Calendar.DATE));
                Date lastDateOfNextMonth = calen.getTime();
                toreqdt = format1.format(lastDateOfNextMonth);

                calen.set(Calendar.DATE, calen.getActualMinimum(Calendar.DATE));

                Date firstDateOfNextMonth = calen.getTime();
                frreqdt = format1.format(firstDateOfNextMonth);
            }

        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_RENEWAL_LIST_UPDATE);
                request.addProperty("strAgentNo", strCIFBDMUserId);
                request.addProperty("strFromReqDate", frreqdt);
                request.addProperty("strToReqDate", toreqdt);
                /*request.addProperty("strFromReqDate", "01-01-2018");
                request.addProperty("strToReqDate", "08-30-2018");*/
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION_RENEWAL_LIST_UPDATE = "http://tempuri.org/getAgentPoliciesRenewalListMonthwise";
                androidHttpTranport.call(SOAP_ACTION_RENEWAL_LIST_UPDATE,
                        envelope);

                Object response = envelope.getResponse();

                if (!response.toString().contentEquals("anyType{}")) {

                    SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();


                    String inputpolicylist = sa.toString();
                    System.out.println("inputpolicylist = " + inputpolicylist);

                    ParseXML prsObj = new ParseXML();

                    inputpolicylist = prsObj.parseXmlTag(
                            inputpolicylist, "CIFPolicyList");

                    strRevivalListErrorCOde1 = new ParseXML().parseXmlTag(
                            inputpolicylist, "ScreenData");

                    if (strRevivalListErrorCOde1 == null) {

                        List<String> Node = prsObj.parseParentNode(
                                inputpolicylist, "Table");

                        List<XMLHolderRenewal> nodeData = prsObj
                                .parseNodeElementRenewal(Node);


                        globalDataList = new ArrayList<>();
                        globalDataList.clear();

                        globalDataList = new ArrayList<>(nodeData);
                        lstRevivalListCount1 = globalDataList.size();

                    }
                }


            } catch (Exception e) {
                mProgressDialog.dismiss();
                running = false;
            }
            return null;

        }


        @Override
        protected void onPostExecute(String unused) {

            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            edittextSearch.setVisibility(View.GONE);
            textviewSearchHint.setVisibility(View.GONE);
            textviewRenewalNote.setVisibility(View.GONE);

            if (running) {
                textviewRenewalNote.setVisibility(View.VISIBLE);

                textviewRenewalNote.setText("");
                if (strRevivalListErrorCOde1 == null) {
                    if (lstRevivalListCount1 != 0) {
                        edittextSearch.setVisibility(View.VISIBLE);
                        textviewSearchHint.setVisibility(View.VISIBLE);
                    }

                    txterrordescrevival1.setText("");
                    txtrevivallistcount1.setText("Total Policy : "
                            + lstRevivalListCount1);

                    String note = mCommonMethods.RENEWAL_NOTE;
                    textviewRenewalNote.setText(Html.fromHtml(note));
                    textviewRenewalNote.setFocusable(true);

                    /*RevivallistView11.setAdapter(selectedAdapterRenewal);
                    RevivallistView11.setVisibility(View.VISIBLE);
                    Utility.setListViewHeightBasedOnChildren(RevivallistView11);*/


                    selectedAdapter = new SelectedAdapter(globalDataList);
                    recyclerview.setAdapter(selectedAdapter);
                    recyclerview.invalidate();
                } else {

                    txterrordescrevival1.setText("No Record Found");
                    txtrevivallistcount1.setText("Total Policy : " + 0);
                    /*List<XMLHolderRenewal> lst;
                    XMLHolderRenewal node = null;
                    lst = new ArrayList<>();
                    lst.clear();
                    lst.add(node);
                    selectedAdapterRenewal = new SelectedAdapterRenewal(
                            context, 0, lst);
                    selectedAdapterRenewal.setNotifyOnChange(true);
                    RevivallistView11.setAdapter(selectedAdapterRenewal);
                    RevivallistView11.setVisibility(View.GONE);*/


                    clearList();
                }
            } else {
                mCommonMethods.showMessageDialog(context, mCommonMethods.NO_RECORD_FOUND);
            }
        }
    }

    class GetProbableCommissionAsyncTask extends AsyncTask<String, String, String> {

        private final String policyNumber;
        private volatile boolean running = true;
        private String errorString = "";
        private String CMS_COMM_AMT = "";

        GetProbableCommissionAsyncTask(String policyNumber) {
            this.policyNumber = policyNumber;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                String METHOD_NAME_PROBABLE_COMMISSION = "getProbableComm_smrt";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_PROBABLE_COMMISSION);
                request.addProperty("strPolNo", policyNumber);


                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME_PROBABLE_COMMISSION;
                androidHttpTranport.call(SOAP_ACTION, envelope);

                Object response = envelope.getResponse();

                if (!response.toString().contentEquals("0")) {

                    SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();

                    String inputpolicylist = sa.toString();

                    ParseXML prsObj = new ParseXML();

                    inputpolicylist = prsObj.parseXmlTag(
                            inputpolicylist, "Data");

                    errorString = new ParseXML().parseXmlTag(
                            inputpolicylist, "ScreenData");

                    if (errorString == null) {

                        String Node = prsObj.parseXmlTag(inputpolicylist, "Table");

                        CMS_COMM_AMT = prsObj.parseXmlTag(Node, "CMS_COMM_AMT");

                    }
                }


            } catch (Exception e) {
                mProgressDialog.dismiss();
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

                if (errorString == null) {
                    mCommonMethods.showMessageDialog(context, "Probable Commission Amount "
                            + getString(R.string.Rs) + CMS_COMM_AMT);
                } else {
                    mCommonMethods.showMessageDialog(context, mCommonMethods.NO_RECORD_FOUND);
                }
            } else {
                mCommonMethods.showMessageDialog(context, mCommonMethods.NO_RECORD_FOUND);
            }
        }
    }

    class SendRenewalDueEmailAsyncTask extends AsyncTask<String, String, String> {

        private final String policyNumber;
        private final String emailId;
        private final String dueDate;
        private final String amount;
        private final String mode;
        private final String name;
        private volatile boolean running = true;
        private String response = "";

        SendRenewalDueEmailAsyncTask(String policyNumber, String emailId, String dueDate, String amount, String mode,
                                     String name) {
            this.policyNumber = policyNumber;
            this.emailId = emailId;
            this.dueDate = dueDate;
            this.amount = amount;
            this.mode = mode;
            this.name = name;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                //SendRenewalDueEmail_SMRT(string strPolicyNo, string strEmail, string strDueDate, string strAmt,
                // string strMode, string Name, string strEmailId, string strMobileNo, string strAuthKey)
                String METHOD_NAME_SEND_EMAIL = "SendRenewalDueEmail_SMRT";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SEND_EMAIL);
                request.addProperty("strPolicyNo", policyNumber);
                request.addProperty("strEmail", emailId);
                request.addProperty("strDueDate", dueDate);
                request.addProperty("strAmt", amount);
                request.addProperty("strMode", mode);
                request.addProperty("Name", name);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", mCommonMethods.getStrAuth());

                Log.d("doInBackground", "doInBackground: " + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME_SEND_EMAIL;
                androidHttpTranport.call(SOAP_ACTION, envelope);

                SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();
                response = sa.toString();
                if (response.equalsIgnoreCase("1")) {
                    response = "success";
                } else {
                    response = "";
                }
            } catch (Exception e) {
                mProgressDialog.dismiss();
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
                if (response.equalsIgnoreCase("success")) {
                    mCommonMethods.showMessageDialog(context, "Email sent successfully");
                } else {
                    mCommonMethods.showMessageDialog(context, "Email sending failed");
                }
            } else {
                mCommonMethods.showMessageDialog(context, "Email sending failed");
            }
        }
    }

    public class SelectedAdapter extends RecyclerView.Adapter<SelectedAdapter.
            ViewHolderAdapter> implements Filterable, JARClassImplementation.TransactionResultInterface {

        private final JARClassImplementation.TransactionResultInterface classContext;
        private ArrayList<XMLHolderRenewal> lstAdapterList, lstSearch;
        private int finalPosition = 0;

        SelectedAdapter(ArrayList<XMLHolderRenewal> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
            classContext = this;
        }


        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<XMLHolderRenewal> results = new ArrayList<>();

                    if (lstSearch == null)
                        lstSearch = lstAdapterList;

                    String strSearch = charSequence == null ? "" : charSequence.toString();

                    if (!strSearch.equals("")) {
                        if (lstSearch != null && lstSearch.size() > 0) {
                            for (final XMLHolderRenewal model : lstSearch) {
                                if (model.getNo().toLowerCase().contains(strSearch.toLowerCase())
                                        || model.getFName().toLowerCase().contains(strSearch.toLowerCase())
                                        || model.getPremiumUp().toLowerCase().contains(strSearch.toLowerCase())
                                        || model.getPremiumAmt().toLowerCase().contains(strSearch.toLowerCase())
                                        || model.getStatus().toLowerCase().contains(strSearch.toLowerCase())) {
                                    results.add(model);
                                }
                            }
                        }
                        oReturn.values = results;
                    } else {
                        oReturn.values = globalDataList;
                    }
                    return oReturn;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    lstAdapterList = (ArrayList<XMLHolderRenewal>) results.values;
                    selectedAdapter = new SelectedAdapter(lstAdapterList);
                    recyclerview.setAdapter(selectedAdapter);
                    lstSearch = null;

                    notifyDataSetChanged();
                }
            };
        }


        @Override
        public int getItemCount() {
            return lstAdapterList.size();
        }

        @Override
        public ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_item_renewallist, parent, false);

            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final ViewHolderAdapter holder, int position) {

            try {

                if (lstAdapterList.get(position).getStatus().contentEquals("Inforce")) {
                    holder.status.setBackgroundColor(Color.parseColor("#008000"));
                } else if (lstAdapterList.get(position).getStatus().contentEquals("Lapse")) {
                    holder.status.setBackgroundColor(Color.parseColor("#FFA500"));
                } else if (lstAdapterList.get(position).getStatus()
                        .contentEquals("Technical Lapse")) {
                    holder.status.setBackgroundColor(Color.parseColor("#FFFF00"));
                } else if (lstAdapterList.get(position).getStatus()
                        .contentEquals("Maturity")) {
                    holder.status.setBackgroundColor(Color.parseColor("#0000FF"));
                } else if (lstAdapterList.get(position).getStatus().contentEquals("Claim")) {
                    holder.status.setBackgroundColor(Color.parseColor("#FFC0CB"));
                } else if (lstAdapterList.get(position).getStatus()
                        .contentEquals("Surrender")) {
                    holder.status.setBackgroundColor(Color.parseColor("#FF0000"));
                }
                if (!lstAdapterList.get(position).getStatus().equalsIgnoreCase("Lapse") &&
                        !lstAdapterList.get(position).getStatus().equalsIgnoreCase("Technical Lapse")) {
                    holder.premiumPaymentOptionsParent.setVisibility(View.VISIBLE);
                } else if (lstAdapterList.get(position).getStatus().equalsIgnoreCase("Lapse") ||
                        lstAdapterList.get(position).getStatus().equalsIgnoreCase("Technical Lapse")) {
                    holder.premiumPaymentOptionsParent.setVisibility(View.GONE);
                }

                holder.txtno.setText(lstAdapterList.get(position).getNo() == null ? "" : lstAdapterList.get(
                        position).getNo());
                holder.txtcustomercode
                        .setText(lstAdapterList.get(position).getHolderId() == null ? ""
                                : lstAdapterList.get(position).getHolderId());

                String fnm = lstAdapterList.get(position).getFName() == null ? "" : lstAdapterList
                        .get(position).getFName();
                String lnm = lstAdapterList.get(position).getLName() == null ? "" : lstAdapterList
                        .get(position).getLName();
                final String name = fnm + " " + lnm;

                holder.fname.setText(name);

                String status = lstAdapterList.get(position).getStatus() == null ? "" : lstAdapterList
                        .get(position).getStatus();

                holder.status.setText(status);

                String premiumFUP = lstAdapterList.get(position).getPremiumUp() == null ? ""
                        : lstAdapterList.get(position).getPremiumUp();

                holder.premiumup.setText(premiumFUP);
                holder.premiumamt
                        .setText(lstAdapterList.get(position).getPremiumAmt() == null ? ""
                                : lstAdapterList.get(position).getPremiumAmt());

                Date premiumFUPDate = new Date(premiumFUP);
                Date today = new Date();
                long diff = premiumFUPDate.getTime() - today.getTime();
                int numOfDays = (int) (diff / (1000 * 60 * 60 * 24));
                System.out.println("numOfDays = " + numOfDays);
                if ((status.equalsIgnoreCase("inforce") || status.equalsIgnoreCase("Technical Lapse"))
                        && numOfDays < 30) {
                    holder.imageviewSMS.setVisibility(View.VISIBLE);
                } else {
                    holder.imageviewSMS.setVisibility(View.INVISIBLE);
                }
                holder.txtrenewalpaytype
                        .setText(lstAdapterList.get(position).getPayType() == null ? ""
                                : lstAdapterList.get(position).getPayType());
                holder.textviewMobileNumber.setText(lstAdapterList.get(position).getCONTACTMOBILE() == null ? ""
                        : lstAdapterList.get(position).getCONTACTMOBILE());


                if (TextUtils.isEmpty(lstAdapterList.get(position).getCONTACTOFFICE())) {
                    holder.trOfficeContactMaster.setVisibility(View.GONE);
                } else {
                    holder.trOfficeContactMaster.setVisibility(View.VISIBLE);
                    holder.textviewContactOffice.setText(lstAdapterList.get(position).getCONTACTOFFICE());

                    holder.textviewContactOffice.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String mobileNumber = holder.textviewContactOffice.getText().toString();
                            if (!TextUtils.isEmpty(mobileNumber)) {
                                mCommonMethods.callMobileNumber(mobileNumber, context);
                            }
                        }
                    });
                    holder.LLofficeContactImage.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String mobileNumber = holder.textviewContactOffice.getText().toString();
                            if (!TextUtils.isEmpty(mobileNumber)) {
                                mCommonMethods.callMobileNumber(mobileNumber, context);
                            }
                        }
                    });

                }

                if (lstAdapterList.get(position).getPOLICYTYPE().equalsIgnoreCase("ULIP")) {
                    holder.trFundValue.setVisibility(View.VISIBLE);
                    holder.buttonFundValue.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            mCommonMethods.hideKeyboard(edittextSearch, context);
                            int index = holder.getAdapterPosition();
                            String holderId = lstAdapterList.get(index).getHolderId();
                            String policyNumber = lstAdapterList.get(index).getNo();
                            //fundValueAsyncTask = new FundValueAsyncTask(holderId, policyNumber);
                            fundValueAsyncTask = new FundValueAsyncTask(context, holderId, policyNumber,
                                    BancaReportsRenewalActivity.this::getFundValueInterfaceMethod);
                            fundValueAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                    });
                } else {
                    holder.trFundValue.setVisibility(View.GONE);
                }

                holder.tvDOC.setText(lstAdapterList.get(position).getPOLICYRISKCOMMENCEMENTDATE());
                holder.tvPaymentMechanism.setText(lstAdapterList.get(position).getPOLICYPAYMENTMECHANISM());
                holder.buttonCRM.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        mCommonMethods.hideKeyboard(edittextSearch, context);
                        int index = holder.getAdapterPosition();
                        //showDispositionAlert(lstAdapterList.get(index));
                        final String premiumFUP = lstAdapterList.get(index).getPremiumUp();
                        mCommonMethods.showDispositionAlert(context, premiumFUP, lstAdapterList.get(index).getNo(),
                                strCIFBDMEmailId, strCIFBDMMObileNo, strCIFBDMUserId);

                    }
                });

                holder.buttonUpdatAltMobile.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        mCommonMethods.hideKeyboard(edittextSearch, context);
                        int index = holder.getAdapterPosition();
                        //showDispositionAlert(lstAdapterList.get(index));
                        final String premiumFUP = lstAdapterList.get(index).getPremiumUp();
                        final String policyNumber = lstAdapterList.get(index).getNo();
                        mCommonMethods.updateAltMobileAlert(context, premiumFUP, policyNumber,
                                BancaReportsRenewalActivity.this);

                    }
                });

                holder.buttonProbableCommission.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCommonMethods.hideKeyboard(edittextSearch, context);
                        int index = holder.getAdapterPosition();
                        String policyNumber = lstAdapterList.get(index).getNo();
                        getProbableCommissionAsyncTask = new GetProbableCommissionAsyncTask(policyNumber);
                        getProbableCommissionAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                });
                holder.tvActualPremium.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCommonMethods.hideKeyboard(edittextSearch, context);
                        int index = holder.getAdapterPosition();
                        selectedPolicyNumber = lstAdapterList.get(index).getNo();
                        if (lstAdapterList.get(position).getStatus().equalsIgnoreCase("Lapse") ||
                                lstAdapterList.get(position).getStatus().equalsIgnoreCase("Technical Lapse")) {
                            getPremiumAmountCommonAsync = new GetPremiumAmountCommonAsync(selectedPolicyNumber, context,
                                    BancaReportsRenewalActivity.this::getPremiumInterfaceMethod);
                            getPremiumAmountCommonAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            String msg = "Gross Premium Amount is - " + lstAdapterList.get(index).getPremiumAmt();
                            mCommonMethods.showMessageDialog(context, msg);
                        }
                    }
                });


                holder.imgcontact_cust_r.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mobileNumber = holder.textviewMobileNumber.getText().toString();

                        if (!TextUtils.isEmpty(mobileNumber)) {
                            mCommonMethods.callMobileNumber(mobileNumber, context);
                        }
                    }
                });
                holder.textviewMobileNumber.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mobileNumber = holder.textviewMobileNumber.getText().toString();
                        if (!TextUtils.isEmpty(mobileNumber)) {
                            mCommonMethods.callMobileNumber(mobileNumber, context);
                        }
                    }
                });

                holder.imageviewSMS.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mCommonMethods.hideKeyboard(edittextSearch, context);


                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                        builder.setTitle("Choose Communication Medium");
                        finalPosition = 0;
                        //final String[] languagesArray = {"English", "Hindi", "Telugu"};
//                    final String[] languagesArray = {"English"};
                        final String[] commMediumArray = {"SMS", "Email"};
                        // cow
                        builder.setSingleChoiceItems(commMediumArray, finalPosition, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finalPosition = which;
                            }
                        });

                        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String commMedium = commMediumArray[finalPosition];
                                if (mCommonMethods.isNetworkConnected(context)) {
                                    int index = holder.getAdapterPosition();
                                    if (commMedium.equalsIgnoreCase("SMS")) {

                                        final String mobileNumber = holder.textviewMobileNumber.getText().toString();
                                        final String policyNumber = lstAdapterList.get(index).getNo();

                                        final String dueDate = lstAdapterList.get(index).getPremiumUp();
                                        final String status = lstAdapterList.get(index).getStatus();
                                        final String amount = lstAdapterList.get(index).getPremiumAmt();

                                    /*SendSmsAsync sendSmsAsync = new SendSmsAsync(policyNumber, mobileNumber, dueDate,
                                            status, amount, "English");
                                    sendSmsAsync..executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/

                                        sendRenewalSMSAsynTask = new SendRenewalSMSAsynTask(policyNumber, mobileNumber, dueDate,
                                                status, amount, "English",
                                                context, BancaReportsRenewalActivity.this::getSMSDetailsInterfaceMethod);
                                        sendRenewalSMSAsynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                    } else if (commMedium.equalsIgnoreCase("Email")) {
                                        final String dueDate = lstAdapterList.get(index).getPremiumUp();
                                        final String paymentMechanism = lstAdapterList.get(index).getPOLICYPAYMENTMECHANISM();
                                        final String policyNumber = lstAdapterList.get(index).getNo();
                                        final String emailid = lstAdapterList.get(index).getEMAILID();
                                        final String amount = lstAdapterList.get(index).getPremiumAmt();
                                        final String name = lstAdapterList.get(index).getFName() + " " +
                                                lstAdapterList.get(index).getLName();

                                        String mode = "";

                                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy");
                                        Date renewalDate = null;
                                        try {
                                            renewalDate = sdf.parse(dueDate);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        if (new Date().before(renewalDate)
                                                && !(paymentMechanism.equalsIgnoreCase("ATM"))
                                                && !(paymentMechanism.equalsIgnoreCase("Direct Bill"))) {
                                            mode = "Pre Alter";
                                        } else if (new Date().before(renewalDate)
                                                && paymentMechanism.equalsIgnoreCase("ATM")
                                                && paymentMechanism.equalsIgnoreCase("Direct Bill")) {
                                            mode = "Pre Non Alter";
                                        } else if (new Date().after(renewalDate)) {
                                            mode = "Post Non Alter";
                                        }
                                        System.out.println("mode = " + mode);
                                        System.out.println("mode = " + paymentMechanism);
                                        SendRenewalDueEmailAsyncTask sendRenewalDueEmailAsyncTask =
                                                new SendRenewalDueEmailAsyncTask(policyNumber, emailid, dueDate, amount, mode,
                                                        name);
                                        sendRenewalDueEmailAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                    }


                                } else {
                                    mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", null);

                        AlertDialog dialog = builder.create();
                        dialog.setCancelable(false);
                        dialog.show();


                    }
                });

                holder.btnPaytm.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCommonMethods.hideKeyboard(edittextSearch, context);
                        int index = holder.getAdapterPosition();
                        String policyNo = lstAdapterList.get(index).getNo();
                        String premiumPayable = lstAdapterList.get(index).getPremiumAmt();
                        String premiumDueDate = lstAdapterList.get(index).getPremiumUp();
                        String mobileNo = lstAdapterList.get(index).getCONTACTMOBILE();

                        Intent i = new Intent(context, BillDeskPayment.class);
                        i.putExtra("policyNo", policyNo);
                        i.putExtra("PremiumAmt", premiumPayable);
                        i.putExtra("name", name);
                        i.putExtra("premiumDueDate", premiumDueDate);
                        i.putExtra("BulletPay", false);
                        i.putExtra("paymentMode", "Paytm");
                        i.putExtra("policyMobileNo", mobileNo);

                        startActivity(i);
                    }
                });

                holder.linearlayoutPayViaMosambee.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String mobileNumber = holder.textviewMobileNumber.getText().toString();
                        if (!TextUtils.isEmpty(mobileNumber)) {
                            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                            int index = holder.getAdapterPosition();
                            selectedPolicyNumber = lstAdapterList.get(index).getNo();
                            String premiumPayable = lstAdapterList.get(index).getPremiumAmt();
                            if (!lstAdapterList.get(position).getStatus().equalsIgnoreCase("Lapse") &&
                                    !lstAdapterList.get(position).getStatus().equalsIgnoreCase("Technical Lapse")) {
                                connectToMPOS(index, premiumPayable,
                                        classContext, holder.container, mobileNumber, strCIFBDMEmailId);
                            } else if (lstAdapterList.get(position).getStatus().equalsIgnoreCase("Lapse") ||
                                    lstAdapterList.get(position).getStatus().equalsIgnoreCase("Technical Lapse")) {
                                getPremiumAmountAsync = new GetPremiumAmountAsync(index,
                                        classContext, holder.container, mobileNumber, strCIFBDMEmailId);
                                getPremiumAmountAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            }


                        }
                    }
                });

            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }


        public void returnTransactionResult(String res, final ResultData resultData) {

            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }


            TransationId = resultData.getTransactionId();
           /*String message = "Result: " + resultData.getResult()
                    + "\nReason code: "
                    + resultData.getReasonCode() + "\nReason: "
                    + resultData.getReason() + "\nTranaction Id: "
                    + resultData.getTransactionId()
                    + "\nTransactin amount: "
                    + resultData.getAmount()
                    + "\nTransactin data: "
                    + resultData.getTransactionData()
                    + "\n TransationId: "+TransationId;*/
            try {
                //String transactionStatus = resultData.getReason().contains("Success") == true ? "Success" : "Fail";
                String transactionStatus = resultData.getTransactionData();
                String resultString = "";
                try {

                    JSONObject jsonObject = new JSONObject(transactionStatus);
                    resultString = jsonObject.getString("result");
                } catch (Exception e) {
                    e.printStackTrace();
                    resultString = "";
                }

                str_Status = resultString;
                if (resultString.equalsIgnoreCase("Success")) {
                    str_amount = resultData.getAmount();
                    TransationId = resultData.getTransactionId();
                    str_Error = resultData.getReason();

                    String cardType = "";
                    try {
                        JSONObject transactionData = new JSONObject(resultData.getTransactionData());
                        //String transactionJson = "{\"businessName\":\"SbiLife\",\"cardType:\":\"VISA\"}";
                        cardType = transactionData.getString("cardType");
                    } catch (Exception e) {
                        e.printStackTrace();
                        cardType = "";
                    }

                    asyncCheckPaymentStatus = new AsyncCheckPaymentStatus(context, selectedPolicyNumber, "", str_amount,
                            TransationId, str_Status, str_Error, cardType);
                    asyncCheckPaymentStatus.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                } else {
                    mCommonMethods.showMessageDialog(context, "Payment Failure. Sorry, Your Payment process is Failure, Try Again");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public class ViewHolderAdapter extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,
                MenuItem.OnMenuItemClickListener {

            private final TextView txtno, txtcustomercode, fname, status, premiumup,
                    premiumamt, txtrenewalpaytype, textviewMobileNumber, textviewContactOffice, tvDOC,
                    tvActualPremium, buttonFundValue, tvPaymentMechanism;
            private final TableRow tblren_updt, trOfficeContactMaster, trFundValue;
            private final Button buttonProbableCommission, btnPaytm, buttonCRM,
                    buttonUpdatAltMobile;//buttonRenewalUpdate,
            private final ImageView imgcontact_cust_r, imageviewSMS;
            private final LinearLayout premiumPaymentOptionsParent;
            private final FrameLayout container;
            private final LinearLayout linearlayoutPayViaMosambee;
            private final LinearLayout LLofficeContactImage;

            ViewHolderAdapter(View v) {
                super(v);
                // get text view
                txtno = v.findViewById(R.id.txtrenewalno);
                txtcustomercode = v.findViewById(R.id.txtcustomercode);
                fname = v.findViewById(R.id.txtrenewallfirstname);

                status = v.findViewById(R.id.txtrenewalstatus);
                premiumup = v.findViewById(R.id.txtrenewalpremiumup);
                premiumamt = v.findViewById(R.id.txtrenewalpremiumamt);

                txtrenewalpaytype = v.findViewById(R.id.txtrenewalpaytype);

                textviewMobileNumber = v.findViewById(R.id.textviewMobileNumber);
                textviewContactOffice = v.findViewById(R.id.textviewContactOffice);
                tvDOC = v.findViewById(R.id.tvDOC);
                //buttonRenewalUpdate = v.findViewById(R.id.buttonRenewalUpdate);
                buttonCRM = v.findViewById(R.id.buttonCRM);
                buttonProbableCommission = v.findViewById(R.id.buttonProbableCommission);
                tvActualPremium = v.findViewById(R.id.tvActualPremium);
                buttonFundValue = v.findViewById(R.id.buttonFundValue);
                tvPaymentMechanism = v.findViewById(R.id.tvPaymentMechanism);
                buttonUpdatAltMobile = v.findViewById(R.id.buttonUpdatAltMobile);
                btnPaytm = v.findViewById(R.id.buttonPayPaytm);
                premiumPaymentOptionsParent = v.findViewById(R.id.ll_premium_paying_options);

                linearlayoutPayViaMosambee = v.findViewById(R.id.linearlayoutPayViaMosambee);

                container = v.findViewById(R.id.frameContainer);

                imgcontact_cust_r = v.findViewById(R.id.imgcontact_cust_r);
                imageviewSMS = v.findViewById(R.id.imageviewSMS);
                LLofficeContactImage = v.findViewById(R.id.LLofficeContactImage);

                tblren_updt = v.findViewById(R.id.tblren_updt);
                trOfficeContactMaster = v.findViewById(R.id.trOfficeContactMaster);
                trFundValue = v.findViewById(R.id.trFundValue);
                if (mCommonMethods.GetUserType(context).contentEquals("AGENT")
                        || mCommonMethods.GetUserType(context).contentEquals("UM")) {
                    // tblren_updt.setVisibility(View.VISIBLE);
                    tblren_updt.setVisibility(View.GONE);
                } else {
                    tblren_updt.setVisibility(View.GONE);
                }

                v.setOnCreateContextMenuListener(this);

            }


            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("Select Action");
                String status = lstAdapterList.get(getAdapterPosition()).getStatus();
                MenuItem customerDetails = menu.add(Menu.NONE, 1, 1, "Customer Details");
                customerDetails.setOnMenuItemClickListener(this);

                if (status.equalsIgnoreCase("Lapse") || status.equalsIgnoreCase("Technical Lapse")) {
                    MenuItem revivalQuotation = menu.add(Menu.NONE, 3, 3, "Revival Quotation");
                    revivalQuotation.setOnMenuItemClickListener(this);
                }
            }

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case 1:
                        String strrenCustomerId = lstAdapterList.get(getAdapterPosition()).getHolderId();
                        if (!TextUtils.isEmpty(strrenCustomerId)) {
                            Intent intent = new Intent(context, CustomerDetailActivity.class);
                            intent.putExtra("CustomerId", strrenCustomerId);
                            intent.putExtra("strUserType", "");
                            intent.putExtra("strAgentCode", strCIFBDMUserId);
                            intent.putExtra("strEmail", strCIFBDMEmailId);
                            intent.putExtra("strMobileNo", strCIFBDMMObileNo);
                            intent.putExtra("strPassword", strCIFBDMPassword.trim());
                            startActivity(intent);
                        }
                        break;

                    /*case 2:
                    case 3:
                        String policyNumberClicked = lstAdapterList.get(getAdapterPosition()).getNo();
                        GetRemarksAsyncTask getRemarksAsyncTask = new GetRemarksAsyncTask(policyNumberClicked);
                        getRemarksAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        break;*/
                    case 3:
                        String strPolicyNo = lstAdapterList.get(getAdapterPosition()).getNo();
                        String mobileNumber = lstAdapterList.get(getAdapterPosition()).getCONTACTMOBILE();
                        String emailId = "";

                        Intent intent = new Intent(context, RevivalQuotationActivity.class);
                        intent.putExtra("policyNumber", strPolicyNo);
                        intent.putExtra("mobileNumber", mobileNumber);
                        intent.putExtra("emailId", emailId);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        }
    }

    class DownloadBranchWiseRenewalListAsync extends AsyncTask<String, String, String> {

        String output;
        private volatile boolean running = true;
        private String error = "", fromDate = "", toDate = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            strFilterClickAction = null;
            mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading Please wait...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);
            mProgressDialog.show();

            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

            String spnRewmonths_text = spnRewmonths.getSelectedItem().toString();

            SimpleDateFormat format1 = new SimpleDateFormat("MM-dd-yyyy");

            if (spnRewmonths_text.contentEquals("Previous Month")) {
                Calendar aCalendar = Calendar.getInstance();
                aCalendar.set(Calendar.DATE, 1);
                aCalendar.add(Calendar.DAY_OF_MONTH, -1);
                Date lastDateOfPreviousMonth = aCalendar.getTime();
                toDate = format1.format(lastDateOfPreviousMonth);

                aCalendar.set(Calendar.DATE, 1);
                aCalendar.add(Calendar.MONTH, -5);
                Date firstDateOfPreviousMonth = aCalendar.getTime();
                fromDate = format1.format(firstDateOfPreviousMonth);
            } else if (spnRewmonths_text.contentEquals("Current Month")) {
                Calendar cale = Calendar.getInstance();
                cale.set(Calendar.DATE,
                        cale.getActualMaximum(Calendar.DATE));
                Date lastDateOfCurrentMonth = cale.getTime();
                toDate = format1.format(lastDateOfCurrentMonth);
                cale.set(Calendar.DATE,
                        cale.getActualMinimum(Calendar.DATE));
                Date firstDateOfCurrentMonth = cale.getTime();
                fromDate = format1.format(firstDateOfCurrentMonth);
            } else if (spnRewmonths_text.contentEquals("Next Month")) {
                Calendar calen = Calendar.getInstance();
                // calen.set(Calendar.MONTH, calen.get(Calendar.MONTH));
                calen.add(Calendar.MONTH, +1);
                calen.set(Calendar.DATE, calen.getActualMaximum(Calendar.DATE));
                Date lastDateOfNextMonth = calen.getTime();
                toDate = format1.format(lastDateOfNextMonth);

                calen.set(Calendar.DATE, calen.getActualMinimum(Calendar.DATE));

                Date firstDateOfNextMonth = calen.getTime();
                fromDate = format1.format(firstDateOfNextMonth);
            }
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;

                /*getbancaRenewalListBranchwise(string strBrcode, string strFromReqDate, string strToReqDate,
                string strEmailId, string strMobileNo, string strAuthKey) */


                request = new SoapObject(NAMESPACE, METHOD_NAME_BRANCHWISE_RENEWAL_LIST_GIO);
                request.addProperty("strAgentNo", strCIFBDMUserId);
                request.addProperty("strFromReqDate", fromDate);
                request.addProperty("strToReqDate", toDate);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);

                request.addProperty("strAuthKey", "QzhCNDc0OTU4NzZDQjI3RTQ4OEMyNEQ3MUZCQjE2QTY=");

                mCommonMethods.TLSv12Enable();

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();


                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME_BRANCHWISE_RENEWAL_LIST_GIO;
                androidHttpTranport.call(SOAP_ACTION, envelope);
                System.out.println("envelope.getResponse() = " + envelope.getResponse());
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                if (!response.toString().contentEquals("<CIFPolicyList />")) {

                    ParseXML prsObj = new ParseXML();
                    String inputpolicylist = response.toString();
                    System.out.println("inputpolicylist = " + inputpolicylist);
                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "CIFPolicyList");

                    error = prsObj.parseXmlTag(inputpolicylist, "ScreenData");

                    if (error == null) {
                        // for agent policy list
//                        List<String> Node = prsObj.parseParentNode(inputpolicylist, "Table");
//                        List<ParseXML.AgentPoliciesRenewalListMonthwiseGio> nodeData = prsObj
//                                .parseNodeBranchwiseRenewalListGio(Node);
//                        globalDataList.clear();
//                        globalDataList.addAll(nodeData);

                        output = inputpolicylist;
                        System.out.println("output = " + output);
                        error = "success";
                    } else {
                        error = "1";
                    }

                } else {
                    error = "1";
                }

            } catch (Exception e) {
                mProgressDialog.dismiss();
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
                if (error.equalsIgnoreCase("success")) {
                    AsynGetLatLang objAsynLoadMap = new AsynGetLatLang(
                            BancaReportsRenewalActivity.this, output);
                    objAsynLoadMap.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    mCommonMethods.showMessageDialog(context, "No Record found");
                    clearList();
                }
            } else {
                mCommonMethods.showMessageDialog(context, "No Record found");
                clearList();
            }
        }
    }

    class AsynGetLatLang extends AsyncTask<String, Void, String> {
        final Context context;
        final String res;
        ProgressDialog progressDialog = null;
        double Lati, Longi;

        int size;

        AsynGetLatLang(Context context, String result) {
            // TODO Auto-generated constructor stub

            res = result;
            System.out.println("res = " + res);
            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            progressDialog = new ProgressDialog(context,
                    ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Please wait...";
            progressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>"
                    + Message + "<b></font>"));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Loading");
            progressDialog.setMax(100);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            if (mCommonMethods.isNetworkConnected(context)) {

                try {
                    Geocoder geocoder = new Geocoder(context,
                            Locale.getDefault());
                    try {
//                        ArrayList<String> strRes = objParse
//                                .parseXmlTagMultiple(res, "Table");
                        List<String> strRes = objParse.parseParentNode(res, "Table");
                        AgentPoliciesRenewalListMonthwiseGioList = objParse.parseNodeBranchwiseRenewalListGio(strRes);

                        size = AgentPoliciesRenewalListMonthwiseGioList.size();
                        Log.d("AsynGetLatLang", "doInBackground: " + size);
                        for (int i = 0; i < AgentPoliciesRenewalListMonthwiseGioList.size(); i++) {

                            if (AgentPoliciesRenewalListMonthwiseGioList.get(i).getPERMANENTPOSTCODE() != null) {
                                //Log.d("AsynGetLatLang "+i, "doInBackground: "+AgentPoliciesRenewalListMonthwiseGioList.get(i).getHOLDERPERSONFIRSTNAME()+" "+AgentPoliciesRenewalListMonthwiseGioList.get(i).getHOLDERPERSONLASTNAME());

//                                @SuppressWarnings("rawtypes")
//                                List addressList = geocoder
//                                        .getFromLocationName(PERMANENTPOSTCODE, 1);
//                                if (addressList != null
//                                        && addressList.size() > 0) {
//                                    Address address = (Address) addressList
//                                            .get(0);
//                                    Lati = address.getLatitude() + "";
//                                    Longi = address.getLongitude() + "";
//
//                                }
                                String locationAddress = AgentPoliciesRenewalListMonthwiseGioList.get(i).getPERMANENTADDRESS1()
                                        + " " + AgentPoliciesRenewalListMonthwiseGioList.get(i).getPERMANENTADDRESS2()
                                        + " " + AgentPoliciesRenewalListMonthwiseGioList.get(i).getPERMANENTADDRESS3()
                                        + " " + AgentPoliciesRenewalListMonthwiseGioList.get(i).getPERMANENTCITY()
                                        + " " + AgentPoliciesRenewalListMonthwiseGioList.get(i).getPERMANENTSTATE()
                                        + " " + AgentPoliciesRenewalListMonthwiseGioList.get(i).getPERMANENTPOSTCODE();


                                List addressList = geocoder.getFromLocationName(locationAddress, 1);
                                if (addressList != null && addressList.size() > 0) {
                                    Address address = (Address) addressList.get(0);
//                                    StringBuilder sb = new StringBuilder();
//                                    sb.append(address.getLatitude()).append("\n");
//                                    sb.append(address.getLongitude()).append("\n");
//                                    result = sb.toString();
                                    Lati = address.getLatitude();
                                    Longi = address.getLongitude();

//                                    Log.d("AsynGetLatLang", "doInBackground: "+locationAddress);
//                                    Log.d("AsynGetLatLang", "doInBackground:1 "+Lati+" == "+Longi);
                                    AgentPoliciesRenewalListMonthwiseGioList.get(i).setPOLICYLATITUDE(Lati);
                                    AgentPoliciesRenewalListMonthwiseGioList.get(i).setPOLICYLONGITUDE(Longi);

                                    String km = getDistance(latLng.latitude, latLng.longitude, Lati, Longi).replace(",", "").replace(" km", "");
                                    if (km != null || km != "") {
                                        AgentPoliciesRenewalListMonthwiseGioList.get(i).setPOLICYDISTANCE(Double.parseDouble(km));
                                    }

                                }
                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return "Server not Found. Please try after some time.";
                }

            } else
                return "Please Activate Internet connection";

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

                Intent intent = new Intent(BancaReportsRenewalActivity.this,
                        PolicyHolderLocatorMapFragmentActivity.class);
                intent.putExtra("retVal", AgentPoliciesRenewalListMonthwiseGioList);
                intent.putExtra("size", size);
                intent.putExtra("strBDMCifCOde", strCIFBDMUserId);
                intent.putExtra("spinnerSelectedItem", spnRewmonths.getSelectedItem().toString());

                startActivity(intent);

            } catch (Exception e) {
                e.getMessage();
            }

        }

    }

    class GetPremiumAmountAsync extends AsyncTask<String, Void, String> {
        private final String SOAP_ACTION_PREMIUM = "http://tempuri.org/GetPremiumDetailsHotPayment_css";
        private final String METHOD_NAME_PREMIUM = "GetPremiumDetailsHotPayment_css";
        private final int index;
        private final String mobileNumber;
        private final String emailId;
        private final JARClassImplementation.TransactionResultInterface classContext;
        private final FrameLayout container;
        private String PremiumAmt;
        private String firtsName;
        private String middleName;
        private String lastName;
        private String dueDate;

        public GetPremiumAmountAsync(int index, JARClassImplementation.TransactionResultInterface classContext,
                                     FrameLayout container, String mobileNumber, String emailId) {
            this.index = index;
            this.classContext = classContext;
            this.container = container;
            this.mobileNumber = mobileNumber;
            this.emailId = emailId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... param) {
            if (mCommonMethods.isNetworkConnected(context)) {

                try {
                    //GetPremiumDetailsHotPayment_css(string strPolicyNo, string baCode, string reqCssStr)
                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_PREMIUM);

                    request.addProperty("strPolicyNo", selectedPolicyNumber);
                    request.addProperty("baCode", "0");
                    request.addProperty("reqCssStr", "EASYACCESS");

                    System.out.println("result " + request.toString());
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    //Enable this envelope if service is written in dot net
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE("https://sbilposservices.sbilife.co.in/service.asmx?wsdl");

                    mCommonMethods.TLSv12Enable();

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);


                    androidHttpTransport.call(SOAP_ACTION_PREMIUM, envelope);
                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                    String result = response.toString();

                    System.out.println("result " + result);

                    if (result.contains("<ErrCode>0</ErrCode>")) {
                        ParseXML prsObj = new ParseXML();
                        result = prsObj.parseXmlTag(result, "ScreenData");

                        firtsName = prsObj.parseXmlTag(result, "Sbi_First_Name");
                        middleName = prsObj.parseXmlTag(result, "Sbi_Middle_Name");
                        lastName = prsObj.parseXmlTag(result, "Sbi_Last_Name");
                        dueDate = prsObj.parseXmlTag(result, "DueDate");
                        PremiumAmt = prsObj.parseXmlTag(result, "GrossAmt");
                        PremiumAmt = PremiumAmt.replaceFirst("^0+(?!$)", "");
                        return "Success";

                    } else if (result.contains("<ErrCode>1</ErrCode>")) {
                        ParseXML prsObj = new ParseXML();
                        result = prsObj.parseXmlTag(result, "ErrDesc");
                        return result;
                    } else {
                        return "Try After Some Time...";
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return "Server not Found. Please try after some time.";
                }

            } else
                return "Please Activate Internet connection";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

            } catch (Exception e) {
                e.getMessage();
            }

            if (result.equals("Success")) {

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.loading_window_twobutton);
                TextView text = dialog.findViewById(R.id.txtalertheader);
                text.setText("Gross Premium Amount is - " + PremiumAmt + "\n" +
                        "Click OK Button to Proceed.");
                Button dialogButton = dialog.findViewById(R.id.btnalert);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                        try {
                            connectToMPOS(index, PremiumAmt, classContext, container, mobileNumber, emailId);
                        } catch (Exception e) {
                        }
                    }
                });
                Button dialogButtoncancel = dialog.findViewById(R.id.btnalertcancel);
                dialogButtoncancel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            } else {
                mCommonMethods.showMessageDialog(context, result);
            }
        }
    }

}
