package sbilife.com.pointofsale_bancaagency.common;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.R;

public class ProductInfo {

    private List<String[]> productList = null;
    private Context context;

    public ProductInfo() {
        setProductDetails();
    }

    public ProductInfo(Context context) {

        this.context = context;
        setProductDetails();
    }

    public String getProductCode(String productName) {
        String ProductCode = "";

        for (int i = 0; i < productList.size(); i++) {
            String[] result = productList.get(i);
            if (result[0].equals(productName))
                ProductCode = result[1];
        }
        return ProductCode;
    }

    public String getProductUIN(String productName) {
        String ProductUIN = "";

        for (int i = 0; i < productList.size(); i++) {
            String[] result = productList.get(i);
            if (result[0].equals(productName))
                ProductUIN = result[2];
        }
        return ProductUIN;
    }

    public String getProductType(String productName) {
        String ProductType = "";

        for (int i = 0; i < productList.size(); i++) {
            String[] result = productList.get(i);
            if (result[0].equals(productName))
                ProductType = result[3];
        }
        return ProductType;
    }

    public String getProductCategory(String productName) {
        String productCategory = "";

        for (int i = 0; i < productList.size(); i++) {
            String[] result = productList.get(i);
            if (result[0].equals(productName))
                productCategory = result[4];
        }
        return productCategory;
    }


