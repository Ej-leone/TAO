package com.ejleone.finesoftafrika.ezzysacco.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by EJ on 5/7/2016.
 */
public class DBhelper extends SQLiteOpenHelper
{
        //db name & version
    private static final String DB_NAME = "Ezzydets";
    private static final int DB_VERSION = 14;

        //Db tables
    public static final String TABLE_NAME ="User";
    public static final String TABLE_NAME1 ="Payment";
    public static final String TABLE_NAME2 = "Product";
    public static final String TABLE_NAME3 = "userStatus";

        //customer columns
    public static final String COL_UNAME ="username";
    public static final String COL_NO ="customerno";
    public static final String COL_NAME = "Name";
    public static final String COL_PNUM = "Pnum";
    public static final String COL_ID ="Pid";
    public static final String COL_DATE ="regdate";
    public static final String COL_STAT ="status";
   // public static final String COL_DATE = "pDate";

        //payment columns
    public static final String COL_1UNAME= "username";
    public static final String COL_1AMNT = "Amount";
    public static final String COL_1DATEE ="Pdate";
    public static final String COL_1TYPE ="type";
    public static final String COL_1RNO="receiptno";
    public static final String COL_1CNo ="customerno";

        //savings columns
    public static final String COL_2PRODCOE ="productcode";
    public static final String COL_2NAME="prouctname";


    //Status COLUMNS
    public static  final String COL_3STAT= "ustatus";
    public static  final String COL_USERNM ="username";
    public static  final String COL_PASS ="password";



    private static final String STRING_CREATE = "CREATE TABLE "+TABLE_NAME +"( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                                                               +COL_UNAME +" VARCHAR,"
                                                               +COL_NO +" INTEGER,"
                                                               +COL_NAME + " TEXT,"
                                                               +COL_DATE +" DATE,"
                                                               +COL_STAT + " VARCHAR, "
                                                               +COL_PNUM  + " VARCHAR,"
                                                               +COL_ID +"  VARCHAR);";


    private static final String STRING_CREATE1 = "CREATE TABLE "+TABLE_NAME1+" (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                                +COL_1UNAME + " TEXT, "
                                                                +COL_1TYPE  + " TEXT, "
                                                                +COL_1AMNT + " VARCHAR, "
                                                                +COL_1CNo+" VARCHAR, "
                                                                +COL_1RNO +" VARCHAR, "
                                                                +COL_1DATEE +" DATE);";
    private static final String STRING_CREATE3 = "CREATE TABLE "+TABLE_NAME3+" (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                                +COL_3STAT + " VARCHAR, "
                                                                +COL_USERNM  + " TEXT, "
                                                                +COL_PASS +" DATE);";




