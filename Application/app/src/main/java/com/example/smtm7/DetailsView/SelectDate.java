package com.example.smtm7.DetailsView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smtm7.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;
import java.util.Date;

public class SelectDate extends AppCompatActivity {

    private Button cancelButton;
    private Button searchButton;
    private DatePicker firstDatePicker;
    private DatePicker secondDatePicker;

    private String dateText1;
    private String dateText2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);

        DatePicker.OnDateChangedListener firstListener = new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateText1 = year + "-" + (monthOfYear + 1) + "-" + String.format("%02d",dayOfMonth);
            }
        };

        DatePicker.OnDateChangedListener secondListener = new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateText2 = year + "-" + (monthOfYear + 1) + "-" + String.format("%02d",dayOfMonth);
            }
        };

        dateText1 = CalendarDay.today().getYear() + "-" + (CalendarDay.today().getMonth() + 1) + "-" + String.format("%02d",CalendarDay.today().getDay());
        dateText2 = dateText1;

        cancelButton = findViewById(R.id.btn_date_cancel);
        searchButton = findViewById(R.id.btn_date_search);

        firstDatePicker = findViewById(R.id.first_date_picker);
        firstDatePicker.init(CalendarDay.today().getYear(), CalendarDay.today().getMonth(), CalendarDay.today().getDay(), firstListener);

        secondDatePicker = findViewById(R.id.second_date_picker);
        secondDatePicker.init(CalendarDay.today().getYear(), CalendarDay.today().getMonth(), CalendarDay.today().getDay(), secondListener);

        //취소
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day1 = Integer.parseInt(dateText1.replace("-", ""));
                int day2 = Integer.parseInt(dateText2.replace("-", ""));

                if (day1 <= day2) {
                    Intent intent = new Intent();
                    intent.putExtra("Check", "False");
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(SelectDate.this, "올바른 날짜 범위가 아닙니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //검색
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("Check", "True");
                intent.putExtra("First", dateText1);
                intent.putExtra("Second", dateText2);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        DetailsActivity.screenCheck = true;
    }
}
