package sbilife.com.pointofsale_bancaagency.utility;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import sbilife.com.pointofsale_bancaagency.R;

public class AlertDialogMessage extends AppCompatActivity{
	
	
	boolean value = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	/**Used To show Dialog with Desire message in given context*/
	public void dialog(Context context,String msg, boolean ismandatry) {

		final Dialog d = new Dialog(context);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		d.setContentView(R.layout.window_pop_up_message_with_single_options);
		TextView text_mssg_1 = d.findViewById(R.id.text_mssg_1);
		if (msg != null)
			text_mssg_1.setText(msg);
		TextView text_mssg_2 = d.findViewById(R.id.text_mssg_2);
		TextView text_mssg_3 = d.findViewById(R.id.text_mssg_3);
		if (ismandatry) {
			text_mssg_2.setVisibility(View.GONE);
			text_mssg_3.setVisibility(View.GONE);
		}
		Button ok = d.findViewById(R.id.idbtnagreement);
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				d.dismiss();
			}

		});
		d.setCancelable(true);
		d.setCanceledOnTouchOutside(true);
		d.show();

	}
	
	
	
}
