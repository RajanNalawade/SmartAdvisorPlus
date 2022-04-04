package sbilife.com.pointofsale_bancaagency;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import sbilife.com.pointofsale_bancaagency.annuityplus.AnnuityPlusActivity;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.flexismartplus.BI_FlexiSmartPlusActivity;
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
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.products.smartplatinaplus.BI_SmartPlatinaPlusActivity;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;
import sbilife.com.pointofsale_bancaagency.retiresmart.BI_RetireSmartActivity;
import sbilife.com.pointofsale_bancaagency.rinnraksha.RinnRakshaActivity;
import sbilife.com.pointofsale_bancaagency.rinnraksha.RinnRakshaSingleActivity;
import sbilife.com.pointofsale_bancaagency.sampoorncancersuraksha.BI_SampoornCancerSurakshaActivity;
import sbilife.com.pointofsale_bancaagency.saralmahaanand.BI_SaralMahaAnandActivity;
import sbilife.com.pointofsale_bancaagency.saralpension.BI_SaralPensionActivity;
import sbilife.com.pointofsale_bancaagency.saralshield.BI_SaralShieldActivity;
import sbilife.com.pointofsale_bancaagency.saralswadhan.BI_SaralSwadhanPlusActivity;
import sbilife.com.pointofsale_bancaagency.shubhnivesh.BI_ShubhNiveshActivity;
import sbilife.com.pointofsale_bancaagency.smartbachat.BI_SmartBachatActivity;
import sbilife.com.pointofsale_bancaagency.smartchamp.BI_SmartChampActivity;
import sbilife.com.pointofsale_bancaagency.smartelite.BI_SmartEliteActivity;
import sbilife.com.pointofsale_bancaagency.smartguaranteedsavings.BI_SmartGuaranteedSavingsPlanActivity;
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
import sbilife.com.pointofsale_bancaagency.smartwomenadvantage.BI_SmartWomenAdvantageActivity;

