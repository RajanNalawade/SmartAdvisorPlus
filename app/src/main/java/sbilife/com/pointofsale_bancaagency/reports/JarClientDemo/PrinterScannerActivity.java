package sbilife.com.pointofsale_bancaagency.reports.JarClientDemo;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mmsc.serial.SerialPortIOManage;
import com.mmsc.serial.SerialPortService;
import com.printer.sdk.Barcode;
import com.printer.sdk.CanvasPrint;
import com.printer.sdk.FontProperty;
import com.printer.sdk.PrinterConstants;
import com.printer.sdk.PrinterInstance;

import sbilife.com.pointofsale_bancaagency.R;


public class PrinterScannerActivity extends AppCompatActivity {

    private Button buttonPrinter, buttonScanner, buttonStopScan, buttonService, buttonServiceStop;
    public PrinterInstance myPrinter;
    private boolean isConnected;
    public static TextView tvScannarData;
    private String devicesName;
    private String devicesAddress;
    private int baudrate;
    private MyBroadcastReceiver myReceiver;
    private Intent intent;


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
                    Toast.makeText(getApplicationContext(), "Connection closed", Toast.LENGTH_SHORT).show();
                    break;
                case PrinterConstants.Connect.NODEVICE:
                    isConnected = false;
                    Toast.makeText(getApplicationContext(), "No connection", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    Toast.makeText(getApplicationContext(), "0", Toast.LENGTH_SHORT).show();
                    break;
                case -1:
                    Toast.makeText(getApplicationContext(), "-1", Toast.LENGTH_SHORT).show();
                    break;
                case -2:
                    Toast.makeText(getApplicationContext(), "-2", Toast.LENGTH_SHORT).show();
                    break;
                case -3:
                    Toast.makeText(getApplicationContext(), "-3", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_printer);
        buttonPrinter = (Button) findViewById(R.id.buttonPrinter);
        buttonScanner = (Button) findViewById(R.id.buttonScanner);
        buttonStopScan = (Button) findViewById(R.id.buttonStopScan);
        buttonService = (Button) findViewById(R.id.buttonService);
        buttonServiceStop = (Button) findViewById(R.id.buttonServiceStop);
        buttonPrinter.setOnClickListener(singleOnclick);
        buttonScanner.setOnClickListener(singleOnclick);
        buttonStopScan.setOnClickListener(singleOnclick);
        buttonService.setOnClickListener(singleOnclick);
        buttonServiceStop.setOnClickListener(singleOnclick);
        tvScannarData = (TextView) findViewById(R.id.tvScannerData);
        tvScannarData.setTag("scanning");
        myReceiver = new MyBroadcastReceiver();
        TRACE.i("===== onCreate");
        System.out.println("===== onCreate");


    }

