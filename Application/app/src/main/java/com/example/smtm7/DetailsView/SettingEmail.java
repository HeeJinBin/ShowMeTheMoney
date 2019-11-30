package com.example.smtm7.DetailsView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.smtm7.Connection.NetworkHelper;
import com.example.smtm7.DataBase.DBEmailAdapter;
import com.example.smtm7.InitialSetting.EmailDialog;
import com.example.smtm7.InitialSetting.EmailItem;
import com.example.smtm7.R;

import java.util.ArrayList;

public class SettingEmail extends AppCompatActivity{

    private Button cancelButton;
    private ImageButton emailAddButton;

    private int count = 0;
    public static ListView listView;
    public static EmailSettingListAdapter listAdapter;
    public static ArrayList<EmailItem> itemList;
    public static ArrayList<EmailItem> initItemList;
    private DBEmailAdapter emailAdapter;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_email);

        itemList = new ArrayList<>();
        initItemList = new ArrayList<>();

        emailAdapter = new DBEmailAdapter(this);
        emailAdapter.open();
        final Cursor cursor = emailAdapter.searchAllEmail();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Log.d("Email", cursor.getString(0));
            EmailItem item = new EmailItem(cursor.getString(0),cursor.getString(1),cursor.getInt(2));
            initItemList.add(item);
            cursor.moveToNext();
            count++;
        }
        emailAdapter.close();

        cancelButton = findViewById(R.id.email_setting_cancel);
        emailAddButton = findViewById(R.id.btn_email_setting_plus);
        listView = findViewById(R.id.email_setting_list);

        listAdapter = new EmailSettingListAdapter(itemList);
        updateList();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<EmailItem> update = new ArrayList<>();
                for(int i = 0;i<itemList.size();i++){
                    boolean newEmail = false;

                    for(int j=0;j<initItemList.size();j++){
                        if(itemList.get(i).getEmail().equals(initItemList.get(j).getEmail())){
                            newEmail = true;
                            break;
                        }
                    }
                    //새로 추가된 이메일
                    if(!newEmail){
                        update.add(itemList.get(i));
                    }
                }

                //추가된 이메일 연동
                for(int i=0;i<update.size();i++){
                    emailAdapter.open();
                    emailAdapter.insertEmail(update.get(i).getEmail(),update.get(i).getPw(),0);
                    emailAdapter.close();
                }
                DetailsActivity.updateTransaction(getApplicationContext());
                finish();
            }
        });

        emailAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count<3) {
                    EmailDialog dialog = new EmailDialog(SettingEmail.this);
                    dialog.callFunction();
                    dialog.setDialogResult(new EmailDialog.OnMyDialogResult() {
                        @Override
                        public void finish(boolean result) {
                            // 이메일 연동을 한 경우
                            if (result) {
                                updateList();
                                count++;
                                if(count==3){
                                    emailAddButton.setClickable(false);
                                    emailAddButton.setSelected(false);
                                    emailAddButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.plus_button_click));
                                }
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "이메일은 최대 3개까지 연동 가능합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateList(){
        itemList.clear();
        emailAdapter.open();
        Cursor cursor = emailAdapter.searchAllEmail();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            EmailItem item = new EmailItem(cursor.getString(0),cursor.getString(1),cursor.getInt(2));
            itemList.add(item);
            cursor.moveToNext();
        }
        emailAdapter.close();

        listAdapter = new EmailSettingListAdapter(itemList);
        listView.setAdapter(listAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DetailsActivity.screenCheck = true;
    }
}
