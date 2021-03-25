package com.pam.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity {
    private static final String TAG = "CalendarActivity";
    private CalendarView mCalendarView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                // i2 = day, i1 = month , i = year
                String date = i2 + "/" + (i1 +1) + "/" + i; // i1 + 1 it returns the correct value of month e.g i1 in march would return 2 thus we need +1
                Log.d(TAG,"onSelectedDayChange: dd/mm/yyyy: " +date);

                Intent intent= new Intent(CalendarActivity.this, MainActivity.class);
                intent.putExtra("date",date);
                startActivity(intent);
            }
        });
    }
}
