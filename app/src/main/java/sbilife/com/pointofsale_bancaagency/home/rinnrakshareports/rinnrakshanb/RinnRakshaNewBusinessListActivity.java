package sbilife.com.pointofsale_bancaagency.home.rinnrakshareports.rinnrakshanb;

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
import sbilife.com.pointofsale_bancaagency.reports.commonreports.ServicingListAdapter;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.ServicingListValuesModel;

public class RinnRakshaNewBusinessListActivity extends AppCompatActivity {

    private CommonMethods commonMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.reports_dynamic_recyclerview_menu_listing);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "New Business");
        initialise();
    }

    private void initialise() {
        Context context = this;
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false);
        // set LayoutManager to RecyclerView
        recyclerView.setLayoutManager(linearLayoutManager);
        ArrayList<ServicingListValuesModel> listMenu = new ArrayList<>();
        listMenu.clear();

        listMenu.add(new ServicingListValuesModel("PDR Report", RinnRakshaOutstandingActivity.class,
                true, false));
        listMenu.add(new ServicingListValuesModel("Proposal Search", RinnRakshaProposalSearchActivity.class,
                true, false));

        ServicingListAdapter servicingListAdapter = new ServicingListAdapter(listMenu, context);
        recyclerView.setAdapter(servicingListAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.invalidate();
    }
}