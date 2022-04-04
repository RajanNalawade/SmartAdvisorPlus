package sbilife.com.pointofsale_bancaagency.reports.agency;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.CommonReportsProposalListActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.MedicalPendingRequirementActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.PolicyDispatchStatusActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.PremiumPaymentReceiptActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.ProposalRinnStatusActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.ServicingListAdapter;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.ServicingListValuesModel;
import sbilife.com.pointofsale_bancaagency.reports.newbusiness.BancaReportsPIWCActivity;
import sbilife.com.pointofsale_bancaagency.reports.newbusiness.InstaImageFailureCasesActivity;
import sbilife.com.pointofsale_bancaagency.reports.newbusiness.NRIPaymentLinkActivity;
import sbilife.com.pointofsale_bancaagency.reports.newbusiness.PIWCAudioCallingPendingListActivity;
import sbilife.com.pointofsale_bancaagency.reports.newbusiness.SendMHRLinkAOLActivity;
import sbilife.com.pointofsale_bancaagency.reports.newbusiness.SendSMSAlternateProcessActivity;
import sbilife.com.pointofsale_bancaagency.utility.DocUploadNonMedicalPendingActivity;

public class AgencyReportsNewBusinessActivity extends AppCompatActivity {

    private Context context;
    private final ArrayList<String> listMenu = new ArrayList<>();
    private CommonMethods commonMethods;
    private String strUserType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.reports_dynamic_recyclerview_menu_listing);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        initialise();
        commonMethods.setApplicationToolbarMenu(this, "New Business");
    }

    private void initialise() {

        listMenu.clear();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        // set LayoutManager to RecyclerView
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<ServicingListValuesModel> listMenu = new ArrayList<>();
        listMenu.clear();

        strUserType = commonMethods.GetUserType(context);
        if (strUserType.equalsIgnoreCase("Agent") || strUserType.equalsIgnoreCase("UM")) {

            listMenu.add(new ServicingListValuesModel("PIWC Status",
                    BancaReportsPIWCActivity.class,
                    true, false));
            listMenu.add(new ServicingListValuesModel("Proposal Status Tracker",
                    AgencyReportsProposalTracker.class,
                    true, false));

            listMenu.add(new ServicingListValuesModel("Proposal List",
                    CommonReportsProposalListActivity.class,
                    false, true));

            listMenu.add(new ServicingListValuesModel("Pending Requirements - Non-Medical",
                    DocUploadNonMedicalPendingActivity.class,
                    false, true));

            listMenu.add(new ServicingListValuesModel("Pending Requirements - Medical",
                    MedicalPendingRequirementActivity.class,
                    false, true));
            listMenu.add(new ServicingListValuesModel("Premium Acknowledgement Receipt",
                    PremiumPaymentReceiptActivity.class,
                    false, true));

            listMenu.add(new ServicingListValuesModel("Rinn Raksha Proposal Tracker",
                    ProposalRinnStatusActivity.class,
                    false, true));

            listMenu.add(new ServicingListValuesModel("Policy Dispatch Status",
                    PolicyDispatchStatusActivity.class,
                    false, true));

            listMenu.add(new ServicingListValuesModel("PIWC(Audio Calling) List",
                    PIWCAudioCallingPendingListActivity.class,
                    true, false));
            listMenu.add(new ServicingListValuesModel("Insta Image Verification Status",
                    InstaImageFailureCasesActivity.class,
                    true, false));
        }

        listMenu.add(new ServicingListValuesModel("Send SMS Alternate Process",
                SendSMSAlternateProcessActivity.class,
                true, false));
        listMenu.add(new ServicingListValuesModel("Send MHR Link (Agent Own Life)",
                SendMHRLinkAOLActivity.class,
                true, false));
        listMenu.add(new ServicingListValuesModel("NRI Payment Link",
                NRIPaymentLinkActivity.class,
                true, false));

        ServicingListAdapter servicingListAdapter = new ServicingListAdapter(listMenu, context);
        recyclerView.setAdapter(servicingListAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.invalidate();
    }

}