public class ProductDesc_webview extends AppCompatActivity implements
        OnItemClickListener, ServiceHits.DownLoadData {
    private TextView txtHeaderName;
    private Context context;
    private TextView tv_loading;
    private String dest_file_path = "";
    private int downloadedSize = 0;
    private String download_file_url = "";
    private CommonMethods commonMethods;
    private ServiceHits service;
    private String productName = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.productdesc_webview);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        txtHeaderName = findViewById(R.id.txtHeaderName_wv);
        TextView txtHeaderUIN = findViewById(R.id.txtHeaderUIN_wv);
        TextView txtHeaderPCode = findViewById(R.id.txtHeaderPCode_wv);
        tv_loading = new TextView(context);

        Button btnBrochure = findViewById(R.id.btnBrochure);

        WebView webview = findViewById(R.id.webview);
        commonMethods.setApplicationToolbarMenu(this, "Products");

        Intent i = getIntent();
        productName = i.getStringExtra("name");

        /*
         * Unit Linked plans
         */

        if (productName.equals("SBI Life - Smart Performer")) {
            //txtHeaderName.setText("SBI Life - Smart Performer");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Smart Performer</u>"));
            txtHeaderUIN.setText("(UIN:111L068V01)");
            txtHeaderPCode.setText("");
        } else if (productName.equals("SBI Life - Unit Plus Super")) {
            //txtHeaderName.setText("SBI Life - Unit Plus Super");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Unit Plus Super</u>"));
            txtHeaderUIN.setText("(UIN:111L069V01)");
            txtHeaderPCode.setText("");
        } else if (productName.equals("SBI Life - Saral Maha Anand")) {
            //txtHeaderName.setText("SBI Life - Saral Maha Anand");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Saral Maha Anand</u>"));
            txtHeaderUIN.setText("(UIN:111L070V02)");
            txtHeaderPCode.setText("(Product Code : 50)");

            webview.loadUrl("file:///android_asset/saral_maha_anand.html");
            dest_file_path = "saral_maha_anand_brochure.pdf";
            //download_file_url ="https://www.sbilife.co.in/saral-maha-anand-policy";
            download_file_url = "https://www.sbilife.co.in/saral-maha-anand-brochure";

        } else if (productName.equals("SBI Life - Smart Elite")) {
            //txtHeaderName.setText("SBI Life - Smart Elite");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Smart Elite</u>"));
            txtHeaderUIN.setText("(UIN:111L072V04)");
            txtHeaderPCode.setText("(Product Code : 53)");

			/*webview.loadUrl("file:///android_asset/smart_elite.html");
			dest_file_path = "smart_elite_brochure.pdf";
			download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/Smart_Eite_Brochure.pdf";*/

            webview.loadUrl("file:///android_asset/smart_elite.html");
            dest_file_path = "smart_elite_brochure.pdf";
            download_file_url = "https://www.sbilife.co.in/smart-elite-brochure";
        } else if (productName.equals("SBI Life - Smart Scholar")) {
            //txtHeaderName.setText("SBI Life - Smart Scholar");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Smart Scholar</u>"));
            txtHeaderUIN.setText("(UIN:111L073V03)");
            txtHeaderPCode.setText("(Product Code : 51)");

            //webview.loadUrl("file:///android_asset/smart_scholar.html");
            //dest_file_path = "smart_scholar_brochure.pdf";
            //download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/Smart_Scholar_Brochure_V2.pdf";

            webview.loadUrl("file:///android_asset/smart_scholar.html");
            dest_file_path = "smart_scholar_brochure.pdf";
            download_file_url = "https://www.sbilife.co.in/smart-scholar-brochure";
        } else if (productName.equals("SBI Life - Smart Bachat")) {
            //txtHeaderName.setText("SBI Life - Smart Wealth Builder");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Smart Bachat</u>"));
            txtHeaderUIN.setText("(UIN:111N108V03)");
            txtHeaderPCode.setText("(Product Code : 2D)");

			/*webview.loadUrl("file:///android_asset/smart_bachat.html");
			dest_file_path = "smart_bacaht_brochure_english.pdf";
			download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/smart_bacaht_brochure_english.pdf";*/

            webview.loadUrl("file:///android_asset/smart_bachat.html");
            dest_file_path = "smart_bacaht_brochure_english.pdf";
            download_file_url = "https://www.sbilife.co.in/smart_bachat_brochure";
        } else if (productName.equals("SBI Life - Smart Horizon")) {
            //txtHeaderName.setText("SBI Life - Smart Horizon");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Smart Horizon</u>"));
            txtHeaderUIN.setText("(UIN:111L074V01)");
            txtHeaderPCode.setText("");
        } else if (productName.equals("SBI Life - Smart Power Insurance")) {
            //txtHeaderName.setText("SBI Life - Smart Power Insurance");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Smart Power Insurance</u>"));
            txtHeaderUIN.setText("(UIN:111L090V02)");
            txtHeaderPCode.setText("(Product Code : 1C)");

			/*webview.loadUrl("file:///android_asset/smart_power_insurance.html");
			dest_file_path = "smart_power_insurance_brochure.pdf";
			download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/Smart_Power_Brochure_new_ver02.pdf";*/

            webview.loadUrl("file:///android_asset/smart_power_insurance.html");
            dest_file_path = "smart_power_insurance_brochure.pdf";
            download_file_url = "https://www.sbilife.co.in/smart-power-insurance-brochure";

        } else if (productName.equals("SBI Life - Smart Wealth Assure")) {
            //txtHeaderName.setText("SBI Life - Smart Wealth Assure");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Smart Wealth Assure</u>"));
            txtHeaderUIN.setText("(UIN:111L077V03)");
            txtHeaderPCode.setText("(Product Code : 55)");

			/*webview.loadUrl("file:///android_asset/smart_wealth_assure.html");
			dest_file_path = "smart_wealth_assure_brochure.pdf";
			download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/Smart_Wealth_Assure_Brochure.pdf";*/

            webview.loadUrl("file:///android_asset/smart_wealth_assure.html");
            dest_file_path = "smart_wealth_assure_brochure.pdf";
            download_file_url = "https://www.sbilife.co.in/smart-wealth-assure-brochure";
        } else if (productName.equals("SBI Life - Smart Wealth Builder")) {
            //txtHeaderName.setText("SBI Life - Smart Wealth Builder");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Smart Wealth Builder</u>"));
            txtHeaderUIN.setText("(UIN:111L095V03)");
            txtHeaderPCode.setText("(Product Code : 1K)");

			/*webview.loadUrl("file:///android_asset/smart_wealth_builder.html");
			dest_file_path = "smart_wealth_builder_brochure.pdf";
			download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/smart_wealth_builder_brochure_eng_v02.pdf";*/

            webview.loadUrl("file:///android_asset/smart_wealth_builder.html");
            dest_file_path = "smart_wealth_builder_brochure.pdf";
            download_file_url = "https://www.sbilife.co.in/smart-wealth-builder-brochure";
        } else if (productName.equals("SBI Life - Smart Women Advantage")) {
            //txtHeaderName.setText("SBI Life - Smart Wealth Builder");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Smart Women Advantage</u>"));
            txtHeaderUIN.setText("(UIN:111N106V01)");
            txtHeaderPCode.setText("(Product Code : 2C)");

			/*webview.loadUrl("file:///android_asset/smart_women_advantage.html");
			dest_file_path = "smart_women_advantage_brochures.pdf";
			download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/smart_women_advantage_brochures.pdf";*/

            webview.loadUrl("file:///android_asset/smart_women_advantage.html");
            dest_file_path = "smart_women_advantage_brochure.pdf";

            download_file_url = "https://www.sbilife.co.in/smart-women-advantage-brochure";
        } else if (productName.equals("SBI Life - Saral Swadhan+")) {
            //txtHeaderName.setText("SBI Life - Smart Wealth Builder");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Saral Swadhan+</u>"));
            txtHeaderUIN.setText("(UIN:111N092V03)");
            txtHeaderPCode.setText("(Product Code : 1J)");

            webview.loadUrl("file:///android_asset/saral_swadhan_plus.html");
            dest_file_path = "saral_swadhan_plus_brochure.pdf";
            download_file_url = "https://www.sbilife.co.in/saral_swadhan_plus_bro_pdf";
        }

        /*
         * Child Plans
         */

        else if (productName.equals("SBI Life - Scholar II")) {
            //txtHeaderName.setText("SBI Life - Scholar II");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Scholar II</u>"));
            txtHeaderUIN.setText("(UIN:111N020V01)");
            txtHeaderPCode.setText("");
        } else if (productName.equals("SBI Life - Smart Champ Insurance")) {
            //txtHeaderName.setText("SBI Life - Smart Guaranteed Savings Plan");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Smart Champ Insurance</u>"));
            txtHeaderUIN.setText("(UIN:111N098V03)");
            txtHeaderPCode.setText("(Product Code : 1P)");

			/*webview.loadUrl("file:///android_asset/smart_champ.html");
			dest_file_path = "smart_champ_brochure.pdf";
			download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/smart_champ_insurance_brochure_05Aug2016.pdf";*/

            webview.loadUrl("file:///android_asset/smart_champ.html");
            dest_file_path = "smart_champ_brochure.pdf";
            download_file_url = "https://www.sbilife.co.in/smart-champ-insurance-brochure";
        }
        /*
         * else if(productName.equals("SBI Life - Smart Scholar")) {
         * txtHeaderName.setText("SBI Life - Smart Scholar");
         * txtHeaderUIN.setText("(UIN:111L073V01)");
         * txtHeaderPCode.setText("(Product Code : 51)"); }
         */
        /*
         * Pension Plans
         */
        else if (productName.equals("SBI Life - Annuity Plus")) {
            //txtHeaderName.setText("SBI Life - Annuity Plus");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Annuity Plus</u>"));
            //UIN: 111N083V11
            txtHeaderUIN.setText("(UIN:111N083V11)");
            txtHeaderPCode.setText("(Product Code:22)");

			/*webview.loadUrl("file:///android_asset/annuity_plus.html");
			dest_file_path = "annuity_plus_brochure.pdf";
			download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/annuity_plus_en.pdf";*/

            webview.loadUrl("file:///android_asset/annuity_plus.html");
            dest_file_path = "annuity_plus_brochure.pdf";
            download_file_url = "https://www.sbilife.co.in/annuity-plus-brochure";
        } else if (productName.equals("SBI Life - Retire Smart")) {
            //txtHeaderName.setText("SBI Life - Retire Smart");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Retire Smart</u>"));
            txtHeaderUIN.setText("(UIN:111L094V02)");
            txtHeaderPCode.setText("(Product Code : 1H)");

			/*webview.loadUrl("file:///android_asset/retire_smart.html");
			dest_file_path = "retire_smart_brochure.pdf";
			download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/Retire_Smart_Brochure_v1.pdf";*/


            webview.loadUrl("file:///android_asset/retire_smart.html");
            dest_file_path = "retire_smart_brochure.pdf";
            download_file_url = "https://www.sbilife.co.in/retire-smart-brochure";
        } else if (productName.equals("SBI Life - Saral Retirement Saver")) {
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Saral Retirement Saver</u>"));
            txtHeaderUIN.setText("(UIN:111N088V03)");
            txtHeaderPCode.setText("(Product Code : 1E)");

			/*webview.loadUrl("file:///android_asset/SaralRetirementSaver.html");
			dest_file_path = "saral_pension_brochure.pdf";
			download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/Saral_Pension_Brochure_en.pdf";*/


            webview.loadUrl("file:///android_asset/SaralRetirementSaver.html");
            dest_file_path = "saral_pension_brochure.pdf";
            download_file_url = "https://www.sbilife.co.in/saral-retirement-saver-brochure";
        }

        /*
         * Protection Plans
         */

        else if (productName.equals("SBI Life - Smart Income Shield Insurance")) {
            //txtHeaderName.setText("SBI Life - Smart Income Shield Insurance");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Smart Income Shield Insurance</u>"));
            txtHeaderUIN.setText("(UIN:111N084V01)");
            txtHeaderPCode.setText("");
        } else if (productName.equals("SBI Life - Saral Shield")) {
            //txtHeaderName.setText("SBI Life - Saral Shield");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Saral Shield</u>"));
            txtHeaderUIN.setText("(UIN:111N066V03)");
            txtHeaderPCode.setText("(Product Code : 47)");

			/*webview.loadUrl("file:///android_asset/saral_shield.html");
			dest_file_path = "saral_shield_brochure.pdf";
			download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/Saral_Shield_Brochure_new_version.pdf";*/

            webview.loadUrl("file:///android_asset/saral_shield.html");
            dest_file_path = "saral_shield_brochure.pdf";
            download_file_url = "https://www.sbilife.co.in/saral-shield-brochure";
        } else if (productName.equals("SBI Life - Swadhan")) {
            //txtHeaderName.setText("SBI Life - Swadhan");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Swadhan</u>"));
            txtHeaderUIN.setText("(UIN:111N013V01)");
            txtHeaderPCode.setText("");
        } else if (productName.equals("SBI Life - Smart Shield")) {
            //txtHeaderName.setText("SBI Life - Smart Shield");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Smart Shield</u>"));

			/*webview.loadUrl("file:///android_asset/smart_shield.html");
			dest_file_path = "smart_shield_brochure.pdf";
			download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/smart_shield_brochure_english_new_product_29july16.pdf";*/

            txtHeaderUIN.setText("(UIN:111N067V07)");
            txtHeaderPCode.setText("(Product Code : 45)");

            webview.loadUrl("file:///android_asset/smart_shield.html");
            dest_file_path = "smart_shield_brochure.pdf";
            download_file_url = "https://www.sbilife.co.in/smart-shield-brochure";
        } else if (productName.equals("SBI Life - eShield")) {
            txtHeaderName.setText("SBI Life - eShield");
            txtHeaderUIN.setText("(UIN:111N089V01)");
            txtHeaderPCode.setText("(Product Code:1G)");

            webview.loadUrl("file:///android_asset/eshield.html");
            dest_file_path = "eshield_brochure.pdf";
            download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/eShield_Brochure_ver-05.pdf";
        } else if (productName.equals("SBI Life - Grameen Bima")) {
            txtHeaderName.setText("SBI Life - Grameen Bima");
            txtHeaderUIN.setText("(UIN : 111N087V01)");
            txtHeaderPCode.setText("(Product Code : 1F)");

            webview.loadUrl("file:///android_asset/grameen_bima.html");
            dest_file_path = "grameen_bima_brochure.pdf";

            download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/grameen_bima_brochure_04_05_15_BR_ENG.pdf";
        }

        /*
         * Saving Plans
         */

        else if (productName.equals("SBI Life - Smart Income Protect")) {
            //txtHeaderName.setText("SBI Life - Smart Income Protect");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Smart Income Protect</u>"));
            txtHeaderUIN.setText("(UIN:111N085V04)");
            txtHeaderPCode.setText("(Product Code : 1B)");

			/*webview.loadUrl("file:///android_asset/smart_income_protect.html");
			dest_file_path = "smart_income_protect_brochure.pdf";
			download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/smart_income_protect_brochure_05Aug2016.pdf";*/

            webview.loadUrl("file:///android_asset/smart_income_protect.html");
            dest_file_path = "smart_income_protect_brochure.pdf";
            download_file_url = "https://www.sbilife.co.in/smart-income-protect-brochure";

        } else if (productName.equals("SBI Life - Smart Money Back Insurance")) {
            //txtHeaderName.setText("SBI Life - Smart Money Back Insurance");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Smart Money Back Insurance</u>"));
            txtHeaderUIN.setText("(UIN:111N082V01)");
            txtHeaderPCode.setText("");
        } else if (productName.equals("SBI Life - Flexi Smart Plus")) {
            //txtHeaderName.setText("SBI Life - Flexi Smart Plus");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Flexi Smart Plus</u>"));
            txtHeaderUIN.setText("(UIN:111N093V01)");
            txtHeaderPCode.setText("(Product Code : 1M)");

			/*webview.loadUrl("file:///android_asset/flexi_smart_plus.html");
			dest_file_path = "flexi_smart_plus_brochure.pdf";
			download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/Flexi_Smart_Plus_Brochure_220314_V1.pdf";*/

            webview.loadUrl("file:///android_asset/flexi_smart_plus.html");
            dest_file_path = "flexi_smart_plus_brochure.pdf";
            download_file_url = "https://www.sbilife.co.in/flexi-smart-plus-brochure";
        } else if (productName.equals("SBI Life - Smart Money Back Gold")) {
            //txtHeaderName.setText("SBI Life - Smart Money Back Gold");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Smart Money Back Gold</u>"));
            txtHeaderUIN.setText("(UIN:111N096V03)");
            txtHeaderPCode.setText("(Product Code : 1N)");

			/*webview.loadUrl("file:///android_asset/smart_money_back_gold.html");
			dest_file_path = "smart_money_back_gold_brochure.pdf";
			download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/smart_money_back_gold_brochure_05Aug2016.pdf";*/


            webview.loadUrl("file:///android_asset/smart_money_back_gold.html");
            dest_file_path = "smart_money_back_gold_brochure.pdf";
            download_file_url = "https://www.sbilife.co.in/smart-money-back-gold-brochure";
        } else if (productName.equals("SBI Life - Sanjeevan Supreme")) {
            //txtHeaderName.setText("SBI Life - Snajeevan Supreme");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Snajeevan Supreme</u>"));
            txtHeaderUIN.setText("(UIN:111N016V01)");
            txtHeaderPCode.setText("");
        } else if (productName.equals("SBI Life - Shubh Nivesh")) {
            //txtHeaderName.setText("SBI Life - Shubh Nivesh");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Shubh Nivesh</u>"));
            txtHeaderUIN.setText("(UIN:111N055V04)");
            txtHeaderPCode.setText("(Product Code : 35)");

			/*webview.loadUrl("file:///android_asset/shubh_nivesh.html");
			dest_file_path = "shubh_nivesh_brochure.pdf";
			download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/shubh_nivesh_brochure_05Aug2016.pdf";*/


            webview.loadUrl("file:///android_asset/shubh_nivesh.html");
            dest_file_path = "shubh_nivesh_brochure.pdf";
            download_file_url = "https://www.sbilife.co.in/shubh-nivesh";
        } else if (productName.equals("SBI Life - Saral Life")) {
            //txtHeaderName.setText("SBI Life - Saral Life");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Saral Life</u>"));
            txtHeaderUIN.setText("(UIN:111N071V01)");
            txtHeaderPCode.setText("");
        } else if (productName.equals("SBI Life - Smart Guaranteed Savings Plan")) {
            //txtHeaderName.setText("SBI Life - Smart Guaranteed Savings Plan");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Smart Guaranteed Savings Plan</u>"));
            txtHeaderUIN.setText("(UIN:111N097V01)");
            txtHeaderPCode.setText("(Product Code : 1X)");

			/*webview.loadUrl("file:///android_asset/smart_guaranteed.html");
			dest_file_path = "smart_guaranteed_brochure.pdf";
			download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/Smart_Guranteed_Savings_Plan_Brochure.pdf";*/

            webview.loadUrl("file:///android_asset/smart_guaranteed.html");
            dest_file_path = "smart_guaranteed_brochure.pdf";
            download_file_url = "https://www.sbilife.co.in/smart-gauranteed-bro";
        } else if (productName.equals("SBI Life - Smart Money Planner")) {
            //txtHeaderName.setText("SBI Life - Smart Guaranteed Savings Plan");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Smart Money Planner</u>"));
            txtHeaderUIN.setText("(UIN:111N101V03)");
            txtHeaderPCode.setText("(Product Code : 1R)");

			/*webview.loadUrl("file:///android_asset/smart_money_planner.html");
			dest_file_path = "smart_money_planner_brochure.pdf";
			download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/smart_money_planner_brochure_05Aug2016.pdf";*/

            webview.loadUrl("file:///android_asset/smart_money_planner.html");
            dest_file_path = "smart_money_planner_brochure.pdf";
            download_file_url = "https://www.sbilife.co.in/smart-money-planner-brochure";
        } else if (productName.equals("SBI Life - Smart Humsafar")) {
            //txtHeaderName.setText("SBI Life - Smart Guaranteed Savings Plan");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Smart Humsafar</u>"));
            txtHeaderUIN.setText("(UIN:111N103V03)");
            txtHeaderPCode.setText("(Product Code : 1W)");

			/*webview.loadUrl("file:///android_asset/smart_humsafar.html");
			dest_file_path = "smart_humsafar_brochure.pdf";
			download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/smart_humsafar_brochure_05Aug2016.pdf";*/

            webview.loadUrl("file:///android_asset/smart_humsafar.html");
            dest_file_path = "smart_humsafar_brochure.pdf";
            download_file_url = "https://www.sbilife.co.in/smart-humsafar-brochure";
        } else if (productName.equals("SBI Life - Smart Swadhan Plus")) {
            //txtHeaderName.setText("SBI Life - Smart Guaranteed Savings Plan");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Smart Swadhan Plus</u>"));
            txtHeaderUIN.setText("(UIN:111N104V02)");
            txtHeaderPCode.setText("(Product Code : 1Z)");

			/*webview.loadUrl("file:///android_asset/smart_swadhan_plus.html");
			dest_file_path = "smart_swadhan_plus_brochure.pdf";
			download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/smart_swadhan_plus_brochure_english.pdf";*/

            webview.loadUrl("file:///android_asset/smart_swadhan_plus.html");
            dest_file_path = "smart_swadhan_plus_brochure.pdf";
            download_file_url = "https://www.sbilife.co.in/smart-swadhan-plus-brochure";
        }

        /*
         * Health Plans
         */

        else if (productName.equals("SBI Life - Hospital Cash")) {
            //txtHeaderName.setText("SBI Life - Hospital Cash");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Hospital Cash</u>"));
            txtHeaderUIN.setText("(UIN:111N065V01)");
            txtHeaderPCode.setText("");
        } else if (productName.equals("SBI Life - Group Criti 9")) {
            //txtHeaderName.setText("SBI Life - Group Criti 9");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Group Criti 9</u>"));
            txtHeaderUIN.setText("(UIN:111N050V01)");
            txtHeaderPCode.setText("");
        } else if (productName.equals("SBI Life - Smart Health Insurance")) {
            //txtHeaderName.setText("SBI Life - Smart Health Insurance");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Smart Health Insurance</u>"));
            txtHeaderUIN.setText("(UIN:111N086V01)");
            txtHeaderPCode.setText("");
        }

        //Group plan
        /*changes starts by rajan 15-02-2018*/
        else if (productName.equals("SBI Life - RiNn Raksha(SBG) - LPPT")) {
            //txtHeaderName.setText("SBI Life - RiNn Raksha");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - RiNn Raksha(SBG) - LPPT</u>"));
            txtHeaderUIN.setText("(UIN:111N078V02)");
            txtHeaderPCode.setText("(Product Code:70)");


			/*webview.loadUrl("file:///android_asset/rinnraksha.html");
			dest_file_path = "rinnraksha_brochure.pdf";
			download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/RiNn_raksha_brochure_ver05.pdf";*/

            webview.loadUrl("file:///android_asset/rinnraksha.html");
            dest_file_path = "RiNn_raksha_brochure_ver05.pdf";
            download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/RiNn_raksha_brochure_ver05.pdf";
        } else if (productName.equals("SBI Life - RiNn Raksha(SBG) - Single")) {
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - RiNn Raksha(SBG) - Single</u>"));
            txtHeaderUIN.setText("(UIN:111N078V02)");
            txtHeaderPCode.setText("(Product Code:70)");


			/*webview.loadUrl("file:///android_asset/rinnraksha.html");
			dest_file_path = "rinnraksha_brochure.pdf";
			download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/RiNn_raksha_brochure_ver05.pdf";*/

            webview.loadUrl("file:///android_asset/rinnraksha.html");
            dest_file_path = "RiNn_raksha_brochure_ver05.pdf";
            download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/RiNn_raksha_brochure_ver05.pdf";
        }
        /*changes end by rajan 15-02-2018*/
        else if (productName.equals("SBI Life - Smart Privilege")) {
            //txtHeaderName.setText("SBI Life - RiNn Raksha");
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Smart Privilege</u>"));


			/*webview.loadUrl("file:///android_asset/smart_privilege.html");
			dest_file_path = "smart_privilege.pdf";
			download_file_url = "http://www.sbilife.co.in/sbilife/images/File/documents/RiNn_raksha_brochure_ver05.pdf";*/

            txtHeaderUIN.setText("(UIN:111L107V03)");
            txtHeaderPCode.setText("(Product Code : 2B)");

            webview.loadUrl("file:///android_asset/smart_privilege.html");
            dest_file_path = "smart_privilege_brochure.pdf";
            download_file_url = "https://www.sbilife.co.in/smart-privilege-brochure";
        } else if (productName.equalsIgnoreCase("SBI Life - Sampoorn Cancer Suraksha")) {
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Sampoorn Cancer Suraksha</u>"));
            txtHeaderUIN.setText("(UIN:111N109V03)");
            txtHeaderPCode.setText("(Product Code : 2E)");

            webview.loadUrl("file:///android_asset/sampoorn_cancer_suraksha.html");
            dest_file_path = "sampoorn_cancer_suraksha_brochure.pdf";
            download_file_url = "https://www.sbilife.co.in/Sampoorn-Cancer-Suraksha-brochure";


        } else if (productName.equalsIgnoreCase("SBI Life - Poorna Suraksha")) {
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - Poorna Suraksha</u>"));
            txtHeaderUIN.setText("(UIN:111N110V03)");
            txtHeaderPCode.setText("(Product Code : 2F)");

            webview.loadUrl("file:///android_asset/poorn_suraksha.html");
            dest_file_path = "poorn_suraksha_brochure.pdf";
            download_file_url = "https://www.sbilife.co.in/poorna-suraksha-brochure";
        } else if (productName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_smart_samriddhi))) {
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - "
                    + getString(R.string.sbi_life_smart_samriddhi) + "</u>"));
            txtHeaderUIN.setText("(UIN:" + getString(R.string.sbi_life_smart_samriddhi_uin) + ")");
            txtHeaderPCode.setText("(Product Code : " + getString(R.string.sbi_life_smart_samriddhi_code) + ")");
            webview.loadUrl("file:///android_asset/smartsamriddhi.html");
            dest_file_path = getString(R.string.sbi_life_smart_samriddhi_brochure_pdf);
            download_file_url = getString(R.string.sbiLifeDownloadLink) + getString(R.string.sbi_life_smart_samriddhi_brochure_link);

        } else if (productName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_saral_insure_wealth_plus))) {

            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - "
                    + getString(R.string.sbi_life_saral_insure_wealth_plus) + "</u>"));
            txtHeaderUIN.setText("(UIN:" + getString(R.string.sbi_life_saral_insure_wealth_plus_uin) + ")");
            txtHeaderPCode.setText("(Product Code : " + getString(R.string.sbi_life_saral_insure_wealth_plus_code) + ")");

            webview.loadUrl("file:///android_asset/saral_insure_wealth_plus.html");

            dest_file_path = getString(R.string.sbi_life_saral_insure_wealth_brochure_pdf);
            download_file_url = getString(R.string.sbiLifeDownloadLink) + getString(R.string.sbi_life_saral_insure_wealth_brochure_link);

        } else if (productName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_smart_insure_wealth_plus))) {
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - "
                    + getString(R.string.sbi_life_smart_insure_wealth_plus) + "</u>"));
            txtHeaderUIN.setText("(UIN:" + getString(R.string.sbi_life_smart_insure_wealth_plus_uin) + ")");
            txtHeaderPCode.setText("(Product Code : " + getString(R.string.sbi_life_smart_insure_wealth_plus_code) + ")");

            webview.loadUrl("file:///android_asset/smart_insure_wealth_plus.html");
            dest_file_path = getString(R.string.sbi_life_smart_insure_wealth_brochure_pdf);
            download_file_url = getString(R.string.sbiLifeDownloadLink) + getString(R.string.sbi_life_smart_samriddhi_brochure_link);
        } else if (productName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_smart_platina_assure))) {
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - "
                    + getString(R.string.sbi_life_smart_platina_assure) + "</u>"));
            txtHeaderUIN.setText("(UIN:" + getString(R.string.sbi_life_smart_platina_assure_uin) + ")");
            txtHeaderPCode.setText("(Product Code : " + getString(R.string.sbi_life_smart_platina_assure_code) + ")");

            webview.loadUrl("file:///android_asset/platina.html");
            dest_file_path = getString(R.string.sbi_life_smart_platina_assure_brochure_pdf);
            download_file_url = getString(R.string.sbiLifeDownloadLink) + getString(R.string.sbi_life_smart_platina_assure_brochure_link);
        } else if (productName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_smart_future_choices))) {
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - "
                    + getString(R.string.sbi_life_smart_future_choices) + "</u>"));
            txtHeaderUIN.setText("(UIN:" + getString(R.string.sbi_life_smart_future_choices_uin) + ")");
            txtHeaderPCode.setText("(Product Code : " + getString(R.string.sbi_life_smart_future_choices_code) + ")");

            webview.loadUrl("file:///android_asset/smart_furure_choice.html");
            dest_file_path = getString(R.string.sbi_life_smart_future_choices_brochure_pdf);
            download_file_url = getString(R.string.sbiLifeDownloadLink) + getString(R.string.sbi_life_smart_future_choices_brochure_link);
        } else if (productName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_saral_jeevan_bima))) {
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - "
                    + getString(R.string.sbi_life_saral_jeevan_bima) + "</u>"));
            txtHeaderUIN.setText("(UIN:" + getString(R.string.sbi_life_saral_jeevan_bima_uin) + ")");
            txtHeaderPCode.setText("(Product Code : " + getString(R.string.sbi_life_saral_jeevan_bima_code) + ")");

            webview.loadUrl("file:///android_asset/saral_jeevan_beema.html");
            dest_file_path = getString(R.string.sbi_life_saral_jeevan_bima_brochure_pdf);
            download_file_url = getString(R.string.sbiLifeDownloadLink) + getString(R.string.sbi_life_saral_jeevan_bima_brochure_link);
        } else if (productName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_new_smart_samriddhi))) {
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - "
                    + getString(R.string.sbi_life_new_smart_samriddhi) + "</u>"));
            txtHeaderUIN.setText("(UIN:" + getString(R.string.sbi_life_new_smart_samriddhi_uin) + ")");
            txtHeaderPCode.setText("(Product Code : " + getString(R.string.sbi_life_new_smart_samriddhi_code) + ")");

            webview.loadUrl("file:///android_asset/newsmartsamriddhi.html");
            dest_file_path = getString(R.string.sbi_life_new_smart_samriddhi_brochure_pdf);
            download_file_url = getString(R.string.sbiLifeDownloadLink) + getString(R.string.sbi_life_new_smart_samriddhi_brochure_link);
        }else if (productName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_saral_pension_new))) {
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - "
                    + getString(R.string.sbi_life_saral_pension_new) + "</u>"));
            txtHeaderUIN.setText("(UIN:" + getString(R.string.sbi_life_saral_pension_new_uin) + ")");
            txtHeaderPCode.setText("(Product Code : " + getString(R.string.sbi_life_saral_pension_new_code) + ")");

            webview.loadUrl("file:///android_asset/saral_pension.html");
            dest_file_path = getString(R.string.sbi_life_saral_pension_new_brochure_pdf);
            download_file_url = getString(R.string.sbiLifeDownloadLink) + getString(R.string.sbi_life_saral_pension_new_brochure_link);
        }else if (productName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_eshield_next))) {
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - "
                    + getString(R.string.sbi_life_eshield_next) + "</u>"));
            txtHeaderUIN.setText("(UIN:" + getString(R.string.sbi_life_eshield_next_uin) + ")");
            txtHeaderPCode.setText("(Product Code : " + getString(R.string.sbi_life_eshield_next_code) + ")");

            webview.loadUrl("file:///android_asset/eShieldNext.html");
            dest_file_path = getString(R.string.sbi_life_eshield_next_brochure_pdf);
            download_file_url = getString(R.string.sbiLifeDownloadLink) +
                    getString(R.string.sbi_life_eshield_next_brochure_link);
        }else if (productName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_smart_platina_plus))) {
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - "
                    + getString(R.string.sbi_life_smart_platina_plus) + "</u>"));
            txtHeaderUIN.setText("(UIN:" + getString(R.string.sbi_life_smart_platina_plus_uin) + ")");
            txtHeaderPCode.setText("(Product Code : " + getString(R.string.sbi_life_smart_platina_plus_code) + ")");

            webview.loadUrl("file:///android_asset/SmartPlatinaPlus.html");
            dest_file_path = getString(R.string.sbi_life_smart_platina_plus_brochure_pdf);
            download_file_url = getString(R.string.sbiLifeDownloadLink) +
                    getString(R.string.sbi_life_smart_platina_plus_brochure_link);
        }

        // webview.getSettings().setBuiltInZoomControls(true);
        // WebSettings zoomenable = webview.getSettings();
        // zoomenable.setBuiltInZoomControls(true);

		/*WebSettings wideviewenable = webview.getSettings();
		wideviewenable.setUseWideViewPort(true);*/

		/*webview.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        webview.getSettings().setSupportZoom(true);  
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setUseWideViewPort(true);*/

        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);


        OnTouchListener txttouch = new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    String headerName = txtHeaderName.getText().toString();

                    if (headerName.equals("SBI Life - Smart Performer")) {


                    } else if (headerName.equals("SBI Life - Unit Plus Super")) {

                    } else if (headerName.equals("SBI Life - Saral Maha Anand")) {

                        Intent intent = new Intent(context, BI_SaralMahaAnandActivity.class);
                        startActivity(intent);


                    } else if (headerName.equals("SBI Life - Smart Elite")) {

                        Intent intent = new Intent(context, BI_SmartEliteActivity.class);
                        startActivity(intent);


                    } else if (headerName.equals("SBI Life - Smart Scholar")) {

                        Intent intent = new Intent(context, BI_SmartScholarActivity.class);
                        startActivity(intent);


                    } else if (headerName.equals("SBI Life - Smart Horizon")) {

                    } else if (headerName.equals("SBI Life - Smart Power Insurance")) {

                        Intent intent = new Intent(context, BI_SmartPowerInsuranceActivity.class);
                        startActivity(intent);


                    } else if (headerName.equals("SBI Life - Smart Wealth Assure")) {

                        Intent intent = new Intent(context, BI_SmartWealthAssureActivity.class);
                        startActivity(intent);

                    } else if (headerName.equals("SBI Life - Smart Wealth Builder")) {

                        Intent intent = new Intent(context, BI_SmartWealthBuilderActivity.class);
                        startActivity(intent);

                    } else if (headerName.equals("SBI Life - Saral Swadhan+")) {

                        Intent intent = new Intent(context, BI_SaralSwadhanPlusActivity.class);
                        startActivity(intent);

                    } else if (headerName.equals("SBI Life - Smart Women Advantage")) {

                        Intent intent = new Intent(context, BI_SmartWomenAdvantageActivity.class);
                        startActivity(intent);

                    }
                    /*
                     * Child Plans
                     */

                    else if (headerName.equals("SBI Life - Scholar II")) {

                    } else if (headerName.equals("SBI Life - Smart Champ Insurance")) {

                        Intent intent = new Intent(context, BI_SmartChampActivity.class);
                        startActivity(intent);
                    }

                    /*
                     * Pension Plans
                     */
                    else if (headerName.equals("SBI Life - Annuity Plus")) {

                        Intent intent = new Intent(context, AnnuityPlusActivity.class);
                        startActivity(intent);

                    } else if (headerName.equals("SBI Life - Retire Smart")) {

                        Intent intent = new Intent(context, BI_RetireSmartActivity.class);
                        startActivity(intent);

                    } else if (headerName.equals("SBI Life - Saral Retirement Saver")) {

                        Intent intent = new Intent(context, BI_SaralPensionActivity.class);
                        startActivity(intent);

                    }

                    /*
                     * Protection Plans
                     */

                    else if (headerName.equals("SBI Life - Smart Income Shield Insurance")) {

                    } else if (headerName.equals("SBI Life - Saral Shield")) {

                        Intent intent = new Intent(context, BI_SaralShieldActivity.class);
                        startActivity(intent);


                    } else if (headerName.equals("SBI Life - Swadhan")) {

                    } else if (headerName.equals("SBI Life - Smart Shield")) {

                        Intent intent = new Intent(context, BI_SmartShieldActivity.class);
                        startActivity(intent);


                    } else if (headerName.equals("SBI Life - eShield")) {

                    } else if (headerName.equals("SBI Life - Grameen Bima")) {

                    }

                    /*
                     * Saving Plans
                     */

                    else if (headerName.equals("SBI Life - Smart Income Protect")) {

                        Intent intent = new Intent(context, BI_SmartIncomeProtectActivity.class);
                        startActivity(intent);

                    } else if (headerName.equals("SBI Life - Smart Money Back Insurance")) {

                    } else if (headerName.equals("SBI Life - Flexi Smart Plus")) {

                        Intent intent = new Intent(context, BI_FlexiSmartPlusActivity.class);
                        startActivity(intent);


                    } else if (headerName.equals("SBI Life - Smart Money Back Gold")) {

                        Intent intent = new Intent(context, BI_SmartMoneyBackGoldActivity.class);
                        startActivity(intent);


                    } else if (headerName.equals("SBI Life - Sanjeevan Supreme")) {

                    } else if (headerName.equals("SBI Life - Saral Swadhan+")) {

                    } else if (headerName.equals("SBI Life - Shubh Nivesh")) {

                        Intent intent = new Intent(context, BI_ShubhNiveshActivity.class);
                        startActivity(intent);

                    } else if (headerName.equals("SBI Life - Saral Life")) {

                    } else if (headerName.equals("SBI Life - Smart Guaranteed Savings Plan")) {

                        Intent intent = new Intent(context, BI_SmartGuaranteedSavingsPlanActivity.class);
                        startActivity(intent);

                    } else if (headerName.equals("SBI Life - Smart Money Planner")) {

						/* Intent intent = new Intent(context, SmartGuaranteedSavingsPlanActivity.class);
            			                 		startActivity(intent);*/

                    } else if (headerName.equals("SBI Life - Smart Humsafar")) {

                        Intent intent = new Intent(context, BI_SmartHumsafarActivity.class);
                        startActivity(intent);

                    } else if (headerName.equals("SBI Life - Smart Swadhan Plus")) {

                        Intent intent = new Intent(context, BI_SmartSwadhanPlusActivity.class);
                        startActivity(intent);

                    }
                    /*
                     * Health Plans
                     */

                    else if (headerName.equals("SBI Life - Hospital Cash")) {

                    } else if (headerName.equals("SBI Life - Group Criti 9")) {

                    } else if (headerName.equals("SBI Life - Smart Health Insurance")) {

                    }
                    //Group plan
                    /*changes starts by rajan 15-02-2018*/
                    else if (headerName.equals("SBI Life - RiNn Raksha(SBG) - LPPT")) {
                        Intent intent = new Intent(context, RinnRakshaActivity.class);
                        intent.putExtra("RIN_RAKSHA_PREMIUM_MODE", "LPPT");
                        startActivity(intent);
                    } else if (headerName.equals("SBI Life - RiNn Raksha(SBG) - Single")) {
                        Intent intent = new Intent(context, RinnRakshaSingleActivity.class);
                        intent.putExtra("RIN_RAKSHA_PREMIUM_MODE", "Single");
                        startActivity(intent);
                    }
                    /*changes end by rajan 15-02-2018*/
                    else if (headerName.equalsIgnoreCase("SBI Life - Sampoorn Cancer Suraksha")) {
                        Intent intent = new Intent(context, BI_SampoornCancerSurakshaActivity.class);
                        startActivity(intent);
                    } else if (headerName.equalsIgnoreCase("SBI Life - Poorna Suraksha")) {
                        Intent intent = new Intent(context, BI_PoornSurakshaActivity.class);
                        startActivity(intent);
                    } else if (headerName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_smart_samriddhi))) {
                        Intent intent = new Intent(context, BI_SmartSamriddhiActivity.class);
                        startActivity(intent);
                    } else if (headerName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_saral_insure_wealth_plus))) {

                        String userType = commonMethods.GetUserType(context);

                        if ((userType.equalsIgnoreCase("AGENT")
                                || userType.equalsIgnoreCase("BAP")
                                || userType.equalsIgnoreCase("IMF"))) {
                            commonMethods.showMessageDialog(context, "You are not applicable for this Product");
                            return false;
                        }
                        Intent intent = new Intent(context, BI_SaralInsureWealthPlusActivity.class);
                        startActivity(intent);
                    } else if (headerName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_smart_insure_wealth_plus))) {
                        String userType = commonMethods.GetUserType(context);
                        if ((userType.equalsIgnoreCase("CAG")
                                || userType.equalsIgnoreCase("CIF"))) {
                            commonMethods.showMessageDialog(context, "You are not applicable for this Product");
                            return false;
                        }
                        Intent intent = new Intent(context, BI_SmartInsureWealthPlusActivity.class);
                        startActivity(intent);
                    } else if (headerName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_smart_platina_assure))) {
                        Intent intent = new Intent(context, BI_SmartPlatinaAssureActivity.class);
                        startActivity(intent);
                    }

                }
                return false;
            }
        };
        txtHeaderName.setOnTouchListener(txttouch);

        btnBrochure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commonMethods.isNetworkConnected(context)) {
                    setContentView(tv_loading);
                    tv_loading.setGravity(Gravity.CENTER);
                    tv_loading.setTypeface(null, Typeface.BOLD);
                    //downloadAndOpenPDF();
                    service_hits();
                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }
            }
        });

    }

    private void service_hits() {

        CommonMethods.UserDetailsValuesModel userDetails = commonMethods
                .setUserDetails(context);

        service = new ServiceHits(this,
                productName, dest_file_path, userDetails.getStrCIFBDMUserId(),
                userDetails.getStrCIFBDMEmailId(), userDetails.getStrCIFBDMMObileNo(),
                commonMethods.GetUserPassword(context), this);

        service.execute();

    }

    private void downloadAndOpenPDF() {
        new Thread(new Runnable() {
            public void run() {
                try {

                    File mFile = downloadFile(download_file_url);

                    if (mFile != null) {
                        commonMethods.openAllDocs(context, mFile);
                    } else {
                        commonMethods.showToast(context, "Blank file error!!");
                    }

					/*Intent intent = new Intent(Intent.ACTION_VIEW);
					Uri path = null;
					//nought changes
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
						path = commonMethods.getContentUri(context, );
						intent.setDataAndType(path, "application/pdf");
						intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

					}else{
						path = Uri.fromFile(downloadFile(download_file_url));
						intent.setDataAndType(path, "application/pdf");
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					}

					// validate that the device can open your File!
					PackageManager pm = context.getPackageManager();
					if (intent.resolveActivity(pm) != null) {
						startActivity(intent);
					}else{
						commonMethods.showToast(context, "Unable to open File");
					}*/

                } catch (IOException e) {
                    //tv_loading.setError("Something went wrong");
                    //commonMethods.showToast(con);
                    commonMethods.printLog("Open PDF : ", e.getMessage());
                }
            }
        }).start();

    }

    private File downloadFile(String dwnload_file_path) {
        File file = null;
        try {

            URL url = new URL(dwnload_file_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");

            /*
             * urlConnection.setReadTimeout(10000); // millis
             * urlConnection.setConnectTimeout(15000); // millis
             */

            urlConnection.setDoOutput(true);

            // connect
            urlConnection.connect();

            // set the path where we want to save <span id="IL_AD5"
            // class="IL_AD">the file</span>
            //File SDCardRoot = Environment.getExternalStorageDirectory();
            // create a new file, to save the <span id="IL_AD11"
            // class="IL_AD">downloaded</span> file
            //file = new File(SDCardRoot, dest_file_path);

            file = new StorageUtils().createFileToAppSpecificDir(context, dest_file_path);

            FileOutputStream fileOutput = new FileOutputStream(file);

            // Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            // this is the total size of the file which we are
            // <span id="IL_AD12" class="IL_AD">downloading</span>
            int totalsize = urlConnection.getContentLength();
            setText("Starting PDF download...");

            // create a buffer...
            byte[] buffer = new byte[1024 * 1024];
            int bufferLength;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                float per = ((float) downloadedSize / totalsize) * 100;
                setText("Total PDF File size  : " + (totalsize / 1024)
                        + " KB\n\nDownloading PDF " + (int) per + "% complete");
            }
            // close the output stream when complete //
            fileOutput.close();
            setText("Download Complete. Open PDF Application installed in the device.");

        } catch (final MalformedURLException e) {
            setTextError("Some error occured. Press back and try again",
                    Color.RED);
        } catch (final IOException e) {
            setTextError("Some error occured. Press back and try again.",
                    Color.RED);
        } catch (final Exception e) {
            setTextError("Failed to download image. Please check your internet connection.",
                    Color.RED);
        }
        return file;
    }

    private void setTextError(final String message, final int color) {
        runOnUiThread(new Runnable() {
            public void run() {
                tv_loading.setTextColor(color);
                tv_loading.setText(message);
            }
        });

    }

    private void setText(final String txt) {
        runOnUiThread(new Runnable() {
            public void run() {
                tv_loading.setText(txt);
            }
        });

    }

    public void OnPremCalci(View v) {
        String headerName = txtHeaderName.getText().toString();

        if (headerName.equals("SBI Life - Smart Performer")) {


        } else if (headerName.equals("SBI Life - Unit Plus Super")) {

        } else if (headerName.equals("SBI Life - Saral Maha Anand")) {

            Intent intent = new Intent(context, BI_SaralMahaAnandActivity.class);
            startActivity(intent);


        } else if (headerName.equals("SBI Life - Smart Elite")) {

            Intent intent = new Intent(context, BI_SmartEliteActivity.class);
            startActivity(intent);


        } else if (headerName.equals("SBI Life - Smart Scholar")) {

            Intent intent = new Intent(context, BI_SmartScholarActivity.class);
            startActivity(intent);


        } else if (headerName.equals("SBI Life - Smart Horizon")) {

        } else if (headerName.equals("SBI Life - Smart Power Insurance")) {

            Intent intent = new Intent(context, BI_SmartPowerInsuranceActivity.class);
            startActivity(intent);


        } else if (headerName.equals("SBI Life - Smart Wealth Assure")) {

            Intent intent = new Intent(context, BI_SmartWealthAssureActivity.class);
            startActivity(intent);

        } else if (headerName.equals("SBI Life - Smart Wealth Builder")) {

            Intent intent = new Intent(context, BI_SmartWealthBuilderActivity.class);
            startActivity(intent);

        } else if (headerName.equals("SBI Life - Saral Swadhan+")) {

            Intent intent = new Intent(context, BI_SaralSwadhanPlusActivity.class);
            startActivity(intent);

        } else if (headerName.equals("SBI Life - Smart Women Advantage")) {

            Intent intent = new Intent(context, BI_SmartWomenAdvantageActivity.class);
            startActivity(intent);

        }
        /*
         * Child Plans
         */

        else if (headerName.equals("SBI Life - Scholar II")) {

        } else if (headerName.equals("SBI Life - Smart Champ Insurance")) {

            Intent intent = new Intent(context, BI_SmartChampActivity.class);
            startActivity(intent);
        }

        /*
         * Pension Plans
         */
        else if (headerName.equals("SBI Life - Annuity Plus")) {

            Intent intent = new Intent(context, AnnuityPlusActivity.class);
            startActivity(intent);

        } else if (headerName.equals("SBI Life - Retire Smart")) {

            Intent intent = new Intent(context, BI_RetireSmartActivity.class);
            startActivity(intent);

        } else if (headerName.equals("SBI Life - Saral Retirement Saver")) {

            Intent intent = new Intent(context, BI_SaralPensionActivity.class);
            startActivity(intent);

        }

        /*
         * Protection Plans
         */

        else if (headerName.equals("SBI Life - Smart Income Shield Insurance")) {

        } else if (headerName.equals("SBI Life - Saral Shield")) {

            Intent intent = new Intent(context, BI_SaralShieldActivity.class);
            startActivity(intent);


        } else if (headerName.equals("SBI Life - Swadhan")) {

        } else if (headerName.equals("SBI Life - Smart Shield")) {

            Intent intent = new Intent(context, BI_SmartShieldActivity.class);
            startActivity(intent);


        } else if (headerName.equals("SBI Life - eShield")) {

        } else if (headerName.equals("SBI Life - Grameen Bima")) {

        }

        /*
         * Saving Plans
         */

        else if (headerName.equals("SBI Life - Smart Income Protect")) {

            Intent intent = new Intent(context, BI_SmartIncomeProtectActivity.class);
            startActivity(intent);

        } else if (headerName.equals("SBI Life - Flexi Smart Plus")) {

            Intent intent = new Intent(context, BI_FlexiSmartPlusActivity.class);
            startActivity(intent);


        } else if (headerName.equals("SBI Life - Smart Money Back Gold")) {

            Intent intent = new Intent(context, BI_SmartMoneyBackGoldActivity.class);
            startActivity(intent);


        } else if (headerName.equals("SBI Life - Sanjeevan Supreme")) {

        } else if (headerName.equals("SBI Life - Shubh Nivesh")) {

            Intent intent = new Intent(context, BI_ShubhNiveshActivity.class);
            startActivity(intent);

        } else if (headerName.equals("SBI Life - Saral Life")) {

        } else if (headerName.equals("SBI Life - Smart Guaranteed Savings Plan")) {

            Intent intent = new Intent(context, BI_SmartGuaranteedSavingsPlanActivity.class);
            startActivity(intent);

        } else if (headerName.equals("SBI Life - Smart Money Planner")) {

			/*Intent intent = new Intent(context, SmartGuaranteedSavingsPlanActivity.class);
			                 		startActivity(intent);*/
            Intent i1 = new Intent(context, BI_SmartMoneyPlannerActivity.class);
            startActivity(i1);

        } else if (headerName.equals("SBI Life - Smart Humsafar")) {

            Intent intent = new Intent(context, BI_SmartHumsafarActivity.class);
            startActivity(intent);

        } else if (headerName.equals("SBI Life - Smart Swadhan Plus")) {

            Intent intent = new Intent(context, BI_SmartSwadhanPlusActivity.class);
            startActivity(intent);

        }
        /*
         * Health Plans
         */

        else if (headerName.equals("SBI Life - Hospital Cash")) {

        } else if (headerName.equals("SBI Life - Group Criti 9")) {

        } else if (headerName.equals("SBI Life - Smart Health Insurance")) {

        }

        //Group plan
        /*changes starts by rajan 15-02-2018*/
        else if (headerName.equals("SBI Life - RiNn Raksha(SBG) - LPPT")) {
            Intent intent = new Intent(context, RinnRakshaActivity.class);
            intent.putExtra("RIN_RAKSHA_PREMIUM_MODE", "LPPT");
            startActivity(intent);
        } else if (headerName.equals("SBI Life - RiNn Raksha(SBG) - Single")) {
            Intent intent = new Intent(context, RinnRakshaSingleActivity.class);
            intent.putExtra("RIN_RAKSHA_PREMIUM_MODE", "Single");
            startActivity(intent);
        } else if (headerName.equalsIgnoreCase("SBI Life - Smart Privilege")) {
            Intent intent = new Intent(context, BI_SmartPrivilegeActivity.class);
            startActivity(intent);
        }
        /*changes end by rajan 15-02-2018*/
        else if (headerName.equalsIgnoreCase("SBI Life - Sampoorn Cancer Suraksha")) {
            Intent intent = new Intent(context, BI_SampoornCancerSurakshaActivity.class);
            startActivity(intent);
        } else if (headerName.equalsIgnoreCase("SBI Life - Poorna Suraksha")) {
            Intent intent = new Intent(context, BI_PoornSurakshaActivity.class);
            startActivity(intent);
        } else if (headerName.equalsIgnoreCase("SBI Life - Smart Bachat")) {
            Intent i1 = new Intent(getApplicationContext(),
                    BI_SmartBachatActivity.class);
            startActivity(i1);
        } else if (headerName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_smart_samriddhi))) {
            Intent intent = new Intent(context, BI_SmartSamriddhiActivity.class);
            startActivity(intent);
        } else if (headerName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_saral_insure_wealth_plus))) {

            String userType = commonMethods.GetUserType(context);

            if ((userType.equalsIgnoreCase("AGENT")
                    || userType.equalsIgnoreCase("BAP")
                    || userType.equalsIgnoreCase("IMF"))) {
                commonMethods.showMessageDialog(context, "You are not applicable for this Product");
                return;
            }
            Intent intent = new Intent(context, BI_SaralInsureWealthPlusActivity.class);
            startActivity(intent);
        } else if (headerName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_smart_insure_wealth_plus))) {
            String userType = commonMethods.GetUserType(context);
            if ((userType.equalsIgnoreCase("CAG")
                    || userType.equalsIgnoreCase("CIF"))) {
                commonMethods.showMessageDialog(context, "You are not applicable for this Product");
                return;
            }
            Intent intent = new Intent(context, BI_SmartInsureWealthPlusActivity.class);
            startActivity(intent);
        } else if (headerName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_smart_platina_assure))) {
            Intent intent = new Intent(context, BI_SmartPlatinaAssureActivity.class);
            startActivity(intent);
        } else if (headerName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_smart_future_choices))) {
            Intent intent = new Intent(context, BI_SmartFutureChoicesActivity.class);
            startActivity(intent);
        } else if (headerName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_saral_jeevan_bima))) {
            Intent intent = new Intent(context, BI_SaralJeevanBimaActivity.class);
            startActivity(intent);
        } else if (headerName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_new_smart_samriddhi))) {
            Intent intent = new Intent(context, BI_NewSmartSamriddhiActivity.class);
            startActivity(intent);
        }else if (headerName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_saral_pension_new))) {
            Intent intent = new Intent(context, BI_SaralPensionNewActivity.class);
            startActivity(intent);
        }else if (headerName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_eshield_next))) {
            Intent intent = new Intent(context, BI_EShieldNext.class);
            startActivity(intent);
        }else if (headerName.equalsIgnoreCase("SBI Life - " + getString(R.string.sbi_life_smart_platina_plus))) {
            Intent intent = new Intent(context, BI_SmartPlatinaPlusActivity.class);
            startActivity(intent);
        }
    }


    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

    }

    @Override
    public void onBackPressed() {
        String strUserType = commonMethods.GetUserType(context);
        if (strUserType.equalsIgnoreCase("Agent") || strUserType.equalsIgnoreCase("UM")) {
            Intent intent = new Intent(getApplicationContext(), CarouselProductActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(getApplicationContext(), BancaProductActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void downLoadData() {
        downloadAndOpenPDF();
    }
}