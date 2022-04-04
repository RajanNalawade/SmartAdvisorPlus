package sbilife.com.pointofsale_bancaagency.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MHRNotificationDailyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {


            CommonMethods commonMethods = new CommonMethods();
//            if (mHour == 01 && mMinute == 50 && mAM_PM == 1 || value.equalsIgnoreCase("0")) {
                SimpleDateFormat formatter = new SimpleDateFormat(
                        "dd-MMM-yyyy", Locale.ENGLISH);

                Date d1 = null, d2 = null;


                String finalDate = "01-FEB-2020";
                String currenDate = formatter.format(new Date());
                try {
                    d1 = formatter.parse(finalDate);
                    d2 = formatter.parse(currenDate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //long difference = Math.abs(d1.getTime() - d2.getTime());
                long difference = d1.getTime() - d2.getTime();
                long differenceDates = difference / (24 * 60 * 60 * 1000);

                if(differenceDates > 0){
                    String message = "Submission of MHR is to be done in Digital form only from 1st Feb, 2020. " +
                            differenceDates + " days left before this becomes mandatory." +
                            "It is easy and convenient. SMS/email with link of Digital MHR is being sent to you " +
                            "for proposals where MHR is required. Open this link to submit MHR";

                    // Gets an instance of the NotificationManager service

                                /*AppSharedPreferences.setData(context, commonMethods.PERSISTENCY_NOTIFICATION,
                                                "1");*/
                    String title = "Digital MHR Submission";
                    int notificationId = -8;
                /*Intent i = new Intent(context, AgencyReportsPersistency.class);
                i.putExtra("fromHome", "Y");
                PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), i, 0);*/
                    commonMethods.commonNotification(context, null, title, message, notificationId);
                }


                AppSharedPreferences.setData(context, new AppSharedPreferences().PERSISTENCY_KEY, "1");
           // }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT);
        }
    }
}
