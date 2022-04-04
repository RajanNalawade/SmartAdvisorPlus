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

 public class GroupTermInsuranceActivity extends AppCompatActivity implements OnItemClickListener{

     private ArrayList<String> listMenu = new ArrayList<String>();
     private Context mContext;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
         setContentView(R.layout.activity_group_term_insurance);
         //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

         mContext = this;
         CommonMethods mCommonMethods = new CommonMethods();
         mCommonMethods.setApplicationToolbarMenu(this,"Group Term Insurance"); ;

         ListView lvGroupTermInsuranceListing = findViewById(R.id.lvGroupTermInsuranceListing);

         listMenu.clear();
         listMenu.add("New Business");
         listMenu.add("Renewal List");
         listMenu.add("Lapsation List");
         listMenu.add("CD Statement");
         listMenu.add("Quote ID Search");
         listMenu.add("Master Policy List");
         listMenu.add("Member View");
         listMenu.add("Claim View");
         listMenu.add("Under Writting");

         ReportsDynamicListingAdapter adapterMenuList = new ReportsDynamicListingAdapter(listMenu, mContext);
         lvGroupTermInsuranceListing.setAdapter(adapterMenuList);

         lvGroupTermInsuranceListing.setOnItemClickListener(this);
     }

     @Override
     public void onItemClick(AdapterView<?> parent, View view, int position,
             long id) {
         // TODO Auto-generated method stub

         Intent intent;

         switch (position) {
         case 0:
             intent = new Intent(mContext, GroupInsuNewBussinessListActivity.class);
             intent.putExtra("BusinessType", "TERM_INSURANCE");
             startActivity(intent);
             break;

         case 1:
             intent = new Intent(mContext, GroupTermRenewalActivity.class);
             startActivity(intent);
             break;

         case 2:
             intent = new Intent(mContext, GroupTermLapseActivity.class);
             startActivity(intent);
             break;

         case 3:
             intent = new Intent(mContext, GroupTermCDStatementActivity.class);
             startActivity(intent);
             break;

         case 4:
             intent = new Intent(mContext, GroupTermInsuQuoteSearchActivity.class);
             startActivity(intent);
             break;

         case 5:
             intent = new Intent(mContext, GroupSearchPolicyNo.class);
             startActivity(intent);
             break;

         case 6:
             intent = new Intent(mContext, GroupSearchMemberView.class);
             startActivity(intent);
             break;

         case 7:
             intent = new Intent(mContext, GroupSearchClaimView.class);
             startActivity(intent);
             break;

         case 8:
             intent = new Intent(mContext, UnderwrittingActivity.class);
             startActivity(intent);
             break;

         default:
             break;
         }
     }
 }
