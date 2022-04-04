package sbilife.com.pointofsale_bancaagency.retiresmart;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sbilife.com.pointofsale_bancaagency.R;


public class Adapter_BI_RetireSmartGridCommon2 extends BaseAdapter {

    public static class ViewHolder {


        TextView tv_policy_year;
        TextView tv_premium;
        TextView tv_premium_allocation_charge;
        TextView tv_annulized_premium_allocation_charge;
        TextView tv_mortality_charge1;
        TextView tv_service_tax_on_mortality_charge1;
        TextView tv_policy_administration_charge;
        TextView Guarantee_charge;
        TextView tv_total_charge1;
        TextView str_AddToTheFund4Pr;
        TextView Guaranteed_Addition;
        TextView Terminal_Addition;
        TextView tv_fund_before_fmc1;
        TextView tv_fund_management_charge1;
        TextView tv_fund_value_at_end1;
        TextView tv_surrender_value1;
        TextView tv_death_benefit1;
    }

    List<M_BI_RetireSmartAdapterCommon2> allElementDetails;
    // LayoutInflater mInflater;
    Context mContext;

    private int[] colors = new int[]{Color.parseColor("#DCDBDB"),
            Color.parseColor("#E8E8E8")};
    //
    // private int[] colors = new int[] { Color.parseColor("#DCDBDB"),
    // Color.parseColor("#E8E8E8") };

    public Adapter_BI_RetireSmartGridCommon2(Context context, List<M_BI_RetireSmartAdapterCommon2> results) {
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
            convertView = mInflater.inflate(R.layout.adapter_bi_grid_retire_smart_common2,
                    null);
            holder = new ViewHolder();

            holder.tv_policy_year = (TextView) convertView.findViewById(R.id.tv_policy_year);
            holder.tv_premium = (TextView) convertView.findViewById(R.id.tv_premium);
            holder.tv_premium_allocation_charge = (TextView) convertView.findViewById(R.id.tv_premium_allocation_charge);
            holder.tv_annulized_premium_allocation_charge = (TextView) convertView.findViewById(R.id.tv_annulized_premium_allocation_charge);
            holder.tv_mortality_charge1 = (TextView) convertView.findViewById(R.id.tv_mortality_charge1);
            holder.tv_service_tax_on_mortality_charge1 = (TextView) convertView.findViewById(R.id.tv_service_tax_on_mortality_charge1);
            holder.tv_policy_administration_charge = (TextView) convertView.findViewById(R.id.tv_policy_administration_charge);
            holder.Guarantee_charge = (TextView) convertView.findViewById(R.id.Guarantee_charge);
            holder.tv_total_charge1 = (TextView) convertView.findViewById(R.id.tv_total_charge1);
            holder.str_AddToTheFund4Pr = (TextView) convertView.findViewById(R.id.str_AddToTheFund4Pr);
            holder.Guaranteed_Addition = (TextView) convertView.findViewById(R.id.Guaranteed_Addition);
            holder.Terminal_Addition = (TextView) convertView.findViewById(R.id.Terminal_Addition);
            holder.tv_fund_before_fmc1 = (TextView) convertView.findViewById(R.id.tv_fund_before_fmc1);
            holder.tv_fund_management_charge1 = (TextView) convertView.findViewById(R.id.tv_fund_management_charge1);
            holder.tv_fund_value_at_end1 = (TextView) convertView.findViewById(R.id.tv_fund_value_at_end1);
            holder.tv_surrender_value1 = (TextView) convertView.findViewById(R.id.tv_surrender_value1);
            holder.tv_death_benefit1 = (TextView) convertView.findViewById(R.id.tv_death_benefit1);

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
        holder.Guarantee_charge.setText(String.valueOf(allElementDetails.get(position).getGuarantee_charge()));
        holder.tv_total_charge1.setText(String.valueOf(allElementDetails.get(position).getTotal_charge1()));
        holder.str_AddToTheFund4Pr.setText(String.valueOf(allElementDetails.get(position).getStr_AddToTheFund4Pr()));
        holder.Guaranteed_Addition.setText(String.valueOf(allElementDetails.get(position).getGuaranteed_Addition()));
        holder.Terminal_Addition.setText(String.valueOf(allElementDetails.get(position).getTerminal_Addition()));
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
