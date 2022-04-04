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

public class GroupFundBusinessActivity extends AppCompatActivity implements OnItemClickListener{

    private ArrayList<String> listMenu = new ArrayList<String>();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_group_fund_business);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        mContext = this;
        CommonMethods mCommonMethods = new CommonMethods();
        mCommonMethods.setApplicationToolbarMenu(this,"Fund Business"); ;

        ListView lvGroupFundBusinessListing = findViewById(R.id.lvGroupFundBusinessListing);

        listMenu.clear();
        listMenu.add("New Business");
        listMenu.add("Mini Fund Statement");
        listMenu.add("NB Requirement Upload");
        listMenu.add("MJ/ML Requirement Upload");
        listMenu.add("Servicing Requirement Upload");
        listMenu.add("Fund Claim Payment");
        listMenu.add("Contribution Received");
        listMenu.add("Master Policy Search");
        listMenu.add("Member View");

        ReportsDynamicListingAdapter adapterMenuList = new ReportsDynamicListingAdapter(listMenu, mContext);
        lvGroupFundBusinessListing.setAdapter(adapterMenuList);

        lvGroupFundBusinessListing.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent;

        switch (position) {
            case 0:
                intent = new Intent(mContext, GroupInsuNewBussinessListActivity.class);
                intent.putExtra("BusinessType", "FUND");
                startActivity(intent);
                break;

            case 1:
                intent = new Intent(mContext, GroupMiniFundActivity.class);
                startActivity(intent);
                break;

            case 2:
                intent = new Intent(mContext, RequireUploadNewBussinessActivity.class);
                //Fund New Business
                intent.putExtra("EMAIL_TYPE", 2);
                startActivity(intent);
                break;

            case 3:
                intent = new Intent(mContext, RequireUploadPolicyNoActivity.class);
                //MJ/ML List Requirement Upload
                intent.putExtra("EMAIL_TYPE", 4);
                startActivity(intent);
                break;


            case 4:
                intent = new Intent(mContext, RequireUploadPolicyNoActivity.class);
                //Servicing List Requirement Upload
                intent.putExtra("EMAIL_TYPE", 5);
                startActivity(intent);
                break;

            case 5:
                intent = new Intent(mContext, GroupFundCliamActivity.class);
                startActivity(intent);
                break;

            case 6:
                intent = new Intent(mContext, GroupFundContributionReceivedActivity.class);
                startActivity(intent);
                break;

            case 7:
                intent = new Intent(mContext, GroupFundSearchPolicyNo.class);
                startActivity(intent);
                break;

            case 8:
                intent = new Intent(mContext, GroupFundSearchMemberView.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
