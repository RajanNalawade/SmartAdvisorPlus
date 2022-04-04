package sbilife.com.pointofsale_bancaagency;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.File;
import java.util.Arrays;

import sbilife.com.pointofsale_bancaagency.common.AppSharedPreferences;
import sbilife.com.pointofsale_bancaagency.futureplanner.FuturePlannerActivity;
import sbilife.com.pointofsale_bancaagency.home.CarouselHomeActivity;
import sbilife.com.pointofsale_bancaagency.home.dashboard.NewDashboardAgent;
import sbilife.com.pointofsale_bancaagency.home.dashboard.NewDashboardCIF;
import sbilife.com.pointofsale_bancaagency.new_bussiness.NewBusinessHomeGroupingActivity;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServicingHomeGroupingActivity;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgentServicingHomeGroupingActivity;
import sbilife.com.pointofsale_bancaagency.utility.DocumentsUploadActivity;

public class CreateMenuDialog {

    private Context mcontext;
    private String Name = "HELLO";
    //private  LinearLayout homeLinearLayout;
    //public static File file;
    //private Activity activity;
    private String userType;
    public File file;

    public CreateMenuDialog(String name, Context context) {
        mcontext = context;
        Name = name;
        CommonMethods commonMethods = new CommonMethods();

        userType = commonMethods.GetUserType(mcontext);
        //activity = (Activity) mcontext;
    }

    @SuppressLint({"DefaultLocale", "InflateParams"})
    @SuppressWarnings("deprecation")
    public void createMenu() {
        LayoutInflater inflater = (LayoutInflater) mcontext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.window_application_menu, null);
        TextView tv_name = v.findViewById(R.id.tv_name);
		/*String namecapital = Name.substring(0, 1).toUpperCase()
				+ Name.substring(1).toLowerCase();*/
        tv_name.setText("Hello, " + Name);
        ListView listView = v.findViewById(R.id.listpopup);
        String[] values = new String[]{"Profile", "Home", "Dashboard",
                "Products", "New Business", "Requirement Upload",
                "Future Planner", "Servicing", "Calender", "Sales Kit", "Help",/* "Settings",*/
                "Logout"};

        MenuItemAdapter adapter = new MenuItemAdapter(mcontext,
                Arrays.asList(values));
        listView.setAdapter(adapter);

        DisplayMetrics dm = new DisplayMetrics();
        ((AppCompatActivity) mcontext).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenheight = dm.heightPixels;

        final PopupWindow pw = new PopupWindow(v, (screenWidth * 4) / 5,
                screenheight + 20, true);
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setOutsideTouchable(true);

        pw.showAtLocation(v.findViewById(R.id.layout_popup_list), Gravity.LEFT,
                0, -500);
        Animation animation = AnimationUtils.loadAnimation(mcontext,
                R.anim.slideright);

