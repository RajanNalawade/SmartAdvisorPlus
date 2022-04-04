package sbilife.com.pointofsale_bancaagency.new_bussiness;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.annuityplus.AnnuityPlusActivity;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.home.CarouselHomeActivity;
import sbilife.com.pointofsale_bancaagency.poornsuraksha.BI_PoornSurakshaActivity;
import sbilife.com.pointofsale_bancaagency.products.eshieldNext.BI_EShieldNext;
import sbilife.com.pointofsale_bancaagency.products.new_smart_samriddhi.BI_NewSmartSamriddhiActivity;
import sbilife.com.pointofsale_bancaagency.products.saralinsurewealthplus.BI_SaralInsureWealthPlusActivity;
import sbilife.com.pointofsale_bancaagency.products.saraljeevanbima.BI_SaralJeevanBimaActivity;
import sbilife.com.pointofsale_bancaagency.products.saralpensionnew.BI_SaralPensionNewActivity;
import sbilife.com.pointofsale_bancaagency.products.smart_samriddhi.BI_SmartSamriddhiActivity;
import sbilife.com.pointofsale_bancaagency.products.smartfuturechoice.BI_SmartFutureChoicesActivity;
import sbilife.com.pointofsale_bancaagency.products.smartinsurewealthplus.BI_SmartInsureWealthPlusActivity;
import sbilife.com.pointofsale_bancaagency.products.smartplatinaassure.BI_SmartPlatinaAssureActivity;
import sbilife.com.pointofsale_bancaagency.products.smartplatinaplus.BI_SmartPlatinaPlusActivity;
import sbilife.com.pointofsale_bancaagency.retiresmart.BI_RetireSmartActivity;
import sbilife.com.pointofsale_bancaagency.rinnraksha.RinnRakshaActivity;
import sbilife.com.pointofsale_bancaagency.rinnraksha.RinnRakshaSingleActivity;
import sbilife.com.pointofsale_bancaagency.sampoorncancersuraksha.BI_SampoornCancerSurakshaActivity;
import sbilife.com.pointofsale_bancaagency.saralpension.BI_SaralPensionActivity;
import sbilife.com.pointofsale_bancaagency.saralshield.BI_SaralShieldActivity;
import sbilife.com.pointofsale_bancaagency.saralswadhan.BI_SaralSwadhanPlusActivity;
import sbilife.com.pointofsale_bancaagency.shubhnivesh.BI_ShubhNiveshActivity;
import sbilife.com.pointofsale_bancaagency.smartbachat.BI_SmartBachatActivity;
import sbilife.com.pointofsale_bancaagency.smartchamp.BI_SmartChampActivity;
import sbilife.com.pointofsale_bancaagency.smartelite.BI_SmartEliteActivity;
import sbilife.com.pointofsale_bancaagency.smarthumsafar.BI_SmartHumsafarActivity;
import sbilife.com.pointofsale_bancaagency.smartincomeprotect.BI_SmartIncomeProtectActivity;
import sbilife.com.pointofsale_bancaagency.smartmoneybackgold.BI_SmartMoneyBackGoldActivity;
import sbilife.com.pointofsale_bancaagency.smartmoneyplanner.BI_SmartMoneyPlannerActivity;
import sbilife.com.pointofsale_bancaagency.smartpower.BI_SmartPowerInsuranceActivity;
import sbilife.com.pointofsale_bancaagency.smartprivilege.BI_SmartPrivilegeActivity;
import sbilife.com.pointofsale_bancaagency.smartscholar.BI_SmartScholarActivity;
import sbilife.com.pointofsale_bancaagency.smartshield.BI_SmartShieldActivity;
import sbilife.com.pointofsale_bancaagency.smartswadhanplus.BI_SmartSwadhanPlusActivity;
import sbilife.com.pointofsale_bancaagency.smartwealthassure.BI_SmartWealthAssureActivity;
import sbilife.com.pointofsale_bancaagency.smartwealthbuilder.BI_SmartWealthBuilderActivity;

public class CarouselProduct extends AppCompatActivity {

    private DatabaseHelper db;

    protected ListView lv;

    // Button btnExit;
    private final Context context = this;
    private Spinner traditionalPlan;
    private Spinner unitLinkPlan;
    private Spinner group;
    private TableRow tblgroup;
    private ImageButton imagtbt_option;
    // String title = "" ;
    // TextView tv_title_common ;
    private AlertDialog.Builder showAlert;
    private final SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd");
    private String userType;

    //private StateIdAsyncTask stateIdAsyncTask;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.productselection);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        db = new DatabaseHelper(this);
        final CommonMethods commonMethods = new CommonMethods();
        imagtbt_option = findViewById(R.id.imagtbt_option);

        // Traditional Plan
        traditionalPlan = findViewById(R.id.traditional_plan);


        // Unit Link Plan
        unitLinkPlan = findViewById(R.id.unit_link_plan);

        commonMethods.setApplicationToolbarMenu(this, "Premium Calculator");
        group = findViewById(R.id.group);

