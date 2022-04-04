package sbilife.com.pointofsale_bancaagency.reports;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class BancaReportsGroupingActivity extends AppCompatActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_banca_reports_grouping);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

		CommonMethods mCommonMethods = new CommonMethods();
		//GO APP menu for the groups operation dept.
		mCommonMethods.setApplicationToolbarMenu(this,"GO APP");

		LinearLayout llGroupTermInsurance = findViewById(R.id.llGroupTermInsurance);
		ImageButton ibGroupTermInsurance = findViewById(R.id.ibGroupTermInsurance);
		TextView tvGroupTermInsurance = findViewById(R.id.tvGroupTermInsurance);

		LinearLayout llGroupRequirementUpload = findViewById(R.id.llGroupRequirementUpload);
		ImageButton ib_group_requirement_upload = findViewById(R.id.ib_group_requirement_upload);
		TextView txt_group_requirement_upload = findViewById(R.id.txt_group_requirement_upload);

		LinearLayout llGroupFundBusiness = findViewById(R.id.llGroupFundBusiness);
		ImageButton ibGroupFundBusiness = findViewById(R.id.ibGroupFundBusiness);
		TextView tvGroupFundBusiness = findViewById(R.id.tvGroupFundBusiness);

		LinearLayout llFeedback = findViewById(R.id.llFeedback);
		ImageButton ibFeedback = findViewById(R.id.ibFeedback);
		TextView tvFeedback = findViewById(R.id.tvFeedback);
		
		llGroupTermInsurance.setOnClickListener(this);
		ibGroupTermInsurance.setOnClickListener(this);
		tvGroupTermInsurance.setOnClickListener(this);

		llGroupRequirementUpload.setOnClickListener(this);
		ib_group_requirement_upload.setOnClickListener(this);
		txt_group_requirement_upload.setOnClickListener(this);

		llGroupFundBusiness.setOnClickListener(this);
		ibGroupFundBusiness.setOnClickListener(this);
		tvGroupFundBusiness.setOnClickListener(this);

		llFeedback.setOnClickListener(this);
		ibFeedback.setOnClickListener(this);
		tvFeedback.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		Intent intent;
		int id = v.getId();

		switch (id) {
			case R.id.llGroupTermInsurance:
				intent = new Intent(this, GroupTermInsuranceActivity.class);
				startActivity(intent);
				break;

			case R.id.ibGroupTermInsurance:
				intent = new Intent(this, GroupTermInsuranceActivity.class);
				startActivity(intent);
				break;

			case R.id.tvGroupTermInsurance:
				intent = new Intent(this, GroupTermInsuranceActivity.class);
				startActivity(intent);
				break;

			case R.id.llGroupRequirementUpload:
				intent = new Intent(this, GroupRequirementUploadActivity.class);
				startActivity(intent);
				break;

			case R.id.ib_group_requirement_upload:
				intent = new Intent(this, GroupRequirementUploadActivity.class);
				startActivity(intent);
				break;

			case R.id.txt_group_requirement_upload:
				intent = new Intent(this, GroupRequirementUploadActivity.class);
				startActivity(intent);
				break;

			case R.id.llGroupFundBusiness:
				intent = new Intent(this, GroupFundBusinessActivity.class);
				startActivity(intent);
				break;

			case R.id.ibGroupFundBusiness:
				intent = new Intent(this, GroupFundBusinessActivity.class);
				startActivity(intent);
				break;

			case R.id.tvGroupFundBusiness:
				intent = new Intent(this, GroupFundBusinessActivity.class);
				startActivity(intent);
				break;

			case R.id.llFeedback:
				intent = new Intent(this, GroupFeedbackActivity.class);
				startActivity(intent);
				break;

			case R.id.ibFeedback:
				intent = new Intent(this, GroupFeedbackActivity.class);
				startActivity(intent);
				break;

			case R.id.tvFeedback:
				intent = new Intent(this, GroupFeedbackActivity.class);
				startActivity(intent);
				break;
		}


	}
	
}
