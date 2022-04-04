package sbilife.com.pointofsale_bancaagency.reports;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.ArrayList;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsMaturityActivity;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsSBDueListActivity;

public class ReportsClaimsListActivity extends AppCompatActivity implements OnItemClickListener {

    private Context context;
    private CommonMethods mCommonMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.reports_dynamic_menu_listing);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        new CommonMethods().setApplicationToolbarMenu(this, "Claims");

        initialise();
    }

    private void initialise() {

        context = this;
        mCommonMethods = new CommonMethods();
        final ArrayList<String> listMenu = new ArrayList<>();
        listMenu.clear();
        listMenu.add("Maturity");
        listMenu.add("SB Due List");

        ListView lvMenuListing = findViewById(R.id.lvMenuListing);
        ReportsDynamicListingAdapter adapterMenuList = new ReportsDynamicListingAdapter(listMenu, context);
        lvMenuListing.setAdapter(adapterMenuList);

        lvMenuListing.setOnItemClickListener(this);
    }

    private void SbDueList() {
        if (mCommonMethods.GetUserType(context).equalsIgnoreCase("AGENT") || mCommonMethods.GetUserType(context).contentEquals("UM")) {
            Intent intent = new Intent(this, AgencyReportsSBDueListActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, BancaReportsSBDueListActivity.class);
            startActivity(intent);
        }
    }

    private void maturityList() {
        if (mCommonMethods.GetUserType(context).equalsIgnoreCase("AGENT")
                || mCommonMethods.GetUserType(context).contentEquals("UM")) {
            Intent i = new Intent(context, AgencyReportsMaturityActivity.class);
            startActivity(i);

        } else {
            Intent i = new Intent(context, BancaReportsMaturityActivity.class);
            startActivity(i);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

        switch (position) {
            case 0:
                maturityList();
                break;
            case 1:
                SbDueList();
                break;

            default:
                break;
        }
    }

}