    @Override
    public void onStart() {
        myReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("MY_ACTION");
        registerReceiver(myReceiver, intentFilter);
        TRACE.i("===== onStart");
        System.out.println("===== onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        TRACE.i("===== onResume");
        System.out.println("===== onResume ");
    }


    @Override
    public void onStop() {
        TRACE.i("===== onStop");
        System.out.println("===== onStop " );
        unregisterReceiver(myReceiver);
        super.onStop();
    }

    SingleOnClickListener singleOnclick = new SingleOnClickListener(1000) {
        @SuppressLint("NewApi")
        @Override
        public void onDebouncedClick(View view) {
            final PowerManager mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            switch (view.getId()) {
                case R.id.buttonService:
                    /*intent = new Intent();
                    intent.putExtra("openPort",true);
                    intent.setClassName("com.mosambee.printService", "com.mosambee.printService.PrinterService");
                    startService(intent);
                    buttonService.setEnabled(false);
                    buttonService.setBackgroundColor(getResources().getColor(R.color.disable));
                    buttonServiceStop.setEnabled(true);
                    buttonServiceStop.setBackgroundColor(getResources().getColor(R.color.enable));*/
                    break;
                case R.id.buttonServiceStop:

                    intent = new Intent();
                    intent.putExtra("openPort",false);
                    intent.putExtra("deviceType","Both");
                    intent.setClassName("com.mosambee.printService4G", "com.mosambee.printService.PrinterService");
                    startService(intent);
                    break;
                case R.id.buttonPrinter:
                    intent = new Intent();
                    intent.putExtra("openPort",true);
                    intent.putExtra("deviceType","Printer");
                    intent.setClassName("com.mosambee.printService4G", "com.mosambee.printService.PrinterService");
                    startService(intent);

                    break;

                case R.id.buttonScanner:
                    try {
                        System.out.println("XXXXXXXXXXXXX-----------in openTheSerialPort");

                        intent = new Intent();
                        intent.putExtra("openPort",true);
                        intent.putExtra("deviceType","Scanner");
                        intent.setClassName("com.mosambee.printService4G", "com.mosambee.printService.PrinterService");
                        startService(intent);

                    } catch (NoSuchMethodError | Exception er) {
                        Toast.makeText(getApplicationContext(), "Connection to scanner failed." + er.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    break;

                case R.id.buttonStopScan:
                    try {
                        intent = new Intent();
                        intent.putExtra("openPort",false);
                        intent.putExtra("deviceType","Scanner");
                        intent.setClassName("com.mosambee.printService4G", "com.mosambee.printService.PrinterService");
                        startService(intent);

                    } catch (NoSuchMethodError | Exception er) {
                        Toast.makeText(getApplicationContext(), "Connection to scanner failed." + er.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 888:
                    SerialPortIOManage.getInstance().sendDataToDevice("00");
                    sendEmptyMessageDelayed(1000, 20);
                    break;
                case 999:
                    SerialPortIOManage.getInstance().sendDataToDevice("00");
                    sendEmptyMessageDelayed(1001, 20);
                    break;
                case 1000:
                    SerialPortIOManage.getInstance().sendDataToDevice("04 E4 04 00 FF 14");
                    break;
                case 1001:
                    SerialPortIOManage.getInstance().sendDataToDevice("07 C6 04 00 FF 8A 08 FD 9E");
                    //SerialPortIOManage.getInstance().sendDataToDevice("07 C6 04 80 14 8A 01 FE 10");
                    break;
            }
        }

        ;
    };


    private void printData() {

        myPrinter.initPrinter();

        //HINDI FONT
        CanvasPrint cp=new CanvasPrint();
        cp.init(PrinterConstants.PrinterType.T9);
        FontProperty fp=new FontProperty();
        fp.setFont(true, false,false,false, 32, null);
        cp.setFontProperty(fp);

        cp.drawText(10,30,"मोसम्बी में आपका स्वागत है");

        fp.setFont(false, false,false,false, 22, null);
        cp.setFontProperty(fp);

        cp.drawText(10,110,"आधार राशि : 150 रुपये");
        cp.drawText(10,150,"टिप राशि : 0.0 रुपये");
        cp.drawLine(10, 180, 400, 180);
        cp.drawText(10,220,"कुल राशि : 150 रुपये");
        myPrinter.printImage(cp.getCanvasImage(), PrinterConstants.PAlign.START, 0, false);

        myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, 1);

        //English Font
        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 1);
        myPrinter.setFont(0, 0, 0, 0, 0);
        myPrinter.printText("Merchant Name" + "\nHDFC" + " BANK" + "\n");

        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 1);
        myPrinter.setFont(0, 0, 0, 1, 0);
        myPrinter.printText("Test transaction\n");

        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 1);
        myPrinter.setFont(0, 0, 0, 1, 0);
        myPrinter.printText("SALE \n");
        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 1);


        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 1);
        myPrinter.setFont(0, 0, 0, 1, 0);
        myPrinter.printText("----------------\n");

        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 0);
        myPrinter.setFont(0, 0, 0, 1, 0);
        myPrinter.printText("TOTAL AMOUNT   : Rs. 10.00");
        myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, 1);

        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 1);
        myPrinter.setFont(0, 0, 0, 1, 0);
        myPrinter.printText("V 1.0.0.0005");

        myPrinter.setFont(0, 0, 0, 0, 0);
        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 1);
        myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);

        //BARCODE PRINT
        Barcode barcode1 = new Barcode(PrinterConstants.BarcodeType.CODE128, 2, 150, 2, "mosambee.com");
        myPrinter.printBarCode(barcode1);


        //QR CODE PRINT
        Barcode barcode2 = new Barcode(PrinterConstants.BarcodeType.QRCODE, 2, 3, 6, "https://www.instagram.com/preetsagaphotography");
        myPrinter.printBarCode(barcode2);

        //PRINT IMAGE
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.mosambee_logo);
        myPrinter.printImage(icon, PrinterConstants.PAlign.NONE, 10, false);


        myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        public MyBroadcastReceiver() {
            super();
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            TRACE.i("===== onReceive");


            Bundle bundle = intent.getExtras();
            String deviceType = bundle.getString("deviceType");
            int deviceState = bundle.getInt("deviceState");
            int deviceOpen1 = bundle.getInt("deviceOpen1");
            int deviceOpen2 = bundle.getInt("deviceOpen2");
            TRACE.i("===== " + deviceType);
            //assert deviceType != null;
            assert deviceType != null;
            switch (deviceType) {
                case "Printer":
                    if (deviceState == 0 && deviceOpen1 == 0 && deviceOpen2 == 0) {
                        buttonPrinter.setEnabled(true);
                        buttonPrinter.setBackgroundColor(getResources().getColor(R.color.actionbar_black));
                        Toast.makeText(getApplicationContext(), "" + deviceType + "\ndeviceState: " + deviceState + "\ndeviceOpen1: " + deviceOpen1 + "\ndeviceOpen2: " + deviceOpen2, Toast.LENGTH_LONG).show();
                        goForPrint();
                    } else {
                        Toast.makeText(getApplicationContext(), "" + deviceType + "\n else", Toast.LENGTH_LONG).show();
                    }
                    break;
                case "Scanner":
                    TRACE.i("deviceState::" + deviceState + "deviceOpen1::" + deviceOpen1);
                    if (deviceState == 0 && deviceOpen1 == 0) {
                        buttonScanner.setEnabled(true);
                        buttonScanner.setBackgroundColor(getResources().getColor(R.color.actionbar_black));
                        buttonStopScan.setEnabled(true);
                        buttonStopScan.setBackgroundColor(getResources().getColor(R.color.actionbar_black));
                        goForScanner();
                        Toast.makeText(getApplicationContext(), "" + deviceType + "\ndeviceState: " + deviceState + "\ndeviceOpen1: " + deviceOpen1, Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "" + deviceType + "\n else", Toast.LENGTH_LONG).show();
                    }
                    break;
                case "Both":
                    if (deviceState == 0 && deviceOpen1 == 0 && deviceOpen2 == 0) {
                        buttonPrinter.setEnabled(true);
                        buttonPrinter.setBackgroundColor(getResources().getColor(R.color.actionbar_black));
                        buttonScanner.setEnabled(true);
                        buttonScanner.setBackgroundColor(getResources().getColor(R.color.actionbar_black));
                        Toast.makeText(getApplicationContext(), "" + deviceType + "\ndeviceState: " + deviceState + "\ndeviceOpen1: " + deviceOpen1 + "\ndeviceOpen2: " + deviceOpen2, Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "" + deviceType + "\n else", Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "" + deviceType, Toast.LENGTH_LONG).show();

                    break;

            }
        }

        //Monochromatic image pass to printImage()

        public void goForPrint() {
         /*   System.out.println("----------buttonPrint--------");
            devicesName = "Serial device";
            //devicesAddress = "/dev/ttyMT0";
            devicesAddress = "/dev/ttyMT2";
            String com_baudrate = "115200";
            baudrate = Integer.parseInt(com_baudrate);
//            myPrinter = PrinterInstance.getPrinterInstance(new File(devicesAddress), baudrate,0, 8, mHandler);
//            myPrinter = PrinterInstance.getPrinterInstance();
            myPrinter = PrinterInstance.getPrinterInstance(new File(devicesAddress), baudrate,0, mHandler);
            System.out.println("myPrinter.getCurrentStatus()-" + myPrinter.getCurrentStatus());
            boolean b = myPrinter.openConnection();
            System.out.println("-----------" + b);
            if (b && myPrinter != null *//*&& myPrinter.getCurrentStatus() == 0*//*) {
                printData();
            } else
                Toast.makeText(getApplicationContext(), "Connection to printer failed.", Toast.LENGTH_SHORT).show();
*/
        }

        public void goForScanner() {
            try {

                TRACE.i("-----------in openTheSerialPort");
                //FactorySerialPortIOManage.getInstance().Connect("dev/ttyMT1", 9600);
                Intent startIntent2 = new Intent(PrinterScannerActivity.this, SerialPortService.class);
                startIntent2.putExtra("serial", "dev/ttyMT1");
                startService(startIntent2);
                handler.removeMessages(1000);
                handler.removeMessages(1001);
                handler.removeMessages(999);
                handler.sendEmptyMessageDelayed(999, 10);

                TRACE.i("-----------in openTheScanHead");
//                SerialPortIOManage.getInstance().resetBuffer();
                handler.removeMessages(888);
                handler.removeMessages(999);
                handler.removeMessages(1000);
                handler.removeMessages(1001);
                handler.sendEmptyMessageDelayed(888, 1000);


            } catch (NoSuchMethodError | Exception er) {
                Toast.makeText(getApplicationContext(), "Connection to scanner failed." + er.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }



    }



}
