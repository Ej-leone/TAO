package com.githinji.ej.ezzysacco11.Fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.githinji.ej.ezzysacco11.Database.DBhelper;
import com.githinji.ej.ezzysacco11.Database.Sessionmanager;
import com.githinji.ej.ezzysacco11.Database.User;
import com.githinji.ej.ezzysacco11.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by EJ on 5/7/2016.
 */
public class Customer_Reg extends Fragment
{

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static Customer_Reg newInstance(int sectionNumber) {
        Customer_Reg fragment = new Customer_Reg();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    //Db
    DBhelper  db;

    SQLiteDatabase mDb;
    Cursor mCursor;

    //Sessionmanager ses;


    //ui
    EditText nm,pid,pnum, uuet;
    Button sv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View cust = inflater.inflate(R.layout.customer,container,false);

        //ses = new Sessionmanager(getActivity());
        sv =(Button)cust.findViewById(R.id.btn_customereg);
        nm=(EditText)cust.findViewById(R.id.toinput_opportunityname);
      //  uuet=(EditText)cust.findViewById(R.id.inputususername);
        pid=(EditText)cust.findViewById(R.id.inputpid);
        pnum=(EditText)cust.findViewById(R.id.inputpnum);

        db = new DBhelper(getActivity());

        //creating a customer

        final User mteja = new User();



        sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




            if(Validateinfo() == true) {


                String searchkey = pid.getText().toString();



                //openconnectins to db

                mDb=db.getWritableDatabase();

                String Seaechquery = "SELECT "+db.COL_NAME+ " FROM "+ db.TABLE_NAME +" WHERE "+db.COL_ID +" = "+searchkey;

                String result [] ={};
                mCursor =  mDb.rawQuery(Seaechquery,null);


                if (mCursor.getCount() <= 0)
                {

                    //HashMap<String, String> user = ses.getUserDetails();
                    //final String saveduserlogvalues=db.getuserlogvalues();
                    final String pref = db.getuserlogvalues();

                    mteja.setUname(nm.getText().toString());
                    mteja.setUuname(pref);
                    mteja.setStatus("1");
                    //mteja.setCustomerno(Integer.parseInt(pid.getText().toString()));
                    mteja.setPnum(pnum.getText().toString());
                    mteja.setPid(pid.getText().toString());




                    final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Saving..");
                    progressDialog.show();


                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    // On complete call either onLoginSuccess or onLoginFailed
                                    // Login(email, password);
                                    db.createToDo(mteja);

                                    /*Write to backup file*/

                                    //set the backup file
                                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                                    {
                                        try {
                                            if (IsExternalStorageAvailableAndWriteable()) {
                                                File extStorage = Environment.getExternalStorageDirectory();
                                                File directory = new File(extStorage.getAbsolutePath() + "/EzzySacco");
                                                directory.mkdirs();

                                                SimpleDateFormat rt = new SimpleDateFormat("yyyy-MM-dd");
                                                File file = new File(directory, rt.format(new Date()) + "_Customer" + ".txt");

                                                FileOutputStream fOut = new FileOutputStream(file, true);
                                                OutputStreamWriter osw = new OutputStreamWriter(fOut);

                                                //---write the string to the file---
                                                osw.write("\n\nUsername: " + nm.getText().toString()
                                                                + "\nPhone number:  " + pnum.getText().toString()
                                                                + "\nID:  " + pid.getText().toString()
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

                                        } catch (IOException ioe)
                                        {

                                            ioe.printStackTrace();

                                            Toast.makeText(getActivity(), ioe.toString(), Toast.LENGTH_LONG).show();
                                            Log.d("Backup:", "  Has not written  to backup");

                                        }

                                    }
                                    else
                                    {

                                    }



                                        Toast.makeText(getActivity(), nm.getText().toString() + " Saved Succesfully", Toast.LENGTH_LONG).show();
                                        // onLoginFailed();
                                        progressDialog.dismiss();

                                        nm.setText("");
                                        pnum.setText("");
                                        pid.setText("");
                                    }
                                }

                                ,500);


                            }

                    else

                {

                   // mCursor.moveToFirst();
                    AlertDialog.Builder bd = new AlertDialog.Builder(getActivity());
                    bd.setTitle("Id number Present in Database");
                    bd.setMessage("Could be you have entered the wrong id number .If not Person is Present in the Database.Contact your Administrator for more ");
                    bd.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });

                    AlertDialog alt = bd.create();
                    alt.show();


                }
            }
            }
        });


        return cust;
    }

    void hideKeyBoard(){

        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getActivity().getCurrentFocus();
        if(view != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    boolean Validateinfo()
    {
        if(isEditTextBlank(nm)){    nm.setError("Field is empty"); return  false;}
//        if(isEditTextBlank(uuet)){uuet.setError("Field is empty"); return false;}
        if(isEditTextBlank(pid)){pid.setError("Field is empty"); return  false;}
        if(isEditTextBlank(pnum)){pnum.setError("Field is empty"); return  false;}

        //validate phone number
        if(pnum.getText().toString().trim().length() != 12)
        {
            Toast.makeText(getContext(),"Use 254 7XX XXX XXX phone number format",Toast.LENGTH_LONG).show();
            return false;

        }

        //verify Id
        if(pid.getText().toString().trim().length()  <= 3)
        {
            Toast.makeText(getContext(), "Write an appropriate id number", Toast.LENGTH_LONG).show();
            return  false;
        }

        return  true;

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


    public boolean isEditTextBlank(EditText et){
        return et.getText().toString().trim().length() == 0;
    }




}
