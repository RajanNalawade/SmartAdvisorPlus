package sbilife.com.pointofsale_bancaagency.home.home_menu_adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.agent_on_boarding.ActivityAOB_Menu;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.home.mhr.MHRProposalListActivity;

public class HomeMenuAdapter extends ListAdapter<HomeMenu, HomeMenuAdapter.ViewHolderAdapter> {

    private CommonMethods mCommonMethods;
    private Context mContext;

    public HomeMenuAdapter(Context mContext,
                           DiffUtil.ItemCallback<HomeMenu> diffCallback) {
        super(diffCallback);
        this.mContext = mContext;
        this.mCommonMethods = new CommonMethods();
    }

    @Override
    public ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_aob_req_dtls_dashboard, parent, false);

        return new ViewHolderAdapter(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolderAdapter holder, int position) {

        HomeMenu item = getItem(position);
        holder.bind(item);

        /*holder.imageButtonAgentIcon.setImageDrawable(mContext.getResources().getDrawable(lstAdapterList.get(position).getIconDrawable()));
        holder.textviewAgentTitle.setText(lstAdapterList.get(position).getStrMenuTitle());

        holder.constraintAOBReqDtls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class aClass = lstAdapterList.get(position).getaClass();
                if (aClass != null) {
                    boolean isHomeTag = lstAdapterList.get(position).isHomeTag();
                    if (isHomeTag) {
                        mCommonMethods.callActivityWithHomeTagYes(mContext, aClass);
                    } else {
                        if (lstAdapterList.get(position).getStrMenuTitle().equalsIgnoreCase("Short MHR")) {
                            Intent i = new Intent(mContext, MHRProposalListActivity.class);
                            i.putExtra("fromHome", "MHR");
                            mContext.startActivity(i);
                        } else {

                            Bundle mBundle = lstAdapterList.get(position).getmBundle();
                            if (mBundle != null) {
                                Intent i = new Intent(mContext, ActivityAOB_Menu.class);
                                i.putExtras(mBundle);
                                mContext.startActivity(i);
                            } else {
                                mCommonMethods.callActivity(mContext, aClass);
                            }
                        }
                        //mCommonMethods.callActivity(mContext, aClass);
                    }
                } else {
                    boolean isDriveLink = lstAdapterList.get(position).isDriveLink();
                    if (isDriveLink) {
                        mCommonMethods.loadDriveURL(lstAdapterList.get(position).getLink(), mContext);
                    }
                }
            }
        });*/
    }

    public static class AdapterDiffUtil extends DiffUtil.ItemCallback<HomeMenu> {

        @Override
        public boolean areItemsTheSame(@NonNull HomeMenu oldItem, @NonNull HomeMenu newItem) {
            return oldItem.getStrMenuTitle().equals(newItem.getStrMenuTitle());
        }

        @Override
        public boolean areContentsTheSame(@NonNull HomeMenu oldItem, @NonNull HomeMenu newItem) {
            return oldItem.equals(newItem);
        }
    }

    public class ViewHolderAdapter extends RecyclerView.ViewHolder {

        private final ImageButton imageButtonAgentIcon;
        private final TextView textviewAgentTitle;
        private final LinearLayout constraintAOBReqDtls;

        ViewHolderAdapter(View v) {
            super(v);
            constraintAOBReqDtls = v.findViewById(R.id.constraintAOBReqDtls);
            imageButtonAgentIcon = v.findViewById(R.id.imageButtonAgentIcon);
            textviewAgentTitle = v.findViewById(R.id.textviewAgentTitle);
        }

        public void bind(HomeMenu item) {

            imageButtonAgentIcon.setImageDrawable(mContext.getResources().getDrawable(item.getIconDrawable()));
            textviewAgentTitle.setText(item.getStrMenuTitle());

            constraintAOBReqDtls.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Class aClass = item.getaClass();
                    if (aClass != null) {
                        boolean isHomeTag = item.isHomeTag();
                        if (isHomeTag) {
                            mCommonMethods.callActivityWithHomeTagYes(mContext, aClass);
                        } else {
                            if (item.getStrMenuTitle().equalsIgnoreCase("Short MHR")) {
                                Intent i = new Intent(mContext, MHRProposalListActivity.class);
                                i.putExtra("fromHome", "MHR");
                                mContext.startActivity(i);
                            } else {

                                Bundle mBundle = item.getmBundle();
                                if (mBundle != null) {
                                    Intent i = new Intent(mContext, ActivityAOB_Menu.class);
                                    i.putExtras(mBundle);
                                    mContext.startActivity(i);
                                } else {
                                    mCommonMethods.callActivity(mContext, aClass);
                                }
                            }
                            //mCommonMethods.callActivity(mContext, aClass);
                        }
                    } else {
                        boolean isDriveLink = item.isDriveLink();
                        if (isDriveLink) {
                            mCommonMethods.loadDriveURL(item.getLink(), mContext);
                        }
                    }
                }
            });
        }
    }
}
