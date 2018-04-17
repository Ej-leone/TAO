package com.githinji.ej.ezzysacco11;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.githinji.ej.ezzysacco11.Database.DBhelper;
import com.githinji.ej.ezzysacco11.Database.Sessionmanager;
import com.githinji.ej.ezzysacco11.Database.userStatus;
import com.githinji.ej.ezzysacco11.Synchronise.CustomerSynchronise;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity
{
    private static final String SOAP_ACTION = "http://compwebsystems.com/LoginVerification";
    String URL ="http://192.168.0.2:800/Ezzyacc/EzzyAccess.asmx?op=LoginVerification";
    String NAMESPACE ="http://compwebsystems.com/";
    String METHOD_NAME="LoginVerification";





    String TAG ="";
    EditText _emailText , _passwordText;
    Button x;
    Boolean STATUS_CODE =Boolean.TRUE;
    DBhelper db;
    SQLiteDatabase mDb;
    // Session Manager Class
    //Sessionmanager session;


   //String auth;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        x = (Button)findViewById(R.id.btn_login);

        _emailText = (EditText)findViewById(R.id.input_email);
        _passwordText = (EditText)findViewById(R.id.input_password);

        db= new DBhelper(Login.this);

        // Session class instance
        //session = new Sessionmanager(getApplicationContext());



        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         *
         * */

        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                email = _emailText.getText().toString();
                password = _passwordText.getText().toString();

                if (Netavail())
                {
                    if(db.ifuseravailable()==false) {
                        login();
                    }
                    else{
                        //String saveduserlogvalues=db.getuserlogvalues();
                        //session.createLoginSession(saveduserlogvalues, "moses");
                        Intent fr = new Intent(Login.this, MainActivity.class);
                        startActivity(fr);
                    }



                }
                else
                {

                    //HashMap<String,String> us = session.getUserDetails();


                    if(db.ifuseravailable()==false)
                    {
                        Toast.makeText(Login.this, "Login at your Base station. Your session has not been started", Toast.LENGTH_SHORT).show();
                    }else
                    {

                            //String saveduserlogvalues=db.getuserlogvalues();
                            //session.createLoginSession(saveduserlogvalues, "moses");//get saved username and password to post to session
                            Intent fr = new Intent(Login.this, MainActivity.class);
                            startActivity(fr);

                    }
                }

                // Intent m = new Intent(Login.this,MainActivity.class);

                //startActivity(m);

            }
        });


    }

    String email , password;

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        //x.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(Login.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();



        // TODO: Implement your own authentication logic here.






        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        // Login(email, password);

                        new myAsyncTask().execute();

                        //onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 1000);
    }


    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {

        //Toast.makeText(getBaseContext(),"Loggin in ....", Toast.LENGTH_LONG).show()
        Intent de = new Intent(Login.this,CustomerSynchronise.class);
        startActivity(de);
       // x.setEnabled(true);
        finish();
    }



    public void onLoginFailed()
    {
       // Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        // x.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() ) {
            _emailText.setError("Username is empty");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty()) {
            _passwordText.setError("password is empty");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

     public boolean Netavail()
     {
         ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

         NetworkInfo inf = cm.getActiveNetworkInfo();
         return inf != null &&inf.isConnected();
     }

    private class myAsyncTask extends AsyncTask<String, Void,String>    {


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

           // Toast.makeText(getBaseContext(),result,Toast.LENGTH_LONG).show();

           if (result.contains("True"))
            {
                onLoginSuccess();
                   /*Set the infromation to the Shared preferences*/
                if(db.ifuseravailable()==false)
                {
                    final userStatus insertulog = new userStatus();
                    db.createuserlog("true",email,password);
                }
                //session.createLoginSession(email, password);
            }
            else if (result.contains("False"))
            {
                // onLoginFailed();

                Toast.makeText(getApplication(),"Either Username is incorrect or user day has not been Started", Toast.LENGTH_SHORT).show();
            }

            else
           {
               Toast.makeText(getApplicationContext(), "Did not connect to the login Service", Toast.LENGTH_SHORT).show();
           }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params)
        {

            String auth = doLogin(email,password);


            Log.d("AAAAuthenitication",email);

            Log.d("AAAAAAAAAAAAAAA",password);
            return auth;
           // return null;
        }
    }




    private String doLogin(String user_id, String password)
    {

        SoapObject resultstring ;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("username", user_id);
        request.addProperty("password", password);
        request.addProperty("accesskey","AXZPS143769DMNCWEBINTERGSERVICES");

        SoapSerializationEnvelope soapenvelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        soapenvelope.dotNet = true;
        soapenvelope.setOutputSoapObject(request);

        HttpTransportSE httptransport = new HttpTransportSE(URL);
        httptransport.debug = true;

        try
        {
            httptransport.call(SOAP_ACTION, soapenvelope);

             resultstring = (SoapObject) soapenvelope.getResponse();
            Log.d("Authenticaion", resultstring + "");

            //Toast.makeText(getBaseContext(),resultstrin.toString(),Toast.LENGTH_LONG).show();
            Log.i("myApp", resultstring.toString());
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
        }
      // return  resultstring.toString();
        return  "Failed";


    }
}





/*   private  void Login(final String uSermail , final String pAssword) {
      StringRequest req = new StringRequest(Request.Method.POST,URL_LOGIN, new Response.Listener<String>()

        {
            @Override
            public void onResponse(String response)
            {

                if(response.trim().equals("Success"))
                {

                    onLoginSuccess();
                }
                else {
                    Toast.makeText(getBaseContext(), "Login Failed ...try again later", Toast.LENGTH_LONG).show();
                    onLoginFailed();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                Toast.makeText(getBaseContext(),error.toString(), Toast.LENGTH_LONG).show();
                x.setEnabled(true);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Post params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("accesskey","AXZPS143769DMNCWEBINTERGSERVICES");
                params.put("username", uSermail);
                params.put("password", pAssword);

                return params;
            }
        };

        // Appcontroller.getInstance().addToRequestQueue(req);
        RequestQueue reqq = Volley.newRequestQueue(this);
        reqq.add(req);


        //Using Ksoap to login to C# services

    }*/


