package sbilife.com.pointofsale_bancaagency.smartwealthbuilder;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sbilife.com.pointofsale_bancaagency.R;

public class Adapter_BI_SmartWealthBuilderGridCommon extends BaseAdapter {

    public static class ViewHolder {


        TextView tv_policy_year;
        TextView tv_premium;
        TextView tv_mortality_charge1;
        TextView tv_total_charge1;
        TextView tv_service_tax_on_mortality_charge1;
        TextView tv_fund_value_at_end1;
        TextView tv_surrender_value1;
        TextView tv_death_benefit1;
        TextView tv_mortality_charge2;
        TextView tv_total_charge2;
        TextView tv_service_tax_on_mortality_charge2;
        TextView tv_fund_value_at_end2;
        TextView tv_surrender_value2;
        TextView tv_death_benefit2;
        TextView tv_commission;

    }

    List<M_BI_SmartWealthBuilderAdapterCommon> allElementDetails;
    // LayoutInflater mInflater;
    Context mContext;

    private int[] colors = new int[]{Color.parseColor("#DCDBDB"),
            Color.parseColor("#E8E8E8")};
    //
    // private int[] colors = new int[] { Color.parseColor("#DCDBDB"),
    // Color.parseColor("#E8E8E8") };

    public Adapter_BI_SmartWealthBuilderGridCommon(Context context, List<M_BI_SmartWealthBuilderAdapterCommon> results) {
        allElementDetails = results;
        mContext = context;
        //mInflater = LayoutInflater.from(context);
    }

    //	public Adapter_BI_grid(Context context, String[] results) {
//		allElementDetails = Arrays.asList(results);
//		mInflater = LayoutInflater.from(context);
//	}
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return allElementDetails.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        //return allElementDetails.get(position);
        return null;
        //return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.adapter_bi_grid_smart_wealth_builder_common,
                    null);
            holder = new ViewHolder();

            holder.tv_policy_year = (TextView) convertView.findViewById(R.id.tv_policy_year);
            holder.tv_premium = (TextView) convertView.findViewById(R.id.tv_premium);
            holder.tv_mortality_charge1 = (TextView) convertView.findViewById(R.id.tv_mortality_charge1);
            holder.tv_total_charge1 = (TextView) convertView.findViewById(R.id.tv_total_charge1);
            holder.tv_service_tax_on_mortality_charge1 = (TextView) convertView.findViewById(R.id.tv_service_tax_on_mortality_charge1);
            holder.tv_fund_value_at_end1 = (TextView) convertView.findViewById(R.id.tv_fund_value_at_end1);
            holder.tv_surrender_value1 = (TextView) convertView.findViewById(R.id.tv_surrender_value1);
            holder.tv_death_benefit1 = (TextView) convertView.findViewById(R.id.tv_death_benefit1);
            holder.tv_mortality_charge2 = (TextView) convertView.findViewById(R.id.tv_mortality_charge2);
            holder.tv_total_charge2 = (TextView) convertView.findViewById(R.id.tv_total_charge2);
            holder.tv_service_tax_on_mortality_charge2 = (TextView) convertView.findViewById(R.id.tv_service_tax_on_mortality_charge2);
            holder.tv_fund_value_at_end2 = (TextView) convertView.findViewById(R.id.tv_fund_value_at_end2);
            holder.tv_surrender_value2 = (TextView) convertView.findViewById(R.id.tv_surrender_value2);
            holder.tv_death_benefit2 = (TextView) convertView.findViewById(R.id.tv_death_benefit2);
            holder.tv_commission = (TextView) convertView.findViewById(R.id.tv_commission);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.tv_policy_year.setText(String.valueOf(allElementDetails.get(position).getPolicy_year()));
        holder.tv_premium.setText(String.valueOf(allElementDetails.get(position).getPremium()));
        holder.tv_mortality_charge1.setText(String.valueOf(allElementDetails.get(position).getMortality_charge1()));
        holder.tv_total_charge1.setText(String.valueOf(allElementDetails.get(position).getTotal_charge1()));
        holder.tv_service_tax_on_mortality_charge1.setText(String.valueOf(allElementDetails.get(position).getService_tax_on_mortality_charge1()));
        holder.tv_fund_value_at_end1.setText(String.valueOf(allElementDetails.get(position).getFund_value_at_end1()));
        holder.tv_surrender_value1.setText(String.valueOf(allElementDetails.get(position).getSurrender_value1()));
        holder.tv_death_benefit1.setText(String.valueOf(allElementDetails.get(position).getDeath_benefit1()));
        holder.tv_mortality_charge2.setText(String.valueOf(allElementDetails.get(position).getMortality_charge2()));
        holder.tv_total_charge2.setText(String.valueOf(allElementDetails.get(position).getTotal_charge2()));
        holder.tv_service_tax_on_mortality_charge2.setText(String.valueOf(allElementDetails.get(position).getService_tax_on_mortality_charge2()));
        holder.tv_fund_value_at_end2.setText(String.valueOf(allElementDetails.get(position).getFund_value_at_end2()));
        holder.tv_surrender_value2.setText(String.valueOf(allElementDetails.get(position).getSurrender_value2()));
        holder.tv_death_benefit2.setText(String.valueOf(allElementDetails.get(position).getDeath_benefit2()));
        holder.tv_commission.setText(String.valueOf(allElementDetails.get(position).getCommission()));

        int colorPos = position % colors.length;
        convertView.setBackgroundColor(colors[colorPos]);

        return convertView;
    }


}
