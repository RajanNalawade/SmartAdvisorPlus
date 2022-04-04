package sbilife.com.pointofsale_bancaagency;

/*
 * it is same as links view but it will show in independent activity, called when carousel view is there.
 * 
 */

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TableRow;
import android.widget.TextView;

import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
public class BancaLinksActivity extends AppCompatActivity {

	//TableLayout tblirda,tbllic;
	private Context context;

	// private ProgressDialog mProgressDialog;
    private CommonMethods commonMethods;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.links_activity);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        commonMethods = new CommonMethods();
        context = this;
        commonMethods.setApplicationToolbarMenu(this,"Links");
		TextView txtwebsite = findViewById(R.id.txtwebsite);
		TextView txtebandhan = findViewById(R.id.txtebandhan);
		TextView txtfaq = findViewById(R.id.txtfaq);
		TextView txtirda = findViewById(R.id.txtirda);
		TextView txtlifeinscouncil = findViewById(R.id.txtlifeinscouncil);
		TextView txtdownlaodforms = findViewById(R.id.txtdownlaodforms);

		TextView txtproposaltracker = findViewById(R.id.txtproposaltracker);
		TextView txtcustomervideo = findViewById(R.id.txtcustomervideo);

		TextView txtbranchlocator = findViewById(R.id.txtbranchlocator);
        
        /*ImageButton imgwebsite = (ImageButton)findViewById(R.id.imgwebsite);
        ImageButton imgebandhan = (ImageButton)findViewById(R.id.imgebandhan);         
        ImageButton imgefaq = (ImageButton)findViewById(R.id.imgefaq);        
        ImageButton imgirda = (ImageButton)findViewById(R.id.imgirda);
        ImageButton imglifeinscouncil = (ImageButton)findViewById(R.id.imglifeinscouncil);
        ImageButton imgdownlaodforms = (ImageButton)findViewById(R.id.imgdownlaodforms);
        
        ImageButton imgproposaltracker = (ImageButton)findViewById(R.id.imgproposaltracker);
        ImageButton imgcustomervideo = (ImageButton)findViewById(R.id.imgcustomervideo);*/

		txtwebsite.setMovementMethod(LinkMovementMethod.getInstance());
		txtebandhan.setMovementMethod(LinkMovementMethod.getInstance());
		txtfaq.setMovementMethod(LinkMovementMethod.getInstance());
		txtirda.setMovementMethod(LinkMovementMethod.getInstance());
		txtlifeinscouncil.setMovementMethod(LinkMovementMethod.getInstance());
		txtdownlaodforms.setMovementMethod(LinkMovementMethod.getInstance());

		txtproposaltracker.setMovementMethod(LinkMovementMethod.getInstance());
		txtcustomervideo.setMovementMethod(LinkMovementMethod.getInstance());

		txtbranchlocator.setMovementMethod(LinkMovementMethod.getInstance());


		TextView txtnricorner = findViewById(R.id.txtnricorner);
		TextView txtsmsbasedser = findViewById(R.id.txtsmsbasedser);
		TextView txtbonusrates = findViewById(R.id.txtbonusrates);
		TextView txtclaims = findViewById(R.id.txtclaims);
		TextView txtabridged_financ_uw_guidelines = findViewById(R.id.txtabridged_financ_uw_guidelines);

		txtnricorner.setMovementMethod(LinkMovementMethod.getInstance());
		txtsmsbasedser.setMovementMethod(LinkMovementMethod.getInstance());
		txtbonusrates.setMovementMethod(LinkMovementMethod.getInstance());
		txtclaims.setMovementMethod(LinkMovementMethod.getInstance());
		txtabridged_financ_uw_guidelines.setMovementMethod(LinkMovementMethod.getInstance());

		TableRow tableRowLifeInsuranceCouncil,tableRowIRDA;
		tableRowLifeInsuranceCouncil = findViewById(R.id.tableRowLifeInsuranceCouncil);
		tableRowIRDA = findViewById(R.id.tableRowIRDA);
		String UserType = commonMethods.GetUserType(context);

		if(UserType.contentEquals("MAN") || UserType.contentEquals("BDM") || UserType.contentEquals("CIF"))
		{
			tableRowLifeInsuranceCouncil.setVisibility(View.VISIBLE);
			tableRowIRDA.setVisibility(View.VISIBLE);
		}
		else
		{
			tableRowLifeInsuranceCouncil.setVisibility(View.GONE);
			tableRowIRDA.setVisibility(View.GONE);
		}


		txtwebsite.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				commonMethods.openWebLink(context, "http://www.sbilife.co.in/sbilife/content/home");
			}
		});

		txtebandhan.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				commonMethods.openWebLink(context, "https://sbilife.co.in/services-check-nav");
			}
		});

		txtfaq.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				//commonMethods.openWebLink(context, "http://nav.sbilife.co.in/UI/FundWiseNAV.aspx");
				commonMethods.openWebLink(context, "https://sbilife.co.in/services-check-nav");
			}
		});

		txtirda.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				commonMethods.openWebLink(context, "http://www.policyholder.gov.in");
			}
		});

		txtlifeinscouncil.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				commonMethods.openWebLink(context, "http://www.lifeinscouncil.org");
			}
		});

		txtdownlaodforms.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				commonMethods.openWebLink(context, "http://www.sbilife.co.in/sbilife/content/17_0");
			}
		});

		txtproposaltracker.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {

				commonMethods.openWebLink(context, "http://www.sbilife.co.in/sbilife/Proposaltracker/details.do");
			}
		});

		txtcustomervideo.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {

				commonMethods.openWebLink(context, "http://www.youtube.com/sbilifeinsurance");
			}
		});

		txtbranchlocator.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				commonMethods.openWebLink(context, "http://www.sbilife.co.in/sbilife/BranchLocator/BranchLocator.do");
			}
		});


		txtnricorner.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				commonMethods.openWebLink(context, "http://www.sbilife.co.in/sbilife/content/11_8373");
			}
		});

		txtsmsbasedser.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				commonMethods.openWebLink(context, "http://www.sbilife.co.in/sbilife/content/11_4041");
			}
		});

		txtbonusrates.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				commonMethods.openWebLink(context, "http://www.sbilife.co.in/sbilife/content/37_7392");
			}
		});

		txtclaims.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				commonMethods.openWebLink(context, "http://www.sbilife.co.in/sbilife/content/11_8891");
			}
		});
		txtabridged_financ_uw_guidelines.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				commonMethods.loadDriveURL("https://drive.google.com/open?id=1MDt-CNpor-zj2KhLqo9K0DRaP_JaiN3p", context);
			}
		});

	}


}