    private void setProductDetails() {
        try {
            productList = new ArrayList<String[]>();
            productList.add(new String[]{""});
            productList.add(new String[]{"Shubh Nivesh", "35", "UIN:111N055V04", "TRADITION", "INDIVIDUAL PLAN"});
            //productList.add(new String[]{"Flexi Smart Plus", "1M", "UIN:111N093V01", "TRADITION", "INDIVIDUAL PLAN"});
            //productList.add(new String[]{"Smart Guaranteed Savings Plan", "1X","UIN:111N097V01","TRADITION","INDIVIDUAL PLAN"});
            productList.add(new String[]{"Saral Swadhan Plus", "1J", "UIN:111N092V03", "TRADITION", "INDIVIDUAL PLAN"});
            productList.add(new String[]{"Smart Shield", "45", "UIN:111N067V07", "TRADITION", "INDIVIDUAL PLAN"});
            //productList.add(new String[]{"Saral Shield", "47", "UIN:111N066V03", "TRADITION", "INDIVIDUAL PLAN"});
            productList.add(new String[]{"Smart Money Back Gold", "1N", "UIN:111N096V03", "TRADITION", "INDIVIDUAL PLAN"});
            productList.add(new String[]{"Smart Champ Insurance", "1P", "UIN:111N098V03", "TRADITION", "INDIVIDUAL PLAN"});
            productList.add(new String[]{"Smart Income Protect", "1B", "UIN:111N085V04", "TRADITION", "INDIVIDUAL PLAN"});
            productList.add(new String[]{"Saral Retirement Saver", "1E", "UIN:111N088V03", "TRADITION", "INDIVIDUAL PLAN"});
            productList.add(new String[]{"Smart Humsafar", "1W", "UIN:111N103V03", "TRADITION", "INDIVIDUAL PLAN"});
            productList.add(new String[]{"Smart Swadhan Plus", "1Z", "UIN:111N104V02", "TRADITION", "INDIVIDUAL PLAN"});
            productList.add(new String[]{"Smart Women Advantage", "2C", "UIN:111N106V01", "TRADITION", "INDIVIDUAL PLAN"});
            productList.add(new String[]{"Smart Money Planner", "1R", "UIN:111N101V03", "TRADITION", "INDIVIDUAL PLAN"});
            productList.add(new String[]{"Annuity Plus", "22", "UIN:111N083V11", "TRADITION", "INDIVIDUAL PLAN"});
            productList.add(new String[]{"Smart Wealth Assure", "55", "UIN:111L077V03", "ULIP", "INDIVIDUAL PLAN"});
            productList.add(new String[]{"Smart Power Insurance", "1C", "UIN:111L090V02", "ULIP", "INDIVIDUAL PLAN"});
            productList.add(new String[]{"Retire Smart", "1H", "UIN:111L094V02", "ULIP", "INDIVIDUAL PLAN"});
            productList.add(new String[]{"Smart Wealth Builder", "1K", "UIN:111L095V03", "ULIP", "INDIVIDUAL PLAN"});
            productList.add(new String[]{"Smart Elite", "53", "UIN:111L072V04", "ULIP", "INDIVIDUAL PLAN"});
            productList.add(new String[]{"Smart Scholar", "51", "UIN:111L073V03", "ULIP", "INDIVIDUAL PLAN"});
            //productList.add(new String[]{"Saral Maha Anand", "50", "UIN:111L070V02", "ULIP", "INDIVIDUAL PLAN"});
            productList.add(new String[]{"Smart Privilege", "2B", "UIN:111L107V03", "ULIP", "INDIVIDUAL PLAN"});
            //***** Added by Priyanka Warekar - 11-01-2017 -  Start ****///
            productList.add(new String[]{"Smart Bachat", "2D", "UIN:111N108V03", "TRADITION", "INDIVIDUAL PLAN"});
            //***** Added by Priyanka Warekar - 11-01-2017 -  End ****///

            productList.add(new String[]{"Sampoorn Cancer Suraksha", "2E", "UIN:111N109V03", "TRADITION", "INDIVIDUAL PLAN"});
            //added by rajan 28-11-2017
            productList.add(new String[]{"Poorna Suraksha", "2F", "UIN:111N110V03", "TRADITION", "INDIVIDUAL PLAN"});
            // added by Tushar 11/10/2018
            //productList.add(new String[]{"Smart Samriddhi", "2G", "UIN:111N097V03", "TRADITION", "INDIVIDUAL PLAN"});
            // added by Tushar 21/11/2018


            productList.add(new String[]{context.getString(R.string.sbi_life_saral_insure_wealth_plus),
                    context.getString(R.string.sbi_life_saral_insure_wealth_plus_code),
                    "UIN:" + context.getString(R.string.sbi_life_saral_insure_wealth_plus_uin), "ULIP", "INDIVIDUAL PLAN"});

            productList.add(new String[]{context.getString(R.string.sbi_life_smart_insure_wealth_plus),
                    context.getString(R.string.sbi_life_smart_insure_wealth_plus_code), "UIN:" + context.getString(R.string.sbi_life_smart_insure_wealth_plus_uin), "ULIP", "INDIVIDUAL PLAN"});

            //added by Tushar 09/04/2019
            productList.add(new String[]{context.getString(R.string.sbi_life_smart_platina_assure),
                    context.getString(R.string.sbi_life_smart_platina_assure_code), "UIN:" + context.getString(R.string.sbi_life_smart_platina_assure_uin), "TRADITION", "INDIVIDUAL PLAN"});
            productList.add(new String[]{context.getString(R.string.sbi_life_smart_future_choices),
                    context.getString(R.string.sbi_life_smart_future_choices_code), "UIN:" + context.getString(R.string.sbi_life_smart_future_choices_uin), "TRADITION", "INDIVIDUAL PLAN"});

            productList.add(new String[]{context.getString(R.string.sbi_life_saral_jeevan_bima),
                    context.getString(R.string.sbi_life_saral_jeevan_bima_code),
                    "UIN:" + context.getString(R.string.sbi_life_saral_jeevan_bima_uin), "TRADITION", "INDIVIDUAL PLAN"});
            productList.add(new String[]{context.getString(R.string.sbi_life_new_smart_samriddhi),
                    context.getString(R.string.sbi_life_new_smart_samriddhi_code), "UIN:"
                    + context.getString(R.string.sbi_life_new_smart_samriddhi_uin), "TRADITION", "INDIVIDUAL PLAN"});

            productList.add(new String[]{context.getString(R.string.sbi_life_saral_pension_new),
                    context.getString(R.string.sbi_life_saral_pension_new_code), "UIN:"
                    + context.getString(R.string.sbi_life_saral_pension_new_uin), "TRADITION", "INDIVIDUAL PLAN"});

            productList.add(new String[]{context.getString(R.string.sbi_life_eshield_next),
                    context.getString(R.string.sbi_life_eshield_next_code), "UIN:"
                    + context.getString(R.string.sbi_life_eshield_next_uin), "TRADITION", "INDIVIDUAL PLAN"});

            productList.add(new String[]{context.getString(R.string.sbi_life_smart_platina_plus),
                    context.getString(R.string.sbi_life_smart_platina_plus_code), "UIN:"
                    + context.getString(R.string.sbi_life_smart_platina_plus_uin), "TRADITION", "INDIVIDUAL PLAN"});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
