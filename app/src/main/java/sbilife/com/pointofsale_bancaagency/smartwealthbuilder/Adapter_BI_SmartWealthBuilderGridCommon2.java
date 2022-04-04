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

class Adapter_BI_SmartWealthBuilderGridCommon2 extends BaseAdapter {

    static class ViewHolder {


        TextView tv_policy_year;
        TextView tv_premium;
        TextView tv_premium_allocation_charge;
        TextView tv_annulized_premium_allocation_charge;
        TextView tv_mortality_charge1;
        TextView tv_service_tax_on_mortality_charge1;
        TextView tv_policy_administration_charge;
        TextView tv_guranteed_addition1;
        TextView tv_total_charge1;
        TextView str_AddToTheFund4Pr;
        TextView tv_fund_before_fmc1;
        TextView tv_fund_management_charge1;
        TextView tv_fund_value_at_end1;
        TextView tv_surrender_value1;
        TextView tv_death_benefit1;
    }

    private final List<M_BI_SmartWealthBuilderAdapterCommon2> allElementDetails;
    // LayoutInflater mInflater;
    private final Context mContext;

    private final int[] colors = new int[]{Color.parseColor("#DCDBDB"),
            Color.parseColor("#E8E8E8")};
    //
    // private int[] colors = new int[] { Color.parseColor("#DCDBDB"),
    // Color.parseColor("#E8E8E8") };

    public Adapter_BI_SmartWealthBuilderGridCommon2(Context context, List<M_BI_SmartWealthBuilderAdapterCommon2> results) {
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
            convertView = mInflater.inflate(R.layout.adapter_bi_grid_smart_wealth_builder_common2,
                    null);
            holder = new ViewHolder();

            holder.tv_policy_year = convertView.findViewById(R.id.tv_policy_year);
            holder.tv_premium = convertView.findViewById(R.id.tv_premium);
            holder.tv_premium_allocation_charge = convertView.findViewById(R.id.tv_premium_allocation_charge);
            holder.tv_annulized_premium_allocation_charge = convertView.findViewById(R.id.tv_annulized_premium_allocation_charge);
            holder.tv_mortality_charge1 = convertView.findViewById(R.id.tv_mortality_charge1);
            holder.tv_service_tax_on_mortality_charge1 = convertView.findViewById(R.id.tv_service_tax_on_mortality_charge1);
            holder.tv_policy_administration_charge = convertView.findViewById(R.id.tv_policy_administration_charge);
            holder.tv_guranteed_addition1 = convertView.findViewById(R.id.tv_guranteed_addition1);
            holder.tv_total_charge1 = convertView.findViewById(R.id.tv_total_charge1);
            holder.str_AddToTheFund4Pr = convertView.findViewById(R.id.str_AddToTheFund4Pr);
            holder.tv_fund_before_fmc1 = convertView.findViewById(R.id.tv_fund_before_fmc1);
            holder.tv_fund_management_charge1 = convertView.findViewById(R.id.tv_fund_management_charge1);
            holder.tv_fund_value_at_end1 = convertView.findViewById(R.id.tv_fund_value_at_end1);
            holder.tv_surrender_value1 = convertView.findViewById(R.id.tv_surrender_value1);
            holder.tv_death_benefit1 = convertView.findViewById(R.id.tv_death_benefit1);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_policy_year.setText(String.valueOf(allElementDetails.get(position).getPolicy_year()));
        holder.tv_premium.setText(String.valueOf(allElementDetails.get(position).getPremium()));
        holder.tv_premium_allocation_charge.setText(String.valueOf(allElementDetails.get(position).getPremium_allocation_charge()));
        holder.tv_annulized_premium_allocation_charge.setText(String.valueOf(allElementDetails.get(position).getAnnulized_premium_allocation_charge()));
        holder.tv_mortality_charge1.setText(String.valueOf(allElementDetails.get(position).getMortality_charge1()));
        holder.tv_service_tax_on_mortality_charge1.setText(String.valueOf(allElementDetails.get(position).getService_tax_on_mortality_charge1()));
        holder.tv_policy_administration_charge.setText(String.valueOf(allElementDetails.get(position).getPolicy_administration_charge()));
        holder.tv_guranteed_addition1.setText(String.valueOf(allElementDetails.get(position).getGuranteed_addition1()));
        holder.tv_total_charge1.setText(String.valueOf(allElementDetails.get(position).getTotal_charge1()));
        holder.str_AddToTheFund4Pr.setText(String.valueOf(allElementDetails.get(position).getStr_AddToTheFund4Pr()));
        holder.tv_fund_before_fmc1.setText(String.valueOf(allElementDetails.get(position).getFund_before_fmc1()));
        holder.tv_fund_management_charge1.setText(String.valueOf(allElementDetails.get(position).getFund_management_charge1()));
        holder.tv_fund_value_at_end1.setText(String.valueOf(allElementDetails.get(position).getFund_value_at_end1()));
        holder.tv_surrender_value1.setText(String.valueOf(allElementDetails.get(position).getSurrender_value1()));
        holder.tv_death_benefit1.setText(String.valueOf(allElementDetails.get(position).getDeath_benefit1()));

        int colorPos = position % colors.length;
        convertView.setBackgroundColor(colors[colorPos]);

        return convertView;
    }
}
