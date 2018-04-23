package com.ejleone.finesoftafrika.ezzysacco.Database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by EJ on 5/14/2016.
 *
 * help manage memory leaks and maintain clear ata information
 * Currently not implemented
 *
 */
public class DatabaseManager {
    private Integer mOpencounter = 0;


    private static DatabaseManager instance;
    private static SQLiteOpenHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;


    public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
        if (instance == null) {
            instance = new DatabaseManager();
            mDatabaseHelper = helper;
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }

        return instance;
    }

    public synchronized SQLiteDatabase openDatabase()
    {
        mOpencounter+=1;
        if(mOpencounter == 1) {
            // Opening new database
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        mOpencounter-=1;
        if(mOpencounter == 0)
        {
            // Closing database
            mDatabase.close();

        }
    }




}
