package sbilife.com.pointofsale_bancaagency.reports;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TableRow;

import sbilife.com.pointofsale_bancaagency.BancaLinksActivity;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.home.CarouselHomeActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.DownloadPPCActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.ReportsUnderwritingActivity;

public class ServicingHomeGroupingActivity extends AppCompatActivity implements OnClickListener {
    private CommonMethods commonMethods;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.servicing_activity);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Servicing");


        LinearLayout ll_ServicingNewBusiness = findViewById(R.id.ll_ServicingNewBusiness);
        LinearLayout ll_ServicingHomePolicyServicing = findViewById(R.id.ll_ServicingHomePolicyServicing);
        LinearLayout ll_ServicingLinks = findViewById(R.id.ll_ServicingLinks);
        LinearLayout ll_ServicingClaims = findViewById(R.id.ll_ServicingClaims);
        LinearLayout ll_ServicingUnderwriting = findViewById(R.id.ll_ServicingUnderwriting);
        LinearLayout linearLayoutGetPPC = findViewById(R.id.linearLayoutGetPPC);
        LinearLayout linearLayoutPPCDownload = findViewById(R.id.linearLayoutPPCDownload);

        //TableRow tableRowPPCDownload = findViewById(R.id.tableRowPPCDownload);
        //tableRowPPCDownload.setVisibility(View.VISIBLE);

		String strUserType = commonMethods.GetUserType(context);
		if (strUserType.contentEquals("ZAM")
				||strUserType.contentEquals("AM")||strUserType.contentEquals("SAM")) {
			linearLayoutGetPPC.setVisibility(View.INVISIBLE);
            linearLayoutPPCDownload.setVisibility(View.INVISIBLE);
		}

        if (commonMethods.GetUserType(context).equalsIgnoreCase("BDM")) {

            LinearLayout linearLayoutGruoping = findViewById(R.id.linearLayoutGruoping);
            TableRow tablerowGroupMenu = findViewById(R.id.tablerowGroupMenu);
            tablerowGroupMenu.setVisibility(View.VISIBLE);

            //tableRowPPCDownload.setVisibility(View.GONE);

            linearLayoutGruoping.setOnClickListener(this);
        }

        ll_ServicingNewBusiness.setOnClickListener(this);
        ll_ServicingHomePolicyServicing.setOnClickListener(this);
        ll_ServicingLinks.setOnClickListener(this);
        ll_ServicingClaims.setOnClickListener(this);
        ll_ServicingUnderwriting.setOnClickListener(this);
        linearLayoutGetPPC.setOnClickListener(this);
        linearLayoutPPCDownload.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {

            case R.id.ll_ServicingHomePolicyServicing:
                commonMethods.callActivity(context,ReportsPolicyServicingListActivity.class);
                break;
            case R.id.ll_ServicingLinks:
                commonMethods.callActivity(context,BancaLinksActivity.class);
                break;
            case R.id.ll_ServicingNewBusiness:
                commonMethods.callActivity(context,BancaReportsNewBusinessActivity.class);
                break;
            case R.id.ll_ServicingClaims:
                commonMethods.callActivity(context,ReportsClaimsListActivity.class);
                break;
            case R.id.ll_ServicingUnderwriting:
                commonMethods.callActivity(context, ReportsUnderwritingActivity.class);
                break;
            case R.id.linearLayoutGetPPC:
                commonMethods.callActivity(context,BancaReportsPPCActivity.class);
                break;

            case R.id.linearLayoutGruoping:
                commonMethods.callActivity(context, BancaReportsGroupingActivity.class);
                break;

            case R.id.linearLayoutPPCDownload:
                commonMethods.callActivity(context,DownloadPPCActivity.class);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ServicingHomeGroupingActivity.this, CarouselHomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}
