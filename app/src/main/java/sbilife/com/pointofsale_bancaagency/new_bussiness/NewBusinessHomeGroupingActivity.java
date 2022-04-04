package sbilife.com.pointofsale_bancaagency.new_bussiness;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods.UserDetailsValuesModel;
import sbilife.com.pointofsale_bancaagency.home.CarouselHomeActivity;
import sbilife.com.pointofsale_bancaagency.needanalysis.NeedAnalysisActivity;
import sbilife.com.pointofsale_bancaagency.needanalysis.OthersProductListActivity;
import sbilife.com.pointofsale_bancaagency.new_bussiness.pivc.NewBussinessInstaPIVCActivity;

public class NewBusinessHomeGroupingActivity extends AppCompatActivity implements OnClickListener {

    private CommonMethods mCommonMethods;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.new_business_home_grouping);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        mCommonMethods = new CommonMethods();

        mCommonMethods.setApplicationToolbarMenu(this, "New Business");

        ImageButton imgBtnnewBusinessGroupingPremiumCalculator = findViewById(R.id.imgBtnnewBusinessGroupingPremiumCalculator);
        ImageButton imgBtnnewBusinessGroupingNewBusiness = findViewById(R.id.imgBtnnewBusinessGroupingNewBusiness);
        ImageButton imgbtnNewBusinessGroupingNeedAnalysis = findViewById(R.id.imgbtnNewBusinessGroupingNeedAnalysis);
        ImageButton imgbtnNewBusinessGroupingNeedAnalysisDashboard = findViewById(R.id.imgbtnNewBusinessGroupingNeedAnalysisDashboard);
        ImageButton imageButtonServicingNeedAnalysisVideo = findViewById(R.id.imageButtonServicingNeedAnalysisVideo);
        ImageButton imageButtonNBInstaPivc = findViewById(R.id.imageButtonNBInstaPivc);
        //ImageButton imageButtonNBEkyc = (ImageButton) findViewById(R.id.imageButtonNBEkyc);
        ImageButton imgbtnNewBusinessGroupingYonoBranch = findViewById(R.id.imgbtnNewBusinessGroupingYonoBranch);

        LinearLayout ll_newBusinessGroupingPremiumCalculator = findViewById(R.id.ll_newBusinessGroupingPremiumCalculator);
        LinearLayout ll_newBusinessGroupingNewBusiness = findViewById(R.id.ll_newBusinessGroupingNewBusiness);
        LinearLayout ll_NewBusinessGroupingNeedAnalysis = findViewById(R.id.ll_NewBusinessGroupingNeedAnalysis);
        LinearLayout ll_NewBusinessGroupingNeedAnalysisDashboard = findViewById(R.id.ll_NewBusinessGroupingNeedAnalysisDashboard);
        LinearLayout ll_ServicingNeedAnalysisVideo = findViewById(R.id.ll_ServicingNeedAnalysisVideo);
        LinearLayout ll_ServicingLinks = findViewById(R.id.ll_ServicingLinks);
        //LinearLayout ll_NBEkyc = (LinearLayout) findViewById(R.id.ll_NBEkyc);
        LinearLayout ll_NB_insta_pivc = findViewById(R.id.ll_NB_insta_pivc);
        LinearLayout ll_NewBusinessGroupingYonoBranch = findViewById(R.id.ll_NewBusinessGroupingYonoBranch);
        String struserType = mCommonMethods.GetUserType(this);
        if (struserType.equalsIgnoreCase("CIF") || struserType.equalsIgnoreCase("BDM")
                || struserType.equalsIgnoreCase("AM") || struserType.equalsIgnoreCase("SAM")
                || struserType.equalsIgnoreCase("ZAM")){
            ll_NewBusinessGroupingYonoBranch.setVisibility(View.VISIBLE);
        }else{
            ll_NewBusinessGroupingYonoBranch.setVisibility(View.GONE);
        }

        imgBtnnewBusinessGroupingPremiumCalculator.setOnClickListener(this);
        imgBtnnewBusinessGroupingNewBusiness.setOnClickListener(this);
        imgbtnNewBusinessGroupingNeedAnalysis.setOnClickListener(this);
        imgbtnNewBusinessGroupingNeedAnalysisDashboard.setOnClickListener(this);
        imageButtonServicingNeedAnalysisVideo.setOnClickListener(this);
        imageButtonNBInstaPivc.setOnClickListener(this);
        //imageButtonNBEkyc.setOnClickListener(this);
        imgbtnNewBusinessGroupingYonoBranch.setOnClickListener(this);

        ll_newBusinessGroupingPremiumCalculator.setOnClickListener(this);
        ll_newBusinessGroupingNewBusiness.setOnClickListener(this);
        ll_NewBusinessGroupingNeedAnalysis.setOnClickListener(this);
        ll_NewBusinessGroupingNeedAnalysisDashboard.setOnClickListener(this);
        ll_ServicingNeedAnalysisVideo.setOnClickListener(this);
        ll_ServicingLinks.setOnClickListener(this);
        //ll_NBEkyc.setOnClickListener(this);
        ll_NB_insta_pivc.setOnClickListener(this);
        ll_NewBusinessGroupingYonoBranch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        String strCIFBDMUserId;
        CommonMethods commonMethods;
        UserDetailsValuesModel userDetailsValuesModel;
        int id = v.getId();
        switch (id) {
            case R.id.imgBtnnewBusinessGroupingPremiumCalculator:
            case R.id.ll_newBusinessGroupingPremiumCalculator:
                intent = new Intent(getApplicationContext(), CarouselProduct.class);
                startActivity(intent);
                break;
            case R.id.imgBtnnewBusinessGroupingNewBusiness:
            case R.id.ll_newBusinessGroupingNewBusiness:
                intent = new Intent(getApplicationContext(), NewBusinessFirstPremium.class);
                startActivity(intent);
                break;
            case R.id.imgbtnNewBusinessGroupingNeedAnalysis:
            case R.id.ll_NewBusinessGroupingNeedAnalysis:

                userDetailsValuesModel = mCommonMethods.setUserDetails(this);
                strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
                String strType = mCommonMethods.GetUserType(this);

                if (strType.equalsIgnoreCase("BAP") || strType.equalsIgnoreCase("CAG")
                        || strType.equalsIgnoreCase("IMF")) {
                    //for Agent/BAP/CAG/IMF
                    intent = new Intent(getApplicationContext(), OthersProductListActivity.class);
                    intent.putExtra("NaInput", "");
                    intent.putExtra("NaOutput", "");
                    intent.putExtra("URNNumber", "");
                    intent.putExtra("group_name", "");
                    startActivity(intent);
                } else {
                    intent = new Intent(getApplicationContext(), NeedAnalysisActivity.class);
                    intent.putExtra("currentTab", "Need Analysis");
                    intent.putExtra("AgentCode", strCIFBDMUserId);
                    intent.putExtra("usertype", strType);
                    startActivity(intent);
                }
                break;
            case R.id.imgbtnNewBusinessGroupingNeedAnalysisDashboard:
            case R.id.ll_NewBusinessGroupingNeedAnalysisDashboard:
                intent = new Intent(getApplicationContext(), NeedAnalysisDashboardActivity.class);
                startActivity(intent);
                break;
            case R.id.imageButtonServicingNeedAnalysisVideo:
            case R.id.ll_ServicingNeedAnalysisVideo:
                mCommonMethods.loadDriveURL("https://drive.google.com/file/d/0B5LinkXikg9QUGQ0YTlMTWxYOTA/view?usp=sharing", this);
                break;

            case R.id.ll_NB_insta_pivc:
                intent = new Intent(getApplicationContext(), NewBussinessInstaPIVCActivity.class);
                startActivity(intent);
                break;

            case R.id.imageButtonNBInstaPivc:
                intent = new Intent(getApplicationContext(), NewBussinessInstaPIVCActivity.class);
                startActivity(intent);
                break;

			/*case R.id.imageButtonNBEkyc:
			case R.id.ll_NBEkyc:
				Intent i = new Intent(getApplicationContext(), NewBusinessEkycActivity.class);
				i.putExtra("FROM", "NB");//new bussiness
				startActivity(i);
				break;*/

            case R.id.imgbtnNewBusinessGroupingYonoBranch:
            case R.id.ll_NewBusinessGroupingYonoBranch:
                intent = new Intent(getApplicationContext(), NB_YoNoBranchPortalActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(NewBusinessHomeGroupingActivity.this, CarouselHomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}
