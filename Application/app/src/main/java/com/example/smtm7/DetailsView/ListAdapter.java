package com.example.smtm7.DetailsView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.smtm7.R;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    LayoutInflater inflater = null;
    private ArrayList<TransactionItem> data = null;
    private int count = 0;

    public ListAdapter(ArrayList<TransactionItem> data){
        this.data = data;
        count = data.size();
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            final Context context = parent.getContext();
            if(inflater == null){
                inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.search_item_transaction, parent,false);
        }
        TextView item = convertView.findViewById(R.id.tv_item);
        TextView date = convertView.findViewById(R.id.tv_date_item);
        TextView office = convertView.findViewById(R.id.tv_office_item);
        TextView pg = convertView.findViewById(R.id.tv_pg_item);
        TextView price = convertView.findViewById(R.id.tv_price_item);

        item.setText(data.get(position).getItem());
        date.setText(data.get(position).getDate());
        office.setText(data.get(position).getOffice());
        pg.setText(data.get(position).getPg());
        price.setText(data.get(position).getPrice());

        return convertView;
    }
}
