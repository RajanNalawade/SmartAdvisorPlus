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

public class GroupRequirementUploadActivity extends AppCompatActivity implements OnItemClickListener {

    private ArrayList<String> listMenu = new ArrayList<String>();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_group_requirement_upload);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        mContext = this;
        CommonMethods mCommonMethods = new CommonMethods();
        mCommonMethods.setApplicationToolbarMenu(this,"Group Requirement Upload"); ;

        ListView lvGroupRequirementUploadListing = findViewById(R.id.lvGroupRequirementUploadListing);

        listMenu.clear();
        listMenu.add("New Business");
        listMenu.add("Renewal List");
        listMenu.add("MJ/ML List");
        listMenu.add("Servicing List");

        ReportsDynamicListingAdapter adapterMenuList = new ReportsDynamicListingAdapter(listMenu, mContext);
        lvGroupRequirementUploadListing.setAdapter(adapterMenuList);

        lvGroupRequirementUploadListing.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent;

        switch (position) {
            case 0:
                intent = new Intent(mContext, RequireUploadNewBussinessActivity.class);
                //Requirement Upload new business
                intent.putExtra("EMAIL_TYPE", 1);
                startActivity(intent);
                break;

            case 1:
                intent = new Intent(mContext, RequireUploadPolicyNoActivity.class);
                intent.putExtra("EMAIL_TYPE", 1);
                startActivity(intent);
                break;

            case 2:
                intent = new Intent(mContext, RequireUploadPolicyNoActivity.class);
                intent.putExtra("EMAIL_TYPE", 2);
                startActivity(intent);
                break;

            case 3:
                intent = new Intent(mContext, RequireUploadPolicyNoActivity.class);
                intent.putExtra("EMAIL_TYPE", 3);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
