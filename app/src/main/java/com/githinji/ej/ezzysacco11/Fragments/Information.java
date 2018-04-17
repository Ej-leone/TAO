package com.githinji.ej.ezzysacco11.Fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.githinji.ej.ezzysacco11.Database.DBhelper;
import com.githinji.ej.ezzysacco11.R;

/**
 * Created by EJ on 5/8/2016.
 */
public class Information extends Fragment
{
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static Information newInstance(int sectionNumber)
    {
        Information fragment = new Information();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);

        return fragment;
    }

    ListView mList, mPay;
    DBhelper mHelper;
    SQLiteDatabase mDb;
    Cursor mCursor,mcurtor;
    SimpleCursorAdapter mAdapter,mAdapterr;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View nm = inflater.inflate(R.layout.information,container,false);

        mList = (ListView)nm.findViewById(R.id.inlv);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        mHelper = new DBhelper(getActivity());
        
        return nm;
    }

    @Override
    public void onResume() {
        super.onResume();

        //Open connections to the database
        mDb = mHelper.getWritableDatabase();
        String[] columns = new String[] {"_id", DBhelper.COL_NAME, DBhelper.COL_ID};

        mCursor = mDb.query(DBhelper.TABLE_NAME, columns, null, null, null, null,null);



        // Refresh the list
       String[] headers = new String[] {DBhelper.COL_NAME, DBhelper.COL_ID};


       mAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.two_line_list_item,mCursor, headers, new int[]{android.R.id.text1, android.R.id.text2});

        mList.setAdapter(mAdapter);




    }

    @Override
    public void onPause() {
        super.onPause();

        //Close all connections
        mDb.close();
        mCursor.close();


    }
}
