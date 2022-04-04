package sbilife.com.pointofsale_bancaagency;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

class MenuItemAdapter extends BaseAdapter {
	private List<String> items;
	private Context mContext;

	private int color = Color.parseColor("#FFFFFF");
	private int color2 = Color.parseColor("#F8F8F8");

	public MenuItemAdapter(Context context, List<String> subMenus) {
		mContext = context;
		this.items = subMenus;
	}

	public int getCount() {
		return items.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	
	public View getView(final int position, View convertView, ViewGroup parent) {
		TextView mName;
		View view = convertView;
		ImageView image;

		if (view == null) {
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.row_list_application_menu, null);
		}
		image = view.findViewById(R.id.list_image);
		mName = view.findViewById(R.id.list_content);

		mName.setText(items.get(position));
		
		if (items.get(position).equalsIgnoreCase("Profile")) {
			image.setBackgroundResource(R.drawable.profilegrey);
			view.setBackgroundColor(color2);
		}
		else if (items.get(position).equalsIgnoreCase("Home")) {
			image.setBackgroundResource(R.drawable.homegray);
			view.setBackgroundColor(color);
		}
		else if (items.get(position).equalsIgnoreCase("Dashboard")) {
			image.setBackgroundResource(R.drawable.dashboardgrey);
			view.setBackgroundColor(color2);
		}
		else  if (items.get(position).equalsIgnoreCase("Products")) {
			image.setBackgroundResource(R.drawable.productsgrey);
			view.setBackgroundColor(color);
		}
		else if (items.get(position).equalsIgnoreCase("New Business")) {
			image.setBackgroundResource(R.drawable.newbusinessesgrey);
			view.setBackgroundColor(color2);
		}else if (items.get(position).equalsIgnoreCase("Requirement Upload")) {
			image.setBackgroundResource(R.drawable.documentuploadgrey);
			view.setBackgroundColor(color);
		}
		else if (items.get(position).equalsIgnoreCase("Future Planner")) {
			image.setBackgroundResource(R.drawable.futureplanninggray);
			view.setBackgroundColor(color2);
		}
		else if (items.get(position).equalsIgnoreCase("Servicing")) {
			image.setBackgroundResource(R.drawable.utilitygrey);
			view.setBackgroundColor(color);
		}else if (items.get(position).equalsIgnoreCase("Calender")) {
			image.setBackgroundResource(R.drawable.plannergrey);
			view.setBackgroundColor(color2);
		}
		else if (items.get(position).equalsIgnoreCase("Sales Kit")) {
			image.setBackgroundResource(R.drawable.saleskitgray);
			view.setBackgroundColor(color);
		}
		else if (items.get(position).equalsIgnoreCase("Help")) {
			image.setBackgroundResource(R.drawable.helpgray);
			view.setBackgroundColor(color2);
		}
		
		/*else if (items.get(position).equalsIgnoreCase("Settings")) {
			image.setBackgroundResource(R.drawable.settingsgray);
			view.setBackgroundColor(color);
		}*/
		else if (items.get(position).equalsIgnoreCase("Logout")) {
			image.setBackgroundResource(R.drawable.logoutgrey);
			view.setBackgroundColor(color);
		}
		return view;
	}

	
}
