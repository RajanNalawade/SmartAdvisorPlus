package sbilife.com.pointofsale_bancaagency.needanalysis;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;

public class NABrochureActivity extends AppCompatActivity {

    private Context mContext;
    private CommonMethods mCommonMethods;
    private StorageUtils mStorageUtils;

    private String str_brochure_download_file_url = "", str_brochure_dest_file_path = "";

    private TextView txtBrochure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_nabrochure);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        txtBrochure = findViewById(R.id.txtBrochure);
        txtBrochure.setTypeface(null, Typeface.BOLD);

        mContext = this;
        mCommonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();

        str_brochure_download_file_url = getIntent().getStringExtra("BROCHURE_URL");
        str_brochure_dest_file_path = getIntent().getStringExtra("BROCHURE_FILE_PATH");

        mCommonMethods.setApplicationToolbarMenu(this, "Brochure");

        if (!str_brochure_download_file_url.equals("") && !str_brochure_dest_file_path.equals(""))
            downloadAndOpenBrochure();
        else
            mCommonMethods.showToast(mContext, "url or file path may be null");
    }

    private void downloadAndOpenBrochure() {

        if (mCommonMethods.isNetworkConnected(mContext)) {
            downloadAndOpenPDF();
        } else {
            mCommonMethods.showToast(mContext, "please internet connection..");
        }
    }

    private void downloadAndOpenPDF() {
        new Thread(new Runnable() {
            public void run() {

                try {
                    File mFile = downloadFile(str_brochure_download_file_url);
                    mCommonMethods.openAllDocs(mContext, mFile);
                } catch (IOException ex) {
                    mCommonMethods.showMessageDialog(mContext, ex.getMessage());
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
            // create a new file, to save the <span id="IL_AD11"
            // class="IL_AD">downloaded</span> file
            file = mStorageUtils.createFileToAppSpecificDir(mContext, str_brochure_dest_file_path);

            FileOutputStream fileOutput = new FileOutputStream(file);

            // Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            // this is the total size of the file which we are
            // <span id="IL_AD12" class="IL_AD">downloading</span>

            int totalsize = urlConnection.getContentLength();

            setText("Starting PDF download...");

            // create a buffer...
            byte[] buffer = new byte[1024 * 1024];
            int bufferLength = 0, downloadedSize = 0;

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

            setTextError(
                    "Failed to download image. Please check your internet connection.",
                    Color.RED);
        }
        return file;
    }

    private void setText(final String txt) {
        runOnUiThread(new Runnable() {
            public void run() {
                txtBrochure.setText(txt);
            }
        });

    }

    private void setTextError(final String message, final int color) {
        runOnUiThread(new Runnable() {
            public void run() {
                txtBrochure.setTextColor(color);
                txtBrochure.setText(message);
            }
        });

    }
}
