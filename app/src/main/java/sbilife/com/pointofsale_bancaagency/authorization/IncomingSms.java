package sbilife.com.pointofsale_bancaagency.authorization;

/**
 * Created by e24356 on 01-03-2017.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class IncomingSms extends BroadcastReceiver {


    // Get the object of SmsManager

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();
        String str="";
        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);

                    String message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: "+ currentMessage.getDisplayOriginatingAddress() + "; message: " + message);

                    try
                    {
                        if (currentMessage.getDisplayOriginatingAddress().equals("AM-SBILIF")/*|| senderNum .equals("500")*/)
                        {
                            str=message.substring(7,12);

                            Intent a = new Intent("IncomingSms");
                            // Data you need to pass to activity
                            a.putExtra("message", str);

                            context.sendBroadcast(a);
                        }
                    }
                    catch(Exception e){}

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }
}