        listView.startAnimation(animation);
        tv_name.startAnimation(animation);

        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parentView, View childView,
                                    int position, long id) {
                pw.dismiss();
                Intent_to_Position(position);
            }

            private void Intent_to_Position(int position) {

                String activityName = ((AppCompatActivity) mcontext).getClass().toString();
                boolean isActivitySelected = false;
                Intent i = null;
                switch (position) {
                    case 0:
                        isActivitySelected = true;
                        i = new Intent(mcontext, ProfileActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        mcontext.startActivity(i);
                        break;
                    case 1:
                        isActivitySelected = true;
                        i = new Intent(mcontext, CarouselHomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mcontext.startActivity(i);
                        break;
                    case 2:
                        isActivitySelected = true;
                        if (userType.equalsIgnoreCase("Agent") || userType.contentEquals("UM")) {
                            i = new Intent(mcontext, NewDashboardAgent.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            mcontext.startActivity(i);
                        } else {
                            i = new Intent(mcontext, NewDashboardCIF.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            mcontext.startActivity(i);
                        }
                        break;

                    case 3:
                        isActivitySelected = true;

                        if (userType.equalsIgnoreCase("Agent") || userType.contentEquals("UM")
                                || userType.contentEquals("BSM") || userType.contentEquals("DSM")
                                || userType.contentEquals("ASM") || userType.contentEquals("RSM")) {
                            i = new Intent(mcontext, CarouselProductActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            mcontext.startActivity(i);
                        } else {
                            i = new Intent(mcontext, BancaProductActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            mcontext.startActivity(i);
                        }
                        break;
                    case 4:
                        isActivitySelected = true;
                        i = new Intent(mcontext,
                                NewBusinessHomeGroupingActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        mcontext.startActivity(i);
                        break;
                    case 5:
                        isActivitySelected = true;
                        i = new Intent(mcontext, DocumentsUploadActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        mcontext.startActivity(i);
                        break;
                    case 6:
                        isActivitySelected = true;
                        i = new Intent(mcontext, FuturePlannerActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        mcontext.startActivity(i);
                        break;
                    case 7:
                        isActivitySelected = true;
                        if (userType.equalsIgnoreCase("Agent") || userType.contentEquals("UM")
                                || userType.equalsIgnoreCase("BSM")
                                || userType.equalsIgnoreCase("DSM")
                                || userType.equalsIgnoreCase("ASM")
                                || userType.equalsIgnoreCase("RSM")) {
                            i = new Intent(mcontext,
                                    AgentServicingHomeGroupingActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            mcontext.startActivity(i);
                        } else {
                            i = new Intent(mcontext,
                                    ServicingHomeGroupingActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            mcontext.startActivity(i);
                        }
                        break;
                    case 8:
                        isActivitySelected = true;
                        if (userType.equalsIgnoreCase("Agent") || userType.contentEquals("UM")) {
                            i = new Intent(mcontext, CarouselCalendarView.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            mcontext.startActivity(i);
                        } else {
                            i = new Intent(mcontext, BancaCalendarView.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            mcontext.startActivity(i);
                        }


                        break;
                    case 9:
                        isActivitySelected = true;
                        new CommonMethods().loadDriveURL("https://drive.google.com/open?id=1xc9KJJIAmee6FXIladPhVsFJ1aDzTFaX", mcontext);
                        break;
                    case 10:
                        isActivitySelected = true;
                        if (userType.equalsIgnoreCase("Agent") || userType.contentEquals("UM")) {
                            //do not uncomment history flag as it is finishing activity when other activity is called
                            i = new Intent(mcontext, CarouselHelpActivity.class);
                            //i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY );
                            mcontext.startActivity(i);
                        } else {
                            i = new Intent(mcontext, BancaHelpActivity.class);
                            //i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY );
                            mcontext.startActivity(i);
                        }
                        break;
				/*case 11:
					isActivitySelected = true;
					i = new Intent(mcontext,SettingActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY );
					mcontext.startActivity(i);
					break;*/
                    case 11:
                        logout();
                        break;

                }

                try {
                    if (isActivitySelected) {
                        if (isFinishActivty(activityName)) {
                            ((AppCompatActivity) mcontext).finish();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private boolean isFinishActivty(String activityName) {
        return !(activityName.contains("ProfileActivity") || activityName.contains("CarouselHomeActivity")
                || activityName.contains("NewDashboardAgent")
                || activityName.contains("NewDashboardCIF") || activityName.contains("CarouselProductActivity")
                || activityName.contains("BancaProductActivity") || activityName.contains("NewBusinessHomeGroupingActivity")
                || activityName.contains("DocumentsUploadActivity") || activityName.contains("FuturePlannerActivity")
                || activityName.contains("AgentServicingHomeGroupingActivity") || activityName.contains("ServicingHomeGroupingActivity")
                || activityName.contains("CarouselCalendarView") || activityName.contains("BancaCalendarView")
                || activityName.contains("CarouselHelpActivity")
                || activityName.contains("BancaHelpActivity") || activityName.contains("SettingActivity"));
    }

    private void logout() {

        final Dialog dialog = new Dialog(mcontext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_yes_no);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        Button bt_yes = dialog.findViewById(R.id.bt_yes);
        Button bt_no = dialog.findViewById(R.id.bt_no);
        ((TextView) dialog.findViewById(R.id.tv_title))
                .setText("Are you sure you want to logout?");
        bt_yes.setText("Yes");
        bt_no.setText("No");
        bt_yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SharedPreferences preferences = ((AppCompatActivity) mcontext).getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                CommonMethods commonMethods = new CommonMethods();
                editor.putString(commonMethods.getNOTIFICATION_PREFERENCE(), "false");
                editor.putString(commonMethods.getDASHBOARD_RENEWAL_UPDATE_PREFERENCE(), "false");
                editor.putString(commonMethods.getLOGGED_PROPOSAL_COUNT_NOTIFICATION_PREFERENCE(), "false");
                editor.putString(commonMethods.getNON_MEDICAL_PENDING_REQ_COUNT_NOTIFICATION_PREFERENCE(), "false");
                editor.putString(commonMethods.getMEDICAL_PENDING_REQ_COUNT_NOTIFICATION_PREFERENCE(), "false");
                editor.putString(commonMethods.MATURITY_LIST_NOTIFICATION_PREFERENCE, "false");
                editor.putString(commonMethods.getClaimRequirementInfo(), "false");
                editor.putString(commonMethods.SB_DUE_LIST_NOTIFICATION_PREFERENCE, "false");
                editor.putString(commonMethods.getRevivalNotificationInfo(), "False");
                editor.putString(commonMethods.getKYCMissingNotification(), "False");
                editor.apply();
                AppSharedPreferences.setData(mcontext, new AppSharedPreferences().PERSISTENCY_KEY, "0");
                new CommonMethods().logoutToLoginActivity(mcontext);
            }
        });

        bt_no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();

    }

    public void links() {
        Intent i = new Intent(mcontext, CarouselLinksActivity.class);
        mcontext.startActivity(i);
    }

}
