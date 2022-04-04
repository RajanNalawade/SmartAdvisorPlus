package sbilife.com.pointofsale_bancaagency.needanalysis;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.annuityplus.BI_AnnuityPlusActivity;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
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
import sbilife.com.pointofsale_bancaagency.products.smartplatinaplus.BI_SmartPlatinaPlusActivity;
import sbilife.com.pointofsale_bancaagency.retiresmart.BI_RetireSmartActivity;
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

public class OthersProductListActivity extends AppCompatActivity {

    private Spinner spinnerOtherProductList;
    private String inputVal = "", outputlist = "", str_brochure_dest_file_path = "", selectedProduct = "",
            str_brochure_download_file_url = "";
    private CommonMethods mCommonMethods;
    private Context mContext;
    public static String URNNumber = "", groupName = "";

    private ProgressDialog mProgressDialog;
    private final String NAMESPACE = "http://tempuri.org/";
    private final String SOAP_ACTION_NA_CBI_UIN_NO = "http://tempuri.org/getUINNum";
    private final String METHOD_NAME_NA_CBI_UIN_NO = "getUINNum";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.product_list_other);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_custom_title_need_analysis);

        mContext = this;
        mCommonMethods = new CommonMethods();
        mCommonMethods.setApplicationToolbarMenu1(this, "Choose Product");

        Intent intent = getIntent();

        inputVal = intent.getStringExtra("NaInput");
        outputlist = intent.getStringExtra("NaOutput");
        URNNumber = intent.getStringExtra("URNNumber");
        groupName = intent.getStringExtra("group_name");

        String[] productList = {"Select Product", "Shubh Nivesh",
                "Saral Swadhan+",
                "Smart Shield", /*"Saral Shield",*/
                "Smart Money Back Gold", "Smart Champ Insurance", "Saral Retirement Saver",
                "Smart Swadhan Plus",
                "Smart Wealth Assure", "Smart Power Insurance", "Retire Smart",
                "Smart Wealth Builder", "Smart Elite", "Smart Scholar",
                "Smart Money Planner", "Smart Income Protect",
                "Smart Humsafar",/*getString(R.string.sbi_life_smart_samriddhi),
					"Saral Maha Anand",
				 "Smart Women Advantage",
				 "Flexi Smart Plus","Smart Guaranteed Savings Plan",*/
                "Smart Bachat",
                "Smart Privilege", "Sampoorn Cancer Suraksha", "Poorna Suraksha",

                getString(R.string.sbi_life_saral_insure_wealth_plus),
                getString(R.string.sbi_life_smart_insure_wealth_plus),
                getString(R.string.sbi_life_smart_platina_assure),
                getString(R.string.sbi_life_annuity_plus),
                getString(R.string.sbi_life_smart_future_choices),
                getString(R.string.sbi_life_saral_jeevan_bima),
                getString(R.string.sbi_life_new_smart_samriddhi),
                getString(R.string.sbi_life_saral_pension_new),
                getString(R.string.sbi_life_eshield_next),
                getString(R.string.sbi_life_smart_platina_plus)

        };

        spinnerOtherProductList = findViewById(R.id.spinnerOtherProductList);

        ArrayAdapter<String> otherProductListAdapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item, productList);
        otherProductListAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerOtherProductList.setAdapter(otherProductListAdapter);
        otherProductListAdapter.notifyDataSetChanged();

        spinnerOtherProductList.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
				/*String strProd = spinnerOtherProductList.getSelectedItem().toString();
				if(!strProd.equalsIgnoreCase("Select Product")){
					productListClicked(strProd);
				}*/


                //for Agent/BAP/CAG/IMF
                selectedProduct = spinnerOtherProductList.getSelectedItem().toString();
                if (!selectedProduct.equalsIgnoreCase("Select Product")) {

                    String strType = mCommonMethods.GetUserType(OthersProductListActivity.this);

                    if (strType.equalsIgnoreCase("BAP")
                            || strType.equalsIgnoreCase("CAG")
                            || strType.equalsIgnoreCase("IMF")) {
                        //for Agent/BAP/CAG/IMF
                        if (strType.equalsIgnoreCase("CAG")) {
                            if (selectedProduct.equalsIgnoreCase(getString(R.string.sbi_life_smart_insure_wealth_plus))) {
                                mCommonMethods.showMessageDialog(mContext, "You are not applicable for this Product");
                                return;
                            }
                        }

                        if (strType.equalsIgnoreCase("BAP")
                                || strType.equalsIgnoreCase("IMF")) {
                            if (selectedProduct.equalsIgnoreCase(getString(R.string.sbi_life_saral_insure_wealth_plus))) {
                                mCommonMethods.showMessageDialog(mContext, "You are not applicable for this Product");
                                return;
                            }
                        }
                        new AsynchURNService().execute();
                    } else {

                        if (strType.equalsIgnoreCase("CIF")) {
                            if (selectedProduct.equalsIgnoreCase(getString(R.string.sbi_life_smart_insure_wealth_plus))) {
                                mCommonMethods.showMessageDialog(mContext, "You are not applicable for this Product");
                                return;
                            }
                        }

                        if (strType.equalsIgnoreCase("AGENT")) {
                            if (selectedProduct.equalsIgnoreCase(getString(R.string.sbi_life_saral_insure_wealth_plus))) {
                                mCommonMethods.showMessageDialog(mContext, "You are not applicable for this Product");
                                return;
                            }
                        }
                        productListClicked(selectedProduct);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    private void productListClicked(String strProd) {
        Intent i = null;


        /***** Added by Priyanka Warekar - 10-11-2016 - start *****/
        strProd = strProd.replaceAll("[*#]", "");
        /***** Added by Priyanka Warekar - 10-11-2016 - end *****/
        if (strProd.equalsIgnoreCase("Smart Wealth Builder"))
            i = new Intent(this,
                    BI_SmartWealthBuilderActivity.class);
        else if (strProd.equalsIgnoreCase("Smart Elite"))
            i = new Intent(this,
                    BI_SmartEliteActivity.class);
        else if (strProd.equalsIgnoreCase("Shubh Nivesh"))
            i = new Intent(this,
                    BI_ShubhNiveshActivity.class);
        else if (strProd.equalsIgnoreCase("Flexi Smart Plus"))
            i = new Intent(this,
                    BI_FlexiSmartPlusActivity.class);
        else if (strProd
                .equalsIgnoreCase("Smart Guaranteed Savings Plan"))
            i = new Intent(this,
                    BI_SmartGuaranteedSavingsPlanActivity.class);
        else if (strProd.equalsIgnoreCase("Smart Women Advantage"))
            i = new Intent(this,
                    BI_SmartWomenAdvantageActivity.class);
        else if (strProd.equalsIgnoreCase("Smart Shield"))
            i = new Intent(this,
                    BI_SmartShieldActivity.class);
        else if (strProd.equalsIgnoreCase("Smart Scholar"))
            i = new Intent(this,
                    BI_SmartScholarActivity.class);
        else if (strProd.equalsIgnoreCase("Smart Money Back Gold"))
            i = new Intent(this,
                    BI_SmartMoneyBackGoldActivity.class);
        else if (strProd.equalsIgnoreCase("Smart Champ Insurance"))
            i = new Intent(this,
                    BI_SmartChampActivity.class);
        else if (strProd.equalsIgnoreCase("Retire Smart"))
            i = new Intent(this,
                    BI_RetireSmartActivity.class);
        else if (strProd.equalsIgnoreCase("Saral Shield"))
            i = new Intent(this,
                    BI_SaralShieldActivity.class);
        else if (strProd.equalsIgnoreCase("Smart Income Protect"))
            i = new Intent(this,
                    BI_SmartIncomeProtectActivity.class);
        else if (strProd.equalsIgnoreCase("Saral Maha Anand"))
            i = new Intent(this,
                    BI_SaralMahaAnandActivity.class);
        else if (strProd.equalsIgnoreCase("Smart Wealth Assure"))
            i = new Intent(this,
                    BI_SmartWealthAssureActivity.class);
        else if (strProd.equalsIgnoreCase("Saral Retirement Saver"))
            i = new Intent(this,
                    BI_SaralPensionActivity.class);
        else if (strProd.equalsIgnoreCase("Smart Power Insurance"))
            i = new Intent(this,
                    BI_SmartPowerInsuranceActivity.class);
        else if (strProd.equalsIgnoreCase("Smart Humsafar"))
            i = new Intent(this,
                    BI_SmartHumsafarActivity.class);
        else if (strProd.equalsIgnoreCase("Smart Swadhan Plus"))
            i = new Intent(this,
                    BI_SmartSwadhanPlusActivity.class);
        else if (strProd.equalsIgnoreCase("Smart Money Planner"))
            i = new Intent(this,
                    BI_SmartMoneyPlannerActivity.class);
        else if (strProd.equalsIgnoreCase("Saral Swadhan+"))
            i = new Intent(this,
                    BI_SaralSwadhanPlusActivity.class);
        else if (strProd.equalsIgnoreCase("Smart Bachat"))
            i = new Intent(this,
                    BI_SmartBachatActivity.class);

        else if (strProd.equalsIgnoreCase("Smart Privilege"))
            i = new Intent(this,
                    BI_SmartPrivilegeActivity.class);

        else if (strProd.equalsIgnoreCase("Sampoorn Cancer Suraksha"))
            i = new Intent(this,
                    BI_SampoornCancerSurakshaActivity.class);

        else if (strProd.equalsIgnoreCase("Poorna Suraksha"))
            i = new Intent(this,
                    BI_PoornSurakshaActivity.class);
        else if (strProd.equalsIgnoreCase(getString(R.string.sbi_life_smart_samriddhi)))
            i = new Intent(this,
                    BI_SmartSamriddhiActivity.class);
        else if (strProd.equalsIgnoreCase(getString(R.string.sbi_life_saral_insure_wealth_plus)))
            i = new Intent(this,
                    BI_SaralInsureWealthPlusActivity.class);
        else if (strProd.equalsIgnoreCase(getString(R.string.sbi_life_saral_insure_wealth_plus)))
            i = new Intent(this,
                    BI_SmartInsureWealthPlusActivity.class);
        else if (strProd.equalsIgnoreCase(getString(R.string.sbi_life_smart_platina_assure)))
            i = new Intent(this,
                    BI_SmartPlatinaAssureActivity.class);
        else if (strProd.equalsIgnoreCase(getString(R.string.sbi_life_annuity_plus)))
            i = new Intent(this,
                    BI_AnnuityPlusActivity.class);
        else if (strProd.equalsIgnoreCase(getString(R.string.sbi_life_smart_future_choices)))
            i = new Intent(this,
                    BI_SmartFutureChoicesActivity.class);
        else if (strProd.equalsIgnoreCase(getString(R.string.sbi_life_saral_jeevan_bima))) {
            i = new Intent(this,
                    BI_SaralJeevanBimaActivity.class);
        } else if (strProd.equalsIgnoreCase(getString(R.string.sbi_life_new_smart_samriddhi))) {
            i = new Intent(this,
                    BI_NewSmartSamriddhiActivity.class);
        } else if (strProd.equalsIgnoreCase(getString(R.string.sbi_life_saral_pension_new))) {
            i = new Intent(this,
                    BI_SaralPensionNewActivity.class);
        }else if (strProd.equalsIgnoreCase(getString(R.string.sbi_life_eshield_next))) {
            i = new Intent(this,
                    BI_EShieldNext.class);
        }else if (strProd.equalsIgnoreCase(getString(R.string.sbi_life_smart_platina_plus))) {
            i = new Intent(this,
                    BI_SmartPlatinaPlusActivity.class);
        }


        ParseXML prsObj = new ParseXML();
        String needAnalysis = "", gender = "", dob = "";
        String strType = mCommonMethods.GetUserType(mContext);

        if (strType.equalsIgnoreCase("BAP") || strType.equalsIgnoreCase("CAG")
                || strType.equalsIgnoreCase("IMF")) {
            //for Agent/BAP/CAG/IMF
            i.putExtra("NAFlag", "1");
            i.putExtra("custDOB", dob);
            i.putExtra("custGender", gender);
            i.putExtra("Other", "N");
            i.putExtra("NaInput", inputVal);
            i.putExtra("NaOutput", outputlist);
            startActivity(i);
        } else {
            needAnalysis = prsObj.parseXmlTag(inputVal, "needAnalysis");
            gender = prsObj.parseXmlTag(needAnalysis, "gender");
            dob = prsObj.parseXmlTag(needAnalysis, "dob");
            NeedAnalysisActivity.URN_NO = URNNumber;

            i.putExtra("NAFlag", "1");
            i.putExtra("custDOB", dob);
            i.putExtra("custGender", gender);
            i.putExtra("Other", "Y");
            i.putExtra("NaInput", inputVal);
            i.putExtra("NaOutput", outputlist);
            startActivity(i);
        }
    }

    //for Agent/BAP/CAG/IMF
    class AsynchURNService extends AsyncTask<String, String, String> {
        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {

            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>"
                    + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog
                    .setTitle(Html
                            .fromHtml("<font color='#00a1e3'><b>Please wait...<b></font>"));
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {

            try {
                running = true;

                SoapObject request;
                request = new SoapObject(NAMESPACE, METHOD_NAME_NA_CBI_UIN_NO);

                mCommonMethods.TLSv12Enable();
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);

                androidHttpTranport.call(SOAP_ACTION_NA_CBI_UIN_NO, envelope);

                SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();
                String inputpolicylist = sa.toString();

                if (Long.parseLong(inputpolicylist) > 0
                        && (inputpolicylist.length() == 10 || inputpolicylist
                        .length() == 12))
                    URNNumber = inputpolicylist;
                else
                    URNNumber = "";

            } catch (Exception e) {

                running = false;
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String unused) {

            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running == true) {

                if (!URNNumber.equals("")) {
                    NeedAnalysisActivity.URN_NO = URNNumber;
                    URNNumber = "";
                    //for Agent/BAP/CAG/IMF
                    show_product_details();

                } else {
                    mCommonMethods.showToast(mContext, "Invalid Valid URN Please Try Again");
                }
            } else {
                mCommonMethods.showToast(mContext, "Something Went Wrong While Generating URN");
            }
        }
    }

    //for Agent/BAP/CAG/IMF
    public void show_product_details() {

        final Dialog d = new Dialog(mContext,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.BLACK));
        d.setContentView(R.layout.productdesc_webview);
        TextView txtHeaderName = d.findViewById(R.id.txtHeaderName_wv);
        TextView txtHeaderUIN = d.findViewById(R.id.txtHeaderUIN_wv);
        TextView txtHeaderPCode = d.findViewById(R.id.txtHeaderPCode_wv);

        Button buttonProceed = d.findViewById(R.id.buttonProceed);
        Button btnBrochurePreCalsi = d.findViewById(R.id.btnBrochurePreCalsi);
        btnBrochurePreCalsi.setVisibility(View.GONE);

        Button btnBrochure = d.findViewById(R.id.btnBrochure);

        RelativeLayout relativeLayoutBrochure = d.findViewById(R.id.relativeLayoutBrochure);
        LinearLayout linearLayoutProceed = d.findViewById(R.id.linearLayoutProceed);

        linearLayoutProceed.setVisibility(View.VISIBLE);
        //linearLayoutBrochure.setVisibility(View.GONE);
        WebView webview = d.findViewById(R.id.webview);

        selectedProduct = selectedProduct.replaceAll("[*#]", "");

        if (selectedProduct.equalsIgnoreCase("Saral Maha Anand")) {
            txtHeaderName.setText(Html
                    .fromHtml("<u>SBI Life - Saral Maha Anand</u>"));
            txtHeaderUIN.setText("(UIN:111L070V02)");
            txtHeaderPCode.setText("(Product Code : 50)");

            webview.loadUrl("file:///android_asset/saral_maha_anand.html");

            str_brochure_dest_file_path = "saral_maha_anand_brochure.pdf";
            //str_brochure_download_file_url ="https://www.sbilife.co.in/saral-maha-anand-policy";
            str_brochure_download_file_url = "https://www.sbilife.co.in/saral-maha-anand-brochure";

        } else if (selectedProduct.equalsIgnoreCase("Smart Elite")) {
            txtHeaderName.setText(Html
                    .fromHtml("<u>SBI Life - Smart Elite</u>"));
            txtHeaderUIN.setText("(UIN:111L072V04)");
            txtHeaderPCode.setText("(Product Code : 53)");

            webview.loadUrl("file:///android_asset/smart_elite.html");

            str_brochure_dest_file_path = "smart_elite_brochure.pdf";
            str_brochure_download_file_url = "https://www.sbilife.co.in/smart-elite-brochure";

        } else if (selectedProduct.equalsIgnoreCase("Smart Scholar")) {
            txtHeaderName.setText(Html
                    .fromHtml("<u>SBI Life - Smart Scholar</u>"));
            txtHeaderUIN.setText("(UIN:111L073V03)");
            txtHeaderPCode.setText("(Product Code : 51)");

            webview.loadUrl("file:///android_asset/smart_scholar.html");

            str_brochure_dest_file_path = "smart_scholar_brochure.pdf";
            str_brochure_download_file_url = "https://www.sbilife.co.in/smart-scholar-brochure";

        } else if (selectedProduct.equalsIgnoreCase("Smart Bachat")) {
            txtHeaderName.setText(Html
                    .fromHtml("<u>SBI Life - Smart Bachat</u>"));
            txtHeaderUIN.setText("(UIN:111N108V03)");
            txtHeaderPCode.setText("(Product Code : 2D)");

            webview.loadUrl("file:///android_asset/smart_bachat.html");

            str_brochure_dest_file_path = "smart_bacaht_brochure_english.pdf";
            str_brochure_download_file_url = "https://www.sbilife.co.in/smart_bachat_brochure";

        } else if (selectedProduct.equalsIgnoreCase("Smart Power Insurance")) {
            txtHeaderName
                    .setText(Html
                            .fromHtml("<u>SBI Life - Smart Power Insurance</u>"));
            txtHeaderUIN.setText("(UIN:111L090V02)");
            txtHeaderPCode.setText("(Product Code : 1C)");

            webview.loadUrl("file:///android_asset/smart_power_insurance.html");

            str_brochure_dest_file_path = "smart_power_insurance_brochure.pdf";
            str_brochure_download_file_url = "https://www.sbilife.co.in/smart-power-insurance-brochure";

        } else if (selectedProduct.equalsIgnoreCase("Smart Wealth Assure")) {
            txtHeaderName.setText(Html
                    .fromHtml("<u>SBI Life - Smart Wealth Assure</u>"));
            txtHeaderUIN.setText("(UIN:111L077V03)");
            txtHeaderPCode.setText("(Product Code : 55)");

            webview.loadUrl("file:///android_asset/smart_wealth_assure.html");

            str_brochure_dest_file_path = "smart_wealth_assure_brochure.pdf";
            str_brochure_download_file_url = "https://www.sbilife.co.in/smart-wealth-assure-brochure";

        } else if (selectedProduct.equalsIgnoreCase("Smart Wealth Builder")) {
            txtHeaderName
                    .setText(Html
                            .fromHtml("<u>SBI Life - Smart Wealth Builder</u>"));
            txtHeaderUIN.setText("(UIN:111L095V03)");
            txtHeaderPCode.setText("(Product Code : 1K)");

            webview.loadUrl("file:///android_asset/smart_wealth_builder.html");

            str_brochure_dest_file_path = "smart_wealth_assure_brochure.pdf";
            str_brochure_download_file_url = "https://www.sbilife.co.in/smart-wealth-assure-brochure";

        } else if (selectedProduct.equalsIgnoreCase("Smart Women Advantage")) {
            txtHeaderName
                    .setText(Html
                            .fromHtml("<u>SBI Life - Smart Women Advantage</u>"));
            txtHeaderUIN.setText("(UIN:111N106V01)");
            txtHeaderPCode.setText("(Product Code : 2C)");

            webview.loadUrl("file:///android_asset/smart_women_advantage.html");

            str_brochure_dest_file_path = "smart_women_advantage_brochure.pdf";
            str_brochure_download_file_url = "https://www.sbilife.co.in/smart-women-advantage-brochure";

        } else if (selectedProduct.equalsIgnoreCase("Saral Swadhan+")) {
            txtHeaderName.setText(Html
                    .fromHtml("<u>SBI Life - Saral Swadhan+</u>"));
            txtHeaderUIN.setText("(UIN:111N092V03)");
            txtHeaderPCode.setText("(Product Code : 1J)");

            webview.loadUrl("file:///android_asset/saral_swadhan_plus.html");

            str_brochure_dest_file_path = "saral_swadhan_plus_brochure.pdf";
            str_brochure_download_file_url = "https://www.sbilife.co.in/saral_swadhan_plus_bro_pdf";

        } else if (selectedProduct.equalsIgnoreCase("Smart Champ Insurance")) {
            txtHeaderName
                    .setText(Html
                            .fromHtml("<u>SBI Life - Smart Champ Insurance</u>"));
            txtHeaderUIN.setText("(UIN:111N098V03)");
            txtHeaderPCode.setText("(Product Code : 1P)");

            webview.loadUrl("file:///android_asset/smart_champ.html");

            str_brochure_dest_file_path = "smart_champ_brochure.pdf";
            str_brochure_download_file_url = "https://www.sbilife.co.in/smart-champ-insurance-brochure";

        } else if (selectedProduct.equalsIgnoreCase("Retire Smart")) {
            txtHeaderName.setText(Html
                    .fromHtml("<u>SBI Life - Retire Smart</u>"));
            txtHeaderUIN.setText("(UIN:111L094V02)");
            txtHeaderPCode.setText("(Product Code : 1H)");

            webview.loadUrl("file:///android_asset/retire_smart.html");

            str_brochure_dest_file_path = "retire_smart_brochure.pdf";
            str_brochure_download_file_url = "https://www.sbilife.co.in/retire-smart-brochure";
        } else if (selectedProduct.equalsIgnoreCase("Saral Retirement Saver")) {
            txtHeaderName.setText(Html
                    .fromHtml("<u>SBI Life - Saral Retirement Saver</u>"));
            txtHeaderUIN.setText("(UIN:111N088V03)");
            txtHeaderPCode.setText("(Product Code : 1E)");

            webview.loadUrl("file:///android_asset/SaralRetirementSaver.html");

            str_brochure_dest_file_path = "saral_pension_brochure.pdf";
            str_brochure_download_file_url = "https://www.sbilife.co.in/saral-retirement-saver-brochure";

        } else if (selectedProduct.equalsIgnoreCase("Saral Shield")) {
            // txtHeaderName.setText("SBI Life - Saral Shield");
            txtHeaderName.setText(Html
                    .fromHtml("<u>SBI Life - Saral Shield</u>"));
            txtHeaderUIN.setText("(UIN:111N066V03)");
            txtHeaderPCode.setText("(Product Code : 47)");

            webview.loadUrl("file:///android_asset/saral_shield.html");

            str_brochure_dest_file_path = "saral_shield_brochure.pdf";
            str_brochure_download_file_url = "https://www.sbilife.co.in/saral-shield-brochure";

        } else if (selectedProduct.equalsIgnoreCase("Smart Shield")) {
            // txtHeaderName.setText("SBI Life - Smart Shield");
            txtHeaderName.setText(Html
                    .fromHtml("<u>SBI Life - Smart Shield</u>"));
            //111N067V02
            txtHeaderUIN.setText("(UIN:111N067V07)");
            txtHeaderPCode.setText("(Product Code : 45)");

            webview.loadUrl("file:///android_asset/smart_shield.html");

            str_brochure_dest_file_path = "saral_shield_brochure.pdf";
            str_brochure_download_file_url = "https://www.sbilife.co.in/saral-shield-brochure";

        } else if (selectedProduct.equalsIgnoreCase("Smart Income Protect")) {
            // txtHeaderName.setText("SBI Life - Smart Income Protect");
            txtHeaderName
                    .setText(Html
                            .fromHtml("<u>SBI Life - Smart Income Protect</u>"));
            txtHeaderUIN.setText("(UIN:111N085V04)");
            txtHeaderPCode.setText("(Product Code : 1B)");

            webview.loadUrl("file:///android_asset/smart_income_protect.html");

            str_brochure_dest_file_path = "smart_income_protect_brochure.pdf";
            str_brochure_download_file_url = "https://www.sbilife.co.in/smart-income-protect-brochure";

        } else if (selectedProduct.equalsIgnoreCase("Flexi Smart Plus")) {
            // txtHeaderName.setText("SBI Life - Flexi Smart Plus");
            txtHeaderName.setText(Html
                    .fromHtml("<u>SBI Life - Flexi Smart Plus</u>"));
            txtHeaderUIN.setText("(UIN:111N093V01)");
            txtHeaderPCode.setText("(Product Code : 1M)");

            webview.loadUrl("file:///android_asset/flexi_smart_plus.html");

            str_brochure_dest_file_path = "flexi_smart_plus_brochure.pdf";
            str_brochure_download_file_url = "https://www.sbilife.co.in/flexi-smart-plus-brochure";

        } else if (selectedProduct.equalsIgnoreCase("Smart Money Back Gold")) {
            // txtHeaderName.setText("SBI Life - Smart Money Back Gold");
            txtHeaderName
                    .setText(Html
                            .fromHtml("<u>SBI Life - Smart Money Back Gold</u>"));
            txtHeaderUIN.setText("(UIN:111N096V03)");
            txtHeaderPCode.setText("(Product Code : 1N)");

            webview.loadUrl("file:///android_asset/smart_money_back_gold.html");

            str_brochure_dest_file_path = "smart_money_back_gold_brochure.pdf";
            str_brochure_download_file_url = "https://www.sbilife.co.in/smart-money-back-gold-brochure";

        } else if (selectedProduct.equalsIgnoreCase("Shubh Nivesh")) {
            txtHeaderName.setText(Html
                    .fromHtml("<u>SBI Life - Shubh Nivesh</u>"));
            txtHeaderUIN.setText("(UIN:111N055V04)");
            txtHeaderPCode.setText("(Product Code : 35)");

            webview.loadUrl("file:///android_asset/shubh_nivesh.html");

            str_brochure_dest_file_path = "shubh_nivesh_brochure.pdf";
            str_brochure_download_file_url = "https://www.sbilife.co.in/shubh-nivesh";

        } else if (selectedProduct
                .equalsIgnoreCase("Smart Guaranteed Savings Plan")) {
            txtHeaderName
                    .setText(Html
                            .fromHtml("<u>SBI Life - Smart Guaranteed Savings Plan</u>"));
            txtHeaderUIN.setText("(UIN:111N097V01)");
            txtHeaderPCode.setText("(Product Code : 1X)");

            webview.loadUrl("file:///android_asset/smart_guaranteed.html");

            str_brochure_dest_file_path = "smart_guaranteed_brochure.pdf";
            str_brochure_download_file_url = "https://www.sbilife.co.in/smart-gauranteed-bro";

        } else if (selectedProduct.equalsIgnoreCase("Smart Money Planner")) {
            txtHeaderName.setText(Html
                    .fromHtml("<u>SBI Life - Smart Money Planner</u>"));
            txtHeaderUIN.setText("(UIN:111N101V03)");
            txtHeaderPCode.setText("(Product Code : 1R)");

            webview.loadUrl("file:///android_asset/smart_money_planner.html");

            str_brochure_dest_file_path = "smart_money_planner_brochure.pdf";
            str_brochure_download_file_url = "https://www.sbilife.co.in/smart-money-planner-brochure";

        } else if (selectedProduct.equalsIgnoreCase("Smart Humsafar")) {
            txtHeaderName.setText(Html
                    .fromHtml("<u>SBI Life - Smart Humsafar</u>"));
            txtHeaderUIN.setText("(UIN:111N103V03)");
            txtHeaderPCode.setText("(Product Code : 1W)");

            webview.loadUrl("file:///android_asset/smart_humsafar.html");

            str_brochure_dest_file_path = "smart_humsafar_brochure.pdf";
            str_brochure_download_file_url = "https://www.sbilife.co.in/smart-humsafar-brochure";
        } else if (selectedProduct.equalsIgnoreCase("Smart Swadhan Plus")) {
            txtHeaderName.setText(Html
                    .fromHtml("<u>SBI Life - Smart Swadhan Plus</u>"));
            txtHeaderUIN.setText("(UIN:111N104V02)");
            txtHeaderPCode.setText("(Product Code : 1Z)");

            webview.loadUrl("file:///android_asset/smart_swadhan_plus.html");

            str_brochure_dest_file_path = "smart_swadhan_plus_brochure.pdf";
            str_brochure_download_file_url = "https://www.sbilife.co.in/smart-swadhan-plus-brochure";
        } else if (selectedProduct.equalsIgnoreCase("Smart Privilege")) {
            txtHeaderName.setText(Html
                    .fromHtml("<u>SBI Life - Smart Privilege</u>"));
            txtHeaderUIN.setText("(UIN No - 111L107V03)");
            txtHeaderPCode.setText("(Product Code:2B)");
            webview.loadUrl("file:///android_asset/smart_privilege.html");

            str_brochure_dest_file_path = "smart_privilege_brochure.pdf";
            str_brochure_download_file_url = "https://www.sbilife.co.in/smart-privilege-brochure";
        } else if (selectedProduct.equalsIgnoreCase("Sampoorn Cancer Suraksha")) {
            //"2E", "UIN : 111N109V03",
            txtHeaderName.setText(Html
                    .fromHtml("<u>SBI Life - Sampoorn Cancer Suraksha</u>"));
            txtHeaderUIN.setText("(UIN No - 111N109V03)");
            txtHeaderPCode.setText("(Product Code:2E)");
            webview.loadUrl("file:///android_asset/sampoorn_cancer_suraksha.html");

            str_brochure_dest_file_path = "sampoorn_cancer_suraksha_brochure.pdf";
            str_brochure_download_file_url = "https://www.sbilife.co.in/Sampoorn-Cancer-Suraksha-brochure";
        } else if (selectedProduct.equalsIgnoreCase("Poorna Suraksha")) {
            txtHeaderName.setText(Html
                    .fromHtml("<u>SBI Life - Poorna Suraksha</u>"));
            txtHeaderUIN.setText("(UIN No - 111N110V03)");
            txtHeaderPCode.setText("(Product Code:2F)");

            webview.loadUrl("file:///android_asset/poorn_suraksha.html");
            str_brochure_dest_file_path = "poorna_suraksha_brochure.pdf";
            str_brochure_download_file_url = "https://www.sbilife.co.in/poorna-suraksha-brochure";
        } else if (selectedProduct.equalsIgnoreCase(getString(R.string.sbi_life_smart_samriddhi))) {
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - "
                    + getString(R.string.sbi_life_smart_samriddhi) + "</u>"));
            txtHeaderUIN.setText("(UIN:" + getString(R.string.sbi_life_smart_samriddhi_uin) + ")");
            txtHeaderPCode.setText("(Product Code : " + getString(R.string.sbi_life_smart_samriddhi_code) + ")");

            webview.loadUrl("file:///android_asset/smartsamriddhi.html");
            str_brochure_dest_file_path = getString(R.string.sbi_life_smart_samriddhi_brochure_pdf);
            str_brochure_download_file_url = getString(R.string.sbiLifeDownloadLink) + getString(R.string.sbi_life_smart_samriddhi_brochure_link);
        } else if (selectedProduct.equalsIgnoreCase(getString(R.string.sbi_life_saral_insure_wealth_plus))) {

            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - "
                    + getString(R.string.sbi_life_saral_insure_wealth_plus) + "</u>"));
            txtHeaderUIN.setText("(UIN:" + getString(R.string.sbi_life_saral_insure_wealth_plus_uin) + ")");
            txtHeaderPCode.setText("(Product Code : " + getString(R.string.sbi_life_saral_insure_wealth_plus_code) + ")");

            webview.loadUrl("file:///android_asset/saral_insure_wealth_plus.html");
            str_brochure_dest_file_path = getString(R.string.sbi_life_saral_insure_wealth_brochure_pdf);
            str_brochure_download_file_url = getString(R.string.sbiLifeDownloadLink) + getString(R.string.sbi_life_saral_insure_wealth_brochure_link);

        } else if (selectedProduct.equalsIgnoreCase(getString(R.string.sbi_life_smart_insure_wealth_plus))) {
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - "
                    + getString(R.string.sbi_life_smart_insure_wealth_plus) + "</u>"));
            txtHeaderUIN.setText("(UIN:" + getString(R.string.sbi_life_smart_insure_wealth_plus_uin) + ")");
            txtHeaderPCode.setText("(Product Code : " + getString(R.string.sbi_life_smart_insure_wealth_plus_code) + ")");

            webview.loadUrl("file:///android_asset/saral_insure_wealth_plus.html");
            str_brochure_dest_file_path = getString(R.string.sbi_life_smart_insure_wealth_brochure_pdf);
            str_brochure_download_file_url = getString(R.string.sbiLifeDownloadLink) + getString(R.string.sbi_life_smart_samriddhi_brochure_link);
        } else if (selectedProduct.equalsIgnoreCase(getString(R.string.sbi_life_smart_platina_assure))) {
            txtHeaderName.setText(Html.fromHtml("<u>SBI Life - "
                    + getString(R.string.sbi_life_smart_platina_assure) + "</u>"));
            txtHeaderUIN.setText("(UIN:" + getString(R.string.sbi_life_smart_platina_assure_uin) + ")");
            txtHeaderPCode.setText("(Product Code : " + getString(R.string.sbi_life_smart_platina_assure_code) + ")");

            webview.loadUrl("file:///android_asset/platina.html");
            str_brochure_dest_file_path = getString(R.string.sbi_life_smart_platina_assure_brochure_pdf);
            str_brochure_download_file_url = getString(R.string.sbiLifeDownloadLink) + getString(R.string.sbi_life_smart_platina_assure_brochure_link);
        } else if (selectedProduct.equalsIgnoreCase(getString(R.string.sbi_life_annuity_plus))) {
            txtHeaderName.setText(Html
                    .fromHtml("<u>SBI Life - " + getString(R.string.sbi_life_annuity_plus) + "</u>"));
            txtHeaderUIN.setText("(UIN No - " + getString(R.string.sbi_life_annuity_plus_uin) + ")");
            txtHeaderPCode.setText("(Product Code:" + getString(R.string.sbi_life_annuity_plus_code) + ")");


            webview.loadUrl("file:///android_asset/annuity_plus.html");
            str_brochure_dest_file_path = getString(R.string.sbi_life_annuity_plus_brochure_pdf);
            str_brochure_download_file_url = getString(R.string.sbiLifeDownloadLink) +
                    getString(R.string.sbi_life_annuity_plus_brochure_link);
        } else if (selectedProduct.equalsIgnoreCase(getString(R.string.sbi_life_smart_future_choices))) {
            txtHeaderName.setText(Html
                    .fromHtml("<u>SBI Life - " + getString(R.string.sbi_life_smart_future_choices) + "</u>"));
            txtHeaderUIN.setText("(UIN No - " + getString(R.string.sbi_life_smart_future_choices_uin) + ")");
            txtHeaderPCode.setText("(Product Code:" + getString(R.string.sbi_life_smart_future_choices_code) + ")");


            webview.loadUrl("file:///android_asset/smart_furure_choice.html");
            str_brochure_dest_file_path = getString(R.string.sbi_life_smart_future_choices_brochure_pdf);
            str_brochure_download_file_url = getString(R.string.sbiLifeDownloadLink) +
                    getString(R.string.sbi_life_smart_future_choices_brochure_link);
        } else if (selectedProduct.equalsIgnoreCase(getString(R.string.sbi_life_saral_jeevan_bima))) {
            txtHeaderName.setText(Html
                    .fromHtml("<u>SBI Life - " + getString(R.string.sbi_life_saral_jeevan_bima) + "</u>"));
            txtHeaderUIN.setText("(UIN No - " + getString(R.string.sbi_life_saral_jeevan_bima_uin) + ")");
            txtHeaderPCode.setText("(Product Code:" + getString(R.string.sbi_life_saral_jeevan_bima_code) + ")");


            webview.loadUrl("file:///android_asset/saral_jeevan_beema.html");
            str_brochure_dest_file_path = getString(R.string.sbi_life_saral_jeevan_bima_brochure_pdf);
            str_brochure_download_file_url = getString(R.string.sbiLifeDownloadLink) +
                    getString(R.string.sbi_life_saral_jeevan_bima_brochure_link);
        } else if (selectedProduct.equalsIgnoreCase(getString(R.string.sbi_life_new_smart_samriddhi))) {
            txtHeaderName.setText(Html
                    .fromHtml("<u>SBI Life - " + getString(R.string.sbi_life_new_smart_samriddhi) + "</u>"));
            txtHeaderUIN.setText("(UIN No - " + getString(R.string.sbi_life_new_smart_samriddhi_uin) + ")");
            txtHeaderPCode.setText("(Product Code:" + getString(R.string.sbi_life_new_smart_samriddhi_code) + ")");


            webview.loadUrl("file:///android_asset/newsmartsamriddhi.html");
            str_brochure_dest_file_path = getString(R.string.sbi_life_new_smart_samriddhi_brochure_pdf);
            str_brochure_download_file_url = getString(R.string.sbiLifeDownloadLink) +
                    getString(R.string.sbi_life_new_smart_samriddhi_brochure_link);
        } else if (selectedProduct.equalsIgnoreCase(getString(R.string.sbi_life_saral_pension_new))) {
            txtHeaderName.setText(Html
                    .fromHtml("<u>SBI Life - " + getString(R.string.sbi_life_saral_pension_new) + "</u>"));
            txtHeaderUIN.setText("(UIN No - " + getString(R.string.sbi_life_saral_pension_new_uin) + ")");
            txtHeaderPCode.setText("(Product Code:" + getString(R.string.sbi_life_saral_pension_new_code) + ")");


            webview.loadUrl("file:///android_asset/saral_pension.html");
            str_brochure_dest_file_path = getString(R.string.sbi_life_saral_pension_new_brochure_pdf);
            str_brochure_download_file_url = getString(R.string.sbiLifeDownloadLink) +
                    getString(R.string.sbi_life_saral_pension_new_brochure_link);
        }else if (selectedProduct.equalsIgnoreCase(getString(R.string.sbi_life_eshield_next))) {
            txtHeaderName.setText(Html
                    .fromHtml("<u>SBI Life - " + getString(R.string.sbi_life_eshield_next) + "</u>"));
            txtHeaderUIN.setText("(UIN No - " + getString(R.string.sbi_life_eshield_next_uin) + ")");
            txtHeaderPCode.setText("(Product Code:" + getString(R.string.sbi_life_eshield_next_code) + ")");


            webview.loadUrl("file:///android_asset/eShieldNext.html");
            str_brochure_dest_file_path = getString(R.string.sbi_life_eshield_next_brochure_pdf);
            str_brochure_download_file_url = getString(R.string.sbiLifeDownloadLink) +
                    getString(R.string.sbi_life_eshield_next_brochure_link);
        }else if (selectedProduct.equalsIgnoreCase(getString(R.string.sbi_life_smart_platina_plus))) {
            txtHeaderName.setText(Html
                    .fromHtml("<u>SBI Life - " + getString(R.string.sbi_life_smart_platina_plus) + "</u>"));
            txtHeaderUIN.setText("(UIN No - " + getString(R.string.sbi_life_smart_platina_plus_uin) + ")");
            txtHeaderPCode.setText("(Product Code:" + getString(R.string.sbi_life_smart_platina_plus_code) + ")");


            webview.loadUrl("file:///android_asset/SmartPlatinaPlus.html");
            str_brochure_dest_file_path = getString(R.string.sbi_life_smart_platina_plus_brochure_pdf);
            str_brochure_download_file_url = getString(R.string.sbiLifeDownloadLink) +
                    getString(R.string.sbi_life_smart_platina_plus_brochure_link);
        }

        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);

        d.show();
        linearLayoutProceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                d.dismiss();
                productListClicked(selectedProduct);
            }
        });
        buttonProceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                d.dismiss();
                productListClicked(selectedProduct);
            }
        });

        btnBrochure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();

                Intent i = new Intent(mContext, NABrochureActivity.class);
                i.putExtra("BROCHURE_URL", str_brochure_download_file_url);
                i.putExtra("BROCHURE_FILE_PATH", str_brochure_dest_file_path);
                startActivity(i);
            }
        });
    }

    void TempMethod(ParseXML prsObj, String resultNode) {

        String CODE, NAME, HEADING, FIELD_1, FIELD_2, NO_OF_IA, CDA_AMOUNT, REGION, DOJ, TOTAL_NB, TOTAL_RENEWALS, NB_REFUND,
                TOTAL_SURRENDER, FTR_ACHV, FTR_SCORE, AUTO_UNDERWRITING_ACHV, AUTO_UNDERWRITING_SCORE, NB_REFUND_ACHV,
                NB_REFUND_SCORE, PIWC_MISMATCH_ACHV, PIWC_MISMATCH_SCORE, ONLINE_PAYMENT_ACHV, ONLINE_PAYMENT_SCORE,
                DIGITAL_ADOPT_ACHV, DIGITAL_ADOPT_SCORE, EARLY_DEATH_CLAIM_ACHV, EARLY_DEATH_CLAIM_SCORE, MIS_SELL_ACHV,
                MIS_SELL_SCORE, PERSISTENCY_13, PERSISTENCY_13_SCORE, ALT_MODE_REG_ACHV, ALT_MODE_REG_SCORE, TOTAL_SCORE,
                REGIONAL_RANK, PAN_INDIA_RANK, MONTH, NOP, TENURE, PERSISTENCY_37, PERSISTENCY_37_SCORE, DEATH_CLAIM_AMOUNT,
                NB_NON_MED_TAT_ACHV, NB_NON_MED_TAT_SCORE, SMART_ADV_ADOP_ACHV, SMART_ADV_ADOP_SCORE, ESHIKSHA_USAGE_ACHV,
                ESHIKSHA_USAGE_SCORE, CUSTOMER_SELFSRV_ACHV, CUSTOMER_SELFSRV_SCORE, FLC_ISSUENCE_ACHV, FLC_ISSUENCE_SCORE,
                YTD_RENEWAL_ACHV, YTD_RENEWAL_SCORE, NPS_ACHV, NPS_SCORE, REQUIREMENT_PENDING_ACHV, REQUIREMENT_PENDING_SCORE,
                PERSISTENCY_61_SCORE, PERSISTENCY_61, PNR_COUNT_SCORE, PIWC_EFFICIENCY_ACHV, PIWC_EFFICIENCY_SCORE, UPLOAD_MONTH;
        CODE = prsObj.parseXmlTag(resultNode, "CODE");
        CODE = CODE == null ? "" : CODE;
        NAME = prsObj.parseXmlTag(resultNode, "NAME");
        NAME = NAME == null ? "" : NAME;

        HEADING = prsObj.parseXmlTag(resultNode, "HEADING");
        HEADING = HEADING == null ? "" : HEADING;

        FIELD_1 = prsObj.parseXmlTag(resultNode, "FIELD_1");
        FIELD_1 = FIELD_1 == null ? "" : FIELD_1;

        FIELD_2 = prsObj.parseXmlTag(resultNode, "FIELD_2");
        FIELD_2 = FIELD_2 == null ? "" : FIELD_2;

        NO_OF_IA = prsObj.parseXmlTag(resultNode, "NO_OF_IA");
        NO_OF_IA = NO_OF_IA == null ? "" : NO_OF_IA;

        CDA_AMOUNT = prsObj.parseXmlTag(resultNode, "CDA_AMOUNT");
        CDA_AMOUNT = CDA_AMOUNT == null ? "" : CDA_AMOUNT;

        REGION = prsObj.parseXmlTag(resultNode, "REGION");
        REGION = REGION == null ? "" : REGION;

        DOJ = prsObj.parseXmlTag(resultNode, "DOJ");
        DOJ = DOJ == null ? "" : DOJ;

        TOTAL_NB = prsObj.parseXmlTag(resultNode, "TOTAL_NB");
        TOTAL_NB = TOTAL_NB == null ? "" : TOTAL_NB;

        TOTAL_RENEWALS = prsObj.parseXmlTag(resultNode, "TOTAL_RENEWALS");
        TOTAL_RENEWALS = TOTAL_RENEWALS == null ? "" : TOTAL_RENEWALS;

        NB_REFUND = prsObj.parseXmlTag(resultNode, "NB_REFUND");
        NB_REFUND = NB_REFUND == null ? "" : NB_REFUND;

        TOTAL_SURRENDER = prsObj.parseXmlTag(resultNode, "TOTAL_SURRENDER");
        TOTAL_SURRENDER = TOTAL_SURRENDER == null ? "" : TOTAL_SURRENDER;

        FTR_ACHV = prsObj.parseXmlTag(resultNode, "FTR_ACHV");
        FTR_ACHV = FTR_ACHV == null ? "" : FTR_ACHV;

        FTR_SCORE = prsObj.parseXmlTag(resultNode, "FTR_SCORE");
        FTR_SCORE = FTR_SCORE == null ? "" : FTR_SCORE;

        AUTO_UNDERWRITING_ACHV = prsObj.parseXmlTag(resultNode, "AUTO_UNDERWRITING_ACHV");
        AUTO_UNDERWRITING_ACHV = AUTO_UNDERWRITING_ACHV == null ? "" : AUTO_UNDERWRITING_ACHV;

        AUTO_UNDERWRITING_SCORE = prsObj.parseXmlTag(resultNode, "AUTO_UNDERWRITING_SCORE");
        AUTO_UNDERWRITING_SCORE = AUTO_UNDERWRITING_SCORE == null ? "" : AUTO_UNDERWRITING_SCORE;

        NB_REFUND_ACHV = prsObj.parseXmlTag(resultNode, "NB_REFUND_ACHV");
        NB_REFUND_ACHV = NB_REFUND_ACHV == null ? "" : NB_REFUND_ACHV;

        NB_REFUND_SCORE = prsObj.parseXmlTag(resultNode, "NB_REFUND_SCORE");
        NB_REFUND_SCORE = NB_REFUND_SCORE == null ? "" : NB_REFUND_SCORE;

        PIWC_MISMATCH_ACHV = prsObj.parseXmlTag(resultNode, "PIWC_MISMATCH_ACHV");
        PIWC_MISMATCH_ACHV = PIWC_MISMATCH_ACHV == null ? "" : PIWC_MISMATCH_ACHV;

        PIWC_MISMATCH_SCORE = prsObj.parseXmlTag(resultNode, "PIWC_MISMATCH_SCORE");
        PIWC_MISMATCH_SCORE = PIWC_MISMATCH_SCORE == null ? "" : PIWC_MISMATCH_SCORE;

        ONLINE_PAYMENT_ACHV = prsObj.parseXmlTag(resultNode, "ONLINE_PAYMENT_ACHV");
        ONLINE_PAYMENT_ACHV = ONLINE_PAYMENT_ACHV == null ? "" : ONLINE_PAYMENT_ACHV;

        ONLINE_PAYMENT_SCORE = prsObj.parseXmlTag(resultNode, "ONLINE_PAYMENT_SCORE");
        ONLINE_PAYMENT_SCORE = ONLINE_PAYMENT_SCORE == null ? "" : ONLINE_PAYMENT_SCORE;

        DIGITAL_ADOPT_ACHV = prsObj.parseXmlTag(resultNode, "DIGITAL_ADOPT_ACHV");
        DIGITAL_ADOPT_ACHV = DIGITAL_ADOPT_ACHV == null ? "" : DIGITAL_ADOPT_ACHV;

        DIGITAL_ADOPT_SCORE = prsObj.parseXmlTag(resultNode, "DIGITAL_ADOPT_SCORE");
        DIGITAL_ADOPT_SCORE = DIGITAL_ADOPT_SCORE == null ? "" : DIGITAL_ADOPT_SCORE;

        EARLY_DEATH_CLAIM_ACHV = prsObj.parseXmlTag(resultNode, "EARLY_DEATH_CLAIM_ACHV");
        EARLY_DEATH_CLAIM_ACHV = EARLY_DEATH_CLAIM_ACHV == null ? "" : EARLY_DEATH_CLAIM_ACHV;

        EARLY_DEATH_CLAIM_SCORE = prsObj.parseXmlTag(resultNode, "EARLY_DEATH_CLAIM_SCORE");
        EARLY_DEATH_CLAIM_SCORE = EARLY_DEATH_CLAIM_SCORE == null ? "" : EARLY_DEATH_CLAIM_SCORE;

        MIS_SELL_ACHV = prsObj.parseXmlTag(resultNode, "MIS_SELL_ACHV");
        MIS_SELL_ACHV = MIS_SELL_ACHV == null ? "" : MIS_SELL_ACHV;

        MIS_SELL_SCORE = prsObj.parseXmlTag(resultNode, "MIS_SELL_SCORE");
        MIS_SELL_SCORE = MIS_SELL_SCORE == null ? "" : MIS_SELL_SCORE;

        PERSISTENCY_13 = prsObj.parseXmlTag(resultNode, "PERSISTENCY_13");
        PERSISTENCY_13 = PERSISTENCY_13 == null ? "" : PERSISTENCY_13;

        PERSISTENCY_13_SCORE = prsObj.parseXmlTag(resultNode, "PERSISTENCY_13_SCORE");
        PERSISTENCY_13_SCORE = PERSISTENCY_13_SCORE == null ? "" : PERSISTENCY_13_SCORE;

        ALT_MODE_REG_ACHV = prsObj.parseXmlTag(resultNode, "ALT_MODE_REG_ACHV");
        ALT_MODE_REG_ACHV = ALT_MODE_REG_ACHV == null ? "" : ALT_MODE_REG_ACHV;

        ALT_MODE_REG_SCORE = prsObj.parseXmlTag(resultNode, "ALT_MODE_REG_SCORE");
        ALT_MODE_REG_SCORE = ALT_MODE_REG_SCORE == null ? "" : ALT_MODE_REG_SCORE;

        TOTAL_SCORE = prsObj.parseXmlTag(resultNode, "TOTAL_SCORE");
        TOTAL_SCORE = TOTAL_SCORE == null ? "" : TOTAL_SCORE;

        REGIONAL_RANK = prsObj.parseXmlTag(resultNode, "REGIONAL_RANK");
        REGIONAL_RANK = REGIONAL_RANK == null ? "" : REGIONAL_RANK;

        PAN_INDIA_RANK = prsObj.parseXmlTag(resultNode, "PAN_INDIA_RANK");
        PAN_INDIA_RANK = PAN_INDIA_RANK == null ? "" : PAN_INDIA_RANK;

        MONTH = prsObj.parseXmlTag(resultNode, "MONTH");
        MONTH = MONTH == null ? "" : MONTH;

        NOP = prsObj.parseXmlTag(resultNode, "NOP");
        NOP = NOP == null ? "" : NOP;

        TENURE = prsObj.parseXmlTag(resultNode, "TENURE");
        TENURE = TENURE == null ? "" : TENURE;

        PERSISTENCY_37 = prsObj.parseXmlTag(resultNode, "PERSISTENCY_37");
        PERSISTENCY_37 = PERSISTENCY_37 == null ? "" : PERSISTENCY_37;

        PERSISTENCY_37_SCORE = prsObj.parseXmlTag(resultNode, "PERSISTENCY_37_SCORE");
        PERSISTENCY_37_SCORE = PERSISTENCY_37_SCORE == null ? "" : PERSISTENCY_37_SCORE;

        DEATH_CLAIM_AMOUNT = prsObj.parseXmlTag(resultNode, "DEATH_CLAIM_AMOUNT");
        DEATH_CLAIM_AMOUNT = DEATH_CLAIM_AMOUNT == null ? "" : DEATH_CLAIM_AMOUNT;

        NB_NON_MED_TAT_ACHV = prsObj.parseXmlTag(resultNode, "NB_NON_MED_TAT_ACHV");
        NB_NON_MED_TAT_ACHV = NB_NON_MED_TAT_ACHV == null ? "" : NB_NON_MED_TAT_ACHV;

        NB_NON_MED_TAT_SCORE = prsObj.parseXmlTag(resultNode, "NB_NON_MED_TAT_SCORE");
        NB_NON_MED_TAT_SCORE = NB_NON_MED_TAT_SCORE == null ? "" : NB_NON_MED_TAT_SCORE;

        SMART_ADV_ADOP_ACHV = prsObj.parseXmlTag(resultNode, "SMART_ADV_ADOP_ACHV");
        SMART_ADV_ADOP_ACHV = SMART_ADV_ADOP_ACHV == null ? "" : SMART_ADV_ADOP_ACHV;

        SMART_ADV_ADOP_SCORE = prsObj.parseXmlTag(resultNode, "SMART_ADV_ADOP_SCORE");
        SMART_ADV_ADOP_SCORE = SMART_ADV_ADOP_SCORE == null ? "" : SMART_ADV_ADOP_SCORE;

        ESHIKSHA_USAGE_ACHV = prsObj.parseXmlTag(resultNode, "ESHIKSHA_USAGE_ACHV");
        ESHIKSHA_USAGE_ACHV = ESHIKSHA_USAGE_ACHV == null ? "" : ESHIKSHA_USAGE_ACHV;

        ESHIKSHA_USAGE_SCORE = prsObj.parseXmlTag(resultNode, "ESHIKSHA_USAGE_SCORE");
        ESHIKSHA_USAGE_SCORE = ESHIKSHA_USAGE_SCORE == null ? "" : ESHIKSHA_USAGE_SCORE;

        CUSTOMER_SELFSRV_ACHV = prsObj.parseXmlTag(resultNode, "CUSTOMER_SELFSRV_ACHV");
        CUSTOMER_SELFSRV_ACHV = CUSTOMER_SELFSRV_ACHV == null ? "" : CUSTOMER_SELFSRV_ACHV;

        CUSTOMER_SELFSRV_SCORE = prsObj.parseXmlTag(resultNode, "CUSTOMER_SELFSRV_SCORE");
        CUSTOMER_SELFSRV_SCORE = CUSTOMER_SELFSRV_SCORE == null ? "" : CUSTOMER_SELFSRV_SCORE;

        FLC_ISSUENCE_ACHV = prsObj.parseXmlTag(resultNode, "FLC_ISSUENCE_ACHV");
        FLC_ISSUENCE_ACHV = FLC_ISSUENCE_ACHV == null ? "" : FLC_ISSUENCE_ACHV;

        FLC_ISSUENCE_SCORE = prsObj.parseXmlTag(resultNode, "FLC_ISSUENCE_SCORE");
        FLC_ISSUENCE_SCORE = FLC_ISSUENCE_SCORE == null ? "" : FLC_ISSUENCE_SCORE;

        YTD_RENEWAL_ACHV = prsObj.parseXmlTag(resultNode, "YTD_RENEWAL_ACHV");
        YTD_RENEWAL_ACHV = YTD_RENEWAL_ACHV == null ? "" : YTD_RENEWAL_ACHV;

        YTD_RENEWAL_SCORE = prsObj.parseXmlTag(resultNode, "YTD_RENEWAL_SCORE");
        YTD_RENEWAL_SCORE = YTD_RENEWAL_SCORE == null ? "" : YTD_RENEWAL_SCORE;

        NPS_ACHV = prsObj.parseXmlTag(resultNode, "NPS_ACHV");
        NPS_ACHV = NPS_ACHV == null ? "" : NPS_ACHV;

        NPS_SCORE = prsObj.parseXmlTag(resultNode, "NPS_SCORE");
        NPS_SCORE = NPS_SCORE == null ? "" : NPS_SCORE;

        REQUIREMENT_PENDING_ACHV = prsObj.parseXmlTag(resultNode, "REQUIREMENT_PENDING_ACHV");
        REQUIREMENT_PENDING_ACHV = REQUIREMENT_PENDING_ACHV == null ? "" : REQUIREMENT_PENDING_ACHV;

        REQUIREMENT_PENDING_SCORE = prsObj.parseXmlTag(resultNode, "REQUIREMENT_PENDING_SCORE");
        REQUIREMENT_PENDING_SCORE = REQUIREMENT_PENDING_SCORE == null ? "" : REQUIREMENT_PENDING_SCORE;

        PERSISTENCY_61_SCORE = prsObj.parseXmlTag(resultNode, "PERSISTENCY_61_SCORE");
        PERSISTENCY_61_SCORE = PERSISTENCY_61_SCORE == null ? "" : PERSISTENCY_61_SCORE;

        PERSISTENCY_61 = prsObj.parseXmlTag(resultNode, "PERSISTENCY_61");
        PERSISTENCY_61 = PERSISTENCY_61 == null ? "" : PERSISTENCY_61;

        PNR_COUNT_SCORE = prsObj.parseXmlTag(resultNode, "PNR_COUNT_SCORE");
        PNR_COUNT_SCORE = PNR_COUNT_SCORE == null ? "" : PNR_COUNT_SCORE;

        PIWC_EFFICIENCY_ACHV = prsObj.parseXmlTag(resultNode, "PIWC_EFFICIENCY_ACHV");
        PIWC_EFFICIENCY_ACHV = PIWC_EFFICIENCY_ACHV == null ? "" : PIWC_EFFICIENCY_ACHV;

        PIWC_EFFICIENCY_SCORE = prsObj.parseXmlTag(resultNode, "PIWC_EFFICIENCY_SCORE");
        PIWC_EFFICIENCY_SCORE = PIWC_EFFICIENCY_SCORE == null ? "" : PIWC_EFFICIENCY_SCORE;

        UPLOAD_MONTH = prsObj.parseXmlTag(resultNode, "UPLOAD_MONTH");
        UPLOAD_MONTH = UPLOAD_MONTH == null ? "" : UPLOAD_MONTH;
    }

}
