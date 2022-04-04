package sbilife.com.pointofsale_bancaagency;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
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

import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.futureplanner.FuturePlannerActivity;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

@SuppressLint("ClickableViewAccessibility")
public class CarouselWebViewActivity extends AppCompatActivity {

	private ProgressDialog pd;
	private String CIFcode;

	private CommonMethods mCommonMethods;
	private String currentTab;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	  	//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.webview1);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

		mCommonMethods = new CommonMethods();
		
		Intent i =getIntent();
		 currentTab = i.getStringExtra("currentTab");

		 if(currentTab == null){
			 currentTab = "";
		 }
		
		
		//tv_title_cif_common = (TextView)findViewById(R.id.tv_commonTitle);
		//tv_title_cif_common.setText(title) ;

		 mCommonMethods.setApplicationToolbarMenu(this, currentTab);

		WebView webview1 = findViewById(R.id.webview1);

		webview1.getSettings().setJavaScriptEnabled(true);
		webview1.setWebViewClient(new myWebClient());
		
		final AppCompatActivity activity = this;

		 webview1.setWebChromeClient(new WebChromeClient() {
		   @Override
		public void onProgressChanged(WebView view, int progress) {
		     // Activities and WebViews measure progress with different scales.
		     // The progress meter will automatically disappear when we reach 100%
		     activity.setProgress(progress * 1000);
		   }
		 });
		 webview1.setWebViewClient(new WebViewClient() {
		   @Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		     Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
		   }
		 });


		Button btnProceed = findViewById(R.id.btn_next);
		// str_URL="http://172.17.134.181/childeduplanner/index1.jsp";
		//		str_URL = "http://agencyonline.sbilife.co.in/childeduplanner/index.jsp";



		try {
			DatabaseHelper dbObj = new DatabaseHelper(CarouselWebViewActivity.this);

			

			CIFcode = SimpleCrypto.decrypt("SBIL", dbObj.GetUserCode());

			System.out.println(" CIFcode string : "+CIFcode);
			String str=encriptRole(CIFcode+"|SmartAdvisor");
			System.out.println(CIFcode+"|SmartAdvisor"+"  Encypted string : "+str);
			//			str_URL="http://172.17.134.181/childeduplanner/home.jsp?iacode=99016019&source=Smartadvisor";
			//			str_URL="http://172.17.134.181/childeduplanner/index1.jsp?";
			//			str_URL="https://agencyonline.sbilife.co.in/childeduplanner/index.jsp";
			//str_URL="http://172.16.2.120:8080/childeduplanner/index.jsp?enc="+str;


			String str_URL = "https://agencyonline.sbilife.co.in/childeduplannertest/index.jsp?enc=" + str;
			// URL url = new URL(str_URL);

			webview1.loadUrl(str_URL);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		btnProceed.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
				Intent intent = new Intent(getApplicationContext(), ChildPlannerListviewActivity.class);					
				intent.putExtra("CIF_Code", CIFcode);
				intent.putExtra("currentTab", currentTab);
				startActivity(intent);
							


			}
		});
	}
	

	
	 @Override
	    public void onBackPressed() {
		 Intent i = new Intent(CarouselWebViewActivity.this,FuturePlannerActivity.class);
		 startActivity(i) ;
		 finish();
	 }
	private  String encriptRole(String sText)
	{
		String FinalEncriptedText = null;
		try {

			String key = "6LQ$%*)!(S{H>X";
			// user
			Cipher cipher1 = Cipher.getInstance("AES/CBC/PKCS5Padding");
			byte[] keyBytes1= new byte[16];
			byte[] b= key.getBytes(StandardCharsets.UTF_8);
			int len= b.length;
			if (len > keyBytes1.length) len = keyBytes1.length;
			System.arraycopy(b, 0, keyBytes1, 0, len);
			SecretKeySpec keySpec = new SecretKeySpec(keyBytes1, "AES");
			IvParameterSpec ivSpec = new IvParameterSpec(keyBytes1);
			cipher1.init(Cipher.ENCRYPT_MODE,keySpec,ivSpec);


			byte[] results = cipher1.doFinal(sText.getBytes(StandardCharsets.UTF_8));

			//		BASE64Encoder encoder = new BASE64Encoder();

            //System.out.println("Text to be encripted: " + sText );

			FinalEncriptedText = new String(Base64.encodeBase64(results));
			FinalEncriptedText = FinalEncriptedText.replace("+", "plus");

			//System.out.println(FinalEncriptedText);
		} catch (Exception e) {
			System.out.println("ERROR : IN THE Encription method ==> "+e.getMessage());
			//e.printStackTrace();
		}

		return FinalEncriptedText;

	}
	private class myWebClient extends WebViewClient {
		// ProgressDialog progressDialog;

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub

			mCommonMethods.TLSv12Enable();
			
			view.loadUrl(url);
			return true;

		}


		@Override
		public void onPageFinished(WebView view, String url) {
		}

	}


}

