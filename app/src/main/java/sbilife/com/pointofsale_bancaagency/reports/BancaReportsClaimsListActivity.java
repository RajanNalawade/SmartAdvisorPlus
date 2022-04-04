package sbilife.com.pointofsale_bancaagency.reports;

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

public class BancaReportsClaimsListActivity extends AppCompatActivity implements OnItemClickListener{



    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout. reports_dynamic_menu_listing);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

		new CommonMethods().setApplicationToolbarMenu(this,"Claims List"); ;

		initialise_listing();
	}

	private void initialise_listing() {
		final ArrayList<String> listMenu = new ArrayList<>();
		listMenu.clear();
		listMenu.add("Survival");
		listMenu.add("Legal");

        Context context = this;

        ListView lvMenuListing = findViewById(R.id.lvMenuListing);
        ReportsDynamicListingAdapter adapterMenuList = new ReportsDynamicListingAdapter(listMenu, context);
		lvMenuListing.setAdapter(adapterMenuList);

		lvMenuListing.setOnItemClickListener(this);
	}

	private void claimsSurvivalReport() {
		/*Intent i = new Intent(context,Bancareportsc.class);
    	startActivity(i);*/
		Toast.makeText(this, "Survival Report", Toast.LENGTH_LONG).show();
	}

	private void claimsLegal() {
		Toast.makeText(this, "Legal", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
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
	}
}
