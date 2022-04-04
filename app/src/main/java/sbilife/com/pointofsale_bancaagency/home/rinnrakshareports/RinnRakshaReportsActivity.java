package sbilife.com.pointofsale_bancaagency.home.rinnrakshareports;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.home.rinnrakshareports.rinnrakshanb.RinnRakshaNewBusinessListActivity;

public class RinnRakshaReportsActivity extends AppCompatActivity implements View.OnClickListener {

    private CommonMethods commonMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_rinn_raksha_reports);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Rinn Raksha - Reports & Notification");
        LinearLayout ll_newBusinessRinRikshaReports = findViewById(R.id.ll_newBusinessRinRikshaReports);
        ll_newBusinessRinRikshaReports.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_newBusinessRinRikshaReports:
                intent = new Intent(this, RinnRakshaNewBusinessListActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}