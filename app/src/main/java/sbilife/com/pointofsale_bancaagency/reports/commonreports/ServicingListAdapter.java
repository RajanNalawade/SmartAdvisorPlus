package sbilife.com.pointofsale_bancaagency.reports.commonreports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class ServicingListAdapter extends RecyclerView.Adapter<ServicingListAdapter.
        ViewHolderAdapter> {
    private CommonMethods commonMethods;
    private ArrayList<ServicingListValuesModel> lstAdapterList;
    private Context context;

    public ServicingListAdapter(ArrayList<ServicingListValuesModel> lstAdapterList, Context context) {
        this.lstAdapterList = lstAdapterList;
        this.context = context;
        commonMethods = new CommonMethods();
    }


    @Override
    public int getItemCount() {
        return lstAdapterList.size();
    }

    @Override
    public ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_reports_dynamic_menu_listing, parent, false);

        return new ViewHolderAdapter(mView);
    }

    @Override
    public void onBindViewHolder(final ViewHolderAdapter holder, final int position) {
        holder.tvMenuItem.setText(lstAdapterList.get(position).getStrMenuTitle());

        holder.llListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Class aClass = lstAdapterList.get(position).getaClass();
                if (aClass != null) {
                    boolean isHomeTag = lstAdapterList.get(position).isHomeTag();
                    boolean isMtag = lstAdapterList.get(position).isMtag();
                    if (isHomeTag) {
                        commonMethods.callActivityWithHomeTagYes(context, aClass);
                    } else if (isMtag) {
                        commonMethods.callActivityWithTagM(context, aClass);
                    } else {
                        commonMethods.callActivity(context, aClass);
                    }
                }

            }
        });
    }

    public class ViewHolderAdapter extends RecyclerView.ViewHolder {
        private final TextView tvMenuItem;
        private final LinearLayout llListItem;

        ViewHolderAdapter(View v) {
            super(v);
            llListItem = v.findViewById(R.id.llListItem);
            tvMenuItem = v.findViewById(R.id.tvMenuItem);

        }

    }
}
