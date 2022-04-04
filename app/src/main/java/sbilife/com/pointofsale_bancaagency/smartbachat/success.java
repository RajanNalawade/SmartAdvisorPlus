package sbilife.com.pointofsale_bancaagency.smartbachat;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.new_bussiness.CarouselProduct;


public class success extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.smartbachatsuccess);
        new CommonMethods().setActionbarLayout(this);
        //dbHelper = new DatabaseHelper(this);

        Button back, gohome;//,sendemail,sendmesssage;

        //mContext = this;

        Intent i = getIntent();
        // Receiving the Data
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

        //Declaration
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
        //ProductDescName = i.getStringExtra("ProductDescName");

		/*final String sal =  "Dear Customer, Based on your request we are happy to give you quote for risk coverage as per details presented below....";
		final String ipproduct = "Product : SBI Life - SARAL SHIELD (UIN:111N066V01)";
		final String ip0 = "User Input Details are as follows:";
		final String op0 = "User Output Details are as follows:";					
		final String ip1 = i.getStringExtra("ip1");
		final String ip2 = i.getStringExtra("ip2");
		final String ip3 = i.getStringExtra("ip3");
		final String ip4 = i.getStringExtra("ip4");
		final String ipend = "Thanks & Regards,";
		final String ipsign = "SBI Life Insurance Co.  Ltd.";*/

        //Display output
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

        if (op7 == null)
            output7.setVisibility(View.GONE);
        else
            output7.setText(op7);

        if (op8 == null)
            output8.setVisibility(View.GONE);
        else
            output8.setText(op8);

        if (op9 == null)
            output9.setVisibility(View.GONE);
        else
            output9.setText(op9);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                setResult(RESULT_OK);
                finish();
            }
        });

        gohome = findViewById(R.id.gohome);
        gohome.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), CarouselProduct.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

    }

}
