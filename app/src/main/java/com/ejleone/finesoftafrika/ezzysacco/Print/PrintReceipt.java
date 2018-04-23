package com.ejleone.finesoftafrika.ezzysacco.Print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.ejleone.finesoftafrika.ezzysacco.Database.Payment;
import com.imagpay.MessageHandler;
import com.imagpay.Settings;
import com.imagpay.mpos.MposHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Handler;

/**
 * Created by ejleone on 4/17/18.
 */

public class PrintReceipt {

    MposHandler handler;
    Settings settings;
    MessageHandler _mHandler;
    private boolean isPrint;
    private boolean isconnect;
    /*
    * @param Amount*/


    public PrintReceipt(Context context,String Amount, String uuname, String type, String customerno, String receiptno, String tupe)
    {
        handler = new MposHandler(context);
        handler.setParameters("/dev/ttyS2", 115200);
        settings = new Settings(handler);
        settings.mPosPowerOn();//


        try {
            if (!handler.isConnected()) {
                isconnect= handler.connect();
                Log.e("Connect Res:" , String.valueOf(isconnect));
            } else {
                handler.close();

                isconnect= handler.connect();
                Log.e("ReConnect Res:", String.valueOf(isconnect));
                // sendMessage("TLL has connected!");
            }
        } catch (Exception e) {
            Log.e("exception",e.getMessage());
        }

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        if(handler.isConnected()){
            printtestCustomer(Amount,uuname,type,customerno,receiptno,tupe,formattedDate);
        }
    }

    private void printtestCustomer(final String Amount, String uuname, final String type, final String customerno, final String receiptno, final String tupe , final String formattedDate) {
        new Thread(new Runnable() {

            public void run() {
                try{
                    isPrint=settings.mPosEnterPrint();
                    if (isPrint ){





                    settings.mPosPrintFontSwitch(Settings.MPOS_PRINT_FONT_NEW);
                    settings.mPosPrintAlign(Settings.MPOS_PRINT_ALIGN_CENTER);
                    settings.mPosPrintTextSize(Settings.MPOS_PRINT_TEXT_DOUBLE_HEIGHT);
                    settings.mPosPrnStr("Together As One Micro Investment Ltd");
                    settings.mPosPrnStr("Mfangano Trade center");
                    settings.mPosPrnStr("Mezzanine Flr M30");
                    settings.mPosPrnStr("Nairobi Kenya");
                    settings.mPosPrnStr("Tel:0719759652");
                    settings.mPosPrintLn();
                    settings.mPosPrintTextSize(Settings.MPOS_PRINT_TEXT_NORMAL);
                    settings.mPosPrintAlign(Settings.MPOS_PRINT_ALIGN_LEFT);
                    settings.mPosPrnStr("The Buisness Receipt stub   \nPlease properly keep");
                    settings.mPosPrnStr("--------------------------");

                    settings.mPosPrnStr("Customer No.: " + customerno);
                    settings.mPosPrnStr("Date/Time: " + formattedDate); //Todo:Replace with actual date
                    settings.mPosPrnStr("Receipt .No.: " + receiptno);
                    settings.mPosPrnStr("Amount: KES " + Amount);
                    settings.mPosPrnStr("type:  " + type );
                    settings.mPosPrnStr("Customer Signature:  "  );
                    settings.mPosPrintLn();
                    settings.mPosPrnStr("--------------------------");
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inPreferredConfig = Bitmap.Config.RGB_565;
                    opt.inPurgeable = true;

                    opt.inInputShareable = true;
                    settings.mPosPrnStr("\n\n");

                    //  handleros.sendEmptyMessage(dismissDailog);


                        Log.e("isprint","true");}else {Log.e("isprint","false");}
                    settings.mPosPrintFontSwitch(Settings.MPOS_PRINT_FONT_NEW);
                    settings.mPosPrintAlign(Settings.MPOS_PRINT_ALIGN_CENTER);
                    settings.mPosPrintTextSize(Settings.MPOS_PRINT_TEXT_DOUBLE_HEIGHT);
                    settings.mPosPrnStr("Together As One Micro Investment Ltd");
                    settings.mPosPrnStr("Mfangano Trade center");
                    settings.mPosPrnStr("Mezzanine Flr M30");
                    settings.mPosPrnStr("Nairobi Kenya");
                    settings.mPosPrnStr("Tel:0719759652");
                    settings.mPosPrintLn();
                    settings.mPosPrintTextSize(Settings.MPOS_PRINT_TEXT_NORMAL);
                    settings.mPosPrintAlign(Settings.MPOS_PRINT_ALIGN_LEFT);
                    settings.mPosPrnStr("The Customer Receipt stub   \nPlease properly keep");
                    settings.mPosPrnStr("--------------------------");

                    settings.mPosPrnStr("Customer No.: " + customerno);
                    settings.mPosPrnStr("Date/Time: " + formattedDate ); //Todo:Replace with actual date
                    settings.mPosPrnStr("Receipt .No.: " + receiptno);
                    settings.mPosPrnStr("Amount: KES " + Amount);
                    settings.mPosPrnStr("type:  " + type );
                    settings.mPosPrnStr("--------------------------");
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inPreferredConfig = Bitmap.Config.RGB_565;
                    opt.inPurgeable = true;
                    opt.inInputShareable = true;
                    settings.mPosPrnStr("\n\n");

                }catch(Exception e){
                    // handleros.sendEmptyMessage(dismissDailog);
                    Log.e("exception",e.getLocalizedMessage());
                }
            }
        }).start();
    }

   


}
