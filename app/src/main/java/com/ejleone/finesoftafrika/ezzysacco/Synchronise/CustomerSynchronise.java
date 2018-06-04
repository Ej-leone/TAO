package com.ejleone.finesoftafrika.ezzysacco.Synchronise;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.ejleone.finesoftafrika.ezzysacco.Constants;
import com.ejleone.finesoftafrika.ezzysacco.Database.DBhelper;
import com.ejleone.finesoftafrika.ezzysacco.Database.User;
import com.ejleone.finesoftafrika.ezzysacco.MainActivity;
import com.ejleone.finesoftafrika.ezzysacco.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class CustomerSynchronise extends AppCompatActivity
{

    private static final String SOAP_ACTION = "http://ezzybooks.co.ke/UpdateMobilecustomerregister";
    private static final String URL = Constants.URl+ "/Ezzyacc/EzzyAccess.asmx?op=UpdateMobilecustomerregister";
    private static final String NAMESPACE ="http://ezzybooks.co.ke/";
    private static final String METHOD_NAME="UpdateMobilecustomerregister";


    DBhelper db;

    SQLiteDatabase mDb;
    Cursor mCursor;

    Button sd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_synchronise);



        db = new DBhelper(getBaseContext());
        //creating a customer
       /* final ProgressDialog progressDialog = new ProgressDialog(CustomerSynchronise.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating Customer...");*/

        new  CustomerSync().execute();
        // sd= (Button)findViewById(R.id.buttonty);
       /* sd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent kj = new Intent(CustomerSynchronise.this, MainActivity.class);
                startActivity(kj);

            }
        });*/
        /* AlertDialog.Builder fr = new AlertDialog.Builder(getBaseContext());
            fr.setTitle("Syncronisation Complete");
            fr.setMessage("Update complete");
            fr.setPositiveButton("Begin Day", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent kj = new Intent(CustomerSynchronise.this, MainActivity.class);
                    startActivity(kj);
                }
            });
        AlertDialog tg = fr.create();
        tg.show();*/
    }


    private class CustomerSync extends AsyncTask
    {

       ProgressDialog  rty = new ProgressDialog(CustomerSynchronise.this);

        @Override
        protected void onPostExecute(Object o)
        {
            super.onPostExecute(o);
            if(rty.isShowing())
            {
                rty.dismiss();
            }
            Toast.makeText(getBaseContext(),"Customers updated,",Toast.LENGTH_LONG).show();
            Intent fg = new Intent(CustomerSynchronise.this,MainActivity.class);
            startActivity(fg);



        }

        @Override
        protected void onPreExecute()
        {
            rty.setMessage("Updating Customer List");
            rty.show();



            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params)
        {
            try {
               soapre(METHOD_NAME,SOAP_ACTION,NAMESPACE,URL);
            }
            catch (XmlPullParserException | IOException re)
            {
                re.printStackTrace();

            }

            return null;
        }
    }


    public Integer soapre(String methodname,String soapaction,String namespace,String url) throws IOException,XmlPullParserException
    {
        SoapObject request = new SoapObject(namespace,methodname);


        mDb=db.getWritableDatabase();
        String selectQuery = "SELECT *"+" FROM " + db.TABLE_NAME +" ORDER BY "+db.COL_NO;
        Cursor cursor = mDb.rawQuery(selectQuery, null);
        String   Lcno = "CCIN000000";

        if(cursor.getCount() > 0)
        {
            cursor.moveToLast();

            Lcno = cursor.getString(cursor.getColumnIndex(db.COL_NO));

    }




        //set up request
        request.addProperty("accesskey", "AXZPS143769DMNCWEBINTERGSERVICES");
        request.addProperty("lastcustomerno", Lcno);

        //testing number
        //request.addProperty("UpdateCustomer","All");

        SoapSerializationEnvelope env = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        env.dotNet = true;
        //prepare request
        env.setOutputSoapObject(request);

        HttpTransportSE htr = new HttpTransportSE(url);
        htr.debug = true;
        htr.call(soapaction, env);

        //result
        SoapObject result =(SoapObject) env.bodyIn;
         System.out.println(result.toString());

       // SoapObject te = (SoapObject)result.getProperty("UpdateMobilecustomerregisterResult");




        if ((result.toString()).contains("{")) {

            SoapObject rep = (SoapObject) env.bodyIn;
            try {
                JSONArray jr = new JSONArray(rep.getPropertyAsString(0));
                for (int i = 0; i < jr.length(); i++) {
                    JSONObject jb = (JSONObject) jr.get(i);


                    String  cno =jb.getString("customerno");
                    String name = jb.getString("fname");
                    String id = jb.getString("nationaid");
                    String pno = jb.getString("telephone1");
                    String status =  "0";



                    Log.d("Syncing: ", name + " " + cno + " " + id + " " + pno + " " + status);



                    User olmteja = new User();
                    olmteja.setUuname("sync");
                    olmteja.setUname(name);
                    olmteja.setPid(id);
                    olmteja.setCustomerno(cno);
                    olmteja.setPnum(pno);
                    olmteja.setStatus(status);

                    db.createToDo(olmteja);

                    mDb.close();

                }
            } catch (JSONException rt) {
                rt.printStackTrace();
            }



            //
            // mteja.setUname(name);
            //mteja.setUuname("Sup");
            //// mteja.setCustomerno(customerno);
            //mteja.setPnum(Pnumber);
            //  mteja.setPid(pid);

            /**/
            //  db.createToDo(mteja);
            //  Log.d("Updating", "works");


        }
         return 3;
    }


    public String LameParser(String input)
    {
        String scust=input.substring(input.indexOf("customerno:")+6, input.indexOf(";", input.indexOf("customerno:")));
        String sid=input.substring(input.indexOf("nationaid:") + 6, input.indexOf(";", input.indexOf("nationaid:")));
        String sname=input.substring(input.indexOf("fname:")+6, input.indexOf(";", input.indexOf("fname:")));
        String sCountry=input.substring(input.indexOf("telephone1:")+9, input.indexOf(";", input.indexOf("telephone1")));

        return  scust;
    }


}
