package sbilife.com.pointofsale_bancaagency.utility;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sbilife.com.pointofsale_bancaagency.R;

class DocumentUploadViewAdapter extends BaseAdapter {

	private Context mContext;
	private List<M_DocumentUploadStatus> mUserInformation;

	private int[] colors = new int[] { Color.parseColor("#DCDBDB"),
			Color.parseColor("#E8E8E8") };

	public DocumentUploadViewAdapter(List<M_DocumentUploadStatus> mCalenarEvent,
			Context mContext) {
		this.mContext = mContext;
		this.mUserInformation = mCalenarEvent;
	}

	public int getCount() {
		return mUserInformation.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int pos) {
		return pos;
	}

	public View getView(int pos, View view, ViewGroup arg2) {
		ViewHolder v;
		if (view == null) {
			LayoutInflater mInflater = LayoutInflater.from(mContext);
			view = mInflater
					.inflate(R.layout.holder_document_upload_view, null);

			v = new ViewHolder();
			v.tv1 = view.findViewById(R.id.c1);
			v.tv2 = view.findViewById(R.id.c2);
			v.tv3 = view.findViewById(R.id.c3);
			v.tv4 = view.findViewById(R.id.c4);

			view.setTag(v);

		} else {
			v = (ViewHolder) view.getTag();
		}

		v.tv1.setText(mUserInformation.get(pos).getProofOf());
		v.tv2.setText(mUserInformation.get(pos).getDocumentName());
		v.tv3.setText(mUserInformation.get(pos).getFileName());
		v.tv4.setText(mUserInformation.get(pos).getSyncDate());

		int colorPos = pos % colors.length;
		view.setBackgroundColor(colors[colorPos]);

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
	}
}
