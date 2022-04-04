package sbilife.com.pointofsale_bancaagency.new_bussiness;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

@SuppressWarnings("deprecation")
public class NewBusinessFirstPremium extends TabActivity implements OnTabChangeListener {


    private DatabaseHelper dbhelper;

    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_banca_agency_tab);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        // final ActionBar actionBar = getActionBar();
        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        
        
       /* LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.window_title, null);
        actionBar.setCustomView(v);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);*/

        dbhelper = new DatabaseHelper(this);
        //String username =dbhelper.GetUserName() ;
        //new CommonMethods().setApplicationToolbarMenu(this, "New Business");


        TabHost host;
        host = getTabHost();
        host.setOnTabChangedListener(this);

        TabSpec channelspec = host.newTabSpec("Add");
        View channelView = createTabView(this, 0, "Add");
        channelspec.setIndicator(channelView);
        Intent chnnelIntent = new Intent(this, NewBusinessFPAddActivity.class);
        channelspec.setContent(chnnelIntent);


        TabSpec proposalspec = host.newTabSpec("View");
        View proposalView = createTabView(this, 0, "View");
        proposalspec.setIndicator(proposalView);
        Intent propsalIntent = new Intent(this, NewBusinessFPViewActivity.class);
        proposalspec.setContent(propsalIntent);

        host.addTab(channelspec);
        host.addTab(proposalspec);

    }

    private View createTabView(final Context context, int id, String text) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        TextView tabText = view.findViewById(R.id.tabText);
        tabText.setText(text);
        return view;
    }


    @Override
    public void onTabChanged(String tabId) {

    }

}