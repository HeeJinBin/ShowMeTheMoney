package com.example.smtm7.DetailsView;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.smtm7.DataBase.DBEmailAdapter;
import com.example.smtm7.InitialSetting.EmailItem;
import com.example.smtm7.R;

import java.util.ArrayList;

import static com.example.smtm7.DetailsView.SettingEmail.itemList;
import static com.example.smtm7.DetailsView.SettingEmail.listAdapter;

public class EmailSettingListAdapter extends BaseAdapter {
    LayoutInflater inflater = null;
    private ArrayList<EmailItem> data = null;
    private int count = 0;

    public EmailSettingListAdapter(ArrayList<EmailItem> data){
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
    public View getView(final int position, View convertView, ViewGroup parent) {
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

        Button cancelButton = (Button)convertView.findViewById(R.id.btn_email_minus);
        final View finalConvertView = convertView;
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBEmailAdapter emailAdapter = new DBEmailAdapter(finalConvertView.getContext());
                emailAdapter.open();
                emailAdapter.deleteEmail(itemList.get(position).getEmail());

                itemList.clear();
                Cursor cursor = emailAdapter.searchAllEmail();
                cursor.moveToFirst();
                while(!cursor.isAfterLast()){
                    EmailItem item = new EmailItem(cursor.getString(0),cursor.getString(1),cursor.getInt(2));
                    itemList.add(item);
                    cursor.moveToNext();
                }
                listAdapter = new EmailSettingListAdapter(itemList);
                SettingEmail.listView.setAdapter(listAdapter);

                emailAdapter.close();
            }
        });

        return convertView;
    }
}
