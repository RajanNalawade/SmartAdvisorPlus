package sbilife.com.pointofsale_bancaagency.reports.commonreports;

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
import sbilife.com.pointofsale_bancaagency.reports.BancaReportsAdvisorProposalsStatusActivity;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsAdvisorProposalsStatusActivity;
import sbilife.com.pointofsale_bancaagency.reports.underwriting.SendLinkActivity;
import sbilife.com.pointofsale_bancaagency.reports.underwriting.SendLinkRinnrakshaActivity;
import sbilife.com.pointofsale_bancaagency.reports.underwriting.ViewMedicalStatusTeleMERActivity;

public class ReportsUnderwritingActivity extends AppCompatActivity {

    private CommonMethods commonMethods;
    private Context context;
    private String strUserType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.reports_dynamic_recyclerview_menu_listing);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        commonMethods = new CommonMethods();

        commonMethods.setApplicationToolbarMenu(this, "Underwriting");

        initialise();
    }

    private void initialise() {

        context = this;
        strUserType = commonMethods.GetUserType(context);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        // set LayoutManager to RecyclerView
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<ServicingListValuesModel> listMenu = new ArrayList<>();
        listMenu.clear();

        if (strUserType.equalsIgnoreCase("Agent") || strUserType.contentEquals("UM")
                || strUserType.contentEquals("BSM") || strUserType.contentEquals("DSM")
                || strUserType.equalsIgnoreCase("ASM")
                || strUserType.equalsIgnoreCase("RSM")) {
            listMenu.add(new ServicingListValuesModel(getString(R.string.channelProposerTrackerLabel), AgencyReportsAdvisorProposalsStatusActivity.class,
                    true, false));
        } else {
            listMenu.add(new ServicingListValuesModel(getString(R.string.channelProposerTrackerLabel), BancaReportsAdvisorProposalsStatusActivity.class,
                    true, false));
        }
        listMenu.add(new ServicingListValuesModel("View Medical Status", ViewMedicalStatusActivity.class, true, false));
        listMenu.add(new ServicingListValuesModel("Send Link", SendLinkActivity.class, true,
                false));
        listMenu.add(new ServicingListValuesModel("View Medical TeleMER Status", ViewMedicalStatusTeleMERActivity.class, false, false));
        listMenu.add(new ServicingListValuesModel("Send Link Rinn Raksha",
                SendLinkRinnrakshaActivity.class,
                true, false));
        ServicingListAdapter servicingListAdapter = new ServicingListAdapter(listMenu, context);
        recyclerView.setAdapter(servicingListAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.invalidate();
    }

}
