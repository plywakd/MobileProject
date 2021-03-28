package com.pam.project;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity {
    private static final String TAG = "CalendarActivity";
    public ArrayList<TodoTask> tasks;
    private CalendarView mCalendarView;
    private ListView todoTasksView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        todoTasksView = (ListView) findViewById(R.id.todoTasks);

//        TESTING of tasks TODO implement it right
        tasks = new ArrayList<>();
        tasks.add(new TodoTask("Test of todo task", LocalDate.now()));
        tasks.add(new TodoTask("Test of todo task show 2", LocalDate.now()));

        ArrayAdapter<TodoTask> arrayAdapter = new ArrayAdapter<TodoTask>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                tasks);

        todoTasksView.setAdapter(arrayAdapter);

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                String date = day + "/" + (month + 1) + "/" + year;
                Log.d(TAG, "onSelectedDayChange: dd/mm/yyyy: " + date);

                Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });
    }
}
