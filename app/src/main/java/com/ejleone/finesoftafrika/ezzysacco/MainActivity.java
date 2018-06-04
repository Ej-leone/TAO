package com.ejleone.finesoftafrika.ezzysacco;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;


import com.ejleone.finesoftafrika.ezzysacco.Database.DBhelper;
import com.ejleone.finesoftafrika.ezzysacco.Fragments.Customer_Reg;
import com.ejleone.finesoftafrika.ezzysacco.Fragments.Information;
import com.ejleone.finesoftafrika.ezzysacco.Fragments.Payment_Rwg;
import com.ejleone.finesoftafrika.ezzysacco.Synchronise.CustomerSynchronise;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
{


    //Sessionmanager ses ;


    private static final String SOAP_ACTION = "http://ezzybooks.co.ke/TransactionSyncronization";

    private static final String SOAP_ACTIONC = "http://ezzybooks.co.ke/Updatecorecustomerregister";

    String URL =Constants.URl+ "/Ezzyacc/EzzyAccess.asmx?op=TransactionSyncronization";

    String URL1 =Constants.URl+ "/Ezzyacc/EzzyAccess.asmx?op=Updatecorecustomerregister";

    String NAMESPACE ="http://ezzybooks.co.ke/";

    String METHOD_NAME="TransactionSyncronization";

    String METHOD_NAME1="Updatecorecustomerregister";

    DBhelper dbt;
    SQLiteDatabase mDb;


    String pret;



    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sessionmanager
        //ses = new Sessionmanager(getBaseContext());

        dbt= new DBhelper(MainActivity.this);

        //set the backup file
        File extStorage = Environment.getExternalStorageDirectory();
        File directory = new File(extStorage.getAbsolutePath() + "/EzzySacco");
        directory.mkdirs();

        SimpleDateFormat rt = new SimpleDateFormat("yyyy-MM-dd");
        File file = new File(directory, rt.format(new Date())+".txt");




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Call the administration", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        /* TOdo  call function to wipe  the Shared Preference and wipe the data  from the Database */
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            if(Netavail()) {
                new MypaySync().execute();
                SQLiteDatabase db = dbt.getWritableDatabase();
                String delqry = "DELETE  FROM "+ dbt.TABLE_NAME3 ;
                db.execSQL(delqry);
                Intent logme = new Intent(MainActivity.this,Login.class);
                startActivity(logme);
            }
            else
            {

                Toast.makeText(MainActivity.this, "You are not connected to the network", Toast.LENGTH_SHORT).show();

            }
            //return true;
        }
        if(id == R.id.action_syncc)
        {

            if(Netavail())
            {
                new  Mycustom().execute();


            }
            else
            {

                Toast.makeText(MainActivity.this, "You are not connected to the network", Toast.LENGTH_SHORT).show();

            }
            //Log.d("Syyyync",result);
        }
        if (id == R.id.action_clearcust)
        {
            if(Netavail()) {
                new  Mycustom().execute();
                SQLiteDatabase db = dbt.getWritableDatabase();
                String deletequery = "DELETE  FROM "+ dbt.TABLE_NAME ;
                db.execSQL(deletequery);
                Intent de = new Intent(MainActivity.this,CustomerSynchronise.class);
                startActivity(de);
                // x.setEnabled(true);
                finish();
            }
            else
            {

                Toast.makeText(MainActivity.this, "You are not connected to the network", Toast.LENGTH_SHORT).show();

            }
            //return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

               switch (position)
               {
                   case 0:
                       return Payment_Rwg.newInstance(position);


                   // break;
                   case 1:

                       return Customer_Reg.newInstance(position);
                   // break;
                  case 2:


                      return Information.newInstance(position);
                   // break;

                   //case 3:
                       //return PaymentInfoView.newInstance(position);
                   default:
                       return null;
               }


           // return PlaceholderFragment.newInstance(position + 1);
        }




        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return   "Payment";
                case 1:
                    return "Customer ";
              case 2:
                   return "Information";
               // case 3:
                  //  return "Payment View";
            }
            return null;
        }
    }


    private  String Dosync()
    {

        //HashMap<String,String> fr = ses.getUserDetails();



        Object resultstring ;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        /*Get all payments from  the db */

        String selectQuery = "SELECT  * FROM "+dbt.TABLE_NAME1 ;
         mDb = dbt.getWritableDatabase();

        Cursor cursor = mDb.rawQuery(selectQuery,null);
        int hmany =cursor.getCount();

        Log.d("items:", String.valueOf(hmany));
        if (hmany != 0)
        {

            while (cursor.moveToFirst())
                    {
                        String amnt= "0";
                        String idnoo = "x";
                        String trantt = "x";
                        String trantd ="";
                        String custm = "c00056";
                        String usert ="sup";
                Log.d("items:", "Kwanza");
                do

                    {



                /*Todo : fix the receipt and the customer number during fine tuning*/

                        amnt = amnt +","+cursor.getString(cursor.getColumnIndex(dbt.COL_1AMNT));
                         //amnt = amnt +","+"200";

                        idnoo = idnoo +","+ cursor.getString(cursor.getColumnIndex(dbt.COL_1CNo));
                        //idnoo = idnoo +","+"12345";

                        trantt = trantt+","+cursor.getString(cursor.getColumnIndex(dbt.COL_1TYPE));
                       //trantt = trantt+","+"egsure";

                        trantd = cursor.getString(cursor.getColumnIndex(dbt.COL_1DATEE));

                        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        usert = usert + ","+ cursor.getString(cursor.getColumnIndex(dbt.COL_1UNAME));

                       // pret  = pret + ","+ "sup";

                      //  trantd = dateFormat.format(new Date());

                        custm = custm +","+cursor.getString(cursor.getColumnIndex(dbt.COL_1RNO));

                       // custm = custm + ","+ "ccccno34";

                        //Log.d("usernames:", pret);
                        Log.d("usernames:", usert);
                    } while (cursor.moveToNext());

                        request.addProperty("accesskey","AXZPS143769DMNCWEBINTERGSERVICES");
                        request.addProperty("source", "MobileApp");
                        request.addProperty("custno",  custm);
                        request.addProperty("idno", idnoo );
                        request.addProperty("trantype", trantt);
                        request.addProperty("trandate", trantd);
                        request.addProperty("amount", amnt);
                        request.addProperty("username", usert);

                        SoapSerializationEnvelope soapenvelope = new SoapSerializationEnvelope(
                                SoapEnvelope.VER11);
                        soapenvelope.dotNet = true;
                        soapenvelope.setOutputSoapObject(request);

                        HttpTransportSE httptransport = new HttpTransportSE(URL);
                        httptransport.debug = true;

                        try
                        {
                            Log.d("ID", idnoo);
                            Log.d("TIDamnt", amnt);
                            Log.d("TIDtt", trantt);
                            Log.d("TIDcn", custm);
                            httptransport.call(SOAP_ACTION, soapenvelope);

                            resultstring = soapenvelope.getResponse();
                            Log.d("Syncing ", resultstring + "");

                            //Toast.makeText(getBaseContext(),resultstrin.toString(),Toast.LENGTH_LONG).show();
                            Log.d("myApp", resultstring.toString());
                            System.out.println("response" + resultstring);
                            String returnedresp= resultstring.toString().trim();

                            if (returnedresp.equals("true")) {
                                Log.d("responseyasync", returnedresp);
                                SQLiteDatabase db = dbt.getWritableDatabase();
                                String delquery = "DELETE FROM "+ dbt.TABLE_NAME1;
                                Log.d("the query",delquery);
                                db.execSQL(delquery);

                            }

                            return  resultstring.toString();


                        } catch (SocketException ex)
                        {
                            Log.e("Error : ", "Error on soapPrimitiveData() " + ex.getMessage());
                            ex.printStackTrace();
                        } catch (Exception e)
                        {
                            Log.e("Error : ", "Error on soapPrimitiveData() " + e.getMessage());
                            e.printStackTrace();
                        }
                        //retur

                //mDb.close();
                //cursor.close();
            }
        }
        else if(hmany == 0)
        {

           Log.d("SyncPay","null numbers in database");
        }
        else
        {
            Log.d("SyncPay","Unexpected error");
        }


        /* Call function to read one by one then delete them one by one
        request.addProperty("accesskey","AXZPS143769DMNCWEBINTERGSERVICES");
        request.addProperty("source", "MobileApp");
        request.addProperty("custno",  "0000000");
        request.addProperty("idno",  "1234567");
        request.addProperty("trantype", "Edsures");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        request.addProperty("trandate", dateFormat.format(new Date()));
        request.addProperty("amount", "6675657");
        request.addProperty("username", "Sup");

        SoapSerializationEnvelope soapenvelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        soapenvelope.dotNet = true;
        soapenvelope.setOutputSoapObject(request);

        HttpTransportSE httptransport = new HttpTransportSE(URL);
        httptransport.debug = true;

        try
        {
            httptransport.call(SOAP_ACTION, soapenvelope);

            resultstring = soapenvelope.getResponse();
            Log.d("Syncing", resultstring + "");

            //Toast.makeText(getBaseContext(),resultstrin.toString(),Toast.LENGTH_LONG).show();
            Log.d("myApp", resultstring.toString());
            System.out.println("response" +resultstring);

            return  resultstring.toString();


        } catch (SocketException ex)
        {
            Log.e("Error : ", "Error on soapPrimitiveData() " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception e)
        {
            Log.e("Error : ", "Error on soapPrimitiveData() " + e.getMessage());
            e.printStackTrace();
        } */
        //return  resultstring.toString();
        return  "Failed";

    }


        private String Synccustomer()
        {


            Object resultstring = null ;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);


            /*Todo : Dont forget to close the db and the cursor after syncing is done*/
        /*Get all payments from  the db */


            String selectQuery = "SELECT  * FROM "+dbt.TABLE_NAME +" WHERE " +dbt.COL_STAT  +" = " +"1";
            mDb = dbt.getWritableDatabase();

            Cursor cursor = mDb.rawQuery(selectQuery,null);
            int hmany =cursor.getCount();

            Log.d("ngapi",String.valueOf(hmany));

            if (hmany != 0)
            {
                    if (cursor.moveToFirst())
                    {
                        String id="";
                        String custno="";
                        String custp="";
                        String name = "";
                        String uname= "";
                        String datee ="";

                        do
                        {
                            Log.d("turn", cursor.getString(cursor.getColumnIndex(dbt.COL_NAME)));

                            name=name+","+cursor.getString(cursor.getColumnIndex(dbt.COL_NAME));
                             id=id+","+cursor.getString(cursor.getColumnIndex(dbt.COL_ID));
                            custno=custno+","+cursor.getString(cursor.getColumnIndex(dbt.COL_NO));
                            custp=custp+","+cursor.getString(cursor.getColumnIndex(dbt.COL_PNUM));
                            uname=cursor.getString(cursor.getColumnIndex(dbt.COL_UNAME));
                            datee=cursor.getString(cursor.getColumnIndex(dbt.COL_DATE));




                        }
                        while (cursor.moveToNext());

                        Log.d("nm",name);
                        request.addProperty("username", uname);
                        request.addProperty("idno",id);
                        request.addProperty("custno",custno );
                        request.addProperty("customerphone",custp );
                        request.addProperty("customername", name);
                        request.addProperty("regdate",datee);
                        request.addProperty("accesskey", "AXZPS143769DMNCWEBINTERGSERVICES");
                        request.addProperty("source", "MobileApp");

                        SoapSerializationEnvelope soapenvelope = new SoapSerializationEnvelope(
                                SoapEnvelope.VER11);
                        soapenvelope.dotNet = true;
                        soapenvelope.setOutputSoapObject(request);

                        HttpTransportSE httptransport = new HttpTransportSE(URL1,3000);
                        httptransport.debug = true;


                        try
                        {
                            httptransport.call(SOAP_ACTIONC, soapenvelope);

                            resultstring = soapenvelope.getResponse();
                            Log.d("Syncing", resultstring + "");

                            //Toast.makeText(getBaseContext(),resultstrin.toString(),Toast.LENGTH_LONG).show();
                            Log.d("myApp", resultstring.toString());
                            System.out.println("response" +resultstring);

                            return  resultstring.toString();


                        } catch (SocketException ex)
                        {
                            Log.e("Error : ", "Error on soapPrimitiveData() " + ex.getMessage());
                            ex.printStackTrace();
                        }
                        catch (Exception e)
                        {
                            Log.e("Error : ", "Error on soapPrimitiveData() " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    mDb.close();
                    cursor.close();

            }
            else if(hmany == 0)
            {

                Log.d("SyncPay","null numbers in database");
                String result = "You have no new numbers  to Sync";
                return result;


            }
            else
            {
                Log.d("SyncPay","Unexpected error");
            }


        /* Call function to read one by one then delete them one by one
            request.addProperty("username", "Sup");
            request.addProperty("idno", "02334554");
            request.addProperty("source", "MobileApp");
            request.addProperty("customerphone","0712376589");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            request.addProperty("regdate", dateFormat.format(new Date()));
            request.addProperty("customername", "Tony Montana");
            request.addProperty("accesskey","AXZPS143769DMNCWEBINTERGSERVICES");*/

            //return  resultstring.toS


            return  resultstring.toString();
        }

    public boolean Netavail()
    {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo inf = cm.getActiveNetworkInfo();
        return inf != null &&inf.isConnected();
    }

    private  class MypaySync extends AsyncTask<String, Void,String>
    {
        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            Toast.makeText(getBaseContext(),s, Toast.LENGTH_SHORT).show();
            //ses.logoutUser();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params){
            String pauth = Dosync();
            Log.d ("Syncpay",pauth);

        return pauth;
        }
    }

    private class Mycustom extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute()
        {

            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

            Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();


            if (s.equals("true"))
            {

                SQLiteDatabase db = dbt.getWritableDatabase();
                String deletequery = "DELETE  FROM "+ dbt.TABLE_NAME + " WHERE " + dbt.COL_STAT +" = " +"1";
                db.execSQL(deletequery);

            }
        }

        @Override
        protected String doInBackground(String... params) {
            String custs = Synccustomer();

            return custs;
        }
    }




/*


*/

}
