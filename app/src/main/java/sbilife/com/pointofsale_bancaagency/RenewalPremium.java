package sbilife.com.pointofsale_bancaagency;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
@SuppressWarnings("deprecation")
public class RenewalPremium extends TabActivity implements OnTabChangeListener{
	
	ViewPager viewPager = null;
	
	private Context context;
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_banca_agency_tab);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
                     
        context = this;
       /* final ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        
        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.window_title, null);
        actionBar.setCustomView(v);*/
        
       
		//new CommonMethods().setApplicationToolbarMenu(this,"Renewal Premium");
        
        /*
         * View Pager
         */
        /*viewPager = (ViewPager) findViewById(R.id.pager_renewal_premium);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener((new ViewPager.OnPageChangeListener() {
			
			public void onPageSelected(int i) {
				actionBar.setSelectedNavigationItem(i);	
				
				//RenewalPremiumAddFragment addFragment = new RenewalPremiumAddFragment();
				//((RenewalPremiumAddFragment)addFragment).et_rene_paymnt_policy_no.setError(""); 
			}
			
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub				
			}
			
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub				
			}
		}));
		
		 actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        ActionBar.Tab rene_pre_add = actionBar.newTab();
        rene_pre_add.setText("Add");
        rene_pre_add.setTabListener(this);
        
        ActionBar.Tab rene_pre_view = actionBar.newTab();
        rene_pre_view.setText("View");
        rene_pre_view.setTabListener(this);
        
        actionBar.addTab(rene_pre_add);
        actionBar.addTab(rene_pre_view);
        
        Intent i = getIntent();
        
        //Checking the intent value to determine which tab to show
        if(i.getBooleanExtra("edit", false)){
        	actionBar.setSelectedNavigationItem(1);
            viewPager.setCurrentItem(1);
        }*/
        
        TabHost host;
        host = getTabHost();	
		host.setOnTabChangedListener(this);
		
        TabSpec channelspec = host.newTabSpec("Add");
		View channelView = createTabView(this,0,"Add");
		channelspec.setIndicator(channelView);
		Intent chnnelIntent = new Intent(this, RenewalPremiumAddActivity.class);
		channelspec.setContent(chnnelIntent);
		
		
		TabSpec proposalspec = host.newTabSpec("View");
		View proposalView = createTabView(this,0,"View");
		proposalspec.setIndicator(proposalView);
		Intent propsalIntent = new Intent(this, RenewalPremiumViewActivity.class);
		proposalspec.setContent(propsalIntent);
		
		host.addTab(channelspec); 
		host.addTab(proposalspec); 
	}
	private View createTabView(final Context context, int id,String text)
	{
		View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
		TextView tabText = view.findViewById(R.id.tabText);
		tabText.setText(text);
		return view;
	}

    	@Override
    	public void onTabChanged(String tabId) {
    		
    	}
}