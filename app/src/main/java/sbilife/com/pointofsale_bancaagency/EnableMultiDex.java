package sbilife.com.pointofsale_bancaagency;

import android.content.Context;
import androidx.multidex.MultiDexApplication;

/**
 * Created by Eminosoft on 1/5/2017.
 */

public class EnableMultiDex extends MultiDexApplication {
    private static EnableMultiDex enableMultiDex;
    private static Context context;

    public EnableMultiDex(){
        enableMultiDex=this;
    }

    public static EnableMultiDex getEnableMultiDexApp() {
        return enableMultiDex;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

    }
}
