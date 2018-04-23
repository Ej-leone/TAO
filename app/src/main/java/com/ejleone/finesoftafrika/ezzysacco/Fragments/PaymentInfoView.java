package com.ejleone.finesoftafrika.ezzysacco.Fragments;

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

import com.ejleone.finesoftafrika.ezzysacco.Database.DBhelper;
import com.ejleone.finesoftafrika.ezzysacco.R;


/**
 * Created by EJ on 5/31/2016.
 */
public class PaymentInfoView extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static PaymentInfoView newInstance(int sectionNumber) {
        PaymentInfoView fragme = new PaymentInfoView();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);

        return fragme;
    }

    ListView mPay;
    DBhelper mHelper;
    SQLiteDatabase mDb;
    Cursor mcurtor;
    SimpleCursorAdapter mAdapterr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View pela = inflater.inflate(R.layout.payinfo, container, false);

        mPay = (ListView) pela.findViewById(R.id.inlvp);

        mPay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        mHelper = new DBhelper(getActivity());


        return pela;
    }


    @Override
    public void onResume() {
        super.onResume();


        //Open connections to the database
        mDb = mHelper.getWritableDatabase();

        String[] columnss = new String[] {"_id", DBhelper.COL_1AMNT, DBhelper.COL_1TYPE};

        mcurtor = mDb.query(DBhelper.TABLE_NAME1, columnss, null, null, null, null,null);

        String[] header = new String[] { DBhelper.COL_1AMNT, DBhelper.COL_1RNO};

        mAdapterr = new SimpleCursorAdapter(getActivity(), android.R.layout.two_line_list_item,mcurtor, header, new int[]{android.R.id.text1, android.R.id.text2});

        mPay.setAdapter(mAdapterr);

    }

    @Override
    public void onPause() {
        super.onPause();

        mDb.close();
        mcurtor.close();
    }
}
