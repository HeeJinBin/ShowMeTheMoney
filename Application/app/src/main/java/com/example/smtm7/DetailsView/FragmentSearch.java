package com.example.smtm7.DetailsView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.smtm7.DataBase.DBTransactionAdapter;
import com.example.smtm7.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FragmentSearch extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ListView listView = null;
    private ArrayList<TransactionItem> itemList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListAdapter listAdapter;
    private DBTransactionAdapter transactionAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        itemList = new ArrayList<>();

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_red_light
        );

        transactionAdapter = new DBTransactionAdapter(getContext());
        transactionAdapter.open();

        Cursor cursor = transactionAdapter.searchAllTransaction();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String office = "";
            if(!cursor.getString(3).equals("none")){
                office = cursor.getString(3);
            }

            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            String price = decimalFormat.format(Double.parseDouble(cursor.getString(5)))+"원";

            TransactionItem item = new TransactionItem(cursor.getString(1),cursor.getString(2),office,cursor.getString(4),price);
            itemList.add(item);
            cursor.moveToNext();
        }

        transactionAdapter.close();

        listView = view.findViewById(R.id.listView);
        listAdapter = new ListAdapter(itemList);
        listView.setAdapter(listAdapter);

        return view;
    }

    //ListView 새로고침
    @Override
    public void onRefresh() {

        //새로고침 아이콘 삭제
        swipeRefreshLayout.setRefreshing(false);
    }
}
