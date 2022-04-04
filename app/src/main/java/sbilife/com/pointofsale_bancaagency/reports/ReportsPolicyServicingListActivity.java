package sbilife.com.pointofsale_bancaagency.reports;

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
import sbilife.com.pointofsale_bancaagency.RenewalPremium;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsPersistency;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.ActivitySPList;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.AlternetModeCollectionStatusActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.AutoMandateRegistrationStatusListReportsActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.BranchWisePersistencyActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.BranchwiseRenewalListActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.RevivalCampaignReportList;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.ServicingListAdapter;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.ServicingListValuesModel;
import sbilife.com.pointofsale_bancaagency.reports.policyservicing.IAChannelPersistencyActivity;
import sbilife.com.pointofsale_bancaagency.reports.policyservicing.PolicyServicingExplainerVidActivity;
import sbilife.com.pointofsale_bancaagency.reports.policyservicing.SendMandateLinkActivity;
import sbilife.com.pointofsale_bancaagency.reports.policyservicing.UnclaimedDataActivity;
import sbilife.com.pointofsale_bancaagency.reports.policyservicing.UnrealizedDataActivity;
import sbilife.com.pointofsale_bancaagency.reports.policyservicing.servicingreqmodule.ServicingRequestModuleActivity;
import sbilife.com.pointofsale_bancaagency.reports.usersreports.BancaReportsBDMUMListActivity;
import sbilife.com.pointofsale_bancaagency.reports.usersreports.ReportsAMBSMListActivity;
import sbilife.com.pointofsale_bancaagency.reports.usersreports.ReportsSAMDSMListActivity;

public class ReportsPolicyServicingListActivity extends AppCompatActivity {

    private Context context;
    private CommonMethods mCommonMethods;
    private String strUserType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.reports_dynamic_recyclerview_menu_listing);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        initialise();
        mCommonMethods.setApplicationToolbarMenu(this, "Policy Servicing");
    }

    private void initialise() {

        context = this;
        mCommonMethods = new CommonMethods();
        strUserType = mCommonMethods.GetUserType(context);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        // set LayoutManager to RecyclerView
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<ServicingListValuesModel> listMenu = new ArrayList<>();
        listMenu.clear();
        if (strUserType.contentEquals("BDM")) {

            listMenu.add(new ServicingListValuesModel("CIF Listing", BancaReportsCIFListActivity.class, true, false));
            listMenu.add(new ServicingListValuesModel("Mandate Registration Status", BancaReportsMandateRegistrationStatusActivity.class, false, false));
            listMenu.add(new ServicingListValuesModel("Persistency", AgencyReportsPersistency.class, true, false));
            listMenu.add(new ServicingListValuesModel("Auto Mandate Status List", AutoMandateRegistrationStatusListReportsActivity.class, false, false));
            listMenu.add(new ServicingListValuesModel("Revival Campaign", RevivalCampaignReportList.class, false, false));
           // listMenu.add(new ServicingListValuesModel("Revival Campaign Polices", RevivalCampaignPoliciesListActivity.class, false, false));
            listMenu.add(new ServicingListValuesModel("Branchwise Renewal List", BranchwiseRenewalListActivity.class, true, false));
            listMenu.add(new ServicingListValuesModel("SP List", ActivitySPList.class, true, false));
            listMenu.add(new ServicingListValuesModel("Branch Wise Persistency", BranchWisePersistencyActivity.class, true, false));
            listMenu.add(new ServicingListValuesModel("Persistency For IA Channel", IAChannelPersistencyActivity.class, true, false));
        } else if (strUserType.contentEquals("CIF")) {
            listMenu.add(new ServicingListValuesModel("Policy List", BancaReportsPolicyListActivity.class, false, false));
            listMenu.add(new ServicingListValuesModel("Renewal Due List", BancaReportsRenewalActivity.class, false, false));
            listMenu.add(new ServicingListValuesModel("Surrender", BancaReportsSurrenderActivity.class, false, false));
            listMenu.add(new ServicingListValuesModel("Revival", BancaReportsRevivalActivity.class, false, false));
            listMenu.add(new ServicingListValuesModel("Help Desk", BancaReportsPolicyDetailsActivity.class, false, false));
            listMenu.add(new ServicingListValuesModel("Mandate Registration Status", BancaReportsMandateRegistrationStatusActivity.class, false, false));
            listMenu.add(new ServicingListValuesModel("Renewal Premium", RenewalPremium.class, false, false));
            listMenu.add(new ServicingListValuesModel("Persistency", AgencyReportsPersistency.class, true, false));
            listMenu.add(new ServicingListValuesModel("Auto Mandate Status List", AutoMandateRegistrationStatusListReportsActivity.class, true, false));
            listMenu.add(new ServicingListValuesModel("Revival Campaign", RevivalCampaignReportList.class, false, false));
            listMenu.add(new ServicingListValuesModel("Alternate Mode Collection Status", AlternetModeCollectionStatusActivity.class, false, false));
            listMenu.add(new ServicingListValuesModel("Unclaimed Data", UnclaimedDataActivity.class, true, false));
            listMenu.add(new ServicingListValuesModel("Unrealized Data", UnrealizedDataActivity.class, true, false));
            listMenu.add(new ServicingListValuesModel("Servicing Request Module", ServicingRequestModuleActivity.class, true, false));
        } else if (strUserType.contentEquals("AM")) {
            listMenu.add(new ServicingListValuesModel("BDM Listing", BancaReportsBDMUMListActivity.class, true, false));
            listMenu.add(new ServicingListValuesModel("Persistency", AgencyReportsPersistency.class, true, false));
        } else if (strUserType.contentEquals("SAM")) {
            listMenu.add(new ServicingListValuesModel("BDM/AM Listing", ReportsAMBSMListActivity.class, true, false));
        } else if (strUserType.contentEquals("ZAM")) {
            listMenu.add(new ServicingListValuesModel("AM/SAM/BDM Listing", ReportsSAMDSMListActivity.class, true, false));
        }

        listMenu.add(new ServicingListValuesModel("Send eMandate Link", SendMandateLinkActivity.class, true, false));
        listMenu.add(new ServicingListValuesModel("Policy Servicing Explainer Video", PolicyServicingExplainerVidActivity.class, true, false));
        //listMenu.add(new ServicingListValuesModel("Servicing Request Dashboard", ServiceRequestDashboardActivity.class, true, false));
        ServicingListAdapter servicingListAdapter = new ServicingListAdapter(listMenu, context);
        recyclerView.setAdapter(servicingListAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.invalidate();
    }

}
