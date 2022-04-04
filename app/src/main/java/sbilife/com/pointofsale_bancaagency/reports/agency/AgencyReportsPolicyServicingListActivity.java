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
import sbilife.com.pointofsale_bancaagency.RenewalPremium;
import sbilife.com.pointofsale_bancaagency.reports.BancaReportsCIFListActivity;
import sbilife.com.pointofsale_bancaagency.reports.BancaReportsRenewalActivity;
import sbilife.com.pointofsale_bancaagency.reports.BancaReportsRevivalActivity;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.AlternetModeCollectionStatusActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.AutoMandateRegistrationStatusListReportsActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.RevivalCampaignReportList;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.ServicingListAdapter;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.ServicingListValuesModel;
import sbilife.com.pointofsale_bancaagency.reports.policyservicing.PolicyServicingExplainerVidActivity;
import sbilife.com.pointofsale_bancaagency.reports.policyservicing.SendMandateLinkActivity;
import sbilife.com.pointofsale_bancaagency.reports.policyservicing.UnclaimedDataActivity;
import sbilife.com.pointofsale_bancaagency.reports.policyservicing.UnrealizedDataActivity;
import sbilife.com.pointofsale_bancaagency.reports.policyservicing.servicingreqmodule.ServicingRequestModuleActivity;
import sbilife.com.pointofsale_bancaagency.reports.usersreports.BancaReportsBDMUMListActivity;
import sbilife.com.pointofsale_bancaagency.reports.usersreports.ReportsAMBSMListActivity;
import sbilife.com.pointofsale_bancaagency.reports.usersreports.ReportsASMListActivity;

public class AgencyReportsPolicyServicingListActivity extends AppCompatActivity {

    private CommonMethods commonMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.reports_dynamic_recyclerview_menu_listing);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Policy Servicing");
        initialise();
    }

    private void initialise() {
        Context context = this;

        String strUserType = commonMethods.GetUserType(context);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        // set LayoutManager to RecyclerView
        recyclerView.setLayoutManager(linearLayoutManager);
        ArrayList<ServicingListValuesModel> listMenu = new ArrayList<>();
        listMenu.clear();
        if (strUserType.contentEquals("UM")) {
            listMenu.add(new ServicingListValuesModel("Agent Listing", BancaReportsCIFListActivity.class, true, false));
            listMenu.add(new ServicingListValuesModel("Mandate Registration Status", AgencyReportsMandateRegistrationStatusActivity.class, false, false));
            listMenu.add(new ServicingListValuesModel("Persistency", AgencyReportsPersistency.class, true, false));
            listMenu.add(new ServicingListValuesModel("Auto Mandate Status List", AutoMandateRegistrationStatusListReportsActivity.class, false, false));
            listMenu.add(new ServicingListValuesModel("Revival Campaign", RevivalCampaignReportList.class, false, false));
        } else if (strUserType.equalsIgnoreCase("AGENT")) {
            listMenu.add(new ServicingListValuesModel("Policy List", AgencyReportsPolicyListActivity.class, false, false));
            listMenu.add(new ServicingListValuesModel("Renewal Due List", BancaReportsRenewalActivity.class, false, false));
            listMenu.add(new ServicingListValuesModel("Surrender", AgencyReportsSurrenderActivity.class, false, false));
            listMenu.add(new ServicingListValuesModel("Revival", BancaReportsRevivalActivity.class, false, false));
            listMenu.add(new ServicingListValuesModel("Help Desk", AgencyReportsPolicyDetailsActivity.class, false, false));
            listMenu.add(new ServicingListValuesModel("Commission", AgencyReportsCommissionActivity.class, false, false));
            listMenu.add(new ServicingListValuesModel("Mandate Registration Status", AgencyReportsMandateRegistrationStatusActivity.class, false, false));

            listMenu.add(new ServicingListValuesModel("Renewal Premium", RenewalPremium.class, false, false));
            listMenu.add(new ServicingListValuesModel("Persistency", AgencyReportsPersistency.class, true, false));
            listMenu.add(new ServicingListValuesModel("Auto Mandate Status List", AutoMandateRegistrationStatusListReportsActivity.class, true, false));
            listMenu.add(new ServicingListValuesModel("Revival Campaign", RevivalCampaignReportList.class, false, false));
            listMenu.add(new ServicingListValuesModel("Alternate Mode Collection Status", AlternetModeCollectionStatusActivity.class, false, false));
            listMenu.add(new ServicingListValuesModel("Unclaimed Data", UnclaimedDataActivity.class, true, false));
            listMenu.add(new ServicingListValuesModel("Unrealized Data", UnrealizedDataActivity.class, true, false));
            listMenu.add(new ServicingListValuesModel("Servicing Request Module", ServicingRequestModuleActivity.class, true, false));
        } else if (strUserType.contentEquals("BSM")) {
            listMenu.add(new ServicingListValuesModel("UM Listing", BancaReportsBDMUMListActivity.class, true, false));
            listMenu.add(new ServicingListValuesModel("Persistency", AgencyReportsPersistency.class, true, false));
        } else if (strUserType.contentEquals("DSM")) {
            listMenu.add(new ServicingListValuesModel("BSM/UM Listing", ReportsAMBSMListActivity.class, true, false));
            listMenu.add(new ServicingListValuesModel("Persistency", AgencyReportsPersistency.class, true, false));

        } else if (strUserType.contentEquals("ASM")) {
            listMenu.add(new ServicingListValuesModel("DSM Listing", ReportsASMListActivity.class, true, false));
            listMenu.add(new ServicingListValuesModel("Persistency", AgencyReportsPersistency.class, true, false));

        } else if (strUserType.contentEquals("RSM")) {
            listMenu.add(new ServicingListValuesModel("ASM Listing", ReportsAMBSMListActivity.class, true, false));
            listMenu.add(new ServicingListValuesModel("Persistency", AgencyReportsPersistency.class, true, false));

        }
        listMenu.add(new ServicingListValuesModel("Send eMandate Link", SendMandateLinkActivity.class, true, false));
        listMenu.add(new ServicingListValuesModel("Policy Servicing Explainer Video", PolicyServicingExplainerVidActivity.class, true, false));

        ServicingListAdapter servicingListAdapter = new ServicingListAdapter(listMenu, context);
        recyclerView.setAdapter(servicingListAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.invalidate();
    }

}