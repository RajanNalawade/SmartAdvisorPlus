package sbilife.com.pointofsale_bancaagency.poornsuraksha;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sbilife.com.pointofsale_bancaagency.R;

class Adapter_BI_PoornSurakshaGrid extends BaseAdapter {
    static class ViewHolder {
        TextView tv_policy_year;
        TextView tv_total_premium_wo_tax;
        TextView tv_death_gurantee;
        TextView tv_critical_illness_benefit_gurantee;

    }

    private List<M_BI_PoornSurakshaGrid_Adpter> allElementDetails;
    // LayoutInflater mInflater;
    private Context mContext;

    private int[] colors = new int[]{Color.parseColor("#DCDBDB"),
            Color.parseColor("#E8E8E8")};

    public Adapter_BI_PoornSurakshaGrid(Context context, List<M_BI_PoornSurakshaGrid_Adpter> results) {
        allElementDetails = results;
        mContext = context;
        //mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {

        return allElementDetails.size();
    }

    @Override
    public Object getItem(int position) {

        //return allElementDetails.get(position);
        return null;
        //return null;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.adapter_bi_grid_poorn_suraksha,
                    null);
            holder = new ViewHolder();
            holder.tv_policy_year = convertView.findViewById(R.id.tv_policy_year);
            holder.tv_total_premium_wo_tax = convertView.findViewById(R.id.tv_total_premium_wo_tax);
            holder.tv_death_gurantee = convertView.findViewById(R.id.tv_death_gurantee);
            holder.tv_critical_illness_benefit_gurantee = convertView.findViewById(R.id.tv_critical_illness_benefit_gurantee);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.tv_policy_year.setText(String.valueOf(allElementDetails.get(position).getPolicy_year()));
        holder.tv_total_premium_wo_tax.setText(String.valueOf(allElementDetails.get(position).getTotal_base_premium_without_tax()));
        holder.tv_death_gurantee.setText(String.valueOf(allElementDetails.get(position).getDeath_gurantee()));
        holder.tv_critical_illness_benefit_gurantee.setText(String.valueOf(allElementDetails.get(position).getcritical_illness_benefit_gurantee()));


        int colorPos = position % colors.length;
        convertView.setBackgroundColor(colors[colorPos]);

        return convertView;
    }

}
