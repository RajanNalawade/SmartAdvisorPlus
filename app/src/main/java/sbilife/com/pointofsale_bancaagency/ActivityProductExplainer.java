package sbilife.com.pointofsale_bancaagency;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class ActivityProductExplainer extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private CommonMethods mCommonMethods;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_product_explainer);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        initialisation();
    }

    private void initialisation() {

        mContext = this;
        mCommonMethods = new CommonMethods();

        mCommonMethods.setApplicationToolbarMenu(this, "Product Explainer");

        TextView textviewUserManual = findViewById(R.id.textviewUserManual);
        TextView txt_product_exp_vid1 = findViewById(R.id.txt_product_exp_vid1);
        TextView txt_product_exp_vid2 = findViewById(R.id.txt_product_exp_vid2);
        TextView txt_product_exp_vid3 = findViewById(R.id.txt_product_exp_vid3);
        TextView tvSmartSamadhanSimulations = findViewById(R.id.tvSmartSamadhanSimulations);
        TextView textviewProtectionExplainer = findViewById(R.id.textviewProtectionExplainer);
        TextView tvSmartSamadhanExplainer = findViewById(R.id.tvSmartSamadhanExplainer);

        txt_product_exp_vid1.setOnClickListener(this);
        txt_product_exp_vid2.setOnClickListener(this);
        txt_product_exp_vid3.setOnClickListener(this);
        textviewUserManual.setOnClickListener(this);
        textviewProtectionExplainer.setOnClickListener(this);
        tvSmartSamadhanSimulations.setOnClickListener(this);
        tvSmartSamadhanExplainer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.txt_product_exp_vid1:
                mCommonMethods.loadDriveURL("https://drive.google.com/open?id=1muF2Gh5AnuUcujYyO49EJsyXsQStQI9q", mContext);
                break;

            case R.id.txt_product_exp_vid2:
                mCommonMethods.loadDriveURL("https://drive.google.com/open?id=1J3RELt9kHwZylmD4mMX586vQGjxD5EEG", mContext);

                break;

            case R.id.txt_product_exp_vid3:
                mCommonMethods.loadDriveURL("https://drive.google.com/open?id=1-NV_f_am3cxfH6fxg81i_0eD430PSoGV", mContext);
                break;
            case R.id.textviewUserManual:
                mCommonMethods.loadDriveURL("https://drive.google.com/open?id=1BEEHBhpQwx0MnIKb2QHDcnauxNM-I_RU", mContext);
                break;
            case R.id.textviewProtectionExplainer:
                mCommonMethods.loadDriveURL("https://drive.google.com/open?id=1lermr3SDvBvdapTkNw7-cn68_hnpdmom", mContext);
                break;
            case R.id.tvSmartSamadhanSimulations:
                String url = "";
                String strUserType = mCommonMethods.GetUserType(mContext);
                if (strUserType.equalsIgnoreCase("AGENT") || strUserType.equalsIgnoreCase("UM")
                        || strUserType.equalsIgnoreCase("BSM")
                        || strUserType.equalsIgnoreCase("DSM")
                        || strUserType.equalsIgnoreCase("ASM")
                        || strUserType.equalsIgnoreCase("RSM")) {
                    url = "https://drive.google.com/open?id=1G94m-HDMZT7BQqNerdaC2kg2-TFP0zTN";
                } else {
                    url = "https://drive.google.com/open?id=1GseaCL7uqujOvlCWjFCpmcHRuvnyz0HL";
                }
                mCommonMethods.loadDriveURL(url, mContext);
                break;

            case R.id.tvSmartSamadhanExplainer:
                mCommonMethods.loadDriveURL("https://drive.google.com/open?id=1tUxMYhTve6QYIEPYUslG6ytFZBzD2fNJ", mContext);
                break;
            default:
                break;
        }
    }
}
