package sbilife.com.pointofsale_bancaagency.cifenrollment.phase1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class CIFEnrollmentMainActivity extends AppCompatActivity {

    public static String quotation_dashboard = "", pf_dasboard = "", dashboard = "", isFlag1 = "";
    private TabLayout tab_layout;
    private ViewPager2 viewPager;
    //private ActionBar actionBar;
    // Tab titles
    private String[] tabs = {"Personal Details",
            "Contact Details", "Qualification", "Exam Details",
            "Identification", "Preview"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cifenrollment_layout_new_main_activity);

        // Initilization

		new CommonMethods().setApplicationToolbarMenu1(this, "CIF on Boarding");
        viewPager = findViewById(R.id.pager);
        tab_layout = findViewById(R.id.tab_layout);

        //actionBar = getActionBar();
        TabsPagerAdapter mAdapter = new TabsPagerAdapter(this, 6);

        Intent i = getIntent();
        quotation_dashboard = i.getStringExtra("Quotation_number");
        pf_dasboard = i.getStringExtra("PF_Number");

        isFlag1 = i.getStringExtra("isFlag1");
        if (i.hasExtra("Dashboard"))
            dashboard = i.getStringExtra("Dashboard");

        viewPager.setAdapter(mAdapter);
        //actionBar.setHomeButtonEnabled(false);
        //actionBar.setDisplayShowHomeEnabled(true);

        //actionBar.setIcon(R.drawable.tileprofessional);
        //actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>CIF on Boarding</font>"));

        //actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.cifenrollment_actionbar_background_color));
        //actionBar.setSplitBackgroundDrawable(getResources().getDrawable(R.drawable.cifenrollment_actionbar_background_color));

        // Adding Tabs
				/*for (String tab_name : tabs) {
					actionBar.addTab(actionBar.newTab()
							.setText(tab_name)
							.setTabListener(this));
				}*/

		new TabLayoutMediator(tab_layout, viewPager,
				new TabLayoutMediator.TabConfigurationStrategy() {
					@Override public void onConfigureTab(TabLayout.Tab tab, int position) {
						tab.setText(tabs[position]);
					}
				}).attach();

        /*for (String tab_name : tabs) {
            tab_layout.addTab(tab_layout.newTab()
                    .setText(tab_name));
        }
        tab_layout.setTabGravity(TabLayout.GRAVITY_START);*/

        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //actionBar.setDisplayShowTitleEnabled(false);
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


//				actionBar.removeTabAt(6);
//				actionBar.addTab(actionBar.newTab().setText("raju").setTabListener(this), 7);


        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
				tab_layout.selectTab(tab_layout.getTabAt(position));
            }
        });
				
				/*viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						// on changing the page
						// make respected tab selected
						actionBar.setSelectedNavigationItem(position);
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
					}
				});*/

    }

	/*@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cifenrollment_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.dashboard) {

            Intent intent = new Intent(this, CIFEnrollmentDashboardActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.logout) {
//			finish();
//			Intent intent = new Intent(this, LoginActivity.class);
//			startActivity(intent);


            new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Exit")
                    .setMessage("Do you want to Logout?")
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

							/*Intent intent = new Intent(CIFEnrollmentMainActivity.this,
									CIFEnrollmentLoginActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);*/


                                    Intent intent = new Intent(CIFEnrollmentMainActivity.this, CIFEnrollmentPFActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();

                                }
                            }).setNegativeButton("No", null).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_yes_no);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button bt_yes = dialog.findViewById(R.id.bt_yes);
        Button bt_no = dialog.findViewById(R.id.bt_no);
        ((TextView) dialog.findViewById(R.id.tv_title)).setText("Are you sure you want to exit?");
        bt_yes.setText("Yes");
        bt_no.setText("No");
        bt_yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CIFEnrollmentMainActivity.this, CIFEnrollmentPFActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });


        bt_no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();

    }

    public ViewPager2 getViewpager() {
        return viewPager;

    }

}
