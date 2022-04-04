package sbilife.com.pointofsale_bancaagency.cifenrollment.phase1;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class CIFEnrollmentDashboardActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

	private SearchView search_view;
	private DatabaseHelper db;

    private GridView gv;
	private UserInformationAdapter adapter;
	private List<M_UserInformation> DashboardDetails = new ArrayList<>();
    private String str_pf_number="";
    private String agentId = "";
	private Boolean isFlag1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.cifenrollment_layout_cif_dashboard);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.cifenrollment_dashboard_title);

		initiatePopupWindow();
		db = new DatabaseHelper(this);
        ParseXML prsObj = new ParseXML();

        new CommonMethods().setApplicationToolbarMenu1(this, "Dashboard");

		agentId = "demouser";

		try {

			DashboardDetails = db.getDashboardDetails(agentId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		adapter = new UserInformationAdapter(DashboardDetails, this);
		gv.setAdapter(adapter);
		registerForContextMenu(gv);
		gv.setTextFilterEnabled(true);
		//gv.setOnItemClickListener(this);

		search_view = findViewById(R.id.search_view);
		setupSearchView();
	}

	private void setupSearchView() {
		search_view.setIconifiedByDefault(false);
		search_view.setOnQueryTextListener(CIFEnrollmentDashboardActivity.this);
		search_view.setSubmitButtonEnabled(true);
		search_view.setQueryHint("Search");
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if (TextUtils.isEmpty(newText)) {
			try {

				DashboardDetails = db.getDashboardDetails(agentId);
				adapter = new UserInformationAdapter(DashboardDetails, this);
				gv.setAdapter(adapter);
				registerForContextMenu(gv);
				gv.setTextFilterEnabled(true);
				//gv.setOnItemClickListener(this);
				gv.clearTextFilter();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			search(newText);
		}
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}

	private void initiatePopupWindow() {
		gv = findViewById(R.id.gv_userinfo);

		LinearLayout popup_form = findViewById(R.id.btn_test);
		popup_form.setVisibility(View.VISIBLE);

		Animation animShow = AnimationUtils.loadAnimation(this, R.anim.slide_in_up);

		popup_form.startAnimation(animShow);

	}

	/*@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		// Create your context menu here
		menu.setHeaderTitle("Proceed");
		AdapterView.AdapterContextMenuInfo info;
		info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		str_quotation = DashboardDetails.get(info.position).getStr_quotation();
		str_pf_number = DashboardDetails.get(info.position).getStr_pf_number();

		isFlag1 = DashboardDetails.get(info.position).getIsFlag1();
		menu.add(0, v.getId(), 0, "Proceed");
		// menu.add(1, v.getId(), 0, "Delete");

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Intent i;

		if (item.getTitle() == "Proceed") {

			i = new Intent(CIFEnrollmentDashboardActivity.this, CIFEnrollmentMainActivity.class);
			i.putExtra("Quotation_number", str_quotation);
			i.putExtra("PF_Number", str_pf_number);
			String str_isflag1 = "";
			if (isFlag1 == true) {
				str_isflag1 = "true";
			} else {
				str_isflag1 = "false";
			}
			i.putExtra("isFlag1", str_isflag1);
			i.putExtra("Dashboard", "true");
			startActivity(i);

		} else if (item.getTitle() == "Delete") {
			DeleteProposalAlert();
		}
		return false;

	}*/

	/*@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		String str_quotation = DashboardDetails.get(position)
				.getStr_quotation();
		// ProductHomePageActivity.quotation_Number = quotation_Number;
		// pendingResult = DashboardDetails.get(position).getSatus();
	}*/

	public void DeleteProposalAlert() {
		Builder builder = new Builder(this,
				AlertDialog.THEME_HOLO_LIGHT);
		builder.setTitle("Alert");
		builder.setMessage("Are you sure want to delete this Proposal?");
		builder.setCancelable(false);
		builder.setPositiveButton("Yes",
				new DeleteProposalAlertOkOnClickListener());
		builder.setNegativeButton("No",
				new DeleteProposalAlertCancelOnClickListener());
		AlertDialog dialog = builder.create();
		dialog.show();

	}

	private final class DeleteProposalAlertOkOnClickListener implements
			DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			try {
                String str_quotation = "";
                long rowId = db.DeleteProposalFromDashboard(str_quotation);

				if (rowId > 0) {

					Toast.makeText(getApplicationContext(),
							"Proposal  Deleted Successfully !!",
							Toast.LENGTH_LONG).show();

				}

				DashboardDetails = db.getDashboardDetails(agentId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			adapter = new UserInformationAdapter(DashboardDetails,
					CIFEnrollmentDashboardActivity.this);
			gv.setAdapter(adapter);
			registerForContextMenu(gv);
			gv.setTextFilterEnabled(true);
			//gv.setOnItemClickListener(CIFEnrollmentDashboardActivity.this);

		}
	}

	private final class DeleteProposalAlertCancelOnClickListener implements
			DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(CIFEnrollmentDashboardActivity.this, CIFEnrollmentPFActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		try {
			DashboardDetails = db.getDashboardDetails(agentId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		adapter = new UserInformationAdapter(DashboardDetails, this);
		gv.setAdapter(adapter);
		registerForContextMenu(gv);
		gv.setTextFilterEnabled(true);
		//gv.setOnItemClickListener(this);
		super.onResume();
	}

	private void search(String like) {

		if (like.length() > 0 && like.startsWith(" ")) {

		} else {

			try {
				DashboardDetails = db
						.getDashboardDetailsByQuotNo(like, agentId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			adapter = new UserInformationAdapter(DashboardDetails, this);
			gv.setAdapter(adapter);
			registerForContextMenu(gv);
			//gv.setOnItemClickListener(this);
		}

	}

}
