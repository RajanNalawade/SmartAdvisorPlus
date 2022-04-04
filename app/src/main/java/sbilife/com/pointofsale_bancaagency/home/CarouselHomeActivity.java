package sbilife.com.pointofsale_bancaagency.home;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import sbilife.com.pointofsale_bancaagency.ActivityProductExplainer;
import sbilife.com.pointofsale_bancaagency.BancaProductActivity;
import sbilife.com.pointofsale_bancaagency.CarouselProductActivity;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.agent_on_boarding.ActivityAOBUMListUnderBSM;
import sbilife.com.pointofsale_bancaagency.agent_on_boarding.Activity_AOB_Authentication;
import sbilife.com.pointofsale_bancaagency.agent_on_boarding.dashboard.ActivityAOBAgentDetails;
import sbilife.com.pointofsale_bancaagency.branchlocator.BranchLocatorMainActivity;
import sbilife.com.pointofsale_bancaagency.common.AppSharedPreferences;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.PersistencyBrodcastReceiver;
import sbilife.com.pointofsale_bancaagency.eftnotification.EFTPendingFormListActivity;
import sbilife.com.pointofsale_bancaagency.futureplanner.FuturePlannerActivity;
import sbilife.com.pointofsale_bancaagency.home.covid19.Covid19QuestionnaireActivity;
import sbilife.com.pointofsale_bancaagency.home.dashboard.NewDashboardAgent;
import sbilife.com.pointofsale_bancaagency.home.dashboard.NewDashboardCIF;
import sbilife.com.pointofsale_bancaagency.home.dgh.DGHActivity;
import sbilife.com.pointofsale_bancaagency.home.e_mhr_format.ActivityE_MHR;
import sbilife.com.pointofsale_bancaagency.home.lmcorner.ContestsLMActivity;
import sbilife.com.pointofsale_bancaagency.home.lmcorner.CovidSelfDeclarationActivity;
import sbilife.com.pointofsale_bancaagency.home.lmcorner.LMCorner;
import sbilife.com.pointofsale_bancaagency.home.mhr.DigitalMHRActivity;
import sbilife.com.pointofsale_bancaagency.home.mhr.MHRActivity;
import sbilife.com.pointofsale_bancaagency.home.mhr.MHRProposalListActivity;
import sbilife.com.pointofsale_bancaagency.home.npsscore.NPSScoreActivity;
import sbilife.com.pointofsale_bancaagency.home.rinnrakshareports.RinnRakshaReportsActivity;
import sbilife.com.pointofsale_bancaagency.new_bussiness.NewBusinessHomeGroupingActivity;
import sbilife.com.pointofsale_bancaagency.posp_ra.Activity_POSP_RA_Authentication;
import sbilife.com.pointofsale_bancaagency.posp_ra.Activity_POSP_RA_UMListUnderBSM;
import sbilife.com.pointofsale_bancaagency.posp_ra.dashboard.Activity_POSP_RA_AgentDetails;
import sbilife.com.pointofsale_bancaagency.reports.BancaReportsCIFListActivity;
import sbilife.com.pointofsale_bancaagency.reports.BancaReportsMaturityActivity;
import sbilife.com.pointofsale_bancaagency.reports.BancaReportsRenewalActivity;
import sbilife.com.pointofsale_bancaagency.reports.BancaReportsSBDueListActivity;
import sbilife.com.pointofsale_bancaagency.reports.ServicingHomeGroupingActivity;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsMaturityActivity;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsSBDueListActivity;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgentServicingHomeGroupingActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.CommonReportsProposalListActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.MedicalPendingRequirementActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.RevivalCampaignReportList;
import sbilife.com.pointofsale_bancaagency.utility.DocUploadNonMedicalPendingActivity;
import sbilife.com.pointofsale_bancaagency.utility.DocumentsUploadActivity;

public class CarouselHomeActivity extends AppCompatActivity {

    private final Integer[] bannerImages = {
            R.drawable.cruise_glory_ebandhan_banner,
            R.drawable.banner_spread_ur_wings,
            R.drawable.banner_pos,
            R.drawable.incentive_scheme_banner,
            R.drawable.lm_club_banner,
            R.drawable.banner_jotc,
            R.drawable.banner_smart_future,
            R.drawable.banner_lm_survey,
            R.drawable.offline_ekyc,
            R.drawable.banner_thinks_dot
    };

    private final String NAMESPACE = "http://tempuri.org/";

    private final ArrayList<HomeMenu> listMenuItem = new ArrayList<>();
    private Context mContext;
    private CommonMethods mCommonMethods;
    private ViewPager slider_pager;
    private LinearLayout layoutDots;
    private Timer mTimer;

    private String strCIFBDMUserId = "", strCIFBDMEmailId = "", strCIFBDMPassword = "", strCIFBDMMObileNo = "", strUserType = "";
    private SharedPreferences mPreferences;
    private ProgressDialog mProgressDialog;

    private String strRevivalListErrorCOde1 = "";
    private long lstRevivalListCount1 = 0;

    private DownloadFileAsyncTaskEFTPendingForm taskEFTPendingForm;
    private DownloadFileAsyncRenewal_update taskRenewal_update;
    private DownloadFileAsyncNonMedicalRequirement downloadFileAsyncNonMedicalRequirement;
    private DownloadFileAsyncTaskLoggedProposalCount downloadFileAsyncTaskLoggedProposalCount;
    private DownloadFileAsyncTaskMedicalRequirement downloadFileAsyncTaskMedicalRequirement;
    private DownloadFileAsyncTaskMaturityListCount downloadFileAsyncTaskMaturityListCount;
    private DownloadFileAsyncTaskSBDueListCount downloadFileAsyncTaskSBDueListCount;
    private DownloadFileAsyncTaskClaimRequirementInfo downloadFileAsyncTaskClaimRequirementInfo;
    private RevivalNotifiationAsyncTask revivalNotifiationAsyncTask;
    private KYCMissingNotifiationAsyncTask kycMissingNotifiationAsyncTask;
    private PromotionalAppMessageAsyncTask promotionalAppMessageAsyncTask;
    private LastLoginTimeAsyncTask lastLoginTimeAsyncTask;
    //private JOTCDhamakaAsyncTask jotcDhamakaAsyncTask;
    private MonthlyGradAllowAsyncTask monthlyGradAllowAsyncTask;
    private TextView tvLastLoginTime;
    private UMIncentiveNotificationAsyncTask umIncentiveNotificationAsyncTask;
    private NBIncentiveUMNotificationAsyncTask nbIncentiveUMNotificationAsyncTask;

