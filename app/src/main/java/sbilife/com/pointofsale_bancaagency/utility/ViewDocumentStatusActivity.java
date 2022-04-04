package sbilife.com.pointofsale_bancaagency.utility;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.GridView;

import java.util.List;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.Utility;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class ViewDocumentStatusActivity extends AppCompatActivity {
	String ProposalNumber="";
	private DatabaseHelper db;
	private Utility objUtility;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.layout_document_upload_status);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		objUtility=new Utility();
		/*ImageView img_header = (ImageView)getWindow().findViewById(R.id.header);
		img_header.setOnClickListener(new OnClickListener() {

			//@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i=new Intent(ViewDocumentStatusActivity.this, CarouselHomeActivity.class);
				startActivity(i);
			}
		});

		ImageButton img = (ImageButton)getWindow().findViewById(R.id.btn);
		img.setOnClickListener(new OnClickListener() {

			//@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				objUtility.showpopup_alert(arg0,ViewDocumentStatusActivity.this);
			}
		});    */
		




		db = new DatabaseHelper(this);
		new CommonMethods().setApplicationToolbarMenu(this,"View Documents");
		GridView gv = findViewById(R.id.gv_userdocumentinfo);

		try {
			List<M_DocumentUploadStatus> DocumentDetails = db.getDocumentsDetails(AdditionalUtilityActivity.ProposalNumber);
			DocumentUploadViewAdapter adapter = new DocumentUploadViewAdapter(
					DocumentDetails, this);
			gv.setAdapter(adapter);
//			gv.setTextFilterEnabled(true);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
