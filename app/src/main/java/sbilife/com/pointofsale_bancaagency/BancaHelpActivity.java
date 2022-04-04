package sbilife.com.pointofsale_bancaagency;

/*
 * it is same as help view but it will show in independent activity, called when carousel view is there.
 * 
 */

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.home.CarouselHomeActivity;

@SuppressWarnings("deprecation")
public class BancaHelpActivity extends AppCompatActivity {

    private DatabaseHelper dbhelper;

	protected ListView lv;

	private CommonMethods mCommonMethods;

	private final Context context = this;

	private  final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.help);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

		mCommonMethods = new CommonMethods();

		dbhelper = new DatabaseHelper(this);
		mCommonMethods.setApplicationToolbarMenu(this, "Help");
		TextView txtwebsite = findViewById(R.id.txtwebsite);
		TextView txtebandhan = findViewById(R.id.txtebandhan);

		TextView txtfaq = findViewById(R.id.txtfaq);

		ImageButton imgwebsite = findViewById(R.id.imgwebsite);
		ImageButton imgebandhan = findViewById(R.id.imgebandhan);

		ImageButton imgefaq = findViewById(R.id.imgefaq);

		txtwebsite.setMovementMethod(LinkMovementMethod.getInstance());
		txtebandhan.setMovementMethod(LinkMovementMethod.getInstance());

        TableLayout tblsbilws = findViewById(R.id.tblsbilws);
        TableLayout tblsbileb = findViewById(R.id.tblsbileb);
        TableLayout tblfaqs = findViewById(R.id.tblfaqs);

        TableLayout tblemailus = findViewById(R.id.tblemailus);
        TextView txtemailus = findViewById(R.id.txtemailus);

		String UserType = GetUserType();

		if (UserType.contentEquals("MAN") || UserType.contentEquals("BDM")
				|| UserType.contentEquals("CIF")) {
			tblsbilws.setVisibility(View.VISIBLE);
			tblsbileb.setVisibility(View.VISIBLE);
			tblfaqs.setVisibility(View.VISIBLE);
			tblemailus.setVisibility(View.GONE);
		} else {
			tblsbilws.setVisibility(View.GONE);
			tblsbileb.setVisibility(View.GONE);
			tblfaqs.setVisibility(View.GONE);
			tblemailus.setVisibility(View.VISIBLE);
		}

		txtwebsite.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				mCommonMethods.openWebLink(context, "http://www.sbilife.co.in/sbilife/content/home");
			}
		});

		txtebandhan.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				mCommonMethods.openWebLink(context, "http://www.ebandhan.net/");
			}
		});

		imgwebsite.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View arg0, MotionEvent arg1) {

				openWeblinkWithPkgAndClass(context, "http://www.sbilife.co.in/sbilife/content/home",
						"com.android.browser", "com.android.browser.BrowserActivity");
				return false;
			}
		});

		imgebandhan.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				openWeblinkWithPkgAndClass(context, "http://www.ebandhan.net/",
						"com.android.browser", "com.android.browser.BrowserActivity");
				return false;
			}
		});

		txtfaq.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {


				mCommonMethods.loadDriveURL("https://drive.google.com/open?id=1NgRm9Jt8tEJHM8hAFZvB-SlFZKosaP6h", context);

				}
		});

		imgefaq.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View arg0, MotionEvent arg1) {
				String extStorageDirectory = Environment
						.getExternalStorageDirectory().toString();
				String direct = "/doc";
				File f = new File(extStorageDirectory + direct + "/");

				if (!f.exists()) {
					f.mkdirs();
				}
				f = new File(f, "faqs" + "." + "docx");

				if (!f.exists()) {
					startDownload();
				} else {

					try {
						mCommonMethods.openAllDocs(context, f);
					}catch (IOException exp){
						exp.printStackTrace();
						mCommonMethods.showToast(context, exp.getMessage());
					}
				}
				return false;
			}
		});

		txtemailus.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				String str = "project.management@sbilife.co.in";
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
				emailIntent.setType("text/plain/email/dir");
				emailIntent.putExtra(Intent.EXTRA_SUBJECT,
						"Query - Smart Advisor");
				emailIntent.putExtra(Intent.EXTRA_TEXT, "");
				emailIntent.setData(Uri.parse("mailto:" + str));
				try {
					startActivity(emailIntent);
				} catch (android.content.ActivityNotFoundException e) {
					Toast.makeText(context,
							"There are No Email Client Installed",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void startDownload() {
		new DownloadFileAsync().execute("demo");
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DIALOG_DOWNLOAD_PROGRESS:
				mProgressDialog = new ProgressDialog(this,
						ProgressDialog.THEME_HOLO_LIGHT);
				String Message = "Loading. Please wait...";
				mProgressDialog.setMessage(Html
						.fromHtml("<font color='#00a1e3'><b>" + Message
								+ "<b></font>"));
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgressDialog.setCancelable(false);
				mProgressDialog.setMax(100);
				mProgressDialog.show();
				return mProgressDialog;
			default:
				return null;
		}
	}

	class DownloadFileAsync extends AsyncTask<String, String, String> {

		private volatile boolean running = true;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		@Override
		protected String doInBackground(String... aurl) {
			try {

				running = true;

				CopyAssets();

			} catch (Exception e) {
				try {
					throw (e);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				mProgressDialog.dismiss();
				running = false;
			}
			return null;

		}

		@Override
		protected void onProgressUpdate(String... progress) {
			Log.d("ANDRO_ASYNC", progress[0]);
			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}

		@Override
		protected void onPostExecute(String unused) {
			dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
			if (running) {
				try {
					String UserType = GetUserType();

					File f;
					if (UserType.contentEquals("MAN")
							|| UserType.contentEquals("BDM")
							|| UserType.contentEquals("CIF")) {
						f = new StorageUtils().createFileToAppSpecificDirDoc(context, "faqs" + "." + "html");
					} else {
						f = new StorageUtils().createFileToAppSpecificDirDoc(context, "faqs_cif_agent" + "." + "html");
					}
					if (f!=null){
						mCommonMethods.openAllDocs(context, f);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				mCommonMethods.showMessageDialog(context, "Document Not Responding,Try again..");
			}
		}
	}

	private void CopyAssets() throws IOException {

		InputStream myInput = null;

		String UserType = GetUserType();

		File f;
		if (UserType.contentEquals("MAN") || UserType.contentEquals("BDM")
				|| UserType.contentEquals("CIF")) {
			f = new StorageUtils().createFileToAppSpecificDirDoc(context, "faqs" + "." + "html");
			myInput = getResources().openRawResource(R.raw.faqs);
		} else {
			f = new StorageUtils().createFileToAppSpecificDirDoc(context, "faqs_cif_agent" + "." + "html");
			myInput = getResources().openRawResource(R.raw.faqs_cif_agent);
		}

		OutputStream myOutput = new FileOutputStream(f);
		// transfer bytes from the input file to the output file
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	private String GetUserType() {
		String strUserType = "";
			try {
				strUserType = SimpleCrypto.decrypt("SBIL",
						dbhelper.GetUserType());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return strUserType;
	}


	@Override
	public void onUserInteraction() {
		super.onUserInteraction();

		// user interact cancel the timer and restart to countdown to next
		// interaction
		// mCountDown.cancel();
		// mCountDown.start();
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(BancaHelpActivity.this,
				CarouselHomeActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(i);
		finish();
	}

	public void openWeblinkWithPkgAndClass(Context context, String strURL, String strPackage, String strClass){

		if (mCommonMethods.isNetworkConnected(context)) {

			Intent intentInternet = new Intent(
					Intent.ACTION_VIEW, Uri.parse(strURL));
			intentInternet.setComponent(new ComponentName(strPackage, strClass));
			intentInternet.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intentInternet);

		} else {
			mCommonMethods.showMessageDialog(context, "Internet Connection Not Present,Try again..");
		}
	}

}