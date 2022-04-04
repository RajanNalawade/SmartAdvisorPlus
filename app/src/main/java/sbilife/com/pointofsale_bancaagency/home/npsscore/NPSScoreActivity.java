package sbilife.com.pointofsale_bancaagency.home.npsscore;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class NPSScoreActivity extends AppCompatActivity implements View.OnClickListener,
        ServiceHits.DownLoadData {
    private final String METHOD_NAME = "getNPSScore_smrt";
    private ProgressDialog mProgressDialog;
    private Context context;
    private CommonMethods commonMethods;

    private int mYear, mMonth, mDay, datecheck = 0;
    private ServiceHits service;

    private TextView textViewFromDate, textViewToDate, textviewRecordCount, textviewProjectType,
            tvtotalOnBoardingNPSCurrentFY, tvtotalOnBoardingNPSPreviousFY, tvtotalRenewalNPSCurrentFY,
            tvtotalRenewalNPSPreviousFY,
            tvtotServiceExpOnlinePayNPSCurrentFY, tvtotServiceExpOnlinePayNPSPreviousFY,
            tvtotServiceExpRegdNPSCurrentFY, tvtotServiceExpRegdPayNPSPreviousFY,
            tvtotServiceExpSBIBankNPSCurrentFY, tvtotServiceExpSBIBankNPSPreviousFY,
            tvtotServiceExpSBILifeNPSCurrentFY, tvtotServiceExpSBILifeNPSPreviousFY,
            tvtotalLivingBenefitNPSCurrentFY, tvtotalLivingBenefitNPSPreviousFY,
            tvtotalTotalNPSCurrentFY, tvtotalTotalNPSPreviousFY, tvtotServiceExperienceNPSCurrentFY,
            tvtotServiceExperienceNPSPreviousFY;

    private String fromDate = "", toDate = "";
    private DownloadNPSScoreAsync downloadNPSScoreAsync;
    private ArrayList<NPSScoreValuesModel> globalDataList, globalDataPreviousList;
    private String strCIFBDMUserId = "", strCIFBDMEmailId = "", strCIFBDMMObileNo = "", currentFYString, previousFYString;
    private HorizontalScrollView horizontalScrollView;

    private RecyclerView recyclerview;

    private SelectedAdapter selectedAdapter;
    private String NPSRecordFlag = "";
    private LinearLayout llCompareProject;
    private Button buttonCompare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_npsscore);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "NPS Score");

        textViewFromDate = findViewById(R.id.textViewFromDate);
        textViewToDate = findViewById(R.id.textViewToDate);
        textviewRecordCount = findViewById(R.id.textviewRecordCount);
        textviewProjectType = findViewById(R.id.textviewProjectType);

        tvtotalOnBoardingNPSCurrentFY = findViewById(R.id.tvtotalOnBoardingNPSCurrentFY);
        tvtotalOnBoardingNPSPreviousFY = findViewById(R.id.tvtotalOnBoardingNPSPreviousFY);
        tvtotalRenewalNPSCurrentFY = findViewById(R.id.tvtotalRenewalNPSCurrentFY);
        tvtotalRenewalNPSPreviousFY = findViewById(R.id.tvtotalRenewalNPSPreviousFY);

        tvtotServiceExpOnlinePayNPSCurrentFY = findViewById(R.id.tvtotServiceExpOnlinePayNPSCurrentFY);
        tvtotServiceExpOnlinePayNPSPreviousFY = findViewById(R.id.tvtotServiceExpOnlinePayNPSPreviousFY);
        tvtotServiceExpRegdNPSCurrentFY = findViewById(R.id.tvtotServiceExpRegdNPSCurrentFY);
        tvtotServiceExpRegdPayNPSPreviousFY = findViewById(R.id.tvtotServiceExpRegdPayNPSPreviousFY);
        tvtotServiceExpSBIBankNPSCurrentFY = findViewById(R.id.tvtotServiceExpSBIBankNPSCurrentFY);
        tvtotServiceExpSBIBankNPSPreviousFY = findViewById(R.id.tvtotServiceExpSBIBankNPSPreviousFY);
        tvtotServiceExpSBILifeNPSCurrentFY = findViewById(R.id.tvtotServiceExpSBILifeNPSCurrentFY);
        tvtotServiceExpSBILifeNPSPreviousFY = findViewById(R.id.tvtotServiceExpSBILifeNPSPreviousFY);

        tvtotalLivingBenefitNPSCurrentFY = findViewById(R.id.tvtotalLivingBenefitNPSCurrentFY);
        tvtotalLivingBenefitNPSPreviousFY = findViewById(R.id.tvtotalLivingBenefitNPSPreviousFY);
        tvtotalTotalNPSCurrentFY = findViewById(R.id.tvtotalTotalNPSCurrentFY);
        tvtotalTotalNPSPreviousFY = findViewById(R.id.tvtotalTotalNPSPreviousFY);
        tvtotServiceExperienceNPSCurrentFY = findViewById(R.id.tvtotServiceExperienceNPSCurrentFY);
        tvtotServiceExperienceNPSPreviousFY = findViewById(R.id.tvtotServiceExperienceNPSPreviousFY);


        horizontalScrollView = findViewById(R.id.horizontalScrollView);
        horizontalScrollView.setVisibility(View.GONE);
        TableRow tablerowOnboarding = findViewById(R.id.tablerowOnboarding);
        TableRow tablerowRenewal = findViewById(R.id.tablerowRenewal);

        TableRow trserviceExpOnlinePay = findViewById(R.id.trserviceExpOnlinePay);
        TableRow trserviceExpRegd = findViewById(R.id.trserviceExpRegd);
        TableRow trserviceExpSBIBank = findViewById(R.id.trserviceExpSBIBank);
        TableRow trserviceExpSBILife = findViewById(R.id.trserviceExpSBILife);
        TableRow trserviceExperience = findViewById(R.id.trserviceExperience);

        TableRow tablerowLivingBenefit = findViewById(R.id.tablerowLivingBenefit);
        TableRow tablerowTotal = findViewById(R.id.tablerowTotal);


        llCompareProject = findViewById(R.id.llCompareProject);
        llCompareProject.setVisibility(View.GONE);

        recyclerview = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        // set LayoutManager to RecyclerView

        globalDataList = new ArrayList<>();
        globalDataPreviousList = new ArrayList<>();
        selectedAdapter = new SelectedAdapter(globalDataList, "");
        recyclerview.setAdapter(selectedAdapter);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setLayoutManager(linearLayoutManager);

        Button buttonOk = findViewById(R.id.buttonOk);
        buttonCompare = findViewById(R.id.buttonCompare);
        textViewFromDate.setOnClickListener(this);
        textViewToDate.setOnClickListener(this);
        buttonOk.setOnClickListener(this);
        buttonCompare.setOnClickListener(this);

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        textViewFromDate.setText(commonMethods.getPreviousMonthDate());
        textViewToDate.setText(commonMethods.getCurrentMonthDate());

        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");

        if (fromHome != null && fromHome.equalsIgnoreCase("Y")) {
            getUserDetails();
        } else {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");
        }

        // getUserDetails();

        mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);


        mProgressDialog.setButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (downloadNPSScoreAsync != null) {
                            downloadNPSScoreAsync.cancel(true);
                        }
                        if (mProgressDialog != null) {
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }
                    }
                });

        mProgressDialog.setMax(100);

        tablerowOnboarding.setOnClickListener(this);
        tablerowRenewal.setOnClickListener(this);
        tablerowLivingBenefit.setOnClickListener(this);
        tablerowTotal.setOnClickListener(this);

        trserviceExpOnlinePay.setOnClickListener(this);
        trserviceExpRegd.setOnClickListener(this);
        trserviceExpSBIBank.setOnClickListener(this);
        trserviceExpSBILife.setOnClickListener(this);
        trserviceExperience.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonOk:
                NPSRecordFlag = "current";
                clearList();

                if (commonMethods.isNetworkConnected(context)) {
                    StringBuffer input = new StringBuffer();

                    fromDate = textViewFromDate.getText().toString();
                    toDate = textViewToDate.getText().toString();
                    if (TextUtils.isEmpty(toDate) || TextUtils.isEmpty(fromDate)) {
                        commonMethods.showMessageDialog(context, "Please Select Dates");
                        return;
                    } else {
                        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");
                        SimpleDateFormat finalDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        Date d1 = null, d2 = null;
                        try {
                            d1 = formatter.parse(fromDate);
                            fromDate = finalDateFormat.format(d1);
                            fromDate = fromDate.toUpperCase();

                            d2 = formatter.parse(toDate);
                            toDate = finalDateFormat.format(d2);
                            toDate = toDate.toUpperCase();

                            Calendar calenderToDate = Calendar.getInstance();
                            calenderToDate.setTime(d2);

                            String toMonthString = (String) DateFormat.format("MM", d2);
                            int monthInt = Integer.parseInt(toMonthString);

                            if (monthInt >= 4) {
                                currentFYString = calenderToDate.get(Calendar.YEAR)
                                        + "-";
                                calenderToDate.add(Calendar.YEAR, +1);
                                currentFYString += String.valueOf(calenderToDate.get(Calendar.YEAR)).substring(2);
                            } else {
                                calenderToDate.add(Calendar.YEAR, -1);

                                currentFYString = calenderToDate.get(Calendar.YEAR)
                                        + "-";
                                calenderToDate.add(Calendar.YEAR, +1);
                                currentFYString += String.valueOf(calenderToDate.get(Calendar.YEAR)).substring(2);
                            }


                            if ((d2.after(d1)) || (d2.equals(d1))) {
                                input.append(fromDate).append(",").append(toDate);
                            } else {
                                commonMethods.showMessageDialog(context, "To date should be greater than From date");
                                return;
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    service_hits(input.toString());

                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }

                break;
            case R.id.buttonCompare:

                if (commonMethods.isNetworkConnected(context)) {
                    if (globalDataList.size() != 0) {
                        NPSRecordFlag = "previous";
                        StringBuffer input = new StringBuffer();

                        fromDate = textViewFromDate.getText().toString();
                        toDate = textViewToDate.getText().toString();
                        if (TextUtils.isEmpty(toDate) || TextUtils.isEmpty(fromDate)) {
                            commonMethods.showMessageDialog(context, "Please Select Dates");
                            return;
                        } else {
                            final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");
                            SimpleDateFormat finalDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                            Date d1 = null, d2 = null;
                            try {
                                d1 = formatter.parse(fromDate);
                                d2 = formatter.parse(toDate);

                                Calendar calendarFromDate = Calendar.getInstance();
                                calendarFromDate.setTime(d1);

                                String fromMonthString = (String) DateFormat.format("MM", d1);
                                int monthInt = Integer.parseInt(fromMonthString);

                                d2 = calendarFromDate.getTime();
                                toDate = finalDateFormat.format(d2);
                                toDate = toDate.toUpperCase();

                                calendarFromDate.add(Calendar.YEAR, -1);
                                previousFYString = calendarFromDate.get(Calendar.YEAR)
                                        + "-";

                                d1 = calendarFromDate.getTime();
                                fromDate = finalDateFormat.format(d1);
                                fromDate = fromDate.toUpperCase();

                                calendarFromDate.add(Calendar.YEAR, +1);
                                previousFYString += String.valueOf(calendarFromDate.get(Calendar.YEAR)).substring(2);


                                if ((d2.after(d1)) || (d2.equals(d1))) {
                                    input.append(fromDate).append(",").append(toDate).append(",Compare");
                                } else {
                                    commonMethods.showMessageDialog(context, "To date should be greater than From date");
                                    return;
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        service_hits(input.toString());
                    } else {
                        commonMethods.showMessageDialog(context, "To compare Last Fy please get current year NPS Score first.");
                    }

                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }
                break;
            case R.id.textViewFromDate:
                datecheck = 1;
                showDateDialog();
                break;
            case R.id.textViewToDate:
                datecheck = 2;
                showDateDialog();
                break;
            case R.id.tablerowOnboarding:
                textviewProjectType.setVisibility(View.VISIBLE);
                textviewRecordCount.setVisibility(View.VISIBLE);
                ArrayList<NPSScoreValuesModel> onBoardingList = new ArrayList<>();
                for (int i = 0; i < globalDataList.size(); i++) {
                    if (globalDataList.get(i).getPROJECT_TYPE().equalsIgnoreCase("Onboarding")) {
                        onBoardingList.add(globalDataList.get(i));
                    }
                }

                textviewProjectType.setText("Onboarding");
                textviewRecordCount.setText("Record Count : " + onBoardingList.size());
                selectedAdapter = new SelectedAdapter(onBoardingList, "Onboarding");
                recyclerview.setAdapter(selectedAdapter);
                recyclerview.invalidate();
                break;
            case R.id.tablerowRenewal:
                textviewProjectType.setVisibility(View.VISIBLE);
                textviewRecordCount.setVisibility(View.VISIBLE);
                ArrayList<NPSScoreValuesModel> renewalList = new ArrayList<>();
                for (int i = 0; i < globalDataList.size(); i++) {
                    if (globalDataList.get(i).getPROJECT_TYPE().equalsIgnoreCase("Renewal")) {
                        renewalList.add(globalDataList.get(i));
                    }
                }
                textviewProjectType.setText("Renewal");
                textviewRecordCount.setText("Record Count : " + renewalList.size());
                selectedAdapter = new SelectedAdapter(renewalList, "Renewal");
                recyclerview.setAdapter(selectedAdapter);
                recyclerview.invalidate();
                break;
            case R.id.tablerowLivingBenefit:
                textviewProjectType.setVisibility(View.VISIBLE);
                textviewRecordCount.setVisibility(View.VISIBLE);
                ArrayList<NPSScoreValuesModel> livingBenefitList = new ArrayList<>();
                for (int i = 0; i < globalDataList.size(); i++) {
                    if (globalDataList.get(i).getPROJECT_TYPE().equalsIgnoreCase("Living Benefit")) {
                        livingBenefitList.add(globalDataList.get(i));
                    }
                }
                textviewProjectType.setText("Living Benefit");
                textviewRecordCount.setText("Record Count : " + livingBenefitList.size());
                selectedAdapter = new SelectedAdapter(livingBenefitList, "Living Benefit");
                recyclerview.setAdapter(selectedAdapter);
                recyclerview.invalidate();
                break;
            case R.id.tablerowTotal:
                textviewProjectType.setVisibility(View.VISIBLE);
                textviewRecordCount.setVisibility(View.VISIBLE);
                ArrayList<NPSScoreValuesModel> totalList = new ArrayList<>();
                for (int i = 0; i < globalDataList.size(); i++) {
                    String projectType = globalDataList.get(i).getPROJECT_TYPE();

                    if (projectType.equalsIgnoreCase("Living Benefit")
                            || projectType.equalsIgnoreCase("Onboarding")
                            || projectType.equalsIgnoreCase("Renewal")
                            || projectType.equalsIgnoreCase("Service Exp Online Payment")
                            || projectType.equalsIgnoreCase("Service Experience Registered")
                            || projectType.equalsIgnoreCase("Service Experience SBI Bank")
                            || projectType.equalsIgnoreCase("Service Experience SBI Life")) {
                        totalList.add(globalDataList.get(i));
                    }
                }
                textviewProjectType.setText("All Project");
                textviewRecordCount.setText("Record Count : " + totalList.size());
                selectedAdapter = new SelectedAdapter(totalList, "Total");
                recyclerview.setAdapter(selectedAdapter);
                recyclerview.invalidate();
                break;

            case R.id.trserviceExpOnlinePay:
                textviewProjectType.setVisibility(View.VISIBLE);
                textviewRecordCount.setVisibility(View.VISIBLE);
                ArrayList<NPSScoreValuesModel> serviceExpOnlinePayList = new ArrayList<>();
                for (int i = 0; i < globalDataList.size(); i++) {
                    if (globalDataList.get(i).getPROJECT_TYPE().equalsIgnoreCase("Service Exp Online Payment")) {
                        serviceExpOnlinePayList.add(globalDataList.get(i));
                    }
                }
                textviewProjectType.setText("Service Exp Online Payment");
                textviewRecordCount.setText("Record Count : " + serviceExpOnlinePayList.size());
                selectedAdapter = new SelectedAdapter(serviceExpOnlinePayList, "Service Exp Online Payment");
                recyclerview.setAdapter(selectedAdapter);
                recyclerview.invalidate();
                break;
            case R.id.trserviceExpRegd:
                textviewProjectType.setVisibility(View.VISIBLE);
                textviewRecordCount.setVisibility(View.VISIBLE);
                ArrayList<NPSScoreValuesModel> serviceExpRegdList = new ArrayList<>();
                for (int i = 0; i < globalDataList.size(); i++) {
                    if (globalDataList.get(i).getPROJECT_TYPE().equalsIgnoreCase("Service Experience Registered")) {
                        serviceExpRegdList.add(globalDataList.get(i));
                    }
                }
                textviewProjectType.setText("Service Experience Registered");
                textviewRecordCount.setText("Record Count : " + serviceExpRegdList.size());
                selectedAdapter = new SelectedAdapter(serviceExpRegdList, "Service Experience Registered");
                recyclerview.setAdapter(selectedAdapter);
                recyclerview.invalidate();
                break;
            case R.id.trserviceExpSBIBank:
                textviewProjectType.setVisibility(View.VISIBLE);
                textviewRecordCount.setVisibility(View.VISIBLE);
                ArrayList<NPSScoreValuesModel> serviceExpSBIBankList = new ArrayList<>();
                for (int i = 0; i < globalDataList.size(); i++) {
                    if (globalDataList.get(i).getPROJECT_TYPE().equalsIgnoreCase("Service Experience SBI Bank")) {
                        serviceExpSBIBankList.add(globalDataList.get(i));
                    }
                }
                textviewProjectType.setText("Service Experience SBI Bank");
                textviewRecordCount.setText("Record Count : " + serviceExpSBIBankList.size());
                selectedAdapter = new SelectedAdapter(serviceExpSBIBankList, "Service Experience SBI Bank");
                recyclerview.setAdapter(selectedAdapter);
                recyclerview.invalidate();
                break;
            case R.id.trserviceExpSBILife:
                textviewProjectType.setVisibility(View.VISIBLE);
                textviewRecordCount.setVisibility(View.VISIBLE);
                ArrayList<NPSScoreValuesModel> serviceExpSBILifeList = new ArrayList<>();
                for (int i = 0; i < globalDataList.size(); i++) {
                    if (globalDataList.get(i).getPROJECT_TYPE().equalsIgnoreCase("Service Experience SBI Life")) {
                        serviceExpSBILifeList.add(globalDataList.get(i));
                    }
                }
                textviewProjectType.setText("Service Experience SBI Life");
                textviewRecordCount.setText("Record Count : " + serviceExpSBILifeList.size());
                selectedAdapter = new SelectedAdapter(serviceExpSBILifeList, "Service Experience SBI Life");
                recyclerview.setAdapter(selectedAdapter);
                recyclerview.invalidate();
                break;
            case R.id.trserviceExperience:
                textviewProjectType.setVisibility(View.VISIBLE);
                textviewRecordCount.setVisibility(View.VISIBLE);
                ArrayList<NPSScoreValuesModel> serviceExperienceList = new ArrayList<>();
                for (int i = 0; i < globalDataList.size(); i++) {
                    if (globalDataList.get(i).getPROJECT_TYPE().equalsIgnoreCase("Service Exp Online Payment")
                            || globalDataList.get(i).getPROJECT_TYPE().equalsIgnoreCase("Service Experience Registered")
                            || globalDataList.get(i).getPROJECT_TYPE().equalsIgnoreCase("Service Experience SBI Bank")
                            || globalDataList.get(i).getPROJECT_TYPE().equalsIgnoreCase("Service Experience SBI Life")) {
                        serviceExperienceList.add(globalDataList.get(i));
                    }
                }
                textviewProjectType.setText("Service Experience");
                textviewRecordCount.setText("Record Count : " + serviceExperienceList.size());
                selectedAdapter = new SelectedAdapter(serviceExperienceList, "Service Experience");
                recyclerview.setAdapter(selectedAdapter);
                recyclerview.invalidate();
                break;
        }
    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        //strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
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
        downloadNPSScoreAsync = new DownloadNPSScoreAsync();
        downloadNPSScoreAsync.execute();
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
            textViewFromDate.setText(totaldate);
        } else if (datecheck == 2) {
            textViewToDate.setText(totaldate);
        }
    }

    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {

            mYear = year;
            mMonth = month;
            mDay = day;
            updateDisplay(mYear, mMonth, mDay);
        }
    };

    private void showDateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(
                context,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                mYear, mMonth, mDay);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    class DownloadNPSScoreAsync extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String status = "";
        private TextView onBoardingNPS, onBoardingDetractors, onBoardingPassives, onBoardingPromoters, onBoardingResponse,
                renewalResponse, renewalPromoters, renewalPassives, renewalDetractors, renewalNPS,

        serviceExpOnlinePayResponse, serviceExpOnlinePayPromoters,
                serviceExpOnlinePayPassives, serviceExpOnlinePayDetractors, serviceExpOnlinePayNPS,
                serviceExpRegdResponse, serviceExpRegdPromoters, serviceExpRegdPassives,
                serviceExpRegdDetractors, serviceExpRegdNPS,
                serviceExpSBIBankResponse, serviceExpSBIBankPromoters, serviceExpSBIBankPassives,
                serviceExpSBIBankDetractors, serviceExpSBIBankNPS,
                serviceExpSBILifedResponse, serviceExpSBILifePromoters, serviceExpSBILifePassives,
                serviceExpSBILifeDetractors, serviceExpSBILifeNPS,
                serviceExperienceResponse, serviceExperiencePromoters, serviceExperiencePassives,
                serviceExperienceDetractors, serviceExperienceNPS,
                livingBenefitResponse, livingBenefitPromoters, livingBenefitPassives, livingBenefitDetractors, livingBenefitNPS,
                totalResponse, totalPromoters, totalPassives, totalDetractors, totalNPS;


        private DecoView arcViewTotal, arcViewonBoardingNPS, arcViewonrenewalNPS, arcViewonlivingBenefitNPS,
                arcViewonserviceExpOnlinePayNPS, arcViewonserviceExpRegdNPS, arcViewonserviceExpSBIBankNPS,
                arcViewonserviceExpSBILifeNPS, arcViewServiceExperienceNPS;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
            onBoardingNPS = findViewById(R.id.onBoardingNPS);
            onBoardingDetractors = findViewById(R.id.onBoardingDetractors);
            onBoardingPassives = findViewById(R.id.onBoardingPassives);
            onBoardingPromoters = findViewById(R.id.onBoardingPromoters);
            onBoardingResponse = findViewById(R.id.onBoardingResponse);

            renewalResponse = findViewById(R.id.renewalResponse);
            renewalPromoters = findViewById(R.id.renewalPromoters);
            renewalPassives = findViewById(R.id.renewalPassives);
            renewalDetractors = findViewById(R.id.renewalDetractors);
            renewalNPS = findViewById(R.id.renewalNPS);

            serviceExpOnlinePayResponse = findViewById(R.id.serviceExpOnlinePayResponse);
            serviceExpOnlinePayPromoters = findViewById(R.id.serviceExpOnlinePayPromoters);
            serviceExpOnlinePayPassives = findViewById(R.id.serviceExpOnlinePayPassives);
            serviceExpOnlinePayDetractors = findViewById(R.id.serviceExpOnlinePayDetractors);
            serviceExpOnlinePayNPS = findViewById(R.id.serviceExpOnlinePayNPS);

            serviceExpRegdResponse = findViewById(R.id.serviceExpRegdResponse);
            serviceExpRegdPromoters = findViewById(R.id.serviceExpRegdPromoters);
            serviceExpRegdPassives = findViewById(R.id.serviceExpRegdPassives);
            serviceExpRegdDetractors = findViewById(R.id.serviceExpRegdDetractors);
            serviceExpRegdNPS = findViewById(R.id.serviceExpRegdNPS);

            serviceExpSBIBankResponse = findViewById(R.id.serviceExpSBIBankResponse);
            serviceExpSBIBankPromoters = findViewById(R.id.serviceExpSBIBankPromoters);
            serviceExpSBIBankPassives = findViewById(R.id.serviceExpSBIBankPassives);
            serviceExpSBIBankDetractors = findViewById(R.id.serviceExpSBIBankDetractors);
            serviceExpSBIBankNPS = findViewById(R.id.serviceExpSBIBankNPS);

            serviceExpSBILifedResponse = findViewById(R.id.serviceExpSBILifedResponse);
            serviceExpSBILifePromoters = findViewById(R.id.serviceExpSBILifePromoters);
            serviceExpSBILifePassives = findViewById(R.id.serviceExpSBILifePassives);
            serviceExpSBILifeDetractors = findViewById(R.id.serviceExpSBILifeDetractors);
            serviceExpSBILifeNPS = findViewById(R.id.serviceExpSBILifeNPS);

            serviceExperienceResponse = findViewById(R.id.serviceExperienceResponse);
            serviceExperiencePromoters = findViewById(R.id.serviceExperiencePromoters);
            serviceExperiencePassives = findViewById(R.id.serviceExperiencePassives);
            serviceExperienceDetractors = findViewById(R.id.serviceExperienceDetractors);
            serviceExperienceNPS = findViewById(R.id.serviceExperienceNPS);

            livingBenefitResponse = findViewById(R.id.livingBenefitResponse);
            livingBenefitPromoters = findViewById(R.id.livingBenefitPromoters);
            livingBenefitPassives = findViewById(R.id.livingBenefitPassives);
            livingBenefitDetractors = findViewById(R.id.livingBenefitDetractors);
            livingBenefitNPS = findViewById(R.id.livingBenefitNPS);

            totalResponse = findViewById(R.id.totalResponse);
            totalPromoters = findViewById(R.id.totalPromoters);
            totalPassives = findViewById(R.id.totalPassives);
            totalDetractors = findViewById(R.id.totalDetractors);
            totalNPS = findViewById(R.id.totalNPS);

            arcViewTotal = findViewById(R.id.arcViewTotal);
            arcViewonBoardingNPS = findViewById(R.id.arcViewonBoardingNPS);
            arcViewonrenewalNPS = findViewById(R.id.arcViewonrenewalNPS);
            arcViewonlivingBenefitNPS = findViewById(R.id.arcViewonlivingBenefitNPS);
            arcViewonserviceExpOnlinePayNPS = findViewById(R.id.arcViewonserviceExpOnlinePayNPS);
            arcViewonserviceExpRegdNPS = findViewById(R.id.arcViewonserviceExpRegdNPS);
            arcViewonserviceExpSBIBankNPS = findViewById(R.id.arcViewonserviceExpSBIBankNPS);
            arcViewonserviceExpSBILifeNPS = findViewById(R.id.arcViewonserviceExpSBILifeNPS);
            arcViewonserviceExpSBILifeNPS = findViewById(R.id.arcViewonserviceExpSBILifeNPS);
            arcViewServiceExperienceNPS = findViewById(R.id.arcViewServiceExperienceNPS);
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;
                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME);
                //, string , string , string , string , string

                request.addProperty("strFromdate", fromDate);
                request.addProperty("strTodate", toDate);
                request.addProperty("strCode", strCIFBDMUserId);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", commonMethods.getStrAuth());

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                String URl = ServiceURL.SERVICE_URL;
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

                        ArrayList<NPSScoreValuesModel> nodeData = parseNodeNPSScore(Node);
                        if (NPSRecordFlag.equalsIgnoreCase("previous")) {
                            globalDataPreviousList.clear();
                            globalDataPreviousList.addAll(nodeData);
                        } else if (NPSRecordFlag.equalsIgnoreCase("current")) {
                            globalDataList.clear();
                            globalDataList.addAll(nodeData);
                        }

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
                horizontalScrollView.setVisibility(View.GONE);
                llCompareProject.setVisibility(View.GONE);
                recyclerview.setVisibility(View.GONE);
                textviewProjectType.setVisibility(View.GONE);
                textviewRecordCount.setVisibility(View.GONE);
                if (status.equalsIgnoreCase("Success")) {
                    if (NPSRecordFlag.equalsIgnoreCase("previous")) {
                        llCompareProject.setVisibility(View.VISIBLE);
                        TextView tvNPSCurrentFY = findViewById(R.id.tvNPSCurrentFY);
                        TextView tvNPSPreviousFY = findViewById(R.id.tvNPSPreviousFY);
                        tvNPSCurrentFY.setText("NPS (FY " + currentFYString + " )");
                        tvNPSPreviousFY.setText("NPS (FY " + previousFYString + " )");
                        if (globalDataList.size() > 0) {
                            for (int i = 0; i < 5; i++) {
                                String projectType = globalDataList.get(i).getPROJECT_TYPE();
                                int npsScore = (int) globalDataList.get(i).getNPSScore();
                                int npsPreviousScore = (int) globalDataPreviousList.get(i).getNPSScore();
                                if (projectType.equalsIgnoreCase("Onboarding_NPS")) {
                                    tvtotalOnBoardingNPSCurrentFY.setText(npsScore + "");
                                    tvtotalOnBoardingNPSPreviousFY.setText(npsPreviousScore + "");
                                } else if (projectType.equalsIgnoreCase("renewal_NPS")) {
                                    tvtotalRenewalNPSCurrentFY.setText(npsScore + "");
                                    tvtotalRenewalNPSPreviousFY.setText(npsPreviousScore + "");
                                } else if (projectType.equalsIgnoreCase("ServiceExpOnlinePayment_NPS")) {
                                    tvtotServiceExpOnlinePayNPSCurrentFY.setText(npsScore + "");
                                    tvtotServiceExpOnlinePayNPSPreviousFY.setText(npsPreviousScore + "");
                                } else if (projectType.equalsIgnoreCase("ServiceExperienceRegistered_NPS")) {
                                    tvtotServiceExpRegdNPSCurrentFY.setText(npsScore + "");
                                    tvtotServiceExpRegdPayNPSPreviousFY.setText(npsPreviousScore + "");
                                } else if (projectType.equalsIgnoreCase("ServiceExperienceSBIBank_NPS")) {
                                    tvtotServiceExpSBIBankNPSCurrentFY.setText(npsScore + "");
                                    tvtotServiceExpSBIBankNPSPreviousFY.setText(npsPreviousScore + "");
                                } else if (projectType.equalsIgnoreCase("ServiceExperienceSBILife_NPS")) {
                                    tvtotServiceExpSBILifeNPSCurrentFY.setText(npsScore + "");
                                    tvtotServiceExpSBILifeNPSPreviousFY.setText(npsPreviousScore + "");
                                } else if (projectType.equalsIgnoreCase("ServiceExperience_NPS")) {
                                    tvtotServiceExperienceNPSCurrentFY.setText(npsScore + "");
                                    tvtotServiceExperienceNPSPreviousFY.setText(npsPreviousScore + "");
                                } else if (projectType.equalsIgnoreCase("Living Benefit_NPS")) {
                                    tvtotalLivingBenefitNPSCurrentFY.setText(npsScore + "");
                                    tvtotalLivingBenefitNPSPreviousFY.setText(npsPreviousScore + "");
                                } else if (projectType.equalsIgnoreCase("Total_NPS")) {
                                    tvtotalTotalNPSCurrentFY.setText(npsScore + "");
                                    tvtotalTotalNPSPreviousFY.setText(npsPreviousScore + "");
                                }
                            }
                        }


                    } else if (NPSRecordFlag.equalsIgnoreCase("current")) {
                        horizontalScrollView.setVisibility(View.VISIBLE);
                        recyclerview.setVisibility(View.VISIBLE);
                        buttonCompare.setEnabled(true);
                        buttonCompare.setBackgroundColor(Color.parseColor("#00a1e3"));
                        for (int i = 0; i < 8; i++) {
                            String projectType = globalDataList.get(i).getPROJECT_TYPE();
                            int npsScore = (int) globalDataList.get(i).getNPSScore();
                            String colorString = "#0BB5FF";
                            if (npsScore < 0) {
                                colorString = "#FF0000";
                            } else if (npsScore >= 0 && npsScore <= 50) {
                                colorString = "#FFFF00";
                            }
                        /*else if (npsScore > 10 && npsScore < 50) {
                            colorString = "#FFFF00";
                        }*/
                            else if (npsScore > 50) {
                                colorString = "#008000";
                            }
                            int promoters = (int) globalDataList.get(i).getPromoters();
                            int passives = (int) globalDataList.get(i).getPassives();
                            int detractors = (int) globalDataList.get(i).getDetractors();
                            int response = (int) (promoters + passives + detractors);

                            if (projectType.equalsIgnoreCase("Onboarding_NPS")) {
                                onBoardingResponse.setText(response + "");
                                onBoardingPromoters.setText((int) promoters + "");
                                onBoardingPassives.setText(passives + "");
                                onBoardingDetractors.setText(detractors + "");
                                onBoardingNPS.setText(npsScore + "");

                                /*if (npsScore > 0) {*/

                                arcViewonBoardingNPS.setVisibility(View.VISIBLE);
                                arcViewonBoardingNPS.addSeries(new SeriesItem
                                        .Builder(Color.argb(255, 218, 218, 218))
                                        .setRange(0, 100, 100)
                                        .setInitialVisibility(false)
                                        .setLineWidth(12f)
                                        .build());

                                //Create data series track
                                SeriesItem seriesItem1 = new SeriesItem
                                        .Builder(Color.parseColor(colorString))
                                        .setRange(0, 100, 0)
                                        .setLineWidth(12f)
                                        .build();
                                arcViewonBoardingNPS.addEvent(new DecoEvent
                                        .Builder(DecoEvent.EventType.EVENT_SHOW, true)
                                        .setDelay(1000)
                                        .setDuration(500)
                                        .build());
                                int series1Index = arcViewonBoardingNPS.addSeries(seriesItem1);
                                arcViewonBoardingNPS.addEvent(new DecoEvent.
                                        Builder((float) globalDataList.get(i).getNPSScore())
                                        .setIndex(series1Index)
                                        .setDelay(1000).build());
                           /* } else {
                                arcViewonBoardingNPS.setVisibility(View.GONE);
                            }*/
                            } else if (projectType.equalsIgnoreCase("renewal_NPS")) {
                                renewalResponse.setText(response + "");
                                renewalPromoters.setText(promoters + "");
                                renewalPassives.setText(passives + "");
                                renewalDetractors.setText(detractors + "");
                                renewalNPS.setText(npsScore + "");
                                /* if (npsScore > 0) {*/
                                arcViewonrenewalNPS.setVisibility(View.VISIBLE);
                                arcViewonrenewalNPS.addSeries(new SeriesItem
                                        .Builder(Color.argb(255, 218, 218, 218))
                                        .setRange(0, 100, 100)
                                        .setInitialVisibility(false)
                                        .setLineWidth(12f)
                                        .build());

                                //Create data series track
                                SeriesItem seriesItem1 = new SeriesItem.Builder(Color.parseColor(colorString))
                                        .setRange(0, 100, 0)
                                        .setLineWidth(12f)
                                        .build();
                                arcViewonrenewalNPS.addEvent(new DecoEvent
                                        .Builder(DecoEvent.EventType.EVENT_SHOW, true)
                                        .setDelay(1000)
                                        .setDuration(500)
                                        .build());
                                int series1Index = arcViewonrenewalNPS.addSeries(seriesItem1);
                                arcViewonrenewalNPS.addEvent(new DecoEvent
                                        .Builder((float) globalDataList.get(i).getNPSScore())
                                        .setIndex(series1Index)
                                        .setDelay(1000).build());
                            /*} else {
                                arcViewonrenewalNPS.setVisibility(View.GONE);
                            }*/

                            } else if (projectType.equalsIgnoreCase("ServiceExpOnlinePayment_NPS")) {
                                serviceExpOnlinePayResponse.setText(response + "");
                                serviceExpOnlinePayPromoters.setText(promoters + "");
                                serviceExpOnlinePayPassives.setText(passives + "");
                                serviceExpOnlinePayDetractors.setText(detractors + "");
                                serviceExpOnlinePayNPS.setText(npsScore + "");

                                /*if (npsScore > 0) {*/
                                arcViewonserviceExpOnlinePayNPS.setVisibility(View.VISIBLE);
                                arcViewonserviceExpOnlinePayNPS.addSeries(new SeriesItem
                                        .Builder(Color.argb(255, 218, 218, 218))
                                        .setRange(0, 100, 100)
                                        .setInitialVisibility(false)
                                        .setLineWidth(12f)
                                        .build());

                                //Create data series track
                                SeriesItem seriesItem1 = new SeriesItem.Builder(Color.parseColor(colorString))
                                        .setRange(0, 100, 0)
                                        .setLineWidth(12f)
                                        .build();
                                arcViewonserviceExpOnlinePayNPS.addEvent(new DecoEvent
                                        .Builder(DecoEvent.EventType.EVENT_SHOW, true)
                                        .setDelay(1000)
                                        .setDuration(500)
                                        .build());
                                int series1Index = arcViewonserviceExpOnlinePayNPS.addSeries(seriesItem1);
                                arcViewonserviceExpOnlinePayNPS.addEvent(new DecoEvent
                                        .Builder((float) globalDataList.get(i).getNPSScore())
                                        .setIndex(series1Index)
                                        .setDelay(1000).build());
                            } else if (projectType.equalsIgnoreCase("ServiceExperienceRegistered_NPS")) {
                                serviceExpRegdResponse.setText(response + "");
                                serviceExpRegdPromoters.setText(promoters + "");
                                serviceExpRegdPassives.setText(passives + "");
                                serviceExpRegdDetractors.setText(detractors + "");
                                serviceExpRegdNPS.setText(npsScore + "");

                                /*if (npsScore > 0) {*/
                                arcViewonserviceExpRegdNPS.setVisibility(View.VISIBLE);
                                arcViewonserviceExpRegdNPS.addSeries(new SeriesItem
                                        .Builder(Color.argb(255, 218, 218, 218))
                                        .setRange(0, 100, 100)
                                        .setInitialVisibility(false)
                                        .setLineWidth(12f)
                                        .build());

                                //Create data series track
                                SeriesItem seriesItem1 = new SeriesItem.Builder(Color.parseColor(colorString))
                                        .setRange(0, 100, 0)
                                        .setLineWidth(12f)
                                        .build();
                                arcViewonserviceExpRegdNPS.addEvent(new DecoEvent
                                        .Builder(DecoEvent.EventType.EVENT_SHOW, true)
                                        .setDelay(1000)
                                        .setDuration(500)
                                        .build());
                                int series1Index = arcViewonserviceExpRegdNPS.addSeries(seriesItem1);
                                arcViewonserviceExpRegdNPS.addEvent(new DecoEvent
                                        .Builder((float) globalDataList.get(i).getNPSScore())
                                        .setIndex(series1Index)
                                        .setDelay(1000).build());
                            } else if (projectType.equalsIgnoreCase("ServiceExperienceSBIBank_NPS")) {
                                serviceExpSBIBankResponse.setText(response + "");
                                serviceExpSBIBankPromoters.setText(promoters + "");
                                serviceExpSBIBankPassives.setText(passives + "");
                                serviceExpSBIBankDetractors.setText(detractors + "");
                                serviceExpSBIBankNPS.setText(npsScore + "");

                                /*if (npsScore > 0) {*/
                                arcViewonserviceExpSBIBankNPS.setVisibility(View.VISIBLE);
                                arcViewonserviceExpSBIBankNPS.addSeries(new SeriesItem
                                        .Builder(Color.argb(255, 218, 218, 218))
                                        .setRange(0, 100, 100)
                                        .setInitialVisibility(false)
                                        .setLineWidth(12f)
                                        .build());

                                //Create data series track
                                SeriesItem seriesItem1 = new SeriesItem.Builder(Color.parseColor(colorString))
                                        .setRange(0, 100, 0)
                                        .setLineWidth(12f)
                                        .build();
                                arcViewonserviceExpSBIBankNPS.addEvent(new DecoEvent
                                        .Builder(DecoEvent.EventType.EVENT_SHOW, true)
                                        .setDelay(1000)
                                        .setDuration(500)
                                        .build());
                                int series1Index = arcViewonserviceExpSBIBankNPS.addSeries(seriesItem1);
                                arcViewonserviceExpSBIBankNPS.addEvent(new DecoEvent
                                        .Builder((float) globalDataList.get(i).getNPSScore())
                                        .setIndex(series1Index)
                                        .setDelay(1000).build());
                            } else if (projectType.equalsIgnoreCase("ServiceExperienceSBILife_NPS")) {
                                serviceExpSBILifedResponse.setText(response + "");
                                serviceExpSBILifePromoters.setText(promoters + "");
                                serviceExpSBILifePassives.setText(passives + "");
                                serviceExpSBILifeDetractors.setText(detractors + "");
                                serviceExpSBILifeNPS.setText(npsScore + "");

                                /*if (npsScore > 0) {*/
                                arcViewonserviceExpSBILifeNPS.setVisibility(View.VISIBLE);
                                arcViewonserviceExpSBILifeNPS.addSeries(new SeriesItem
                                        .Builder(Color.argb(255, 218, 218, 218))
                                        .setRange(0, 100, 100)
                                        .setInitialVisibility(false)
                                        .setLineWidth(12f)
                                        .build());

                                //Create data series track
                                SeriesItem seriesItem1 = new SeriesItem.Builder(Color.parseColor(colorString))
                                        .setRange(0, 100, 0)
                                        .setLineWidth(12f)
                                        .build();
                                arcViewonserviceExpSBILifeNPS.addEvent(new DecoEvent
                                        .Builder(DecoEvent.EventType.EVENT_SHOW, true)
                                        .setDelay(1000)
                                        .setDuration(500)
                                        .build());
                                int series1Index = arcViewonserviceExpSBILifeNPS.addSeries(seriesItem1);
                                arcViewonserviceExpSBILifeNPS.addEvent(new DecoEvent
                                        .Builder((float) globalDataList.get(i).getNPSScore())
                                        .setIndex(series1Index)
                                        .setDelay(1000).build());
                            } else if (projectType.equalsIgnoreCase("ServiceExperience_NPS")) {
                                serviceExperienceResponse.setText(response + "");
                                serviceExperiencePromoters.setText(promoters + "");
                                serviceExperiencePassives.setText(passives + "");
                                serviceExperienceDetractors.setText(detractors + "");
                                serviceExperienceNPS.setText(npsScore + "");

                                /*if (npsScore > 0) {*/
                                arcViewServiceExperienceNPS.setVisibility(View.VISIBLE);
                                arcViewServiceExperienceNPS.addSeries(new SeriesItem
                                        .Builder(Color.argb(255, 218, 218, 218))
                                        .setRange(0, 100, 100)
                                        .setInitialVisibility(false)
                                        .setLineWidth(12f)
                                        .build());

                                //Create data series track
                                SeriesItem seriesItem1 = new SeriesItem.Builder(Color.parseColor(colorString))
                                        .setRange(0, 100, 0)
                                        .setLineWidth(12f)
                                        .build();
                                arcViewServiceExperienceNPS.addEvent(new DecoEvent
                                        .Builder(DecoEvent.EventType.EVENT_SHOW, true)
                                        .setDelay(1000)
                                        .setDuration(500)
                                        .build());
                                int series1Index = arcViewServiceExperienceNPS.addSeries(seriesItem1);
                                arcViewServiceExperienceNPS.addEvent(new DecoEvent
                                        .Builder((float) globalDataList.get(i).getNPSScore())
                                        .setIndex(series1Index)
                                        .setDelay(1000).build());
                            } else if (projectType.equalsIgnoreCase("Living Benefit_NPS")) {
                                livingBenefitResponse.setText(response + "");
                                livingBenefitPromoters.setText(promoters + "");
                                livingBenefitPassives.setText(passives + "");
                                livingBenefitDetractors.setText(detractors + "");
                                livingBenefitNPS.setText(npsScore + "");

                                /*if (npsScore > 0) {*/
                                arcViewonlivingBenefitNPS.setVisibility(View.VISIBLE);
                                arcViewonlivingBenefitNPS.addSeries(new SeriesItem
                                        .Builder(Color.argb(255, 218, 218, 218))
                                        .setRange(0, 100, 100)
                                        .setInitialVisibility(false)
                                        .setLineWidth(12f)
                                        .build());

                                //Create data series track
                                SeriesItem seriesItem1 = new SeriesItem.Builder(Color.parseColor(colorString))
                                        .setRange(0, 100, 0)
                                        .setLineWidth(12f)
                                        .build();
                                arcViewonlivingBenefitNPS.addEvent(new DecoEvent
                                        .Builder(DecoEvent.EventType.EVENT_SHOW, true)
                                        .setDelay(1000)
                                        .setDuration(500)
                                        .build());
                                int series1Index = arcViewonlivingBenefitNPS.addSeries(seriesItem1);
                                arcViewonlivingBenefitNPS.addEvent(new DecoEvent
                                        .Builder((float) globalDataList.get(i).getNPSScore())
                                        .setIndex(series1Index)
                                        .setDelay(1000).build());
                            } else if (projectType.equalsIgnoreCase("Total_NPS")) {

                                totalResponse.setText(response + "");
                                totalPromoters.setText(promoters + "");
                                totalPassives.setText(passives + "");
                                totalDetractors.setText(detractors + "");
                                totalNPS.setText(npsScore + "");

                                /*if (npsScore > 0) {*/
                                arcViewTotal.setVisibility(View.VISIBLE);
                                arcViewTotal.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                                        .setRange(0, 100, 100)
                                        .setInitialVisibility(false)
                                        .setLineWidth(12f)
                                        .build());

                                //Create data series track
                                SeriesItem seriesItem1 = new SeriesItem.Builder(Color.parseColor(colorString))
                                        .setRange(0, 100, 0)
                                        .setLineWidth(12f)
                                        .build();

                                int series1Index = arcViewTotal.addSeries(seriesItem1);

                                arcViewTotal.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                                        .setDelay(1000)
                                        .setDuration(2000)
                                        .build());

                                arcViewTotal.addEvent(new DecoEvent.Builder((float) globalDataList.get(i).getNPSScore()).setIndex(series1Index).setDelay(4000).build());
                            }
                        }
                    }

                } else {
                    commonMethods.showMessageDialog(context, "No record found");
                    clearList();
                }
            } else {
                commonMethods.showMessageDialog(context, "No record found");
                clearList();
            }
        }
    }

    private void clearList() {
        horizontalScrollView.setVisibility(View.GONE);
        llCompareProject.setVisibility(View.GONE);
        recyclerview.setVisibility(View.GONE);
        textviewRecordCount.setText("");
        textviewProjectType.setText("");
        if (globalDataList != null && selectedAdapter != null) {
            globalDataList.clear();
            selectedAdapter = new SelectedAdapter(new ArrayList<NPSScoreValuesModel>(), "");
            recyclerview.setAdapter(selectedAdapter);
            recyclerview.invalidate();
        }
    }

    public class SelectedAdapter extends RecyclerView.Adapter<SelectedAdapter.
            ViewHolderAdapter> {

        private final ArrayList<NPSScoreValuesModel> lstAdapterList;
        private ArrayList<NPSScoreValuesModel> lstSearch;

        SelectedAdapter(ArrayList<NPSScoreValuesModel> lstAdapterList, String projectName) {
            this.lstAdapterList = lstAdapterList;
        }


        @Override
        public int getItemCount() {
            return lstAdapterList.size();
        }

        @Override
        public ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_item_nps, parent, false);

            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final ViewHolderAdapter holder, int position) {

            holder.textviewPolicyNumber.setText(lstAdapterList.get(position).getPOLICY_NO());
            holder.textviewRating.setText(lstAdapterList.get(position).getRATING() + "");
            holder.textviewPrimaryComment.setText(lstAdapterList.get(position).getPRIMARY_COMMENT());

            String reason = lstAdapterList.get(position).getIMPROVE_FURTHER_CALL();
            if (TextUtils.isEmpty(reason)) {
                reason = "Reason not selected";
            }
            holder.textviewReason.setText(reason);
            holder.textviewAnyOtherReason.setText(lstAdapterList.get(position).getOTHER_REASON_0_6R());

            String projectType = lstAdapterList.get(position).getPROJECT_TYPE();
            holder.textviewProjectType.setText(projectType);
            System.out.println("projectType = " + projectType + ", position = " + position);
            if (projectType.equalsIgnoreCase("Service Exp Online Payment")
                    || projectType.equalsIgnoreCase("Service Experience Registered")
                    || projectType.equalsIgnoreCase("Service Experience SBI Bank")
                    || projectType.equalsIgnoreCase("Service Experience SBI Life")) {
                holder.llProjectType.setVisibility(View.VISIBLE);
            } else {
                holder.llProjectType.setVisibility(View.GONE);
            }
        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder {
            private final TextView textviewPolicyNumber, textviewRating, textviewProjectType,
                    textviewPrimaryComment, textviewReason, textviewAnyOtherReason;
            private LinearLayout llProjectType;

            ViewHolderAdapter(View v) {
                super(v);
                textviewPolicyNumber = v.findViewById(R.id.textviewPolicyNumber);
                textviewRating = v.findViewById(R.id.textviewRating);
                textviewProjectType = v.findViewById(R.id.textviewProjectType);

                textviewPrimaryComment = v.findViewById(R.id.textviewPrimaryComment);
                textviewReason = v.findViewById(R.id.textviewReason);
                textviewAnyOtherReason = v.findViewById(R.id.textviewAnyOtherReason);
                llProjectType = v.findViewById(R.id.llProjectType);

            }

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

        if (downloadNPSScoreAsync != null) {
            downloadNPSScoreAsync.cancel(true);
        }
        if (service != null) {
            service.cancel(true);
        }
    }

    public class NPSScoreValuesModel {
        private final String NPS_MASTER_KEY;
        private final String POLICY_NO;
        private final String REQUEST_DATE;
        private final String RESPONDED_ON;
        private final String NOT_GO_WELL_OTHERS;
        private final String OTHER_REASON_0_6R;
        private final String CHANNEL;
        private final String CREATED_BY;
        private final String CREATED_DATE;
        private final String PROJECT_TYPE;

        private int RATING = 0;
        private double promoters = 0, detractors = 0, passives = 0;
        private double NPSScore = 0;
        private double total = 0;
        private final String PRIMARY_COMMENT;
        private final String IMPROVE_FURTHER_CALL;
        private final String LIKELY_RECOMMEND_SBI_LIFE;

        /*private String PRIMARY_QUESTION, ONLN_PREM_PAY_EXP, PREM_RECEIPT, SBI_EXP, CUST_CAREEXP,
                SALES_PERSONNEL, PRODUCT_EXP_EXP, PREM_PAY_TRANS_FAILED, PREM_PAY_APP_NOTUSERFRNDLY,
                PREM_PAY_LENGTHY_PAYPROCESS, PREM_PAY_NO_ACKNOWLEDGEMENT, PREM_PAY_REM_NOTRECEIVED,
                PREM_RECEIPT_NOTRECEIVED_TRANS, PREM_RECEIPT_NOTREC_REGULARLY, PREM_RECEIPT_INFO_INCORRECT,
                PREM_RECEIPT_NONOFABOVE, SBI_EXP_KNOWLEDGE, SBI_EXP_TIME_TAKEN, SBI_EXP_BEHAVIOUR_COURTESY,
                SBI_EXP_NONOFABOVE, CUST_CARE_AUDIOCLARITY, CUST_CARE_KNOWLEDGE_PERSONNEL, CUST_CARE_RESOLUTION,
                CUST_CARE_BEHAVIOUR, CUST_CARE_NONOFABOVE, SALE_UNAVAILABLE_SERVICE, SALES_KNOWLEDGE_PERSONNEL,
                SALES_RESOLUTION_TIME, SALES_BEHAVIOUR, SALES_NONOFABOVE, PROD_EXP_POLICY, PROD_EXP_INVESTMENT_RETURN,
                PROD_EXP_RETURN_DIFFER, PROD_EXP_NONOFABOVE, NOTA_ONLN_PREM_PAY_EXP, NOTA_PREMIUM_RECEIPT,
                NOTA_SBI_EXP, NOTA_CUST_CARE, NOTA_SALES_PERSONNEL, NOTA_PROD_EXP, ONLN_PREM_PAY_EXP_LIMITED, ONLN_PREM_PAY_EXP_NONEOFABOVE,
                REGION;*/

        public NPSScoreValuesModel(String NPS_MASTER_KEY, String POLICY_NO, String REQUEST_DATE,
                                   String RESPONDED_ON, int RATING, String NOT_GO_WELL_OTHERS,
                                   String OTHER_REASON_0_6R, String CHANNEL, String CREATED_BY,
                                   String CREATED_DATE, double promoters, double detractors,
                                   double NPSScore, String PROJECT_TYPE, double total, double passives,
                                   String PRIMARY_COMMENT, String IMPROVE_FURTHER_CALL,
                                   String LIKELY_RECOMMEND_SBI_LIFE)
            //Added updation on 3-Nov-2020
                                   /*String PRIMARY_QUESTION, String ONLN_PREM_PAY_EXP, String PREM_RECEIPT, String SBI_EXP,
                                   String CUST_CAREEXP, String SALES_PERSONNEL, String PRODUCT_EXP_EXP, String PREM_PAY_TRANS_FAILED,
                                   String PREM_PAY_APP_NOTUSERFRNDLY,
                                   String PREM_PAY_LENGTHY_PAYPROCESS, String PREM_PAY_NO_ACKNOWLEDGEMENT,
                                   String PREM_PAY_REM_NOTRECEIVED,
                                   String PREM_RECEIPT_NOTRECEIVED_TRANS, String PREM_RECEIPT_NOTREC_REGULARLY,
                                   String PREM_RECEIPT_INFO_INCORRECT,
                                   String PREM_RECEIPT_NONOFABOVE, String SBI_EXP_KNOWLEDGE, String SBI_EXP_TIME_TAKEN,
                                   String SBI_EXP_BEHAVIOUR_COURTESY,
                                   String SBI_EXP_NONOFABOVE, String CUST_CARE_AUDIOCLARITY, String CUST_CARE_KNOWLEDGE_PERSONNEL,
                                   String CUST_CARE_RESOLUTION,
                                   String CUST_CARE_BEHAVIOUR, String CUST_CARE_NONOFABOVE, String SALE_UNAVAILABLE_SERVICE,
                                   String SALES_KNOWLEDGE_PERSONNEL,
                                   String SALES_RESOLUTION_TIME, String SALES_BEHAVIOUR, String SALES_NONOFABOVE, String PROD_EXP_POLICY,
                                   String PROD_EXP_INVESTMENT_RETURN,
                                   String PROD_EXP_RETURN_DIFFER, String PROD_EXP_NONOFABOVE, String NOTA_ONLN_PREM_PAY_EXP,
                                   String NOTA_PREMIUM_RECEIPT,
                                   String NOTA_SBI_EXP, String NOTA_CUST_CARE, String NOTA_SALES_PERSONNEL, String NOTA_PROD_EXP,
                                   String ONLN_PREM_PAY_EXP_LIMITED,
                                   String ONLN_PREM_PAY_EXP_NONEOFABOVE, String REGION)*/ {
            this.NPS_MASTER_KEY = NPS_MASTER_KEY;
            this.POLICY_NO = POLICY_NO;
            this.REQUEST_DATE = REQUEST_DATE;
            this.RESPONDED_ON = RESPONDED_ON;
            this.RATING = RATING;
            this.NOT_GO_WELL_OTHERS = NOT_GO_WELL_OTHERS;
            this.OTHER_REASON_0_6R = OTHER_REASON_0_6R;
            this.CHANNEL = CHANNEL;
            this.CREATED_BY = CREATED_BY;
            this.CREATED_DATE = CREATED_DATE;
            this.promoters = promoters;
            this.detractors = detractors;
            this.NPSScore = NPSScore;
            this.PROJECT_TYPE = PROJECT_TYPE;
            this.total = total;
            this.passives = passives;
            this.PRIMARY_COMMENT = PRIMARY_COMMENT;
            this.IMPROVE_FURTHER_CALL = IMPROVE_FURTHER_CALL;
            this.LIKELY_RECOMMEND_SBI_LIFE = LIKELY_RECOMMEND_SBI_LIFE;

        }

        public String getNPS_MASTER_KEY() {
            return NPS_MASTER_KEY;
        }

        public String getPOLICY_NO() {
            return POLICY_NO;
        }

        public String getREQUEST_DATE() {
            return REQUEST_DATE;
        }

        public String getRESPONDED_ON() {
            return RESPONDED_ON;
        }


        public String getNOT_GO_WELL_OTHERS() {
            return NOT_GO_WELL_OTHERS;
        }

        public String getOTHER_REASON_0_6R() {
            return OTHER_REASON_0_6R;
        }

        public String getCHANNEL() {
            return CHANNEL;
        }

        public String getCREATED_BY() {
            return CREATED_BY;
        }

        public String getCREATED_DATE() {
            return CREATED_DATE;
        }

        public String getPROJECT_TYPE() {
            return PROJECT_TYPE;
        }

        public int getRATING() {
            return RATING;
        }

        public double getPromoters() {
            return promoters;
        }

        public double getDetractors() {
            return detractors;
        }

        public double getPassives() {
            return passives;
        }

        public double getNPSScore() {
            return NPSScore;
        }

        public double getTotal() {
            return total;
        }

        public String getPRIMARY_COMMENT() {
            return PRIMARY_COMMENT;
        }

        public String getIMPROVE_FURTHER_CALL() {
            return IMPROVE_FURTHER_CALL;
        }

        public String getLIKELY_RECOMMEND_SBI_LIFE() {
            return LIKELY_RECOMMEND_SBI_LIFE;
        }

    }

    public ArrayList<NPSScoreValuesModel> parseNodeNPSScore(List<String> lsNode) {
        ArrayList<NPSScoreValuesModel> lsData = new ArrayList<>();

        String NPS_MASTER_KEY, POLICY_NO, REQUEST_DATE, RESPONDED_ON, NOT_GO_WELL_TIME_TAK_ISSUE_POL,
                NOT_GO_WELL_OTHERS, OTHER_REASON_0_6R, CHANNEL, CREATED_BY, CREATED_DATE, PROJECT_TYPE;

        double promotersOnboarding = 0, detractorsOnboarding = 0, passivesOnboarding = 0;
        //Service Exp Online Payment
        double promotersServiceExpOnlinePayment = 0, detractorsServiceExpOnlinePayment = 0, passivesServiceExpOnlinePayment = 0;
        //Service Experience Registered
        double promotersServiceExpeRegd = 0, detractorsServiceExpeRegd = 0, passivesServiceExpeRegd = 0;
        //Service Experience SBI Bank
        double promotersServiceExpeSBIBank = 0, detractorsServiceExpeSBIBank = 0, passivesServiceExpeSBIBank = 0;
        //Service Experience SBI Life
        double promotersServiceExpeSBILife = 0, detractorsServiceExpeSBILife = 0, passivesServiceExpeSBILife = 0;
        double promotersRenewal = 0, detractorsRenewal = 0, passivesRenewal = 0;
        double promotersLivingBenefit = 0, detractorsLivingBenefit = 0, passivesLivingBenefit = 0;
        double onBoardingTotal = 0, renewalTotal = 0, livingBenefitTotal = 0, passivesTotal = 0,
                serviceExpOnlinePaymentTotal = 0, serviceExpeRegdTotal = 0, serviceExpeSBIBankTotal = 0, serviceExpeSBILifeTotal = 0;

        int RATING = 0;
        double NPSScore = 0.0;
        String PRIMARY_COMMENT, IMPROVE_FURTHER_CALL, IMPROVE_FURTHER_STATUS_UPDATE, NOT_GO_WELL_PROD_INFO_SALES,
                IMPROVE_FURTHER_MEDICAL_PROCES, LIKELY_RECOMMEND_SBI_LIFE, IMPROVE_REQ_SUBMISSION_PROCESS,
                HAPPY_OTHER_REASON_LB, HAPPY_OTHER_REASON_LB_9_10R, HAPPY_OTHER_REASON_RENEWAL,
                HAPPY_RENEWAL_OTH_REASON_9_10R, POOR_OTHER_REASON_RENEWAL;

        String NOT_GO_WELL_PROP_FIL_PROCESS, NOT_GO_WELL_SUPPORT_SALES, NOT_GO_WELL_DOC_REQ,
                NOT_GO_WELL_CALL, NOT_GO_WELL_STATUS_UPDATE, NOT_GO_WELL_MEDICAL_PROCESS;

        String IMPROVE_FURTHER, IMPROVE_FUR_PROD_INFO_SALES, IMPROVE_FUR_PROP_FILLI_PROCESS, IMPROVE_FURTHER_SUPPORT_SALES,
                IMPROVE_FURTHER_DOC_REQ, IMPROVE_FUR_TIME_TAK_ISSUE_POL, IMPROVE_FURTHER_OTHERS,
                IMPROVE_RENEWAL_PAYMENT_EXP, IMPROVE_ONLINE_SERVICING_EXP,
                IMPROVE_EXP_CUST_CARE_BRANCH, IMPROVE_EXP_SALES_PERSON, IMPROVE_COMM_PLCY_STMT, IMPROVE_OTHER_REASON_RENEWAL,
                IMPROVE_RENE_OTHER_REASON_7_8R, IMPROVE_TIME_TAKEN_PROCES_PAYM, IMPROVE_QUALI_SERV_BRANCH_OFFI, IMPROVE_RETURN_INVEST,
                IMPROVE_OTHER_REASON_LB, IMPROVE_OTHER_REASON_LB_7_8R;

        String OTHER_REASON_7_8R, POOR_RENEWAL_OTHER_REASON_0_6R, POOR_OTHER_REASON_LB, POOR_OTHER_REASON_LB_06R;
        String POOR_RENEWAL_PAYMENT_EXP, POOR_ONLINE_SERVICING_EXP, POOR_EXP_CUST_CARE_BRANCH,
                POOR_EXP_SALES_PERSON, DELAY_RECEIPT_COMM_PLCY_STMT, HAPPY_RENEWAL_PAYMENT_EXP,
                HAPPY_ONLINE_SERVICING_EXP, HAPPY_EXP_CUST_CARE_BRANCH, HAPPY_EXP_SALES_PERSON,
                REGULAR_RECEIPT_COMM_PLCY_STMT,
                EASY_TO_PAY_RENEWAL_PREM, COMPLEX_REQ_SUBMISSION_PROCESS,
                DELAYED_PROCESSING_PAYMENT, KNOWLEDGE_BRANCH_OFFICIAL, RETURN_INVEST_NOT_AS_EXPECTED,
                SIMPLE_REQ_SUBMISSION_PROCESS,
                PROMPT_PROCESSING_PAYMENT, SERVICE_RENDERED_BRANCH_OFFICI, GOOD_RETURN_INVEST,
                NOT_HELPFUL_BRANCH_OFF;

        //Added by Tushar on 03-Nov-2020
        String PRIMARY_QUESTION, ONLN_PREM_PAY_EXP, PREM_RECEIPT, SBI_EXP, CUST_CAREEXP,
                SALES_PERSONNEL, PRODUCT_EXP_EXP, PREM_PAY_TRANS_FAILED, PREM_PAY_APP_NOTUSERFRNDLY,
                PREM_PAY_LENGTHY_PAYPROCESS, PREM_PAY_NO_ACKNOWLEDGEMENT, PREM_PAY_REM_NOTRECEIVED,
                PREM_RECEIPT_NOTRECEIVED_TRANS, PREM_RECEIPT_NOTREC_REGULARLY, PREM_RECEIPT_INFO_INCORRECT,
                PREM_RECEIPT_NONOFABOVE, SBI_EXP_KNOWLEDGE, SBI_EXP_TIME_TAKEN, SBI_EXP_BEHAVIOUR_COURTESY,
                SBI_EXP_NONOFABOVE, CUST_CARE_AUDIOCLARITY, CUST_CARE_KNOWLEDGE_PERSONNEL, CUST_CARE_RESOLUTION,
                CUST_CARE_BEHAVIOUR, CUST_CARE_NONOFABOVE, SALE_UNAVAILABLE_SERVICE, SALES_KNOWLEDGE_PERSONNEL,
                SALES_RESOLUTION_TIME, SALES_BEHAVIOUR, SALES_NONOFABOVE, PROD_EXP_POLICY, PROD_EXP_INVESTMENT_RETURN,
                PROD_EXP_RETURN_DIFFER, PROD_EXP_NONOFABOVE, NOTA_ONLN_PREM_PAY_EXP, NOTA_PREMIUM_RECEIPT,
                NOTA_SBI_EXP, NOTA_CUST_CARE, NOTA_SALES_PERSONNEL, NOTA_PROD_EXP, ONLN_PREM_PAY_EXP_LIMITED,
                ONLN_PREM_PAY_EXP_NONEOFABOVE, REGION;
        ParseXML parseXML = new ParseXML();
        for (String Node : lsNode) {
            NPS_MASTER_KEY = parseXML.parseXmlTag(Node, "NPS_MASTER_KEY");
            NPS_MASTER_KEY = NPS_MASTER_KEY == null ? "" : NPS_MASTER_KEY;

            POLICY_NO = parseXML.parseXmlTag(Node, "POLICY_NO");
            POLICY_NO = POLICY_NO == null ? "" : POLICY_NO;

            REQUEST_DATE = parseXML.parseXmlTag(Node, "REQUEST_DATE");
            REQUEST_DATE = REQUEST_DATE == null ? "" : REQUEST_DATE;

            RESPONDED_ON = parseXML.parseXmlTag(Node, "RESPONDED_ON");
            RESPONDED_ON = RESPONDED_ON == null ? "" : RESPONDED_ON;

            try {
                RATING = Integer.valueOf(parseXML.parseXmlTag(Node, "RATING"));
            } catch (Exception e) {
                RATING = 0;
            }

            NOT_GO_WELL_TIME_TAK_ISSUE_POL = parseXML.parseXmlTag(Node, "NOT_GO_WELL_TIME_TAK_ISSUE_POL");
            NOT_GO_WELL_TIME_TAK_ISSUE_POL = NOT_GO_WELL_TIME_TAK_ISSUE_POL == null ? "" : NOT_GO_WELL_TIME_TAK_ISSUE_POL;

            NOT_GO_WELL_OTHERS = parseXML.parseXmlTag(Node, "NOT_GO_WELL_OTHERS");
            NOT_GO_WELL_OTHERS = NOT_GO_WELL_OTHERS == null ? "" : NOT_GO_WELL_OTHERS;
            if (!TextUtils.isEmpty(NOT_GO_WELL_OTHERS)) {
                NOT_GO_WELL_OTHERS = NOT_GO_WELL_OTHERS + ",";
            }

            if (!TextUtils.isEmpty(NOT_GO_WELL_TIME_TAK_ISSUE_POL)) {
                NOT_GO_WELL_OTHERS += NOT_GO_WELL_TIME_TAK_ISSUE_POL + ",";
            }

            NOT_GO_WELL_PROP_FIL_PROCESS = parseXML.parseXmlTag(Node, "NOT_GO_WELL_PROP_FIL_PROCESS");
            NOT_GO_WELL_PROP_FIL_PROCESS = NOT_GO_WELL_PROP_FIL_PROCESS == null ? "" : NOT_GO_WELL_PROP_FIL_PROCESS;

            if (!TextUtils.isEmpty(NOT_GO_WELL_PROP_FIL_PROCESS)) {
                NOT_GO_WELL_OTHERS += NOT_GO_WELL_PROP_FIL_PROCESS + ",";
            }

            NOT_GO_WELL_SUPPORT_SALES = parseXML.parseXmlTag(Node, "NOT_GO_WELL_SUPPORT_SALES");
            NOT_GO_WELL_SUPPORT_SALES = NOT_GO_WELL_SUPPORT_SALES == null ? "" : NOT_GO_WELL_SUPPORT_SALES;

            if (!TextUtils.isEmpty(NOT_GO_WELL_SUPPORT_SALES)) {
                NOT_GO_WELL_OTHERS += NOT_GO_WELL_SUPPORT_SALES + ",";
            }

            NOT_GO_WELL_DOC_REQ = parseXML.parseXmlTag(Node, "NOT_GO_WELL_DOC_REQ");
            NOT_GO_WELL_DOC_REQ = NOT_GO_WELL_DOC_REQ == null ? "" : NOT_GO_WELL_DOC_REQ;
            if (!TextUtils.isEmpty(NOT_GO_WELL_DOC_REQ)) {
                NOT_GO_WELL_OTHERS += NOT_GO_WELL_DOC_REQ + ",";
            }


            NOT_GO_WELL_CALL = parseXML.parseXmlTag(Node, "NOT_GO_WELL_CALL");
            NOT_GO_WELL_CALL = NOT_GO_WELL_CALL == null ? "" : NOT_GO_WELL_CALL;
            if (!TextUtils.isEmpty(NOT_GO_WELL_CALL)) {
                NOT_GO_WELL_OTHERS += NOT_GO_WELL_CALL + ",";
            }

            NOT_GO_WELL_STATUS_UPDATE = parseXML.parseXmlTag(Node, "NOT_GO_WELL_STATUS_UPDATE");
            NOT_GO_WELL_STATUS_UPDATE = NOT_GO_WELL_STATUS_UPDATE == null ? "" : NOT_GO_WELL_STATUS_UPDATE;
            if (!TextUtils.isEmpty(NOT_GO_WELL_STATUS_UPDATE)) {
                NOT_GO_WELL_OTHERS += NOT_GO_WELL_STATUS_UPDATE + ",";
            }

            NOT_GO_WELL_MEDICAL_PROCESS = parseXML.parseXmlTag(Node, "NOT_GO_WELL_MEDICAL_PROCESS");
            NOT_GO_WELL_MEDICAL_PROCESS = NOT_GO_WELL_MEDICAL_PROCESS == null ? "" : NOT_GO_WELL_MEDICAL_PROCESS;
            if (!TextUtils.isEmpty(NOT_GO_WELL_MEDICAL_PROCESS)) {
                NOT_GO_WELL_OTHERS += NOT_GO_WELL_MEDICAL_PROCESS + ",";
            }

            NOT_GO_WELL_PROD_INFO_SALES = parseXML.parseXmlTag(Node, "NOT_GO_WELL_PROD_INFO_SALES");
            NOT_GO_WELL_PROD_INFO_SALES = NOT_GO_WELL_PROD_INFO_SALES == null ? "" : NOT_GO_WELL_PROD_INFO_SALES;
            if (!TextUtils.isEmpty(NOT_GO_WELL_PROD_INFO_SALES)) {
                NOT_GO_WELL_OTHERS += NOT_GO_WELL_PROD_INFO_SALES + ",";
            }

            OTHER_REASON_0_6R = parseXML.parseXmlTag(Node, "OTHER_REASON_0_6R");
            OTHER_REASON_0_6R = OTHER_REASON_0_6R == null ? "" : OTHER_REASON_0_6R;
            if (!TextUtils.isEmpty(OTHER_REASON_0_6R)) {
                OTHER_REASON_0_6R = OTHER_REASON_0_6R + ",";
            }

            HAPPY_OTHER_REASON_LB = parseXML.parseXmlTag(Node, "HAPPY_OTHER_REASON_LB");
            HAPPY_OTHER_REASON_LB = HAPPY_OTHER_REASON_LB == null ? "" : HAPPY_OTHER_REASON_LB;
            if (!TextUtils.isEmpty(HAPPY_OTHER_REASON_LB)) {
                OTHER_REASON_0_6R += HAPPY_OTHER_REASON_LB + ",";
            }


            HAPPY_OTHER_REASON_LB_9_10R = parseXML.parseXmlTag(Node, "HAPPY_OTHER_REASON_LB_9_10R");
            HAPPY_OTHER_REASON_LB_9_10R = HAPPY_OTHER_REASON_LB_9_10R == null ? "" : HAPPY_OTHER_REASON_LB_9_10R;
            if (!TextUtils.isEmpty(HAPPY_OTHER_REASON_LB_9_10R)) {
                OTHER_REASON_0_6R += HAPPY_OTHER_REASON_LB_9_10R + ",";
            }

            HAPPY_OTHER_REASON_RENEWAL = parseXML.parseXmlTag(Node, "HAPPY_OTHER_REASON_RENEWAL");
            HAPPY_OTHER_REASON_RENEWAL = HAPPY_OTHER_REASON_RENEWAL == null ? "" : HAPPY_OTHER_REASON_RENEWAL;
            if (!TextUtils.isEmpty(HAPPY_OTHER_REASON_RENEWAL)) {
                OTHER_REASON_0_6R += HAPPY_OTHER_REASON_RENEWAL + ",";
            }

            HAPPY_RENEWAL_OTH_REASON_9_10R = parseXML.parseXmlTag(Node, "HAPPY_RENEWAL_OTH_REASON_9_10R");
            HAPPY_RENEWAL_OTH_REASON_9_10R = HAPPY_RENEWAL_OTH_REASON_9_10R == null ? "" : HAPPY_RENEWAL_OTH_REASON_9_10R;
            if (!TextUtils.isEmpty(HAPPY_RENEWAL_OTH_REASON_9_10R)) {
                OTHER_REASON_0_6R += HAPPY_RENEWAL_OTH_REASON_9_10R + ",";
            }

            POOR_OTHER_REASON_RENEWAL = parseXML.parseXmlTag(Node, "POOR_OTHER_REASON_RENEWAL");
            POOR_OTHER_REASON_RENEWAL = POOR_OTHER_REASON_RENEWAL == null ? "" : POOR_OTHER_REASON_RENEWAL;
            if (!TextUtils.isEmpty(POOR_OTHER_REASON_RENEWAL)) {
                OTHER_REASON_0_6R += POOR_OTHER_REASON_RENEWAL + ",";
            }
            OTHER_REASON_7_8R = parseXML.parseXmlTag(Node, "OTHER_REASON_7_8R");
            OTHER_REASON_7_8R = OTHER_REASON_7_8R == null ? "" : OTHER_REASON_7_8R;
            if (!TextUtils.isEmpty(OTHER_REASON_7_8R)) {
                OTHER_REASON_0_6R += OTHER_REASON_7_8R + ",";
            }

            POOR_RENEWAL_OTHER_REASON_0_6R = parseXML.parseXmlTag(Node, "POOR_RENEWAL_OTHER_REASON_0_6R");
            POOR_RENEWAL_OTHER_REASON_0_6R = POOR_RENEWAL_OTHER_REASON_0_6R == null ? "" : POOR_RENEWAL_OTHER_REASON_0_6R;
            if (!TextUtils.isEmpty(POOR_RENEWAL_OTHER_REASON_0_6R)) {
                OTHER_REASON_0_6R += POOR_RENEWAL_OTHER_REASON_0_6R + ",";
            }

            POOR_OTHER_REASON_LB = parseXML.parseXmlTag(Node, "POOR_OTHER_REASON_LB");
            POOR_OTHER_REASON_LB = POOR_OTHER_REASON_LB == null ? "" : POOR_OTHER_REASON_LB;
            if (!TextUtils.isEmpty(POOR_OTHER_REASON_LB)) {
                OTHER_REASON_0_6R += POOR_OTHER_REASON_LB + ",";
            }

            POOR_OTHER_REASON_LB_06R = parseXML.parseXmlTag(Node, "POOR_OTHER_REASON_LB_06R");
            POOR_OTHER_REASON_LB_06R = POOR_OTHER_REASON_LB_06R == null ? "" : POOR_OTHER_REASON_LB_06R;
            if (!TextUtils.isEmpty(POOR_OTHER_REASON_LB_06R)) {
                OTHER_REASON_0_6R += POOR_OTHER_REASON_LB_06R + ",";
            }
            IMPROVE_OTHER_REASON_RENEWAL = parseXML.parseXmlTag(Node, "IMPROVE_OTHER_REASON_RENEWAL");
            IMPROVE_OTHER_REASON_RENEWAL = IMPROVE_OTHER_REASON_RENEWAL == null ? "" : IMPROVE_OTHER_REASON_RENEWAL;
            if (!TextUtils.isEmpty(IMPROVE_OTHER_REASON_RENEWAL)) {
                OTHER_REASON_0_6R += IMPROVE_OTHER_REASON_RENEWAL + ",";
            }

            IMPROVE_RENE_OTHER_REASON_7_8R = parseXML.parseXmlTag(Node, "IMPROVE_RENE_OTHER_REASON_7_8R");
            IMPROVE_RENE_OTHER_REASON_7_8R = IMPROVE_RENE_OTHER_REASON_7_8R == null ? "" : IMPROVE_RENE_OTHER_REASON_7_8R;
            if (!TextUtils.isEmpty(IMPROVE_RENE_OTHER_REASON_7_8R)) {
                OTHER_REASON_0_6R += IMPROVE_RENE_OTHER_REASON_7_8R + ",";
            }


            IMPROVE_OTHER_REASON_LB = parseXML.parseXmlTag(Node, "IMPROVE_OTHER_REASON_LB");
            IMPROVE_OTHER_REASON_LB = IMPROVE_OTHER_REASON_LB == null ? "" : IMPROVE_OTHER_REASON_LB;
            if (!TextUtils.isEmpty(IMPROVE_OTHER_REASON_LB)) {
                OTHER_REASON_0_6R += IMPROVE_OTHER_REASON_LB + ",";
            }

            IMPROVE_OTHER_REASON_LB_7_8R = parseXML.parseXmlTag(Node, "IMPROVE_OTHER_REASON_LB_7_8R");
            IMPROVE_OTHER_REASON_LB_7_8R = IMPROVE_OTHER_REASON_LB_7_8R == null ? "" : IMPROVE_OTHER_REASON_LB_7_8R;
            if (!TextUtils.isEmpty(IMPROVE_OTHER_REASON_LB_7_8R)) {
                OTHER_REASON_0_6R += IMPROVE_OTHER_REASON_LB_7_8R + ",";
            }

            IMPROVE_FURTHER_OTHERS = parseXML.parseXmlTag(Node, "IMPROVE_FURTHER_OTHERS");
            IMPROVE_FURTHER_OTHERS = IMPROVE_FURTHER_OTHERS == null ? "" : IMPROVE_FURTHER_OTHERS;
            if (!TextUtils.isEmpty(IMPROVE_FURTHER_OTHERS)) {
                OTHER_REASON_0_6R += IMPROVE_FURTHER_OTHERS + ",";
            }
            CHANNEL = parseXML.parseXmlTag(Node, "CHANNEL");
            CHANNEL = CHANNEL == null ? "" : CHANNEL;

            CREATED_BY = parseXML.parseXmlTag(Node, "CREATED_BY");
            CREATED_BY = CREATED_BY == null ? "" : CREATED_BY;

            CREATED_DATE = parseXML.parseXmlTag(Node, "CREATED_DATE");
            CREATED_DATE = CREATED_DATE == null ? "" : CREATED_DATE;

            PROJECT_TYPE = parseXML.parseXmlTag(Node, "PROJECT_TYPE");
            PROJECT_TYPE = PROJECT_TYPE == null ? "" : PROJECT_TYPE;

            LIKELY_RECOMMEND_SBI_LIFE = parseXML.parseXmlTag(Node, "LIKELY_RECOMMEND_SBI_LIFE");
            LIKELY_RECOMMEND_SBI_LIFE = LIKELY_RECOMMEND_SBI_LIFE == null ? "" : LIKELY_RECOMMEND_SBI_LIFE;


            if (PROJECT_TYPE.equalsIgnoreCase("Onboarding")) {
                onBoardingTotal++;
                if (RATING > 8) {
                    promotersOnboarding++;
                } else if (RATING < 7) {
                    detractorsOnboarding++;
                } else {
                    passivesOnboarding++;
                }
            } else if (PROJECT_TYPE.equalsIgnoreCase("Renewal")) {
                renewalTotal++;
                if (RATING > 8) {
                    promotersRenewal++;
                } else if (RATING < 7) {
                    detractorsRenewal++;
                } else {
                    passivesRenewal++;
                }
            } else if (PROJECT_TYPE.equalsIgnoreCase("Service Exp Online Payment")) {
                serviceExpOnlinePaymentTotal++;
                if (RATING > 8) {
                    promotersServiceExpOnlinePayment++;
                } else if (RATING < 7) {
                    detractorsServiceExpOnlinePayment++;
                } else {
                    passivesServiceExpOnlinePayment++;
                }
            } else if (PROJECT_TYPE.equalsIgnoreCase("Service Experience Registered")) {
                serviceExpeRegdTotal++;
                if (RATING > 8) {
                    promotersServiceExpeRegd++;
                } else if (RATING < 7) {
                    detractorsServiceExpeRegd++;
                } else {
                    passivesServiceExpeRegd++;
                }
            } else if (PROJECT_TYPE.equalsIgnoreCase("Service Experience SBI Bank")) {
                serviceExpeSBIBankTotal++;
                if (RATING > 8) {
                    promotersServiceExpeSBIBank++;
                } else if (RATING < 7) {
                    detractorsServiceExpeSBIBank++;
                } else {
                    passivesServiceExpeSBIBank++;
                }
            } else if (PROJECT_TYPE.equalsIgnoreCase("Service Experience SBI Life")) {
                serviceExpeSBILifeTotal++;
                if (RATING > 8) {
                    promotersServiceExpeSBILife++;
                } else if (RATING < 7) {
                    detractorsServiceExpeSBILife++;
                } else {
                    passivesServiceExpeSBILife++;
                }
            } else if (PROJECT_TYPE.equalsIgnoreCase("Living Benefit")) {
                try {
                    RATING = Integer.valueOf(LIKELY_RECOMMEND_SBI_LIFE);
                } catch (Exception e) {
                    RATING = 0;
                }
                livingBenefitTotal++;
                if (RATING > 8) {
                    promotersLivingBenefit++;
                } else if (RATING < 7) {
                    detractorsLivingBenefit++;
                } else {
                    passivesLivingBenefit++;
                }

            }

            PRIMARY_COMMENT = parseXML.parseXmlTag(Node, "PRIMARY_COMMENT");
            PRIMARY_COMMENT = PRIMARY_COMMENT == null ? "" : PRIMARY_COMMENT;


            IMPROVE_FURTHER_CALL = parseXML.parseXmlTag(Node, "IMPROVE_FURTHER_CALL");
            IMPROVE_FURTHER_CALL = IMPROVE_FURTHER_CALL == null ? "" : IMPROVE_FURTHER_CALL;
            if (!TextUtils.isEmpty(IMPROVE_FURTHER_CALL)) {
                IMPROVE_FURTHER_CALL = IMPROVE_FURTHER_CALL + ",";
            }

            IMPROVE_FURTHER_STATUS_UPDATE = parseXML.parseXmlTag(Node, "IMPROVE_FURTHER_STATUS_UPDATE");
            IMPROVE_FURTHER_STATUS_UPDATE = IMPROVE_FURTHER_STATUS_UPDATE == null ? "" : IMPROVE_FURTHER_STATUS_UPDATE;
            if (!TextUtils.isEmpty(IMPROVE_FURTHER_STATUS_UPDATE)) {
                IMPROVE_FURTHER_CALL += IMPROVE_FURTHER_STATUS_UPDATE + ",";
            }

            IMPROVE_FURTHER_MEDICAL_PROCES = parseXML.parseXmlTag(Node, "IMPROVE_FURTHER_MEDICAL_PROCES");
            IMPROVE_FURTHER_MEDICAL_PROCES = IMPROVE_FURTHER_MEDICAL_PROCES == null ? "" : IMPROVE_FURTHER_MEDICAL_PROCES;
            if (!TextUtils.isEmpty(IMPROVE_FURTHER_MEDICAL_PROCES)) {
                IMPROVE_FURTHER_CALL += IMPROVE_FURTHER_MEDICAL_PROCES + ",";
            }

            IMPROVE_REQ_SUBMISSION_PROCESS = parseXML.parseXmlTag(Node, "IMPROVE_REQ_SUBMISSION_PROCESS");
            IMPROVE_REQ_SUBMISSION_PROCESS = IMPROVE_REQ_SUBMISSION_PROCESS == null ? "" : IMPROVE_REQ_SUBMISSION_PROCESS;
            if (!TextUtils.isEmpty(IMPROVE_REQ_SUBMISSION_PROCESS)) {
                IMPROVE_FURTHER_CALL += IMPROVE_REQ_SUBMISSION_PROCESS + ",";
            }

            IMPROVE_FURTHER = parseXML.parseXmlTag(Node, "IMPROVE_FURTHER");
            IMPROVE_FURTHER = IMPROVE_FURTHER == null ? "" : IMPROVE_FURTHER;
            if (!TextUtils.isEmpty(IMPROVE_FURTHER)) {
                IMPROVE_FURTHER_CALL += IMPROVE_FURTHER + ",";
            }

            IMPROVE_FUR_PROD_INFO_SALES = parseXML.parseXmlTag(Node, "IMPROVE_FUR_PROD_INFO_SALES");
            IMPROVE_FUR_PROD_INFO_SALES = IMPROVE_FUR_PROD_INFO_SALES == null ? "" : IMPROVE_FUR_PROD_INFO_SALES;
            if (!TextUtils.isEmpty(IMPROVE_FUR_PROD_INFO_SALES)) {
                IMPROVE_FURTHER_CALL += IMPROVE_FUR_PROD_INFO_SALES + ",";
            }

            IMPROVE_FUR_PROP_FILLI_PROCESS = parseXML.parseXmlTag(Node, "IMPROVE_FUR_PROP_FILLI_PROCESS");
            IMPROVE_FUR_PROP_FILLI_PROCESS = IMPROVE_FUR_PROP_FILLI_PROCESS == null ? "" : IMPROVE_FUR_PROP_FILLI_PROCESS;
            if (!TextUtils.isEmpty(IMPROVE_FUR_PROP_FILLI_PROCESS)) {
                IMPROVE_FURTHER_CALL += IMPROVE_FUR_PROP_FILLI_PROCESS + ",";
            }

            IMPROVE_FURTHER_SUPPORT_SALES = parseXML.parseXmlTag(Node, "IMPROVE_FURTHER_SUPPORT_SALES");
            IMPROVE_FURTHER_SUPPORT_SALES = IMPROVE_FURTHER_SUPPORT_SALES == null ? "" : IMPROVE_FURTHER_SUPPORT_SALES;
            if (!TextUtils.isEmpty(IMPROVE_FURTHER_SUPPORT_SALES)) {
                IMPROVE_FURTHER_CALL += IMPROVE_FURTHER_SUPPORT_SALES + ",";
            }

            IMPROVE_FURTHER_DOC_REQ = parseXML.parseXmlTag(Node, "IMPROVE_FURTHER_DOC_REQ");
            IMPROVE_FURTHER_DOC_REQ = IMPROVE_FURTHER_DOC_REQ == null ? "" : IMPROVE_FURTHER_DOC_REQ;
            if (!TextUtils.isEmpty(IMPROVE_FURTHER_DOC_REQ)) {
                IMPROVE_FURTHER_CALL += IMPROVE_FURTHER_DOC_REQ + ",";
            }

            IMPROVE_FUR_TIME_TAK_ISSUE_POL = parseXML.parseXmlTag(Node, "IMPROVE_FUR_TIME_TAK_ISSUE_POL");
            IMPROVE_FUR_TIME_TAK_ISSUE_POL = IMPROVE_FUR_TIME_TAK_ISSUE_POL == null ? "" : IMPROVE_FUR_TIME_TAK_ISSUE_POL;
            if (!TextUtils.isEmpty(IMPROVE_FUR_TIME_TAK_ISSUE_POL)) {
                IMPROVE_FURTHER_CALL += IMPROVE_FUR_TIME_TAK_ISSUE_POL + ",";
            }

            IMPROVE_RENEWAL_PAYMENT_EXP = parseXML.parseXmlTag(Node, "IMPROVE_RENEWAL_PAYMENT_EXP");
            IMPROVE_RENEWAL_PAYMENT_EXP = IMPROVE_RENEWAL_PAYMENT_EXP == null ? "" : IMPROVE_RENEWAL_PAYMENT_EXP;
            if (!TextUtils.isEmpty(IMPROVE_RENEWAL_PAYMENT_EXP)) {
                IMPROVE_FURTHER_CALL += IMPROVE_RENEWAL_PAYMENT_EXP + ",";
            }

            IMPROVE_ONLINE_SERVICING_EXP = parseXML.parseXmlTag(Node, "IMPROVE_ONLINE_SERVICING_EXP");
            IMPROVE_ONLINE_SERVICING_EXP = IMPROVE_ONLINE_SERVICING_EXP == null ? "" : IMPROVE_ONLINE_SERVICING_EXP;
            if (!TextUtils.isEmpty(IMPROVE_ONLINE_SERVICING_EXP)) {
                IMPROVE_FURTHER_CALL += IMPROVE_ONLINE_SERVICING_EXP + ",";
            }

            IMPROVE_EXP_CUST_CARE_BRANCH = parseXML.parseXmlTag(Node, "IMPROVE_EXP_CUST_CARE_BRANCH");
            IMPROVE_EXP_CUST_CARE_BRANCH = IMPROVE_EXP_CUST_CARE_BRANCH == null ? "" : IMPROVE_EXP_CUST_CARE_BRANCH;
            if (!TextUtils.isEmpty(IMPROVE_EXP_CUST_CARE_BRANCH)) {
                IMPROVE_FURTHER_CALL += IMPROVE_EXP_CUST_CARE_BRANCH + ",";
            }

            IMPROVE_EXP_SALES_PERSON = parseXML.parseXmlTag(Node, "IMPROVE_EXP_SALES_PERSON");
            IMPROVE_EXP_SALES_PERSON = IMPROVE_EXP_SALES_PERSON == null ? "" : IMPROVE_EXP_SALES_PERSON;
            if (!TextUtils.isEmpty(IMPROVE_EXP_SALES_PERSON)) {
                IMPROVE_FURTHER_CALL += IMPROVE_EXP_SALES_PERSON + ",";
            }

            IMPROVE_COMM_PLCY_STMT = parseXML.parseXmlTag(Node, "IMPROVE_COMM_PLCY_STMT");
            IMPROVE_COMM_PLCY_STMT = IMPROVE_COMM_PLCY_STMT == null ? "" : IMPROVE_COMM_PLCY_STMT;
            if (!TextUtils.isEmpty(IMPROVE_COMM_PLCY_STMT)) {
                IMPROVE_FURTHER_CALL += IMPROVE_COMM_PLCY_STMT + ",";
            }


            IMPROVE_TIME_TAKEN_PROCES_PAYM = parseXML.parseXmlTag(Node, "IMPROVE_TIME_TAKEN_PROCES_PAYM");
            IMPROVE_TIME_TAKEN_PROCES_PAYM = IMPROVE_TIME_TAKEN_PROCES_PAYM == null ? "" : IMPROVE_TIME_TAKEN_PROCES_PAYM;
            if (!TextUtils.isEmpty(IMPROVE_TIME_TAKEN_PROCES_PAYM)) {
                IMPROVE_FURTHER_CALL += IMPROVE_TIME_TAKEN_PROCES_PAYM + ",";
            }

            IMPROVE_QUALI_SERV_BRANCH_OFFI = parseXML.parseXmlTag(Node, "IMPROVE_QUALI_SERV_BRANCH_OFFI");
            IMPROVE_QUALI_SERV_BRANCH_OFFI = IMPROVE_QUALI_SERV_BRANCH_OFFI == null ? "" : IMPROVE_QUALI_SERV_BRANCH_OFFI;
            if (!TextUtils.isEmpty(IMPROVE_QUALI_SERV_BRANCH_OFFI)) {
                IMPROVE_FURTHER_CALL += IMPROVE_QUALI_SERV_BRANCH_OFFI + ",";
            }

            IMPROVE_RETURN_INVEST = parseXML.parseXmlTag(Node, "IMPROVE_RETURN_INVEST");
            IMPROVE_RETURN_INVEST = IMPROVE_RETURN_INVEST == null ? "" : IMPROVE_RETURN_INVEST;
            if (!TextUtils.isEmpty(IMPROVE_RETURN_INVEST)) {
                IMPROVE_FURTHER_CALL += IMPROVE_RETURN_INVEST + ",";
            }

            if (!TextUtils.isEmpty(NOT_GO_WELL_OTHERS)) {
                IMPROVE_FURTHER_CALL += NOT_GO_WELL_OTHERS + ",";
            }

            POOR_RENEWAL_PAYMENT_EXP = parseXML.parseXmlTag(Node, "POOR_RENEWAL_PAYMENT_EXP");
            POOR_RENEWAL_PAYMENT_EXP = POOR_RENEWAL_PAYMENT_EXP == null ? "" : POOR_RENEWAL_PAYMENT_EXP;
            if (!TextUtils.isEmpty(POOR_RENEWAL_PAYMENT_EXP)) {
                IMPROVE_FURTHER_CALL += POOR_RENEWAL_PAYMENT_EXP + ",";
            }
            POOR_ONLINE_SERVICING_EXP = parseXML.parseXmlTag(Node, "POOR_ONLINE_SERVICING_EXP");
            POOR_ONLINE_SERVICING_EXP = POOR_ONLINE_SERVICING_EXP == null ? "" : POOR_ONLINE_SERVICING_EXP;
            if (!TextUtils.isEmpty(POOR_ONLINE_SERVICING_EXP)) {
                IMPROVE_FURTHER_CALL += POOR_ONLINE_SERVICING_EXP + ",";
            }

            POOR_EXP_CUST_CARE_BRANCH = parseXML.parseXmlTag(Node, "POOR_EXP_CUST_CARE_BRANCH");
            POOR_EXP_CUST_CARE_BRANCH = POOR_EXP_CUST_CARE_BRANCH == null ? "" : POOR_EXP_CUST_CARE_BRANCH;
            if (!TextUtils.isEmpty(POOR_EXP_CUST_CARE_BRANCH)) {
                IMPROVE_FURTHER_CALL += POOR_EXP_CUST_CARE_BRANCH + ",";
            }

            POOR_EXP_SALES_PERSON = parseXML.parseXmlTag(Node, "POOR_EXP_SALES_PERSON");
            POOR_EXP_SALES_PERSON = POOR_EXP_SALES_PERSON == null ? "" : POOR_EXP_SALES_PERSON;
            if (!TextUtils.isEmpty(POOR_EXP_SALES_PERSON)) {
                IMPROVE_FURTHER_CALL += POOR_EXP_SALES_PERSON + ",";
            }

            DELAY_RECEIPT_COMM_PLCY_STMT = parseXML.parseXmlTag(Node, "DELAY_RECEIPT_COMM_PLCY_STMT");
            DELAY_RECEIPT_COMM_PLCY_STMT = DELAY_RECEIPT_COMM_PLCY_STMT == null ? "" : DELAY_RECEIPT_COMM_PLCY_STMT;
            if (!TextUtils.isEmpty(DELAY_RECEIPT_COMM_PLCY_STMT)) {
                IMPROVE_FURTHER_CALL += DELAY_RECEIPT_COMM_PLCY_STMT + ",";
            }

            HAPPY_RENEWAL_PAYMENT_EXP = parseXML.parseXmlTag(Node, "HAPPY_RENEWAL_PAYMENT_EXP");
            HAPPY_RENEWAL_PAYMENT_EXP = HAPPY_RENEWAL_PAYMENT_EXP == null ? "" : HAPPY_RENEWAL_PAYMENT_EXP;
            if (!TextUtils.isEmpty(HAPPY_RENEWAL_PAYMENT_EXP)) {
                IMPROVE_FURTHER_CALL += HAPPY_RENEWAL_PAYMENT_EXP + ",";
            }

            HAPPY_ONLINE_SERVICING_EXP = parseXML.parseXmlTag(Node, "HAPPY_ONLINE_SERVICING_EXP");
            HAPPY_ONLINE_SERVICING_EXP = HAPPY_ONLINE_SERVICING_EXP == null ? "" : HAPPY_ONLINE_SERVICING_EXP;
            if (!TextUtils.isEmpty(HAPPY_ONLINE_SERVICING_EXP)) {
                IMPROVE_FURTHER_CALL += HAPPY_ONLINE_SERVICING_EXP + ",";
            }

            HAPPY_EXP_CUST_CARE_BRANCH = parseXML.parseXmlTag(Node, "HAPPY_EXP_CUST_CARE_BRANCH");
            HAPPY_EXP_CUST_CARE_BRANCH = HAPPY_EXP_CUST_CARE_BRANCH == null ? "" : HAPPY_EXP_CUST_CARE_BRANCH;
            if (!TextUtils.isEmpty(HAPPY_EXP_CUST_CARE_BRANCH)) {
                IMPROVE_FURTHER_CALL += HAPPY_EXP_CUST_CARE_BRANCH + ",";
            }

            HAPPY_EXP_SALES_PERSON = parseXML.parseXmlTag(Node, "HAPPY_EXP_SALES_PERSON");
            HAPPY_EXP_SALES_PERSON = HAPPY_EXP_SALES_PERSON == null ? "" : HAPPY_EXP_SALES_PERSON;
            if (!TextUtils.isEmpty(HAPPY_EXP_SALES_PERSON)) {
                IMPROVE_FURTHER_CALL += HAPPY_EXP_SALES_PERSON + ",";
            }

            REGULAR_RECEIPT_COMM_PLCY_STMT = parseXML.parseXmlTag(Node, "REGULAR_RECEIPT_COMM_PLCY_STMT");
            REGULAR_RECEIPT_COMM_PLCY_STMT = REGULAR_RECEIPT_COMM_PLCY_STMT == null ? "" : REGULAR_RECEIPT_COMM_PLCY_STMT;
            if (!TextUtils.isEmpty(REGULAR_RECEIPT_COMM_PLCY_STMT)) {
                IMPROVE_FURTHER_CALL += REGULAR_RECEIPT_COMM_PLCY_STMT + ",";
            }

            EASY_TO_PAY_RENEWAL_PREM = parseXML.parseXmlTag(Node, "EASY_TO_PAY_RENEWAL_PREM");
            EASY_TO_PAY_RENEWAL_PREM = EASY_TO_PAY_RENEWAL_PREM == null ? "" : EASY_TO_PAY_RENEWAL_PREM;
            if (!TextUtils.isEmpty(EASY_TO_PAY_RENEWAL_PREM)) {
                IMPROVE_FURTHER_CALL += EASY_TO_PAY_RENEWAL_PREM + ",";
            }

            COMPLEX_REQ_SUBMISSION_PROCESS = parseXML.parseXmlTag(Node, "COMPLEX_REQ_SUBMISSION_PROCESS");
            COMPLEX_REQ_SUBMISSION_PROCESS = COMPLEX_REQ_SUBMISSION_PROCESS == null ? "" : COMPLEX_REQ_SUBMISSION_PROCESS;
            if (!TextUtils.isEmpty(COMPLEX_REQ_SUBMISSION_PROCESS)) {
                IMPROVE_FURTHER_CALL += COMPLEX_REQ_SUBMISSION_PROCESS + ",";
            }

            DELAYED_PROCESSING_PAYMENT = parseXML.parseXmlTag(Node, "DELAYED_PROCESSING_PAYMENT");
            DELAYED_PROCESSING_PAYMENT = DELAYED_PROCESSING_PAYMENT == null ? "" : DELAYED_PROCESSING_PAYMENT;
            if (!TextUtils.isEmpty(DELAYED_PROCESSING_PAYMENT)) {
                IMPROVE_FURTHER_CALL += DELAYED_PROCESSING_PAYMENT + ",";
            }

            KNOWLEDGE_BRANCH_OFFICIAL = parseXML.parseXmlTag(Node, "KNOWLEDGE_BRANCH_OFFICIAL");
            KNOWLEDGE_BRANCH_OFFICIAL = KNOWLEDGE_BRANCH_OFFICIAL == null ? "" : KNOWLEDGE_BRANCH_OFFICIAL;
            if (!TextUtils.isEmpty(KNOWLEDGE_BRANCH_OFFICIAL)) {
                IMPROVE_FURTHER_CALL += KNOWLEDGE_BRANCH_OFFICIAL + ",";
            }

            RETURN_INVEST_NOT_AS_EXPECTED = parseXML.parseXmlTag(Node, "RETURN_INVEST_NOT_AS_EXPECTED");
            RETURN_INVEST_NOT_AS_EXPECTED = RETURN_INVEST_NOT_AS_EXPECTED == null ? "" : RETURN_INVEST_NOT_AS_EXPECTED;
            if (!TextUtils.isEmpty(RETURN_INVEST_NOT_AS_EXPECTED)) {
                IMPROVE_FURTHER_CALL += RETURN_INVEST_NOT_AS_EXPECTED + ",";
            }

            SIMPLE_REQ_SUBMISSION_PROCESS = parseXML.parseXmlTag(Node, "SIMPLE_REQ_SUBMISSION_PROCESS");
            SIMPLE_REQ_SUBMISSION_PROCESS = SIMPLE_REQ_SUBMISSION_PROCESS == null ? "" : SIMPLE_REQ_SUBMISSION_PROCESS;
            if (!TextUtils.isEmpty(SIMPLE_REQ_SUBMISSION_PROCESS)) {
                IMPROVE_FURTHER_CALL += SIMPLE_REQ_SUBMISSION_PROCESS + ",";
            }

            PROMPT_PROCESSING_PAYMENT = parseXML.parseXmlTag(Node, "PROMPT_PROCESSING_PAYMENT");
            PROMPT_PROCESSING_PAYMENT = PROMPT_PROCESSING_PAYMENT == null ? "" : PROMPT_PROCESSING_PAYMENT;
            if (!TextUtils.isEmpty(PROMPT_PROCESSING_PAYMENT)) {
                IMPROVE_FURTHER_CALL += PROMPT_PROCESSING_PAYMENT + ",";
            }

            SERVICE_RENDERED_BRANCH_OFFICI = parseXML.parseXmlTag(Node, "SERVICE_RENDERED_BRANCH_OFFICI");
            SERVICE_RENDERED_BRANCH_OFFICI = SERVICE_RENDERED_BRANCH_OFFICI == null ? "" : SERVICE_RENDERED_BRANCH_OFFICI;
            if (!TextUtils.isEmpty(SERVICE_RENDERED_BRANCH_OFFICI)) {
                IMPROVE_FURTHER_CALL += SERVICE_RENDERED_BRANCH_OFFICI + ",";
            }

            GOOD_RETURN_INVEST = parseXML.parseXmlTag(Node, "GOOD_RETURN_INVEST");
            GOOD_RETURN_INVEST = GOOD_RETURN_INVEST == null ? "" : GOOD_RETURN_INVEST;
            if (!TextUtils.isEmpty(GOOD_RETURN_INVEST)) {
                IMPROVE_FURTHER_CALL += GOOD_RETURN_INVEST + ",";
            }

            NOT_HELPFUL_BRANCH_OFF = parseXML.parseXmlTag(Node, "NOT_HELPFUL_BRANCH_OFF");
            NOT_HELPFUL_BRANCH_OFF = NOT_HELPFUL_BRANCH_OFF == null ? "" : NOT_HELPFUL_BRANCH_OFF;
            if (!TextUtils.isEmpty(NOT_HELPFUL_BRANCH_OFF)) {
                IMPROVE_FURTHER_CALL += NOT_HELPFUL_BRANCH_OFF + ",";
            }

            //Added on 04 Nov 2020
            PRIMARY_QUESTION = parseXML.parseXmlTag(Node, "PRIMARY_QUESTION");
            PRIMARY_QUESTION = PRIMARY_QUESTION == null ? "" : PRIMARY_QUESTION;

            ONLN_PREM_PAY_EXP = parseXML.parseXmlTag(Node, "ONLN_PREM_PAY_EXP");
            ONLN_PREM_PAY_EXP = ONLN_PREM_PAY_EXP == null ? "" : ONLN_PREM_PAY_EXP;
            if (!TextUtils.isEmpty(ONLN_PREM_PAY_EXP)) {
                OTHER_REASON_0_6R += ONLN_PREM_PAY_EXP + ",";
            }


            PREM_RECEIPT = parseXML.parseXmlTag(Node, "PREM_RECEIPT");
            PREM_RECEIPT = PREM_RECEIPT == null ? "" : PREM_RECEIPT;

            SBI_EXP = parseXML.parseXmlTag(Node, "SBI_EXP");
            SBI_EXP = SBI_EXP == null ? "" : SBI_EXP;
            if (!TextUtils.isEmpty(SBI_EXP)) {
                OTHER_REASON_0_6R += SBI_EXP + ",";
            }

            CUST_CAREEXP = parseXML.parseXmlTag(Node, "CUST_CAREEXP");
            CUST_CAREEXP = CUST_CAREEXP == null ? "" : CUST_CAREEXP;
            if (!TextUtils.isEmpty(CUST_CAREEXP)) {
                OTHER_REASON_0_6R += CUST_CAREEXP + ",";
            }

            SALES_PERSONNEL = parseXML.parseXmlTag(Node, "SALES_PERSONNEL");
            SALES_PERSONNEL = SALES_PERSONNEL == null ? "" : SALES_PERSONNEL;

            PRODUCT_EXP_EXP = parseXML.parseXmlTag(Node, "PRODUCT_EXP_EXP");
            PRODUCT_EXP_EXP = PRODUCT_EXP_EXP == null ? "" : PRODUCT_EXP_EXP;
            if (!TextUtils.isEmpty(PRODUCT_EXP_EXP)) {
                OTHER_REASON_0_6R += PRODUCT_EXP_EXP + ",";
            }

            PREM_PAY_TRANS_FAILED = parseXML.parseXmlTag(Node, "PREM_PAY_TRANS_FAILED");
            PREM_PAY_TRANS_FAILED = PREM_PAY_TRANS_FAILED == null ? "" : PREM_PAY_TRANS_FAILED;

            PREM_PAY_APP_NOTUSERFRNDLY = parseXML.parseXmlTag(Node, "PREM_PAY_APP_NOTUSERFRNDLY");
            PREM_PAY_APP_NOTUSERFRNDLY = PREM_PAY_APP_NOTUSERFRNDLY == null ? "" : PREM_PAY_APP_NOTUSERFRNDLY;
            if (!TextUtils.isEmpty(PREM_PAY_APP_NOTUSERFRNDLY)) {
                IMPROVE_FURTHER_CALL += PREM_PAY_APP_NOTUSERFRNDLY + ",";
            }

            PREM_PAY_LENGTHY_PAYPROCESS = parseXML.parseXmlTag(Node, "PREM_PAY_LENGTHY_PAYPROCESS");
            PREM_PAY_LENGTHY_PAYPROCESS = PREM_PAY_LENGTHY_PAYPROCESS == null ? "" : PREM_PAY_LENGTHY_PAYPROCESS;
            if (!TextUtils.isEmpty(PREM_PAY_LENGTHY_PAYPROCESS)) {
                IMPROVE_FURTHER_CALL += PREM_PAY_LENGTHY_PAYPROCESS + ",";
            }

            PREM_PAY_NO_ACKNOWLEDGEMENT = parseXML.parseXmlTag(Node, "PREM_PAY_NO_ACKNOWLEDGEMENT");
            PREM_PAY_NO_ACKNOWLEDGEMENT = PREM_PAY_NO_ACKNOWLEDGEMENT == null ? "" : PREM_PAY_NO_ACKNOWLEDGEMENT;
            if (!TextUtils.isEmpty(PREM_PAY_NO_ACKNOWLEDGEMENT)) {
                OTHER_REASON_0_6R += PREM_PAY_NO_ACKNOWLEDGEMENT + ",";
            }

            PREM_PAY_REM_NOTRECEIVED = parseXML.parseXmlTag(Node, "PREM_PAY_REM_NOTRECEIVED");
            PREM_PAY_REM_NOTRECEIVED = PREM_PAY_REM_NOTRECEIVED == null ? "" : PREM_PAY_REM_NOTRECEIVED;

            PREM_RECEIPT_NOTRECEIVED_TRANS = parseXML.parseXmlTag(Node, "PREM_RECEIPT_NOTRECEIVED_TRANS");
            PREM_RECEIPT_NOTRECEIVED_TRANS = PREM_RECEIPT_NOTRECEIVED_TRANS == null ? "" : PREM_RECEIPT_NOTRECEIVED_TRANS;

            PREM_RECEIPT_NOTREC_REGULARLY = parseXML.parseXmlTag(Node, "PREM_RECEIPT_NOTREC_REGULARLY");
            PREM_RECEIPT_NOTREC_REGULARLY = PREM_RECEIPT_NOTREC_REGULARLY == null ? "" : PREM_RECEIPT_NOTREC_REGULARLY;

            if (!TextUtils.isEmpty(PREM_RECEIPT_NOTREC_REGULARLY)) {
                OTHER_REASON_0_6R += PREM_RECEIPT_NOTREC_REGULARLY + ",";
            }
            PREM_RECEIPT_INFO_INCORRECT = parseXML.parseXmlTag(Node, "PREM_RECEIPT_INFO_INCORRECT");
            PREM_RECEIPT_INFO_INCORRECT = PREM_RECEIPT_INFO_INCORRECT == null ? "" : PREM_RECEIPT_INFO_INCORRECT;

            PREM_RECEIPT_NONOFABOVE = parseXML.parseXmlTag(Node, "PREM_RECEIPT_NONOFABOVE");
            PREM_RECEIPT_NONOFABOVE = PREM_RECEIPT_NONOFABOVE == null ? "" : PREM_RECEIPT_NONOFABOVE;

            SBI_EXP_KNOWLEDGE = parseXML.parseXmlTag(Node, "SBI_EXP_KNOWLEDGE");
            SBI_EXP_KNOWLEDGE = SBI_EXP_KNOWLEDGE == null ? "" : SBI_EXP_KNOWLEDGE;
            if (!TextUtils.isEmpty(SBI_EXP_KNOWLEDGE)) {
                OTHER_REASON_0_6R += SBI_EXP_KNOWLEDGE + ",";
            }

            SBI_EXP_TIME_TAKEN = parseXML.parseXmlTag(Node, "SBI_EXP_TIME_TAKEN");
            SBI_EXP_TIME_TAKEN = SBI_EXP_TIME_TAKEN == null ? "" : SBI_EXP_TIME_TAKEN;
            if (!TextUtils.isEmpty(SBI_EXP_TIME_TAKEN)) {
                OTHER_REASON_0_6R += SBI_EXP_TIME_TAKEN + ",";
            }

            SBI_EXP_BEHAVIOUR_COURTESY = parseXML.parseXmlTag(Node, "SBI_EXP_BEHAVIOUR_COURTESY");
            SBI_EXP_BEHAVIOUR_COURTESY = SBI_EXP_BEHAVIOUR_COURTESY == null ? "" : SBI_EXP_BEHAVIOUR_COURTESY;
            if (!TextUtils.isEmpty(SBI_EXP_BEHAVIOUR_COURTESY)) {
                OTHER_REASON_0_6R += SBI_EXP_BEHAVIOUR_COURTESY + ",";
            }

            SBI_EXP_NONOFABOVE = parseXML.parseXmlTag(Node, "SBI_EXP_NONOFABOVE");
            SBI_EXP_NONOFABOVE = SBI_EXP_NONOFABOVE == null ? "" : SBI_EXP_NONOFABOVE;

            CUST_CARE_AUDIOCLARITY = parseXML.parseXmlTag(Node, "CUST_CARE_AUDIOCLARITY");
            CUST_CARE_AUDIOCLARITY = CUST_CARE_AUDIOCLARITY == null ? "" : CUST_CARE_AUDIOCLARITY;
            if (!TextUtils.isEmpty(CUST_CARE_AUDIOCLARITY)) {
                OTHER_REASON_0_6R += CUST_CARE_AUDIOCLARITY + ",";
            }

            CUST_CARE_KNOWLEDGE_PERSONNEL = parseXML.parseXmlTag(Node, "CUST_CARE_KNOWLEDGE_PERSONNEL");
            CUST_CARE_KNOWLEDGE_PERSONNEL = CUST_CARE_KNOWLEDGE_PERSONNEL == null ? "" : CUST_CARE_KNOWLEDGE_PERSONNEL;
            if (!TextUtils.isEmpty(CUST_CARE_KNOWLEDGE_PERSONNEL)) {
                IMPROVE_FURTHER_CALL += CUST_CARE_KNOWLEDGE_PERSONNEL + ",";
            }

            CUST_CARE_RESOLUTION = parseXML.parseXmlTag(Node, "CUST_CARE_RESOLUTION");
            CUST_CARE_RESOLUTION = CUST_CARE_RESOLUTION == null ? "" : CUST_CARE_RESOLUTION;
            if (!TextUtils.isEmpty(CUST_CARE_RESOLUTION)) {
                IMPROVE_FURTHER_CALL += CUST_CARE_RESOLUTION + ",";
            }

            CUST_CARE_BEHAVIOUR = parseXML.parseXmlTag(Node, "CUST_CARE_BEHAVIOUR");
            CUST_CARE_BEHAVIOUR = CUST_CARE_BEHAVIOUR == null ? "" : CUST_CARE_BEHAVIOUR;
            if (!TextUtils.isEmpty(CUST_CARE_BEHAVIOUR)) {
                IMPROVE_FURTHER_CALL += CUST_CARE_BEHAVIOUR + ",";
            }

            CUST_CARE_NONOFABOVE = parseXML.parseXmlTag(Node, "CUST_CARE_NONOFABOVE");
            CUST_CARE_NONOFABOVE = CUST_CARE_NONOFABOVE == null ? "" : CUST_CARE_NONOFABOVE;

            SALE_UNAVAILABLE_SERVICE = parseXML.parseXmlTag(Node, "SALE_UNAVAILABLE_SERVICE");
            SALE_UNAVAILABLE_SERVICE = SALE_UNAVAILABLE_SERVICE == null ? "" : SALE_UNAVAILABLE_SERVICE;

            SALES_KNOWLEDGE_PERSONNEL = parseXML.parseXmlTag(Node, "SALES_KNOWLEDGE_PERSONNEL");
            SALES_KNOWLEDGE_PERSONNEL = SALES_KNOWLEDGE_PERSONNEL == null ? "" : SALES_KNOWLEDGE_PERSONNEL;
            if (!TextUtils.isEmpty(SALES_KNOWLEDGE_PERSONNEL)) {
                IMPROVE_FURTHER_CALL += SALES_KNOWLEDGE_PERSONNEL + ",";
            }

            SALES_RESOLUTION_TIME = parseXML.parseXmlTag(Node, "SALES_RESOLUTION_TIME");
            SALES_RESOLUTION_TIME = SALES_RESOLUTION_TIME == null ? "" : SALES_RESOLUTION_TIME;
            if (!TextUtils.isEmpty(SALES_RESOLUTION_TIME)) {
                IMPROVE_FURTHER_CALL += SALES_RESOLUTION_TIME + ",";
            }

            SALES_BEHAVIOUR = parseXML.parseXmlTag(Node, "SALES_BEHAVIOUR");
            SALES_BEHAVIOUR = SALES_BEHAVIOUR == null ? "" : SALES_BEHAVIOUR;
            if (!TextUtils.isEmpty(SALES_BEHAVIOUR)) {
                IMPROVE_FURTHER_CALL += SALES_BEHAVIOUR + ",";
            }

            SALES_NONOFABOVE = parseXML.parseXmlTag(Node, "SALES_NONOFABOVE");
            SALES_NONOFABOVE = SALES_NONOFABOVE == null ? "" : SALES_NONOFABOVE;

            PROD_EXP_POLICY = parseXML.parseXmlTag(Node, "PROD_EXP_POLICY");
            PROD_EXP_POLICY = PROD_EXP_POLICY == null ? "" : PROD_EXP_POLICY;
            if (!TextUtils.isEmpty(PROD_EXP_POLICY)) {
                OTHER_REASON_0_6R += PROD_EXP_POLICY + ",";
            }

            PROD_EXP_INVESTMENT_RETURN = parseXML.parseXmlTag(Node, "PROD_EXP_INVESTMENT_RETURN");
            PROD_EXP_INVESTMENT_RETURN = PROD_EXP_INVESTMENT_RETURN == null ? "" : PROD_EXP_INVESTMENT_RETURN;

            PROD_EXP_RETURN_DIFFER = parseXML.parseXmlTag(Node, "PROD_EXP_RETURN_DIFFER");
            PROD_EXP_RETURN_DIFFER = PROD_EXP_RETURN_DIFFER == null ? "" : PROD_EXP_RETURN_DIFFER;

            PROD_EXP_NONOFABOVE = parseXML.parseXmlTag(Node, "PROD_EXP_NONOFABOVE");
            PROD_EXP_NONOFABOVE = PROD_EXP_NONOFABOVE == null ? "" : PROD_EXP_NONOFABOVE;

            NOTA_ONLN_PREM_PAY_EXP = parseXML.parseXmlTag(Node, "NOTA_ONLN_PREM_PAY_EXP");
            NOTA_ONLN_PREM_PAY_EXP = NOTA_ONLN_PREM_PAY_EXP == null ? "" : NOTA_ONLN_PREM_PAY_EXP;

            NOTA_PREMIUM_RECEIPT = parseXML.parseXmlTag(Node, "NOTA_PREMIUM_RECEIPT");
            NOT_HELPFUL_BRANCH_OFF = NOTA_PREMIUM_RECEIPT == null ? "" : NOTA_PREMIUM_RECEIPT;

            NOTA_SBI_EXP = parseXML.parseXmlTag(Node, "NOTA_SBI_EXP");
            NOTA_SBI_EXP = NOTA_SBI_EXP == null ? "" : NOTA_SBI_EXP;
            if (!TextUtils.isEmpty(NOTA_SBI_EXP)) {
                OTHER_REASON_0_6R += NOTA_SBI_EXP + ",";
            }

            NOTA_CUST_CARE = parseXML.parseXmlTag(Node, "NOTA_CUST_CARE");
            NOTA_CUST_CARE = NOTA_CUST_CARE == null ? "" : NOTA_CUST_CARE;

            NOTA_SALES_PERSONNEL = parseXML.parseXmlTag(Node, "NOTA_SALES_PERSONNEL");
            NOTA_SALES_PERSONNEL = NOTA_SALES_PERSONNEL == null ? "" : NOTA_SALES_PERSONNEL;

            NOTA_PROD_EXP = parseXML.parseXmlTag(Node, "NOTA_PROD_EXP");
            NOTA_PROD_EXP = NOTA_PROD_EXP == null ? "" : NOTA_PROD_EXP;
            if (!TextUtils.isEmpty(NOTA_PROD_EXP)) {
                OTHER_REASON_0_6R += NOTA_PROD_EXP + ",";
            }

            ONLN_PREM_PAY_EXP_LIMITED = parseXML.parseXmlTag(Node, "ONLN_PREM_PAY_EXP_LIMITED");
            ONLN_PREM_PAY_EXP_LIMITED = ONLN_PREM_PAY_EXP_LIMITED == null ? "" : ONLN_PREM_PAY_EXP_LIMITED;

            ONLN_PREM_PAY_EXP_NONEOFABOVE = parseXML.parseXmlTag(Node, "ONLN_PREM_PAY_EXP_NONEOFABOVE");
            ONLN_PREM_PAY_EXP_NONEOFABOVE = ONLN_PREM_PAY_EXP_NONEOFABOVE == null ? "" : ONLN_PREM_PAY_EXP_NONEOFABOVE;

            REGION = parseXML.parseXmlTag(Node, "REGION");
            REGION = REGION == null ? "" : REGION;
            NPSScoreValuesModel nodeVal = new NPSScoreValuesModel(NPS_MASTER_KEY, POLICY_NO, REQUEST_DATE,
                    RESPONDED_ON, RATING, NOT_GO_WELL_OTHERS,
                    OTHER_REASON_0_6R, CHANNEL, CREATED_BY, CREATED_DATE, 0,
                    0, 0, PROJECT_TYPE, 0, 0, PRIMARY_COMMENT,
                    IMPROVE_FURTHER_CALL,
                    LIKELY_RECOMMEND_SBI_LIFE);

            lsData.add(nodeVal);
        }

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.FLOOR);

        //ONboarding result
        if (onBoardingTotal != 0) {
            NPSScore = ((promotersOnboarding * 100) / onBoardingTotal) - ((detractorsOnboarding * 100) / onBoardingTotal);
            NPSScore = new Double(df.format(NPSScore));
        } else {
            NPSScore = 0;
        }

        NPSScoreValuesModel nodeVal = new NPSScoreValuesModel("Onboarding", "", "",
                "", 0, "",
                "", "", "", "",
                promotersOnboarding, detractorsOnboarding, NPSScore,
                "Onboarding_NPS", onBoardingTotal, passivesOnboarding, "",
                "", "");
        lsData.add(nodeVal);
        //End ONboarding result

        //Renewal result
        if (renewalTotal != 0) {
            NPSScore = ((promotersRenewal * 100) / renewalTotal) - ((detractorsRenewal * 100) / renewalTotal);
            NPSScore = new Double(df.format(NPSScore));
        } else {
            NPSScore = 0;
        }

        nodeVal = new NPSScoreValuesModel("Renewal", "", "",
                "", 0, "",
                "", "", "", "",
                promotersRenewal, detractorsRenewal, NPSScore,
                "Renewal_NPS", renewalTotal, passivesRenewal, ""
                , "", "");
        lsData.add(nodeVal);
        //End Renewal result
       /* Service Exp Online Payment
        Service Experience Registered
        Service Experience SBI Bank
        Service Experience SBI Life*/
        /*//Service Exp Online Payment
        if (serviceExpOnlinePaymentTotal != 0) {
            NPSScore = ((promotersServiceExpOnlinePayment * 100) / serviceExpOnlinePaymentTotal)
                    - ((detractorsServiceExpOnlinePayment * 100) / serviceExpOnlinePaymentTotal);
            NPSScore = new Double(df.format(NPSScore));
        } else {
            NPSScore = 0;
        }

        nodeVal = new NPSScoreValuesModel("Service Exp Online Payment", "", "",
                "", 0, "",
                "", "", "", "",
                promotersServiceExpOnlinePayment, detractorsServiceExpOnlinePayment, NPSScore,
                "ServiceExpOnlinePayment_NPS", serviceExpOnlinePaymentTotal, passivesServiceExpOnlinePayment, ""
                , "", "");
        lsData.add(nodeVal);
        //End Service Exp Online Payment

        // Service Experience Registered
        if (serviceExpeRegdTotal != 0) {
            NPSScore = ((promotersServiceExpeRegd * 100) / serviceExpeRegdTotal)
                    - ((detractorsServiceExpeRegd * 100) / serviceExpeRegdTotal);
            NPSScore = new Double(df.format(NPSScore));
        } else {
            NPSScore = 0;
        }

        nodeVal = new NPSScoreValuesModel("Service Experience Registered", "", "",
                "", 0, "",
                "", "", "", "",
                promotersServiceExpeRegd, detractorsServiceExpeRegd, NPSScore,
                "ServiceExperienceRegistered_NPS", serviceExpeRegdTotal, passivesServiceExpeRegd, ""
                , "", "");
        lsData.add(nodeVal);
        //End  Service Experience Registered

        //Service Experience SBI Bank
        if (serviceExpeSBIBankTotal != 0) {
            NPSScore = ((promotersServiceExpeSBIBank * 100) / serviceExpeSBIBankTotal)
                    - ((detractorsServiceExpeSBIBank * 100) / serviceExpeSBIBankTotal);
            NPSScore = new Double(df.format(NPSScore));
        } else {
            NPSScore = 0;
        }

        nodeVal = new NPSScoreValuesModel("Service Experience SBI Bank", "", "",
                "", 0, "",
                "", "", "", "",
                promotersServiceExpeSBIBank, detractorsServiceExpeSBIBank, NPSScore,
                "ServiceExperienceSBIBank_NPS", serviceExpeSBIBankTotal, passivesServiceExpeSBIBank, ""
                , "", "");
        lsData.add(nodeVal);
        //End Service Experience SBI Bank

        //Service Experience SBI Life
        if (serviceExpeSBILifeTotal != 0) {
            NPSScore = ((promotersServiceExpeSBILife * 100) / serviceExpeSBILifeTotal)
                    - ((detractorsServiceExpeSBILife * 100) / serviceExpeSBILifeTotal);
            NPSScore = new Double(df.format(NPSScore));
        } else {
            NPSScore = 0;
        }

        nodeVal = new NPSScoreValuesModel("Service Experience SBI Life", "", "",
                "", 0, "",
                "", "", "", "",
                promotersServiceExpeSBILife, detractorsServiceExpeSBILife, NPSScore,
                "ServiceExperienceSBILife_NPS", serviceExpeSBILifeTotal, passivesServiceExpeSBILife, ""
                , "", "");
        lsData.add(nodeVal);
        //End Service Experience SBI Life*/


        //Service Experience

        double allServiceExperience = serviceExpOnlinePaymentTotal +
                serviceExpeRegdTotal + serviceExpeSBIBankTotal + serviceExpeSBILifeTotal;
        //Service Exp Online Payment
        double allServiceExperiencePromoters = promotersServiceExpOnlinePayment + promotersServiceExpeRegd +
                promotersServiceExpeSBIBank + promotersServiceExpeSBILife;

        double allServiceExperienceDetractors = detractorsServiceExpOnlinePayment + detractorsServiceExpeRegd +
                detractorsServiceExpeSBIBank + detractorsServiceExpeSBILife;


        double allServiceExperiencePassives = passivesServiceExpOnlinePayment + passivesServiceExpeRegd +
                passivesServiceExpeSBIBank + passivesServiceExpeSBILife;
        if (allServiceExperience != 0) {
            NPSScore = ((allServiceExperiencePromoters * 100) / allServiceExperience)
                    - ((allServiceExperienceDetractors * 100) / allServiceExperience);
            NPSScore = new Double(df.format(NPSScore));
        } else {
            NPSScore = 0;
        }

        nodeVal = new NPSScoreValuesModel("Service Experience", "", "",
                "", 0, "",
                "", "", "", "",
                allServiceExperiencePromoters, allServiceExperienceDetractors, NPSScore,
                "ServiceExperience_NPS", serviceExpeSBILifeTotal, allServiceExperiencePassives, ""
                , "", "");
        lsData.add(nodeVal);
        //End Service Experience

        //Living Benefit result
        if (livingBenefitTotal != 0) {
            NPSScore = ((promotersLivingBenefit * 100) / livingBenefitTotal) - ((detractorsLivingBenefit * 100) / livingBenefitTotal);
            NPSScore = new Double(df.format(NPSScore));
        } else {
            NPSScore = 0;
        }

        nodeVal = new NPSScoreValuesModel("Living Benefit", "", "",
                "", 0, "",
                "", "", "", "",
                promotersLivingBenefit, detractorsLivingBenefit, NPSScore,
                "Living Benefit_NPS", livingBenefitTotal, passivesLivingBenefit, "",
                "", "");
        lsData.add(nodeVal);
        //End Living Benefit result

        //All Total
        /*double allProjectPromotersTotal = promotersOnboarding + promotersRenewal + promotersLivingBenefit
                + promotersServiceExpOnlinePayment + promotersServiceExpeRegd + promotersServiceExpeSBIBank
                + promotersServiceExpeSBILife;*/
        double allProjectPromotersTotal = promotersOnboarding + promotersRenewal + promotersLivingBenefit
                + allServiceExperiencePromoters;
        double allProjectDetractorTotal = detractorsOnboarding + detractorsRenewal + detractorsLivingBenefit
                + allServiceExperienceDetractors;
        double allPassivesTotal = passivesOnboarding + passivesRenewal + passivesLivingBenefit +
                allServiceExperiencePassives;

        if (lsNode.size() != 0) {
            NPSScore = ((allProjectPromotersTotal * 100) / lsNode.size()) -
                    ((allProjectDetractorTotal * 100) / lsNode.size());
            NPSScore = new Double(df.format(NPSScore));
        } else {
            NPSScore = 0;
        }


        nodeVal = new NPSScoreValuesModel("Total", "", "",
                "", 0, "",
                "", "", "", "",
                allProjectPromotersTotal, allProjectDetractorTotal, NPSScore,
                "Total_NPS", 0, allPassivesTotal, "", "", "");
        lsData.add(nodeVal);

        Collections.reverse(lsData);
        return lsData;
    }
}
