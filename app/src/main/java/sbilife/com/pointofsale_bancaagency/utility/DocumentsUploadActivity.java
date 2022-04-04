package sbilife.com.pointofsale_bancaagency.utility;

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
import sbilife.com.pointofsale_bancaagency.home.CarouselHomeActivity;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ReportsDynamicListingAdapter;

public class DocumentsUploadActivity extends AppCompatActivity implements OnItemClickListener {

    private Intent intentBundle;
    private ArrayList<String> listMenu;
    private String fromHome;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.documents_upload_layout);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        new CommonMethods().setApplicationToolbarMenu(this, "Documents Upload");
        intentBundle = getIntent();
        fromHome = intentBundle.getStringExtra("fromHome");

        listMenu = new ArrayList<>();
        initialise();
    }

    private void initialise() {
        Context context = this;

        listMenu.clear();
        listMenu.add("Upload Documents");
        listMenu.add("Upload Parivartan Documents");
        listMenu.add("Upload All Documents");
        //listMenu.add("Upload Rinraksha Parivartan Documents");
        listMenu.add("Pending Requirements - Non-Medical");

        ListView listviewDocumentUploadMenu = findViewById(R.id.listviewDocumentUploadMenu);
        ReportsDynamicListingAdapter adapterMenuList = new ReportsDynamicListingAdapter(listMenu, context);
        listviewDocumentUploadMenu.setAdapter(adapterMenuList);

        listviewDocumentUploadMenu.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

        Intent intent;

        switch (position) {
            case 0:
                intent = new Intent(getApplicationContext(), AdditionalUtilityActivity.class);
                if (fromHome != null && fromHome.equalsIgnoreCase("N")) {
                    intent.putExtra("fromHome", "N");
                    intent.putExtra("strBDMCifCOde", intentBundle.getStringExtra("strBDMCifCOde"));
                    intent.putExtra("strEmailId", intentBundle.getStringExtra("strEmailId"));
                    intent.putExtra("strMobileNo", intentBundle.getStringExtra("strMobileNo"));
                    intent.putExtra("strUserType", intentBundle.getStringExtra("strUserType"));
                }
                startActivity(intent);
                break;

            case 1:
                intent = new Intent(getApplicationContext(), ParivartanDocumentsUploadActivity.class);
                if (fromHome != null && fromHome.equalsIgnoreCase("N")) {
                    intent.putExtra("fromHome", "N");
                    intent.putExtra("strBDMCifCOde", intentBundle.getStringExtra("strBDMCifCOde"));
                    intent.putExtra("strEmailId", intentBundle.getStringExtra("strEmailId"));
                    intent.putExtra("strMobileNo", intentBundle.getStringExtra("strMobileNo"));
                    intent.putExtra("strUserType", intentBundle.getStringExtra("strUserType"));
                }
                startActivity(intent);
                break;

            case 2:
                intent = new Intent(getApplicationContext(), AllDocumentsUploadActivity.class);
                intent.putExtra("PROPOSAL_NO", "");
                startActivity(intent);
                break;

            case 3:
                intent = new Intent(getApplicationContext(), DocUploadNonMedicalPendingActivity.class);
                intent.putExtra("fromHome","M");
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {

        if (fromHome != null && fromHome.equalsIgnoreCase("N")) {
            super.onBackPressed();
        } else {
            Intent i = new Intent(DocumentsUploadActivity.this, CarouselHomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
    }
}
