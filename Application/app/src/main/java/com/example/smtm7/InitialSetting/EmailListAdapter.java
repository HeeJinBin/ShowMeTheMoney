package com.example.smtm7.InitialSetting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.smtm7.R;

import java.util.ArrayList;

public class EmailListAdapter extends BaseAdapter {
    LayoutInflater inflater = null;
    private ArrayList<EmailItem> data = null;
    private int count = 0;

    public EmailListAdapter(ArrayList<EmailItem> data){
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
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.item_email, parent, false);
        }

        TextView index = convertView.findViewById(R.id.email_index);
        TextView info = convertView.findViewById(R.id.email_info);

        index.setText(Integer.toString(position+1));
        info.setText(data.get(position).getEmail());

        return convertView;
    }
}