    private static final String STRING_CREATE2 = "CREATE TABLE "+TABLE_NAME2+" (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_2NAME   + " TEXT, " +COL_2PRODCOE  + " INTEGER );";




    public  DBhelper(Context context)

    {
        super(context, DB_NAME, null, DB_VERSION);
    }





    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //Create the database table
        db.execSQL(STRING_CREATE);
        db.execSQL(STRING_CREATE1);
       db.execSQL(STRING_CREATE3);

        /*You may also load initial dummy values into the database here
        ContentValues cv = new ContentValues(2);
        cv.put(COL_NAME, "John Doe");
        cv.put(COL_PNUM,0722245345);
        cv.put(COL_ID,3024678);*/

        //Create a formatter for SQL date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //cv.put(COL_DATE, dateFormat.format(new Date()));

         //Insert 'now' as the date
       // db.insert(TABLE_NAME, null, cv);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //Todo:alter saving the data
        //Upgrade from v1. Adding phone number
      /*  if(oldVersion <= 1)
        {
            db.execSQL("ALTER TABLE "+TABLE_NAME+" ADD COLUMN phone_number INTEGER;");
        }

        //Upgrade from v2. Add entry date
        if(oldVersion <= 2)
        {
            db.execSQL("ALTER TABLE "+TABLE_NAME+" ADD COLUMN entry_date DATE;");
        }*/

        Log.w("TAG", "Upgrading database from version " + oldVersion + " to "+ newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME3);
        onCreate(db);
    }

  //Customer Table

    public void createToDo(User todo)
    {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_UNAME, todo.getUuname());
        values.put(COL_PNUM, todo.getPnum());
        values.put(COL_NAME, todo.getUname());
        values.put(COL_NO, todo.getCustomerno());
        values.put(COL_ID, todo.getPid());
        values.put(COL_STAT,todo.getStatus());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        values.put(COL_DATE,dateFormat.format(new Date()));

        // insert row
       db.insert(TABLE_NAME, null, values);



        Log.d("Inserted", "successfully");
        db.close();

            /*insert tag_ids
        for (long tag_id : tag_ids) {
            createTodoTag(todo_id, tag_id);
        }

        return todo_id;*/

    }


    //Payment Table
    public void createuserlog(String statuscode,String usercode, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_3STAT, statuscode);
        values.put(COL_USERNM, usercode);
        values.put(COL_PASS, password);
        db.insert(TABLE_NAME3, null, values);

        Log.d("user inserted", statuscode + " -" + usercode + "-" + password + "");

        /*insert row
        long tag_id = db.insert(TABLE_TAG, null, values);

        return tag_id; */
    }

    public Boolean ifuseravailable()
    {
        boolean useravail=false;
        String selectQuery = "SELECT  * FROM "+TABLE_NAME3;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                useravail=true;
            } while (cursor.moveToNext());
        }
        database.close();
        return useravail;
    }
    public String  getuserlogvalues()
    {
        String usercodenpass="";
        String selectQuery = "SELECT "+ COL_USERNM +" FROM "+TABLE_NAME3;
        SQLiteDatabase dbase = this.getWritableDatabase();
        Cursor cursor = dbase.rawQuery(selectQuery, null);
        int niwangapi =cursor.getCount();

        Log.d("niwangapi",String.valueOf(niwangapi));
        if (cursor.moveToFirst()) {
            do {

                usercodenpass=cursor.getString(cursor.getColumnIndex(COL_USERNM));
            } while (cursor.moveToNext());
        }
        //Log.d("getuserlogvalues:",usercodenpass);
        dbase.close();
        return  usercodenpass;
    }
    public long createTag(Payment tag) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_1AMNT, tag.getAmount());
        values.put(COL_1CNo, tag.getCustomerno());
        values.put(COL_1UNAME,tag.getUuname());
        values.put(COL_1RNO,tag.getReceiptno());
        values.put(COL_1TYPE,tag.getType());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        values.put(COL_1DATEE,dateFormat.format(new Date()));

        long success =db.insert(TABLE_NAME1, null, values);

        Log.d("Data inserted", tag.getAmount() + " " + tag.getReceiptno() + " " + tag.getCustomerno()+""+tag.getType()+"");

        /*insert row
        long tag_id = db.insert(TABLE_TAG, null, values);

        return tag_id; */
        return  success;
    }


    /**
     * Get list of Users from SQLite DB as Array List
     * @return
     */
    public ArrayList<HashMap<String, String>> getAllPay()
    {
        ArrayList<HashMap<String, String>> wordList;

        wordList = new ArrayList<HashMap<String, String>>();

        String selectQuery = "SELECT  * FROM "+TABLE_NAME1;

        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
               // map.put("userid", cursor.getString(0));
                map.put("username",cursor.getString(cursor.getColumnIndex(COL_1UNAME)));
                map.put("customerno",cursor.getString(cursor.getColumnIndex(COL_1CNo)));
                map.put("date",cursor.getString(cursor.getColumnIndex(COL_1DATEE)));
                map.put("type",cursor.getString(cursor.getColumnIndex(COL_1TYPE)));
                map.put("Amount", cursor.getString(cursor.getColumnIndex(COL_1AMNT)));
                map.put("receiptno",cursor.getString(cursor.getColumnIndex(COL_1RNO)));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return wordList;
    }






    public List<String> getAllIds()
    {
        List<String> Ids = new ArrayList<>();

        String selectidqueries = "SELECT* "+ COL_ID +" FROM "+ TABLE_NAME;

        Log.e("LOG", selectidqueries);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectidqueries,null);

        //All and adding to list

        if(c.moveToFirst())
        {
            do {

                String Ti = c.getString(c.getColumnIndex(COL_ID));

            }while (c.moveToNext());
        }

        c.close();
        return Ids;
    }



    /**
     * Get Sync status of SQLite
     * @return
     */
    public String getSyncStatus(){
        String msg = null;
        if(this.dbSyncCount() == 0){
            msg = "SQLite and Remote MySQL DBs are in Sync!";
        }else{
            msg = "DB Sync neededn";
        }
        return msg;
    }

    /**
     * Get SQLite records that are yet to be Synced
     * @return
     */
    public int dbSyncCount(){
        int count = 0;
        String selectQuery = "SELECT  * FROM users where udpateStatus = '"+"no"+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();

        cursor.close();
        database.close();
        return count;
    }



    /**

     * Compose JSON out of SQLite records

     * @return

     */

    public String composeJSONfromSQLite(){

        ArrayList<HashMap<String, String>> wordList;

        wordList = new ArrayList<HashMap<String, String>>();

        String selectQuery = "SELECT  * FROM users where udpateStatus = '"+"no"+"'";

        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {

                HashMap<String, String> map = new HashMap<String, String>();

                map.put("userId", cursor.getString(0));

                map.put("userName", cursor.getString(1));

                wordList.add(map);

            } while (cursor.moveToNext());

        }

        database.close();

        Gson gson = new GsonBuilder().create();

        //Use GSON to serialize Array List to JSON

        return gson.toJson(wordList);

    }




    /**

     * Update Sync status against each User ID

     * @param id

     * @param status

     */

    public void updateSyncStatus(String id, String status){

        SQLiteDatabase database = this.getWritableDatabase();

        String updateQuery = "Update users set udpateStatus = '"+ status +"' where userId="+"'"+ id +"'";

        Log.d("query",updateQuery);

        database.execSQL(updateQuery);

        database.close();

    }






}
