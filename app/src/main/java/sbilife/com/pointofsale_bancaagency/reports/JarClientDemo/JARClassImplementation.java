package sbilife.com.pointofsale_bancaagency.reports.JarClientDemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mosambee.lib.Currency;
import com.mosambee.lib.MosCallback;
import com.mosambee.lib.ResultData;
import com.mosambee.lib.TransactionResult;
import com.printer.sdk.PrinterConstants;

public class JARClassImplementation implements TransactionResult {

    private Context context;
    MosCallback moscCallback;
    private boolean isConnected;
    private AppCompatActivity _activity;
    //MainActivity m;
    private TransactionResultInterface resultInterface;
    public interface TransactionResultInterface{
        void returnTransactionResult(String result, ResultData resultData);
    }

    void getLocation(String user, String pswd) {
        moscCallback = new MosCallback(context);
        moscCallback.initialise(user, pswd, this);
        moscCallback.getLocation();
    }


    public void startProcess(String user, String pswd, String transType, FrameLayout container,
                             String orderId,String amount,TransactionResultInterface resultInterface,
                             String mobileNumber,String emailId) {

        this.resultInterface =  resultInterface;
        moscCallback = new MosCallback(context);
        moscCallback.initialise(user, pswd, this);
        moscCallback.initializeSignatureView(container, "#55004A", "#750F5A");
       // moscCallback.initialiseFields(transType, "", "cGjhE$@fdhj4675riesae", false, "faiz.saifi@mosambee.in", "merchantRef1", "bt", "09082013101105", orderId);
        moscCallback.initialiseFields(transType, mobileNumber, "", false, emailId, "merchantRef1", "bt", "31072020101105", orderId);

        moscCallback.processTransaction(orderId, Double.parseDouble(amount),Double.parseDouble("675466"), "", Currency.INR);
        //moscCallback.processTransaction(orderId, Double.parseDouble(amount),Double.parseDouble("675466"), "","0356");
//        moscCallback.processTransaction(orderId, Double.parseDouble(amount),Double.parseDouble("675466"), "",Currency.valueOf("0356"));
//        moscCallback.processTransaction(orderId, Double.parseDouble(amount),Double.parseDouble("675466"), "");

    }
    public void stopProcess() {
        moscCallback = new MosCallback(context);
        moscCallback.posReset();
    }

    public void sendEmail(String transactionID, String email) {
        moscCallback = new MosCallback(context);
        moscCallback.sendEmail(transactionID,email);
    }

    public void sendSMS(String transactionID, String mobileNumber) {
        moscCallback = new MosCallback(context);
        moscCallback.sendSMS(Long.parseLong(transactionID),mobileNumber);
    }
    public void setContext(Context context) {
        this.context = context;
       // m = new MainActivity();
    }

    public void setActivity(AppCompatActivity act) {
        _activity = act;
    }

    @Override
    public void onCommand(String command) {
      //  m = new MainActivity();
      //  m.setCommand(command);
     //   resultInterface.returnTransactionResult(command);
    }

    @Override
    public void onResult(ResultData result) {
        System.out.println();
        System.out.println("\n-----Result: "+result.getResult()+ "\n-----Reason: " + result.getReason()+"\n-----Data: "+ result.getTransactionData() );
        result.getResult();

        resultInterface.returnTransactionResult("",result);
       // m = new MainActivity();


       /* if(result.getResult()){
            moscCallback.sendEmail(result.getTransactionId(),"9768693970");
        }*/

      //  m.setData(result);

        TRACE.d("Transaction Response:::"+result.getTransactionData());
        /*if(result.getResult())
        {
            try {

                moscCallback = new MosCallback(context);
                JSONObject jsonObject = new JSONObject(result.getTransactionData());
                if(!jsonObject.getString("transType").equals("TRANSACTION_RECEIPT") && jsonObject.has("businessName"))
                moscCallback.printReceipt(context,getPrinterInstance(),jsonObject);
                else if(jsonObject.getString("transType").equals("TRANSACTION_RECEIPT") && jsonObject.has("businessName"))
                    moscCallback.printReceipt(result.getTransactionId());
                else
                    m.setData(result);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/

    }

   /* public PrinterInstance getPrinterInstance(){
        String devicesName = "Serial device";
        String devicesAddress = "/dev/ttyMT2";
        String com_baudrate = "115200";
        int baudrate = Integer.parseInt(com_baudrate);
        PrinterInstance myPrinter = PrinterInstance.getPrinterInstance(new File(devicesAddress), baudrate,0, mHandler);
        System.out.println("myPrinter.getCurrentStatus()-" + myPrinter.getCurrentStatus());
        boolean b = myPrinter.openConnection();
        System.out.println("-----------" + b);
        return myPrinter;
    }
*/
    private Handler mHandler = new Handler() {
        @SuppressLint("ShowToast")
        @Override
        public void handleMessage(Message msg) {

            System.out.println("@@@@@@@@@@@@" + msg.what);
            switch (msg.what) {
                case PrinterConstants.Connect.SUCCESS:
                    isConnected = true;
                    System.out.println("isConnected status:::;" + isConnected);
                    break;
                case PrinterConstants.Connect.FAILED:
                    isConnected = false;

                    break;
                case PrinterConstants.Connect.CLOSED:
                    isConnected = false;
                    Toast.makeText(context, "Connection closed", Toast.LENGTH_SHORT).show();
                    break;
                case PrinterConstants.Connect.NODEVICE:
                    isConnected = false;
                    Toast.makeText(context, "No connection", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    Toast.makeText(context, "0", Toast.LENGTH_SHORT).show();
                    break;
                case -1:
                    Toast.makeText(context, "-1", Toast.LENGTH_SHORT).show();
                    break;
                case -2:
                    Toast.makeText(context, "-2", Toast.LENGTH_SHORT).show();
                    break;
                case -3:
                    Toast.makeText(context, "-3", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
}
