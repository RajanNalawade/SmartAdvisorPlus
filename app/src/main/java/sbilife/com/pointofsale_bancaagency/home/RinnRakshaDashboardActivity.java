package sbilife.com.pointofsale_bancaagency.home;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class RinnRakshaDashboardActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    private Context context;
    private CommonMethods commonMethods;
    private GenerateTokenAsync generateTokenAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_rinn_raksha_dashboard);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Rinn Raksha Dashboard");

        generateTokenAsync = new GenerateTokenAsync();
        generateTokenAsync.execute();

        //postData();
    }

    class GenerateTokenAsync extends AsyncTask<String, String, String> {
        private volatile boolean running = true;
        private String token = "";

        private final String URl = "https://maasaan.sbilife.co.in:8096/api/Appuser/desktopqlikticket?data=" + commonMethods.GetUserCode(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading Please wait...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);

            mProgressDialog.setButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            if (generateTokenAsync != null) {
                                generateTokenAsync.cancel(true);
                            }
                            if (mProgressDialog != null) {
                                if (mProgressDialog.isShowing()) {
                                    mProgressDialog.dismiss();
                                }
                            }
                        }
                    });

            mProgressDialog.setMax(100);
            mProgressDialog.show();

        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = false;


                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(URl);
                HttpResponse response = httpclient.execute(httppost);
                String responseString = new BasicResponseHandler().handleResponse(response);

                JSONObject jsonObject = new JSONObject(responseString);
                String value = jsonObject.getString("value");

                System.out.println("value:" + value);

                if (value != null && value == "true") {
                    token = jsonObject.getString("data");
                    running = true;
                    return token;
                }
            } catch (Exception e) {
                running = false;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (running) {
                /*WebView webview = findViewById(R.id.webview);
                webview.loadUrl("https://maasaan.sbilife.co.in/extensions/RinnRaksha_Live/RinnRaksha_Live.html?qlikTicket=" + token);*/
                commonMethods.openWebLink(context, "https://maasaan.sbilife.co.in/extensions/RinnRaksha_Live/RinnRaksha_Live.html?qlikTicket=" + token);
            } else {
                commonMethods.showMessageDialog(context, "Bad Request");
            }
        }

    }

    public void postData() {
        String URl = "https://maasaan.sbilife.co.in:8096/api/Appuser/desktopqlikticket?data=" + commonMethods.GetUserCode(context);
        // Create a new HttpClient and Post Header

    }
}
