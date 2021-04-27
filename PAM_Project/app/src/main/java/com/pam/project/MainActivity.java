package com.pam.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView dateLabel;

    private Button calendarView;
    private Button allTasksView;

    private Intent allTasksIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateLabel = (TextView) findViewById(R.id.date);
        calendarView = (Button) findViewById(R.id.btnGoCalendar);
        allTasksView = (Button) findViewById(R.id.allTasks);

        allTasksIntent = new Intent(MainActivity.this, AllTasksActivity.class);

        Intent incomingIntent = getIntent();
        String date = incomingIntent.getStringExtra("date");
        dateLabel.setText(date);

        calendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        allTasksView.setOnClickListener((click) -> {
            MainActivity.this.startActivity(allTasksIntent);
        });
    }
}