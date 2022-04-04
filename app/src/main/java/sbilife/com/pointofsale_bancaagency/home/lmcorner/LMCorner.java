package sbilife.com.pointofsale_bancaagency.home.lmcorner;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class LMCorner extends AppCompatActivity implements View.OnClickListener {


    private CommonMethods mCommonMethods;
    private Context context;
    private final ArrayList<LMMenu> listMenuItem = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_lmcorner);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        mCommonMethods = new CommonMethods();
        context = this;
        mCommonMethods.setApplicationToolbarMenu(this, "LM Corner");


        /*LinearLayout layoutLMClubMemberShipReport = findViewById(R.id.layoutLMClubMemberShipReport);
        LinearLayout layoutCOTTOTTentativeReport = findViewById(R.id.layoutCOTTOTTentativeReport);
        LinearLayout layoutJOTCReport = findViewById(R.id.layoutJOTCReport);
        LinearLayout llDOGHCovid = findViewById(R.id.llDOGHCovid);
        LinearLayout lltMonthlyGradAllowReport = findViewById(R.id.lltMonthlyGradAllowReport);
        LinearLayout llCovidSelfDeclaration = findViewById(R.id.llCovidSelfDeclaration);
        layoutLMClubMemberShipReport.setOnClickListener(this);
        layoutCOTTOTTentativeReport.setOnClickListener(this);
        layoutJOTCReport.setOnClickListener(this);
        llDOGHCovid.setOnClickListener(this);
        lltMonthlyGradAllowReport.setOnClickListener(this);
        llCovidSelfDeclaration.setOnClickListener(this);*/

        listMenuItem.add(new LMMenu(R.drawable.ic_icon_cot_tot_report, "COT and TOT Tentative Standings Report",
                COTTOTTentativeReportActivity.class, false, false, ""));

        listMenuItem.add(new LMMenu(R.drawable.icon_ppc_download, "JOTC Report",
                JOTCActivity.class, false, false, ""));

       /* listMenuItem.add(new LMMenu(R.drawable.dogh_covid, "DOGH/Covid",
                DOGHCovidActivity.class, false, false, "")); */

        listMenuItem.add(new LMMenu(R.drawable.dogh_covid, "DOGH/Covid",
                CovidDoghNextActivity.class, false, false, ""));


        listMenuItem.add(new LMMenu(R.drawable.ic_covid_self_declaration, "Covid Self Declaration",
                CovidSelfDeclarationActivity.class, false, false, ""));

        listMenuItem.add(new LMMenu(R.drawable.ic_monthly_graduation_allowance_report, "Monthly Graduation Allowance Report",
                MonthlyGraduationAllowanceReportActivity.class, false, false, ""));

        listMenuItem.add(new LMMenu(R.drawable.ic_icon_club_memebership_report, "LM Club Membership Report",
                LMClubMembershipReportActivity.class, false, false, ""));

        /*listMenuItem.add(new LMMenu(R.drawable.lm_survey, "LM Survey",
                IAClubDetailActivity.class, false, false, ""));*/
        String userType = mCommonMethods.GetUserType(context);

        if (userType.equalsIgnoreCase("AGENT")) {
            listMenuItem.add(new LMMenu(R.drawable.ic_future_secure_fund, "Contest",
                    ContestsLMActivity.class, false, false, ""));
        }


        RecyclerView rbCarouselHome = findViewById(R.id.recyclerView);
        // set LayoutManager to RecyclerView
        rbCarouselHome.setLayoutManager(new GridLayoutManager(LMCorner.this, 2));

        // call the constructor of CustomAdapter to send the reference and data to Adapter
        LMMenuAdapter lmMenuAdapter = new LMMenuAdapter(listMenuItem);
        rbCarouselHome.setAdapter(lmMenuAdapter);
        rbCarouselHome.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onClick(View view) {

       /* switch (view.getId()) {
            case R.id.layoutLMClubMemberShipReport:
                mCommonMethods.callActivity(context, LMClubMembershipReportActivity.class);
                break;

            case R.id.layoutCOTTOTTentativeReport:
                mCommonMethods.callActivity(context, COTTOTTentativeReportActivity.class);
                break;

            case R.id.layoutJOTCReport:
                mCommonMethods.callActivity(context, JOTCActivity.class);
                break;
            case R.id.llDOGHCovid:
                mCommonMethods.callActivity(context, DOGHCovidActivity.class);
                break;

            case R.id.llCovidSelfDeclaration:
                mCommonMethods.callActivity(context, CovidSelfDeclarationActivity.class);
                break;
            case R.id.lltMonthlyGradAllowReport:
                mCommonMethods.callActivity(context, MonthlyGraduationAllowanceReportActivity.class);
                break;
        }*/
    }

    public class LMMenuAdapter extends RecyclerView.Adapter<LMMenuAdapter.ViewHolderAdapter> {

        private final ArrayList<LMMenu> lstAdapterList;

        LMMenuAdapter(ArrayList<LMMenu> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public int getItemCount() {
            return lstAdapterList.size();
        }

        @Override
        public ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_aob_req_dtls_dashboard, parent, false);

            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final ViewHolderAdapter holder, final int position) {
            holder.imageButtonAgentIcon.setImageDrawable(context.getResources().getDrawable(lstAdapterList.get(position).getIconDrawable()));
            holder.textviewAgentTitle.setText(lstAdapterList.get(position).getStrMenuTitle());
            holder.textviewAgentTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            holder.constraintAOBReqDtls.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Class aClass = lstAdapterList.get(position).getaClass();
                    if (aClass != null) {
                        /*boolean isHomeTag = lstAdapterList.get(position).isHomeTag();
                        if (isHomeTag) {
                            mCommonMethods.callActivityWithHomeTagYes(context, aClass);
                        } else {
                            if (lstAdapterList.get(position).getStrMenuTitle().equalsIgnoreCase("Short MHR")) {
                                Intent i = new Intent(context, MHRProposalListActivity.class);
                                i.putExtra("fromHome", "MHR");
                                context.startActivity(i);
                            } else {
                                mCommonMethods.callActivity(context, aClass);
                            }*/
                        mCommonMethods.callActivity(context, aClass);
                    }


                }
            });
        }

        @Override
        public void onViewRecycled(ViewHolderAdapter holder) {
            holder.itemView.setOnLongClickListener(null);
            super.onViewRecycled(holder);
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
        }
    }

    class LMMenu {
        private final int iconDrawable;
        private final String strMenuTitle;
        private final Class aClass;
        private final boolean homeTag;
        private final boolean driveLink;
        private final String link;

        LMMenu(int iconDrawable, String strMenuTitle, Class aClass, boolean homeTag,
               boolean driveLink, String link) {
            this.strMenuTitle = strMenuTitle;
            this.iconDrawable = iconDrawable;
            this.aClass = aClass;
            this.homeTag = homeTag;
            this.driveLink = driveLink;
            this.link = link;
        }

        Class getaClass() {
            return aClass;
        }

        boolean isHomeTag() {
            return homeTag;
        }

        boolean isDriveLink() {
            return driveLink;
        }

        String getLink() {
            return link;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LMMenu homeMenu = (LMMenu) o;
            return iconDrawable == homeMenu.iconDrawable &&
                    strMenuTitle.equals(homeMenu.strMenuTitle);
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public int hashCode() {
            return Objects.hash(iconDrawable, strMenuTitle);
        }

        int getIconDrawable() {
            return iconDrawable;
        }


        String getStrMenuTitle() {
            return strMenuTitle;
        }

    }
}
