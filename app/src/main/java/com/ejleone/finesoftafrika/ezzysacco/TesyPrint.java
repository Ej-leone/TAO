package com.ejleone.finesoftafrika.ezzysacco;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.imagpay.MessageHandler;
import com.imagpay.Settings;
import com.imagpay.mpos.MposHandler;

public class TesyPrint extends AppCompatActivity {


    MposHandler handler;
    TextView dess;
    Settings settings;
    MessageHandler _mHandler;
    private boolean isPrint;
    private boolean isconnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tesy_print);

        dess = findViewById(R.id.rvvd);
        handler = new MposHandler(getApplicationContext());
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

        dess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isconnect) {
                    printtest();
                }
            }
        });
    }

    private void printtest() {
        new Thread(new Runnable() {

            public void run() {
                try{
                    isPrint=settings.mPosEnterPrint();
                    if (isPrint ){Log.e("isprint","true");}else {Log.e("isprint","false");}
                    // handleros.sendEmptyMessage("showPrintDailog");
//				settings.mPosPrintAlign(Settings.MPOS_PRINT_ALIGN_CENTER);
//				settings.mPosPrintTextSize(Settings.MPOS_PRINT_TEXT_DOUBLE_HEIGHT);
//				settings.mPosPrnStr("POS Signed Order");
//				settings.mPosPrintLn();
//				settings.mPosPrintTextSize(Settings.MPOS_PRINT_TEXT_NORMAL);
//				settings.mPosPrintAlign(Settings.MPOS_PRINT_ALIGN_LEFT);
//				settings.mPosPrnStr("Merchant stub");
//				settings.mPosPrnStr("----------------------------------------------");
//				settings.mPosPrnStr("Merchant Name:HCCTG");
//				settings.mPosPrnStr("Merchant No.:846584000103052");
//				settings.mPosPrnStr("Terminal No.:12345678");
//				settings.mPosPrnStr("categories: visa card");
//				settings.mPosPrnStr("Period of Validity:2016/07");
//				settings.mPosPrnStr("Batch no.:000101");
//				settings.mPosPrnStr("Card Number:");
//				settings.mPosPrnStr("622202400******0269");
//				settings.mPosPrnStr("Trade Type:consumption");
//				settings.mPosPrnStr("Serial No.:000024 \nAuthenticode:096706");
//				settings.mPosPrnStr("Date/Time:2016/09/01 11:27:12");
//				settings.mPosPrnStr("Ref.No.:123456789012345");
//				settings.mPosPrnStr("Amount:$ 100.00");
//				settings.mPosPrnStr("-----------------------------------------------");
//				settings.mPosPrnStr("Cardholder's signature:\n\n");
//				settings.mPosPrnStr("-----------------------------------------------");
//				settings.mPosPrnStr("Please confirm the above transaction,\nagree to be included in the card account\n\n\n\n\n");
                    settings.mPosPrintFontSwitch(Settings.MPOS_PRINT_FONT_NEW);
                    settings.mPosPrintAlign(Settings.MPOS_PRINT_ALIGN_CENTER);
                    settings.mPosPrintTextSize(Settings.MPOS_PRINT_TEXT_DOUBLE_HEIGHT);
                    settings.mPosPrnStr("POS Signed Order");
                    settings.mPosPrintLn();
                    settings.mPosPrintTextSize(Settings.MPOS_PRINT_TEXT_NORMAL);
                    settings.mPosPrintAlign(Settings.MPOS_PRINT_ALIGN_LEFT);
                    settings.mPosPrnStr("The cardholder stub   \nPlease properly keep");
                    settings.mPosPrnStr("--------------------------");
                    settings.mPosPrnStr("Merchant Name:HCCTG");
                    settings.mPosPrnStr("Merchant No.:846584000103052");
                    settings.mPosPrnStr("Terminal No.:12345678");
                    settings.mPosPrnStr("categories: visa card");
                    settings.mPosPrnStr("Period of Validity:2016/07");
                    settings.mPosPrnStr("Batch no.:000101");
                    settings.mPosPrnStr("Card Number:");
                    settings.mPosPrnStr("622202400******0269");
                    settings.mPosPrnStr("Trade Type:consumption");
                    settings.mPosPrnStr("Serial No.:000024  \nAuthenticode:096706");
                    settings.mPosPrnStr("Date/Time:2016/09/01 11:27:12");
                    settings.mPosPrnStr("Ref.No.:123456789012345");
                    settings.mPosPrnStr("Amount:$ 100.00");
                    settings.mPosPrnStr("--------------------------");
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inPreferredConfig = Bitmap.Config.RGB_565;
                    opt.inPurgeable = true;
                    opt.inInputShareable = true;
                    // @SuppressLint("ResourceType") InputStream is = getResources().openRawResource(R.drawable.hcctgn);
                    // Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
                    // settings.mPosPrintAlign(Settings.MPOS_PRINT_ALIGN_CENTER);
                    //settings.mPosPrnImg(bitmap);
                    settings.mPosPrnStr("\n\n");
                    //if (!bitmap.isRecycled()) {
                    //  bitmap.recycle();
                    // }
                    // bitmap = null;
                    //  handleros.sendEmptyMessage(dismissDailog);
                }catch(Exception e){
                    // handleros.sendEmptyMessage(dismissDailog);
                    Log.e("exception",e.getLocalizedMessage());
                }
            }
        }).start();
    }
}
