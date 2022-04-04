package sbilife.com.pointofsale_bancaagency.posp_ra.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class Activity_POSP_RA_ReqDetailDashboard extends AppCompatActivity implements View.OnClickListener {

    private CommonMethods mCommonMethods;
    private Context mContext;

    private ImageButton imageButtonAgentDetails, imageButtonReqDoc;
    private TextView textviewAgentDetails, textviewReqDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_posp_ra_req_dtls_dashboard);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        mCommonMethods = new CommonMethods();
        mContext = this;

        mCommonMethods.setApplicationToolbarMenu(this, "POSP RA Dashboard");

        imageButtonAgentDetails = (ImageButton) findViewById(R.id.imageButtonAgentDetails);
        imageButtonReqDoc = (ImageButton) findViewById(R.id.imageButtonReqDoc);
        textviewAgentDetails = (TextView) findViewById(R.id.textviewAgentDetails);
        textviewReqDoc = (TextView) findViewById(R.id.textviewReqDoc);

        imageButtonAgentDetails.setOnClickListener(this);
        imageButtonReqDoc.setOnClickListener(this);
        textviewAgentDetails.setOnClickListener(this);
        textviewReqDoc.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.imageButtonAgentDetails:
                mCommonMethods.callActivity(mContext, Activity_POSP_RA_AgentDetails.class);
                break;

            case R.id.textviewAgentDetails:
                mCommonMethods.callActivity(mContext, Activity_POSP_RA_AgentDetails.class);
                break;

            case R.id.imageButtonReqDoc:
                mCommonMethods.callActivity(mContext, Activity_POSP_RA_AgentReqDoc.class);
                break;

            case R.id.textviewReqDoc:
                mCommonMethods.callActivity(mContext, Activity_POSP_RA_AgentReqDoc.class);
                break;

            default:
                break;
        }

    }
}
