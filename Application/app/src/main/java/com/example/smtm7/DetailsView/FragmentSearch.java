package com.example.smtm7.DetailsView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.smtm7.Connection.ApiService;
import com.example.smtm7.Connection.NetworkHelper;
import com.example.smtm7.Connection.ResponseCheck;
import com.example.smtm7.Connection.ResponseTransaction;
import com.example.smtm7.DataBase.DBTransactionAdapter;
import com.example.smtm7.DataBase.SharedPreferenceBase;
import com.example.smtm7.InitialSetting.EmailDialog;
import com.example.smtm7.InitialSetting.EmailInterlock;
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                DetailsActivity.screenCheck = false;
                TransactionDeleteDialog dialog = new TransactionDeleteDialog(getContext());
                dialog.callFunction();
                dialog.setDialogResult(new TransactionDeleteDialog.OnMyDialogResult() {
                    @Override
                    public void finish(boolean result) {
                        if(result){ // 거래내역 삭제
                            transactionAdapter.open();
                            int date = Integer.parseInt(itemList.get(position).getDate().replace("-",""));
                            int price = Integer.parseInt(itemList.get(position).getPrice().replace(",","").replace("원",""));
                            transactionAdapter.deleteTransaction(itemList.get(position).getPg(), date, itemList.get(position).getItem(), price);
                            transactionAdapter.close();

                            updateList();
                            Toast.makeText(getContext(), "거래내역이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return view;
    }

    //ListView 새로고침
    @Override
    public void onRefresh() {
        //거래내역 데이터 받아오기
        NetworkHelper networkHelper = new NetworkHelper();
        final ApiService apiService = networkHelper.getApiService();

        final SharedPreferenceBase sharedPreferenceBase = new SharedPreferenceBase(getContext());

        apiService.checkTransaction(sharedPreferenceBase.getString("access"), sharedPreferenceBase.getString("id"), Integer.parseInt(sharedPreferenceBase.getString("transactionIndex"))).enqueue(new Callback<ResponseCheck>() {
            @Override
            public void onResponse(Call<ResponseCheck> call, Response<ResponseCheck> response) {
                if(response.isSuccessful()){
                    ResponseCheck body = response.body();
                    if(body.getMessage().equals("something")){
                        apiService.getTransaction(sharedPreferenceBase.getString("access"), sharedPreferenceBase.getString("id"), Integer.parseInt(sharedPreferenceBase.getString("transactionIndex"))).enqueue(new Callback<List<ResponseTransaction>>() {
                            @Override
                            public void onResponse(Call<List<ResponseTransaction>> call, Response<List<ResponseTransaction>> response) {
                                if(response.isSuccessful()){
                                    int index = Integer.parseInt(sharedPreferenceBase.getString("transactionIndex"));
                                    List<ResponseTransaction> resource = response.body();
                                    transactionAdapter.open();

                                    Log.d("transactionIndex", "first index: "+ index);
                                    //내부 DB에 저장 (Transaction)
                                    for(ResponseTransaction re : resource){
                                        index++;
                                        Log.d("TAG", re.getDate());
                                        int date = Integer.parseInt(re.getDate().replace("-",""));
                                        transactionAdapter.insertTransaction(re.getPGname(), date, re.getPurchasing_office(), re.getPurchasing_item(), re.getPrice());
                                        Log.d("TAG", re.getPGname()+" "+date+" "+re.getPurchasing_office()+" "+re.getPurchasing_item()+" "+re.getPrice());
                                    }

                                    Log.d("transactionIndex", "update index: "+ index);

                                    transactionAdapter.close();
                                    sharedPreferenceBase.setString("transactionIndex", Integer.toString(index));
                                    updateList();

                                    Toast.makeText(getContext(), "거래내역이 업데이트되었습니다.",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<List<ResponseTransaction>> call, Throwable t) {

                            }
                        });
                    } else{
                        Toast.makeText(getContext(), "업데이트할 내역이 없습니다.",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseCheck> call, Throwable t) {

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
            if(!itemList.contains(item)) {
                itemList.add(item);
                Log.d("TAG", "updateList: "+cursor.getString(1)+date+office+cursor.getString(4)+price);
            }
            cursor.moveToNext();
        }
        transactionAdapter.close();

        listAdapter = new ListAdapter(itemList);
        listView.setAdapter(listAdapter);
    }
}
