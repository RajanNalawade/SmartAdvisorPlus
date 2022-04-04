package sbilife.com.pointofsale_bancaagency.flexismart;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.new_bussiness.CarouselProduct;

public class success extends AppCompatActivity {

    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.flexismartsuccess);
        new CommonMethods().setActionbarLayout(this);
        context = this;

        Button back, gohome, sendemail, sendmesssage;

        Intent i = getIntent();
        // Receiving the Data
        final String op = i.getStringExtra("op");
        final String op1 = i.getStringExtra("op1");
        final String op2 = i.getStringExtra("op2");

        // Receiving the Input to diaplay Data
        final String sal = "Dear Customer, Based on your request we are happy to give you quote for risk coverage as per details presented below....";
        final String ipproduct = "Product : SBI Life - FLEXI SMART INSURANCE (UIN:111N080V01)";
        final String ip0 = "User Input Details are as follows:";
        final String op0 = "User Output Details are as follows:";
        final String ip1 = i.getStringExtra("ip1");
        final String ip2 = i.getStringExtra("ip2");
        final String ip3 = i.getStringExtra("ip3");
        final String ip4 = i.getStringExtra("ip4");
        final String ip5 = i.getStringExtra("ip5");
        final String ipend = "Thanks & Regards,";
        final String ipsign = "SBI Life Insurance Co.  Ltd.";


        //Declaration
        TextView output = findViewById(R.id.output);
        TextView output1 = findViewById(R.id.output1);
        TextView output2 = findViewById(R.id.output2);

        //Display output
        output.setText(op == null ? "" : op);
        output1.setText(op1 == null ? "" : op1);
        output2.setText(op2 == null ? "" : op2);

        back = findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                setResult(RESULT_OK);
                finish();
            }
        });

        gohome = findViewById(R.id.gohome);
        gohome.setOnClickListener(new OnClickListener() {
            //@Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), CarouselProduct.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();

            }
        });

        sendemail = findViewById(R.id.sendemail);
        sendemail.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                String emailBody = sal + "\n" + ip0 + "\n" + ipproduct + "\n" +
                        ip1 + "\n" + ip2 + "\n" + ip3 + "\n" + ip4 + "\n" + ip5
                        + "\n" + "\n" + op0 + "\n" +
                        op == null ? "" : op + "\n" + op1 == null ? "" : op1 + "\n" + op2 == null ? "" : op2
                        + "\n" + "\n" + ipend + "\n" + ipsign;
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setType("text/plain/email/dir");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Policy value calculation");
                emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
                emailIntent.setData(Uri.parse("mailto:"));
                try {
                    startActivity(emailIntent);
                } catch (android.content.ActivityNotFoundException e) {
                    Toast.makeText(context, "There are No Email Client Installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        sendmesssage = findViewById(R.id.sendmesssage);
        sendmesssage.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                //String smsBody = op + "\n" + op1 + "\n" + op2;
                String smsBody = sal + "\n" + ip0 + "\n" + ipproduct + "\n" + ip1 + "\n" + "\n" +
                        op0 + "\n" +
                        op == null ? "" : op + "\n" + op1 == null ? "" : op1 + "\n" + op2 == null ? "" : op2
                        + "\n" + "\n" + ipend + "\n" + ipsign;
                Uri uri = Uri.parse("smsto:");
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri);
                smsIntent.putExtra("sms_body", smsBody);
                startActivity(smsIntent);
            }
        });

    }


}
