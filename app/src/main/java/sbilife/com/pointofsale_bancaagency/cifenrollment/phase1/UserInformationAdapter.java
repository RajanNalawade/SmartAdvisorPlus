package sbilife.com.pointofsale_bancaagency.cifenrollment.phase1;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.R;

class UserInformationAdapter extends BaseAdapter {

	private Context mContext;
	private List<M_UserInformation> mUserInformation = new ArrayList<>();

	private int[] colors = new int[] { Color.parseColor("#DCDBDB"),
			Color.parseColor("#E8E8E8") };

	public UserInformationAdapter(List<M_UserInformation> mCalenarEvent,
			Context mContext) {
		this.mContext = mContext;
		this.mUserInformation = mCalenarEvent;
	}

	@Override
	public int getCount() {
		return mUserInformation.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(final int pos, View view, ViewGroup arg2) {
		ViewHolder v;
		if (view == null) {
			LayoutInflater mInflater = LayoutInflater.from(mContext);
			view = mInflater.inflate(R.layout.cifenrollment_holder_userinformation, null);

			v = new ViewHolder();
			
			v.ll_cifenrollment_dash_row = view.findViewById(R.id.ll_cifenrollment_dash_row);
			
			v.tv1 = view.findViewById(R.id.c1);
			v.tv2 = view.findViewById(R.id.c2);
			v.tv3 = view.findViewById(R.id.c3);
			v.tv4 = view.findViewById(R.id.c4);
			v.tv5 = view.findViewById(R.id.c5);
			v.tv6 = view.findViewById(R.id.c6);
			v.tv7 = view.findViewById(R.id.c7);
			v.tv8 = view.findViewById(R.id.c8);
			v.tv9 = view.findViewById(R.id.c9);

			view.setTag(v);

		} else {
			v = (ViewHolder) view.getTag();
		}

		v.tv1.setText(mUserInformation.get(pos).getStr_candidate_name());
		v.tv2.setText(mUserInformation.get(pos).getStr_quotation());
		v.tv3.setText(mUserInformation.get(pos).getStr_pf_number());
		v.tv4.setText(mUserInformation.get(pos).getStr_status());
		v.tv5.setText(mUserInformation.get(pos).getStr_email_id());
		v.tv6.setText(mUserInformation.get(pos).getStr_created_date());
		v.tv7.setText(mUserInformation.get(pos).getStr_mobile_no());
		v.tv8.setText(mUserInformation.get(pos).getStr_aadhar_card_no());
		v.tv9.setText(mUserInformation.get(pos).getStr_contact_person_email_id());
		
		

		int colorPos = pos % colors.length;
		view.setBackgroundColor(colors[colorPos]);
		
		v.ll_cifenrollment_dash_row.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {

				proceedalertDialog(pos);
				
				return false;
			}
		});

		return view;
	}

	static class ViewHolder {
		TextView tv1;
		TextView tv2;
		TextView tv3;
		TextView tv4;
		TextView tv5;
		TextView tv6;
		TextView tv7;
		TextView tv8;
		TextView tv9;
		
		LinearLayout ll_cifenrollment_dash_row;
	}
	
	private void proceedalertDialog(final int posi) {

		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.loading_window);
		TextView text = dialog
				.findViewById(R.id.txtalertheader);
		text.setText("Next Step...");
		
		Button dialogButton = dialog.findViewById(R.id.btnalert);
		dialogButton.setText("Proceed");
		dialogButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				dialog.dismiss();
				
				Intent i = new Intent(mContext, CIFEnrollmentMainActivity.class);
				i.putExtra("Quotation_number", mUserInformation.get(posi).getStr_quotation());
				i.putExtra("PF_Number", mUserInformation.get(posi).getStr_pf_number());
				String str_isflag1 = "";
				if (mUserInformation != null && mUserInformation.get(posi).getIsFlag1()) {
					str_isflag1 = "true";
				} else {
					str_isflag1 = "false";
				}
				i.putExtra("isFlag1", str_isflag1);
				i.putExtra("Dashboard", "true");
				mContext.startActivity(i);
			}
		});
		dialog.show();
	}
}
