package sbilife.com.pointofsale_bancaagency.rinnraksha;

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

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.new_bussiness.CarouselProduct;


public class RinnRakshaSuccess extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    private Context mContext;
    String ProductDescName;
    Button goToProductDesc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.rinnrakshasuccess);
        new CommonMethods().setActionbarLayout(this);

        Button back, gohome, sendemail, sendmesssage;

        dbHelper = new DatabaseHelper(this);


        mContext = this;

        Intent i = getIntent();
        // Receiving the Data

        final String op = i.getStringExtra("op");
        final String op1 = i.getStringExtra("op1");
        final String op2 = i.getStringExtra("op2");
        final String op3 = i.getStringExtra("op3");
        final String op4 = i.getStringExtra("op4");
        final String op5 = i.getStringExtra("op5");
        final String op6 = i.getStringExtra("op6");
        final String op7 = i.getStringExtra("op7");
        final String op8 = i.getStringExtra("op8");
        final String op9 = i.getStringExtra("op9");
        final String op10 = i.getStringExtra("op10");
        //ProductDescName = i.getStringExtra("ProductDescName");


        final String sal = "Dear Customer, Based on your request we are happy to give you quote for risk coverage as per details presented below....";
        final String ipproduct = i.getStringExtra("ProductName");
        final String ip0 = "User Input Details are as follows:";
        final String op0 = "User Output Details are as follows:";
        final String ipend = "Thanks & Regards,";
        final String ipsign = "SBI Life Insurance Co.  Ltd.";

        TextView output = findViewById(R.id.output);
        TextView output1 = findViewById(R.id.output1);
        TextView output2 = findViewById(R.id.output2);
        TextView output3 = findViewById(R.id.output3);
        TextView output4 = findViewById(R.id.output4);
        TextView output5 = findViewById(R.id.output5);
        TextView output6 = findViewById(R.id.output6);
        TextView output7 = findViewById(R.id.output7);
        TextView output8 = findViewById(R.id.output8);
        TextView output9 = findViewById(R.id.output9);
        TextView output10 = findViewById(R.id.output10);

        output.setText(op);
        output1.setText(op1);
        output2.setText(op2);
        output3.setText(op3);
        output4.setText(op4);
        output5.setText(op5);
        output6.setText(op6);
        output7.setText(op7);
        output8.setText(op8);
        output9.setText(op9);
        output10.setText(op10);

        if (op2 == null)
            output2.setVisibility(View.GONE);
        else {
            output2.setText(op2);
            output2.setVisibility(View.VISIBLE);
        }

        if (op3 == null)
            output3.setVisibility(View.GONE);
        else {
            output3.setText(op3);
            output3.setVisibility(View.VISIBLE);
        }

        if (op4 == null)
            output4.setVisibility(View.GONE);
        else {
            output4.setText(op4);
            output4.setVisibility(View.VISIBLE);
        }

        if (op5 == null)
            output5.setVisibility(View.GONE);
        else {
            output5.setText(op5);
            output5.setVisibility(View.VISIBLE);
        }

        if (op6 == null)
            output6.setVisibility(View.GONE);
        else {
            output6.setText(op6);
            output6.setVisibility(View.VISIBLE);
        }

        if (op7 == null)
            output7.setVisibility(View.GONE);
        else {
            output7.setText(op7);
            output7.setVisibility(View.VISIBLE);
        }

        if (op8 == null)
            output8.setVisibility(View.GONE);
        else {
            output8.setText(op8);
            output8.setVisibility(View.VISIBLE);
        }

        if (op9 == null)
            output9.setVisibility(View.GONE);
        else {
            output9.setText(op9);
            output9.setVisibility(View.VISIBLE);
        }

        if (op10 == null)
            output10.setVisibility(View.GONE);
        else {
            output10.setText(op10);
            output10.setVisibility(View.VISIBLE);
        }

        back = findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                setResult(RESULT_OK);
                finish();
            }
        });

        gohome = findViewById(R.id.gohome);
        gohome.setOnClickListener(new OnClickListener() {
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
                String emailBody = sal + "\n" + ip0 + "\n" + ipproduct + "\n" + "\n" + op0 + "\n" +
                        op == null ? "" : op + "\n" + op1 == null ? "" : op1 + "\n" + op2 == null ? "" : op2 + "\n" + op3 == null ? "" : op3 +
                        "\n" + op4 == null ? "" : op4 + "\n" + op5 == null ? "" : op5 + "\n" + op6 == null ? "" : op6 +
                        "\n" + op7 == null ? "" : op7 + "\n" + op8 == null ? "" : op8 + "\n" + op9 == null ? "" : op9 +
                        "\n" + op10 == null ? "" : op10 +
                        "\n" + "\n" + ipend + "\n" + ipsign;
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setType("text/plain/email/dir");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Policy value calculation");
                emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
                emailIntent.setData(Uri.parse("mailto:"));
                try {
                    startActivity(emailIntent);
                } catch (android.content.ActivityNotFoundException e) {
                    Toast.makeText(mContext, "There are No Email Client Installed", Toast.LENGTH_SHORT).show();
                }
            /*}
	        else
	        {
	        	validationAlert();
	        }*/

            }
        });

        sendmesssage = findViewById(R.id.sendmesssage);
        sendmesssage.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                //String smsBody = op + "\n" + op1 + "\n" + op2 + "\n" + op3;
                String smsBody = sal + "\n" + ip0 + "\n" + ipproduct + "\n" + "\n" + op0 + "\n" + op == null ? "" : op + "\n" + op1 == null ? "" : op1 + "\n" + op2 == null ? "" : op2 + "\n" + op3 == null ? "" : op3
                        + "\n" + op4 == null ? "" : op4 + "\n" + op5 == null ? "" : op5 + "\n" + op6 == null ? "" : op6 +
                        "\n" + op7 == null ? "" : op7 + "\n" + op8 == null ? "" : op8 + "\n" + op9 == null ? "" : op9 +
                        "\n" + op10 == null ? "" : op10 +
                        "\n" + "\n" + ipend + "\n" + ipsign;
                Uri uri = Uri.parse("smsto:");
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri);
                smsIntent.putExtra("sms_body", smsBody);
                startActivity(smsIntent);
            }
        });


    }


    public String GetUserType() {
        String strUserType = "";
        try {
            strUserType = SimpleCrypto.decrypt("SBIL", dbHelper.GetUserType());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return strUserType;
    }

    //after 5 minute it will get log out
			/*private CountDownTimer mCountDown = new CountDownTimer(300000, 300000)
				{

				    @Override
				    public void onTick(long millisUntilFinished)
				    {

				    }


				    @Override
				    public void onFinish()
				    {
				        //show your dialog here
				    	new CommonMethods().logoutToLoginActivity(mContext);
				    }
				};  


				@Override
				public void onResume()
				{
				    super.onResume();

				    mCountDown.start();
				}  
				@Override
				public void onPause()
				{
				    super.onPause();

				    mCountDown.cancel();
				}  
				@Override
				public void onUserInteraction()
				{
				    super.onUserInteraction();

				    // user interact cancel the timer and restart to countdown to next interaction
				    mCountDown.cancel();
				    mCountDown.start();
				} */


}
