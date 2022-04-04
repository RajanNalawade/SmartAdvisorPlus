package sbilife.com.pointofsale_bancaagency.futureplanner;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;

@SuppressLint("ClickableViewAccessibility")
public class RetirementPlannerWebViewActivity extends AppCompatActivity {


    private String CIFcode;
    private String title = "";
    private String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.retiement_webview);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        Intent i = getIntent();
        title = i.getStringExtra("currentTab");

        // tv_title_cif_common = (TextView)findViewById(R.id.tv_commonTitle);
        // tv_title_cif_common.setText(title) ;
        DatabaseHelper dbhelper = new DatabaseHelper(this);
        username = dbhelper.GetUserName();
        new CommonMethods().setApplicationToolbarMenu(this, title);

        dbhelper = new DatabaseHelper(this);

        WebView webview1 = findViewById(R.id.wbvw_retirement);

        // getWindow().requestFeature(Window.FEATURE_PROGRESS);

        webview1.getSettings().setJavaScriptEnabled(true);
        webview1.setWebViewClient(new myWebClient());

        final AppCompatActivity activity = this;
        webview1.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different
                // scales.
                // The progress meter will automatically disappear when we reach
                // 100%
                activity.setProgress(progress * 1000);
            }
        });
        webview1.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                Toast.makeText(activity, "Oh no! " + description,
                        Toast.LENGTH_SHORT).show();
            }
        });

        Button btnProceed = findViewById(R.id.btn_retirement_next);
        // str_URL="http://172.17.134.181/childeduplanner/index1.jsp";
        // str_URL =
        // "http://agencyonline.sbilife.co.in/childeduplanner/index.jsp";

        try {
            DatabaseHelper dbObj = new DatabaseHelper(RetirementPlannerWebViewActivity.this);
            CIFcode = SimpleCrypto.decrypt("SBIL", dbObj.GetUserCode());

            System.out.println(" CIFcode string : " + CIFcode);
            String str = encriptRole(CIFcode + "|SmartAdvisor");
            System.out.println(CIFcode + "|SmartAdvisor"
                    + "  Encypted string : " + str);

            String str_URL = "https://agencyonline.sbilife.co.in/retirement/index.jsp?enc="
                    + str;
            webview1.loadUrl(str_URL);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        btnProceed.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                // Intent intent = new Intent(getApplicationContext(),
                // ChildPlannerListviewActivity.class);
                // intent.putExtra("CIF_Code", CIFcode);
                // startActivity(intent);

                Intent intentRetireLeads = new Intent(getBaseContext(),
                        RetirementPlannerListviewActivity.class);
                intentRetireLeads.putExtra("CIF_Code", CIFcode);
                intentRetireLeads.putExtra("currentTab", "Retirement Planner Leads");
                startActivity(intentRetireLeads);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(RetirementPlannerWebViewActivity.this,
                FuturePlannerActivity.class);
        startActivity(i);
        finish();
    }

    /*
     * public void onApplicationMenu(View v) { CreateMenuDialog objMenu=new
     * CreateMenuDialog(username,RetirementPlannerWebViewActivity.this);
     * objMenu.createMenu(v);
     *
     * }
     */

    private static String encriptRole(String sText) {
        String FinalEncriptedText = null;
        try {

            String key = "6LQ$%*)!(S{H>X";
            // user
            Cipher cipher1 = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] keyBytes1 = new byte[16];
            byte[] b = key.getBytes(StandardCharsets.UTF_8);
            int len = b.length;
            if (len > keyBytes1.length)
                len = keyBytes1.length;
            System.arraycopy(b, 0, keyBytes1, 0, len);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes1, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(keyBytes1);
            cipher1.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            byte[] results = cipher1.doFinal(sText.getBytes(StandardCharsets.UTF_8));

            // BASE64Encoder encoder = new BASE64Encoder();

            // System.out.println("Text to be encripted: " + sText );

            FinalEncriptedText = new String(Base64.encodeBase64(results));
            FinalEncriptedText = FinalEncriptedText.replace("+", "plus");

            // System.out.println(FinalEncriptedText);
        } catch (Exception e) {
            System.out.println("ERROR : IN THE Encription method ==> "
                    + e.getMessage());
            // e.printStackTrace();
        }

        return FinalEncriptedText;

    }

    class myWebClient extends WebViewClient {
        // ProgressDialog progressDialog;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
        }

    }


}
