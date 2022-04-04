package sbilife.com.pointofsale_bancaagency.futureplanner;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import sbilife.com.pointofsale_bancaagency.CarouselWebViewActivity;
import sbilife.com.pointofsale_bancaagency.ChildPlannerListviewActivity;
import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.home.CarouselHomeActivity;

public class FuturePlannerActivity extends AppCompatActivity implements OnClickListener {

    private String CIF_Code = "";

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.future_planner_activity);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        DatabaseHelper dbhelper = new DatabaseHelper(this);
        context = this;
        new CommonMethods().setApplicationToolbarMenu(this, "Future Planner");
        // imagtbt_option=(ImageButton)findViewById(R.id.imagtbt_option);
        ImageButton imgbt_childedu_planner = findViewById(R.id.imgbt_childedu_planner);
        ImageButton imgbt_childedu_leads = findViewById(R.id.imgbt_childedu_leads);
        ImageButton imgbt_retirement_planner = findViewById(R.id.imgbt_retirement_planner);
        ImageButton imgbt_retirement_plannerleads = findViewById(R.id.imgbt_retirement_plannerleads);


        LinearLayout ll_childedu_planner = findViewById(R.id.ll_childedu_planner);
        LinearLayout ll_childedu_leads = findViewById(R.id.ll_childedu_leads);
        LinearLayout ll_retirement_planner = findViewById(R.id.ll_retirement_planner);
        LinearLayout ll_retirement_plannerleads = findViewById(R.id.ll_retirement_plannerleads);

        LinearLayout linearLayoutHLVCalculator = findViewById(R.id.linearLayoutHLVCalculator);
        ImageButton imgageButtonHLVCalculatorplanner = findViewById(R.id.imgageButtonHLVCalculatorplanner);

        LinearLayout linearLayoutHLVCalculatorleads = findViewById(R.id.linearLayoutHLVCalculatorleads);
        ImageButton imgbuttonHLVCalculatorleads = findViewById(R.id.imgbuttonHLVCalculatorleads);

        linearLayoutHLVCalculator.setOnClickListener(this);
        imgageButtonHLVCalculatorplanner.setOnClickListener(this);

        try {
            CIF_Code = SimpleCrypto.decrypt("SBIL", dbhelper.GetUserCode());
        } catch (Exception e) {
            e.printStackTrace();
        }

        imgbuttonHLVCalculatorleads.setOnClickListener(this);
        linearLayoutHLVCalculatorleads.setOnClickListener(this);

        ll_childedu_planner.setOnClickListener(this);
        imgbt_childedu_planner.setOnClickListener(this);

        imgbt_childedu_leads.setOnClickListener(this);
        ll_childedu_leads.setOnClickListener(this);

        imgbt_retirement_planner.setOnClickListener(this);
        ll_retirement_planner.setOnClickListener(this);

        imgbt_retirement_plannerleads.setOnClickListener(this);
        ll_retirement_plannerleads.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.future_planner, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(FuturePlannerActivity.this,
                CarouselHomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.linearLayoutHLVCalculator:
            case R.id.imgageButtonHLVCalculatorplanner:
                Intent intent = new Intent(FuturePlannerActivity.this,
                        HLVCalculatorWebViewActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.imgbuttonHLVCalculatorleads:
            case R.id.linearLayoutHLVCalculatorleads:
                Intent intentList = new Intent(FuturePlannerActivity.this,
                        HLVCalculatorListViewActivity.class);
                startActivity(intentList);
                finish();
                break;

            case R.id.ll_childedu_planner:
            case R.id.imgbt_childedu_planner:
                Intent intentChildPlanner = new Intent(FuturePlannerActivity.this,
                        CarouselWebViewActivity.class);
                intentChildPlanner
                        .putExtra("currentTab", "Child Education Planner");
                startActivity(intentChildPlanner);
                finish();
                break;

            case R.id.imgbt_childedu_leads:
            case R.id.ll_childedu_leads:
                try {
                    DatabaseHelper dbObj = new DatabaseHelper(
                            FuturePlannerActivity.this);
                    Intent intentChildList = new Intent(FuturePlannerActivity.this,
                            ChildPlannerListviewActivity.class);
                    intentChildList.putExtra("CIF_Code",
                            SimpleCrypto.decrypt("SBIL", dbObj.GetUserCode()));
                    intentChildList.putExtra("currentTab",
                            "Child Education Planner Leads");
                    startActivity(intentChildList);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.imgbt_retirement_planner:
            case R.id.ll_retirement_planner:

                Intent intentRetirePlanner = new Intent(FuturePlannerActivity.this,
                        RetirementPlannerWebViewActivity.class);
                intentRetirePlanner.putExtra("currentTab", "Retirement Planner");
                startActivity(intentRetirePlanner);
                finish();
                break;

            case R.id.imgbt_retirement_plannerleads:
            case R.id.ll_retirement_plannerleads:
                Intent intentRetireLeads = new Intent(FuturePlannerActivity.this,
                        RetirementPlannerListviewActivity.class);
                intentRetireLeads.putExtra("CIF_Code", CIF_Code);
                intentRetireLeads.putExtra("currentTab", "Retirement Planner Leads");
                startActivity(intentRetireLeads);
                finish();
                break;
        }
    }
}
