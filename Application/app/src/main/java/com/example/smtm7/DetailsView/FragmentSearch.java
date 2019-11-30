package com.example.smtm7.DetailsView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.smtm7.Connection.ApiService;
import com.example.smtm7.Connection.NetworkHelper;
import com.example.smtm7.Connection.ResponseTransaction;
import com.example.smtm7.DataBase.DBTransactionAdapter;
import com.example.smtm7.DataBase.SharedPreferenceBase;
import com.example.smtm7.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            String date = cursor.getString(2).substring(0,4)+"-"+cursor.getString(2).substring(4,6)+"-"+cursor.getString(2).substring(6,8);

            TransactionItem item = new TransactionItem(cursor.getString(1),date,office,cursor.getString(4),price);
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
        //거래내역 데이터 받아오기
        NetworkHelper networkHelper = new NetworkHelper();
        ApiService apiService = networkHelper.getApiService();

        final SharedPreferenceBase sharedPreferenceBase = new SharedPreferenceBase(getContext());

        apiService.getTransaction(sharedPreferenceBase.getString("access"), sharedPreferenceBase.getString("id"), Integer.parseInt(sharedPreferenceBase.getString("transactionIndex"))).enqueue(new Callback<List<ResponseTransaction>>() {
            @Override
            public void onResponse(Call<List<ResponseTransaction>> call, Response<List<ResponseTransaction>> response) {
                if(response.isSuccessful()){
                    List<ResponseTransaction> resource = response.body();

                    int index = Integer.parseInt(sharedPreferenceBase.getString("transactionIndex"));
                    transactionAdapter.open();

                    Log.d("transactionIndex", "first index: "+index);
                    //내부 DB에 저장 (Transaction)
                    for(ResponseTransaction re : resource){
                        index++;
                        Log.d("TAG", re.getDate());
                        int date = Integer.parseInt(re.getDate().replace("-",""));
                        transactionAdapter.insertTransaction(re.getPGname(), date, re.getPurchasing_office(), re.getPurchasing_item(), re.getPrice());
                        Log.d("TAG", re.getPGname()+" "+date+" "+re.getPurchasing_office()+" "+re.getPurchasing_item()+" "+re.getPrice());
                    }

                    Log.d("transactionIndex", "update index: "+index);

                    transactionAdapter.close();
                    sharedPreferenceBase.setString("transactionIndex", Integer.toString(index));
                    updateList();
                }
            }

            @Override
            public void onFailure(Call<List<ResponseTransaction>> call, Throwable t) {

            }
        });
        //새로고침 아이콘 삭제
        swipeRefreshLayout.setRefreshing(false);
    }

    public void updateList(){
        transactionAdapter.open();
        itemList.clear();

        Cursor cursor = transactionAdapter.searchAllTransaction();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String office = "";
            if(!cursor.getString(3).equals("none")){
                office = cursor.getString(3);
            }

            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            String price = decimalFormat.format(Double.parseDouble(cursor.getString(5)))+"원";

            String date = cursor.getString(2).substring(0,4)+"-"+cursor.getString(2).substring(4,6)+"-"+cursor.getString(2).substring(6,8);

            TransactionItem item = new TransactionItem(cursor.getString(1),date,office,cursor.getString(4),price);
            Log.d("TAG", cursor.getString(1)+date+office+cursor.getString(4)+price);
            itemList.add(item);
            cursor.moveToNext();
        }
        transactionAdapter.close();

        listAdapter = new ListAdapter(itemList);
        listView.setAdapter(listAdapter);
    }
}
