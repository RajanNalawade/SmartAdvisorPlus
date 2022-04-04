package sbilife.com.pointofsale_bancaagency.smartchamp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.new_bussiness.CarouselProduct;

public class SmartChampSuccess extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    private Button back;
    private Button gohome;
    private Button sendemail;
    private Button sendmesssage;
    private TextView successHeader;
    ImageButton btnMenu;
    private Context mContext;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.smartchampsuccess);
        new CommonMethods().setActionbarLayout(this);

        mContext = this;
        dbHelper = new DatabaseHelper(this);

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
        final int policyterm = Integer.parseInt(i.getStringExtra("policyTerm"));

        // Receiving the Input to diaplay Data
        final String sal = "Dear Customer, Based on your request we are happy to give you quote for risk coverage as per details presented below....";
        final String ipproduct = "Product : SBI Life - Smart Champ Insurance (UIN:111N098V03)";
        //final String ip0 = "User Input Details are as follows:";
        final String op0 = "User Output Details are as follows:";
        //final String ip1 = i.getStringExtra("ip1");
        //final String ip2 = i.getStringExtra("ip2");
        //final String ip3 = i.getStringExtra("ip3");
        //final String ip4 = i.getStringExtra("ip4");
        final String ipend = "Thanks & Regards,";
        final String ipsign = "SBI Life Insurance Co.  Ltd.";


        //Declaration
        TextView output = findViewById(R.id.output);
        TextView output1 = findViewById(R.id.output1);
        TextView output2 = findViewById(R.id.output2);


        TextView data1 = findViewById(R.id.tableData1);
        TextView data2 = findViewById(R.id.tableData2);
        TextView data3 = findViewById(R.id.tableData3);
        TextView data4 = findViewById(R.id.tableData4);
        TextView data5 = findViewById(R.id.tableData5);
        TextView data6 = findViewById(R.id.tableData6);
        TextView data7 = findViewById(R.id.tableData7);
        TextView data8 = findViewById(R.id.tableData8);
        TextView data9 = findViewById(R.id.tableData9);
        TextView data10 = findViewById(R.id.tableData10);
        TextView data11 = findViewById(R.id.tableData11);
        TextView data12 = findViewById(R.id.tableData12);
        TextView data13 = findViewById(R.id.tableData13);
        TextView data14 = findViewById(R.id.tableData14);
        TextView data15 = findViewById(R.id.tableData15);
        TextView data16 = findViewById(R.id.tableData16);


        //Heading
        successHeader = findViewById(R.id.txt_successheader);

        //Display output
        output.setText(op);
        output1.setText(op1);
        output2.setText(op2);

        data1.setText("" + (policyterm - 3));
        data2.setText(op3);
        data3.setText(op6);
        data4.setText(op7);

        data5.setText("" + (policyterm - 2));
        data6.setText(op3);
        data7.setText(op6);
        data8.setText(op7);

        data9.setText("" + (policyterm - 1));
        data10.setText(op3);
        data11.setText(op6);
        data12.setText(op7);

        data13.setText("" + (policyterm));
        data14.setText(op3);
        data15.setText(op4);
        data16.setText(op5);


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

                StringBuilder str = new StringBuilder();
                str.append(sal);
                str.append("\n");
                str.append(ipproduct);
                str.append("\n");
                str.append(op0);
                str.append("\n");
                str.append(op == null ? "" : op);
                str.append("\n");
                str.append(op1 == null ? "" : op1);
                str.append("\n");
                str.append(op2 == null ? "" : op2);
                str.append("\n");
                str.append(op3 == null ? "" : op3);
                str.append("\n");
                str.append(op4 == null ? "" : op4);
                str.append("\n");
                str.append(op5 == null ? "" : op5);
                str.append("\n");
                str.append(op6 == null ? "" : op6);
                str.append("\n");
                str.append(op7 == null ? "" : op7);
                str.append("\n");
                str.append(policyterm);
                str.append("\n");
                str.append(ipend);
                str.append("\n");
                str.append(ipsign);

                String emailBody = str.toString();

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
				/*String smsBody = sal + "\n" + ipproduct + "\n" + op0 + "\n" + op == null ? "" : op + "\n" + op1 == null ? "" : op1 + "\n" + op2 == null ? "" : op2 + "\n" + op3 == null ? "" : op3 +
						"\n" + op4 == null ? "" : op4 + "\n" + op5 == null ? "" : op5 + "\n" + op6 == null ? "" : op6 + "\n" + op7 == null ? "" : op7
						+ "\n" + "\n"+ ipend + "\n" + ipsign;*/

                StringBuilder str = new StringBuilder();
                str.append(sal);
                str.append("\n");
                str.append(ipproduct);
                str.append("\n");
                str.append(op0);
                str.append("\n");
                str.append(op == null ? "" : op);
                str.append("\n");
                str.append(op1 == null ? "" : op1);
                str.append("\n");
                str.append(op2 == null ? "" : op2);
                str.append("\n");
                str.append(op3 == null ? "" : op3);
                str.append("\n");
                str.append(op4 == null ? "" : op4);
                str.append("\n");
                str.append(op5 == null ? "" : op5);
                str.append("\n");
                str.append(op6 == null ? "" : op6);
                str.append("\n");
                str.append(op7 == null ? "" : op7);
                str.append("\n");
                str.append(policyterm);
                str.append("\n");
                str.append(ipend);
                str.append("\n");
                str.append(ipsign);

                String smsBody = str.toString();

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
    /*********************************Menu popup ends here *************************/


}
