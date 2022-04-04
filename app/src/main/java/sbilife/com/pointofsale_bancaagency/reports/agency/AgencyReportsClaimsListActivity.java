package sbilife.com.pointofsale_bancaagency.reports.agency;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ReportsDynamicListingAdapter;

public class AgencyReportsClaimsListActivity extends AppCompatActivity implements
		OnItemClickListener {

    private final ArrayList<String> listMenu = new ArrayList<>();

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.reports_dynamic_menu_listing);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		initialise();
		new CommonMethods().setApplicationToolbarMenu(this,"Claims List"); ;
	}

	private void initialise() {
        Context context = this;
		
		listMenu.clear();
		listMenu.add("Survival");
		listMenu.add("Legal");

        ListView lvMenuListing = findViewById(R.id.lvMenuListing);
        ReportsDynamicListingAdapter adapterMenuList = new ReportsDynamicListingAdapter(listMenu, context);
		lvMenuListing.setAdapter(adapterMenuList);
		
		lvMenuListing.setOnItemClickListener(this);
	}

	private void claimsSurvivalReport() {
		Toast.makeText(this, "Survival Report", Toast.LENGTH_LONG).show();
	}

	private void claimsLegal() {
		Toast.makeText(this, "Legal", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:
			claimsSurvivalReport();
			break;
		case 1:
			claimsLegal();
			break;
		default:
			break;
		}
	}}

