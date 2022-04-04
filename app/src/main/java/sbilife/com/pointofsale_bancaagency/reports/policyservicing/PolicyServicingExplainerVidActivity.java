package sbilife.com.pointofsale_bancaagency.reports.policyservicing;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class PolicyServicingExplainerVidActivity extends AppCompatActivity implements View.OnClickListener {
    private CommonMethods commonMethods;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_policy_servicing_explainer_vid);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        initialisation();
    }

    private void initialisation() {

        context = this;
        commonMethods = new CommonMethods();

        commonMethods.setApplicationToolbarMenu(this, "Product Explainer");

        TextView tvEMandate = findViewById(R.id.tvEMandate);
        TextView tvMyPolicy = findViewById(R.id.tvMyPolicy);

        tvEMandate.setOnClickListener(this);
        tvMyPolicy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tvEMandate:
                commonMethods.loadDriveURL("https://drive.google.com/file/d/1x0k0DsERCdJ6Ajj9bWzNkLSpFC8DYwul/view?usp=sharing", context);
                break;

            case R.id.tvMyPolicy:
                commonMethods.loadDriveURL("https://drive.google.com/file/d/1vpyGYkjsGCy_AbkZFjd-zoB5f0WIdDjA/view?usp=sharing", context);
                break;


        }
    }
}