package sbilife.com.pointofsale_bancaagency.reports;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sbilife.com.pointofsale_bancaagency.R;

public class ReportsDynamicListingAdapter extends BaseAdapter {

	private ArrayList<String> listMenu = null;
	private Context mContext;
	
	public ReportsDynamicListingAdapter(ArrayList<String> listMenu, Context mContext) {
		super();
		this.listMenu = listMenu;
		this.mContext = mContext;
	}
	
	@Override
	public int getCount() {
		return listMenu.size();
	}

	@Override
	public Object getItem(int position) {
		return listMenu.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View viewList;
		
		TextView txtMenu;
		
		LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
            if (convertView == null) {
 
            	viewList = new View(mContext);
               
            	viewList = inflater.inflate(R.layout.row_reports_dynamic_menu_listing, null);
               
                 txtMenu = viewList.findViewById(R.id.tvMenuItem);
                 
                 txtMenu.setText(listMenu.get(position));
                
            } else {
            	viewList = convertView;
            }
            
            
            return viewList;
	}

}