    private JOTCMessageAsyncTask jotcMessageAsyncTask;
    private CruiseMessageAsyncTask cruiseMessageAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_carousel_home);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        initialisation();
        try {
            Intent intent = new Intent(getApplicationContext(), PersistencyBrodcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            if (alarmManager != null) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            }

        } catch (Exception e) {

        }
    }

    private void initialisation() {

        mContext = this;
        mCommonMethods = new CommonMethods();

        mCommonMethods.setApplicationToolbarMenu(this, "Home");

        mPreferences = getPreferences(MODE_PRIVATE);
        mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);


        mProgressDialog.setButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (taskRenewal_update != null) {
                    taskRenewal_update.cancel(true);
                }

                if (taskEFTPendingForm != null) {
                    taskEFTPendingForm.cancel(true);
                }

                if (downloadFileAsyncTaskLoggedProposalCount != null) {
                    downloadFileAsyncTaskLoggedProposalCount.cancel(true);
                }

                if (downloadFileAsyncNonMedicalRequirement != null) {
                    downloadFileAsyncNonMedicalRequirement.cancel(true);
                }

                if (downloadFileAsyncTaskMedicalRequirement != null) {
                    downloadFileAsyncTaskMedicalRequirement.cancel(true);
                }

                if (downloadFileAsyncTaskMaturityListCount != null) {
                    downloadFileAsyncTaskMaturityListCount.cancel(true);
                }
                if (downloadFileAsyncTaskSBDueListCount != null) {
                    downloadFileAsyncTaskSBDueListCount.cancel(true);
                }

                if (downloadFileAsyncTaskClaimRequirementInfo != null) {
                    downloadFileAsyncTaskClaimRequirementInfo.cancel(true);
                }
                if (revivalNotifiationAsyncTask != null) {
                    revivalNotifiationAsyncTask.cancel(true);
                }
                if (kycMissingNotifiationAsyncTask != null) {
                    kycMissingNotifiationAsyncTask.cancel(true);
                }
                if (promotionalAppMessageAsyncTask != null) {
                    promotionalAppMessageAsyncTask.cancel(true);
                }
                if (lastLoginTimeAsyncTask != null) {
                    lastLoginTimeAsyncTask.cancel(true);
                }


                /*if (jotcDhamakaAsyncTask != null) {
                    jotcDhamakaAsyncTask.cancel(true);
                }*/

                if (monthlyGradAllowAsyncTask != null) {
                    monthlyGradAllowAsyncTask.cancel(true);
                }

                if (umIncentiveNotificationAsyncTask != null) {
                    umIncentiveNotificationAsyncTask.cancel(true);
                }
                if (nbIncentiveUMNotificationAsyncTask != null) {
                    nbIncentiveUMNotificationAsyncTask.cancel(true);
                }
                if (jotcMessageAsyncTask != null) {
                    jotcMessageAsyncTask.cancel(true);
                }

                if (cruiseMessageAsyncTask != null) {
                    cruiseMessageAsyncTask.cancel(true);
                }
                mProgressDialog.dismiss();
            }
        });


        slider_pager = findViewById(R.id.slider_pager);
        layoutDots = findViewById(R.id.layoutDots);
        RecyclerView rbCarouselHome = findViewById(R.id.rbCarouselHome);
        tvLastLoginTime = findViewById(R.id.tvLastLoginTime);
        tvLastLoginTime.setVisibility(View.GONE);
        try {
            strCIFBDMUserId = mCommonMethods.GetUserID(mContext);
            strCIFBDMEmailId = mCommonMethods.GetUserEmail(mContext);
            strCIFBDMPassword = mCommonMethods.GetUserPassword(mContext);
            strCIFBDMMObileNo = mCommonMethods.GetUserMobile(mContext);
            strUserType = mCommonMethods.GetUserType(mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        lastLoginTimeAsyncTask = new LastLoginTimeAsyncTask();
        lastLoginTimeAsyncTask.execute();


        // adding bottom dots
        addBottomDots(0);

        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(bannerImages);
        slider_pager.setAdapter(myViewPagerAdapter);
        slider_pager.addOnPageChangeListener(viewPagerPageChangeListener);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                slider_pager.post(new Runnable() {

                    @Override
                    public void run() {
                        slider_pager.setCurrentItem((slider_pager.getCurrentItem() + 1) % bannerImages.length);
                    }
                });
            }
        };
        mTimer = new Timer();
        mTimer.schedule(timerTask, 4000, 4000);

        getUserTypeMenuList();

        // set LayoutManager to RecyclerView
        rbCarouselHome.setLayoutManager(new GridLayoutManager(CarouselHomeActivity.this, 2));

        // call the constructor of CustomAdapter to send the reference and data to Adapter
        HomeMenuAdapter mHomeMenuAdapter = new HomeMenuAdapter(listMenuItem);
        rbCarouselHome.setAdapter(mHomeMenuAdapter);
        rbCarouselHome.setItemAnimator(new DefaultItemAnimator());

        taskRenewal_update = new DownloadFileAsyncRenewal_update();
        taskEFTPendingForm = new DownloadFileAsyncTaskEFTPendingForm();

        if (strUserType.equalsIgnoreCase("AGENT")) {
            cruiseMessageAsyncTask = new CruiseMessageAsyncTask();
            cruiseMessageAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            String notificationFlag = mPreferences.getString(mCommonMethods.getDASHBOARD_RENEWAL_UPDATE_PREFERENCE(), "false");
            String revivalNotificationInfo = mPreferences.getString(mCommonMethods.getRevivalNotificationInfo(), "false");

            if (!notificationFlag.equalsIgnoreCase("true")) {
                startDownloadRenewal_update();
            } else if (!revivalNotificationInfo.equalsIgnoreCase("true")) {
                startDownloadRevivalNotification();
            } else {
                startDownloadLoggedProposalCount();
            }

        } else if (strUserType.equalsIgnoreCase("BDM")) {
            whatsAppDialog();
            String notificationFlag = mPreferences.getString(mCommonMethods.getNOTIFICATION_PREFERENCE(), "false");
            if (!notificationFlag.equalsIgnoreCase("true")) {
                startDownloadEFTPendingForms();
            } else {
                startDownloadLoggedProposalCount();
            }

        } else if (strUserType.equalsIgnoreCase("CIF")) {
            whatsAppDialog();
            String revivalNotificationInfo = mPreferences.getString(mCommonMethods.getRevivalNotificationInfo(), "false");
            if (!revivalNotificationInfo.equalsIgnoreCase("true")) {
                startDownloadRevivalNotification();
            } else {
                startDownloadLoggedProposalCount();
            }
        } else if (strUserType.equalsIgnoreCase("UM")) {

            String appMsgStatus = "Dear Colleague,\n" +
                    "\n" +"Are you ready for CRUISE TO GLORY JAFFNA – SRI LANKA CAMPAIGN??" +
                    " Leverage this opportunity to qualify for a cruise to Jaffna, Sri Lanka " +
                    "along with your life Mitra’s. T&C applicable.\n" +
                    "\n" +"Looking forward to overwhelming participation from you and your " +
                    "Life Mitras. Wishing you and your team the very best. – Retail Agency";

            final AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
            LayoutInflater inflater = ((AppCompatActivity) mContext).getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_with_ok_button, null);
            builder1.setView(dialogView);
            TextView text = dialogView.findViewById(R.id.tv_title);
            text.setText(appMsgStatus);
            //text.setMovementMethod(LinkMovementMethod.getInstance());
            Button dialogButton = dialogView.findViewById(R.id.bt_ok);
            dialogButton.setText("Ok");
            final AlertDialog dialog = builder1.create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //dialog.setContentView(R.layout.dialog_with_ok_button);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                    nbIncentiveUMNotificationAsyncTask = new NBIncentiveUMNotificationAsyncTask();
                    nbIncentiveUMNotificationAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            });
            dialog.show();
            startDownloadLoggedProposalCount();
        } else if (strUserType.equalsIgnoreCase("AM") || strUserType.equalsIgnoreCase("SAM") ||
                strUserType.equalsIgnoreCase("ZAM") || strUserType.equalsIgnoreCase("BSM") ||
                strUserType.equalsIgnoreCase("DSM")) {
            whatsAppDialog();
            startDownloadKYCMissingNotification();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_yes_no);
            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
            Button bt_yes = dialog.findViewById(R.id.bt_yes);
            Button bt_no = dialog.findViewById(R.id.bt_no);
            ((TextView) dialog.findViewById(R.id.tv_title))
                    .setText("Are you sure you want to Logout?");
            bt_yes.setText("Yes");
            bt_no.setText("No");
            bt_yes.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                    NotificationManager notificationManager = (NotificationManager)
                            mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancelAll();

                    //SharedPreferences preferences = getPreferences(MODE_PRIVATE);

                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putString(mCommonMethods.getNOTIFICATION_PREFERENCE(), "False");
                    editor.putString(mCommonMethods.getClaimRequirementInfo(), "False");
                    editor.putString(mCommonMethods.getDASHBOARD_RENEWAL_UPDATE_PREFERENCE(), "False");
                    editor.putString(mCommonMethods.getLOGGED_PROPOSAL_COUNT_NOTIFICATION_PREFERENCE(), "False");
                    editor.putString(mCommonMethods.getNON_MEDICAL_PENDING_REQ_COUNT_NOTIFICATION_PREFERENCE(), "False");
                    editor.putString(mCommonMethods.getMEDICAL_PENDING_REQ_COUNT_NOTIFICATION_PREFERENCE(), "False");
                    editor.putString(mCommonMethods.MATURITY_LIST_NOTIFICATION_PREFERENCE, "false");
                    editor.putString(mCommonMethods.SB_DUE_LIST_NOTIFICATION_PREFERENCE, "false");
                    editor.putString(mCommonMethods.getRevivalNotificationInfo(), "False");
                    editor.putString(mCommonMethods.getKYCMissingNotification(), "False");
                    editor.apply();

                    AppSharedPreferences.setData(mContext, new AppSharedPreferences().PERSISTENCY_KEY, "0");

                    mCommonMethods.logoutToLoginActivity(mContext);
                }
            });
            bt_no.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });
            dialog.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        final Configuration oldConfig = getResources().getConfiguration();
        final int diff = oldConfig.diff(newConfig);
        final Configuration target = diff == 0 ? oldConfig : newConfig;

        super.onConfigurationChanged(target);
    }

    @Override
    protected void onDestroy() {

        mTimer.cancel();

        killAsyncTasks();

        super.onDestroy();
    }

    @Override
    protected void onStop() {

        mTimer.cancel();

        killAsyncTasks();

        super.onStop();
    }

    private void killAsyncTasks() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (taskRenewal_update != null) {
            taskRenewal_update.cancel(true);
        }

        if (taskEFTPendingForm != null) {
            taskEFTPendingForm.cancel(true);
        }

        if (downloadFileAsyncTaskLoggedProposalCount != null) {
            downloadFileAsyncTaskLoggedProposalCount.cancel(true);
        }

        if (downloadFileAsyncNonMedicalRequirement != null) {
            downloadFileAsyncNonMedicalRequirement.cancel(true);
        }

        if (downloadFileAsyncTaskMedicalRequirement != null) {
            downloadFileAsyncTaskMedicalRequirement.cancel(true);
        }

        if (downloadFileAsyncTaskMaturityListCount != null) {
            downloadFileAsyncTaskMaturityListCount.cancel(true);
        }
        if (downloadFileAsyncTaskSBDueListCount != null) {
            downloadFileAsyncTaskSBDueListCount.cancel(true);
        }

        if (downloadFileAsyncTaskClaimRequirementInfo != null) {
            downloadFileAsyncTaskClaimRequirementInfo.cancel(true);
        }
        if (revivalNotifiationAsyncTask != null) {
            revivalNotifiationAsyncTask.cancel(true);
        }
        if (kycMissingNotifiationAsyncTask != null) {
            kycMissingNotifiationAsyncTask.cancel(true);
        }
        if (promotionalAppMessageAsyncTask != null) {
            promotionalAppMessageAsyncTask.cancel(true);
        }
        if (lastLoginTimeAsyncTask != null) {
            lastLoginTimeAsyncTask.cancel(true);
        }

       /* if (jotcDhamakaAsyncTask != null) {
            jotcDhamakaAsyncTask.cancel(true);
        }*/

        if (monthlyGradAllowAsyncTask != null) {
            monthlyGradAllowAsyncTask.cancel(true);
        }

        if (umIncentiveNotificationAsyncTask != null) {
            umIncentiveNotificationAsyncTask.cancel(true);
        }

        if (nbIncentiveUMNotificationAsyncTask != null) {
            nbIncentiveUMNotificationAsyncTask.cancel(true);
        }

        if (jotcMessageAsyncTask != null) {
            jotcMessageAsyncTask.cancel(true);
        }
        if (cruiseMessageAsyncTask != null) {
            cruiseMessageAsyncTask.cancel(true);
        }

    }

    private void startDownloadEFTPendingForms() {
        taskEFTPendingForm = new DownloadFileAsyncTaskEFTPendingForm();
        taskEFTPendingForm.execute("demo");
    }

    private void startDownloadLoggedProposalCount() {

        String loggedProposalCount = mPreferences.getString(mCommonMethods.getLOGGED_PROPOSAL_COUNT_NOTIFICATION_PREFERENCE(), "false");
        String nonMedicalPendingReq = mPreferences.getString(mCommonMethods.getNON_MEDICAL_PENDING_REQ_COUNT_NOTIFICATION_PREFERENCE(), "false");
        String medicalPendingReq = mPreferences.getString(mCommonMethods.getMEDICAL_PENDING_REQ_COUNT_NOTIFICATION_PREFERENCE(), "false");
        String maturityListCount = mPreferences.getString(mCommonMethods.MATURITY_LIST_NOTIFICATION_PREFERENCE, "false");
        String sbDueListCount = mPreferences.getString(mCommonMethods.SB_DUE_LIST_NOTIFICATION_PREFERENCE, "false");
        String cliaimRequirementInfo = mPreferences.getString(mCommonMethods.getClaimRequirementInfo(), "false");
        String KYCMissingNotification = mPreferences.getString(mCommonMethods.getKYCMissingNotification(), "false");

        if (!loggedProposalCount.equalsIgnoreCase("true")) {
            downloadFileAsyncTaskLoggedProposalCount = new DownloadFileAsyncTaskLoggedProposalCount();
            downloadFileAsyncTaskLoggedProposalCount.execute("demo");
        } else if (!nonMedicalPendingReq.equalsIgnoreCase("true")) {
            startDownloadNonMedicalPendingRequirement();
        } else if (!medicalPendingReq.equalsIgnoreCase("true")) {
            startDownloadMedicalPendingRequirement();
        } else if (!maturityListCount.equalsIgnoreCase("true")) {
            startDownloadMaturutyListCount();
        } else if (!sbDueListCount.equalsIgnoreCase("true")) {
            startDownloadSBDueListListCount();
        } else if (!cliaimRequirementInfo.equalsIgnoreCase("true")) {
            startDownloadClaimRequirementInfo();
        } else if (!KYCMissingNotification.equalsIgnoreCase("true")) {
            startDownloadKYCMissingNotification();
        }
    }

    private void startDownloadRenewal_update() {
        taskRenewal_update = new DownloadFileAsyncRenewal_update();
        taskRenewal_update.execute("demo");
    }

    private void startDownloadNonMedicalPendingRequirement() {
        String nonMedicalPendingReq = mPreferences.getString(mCommonMethods.getNON_MEDICAL_PENDING_REQ_COUNT_NOTIFICATION_PREFERENCE(), "false");
        String medicalPendingReq = mPreferences.getString(mCommonMethods.getMEDICAL_PENDING_REQ_COUNT_NOTIFICATION_PREFERENCE(), "false");
        String maturityListCount = mPreferences.getString(mCommonMethods.MATURITY_LIST_NOTIFICATION_PREFERENCE, "false");
        String sbDueListCount = mPreferences.getString(mCommonMethods.SB_DUE_LIST_NOTIFICATION_PREFERENCE, "false");
        String cliaimRequirementInfo = mPreferences.getString(mCommonMethods.getClaimRequirementInfo(), "false");
        String KYCMissingNotification = mPreferences.getString(mCommonMethods.getKYCMissingNotification(), "false");

        if (!nonMedicalPendingReq.equalsIgnoreCase("true")) {
            downloadFileAsyncNonMedicalRequirement = new DownloadFileAsyncNonMedicalRequirement();
            downloadFileAsyncNonMedicalRequirement.execute("demo");
        } else if (!medicalPendingReq.equalsIgnoreCase("true")) {
            startDownloadMedicalPendingRequirement();
        } else if (!maturityListCount.equalsIgnoreCase("true")) {
            startDownloadMaturutyListCount();
        } else if (!sbDueListCount.equalsIgnoreCase("true")) {
            startDownloadSBDueListListCount();
        } else if (!cliaimRequirementInfo.equalsIgnoreCase("true")) {
            startDownloadClaimRequirementInfo();
        } else if (!KYCMissingNotification.equalsIgnoreCase("true")) {
            startDownloadKYCMissingNotification();
        }
    }

    private void startDownloadMedicalPendingRequirement() {
        String medicalPendingReq = mPreferences.getString(mCommonMethods.getMEDICAL_PENDING_REQ_COUNT_NOTIFICATION_PREFERENCE(), "false");
        String maturityListCount = mPreferences.getString(mCommonMethods.MATURITY_LIST_NOTIFICATION_PREFERENCE, "false");
        String sbDueListCount = mPreferences.getString(mCommonMethods.SB_DUE_LIST_NOTIFICATION_PREFERENCE, "false");
        String cliaimRequirementInfo = mPreferences.getString(mCommonMethods.getClaimRequirementInfo(), "false");
        String KYCMissingNotification = mPreferences.getString(mCommonMethods.getKYCMissingNotification(), "false");

        if (!medicalPendingReq.equalsIgnoreCase("true")) {
            downloadFileAsyncTaskMedicalRequirement = new DownloadFileAsyncTaskMedicalRequirement();
            downloadFileAsyncTaskMedicalRequirement.execute();
        } else if (!maturityListCount.equalsIgnoreCase("true")) {
            startDownloadMaturutyListCount();
        } else if (!sbDueListCount.equalsIgnoreCase("true")) {
            startDownloadSBDueListListCount();
        } else if (!cliaimRequirementInfo.equalsIgnoreCase("true")) {
            startDownloadClaimRequirementInfo();
        } else if (!KYCMissingNotification.equalsIgnoreCase("true")) {
            startDownloadKYCMissingNotification();
        }
    }

    private void startDownloadMaturutyListCount() {
        String maturityListCount = mPreferences.getString(mCommonMethods.MATURITY_LIST_NOTIFICATION_PREFERENCE, "false");
        String sbDueListCount = mPreferences.getString(mCommonMethods.SB_DUE_LIST_NOTIFICATION_PREFERENCE, "false");
        String cliaimRequirementInfo = mPreferences.getString(mCommonMethods.getClaimRequirementInfo(), "false");
        String KYCMissingNotification = mPreferences.getString(mCommonMethods.getKYCMissingNotification(), "false");

        if (!maturityListCount.equalsIgnoreCase("true")) {
            downloadFileAsyncTaskMaturityListCount = new DownloadFileAsyncTaskMaturityListCount();
            downloadFileAsyncTaskMaturityListCount.execute();
        } else if (!sbDueListCount.equalsIgnoreCase("true")) {
            startDownloadSBDueListListCount();
        } else if (!cliaimRequirementInfo.equalsIgnoreCase("true")) {
            startDownloadClaimRequirementInfo();
        } else if (!KYCMissingNotification.equalsIgnoreCase("true")) {
            startDownloadKYCMissingNotification();
        }
    }

    private void startDownloadSBDueListListCount() {
        String sbDueListCount = mPreferences.getString(mCommonMethods.SB_DUE_LIST_NOTIFICATION_PREFERENCE, "false");
        String cliaimRequirementInfo = mPreferences.getString(mCommonMethods.getClaimRequirementInfo(), "false");
        String KYCMissingNotification = mPreferences.getString(mCommonMethods.getKYCMissingNotification(), "false");

        if (!sbDueListCount.equalsIgnoreCase("true")) {
            downloadFileAsyncTaskSBDueListCount = new DownloadFileAsyncTaskSBDueListCount();
            downloadFileAsyncTaskSBDueListCount.execute();
        } else if (!cliaimRequirementInfo.equalsIgnoreCase("true")) {
            startDownloadClaimRequirementInfo();
        } else if (!KYCMissingNotification.equalsIgnoreCase("true")) {
            startDownloadKYCMissingNotification();
        }
    }

    private void startDownloadClaimRequirementInfo() {
        String cliaimRequirementInfo = mPreferences.getString(mCommonMethods.getClaimRequirementInfo(), "false");
        String KYCMissingNotification = mPreferences.getString(mCommonMethods.getKYCMissingNotification(), "false");

        if (!cliaimRequirementInfo.equalsIgnoreCase("true")) {
            downloadFileAsyncTaskClaimRequirementInfo = new DownloadFileAsyncTaskClaimRequirementInfo();
            downloadFileAsyncTaskClaimRequirementInfo.execute();
        } else if (!KYCMissingNotification.equalsIgnoreCase("true")) {
            kycMissingNotifiationAsyncTask = new KYCMissingNotifiationAsyncTask();
            kycMissingNotifiationAsyncTask.execute();
        }
    }

    private void startDownloadRevivalNotification() {
        String revivalNotificationInfo = mPreferences.getString(mCommonMethods.getRevivalNotificationInfo(), "false");
        if (!revivalNotificationInfo.equalsIgnoreCase("true")) {
            revivalNotifiationAsyncTask = new RevivalNotifiationAsyncTask();
            revivalNotifiationAsyncTask.execute();
        } else {
            startDownloadLoggedProposalCount();
        }
    }

    private void startDownloadKYCMissingNotification() {
        String KYCMissingNotification = mPreferences.getString(mCommonMethods.getKYCMissingNotification(), "false");
        if (!KYCMissingNotification.equalsIgnoreCase("true")) {
            kycMissingNotifiationAsyncTask = new KYCMissingNotifiationAsyncTask();
            kycMissingNotifiationAsyncTask.execute();
        } else {
            startDownloadLoggedProposalCount();
        }
    }

    //get User Type Menus
    private void getUserTypeMenuList() {

        if (strUserType.equalsIgnoreCase("Agent") || strUserType.contentEquals("UM")
                || strUserType.contentEquals("BSM") || strUserType.contentEquals("DSM")
                || strUserType.equalsIgnoreCase("ASM") || strUserType.equalsIgnoreCase("RSM")) {
            listMenuItem.add(new HomeMenu(R.drawable.dashboard1, "Dashboard",
                    NewDashboardAgent.class, false, false, ""));
            listMenuItem.add(new HomeMenu(R.drawable.products1, "Products",
                    CarouselProductActivity.class, false, false, ""));

            //Common
            listMenuItem.add(new HomeMenu(R.drawable.newbusinesses1, "New Business",
                    NewBusinessHomeGroupingActivity.class, false, false, ""));
            listMenuItem.add(new HomeMenu(R.drawable.uploaddocument1, "Requirement Upload",
                    DocumentsUploadActivity.class, false, false, ""));
            listMenuItem.add(new HomeMenu(R.drawable.childeducationplanner1, "Future Planner",
                    FuturePlannerActivity.class, false, false, ""));
            //Common end

            listMenuItem.add(new HomeMenu(R.drawable.servicingnew, "Servicing", AgentServicingHomeGroupingActivity.class
                    , false, false, ""));

            //Common
            listMenuItem.add(new HomeMenu(R.drawable.branch_locator, "Branch Locator",
                    BranchLocatorMainActivity.class, false, false, ""));
            listMenuItem.add(new HomeMenu(R.drawable.sales_kit, "Sales kit", null, false,
                    true, "https://drive.google.com/open?id=1xc9KJJIAmee6FXIladPhVsFJ1aDzTFaX"));

            listMenuItem.add(new HomeMenu(R.drawable.ic_nps, "NPS", NPSScoreActivity.class,
                    true, false, ""));

            listMenuItem.add(new HomeMenu(R.drawable.ic_mhr, "Digital MHR", DigitalMHRActivity.class,
                    true, false, ""));
            listMenuItem.add(new HomeMenu(R.drawable.ic_dgh, "DGH", DGHActivity.class,
                    true, false, ""));

            listMenuItem.add(new HomeMenu(R.drawable.ic_covid_icon, "Covid-19 Questionnaire", Covid19QuestionnaireActivity.class,
                    true, false, ""));
            //Common End

        } else {
            listMenuItem.add(new HomeMenu(R.drawable.dashboard1, "Dashboard",
                    NewDashboardCIF.class, false, false, ""));
            listMenuItem.add(new HomeMenu(R.drawable.products1, "Products",
                    BancaProductActivity.class, false, false, ""));

            //Common
            listMenuItem.add(new HomeMenu(R.drawable.newbusinesses1, "New Business",
                    NewBusinessHomeGroupingActivity.class, false, false, ""));
            listMenuItem.add(new HomeMenu(R.drawable.uploaddocument1, "Requirement Upload",
                    DocumentsUploadActivity.class, false, false, ""));
            listMenuItem.add(new HomeMenu(R.drawable.childeducationplanner1, "Future Planner",
                    FuturePlannerActivity.class, false, false, ""));
            //Common end

            listMenuItem.add(new HomeMenu(R.drawable.servicingnew, "Servicing", ServicingHomeGroupingActivity.class
                    , false, false, ""));

            //Common
            listMenuItem.add(new HomeMenu(R.drawable.branch_locator, "Branch Locator",
                    BranchLocatorMainActivity.class, false, false, ""));
            listMenuItem.add(new HomeMenu(R.drawable.sales_kit, "Sales kit", null, false,
                    true, "https://drive.google.com/open?id=1xc9KJJIAmee6FXIladPhVsFJ1aDzTFaX"));

            listMenuItem.add(new HomeMenu(R.drawable.ic_nps, "NPS", NPSScoreActivity.class,
                    true, false, ""));

            listMenuItem.add(new HomeMenu(R.drawable.ic_mhr, "Digital MHR", DigitalMHRActivity.class,
                    true, false, ""));
            listMenuItem.add(new HomeMenu(R.drawable.ic_dgh, "DGH", DGHActivity.class,
                    true, false, ""));

            listMenuItem.add(new HomeMenu(R.drawable.ic_covid_icon, "Covid-19 Questionnaire", Covid19QuestionnaireActivity.class,
                    true, false, ""));
            //Common End
        }


        switch (strUserType) {
            case "BDM":
                listMenuItem.add(new HomeMenu(R.drawable.icon_e_mhr, "e mhr format", ActivityE_MHR.class,
                        true, false, ""));
                listMenuItem.add(new HomeMenu(R.drawable.icon_operations_quality_score_card,
                        "Operation quality score card", ScoreCardActivity.class,
                        false, false, ""));
                listMenuItem.add(new HomeMenu(R.drawable.ic_icon_club_memebership_report, "Rin raksha Dashboard",
                        RinnRakshaDashboardActivity.class, true, false, ""));
                listMenuItem.add(new HomeMenu(R.drawable.bdmtrackernew, "BDM Tracker",
                        BancaBDMTracker.class, false, false, ""));
                listMenuItem.add(new HomeMenu(R.drawable.ic_shortmhr, "Short MHR", MHRActivity.class,
                        true, false, ""));

                listMenuItem.add(new HomeMenu(R.drawable.rinn_raksha_reports_not, "Rinn Raksha - Reports & Notification",
                        RinnRakshaReportsActivity.class,
                        true, false, ""));
                break;

            case "AM":
            case "SAM":
            case "ZAM":
                listMenuItem.add(new HomeMenu(R.drawable.icon_e_mhr, "e mhr format", ActivityE_MHR.class,
                        true, false, ""));
                listMenuItem.add(new HomeMenu(R.drawable.icon_operations_quality_score_card,
                        "Operation quality score card", ScoreCardActivity.class,
                        false, false, ""));
                listMenuItem.add(new HomeMenu(R.drawable.ic_icon_club_memebership_report,
                        "Rin raksha Dashboard", RinnRakshaDashboardActivity.class, true, false, ""));
                listMenuItem.add(new HomeMenu(R.drawable.ic_shortmhr, "Short MHR", MHRActivity.class,
                        true, false, ""));
                break;

            case "AGENT":
                listMenuItem.add(new HomeMenu(R.drawable.icon_product_explainer, "Product Explainer",
                        ActivityProductExplainer.class, false, false, ""));
                listMenuItem.add(new HomeMenu(R.drawable.ic_icon_lm_corner, "LM Corner",
                        LMCorner.class, true, false, ""));
                break;

            case "UM":
                listMenuItem.add(new HomeMenu(R.drawable.icon_e_mhr, "e mhr format", ActivityE_MHR.class,
                        true, false, ""));
                listMenuItem.add(new HomeMenu(R.drawable.icon_operations_quality_score_card, "Operation quality score card",
                        ScoreCardActivity.class, false, false, ""));

                /*String URLPresentationUM = "https://drive.google.com/open?id=0B5LinkXikg9Qd1plRFRfb2Y4dVlWVzZyanV5NmNsZHZfVC1J";
                listMenuItem.add(new HomeMenu(R.drawable.icon_protection_presentation_for_distributors, "Protection Presentation For Distributors",
                        null, false, true, URLPresentationUM));
                String URLUM = "https://drive.google.com/open?id=0B5LinkXikg9QcldKZ1N0WlFxd3RhcFM5SGtoU0xhNnZaYWNz";
                listMenuItem.add(new HomeMenu(R.drawable.icon_reference_slides, "Reference Slides",
                        null, false, true, URLUM));*/

                listMenuItem.add(new HomeMenu(R.drawable.icon_aob_agent_details, "Agent Dashboard",
                        ActivityAOBAgentDetails.class, false, false, ""));

                listMenuItem.add(new HomeMenu(R.drawable.icon_aob, "Agent On Boarding",
                        Activity_AOB_Authentication.class, false, false, ""));

                listMenuItem.add(new HomeMenu(R.drawable.icon_posp_dashboard, "POSP-RA Dashboard",
                        Activity_POSP_RA_AgentDetails.class, false, false, ""));

                listMenuItem.add(new HomeMenu(R.drawable.icon_posp_ra, "POSP-RA",
                        Activity_POSP_RA_Authentication.class, false, false, ""));

                /*listMenuItem.add(new HomeMenu(R.drawable.ic_mhr, "MHR",
                        MHRProposalListActivity.class,false,false,""));*/
                listMenuItem.add(new HomeMenu(R.drawable.ic_icon_lm_corner, "LM Corner",
                        BancaReportsCIFListActivity.class, true, false, ""));
                listMenuItem.add(new HomeMenu(R.drawable.ic_shortmhr, "Short MHR", MHRActivity.class,
                        true, false, ""));
                listMenuItem.add(new HomeMenu(R.drawable.ic_covid_self_declaration, "Covid Self Declaration", CovidSelfDeclarationActivity.class,
                        true, false, ""));
                listMenuItem.add(new HomeMenu(R.drawable.ic_icon_lm_corner, "Contest",
                        ContestsLMActivity.class, true, false, ""));
                break;


            case "BSM":
            case "DSM":
                listMenuItem.add(new HomeMenu(R.drawable.icon_e_mhr, "e mhr format", ActivityE_MHR.class,
                        true, false, ""));
                listMenuItem.add(new HomeMenu(R.drawable.icon_operations_quality_score_card, "Operation quality score card",
                        ScoreCardActivity.class, false, false, ""));
                listMenuItem.add(new HomeMenu(R.drawable.icon_aob_agent_details, "AOB Dashboard",
                        ActivityAOBUMListUnderBSM.class, true, false, ""));
                listMenuItem.add(new HomeMenu(R.drawable.icon_aob_intrview_que, "AOB Interview Questions",
                        ActivityAOBUMListUnderBSM.class, false, false, ""));
                listMenuItem.add(new HomeMenu(R.drawable.icon_posp_dashboard, "POSP-RA Dashboard",
                        Activity_POSP_RA_UMListUnderBSM.class, true, false, ""));
                listMenuItem.add(new HomeMenu(R.drawable.icon_posp_interview_questions, "POSP-RA Interview Questions",
                        Activity_POSP_RA_UMListUnderBSM.class, false, false, ""));
                listMenuItem.add(new HomeMenu(R.drawable.ic_shortmhr, "Short MHR", MHRActivity.class,
                        true, false, ""));
                listMenuItem.add(new HomeMenu(R.drawable.ic_covid_self_declaration, "Covid Self Declaration", CovidSelfDeclarationActivity.class,
                        true, false, ""));
                listMenuItem.add(new HomeMenu(R.drawable.ic_sales_comments, "Contest",
                        ContestsLMActivity.class, true, false, ""));
                break;

            case "CAG":
                listMenuItem.add(new HomeMenu(R.drawable.icon_operations_quality_score_card, "Operation quality score card",
                        ScoreCardActivity.class, false, false, ""));
                break;

            default:
                break;
        }
    }

    class MyViewPagerAdapter extends PagerAdapter {
        private final Integer[] bannerImages;

        MyViewPagerAdapter(Integer[] arrIcon) {
            this.bannerImages = arrIcon;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.slidingimages_layout, container, false);
            try {
                assert view != null;
                final ImageView imageView = view.findViewById(R.id.image);

                Bitmap d = BitmapFactory.decodeResource(mContext.getResources(), bannerImages[position]);
                int nh = (int) (d.getHeight() * (512.0 / d.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(d, 512, nh, true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                scaled.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                imageView.setImageBitmap(scaled);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (position == 5) {
                            //banner Jotc
                            mCommonMethods.loadDriveURL("https://drive.google.com/file/d/1gDtgw_ED6Pjr9edP_OuR9Jfp326gQ4eW/view?usp=sharing", mContext);
                        }

                        if (position == 6) {
                            //Smart Future
                            mCommonMethods.openWebLink(mContext, "https://bit.ly/3onO8Y6");
                        }

                        if (position == 8) {
                            //Offline Kyc
                            mCommonMethods.loadDriveURL("https://drive.google.com/open?id=1ElFCRhDqOUbvfZ3aVtfmyFCVG0g4szMr", mContext);
                        }
                        if (position == 9) {
                            //Thank Dot
                            mCommonMethods.openWebLink(mContext, "https://www.sbilife.co.in/ThanksADot");
                        }

                    }
                });
                container.addView(view);

                return view;
            } catch (Exception e) {
                //Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG);
            }
            return view;
        }

        @Override
        public int getCount() {
            return bannerImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    private final ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[bannerImages.length];

        layoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(mContext);

            if (dots[i] != null) {
                dots[i].setText(Html.fromHtml("&#8226;"));
                dots[i].setTextSize(35);
                dots[i].setTextColor(getResources().getColor(R.color.grey05));
                layoutDots.addView(dots[i]);
            }
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.white));
    }

    public class HomeMenuAdapter extends RecyclerView.Adapter<HomeMenuAdapter.ViewHolderAdapter> {

        private final ArrayList<HomeMenu> lstAdapterList;

        HomeMenuAdapter(ArrayList<HomeMenu> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public int getItemCount() {
            return lstAdapterList.size();
        }

        @Override
        public ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_aob_req_dtls_dashboard, parent, false);

            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final ViewHolderAdapter holder, final int position) {
            holder.imageButtonAgentIcon.setImageDrawable(mContext.getResources().getDrawable(lstAdapterList.get(position).getIconDrawable()));
            holder.textviewAgentTitle.setText(lstAdapterList.get(position).getStrMenuTitle());

            holder.constraintAOBReqDtls.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Class aClass = lstAdapterList.get(position).getaClass();
                    if (aClass != null) {
                        boolean isHomeTag = lstAdapterList.get(position).isHomeTag();
                        if (isHomeTag) {
                            mCommonMethods.callActivityWithHomeTagYes(mContext, aClass);
                        } else {
                            if (lstAdapterList.get(position).getStrMenuTitle().equalsIgnoreCase("Short MHR")) {
                                Intent i = new Intent(mContext, MHRProposalListActivity.class);
                                i.putExtra("fromHome", "MHR");
                                mContext.startActivity(i);
                            } else {
                                mCommonMethods.callActivity(mContext, aClass);
                            }
                            //mCommonMethods.callActivity(mContext, aClass);
                        }
                    } else {
                        boolean isDriveLink = lstAdapterList.get(position).isDriveLink();
                        if (isDriveLink) {
                            mCommonMethods.loadDriveURL(lstAdapterList.get(position).getLink(), mContext);
                        }
                    }


                }
            });
        }

        @Override
        public void onViewRecycled(ViewHolderAdapter holder) {
            holder.itemView.setOnLongClickListener(null);
            super.onViewRecycled(holder);
        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder {

            private final ImageButton imageButtonAgentIcon;
            private final TextView textviewAgentTitle;
            private final LinearLayout constraintAOBReqDtls;

            ViewHolderAdapter(View v) {
                super(v);
                constraintAOBReqDtls = v.findViewById(R.id.constraintAOBReqDtls);
                imageButtonAgentIcon = v.findViewById(R.id.imageButtonAgentIcon);
                textviewAgentTitle = v.findViewById(R.id.textviewAgentTitle);
            }
        }
    }

    class HomeMenu {
        private final int iconDrawable;
        private final String strMenuTitle;
        private final Class aClass;
        private final boolean homeTag;
        private final boolean driveLink;
        private final String link;

        HomeMenu(int iconDrawable, String strMenuTitle, Class aClass, boolean homeTag,
                 boolean driveLink, String link) {
            this.strMenuTitle = strMenuTitle;
            this.iconDrawable = iconDrawable;
            this.aClass = aClass;
            this.homeTag = homeTag;
            this.driveLink = driveLink;
            this.link = link;
        }

        Class getaClass() {
            return aClass;
        }

        boolean isHomeTag() {
            return homeTag;
        }

        boolean isDriveLink() {
            return driveLink;
        }

        String getLink() {
            return link;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HomeMenu homeMenu = (HomeMenu) o;
            return iconDrawable == homeMenu.iconDrawable &&
                    strMenuTitle.equals(homeMenu.strMenuTitle);
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public int hashCode() {
            return Objects.hash(iconDrawable, strMenuTitle);
        }

        int getIconDrawable() {
            return iconDrawable;
        }


        String getStrMenuTitle() {
            return strMenuTitle;
        }

    }

    class DownloadFileAsyncTaskEFTPendingForm extends AsyncTask<String, String, String> {
        private volatile boolean running = true;
        /*private  final String SOAP_ACTION_EFT_NOT_RECEIVED_FORMS = "http://tempuri.org/getBAOnlineURN_SMRT";
        private  final String METHOD_NAME_EFT_NOT_RECEIVED_FORMS = "getBAOnlineURN_SMRT";*/

        int flag = 0;

        List<ParseXML.EFTPendingForm> nodeData = new ArrayList<>();


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

                String METHOD_NAME_EFT_NOT_RECEIVED_FORMS = "getnonsubmissionform_smrt";
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_EFT_NOT_RECEIVED_FORMS);
                request.addProperty("strCode", mCommonMethods.setUserDetails(mContext).getStrCIFBDMUserId());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                String SOAP_ACTION_EFT_NOT_RECEIVED_FORMS = "http://tempuri.org/getnonsubmissionform_smrt";
                androidHttpTranport.call(SOAP_ACTION_EFT_NOT_RECEIVED_FORMS,
                        envelope);

                SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();
                String inputpolicylist = sa.toString();

                if (inputpolicylist.equalsIgnoreCase("0")) {
                    flag = 0;
                } else {
                    flag = 1;
                    ParseXML prsObj = new ParseXML();
                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "Data");

                    if (inputpolicylist != null) {
                        List<String> Node = prsObj.parseParentNode(
                                inputpolicylist, "Table");
                        nodeData = prsObj.parseNodeEFTNode(Node);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                running = false;
            }
            return null;

        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (running) {
                if (flag == 1) {
                    for (int i = 0; i < nodeData.size(); i++) {
                        Intent intent = new Intent(mContext, EFTPendingFormListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        String title = "PPL no. " + nodeData.get(i).getPOLICY_NO() + " Dt(" + nodeData.get(i).getCASHIERINGDATE() + ")";
                        String message = "Submit within 3 days to qualify for FTR";

                        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
                                intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        mCommonMethods.commonNotification(mContext, pendingIntent, title, message, i);
                    }
                }
            }
            //SharedPreferences preferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString(mCommonMethods.getNOTIFICATION_PREFERENCE(), "true");
            editor.apply();
            startDownloadLoggedProposalCount();
        }
    }

    class DownloadFileAsyncTaskLoggedProposalCount extends AsyncTask<String, String, String> {
        private volatile boolean running = true;

        int totalCount = 0;


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

                String METHOD_NAME_LOGGED_PROPOSAL_COUNT = "getLoggedPropCout_notify_smrt";
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_LOGGED_PROPOSAL_COUNT);
                request.addProperty("strCode", mCommonMethods.setUserDetails(mContext).getStrCIFBDMUserId());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                String SOAP_ACTION_LOGGED_PROPOSAL_COUNT = "http://tempuri.org/getLoggedPropCout_notify_smrt";
                androidHttpTranport.call(SOAP_ACTION_LOGGED_PROPOSAL_COUNT,
                        envelope);

                Object sa = envelope.getResponse();
                String inputpolicylist = sa.toString();

                if (!inputpolicylist.contentEquals("<Data />")) {

                    try {

                        if (!sa.toString().equalsIgnoreCase("")) {
                            ParseXML prsObj = new ParseXML();

                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "Data");
                            if (inputpolicylist != null) {

                                /*For success : -
                                   <PolicyDetails> <Table> <TOTAL>1</TOTAL> </Table> </PolicyDetails>*/
                                List<String> Node = prsObj.parseParentNode(
                                        inputpolicylist, "Table");
                                for (String nodeItem : Node) {
                                    totalCount = Integer.parseInt(prsObj.parseXmlTag(nodeItem, "TOTAL"));
                                }

                            } else {
                                totalCount = 0;
                            }
                        } else {
                            totalCount = 0;
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

            } catch (Exception e) {
                e.printStackTrace();
                running = false;
            }
            return null;

        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (running) {
                if (totalCount > 0) {

                    Intent intent = new Intent(mContext, CommonReportsProposalListActivity.class);
                    intent.putExtra("fromHome", "Y");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
                            intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    String title = "Logged Proposal";
                    String message = "Logged Proposal Count is " + totalCount;
                    int notificationId = -1;
                    mCommonMethods.commonNotification(mContext, pendingIntent, title, message, notificationId);


                }
            }
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString(mCommonMethods.getLOGGED_PROPOSAL_COUNT_NOTIFICATION_PREFERENCE(), "true");
            editor.apply();
            startDownloadNonMedicalPendingRequirement();
        }
    }

    class DownloadFileAsyncTaskMedicalRequirement extends AsyncTask<String, String, String> {
        private volatile boolean running = true;

        int totalCount = 0;

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

                String METHOD_NAME_MEDICAL_REQUIREMENT = "getMedReq_noti_smrt";
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_MEDICAL_REQUIREMENT);
                request.addProperty("strCode", mCommonMethods.setUserDetails(mContext).getStrCIFBDMUserId());
                request.addProperty("strType", "Pending for Requirement - Medical");//new DatabaseHelper(context).GetUserType());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                String SOAP_ACTION_MEDICAL_REQUIREMENT = "http://tempuri.org/getMedReq_noti_smrt";
                androidHttpTranport.call(SOAP_ACTION_MEDICAL_REQUIREMENT,
                        envelope);

                Object sa = envelope.getResponse();
                String inputpolicylist = sa.toString();

                System.out.println("response:" + inputpolicylist);
                if (!inputpolicylist.contentEquals("<PolicyDetails />")) {

                    try {

                        if (!sa.toString().equalsIgnoreCase("")) {
                            ParseXML prsObj = new ParseXML();

                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "PolicyDetails");
                            if (inputpolicylist != null) {

                                /*For success : -
                                   <PolicyDetails> <Table> <TOTAL>1</TOTAL> </Table> </PolicyDetails>*/
                                List<String> Node = prsObj.parseParentNode(
                                        inputpolicylist, "Table");
                                for (String nodeItem : Node) {
                                    totalCount = Integer.parseInt(prsObj.parseXmlTag(nodeItem, "TOTAL"));
                                }

                            } else {
                                totalCount = 0;
                            }
                        } else {
                            totalCount = 0;
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

            } catch (Exception e) {
                e.printStackTrace();
                running = false;
            }
            return null;

        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (running) {
                if (totalCount > 0) {

                    Intent intent = new Intent(mContext, MedicalPendingRequirementActivity.class);
                    intent.putExtra("fromHome", "Y");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
                            intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    String title = "Pending Requirement";
                    String message = "Pending Requirement -  Medical :  " + totalCount;
                    int notificationId = -3;
                    mCommonMethods.commonNotification(mContext, pendingIntent, title, message, notificationId);


                }
            }
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString(mCommonMethods.getMEDICAL_PENDING_REQ_COUNT_NOTIFICATION_PREFERENCE(), "true");
            editor.apply();
            startDownloadMaturutyListCount();
        }
    }

    class DownloadFileAsyncTaskMaturityListCount extends AsyncTask<String, String, String> {
        private volatile boolean running = true;

        int totalCount = 0;

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

                String METHOD_NAME_MATURITY_LIST_COUNT = "getMaturityCountNotify_smrt";
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_MATURITY_LIST_COUNT);
                request.addProperty("strCode", mCommonMethods.setUserDetails(mContext).getStrCIFBDMUserId());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                String SOAP_ACTION_MATURITY_LIST_COUNT = "http://tempuri.org/getMaturityCountNotify_smrt";
                androidHttpTranport.call(SOAP_ACTION_MATURITY_LIST_COUNT,
                        envelope);

                Object sa = envelope.getResponse();
                String inputpolicylist = sa.toString();

                System.out.println("response:" + inputpolicylist);
                if (!inputpolicylist.contentEquals("<PolicyDetails />")) {

                    try {

                        if (!sa.toString().equalsIgnoreCase("")) {
                            ParseXML prsObj = new ParseXML();

                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "PolicyDetails");
                            if (inputpolicylist != null) {

                                /*For success : -
                                   <PolicyDetails> <Table> <TOTAL>1</TOTAL> </Table> </PolicyDetails>*/
                                List<String> Node = prsObj.parseParentNode(
                                        inputpolicylist, "Table");
                                for (String nodeItem : Node) {
                                    totalCount = Integer.parseInt(prsObj.parseXmlTag(nodeItem, "COUNT_OF_POLICIES"));
                                }

                            } else {
                                totalCount = 0;
                            }
                        } else {
                            totalCount = 0;
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

            } catch (Exception e) {
                e.printStackTrace();
                running = false;
            }
            return null;

        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (running) {
                if (totalCount > 0) {


                    Intent intent;
                    if (strUserType.equalsIgnoreCase("CIF") || strUserType.equalsIgnoreCase("BDM")) {
                        intent = new Intent(mContext, BancaReportsMaturityActivity.class);
                    } else {
                        intent = new Intent(mContext, AgencyReportsMaturityActivity.class);
                    }

                    intent.putExtra("fromHome", "Y");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
                            intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    System.out.println("totalCount = [" + totalCount + "]");
                    String title = "Maturity Dues";
                    String message = "Maturity Dues :  " + totalCount;
                    int notificationId = -4;
                    mCommonMethods.commonNotification(mContext, pendingIntent, title, message, notificationId);


                }


            }
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString(mCommonMethods.MATURITY_LIST_NOTIFICATION_PREFERENCE, "true");
            editor.apply();
            startDownloadSBDueListListCount();
        }
    }

    class DownloadFileAsyncTaskSBDueListCount extends AsyncTask<String, String, String> {
        private volatile boolean running = true;

        int totalCount = 0;

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

                String METHOD_NAME_SB_DUE_LIST_COUNT = "getSBDueCountNotify_smrt";
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_SB_DUE_LIST_COUNT);
                request.addProperty("strCode", mCommonMethods.setUserDetails(mContext).getStrCIFBDMUserId());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                String SOAP_ACTION_SB_DUE_LIST_COUNT = "http://tempuri.org/getSBDueCountNotify_smrt";
                androidHttpTranport.call(SOAP_ACTION_SB_DUE_LIST_COUNT,
                        envelope);

                Object sa = envelope.getResponse();
                String inputpolicylist = sa.toString();

                System.out.println("response:" + inputpolicylist);
                if (!inputpolicylist.contentEquals("<PolicyDetails />")) {

                    try {

                        if (!sa.toString().equalsIgnoreCase("")) {
                            ParseXML prsObj = new ParseXML();

                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "PolicyDetails");
                            if (inputpolicylist != null) {

                                /*For success : -
                                   <PolicyDetails> <Table> <TOTAL>1</TOTAL> </Table> </PolicyDetails>*/
                                List<String> Node = prsObj.parseParentNode(
                                        inputpolicylist, "Table");
                                for (String nodeItem : Node) {
                                    totalCount = Integer.parseInt(prsObj.parseXmlTag(nodeItem, "COUNT_OF_POLICIES"));
                                }

                            } else {
                                totalCount = 0;
                            }
                        } else {
                            totalCount = 0;
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

            } catch (Exception e) {
                e.printStackTrace();
                running = false;
            }
            return null;

        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (running) {
                if (totalCount > 0) {

                    Intent intent;
                    if (strUserType.equalsIgnoreCase("CIF") || strUserType.equalsIgnoreCase("BDM")) {
                        intent = new Intent(mContext, BancaReportsSBDueListActivity.class);
                    } else {
                        intent = new Intent(mContext, AgencyReportsSBDueListActivity.class);
                    }

                    intent.putExtra("fromHome", "Y");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
                            intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    String title = "Survival Benefit Dues";
                    String message = "Survival Benefit Dues :  " + totalCount;
                    int notificationId = -5;
                    mCommonMethods.commonNotification(mContext, pendingIntent, title, message, notificationId);
                }

            }
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString(mCommonMethods.SB_DUE_LIST_NOTIFICATION_PREFERENCE, "true");
            editor.apply();
            startDownloadClaimRequirementInfo();

        }
    }

    class DownloadFileAsyncTaskClaimRequirementInfo extends AsyncTask<String, String, String> {
        private volatile boolean running = true;

        int flag = 0;

        List<ParseXML.ClaimRequirementInfoValuesModel> nodeData = new ArrayList<>();

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

                String METHOD_NAME = "getClaimReqInfo";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("strCode", mCommonMethods.setUserDetails(mContext).getStrCIFBDMUserId());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                String SOAP_ACTION = "http://tempuri.org/getClaimReqInfo";
                androidHttpTranport.call(SOAP_ACTION, envelope);

                Object sa = envelope.getResponse();
                String inputpolicylist = sa.toString();

                /*<Data> <Table>
                <CLAIM_TYPE>Individual Annuity</CLAIM_TYPE>
                <AMOUNT>8271</AMOUNT>
                <POLICY_NO>22001842006</POLICY_NO>
                <LA>Mr Tukaram Punjaji Lahane</LA> </Table>
                <Table> <CLAIM_TYPE>Individual Annuity</CLAIM_TYPE> <AMOUNT>8227</AMOUNT>
                <POLICY_NO>22001842104</POLICY_NO> <LA>Mrs Indu Tukaram Lahane</LA> </Table> </Data>*/

                if (inputpolicylist.equalsIgnoreCase("0")) {
                    flag = 0;
                } else {
                    flag = 1;
                    ParseXML prsObj = new ParseXML();
                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "Data");

                    if (inputpolicylist != null) {
                        List<String> Node = prsObj.parseParentNode(
                                inputpolicylist, "Table");
                        nodeData = prsObj.parseNodeClaimRequirementInfo(Node);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                running = false;
            }
            return null;

        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (running) {
                if (flag == 1) {
                    for (int i = 0; i < nodeData.size(); i++) {


                        String title = "Claim payout of policy sourced by you";
                        String message = nodeData.get(i).getCLAIM_TYPE() + " of your sourced policy no." + nodeData.get(i).getPOLICY_NO()
                                + " for Rs. " + nodeData.get(i).getAMOUNT() + "/-"
                                + " in favour of " + nodeData.get(i).getLA()
                                + " has been processed for payment. Please take a note of the same.";

                        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
                                new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
                        mCommonMethods.commonNotification(mContext, pendingIntent, title, message, i);

                    }
                }
            }
            //SharedPreferences preferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString(mCommonMethods.getClaimRequirementInfo(), "true");
            editor.apply();
            startDownloadKYCMissingNotification();

        }
    }

    class DownloadFileAsyncNonMedicalRequirement extends AsyncTask<String, String, String> {
        private volatile boolean running = true;

        private int totalCount = -1;

        //List<ParseXML.EFTPendingForm> nodeData = new ArrayList<ParseXML.EFTPendingForm>();
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

                String METHOD_NAME_NON_MEDICAL_REQUIREMENT = "getNonMedReq_noti_smrt";
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_NON_MEDICAL_REQUIREMENT);
                request.addProperty("strCode", mCommonMethods.setUserDetails(mContext).getStrCIFBDMUserId());
                request.addProperty("strType", "Pending for Requirement - Non Medical");//new DatabaseHelper(context).GetUserType());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                String SOAP_ACTION_NON_MEDICAL_REQUIREMENT = "http://tempuri.org/getNonMedReq_noti_smrt";
                androidHttpTranport.call(SOAP_ACTION_NON_MEDICAL_REQUIREMENT,
                        envelope);

                Object sa = envelope.getResponse();
                String inputpolicylist = sa.toString();

                System.out.println("response:" + inputpolicylist);
                if (!inputpolicylist.contentEquals("<PolicyDetails />")) {
                    try {
                        if (!sa.toString().equalsIgnoreCase("")) {
                            ParseXML prsObj = new ParseXML();

                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "PolicyDetails");
                            if (inputpolicylist != null) {

                                /*For success : -
                                   <PolicyDetails> <Table> <TOTAL>1</TOTAL> </Table> </PolicyDetails>*/
                                List<String> Node = prsObj.parseParentNode(
                                        inputpolicylist, "Table");
                                for (String nodeItem : Node) {
                                    totalCount = Integer.parseInt(prsObj.parseXmlTag(nodeItem, "TOTAL"));
                                }

                            } else {
                                totalCount = 0;
                            }
                        } else {
                            totalCount = 0;
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

            } catch (Exception e) {
                e.printStackTrace();
                running = false;
            }
            return null;

        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (running) {
                if (totalCount > 0) {

                    Intent intent = new Intent(mContext, DocUploadNonMedicalPendingActivity.class);
                    intent.putExtra("fromHome", "Y");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
                            intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    String title = "Pending Requirement";
                    String message = "Pending Requirement - Non Medical :  " + totalCount;
                    int notificationId = -2;
                    mCommonMethods.commonNotification(mContext, pendingIntent, title, message, notificationId);
                }
            }
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString(mCommonMethods.getNON_MEDICAL_PENDING_REQ_COUNT_NOTIFICATION_PREFERENCE(), "true");
            editor.apply();
            startDownloadMedicalPendingRequirement();
        }
    }

    class DownloadFileAsyncRenewal_update extends AsyncTask<String, String, String> {

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
                SoapObject request;

                Calendar cal = Calendar.getInstance();
                int mYear = cal.get(Calendar.YEAR);
                int mMonth = cal.get(Calendar.MONTH);
                int mDay = cal.get(Calendar.DAY_OF_MONTH);
                cal.add(Calendar.DAY_OF_MONTH, 7);

                SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
                String outputdate = sdf1.format(cal.getTime());

                String y = String.valueOf(mYear);
                String m = String.valueOf(mMonth + 1);
                String d = String.valueOf(mDay);

                String todaysdate = m + "-" + d + "-" + y;

                String METHOD_NAME_RENEWAL_LIST_UPDATE = "getAgentPoliciesRenewalListMonthwise";
                request = new SoapObject(NAMESPACE, METHOD_NAME_RENEWAL_LIST_UPDATE);

                request.addProperty("strAgentNo", strCIFBDMUserId);

                request.addProperty("strFromReqDate", todaysdate);
                request.addProperty("strToReqDate", outputdate);

                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());

                SoapSerializationEnvelope envelope =
                        new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                try {
                    String SOAP_ACTION_RENEWAL_LIST_UPDATE = "http://tempuri.org/getAgentPoliciesRenewalListMonthwise";
                    androidHttpTranport.call(SOAP_ACTION_RENEWAL_LIST_UPDATE, envelope);

                    Object response = envelope.getResponse();

                    if (!response.toString().equalsIgnoreCase("anyType{}")) {

                        SoapPrimitive sa;
                        try {
                            sa = (SoapPrimitive) envelope.getResponse();

                            String inputpolicylist = sa.toString();

                            ParseXML prsObj = new ParseXML();

                            inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "CIFPolicyList");
                            inputpolicylist = new ParseXML().parseXmlTag(inputpolicylist, "ScreenData");
                            strRevivalListErrorCOde1 = inputpolicylist;

                            if (strRevivalListErrorCOde1 == null) {
                                inputpolicylist = sa.toString();
                                inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "CIFPolicyList");

                                List<String> Node = prsObj.parseParentNode(inputpolicylist, "Table");
                                List<ParseXML.XMLHolderRenewal> nodeData = prsObj.parseNodeElementRenewal(Node);
                                lstRevivalListCount1 = nodeData.size();
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
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    running = false;
                }
            } catch (Exception e) {
                try {
                    throw (e);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                running = false;
            }
            return null;

        }


        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (running) {
                if (strRevivalListErrorCOde1 == null) {

                    String title = "Total Renewal Policy";
                    String message = "Total renewal policy for next 7 days. :  " + lstRevivalListCount1;
                    int notificationId = -6;
                    Intent intent = new Intent(mContext, BancaReportsRenewalActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
                            intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mCommonMethods.commonNotification(mContext, pendingIntent, title, message, notificationId);
                }
            }
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString(mCommonMethods.getDASHBOARD_RENEWAL_UPDATE_PREFERENCE(), "true");
            editor.apply();
            startDownloadRevivalNotification();
        }
    }

   /* private void playProductExplainerVideo() {

        mCommonMethods.callActivity(mContext, ActivityProductExplainer.class);

    }*/

    class RevivalNotifiationAsyncTask extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private int totalRevivedCasesCount = 0;
        private int totalRevivableCasesCount = 0;

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

                String METHOD_NAME_REVIVAL_NOTIFICATION = "getRevivalSummary_smrt";
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_REVIVAL_NOTIFICATION);
                request.addProperty("strCode", mCommonMethods.setUserDetails(mContext).getStrCIFBDMUserId());
                request.addProperty("strType", strUserType);
                mCommonMethods.appendSecurityParams(mContext, request, strCIFBDMEmailId, strCIFBDMMObileNo);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                String SOAP_ACTION_REVIVAL_NOTIFICATION = "http://tempuri.org/" + METHOD_NAME_REVIVAL_NOTIFICATION;
                androidHttpTranport.call(SOAP_ACTION_REVIVAL_NOTIFICATION,
                        envelope);

                Object sa = envelope.getResponse();
                String inputpolicylist = sa.toString();

                System.out.println("response:" + inputpolicylist);
                if (!inputpolicylist.contentEquals("<Data />")) {
                    if (!sa.toString().equalsIgnoreCase("")) {
                        ParseXML prsObj = new ParseXML();

                        inputpolicylist = prsObj.parseXmlTag(
                                inputpolicylist, "Data");
                        if (inputpolicylist != null) {

                                /*For success : -
                                   <Data> <Table> <TOTAL_REVIVED_CASES>2</TOTAL_REVIVED_CASES>
                                   <TOTAL_REVIVABLE_CASES>37</TOTAL_REVIVABLE_CASES> </Table> </Data> */
                            List<String> Node = prsObj.parseParentNode(
                                    inputpolicylist, "Table");
                            for (String nodeItem : Node) {
                                totalRevivedCasesCount = Integer.parseInt(prsObj.parseXmlTag(nodeItem, "TOTAL_REVIVED_CASES"));
                                totalRevivableCasesCount = Integer.parseInt(prsObj.parseXmlTag(nodeItem, "TOTAL_REVIVABLE_CASES"));
                            }

                        } else {
                            totalRevivedCasesCount = 0;
                            totalRevivableCasesCount = 0;
                        }
                    } else {
                        totalRevivedCasesCount = 0;
                        totalRevivableCasesCount = 0;
                        running = false;
                        mProgressDialog.dismiss();

                    }
                } else {
                    mProgressDialog.dismiss();
                    running = false;
                }

            } catch (Exception e) {
                e.printStackTrace();
                running = false;
            }
            return null;

        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (running) {
                if (totalRevivableCasesCount > 0 || totalRevivedCasesCount > 0) {


                    Intent intent = new Intent(mContext, RevivalCampaignReportList.class);
                    intent.putExtra("fromHome", "Y");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
                            intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    String title = "Revival Campaign";
                    String message = "Have you revived your Customer policy???\nTotal Revivable cases - "
                            + totalRevivableCasesCount + ", Total Revived cases - " + totalRevivedCasesCount
                            + ".Tap to revive";

                    int notificationId = -9;
                    // mCommonMethods.commonNotification(mContext, pendingIntent, title, message, notificationId);
                    mCommonMethods.commonNotification(mContext, pendingIntent, title, message, notificationId);
                }

            }
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString(mCommonMethods.getRevivalNotificationInfo(), "true");
            editor.apply();
            startDownloadLoggedProposalCount();
        }
    }


    class KYCMissingNotifiationAsyncTask extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String KYCStatus = "0";

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
                //getNotifyKYC(string strCode, string strEmailId, string strMobileNo, string strAuthKey)
                String METHOD_NAME_KYC_MISSING = "getNotifyKYC";
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_KYC_MISSING);
                request.addProperty("strCode", mCommonMethods.setUserDetails(mContext).getStrCIFBDMUserId());
                mCommonMethods.appendSecurityParams(mContext, request, strCIFBDMEmailId, strCIFBDMMObileNo);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                String SOAP_ACTION_KYC_MISSING = "http://tempuri.org/" + METHOD_NAME_KYC_MISSING;
                androidHttpTranport.call(SOAP_ACTION_KYC_MISSING,
                        envelope);

                Object sa = envelope.getResponse();
                String inputpolicylist = sa.toString();

                KYCStatus = inputpolicylist;

            } catch (Exception e) {
                e.printStackTrace();
                running = false;
            }
            return null;

        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (running) {
                if (!KYCStatus.equalsIgnoreCase("0")) {
                    Intent intent = new Intent();
                    PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
                            intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    String title = "KYC Notification";
                    int notificationId = -10;
                    // mCommonMethods.commonNotification(mContext, pendingIntent, title, message, notificationId);
                    mCommonMethods.commonNotification(mContext, pendingIntent, title, KYCStatus, notificationId);
                }


            }
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString(mCommonMethods.getKYCMissingNotification(), "true");
            editor.apply();

            promotionalAppMessageAsyncTask = new PromotionalAppMessageAsyncTask();
            promotionalAppMessageAsyncTask.execute();
        }
    }


    class PromotionalAppMessageAsyncTask extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String appMsgStatus = "0";

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
                //getNotifyKYC(string strCode, string strEmailId, string strMobileNo, string strAuthKey)
                String METHOD_NAME_AP_MESSAGE = "getAppMessage";
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_AP_MESSAGE);
                request.addProperty("strAppName", "Smart Advisor");
                //mCommonMethods.appendSecurityParams(mContext, request, strCIFBDMEmailId, strCIFBDMMObileNo);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                String SOAP_ACTION_AP_MESSAGE = "http://tempuri.org/" + METHOD_NAME_AP_MESSAGE;
                androidHttpTranport.call(SOAP_ACTION_AP_MESSAGE,
                        envelope);

                Object sa = envelope.getResponse();
                String inputpolicylist = sa.toString();
               /* <PolicyData> <Table>
            <APP_VERSION>2.0</APP_VERSION>
            <APP_EFFECTIVE_DATE>02-11-2020</APP_EFFECTIVE_DATE>
            <APP_NO_OF_DAYS>15</APP_NO_OF_DAYS>
            <APP_MSG>A Smarter Choice to insure your future. Stay tuned for a smart savings plan https://bit.ly/3fVOphY 2M.Cr1.ver.01-12-20 WEB B ENG</APP_MSG>
                // </Table> </PolicyData>*/

                if (!inputpolicylist.contentEquals("<PolicyData />")) {

                    try {

                        if (!sa.toString().equalsIgnoreCase("")) {
                            ParseXML prsObj = new ParseXML();

                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "PolicyData");
                            if (inputpolicylist != null) {

                                /*For success : -
                                   <PolicyDetails> <Table> <TOTAL>1</TOTAL> </Table> </PolicyDetails>*/
                                List<String> Node = prsObj.parseParentNode(
                                        inputpolicylist, "Table");
                                for (String nodeItem : Node) {
                                    appMsgStatus = prsObj.parseXmlTag(nodeItem, "APP_MSG");
                                }

                            } else {
                                appMsgStatus = "0";
                            }
                        } else {
                            appMsgStatus = "0";
                            running = false;
                            mProgressDialog.dismiss();

                        }
                    } catch (Exception e) {
                        mProgressDialog.dismiss();
                        running = false;
                    }
                } else {
                    mProgressDialog.dismiss();
                    running = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                running = false;
            }
            return null;

        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (running) {
                if (!appMsgStatus.equalsIgnoreCase("0")) {
                   /* final SpannableString spannableString =
                            new SpannableString(appMsgStatus);
                    Linkify.addLinks(spannableString, Linkify.WEB_URLS);


                    Intent intent = new Intent();
                    PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
                            intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    String title = "Message";
                    int notificationId = -11;
                    mCommonMethods.commonNotification(mContext, pendingIntent, title, spannableString.toString(), notificationId);*/
                    appMsgStatus = appMsgStatus.replace("\\n\\n", "\n\n");
                    Log.d("msg = ", appMsgStatus);
                    final SpannableString spannableString = new SpannableString(appMsgStatus); // msg should have url to enable clicking
                    Linkify.addLinks(spannableString, Linkify.WEB_URLS);

                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                    LayoutInflater inflater = ((AppCompatActivity) mContext).getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialog_with_ok_button, null);
                    builder1.setView(dialogView);
                    TextView text = dialogView.findViewById(R.id.tv_title);
                    text.setText(spannableString);
                    text.setMovementMethod(LinkMovementMethod.getInstance());
                    Button dialogButton = dialogView.findViewById(R.id.bt_ok);
                    dialogButton.setText("Ok");
                    final AlertDialog dialog = builder1.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    //dialog.setContentView(R.layout.dialog_with_ok_button);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        }
    }


    class LastLoginTimeAsyncTask extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String loginTime = "0";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... aurl) {

            try {
                running = true;
                //saveLoginLog_smrt(string strCode,string strType, string strEmailId, string strMobileNo, string strAuthKey)
                String METHOD_NAME_LOGIN_TIME = "saveLoginLog_smrt";
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_LOGIN_TIME);
                request.addProperty("strCode", strCIFBDMUserId);
                request.addProperty("strType", "SmartAdvisor");
                mCommonMethods.appendSecurityParams(mContext, request, strCIFBDMEmailId, strCIFBDMMObileNo);

                System.out.println("request.toString() = " + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                String SOAP_ACTION_AP_MESSAGE = "http://tempuri.org/" + METHOD_NAME_LOGIN_TIME;
                androidHttpTranport.call(SOAP_ACTION_AP_MESSAGE,
                        envelope);

                Object sa = envelope.getResponse();
                String inputpolicylist = sa.toString();
                System.out.println("request.toString() = " + inputpolicylist);
                if (inputpolicylist.equalsIgnoreCase("0")) {
                    running = false;
                    loginTime = "0";

                } else {
                    loginTime = inputpolicylist;
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
                if (!loginTime.equalsIgnoreCase("0")) {
                    tvLastLoginTime.setText("Last Login Date & Time - " + loginTime);
                    tvLastLoginTime.setVisibility(View.VISIBLE);
                } else {
                    tvLastLoginTime.setText("");
                    tvLastLoginTime.setVisibility(View.GONE);
                }
            }
        }
    }


    class AgentNotificationAsyncTask extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private int flag;
        private String SLAB, GAP_NEXT_SLAB;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... aurl) {

            try {
                running = true;
                //getTentativeQalifierSummary(string strCode, string strEmailId, string strMobileNo, string strAuthKey)
                String METHOD_NAME = "getTentativeQalifierSummary";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("strCode", strCIFBDMUserId);
                mCommonMethods.appendSecurityParams(mContext, request, strCIFBDMEmailId, strCIFBDMMObileNo);

                System.out.println("request.toString() = " + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                String SOAP_ACTION_AP_MESSAGE = "http://tempuri.org/" + METHOD_NAME;
                androidHttpTranport.call(SOAP_ACTION_AP_MESSAGE,
                        envelope);

                Object sa = envelope.getResponse();
                String inputpolicylist = sa.toString();
                System.out.println("request.toString() = " + inputpolicylist);
                if (inputpolicylist.equalsIgnoreCase("0")) {
                    flag = 0;
                } else {
                    flag = 1;
                    ParseXML prsObj = new ParseXML();
                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "Data");

                    if (inputpolicylist != null) {
                        String Node = prsObj.parseXmlTag(
                                inputpolicylist, "Table");
                        //<Data> <Table> <IA_CODE>990707863</IA_CODE> <SLAB>SLAB1</SLAB> <GAP_NEXT_SLAB>23000</GAP_NEXT_SLAB> </Table> </Data>
                        SLAB = prsObj.parseXmlTag(Node, "SLAB");
                        GAP_NEXT_SLAB = prsObj.parseXmlTag(Node, "GAP_NEXT_SLAB");
                    }
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
                    String appMsgStatus = "Congratulations! You have Qualified at " + SLAB + " in Monsoon Magic Campaign on cashiering basis *. " +
                            "Your balance to qualify in next slab is " + GAP_NEXT_SLAB + ".\n" +
                            "\n" + "*The final qualification is subject to Issuance and GST exclusions.";

                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                    LayoutInflater inflater = ((AppCompatActivity) mContext).getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialog_with_ok_button, null);
                    builder1.setView(dialogView);
                    TextView text = dialogView.findViewById(R.id.tv_title);
                    text.setText(appMsgStatus);
                    //text.setMovementMethod(LinkMovementMethod.getInstance());
                    Button dialogButton = dialogView.findViewById(R.id.bt_ok);
                    dialogButton.setText("Ok");
                    final AlertDialog dialog = builder1.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    //dialog.setContentView(R.layout.dialog_with_ok_button);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            dialog.dismiss();
                            /*jotcDhamakaAsyncTask = new JOTCDhamakaAsyncTask();
                            jotcDhamakaAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/
                        }
                    });
                    dialog.show();
                } else {
                    /*jotcDhamakaAsyncTask = new JOTCDhamakaAsyncTask();
                    jotcDhamakaAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/
                }
            } else {
                /*jotcDhamakaAsyncTask = new JOTCDhamakaAsyncTask();
                jotcDhamakaAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/
            }
        }
    }

    class JOTCDhamakaAsyncTask extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private int flag;
        private String JULY_AUGUST_RATED_NB;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... aurl) {

            try {
                running = true;
                //getJOTC_dhamaka(string strCode, string strEmailId, string strMobileNo, string strAuthKey)
                String METHOD_NAME = "getJOTC_dhamaka";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("strCode", strCIFBDMUserId);
                mCommonMethods.appendSecurityParams(mContext, request, strCIFBDMEmailId, strCIFBDMMObileNo);

                System.out.println("request.toString() = " + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                String SOAP_ACTION_AP_MESSAGE = "http://tempuri.org/" + METHOD_NAME;
                androidHttpTranport.call(SOAP_ACTION_AP_MESSAGE,
                        envelope);

                Object sa = envelope.getResponse();
                String inputpolicylist = sa.toString();
                System.out.println("request.toString() = " + inputpolicylist);
                if (inputpolicylist.equalsIgnoreCase("0")) {
                    flag = 0;
                } else {
                    flag = 1;
                    ParseXML prsObj = new ParseXML();
                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "Data");

                    if (inputpolicylist != null) {
                        String Node = prsObj.parseXmlTag(
                                inputpolicylist, "Table");
                        //<Data> <Table> <IA_CODE>990707863</IA_CODE> <SLAB>SLAB1</SLAB> <GAP_NEXT_SLAB>23000</GAP_NEXT_SLAB> </Table> </Data>
                        JULY_AUGUST_RATED_NB = prsObj.parseXmlTag(Node, "JULY_AUGUST_RATED_NB");
                    }
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
                    if (!TextUtils.isEmpty(JULY_AUGUST_RATED_NB)) {
                        long amount = Long.parseLong(JULY_AUGUST_RATED_NB);
                        String appMsgStatus = "";
                        if (amount >= 1500000) {
                            appMsgStatus = "Congratulations on completing JOTC Emerald.." +
                                    "Go for Ruby/Diamond by using double dhamaka credit available till " +
                                    "31st August. All the very best!!! T&C Apply";
                        } else if (amount < 1500000 && amount >= 300000) {
                            appMsgStatus = "Well done you have reached 3 lacs and qualified for double " +
                                    "dhamaka credit. Now qualify for JOTC Emerald by doing the balance 24 lakhs" +
                                    " by using double dhamaka credit available till 31st August. All the very best!!! T&C Apply";
                        } else if (amount < 300000 && amount >= 100000) {
                            appMsgStatus = "Well done you have reached 1 lac. Now complete the balance 2 lacs to" +
                                    " enjoy JOTC double dhamaka credit available till 31st August. " +
                                    "All the very best!!! T&C Apply";
                        }

                        if (!TextUtils.isEmpty(appMsgStatus)) {
                            final AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                            LayoutInflater inflater = ((AppCompatActivity) mContext).getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.dialog_with_ok_button, null);
                            builder1.setView(dialogView);
                            TextView text = dialogView.findViewById(R.id.tv_title);
                            text.setText(appMsgStatus);
                            //text.setMovementMethod(LinkMovementMethod.getInstance());
                            Button dialogButton = dialogView.findViewById(R.id.bt_ok);
                            dialogButton.setText("Ok");
                            final AlertDialog dialog = builder1.create();
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            //dialog.setContentView(R.layout.dialog_with_ok_button);
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    /*monthlyGradAllowAsyncTask = new MonthlyGradAllowAsyncTask();
                                    monthlyGradAllowAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/
                                }
                            });
                            dialog.show();
                        } else {
                           /* monthlyGradAllowAsyncTask = new MonthlyGradAllowAsyncTask();
                            monthlyGradAllowAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/
                        }
                    } else {
                        /*monthlyGradAllowAsyncTask = new MonthlyGradAllowAsyncTask();
                        monthlyGradAllowAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/
                    }
                } else {
                   /* monthlyGradAllowAsyncTask = new MonthlyGradAllowAsyncTask();
                    monthlyGradAllowAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/
                }
            } else {
               /* monthlyGradAllowAsyncTask = new MonthlyGradAllowAsyncTask();
                monthlyGradAllowAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/
            }
        }
    }

    class MonthlyGradAllowAsyncTask extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private int flag;
        private String PPOUP_MESSAGE;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... aurl) {

            try {
                running = true;
                //getGraduationAllowance_dhamaka(string strCode, string strEmailId, string strMobileNo, string strAuthKey)
                String METHOD_NAME = "getGraduationAllowance_dhamaka";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("strCode", strCIFBDMUserId);
                mCommonMethods.appendSecurityParams(mContext, request, strCIFBDMEmailId, strCIFBDMMObileNo);

                System.out.println("request.toString() = " + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                String SOAP_ACTION_AP_MESSAGE = "http://tempuri.org/" + METHOD_NAME;
                androidHttpTranport.call(SOAP_ACTION_AP_MESSAGE,
                        envelope);

                Object sa = envelope.getResponse();
                String inputpolicylist = sa.toString();
                System.out.println("request.toString() = " + inputpolicylist);
                if (inputpolicylist.equalsIgnoreCase("0")) {
                    flag = 0;
                } else {
                    flag = 1;
                    ParseXML prsObj = new ParseXML();
                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "Data");

                    if (inputpolicylist != null) {
                        String Node = prsObj.parseXmlTag(
                                inputpolicylist, "Table");
                        PPOUP_MESSAGE = prsObj.parseXmlTag(Node, "PPOUP_MESSAGE");
                    }
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
                    if (!TextUtils.isEmpty(PPOUP_MESSAGE)) {
                        String appMsgStatus = PPOUP_MESSAGE.replace("&amp;", "&");

                        final AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                        LayoutInflater inflater = ((AppCompatActivity) mContext).getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.dialog_with_ok_button, null);
                        builder1.setView(dialogView);
                        TextView text = dialogView.findViewById(R.id.tv_title);
                        text.setText(appMsgStatus);
                        //text.setMovementMethod(LinkMovementMethod.getInstance());
                        Button dialogButton = dialogView.findViewById(R.id.bt_ok);
                        dialogButton.setText("Ok");
                        final AlertDialog dialog = builder1.create();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        //dialog.setContentView(R.layout.dialog_with_ok_button);
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                dialog.dismiss();
                                whatsAppDialog();
                            }
                        });
                        dialog.show();

                    } else {
                        whatsAppDialog();
                    }

                } else {
                    whatsAppDialog();
                }
            } else {
                whatsAppDialog();
            }
        }
    }

    class UMIncentiveNotificationAsyncTask extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private int flag;
        private String MESSAGE;


        @Override
        protected String doInBackground(String... aurl) {

            try {
                running = true;
                //getIncentiveUM(string strCode, string strEmailId, string strMobileNo, string strAuthKey)
                String METHOD_NAME = "getIncentiveUM";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("strCode", strCIFBDMUserId);
                mCommonMethods.appendSecurityParams(mContext, request, strCIFBDMEmailId, strCIFBDMMObileNo);

                System.out.println("request.toString() = " + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                String SOAP_ACTION_AP_MESSAGE = "http://tempuri.org/" + METHOD_NAME;
                androidHttpTranport.call(SOAP_ACTION_AP_MESSAGE,
                        envelope);

                Object sa = envelope.getResponse();
                String inputpolicylist = sa.toString();
                System.out.println("request.toString() = " + inputpolicylist);
                if (inputpolicylist.equalsIgnoreCase("0")) {
                    flag = 0;
                } else {
                    flag = 1;
                    ParseXML prsObj = new ParseXML();
                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "Data");

                    if (inputpolicylist != null) {
                        String Node = prsObj.parseXmlTag(
                                inputpolicylist, "Table");
                        //<Data> <Table> <IA_CODE>990707863</IA_CODE> <SLAB>SLAB1</SLAB> <GAP_NEXT_SLAB>23000</GAP_NEXT_SLAB> </Table> </Data>
                        MESSAGE = prsObj.parseXmlTag(Node, "MESSAGE");
                    }
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

                    final AlertDialog.Builder builderOutside = new AlertDialog.Builder(mContext);
                    LayoutInflater inflater = ((AppCompatActivity) mContext).getLayoutInflater();
                    View dialogViewOutside = inflater.inflate(R.layout.dialog_with_ok_button, null);
                    builderOutside.setView(dialogViewOutside);
                    TextView textOutside = dialogViewOutside.findViewById(R.id.tv_title);
                    textOutside.setText(MESSAGE);
                    //text.setMovementMethod(LinkMovementMethod.getInstance());
                    Button dialogButtonOutside = dialogViewOutside.findViewById(R.id.bt_ok);
                    dialogButtonOutside.setText("Ok");
                    final AlertDialog dialogOutside = builderOutside.create();
                    dialogOutside.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    //dialog.setContentView(R.layout.dialog_with_ok_button);
                    dialogButtonOutside.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            dialogOutside.dismiss();
                            whatsAppDialog();
                            /*String appMsgStatus = "Breaking News  !!! Introducing Dhamaka Plus, avail 150% credit for September 2021 " +
                                    "for all individual products. Cashiering Period from September 1,2021 to September 30,2021" +
                                    " with Allocation period up to October 18,2021. T&C Apply  Leverage this opportunity for more" +
                                    " Life Mitras in your team to qualify for JOTC 2022.\n" + "\n" +
                                    "Wishing you and your team the very best – Retail Agency";

                            final AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                            View dialogView = inflater.inflate(R.layout.dialog_with_ok_button, null);
                            builder1.setView(dialogView);
                            TextView text = dialogView.findViewById(R.id.tv_title);
                            text.setText(appMsgStatus);
                            Button dialogButton = dialogView.findViewById(R.id.bt_ok);
                            dialogButton.setText("Ok");
                            final AlertDialog dialog = builder1.create();
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();*/
                        }
                    });
                    dialogOutside.show();
                } else {
                    whatsAppDialog();
                }
            } else {
                whatsAppDialog();
            }
        }
    }

    class NBIncentiveUMNotificationAsyncTask extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private int flag;
        private String MESSAGE;

        @Override
        protected String doInBackground(String... aurl) {

            try {
                running = true;
                //getNBIncentiveUM(string strCode, string strEmailId, string strMobileNo, string strAuthKey)
                String METHOD_NAME = "getNBIncentiveUM";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("strCode", strCIFBDMUserId);
                mCommonMethods.appendSecurityParams(mContext, request, strCIFBDMEmailId, strCIFBDMMObileNo);

                System.out.println("request.toString() = " + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                String SOAP_ACTION_AP_MESSAGE = "http://tempuri.org/" + METHOD_NAME;
                androidHttpTranport.call(SOAP_ACTION_AP_MESSAGE,
                        envelope);

                Object sa = envelope.getResponse();
                String inputpolicylist = sa.toString();
                System.out.println("request.toString() = " + inputpolicylist);
                if (inputpolicylist.equalsIgnoreCase("0")) {
                    flag = 0;
                } else {
                    flag = 1;
                    ParseXML prsObj = new ParseXML();
                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "Data");

                    if (inputpolicylist != null) {
                        String Node = prsObj.parseXmlTag(
                                inputpolicylist, "Table");
                        //<Data> <Table> <IA_CODE>990707863</IA_CODE> <SLAB>SLAB1</SLAB> <GAP_NEXT_SLAB>23000</GAP_NEXT_SLAB> </Table> </Data>
                        MESSAGE = prsObj.parseXmlTag(Node, "MESSAGE");
                        MESSAGE = MESSAGE.replace("ALL THE VERY", "ALL THE VERY BEST.");
                    }
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

                    final AlertDialog.Builder builderOutside = new AlertDialog.Builder(mContext);
                    LayoutInflater inflater = ((AppCompatActivity) mContext).getLayoutInflater();
                    View dialogViewOutside = inflater.inflate(R.layout.dialog_with_ok_button, null);
                    builderOutside.setView(dialogViewOutside);
                    TextView textOutside = dialogViewOutside.findViewById(R.id.tv_title);
                    textOutside.setText(MESSAGE);
                    //text.setMovementMethod(LinkMovementMethod.getInstance());
                    Button dialogButtonOutside = dialogViewOutside.findViewById(R.id.bt_ok);
                    dialogButtonOutside.setText("Ok");
                    final AlertDialog dialogOutside = builderOutside.create();
                    dialogOutside.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    //dialog.setContentView(R.layout.dialog_with_ok_button);
                    dialogButtonOutside.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            dialogOutside.dismiss();
                            umIncentiveNotificationAsyncTask = new UMIncentiveNotificationAsyncTask();
                            umIncentiveNotificationAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                    });
                    dialogOutside.show();
                } else {
                    umIncentiveNotificationAsyncTask = new UMIncentiveNotificationAsyncTask();
                    umIncentiveNotificationAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            } else {
                umIncentiveNotificationAsyncTask = new UMIncentiveNotificationAsyncTask();
                umIncentiveNotificationAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    }

    private void whatsAppDialog() {
        String appMsgStatus = " We are now on WhatsApp. Policy holder can give a missed call on +919029006575 to " +
                "connect with us & explore our services.";

        Log.d("msg = ", appMsgStatus);
        final SpannableString spannableString = new SpannableString(appMsgStatus); // msg should have url to enable clicking
        Linkify.addLinks(spannableString, Linkify.PHONE_NUMBERS);

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = ((AppCompatActivity) mContext).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_with_ok_button, null);
        builder1.setView(dialogView);
        TextView text = dialogView.findViewById(R.id.tv_title);
        text.setText(spannableString);
        text.setMovementMethod(LinkMovementMethod.getInstance());
        Button dialogButton = dialogView.findViewById(R.id.bt_ok);
        dialogButton.setText("Ok");
        final AlertDialog dialog = builder1.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.setContentView(R.layout.dialog_with_ok_button);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    class JOTCMessageAsyncTask extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private int flag;
        private String IA_NAME, TOTAL_NET_RATED_PREMIUM, SLAB,
                TOTAL_NET_RATED_PROTECTION, GAP_TO_NEXT_SLAB,
                JOTC_STATUS, PROTECTION_SHORTFALL;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... aurl) {

            try {
                running = true;
                //getJOTC_msg(string strCode, string strEmailId, string strMobileNo, string strAuthKey)
                String METHOD_NAME = "getJOTC_msg";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("strCode", strCIFBDMUserId);
                mCommonMethods.appendSecurityParams(mContext, request, strCIFBDMEmailId, strCIFBDMMObileNo);

                System.out.println("request.toString() = " + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                String SOAP_ACTION_AP_MESSAGE = "http://tempuri.org/" + METHOD_NAME;
                androidHttpTranport.call(SOAP_ACTION_AP_MESSAGE,
                        envelope);

                Object sa = envelope.getResponse();
                String inputpolicylist = sa.toString();
                System.out.println("request.toString() = " + inputpolicylist);
                if (inputpolicylist.equalsIgnoreCase("0")) {
                    flag = 0;
                } else {
                    flag = 1;
                    ParseXML prsObj = new ParseXML();
                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "Data");

                    if (inputpolicylist != null) {
                        String Node = prsObj.parseXmlTag(
                                inputpolicylist, "Table");

                        //<Data>
                        //  <Table>
                        //    <IA_CODE>990588105</IA_CODE>
                        //    <IA_NAME>Irene  Dlima</IA_NAME>
                        //    <TOTAL_NET_RATED_PREMIUM>3000000</TOTAL_NET_RATED_PREMIUM>
                        //    <SLAB>30 LACS &amp;amp; ABOVE</SLAB>
                        //    <TOTAL_NET_RATED_PROTECTION>20000</TOTAL_NET_RATED_PROTECTION>
                        //    <GAP_TO_NEXT_SLAB>0</GAP_TO_NEXT_SLAB>
                        //    <JOTC_STATUS>NOT QUALIFIED</JOTC_STATUS>
                        //    <PROTECTION_SHORTFALL>80000</PROTECTION_SHORTFALL>
                        //  </Table>
                        //</Data>
                        IA_NAME = prsObj.parseXmlTag(Node, "IA_NAME");
                        IA_NAME = IA_NAME == null ? "" : IA_NAME;

                        TOTAL_NET_RATED_PREMIUM = prsObj.parseXmlTag(Node, "TOTAL_NET_RATED_PREMIUM");
                        TOTAL_NET_RATED_PREMIUM = TOTAL_NET_RATED_PREMIUM == null ? "" : TOTAL_NET_RATED_PREMIUM;

                        SLAB = prsObj.parseXmlTag(Node, "SLAB");
                        SLAB = SLAB == null ? "" : SLAB;

                        TOTAL_NET_RATED_PROTECTION = prsObj.parseXmlTag(Node, "TOTAL_NET_RATED_PROTECTION");
                        TOTAL_NET_RATED_PROTECTION = TOTAL_NET_RATED_PROTECTION == null ? "" : TOTAL_NET_RATED_PROTECTION;

                        GAP_TO_NEXT_SLAB = prsObj.parseXmlTag(Node, "GAP_TO_NEXT_SLAB");
                        GAP_TO_NEXT_SLAB = GAP_TO_NEXT_SLAB == null ? "" : GAP_TO_NEXT_SLAB;

                        JOTC_STATUS = prsObj.parseXmlTag(Node, "JOTC_STATUS");
                        JOTC_STATUS = JOTC_STATUS == null ? "" : JOTC_STATUS;

                        PROTECTION_SHORTFALL = prsObj.parseXmlTag(Node, "PROTECTION_SHORTFALL");
                        PROTECTION_SHORTFALL = PROTECTION_SHORTFALL == null ? "" : PROTECTION_SHORTFALL;
                    }
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
                if (TextUtils.isEmpty(IA_NAME)) {
                    IA_NAME = "LM";
                }
                String msg = "Dear " + IA_NAME + ", your JOTC qualification status is " + JOTC_STATUS + ".For the next slab " +
                        "your NB shortfall is " + GAP_TO_NEXT_SLAB + " and the protection short fall is " +
                        PROTECTION_SHORTFALL + " ALL THE VERY BEST!!!\n" +
                        "*T&C applied. Qualification will be subject to persistency as per norms.";
                if (flag == 1) {
                        /*long amount = Long.parseLong(JULY_AUGUST_RATED_NB);
                        String appMsgStatus = "";
                        if (amount >= 1500000) {
                            appMsgStatus = "Congratulations on completing JOTC Emerald.." +
                                    "Go for Ruby/Diamond by using double dhamaka credit available till " +
                                    "31st August. All the very best!!! T&C Apply";
                        } else if (amount < 1500000 && amount >= 300000) {
                            appMsgStatus = "Well done you have reached 3 lacs and qualified for double " +
                                    "dhamaka credit. Now qualify for JOTC Emerald by doing the balance 24 lakhs" +
                                    " by using double dhamaka credit available till 31st August. All the very best!!! T&C Apply";
                        } else if (amount < 300000 && amount >= 100000) {
                            appMsgStatus = "Well done you have reached 1 lac. Now complete the balance 2 lacs to" +
                                    " enjoy JOTC double dhamaka credit available till 31st August. " +
                                    "All the very best!!! T&C Apply";
                        }*/

                            final AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                            LayoutInflater inflater = ((AppCompatActivity) mContext).getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.dialog_with_ok_button, null);
                            builder1.setView(dialogView);
                            TextView text = dialogView.findViewById(R.id.tv_title);
                            text.setText(msg);
                            //text.setMovementMethod(LinkMovementMethod.getInstance());
                            Button dialogButton = dialogView.findViewById(R.id.bt_ok);
                            dialogButton.setText("Ok");
                            final AlertDialog dialog = builder1.create();
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            //dialog.setContentView(R.layout.dialog_with_ok_button);
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    monthlyGradAllowAsyncTask = new MonthlyGradAllowAsyncTask();
                                    monthlyGradAllowAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                }
                            });
                            dialog.show();


                } else {
                    monthlyGradAllowAsyncTask = new MonthlyGradAllowAsyncTask();
                    monthlyGradAllowAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            } else {
                monthlyGradAllowAsyncTask = new MonthlyGradAllowAsyncTask();
                monthlyGradAllowAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    }

    private void displayAgentMessages() {
        String appMsgStatus = "Dear Life Mitra,\n" +
                "\n" + "THE FINAL OPPORTUNITY to leverage JOTC BOOSTER. You are eligible for " +
                "150% Credit on every Rated NBP across all Products including Protection Business " +
                "subject to Cashiering of Minimum Rated Business of 2 lakhs in the month of October" +
                " 2021 & Issuance up to 15th November 2021.  \n" +
                "Wishing you the very best – Retail Agency";

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = ((AppCompatActivity) mContext).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_with_ok_button, null);
        builder1.setView(dialogView);
        TextView text = dialogView.findViewById(R.id.tv_title);
        text.setText(appMsgStatus);
        //text.setMovementMethod(LinkMovementMethod.getInstance());
        Button dialogButton = dialogView.findViewById(R.id.bt_ok);
        dialogButton.setText("Ok");
        final AlertDialog dialog = builder1.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.setContentView(R.layout.dialog_with_ok_button);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                monthlyGradAllowAsyncTask = new MonthlyGradAllowAsyncTask();
                monthlyGradAllowAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        dialog.show();
    }

    class CruiseMessageAsyncTask extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private int flag;
        private String IA_CODE,CASHIERING,FINAL_WEIGHTED_PREMIUM,FINAL_SLAB,SHORT_FALL;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... aurl) {

            try {
                running = true;
                //getCRUISE_msg(string strCode, string strEmailId, string strMobileNo, string strAuthKey)
                String METHOD_NAME = "getCRUISE_msg";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("strCode", strCIFBDMUserId);
                mCommonMethods.appendSecurityParams(mContext, request, strCIFBDMEmailId, strCIFBDMMObileNo);

                System.out.println("request.toString() = " + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                String SOAP_ACTION_AP_MESSAGE = "http://tempuri.org/" + METHOD_NAME;
                androidHttpTranport.call(SOAP_ACTION_AP_MESSAGE,
                        envelope);

                Object sa = envelope.getResponse();
                String inputpolicylist = sa.toString();
                System.out.println("request.toString() = " + inputpolicylist);
                if (inputpolicylist.equalsIgnoreCase("0")) {
                    flag = 0;
                } else {
                    flag = 1;
                    ParseXML prsObj = new ParseXML();
                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "Data");

                    if (inputpolicylist != null) {
                        String Node = prsObj.parseXmlTag(
                                inputpolicylist, "Table");

                                      /* <Data>
                                        <Table>
                                            <IA_CODE>10044267</IA_CODE>
                                            <CASHIERING>811696</CASHIERING>
                                            <FINAL_WEIGHTED_PREMIUM>650375</FINAL_WEIGHTED_PREMIUM>
                                            <FINAL_SLAB>L2</FINAL_SLAB>
                                            <SHORT_FALL>149625</SHORT_FALL>
                                        </Table>
                                    </Data>*/
                        IA_CODE = prsObj.parseXmlTag(Node, "IA_CODE");
                        IA_CODE = IA_CODE == null ? "" : IA_CODE;

                        CASHIERING = prsObj.parseXmlTag(Node, "CASHIERING");
                        CASHIERING = CASHIERING == null ? "" : CASHIERING;

                        FINAL_WEIGHTED_PREMIUM = prsObj.parseXmlTag(Node, "FINAL_WEIGHTED_PREMIUM");
                        FINAL_WEIGHTED_PREMIUM = FINAL_WEIGHTED_PREMIUM == null ? "" : FINAL_WEIGHTED_PREMIUM;

                        FINAL_SLAB = prsObj.parseXmlTag(Node, "FINAL_SLAB");
                        FINAL_SLAB = FINAL_SLAB == null ? "" : FINAL_SLAB;

                        SHORT_FALL = prsObj.parseXmlTag(Node, "SHORT_FALL");
                        SHORT_FALL = SHORT_FALL == null ? "" : SHORT_FALL;
                    }
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

                /*Heartiest Congratulations you have qualified in FINAL_SLAB
                slab of Cruise to Glory with FINAL_WEIGHTED_PREMIUM premium.
                For the next slab your NB shortfall is SHORT_FALL.
                        All the very best!!! * On cashiering basis. T & C Apply.*/
                String msg = "Heartiest Congratulations you have qualified in " + FINAL_SLAB
                        + " slab of Cruise to Glory with " + FINAL_WEIGHTED_PREMIUM
                        + " premium. For the next slab your NB shortfall is " + SHORT_FALL
                        + " . All the very best!!! \n* On cashiering basis. T & C Apply.";
                if (flag == 1) {

                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                    LayoutInflater inflater = ((AppCompatActivity) mContext).getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialog_with_ok_button, null);
                    builder1.setView(dialogView);
                    TextView text = dialogView.findViewById(R.id.tv_title);
                    text.setText(msg);
                    //text.setMovementMethod(LinkMovementMethod.getInstance());
                    Button dialogButton = dialogView.findViewById(R.id.bt_ok);
                    dialogButton.setText("Ok");
                    final AlertDialog dialog = builder1.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    //dialog.setContentView(R.layout.dialog_with_ok_button);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            dialog.dismiss();
                            displayCruiseMsg();
                        }
                    });
                    dialog.show();


                } else {
                    displayCruiseMsg();
}
            } else {
                displayCruiseMsg();
            }
        }
    }

    private void displayCruiseMsg(){
        String appMsgStatus = "Dear Life Mitra,\n" +
                "\n" +"Are you ready for CRUISE TO GLORY JAFFNA – SRI LANKA CAMPAIGN?? " +
                "Leverage this opportunity to qualify for a cruise to Jaffna, Sri Lanka. T&C applicable.\n" +
                "\n" +"Looking forward to your overwhelming participation. Wishing you " +
                "the very best. – Retail Agency";

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = ((AppCompatActivity) mContext).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_with_ok_button, null);
        builder1.setView(dialogView);
        TextView text = dialogView.findViewById(R.id.tv_title);
        text.setText(appMsgStatus);
        //text.setMovementMethod(LinkMovementMethod.getInstance());
        Button dialogButton = dialogView.findViewById(R.id.bt_ok);
        dialogButton.setText("Ok");
        final AlertDialog dialog = builder1.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.setContentView(R.layout.dialog_with_ok_button);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                jotcMessageAsyncTask = new JOTCMessageAsyncTask();
                jotcMessageAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);}
        });
        dialog.show();
    }


}