package sbilife.com.pointofsale_bancaagency;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sbilife.com.pointofsale_bancaagency.home.clsActivitySubCategory;

class Spinner_BaseAdapterCategory extends BaseAdapter {

	static class ViewHolder {
		TextView c1;
	}

	private List<clsActivitySubCategory> allElementDetails;
	private LayoutInflater mInflater;

	public Spinner_BaseAdapterCategory(Context context, List<clsActivitySubCategory> results) {
		allElementDetails = results;
		mInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return allElementDetails.size();
	}

	public Object getItem(int position) {
		return allElementDetails.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getPosition(String resource_type) {
		// TODO Auto-generated method stub
		return allElementDetails.indexOf(resource_type);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.spinner_item, null);
			holder = new ViewHolder();
			holder.c1 = convertView.findViewById(R.id.txtspinitem);

			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.c1.setText(String.valueOf(allElementDetails.get(position).getName()));	

		return convertView;
	}

}
