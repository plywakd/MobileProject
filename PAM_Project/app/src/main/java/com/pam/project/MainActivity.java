package com.pam.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private  static final String TAG = "MainActivity";
    private TextView theDate;

    private Button btnGoCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        theDate = (TextView) findViewById(R.id.date);
        btnGoCalendar = (Button) findViewById(R.id.btnGoCalendar);

        Intent incomingIntent = getIntent();
        String date = incomingIntent.getStringExtra("date");
        theDate.setText(date);

        btnGoCalendar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });
    }
}