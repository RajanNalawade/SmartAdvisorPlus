package sbilife.com.pointofsale_bancaagency.reports.JarClientDemo;

import android.content.Context;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.mosambee.lib.Currency;
import com.mosambee.lib.MosCallback;
import com.mosambee.lib.ResultData;
import com.mosambee.lib.TransactionResult;

import sbilife.com.pointofsale_bancaagency.reports.BancaReportsRenewalActivity;

public class JARClass implements TransactionResult {

    private Context context;
    MosCallback moscCallback;
    private AppCompatActivity _activity;
    BancaReportsRenewalActivity m;

    void startProcess(String user, String pswd, String transType, FrameLayout container, String orderId, String amount) {
        moscCallback = new MosCallback(context);
        //  moscCallback.setActivityForJUNO(_activity);
        moscCallback.initialise(user, pswd, this);
        // initializeSignatureView(FrameLayout, primaryColor, secondaryColor);
        moscCallback.initializeSignatureView(container, "#55004A", "#750F5A");
        moscCallback.initialiseFields(transType, "", "cGjhE$@fdhj4675riesae", false, "faiz.saifi@mosambee.in", "merchantRef1", "serial", "09082013101105", orderId);
//        moscCallback.processTransaction("125457893", Double.parseDouble(amount), Double.parseDouble("675466"), "879209");
        moscCallback.processTransaction("125457893", Double.parseDouble(amount), Double.parseDouble("675466"), "879209", Currency.INR);
        //moscCallback.processTransaction("125457893", Double.parseDouble(amount), Double.parseDouble("675466"), "879209","0356");
//        moscCallback.processTransaction("125457893", Double.parseDouble(amount), Double.parseDouble("675466"), "879209", Currency.valueOf("0356"));
//        moscCallback.getLocation();
    }

    public void getReceipt(String invoiceNo) {
        moscCallback = new MosCallback(context);
        moscCallback = new MosCallback(context);
        moscCallback.initialise("", "1234", this);
        moscCallback.getMetaData(invoiceNo);
    }

    public void setContext(Context context) {
        this.context = context;
        m = new BancaReportsRenewalActivity();
    }

    @Override
    public void onCommand(String command) {
        //System.out.println("command is--------------------" + command);
        m = new BancaReportsRenewalActivity();
        //m.setCommand(command);
    }

    @Override
    public void onResult(ResultData result) {
        System.out.println();
        System.out.println("\n-----Result: "+result.getResult()+ "\n-----Reason: " + result.getReason()+"\n-----Data: "+ result.getTransactionData() );
        result.getResult();
       // m.setData(result);
    }

    public void stopProcess() {
        moscCallback = new MosCallback(context);
        moscCallback.posReset();
    }

    void setActivity(AppCompatActivity act) {
        _activity = act;
    }
}
