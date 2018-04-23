package com.ejleone.finesoftafrika.ezzysacco.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.ejleone.finesoftafrika.ezzysacco.Database.DBhelper;
import com.ejleone.finesoftafrika.ezzysacco.Database.Payment;
import com.ejleone.finesoftafrika.ezzysacco.Print.PrintReceipt;
import com.ejleone.finesoftafrika.ezzysacco.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by EJ on 5/7/2016.
 */
public class Payment_Rwg extends Fragment
{
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static Payment_Rwg newInstance(int sectionNumber) {
        Payment_Rwg fragment = new Payment_Rwg();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;

    }

    SQLiteDatabase mDb;
    Cursor mCursor;


    int ST_RC= 1;
    TextView gh;
    Button sd,ser;
    EditText am ,se ,retnu ;
    TextView um;
    RadioGroup fr;
    Spinner ty;
    String [] typeee ={"registration","Saving ","Loan Repayment"};
    String [] ammt  = { "Normal Saving","Edsure Saving" , "Group Saving"};
    //sionmanager ses;

    String type,tupe;


    DBhelper db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final View payee = inflater.inflate(R.layout.payment,container,false);

        //add action bar items
        //setHasOptionsMenu(true);



        //initalise values
        final Payment pesa = new Payment();
        db = new DBhelper(getActivity());
        //ses = new Sessionmanager(getActivity());

        um=(TextView)payee.findViewById(R.id.Textname);
        retnu =(EditText)payee.findViewById(R.id.inputrcptno);
        ser=(Button)payee.findViewById(R.id.btnserch);
        sd=(Button)payee.findViewById(R.id.btn_paymentreg);
        ty= (Spinner)payee.findViewById(R.id.pay_spinner);
        se = (EditText)payee.findViewById(R.id.searched);
        gh =(TextView)payee.findViewById(R.id.Textname);
        am = (EditText)payee.findViewById(R.id.inputamnt);


