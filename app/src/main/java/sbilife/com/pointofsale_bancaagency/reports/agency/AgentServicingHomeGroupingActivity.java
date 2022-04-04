package sbilife.com.pointofsale_bancaagency.reports.agency;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;

import sbilife.com.pointofsale_bancaagency.CarouselLinksActivity;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.home.CarouselHomeActivity;
import sbilife.com.pointofsale_bancaagency.reports.BancaReportsPPCActivity;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ReportsClaimsListActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.DownloadPPCActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.ReportsUnderwritingActivity;

public class AgentServicingHomeGroupingActivity extends AppCompatActivity implements OnClickListener{

	private CommonMethods commonMethods;
	private Context context;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.agents_servicing_home_grouping);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		commonMethods = new CommonMethods();
		commonMethods.setApplicationToolbarMenu(this,"Servicing");
	
		context = this;


        LinearLayout ll_AgentsServicingHomePolicyServicing = findViewById(R.id.ll_AgentsServicingHomePolicyServicing);
        LinearLayout ll_AgentsServicingNewBusiness = findViewById(R.id.ll_AgentsServicingNewBusiness);
        LinearLayout ll_AgentsServicingUnderwriting = findViewById(R.id.ll_AgentsServicingUnderwriting);
        LinearLayout ll_AgentsServicingClaims = findViewById(R.id.ll_AgentsServicingClaims);
        LinearLayout ll_AgentsServicingLinks = findViewById(R.id.ll_AgentsServicingLinks);

        LinearLayout linearLayoutGetPPC = findViewById(R.id.linearLayoutGetPPC);
		LinearLayout linearLayoutPPCDownload = findViewById(R.id.linearLayoutPPCDownload);

		//TableRow tableRowPPCDownload = findViewById(R.id.tableRowPPCDownload);
		//tableRowPPCDownload.setVisibility(View.VISIBLE);
		
		String strUserType = commonMethods.GetUserType(context);
		if (strUserType.contentEquals("UM")
				||strUserType.contentEquals("BSM")
				||strUserType.contentEquals("DSM")
				||strUserType.contentEquals("ASM")||strUserType.contentEquals("RSM")) {
			linearLayoutGetPPC.setVisibility(View.INVISIBLE);
			linearLayoutPPCDownload.setVisibility(View.INVISIBLE);
		}
		if (strUserType.contentEquals("UM")){
			ll_AgentsServicingNewBusiness.setVisibility(View.VISIBLE);
			linearLayoutPPCDownload.setVisibility(View.VISIBLE);
		}
		
		ll_AgentsServicingHomePolicyServicing.setOnClickListener(this);
		ll_AgentsServicingNewBusiness.setOnClickListener(this);
		ll_AgentsServicingUnderwriting.setOnClickListener(this);
		ll_AgentsServicingClaims.setOnClickListener(this);
		ll_AgentsServicingLinks.setOnClickListener(this);
		linearLayoutGetPPC.setOnClickListener(this);
		linearLayoutPPCDownload.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.ll_AgentsServicingHomePolicyServicing:
				commonMethods.callActivity(context, AgencyReportsPolicyServicingListActivity.class);

				break;
			case R.id.ll_AgentsServicingLinks:
				commonMethods.callActivity(context,  CarouselLinksActivity.class);

				break;
			case R.id.ll_AgentsServicingNewBusiness:
				commonMethods.callActivity(context,  AgencyReportsNewBusinessActivity.class);

				break;
			case R.id.ll_AgentsServicingClaims:
				commonMethods.callActivity(context, ReportsClaimsListActivity.class);

				break;
			case R.id.ll_AgentsServicingUnderwriting:
				commonMethods.callActivity(context, ReportsUnderwritingActivity.class);
				break;
			case R.id.linearLayoutGetPPC:
				commonMethods.callActivity(context, BancaReportsPPCActivity.class);

				break;
			case R.id.linearLayoutPPCDownload:
				commonMethods.callActivity(context,  DownloadPPCActivity.class);

				break;
		}
		
	}
	
	@Override
    public void onBackPressed() {
			Intent i = new Intent(AgentServicingHomeGroupingActivity.this, CarouselHomeActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(i);
			finish();
		}
}

