package sbilife.com.pointofsale_bancaagency.common;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSharedPreferences {
    public String PERSISTENCY_KEY = "persistencyKey";

    public String getImeiNo() {
        String imeiNo = "imeiNo";
        return imeiNo;
    }

    public String getPhoneModelNo() {
        String phoneModelNo = "phoneModelNo";
        return phoneModelNo;
    }

    public static String getData(Context context, String key, String value) {
        try {
            SharedPreferences preferences = context.getSharedPreferences("SmartAdvisorPlus", 0);
            return preferences.getString(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }


    public static void setData(Context context, String key, String value) {
        try {
            SharedPreferences preferences = context.getSharedPreferences("SmartAdvisorPlus", 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