        //adapter for the spinner
        ArrayAdapter<String> jk = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,typeee);
        ty.setAdapter(jk);


        ty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index = ty.getSelectedItemPosition();

                if (index == 1)
                {

                    AlertDialog.Builder bg = new AlertDialog.Builder(getActivity());
                    bg.setTitle("Choose a case");
                    bg.setSingleChoiceItems(ammt, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Toast.makeText(getActivity(), "You chose " + ammt[i], Toast.LENGTH_LONG).show();

                            type = ammt[i];
                            Log.d("Set",type);

                        }
                    });
                    bg.setPositiveButton("set", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog ad = bg.create();
                    ad.show();
                }
                else
                {
                    type = ty.getSelectedItem().toString();
                    Log.d("Set",type);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*Disable the Amount* and Save Button */

        sd.setEnabled(false);


       // am.setEnabled(false);
        //search items through the database
        /*sync button  used to give a list of the available numbers in the databse*/




        ser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                final String searchkey = se.getText().toString();

                //openconnectins to db

                if (validate())
                {
                mDb=db.getWritableDatabase();

                String Seaechquery = "SELECT "+db.COL_NAME+ " FROM "+ db.TABLE_NAME +" WHERE "+db.COL_ID +" = "+searchkey;

                String result [] ={};
                mCursor =  mDb.rawQuery(Seaechquery,null);




                if (mCursor != null)
                {

                    if (mCursor.moveToFirst()) {


                        final String ret = mCursor.getString(mCursor.getColumnIndex(db.COL_NAME));

                        Toast.makeText(getActivity(), ret, Toast.LENGTH_LONG).show();

                        gh.setText(ret);
                        sd.setEnabled(true);

                        Log.d("database retreival", ret);



                        /*Get the data*/
                        /*Amount,id,receipt number,type,uname
                        *  Username gotten from shared preference of the current logged in user
                         * Date Sets Automatic ..
                         * TODo make sure a date verification is added on the fragment to verify the actual date from the net incase user date is incoreect
                          * */



                        sd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (validatepayitems())
                                {

                                    // get user data from session

                                    //HashMap<String, String> user = ses.getUserDetails();
                                    final String saveduserlogvalues=db.getuserlogvalues();

                                    //final String pref = user.get(Sessionmanager.KEY_NAME);
                                    final String pref = saveduserlogvalues;
                                    //Log.d("savetranuname", saveduserlogvalues);
                                    final String amunt = am.getText().toString();
                                    final String receipt = retnu.getText().toString();


                                    pesa.setReceiptno(receipt);
                                    pesa.setAmount(amunt);
                                    pesa.setCustomerno(searchkey);
                                    pesa.setType(type);
                                    pesa.setTupe(tupe);
                                    pesa.setUuname(pref);
                                    Log.d("trantype", type);
                                    mDb = db.getWritableDatabase();

                                    AlertDialog.Builder cf = new AlertDialog.Builder(getActivity());
                                    cf.setTitle("Confirm Payment");
                                    cf.setMessage("Amount          : " + amunt
                                                    + "\n" + "Receipt number : " + receipt
                                                    + "\n" + "Type             : " + type
                                                    + "\n" + "Agent           : " + pref
                                                    + "\n" + "Customer        : " + ret

                                    );
                                    cf.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {


                                            final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                                                    R.style.AppTheme_Dark_Dialog);
                                            progressDialog.setIndeterminate(true);
                                            progressDialog.setMessage(amunt + " Saving..");
                                            progressDialog.show();


                                            //backup info in a text file
                                            //set the backup file

                                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                                                try {
                                                    if (IsExternalStorageAvailableAndWriteable())

                                                    {
                                                        File extStorage = Environment.getExternalStorageDirectory();
                                                        File directory = new File(extStorage.getAbsolutePath() + "/EzzySacco");
                                                        directory.mkdirs();

                                                        SimpleDateFormat rt = new SimpleDateFormat("yyyy-MM-dd");
                                                        File file = new File(directory, rt.format(new Date()) + "_payment" + ".txt");

                                                        FileOutputStream fOut = new FileOutputStream(file, true);
                                                        OutputStreamWriter osw = new OutputStreamWriter(fOut);

                                                        //---write the string to the file---
                                                        osw.write("\n\nCustomer: " + ret
                                                                        + "\nReceiptno:  " + receipt
                                                                        + "\nAgent:  " + pref
                                                                        + "\nAmount:" + amunt
                                                                        + "\nType:  " + type
                                                        );
                                                        osw.flush();
                                                        osw.close();
                                                    } else {
                                                        AlertDialog.Builder fr = new AlertDialog.Builder(getActivity());
                                                        fr.setMessage("Local Backup");
                                                        fr.setMessage("Cannot write to local Back up external Storage not Available");

                                                        AlertDialog dr = fr.create();
                                                        dr.show();
                                                    }
                                                } catch (IOException ioe) {
                                                    ioe.printStackTrace();
                                                    Toast.makeText(getActivity(), ioe.toString(), Toast.LENGTH_LONG).show();
                                                    Log.d("Backup:", ioe.toString()+"  Has not written  to backup");
                                                }
                                            } else {
                                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ST_RC);
                                                return;
                                            }


                                            new android.os.Handler().postDelayed
                                                    (
                                                            new Runnable() {
                                                                public void run() {
                                                                    // On complete call clear all fields

                                                                    se.setText("");
                                                                    am.setText("");
                                                                    sd.setEnabled(false);
                                                                    retnu.setText("");
                                                                    um.setText("Customer Name");
                                                                    ty.setSelection(0);


                                                                  Long longrt = db.createTag(pesa);

                                                                  if(longrt >= 0){
                                                                      new PrintReceipt(getActivity(),amunt,pref,type,ret,receipt,type);
                                                                  }
                                                                    // onLoginFailed();
                                                                    progressDialog.dismiss();
                                                                }
                                                            }, 500);


                                        }
                                    });
                                    cf.setNegativeButton("no", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                                    AlertDialog fr = cf.create();
                                    fr.show();

                                }
                            }
                        });

                    }

                }

                else
                {
                    Toast.makeText(getActivity(),"error:id number not found",Toast.LENGTH_SHORT).show();
                }


                mCursor.close();


            }
            }


        });

        //get the key Search item
        //String searchkey = se.getText().toString();

        /*Search through the list of all the Id numbers gotten */

        /*If found  use  the id number to retrieve the name and set the name to the  */



        //ge
        return payee;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode ==ST_RC)
        {

        }
    }

    public boolean IsExternalStorageAvailableAndWriteable()
    {        boolean externalStorageAvailable = false;
        boolean externalStorageWriteable = false;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state))
        {
          //---you can read and write the media--
          externalStorageAvailable = externalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
        {
                    //---you can only read the media--
                    externalStorageAvailable = true;
            externalStorageWriteable = false;
        } else
        {            //---you cannot read nor write the media--
         externalStorageAvailable = externalStorageWriteable = false;
        }        return externalStorageAvailable && externalStorageWriteable;
    }



    public boolean validate() {
        boolean valid = true;

        String email = se.getText().toString();


        if (email.isEmpty() ) {
            se.setError("Username is empty");
            valid = false;
        } else {
            se.setError(null);
        }

        if (email.contains("+") ||email.contains("-")||email.contains("*")||email.contains("/")||email.contains("#")||email.contains("."))
        {
            se.setError("Incorrect id format");
            valid = false;
        } else {
            se.setError(null);
        }

        return valid;
    }

    public boolean validatepayitems(){
        boolean validp = true;



        if(am.getText().toString().trim().length() < 1)
        {
            Toast.makeText(getContext(),"Amount is required before you proceed",Toast.LENGTH_LONG).show();
            return false;

        }
        if(retnu.getText().toString().trim().length() < 1)
        {
            Toast.makeText(getContext(),"Receipt number is required before you proceed",Toast.LENGTH_LONG).show();
            return false;

        }


        return validp;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_ty,menu);
        MenuItem it = menu.findItem(R.id.searchtbb34);
        SearchView sv = (SearchView) MenuItemCompat.getActionView(it);



        super.onCreateOptionsMenu(menu, inflater);
    }
}