        db = new DatabaseHelper(this);

        tblgroup = findViewById(R.id.tblgroup);
        userType = commonMethods.GetUserType(context);
        if (userType.contentEquals("AGENT")
                || userType.contentEquals("UM")) {
            tblgroup.setVisibility(View.GONE);
        } else {
            tblgroup.setVisibility(View.VISIBLE);
        }

        //Added by Tushar Kadam to get Kerala Resident
		/*String kerlaDiscountDetails = AppSharedPreferences.getData(context,(new CommonMethods().getKerlaDiscount()),"");
		if(TextUtils.isEmpty(kerlaDiscountDetails)) {
			stateIdAsyncTask = new StateIdAsyncTask(context, commonMethods.GetUserCode(context), userType);
			stateIdAsyncTask.execute();
		}*/

        /******************************* Item listener starts here ***************************************/

        // Traditional products
        traditionalPlan.setOnItemSelectedListener(new OnItemSelectedListener() {

            // @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {

                String productName = traditionalPlan.getSelectedItem().toString();

                if (productName.equalsIgnoreCase("SBI Life - Smart Swadhan Plus")) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_SmartSwadhanPlusActivity.class);
                    startActivity(i1);
                } else if (productName.equalsIgnoreCase("SBI Life - Smart Humsafar")) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_SmartHumsafarActivity.class);
                    startActivity(i1);
                } else if (productName.equalsIgnoreCase("SBI Life - Smart Champ Insurance")) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_SmartChampActivity.class);
                    startActivity(i1);
                } else if (productName.equalsIgnoreCase("SBI Life - Shubh Nivesh")) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_ShubhNiveshActivity.class);
                    startActivity(i1);
                } else if (productName.equalsIgnoreCase("SBI Life - Saral Shield")) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_SaralShieldActivity.class);
                    startActivity(i1);
                } else if (productName.equalsIgnoreCase("SBI Life - Smart Shield")) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_SmartShieldActivity.class);
                    startActivity(i1);
                } else if (productName.equalsIgnoreCase("SBI Life - Annuity Plus")) {
                    Intent i1 = new Intent(getApplicationContext(),
                            AnnuityPlusActivity.class);
                    startActivity(i1);
                } else if (productName.equalsIgnoreCase("SBI Life - Smart Money Back Gold")) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_SmartMoneyBackGoldActivity.class);
                    startActivity(i1);
                } else if (productName.equalsIgnoreCase("SBI Life - Saral Retirement Saver")) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_SaralPensionActivity.class);
                    startActivity(i1);
                } else if (productName.equalsIgnoreCase("SBI Life - Smart Income Protect")) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_SmartIncomeProtectActivity.class);
                    startActivity(i1);
                } else if (productName.equalsIgnoreCase("SBI Life - Saral Swadhan Plus")) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_SaralSwadhanPlusActivity.class);
                    startActivity(i1);
                } else if (productName.equalsIgnoreCase("SBI Life - Smart Bachat")) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_SmartBachatActivity.class);
                    startActivity(i1);
                } else if (productName.equalsIgnoreCase("SBI Life - Smart Money Planner")) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_SmartMoneyPlannerActivity.class);
                    startActivity(i1);
                } else if (productName.equalsIgnoreCase("SBI Life - Sampoorn Cancer Suraksha")) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_SampoornCancerSurakshaActivity.class);
                    startActivity(i1);

                } else if (productName.equalsIgnoreCase("SBI Life - Poorna Suraksha")) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_PoornSurakshaActivity.class);
                    startActivity(i1);
                } else if (productName.equalsIgnoreCase("SBI Life - Smart Samriddhi")) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_SmartSamriddhiActivity.class);
                    startActivity(i1);
                } else if (productName.equalsIgnoreCase("SBI Life - Smart Platina Assure")) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_SmartPlatinaAssureActivity.class);
                    startActivity(i1);
                } else if (productName.equalsIgnoreCase("SBI Life - Smart Future Choices")) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_SmartFutureChoicesActivity.class);
                    startActivity(i1);
                } else if (productName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_saral_jeevan_bima))) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_SaralJeevanBimaActivity.class);
                    startActivity(i1);
                } else if (productName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_new_smart_samriddhi))) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_NewSmartSamriddhiActivity.class);
                    startActivity(i1);
                } else if (productName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_saral_pension_new))) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_SaralPensionNewActivity.class);
                    startActivity(i1);
                }else if (productName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_eshield_next))) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_EShieldNext.class);
                    startActivity(i1);
                }else if (productName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_smart_platina_plus))) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_SmartPlatinaPlusActivity.class);
                    startActivity(i1);
                } else if (productName.equalsIgnoreCase("")) {
                }

                traditionalPlan.setSelection(0);
            }

            // @Override
            public void onNothingSelected(AdapterView<?> arg0) {


            }
        });

        // unit Linked products
        unitLinkPlan.setOnItemSelectedListener(new OnItemSelectedListener() {

            // @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {
                String productName = unitLinkPlan.getSelectedItem().toString();
                if (productName.equalsIgnoreCase("SBI Life - Smart Power Insurance")) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_SmartPowerInsuranceActivity.class);
                    startActivity(i1);

                }
				 /*else if(productName.equalsIgnoreCase("")){

					Intent i1 = new Intent(getApplicationContext(),
							BI_SaralMahaAnandActivity.class);
					startActivity(i1);
				}*/
                else if (productName.equalsIgnoreCase("SBI Life - Smart Elite")) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_SmartEliteActivity.class);
                    startActivity(i1);
                } else if (productName.equalsIgnoreCase("SBI Life - Smart Wealth Assure")) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_SmartWealthAssureActivity.class);
                    startActivity(i1);
                } else if (productName.equalsIgnoreCase("SBI Life - Smart Scholar")) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_SmartScholarActivity.class);
                    startActivity(i1);
                } else if (productName.equalsIgnoreCase("SBI Life - Smart Wealth Builder")) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_SmartWealthBuilderActivity.class);
                    startActivity(i1);
                } else if (productName.equalsIgnoreCase("SBI Life - Retire Smart")) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_RetireSmartActivity.class);
                    startActivity(i1);
                } else if (productName.equalsIgnoreCase("SBI Life - Smart Privilege")) {
                    Intent i1 = new Intent(getApplicationContext(),
                            BI_SmartPrivilegeActivity.class);
                    startActivity(i1);
                } else if (productName.equalsIgnoreCase("SBI Life - Saral InsureWealth Plus")) {

                    if (userType.equalsIgnoreCase("AGENT")
                            || userType.equalsIgnoreCase("BAP")
                            || userType.equalsIgnoreCase("IMF")) {
                        commonMethods.showMessageDialog(context, "You are not applicable for this Product");
                        unitLinkPlan.setSelection(0);
                        return;
                    }

                    Intent i1 = new Intent(getApplicationContext(),
                            BI_SaralInsureWealthPlusActivity.class);
                    startActivity(i1);
                } else if (productName.equalsIgnoreCase("SBI Life - Smart InsureWealth Plus")) {


                    if (userType.equalsIgnoreCase("CIF")
                            || userType.equalsIgnoreCase("CAG")) {
                        commonMethods.showMessageDialog(context,
                                "You are not applicable for this Product");
                        unitLinkPlan.setSelection(0);
                        return;
                    }

                    Intent i1 = new Intent(getApplicationContext(),
                            BI_SmartInsureWealthPlusActivity.class);
                    startActivity(i1);
                }
                unitLinkPlan.setSelection(0);
            }

            // @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        // group products
        group.setOnItemSelectedListener(new OnItemSelectedListener() {

            // @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {

                /*changes starts by rajan 15-02-2018*/
                switch (pos) {
                    case 1:
                        //for lppt premium
                        Intent i1 = new Intent(getApplicationContext(), RinnRakshaActivity.class);
                        i1.putExtra("RIN_RAKSHA_PREMIUM_MODE", "LPPT");
                        startActivity(i1);
                        break;
                    case 2:
                        //for single premium
                        Intent i2 = new Intent(getApplicationContext(), RinnRakshaSingleActivity.class);
                        i2.putExtra("RIN_RAKSHA_PREMIUM_MODE", "Single");
                        startActivity(i2);
                        break;
                }
                /*changes end by rajan 15-02-2018*/

                group.setSelection(0);
            }

            // @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        showAlert = new Builder(context);

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String installDate = preferences.getString("InstallAppsDate", null);
        if (installDate == null) {
            // First run, so save the current date
            SharedPreferences.Editor editor = preferences.edit();
            Date now = new Date();
            String dateString = formatter.format(now);
            editor.putString("InstallAppsDate", dateString);
            editor.apply();
        } else {
            // Commit the edits!
            Date d1 = null;
            try {
                d1 = formatter.parse(installDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // System.out.println("**********"+(new Date().getTime() -
            // d1.getTime())/86400000);
            if (((new Date().getTime() - d1.getTime()) / 86400000) > 30) {
                showAlert
                        .setMessage("Calculator has been Expired, Please download updated version of Calculator");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent addPersonalProfile = new Intent(
                                        getApplicationContext(),
                                        CarouselHomeActivity.class);
                                startActivity(addPersonalProfile);

                            }

                        });
                showAlert.setOnKeyListener(new OnKeyListener() {

                    public boolean onKey(DialogInterface arg0, int arg1,
                                         KeyEvent arg2) {
                        // TODO Auto-generated method stub
                        Intent addPersonalProfile = new Intent(
                                getApplicationContext(),
                                CarouselHomeActivity.class);
                        startActivity(addPersonalProfile);
                        return false;
                    }
                });
                showAlert.show();

            }

        }

    }

    @Override
    protected void onDestroy() {

		/*if(stateIdAsyncTask !=null){
			stateIdAsyncTask.cancel(true);
		}*/
        super.onDestroy();
    }

}


