package sbilife.com.pointofsale_bancaagency.agent_on_boarding;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.agent_on_boarding.dashboard.ActivityAOBAgentDetails;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.home.home_menu_adapter.HomeMenu;
import sbilife.com.pointofsale_bancaagency.home.home_menu_adapter.HomeMenuAdapter;
import sbilife.com.pointofsale_bancaagency.posp_ra.Activity_POSP_RA_Authentication;
import sbilife.com.pointofsale_bancaagency.posp_ra.dashboard.Activity_POSP_RA_AgentDetails;

public class ActivityAOB_Menu extends AppCompatActivity {

    private final ArrayList<HomeMenu> listMenuItem = new ArrayList<>();
    private Context mContext;
    private CommonMethods mCommonMethods;
    private RecyclerView listAOBMenu;
    private String strType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aob_menu);

        mContext = this;
        mCommonMethods = new CommonMethods();

        strType = getIntent().getExtras().getString("STRING_DATA");
        strType = strType == null ? "" : strType;

        listAOBMenu = findViewById(R.id.listAOBMenu);

        getUserTypeMenuList();

        // set LayoutManager to RecyclerView
        listAOBMenu.setLayoutManager(new GridLayoutManager(ActivityAOB_Menu.this, 2));

        // call the constructor of CustomAdapter to send the reference and data to Adapter
        HomeMenuAdapter mHomeMenuAdapter = new HomeMenuAdapter(mContext, new HomeMenuAdapter.AdapterDiffUtil());
        mHomeMenuAdapter.submitList(listMenuItem);
        listAOBMenu.setAdapter(mHomeMenuAdapter);
        listAOBMenu.setItemAnimator(new DefaultItemAnimator());
    }

    //get User Type Menus
    private void getUserTypeMenuList() {

        if (strType.equals("New")) {

            mCommonMethods.setApplicationToolbarMenu(this, "Agent on Boarding");

            listMenuItem.add(new HomeMenu(R.drawable.icon_aob, "Agent On Boarding",
                    Activity_AOB_Authentication.class, false, false, "", null));

            listMenuItem.add(new HomeMenu(R.drawable.icon_aob_agent_details, "AOB Dashboard",
                    ActivityAOBAgentDetails.class, false, false, "", null));
        } else if (strType.equals(mCommonMethods.str_posp_ra_customer_type)) {

            mCommonMethods.setApplicationToolbarMenu(this, "POSP-RA");

            listMenuItem.add(new HomeMenu(R.drawable.icon_posp_ra, "POSP-RA",
                    Activity_POSP_RA_Authentication.class, false, false, "", null));

            listMenuItem.add(new HomeMenu(R.drawable.icon_posp_dashboard, "POSP-RA Dashboard",
                    Activity_POSP_RA_AgentDetails.class, false, false, "", null));
        }
    }
}