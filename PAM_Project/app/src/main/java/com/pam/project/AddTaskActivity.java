package com.pam.project;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddTaskActivity extends AppCompatActivity {

    private Spinner optionSpinner;
    private String repeatOption;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task_layout);
        optionSpinner = (Spinner) findViewById(R.id.repeatSpinner);

        optionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                TODO implement repeat option assignment
                repeatOption = optionSpinner.getSelectedItem().toString();
                System.out.println("Option: " + repeatOption);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                TODO if needed
            }
        });
    }
}
